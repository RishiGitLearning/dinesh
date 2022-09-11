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
public class rptBudgetReviewSales2021 extends javax.swing.JApplet {

    private EITLTableModel DataModel = new EITLTableModel();

    private EITLComboModel modelDept = new EITLComboModel();
    private EITLComboModel modelShift = new EITLComboModel();
    private EITLComboModel modelMainCategory = new EITLComboModel();
    private EITLComboModel modelCategory = new EITLComboModel();
    private EITLComboModel cmbIncharge = new EITLComboModel();
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    private int prvmnth, curmnth, remainmonth, currentmonth;
    private String pmnth, cmnth, pfinyear, pyear;

    //GenerateInvoiceParameterModificationCombo();
    /**
     * Initializes the applet frmChangePassword
     */
    public void init() {
        initComponents();
        setSize(1000, 750);

        pyear = "2020";
        GenerateCombo();
        //canpieced.setVisible(false);
        jLabel1.setForeground(Color.WHITE);
        txtpartyname.setEnabled(false);

        TabList.remove(jPanel8);
        FormatGridZAS();
        Startup();
        cmbFY.setSelectedIndex(cmbFY.getItemCount() - 1);
        if (cmbmonth.getSelectedItem().toString().equalsIgnoreCase("2") || cmbmonth.getSelectedItem().toString().equalsIgnoreCase("3")) {
            pfinyear = "2122";
            if (cmbFY.getSelectedIndex() == 1) {
                pyear = "2021";
            } else {
                pyear = "2020";
            }
        } else if (cmbFY.getSelectedIndex() == 0) {
            pfinyear = "2021";
            pyear = "2020";
        } else {
            pyear = "2021";
            pfinyear = "2122";
        }
        //pfinyear=cmbFY.getSelectedItem().toString().substring(2, 4)+cmbFY.getSelectedItem().toString().substring(7, 9);        
        //pfinyear = "2021";

    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ExporttoExcelFileChooser = new javax.swing.JFileChooser();
        file1 = new javax.swing.JFileChooser();
        CANEXCESSGRUP = new javax.swing.ButtonGroup();
        YTMGROUP = new javax.swing.ButtonGroup();
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
        jPanel17 = new javax.swing.JPanel();
        btnZGView1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnPrdwiseView = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnGrpwiseView = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnGrpwiseView1 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btnPMView = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        btnPWView = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        btnPUView = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        btnYTMView = new javax.swing.JButton();
        ytmdbtn = new javax.swing.JRadioButton();
        ytmrbtn = new javax.swing.JRadioButton();
        jPanel16 = new javax.swing.JPanel();
        btnSFUPView = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        btnCANView = new javax.swing.JButton();
        canpieced = new javax.swing.JRadioButton();
        canupn = new javax.swing.JRadioButton();
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
        jLabel4 = new javax.swing.JLabel();
        cmbmonth = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Sales Projection Report");
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

        TabList.addTab("Zonewise Projection", jPanel3);

        jPanel4.setLayout(null);

        btnZPrtView.setText("View");
        btnZPrtView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZPrtViewActionPerformed(evt);
            }
        });
        jPanel4.add(btnZPrtView);
        btnZPrtView.setBounds(860, 0, 100, 30);

        TabList.addTab("Productwise Projection", jPanel4);

        jPanel5.setLayout(null);

        btnZPPView.setText("View");
        btnZPPView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZPPViewActionPerformed(evt);
            }
        });
        jPanel5.add(btnZPPView);
        btnZPPView.setBounds(860, 0, 100, 30);

        TabList.addTab("Product Zonewise Projection", jPanel5);

        jPanel6.setLayout(null);

        btnZPView.setText("View");
        btnZPView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZPViewActionPerformed(evt);
            }
        });
        jPanel6.add(btnZPView);
        btnZPView.setBounds(860, 0, 100, 30);

        TabList.addTab("New Customer Added", jPanel6);

        jPanel7.setLayout(null);

        btnZGView.setText("View");
        btnZGView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZGViewActionPerformed(evt);
            }
        });
        jPanel7.add(btnZGView);
        btnZGView.setBounds(860, 0, 100, 30);

        TabList.addTab("Customer with Reduction", jPanel7);

        jPanel17.setLayout(null);

        btnZGView1.setText("View");
        btnZGView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZGView1ActionPerformed(evt);
            }
        });
        jPanel17.add(btnZGView1);
        btnZGView1.setBounds(860, 0, 100, 30);

        TabList.addTab("Customer with Upgradtion", jPanel17);

        jPanel8.setLayout(null);

        btnPrdwiseView.setText("View");
        btnPrdwiseView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrdwiseViewActionPerformed(evt);
            }
        });
        jPanel8.add(btnPrdwiseView);
        btnPrdwiseView.setBounds(860, 0, 100, 30);

        TabList.addTab("Obsolesence/rejection/excess", jPanel8);

        jPanel9.setLayout(null);

        btnGrpwiseView.setText("View");
        btnGrpwiseView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrpwiseViewActionPerformed(evt);
            }
        });
        jPanel9.add(btnGrpwiseView);
        btnGrpwiseView.setBounds(860, 0, 100, 30);

        TabList.addTab("Zone wise,customer wise Sales Achievement", jPanel9);

        jPanel10.setLayout(null);

        btnGrpwiseView1.setText("View");
        btnGrpwiseView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrpwiseView1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnGrpwiseView1);
        btnGrpwiseView1.setBounds(860, 0, 100, 30);

        TabList.addTab("Segment wise,Zone wise Projected Sales", jPanel10);

        jPanel11.setLayout(null);

        btnPMView.setText("View");
        btnPMView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPMViewActionPerformed(evt);
            }
        });
        jPanel11.add(btnPMView);
        btnPMView.setBounds(860, 0, 100, 30);

        TabList.addTab("Productwise Meterwise", jPanel11);

        jPanel12.setLayout(null);

        btnPWView.setText("View");
        btnPWView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPWViewActionPerformed(evt);
            }
        });
        jPanel12.add(btnPWView);
        btnPWView.setBounds(860, 0, 100, 30);

        TabList.addTab("Partywise", jPanel12);

        jPanel13.setLayout(null);

        btnPUView.setText("View");
        btnPUView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPUViewActionPerformed(evt);
            }
        });
        jPanel13.add(btnPUView);
        btnPUView.setBounds(860, 0, 100, 30);

        TabList.addTab("PartyUPNwise", jPanel13);

        jPanel15.setLayout(null);

        btnYTMView.setText("View");
        btnYTMView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYTMViewActionPerformed(evt);
            }
        });
        jPanel15.add(btnYTMView);
        btnYTMView.setBounds(860, 0, 100, 30);

        YTMGROUP.add(ytmdbtn);
        ytmdbtn.setText("Sales Projection with uncertainty");
        jPanel15.add(ytmdbtn);
        ytmdbtn.setBounds(180, 10, 290, 30);

        YTMGROUP.add(ytmrbtn);
        ytmrbtn.setSelected(true);
        ytmrbtn.setText("Sales Projection");
        jPanel15.add(ytmrbtn);
        ytmrbtn.setBounds(10, 10, 160, 30);

        TabList.addTab("Yet To Mfg", jPanel15);

        jPanel16.setLayout(null);

        btnSFUPView.setText("View");
        btnSFUPView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSFUPViewActionPerformed(evt);
            }
        });
        jPanel16.add(btnSFUPView);
        btnSFUPView.setBounds(860, 0, 100, 30);

        TabList.addTab("Sale Followup ", jPanel16);

        jPanel14.setLayout(null);

        btnCANView.setText("View");
        btnCANView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCANViewActionPerformed(evt);
            }
        });
        jPanel14.add(btnCANView);
        btnCANView.setBounds(860, 0, 100, 30);

        CANEXCESSGRUP.add(canpieced);
        canpieced.setText("Sales Projection with uncertainty");
        jPanel14.add(canpieced);
        canpieced.setBounds(180, 10, 300, 30);

        CANEXCESSGRUP.add(canupn);
        canupn.setSelected(true);
        canupn.setText("Sales Projection");
        canupn.setContentAreaFilled(false);
        canupn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                canupnActionPerformed(evt);
            }
        });
        jPanel14.add(canupn);
        canupn.setBounds(10, 10, 150, 30);

        TabList.addTab("Cancellable/Non Cancellable Excess", jPanel14);

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

        cmbFY.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2020-2021", "2021-2022" }));
        cmbFY.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFYItemStateChanged(evt);
            }
        });
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

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Approved", "Unapproved", "Cancelled", "All" }));
        cmbStatus.setSelectedIndex(3);
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
        jLabel3.setText("Month");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(310, 30, 50, 20);

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
        txtpartycode.setBounds(550, 30, 70, 20);

        txtpartyname.setDisabledTextColor(java.awt.Color.black);
        txtpartyname = new JTextFieldHint(new JTextField(),"Party Name");
        txtpartyname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpartynameActionPerformed(evt);
            }
        });
        getContentPane().add(txtpartyname);
        txtpartyname.setBounds(620, 30, 370, 20);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Party Code : ");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(450, 30, 90, 20);

        cmbmonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbmonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbmonthActionPerformed(evt);
            }
        });
        getContentPane().add(cmbmonth);
        cmbmonth.setBounds(370, 30, 60, 24);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("OB->Original Budget\nPP->Previous Month Projection\nPPD->Previous Month Projection-Previous Month Doubtful Projection\nCP->Current Month Projection\nCPD->Current Month Projection-Current Month Doubtful Projection");
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 540, 560, 90);
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
        try {
            if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Approval Status")) {
                FormatGridZAS();
                cmbStatus.setEnabled(false);
                INCHARGE.setEnabled(false);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Productwise Meterwise")) {
                FormatGridZPM();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zonewise Projection")) {
                FormatGridZPMP();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Productwise Projection")) {
                FormatGridZParty();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Product Zonewise Projection")) {
                FormatGridZPP();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("New Customer Added")) {
                FormatGridZP();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Customer with Reduction")) {
                FormatGridZG();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(true);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                //txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Customer with Upgradtion")) {
                FormatGridZGU();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(true);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                //txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Zone wise,customer wise Sales Achievement")) {
                FormatGridGroupwise();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(true);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                //txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Segment wise,Zone wise Projected Sales")) {
                FormatGridSegmentwise();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Productwise Meterwise")) {
                FormatGridPM();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(false);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(true);
                cmbmonth.setEnabled(true);
                INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                //txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Partywise")) {
                FormatGridPW();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(true);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("PartyUPNwise")) {
                FormatGridPU();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(true);
                txtproductcode.setEnabled(true);
                cmbmonth.setEnabled(true);
                //INCHARGE.setSelectedIndex(0);
                //txtpartycode.setText("");
                //txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Yet To Mfg")) {
                FormatGridYTM();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(false);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                cmbStatus.setSelectedIndex(0);
                INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Sale Followup ")) {
                FormatGridSFUP();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(true);
                txtproductcode.setEnabled(true);
                cmbmonth.setEnabled(true);
                cmbStatus.setSelectedIndex(0);
                //INCHARGE.setSelectedIndex(0);
                //txtpartycode.setText("");
                //txtproductcode.setText("");
            } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Cancellable/Non Cancellable Excess")) {
                FormatGridCancellable();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(false);
                txtpartycode.setEnabled(false);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                cmbStatus.setSelectedIndex(0);
                INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            }
        } catch (Exception e) {
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
            aList.SQL = "SELECT DISTINCT QUALITY_NO,GROUP_NAME FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM = " + pyear + " AND YEAR_TO = " + (Integer.parseInt(pyear) + 1);
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
            aList.SQL = "SELECT DISTINCT PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM = " + pyear + " AND YEAR_TO = " + (Integer.parseInt(pyear) + 1);
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

    private void cmbmonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbmonthActionPerformed
        // TODO add your handling code here:
        SetVariable();
        if (cmbmonth.getSelectedItem().toString().equalsIgnoreCase("2") || cmbmonth.getSelectedItem().toString().equalsIgnoreCase("3")) {
            pfinyear = "2122";
            if (cmbFY.getSelectedIndex() == 1) {
                pyear = "2021";
            } else {
                pyear = "2020";
            }
        } else if (cmbFY.getSelectedIndex() == 0) {
            pfinyear = "2021";
            pyear = "2020";
        } else {
            pyear = "2021";
            pfinyear = "2122";
        }
    }//GEN-LAST:event_cmbmonthActionPerformed

    private void btnGrpwiseView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrpwiseView1ActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateSegmentwise();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnGrpwiseView1ActionPerformed

    private void btnPMViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPMViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GeneratePM();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnPMViewActionPerformed

    private void btnPWViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPWViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GeneratePW();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnPWViewActionPerformed

    private void btnPUViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPUViewActionPerformed
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GeneratePU();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnPUViewActionPerformed

    private void btnCANViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCANViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            if (cmbStatus.getSelectedIndex() < 2) {
                GenerateCancellable();
            } else {
                JOptionPane.showMessageDialog(null, "Status should be only Approved/Unapproved only");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnCANViewActionPerformed

    private void btnYTMViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYTMViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateYTM();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnYTMViewActionPerformed

    private void btnSFUPViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSFUPViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateSFUP();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnSFUPViewActionPerformed

    private void canupnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_canupnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_canupnActionPerformed

    private void btnZGView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZGView1ActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateZGU();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnZGView1ActionPerformed

    private void cmbFYItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFYItemStateChanged
        // TODO add your handling code here:
        SetVariable();
        if (cmbmonth.getSelectedItem().toString().equalsIgnoreCase("2") || cmbmonth.getSelectedItem().toString().equalsIgnoreCase("3")) {
            pfinyear = "2122";
            if (cmbFY.getSelectedIndex() == 1) {
                pyear = "2021";
            } else {
                pyear = "2020";
            }
        } else if (cmbFY.getSelectedIndex() == 0) {
            pfinyear = "2021";
            pyear = "2020";
        } else {
            pyear = "2021";
            pfinyear = "2122";
        }
    }//GEN-LAST:event_cmbFYItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup CANEXCESSGRUP;
    private javax.swing.JFileChooser ExporttoExcelFileChooser;
    private javax.swing.JComboBox INCHARGE;
    private javax.swing.JTabbedPane TabList;
    private javax.swing.JTable Table;
    private javax.swing.ButtonGroup YTMGROUP;
    private javax.swing.JButton btnCANView;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEmpMstETE;
    private javax.swing.JButton btnGrpwiseView;
    private javax.swing.JButton btnGrpwiseView1;
    private javax.swing.JButton btnPMView;
    private javax.swing.JButton btnPUView;
    private javax.swing.JButton btnPWView;
    private javax.swing.JButton btnPrdwiseView;
    private javax.swing.JButton btnSFUPView;
    private javax.swing.JButton btnYTMView;
    private javax.swing.JButton btnZASView;
    private javax.swing.JButton btnZGView;
    private javax.swing.JButton btnZGView1;
    private javax.swing.JButton btnZPMPView;
    private javax.swing.JButton btnZPMView;
    private javax.swing.JButton btnZPPView;
    private javax.swing.JButton btnZPView;
    private javax.swing.JButton btnZPrtView;
    private javax.swing.JRadioButton canpieced;
    private javax.swing.JRadioButton canupn;
    private javax.swing.JComboBox cmbFY;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JComboBox cmbmonth;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblMonthCmb1;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JTextField txtpartyname;
    private javax.swing.JTextField txtproductcode;
    private javax.swing.JRadioButton ytmdbtn;
    private javax.swing.JRadioButton ytmrbtn;
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

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Incharge");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Total No of Parties");
        DataModel.addColumn("Approve by Area incharge");
        DataModel.addColumn("Mark As Complete");
        DataModel.addColumn("Final Approved");
        DataModel.addColumn("Cancelled");
        DataModel.addColumn("Grand Total");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
        Table.getColumnModel().getColumn(1).setMinWidth(50);
        Table.getColumnModel().getColumn(2).setMinWidth(50);
        Table.getColumnModel().getColumn(3).setMinWidth(150);
        Table.getColumnModel().getColumn(4).setMinWidth(150);
        Table.getColumnModel().getColumn(5).setMinWidth(100);
        Table.getColumnModel().getColumn(6).setMinWidth(100);
        Table.getColumnModel().getColumn(7).setMinWidth(100);

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
                    + "SUM(CREATOR) AS CREATOR,SUM(FINALAPPROVER) AS FINAL_APPROVER,SUM(MAC) AS MAC "
                    + "FROM "
                    + "(SELECT INCHARGE,INCHARGE_NAME,V.DOC_NO,APPROVED,CANCELED,CREATOR,FINALAPPROVER,MAC FROM "
                    + "(SELECT DISTINCT INCHARGE,INCHARGE_NAME,DOC_NO,COALESCE(APPROVED,0) AS APPROVED ,COALESCE(CANCELED,0) AS CANCELED FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD  AND  LEFT(DOC_NO,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + cmbmonth.getSelectedItem().toString() + ",2))"
                    + ") AS V "
                    + "LEFT JOIN "
                    + "(SELECT DOC_NO, "
                    + "CASE WHEN USER_ID IN (394,318,361,352,331,280) THEN 1 ELSE 0 END AS CREATOR, "
                    + "CASE WHEN USER_ID IN (399) THEN 1 ELSE 0 END AS MAC,CASE WHEN USER_ID IN (28,36) THEN 1 ELSE 0 END AS FINALAPPROVER "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID = 834 AND DOC_DATE >='2019-12-19' AND STATUS = 'W'  AND  LEFT(DOC_NO,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + cmbmonth.getSelectedItem().toString() + ",2)))  AS D "
                    + "ON D.DOC_NO = V.DOC_NO) AS M "
                    + "GROUP BY INCHARGE,INCHARGE_NAME "
                    + ""
                    + "UNION ALL "
                    + ""
                    + "SELECT CONVERT('' USING UTF8) AS INCHARGE,CONVERT('TOTAL' USING UTF8) AS INCHARGE_NAME,COUNT(*) AS TOTAL , "
                    + "SUM(COALESCE(CASE WHEN APPROVED =1 AND CANCELED =0 THEN 1 ELSE 0 END,0)) AS FINAL_APPROVED, "
                    + "SUM(COALESCE(CASE WHEN CANCELED =1 AND APPROVED =1 THEN 1 ELSE 0 END,0)) AS CANCELLED, "
                    + "SUM(COALESCE(CASE WHEN CANCELED =0 AND APPROVED =0 THEN 1 ELSE 0 END,0)) AS UNDER_APPROVED, "
                    + "SUM(CREATOR) AS CREATOR,SUM(FINALAPPROVER) AS FINAL_APPROVER,SUM(MAC) "
                    + "FROM "
                    + "(SELECT INCHARGE,INCHARGE_NAME,V.DOC_NO,APPROVED,CANCELED,CREATOR,FINALAPPROVER,MAC FROM "
                    + "(SELECT DISTINCT INCHARGE,INCHARGE_NAME,DOC_NO,COALESCE(APPROVED,0) AS APPROVED ,COALESCE(CANCELED,0) AS CANCELED FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = " + pyear + " AND YEAR_TO = " + (Integer.parseInt(pyear) + 1)+" "
                    + "AND INCHARGE = INCHARGE_CD  AND  LEFT(DOC_NO,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + cmbmonth.getSelectedItem().toString() + ",2)) "
                    + ") AS V "
                    + "LEFT JOIN "
                    + "(SELECT DOC_NO, "
                    + "CASE WHEN USER_ID IN (394,318,361,352,331,280) THEN 1 ELSE 0 END AS CREATOR, "
                    + "CASE WHEN USER_ID IN (399) THEN 1 ELSE 0 END AS MAC,CASE WHEN USER_ID IN (28,36) THEN 1 ELSE 0 END AS FINALAPPROVER "
                    + "FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID = 834 AND DOC_DATE >='2019-12-19' AND STATUS = 'W'  AND  LEFT(DOC_NO,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + cmbmonth.getSelectedItem().toString() + ",2)))  AS D "
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
                    rowData[5] = rsTmp.getString("MAC");
                    rowData[6] = rsTmp.getString("FINAL_APPROVED");
                    rowData[7] = rsTmp.getString("CANCELLED");
                    rowData[8] = rsTmp.getString("TOTAL");
                    rowData[9] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

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
        DataModel.addColumn("ZONE");
        DataModel.addColumn("PROD GROUP");
        DataModel.addColumn("LENGTH RANGE");
        DataModel.addColumn("OB QTY");
        DataModel.addColumn("OB Kgs");
        DataModel.addColumn("OB Value(Rs Lacs)");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZPM() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;

            String p, z;
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
            String strSQL = "SELECT INCHARGE_NAME,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' "
                    + "ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP, SIZE_CRITERIA,sum(COALESCE(actual_budget,0)) AS QTY,"
                    + "round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as weight,"
                    + "SUM(round(coalesce(actual_budget_value,0)/100000,2)) as value,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "0 as pbudget,0 as pweight,0 as pvalue,0 as pdbudget,0 as pdweight,0 as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by incharge,prod_group,size_criteria "
                    + "order by incharge,prod_group,size_criteria ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                p = rsTmp.getString("PROD_GROUP");
                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    if (!p.equalsIgnoreCase(rsTmp.getString("PROD_GROUP")) || !z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[2] = "Product Total";
                        rowData[4] = String.format("%.2f", pobqty);
                        rowData[5] = String.format("%.2f", pobkg);
                        rowData[6] = String.format("%.2f", pobval);
                        rowData[7] = String.format("%.2f", pppqty);
                        rowData[8] = String.format("%.2f", pppkg);
                        rowData[9] = String.format("%.2f", pppval);
                        rowData[10] = String.format("%.2f", pdppqty);
                        rowData[11] = String.format("%.2f", pdppkg);
                        rowData[12] = String.format("%.2f", pdppval);
                        rowData[13] = String.format("%.2f", pcpqty);
                        rowData[14] = String.format("%.2f", pcpkg);
                        rowData[15] = String.format("%.2f", pcpval);
                        rowData[16] = String.format("%.2f", pdcpqty);
                        rowData[17] = String.format("%.2f", pdcpkg);
                        rowData[18] = String.format("%.2f", pdcpval);
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        p = rsTmp.getString("PROD_GROUP");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pdppqty = pdppkg = pdppval = pcpqty = pcpkg = pcpval = pdcpqty = pdcpkg = pdcpval = 0;
                    }
                    if (!z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Zone Total";
                        rowData[4] = String.format("%.2f", zobqty);
                        rowData[5] = String.format("%.2f", zobkg);
                        rowData[6] = String.format("%.2f", zobval);
                        rowData[7] = String.format("%.2f", zppqty);
                        rowData[8] = String.format("%.2f", zppkg);
                        rowData[9] = String.format("%.2f", zppval);
                        rowData[10] = String.format("%.2f", zdppqty);
                        rowData[11] = String.format("%.2f", zdppkg);
                        rowData[12] = String.format("%.2f", zdppval);
                        rowData[13] = String.format("%.2f", zcpqty);
                        rowData[14] = String.format("%.2f", zcpkg);
                        rowData[15] = String.format("%.2f", zcpval);
                        rowData[16] = String.format("%.2f", zdcpqty);
                        rowData[17] = String.format("%.2f", zdcpkg);
                        rowData[18] = String.format("%.2f", zdcpval);
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        z = rsTmp.getString("INCHARGE_NAME");
                        zobqty = zobkg = zobval = zppqty = zppkg = zppval = zdppqty = zdppkg = zdppval = zcpqty = zcpkg = zcpval = zdcpqty = zdcpkg = zdcpval = 0;
                    }
                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PROD_GROUP");
                    rowData[3] = rsTmp.getString("SIZE_CRITERIA");
                    rowData[4] = rsTmp.getString("QTY");
                    rowData[5] = rsTmp.getString("weight");
                    rowData[6] = rsTmp.getString("value");
                    rowData[7] = rsTmp.getString("pbudget");
                    rowData[8] = rsTmp.getString("pweight");
                    rowData[9] = rsTmp.getString("pvalue");
                    rowData[10] = rsTmp.getString("pdbudget");
                    rowData[11] = rsTmp.getString("pdweight");
                    rowData[12] = rsTmp.getString("pdvalue");
                    rowData[13] = rsTmp.getString("cbudget");
                    rowData[14] = rsTmp.getString("cweight");
                    rowData[15] = rsTmp.getString("cvalue");
                    rowData[16] = rsTmp.getString("cdbudget");
                    rowData[17] = rsTmp.getString("cdweight");
                    rowData[18] = rsTmp.getString("cdvalue");
                    rowData[19] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
                    gobqty = gobqty + rsTmp.getDouble("QTY");
                    gobkg = gobkg + rsTmp.getDouble("weight");
                    gobval = gobval + rsTmp.getDouble("value");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    pobqty = pobqty + rsTmp.getDouble("QTY");
                    pobkg = pobkg + rsTmp.getDouble("weight");
                    pobval = pobval + rsTmp.getDouble("value");
                    pppqty = pppqty + rsTmp.getDouble("pbudget");
                    pppkg = pppkg + rsTmp.getDouble("pweight");
                    pppval = pppval + rsTmp.getDouble("pvalue");
                    pdppqty = pdppqty + rsTmp.getDouble("pdbudget");
                    pdppkg = pdppkg + rsTmp.getDouble("pdweight");
                    pdppval = pdppval + rsTmp.getDouble("pdvalue");
                    pcpqty = pcpqty + rsTmp.getDouble("cbudget");
                    pcpkg = pcpkg + rsTmp.getDouble("cweight");
                    pcpval = pcpval + rsTmp.getDouble("cvalue");
                    pdcpqty = pdcpqty + rsTmp.getDouble("cdbudget");
                    pdcpkg = pdcpkg + rsTmp.getDouble("cdweight");
                    pdcpval = pdcpval + rsTmp.getDouble("cdvalue");

                    zobqty = zobqty + rsTmp.getDouble("QTY");
                    zobkg = zobkg + rsTmp.getDouble("weight");
                    zobval = zobval + rsTmp.getDouble("value");
                    zppqty = zppqty + rsTmp.getDouble("pbudget");
                    zppkg = zppkg + rsTmp.getDouble("pweight");
                    zppval = zppval + rsTmp.getDouble("pvalue");
                    zdppqty = zdppqty + rsTmp.getDouble("pdbudget");
                    zdppkg = zdppkg + rsTmp.getDouble("pdweight");
                    zdppval = zdppval + rsTmp.getDouble("pdvalue");
                    zcpqty = zcpqty + rsTmp.getDouble("cbudget");
                    zcpkg = zcpkg + rsTmp.getDouble("cweight");
                    zcpval = zcpval + rsTmp.getDouble("cvalue");
                    zdcpqty = zdcpqty + rsTmp.getDouble("cdbudget");
                    zdcpkg = zdcpkg + rsTmp.getDouble("cdweight");
                    zdcpval = zdcpval + rsTmp.getDouble("cdvalue");

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[2] = "Product Total";
                rowData[4] = String.format("%.2f", pobqty);
                rowData[5] = String.format("%.2f", pobkg);
                rowData[6] = String.format("%.2f", pobval);
                rowData[7] = String.format("%.2f", pppqty);
                rowData[8] = String.format("%.2f", pppkg);
                rowData[9] = String.format("%.2f", pppval);
                rowData[10] = String.format("%.2f", pdppqty);
                rowData[11] = String.format("%.2f", pdppkg);
                rowData[12] = String.format("%.2f", pdppval);
                rowData[13] = String.format("%.2f", pcpqty);
                rowData[14] = String.format("%.2f", pcpkg);
                rowData[15] = String.format("%.2f", pcpval);
                rowData[16] = String.format("%.2f", pdcpqty);
                rowData[17] = String.format("%.2f", pdcpkg);
                rowData[18] = String.format("%.2f", pdcpval);
                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = 0;

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Zone Total";
                rowData[4] = String.format("%.2f", zobqty);
                rowData[5] = String.format("%.2f", zobkg);
                rowData[6] = String.format("%.2f", zobval);
                rowData[7] = String.format("%.2f", zppqty);
                rowData[8] = String.format("%.2f", zppkg);
                rowData[9] = String.format("%.2f", zppval);
                rowData[10] = String.format("%.2f", zdppqty);
                rowData[11] = String.format("%.2f", zdppkg);
                rowData[12] = String.format("%.2f", zdppval);
                rowData[13] = String.format("%.2f", zcpqty);
                rowData[14] = String.format("%.2f", zcpkg);
                rowData[15] = String.format("%.2f", zcpval);
                rowData[16] = String.format("%.2f", zdcpqty);
                rowData[17] = String.format("%.2f", zdcpkg);
                rowData[18] = String.format("%.2f", zdcpval);
                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = 0;

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[4] = String.format("%.2f", gobqty);
                rowData[5] = String.format("%.2f", gobkg);
                rowData[6] = String.format("%.2f", gobval);
                rowData[7] = String.format("%.2f", gppqty);
                rowData[8] = String.format("%.2f", gppkg);
                rowData[9] = String.format("%.2f", gppval);
                rowData[10] = String.format("%.2f", gdppqty);
                rowData[11] = String.format("%.2f", gdppkg);
                rowData[12] = String.format("%.2f", gdppval);
                rowData[13] = String.format("%.2f", gcpqty);
                rowData[14] = String.format("%.2f", gcpkg);
                rowData[15] = String.format("%.2f", gcpval);
                rowData[16] = String.format("%.2f", gdcpqty);
                rowData[17] = String.format("%.2f", gdcpkg);
                rowData[18] = String.format("%.2f", gdcpval);
                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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
        DataModel.addColumn("OB QTY");
        DataModel.addColumn("OB Kgs");
        DataModel.addColumn("OB Value(Rs Lacs)");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZPMP() {
        String cndtn = "";
        String grp_cndtn = "";
        String TableName = "PRODUCTION.FELT_BUDGET_REVIEW_DETAIL";
        try {
            FormatGridZPMP(); //clear existing content of table
            ResultSet rsTmp;

//            if (chkFAZPMP.isSelected()) {
//                TableName = "PRODUCTION.FELT_BUDGET";
//            } else {
//                TableName = "PRODUCTION.FELT_BUDGET_REVIEW_DETAIL";
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

            String strSQL = "SELECT INCHARGE_NAME,sum(COALESCE(actual_budget,0)) AS QTY,"
                    + "round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as weight,"
                    + "SUM(round(coalesce(actual_budget_value,0)/100000,2)) as value,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "0 as pbudget,0 as pweight,0 as pvalue,0 as pdbudget,0 as pdweight,0 as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by incharge "
                    + "order by incharge ";

            System.out.println("Query..." + strSQL);
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;

            String p, z;

            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                z = rsTmp.getString("INCHARGE_NAME");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("QTY");
                    rowData[3] = rsTmp.getString("weight");
                    rowData[4] = rsTmp.getString("value");
                    rowData[5] = rsTmp.getString("pbudget");
                    rowData[6] = rsTmp.getString("pweight");
                    rowData[7] = rsTmp.getString("pvalue");
                    rowData[8] = rsTmp.getString("pdbudget");
                    rowData[9] = rsTmp.getString("pdweight");
                    rowData[10] = rsTmp.getString("pdvalue");
                    rowData[11] = rsTmp.getString("cbudget");
                    rowData[12] = rsTmp.getString("cweight");
                    rowData[13] = rsTmp.getString("cvalue");
                    rowData[14] = rsTmp.getString("cdbudget");
                    rowData[15] = rsTmp.getString("cdweight");
                    rowData[16] = rsTmp.getString("cdvalue");
                    rowData[17] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    gobqty = gobqty + rsTmp.getDouble("QTY");
                    gobkg = gobkg + rsTmp.getDouble("weight");
                    gobval = gobval + rsTmp.getDouble("value");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[2] = String.format("%.2f", gobqty);
                rowData[3] = String.format("%.2f", gobkg);
                rowData[4] = String.format("%.2f", gobval);
                rowData[5] = String.format("%.2f", gppqty);
                rowData[6] = String.format("%.2f", gppkg);
                rowData[7] = String.format("%.2f", gppval);
                rowData[8] = String.format("%.2f", gdppqty);
                rowData[9] = String.format("%.2f", gdppkg);
                rowData[10] = String.format("%.2f", gdppval);
                rowData[11] = String.format("%.2f", gcpqty);
                rowData[12] = String.format("%.2f", gcpkg);
                rowData[13] = String.format("%.2f", gcpval);
                rowData[14] = String.format("%.2f", gdcpqty);
                rowData[15] = String.format("%.2f", gdcpkg);
                rowData[16] = String.format("%.2f", gdcpval);
                DataModel.addRow(rowData);
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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
        DataModel.addColumn("Product");
        DataModel.addColumn("OB QTY");
        DataModel.addColumn("OB Kgs");
        DataModel.addColumn("OB Value(Rs Lacs)");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
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

            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND INCHARGE_NAME = '" + INCHARGE.getSelectedItem() + "' ";
            }

            String strSQL = "SELECT CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' "
                    + "ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP, sum(COALESCE(actual_budget,0)) AS QTY,"
                    + "round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as weight,"
                    + "SUM(round(coalesce(actual_budget_value,0)/100000,2)) as value,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "0 as pbudget,0 as pweight,0 as pvalue,0 as pdbudget,0 as pdweight,0 as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2))  as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2))  as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by prod_group "
                    + "order by prod_group ";

            System.out.println("Query..." + strSQL);
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;

            String p, z;
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;

                p = rsTmp.getString("PROD_GROUP");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PROD_GROUP");
                    rowData[2] = rsTmp.getString("QTY");
                    rowData[3] = rsTmp.getString("weight");
                    rowData[4] = rsTmp.getString("value");
                    rowData[5] = rsTmp.getString("pbudget");
                    rowData[6] = rsTmp.getString("pweight");
                    rowData[7] = rsTmp.getString("pvalue");
                    rowData[8] = rsTmp.getString("pdbudget");
                    rowData[9] = rsTmp.getString("pdweight");
                    rowData[10] = rsTmp.getString("pdvalue");
                    rowData[11] = rsTmp.getString("cbudget");
                    rowData[12] = rsTmp.getString("cweight");
                    rowData[13] = rsTmp.getString("cvalue");
                    rowData[14] = rsTmp.getString("cdbudget");
                    rowData[15] = rsTmp.getString("cdweight");
                    rowData[16] = rsTmp.getString("cdvalue");
                    rowData[17] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    gobqty = gobqty + rsTmp.getDouble("QTY");
                    gobkg = gobkg + rsTmp.getDouble("weight");
                    gobval = gobval + rsTmp.getDouble("value");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[2] = String.format("%.2f", gobqty);
                rowData[3] = String.format("%.2f", gobkg);
                rowData[4] = String.format("%.2f", gobval);
                rowData[5] = String.format("%.2f", gppqty);
                rowData[6] = String.format("%.2f", gppkg);
                rowData[7] = String.format("%.2f", gppval);
                rowData[8] = String.format("%.2f", gdppqty);
                rowData[9] = String.format("%.2f", gdppkg);
                rowData[10] = String.format("%.2f", gdppval);
                rowData[11] = String.format("%.2f", gcpqty);
                rowData[12] = String.format("%.2f", gcpkg);
                rowData[13] = String.format("%.2f", gcpval);
                rowData[14] = String.format("%.2f", gdcpqty);
                rowData[15] = String.format("%.2f", gdcpkg);
                rowData[16] = String.format("%.2f", gdcpval);
                DataModel.addRow(rowData);
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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
        DataModel.addColumn("Product Code");
        DataModel.addColumn("Zone");
        DataModel.addColumn("OB QTY");
        DataModel.addColumn("OB Kgs");
        DataModel.addColumn("OB Value(Rs Lacs)");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
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

            String strSQL = "SELECT INCHARGE_NAME,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' "
                    + "ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP, SIZE_CRITERIA,sum(COALESCE(actual_budget,0)) AS QTY,"
                    + "round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as weight,"
                    + "SUM(round(coalesce(actual_budget_value,0)/100000,2)) as value,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "0 as pbudget,0 as pweight,0 as pvalue,0 as pdbudget,0 as pdweight,0 as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2))  as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2))  as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by prod_group,incharge "
                    + "order by prod_group,incharge ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;

            String p, z;

            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());

            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                p = rsTmp.getString("PROD_GROUP");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    if (!p.equalsIgnoreCase(rsTmp.getString("PROD_GROUP"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Product Total";
                        rowData[3] = String.format("%.2f", pobqty);
                        rowData[4] = String.format("%.2f", pobkg);
                        rowData[5] = String.format("%.2f", pobval);
                        rowData[6] = String.format("%.2f", pppqty);
                        rowData[7] = String.format("%.2f", pppkg);
                        rowData[8] = String.format("%.2f", pppval);
                        rowData[9] = String.format("%.2f", pdppqty);
                        rowData[10] = String.format("%.2f", pdppkg);
                        rowData[11] = String.format("%.2f", pdppval);
                        rowData[12] = String.format("%.2f", pcpqty);
                        rowData[13] = String.format("%.2f", pcpkg);
                        rowData[14] = String.format("%.2f", pcpval);
                        rowData[15] = String.format("%.2f", pdcpqty);
                        rowData[16] = String.format("%.2f", pdcpkg);
                        rowData[17] = String.format("%.2f", pdcpval);
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        p = rsTmp.getString("PROD_GROUP");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pdppqty = pdppkg = pdppval = pcpqty = pcpkg = pcpval = pdcpqty = pdcpkg = pdcpval = 0;
                    }

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PROD_GROUP");
                    rowData[2] = rsTmp.getString("INCHARGE_NAME");
                    rowData[3] = rsTmp.getString("QTY");
                    rowData[4] = rsTmp.getString("weight");
                    rowData[5] = rsTmp.getString("value");
                    rowData[6] = rsTmp.getString("pbudget");
                    rowData[7] = rsTmp.getString("pweight");
                    rowData[8] = rsTmp.getString("pvalue");
                    rowData[9] = rsTmp.getString("pdbudget");
                    rowData[10] = rsTmp.getString("pdweight");
                    rowData[11] = rsTmp.getString("pdvalue");
                    rowData[12] = rsTmp.getString("cbudget");
                    rowData[13] = rsTmp.getString("cweight");
                    rowData[14] = rsTmp.getString("cvalue");
                    rowData[15] = rsTmp.getString("cdbudget");
                    rowData[16] = rsTmp.getString("cdweight");
                    rowData[17] = rsTmp.getString("cdvalue");
                    rowData[18] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    gobqty = gobqty + rsTmp.getDouble("QTY");
                    gobkg = gobkg + rsTmp.getDouble("weight");
                    gobval = gobval + rsTmp.getDouble("value");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    pobqty = pobqty + rsTmp.getDouble("QTY");
                    pobkg = pobkg + rsTmp.getDouble("weight");
                    pobval = pobval + rsTmp.getDouble("value");
                    pppqty = pppqty + rsTmp.getDouble("pbudget");
                    pppkg = pppkg + rsTmp.getDouble("pweight");
                    pppval = pppval + rsTmp.getDouble("pvalue");
                    pdppqty = pdppqty + rsTmp.getDouble("pdbudget");
                    pdppkg = pdppkg + rsTmp.getDouble("pdweight");
                    pdppval = pdppval + rsTmp.getDouble("pdvalue");
                    pcpqty = pcpqty + rsTmp.getDouble("cbudget");
                    pcpkg = pcpkg + rsTmp.getDouble("cweight");
                    pcpval = pcpval + rsTmp.getDouble("cvalue");
                    pdcpqty = pdcpqty + rsTmp.getDouble("cdbudget");
                    pdcpkg = pdcpkg + rsTmp.getDouble("cdweight");
                    pdcpval = pdcpval + rsTmp.getDouble("cdvalue");

                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Product Total";
                rowData[3] = String.format("%.2f", pobqty);
                rowData[4] = String.format("%.2f", pobkg);
                rowData[5] = String.format("%.2f", pobval);
                rowData[6] = String.format("%.2f", pppqty);
                rowData[7] = String.format("%.2f", pppkg);
                rowData[8] = String.format("%.2f", pppval);
                rowData[9] = String.format("%.2f", pdppqty);
                rowData[10] = String.format("%.2f", pdppkg);
                rowData[11] = String.format("%.2f", pdppval);
                rowData[12] = String.format("%.2f", pcpqty);
                rowData[13] = String.format("%.2f", pcpkg);
                rowData[14] = String.format("%.2f", pcpval);
                rowData[15] = String.format("%.2f", pdcpqty);
                rowData[16] = String.format("%.2f", pdcpkg);
                rowData[17] = String.format("%.2f", pdcpval);
                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                pobqty = pobkg = pobval = pppqty = pppkg = pppval = pdppqty = pdppkg = pdppval = pcpqty = pcpkg = pcpval = pdcpqty = pdcpkg = pdcpval = 0;

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[3] = String.format("%.2f", gobqty);
                rowData[4] = String.format("%.2f", gobkg);
                rowData[5] = String.format("%.2f", gobval);
                rowData[6] = String.format("%.2f", gppqty);
                rowData[7] = String.format("%.2f", gppkg);
                rowData[8] = String.format("%.2f", gppval);
                rowData[9] = String.format("%.2f", gdppqty);
                rowData[10] = String.format("%.2f", gdppkg);
                rowData[11] = String.format("%.2f", gdppval);
                rowData[12] = String.format("%.2f", gcpqty);
                rowData[13] = String.format("%.2f", gcpkg);
                rowData[14] = String.format("%.2f", gcpval);
                rowData[15] = String.format("%.2f", gdcpqty);
                rowData[16] = String.format("%.2f", gdcpkg);
                rowData[17] = String.format("%.2f", gdcpval);
                DataModel.addRow(rowData);
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Customer");
        DataModel.addColumn("Status");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
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

            String strSQL = "SELECT INCHARGE_NAME,PARTY_CODE,PARTY_NAME,GROUP_CONCAT(DISTINCT STS) AS STS,"
                    + "SUM(CBUDGET) AS CBUDGET,SUM(CWEIGHT) AS CWEIGHT,SUM(CVALUE) AS CVALUE,"
                    + "SUM(CDBUDGET) AS CDBUDGET,SUM(CDWEIGHT) AS CDWEIGHT,SUM(CDVALUE) AS CDVALUE "
                    + " FROM (SELECT INCHARGE_NAME,party_code,party_name,machine_no,position_no,"
                    + "group_concat(distinct case when coalesce(party_status,'')='NEWP' THEN  'NEW PARTY' WHEN COALESCE(PARTY_STATUS,'')='NEWM' THEN 'NEW POSIOTION' END) AS STS,";

            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue ,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) and (party_status='NEWP' or PARTY_STATUS='NEWM') "
                    + cndtn
                    + " group by INCHARGE_NAME,party_code,machine_no,position_no "
                    + " order by INCHARGE_NAME,party_code,machine_no,position_no) AS D "
                    + " GROUP BY INCHARGE_NAME,PARTY_CODE "
                    + " ORDER BY INCHARGE_NAME,PARTY_CODE ";

            System.out.println("Query..." + strSQL);
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;
            String p, z;

            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                p = rsTmp.getString("INCHARGE_NAME");
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    if (!p.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Zone Total";

                        rowData[5] = String.format("%.2f", pcpqty);
                        rowData[6] = String.format("%.2f", pcpkg);
                        rowData[7] = String.format("%.2f", pcpval);
                        rowData[8] = String.format("%.2f", pdcpqty);
                        rowData[9] = String.format("%.2f", pdcpkg);
                        rowData[10] = String.format("%.2f", pdcpval);
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        p = rsTmp.getString("INCHARGE_NAME");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pdppqty = pdppkg = pdppval = pcpqty = pcpkg = pcpval = pdcpqty = pdcpkg = pdcpval = 0;
                    }
                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PARTY_CODE");
                    rowData[3] = rsTmp.getString("PARTY_NAME");
                    rowData[4] = rsTmp.getString("STS");
                    rowData[5] = rsTmp.getString("CBUDGET");
                    rowData[6] = rsTmp.getString("CWEIGHT");
                    rowData[7] = rsTmp.getString("CVALUE");
                    rowData[8] = rsTmp.getString("CDBUDGET");
                    rowData[9] = rsTmp.getString("CDWEIGHT");
                    rowData[10] = rsTmp.getString("CDVALUE");
                    rowData[11] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    pcpqty = pcpqty + rsTmp.getDouble("cbudget");
                    pcpkg = pcpkg + rsTmp.getDouble("cweight");
                    pcpval = pcpval + rsTmp.getDouble("cvalue");
                    pdcpqty = pdcpqty + rsTmp.getDouble("cdbudget");
                    pdcpkg = pdcpkg + rsTmp.getDouble("cdweight");
                    pdcpval = pdcpval + rsTmp.getDouble("cdvalue");

                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[2] = "Zone Total";
                rowData[5] = String.format("%.2f", pcpqty);
                rowData[6] = String.format("%.2f", pcpkg);
                rowData[7] = String.format("%.2f", pcpval);
                rowData[8] = String.format("%.2f", pdcpqty);
                rowData[9] = String.format("%.2f", pdcpkg);
                rowData[10] = String.format("%.2f", pdcpval);
                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = 0;

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[5] = String.format("%.2f", gcpqty);
                rowData[6] = String.format("%.2f", gcpkg);
                rowData[7] = String.format("%.2f", gcpval);
                rowData[8] = String.format("%.2f", gdcpqty);
                rowData[9] = String.format("%.2f", gdcpkg);
                rowData[10] = String.format("%.2f", gdcpval);
                DataModel.addRow(rowData);
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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
        DataModel.addColumn("PartyCode");
        DataModel.addColumn("Customer Name");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("Reduction [PP-CP] QTY");
        DataModel.addColumn("Reduction [PP-CP] Kgs");
        DataModel.addColumn("Reduction [PP-CP] Value(Rs Lacs)");
        DataModel.addColumn("Reduction [PPD-CPD] QTY");
        DataModel.addColumn("Reduction [PPD-CPD] Kgs");
        DataModel.addColumn("Reduction [PPD-CPD] Value(Rs Lacs)");
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

            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND INCHARGE_NAME = '" + INCHARGE.getSelectedItem() + "' ";
            }

            String strSQL = "SELECT * FROM (SELECT D.*,pbudget-cbudget as dbudget,pweight-cweight as dweight,pvalue-cvalue as dvalue,"
                    + "pdbudget-cdbudget as ddbudget,pdweight-cdweight as ddweight,pdvalue-cdvalue as ddvalue "
                    + "FROM (SELECT INCHARGE_NAME,party_code,party_name,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "sum(coalesce(ACTUAL_BUDGET,0)) as pbudget,round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "round(sum(coalesce(actual_budget_value,0)),2) as pvalue,sum(coalesce(ACTUAL_BUDGET,0)) as pdbudget,round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "round(sum(coalesce(actual_budget_value,0)),2) as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by incharge,party_code "
                    + "order by incharge,party_code) AS D) AS DD "
                    + "WHERE (CBUDGET<PBUDGET OR CDBUDGET<PDBUDGET) ";

            System.out.println("Query..." + strSQL);
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdobqty, gdobkg, gdobval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdobqty, pdobkg, pdobval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdobqty = gdobkg = gdobval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdobqty = pdobkg = pdobval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;
            String p, z;

            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    if (!z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Zone Total";
                        rowData[4] = String.format("%.2f", pppqty);
                        rowData[5] = String.format("%.2f", pppkg);
                        rowData[6] = String.format("%.2f", pppval);
                        rowData[7] = String.format("%.2f", pdppqty);
                        rowData[8] = String.format("%.2f", pdppkg);
                        rowData[9] = String.format("%.2f", pdppval);
                        rowData[10] = String.format("%.2f", pcpqty);
                        rowData[11] = String.format("%.2f", pcpkg);
                        rowData[12] = String.format("%.2f", pcpval);
                        rowData[13] = String.format("%.2f", pdcpqty);
                        rowData[14] = String.format("%.2f", pdcpkg);
                        rowData[15] = String.format("%.2f", pdcpval);
                        rowData[16] = String.format("%.2f", pobqty);
                        rowData[17] = String.format("%.2f", pobkg);
                        rowData[18] = String.format("%.2f", pobval);
                        rowData[19] = String.format("%.2f", pdobqty);
                        rowData[20] = String.format("%.2f", pdobkg);
                        rowData[21] = String.format("%.2f", pdobval);

                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        z = rsTmp.getString("INCHARGE_NAME");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pdppqty = pdppkg = pdppval = pcpqty = pcpkg = pcpval = pdcpqty = pdcpkg = pdcpval =
                                pdobqty=pdobkg=pdobval=0;
                    }

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PARTY_CODE");
                    rowData[3] = rsTmp.getString("PARTY_NAME");
                    rowData[4] = rsTmp.getString("pbudget");
                    rowData[5] = rsTmp.getString("pweight");
                    rowData[6] = rsTmp.getString("pvalue");
                    rowData[7] = rsTmp.getString("pdbudget");
                    rowData[8] = rsTmp.getString("pdweight");
                    rowData[9] = rsTmp.getString("pdvalue");
                    rowData[10] = rsTmp.getString("cbudget");
                    rowData[11] = rsTmp.getString("cweight");
                    rowData[12] = rsTmp.getString("cvalue");
                    rowData[13] = rsTmp.getString("cdbudget");
                    rowData[14] = rsTmp.getString("cdweight");
                    rowData[15] = rsTmp.getString("cdvalue");
                    rowData[16] = rsTmp.getString("dbudget");
                    rowData[17] = rsTmp.getString("dweight");
                    rowData[18] = rsTmp.getString("dvalue");
                    rowData[19] = rsTmp.getString("ddbudget");
                    rowData[20] = rsTmp.getString("ddweight");
                    rowData[21] = rsTmp.getString("ddvalue");
                    rowData[22] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    gobqty = gobqty + rsTmp.getDouble("dbudget");
                    gobkg = gobkg + rsTmp.getDouble("dweight");
                    gobval = gobval + rsTmp.getDouble("dvalue");
                    gdobqty = gdobqty + rsTmp.getDouble("ddbudget");
                    gdobkg = gdobkg + rsTmp.getDouble("ddweight");
                    gdobval = gdobval + rsTmp.getDouble("ddvalue");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    pobqty = pobqty + rsTmp.getDouble("dbudget");
                    pobkg = pobkg + rsTmp.getDouble("dweight");
                    pobval = pobval + rsTmp.getDouble("dvalue");
                    pdobqty = pdobqty + rsTmp.getDouble("ddbudget");
                    pdobkg = pdobkg + rsTmp.getDouble("ddweight");
                    pdobval = pdobval + rsTmp.getDouble("ddvalue");
                    pppqty = pppqty + rsTmp.getDouble("pbudget");
                    pppkg = pppkg + rsTmp.getDouble("pweight");
                    pppval = pppval + rsTmp.getDouble("pvalue");
                    pdppqty = pdppqty + rsTmp.getDouble("pdbudget");
                    pdppkg = pdppkg + rsTmp.getDouble("pdweight");
                    pdppval = pdppval + rsTmp.getDouble("pdvalue");
                    pcpqty = pcpqty + rsTmp.getDouble("cbudget");
                    pcpkg = pcpkg + rsTmp.getDouble("cweight");
                    pcpval = pcpval + rsTmp.getDouble("cvalue");
                    pdcpqty = pdcpqty + rsTmp.getDouble("cdbudget");
                    pdcpkg = pdcpkg + rsTmp.getDouble("cdweight");
                    pdcpval = pdcpval + rsTmp.getDouble("cdvalue");

                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Zone Total";
                rowData[4] = String.format("%.2f", pppqty);
                rowData[5] = String.format("%.2f", pppkg);
                rowData[6] = String.format("%.2f", pppval);
                rowData[7] = String.format("%.2f", pdppqty);
                rowData[8] = String.format("%.2f", pdppkg);
                rowData[9] = String.format("%.2f", pdppval);
                rowData[10] = String.format("%.2f", pcpqty);
                rowData[11] = String.format("%.2f", pcpkg);
                rowData[12] = String.format("%.2f", pcpval);
                rowData[13] = String.format("%.2f", pdcpqty);
                rowData[14] = String.format("%.2f", pdcpkg);
                rowData[15] = String.format("%.2f", pdcpval);
                rowData[16] = String.format("%.2f", pobqty);
                rowData[17] = String.format("%.2f", pobkg);
                rowData[18] = String.format("%.2f", pobval);
                rowData[19] = String.format("%.2f", pdobqty);
                rowData[20] = String.format("%.2f", pdobkg);
                rowData[21] = String.format("%.2f", pdobval);

                DataModel.addRow(rowData);

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[4] = String.format("%.2f", gppqty);
                rowData[5] = String.format("%.2f", gppkg);
                rowData[6] = String.format("%.2f", gppval);
                rowData[7] = String.format("%.2f", gdppqty);
                rowData[8] = String.format("%.2f", gdppkg);
                rowData[9] = String.format("%.2f", gdppval);
                rowData[10] = String.format("%.2f", gcpqty);
                rowData[11] = String.format("%.2f", gcpkg);
                rowData[12] = String.format("%.2f", gcpval);
                rowData[13] = String.format("%.2f", gdcpqty);
                rowData[14] = String.format("%.2f", gdcpkg);
                rowData[15] = String.format("%.2f", gdcpval);
                rowData[16] = String.format("%.2f", gobqty);
                rowData[17] = String.format("%.2f", gobkg);
                rowData[18] = String.format("%.2f", gobval);
                rowData[19] = String.format("%.2f", gdobqty);
                rowData[20] = String.format("%.2f", gdobkg);
                rowData[21] = String.format("%.2f", gdobval);

                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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

    private void FormatGridZGU() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("PartyCode");
        DataModel.addColumn("Customer Name");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("Reduction [PP-CP] QTY");
        DataModel.addColumn("Reduction [PP-CP] Kgs");
        DataModel.addColumn("Reduction [PP-CP] Value(Rs Lacs)");
        DataModel.addColumn("Reduction [PPD-CPD] QTY");
        DataModel.addColumn("Reduction [PPD-CPD] Kgs");
        DataModel.addColumn("Reduction [PPD-CPD] Value(Rs Lacs)");
        DataModel.addColumn("RunTime");
        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateZGU() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridZGU(); //clear existing content of table
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

            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND INCHARGE_NAME = '" + INCHARGE.getSelectedItem() + "' ";
            }

            String strSQL = "SELECT * FROM (SELECT D.*,cbudget-pbudget as dbudget,cweight-pweight as dweight,cvalue-pvalue as dvalue,"
                    + "cdbudget-pdbudget as ddbudget,cdweight-pdweight as ddweight,cdvalue-pdvalue as ddvalue "
                    + "FROM (SELECT INCHARGE_NAME,party_code,party_name,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "sum(coalesce(ACTUAL_BUDGET,0)) as pbudget,round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "round(sum(coalesce(actual_budget_value,0)),2) as pvalue,sum(coalesce(ACTUAL_BUDGET,0)) as pdbudget,round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "round(sum(coalesce(actual_budget_value,0)),2) as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by incharge,party_code "
                    + "order by incharge,party_code) AS D) AS DD "
                    + "WHERE (CBUDGET>PBUDGET OR CDBUDGET>PDBUDGET) ";

            System.out.println("Query..." + strSQL);
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdobqty, gdobkg, gdobval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdobqty, pdobkg, pdobval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdobqty = gdobkg = gdobval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdobqty = pdobkg = pdobval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;
            String p, z;

            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    if (!z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Zone Total";
                        rowData[4] = String.format("%.2f", pppqty);
                        rowData[5] = String.format("%.2f", pppkg);
                        rowData[6] = String.format("%.2f", pppval);
                        rowData[7] = String.format("%.2f", pdppqty);
                        rowData[8] = String.format("%.2f", pdppkg);
                        rowData[9] = String.format("%.2f", pdppval);
                        rowData[10] = String.format("%.2f", pcpqty);
                        rowData[11] = String.format("%.2f", pcpkg);
                        rowData[12] = String.format("%.2f", pcpval);
                        rowData[13] = String.format("%.2f", pdcpqty);
                        rowData[14] = String.format("%.2f", pdcpkg);
                        rowData[15] = String.format("%.2f", pdcpval);
                        rowData[16] = String.format("%.2f", pobqty);
                        rowData[17] = String.format("%.2f", pobkg);
                        rowData[18] = String.format("%.2f", pobval);
                        rowData[19] = String.format("%.2f", pdobqty);
                        rowData[20] = String.format("%.2f", pdobkg);
                        rowData[21] = String.format("%.2f", pdobval);

                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        z = rsTmp.getString("INCHARGE_NAME");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pdppqty = pdppkg = pdppval = pcpqty = pcpkg = pcpval = pdcpqty = pdcpkg = pdcpval = 0;
                    }

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PARTY_CODE");
                    rowData[3] = rsTmp.getString("PARTY_NAME");
                    rowData[4] = rsTmp.getString("pbudget");
                    rowData[5] = rsTmp.getString("pweight");
                    rowData[6] = rsTmp.getString("pvalue");
                    rowData[7] = rsTmp.getString("pdbudget");
                    rowData[8] = rsTmp.getString("pdweight");
                    rowData[9] = rsTmp.getString("pdvalue");
                    rowData[10] = rsTmp.getString("cbudget");
                    rowData[11] = rsTmp.getString("cweight");
                    rowData[12] = rsTmp.getString("cvalue");
                    rowData[13] = rsTmp.getString("cdbudget");
                    rowData[14] = rsTmp.getString("cdweight");
                    rowData[15] = rsTmp.getString("cdvalue");
                    rowData[16] = rsTmp.getString("dbudget");
                    rowData[17] = rsTmp.getString("dweight");
                    rowData[18] = rsTmp.getString("dvalue");
                    rowData[19] = rsTmp.getString("ddbudget");
                    rowData[20] = rsTmp.getString("ddweight");
                    rowData[21] = rsTmp.getString("ddvalue");
                    rowData[22] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    gobqty = gobqty + rsTmp.getDouble("dbudget");
                    gobkg = gobkg + rsTmp.getDouble("dweight");
                    gobval = gobval + rsTmp.getDouble("dvalue");
                    gdobqty = gdobqty + rsTmp.getDouble("ddbudget");
                    gdobkg = gdobkg + rsTmp.getDouble("ddweight");
                    gdobval = gdobval + rsTmp.getDouble("ddvalue");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    pobqty = pobqty + rsTmp.getDouble("dbudget");
                    pobkg = pobkg + rsTmp.getDouble("dweight");
                    pobval = pobval + rsTmp.getDouble("dvalue");
                    pdobqty = pdobqty + rsTmp.getDouble("ddbudget");
                    pdobkg = pdobkg + rsTmp.getDouble("ddweight");
                    pdobval = pdobval + rsTmp.getDouble("ddvalue");
                    pppqty = pppqty + rsTmp.getDouble("pbudget");
                    pppkg = pppkg + rsTmp.getDouble("pweight");
                    pppval = pppval + rsTmp.getDouble("pvalue");
                    pdppqty = pdppqty + rsTmp.getDouble("pdbudget");
                    pdppkg = pdppkg + rsTmp.getDouble("pdweight");
                    pdppval = pdppval + rsTmp.getDouble("pdvalue");
                    pcpqty = pcpqty + rsTmp.getDouble("cbudget");
                    pcpkg = pcpkg + rsTmp.getDouble("cweight");
                    pcpval = pcpval + rsTmp.getDouble("cvalue");
                    pdcpqty = pdcpqty + rsTmp.getDouble("cdbudget");
                    pdcpkg = pdcpkg + rsTmp.getDouble("cdweight");
                    pdcpval = pdcpval + rsTmp.getDouble("cdvalue");

                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Zone Total";
                rowData[4] = String.format("%.2f", pppqty);
                rowData[5] = String.format("%.2f", pppkg);
                rowData[6] = String.format("%.2f", pppval);
                rowData[7] = String.format("%.2f", pdppqty);
                rowData[8] = String.format("%.2f", pdppkg);
                rowData[9] = String.format("%.2f", pdppval);
                rowData[10] = String.format("%.2f", pcpqty);
                rowData[11] = String.format("%.2f", pcpkg);
                rowData[12] = String.format("%.2f", pcpval);
                rowData[13] = String.format("%.2f", pdcpqty);
                rowData[14] = String.format("%.2f", pdcpkg);
                rowData[15] = String.format("%.2f", pdcpval);
                rowData[16] = String.format("%.2f", pobqty);
                rowData[17] = String.format("%.2f", pobkg);
                rowData[18] = String.format("%.2f", pobval);
                rowData[19] = String.format("%.2f", pdobqty);
                rowData[20] = String.format("%.2f", pdobkg);
                rowData[21] = String.format("%.2f", pdobval);

                DataModel.addRow(rowData);

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[4] = String.format("%.2f", gppqty);
                rowData[5] = String.format("%.2f", gppkg);
                rowData[6] = String.format("%.2f", gppval);
                rowData[7] = String.format("%.2f", gdppqty);
                rowData[8] = String.format("%.2f", gdppkg);
                rowData[9] = String.format("%.2f", gdppval);
                rowData[10] = String.format("%.2f", gcpqty);
                rowData[11] = String.format("%.2f", gcpkg);
                rowData[12] = String.format("%.2f", gcpval);
                rowData[13] = String.format("%.2f", gdcpqty);
                rowData[14] = String.format("%.2f", gdcpkg);
                rowData[15] = String.format("%.2f", gdcpval);
                rowData[16] = String.format("%.2f", gobqty);
                rowData[17] = String.format("%.2f", gobkg);
                rowData[18] = String.format("%.2f", gobval);
                rowData[19] = String.format("%.2f", gdobqty);
                rowData[20] = String.format("%.2f", gdobkg);
                rowData[21] = String.format("%.2f", gdobval);

                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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
//                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
//                    + "AND INCHARGE = INCHARGE_CD "
//                    + cndtn + " "
//                    + "GROUP BY QUALITY_NO,GROUP_NAME "
//                    + "ORDER BY QUALITY_NO,GROUP_NAME ";
            strSQL = "SELECT QUALITY_NO,GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,SUM(ROUND(COALESCE(Q4NET_AMOUNT/100000,0),2)) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM = 2020 AND YEAR_TO = 2021 "
                    + "AND INCHARGE = INCHARGE_CD "
                    + cndtn + " "
                    + "GROUP BY QUALITY_NO,GROUP_NAME "
                    + "UNION ALL "
                    + "SELECT CONVERT('GRAND' USING UTF8) AS QUALITY_NO,CONVERT('TOTAL' USING UTF8) AS GROUP_NAME, "
                    + "SUM(COALESCE(Q4,0)) AS QTY, SUM(COALESCE(TOTAL_KG,0)) AS WT_KGS, SUM(COALESCE(TOTAL_SQMTR,0)) AS SQMTR,SUM(ROUND(COALESCE(Q4NET_AMOUNT/100000,0),2)) AS AMT "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  WHERE YEAR_FROM=" + pyear + " "
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
        DataModel.addColumn("Zone");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Customer");
        DataModel.addColumn("Total Project Sales [" + pmnth + "]");
        DataModel.addColumn("Total Project-Doubtful Sales [" + pmnth + "]");
        DataModel.addColumn("Pro rata Projected Sales(month) Rs lacs");
        DataModel.addColumn("Pro rata Projected-Doubtful Sales(month) Rs lacs");
        DataModel.addColumn("Sales achieved(Rs Lacs) Up to [" + pmnth + "]");
        DataModel.addColumn("% achievement");
        DataModel.addColumn("% achievement with Doubtful");
        DataModel.addColumn("Req. Avg. Monthly Sales");
        DataModel.addColumn("Req. Avg. Monthly Sales with Doubtful");
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

            String lstdate = data.getStringValueFromDB("SELECT LAST_DAY(CONCAT('2020-',MONTH(STR_TO_DATE('" + pmnth + "','%b')),'-01')) FROM DUAL");
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
            String strSQL = "SELECT D.*,ROUND(coalesce(amt1,0)-COALESCE(PGRVAL/100000,0),2) as amt "
                    + "FROM (SELECT INCHARGE_NAME,party_code,party_name,SUM(round(DISPATCH_VALUE/100000,2)) as amt1,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "round(sum(coalesce(actual_budget_value,0)),2) as pvalue ";
            } else {
                strSQL = strSQL + " SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + " SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2))  as pdvalue  ";
            }
            strSQL = strSQL + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) ";
            //+ "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
//            if (!pmnth.equalsIgnoreCase("")) {
//                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
//            }
//            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0)  ";

            strSQL = strSQL + cndtn
                    + " group by INCHARGE_NAME,party_code "
                    + "order by INCHARGE_NAME,party_code) AS D "
                    + "LEFT JOIN (SELECT PARTY_CODE AS PARTYCODE,SUM(NET_AMOUNT) AS PGRVAL FROM "
                    + "(SELECT PARTY_CODE,PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
                    + "WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ";
            if (pyear.equalsIgnoreCase("2020")) {
                strSQL = strSQL + " AND INVOICE_DATE<'2020-04-01' AND H.DOC_DATE>='2020-04-01' AND H.DOC_DATE<='2021-03-31') AS A ";
            } else {
                strSQL = strSQL + " AND INVOICE_DATE<'2021-04-01' AND H.DOC_DATE>='2021-04-01' AND H.DOC_DATE<='2022-03-31') AS A ";
            }

            strSQL = strSQL + " GROUP BY PARTY_CODE) AS PGR "
                    + "ON PARTY_CODE=PARTYCODE";

            System.out.println("Query..." + strSQL);
            double gtotsal, gprorata, gsal, ztotsal, zprorata, zsal, gdtotsal, gdprorata, gdsal, zdtotsal, zdprorata, zdsal;
            gtotsal = gprorata = gsal = ztotsal = zprorata = zsal = gdtotsal = gdprorata = gdsal = zdtotsal = zdprorata = zdsal = 0;
            String p, z;

            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0, prorata = 0;
                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                switch (curmnth) {
                    case 4:
                        prorata = 1;
                        break;
                    case 5:
                        prorata = 1;
                        break;
                    case 6:
                        prorata = 2;
                        break;
                    case 7:
                        prorata = 3;
                        break;
                    case 8:
                        prorata = 4;
                        break;
                    case 9:
                        prorata = 5;
                        break;
                    case 10:
                        prorata = 6;
                        break;
                    case 11:
                        prorata = 7;
                        break;
                    case 12:
                        prorata = 8;
                        break;
                    case 1:
                        prorata = 9;
                        break;
                    case 2:
                        prorata = 10;
                        break;
                    case 3:
                        prorata = 11;
                        break;
                }
                double pr = 0, prd = 0;
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    if (!z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Zone Total";
                        rowData[4] = String.format("%.2f", ztotsal);
                        rowData[5] = String.format("%.2f", zdtotsal);
                        rowData[6] = String.format("%.2f", zprorata);
                        rowData[7] = String.format("%.2f", zdprorata);
                        rowData[8] = String.format("%.2f", zsal);
                        rowData[9] = String.format("%.2f", (zsal / zprorata) * 100);
                        rowData[10] = String.format("%.2f", (zdsal / zdprorata) * 100);
                        rowData[11] = String.format("%.2f", (ztotsal - zsal) / remainmonth);
                        rowData[12] = String.format("%.2f", (zdtotsal - zdsal) / remainmonth);
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        z = rsTmp.getString("INCHARGE_NAME");
                        ztotsal = zprorata = zsal = zdtotsal = zdprorata = zdsal = 0;
                    }

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PARTY_CODE");
                    rowData[3] = rsTmp.getString("PARTY_NAME");
                    rowData[4] = rsTmp.getString("pvalue");
                    rowData[5] = rsTmp.getString("pdvalue");
                    pr = (rsTmp.getDouble("pvalue") / 12) * prorata;
                    prd = (rsTmp.getDouble("pdvalue") / 12) * prorata;
                    rowData[6] = String.format("%.2f", pr);
                    rowData[7] = String.format("%.2f", prd);
                    rowData[8] = rsTmp.getString("amt");
                    try {
                        rowData[9] = String.format("%.2f", (rsTmp.getDouble("amt") / pr) * 100);
                    } catch (Exception e) {
                        rowData[9] = 0.00;
                    }
                    try {
                        rowData[10] = String.format("%.2f", (rsTmp.getDouble("amt") / prd) * 100);
                    } catch (Exception e1) {
                        rowData[10] = 0.00;
                    }
                    rowData[11] = String.format("%.2f", (rsTmp.getDouble("pvalue") - rsTmp.getDouble("amt")) / remainmonth);
                    rowData[12] = String.format("%.2f", (rsTmp.getDouble("pdvalue") - rsTmp.getDouble("amt")) / remainmonth);
                    rowData[13] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    gtotsal = gtotsal + rsTmp.getDouble("pvalue");
                    gprorata = gprorata + pr;
                    gsal = gsal + rsTmp.getDouble("amt");
                    gdtotsal = gdtotsal + rsTmp.getDouble("pdvalue");
                    gdprorata = gdprorata + prd;
                    gdsal = gdsal + rsTmp.getDouble("amt");

                    ztotsal = ztotsal + rsTmp.getDouble("pvalue");
                    zprorata = zprorata + pr;
                    zsal = zsal + rsTmp.getDouble("amt");
                    zdtotsal = zdtotsal + rsTmp.getDouble("pdvalue");
                    zdprorata = zdprorata + prd;
                    zdsal = zdsal + rsTmp.getDouble("amt");

                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Zone Total";
                rowData[4] = String.format("%.2f", ztotsal);
                rowData[5] = String.format("%.2f", zdtotsal);
                rowData[6] = String.format("%.2f", zprorata);
                rowData[7] = String.format("%.2f", zdprorata);
                rowData[8] = String.format("%.2f", zsal);
                rowData[9] = String.format("%.2f", (zsal / zprorata) * 100);
                rowData[10] = String.format("%.2f", (zdsal / zdprorata) * 100);
                rowData[11] = String.format("%.2f", (ztotsal - zsal) / remainmonth);
                rowData[12] = String.format("%.2f", (zdtotsal - zdsal) / remainmonth);
                DataModel.addRow(rowData);

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[4] = String.format("%.2f", gtotsal);
                rowData[5] = String.format("%.2f", gdtotsal);
                rowData[6] = String.format("%.2f", gprorata);
                rowData[7] = String.format("%.2f", gdprorata);
                rowData[8] = String.format("%.2f", gsal);
                rowData[9] = String.format("%.2f", (gsal / gprorata) * 100);
                rowData[10] = String.format("%.2f", (gdsal / gdprorata) * 100);
                rowData[11] = String.format("%.2f", (gtotsal - gsal) / remainmonth);
                rowData[12] = String.format("%.2f", (gdtotsal - gdsal) / remainmonth);
                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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

    private void FormatGridSegmentwise() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Segment");
        DataModel.addColumn("Zone");
        DataModel.addColumn("OB QTY");
        DataModel.addColumn("OB Kgs");
        DataModel.addColumn("OB Value(Rs Lacs)");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateSegmentwise() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridSegmentwise(); //clear existing content of table
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
            String strSQL = "SELECT SEGMENT,INCHARGE_NAME,sum(QTY) as dbudget,sum(weight) as dweight,sum(value) as dvalue,"
                    + "sum(pbudget) as pbudget,sum(pweight) as pweight,sum(pvalue) as pvalue,"
                    + "sum(pdbudget) as pdbudget,sum(pdweight) as pdweight,sum(pdvalue) as pdvalue,"
                    + "sum(cbudget) as cbudget,sum(cweight) as cweight,sum(cvalue) as cvalue, "
                    + "sum(cdbudget) as cdbudget,sum(cdweight) as cdweight,sum(cdvalue) as cdvalue "
                    + "FROM (SELECT SEGMENT,PARTY_CODE,MACHINE_NO,POSITION_NO,INCHARGE_NAME,"
                    + "CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP,sum(COALESCE(actual_budget,0)) AS QTY,"
                    + "round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as weight,"
                    + "SUM(round(coalesce(actual_budget_value,0)/100000,2)) as value,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "0 as pbudget,0 as pweight,0 as pvalue,0 as pdbudget,0 as pdweight,0 as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue  "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0)  "
                    + cndtn
                    + " group by PARTY_CODE,MACHINE_NO,POSITION_NO,incharge,prod_group,size_criteria "
                    + "order by PARTY_CODE,MACHINE_NO,POSITION_NO,incharge,prod_group,size_criteria) AS D "
                    + "group by SEGMENT,INCHARGE_NAME "
                    + "order by SEGMENT,INCHARGE_NAME ";

            System.out.println("Query..." + strSQL);
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;
            String p, z;

            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                z = rsTmp.getString("SEGMENT");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    if (!z.equalsIgnoreCase(rsTmp.getString("SEGMENT"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Segment Total";
                        rowData[3] = String.format("%.2f", pobqty);
                        rowData[4] = String.format("%.2f", pobkg);
                        rowData[5] = String.format("%.2f", pobval);
                        rowData[6] = String.format("%.2f", pppqty);
                        rowData[7] = String.format("%.2f", pppkg);
                        rowData[8] = String.format("%.2f", pppval);
                        rowData[9] = String.format("%.2f", pdppqty);
                        rowData[10] = String.format("%.2f", pdppkg);
                        rowData[11] = String.format("%.2f", pdppval);
                        rowData[12] = String.format("%.2f", pcpqty);
                        rowData[13] = String.format("%.2f", pcpkg);
                        rowData[14] = String.format("%.2f", pcpval);
                        rowData[15] = String.format("%.2f", pdcpqty);
                        rowData[16] = String.format("%.2f", pdcpkg);
                        rowData[17] = String.format("%.2f", pdcpval);

                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        z = rsTmp.getString("SEGMENT");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pdppqty = pdppkg = pdppval = pcpqty = pcpkg = pcpval = pdcpqty = pdcpkg = pdcpval = 0;
                    }

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("SEGMENT");
                    rowData[2] = rsTmp.getString("INCHARGE_NAME");
                    rowData[3] = rsTmp.getString("dbudget");
                    rowData[4] = rsTmp.getString("dweight");
                    rowData[5] = rsTmp.getString("dvalue");
                    rowData[6] = rsTmp.getString("pbudget");
                    rowData[7] = rsTmp.getString("pweight");
                    rowData[8] = rsTmp.getString("pvalue");
                    rowData[9] = rsTmp.getString("pdbudget");
                    rowData[10] = rsTmp.getString("pdweight");
                    rowData[11] = rsTmp.getString("pdvalue");
                    rowData[12] = rsTmp.getString("cbudget");
                    rowData[13] = rsTmp.getString("cweight");
                    rowData[14] = rsTmp.getString("cvalue");
                    rowData[15] = rsTmp.getString("cdbudget");
                    rowData[16] = rsTmp.getString("cdweight");
                    rowData[17] = rsTmp.getString("cdvalue");
                    rowData[18] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    gobqty = gobqty + rsTmp.getDouble("dbudget");
                    gobkg = gobkg + rsTmp.getDouble("dweight");
                    gobval = gobval + rsTmp.getDouble("dvalue");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    pobqty = pobqty + rsTmp.getDouble("dbudget");
                    pobkg = pobkg + rsTmp.getDouble("dweight");
                    pobval = pobval + rsTmp.getDouble("dvalue");
                    pppqty = pppqty + rsTmp.getDouble("pbudget");
                    pppkg = pppkg + rsTmp.getDouble("pweight");
                    pppval = pppval + rsTmp.getDouble("pvalue");
                    pdppqty = pdppqty + rsTmp.getDouble("pdbudget");
                    pdppkg = pdppkg + rsTmp.getDouble("pdweight");
                    pdppval = pdppval + rsTmp.getDouble("pdvalue");
                    pcpqty = pcpqty + rsTmp.getDouble("cbudget");
                    pcpkg = pcpkg + rsTmp.getDouble("cweight");
                    pcpval = pcpval + rsTmp.getDouble("cvalue");
                    pdcpqty = pdcpqty + rsTmp.getDouble("cdbudget");
                    pdcpkg = pdcpkg + rsTmp.getDouble("cdweight");
                    pdcpval = pdcpval + rsTmp.getDouble("cdvalue");

                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Segment Total";
                rowData[3] = String.format("%.2f", pobqty);
                rowData[4] = String.format("%.2f", pobkg);
                rowData[5] = String.format("%.2f", pobval);
                rowData[6] = String.format("%.2f", pppqty);
                rowData[7] = String.format("%.2f", pppkg);
                rowData[8] = String.format("%.2f", pppval);
                rowData[9] = String.format("%.2f", pdppqty);
                rowData[10] = String.format("%.2f", pdppkg);
                rowData[11] = String.format("%.2f", pdppval);
                rowData[12] = String.format("%.2f", pcpqty);
                rowData[13] = String.format("%.2f", pcpkg);
                rowData[14] = String.format("%.2f", pcpval);
                rowData[15] = String.format("%.2f", pdcpqty);
                rowData[16] = String.format("%.2f", pdcpkg);
                rowData[17] = String.format("%.2f", pdcpval);

                DataModel.addRow(rowData);

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[3] = String.format("%.2f", gobqty);
                rowData[4] = String.format("%.2f", gobkg);
                rowData[5] = String.format("%.2f", gobval);
                rowData[6] = String.format("%.2f", gppqty);
                rowData[7] = String.format("%.2f", gppkg);
                rowData[8] = String.format("%.2f", gppval);
                rowData[9] = String.format("%.2f", gdppqty);
                rowData[10] = String.format("%.2f", gdppkg);
                rowData[11] = String.format("%.2f", gdppval);
                rowData[12] = String.format("%.2f", gcpqty);
                rowData[13] = String.format("%.2f", gcpkg);
                rowData[14] = String.format("%.2f", gcpval);
                rowData[15] = String.format("%.2f", gdcpqty);
                rowData[16] = String.format("%.2f", gdcpkg);
                rowData[17] = String.format("%.2f", gdcpval);

                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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

    private void SetVariable() {
        HashMap mnth = new HashMap();
        mnth.put(0, "");
        mnth.put(1, "JAN");
        mnth.put(2, "FEB");
        mnth.put(3, "MAR");
        mnth.put(4, "APR");
        mnth.put(5, "MAY");
        mnth.put(6, "JUN");
        mnth.put(7, "JUL");
        mnth.put(8, "AUG");
        mnth.put(9, "SEP");
        mnth.put(10, "OCT");
        mnth.put(11, "NOV");
        mnth.put(12, "DEC");

        curmnth = cmbmonth.getSelectedIndex() + 1;
        if (curmnth == 4) {
            prvmnth = 0;
        } else if (curmnth == 1) {
            prvmnth = 12;
        } else {
            prvmnth = curmnth - 1;
        }
        pmnth = mnth.get(prvmnth).toString();
        cmnth = mnth.get(curmnth).toString();
        remainmonth = currentmonth = 0;
        OUTER:
        OUTER_1:
        switch (cmnth) {
            case "APR":
                remainmonth = 12;
                currentmonth = 1;
                break;
            case "MAY":
                remainmonth = 11;
                currentmonth = 2;
                break;
            case "JUN":
                remainmonth = 10;
                currentmonth = 3;
                break;
            case "JUL":
                remainmonth = 9;
                currentmonth = 4;
                break;
            case "AUG":
                remainmonth = 8;
                currentmonth = 5;
                break;
            case "SEP":
                remainmonth = 7;
                currentmonth = 6;
                break;
            case "OCT":
                remainmonth = 6;
                currentmonth = 7;
                break;
            case "NOV":
                remainmonth = 5;
                currentmonth = 8;
                break;
            case "DEC":
                remainmonth = 4;
                currentmonth = 9;
                break;
            case "JAN":
                remainmonth = 3;
                currentmonth = 10;
                break;
            case "FEB":
                switch (pyear) {
                    case "2021":
//                        remainmonth = 12;
//                        currentmonth = 1;
                        remainmonth = 2;
                        currentmonth = 11;
                        System.out.println(" r:" + remainmonth + " c:" + currentmonth);
                        break OUTER;
                    case "2020":
                        remainmonth = 2;
                        currentmonth = 11;
                        break OUTER;
                }
                break;
            case "MAR":
                switch (pyear) {
                    case "2021":
//                        remainmonth = 12;
//                        currentmonth = 1;
                        remainmonth = 1;
                        currentmonth = 12;
                        break OUTER_1;
                    case "2020":
                        remainmonth = 1;
                        currentmonth = 12;
                        break OUTER_1;
                }
                break;
        }
    }

    private void FormatGridPM() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("PROD GROUP");
        DataModel.addColumn("LENGTH RANGE");
        DataModel.addColumn("OB QTY");
        DataModel.addColumn("OB Kgs");
        DataModel.addColumn("OB Value(Rs Lacs)");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GeneratePM() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;

            String p, z;
            FormatGridPM(); //clear existing content of table
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
            String strSQL = "SELECT INCHARGE_NAME,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' "
                    + "ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP, SIZE_CRITERIA,sum(COALESCE(actual_budget,0)) AS QTY,"
                    + "round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as weight,"
                    + "SUM(round(coalesce(actual_budget_value,0)/100000,2)) as value,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "0 as pbudget,0 as pweight,0 as pvalue,0 as pdbudget,0 as pdweight,0 as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by prod_group,size_criteria "
                    + "order by prod_group,size_criteria ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                p = rsTmp.getString("PROD_GROUP");
//                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    if (!p.equalsIgnoreCase(rsTmp.getString("PROD_GROUP"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[2] = "Product Total";
                        rowData[3] = String.format("%.2f", pobqty);
                        rowData[4] = String.format("%.2f", pobkg);
                        rowData[5] = String.format("%.2f", pobval);
                        rowData[6] = String.format("%.2f", pppqty);
                        rowData[7] = String.format("%.2f", pppkg);
                        rowData[8] = String.format("%.2f", pppval);
                        rowData[9] = String.format("%.2f", pdppqty);
                        rowData[10] = String.format("%.2f", pdppkg);
                        rowData[11] = String.format("%.2f", pdppval);
                        rowData[12] = String.format("%.2f", pcpqty);
                        rowData[13] = String.format("%.2f", pcpkg);
                        rowData[14] = String.format("%.2f", pcpval);
                        rowData[15] = String.format("%.2f", pdcpqty);
                        rowData[16] = String.format("%.2f", pdcpkg);
                        rowData[17] = String.format("%.2f", pdcpval);
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        p = rsTmp.getString("PROD_GROUP");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pdppqty = pdppkg = pdppval = pcpqty = pcpkg = pcpval = pdcpqty = pdcpkg = pdcpval = 0;
                    }
                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PROD_GROUP");
                    rowData[2] = rsTmp.getString("SIZE_CRITERIA");
                    rowData[3] = rsTmp.getString("QTY");
                    rowData[4] = rsTmp.getString("weight");
                    rowData[5] = rsTmp.getString("value");
                    rowData[6] = rsTmp.getString("pbudget");
                    rowData[7] = rsTmp.getString("pweight");
                    rowData[8] = rsTmp.getString("pvalue");
                    rowData[9] = rsTmp.getString("pdbudget");
                    rowData[10] = rsTmp.getString("pdweight");
                    rowData[11] = rsTmp.getString("pdvalue");
                    rowData[12] = rsTmp.getString("cbudget");
                    rowData[13] = rsTmp.getString("cweight");
                    rowData[14] = rsTmp.getString("cvalue");
                    rowData[15] = rsTmp.getString("cdbudget");
                    rowData[16] = rsTmp.getString("cdweight");
                    rowData[17] = rsTmp.getString("cdvalue");
                    rowData[18] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
                    gobqty = gobqty + rsTmp.getDouble("QTY");
                    gobkg = gobkg + rsTmp.getDouble("weight");
                    gobval = gobval + rsTmp.getDouble("value");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    pobqty = pobqty + rsTmp.getDouble("QTY");
                    pobkg = pobkg + rsTmp.getDouble("weight");
                    pobval = pobval + rsTmp.getDouble("value");
                    pppqty = pppqty + rsTmp.getDouble("pbudget");
                    pppkg = pppkg + rsTmp.getDouble("pweight");
                    pppval = pppval + rsTmp.getDouble("pvalue");
                    pdppqty = pdppqty + rsTmp.getDouble("pdbudget");
                    pdppkg = pdppkg + rsTmp.getDouble("pdweight");
                    pdppval = pdppval + rsTmp.getDouble("pdvalue");
                    pcpqty = pcpqty + rsTmp.getDouble("cbudget");
                    pcpkg = pcpkg + rsTmp.getDouble("cweight");
                    pcpval = pcpval + rsTmp.getDouble("cvalue");
                    pdcpqty = pdcpqty + rsTmp.getDouble("cdbudget");
                    pdcpkg = pdcpkg + rsTmp.getDouble("cdweight");
                    pdcpval = pdcpval + rsTmp.getDouble("cdvalue");

                    zobqty = zobqty + rsTmp.getDouble("QTY");
                    zobkg = zobkg + rsTmp.getDouble("weight");
                    zobval = zobval + rsTmp.getDouble("value");
                    zppqty = zppqty + rsTmp.getDouble("pbudget");
                    zppkg = zppkg + rsTmp.getDouble("pweight");
                    zppval = zppval + rsTmp.getDouble("pvalue");
                    zdppqty = zdppqty + rsTmp.getDouble("pdbudget");
                    zdppkg = zdppkg + rsTmp.getDouble("pdweight");
                    zdppval = zdppval + rsTmp.getDouble("pdvalue");
                    zcpqty = zcpqty + rsTmp.getDouble("cbudget");
                    zcpkg = zcpkg + rsTmp.getDouble("cweight");
                    zcpval = zcpval + rsTmp.getDouble("cvalue");
                    zdcpqty = zdcpqty + rsTmp.getDouble("cdbudget");
                    zdcpkg = zdcpkg + rsTmp.getDouble("cdweight");
                    zdcpval = zdcpval + rsTmp.getDouble("cdvalue");

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[2] = "Product Total";
                rowData[3] = String.format("%.2f", pobqty);
                rowData[4] = String.format("%.2f", pobkg);
                rowData[5] = String.format("%.2f", pobval);
                rowData[6] = String.format("%.2f", pppqty);
                rowData[7] = String.format("%.2f", pppkg);
                rowData[8] = String.format("%.2f", pppval);
                rowData[9] = String.format("%.2f", pdppqty);
                rowData[10] = String.format("%.2f", pdppkg);
                rowData[11] = String.format("%.2f", pdppval);
                rowData[12] = String.format("%.2f", pcpqty);
                rowData[13] = String.format("%.2f", pcpkg);
                rowData[14] = String.format("%.2f", pcpval);
                rowData[15] = String.format("%.2f", pdcpqty);
                rowData[16] = String.format("%.2f", pdcpkg);
                rowData[17] = String.format("%.2f", pdcpval);
                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = 0;

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[3] = String.format("%.2f", gobqty);
                rowData[4] = String.format("%.2f", gobkg);
                rowData[5] = String.format("%.2f", gobval);
                rowData[6] = String.format("%.2f", gppqty);
                rowData[7] = String.format("%.2f", gppkg);
                rowData[8] = String.format("%.2f", gppval);
                rowData[9] = String.format("%.2f", gdppqty);
                rowData[10] = String.format("%.2f", gdppkg);
                rowData[11] = String.format("%.2f", gdppval);
                rowData[12] = String.format("%.2f", gcpqty);
                rowData[13] = String.format("%.2f", gcpkg);
                rowData[14] = String.format("%.2f", gcpval);
                rowData[15] = String.format("%.2f", gdcpqty);
                rowData[16] = String.format("%.2f", gdcpkg);
                rowData[17] = String.format("%.2f", gdcpval);
                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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

    private void FormatGridPW() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("PARTY CODE");
        DataModel.addColumn("NAME");
        DataModel.addColumn("OB QTY");
        DataModel.addColumn("OB Kgs");
        DataModel.addColumn("OB Value(Rs Lacs)");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GeneratePW() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;

            String p, z;
            FormatGridPW(); //clear existing content of table
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
            String strSQL = "SELECT PARTY_CODE,PARTY_NAME,UPN,INCHARGE_NAME,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' "
                    + "ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP, SIZE_CRITERIA,sum(COALESCE(actual_budget,0)) AS QTY,"
                    + "round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as weight,"
                    + "SUM(round(coalesce(actual_budget_value,0)/100000,2)) as value,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "0 as pbudget,0 as pweight,0 as pvalue,0 as pdbudget,0 as pdweight,0 as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue  "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by PARTY_CODE "
                    + "order by PARTY_CODE ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
//                p = rsTmp.getString("PROD_GROUP");
//                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PARTY_CODE");
                    rowData[2] = rsTmp.getString("PARTY_NAME");
                    rowData[3] = rsTmp.getString("QTY");
                    rowData[4] = rsTmp.getString("weight");
                    rowData[5] = rsTmp.getString("value");
                    rowData[6] = rsTmp.getString("pbudget");
                    rowData[7] = rsTmp.getString("pweight");
                    rowData[8] = rsTmp.getString("pvalue");
                    rowData[9] = rsTmp.getString("pdbudget");
                    rowData[10] = rsTmp.getString("pdweight");
                    rowData[11] = rsTmp.getString("pdvalue");
                    rowData[12] = rsTmp.getString("cbudget");
                    rowData[13] = rsTmp.getString("cweight");
                    rowData[14] = rsTmp.getString("cvalue");
                    rowData[15] = rsTmp.getString("cdbudget");
                    rowData[16] = rsTmp.getString("cdweight");
                    rowData[17] = rsTmp.getString("cdvalue");
                    rowData[18] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    gobqty = gobqty + rsTmp.getDouble("QTY");
                    gobkg = gobkg + rsTmp.getDouble("weight");
                    gobval = gobval + rsTmp.getDouble("value");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[3] = String.format("%.2f", gobqty);
                rowData[4] = String.format("%.2f", gobkg);
                rowData[5] = String.format("%.2f", gobval);
                rowData[6] = String.format("%.2f", gppqty);
                rowData[7] = String.format("%.2f", gppkg);
                rowData[8] = String.format("%.2f", gppval);
                rowData[9] = String.format("%.2f", gdppqty);
                rowData[10] = String.format("%.2f", gdppkg);
                rowData[11] = String.format("%.2f", gdppval);
                rowData[12] = String.format("%.2f", gcpqty);
                rowData[13] = String.format("%.2f", gcpkg);
                rowData[14] = String.format("%.2f", gcpval);
                rowData[15] = String.format("%.2f", gdcpqty);
                rowData[16] = String.format("%.2f", gdcpkg);
                rowData[17] = String.format("%.2f", gdcpval);
                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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

    private void FormatGridPU() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("UPN");
        DataModel.addColumn("NAME");
        DataModel.addColumn("POSITION DESC");
        DataModel.addColumn("OB QTY");
        DataModel.addColumn("OB Kgs");
        DataModel.addColumn("OB Value(Rs Lacs)");
        DataModel.addColumn("PP [" + pmnth + "] QTY");
        DataModel.addColumn("PP [" + pmnth + "] Kgs");
        DataModel.addColumn("PP [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("PPD [" + pmnth + "] QTY");
        DataModel.addColumn("PPD [" + pmnth + "] Kgs");
        DataModel.addColumn("PPD [" + pmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Kgs");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("CPD [" + cmnth + "] QTY");
        DataModel.addColumn("CPD [" + cmnth + "] Kgs");
        DataModel.addColumn("CPD [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GeneratePU() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;

            String p, z;
            FormatGridPU(); //clear existing content of table
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
            String strSQL = "SELECT PARTY_CODE,PARTY_NAME,UPN,POSITION_DESC,INCHARGE_NAME,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                    + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' "
                    + "ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP, SIZE_CRITERIA,sum(COALESCE(actual_budget,0)) AS QTY,"
                    + "round(sum(coalesce(actual_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as weight,"
                    + "SUM(round(coalesce(actual_budget_value,0)/100000,2)) as value,";
            if (pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "0 as pbudget,0 as pweight,0 as pvalue,0 as pdbudget,0 as pdweight,0 as pdvalue,";
            } else {
                strSQL = strSQL + "sum(coalesce(" + pmnth + "_budget,0)) as pbudget,"
                        + "round(sum(coalesce(" + pmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pweight,"
                        + "SUM(round(coalesce(" + pmnth + "_net_amount,0)/100000,2)) as pvalue,"
                        + "sum(coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0)) as pdbudget,"
                        + "round(sum((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as pdweight,"
                        + "SUM(round(((coalesce(" + pmnth + "_budget,0)-coalesce(" + pmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as pdvalue,";
            }
            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue,"
                    + "sum(coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) as cdbudget,"
                    + "round(sum((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cdweight,"
                    + "SUM(round(((coalesce(" + cmnth + "_budget,0)-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))*COALESCE(SELLING_PRICE,0))/100000,2)) as cdvalue "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE  "
                    + "WHERE YEAR_FROM=" + pyear + " "
                    + "AND INCHARGE = INCHARGE_CD and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + " group by UPN "
                    + "order by UPN ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
//                p = rsTmp.getString("PROD_GROUP");
//                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("UPN");
                    rowData[2] = rsTmp.getString("PARTY_NAME");
                    rowData[3] = rsTmp.getString("POSITION_DESC");
                    rowData[4] = rsTmp.getString("QTY");
                    rowData[5] = rsTmp.getString("weight");
                    rowData[6] = rsTmp.getString("value");
                    rowData[7] = rsTmp.getString("pbudget");
                    rowData[8] = rsTmp.getString("pweight");
                    rowData[9] = rsTmp.getString("pvalue");
                    rowData[10] = rsTmp.getString("pdbudget");
                    rowData[11] = rsTmp.getString("pdweight");
                    rowData[12] = rsTmp.getString("pdvalue");
                    rowData[13] = rsTmp.getString("cbudget");
                    rowData[14] = rsTmp.getString("cweight");
                    rowData[15] = rsTmp.getString("cvalue");
                    rowData[16] = rsTmp.getString("cdbudget");
                    rowData[17] = rsTmp.getString("cdweight");
                    rowData[18] = rsTmp.getString("cdvalue");
                    rowData[19] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    gobqty = gobqty + rsTmp.getDouble("QTY");
                    gobkg = gobkg + rsTmp.getDouble("weight");
                    gobval = gobval + rsTmp.getDouble("value");
                    gppqty = gppqty + rsTmp.getDouble("pbudget");
                    gppkg = gppkg + rsTmp.getDouble("pweight");
                    gppval = gppval + rsTmp.getDouble("pvalue");
                    gdppqty = gdppqty + rsTmp.getDouble("pdbudget");
                    gdppkg = gdppkg + rsTmp.getDouble("pdweight");
                    gdppval = gdppval + rsTmp.getDouble("pdvalue");
                    gcpqty = gcpqty + rsTmp.getDouble("cbudget");
                    gcpkg = gcpkg + rsTmp.getDouble("cweight");
                    gcpval = gcpval + rsTmp.getDouble("cvalue");
                    gdcpqty = gdcpqty + rsTmp.getDouble("cdbudget");
                    gdcpkg = gdcpkg + rsTmp.getDouble("cdweight");
                    gdcpval = gdcpval + rsTmp.getDouble("cdvalue");

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[4] = String.format("%.2f", gobqty);
                rowData[5] = String.format("%.2f", gobkg);
                rowData[6] = String.format("%.2f", gobval);
                rowData[7] = String.format("%.2f", gppqty);
                rowData[8] = String.format("%.2f", gppkg);
                rowData[9] = String.format("%.2f", gppval);
                rowData[10] = String.format("%.2f", gdppqty);
                rowData[11] = String.format("%.2f", gdppkg);
                rowData[12] = String.format("%.2f", gdppval);
                rowData[13] = String.format("%.2f", gcpqty);
                rowData[14] = String.format("%.2f", gcpkg);
                rowData[15] = String.format("%.2f", gcpval);
                rowData[16] = String.format("%.2f", gdcpqty);
                rowData[17] = String.format("%.2f", gdcpkg);
                rowData[18] = String.format("%.2f", gdcpval);
                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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

    private void FormatGridCancellable() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        if (canupn.isSelected()) {
            DataModel.addColumn("Sr.");
            DataModel.addColumn("UPN");
            DataModel.addColumn("Party Code");
            DataModel.addColumn("Name");
            DataModel.addColumn("CP [" + cmnth + "] QTY");
            DataModel.addColumn("WIP QTY");
            DataModel.addColumn("Stock QTY");
            if (cmbFY.getSelectedIndex() == 1) {
                DataModel.addColumn("Proj Opening QTY");
            }
            DataModel.addColumn("DISPATCH QTY");
            DataModel.addColumn("EXCESS QTY");
            DataModel.addColumn("Cancellable Qty");
            DataModel.addColumn("Cancellable Piece[s]");
            DataModel.addColumn("Non Cancellable Qty");
            DataModel.addColumn("Non Cancellable Piece[s]");
            DataModel.addColumn("RunTime");
        }
        if (canpieced.isSelected()) {
            DataModel.addColumn("Sr.");
            DataModel.addColumn("UPN");
            DataModel.addColumn("Party Code");
            DataModel.addColumn("Name");
            DataModel.addColumn("CPD [" + cmnth + "] QTY");
            DataModel.addColumn("WIP QTY");
            DataModel.addColumn("Stock QTY");
            if (cmbFY.getSelectedIndex() == 1) {
                DataModel.addColumn("Proj Opn QTY");
            }
            DataModel.addColumn("DISPATCH QTY");
            DataModel.addColumn("EXCESS QTY");
            DataModel.addColumn("Cancellable Qty");
            DataModel.addColumn("Cancellable Piece[s]");
            DataModel.addColumn("Non Cancellable Qty");
            DataModel.addColumn("Non Cancellable Piece[s]");
            DataModel.addColumn("RunTime");
        }
        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateCancellable() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridCancellable(); //clear existing content of table
            ResultSet rsTmp;

            String strSQL = "";
            double ma, mc, mnc;
            ma = mc = mnc = 0;
            if (canupn.isSelected()) {
                strSQL = "SELECT "
                        + "CNE_UPN AS UPN,CNE_PARTY_CODE,CNE_PARTY_NAME AS PARTY_NAME, "
                        + "D.*,COUNT(CASE WHEN CNE_PIECE_STAGE IN ('PLANNING','BOOKING') THEN CNE_PIECE_NO END) AS CANCELPIECE, "
                        + "COUNT(CASE WHEN CNE_PIECE_STAGE NOT IN ('PLANNING','BOOKING') THEN CNE_PIECE_NO END) AS NONCANCELPIECE, "
                        + "COALESCE(GROUP_CONCAT(CASE WHEN CNE_PIECE_STAGE IN ('PLANNING','BOOKING') THEN CONCAT(CNE_PIECE_NO,'(',CNE_PIECE_STAGE,')') END),'') AS CAN_PIECE, "
                        + "COALESCE(GROUP_CONCAT(CASE WHEN CNE_PIECE_STAGE NOT IN ('PLANNING','BOOKING') THEN CONCAT(CNE_PIECE_NO,'(',CNE_PIECE_STAGE,')') END),'') AS NON_CAN_PIECE ";
                if (cmbStatus.getSelectedItem().toString().equalsIgnoreCase("Approved")) {
                    strSQL = strSQL + " FROM PRODUCTION.FELT_SALES_PROJECTION_CP_NCP_EXCESS F ";
                }
                if (cmbStatus.getSelectedItem().toString().equalsIgnoreCase("Unapproved")) {
                    strSQL = strSQL + " FROM PRODUCTION.FELT_SALES_PROJECTION_CP_NCP_EXCESS_UNDERAPPROVE F ";
                }
                strSQL = strSQL + "LEFT JOIN "
                        + "(SELECT UPN AS SPUPN,SUM(CURRENT_PROJECTION) AS CURRENT_PROJECTION,SUM(WIP_QTY) AS WIP_QTY,SUM(STOCK_QTY) AS STOCK_QTY,SUM(DISPATCH_QTY) AS DISPATCH_QTY,SUM(ACESS_QTY) AS ACESS_QTY,SUM(OC_STOCK) AS PROJ_OPN_STK "
                        + " FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D WHERE D.YEAR_FROM=" + pyear + "  AND left(D.doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) AND ACESS_QTY>0 GROUP BY UPN) D "
                        + "ON F.CNE_UPN=D.SPUPN "
                        + " WHERE CNE_FROM_YEAR=" + pyear + "  AND left(CNE_DOC_NO,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                        + "GROUP BY CNE_UPN "
                        + "HAVING COUNT(CNE_PIECE_NO)>0 "
                        + "ORDER BY CNE_UPN";
            }
            if (canpieced.isSelected()) {
                strSQL = "SELECT "
                        + "CNE_UPN AS UPN,CNE_PARTY_CODE,CNE_PARTY_NAME  AS PARTY_NAME, "
                        + "D.*,COUNT(CASE WHEN CNE_PIECE_STAGE IN ('PLANNING','BOOKING') THEN CNE_PIECE_NO END) AS CANCELPIECE, "
                        + "COUNT(CASE WHEN CNE_PIECE_STAGE NOT IN ('PLANNING','BOOKING') THEN CNE_PIECE_NO END) AS NONCANCELPIECE, "
                        + "COALESCE(GROUP_CONCAT(CASE WHEN CNE_PIECE_STAGE IN ('PLANNING','BOOKING') THEN CONCAT(CNE_PIECE_NO,'(',CNE_PIECE_STAGE,')') END),'') AS CAN_PIECE, "
                        + "COALESCE(GROUP_CONCAT(CASE WHEN CNE_PIECE_STAGE NOT IN ('PLANNING','BOOKING') THEN CONCAT(CNE_PIECE_NO,'(',CNE_PIECE_STAGE,')') END),'') AS NON_CAN_PIECE ";
                if (cmbStatus.getSelectedItem().toString().equalsIgnoreCase("Approved")) {
                    strSQL = strSQL + " FROM PRODUCTION.FELT_SALES_PROJECTION_CP_NCP_EXCESS_DOUBTFUL F ";
                }
                if (cmbStatus.getSelectedItem().toString().equalsIgnoreCase("Unapproved")) {
                    strSQL = strSQL + " FROM PRODUCTION.FELT_SALES_PROJECTION_CP_NCP_EXCESS_DOUBTFUL_UNDERAPPROVE F ";
                }
                strSQL = strSQL + "LEFT JOIN "
                        + "(SELECT UPN AS SPUPN,SUM(CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) AS CURRENT_PROJECTION,SUM(WIP_QTY) AS WIP_QTY,SUM(STOCK_QTY) AS STOCK_QTY,SUM(DISPATCH_QTY) AS DISPATCH_QTY,SUM(ACESSD_QTY) AS ACESS_QTY,SUM(OC_STOCK) AS PROJ_OPN_STK "
                        + " FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D WHERE D.YEAR_FROM=" + pyear + "  AND left(D.doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2))  AND ACESSD_QTY>0 GROUP BY UPN) D "
                        + "ON F.CNE_UPN=D.SPUPN "
                        + " WHERE CNE_FROM_YEAR=" + pyear + "  AND left(CNE_DOC_NO,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                        + "GROUP BY CNE_UPN "
                        + "HAVING COUNT(CNE_PIECE_NO)>0 "
                        + "ORDER BY CNE_UPN";
            }

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            int pos = 0;
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

//                    if (canupn.isSelected()) {
                    pos = 0;
                    rowData[pos] = Integer.toString(cnt);
                    pos++;
                    rowData[pos] = rsTmp.getString("UPN");
                    pos++;
                    rowData[pos] = rsTmp.getString("UPN").substring(0, 6);
                    pos++;
                    rowData[pos] = rsTmp.getString("PARTY_NAME");
                    pos++;
                    rowData[pos] = rsTmp.getString("CURRENT_PROJECTION");
                    pos++;
                    rowData[pos] = rsTmp.getString("WIP_QTY");
                    pos++;
                    rowData[pos] = rsTmp.getString("STOCK_QTY");
                    pos++;
                    if (cmbFY.getSelectedIndex() == 1) {
                        rowData[pos] = rsTmp.getString("PROJ_OPN_STK");
                        pos++;
                    }
                    rowData[pos] = rsTmp.getString("DISPATCH_QTY");
                    pos++;
                    rowData[pos] = rsTmp.getString("ACESS_QTY");
                    pos++;
                    rowData[pos] = rsTmp.getString("CANCELPIECE");
                    pos++;
                    rowData[pos] = rsTmp.getString("CAN_PIECE");
                    pos++;
                    rowData[pos] = rsTmp.getString("NONCANCELPIECE");
                    pos++;
                    rowData[pos] = rsTmp.getString("NON_CAN_PIECE");
                    pos++;
                    rowData[pos] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
                    pos++;
                    ma = ma + rsTmp.getDouble("ACESS_QTY");
                    mc = mc + rsTmp.getDouble("CANCELPIECE");
                    mnc = mnc + rsTmp.getDouble("NONCANCELPIECE");
                    //}
                    //if (canpieced.isSelected()) {
//                        rowData[0] = Integer.toString(cnt);
//                        rowData[1] = rsTmp.getString("UPN");
//                        rowData[2] = rsTmp.getString("UPN").substring(0, 6);
//                        rowData[3] = rsTmp.getString("PARTY_NAME");
//                        rowData[4] = rsTmp.getString("CURRENT_PROJECTION");
//                        rowData[5] = rsTmp.getString("WIP_QTY");
//                        rowData[6] = rsTmp.getString("STOCK_QTY");
//                        rowData[7] = rsTmp.getString("DISPATCH_QTY");
//                        rowData[8] = rsTmp.getString("ACESS_QTY");
//                        rowData[9] = rsTmp.getString("CANCELPIECE");
//                        rowData[10] = rsTmp.getString("CAN_PIECE");
//                        rowData[11] = rsTmp.getString("NONCANCELPIECE");
//                        rowData[12] = rsTmp.getString("NON_CAN_PIECE");
//                        rowData[13] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
//                        ma = ma + rsTmp.getDouble("ACESS_QTY");
//                        mc = mc + rsTmp.getDouble("CANCELPIECE");
//                        mnc = mnc + rsTmp.getDouble("NONCANCELPIECE");
//                    }
                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[3] = "Total";
                if (cmbFY.getSelectedIndex() == 0) {
                    rowData[8] = ma;
                    rowData[9] = mc;
                    rowData[11] = mnc;
                } else {
                    rowData[9] = ma;
                    rowData[10] = mc;
                    rowData[12] = mnc;
                }
                DataModel.addRow(rowData);
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

    private void FormatGridYTM() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        if (ytmrbtn.isSelected()) {
            DataModel.addColumn("Sr.");
            DataModel.addColumn("ZONE");
            DataModel.addColumn("PROD GROUP");
            DataModel.addColumn("LENGTH RANGE");
            DataModel.addColumn("Monthly Commitment");
            DataModel.addColumn("CP [" + cmnth + "] QTY");
            DataModel.addColumn("OC STOCK [Up to " + pmnth + "] QTY");
            DataModel.addColumn("DESPATCH [Up to " + pmnth + "] QTY");
            DataModel.addColumn("NonCancelable [" + cmnth + "] QTY");
            DataModel.addColumn("Prev GR [" + cmnth + "] QTY");
            DataModel.addColumn("QTY CP-STK-DSP+NC+GR");
            DataModel.addColumn("KG CP-STK-DSP+NC+GR");
            DataModel.addColumn("SQMTR CP-STK-DSP+NC+GR");
            DataModel.addColumn("VALUE  CP-STK-DSP+NC+GR");
            DataModel.addColumn("YTM [" + cmnth + "] QTY");
            DataModel.addColumn("Yet To Mfg [" + cmnth + "] QTY");
            DataModel.addColumn("Yet To Mfg [" + cmnth + "] KG");
            DataModel.addColumn("Yet To Mfg [" + cmnth + "] SQMTR");
            DataModel.addColumn("Yet To Mfg [" + cmnth + "] VALUE");
            DataModel.addColumn("Avg.Rate [" + cmnth + "]");
            DataModel.addColumn("RunTime");
        }
        if (ytmdbtn.isSelected()) {
            DataModel.addColumn("Sr.");
            DataModel.addColumn("ZONE");
            DataModel.addColumn("PROD GROUP");
            DataModel.addColumn("LENGTH RANGE");
            DataModel.addColumn("Monthly Commitment");
            DataModel.addColumn("CPD [" + cmnth + "] QTY");
            DataModel.addColumn("OC STOCK [Up to " + pmnth + "] QTY");
            DataModel.addColumn("DESPATCH [Up to " + pmnth + "] QTY");
            DataModel.addColumn("NonCancelable [" + cmnth + "] QTY");
            DataModel.addColumn("Prev GR [" + cmnth + "] QTY");
            DataModel.addColumn("QTY CP-STK-DSP+NC+GR");
            DataModel.addColumn("KG CP-STK-DSP+NC+GR");
            DataModel.addColumn("SQMTR CP-STK-DSP+NC+GR");
            DataModel.addColumn("VALUE  CP-STK-DSP+NC+GR");
            DataModel.addColumn("YTM [" + cmnth + "] QTY");
            DataModel.addColumn("Yet To Mfg [" + cmnth + "] QTY");
            DataModel.addColumn("Yet To Mfg [" + cmnth + "] KG");
            DataModel.addColumn("Yet To Mfg [" + cmnth + "] SQMTR");
            DataModel.addColumn("Yet To Mfg [" + cmnth + "] VALUE");
            DataModel.addColumn("Avg.Rate [" + cmnth + "]");
            DataModel.addColumn("RunTime");
        }
        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateYTM() {

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL  "
                + "SET CANCELABLE_PIECE_NO=0,CANCELABLE_PIECES='',NONCANCELABLE_PIECE_NO=0,NONCANCELABLE_PIECES='',"
                + "CANCELABLED_PIECE_NO=0,CANCELABLED_PIECES='',NONCANCELABLED_PIECE_NO=0,NONCANCELABLED_PIECES='' "
                + "WHERE  YEAR_FROM=" + pyear + " AND left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2))");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,"
                + "(SELECT CNE_UPN,COUNT(CASE WHEN CNE_PIECE_STAGE IN ('PLANNING','BOOKING') THEN CNE_PIECE_NO END) AS CANCELPIECE,"
                + "COUNT(CASE WHEN CNE_PIECE_STAGE NOT IN('PLANNING','BOOKING') THEN CNE_PIECE_NO END) AS NONCANCELPIECE,"
                + "COALESCE(GROUP_CONCAT(CASE WHEN CNE_PIECE_STAGE IN('PLANNING','BOOKING') THEN CONCAT(CNE_PIECE_NO,'(',CNE_PIECE_STAGE,')') END),'') AS CAN_PIECE,"
                + "COALESCE(GROUP_CONCAT(CASE WHEN CNE_PIECE_STAGE NOT IN ('PLANNING','BOOKING') THEN CONCAT(CNE_PIECE_NO,'(',CNE_PIECE_STAGE,')') END),'') AS NON_CAN_PIECE "
                + "FROM PRODUCTION.FELT_SALES_PROJECTION_CP_NCP_EXCESS F "
                + "WHERE  CNE_FROM_YEAR=" + pyear + " AND left(CNE_DOC_NO,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                + "GROUP BY CNE_UPN HAVING COUNT(CNE_PIECE_NO) > 0 ORDER BY CNE_UPN) AS N "
                + "SET CANCELABLE_PIECE_NO = CANCELPIECE, CANCELABLE_PIECES = CAN_PIECE, "
                + "NONCANCELABLE_PIECE_NO = NONCANCELPIECE, NONCANCELABLE_PIECES = NON_CAN_PIECE "
                + "WHERE YEAR_FROM=" + pyear + " AND D.UPN = N.CNE_UPN AND  YEAR_FROM=" + pyear + " AND left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2))");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,"
                + "(SELECT CNE_UPN,COUNT(CASE WHEN CNE_PIECE_STAGE IN ('PLANNING','BOOKING') THEN CNE_PIECE_NO END) AS CANCELPIECE,"
                + "COUNT(CASE WHEN CNE_PIECE_STAGE NOT IN('PLANNING','BOOKING') THEN CNE_PIECE_NO END) AS NONCANCELPIECE,"
                + "COALESCE(GROUP_CONCAT(CASE WHEN CNE_PIECE_STAGE IN('PLANNING','BOOKING') THEN CONCAT(CNE_PIECE_NO,'(',CNE_PIECE_STAGE,')') END),'') AS CAN_PIECE,"
                + "COALESCE(GROUP_CONCAT(CASE WHEN CNE_PIECE_STAGE NOT IN ('PLANNING','BOOKING') THEN CONCAT(CNE_PIECE_NO,'(',CNE_PIECE_STAGE,')') END),'') AS NON_CAN_PIECE "
                + "FROM PRODUCTION.FELT_SALES_PROJECTION_CP_NCP_EXCESS_DOUBTFUL F "
                + "WHERE CNE_FROM_YEAR=" + pyear + " AND left(CNE_DOC_NO,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                + "GROUP BY CNE_UPN HAVING COUNT(CNE_PIECE_NO) > 0 ORDER BY CNE_UPN) AS N "
                + "SET CANCELABLED_PIECE_NO = CANCELPIECE, CANCELABLED_PIECES = CAN_PIECE, "
                + "NONCANCELABLED_PIECE_NO = NONCANCELPIECE,NONCANCELABLED_PIECES = NON_CAN_PIECE "
                + "WHERE YEAR_FROM=" + pyear + " AND D.UPN = N.CNE_UPN AND YEAR_FROM=" + pyear + " AND left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2))");
        String cndtn = "";
        String grp_cndtn = "";
        try {
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gcp, gppqty2, gppkg2, gppval2, gsqmtr, gsqmtr2,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pcp, pppqty2, pppkg2, pppval2, psqmtr, psqmtr2,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zcp, zppqty2, zppkg2, zppval2, zsqmtr, zsqmtr2;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gcp = gppqty2 = gppkg2 = gppval2 = gsqmtr = gsqmtr2
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pcp = pppqty2 = pppkg2 = pppval2 = psqmtr = psqmtr2
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zcp = zppqty2 = zppkg2 = zppval2 = zsqmtr = zsqmtr2 = 0;

            String p, z;
            FormatGridYTM(); //clear existing content of table
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
            if (ytmrbtn.isSelected()) {
                strSQL = "SELECT *,ROUND(YTM1*WEIGHT,2) AS YTMKG,ROUND(YTM1*SQMTR,2) AS YTMSQMTR,"
                        + "ROUND(YTM1*SELLING_PRICE,2) AS YTMVAL,"
                        + "ROUND(YTM2*WEIGHT,2) AS YTMKG2,ROUND(YTM2*SQMTR,2) AS YTMSQMTR2,"
                        + "ROUND(YTM2*SELLING_PRICE,2) AS YTMVAL2 FROM "
                        //+ "(SELECT AVG(SELLING_PRICE-COALESCE(PENDING_VALUE ,0)) AS SELLING_PRICE,"
                        + "(SELECT AVG(SELLING_PRICE) AS SELLING_PRICE,"
                        + "AVG(PRESS_WEIGHT) AS WEIGHT,"
                        + "AVG(DRY_SQMTR) AS SQMTR,"
                        + "INCHARGE_NAME,PROD_GROUP,SIZE_CRITERIA,PRODUCT_CAPACITY,SUM(COALESCE(CURRENT_PROJECTION,0)) AS SP,"
                        + "SUM(COALESCE(OC_STOCK,0)) AS OC,"
                        + "SUM(COALESCE(DISPATCH_QTY,0)) AS DISP,SUM(COALESCE(NONCANCELABLE_PIECE_NO,0)) AS NCAN,SUM(COALESCE(PREV_GR_QTY,0)) AS PGR,"
                        + "CEILING((SUM(COALESCE(CURRENT_PROJECTION,0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                        + "+SUM(COALESCE(NONCANCELABLE_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0)))/" + remainmonth + ") AS YTM,"
                        + "ROUND((SUM(COALESCE(CURRENT_PROJECTION,0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                        + "+SUM(COALESCE(NONCANCELABLE_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0)))/" + remainmonth + ",2) AS YTM1,"
                        + "ROUND((SUM(COALESCE(CURRENT_PROJECTION,0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                        + "+SUM(COALESCE(NONCANCELABLE_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0))),2) AS YTM2 "
                        + " FROM (SELECT D.*,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                        + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP,INCHARGE_NAME FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS INCHRG "
                        + "ON INCHARGE=INCHARGE_CD "
                        + "where (CURRENT_PROJECTION+COALESCE(NONCANCELABLE_PIECE_NO,0))!=0 "
                        + cndtn
                        + " AND YEAR_FROM=" + pyear + " AND left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2))) AS D "
                        + "LEFT JOIN PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
                        + "ON D.PROD_GROUP=SUBSTRING(PRODUCT_CAPTION,4) AND D.INCHARGE=INCHARGE_CODE "
                        + "AND CASE WHEN LENGTH(TRIM(SIZE_CRITERIA))<5 THEN 0 ELSE LEFT(SIZE_CRITERIA,1) END=MTR_CAPTION_CODE "
                        + "GROUP BY INCHARGE,PROD_GROUP,SIZE_CRITERIA "
                        + "ORDER BY INCHARGE,PROD_GROUP,SIZE_CRITERIA) AS DD";
            }
            if (ytmdbtn.isSelected()) {
                strSQL = "SELECT *,ROUND(YTM1*WEIGHT,2) AS YTMKG,ROUND(YTM1*SQMTR,2) AS YTMSQMTR,"
                        + "ROUND(YTM1*SELLING_PRICE,2) AS YTMVAL,"
                        + "ROUND(YTM2*WEIGHT,2) AS YTMKG2,ROUND(YTM2*SQMTR,2) AS YTMSQMTR2,"
                        + "ROUND(YTM2*SELLING_PRICE,2) AS YTMVAL2 FROM "
                        //+ "(SELECT AVG(SELLING_PRICE-COALESCE(PENDING_VALUE ,0)) AS SELLING_PRICE,"
                        + "(SELECT AVG(SELLING_PRICE) AS SELLING_PRICE,"
                        + "AVG(PRESS_WEIGHT) AS WEIGHT,"
                        + "AVG(DRY_SQMTR) AS SQMTR,"
                        + "INCHARGE_NAME,PROD_GROUP,SIZE_CRITERIA,PRODUCT_CAPACITY,SUM(COALESCE((CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)),0)) AS SP,"
                        + "SUM(COALESCE(OC_STOCK,0)) AS OC,SUM(COALESCE(DISPATCH_QTY,0)) AS DISP,SUM(COALESCE(NONCANCELABLED_PIECE_NO,0)) AS NCAN,SUM(COALESCE(PREV_GR_QTY,0)) AS PGR,"
                        + "CEILING((SUM(COALESCE((CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)),0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                        + "+SUM(COALESCE(NONCANCELABLED_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0)))/" + remainmonth + ") AS YTM,"
                        + "ROUND((SUM(COALESCE((CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)),0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                        + "+SUM(COALESCE(NONCANCELABLED_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0)))/" + remainmonth + ",2) AS YTM1,  "
                        + "ROUND((SUM(COALESCE((CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)),0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                        + "+SUM(COALESCE(NONCANCELABLED_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0))),2) AS YTM2 "
                        + " FROM (SELECT D.*,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                        + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP,INCHARGE_NAME FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS INCHRG "
                        + "ON INCHARGE=INCHARGE_CD "
                        + "where (CURRENT_PROJECTION+COALESCE(NONCANCELABLED_PIECE_NO,0))!=0 "
                        + cndtn
                        + " AND  YEAR_FROM=" + pyear + " AND left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2))) AS D "
                        + "LEFT JOIN PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
                        + "ON D.PROD_GROUP=SUBSTRING(PRODUCT_CAPTION,4) AND D.INCHARGE=INCHARGE_CODE "
                        + "AND CASE WHEN LENGTH(TRIM(SIZE_CRITERIA))<5 THEN 0 ELSE LEFT(SIZE_CRITERIA,1) END=MTR_CAPTION_CODE "
                        + "GROUP BY INCHARGE,PROD_GROUP,SIZE_CRITERIA "
                        + "ORDER BY INCHARGE,PROD_GROUP,SIZE_CRITERIA) AS DD";
            }
            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                p = rsTmp.getString("PROD_GROUP");
                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    if (!p.equalsIgnoreCase(rsTmp.getString("PROD_GROUP")) || !z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[2] = "Product Total";
                        rowData[4] = String.format("%.2f", pcp);
                        rowData[5] = String.format("%.2f", pobqty);
                        rowData[6] = String.format("%.2f", pobkg);
                        rowData[7] = String.format("%.2f", pobval);
                        rowData[8] = String.format("%.2f", pppqty);
                        rowData[9] = String.format("%.2f", pppkg);
                        rowData[10] = String.format("%.2f", pppqty2);
                        rowData[11] = String.format("%.2f", pppkg2);
                        rowData[12] = String.format("%.2f", psqmtr2);
                        rowData[13] = String.format("%.2f", pppval2);
                        rowData[15] = String.format("%.2f", pppval);
                        rowData[16] = String.format("%.2f", pcpqty);
                        rowData[17] = String.format("%.2f", psqmtr);
                        rowData[18] = String.format("%.2f", pcpkg);

                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        p = rsTmp.getString("PROD_GROUP");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pcp = pppqty2 = pppkg2 = pppval2 = psqmtr = psqmtr2 = 0;
                    }
                    if (!z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Zone Total";
                        rowData[4] = String.format("%.2f", zcp);
                        rowData[5] = String.format("%.2f", zobqty);
                        rowData[6] = String.format("%.2f", zobkg);
                        rowData[7] = String.format("%.2f", zobval);
                        rowData[8] = String.format("%.2f", zppqty);
                        rowData[9] = String.format("%.2f", zppkg);
                        rowData[10] = String.format("%.2f", zppqty2);
                        rowData[11] = String.format("%.2f", zppkg2);
                        rowData[12] = String.format("%.2f", zsqmtr2);
                        rowData[13] = String.format("%.2f", zppval2);
                        rowData[15] = String.format("%.2f", zppval);
                        rowData[16] = String.format("%.2f", zcpqty);
                        rowData[17] = String.format("%.2f", zsqmtr);
                        rowData[18] = String.format("%.2f", zcpkg);

                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        z = rsTmp.getString("INCHARGE_NAME");
                        zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zcp = zppqty2 = zppkg2 = zppval2 = zsqmtr = zsqmtr2 = 0;
                    }
                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INCHARGE_NAME");
                    rowData[2] = rsTmp.getString("PROD_GROUP");
                    rowData[3] = rsTmp.getString("SIZE_CRITERIA");
                    rowData[4] = rsTmp.getString("PRODUCT_CAPACITY");
                    rowData[5] = rsTmp.getString("SP");
                    rowData[6] = rsTmp.getString("OC");
                    rowData[7] = rsTmp.getString("DISP");
                    rowData[8] = rsTmp.getString("NCAN");
                    rowData[9] = rsTmp.getString("PGR");
                    rowData[10] = rsTmp.getString("YTM2");
                    rowData[11] = rsTmp.getString("YTMKG2");
                    rowData[12] = rsTmp.getString("YTMSQMTR2");
                    rowData[13] = rsTmp.getString("YTMVAL2");
                    rowData[14] = rsTmp.getString("YTM1");
                    rowData[15] = rsTmp.getString("YTM");
                    rowData[16] = rsTmp.getString("YTMKG");
                    rowData[17] = rsTmp.getString("YTMSQMTR");
                    rowData[18] = rsTmp.getString("YTMVAL");
                    rowData[19] = String.format("%.2f", rsTmp.getDouble("YTMVAL") / (rsTmp.getDouble("YTMKG") + rsTmp.getDouble("YTMSQMTR")));
                    rowData[20] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
                    gobqty = gobqty + rsTmp.getDouble("SP");
                    gobkg = gobkg + rsTmp.getDouble("OC");
                    gobval = gobval + rsTmp.getDouble("DISP");
                    gppqty = gppqty + rsTmp.getDouble("NCAN");
                    gppkg = gppkg + rsTmp.getDouble("PGR");
                    gppval = gppval + rsTmp.getDouble("YTM");
                    gcpqty = gcpqty + rsTmp.getDouble("YTMKG");
                    gcpkg = gcpkg + rsTmp.getDouble("YTMVAL");
                    gppqty2 = gppqty2 + rsTmp.getDouble("YTM2");
                    gppkg2 = gppkg2 + rsTmp.getDouble("YTMKG2");
                    gppval2 = gppval2 + rsTmp.getDouble("YTMVAL2");
                    gcp = gcp + rsTmp.getDouble("PRODUCT_CAPACITY");
                    gsqmtr = gsqmtr + rsTmp.getDouble("YTMSQMTR");
                    gsqmtr2 = gsqmtr2 + rsTmp.getDouble("YTMSQMTR2");

                    pobqty = pobqty + rsTmp.getDouble("SP");
                    pobkg = pobkg + rsTmp.getDouble("OC");
                    pobval = pobval + rsTmp.getDouble("DISP");
                    pppqty = pppqty + rsTmp.getDouble("NCAN");
                    pppkg = pppkg + rsTmp.getDouble("PGR");
                    pppval = pppval + rsTmp.getDouble("YTM");
                    pcpqty = pcpqty + rsTmp.getDouble("YTMKG");
                    pcpkg = pcpkg + rsTmp.getDouble("YTMVAL");
                    pppqty2 = pppqty2 + rsTmp.getDouble("YTM2");
                    pppkg2 = pppkg2 + rsTmp.getDouble("YTMKG2");
                    pppval2 = pppval2 + rsTmp.getDouble("YTMVAL2");
                    pcp = pcp + rsTmp.getDouble("PRODUCT_CAPACITY");
                    psqmtr = psqmtr + rsTmp.getDouble("YTMSQMTR");
                    psqmtr2 = psqmtr2 + rsTmp.getDouble("YTMSQMTR2");

                    zobqty = zobqty + rsTmp.getDouble("SP");
                    zobkg = zobkg + rsTmp.getDouble("OC");
                    zobval = zobval + rsTmp.getDouble("DISP");
                    zppqty = zppqty + rsTmp.getDouble("NCAN");
                    zppkg = zppkg + rsTmp.getDouble("PGR");
                    zppval = zppval + rsTmp.getDouble("YTM");
                    zcpqty = zcpqty + rsTmp.getDouble("YTMKG");
                    zcpkg = zcpkg + rsTmp.getDouble("YTMVAL");
                    zppqty2 = zppqty2 + rsTmp.getDouble("YTM2");
                    zppkg2 = zppkg2 + rsTmp.getDouble("YTMKG2");
                    zppval2 = zppval2 + rsTmp.getDouble("YTMVAL2");
                    zcp = zcp + rsTmp.getDouble("PRODUCT_CAPACITY");
                    zsqmtr = zsqmtr + rsTmp.getDouble("YTMSQMTR");
                    zsqmtr2 = zsqmtr2 + rsTmp.getDouble("YTMSQMTR2");

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[2] = "Product Total";
                rowData[4] = String.format("%.2f", pcp);
                rowData[5] = String.format("%.2f", pobqty);
                rowData[6] = String.format("%.2f", pobkg);
                rowData[7] = String.format("%.2f", pobval);
                rowData[8] = String.format("%.2f", pppqty);
                rowData[9] = String.format("%.2f", pppkg);
                rowData[10] = String.format("%.2f", pppqty2);
                rowData[11] = String.format("%.2f", pppkg2);
                rowData[12] = String.format("%.2f", psqmtr2);
                rowData[13] = String.format("%.2f", pppval2);
                rowData[15] = String.format("%.2f", pppval);
                rowData[16] = String.format("%.2f", pcpqty);
                rowData[17] = String.format("%.2f", psqmtr);
                rowData[18] = String.format("%.2f", pcpkg);

                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pppqty2 = pppkg2 = pppval2 = 0;

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Zone Total";
                rowData[4] = String.format("%.2f", zcp);
                rowData[5] = String.format("%.2f", zobqty);
                rowData[6] = String.format("%.2f", zobkg);
                rowData[7] = String.format("%.2f", zobval);
                rowData[8] = String.format("%.2f", zppqty);
                rowData[9] = String.format("%.2f", zppkg);
                rowData[10] = String.format("%.2f", zppqty2);
                rowData[11] = String.format("%.2f", zppkg2);
                rowData[12] = String.format("%.2f", zsqmtr2);
                rowData[13] = String.format("%.2f", zppval2);
                rowData[15] = String.format("%.2f", zppval);
                rowData[16] = String.format("%.2f", zcpqty);
                rowData[17] = String.format("%.2f", zsqmtr);
                rowData[18] = String.format("%.2f", zcpkg);

                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zppqty2 = zppkg2 = zppval2 = 0;
                if (ytmrbtn.isSelected()) {
                    strSQL = "SELECT PROD_GROUP,SIZE_CRITERIA,(PRODCAPACITY) AS PRODCAPACITY,SUM(SP) AS SP,SUM(OC) AS OC,SUM(DISP) AS DISP,SUM(NCAN) AS NCAN,SUM(PGR) AS PGR,"
                            + "CEILING(SUM(YTM1)) AS YTM,SUM(YTM1) AS YTM1,SUM(YTMKG) AS YTMKG,"
                            + "SUM(YTMSQMTR) AS YTMSQMTR,SUM(YTMSQMTR2) AS YTMSQMTR2,"
                            + "SUM(YTMVAL) AS YTMVAL,SUM(YTM2) AS YTM2,SUM(YTMKG2) AS YTMKG2,SUM(YTMVAL2) AS YTMVAL2 "
                            + " FROM (SELECT *,ROUND(YTM1*WEIGHT,2) AS YTMKG,ROUND(YTM1*SQMTR,2) AS YTMSQMTR,"
                            + "ROUND(YTM1*SELLING_PRICE,2) AS YTMVAL,"
                            + "ROUND(YTM2*WEIGHT,2) AS YTMKG2,ROUND(YTM2*SQMTR,2) AS YTMSQMTR2,"
                            + "ROUND(YTM2*SELLING_PRICE,2) AS YTMVAL2 FROM "
                            //+ "(SELECT AVG(SELLING_PRICE-COALESCE(PENDING_VALUE ,0)) AS SELLING_PRICE,"
                            + "(SELECT AVG(SELLING_PRICE) AS SELLING_PRICE,"
                            + "AVG(PRESS_WEIGHT) AS WEIGHT,"
                            + "AVG(DRY_SQMTR) AS SQMTR,"
                            + "INCHARGE_NAME,PROD_GROUP,SIZE_CRITERIA,PRODCAPACITY,SUM(COALESCE(CURRENT_PROJECTION,0)) AS SP,"
                            + "SUM(COALESCE(OC_STOCK,0)) AS OC,SUM(COALESCE(DISPATCH_QTY,0)) AS DISP,SUM(COALESCE(NONCANCELABLE_PIECE_NO,0)) AS NCAN,SUM(COALESCE(PREV_GR_QTY,0)) AS PGR,"
                            + "CEILING((SUM(COALESCE(CURRENT_PROJECTION,0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                            + "+SUM(COALESCE(NONCANCELABLE_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0)))/" + remainmonth + ") AS YTM,"
                            + "ROUND((SUM(COALESCE(CURRENT_PROJECTION,0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                            + "+SUM(COALESCE(NONCANCELABLE_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0)))/" + remainmonth + ",2) AS YTM1,  "
                            + "ROUND((SUM(COALESCE(CURRENT_PROJECTION,0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                            + "+SUM(COALESCE(NONCANCELABLE_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0))),2) AS YTM2 "
                            + " FROM (SELECT D.*,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                            + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP,INCHARGE_NAME FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D "
                            + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS INCHRG "
                            + "ON INCHARGE=INCHARGE_CD "
                            + "WHERE (CURRENT_PROJECTION+COALESCE(NONCANCELABLE_PIECE_NO,0))!=0 "
                            + cndtn
                            + " AND  YEAR_FROM=" + pyear + " AND left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2))) AS D "
                            + "LEFT JOIN (SELECT SUM(PRODUCT_CAPACITY) AS PRODCAPACITY,D.* FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY D WHERE INCHARGE_CODE IN (1,2,3,5,6,7) "
                            + "AND MTR_CAPTION_CODE!=10 GROUP BY PRODUCT_CAPTION,MTR_CAPTION) AS CAPACITY "
                            + "ON D.PROD_GROUP=SUBSTRING(PRODUCT_CAPTION,4) AND CASE WHEN LENGTH(TRIM(SIZE_CRITERIA))<5 THEN 0 ELSE LEFT(SIZE_CRITERIA,1) END=MTR_CAPTION_CODE "
                            + "WHERE MTR_CAPTION_CODE!=10 "
                            + "GROUP BY INCHARGE,PROD_GROUP,SIZE_CRITERIA "
                            + "ORDER BY INCHARGE,PROD_GROUP,SIZE_CRITERIA) AS DD ) AS DDD "
                            + "GROUP BY PROD_GROUP,SIZE_CRITERIA";
                }
                if (ytmdbtn.isSelected()) {
                    strSQL = "SELECT PROD_GROUP,SIZE_CRITERIA,(PRODCAPACITY) AS PRODCAPACITY,SUM(SP) AS SP,SUM(OC) AS OC,SUM(DISP) AS DISP,SUM(NCAN) AS NCAN,SUM(PGR) AS PGR,"
                            + "CEILING(SUM(YTM1)) AS YTM,SUM(YTM1) AS YTM1,SUM(YTMKG) AS YTMKG,"
                            + "SUM(YTMSQMTR) AS YTMSQMTR,SUM(YTMSQMTR2) AS YTMSQMTR2,"
                            + "SUM(YTMVAL) AS YTMVAL,"
                            + "SUM(YTM2) AS YTM2,SUM(YTMKG2) AS YTMKG2,SUM(YTMVAL2) AS YTMVAL2 "
                            + " FROM (SELECT *,ROUND(YTM1*WEIGHT,2) AS YTMKG,ROUND(YTM1*SQMTR,2) AS YTMSQMTR,"
                            + "ROUND(YTM1*SELLING_PRICE,2) AS YTMVAL,"
                            + "ROUND(YTM2*WEIGHT,2) AS YTMKG2,ROUND(YTM2*SQMTR,2) AS YTMSQMTR2,"
                            + "ROUND(YTM2*SELLING_PRICE,2) AS YTMVAL2 FROM "
                            //+ "(SELECT AVG(SELLING_PRICE-COALESCE(PENDING_VALUE ,0)) AS SELLING_PRICE,"
                            + "(SELECT AVG(SELLING_PRICE) AS SELLING_PRICE,"
                            + "AVG(PRESS_WEIGHT) AS WEIGHT,"
                            + "AVG(DRY_SQMTR) AS SQMTR,"
                            + "INCHARGE_NAME,PROD_GROUP,SIZE_CRITERIA,PRODCAPACITY,SUM(COALESCE((CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)),0)) AS SP,"
                            + "SUM(COALESCE(OC_STOCK,0)) AS OC,SUM(COALESCE(DISPATCH_QTY,0)) AS DISP,SUM(COALESCE(NONCANCELABLED_PIECE_NO,0)) AS NCAN,SUM(COALESCE(PREV_GR_QTY,0)) AS PGR,"
                            + "CEILING((SUM(COALESCE((CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)),0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                            + "+SUM(COALESCE(NONCANCELABLED_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0)))/" + remainmonth + ") AS YTM ,"
                            + "ROUND((SUM(COALESCE((CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)),0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                            + "+SUM(COALESCE(NONCANCELABLED_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0)))/" + remainmonth + ",2) AS YTM1,  "
                            + "ROUND((SUM(COALESCE((CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)),0))-SUM(COALESCE(OC_STOCK,0))-SUM(COALESCE(DISPATCH_QTY,0)) "
                            + "+SUM(COALESCE(NONCANCELABLED_PIECE_NO,0))+SUM(COALESCE(PREV_GR_QTY,0))),2) AS YTM2 "
                            + " FROM (SELECT D.*,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' WHEN GROUP_NAME IN ('HDS') THEN 'HDS' "
                            + "WHEN GROUP_NAME IN ('SDF') THEN 'SDF' ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP,INCHARGE_NAME FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D "
                            + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS INCHRG "
                            + "ON INCHARGE=INCHARGE_CD "
                            + "WHERE (CURRENT_PROJECTION+COALESCE(NONCANCELABLED_PIECE_NO,0))!=0 "
                            + cndtn
                            + " AND  YEAR_FROM=" + pyear + " AND left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2))) AS D "
                            + "LEFT JOIN (SELECT SUM(PRODUCT_CAPACITY) AS PRODCAPACITY,D.* FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY D WHERE INCHARGE_CODE IN (1,2,3,5,6,7) "
                            + "AND MTR_CAPTION_CODE!=10 GROUP BY PRODUCT_CAPTION,MTR_CAPTION) AS CAPACITY "
                            + "ON D.PROD_GROUP=SUBSTRING(PRODUCT_CAPTION,4) AND CASE WHEN LENGTH(TRIM(SIZE_CRITERIA))<5 THEN 0 ELSE LEFT(SIZE_CRITERIA,1) END=MTR_CAPTION_CODE "
                            + "WHERE MTR_CAPTION_CODE!=10 "
                            + "GROUP BY INCHARGE,PROD_GROUP,SIZE_CRITERIA "
                            + "ORDER BY INCHARGE,PROD_GROUP,SIZE_CRITERIA) AS DD ) AS DDD "
                            + "GROUP BY PROD_GROUP,SIZE_CRITERIA";
                }

                System.out.println("Query..." + strSQL);
                rsTmp = data.getResult(strSQL);
                rsTmp.first();
                pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pcp = pppqty2 = pppkg2 = pppval2 = psqmtr = psqmtr2 = 0;
                p = rsTmp.getString("PROD_GROUP");
                while (!rsTmp.isAfterLast()) {

                    if (!p.equalsIgnoreCase(rsTmp.getString("PROD_GROUP"))) {
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[2] = "Product Total";
                        rowData[4] = String.format("%.2f", pcp);
                        rowData[5] = String.format("%.2f", pobqty);
                        rowData[6] = String.format("%.2f", pobkg);
                        rowData[7] = String.format("%.2f", pobval);
                        rowData[8] = String.format("%.2f", pppqty);
                        rowData[9] = String.format("%.2f", pppkg);
                        rowData[10] = String.format("%.2f", pppqty2);
                        rowData[11] = String.format("%.2f", pppkg2);
                        rowData[12] = String.format("%.2f", psqmtr2);
                        rowData[13] = String.format("%.2f", pppval2);
                        rowData[15] = String.format("%.2f", pppval);
                        rowData[16] = String.format("%.2f", pcpqty);
                        rowData[17] = String.format("%.2f", psqmtr);
                        rowData[18] = String.format("%.2f", pcpkg);

                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        p = rsTmp.getString("PROD_GROUP");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pcp = pppqty2 = pppkg2 = pppval2 = psqmtr = psqmtr2 = 0;
                    }

                    rowData = new Object[100];

                    rowData[2] = rsTmp.getString("PROD_GROUP");
                    rowData[3] = rsTmp.getString("SIZE_CRITERIA");
                    rowData[4] = rsTmp.getString("PRODCAPACITY");
                    rowData[5] = rsTmp.getString("SP");
                    rowData[6] = rsTmp.getString("OC");
                    rowData[7] = rsTmp.getString("DISP");
                    rowData[8] = rsTmp.getString("NCAN");
                    rowData[9] = rsTmp.getString("PGR");
                    rowData[10] = rsTmp.getString("YTM2");
                    rowData[11] = rsTmp.getString("YTMKG2");
                    rowData[12] = rsTmp.getString("YTMSQMTR2");
                    rowData[13] = rsTmp.getString("YTMVAL2");
                    //rowData[14] = rsTmp.getString("YTM1");
                    rowData[15] = rsTmp.getString("YTM");
                    rowData[16] = rsTmp.getString("YTMKG");
                    rowData[17] = rsTmp.getString("YTMSQMTR");
                    rowData[18] = rsTmp.getString("YTMVAL");
                    rowData[19] = String.format("%.2f", rsTmp.getDouble("YTMVAL") / (rsTmp.getDouble("YTMKG") + rsTmp.getDouble("YTMSQMTR")));
                    rowData[20] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();

                    DataModel.addRow(rowData);
                    pobqty = pobqty + rsTmp.getDouble("SP");
                    pobkg = pobkg + rsTmp.getDouble("OC");
                    pobval = pobval + rsTmp.getDouble("DISP");
                    pppqty = pppqty + rsTmp.getDouble("NCAN");
                    pppkg = pppkg + rsTmp.getDouble("PGR");
                    pppqty2 = pppqty2 + rsTmp.getDouble("YTM2");
                    pppkg2 = pppkg2 + rsTmp.getDouble("YTMKG2");
                    pppval = pppval + rsTmp.getDouble("YTM");
                    pppval2 = pppval2 + rsTmp.getDouble("YTMVAL2");
                    pcpqty = pcpqty + rsTmp.getDouble("YTMKG");
                    pcpkg = pcpkg + rsTmp.getDouble("YTMVAL");
                    pcp = pcp + rsTmp.getDouble("PRODCAPACITY");
                    psqmtr = psqmtr + rsTmp.getDouble("YTMSQMTR");
                    psqmtr2 = psqmtr2 + rsTmp.getDouble("YTMSQMTR2");
                    rsTmp.next();
                }
                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[2] = "Product Total";
                rowData[4] = String.format("%.2f", pcp);
                rowData[5] = String.format("%.2f", pobqty);
                rowData[6] = String.format("%.2f", pobkg);
                rowData[7] = String.format("%.2f", pobval);
                rowData[8] = String.format("%.2f", pppqty);
                rowData[9] = String.format("%.2f", pppkg);
                rowData[10] = String.format("%.2f", pppqty2);
                rowData[11] = String.format("%.2f", pppkg2);
                rowData[12] = String.format("%.2f", psqmtr2);
                rowData[13] = String.format("%.2f", pppval2);
                rowData[15] = String.format("%.2f", pppval);
                rowData[16] = String.format("%.2f", pcpqty);
                rowData[17] = String.format("%.2f", psqmtr);
                rowData[18] = String.format("%.2f", pcpkg);

                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[4] = String.format("%.2f", gcp);
                rowData[5] = String.format("%.2f", gobqty);
                rowData[6] = String.format("%.2f", gobkg);
                rowData[7] = String.format("%.2f", gobval);
                rowData[8] = String.format("%.2f", gppqty);
                rowData[9] = String.format("%.2f", gppkg);
                rowData[10] = String.format("%.2f", gppqty2);
                rowData[11] = String.format("%.2f", gppkg2);
                rowData[12] = String.format("%.2f", gsqmtr2);
                rowData[13] = String.format("%.2f", gppval2);
                rowData[15] = String.format("%.2f", gppval);
                rowData[16] = String.format("%.2f", gcpqty);
                rowData[17] = String.format("%.2f", gsqmtr);
                rowData[18] = String.format("%.2f", gcpkg);

                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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

    private void FormatGridSFUP() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("ZONE");
        DataModel.addColumn("UPN");
        DataModel.addColumn("Machine");
        DataModel.addColumn("Position");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Name");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("Prev GR");
        DataModel.addColumn("Current Projection + GR");
        DataModel.addColumn("WIP with OC [" + cmnth + "] QTY");
        DataModel.addColumn("Stock QTY");
        if (cmbFY.getSelectedIndex() == 1) {
            DataModel.addColumn("Proj Opening Qty");
        }
        DataModel.addColumn("Despatch QTY");
        DataModel.addColumn("Priority till Month [" + cmnth + "] QTY");
        DataModel.addColumn("Followup Priority");
        DataModel.addColumn("Follow up with PP");
        DataModel.addColumn("Followup with OC/Order");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateSFUP() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gp, gfupp, gfus, gppopstk,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pp, pfupp, pfus, pppopstk,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zp, zfupp, zfus, zppopstk;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gp = gfupp = gfus = gppopstk
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pp = pcpval = pfupp = pfus = pppopstk
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zp = zcpval = zfupp = zfus = zppopstk = 0;

            String p, z;
            FormatGridSFUP(); //clear existing content of table
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
            String strSQL = "SELECT *,"
                    + "COALESCE(CASE WHEN FOLLOWUP='YES' AND PRIORITY>(PROJ_OPN_STK+STOCKQTY+DISPATCHQTY) THEN CASE WHEN WIPQTY>0 THEN CASE WHEN PRIORITY-(PROJ_OPN_STK+STOCKQTY+DISPATCHQTY)>WIPQTY THEN WIPQTY ELSE PRIORITY-(PROJ_OPN_STK+STOCKQTY+DISPATCHQTY) END END END,0) AS FOLLOWUPPP,"
                    + "COALESCE(CASE WHEN FOLLOWUP='YES' AND PRIORITY>(PROJ_OPN_STK+STOCKQTY+DISPATCHQTY) THEN CASE WHEN PRIORITY>(WIPQTY+PROJ_OPN_STK+STOCKQTY+DISPATCHQTY) THEN PRIORITY-(WIPQTY+PROJ_OPN_STK+STOCKQTY+DISPATCHQTY) END END,0) AS FOLLOWUPSALE "
                    + " FROM (SELECT *,CEILING((CP_PGR/12)*" + currentmonth + ") AS PRIORITY,"
                    + "CASE WHEN CEILING((CP_PGR/12)*" + currentmonth + ")>DISPATCHQTY THEN 'YES' ELSE 'NO' END AS FOLLOWUP "
                    + "FROM (SELECT INCHARGE_NAME,UPN,MACHINE_NO,POSITION_DESC,PARTY_CODE,PARTY_NAME,"
                    + "COALESCE(CURRENT_PROJECTION,0) AS CURRENT_PROJECTION,COALESCE(PREV_GR_QTY,0) AS PREV_GR_QTY,COALESCE(CURRENT_PROJECTION,0)+COALESCE(PREV_GR_QTY,0) AS CP_PGR,"
                    + "COALESCE(WIPQTY,0) AS WIPQTY,COALESCE(STOCK_QTY,0) AS STOCKQTY,COALESCE(DISPATCH_QTY,0) AS DISPATCHQTY,";
            if (cmbFY.getSelectedIndex() == 0) {
                strSQL = strSQL + "0 AS PROJ_OPN_STK ";
            }
            if (cmbFY.getSelectedIndex() == 1) {
                strSQL = strSQL + "COALESCE(OC_STOCK,0) AS PROJ_OPN_STK ";
            }
            strSQL = strSQL + " FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "LEFT JOIN (SELECT PR_UPN,COUNT(PR_UPN) AS PGR,SUM(NET_AMOUNT) AS PGRVAL,SUM(ACTUAL_WEIGHT) AS PGRKG FROM (SELECT PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
                    + "WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ";
            if (pyear.equalsIgnoreCase("2020")) {
                strSQL = strSQL + " AND INVOICE_DATE<'2020-04-01' AND H.DOC_DATE>='2020-04-01' AND H.DOC_DATE<='2021-03-31') AS A ";
            } else {
                strSQL = strSQL + " AND INVOICE_DATE<'2021-04-01' AND H.DOC_DATE>='2021-04-01' AND H.DOC_DATE<='2022-03-31') AS A ";
            }
            strSQL = strSQL + "LEFT JOIN (SELECT PR_PIECE_NO,PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) AS P "
                    + "ON PIECE_NO=PR_PIECE_NO "
                    + "GROUP BY PR_UPN) AS PGR "
                    + "ON PGR.PR_UPN=UPN "
                    + "LEFT JOIN (SELECT PR_UPN,COUNT(*) AS WIPQTY,SUM(PR_FELT_VALUE_WITH_GST) AS WIPVAL FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "WHERE PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','SDML','DIVERTED_FINISHING_STOCK','HEAT_SETTING','MARKING','SPLICING') "
                    + " AND COALESCE(PR_DELINK,'')!='OBSOLETE' ";
            if (curmnth < 4) {
                strSQL = strSQL + "AND UPPER(PR_OC_MONTHYEAR)='" + cmnth + " - " + (Integer.parseInt(pyear) + 1) + "' ";
            } else {
                strSQL = strSQL + "AND UPPER(PR_OC_MONTHYEAR)='" + cmnth + " - " + pyear + "' ";
            }
            strSQL = strSQL + "AND ";
            if (pyear.equalsIgnoreCase("2020")) {
                strSQL = strSQL + "((PR_REQ_MTH_LAST_DDMMYY>='2020-04-01' AND PR_REQ_MTH_LAST_DDMMYY<='2021-03-31') "
                        + "OR "
                        + "(PR_CURRENT_SCH_LAST_DDMMYY>='2020-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2021-03-31')) ";
            } else {
                strSQL = strSQL + "((PR_REQ_MTH_LAST_DDMMYY>='2021-04-01' AND PR_REQ_MTH_LAST_DDMMYY<='2022-03-31') "
                        + "OR "
                        + "(PR_CURRENT_SCH_LAST_DDMMYY>='2021-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2022-03-31')) ";
            }
            strSQL = strSQL + "GROUP BY PR_UPN) AS WIP "
                    + "ON WIP.PR_UPN=UPN "
                    + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS INCHRG "
                    + "ON INCHARGE=INCHARGE_CD "
                    + "WHERE YEAR_FROM=" + pyear + "  AND left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + cndtn + " ) AS DD) AS D "
                    + "ORDER BY INCHARGE_NAME,PARTY_CODE,UPN ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0, pos;
                p = rsTmp.getString("PARTY_CODE");
                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    if (!p.equalsIgnoreCase(rsTmp.getString("PARTY_CODE")) || !z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[5] = "Party Total";
                        rowData[7] = String.format("%.2f", pobqty);
                        rowData[8] = String.format("%.2f", pobkg);
                        rowData[9] = String.format("%.2f", pobval);
                        rowData[10] = String.format("%.2f", pppqty);
                        rowData[11] = String.format("%.2f", pppkg);
                        if (cmbFY.getSelectedIndex() == 0) {
                            rowData[12] = String.format("%.2f", pppval);
                            rowData[13] = String.format("%.2f", pcpqty);
                            rowData[15] = String.format("%.2f", pfupp);
                            rowData[16] = String.format("%.2f", pfus);
                        }
                        if (cmbFY.getSelectedIndex() == 1) {
                            rowData[12] = String.format("%.2f", pppopstk);
                            rowData[13] = String.format("%.2f", pppval);
                            rowData[14] = String.format("%.2f", pcpqty);
                            rowData[16] = String.format("%.2f", pfupp);
                            rowData[17] = String.format("%.2f", pfus);
                        }
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        p = rsTmp.getString("PARTY_CODE");
                        pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pfupp = pfus = pppopstk=0;
                    }
                    if (!z.equalsIgnoreCase(rsTmp.getString("INCHARGE_NAME"))) {
                        Object[] rowData = new Object[100];
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        rowData[1] = "Zone Total";
                        rowData[7] = String.format("%.2f", zobqty);
                        rowData[8] = String.format("%.2f", zobkg);
                        rowData[9] = String.format("%.2f", zobval);
                        rowData[10] = String.format("%.2f", zppqty);
                        rowData[11] = String.format("%.2f", zppkg);
                        if (cmbFY.getSelectedIndex() == 0) {
                            rowData[12] = String.format("%.2f", zppval);
                            rowData[13] = String.format("%.2f", zcpqty);
                            rowData[15] = String.format("%.2f", zfupp);
                            rowData[16] = String.format("%.2f", zfus);
                        }
                        if (cmbFY.getSelectedIndex() == 1) {
                            rowData[12] = String.format("%.2f", zppopstk);
                            rowData[13] = String.format("%.2f", zppval);
                            rowData[14] = String.format("%.2f", zcpqty);
                            rowData[16] = String.format("%.2f", zfupp);
                            rowData[17] = String.format("%.2f", zfus);
                        }
                        DataModel.addRow(rowData);
                        rowData = new Object[100];
                        DataModel.addRow(rowData);
                        z = rsTmp.getString("INCHARGE_NAME");
                        zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zfupp = zfus = zppopstk=0;
                    }
                    Object[] rowData = new Object[100];
                    pos = 0;
                    rowData[pos] = Integer.toString(cnt);
                    pos++;
                    rowData[pos] = rsTmp.getString("INCHARGE_NAME");
                    pos++;
                    rowData[pos] = rsTmp.getString("UPN");
                    pos++;
                    rowData[pos] = rsTmp.getString("MACHINE_NO");
                    pos++;
                    rowData[pos] = rsTmp.getString("POSITION_DESC");
                    pos++;
                    rowData[pos] = rsTmp.getString("PARTY_CODE");
                    pos++;
                    rowData[pos] = rsTmp.getString("PARTY_NAME");
                    pos++;
                    rowData[pos] = rsTmp.getString("CURRENT_PROJECTION");
                    pos++;
                    rowData[pos] = rsTmp.getString("PREV_GR_QTY");
                    pos++;
                    rowData[pos] = rsTmp.getString("CP_PGR");
                    pos++;
                    rowData[pos] = rsTmp.getString("WIPQTY");
                    pos++;
                    rowData[pos] = rsTmp.getString("STOCKQTY");
                    pos++;
                    if (cmbFY.getSelectedIndex() == 1) {
                        rowData[pos] = rsTmp.getString("PROJ_OPN_STK");
                        pos++;
                    }
                    rowData[pos] = rsTmp.getString("DISPATCHQTY");
                    pos++;
                    rowData[pos] = rsTmp.getString("PRIORITY");
                    pos++;
                    rowData[pos] = rsTmp.getString("FOLLOWUP");
                    pos++;
                    rowData[pos] = rsTmp.getString("FOLLOWUPPP");
                    pos++;
                    rowData[pos] = rsTmp.getString("FOLLOWUPSALE");
                    pos++;
                    rowData[pos] = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
                    pos++;
                    gobqty = gobqty + rsTmp.getDouble("CURRENT_PROJECTION");
                    gobkg = gobkg + rsTmp.getDouble("PREV_GR_QTY");
                    gobval = gobval + rsTmp.getDouble("CP_PGR");
                    gppqty = gppqty + rsTmp.getDouble("WIPQTY");
                    gppkg = gppkg + rsTmp.getDouble("STOCKQTY");
                    gppval = gppval + rsTmp.getDouble("DISPATCHQTY");
                    gppopstk = gppopstk + rsTmp.getDouble("PROJ_OPN_STK");
                    gcpqty = gcpqty + rsTmp.getDouble("PRIORITY");
                    gfupp = gfupp + rsTmp.getDouble("FOLLOWUPPP");
                    gfus = gfus + rsTmp.getDouble("FOLLOWUPSALE");

                    pobqty = pobqty + rsTmp.getDouble("CURRENT_PROJECTION");
                    pobkg = pobkg + rsTmp.getDouble("PREV_GR_QTY");
                    pobval = pobval + rsTmp.getDouble("CP_PGR");
                    pppqty = pppqty + rsTmp.getDouble("WIPQTY");
                    pppkg = pppkg + rsTmp.getDouble("STOCKQTY");
                    pppval = pppval + rsTmp.getDouble("DISPATCHQTY");
                    pppopstk = pppopstk + rsTmp.getDouble("PROJ_OPN_STK");
                    pcpqty = pcpqty + rsTmp.getDouble("PRIORITY");
                    pfupp = pfupp + rsTmp.getDouble("FOLLOWUPPP");
                    pfus = pfus + rsTmp.getDouble("FOLLOWUPSALE");

                    zobqty = zobqty + rsTmp.getDouble("CURRENT_PROJECTION");
                    zobkg = zobkg + rsTmp.getDouble("PREV_GR_QTY");
                    zobval = zobval + rsTmp.getDouble("CP_PGR");
                    zppqty = zppqty + rsTmp.getDouble("WIPQTY");
                    zppkg = zppkg + rsTmp.getDouble("STOCKQTY");
                    zppval = zppval + rsTmp.getDouble("DISPATCHQTY");
                    zppopstk = zppopstk + rsTmp.getDouble("PROJ_OPN_STK");
                    zcpqty = zcpqty + rsTmp.getDouble("PRIORITY");
                    zfupp = zfupp + rsTmp.getDouble("FOLLOWUPPP");
                    zfus = zfus + rsTmp.getDouble("FOLLOWUPSALE");

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                Object[] rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[5] = "Party Total";
                rowData[7] = String.format("%.2f", pobqty);
                rowData[8] = String.format("%.2f", pobkg);
                rowData[9] = String.format("%.2f", pobval);
                rowData[10] = String.format("%.2f", pppqty);
                rowData[11] = String.format("%.2f", pppkg);
                if (cmbFY.getSelectedIndex() == 0) {
                    rowData[12] = String.format("%.2f", pppval);
                    rowData[13] = String.format("%.2f", pcpqty);
                    rowData[15] = String.format("%.2f", pfupp);
                    rowData[16] = String.format("%.2f", pfus);
                }
                if (cmbFY.getSelectedIndex() == 1) {
                    rowData[12] = String.format("%.2f", pppopstk);
                    rowData[13] = String.format("%.2f", pppval);
                    rowData[14] = String.format("%.2f", pcpqty);
                    rowData[16] = String.format("%.2f", pfupp);
                    rowData[17] = String.format("%.2f", pfus);
                }

                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = 0;

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Zone Total";
                rowData[7] = String.format("%.2f", zobqty);
                rowData[8] = String.format("%.2f", zobkg);
                rowData[9] = String.format("%.2f", zobval);
                rowData[10] = String.format("%.2f", zppqty);
                rowData[11] = String.format("%.2f", zppkg);
                if (cmbFY.getSelectedIndex() == 0) {
                    rowData[12] = String.format("%.2f", zppval);
                    rowData[13] = String.format("%.2f", zcpqty);
                    rowData[15] = String.format("%.2f", zfupp);
                    rowData[16] = String.format("%.2f", zfus);
                }
                if (cmbFY.getSelectedIndex() == 1) {
                    rowData[12] = String.format("%.2f", zppopstk);
                    rowData[13] = String.format("%.2f", zppval);
                    rowData[14] = String.format("%.2f", zcpqty);
                    rowData[16] = String.format("%.2f", zfupp);
                    rowData[17] = String.format("%.2f", zfus);
                }

                DataModel.addRow(rowData);
                rowData = new Object[100];
                DataModel.addRow(rowData);
                zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = 0;

                rowData = new Object[100];
                DataModel.addRow(rowData);
                rowData = new Object[100];
                rowData[1] = "Grand Total";
                rowData[7] = String.format("%.2f", gobqty);
                rowData[8] = String.format("%.2f", gobkg);
                rowData[9] = String.format("%.2f", gobval);
                rowData[10] = String.format("%.2f", gppqty);
                rowData[11] = String.format("%.2f", gppkg);
                if (cmbFY.getSelectedIndex() == 0) {
                    rowData[12] = String.format("%.2f", gppval);
                    rowData[13] = String.format("%.2f", gcpqty);
                    rowData[15] = String.format("%.2f", gfupp);
                    rowData[16] = String.format("%.2f", gfus);
                }
                if (cmbFY.getSelectedIndex() == 1) {
                    rowData[12] = String.format("%.2f", gppopstk);
                    rowData[13] = String.format("%.2f", gppval);
                    rowData[14] = String.format("%.2f", gcpqty);
                    rowData[16] = String.format("%.2f", gfupp);
                    rowData[17] = String.format("%.2f", gfus);
                }
                DataModel.addRow(rowData);

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
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

    private void Startup() {
        String s = "";
        String mn = data.getStringValueFromDB("SELECT UPPER(LEFT(MONTHNAME(CURDATE()),3)) FROM DUAL");
        String nmn = data.getStringValueFromDB("SELECT UPPER(LEFT(MONTHNAME(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),3)) FROM DUAL");

        String lstdt = data.getStringValueFromDB("SELECT SUBDATE(CURDATE(),INTERVAL DAY(CURDATE()) DAY) FROM DUAL");
        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL, "
                + "(SELECT D.MM_UPN_NO,D.MM_PARTY_CODE AS PARTY_CODE,CONCAT(PARTY_NAME,', ',CITY_ID) AS PARTY_NAME,D.MM_MACHINE_NO AS MACHINE_NO,MM_MACHINE_POSITION,POSITION_DESC,MM_FELT_STYLE,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,(MM_FELT_LENGTH*MM_FELT_WIDTH*MM_FELT_GSM/1000) AS MM_TH_WEIGHT,MM_FELT_LENGTH*MM_FELT_WIDTH AS AREA_SQMTR,MM_FABRIC_LENGTH,MM_FABRIC_WIDTH,MM_SIZE_M2,(MM_FABRIC_LENGTH*MM_FABRIC_WIDTH*MM_FELT_GSM/1000) AS MM_FABRIC_TH_WEIGHT,MM_ITEM_CODE,GROUP_NAME,CASE WHEN WT_RATE =0 THEN SQM_CHRG ELSE WT_RATE END  AS FELT_RATE ,INCHARGE_CD FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,  PRODUCTION.FELT_MACHINE_MASTER_DETAIL D, PRODUCTION.FELT_QLT_RATE_MASTER R, DINESHMILLS.D_SAL_PARTY_MASTER P , PRODUCTION.FELT_MACHINE_POSITION_MST MP WHERE H.MM_DOC_NO=D.MM_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0  AND MM_ITEM_CODE = PRODUCT_CODE  AND EFFECTIVE_TO ='0000-00-00'  AND P.PARTY_CODE = H.MM_PARTY_CODE AND POSITION_NO+0 = MM_MACHINE_POSITION+0 AND COALESCE(POSITION_CLOSE_IND,0) !=1 AND COALESCE(PARTY_CLOSE_IND,0) !=1 "
                + ") AS M "
                + "SET STYLE = MM_FELT_STYLE, "
                + "PRESS_LENGTH = MM_FELT_LENGTH, "
                + "PRESS_WIDTH = MM_FELT_WIDTH, "
                + "PRESS_WEIGHT = MM_TH_WEIGHT, "
                + "PRESS_GSM = MM_FELT_GSM, "
                + "PRESS_SQMTR = AREA_SQMTR, "
                + "DRY_LENGTH =MM_FABRIC_LENGTH, "
                + "DRY_WIDTH =MM_FABRIC_WIDTH, "
                + "DRY_WEIGHT = MM_FABRIC_TH_WEIGHT, "
                + "DRY_SQMTR = MM_SIZE_M2 "
                + "WHERE COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 AND UPN = MM_UPN_NO ";
        data.Execute(s);
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION=" + mn + "_BUDGET,CURRENT_PROJECTION_VALUE=" + mn + "_NET_AMOUNT "
                + "WHERE LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+MONTH(curdate()),2) ) "
                + "AND CURRENT_PROJECTION!=" + mn + "_BUDGET ");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION=" + nmn + "_BUDGET,CURRENT_PROJECTION_VALUE=" + nmn + "_NET_AMOUNT "
                + "WHERE LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+MONTH(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),2)) "
                + "AND CURRENT_PROJECTION!=" + nmn + "_BUDGET ");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION=" + mn + "_BUDGET,CURRENT_PROJECTION_VALUE=" + mn + "_NET_AMOUNT "
                + "WHERE LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+MONTH(curdate()),2) ) "
                + "AND CURRENT_PROJECTION!=" + mn + "_BUDGET ");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION=" + nmn + "_BUDGET,CURRENT_PROJECTION_VALUE=" + nmn + "_NET_AMOUNT "
                + "WHERE LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+MONTH(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),2)) "
                + "AND CURRENT_PROJECTION!=" + nmn + "_BUDGET ");

        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D, "
                + "( "
                + "SELECT H.MM_PARTY_CODE,H.MM_MACHINE_NO,MM_MACHINE_POSITION,COALESCE(MM_AVG_LIFE,0) AS M,ceiling((340/COALESCE(MM_AVG_LIFE,0))) AS V,MM_FELT_VALUE_WITH_GST AS G FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND H.APPROVED =1 AND H.CANCELED =0) AS C "
                + "SET AVG_LIFE  = M, SELLING_PRICE = ROUND(G,0)  WHERE YEAR_FROM =2020 "
                + "AND MM_PARTY_CODE = PARTY_CODE AND MACHINE_NO+0 = MM_MACHINE_NO+0 AND MM_MACHINE_POSITION+0 = POSITION_NO+0";
        data.Execute(s);
        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D, "
                + "( "
                + "SELECT H.MM_PARTY_CODE,H.MM_MACHINE_NO,MM_MACHINE_POSITION,COALESCE(MM_AVG_LIFE,0) AS M,ceiling((340/COALESCE(MM_AVG_LIFE,0))) AS V,MM_FELT_VALUE_WITH_GST AS G FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND H.APPROVED =1 AND H.CANCELED =0) AS C "
                + "SET AVG_LIFE  = M, SELLING_PRICE = ROUND(G,0)  WHERE YEAR_FROM =2021 "
                + "AND MM_PARTY_CODE = PARTY_CODE AND MACHINE_NO+0 = MM_MACHINE_NO+0 AND MM_MACHINE_POSITION+0 = POSITION_NO+0";
        data.Execute(s);
        
        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D, "
                + "( "
                + "SELECT H.MM_PARTY_CODE,H.MM_MACHINE_NO,MM_MACHINE_POSITION,COALESCE(MM_AVG_LIFE,0) AS M,ceiling((340/COALESCE(MM_AVG_LIFE,0))) AS V,MM_FELT_VALUE_WITH_GST AS G FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND H.APPROVED =1 AND H.CANCELED =0) AS C "
                + "SET AVG_LIFE  = M, SELLING_PRICE = ROUND(G,0)  WHERE YEAR_FROM =2022 "
                + "AND MM_PARTY_CODE = PARTY_CODE AND MACHINE_NO+0 = MM_MACHINE_NO+0 AND MM_MACHINE_POSITION+0 = POSITION_NO+0";
        data.Execute(s);

        cmbmonth.setSelectedIndex(data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL") - 1);
        SetVariable();
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D, PRODUCTION.FELT_MACHINE_MASTER_DETAIL M "
                + "SET SELLING_PRICE=MM_FELT_VALUE_WITH_GST WHERE D.UPN=M.MM_UPN_NO ");
        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,"
                + "(SELECT H.MM_PARTY_CODE,H.MM_MACHINE_NO,MM_MACHINE_POSITION,COALESCE(MM_AVG_LIFE,0) AS M,ceiling((340/COALESCE(MM_AVG_LIFE,0))) AS V,MM_FELT_VALUE_WITH_GST AS G FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND H.APPROVED =1 AND H.CANCELED =0) AS C "
                + "SET AVG_LIFE  = M, SELLING_PRICE = G  WHERE    YEAR_FROM =2020  "
                + "AND MM_PARTY_CODE = PARTY_CODE AND MACHINE_NO+0 = MM_MACHINE_NO+0 AND MM_MACHINE_POSITION+0 = POSITION_NO+0";
        data.Execute(s);
        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,"
                + "(SELECT H.MM_PARTY_CODE,H.MM_MACHINE_NO,MM_MACHINE_POSITION,COALESCE(MM_AVG_LIFE,0) AS M,ceiling((340/COALESCE(MM_AVG_LIFE,0))) AS V,MM_FELT_VALUE_WITH_GST AS G FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND H.APPROVED =1 AND H.CANCELED =0) AS C "
                + "SET AVG_LIFE  = M, SELLING_PRICE = G  WHERE    YEAR_FROM =2021  "
                + "AND MM_PARTY_CODE = PARTY_CODE AND MACHINE_NO+0 = MM_MACHINE_NO+0 AND MM_MACHINE_POSITION+0 = POSITION_NO+0";
        data.Execute(s);
        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,"
                + "(SELECT H.MM_PARTY_CODE,H.MM_MACHINE_NO,MM_MACHINE_POSITION,COALESCE(MM_AVG_LIFE,0) AS M,ceiling((340/COALESCE(MM_AVG_LIFE,0))) AS V,MM_FELT_VALUE_WITH_GST AS G FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
                + "WHERE H.MM_DOC_NO = D.MM_DOC_NO AND H.APPROVED =1 AND H.CANCELED =0) AS C "
                + "SET AVG_LIFE  = M, SELLING_PRICE = G  WHERE    YEAR_FROM =2022  "
                + "AND MM_PARTY_CODE = PARTY_CODE AND MACHINE_NO+0 = MM_MACHINE_NO+0 AND MM_MACHINE_POSITION+0 = POSITION_NO+0";
        data.Execute(s);

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL B,(SELECT G.PARTY_CODE, DISC_PER,PRODUCT_CODE "
                + "FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_DETAIL  G "
                + "WHERE SUBSTRING(PRODUCT_CODE,1,3) IN ('729','719') AND D.GROUP_CODE = G.GROUP_CODE "
                + "AND  EFFECTIVE_FROM >='2020-04-01' AND EFFECTIVE_TO <='2021-03-31' AND APPROVED =1 AND D.PARTY_CODE ='') AS G "
                + "SET SPL_DISCOUNT = DISC_PER "
                + "WHERE B.YEAR_FROM=2020 AND G.PARTY_CODE = B.PARTY_CODE AND QUALITY_NO = PRODUCT_CODE");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL B,(SELECT G.PARTY_CODE, DISC_PER,PRODUCT_CODE "
                + "FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_DETAIL  G "
                + "WHERE SUBSTRING(PRODUCT_CODE,1,3) IN ('729','719') AND D.GROUP_CODE = G.GROUP_CODE "
                + "AND  EFFECTIVE_FROM >='2020-04-01' AND EFFECTIVE_TO <='2022-03-31' AND APPROVED =1 AND D.PARTY_CODE ='') AS G "
                + "SET SPL_DISCOUNT = DISC_PER "
                + "WHERE B.YEAR_FROM=2021 AND G.PARTY_CODE = B.PARTY_CODE AND QUALITY_NO = PRODUCT_CODE");

        data.Execute("UPDATE  PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL R, PRODUCTION.FELT_BUDGET_REVIEW_DETAIL B "
                + "SET SPL_DISCOUNT = DISC_PER "
                + "WHERE B.YEAR_FROM=2020 AND SUBSTRING(PRODUCT_CODE,1,3) IN ('729','719') "
                + "AND  EFFECTIVE_FROM >='2020-04-01' AND EFFECTIVE_TO <='2021-03-31' AND R.APPROVED =1 AND B.APPROVED =1 "
                + "AND  B.GROUP_NAME IN ('SDF','HDS') AND PRODUCT_CODE = QUALITY_NO "
                + "AND R.PARTY_CODE != '' AND R.PARTY_CODE = B.PARTY_CODE");
        data.Execute("UPDATE  PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL R, PRODUCTION.FELT_BUDGET_REVIEW_DETAIL B "
                + "SET SPL_DISCOUNT = DISC_PER "
                + "WHERE B.YEAR_FROM=2021 AND SUBSTRING(PRODUCT_CODE,1,3) IN ('729','719') "
                + "AND  EFFECTIVE_FROM >='2020-04-01' AND EFFECTIVE_TO <='2022-03-31' AND R.APPROVED =1 AND B.APPROVED =1 "
                + "AND  B.GROUP_NAME IN ('SDF','HDS') AND PRODUCT_CODE = QUALITY_NO "
                + "AND R.PARTY_CODE != '' AND R.PARTY_CODE = B.PARTY_CODE");

        data.Execute("UPDATE  PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL R, PRODUCTION.FELT_BUDGET_REVIEW_DETAIL B "
                + "SET SPL_DISCOUNT = DISC_PER "
                + "WHERE B.YEAR_FROM=2021 AND SUBSTRING(PRODUCT_CODE,1,3) IN ('729','719') "
                + "AND  EFFECTIVE_FROM >='2021-04-01' AND EFFECTIVE_TO <='2022-03-31' AND R.APPROVED =1  "
                + "AND  B.GROUP_NAME IN ('SDF','HDS') AND PRODUCT_CODE = QUALITY_NO "
                + "AND R.PARTY_CODE != '' AND R.PARTY_CODE = B.PARTY_CODE");
        data.Execute("UPDATE  PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL R, PRODUCTION.FELT_BUDGET_REVIEW_DETAIL B "
                + "SET SPL_DISCOUNT = DISC_PER "
                + "WHERE B.YEAR_FROM=2022 AND SUBSTRING(PRODUCT_CODE,1,3) IN ('729','719') "
                + "AND  EFFECTIVE_FROM >='2021-04-01' AND EFFECTIVE_TO <='2022-03-31' AND R.APPROVED =1  "
                + "AND  B.GROUP_NAME IN ('SDF','HDS') AND PRODUCT_CODE = QUALITY_NO " 
                + "AND R.PARTY_CODE != '' AND R.PARTY_CODE = B.PARTY_CODE"); 
        

        //Pending Value is Discount Value
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET PENDING_VALUE = CASE WHEN GROUP_NAME ='SDF' AND SPL_DISCOUNT =0 THEN (SELLING_PRICE *0.50) "
                + " WHEN GROUP_NAME ='HDS' AND SPL_DISCOUNT =0 THEN (SELLING_PRICE *0.21) ELSE 0 END "
                + "WHERE  GROUP_NAME IN ('SDF','HDS') ");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET PENDING_VALUE = 0 "
                + "WHERE  GROUP_NAME IN ('SDF','HDS') "
                + "AND INCHARGE=6");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL SET SELLING_PRICE_MACHINE_MASTER=SELLING_PRICE");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL SET SELLING_PRICE=SELLING_PRICE-COALESCE(PENDING_VALUE,0)");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,PRODUCTION.FELT_EXPORT_RATE_MASTER M "
                + "SET SELLING_PRICE=RATE WHERE D.PARTY_CODE=M.PARTY_CODE AND D.MACHINE_NO=M.MACHINE_NO AND D.POSITION_NO=M.POSITION_NO");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION=" + mn + "_BUDGET,CURRENT_PROJECTION_VALUE=" + mn + "_NET_AMOUNT "
                + "WHERE LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+MONTH(curdate()),2)) "
                + "AND CURRENT_PROJECTION!=" + mn + "_BUDGET ");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION=" + nmn + "_BUDGET,CURRENT_PROJECTION_VALUE=" + nmn + "_NET_AMOUNT "
                + "WHERE LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+MONTH(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),2)) "
                + "AND CURRENT_PROJECTION!=" + nmn + "_BUDGET ");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION_VALUE=0,"
                + "APR_NET_AMOUNT=0,"
                + "MAY_NET_AMOUNT=0,"
                + "JUN_NET_AMOUNT=0,"
                + "JUL_NET_AMOUNT=0,"
                + "AUG_NET_AMOUNT=0,"
                + "SEP_NET_AMOUNT=0,"
                + "OCT_NET_AMOUNT=0,"
                + "NOV_NET_AMOUNT=0,"
                + "DEC_NET_AMOUNT=0,"
                + "JAN_NET_AMOUNT=0,"
                + "FEB_NET_AMOUNT=0,"
                + "MAR_NET_AMOUNT=0 WHERE YEAR_FROM=2020 ");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION_VALUE=0,"
                + "APR_NET_AMOUNT=0,"
                + "MAY_NET_AMOUNT=0,"
                + "JUN_NET_AMOUNT=0,"
                + "JUL_NET_AMOUNT=0,"
                + "AUG_NET_AMOUNT=0,"
                + "SEP_NET_AMOUNT=0,"
                + "OCT_NET_AMOUNT=0,"
                + "NOV_NET_AMOUNT=0,"
                + "DEC_NET_AMOUNT=0,"
                + "JAN_NET_AMOUNT=0,"
                + "FEB_NET_AMOUNT=0,"
                + "MAR_NET_AMOUNT=0 WHERE YEAR_FROM=2021 ");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION_VALUE=0,"
                + "APR_NET_AMOUNT=0,"
                + "MAY_NET_AMOUNT=0,"
                + "JUN_NET_AMOUNT=0,"
                + "JUL_NET_AMOUNT=0,"
                + "AUG_NET_AMOUNT=0,"
                + "SEP_NET_AMOUNT=0,"
                + "OCT_NET_AMOUNT=0,"
                + "NOV_NET_AMOUNT=0,"
                + "DEC_NET_AMOUNT=0,"
                + "JAN_NET_AMOUNT=0,"
                + "FEB_NET_AMOUNT=0,"
                + "MAR_NET_AMOUNT=0 WHERE YEAR_FROM=2022 ");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET CURRENT_PROJECTION_VALUE=SELLING_PRICE*COALESCE(CURRENT_PROJECTION,0),"
                + "APR_NET_AMOUNT=SELLING_PRICE*COALESCE(APR_BUDGET,0),"
                + "MAY_NET_AMOUNT=SELLING_PRICE*COALESCE(MAY_BUDGET,0),"
                + "JUN_NET_AMOUNT=SELLING_PRICE*COALESCE(JUN_BUDGET,0),"
                + "JUL_NET_AMOUNT=SELLING_PRICE*COALESCE(JUL_BUDGET,0),"
                + "AUG_NET_AMOUNT=SELLING_PRICE*COALESCE(AUG_BUDGET,0),"
                + "SEP_NET_AMOUNT=SELLING_PRICE*COALESCE(SEP_BUDGET,0),"
                + "OCT_NET_AMOUNT=SELLING_PRICE*COALESCE(OCT_BUDGET,0),"
                + "NOV_NET_AMOUNT=SELLING_PRICE*COALESCE(NOV_BUDGET,0),"
                + "DEC_NET_AMOUNT=SELLING_PRICE*COALESCE(DEC_BUDGET,0),"
                + "JAN_NET_AMOUNT=SELLING_PRICE*COALESCE(JAN_BUDGET,0),"
                + "FEB_NET_AMOUNT=SELLING_PRICE*COALESCE(FEB_BUDGET,0),"
                + "MAR_NET_AMOUNT=SELLING_PRICE*COALESCE(MAR_BUDGET,0) ");

//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + "SET CURRENT_PROJECTION_VALUE=SELLING_PRICE*COALESCE(CURRENT_PROJECTION,0),"
//                + "APR_NET_AMOUNT=SELLING_PRICE*COALESCE(APR_BUDGET,0),"
//                + "MAY_NET_AMOUNT=SELLING_PRICE*COALESCE(MAY_BUDGET,0),"
//                + "JUN_NET_AMOUNT=SELLING_PRICE*COALESCE(JUN_BUDGET,0),"
//                + "JUL_NET_AMOUNT=SELLING_PRICE*COALESCE(JUL_BUDGET,0),"
//                + "AUG_NET_AMOUNT=SELLING_PRICE*COALESCE(AUG_BUDGET,0),"
//                + "SEP_NET_AMOUNT=SELLING_PRICE*COALESCE(SEP_BUDGET,0),"
//                + "OCT_NET_AMOUNT=SELLING_PRICE*COALESCE(OCT_BUDGET,0),"
//                + "NOV_NET_AMOUNT=SELLING_PRICE*COALESCE(NOV_BUDGET,0),"
//                + "DEC_NET_AMOUNT=SELLING_PRICE*COALESCE(DEC_BUDGET,0),"
//                + "JAN_NET_AMOUNT=SELLING_PRICE*COALESCE(JAN_BUDGET,0),"
//                + "FEB_NET_AMOUNT=SELLING_PRICE*COALESCE(FEB_BUDGET,0),"
//                + "MAR_NET_AMOUNT=SELLING_PRICE*COALESCE(MAR_BUDGET,0) "
//                + " WHERE  LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),2))");
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + " SET DISPATCH_QTY=0,STOCK_QTY=0,"
//                //+ "WIP_QTY=0,"
//                + "CANCEL_QTY=0,OBSOLETE_QTY=0,OBSOLETE_WIP_QTY=0,OBSOLETE_STOCK_QTY=0,OC_STOCK=0 "
//                + " WHERE  LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PARTY_CODE,MACHINE_NO,POSITION_NO,SUM(NO_OF_PIECES) AS INVQTY,SUM(INVOICE_AMT) AS INVAMT "
//                + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H "
//                + "WHERE INVOICE_DATE>='2020-04-01' "
//                + "AND PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ) "
//                + "GROUP BY PARTY_CODE,MACHINE_NO,POSITION_NO) AS I "
//                + "SET D.DISPATCH_QTY=I.INVQTY,DISPATCH_VALUE=INVAMT "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PARTY_CODE,MACHINE_NO,POSITION_NO,SUM(NO_OF_PIECES) AS INVQTY,SUM(INVOICE_AMT) AS INVAMT "
//                + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H "
//                + "WHERE APPROVED=1 AND COALESCE(CANCELLED,0)=0 AND INVOICE_DATE>='2020-04-01' AND INVOICE_DATE<='" + lstdt + "' "
//                + "AND PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ) "
//                + "GROUP BY PARTY_CODE,MACHINE_NO,POSITION_NO) AS I "
//                + "SET D.DISPATCH_QTY=I.INVQTY,DISPATCH_VALUE=INVAMT "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PR_UPN,COUNT(*) AS STKQTY,SUM(PR_FELT_VALUE_WITH_GST) AS STKVAL FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                + " WHERE PR_PIECE_STAGE IN ('IN STOCK','BSR') "
//                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE' "
//                + "AND  "
//                + "((PR_REQ_MTH_LAST_DDMMYY>='2020-04-01' AND PR_REQ_MTH_LAST_DDMMYY<='2021-03-31') "
//                + "OR  "
//                + "(PR_CURRENT_SCH_LAST_DDMMYY>='2020-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2021-03-31')) "
//                + "GROUP BY PR_UPN) AS I  "
//                + "SET D.STOCK_QTY=I.STKQTY,D.STOCK_VALUE=STKVAL "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.UPN=I.PR_UPN";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PR_UPN,COUNT(*) AS WIPQTY,SUM(PR_FELT_VALUE_WITH_GST) AS WIPVAL FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                + " WHERE PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','SDML','DIVERTED_FINISHING_STOCK') "
//                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE' "
//                + "AND "
//                + "((PR_REQ_MTH_LAST_DDMMYY>='2020-04-01' AND PR_REQ_MTH_LAST_DIADMMYY<='2021-03-31') "
//                + "OR "
//                + "(PR_CURRENT_SCH_LAST_DDMMYY>='2020-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2021-03-31')) "
//                + "GROUP BY PR_UPN) AS I "
//                + "SET D.WIP_QTY=I.WIPQTY,D.WIP_VALUE=WIPVAL "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.UPN=I.PR_UPN";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PR_UPN,COUNT(*) AS WIPQTY,SUM(PR_FELT_VALUE_WITH_GST) AS WIPVAL FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PIECE_STAGE IN ('CANCELED') "
//                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE' "
//                + "AND "
//                + "((PR_REQ_MTH_LAST_DDMMYY>='2020-04-01' AND PR_REQ_MTH_LAST_DDMMYY<='2021-03-31') "
//                + "OR "
//                + "(PR_CURRENT_SCH_LAST_DDMMYY>='2020-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2021-03-31')) "
//                + "GROUP BY PR_UPN) AS I  "
//                + "SET D.CANCEL_QTY=I.WIPQTY,D.CANCEL_VALUE=WIPVAL "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.UPN=I.PR_UPN";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT MM_UPN_NO as PR_UPN1,COUNT(*) AS STKQTY,SUM(PR_FELT_VALUE_WITH_GST) AS STKVAL FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                + "left join (SELECT D.MM_UPN_NO,D.MM_PARTY_CODE,D.MM_MACHINE_NO,D.MM_MACHINE_POSITION FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
//                + "WHERE D.MM_DOC_NO=H.MM_DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ) AS MACHINE "
//                + "ON mm_party_code=pr_party_code and right(100+mm_machine_no,2)=right(100+pr_machine_no,2) and right(100+mm_machine_position,2)=right(100+pr_position_no,2) WHERE  PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR','SDML','DIVERTED_FINISHING_STOCK') AND COALESCE(PR_DELINK,'')='OBSOLETE' "
//                + "AND (PR_OBSOLETE_DATE>='2020-04-01' AND PR_OBSOLETE_DATE<='2021-03-31') AND  "
//                + "((PR_REQ_MTH_LAST_DDMMYY>='2020-04-01' AND PR_REQ_MTH_LAST_DDMMYY<='2021-03-31') "
//                + "OR "
//                + "(PR_CURRENT_SCH_LAST_DDMMYY>='2020-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2021-03-31')) "
//                + "GROUP BY PR_UPN1) AS I "
//                + "SET D.OBSOLETE_QTY=I.STKQTY,D.OBSOLETE_VALUE=STKVAL "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.UPN=I.PR_UPN1";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT MM_UPN_NO as PR_UPN1,COUNT(*) AS STKQTY,SUM(PR_FELT_VALUE_WITH_GST) AS STKVAL FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                + "left join (SELECT D.MM_UPN_NO,D.MM_PARTY_CODE,D.MM_MACHINE_NO,D.MM_MACHINE_POSITION FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
//                + "WHERE D.MM_DOC_NO=H.MM_DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ) AS MACHINE "
//                + "ON mm_party_code=pr_party_code and right(100+mm_machine_no,2)=right(100+pr_machine_no,2) and right(100+mm_machine_position,2)=right(100+pr_position_no,2) WHERE  PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','SDML','DIVERTED_FINISHING_STOCK') AND COALESCE(PR_DELINK,'')='OBSOLETE' "
//                + "AND (PR_OBSOLETE_DATE>='2020-04-01' AND PR_OBSOLETE_DATE<='2021-03-31') AND   "
//                + "((PR_REQ_MTH_LAST_DDMMYY>='2020-04-01' AND PR_REQ_MTH_LAST_DDMMYY<='2021-03-31') "
//                + "OR "
//                + "(PR_CURRENT_SCH_LAST_DDMMYY>='2020-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2021-03-31')) "
//                + "GROUP BY PR_UPN1) AS I "
//                + "SET D.OBSOLETE_WIP_QTY=I.STKQTY,D.OBSOLETE_WIP_VALUE=STKVAL "
//                + "WHERE D.UPN=I.PR_UPN1";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT MM_UPN_NO as PR_UPN1,COUNT(*) AS STKQTY,SUM(PR_FELT_VALUE_WITH_GST) AS STKVAL FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                + "left join (SELECT D.MM_UPN_NO,D.MM_PARTY_CODE,D.MM_MACHINE_NO,D.MM_MACHINE_POSITION FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H "
//                + "WHERE D.MM_DOC_NO=H.MM_DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ) AS MACHINE "
//                + "ON mm_party_code=pr_party_code and right(100+mm_machine_no,2)=right(100+pr_machine_no,2) and right(100+mm_machine_position,2)=right(100+pr_position_no,2) WHERE PR_PIECE_STAGE IN ('IN STOCK','BSR') AND COALESCE(PR_DELINK,'')='OBSOLETE' "
//                + "AND (PR_OBSOLETE_DATE>='2020-04-01' AND PR_OBSOLETE_DATE<='2021-03-31') AND  "
//                + "((PR_REQ_MTH_LAST_DDMMYY>='2020-04-01' AND PR_REQ_MTH_LAST_DDMMYY<='2021-03-31') "
//                + "OR "
//                + "(PR_CURRENT_SCH_LAST_DDMMYY>='2020-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2021-03-31')) "
//                + "GROUP BY PR_UPN1) AS I  "
//                + "SET D.OBSOLETE_STOCK_QTY=I.STKQTY,D.OBSOLETE_STOCK_VALUE=STKVAL "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.UPN=I.PR_UPN1";
//        data.Execute(s);
//        s = "UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PR_UPN,COUNT(*) AS WIPQTY,SUM(PR_FELT_VALUE_WITH_GST) AS WIPVAL FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PIECE_STAGE IN ('CANCELED') "
//                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE' "
//                + "AND "
//                + "((PR_REQ_MTH_LAST_DDMMYY>='2020-04-01' AND PR_REQ_MTH_LAST_DDMMYY<='2021-03-31') "
//                + "OR "
//                + "(PR_CURRENT_SCH_LAST_DDMMYY>='2020-04-01' AND PR_CURRENT_SCH_LAST_DDMMYY<='2021-03-31')) "
//                + "GROUP BY PR_UPN) AS I "
//                + "SET D.CANCEL_QTY=I.WIPQTY,D.CANCEL_VALUE=WIPVAL "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.UPN=I.PR_UPN";
//        data.Execute(s);
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + "SET " + mn + "_BUDGET=DISPATCH_QTY," + mn + "_REMARK='CLOSED',"
//                + "CURRENT_PROJECTION=DISPATCH_QTY,CURRENT_PROJECTION_VALUE=DISPATCH_VALUE,"
//                + mn + "_NET_AMOUNT=DISPATCH_VALUE "
//                + "WHERE  party_status='CLOSED' and  "
//                + "LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,"
//                + "(SELECT *,CASE WHEN CP>=ACESS_QTY THEN ACESS_QTY ELSE CP END AS CANCELPIECE,"
//                + "CASE WHEN CP<ACESS_QTY THEN ACESS_QTY-CP ELSE 0 END AS NONCANCELPIECE FROM "
//                + "(SELECT UPN,PARTY_NAME,CURRENT_PROJECTION,STOCK_QTY,WIP_QTY,DISPATCH_QTY,ACESS_QTY,"
//                + "COUNT(CASE WHEN PR_PIECE_STAGE IN ('PLANNING','BOOKING') THEN PR_PIECE_NO END) AS CP,"
//                + "COUNT(CASE WHEN PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR') THEN PR_PIECE_NO END) AS NCP,"
//                + "ACESS_QTY - COUNT(PR_PIECE_NO) ,COALESCE(GROUP_CONCAT(CASE WHEN PR_PIECE_STAGE IN ('PLANNING','BOOKING') THEN CONCAT(PR_PIECE_NO,'(',PR_PIECE_STAGE,')') END),'') AS CAN_PIECE,"
//                + "COALESCE(GROUP_CONCAT(CASE WHEN PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR') THEN CONCAT(PR_PIECE_NO,'(',PR_PIECE_STAGE,')') END),'') AS NON_CAN_PIECE,"
//                + "GROUP_CONCAT(CONCAT(PR_PIECE_NO,'(',PR_PIECE_STAGE,')')) AS CANPIECE, COUNT(PR_PIECE_NO) FROM "
//                + "(SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL P WHERE COALESCE(APPROVED,0) =1 AND COALESCE(CANCELED,0) =0 AND LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(curdate()),2)) AND ACESS_QTY > 0 ) AS A "
//                + "LEFT JOIN (SELECT PR_UPN,PR_PIECE_NO,PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_UPN IN (SELECT UPN FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL P WHERE COALESCE(APPROVED,0) =1 AND COALESCE(CANCELED,0) =0 AND LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(curdate()),2)) AND ACESS_QTY > 0) AND PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR') ) AS B ON PR_UPN= UPN GROUP BY UPN ORDER BY UPN) AS D WHERE CANPIECE IS NOT NULL) AS C "
//                + "SET CANCELABLE_PIECE_NO=CANCELPIECE,CANCELABLE_PIECES=CAN_PIECE,"
//                + "NONCANCELABLE_PIECE_NO=NONCANCELPIECE,NONCANCELABLE_PIECES=NON_CAN_PIECE "
//                + "WHERE D.UPN=C.UPN AND LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,"
//                + "(SELECT *,CASE WHEN CP>=ACESSD_QTY THEN ACESSD_QTY ELSE CP END AS CANCELPIECE,"
//                + "CASE WHEN CP<ACESSD_QTY THEN ACESSD_QTY-CP ELSE 0 END AS NONCANCELPIECE FROM "
//                + "(SELECT UPN,PARTY_NAME,(CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0)) AS CURRENT_PROJECTION,STOCK_QTY,WIP_QTY,DISPATCH_QTY,ACESS_QTY,ACESSD_QTY,"
//                + "COUNT(CASE WHEN PR_PIECE_STAGE IN ('PLANNING','BOOKING') THEN PR_PIECE_NO END) AS CP,"
//                + "COUNT(CASE WHEN PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR') THEN PR_PIECE_NO END) AS NCP,"
//                + "ACESSD_QTY - COUNT(PR_PIECE_NO) ,COALESCE(GROUP_CONCAT(CASE WHEN PR_PIECE_STAGE IN ('PLANNING','BOOKING') THEN CONCAT(PR_PIECE_NO,'(',PR_PIECE_STAGE,')') END),'') AS CAN_PIECE,"
//                + "COALESCE(GROUP_CONCAT(CASE WHEN PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR') THEN CONCAT(PR_PIECE_NO,'(',PR_PIECE_STAGE,')') END),'') AS NON_CAN_PIECE,"
//                + "GROUP_CONCAT(CONCAT(PR_PIECE_NO,'(',PR_PIECE_STAGE,')')) AS CANPIECE, COUNT(PR_PIECE_NO) FROM "
//                + "(SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL P WHERE COALESCE(APPROVED,0) =1 AND COALESCE(CANCELED,0) =0 AND LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(curdate()),2)) AND ACESSD_QTY > 0 ) AS A "
//                + "LEFT JOIN (SELECT PR_UPN,PR_PIECE_NO,PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_UPN IN (SELECT UPN FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL P WHERE COALESCE(APPROVED,0) =1 AND COALESCE(CANCELED,0) =0 AND LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(curdate()),2)) AND ACESSD_QTY > 0) AND PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPIRALLING','SEAMING','ASSEMBLY','IN STOCK','BSR') ) AS B ON PR_UPN= UPN GROUP BY UPN ORDER BY UPN) AS D WHERE CANPIECE IS NOT NULL) AS C "
//                + "SET CANCELABLED_PIECE_NO=CANCELPIECE,CANCELABLED_PIECES=CAN_PIECE,"
//                + "NONCANCELABLED_PIECE_NO=NONCANCELPIECE,NONCANCELABLED_PIECES=NON_CAN_PIECE "
//                + "WHERE D.UPN=C.UPN AND LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PR_UPN AS OCUPN,COUNT(*) AS OCSTK FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                + "WHERE UPPER(PR_OC_MONTHYEAR)='" + mn + " - 2020' "
//                + "AND PR_PIECE_STAGE IN ('IN STOCK','BSR') "
//                + "GROUP BY PR_UPN) AS OC "
//                + "SET OC_STOCK=OCSTK "
//                + "WHERE D.UPN=OC.OCUPN  AND LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PR_UPN AS OCUPN,COUNT(*) AS OCSTK,PR_OC_MONTHYEAR,PR_OC_LAST_DDMMYY "
//                + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER D "
//                + "WHERE PR_PIECE_STAGE IN ('IN STOCK','BSR') AND COALESCE(PR_DELINK,'')='' "
//                + "  AND (PR_OC_LAST_DDMMYY!='0000-00-00' OR COALESCE(PR_OC_MONTHYEAR,'')!='')"
//                + "GROUP BY PR_UPN) AS OC "
//                + "SET OC_STOCK=OCSTK "
//                + "WHERE D.UPN=OC.OCUPN  AND LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D"
                + "SET OC_STOCK=0 "
                + "WHERE D.UPN=OC.OCUPN  AND LEFT(D.DOC_NO,7)=CONCAT('B2122',RIGHT(100+(MONTH(CURDATE())),2))"
                + " AND D.YEAR_FROM=2020 ");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D"
                + "SET OC_STOCK=0 "
                + "WHERE D.UPN=OC.OCUPN  AND LEFT(D.DOC_NO,7)=CONCAT('B2122',RIGHT(100+(MONTH(CURDATE())),2))"
                + " AND D.YEAR_FROM=2021 ");
        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WH_MTH_CLOSING_PIECE_STOCK "
                + "SET REQ_MONTH=PR_REQUESTED_MONTH,REQ_LAST_DDMMYY=PR_REQ_MTH_LAST_DDMMYY,"
                + "OC_LAST_DDMMYY=PR_OC_LAST_DDMMYY,"
                + "CURRENT_LAST_DDMMYY=PR_CURRENT_SCH_LAST_DDMMYY "
                + "WHERE PIECE_NO=PR_PIECE_NO AND MTH_CLOSING_DATE='" + lstdt + "' ");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT UPN AS OCUPN,COUNT(*) AS OCSTK,REQ_LAST_DDMMYY FROM PRODUCTION.FELT_WH_MTH_CLOSING_PIECE_STOCK F "
                + "WHERE MTH_CLOSING_DATE='" + lstdt + "' "
                + "AND COALESCE(OC_MONTH,'')!='' "
                + "AND PIECE_STAGE IN ('IN STOCK','BSR') "
                + "GROUP BY UPN) AS OC "
                + "SET OC_STOCK=OCSTK "
                + "WHERE D.UPN=OC.OCUPN  AND LEFT(D.DOC_NO,7)=CONCAT('B2122',RIGHT(100+(MONTH(CURDATE())),2)) "
                + " AND D.YEAR_FROM=2020 AND REQ_LAST_DDMMYY<'2021-04-01'");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT UPN AS OCUPN,COUNT(*) AS OCSTK,REQ_LAST_DDMMYY FROM PRODUCTION.FELT_WH_MTH_CLOSING_PIECE_STOCK F "
                + "WHERE MTH_CLOSING_DATE='" + lstdt + "' "
                + "AND COALESCE(OC_MONTH,'')!='' "
                + "AND PIECE_STAGE IN ('IN STOCK','BSR') "
                + "GROUP BY UPN) AS OC "
                + "SET OC_STOCK=OCSTK "
                + "WHERE D.UPN=OC.OCUPN  AND LEFT(D.DOC_NO,7)=CONCAT('B2122',RIGHT(100+(MONTH(CURDATE())),2)) "
                + " AND D.YEAR_FROM=2021 AND REQ_LAST_DDMMYY>='2021-04-01'");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PR_UPN,COUNT(PR_UPN) AS PGR,SUM(NET_AMOUNT) AS PGRVAL FROM (SELECT PIECE_NO,NET_AMOUNT,ACTUAL_WEIGHT FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H "
//                + "WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 "
//                + "AND INVOICE_DATE<'2020-04-01' AND H.DOC_DATE>='2020-04-01' AND H.DOC_DATE<='2021-03-31') AS A "
//                + "LEFT JOIN (SELECT PR_PIECE_NO,PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER) AS P "
//                + "ON PIECE_NO=PR_PIECE_NO "
//                + "GROUP BY PR_UPN) AS GR "
//                + "SET PREV_GR_QTY=PGR,PREV_GR_VALUE=PGRVAL "
//                + "WHERE D.UPN=GR.PR_UPN   AND LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D,(SELECT PR_PARTY_CODE AS PARTY_CODE,PR_MACHINE_NO AS MACHINE_NO,PR_POSITION_NO AS POSITION_NO,"
//                + "COUNT(FELT_AMEND_PIECE_NO) AS INVQTY,SUM(FELT_AMEND_EXPORT_INV_AMT) AS INVAMT  FROM PRODUCTION.FELT_PIECE_AMEND,PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                + "WHERE FELT_AMEND_REASON =8 AND PR_PIECE_NO = FELT_AMEND_PIECE_NO AND FELT_AMEND_EXPORT_INV_DATE >= '2020-04-01'  AND FELT_AMEND_EXPORT_INV_DATE<='" + lstdt + "' "
//                + "GROUP BY PR_PARTY_CODE,PR_MACHINE_NO,PR_POSITION_NO) AS I  "
//                + "SET D.DISPATCH_QTY=I.INVQTY,DISPATCH_VALUE=INVAMT "
//                + "WHERE  LEFT(D.DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2)) AND D.PARTY_CODE=I.PARTY_CODE AND D.MACHINE_NO=I.MACHINE_NO AND D.POSITION_NO=I.POSITION_NO");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + "SET WIP_VALUE=WIP_QTY*SELLING_PRICE,STOCK_VALUE=STOCK_QTY*SELLING_PRICE,ACTUAL_BUDGET_VALUE=ACTUAL_BUDGET*SELLING_PRICE,"
//                + "CURRENT_PROJECTION_VALUE=CURRENT_PROJECTION*SELLING_PRICE,"
//                + mn + "_NET_AMOUNT=" + mn + "_BUDGET*SELLING_PRICE WHERE LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))  AND INCHARGE =6 AND LEFT(PARTY_CODE,1)!='8'");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + "SET ACESS_QTY=(WIP_QTY+STOCK_QTY+DISPATCH_QTY)-CURRENT_PROJECTION "
//                + "WHERE  LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + "SET ACESS_QTY=0 "
//                + "WHERE ACESS_QTY<0 AND LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + "SET ACESSD_QTY=(WIP_QTY+STOCK_QTY+DISPATCH_QTY)-(CURRENT_PROJECTION-coalesce(" + cmnth + "_DOUBTFUL_QTY,0))"
//                + "WHERE  LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+(MONTH(CURDATE())),2))");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET ACESS_QTY=0 "
                + "WHERE ACESSD_QTY<0 AND LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+(MONTH(CURDATE())),2))");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET ACESS_QTY=0 "
                + "WHERE ACESSD_QTY<0 AND LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+MONTH(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),2))");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET ACESSD_QTY=0 "
                + "WHERE ACESSD_QTY<0 AND LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+(MONTH(CURDATE())),2))");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET ACESSD_QTY=0 "
                + "WHERE ACESSD_QTY<0 AND LEFT(DOC_NO,7)=CONCAT('B2122',RIGHT(100+MONTH(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),2))");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET ACESS_VALUE=ACESS_QTY*SELLING_PRICE ");

        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                + "SET ACESSD_VALUE=ACESSD_QTY*SELLING_PRICE ");

//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + "SET ACESS_VALUE=ACESS_QTY*SELLING_PRICE "
//                + "WHERE LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),2))");
//        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                + "SET ACESSD_VALUE=ACESSD_QTY*SELLING_PRICE "
//                + "WHERE LEFT(DOC_NO,7)=CONCAT('B2021',RIGHT(100+MONTH(DATE_ADD(LAST_DAY(CURDATE()),INTERVAL 1 DAY)),2))");
    }
}
