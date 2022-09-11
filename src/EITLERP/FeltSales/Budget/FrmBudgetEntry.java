/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Budget;

import EITLERP.AppletFrame;
import EITLERP.BigEdit;
import EITLERP.JTextFieldHint;
import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.LOV;
import EITLERP.FeltSales.common.SelectFirstFree;
import EITLERP.Loader;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.clsAuthority;
import EITLERP.clsDepartment;
import EITLERP.clsDocFlow;
import EITLERP.clsFirstFree;
import EITLERP.clsHierarchy;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import EITLERP.frmPendingApprovals;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Dharmendra PRAJAPATI
 *
 */
public class FrmBudgetEntry extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbModuleModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableCellRenderer CellAlign = new EITLTableCellRenderer();
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    private int ModuleId = 768;
    private String DOC_NO = "";
    private clsBudgetEntry BudgetEntry;
    private EITLComboModel cmbSendToModel;
    private int FinalApprovedBy = 0;
    private int mnoofmachine = 0;
    String cellLastValue = "";
    String seleval = "", seltyp = "", selqlt = "", selshd = "", selpiece = "", selext = "", selinv = "", selsz = "";
    private int mlstrc;
    
    String machineno="";

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateForDB = new SimpleDateFormat("yyyy-MM-dd");
    
    JScrollPane[] scrollpane=new JScrollPane[100];
    JTable[] tblPress = new JTable[100];
    final EITLTableModel[] DataModel_Press = new EITLTableModel[100];

    JTable[] tblDryer = new JTable[100];
    final EITLTableModel[] DataModel_dryer = new EITLTableModel[100];

    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    public frmPendingApprovals frmPA;

    @Override
    public void init() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        initComponents();
        //cmdimport.setVisible(false);
        GenerateCombos();
//        FormatGrid();
        GenerateFromCombo();
        //GenerateHierarchyCombo();

        SetMenuForRights();
        DefaultSettings();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();

        BudgetEntry = new clsBudgetEntry();
        boolean load = BudgetEntry.LoadData(EITLERPGLOBAL.gCompanyID);
        if (load) {
            DisplayData();
            MoveLast();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + BudgetEntry.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        SetupApproval();
        mnoofmachine = 0;
        SetFields(false);
        
        
        jButton3.setVisible(false);
        jButton1.setVisible(false); 
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    public void DefaultSettings() {

        //String data = toString();
//        Object[] rowData = new Object[15];
//        rowData[0] = "1";
//        DataModel.addRow(rowData);
        cmdTop.setIcon(EITLERPGLOBAL.getImage("TOP"));
        cmdBack.setIcon(EITLERPGLOBAL.getImage("BACK"));
        cmdNext.setIcon(EITLERPGLOBAL.getImage("NEXT"));
        cmdLast.setIcon(EITLERPGLOBAL.getImage("LAST"));
        cmdNew.setIcon(EITLERPGLOBAL.getImage("NEW"));
        cmdEdit.setIcon(EITLERPGLOBAL.getImage("EDIT"));
        cmdDelete.setIcon(EITLERPGLOBAL.getImage("DELETE"));
        cmdSave.setIcon(EITLERPGLOBAL.getImage("SAVE"));
        cmdCancel.setIcon(EITLERPGLOBAL.getImage("UNDO"));
        cmdFilter.setIcon(EITLERPGLOBAL.getImage("FIND"));
        cmdPreview.setIcon(EITLERPGLOBAL.getImage("PREVIEW"));
        cmdPrint.setIcon(EITLERPGLOBAL.getImage("PRINT"));
        cmdExit.setIcon(EITLERPGLOBAL.getImage("EXIT"));
        clearFields();
        //lblTitle1.setBackground(new Color(0, 102, 153));
        lblTitle1.setForeground(Color.WHITE);
    }

    private void clearFields() {

        //JOptionPane.showMessageDialog(null, "Data Model size : "+DataModel.getRowCount());
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
//        FormatGrid();
        // FormatGridA();
        FormatGridHS();
        txtyearfrom.setText("");
        lblyearto.setText("");

//        for (int i = 0; i < DataModel.getRowCount(); i++) {
//            DataModel.removeRow(i);
//        }
//        if (DataModel.getRowCount() > 0) {
//            DataModel.removeRow(0);
//        }
//        Object[] rowData = new Object[15];
//        rowData[0] = 1;
//        DataModel.addRow(rowData);
    }

    private void DisplayData() {

        //=========== Color Indication ===============//
        try {
            btnSendFAmail.setEnabled(false);
            if (BudgetEntry.getAttribute("APPROVED").getInt() == 1) {
                lblTitle1.setBackground(Color.BLUE);
                lblTitle1.setForeground(Color.WHITE);
                btnSendFAmail.setEnabled(true);
            }

            if (BudgetEntry.getAttribute("APPROVED").getInt() == 0) {
                lblTitle1.setBackground(Color.GRAY);
                lblTitle1.setForeground(Color.WHITE);
            }

            if (BudgetEntry.getAttribute("CANCELED").getInt() == 1) {
                lblTitle1.setBackground(Color.RED);
                lblTitle1.setForeground(Color.BLACK);
            }
        } catch (Exception c) {

            c.printStackTrace();
        }

        //============================================//
        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleId)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        clearFields();
        try {
            DOC_NO = BudgetEntry.getAttribute("DOC_NO").getString();
            lblTitle1.setText("Budget Entry  - " + DOC_NO);
            DOC_NO1.setText(DOC_NO);

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) BudgetEntry.getAttribute("HIERARCHY_ID").getVal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mnoofmachine = 0;
        try {
            tabTest.removeAll();
            JPanel machines = new JPanel();
            ResultSet r;
            r = data.getResult("SELECT * FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' ORDER BY MACHINE_NO,POSITION_NO");
            r.first();
            String mmachine = "";
            txtpartycode.setText(r.getString("PARTY_CODE"));
            txtpartyname.setText(r.getString("PARTY_NAME"));
            txtyearfrom.setText(r.getString("YEAR_FROM"));
            lblyearto.setText(r.getString("YEAR_TO"));
            int i = 0;
            mmachine = r.getString("MACHINE_NO");
            machineno=mmachine;
            machines = new JPanel();
            machines.setLayout(null);
            tabTest.removeAll();
            tabTest.add("Machine " + r.getString("MACHINE_NO"), machines);

            
            tblPress[i] = new JTable();
            //scrollpane[i]=new JScrollPane();
            //scrollpane[i].add(tblPress[i]);
            DataModel_Press[i] = new EITLTableModel();
            tblPress[i].removeAll();            
            tblPress[i].setModel(DataModel_Press[i]);
            //tblDryer.setBounds(10, 10, 500, 100);

            tblPress[i].setAutoResizeMode(0);

            DataModel_Press[i].addColumn("SrNo"); //0 - Read Only
            DataModel_Press[i].addColumn("UPN"); //1
            DataModel_Press[i].addColumn("Position Desc"); //2
            DataModel_Press[i].addColumn("Quality"); //3
            DataModel_Press[i].addColumn("Group"); //4
            DataModel_Press[i].addColumn("Value"); //5
            DataModel_Press[i].addColumn("Potential"); //6
            DataModel_Press[i].addColumn("Projected Qty"); //7
            DataModel_Press[i].addColumn("Projected Value"); //8
            DataModel_Press[i].addColumn("Qty " + String.valueOf(r.getInt("YEAR_FROM") - 1).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 1).substring(2, 4)); //9
            DataModel_Press[i].addColumn("Value " + String.valueOf(r.getInt("YEAR_FROM") - 1).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 1).substring(2, 4)); //10
            DataModel_Press[i].addColumn("Qty " + String.valueOf(r.getInt("YEAR_FROM") - 2).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 2).substring(2, 4)); //11
            DataModel_Press[i].addColumn("Value " + String.valueOf(r.getInt("YEAR_FROM") - 2).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 2).substring(2, 4)); //12
            
            DataModel_Press[i].addColumn("WIP 31 Mar Qty"); //13
            DataModel_Press[i].addColumn("WIP 31 Mar Value"); //14
            DataModel_Press[i].addColumn("Stock 31 Mar Qty"); //15
            DataModel_Press[i].addColumn("Stock 31 Mar Value"); //16          
            
            
            DataModel_Press[i].addColumn("Stock Qty"); //17
            DataModel_Press[i].addColumn("Stock Value"); //18
            DataModel_Press[i].addColumn("WIP Qty"); //19
            DataModel_Press[i].addColumn("WIP Value"); //20
            
            DataModel_Press[i].addColumn("Avg Life"); //21
            DataModel_Press[i].addColumn("Last Inv Dt"); //22
            DataModel_Press[i].addColumn("Projected MMYYYY"); //23
            DataModel_Press[i].addColumn("Incharge"); //24
            DataModel_Press[i].addColumn("Size Criteria"); //25
            DataModel_Press[i].addColumn("Party Group"); //26

//            tblPress[i].getColumnModel().getColumn(0).setMaxWidth(30);
//            tblPress[i].getColumnModel().getColumn(1).setMinWidth(90);
//            tblPress[i].getColumnModel().getColumn(2).setMinWidth(80);
//            tblPress[i].getColumnModel().getColumn(3).setMaxWidth(50);
//            tblPress[i].getColumnModel().getColumn(4).setMinWidth(50);
//            tblPress[i].getColumnModel().getColumn(5).setMaxWidth(60);
//            tblPress[i].getColumnModel().getColumn(6).setMaxWidth(60);
//            tblPress[i].getColumnModel().getColumn(8).setMinWidth(90);
//            tblPress[i].getColumnModel().getColumn(9).setMinWidth(80);
//            tblPress[i].getColumnModel().getColumn(10).setMinWidth(80);
//            tblPress[i].getColumnModel().getColumn(11).setMinWidth(80);
//            tblPress[i].getColumnModel().getColumn(12).setMinWidth(80);
//            tblPress[i].getColumnModel().getColumn(12).setMinWidth(80);
//            tblPress[i].getColumnModel().getColumn(14).setMinWidth(80);
//            tblPress[i].getColumnModel().getColumn(15).setMinWidth(80);
//            tblPress[i].getColumnModel().getColumn(16).setMinWidth(80);
             
            final TableColumnModel columnModel1 = tblPress[i].getColumnModel();
                for (int column = 0; column < tblPress[i].getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < tblPress[i].getRowCount(); row++) {
                        TableCellRenderer renderer = tblPress[i].getCellRenderer(row, column);
                        Component comp = tblPress[i].prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel1.getColumn(column).setPreferredWidth(width);
                }
            
            final int final_i = i;
            tblPress[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent ke) {
                    for (int ji = 0; ji < DataModel_Press[final_i].getRowCount(); ji++) {
                        double value = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 5).toString());

                        double pq = 0;
                        try {
                            pq = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 7).toString());
                        } catch (Exception nf) {
                            pq = 0;
                        }
                        DataModel_Press[final_i].setValueAt(value * pq, ji, 8);
                    }
                }
            });

            JScrollPane jScrollPane2 = new JScrollPane();
            jScrollPane2.setViewportView(tblPress[i]);
            

            machines.add(jScrollPane2);
            jScrollPane2.setBounds(0, 40, 1300, 100);
            Object[] rowData = new Object[30];//20

            tblDryer[i] = new JTable();
            DataModel_dryer[i] = new EITLTableModel();
            tblDryer[i].removeAll();
            tblDryer[i].setModel(DataModel_dryer[i]);
            //tblDryer.setBounds(10, 10, 500, 100);

            tblDryer[i].setAutoResizeMode(0);

            DataModel_dryer[i].addColumn("SrNo"); //0 - Read Only
            DataModel_dryer[i].addColumn("UPN"); //1
            DataModel_dryer[i].addColumn("Position Desc"); //2
            DataModel_dryer[i].addColumn("Quality"); //3
            DataModel_dryer[i].addColumn("Group"); //4
            DataModel_dryer[i].addColumn("Value"); //5
            DataModel_dryer[i].addColumn("Potential"); //6
            DataModel_dryer[i].addColumn("Projected Qty"); //7
            DataModel_dryer[i].addColumn("Projected Value"); //8
            DataModel_dryer[i].addColumn("Qty " + String.valueOf(r.getInt("YEAR_FROM") - 1).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 1).substring(2, 4)); //9
            DataModel_dryer[i].addColumn("Value " + String.valueOf(r.getInt("YEAR_FROM") - 1).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 1).substring(2, 4)); //10
            DataModel_dryer[i].addColumn("Qty " + String.valueOf(r.getInt("YEAR_FROM") - 2).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 2).substring(2, 4)); //11
            DataModel_dryer[i].addColumn("Value " + String.valueOf(r.getInt("YEAR_FROM") - 2).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 2).substring(2, 4)); //12
            
            DataModel_dryer[i].addColumn("WIP 31 Mar Qty"); //13
            DataModel_dryer[i].addColumn("WIP 31 Mar Value"); //14
            DataModel_dryer[i].addColumn("Stock 31 Mar Qty"); //15
            DataModel_dryer[i].addColumn("Stock 31 Mar Value"); //16                                 
            
            DataModel_dryer[i].addColumn("Stock Qty"); //17
            DataModel_dryer[i].addColumn("Stock Value"); //18
            DataModel_dryer[i].addColumn("WIP Qty"); //19
            DataModel_dryer[i].addColumn("WIP Value"); //20
            
            DataModel_dryer[i].addColumn("Avg Life"); //20
            DataModel_dryer[i].addColumn("Last Inv Dt"); //22
            DataModel_dryer[i].addColumn("Projected MMYYYY"); //23
            DataModel_dryer[i].addColumn("Incharge"); //24
            DataModel_dryer[i].addColumn("Size Criteria"); //25
            DataModel_dryer[i].addColumn("Party Group"); //26

//            tblDryer[i].getColumnModel().getColumn(0).setMaxWidth(30);
//            tblDryer[i].getColumnModel().getColumn(1).setMinWidth(90);
//            tblDryer[i].getColumnModel().getColumn(2).setMinWidth(80);
//            tblDryer[i].getColumnModel().getColumn(3).setMaxWidth(50);
//            tblDryer[i].getColumnModel().getColumn(4).setMinWidth(50);
//            tblDryer[i].getColumnModel().getColumn(5).setMaxWidth(60);
//            tblDryer[i].getColumnModel().getColumn(6).setMaxWidth(60);
//            tblDryer[i].getColumnModel().getColumn(8).setMinWidth(90);
//            tblDryer[i].getColumnModel().getColumn(9).setMinWidth(80);
//            tblDryer[i].getColumnModel().getColumn(10).setMinWidth(80);
//            tblDryer[i].getColumnModel().getColumn(11).setMinWidth(80);
//            tblDryer[i].getColumnModel().getColumn(12).setMinWidth(80);
//            tblDryer[i].getColumnModel().getColumn(12).setMinWidth(80);
//            tblDryer[i].getColumnModel().getColumn(14).setMinWidth(80);
//            tblDryer[i].getColumnModel().getColumn(15).setMinWidth(80);
//            tblDryer[i].getColumnModel().getColumn(16).setMinWidth(80);

            //tblDryer[i].getColumnModel().getColumn(0).set
            
               final TableColumnModel columnModel = tblDryer[i].getColumnModel();
                for (int column = 0; column < tblDryer[i].getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < tblDryer[i].getRowCount(); row++) {
                        TableCellRenderer renderer = tblDryer[i].getCellRenderer(row, column);
                        Component comp = tblDryer[i].prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            
            tblDryer[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent ke) {
                    for (int ji = 0; ji < DataModel_dryer[final_i].getRowCount(); ji++) {
                        double value = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 5).toString());

                        double pq = 0;
                        try {
                            pq = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 7).toString());
                        } catch (Exception nf) {
                            pq = 0;
                        }
                        DataModel_dryer[final_i].setValueAt(value * pq, ji, 8);
                    }
                }
            });

            JScrollPane jScrollPane3 = new JScrollPane();
            jScrollPane3.setViewportView(tblDryer[i]);

            jScrollPane3.setBounds(0, 200, 1300, 150);
            machines.add(jScrollPane3);

            JLabel lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Press");
            lblMachine.setBounds(0, 5, 200, 50);
            machines.add(lblMachine);
            lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Dryer");
            lblMachine.setBounds(0, 150, 200, 50);
            machines.add(lblMachine);
            int msr = 1;
            int msr1 = 1;
            while (!r.isAfterLast()) {
                if (mmachine.equalsIgnoreCase(r.getString("MACHINE_NO"))) {
                    rowData = new Object[30];
                    if (r.getString("GROUP_NAME").equalsIgnoreCase("HDS") || r.getString("GROUP_NAME").equalsIgnoreCase("SDF")) {
                        rowData[0] = msr1;
                        rowData[1] = r.getString("UPN");
                        rowData[2] = r.getString("POSITION_DESC");
                        rowData[3] = r.getString("QUALITY_NO");
                        rowData[4] = r.getString("GROUP_NAME");
                        rowData[5] = r.getDouble("SELLING_PRICE");
                        rowData[6] = r.getDouble("POTENTIAL");
                        if (r.getInt("Q4") == 0) {
                            rowData[7] = "";
                        } else {
                            rowData[7] = r.getInt("Q4");
                        }
                        if (r.getInt("Q4NET_AMOUNT") == 0) {
                            rowData[8] = "";
                        } else {
                            rowData[8] = r.getInt("Q4NET_AMOUNT");
                        }
                        
                        rowData[9] = r.getDouble("PREV_YEAR_QTY");
                        rowData[10] = r.getDouble("PREV_YEAR_VALUE");
                        rowData[11] = r.getDouble("PREV_PREV_YEAR_QTY");
                        rowData[12] = r.getDouble("PREV_PREV_YEAR_VALUE");
                        
                        rowData[13] = r.getDouble("WIP_31_MAR_QTY");
                        rowData[14] = r.getDouble("WIP_31_MAR_VALUE");
                        rowData[15] = r.getDouble("STOCK_31_MAR_QTY");
                        rowData[16] = r.getDouble("STOCK_31_MAR_VALUE");
                        
                        rowData[17] = r.getDouble("STOCK_QTY");
                        rowData[18] = r.getDouble("STOCK_VALUE");
                        rowData[19] = r.getDouble("WIP_QTY");
                        rowData[20] = r.getDouble("WIP_VALUE");
                        
                        rowData[21] = r.getDouble("AVG_LIFE");
                        rowData[22] = EITLERPGLOBAL.formatDate(r.getString("LAST_INVOICE_DATE"));
                        rowData[23] = EITLERPGLOBAL.formatDate(r.getString("PROJECTED_MMYYYY"));
                        rowData[24] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='"+r.getString("INCHARGE")+"'");
                        rowData[25] = r.getString("SIZE_CRITERIA");
                        rowData[26] = r.getString("PARTY_GROUP");
                        
                        msr1++;
                        DataModel_dryer[i].addRow(rowData);
                    } else {
                        rowData[0] = msr;
                        rowData[1] = r.getString("UPN");
                        rowData[2] = r.getString("POSITION_DESC");
                        rowData[3] = r.getString("QUALITY_NO");
                        rowData[4] = r.getString("GROUP_NAME");
                        rowData[5] = r.getDouble("SELLING_PRICE");
                        rowData[6] = r.getDouble("POTENTIAL");
                        if (r.getInt("Q4") == 0) {
                            rowData[7] = "";
                        } else {
                            rowData[7] = r.getInt("Q4");
                        }
                        
                        if (r.getInt("Q4NET_AMOUNT") == 0) {
                            rowData[8] = "";
                        } else {
                            rowData[8] = r.getInt("Q4NET_AMOUNT");
                        }
                                               
                        
                        rowData[9] = r.getDouble("PREV_YEAR_QTY");
                        rowData[10] = r.getDouble("PREV_YEAR_VALUE");
                        rowData[11] = r.getDouble("PREV_PREV_YEAR_QTY");
                        rowData[12] = r.getDouble("PREV_PREV_YEAR_VALUE");
                        
                        rowData[13] = r.getDouble("WIP_31_MAR_QTY");
                        rowData[14] = r.getDouble("WIP_31_MAR_VALUE");
                        rowData[15] = r.getDouble("STOCK_31_MAR_QTY");
                        rowData[16] = r.getDouble("STOCK_31_MAR_VALUE");
                        
                        
                        rowData[17] = r.getDouble("STOCK_QTY");                        
                        rowData[18] = r.getDouble("STOCK_VALUE");
                        rowData[19] = r.getDouble("WIP_QTY");
                        rowData[20] = r.getDouble("WIP_VALUE");
                        
                        rowData[21] = r.getDouble("AVG_LIFE");
                        rowData[22] = EITLERPGLOBAL.formatDate(r.getString("LAST_INVOICE_DATE"));
                        rowData[23] = EITLERPGLOBAL.formatDate(r.getString("PROJECTED_MMYYYY"));
                        rowData[24] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='"+r.getString("INCHARGE")+"'");
                        rowData[25] = r.getString("SIZE_CRITERIA");
                        rowData[26] = r.getString("PARTY_GROUP");
                        
                        msr++;
                        DataModel_Press[i].addRow(rowData);
                    }
                } else {
                    i++;
                    JPanel machine = new JPanel();
                    machine = new JPanel();
                    machine.setLayout(null);

                    tabTest.add("Machine " + r.getString("MACHINE_NO"), machine);

                    tblPress[i] = new JTable();
                    DataModel_Press[i] = new EITLTableModel();

                    tblPress[i].removeAll();
                    tblPress[i].setModel(DataModel_Press[i]);
                    //tblDryer.setBounds(10, 10, 500, 100);

                    tblPress[i].setAutoResizeMode(0);

                    DataModel_Press[i].addColumn("SrNo"); //0 - Read Only
                    DataModel_Press[i].addColumn("UPN"); //1
                    DataModel_Press[i].addColumn("Position Desc"); //2
                    DataModel_Press[i].addColumn("Quality"); //3
                    DataModel_Press[i].addColumn("Group"); //4
                    DataModel_Press[i].addColumn("Value"); //5
                    DataModel_Press[i].addColumn("Potential"); //6
                    DataModel_Press[i].addColumn("Projected Qty"); //7
                    DataModel_Press[i].addColumn("Projected Value"); //8
                    DataModel_Press[i].addColumn("Qty " + String.valueOf(r.getInt("YEAR_FROM") - 1).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 1).substring(2, 4)); //9
                    DataModel_Press[i].addColumn("Value " + String.valueOf(r.getInt("YEAR_FROM") - 1).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 1).substring(2, 4)); //10
                    DataModel_Press[i].addColumn("Qty " + String.valueOf(r.getInt("YEAR_FROM") - 2).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 2).substring(2, 4)); //11
                    DataModel_Press[i].addColumn("Value " + String.valueOf(r.getInt("YEAR_FROM") - 2).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 2).substring(2, 4)); //12
            
                    
                    DataModel_Press[i].addColumn("WIP 31 Mar Qty"); //13
                    DataModel_Press[i].addColumn("WIP 31 Mar Value"); //14
                    DataModel_Press[i].addColumn("Stock 31 Mar Qty"); //15
                    DataModel_Press[i].addColumn("Stock 31 Mar Value"); //16          
            
                    
//                    DataModel_Press[i].addColumn("Stock Qty"); //17
//                    DataModel_Press[i].addColumn("Stock Value"); //18
//                    DataModel_Press[i].addColumn("WIP Qty"); //19
//                    DataModel_Press[i].addColumn("WIP Value"); //20
//                    
//                    DataModel_Press[i].addColumn("Avg Life"); //21
//                    DataModel_Press[i].addColumn("Last Inv Dt"); //22
//                    DataModel_Press[i].addColumn("Projected MMYYYY"); //23
//                    DataModel_Press[i].addColumn("Size Criteria"); //24
//                    DataModel_Press[i].addColumn("Party Group"); //25
                    
                    
                    
                    DataModel_Press[i].addColumn("Stock Qty"); //17
            DataModel_Press[i].addColumn("Stock Value"); //18
            DataModel_Press[i].addColumn("WIP Qty"); //19
            DataModel_Press[i].addColumn("WIP Value"); //20
            
            DataModel_Press[i].addColumn("Avg Life"); //21
            DataModel_Press[i].addColumn("Last Inv Dt"); //22
            DataModel_Press[i].addColumn("Projected MMYYYY"); //23
            DataModel_Press[i].addColumn("Incharge"); //24
            DataModel_Press[i].addColumn("Size Criteria"); //25
            DataModel_Press[i].addColumn("Party Group"); //26


                    tblPress[i].getColumnModel().getColumn(0).setMaxWidth(30);
                    tblPress[i].getColumnModel().getColumn(1).setMinWidth(90);
                    tblPress[i].getColumnModel().getColumn(2).setMinWidth(80);
                    tblPress[i].getColumnModel().getColumn(3).setMaxWidth(50);
                    tblPress[i].getColumnModel().getColumn(4).setMinWidth(50);
                    tblPress[i].getColumnModel().getColumn(5).setMaxWidth(60);
                    tblPress[i].getColumnModel().getColumn(6).setMaxWidth(60);
                    tblPress[i].getColumnModel().getColumn(8).setMinWidth(90);
                    tblPress[i].getColumnModel().getColumn(9).setMinWidth(80);
                    tblPress[i].getColumnModel().getColumn(10).setMinWidth(80);
                    tblPress[i].getColumnModel().getColumn(11).setMinWidth(80);
                    tblPress[i].getColumnModel().getColumn(12).setMinWidth(80);
                    tblPress[i].getColumnModel().getColumn(12).setMinWidth(80);
                    tblPress[i].getColumnModel().getColumn(14).setMinWidth(80);
                    tblPress[i].getColumnModel().getColumn(15).setMinWidth(80);
                    tblPress[i].getColumnModel().getColumn(16).setMinWidth(80);

                    final int final_i1 = i;
                    tblPress[i].addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent ke) {
                            for (int ji = 0; ji < DataModel_Press[final_i1].getRowCount(); ji++) {
                                double value = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 5).toString());

                                double pq = 0;
                                try {
                                    pq = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 7).toString());
                                } catch (Exception nf) {
                                    pq = 0;
                                }
                                DataModel_Press[final_i1].setValueAt(value * pq, ji, 8);
                            }
                        }
                    });

                    jScrollPane2 = new JScrollPane();
                    jScrollPane2.setViewportView(tblPress[i]);

                    machine.add(jScrollPane2);
                    jScrollPane2.setBounds(0, 40, 1300, 100);

                    tblDryer[i] = new JTable();
                    DataModel_dryer[i] = new EITLTableModel();

                    tblDryer[i].removeAll();
                    tblDryer[i].setModel(DataModel_dryer[i]);
                    //tblDryer.setBounds(10, 10, 500, 100);

                    tblDryer[i].setAutoResizeMode(0);

                    DataModel_dryer[i].addColumn("SrNo"); //0 - Read Only
                    DataModel_dryer[i].addColumn("UPN"); //1
                    DataModel_dryer[i].addColumn("Position Desc"); //2
                    DataModel_dryer[i].addColumn("Quality"); //3
                    DataModel_dryer[i].addColumn("Group"); //4
                    DataModel_dryer[i].addColumn("Value"); //5
                    DataModel_dryer[i].addColumn("Potential"); //6
                    DataModel_dryer[i].addColumn("Projected Qty"); //7
                    DataModel_dryer[i].addColumn("Projected Value"); //8
                    DataModel_dryer[i].addColumn("Qty " + String.valueOf(r.getInt("YEAR_FROM") - 1).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 1).substring(2, 4)); //9
                    DataModel_dryer[i].addColumn("Value " + String.valueOf(r.getInt("YEAR_FROM") - 1).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 1).substring(2, 4)); //10
                    DataModel_dryer[i].addColumn("Qty " + String.valueOf(r.getInt("YEAR_FROM") - 2).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 2).substring(2, 4)); //11
                    DataModel_dryer[i].addColumn("Value " + String.valueOf(r.getInt("YEAR_FROM") - 2).substring(2, 4) + "-" + String.valueOf(r.getInt("YEAR_TO") - 2).substring(2, 4)); //12
                    
                    DataModel_dryer[i].addColumn("WIP 31 Mar Qty"); //13
                    DataModel_dryer[i].addColumn("WIP 31 Mar Value"); //14
                    DataModel_dryer[i].addColumn("Stock 31 Mar Qty"); //15
                    DataModel_dryer[i].addColumn("Stock 31 Mar Value"); //16                      
                    
                    DataModel_dryer[i].addColumn("Stock Qty"); //13
                    DataModel_dryer[i].addColumn("Stock Value"); //14
                    DataModel_dryer[i].addColumn("WIP Qty"); //15
                    DataModel_dryer[i].addColumn("WIP Value"); //16
                    
                    DataModel_dryer[i].addColumn("Avg Life"); //13
                    DataModel_dryer[i].addColumn("Last Inv Dt"); //14
                    DataModel_dryer[i].addColumn("Projected MMYYYY"); //15
                    DataModel_dryer[i].addColumn("Incharge"); //24
                    DataModel_dryer[i].addColumn("Size Criteria"); //15
                    DataModel_dryer[i].addColumn("Party Group"); //15


                    tblDryer[i].getColumnModel().getColumn(0).setMaxWidth(30);
                    tblDryer[i].getColumnModel().getColumn(1).setMinWidth(90);
                    tblDryer[i].getColumnModel().getColumn(2).setMinWidth(80);
                    tblDryer[i].getColumnModel().getColumn(3).setMaxWidth(50);
                    tblDryer[i].getColumnModel().getColumn(4).setMinWidth(50);
                    tblDryer[i].getColumnModel().getColumn(5).setMaxWidth(60);
                    tblDryer[i].getColumnModel().getColumn(6).setMaxWidth(60);
                    tblDryer[i].getColumnModel().getColumn(8).setMinWidth(90);
                    tblDryer[i].getColumnModel().getColumn(9).setMinWidth(80);
                    tblDryer[i].getColumnModel().getColumn(10).setMinWidth(80);
                    tblDryer[i].getColumnModel().getColumn(11).setMinWidth(80);
                    tblDryer[i].getColumnModel().getColumn(12).setMinWidth(80);
                    tblDryer[i].getColumnModel().getColumn(12).setMinWidth(80);
                    tblDryer[i].getColumnModel().getColumn(14).setMinWidth(80);
                    tblDryer[i].getColumnModel().getColumn(15).setMinWidth(80);
                    tblDryer[i].getColumnModel().getColumn(16).setMinWidth(80);

                    tblDryer[i].addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent ke) {
                            for (int ji = 0; ji < DataModel_dryer[final_i1].getRowCount(); ji++) {
                                double value = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 5).toString());

                                double pq = 0;
                                try {
                                    pq = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 7).toString());
                                } catch (Exception nf) {
                                    pq = 0;
                                }
                                DataModel_dryer[final_i1].setValueAt(value * pq, ji, 8);
                            }
                        }
                    });

                    jScrollPane3 = new JScrollPane();
                    jScrollPane3.setViewportView(tblDryer[i]);

                    jScrollPane3.setBounds(0, 200, 1300, 150);
                    machine.add(jScrollPane3);

                    lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Press");
                    lblMachine.setBounds(0, 5, 200, 50);
                    machine.add(lblMachine);
                    lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Dryer");
                    lblMachine.setBounds(0, 150, 200, 50);
                    machine.add(lblMachine);
                    mmachine = r.getString("MACHINE_NO");
                    msr = msr1 = 1;
                    rowData = new Object[30];
                    if (r.getString("GROUP_NAME").equalsIgnoreCase("HDS") || r.getString("GROUP_NAME").equalsIgnoreCase("SDF")) {
                        rowData[0] = msr1;
                        rowData[1] = r.getString("UPN");
                        rowData[2] = r.getString("POSITION_DESC");
                        rowData[3] = r.getString("QUALITY_NO");
                        rowData[4] = r.getString("GROUP_NAME");
                        rowData[5] = r.getDouble("SELLING_PRICE");
                        rowData[6] = r.getDouble("POTENTIAL");
                        if (r.getInt("Q4") == 0) {
                            rowData[7] = "";
                        } else {
                            rowData[7] = r.getInt("Q4");
                        }
                        if (r.getInt("Q4NET_AMOUNT") == 0) {
                            rowData[8] = "";
                        } else {
                            rowData[8] = r.getInt("Q4NET_AMOUNT");
                        }
                        rowData[9] = r.getDouble("PREV_YEAR_QTY");
                        rowData[10] = r.getDouble("PREV_YEAR_VALUE");
                        rowData[11] = r.getDouble("PREV_PREV_YEAR_QTY");
                        rowData[12] = r.getDouble("PREV_PREV_YEAR_VALUE");
                        
                        rowData[13] = r.getDouble("WIP_31_MAR_QTY");
                        rowData[14] = r.getDouble("WIP_31_MAR_VALUE");
                        rowData[15] = r.getDouble("STOCK_31_MAR_QTY");
                        rowData[16] = r.getDouble("STOCK_31_MAR_VALUE");
                                                
                        rowData[17] = r.getDouble("STOCK_QTY");
                        rowData[18] = r.getDouble("STOCK_VALUE");
                        rowData[19] = r.getDouble("WIP_QTY");
                        rowData[20] = r.getDouble("WIP_VALUE");
                                                
                        rowData[21] = r.getDouble("AVG_LIFE");
                        rowData[22] = EITLERPGLOBAL.formatDate(r.getString("LAST_INVOICE_DATE"));
                        rowData[23] = EITLERPGLOBAL.formatDate(r.getString("PROJECTED_MMYYYY"));
                        rowData[24] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='"+r.getString("INCHARGE")+"'");
                        rowData[25] = EITLERPGLOBAL.formatDate(r.getString("SIZE_CRITERIA"));
                        rowData[26] = r.getString("PARTY_GROUP");
                        
                        
                        msr1++;
                        DataModel_dryer[i].addRow(rowData);
                    } else {
                        rowData[0] = msr;
                        rowData[1] = r.getString("UPN");
                        rowData[2] = r.getString("POSITION_DESC");
                        rowData[3] = r.getString("QUALITY_NO");
                        rowData[4] = r.getString("GROUP_NAME");
                        rowData[5] = r.getDouble("SELLING_PRICE");
                        rowData[6] = r.getDouble("POTENTIAL");
                        if (r.getInt("Q4") == 0) {
                            rowData[7] = "";
                        } else {
                            rowData[7] = r.getInt("Q4");
                        }
                        if (r.getInt("Q4NET_AMOUNT") == 0) {
                            rowData[8] = "";
                        } else {
                            rowData[8] = r.getInt("Q4NET_AMOUNT");
                        }
                        rowData[9] = r.getDouble("PREV_YEAR_QTY");
                        rowData[10] = r.getDouble("PREV_YEAR_VALUE");
                        rowData[11] = r.getDouble("PREV_PREV_YEAR_QTY");
                        rowData[12] = r.getDouble("PREV_PREV_YEAR_VALUE");
                        
                        rowData[13] = r.getDouble("WIP_31_MAR_QTY");
                        rowData[14] = r.getDouble("WIP_31_MAR_VALUE");
                        rowData[15] = r.getDouble("STOCK_31_MAR_QTY");
                        rowData[16] = r.getDouble("STOCK_31_MAR_VALUE");
                                                
                        rowData[17] = r.getDouble("STOCK_QTY");
                        rowData[18] = r.getDouble("STOCK_VALUE");
                        rowData[19] = r.getDouble("WIP_QTY");
                        rowData[20] = r.getDouble("WIP_VALUE");
                                                
                        rowData[21] = r.getDouble("AVG_LIFE");
                        rowData[22] = EITLERPGLOBAL.formatDate(r.getString("LAST_INVOICE_DATE"));
                        rowData[23] = EITLERPGLOBAL.formatDate(r.getString("PROJECTED_MMYYYY"));                        
                        rowData[24] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='"+r.getString("INCHARGE")+"'");
                        rowData[25] = EITLERPGLOBAL.formatDate(r.getString("SIZE_CRITERIA"));
                        rowData[26] = r.getString("PARTY_GROUP");
                        
                        msr++;
                        DataModel_Press[i].addRow(rowData);
                    }
                }
                r.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        SetFields(false);
        try {
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = BudgetEntry.getAttribute("DOC_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(ModuleId, DocNo);
            for (int i = 1; i <= List.size(); i++) {
                clsDocFlow ObjFlow = (clsDocFlow) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];
                //JOptionPane.showMessageDialog(null, "USER ID : "+ObjFlow.getAttribute("USER_ID").getVal());
                rowData[0] = Integer.toString(i);
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2] = (String) ObjFlow.getAttribute("STATUS").getObj();
                rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[4] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6] = (String) ObjFlow.getAttribute("REMARKS").getObj();

                DataModelApprovalStatus.addRow(rowData);
            }

            //Showing Audit Trial History
            FormatGridHS();
            //HashMap History = BudgetEntry.getHistoryList(EITLERPGLOBAL.gCompanyID + "", DocNo);
            HashMap History = clsBudgetEntry.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsBudgetEntry ObjHistory = (clsBudgetEntry) History.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = ObjHistory.getAttribute("ENTRY_DATE").getString();

                String ApprovalStatus = "";

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                    FinalApprovedBy = (int) ObjHistory.getAttribute("UPDATED_BY").getVal();
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }
                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }
                rowData[3] = ApprovalStatus;
                rowData[4] = ObjHistory.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjHistory.getAttribute("FROM_IP").getString();
                DataModelUpdateHistory.addRow(rowData);
            }
            //============================================================//
            //setSTATUS();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void FormatGridA() {
        DataModelApprovalStatus = new EITLTableModel();

        TableApprovalStatus.removeAll();
        TableApprovalStatus.setModel(DataModelApprovalStatus);

        //Set the table Readonly
        DataModelApprovalStatus.TableReadOnly(true);

        //Add the columns
        DataModelApprovalStatus.addColumn("Sr.");
        DataModelApprovalStatus.addColumn("User");
        DataModelApprovalStatus.addColumn("Status");
        DataModelApprovalStatus.addColumn("Department");
        DataModelApprovalStatus.addColumn("Received Date");
        DataModelApprovalStatus.addColumn("Action Date");
        DataModelApprovalStatus.addColumn("Remarks");

        TableApprovalStatus.setAutoResizeMode(TableApprovalStatus.AUTO_RESIZE_OFF);

    }

    private void SetMenuForRights() {
        // --- Add Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 600073, 6000731)) { //7008,70081
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 600073, 6000732)) { //7008,70082
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 600073, 6000733)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 600073, 6000734)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            //cmdPreview.setEnabled(false);
            //cmdPrint.setEnabled(false);
        }
    }

    private void SetupApproval() {
        /*// --- Hierarchy Change Rights Check --------
         if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,75)) {
         cmbHierarchy.setEnabled(true);
         }else {
         cmbHierarchy.setEnabled(false);
         }*/

        // select hold for default approval
        OpgHold.setSelected(true);

        if (EditMode == EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
            OpgReject.setEnabled(false);
        } else {
            cmbHierarchy.setEnabled(false);
        }

        //Set Default Hierarchy ID for User
        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);

        if (EditMode == EITLERPGLOBAL.ADD) {
            //lnFromUserId = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString());
            //lnFromUserId = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(ModuleId, FromUserID, BudgetEntry.getAttribute("DOC_NO").getString());

            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        if (clsHierarchy.CanSkip(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }

        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        } else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }

        //In Edit Mode Hierarchy and Reject Should be disabled
        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString() + "")) {
                OpgReject.setEnabled(false);
            }
        }

        if (EditMode == 0) {
            //Disable all hierarchy controls if not in Add/Edit Mode
            cmbHierarchy.setEnabled(false);
            txtFrom.setEnabled(false);
            txtFromRemarks.setEnabled(false);
            OpgApprove.setEnabled(false);
            OpgFinal.setEnabled(false);
            OpgReject.setEnabled(false);
            cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }
    }

    private void FormatGridHS() {
        DataModelUpdateHistory = new EITLTableModel();

        TableUpdateHistory.removeAll();
        TableUpdateHistory.setModel(DataModelUpdateHistory);

        //Set the table Readonly
        DataModelUpdateHistory.TableReadOnly(true);

        //Add the columns
        DataModelUpdateHistory.addColumn("Rev No.");
        DataModelUpdateHistory.addColumn("User");
        DataModelUpdateHistory.addColumn("Date");
        DataModelUpdateHistory.addColumn("Status");
        DataModelUpdateHistory.addColumn("Remarks");
        DataModelUpdateHistory.addColumn("From IP");

        TableUpdateHistory.setAutoResizeMode(TableUpdateHistory.AUTO_RESIZE_OFF);
    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbType ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId);

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId);
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (EITLERPGLOBAL.gNewUserID == clsFeltProductionApprovalFlow.getCreator(ModuleId, DOC_NO + "")) {
                List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + (ModuleId));
            } else {
                List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId);
            }
        }

        for (int i = 1; i <= List.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //
    }

    private void GenerateFromCombo() {
        //Generates Combo Boxes
        HashMap List = new HashMap();

        try {
            if (EditMode == EITLERPGLOBAL.ADD) {
                //----- Generate cmbType ------- //
                cmbToModel = new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);

                List = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
                for (int i = 1; i <= List.size(); i++) {
                    clsUser ObjUser = (clsUser) List.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();

                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    } else {
                        cmbToModel.addElement(aData);
                    }
                }
                //------------------------------ //
            } else {
                //----- Generate cmbType ------- //
                cmbToModel = new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);

                List = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, DOC_NO + "");
                for (int i = 1; i <= List.size(); i++) {
                    clsUser ObjUser = (clsUser) List.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
                    cmbToModel.addElement(aData);
                }
                //------------------------------ //
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGridApprovalStatus() {
        DataModelApprovalStatus = new EITLTableModel();

        TableApprovalStatus.removeAll();
        TableApprovalStatus.setModel(DataModelApprovalStatus);

        //Set the table Readonly
        DataModelApprovalStatus.TableReadOnly(true);

        //Add the columns
        DataModelApprovalStatus.addColumn("Sr.");
        DataModelApprovalStatus.addColumn("User");
        DataModelApprovalStatus.addColumn("Department");
        DataModelApprovalStatus.addColumn("Status");
        DataModelApprovalStatus.addColumn("Received Date");
        DataModelApprovalStatus.addColumn("Action Date");
        DataModelApprovalStatus.addColumn("Remarks");

        TableColumnModel tcm = TableApprovalStatus.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(3).setPreferredWidth(20);
        tcm.getColumn(4).setPreferredWidth(90);
        tcm.getColumn(5).setPreferredWidth(90);
    }

    private void FormatGridUpdateHistory() {
        DataModelUpdateHistory = new EITLTableModel();

        TableUpdateHistory.removeAll();
        TableUpdateHistory.setModel(DataModelUpdateHistory);

        //Set the table Readonly
        DataModelUpdateHistory.TableReadOnly(true);

        //Add the columns
        DataModelUpdateHistory.addColumn("Rev No.");
        DataModelUpdateHistory.addColumn("User");
        DataModelUpdateHistory.addColumn("Date");
        DataModelUpdateHistory.addColumn("Status");
        DataModelUpdateHistory.addColumn("Remarks");

        TableColumnModel tcm = TableUpdateHistory.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(2).setPreferredWidth(50);
        tcm.getColumn(3).setPreferredWidth(20);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        Tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        lblStatus1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        DOC_NO1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtyearfrom = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblyearto = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtpartycode = new javax.swing.JTextField();
        txtpartyname = new javax.swing.JLabel();
        tabTest = new javax.swing.JTabbedPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        Tab2 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        cmbHierarchy = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        txtFrom = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtFromRemarks = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        OpgApprove = new javax.swing.JRadioButton();
        OpgFinal = new javax.swing.JRadioButton();
        OpgReject = new javax.swing.JRadioButton();
        OpgHold = new javax.swing.JRadioButton();
        jLabel33 = new javax.swing.JLabel();
        cmbSendTo = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        txtToRemarks = new javax.swing.JTextField();
        cmdBackToTab0 = new javax.swing.JButton();
        cmdFromRemarksBig = new javax.swing.JButton();
        cmdNextToTab3 = new javax.swing.JButton();
        btnSendFAmail = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        StatusPanel = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableApprovalStatus = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableUpdateHistory = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        ToolBar = new javax.swing.JToolBar();
        cmdTop = new javax.swing.JButton();
        cmdBack = new javax.swing.JButton();
        cmdNext = new javax.swing.JButton();
        cmdLast = new javax.swing.JButton();
        cmdNew = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdFilter = new javax.swing.JButton();
        cmdPreview = new javax.swing.JButton();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        lblTitle1 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        Tab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabMouseClicked(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        jLabel17.setText("Budget Detail");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 90, 120, 20);

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(lblStatus1);
        lblStatus1.setBounds(0, 550, 920, 30);

        jLabel1.setText("Document No");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 20, 100, 20);

        DOC_NO1.setEditable(false);
        DOC_NO1.setText("BU000001");
        jPanel1.add(DOC_NO1);
        DOC_NO1.setBounds(140, 20, 130, 30);

        jLabel2.setText("Year");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(290, 20, 30, 20);

        txtyearfrom.setEditable(false);
        jPanel1.add(txtyearfrom);
        txtyearfrom.setBounds(320, 20, 60, 30);

        jLabel3.setText("YYYY");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(320, 5, 40, 16);

        lblyearto.setText("YYYY");
        jPanel1.add(lblyearto);
        lblyearto.setBounds(380, 25, 50, 20);

        jLabel4.setText("Party Code");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 70, 70, 20);

        txtpartycode.setEditable(false);
        txtpartycode.setToolTipText("");
        jPanel1.add(txtpartycode);
        txtpartycode.setBounds(140, 60, 130, 40);

        txtpartyname.setText("Party Name");
        jPanel1.add(txtpartyname);
        txtpartyname.setBounds(280, 60, 610, 40);
        jPanel1.add(tabTest);
        tabTest.setBounds(10, 120, 1260, 400);

        jButton1.setText("PartywiseProductwise Sales History");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(910, 10, 370, 40);

        jButton2.setText("Partywise Sales History");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(440, 10, 210, 40);

        jButton3.setText("Machinewise Sales History[Selected]");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(650, 10, 260, 40);

        Tab.addTab("Budget Entry", jPanel1);

        jPanel2.setLayout(null);

        Tab2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Tab2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab2FocusGained(evt);
            }
        });
        Tab2.setLayout(null);

        jLabel31.setText("Hierarchy ");
        Tab2.add(jLabel31);
        jLabel31.setBounds(10, 23, 66, 16);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        cmbHierarchy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbHierarchyFocusGained(evt);
            }
        });
        Tab2.add(cmbHierarchy);
        cmbHierarchy.setBounds(90, 20, 180, 28);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(10, 62, 56, 16);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFrom);
        txtFrom.setBounds(90, 60, 180, 28);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(10, 95, 62, 16);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(90, 95, 530, 28);

        jLabel36.setText("Your Action  ");
        Tab2.add(jLabel36);
        jLabel36.setBounds(10, 130, 81, 16);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        buttonGroup5.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgApproveItemStateChanged(evt);
            }
        });
        OpgApprove.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgApproveFocusGained(evt);
            }
        });
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 171, 23);

        buttonGroup5.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });
        OpgFinal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgFinalItemStateChanged(evt);
            }
        });
        OpgFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgFinalFocusGained(evt);
            }
        });
        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 136, 20);

        buttonGroup5.add(OpgReject);
        OpgReject.setText("Reject");
        OpgReject.setEnabled(false);
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });
        OpgReject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgRejectItemStateChanged(evt);
            }
        });
        OpgReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpgRejectActionPerformed(evt);
            }
        });
        OpgReject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgRejectFocusGained(evt);
            }
        });
        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 136, 20);

        buttonGroup5.add(OpgHold);
        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        OpgHold.setEnabled(false);
        OpgHold.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgHoldMouseClicked(evt);
            }
        });
        OpgHold.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgHoldItemStateChanged(evt);
            }
        });
        OpgHold.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgHoldFocusGained(evt);
            }
        });
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        Tab2.add(jPanel6);
        jPanel6.setBounds(90, 130, 180, 100);

        jLabel33.setText("Send To");
        Tab2.add(jLabel33);
        jLabel33.setBounds(10, 253, 60, 16);

        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(90, 250, 180, 28);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(10, 292, 60, 16);

        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(90, 290, 570, 28);

        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(450, 340, 102, 28);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(630, 95, 33, 21);

        cmdNextToTab3.setMnemonic('N');
        cmdNextToTab3.setText("Next >>");
        cmdNextToTab3.setToolTipText("Next Tab");
        cmdNextToTab3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab3ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNextToTab3);
        cmdNextToTab3.setBounds(560, 340, 102, 28);

        btnSendFAmail.setText("Send final approved mail");
        btnSendFAmail.setEnabled(false);
        btnSendFAmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFAmailActionPerformed(evt);
            }
        });
        Tab2.add(btnSendFAmail);
        btnSendFAmail.setBounds(546, 10, 200, 28);

        jPanel2.add(Tab2);
        Tab2.setBounds(10, 0, 760, 410);

        Tab.addTab("Approval", jPanel2);

        jPanel3.setLayout(null);

        StatusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusPanel.setLayout(null);

        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 16);

        TableApprovalStatus.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(TableApprovalStatus);

        StatusPanel.add(jScrollPane2);
        jScrollPane2.setBounds(0, 40, 694, 120);

        jLabel19.setText("Document Update History");
        StatusPanel.add(jLabel19);
        jLabel19.setBounds(10, 170, 182, 16);

        TableUpdateHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(TableUpdateHistory);

        StatusPanel.add(jScrollPane6);
        jScrollPane6.setBounds(10, 190, 540, 130);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        StatusPanel.add(cmdViewHistory);
        cmdViewHistory.setBounds(570, 170, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        StatusPanel.add(cmdNormalView);
        cmdNormalView.setBounds(570, 200, 132, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        StatusPanel.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(570, 230, 132, 24);

        txtAuditRemarks.setEnabled(false);
        StatusPanel.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(570, 260, 129, 28);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(570, 290, 130, 30);

        jPanel3.add(StatusPanel);
        StatusPanel.setBounds(10, 0, 790, 380);

        Tab.addTab("Status", jPanel3);

        getContentPane().add(Tab);
        Tab.setBounds(0, 80, 1310, 620);

        ToolBar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        ToolBar.setRollover(true);

        cmdTop.setToolTipText("First Record");
        cmdTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTopActionPerformed(evt);
            }
        });
        ToolBar.add(cmdTop);

        cmdBack.setToolTipText("Previous Record");
        cmdBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackActionPerformed(evt);
            }
        });
        ToolBar.add(cmdBack);

        cmdNext.setToolTipText("Next Record");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });
        ToolBar.add(cmdNext);

        cmdLast.setToolTipText("Last Record");
        cmdLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLastActionPerformed(evt);
            }
        });
        ToolBar.add(cmdLast);

        cmdNew.setToolTipText("New Record");
        cmdNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNewActionPerformed(evt);
            }
        });
        ToolBar.add(cmdNew);

        cmdEdit.setToolTipText("Edit");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });
        ToolBar.add(cmdEdit);

        cmdDelete.setToolTipText("Delete");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });
        ToolBar.add(cmdDelete);

        cmdSave.setToolTipText("Save");
        cmdSave.setEnabled(false);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        ToolBar.add(cmdSave);

        cmdCancel.setToolTipText("Cancel");
        cmdCancel.setEnabled(false);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        ToolBar.add(cmdCancel);

        cmdFilter.setToolTipText("Find");
        cmdFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFilterActionPerformed(evt);
            }
        });
        ToolBar.add(cmdFilter);

        cmdPreview.setToolTipText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });
        ToolBar.add(cmdPreview);

        cmdPrint.setToolTipText("Print");
        ToolBar.add(cmdPrint);

        cmdExit.setToolTipText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        ToolBar.add(cmdExit);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 930, 40);

        lblTitle1.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle1.setText("Budget Entry");
        lblTitle1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle1.setOpaque(true);
        getContentPane().add(lblTitle1);
        lblTitle1.setBounds(0, 40, 930, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged

        //SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //JOptionPane.showMessageDialog(null, "On State Change SelHierarchyId : "+SelHierarchyID);
        GenerateSendToCombo();

        if (clsHierarchy.CanSkip((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }

        if (clsHierarchy.CanFinalApprove((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
                OpgFinal.setEnabled(true);
            }
        } else {
            OpgApprove.setEnabled(false);
            OpgApprove.setSelected(false);
        }

        if (clsHierarchy.IsCreator((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            OpgApprove.setEnabled(true);
            OpgReject.setEnabled(false);
            OpgReject.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        lblStatus1.setText("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //JOptionPane.showMessageDialog(null, "SelHierarchyId : "+SelHierarchyID);
        DOC_NO = DOC_NO1.getText();
        cmbSendTo.setEnabled(true);
        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedSendToCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(ModuleId, DOC_NO + "")) {
                cmbSendTo.setEnabled(true);
                txtToRemarks.setEnabled(true);
                txtFromRemarks.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }
        if (cmbSendTo.getItemCount() <= 0) {
            GenerateSendToCombo();
        }

        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);
        OpgApprove.setSelected(true);
        OpgHold.setSelected(false);
        //txtToRemarks.setEnabled(false);
        if (!OpgApprove.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgApproveMouseClicked
    private void GenerateRejectedSendToCombo() {
        HashMap hmRejectedSendToList = new HashMap();

        cmbSendToModel = new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbSendToModel);

        //Now Add other hierarchy Users
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        hmRejectedSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, true);

        for (int i = 1; i <= hmRejectedSendToList.size(); i++) {
            clsUser ObjUser = (clsUser) hmRejectedSendToList.get(Integer.toString(i));

            ComboData aData = new ComboData();
            aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
            aData.Text = ObjUser.getAttribute("USER_NAME").getString();

            boolean IncludeUser = false;
            //Decide to include user or not
            if (EditMode == EITLERPGLOBAL.EDIT) {

                if (OpgApprove.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(ModuleId, DOC_NO + "", ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    //JOptionPane.showMessageDialog(null, "Module Id :"+ModuleId+", DOC No : "+sorder_no+", User Id : "+ObjUser.getAttribute("USER_ID").getInt()+", New user Id "+SDMLERPGLOBAL.gNewUserID);
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(ModuleId, DOC_NO + "", ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                    // JOptionPane.showMessageDialog(null, "IncludeUser = "+IncludeUser);
                }

                if (IncludeUser && (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID)) {
                    cmbSendToModel.addElement(aData);
                }
            } else {
                if ((ObjUser.getAttribute("USER_ID").getInt()) != EITLERPGLOBAL.gNewUserID) {
                    cmbSendToModel.addElement(aData);
                }
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            int Creator = clsFeltProductionApprovalFlow.getCreator(ModuleId, DOC_NO + "");
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }
    }
    private void OpgApproveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgApproveItemStateChanged

    }//GEN-LAST:event_OpgApproveItemStateChanged

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        lblStatus1.setText("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(true);
        OpgReject.setSelected(false);
        OpgHold.setSelected(false);

        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgFinalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgFinalItemStateChanged

    }//GEN-LAST:event_OpgFinalItemStateChanged

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained

    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgRejectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgRejectItemStateChanged
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectItemStateChanged

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained

    }//GEN-LAST:event_OpgRejectFocusGained

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);
        OpgHold.setSelected(true);
    }//GEN-LAST:event_OpgHoldMouseClicked


    private void OpgHoldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgHoldItemStateChanged

    }//GEN-LAST:event_OpgHoldItemStateChanged

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained

    }//GEN-LAST:event_OpgHoldFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained

    }//GEN-LAST:event_cmbSendToFocusGained

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained

    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmdBackToTab0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab0ActionPerformed
        Tab.setSelectedIndex(0);
    }//GEN-LAST:event_cmdBackToTab0ActionPerformed

    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed

    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed

    private void cmdNextToTab3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab3ActionPerformed
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNextToTab3ActionPerformed

    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained

    }//GEN-LAST:event_Tab2FocusGained

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        String DocNo = DOC_NO1.getText();
        //BudgetEntry.ShowHistory(DocNo);
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        //BudgetEntry.HistoryView = false;
        //BudgetEntry.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        if (TableUpdateHistory.getRowCount() > 0 && TableUpdateHistory.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableUpdateHistory.getValueAt(TableUpdateHistory.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabMouseClicked

    }//GEN-LAST:event_TabMouseClicked

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        MoveNext();

    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Delete();
        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        // REPORT QUERY : SELECT A.DOC_NO,A.PIECE_NO,A.DL_REMARK,B.PR_MACHINE_NO,B.PR_POSITION_NO,B.PR_LENGTH,B.PR_WIDTH,B.PR_GSM,B.PR_GROUP,B.PR_STYLE,B.PR_SQMTR,B.PR_STYLE,B.PR_SYN_PER,B.PR_PIECE_REMARK,B.PR_PIECE_STAGE,B.PR_PRODUCT_CODE,B.PR_PARTY_CODE,B.PR_PO_NO,B.PR_PO_DATE,B.PR_REFERENCE_DATE,B.PR_ORDER_REMARK,B.PR_ORDER_DATE FROM  PRODUCTION.FELT_SALES_DIVERSION_LIST_APPROVAL A, PRODUCTION.FELT_SALES_PIECE_REGISTER B;
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void OpgRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpgRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpgRejectActionPerformed

    private void btnSendFAmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFAmailActionPerformed
        // TODO add your handling code here:
        //System.out.println("Sel Hierarchy : "+SelHierarchyID);

        System.out.println("finishing approved = " + BudgetEntry.getAttribute("APPROVED").getInt());

        if (BudgetEntry.getAttribute("APPROVED").getInt() == 1) {
            int value = JOptionPane.showConfirmDialog(this, " Are you sure? You want to send Final Approved mail to all users? ", "Confirmation Alert!", JOptionPane.YES_NO_OPTION);
            System.out.println("VALUE = " + value);
            if (value == 0) {
                try {
                    String DOC_NO = DOC_NO1.getText();
                    String DOC_DATE = BudgetEntry.getAttribute("CREATED_DATE").getString();
                    String Party_Code = "multiple";
                    int Hierarchy = (int) BudgetEntry.getAttribute("HIERARCHY_ID").getInt();

                    System.out.println("ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true " + 603 + "," + DOC_NO + "," + DOC_DATE + "," + Party_Code + "," + EITLERPGLOBAL.gNewUserID + "," + Hierarchy + "," + true);
                    System.out.println("Final Approved By : " + FinalApprovedBy);
                    String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, FinalApprovedBy, Hierarchy, true, EITLERPGLOBAL.gNewUserID);
                    System.out.println("Send Mail Responce : " + responce);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_btnSendFAmailActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      //Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.Budget.FrmBudgetReport", true);
      //EITLERP.FeltSales.Budget.FrmBudgetReport ObjBudgetReport= (EITLERP.FeltSales.Budget.FrmBudgetReport) ObjLoader.getObj();
       String PartyCode = "";
       PartyCode = txtpartycode.getText();
       
      String mc= Integer.toString(tabTest.getSelectedIndex()+1);
      int i=tabTest.getSelectedIndex();
      String title = tabTest.getTitleAt(i).substring(8); 
      
        System.out.println(title); 
      
        System.out.println(mc);
              
       
       AppletFrame aFrame = new AppletFrame("Budget Report");
       aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetReport","Budget Report");
       EITLERP.FeltSales.Budget.FrmBudgetReport ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetReport) aFrame.ObjApplet; 
       //ObjItem.GetParameter(PartyCode, machineno); 
       ObjItem.GetParameter(PartyCode, title); 
              
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       AppletFrame aFrame = new AppletFrame("Budget Report");
       aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetReport","Budget Report");
       EITLERP.FeltSales.Budget.FrmBudgetReport ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetReport) aFrame.ObjApplet; 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       AppletFrame aFrame = new AppletFrame("Budget Report");
       aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetReport","Budget Report");
       EITLERP.FeltSales.Budget.FrmBudgetReport ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetReport) aFrame.ObjApplet; 
    }//GEN-LAST:event_jButton1ActionPerformed
    private void MoveFirst() {
        BudgetEntry.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        BudgetEntry.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        BudgetEntry.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        BudgetEntry.MoveLast();
        DisplayData();
    }

    private void Find() {
//        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.Budget.frmFindFeltManualBudget", true);
//        frmFindFeltManualBudget ObjFindfeltPieceDivision = (frmFindFeltManualBudget) ObjLoader.getObj();
//
//        if (ObjFindfeltPieceDivision.Cancelled == false) {
//            if (!BudgetEntry.Filter(ObjFindfeltPieceDivision.stringFindQuery)) {
//                JOptionPane.showMessageDialog(this, "No records found.", "Find Felt Division", JOptionPane.YES_OPTION);
//            }
//            MoveLast();
//        }
        
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.Budget.frmFindBudgetEntry", true);
        frmFindBudgetEntry ObjFindfeltPieceDivision = (frmFindBudgetEntry) ObjLoader.getObj();

        if (ObjFindfeltPieceDivision.Cancelled == false) {
            if (!BudgetEntry.Filter(ObjFindfeltPieceDivision.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.", "Find Felt Division", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    public void FindEx(int pCompanyID, String docno) {
        BudgetEntry.Filter(" DOC_NO='" + docno + "'");
        BudgetEntry.MoveFirst();
        DisplayData();
    }

    public void FindWaiting() {
        BudgetEntry.Filter(" DOC_NO IN (SELECT DISTINCT PRODUCTION.FELT_BUDGET_DETAIL.DOC_NO FROM PRODUCTION.FELT_BUDGET_DETAIL, PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_BUDGET_DETAIL.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=" + ModuleId + " AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    private void Add() {

//        if (!EITLERPGLOBAL.YearIsOpen) {
//            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
//            return;
//        }
//        EditMode = EITLERPGLOBAL.ADD;
////        cmdimport.setVisible(true);
//
//        SetFields(true);
//        DisableToolbar();
//
//        SetupApproval();
//
//        clearFields();
//
//        SelectFirstFree aList = new SelectFirstFree();
//        aList.ModuleID = ModuleId;
//        aList.FirstFreeNo = 236;
//        FFNo = aList.FirstFreeNo;
//        DOC_NO1.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, false));
//        DOC_NO1.setText( DOC_NO1.getText().substring(2));
//        lblTitle1.setText("Budget Manual Entry - " + DOC_NO1.getText());
//        txtyearfrom.requestFocus();
//        //import_data();
    }

    private void Save() {

//        if (chk_import_data())
        {
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + txtpartycode.getText() + "' ")) {
                JOptionPane.showMessageDialog(null, "Party closed in Party Master.");
                return;
            }

//            for (int j = 0; j < Table.getRowCount(); j++) {
//                String machineNo = ((String) Table.getValueAt(j, 3)).trim();
//                String positionNo = ((String) Table.getValueAt(j, 4)).trim();
//
//                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MACHINE_CLOSE_IND=1 AND MM_PARTY_CODE='" + txtpartycode.getText() + "' AND MM_MACHINE_NO='" + machineNo + "' ")) {
//                    JOptionPane.showMessageDialog(null, "Party Machine closed in Machine Master at Row : " + (j + 1));
//                    return;
//                } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE POSITION_CLOSE_IND=1 AND MM_PARTY_CODE='" + txtpartycode.getText() + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "' ")) {
//                    JOptionPane.showMessageDialog(null, "Party Machine Position closed in Machine Master at Row : " + (j + 1));
//                    return;
//                }
//
//            }
//
//            if (Table.getRowCount() <= 0) {
//                JOptionPane.showMessageDialog(this, "Enter Budget Details Before Saving.", "ERROR", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
            if (txtyearfrom.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(this, "Please Enter Year...", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SetData();

            if (cmbHierarchy.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Select the hierarchy.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
                JOptionPane.showMessageDialog(this, "Select the Approval Action.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Enter the remarks for rejection", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
                JOptionPane.showMessageDialog(this, "Select the user, to whom rejected document to be send", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //BudgetEntry.LoadData();
            if (EditMode == EITLERPGLOBAL.ADD) {
                if (BudgetEntry.Insert()) {
                    SelectFirstFree aList = new SelectFirstFree();
                    aList.ModuleID = ModuleId;
                    aList.FirstFreeNo = 236;
                    FFNo = aList.FirstFreeNo;
                    clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, true);

                    if (OpgFinal.isSelected()) {
                        try {

                            String DOC_NO = DOC_NO1.getText();
                            String DOC_DATE = EITLERPGLOBAL.getCurrentDate();
                            String Party_Code = "multiple";

                            //update_budget(DOC_NO);
                            String sql="";
                            sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                    + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + DOC_NO + "')";
                            System.out.println(sql);
            data.Execute(sql);
            sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA,Q4NET_AMOUNT)  "
                    + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1, "
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA,Q4NET_AMOUNT"
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL "
                    + "WHERE DOC_NO='" + DOC_NO + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                            System.out.println(sql);
            data.Execute(sql);

                            String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true, 0);
                            System.out.println("Send Mail Responce : " + responce);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    DisplayData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + BudgetEntry.LastError, " SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (EditMode == EITLERPGLOBAL.EDIT) {
                if (BudgetEntry.Update()) {
                    if (OpgFinal.isSelected()) {
                        try {

                            String DOC_NO = DOC_NO1.getText();
                            String DOC_DATE = EITLERPGLOBAL.getCurrentDate();
                            String Party_Code = "multiple";

                            //update_budget(DOC_NO);
                            String sql="";
                            sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                    + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + DOC_NO + "')";
            data.Execute(sql);
            sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA,Q4NET_AMOUNT,PARTY_GROUP)  "
                    + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1, "
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA,Q4NET_AMOUNT,PARTY_GROUP"
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL "
                    + "WHERE DOC_NO='" + DOC_NO + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
            data.Execute(sql);
                            

                            String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true, 0);
                            System.out.println("Send Mail Responce : " + responce);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    DisplayData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured while saving editing. Error is " + BudgetEntry.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            EditMode = 0;
            SetFields(false);
            EnableToolbar();
            SetMenuForRights();
            try {
                if (PENDING_DOCUMENT) {
                    frmPA.RefreshView();
                    PENDING_DOCUMENT = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//            else {
//            JOptionPane.showMessageDialog(this, "Invalid Status....");
//        }
    }

    private void Cancel() {
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();

    }

    private void EnableToolbar() {
        cmdTop.setEnabled(true);
        cmdBack.setEnabled(true);
        cmdNext.setEnabled(true);
        cmdLast.setEnabled(true);
        cmdNew.setEnabled(true);
        cmdEdit.setEnabled(true);
        cmdDelete.setEnabled(true);
        cmdSave.setEnabled(false);
        cmdCancel.setEnabled(false);
        cmdFilter.setEnabled(true);
        cmdPreview.setEnabled(true);
        cmdPrint.setEnabled(true);
        cmdExit.setEnabled(true);
    }

    private void Edit() {

        String productionDocumentNo = (String) BudgetEntry.getAttribute("DOC_NO").getString();

        if (BudgetEntry.IsEditable(EITLERPGLOBAL.gCompanyID, productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            DisableToolbar();
            GenerateCombos();
            //GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            // SetupApproval();
            //ReasonResetReadonly();
            //cmbOrderReason.setEnabled(false);
            //if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, productionDocumentNo)) {
                SetFields(true);
            //} else {

             //   EnableApproval();
            //}
        } else {
            JOptionPane.showMessageDialog(null, "You cannot edit this record. \n It is either approved/rejected or waiting approval for other user");
        }

    }

// find rate update by doc no
    public void Find(String docNo) {
        BudgetEntry.Filter(" DOC_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    private void Delete() {

    }

    private void GenerateSendToCombo() {
        HashMap hmSendToList = new HashMap();
        try {
            cmbSendToModel = new EITLComboModel();
            cmbSendTo.removeAllItems();
            cmbSendTo.setModel(cmbSendToModel);
            if (EditMode == EITLERPGLOBAL.ADD) {
                hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();

                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    } else {
                        cmbSendToModel.addElement(aData);
                    }
                }
            } else {
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, DOC_NO1.getText());
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
                    cmbSendToModel.addElement(aData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EnableApproval() {
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        txtToRemarks.setEnabled(true);
        SetupApproval();
    }

    private void SetFields(boolean pStat) {

        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        txtyearfrom.setEnabled(pStat);
        DOC_NO1.setEnabled(pStat);
        txtpartycode.setEnabled(pStat);        
        for (int p = 0; p < tabTest.getTabCount(); p++) {
            tblPress[p].setEnabled(pStat);
            tblDryer[p].setEnabled(pStat);            
        }
//        Table.setEnabled(pStat);

        if (!OpgReject.isSelected()) {
            SetupApproval();
        }
    }

    private void DisableToolbar() {
        cmdTop.setEnabled(false);
        cmdBack.setEnabled(false);
        cmdNext.setEnabled(false);
        cmdLast.setEnabled(false);
        cmdNew.setEnabled(false);
        cmdEdit.setEnabled(false);
        cmdDelete.setEnabled(false);
        cmdSave.setEnabled(true);
        cmdCancel.setEnabled(true);
        cmdFilter.setEnabled(false);
        cmdPreview.setEnabled(false);
        cmdPrint.setEnabled(false);
        cmdExit.setEnabled(false);

    }

    private void SetData() {

//      
        BudgetEntry.setAttribute("DOC_NO", DOC_NO1.getText());

        DOC_NO = DOC_NO1.getText();

        BudgetEntry.setAttribute("DOC_NO", DOC_NO);
        BudgetEntry.setAttribute("MODULE_ID", ModuleId);
        BudgetEntry.setAttribute("USER_ID", EITLERPGLOBAL.gNewUserID);

        BudgetEntry.setAttribute("REJECTED_REMARKS", txtToRemarks.getText());
        BudgetEntry.setAttribute("REMARKS", "");
        BudgetEntry.setAttribute("APPROVAL_STATUS", "");
        BudgetEntry.setAttribute("APPROVER_REMARKS", txtFromRemarks.getText());
        BudgetEntry.setAttribute("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());

        //----- Update Approval Specific Fields -----------//
        BudgetEntry.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        BudgetEntry.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        BudgetEntry.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        BudgetEntry.setAttribute("FROM_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            BudgetEntry.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            BudgetEntry.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            BudgetEntry.setAttribute("APPROVAL_STATUS", "R");
            BudgetEntry.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            BudgetEntry.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            BudgetEntry.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            BudgetEntry.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            BudgetEntry.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            BudgetEntry.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            BudgetEntry.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            BudgetEntry.setAttribute("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        BudgetEntry.colMRItems.clear();
        for (int p = 0; p < tabTest.getTabCount(); p++) {
            tblPress[p].setEnabled(false);
            tblDryer[p].setEnabled(false);            
            for (int m = 0; m < tblPress[p].getRowCount(); m++) {
                clsBudgetEntryItem ObjItem = new clsBudgetEntryItem();
                
                ObjItem.setAttribute("DOC_NO", DOC_NO1.getText());
                ObjItem.setAttribute("YEAR_FROM", txtyearfrom.getText());
                ObjItem.setAttribute("YEAR_TO", lblyearto.getText());
                ObjItem.setAttribute("PARTY_CODE", txtpartycode.getText());
                ObjItem.setAttribute("PARTY_NAME", txtpartyname.getText());                
                ObjItem.setAttribute("MACHINE_NO",data.getStringValueFromDB("SELECT MACHINE_NO FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE UPN='"+tblPress[p].getValueAt(m, 1).toString()+"'"));
                ObjItem.setAttribute("POSITION_NO",data.getStringValueFromDB("SELECT POSITION_NO FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE UPN='"+tblPress[p].getValueAt(m, 1).toString()+"'"));
                
                ObjItem.setAttribute("UPN", tblPress[p].getValueAt(m, 1).toString());
                ObjItem.setAttribute("POSITION_DESC", tblPress[p].getValueAt(m, 2).toString());
                ObjItem.setAttribute("QUALITY_NO", tblPress[p].getValueAt(m, 3).toString());
                ObjItem.setAttribute("GROUP_NAME", tblPress[p].getValueAt(m, 4).toString());
                System.out.println(Double.parseDouble(tblPress[p].getValueAt(m, 5).toString()));
                ObjItem.setAttribute("SELLING_PRICE", Double.parseDouble(tblPress[p].getValueAt(m, 5).toString()));
                ObjItem.setAttribute("POTENTIAL", Double.parseDouble(tblPress[p].getValueAt(m, 6).toString()));
                if(tblPress[p].getValueAt(m, 7).toString().equals("")){
                    ObjItem.setAttribute("Q4", 0.00);
                }else{
                    ObjItem.setAttribute("Q4", Double.parseDouble(tblPress[p].getValueAt(m, 7).toString()));              }                                                 
                
                if(tblPress[p].getValueAt(m, 8).toString().equals("")){
                    ObjItem.setAttribute("Q4NET_AMOUNT", 0.00);
                }else{
                    ObjItem.setAttribute("Q4NET_AMOUNT", Double.parseDouble(tblPress[p].getValueAt(m, 8).toString()));                
                }                             
                //System.out.println(Double.parseDouble(tblDryer[p].getValueAt(m, 8).toString()));
                
                ObjItem.setAttribute("PREV_PREV_YEAR_QTY", Double.parseDouble(tblPress[p].getValueAt(m, 9).toString()));
                ObjItem.setAttribute("PREV_PREV_YEAR_VALUE", Double.parseDouble(tblPress[p].getValueAt(m, 10).toString()));
                ObjItem.setAttribute("PREV_YEAR_QTY", Double.parseDouble(tblPress[p].getValueAt(m, 11).toString()));
                ObjItem.setAttribute("PREV_YEAR_VALUE", Double.parseDouble(tblPress[p].getValueAt(m, 12).toString()));
                
                ObjItem.setAttribute("WIP_31_MAR_QTY", Double.parseDouble(tblPress[p].getValueAt(m, 13).toString()));
                ObjItem.setAttribute("WIP_31_MAR_VALUE", Double.parseDouble(tblPress[p].getValueAt(m, 14).toString()));                
                ObjItem.setAttribute("STOCK_31_MAR_QTY", Double.parseDouble(tblPress[p].getValueAt(m, 15).toString()));
                ObjItem.setAttribute("STOCK_31_MAR_VALUE", Double.parseDouble(tblPress[p].getValueAt(m, 16).toString()));
                
                ObjItem.setAttribute("STOCK_QTY", Double.parseDouble(tblPress[p].getValueAt(m, 17).toString()));
                ObjItem.setAttribute("STOCK_VALUE", Double.parseDouble(tblPress[p].getValueAt(m, 18).toString()));                
                ObjItem.setAttribute("WIP_QTY", Double.parseDouble(tblPress[p].getValueAt(m, 19).toString()));
                ObjItem.setAttribute("WIP_VALUE", Double.parseDouble(tblPress[p].getValueAt(m, 20).toString()));
                
                ObjItem.setAttribute("AVG_LIFE", Double.parseDouble(tblPress[p].getValueAt(m, 21).toString()));
                ObjItem.setAttribute("LAST_INVOICE_DATE", tblPress[p].getValueAt(m, 22).toString());
                ObjItem.setAttribute("PROJECTED_MMYYYY", tblPress[p].getValueAt(m, 23).toString());
                ObjItem.setAttribute("INCHARGE", data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_NAME='"+tblPress[p].getValueAt(m, 24).toString().trim()+"'"));
                ObjItem.setAttribute("SIZE_CRITERIA", tblPress[p].getValueAt(m, 25).toString());
                ObjItem.setAttribute("PARTY_GROUP", tblPress[p].getValueAt(m, 26).toString());
                
                BudgetEntry.colMRItems.put(Integer.toString(BudgetEntry.colMRItems.size() + 1), ObjItem);
                
            }
            
            for (int m = 0; m < tblDryer[p].getRowCount(); m++) {
                clsBudgetEntryItem ObjItem = new clsBudgetEntryItem();
                
                ObjItem.setAttribute("DOC_NO", DOC_NO1.getText());
                ObjItem.setAttribute("YEAR_FROM", txtyearfrom.getText());
                ObjItem.setAttribute("YEAR_TO", lblyearto.getText());
                
                ObjItem.setAttribute("PARTY_CODE", txtpartycode.getText());
                ObjItem.setAttribute("PARTY_NAME", txtpartyname.getText());
                ObjItem.setAttribute("MACHINE_NO",data.getStringValueFromDB("SELECT MACHINE_NO FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE UPN='"+tblDryer[p].getValueAt(m, 1).toString()+"'"));
                ObjItem.setAttribute("POSITION_NO",data.getStringValueFromDB("SELECT POSITION_NO FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE UPN='"+tblDryer[p].getValueAt(m, 1).toString()+"'"));
                
                ObjItem.setAttribute("UPN", tblDryer[p].getValueAt(m, 1).toString());
                ObjItem.setAttribute("POSITION_DESC", tblDryer[p].getValueAt(m, 2).toString());
                ObjItem.setAttribute("QUALITY_NO", tblDryer[p].getValueAt(m, 3).toString());
                ObjItem.setAttribute("GROUP_NAME", tblDryer[p].getValueAt(m, 4).toString());
                ObjItem.setAttribute("SELLING_PRICE", tblDryer[p].getValueAt(m, 5).toString());
                ObjItem.setAttribute("POTENTIAL", Double.parseDouble(tblDryer[p].getValueAt(m, 6).toString()));
                
                
                if(tblDryer[p].getValueAt(m, 7).toString().equals("")){
                    ObjItem.setAttribute("Q4", 0.00);
                }else{
                    ObjItem.setAttribute("Q4", Double.parseDouble(tblDryer[p].getValueAt(m, 7).toString()));
                }                                                 
                if(tblDryer[p].getValueAt(m, 8).toString().equals("")){
                    ObjItem.setAttribute("Q4NET_AMOUNT", 0.00);
                }else{
                    ObjItem.setAttribute("Q4NET_AMOUNT", Double.parseDouble(tblDryer[p].getValueAt(m, 8).toString()));                
                }
                
                //ObjItem.setAttribute("Q4NET_AMOUNT", Double.parseDouble(tblDryer[p].getValueAt(m, 8).toString()));                                
                ObjItem.setAttribute("PREV_PREV_YEAR_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, 9).toString()));
                ObjItem.setAttribute("PREV_PREV_YEAR_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 10).toString()));
                ObjItem.setAttribute("PREV_YEAR_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, 11).toString()));
                ObjItem.setAttribute("PREV_YEAR_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 12).toString()));
                
                ObjItem.setAttribute("WIP_31_MAR_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, 13).toString()));
                ObjItem.setAttribute("WIP_31_MAR_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 14).toString()));                
                ObjItem.setAttribute("STOCK_31_MAR_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, 15).toString()));
                ObjItem.setAttribute("STOCK_31_MAR_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 16).toString()));                
                
                ObjItem.setAttribute("STOCK_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, 17).toString()));
                ObjItem.setAttribute("STOCK_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 18).toString()));                
                ObjItem.setAttribute("WIP_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, 19).toString()));
                ObjItem.setAttribute("WIP_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 20).toString()));
                
                ObjItem.setAttribute("AVG_LIFE", Double.parseDouble(tblDryer[p].getValueAt(m, 21).toString()));
                ObjItem.setAttribute("LAST_INVOICE_DATE", tblDryer[p].getValueAt(m, 22).toString());
                ObjItem.setAttribute("PROJECTED_MMYYYY", tblDryer[p].getValueAt(m, 23).toString());
                ObjItem.setAttribute("INCHARGE", data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_NAME='"+tblDryer[p].getValueAt(m, 24).toString().trim()+"'"));
                ObjItem.setAttribute("SIZE_CRITERIA", tblDryer[p].getValueAt(m, 25).toString());
                ObjItem.setAttribute("PARTY_GROUP", tblDryer[p].getValueAt(m, 26).toString());
                
                BudgetEntry.colMRItems.put(Integer.toString(BudgetEntry.colMRItems.size() + 1), ObjItem);
                
                
                
                
            }
        }
        
        //double mq1, mq2, mq3, mq4;
      //for (int m = 0; m < tblPress[i].getRowCount(); m++) {
        //for (int m = 0; m < Table.getRowCount(); m++) {
//
//            try {
//                mq1 = Double.parseDouble(Table.getValueAt(m, 22).toString());
//            } catch (Exception e) {
//                mq1 = 0;
//            }
//            try {
//                mq2 = Double.parseDouble(Table.getValueAt(m, 25).toString());
//            } catch (Exception e) {
//                mq2 = 0;
//            }
//            try {
//                mq3 = Double.parseDouble(Table.getValueAt(m, 28).toString());
//            } catch (Exception e) {
//                mq3 = 0;
//            }
//            try {
//                mq4 = Double.parseDouble(Table.getValueAt(m, 31).toString());
//            } catch (Exception e) {
//                mq4 = 0;
//            }
//
//            if (mq1 > 0 || mq2 > 0 || mq3 > 0 || mq4 > 0) {
//                clsBudgetEntryItem ObjItem = new clsBudgetEntryItem();
//
//                ObjItem.setAttribute("DOC_NO", DOC_NO1.getText());
//                ObjItem.setAttribute("YEAR_FROM", txtyearfrom.getText());
//                ObjItem.setAttribute("YEAR_TO", lblyearto.getText());
//                ObjItem.setAttribute("PARTY_CODE", Table.getValueAt(m, 1).toString());
//                ObjItem.setAttribute("PARTY_NAME", Table.getValueAt(m, 2).toString());
//                ObjItem.setAttribute("MACHINE_NO", Table.getValueAt(m, 3).toString());
//                ObjItem.setAttribute("POSITION_NO", Table.getValueAt(m, 4).toString());
//                ObjItem.setAttribute("POSITION_DESC", Table.getValueAt(m, 5).toString());
//                ObjItem.setAttribute("STYLE", Table.getValueAt(m, 6).toString());
//                ObjItem.setAttribute("PRESS_LENGTH", Double.parseDouble(Table.getValueAt(m, 7).toString()));
//                ObjItem.setAttribute("PRESS_WIDTH", Double.parseDouble(Table.getValueAt(m, 8).toString()));
//                ObjItem.setAttribute("PRESS_GSM", Double.parseDouble(Table.getValueAt(m, 9).toString()));
//                ObjItem.setAttribute("PRESS_WEIGHT", Double.parseDouble(Table.getValueAt(m, 10).toString()));
//                ObjItem.setAttribute("PRESS_SQMTR", Double.parseDouble(Table.getValueAt(m, 11).toString()));
//                ObjItem.setAttribute("DRY_LENGTH", Double.parseDouble(Table.getValueAt(m, 12).toString()));
//                ObjItem.setAttribute("DRY_WIDTH", Double.parseDouble(Table.getValueAt(m, 13).toString()));
//                ObjItem.setAttribute("DRY_SQMTR", Double.parseDouble(Table.getValueAt(m, 14).toString()));
//                ObjItem.setAttribute("DRY_WEIGHT", Double.parseDouble(Table.getValueAt(m, 15).toString()));
//                ObjItem.setAttribute("QUALITY_NO", Table.getValueAt(m, 16).toString());
//                ObjItem.setAttribute("GROUP_NAME", Table.getValueAt(m, 17).toString());
//                ObjItem.setAttribute("SELLING_PRICE", Double.parseDouble(Table.getValueAt(m, 18).toString()));
//                ObjItem.setAttribute("SPL_DISCOUNT", Double.parseDouble(Table.getValueAt(m, 19).toString()));
//                ObjItem.setAttribute("WIP", Double.parseDouble(Table.getValueAt(m, 20).toString()));
//                ObjItem.setAttribute("STOCK", Double.parseDouble(Table.getValueAt(m, 21).toString()));
//                try {
//                    ObjItem.setAttribute("Q1", Double.parseDouble(Table.getValueAt(m, 22).toString()));
//                    ObjItem.setAttribute("Q1KG", Double.parseDouble(Table.getValueAt(m, 23).toString()));
//                    ObjItem.setAttribute("Q1SQMTR", Double.parseDouble(Table.getValueAt(m, 24).toString()));
//                } catch (Exception q1) {
//                    ObjItem.setAttribute("Q1", 0.0);
//                    ObjItem.setAttribute("Q1KG", 0.0);
//                    ObjItem.setAttribute("Q1SQMTR", 0.0);
//                }
//                try {
//                    ObjItem.setAttribute("Q2", Double.parseDouble(Table.getValueAt(m, 25).toString()));
//                    ObjItem.setAttribute("Q2KG", Double.parseDouble(Table.getValueAt(m, 26).toString()));
//                    ObjItem.setAttribute("Q2SQMTR", Double.parseDouble(Table.getValueAt(m, 27).toString()));
//                } catch (Exception q2) {
//                    ObjItem.setAttribute("Q2", 0.0);
//                    ObjItem.setAttribute("Q2KG", 0.0);
//                    ObjItem.setAttribute("Q2SQMTR", 0.0);
//                }
//                try {
//                    ObjItem.setAttribute("Q3", Double.parseDouble(Table.getValueAt(m, 28).toString()));
//                    ObjItem.setAttribute("Q3KG", Double.parseDouble(Table.getValueAt(m, 29).toString()));
//                    ObjItem.setAttribute("Q3SQMTR", Double.parseDouble(Table.getValueAt(m, 30).toString()));
//                } catch (Exception q3) {
//                    ObjItem.setAttribute("Q3", 0.0);
//                    ObjItem.setAttribute("Q3KG", 0.0);
//                    ObjItem.setAttribute("Q3SQMTR", 0.0);
//                }
//                try {
//                    ObjItem.setAttribute("Q4", Double.parseDouble(Table.getValueAt(m, 31).toString()));
//                    ObjItem.setAttribute("Q4KG", Double.parseDouble(Table.getValueAt(m, 32).toString()));
//                    ObjItem.setAttribute("Q4SQMTR", Double.parseDouble(Table.getValueAt(m, 33).toString()));
//                } catch (Exception q4) {
//                    ObjItem.setAttribute("Q4", 0.0);
//                    ObjItem.setAttribute("Q4KG", 0.0);
//                    ObjItem.setAttribute("Q4SQMTR", 0.0);
//                }
//                ObjItem.setAttribute("TOTAL", Double.parseDouble(Table.getValueAt(m, 34).toString()));
//                ObjItem.setAttribute("TOTAL_KG", Double.parseDouble(Table.getValueAt(m, 35).toString()));
//                ObjItem.setAttribute("TOTAL_SQMTR", Double.parseDouble(Table.getValueAt(m, 36).toString()));
//                ObjItem.setAttribute("GST_PER", Double.parseDouble(Table.getValueAt(m, 37).toString()));
//                ObjItem.setAttribute("GROSS_AMOUNT", Double.parseDouble(Table.getValueAt(m, 38).toString()));
//                ObjItem.setAttribute("DISCOUNT_AMOUNT", Double.parseDouble(Table.getValueAt(m, 39).toString()));
//                ObjItem.setAttribute("NET_AMOUNT", Double.parseDouble(Table.getValueAt(m, 40).toString()));
//                ObjItem.setAttribute("PARTY_STATUS", Table.getValueAt(m, 41).toString());
//                ObjItem.setAttribute("SYSTEM_STATUS", Table.getValueAt(m, 42).toString());
//                ObjItem.setAttribute("REMARKS", Table.getValueAt(m, 43).toString());
//                ObjItem.setAttribute("PP_REMARKS", Table.getValueAt(m, 44).toString());
//                BudgetEntry.colMRItems.put(Integer.toString(BudgetEntry.colMRItems.size() + 1), ObjItem);
//            }
//        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DOC_NO1;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnSendFAmail;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBackToTab0;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFromRemarksBig;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNextToTab3;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle1;
    private javax.swing.JLabel lblyearto;
    private javax.swing.JTabbedPane tabTest;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JLabel txtpartyname;
    private javax.swing.JTextField txtyearfrom;
    // End of variables declaration//GEN-END:variables
 

    private void update_budget(String mDocNo) {
        String mcurdt, mqtrdt, sql;
        mcurdt = EITLERPGLOBAL.getCurrentDateDB();
        int mdiff;
        mqtrdt = txtyearfrom.getText().trim() + "-06-30";
        mdiff = data.getIntValueFromDB("SELECT DATEDIFF('" + mcurdt + "','" + mqtrdt + "') FROM DUAL");
        if (mdiff <= 0) {
            sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                    + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
            data.Execute(sql);
            sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA)  "
                    + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1, "
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA"
                    + "FROM PRODUCTION.FELT_BUDGET_DETAIL "
                    + "WHERE DOC_NO='" + mDocNo + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
            data.Execute(sql);
        } else {
            mqtrdt = txtyearfrom.getText().trim() + "-09-30";
            mdiff = data.getIntValueFromDB("SELECT DATEDIFF('" + mcurdt + "','" + mqtrdt + "') FROM DUAL");
            if (mdiff <= 0) {
                sql = "TRUNCATE TABLE PRODUCTION.TMP_FELT_BUDGET";
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.TMP_FELT_BUDGET "
                        + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,"
                        + "DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,"
                        + "Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,TOTAL_KG,TOTAL_SQMTR,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1, "
                        + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA"
                        + "FROM PRODUCTION.FELT_BUDGET_DETAIL "
                        + "WHERE DOC_NO='" + mDocNo + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.FELT_BUDGET B,PRODUCTION.TMP_FELT_BUDGET D "
                        + "SET D.Q1=B.Q1,D.Q1KG=B.Q1KG,D.Q1SQMTR=B.Q1SQMTR "
                        + "WHERE D.KEY1=B.KEY1";
                data.Execute(sql);
                sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                        + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.TMP_FELT_BUDGET)";
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                        + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA) "
                        + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                        + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                        + "FROM PRODUCTION.TMP_FELT_BUDGET "
                        + "WHERE KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                data.Execute(sql);

            } else {
                mqtrdt = txtyearfrom.getText().trim() + "-12-31";
                mdiff = data.getIntValueFromDB("SELECT DATEDIFF('" + mcurdt + "','" + mqtrdt + "') FROM DUAL");
                if (mdiff <= 0) {
                    sql = "TRUNCATE TABLE PRODUCTION.TMP_FELT_BUDGET";
                    data.Execute(sql);
                    sql = "INSERT INTO PRODUCTION.TMP_FELT_BUDGET  "
                            + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,"
                            + "DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,"
                            + "Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,TOTAL_KG,TOTAL_SQMTR,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                            + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                            + "FROM PRODUCTION.FELT_BUDGET_DETAIL "
                            + "WHERE DOC_NO='" + mDocNo + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                    data.Execute(sql);

                    sql = "UPDATE PRODUCTION.FELT_BUDGET B,PRODUCTION.TMP_FELT_BUDGET D "
                            + "SET D.Q1=B.Q1,D.Q1KG=B.Q1KG,D.Q1SQMTR=B.Q1SQMTR,"
                            + "D.Q2=B.Q2,D.Q2KG=B.Q2KG,D.Q2SQMTR=B.Q2SQMTR "
                            + "WHERE D.KEY1=B.KEY1";
                    data.Execute(sql);
                    sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                            + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.TMP_FELT_BUDGET)";
                    data.Execute(sql);
                    sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                            + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA) "
                            + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                            + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                            + "FROM PRODUCTION.TMP_FELT_BUDGET "
                            + "WHERE KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                    data.Execute(sql);

                } else {
                    mqtrdt = lblyearto.getText().trim() + "-03-31";
                    mdiff = data.getIntValueFromDB("SELECT DATEDIFF('" + mcurdt + "','" + mqtrdt + "') FROM DUAL");
                    if (mdiff <= 0) {
                        sql = "TRUNCATE TABLE PRODUCTION.TMP_FELT_BUDGET";
                        data.Execute(sql);
                        sql = "INSERT INTO PRODUCTION.TMP_FELT_BUDGET "
                                + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,"
                                + "DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,"
                                + "Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,TOTAL_KG,TOTAL_SQMTR,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                                + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                                + "FROM PRODUCTION.FELT_BUDGET_DETAIL "
                                + "WHERE DOC_NO='" + mDocNo + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                        data.Execute(sql);

                        sql = "UPDATE PRODUCTION.FELT_BUDGET B,PRODUCTION.TMP_FELT_BUDGET D "
                                + "SET D.Q1=B.Q1,D.Q1KG=B.Q1KG,D.Q1SQMTR=B.Q1SQMTR,"
                                + "D.Q2=B.Q2,D.Q2KG=B.Q2KG,D.Q2SQMTR=B.Q2SQMTR, "
                                + "D.Q3=B.Q3,D.Q3KG=B.Q3KG,D.Q3SQMTR=B.Q3SQMTR "
                                + "WHERE D.KEY1=B.KEY1";
                        data.Execute(sql);
                        sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.TMP_FELT_BUDGET)";
                        data.Execute(sql);
                        sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                                + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA)  "
                                + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                                + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                                + "FROM PRODUCTION.TMP_FELT_BUDGET "
                                + "WHERE KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                        data.Execute(sql);

                    }
                }
            }
        }
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET TOTAL=COALESCE(Q1,0)+COALESCE(Q2,0)+COALESCE(Q3,0)+COALESCE(Q4,0) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET TOTAL_KG=TOTAL*(COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0)),"
                + "TOTAL_SQMTR=TOTAL*(COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0)) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET GROSS_AMOUNT=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN TOTAL_SQMTR ELSE TOTAL_KG END)*SELLING_PRICE)*1.12,2) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET DISCOUNT_AMOUNT=ROUND(GROSS_AMOUNT*(SPL_DISCOUNT/100),2) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET NET_AMOUNT=GROSS_AMOUNT-DISCOUNT_AMOUNT"
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET Q1GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q1 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q1 END)*SELLING_PRICE)*1.12,2),"
                + "Q2GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q2 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q2 END)*SELLING_PRICE)*1.12,2),"
                + "Q3GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q3 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q3 END)*SELLING_PRICE)*1.12,2),"
                + "Q4GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q4 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q4 END)*SELLING_PRICE)*1.12,2) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET Q1DISCOUNT=ROUND(Q1GROSS*(SPL_DISCOUNT/100),2),"
                + "Q2DISCOUNT=ROUND(Q2GROSS*(SPL_DISCOUNT/100),2),"
                + "Q3DISCOUNT=ROUND(Q3GROSS*(SPL_DISCOUNT/100),2),"
                + "Q4DISCOUNT=ROUND(Q4GROSS*(SPL_DISCOUNT/100),2)  "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET Q1NET_AMOUNT=ROUND(Q1GROSS-Q1DISCOUNT,2),"
                + "Q2NET_AMOUNT=ROUND(Q2GROSS-Q2DISCOUNT,2),"
                + "Q3NET_AMOUNT=ROUND(Q3GROSS-Q3DISCOUNT,2),"
                + "Q4NET_AMOUNT=ROUND(Q4GROSS-Q4DISCOUNT,2) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
    }
}
