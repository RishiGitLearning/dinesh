/*
 * frmChangePassword.java
 *
 * Created on July 3, 2004, 3:36 PM
 */
package EITLERP.FeltSales.Budget;

import EITLERP.*;
import EITLERP.FeltSales.PieceRegister.clsIncharge;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
//import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
//import EITLERP.Sales.clsExcelExporter;

/*<APPLET CODE=frmChangePassword HEIGHT=200 WIDTH=430></APPLET>*/
/**
 *
 * @author Daxesh Prajapati
 */
public class rptBudgetSales2021 extends javax.swing.JApplet {

    private EITLTableModel DataModel = new EITLTableModel();

    private EITLComboModel modelDept = new EITLComboModel();
    private EITLComboModel modelShift = new EITLComboModel();
    private EITLComboModel modelMainCategory = new EITLComboModel();
    private EITLComboModel modelCategory = new EITLComboModel();
    private EITLComboModel cmbIncharge = new EITLComboModel();
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    //GenerateInvoiceParameterModificationCombo();
    /**
     * Initializes the applet frmChangePassword
     */
    public void init() {
        initComponents();
        setSize(1000, 750);

        GenerateCombo();

        jLabel1.setForeground(Color.WHITE);
        txtpartyname.setEnabled(false);

        FormatGridZAS();

//        String s = "";
//        s = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL, "
//                + "(SELECT D.MM_UPN_NO,D.MM_PARTY_CODE AS PARTY_CODE,CONCAT(PARTY_NAME,', ',CITY_ID) AS PARTY_NAME,D.MM_MACHINE_NO AS MACHINE_NO,MM_MACHINE_POSITION,POSITION_DESC,MM_FELT_STYLE,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,(MM_FELT_LENGTH*MM_FELT_WIDTH*MM_FELT_GSM/1000) AS MM_TH_WEIGHT,MM_FELT_LENGTH*MM_FELT_WIDTH AS AREA_SQMTR,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,(MM_FABRIC_LENGTH*MM_FABRIC_WIDTH*MM_FELT_GSM/1000) AS MM_FABRIC_TH_WEIGHT,MM_ITEM_CODE,GROUP_NAME,CASE WHEN WT_RATE =0 THEN SQM_CHRG ELSE WT_RATE END  AS FELT_RATE ,INCHARGE_CD FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,  PRODUCTION.FELT_MACHINE_MASTER_DETAIL D, PRODUCTION.FELT_QLT_RATE_MASTER R, DINESHMILLS.D_SAL_PARTY_MASTER P , PRODUCTION.FELT_MACHINE_POSITION_MST MP WHERE H.MM_DOC_NO=D.MM_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0  AND MM_ITEM_CODE = PRODUCT_CODE  AND EFFECTIVE_TO ='0000-00-00'  AND P.PARTY_CODE = H.MM_PARTY_CODE AND POSITION_NO+0 = MM_MACHINE_POSITION+0 AND COALESCE(POSITION_CLOSE_IND,0) !=1 AND COALESCE(PARTY_CLOSE_IND,0) !=1 "
//                + ") AS M "
//                + "SET STYLE = MM_FELT_STYLE, "
//                + "PRESS_LENGTH = MM_FELT_LENGTH, "
//                + "PRESS_WIDTH = MM_FELT_WIDTH, "
//                + "PRESS_WEIGHT = MM_TH_WEIGHT, "
//                + "PRESS_GSM = MM_FELT_GSM, "
//                + "PRESS_SQMTR = AREA_SQMTR, "
//                + "DRY_LENGTH =MM_FABRIC_LENGTH, "
//                + "DRY_WIDTH =MM_FABRIC_WIDTH, "
//                + "DRY_WEIGHT = MM_FABRIC_TH_WEIGHT, "
//                + "DRY_SQMTR = MM_SIZE_M2 "
//                + "WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021  AND UPN = MM_UPN_NO ";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL D, "
//                + "( "
//                + "SELECT H.MM_PARTY_CODE,H.MM_MACHINE_NO,MM_MACHINE_POSITION,COALESCE(MM_AVG_LIFE,0) AS M,ceiling((340/COALESCE(MM_AVG_LIFE,0))) AS V,MM_FELT_VALUE_WITH_GST AS G FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
//                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND H.APPROVED =1 AND H.CANCELED =0) AS C "
//                + "SET AVG_LIFE  = M, POTENTIAL = V,SELLING_PRICE = ROUND(G,0)  WHERE YEAR_FROM =2020 AND YEAR_TO = 2021 "
//                + "AND MM_PARTY_CODE = PARTY_CODE AND MACHINE_NO+0 = MM_MACHINE_NO+0 AND MM_MACHINE_POSITION+0 = POSITION_NO+0";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET Q4NET_AMOUNT = ROUND(Q4*ROUND(SELLING_PRICE,0),0) WHERE YEAR_FROM =2020 AND YEAR_TO = 2021";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET Q4KG = (PRESS_WEIGHT+ DRY_WEIGHT) WHERE YEAR_FROM =2020 AND YEAR_TO = 2021";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET Q4SQMTR= (PRESS_SQMTR + DRY_SQMTR) WHERE YEAR_FROM =2020 AND YEAR_TO = 2021";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET TOTAL_KG = Q4 * Q4KG WHERE YEAR_FROM =2020 AND YEAR_TO = 2021";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET TOTAL_SQMTR = Q4 * Q4SQMTR WHERE YEAR_FROM =2020 AND YEAR_TO = 2021";
//        data.Execute(s);

        String s = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET Q4NET_AMOUNT = NET_AMOUNT WHERE YEAR_FROM =2020 AND YEAR_TO = 2021";
        data.Execute(s);
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ExporttoExcelFileChooser = new javax.swing.JFileChooser();
        file1 = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TabList = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnZASView = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnZPMView = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnZPMPView = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnZPrtView = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnZPPView = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnZPView = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnZGView = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnPrdwiseView = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnGrpwiseView = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane25 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        btnEmpMstETE = new javax.swing.JButton();
        cmbFY = new javax.swing.JComboBox();
        lblMonthCmb1 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        INCHARGE = new javax.swing.JComboBox();
        jLabel35 = new javax.swing.JLabel();
        txtproductcode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtpartycode = new javax.swing.JTextField();
        txtpartyname = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Budget Sales Report for Year 2020-2021");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 2, 1000, 25);

        jLabel2.setBackground(new java.awt.Color(0, 102, 153));
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 90, 1000, 10);

        TabList.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabListStateChanged(evt);
            }
        });

        jPanel1.setLayout(null);

        btnZASView.setText("View");
        btnZASView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZASViewActionPerformed(evt);
            }
        });
        jPanel1.add(btnZASView);
        btnZASView.setBounds(860, 0, 100, 30);

        TabList.addTab("Zonewise Approval Status", jPanel1);

        jPanel2.setLayout(null);

        btnZPMView.setText("View");
        btnZPMView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZPMViewActionPerformed(evt);
            }
        });
        jPanel2.add(btnZPMView);
        btnZPMView.setBounds(860, 0, 100, 30);

        TabList.addTab("Zonewise Productwise Meterwise", jPanel2);

        jPanel3.setLayout(null);

        btnZPMPView.setText("View");
        btnZPMPView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZPMPViewActionPerformed(evt);
            }
        });
        jPanel3.add(btnZPMPView);
        btnZPMPView.setBounds(860, 0, 100, 30);

        TabList.addTab("Zonewise Productwise M/CWise Positionwise", jPanel3);

        jPanel4.setLayout(null);

        btnZPrtView.setText("View");
        btnZPrtView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZPrtViewActionPerformed(evt);
            }
        });
        jPanel4.add(btnZPrtView);
        btnZPrtView.setBounds(860, 0, 100, 30);

        TabList.addTab("Zonewise Partywise", jPanel4);

        jPanel5.setLayout(null);

        btnZPPView.setText("View");
        btnZPPView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZPPViewActionPerformed(evt);
            }
        });
        jPanel5.add(btnZPPView);
        btnZPPView.setBounds(860, 0, 100, 30);

        TabList.addTab("Zonewise Partywise Productwise", jPanel5);

        jPanel6.setLayout(null);

        btnZPView.setText("View");
        btnZPView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZPViewActionPerformed(evt);
            }
        });
        jPanel6.add(btnZPView);
        btnZPView.setBounds(860, 0, 100, 30);

        TabList.addTab("Zonewise Productwise", jPanel6);

        jPanel7.setLayout(null);

        btnZGView.setText("View");
        btnZGView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZGViewActionPerformed(evt);
            }
        });
        jPanel7.add(btnZGView);
        btnZGView.setBounds(860, 0, 100, 30);

        TabList.addTab("Zonewise Groupwise", jPanel7);

        jPanel8.setLayout(null);

        btnPrdwiseView.setText("View");
        btnPrdwiseView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrdwiseViewActionPerformed(evt);
            }
        });
        jPanel8.add(btnPrdwiseView);
        btnPrdwiseView.setBounds(860, 0, 100, 30);

        TabList.addTab("Productwise", jPanel8);

        jPanel9.setLayout(null);

        btnGrpwiseView.setText("View");
        btnGrpwiseView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrpwiseViewActionPerformed(evt);
            }
        });
        jPanel9.add(btnGrpwiseView);
        btnGrpwiseView.setBounds(860, 0, 100, 30);

        TabList.addTab("Groupwise", jPanel9);

        getContentPane().add(TabList);
        TabList.setBounds(10, 103, 980, 110);

        btnClear.setText("Clear All");
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear);
        btnClear.setBounds(880, 50, 110, 30);

        Table.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane25.setViewportView(Table);

        getContentPane().add(jScrollPane25);
        jScrollPane25.setBounds(10, 220, 980, 320);

        btnEmpMstETE.setLabel("Export to Excel");
        btnEmpMstETE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpMstETEActionPerformed(evt);
            }
        });
        getContentPane().add(btnEmpMstETE);
        btnEmpMstETE.setBounds(830, 550, 150, 30);

        cmbFY.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2020-2021" }));
        getContentPane().add(cmbFY);
        cmbFY.setBounds(130, 30, 170, 20);

        lblMonthCmb1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMonthCmb1.setText("Financial Year : ");
        getContentPane().add(lblMonthCmb1);
        lblMonthCmb1.setBounds(0, 30, 120, 20);

        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel86.setText("Status : ");
        getContentPane().add(jLabel86);
        jLabel86.setBounds(60, 60, 60, 20);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Approved", "Unapproved", "Cancelled" }));
        cmbStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbStatusItemStateChanged(evt);
            }
        });
        getContentPane().add(cmbStatus);
        cmbStatus.setBounds(130, 60, 150, 20);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Zone : ");
        getContentPane().add(jLabel26);
        jLabel26.setBounds(340, 60, 60, 20);

        INCHARGE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Sales Eng 1", "Sales Eng 2", "Sales Eng 3" }));
        getContentPane().add(INCHARGE);
        INCHARGE.setBounds(410, 60, 140, 20);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Product Code : ");
        getContentPane().add(jLabel35);
        jLabel35.setBounds(560, 60, 110, 20);

        txtproductcode.setToolTipText("Press F! key to search Product Code");
        txtproductcode = new JTextFieldHint(new JTextField(),"Search By Press F1");
        txtproductcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtproductcodeActionPerformed(evt);
            }
        });
        txtproductcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtproductcodeKeyPressed(evt);
            }
        });
        getContentPane().add(txtproductcode);
        txtproductcode.setBounds(680, 60, 170, 20);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Party Code : ");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(310, 30, 90, 20);

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
        txtpartycode.setBounds(410, 30, 70, 20);

        txtpartyname.setDisabledTextColor(java.awt.Color.black);
        txtpartyname = new JTextFieldHint(new JTextField(),"Party Name");
        txtpartyname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpartynameActionPerformed(evt);
            }
        });
        getContentPane().add(txtpartyname);
        txtpartyname.setBounds(480, 30, 370, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        cmbFY.setSelectedIndex(0);
    }//GEN-LAST:event_btnClearActionPerformed


    private void btnEmpMstETEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpMstETEActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(Table, new File(file1.getSelectedFile().toString() + ".xls"), "Sheet1");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnEmpMstETEActionPerformed

    private void btnZASViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZASViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateZAS();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnZASViewActionPerformed

    private void TabListStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabListStateChanged
        // TODO add your handling code here:
        if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Approval Status")) {
            FormatGridZAS();
            cmbStatus.setEnabled(false);
            INCHARGE.setEnabled(false);
            txtpartycode.setEnabled(false);
            txtproductcode.setEnabled(false);
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Productwise Meterwise")) {
            FormatGridZPM();
            cmbStatus.setEnabled(true);
            INCHARGE.setEnabled(false);
            txtpartycode.setEnabled(false);
            txtproductcode.setEnabled(false);
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Productwise M/CWise Positionwise")) {
            FormatGridZPMP();
            cmbStatus.setEnabled(true);
            INCHARGE.setEnabled(true);
            txtpartycode.setEnabled(true);
            txtproductcode.setEnabled(true);
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Partywise")) {
            FormatGridZParty();
            cmbStatus.setEnabled(true);
            INCHARGE.setEnabled(true);
            txtpartycode.setEnabled(false);
            txtproductcode.setEnabled(false);
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Partywise Productwise")) {
            FormatGridZPP();
            cmbStatus.setEnabled(true);
            INCHARGE.setEnabled(true);
            txtpartycode.setEnabled(true);
            txtproductcode.setEnabled(true);
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Productwise")) {
            FormatGridZP();
            cmbStatus.setEnabled(true);
            INCHARGE.setEnabled(true);
            txtpartycode.setEnabled(false);
            txtproductcode.setEnabled(true);
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Groupwise")) {
            FormatGridZG();
            cmbStatus.setEnabled(true);
            INCHARGE.setEnabled(true);
            txtpartycode.setEnabled(false);
            txtproductcode.setEnabled(false);
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Productwise")) {
            FormatGridProductwise();
            cmbStatus.setEnabled(true);
            INCHARGE.setEnabled(false);
            txtpartycode.setEnabled(false);
            txtproductcode.setEnabled(false);
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Groupwise")) {
            FormatGridGroupwise();
            cmbStatus.setEnabled(true);
            INCHARGE.setEnabled(false);
            txtpartycode.setEnabled(false);
            txtproductcode.setEnabled(false);
        }
    }//GEN-LAST:event_TabListStateChanged

    private void btnZPMViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZPMViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateZPM();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnZPMViewActionPerformed

    private void btnZPMPViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZPMPViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateZPMP();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnZPMPViewActionPerformed

    private void btnZPrtViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZPrtViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateZParty();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnZPrtViewActionPerformed

    private void btnZPPViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZPPViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateZPP();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnZPPViewActionPerformed

    private void btnZPViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZPViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateZP();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnZPViewActionPerformed

    private void btnZGViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZGViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateZG();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnZGViewActionPerformed

    private void btnPrdwiseViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrdwiseViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateProductwise();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnPrdwiseViewActionPerformed

    private void btnGrpwiseViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrpwiseViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateGroupwise();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnGrpwiseViewActionPerformed

    private void cmbStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbStatusItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusItemStateChanged

    private void txtproductcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtproductcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtproductcodeActionPerformed

    private void txtproductcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtproductcodeKeyPressed

        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            //            aList.SQL = "SELECT SUBSTRING(ITEM_CODE,1,6) AS ITEM_CODE,ITEM_DESC,GRUP FROM PRODUCTION.FELT_RATE_MASTER ORDER BY ITEM_CODE ";
//            aList.SQL = "SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC,GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER ORDER BY PRODUCT_CODE ";
            aList.SQL = "SELECT DISTINCT QUALITY_NO,GROUP_NAME FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 ";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            if (aList.ShowLOV()) {
                //txtpartycode.setText(aList.ReturnVal);
                //txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                //txtproductcode.setText(aList.ReturnVal);
                txtproductcode.setText(txtproductcode.getText() + aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtproductcodeKeyPressed

    private void txtpartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtpartycodeFocusLost
        // TODO add your handling code here:
        if (!txtpartycode.getText().trim().equals("") && data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + txtpartycode.getText().trim() + "' AND MAIN_ACCOUNT_CODE=210010 ")) {
            txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, txtpartycode.getText()));

        } else {
            if (!txtpartycode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Party Code doesn't exist/under approval.");
            }
            txtpartycode.setText("");
            txtpartyname.setText("");
        }
    }//GEN-LAST:event_txtpartycodeFocusLost

    private void txtpartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpartycodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            //            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_CLOSE_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 ";
//            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_CLOSE_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0";
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 ";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtpartycode.setText(aList.ReturnVal);
                txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtpartycodeKeyPressed

    private void txtpartynameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpartynameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpartynameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser ExporttoExcelFileChooser;
    private javax.swing.JComboBox INCHARGE;
    private javax.swing.JTabbedPane TabList;
    private javax.swing.JTable Table;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEmpMstETE;
    private javax.swing.JButton btnGrpwiseView;
    private javax.swing.JButton btnPrdwiseView;
    private javax.swing.JButton btnZASView;
    private javax.swing.JButton btnZGView;
    private javax.swing.JButton btnZPMPView;
    private javax.swing.JButton btnZPMView;
    private javax.swing.JButton btnZPPView;
    private javax.swing.JButton btnZPView;
    private javax.swing.JButton btnZPrtView;
    private javax.swing.JComboBox cmbFY;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JLabel lblMonthCmb1;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JTextField txtpartyname;
    private javax.swing.JTextField txtproductcode;
    // End of variables declaration//GEN-END:variables

    private void GenerateCombo() {

        HashMap List = new HashMap();
        clsIncharge ObjIncharge;

        INCHARGE.setModel(cmbIncharge);
        cmbIncharge.removeAllElements();  //Clearing previous contents

        List = clsIncharge.getIncgargeList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjIncharge = (clsIncharge) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Text = (String) ObjIncharge.getAttribute("INCHARGE_NAME").getObj();
            aData.Code = (long) ObjIncharge.getAttribute("INCHARGE_CD").getVal();
            cmbIncharge.addElement(aData);
        }

    }

    private void FormatGridZAS() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

//        DataModel.addColumn("Sr.");
//        DataModel.addColumn("INCHARGE");
//        DataModel.addColumn("ZONE");
//        DataModel.addColumn("TOTAL");
//        DataModel.addColumn("FINAL_APPROVED");
//        DataModel.addColumn("CANCELLED");
//        DataModel.addColumn("UNDER_APPROVED");
//        DataModel.addColumn("CREATOR");
////        DataModel.addColumn("APPROVER");
//        DataModel.addColumn("FINAL_APPROVER");
//        DataModel.addColumn("RunTime");
        DataModel.addColumn("Sr.");
        DataModel.addColumn("Incharge");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Yet to Proposed");
        DataModel.addColumn("Proposed");
        DataModel.addColumn("Final Approved");
        DataModel.addColumn("Cancelled");
        DataModel.addColumn("Total");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZAS() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridZAS(); //clear existing content of table
            ResultSet rsTmp;

            String strSQL = "";

            strSQL = "SELECT INCHARGE,INCHARGE_NAME,COUNT(*) AS TOTAL , "
                    + "SUM(COALESCE(CASE WHEN APPROVED =1 AND CANCELED =0 THEN 1 ELSE 0 END,0)) AS FINAL_APPROVED, "
                    + "SUM(COALESCE(CASE WHEN CANCELED =1 AND APPROVED =1 THEN 1 ELSE 0 END,0)) AS CANCELLED, "
                    + "SUM(COALESCE(CASE WHEN CANCELED =0 AND APPROVED =0 THEN 1 ELSE 0 END,0)) AS UNDER_APPROVED, "
                    //                    + "SUM(CREATOR) AS CREATOR,SUM(APPROVER) AS APPROVER,SUM(FINALAPPROVER) AS FINAL_APPROVER "
                    + "SUM(CREATOR) AS CREATOR,SUM(FINALAPPROVER) AS FINAL_APPROVER "
                    + "FROM "
                    //                    + "(SELECT INCHARGE,INCHARGE_NAME,V.DOC_NO,APPROVED,CANCELED,CREATOR,FINALAPPROVER,APPROVER FROM "
                    + "(SELECT INCHARGE,INCHARGE_NAME,V.DOC_NO,APPROVED,CANCELED,CREATOR,FINALAPPROVER FROM "
                    + "(SELECT DISTINCT INCHARGE,INCHARGE_NAME,DOC_NO,COALESCE(APPROVED,0) AS APPROVED ,COALESCE(CANCELED,0) AS CANCELED FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + ") AS V "
                    + "LEFT JOIN "
                    + "(SELECT DOC_NO, "
                    + "CASE WHEN USER_ID IN (329,318,361,352,331,280) THEN 1 ELSE 0 END AS CREATOR, "
                    //                    + "CASE WHEN USER_ID IN (28) THEN 1 ELSE 0 END AS APPROVER, "
                    + "CASE WHEN USER_ID IN (28,36) THEN 1 ELSE 0 END AS FINALAPPROVER "
                    //                    + "CASE WHEN USER_ID IN (98) THEN 1 ELSE 0 END AS FINALAPPROVER "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID = 768 AND DOC_DATE >='2019-12-19' AND STATUS = 'W')  AS D "
                    + "ON D.DOC_NO = V.DOC_NO) AS M "
                    + "GROUP BY INCHARGE,INCHARGE_NAME "
                    + ""
                    + "UNION ALL "
                    + ""
                    + "SELECT CONVERT('GRAND' USING UTF8) AS INCHARGE,CONVERT('TOTAL' USING UTF8) AS INCHARGE_NAME,COUNT(*) AS TOTAL , "
                    + "SUM(COALESCE(CASE WHEN APPROVED =1 AND CANCELED =0 THEN 1 ELSE 0 END,0)) AS FINAL_APPROVED, "
                    + "SUM(COALESCE(CASE WHEN CANCELED =1 AND APPROVED =1 THEN 1 ELSE 0 END,0)) AS CANCELLED, "
                    + "SUM(COALESCE(CASE WHEN CANCELED =0 AND APPROVED =0 THEN 1 ELSE 0 END,0)) AS UNDER_APPROVED, "
                    //                    + "SUM(CREATOR) AS CREATOR,SUM(APPROVER) AS APPROVER,SUM(FINALAPPROVER) AS FINAL_APPROVER "
                    + "SUM(CREATOR) AS CREATOR,SUM(FINALAPPROVER) AS FINAL_APPROVER "
                    + "FROM "
                    //                    + "(SELECT INCHARGE,INCHARGE_NAME,V.DOC_NO,APPROVED,CANCELED,CREATOR,FINALAPPROVER,APPROVER FROM "
                    + "(SELECT INCHARGE,INCHARGE_NAME,V.DOC_NO,APPROVED,CANCELED,CREATOR,FINALAPPROVER FROM "
                    + "(SELECT DISTINCT INCHARGE,INCHARGE_NAME,DOC_NO,COALESCE(APPROVED,0) AS APPROVED ,COALESCE(CANCELED,0) AS CANCELED FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + ") AS V "
                    + "LEFT JOIN "
                    + "(SELECT DOC_NO, "
                    + "CASE WHEN USER_ID IN (329,318,361,352,331,280) THEN 1 ELSE 0 END AS CREATOR, "
                    //                    + "CASE WHEN USER_ID IN (28) THEN 1 ELSE 0 END AS APPROVER, "
                    + "CASE WHEN USER_ID IN (28,36) THEN 1 ELSE 0 END AS FINALAPPROVER "
                    //                    + "CASE WHEN USER_ID IN (98) THEN 1 ELSE 0 END AS FINALAPPROVER "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID = 768 AND DOC_DATE >='2019-12-19' AND STATUS = 'W')  AS D "
                    + "ON D.DOC_NO = V.DOC_NO) AS M ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

//                    rowData[0] = Integer.toString(cnt);
//                    rowData[1] = rsTmp.getString("INCHARGE");
//                    rowData[2] = rsTmp.getString("INCHARGE_NAME");
//                    rowData[3] = rsTmp.getString("TOTAL");
//                    rowData[4] = rsTmp.getString("FINAL_APPROVED");
//                    rowData[5] = rsTmp.getString("CANCELLED");
//                    rowData[6] = rsTmp.getString("UNDER_APPROVED");
//                    rowData[7] = rsTmp.getString("CREATOR");
////                    rowData[8] = rsTmp.getString("APPROVER");
//                    rowData[8] = rsTmp.getString("FINAL_APPROVER");
//                    rowData[9] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE");
                    rowData[2] = rsTmp.getString("INCHARGE_NAME");
                    rowData[3] = rsTmp.getString("CREATOR");
                    rowData[4] = rsTmp.getString("FINAL_APPROVER");
                    rowData[5] = rsTmp.getString("FINAL_APPROVED");
                    rowData[6] = rsTmp.getString("CANCELLED");
                    rowData[7] = rsTmp.getString("TOTAL");
                    rowData[8] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void FormatGridZPM() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");

        DataModel.addColumn("PROD GROUP");
        DataModel.addColumn("SIZE CRITERIA");
        DataModel.addColumn("ACNE Capacity");
        DataModel.addColumn("ACNE Actual");
        DataModel.addColumn("EASTWEST Capacity");
        DataModel.addColumn("EASTWEST Actual");
        DataModel.addColumn("KEYCLIENT Capacity");
        DataModel.addColumn("KEYCLIENT Actual");
        DataModel.addColumn("NORTH Capacity");
        DataModel.addColumn("NORTH Actual");
        DataModel.addColumn("SOUTH Capacity");
        DataModel.addColumn("SOUTH Actual");
        DataModel.addColumn("Total Capacity");
        DataModel.addColumn("Total Actual");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZPM() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridZPM(); //clear existing content of table
            ResultSet rsTmp;

            int status = cmbStatus.getSelectedIndex();
            if (status == 0) {
                cndtn += " AND COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 1) {
                cndtn += " AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 2) {
                cndtn += " AND COALESCE(CANCELED,0)=1 ";
            }

            String strSQL = "";

            String aQry = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET SIZE_CRITERIA = CASE WHEN PRESS_LENGTH <=18.99 THEN 'BELOW 18.99' "
                    + "WHEN PRESS_LENGTH >= 19 AND PRESS_LENGTH <=25.50 THEN '19.00 TO 25.50 MTRS' "
                    + "WHEN PRESS_LENGTH >= 25.51 AND PRESS_LENGTH <=33.50 THEN '25.60 TO 33.50 MTRS' "
                    + "WHEN PRESS_LENGTH >= 33.51 AND PRESS_LENGTH <=46.50 THEN '33.60 TO 46.50 MTRS' "
                    + "WHEN PRESS_LENGTH >= 46.51 AND PRESS_LENGTH <=500 THEN 'ABOVE  46.50' END "
                    + "WHERE  YEAR_FROM = 2020 AND YEAR_TO = 2021 AND GROUP_NAME IN ('ACNE','FCNE')";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET SIZE_CRITERIA = CASE WHEN PRESS_LENGTH <=7.99 THEN 'BELOW 8' "
                    + "WHEN PRESS_LENGTH >= 8 AND PRESS_LENGTH <=13.20 THEN '08.00 TO 13.20 MTRS' "
                    + "WHEN PRESS_LENGTH >= 13.21 AND PRESS_LENGTH <=18.00 THEN '13.30 TO 18.00 MTRS' "
                    + "WHEN PRESS_LENGTH >= 18.01 AND PRESS_LENGTH <=46.00 THEN '18.10 TO 46.00 MTRS' "
                    + "WHEN PRESS_LENGTH >= 46.01 AND PRESS_LENGTH <=68 THEN '46.10 TO 68.00 MTRS' "
                    + "WHEN PRESS_LENGTH >= 68 AND PRESS_LENGTH <=500 THEN 'ABOVE 68.01 MTRS' END "
                    + "WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 AND GROUP_NAME NOT IN ('HDS','ACNE','SDF','FCNE')";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET SIZE_CRITERIA = 'HDS' "
                    + "WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 AND GROUP_NAME IN ('HDS')";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_DETAIL SET SIZE_CRITERIA = 'SDF' "
                    + "WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 AND GROUP_NAME IN ('SDF')";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "TRUNCATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

//            aQry = "INSERT INTO PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY "
//                    + "SELECT * FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY WHERE LOGIN_ID =47  AND REQUESTED_MONTH ='' "
//                    + "ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE+0 ";            
            aQry = "INSERT INTO PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY "
                    + "SELECT * FROM PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY_MONTHLY WHERE LOGIN_ID =47  AND REQUESTED_MONTH ='' "
                    + "ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE+0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET ACNE_ACTUAL = 0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET EXPORT_ACTUAL =0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET NORTH_ACTUAL =0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET SOUTH_ACTUAL=0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET EAST_ACTUAL =0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET KEY_ACTUAL =0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET TOTAL_ACTUAL =0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET ACNE_CAPACITY = ACNE_CAPACITY*12";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET EXPORT_CAPACITY =0";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET NORTH_CAPACITY =NORTH_CAPACITY*12";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET SOUTH_CAPACITY =SOUTH_CAPACITY*12";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET EAST_CAPACITY =EAST_CAPACITY*12";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET KEY_CAPACITY =KEY_CAPACITY*12";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET TOTAL_CAPACITY =TOTAL_CAPACITY*12";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY SET TOTAL_CAPACITY =ACNE_CAPACITY+ NORTH_CAPACITY+SOUTH_CAPACITY+EAST_CAPACITY + KEY_CAPACITY ";
            //System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY,"
                    + "(SELECT PROD_GROUP,SIZE_CRITERIA, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 5 THEN QTY ELSE 0 END,0)) AS ACNE, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 3 THEN QTY ELSE 0 END,0)) AS EASTWEST, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 7 THEN QTY ELSE 0 END,0)) AS KEYCLIENT, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 2 THEN QTY ELSE 0 END,0)) AS NORTH, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 1 THEN QTY ELSE 0 END,0)) AS SOUTH, "
                    + "SUM(QTY) AS GRAND "
                    + "FROM "
                    + "(SELECT CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN '1. ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN '3. HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN '4. SDF' "
                    + "ELSE '2. MNE/MNG' END USING UTF8) AS PROD_GROUP, SIZE_CRITERIA,COALESCE(Q4,0) AS QTY,INCHARGE,INCHARGE_NAME "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD " + cndtn + " ) AS V "
                    + "GROUP BY PROD_GROUP,SIZE_CRITERIA "
                    + ") AS M "
                    + "SET ACNE_ACTUAL = ACNE, NORTH_ACTUAL = NORTH,SOUTH_ACTUAL = SOUTH,EAST_ACTUAL= EASTWEST,KEY_ACTUAL= KEYCLIENT,TOTAL_ACTUAL= GRAND "
                    + "WHERE PRODUCT_CAPTION= PROD_GROUP "
                    + "AND SIZE_CRITERIA = MTR_CAPTION";
//            System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY,(SELECT PROD_GROUP, "
                    + "CONVERT(CASE WHEN PROD_GROUP = '1. ACNE' THEN ' SUB TOTAL ' "
                    + "WHEN PROD_GROUP = '2. MNE/MNG' THEN ' SUB TOTAL ' "
                    + "WHEN PROD_GROUP = '3. HDS' THEN 'HDS' "
                    + "WHEN PROD_GROUP = '4. SDF' THEN 'SDF' END USING UTF8) AS SIZE_CRITERIA, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 5 THEN QTY ELSE 0 END,0)) AS ACNE, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 3 THEN QTY ELSE 0 END,0)) AS EASTWEST, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 7 THEN QTY ELSE 0 END,0)) AS KEYCLIENT, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 2 THEN QTY ELSE 0 END,0)) AS NORTH, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 1 THEN QTY ELSE 0 END,0)) AS SOUTH, "
                    + "SUM(QTY) AS GRAND "
                    + "FROM "
                    + "(SELECT "
                    + "CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN '1. ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN '3. HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN '4. SDF' "
                    + "ELSE '2. MNE/MNG' END USING UTF8)AS PROD_GROUP, SIZE_CRITERIA,COALESCE(Q4,0) AS QTY,INCHARGE,INCHARGE_NAME "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD " + cndtn + " ) AS V "
                    + "GROUP BY PROD_GROUP "
                    + ") AS M "
                    + "SET ACNE_ACTUAL = ACNE, NORTH_ACTUAL = NORTH,SOUTH_ACTUAL = SOUTH,EAST_ACTUAL= EASTWEST,KEY_ACTUAL= KEYCLIENT,TOTAL_ACTUAL= GRAND "
                    + "WHERE PRODUCT_CAPTION= PROD_GROUP "
                    + "AND SIZE_CRITERIA = MTR_CAPTION";
//            System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            aQry = "UPDATE PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY,( "
                    + "SELECT CONVERT('5. GRAND TOTAL' USING UTF8) AS PROD_GROUP,CONVERT('GRAND TOTAL ' USING UTF8) AS   SIZE_CRITERIA, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 5 THEN QTY ELSE 0 END,0)) AS ACNE, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 3 THEN QTY ELSE 0 END,0)) AS EASTWEST, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 7 THEN QTY ELSE 0 END,0)) AS KEYCLIENT, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 2 THEN QTY ELSE 0 END,0)) AS NORTH, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE = 1 THEN QTY ELSE 0 END,0)) AS SOUTH, "
                    + "SUM(QTY) AS GRAND "
                    + "FROM "
                    + "(SELECT "
                    + "CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN '1. ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN '3. HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN '4. SDF' "
                    + "ELSE '2. MNE/MNG' END USING UTF8)AS PROD_GROUP, SIZE_CRITERIA,COALESCE(Q4,0) AS QTY,INCHARGE,INCHARGE_NAME "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD " + cndtn + " ) AS V "
                    + ") AS M "
                    + "SET ACNE_ACTUAL = ACNE, NORTH_ACTUAL = NORTH,SOUTH_ACTUAL = SOUTH,EAST_ACTUAL= EASTWEST,KEY_ACTUAL= KEYCLIENT,TOTAL_ACTUAL= GRAND "
                    + "WHERE PRODUCT_CAPTION= PROD_GROUP "
                    + "AND SIZE_CRITERIA = MTR_CAPTION";
//            System.out.println("aQry : "+aQry);        
            data.Execute(aQry);

            strSQL = "SELECT * FROM PRODUCTION.FELT_BUDGET_PRODUCTION_CAPACITY";

//            strSQL = "SELECT PROD_GROUP,SIZE_CRITERIA, "
//                    + "SUM(COALESCE(CASE WHEN INCHARGE = 5 THEN QTY ELSE 0 END,0)) AS ACNE, "
//                    + "SUM(COALESCE(CASE WHEN INCHARGE = 3 THEN QTY ELSE 0 END,0)) AS EASTWEST, "
//                    + "SUM(COALESCE(CASE WHEN INCHARGE = 7 THEN QTY ELSE 0 END,0)) AS KEYCLIENT, "
//                    + "SUM(COALESCE(CASE WHEN INCHARGE = 2 THEN QTY ELSE 0 END,0)) AS NORTH, "
//                    + "SUM(COALESCE(CASE WHEN INCHARGE = 1 THEN QTY ELSE 0 END,0)) AS SOUTH, "
//                    + "SUM(QTY) AS GRAND "
//                    + "FROM "
//                    + "(SELECT "
//                    + "CASE WHEN GROUP_NAME IN ('ACNE') THEN '1. ACNE' "
//                    + "WHEN GROUP_NAME IN ('HDS') THEN '3. HDS' "
//                    + "WHEN GROUP_NAME IN ('SDF') THEN '4. SDF' "
//                    + "ELSE '2. MNE/MNG' END AS PROD_GROUP, SIZE_CRITERIA,COALESCE(Q4,0) AS QTY,INCHARGE,INCHARGE_NAME "
//                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
//                    + "AND INCHARGE = INCHARGE_CD  ) AS V "
//                    + "GROUP BY PROD_GROUP,SIZE_CRITERIA";
            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PRODUCT_CAPTION");
                    rowData[2] = rsTmp.getString("MTR_CAPTION");
                    rowData[3] = rsTmp.getString("ACNE_CAPACITY");
                    rowData[4] = rsTmp.getString("ACNE_ACTUAL");
                    rowData[5] = rsTmp.getString("EAST_CAPACITY");
                    rowData[6] = rsTmp.getString("EAST_ACTUAL");
                    rowData[7] = rsTmp.getString("KEY_CAPACITY");
                    rowData[8] = rsTmp.getString("KEY_ACTUAL");
                    rowData[9] = rsTmp.getString("NORTH_CAPACITY");
                    rowData[10] = rsTmp.getString("NORTH_ACTUAL");
                    rowData[11] = rsTmp.getString("SOUTH_CAPACITY");
                    rowData[12] = rsTmp.getString("SOUTH_ACTUAL");
                    rowData[13] = rsTmp.getString("TOTAL_CAPACITY");
                    rowData[14] = rsTmp.getString("TOTAL_ACTUAL");
                    rowData[15] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void FormatGridZPMP() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("UPN");
        DataModel.addColumn("Machine No");
        DataModel.addColumn("Position No");
        DataModel.addColumn("Position Desc");
        DataModel.addColumn("Product Code");
        DataModel.addColumn("Group");
        DataModel.addColumn("Style");
        DataModel.addColumn("Length");
        DataModel.addColumn("width");
        DataModel.addColumn("GSM");
        DataModel.addColumn("Weight");
        DataModel.addColumn("Sq Mtr");
        DataModel.addColumn("No of Pcs");
        DataModel.addColumn("Wt Kgs");
        DataModel.addColumn("Sq.Mtrs");
        DataModel.addColumn("Value (in Lakhs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZPMP() {
        String cndtn = "";
        String grp_cndtn = "";
        String TableName = "PRODUCTION.FELT_BUDGET_DETAIL";
        try {
            FormatGridZPMP(); //clear existing content of table
            ResultSet rsTmp;

//            if (chkFAZPMP.isSelected()) {
//                TableName = "PRODUCTION.FELT_BUDGET";
//            } else {
//                TableName = "PRODUCTION.FELT_BUDGET_DETAIL";
//            }
            int status = cmbStatus.getSelectedIndex();
            if (status == 0) {
                cndtn += " AND COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 1) {
                cndtn += " AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 2) {
                cndtn += " AND COALESCE(CANCELED,0)=1 ";
            }

            if (!txtpartycode.getText().trim().equals("")) {
                cndtn += " AND PARTY_CODE= '" + txtpartycode.getText().trim() + "' ";
            }

            if (!txtproductcode.getText().trim().equals("")) {
                String[] Products = txtproductcode.getText().trim().split(",");
                for (int i = 0; i < Products.length; i++) {
                    if (i == 0) {
                        cndtn += " AND (QUALITY_NO = '" + Products[i] + "' ";
                    } else {
                        cndtn += " OR QUALITY_NO = '" + Products[i] + "' ";
                    }
                }
                cndtn += ")";
            }

            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND INCHARGE_NAME = '" + INCHARGE.getSelectedItem() + "' ";
            }

            String strSQL = "";

            strSQL = "SELECT INCHARGE_NAME,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,UPN,QUALITY_NO,GROUP_NAME,STYLE, "
                    + "(PRESS_LENGTH + DRY_LENGTH) AS LENGTH, "
                    + "(PRESS_WIDTH + DRY_WIDTH) AS WIDTH, "
                    + "(PRESS_GSM) AS GSM, "
                    + "(PRESS_WEIGHT + DRY_WEIGHT) AS WEIGHT, "
                    + "(PRESS_SQMTR + DRY_SQMTR) AS SQMTR, "
                    + "COALESCE(Q4,0) AS QTY, COALESCE(TOTAL_KG,0) AS WT_KGS, COALESCE(TOTAL_SQMTR,0) AS T_SQMTR,ROUND(COALESCE(Q4NET_AMOUNT/100000,0),2) AS AMT "
                    //                    + "FROM "+TableName+",PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    //                    + "ORDER BY INCHARGE_NAME,PARTY_CODE "
                    + " "
                    + "UNION ALL "
                    + " "
                    + "SELECT CONVERT('[GRAND]' USING UTF8) AS INCHARGE_NAME,CONVERT('TOTAL' USING UTF8) AS PARTY_CODE, '' AS PARTY_NAME, '' AS MACHINE_NO, '' AS POSITION_NO, '' AS POSITION_DESC, '' AS UPN, '' AS QUALITY_NO, '' AS GROUP_NAME, '' AS STYLE, "
                    + " '' AS LENGTH, "
                    + " '' AS WIDTH, "
                    + " '' AS GSM, "
                    + "SUM((PRESS_WEIGHT + DRY_WEIGHT)) AS WEIGHT, "
                    + "SUM((PRESS_SQMTR + DRY_SQMTR)) AS SQMTR, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS T_SQMTR,SUM(ROUND(COALESCE(Q4NET_AMOUNT/100000,0),2)) AS AMT "
                    //                    + "FROM "+TableName+",PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "ORDER BY INCHARGE_NAME,PARTY_CODE,MACHINE_NO,POSITION_NO";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PARTY_CODE");
                    rowData[3] = rsTmp.getString("PARTY_NAME");
                    rowData[4] = rsTmp.getString("UPN");
                    rowData[5] = rsTmp.getString("MACHINE_NO");
                    rowData[6] = rsTmp.getString("POSITION_NO");
                    rowData[7] = rsTmp.getString("POSITION_DESC");
                    rowData[8] = rsTmp.getString("QUALITY_NO");
                    rowData[9] = rsTmp.getString("GROUP_NAME");
                    rowData[10] = rsTmp.getString("STYLE");
                    rowData[11] = rsTmp.getString("LENGTH");
                    rowData[12] = rsTmp.getString("WIDTH");
                    rowData[13] = rsTmp.getString("GSM");
                    rowData[14] = rsTmp.getString("WEIGHT");
                    rowData[15] = rsTmp.getString("SQMTR");
                    rowData[16] = rsTmp.getString("QTY");
                    rowData[17] = rsTmp.getString("WT_KGS");
                    rowData[18] = rsTmp.getString("T_SQMTR");
                    rowData[19] = rsTmp.getString("AMT");
                    rowData[20] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void FormatGridZParty() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("No of Pcs");
        DataModel.addColumn("Wt Kgs");
        DataModel.addColumn("Sq.Mtrs");
        DataModel.addColumn("Value (in Lakhs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZParty() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridZParty(); //clear existing content of table
            ResultSet rsTmp;

            int status = cmbStatus.getSelectedIndex();
            if (status == 0) {
                cndtn += " AND COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 1) {
                cndtn += " AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 2) {
                cndtn += " AND COALESCE(CANCELED,0)=1 ";
            }

//            if (!txtpartycode.getText().trim().equals("")) {
//                cndtn += " AND PARTY_CODE= '" + txtpartycode.getText().trim() + "' ";
//            }
//
//            if (!txtproductcode.getText().trim().equals("")) {
//                String[] Products = txtproductcode.getText().trim().split(",");
//                for (int i = 0; i < Products.length; i++) {
//                    if (i == 0) {
//                        cndtn += " AND (QUALITY_NO = '" + Products[i] + "' ";
//                    } else {
//                        cndtn += " OR QUALITY_NO = '" + Products[i] + "' ";
//                    }
//                }
//                cndtn += ")";
//            }
            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND INCHARGE_NAME = '" + INCHARGE.getSelectedItem() + "' ";
            }

            String strSQL = "";

//            strSQL = "SELECT INCHARGE_NAME,PARTY_CODE,PARTY_NAME, "
//                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
//                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
//                    + "AND INCHARGE = INCHARGE_CD "
//                    + cndtn + " "
//                    + "GROUP BY INCHARGE_NAME,PARTY_CODE "
//                    + "ORDER BY INCHARGE_NAME,PARTY_CODE ";
            strSQL = "SELECT INCHARGE_NAME,PARTY_CODE,PARTY_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "GROUP BY INCHARGE_NAME,PARTY_CODE "
                    + "UNION ALL "
                    + "SELECT CONVERT('GRAND' USING UTF8) AS INCHARGE_NAME,CONVERT('TOTAL' USING UTF8) AS PARTY_CODE, '' AS PARTY_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PARTY_CODE");
                    rowData[3] = rsTmp.getString("PARTY_NAME");
                    rowData[4] = rsTmp.getString("QTY");
                    rowData[5] = rsTmp.getString("WT_KGS");
                    rowData[6] = rsTmp.getString("SQMTR");
                    rowData[7] = rsTmp.getString("AMT");
                    rowData[8] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void FormatGridZPP() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Product Code");
        DataModel.addColumn("Group");
        DataModel.addColumn("No of Pcs");
        DataModel.addColumn("Wt Kgs");
        DataModel.addColumn("Sq.Mtrs");
        DataModel.addColumn("Value (in Lakhs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZPP() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridZPP(); //clear existing content of table
            ResultSet rsTmp;

            int status = cmbStatus.getSelectedIndex();
            if (status == 0) {
                cndtn += " AND COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 1) {
                cndtn += " AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 2) {
                cndtn += " AND COALESCE(CANCELED,0)=1 ";
            }

            if (!txtpartycode.getText().trim().equals("")) {
                cndtn += " AND PARTY_CODE= '" + txtpartycode.getText().trim() + "' ";
            }

            if (!txtproductcode.getText().trim().equals("")) {
                String[] Products = txtproductcode.getText().trim().split(",");
                for (int i = 0; i < Products.length; i++) {
                    if (i == 0) {
                        cndtn += " AND (QUALITY_NO = '" + Products[i] + "' ";
                    } else {
                        cndtn += " OR QUALITY_NO = '" + Products[i] + "' ";
                    }
                }
                cndtn += ")";
            }

            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND INCHARGE_NAME = '" + INCHARGE.getSelectedItem() + "' ";
            }

            String strSQL = "";

//            strSQL = "SELECT INCHARGE_NAME,PARTY_CODE,PARTY_NAME,QUALITY_NO,GROUP_NAME, "
//                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
//                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
//                    + "AND INCHARGE = INCHARGE_CD "
//                    + cndtn + " "
//                    + "GROUP BY INCHARGE_NAME,PARTY_CODE,QUALITY_NO,GROUP_NAME "
//                    + "ORDER BY INCHARGE_NAME,PARTY_CODE,QUALITY_NO,GROUP_NAME ";
            strSQL = "SELECT INCHARGE_NAME,PARTY_CODE,PARTY_NAME,QUALITY_NO,GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "GROUP BY INCHARGE_NAME,PARTY_CODE,QUALITY_NO,GROUP_NAME "
                    + "UNION ALL "
                    + "SELECT CONVERT('GRAND' USING UTF8) AS INCHARGE_NAME,CONVERT('TOTAL' USING UTF8) AS PARTY_CODE, '' AS PARTY_NAME, '' AS QUALITY_NO, '' AS GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PARTY_CODE");
                    rowData[3] = rsTmp.getString("PARTY_NAME");
                    rowData[4] = rsTmp.getString("QUALITY_NO");
                    rowData[5] = rsTmp.getString("GROUP_NAME");
                    rowData[6] = rsTmp.getString("QTY");
                    rowData[7] = rsTmp.getString("WT_KGS");
                    rowData[8] = rsTmp.getString("SQMTR");
                    rowData[9] = rsTmp.getString("AMT");
                    rowData[10] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void FormatGridZP() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Product Code");
        DataModel.addColumn("Group");
        DataModel.addColumn("No of Pcs");
        DataModel.addColumn("Wt Kgs");
        DataModel.addColumn("Sq.Mtrs");
        DataModel.addColumn("Value (in Lakhs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZP() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridZP(); //clear existing content of table
            ResultSet rsTmp;

            int status = cmbStatus.getSelectedIndex();
            if (status == 0) {
                cndtn += " AND COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 1) {
                cndtn += " AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 2) {
                cndtn += " AND COALESCE(CANCELED,0)=1 ";
            }

//            if (!txtpartycode.getText().trim().equals("")) {
//                cndtn += " AND PARTY_CODE= '" + txtpartycode.getText().trim() + "' ";
//            }
            if (!txtproductcode.getText().trim().equals("")) {
                String[] Products = txtproductcode.getText().trim().split(",");
                for (int i = 0; i < Products.length; i++) {
                    if (i == 0) {
                        cndtn += " AND (QUALITY_NO = '" + Products[i] + "' ";
                    } else {
                        cndtn += " OR QUALITY_NO = '" + Products[i] + "' ";
                    }
                }
                cndtn += ")";
            }

            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND INCHARGE_NAME = '" + INCHARGE.getSelectedItem() + "' ";
            }

            String strSQL = "";

//            strSQL = "SELECT INCHARGE_NAME,QUALITY_NO,GROUP_NAME, "
//                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
//                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
//                    + "AND INCHARGE = INCHARGE_CD "
//                    + cndtn + " "
//                    + "GROUP BY INCHARGE_NAME,QUALITY_NO,GROUP_NAME "
//                    + "ORDER BY INCHARGE_NAME,QUALITY_NO,GROUP_NAME ";
            strSQL = "SELECT INCHARGE_NAME,QUALITY_NO,GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "GROUP BY INCHARGE_NAME,QUALITY_NO,GROUP_NAME "
                    + "UNION ALL "
                    + "SELECT CONVERT('GRAND' USING UTF8) AS INCHARGE_NAME,CONVERT('TOTAL' USING UTF8) AS QUALITY_NO, '' AS GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("QUALITY_NO");
                    rowData[3] = rsTmp.getString("GROUP_NAME");
                    rowData[4] = rsTmp.getString("QTY");
                    rowData[5] = rsTmp.getString("WT_KGS");
                    rowData[6] = rsTmp.getString("SQMTR");
                    rowData[7] = rsTmp.getString("AMT");
                    rowData[8] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void FormatGridZG() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Group");
        DataModel.addColumn("No of Pcs");
        DataModel.addColumn("Wt Kgs");
        DataModel.addColumn("Sq.Mtrs");
        DataModel.addColumn("Value (in Lakhs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZG() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridZG(); //clear existing content of table
            ResultSet rsTmp;

            int status = cmbStatus.getSelectedIndex();
            if (status == 0) {
                cndtn += " AND COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 1) {
                cndtn += " AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 2) {
                cndtn += " AND COALESCE(CANCELED,0)=1 ";
            }

//            if (!txtpartycode.getText().trim().equals("")) {
//                cndtn += " AND PARTY_CODE= '" + txtpartycode.getText().trim() + "' ";
//            }
//
//            if (!txtproductcode.getText().trim().equals("")) {
//                String[] Products = txtproductcode.getText().trim().split(",");
//                for (int i = 0; i < Products.length; i++) {
//                    if (i == 0) {
//                        cndtn += " AND (QUALITY_NO = '" + Products[i] + "' ";
//                    } else {
//                        cndtn += " OR QUALITY_NO = '" + Products[i] + "' ";
//                    }
//                }
//                cndtn += ")";
//            }
            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND INCHARGE_NAME = '" + INCHARGE.getSelectedItem() + "' ";
            }

            String strSQL = "";

//            strSQL = "SELECT INCHARGE_NAME,GROUP_NAME, "
//                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
//                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
//                    + "AND INCHARGE = INCHARGE_CD "
//                    + cndtn + " "
//                    + "GROUP BY INCHARGE_NAME,GROUP_NAME "
//                    + "ORDER BY INCHARGE_NAME,GROUP_NAME ";
            strSQL = "SELECT INCHARGE_NAME,GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "GROUP BY INCHARGE_NAME,GROUP_NAME "
                    + "UNION ALL "
                    + "SELECT CONVERT('GRAND' USING UTF8) AS INCHARGE_NAME,CONVERT('TOTAL' USING UTF8) AS GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("GROUP_NAME");
                    rowData[3] = rsTmp.getString("QTY");
                    rowData[4] = rsTmp.getString("WT_KGS");
                    rowData[5] = rsTmp.getString("SQMTR");
                    rowData[6] = rsTmp.getString("AMT");
                    rowData[7] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void FormatGridProductwise() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Product Code");
        DataModel.addColumn("Group");
        DataModel.addColumn("No of Pcs");
        DataModel.addColumn("Wt Kgs");
        DataModel.addColumn("Sq.Mtrs");
        DataModel.addColumn("Value (in Lakhs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateProductwise() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridProductwise(); //clear existing content of table
            ResultSet rsTmp;

            int status = cmbStatus.getSelectedIndex();
            if (status == 0) {
                cndtn += " AND COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 1) {
                cndtn += " AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 2) {
                cndtn += " AND COALESCE(CANCELED,0)=1 ";
            }

            String strSQL = "";

//            strSQL = "SELECT QUALITY_NO,GROUP_NAME, "
//                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
//                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
//                    + "AND INCHARGE = INCHARGE_CD "
//                    + cndtn + " "
//                    + "GROUP BY QUALITY_NO,GROUP_NAME "
//                    + "ORDER BY QUALITY_NO,GROUP_NAME ";
            strSQL = "SELECT QUALITY_NO,GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "GROUP BY QUALITY_NO,GROUP_NAME "
                    + "UNION ALL "
                    + "SELECT CONVERT('GRAND' USING UTF8) AS QUALITY_NO,CONVERT('TOTAL' USING UTF8) AS GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " ";
//                    + ") AS A "
//                    + "ORDER BY QUALITY_NO,GROUP_NAME ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("QUALITY_NO");
                    rowData[2] = rsTmp.getString("GROUP_NAME");
                    rowData[3] = rsTmp.getString("QTY");
                    rowData[4] = rsTmp.getString("WT_KGS");
                    rowData[5] = rsTmp.getString("SQMTR");
                    rowData[6] = rsTmp.getString("AMT");
                    rowData[7] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void FormatGridGroupwise() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Group");
        DataModel.addColumn("No of Pcs");
        DataModel.addColumn("Wt Kgs");
        DataModel.addColumn("Sq.Mtrs");
        DataModel.addColumn("Value (in Lakhs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateGroupwise() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridGroupwise(); //clear existing content of table
            ResultSet rsTmp;

            int status = cmbStatus.getSelectedIndex();
            if (status == 0) {
                cndtn += " AND COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 1) {
                cndtn += " AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
            }
            if (status == 2) {
                cndtn += " AND COALESCE(CANCELED,0)=1 ";
            }

            String strSQL = "";

//            strSQL = "SELECT GROUP_NAME, "
//                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
//                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
//                    + "AND INCHARGE = INCHARGE_CD "
//                    + cndtn + " "
//                    + "GROUP BY GROUP_NAME "
//                    + "ORDER BY GROUP_NAME ";
            strSQL = "SELECT CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN '1. ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN '3. HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN '4. SDF' "
                    + "ELSE '2. MNE/MNG' END USING UTF8) AS PROD_GROUP, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "GROUP BY PROD_GROUP "
                    + "UNION ALL "
                    + "SELECT CONVERT('5. GRAND TOTAL' USING UTF8) AS PROD_GROUP, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,ROUND(SUM(COALESCE(Q4NET_AMOUNT/100000,0)),2) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "GROUP BY PROD_GROUP";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PROD_GROUP");
                    rowData[2] = rsTmp.getString("QTY");
                    rowData[3] = rsTmp.getString("WT_KGS");
                    rowData[4] = rsTmp.getString("SQMTR");
                    rowData[5] = rsTmp.getString("AMT");
                    rowData[6] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}
