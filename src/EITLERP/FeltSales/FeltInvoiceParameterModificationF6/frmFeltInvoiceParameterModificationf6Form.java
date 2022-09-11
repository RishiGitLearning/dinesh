/*
 * frmFeltProcessInvoiceVariable.java
 *
 * Created on June 19, 2013, 5:27 PM
 */
package EITLERP.FeltSales.FeltInvoiceParameterModificationF6;

/**
 *
 * @author RAJPALSINH JADEJA
 */
import EITLERP.AppletFrame;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.util.HashMap;
import java.net.URL;
import EITLERP.EITLComboModel;
import EITLERP.EITLTableModel;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLERPGLOBAL;
import EITLERP.BigEdit;
import EITLERP.clsUser;
import EITLERP.clsDepartment;
import EITLERP.clsHierarchy;
import EITLERP.clsAuthority;
import EITLERP.clsDocFlow;
import EITLERP.ComboData;
import EITLERP.FeltSales.FeltInvReport.clsFeltSalesInvoice;
import EITLERP.FeltSales.FeltPacking.clsFeltPacking;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsVoucher;
import EITLERP.Loader;
import EITLERP.frmPendingApprovals;
import EITLERP.clsSales_Party;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Production.ReportUI.JTextFieldHint;
import EITLERP.LOV;
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;
import EITLERP.data;
import EITLERP.frmSalesParty;
import TReportWriter.TReportEngine;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import javax.swing.JTextField;
import static EITLERP.FeltSales.FeltInvReport.frmFeltInvPro.OutstandingReport;
import EITLERP.Finance.ReportsUI.frmRptDebtorsOutStanding;
import java.sql.SQLException;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class frmFeltInvoiceParameterModificationf6Form extends javax.swing.JApplet {

    private clsFeltInvoiceParameterModificationf6Form ObjFeltReopenBale;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    private EITLTableCellRenderer Rend1=new EITLTableCellRenderer();
    
    private EITLTableCellRenderer RowFormat=new EITLTableCellRenderer();
    private EITLTableCellRenderer CellAlign=new EITLTableCellRenderer();
    private EITLTableCellRenderer ColumnColor=new EITLTableCellRenderer();
    private EITLTableCellRenderer POColor=new EITLTableCellRenderer();
    
    private EITLTableCellRenderer RowFormat1=new EITLTableCellRenderer();
    private EITLTableCellRenderer CellAlign1=new EITLTableCellRenderer();
    private EITLTableCellRenderer ColumnColor1=new EITLTableCellRenderer();
    private EITLTableCellRenderer POColor1=new EITLTableCellRenderer();
    
    private int EditMode = 0;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromUserId = 0;
    private int FFNo = 0; //First Free No.
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    private boolean DoNotEvaluate = false;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbSendToModel;

    private EITLTableModel DataModelPieceNo;
    private EITLTableModel DataModelBaleNo;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel = new EITLTableModel();
    private TReportEngine objEngine = new TReportEngine();

    private EITLERP.FeltSales.common.FeltInvCalc inv_calculation;
    public frmPendingApprovals frmPA;
    public HashMap hmFeltGroupMasterDetails;

    /**
     * Creates new form frmFeltProcessInvoiceVariable
     */
    public void init() {
        System.gc();
        setSize(1000, 700);
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

        ObjFeltReopenBale = new clsFeltInvoiceParameterModificationf6Form();
        SetMenuForRights();
        GenerateHierarchyCombo();
        GenerateSendToCombo();

        SetFields(false);
        FormatGrid();
        cmdPreview.setEnabled(true);
        cmdAdd.setEnabled(false);
        cmdRemove.setEnabled(false);

        if (ObjFeltReopenBale.LoadData()) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + ObjFeltReopenBale.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            cmdNextToTab3.setEnabled(true);
            cmdShowData.setEnabled(true);
            mnuShow.setEnabled(true);
            cmdSelectAll.setEnabled(true);
            cmdClearAll.setEnabled(true);
            jButton1.setEnabled(true);
        } else {
            cmdNextToTab3.setEnabled(false);
            cmdShowData.setEnabled(false);
            mnuShow.setEnabled(false);
            cmdSelectAll.setEnabled(false);
            cmdClearAll.setEnabled(false);
            jButton1.setEnabled(false);
        }
        FormatGridBaleTable();
        cmdAdd.setVisible(false);
        cmdRemove.setVisible(false);
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
        Tab4 = new javax.swing.JPanel();
        cmdNextToTab3 = new javax.swing.JButton();
        mnuShow = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        BaleSelectTable = new javax.swing.JTable();
        cmdShowData = new javax.swing.JButton();
        cmdSelectAll = new javax.swing.JButton();
        cmdClearAll = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        cmdSearch = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        Tab1 = new javax.swing.JPanel();
        cmdNextToTab1 = new javax.swing.JButton();
        mnuShow2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        BaleTable = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        cmdBackToTab2 = new javax.swing.JButton();
        cmdShowRemarks1 = new javax.swing.JButton();
        txtSearch1 = new javax.swing.JTextField();
        cmdSearch1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
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
        btnSendEmail = new javax.swing.JButton();
        Tab3 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableApprovalStatus = new javax.swing.JTable();
        lblDocumentHistory = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableUpdateHistory = new javax.swing.JTable();
        cmdBackToTab1 = new javax.swing.JButton();
        cmdBackToNormal = new javax.swing.JButton();
        cmdViewRevisions = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtdocno = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtprocessdate = new javax.swing.JTextField();

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
        ToolBar.setBounds(0, 0, 810, 40);

        lblTitle.setBackground(new java.awt.Color(178, 182, 185));
        lblTitle.setText("INVOICE PARAMETER MODIFICATION F6 ENTRY FORM-");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 810, 25);

        Tab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabStateChanged(evt);
            }
        });

        Tab4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab4FocusGained(evt);
            }
        });
        Tab4.setLayout(null);

        cmdNextToTab3.setMnemonic('N');
        cmdNextToTab3.setText("Next >>");
        cmdNextToTab3.setToolTipText("Next Tab");
        cmdNextToTab3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab3ActionPerformed(evt);
            }
        });
        Tab4.add(cmdNextToTab3);
        cmdNextToTab3.setBounds(180, 10, 90, 25);

        mnuShow.setText("Show Party ");
        mnuShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuShowActionPerformed(evt);
            }
        });
        Tab4.add(mnuShow);
        mnuShow.setBounds(20, 390, 110, 30);

        BaleSelectTable.setModel(new javax.swing.table.DefaultTableModel(
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
        BaleSelectTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BaleSelectTableKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(BaleSelectTable);

        Tab4.add(jScrollPane5);
        jScrollPane5.setBounds(20, 50, 750, 320);

        cmdShowData.setMnemonic('A');
        cmdShowData.setText("Show Data");
        cmdShowData.setToolTipText("Add Row");
        cmdShowData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowDataActionPerformed(evt);
            }
        });
        Tab4.add(cmdShowData);
        cmdShowData.setBounds(20, 10, 120, 30);

        cmdSelectAll.setText("Select All");
        cmdSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectAllActionPerformed(evt);
            }
        });
        Tab4.add(cmdSelectAll);
        cmdSelectAll.setBounds(150, 390, 106, 25);

        cmdClearAll.setText("Clear All");
        cmdClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearAllActionPerformed(evt);
            }
        });
        cmdClearAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdClearAllKeyPressed(evt);
            }
        });
        Tab4.add(cmdClearAll);
        cmdClearAll.setBounds(270, 390, 100, 25);

        jButton1.setText("Show OutStanding");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab4.add(jButton1);
        jButton1.setBounds(520, 390, 150, 30);

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });
        Tab4.add(txtSearch);
        txtSearch.setBounds(500, 10, 120, 19);

        cmdSearch.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cmdSearch.setText("Go");
        cmdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSearchActionPerformed(evt);
            }
        });
        Tab4.add(cmdSearch);
        cmdSearch.setBounds(624, 10, 48, 20);

        jLabel8.setText("Search");
        Tab4.add(jLabel8);
        jLabel8.setBounds(445, 13, 50, 15);

        Tab.addTab("BALE SELECTION", Tab4);

        Tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab1FocusGained(evt);
            }
        });
        Tab1.setLayout(null);

        cmdNextToTab1.setMnemonic('N');
        cmdNextToTab1.setText("Next >>");
        cmdNextToTab1.setToolTipText("Next Tab");
        cmdNextToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNextToTab1);
        cmdNextToTab1.setBounds(680, 390, 90, 25);

        mnuShow2.setText("Show Party ");
        mnuShow2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuShow2ActionPerformed(evt);
            }
        });
        Tab1.add(mnuShow2);
        mnuShow2.setBounds(20, 390, 90, 30);

        BaleTable.setModel(new javax.swing.table.DefaultTableModel(
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
        BaleTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BaleTableKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(BaleTable);

        Tab1.add(jScrollPane4);
        jScrollPane4.setBounds(20, 40, 750, 330);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setToolTipText("Add Row");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        Tab1.add(cmdAdd);
        cmdAdd.setBounds(340, 390, 80, 30);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setToolTipText("Remove Selected Row");
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        Tab1.add(cmdRemove);
        cmdRemove.setBounds(430, 390, 80, 30);

        cmdBackToTab2.setMnemonic('B');
        cmdBackToTab2.setText("<< Back");
        cmdBackToTab2.setToolTipText("Previous Tab");
        cmdBackToTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab2ActionPerformed(evt);
            }
        });
        Tab1.add(cmdBackToTab2);
        cmdBackToTab2.setBounds(580, 390, 100, 25);

        cmdShowRemarks1.setText("Show Remarks");
        cmdShowRemarks1.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdShowRemarks1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarks1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdShowRemarks1);
        cmdShowRemarks1.setBounds(630, 10, 130, 25);

        txtSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearch1KeyTyped(evt);
            }
        });
        Tab1.add(txtSearch1);
        txtSearch1.setBounds(420, 10, 120, 19);

        cmdSearch1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cmdSearch1.setText("Go");
        cmdSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSearch1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdSearch1);
        cmdSearch1.setBounds(550, 10, 50, 20);

        jLabel9.setText("Search");
        Tab1.add(jLabel9);
        jLabel9.setBounds(360, 10, 50, 15);

        Tab.addTab("BALE SELECTED", Tab1);

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab2FocusGained(evt);
            }
        });
        Tab2.setLayout(null);

        jLabel31.setText("Hierarchy ");
        Tab2.add(jLabel31);
        jLabel31.setBounds(7, 13, 62, 15);

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
        cmbHierarchy.setBounds(86, 10, 230, 24);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(7, 42, 33, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        txtFrom.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtFrom.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFrom);
        txtFrom.setBounds(86, 40, 230, 21);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(7, 72, 61, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setEnabled(false);
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(86, 70, 500, 19);

        jLabel36.setText("Your Action");
        Tab2.add(jLabel36);
        jLabel36.setBounds(7, 100, 73, 15);

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
        OpgApprove.setBounds(6, 6, 150, 23);

        buttonGroup1.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
        OpgFinal.setNextFocusableComponent(OpgReject);
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });
        OpgFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgFinalFocusGained(evt);
            }
        });
        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 120, 20);

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
        OpgReject.setBounds(6, 54, 70, 20);

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
        OpgHold.setBounds(6, 76, 130, 20);

        Tab2.add(jPanel6);
        jPanel6.setBounds(86, 100, 160, 100);

        jLabel33.setText("Send To");
        Tab2.add(jLabel33);
        jLabel33.setBounds(7, 212, 50, 15);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(86, 210, 230, 24);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(7, 242, 60, 15);

        txtToRemarks.setEnabled(false);
        txtToRemarks.setNextFocusableComponent(cmdBackToTab0);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(86, 240, 490, 19);

        cmdBackToTab0.setMnemonic('B');
        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.setToolTipText("Previous Tab");
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(410, 280, 100, 25);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(610, 70, 49, 21);

        cmdNextToTab2.setMnemonic('N');
        cmdNextToTab2.setText("Next >>");
        cmdNextToTab2.setToolTipText("Next Tab");
        cmdNextToTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNextToTab2);
        cmdNextToTab2.setBounds(540, 280, 100, 25);

        btnSendEmail.setText("Send Email Notification");
        btnSendEmail.setEnabled(false);
        btnSendEmail.setMargin(new java.awt.Insets(-4, -4, -4, -4));
        btnSendEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendEmailActionPerformed(evt);
            }
        });
        Tab2.add(btnSendEmail);
        btnSendEmail.setBounds(560, 10, 180, 13);

        Tab.addTab("Approval", Tab2);

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.setLayout(null);

        jLabel26.setText("Document Approval Status");
        Tab3.add(jLabel26);
        jLabel26.setBounds(8, 5, 180, 15);

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
        TableApprovalStatus.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(TableApprovalStatus);

        Tab3.add(jScrollPane2);
        jScrollPane2.setBounds(12, 24, 600, 100);

        lblDocumentHistory.setText("Document Update History");
        Tab3.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(8, 132, 163, 15);

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
        TableUpdateHistory.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane3.setViewportView(TableUpdateHistory);

        Tab3.add(jScrollPane3);
        jScrollPane3.setBounds(10, 150, 510, 150);

        cmdBackToTab1.setMnemonic('B');
        cmdBackToTab1.setText("<< Back");
        cmdBackToTab1.setToolTipText("Previous Tab");
        cmdBackToTab1.setIconTextGap(0);
        cmdBackToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab1ActionPerformed(evt);
            }
        });
        Tab3.add(cmdBackToTab1);
        cmdBackToTab1.setBounds(530, 250, 110, 25);

        cmdBackToNormal.setText("Back To Normal");
        cmdBackToNormal.setMargin(new java.awt.Insets(2, 3, 2, 3));
        cmdBackToNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToNormalActionPerformed(evt);
            }
        });
        Tab3.add(cmdBackToNormal);
        cmdBackToNormal.setBounds(530, 180, 110, 25);

        cmdViewRevisions.setText("View Revisions");
        cmdViewRevisions.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdViewRevisions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewRevisionsActionPerformed(evt);
            }
        });
        Tab3.add(cmdViewRevisions);
        cmdViewRevisions.setBounds(530, 150, 110, 25);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        Tab3.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(530, 210, 110, 25);

        Tab.addTab("Status", Tab3);

        getContentPane().add(Tab);
        Tab.setBounds(2, 116, 810, 480);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(10, 610, 740, 22);

        jLabel2.setText("Doc No");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(40, 80, 50, 15);

        txtdocno.setEditable(false);
        txtdocno.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        txtdocno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdocnoActionPerformed(evt);
            }
        });
        getContentPane().add(txtdocno);
        txtdocno.setBounds(100, 70, 130, 21);

        lblRevNo.setText(".....");
        getContentPane().add(lblRevNo);
        lblRevNo.setBounds(240, 80, 30, 15);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Doc Date");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(280, 80, 100, 15);
        getContentPane().add(txtDocDate);
        txtDocDate.setBounds(390, 70, 130, 30);

        jLabel5.setText("Process Up To Date");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel5);
        jLabel5.setBounds(530, 80, 140, 15);

        txtprocessdate.setEditable(false);
        txtprocessdate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtprocessdateFocusLost(evt);
            }
        });
        getContentPane().add(txtprocessdate);
        txtprocessdate.setBounds(680, 70, 130, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed

    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        Report();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        if (TableUpdateHistory.getRowCount() > 0 && TableUpdateHistory.getSelectedRow() >= 0) {
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText.setText(TableUpdateHistory.getValueAt(TableUpdateHistory.getSelectedRow(), 4).toString());
            bigEdit.ShowEdit();
        } else {
            JOptionPane.showMessageDialog(this, "Select a row from Document Update History", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdBackToNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToNormalActionPerformed
        ObjFeltReopenBale.HistoryView = false;
        ObjFeltReopenBale.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdBackToNormalActionPerformed

    private void cmdViewRevisionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewRevisionsActionPerformed
        ObjFeltReopenBale.ShowHistory(EITLERPGLOBAL.formatDateDB(txtDocDate.getText()), txtdocno.getText());
        MoveLast();
    }//GEN-LAST:event_cmdViewRevisionsActionPerformed

    private void cmdBackToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab1ActionPerformed
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdBackToTab1ActionPerformed

    private void cmdNextToTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab2ActionPerformed
        Tab.setSelectedIndex(3);
    }//GEN-LAST:event_cmdNextToTab2ActionPerformed

    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtFromRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed

    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained
        cmbHierarchy.requestFocus();
    }//GEN-LAST:event_Tab2FocusGained

    private void cmdBackToTab0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab0ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBackToTab0ActionPerformed

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained
        lblStatus.setText("Enter the remarks for next user");
    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        lblStatus.setText("Select the user to whom document to be forwarded");
    }//GEN-LAST:event_cmbSendToFocusGained

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgHoldFocusGained

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgRejectFocusGained

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        lblStatus.setText("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        OpgHold.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgReject.setSelected(false);

        cmbSendTo.setEnabled(false);
    }//GEN-LAST:event_OpgHoldMouseClicked

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        OpgFinal.setSelected(true);
        OpgReject.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedSendToCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString())) {
                cmbSendTo.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }

        if (cmbSendTo.getItemCount() <= 0) {
            GenerateSendToCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        ObjFeltReopenBale.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Delete();
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

    private void Tab1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab1FocusGained

    }//GEN-LAST:event_Tab1FocusGained

    private void cmdNextToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab1ActionPerformed
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNextToTab1ActionPerformed

    private void txtdocnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdocnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdocnoActionPerformed

    private void txtprocessdateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtprocessdateFocusLost
        try {
            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
            java.util.Date docdt, procdt;
            docdt = df.parse(txtDocDate.getText());
            procdt = df.parse(txtprocessdate.getText());
            java.sql.Date ddt = new java.sql.Date(docdt.getTime());
            java.sql.Date pdt = new java.sql.Date(procdt.getTime());
            if (EITLERPGLOBAL.DateDiff(pdt, ddt) > 0) {
                JOptionPane.showMessageDialog(null, "Please Process Date is not less then Current Date");
                txtprocessdate.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtprocessdateFocusLost

    private void mnuShow2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuShow2ActionPerformed
        //if ((!txtPartyCode.getText().equals(""))) {
            String DocNo=BaleTable.getValueAt(BaleTable.getSelectedRow(), 1).toString();
            //String DocNo = txtPartyCode.getText().trim();
            AppletFrame aFrame = new AppletFrame("Party Master");
            aFrame.startAppletEx("EITLERP.frmSalesParty", "Party Master");
            frmSalesParty ObjSalesParty = (frmSalesParty) aFrame.ObjApplet;
            ObjSalesParty.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo, "210010");
        //}
    }//GEN-LAST:event_mnuShow2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        AppletFrame aFrame = new AppletFrame("Debtors OutStanding");
        aFrame.startAppletEx("EITLERP.Finance.ReportsUI.frmRptDebtorsOutStanding", "Debtors OutStanding");
        frmRptDebtorsOutStanding ObjSalesParty = (frmRptDebtorsOutStanding) aFrame.ObjApplet;

//        
//        if (!txtchargecodeold1.getText().equals("09") && !txtchargecodeold1.getText().equals("01")) {
//            data.Execute("TRUNCATE TEMP_DATABASE.TEMP_PARA_OUTSTANDING_REPORT");
//            data.Execute("INSERT INTO TEMP_DATABASE.TEMP_PARA_OUTSTANDING_REPORT (MAIN_PARTY_CODE,SUB_PARTY_CODE) VALUES ('" + txtPartyCode.getText() + "','" + txtPartyCode.getText() + "')");
//            double MainBalance = EITLERPGLOBAL.round(BalanceTransfer1("210010", txtPartyCode.getText()), 2);
//            data.Execute("UPDATE TEMP_DATABASE.TEMP_PARA_OUTSTANDING_REPORT SET SUB_OUTSTANDING_BAL=" + MainBalance + " WHERE MAIN_PARTY_CODE='" + txtPartyCode.getText() + "' ");
//            OutstandingReport();
//        } else {
//            JOptionPane.showMessageDialog(null, "Outstanding Not Required as ChargeCode in ('01','09')");
//        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
               
        /*
        Object[] rowData = new Object[34];
        rowData[0] = Integer.toString(BaleTable.getRowCount() + 1);
        rowData[1] = "";
        rowData[2] = "";
        rowData[3] = "";
        rowData[4] = "";
        rowData[5] = "";
        rowData[6] = "";

        DataModelPieceNo.addRow(rowData);
        BaleTable.changeSelection(BaleTable.getRowCount() - 1, 1, false, false);
        BaleTable.requestFocus();
        
        SelectParty ObjMR = new SelectParty();
        */
        FormatGrid();
        GenerateGrid1();
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        if (BaleTable.getRowCount() > 0) {
            DataModelPieceNo.removeRow(BaleTable.getSelectedRow());
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void BaleTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BaleTableKeyPressed
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            if (evt.getKeyCode() == 112) //F1 Key pressed
            {
            //EITLERP.FeltSales.FeltProcessInvoiceVariable.PKGLOV aList = new EITLERP.FeltSales.FeltProcessInvoiceVariable.PKGLOV();
                // aList.SQL="SELECT PKG_BALE_NO,PKG_PARTY_CODE,PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST_DETAIL) AND BALE_REOPEN_FLG=0";
                // aList.SQL="SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST)  AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 GROUP BY B.PKG_BALE_NO ORDER BY A.PKG_BALE_NO"; 
                LOV aList = new LOV();
//                aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE  CANCELED=0 ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO UNION ALL SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=1 AND PROCESSING_DATE>=CURDATE()) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO ORDER BY PKG_BALE_NO";
//                aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=0 AND PROCESSING_DATE>=CURDATE() ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO UNION ALL SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=1 AND PROCESSING_DATE>=CURDATE()) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO ORDER BY PKG_BALE_NO";
                aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=0 AND PROCESSING_DATE>=CURDATE() ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO\n"
                        + " UNION ALL \n"
                        + " SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=1 AND PROCESSING_DATE>=CURDATE()) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO ORDER BY PKG_PARTY_CODE,PKG_BALE_NO";

            //aList.SQL = "SELECT NULL,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE  CANCELED=0 ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 GROUP BY B.PKG_BALE_NO UNION ALL SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=1 AND PROCESSING_DATE>=CURDATE()) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 GROUP BY B.PKG_BALE_NO ORDER BY PKG_BALE_NO";

                //aList.SQL = "SELECT NULL,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE  CANCELED=0 ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "'GROUP BY B.PKG_BALE_NO UNION ALL SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=1 AND DOC_DATE>=CURDATE()) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 GROUP BY B.PKG_BALE_NO ORDER BY PKG_BALE_NO";
                aList.ReturnCol = 2;
                aList.SecondCol=3;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 3;

                if (aList.ShowLOV()) {
                    String str = aList.ReturnVal;
                    //str = str.substring(1, str.length());
                    BaleTable.setValueAt(aList.SecondVal, BaleTable.getSelectedRow(), 1);
                    BaleTable.setValueAt(clsFeltInvoiceParameterModificationf6Form.getParyName(EITLERPGLOBAL.gCompanyID, aList.SecondVal), BaleTable.getSelectedRow(), 2);
                    BaleTable.setValueAt(aList.ReturnVal, BaleTable.getSelectedRow(), 3);
                    String BaleDate = EITLERPGLOBAL.formatDate(clsFeltInvoiceParameterModificationf6Form.getBaleDate(aList.ReturnVal));
                    BaleTable.setValueAt(BaleDate, BaleTable.getSelectedRow(), 4);

                    String PieceNO = data.getStringValueFromDB("SELECT PKG_PIECE_NO FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String ProductCode = data.getStringValueFromDB("SELECT PKG_PRODUCT_CODE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    //String Partycode = txtPartyCode.getText();//txtPartyCode.setText(Partycode);
                    String Length = data.getStringValueFromDB("SELECT PKG_LENGTH FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String Width = data.getStringValueFromDB("SELECT PKG_WIDTH FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String Weight = data.getStringValueFromDB("SELECT PKG_WEIGHT FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String Sqmtr = data.getStringValueFromDB("SELECT PKG_SQM FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
//                String Orderdate = data.getStringValueFromDB("SELECT PKG_ORDER_DATE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "'");
                    String Orderdate = EITLERPGLOBAL.getCurrentDateDB();
                    //lblPartyName.setText(clsFeltInvoiceParameterModificationf6Form.getParyName(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));
//                    txtchargecodeold1.setText(clsFeltInvoiceParameterModificationf6Form.getChargeCode(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));


                    inv_calculation = EITLERP.FeltSales.common.clsOrderValueCalc.calculate(PieceNO, ProductCode, aList.SecondVal, Float.parseFloat(Length), Float.parseFloat(Width), Float.parseFloat(Weight), Float.parseFloat(Sqmtr), Orderdate);
                    Float billvalue = inv_calculation.getFicInvAmt();
                    //txtBillValue.setText(billvalue.toString());
                    BaleTable.setValueAt(billvalue, BaleTable.getSelectedRow(), 5);
                    BaleTable.setValueAt(PieceNO, BaleTable.getSelectedRow(), 6);
                }

            }

        }// TODO add your handling code here:
    }//GEN-LAST:event_BaleTableKeyPressed

    private void btnSendEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendEmailActionPerformed
         System.out.println("Parameter Modification approved = "+ObjFeltReopenBale.getAttribute("APPROVED").getInt());

        if(ObjFeltReopenBale.getAttribute("APPROVED").getInt() == 1)
        {
            int value = JOptionPane.showConfirmDialog(this, " Are you sure? You want to send Final Approved mail to all users? ","Confirmation Alert!",JOptionPane.YES_NO_OPTION);
            System.out.println("VALUE = "+value);
            if(value==0)
            {
                try{
                    String DOC_NO = txtdocno.getText();
                    String DOC_DATE = txtDocDate.getText();
                    //String Party_Code = txtPartyCode.getText();

                    String responce = JavaMail.sendNotificationMailOfDetail(clsFeltInvoiceParameterModificationf6Form.ModuleID, DOC_NO, DOC_DATE, "", EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
                    System.out.println("Send Mail Responce : " + responce);

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_btnSendEmailActionPerformed

    private void cmdClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearAllActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<BaleSelectTable.getRowCount();i++)
        {
            DataModelBaleNo.setValueAt(Boolean.valueOf(false), i, 1);
        }
    }//GEN-LAST:event_cmdClearAllActionPerformed

    private void cmdClearAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdClearAllKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdClearAllKeyPressed

    private void cmdNextToTab3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab3ActionPerformed
        Tab.setSelectedIndex(1);
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            //if(EditMode == EITLERPGLOBAL.ADD){
              FormatGrid();
            //}
            int tablerow=BaleTable.getRowCount();
            
        for (int i =tablerow-1 ; i >= ObjFeltReopenBale.hmFeltBaleNoDetails.size(); i--) {        
            DataModelPieceNo.removeRow(i);
        }    
        GenerateGrid1();
        }
    }//GEN-LAST:event_cmdNextToTab3ActionPerformed

    private void mnuShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuShowActionPerformed
        //if ((!txtPartyCode.getText().equals(""))) {
            String DocNo=BaleSelectTable.getValueAt(BaleSelectTable.getSelectedRow(), 2).toString();
            //String DocNo = txtPartyCode.getText().trim();
            AppletFrame aFrame = new AppletFrame("Party Master");
            aFrame.startAppletEx("EITLERP.frmSalesParty", "Party Master");
            frmSalesParty ObjSalesParty = (frmSalesParty) aFrame.ObjApplet;
            ObjSalesParty.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo, "210010");
        //}
    }//GEN-LAST:event_mnuShowActionPerformed

    private void BaleSelectTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BaleSelectTableKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BaleSelectTableKeyPressed

    private void cmdShowDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowDataActionPerformed
     FormatGridBaleTable();
     GenerateGrid();
     
     for (int i = 0; i < BaleTable.getRowCount(); i++) {            
         String partyCode=(String) BaleTable.getValueAt(i, 2);
         String baleNo=(String) BaleTable.getValueAt(i, 4);
         String baleDate=(String) BaleTable.getValueAt(i,5);
         //String PieceNo = data.getStringValueFromDB("SELECT PKG_PIECE_NO FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + baleNo + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(baleDate) + "'");
         String pieceNo = (String) BaleTable.getValueAt(i,7);;
         System.out.println(pieceNo);
         for (int j = 0; j < BaleSelectTable.getRowCount(); j++) {            
             if(BaleSelectTable.getValueAt(j, 2).toString().equals(partyCode) && BaleSelectTable.getValueAt(j, 4).toString().equals(baleNo) && BaleSelectTable.getValueAt(j, 7).toString().equals(pieceNo)){
                   BaleSelectTable.setValueAt(true, j, 1);
             }             
         }
            
        }

    }//GEN-LAST:event_cmdShowDataActionPerformed

    private void Tab4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab4FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_Tab4FocusGained

    private void cmdSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectAllActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<BaleSelectTable.getRowCount();i++)
        {
            DataModelBaleNo.setValueAt(Boolean.valueOf(true), i, 1);
        }
    }//GEN-LAST:event_cmdSelectAllActionPerformed

    private void TabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabStateChanged
        /*
        if (Tab.getTitleAt(Tab.getSelectedIndex()).equals("BALE SELECTED")) {
            FormatGrid();
            GenerateGrid1();
        } else if (Tab.getTitleAt(Tab.getSelectedIndex()).equals("BALE SELECTION")) {
            
        } else {

        }
        */
    }//GEN-LAST:event_TabStateChanged

    private void cmdBackToTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab2ActionPerformed
        Tab.setSelectedIndex(0);
    }//GEN-LAST:event_cmdBackToTab2ActionPerformed

    private void cmdShowRemarks1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarks1ActionPerformed
        if (BaleTable.getRowCount() > 0 && BaleTable.getSelectedRow() >= 0) {
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText.setText(BaleTable.getValueAt(BaleTable.getSelectedRow(), 8).toString());
            bigEdit.ShowEdit();
        } else {
            JOptionPane.showMessageDialog(this, "Select a row from Bale Selected", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdShowRemarks1ActionPerformed

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        // TODO add your handling code here:
        try {
            //searchWithin(evt.getKeyChar());

        }
        catch(Exception e) {

        }
    }//GEN-LAST:event_txtSearchKeyTyped

    private void cmdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSearchActionPerformed
        // TODO add your handling code here:
        searchWithin(' ');
    }//GEN-LAST:event_cmdSearchActionPerformed

    private void txtSearch1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearch1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearch1KeyTyped

    private void cmdSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSearch1ActionPerformed
        searchWithin1(' ');
    }//GEN-LAST:event_cmdSearch1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable BaleSelectTable;
    private javax.swing.JTable BaleTable;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab3;
    private javax.swing.JPanel Tab4;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnSendEmail;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBackToNormal;
    private javax.swing.JButton cmdBackToTab0;
    private javax.swing.JButton cmdBackToTab1;
    private javax.swing.JButton cmdBackToTab2;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClearAll;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFromRemarksBig;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNextToTab1;
    private javax.swing.JButton cmdNextToTab2;
    private javax.swing.JButton cmdNextToTab3;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdSearch;
    private javax.swing.JButton cmdSearch1;
    private javax.swing.JButton cmdSelectAll;
    private javax.swing.JButton cmdShowData;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdShowRemarks1;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewRevisions;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JButton mnuShow;
    private javax.swing.JButton mnuShow2;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearch1;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtdocno;
    private javax.swing.JTextField txtprocessdate;
    // End of variables declaration//GEN-END:variables

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

        txtdocno.setEditable(pStat);
        txtDocDate.setEnabled(pStat);

        //txtPartyCode.setEnabled(pStat);
        txtprocessdate.setEditable(pStat);
        
        
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

//        txtprocessdate.setText("");
        txtprocessdate.setText(EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT CURDATE() + INTERVAL '7' DAY FROM DUAL")));
        txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());
        

        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        

        
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();

    }

    private void DisplayData() {

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, 754)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //=========== Title Bar Color Indication ===============//
        try {
            btnSendEmail.setEnabled(false);
            
            if (EditMode == 0) {
                if (ObjFeltReopenBale.getAttribute("APPROVED").getInt() == 1) {
                    lblTitle.setBackground(Color.BLUE);
                    btnSendEmail.setEnabled(true);                    
                } else {
                    lblTitle.setBackground(Color.GRAY);
                }

                if (ObjFeltReopenBale.getAttribute("CANCELED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
            //============================================//

            String DOCNO = ObjFeltReopenBale.getAttribute("DOC_NO").getString();
            lblTitle.setText(" INVOICE PARAMETER MODIFICATION F6 ENTRY FORM - " + DOCNO);
            lblRevNo.setText(Integer.toString((int) ObjFeltReopenBale.getAttribute("REVISION_NO").getVal()));
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjFeltReopenBale.getAttribute("HIERARCHY_ID").getInt());
            DoNotEvaluate = true;

            txtdocno.setText(DOCNO);
            txtDocDate.setText(EITLERPGLOBAL.formatDate(ObjFeltReopenBale.getAttribute("DOC_DATE").getString()));
            txtprocessdate.setText(EITLERPGLOBAL.formatDate(ObjFeltReopenBale.getAttribute("PROCESSING_DATE").getString()));
           // txtbaleno.setText(ObjFeltReopenBale.getAttribute("BALE_NO").getString());
            //  String blNo = data.getStringValueFromDB("SELECT GROUP_CONCAT(BALE_NO) FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO='" + DOCNO + "'");
//            txtbaleno.setText(ObjFeltReopenBale.getAttribute("BALE_NO").getString());
            //  txtbaleno.setText(blNo);
            //txtPartyCode.setText(ObjFeltReopenBale.getAttribute("PARTY_CODE").getString());
            //lblPartyName.setText(ObjFeltReopenBale.getAttribute("PARTY_NAME").getString());
            
            //txtBillValue.setText(ObjFeltReopenBale.getAttribute("BILL_VALUE").getString());
            FormatGrid();
            for (int i = 1; i <= ObjFeltReopenBale.hmFeltBaleNoDetails.size(); i++) {
                clsFeltInvoiceParameterModificationf6FormDetail ObjFeltReopenBaleDetails = (clsFeltInvoiceParameterModificationf6FormDetail) ObjFeltReopenBale.hmFeltBaleNoDetails.get(Integer.toString(i));

                Object[] rowData = new Object[10];
                rowData[0] = Integer.toString(i);
                rowData[1] = ObjFeltReopenBaleDetails.getAttribute("F6").getBool();
                rowData[2] = ObjFeltReopenBaleDetails.getAttribute("PARTY_CODE").getString();
                rowData[3] = ObjFeltReopenBaleDetails.getAttribute("PARTY_NAME").getString();
                rowData[4] = ObjFeltReopenBaleDetails.getAttribute("BALE_NO").getString();
                rowData[5] = EITLERPGLOBAL.formatDate(ObjFeltReopenBaleDetails.getAttribute("BALE_DATE").getString());
                rowData[6] = Double.toString(EITLERPGLOBAL.round(ObjFeltReopenBaleDetails.getAttribute("BILL_VALUE").getVal(),2));
                rowData[7] = ObjFeltReopenBaleDetails.getAttribute("REMARKS").getString();
                
                String PieceNo = data.getStringValueFromDB("SELECT PKG_PIECE_NO FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + ObjFeltReopenBaleDetails.getAttribute("BALE_NO").getString() + "' AND PKG_BALE_DATE='" + ObjFeltReopenBaleDetails.getAttribute("BALE_DATE").getString() + "'");
                rowData[7] = PieceNo;
                DataModelPieceNo.addRow(rowData);
            }
//======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap hmList = new HashMap();

            hmList = clsFeltProductionApprovalFlow.getDocumentFlow(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString());
            for (int i = 1; i <= hmList.size(); i++) {
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
            FormatGridUpdateHistory();
            HashMap hmApprovalHistory = clsFeltInvoiceParameterModificationf6Form.getHistoryList(ObjFeltReopenBale.getAttribute("DOC_DATE").getString(), DOCNO);
            for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                clsFeltInvoiceParameterModificationf6Form ObjFeltReopenBale = (clsFeltInvoiceParameterModificationf6Form) hmApprovalHistory.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjFeltReopenBale.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(2, (int) ObjFeltReopenBale.getAttribute("UPDATED_BY").getVal());
                rowData[2] = ObjFeltReopenBale.getAttribute("ENTRY_DATE").getString();

                String ApprovalStatus = "";

                if ((ObjFeltReopenBale.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }

                if ((ObjFeltReopenBale.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if ((ObjFeltReopenBale.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }

                if ((ObjFeltReopenBale.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if ((ObjFeltReopenBale.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if ((ObjFeltReopenBale.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if ((ObjFeltReopenBale.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                rowData[3] = ApprovalStatus;
                rowData[4] = ObjFeltReopenBale.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjFeltReopenBale.getAttribute("FROM_IP").getString();

                DataModelUpdateHistory.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DoNotEvaluate = false;
    }

    //Generates Hierarchy Combo Box
    private void GenerateHierarchyCombo() {
        HashMap hmHierarchyList = new HashMap();

        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        hmHierarchyList = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=754 ");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            hmHierarchyList = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=754 ");
        }
        for (int i = 1; i <= hmHierarchyList.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
    }

    private void SetupApproval() {
        OpgHold.setSelected(true);
        if (EditMode == EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
            OpgReject.setEnabled(false);
        } else {
            cmbHierarchy.setEnabled(false);
        }

        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);

        if (EditMode == EITLERPGLOBAL.ADD) {
            lnFromUserId = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {
            int FromUserID = clsFeltProductionApprovalFlow.getFromID(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString());
            lnFromUserId = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(754, FromUserID, ObjFeltReopenBale.getAttribute("DOC_NO").getString());

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

        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString())) {
                OpgReject.setEnabled(false);
            }
        }

        if (EditMode == 0) {
            cmbHierarchy.setEnabled(false);
            txtFrom.setEnabled(false);
            txtFromRemarks.setEnabled(false);
            OpgApprove.setEnabled(false);
            OpgFinal.setEnabled(false);
            OpgReject.setEnabled(false);
            cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }

        if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
            OpgReject.setEnabled(false);
        }
        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
            //JOptionPane.showMessageDialog(null, "Final Approver");
            OpgApprove.setEnabled(false);
        }
    }

    private void SetMenuForRights() {
        // --- Add Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6060, 60601)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6060, 60602)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6060, 60603)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6060, 60604)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }

    private void Add() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(this, "The year is closed. You cannot enter/edit any transaction", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //----------------------------------//
        EITLERP.SelectFirstFree aList = new EITLERP.SelectFirstFree();
        aList.ModuleID = 754;
        aList.FirstFreeNo = 390; //217
        FFNo = aList.FirstFreeNo;
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        FFNo = aList.FirstFreeNo;
        SetupApproval();
        txtdocno.setText(EITLERP.clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 754, FFNo, false));
        lblTitle.setText("INVOICE PARAMETER MODIFICATION ENTRY FORM - " + txtdocno.getText());
        lblTitle.setBackground(Color.GRAY);
        txtDocDate.setEditable(false);
        txtprocessdate.setEditable(true);
        //txtPartyCode.setEditable(false);
        txtdocno.setEditable(false);
        cmdAdd.setEnabled(true);
        cmdRemove.setEnabled(true);
        FormatGrid();

        cmdNextToTab3.setEnabled(true);
        cmdShowData.setEnabled(true);
        mnuShow.setEnabled(true);
        cmdSelectAll.setEnabled(true);
        cmdClearAll.setEnabled(true);
        jButton1.setEnabled(true);


    }

    private void Edit() {
        if (ObjFeltReopenBale.IsEditable(txtdocno.getText(), ObjFeltReopenBale.getAttribute("DOC_DATE").getString(), EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            if (clsFeltProductionApprovalFlow.IsCreator(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString())) {
                SetFields(true);

                cmdNextToTab3.setEnabled(true);
                cmdShowData.setEnabled(true);
                mnuShow.setEnabled(true);
                cmdSelectAll.setEnabled(true);
                cmdClearAll.setEnabled(true);
                jButton1.setEnabled(true);

            } else {
                EnableApproval();
            }
        } else {
            JOptionPane.showMessageDialog(this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void Delete() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(this, "The year is closed. You cannot enter/edit any transaction", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //----------------------------------//
    }

    private void Save() {

        if(BaleTable.getRowCount()==0){
            JOptionPane.showMessageDialog(frmFeltInvoiceParameterModificationf6Form.this, "Please select atleast one bale from BALE SELECTION !!");
            return;
        }
        
        if (txtprocessdate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(frmFeltInvoiceParameterModificationf6Form.this, "Please enter Processing Upto Date.");
            return;
        }

        if (!EITLERPGLOBAL.isDate(txtprocessdate.getText())) {
            JOptionPane.showMessageDialog(frmFeltInvoiceParameterModificationf6Form.this, "Please enter currect date format dd/MM/yyyy.");
            return;
        }

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(frmFeltInvoiceParameterModificationf6Form.this, "Select the hierarchy.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(frmFeltInvoiceParameterModificationf6Form.this, "Select the Approval Action.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(frmFeltInvoiceParameterModificationf6Form.this, "Enter the remarks for rejection", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(frmFeltInvoiceParameterModificationf6Form.this, "Select the user, to whom rejected document to be send", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (EditMode == EITLERPGLOBAL.ADD) {
            
        }

        SetData();

        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjFeltReopenBale.Insert()) {

                /*if (OpgFinal.isSelected()) {
                    try {
                        String DOC_NO = txtdocno.getText();
                        String DOC_DATE = txtDocDate.getText();
                        String Party_Code = txtPartyCode.getText();

                        String responce = JavaMail.sendFinalApprovalMail(clsFeltInvoiceParameterModificationf6Form.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                        System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/

                DisplayData();
                
                String query="SELECT USER_ID FROM D_COM_HIERARCHY H,D_COM_HIERARCHY_RIGHTS HR WHERE H.HIERARCHY_ID=HR.HIERARCHY_ID AND MODULE_ID="+clsFeltInvoiceParameterModificationf6Form.ModuleID+" AND HR.HIERARCHY_ID="+EITLERPGLOBAL.getComboCode(cmbHierarchy)+" AND SR_NO=2";
                int OperationIncharge=data.getIntValueFromDB(query);                
                //if(AreaIncharge==EITLERPGLOBAL.gNewUserID){
                //if(OpgApprove.isSelected()) {                    
                if((OperationIncharge==EITLERPGLOBAL.gNewUserID) && (OpgApprove.isSelected())){
                    String DOC_NO = txtdocno.getText();
                        String DOC_DATE = txtDocDate.getText();
                        //String Party_Code = txtPartyCode.getText();

                    String responce = JavaMail.sendNotificationMailOfDetail(clsFeltInvoiceParameterModificationf6Form.ModuleID, DOC_NO, DOC_DATE, "", EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                    System.out.println("Send Mail Responce : " + responce);
                }
                //}
                //}
                
                
                if(OpgFinal.isSelected()){
                    String DOC_NO = txtdocno.getText();
                    String DOC_DATE = txtDocDate.getText();
                    //String Party_Code = txtPartyCode.getText();

                    String responce = JavaMail.sendNotificationMailOfDetail(clsFeltInvoiceParameterModificationf6Form.ModuleID, DOC_NO, DOC_DATE, "", EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
                    System.out.println("Send Mail Responce : " + responce);
                }
                
                
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + ObjFeltReopenBale.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjFeltReopenBale.Update()) {

                /*if (OpgFinal.isSelected()) {                    
                    try {
                        String DOC_NO = txtdocno.getText();
                        String DOC_DATE = txtDocDate.getText();
                        String Party_Code = txtPartyCode.getText();

                        String responce = JavaMail.sendFinalApprovalMail(clsFeltInvoiceParameterModificationf6Form.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                        System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/                

                DisplayData();
                String query="SELECT USER_ID FROM D_COM_HIERARCHY H,D_COM_HIERARCHY_RIGHTS HR WHERE H.HIERARCHY_ID=HR.HIERARCHY_ID AND MODULE_ID="+clsFeltInvoiceParameterModificationf6Form.ModuleID+" AND HR.HIERARCHY_ID="+EITLERPGLOBAL.getComboCode(cmbHierarchy)+" AND SR_NO=2";
                int OperationIncharge=data.getIntValueFromDB(query);                
                //if(AreaIncharge==EITLERPGLOBAL.gNewUserID){
                //if(OpgApprove.isSelected()) {                    
                if((OperationIncharge==EITLERPGLOBAL.gNewUserID) && (OpgApprove.isSelected())){
                    String DOC_NO = txtdocno.getText();
                        String DOC_DATE = txtDocDate.getText();
                       // String Party_Code = txtPartyCode.getText();

                    String responce = JavaMail.sendNotificationMailOfDetail(clsFeltInvoiceParameterModificationf6Form.ModuleID, DOC_NO, DOC_DATE, "", EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                    System.out.println("Send Mail Responce : " + responce);
                }
                //}
                //}                
                
                if(OpgFinal.isSelected()){
                    String DOC_NO = txtdocno.getText();
                    String DOC_DATE = txtDocDate.getText();
                    //String Party_Code = txtPartyCode.getText();

                    String responce = JavaMail.sendNotificationMailOfDetail(clsFeltInvoiceParameterModificationf6Form.ModuleID, DOC_NO, DOC_DATE, "", EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
                    System.out.println("Send Mail Responce : " + responce);
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving editing. Error is " + ObjFeltReopenBale.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        EditMode = 0;
        SetFields(false);
        txtToRemarks.setText("");
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
        cmdNextToTab3.setEnabled(false);
            cmdShowData.setEnabled(false);
            mnuShow.setEnabled(false);
            cmdSelectAll.setEnabled(false);
            cmdClearAll.setEnabled(false);
            jButton1.setEnabled(false);
        
    }

    //Sets data to the Details Class Object
    private void SetData() {

        ObjFeltReopenBale.setAttribute("FFNO", FFNo);
        ObjFeltReopenBale.setAttribute("DOC_NO", txtdocno.getText());
        ObjFeltReopenBale.setAttribute("DOC_DATE", EITLERPGLOBAL.formatDateDB(txtDocDate.getText()));
        ObjFeltReopenBale.setAttribute("PROCESSING_DATE", EITLERPGLOBAL.formatDateDB(txtprocessdate.getText()));
        //ObjFeltReopenBale.setAttribute("PARTY_CODE", txtPartyCode.getText());
        //ObjFeltReopenBale.setAttribute("PARTY_NAME", lblPartyName.getText());
        ObjFeltReopenBale.setAttribute("CHARGE_CODE_NEW", "01");
        try {
            ObjFeltReopenBale.setAttribute("CRITICAL_LIMIT_NEW", 0.00);
        } catch (Exception e) {
        }
        ObjFeltReopenBale.setAttribute("INSURANCE_CODE", 0);
        ObjFeltReopenBale.setAttribute("TRANSPORTER_CODE", "");
        ObjFeltReopenBale.setAttribute("WITHOUT_CRITICAL_LIMIT", 0);
        //ObjFeltReopenBale.setAttribute("F6", 1);
        ObjFeltReopenBale.setAttribute("REMARKS", "");
        ObjFeltReopenBale.setAttribute("VEHICLE_NO", "");        
        ObjFeltReopenBale.setAttribute("ADV_DOC_NO", "");
        try {
            ObjFeltReopenBale.setAttribute("ADV_AGN_INV_AMT", 0.00);
        } catch (Exception e) {
        }
        try {
            ObjFeltReopenBale.setAttribute("ADV_AGN_IGST_AMT", 0.00);
        } catch (Exception e) {
        }
        try {
            ObjFeltReopenBale.setAttribute("ADV_AGN_SGST_AMT", 0.00);
        } catch (Exception e) {
        }
        try {
            ObjFeltReopenBale.setAttribute("ADV_AGN_CGST_AMT", 0.00);
        } catch (Exception e) {
        }
        try {
            ObjFeltReopenBale.setAttribute("ADV_AGN_GST_COMP_CESS_AMT", 0.00);
        } catch (Exception e) {
        }
        

        ObjFeltReopenBale.hmFeltBaleNoDetails.clear();
        for (int i = 0; i < BaleTable.getRowCount(); i++) {
            
            if (BaleTable.getValueAt(i, 1).toString().equalsIgnoreCase("TRUE")) {
             clsFeltInvoiceParameterModificationf6FormDetail ObjFeltReopenBaleDetails = new clsFeltInvoiceParameterModificationf6FormDetail();
 
            ObjFeltReopenBaleDetails.setAttribute("F6", true); //3            
            ObjFeltReopenBaleDetails.setAttribute("PARTY_CODE", (String) BaleTable.getValueAt(i, 2)); //1 
            ObjFeltReopenBaleDetails.setAttribute("PARTY_NAME", (String) BaleTable.getValueAt(i, 3)); //1 
            ObjFeltReopenBaleDetails.setAttribute("BALE_NO", (String) BaleTable.getValueAt(i, 4)); //1
            ObjFeltReopenBaleDetails.setAttribute("BALE_DATE", (String) BaleTable.getValueAt(i, 5));
            ObjFeltReopenBaleDetails.setAttribute("BILL_VALUE", Float.parseFloat(BaleTable.getValueAt(i, 6).toString())); //3
            ObjFeltReopenBaleDetails.setAttribute("REMARKS", (String) BaleTable.getValueAt(i, 8)); //1 

            ObjFeltReopenBale.hmFeltBaleNoDetails.put(Integer.toString(ObjFeltReopenBale.hmFeltBaleNoDetails.size() + 1), ObjFeltReopenBaleDetails);
            
            }
        }

        //-------- Update Approval Specific Fields -----------//
        ObjFeltReopenBale.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjFeltReopenBale.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjFeltReopenBale.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjFeltReopenBale.setAttribute("FROM_REMARKS", txtToRemarks.getText().trim());
        ObjFeltReopenBale.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
        if (OpgApprove.isSelected()) {
            ObjFeltReopenBale.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjFeltReopenBale.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjFeltReopenBale.setAttribute("APPROVAL_STATUS", "R");
            ObjFeltReopenBale.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjFeltReopenBale.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjFeltReopenBale.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
        } else {
            ObjFeltReopenBale.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
        }

    }

    private void Cancel() {
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
    }

    // find details by doc no
    public void Find(String docNo) {
        ObjFeltReopenBale.Filter(" DOC_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    // find all pending document
    public void FindWaiting() {
        ObjFeltReopenBale.Filter("DOC_NO IN (SELECT H.DOC_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST H, PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=754 AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pGroupId) {
        ObjFeltReopenBale.Filter(" DOC_NO='" + pGroupId + "'");
        ObjFeltReopenBale.MoveFirst();
        DisplayData();
    }

    private void MoveFirst() {
        ObjFeltReopenBale.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjFeltReopenBale.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjFeltReopenBale.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjFeltReopenBale.MoveLast();
        DisplayData();
    }

    private void EnableApproval() {

        txtdocno.setEnabled(true);
        txtprocessdate.setEnabled(true);
        //txtPartyCode.setEditable(true);
        
        
        // txtpartycode.setEnabled(true);
        //txtpieceno.setEnabled(true);
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        OpgHold.setSelected(true);
        txtToRemarks.setEnabled(true);
        SetupApproval();

        //=============== Setting Table Fields ==================//
        DataModel.ClearAllReadOnly();

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
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();

                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    } else {
                        cmbSendToModel.addElement(aData);
                    }
                }
            } else {
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString());
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString(), ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString(), ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(754, ObjFeltReopenBale.getAttribute("DOC_NO").getString());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
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
        DataModelUpdateHistory.addColumn("From Ip");

        TableColumnModel tcm = TableUpdateHistory.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(2).setPreferredWidth(50);
        tcm.getColumn(3).setPreferredWidth(20);
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.FeltInvoiceParameterModificationF6.frmFindInvoiceParameterModificationf6Form", true);
        frmFindInvoiceParameterModificationf6Form ObjFeltReopenBale1 = (frmFindInvoiceParameterModificationf6Form) ObjLoader.getObj();

        if (ObjFeltReopenBale1.Cancelled == false) {
            if (!ObjFeltReopenBale.Filter(ObjFeltReopenBale1.stringFindQuery)) {
                JOptionPane.showMessageDialog(frmFeltInvoiceParameterModificationf6Form.this, " No records found.", "Find Invoice Process Control Parameter Modification Details  Details", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    private void Report() {

        try {

            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData = new TReportWriter.SimpleDataProvider.TTable();

            objReportData.AddColumn("DOC_NO");
            objReportData.AddColumn("PROCESSING_DATE");
            objReportData.AddColumn("BALE_NO");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("CHARGE_CODE_NEW");
            objReportData.AddColumn("CRITICAL_LIMIT_NEW");
            objReportData.AddColumn("TRANSPORTER_CODE");
            objReportData.AddColumn("INSURANCE_CODE");

            TReportWriter.SimpleDataProvider.TRow objOpeningRow = objReportData.newRow();

            objOpeningRow.setValue("DOC_NO", "");
            objOpeningRow.setValue("PROCESSING_DATE", "");
            objOpeningRow.setValue("BALE_NO", "");
            objOpeningRow.setValue("PARTY_CODE", "");
            objOpeningRow.setValue("CHARGE_CODE_NEW", "");
            objOpeningRow.setValue("CRITICAL_LIMIT_NEW", "");
            objOpeningRow.setValue("TRANSPORTER_CODE", "");
            objOpeningRow.setValue("INSURANCE_CODE", "");

            String strSQL = "SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='" + txtdocno.getText().trim() + "' AND APPROVED=1";

            System.out.println(strSQL);
            ResultSet rsTmp = data.getResult(strSQL);
            rsTmp.first();

            int Counter = 0;

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    Counter++;
                    objRow = objReportData.newRow();

                    objRow.setValue("DOC_NO", UtilFunctions.getString(rsTmp, "DOC_NO", ""));
                    objRow.setValue("PROCESSING_DATE", UtilFunctions.getString(rsTmp, "PROCESSING_DATE", ""));
                    objRow.setValue("BALE_NO", UtilFunctions.getString(rsTmp, "BALE_NO", ""));
                    objRow.setValue("PARTY_CODE", UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                    objRow.setValue("CHARGE_CODE_NEW", UtilFunctions.getString(rsTmp, "CHARGE_CODE_NEW", ""));
                    objRow.setValue("CRITICAL_LIMIT_NEW", UtilFunctions.getString(rsTmp, "CRITICAL_LIMIT_NEW", ""));
                    objRow.setValue("TRANSPORTER_CODE", UtilFunctions.getString(rsTmp, "TRANSPORTER_CODE", ""));
                    objRow.setValue("INSURANCE_CODE", UtilFunctions.getString(rsTmp, "INSURANCE_CODE", ""));

                    objReportData.AddRow(objRow);

                    rsTmp.next();
                }
            }

            int Comp_ID = EITLERPGLOBAL.gCompanyID;

            HashMap Parameters = new HashMap();
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());

            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/rptInvoiceParameter.rpt", Parameters, objReportData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double BalanceTransfer1(String MainCode, String PartyCode) {
        String SQL = "", FromDate = "", ToDate = "", InvoiceNo = "", InvoiceDate = "", BookCode = "", ChargeCode = "";
        ResultSet rsInvoice = null;
        int InvoiceType = 0, EntryNo = 0;
        double TotalBalance = 0;
        try {
            if (MainCode.equals("210010")) {
                InvoiceType = 2;
                BookCode = " AND BOOK_CODE IN ('09') "; //,'18'
                //ChargeCode = " AND CHARGE_CODE IN ('02','08') ";
            }
            // SET LAST CLOSING DATE & TO DATE
            ToDate = EITLERPGLOBAL.getCurrentDateDB();
            FromDate = EITLERPGLOBAL.FinFromDateDB;
            SQL = "SELECT ENTRY_NO FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='" + ToDate + "' ORDER BY ENTRY_DATE DESC";
            EntryNo = data.getIntValueFromDB(SQL, FinanceGlobal.FinURL);
            FromDate = data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_NO=" + EntryNo, FinanceGlobal.FinURL);
            // ------------------------------

            // GET PARTY'S INVOICE NO,INVOICE DATE USING UNION FROM OUTSTANDING DETAIL AND VOUCHER TABLES
            SQL = "(SELECT MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,BOOK_CODE,VOUCHER_NO,VOUCHER_DATE,LEGACY_NO,INVOICE_NO,INVOICE_DATE,LINK_NO,AMOUNT,EFFECT FROM FINANCE.D_FIN_DR_OPENING_OUTSTANDING_DETAIL "
                    + "WHERE INVOICE_TYPE=" + InvoiceType + " AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND ENTRY_NO=" + EntryNo + " AND EFFECT='D' AND (MATCHED_DATE>'" + ToDate + "' OR MATCHED_DATE='0000-00-00') " + BookCode + " ) "
                    + "UNION ALL "
                    + "(SELECT B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.BOOK_CODE,A.VOUCHER_NO,A.VOUCHER_DATE,A.LEGACY_NO,B.INVOICE_NO,B.INVOICE_DATE,B.LINK_NO,B.AMOUNT,B.EFFECT FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B "
                    + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='" + MainCode + "' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.EFFECT='D' AND A.APPROVED=1 AND A.CANCELLED=0 "
                    + "AND A.VOUCHER_DATE >'" + FromDate + "' AND A.VOUCHER_DATE <='" + ToDate + "' AND (B.MATCHED_DATE>'" + ToDate + "' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) " + BookCode + " ) "
                    + "ORDER BY VOUCHER_DATE ";

            data.Execute("INSERT INTO TEMP_DATABASE.TEMP_BAL_TR (MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,BOOK_CODE,VOUCHER_NO,VOUCHER_DATE,LEGACY_NO,INVOICE_NO,INVOICE_DATE,LINK_NO,AMOUNT,EFFECT) " + SQL);

            rsInvoice = data.getResult(SQL, FinanceGlobal.FinURL);
            rsInvoice.first();
            InvoiceNo = "";
            InvoiceDate = "";
            // ---------------------------------------------------------------------

            if (rsInvoice.getRow() > 0) {
                while (!rsInvoice.isAfterLast()) {

                    String VoucherNo = UtilFunctions.getString(rsInvoice, "VOUCHER_NO", "");
                    InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                    InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE", "");
                    double DebitAmount = 0;
                    double AdjustedAmount = 0;
                    if (clsVoucher.getVoucherType(VoucherNo) != FinanceGlobal.TYPE_SALES_JOURNAL) { //&& clsVoucher.getVoucherType(VoucherNo)!=FinanceGlobal.TYPE_DEBIT_NOTE
                        rsInvoice.next();
                        continue;
                    }
//                    if (InvoiceType == 2 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
//                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("02") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("08") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("04")) {
//                            rsInvoice.next();
//                            continue;
//                        }
//                    } else if (InvoiceType == 1 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
//                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("02") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("08") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("04")) {
//                            rsInvoice.next();
//                            continue;
//                        }
//                    }

                    if (InvoiceType == 2 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4")) {
                            rsInvoice.next();
                            continue;
                        }
                    } else if (InvoiceType == 1 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("5") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4")) {
                            rsInvoice.next();
                            continue;
                        }
                    }

                    if (!data.IsRecordExist("SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE DEBITNOTE_VOUCHER_NO='" + VoucherNo + "'", FinanceGlobal.FinURL)
                            && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_DEBIT_NOTE) {
                        rsInvoice.next();
                        continue;
                    }

                    if (clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
                        SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + VoucherNo + "' AND EFFECT='D' "
                                + "AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND SUB_ACCOUNT_CODE='" + PartyCode + "' "
                                + "AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' "
                                + "AND (MATCHED_DATE>'" + ToDate + "' OR MATCHED_DATE='0000-00-00' OR MATCHED_DATE IS NULL ) ";
                        DebitAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);

                        SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_DETAIL B, D_FIN_VOUCHER_HEADER A "
                                + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.CANCELLED=0 "
                                + "AND (B.MATCHED_DATE>'" + ToDate + "' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) "
                                + "AND B.MAIN_ACCOUNT_CODE='" + MainCode + "' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.EFFECT='C' "
                                + "AND B.INVOICE_DATE='" + InvoiceDate + "' AND B.INVOICE_NO='" + InvoiceNo + "' ";

                        AdjustedAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);
                    } else {
                        SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + VoucherNo + "' AND EFFECT='D' "
                                + "AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND SUB_ACCOUNT_CODE='" + PartyCode + "' "
                                + "AND (MATCHED_DATE>'" + ToDate + "' OR MATCHED_DATE='0000-00-00' OR MATCHED_DATE IS NULL ) ";
                        DebitAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);

                        SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_DETAIL B, D_FIN_VOUCHER_HEADER A "
                                + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.CANCELLED=0 "
                                + "AND (B.MATCHED_DATE>'" + ToDate + "' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) "
                                + "AND B.MAIN_ACCOUNT_CODE='" + MainCode + "' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.EFFECT='C' "
                                + "AND B.GRN_NO='" + VoucherNo + "' ";
                        AdjustedAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);
                    }
                    if (DebitAmount == AdjustedAmount) {
                        rsInvoice.next();
                        continue;
                    }

                    TotalBalance = EITLERPGLOBAL.round(TotalBalance + EITLERPGLOBAL.round(DebitAmount - AdjustedAmount, 2), 2);
                    rsInvoice.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return TotalBalance;
        }
        return TotalBalance;
    }

    public static void OutstandingReport() {

        String strSQL = "";
        String Condition = "";
        ResultSet rsData = null;
        TReportEngine objEngine1 = new TReportEngine();
        TReportWriter.SimpleDataProvider.TTable objData = new TReportWriter.SimpleDataProvider.TTable();

        try {
            objData.AddColumn("SUB_PARTY_CODE");
            objData.AddColumn("GROUP_DESC");
            //objData.AddColumn("MAIN_PARTY_CODE");
            objData.AddColumn("PARTY_NAME");
            objData.AddColumn("AMOUNT");
            objData.AddColumn("GRP_AMOUNT");
            objData.AddColumn("PARTY_LIMIT");
            objData.AddColumn("GROUP_LIMIT");

//            strSQL = "SELECT SUB_PARTY_CODE ,SUB_OUTSTANDING_BAL AS AMOUNT FROM TEMP_DATABASE.TEMP_OUTSTANDING_REPORT ";
            strSQL = "SELECT DISTINCT SUB_PARTY_CODE,MAIN_PARTY_CODE,SUB_OUTSTANDING_BAL AS AMOUNT FROM TEMP_DATABASE.TEMP_PARA_OUTSTANDING_REPORT";

            rsData = data.getResult(strSQL);
            System.out.println(strSQL);
            rsData.first();
            TReportWriter.SimpleDataProvider.TRow objRow = null;
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {
                    objRow = objData.newRow();
                    //String SubAccountCode = UtilFunctions.getString(rsData, "SUB_PARTY_CODE", "");
                    double Amount = UtilFunctions.getDouble(rsData, "AMOUNT", 0);
                    String PartyName = data.getStringValueFromDB("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + rsData.getString("SUB_PARTY_CODE") + "' AND MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0");
                    double pLimit = 0;
                    if (data.IsRecordExist("SELECT D.PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H WHERE D.GROUP_CODE=H.GROUP_CODE AND D.PARTY_CODE ='" + rsData.getString("SUB_PARTY_CODE") + "' AND H.APPROVED=1 AND H.CANCELED=0")) {
                        pLimit = data.getDoubleValueFromDB("SELECT D.CRITICAL_LIMIT FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H WHERE D.GROUP_CODE=H.GROUP_CODE AND D.PARTY_CODE ='" + rsData.getString("SUB_PARTY_CODE") + "' AND H.APPROVED=1 AND H.CANCELED=0");
                    } else {
                        pLimit = data.getDoubleValueFromDB("SELECT AMOUNT_LIMIT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE ='" + rsData.getString("SUB_PARTY_CODE") + "' AND MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0");
                    }

                    //Amount = clsAccount.get09AmountByVoucher(VoucherNo,MainCode,SubAccountCode,Amount);
                    //if (Amount > 0) {
                    objRow.setValue("SUB_PARTY_CODE", UtilFunctions.getString(rsData, "SUB_PARTY_CODE", ""));
                    objRow.setValue("GROUP_DESC", data.getStringValueFromDB("SELECT H.GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H WHERE D.GROUP_CODE=H.GROUP_CODE AND D.PARTY_CODE ='" + rsData.getString("SUB_PARTY_CODE") + "' AND H.APPROVED=1 AND H.CANCELED=0"));
//                        objRow.setValue("PARTY_NAME", clsPartyMaster.getAccountName("210010", UtilFunctions.getString(rsData, "SUB_PARTY_CODE", "")));
                    objRow.setValue("PARTY_NAME", PartyName);
                    objRow.setValue("AMOUNT", Double.toString(Amount));
                    objRow.setValue("GRP_AMOUNT", Double.toString(0));
//                        objRow.setValue("PARTY_LIMIT", Double.toString(data.getDoubleValueFromDB("SELECT D.CRITICAL_LIMIT FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H WHERE D.GROUP_CODE=H.GROUP_CODE AND D.PARTY_CODE ='"+rsData.getString("SUB_PARTY_CODE")+"' AND H.APPROVED=1 AND H.CANCELED=0")));
                    objRow.setValue("PARTY_LIMIT", Double.toString(pLimit));
                    objRow.setValue("GROUP_LIMIT", Double.toString(data.getDoubleValueFromDB("SELECT H.GROUP_CRITICAL_LIMIT FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H WHERE D.GROUP_CODE=H.GROUP_CODE AND D.PARTY_CODE ='" + rsData.getString("SUB_PARTY_CODE") + "' AND H.APPROVED=1 AND H.CANCELED=0")));
                    objData.AddRow(objRow);
                    //}
                    rsData.next();
                }
            }

            HashMap Parameters = new HashMap();

            //Parameters.put("PARTY_NAME", MainCode + " - " + clsPartyMaster.getAccountName(MainCode.trim(), ""));
            Parameters.put("AS_ON_DATE", EITLERPGLOBAL.getCurrentDate());
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
            objEngine1.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/finance/rptOutstandingAmountSummary.rpt", Parameters, objData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void FormatGridBaleTable() {
        DataModelBaleNo = new EITLTableModel();
        BaleSelectTable.removeAll();

        BaleSelectTable.setModel(DataModelBaleNo);
        TableColumnModel ColModel = BaleSelectTable.getColumnModel();
        BaleSelectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DataModelBaleNo.addColumn("Sr.No"); //0
        DataModelBaleNo.addColumn("F6"); //0
        DataModelBaleNo.addColumn("Party Code"); //1
        DataModelBaleNo.addColumn("Party Name"); //2
        DataModelBaleNo.addColumn("Bale No"); //3
        DataModelBaleNo.addColumn("Bale Date"); //4
        DataModelBaleNo.addColumn("Bill Value"); //5
        DataModelBaleNo.addColumn("Piece No"); //6

        DataModelBaleNo.SetVariable(0, "");
        DataModelBaleNo.SetVariable(4, "BALE_NO");
        DataModelBaleNo.SetVariable(5, "BALE_DATE");
        DataModelBaleNo.SetVariable(6, "BILL_VALUE");

        //DataModelPieceNo.SetNumeric(2, true);
        DataModelBaleNo.TableReadOnly(false);

        for (int i = 2; i <= 10; i++) {
            DataModelBaleNo.SetReadOnly(i);
        }
        
        Rend.setCustomComponent(1,"CheckBox");
        BaleSelectTable.getColumnModel().getColumn(1).setCellRenderer(Rend);
        BaleSelectTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        RowFormat=new EITLTableCellRenderer();
            
            for(int j=2;j<BaleSelectTable.getColumnCount();j++) {
                BaleSelectTable.getColumnModel().getColumn(j).setCellRenderer(RowFormat);
            }
            
            
            CellAlign.setHorizontalAlignment(JLabel.RIGHT);
            
    }

    private void FormatGrid() {
        DataModelPieceNo = new EITLTableModel();
        BaleTable.removeAll();

        BaleTable.setModel(DataModelPieceNo);
        TableColumnModel ColModel = BaleTable.getColumnModel();
        BaleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DataModelPieceNo.addColumn("Sr.No"); //0
        DataModelPieceNo.addColumn("F6"); //0
        DataModelPieceNo.addColumn("Party Code"); //1
        DataModelPieceNo.addColumn("Party Name"); //2
        DataModelPieceNo.addColumn("Bale No"); //3
        DataModelPieceNo.addColumn("Bale Date"); //4
        DataModelPieceNo.addColumn("Bill Value"); //5
        DataModelPieceNo.addColumn("Piece No"); //6
        DataModelPieceNo.addColumn("Remarks"); //6

        DataModelPieceNo.SetVariable(0, "");
        DataModelPieceNo.SetVariable(4, "BALE_NO");
        DataModelPieceNo.SetVariable(5, "BALE_DATE");
        DataModelPieceNo.SetVariable(6, "BILL_VALUE");

        //DataModelPieceNo.SetNumeric(2, true);
        DataModelPieceNo.TableReadOnly(false);

        for (int i = 1; i <= 7; i++) {
            DataModelPieceNo.SetReadOnly(i);
        }
        
        Rend1.setCustomComponent(1,"CheckBox");
        BaleTable.getColumnModel().getColumn(1).setCellRenderer(Rend1);
        BaleTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        RowFormat1=new EITLTableCellRenderer();
            
            for(int j=2;j<BaleTable.getColumnCount();j++) {
                BaleTable.getColumnModel().getColumn(j).setCellRenderer(RowFormat1);
            }
            
            
            CellAlign1.setHorizontalAlignment(JLabel.RIGHT);
        
    }
    
    private void GenerateGrid(){
    try {       
            
            //String strSQL2 = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=0 AND PROCESSING_DATE>=CURDATE() ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO \n"
            //            + " UNION ALL \n"
            //            + " SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=1 AND PROCESSING_DATE>=CURDATE()) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO ORDER BY PKG_PARTY_CODE,PKG_BALE_NO ";
        String strSQL2 = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=0 AND PROCESSING_DATE>=CURDATE() ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO \n"
                + " UNION ALL \n"
                + " SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=1 AND PROCESSING_DATE>=CURDATE()) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO "// ORDER BY PKG_PARTY_CODE,PKG_BALE_NO "
                + " UNION ALL \n"
                + " SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+txtdocno.getText()+"') AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO ORDER BY PKG_PARTY_CODE,PKG_BALE_NO ";

        
            System.out.println("SQL "+strSQL2);
            
            ResultSet rs = data.getResult(strSQL2);            int cnt = 1;
            int srno = 1;
            while (!rs.isAfterLast()) {
                Object[] rowData = new Object[10];
                // rowData[0]=rs.getString("");
                rowData[0] = srno++; //By default not selected//cnt++;
                rowData[1] = Boolean.valueOf(false); //By default not selected//cnt++;
                rowData[2] = rs.getString("PKG_PARTY_CODE");
                rowData[3] = rs.getString("PKG_PARTY_NAME");
                rowData[4] = rs.getString("PKG_BALE_NO");
                String BaleDate = EITLERPGLOBAL.formatDate(clsFeltInvoiceParameterModificationf6Form.getBaleDate(rs.getString("PKG_BALE_NO")));
                rowData[5] = BaleDate;
                
                String PieceNO = data.getStringValueFromDB("SELECT PKG_PIECE_NO FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                String ProductCode = data.getStringValueFromDB("SELECT PKG_PRODUCT_CODE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                //String Partycode = txtPartyCode.getText();//txtPartyCode.setText(Partycode);
                String Length = data.getStringValueFromDB("SELECT PKG_LENGTH FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                String Width = data.getStringValueFromDB("SELECT PKG_WIDTH FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                String Weight = data.getStringValueFromDB("SELECT PKG_WEIGHT FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                String Sqmtr = data.getStringValueFromDB("SELECT PKG_SQM FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
//                String Orderdate = data.getStringValueFromDB("SELECT PKG_ORDER_DATE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "'");
                String Orderdate = EITLERPGLOBAL.getCurrentDateDB();
                    //lblPartyName.setText(clsFeltInvoiceParameterModificationf6Form.getParyName(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));
//                    txtchargecodeold1.setText(clsFeltInvoiceParameterModificationf6Form.getChargeCode(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));

                
                
                inv_calculation = EITLERP.FeltSales.common.clsOrderValueCalc.calculate(PieceNO, ProductCode, rs.getString("PKG_PARTY_CODE"), Float.parseFloat(Length), Float.parseFloat(Width), Float.parseFloat(Weight), Float.parseFloat(Sqmtr), Orderdate);
                    Float billvalue = inv_calculation.getFicInvAmt();
                rowData[6] = billvalue;
                rowData[7] = PieceNO;                
                DataModelBaleNo.addRow(rowData);
                //DataModel.addRow(rowData);
                rs.next();
            }
            rs.close();
            //Object[] rowData2 = new Object[25];
            //DataModelB2BMIR.addRow(rowData2);
            
            
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        
}

    
    private void GenerateGrid1(){
    try {       
            
            String strSQL2 = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=0 AND PROCESSING_DATE>=CURDATE() ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO \n"
                        + " UNION ALL \n"
                        + " SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE CANCELED=1 AND PROCESSING_DATE>=CURDATE()) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO "// ORDER BY PKG_PARTY_CODE,PKG_BALE_NO "
                        + " UNION ALL \n"
                        + " SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE DOC_NO='"+txtdocno.getText()+"') AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.CANCELED=0 AND A.INVOICE_FLG=0  GROUP BY B.PKG_BALE_NO ORDER BY PKG_PARTY_CODE,PKG_BALE_NO ";
            
            System.out.println("SQL "+strSQL2);
            
            ResultSet rs = data.getResult(strSQL2);            int cnt = 1;
            int srno = BaleTable.getRowCount()+1;
            while (!rs.isAfterLast()) {
                Object[] rowData = new Object[10];
                // rowData[0]=rs.getString("");
                //rowData[0] = srno++; //By default not selected//cnt++;                
                if (BaleSelectTable.getValueAt(rs.getRow()-1, 1).toString().equalsIgnoreCase("TRUE")) {
                rowData[0] = srno++;
                rowData[1] = Boolean.valueOf(true); //By default not selected//cnt++;
                rowData[2] = rs.getString("PKG_PARTY_CODE");
                rowData[3] = rs.getString("PKG_PARTY_NAME");
                rowData[4] = rs.getString("PKG_BALE_NO");
                String BaleDate = EITLERPGLOBAL.formatDate(clsFeltInvoiceParameterModificationf6Form.getBaleDate(rs.getString("PKG_BALE_NO")));
                rowData[5] = BaleDate;
                
                String PieceNO = data.getStringValueFromDB("SELECT PKG_PIECE_NO FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                String ProductCode = data.getStringValueFromDB("SELECT PKG_PRODUCT_CODE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                //String Partycode = txtPartyCode.getText();//txtPartyCode.setText(Partycode);
                String Length = data.getStringValueFromDB("SELECT PKG_LENGTH FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                String Width = data.getStringValueFromDB("SELECT PKG_WIDTH FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                String Weight = data.getStringValueFromDB("SELECT PKG_WEIGHT FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                String Sqmtr = data.getStringValueFromDB("SELECT PKG_SQM FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + rs.getString("PKG_BALE_NO") + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
//                String Orderdate = data.getStringValueFromDB("SELECT PKG_ORDER_DATE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "'");
                String Orderdate = EITLERPGLOBAL.getCurrentDateDB();
                    //lblPartyName.setText(clsFeltInvoiceParameterModificationf6Form.getParyName(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));
//                    txtchargecodeold1.setText(clsFeltInvoiceParameterModificationf6Form.getChargeCode(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));
               
                
                inv_calculation = EITLERP.FeltSales.common.clsOrderValueCalc.calculate(PieceNO, ProductCode, rs.getString("PKG_PARTY_CODE"), Float.parseFloat(Length), Float.parseFloat(Width), Float.parseFloat(Weight), Float.parseFloat(Sqmtr), Orderdate);
                    Float billvalue = inv_calculation.getFicInvAmt();
                rowData[6] = billvalue;
                rowData[7] = PieceNO; 
                DataModelPieceNo.addRow(rowData);
                }
                //DataModel.addRow(rowData);
                rs.next();
            }
            rs.close();
            //Object[] rowData2 = new Object[25];
            //DataModelB2BMIR.addRow(rowData2);
            
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        
}
    
    private void searchWithin(char recentKey) {
        
        try {
            
            RowFormat.removeBackColors();
            CellAlign.removeBackColors();
            
            BaleSelectTable.repaint();
            
            String searchString=txtSearch.getText();
            
            if(!searchString.trim().equals("")) {
                
                
                if(recentKey!=' ') {
                    searchString=searchString+recentKey;
                }
                
                searchString=searchString.toLowerCase();
                
                int currentCol=BaleSelectTable.getSelectedColumn();
                
                for(int i=0;i<=BaleSelectTable.getRowCount()-1;i++) {
                    
                    if(BaleSelectTable.getValueAt(i,currentCol).toString().trim().toLowerCase().indexOf(searchString)!=-1) {
                        
                        BaleSelectTable.changeSelection(i, currentCol, false,false);
                        
                        RowFormat.setBackColor(i, currentCol, Color.YELLOW);
                        
                        if(currentCol==DataModel.getColFromVariable("DEBIT_AMOUNT")||currentCol==DataModel.getColFromVariable("CREDIT_AMOUNT")) {
                            CellAlign.setBackColor(i, currentCol, Color.YELLOW);
                        }
                        if(currentCol==DataModel.getColFromVariable("PO_NO")) {
                            POColor.setBackColor(i, currentCol, Color.YELLOW);
                        }
                    }
                }
                BaleSelectTable.repaint();
            }
        }
        catch(Exception e) {
            
        }
    }
    
    private void searchWithin1(char recentKey) {

        try {

            RowFormat1.removeBackColors();
            CellAlign1.removeBackColors();

            BaleTable.repaint();

            String searchString = txtSearch1.getText();

            if (!searchString.trim().equals("")) {

                if (recentKey != ' ') {
                    searchString = searchString + recentKey;
                }

                searchString = searchString.toLowerCase();

                int currentCol = BaleTable.getSelectedColumn();

                for (int i = 0; i <= BaleTable.getRowCount() - 1; i++) {

                    if (BaleTable.getValueAt(i, currentCol).toString().trim().toLowerCase().indexOf(searchString) != -1) {

                        BaleTable.changeSelection(i, currentCol, false, false);

                        RowFormat1.setBackColor(i, currentCol, Color.YELLOW);

                        if (currentCol == DataModel.getColFromVariable("DEBIT_AMOUNT") || currentCol == DataModel.getColFromVariable("CREDIT_AMOUNT")) {
                            CellAlign1.setBackColor(i, currentCol, Color.YELLOW);
                        }
                        if (currentCol == DataModel.getColFromVariable("PO_NO")) {
                            POColor1.setBackColor(i, currentCol, Color.YELLOW);
                        }
                    }
                }
                BaleTable.repaint();
            }
        } catch (Exception e) {

        }
    }


}
