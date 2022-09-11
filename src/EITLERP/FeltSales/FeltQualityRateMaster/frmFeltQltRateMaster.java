/*
 * frmFeltRateMaster.java
 *
 * Created on September 3, 2013, 5:10 PM
 */
package EITLERP.FeltSales.FeltQualityRateMaster;

/**
 *
 * @author Vivek Kumar
 */
//import EITLERP.Production.FeltRateMaster.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.table.TableColumnModel;

import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableModel;
import EITLERP.EITLComboModel;
import EITLERP.BigEdit;
import EITLERP.clsUser;
import EITLERP.clsDepartment;
import EITLERP.clsHierarchy;
import EITLERP.clsAuthority;
import EITLERP.clsDocFlow;
import EITLERP.ComboData;
import EITLERP.Loader;
import EITLERP.AppletFrame;
import EITLERP.frmPendingApprovals;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.SelectFirstFree;
import EITLERP.data;
import TReportWriter.*;
import TReportWriter.TReportEngine;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class frmFeltQltRateMaster extends javax.swing.JApplet {
    
    private int EditMode = 0;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromUserId = 0;
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    private TReportEngine objEngine = new TReportEngine();
    
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbSendToModel;
    private EITLComboModel cmbChemicalTreatmentIndicatorModel;
    private EITLComboModel cmbPINIndicatormodel;
    private EITLComboModel cmbSpiralIndicatorModel;
    private EITLComboModel cmbSurchargeIndicatorModel;
    private EITLComboModel cmbSQMIndicatorModel;
    private EITLComboModel cmbExcCatIndicatorModel;
    private EITLComboModel cmbCategoryModel;
    private EITLComboModel cmbFabricCategoryModel;
    private EITLComboModel cmbPositionCategoryModel;
    
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel = new EITLTableModel();
    
    private clsFeltQltRateMaster ObjFeltQltRateMaster;
    public frmPendingApprovals frmPA;

    /**
     * Creates new form frmFeltRateMaster
     */
    public void init() {
        System.gc();
        setSize(950, 590);
        initComponents();
        lblTitle.setForeground(Color.WHITE);

        //Now show the Images
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
        
        ObjFeltQltRateMaster = new clsFeltQltRateMaster();
        //GenerateCombos();
        GenerateIndCombos();
        GenerateCatgCombos();
        
        GenerateHierarchyCombo();
        GenerateSendToCombo();
        
        if (ObjFeltQltRateMaster.LoadData()) {
            ObjFeltQltRateMaster.MoveLast();
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while loading data. Error is " + ObjFeltQltRateMaster.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        SetMenuForRights();
        
        SetFields(false);
//        txtWhCd.setEditable(false);
//        txtQualityID.setEditable(false);
//        txtQualityDesc.setEditable(false);
//        txtEffectiveFrom.setEditable(false);
//        txtEffectiveTo.setEditable(false);
//        txtSynPercent.setEditable(false);
//        cmbSQMIndicator.setEnabled(false);
//        cmbSurchargeIndicator.setEnabled(false);
//        cmbPINIndicator.setEnabled(false);
//        cmbChemicalTreatmentIndicator.setEnabled(false);
//        cmbSpiralIndicator.setEnabled(false);
//        cmbExcCatIndicator.setEnabled(false);
//        cmbCategory.setEnabled(false);
//        cmbFabricCategory.setEnabled(false);
//        cmbPositionCategory.setEnabled(false);
//        txtSqmChrg.setEditable(false);
//        txtSurChrg.setEditable(false);
//        txtPinChrg.setEditable(false);
//        txtChemTrtChrg.setEditable(false);
//        txtSpiralChrg.setEditable(false);
//        txtExcCatChrg.setEditable(false);
//        txtWTRate.setEditable(false);
//        txtGroup.setEditable(false);
//        txtRemarks.setEditable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        lblTitle = new javax.swing.JLabel();
        Tab = new javax.swing.JTabbedPane();
        Tab3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtEffectiveTo = new javax.swing.JTextField(7);
        cmdNext2 = new javax.swing.JButton();
        lblRevNo1 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtQualityDesc = new javax.swing.JTextField();
        cmbChemicalTreatmentIndicator = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cmbPINIndicator = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        cmbSurchargeIndicator = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        cmbSQMIndicator = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        cmbSpiralIndicator = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        txtSynPercent = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtWTRate = new javax.swing.JTextField();
        txtGroup = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtWhCd = new javax.swing.JTextField(7);
        jLabel42 = new javax.swing.JLabel();
        txtChemTrtChrg = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtSurChrg = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtSqmChrg = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txtPinChrg = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtSpiralChrg = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        txtExcCatChrg = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        cmbPositionCategory = new javax.swing.JComboBox();
        jLabel49 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField(7);
        cmbExcCatIndicator = new javax.swing.JComboBox();
        cmbCategory = new javax.swing.JComboBox();
        cmbFabricCategory = new javax.swing.JComboBox();
        jLabel50 = new javax.swing.JLabel();
        txtOldSqmChrg = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtOldWTRate = new javax.swing.JTextField();
        txtQualityID = new javax.swing.JTextField(7);
        txtEffectiveFrom = new javax.swing.JTextField(7);
        jLabel52 = new javax.swing.JLabel();
        txtDivGroup = new javax.swing.JTextField();
        txtShowRemark = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtOldRate = new javax.swing.JTextField();
        txtWeightRateCriteria = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtSurChrgPer2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSurChrgPer1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSurChargeLenCriteria = new javax.swing.JTextField();
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
        cmdNextToTab2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableApprovalStatus = new javax.swing.JTable();
        lblDocumentHistory = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableUpdateHistory = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        cmdBackToTab1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Table1 = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

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

        cmdNext.setToolTipText("Next record");
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

        cmdEdit.setToolTipText("Edit Record");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });
        ToolBar.add(cmdEdit);

        cmdDelete.setToolTipText("Delete Record");
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
        cmdPreview.setEnabled(false);
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });
        ToolBar.add(cmdPreview);

        cmdPrint.setToolTipText("Print");
        cmdPrint.setEnabled(false);
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });
        ToolBar.add(cmdPrint);

        cmdExit.setToolTipText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        ToolBar.add(cmdExit);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 940, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("FELT QUALITY RATE MASTER");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 940, 25);

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.setLayout(null);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Document No.");
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel17);
        jLabel17.setBounds(330, 20, 110, 16);

        txtDocNo.setEditable(false);
        Tab3.add(txtDocNo);
        txtDocNo.setBounds(470, 20, 110, 28);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18.setText("Product Code");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel18);
        jLabel18.setBounds(20, 50, 130, 16);

        txtEffectiveTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEffectiveToFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEffectiveToFocusLost(evt);
            }
        });
        txtEffectiveTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEffectiveToKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEffectiveToKeyTyped(evt);
            }
        });
        Tab3.add(txtEffectiveTo);
        txtEffectiveTo.setBounds(470, 80, 110, 28);

        cmdNext2.setMnemonic('X');
        cmdNext2.setText("Next >>");
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });
        Tab3.add(cmdNext2);
        cmdNext2.setBounds(700, 380, 102, 28);

        lblRevNo1.setText("...");
        Tab3.add(lblRevNo1);
        lblRevNo1.setBounds(580, 20, 20, 18);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Product Desc.");
        jLabel19.setToolTipText("Quality Description");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel19);
        jLabel19.setBounds(330, 50, 130, 16);

        txtQualityDesc.setToolTipText("Quality Description");
        txtQualityDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQualityDescFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQualityDescFocusLost(evt);
            }
        });
        Tab3.add(txtQualityDesc);
        txtQualityDesc.setBounds(470, 50, 400, 28);

        cmbChemicalTreatmentIndicator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbChemicalTreatmentIndicatorItemStateChanged(evt);
            }
        });
        Tab3.add(cmbChemicalTreatmentIndicator);
        cmbChemicalTreatmentIndicator.setBounds(160, 170, 130, 28);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("Chemical Treatment");
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel20);
        jLabel20.setBounds(20, 170, 130, 16);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("PIN Indicator");
        jLabel21.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel21);
        jLabel21.setBounds(20, 230, 80, 16);

        cmbPINIndicator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPINIndicatorItemStateChanged(evt);
            }
        });
        Tab3.add(cmbPINIndicator);
        cmbPINIndicator.setBounds(160, 230, 130, 28);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("Surcharges Indicator");
        jLabel22.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel22);
        jLabel22.setBounds(20, 200, 140, 16);

        cmbSurchargeIndicator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSurchargeIndicatorItemStateChanged(evt);
            }
        });
        Tab3.add(cmbSurchargeIndicator);
        cmbSurchargeIndicator.setBounds(160, 200, 130, 28);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("SQM Indicator");
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel23);
        jLabel23.setBounds(20, 110, 90, 16);

        cmbSQMIndicator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSQMIndicatorItemStateChanged(evt);
            }
        });
        Tab3.add(cmbSQMIndicator);
        cmbSQMIndicator.setBounds(160, 110, 130, 28);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("Spiral Indicator");
        jLabel24.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel24);
        jLabel24.setBounds(20, 260, 100, 16);

        cmbSpiralIndicator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSpiralIndicatorItemStateChanged(evt);
            }
        });
        Tab3.add(cmbSpiralIndicator);
        cmbSpiralIndicator.setBounds(160, 260, 130, 28);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("Synthetic %");
        jLabel25.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel25);
        jLabel25.setBounds(20, 140, 77, 16);

        txtSynPercent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSynPercentFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSynPercentFocusLost(evt);
            }
        });
        Tab3.add(txtSynPercent);
        txtSynPercent.setBounds(160, 140, 110, 28);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("Weight Rate (per Kg.)");
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel27);
        jLabel27.setBounds(330, 140, 150, 16);

        txtWTRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtWTRateFocusGained(evt);
            }
        });
        Tab3.add(txtWTRate);
        txtWTRate.setBounds(470, 140, 110, 28);

        txtGroup.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGroupFocusGained(evt);
            }
        });
        Tab3.add(txtGroup);
        txtGroup.setBounds(160, 320, 110, 28);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("Group");
        jLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel28);
        jLabel28.setBounds(20, 320, 100, 20);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("Remarks");
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel29);
        jLabel29.setBounds(20, 380, 130, 16);

        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRemarksFocusGained(evt);
            }
        });
        Tab3.add(txtRemarks);
        txtRemarks.setBounds(160, 380, 490, 28);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("Effective From");
        jLabel30.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel30);
        jLabel30.setBounds(20, 80, 100, 16);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel37.setText("Effective To");
        jLabel37.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel37);
        jLabel37.setBounds(330, 80, 90, 16);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setText("Category");
        jLabel38.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel38);
        jLabel38.setBounds(20, 350, 70, 16);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel39.setText("Fabric Category");
        jLabel39.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel39);
        jLabel39.setBounds(330, 350, 130, 16);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel40.setText("Position Category");
        jLabel40.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel40);
        jLabel40.setBounds(640, 350, 130, 16);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel41.setText("WH CD");
        jLabel41.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel41);
        jLabel41.setBounds(20, 20, 70, 16);

        txtWhCd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtWhCdFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtWhCdFocusLost(evt);
            }
        });
        Tab3.add(txtWhCd);
        txtWhCd.setBounds(160, 20, 110, 28);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel42.setText("Chemical TRT Charge");
        jLabel42.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel42);
        jLabel42.setBounds(330, 170, 140, 16);

        txtChemTrtChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtChemTrtChrgFocusGained(evt);
            }
        });
        Tab3.add(txtChemTrtChrg);
        txtChemTrtChrg.setBounds(470, 170, 110, 28);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("Surcharges Charge");
        jLabel43.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel43);
        jLabel43.setBounds(330, 200, 140, 16);

        txtSurChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSurChrgFocusGained(evt);
            }
        });
        Tab3.add(txtSurChrg);
        txtSurChrg.setBounds(470, 200, 110, 28);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setText("SQM Charge (per Mtr)");
        jLabel44.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel44);
        jLabel44.setBounds(330, 110, 140, 16);

        txtSqmChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSqmChrgFocusGained(evt);
            }
        });
        Tab3.add(txtSqmChrg);
        txtSqmChrg.setBounds(470, 110, 110, 28);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("PIN Charge");
        jLabel45.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel45);
        jLabel45.setBounds(330, 230, 80, 16);

        txtPinChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPinChrgFocusGained(evt);
            }
        });
        Tab3.add(txtPinChrg);
        txtPinChrg.setBounds(470, 230, 110, 28);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("Spiral Charge");
        jLabel46.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel46);
        jLabel46.setBounds(330, 260, 120, 16);

        txtSpiralChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSpiralChrgFocusGained(evt);
            }
        });
        Tab3.add(txtSpiralChrg);
        txtSpiralChrg.setBounds(470, 260, 110, 28);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel47.setText("EXC Cat Charge");
        jLabel47.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel47);
        jLabel47.setBounds(330, 290, 120, 16);

        txtExcCatChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtExcCatChrgFocusGained(evt);
            }
        });
        Tab3.add(txtExcCatChrg);
        txtExcCatChrg.setBounds(470, 290, 110, 28);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("EXC Cat Indicator");
        jLabel48.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel48);
        jLabel48.setBounds(20, 290, 120, 16);
        Tab3.add(cmbPositionCategory);
        cmbPositionCategory.setBounds(780, 350, 130, 28);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel49.setText("Doc Date.");
        jLabel49.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel49);
        jLabel49.setBounds(660, 20, 70, 16);

        txtDocDate.setEditable(false);
        txtDocDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDocDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDocDateFocusLost(evt);
            }
        });
        Tab3.add(txtDocDate);
        txtDocDate.setBounds(760, 20, 110, 28);

        cmbExcCatIndicator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbExcCatIndicatorItemStateChanged(evt);
            }
        });
        Tab3.add(cmbExcCatIndicator);
        cmbExcCatIndicator.setBounds(160, 290, 130, 28);
        Tab3.add(cmbCategory);
        cmbCategory.setBounds(160, 350, 130, 28);
        Tab3.add(cmbFabricCategory);
        cmbFabricCategory.setBounds(470, 350, 130, 28);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel50.setText("Old SQM Charge (per Mtr)");
        jLabel50.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel50);
        jLabel50.setBounds(590, 110, 180, 16);

        txtOldSqmChrg.setEditable(false);
        txtOldSqmChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtOldSqmChrgFocusGained(evt);
            }
        });
        Tab3.add(txtOldSqmChrg);
        txtOldSqmChrg.setBounds(780, 110, 90, 28);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel51.setText("Old Weight Rate (per Kg.)");
        jLabel51.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel51);
        jLabel51.setBounds(590, 140, 180, 16);

        txtOldWTRate.setEditable(false);
        txtOldWTRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtOldWTRateFocusGained(evt);
            }
        });
        Tab3.add(txtOldWTRate);
        txtOldWTRate.setBounds(780, 140, 90, 28);

        txtQualityID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQualityIDFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQualityIDFocusLost(evt);
            }
        });
        txtQualityID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQualityIDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQualityIDKeyTyped(evt);
            }
        });
        Tab3.add(txtQualityID);
        txtQualityID.setBounds(160, 50, 110, 28);

        txtEffectiveFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEffectiveFromFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEffectiveFromFocusLost(evt);
            }
        });
        txtEffectiveFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEffectiveFromKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEffectiveFromKeyTyped(evt);
            }
        });
        Tab3.add(txtEffectiveFrom);
        txtEffectiveFrom.setBounds(160, 80, 110, 28);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("Diversion Group");
        jLabel52.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab3.add(jLabel52);
        jLabel52.setBounds(330, 320, 130, 20);

        txtDivGroup.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDivGroupFocusGained(evt);
            }
        });
        Tab3.add(txtDivGroup);
        txtDivGroup.setBounds(470, 320, 110, 28);

        txtShowRemark.setText("Show");
        txtShowRemark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShowRemarkActionPerformed(evt);
            }
        });
        Tab3.add(txtShowRemark);
        txtShowRemark.setBounds(650, 380, 30, 28);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Old Rate");
        Tab3.add(jLabel1);
        jLabel1.setBounds(620, 180, 150, 30);
        Tab3.add(txtOldRate);
        txtOldRate.setBounds(780, 180, 90, 30);
        Tab3.add(txtWeightRateCriteria);
        txtWeightRateCriteria.setBounds(780, 210, 90, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Weight Rate Criteria");
        Tab3.add(jLabel2);
        jLabel2.setBounds(590, 210, 180, 30);
        Tab3.add(txtSurChrgPer2);
        txtSurChrgPer2.setBounds(780, 310, 90, 30);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Surcharge 2 Per");
        Tab3.add(jLabel3);
        jLabel3.setBounds(590, 320, 180, 16);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Surcharge 1 Per");
        Tab3.add(jLabel4);
        jLabel4.setBounds(590, 290, 180, 16);
        Tab3.add(txtSurChrgPer1);
        txtSurChrgPer1.setBounds(780, 280, 90, 30);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Surcharge Length Criteria");
        Tab3.add(jLabel5);
        jLabel5.setBounds(590, 260, 180, 16);
        Tab3.add(txtSurChargeLenCriteria);
        txtSurChargeLenCriteria.setBounds(780, 250, 90, 30);

        Tab.addTab("Qlt Rate List", Tab3);

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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
        cmbHierarchy.setNextFocusableComponent(OpgApprove);
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
        txtFromRemarks.setEnabled(false);
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(90, 95, 530, 28);

        jLabel36.setText("Your Action  ");
        Tab2.add(jLabel36);
        jLabel36.setBounds(10, 130, 81, 16);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        buttonGroup1.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.setNextFocusableComponent(OpgFinal);
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

        buttonGroup1.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
        OpgFinal.setNextFocusableComponent(OpgReject);
        OpgFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgFinalFocusGained(evt);
            }
        });
        OpgFinal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgFinalItemStateChanged(evt);
            }
        });
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });
        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 136, 20);

        buttonGroup1.add(OpgReject);
        OpgReject.setText("Reject");
        OpgReject.setEnabled(false);
        OpgReject.setNextFocusableComponent(OpgHold);
        OpgReject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgRejectFocusGained(evt);
            }
        });
        OpgReject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgRejectItemStateChanged(evt);
            }
        });
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });
        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 136, 20);

        buttonGroup1.add(OpgHold);
        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        OpgHold.setEnabled(false);
        OpgHold.setNextFocusableComponent(cmbSendTo);
        OpgHold.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgHoldFocusGained(evt);
            }
        });
        OpgHold.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgHoldItemStateChanged(evt);
            }
        });
        OpgHold.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgHoldMouseClicked(evt);
            }
        });
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        Tab2.add(jPanel6);
        jPanel6.setBounds(90, 130, 180, 100);

        jLabel33.setText("Send To");
        Tab2.add(jLabel33);
        jLabel33.setBounds(10, 253, 60, 16);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
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
        txtToRemarks.setNextFocusableComponent(cmdBackToTab0);
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
        cmdBackToTab0.setBounds(440, 370, 102, 28);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(630, 95, 33, 21);

        cmdNextToTab2.setMnemonic('N');
        cmdNextToTab2.setText("Next >>");
        cmdNextToTab2.setToolTipText("Next Tab");
        cmdNextToTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNextToTab2);
        cmdNextToTab2.setBounds(560, 370, 102, 28);

        Tab.addTab("Approval", Tab2);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(null);

        jLabel26.setText("Document Approval Status");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(8, 10, 242, 16);

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

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(12, 30, 680, 150);

        lblDocumentHistory.setText("Document Update History");
        jPanel1.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(10, 190, 182, 16);

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
        jScrollPane3.setViewportView(TableUpdateHistory);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 210, 560, 190);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        jPanel1.add(cmdViewHistory);
        cmdViewHistory.setBounds(580, 210, 110, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        jPanel1.add(cmdNormalView);
        cmdNormalView.setBounds(580, 240, 110, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        jPanel1.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(580, 280, 110, 24);

        cmdBackToTab1.setMnemonic('B');
        cmdBackToTab1.setText("<< Back");
        cmdBackToTab1.setToolTipText("Previous Tab");
        cmdBackToTab1.setIconTextGap(0);
        cmdBackToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab1ActionPerformed(evt);
            }
        });
        jPanel1.add(cmdBackToTab1);
        cmdBackToTab1.setBounds(580, 370, 110, 28);

        Tab.addTab("Status", jPanel1);

        jPanel2.setLayout(null);

        Table1.setModel(new javax.swing.table.DefaultTableModel(
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
        Table1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        Table1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane4.setViewportView(Table1);

        jPanel2.add(jScrollPane4);
        jScrollPane4.setBounds(11, 10, 710, 380);

        Tab.addTab("Rate History", jPanel2);

        getContentPane().add(Tab);
        Tab.setBounds(0, 66, 940, 470);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 540, 710, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        ObjFeltQltRateMaster.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        Cancel();
        if (ObjFeltQltRateMaster.LoadData()) {
            ObjFeltQltRateMaster.MoveLast();
            DisplayData();
        } else {
        }
        SetFields(false);
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            
        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed

    private void cmdBackToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab1ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBackToTab1ActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        if (TableUpdateHistory.getRowCount() > 0 && TableUpdateHistory.getSelectedRow() >= 0) {
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText.setText(TableUpdateHistory.getValueAt(TableUpdateHistory.getSelectedRow(), 4).toString());
            bigEdit.ShowEdit();
        } else {
            JOptionPane.showMessageDialog(this, "Select a row from Document Update History", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        ObjFeltQltRateMaster.HistoryView = false;
        ObjFeltQltRateMaster.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        ObjFeltQltRateMaster.ShowHistory(txtDocNo.getText().trim());
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained
        cmbHierarchy.requestFocus();
    }//GEN-LAST:event_Tab2FocusGained

    private void cmdNextToTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab2ActionPerformed
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNextToTab2ActionPerformed

    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtFromRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed

    private void cmdBackToTab0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab0ActionPerformed
        Tab.setSelectedIndex(0);
    }//GEN-LAST:event_cmdBackToTab0ActionPerformed

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained
        lblStatus.setText("Enter the remarks for next user");
    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        lblStatus.setText("Select the user to whom document to be forwarded");
    }//GEN-LAST:event_cmbSendToFocusGained

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        OpgHold.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgReject.setSelected(false);
        
        cmbSendTo.setEnabled(false);
    }//GEN-LAST:event_OpgHoldMouseClicked

    private void OpgHoldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgHoldItemStateChanged

    }//GEN-LAST:event_OpgHoldItemStateChanged

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgHoldFocusGained

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);
        
        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgRejectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgRejectItemStateChanged

    }//GEN-LAST:event_OpgRejectItemStateChanged

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgRejectFocusGained

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        OpgFinal.setSelected(true);
        OpgReject.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);
        
        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgFinalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgFinalItemStateChanged

    }//GEN-LAST:event_OpgFinalItemStateChanged

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        //SetupApproval();
        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedSendToCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString())) {
                cmbSendTo.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }
        
        if (cmbSendTo.getItemCount() <= 0) {
            GenerateSendToCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

    private void OpgApproveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgApproveItemStateChanged

    }//GEN-LAST:event_OpgApproveItemStateChanged

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        lblStatus.setText("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
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
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void txtEffectiveFromKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEffectiveFromKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEffectiveFromKeyTyped

    private void txtEffectiveFromKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEffectiveFromKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEffectiveFromKeyPressed

    private void txtEffectiveFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffectiveFromFocusLost
        // TODO add your handling code here:
        if (txtEffectiveFrom.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter From Date");
            txtEffectiveFrom.requestFocus();
        } else if (!EITLERPGLOBAL.isDate(txtEffectiveFrom.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid From Date in DD/MM/YYYY format.");
            txtEffectiveFrom.setText("");
            txtEffectiveFrom.requestFocus();
        } else {
            txtEffectiveTo.setText("");
        }
    }//GEN-LAST:event_txtEffectiveFromFocusLost

    private void txtEffectiveFromFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffectiveFromFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEffectiveFromFocusGained

    private void txtQualityIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQualityIDKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQualityIDKeyTyped

    private void txtQualityIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQualityIDKeyPressed
        // TODO add your handling code here:
        clearFields2();
    }//GEN-LAST:event_txtQualityIDKeyPressed

    private void txtQualityIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQualityIDFocusLost
        // TODO add your handling code here:
        if (txtQualityID.getText().trim().length() != 6) {
            JOptionPane.showMessageDialog(null, "Enter Correct Product Code");
            txtQualityID.setText("");
            txtQualityID.requestFocus();
        }

        //-================================================================================================
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();
            
            String strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + txtQualityID.getText().trim() + "' AND DOC_NO NOT IN ('" + txtDocNo.getText().trim() + "') AND APPROVED=0 AND CANCELED=0 "; //AND EFFECTIVE_TO IS NULL AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'
//            System.out.println(strSQL);
            ResultSet rsTmp = data.getResult(strSQL);
            rsTmp.first();
            
            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                JOptionPane.showMessageDialog(null, "Please Final Approve or Reject Priveous Record of PRODUCT CODE : '" + txtQualityID.getText() + "'.");
                txtQualityID.setText("");
                //Cancel();
            } else {
                
                String sr = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + txtQualityID.getText().trim() + "' AND DOC_NO NOT IN ('" + txtDocNo.getText().trim() + "') AND APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC"; //AND EFFECTIVE_TO IS NULL AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'
//                System.out.println(sr);
                ResultSet rs = data.getResult(sr);
                rs.first();
                
                if (rs.getInt("COUNT") > 0) {
                    String sql = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + txtQualityID.getText().trim() + "' AND DOC_NO NOT IN ('" + txtDocNo.getText().trim() + "') AND APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC"; //AND EFFECTIVE_TO IS NULL AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'
//                    System.out.println(sql);
                    ResultSet rsData = data.getResult(sql);
                    rsData.first();
                    
                    txtQualityDesc.setText(rsData.getString("PRODUCT_DESC"));
                    txtQualityDesc.setEnabled(false);
                    txtSynPercent.setText(String.valueOf(rsData.getInt("SYN_PER")));
                    
                    txtOldSqmChrg.setText(String.valueOf(rsData.getFloat("SQM_CHRG")));
                    txtOldWTRate.setText(String.valueOf(rsData.getFloat("WT_RATE")));
                    
                    EITLERPGLOBAL.setComboIndex(cmbChemicalTreatmentIndicator, rsData.getString("CHEM_TRT_IND"));
                    EITLERPGLOBAL.setComboIndex(cmbSurchargeIndicator, rsData.getString("SUR_IND"));
                    EITLERPGLOBAL.setComboIndex(cmbSQMIndicator, rsData.getString("SQM_IND"));
                    cmbSQMIndicator.setEnabled(false);
                    EITLERPGLOBAL.setComboIndex(cmbPINIndicator, rsData.getString("PIN_IND"));
                    EITLERPGLOBAL.setComboIndex(cmbSpiralIndicator, rsData.getString("SPR_IND"));
                    EITLERPGLOBAL.setComboIndex(cmbExcCatIndicator, rsData.getString("EXC_CAT_IND"));
                    
                    txtChemTrtChrg.setText(String.valueOf(rsData.getFloat("CHEM_TRT_CHRG")));
                    txtSqmChrg.setText(String.valueOf(rsData.getFloat("SQM_CHRG")));
                    txtPinChrg.setText(String.valueOf(rsData.getFloat("PIN_CHRG")));
                    txtSurChrg.setText(String.valueOf(rsData.getFloat("SUR_CHRG")));
                    txtSpiralChrg.setText(String.valueOf(rsData.getFloat("SPR_CHRG")));
                    txtExcCatChrg.setText(String.valueOf(rsData.getFloat("EXC_CAT_CHRG")));
                    
                    txtOldRate.setText(String.valueOf(rsData.getFloat("WT_RATE")));
                    txtWeightRateCriteria.setText(String.valueOf(rsData.getFloat("WEIGHT_RATE_CRITERIA")));
                    txtSurChargeLenCriteria.setText(String.valueOf(rsData.getFloat("SURCHARGE_LENGTH_CRITERIA")));
                    txtSurChrgPer1.setText(String.valueOf(rsData.getFloat("SURCHARGE_1_PER")));
                    txtSurChrgPer2.setText(String.valueOf(rsData.getFloat("SURCHARGE_2_PER")));
                    
                    txtGroup.setText(rsData.getString("GROUP_NAME"));
                    txtDivGroup.setText(rsData.getString("DIVERSION_GROUP"));
                    
                    EITLERPGLOBAL.setComboIndex(cmbCategory, rsData.getString("CATEGORY"));
                    EITLERPGLOBAL.setComboIndex(cmbFabricCategory, rsData.getString("FABRIC_CATG"));
                    EITLERPGLOBAL.setComboIndex(cmbPositionCategory, rsData.getString("POSITION_CATG"));
//                    cmbCategory.setSelectedItem(rsData.getString("CATEGORY"));
//                    cmbFabricCategory.setSelectedItem(rsData.getString("FABRIC_CATG"));
//                    cmbPositionCategory.setSelectedItem(rsData.getString("POSITION_CATG"));
                    for (int i = 0; i < DataModel.getRowCount(); i++) {
                        DataModel.removeRow(i);
                    }
                    if (DataModel.getRowCount() > 0) {
                        DataModel.removeRow(0);
                    }
                    
                    int a = 0;
                    
                    while (!rsData.isAfterLast()) {
                        a = a + 1;
                        
                        Object[] rowData = new Object[10];
                        rowData[0] = a;
                        rowData[1] = rsData.getString("PRODUCT_CODE");
                        rowData[2] = EITLERPGLOBAL.formatDateDB(rsData.getString("EFFECTIVE_FROM"));
                        rowData[3] = EITLERPGLOBAL.formatDateDB(rsData.getString("EFFECTIVE_TO"));
                        rowData[4] = String.valueOf(rsData.getFloat("SQM_CHRG"));
                        rowData[5] = String.valueOf(rsData.getFloat("WT_RATE"));
                        
                        DataModel.addRow(rowData);
                        
                        rsData.next();
                    }
                } else {
                    
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }//GEN-LAST:event_txtQualityIDFocusLost

    private void txtQualityIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQualityIDFocusGained
        // TODO add your handling code here:
        lblStatus.setText("Enter the Quality ID");
    }//GEN-LAST:event_txtQualityIDFocusGained

    private void txtOldWTRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOldWTRateFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOldWTRateFocusGained

    private void txtOldSqmChrgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOldSqmChrgFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOldSqmChrgFocusGained

    private void cmbExcCatIndicatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbExcCatIndicatorItemStateChanged
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            int a = cmbExcCatIndicator.getSelectedIndex();
            if (a == 0) {
                txtExcCatChrg.setEnabled(false);
                txtExcCatChrg.setEditable(false);
                txtExcCatChrg.setText("");
            } else {
                txtExcCatChrg.setEnabled(true);
                txtExcCatChrg.setEditable(true);
            }
        }
    }//GEN-LAST:event_cmbExcCatIndicatorItemStateChanged

    private void txtDocDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDocDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocDateFocusLost

    private void txtDocDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDocDateFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocDateFocusGained

    private void txtExcCatChrgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExcCatChrgFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtExcCatChrgFocusGained

    private void txtSpiralChrgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSpiralChrgFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpiralChrgFocusGained

    private void txtPinChrgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPinChrgFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPinChrgFocusGained

    private void txtSqmChrgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSqmChrgFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSqmChrgFocusGained

    private void txtSurChrgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSurChrgFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSurChrgFocusGained

    private void txtChemTrtChrgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChemTrtChrgFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChemTrtChrgFocusGained

    private void txtWhCdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWhCdFocusLost
        // TODO add your handling code here:
        //        String a=txtWhCd.getText().trim();
        //        if(txtWhCd.getText().trim().length()!=2){
        //            JOptionPane.showMessageDialog(null, "Enter Correct Code");
        //            txtWhCd.setText("");
        //            txtWhCd.requestFocus();
        //        }
        //        else{
        //            JOptionPane.showMessageDialog(null, "correct");
        //        }
    }//GEN-LAST:event_txtWhCdFocusLost

    private void txtWhCdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWhCdFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWhCdFocusGained

    private void txtRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusGained
        lblStatus.setText("Enter Remarks");
    }//GEN-LAST:event_txtRemarksFocusGained

    private void txtGroupFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupFocusGained
        lblStatus.setText("Enter Group");
    }//GEN-LAST:event_txtGroupFocusGained

    private void txtWTRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWTRateFocusGained
        lblStatus.setText("Enter Weight Rate");
    }//GEN-LAST:event_txtWTRateFocusGained

    private void txtSynPercentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSynPercentFocusLost
        // TODO add your handling code here:

        int b = 0;
        char z[] = new char[10];
        for (int i = 0; i < txtSynPercent.getText().trim().length(); i++) {
            z[i] = txtSynPercent.getText().trim().charAt(i);
            
            if (z[i] >= 48 && z[i] <= 57) {
                continue;
            } else {
                b = 1;
                break;
            }
        }
        
        if (b == 1) {
            JOptionPane.showMessageDialog(null, "Character Found");
            txtSynPercent.setText("");
            //txtSynPercent.requestFocus();
        }
        //        else
        //            JOptionPane.showMessageDialog(null, "Correct Percentage");

        float n = Float.valueOf(txtSynPercent.getText().trim());
        if (n < 0 || n > 100) {
            JOptionPane.showMessageDialog(null, "Enter Percentage in Range of 0 to 100");
            txtSynPercent.setText("");
            //txtSynPercent.requestFocus();
        }
    }//GEN-LAST:event_txtSynPercentFocusLost

    private void txtSynPercentFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSynPercentFocusGained
        lblStatus.setText("Enter Synthetic Percentage");
    }//GEN-LAST:event_txtSynPercentFocusGained

    private void cmbSpiralIndicatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSpiralIndicatorItemStateChanged
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            int a = cmbSpiralIndicator.getSelectedIndex();
            if (a == 0) {
                txtSpiralChrg.setEnabled(false);
                txtSpiralChrg.setEditable(false);
                txtSpiralChrg.setText("");
            } else {
                txtSpiralChrg.setEnabled(true);
                txtSpiralChrg.setEditable(true);
            }
        }
    }//GEN-LAST:event_cmbSpiralIndicatorItemStateChanged

    private void cmbSQMIndicatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSQMIndicatorItemStateChanged
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            int a = cmbSQMIndicator.getSelectedIndex();
            if (a == 0) {
                txtSqmChrg.setEnabled(false);
                txtSqmChrg.setText("");
                txtWTRate.setEnabled(true);
                txtWTRate.setEditable(true);
                
            } else {
                txtSqmChrg.setEnabled(true);
                txtSqmChrg.setEditable(true);
                txtWTRate.setEnabled(false);
                txtWTRate.setText("");
                
            }
        }
    }//GEN-LAST:event_cmbSQMIndicatorItemStateChanged

    private void cmbSurchargeIndicatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSurchargeIndicatorItemStateChanged
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            int a = cmbSurchargeIndicator.getSelectedIndex();
            if (a == 0) {
                txtSurChrg.setEnabled(false);
                txtSurChrg.setEditable(false);
                txtSurChrg.setText("");
            } else {
                txtSurChrg.setEnabled(true);
                txtSurChrg.setEditable(true);
            }
        }
    }//GEN-LAST:event_cmbSurchargeIndicatorItemStateChanged

    private void cmbPINIndicatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPINIndicatorItemStateChanged
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            int a = cmbPINIndicator.getSelectedIndex();
            if (a == 0) {
                txtPinChrg.setEnabled(false);
                txtPinChrg.setEditable(false);
                txtPinChrg.setText("");
            } else {
                txtPinChrg.setEnabled(true);
                txtPinChrg.setEditable(true);
            }
        }
    }//GEN-LAST:event_cmbPINIndicatorItemStateChanged

    private void cmbChemicalTreatmentIndicatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbChemicalTreatmentIndicatorItemStateChanged
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            int a = cmbChemicalTreatmentIndicator.getSelectedIndex();
            if (a == 0) {
                //txtChemTrtChrg.setEditable(false);
                txtChemTrtChrg.setEnabled(false);
                txtChemTrtChrg.setEditable(false);
                txtChemTrtChrg.setText("");
            } else {
                txtChemTrtChrg.setEnabled(true);
                txtChemTrtChrg.setEditable(true);
                //txtChemTrtChrg.setEditable(true);
            }
        }
    }//GEN-LAST:event_cmbChemicalTreatmentIndicatorItemStateChanged

    private void txtQualityDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQualityDescFocusLost
        // TODO add your handling code here:
        if (txtQualityDesc.getText().trim().length() > 35) {
            JOptionPane.showMessageDialog(null, "Enter Product Description less than 35 Character");
            //txtQualityDesc.setText("");
            txtQualityDesc.requestFocus();
        }
    }//GEN-LAST:event_txtQualityDescFocusLost

    private void txtQualityDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQualityDescFocusGained
        lblStatus.setText("Enter Quality Description");
    }//GEN-LAST:event_txtQualityDescFocusGained

    private void cmdNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext2ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext2ActionPerformed

    private void txtEffectiveToKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEffectiveToKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEffectiveToKeyTyped

    private void txtEffectiveToKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEffectiveToKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtEffectiveToKeyPressed

    private void txtEffectiveToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffectiveToFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_txtEffectiveToFocusLost

    private void txtEffectiveToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffectiveToFocusGained

    }//GEN-LAST:event_txtEffectiveToFocusGained

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
        HashMap Parameters = new HashMap();
        Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
        
        new TReportWriter.TReportEngine().PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/QLTRTMST.rpt", Parameters, ObjFeltQltRateMaster.getReportData());
        EITLERPGLOBAL.PAGE_BREAK = true;
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void txtDivGroupFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDivGroupFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDivGroupFocusGained

    private void txtShowRemarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShowRemarkActionPerformed
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(this, txtRemarks.getText());
        JTextArea ta = new JTextArea(10, 20);
        ta.setEditable(false);
        ta.setText(txtRemarks.getText());
        ta.setSize(600, 100);
        ta.setLineWrap(true);
        JScrollPane msgPane = new JScrollPane(ta);
        msgPane.setSize(600, 100);
        JOptionPane.showMessageDialog(this, msgPane, "Remark", JOptionPane.INFORMATION_MESSAGE);        
    }//GEN-LAST:event_txtShowRemarkActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab3;
    private javax.swing.JTable Table1;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbCategory;
    private javax.swing.JComboBox cmbChemicalTreatmentIndicator;
    private javax.swing.JComboBox cmbExcCatIndicator;
    private javax.swing.JComboBox cmbFabricCategory;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbPINIndicator;
    private javax.swing.JComboBox cmbPositionCategory;
    private javax.swing.JComboBox cmbSQMIndicator;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JComboBox cmbSpiralIndicator;
    private javax.swing.JComboBox cmbSurchargeIndicator;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBackToTab0;
    private javax.swing.JButton cmdBackToTab1;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFromRemarksBig;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext2;
    private javax.swing.JButton cmdNextToTab2;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblRevNo1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtChemTrtChrg;
    private javax.swing.JTextField txtDivGroup;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtEffectiveFrom;
    private javax.swing.JTextField txtEffectiveTo;
    private javax.swing.JTextField txtExcCatChrg;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGroup;
    private javax.swing.JTextField txtOldRate;
    private javax.swing.JTextField txtOldSqmChrg;
    private javax.swing.JTextField txtOldWTRate;
    private javax.swing.JTextField txtPinChrg;
    private javax.swing.JTextField txtQualityDesc;
    private javax.swing.JTextField txtQualityID;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JButton txtShowRemark;
    private javax.swing.JTextField txtSpiralChrg;
    private javax.swing.JTextField txtSqmChrg;
    private javax.swing.JTextField txtSurChargeLenCriteria;
    private javax.swing.JTextField txtSurChrg;
    private javax.swing.JTextField txtSurChrgPer1;
    private javax.swing.JTextField txtSurChrgPer2;
    private javax.swing.JTextField txtSynPercent;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtWTRate;
    private javax.swing.JTextField txtWeightRateCriteria;
    private javax.swing.JTextField txtWhCd;
    // End of variables declaration//GEN-END:variables

    //Puts toolbar in enable mode
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

    //Puts toolbar in disable mode
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
    
    private void SetFields(boolean pStat) {

        //UserServiceImpl service = new UserServiceImpl();
        //DComUserMaster user = service.checkLOGIN_ID(EITLERPGLOBAL.gLoginID+"");
        //S_O_NO.setEditable(false);
        txtWhCd.setEnabled(false);
        txtDocNo.setEnabled(false);
        txtDocDate.setEnabled(false);
        txtOldSqmChrg.setEnabled(false);
        txtOldSqmChrg.setVisible(true);
        txtOldWTRate.setEnabled(false);
        txtOldWTRate.setVisible(true);
        txtWhCd.setVisible(true);
        txtDocDate.setVisible(true);
        txtDocNo.setVisible(true);
        txtQualityID.setEnabled(pStat);
        txtQualityID.setEditable(pStat);
        txtQualityDesc.setEnabled(pStat);
        txtQualityDesc.setEditable(pStat);
        txtEffectiveFrom.setEnabled(pStat);
        txtEffectiveFrom.setEditable(pStat);
        txtEffectiveTo.setEnabled(pStat);
        txtEffectiveTo.setEditable(pStat);
        txtSynPercent.setEnabled(pStat);
        txtSynPercent.setEditable(pStat);
//        txtSQMRate.setEnabled(pStat);
        txtWTRate.setEnabled(pStat);
        txtWTRate.setEditable(pStat);
        txtChemTrtChrg.setEnabled(false);
        txtChemTrtChrg.setEditable(false);
        txtPinChrg.setEnabled(false);
        txtPinChrg.setEditable(false);
        txtSqmChrg.setEnabled(false);
        txtSqmChrg.setEditable(false);
        txtSurChrg.setEnabled(false);
        txtSurChrg.setEditable(false);
        txtSpiralChrg.setEnabled(false);
        txtSpiralChrg.setEditable(false);
        txtExcCatChrg.setEnabled(false);
        txtExcCatChrg.setEditable(false);
        txtGroup.setEnabled(pStat);
        txtGroup.setEditable(pStat);
        txtDivGroup.setEnabled(pStat);
        txtDivGroup.setEditable(pStat);
        txtRemarks.setEnabled(pStat);
        txtRemarks.setEditable(pStat);
        
        txtOldRate.setEditable(false);
        txtWeightRateCriteria.setEditable(pStat);
        txtSurChargeLenCriteria.setEditable(pStat);
        txtSurChrgPer1.setEditable(pStat);
        txtSurChrgPer2.setEditable(pStat);
        
        
//        txtCategory.setEnabled(pStat);
//        txtFebricCategory.setEnabled(pStat);
//        txtPositionCategory.setEnabled(pStat);
//        txtFeltCategory.setEnabled(pStat);
        cmbChemicalTreatmentIndicator.setEnabled(pStat);
        cmbSurchargeIndicator.setEnabled(pStat);
        cmbSQMIndicator.setEnabled(pStat);
        cmbPINIndicator.setEnabled(pStat);
        cmbSpiralIndicator.setEnabled(pStat);
        cmbExcCatIndicator.setEnabled(pStat);
        cmbCategory.setEnabled(pStat);
        cmbFabricCategory.setEnabled(pStat);
        cmbPositionCategory.setEnabled(pStat);
        
        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        
        SetupApproval();
        
    }
    
    private void ClearFields() {
        
        txtWhCd.setText("");
        txtDocNo.setText("");
        txtDocDate.setText("");
        txtQualityID.setText("");
        txtQualityDesc.setText("");
        txtEffectiveFrom.setText("");
        txtEffectiveTo.setText("");
        txtSynPercent.setText("");
//        txtSQMRate.setText("");
        txtOldSqmChrg.setText("");
        txtOldWTRate.setText("");
        txtWTRate.setText("");
        txtChemTrtChrg.setText("");
        txtSurChrg.setText("");
        txtSqmChrg.setText("");
        txtPinChrg.setText("");
        txtSpiralChrg.setText("");
        txtExcCatChrg.setText("");
        txtGroup.setText("");
        txtDivGroup.setText("");
        txtRemarks.setText("");
        
        txtOldRate.setText("");
        txtWeightRateCriteria.setText("");
        txtSurChargeLenCriteria.setText("");
        txtSurChrgPer1.setText("");
        txtSurChrgPer2.setText("");
        
//        txtCategory.setText("");
//        txtFebricCategory.setText("");
//        txtPositionCategory.setText("");
//        txtFeltCategory.setText("");
        cmbChemicalTreatmentIndicator.setSelectedIndex(0);
        cmbSurchargeIndicator.setSelectedIndex(0);
        cmbSQMIndicator.setSelectedIndex(0);
        cmbPINIndicator.setSelectedIndex(0);
        cmbSpiralIndicator.setSelectedIndex(0);
        cmbExcCatIndicator.setSelectedIndex(0);
        
        cmbCategory.setSelectedIndex(0);
        cmbFabricCategory.setSelectedIndex(0);
        cmbPositionCategory.setSelectedIndex(0);
        
        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        
        FormatGrid();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
    }
    
    private void clearFields2() {

//        txtWhCd.setText("");
//        txtDocNo.setText("");
//        txtDocDate.setText("");
        //txtQualityID.setText("");
        txtQualityDesc.setText("");
        txtQualityDesc.setEnabled(true);
        txtEffectiveFrom.setText("");
        txtEffectiveTo.setText("");
        txtSynPercent.setText("");
//        txtSQMRate.setText("");
        txtOldSqmChrg.setText("");
        txtOldWTRate.setText("");
        txtWTRate.setText("");
        txtChemTrtChrg.setText("");
        txtSurChrg.setText("");
        txtSqmChrg.setText("");
        txtPinChrg.setText("");
        txtSpiralChrg.setText("");
        txtExcCatChrg.setText("");
        txtGroup.setText("");
        txtDivGroup.setText("");
        txtRemarks.setText("");
//        txtCategory.setText("");
//        txtFebricCategory.setText("");
//        txtPositionCategory.setText("");
//        txtFeltCategory.setText("");
        cmbChemicalTreatmentIndicator.setSelectedIndex(0);
        cmbSQMIndicator.setEnabled(true);
        cmbSurchargeIndicator.setSelectedIndex(0);
        cmbSQMIndicator.setSelectedIndex(0);
        cmbPINIndicator.setSelectedIndex(0);
        cmbSpiralIndicator.setSelectedIndex(0);
        cmbExcCatIndicator.setSelectedIndex(0);
        
        cmbCategory.setSelectedIndex(0);
        cmbFabricCategory.setSelectedIndex(0);
        cmbPositionCategory.setSelectedIndex(0);
        
        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        
        FormatGrid();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
    }

    //Didplay data on the Screen
    private void DisplayData() {
        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = 601;
            
            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        try {
            //=========== Title Bar Color Indication ===============//
            if (EditMode == 0) {
                if (ObjFeltQltRateMaster.getAttribute("APPROVED").getInt() == 1) {
                    lblTitle.setBackground(Color.BLUE);
                } else {
                    lblTitle.setBackground(Color.GRAY);
                }
                
                if (ObjFeltQltRateMaster.getAttribute("CANCELED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
            //============================================//

            ClearFields();
            
            lblTitle.setText("FELT QUALITY RATE MASTER - " + ObjFeltQltRateMaster.getAttribute("DOC_NO").getString());
            lblRevNo1.setText(Integer.toString((int) ObjFeltQltRateMaster.getAttribute("REVISION_NO").getVal()));
            txtWhCd.setText(ObjFeltQltRateMaster.getAttribute("WH_CD").getString());
            txtDocNo.setText(ObjFeltQltRateMaster.getAttribute("DOC_NO").getString());
            txtDocDate.setText(EITLERPGLOBAL.formatDate(ObjFeltQltRateMaster.getAttribute("DOC_DATE").getString()));
            txtQualityID.setText(ObjFeltQltRateMaster.getAttribute("PRODUCT_CODE").getString());
            txtQualityDesc.setText(ObjFeltQltRateMaster.getAttribute("PRODUCT_DESC").getString());
            txtEffectiveFrom.setText(EITLERPGLOBAL.formatDate(ObjFeltQltRateMaster.getAttribute("EFFECTIVE_FROM").getString()));
            txtEffectiveTo.setText(EITLERPGLOBAL.formatDate(ObjFeltQltRateMaster.getAttribute("EFFECTIVE_TO").getString()));
            //txtSynPercent.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("SYN_PER").getVal()));
            txtSynPercent.setText(Integer.toString((int) ObjFeltQltRateMaster.getAttribute("SYN_PER").getVal()));
//        txtSQMRate.setText(rateMst.getSqmRate().toString());

            String sr1 = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + txtQualityID.getText().trim() + "' AND DOC_NO NOT IN ('" + txtDocNo.getText().trim() + "') AND APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC"; //AND EFFECTIVE_TO IS NULL AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'
//            System.out.println(sr1);
            ResultSet rs1 = data.getResult(sr1);
            rs1.first();
            
            if (rs1.getInt("COUNT") > 0) {
                String sql2 = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + ObjFeltQltRateMaster.getAttribute("PRODUCT_CODE").getString() + "' AND DOC_NO NOT IN ('" + ObjFeltQltRateMaster.getAttribute("DOC_NO").getString() + "') AND APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC"; //AND EFFECTIVE_TO IS NULL AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'
//                System.out.println(sql2);
                ResultSet rsData1 = data.getResult(sql2);
                rsData1.first();
                
                txtOldSqmChrg.setText(String.valueOf(rsData1.getFloat("SQM_CHRG")));
                txtOldWTRate.setText(String.valueOf(rsData1.getFloat("WT_RATE")));
            } else {
                txtOldSqmChrg.setText("");
                txtOldWTRate.setText("");
            }
            
            txtWTRate.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("WT_RATE").getVal()));
            txtChemTrtChrg.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("CHEM_TRT_CHRG").getVal()));
            txtSqmChrg.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("SQM_CHRG").getVal()));
            txtPinChrg.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("PIN_CHRG").getVal()));
            txtSurChrg.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("SUR_CHRG").getVal()));
            txtSpiralChrg.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("SPR_CHRG").getVal()));
            txtExcCatChrg.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("EXC_CAT_CHRG").getVal()));
            
            
            txtOldRate.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("OLD_RATE").getVal()));
            txtWeightRateCriteria.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("WEIGHT_RATE_CRITERIA").getVal()));
            txtSurChargeLenCriteria.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("SURCHARGE_LENGTH_CRITERIA").getVal()));
            txtSurChrgPer1.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("SURCHARGE_1_PER").getVal()));
            txtSurChrgPer2.setText(Float.toString((float) ObjFeltQltRateMaster.getAttribute("SURCHARGE_2_PER").getVal()));
            
            txtGroup.setText(ObjFeltQltRateMaster.getAttribute("GROUP_NAME").getString());
            txtDivGroup.setText(ObjFeltQltRateMaster.getAttribute("DIVERSION_GROUP").getString());
//        txtCategory.setText(rateMst.getCategory());
//        txtFebricCategory.setText(rateMst.getFabricCatg());
//        txtPositionCategory.setText(rateMst.getPositionCatg());
//        txtFeltCategory.setText(rateMst.getFeltCatg());
            txtRemarks.setText(ObjFeltQltRateMaster.getAttribute("REMARKS").getString());
            EITLERPGLOBAL.setComboIndex(cmbChemicalTreatmentIndicator, ObjFeltQltRateMaster.getAttribute("CHEM_TRT_IND").getString());
            EITLERPGLOBAL.setComboIndex(cmbSurchargeIndicator, ObjFeltQltRateMaster.getAttribute("SUR_IND").getString());
            EITLERPGLOBAL.setComboIndex(cmbSQMIndicator, ObjFeltQltRateMaster.getAttribute("SQM_IND").getString());
            EITLERPGLOBAL.setComboIndex(cmbPINIndicator, ObjFeltQltRateMaster.getAttribute("PIN_IND").getString());
            EITLERPGLOBAL.setComboIndex(cmbSpiralIndicator, ObjFeltQltRateMaster.getAttribute("SPR_IND").getString());
            EITLERPGLOBAL.setComboIndex(cmbExcCatIndicator, ObjFeltQltRateMaster.getAttribute("EXC_CAT_IND").getString());
            
            EITLERPGLOBAL.setComboIndex(cmbCategory, ObjFeltQltRateMaster.getAttribute("CATEGORY").getString());
            EITLERPGLOBAL.setComboIndex(cmbFabricCategory, ObjFeltQltRateMaster.getAttribute("FABRIC_CATG").getString());
            EITLERPGLOBAL.setComboIndex(cmbPositionCategory, ObjFeltQltRateMaster.getAttribute("POSITION_CATG").getString());

//            cmbCategory.setSelectedItem(ObjFeltQltRateMaster.getAttribute("CATEGORY").getString());
//            cmbFabricCategory.setSelectedItem(ObjFeltQltRateMaster.getAttribute("FABRIC_CATG").getString());
//            cmbPositionCategory.setSelectedItem(ObjFeltQltRateMaster.getAttribute("POSITION_CATG").getString());
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) ObjFeltQltRateMaster.getAttribute("HIERARCHY_ID").getVal());
            
            FormatGrid();
            //Now Generate Table
            for (int i = 1; i <= ObjFeltQltRateMaster.hmRateDetails.size(); i++) {
                clsFeltQltRateMasterDetail ObjFeltQltRateMasterDetail = (clsFeltQltRateMasterDetail) ObjFeltQltRateMaster.hmRateDetails.get(Integer.toString(i));
                Object[] rowData = new Object[10];
                
                rowData[0] = Integer.toString(i);
                rowData[1] = ObjFeltQltRateMasterDetail.getAttribute("PRODUCT_CODE").getObj();
                rowData[2] = EITLERPGLOBAL.formatDate(ObjFeltQltRateMasterDetail.getAttribute("EFFECTIVE_FROM").getString());
                rowData[3] = EITLERPGLOBAL.formatDate(ObjFeltQltRateMasterDetail.getAttribute("EFFECTIVE_TO").getString());
                rowData[4] = Float.toString((float) ObjFeltQltRateMasterDetail.getAttribute("SQM_CHRG").getVal());
                rowData[5] = Float.toString((float) ObjFeltQltRateMasterDetail.getAttribute("WT_RATE").getVal());

//                rowData[6]=Float.toString((float)ObjFeltQltRateMasterDetail.getAttribute("CHARGES").getVal());
//                rowData[7]=EITLERPGLOBAL.formatDate(ObjFeltQltRateMasterDetail.getAttribute("CHARGES_FROM_DATE").getString());
//                rowData[8]=EITLERPGLOBAL.formatDate(ObjFeltQltRateMasterDetail.getAttribute("CHARGES_TO_DATE").getString());
//                rowData[9]=ObjFeltQltRateMasterDetail.getAttribute("DOC_NO").getString();
//                
                DataModel.addRow(rowData);
            }

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap hmList = new HashMap();
            
            hmList = clsFeltProductionApprovalFlow.getDocumentFlow(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString());
            for (int i = 1; i <= hmList.size(); i++) {
                //clsDocFlow is collection class used for holding approval flow data
                clsDocFlow ObjFlow = (clsDocFlow) hmList.get(Integer.toString(i));
                Object[] rowData = new Object[7];
                
                rowData[0] = Integer.toString(i);
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal()));
                rowData[3] = ObjFlow.getAttribute("STATUS").getString();
                rowData[4] = EITLERPGLOBAL.formatDate(ObjFlow.getAttribute("RECEIVED_DATE").getString());
                rowData[5] = EITLERPGLOBAL.formatDate(ObjFlow.getAttribute("ACTION_DATE").getString());
                rowData[6] = ObjFlow.getAttribute("REMARKS").getString();
                
                DataModelApprovalStatus.addRow(rowData);
            }
            //============================================================//

            // Generating Grid for Showing Production Details Update History
            FormatGridUpdateHistory();
            HashMap hmApprovalHistory = clsFeltQltRateMaster.getHistoryList(EITLERPGLOBAL.gCompanyID, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString());
            for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                clsFeltQltRateMaster ObjFeltQltRateMaster = (clsFeltQltRateMaster) hmApprovalHistory.get(Integer.toString(i));
                Object[] rowData = new Object[6];
                
                rowData[0] = Integer.toString((int) ObjFeltQltRateMaster.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(2, (int) ObjFeltQltRateMaster.getAttribute("UPDATED_BY").getVal());
                rowData[2] = ObjFeltQltRateMaster.getAttribute("ENTRY_DATE").getString();
                
                String ApprovalStatus = "";
                
                if ((ObjFeltQltRateMaster.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }
                
                if ((ObjFeltQltRateMaster.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }
                
                if ((ObjFeltQltRateMaster.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }
                
                if ((ObjFeltQltRateMaster.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }
                
                if ((ObjFeltQltRateMaster.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }
                
                if ((ObjFeltQltRateMaster.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }
                
                if ((ObjFeltQltRateMaster.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }
                
                rowData[3] = ApprovalStatus;
                rowData[4] = ObjFeltQltRateMaster.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjFeltQltRateMaster.getAttribute("FROM_IP").getString();
                
                DataModelUpdateHistory.addRow(rowData);
            }
            //============================================================//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Sets data to the Class Object
    private void SetData() {
        ObjFeltQltRateMaster.setAttribute("DOC_NO", txtDocNo.getText().trim());
        ObjFeltQltRateMaster.setAttribute("DOC_DATE", txtDocDate.getText().trim());
        ObjFeltQltRateMaster.setAttribute("WH_CD", txtWhCd.getText().trim());
        ObjFeltQltRateMaster.setAttribute("PRODUCT_CODE", txtQualityID.getText().trim());
        ObjFeltQltRateMaster.setAttribute("PRODUCT_DESC", txtQualityDesc.getText().trim().toUpperCase());
        ObjFeltQltRateMaster.setAttribute("EFFECTIVE_FROM", txtEffectiveFrom.getText().trim());
        ObjFeltQltRateMaster.setAttribute("EFFECTIVE_TO", txtEffectiveTo.getText().trim());
        
        if (txtWTRate.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("WT_RATE", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("WT_RATE", Float.parseFloat(txtWTRate.getText().trim()));
        }
        
        if (txtSynPercent.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("SYN_PER", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("SYN_PER", Float.parseFloat(txtSynPercent.getText().trim()));
        }
        
        ObjFeltQltRateMaster.setAttribute("SQM_IND", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbSQMIndicator)));
        ObjFeltQltRateMaster.setAttribute("CHEM_TRT_IND", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbChemicalTreatmentIndicator)));
        ObjFeltQltRateMaster.setAttribute("PIN_IND", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbPINIndicator)));
        ObjFeltQltRateMaster.setAttribute("SPR_IND", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbSpiralIndicator)));
        ObjFeltQltRateMaster.setAttribute("SUR_IND", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbSurchargeIndicator)));
        ObjFeltQltRateMaster.setAttribute("EXC_CAT_IND", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbExcCatIndicator)));
        
        if (txtSqmChrg.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("SQM_CHRG", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("SQM_CHRG", Float.parseFloat(txtSqmChrg.getText().trim()));
        }
        
        if (txtChemTrtChrg.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("CHEM_TRT_CHRG", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("CHEM_TRT_CHRG", Float.parseFloat(txtChemTrtChrg.getText().trim()));
        }
        
        if (txtPinChrg.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("PIN_CHRG", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("PIN_CHRG", Float.parseFloat(txtPinChrg.getText().trim()));
        }
        
        if (txtSpiralChrg.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("SPR_CHRG", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("SPR_CHRG", Float.parseFloat(txtSpiralChrg.getText().trim()));
        }
        
        if (txtSurChrg.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("SUR_CHRG", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("SUR_CHRG", Float.parseFloat(txtSurChrg.getText().trim()));
        }
        
        if (txtExcCatChrg.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("EXC_CAT_CHRG", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("EXC_CAT_CHRG", Float.parseFloat(txtExcCatChrg.getText().trim()));
        }
        
        ObjFeltQltRateMaster.setAttribute("GROUP_NAME", txtGroup.getText().trim().toUpperCase());
        ObjFeltQltRateMaster.setAttribute("DIVERSION_GROUP", txtDivGroup.getText().trim().toUpperCase());
        ObjFeltQltRateMaster.setAttribute("REMARKS", txtRemarks.getText().trim().toUpperCase());
        
        ObjFeltQltRateMaster.setAttribute("CATEGORY", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbCategory)));
        ObjFeltQltRateMaster.setAttribute("FABRIC_CATG", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbFabricCategory)));
        ObjFeltQltRateMaster.setAttribute("POSITION_CATG", Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbPositionCategory)));
        
        if (txtOldRate.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("OLD_RATE", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("OLD_RATE", Float.parseFloat(txtOldRate.getText().trim()));
        }
        
        if (txtWeightRateCriteria.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("WEIGHT_RATE_CRITERIA", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("WEIGHT_RATE_CRITERIA", Float.parseFloat(txtWeightRateCriteria.getText().trim()));
        }
        
        if (txtSurChargeLenCriteria.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("SURCHARGE_LENGTH_CRITERIA", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("SURCHARGE_LENGTH_CRITERIA", Float.parseFloat(txtSurChargeLenCriteria.getText().trim()));
        }

        if (txtSurChrgPer1.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("SURCHARGE_1_PER", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("SURCHARGE_1_PER", Float.parseFloat(txtSurChrgPer1.getText().trim()));
        }
        
        if (txtSurChrgPer2.getText().equalsIgnoreCase("")) {
            ObjFeltQltRateMaster.setAttribute("SURCHARGE_2_PER", String.valueOf(0));
        } else {
            ObjFeltQltRateMaster.setAttribute("SURCHARGE_2_PER", Float.parseFloat(txtSurChrgPer2.getText().trim()));
        }
        
//        ObjFeltQltRateMaster.setAttribute("SYN_PER",Float.parseFloat(txtSynPercent.getText().trim()));
//        ObjFeltQltRateMaster.setAttribute("SQM_RATE",Float.parseFloat(txtSQMRate.getText().trim()));
//        ObjFeltQltRateMaster.setAttribute("WT_RATE",Float.parseFloat(txtWTRate.getText().trim()));
//        ObjFeltQltRateMaster.setAttribute("CHEM_TRT_IND",Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbChemicalTreatmentIndicator)));
//        ObjFeltQltRateMaster.setAttribute("PIN_IND",Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbPINIndicator)));
//        ObjFeltQltRateMaster.setAttribute("SPRL_IND",Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbSpiralIndicator)));
//        ObjFeltQltRateMaster.setAttribute("SUR_IND",Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbSurchargeIndicator)));
//        ObjFeltQltRateMaster.setAttribute("SQM_IND",Integer.parseInt(EITLERPGLOBAL.getCombostrCode(cmbSQMIndicator)));
//        ObjFeltQltRateMaster.setAttribute("CHARGES",Float.parseFloat(txtCharges.getText().trim()));
//        ObjFeltQltRateMaster.setAttribute("GROUP",txtGroup.getText().trim().toUpperCase());
//        ObjFeltQltRateMaster.setAttribute("REMARKS",txtRemarks.getText().trim());
        //----- Update Approval Specific Fields -----------//
        ObjFeltQltRateMaster.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjFeltQltRateMaster.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjFeltQltRateMaster.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjFeltQltRateMaster.setAttribute("FROM_REMARKS", txtToRemarks.getText().trim().toUpperCase());
        ObjFeltQltRateMaster.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
        
        if (OpgApprove.isSelected()) {
            ObjFeltQltRateMaster.setAttribute("APPROVAL_STATUS", "A");
        }
        
        if (OpgFinal.isSelected()) {
            ObjFeltQltRateMaster.setAttribute("APPROVAL_STATUS", "F");
        }
        
        if (OpgReject.isSelected()) {
            ObjFeltQltRateMaster.setAttribute("APPROVAL_STATUS", "R");
            ObjFeltQltRateMaster.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }
        
        if (OpgHold.isSelected()) {
            ObjFeltQltRateMaster.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjFeltQltRateMaster.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjFeltQltRateMaster.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjFeltQltRateMaster.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjFeltQltRateMaster.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
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
            lnFromUserId = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {
            int FromUserID = clsFeltProductionApprovalFlow.getFromID(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString());
            lnFromUserId = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(601, FromUserID, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString());
            
            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }
        
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateSendToCombo();
        
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
            if (clsFeltProductionApprovalFlow.IsCreator(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString())) {
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
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6051, 60511)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6051, 60512)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6051, 60513)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6051, 60515)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }
    
    private void Add() {
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 601;
        aList.FirstFreeNo = 199;
        
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        FormatGrid();
        
        SelPrefix = aList.Prefix; //Selected Prefix;
        SelSuffix = aList.Suffix;
        FFNo = aList.FirstFreeNo;
        
        SetupApproval();
        //Display newly generated document no.
        txtWhCd.setText(String.valueOf(2));
        txtDocNo.setText(clsFeltQltRateMaster.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 601, FFNo, false));
        txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());
        
        lblTitle.setText("FELT QUALITY RATE MASTER - " + txtDocNo.getText());
        lblTitle.setBackground(Color.GRAY);
    }
    
    private void Edit() {
        String lDocNo = ObjFeltQltRateMaster.getAttribute("DOC_NO").getString();
        if (ObjFeltQltRateMaster.IsEditable(lDocNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            
            if (clsFeltProductionApprovalFlow.IsCreator(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString())) {
                SetFields(true);
            } else {
                EnableApproval();
            }
            
            DisableToolbar();
        } else {
            JOptionPane.showMessageDialog(this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void Save() {
        //Form level validations
        if (txtQualityID.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Quality ID.");
            return;
        }
        
        if (txtQualityID.getText().trim().length() != 6) {
            JOptionPane.showMessageDialog(this, "Quality ID must be of 6 character.");
            return;
        }

//        // check if quality exists
//        if (EditMode == EITLERPGLOBAL.ADD) {
//            if (ObjFeltQltRateMaster.checkQualityNo(txtQualityID.getText().trim())) {
//                JOptionPane.showMessageDialog(this, "Quality No. " + txtQualityID.getText().trim() + " already exists.");
//                return;
//            }
//            //generating document no.
//            txtDocNo.setText("FR" + Integer.toString(EITLERPGLOBAL.FinYearFrom).substring(2) + txtQualityID.getText().trim());
//        }
        if (txtQualityDesc.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Quality Description.");
            return;
        }
        
        if (txtQualityDesc.getText().trim().length() > 35) {
            JOptionPane.showMessageDialog(this, "Quality Description can not more than 35 character.");
            return;
        }
        
        try {
            if (txtSynPercent.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Enter Synthetic Percentage.");
                return;
            } else {
                Float.parseFloat(txtSynPercent.getText().trim());
            }

//            if (txtSQMRate.getText().trim().equals("")) {
//                JOptionPane.showMessageDialog(this, "Enter Square Meter Rate.");
//                return;
//            } else {
//                Float.parseFloat(txtSQMRate.getText().trim());
//            }
//            if (txtWTRate.getText().trim().equals("")) {
//                JOptionPane.showMessageDialog(this, "Enter Weight Rate.");
//                return;
//            } else {
//                Float.parseFloat(txtWTRate.getText().trim());
//            }
//            if (txtCharges.getText().trim().equals("")) {
//                JOptionPane.showMessageDialog(this, "Enter Charges.");
//                return;
//            } else {
//                Float.parseFloat(txtCharges.getText().trim());
//            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Enter Correct Details. Error is : " + nfe.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            nfe.printStackTrace();
            return;
        }

//        if (cmbChemicalTreatmentIndicator.getSelectedIndex() == 0) {
//            JOptionPane.showMessageDialog(this, "Select Chemical Treatment Indicator.");
//            return;
//        }
//
//        if (cmbSurchargeIndicator.getSelectedIndex() == 0) {
//            JOptionPane.showMessageDialog(this, "Select Surcharge Indicator.");
//            return;
//        }
//
//        if (cmbPINIndicator.getSelectedIndex() == 0) {
//            JOptionPane.showMessageDialog(this, "Select PIN Indicator.");
//            return;
//        }
//
//        if (cmbSpiralIndicator.getSelectedIndex() == 0) {
//            JOptionPane.showMessageDialog(this, "Select Spiral Indicator.");
//            return;
//        }
//
//        if (cmbSQMIndicator.getSelectedIndex() == 0) {
//            JOptionPane.showMessageDialog(this, "Select Square Metre Indicator.");
//            return;
//        }
        if (txtGroup.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Group.");
            return;
        }
        
        if (txtDivGroup.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Diversion Group.");
            return;
        }
        /*
         if (txtRemarks.getText().trim().equals("")) {
         JOptionPane.showMessageDialog(this,"Enter Remarks.");
         return;
         }
         */
        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select the Hierarchy.");
            return;
        }
        
        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(this, "Select the Approval Action.");
            return;
        }
        
        if (OpgReject.isSelected() && txtToRemarks.getText().trim().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter the remarks for rejection");
            return;
        }
        
        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(this, "Select the user, to whom rejected document to be send");
            return;
        }
        
        SetData();
        
        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjFeltQltRateMaster.Insert()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + ObjFeltQltRateMaster.LastError);
                return;
            }
        }
        
        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjFeltQltRateMaster.Update()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving Editing. Error is " + ObjFeltQltRateMaster.LastError);
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
    
    private void Cancel() {
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
    }
    
    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.FeltQualityRateMaster.frmFindFeltQltRate", true);
        frmFindFeltQltRate ObjFindFeltQltRate = (frmFindFeltQltRate) ObjLoader.getObj();
        
        if (ObjFindFeltQltRate.Cancelled == false) {
            if (!ObjFeltQltRateMaster.Filter(ObjFindFeltQltRate.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.", "Find Felt Quality Rate", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    // find rate update by doc no
    public void Find(String docNo) {
        ObjFeltQltRateMaster.Filter("DOC_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    // find all pending document
    public void FindWaiting() {
        ObjFeltQltRateMaster.Filter("DOC_NO IN (SELECT PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO FROM PRODUCTION.FELT_QLT_RATE_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_QLT_RATE_MASTER.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=" + 601 + ")");
        SetMenuForRights();
        DisplayData();
    }
    
    private void MoveFirst() {
        ObjFeltQltRateMaster.MoveFirst();
        DisplayData();
    }
    
    private void MovePrevious() {
        ObjFeltQltRateMaster.MovePrevious();
        DisplayData();
    }
    
    private void MoveNext() {
        ObjFeltQltRateMaster.MoveNext();
        DisplayData();
    }
    
    private void MoveLast() {
        ObjFeltQltRateMaster.MoveLast();
        DisplayData();
    }
    
    private void EnableApproval() {
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        txtToRemarks.setEnabled(true);
        SetupApproval();

//        //========== Setting Up Header Fields ================//
//        String FieldName = "";
//        int SelHierarchy = EITLERPGLOBAL.getComboCode(cmbHierarchy);
//
//        for (int i = 0; i < Tab1.getComponentCount() - 1; i++) {
//            if (Tab1.getComponent(i).getName() != null) {
//                FieldName = Tab1.getComponent(i).getName();
//                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
//                    Tab1.getComponent(i).setEnabled(true);
//                }
//            }
//        }
//        //=============== Header Fields Setup Complete =================//
//
//        //=============== Setting Table Fields ==================//
//        DataModel.ClearAllReadOnly();
//        for (int i = 0; i < Table.getColumnCount(); i++) {
//            FieldName = DataModel.getVariable(i);
//
//            if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
//                //Do Nothing
//            } else {
//                DataModel.SetReadOnly(i);
//            }
//        }
//        //=======================================================//
    }
    
    private void FormatGrid() {
        try {
            DataModel = new EITLTableModel();
            Table1.removeAll();
            
            Table1.setModel(DataModel);
            Table1.setAutoResizeMode(0);
            DataModel.TableReadOnly(true);
            
            DataModel.addColumn("SrNo"); //0 - Read Only
            DataModel.addColumn("Product Code"); //1
            DataModel.addColumn("Effective From");
            DataModel.addColumn("Effective To");
            DataModel.addColumn("SQM Charges"); //2
            DataModel.addColumn("Weight Rate"); //3

            DataModel.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel.SetVariable(1, "Product Code"); //1
            DataModel.SetVariable(2, "Effective From");
            DataModel.SetVariable(3, "Effective To");
            DataModel.SetVariable(4, "SQM Charges"); //2
            DataModel.SetVariable(5, "Weight Rate"); //3

            Table1.getColumnModel().getColumn(0).setMinWidth(20);
            Table1.getColumnModel().getColumn(1).setMinWidth(100);
            Table1.getColumnModel().getColumn(2).setMinWidth(100);
            Table1.getColumnModel().getColumn(3).setMinWidth(100);
            Table1.getColumnModel().getColumn(4).setMinWidth(100);
            Table1.getColumnModel().getColumn(5).setMinWidth(100);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void FormatGrid1() {
//        try {
//            DataModel = new EITLTableModel();
//            Table.removeAll();
//
//            Table.setModel(DataModel);
//            Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//
//            //Add Columns to it
//            DataModel.addColumn("Sr."); //0 - Read Only
//            DataModel.addColumn("Quality ID"); //1 //Read Only
//            DataModel.addColumn("Sqr. Mtr. Rate");
//            DataModel.addColumn("Weight Rate");
//            DataModel.addColumn("Rate From Date");
//            DataModel.addColumn("Rate To Date");
//            DataModel.addColumn("Charges");
//            DataModel.addColumn("Charges From Date");
//            DataModel.addColumn("Charges To Date");
//            DataModel.addColumn("Doc No");
//
//            DataModel.TableReadOnly(true);
//            DataModel.SetReadOnly(0);
//            DataModel.SetReadOnly(1);
//
//            TableColumnModel tcm = Table.getColumnModel();
//            tcm.getColumn(0).setPreferredWidth(20);
//            tcm.getColumn(1).setPreferredWidth(80);
//            tcm.getColumn(2).setPreferredWidth(100);
//            tcm.getColumn(3).setPreferredWidth(90);
//            tcm.getColumn(4).setPreferredWidth(110);
//            tcm.getColumn(5).setPreferredWidth(100);
//            tcm.getColumn(6).setPreferredWidth(90);
//            tcm.getColumn(7).setPreferredWidth(130);
//            tcm.getColumn(8).setPreferredWidth(110);
//            tcm.getColumn(9).setMaxWidth(0);
//        } catch (Exception e) {
//
//        }
//    }
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
        DataModelUpdateHistory.addColumn("From Ip");
        
        TableColumnModel tcm = TableUpdateHistory.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(2).setPreferredWidth(50);
        tcm.getColumn(3).setPreferredWidth(20);
    }

    //Generates Hierarchy Combo Box
    private void GenerateHierarchyCombo() {
        HashMap hmHierarchyList = new HashMap();
        
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        hmHierarchyList = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=601 ");
        
        if (EditMode == EITLERPGLOBAL.EDIT) {
            hmHierarchyList = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=601 ");
        }
        for (int i = 1; i <= hmHierarchyList.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = ObjHierarchy.getAttribute("HIERARCHY_NAME").getString();
            cmbHierarchyModel.addElement(aData);
        }
    }

    //Generates Send To Combo Box for Rejected User
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString(), ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                }
                
                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString(), ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }
    }

    //Generates Send To Combo Box
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
                    aData.Text = ObjUser.getAttribute("USER_NAME").getString();
                    
                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    } else {
                        cmbSendToModel.addElement(aData);
                    }
                }
            } else {
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(601, ObjFeltQltRateMaster.getAttribute("DOC_NO").getString());
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = ObjUser.getAttribute("USER_NAME").getString();
                    cmbSendToModel.addElement(aData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // generates all combos
//    private void GenerateCombos() {
//        //--- Generate Chemical Treatment Combo ------//
//        cmbChemicalTreatmentIndicatorModel =new EITLComboModel();
//        cmbChemicalTreatmentIndicator.removeAllItems();
//        cmbChemicalTreatmentIndicator.setModel(cmbChemicalTreatmentIndicatorModel);
//        
//        ComboData aData=new ComboData();
//        aData.strCode="-1";
//        aData.Text="Chemical Treatment";
//        cmbChemicalTreatmentIndicatorModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="0";
//        aData.Text="Non Applicable";
//        cmbChemicalTreatmentIndicatorModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="1";
//        aData.Text="Applicable";
//        cmbChemicalTreatmentIndicatorModel.addElement(aData);
//        //===============================//
//        
//        //--- Generate PIN Indicator Combo ------//
//        cmbPINIndicatormodel =new EITLComboModel();
//        cmbPINIndicator.removeAllItems();
//        cmbPINIndicator.setModel(cmbPINIndicatormodel);
//        
//        aData=new ComboData();
//        aData.strCode="-1";
//        aData.Text="PIN Indicator";
//        cmbPINIndicatormodel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="0";
//        aData.Text="Non Applicable";
//        cmbPINIndicatormodel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="1";
//        aData.Text="Applicable";
//        cmbPINIndicatormodel.addElement(aData);
//        //===============================//
//        
//        //--- Generate Spiral Indicator Combo ------//
//        cmbSpiralIndicatorModel =new EITLComboModel();
//        cmbSpiralIndicator.removeAllItems();
//        cmbSpiralIndicator.setModel(cmbSpiralIndicatorModel);
//        
//        aData=new ComboData();
//        aData.strCode="-1";
//        aData.Text="Spiral Indicator";
//        cmbSpiralIndicatorModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="0";
//        aData.Text="Non Applicable";
//        cmbSpiralIndicatorModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="1";
//        aData.Text="Applicable";
//        cmbSpiralIndicatorModel.addElement(aData);
//        //===============================//
//        
//        //--- Generate sur charges Indicator Combo ------//
//        cmbSurchargeIndicatorModel =new EITLComboModel();
//        cmbSurchargeIndicator.removeAllItems();
//        cmbSurchargeIndicator.setModel(cmbSurchargeIndicatorModel);
//        
//        aData=new ComboData();
//        aData.strCode="-1";
//        aData.Text="Surcharge Indicator";
//        cmbSurchargeIndicatorModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="0";
//        aData.Text="Non Applicable";
//        cmbSurchargeIndicatorModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="1";
//        aData.Text="Applicable";
//        cmbSurchargeIndicatorModel.addElement(aData);
//        //===============================//
//        
//        //--- Generate SQM Indicator Combo ------//
//        cmbSQMIndicatorModel =new EITLComboModel();
//        cmbSQMIndicator.removeAllItems();
//        cmbSQMIndicator.setModel(cmbSQMIndicatorModel);
//        
//        aData=new ComboData();
//        aData.strCode="-1";
//        aData.Text="SQM Indicator";
//        cmbSQMIndicatorModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="0";
//        aData.Text="Non Applicable";
//        cmbSQMIndicatorModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.strCode="1";
//        aData.Text="Applicable";
//        cmbSQMIndicatorModel.addElement(aData);
//        //===============================//
//    }
    private void GenerateIndCombos() {
        //--- Generate Chemical Treatment Combo ------//
        cmbChemicalTreatmentIndicatorModel = new EITLComboModel();
        cmbChemicalTreatmentIndicator.removeAllItems();
        cmbChemicalTreatmentIndicator.setModel(cmbChemicalTreatmentIndicatorModel);
        
        ComboData aData = new ComboData();
//        aData.strCode = "-1";
//        aData.Text = "Chemical Treatment";
//        cmbChemicalTreatmentIndicatorModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbChemicalTreatmentIndicatorModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "Applicable";
        cmbChemicalTreatmentIndicatorModel.addElement(aData);
        //===============================//

        //--- Generate PIN Indicator Combo ------//
        cmbPINIndicatormodel = new EITLComboModel();
        cmbPINIndicator.removeAllItems();
        cmbPINIndicator.setModel(cmbPINIndicatormodel);
//
//        aData = new ComboData();
//        aData.strCode = "-1";
//        aData.Text = "PIN Indicator";
//        cmbPINIndicatormodel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbPINIndicatormodel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "Applicable";
        cmbPINIndicatormodel.addElement(aData);
        //===============================//

        //--- Generate Spiral Indicator Combo ------//
        cmbSpiralIndicatorModel = new EITLComboModel();
        cmbSpiralIndicator.removeAllItems();
        cmbSpiralIndicator.setModel(cmbSpiralIndicatorModel);
//
//        aData = new ComboData();
//        aData.strCode = "-1";
//        aData.Text = "Spiral Indicator";
//        cmbSpiralIndicatorModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbSpiralIndicatorModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "Applicable";
        cmbSpiralIndicatorModel.addElement(aData);
        //===============================//

        //--- Generate sur charges Indicator Combo ------//
        cmbSurchargeIndicatorModel = new EITLComboModel();
        cmbSurchargeIndicator.removeAllItems();
        cmbSurchargeIndicator.setModel(cmbSurchargeIndicatorModel);
//
//        aData = new ComboData();
//        aData.strCode = "-1";
//        aData.Text = "Surcharge Indicator";
//        cmbSurchargeIndicatorModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbSurchargeIndicatorModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "Applicable";
        cmbSurchargeIndicatorModel.addElement(aData);
        //===============================//

        //--- Generate SQM Indicator Combo ------//
        cmbSQMIndicatorModel = new EITLComboModel();
        cmbSQMIndicator.removeAllItems();
        cmbSQMIndicator.setModel(cmbSQMIndicatorModel);
//
//        aData = new ComboData();
//        aData.strCode = "-1";
//        aData.Text = "SQM Indicator";
//        cmbSQMIndicatorModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbSQMIndicatorModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "Applicable";
        cmbSQMIndicatorModel.addElement(aData);
        //===============================//

        //--- Generate EXC CAT Indicator Combo ------//
        cmbExcCatIndicatorModel = new EITLComboModel();
        cmbExcCatIndicator.removeAllItems();
        cmbExcCatIndicator.setModel(cmbExcCatIndicatorModel);
//
//        aData = new ComboData();
//        aData.strCode = "-1";
//        aData.Text = "SQM Indicator";
//        cmbSQMIndicatorModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbExcCatIndicatorModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "1";
        cmbExcCatIndicatorModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "2";
        aData.Text = "2";
        cmbExcCatIndicatorModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "3";
        aData.Text = "3";
        cmbExcCatIndicatorModel.addElement(aData);
        //===============================//
    }
    
    private void GenerateCatgCombos() {
        //--- Generate Category Combo ------//
        cmbCategoryModel = new EITLComboModel();
        cmbCategory.removeAllItems();
        cmbCategory.setModel(cmbCategoryModel);
        
        ComboData aData = new ComboData();
        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbCategoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "ACNE";
        cmbCategoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "2";
        aData.Text = "DRY";
        cmbCategoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "3";
        aData.Text = "WET";
        cmbCategoryModel.addElement(aData);

        //===============================//
        //--- Generate Fabric Category Combo ------//
        cmbFabricCategoryModel = new EITLComboModel();
        cmbFabricCategory.removeAllItems();
        cmbFabricCategory.setModel(cmbFabricCategoryModel);
        
        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbFabricCategoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "BASE";
        cmbFabricCategoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "2";
        aData.Text = "CLOTH";
        cmbFabricCategoryModel.addElement(aData);

        //===============================//
        //--- Generate Position Category Combo ------//
        cmbPositionCategoryModel = new EITLComboModel();
        cmbPositionCategory.removeAllItems();
        cmbPositionCategory.setModel(cmbPositionCategoryModel);
        
        aData = new ComboData();
        aData.strCode = "0";
        aData.Text = "Non Applicable";
        cmbPositionCategoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "1";
        aData.Text = "DRIER";
        cmbPositionCategoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "2";
        aData.Text = "PRESS";
        cmbPositionCategoryModel.addElement(aData);

        //===============================//
    }

//    public static void PreviewReport() {
//        String strSQL = "";
//        String Condition = "";
//        ResultSet rsData = null;
//        TReportEngine objEngine1 = new TReportEngine();
//        TReportWriter.SimpleDataProvider.TTable objData = new TReportWriter.SimpleDataProvider.TTable();
//
//        try {
//            objData.AddColumn("PARTY_CODE");
//            objData.AddColumn("BALE_NO");
//            objData.AddColumn("BALE_DATE");
//            objData.AddColumn("CHARGE_CODE");
//            objData.AddColumn("INVOICE_AMT");
//            objData.AddColumn("MASTER_AMT");
//            objData.AddColumn("BALANCE_AMT");
//            objData.AddColumn("REMARK");
//
//            strSQL = "SELECT PARTY_CODE,BALE_NO,PKG_BALE_DATE AS BALE_DATE,CHARGE_CODE,INVOICE_AMT,CRITICAL_LIMIT_AMT AS MASTER_AMT,INV_CRITICAL_LIMIT_AMT AS BALANCE_AMT,CASE WHEN FLAG=0 THEN \"UNSUFFICIENT BAL\" ELSE \"PROCESSED\" END AS REMARK FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,PRODUCTION.FELT_PKG_SLIP_HEADER P WHERE BALE_NO=PKG_BALE_NO";
//
//            rsData = data.getResult(strSQL);
//            System.out.println(strSQL);
//            rsData.first();
//            TReportWriter.SimpleDataProvider.TRow objRow = null;
//            if (rsData.getRow() > 0) {
//                while (!rsData.isAfterLast()) {
//                    objRow = objData.newRow();
//                    
//                    objRow.setValue("PARTY_CODE", rsData.getString("PARTY_CODE"));
//                    objRow.setValue("BALE_NO",rsData.getString("BALE_NO"));
//                    objRow.setValue("BALE_DATE",EITLERPGLOBAL.formatDate(rsData.getString("BALE_DATE")));
//                    objRow.setValue("CHARGE_CODE",rsData.getString("CHARGE_CODE"));
//                    objRow.setValue("INVOICE_AMT",Double.toString(rsData.getDouble("INVOICE_AMT")));
//                    objRow.setValue("MASTER_AMT",Double.toString(rsData.getDouble("MASTER_AMT")));
//                    objRow.setValue("BALANCE_AMT",Double.toString(rsData.getDouble("BALANCE_AMT")));
//                    objRow.setValue("REMARK",rsData.getString("REMARK"));
//                    
//                    objData.AddRow(objRow);
//                    
//                    rsData.next();
//                }
//            }
//
//            HashMap Parameters = new HashMap();
//
//            //Parameters.put("PARTY_NAME", clsPartyMaster.getAccountName(MainCode.trim(), ""));
//            //Parameters.put("AS_ON_DATE", EITLERPGLOBAL.getCurrentDate());
//            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
//            objEngine1.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/rptFeltInvList.rpt", Parameters, objData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void PreviewReport() {
        String prodNo = txtQualityID.getText().trim();
        
        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Conn = data.getConn();
            st = Conn.createStatement();
            
            HashMap parameterMap = new HashMap();
            
            parameterMap.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
            
            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
            
            String strSQL = "SELECT PRODUCT_CODE,PRODUCT_DESC,SYN_PER,SQM_CHRG,WT_RATE,EFFECTIVE_FROM,EFFECTIVE_TO,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,SQM_IND,EXC_CAT_IND,EXC_CAT_CHRG,GROUP_NAME,DIVERSION_GROUP,CASE WHEN CATEGORY=1 THEN 'ACNE' ELSE CASE WHEN CATEGORY=2 THEN 'DRY' ELSE CASE WHEN CATEGORY=3 THEN 'WET' ELSE ' ' END END END AS CATEGORY, ";
            strSQL += "CASE WHEN FABRIC_CATG=1 THEN 'BASE' ELSE CASE WHEN FABRIC_CATG=2 THEN 'CLOTH' ELSE ' ' END END AS FABRIC_CATG, ";
            strSQL += "CASE WHEN POSITION_CATG=1 THEN 'DRIER' ELSE CASE WHEN POSITION_CATG=2 THEN 'PRESS' ELSE ' ' END END AS POSITION_CATG ";
            strSQL += "FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + prodNo + "' AND APPROVED=1 AND CANCELED=0 ORDER BY EFFECTIVE_FROM";
            
            rpt.setReportName("/EITLERP/FeltSales/FeltQualityRateMaster/QltRateHistory.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                Conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
