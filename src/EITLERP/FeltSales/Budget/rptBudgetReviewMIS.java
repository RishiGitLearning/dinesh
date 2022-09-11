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
public class rptBudgetReviewMIS extends javax.swing.JApplet {

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
        FormatGridPPW();
        cmbFY.setSelectedIndex(cmbFY.getItemCount() - 1);
        if (cmbmonth.getSelectedItem().toString().equalsIgnoreCase("2") || cmbmonth.getSelectedItem().toString().equalsIgnoreCase("3")) {
            pfinyear = "2021";
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
        cmbmonth.setSelectedIndex(data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL") - 1);
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
        jPanel12 = new javax.swing.JPanel();
        btnPWView = new javax.swing.JButton();
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
        cmbmonth.setBounds(370, 30, 60, 20);

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

    private void TabListStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabListStateChanged
        // TODO add your handling code here:
        try {
            if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Partywise")) {
                FormatGridPPW();
                cmbStatus.setEnabled(true);
                INCHARGE.setEnabled(true);
                txtpartycode.setEnabled(true);
                txtproductcode.setEnabled(false);
                cmbmonth.setEnabled(true);
                INCHARGE.setSelectedIndex(0);
                txtpartycode.setText("");
                txtproductcode.setText("");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_TabListStateChanged

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
            pfinyear = "2021";
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

    private void btnPWViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPWViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GeneratePPW();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnPWViewActionPerformed

    private void cmbFYItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFYItemStateChanged
        // TODO add your handling code here:
        SetVariable();
        if (cmbmonth.getSelectedItem().toString().equalsIgnoreCase("2") || cmbmonth.getSelectedItem().toString().equalsIgnoreCase("3")) {
            pfinyear = "2021";
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
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEmpMstETE;
    private javax.swing.JButton btnPWView;
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
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JTextArea jTextArea1;
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
                        remainmonth = 12;
                        currentmonth = 1;
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
                        remainmonth = 12;
                        currentmonth = 1;
                        break OUTER_1;
                    case "2020":
                        remainmonth = 1;
                        currentmonth = 12;
                        break OUTER_1;
                }
                break;
        }
    }

    private void FormatGridPPW() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("PARTY CODE");
        DataModel.addColumn("NAME");
        DataModel.addColumn("Product Code");
        DataModel.addColumn("Product Group");
        DataModel.addColumn("Kg");
        DataModel.addColumn("SQMTR");
        DataModel.addColumn("CP [" + cmnth + "] QTY");
        DataModel.addColumn("CP [" + cmnth + "] Value(Rs Lacs)");
        DataModel.addColumn("Disc%");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GeneratePPW() {
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
            FormatGridPPW(); //clear existing content of table
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
            String strSQL = "SELECT INCHARGE_NAME AS ZONE,PARTY_CODE,PARTY_NAME,QUALITY_NO,PROD_GROUP AS PRODUCT,"
                    + "c_p_weight AS KG,c_d_sqmtr AS SQMTR,cbudget AS NO_OF_PIECES,cvalue AS VALUE,"
                    + "CASE WHEN GROUP_NAME ='SDF' AND SPL_DISCOUNT =0 THEN 50 WHEN GROUP_NAME ='HDS' AND SPL_DISCOUNT =0 THEN 21 "
                    + "WHEN DISC_PER>0 AND SPL_DISCOUNT=0 THEN DISC_PER ELSE SPL_DISCOUNT END AS DISC_PER FROM ("
                    + "SELECT PARTY_CODE,PARTY_NAME,QUALITY_NO,INCHARGE_NAME,GROUP_NAME,SPL_DISCOUNT,CONVERT(CASE WHEN GROUP_NAME IN ('ACNE','FCNE') THEN 'ACNE' "
                    + "WHEN GROUP_NAME IN ('HDS') THEN 'HDS' WHEN GROUP_NAME IN ('SDF') THEN 'SDF' ELSE 'MNE/MNG' END USING UTF8) AS PROD_GROUP, "
                    + "SIZE_CRITERIA,";

            strSQL = strSQL + "sum(coalesce(" + cmnth + "_budget,0)) as cbudget,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0)+coalesce(dry_weight,0))),2) as cweight,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(press_weight,0))),2) as c_p_weight,"
                    + "round(sum(coalesce(" + cmnth + "_budget,0)*(coalesce(dry_sqmtr,0))),2) as c_d_sqmtr,"
                    + "SUM(round(coalesce(" + cmnth + "_net_amount,0)/100000,2)) as cvalue ";

            strSQL = strSQL + " FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL,PRODUCTION.FELT_INCHARGE "
                    + "  WHERE YEAR_FROM=" + pyear + "  AND INCHARGE = INCHARGE_CD "
                    + "and left(doc_no,7)=CONCAT('B" + pfinyear + "',RIGHT(100+" + curmnth + ",2)) "
                    + "AND (coalesce(ACTUAL_BUDGET,0)>0 OR ";
            if (!pmnth.equalsIgnoreCase("")) {
                strSQL = strSQL + "coalesce(" + pmnth + "_budget,0)>0 OR ";
            }
            strSQL = strSQL + "coalesce(" + cmnth + "_budget,0)>0) "
                    + cndtn
                    + "  group by PARTY_CODE,INCHARGE_NAME,QUALITY_NO,SIZE_CRITERIA "
                    + "order by PARTY_CODE,INCHARGE_NAME,QUALITY_NO,SIZE_CRITERIA) AS DD "
                    + "  LEFT JOIN (SELECT DISTINCT PARTY_CODE AS PCD,PRODUCT_CODE AS  PRODUCT,DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D "
                    + "WHERE COALESCE(APPROVED,0)=1 AND COALESCE(CANCELED,0)=0 AND COALESCE(MACHINE_NO,'')='' AND COALESCE(MACHINE_POSITION,0)*1=0 "
                    + "AND COALESCE(PIECE_NO,'') IN ('','NULL','null') AND EFFECTIVE_FROM <= CURDATE() AND "
                    + "( EFFECTIVE_TO>= CURDATE() OR COALESCE(EFFECTIVE_TO,'0000-00-00')='0000-00-00') AND "
                    + "LENGTH(COALESCE(PRODUCT_CODE,''))=6 AND LENGTH(COALESCE(PARTY_CODE,''))=6 AND  DIVERSION_FLAG=0 "
                    + "ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC) AS DISC "
                    + "ON  PARTY_CODE=PCD AND QUALITY_NO=PRODUCT";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
//                p = rsTmp.getString("PROD_GROUP");
//                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                String mcurdttime = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("ZONE");
                    rowData[2] = rsTmp.getString("PARTY_CODE");
                    rowData[3] = rsTmp.getString("PARTY_NAME");
                    rowData[4] = rsTmp.getString("QUALITY_NO");
                    rowData[5] = rsTmp.getString("PRODUCT");
                    rowData[6] = rsTmp.getString("KG");
                    rowData[7] = rsTmp.getString("SQMTR");
                    rowData[8] = rsTmp.getString("NO_OF_PIECES");
                    rowData[9] = rsTmp.getString("VALUE");
                    rowData[10] = rsTmp.getString("DISC_PER");
                    rowData[11] = mcurdttime;

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }

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

}
