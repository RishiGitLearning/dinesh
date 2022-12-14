/*
 * frmChangePassword.java
 *
 * Created on July 3, 2004, 3:36 PM
 */
package EITLERP.FeltSales.PieceRegister;

import EITLERP.*;
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
public class FrmMISAdvanceSearch extends javax.swing.JApplet {

    private EITLTableModel DataModel = new EITLTableModel();

    private EITLComboModel modelDept = new EITLComboModel();
    private EITLComboModel modelShift = new EITLComboModel();
    private EITLComboModel modelMainCategory = new EITLComboModel();
    private EITLComboModel modelCategory = new EITLComboModel();
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    //GenerateInvoiceParameterModificationCombo();
    /**
     * Initializes the applet frmChangePassword
     */
    public void init() {
        initComponents();
        setSize(1000, 750);

        jLabel1.setForeground(Color.WHITE);

        FormatGridPeriodPRJSAL();
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
        ShiftSchedule = new javax.swing.JPanel();
        btnPeriodPRJSALView = new javax.swing.JButton();
        lblMonthCmb4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnMonthwisePRJSALView = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane25 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        btnEmpMstETE = new javax.swing.JButton();
        cmbFY = new javax.swing.JComboBox();
        lblMonthCmb1 = new javax.swing.JLabel();
        lblMonthCmb2 = new javax.swing.JLabel();
        lblMonthCmb3 = new javax.swing.JLabel();
        lblMonthCmb = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("MIS - Advance Search");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 2, 1000, 25);

        jLabel2.setBackground(new java.awt.Color(0, 102, 153));
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 120, 1000, 10);

        TabList.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabListStateChanged(evt);
            }
        });

        ShiftSchedule.setLayout(null);

        btnPeriodPRJSALView.setText("View");
        btnPeriodPRJSALView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPeriodPRJSALViewActionPerformed(evt);
            }
        });
        ShiftSchedule.add(btnPeriodPRJSALView);
        btnPeriodPRJSALView.setBounds(860, 0, 100, 30);

        lblMonthCmb4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblMonthCmb4.setText("Sub Total = (Total Sales FY + Ordered + WIP + Stock + BSR)");
        ShiftSchedule.add(lblMonthCmb4);
        lblMonthCmb4.setBounds(10, 0, 500, 20);

        TabList.addTab("Projected Sales for Period", ShiftSchedule);

        jPanel1.setLayout(null);

        btnMonthwisePRJSALView.setText("View");
        btnMonthwisePRJSALView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonthwisePRJSALViewActionPerformed(evt);
            }
        });
        jPanel1.add(btnMonthwisePRJSALView);
        btnMonthwisePRJSALView.setBounds(860, 0, 100, 30);

        TabList.addTab("Monthwise Projected Sales", jPanel1);

        getContentPane().add(TabList);
        TabList.setBounds(10, 133, 980, 80);

        btnClear.setText("Clear All");
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear);
        btnClear.setBounds(870, 80, 110, 30);

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

        cmbFY.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "2019-2020" }));
        getContentPane().add(cmbFY);
        cmbFY.setBounds(140, 60, 170, 20);

        lblMonthCmb1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMonthCmb1.setText("Financial Year : ");
        getContentPane().add(lblMonthCmb1);
        lblMonthCmb1.setBounds(10, 60, 120, 20);

        lblMonthCmb2.setText("Note : ");
        getContentPane().add(lblMonthCmb2);
        lblMonthCmb2.setBounds(10, 550, 50, 20);

        lblMonthCmb3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblMonthCmb3.setText("All Values are as on Date (in Lakhs).");
        getContentPane().add(lblMonthCmb3);
        lblMonthCmb3.setBounds(70, 550, 500, 20);

        lblMonthCmb.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblMonthCmb.setText("Difference = (Total Actual - Rev. Total Projected)");
        getContentPane().add(lblMonthCmb);
        lblMonthCmb.setBounds(70, 570, 500, 20);
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

    private void btnPeriodPRJSALViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPeriodPRJSALViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GeneratePeriodPRJSAL();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnPeriodPRJSALViewActionPerformed

    private void TabListStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabListStateChanged
        // TODO add your handling code here:
        if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Projected Sales for Period")) {
            FormatGridPeriodPRJSAL();
        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Monthwise Projected Sales")) {
            FormatGridMonthwisePRJSAL();
        }
    }//GEN-LAST:event_TabListStateChanged

    private void btnMonthwisePRJSALViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonthwisePRJSALViewActionPerformed
        // TODO add your handling code here:
        if (!cmbFY.getSelectedItem().equals("Select")) {
            GenerateMonthwisePRJSAL();
        } else {
            JOptionPane.showMessageDialog(null, "Select Financial Year");
        }
    }//GEN-LAST:event_btnMonthwisePRJSALViewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser ExporttoExcelFileChooser;
    private javax.swing.JPanel ShiftSchedule;
    private javax.swing.JTabbedPane TabList;
    private javax.swing.JTable Table;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEmpMstETE;
    private javax.swing.JButton btnMonthwisePRJSALView;
    private javax.swing.JButton btnPeriodPRJSALView;
    private javax.swing.JComboBox cmbFY;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JLabel lblMonthCmb;
    private javax.swing.JLabel lblMonthCmb1;
    private javax.swing.JLabel lblMonthCmb2;
    private javax.swing.JLabel lblMonthCmb3;
    private javax.swing.JLabel lblMonthCmb4;
    // End of variables declaration//GEN-END:variables

    private void FormatGridPeriodPRJSAL() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Actual Sales");
        DataModel.addColumn("GR");
        DataModel.addColumn("Net Sales");
        DataModel.addColumn("Ordered");
        DataModel.addColumn("WIP");
        DataModel.addColumn("Stock");
        DataModel.addColumn("BSR");
        DataModel.addColumn("Sub Total");
        DataModel.addColumn("Difference");
        DataModel.addColumn("Total Projected Sales");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GeneratePeriodPRJSAL() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridPeriodPRJSAL(); //clear existing content of table
            ResultSet rsTmp;

            String strSQL = "";

            strSQL = "SELECT * FROM PRODUCTION.FELT_PROJECTED_ORDER_SALES WHERE FY = '" + cmbFY.getSelectedItem() + "' ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
            System.out.println("Row no." + rsTmp.getRow());
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
                    rowData[4] = rsTmp.getString("SALES_FY");
                    rowData[5] = rsTmp.getString("GR_FY");
                    rowData[6] = rsTmp.getString("TOTAL_SALES_FY");
                    rowData[7] = rsTmp.getString("ORDER_FY");
                    rowData[8] = rsTmp.getString("WIP_FY");
                    rowData[9] = rsTmp.getString("STOCK_FY");
                    rowData[10] = rsTmp.getString("BSR_FY");
                    rowData[11] = EITLERPGLOBAL.round(Double.parseDouble(rsTmp.getString("TOTAL_SALES_FY"))+Double.parseDouble(rsTmp.getString("ORDER_FY"))+Double.parseDouble(rsTmp.getString("WIP_FY"))+Double.parseDouble(rsTmp.getString("STOCK_FY"))+Double.parseDouble(rsTmp.getString("BSR_FY")),2);
                    rowData[12] = rsTmp.getString("TOTAL_SALES_DIFF");
                    rowData[13] = rsTmp.getString("TOTAL_SALES_PRJ");
                    rowData[14] = EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime();

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
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void FormatGridMonthwisePRJSAL() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Zone");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Oct Projected");
        DataModel.addColumn("Oct Actual");
        DataModel.addColumn("Nov Projected");
        DataModel.addColumn("Rev. Nov Projected");
        DataModel.addColumn("Nov Actual");
        DataModel.addColumn("Dec Projected");
        DataModel.addColumn("Rev. Dec Projected");
        DataModel.addColumn("Dec Actual");
        DataModel.addColumn("Total Projected");
        DataModel.addColumn("Rev. Total Projected");
        DataModel.addColumn("Difference");
        DataModel.addColumn("Total Actual");
        DataModel.addColumn("Next FY Actual");
        DataModel.addColumn("RunTime");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateMonthwisePRJSAL() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridMonthwisePRJSAL(); //clear existing content of table
            ResultSet rsTmp;

            String strSQL = "";

            strSQL = "SELECT * FROM PRODUCTION.FELT_PROJECTED_ORDER_SALES WHERE FY = '" + cmbFY.getSelectedItem() + "' ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
            System.out.println("Row no." + rsTmp.getRow());
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
                    rowData[4] = rsTmp.getString("OCT_ORDER_PRJ");
                    rowData[5] = rsTmp.getString("OCT_ORDER_ACT");
                    rowData[6] = rsTmp.getString("NOV_ORDER_PRJ");
                    rowData[7] = rsTmp.getString("NOV_ORDER_PRJ_REV");
                    rowData[8] = rsTmp.getString("NOV_ORDER_ACT");
                    rowData[9] = rsTmp.getString("DEC_ORDER_PRJ");
                    rowData[10] = rsTmp.getString("DEC_ORDER_PRJ_REV");
                    rowData[11] = rsTmp.getString("DEC_ORDER_ACT");
                    rowData[12] = rsTmp.getString("TOTALFY_ORDER_PRJ");
                    rowData[13] = rsTmp.getString("TOTALFY_ORDER_PRJ_REV");
//                    rowData[11] = EITLERPGLOBAL.round(Double.parseDouble(rsTmp.getString("TOTALFY_ORDER_ACT"))-Double.parseDouble(rsTmp.getString("TOTALFY_ORDER_PRJ")),2);
                    rowData[14] = EITLERPGLOBAL.round(Double.parseDouble(rsTmp.getString("TOTALFY_ORDER_ACT"))-Double.parseDouble(rsTmp.getString("TOTALFY_ORDER_PRJ_REV")),2);
                    rowData[15] = rsTmp.getString("TOTALFY_ORDER_ACT");
                    rowData[16] = rsTmp.getString("NEXTFY_ORDER_ACT");
                    rowData[17] = EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime();

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
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}
