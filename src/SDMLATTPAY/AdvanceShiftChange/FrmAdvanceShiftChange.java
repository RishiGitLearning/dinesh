/*35833b,35834b
 * frmChangePassword.java
 *
 * Created on July 3, 2004, 3:36 PM
 */
package SDMLATTPAY.AdvanceShiftChange;

import EITLERP.*;
import SDMLATTPAY.Employee.clsMaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
//import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
//import EITLERP.Sales.clsExcelExporter;

/*<APPLET CODE=frmChangePassword HEIGHT=200 WIDTH=430></APPLET>*/
/**
 *
 * @author Daxesh Prajapati
 */
public class FrmAdvanceShiftChange extends javax.swing.JApplet {

    private clsExcel_Exporter exp = new clsExcel_Exporter();

    private EITLTableModel DataModel = new EITLTableModel();

    private EITLComboModel modelDept = new EITLComboModel();
    private EITLComboModel modelShift = new EITLComboModel();
    private EITLComboModel modelMainCategory = new EITLComboModel();
    private EITLComboModel modelCategory = new EITLComboModel();
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();
    Map<String, Object> map = new HashMap<String, Object>();
    private EITLTableModel DataModel_Punch_From;
    private EITLTableModel DataModel_Punch_From1;
    private EITLTableModel DataModel_To_DELETION;
    private EITLTableModel DataModel_To_DELETION1;
    private EITLTableModel DataModel_To_SHIFT_99;
    private EITLTableModel DataModel_To_SHIFT_REG;
    private EITLTableModel DataModel_To_SHIFT_11;
    private EITLTableModel DataModel_History;
    private EITLTableModel DataModel_Deleted_Revert;

    //GenerateInvoiceParameterModificationCombo();
    /**
     * Initializes the applet frmChangePassword
     */
    public void init() {
        initComponents();
        //setSize(1320, 750);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);

        jLabel1.setForeground(Color.WHITE);
        txtFromDate.setText("");
        txtToDate.setText("");
        txtFromDate.setEnabled(false);
        txtToDate.setEnabled(false);

        GenerateDeptCombo();
        GenerateShiftCombo();
        GenerateMainCategoryCombo();
        GenerateCategoryCombo();

        map_initilization();
        //DailyAttSummary.setSize(dim.width,170);
        btnUpdate.setEnabled(false);
        int authority_SSC = data.getIntValueFromDB("SELECT SSC_UPDATE FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE_UPDATE_AUTHORITY where USER_ID='" + EITLERPGLOBAL.gUserID + "'");
        if (authority_SSC == 1) {
            btnUpdate.setEnabled(true);
        }

        btnUpdatePunch.setEnabled(false);
        btnUpdatePunch1.setEnabled(false);
        int authority_PUNCH = data.getIntValueFromDB("SELECT PUNCH_UPDATE FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE_UPDATE_AUTHORITY where USER_ID='" + EITLERPGLOBAL.gUserID + "'");
        if (authority_PUNCH == 1) {
            btnUpdatePunch.setEnabled(true);
            btnUpdatePunch1.setEnabled(true);
        }

        FormatGrid_PunchUpdate();
        cmbMonth.setSelectedIndex(EITLERPGLOBAL.getCurrentMonth());
        txtYear.setText(EITLERPGLOBAL.getCurrentYear() + "");
        cmbMonthHistory.setSelectedIndex(EITLERPGLOBAL.getCurrentMonth());
        txtYearHistory.setText(EITLERPGLOBAL.getCurrentYear() + "");

        btnRevertSelected.setEnabled(false);
        int authority_REVERT_DELETED = data.getIntValueFromDB("SELECT REVERT_DELETED FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE_UPDATE_AUTHORITY where USER_ID='" + EITLERPGLOBAL.gUserID + "'");
        if (authority_REVERT_DELETED == 1) {
            btnRevertSelected.setEnabled(true);
        }

    }

    private void FormatGrid_PunchUpdate() {
        try {

            DataModel_Punch_From = new EITLTableModel();
            tbl_Punch_From.removeAll();

            tbl_Punch_From.setModel(DataModel_Punch_From);
            tbl_Punch_From.setAutoResizeMode(0);

            DataModel_Punch_From.addColumn("PUNCH TIME"); //1
            DataModel_Punch_From.SetReadOnly(0);

            tbl_Punch_From.getColumnModel().getColumn(0).setMinWidth(87);

            DataModel_Punch_From1 = new EITLTableModel();
            tbl_Punch_From1.removeAll();

            tbl_Punch_From1.setModel(DataModel_Punch_From1);
            tbl_Punch_From1.setAutoResizeMode(0);

            DataModel_Punch_From1.addColumn("PUNCH TIME"); //1
            DataModel_Punch_From1.SetReadOnly(0);

            tbl_Punch_From1.getColumnModel().getColumn(0).setMinWidth(87);

            DataModel_To_DELETION = new EITLTableModel();
            tbl_To_DELETION.removeAll();

            tbl_To_DELETION.setModel(DataModel_To_DELETION);
            tbl_To_DELETION.setAutoResizeMode(0);

            DataModel_To_DELETION.addColumn("PUNCH TIME"); //1
            DataModel_To_DELETION.SetReadOnly(0);

            tbl_To_DELETION.getColumnModel().getColumn(0).setMinWidth(87);
            
            DataModel_To_DELETION1 = new EITLTableModel();
            tbl_To_DELETION1.removeAll();

            tbl_To_DELETION1.setModel(DataModel_To_DELETION1);
            tbl_To_DELETION1.setAutoResizeMode(0);

            DataModel_To_DELETION1.addColumn("PUNCH TIME"); //1
            DataModel_To_DELETION1.SetReadOnly(0);

            tbl_To_DELETION1.getColumnModel().getColumn(0).setMinWidth(87);

            DataModel_To_SHIFT_99 = new EITLTableModel();
            tbl_To_SHIFT_99.removeAll();

            tbl_To_SHIFT_99.setModel(DataModel_To_SHIFT_99);
            tbl_To_SHIFT_99.setAutoResizeMode(0);

            DataModel_To_SHIFT_99.addColumn("PUNCH TIME"); //1
            DataModel_To_SHIFT_99.SetReadOnly(0);

            tbl_To_SHIFT_99.getColumnModel().getColumn(0).setMinWidth(87);

            DataModel_To_SHIFT_REG = new EITLTableModel();
            tbl_To_SHIFT_REG.removeAll();

            tbl_To_SHIFT_REG.setModel(DataModel_To_SHIFT_REG);
            tbl_To_SHIFT_REG.setAutoResizeMode(0);

            DataModel_To_SHIFT_REG.addColumn("PUNCH TIME"); //1
            DataModel_To_SHIFT_REG.SetReadOnly(0);

            tbl_To_SHIFT_REG.getColumnModel().getColumn(0).setMinWidth(87);

            DataModel_To_SHIFT_11 = new EITLTableModel();
            tbl_To_SHIFT_11.removeAll();

            tbl_To_SHIFT_11.setModel(DataModel_To_SHIFT_11);
            tbl_To_SHIFT_11.setAutoResizeMode(0);

            DataModel_To_SHIFT_11.addColumn("PUNCH TIME"); //1
            DataModel_To_SHIFT_11.SetReadOnly(0);

            tbl_To_SHIFT_11.getColumnModel().getColumn(0).setMinWidth(87);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        CoffRokadiBtnGrp = new javax.swing.ButtonGroup();
        LCBtnGrp = new javax.swing.ButtonGroup();
        GatePassBtnGrp = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        lblEmpNo = new javax.swing.JLabel();
        lblMonthCmb = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        TabList = new javax.swing.JTabbedPane();
        DailyAttSummary = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblSelectedEmpName = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        txtSelectedEmpNo_shiftchange = new javax.swing.JTextField();
        txtSelectedEmpName_shiftchange = new javax.swing.JTextField();
        txtSelectedDept_shiftchange = new javax.swing.JTextField();
        txtDay2 = new javax.swing.JTextField();
        txtDay3 = new javax.swing.JTextField();
        txtDay4 = new javax.swing.JTextField();
        txtDay5 = new javax.swing.JTextField();
        txtDay6 = new javax.swing.JTextField();
        txtDay7 = new javax.swing.JTextField();
        txtDay8 = new javax.swing.JTextField();
        txtDay9 = new javax.swing.JTextField();
        txtDay10 = new javax.swing.JTextField();
        txtDay11 = new javax.swing.JTextField();
        txtDay12 = new javax.swing.JTextField();
        txtDay13 = new javax.swing.JTextField();
        txtDay14 = new javax.swing.JTextField();
        txtDay15 = new javax.swing.JTextField();
        txtDay16 = new javax.swing.JTextField();
        txtDay17 = new javax.swing.JTextField();
        txtDay18 = new javax.swing.JTextField();
        txtDay19 = new javax.swing.JTextField();
        txtDay20 = new javax.swing.JTextField();
        txtDay21 = new javax.swing.JTextField();
        txtDay22 = new javax.swing.JTextField();
        txtDay23 = new javax.swing.JTextField();
        txtDay24 = new javax.swing.JTextField();
        txtDay25 = new javax.swing.JTextField();
        txtDay31 = new javax.swing.JTextField();
        txtDay26 = new javax.swing.JTextField();
        txtDay27 = new javax.swing.JTextField();
        txtDay28 = new javax.swing.JTextField();
        txtDay29 = new javax.swing.JTextField();
        txtDay30 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSelectedYear_shiftchange = new javax.swing.JTextField();
        txtSelectedMonth_shiftchange = new javax.swing.JTextField();
        txtDay1 = new javax.swing.JTextField();
        txtDayOT1 = new javax.swing.JTextField();
        txtDayOT2 = new javax.swing.JTextField();
        txtDayOT3 = new javax.swing.JTextField();
        txtDayOT4 = new javax.swing.JTextField();
        txtDayOT5 = new javax.swing.JTextField();
        txtDayOT6 = new javax.swing.JTextField();
        txtDayOT8 = new javax.swing.JTextField();
        txtDayOT7 = new javax.swing.JTextField();
        txtDayOT9 = new javax.swing.JTextField();
        txtDayOT10 = new javax.swing.JTextField();
        txtDayOT11 = new javax.swing.JTextField();
        txtDayOT12 = new javax.swing.JTextField();
        txtDayOT13 = new javax.swing.JTextField();
        txtDayOT14 = new javax.swing.JTextField();
        txtDayOT16 = new javax.swing.JTextField();
        txtDayOT15 = new javax.swing.JTextField();
        txtDayOT17 = new javax.swing.JTextField();
        txtDayOT18 = new javax.swing.JTextField();
        txtDayOT20 = new javax.swing.JTextField();
        txtDayOT19 = new javax.swing.JTextField();
        txtDayOT21 = new javax.swing.JTextField();
        txtDayOT22 = new javax.swing.JTextField();
        txtDayOT23 = new javax.swing.JTextField();
        txtDayOT24 = new javax.swing.JTextField();
        txtDayOT25 = new javax.swing.JTextField();
        txtDayOT26 = new javax.swing.JTextField();
        txtDayOT27 = new javax.swing.JTextField();
        txtDayOT28 = new javax.swing.JTextField();
        txtDayOT29 = new javax.swing.JTextField();
        txtDayOT30 = new javax.swing.JTextField();
        txtDayOT31 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        txtDate_updatepunch = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtSelectedEmpNo_updatepunch = new javax.swing.JTextField();
        lblSelectedEmpName1 = new javax.swing.JLabel();
        txtSelectedEmpName_updatepunch = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtSelectedDept_updatepunch = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_Punch_From = new javax.swing.JTable();
        btnDeletion = new javax.swing.JButton();
        btnShift99 = new javax.swing.JButton();
        btnShift11 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_To_DELETION = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_To_SHIFT_99 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_To_SHIFT_11 = new javax.swing.JTable();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        btnUpdatePunch = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        txtDate_updatepunch1 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtSelectedEmpNo_updatepunch1 = new javax.swing.JTextField();
        lblSelectedEmpName2 = new javax.swing.JLabel();
        txtSelectedEmpName_updatepunch1 = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtSelectedDept_updatepunch1 = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbl_Punch_From1 = new javax.swing.JTable();
        btnShiftReg = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbl_To_SHIFT_REG = new javax.swing.JTable();
        jLabel60 = new javax.swing.JLabel();
        btnUpdatePunch1 = new javax.swing.JButton();
        jLabel58 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbl_To_DELETION1 = new javax.swing.JTable();
        btnDeletion1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        txtRevertDeleted = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txtYearRevertDeleted = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        btnRevertDeleted_ShowData = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblRevertDeleted = new javax.swing.JTable();
        lblEmpNameHistory1 = new javax.swing.JLabel();
        cmbMonthRevertDeleted = new javax.swing.JComboBox();
        btnRevertSelected = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        txtEmpNoSearch = new javax.swing.JTextField();
        cmbDataUpdate = new javax.swing.JComboBox();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtYearHistory = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        btnHistory = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
        lblEmpNameHistory = new javax.swing.JLabel();
        cmbMonthHistory = new javax.swing.JComboBox();
        btnClear = new javax.swing.JButton();
        txtEmpNo = new javax.swing.JTextField();
        txtEmpName = new javax.swing.JTextField();
        lblYearCmb = new javax.swing.JLabel();
        lblDeptCmb = new javax.swing.JLabel();
        cmbDept = new javax.swing.JComboBox();
        lblShiftCmb = new javax.swing.JLabel();
        cmbShift = new javax.swing.JComboBox();
        txtYear = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        cmbMainCategory = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox();
        jScrollPane25 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        lblDate1 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnDailyAttView = new javax.swing.JButton();
        cmbMonth = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Attendance - Advance Shift Change");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 2, 1380, 25);

        lblEmpNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmpNo.setText("Employee No : ");
        getContentPane().add(lblEmpNo);
        lblEmpNo.setBounds(10, 30, 110, 20);

        lblMonthCmb.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMonthCmb.setText("Month : ");
        getContentPane().add(lblMonthCmb);
        lblMonthCmb.setBounds(560, 50, 60, 20);

        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setText("From Date : ");
        getContentPane().add(lblDate);
        lblDate.setBounds(570, 30, 110, 20);

        txtFromDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(680, 30, 100, 20);

        TabList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabListMouseClicked(evt);
            }
        });
        TabList.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabListStateChanged(evt);
            }
        });

        DailyAttSummary.setLayout(null);

        jLabel3.setText("Emp No");
        DailyAttSummary.add(jLabel3);
        jLabel3.setBounds(20, 10, 70, 20);

        lblSelectedEmpName.setText("Emp Name");
        DailyAttSummary.add(lblSelectedEmpName);
        lblSelectedEmpName.setBounds(190, 10, 80, 20);

        jLabel6.setText("Department");
        DailyAttSummary.add(jLabel6);
        jLabel6.setBounds(510, 10, 110, 20);

        btnUpdate.setText("UPDATE");
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        DailyAttSummary.add(btnUpdate);
        btnUpdate.setBounds(1000, 90, 130, 23);

        txtSelectedEmpNo_shiftchange.setEditable(false);
        DailyAttSummary.add(txtSelectedEmpNo_shiftchange);
        txtSelectedEmpNo_shiftchange.setBounds(80, 10, 100, 20);

        txtSelectedEmpName_shiftchange.setEditable(false);
        DailyAttSummary.add(txtSelectedEmpName_shiftchange);
        txtSelectedEmpName_shiftchange.setBounds(270, 10, 230, 20);

        txtSelectedDept_shiftchange.setEditable(false);
        DailyAttSummary.add(txtSelectedDept_shiftchange);
        txtSelectedDept_shiftchange.setBounds(600, 10, 150, 20);
        DailyAttSummary.add(txtDay2);
        txtDay2.setBounds(90, 60, 30, 30);
        DailyAttSummary.add(txtDay3);
        txtDay3.setBounds(120, 60, 30, 30);
        DailyAttSummary.add(txtDay4);
        txtDay4.setBounds(150, 60, 30, 30);
        DailyAttSummary.add(txtDay5);
        txtDay5.setBounds(180, 60, 30, 30);
        DailyAttSummary.add(txtDay6);
        txtDay6.setBounds(210, 60, 30, 30);
        DailyAttSummary.add(txtDay7);
        txtDay7.setBounds(240, 60, 30, 30);
        DailyAttSummary.add(txtDay8);
        txtDay8.setBounds(270, 60, 30, 30);
        DailyAttSummary.add(txtDay9);
        txtDay9.setBounds(300, 60, 30, 30);
        DailyAttSummary.add(txtDay10);
        txtDay10.setBounds(330, 60, 30, 30);
        DailyAttSummary.add(txtDay11);
        txtDay11.setBounds(360, 60, 30, 30);

        txtDay12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDay12ActionPerformed(evt);
            }
        });
        DailyAttSummary.add(txtDay12);
        txtDay12.setBounds(390, 60, 30, 30);
        DailyAttSummary.add(txtDay13);
        txtDay13.setBounds(420, 60, 30, 30);
        DailyAttSummary.add(txtDay14);
        txtDay14.setBounds(450, 60, 30, 30);
        DailyAttSummary.add(txtDay15);
        txtDay15.setBounds(480, 60, 30, 30);
        DailyAttSummary.add(txtDay16);
        txtDay16.setBounds(510, 60, 30, 30);
        DailyAttSummary.add(txtDay17);
        txtDay17.setBounds(540, 60, 30, 30);
        DailyAttSummary.add(txtDay18);
        txtDay18.setBounds(570, 60, 30, 30);
        DailyAttSummary.add(txtDay19);
        txtDay19.setBounds(600, 60, 30, 30);
        DailyAttSummary.add(txtDay20);
        txtDay20.setBounds(630, 60, 30, 30);
        DailyAttSummary.add(txtDay21);
        txtDay21.setBounds(660, 60, 30, 30);
        DailyAttSummary.add(txtDay22);
        txtDay22.setBounds(690, 60, 30, 30);
        DailyAttSummary.add(txtDay23);
        txtDay23.setBounds(720, 60, 30, 30);
        DailyAttSummary.add(txtDay24);
        txtDay24.setBounds(750, 60, 30, 30);
        DailyAttSummary.add(txtDay25);
        txtDay25.setBounds(780, 60, 30, 30);
        DailyAttSummary.add(txtDay31);
        txtDay31.setBounds(960, 60, 30, 30);
        DailyAttSummary.add(txtDay26);
        txtDay26.setBounds(810, 60, 30, 30);
        DailyAttSummary.add(txtDay27);
        txtDay27.setBounds(840, 60, 30, 30);
        DailyAttSummary.add(txtDay28);
        txtDay28.setBounds(870, 60, 30, 30);
        DailyAttSummary.add(txtDay29);
        txtDay29.setBounds(900, 60, 30, 30);
        DailyAttSummary.add(txtDay30);
        txtDay30.setBounds(930, 60, 30, 30);

        jLabel5.setText("Date");
        DailyAttSummary.add(jLabel5);
        jLabel5.setBounds(10, 40, 50, 14);

        jLabel7.setText("OT Shift");
        DailyAttSummary.add(jLabel7);
        jLabel7.setBounds(0, 90, 60, 30);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("31");
        DailyAttSummary.add(jLabel8);
        jLabel8.setBounds(960, 40, 20, 14);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("1");
        DailyAttSummary.add(jLabel9);
        jLabel9.setBounds(60, 40, 20, 14);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("2");
        DailyAttSummary.add(jLabel10);
        jLabel10.setBounds(90, 40, 20, 14);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("3");
        DailyAttSummary.add(jLabel11);
        jLabel11.setBounds(120, 40, 20, 14);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("4");
        DailyAttSummary.add(jLabel12);
        jLabel12.setBounds(150, 40, 20, 14);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("5");
        DailyAttSummary.add(jLabel13);
        jLabel13.setBounds(180, 40, 20, 14);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("6");
        DailyAttSummary.add(jLabel14);
        jLabel14.setBounds(210, 40, 20, 14);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("7");
        DailyAttSummary.add(jLabel15);
        jLabel15.setBounds(240, 40, 20, 14);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("8");
        DailyAttSummary.add(jLabel16);
        jLabel16.setBounds(270, 40, 20, 14);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("9");
        DailyAttSummary.add(jLabel17);
        jLabel17.setBounds(300, 40, 20, 14);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("10");
        DailyAttSummary.add(jLabel18);
        jLabel18.setBounds(330, 40, 20, 14);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("11");
        DailyAttSummary.add(jLabel19);
        jLabel19.setBounds(360, 40, 20, 14);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("12");
        DailyAttSummary.add(jLabel20);
        jLabel20.setBounds(390, 40, 20, 14);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("13");
        DailyAttSummary.add(jLabel21);
        jLabel21.setBounds(420, 40, 20, 14);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("14");
        DailyAttSummary.add(jLabel22);
        jLabel22.setBounds(450, 40, 20, 14);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("15");
        DailyAttSummary.add(jLabel23);
        jLabel23.setBounds(480, 40, 20, 14);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("16");
        DailyAttSummary.add(jLabel24);
        jLabel24.setBounds(510, 40, 20, 14);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("17");
        DailyAttSummary.add(jLabel25);
        jLabel25.setBounds(540, 40, 20, 14);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("18");
        DailyAttSummary.add(jLabel26);
        jLabel26.setBounds(570, 40, 20, 14);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("19");
        DailyAttSummary.add(jLabel28);
        jLabel28.setBounds(600, 40, 20, 14);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("20");
        DailyAttSummary.add(jLabel29);
        jLabel29.setBounds(630, 40, 20, 14);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("21");
        DailyAttSummary.add(jLabel30);
        jLabel30.setBounds(660, 40, 20, 14);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("22");
        DailyAttSummary.add(jLabel31);
        jLabel31.setBounds(690, 40, 20, 14);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("23");
        DailyAttSummary.add(jLabel32);
        jLabel32.setBounds(720, 40, 20, 14);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("24");
        DailyAttSummary.add(jLabel33);
        jLabel33.setBounds(750, 40, 20, 14);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("25");
        DailyAttSummary.add(jLabel34);
        jLabel34.setBounds(780, 40, 20, 14);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("26");
        DailyAttSummary.add(jLabel35);
        jLabel35.setBounds(810, 40, 20, 14);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("27");
        DailyAttSummary.add(jLabel36);
        jLabel36.setBounds(840, 40, 20, 14);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("28");
        DailyAttSummary.add(jLabel38);
        jLabel38.setBounds(870, 40, 20, 14);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("29");
        DailyAttSummary.add(jLabel39);
        jLabel39.setBounds(900, 40, 20, 14);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("30");
        DailyAttSummary.add(jLabel40);
        jLabel40.setBounds(930, 40, 20, 14);

        jLabel2.setText("Month - Year");
        DailyAttSummary.add(jLabel2);
        jLabel2.setBounds(770, 10, 90, 14);

        txtSelectedYear_shiftchange.setEditable(false);
        DailyAttSummary.add(txtSelectedYear_shiftchange);
        txtSelectedYear_shiftchange.setBounds(900, 10, 70, 20);

        txtSelectedMonth_shiftchange.setEditable(false);
        DailyAttSummary.add(txtSelectedMonth_shiftchange);
        txtSelectedMonth_shiftchange.setBounds(860, 10, 40, 20);
        DailyAttSummary.add(txtDay1);
        txtDay1.setBounds(60, 60, 30, 30);
        DailyAttSummary.add(txtDayOT1);
        txtDayOT1.setBounds(60, 90, 30, 30);
        DailyAttSummary.add(txtDayOT2);
        txtDayOT2.setBounds(90, 90, 30, 30);
        DailyAttSummary.add(txtDayOT3);
        txtDayOT3.setBounds(120, 90, 30, 30);
        DailyAttSummary.add(txtDayOT4);
        txtDayOT4.setBounds(150, 90, 30, 30);
        DailyAttSummary.add(txtDayOT5);
        txtDayOT5.setBounds(180, 90, 30, 30);
        DailyAttSummary.add(txtDayOT6);
        txtDayOT6.setBounds(210, 90, 30, 30);
        DailyAttSummary.add(txtDayOT8);
        txtDayOT8.setBounds(270, 90, 30, 30);
        DailyAttSummary.add(txtDayOT7);
        txtDayOT7.setBounds(240, 90, 30, 30);
        DailyAttSummary.add(txtDayOT9);
        txtDayOT9.setBounds(300, 90, 30, 30);
        DailyAttSummary.add(txtDayOT10);
        txtDayOT10.setBounds(330, 90, 30, 30);
        DailyAttSummary.add(txtDayOT11);
        txtDayOT11.setBounds(360, 90, 30, 30);

        txtDayOT12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDayOT12ActionPerformed(evt);
            }
        });
        DailyAttSummary.add(txtDayOT12);
        txtDayOT12.setBounds(390, 90, 30, 30);
        DailyAttSummary.add(txtDayOT13);
        txtDayOT13.setBounds(420, 90, 30, 30);
        DailyAttSummary.add(txtDayOT14);
        txtDayOT14.setBounds(450, 90, 30, 30);
        DailyAttSummary.add(txtDayOT16);
        txtDayOT16.setBounds(510, 90, 30, 30);
        DailyAttSummary.add(txtDayOT15);
        txtDayOT15.setBounds(480, 90, 30, 30);
        DailyAttSummary.add(txtDayOT17);
        txtDayOT17.setBounds(540, 90, 30, 30);
        DailyAttSummary.add(txtDayOT18);
        txtDayOT18.setBounds(570, 90, 30, 30);
        DailyAttSummary.add(txtDayOT20);
        txtDayOT20.setBounds(630, 90, 30, 30);
        DailyAttSummary.add(txtDayOT19);
        txtDayOT19.setBounds(600, 90, 30, 30);
        DailyAttSummary.add(txtDayOT21);
        txtDayOT21.setBounds(660, 90, 30, 30);
        DailyAttSummary.add(txtDayOT22);
        txtDayOT22.setBounds(690, 90, 30, 30);
        DailyAttSummary.add(txtDayOT23);
        txtDayOT23.setBounds(720, 90, 30, 30);
        DailyAttSummary.add(txtDayOT24);
        txtDayOT24.setBounds(750, 90, 30, 30);
        DailyAttSummary.add(txtDayOT25);
        txtDayOT25.setBounds(780, 90, 30, 30);
        DailyAttSummary.add(txtDayOT26);
        txtDayOT26.setBounds(810, 90, 30, 30);
        DailyAttSummary.add(txtDayOT27);
        txtDayOT27.setBounds(840, 90, 30, 30);
        DailyAttSummary.add(txtDayOT28);
        txtDayOT28.setBounds(870, 90, 30, 30);
        DailyAttSummary.add(txtDayOT29);
        txtDayOT29.setBounds(900, 90, 30, 30);
        DailyAttSummary.add(txtDayOT30);
        txtDayOT30.setBounds(930, 90, 30, 30);
        DailyAttSummary.add(txtDayOT31);
        txtDayOT31.setBounds(960, 90, 30, 30);

        jLabel41.setText("Shift");
        DailyAttSummary.add(jLabel41);
        jLabel41.setBounds(10, 60, 50, 30);

        TabList.addTab("Shift Change", DailyAttSummary);

        jPanel1.setLayout(null);

        jLabel42.setText("DATE");
        jPanel1.add(jLabel42);
        jLabel42.setBounds(20, 0, 26, 14);

        txtDate_updatepunch.setEditable(false);
        jPanel1.add(txtDate_updatepunch);
        txtDate_updatepunch.setBounds(70, 0, 130, 20);

        jLabel43.setText("Emp No");
        jPanel1.add(jLabel43);
        jLabel43.setBounds(210, 0, 70, 20);

        txtSelectedEmpNo_updatepunch.setEditable(false);
        jPanel1.add(txtSelectedEmpNo_updatepunch);
        txtSelectedEmpNo_updatepunch.setBounds(270, 0, 100, 20);

        lblSelectedEmpName1.setText("Emp Name");
        jPanel1.add(lblSelectedEmpName1);
        lblSelectedEmpName1.setBounds(380, 0, 80, 20);

        txtSelectedEmpName_updatepunch.setEditable(false);
        jPanel1.add(txtSelectedEmpName_updatepunch);
        txtSelectedEmpName_updatepunch.setBounds(460, 0, 230, 20);

        jLabel44.setText("Department");
        jPanel1.add(jLabel44);
        jLabel44.setBounds(700, 0, 110, 20);

        txtSelectedDept_updatepunch.setEditable(false);
        jPanel1.add(txtSelectedDept_updatepunch);
        txtSelectedDept_updatepunch.setBounds(790, 0, 150, 20);

        tbl_Punch_From.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tbl_Punch_From);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(20, 20, 130, 130);

        btnDeletion.setText("DELETION    >>");
        btnDeletion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletionActionPerformed(evt);
            }
        });
        jPanel1.add(btnDeletion);
        btnDeletion.setBounds(170, 50, 150, 20);

        btnShift99.setText("SHIFT TO 99    >>");
        btnShift99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShift99ActionPerformed(evt);
            }
        });
        jPanel1.add(btnShift99);
        btnShift99.setBounds(170, 70, 150, 20);

        btnShift11.setText("SHIFT TO 11    >>");
        btnShift11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShift11ActionPerformed(evt);
            }
        });
        jPanel1.add(btnShift11);
        btnShift11.setBounds(170, 90, 150, 20);

        tbl_To_DELETION.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_To_DELETION);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(360, 20, 110, 130);

        tbl_To_SHIFT_99.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tbl_To_SHIFT_99);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(530, 20, 110, 130);

        tbl_To_SHIFT_11.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tbl_To_SHIFT_11);

        jPanel1.add(jScrollPane4);
        jScrollPane4.setBounds(690, 20, 110, 130);

        jLabel46.setFont(new java.awt.Font("Cantarell", 1, 10)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("<html>D<br>E<br>L<br>E<br>T<br>I<br>O<br>N<br></html>");
        jLabel46.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel1.add(jLabel46);
        jLabel46.setBounds(350, 20, 20, 110);

        jLabel47.setFont(new java.awt.Font("Cantarell", 1, 10)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel47.setText("<html>M<br>A<br>C<br>H<br>I<br>N<br>E<br> <br>1<br>1<br></html>");
        jLabel47.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel1.add(jLabel47);
        jLabel47.setBounds(680, 20, 10, 130);

        jLabel48.setFont(new java.awt.Font("Cantarell", 1, 10)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("<html>M<br>A<br>C<br>H<br>I<br>N<br>E<br> <br>9<br>9<br></html>");
        jLabel48.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel1.add(jLabel48);
        jLabel48.setBounds(520, 20, 10, 130);

        btnUpdatePunch.setText("UPDATE");
        btnUpdatePunch.setEnabled(false);
        btnUpdatePunch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePunchActionPerformed(evt);
            }
        });
        jPanel1.add(btnUpdatePunch);
        btnUpdatePunch.setBounds(830, 80, 150, 23);

        TabList.addTab("Update Punch", jPanel1);

        jPanel4.setLayout(null);

        jLabel53.setText("DATE");
        jPanel4.add(jLabel53);
        jLabel53.setBounds(20, 0, 26, 14);

        txtDate_updatepunch1.setEditable(false);
        jPanel4.add(txtDate_updatepunch1);
        txtDate_updatepunch1.setBounds(70, 0, 130, 20);

        jLabel56.setText("Emp No");
        jPanel4.add(jLabel56);
        jLabel56.setBounds(210, 0, 70, 20);

        txtSelectedEmpNo_updatepunch1.setEditable(false);
        jPanel4.add(txtSelectedEmpNo_updatepunch1);
        txtSelectedEmpNo_updatepunch1.setBounds(270, 0, 100, 20);

        lblSelectedEmpName2.setText("Emp Name");
        jPanel4.add(lblSelectedEmpName2);
        lblSelectedEmpName2.setBounds(380, 0, 80, 20);

        txtSelectedEmpName_updatepunch1.setEditable(false);
        jPanel4.add(txtSelectedEmpName_updatepunch1);
        txtSelectedEmpName_updatepunch1.setBounds(460, 0, 230, 20);

        jLabel57.setText("Department");
        jPanel4.add(jLabel57);
        jLabel57.setBounds(700, 0, 110, 20);

        txtSelectedDept_updatepunch1.setEditable(false);
        jPanel4.add(txtSelectedDept_updatepunch1);
        txtSelectedDept_updatepunch1.setBounds(790, 0, 150, 20);

        tbl_Punch_From1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane7.setViewportView(tbl_Punch_From1);

        jPanel4.add(jScrollPane7);
        jScrollPane7.setBounds(20, 20, 130, 130);

        btnShiftReg.setText("SHIFT TO Reg    >>");
        btnShiftReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShiftRegActionPerformed(evt);
            }
        });
        jPanel4.add(btnShiftReg);
        btnShiftReg.setBounds(170, 70, 150, 20);

        tbl_To_SHIFT_REG.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(tbl_To_SHIFT_REG);

        jPanel4.add(jScrollPane9);
        jScrollPane9.setBounds(540, 20, 110, 130);

        jLabel60.setFont(new java.awt.Font("Cantarell", 1, 10)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel60.setText("<html>R<br>E<br>G<br>U<br>L<br>A<br>R<br></html>");
        jLabel60.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel4.add(jLabel60);
        jLabel60.setBounds(530, 20, 10, 130);

        btnUpdatePunch1.setText("UPDATE");
        btnUpdatePunch1.setEnabled(false);
        btnUpdatePunch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePunch1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnUpdatePunch1);
        btnUpdatePunch1.setBounds(670, 70, 150, 23);

        jLabel58.setFont(new java.awt.Font("Cantarell", 1, 10)); // NOI18N
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel58.setText("<html>D<br>E<br>L<br>E<br>T<br>I<br>O<br>N<br></html>");
        jLabel58.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel4.add(jLabel58);
        jLabel58.setBounds(350, 20, 20, 110);

        tbl_To_DELETION1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane8.setViewportView(tbl_To_DELETION1);

        jPanel4.add(jScrollPane8);
        jScrollPane8.setBounds(360, 20, 110, 130);

        btnDeletion1.setText("DELETION    >>");
        btnDeletion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletion1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnDeletion1);
        btnDeletion1.setBounds(170, 50, 150, 20);

        TabList.addTab("Rokdi  Punch", jPanel4);

        jPanel3.setLayout(null);

        jLabel52.setText("EMP NO");
        jPanel3.add(jLabel52);
        jLabel52.setBounds(30, 10, 80, 30);

        txtRevertDeleted.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRevertDeletedFocusLost(evt);
            }
        });
        txtRevertDeleted.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRevertDeletedKeyPressed(evt);
            }
        });
        jPanel3.add(txtRevertDeleted);
        txtRevertDeleted.setBounds(100, 10, 120, 30);

        jLabel54.setText("Year");
        jPanel3.add(jLabel54);
        jLabel54.setBounds(420, 10, 60, 30);

        txtYearRevertDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtYearRevertDeletedActionPerformed(evt);
            }
        });
        jPanel3.add(txtYearRevertDeleted);
        txtYearRevertDeleted.setBounds(460, 10, 90, 30);

        jLabel55.setText("Month");
        jPanel3.add(jLabel55);
        jLabel55.setBounds(240, 20, 80, 20);

        btnRevertDeleted_ShowData.setText("Show Data");
        btnRevertDeleted_ShowData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevertDeleted_ShowDataActionPerformed(evt);
            }
        });
        jPanel3.add(btnRevertDeleted_ShowData);
        btnRevertDeleted_ShowData.setBounds(560, 10, 110, 23);

        tblRevertDeleted.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(tblRevertDeleted);

        jPanel3.add(jScrollPane6);
        jScrollPane6.setBounds(12, 62, 1320, 320);
        jPanel3.add(lblEmpNameHistory1);
        lblEmpNameHistory1.setBounds(100, 40, 260, 0);

        cmbMonthRevertDeleted.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jPanel3.add(cmbMonthRevertDeleted);
        cmbMonthRevertDeleted.setBounds(300, 20, 54, 20);

        btnRevertSelected.setText("Revert Selected");
        btnRevertSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevertSelectedActionPerformed(evt);
            }
        });
        jPanel3.add(btnRevertSelected);
        btnRevertSelected.setBounds(20, 400, 180, 23);

        TabList.addTab("Deleted Punch Revert", jPanel3);

        jPanel2.setLayout(null);

        jLabel45.setText("EMP NO");
        jPanel2.add(jLabel45);
        jLabel45.setBounds(30, 10, 80, 30);

        txtEmpNoSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmpNoSearchFocusLost(evt);
            }
        });
        txtEmpNoSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNoSearchKeyPressed(evt);
            }
        });
        jPanel2.add(txtEmpNoSearch);
        txtEmpNoSearch.setBounds(100, 10, 120, 30);

        cmbDataUpdate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Shift Change", "Punch Update" }));
        jPanel2.add(cmbDataUpdate);
        cmbDataUpdate.setBounds(300, 10, 150, 30);

        jLabel49.setText("Data");
        jPanel2.add(jLabel49);
        jLabel49.setBounds(249, 10, 40, 30);

        jLabel50.setText("Year");
        jPanel2.add(jLabel50);
        jLabel50.setBounds(510, 30, 60, 30);

        txtYearHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtYearHistoryActionPerformed(evt);
            }
        });
        jPanel2.add(txtYearHistory);
        txtYearHistory.setBounds(570, 30, 90, 30);

        jLabel51.setText("Month");
        jPanel2.add(jLabel51);
        jLabel51.setBounds(510, 10, 80, 20);

        btnHistory.setText("Show Data");
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });
        jPanel2.add(btnHistory);
        btnHistory.setBounds(750, 10, 110, 23);

        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tblHistory);

        jPanel2.add(jScrollPane5);
        jScrollPane5.setBounds(12, 62, 1320, 410);
        jPanel2.add(lblEmpNameHistory);
        lblEmpNameHistory.setBounds(100, 40, 260, 0);

        cmbMonthHistory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jPanel2.add(cmbMonthHistory);
        cmbMonthHistory.setBounds(570, 10, 54, 20);

        TabList.addTab("History", jPanel2);

        getContentPane().add(TabList);
        TabList.setBounds(0, 110, 1350, 180);

        btnClear.setText("Clear All");
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear);
        btnClear.setBounds(740, 60, 110, 30);

        txtEmpNo.setToolTipText("Press F1 key for search Employee No");
        txtEmpNo = new JTextFieldHint(new JTextField(),"Search by F1");
        txtEmpNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmpNoFocusLost(evt);
            }
        });
        txtEmpNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNoKeyPressed(evt);
            }
        });
        getContentPane().add(txtEmpNo);
        txtEmpNo.setBounds(120, 30, 120, 20);

        txtEmpName.setEditable(false);
        txtEmpName.setDisabledTextColor(java.awt.Color.black);
        txtEmpName.setEnabled(false);
        getContentPane().add(txtEmpName);
        txtEmpName.setBounds(250, 30, 280, 20);

        lblYearCmb.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblYearCmb.setText("Year : ");
        getContentPane().add(lblYearCmb);
        lblYearCmb.setBounds(560, 70, 60, 20);

        lblDeptCmb.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDeptCmb.setText("Department : ");
        getContentPane().add(lblDeptCmb);
        lblDeptCmb.setBounds(10, 50, 110, 20);

        cmbDept.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDeptActionPerformed(evt);
            }
        });
        getContentPane().add(cmbDept);
        cmbDept.setBounds(120, 50, 140, 20);

        lblShiftCmb.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShiftCmb.setText("Shift : ");
        getContentPane().add(lblShiftCmb);
        lblShiftCmb.setBounds(10, 70, 110, 20);

        cmbShift.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbShift);
        cmbShift.setBounds(120, 70, 140, 20);
        getContentPane().add(txtYear);
        txtYear.setBounds(620, 70, 80, 20);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Main Category : ");
        getContentPane().add(jLabel37);
        jLabel37.setBounds(270, 50, 120, 20);

        cmbMainCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbMainCategory);
        cmbMainCategory.setBounds(390, 50, 150, 20);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Category : ");
        getContentPane().add(jLabel27);
        jLabel27.setBounds(270, 70, 120, 20);

        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbCategory);
        cmbCategory.setBounds(390, 70, 150, 20);

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
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableMouseClicked(evt);
            }
        });
        jScrollPane25.setViewportView(Table);

        getContentPane().add(jScrollPane25);
        jScrollPane25.setBounds(10, 300, 1350, 320);

        lblDate1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate1.setText("To Date : ");
        getContentPane().add(lblDate1);
        lblDate1.setBounds(780, 30, 90, 20);

        txtToDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();
        getContentPane().add(txtToDate);
        txtToDate.setBounds(870, 30, 100, 20);

        jLabel4.setBackground(new java.awt.Color(0, 102, 153));
        jLabel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4);
        jLabel4.setBounds(0, 96, 1380, 4);

        btnDailyAttView.setText("SHOW DATA");
        btnDailyAttView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyAttViewActionPerformed(evt);
            }
        });
        getContentPane().add(btnDailyAttView);
        btnDailyAttView.setBounds(860, 60, 120, 23);

        cmbMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        getContentPane().add(cmbMonth);
        cmbMonth.setBounds(620, 50, 54, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtEmpName.setText("");
        txtEmpNo.setText("");
        txtFromDate.setText("");
        txtToDate.setText("");
        txtYear.setText("");
        cmbMonth.setSelectedIndex(0);
        cmbDept.setSelectedIndex(0);
        cmbShift.setSelectedIndex(0);
        cmbMainCategory.setSelectedIndex(0);
        cmbCategory.setSelectedIndex(0);
    }//GEN-LAST:event_btnClearActionPerformed


    private void txtEmpNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmpNoFocusLost
        // TODO add your handling code here:
        if (!txtEmpNo.getText().trim().equals("") && data.IsRecordExist("SELECT * FROM SDMLATTPAY.ATTPAY_EMPMST WHERE PAY_EMP_NO='" + txtEmpNo.getText().trim() + "' AND APPROVED=1 AND CANCELED=0")) {
            txtEmpName.setText(data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE PAY_EMP_NO='" + txtEmpNo.getText() + "'"));
        } else {
            if (!txtEmpNo.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Employee No doesn't exist/under approval.");
            }
            txtEmpNo.setText("");
            txtEmpName.setText("");
        }
    }//GEN-LAST:event_txtEmpNoFocusLost

    private void txtEmpNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            ATTPAYLOV aList = new ATTPAYLOV();
            aList.SQL = "SELECT PAY_EMP_NO,EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE APPROVED=1 AND CANCELED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtEmpNo.setText(aList.ReturnVal);
                txtEmpName.setText(data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE PAY_EMP_NO='" + txtEmpNo.getText() + "'"));
            }
        }
    }//GEN-LAST:event_txtEmpNoKeyPressed

    private void cmbDeptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDeptActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbDeptActionPerformed

    private void TabListStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabListStateChanged
        // TODO add your handling code here:
        if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Shift Schedule")) {
            FormatGridShiftSchedule();

            txtEmpNo.setEnabled(true);
            txtFromDate.setEnabled(false);
            txtToDate.setEnabled(false);
            txtYear.setEnabled(true);
            cmbMonth.setEnabled(true);
            cmbDept.setEnabled(true);
            cmbShift.setEnabled(false);
            cmbMainCategory.setEnabled(false);
            cmbCategory.setEnabled(false);

        } else if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Daily Attendance Summary")) {
            FormatGridDailyAtt();

            txtEmpNo.setEnabled(true);
            txtFromDate.setEnabled(true);
            txtToDate.setEnabled(true);
            txtYear.setEnabled(true);
            cmbMonth.setEnabled(true);
            cmbDept.setEnabled(true);
            cmbShift.setEnabled(true);
            cmbMainCategory.setEnabled(true);
            cmbCategory.setEnabled(true);

        }
    }//GEN-LAST:event_TabListStateChanged

    private void btnDailyAttViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyAttViewActionPerformed
        // TODO add your handling code here:
        if (!txtFromDate.getText().trim().equals("") && !txtToDate.getText().trim().equals("")) {
            if (!DateValidate()) {
                return;
            } else {
                GenerateDailyAtt();
            }
        } else if (!cmbMonth.getSelectedItem().equals("Select") && !txtYear.getText().trim().equals("")) {
            if (!MonthYearValidate()) {
                return;
            } else {
                GenerateDailyAtt();
            }
        } else {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Enter From-To Date or Enter Month-Year");
        }
    }//GEN-LAST:event_btnDailyAttViewActionPerformed

    private void txtDay12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDay12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDay12ActionPerformed

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:

        clearFields();

        String EMP_NO = Table.getValueAt(Table.getSelectedRow(), 1).toString();
        String EMP_NAME = Table.getValueAt(Table.getSelectedRow(), 2).toString();
        String DEPARTMENT = Table.getValueAt(Table.getSelectedRow(), 3).toString();
        String PUNCH_DATE = EITLERPGLOBAL.formatDateDB(Table.getValueAt(Table.getSelectedRow(), 4).toString());

        if (TabList.getSelectedIndex() == 0) {
            if (!EMP_NO.equals("") && !PUNCH_DATE.equals("")) {
                int MONTH = EITLERPGLOBAL.getMonth(PUNCH_DATE);
                int YEAR = EITLERPGLOBAL.getYear(PUNCH_DATE);
                txtSelectedMonth_shiftchange.setText(MONTH + "");
                txtSelectedYear_shiftchange.setText(YEAR + "");
                txtSelectedEmpNo_shiftchange.setText(EMP_NO);
                txtSelectedEmpName_shiftchange.setText(EMP_NAME);
                txtSelectedDept_shiftchange.setText(DEPARTMENT);

                try {
                    ResultSet result = data.getResult("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE WHERE SSC_EMPID='" + EMP_NO + "' AND SSC_YEAR='" + YEAR + "' AND SSC_MONTH='" + MONTH + "'");
                    System.out.println("SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE WHERE SSC_EMPID='" + EMP_NO + "' AND SSC_YEAR='" + YEAR + "' AND SSC_MONTH='" + MONTH + "'");
                    result.first();

                    while (!result.isAfterLast()) {
                        txtDay1.setText(result.getString("SSC_1"));
                        txtDay2.setText(result.getString("SSC_2"));
                        txtDay3.setText(result.getString("SSC_3"));
                        txtDay4.setText(result.getString("SSC_4"));
                        txtDay5.setText(result.getString("SSC_5"));
                        txtDay6.setText(result.getString("SSC_6"));
                        txtDay7.setText(result.getString("SSC_7"));
                        txtDay8.setText(result.getString("SSC_8"));
                        txtDay9.setText(result.getString("SSC_9"));
                        txtDay10.setText(result.getString("SSC_10"));
                        txtDay11.setText(result.getString("SSC_11"));
                        txtDay12.setText(result.getString("SSC_12"));
                        txtDay13.setText(result.getString("SSC_13"));
                        txtDay14.setText(result.getString("SSC_14"));
                        txtDay15.setText(result.getString("SSC_15"));
                        txtDay16.setText(result.getString("SSC_16"));
                        txtDay17.setText(result.getString("SSC_17"));
                        txtDay18.setText(result.getString("SSC_18"));
                        txtDay19.setText(result.getString("SSC_19"));
                        txtDay20.setText(result.getString("SSC_20"));
                        txtDay21.setText(result.getString("SSC_21"));
                        txtDay22.setText(result.getString("SSC_22"));
                        txtDay23.setText(result.getString("SSC_23"));
                        txtDay24.setText(result.getString("SSC_24"));
                        txtDay25.setText(result.getString("SSC_25"));
                        txtDay26.setText(result.getString("SSC_26"));
                        txtDay27.setText(result.getString("SSC_27"));
                        txtDay28.setText(result.getString("SSC_28"));
                        txtDay29.setText(result.getString("SSC_29"));
                        txtDay30.setText(result.getString("SSC_30"));
                        txtDay31.setText(result.getString("SSC_31"));

                        if (result.getString("SSC_29").equals("")) {
                            txtDay29.setEditable(false);
                        } else {
                            txtDay29.setEditable(true);
                        }

                        if (result.getString("SSC_30").equals("")) {
                            txtDay30.setEditable(false);
                        } else {
                            txtDay30.setEditable(true);
                        }

                        if (result.getString("SSC_31").equals("")) {
                            txtDay31.setEditable(false);
                        } else {
                            txtDay31.setEditable(true);
                        }

                        result.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    ResultSet result = data.getResult("SELECT * FROM SDMLATTPAY.ATT_OT_SHIFT_SCHEDULE WHERE OTSSC_EMPID='" + EMP_NO + "' AND OTSSC_YEAR='" + YEAR + "' AND OTSSC_MONTH='" + MONTH + "'");
                    System.out.println("SELECT * FROM SDMLATTPAY.ATT_OT_SHIFT_SCHEDULE WHERE OTSSC_EMPID='" + EMP_NO + "' AND OTSSC_YEAR='" + YEAR + "' AND OTSSC_MONTH='" + MONTH + "'");
                    result.first();

                    while (!result.isAfterLast()) {
                        txtDayOT1.setText(result.getString("OTSSC_1"));
                        txtDayOT2.setText(result.getString("OTSSC_2"));
                        txtDayOT3.setText(result.getString("OTSSC_3"));
                        txtDayOT4.setText(result.getString("OTSSC_4"));
                        txtDayOT5.setText(result.getString("OTSSC_5"));
                        txtDayOT6.setText(result.getString("OTSSC_6"));
                        txtDayOT7.setText(result.getString("OTSSC_7"));
                        txtDayOT8.setText(result.getString("OTSSC_8"));
                        txtDayOT9.setText(result.getString("OTSSC_9"));
                        txtDayOT10.setText(result.getString("OTSSC_10"));
                        txtDayOT11.setText(result.getString("OTSSC_11"));
                        txtDayOT12.setText(result.getString("OTSSC_12"));
                        txtDayOT13.setText(result.getString("OTSSC_13"));
                        txtDayOT14.setText(result.getString("OTSSC_14"));
                        txtDayOT15.setText(result.getString("OTSSC_15"));
                        txtDayOT16.setText(result.getString("OTSSC_16"));
                        txtDayOT17.setText(result.getString("OTSSC_17"));
                        txtDayOT18.setText(result.getString("OTSSC_18"));
                        txtDayOT19.setText(result.getString("OTSSC_19"));
                        txtDayOT20.setText(result.getString("OTSSC_20"));
                        txtDayOT21.setText(result.getString("OTSSC_21"));
                        txtDayOT22.setText(result.getString("OTSSC_22"));
                        txtDayOT23.setText(result.getString("OTSSC_23"));
                        txtDayOT24.setText(result.getString("OTSSC_24"));
                        txtDayOT25.setText(result.getString("OTSSC_25"));
                        txtDayOT26.setText(result.getString("OTSSC_26"));
                        txtDayOT27.setText(result.getString("OTSSC_27"));
                        txtDayOT28.setText(result.getString("OTSSC_28"));
                        txtDayOT29.setText(result.getString("OTSSC_29"));
                        txtDayOT30.setText(result.getString("OTSSC_30"));
                        txtDayOT31.setText(result.getString("OTSSC_31"));

                        if (result.getString("OTSSC_29").equals("")) {
                            txtDayOT29.setEditable(false);
                        } else {
                            txtDayOT29.setEditable(true);
                        }

                        if (result.getString("OTSSC_30").equals("")) {
                            txtDayOT30.setEditable(false);
                        } else {
                            txtDayOT30.setEditable(true);
                        }

                        if (result.getString("OTSSC_31").equals("")) {
                            txtDayOT31.setEditable(false);
                        } else {
                            txtDayOT31.setEditable(true);
                        }

                        result.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else if (TabList.getSelectedIndex() == 1) {

            if (!EMP_NO.equals("") && !PUNCH_DATE.equals("")) {
                int MONTH = EITLERPGLOBAL.getMonth(PUNCH_DATE);
                int YEAR = EITLERPGLOBAL.getYear(PUNCH_DATE);
                //txtSelectedMonth_shiftchange.setText(MONTH+"");
                //txtSelectedYear_shiftchange.setText(YEAR+"");
                txtSelectedEmpNo_updatepunch.setText(EMP_NO);
                txtSelectedEmpName_updatepunch.setText(EMP_NAME);
                txtSelectedDept_updatepunch.setText(DEPARTMENT);
                txtDate_updatepunch.setText(PUNCH_DATE);
                try {
                    btnDeletion.setEnabled(false);
                    ResultSet result = data.getResult("SELECT DISTINCT P_TIME FROM SDMLATTPAY.ATT_DATA WHERE EMP_CODE='" + EMP_NO + "' AND PUNCH_DATE='" + PUNCH_DATE + "' AND UPDATE_IND NOT IN ('DELETION') AND MACHINE NOT IN (11,99,98)");
                    System.out.println("SELECT DISTINCT P_TIME FROM SDMLATTPAY.ATT_DATA WHERE EMP_CODE='" + EMP_NO + "' AND PUNCH_DATE='" + PUNCH_DATE + "' AND UPDATE_IND NOT IN ('DELETION') AND MACHINE NOT IN (11,99,98)");
                    result.first();

                    int cnt = 0;

                    while (!result.isAfterLast()) {
                        Object[] rowData = new Object[1];
                        rowData[0] = result.getString("P_TIME");
                        DataModel_Punch_From.addRow(rowData);
                        cnt++;
                        result.next();
                    }

                    if (cnt % 2 == 0) {
                        btnDeletion.setEnabled(false);
                    } else {
                        btnDeletion.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (TabList.getSelectedIndex() == 2) {

            if (!EMP_NO.equals("") && !PUNCH_DATE.equals("")) {
                int MONTH = EITLERPGLOBAL.getMonth(PUNCH_DATE);
                int YEAR = EITLERPGLOBAL.getYear(PUNCH_DATE);
                //txtSelectedMonth_shiftchange.setText(MONTH+"");
                //txtSelectedYear_shiftchange.setText(YEAR+"");
                txtSelectedEmpNo_updatepunch1.setText(EMP_NO);
                txtSelectedEmpName_updatepunch1.setText(EMP_NAME);
                txtSelectedDept_updatepunch1.setText(DEPARTMENT);
                txtDate_updatepunch1.setText(PUNCH_DATE);
                try {

                    ResultSet result = data.getResult("SELECT DISTINCT P_TIME FROM SDMLATTPAY.ATT_DATA WHERE MACHINE='11' AND EMP_CODE='" + EMP_NO + "' AND PUNCH_DATE='" + PUNCH_DATE + "' AND UPDATE_IND NOT IN ('DELETION')  ORDER BY A_DATETIME");
                    System.out.println("SELECT DISTINCT P_TIME FROM SDMLATTPAY.ATT_DATA WHERE MACHINE='11' AND EMP_CODE='" + EMP_NO + "' AND PUNCH_DATE='" + PUNCH_DATE + "' AND UPDATE_IND NOT IN ('DELETION')  ORDER BY A_DATETIME");
                    result.first();

                    int cnt = 0;

                    while (!result.isAfterLast()) {
                        Object[] rowData = new Object[1];
                        rowData[0] = result.getString("P_TIME");
                        DataModel_Punch_From1.addRow(rowData);
                        cnt++;
                        result.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }//GEN-LAST:event_TableMouseClicked
    private void clearFields() {
        try {
            txtSelectedEmpNo_updatepunch.setText("");
            txtSelectedEmpName_updatepunch.setText("");
            txtSelectedDept_updatepunch.setText("");
            txtDate_updatepunch.setText("");
            txtSelectedMonth_shiftchange.setText("");
            txtSelectedYear_shiftchange.setText("");
            txtSelectedEmpNo_shiftchange.setText("");
            txtSelectedEmpName_shiftchange.setText("");
            txtSelectedDept_shiftchange.setText("");
            for (int i = 1; i <= 31; i++) {
                Object obj = map.get("txtDay" + i);
                JTextField txtObj = (JTextField) obj;
                txtObj.setText("");
                Object obj1 = map.get("txtDayOT" + i);
                JTextField txtObj1 = (JTextField) obj1;
                txtObj1.setText("");
            }
            while (DataModel_Punch_From.getRowCount() > 0) {
                DataModel_Punch_From.removeRow(0);
            }
            while (DataModel_Punch_From1.getRowCount() > 0) {
                DataModel_Punch_From1.removeRow(0);
            }
            while (DataModel_To_DELETION.getRowCount() > 0) {
                DataModel_To_DELETION.removeRow(0);
            }
            while (DataModel_To_DELETION1.getRowCount() > 0) {
                DataModel_To_DELETION1.removeRow(0);
            }
            while (DataModel_To_SHIFT_11.getRowCount() > 0) {
                DataModel_To_SHIFT_11.removeRow(0);
            }
            while (DataModel_To_SHIFT_99.getRowCount() > 0) {
                DataModel_To_SHIFT_99.removeRow(0);
            }
            while (DataModel_To_SHIFT_REG.getRowCount() > 0) {
                DataModel_To_SHIFT_REG.removeRow(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:

        validation();

        for (int i = 1; i <= 31; i++) {
            Object obj = map.get("txtDay" + i);
            JTextField txtObj = (JTextField) obj;
            if (!txtObj.getText().equals("")) {
                String getShift = data.getStringValueFromDB("SELECT SHIFT_ID FROM SDMLATTPAY.ATT_SHIFT WHERE SHIFT_ID='" + txtObj.getText() + "'");
                if (getShift.equals("")) {
                    JOptionPane.showMessageDialog(this, "Shift " + txtObj.getText() + " is invalid!");
                    txtObj.requestFocus();
                    return;
                }
            }
        }

        String update = "";
        String EMP_NO = Table.getValueAt(Table.getSelectedRow(), 1).toString();
        String EMP_NAME = Table.getValueAt(Table.getSelectedRow(), 2).toString();
        String DEPARTMENT = Table.getValueAt(Table.getSelectedRow(), 3).toString();
        String PUNCH_DATE = EITLERPGLOBAL.formatDateDB(Table.getValueAt(Table.getSelectedRow(), 4).toString());
        int MONTH = EITLERPGLOBAL.getMonth(PUNCH_DATE);
        int YEAR = EITLERPGLOBAL.getYear(PUNCH_DATE);
        for (int i = 1; i <= 31; i++) {
            Object obj = map.get("txtDay" + i);
            JTextField txtObj = (JTextField) obj;
            if (i != 31) {
                update = update + "SSC_" + i + "='" + txtObj.getText() + "',";
            } else {
                update = update + "SSC_" + i + "='" + txtObj.getText() + "'";
            }
        }
        String update_query = "UPDATE SDMLATTPAY.ATT_SHIFT_SCHEDULE SET " + update + " WHERE SSC_EMPID='" + EMP_NO + "' AND SSC_YEAR='" + YEAR + "' AND SSC_MONTH='" + MONTH + "'";
        System.out.println("Update : " + update_query);
        try {
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

            String backup_query = "INSERT INTO SDMLATTPAY.ATT_SHIFT_SCHEDULE_HISTORY ( "
                    + "SSC_EMPID, SSC_YEAR, SSC_MONTH, SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31, SSC_key1, UPDATE_STATUS, USER_ID, FROM_IP "
                    + ") (SELECT  SSC_EMPID, SSC_YEAR, SSC_MONTH, SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31, SSC_key1,'BEFORE UPDATE','" + EITLERPGLOBAL.gUserID + "', '" + str_split[1] + "' "
                    + "FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE WHERE SSC_EMPID='" + EMP_NO + "' AND SSC_YEAR='" + YEAR + "' AND SSC_MONTH='" + MONTH + "')";
            data.Execute(backup_query);

            data.Execute(update_query);

            backup_query = "INSERT INTO SDMLATTPAY.ATT_SHIFT_SCHEDULE_HISTORY ( "
                    + "SSC_EMPID, SSC_YEAR, SSC_MONTH, SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31, SSC_key1, UPDATE_STATUS, USER_ID, FROM_IP "
                    + ") (SELECT  SSC_EMPID, SSC_YEAR, SSC_MONTH, SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31, SSC_key1,'AFTER UPDATE','" + EITLERPGLOBAL.gUserID + "', '" + str_split[1] + "' "
                    + "FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE WHERE SSC_EMPID='" + EMP_NO + "' AND SSC_YEAR='" + YEAR + "' AND SSC_MONTH='" + MONTH + "')";
            data.Execute(backup_query);
            String OT_update = "";
            for (int i = 1; i <= 31; i++) {
                Object obj = map.get("txtDayOT" + i);
                JTextField txtObj = (JTextField) obj;
                if (i != 31) {
                    OT_update = OT_update + "OTSSC_" + i + "='" + txtObj.getText() + "',";
                } else {
                    OT_update = OT_update + "OTSSC_" + i + "='" + txtObj.getText() + "'";
                }
            }
            String update_queryOT = "UPDATE SDMLATTPAY.ATT_OT_SHIFT_SCHEDULE SET " + OT_update + " WHERE OTSSC_EMPID='" + EMP_NO + "' AND OTSSC_YEAR='" + YEAR + "' AND OTSSC_MONTH='" + MONTH + "'";
            System.out.println("Update : " + update_queryOT);

            String backup_queryOT = "INSERT INTO SDMLATTPAY.ATT_SHIFT_SCHEDULE_HISTORY_OT ( "
                    + "OTSSC_EMPID, OTSSC_YEAR, OTSSC_MONTH, OTSSC_1, OTSSC_2, OTSSC_3, OTSSC_4, OTSSC_5, OTSSC_6, OTSSC_7, OTSSC_8, OTSSC_9, OTSSC_10, OTSSC_11, OTSSC_12, OTSSC_13, OTSSC_14, OTSSC_15, OTSSC_16, OTSSC_17, OTSSC_18, OTSSC_19, OTSSC_20, OTSSC_21, OTSSC_22, OTSSC_23, OTSSC_24, OTSSC_25, OTSSC_26, OTSSC_27, OTSSC_28, OTSSC_29, OTSSC_30, OTSSC_31, OTSSC_key1, UPDATE_STATUS, USER_ID, FROM_IP "
                    + ") ("
                    + "SELECT  OTSSC_EMPID, OTSSC_YEAR, OTSSC_MONTH, OTSSC_1, OTSSC_2, OTSSC_3, OTSSC_4, OTSSC_5, OTSSC_6, OTSSC_7, OTSSC_8, OTSSC_9, OTSSC_10, OTSSC_11, OTSSC_12, OTSSC_13, OTSSC_14, OTSSC_15, OTSSC_16, OTSSC_17, OTSSC_18, OTSSC_19, OTSSC_20, OTSSC_21, OTSSC_22, OTSSC_23, OTSSC_24, OTSSC_25, OTSSC_26, OTSSC_27, OTSSC_28, OTSSC_29, OTSSC_30, OTSSC_31, OTSSC_key1,'BEFORE UPDATE','" + EITLERPGLOBAL.gUserID + "', '" + str_split[1] + "' "
                    + "FROM SDMLATTPAY.ATT_OT_SHIFT_SCHEDULE WHERE OTSSC_EMPID='" + EMP_NO + "' AND OTSSC_YEAR='" + YEAR + "' AND OTSSC_MONTH='" + MONTH + "')";
            data.Execute(backup_queryOT);

            data.Execute(update_queryOT);

            backup_queryOT = "INSERT INTO SDMLATTPAY.ATT_SHIFT_SCHEDULE_HISTORY_OT ( "
                    + "OTSSC_EMPID, OTSSC_YEAR, OTSSC_MONTH, OTSSC_1, OTSSC_2, OTSSC_3, OTSSC_4, OTSSC_5, OTSSC_6, OTSSC_7, OTSSC_8, OTSSC_9, OTSSC_10, OTSSC_11, OTSSC_12, OTSSC_13, OTSSC_14, OTSSC_15, OTSSC_16, OTSSC_17, OTSSC_18, OTSSC_19, OTSSC_20, OTSSC_21, OTSSC_22, OTSSC_23, OTSSC_24, OTSSC_25, OTSSC_26, OTSSC_27, OTSSC_28, OTSSC_29, OTSSC_30, OTSSC_31, OTSSC_key1, UPDATE_STATUS, USER_ID, FROM_IP "
                    + ") ("
                    + "SELECT  OTSSC_EMPID, OTSSC_YEAR, OTSSC_MONTH, OTSSC_1, OTSSC_2, OTSSC_3, OTSSC_4, OTSSC_5, OTSSC_6, OTSSC_7, OTSSC_8, OTSSC_9, OTSSC_10, OTSSC_11, OTSSC_12, OTSSC_13, OTSSC_14, OTSSC_15, OTSSC_16, OTSSC_17, OTSSC_18, OTSSC_19, OTSSC_20, OTSSC_21, OTSSC_22, OTSSC_23, OTSSC_24, OTSSC_25, OTSSC_26, OTSSC_27, OTSSC_28, OTSSC_29, OTSSC_30, OTSSC_31, OTSSC_key1,'AFTER UPDATE','" + EITLERPGLOBAL.gUserID + "', '" + str_split[1] + "' "
                    + "FROM SDMLATTPAY.ATT_OT_SHIFT_SCHEDULE WHERE OTSSC_EMPID='" + EMP_NO + "' AND OTSSC_YEAR='" + YEAR + "' AND OTSSC_MONTH='" + MONTH + "')";
            data.Execute(backup_queryOT);

            JOptionPane.showMessageDialog(this, "Record updated...!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void map_initilization() {
        map.put("txtDay1", txtDay1);
        map.put("txtDay2", txtDay2);
        map.put("txtDay3", txtDay3);
        map.put("txtDay4", txtDay4);
        map.put("txtDay5", txtDay5);
        map.put("txtDay6", txtDay6);
        map.put("txtDay7", txtDay7);
        map.put("txtDay8", txtDay8);
        map.put("txtDay9", txtDay9);
        map.put("txtDay10", txtDay10);
        map.put("txtDay11", txtDay11);
        map.put("txtDay12", txtDay12);
        map.put("txtDay13", txtDay13);
        map.put("txtDay14", txtDay14);
        map.put("txtDay15", txtDay15);
        map.put("txtDay16", txtDay16);
        map.put("txtDay17", txtDay17);
        map.put("txtDay18", txtDay18);
        map.put("txtDay19", txtDay19);
        map.put("txtDay20", txtDay20);
        map.put("txtDay21", txtDay21);
        map.put("txtDay22", txtDay22);
        map.put("txtDay23", txtDay23);
        map.put("txtDay24", txtDay24);
        map.put("txtDay25", txtDay25);
        map.put("txtDay26", txtDay26);
        map.put("txtDay27", txtDay27);
        map.put("txtDay28", txtDay28);
        map.put("txtDay29", txtDay29);
        map.put("txtDay30", txtDay30);
        map.put("txtDay31", txtDay31);

        map.put("txtDayOT1", txtDayOT1);
        map.put("txtDayOT2", txtDayOT2);
        map.put("txtDayOT3", txtDayOT3);
        map.put("txtDayOT4", txtDayOT4);
        map.put("txtDayOT5", txtDayOT5);
        map.put("txtDayOT6", txtDayOT6);
        map.put("txtDayOT7", txtDayOT7);
        map.put("txtDayOT8", txtDayOT8);
        map.put("txtDayOT9", txtDayOT9);
        map.put("txtDayOT10", txtDayOT10);
        map.put("txtDayOT11", txtDayOT11);
        map.put("txtDayOT12", txtDayOT12);
        map.put("txtDayOT13", txtDayOT13);
        map.put("txtDayOT14", txtDayOT14);
        map.put("txtDayOT15", txtDayOT15);
        map.put("txtDayOT16", txtDayOT16);
        map.put("txtDayOT17", txtDayOT17);
        map.put("txtDayOT18", txtDayOT18);
        map.put("txtDayOT19", txtDayOT19);
        map.put("txtDayOT20", txtDayOT20);
        map.put("txtDayOT21", txtDayOT21);
        map.put("txtDayOT22", txtDayOT22);
        map.put("txtDayOT23", txtDayOT23);
        map.put("txtDayOT24", txtDayOT24);
        map.put("txtDayOT25", txtDayOT25);
        map.put("txtDayOT26", txtDayOT26);
        map.put("txtDayOT27", txtDayOT27);
        map.put("txtDayOT28", txtDayOT28);
        map.put("txtDayOT29", txtDayOT29);
        map.put("txtDayOT30", txtDayOT30);
        map.put("txtDayOT31", txtDayOT31);
    }

    private boolean validation() {
//        for(Object obj : map.entrySet())
//        {
//            JTextField txtObj = (JTextField) obj;
//            
//            if(txtObj.isEnabled())
//            {
//                if(txtObj.getText().equals(""))
//                {
//                    JOptionPane.showMessageDialog(this, "For Day "+txtObj.getName()+" can not blank.");
//                    return false;
//                }
//            }
//        }

        return true;
    }

    private void txtDayOT12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDayOT12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDayOT12ActionPerformed

    private void btnDeletionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletionActionPerformed
        // TODO add your handling code here:

        try {

            if (tbl_Punch_From.getSelectedRowCount() != 0) {
                int rows[] = tbl_Punch_From.getSelectedRows();
                for (int i = (rows.length - 1); i >= 0; i--) {
                    String Piece_No = tbl_Punch_From.getValueAt(rows[i], 0).toString();

                    Object[] rowData = new Object[2];
                    rowData[0] = Piece_No;
                    DataModel_To_DELETION.addRow(rowData);

                    DataModel_Punch_From.removeRow(rows[i]);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select PUNCH");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDeletionActionPerformed

    private void btnShift99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShift99ActionPerformed
        // TODO add your handling code here:

        try {

            if (tbl_Punch_From.getSelectedRowCount() != 0) {
                int rows[] = tbl_Punch_From.getSelectedRows();
                for (int i = (rows.length - 1); i >= 0; i--) {
                    String Piece_No = tbl_Punch_From.getValueAt(rows[i], 0).toString();

                    Object[] rowData = new Object[2];
                    rowData[0] = Piece_No;
                    DataModel_To_SHIFT_99.addRow(rowData);

                    DataModel_Punch_From.removeRow(rows[i]);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select PUNCH");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnShift99ActionPerformed

    private void btnShift11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShift11ActionPerformed
        // TODO add your handling code here:
        try {

            if (tbl_Punch_From.getSelectedRowCount() != 0) {
                int rows[] = tbl_Punch_From.getSelectedRows();
                for (int i = (rows.length - 1); i >= 0; i--) {
                    String Piece_No = tbl_Punch_From.getValueAt(rows[i], 0).toString();

                    Object[] rowData = new Object[2];
                    rowData[0] = Piece_No;
                    DataModel_To_SHIFT_11.addRow(rowData);

                    DataModel_Punch_From.removeRow(rows[i]);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select PUNCH");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnShift11ActionPerformed

    private void btnUpdatePunchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePunchActionPerformed
        // TODO add your handling code here:

        String Date = txtDate_updatepunch.getText();
        String SelectedEmpNo_updatepunch = txtSelectedEmpNo_updatepunch.getText();

        for (int i = 0; i < DataModel_To_DELETION.getRowCount(); i++) {
            String Punch = DataModel_To_DELETION.getValueAt(i, 0).toString();
            try {
                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'BEFORE UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "' AND MACHINE NOT IN (11,99,98)");
                data.Execute("UPDATE SDMLATTPAY.ATT_DATA  SET UPDATE_IND='DELETION' WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "' AND MACHINE NOT IN (11,99,98)");
                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'AFTER UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "' AND MACHINE NOT IN (11,99,98)");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        for (int i = 0; i < DataModel_To_SHIFT_99.getRowCount(); i++) {
            String Punch = DataModel_To_SHIFT_99.getValueAt(i, 0).toString();
            try {
                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'BEFORE UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");
                data.Execute("UPDATE SDMLATTPAY.ATT_DATA  SET MACHINE='99' WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");
                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'AFTER UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < DataModel_To_SHIFT_11.getRowCount(); i++) {
            String Punch = DataModel_To_SHIFT_11.getValueAt(i, 0).toString();
            try {
                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'BEFORE UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");
                data.Execute("UPDATE SDMLATTPAY.ATT_DATA  SET MACHINE='11' WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");
                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'AFTER UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        JOptionPane.showMessageDialog(this, "Record updated...!");
        clearUpdateData();


    }//GEN-LAST:event_btnUpdatePunchActionPerformed
    private void clearUpdateData() {
        FormatGrid_PunchUpdate();
    }
    private void TabListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabListMouseClicked
        // TODO add your handling code here:
        if (TabList.getSelectedIndex() == 3 || TabList.getSelectedIndex() == 4) {
            TabList.setSize(1360, 500);
        } else {
            TabList.setSize(1360, 190);
        }
    }//GEN-LAST:event_TabListMouseClicked

    private void txtEmpNoSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNoSearchKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            ATTPAYLOV aList = new ATTPAYLOV();
            aList.SQL = "SELECT PAY_EMP_NO,EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE APPROVED=1 AND CANCELED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtEmpNoSearch.setText(aList.ReturnVal);
                lblEmpNameHistory.setText(data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE PAY_EMP_NO='" + txtEmpNoSearch.getText() + "'"));
            }
        }
    }//GEN-LAST:event_txtEmpNoSearchKeyPressed

    private void txtEmpNoSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmpNoSearchFocusLost
        // TODO add your handling code here:
        lblEmpNameHistory.setText(data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE PAY_EMP_NO='" + txtEmpNoSearch.getText() + "'"));
    }//GEN-LAST:event_txtEmpNoSearchFocusLost

    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        // TODO add your handling code here:
        try {
            if (txtEmpNoSearch.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter employee code");
                txtEmpNoSearch.requestFocus();
                return;
            }
            if (cmbDataUpdate.getSelectedIndex() == 0) {
                ResultSet rsData = data.getResult("SELECT SSC_EMPID, SSC_YEAR, SSC_MONTH, UPDATE_STATUS, USER_ID, DATE_TIME, FROM_IP, SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31 "
                        + " FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE_HISTORY where SSC_EMPID='" + txtEmpNoSearch.getText() + "' AND SSC_YEAR ='" + txtYearHistory.getText() + "' AND SSC_MONTH='" + cmbMonthHistory.getSelectedItem() + "'  ");
                System.out.println("SELECT SSC_EMPID, SSC_YEAR, SSC_MONTH, UPDATE_STATUS, USER_ID, DATE_TIME, FROM_IP, SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31 "
                        + " FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE_HISTORY where SSC_EMPID='" + txtEmpNoSearch.getText() + "' AND SSC_YEAR ='" + txtYearHistory.getText() + "' AND SSC_MONTH='" + cmbMonthHistory.getSelectedItem() + "'  ");
                DataModel_History = new EITLTableModel();
                tblHistory.removeAll();

                tblHistory.setModel(DataModel_History);
                tblHistory.setAutoResizeMode(0);
                // , , , , , SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31        
                DataModel_History.addColumn("Sr.");
                DataModel_History.addColumn("SSC_EMPID");
                DataModel_History.addColumn("SSC_YEAR");
                DataModel_History.addColumn("SSC_MONTH");
                DataModel_History.addColumn("UPDATE_STATUS");
                DataModel_History.addColumn("USER_ID");
                DataModel_History.addColumn("DATE_TIME");
                DataModel_History.addColumn("FROM_IP");
                for (int i = 1; i <= 31; i++) {
                    DataModel_History.addColumn("DATE " + i);
                }

                tblHistory.getColumnModel().getColumn(0).setMinWidth(50);
                tblHistory.getColumnModel().getColumn(1).setMinWidth(100);
                tblHistory.getColumnModel().getColumn(2).setMinWidth(80);
                tblHistory.getColumnModel().getColumn(3).setMinWidth(80);
                tblHistory.getColumnModel().getColumn(4).setMinWidth(150);
                tblHistory.getColumnModel().getColumn(5).setMinWidth(150);
                tblHistory.getColumnModel().getColumn(6).setMinWidth(150);
                tblHistory.getColumnModel().getColumn(7).setMinWidth(100);

                rsData.first();
                int cnt = 0;
                while (!rsData.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    cnt++;
                    rowData[0] = cnt;
                    rowData[1] = rsData.getString(1);
                    rowData[2] = rsData.getString(2);
                    rowData[3] = rsData.getString(3);
                    rowData[4] = rsData.getString(4);
                    rowData[5] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(rsData.getString(5)));
                    rowData[6] = rsData.getString(6);
                    rowData[7] = rsData.getString(7);

                    for (int i = 8; i < 8 + 31; i++) {
                        rowData[i] = rsData.getString(i);
                    }

                    DataModel_History.addRow(rowData);
                    rsData.next();
                }

            } else if (cmbDataUpdate.getSelectedIndex() == 1) {

                ResultSet rsData = data.getResult("SELECT * "
                        + " FROM SDMLATTPAY.ATT_DATA_UPDATE_HISTORY where EMP_CODE='" + txtEmpNoSearch.getText() + "' AND PUNCH_DATE  like '" + txtYearHistory.getText() + "-" + cmbMonthHistory.getSelectedItem() + "%'");
                System.out.println("SELECT * "
                        + " FROM SDMLATTPAY.ATT_DATA_UPDATE_HISTORY where EMP_CODE='" + txtEmpNoSearch.getText() + "' AND PUNCH_DATE like '" + txtYearHistory.getText() + "-" + cmbMonthHistory.getSelectedItem() + "%");
                DataModel_History = new EITLTableModel();
                tblHistory.removeAll();

                tblHistory.setModel(DataModel_History);
                tblHistory.setAutoResizeMode(0);
                // , , , , , SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31        
                DataModel_History.addColumn("Sr.");
                DataModel_History.addColumn("EMP_CODE");
                DataModel_History.addColumn("PUNCH_DATE");
                DataModel_History.addColumn("P_TIME");
                DataModel_History.addColumn("UPDATE_IND");
                DataModel_History.addColumn("MACHINE");
                DataModel_History.addColumn("UPDATE_STATUS");
                DataModel_History.addColumn("USER_ID");
                DataModel_History.addColumn("DATE_TIME");

                tblHistory.getColumnModel().getColumn(0).setMinWidth(50);
                tblHistory.getColumnModel().getColumn(1).setMinWidth(100);
                tblHistory.getColumnModel().getColumn(2).setMinWidth(80);
                tblHistory.getColumnModel().getColumn(3).setMinWidth(80);
                tblHistory.getColumnModel().getColumn(4).setMinWidth(150);
                tblHistory.getColumnModel().getColumn(5).setMinWidth(150);
                tblHistory.getColumnModel().getColumn(6).setMinWidth(150);
                tblHistory.getColumnModel().getColumn(7).setMinWidth(150);
                tblHistory.getColumnModel().getColumn(8).setMinWidth(150);

                rsData.first();
                int cnt = 0;
                while (!rsData.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    cnt++;
                    rowData[0] = cnt;
                    rowData[1] = rsData.getString(2);
                    rowData[2] = rsData.getString(3);
                    rowData[3] = rsData.getString(4);
                    rowData[4] = rsData.getString(5);
                    rowData[5] = rsData.getString(6);
                    rowData[6] = rsData.getString(7);
                    rowData[7] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(rsData.getString(8)));
                    rowData[8] = rsData.getString(9);
                    DataModel_History.addRow(rowData);
                    rsData.next();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnHistoryActionPerformed

    private void txtYearHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtYearHistoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtYearHistoryActionPerformed

    private void txtRevertDeletedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRevertDeletedFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRevertDeletedFocusLost

    private void txtRevertDeletedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRevertDeletedKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRevertDeletedKeyPressed

    private void txtYearRevertDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtYearRevertDeletedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtYearRevertDeletedActionPerformed

    private void btnRevertDeleted_ShowDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevertDeleted_ShowDataActionPerformed
        // TODO add your handling code here:
        try {

            DataModel_Deleted_Revert = new EITLTableModel();
            tblRevertDeleted.removeAll();

            tblRevertDeleted.setModel(DataModel_Deleted_Revert);
            tblRevertDeleted.setAutoResizeMode(0);
            // , , , , , SSC_1, SSC_2, SSC_3, SSC_4, SSC_5, SSC_6, SSC_7, SSC_8, SSC_9, SSC_10, SSC_11, SSC_12, SSC_13, SSC_14, SSC_15, SSC_16, SSC_17, SSC_18, SSC_19, SSC_20, SSC_21, SSC_22, SSC_23, SSC_24, SSC_25, SSC_26, SSC_27, SSC_28, SSC_29, SSC_30, SSC_31        
            DataModel_Deleted_Revert.addColumn("Sr.");
            DataModel_Deleted_Revert.addColumn("EMP_CODE");
            DataModel_Deleted_Revert.addColumn("P_TIME");
            DataModel_Deleted_Revert.addColumn("MACHINE");
            DataModel_Deleted_Revert.addColumn("PUNCH_DATE");
            DataModel_Deleted_Revert.addColumn("UPDATE_IND");

            ResultSet rsData = data.getResult("SELECT EMP_CODE,P_TIME,MACHINE,PUNCH_DATE,UPDATE_IND from SDMLATTPAY.ATT_DATA where UPDATE_IND='DELETION' AND EMP_CODE='" + txtRevertDeleted.getText() + "' AND "
                    + "PUNCH_DATE LIKE '" + txtYearRevertDeleted.getText() + "-" + cmbMonthRevertDeleted.getSelectedItem() + "%' ");
            System.out.println("SELECT EMP_CODE,P_TIME,MACHINE,PUNCH_DATE,UPDATE_IND from SDMLATTPAY.ATT_DATA where UPDATE_IND='DELETION' AND EMP_CODE='" + txtEmpNoSearch.getText() + "' AND "
                    + "PUNCH_DATE LIKE '" + txtYearRevertDeleted.getText() + "-" + cmbMonthRevertDeleted.getSelectedItem() + "%' ");
            rsData.first();
            int cnt = 0;
            while (!rsData.isAfterLast()) {
                Object[] rowData = new Object[100];
                cnt++;
                rowData[0] = cnt;
                rowData[1] = rsData.getString(1);
                rowData[2] = rsData.getString(2);
                rowData[3] = rsData.getString(3);
                rowData[4] = rsData.getString(4);
                rowData[5] = rsData.getString(5);
                DataModel_Deleted_Revert.addRow(rowData);
                rsData.next();
            }

            /*
             SELECT * from SDMLATTPAY.ATT_DATA where UPDATE_IND='DELETION' AND EMP_CODE='BRD005048' AND 
             PUNCH_DATE LIKE '2019-07%'
             */
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnRevertDeleted_ShowDataActionPerformed

    private void btnRevertSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevertSelectedActionPerformed
        // TODO add your handling code here:

        try {

            int[] count = tblRevertDeleted.getSelectedRows();

            for (int i = 0; i < count.length; i++) {

                String EMPNO = tblRevertDeleted.getValueAt(count[i], 1).toString();
                String P_TIME = tblRevertDeleted.getValueAt(count[i], 2).toString();
                //PUNCH_DATE LIKE '' 
                //JOptionPane.show
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure? You want to revert deleted Punch for EMP " + EMPNO + ", Punch Time " + P_TIME, "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == 0) {
                    System.out.println("UPDATE SDMLATTPAY.ATT_DATA  SET UPDATE_IND='' WHERE EMP_CODE='" + EMPNO + "' AND P_TIME='" + P_TIME + "' AND PUNCH_DATE like '" + txtYearRevertDeleted.getText() + "-" + cmbMonthRevertDeleted.getSelectedItem() + "%'");
                    data.Execute("UPDATE SDMLATTPAY.ATT_DATA  SET UPDATE_IND='' WHERE EMP_CODE='" + EMPNO + "' AND P_TIME='" + P_TIME + "' AND PUNCH_DATE like '" + txtYearRevertDeleted.getText() + "-" + cmbMonthRevertDeleted.getSelectedItem() + "%'");
                    JOptionPane.showMessageDialog(this, "Revert Success..!");
                    btnRevertDeleted_ShowDataActionPerformed(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnRevertSelectedActionPerformed

    private void btnShiftRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShiftRegActionPerformed
        // TODO add your handling code here:
        try {

            if (tbl_Punch_From1.getSelectedRowCount() != 0) {
                int rows[] = tbl_Punch_From1.getSelectedRows();
                for (int i = (rows.length - 1); i >= 0; i--) {
                    String Piece_No = tbl_Punch_From1.getValueAt(rows[i], 0).toString();

                    Object[] rowData = new Object[2];
                    rowData[0] = Piece_No;
                    DataModel_To_SHIFT_REG.addRow(rowData);

                    DataModel_Punch_From1.removeRow(rows[i]);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select PUNCH");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnShiftRegActionPerformed

    private void btnUpdatePunch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePunch1ActionPerformed
        // TODO add your handling code here:
        String Date = txtDate_updatepunch1.getText();
        String SelectedEmpNo_updatepunch = txtSelectedEmpNo_updatepunch1.getText();

        for (int i = 0; i < DataModel_To_DELETION1.getRowCount(); i++) {
            String Punch = DataModel_To_DELETION1.getValueAt(i, 0).toString();
            try {
                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'BEFORE UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "' AND MACHINE IN (11,99,98)");
                data.Execute("UPDATE SDMLATTPAY.ATT_DATA  SET UPDATE_IND='DELETION' WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'  AND MACHINE IN (11,99,98)");
                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'AFTER UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'  AND MACHINE IN (11,99,98)");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        for (int i = 0; i < DataModel_To_SHIFT_REG.getRowCount(); i++) {
            String Punch = DataModel_To_SHIFT_REG.getValueAt(i, 0).toString();
            try {
                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'BEFORE UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE MACHINE='11' AND EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");
                data.Execute("UPDATE SDMLATTPAY.ATT_DATA  SET MACHINE='00' WHERE MACHINE='11' AND EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");
                data.Execute("INSERT INTO SDMLATTPAY.ATT_DATA_UPDATE_HISTORY (EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, UPDATE_STATUS, USER_ID, FROM_IP)"
                        + " SELECT EMP_CODE, PUNCH_DATE, P_TIME, UPDATE_IND, MACHINE, 'AFTER UPDATE', " + EITLERPGLOBAL.gUserID + ", '" + str_split[1] + "' FROM SDMLATTPAY.ATT_DATA WHERE MACHINE='00' AND  EMP_CODE='" + SelectedEmpNo_updatepunch + "' AND  P_TIME='" + Punch + "' AND PUNCH_DATE='" + Date + "'");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Record updated...!");
        clearUpdateData();
    }//GEN-LAST:event_btnUpdatePunch1ActionPerformed

    private void btnDeletion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletion1ActionPerformed
        // TODO add your handling code here:
        try {

            if (tbl_Punch_From1.getSelectedRowCount() != 0) {
                int rows[] = tbl_Punch_From1.getSelectedRows();
                for (int i = (rows.length - 1); i >= 0; i--) {
                    String punch = tbl_Punch_From1.getValueAt(rows[i], 0).toString();

                    Object[] rowData = new Object[2];
                    rowData[0] = punch;
                    DataModel_To_DELETION1.addRow(rowData);

                    DataModel_Punch_From1.removeRow(rows[i]);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select PUNCH");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDeletion1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup CoffRokadiBtnGrp;
    private javax.swing.JPanel DailyAttSummary;
    private javax.swing.JFileChooser ExporttoExcelFileChooser;
    private javax.swing.ButtonGroup GatePassBtnGrp;
    private javax.swing.ButtonGroup LCBtnGrp;
    private javax.swing.JTabbedPane TabList;
    private javax.swing.JTable Table;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDailyAttView;
    private javax.swing.JButton btnDeletion;
    private javax.swing.JButton btnDeletion1;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnRevertDeleted_ShowData;
    private javax.swing.JButton btnRevertSelected;
    private javax.swing.JButton btnShift11;
    private javax.swing.JButton btnShift99;
    private javax.swing.JButton btnShiftReg;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnUpdatePunch;
    private javax.swing.JButton btnUpdatePunch1;
    private javax.swing.JComboBox cmbCategory;
    private javax.swing.JComboBox cmbDataUpdate;
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JComboBox cmbMainCategory;
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JComboBox cmbMonthHistory;
    private javax.swing.JComboBox cmbMonthRevertDeleted;
    private javax.swing.JComboBox cmbShift;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDate1;
    private javax.swing.JLabel lblDeptCmb;
    private javax.swing.JLabel lblEmpNameHistory;
    private javax.swing.JLabel lblEmpNameHistory1;
    private javax.swing.JLabel lblEmpNo;
    private javax.swing.JLabel lblMonthCmb;
    private javax.swing.JLabel lblSelectedEmpName;
    private javax.swing.JLabel lblSelectedEmpName1;
    private javax.swing.JLabel lblSelectedEmpName2;
    private javax.swing.JLabel lblShiftCmb;
    private javax.swing.JLabel lblYearCmb;
    private javax.swing.JTable tblHistory;
    private javax.swing.JTable tblRevertDeleted;
    private javax.swing.JTable tbl_Punch_From;
    private javax.swing.JTable tbl_Punch_From1;
    private javax.swing.JTable tbl_To_DELETION;
    private javax.swing.JTable tbl_To_DELETION1;
    private javax.swing.JTable tbl_To_SHIFT_11;
    private javax.swing.JTable tbl_To_SHIFT_99;
    private javax.swing.JTable tbl_To_SHIFT_REG;
    private javax.swing.JTextField txtDate_updatepunch;
    private javax.swing.JTextField txtDate_updatepunch1;
    private javax.swing.JTextField txtDay1;
    private javax.swing.JTextField txtDay10;
    private javax.swing.JTextField txtDay11;
    private javax.swing.JTextField txtDay12;
    private javax.swing.JTextField txtDay13;
    private javax.swing.JTextField txtDay14;
    private javax.swing.JTextField txtDay15;
    private javax.swing.JTextField txtDay16;
    private javax.swing.JTextField txtDay17;
    private javax.swing.JTextField txtDay18;
    private javax.swing.JTextField txtDay19;
    private javax.swing.JTextField txtDay2;
    private javax.swing.JTextField txtDay20;
    private javax.swing.JTextField txtDay21;
    private javax.swing.JTextField txtDay22;
    private javax.swing.JTextField txtDay23;
    private javax.swing.JTextField txtDay24;
    private javax.swing.JTextField txtDay25;
    private javax.swing.JTextField txtDay26;
    private javax.swing.JTextField txtDay27;
    private javax.swing.JTextField txtDay28;
    private javax.swing.JTextField txtDay29;
    private javax.swing.JTextField txtDay3;
    private javax.swing.JTextField txtDay30;
    private javax.swing.JTextField txtDay31;
    private javax.swing.JTextField txtDay4;
    private javax.swing.JTextField txtDay5;
    private javax.swing.JTextField txtDay6;
    private javax.swing.JTextField txtDay7;
    private javax.swing.JTextField txtDay8;
    private javax.swing.JTextField txtDay9;
    private javax.swing.JTextField txtDayOT1;
    private javax.swing.JTextField txtDayOT10;
    private javax.swing.JTextField txtDayOT11;
    private javax.swing.JTextField txtDayOT12;
    private javax.swing.JTextField txtDayOT13;
    private javax.swing.JTextField txtDayOT14;
    private javax.swing.JTextField txtDayOT15;
    private javax.swing.JTextField txtDayOT16;
    private javax.swing.JTextField txtDayOT17;
    private javax.swing.JTextField txtDayOT18;
    private javax.swing.JTextField txtDayOT19;
    private javax.swing.JTextField txtDayOT2;
    private javax.swing.JTextField txtDayOT20;
    private javax.swing.JTextField txtDayOT21;
    private javax.swing.JTextField txtDayOT22;
    private javax.swing.JTextField txtDayOT23;
    private javax.swing.JTextField txtDayOT24;
    private javax.swing.JTextField txtDayOT25;
    private javax.swing.JTextField txtDayOT26;
    private javax.swing.JTextField txtDayOT27;
    private javax.swing.JTextField txtDayOT28;
    private javax.swing.JTextField txtDayOT29;
    private javax.swing.JTextField txtDayOT3;
    private javax.swing.JTextField txtDayOT30;
    private javax.swing.JTextField txtDayOT31;
    private javax.swing.JTextField txtDayOT4;
    private javax.swing.JTextField txtDayOT5;
    private javax.swing.JTextField txtDayOT6;
    private javax.swing.JTextField txtDayOT7;
    private javax.swing.JTextField txtDayOT8;
    private javax.swing.JTextField txtDayOT9;
    private javax.swing.JTextField txtEmpName;
    private javax.swing.JTextField txtEmpNo;
    private javax.swing.JTextField txtEmpNoSearch;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtRevertDeleted;
    private javax.swing.JTextField txtSelectedDept_shiftchange;
    private javax.swing.JTextField txtSelectedDept_updatepunch;
    private javax.swing.JTextField txtSelectedDept_updatepunch1;
    private javax.swing.JTextField txtSelectedEmpName_shiftchange;
    private javax.swing.JTextField txtSelectedEmpName_updatepunch;
    private javax.swing.JTextField txtSelectedEmpName_updatepunch1;
    private javax.swing.JTextField txtSelectedEmpNo_shiftchange;
    private javax.swing.JTextField txtSelectedEmpNo_updatepunch;
    private javax.swing.JTextField txtSelectedEmpNo_updatepunch1;
    private javax.swing.JTextField txtSelectedMonth_shiftchange;
    private javax.swing.JTextField txtSelectedYear_shiftchange;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtYear;
    private javax.swing.JTextField txtYearHistory;
    private javax.swing.JTextField txtYearRevertDeleted;
    // End of variables declaration//GEN-END:variables

    private void GenerateDeptCombo() {

        HashMap List = new HashMap();
        clsMaster ObjDept;

        cmbDept.setModel(modelDept);
        modelDept.removeAllElements();  //Clearing previous contents

        ComboData aData = new ComboData();
        aData.Text = "Select Dept";
        aData.Code = 0;
        modelDept.addElement(aData);

        List = clsMaster.getDepartmentList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjDept = (clsMaster) List.get(Integer.toString(i));
//            ComboData aData = new ComboData();
            aData.Text = (String) ObjDept.getAttribute("Name").getObj();
            aData.Code = (long) ObjDept.getAttribute("DPTID").getVal();
            modelDept.addElement(aData);
        }
    }

    private void GenerateShiftCombo() {

        HashMap List = new HashMap();
        clsMaster ObjShift;

        cmbShift.setModel(modelShift);
        modelShift.removeAllElements();  //Clearing previous contents

        ComboData aData = new ComboData();
        aData.Text = "Select Shift";
        aData.Code = 0;
        modelShift.addElement(aData);

        List = clsMaster.getShiftList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjShift = (clsMaster) List.get(Integer.toString(i));
//            ComboData aData = new ComboData();
            aData.Text = (String) ObjShift.getAttribute("SHIFT_NAME").getObj();
            aData.Code = (long) ObjShift.getAttribute("SHIFT_ID").getVal();
            modelShift.addElement(aData);
        }
    }

    private void GenerateMainCategoryCombo() {

        HashMap List = new HashMap();
        clsMaster ObjMainCategory;

        cmbMainCategory.setModel(modelMainCategory);
        modelMainCategory.removeAllElements();  //Clearing previous contents

        ComboData aData = new ComboData();
        aData.Text = "Select Main Category";
        aData.Code = 0;
        modelMainCategory.addElement(aData);

        List = clsMaster.getMainCategoryList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjMainCategory = (clsMaster) List.get(Integer.toString(i));
//            ComboData aData = new ComboData();
            aData.Text = (String) ObjMainCategory.getAttribute("Name").getObj();
            aData.Code = (long) ObjMainCategory.getAttribute("SECID").getVal();
            modelMainCategory.addElement(aData);
        }
    }

    private void GenerateCategoryCombo() {

        HashMap List = new HashMap();
        clsMaster ObjCategory;

        cmbCategory.setModel(modelCategory);
        modelCategory.removeAllElements();  //Clearing previous contents

        ComboData aData = new ComboData();
        aData.Text = "Select Category";
        aData.Code = 0;
        modelCategory.addElement(aData);

        List = clsMaster.getCategoryList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjCategory = (clsMaster) List.get(Integer.toString(i));
//            ComboData aData = new ComboData();
            aData.Text = (String) ObjCategory.getAttribute("Name").getObj();
            aData.Code = (long) ObjCategory.getAttribute("CTGID").getVal();
            modelCategory.addElement(aData);
        }
    }

    private void FormatGridShiftSchedule() {
        try {

            ResultSet rs;
            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            Table.setAutoResizeMode(0);
            DataModel.addColumn("Sr.");
            String sql = "SELECT '' AS 'Emp no','' AS 'Name','' AS 'Department','' AS 'Month','' AS 'Year'";
            for (int i = 1; i <= 31; i++) {
                sql = sql + ",'' AS '" + i + "'";
            }
            sql = sql + " FROM DUAL";
            rs = data.getResult(sql);
            ResultSetMetaData rsInfo = rs.getMetaData();

            //Format the table from the resultset meta data
            int i = 1;
            DataModel.ClearAllReadOnly();

            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                DataModel.addColumn(rsInfo.getColumnName(i));
                DataModel.SetReadOnly(i);
            }

            DataModel.SetReadOnly(0);
            Table.getColumnModel().getColumn(0).setMaxWidth(50);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateShiftSchedule() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridShiftSchedule(); //clear existing content of table
            ResultSet rsTmp;

            if (!txtEmpNo.getText().trim().equals("")) {
                cndtn += " AND SSC_EMPID= '" + txtEmpNo.getText().trim() + "'";
            }
            if (!cmbDept.getSelectedItem().equals("Select Dept")) {
                grp_cndtn += " WHERE EMP_DEPARTMENT= " + cmbDept.getSelectedIndex() + " ";
            }
            if (!cmbMonth.getSelectedItem().equals("Select")) {
                cndtn += " AND SSC_MONTH= " + cmbMonth.getSelectedIndex() + " ";
            }
            if (!txtYear.getText().trim().equals("")) {
                cndtn += " AND SSC_YEAR= '" + txtYear.getText().trim() + "'";
            }

            String strSQL = "";

            strSQL = "SELECT * FROM ( SELECT * FROM SDMLATTPAY.ATT_SHIFT_SCHEDULE WHERE 1=1 " + cndtn + " ) AS SSC "
                    + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP "
                    + "ON SSC.SSC_EMPID=EMP.PAY_EMP_NO "
                    + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT "
                    + "ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                    + grp_cndtn + " "
                    + "ORDER BY PAY_EMP_NO,SSC_YEAR,SSC_MONTH ";

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
                    rowData[1] = rsTmp.getString("PAY_EMP_NO");
                    rowData[2] = rsTmp.getString("EMP_NAME");
                    rowData[3] = rsTmp.getString("DPTNAME");
                    rowData[4] = rsTmp.getString("SSC_MONTH");
                    rowData[5] = rsTmp.getString("SSC_YEAR");
                    for (int j = 1; j <= 31; j++) {
                        rowData[j + 5] = rsTmp.getString("SSC_" + j);
                    }

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

    private void FormatGridDailyAtt() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Emp Pay No");
        DataModel.addColumn("Emp Name");
        DataModel.addColumn("Department");
        DataModel.addColumn("Punch Date");
        DataModel.addColumn("No of Punches");
        DataModel.addColumn("Shift");
        DataModel.addColumn("Status");
        DataModel.addColumn("Spe. Sanction Status");
        DataModel.addColumn("In Time");
        DataModel.addColumn("Out Time");
        DataModel.addColumn("Working Hours");
        DataModel.addColumn("Personal GP 1st Half");
        DataModel.addColumn("Personal GP 2st Half");
        DataModel.addColumn("Official GP 1st Half");
        DataModel.addColumn("Official GP 2st Half");
        DataModel.addColumn("Late Hours");
        DataModel.addColumn("LC+LWP");
        DataModel.addColumn("Punches");
        DataModel.addColumn("Personal GP Punches");
        DataModel.addColumn("Official GP Punches");
        DataModel.addColumn("Machine 11 (OT) Punches");
        DataModel.addColumn("Dummy Punches");
        DataModel.addColumn("Deletion Punches");
        DataModel.addColumn("Correction Punches");
        DataModel.addColumn("Mis Punches");
        DataModel.addColumn("Category");
        DataModel.addColumn("Sub Category");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateDailyAtt() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGridDailyAtt(); //clear existing content of table
            ResultSet rsTmp;

            if (!txtEmpNo.getText().trim().equals("")) {
                cndtn += " AND EMPID= '" + txtEmpNo.getText().trim() + "'";
            }
            if (!cmbDept.getSelectedItem().equals("Select Dept")) {
                grp_cndtn += " WHERE EMP_DEPARTMENT= " + cmbDept.getSelectedIndex() + " ";
            }
            if (!cmbShift.getSelectedItem().equals("Select Shift")) {
                cndtn += " AND SHIFT= " + cmbShift.getSelectedIndex() + " ";
            }
            if (!cmbMainCategory.getSelectedItem().equals("Select Main Category")) {
                cndtn += " AND MAIN_CATEGORY= " + cmbMainCategory.getSelectedIndex() + " ";
            }
            if (!cmbCategory.getSelectedItem().equals("Select Category")) {
                cndtn += " AND CATEGORY= " + cmbCategory.getSelectedIndex() + " ";
            }
            if (!cmbMonth.getSelectedItem().equals("Select")) {
                cndtn += " AND MM= " + cmbMonth.getSelectedIndex() + " ";
            }
            if (!txtYear.getText().trim().equals("")) {
                cndtn += " AND YYYY= '" + txtYear.getText().trim() + "'";
            }
            if (!txtFromDate.getText().trim().equals("")) {
                cndtn += " AND PUNCHDATE>= '" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "'";
            }
            if (!txtToDate.getText().trim().equals("")) {
                cndtn += " AND PUNCHDATE<= '" + EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) + "'";
            }

            String strSQL = "";

            strSQL = "SELECT * FROM ( SELECT *, "
                    + "SEC_TO_TIME(TIME_TO_SEC(GP_FIRST_HALF) + TIME_TO_SEC(GP_SECOND_HALF) + TIME_TO_SEC(LATE_COMING_HRS) + TIME_TO_SEC(LUNCH_LATE_HRS)+TIME_TO_SEC(GP_ADDITIONAL_HRS)) AS TOTAL_LATE_COMING "
                    + "FROM SDMLATTPAY.ATT_DATA_DAILY_SUMMARY WHERE 1=1 " + cndtn + " ) AS DAS "
                    + "LEFT JOIN ( SELECT PAY_EMP_NO,EMP_NAME,EMP_DEPARTMENT,EMP_MAIN_CATEGORY,EMP_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST ) AS EMP "
                    + "ON DAS.EMPID=EMP.PAY_EMP_NO "
                    + "LEFT JOIN ( SELECT DPTID,NAME AS DPTNAME FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ) AS DPT "
                    + "ON DPT.DPTID=EMP.EMP_DEPARTMENT "
                    + "LEFT JOIN ( SELECT SECID,NAME AS SECNAME FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER ) AS SEC "
                    + "ON SEC.SECID=DAS.MAIN_CATEGORY "
                    + "LEFT JOIN ( SELECT CTGID,NAME AS CTGNAME FROM SDMLATTPAY.ATT_CATEGORY_MASTER ) AS CTG "
                    + "ON CTG.CTGID=DAS.CATEGORY "
                    + "LEFT JOIN ( SELECT *,'YES' AS SSS FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE APPROVED=1 AND CANCELED=0 ) AS SS "
                    + "ON DAS.EMPID=SS.EMP_CODE AND DAS.PUNCHDATE=SS.A_DATE "
                    + " " + grp_cndtn + " ";

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
                    rowData[1] = rsTmp.getString("EMPID");
                    rowData[2] = rsTmp.getString("EMP_NAME");
                    rowData[3] = rsTmp.getString("DPTNAME");
                    rowData[4] = EITLERPGLOBAL.formatDate(rsTmp.getString("PUNCHDATE"));
                    rowData[5] = rsTmp.getString("PUNCHES_NOS");
                    rowData[6] = rsTmp.getString("SHIFT");
                    rowData[7] = rsTmp.getString("PRESENT_FIRST") + " " + rsTmp.getString("PRESENT_SECOND");
                    rowData[8] = rsTmp.getString("SSS");
                    rowData[9] = rsTmp.getString("INTIME").substring(11, 16);
                    rowData[10] = rsTmp.getString("OUTTIME").substring(11, 16);
                    rowData[11] = rsTmp.getString("TOTAL_WORKING_HRS").substring(0, 5);
                    rowData[12] = rsTmp.getString("GP_FIRST_HALF").substring(0, 5);
                    rowData[13] = rsTmp.getString("GP_SECOND_HALF").substring(0, 5);
                    rowData[14] = rsTmp.getString("GPO_FIRST_HALF").substring(0, 5);
                    rowData[15] = rsTmp.getString("GPO_SECOND_HALF").substring(0, 5);
                    rowData[16] = rsTmp.getString("GATEPASS_LATE").substring(0, 5);
                    rowData[17] = rsTmp.getString("TOTAL_LATE_COMING").substring(0, 5);
                    rowData[18] = rsTmp.getString("ALL_PUNCHES");
                    rowData[19] = rsTmp.getString("ALL_PERSONAL_GATEPASS_PUNCHES");
                    rowData[20] = rsTmp.getString("ALL_OFFICIAL_GATEPASS_PUNCHES");
                    rowData[21] = rsTmp.getString("ALL_OT_PUNCHES");
                    rowData[22] = rsTmp.getString("ALL_DUMMY_PUNCHES");
                    rowData[23] = rsTmp.getString("ALL_DELETE_PUNCHES");
                    rowData[24] = rsTmp.getString("ALL_CORRECTION_PUNCHES");
                    rowData[25] = rsTmp.getString("ALL_MIS_PUNCHES");
                    rowData[26] = rsTmp.getString("SECNAME");
                    rowData[27] = rsTmp.getString("CTGNAME");

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

    private void FormatGridShiftMaster() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");
        DataModel.addColumn("Code");
        DataModel.addColumn("Name");
        DataModel.addColumn("In Time");
        DataModel.addColumn("Out Time");
        DataModel.addColumn("Lunch In");
        DataModel.addColumn("Lunch Out");
        DataModel.addColumn("Working Hours");

        DataModel.TableReadOnly(true);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void GenerateShiftMst() {
        try {
            FormatGridShiftMaster(); //clear existing content of table
            ResultSet rsTmp;

            String strSQL = "";

            strSQL = "SELECT * FROM SDMLATTPAY.ATT_SHIFT";

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
                    rowData[1] = rsTmp.getString("SHIFT_ID");
                    rowData[2] = rsTmp.getString("SHIFT_NAME");
                    rowData[3] = rsTmp.getString("SHIFT_IN_TIME");
                    rowData[4] = rsTmp.getString("SHIFT_OUT_TIME");
                    rowData[5] = rsTmp.getString("SHIFT_LUNCH_IN");
                    rowData[6] = rsTmp.getString("SHIFT_LUNCH_OUT");
                    rowData[7] = rsTmp.getString("SHIFT_WRK_HRS");

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

    private boolean DateValidate() {
        if (txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Enter From Date");
            txtFromDate.setText("");
            return false;
        }
        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) + "',CURDATE()) FROM DUAL") > 0) {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Enter Less than or Equals Current Date in From Date");
            txtFromDate.setText("");
            return false;
        }

        if (txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Enter To Date");
            txtToDate.setText("");
            return false;
        }
        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtToDate.getText()) + "','" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) + "') FROM DUAL") < 0) {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Enter Greater Date than From Date in To Date");
            txtToDate.setText("");
            return false;
        }

        return true;
    }

    private boolean MonthYearValidate() {
        if (cmbMonth.getSelectedItem().equals("Select")) {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Select Month");
            return false;
        }
        if (txtYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Enter Year");
            txtYear.setText("");
            return false;
        }
        if (txtYear.getText().trim().length() != 4) {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Enter Valid Year");
            txtYear.setText("");
            return false;
        }
        if (!EITLERPGLOBAL.IsNumber(txtYear.getText().trim())) {
            JOptionPane.showMessageDialog(FrmAdvanceShiftChange.this, "Please Enter Valid Year");
            txtYear.setText("");
            return false;
        }

        return true;
    }
}
