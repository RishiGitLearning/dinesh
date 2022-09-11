/*
 * frmTemplate.java
 *
 * Created on April 7, 2004, 3:10 PM
 */
package EITLERP.Stores;

/**
 *
 * @author nhpatel
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Purchase.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.text.*;
import java.net.*;
import java.io.*;
import EITLERP.Utils.*;
import EITLERP.Finance.*;
import java.sql.*;
import java.math.BigDecimal;

/**
 *
 */
public class frmGRNGen extends javax.swing.JApplet {
    
    private int EditMode = 0;
    
    private EITLTableModel DataModelH;
    private EITLTableModel DataModelL;
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    
    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    clsTaxColumn ObjTax = new clsTaxColumn();
    clsColumn ObjColumn = new clsColumn();
    
    private JEP myParser = new JEP();
    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;
    
    private clsGRNGen ObjGRN;
    
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbFinHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbTransportModel;
    private EITLComboModel cmbCurrencyModel;
    private EITLComboModel cmbStatusModel;
    private EITLComboModel cmbReasonModel;
    
    private EITLComboModel cmbPFPostModel;
    private EITLComboModel cmbFreightPostModel;
    private EITLComboModel cmbOctroiPostModel;
    private EITLComboModel cmbInsurancePostModel;
    private EITLComboModel cmbClearancePostModel;
    private EITLComboModel cmbAirFreightPostModel;
    private EITLComboModel cmbOthersPostModel;
    private EITLComboModel cmbPaymentTypeModel;
    
    private EITLTableModel DataModelA;
    
    private boolean HistoryView = false;
    private String theDocNo = "";
    private EITLTableModel DataModelHS;
    
    String cellLastValueL = "";
    String cellLastValueH = "";
    
    private double CurrencyRate = 0;
    
    public frmPendingApprovals frmPA;
    
    private EITLTableModel DataModel_invoiced;
    private EITLTableModel DataModel_received;
    private EITLTableModel DataModel_debitnote;
    private EITLTableModel DataModelHSN;
    private EITLTableModel DataModelHSNGRNPJV;
    
    private String[] c = new String[100];
    private String[] a = new String[100];
    private String[] b = new String[100];
    
    private String hsnCode = "";
    private double netAmt = 0;
    private double CGSTAmt = 0;
    private double SGSTAmt = 0;
    private double IGSTAmt = 0;
    private double RCMAmt = 0;
    private double CompositionAmt = 0;
    private double GSTCompCessAmt = 0;
    
    /**
     * Creates new form frmTemplate
     */
    public void init() {
        
        System.gc();
        setSize(773, 525);
        initComponents();
        
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
        
        ObjTax.LoadData((int) EITLERPGLOBAL.gCompanyID);
        ObjColumn.LoadData((int) EITLERPGLOBAL.gCompanyID);
        
        FormatGrid();
        FormatGrid_H();
        FormatGrid_HSN();
        FormatGridHSNGRNPJV();
        SetNumberFormats();
        
        GenerateCombos();
        ObjGRN = new clsGRNGen();
        
        SetMenuForRights();
        
        if (getName().equals("Link")) {
            
        } else {
            if (ObjGRN.LoadData(EITLERPGLOBAL.gCompanyID)) {
                ObjGRN.MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while loading data. Error is " + ObjGRN.LastError);
            }
        }
        
        txtAuditRemarks.setVisible(false);
        lblFinHierarchy.setVisible(false);
        cmbFinHierarchy.setVisible(false);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu11 = new javax.swing.JPopupMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
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
        Tab1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSuppCode = new javax.swing.JTextField();
        txtSuppName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtChalanNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtChalanDate = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtLRNo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtLRDate = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtInvoiceNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtInvoiceDate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbTransporter = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        txtGatepassNo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cmbCurrency = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        chkImportConcess = new javax.swing.JCheckBox();
        chkCancelled = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        cmdNext1 = new javax.swing.JButton();
        txtCurrencyRate = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        txtSuffix = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cmdRemarksBig = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txtPaymentRate = new javax.swing.JTextField();
        cmdChange = new javax.swing.JButton();
        chkKeep = new javax.swing.JCheckBox();
        lblTransName = new javax.swing.JLabel();
        txtTransName = new javax.swing.JTextField();
        Tab2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableL = new javax.swing.JTable();
        HeaderPane = new javax.swing.JScrollPane();
        TableH = new javax.swing.JTable();
        cmdInsert = new javax.swing.JButton();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        cmdNext2 = new javax.swing.JButton();
        cmdBack2 = new javax.swing.JButton();
        txtGrossAmount = new javax.swing.JTextField();
        txtNetAmount = new javax.swing.JTextField();
        cmdShowPO = new javax.swing.JButton();
        cmdShowMIR = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtFinalAmount = new javax.swing.JTextField();
        lblColumnTotal = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtInvoiceAmount = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbPaymentType = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        cmbPFPost = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        cmbFreightPost = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        cmbOctroiPost = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        cmbInsurancePost = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        cmbClearancePost = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        cmbAirFreightPost = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        cmbOthersPost = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TableHSN = new javax.swing.JTable();
        btnHSNUpdate = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        TableHSNGRNPJV = new javax.swing.JTable();
        Tab3 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        cmbFinHierarchy = new javax.swing.JComboBox();
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
        cmdBack3 = new javax.swing.JButton();
        cmdFromRemarksBig = new javax.swing.JButton();
        lblFinHierarchy = new javax.swing.JLabel();
        cmbHierarchy = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        lblDocumentHistory = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableHS = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdPreviewA = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        jMenuItem1.setText("GST");
        jMenuItem1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jMenuItem1ItemStateChanged(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Non GST");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setText("Non GST Updated");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

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
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText(" GOODS RECEIPT NOTE (General)");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 804, 25);

        Tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab1FocusGained(evt);
            }
        });
        Tab1.setLayout(null);

        jLabel2.setText("GRN No.");
        Tab1.add(jLabel2);
        jLabel2.setBounds(52, 18, 56, 15);

        txtDocNo.setEditable(false);
        txtDocNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDocNoFocusLost(evt);
            }
        });
        Tab1.add(txtDocNo);
        txtDocNo.setBounds(114, 14, 100, 19);

        jLabel3.setText("Date");
        Tab1.add(jLabel3);
        jLabel3.setBounds(254, 18, 34, 15);

        txtDocDate.setEditable(false);
        txtDocDate.setName("GRN_DATE"); // NOI18N
        txtDocDate.setNextFocusableComponent(txtSuppCode);
        txtDocDate.setEnabled(false);
        txtDocDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDocDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDocDateFocusLost(evt);
            }
        });
        Tab1.add(txtDocDate);
        txtDocDate.setBounds(290, 16, 100, 19);

        jLabel4.setText("Supplier");
        Tab1.add(jLabel4);
        jLabel4.setBounds(54, 109, 52, 15);

        txtSuppCode.setEnabled(false);
        txtSuppCode.setName("SUPP_ID"); // NOI18N
        txtSuppCode.setNextFocusableComponent(txtChalanNo);
        txtSuppCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSuppCodeActionPerformed(evt);
            }
        });
        txtSuppCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusLost(evt);
            }
        });
        txtSuppCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSuppCodeKeyPressed(evt);
            }
        });
        Tab1.add(txtSuppCode);
        txtSuppCode.setBounds(114, 105, 62, 19);

        txtSuppName.setEnabled(false);
        Tab1.add(txtSuppName);
        txtSuppName.setBounds(178, 105, 212, 19);

        jLabel5.setText("Chalan No.");
        Tab1.add(jLabel5);
        jLabel5.setBounds(40, 145, 68, 15);

        txtChalanNo.setName("CHALAN_NO"); // NOI18N
        txtChalanNo.setNextFocusableComponent(txtChalanDate);
        txtChalanNo.setEnabled(false);
        txtChalanNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtChalanNoFocusGained(evt);
            }
        });
        Tab1.add(txtChalanNo);
        txtChalanNo.setBounds(114, 141, 114, 19);

        jLabel6.setText("Date");
        Tab1.add(jLabel6);
        jLabel6.setBounds(254, 145, 34, 15);

        txtChalanDate.setName("CHALAN_DATE"); // NOI18N
        txtChalanDate.setNextFocusableComponent(txtLRNo);
        txtChalanDate.setEnabled(false);
        txtChalanDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtChalanDateFocusGained(evt);
            }
        });
        Tab1.add(txtChalanDate);
        txtChalanDate.setBounds(290, 143, 100, 19);

        jLabel7.setText("L.R. No.");
        Tab1.add(jLabel7);
        jLabel7.setBounds(53, 170, 50, 15);

        txtLRNo.setName("LR_NO"); // NOI18N
        txtLRNo.setNextFocusableComponent(txtLRDate);
        txtLRNo.setEnabled(false);
        txtLRNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLRNoFocusGained(evt);
            }
        });
        Tab1.add(txtLRNo);
        txtLRNo.setBounds(114, 166, 114, 19);

        jLabel8.setText("Date");
        Tab1.add(jLabel8);
        jLabel8.setBounds(255, 170, 34, 15);

        txtLRDate.setName("LR_DATE"); // NOI18N
        txtLRDate.setNextFocusableComponent(txtInvoiceNo);
        txtLRDate.setEnabled(false);
        txtLRDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLRDateFocusGained(evt);
            }
        });
        Tab1.add(txtLRDate);
        txtLRDate.setBounds(291, 168, 100, 19);

        jLabel9.setText("Invoice No.");
        Tab1.add(jLabel9);
        jLabel9.setBounds(32, 195, 74, 15);

        txtInvoiceNo.setName("INVOICE_NO"); // NOI18N
        txtInvoiceNo.setNextFocusableComponent(txtInvoiceDate);
        txtInvoiceNo.setEnabled(false);
        txtInvoiceNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInvoiceNoFocusGained(evt);
            }
        });
        Tab1.add(txtInvoiceNo);
        txtInvoiceNo.setBounds(114, 192, 114, 19);

        jLabel10.setText("Date");
        Tab1.add(jLabel10);
        jLabel10.setBounds(256, 196, 34, 15);

        txtInvoiceDate.setName("INVOICE_DATE"); // NOI18N
        txtInvoiceDate.setNextFocusableComponent(cmbTransporter);
        txtInvoiceDate.setEnabled(false);
        txtInvoiceDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInvoiceDateFocusGained(evt);
            }
        });
        Tab1.add(txtInvoiceDate);
        txtInvoiceDate.setBounds(292, 194, 100, 19);

        jLabel11.setText("Transporter ");
        Tab1.add(jLabel11);
        jLabel11.setBounds(22, 249, 82, 15);

        cmbTransporter.setName("TRANSPORTER"); // NOI18N
        cmbTransporter.setNextFocusableComponent(txtTransName);
        cmbTransporter.setEnabled(false);
        cmbTransporter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbTransporterFocusGained(evt);
            }
        });
        Tab1.add(cmbTransporter);
        cmbTransporter.setBounds(114, 245, 274, 20);

        jLabel12.setText("Inward No.");
        Tab1.add(jLabel12);
        jLabel12.setBounds(33, 310, 75, 15);

        txtGatepassNo.setName("GATEPASS_NO"); // NOI18N
        txtGatepassNo.setNextFocusableComponent(cmbCurrency);
        txtGatepassNo.setEnabled(false);
        txtGatepassNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGatepassNoFocusGained(evt);
            }
        });
        Tab1.add(txtGatepassNo);
        txtGatepassNo.setBounds(114, 307, 114, 19);

        jLabel13.setText("Currency");
        Tab1.add(jLabel13);
        jLabel13.setBounds(433, 118, 56, 15);

        cmbCurrency.setName("CURRENCY_ID"); // NOI18N
        cmbCurrency.setNextFocusableComponent(txtCurrencyRate);
        cmbCurrency.setEnabled(false);
        cmbCurrency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbCurrencyFocusGained(evt);
            }
        });
        Tab1.add(cmbCurrency);
        cmbCurrency.setBounds(494, 114, 134, 24);

        jLabel14.setText("Rate");
        Tab1.add(jLabel14);
        jLabel14.setBounds(457, 151, 34, 15);

        chkImportConcess.setText("Import Concessional");
        chkImportConcess.setEnabled(false);
        chkImportConcess.setName("IMPORT_CONCESS"); // NOI18N
        chkImportConcess.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkImportConcessFocusGained(evt);
            }
        });
        Tab1.add(chkImportConcess);
        chkImportConcess.setBounds(442, 7, 162, 23);

        chkCancelled.setText("Cancelled");
        chkCancelled.setEnabled(false);
        chkCancelled.setNextFocusableComponent(txtRemarks);
        chkCancelled.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkCancelledFocusGained(evt);
            }
        });
        Tab1.add(chkCancelled);
        chkCancelled.setBounds(442, 29, 94, 23);

        jLabel17.setText("Status");
        Tab1.add(jLabel17);
        jLabel17.setBounds(447, 74, 46, 15);

        cmbStatus.setName("OPEN_STATUS"); // NOI18N
        cmbStatus.setEnabled(false);
        cmbStatus.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbStatusFocusGained(evt);
            }
        });
        Tab1.add(cmbStatus);
        cmbStatus.setBounds(494, 70, 94, 24);

        jLabel21.setText("Remarks");
        Tab1.add(jLabel21);
        jLabel21.setBounds(40, 338, 64, 15);

        txtRemarks.setName("REMARKS"); // NOI18N
        txtRemarks.setNextFocusableComponent(cmdNext1);
        txtRemarks.setEnabled(false);
        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRemarksFocusGained(evt);
            }
        });
        Tab1.add(txtRemarks);
        txtRemarks.setBounds(114, 334, 416, 19);

        cmdNext1.setText("Next >>");
        cmdNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNext1);
        cmdNext1.setBounds(630, 282, 102, 25);

        txtCurrencyRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCurrencyRate.setName("CURRENCY_DATE"); // NOI18N
        txtCurrencyRate.setNextFocusableComponent(cmbStatus);
        txtCurrencyRate.setEnabled(false);
        Tab1.add(txtCurrencyRate);
        txtCurrencyRate.setBounds(494, 148, 103, 20);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(216, 16, 34, 15);

        txtSuffix.setName("REFA"); // NOI18N
        txtSuffix.setEnabled(false);
        Tab1.add(txtSuffix);
        txtSuffix.setBounds(114, 74, 62, 19);

        jLabel15.setText("Suffix");
        Tab1.add(jLabel15);
        jLabel15.setBounds(67, 76, 43, 15);

        cmdRemarksBig.setText("...");
        cmdRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemarksBigActionPerformed(evt);
            }
        });
        Tab1.add(cmdRemarksBig);
        cmdRemarksBig.setBounds(534, 333, 32, 21);

        jLabel16.setText("Payment Rate");
        Tab1.add(jLabel16);
        jLabel16.setBounds(401, 182, 110, 15);

        txtPaymentRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPaymentRate.setEnabled(false);
        txtPaymentRate.setName("CURRENCY_RATE"); // NOI18N
        Tab1.add(txtPaymentRate);
        txtPaymentRate.setBounds(520, 180, 96, 21);

        cmdChange.setText("Change");
        cmdChange.setEnabled(false);
        cmdChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdChangeActionPerformed(evt);
            }
        });
        Tab1.add(cmdChange);
        cmdChange.setBounds(129, 38, 88, 20);

        chkKeep.setText("Keep Doc No.");
        Tab1.add(chkKeep);
        chkKeep.setBounds(630, 3, 113, 23);

        lblTransName.setText("Trans. Name");
        Tab1.add(lblTransName);
        lblTransName.setBounds(31, 278, 86, 15);

        txtTransName.setName("REMARKS"); // NOI18N
        txtTransName.setNextFocusableComponent(txtGatepassNo);
        txtTransName.setEnabled(false);
        Tab1.add(txtTransName);
        txtTransName.setBounds(114, 276, 271, 19);

        Tab.addTab("Header ", Tab1);

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab2FocusGained(evt);
            }
        });
        Tab2.setLayout(null);

        TableL.setModel(new javax.swing.table.DefaultTableModel(
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
        TableL.setNextFocusableComponent(TableH);
        TableL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TableLFocusLost(evt);
            }
        });
        TableL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableLKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableLKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(TableL);

        Tab2.add(jScrollPane1);
        jScrollPane1.setBounds(6, 38, 732, 176);

        TableH.setModel(new javax.swing.table.DefaultTableModel(
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
        TableH.setNextFocusableComponent(cmdNext2);
        HeaderPane.setViewportView(TableH);

        Tab2.add(HeaderPane);
        HeaderPane.setBounds(6, 240, 254, 124);

        cmdInsert.setMnemonic('I');
        cmdInsert.setText("Insert from MIR");
        cmdInsert.setEnabled(false);
        cmdInsert.setNextFocusableComponent(TableL);
        cmdInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInsertActionPerformed(evt);
            }
        });
        cmdInsert.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdInsertFocusGained(evt);
            }
        });
        Tab2.add(cmdInsert);
        cmdInsert.setBounds(402, 8, 140, 25);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.setNextFocusableComponent(TableL);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        cmdAdd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdAddFocusGained(evt);
            }
        });
        Tab2.add(cmdAdd);
        cmdAdd.setBounds(554, 8, 88, 25);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setEnabled(false);
        cmdRemove.setNextFocusableComponent(TableL);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        cmdRemove.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdRemoveFocusGained(evt);
            }
        });
        Tab2.add(cmdRemove);
        cmdRemove.setBounds(646, 8, 92, 25);

        jLabel18.setText("Gross Amount");
        Tab2.add(jLabel18);
        jLabel18.setBounds(492, 225, 99, 15);

        jLabel19.setText("Net Amount");
        Tab2.add(jLabel19);
        jLabel19.setBounds(509, 250, 82, 15);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setText("GRN Items");
        Tab2.add(jLabel20);
        jLabel20.setBounds(10, 14, 70, 15);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab2.add(jPanel4);
        jPanel4.setBounds(78, 22, 300, 5);

        cmdNext2.setText("Next >>");
        cmdNext2.setNextFocusableComponent(cmdBack2);
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNext2);
        cmdNext2.setBounds(630, 340, 102, 25);

        cmdBack2.setText("<< Back");
        cmdBack2.setNextFocusableComponent(cmdInsert);
        cmdBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBack2);
        cmdBack2.setBounds(524, 340, 102, 25);

        txtGrossAmount.setEditable(false);
        txtGrossAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtGrossAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Tab2.add(txtGrossAmount);
        txtGrossAmount.setBounds(596, 222, 140, 19);

        txtNetAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtNetAmount.setEditable(false);
        txtNetAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Tab2.add(txtNetAmount);
        txtNetAmount.setBounds(598, 248, 140, 19);

        cmdShowPO.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        cmdShowPO.setText("Show PO");
        cmdShowPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowPOActionPerformed(evt);
            }
        });
        Tab2.add(cmdShowPO);
        cmdShowPO.setBounds(317, 249, 110, 24);

        cmdShowMIR.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        cmdShowMIR.setText("Show MIR");
        cmdShowMIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowMIRActionPerformed(evt);
            }
        });
        Tab2.add(cmdShowMIR);
        cmdShowMIR.setBounds(317, 220, 110, 24);

        jLabel23.setText("Final Amount");
        Tab2.add(jLabel23);
        jLabel23.setBounds(496, 276, 95, 15);

        txtFinalAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtFinalAmount.setEditable(false);
        txtFinalAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Tab2.add(txtFinalAmount);
        txtFinalAmount.setBounds(598, 274, 140, 19);

        lblColumnTotal.setForeground(new java.awt.Color(102, 102, 255));
        lblColumnTotal.setText(".");
        Tab2.add(lblColumnTotal);
        lblColumnTotal.setBounds(7, 220, 250, 15);

        jLabel37.setText("Invoice Amount");
        Tab2.add(jLabel37);
        jLabel37.setBounds(490, 304, 108, 15);

        txtInvoiceAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtInvoiceAmount.setEnabled(false);
        txtInvoiceAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountKeyPressed(evt);
            }
        });
        Tab2.add(txtInvoiceAmount);
        txtInvoiceAmount.setBounds(598, 300, 136, 19);

        Tab.addTab("Item Information", Tab2);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(null);

        jLabel1.setText("PF");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(31, 28, 30, 15);

        cmbPaymentType.setBackground(new java.awt.Color(255, 255, 204));
        cmbPaymentType.setEnabled(false);
        jPanel2.add(cmbPaymentType);
        cmbPaymentType.setBounds(159, 271, 110, 24);

        jLabel22.setText("Freight");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(30, 58, 50, 15);

        cmbPFPost.setEnabled(false);
        jPanel2.add(cmbPFPost);
        cmbPFPost.setBounds(158, 23, 110, 24);

        jLabel24.setText("Octroi");
        jPanel2.add(jLabel24);
        jLabel24.setBounds(29, 93, 50, 15);

        cmbFreightPost.setEnabled(false);
        jPanel2.add(cmbFreightPost);
        cmbFreightPost.setBounds(158, 54, 110, 24);

        jLabel25.setText("Insurance");
        jPanel2.add(jLabel25);
        jLabel25.setBounds(30, 126, 70, 15);

        cmbOctroiPost.setEnabled(false);
        jPanel2.add(cmbOctroiPost);
        cmbOctroiPost.setBounds(158, 89, 110, 24);

        jLabel27.setText("Clearance Charges");
        jPanel2.add(jLabel27);
        jLabel27.setBounds(32, 161, 120, 15);

        cmbInsurancePost.setEnabled(false);
        jPanel2.add(cmbInsurancePost);
        cmbInsurancePost.setBounds(158, 121, 110, 24);

        jLabel28.setText("Air Freight");
        jPanel2.add(jLabel28);
        jLabel28.setBounds(34, 197, 80, 15);

        cmbClearancePost.setEnabled(false);
        jPanel2.add(cmbClearancePost);
        cmbClearancePost.setBounds(158, 156, 110, 24);

        jLabel29.setText("Others");
        jPanel2.add(jLabel29);
        jLabel29.setBounds(36, 232, 50, 15);

        cmbAirFreightPost.setEnabled(false);
        jPanel2.add(cmbAirFreightPost);
        cmbAirFreightPost.setBounds(158, 191, 110, 24);

        jLabel30.setText("Payment Type");
        jPanel2.add(jLabel30);
        jLabel30.setBounds(36, 275, 100, 15);

        cmbOthersPost.setEnabled(false);
        jPanel2.add(cmbOthersPost);
        cmbOthersPost.setBounds(158, 229, 110, 24);

        Tab.addTab("Posting Information", jPanel2);

        jPanel5.setLayout(null);

        TableHSN.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane7.setViewportView(TableHSN);

        jPanel5.add(jScrollPane7);
        jScrollPane7.setBounds(10, 10, 370, 310);

        btnHSNUpdate.setText("UPDATE");
        btnHSNUpdate.setEnabled(false);
        btnHSNUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHSNUpdateActionPerformed(evt);
            }
        });
        jPanel5.add(btnHSNUpdate);
        btnHSNUpdate.setBounds(430, 40, 130, 25);

        Tab.addTab("Enter HSN Manually", jPanel5);

        jPanel7.setLayout(null);

        TableHSNGRNPJV.setModel(new javax.swing.table.DefaultTableModel(
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
        TableHSNGRNPJV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableHSNGRNPJVKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableHSNGRNPJVKeyReleased(evt);
            }
        });
        jScrollPane8.setViewportView(TableHSNGRNPJV);

        jPanel7.add(jScrollPane8);
        jScrollPane8.setBounds(10, 10, 710, 310);

        Tab.addTab("HSN Wise GRN PJV Posting", jPanel7);

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab3FocusGained(evt);
            }
        });
        Tab3.setLayout(null);

        jLabel31.setText("Hierarchy ");
        Tab3.add(jLabel31);
        jLabel31.setBounds(16, 18, 66, 15);

        cmbFinHierarchy.setEditable(true);
        cmbFinHierarchy.setNextFocusableComponent(OpgApprove);
        cmbFinHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFinHierarchyItemStateChanged(evt);
            }
        });
        cmbFinHierarchy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbFinHierarchyFocusGained(evt);
            }
        });
        Tab3.add(cmbFinHierarchy);
        cmbFinHierarchy.setBounds(146, 308, 240, 24);

        jLabel32.setText("From");
        Tab3.add(jLabel32);
        jLabel32.setBounds(20, 52, 56, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        Tab3.add(txtFrom);
        txtFrom.setBounds(86, 50, 182, 19);

        jLabel35.setText("Remarks");
        Tab3.add(jLabel35);
        jLabel35.setBounds(20, 82, 62, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setEnabled(false);
        Tab3.add(txtFromRemarks);
        txtFromRemarks.setBounds(86, 78, 518, 19);

        jLabel36.setText("Your Action  ");
        Tab3.add(jLabel36);
        jLabel36.setBounds(8, 124, 76, 15);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        buttonGroup1.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.setNextFocusableComponent(OpgFinal);
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
        OpgApprove.setBounds(6, 6, 168, 23);

        buttonGroup1.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
        OpgFinal.setNextFocusableComponent(OpgReject);
        OpgFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgFinalFocusGained(evt);
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
        OpgHold.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgHoldMouseClicked(evt);
            }
        });
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        Tab3.add(jPanel6);
        jPanel6.setBounds(88, 120, 182, 100);

        jLabel33.setText("Send To");
        Tab3.add(jLabel33);
        jLabel33.setBounds(18, 228, 60, 15);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab3.add(cmbSendTo);
        cmbSendTo.setBounds(84, 224, 184, 24);

        jLabel34.setText("Remarks");
        Tab3.add(jLabel34);
        jLabel34.setBounds(16, 264, 60, 15);

        txtToRemarks.setNextFocusableComponent(cmdBack3);
        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab3.add(txtToRemarks);
        txtToRemarks.setBounds(84, 260, 516, 19);

        cmdBack3.setText("<< Back");
        cmdBack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack3ActionPerformed(evt);
            }
        });
        Tab3.add(cmdBack3);
        cmdBack3.setBounds(620, 320, 102, 25);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab3.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(608, 77, 32, 21);

        lblFinHierarchy.setText("Finance Hierarchy");
        Tab3.add(lblFinHierarchy);
        lblFinHierarchy.setBounds(18, 312, 120, 15);

        cmbHierarchy.setEditable(true);
        cmbHierarchy.setNextFocusableComponent(OpgApprove);
        cmbHierarchy.setEnabled(false);
        Tab3.add(cmbHierarchy);
        cmbHierarchy.setBounds(86, 14, 184, 24);

        Tab.addTab("Approval", Tab3);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(null);

        jLabel26.setText("Document Approval Status");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(12, 10, 242, 15);

        TableA.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(TableA);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(12, 40, 697, 144);

        lblDocumentHistory.setText("Document Update History");
        jPanel1.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(13, 190, 182, 15);

        TableHS.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(TableHS);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(13, 206, 550, 133);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        jPanel1.add(cmdViewHistory);
        cmdViewHistory.setBounds(575, 237, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        jPanel1.add(cmdNormalView);
        cmdNormalView.setBounds(575, 267, 132, 24);

        cmdPreviewA.setText("Preview Report");
        cmdPreviewA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewAActionPerformed(evt);
            }
        });
        jPanel1.add(cmdPreviewA);
        cmdPreviewA.setBounds(575, 206, 132, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        jPanel1.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(575, 296, 132, 24);

        txtAuditRemarks.setEnabled(false);
        jPanel1.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(576, 332, 129, 19);

        Tab.addTab("Status", jPanel1);

        getContentPane().add(Tab);
        Tab.setBounds(4, 68, 750, 440);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 510, 752, 22);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);
    }// </editor-fold>//GEN-END:initComponents
    
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        PreviewReport_NonGST_Updated();
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    
    private void jMenuItem1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jMenuItem1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ItemStateChanged
    
    private void txtDocDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDocDateFocusLost
        // TODO add your handling code here:
        try {
            java.sql.Date LimitDate = java.sql.Date.valueOf("2005-06-30");
            java.sql.Date DocDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtDocDate.getText()));
            
            if (DocDate.before(LimitDate)) {
                OpgFinal.setEnabled(true);
            } else {
                OpgFinal.setEnabled(false);
            }
        } catch (Exception e) {
            
        }
    }//GEN-LAST:event_txtDocDateFocusLost
    
    private void txtDocNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDocNoFocusLost
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD) {
            if (clsGRNGen.IsGRNExist(EITLERPGLOBAL.gCompanyID, txtDocNo.getText())) {
                JOptionPane.showMessageDialog(null, "GRN No. already exist");
            }
        }
    }//GEN-LAST:event_txtDocNoFocusLost
    
    private void cmdChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdChangeActionPerformed
        // TODO add your handling code here:
        txtDocNo.setEnabled(false);
        txtDocNo.setEditable(false);
    }//GEN-LAST:event_cmdChangeActionPerformed
    
    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if (TableHS.getRowCount() > 0 && TableHS.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableHS.getValueAt(TableHS.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }
        
    }//GEN-LAST:event_cmdShowRemarksActionPerformed
    
    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtFromRemarks;
        bigEdit.ShowEdit();
        
    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed
    
    private void cmdRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemarksBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdRemarksBigActionPerformed
    
    private void cmdPreviewAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewAActionPerformed
        // TODO add your handling code here:
        PreviewAuditReport();
    }//GEN-LAST:event_cmdPreviewAActionPerformed
    
    private void cmdShowPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowPOActionPerformed
        // TODO add your handling code here:
        if (TableL.getRowCount() > 0 && TableL.getSelectedRow() >= 0) {
            String PONo = DataModelL.getValueByVariable("PO_NO", TableL.getSelectedRow());
            
            int POType = clsPOGen.getPOType(EITLERPGLOBAL.gCompanyID, PONo);
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = POType;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, PONo);
        }
        
    }//GEN-LAST:event_cmdShowPOActionPerformed
    
    private void cmdShowMIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowMIRActionPerformed
        // TODO add your handling code here:
        try {
            String DocNo = DataModelL.getValueByVariable("MIR_NO", TableL.getSelectedRow());
            
            if (!DocNo.trim().equals("")) {
                AppletFrame aFrame = new AppletFrame("MIR");
                aFrame.startAppletEx("EITLERP.Stores.frmMIRGen", "MIR");
                frmMIRGen ObjDoc = (frmMIRGen) aFrame.ObjApplet;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            } else {
                JOptionPane.showMessageDialog(null, "MIR no. not specified");
                
            }
        } catch (Exception e) {
            
        }
        
        
    }//GEN-LAST:event_cmdShowMIRActionPerformed
    
    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjGRN.LoadData(EITLERPGLOBAL.gCompanyID);
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed
    
    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo = txtDocNo.getText();
        ObjGRN.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed
    
    private void TableLKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableLKeyReleased
        // TODO add your handling code here:
        if (EditMode != 0) {
            if (evt.getKeyCode() == 67 && evt.getModifiersEx() == 128) //Ctrl+C Key Combonation
            {
                DoNotEvaluate = true;
                
                //Check that any row exist
                if (TableL.getRowCount() > 0) {
                    //First Add new row
                    Object[] rowData = new Object[1];
                    DataModelL.addRow(rowData);
                    int NewRow = TableL.getRowCount() - 1;
                    
                    //Copy New row with Previous one
                    for (int i = 0; i < TableL.getColumnCount(); i++) {
                        TableL.setValueAt(TableL.getValueAt(TableL.getSelectedRow(), i), NewRow, i);
                    }
                    UpdateSrNo();
                }
                DoNotEvaluate = false;
            }
        }
    }//GEN-LAST:event_TableLKeyReleased
    
    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the remarks for next user");
    }//GEN-LAST:event_txtToRemarksFocusGained
    
    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the user to whom document to be forwarded");
    }//GEN-LAST:event_cmbSendToFocusGained
    
    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained
        // TODO add your handling code here:
        ShowMessage("Select approval action");
    }//GEN-LAST:event_OpgHoldFocusGained
    
    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained
        // TODO add your handling code here:
        ShowMessage("Select approval action");
    }//GEN-LAST:event_OpgRejectFocusGained
    
    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained
        // TODO add your handling code here:
        ShowMessage("Select approval action");
    }//GEN-LAST:event_OpgFinalFocusGained
    
    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        // TODO add your handling code here:
        ShowMessage("Select approval action");
    }//GEN-LAST:event_OpgApproveFocusGained
    
    private void cmbFinHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbFinHierarchyFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the hierarchy for the approval");
    }//GEN-LAST:event_cmbFinHierarchyFocusGained
    
    private void cmdRemoveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdRemoveFocusGained
        // TODO add your handling code here:
        ShowMessage("Click this button to remove selected row from the table");
    }//GEN-LAST:event_cmdRemoveFocusGained
    
    private void cmdAddFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdAddFocusGained
        // TODO add your handling code here:
        ShowMessage("Click this button to add a new row to the table");
    }//GEN-LAST:event_cmdAddFocusGained
    
    private void cmdInsertFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdInsertFocusGained
        // TODO add your handling code here:
        ShowMessage("Click this button to bring Insert MIR items dialog box");
    }//GEN-LAST:event_cmdInsertFocusGained
    
    private void txtRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the remarks for this document");
    }//GEN-LAST:event_txtRemarksFocusGained
    
    private void chkCancelledFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkCancelledFocusGained
        // TODO add your handling code here:
        ShowMessage("Shows cancel status of this document");
    }//GEN-LAST:event_chkCancelledFocusGained
    
    private void chkImportConcessFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkImportConcessFocusGained
        // TODO add your handling code here:
        ShowMessage("Specify whether this GRN contains Import concessional items");
    }//GEN-LAST:event_chkImportConcessFocusGained
    
    private void cmbStatusFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbStatusFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Open status for the document");
    }//GEN-LAST:event_cmbStatusFocusGained
    
    private void cmbCurrencyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbCurrencyFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the currency");
    }//GEN-LAST:event_cmbCurrencyFocusGained
    
    private void txtGatepassNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGatepassNoFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the gatepass no.");
    }//GEN-LAST:event_txtGatepassNoFocusGained
    
    private void cmbTransporterFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbTransporterFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the transporter");
    }//GEN-LAST:event_cmbTransporterFocusGained
    
    private void txtInvoiceDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvoiceDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Invoice Date in DD/MM/YYYY");
    }//GEN-LAST:event_txtInvoiceDateFocusGained
    
    private void txtInvoiceNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvoiceNoFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Invoice No.");
    }//GEN-LAST:event_txtInvoiceNoFocusGained
    
    private void txtLRDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLRDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter LR Date in DD/MM/YYYY");
    }//GEN-LAST:event_txtLRDateFocusGained
    
    private void txtLRNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLRNoFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter LR No.");
    }//GEN-LAST:event_txtLRNoFocusGained
    
    private void txtChalanDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChalanDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Chalan date in DD/MM/YYYY");
    }//GEN-LAST:event_txtChalanDateFocusGained
    
    private void txtChalanNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChalanNoFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Chalan no.");
    }//GEN-LAST:event_txtChalanNoFocusGained
    
    private void txtSuppCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter supplier id. Press F1 for the list of suppliers");
    }//GEN-LAST:event_txtSuppCodeFocusGained
    
    private void txtDocDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDocDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter GRN Date in DD/MM/YYYY");
    }//GEN-LAST:event_txtDocDateFocusGained
    
    private void Tab3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab3FocusGained
        // TODO add your handling code here:
        cmbHierarchy.requestFocus();
    }//GEN-LAST:event_Tab3FocusGained
    
    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained
        // TODO add your handling code here:
        cmdInsert.requestFocus();
    }//GEN-LAST:event_Tab2FocusGained
    
    private void Tab1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab1FocusGained
        // TODO add your handling code here:
        txtDocDate.requestFocus();
    }//GEN-LAST:event_Tab1FocusGained
    
    private void cmdBack3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack3ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBack3ActionPerformed
    
    private void cmdBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack2ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(0);
    }//GEN-LAST:event_cmdBack2ActionPerformed
    
    private void cmdNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext2ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext2ActionPerformed
    
    private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext1ActionPerformed
        // TODO add your handling code here:
        
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext1ActionPerformed
    
    private void txtSuppCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSuppCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSuppCodeActionPerformed
    
    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);
        OpgHold.setSelected(true);
    }//GEN-LAST:event_OpgHoldMouseClicked
    
    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(true);
        OpgHold.setSelected(false);
        
        GenerateRejectedUserCombo();
        cmbSendTo.setEnabled(true);
        
    }//GEN-LAST:event_OpgRejectMouseClicked
    
    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(true);
        OpgReject.setSelected(false);
        OpgHold.setSelected(false);
        
        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
        
        if (OpgFinal.isEnabled()) {
            lblFinHierarchy.setVisible(true);
            cmbFinHierarchy.setVisible(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked
    
    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        // TODO add your handling code here:
        SetupApproval();
        
        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID, 7, txtDocNo.getText())) {
                cmbSendTo.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }
        
    }//GEN-LAST:event_OpgApproveMouseClicked
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ObjColumn.Close();
        ObjTax.Close();
        ObjGRN.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        jPopupMenu1.show(cmdPreview, 0, 30);
        
        //PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed
    
    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed
    
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        Edit();
        FormatGrid_HSN();
        //        for (int i = 0; i < TableL.getRowCount(); i++) {
        //            String chkId = DataModelL.getValueByVariable("ITEM_ID", i);
        //            if (chkId.startsWith("99")) {
        //                Object[] rowData2 = new Object[25];
        //                rowData2[0] = chkId;
        //                String HSNCODE = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, chkId);
        //                rowData2[1] = HSNCODE;
        //                DataModelHSN.addRow(rowData2);
        //            }
        //        }
        
        if(EITLERPGLOBAL.gCompanyID == 2) {
            for (int i = 0; i < TableL.getRowCount(); i++) {
                String chkId = DataModelL.getValueByVariable("ITEM_ID", i);
                if (chkId.startsWith("99")) {
                    Object[] rowData2 = new Object[25];
                    rowData2[0] = chkId;
                    String HSNCODE = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, chkId);
                    rowData2[1] = HSNCODE;
                    DataModelHSN.addRow(rowData2);
                }
            }
        }
        
        if(EITLERPGLOBAL.gCompanyID == 3) {
            for (int i = 0; i < TableL.getRowCount(); i++) {
                String chkId = DataModelL.getValueByVariable("ITEM_ID", i);
                if (chkId.startsWith("00004")) {
                    int iChkId = Integer.parseInt(chkId);
                    if (iChkId>=4001 && iChkId<=4999) {
                        Object[] rowData2 = new Object[25];
                        rowData2[0] = chkId;
                        String HSNCODE = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, chkId);
                        rowData2[1] = HSNCODE;
                        DataModelHSN.addRow(rowData2);
                    }
                }
            }
        }
    }//GEN-LAST:event_cmdEditActionPerformed
    
    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed
    
    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed
    
    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed
    
    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed
    
    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed
    
    private void txtSuppCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppCodeKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            
            aList.SQL = "SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND BLOCKED='N' AND APPROVED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            
            if (aList.ShowLOV()) {
                txtSuppCode.setText(aList.ReturnVal);
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
            
            String MSMEUAN=data.getStringValueFromDB("SELECT MSME_UAN FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+aList.ReturnVal+"'");
            if(!MSMEUAN.equals("")){
                if(!MSMEUAN.equals("N/A")){
                   JOptionPane.showMessageDialog(null, "This party "+aList.ReturnVal+" is MSME Party. Please proceed immediately");
             
                }
            }
            String MSMEDIC=data.getStringValueFromDB("SELECT MSME_DIC_NO FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+aList.ReturnVal+"'");
            if(!MSMEDIC.equals("")){
                if(!MSMEDIC.equals("N/A")){
                   JOptionPane.showMessageDialog(null, "This party "+aList.ReturnVal+" is MSME Party. Please proceed immediately");
             
                }
            }
            int MSME=data.getIntValueFromDB("SELECT MSME FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+aList.ReturnVal+"'");
            if(MSME==1){
                   JOptionPane.showMessageDialog(null, "This party "+aList.ReturnVal+" is MSME Party. Please proceed immediately");
             
            }
        }
        //=========================================
        
    }//GEN-LAST:event_txtSuppCodeKeyPressed
    
    private void txtSuppCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusLost
        // TODO add your handling code here:
        if (!txtSuppCode.getText().trim().equals("")) {
            txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
        }
        
        //Special Addition
        if (txtSuppCode.getText().trim().equals("000000")) {
            //Allow to enter party name
            txtSuppName.setEnabled(true);
            txtSuppName.requestFocus();
            txtSuppName.selectAll();
        } else {
            txtSuppName.setEnabled(false);
        }
        
    }//GEN-LAST:event_txtSuppCodeFocusLost
    
    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        if (TableL.getRowCount() > 0) {
            DataModelL.removeRow(TableL.getSelectedRow());
            UpdateSrNo();
        }
        FormatGridHSNGRNPJV();
        GenerateHSNGRNData();
    }//GEN-LAST:event_cmdRemoveActionPerformed
    
    private void TableLFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableLFocusLost
        // TODO add your handling code here:
        
        //Update Header Custom Columns
        //        for(int i=0;i<TableH.getRowCount();i++) {
        //            UpdateResults_H(i);
        //        }
        
    }//GEN-LAST:event_TableLFocusLost
    
    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        DoNotEvaluate = true;
        int ImportCol = DataModelL.getColFromVariable("EXCISE_GATEPASS_GIVEN");
        Object[] rowData = new Object[ImportCol + 1];
        rowData[ImportCol] = Boolean.valueOf(false);
        DataModelL.addRow(rowData);
        DataModelL.SetUserObject(TableL.getRowCount() - 1, new HashMap());
        TableL.changeSelection(TableL.getRowCount() - 1, 1, false, false);
        UpdateSrNo();
        DoNotEvaluate = false;
    }//GEN-LAST:event_cmdAddActionPerformed
    
    private void TableLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableLKeyPressed
        // TODO add your handling code here:
        if (EditMode != 0) {
            
            //======================================================//
            if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("PO_NO")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {
                    
                    SelectPONo aList = new SelectPONo();
                    
                    aList.ItemID = DataModelL.getValueByVariable("ITEM_ID", TableL.getSelectedRow());
                    
                    if (aList.ShowList()) {
                        DataModelL.setValueByVariable("PO_NO", aList.SelPONo, TableL.getSelectedRow());
                        DataModelL.setValueByVariable("PO_SR_NO", Integer.toString(aList.SelPOSrNo), TableL.getSelectedRow());
                        DataModelL.setValueByVariable("PO_TYPE", Integer.toString(aList.SelPOType), TableL.getSelectedRow());
                        
                        int DeptID = aList.SelPODept;
                        DataModelL.setValueByVariable("DEPT_ID", Integer.toString(DeptID), TableL.getSelectedRow());
                        DataModelL.setValueByVariable("DEPT_NAME", clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, DeptID), TableL.getSelectedRow());
                        
                        clsPOGen tmpObj = new clsPOGen();
                        
                        clsPOGen ObjPO = (clsPOGen) tmpObj.getObject(EITLERPGLOBAL.gCompanyID, aList.SelPONo, aList.SelPOType);
                        
                        for (int i = 1; i <= ObjPO.colPOItems.size(); i++) {
                            if (i == aList.SelPOSrNo) {
                                clsPOItem ObjItem = (clsPOItem) ObjPO.colPOItems.get(Integer.toString(i));
                                DataModelL.setValueByVariable("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj(), TableL.getSelectedRow());
                                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), TableL.getSelectedRow());
                                DataModelL.setValueByVariable("PO_QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), TableL.getSelectedRow());
                                DataModelL.setValueByVariable("BALANCE_PO_QTY", Double.toString(ObjItem.getAttribute("QTY").getVal() - ObjItem.getAttribute("RECD_QTY").getVal()), TableL.getSelectedRow());
                                DataModelL.setValueByVariable("RATE", Double.toString(ObjItem.getAttribute("RATE").getVal()), TableL.getSelectedRow());
                                
                                //========= Import Tax Columns - Match both columns with variable name ==========//
                                //                                for(int c=1;c<=10;c++) {
                                for (int c = 1; c <= 20; c++) {
                                    //Get the Column ID
                                    int lnColID = (int) ObjItem.getAttribute("COLUMN_" + c + "_ID").getVal();
                                    String strVariable = "";
                                    double lnPercentValue = 0, lnValue = 0;
                                    
                                    //Record the values
                                    lnPercentValue = ObjItem.getAttribute("COLUMN_" + c + "_PER").getVal();
                                    lnValue = ObjItem.getAttribute("COLUMN_" + c + "_AMT").getVal();
                                    
                                    if (lnColID > 0) //Column ID set .. Continue
                                    {
                                        //Get the Variable Name
                                        strVariable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, lnColID);
                                    }
                                    
                                    //We have variable Name - Get the column no. of this form
                                    int lnDestCol = DataModelL.getColFromVariable(strVariable);
                                    
                                    if (lnDestCol >= 0) //We have found the column
                                    {
                                        //Replace the value of this form
                                        DataModelL.setValueAt(Double.toString(lnValue), TableL.getSelectedRow(), lnDestCol);
                                        
                                        //Now check that percentage is used
                                        int lnTaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, lnColID);
                                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, lnTaxID)) {
                                            DataModelL.setValueAt(Double.toString(lnPercentValue), TableL.getSelectedRow(), lnDestCol - 1);
                                        }
                                    }
                                }
                                //===================== Import Completed =================//
                                
                                UpdateResults(DataModelL.getColFromVariable("QTY"));
                            }
                            
                        }
                    }
                }
            }
            //=====================================================================//
            //====================================================================//
            
            //=========== Department List ===============
            if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("DEPT_ID")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {
                    LOV aList = new LOV();
                    
                    aList.SQL = "SELECT DEPT_ID,DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ORDER BY DEPT_DESC";
                    aList.ReturnCol = 1;
                    aList.ShowReturnCol = true;
                    aList.DefaultSearchOn = 2;
                    
                    if (aList.ShowLOV()) {
                        if (TableL.getCellEditor() != null) {
                            TableL.getCellEditor().stopCellEditing();
                        }
                        TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(), DataModelL.getColFromVariable("DEPT_ID"));
                    }
                }
            }
            //=========================================
            
            //=========== Rejected Reason List ===============
            if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("REJECTED_REASON_ID")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {
                    LOV aList = new LOV();
                    
                    aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARA_ID='REJECTED_REASON' ORDER BY PARA_CODE";
                    aList.ReturnCol = 1;
                    aList.ShowReturnCol = true;
                    aList.DefaultSearchOn = 2;
                    
                    if (aList.ShowLOV()) {
                        if (TableL.getCellEditor() != null) {
                            TableL.getCellEditor().stopCellEditing();
                        }
                        
                        TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(), DataModelL.getColFromVariable("REJECTED_REASON_ID"));
                    }
                }
            }
            //=========================================
            
            if (evt.getKeyCode() == 122) //F11 Key pressed
            {
                String lItemID = (String) TableL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                
                frmItemHistory ObjItem = new frmItemHistory();
                ObjItem.ShowForm(lItemID);
            }
            
            //=========== Item List ===============
            /*if(TableL.getSelectedColumn()==DataModelL.getColFromVariable("ITEM_ID")) {
             if(evt.getKeyCode()==112) //F1 Key pressed
             {
             LOV aList=new LOV();
             
             aList.SQL="SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 ORDER BY ITEM_ID";
             aList.ReturnCol=1;
             aList.ShowReturnCol=true;
             aList.DefaultSearchOn=2;
             
             if(aList.ShowLOV()) {
             if(TableL.getCellEditor()!=null) {
             TableL.getCellEditor().stopCellEditing();
             }
             
             TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(),DataModelL.getColFromVariable("ITEM_ID"));
             }
             }
             }*/
            //=========================================
            //            if(evt.getKeyCode()==155)//Insert Key Pressed
            //            {
            //                Object[] rowData=new Object[1];
            //                DataModelL.addRow(rowData);
            //                DataModelL.SetUserObject(TableL.getRowCount()-1,new HashMap());
            //                TableL.changeSelection(TableL.getRowCount()-1, 1, false,false);
            //                UpdateSrNo();
            //            }
        }
    }//GEN-LAST:event_TableLKeyPressed
    
    private void cmdInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInsertActionPerformed
        // TODO add your handling code here:
        FormatGrid_HSN();
        FormatGridHSNGRNPJV();
        String MIRNo = "";
        SelectMIR ObjMIR = new SelectMIR();
        ObjMIR.MIRType = 1; //General MIRs
        if (ObjMIR.ShowList()) {
            DoNotEvaluate = true;
            
            if (ObjMIR.CopyHeader) {
                txtSuppCode.setText((String) ObjMIR.ObjMIRGen.getAttribute("SUPP_ID").getObj());
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
                
                if (txtSuppCode.getText().trim().equals("000000")) {
                    txtSuppName.setText((String) ObjMIR.ObjMIRGen.getAttribute("PARTY_NAME").getObj());
                }
                txtChalanNo.setText((String) ObjMIR.ObjMIRGen.getAttribute("CHALAN_NO").getObj());
                txtChalanDate.setText(EITLERPGLOBAL.formatDate((String) ObjMIR.ObjMIRGen.getAttribute("CHALAN_DATE").getObj()));
                txtLRNo.setText((String) ObjMIR.ObjMIRGen.getAttribute("LR_NO").getObj());
                txtLRDate.setText(EITLERPGLOBAL.formatDate((String) ObjMIR.ObjMIRGen.getAttribute("LR_DATE").getObj()));
                txtInvoiceNo.setText((String) ObjMIR.ObjMIRGen.getAttribute("INVOICE_NO").getObj());
                txtInvoiceDate.setText(EITLERPGLOBAL.formatDate((String) ObjMIR.ObjMIRGen.getAttribute("INVOICE_DATE").getObj()));
                EITLERPGLOBAL.setComboIndex(cmbTransporter, (int) ObjMIR.ObjMIRGen.getAttribute("TRANSPORTER").getVal());
                EITLERPGLOBAL.setComboIndex(cmbCurrency, (int) ObjMIR.ObjMIRGen.getAttribute("CURRENCY_ID").getVal());
                txtCurrencyRate.setText(Double.toString(ObjMIR.ObjMIRGen.getAttribute("CURRENCY_RATE").getVal()));
                txtGatepassNo.setText((String) ObjMIR.ObjMIRGen.getAttribute("GATEPASS_NO").getObj());
                txtTransName.setText((String) ObjMIR.ObjMIRGen.getAttribute("TRANSPORTER_NAME").getObj());
                chkImportConcess.setSelected(ObjMIR.ObjMIRGen.getAttribute("IMPORT_CONCESS").getBool());
                
            }
            
            //Check the Duplication
            for (int i = 1; i <= ObjMIR.colSelItems.size(); i++) {
                clsMIRRawItem ObjItem = (clsMIRRawItem) ObjMIR.colSelItems.get(Integer.toString(i));
                
                for (int r = 0; r < TableL.getRowCount(); r++) {
                    MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                    int MIRSrNo = (int) ObjItem.getAttribute("SR_NO").getVal();
                    
                    String tMIRNo = (String) DataModelL.getValueByVariable("MIR_NO", r);
                    int tMIRSrNo = Integer.parseInt((String) DataModelL.getValueByVariable("MIR_SR_NO", r));
                    
                    if (MIRNo.equals(tMIRNo) && (MIRSrNo == tMIRSrNo)) {
                        JOptionPane.showMessageDialog(null, "MIR No. " + MIRNo + " Sr. " + MIRSrNo + " already exist ");
                        return;
                    }
                }
            }
            //Duplication check completed
            
            txtDocDate.requestFocus();
            
            //It will contain MIR Item Objects
            for (int i = 1; i <= ObjMIR.colSelItems.size(); i++) {
                clsMIRRawItem ObjItem = (clsMIRRawItem) ObjMIR.colSelItems.get(Integer.toString(i));
                
                String chkItemId = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                //int ichkItemId = Integer.parseInt(chkItemId);
                
                //                if (chkItemId.startsWith("99")) {
                //                    Object[] rowData2 = new Object[2];
                //                    rowData2[0] = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                //                    String HSN_CODE = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                //                    rowData2[1] = HSN_CODE;
                //                    DataModelHSN.addRow(rowData2);
                //                }
                
                if(EITLERPGLOBAL.gCompanyID == 2) {
                    if (chkItemId.startsWith("99")) {
                        Object[] rowData2 = new Object[2];
                        rowData2[0] = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                        String HSN_CODE = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                        rowData2[1] = HSN_CODE;
                        DataModelHSN.addRow(rowData2);
                    }
                }
                
                if(EITLERPGLOBAL.gCompanyID == 3) {
                    if (chkItemId.startsWith("00004")) {
                        int ichkItemId = Integer.parseInt(chkItemId);
                        if (ichkItemId>=4001 && ichkItemId<=4999) {
                            Object[] rowData2 = new Object[2];
                            rowData2[0] = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                            String HSN_CODE = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                            rowData2[1] = HSN_CODE;
                            DataModelHSN.addRow(rowData2);
                        }
                    }
                }
                
                //Add Blank Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);
                
                int NewRow = TableL.getRowCount() - 1;
                
                DataModelL.setValueByVariable("SR_NO", Integer.toString((int) ObjItem.getAttribute("SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj(), NewRow);
                String HSN_SAC_CODE = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("HSN_SAC_CODE", HSN_SAC_CODE, NewRow);
                DataModelL.setValueByVariable("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj(), NewRow);
                String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                DataModelL.setValueByVariable("RATE", Double.toString(ObjItem.getAttribute("RATE").getVal()), NewRow);
                DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("BAL_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("EXCESS_QTY", Double.toString(ObjItem.getAttribute("EXCESS_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("PO_QTY", Double.toString(ObjItem.getAttribute("PO_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("BALANCE_PO_QTY", Double.toString(ObjItem.getAttribute("BALANCE_PO_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("UNIT_ID", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);
                String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                DataModelL.setValueByVariable("DEPT_ID", Integer.toString((int) ObjItem.getAttribute("DEPT_ID").getVal()), NewRow);
                DataModelL.setValueByVariable("DEPT_NAME", clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal()), NewRow);
                DataModelL.setValueByVariable("RECEIVED_QTY", Double.toString(ObjItem.getAttribute("RECEIVED_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("REJECTED_QTY", Double.toString(ObjItem.getAttribute("REJECTED_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("REJECTED_REASON_ID", Integer.toString((int) ObjItem.getAttribute("REJECTED_REASON_ID").getVal()), NewRow);
                DataModelL.setValueByVariable("REJECTED_REASON", clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "REJECTED_REASON", (int) ObjItem.getAttribute("REJECTED_REASON_ID").getVal()), NewRow);
                
                DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);
                DataModelL.setValueByVariable("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("BOE_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("BOE_DATE").getObj()), NewRow);
                DataModelL.setValueByVariable("SHADE", (String) ObjItem.getAttribute("SHADE").getObj(), NewRow);
                DataModelL.setValueByVariable("W_MIE", (String) ObjItem.getAttribute("W_MIE").getObj(), NewRow);
                DataModelL.setValueByVariable("NO_CASE", (String) ObjItem.getAttribute("NO_CASE").getObj(), NewRow);
                DataModelL.setValueByVariable("EXCISE_GATEPASS_GIVEN", Boolean.toString(ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool()), NewRow);
                DataModelL.setValueByVariable("IMPORT_CONCESS", Boolean.toString(ObjItem.getAttribute("IMPORT_CONCESS").getBool()), NewRow);
                DataModelL.setValueByVariable("MATERIAL_CODE", (String) ObjItem.getAttribute("MATERIAL_CODE").getObj(), NewRow);
                DataModelL.setValueByVariable("MATERIAL_DESCRIPTION", (String) ObjItem.getAttribute("MATERIAL_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("QUALITY_NO", (String) ObjItem.getAttribute("QUALITY_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("PAGE_NO", (String) ObjItem.getAttribute("PAGE_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("EXCESS", Double.toString(ObjItem.getAttribute("EXCESS").getVal()), NewRow);
                DataModelL.setValueByVariable("SHORTAGE", Double.toString(ObjItem.getAttribute("SHORTAGE").getVal()), NewRow);
                DataModelL.setValueByVariable("CHALAN_QTY", Double.toString(ObjItem.getAttribute("CHALAN_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("L_F_NO", (String) ObjItem.getAttribute("L_F_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), NewRow);
                DataModelL.setValueByVariable("GROSS_AMOUNT", Double.toString(ObjItem.getAttribute("TOTAL_AMOUNT").getVal()), NewRow);
                DataModelL.setValueByVariable("NET_AMOUNT", Double.toString(ObjItem.getAttribute("NET_AMOUNT").getVal()), NewRow);
                DataModelL.setValueByVariable("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("MIR_SR_NO", Integer.toString((int) ObjItem.getAttribute("SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("PO_NO", (String) ObjItem.getAttribute("PO_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("PO_SR_NO", Integer.toString((int) ObjItem.getAttribute("PO_SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("PO_TYPE", Integer.toString((int) ObjItem.getAttribute("PO_TYPE").getVal()), NewRow);
                DataModelL.setValueByVariable("MIR_QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), NewRow);
                
                //========= Import Tax Columns - Match both columns with variable name ==========//
                //                for(int c=1;c<=30;c++) {
                for (int c = 1; c <= 39; c++) {
                    //Get the Column ID
                    int lnColID = (int) ObjItem.getAttribute("COLUMN_" + c + "_ID").getVal();
                    String strVariable = "";
                    double lnPercentValue = 0, lnValue = 0;
                    //  System.out.println("CAPTION : "+ObjItem.getAttribute("COLUMN_"+c+"_CAPTION").getVal()+" , C = "+c);
                    if (c == 11) {
                        boolean halt = true;
                    }
                    //Record the values
                    lnPercentValue = ObjItem.getAttribute("COLUMN_" + c + "_PER").getVal();
                    lnValue = ObjItem.getAttribute("COLUMN_" + c + "_AMT").getVal();
                    
                    if (lnColID > 0) //Column ID set .. Continue
                    {
                        //Get the Variable Name
                        strVariable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, lnColID);
                    }
                    
                    //We have variable Name - Get the column no. of this form
                    int lnDestCol = DataModelL.getColFromVariable(strVariable);
                    
                    if (lnDestCol >= 0) //We have found the column
                    {
                        //Replace the value of this form
                        DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);
                        
                        //Now check that percentage is used
                        int lnTaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, lnColID);
                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, lnTaxID)) {
                            DataModelL.setValueAt(Double.toString(lnPercentValue), NewRow, lnDestCol - 1);
                            DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);
                        } else {
                            DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);
                        }
                    }
                }
                //===================== Import Completed =================//
                
                //ADDED BY GAURANG ON 12/08/2017
                DataModelL.setValueByVariable("P_850", DataModelL.getValueByVariable("P_844", NewRow), NewRow);//,P_823 //,P_844
                DataModelL.setValueByVariable("CGST_INPUT_CREDIT", DataModelL.getValueByVariable("CGST", NewRow), NewRow);
                
                DataModelL.setValueByVariable("P_851", DataModelL.getValueByVariable("P_845", NewRow), NewRow);//,P_824 //,P_845
                DataModelL.setValueByVariable("SGST_INPUT_CREDIT", DataModelL.getValueByVariable("SGST", NewRow), NewRow); //IGST_PER
                
                DataModelL.setValueByVariable("P_852", DataModelL.getValueByVariable("P_846", NewRow), NewRow); //,P_825 //,P_846
                DataModelL.setValueByVariable("IGST_INPUT_CREDIT", DataModelL.getValueByVariable("IGST", NewRow), NewRow);
                //------------------------------
                
                DoNotEvaluate = false;
                TableL.changeSelection(NewRow, 0, false, false);
                UpdateResults(DataModelL.getColFromVariable("QTY"));
                DoNotEvaluate = true;
                
            }
            
            //========= Header Columns ==========//
            //            for(int c=1;c<=30;c++) {
            for (int c = 1; c <= 30; c++) {
                //Get the Column ID
                
                int lnColID = (int) ObjMIR.ObjMIRGen.getAttribute("COLUMN_" + c + "_ID").getVal();
                String strVariable = "";
                double lnPercentValue = 0, lnValue = 0;
                //System.out.println("EXECUTE SUCCESS : "+c);
                //Record the values
                lnPercentValue = ObjMIR.ObjMIRGen.getAttribute("COLUMN_" + c + "_PER").getVal();
                lnValue = ObjMIR.ObjMIRGen.getAttribute("COLUMN_" + c + "_AMT").getVal();
                
                if (lnColID > 0) //Column ID set .. Continue
                {
                    //Get the Variable Name
                    strVariable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, lnColID);
                }
                
                //We have variable Name - Get the column no. of this form
                int lnDestCol = DataModelH.getColFromVariable(strVariable);
                
                if (lnDestCol >= 0) //We have found the column
                {
                    //Replace the value of this form
                    //Now check that percentage is used
                    int lnTaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, lnColID);
                    if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, lnTaxID)) {
                        DataModelH.setValueAt(Double.toString(lnPercentValue), lnDestCol - 1, 1);
                        DataModelH.setValueAt(Double.toString(lnValue), lnDestCol, 1);
                    } else {
                        DataModelH.setValueAt(Double.toString(lnValue), lnDestCol, 1);
                    }
                }
            }
            //===================== Import Completed =================//
            
            UpdateSrNo();
            DoNotEvaluate = false;
        }
        GenerateHSNGRNData();
    }//GEN-LAST:event_cmdInsertActionPerformed
    
    
    private void cmbFinHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFinHierarchyItemStateChanged
        // TODO add your handling code here:
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if (clsHierarchy.CanSkip((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }
        
        if (clsHierarchy.CanFinalApprove((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        } else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
        
        //Set Default Send to User
    }//GEN-LAST:event_cmbFinHierarchyItemStateChanged
    
    private void GSTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GSTActionPerformed
        // TODO add your handling code here:
        PreviewReport_GST();
    }//GEN-LAST:event_GSTActionPerformed
    
    private void Non_GSTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Non_GSTActionPerformed
        // TODO add your handling code here:
        PreviewReport();
    }//GEN-LAST:event_Non_GSTActionPerformed
    
    private void txtInvoiceAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInvoiceAmountKeyPressed
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        PreviewReport_GST();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        PreviewReport();
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    
    private void btnHSNUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHSNUpdateActionPerformed
        // TODO add your handling code here:
        for (int p = 0; p < TableHSN.getRowCount(); p++) {
            String pItemId = TableHSN.getValueAt(p, 0).toString().trim();
            String pHSNCd = TableHSN.getValueAt(p, 1).toString();
            //System.out.println("p ITEM ID : "+pItemId);
            //System.out.println("p HSN CODE : "+pHSNCd);
            //for (int q = 0; q < TableL.getRowCount(); q++) {
                //String qItemId = TableL.getValueAt(q, DataModelL.getColFromVariable("ITEM_ID")).toString();
                //String qHSNCd = TableL.getValueAt(q, DataModelL.getColFromVariable("HSN_SAC_CODE")).toString();
                String qItemId = TableL.getValueAt(p, DataModelL.getColFromVariable("ITEM_ID")).toString();
                String qHSNCd = TableL.getValueAt(p, DataModelL.getColFromVariable("HSN_SAC_CODE")).toString();
                //System.out.println("qITEM ID : "+qItemId);
                //System.out.println("qHSN CODE : "+qHSNCd);
                //if (qItemId.equals(pItemId)) {
                Boolean result=pItemId.equals(qItemId);
                if (result) {
                    //System.out.println("UPDATED ***");
                    TableL.setValueAt(pHSNCd, p, DataModelL.getColFromVariable("HSN_SAC_CODE"));
                }
                
            //}
        }
    }//GEN-LAST:event_btnHSNUpdateActionPerformed
    
    private void TableHSNGRNPJVKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableHSNGRNPJVKeyPressed
        // TODO add your handling code here:
        DecimalFormat newFormat = new DecimalFormat("#.##");
        for (int i = 0; i < TableHSNGRNPJV.getRowCount(); i++) {
            double rcvd_net_amt = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 2).toString());
            double rcvd_cgst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 5).toString());
            double rcvd_sgst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 8).toString());
            double rcvd_igst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 11).toString());
            double rcvd_rcm = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 14).toString());
            double rcvd_composition = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 17).toString());
            double rcvd_gst_compensdation_cess = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 20).toString());
            
            double inv_net_amt = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 1).toString());
            double inv_cgst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 4).toString());
            double inv_sgst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 7).toString());
            double inv_igst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 10).toString());
            double inv_rcm = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 13).toString());
            double inv_composition = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 16).toString());
            double inv_gst_compensdation_cess = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 19).toString());
            
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_net_amt - rcvd_net_amt), i, 3);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_cgst - rcvd_cgst), i, 6);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_sgst - rcvd_sgst), i, 9);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_igst - rcvd_igst), i, 12);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_rcm - rcvd_rcm), i, 15);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_composition - rcvd_composition), i, 18);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_gst_compensdation_cess - rcvd_gst_compensdation_cess), i, 21);
        }
    }//GEN-LAST:event_TableHSNGRNPJVKeyPressed
    
    private void TableHSNGRNPJVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableHSNGRNPJVKeyReleased
        // TODO add your handling code here:
        DecimalFormat newFormat = new DecimalFormat("#.##");
        for (int i = 0; i < TableHSNGRNPJV.getRowCount(); i++) {
            double rcvd_net_amt = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 2).toString());
            double rcvd_cgst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 5).toString());
            double rcvd_sgst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 8).toString());
            double rcvd_igst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 11).toString());
            double rcvd_rcm = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 14).toString());
            double rcvd_composition = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 17).toString());
            double rcvd_gst_compensdation_cess = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 20).toString());
            
            double inv_net_amt = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 1).toString());
            double inv_cgst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 4).toString());
            double inv_sgst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 7).toString());
            double inv_igst = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 10).toString());
            double inv_rcm = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 13).toString());
            double inv_composition = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 16).toString());
            double inv_gst_compensdation_cess = Double.parseDouble(DataModelHSNGRNPJV.getValueAt(i, 19).toString());
            
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_net_amt - rcvd_net_amt), i, 3);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_cgst - rcvd_cgst), i, 6);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_sgst - rcvd_sgst), i, 9);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_igst - rcvd_igst), i, 12);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_rcm - rcvd_rcm), i, 15);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_composition - rcvd_composition), i, 18);
            TableHSNGRNPJV.setValueAt(newFormat.format(inv_gst_compensdation_cess - rcvd_gst_compensdation_cess), i, 21);
        }
    }//GEN-LAST:event_TableHSNGRNPJVKeyReleased
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane HeaderPane;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab3;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableH;
    private javax.swing.JTable TableHS;
    private javax.swing.JTable TableHSN;
    private javax.swing.JTable TableHSNGRNPJV;
    private javax.swing.JTable TableL;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnHSNUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkCancelled;
    private javax.swing.JCheckBox chkImportConcess;
    private javax.swing.JCheckBox chkKeep;
    private javax.swing.JComboBox cmbAirFreightPost;
    private javax.swing.JComboBox cmbClearancePost;
    private javax.swing.JComboBox cmbCurrency;
    private javax.swing.JComboBox cmbFinHierarchy;
    private javax.swing.JComboBox cmbFreightPost;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbInsurancePost;
    private javax.swing.JComboBox cmbOctroiPost;
    private javax.swing.JComboBox cmbOthersPost;
    private javax.swing.JComboBox cmbPFPost;
    private javax.swing.JComboBox cmbPaymentType;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JComboBox cmbTransporter;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBack2;
    private javax.swing.JButton cmdBack3;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdChange;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFromRemarksBig;
    private javax.swing.JButton cmdInsert;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext1;
    private javax.swing.JButton cmdNext2;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewA;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemarksBig;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowMIR;
    private javax.swing.JButton cmdShowPO;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblColumnTotal;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblFinHierarchy;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTransName;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtChalanDate;
    private javax.swing.JTextField txtChalanNo;
    private javax.swing.JTextField txtCurrencyRate;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtFinalAmount;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGatepassNo;
    private javax.swing.JTextField txtGrossAmount;
    private javax.swing.JTextField txtInvoiceAmount;
    private javax.swing.JTextField txtInvoiceDate;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtLRDate;
    private javax.swing.JTextField txtLRNo;
    private javax.swing.JTextField txtNetAmount;
    private javax.swing.JTextField txtPaymentRate;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtSuffix;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSuppName;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtTransName;
    // End of variables declaration//GEN-END:variables
    
    private void EnableToolbar() {
        //Puts toolbar in enable mode
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
    
    private void DisableToolbar() {
        //Puts toolbar in enable mode
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
    
    //Didplay data on the Screen
    private void DisplayData() {
        
        //=========== Color Indication ===============//
        try {
            if (ObjGRN.getAttribute("APPROVED").getInt() == 1) {
                lblTitle.setBackground(Color.BLUE);
            }
            
            if (ObjGRN.getAttribute("APPROVED").getInt() != 1) {
                lblTitle.setBackground(Color.GRAY);
            }
            
            if (ObjGRN.getAttribute("CANCELLED").getInt() == 1) {
                lblTitle.setBackground(Color.RED);
            }
            
        } catch (Exception c) {
            
        }
        //============================================//
        
        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = 7;
            
            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//
        
        try {
            ClearFields();
            txtDocNo.setText((String) ObjGRN.getAttribute("GRN_NO").getObj());
            lblTitle.setText("GOODS RECEIPT NOTE (General) - " + txtDocNo.getText());
            lblRevNo.setText(Integer.toString((int) ObjGRN.getAttribute("REVISION_NO").getVal()));
            txtDocDate.setText(EITLERPGLOBAL.formatDate((String) ObjGRN.getAttribute("GRN_DATE").getObj()));
            txtSuppCode.setText((String) ObjGRN.getAttribute("SUPP_ID").getObj());
            txtSuffix.setText((String) ObjGRN.getAttribute("REFA").getObj());
            txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
            
            if (txtSuppCode.getText().trim().equals("000000")) {
                txtSuppName.setText((String) ObjGRN.getAttribute("PARTY_NAME").getObj());
            }
            
            txtChalanNo.setText((String) ObjGRN.getAttribute("CHALAN_NO").getObj());
            txtChalanDate.setText(EITLERPGLOBAL.formatDate((String) ObjGRN.getAttribute("CHALAN_DATE").getObj()));
            txtLRNo.setText((String) ObjGRN.getAttribute("LR_NO").getObj());
            txtLRDate.setText(EITLERPGLOBAL.formatDate((String) ObjGRN.getAttribute("LR_DATE").getObj()));
            txtInvoiceNo.setText((String) ObjGRN.getAttribute("INVOICE_NO").getObj());
            txtInvoiceDate.setText(EITLERPGLOBAL.formatDate((String) ObjGRN.getAttribute("INVOICE_DATE").getObj()));
            EITLERPGLOBAL.setComboIndex(cmbTransporter, (int) ObjGRN.getAttribute("TRANSPORTER").getVal());
            txtGatepassNo.setText((String) ObjGRN.getAttribute("GATEPASS_NO").getObj());
            txtTransName.setText((String) ObjGRN.getAttribute("TRANSPORTER_NAME").getObj());
            EITLERPGLOBAL.setComboIndex(cmbCurrency, (int) ObjGRN.getAttribute("CURRENCY_ID").getVal());
            txtCurrencyRate.setText(Double.toString(ObjGRN.getAttribute("CURRENCY_RATE").getVal()));
            txtPaymentRate.setText(Double.toString(ObjGRN.getAttribute("CURRENCY_RATE_PAYMENT").getVal()));
            EITLERPGLOBAL.setComboIndex(cmbStatus, (String) ObjGRN.getAttribute("OPEN_STATUS").getObj());
            
            chkImportConcess.setSelected(ObjGRN.getAttribute("IMPORT_CONCESS").getBool());
            chkCancelled.setSelected(ObjGRN.getAttribute("CANCELLED").getBool());
            txtRemarks.setText((String) ObjGRN.getAttribute("REMARKS").getObj());
            txtInvoiceAmount.setText(new BigDecimal(EITLERPGLOBAL.round(ObjGRN.getAttribute("INVOICE_AMOUNT").getVal(), 3)).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) ObjGRN.getAttribute("HIERARCHY_ID").getVal());
            
            EITLERPGLOBAL.setComboIndex(cmbPFPost, ObjGRN.getAttribute("PF_POST").getInt());
            EITLERPGLOBAL.setComboIndex(cmbFreightPost, ObjGRN.getAttribute("FREIGHT_POST").getInt());
            EITLERPGLOBAL.setComboIndex(cmbOctroiPost, ObjGRN.getAttribute("OCTROI_POST").getInt());
            EITLERPGLOBAL.setComboIndex(cmbInsurancePost, ObjGRN.getAttribute("INSURANCE_POST").getInt());
            EITLERPGLOBAL.setComboIndex(cmbClearancePost, ObjGRN.getAttribute("CLEARANCE_POST").getInt());
            EITLERPGLOBAL.setComboIndex(cmbAirFreightPost, ObjGRN.getAttribute("AIR_FREIGHT_POST").getInt());
            EITLERPGLOBAL.setComboIndex(cmbOthersPost, ObjGRN.getAttribute("OTHERS_POST").getInt());
            EITLERPGLOBAL.setComboIndex(cmbPaymentType, ObjGRN.getAttribute("PAYMENT_TYPE").getInt());
            
            //            ObjGRN.setAttribute("INV_GROSS_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(0, 1)+""))));
            //        ObjGRN.setAttribute("INV_NET_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(1, 1)+""))));
            //        ObjGRN.setAttribute("INV_FINAL_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(2, 1)+""))));
            //        ObjGRN.setAttribute("INV_INVOICE_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(3, 1)+""))));
            //        ObjGRN.setAttribute("INV_CGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(4, 1)+""))));
            //        ObjGRN.setAttribute("INV_SGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(5, 1)+""))));
            //        ObjGRN.setAttribute("INV_IGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(6, 1)+""))));
            //        ObjGRN.setAttribute("INV_RCM", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(7, 1)+""))));
            //        ObjGRN.setAttribute("INV_COMPOSITION", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(8, 1)+""))));
            //        ObjGRN.setAttribute("INV_GST_COMPENSATION_CESS", Double.valueOf(newFormat.format(Double.parseDouble(Table_INVOICED.getValueAt(9, 1)+""))));
            //============= Display Custom Columns ========================
            //            for(int i=1;i<=30;i++) {
            for (int i = 1; i <= 30; i++) {
                int ColID = (int) ObjGRN.getAttribute("COLUMN_" + Integer.toString(i) + "_ID").getVal();
                int Col = DataModelH.getColFromID(ColID);
                int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                String Variable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, ColID);
                
                //   System.out.println("E "+ i);
                if (ColID != 0) {
                    //Set the Formula
                    if (ObjGRN.getAttribute("COLUMN_" + Integer.toString(i) + "_FORMULA").getObj() != null) {
                        DataModelH.SetFormula(Col, (String) ObjGRN.getAttribute("COLUMN_" + Integer.toString(i) + "_FORMULA").getObj());
                    } else {
                        DataModelH.SetFormula(Col, "");
                    }
                    
                    //Set the Percentage. If there
                    if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                        DataModelH.setValueByVariableEx("P_" + ColID, Double.toString(ObjGRN.getAttribute("COLUMN_" + Integer.toString(i) + "_PER").getVal()), 1);
                    }
                    
                    //Set the Value
                    DataModelH.setValueByVariableEx(Variable, Double.toString(ObjGRN.getAttribute("COLUMN_" + Integer.toString(i) + "_AMT").getVal()), 1);
                }
                
            }
            //=================================================================//
            
            //========= Display Line Items =============//
            DataModelL.ClearCollections();
            FormatGrid();
            
            DoNotEvaluate = true;
            
            for (int i = 1; i <= ObjGRN.colGRNItems.size(); i++) {
                //Insert New Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);
                int NewRow = TableL.getRowCount() - 1;
                
                clsGRNGenItem ObjItem = (clsGRNGenItem) ObjGRN.colGRNItems.get(Integer.toString(i));
                String HSN_SAC_CODE = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("SR_NO", Integer.toString(i), NewRow);
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj(), NewRow);
                if (ObjItem.getAttribute("HSN_SAC_CODE").getObj().equals("") || ObjItem.getAttribute("HSN_SAC_CODE").getObj() == null) {
                    DataModelL.setValueByVariable("HSN_SAC_CODE", HSN_SAC_CODE, NewRow);
                } else {
                    DataModelL.setValueByVariable("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj(), NewRow);
                }
                //DataModelL.setValueByVariable("HSN_SAC_CODE", HSN_SAC_CODE, NewRow);
                DataModelL.setValueByVariable("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj(), NewRow);
                String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                DataModelL.setValueByVariable("RATE", Double.toString(ObjItem.getAttribute("RATE").getVal()), NewRow);
                DataModelL.setValueByVariable("LANDED_RATE", Double.toString(ObjItem.getAttribute("LANDED_RATE").getVal()), NewRow);
                DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("EXCESS_QTY", Double.toString(ObjItem.getAttribute("EXCESS_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("PO_QTY", Double.toString(ObjItem.getAttribute("PO_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("RECEIVED_QTY", Double.toString(ObjItem.getAttribute("RECEIVED_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("BALANCE_PO_QTY", Double.toString(ObjItem.getAttribute("BALANCE_PO_QTY").getVal()), NewRow);
                
                DataModelL.setValueByVariable("TOLERANCE_LIMIT", Double.toString(ObjItem.getAttribute("TOLERANCE_LIMIT").getVal()), NewRow);
                DataModelL.setValueByVariable("UNIT_ID", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);
                String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                
                DataModelL.setValueByVariable("DEPT_ID", Integer.toString((int) ObjItem.getAttribute("DEPT_ID").getVal()), NewRow);
                DataModelL.setValueByVariable("DEPT_NAME", clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal()), NewRow);
                
                DataModelL.setValueByVariable("REJECTED_REASON_ID", Integer.toString((int) ObjItem.getAttribute("REJECTED_REASON_ID").getVal()), NewRow);
                DataModelL.setValueByVariable("REJECTED_REASON", clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "REJECTION_REASON", (int) ObjItem.getAttribute("REJECTED_REASON_ID").getVal()), NewRow);
                
                DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);
                DataModelL.setValueByVariable("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("BOE_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("BOE_DATE").getObj()), NewRow);
                DataModelL.setValueByVariable("REJECTED_QTY", Double.toString(ObjItem.getAttribute("REJECTED_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("SHADE", (String) ObjItem.getAttribute("SHADE").getObj(), NewRow);
                DataModelL.setValueByVariable("W_MIE", (String) ObjItem.getAttribute("W_MIE").getObj(), NewRow);
                DataModelL.setValueByVariable("NO_CASE", (String) ObjItem.getAttribute("NO_CASE").getObj(), NewRow);
                DataModelL.setValueByVariable("EXCISE_GATEPASS_GIVEN", Boolean.valueOf(ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool()), NewRow);
                DataModelL.setValueByVariable("IMPORT_CONCESS", Boolean.valueOf(ObjItem.getAttribute("IMPORT_CONCESS").getBool()), NewRow);
                DataModelL.setValueByVariable("MATERIAL_CODE", (String) ObjItem.getAttribute("MATERIAL_CODE").getObj(), NewRow);
                DataModelL.setValueByVariable("MATERIAL_DESCRIPTION", (String) ObjItem.getAttribute("MATERIAL_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("QUALITY_NO", (String) ObjItem.getAttribute("QUALITY_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("PAGE_NO", (String) ObjItem.getAttribute("PAGE_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("EXCESS", Double.toString(ObjItem.getAttribute("EXCESS").getVal()), NewRow);
                DataModelL.setValueByVariable("SHORTAGE", Double.toString(ObjItem.getAttribute("SHORTAGE").getVal()), NewRow);
                DataModelL.setValueByVariable("CHALAN_QTY", Double.toString(ObjItem.getAttribute("CHALAN_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("L_F_NO", (String) ObjItem.getAttribute("L_F_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), NewRow);
                /*DataModelL.setValueByVariable("GROSS_AMOUNT",Double.toString(ObjItem.getAttribute("TOTAL_AMOUNT").getVal()),NewRow);
                 DataModelL.setValueByVariable("NET_AMOUNT",Double.toString(ObjItem.getAttribute("NET_AMOUNT").getVal()),NewRow);*/
                DataModelL.setValueByVariable("GROSS_AMOUNT", new BigDecimal(ObjItem.getAttribute("TOTAL_AMOUNT").getVal()).setScale(3, BigDecimal.ROUND_HALF_UP).toString(), NewRow);
                DataModelL.setValueByVariable("NET_AMOUNT", new BigDecimal(ObjItem.getAttribute("NET_AMOUNT").getVal()).setScale(3, BigDecimal.ROUND_HALF_UP).toString(), NewRow);
                DataModelL.setValueByVariable("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("MIR_SR_NO", Integer.toString((int) ObjItem.getAttribute("MIR_SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("PO_NO", (String) ObjItem.getAttribute("PO_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("PO_SR_NO", Integer.toString((int) ObjItem.getAttribute("PO_SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("PO_TYPE", Integer.toString((int) ObjItem.getAttribute("PO_TYPE").getVal()), NewRow);
                DataModelL.setValueByVariable("MIR_QTY", Double.toString(ObjItem.getAttribute("MIR_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("RND_DEDUCTION_REASON", ObjItem.getAttribute("RND_DEDUCTION_REASON").getString(), NewRow);
                
                //============= Display Custom Columns ========================
                //                for(int c=1;c<=30;c++) {
                for (int c = 1; c <= 39; c++) {
                    int ColID = (int) ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_ID").getVal();
                    int Col = DataModelL.getColFromID(ColID);
                    int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                    String Variable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, ColID);
                    //   System.out.println("39 c = "+c);
                    if (ColID != 0) {
                        //Set the Formula
                        if (ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_FORMULA").getObj() != null) {
                            DataModelL.SetFormula(Col, (String) ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_FORMULA").getObj());
                        } else {
                            DataModelL.SetFormula(Col, "");
                        }
                        
                        //Set the Percentage. If there
                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                            DataModelL.setValueByVariable("P_" + ColID, Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_PER").getVal()), NewRow);
                        }
                        
                        //Set the Value
                        DataModelL.setValueByVariable(Variable, Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_AMT").getVal()), NewRow);
                    }
                }
                //=================================================================//
                
            }
            
            //========= Display HSN GRN PJV Items =============//
            FormatGridHSNGRNPJV();
            
            DoNotEvaluate = true;
            
            System.out.println("HSN SIZE : "+ObjGRN.colHSNGRNPJVItems.size());
            if (ObjGRN.colHSNGRNPJVItems.size()==0) {
                GenerateHSNGRNData();
            } else {
                
                for (int i = 1; i <= ObjGRN.colHSNGRNPJVItems.size(); i++) {
                    //Insert New Row
                    Object[] rowData = new Object[1];
                    DataModelHSNGRNPJV.addRow(rowData);
                    int NewRow = TableHSNGRNPJV.getRowCount() - 1;
                    
                    clsHSNGRNPJVItem ObjHSNItem = (clsHSNGRNPJVItem) ObjGRN.colHSNGRNPJVItems.get(Integer.toString(i));
                    
                    DataModelHSNGRNPJV.setValueByVariable("HSN_CODE", (String) ObjHSNItem.getAttribute("HSN_CODE").getObj(), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("INVOICE_AMOUNT", Double.toString(ObjHSNItem.getAttribute("INVOICE_AMOUNT").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("INVOICE_CGST", Double.toString(ObjHSNItem.getAttribute("INVOICE_CGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("INVOICE_SGST", Double.toString(ObjHSNItem.getAttribute("INVOICE_SGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("INVOICE_IGST", Double.toString(ObjHSNItem.getAttribute("INVOICE_IGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("INVOICE_RCM", Double.toString(ObjHSNItem.getAttribute("INVOICE_RCM").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("INVOICE_COMPOSITION", Double.toString(ObjHSNItem.getAttribute("INVOICE_COMPOSITION").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("INVOICE_GST_COMP_CESS", Double.toString(ObjHSNItem.getAttribute("INVOICE_GST_COMP_CESS").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("RECEIVED_AMOUNT", Double.toString(ObjHSNItem.getAttribute("RECEIVED_AMOUNT").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("RECEIVED_CGST", Double.toString(ObjHSNItem.getAttribute("RECEIVED_CGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("RECEIVED_SGST", Double.toString(ObjHSNItem.getAttribute("RECEIVED_SGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("RECEIVED_IGST", Double.toString(ObjHSNItem.getAttribute("RECEIVED_IGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("RECEIVED_RCM", Double.toString(ObjHSNItem.getAttribute("RECEIVED_RCM").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("RECEIVED_COMPOSITION", Double.toString(ObjHSNItem.getAttribute("RECEIVED_COMPOSITION").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("RECEIVED_GST_COMP_CESS", Double.toString(ObjHSNItem.getAttribute("RECEIVED_GST_COMP_CESS").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("DEBIT_NOTE_AMOUNT", Double.toString(ObjHSNItem.getAttribute("DEBIT_NOTE_AMOUNT").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("DEBIT_NOTE_CGST", Double.toString(ObjHSNItem.getAttribute("DEBIT_NOTE_CGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("DEBIT_NOTE_SGST", Double.toString(ObjHSNItem.getAttribute("DEBIT_NOTE_SGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("DEBIT_NOTE_IGST", Double.toString(ObjHSNItem.getAttribute("DEBIT_NOTE_IGST").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("DEBIT_NOTE_RCM", Double.toString(ObjHSNItem.getAttribute("DEBIT_NOTE_RCM").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("DEBIT_NOTE_COMPOSITION", Double.toString(ObjHSNItem.getAttribute("DEBIT_NOTE_COMPOSITION").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("DEBIT_NOTE_GST_COMP_CESS", Double.toString(ObjHSNItem.getAttribute("DEBIT_NOTE_GST_COMP_CESS").getVal()), NewRow);
                    
                    DataModelHSNGRNPJV.setValueByVariable("CGST_INPUT_CR_PER", Double.toString(ObjHSNItem.getAttribute("CGST_INPUT_CR_PER").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("CGST_INPUT_CR_AMT", Double.toString(ObjHSNItem.getAttribute("CGST_INPUT_CR_AMT").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("SGST_INPUT_CR_PER", Double.toString(ObjHSNItem.getAttribute("SGST_INPUT_CR_PER").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("SGST_INPUT_CR_AMT", Double.toString(ObjHSNItem.getAttribute("SGST_INPUT_CR_AMT").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("IGST_INPUT_CR_PER", Double.toString(ObjHSNItem.getAttribute("IGST_INPUT_CR_PER").getVal()), NewRow);
                    DataModelHSNGRNPJV.setValueByVariable("IGST_INPUT_CR_AMT", Double.toString(ObjHSNItem.getAttribute("IGST_INPUT_CR_AMT").getVal()), NewRow);
                    
                }
                
            }
            //=================================================================//
            
            DoNotEvaluate = false;
            
            //UpdateResults_H(0);
            UpdateAmounts();
            UpdateSrNo();
            
            if (EditMode == 0) {
                DataModelL.TableReadOnly(true);
                DataModelH.TableReadOnly(true);
            }
            //=========================================//
            
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = (String) ObjGRN.getAttribute("GRN_NO").getObj();
            List = ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, 7, DocNo);
            for (int i = 1; i <= List.size(); i++) {
                clsDocFlow ObjFlow = (clsDocFlow) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];
                
                rowData[0] = Integer.toString(i);
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2] = (String) ObjFlow.getAttribute("STATUS").getObj();
                rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[4] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6] = (String) ObjFlow.getAttribute("REMARKS").getObj();
                
                DataModelA.addRow(rowData);
            }
            
            //Showing Audit Trial History
            FormatGridHS();
            HashMap History = clsGRNGen.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsGRNGen ObjHistory = (clsGRNGen) History.get(Integer.toString(i));
                Object[] rowData = new Object[5];
                
                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjHistory.getAttribute("ENTRY_DATE").getObj());
                
                String ApprovalStatus = "";
                
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("A")) {
                    ApprovalStatus = "Approved";
                }
                
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }
                
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }
                
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }
                
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("P")) {
                    ApprovalStatus = "Pending";
                }
                
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }
                
                rowData[3] = ApprovalStatus;
                rowData[4] = (String) ObjHistory.getAttribute("APPROVER_REMARKS").getObj();
                
                DataModelHS.addRow(rowData);
            }
            
            //============================================================//
            ShowMessage("Ready");
        } catch (Exception e) {
        }
    }
    
    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjGRN.UserDocNo = txtDocNo.isEnabled();
            ObjGRN.setAttribute("GRN_NO", txtDocNo.getText());
        } else {
            ObjGRN.UserDocNo = false;
        }
        ObjGRN.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
        ObjGRN.setAttribute("PREFIX", SelPrefix);
        ObjGRN.setAttribute("SUFFIX", SelSuffix);
        ObjGRN.setAttribute("FFNO", FFNo);
        //ObjGRN.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDateDB(txtDocDate.getText()));
        if (EITLERPGLOBAL.formatDateDB(txtDocDate.getText()).equals("")) {
            ObjGRN.setAttribute("GRN_DATE", "0000-00-00");
        } else {
            ObjGRN.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDateDB(txtDocDate.getText()));
        }
        
        ObjGRN.setAttribute("SUPP_ID", txtSuppCode.getText());
        
        ObjGRN.setAttribute("PARTY_NAME", "");
        if (txtSuppCode.getText().trim().equals("000000")) {
            ObjGRN.setAttribute("PARTY_NAME", txtSuppName.getText());
        }
        
        ObjGRN.setAttribute("REFA", txtSuffix.getText());
        ObjGRN.setAttribute("CHALAN_NO", txtChalanNo.getText());
        //ObjGRN.setAttribute("CHALAN_DATE",EITLERPGLOBAL.formatDateDB(txtChalanDate.getText()));
        if (EITLERPGLOBAL.formatDateDB(txtChalanDate.getText()).equals("")) {
            ObjGRN.setAttribute("CHALAN_DATE", "0000-00-00");
        } else {
            ObjGRN.setAttribute("CHALAN_DATE", EITLERPGLOBAL.formatDateDB(txtChalanDate.getText()));
        }
        
        ObjGRN.setAttribute("LR_NO", txtLRNo.getText());
        
        if (EITLERPGLOBAL.formatDateDB(txtLRDate.getText()).equals("")) {
            ObjGRN.setAttribute("LR_DATE", "0000-00-00");
        } else {
            ObjGRN.setAttribute("LR_DATE", EITLERPGLOBAL.formatDateDB(txtLRDate.getText()));
        }
        
        //ObjGRN.setAttribute("LR_DATE",EITLERPGLOBAL.formatDateDB(txtLRDate.getText()));
        ObjGRN.setAttribute("INVOICE_NO", txtInvoiceNo.getText());
        //ObjGRN.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(txtInvoiceDate.getText()));
        if (EITLERPGLOBAL.formatDateDB(txtInvoiceDate.getText()).equals("")) {
            ObjGRN.setAttribute("INVOICE_DATE", "0000-00-00");
        } else {
            ObjGRN.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(txtInvoiceDate.getText()));
        }
        
        ObjGRN.setAttribute("TRANSPORTER", EITLERPGLOBAL.getComboCode(cmbTransporter));
        ObjGRN.setAttribute("TRANSPORTER_NAME", txtTransName.getText());
        ObjGRN.setAttribute("GATEPASS_NO", txtGatepassNo.getText());
        ObjGRN.setAttribute("CURRENCY_ID", EITLERPGLOBAL.getComboCode(cmbCurrency));
        ObjGRN.setAttribute("CURRENCY_RATE", EITLERPGLOBAL.round(Double.parseDouble(txtCurrencyRate.getText()), 5));
        ObjGRN.setAttribute("CURRENCY_RATE_PAYMENT", EITLERPGLOBAL.round(Double.parseDouble(txtPaymentRate.getText()), 5));
        ObjGRN.setAttribute("OPEN_STATUS", EITLERPGLOBAL.getCombostrCode(cmbStatus));
        
        ObjGRN.setAttribute("PF_POST", EITLERPGLOBAL.getComboCode(cmbPFPost));
        ObjGRN.setAttribute("FREIGHT_POST", EITLERPGLOBAL.getComboCode(cmbFreightPost));
        ObjGRN.setAttribute("OCTROI_POST", EITLERPGLOBAL.getComboCode(cmbOctroiPost));
        ObjGRN.setAttribute("INSURANCE_POST", EITLERPGLOBAL.getComboCode(cmbInsurancePost));
        ObjGRN.setAttribute("CLEARANCE_POST", EITLERPGLOBAL.getComboCode(cmbClearancePost));
        ObjGRN.setAttribute("AIR_FREIGHT_POST", EITLERPGLOBAL.getComboCode(cmbAirFreightPost));
        ObjGRN.setAttribute("OTHERS_POST", EITLERPGLOBAL.getComboCode(cmbOthersPost));
        ObjGRN.setAttribute("PAYMENT_TYPE", EITLERPGLOBAL.getComboCode(cmbPaymentType));
        ObjGRN.setAttribute("INVOICE_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(txtInvoiceAmount.getText()), 5));
        
        //DecimalFormat newFormat = new DecimalFormat("#.##");
        /*
         ObjGRN.setAttribute("INV_GROSS_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(0, 1)+""),5));
         ObjGRN.setAttribute("INV_NET_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(1, 1)+""),5));
         ObjGRN.setAttribute("INV_FINAL_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(2, 1)+""),5));
         ObjGRN.setAttribute("INV_INVOICE_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(3, 1)+""),5));
         ObjGRN.setAttribute("INV_CGST", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(4, 1)+""),5));
         ObjGRN.setAttribute("INV_SGST", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(5, 1)+""),5));
         ObjGRN.setAttribute("INV_IGST", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(6, 1)+""),5));
         ObjGRN.setAttribute("INV_RCM", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(7, 1)+""),5));
         ObjGRN.setAttribute("INV_COMPOSITION", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(8, 1)+""),5));
         ObjGRN.setAttribute("INV_GST_COMPENSATION_CESS", EITLERPGLOBAL.round(Double.parseDouble(Table_INVOICED.getValueAt(9, 1)+""),5));
         
         ObjGRN.setAttribute("RCVD_GROSS_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(0, 1)+""),5));
         ObjGRN.setAttribute("RCVD_NET_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(1, 1)+""),5));
         ObjGRN.setAttribute("RCVD_FINAL_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(2, 1)+""),5));
         ObjGRN.setAttribute("RCVD_INVOICE_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(3, 1)+""),5));
         ObjGRN.setAttribute("RCVD_CGST", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(4, 1)+""),5));
         ObjGRN.setAttribute("RCVD_SGST", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(5, 1)+""),5));
         ObjGRN.setAttribute("RCVD_IGST", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(6, 1)+""),5));
         ObjGRN.setAttribute("RCVD_RCM", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(7, 1)+""),5));
         ObjGRN.setAttribute("RCVD_COMPOSITION", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(8, 1)+""),5));
         ObjGRN.setAttribute("RCVD_GST_COMPENSATION_CESS", EITLERPGLOBAL.round(Double.parseDouble(Table_RECEIVED.getValueAt(9, 1)+""),5));
         
         ObjGRN.setAttribute("DB_GROSS_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(0, 1)+""),5));
         ObjGRN.setAttribute("DB_NET_AMT",EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(1, 1)+""),5));
         ObjGRN.setAttribute("DB_FINAL_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(2, 1)+""),5));
         ObjGRN.setAttribute("DB_INVOICE_AMT", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(3, 1)+""),5));
         ObjGRN.setAttribute("DB_CGST", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(4, 1)+""),5));
         ObjGRN.setAttribute("DB_SGST", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(5, 1)+""),5));
         ObjGRN.setAttribute("DB_IGST", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(6, 1)+""),5));
         ObjGRN.setAttribute("DB_RCM", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(7, 1)+""),5));
         ObjGRN.setAttribute("DB_COMPOSITION", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(8, 1)+""),5));
         ObjGRN.setAttribute("DB_GST_COMPENSATION_CESS", EITLERPGLOBAL.round(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(9, 1)+""),5));
         */
        if (chkImportConcess.isSelected()) {
            ObjGRN.setAttribute("IMPORT_CONCESS", true);
        } else {
            ObjGRN.setAttribute("IMPORT_CONCESS", false);
        }
        
        if (chkCancelled.isSelected()) {
            ObjGRN.setAttribute("CANCELLED", true);
        } else {
            ObjGRN.setAttribute("CANCELLED", false);
        }
        
        ObjGRN.setAttribute("GRN_TYPE", 1); //Fixed type 1 - General Material
        ObjGRN.setAttribute("FOR_STORE", clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
        ObjGRN.setAttribute("REMARKS", txtRemarks.getText());
        
        //----- Update Approval Specific Fields -----------//
        ObjGRN.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjGRN.setAttribute("FIN_HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbFinHierarchy));
        ObjGRN.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjGRN.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjGRN.setAttribute("FROM_REMARKS", txtToRemarks.getText());
        
        if (OpgApprove.isSelected()) {
            ObjGRN.setAttribute("APPROVAL_STATUS", "A");
        }
        
        if (OpgFinal.isSelected()) {
            ObjGRN.setAttribute("APPROVAL_STATUS", "F");
        }
        
        if (OpgReject.isSelected()) {
            ObjGRN.setAttribute("APPROVAL_STATUS", "R");
            ObjGRN.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }
        
        if (OpgHold.isSelected()) {
            ObjGRN.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//
        
        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjGRN.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjGRN.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjGRN.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjGRN.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        
        //============= Set Custom Columns ========================
        int ColCounter = 0;
        
        for (int i = 0; i < TableH.getRowCount(); i++) {
            double lnPercentValue = 0;
            int ColID = DataModelH.getColID(i);
            int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
            String Variable = DataModelH.getVariable(i);
            
            if ((ColID != 0) && (ColID != -99) && (!Variable.substring(0, 2).equals("P_"))) {
                ColCounter++;
                ObjGRN.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_ID", ColID);
                ObjGRN.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_FORMULA", DataModelH.getFormula(i));
                
                if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                    lnPercentValue = Double.parseDouble(DataModelH.getValueByVariableEx("P_" + ColID, 1));
                    ObjGRN.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_PER", EITLERPGLOBAL.round(lnPercentValue, 3));
                }
                ObjGRN.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_AMT", EITLERPGLOBAL.round(Double.parseDouble(DataModelH.getValueByVariableEx(Variable, 1)), 5));
                ObjGRN.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_CAPTION", (String) TableH.getValueAt(i, 0));
            }
        }
        //=================================================================
        
        //=================== Setting up Line Items ==================//
        ObjGRN.colGRNItems.clear();
        
        for (int i = 0; i < TableL.getRowCount(); i++) {
            clsGRNGenItem ObjItem = new clsGRNGenItem();
            
            ObjItem.setAttribute("SR_NO", DataModelL.getValueByVariable("SR_NO", i));
            ObjItem.setAttribute("ITEM_ID", DataModelL.getValueByVariable("ITEM_ID", i));
            //ObjItem.setAttribute("HSN_SAC_CODE",clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, DataModelL.getValueByVariable("ITEM_ID",i)));
            
            String hsn = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, DataModelL.getValueByVariable("ITEM_ID", i));
            if (hsn.equals("") || hsn == null) {
                ObjItem.setAttribute("HSN_SAC_CODE", DataModelL.getValueByVariable("HSN_SAC_CODE", i));
            } else {
                ObjItem.setAttribute("HSN_SAC_CODE", clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, DataModelL.getValueByVariable("ITEM_ID", i)));
            }
            
            ObjItem.setAttribute("ITEM_EXTRA_DESC", DataModelL.getValueByVariable("ITEM_EXTRA_DESC", i));
            ObjItem.setAttribute("RATE", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("RATE", i)), 5));
            ObjItem.setAttribute("LANDED_RATE", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("LANDED_RATE", i)), 5));
            ObjItem.setAttribute("QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("QTY", i)), 3));
            ObjItem.setAttribute("EXCESS_QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("EXCESS_QTY", i)), 3));
            ObjItem.setAttribute("PO_QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("PO_QTY", i)), 3));
            ObjItem.setAttribute("RECEIVED_QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("RECEIVED_QTY", i)), 3));
            ObjItem.setAttribute("BALANCE_PO_QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("BALANCE_PO_QTY", i)), 3));
            ObjItem.setAttribute("TOLERANCE_LIMIT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("TOLERANCE_LIMIT", i)), 3));
            ObjItem.setAttribute("UNIT", Integer.parseInt(DataModelL.getValueByVariable("UNIT_ID", i)));
            ObjItem.setAttribute("DEPT_ID", Integer.parseInt(DataModelL.getValueByVariable("DEPT_ID", i)));
            ObjItem.setAttribute("REJECTED_REASON_ID", Integer.parseInt(DataModelL.getValueByVariable("REJECTED_REASON_ID", i)));
            ObjItem.setAttribute("BOE_NO", DataModelL.getValueByVariable("BOE_NO", i));
            ObjItem.setAttribute("BOE_SR_NO", DataModelL.getValueByVariable("BOE_SR_NO", i));
            
            if (EITLERPGLOBAL.formatDate(DataModelL.getValueByVariable("BOE_DATE", i)).equals("")) {
                ObjItem.setAttribute("BOE_DATE", "0000-00-00");
            } else {
                ObjItem.setAttribute("BOE_DATE", EITLERPGLOBAL.formatDate(DataModelL.getValueByVariable("BOE_DATE", i)));
            }
            //ObjItem.setAttribute("BOE_DATE",EITLERPGLOBAL.formatDate(DataModelL.getValueByVariable("BOE_DATE",i)));
            ObjItem.setAttribute("REJECTED_QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("REJECTED_QTY", i)), 3));
            ObjItem.setAttribute("REASON", DataModelL.getValueByVariable("REASON", i));
            ObjItem.setAttribute("SHADE", DataModelL.getValueByVariable("SHADE", i));
            ObjItem.setAttribute("W_MIE", DataModelL.getValueByVariable("W_MIE", i));
            ObjItem.setAttribute("NO_CASE", DataModelL.getValueByVariable("NO_CASE", i));
            ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN", DataModelL.getBoolValueByVariable("EXCISE_GATEPASS_GIVEN", i));
            ObjItem.setAttribute("IMPORT_CONCESS", DataModelL.getBoolValueByVariable("IMPORT_CONCESS", i));
            ObjItem.setAttribute("MATERIAL_CODE", DataModelL.getValueByVariable("MATERIAL_CODE", i));
            ObjItem.setAttribute("MATERIAL_DESC", DataModelL.getValueByVariable("MATERIAL_DESCRIPTION", i));
            ObjItem.setAttribute("QUALITY_NO", DataModelL.getValueByVariable("QUALITY_NO", i));
            ObjItem.setAttribute("PAGE_NO", DataModelL.getValueByVariable("PAGE_NO", i));
            ObjItem.setAttribute("EXCESS", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("EXCESS", i)), 3));
            ObjItem.setAttribute("SHORTAGE", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("SHORTAGE", i)), 3));
            ObjItem.setAttribute("CHALAN_QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("CHALAN_QTY", i)), 3));
            ObjItem.setAttribute("L_F_NO", DataModelL.getValueByVariable("L_F_NO", i));
            ObjItem.setAttribute("REMARKS", DataModelL.getValueByVariable("REMARKS", i));
            ObjItem.setAttribute("TOTAL_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("GROSS_AMOUNT", i)), 5));
            ObjItem.setAttribute("NET_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("NET_AMOUNT", i)), 5));
            ObjItem.setAttribute("WAREHOUSE_ID", DataModelL.getValueByVariable("WAREHOUSE_ID", i));
            ObjItem.setAttribute("LOCATION_ID", DataModelL.getValueByVariable("LOCATION_ID", i));
            ObjItem.setAttribute("MIR_NO", DataModelL.getValueByVariable("MIR_NO", i));
            ObjItem.setAttribute("MIR_SR_NO", Integer.parseInt(DataModelL.getValueByVariable("MIR_SR_NO", i)));
            ObjItem.setAttribute("PO_NO", DataModelL.getValueByVariable("PO_NO", i));
            ObjItem.setAttribute("PO_SR_NO", Integer.parseInt(DataModelL.getValueByVariable("PO_SR_NO", i)));
            ObjItem.setAttribute("PO_TYPE", Integer.parseInt(DataModelL.getValueByVariable("PO_TYPE", i)));
            ObjItem.setAttribute("MIR_QTY", Double.parseDouble(DataModelL.getValueByVariable("MIR_QTY", i)));
            ObjItem.setAttribute("BARCODE_TYPE", DataModelL.getValueByVariable("BARCODE_TYPE", i));
            ObjItem.setAttribute("RND_DEDUCTION_REASON", DataModelL.getValueByVariable("RND_DEDUCTION_REASON", i));
            
            //============= Set Custom Columns ========================//
            ColCounter = 0;
            
            System.out.println("TableL : " + TableL.getColumnCount());
            
            for (int c = 0; c < TableL.getColumnCount() - 1; c++) {
                double lnPercentValue = 0;
                int ColID = DataModelL.getColID(c);
                int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                String Variable = DataModelL.getVariable(c);
                
                if ((ColID != 0) && (ColID != -99) && (!Variable.substring(0, 2).equals("P_"))) {
                    ColCounter++;
                    System.out.println("Columns line : " + Integer.toString(ColCounter));
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_ID", ColID);
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_FORMULA", DataModelL.getFormula(c));
                    
                    if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                        lnPercentValue = Double.parseDouble(DataModelL.getValueByVariable("P_" + ColID, i));
                        ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_PER", EITLERPGLOBAL.round(lnPercentValue, 3));
                    }
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_AMT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable(Variable, i)), 5));
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_CAPTION", TableL.getColumnName(c));
                }
            }
            //===========================================================//
            
            ObjGRN.colGRNItems.put(Integer.toString(ObjGRN.colGRNItems.size() + 1), ObjItem);
        }
        //======================Completed ===========================//
        
        //=================== Setting up HSN GRN Items ==================//
        ObjGRN.colHSNGRNPJVItems.clear();
        
        for (int i = 0; i < TableHSNGRNPJV.getRowCount(); i++) {
            clsHSNGRNPJVItem ObjHSNItem = new clsHSNGRNPJVItem();
            
            ObjHSNItem.setAttribute("HSN_CODE", DataModelHSNGRNPJV.getValueAt(i, 0).toString());
            ObjHSNItem.setAttribute("INVOICE_AMOUNT", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 1).toString(), 0));
            ObjHSNItem.setAttribute("INVOICE_CGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 4).toString(), 0));
            ObjHSNItem.setAttribute("INVOICE_SGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 7).toString(), 0));
            ObjHSNItem.setAttribute("INVOICE_IGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 10).toString(), 0));
            ObjHSNItem.setAttribute("INVOICE_RCM", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 13).toString(), 0));
            ObjHSNItem.setAttribute("INVOICE_COMPOSITION", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 16).toString(), 0));
            ObjHSNItem.setAttribute("INVOICE_GST_COMP_CESS", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 19).toString(), 0));
            ObjHSNItem.setAttribute("RECEIVED_AMOUNT", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 2).toString(), 0));
            ObjHSNItem.setAttribute("RECEIVED_CGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 5).toString(), 0));
            ObjHSNItem.setAttribute("RECEIVED_SGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 8).toString(), 0));
            ObjHSNItem.setAttribute("RECEIVED_IGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 11).toString(), 0));
            ObjHSNItem.setAttribute("RECEIVED_RCM", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 14).toString(), 0));
            ObjHSNItem.setAttribute("RECEIVED_COMPOSITION", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 17).toString(), 0));
            ObjHSNItem.setAttribute("RECEIVED_GST_COMP_CESS", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 20).toString(), 0));
            ObjHSNItem.setAttribute("DEBIT_NOTE_AMOUNT", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 3).toString(), 0));
            ObjHSNItem.setAttribute("DEBIT_NOTE_CGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 6).toString(), 0));
            ObjHSNItem.setAttribute("DEBIT_NOTE_SGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 9).toString(), 0));
            ObjHSNItem.setAttribute("DEBIT_NOTE_IGST", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 12).toString(), 0));
            ObjHSNItem.setAttribute("DEBIT_NOTE_RCM", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 15).toString(), 0));
            ObjHSNItem.setAttribute("DEBIT_NOTE_COMPOSITION", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 18).toString(), 0));
            ObjHSNItem.setAttribute("DEBIT_NOTE_GST_COMP_CESS", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 21).toString(), 0));
            
            ObjHSNItem.setAttribute("CGST_INPUT_CR_PER", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 22).toString(), 0));
            ObjHSNItem.setAttribute("CGST_INPUT_CR_AMT", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 23).toString(), 0));
            ObjHSNItem.setAttribute("SGST_INPUT_CR_PER", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 24).toString(), 0));
            ObjHSNItem.setAttribute("SGST_INPUT_CR_AMT", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 25).toString(), 0));
            ObjHSNItem.setAttribute("IGST_INPUT_CR_PER", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 26).toString(), 0));
            ObjHSNItem.setAttribute("IGST_INPUT_CR_AMT", HSNDouble(DataModelHSNGRNPJV.getValueAt(i, 27).toString(), 0));
            
            ObjGRN.colHSNGRNPJVItems.put(Integer.toString(ObjGRN.colHSNGRNPJVItems.size() + 1), ObjHSNItem);
        }
        //======================Completed ===========================//
        
    }
    
    private void SetupColumns() {
        HashMap List = new HashMap();
        HashMap ColList = new HashMap();
        
        List = clsColumn.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND MODULE_ID=7 AND HEADER_LINE='L' ORDER BY COL_ORDER");
        TableColumnModel ColModel = TableL.getColumnModel();
        
        for (int i = 1; i <= List.size(); i++) {
            clsColumn ObjColumn = (clsColumn) List.get(Integer.toString(i));
            int lTaxID = (int) ObjColumn.getAttribute("TAX_ID").getVal();
            int lColID = (int) ObjColumn.getAttribute("SR_NO").getVal();
            
            clsTaxColumn ObjTax = (clsTaxColumn) clsTaxColumn.getObject((int) EITLERPGLOBAL.gCompanyID, lTaxID);
            if ((boolean) ObjTax.getAttribute("USE_PERCENT").getBool()) {
                //Add Percentage Column
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj() + "%");
                
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
                
                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);
                
                //Set Variable for % Column. It will be P_ID
                DataModelL.SetVariable(TableL.getColumnCount() - 1, "P_" + Integer.toString(lColID));
                
                //Set the Operationg Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
                
                //Set stat - Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, true);
                
                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());
                
                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }
                
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj());
                //System.out.println("Collumn Name : "+(String)ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
                
                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);
                
                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() != null) {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                } else {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, "  ");
                }
                
                //Set the Operation Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());
                
                //Set stat - Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());
                
                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());
                
                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }
            } else {
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj());
                
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);
                
                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() == null) {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, "  ");
                } else {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }
                
                //Set the Operation Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());
                
                //Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());
                
                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());
                
                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }
            }
            
            if(!txtSuppCode.getText().startsWith("7")){
                if(lTaxID==844){
                   DataModelL.SetReadOnly(TableL.getColumnCount() - 1); 
                   if ((boolean) ObjTax.getAttribute("USE_PERCENT").getBool()) {
                       DataModelL.SetReadOnly(TableL.getColumnCount() - 2); 
                   }                   
                }
            }else{
                if(lTaxID==842 || lTaxID==840 || lTaxID==838 || lTaxID==836 || lTaxID==833 || lTaxID==830 || lTaxID==827 || lTaxID==825 || lTaxID==822){
                   DataModelL.SetReadOnly(TableL.getColumnCount() - 1); 
                   if ((boolean) ObjTax.getAttribute("USE_PERCENT").getBool()) {
                       DataModelL.SetReadOnly(TableL.getColumnCount() - 2); 
                   }                   
                }
            }
        }
        
        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableL.setRowSelectionAllowed(true);
        TableL.setColumnSelectionAllowed(true);
        
        ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=7 AND HIDDEN=0 AND SHOW_LAST=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for (int i = 1; i <= ColList.size(); i++) {
            clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));
            
            //Add Column First
            DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj()); //0
            
            if (ObjColumn.getAttribute("NUMERIC").getBool()) {
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
            } else {
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, false);
            }
            
            String Variable = (String) ObjColumn.getAttribute("VARIABLE").getObj();
            
            if (Variable.equals("QTY") || Variable.equals("RATE") || Variable.equals("GROSS_AMOUNT") || Variable.equals("NET_AMOUNT")) {
                DataModelL.SetColID(TableL.getColumnCount() - 1, -99);
            } else {
                DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
            }
            
            DataModelL.SetVariable(TableL.getColumnCount() - 1, Variable.trim());
            DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
            DataModelL.SetInclude(TableL.getColumnCount() - 1, true);
            
            if (ObjColumn.getAttribute("READONLY").getBool()) {
                DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
            }
        }
        
        ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=7 AND HIDDEN=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for (int i = 1; i <= ColList.size(); i++) {
            clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));
            
            //Add Column First
            DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj()); //
            DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
            DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE").getObj());
            DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
            DataModelL.SetInclude(TableL.getColumnCount() - 1, true);
            DataModelL.SetNumeric(TableL.getColumnCount() - 1, ObjColumn.getAttribute("NUMERIC").getBool());
            
            DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
            
            //Hide the Column
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setMaxWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setWidth(0);
        }
    }
    
    private void FormatGrid() {
        HashMap ColList = new HashMap();
        
        try {
            txtGrossAmount.requestFocus();
            DataModelL = new EITLTableModel();
            
            TableL.removeAll();
            TableL.setModel(DataModelL);
            
            //Set the table Readonly
            DataModelL.TableReadOnly(false);
            
            ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=7 AND HIDDEN=0 AND SHOW_LAST=0 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
            for (int i = 1; i <= ColList.size(); i++) {
                clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));
                
                //Add Column First
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj()); //0
                
                if (ObjColumn.getAttribute("NUMERIC").getBool()) {
                    DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
                } else {
                    DataModelL.SetNumeric(TableL.getColumnCount() - 1, false);
                }
                
                String Variable = (String) ObjColumn.getAttribute("VARIABLE").getObj();
                
                if (Variable.equals("QTY") || Variable.equals("RATE") || Variable.equals("GROSS_AMOUNT") || Variable.equals("NET_AMOUNT")) {
                    DataModelL.SetColID(TableL.getColumnCount() - 1, -99);
                } else {
                    DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
                }
                
                DataModelL.SetVariable(TableL.getColumnCount() - 1, Variable.trim());
                DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
                DataModelL.SetInclude(TableL.getColumnCount() - 1, true);
                
                if (ObjColumn.getAttribute("READONLY").getBool()) {
                    DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
                }
            }
            
            SetupColumns();
            
            //Now hide the column 1
            TableColumnModel ColModel = TableL.getColumnModel();
            TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            //------- Install Table List Selection Listener ------//
            TableL.getColumnModel().getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    int last = TableL.getSelectedColumn();
                    String strVar = DataModelL.getVariable(last);
                    
                    if (DoNotEvaluate) {
                        return;
                    }
                    
                    //=============== Cell Editing Routine =======================//
                    try {
                        cellLastValueL = (String) TableL.getValueAt(TableL.getSelectedRow(), TableL.getSelectedColumn());
                        
                        TableL.editCellAt(TableL.getSelectedRow(), TableL.getSelectedColumn());
                        if (TableL.getEditorComponent() instanceof JTextComponent) {
                            ((JTextComponent) TableL.getEditorComponent()).selectAll();
                        }
                        
                        //=========Display Column Total ===============//
                        double ColTotal = 0;
                        for (int i = 0; i < TableL.getRowCount(); i++) {
                            if (EITLERPGLOBAL.IsNumber(TableL.getValueAt(i, last).toString())) {
                                ColTotal = ColTotal + Double.parseDouble(TableL.getValueAt(i, last).toString());
                            }
                        }
                        lblColumnTotal.setText(Double.toString(EITLERPGLOBAL.round(ColTotal, 3)));
                        //============================================//
                        
                    } catch (Exception cell) {
                    }
                    //============= Cell Editing Routine Ended =================//
                    
                    if (strVar == null) {
                        return;
                    }
                    
                    ShowMessage("Ready");
                    
                    try {
                        
                        if (strVar.equals("ITEM_ID")) {
                            ShowMessage("Enter item id. Press F1 to for the list of items");
                        }
                        
                        if (strVar.equals("QTY")) {
                            ShowMessage("Enter Qty");
                        }
                        
                        if (strVar.equals("RATE")) {
                            ShowMessage("Enter Rate");
                        }
                        
                        if (strVar.equals("EXCISE_GATEPASS_GIVEN")) {
                            ShowMessage("Press Spacebar to specify whether excise gatepass is given or not");
                        }
                    } catch (Exception v) {
                        
                    }
                }
            }
            );
            //===================================================//
            
            //----- Install Table Model Event Listener -------//
            TableL.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        if (DoNotEvaluate) {
                            return;
                        }
                        
                        //=========== Cell Update Prevention Check ===========//
                        String curValue = (String) TableL.getValueAt(TableL.getSelectedRow(), e.getColumn());
                        if (curValue.equals(cellLastValueL)) {
                            return;
                        } else {
                        }
                        //====================================================//
                        
                        int col = e.getColumn();
                        
                        if (!Updating) {
                            UpdateResults(col);
                            FormatGridHSNGRNPJV();
                            GenerateHSNGRNData();
                        }
                        
                        if (col == DataModelL.getColFromVariable("DEPT_ID")) {
                            String DeptName = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(DataModelL.getValueByVariable("DEPT_ID", TableL.getSelectedRow())));
                            DataModelL.setValueByVariable("DEPT_NAME", DeptName, TableL.getSelectedRow());
                        }
                        
                        if (col == DataModelL.getColFromVariable("REJECTED_REASON_ID")) {
                            String Reason = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "REJECTED_REASON", Integer.parseInt(DataModelL.getValueByVariable("REJECTED_REASON_ID", TableL.getSelectedRow())));
                            DataModelL.setValueByVariable("REJECTED_REASON", Reason, TableL.getSelectedRow());
                        }
                        
                        //If Item ID has changed
                        if (col == DataModelL.getColFromVariable("ITEM_ID")) {
                            try {
                                DoNotEvaluate = true; //Stops Formula Evaluation
                                String lItemID = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                                String lItemName = clsItem.getItemName((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lHsnSacCode = clsItem.getHsnSacCode((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lWareHouseID = clsItem.getItemWareHouseID((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lLocationID = clsItem.getItemLocationID((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                TableL.setValueAt(lItemName, TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_NAME"));
                                TableL.setValueAt(lHsnSacCode, TableL.getSelectedRow(), DataModelL.getColFromVariable("HSN_SAC_CODE"));
                                TableL.setValueAt(lWareHouseID, TableL.getSelectedRow(), DataModelL.getColFromVariable("WAREHOUSE_ID"));
                                TableL.setValueAt(lLocationID, TableL.getSelectedRow(), DataModelL.getColFromVariable("LOCATION_ID"));
                                
                                double UnitRate = clsItem.getRate(EITLERPGLOBAL.gCompanyID, lItemID);
                                if (Double.parseDouble((String) DataModelL.getValueByVariable("RATE", TableL.getSelectedRow())) <= 0) {
                                    TableL.setValueAt(Double.toString(UnitRate), TableL.getSelectedRow(), DataModelL.getColFromVariable("RATE"));
                                }
                                
                                int lItemUnit = clsItem.getItemUnit(EITLERPGLOBAL.gCompanyID, lItemID);
                                TableL.setValueAt(Integer.toString(lItemUnit), TableL.getSelectedRow(), DataModelL.getColFromVariable("UNIT_ID"));
                                String lUnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", lItemUnit);
                                TableL.setValueAt(lUnitName, TableL.getSelectedRow(), DataModelL.getColFromVariable("UNIT_NAME"));
                                DoNotEvaluate = false;
                                
                            } catch (Exception ex) {
                                DoNotEvaluate = false;
                            }
                        }
                        
                        if (col == DataModelL.getColFromVariable("QTY")) {
                            try {
                                double POQty = Double.parseDouble((String) DataModelL.getValueByVariable("PO_QTY", TableL.getSelectedRow()));
                                //Integer.parseInt((String)DataModelL.getValueAt(TableL.getSelectedRow(),DataModelL.getColFromVariable("PO_QTY")));
                                double Qty = Double.parseDouble((String) DataModelL.getValueByVariable("QTY", TableL.getSelectedRow()));
                                if (POQty > 0) {
                                    if (POQty < Qty) {
                                        JOptionPane.showMessageDialog(null, "Accepted Qty is greater than PO Qty.");
                                        //TableL.setValueAt(Double.toString(POQty), TableL.getSelectedRow(), DataModelL.getColFromVariable("QTY"));
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        
                        if (col == DataModelL.getColFromVariable("RECEIVED_QTY")) {
                            try {
                                double POQty = Double.parseDouble((String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("PO_QTY")));
                                double Qty = Double.parseDouble((String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("RECEIVED_QTY")));
                                if (POQty > 0) {
                                    if (POQty < Qty) {
                                        JOptionPane.showMessageDialog(null, "Received Qty is greater than PO Qty.");
                                        //TableL.setValueAt(Double.toString(POQty), TableL.getSelectedRow(), DataModelL.getColFromVariable("RECEIVED_QTY"));
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
            
            int ImportCol = DataModelL.getColFromVariable("EXCISE_GATEPASS_GIVEN");
            Renderer.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            TableL.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            TableL.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
            
            int ImportCol2 = DataModelL.getColFromVariable("IMPORT_CONCESS");
            Renderer.setCustomComponent(ImportCol2, "CheckBox");
            aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            TableL.getColumnModel().getColumn(ImportCol2).setCellEditor(new DefaultCellEditor(aCheckBox));
            TableL.getColumnModel().getColumn(ImportCol2).setCellRenderer(Renderer);
        } catch (Exception e) {
            
        }
        
        //Add ComboBox
        /*JComboBox cmbBarcodeType=new JComboBox();
         
         EITLComboModel cmbBarcodeTypeModel=new EITLComboModel();
         cmbBarcodeType.removeAllItems();
         cmbBarcodeType.setModel(cmbBarcodeTypeModel);
         
         ComboData aData=new ComboData();
         aData.Code=1;
         aData.Text="Single";
         cmbBarcodeTypeModel.addElement(aData);
         
         aData=new ComboData();
         aData.Code=2;
         aData.Text="Individual";
         cmbBarcodeTypeModel.addElement(aData);
         
         aData=new ComboData();
         aData.Code=3;
         aData.Text="No Barcode";
         cmbBarcodeTypeModel.addElement(aData);
         
         
         Renderer.setCustomComponent(DataModelL.getColFromVariable("BARCODE_TYPE"),"ComboBox");
         Renderer.setCustomComponent(DataModelL.getColFromVariable("BARCODE_TYPE"),cmbBarcodeType);
         
         TableL.getColumnModel().getColumn(DataModelL.getColFromVariable("BARCODE_TYPE")).setCellEditor(new DefaultCellEditor(cmbBarcodeType));*/
    }
    
    private void FormatGrid_HSN() {
        DataModelHSN = new EITLTableModel();
        TableHSN.removeAll();
        TableHSN.setModel(DataModelHSN);
        
        DataModelHSN.addColumn("Item ID");
        DataModelHSN.addColumn("HSN/SAC Code");
        
        DataModelHSN.SetVariable(0, "ITEM_ID");
        DataModelHSN.SetVariable(1, "HSN_SAC_CODE");
        
        DataModelHSN.SetReadOnly(0);
    }
    
    private void FormatGridHSNGRNPJV() {
        
        Updating = true; //Stops recursion
        
        try {
            
            DataModelHSNGRNPJV = new EITLTableModel();
            TableHSNGRNPJV.removeAll();
            TableHSNGRNPJV.setModel(DataModelHSNGRNPJV);
            TableColumnModel ColModel = TableHSNGRNPJV.getColumnModel();
            TableHSNGRNPJV.setAutoResizeMode(TableHSNGRNPJV.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);
            
            //DataModelDesc.addColumn("Sr.");    //0
            DataModelHSNGRNPJV.addColumn("HSN Code");    //1
            DataModelHSNGRNPJV.addColumn("Invoice Amt");    //2
            DataModelHSNGRNPJV.addColumn("Receive Amt");    //3
            DataModelHSNGRNPJV.addColumn("Debit Note Amt");    //4
            DataModelHSNGRNPJV.addColumn("Invoice CGST");    //5
            DataModelHSNGRNPJV.addColumn("Receive CGST");    //6
            DataModelHSNGRNPJV.addColumn("Debit Note CGST");    //7
            DataModelHSNGRNPJV.addColumn("Invoice SGST");    //8
            DataModelHSNGRNPJV.addColumn("Receive SGST");    //9
            DataModelHSNGRNPJV.addColumn("Debit Note SGST");    //10
            DataModelHSNGRNPJV.addColumn("Invoice IGST");    //11
            DataModelHSNGRNPJV.addColumn("Receive IGST");    //12
            DataModelHSNGRNPJV.addColumn("Debit Note IGST");    //13
            DataModelHSNGRNPJV.addColumn("Invoice RCM");    //14
            DataModelHSNGRNPJV.addColumn("Receive RCM");    //15
            DataModelHSNGRNPJV.addColumn("Debit Note RCM");    //16
            DataModelHSNGRNPJV.addColumn("Invoice Composition");    //17
            DataModelHSNGRNPJV.addColumn("Receive Composition");    //18
            DataModelHSNGRNPJV.addColumn("Debit Note Composition");    //19
            DataModelHSNGRNPJV.addColumn("Invoice GST Comp Cess");    //20
            DataModelHSNGRNPJV.addColumn("Receive GST Comp Cess");    //21
            DataModelHSNGRNPJV.addColumn("Debit Note GST Comp Cess");    //22
            
            DataModelHSNGRNPJV.addColumn("CGST Input Cr Per");    //23
            DataModelHSNGRNPJV.addColumn("CGST Input Cr Amt");    //24
            DataModelHSNGRNPJV.addColumn("SGST Input Cr Per");    //25
            DataModelHSNGRNPJV.addColumn("SGST Input Cr Amt");    //26
            DataModelHSNGRNPJV.addColumn("IGST Input Cr Per");    //27
            DataModelHSNGRNPJV.addColumn("IGST Input Cr Amt");    //28
            
            DataModelHSNGRNPJV.SetVariable(0, "HSN_CODE");    //1
            DataModelHSNGRNPJV.SetVariable(1, "INVOICE_AMOUNT");    //2
            DataModelHSNGRNPJV.SetVariable(2, "RECEIVED_AMOUNT");    //3
            DataModelHSNGRNPJV.SetVariable(3, "DEBIT_NOTE_AMOUNT");    //4
            DataModelHSNGRNPJV.SetVariable(4, "INVOICE_CGST");    //5
            DataModelHSNGRNPJV.SetVariable(5, "RECEIVED_CGST");    //6
            DataModelHSNGRNPJV.SetVariable(6, "DEBIT_NOTE_CGST");    //7
            DataModelHSNGRNPJV.SetVariable(7, "INVOICE_SGST");    //8
            DataModelHSNGRNPJV.SetVariable(8, "RECEIVED_SGST");    //9
            DataModelHSNGRNPJV.SetVariable(9, "DEBIT_NOTE_SGST");    //10
            DataModelHSNGRNPJV.SetVariable(10, "INVOICE_IGST");    //11
            DataModelHSNGRNPJV.SetVariable(11, "RECEIVED_IGST");    //12
            DataModelHSNGRNPJV.SetVariable(12, "DEBIT_NOTE_IGST");    //13
            DataModelHSNGRNPJV.SetVariable(13, "INVOICE_RCM");    //14
            DataModelHSNGRNPJV.SetVariable(14, "RECEIVED_RCM");    //15
            DataModelHSNGRNPJV.SetVariable(15, "DEBIT_NOTE_RCM");    //16
            DataModelHSNGRNPJV.SetVariable(16, "INVOICE_COMPOSITION");    //17
            DataModelHSNGRNPJV.SetVariable(17, "RECEIVED_COMPOSITION");    //18
            DataModelHSNGRNPJV.SetVariable(18, "DEBIT_NOTE_COMPOSITION");    //19
            DataModelHSNGRNPJV.SetVariable(19, "INVOICE_GST_COMP_CESS");    //20
            DataModelHSNGRNPJV.SetVariable(20, "RECEIVED_GST_COMP_CESS");    //21
            DataModelHSNGRNPJV.SetVariable(21, "DEBIT_NOTE_GST_COMP_CESS");    //22
            
            DataModelHSNGRNPJV.SetVariable(22, "CGST_INPUT_CR_PER");    //23
            DataModelHSNGRNPJV.SetVariable(23, "CGST_INPUT_CR_AMT");    //24
            DataModelHSNGRNPJV.SetVariable(24, "SGST_INPUT_CR_PER");    //25
            DataModelHSNGRNPJV.SetVariable(25, "SGST_INPUT_CR_AMT");    //26
            DataModelHSNGRNPJV.SetVariable(26, "IGST_INPUT_CR_PER");    //27
            DataModelHSNGRNPJV.SetVariable(27, "IGST_INPUT_CR_AMT");    //28
            
            DataModelHSNGRNPJV.SetReadOnly(0);
            DataModelHSNGRNPJV.SetReadOnly(2);
            DataModelHSNGRNPJV.SetReadOnly(3);
            DataModelHSNGRNPJV.SetReadOnly(5);
            DataModelHSNGRNPJV.SetReadOnly(6);
            DataModelHSNGRNPJV.SetReadOnly(8);
            DataModelHSNGRNPJV.SetReadOnly(9);
            DataModelHSNGRNPJV.SetReadOnly(11);
            DataModelHSNGRNPJV.SetReadOnly(12);
            DataModelHSNGRNPJV.SetReadOnly(14);
            DataModelHSNGRNPJV.SetReadOnly(15);
            DataModelHSNGRNPJV.SetReadOnly(17);
            DataModelHSNGRNPJV.SetReadOnly(18);
            DataModelHSNGRNPJV.SetReadOnly(20);
            DataModelHSNGRNPJV.SetReadOnly(21);
            
            DataModelHSNGRNPJV.SetReadOnly(22);
            DataModelHSNGRNPJV.SetReadOnly(23);
            DataModelHSNGRNPJV.SetReadOnly(24);
            DataModelHSNGRNPJV.SetReadOnly(25);
            DataModelHSNGRNPJV.SetReadOnly(26);
            DataModelHSNGRNPJV.SetReadOnly(27);
            
            //            //------- Install Table List Selection Listener ------//
            //            TableHSNGRNPJV.getColumnModel().getSelectionModel().addListSelectionListener(
            //                    new ListSelectionListener() {
            //                        public void valueChanged(ListSelectionEvent e) {
            //                            int last = TableHSNGRNPJV.getSelectedColumn();
            //                            String strVar = DataModelHSNGRNPJV.getVariable(last);
            //
            //                            //=============== Cell Editing Routine =======================//
            //                            try {
            //                                String cellLastValue = (String) TableHSNGRNPJV.getValueAt(TableHSNGRNPJV.getSelectedRow(), TableHSNGRNPJV.getSelectedColumn());
            //
            //                                TableHSNGRNPJV.editCellAt(TableHSNGRNPJV.getSelectedRow(), TableHSNGRNPJV.getSelectedColumn());
            //                                if (TableHSNGRNPJV.getEditorComponent() instanceof JTextComponent) {
            //                                    ((JTextComponent) TableHSNGRNPJV.getEditorComponent()).selectAll();
            //                                }
            //                            } catch (Exception cell) {
            //                            }
            //                            //============= Cell Editing Routine Ended =================//
            //
            //                            ShowMessage("Ready...");
            //
            //                            //       if(last==7){
            //                            //         TableDesc.editCellAt(TableDesc.getSelectedRow(),TableDesc.getSelectedColumn()+1);
            //                            //    }
            //                        }
            //                    }
            //            );
            for (int q = 0; q < 22; q++) {
                TableHSNGRNPJV.getColumnModel().getColumn(q).setMinWidth(100);
                //TableHSNGRNPJV.getColumnModel().getColumn(q).setMaxWidth(150);
            }
            
        } catch (Exception e) {
            
        }
        Updating = false;
        //Table formatting completed
        
    }
    
    private void GatherVariableValues_H() {
        String strVariable = "";
        int varCol = 0;
        double lnValue = 0, lnSum = 0;
        
        //Scan the table and gather values for variables
        colVariables_H.clear();
        
        myParser.initSymTab(); // clear the contents of the symbol table
        myParser.addStandardConstants();
        myParser.addComplex(); // among other things adds i to the symbol table
        
        for (int i = 0; i < TableH.getRowCount(); i++) {
            double lValue = 0;
            if (DataModelH.getVariable(i) != null) {
                if (!DataModelH.getVariable(i).equals("")) //If Variable not blank
                {
                    colVariables_H.put(DataModelH.getVariable(i), (String) TableH.getValueAt(i, 1));
                    
                    //Add variable Value to Parser Table
                    if ((TableH.getValueAt(i, 1) != null) && (!TableH.getValueAt(i, 1).toString().equals(""))) {
                        lValue = Double.parseDouble((String) TableH.getValueAt(i, 1));
                    } else {
                        lValue = 0;
                    }
                    myParser.addVariable(DataModelH.getVariable(i), lValue);
                }
            }
        }
        
        //Gather Variables - sum of line columns
        for (int c = 0; c < TableL.getColumnCount(); c++) {
            strVariable = DataModelL.getVariable(c);
            strVariable = strVariable.trim();
            
            if ((strVariable != null) && (!strVariable.equals(""))) {
                varCol = DataModelL.getColFromVariable(strVariable);
                
                //Do the sum
                lnSum = 0;
                
                try {
                    for (int r = 0; r < TableL.getRowCount(); r++) {
                        String theVal = (String) DataModelL.getValueAt(r, varCol);
                        
                        if (theVal == null) {
                        } else {
                            lnValue = Double.parseDouble(TableL.getValueAt(r, varCol).toString());
                            lnSum = lnSum + lnValue;
                        }
                    }
                } catch (Exception e) {
                }
                //Sum Complete. Add to Parser Table
                myParser.addVariable("SUM_" + strVariable, lnSum);
            }
        }
        
        CurrencyRate = 1;
        
        if (EITLERPGLOBAL.IsNumber(txtCurrencyRate.getText())) {
            CurrencyRate = Double.parseDouble(txtCurrencyRate.getText());
            if (CurrencyRate == 0) {
                CurrencyRate = 1;
            }
        }
        
        myParser.addVariable("CURRENCY_RATE", CurrencyRate);
        
    }
    
    private void SetupColumns_H() {
        HashMap List = new HashMap();
        Object[] rowData;
        
        List = clsColumn.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND MODULE_ID=7 AND HEADER_LINE='H' ORDER BY COL_ORDER");
        TableColumnModel ColModel = TableH.getColumnModel();
        
        TableH.removeAll();
        
        if (List.size() <= 0) {
            HeaderPane.setVisible(false);
        }
        
        for (int i = 1; i <= List.size(); i++) {
            clsColumn ObjColumn = (clsColumn) List.get(Integer.toString(i));
            int lTaxID = (int) ObjColumn.getAttribute("TAX_ID").getVal();
            int lColID = (int) ObjColumn.getAttribute("SR_NO").getVal();
            
            clsTaxColumn ObjTax = (clsTaxColumn) clsTaxColumn.getObject((int) EITLERPGLOBAL.gCompanyID, lTaxID);
            if ((boolean) ObjTax.getAttribute("USE_PERCENT").getBool()) {
                //Add Percentage Column
                //DataModelL.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj()+"%");
                
                rowData = new Object[2];
                rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj() + "%";
                rowData[1] = "0.00";
                DataModelH.addRow(rowData);
                
                //Set Column ID
                DataModelH.SetColID(TableH.getRowCount() - 1, lColID);
                
                //Set Variable for % Column. It will be P_ID
                DataModelH.SetVariable(TableH.getRowCount() - 1, "P_" + Integer.toString(lColID));
                
                //Set the Operationg Add/Substract
                DataModelH.SetOperation(TableH.getRowCount() - 1, "-");
                
                //Set stat - Include it in calculation or not
                DataModelH.SetInclude(TableH.getRowCount() - 1, true);
                
                //Set Formula
                DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());
                
                rowData = new Object[2];
                rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj();
                rowData[1] = "0.00";
                DataModelH.addRow(rowData);
                
                //Set Column ID
                DataModelH.SetColID(TableH.getRowCount() - 1, lColID);
                
                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() != null) {
                    DataModelH.SetVariable(TableH.getRowCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                } else {
                    DataModelH.SetVariable(TableH.getRowCount() - 1, "  ");
                }
                
                //Set the Operationg Add/Substract
                DataModelH.SetOperation(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());
                
                //Set stat - Include it in calculation or not
                DataModelH.SetInclude(TableH.getRowCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());
                
                //Set Formula
                DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());
                
            } else {
                //DataModelH.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj());
                //Set Column ID
                
                rowData = new Object[2];
                rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj();
                rowData[1] = "0.00";
                DataModelH.addRow(rowData);
                
                DataModelH.SetColID(TableH.getRowCount() - 1, lColID);
                
                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() == null) {
                    DataModelH.SetVariable(TableH.getRowCount() - 1, "  ");
                } else {
                    DataModelH.SetVariable(TableH.getRowCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }
                
                //Set the Operationg Add/Substract
                DataModelH.SetOperation(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());
                
                //Set stat - Include it in calculation or not
                DataModelH.SetInclude(TableH.getRowCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());
                
                //Set Formula
                DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());
            }
        }
    }
    
    private void FormatGrid_H() {
        DataModelH = new EITLTableModel();
        
        EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
        
        TableH.removeAll();
        TableH.setModel(DataModelH);
        
        Renderer.setColor(0, 0, Color.LIGHT_GRAY);
        
        //Set the table Readonly
        DataModelH.TableReadOnly(false);
        DataModelH.SetReadOnly(0);
        
        //Add Default Columns
        DataModelH.addColumn("Column");
        DataModelH.addColumn("Value");
        DataModelH.SetNumeric(1, true);
        
        TableH.getColumnModel().getColumn(0).setCellRenderer(Renderer);
        SetupColumns_H();
        
        TableColumnModel ColModel = TableH.getColumnModel();
        TableH.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //------- Install Table List Selection Listener ------//
        TableH.getColumnModel().getSelectionModel().addListSelectionListener(
        new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int last = TableH.getSelectedColumn();
                String strVar = DataModelH.getVariable(last);
                
                //=============== Cell Editing Routine =======================//
                try {
                    cellLastValueH = (String) TableH.getValueAt(TableH.getSelectedRow(), TableH.getSelectedColumn());
                    
                    TableH.editCellAt(TableH.getSelectedRow(), TableH.getSelectedColumn());
                    if (TableH.getEditorComponent() instanceof JTextComponent) {
                        ((JTextComponent) TableH.getEditorComponent()).selectAll();
                    }
                } catch (Exception cell) {
                }
                //============= Cell Editing Routine Ended =================//
                
            }
        }
        );
        //===================================================//
        
        //----- Install Table Model Event Listener -------//
        TableH.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    
                    //=========== Cell Update Prevention Check ===========//
                    String curValue = (String) TableH.getValueAt(TableH.getSelectedRow(), e.getColumn());
                    if (curValue.equals(cellLastValueH)) {
                        return;
                    }
                    //====================================================//
                    
                    int col = e.getColumn();
                    int row = e.getLastRow();
                    if (!Updating_H) {
                        UpdateResults_H(row);
                    }
                }
            }
        });
    }
    
    private void UpdateResults_H(int pCol) {
        try {
            int ColID = 0, TaxID = 0, UpdateCol = 0;
            String strFormula = "", strItemID = "", strVariable = "", srcVariable = "", srcVar2 = "";
            double lnPercentValue = 0, lnFinalResult = 0, lnNetAmount = 0;
            Object result;
            boolean updateIt = true;
            int QtyCol = 0, RateCol = 0, GAmountCol = 0;
            
            Updating_H = true; //Stops Recursion
            
            srcVariable = DataModelH.getVariable(pCol); //Variable name of currently updated Column
            
            //If this column is percentage column. Variable name would be P_XXX
            //We shoule use actual variable name, it will be found on it's associated next column
            if (srcVariable.substring(0, 2).equals("P_")) {
                srcVariable = DataModelH.getVariable(pCol + 1);
            }
            
            GatherVariableValues_H();
            
            for (int i = 0; i < TableH.getRowCount(); i++) {
                strVariable = DataModelH.getVariable(i);
                
                ColID = DataModelH.getColID(i);
                
                TaxID = ObjColumn.getTaxID((int) EITLERPGLOBAL.gCompanyID, ColID);
                
                //Exclude Percentage Columns and System Columns
                if ((!strVariable.substring(0, 2).equals("P_")) && (ColID != 0)) {
                    //If percentage is used
                    if (ObjTax.getUsePercentage((int) EITLERPGLOBAL.gCompanyID, TaxID)) {
                        
                        //Load the Formula for calculation
                        if ((EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                            strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID);
                        } else {
                            strFormula = DataModelH.getFormula(i);
                        }
                        
                        //Now Read Associated Percentage Column
                        lnPercentValue = Double.parseDouble(DataModelH.getValueByVariableEx("P_" + Integer.toString(ColID), 1));
                        
                        //Now Parse Main expression
                        myParser.parseExpression(strFormula);
                        result = myParser.getValueAsObject();
                        if (result != null) {
                            //Now get the percentage of the main result
                            lnFinalResult = (Double.parseDouble(result.toString()) * lnPercentValue) / 100;
                            //Update the Column
                            srcVar2 = DataModelH.getVariable(pCol + 1);
                            
                            UpdateCol = DataModelH.getColFromVariable(strVariable);
                            
                            updateIt = false;
                            
                            if (UpdateCol != pCol) {
                                if (UpdateCol == pCol + 1) {
                                    updateIt = true;
                                } else {
                                    if ((strFormula.indexOf(srcVariable) != -1)) { //If this column is dependent on updated column
                                        updateIt = true; //Then update it
                                    } else {
                                        if ((strFormula.indexOf("QTY") != -1) || (strFormula.indexOf("RATE") != -1) || (strFormula.indexOf("GROSS_AMOUNT") != -1)) {
                                            if (pCol == QtyCol || pCol == RateCol || pCol == GAmountCol) {
                                                updateIt = true;
                                            }
                                        }
                                    }
                                }
                                
                                //============ New Change In Parser =============//
                                //Now Condition. First check whether percentage has been entered
                                if (lnPercentValue > 0) {
                                    //Yes Percentage Entered. Then we must update the associated column
                                    updateIt = true;
                                } else {
                                    //If not Percentage entered than check whether any value is there
                                    //Otherwise go with the Dependent decision
                                    updateIt = false;
                                }
                                //=================================================//
                                
                            }
                            if (updateIt) {
                                DataModelH.setValueByVariableEx(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 3)), 1);
                            }
                            //Re Gather Fresh Variable Values
                            GatherVariableValues_H();
                        }
                    } else //Percentage Not Used
                    {
                        
                        //Load the Formula for calculation
                        if ((EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                            strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID);
                        } else {
                            strFormula = DataModelH.getFormula(i);
                        }
                        
                        //Now Parse Main expression
                        myParser.parseExpression(strFormula);
                        result = myParser.getValueAsObject();
                        if (result != null) {
                            //Now get the percentage of the main result
                            lnFinalResult = Double.parseDouble(result.toString());
                            //Update the Column
                            UpdateCol = DataModelH.getColFromVariable(strVariable);
                            
                            updateIt = false;
                            
                            if (UpdateCol != pCol) {
                                if (strFormula.indexOf(srcVariable) != -1) {
                                    updateIt = true;
                                } else {
                                    updateIt = true;
                                }
                                
                                //============ New Change In Parser =============//
                                //Now Condition. First check whether percentage has been entered
                                if (lnPercentValue > 0) {
                                    //Yes Percentage Entered. Then we must update the associated column
                                    updateIt = true;
                                } else {
                                    //If not Percentage entered than check whether any value is there
                                    //Otherwise go with the Dependent decision
                                }
                                //=================================================//
                                
                            }
                            if (updateIt) {
                                DataModelH.setValueByVariableEx(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 3)), 1);
                            }
                            //Re Gather Fresh Variable Values
                            GatherVariableValues_H();
                        }
                    }
                }
            }
            Updating_H = false;
            UpdateAmounts();
        } catch (Exception e) {
            Updating_H = false;
        }
    }
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";
        
        //----- Generate cmbType ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=7");
        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=7");
        }
        
        for (int i = 1; i <= List.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //
        
        //----- Generate cmbType ------- //
        cmbFinHierarchyModel = new EITLComboModel();
        cmbFinHierarchy.removeAllItems();
        cmbFinHierarchy.setModel(cmbFinHierarchyModel);
        
        List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsVoucher.ModuleID);
        
        for (int i = 1; i <= List.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbFinHierarchyModel.addElement(aData);
        }
        //------------------------------ //
        
        //----- Generate Department Combo ------- //
        cmbCurrencyModel = new EITLComboModel();
        cmbCurrency.removeAllItems();
        cmbCurrency.setModel(cmbCurrencyModel);
        
        List = clsCurrency.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);
        for (int i = 1; i <= List.size(); i++) {
            clsCurrency ObjCurrency = (clsCurrency) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjCurrency.getAttribute("CURRENCY_ID").getVal();
            aData.Text = (String) ObjCurrency.getAttribute("CURRENCY_DESC").getObj();
            cmbCurrencyModel.addElement(aData);
        }
        //------------------------------ //
        
        //----- Generate cmbType ------- //
        cmbTransportModel = new EITLComboModel();
        cmbTransporter.removeAllItems();
        cmbTransporter.setModel(cmbTransportModel);
        
        strCondition = " WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND PARA_ID='TRANSPORT'";
        
        List = clsParameter.getList(strCondition);
        for (int i = 1; i <= List.size(); i++) {
            clsParameter ObjPara = (clsParameter) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjPara.getAttribute("PARA_CODE").getVal();
            aData.Text = (String) ObjPara.getAttribute("DESC").getObj();
            aData.strCode = "";
            cmbTransportModel.addElement(aData);
        }
        //------------------------------ //
        
        //--- Generate Type Combo ------//
        cmbStatusModel = new EITLComboModel();
        cmbStatus.removeAllItems();
        cmbStatus.setModel(cmbStatusModel);
        
        ComboData aData = new ComboData();
        aData.strCode = "O";
        aData.Text = "Open";
        cmbStatusModel.addElement(aData);
        
        aData = new ComboData();
        aData.strCode = "C";
        aData.Text = "Close";
        cmbStatusModel.addElement(aData);
        //===============================//
        
        //=====Voucher Posting Modules========//
        cmbPFPostModel = new EITLComboModel();
        cmbPFPost.removeAllItems();
        cmbPFPost.setModel(cmbPFPostModel);
        
        aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Paid by Party";
        cmbPFPostModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Paid by Us";
        cmbPFPostModel.addElement(aData);
        
        cmbFreightPostModel = new EITLComboModel();
        cmbFreightPost.removeAllItems();
        cmbFreightPost.setModel(cmbFreightPostModel);
        
        aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Paid by Party";
        cmbFreightPostModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Paid by Us";
        cmbFreightPostModel.addElement(aData);
        
        cmbOctroiPostModel = new EITLComboModel();
        cmbOctroiPost.removeAllItems();
        cmbOctroiPost.setModel(cmbOctroiPostModel);
        
        aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Paid by Party";
        cmbOctroiPostModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Paid by Us";
        cmbOctroiPostModel.addElement(aData);
        
        cmbInsurancePostModel = new EITLComboModel();
        cmbInsurancePost.removeAllItems();
        cmbInsurancePost.setModel(cmbInsurancePostModel);
        
        aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Paid by Party";
        cmbInsurancePostModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Paid by Us";
        cmbInsurancePostModel.addElement(aData);
        
        cmbClearancePostModel = new EITLComboModel();
        cmbClearancePost.removeAllItems();
        cmbClearancePost.setModel(cmbClearancePostModel);
        
        aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Paid by Party";
        cmbClearancePostModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Paid by Us";
        cmbClearancePostModel.addElement(aData);
        
        cmbAirFreightPostModel = new EITLComboModel();
        cmbAirFreightPost.removeAllItems();
        cmbAirFreightPost.setModel(cmbAirFreightPostModel);
        
        aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Paid by Party";
        cmbAirFreightPostModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Paid by Us";
        cmbAirFreightPostModel.addElement(aData);
        
        cmbOthersPostModel = new EITLComboModel();
        cmbOthersPost.removeAllItems();
        cmbOthersPost.setModel(cmbOthersPostModel);
        
        aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Paid by Party";
        cmbOthersPostModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Paid by Us";
        cmbOthersPostModel.addElement(aData);
        
        cmbPaymentTypeModel = new EITLComboModel();
        cmbPaymentType.removeAllItems();
        cmbPaymentType.setModel(cmbPaymentTypeModel);
        
        aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Credit";
        cmbPaymentTypeModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Cash";
        cmbPaymentTypeModel.addElement(aData);
        
        //====================================//
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
                
                List = ApprovalFlow.getRemainingUsers((int) EITLERPGLOBAL.gCompanyID, 7, (String) ObjGRN.getAttribute("GRN_NO").getObj());
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
        }
        
    }
    
    private void SetupApproval() {
        // --- Hierarchy Change Rights Check --------
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 535)) {
            cmbHierarchy.setEnabled(true);
        } else {
            cmbHierarchy.setEnabled(false);
        }
        
        //Set Default Hierarchy ID for User
        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);
        
        if (EditMode == EITLERPGLOBAL.ADD) {
            lnFromID = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {
            
            int FromUserID = ApprovalFlow.getFromID((int) EITLERPGLOBAL.gCompanyID, 7, (String) ObjGRN.getAttribute("GRN_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = ApprovalFlow.getFromRemarks((int) EITLERPGLOBAL.gCompanyID, 7, FromUserID, (String) ObjGRN.getAttribute("GRN_NO").getObj());
            
            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }
        
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if (clsHierarchy.CanSkip((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }
        
        if (clsHierarchy.CanFinalApprove((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        } else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
        
        //In Edit Mode Hierarchy Should be disabled
        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 531)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,532))
         {
         cmdEdit.setEnabled(true);
         }
         else
         {
         cmdEdit.setEnabled(false);
         }*/
        
        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 533)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 534)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }
    
    private void UpdateSrNo() {
        int SrCol = DataModelL.getColFromVariable("SR_NO");
        
        for (int i = 0; i < TableL.getRowCount(); i++) {
            TableL.setValueAt(Integer.toString(i + 1), i, SrCol);
        }
    }
    
    private void UpdateAmounts() {
        
        try {
            
            //== Final Pass - Update the Net Amount ==
            double lnNetAmount = 0;
            double lnColValue = 0;
            double lnGrossAmount = 0, lnSumNetAmount = 0, lnSumGrossAmount = 0;
            double HeaderTotal = 0;
            int NetAmountCol = 0, GrossAmountCol = 0;
            
            NetAmountCol = DataModelL.getColFromVariable("NET_AMOUNT");
            GrossAmountCol = DataModelL.getColFromVariable("GROSS_AMOUNT");
            
            for (int i = 0; i < TableL.getRowCount(); i++) {
                if (TableL.getValueAt(i, NetAmountCol) != null) {
                    lnSumNetAmount = lnSumNetAmount + Double.parseDouble((String) TableL.getValueAt(i, NetAmountCol));
                    lnSumGrossAmount = lnSumGrossAmount + Double.parseDouble((String) TableL.getValueAt(i, GrossAmountCol));
                }
            }
            
            for (int c = 0; c < TableH.getRowCount(); c++) {
                if (DataModelH.getInclude(c) == false) {
                    //Read column value
                    if (TableH.getValueAt(c, 1).toString().equals("")) {
                        lnColValue = 0;
                    } else {
                        lnColValue = Double.parseDouble((String) TableH.getValueAt(c, 1));
                    }
                    
                    if (DataModelH.getOperation(c).equals("+")) //Add
                    {
                        lnGrossAmount = lnGrossAmount + lnColValue;
                        HeaderTotal += lnColValue;
                    } else //Substract
                    {
                        lnGrossAmount = lnGrossAmount - lnColValue;
                        HeaderTotal -= lnColValue;
                    }
                }
            }
            
            /*txtGrossAmount.setText(Double.toString(EITLERPGLOBAL.round(lnSumGrossAmount,5)));
             txtNetAmount.setText(Double.toString(EITLERPGLOBAL.round(lnSumNetAmount,5)));
             txtFinalAmount.setText(Double.toString(EITLERPGLOBAL.round(lnSumNetAmount+lnGrossAmount,5)));*/
            txtGrossAmount.setText(new BigDecimal(EITLERPGLOBAL.round(lnSumGrossAmount, 3)).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
            txtNetAmount.setText(new BigDecimal(EITLERPGLOBAL.round(lnSumNetAmount, 3)).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
            txtFinalAmount.setText(new BigDecimal(EITLERPGLOBAL.round(lnSumNetAmount + lnGrossAmount, 3)).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
            
            lnGrossAmount = 0;
            GrossAmountCol = DataModelL.getColFromVariable("NET_AMOUNT");
            
            for (int i = 0; i < TableL.getRowCount(); i++) {
                if (TableL.getValueAt(i, GrossAmountCol) != null) {
                    lnGrossAmount = lnGrossAmount + Double.parseDouble((String) TableL.getValueAt(i, GrossAmountCol));
                }
            }
            
            for (int i = 0; i < TableL.getRowCount(); i++) {
                if (TableL.getValueAt(i, NetAmountCol) != null) {
                    //======= Calculate Landed Rate =======//
                    double NetAmount = EITLERPGLOBAL.round(Double.parseDouble((String) TableL.getValueAt(i, NetAmountCol)), 3);
                    NetAmount = NetAmount + UtilFunctions.CDbl(DataModelL.getValueByVariable("RND_DEDUCTION", i));
                    double GrossAmount = EITLERPGLOBAL.round(Double.parseDouble((String) TableL.getValueAt(i, GrossAmountCol)), 3);
                    double Percent = 0;
                    double lnQty = Double.parseDouble((String) TableL.getValueAt(i, DataModelL.getColFromVariable("QTY")));
                    double lnRecpQty = Double.parseDouble((String) TableL.getValueAt(i, DataModelL.getColFromVariable("RECEIVED_QTY")));
                    
                    double lnLandedRate = 0;
                    
                    if (lnQty > 0) {
                        Percent = EITLERPGLOBAL.round((GrossAmount * 100) / lnGrossAmount, 3);
                        
                        if (HeaderTotal != 0) {
                            lnLandedRate = EITLERPGLOBAL.round((NetAmount / lnQty), 3) + EITLERPGLOBAL.round((((HeaderTotal * Percent) / 100) / lnQty), 3);
                        } else {
                            lnLandedRate = EITLERPGLOBAL.round((NetAmount / lnQty), 3);
                        }
                        
                        lnLandedRate = EITLERPGLOBAL.round(lnLandedRate, 5);
                    } else {
                        lnLandedRate = 0;
                    }
                    
                    Updating = true;
                    DataModelL.setValueByVariable("LANDED_RATE", new BigDecimal(lnLandedRate).setScale(3, BigDecimal.ROUND_HALF_UP).toString(), i);
                    Updating = false;
                }
            }
            //==========================================================================//
        } catch (Exception e) {
            
        }
    }
    
    private void ClearFields() {
        //txtDocDate.setText("");
        txtSuppCode.setText("");
        txtChalanNo.setText("");
        txtChalanDate.setText("");
        txtLRNo.setText("");
        txtLRDate.setText("");
        txtInvoiceNo.setText("");
        txtInvoiceDate.setText("");
        txtGatepassNo.setText("");
        txtTransName.setText("");
        txtCurrencyRate.setText("0.00");
        txtPaymentRate.setText("0.00");
        txtRemarks.setText("");
        txtToRemarks.setText("");
        txtSuffix.setText("");
        txtSuppName.setText("");
        chkImportConcess.setSelected(false);
        chkCancelled.setSelected(false);
        txtToRemarks.setText("");
        txtInvoiceAmount.setText("");
        
        FormatGrid();
        FormatGrid_H();
        FormatGridA();
        FormatGridHS();
        FormatGrid_HSN();
        FormatGridHSNGRNPJV();
        
        txtGrossAmount.setText("0.00");
        txtNetAmount.setText("0.00");
        cmbCurrency.setSelectedIndex(0);
        
        EITLERPGLOBAL.setComboIndex(cmbPFPost, 0);
        EITLERPGLOBAL.setComboIndex(cmbFreightPost, 0);
        EITLERPGLOBAL.setComboIndex(cmbOctroiPost, 0);
        EITLERPGLOBAL.setComboIndex(cmbInsurancePost, 0);
        EITLERPGLOBAL.setComboIndex(cmbClearancePost, 0);
        EITLERPGLOBAL.setComboIndex(cmbAirFreightPost, 0);
        EITLERPGLOBAL.setComboIndex(cmbOthersPost, 0);
        EITLERPGLOBAL.setComboIndex(cmbPaymentType, 0);
        
    }
    
    private void SetFields(boolean pStat) {
        txtDocNo.setEnabled(false);
        cmdChange.setEnabled(pStat);
        //txtDocDate.setEnabled(pStat);
        txtSuppCode.setEnabled(pStat);
        
        if (pStat && txtSuppCode.getText().trim().equals("000000")) {
            txtSuppName.setEnabled(pStat);
        }
        
        txtChalanNo.setEnabled(pStat);
        txtChalanDate.setEnabled(pStat);
        txtLRNo.setEnabled(pStat);
        txtLRDate.setEnabled(pStat);
        txtInvoiceNo.setEnabled(pStat);
        txtInvoiceDate.setEnabled(pStat);
        cmbTransporter.setEnabled(pStat);
        txtTransName.setEnabled(pStat);
        txtGatepassNo.setEnabled(pStat);
        cmbCurrency.setEnabled(pStat);
        txtCurrencyRate.setEnabled(pStat);
        txtPaymentRate.setEnabled(pStat);
        cmbStatus.setEnabled(pStat);
        chkImportConcess.setEnabled(pStat);
        chkCancelled.setEnabled(pStat);
        txtRemarks.setEnabled(pStat);
        txtSuffix.setEnabled(pStat);
        txtInvoiceAmount.setEnabled(pStat);
        
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        
        cmdInsert.setEnabled(pStat);
        //cmdAdd.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);
        btnHSNUpdate.setEnabled(true);
        
        SetupApproval();
        
        DataModelH.TableReadOnly(!pStat);
        DataModelL.TableReadOnly(!pStat);
        
        if (!pStat) {
            lblFinHierarchy.setVisible(false);
            cmbFinHierarchy.setVisible(false);
        }
        
        cmbPFPost.setEnabled(pStat);
        cmbFreightPost.setEnabled(pStat);
        cmbOctroiPost.setEnabled(pStat);
        cmbInsurancePost.setEnabled(pStat);
        cmbClearancePost.setEnabled(pStat);
        cmbAirFreightPost.setEnabled(pStat);
        cmbOthersPost.setEnabled(pStat);
        cmbPaymentType.setEnabled(pStat);
    }
    
    private boolean Validate() {
        int ValidEntryCount = 0;
        int CurrId = EITLERPGLOBAL.getComboCode(cmbCurrency);
        if (CurrId != 1) {
            if ((txtCurrencyRate.getText().trim().equals("")) || (txtCurrencyRate.getText().trim().equals("0.00"))) {
                JOptionPane.showMessageDialog(null, "Please enter Currency Rate.");
                return false;
            }
        }
        //Validates Item Entries
        if (TableL.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter at least one item");
            return false;
        }
        
        if (txtInvoiceNo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter invoice no.");
            return false;
        }
        
        if (!txtInvoiceNo.getText().trim().equals("-")) {
            if (UtilFunctions.CDbl(txtInvoiceAmount.getText()) <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter invoice amount");
                return false;
            }
        }
        
        if (!txtChalanNo.getText().trim().equals("")) {
            if (txtChalanDate.getText().trim().equals("") || !EITLERPGLOBAL.isDate(txtChalanDate.getText())) {
                JOptionPane.showMessageDialog(null, "Please enter valid chalan date");
                return false;
            }
            
        }
        
        if (OpgFinal.isSelected()) {
            if (txtInvoiceDate.getText().trim().equals("") || !EITLERPGLOBAL.isDate(txtInvoiceDate.getText())) {
                JOptionPane.showMessageDialog(null, "Please enter valid Invoice date");
                return false;
            }
            
        }
        
        if (txtSuppCode.getText().trim().equals("000000") && txtSuppName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter party name");
            return false;
        }
        
        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int RateCol = DataModelL.getColFromVariable("RATE");
        int QtyCol = DataModelL.getColFromVariable("QTY");
        int BOECol = DataModelL.getColFromVariable("BOE_NO");
        
        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "";
            double Rate = 0, Qty = 0;
            
            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, RateCol) != null && TableL.getValueAt(i, QtyCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                Rate = Double.parseDouble((String) TableL.getValueAt(i, RateCol));
                Qty = Double.parseDouble((String) TableL.getValueAt(i, QtyCol));
                int DeptID = Integer.parseInt(DataModelL.getValueByVariable("DEPT_ID", i));
                
                double ChalanQty = Double.parseDouble((String) TableL.getValueAt(i, DataModelL.getColFromVariable("CHALAN_QTY")));
                
                if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID) && ChalanQty > 0 && DeptID > 0) {
                    ValidEntryCount++;
                } else {
                    JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. Valid Item ID,Quantity,Chalan Qty,Department");
                    TableL.changeSelection(i, 1, false, false);
                    return false;
                }
                
            }
            
            //Replace X wherever BOE No. is Blank
            if (DataModelL.getValueAt(i, BOECol).toString().trim().equals("")) {
                DataModelL.setValueAt("X", i, BOECol);
            }
            
            if (OpgFinal.isSelected()) {
                if (data.IsRecordExist("SELECT * FROM D_PUR_AMEND_HEADER WHERE APPROVED=0 AND CANCELLED=0 AND PO_NO='" + DataModelL.getValueByVariable("PO_NO", i) + "'")) {
                    JOptionPane.showMessageDialog(null, "PO Amendment is under Approval");
                    return false;
                }
            }
            
            double POQty = Double.parseDouble((String) DataModelL.getValueAt(i, DataModelL.getColFromVariable("PO_QTY")));
            double ReceivedQty = Double.parseDouble((String) DataModelL.getValueAt(i, DataModelL.getColFromVariable("RECEIVED_QTY")));
            double AcceptedQty = Double.parseDouble((String) DataModelL.getValueAt(i, DataModelL.getColFromVariable("QTY")));
            
            double RejectedQty = Double.parseDouble((String) DataModelL.getValueAt(i, DataModelL.getColFromVariable("REJECTED_QTY")));
            
            double ToleranceLimit = data.getDoubleValueFromDB("SELECT TOLERANCE_LIMIT FROM D_INV_ITEM_MASTER WHERE ITEM_ID LIKE '" + ItemID + "%' ");
            double ToleranceQty = (POQty * ToleranceLimit) / 100;
            if (ToleranceLimit > 0) {
                POQty = POQty + ToleranceQty;
            }
            
            //bhavesh code
            if ((AcceptedQty + RejectedQty) > (ReceivedQty + 0.001)) {
                JOptionPane.showMessageDialog(null, "Approved and Rejected Quantity is Greater Than Received Quantity. Please verify");
                return false;
            }
            
            //----------------------------
            if (POQty > 0) {
                if (POQty < AcceptedQty) {
                    JOptionPane.showMessageDialog(null, "Accepted Qty (" + AcceptedQty + ") exceeds PO Qty (" + (POQty - ToleranceQty) + ") with Tolerance Limit (" + ToleranceQty + ")");
                    return false;
                }
                /*if (POQty < ReceivedQty) {
                 JOptionPane.showMessageDialog(null,"Received Qty ("+ReceivedQty+") exceeds PO Qty ("+(POQty - ToleranceQty)+") with Tolerance Limit ("+ToleranceQty+")");
                 return false;
                 }*/
            }
        }
        
        if (ValidEntryCount == 0) {
            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify");
            return false;
        }
        
        //Now Header level validations
        if (txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter GRN Date");
            return false;
        }
        
        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Please select the hierarchy.");
            return false;
        }
        
        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null, "Please select the Approval Action");
            return false;
        }
        
        if (!txtSuppCode.getText().trim().equals("000000")) {
            if (!clsSupplier.IsValidSuppCodeEx1(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText())) {
                JOptionPane.showMessageDialog(null, "Please enter valid supplier code");
                return false;
            }
        }
        
        if (!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid GRN Date");
            return false;
        }
        
        if (!EITLERPGLOBAL.isDate(txtInvoiceDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid invoice date");
            return false;
        }
        
        if (!EITLERPGLOBAL.isDate(txtLRDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid L.R. date");
            return false;
        }
        
        if (!EITLERPGLOBAL.isDate(txtChalanDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid Chalan date");
            return false;
        }
        
        java.sql.Date docDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtDocDate.getText()));
        
        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the remarks for rejection");
            return false;
        }
        
        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please select the user, to whom rejected document to be send");
            return false;
        }
        
        //add on 03/09/09
        for (int i = 0; i < TableH.getRowCount(); i++) {
            if (Double.parseDouble(txtGrossAmount.getText().trim()) > 0) {
                double lnPercentValue = 0;
                int ColID = DataModelH.getColID(i);
                int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                String Variable = DataModelH.getVariable(i);
                if (Variable.substring(0, 2).equals("P_")) {
                    lnPercentValue = Double.parseDouble(TableH.getValueAt(i, 1).toString());
                    if (lnPercentValue > Double.parseDouble("100")) {
                        JOptionPane.showMessageDialog(this, "Please enter valid Percentage in " + Variable + "(" + lnPercentValue + "%) at header level");
                        return false;
                    }
                }
                
                if ((ColID != 0) && (ColID != -99) && (!Variable.substring(0, 2).equals("P_"))) {
                    if (data.IsRecordExist("SELECT * FROM D_COM_TAX_HEADER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID) + " AND TAX_ID=" + Integer.toString(TaxID) + " AND TAX_ID NOT IN (384,332) ")) {
                        lnPercentValue = Double.parseDouble(TableH.getValueAt(i, 1).toString());
                        if (lnPercentValue > Double.parseDouble(txtGrossAmount.getText().trim())) {
                            JOptionPane.showMessageDialog(this, "Please enter valid Amount in " + Variable.substring(0, Variable.indexOf("_")) + "(" + lnPercentValue + ") at header level");
                            return false;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < TableL.getRowCount(); i++) {
            for (int j = 1; j < TableL.getColumnCount(); j++) {
                if (Double.parseDouble(DataModelL.getValueByVariable("GROSS_AMOUNT", i)) > 0) {
                    double lnPercentValue = 0;
                    int ColID = DataModelL.getColID(j);
                    int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                    String Variable = DataModelL.getVariable(j);
                    if (Variable.substring(0, 2).equals("P_")) {
                        if (ObjTax.getUsePercentage((int) EITLERPGLOBAL.gCompanyID, TaxID)) {
                            lnPercentValue = Double.parseDouble(TableL.getValueAt(i, j).toString());
                            if (lnPercentValue > Double.parseDouble("100")) {
                                JOptionPane.showMessageDialog(this, "Please enter valid Percentage in " + Variable + "(" + lnPercentValue + "%) at detail level");
                                return false;
                            }
                        }
                    }
                    if ((ColID != 0) && (ColID != -99) && (!Variable.substring(0, 2).equals("P_"))) {
                        if (data.IsRecordExist("SELECT * FROM D_COM_TAX_HEADER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID) + " "
                        + "AND TAX_ID=" + Integer.toString(TaxID) + " AND TAX_ID NOT IN (323,377,379,380) ")) {
                            lnPercentValue = Double.parseDouble(TableL.getValueAt(i, j).toString());
                            if (lnPercentValue > Double.parseDouble(DataModelL.getValueByVariable("GROSS_AMOUNT", i))) {
                                JOptionPane.showMessageDialog(this, "Please enter valid Amount in " + Variable + "(" + lnPercentValue + ") at detail level");
                                return false;
                            }
                            
                        }
                    }
                }
            }
        }
        //end on 03/09/09
        /* 
        DataModelHSNGRNPJV.SetVariable(22, "CGST_INPUT_CR_PER");    //23
            DataModelHSNGRNPJV.SetVariable(23, "CGST_INPUT_CR_AMT");    //24
            DataModelHSNGRNPJV.SetVariable(24, "SGST_INPUT_CR_PER");    //25
            DataModelHSNGRNPJV.SetVariable(25, "SGST_INPUT_CR_AMT");    //26
            DataModelHSNGRNPJV.SetVariable(26, "IGST_INPUT_CR_PER");    //27
            DataModelHSNGRNPJV.SetVariable(27, "IGST_INPUT_CR_AMT");    //28*/
        for (int j = 0; j < TableHSNGRNPJV.getRowCount(); j++) {
           /* if(Double.parseDouble(TableHSNGRNPJV.getValueAt(j, 22).toString())==0 || Double.parseDouble(TableHSNGRNPJV.getValueAt(j, 24).toString())==0){
                if(Double.parseDouble(TableHSNGRNPJV.getValueAt(j, 23).toString())!=0 || Double.parseDouble(TableHSNGRNPJV.getValueAt(j, 25).toString())!=0){
                    JOptionPane.showMessageDialog(this, "Please enter valid Input Credit CGST/SGST % at detail level");
                    return false;
                }
            }else{
                
            }
            */
            /*if(Double.parseDouble(TableHSNGRNPJV.getValueAt(j, 24).toString())==0){
                if(Double.parseDouble(TableHSNGRNPJV.getValueAt(j, 25).toString())!=0){
                    JOptionPane.showMessageDialog(this, "Please enter valid Input Credit SGST % at detail level");
                    return false;
                }
            }
            */
            /*
            if(Double.parseDouble(TableHSNGRNPJV.getValueAt(j, 26).toString())==0){
                if(Double.parseDouble(TableHSNGRNPJV.getValueAt(j, 27).toString())!=0){
                    JOptionPane.showMessageDialog(this, "Please enter valid Input Credit IGST % at detail level");
                    return false;
                }
            }
            */
            try{
            if(!DataModelHSNGRNPJV.getValueByVariable("CGST_INPUT_CR_PER", j).equals("")){    
            if(Double.valueOf(DataModelHSNGRNPJV.getValueByVariable("CGST_INPUT_CR_PER", j))!=0){
                if(Double.valueOf(DataModelHSNGRNPJV.getValueByVariable("CGST_INPUT_CR_AMT", j))==0){
                    JOptionPane.showMessageDialog(this, "Please enter valid Input Credit CGST Amt at detail level");
                    return false;
                }
            }
            }
            if(!DataModelHSNGRNPJV.getValueByVariable("SGST_INPUT_CR_PER", j).equals("")){    
            if(Double.valueOf(DataModelHSNGRNPJV.getValueByVariable("SGST_INPUT_CR_PER", j))!=0){
                if(Double.valueOf(DataModelHSNGRNPJV.getValueByVariable("SGST_INPUT_CR_AMT", j))==0){
                    JOptionPane.showMessageDialog(this, "Please enter valid Input Credit SGST Amt at detail level");
                    return false;
                }
            }
            }
            if(!DataModelHSNGRNPJV.getValueByVariable("IGST_INPUT_CR_PER", j).equals("")){    
            if(Double.valueOf(DataModelHSNGRNPJV.getValueByVariable("IGST_INPUT_CR_PER", j))!=0){
                if(Double.valueOf(DataModelHSNGRNPJV.getValueByVariable("IGST_INPUT_CR_AMT", j))==0){
                    JOptionPane.showMessageDialog(this, "Please enter valid Input Credit IGST Amt at detail level");
                    return false;
                }
            } 
            }
            }catch(Exception e){
                e.printStackTrace();
                           JOptionPane.showMessageDialog(null,
     "An unexpected error has occurred:\n" + e.getMessage() + '\n' + Thread.currentThread().getStackTrace() +  "\nPlease send this error or inform.Thanks for your help.",
     "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        
        
        
        return true;
    }
    
    private void SetNumberFormats() {
        /*DecimalFormat decimalFormat=new DecimalFormat("0.00");
         NumberFormatter ObjFormater=new NumberFormatter(decimalFormat);
         ObjFormater.setAllowsInvalid(false);
         txtCurrencyRate.setFormatterFactory(new DefaultFormatterFactory(ObjFormater));*/
    }
    
    private void Add() {
        
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 7;
        
        if (aList.ShowList()) {
            EditMode = EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SelPrefix = aList.Prefix; //Selected Prefix;
            SelSuffix = aList.Suffix;
            FFNo = aList.FirstFreeNo;
            SetupApproval();
            //Display newly generated document no.
            if (chkKeep.isSelected()) {
                
            } else {
                txtDocNo.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 7, FFNo, false));
                txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());
            }
            txtDocDate.requestFocus();
            
            lblTitle.setText("GOODS RECEIPT NOTE (General) - " + txtDocNo.getText());
            lblTitle.setBackground(Color.BLUE);
        } else {
            JOptionPane.showMessageDialog(null, "You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
        
    }
    
    private void Edit() {
        
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        String lDocNo = (String) ObjGRN.getAttribute("GRN_NO").getObj();
        if (ObjGRN.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            
            EITLERPGLOBAL.ChangeCursorToWait(this);
            
            EditMode = EITLERPGLOBAL.EDIT;
            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//
            
            if (ApprovalFlow.IsCreator(7, lDocNo) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 532)) {
                SetFields(true);
            } else {
                EnableApproval();
            }
            
            DisableToolbar();
            txtDocDate.requestFocus();
            
            EITLERPGLOBAL.ChangeCursorToDefault(this);
            
            String MSMEUAN=data.getStringValueFromDB("SELECT MSME_UAN FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+txtSuppCode.getText()+"'");
            if(!MSMEUAN.equals("")){
                if(!MSMEUAN.equals("N/A")){
                   JOptionPane.showMessageDialog(null, "This party "+txtSuppCode.getText()+" is MSME Party. Please proceed immediately");
             
                }
            }
            String MSMEDIC=data.getStringValueFromDB("SELECT MSME_DIC_NO FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+txtSuppCode.getText()+"'");
            if(!MSMEDIC.equals("")){
                if(!MSMEDIC.equals("N/A")){
                   JOptionPane.showMessageDialog(null, "This party "+txtSuppCode.getText()+" is MSME Party. Please proceed immediately");
             
                }
            }
            int MSME=data.getIntValueFromDB("SELECT MSME FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+txtSuppCode.getText()+"'");
            if(MSME==1){
                   JOptionPane.showMessageDialog(null, "This party "+txtSuppCode.getText()+" is MSME Party. Please proceed immediately");
             
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot edit this record. It is either approved/rejected or waiting approval for other user");
        }
    }
    
    private void Delete() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        String lDocNo = (String) ObjGRN.getAttribute("GRN_NO").getObj();
        
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record ?", "SDML ERP", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (ObjGRN.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
                if (ObjGRN.Delete(EITLERPGLOBAL.gNewUserID)) {
                    MoveLast();
                } else {
                    JOptionPane.showMessageDialog(null, "Error occured while deleting. Error is " + ObjGRN.LastError);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
            }
        }
    }
    
    private void Save() {
                if (!txtSuppCode.getText().trim().equals("")) {
            String MSMEUAN=data.getStringValueFromDB("SELECT MSME_UAN FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+txtSuppCode.getText()+"'");
            if(!MSMEUAN.equals("")){
                if(!MSMEUAN.equals("N/A")){
                   JOptionPane.showMessageDialog(null, "This party "+txtSuppCode.getText()+" is MSME Party. Please proceed immediately");
             
                }
            }
            String MSMEDIC=data.getStringValueFromDB("SELECT MSME_DIC_NO FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+txtSuppCode.getText()+"'");
            if(!MSMEDIC.equals("")){
                if(!MSMEDIC.equals("N/A")){
                   JOptionPane.showMessageDialog(null, "This party "+txtSuppCode.getText()+" is MSME Party. Please proceed immediately");
             
                }
            }
            int MSME=data.getIntValueFromDB("SELECT MSME FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+txtSuppCode.getText()+"'");
            if(MSME==1){
                   JOptionPane.showMessageDialog(null, "This party "+txtSuppCode.getText()+" is MSME Party. Please proceed immediately");
             
            }
         }
        //Form level validations
        if (Validate() == false) {
            return; //Validation failed
        }
        
        EITLERPGLOBAL.ChangeCursorToWait(this);
        
        SetData();
        
        //ADDED BY GAURANG on 10/08/2017 for HSN/SAC Code Validation on Final Approval
        int hieId = ObjGRN.getAttribute("HIERARCHY_ID").getInt();
        String StatusCd = (String) ObjGRN.getAttribute("APPROVAL_STATUS").getObj();
        if (hieId != 1669) {
            if (!StatusCd.equals("R")) {
                for (int i = 1; i <= ObjGRN.colGRNItems.size(); i++) {
                    clsGRNGenItem ItemDetail = (clsGRNGenItem) ObjGRN.colGRNItems.get(Integer.toString(i));
                    String HsnSacCode = (String) ItemDetail.getAttribute("HSN_SAC_CODE").getObj();
                    if (HsnSacCode.equals("") || HsnSacCode == null || HsnSacCode.length() < 2) {
                        JOptionPane.showMessageDialog(null, "Please Insert HSN/SAC Code Into ITEM Master.");
                        return;
                    }
                }
            }
        }
        //-------------------------------
        
        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjGRN.Insert()) {
                MoveLast();
                DisplayData();
            } else {
                
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjGRN.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjGRN.Update()) {
                
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjGRN.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        try {
            frmPA.RefreshView();
        } catch (Exception e) {
        }
        ShowMessage("Ready");
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void Cancel() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.Stores.frmGRNGenFind", true);
        frmGRNGenFind ObjReturn = (frmGRNGenFind) ObjLoader.getObj();
        
        if (ObjReturn.Cancelled == false) {
            if (!ObjGRN.Filter(ObjReturn.strQuery, EITLERPGLOBAL.gCompanyID)) {
                JOptionPane.showMessageDialog(null, "No records found.");
            }
            MoveLast();
        }
    }
    
    private void MoveFirst() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjGRN.MoveFirst();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MovePrevious() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjGRN.MovePrevious();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MoveNext() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjGRN.MoveNext();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MoveLast() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjGRN.MoveLast();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    public void FindEx(int pCompanyID, String pDocNo) {
        ObjGRN.Filter(" WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1", pCompanyID);
        ObjGRN.MoveLast();
        DisplayData();
    }
    
    public void FindByCompany(int pCompanyID, String pDocNo) {
        String dbURL = clsFinYear.getDBURL(pCompanyID, EITLERPGLOBAL.FinYearFrom);
        ObjGRN.Filter(" WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1", pCompanyID, dbURL);
        ObjGRN.MoveLast();
        DisplayData();
    }
    
    public void FindWaiting() {
        ObjGRN.Filter(" WHERE GRN_NO IN(SELECT D_INV_GRN_HEADER.GRN_NO FROM D_INV_GRN_HEADER,D_COM_DOC_DATA WHERE D_INV_GRN_HEADER.GRN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GRN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GRN_HEADER.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND D_COM_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND D_COM_DOC_DATA.STATUS='W' AND D_INV_GRN_HEADER.GRN_TYPE=1 AND MODULE_ID=7)", EITLERPGLOBAL.gCompanyID);
        ObjGRN.MoveLast();
        DisplayData();
    }
    
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }
    
    private void FormatGridA() {
        DataModelA = new EITLTableModel();
        
        TableA.removeAll();
        TableA.setModel(DataModelA);
        
        //Set the table Readonly
        DataModelA.TableReadOnly(true);
        
        //Add the columns
        DataModelA.addColumn("Sr.");
        DataModelA.addColumn("User");
        DataModelA.addColumn("Status");
        DataModelA.addColumn("Department");
        DataModelA.addColumn("Received Date");
        DataModelA.addColumn("Action Date");
        DataModelA.addColumn("Remarks");
        
        TableA.setAutoResizeMode(TableA.AUTO_RESIZE_OFF);
        
    }
    
    private void UpdateResults(int pCol) {
        if (!DoNotEvaluate) {
            try {
                int ColID = 0, TaxID = 0, UpdateCol = 0;
                String strFormula = "", strItemID = "", strVariable = "", srcVariable = "", srcVar2 = "";
                double lnPercentValue = 0, lnFinalResult = 0, lnNetAmount = 0;
                Object result;
                boolean updateIt = true;
                int QtyCol = 0, RateCol = 0, GAmountCol = 0;
                
                Updating = true; //Stops Recursion
                
                srcVariable = DataModelL.getVariable(pCol); //Variable name of currently updated Column
                
                //If this column is percentage column. Variable name would be P_XXX
                //We shoule use actual variable name, it will be found on it's associated next column
                if (srcVariable.substring(0, 2).equals("P_")) {
                    srcVariable = DataModelL.getVariable(pCol + 1);
                }
                
                QtyCol = DataModelL.getColFromVariable("QTY"); //Index of Qty Column
                RateCol = DataModelL.getColFromVariable("RATE"); //Index of Rate Column
                GAmountCol = DataModelL.getColFromVariable("GROSS_AMOUNT"); //Index of Gross Amount Column
                
                //======= Read the Item ID - To be used when accessing item specific formula ===//
                String cellValue = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                if (cellValue == null) {
                    strItemID = "";
                } else {
                    strItemID = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                }
                //================================================================================
                
                GatherVariableValues();
                
                //====== Update Gross Amount =======
                CurrencyRate = 1;
                
                if (EITLERPGLOBAL.IsNumber(txtCurrencyRate.getText())) {
                    CurrencyRate = Double.parseDouble(txtCurrencyRate.getText());
                    if (CurrencyRate == 0) {
                        CurrencyRate = 1;
                    }
                }
                
                myParser.parseExpression("QTY*RATE");
                result = myParser.getValueAsObject();
                if (result != null) {
                    String RoundNum = Double.toString(EITLERPGLOBAL.round(Double.parseDouble(result.toString()) * CurrencyRate, 5));
                    DataModelL.setValueByVariable("GROSS_AMOUNT", RoundNum, TableL.getSelectedRow());
                }
                //=================================
                
                for (int i = 0; i < TableL.getColumnCount(); i++) {
                    strVariable = DataModelL.getVariable(i);
                    
                    ColID = DataModelL.getColID(i);
                    
                    TaxID = ObjColumn.getTaxID((int) EITLERPGLOBAL.gCompanyID, ColID);
                    
                    //Exclude Percentage Columns and System Columns
                    if ((!strVariable.substring(0, 2).equals("P_")) && (ColID != 0) && (ColID != -99)) {
                        //If percentage is used
                        if (ObjTax.getUsePercentage((int) EITLERPGLOBAL.gCompanyID, TaxID)) {
                            
                            //Load the Formula for calculation
                            if ((EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                                strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID, strItemID);
                            } else {
                                strFormula = DataModelL.getFormula(i);
                            }
                            
                            //Now Read Associated Percentage Column
                            lnPercentValue = Double.parseDouble(DataModelL.getValueByVariable("P_" + Integer.toString(ColID), TableL.getSelectedRow()));
                            
                            //Now Parse Main expression
                            myParser.parseExpression(strFormula);
                            result = myParser.getValueAsObject();
                            if (result != null) {
                                //Now get the percentage of the main result
                                lnFinalResult = (Double.parseDouble(result.toString()) * lnPercentValue) / 100;
                                //Update the Column
                                srcVar2 = DataModelL.getVariable(pCol + 1);
                                
                                UpdateCol = DataModelL.getColFromVariable(strVariable);
                                
                                updateIt = false;
                                
                                if (UpdateCol != pCol) {
                                    if (UpdateCol == pCol + 1) {
                                        updateIt = true;
                                    } else {
                                        if ((strFormula.indexOf(srcVariable) != -1)) { //If this column is dependent on updated column
                                            updateIt = true; //Then update it
                                        } else {
                                            
                                            //Check whether the formula is dependent on any system Columns
                                            boolean Dependent = false;
                                            int dCol = 0;
                                            
                                            for (int d = 0; d <= TableL.getColumnCount() - 1; d++) {
                                                if (DataModelL.getColID(d) == 0) //It's System Column
                                                {
                                                    String dVariable = DataModelL.getVariable(d);
                                                    if (strFormula.indexOf(dVariable) != -1) {
                                                        if (pCol == d) {
                                                            Dependent = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            
                                        }
                                    }
                                    
                                    //============ New Change In Parser =============//
                                    //Now Condition. First check whether percentage has been entered
                                    if (lnPercentValue > 0) {
                                        //Yes Percentage Entered. Then we must update the associated column
                                        updateIt = true;
                                    } else {
                                        //If not Percentage entered than check whether any value is there
                                        //Otherwise go with the Dependent decision
                                    }
                                    //=================================================//
                                    
                                }
                                
                                if (updateIt) {
                                    DataModelL.setValueByVariable(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 3)), TableL.getSelectedRow());
                                }
                                //Re Gather Fresh Variable Values
                                GatherVariableValues();
                            }
                        } else //Percentage Not Used
                        {
                            //Load the Formula for calculation
                            if ((EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                                strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID, strItemID);
                            } else {
                                strFormula = DataModelL.getFormula(i);
                            }
                            
                            //Now Parse Main expression
                            myParser.parseExpression(strFormula);
                            result = myParser.getValueAsObject();
                            if (result != null) {
                                //Now get the percentage of the main result
                                lnFinalResult = Double.parseDouble(result.toString());
                                //Update the Column
                                UpdateCol = DataModelL.getColFromVariable(strVariable);
                                
                                updateIt = false;
                                
                                if (UpdateCol != pCol) {
                                    if (strFormula.indexOf(srcVariable) != -1) {
                                        updateIt = true;
                                    } else {
                                        
                                        //Check whether the formula is dependent on any system Columns
                                        boolean Dependent = false;
                                        int dCol = 0;
                                        
                                        for (int d = 0; d <= TableL.getColumnCount() - 1; d++) {
                                            if (DataModelL.getColID(d) == 0) //It's System Column
                                            {
                                                String dVariable = DataModelL.getVariable(d);
                                                
                                                if (strFormula.indexOf(dVariable) != -1) {
                                                    if (pCol == d) {
                                                        Dependent = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        
                                        if (Dependent) {
                                            updateIt = true;
                                        }
                                        
                                    }
                                    
                                    //============ New Change In Parser =============//
                                    //Now Condition. First check whether percentage has been entered
                                    if (lnPercentValue > 0) {
                                        //Yes Percentage Entered. Then we must update the associated column
                                        updateIt = true;
                                    } else {
                                        //If not Percentage entered than check whether any value is there
                                        //Otherwise go with the Dependent decision
                                    }
                                    //=================================================//
                                    
                                }
                                if (updateIt) {
                                    DataModelL.setValueByVariable(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 3)), TableL.getSelectedRow());
                                }
                                //Re Gather Fresh Variable Values
                                GatherVariableValues();
                            }
                        }
                    }
                }
                
                //== Final Pass - Update the Net Amount ==
                lnNetAmount = 0;
                double lnColValue = 0;
                double lnGrossAmount = 0;
                
                lnGrossAmount = Double.parseDouble((String) DataModelL.getValueAt(TableL.getSelectedRow(), GAmountCol));
                
                for (int c = 0; c < TableL.getColumnCount(); c++) {
                    
                    //To be included in Calculation or not
                    if (DataModelL.getInclude(c) == false) {
                        //Read column value
                        
                        if (TableL.getValueAt(TableL.getSelectedRow(), c).toString().equals("")) {
                            lnColValue = 0;
                        } else {
                            lnColValue = Double.parseDouble((String) TableL.getValueAt(TableL.getSelectedRow(), c));
                        }
                        
                        if (DataModelL.getOperation(c).equals("+")) //Add
                        {
                            lnGrossAmount = lnGrossAmount + lnColValue;
                        } else //Substract
                        {
                            lnGrossAmount = lnGrossAmount - lnColValue;
                        }
                        
                    }
                }
                
                //Now update the Net Amount
                DataModelL.setValueByVariable("NET_AMOUNT", Double.toString(EITLERPGLOBAL.round(lnGrossAmount, 5)), TableL.getSelectedRow());
                
                Updating = false;
                
                //=======================================================//
                //======= New Change. Reverse Calculation ===============//
                //Calculate Percentage based on Amount
                ColID = DataModelL.getColID(pCol);
                int AsColID = DataModelL.getColID(pCol - 1);
                
                if (ColID != 0 && ColID != -99 && ColID == AsColID) {
                    TaxID = ObjColumn.getTaxID((int) EITLERPGLOBAL.gCompanyID, ColID);
                    //Read the formula
                    strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID, strItemID);
                    double EnteredValue = Double.parseDouble((String) DataModelL.getValueAt(TableL.getSelectedRow(), pCol));
                    
                    //Now Parse Main expression
                    myParser.parseExpression(strFormula);
                    result = myParser.getValueAsObject();
                    if (result != null) {
                        //x=(Gross Amount*Percent)/100
                        
                        //Reverse
                        // x*100/Gross amount=Percent
                        double percentValue = 0;
                        double val = 0;
                        
                        val = Double.parseDouble(result.toString());
                        
                        if (val != 0) {
                            percentValue = EITLERPGLOBAL.round((EnteredValue * 100) / val, 3);
                            DoNotEvaluate = true;
                            TableL.setValueAt(Double.toString(percentValue), TableL.getSelectedRow(), pCol - 1);
                            DoNotEvaluate = false;
                        }
                    }
                }
                //======================================================//
                //============= End of Reverse Procedure ===============//
                
                UpdateResults_H(0);
                UpdateAmounts();
            } catch (Exception e) {
                Updating = false;
            }
        }// Do not Evaluate
    }
    
    private void GatherVariableValues() {
        //Scan the table and gather values for variables
        colVariables.clear();
        
        myParser.initSymTab(); // clear the contents of the symbol table
        myParser.addStandardConstants();
        myParser.addComplex(); // among other things adds i to the symbol table
        
        for (int i = 0; i < TableL.getColumnCount(); i++) {
            double lValue = 0;
            if (DataModelL.getVariable(i) != null) {
                //if((!DataModelL.getVariable(i).trim().equals(""))&&(DataModelL.getColID(i)!=0))    //If Variable not blank
                if ((!DataModelL.getVariable(i).trim().equals(""))) {
                    //colVariables.put(DataModelL.getVariable(i),(String)DataModelL.getValueAt(TableL.getSelectedRow(), i));
                    
                    //Add variable Value to Parser Table
                    if ((TableL.getValueAt(TableL.getSelectedRow(), i) != null) && (!TableL.getValueAt(TableL.getSelectedRow(), i).toString().equals(""))) {
                        if (TableL.getValueAt(TableL.getSelectedRow(), i) instanceof Boolean) {
                            if (DataModelL.getBoolValueByVariable(DataModelL.getVariable(i), TableL.getSelectedRow())) {
                                lValue = 1;
                            } else {
                                lValue = 0;
                            }
                        } else {
                            if (EITLERPGLOBAL.IsNumber((String) TableL.getValueAt(TableL.getSelectedRow(), i))) {
                                lValue = Double.parseDouble((String) TableL.getValueAt(TableL.getSelectedRow(), i));
                            }
                        }
                    } else {
                        lValue = 0;
                    }
                    myParser.addVariable(DataModelL.getVariable(i), lValue);
                }
            }
        }
        
        myParser.addFunction("IIF", new IIF(myParser));
        
        CurrencyRate = 1;
        
        if (EITLERPGLOBAL.IsNumber(txtCurrencyRate.getText())) {
            CurrencyRate = Double.parseDouble(txtCurrencyRate.getText());
            if (CurrencyRate == 0) {
                CurrencyRate = 1;
            }
        }
        
        myParser.addVariable("CURRENCY_RATE", CurrencyRate);
        
    }
    
    private void EnableApproval() {
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        txtToRemarks.setEnabled(true);
        SetupApproval();
        
        //========== Setting Up Header Fields ================//
        String FieldName = "";
        int SelHierarchy = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        
        for (int i = 0; i < Tab1.getComponentCount() - 1; i++) {
            if (Tab1.getComponent(i).getName() != null) {
                
                FieldName = Tab1.getComponent(i).getName();
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    
                    Tab1.getComponent(i).setEnabled(true);
                }
                
            }
        }
        //=============== Header Fields Setup Complete =================//
        
        //=============== Setting Table Fields ==================//
        DataModelL.ClearAllReadOnly();
        for (int i = 0; i < TableL.getColumnCount(); i++) {
            FieldName = DataModelL.getVariable(i);
            
            if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
                //Do Nothing
            } else {
                DataModelL.SetReadOnly(i);
            }
        }
        //=======================================================//
        
    }
    
    private void FormatGridHS() {
        DataModelHS = new EITLTableModel();
        
        TableHS.removeAll();
        TableHS.setModel(DataModelHS);
        
        //Set the table Readonly
        DataModelHS.TableReadOnly(true);
        
        //Add the columns
        DataModelHS.addColumn("Rev No.");
        DataModelHS.addColumn("User");
        DataModelHS.addColumn("Date");
        DataModelHS.addColumn("Status");
        DataModelHS.addColumn("Remarks");
        
        TableHS.setAutoResizeMode(TableHS.AUTO_RESIZE_OFF);
    }
    
    private void PreviewReport() {
        HashMap Params=new HashMap();
        
        //(1) company_id - Integer
        //(2) grn_no     - String
        
        if(chkCancelled.isSelected()) {
            JOptionPane.showMessageDialog(null,"You cannot take printout of cancelled document");
            return;
        }
        
        
        Params.put("company_id", new Integer(EITLERPGLOBAL.gCompanyID));
        Params.put("grn_no",txtDocNo.getText());
        
        Connection Conn = null;
        
        Conn = data.getConn();
        EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(Params, Conn);
        
        String strSQL = "select "
        +" a.party_name,b.item_extra_desc,usr.user_name usr_name,a.created_date usr_date,e.company_id,e.company_name,"
        +" e.add1,e.add2,e.add3,e.city,e.state,e.pincode,e.phone,e.fax ,a.supp_id,c.supp_name,c.add1 s_add1,"
        +" c.add2 s_add2,c.add3 s_add3,c.city s_city,a.grn_no,a.grn_date,a.chalan_no,a.chalan_date,a.invoice_no,"
        +" a.invoice_date,a.invoice_amount,a.lr_no,a.lr_date,a.gatepass_no,a.transporter,g1.desc transporter_name,"
        +" a.approved,a.rejected,a.cancelled,b.sr_no,b.item_id,d.item_description,b.unit,g.DESC UNITDESC,"
        +" b.po_no,f1.po_date,b.po_sr_no,b.mir_no,MIRH.MIR_DATE,b.location_id,b.remarks BRE,b.received_qty,b.balance_po_qty,f.recd_qty rec_qty,b.mir_sr_no,"
        +" b.CHALAN_QTY,b.REJECTED_QTY ,b.qty acc_qty,f.qty po_qty,round((f.qty - b.qty ),2) bal_qty,c.supplier_code,"
        +" d.item_sys_id,f2.indent_no,f2.indent_date,b.landed_rate fnl_rate,b.column_6_amt freight_detail,b.column_7_amt octroi_detail,"
        +" b.column_21_amt others_detail,a.column_6_amt freight_header,a.column_7_amt octroi_header,a.column_21_amt others_header,dept.dept_desc "
        +" from "
        +" D_INV_GRN_HEADER as  a "
        +" left join D_COM_SUPP_MASTER  as c on ( a.supp_id= c.supplier_code and a.company_id = c.company_id) "
        +" left join D_COM_PARAMETER_MAST as g1 on ( g1.PARA_ID='TRANSPORT' AND g1.PARA_CODE=a.transporter and g1.company_id = a.company_id) "
        +" left join D_COM_USER_MASTER as usr on (a.created_by = usr.user_id and a.company_id=usr.company_id), "
        +" D_INV_GRN_DETAIL as b "
        +" left join D_COM_DEPT_MASTER as dept on (b.dept_id = dept.dept_id) "
        +" left join D_PUR_PO_DETAIL as f on (b.company_id=f.company_id and b.po_no=f.po_no and b.item_id = f.item_id and b.po_sr_no = f.sr_no) "
        +" left join D_PUR_PO_HEADER as f1 on (f.company_id = f1.company_id and f.po_no = f1.po_no) "
        +" left join D_INV_INDENT_HEADER as f2 on ( f.company_id = f2.company_id and f.indent_no = f2.indent_no) "
        +" left join D_COM_PARAMETER_MAST as g ON (g.company_id = b.company_id and g.PARA_ID='UNIT' and g.PARA_CODE=b.UNIT) "
        +" left join D_INV_ITEM_MASTER AS d ON (b.company_id = d.company_id and b.item_id = d.item_id)"
        +" left join D_INV_MIR_HEADER AS MIRH ON (MIRH.MIR_NO=b.MIR_NO AND MIRH.MIR_TYPE=b.MIR_TYPE), "
        +" D_COM_COMPANY_MASTER as e"
        +" where "
        +" a.grn_no = '"+txtDocNo.getText()+"' "
        +" and a.company_id= "+EITLERPGLOBAL.gCompanyID+""
        +" and a.company_id = b.company_id"
        +" and a.grn_no = b.grn_no"
        +" and a.company_id = e.company_id";
        
        System.out.println(strSQL);
        
        rpt.setReportName("/EITLERP/Reports/jrxml/rpt_INV_GRN.jrxml", 1, strSQL); //productlist is the name of my jasper file.
        rpt.callReport();
        
        //        try {
        //            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptGRNGen.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
        //            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        //        }
        //        catch(Exception e) {
        //            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        //        }
    }
    private void PreviewReport_GST() {
        HashMap Params=new HashMap();
        
        //(1) company_id - Integer
        //(2) grn_no     - String
        
        if(chkCancelled.isSelected()) {
            JOptionPane.showMessageDialog(null,"You cannot take printout of cancelled document");
            return;
        }
        
        
        Params.put("company_id", new Integer(EITLERPGLOBAL.gCompanyID));
        Params.put("grn_no",txtDocNo.getText());
        
        Connection Conn = null;
        
        Conn = data.getConn();
        EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(Params, Conn);
        
        String strSQL = "SELECT * FROM (select "
        //+" a.party_name,b.item_extra_desc,usr.user_name usr_name,a.created_date usr_date,e.company_id,e.company_name,"
        + " e.add1,e.add2,e.add3,e.city,e.state,e.pincode,e.phone,e.fax ,a.supp_id,IF(c.MSME=1,CONCAT(c.supp_name,' (MSME Party)'),c.supp_name) supp_name,c.add1 s_add1,"
        +" e.add1,e.add2,e.add3,e.city,e.state,e.pincode,e.phone,e.fax ,a.supp_id,c.supp_name,c.add1 s_add1,"
        +" c.add2 s_add2,c.add3 s_add3,c.city s_city,a.grn_no,a.grn_date,a.chalan_no,a.chalan_date,a.invoice_no,"
        +" a.invoice_date,a.invoice_amount,a.lr_no,a.lr_date,a.gatepass_no,a.transporter,g1.desc transporter_name,"
        +" a.approved,a.rejected,a.cancelled,b.sr_no,b.item_id,d.item_description,b.unit,g.DESC UNITDESC,"
        +" b.po_no,f1.po_date,b.po_sr_no,b.mir_no,MIRH.MIR_DATE,b.location_id,b.remarks BRE,b.received_qty,b.balance_po_qty,f.recd_qty rec_qty,b.mir_sr_no,"
        +" b.CHALAN_QTY,b.REJECTED_QTY ,b.qty acc_qty,f.qty po_qty,round((f.qty - b.qty ),2) bal_qty,c.supplier_code,"
        +" d.item_sys_id,f2.indent_no,f2.indent_date,b.landed_rate fnl_rate,b.column_15_amt freight_detail,b.column_16_amt octroi_detail,"
        +" b.column_31_amt others_detail,a.column_6_amt freight_header,a.column_7_amt octroi_header,a.column_21_amt others_header,dept.dept_desc,"
        +" e.GSTIN_NO,d.HSN_SAC_CODE,c.GSTIN_NO AS SUPP_GSTIN_NO "
        +" from "
        +" D_INV_GRN_HEADER as  a "
        +" left join D_COM_SUPP_MASTER  as c on ( a.supp_id= c.supplier_code and a.company_id = c.company_id) "
        +" left join D_COM_PARAMETER_MAST as g1 on ( g1.PARA_ID='TRANSPORT' AND g1.PARA_CODE=a.transporter and g1.company_id = a.company_id) "
        +" left join D_COM_USER_MASTER as usr on (a.created_by = usr.user_id and a.company_id=usr.company_id),"
        +" D_INV_GRN_DETAIL as b "
        +" left join D_COM_DEPT_MASTER as dept on (b.dept_id = dept.dept_id) "
        +" left join D_PUR_PO_DETAIL as f on (b.company_id=f.company_id and b.po_no=f.po_no and b.item_id = f.item_id and b.po_sr_no = f.sr_no) "
        +" left join D_PUR_PO_HEADER as f1 on (f.company_id = f1.company_id and f.po_no = f1.po_no) "
        +" left join D_INV_INDENT_HEADER as f2 on ( f.company_id = f2.company_id and f.indent_no = f2.indent_no) "
        +" left join D_COM_PARAMETER_MAST as g ON (g.company_id = b.company_id and g.PARA_ID='UNIT' and g.PARA_CODE=b.UNIT) "
        +" left join D_INV_ITEM_MASTER AS d ON (b.company_id = d.company_id and b.item_id = d.item_id)"
        +" left join D_INV_MIR_HEADER AS MIRH ON (MIRH.MIR_NO=b.MIR_NO AND MIRH.MIR_TYPE=b.MIR_TYPE),"
        +" D_COM_COMPANY_MASTER as e"
        +" where "
        +" a.grn_no = '"+txtDocNo.getText()+"'"
        +" and a.company_id= "+EITLERPGLOBAL.gCompanyID+" "
        +" and a.company_id = b.company_id "
        +" and a.grn_no = b.grn_no "
        +" and a.company_id = e.company_id) SUB ";
        
        System.out.println(strSQL);
        
        rpt.setReportName("/EITLERP/Reports/jrxml/rpt_INV_GRN_GST.jrxml", 1, strSQL); //productlist is the name of my jasper file.
        rpt.callReport();
        //
        //        try {
        //            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptGRNGen_GST.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
        //            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        //        }
        //        catch(Exception e) {
        //            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        //        }
    }
    private void PreviewReport_NonGST_Updated() {
        HashMap Params=new HashMap();
        
        //(1) company_id - Integer
        //(2) grn_no     - String
        
        if(chkCancelled.isSelected()) {
            JOptionPane.showMessageDialog(null,"You cannot take printout of cancelled document");
            return;
        }
        
        
        Params.put("company_id", new Integer(EITLERPGLOBAL.gCompanyID));
        Params.put("grn_no",txtDocNo.getText());
        
        Connection Conn = null;
        
        Conn = data.getConn();
        EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(Params, Conn);
        
        String strSQL = "SELECT * FROM (select "
        +" a.party_name,b.item_extra_desc,usr.user_name usr_name,a.created_date usr_date,e.company_id,e.company_name,"
        +" e.add1,e.add2,e.add3,e.city,e.state,e.pincode,e.phone,e.fax ,a.supp_id,c.supp_name,c.add1 s_add1,"
        +" c.add2 s_add2,c.add3 s_add3,c.city s_city,a.grn_no,a.grn_date,a.chalan_no,a.chalan_date,a.invoice_no,"
        +" a.invoice_date,a.invoice_amount,a.lr_no,a.lr_date,a.gatepass_no,a.transporter,g1.desc transporter_name,"
        +" a.approved,a.rejected,a.cancelled,b.sr_no,b.item_id,d.item_description,b.unit,g.DESC UNITDESC,"
        +" b.po_no,f1.po_date,b.po_sr_no,b.mir_no,MIRH.MIR_DATE,b.location_id,b.remarks BRE,b.received_qty,b.balance_po_qty,f.recd_qty rec_qty,b.mir_sr_no,"
        +" b.CHALAN_QTY,b.REJECTED_QTY ,b.qty acc_qty,f.qty po_qty,round((f.qty - b.qty ),2) bal_qty,c.supplier_code,"
        +" d.item_sys_id,f2.indent_no,f2.indent_date,b.landed_rate fnl_rate,b.column_15_amt freight_detail,b.column_16_amt octroi_detail,"
        +" b.column_31_amt others_detail,a.column_6_amt freight_header,a.column_7_amt octroi_header,a.column_21_amt others_header,dept.dept_desc "
        +" from "
        +" D_INV_GRN_HEADER as  a "
        +" left join D_COM_SUPP_MASTER  as c on ( a.supp_id= c.supplier_code and a.company_id = c.company_id) "
        +" left join D_COM_PARAMETER_MAST as g1 on ( g1.PARA_ID='TRANSPORT' AND g1.PARA_CODE=a.transporter and g1.company_id = a.company_id) "
        +" left join D_COM_USER_MASTER as usr on (a.created_by = usr.user_id and a.company_id=usr.company_id), "
        +" D_INV_GRN_DETAIL as b "
        +" left join D_COM_DEPT_MASTER as dept on (b.dept_id = dept.dept_id) "
        +" left join D_PUR_PO_DETAIL as f on (b.company_id=f.company_id and b.po_no=f.po_no and b.item_id = f.item_id and b.po_sr_no = f.sr_no) "
        +" left join D_PUR_PO_HEADER as f1 on (f.company_id = f1.company_id and f.po_no = f1.po_no) "
        +" left join D_INV_INDENT_HEADER as f2 on ( f.company_id = f2.company_id and f.indent_no = f2.indent_no) "
        +" left join D_COM_PARAMETER_MAST as g ON (g.company_id = b.company_id and g.PARA_ID='UNIT' and g.PARA_CODE=b.UNIT) "
        +" left join D_INV_ITEM_MASTER AS d ON (b.company_id = d.company_id and b.item_id = d.item_id) "
        +" left join D_INV_MIR_HEADER AS MIRH ON (MIRH.MIR_NO=b.MIR_NO AND MIRH.MIR_TYPE=b.MIR_TYPE), "
        +" D_COM_COMPANY_MASTER as e "
        +" where "
        +" a.grn_no = '"+txtDocNo.getText()+ "'"
        +" and a.company_id= "+EITLERPGLOBAL.gCompanyID+""
        +" and a.company_id = b.company_id "
        +" and a.grn_no = b.grn_no "
        +" and a.company_id = e.company_id) SUB ";
        
        System.out.println(strSQL);
        
        rpt.setReportName("/EITLERP/Reports/jrxml/rpt_INV_GRN_UPDATE.jrxml", 1, strSQL); //productlist is the name of my jasper file.
        rpt.callReport();
        
        //        try {
        //            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptGRNGen_NonGST_Updated.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
        //            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        //        }
        //        catch(Exception e) {
        //            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        //        }
    }
    
    
    private void PreviewAuditReport() {
        try {
            URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptGRN1A.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&DocType=1");
            EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Previwing " + e.getMessage());
        }
    }
    
    private void GenerateRejectedUserCombo() {
        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        
        //----- Generate cmbType ------- //
        cmbToModel = new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbToModel);
        
        //** Find the department who raised PO**//
        if (EditMode == EITLERPGLOBAL.EDIT && OpgReject.isSelected()) {
            try {
                String GRNNo = ObjGRN.getAttribute("GRN_NO").getString();
                
                //--> Find distinct PO nos in this GRN
                ResultSet rsPO = data.getResult("SELECT DISTINCT(PO_NO) FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND PO_NO<>''");
                rsPO.first();
                
                if (rsPO.getRow() > 0) {
                    //Now Find distinct departments in all these POs
                    while (!rsPO.isAfterLast()) {
                        String PONo = rsPO.getString("PO_NO");
                        
                        int BuyerID = data.getIntValueFromDB("SELECT BUYER FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "'");
                        
                        ComboData aData = new ComboData();
                        aData.Code = BuyerID;
                        aData.Text = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, BuyerID);
                        
                        if (aData.Code != EITLERPGLOBAL.gNewUserID) {
                            cmbToModel.addElement(aData);
                        }
                        
                        rsPO.next();
                    }
                    
                }
            } catch (Exception e) {
            }
            
        }
        
        //Now Add other hierarchy Users
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        
        List = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, true);
        for (int i = 1; i <= List.size(); i++) {
            clsUser ObjUser = (clsUser) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
            aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
            
            /// NEW CODE ///
            boolean IncludeUser = false;
            //Decide to include user or not
            if (EditMode == EITLERPGLOBAL.EDIT) {
                if (OpgApprove.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, 7, txtDocNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if (OpgReject.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, 7, txtDocNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if (IncludeUser && (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID)) {
                    cmbToModel.addElement(aData);
                }
            } else {
                if (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID) {
                    cmbToModel.addElement(aData);
                }
            }
            /// END NEW CODE ///
            
        }
        //------------------------------ //
        
        if (EditMode == EITLERPGLOBAL.EDIT) {
            int Creator = ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, 7, txtDocNo.getText());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }
        
    }
    
    //    private void GenerateHSNGRNData1() {
    //        ArrayList<String> list = new ArrayList<>();
    //        ArrayList<String> list1 = new ArrayList<>();
    //        ArrayList<String> list2 = new ArrayList<>();
    ////        System.out.println("TOTAL Rows : " + TableL.getRowCount());
    //
    //        for (int i = 0; i < TableL.getRowCount(); i++) {
    //            list.add(DataModelL.getValueByVariable("HSN_SAC_CODE", i));
    //        }
    //
    //        for (String d : list) {
    //            if (d != null && d.length() > 0) {
    ////                System.out.println("VALUE OF LIST : " + d);
    //                list1.add(d);
    //            }
    //        }
    //
    //        if (list1.size() > 0) {
    //            for (int i = 0; i < list1.size(); i++) {
    ////                System.out.println("list1 vslue at " + i + " is : " + list1.get(i));
    //            }
    //
    //            int cnt = 0;
    ////            System.out.println("vvvvvvvvvvvv : " + String.valueOf(list1.get(0)));
    //            c[cnt] = String.valueOf(list1.get(0));
    //            list2.add(list1.get(0));
    //            for (int i = 0; i < list1.size(); i++) {
    //                if (!c[cnt].equals(list1.get(i))) {
    //                    if (!list2.contains(list1.get(i))) {
    //                        cnt++;
    //                        c[cnt] = list1.get(i);
    //                        list2.add(c[cnt]);
    //                    }
    ////                    if (list2.size() == 0) {
    ////                        cnt++;
    ////                        c[cnt] = list1.get(i);
    ////                        list2.add(c[cnt]);
    ////                    } else {
    ////                        if (!list2.contains(list1.get(i))) {
    ////                            cnt++;
    ////                            c[cnt] = list1.get(i);
    ////                            list2.add(c[cnt]);
    ////                        }
    ////                    }
    //                }
    //            }
    ////            for (int i = 0; i < list1.size(); i++) {
    ////                if (!c[cnt].equals(list1.get(i))) {
    ////                    cnt++;
    ////                    c[cnt] = list1.get(i);
    ////                }
    ////            }
    //
    ////            System.out.println("COUNT : " + cnt);
    //            for (int i = 0; i <= cnt; i++) {
    ////                System.out.println("FINAL LIST OF HSN : " + c[i]);
    //            }
    //
    //            for (int i = 0; i <= cnt; i++) {
    //                netAmt = 0;
    //                CGSTAmt = 0;
    //                SGSTAmt = 0;
    //                IGSTAmt = 0;
    //                RCMAmt = 0;
    //                CompositionAmt = 0;
    //                GSTCompCessAmt = 0;
    //                for (int j = 0; j < TableL.getRowCount(); j++) {
    //                    hsnCode = DataModelL.getValueByVariable("HSN_SAC_CODE", j);
    //                    if (c[i].equals(hsnCode)) {
    //                        netAmt += Double.valueOf(DataModelL.getValueByVariable("NET_AMOUNT", j));
    //                        CGSTAmt += Double.valueOf(DataModelL.getValueByVariable("CGST", j));
    //                        SGSTAmt += Double.valueOf(DataModelL.getValueByVariable("SGST", j));
    //                        IGSTAmt += Double.valueOf(DataModelL.getValueByVariable("IGST", j));
    //                        RCMAmt += Double.valueOf(DataModelL.getValueByVariable("RCM", j));
    //                        CompositionAmt += Double.valueOf(DataModelL.getValueByVariable("COMPOSITION", j));
    //                        GSTCompCessAmt += Double.valueOf(DataModelL.getValueByVariable("GST_COMPENSATION_CESS", j));
    //                    }
    //                }
    //                Object[] rowData2 = new Object[25];
    //                rowData2[0] = c[i];
    //                rowData2[1] = 0.00;
    //                rowData2[2] = netAmt;
    //                rowData2[3] = 0.00;
    //                rowData2[4] = 0.00;
    //                rowData2[5] = CGSTAmt;
    //                rowData2[6] = 0.00;
    //                rowData2[7] = 0.00;
    //                rowData2[8] = SGSTAmt;
    //                rowData2[9] = 0.00;
    //                rowData2[10] = 0.00;
    //                rowData2[11] = IGSTAmt;
    //                rowData2[12] = 0.00;
    //                rowData2[13] = 0.00;
    //                rowData2[14] = RCMAmt;
    //                rowData2[15] = 0.00;
    //                rowData2[16] = 0.00;
    //                rowData2[17] = CompositionAmt;
    //                rowData2[18] = 0.00;
    //                rowData2[19] = 0.00;
    //                rowData2[20] = GSTCompCessAmt;
    //                rowData2[21] = 0.00;
    //                DataModelHSNGRNPJV.addRow(rowData2);
    //            }
    //        }
    //    }
    private void GenerateHSNGRNData() {
        try {
            String hsnCd = "";
            int aCnt = 0;
            int bCnt = 0;
            
            for (int i = 0; i < TableL.getRowCount(); i++) {
                hsnCd = DataModelL.getValueByVariable("HSN_SAC_CODE", i);
                if (hsnCd != null || !hsnCd.equals("") || hsnCd.length() > 0) {
                    a[aCnt] = DataModelL.getValueByVariable("HSN_SAC_CODE", i);
                    //System.out.println("A Count : " + aCnt);
                    aCnt++;
                }
            }
            
            if (aCnt > 0) {
                for (int s = 0; s < aCnt; s++) {
                    //System.out.println("S Count : " + s + " : " + a[s]);
                    for (int m = s + 1; m < aCnt; m++) {
                        if (a[s] != null && a[s].equals(a[m])) {
                            //System.out.println("M Count : " + m + " : " + a[m]);
                            a[m] = null;
                            //System.out.println("Value of M : " + m + " : " + a[m]);
                        }
                    }
                }
                
                for (int p = 0; p < aCnt; p++) {
                    if (a[p] != null) {
                        b[bCnt] = a[p];
                        //System.out.println("b[" + bCnt + "] : " + b[bCnt]);
                        bCnt++;
                    }
                }
                
                for (int i = 0; i < bCnt; i++) {
                    netAmt = 0;
                    CGSTAmt = 0;
                    SGSTAmt = 0;
                    IGSTAmt = 0;
                    RCMAmt = 0;
                    CompositionAmt = 0;
                    GSTCompCessAmt = 0;
                    
                    double CGSTInputCr=0;
                    double CGSTInputCrPer=0;
                    double SGSTInputCr=0;
                    double SGSTInputCrPer=0;
                    double IGSTInputCr=0;
                    double IGSTInputCrPer=0;
                    
                    for (int j = 0; j < TableL.getRowCount(); j++) {
                        hsnCode = DataModelL.getValueByVariable("HSN_SAC_CODE", j);
                        if (b[i].equals(hsnCode)) {
                            //netAmt += Double.valueOf(DataModelL.getValueByVariable("NET_AMOUNT", j)); //closed on 12/09/2017
                            netAmt += Double.valueOf(DataModelL.getValueByVariable("GROSS_AMOUNT", j)) - Double.valueOf(DataModelL.getValueByVariable("DISCOUNT", j)); //added on 12/09/2017
                            CGSTAmt += Double.valueOf(DataModelL.getValueByVariable("CGST", j));
                            SGSTAmt += Double.valueOf(DataModelL.getValueByVariable("SGST", j));
                            IGSTAmt += Double.valueOf(DataModelL.getValueByVariable("IGST", j));
                            RCMAmt += Double.valueOf(DataModelL.getValueByVariable("RCM", j));
                            CompositionAmt += Double.valueOf(DataModelL.getValueByVariable("COMPOSITION", j));
                            GSTCompCessAmt += Double.valueOf(DataModelL.getValueByVariable("GST_COMPENSATION_CESS", j));
                            
                            CGSTInputCr += Double.valueOf(DataModelL.getValueByVariable("CGST_INPUT_CREDIT", j));
                            SGSTInputCr += Double.valueOf(DataModelL.getValueByVariable("SGST_INPUT_CREDIT", j));
                            IGSTInputCr += Double.valueOf(DataModelL.getValueByVariable("IGST_INPUT_CREDIT", j));                            
                            CGSTInputCrPer = Double.valueOf(DataModelL.getValueByVariable("P_850", j)); //9 //sr no of dcomcolumns
                            SGSTInputCrPer = Double.valueOf(DataModelL.getValueByVariable("P_851", j)); //10
                            IGSTInputCrPer = Double.valueOf(DataModelL.getValueByVariable("P_852", j)); //11
                            
                        }
                    }
                    Object[] rowData2 = new Object[30];
                    rowData2[0] = b[i];
                    rowData2[1] = netAmt;
                    rowData2[2] = netAmt;
                    rowData2[3] = 0.00;
                    rowData2[4] = CGSTAmt;
                    rowData2[5] = CGSTAmt;
                    rowData2[6] = 0.00;
                    rowData2[7] = SGSTAmt;
                    rowData2[8] = SGSTAmt;
                    rowData2[9] = 0.00;
                    rowData2[10] = IGSTAmt;
                    rowData2[11] = IGSTAmt;
                    rowData2[12] = 0.00;
                    rowData2[13] = RCMAmt;
                    rowData2[14] = RCMAmt;
                    rowData2[15] = 0.00;
                    rowData2[16] = CompositionAmt;
                    rowData2[17] = CompositionAmt;
                    rowData2[18] = 0.00;
                    rowData2[19] = GSTCompCessAmt;
                    rowData2[20] = GSTCompCessAmt;
                    rowData2[21] = 0.00;
                    
                    rowData2[22] = CGSTInputCrPer;
                    rowData2[23] = CGSTInputCr;
                    rowData2[24] = SGSTInputCrPer;
                    rowData2[25] = SGSTInputCr;
                    rowData2[26] = IGSTInputCrPer;
                    rowData2[27] = IGSTInputCr;                    
                    
                    DataModelHSNGRNPJV.addRow(rowData2);
                }
                
            } //End of Array with Record
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static double HSNDouble(String columnName, double DefaultValue) {
        double value = 0;
        try {
            if (columnName.equals("") || columnName == null) {
                return DefaultValue;
            } else {
                value = Double.parseDouble(columnName);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DefaultValue;
        }
    }
    
}
