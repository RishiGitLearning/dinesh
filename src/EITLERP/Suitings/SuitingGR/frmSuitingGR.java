/*
 * frmSuitingGR.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.Suitings.SuitingGR;

/**
 *
 * @author
 */
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
import EITLERP.*;
import java.net.URL;
import java.sql.*;
import EITLERP.Finance.UtilFunctions;
import TReportWriter.*;
import EITLERP.data;
import EITLERP.LOV;
import EITLERP.EITLComboModel;
import EITLERP.EITLTableModel;
import EITLERP.EITLERPGLOBAL;
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
import EITLERP.Production.FeltUser;
import java.io.File;
import EITLERP.Sales.frmSalesInvoice;
import EITLERP.Production.ReportUI.JTextFieldHint;



public class frmSuitingGR extends javax.swing.JApplet {

    private int EditMode = 0;
    private int SelModule = 0;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromUserId = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    public int DeptID = EITLERPGLOBAL.gUserDeptID;
    public String finalapproved = "";
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    private boolean DoNotEvaluate = false;
    private clsSuitingGR ObjFeltCreditNote;
    //private clsExcelExporter exp = new clsExcelExporter();
    private TReportEngine objEngine = new TReportEngine();
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbSendToModel;
    private EITLComboModel cmbOrderReasonModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableModel DataModelHSNINVOICEPOST;
    
    private String[] c = new String[100];
    private String[] a = new String[100];
    private String[] b = new String[100];
    
    private String hsnCode = "";
    private String hcd = "";
    private String icd = "";
    private String idt = "";
    private String LINKno = "";
//    private String netAmt = "";
//    private String netgrAmt = "";
//    private String CGSTAmt = "";
//    private String SGSTAmt = "";
//    private String IGSTAmt = "";
//    private String Compositionamt = "";
//    private double netAmt = 0;
//    private double CGSTAmt = 0;
//    private double SGSTAmt = 0;
//    private double IGSTAmt = 0;
//    private double RCMAmt = 0;
    private double HsnNo = 0;
    
    
    private double netAmt=0;
    private double netgrAmt=0;
    private double CGSTAmt=0;
    private double SGSTAmt=0;
    private double IGSTAmt=0;
    private double GRQty=0;
    private double TotalAmt = 0;
    
    private double CGSTPer=0;
    private double SGSTPer=0;
    private double IGSTPer=0;
    private double RCMPer=0;
    private String InvoiceNo="";
    
    
    private double RCMAmt = 0;
    private double GSTCompCessAmt = 0;
    private double netGRAmt=0;
    
    private EITLTableCellRenderer RowFormat = new EITLTableCellRenderer();
    public frmPendingApprovals frmPA;
    public double total_r6;
    private boolean Updating = false;

    /**
     * Creates new form frmSuitingGR
     */
    public void init() {
        System.gc();
        setSize(830, 590);
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

        DataModel = new EITLTableModel();

        ObjFeltCreditNote = new clsSuitingGR();

        SetMenuForRights();
        GenerateHierarchyCombo();
        GenerateSendToCombo();
        FormatGrid();
        FormatGridHSN_InvoiePst();
        cmdPreview.setEnabled(true);
        if (ObjFeltCreditNote.LoadData()) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(frmSuitingGR.this, "Error occured while Loading Data. Error is " + ObjFeltCreditNote.LastError, "DATA LOADING ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuIAccount = new javax.swing.JMenuItem();
        jMenuIPartyCredit = new javax.swing.JMenuItem();
        mnuShow = new javax.swing.JPopupMenu();
        ShowInv = new javax.swing.JMenuItem();
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
        jLabel3 = new javax.swing.JLabel();
        txtGRDate = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cmdNextToTab1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtGRID = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtGRDesc = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtInwardNo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtInwardDate = new javax.swing.JTextField();
        Tab3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Table1 = new javax.swing.JTable();
        cmdRemove1 = new javax.swing.JButton();
        cmdNextToTab3 = new javax.swing.JButton();
        cmdBackToTab2 = new javax.swing.JButton();
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
        cmdBackToTab1 = new javax.swing.JButton();
        cmdBackToNormal = new javax.swing.JButton();
        cmdViewRevisions = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        jMenuIAccount.setActionCommand("Account Report");
        jMenuIAccount.setLabel("Account Report");
        jMenuIAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuIAccountActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuIAccount);

        jMenuIPartyCredit.setActionCommand("Party Credit Note Report");
        jMenuIPartyCredit.setLabel("Party Credit Note Report");
        jMenuIPartyCredit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuIPartyCreditActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuIPartyCredit);

        ShowInv.setText("Show Invoice");
        ShowInv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowInvActionPerformed(evt);
            }
        });
        mnuShow.add(ShowInv);

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
        ToolBar.setBounds(0, 0, 830, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText(" SUITING GOOD RETURNS ENTRY FORM - ");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 830, 25);

        Tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab1FocusGained(evt);
            }
        });
        Tab1.setLayout(null);

        jLabel3.setText("GR Date");
        Tab1.add(jLabel3);
        jLabel3.setBounds(210, 10, 70, 16);

        txtGRDate.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtGRDate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtGRDate.setEnabled(false);
        txtGRDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGRDateFocusGained(evt);
            }
        });
        Tab1.add(txtGRDate);
        txtGRDate.setBounds(280, 10, 102, 29);

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
        Table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableMouseClicked(evt);
            }
        });
        Table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TableFocusGained(evt);
            }
        });
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 130, 760, 290);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setToolTipText("Add Row");
        cmdAdd.setEnabled(false);
        cmdAdd.setNextFocusableComponent(cmdRemove);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        Tab1.add(cmdAdd);
        cmdAdd.setBounds(460, 430, 90, 28);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setToolTipText("Remove Selected Row");
        cmdRemove.setEnabled(false);
        cmdRemove.setNextFocusableComponent(cmdNextToTab1);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        Tab1.add(cmdRemove);
        cmdRemove.setBounds(570, 430, 90, 28);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel3);
        jPanel3.setBounds(0, 110, 770, 6);

        cmdNextToTab1.setMnemonic('N');
        cmdNextToTab1.setText("Next >>");
        cmdNextToTab1.setToolTipText("Next Tab");
        cmdNextToTab1.setNextFocusableComponent(cmdRemove);
        cmdNextToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNextToTab1);
        cmdNextToTab1.setBounds(680, 430, 90, 28);

        jLabel5.setText("GR  Desc No");
        Tab1.add(jLabel5);
        jLabel5.setBounds(450, 10, 80, 16);

        txtGRID.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtGRID.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtGRID.setEnabled(false);
        Tab1.add(txtGRID);
        txtGRID.setBounds(90, 10, 102, 29);

        lblRevNo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(400, 10, 20, 18);

        jLabel6.setText("GR  ID");
        Tab1.add(jLabel6);
        jLabel6.setBounds(20, 10, 40, 16);

        txtGRDesc.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtGRDesc.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtGRDesc.setEnabled(false);
        Tab1.add(txtGRDesc);
        txtGRDesc.setBounds(540, 10, 102, 29);

        jLabel7.setText("Party Code");
        Tab1.add(jLabel7);
        jLabel7.setBounds(10, 40, 70, 16);

        txtPartyCode.setEnabled(false);
        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusLost(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyReleased(evt);
            }
        });
        Tab1.add(txtPartyCode);
        txtPartyCode.setBounds(90, 40, 100, 28);

        txtPartyName.setEnabled(false);
        Tab1.add(txtPartyName);
        txtPartyName.setBounds(210, 40, 280, 28);

        txtCity.setEnabled(false);
        Tab1.add(txtCity);
        txtCity.setBounds(510, 40, 150, 28);

        jLabel8.setText("Inward No");
        Tab1.add(jLabel8);
        jLabel8.setBounds(10, 70, 70, 16);

        txtInwardNo.setEnabled(false);
        Tab1.add(txtInwardNo);
        txtInwardNo.setBounds(90, 70, 100, 28);

        jLabel9.setText("Inward Date");
        Tab1.add(jLabel9);
        jLabel9.setBounds(210, 70, 90, 16);

        txtInwardDate.setEnabled(false);
        Tab1.add(txtInwardDate);
        txtInwardDate.setBounds(300, 70, 150, 28);

        Tab.addTab("Goods Returns Details", Tab1);

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab3FocusGained(evt);
            }
        });
        Tab3.setLayout(null);

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
        Table1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table1MouseClicked(evt);
            }
        });
        Table1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Table1FocusGained(evt);
            }
        });
        Table1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Table1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table1KeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(Table1);

        Tab3.add(jScrollPane4);
        jScrollPane4.setBounds(10, 10, 760, 290);

        cmdRemove1.setMnemonic('R');
        cmdRemove1.setText("Remove");
        cmdRemove1.setToolTipText("Remove Selected Row");
        cmdRemove1.setEnabled(false);
        cmdRemove1.setNextFocusableComponent(cmdNextToTab1);
        cmdRemove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemove1ActionPerformed(evt);
            }
        });
        Tab3.add(cmdRemove1);
        cmdRemove1.setBounds(450, 430, 90, 28);

        cmdNextToTab3.setMnemonic('N');
        cmdNextToTab3.setText("Next >>");
        cmdNextToTab3.setToolTipText("Next Tab");
        cmdNextToTab3.setNextFocusableComponent(cmdRemove);
        cmdNextToTab3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab3ActionPerformed(evt);
            }
        });
        Tab3.add(cmdNextToTab3);
        cmdNextToTab3.setBounds(680, 430, 90, 28);

        cmdBackToTab2.setMnemonic('B');
        cmdBackToTab2.setText("<< Back");
        cmdBackToTab2.setToolTipText("Previous Tab");
        cmdBackToTab2.setNextFocusableComponent(cmdRemove);
        cmdBackToTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab2ActionPerformed(evt);
            }
        });
        Tab3.add(cmdBackToTab2);
        cmdBackToTab2.setBounds(560, 430, 102, 28);

        Tab.addTab("HSN Invoice Wise Credit Note Posting", Tab3);

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab2FocusGained(evt);
            }
        });
        Tab2.setLayout(null);

        jLabel31.setText("Hierarchy ");
        Tab2.add(jLabel31);
        jLabel31.setBounds(7, 23, 62, 16);

        cmbHierarchy.setNextFocusableComponent(OpgApprove);
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
        cmbHierarchy.setBounds(86, 20, 184, 28);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(7, 62, 33, 16);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        txtFrom.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtFrom.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFrom);
        txtFrom.setBounds(86, 60, 184, 29);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(7, 97, 61, 16);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setEnabled(false);
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(86, 95, 630, 28);

        jLabel36.setText("Your Action");
        Tab2.add(jLabel36);
        jLabel36.setBounds(7, 130, 73, 16);

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
        jPanel6.setBounds(86, 130, 184, 100);

        jLabel33.setText("Send To");
        Tab2.add(jLabel33);
        jLabel33.setBounds(7, 249, 50, 16);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(86, 245, 184, 28);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(7, 288, 60, 16);

        txtToRemarks.setNextFocusableComponent(cmdBackToTab0);
        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(86, 286, 630, 28);

        cmdBackToTab0.setMnemonic('B');
        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.setToolTipText("Previous Tab");
        cmdBackToTab0.setNextFocusableComponent(cmdRemove);
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(500, 350, 102, 28);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(728, 94, 24, 21);

        cmdNextToTab2.setMnemonic('N');
        cmdNextToTab2.setText("Next >>");
        cmdNextToTab2.setToolTipText("Next Tab");
        cmdNextToTab2.setNextFocusableComponent(cmdRemove);
        cmdNextToTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNextToTab2);
        cmdNextToTab2.setBounds(620, 350, 102, 28);

        Tab.addTab("Approval", Tab2);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(null);

        jLabel26.setText("Document Approval Status");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(8, 5, 170, 16);

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

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(12, 24, 760, 150);

        lblDocumentHistory.setText("Document Update History");
        jPanel1.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(8, 191, 163, 16);

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

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 210, 640, 180);

        cmdBackToTab1.setMnemonic('B');
        cmdBackToTab1.setText("<< Back");
        cmdBackToTab1.setToolTipText("Previous Tab");
        cmdBackToTab1.setIconTextGap(0);
        cmdBackToTab1.setNextFocusableComponent(cmdRemove);
        cmdBackToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab1ActionPerformed(evt);
            }
        });
        jPanel1.add(cmdBackToTab1);
        cmdBackToTab1.setBounds(662, 390, 110, 28);

        cmdBackToNormal.setText("Back To Normal");
        cmdBackToNormal.setMargin(new java.awt.Insets(2, 3, 2, 3));
        cmdBackToNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToNormalActionPerformed(evt);
            }
        });
        jPanel1.add(cmdBackToNormal);
        cmdBackToNormal.setBounds(662, 240, 110, 32);

        cmdViewRevisions.setText("View Revisions");
        cmdViewRevisions.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdViewRevisions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewRevisionsActionPerformed(evt);
            }
        });
        jPanel1.add(cmdViewRevisions);
        cmdViewRevisions.setBounds(662, 210, 110, 32);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        jPanel1.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(662, 270, 110, 32);

        Tab.addTab("Status", jPanel1);

        getContentPane().add(Tab);
        Tab.setBounds(2, 66, 790, 510);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 590, 790, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void ShowInvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowInvActionPerformed
//try {
//            //String InvoiceNo=DataModel.getValueByVariable("CN_INVOICE_NO", Table.getSelectedRow());
//            String InvoiceNo=DataModel.getValueAt(Table.getSelectedRow(),2).toString(); 
//            //String InvoiceDate=DataModel.getValueByVariable("CN_INVOICE_DATE", Table.getSelectedRow());
//            String InvoiceDate=EITLERPGLOBAL.formatDateDB(DataModel.getValueAt(Table.getSelectedRow(),3).toString()); 
//            String PartyCode=DataModel.getValueAt(Table.getSelectedRow(),4).toString(); 
//            AppletFrame aFrame=new AppletFrame("Sales Invoice");
//            aFrame.startAppletEx("EITLERP.Sales.frmSalesInvoice","Sales Invoice");
//            frmSalesInvoice ObjDoc=(frmSalesInvoice) aFrame.ObjApplet; 
//            //int CompanyID=UtilFunctions.CInt(DataModel.getValueByVariable("REF_COMPANY_ID", Table.getSelectedRow()));
//            ObjDoc.FindEx1(PartyCode,InvoiceNo,InvoiceDate); 
//            
//        }
//        catch(Exception e) {
//                
//        }           // TODO add your handling code here:
    }//GEN-LAST:event_ShowInvActionPerformed

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed

        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            
            Updating = true;

            if (Table.getSelectedColumn() == 1) {
                LOV aList = new LOV();

                String strSQL = "SELECT CONCAT(PIECE_NO,QUALITY_NO,PARTY_CODE,INVOICE_DATE,INVOICE_NO) AS DATA_KEY,PIECE_NO,PARTY_CODE,INVOICE_NO,INVOICE_DATE FROM DINESHMILLS.D_SAL_INVOICE_DETAIL WHERE PARTY_CODE='" + txtPartyCode.getText() + "' AND INVOICE_DATE>='2010-07-01' ORDER BY PIECE_NO";

                aList.SQL = strSQL;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;

                if (aList.ShowLOV()) {

                    if (Table.getCellEditor() != null) {
                        Table.getCellEditor().stopCellEditing();
                    }
                    Table.setValueAt(aList.ReturnVal, Table.getSelectedRow(), 1);

                    //   inv_calculation = EITLERP.FeltSales.Order.clsOrderValue.calculate(theDocNo, theDocNo, strSQL, TOP_ALIGNMENT, TOP_ALIGNMENT, FFNo, TOP_ALIGNMENT, TOP_ALIGNMENT, strSQL)
                }

            }
            
            Updating = false;
            
        }
        

    }//GEN-LAST:event_TableKeyPressed

    private void TableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusGained

    }//GEN-LAST:event_TableFocusGained

    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased

        Updating = true;
        
        double ShortLengthAmount = 0, grossqty = 0, rate = 0, shortlengthper = 0, GRQty = 0, ShotLengthAmt = 0, GRGrossAmt = 0, StockLotAmt = 0, ReverseAmt = 0, NetGRAmt = 0;
         double GRAmt=0,ShotLotPer=0,ShotLotAmt=0,CgstAmt=0,SgstAmt=0,IgstAmt=0,totalAmt=0,CgstPer=0,SgstPer=0,IgstPer=0,RcmPer=0,RcmAmt=0,totalamt=0;

        try {
            
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 8).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 8);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 12).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 12);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 16).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 16);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 11).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 11);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 10).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 10);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 20).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 20);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 21).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 21);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 18).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 18);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 19).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 19);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 24).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 24);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 25).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 25);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 26).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 26);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 27).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 27);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 28).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 28);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 29).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 29);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 30).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 30);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 31).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 31);
            }
            if ("".equals(Table.getValueAt(Table.getSelectedRow(), 32).toString())) {
                Table.setValueAt("0", Table.getSelectedRow(), 32);
            }
            
            grossqty = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 8).toString());
            rate = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 12).toString());
            shortlengthper = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 16).toString());
            GRQty = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 11).toString());
            StockLotAmt = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 20).toString());
            ReverseAmt = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 21).toString());
            GRAmt = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 18).toString());
            ShotLotPer = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 19).toString());
            CgstPer = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 24).toString());
            CgstAmt = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 25).toString());
            SgstPer = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 26).toString());
            SgstAmt = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 27).toString());
            IgstPer = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 28).toString());
            IgstAmt = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 29).toString());
            RcmPer = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 30).toString());
            RcmAmt = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 31).toString());
            totalamt = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 32).toString());
//            ShotLotAmt =Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 20).toString());
                    
            if ((Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 11).toString())) > (Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 10).toString()))) {
                JOptionPane.showMessageDialog(null, "GR QTY is not allowed more than NET QTY");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShotLengthAmt = GRQty * rate * shortlengthper / 100;

        Table.setValueAt(String.valueOf(ShotLengthAmt), Table.getSelectedRow(), 17);

        GRGrossAmt = (GRQty * rate) - ShotLengthAmt;

        Table.setValueAt(String.valueOf(GRGrossAmt), Table.getSelectedRow(), 18);

        NetGRAmt = (GRGrossAmt - StockLotAmt - ReverseAmt);

        Table.setValueAt(String.valueOf(NetGRAmt), Table.getSelectedRow(), 22);

//        String QualityCode = Table.getValueAt(Table.getSelectedRow(), 5).toString();
        
        
        ShotLotAmt =  GRAmt*ShotLotPer/ 100;
        Table.setValueAt(String.valueOf(ShotLotAmt), Table.getSelectedRow(), 20);

           
        

//        System.out.println(QualityCode.substring(1, 3));
//        if ("18".equals(QualityCode.substring(1, 3))) {
//            Table.setValueAt("5112", Table.getSelectedRow(), 23);
//        } else {
//            Table.setValueAt("5515", Table.getSelectedRow(), 23);
//        }

        String mstate = "";
        mstate = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + txtPartyCode.getText().trim() + "' AND APPROVED=1 AND CANCELLED=0");

        if (mstate.equalsIgnoreCase("24")) {
            Table.setValueAt(2.5, Table.getSelectedRow(), 24);
            Table.setValueAt(2.5, Table.getSelectedRow(), 26);
        } else {
            Table.setValueAt(5, Table.getSelectedRow(), 28);
        }

        double d1 = 0, d2 = 0, d3 = 0;
        double s1 = 0, s2 = 0, s3 = 0;

        if (mstate.equalsIgnoreCase("24")) {
            d1 = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 22).toString());
            d2 = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 24).toString());
            d3 = d1 * d2 / 100;

            Table.setValueAt(d3, Table.getSelectedRow(), 25);
            Table.setValueAt(d3, Table.getSelectedRow(), 27);
        } else {

            s1 = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 22).toString());
            s2 = Double.parseDouble(Table.getValueAt(Table.getSelectedRow(), 28).toString());
            s3 = s1 * s2 / 100;
//         
            Table.setValueAt(s3, Table.getSelectedRow(), 29);
        }
        
        totalAmt =  NetGRAmt+d1+d2+s3;
        Table.setValueAt(String.valueOf(totalAmt), Table.getSelectedRow(), 32);
        
        Updating = false;
        
    }//GEN-LAST:event_TableKeyReleased

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        //   ReasonResetReadonly() ;       // TODO add your handling code here:
    }//GEN-LAST:event_TableMouseClicked

    private void jMenuIPartyCreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuIPartyCreditActionPerformed
        PartyCreditNoteReport();
    }//GEN-LAST:event_jMenuIPartyCreditActionPerformed

    private void jMenuIAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuIAccountActionPerformed
        ReportShow();
    }//GEN-LAST:event_jMenuIAccountActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed

    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        jPopupMenu.show(cmdPreview,0, 30);
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        if (TableUpdateHistory.getRowCount() > 0 && TableUpdateHistory.getSelectedRow() >= 0) {
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText.setText(TableUpdateHistory.getValueAt(TableUpdateHistory.getSelectedRow(), 4).toString());
            bigEdit.ShowEdit();
        } else {
            JOptionPane.showMessageDialog(frmSuitingGR.this, "Select a row from Document Update History");
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdBackToNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToNormalActionPerformed
        ObjFeltCreditNote.HistoryView = false;
        ObjFeltCreditNote.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdBackToNormalActionPerformed

    private void cmdViewRevisionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewRevisionsActionPerformed
        ObjFeltCreditNote.ShowHistory(EITLERPGLOBAL.formatDateDB(txtGRDate.getText()), txtGRID.getText());
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

    private void cmdNextToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab1ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNextToTab1ActionPerformed

    private void Tab1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab1FocusGained
        txtGRDate.requestFocus();
    }//GEN-LAST:event_Tab1FocusGained

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained
        ShowMessage("Enter the remarks for next user");
    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        ShowMessage("Select the user to whom document to be forwarded");
    }//GEN-LAST:event_cmbSendToFocusGained

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgHoldFocusGained

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgRejectFocusGained

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        ShowMessage("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void txtGRDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGRDateFocusGained
        ShowMessage("Enter Updation Date");
    }//GEN-LAST:event_txtGRDateFocusGained

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        finalapproved = "NO";
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
        finalapproved = "NO";
        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        OpgFinal.setSelected(true);
        OpgReject.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);
        finalapproved = "YES";
        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        //SetupApproval();
        finalapproved = "NO";
        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedSendToCombo();
            if (ApprovalFlow.IsOnceRejectedDoc(2, 757, txtGRID.getText())) {
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
        ObjFeltCreditNote.Close();
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
        if (JOptionPane.showConfirmDialog(frmSuitingGR.this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        if (Table.getRowCount() > 0) {
            DataModel.removeRow(Table.getSelectedRow());
        }
        FormatGridHSN_InvoiePst();
        GenerateHSNGRNData();
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed

        if (txtPartyCode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Party Code Detail First.");
            return;
        } else {

            Updating = true;
            Object[] rowData = new Object[50];
            rowData[0] = Integer.toString(Table.getRowCount() + 1);
            rowData[1] = "";
            rowData[2] = "";
            rowData[3] = "";
            rowData[4] = "";
            rowData[5] = "";
            rowData[6] = "";
            rowData[7] = "";
            rowData[8] = "";
            rowData[9] = "";
            rowData[10] = "";
            rowData[11] = "";
            rowData[12] = "";
            rowData[13] = "";
            rowData[14] = "";
            rowData[15] = "";
            rowData[16] = "";
            rowData[17] = "";
            rowData[18] = "";
            rowData[19] = "";
            rowData[20] = "";
            rowData[21] = "";
            rowData[22] = "";
            rowData[23] = "";
            rowData[24] = "";
            rowData[25] = "";
            rowData[26] = "";
            rowData[27] = "";
            rowData[28] = "";
            rowData[29] = "";
            rowData[30] = "";
            rowData[31] = "";
            rowData[32] = "";
            rowData[33] = "";
            rowData[34] = "";
            //rowData[35] = "";

            DataModel.addRow(rowData);
            //DataModelHSNINVOICEPOST.addRow(rowData);
            Updating = false;
            Table.changeSelection(Table.getRowCount() - 1, 1, false, false);
           // Table1.changeSelection(Table1.getRowCount() - 1, 1, false, false);
            Table.requestFocus();
           // Table1.requestFocus();

        }
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateSendToCombo();

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
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //            aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CODE NOT IN(SELECT DISTINCT(MM_PARTY_CODE) FROM DINESHMILLS.FELT_MACHINE_MASTER_HEADER )ORDER BY PARTY_NAME";
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210027' ORDER BY PARTY_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);
                txtPartyName.setText(clsSuitingGR.getParyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
                txtCity.setText(clsSuitingGR.getStation(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));

            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        try {
            if (!txtPartyCode.getText().equals("")) {
                String strSQL = "";
                ResultSet rsTmp;
                strSQL = "";
                strSQL += "SELECT PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + txtPartyCode.getText().trim() + "";
                rsTmp = data.getResult(strSQL);
                if (rsTmp.first()) {
                    rsTmp.first();
                    txtPartyName.setText(rsTmp.getString("PARTY_NAME"));
                    txtCity.setText(rsTmp.getString("CITY_ID"));
//                    txtInwardNo.requestFocus();
                }

            }
        } catch (Exception e) {

        }// TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void txtPartyCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyReleased
        //txtInwardNo.requestFocus();                // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCodeKeyReleased

    private void Table1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Table1MouseClicked

    private void Table1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Table1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_Table1FocusGained

    private void Table1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Table1KeyPressed

    private void Table1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_Table1KeyReleased

    private void cmdRemove1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemove1ActionPerformed
    if (Table1.getRowCount() > 0) {
            DataModelHSNINVOICEPOST.removeRow(Table1.getSelectedRow());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmdRemove1ActionPerformed

    private void cmdNextToTab3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab3ActionPerformed
        Tab.setSelectedIndex(2);        // TODO add your handling code here:
    }//GEN-LAST:event_cmdNextToTab3ActionPerformed

    private void Tab3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab3FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_Tab3FocusGained

    private void cmdBackToTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab2ActionPerformed
    Tab.setSelectedIndex(0);            // TODO add your handling code here:
    }//GEN-LAST:event_cmdBackToTab2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JMenuItem ShowInv;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab3;
    private javax.swing.JTable Table;
    private javax.swing.JTable Table1;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
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
    private javax.swing.JButton cmdRemove1;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewRevisions;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuIAccount;
    private javax.swing.JMenuItem jMenuIPartyCredit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPopupMenu mnuShow;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGRDate;
    private javax.swing.JTextField txtGRDesc;
    private javax.swing.JTextField txtGRID;
    private javax.swing.JTextField txtInwardDate;
    private javax.swing.JTextField txtInwardNo;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtToRemarks;
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
        txtGRDate.setEnabled(pStat);
        txtPartyCode.setEnabled(pStat);
        txtPartyName.setEnabled(pStat);
        txtCity.setEnabled(pStat);
        txtInwardNo.setEnabled(pStat);
        txtInwardDate.setEnabled(pStat);
        txtGRID.setEnabled(pStat);
        txtGRDesc.setEnabled(pStat);
        cmdAdd.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);
        cmdRemove1.setEnabled(pStat);
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
        txtGRDate.setText("");
        txtGRID.setText("");
        txtGRDesc.setText("");
        txtPartyCode.setText("");
        txtPartyName.setText("");
        txtCity.setText("");
        txtInwardDate.setText("");
        txtInwardNo.setText("");
        FormatGrid();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
    }

    //Display data on the Screen
    private void DisplayData() {
        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, 757)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }

        //=========== Title Bar Color Indication ===============//
        try {
            if (EditMode == 0) {
//                if (ObjFeltCreditNote.getAttribute("APPROVED").getInt() == 1) {
//                    lblTitle.setBackground(Color.BLUE);
//                } else {
//                    lblTitle.setBackground(Color.GRAY);
//                }

            }
            //============================================//

            String GRDate = EITLERPGLOBAL.formatDate(ObjFeltCreditNote.getAttribute("GR_DATE").getString());
            String GRID = ObjFeltCreditNote.getAttribute("GR_ID").getString();
            String GRDesc = ObjFeltCreditNote.getAttribute("GR_DESC_NO").getString();
            String PartyCode = ObjFeltCreditNote.getAttribute("PARTY_CODE").getString();
            String PartyName = ObjFeltCreditNote.getAttribute("PARTY_NAME").getString();
            String City = ObjFeltCreditNote.getAttribute("CITY").getString();
            String InwardNo = ObjFeltCreditNote.getAttribute("INWARD_NO").getString();
            String InwardDate = EITLERPGLOBAL.formatDate(ObjFeltCreditNote.getAttribute("INWARD_DATE").getString());

            lblTitle.setText(" SUITING GOODS RETURNS ENTRYN FORM - " + GRID);
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) ObjFeltCreditNote.getAttribute("HIERARCHY_ID").getVal());

            DoNotEvaluate = true;

            //FormatGrid();
            txtGRDate.setText(GRDate);
            txtGRID.setText(GRID);
            txtGRDesc.setText(GRDesc);
            txtPartyCode.setText(PartyCode);
            txtPartyName.setText(PartyName);
            txtCity.setText(City);
            txtInwardNo.setText(InwardNo);
            txtInwardDate.setText(InwardDate);

            FormatGrid();

            //Now Generate Table
            for (int i = 1; i <= ObjFeltCreditNote.hmFeltCreditNoteDetails.size(); i++) {
                clsSuitingGRDetails ObjFeltCreditNoteDetails = (clsSuitingGRDetails) ObjFeltCreditNote.hmFeltCreditNoteDetails.get(Integer.toString(i));

                Object[] rowData = new Object[45];

                rowData[0] = ObjFeltCreditNoteDetails.getAttribute("SR_NO").getString();
                rowData[1] = ObjFeltCreditNoteDetails.getAttribute("GR_PIECE_NO").getString();
                rowData[2] = ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_NO").getString();
                rowData[3] = EITLERPGLOBAL.formatDate(ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DATE").getString());
                rowData[4] = ObjFeltCreditNoteDetails.getAttribute("GR_LINK_NO").getString();
                rowData[5] = ObjFeltCreditNoteDetails.getAttribute("GR_QUALITY_NO").getString();
                rowData[6] = ObjFeltCreditNoteDetails.getAttribute("GR_SHADE").getString();
                rowData[7] = ObjFeltCreditNoteDetails.getAttribute("GR_UNIT_CODE").getString();
                rowData[8] = ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_QTY").getString();
                rowData[9] = ObjFeltCreditNoteDetails.getAttribute("FLAG").getString();
                rowData[10] = ObjFeltCreditNoteDetails.getAttribute("GR_NET_QTY").getString();
                rowData[11] = ObjFeltCreditNoteDetails.getAttribute("GR_QTY").getString();
                rowData[12] = ObjFeltCreditNoteDetails.getAttribute("GR_RATE").getString();
                rowData[13] = ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_DISC").getString();
                rowData[14] = ObjFeltCreditNoteDetails.getAttribute("GR_ADD_DISC").getString();
                rowData[15] = ObjFeltCreditNoteDetails.getAttribute("GR_INVOICE_AMOUNT").getString();
                rowData[16] = ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_PER").getString();
                rowData[17] = ObjFeltCreditNoteDetails.getAttribute("GR_SHOT_LENGTH_AMOUNT").getString();
                rowData[18] = ObjFeltCreditNoteDetails.getAttribute("GR_GROSS_AMOUNT").getString();
                rowData[19] = ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_PER").getString();
                rowData[20] = ObjFeltCreditNoteDetails.getAttribute("GR_STOCK_LOT_AMOUNT").getString();
                rowData[21] = ObjFeltCreditNoteDetails.getAttribute("GR_REVERSE_AMOUNT").getString();
                rowData[22] = ObjFeltCreditNoteDetails.getAttribute("GR_NET_GR_AMOUNT").getString();
                rowData[23] = ObjFeltCreditNoteDetails.getAttribute("GR_HSN_NO").getString();
                rowData[24] = ObjFeltCreditNoteDetails.getAttribute("GR_CGST_PER").getString();
                rowData[25] = ObjFeltCreditNoteDetails.getAttribute("GR_CGST_AMOUNT").getString();
                rowData[26] = ObjFeltCreditNoteDetails.getAttribute("GR_SGST_PER").getString();
                rowData[27] = ObjFeltCreditNoteDetails.getAttribute("GR_SGST_AMOUNT").getString();
                rowData[28] = ObjFeltCreditNoteDetails.getAttribute("GR_IGST_PER").getString();
                rowData[29] = ObjFeltCreditNoteDetails.getAttribute("GR_IGST_AMOUNT").getString();
                rowData[30] = ObjFeltCreditNoteDetails.getAttribute("GR_RCM_PER").getString();
                rowData[31] = ObjFeltCreditNoteDetails.getAttribute("GR_RCM_AMOUNT").getString();
                rowData[32] = ObjFeltCreditNoteDetails.getAttribute("GR_TOTAL_AMT").getString();
                //rowData[33] = ObjFeltCreditNoteDetails.getAttribute("GR_LINK_NO").getString();
                
                DataModel.addRow(rowData);
            }
            
            FormatGridHSN_InvoiePst();
             for (int i = 1; i <= ObjFeltCreditNote.hmFeltCreditNoteDetails1.size(); i++) {
                clsSuitingGRDetails ObjFeltCreditNoteDetails1 = (clsSuitingGRDetails) ObjFeltCreditNote.hmFeltCreditNoteDetails1.get(Integer.toString(i));
                
                 Object[] rowData = new Object[45];

                rowData[0] = ObjFeltCreditNoteDetails1.getAttribute("HSN_NO").getString();
                rowData[1] = ObjFeltCreditNoteDetails1.getAttribute("INVOICE_NO").getString();
                rowData[2] = EITLERPGLOBAL.formatDate(ObjFeltCreditNoteDetails1.getAttribute("INVOICE_DATE").getString());
                rowData[3] = ObjFeltCreditNoteDetails1.getAttribute("GR_QTY").getDouble();
                rowData[4] = ObjFeltCreditNoteDetails1.getAttribute("NET_AMOUNT").getDouble();
                rowData[5] = ObjFeltCreditNoteDetails1.getAttribute("NET_GR_AMOUNT").getDouble();
                rowData[6] = ObjFeltCreditNoteDetails1.getAttribute("CGST_PER").getDouble();
                rowData[7] = ObjFeltCreditNoteDetails1.getAttribute("CGST_AMOUNT").getDouble();
                rowData[8] = ObjFeltCreditNoteDetails1.getAttribute("SGST_PER").getDouble();
                rowData[9] = ObjFeltCreditNoteDetails1.getAttribute("SGST_AMOUNT").getDouble();
                rowData[10] = ObjFeltCreditNoteDetails1.getAttribute("IGST_PER").getDouble();
                rowData[11] = ObjFeltCreditNoteDetails1.getAttribute("IGST_AMOUNT").getDouble();
                rowData[12] = ObjFeltCreditNoteDetails1.getAttribute("RCM_PER").getDouble();
                rowData[13] = ObjFeltCreditNoteDetails1.getAttribute("RCM_AMOUNT").getDouble();
                rowData[14] = ObjFeltCreditNoteDetails1.getAttribute("TOTAL_AMOUNT").getDouble();
                rowData[15] = ObjFeltCreditNoteDetails1.getAttribute("LINK_NO").getString();
                
                DataModelHSNINVOICEPOST.addRow(rowData);
            }
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap hmList = new HashMap();

            hmList = ApprovalFlow.getDocumentFlow((int) EITLERPGLOBAL.gCompanyID, 757, GRID);
            for (int i = 1; i <= hmList.size(); i++) {
                //clsDocFlow is collection class used for holding approval flow data
                clsDocFlow ObjFlow = (clsDocFlow) hmList.get(Integer.toString(i));
                Object[] rowData = new Object[8];

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

            HashMap hmApprovalHistory = clsSuitingGR.getHistoryList(GRDate, GRID);
            for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                clsSuitingGR ObjFeltCreditNote = (clsSuitingGR) hmApprovalHistory.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = Integer.toString((int) ObjFeltCreditNote.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFeltCreditNote.getAttribute("UPDATED_BY").getVal());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjFeltCreditNote.getAttribute("ENTRY_DATE").getString());

                String ApprovalStatus = "";

                if ((ObjFeltCreditNote.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }

                if ((ObjFeltCreditNote.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if ((ObjFeltCreditNote.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }

                if ((ObjFeltCreditNote.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if ((ObjFeltCreditNote.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if ((ObjFeltCreditNote.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if ((ObjFeltCreditNote.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                rowData[3] = ApprovalStatus;
                rowData[4] = ObjFeltCreditNote.getAttribute("REJECTED_REMARKS").getString();

                DataModelUpdateHistory.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DoNotEvaluate = false;
    }

    private void FormatGrid() {
        try {
            cmdAdd.requestFocus();

            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            TableColumnModel ColModel = Table.getColumnModel();
            Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();

            //Add Columns to it
            DataModel.addColumn("Sr. No."); //0
            DataModel.addColumn("Piece No");//1
            DataModel.addColumn("Invoice No");//2
            DataModel.addColumn("Invoice Date");//3
            DataModel.addColumn("Link No");//4
            DataModel.addColumn("Quality");//5
            DataModel.addColumn("Shade");//6
            DataModel.addColumn("Unit Code ");//8
            DataModel.addColumn("Gross Qty ");//9
            DataModel.addColumn("Flag");//10
            DataModel.addColumn("Net Qty ");//11
            DataModel.addColumn("GR Qty ");//12
            DataModel.addColumn("Rate");//13
            DataModel.addColumn("Invoice Discount %");//14
            DataModel.addColumn("Add Discount %");//15
            DataModel.addColumn("Invoice Amount");//16
            DataModel.addColumn("Shot Length Percentage");//17
            DataModel.addColumn("Shot Length Amount");//18
            DataModel.addColumn("GR Gross Amount");//19
            DataModel.addColumn("Stock Lot Percentage");//20
            DataModel.addColumn("Stock Lot Amount");//21
            DataModel.addColumn("Reverse Amount");//22
            DataModel.addColumn("Net GR Amount");//23
            DataModel.addColumn("HSN No");//24
            DataModel.addColumn("CGST Per");//25
            DataModel.addColumn("CGST Amount");//26
            DataModel.addColumn("SGST Per");//27
            DataModel.addColumn("SGST Amount");//28
            DataModel.addColumn("IGST Per");//29
            DataModel.addColumn("IGST Amount");//30
            DataModel.addColumn("RCM Per");//31
            DataModel.addColumn("RCM Amount");//32
            DataModel.addColumn("GR Total Amount");//33
            
            DataModel.SetReadOnly(0);
            DataModel.SetReadOnly(1);
            DataModel.SetReadOnly(2);
            DataModel.SetReadOnly(3);
            DataModel.SetReadOnly(4);
            DataModel.SetReadOnly(5);
            DataModel.SetReadOnly(6);
            DataModel.SetReadOnly(7);
            DataModel.SetReadOnly(8);
            DataModel.SetReadOnly(9);
            DataModel.SetReadOnly(10);
            DataModel.SetReadOnly(12);
            DataModel.SetReadOnly(15);
            DataModel.SetReadOnly(17);
            DataModel.SetReadOnly(18);
            DataModel.SetReadOnly(20);
            DataModel.SetReadOnly(22);
            //DataModel.SetReadOnly(23);
            DataModel.SetReadOnly(24);
            DataModel.SetReadOnly(25);
            DataModel.SetReadOnly(26);
            DataModel.SetReadOnly(27);
            DataModel.SetReadOnly(28);
            DataModel.SetReadOnly(29);
            DataModel.SetReadOnly(30);
            DataModel.SetReadOnly(31);
            DataModel.SetReadOnly(32);
            
            
            if (EditMode != 0) {
                //------- Install Table List Selection Listener ------//
                Table.getColumnModel().getSelectionModel().addListSelectionListener(
                        new ListSelectionListener() {
                            public void valueChanged(ListSelectionEvent e) {
                                int column = Table.getSelectedColumn();
                                String strVar = DataModel.getVariable(column);
                                //=============== Cell Editing Routine =======================//
                                Table.editCellAt(Table.getSelectedRow(), column);
                                if (Table.getEditorComponent() instanceof JTextComponent) {
                                    ((JTextComponent) Table.getEditorComponent()).selectAll();
                                }
                                //============= Cell Editing Routine Ended =================//
                            }
                        });

                Table.getModel().addTableModelListener(new TableModelListener() {
                    public void tableChanged(TableModelEvent e) {
                        if (e.getType() == TableModelEvent.UPDATE) {
                            int row = Table.getSelectedRow();
                            int column = e.getColumn();

                            //=========== Cell Update Prevention Check ===========//
                            String curValue = ((String) Table.getValueAt(row, column)).trim();
                            if (curValue.equals("")) {
                                return;
                            }
                            //====================================================//
                            if (DoNotEvaluate) {
                                return;
                            }

                            //EITLERPGLOBAL.formatDate(ObjFlow.getAttribute("ACTION_DATE").getString())
                            if (column == 1) {

//                                else{
                                //12277/03 (0-7)  319907 (8-13)  005008 (14-19) 2016-12-22 (20-29) 5130710 (30-36)
//                                String PieceNo = ((String) Table.getValueAt(row, 1)).trim();
//                                String Party_Code=txtPartyCode.getText().trim();
//                                String Invoice_No = PieceNo.substring(14, 20);
//                                Table.setValueAt(PieceNo.substring(14, 20), row,2);
//                                Table.setValueAt(EITLERPGLOBAL.formatDate(PieceNo.substring(20, 30)), row, 3);
//                                String Invoice_Date = PieceNo.substring(20, 30); 
//                                Table.setValueAt(PieceNo.substring(0, 8), row, 1);
//                                PieceNo = PieceNo.substring(0, 8);
                                //12277/03 (0-7)  5130710 (8-14) 319907 (15-20) 2016-12-22 (21-30) 005008 (31) 
                                String PieceNo = ((String) Table.getValueAt(row, 1)).trim();
                                String Party_Code=txtPartyCode.getText().trim();
                                String Invoice_No = PieceNo.substring(31);
                                Table.setValueAt(PieceNo.substring(31), row,2);
                                Table.setValueAt(EITLERPGLOBAL.formatDate(PieceNo.substring(21, 31)), row, 3);
                                String Invoice_Date = PieceNo.substring(21, 31); 
                                Table.setValueAt(PieceNo.substring(0, 8), row, 1);
                                PieceNo = PieceNo.substring(0, 8);
                                //Table.setValueAt(ObjFeltCreditNote.getAlphaInvoiceNo(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 4);
                                Table.setValueAt(ObjFeltCreditNote.getLinkNo(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 4);
                                Table.setValueAt(ObjFeltCreditNote.getProductCode(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 5);
                                Table.setValueAt(ObjFeltCreditNote.getShade(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 6);
                                Table.setValueAt(ObjFeltCreditNote.getUnitCode(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 7);
                                Table.setValueAt(ObjFeltCreditNote.getGrossQty(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 8);
                                Table.setValueAt(ObjFeltCreditNote.getFlag(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 9);
                                Table.setValueAt(ObjFeltCreditNote.getNetQty(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 10);
                                Table.setValueAt(ObjFeltCreditNote.getRate(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 12);
                                Table.setValueAt(ObjFeltCreditNote.getInvoiceDisc(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 13);
                                Table.setValueAt(ObjFeltCreditNote.getAddDisc(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 14);
                                Table.setValueAt(ObjFeltCreditNote.getInvoiceAmt(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 15);
                                Table.setValueAt(ObjFeltCreditNote.getHsnNo(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 23);   
                                //Table.setValueAt(ObjFeltCreditNote.getLinkNo(PieceNo,Party_Code,Invoice_No,Invoice_Date), row, 33);    
                            }
                            
                            if (!Updating) {
                                FormatGridHSN_InvoiePst();
                                GenerateHSNGRNData();
                            }
                        }
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Enter Correct Details in Table. Error is : " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Generates Hierarchy Combo Box
    private void GenerateHierarchyCombo() {
        HashMap hmHierarchyList = new HashMap();

        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        hmHierarchyList = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=757 ");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            hmHierarchyList = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=757 ");
        }
        for (int i = 1; i <= hmHierarchyList.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
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
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();

                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    } else {
                        cmbSendToModel.addElement(aData);
                    }
                }
            } else {
                hmSendToList = ApprovalFlow.getRemainingUsers((int) EITLERPGLOBAL.gCompanyID, 757, ObjFeltCreditNote.getAttribute("GR_ID").getString());
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
                    IncludeUser = ApprovalFlow.IncludeUserInApproval((int) EITLERPGLOBAL.gCompanyID, 757, txtGRID.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInRejection((int) EITLERPGLOBAL.gCompanyID, 757, txtGRID.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (IncludeUser && (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID)) {
                    cmbSendToModel.addElement(aData);
                }
            } else {
                if (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID) {
                    cmbSendToModel.addElement(aData);
                }
            }

        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            int Creator = ApprovalFlow.getCreator((int) EITLERPGLOBAL.gCompanyID, 757, txtGRID.getText());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }
    }

    //Generates User Name Combo Box
    private void SetupApproval() {

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
            int FromUserID = ApprovalFlow.getFromID((int) EITLERPGLOBAL.gCompanyID, 757, ObjFeltCreditNote.getAttribute("GR_ID").getString());
            lnFromUserId = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = ApprovalFlow.getFromRemarks((int) EITLERPGLOBAL.gCompanyID, 757, FromUserID, ObjFeltCreditNote.getAttribute("GR_ID").getString());

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
            if (ApprovalFlow.IsCreator(757, txtGRID.getText())) {
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8045, 80451)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8045, 80452)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8045, 80453)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8045, 80454)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }

    private void Add() {
        //  EditMode=EITLERPGLOBAL.ADD;
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 757;

        if (aList.ShowList()) {
            EditMode = EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SetupApproval();
            SelPrefix = aList.Prefix; //Selected Prefix;
            SelSuffix = aList.Suffix;
            FFNo = aList.FirstFreeNo;
            txtGRID.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 757, FFNo, false));
            txtGRDate.requestFocus();
            lblTitle.setText("SUITING GOODS RETURNS - " + txtGRID.getText());
            System.out.println(txtGRID.getText());
            lblTitle.setBackground(Color.BLUE);
        } else {
            JOptionPane.showMessageDialog(null, "You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }

    }

    private void Edit() {

        String productionDocumentNo = (String) ObjFeltCreditNote.getAttribute("GR_ID").getObj();
       
        if (ObjFeltCreditNote.IsEditable(EITLERPGLOBAL.gCompanyID, productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            
            EditMode = EITLERPGLOBAL.EDIT;

            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            //ReasonResetReadonly();
            if (ApprovalFlow.IsCreator(757, productionDocumentNo)) {
                SetFields(true);

            } else {

                EnableApproval();
            }
        } else {
            JOptionPane.showMessageDialog(frmSuitingGR.this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "EDITING ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Delete() {
        if (ObjFeltCreditNote.CanDelete(txtGRID.getText(), txtGRDate.getText(), EITLERPGLOBAL.gNewUserID)) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(frmSuitingGR.this, ObjFeltCreditNote.LastError, "DELETION ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     */    
    private void Save() {

        String GRId, GRDate, GRDesc, GRPartyCode, GRPartyName, GRCity, GRInwardNo, GRInwardDate,CompanyId;

        int i = 0, j = 0;
        String SrNoString = "", PieceNoString = "", InvoiceNoString = "", Invoice_DateString = "", AlphaInvoieNoString = "", QualityNoString = "", ShadString = "", UnitCodeString = "";
        String GrossQtyString = "", NetQtyString = "", RateString = "", InvoiceDiscString = "", AddDiscString = "", FlagString = "", GRQtyString = "";
        String InvoiceAmountString = "", ShotLengthPerString = "", ShotLengthAmtString = "", GRGrossAmountString = "", StockLotPerString = "";
        String StockLotAmtString = "", ReverseAmtString = "", NetGRAmtString = "", HSNNOString = "", CGSTPerString = "", SGSTPerString = "", IGSTPerString = "", CGSTAmtString = "", SGSTAmtString = "", IGSTAmtString = "", GSTCompositionPerString = "", GSTCompositionAmtString = "";
        String HSNnoSting="",InvoicenoString="",InvoiceDateString="",GrossString="",TotalamtString="",LinkNoString="",linkno="";
        double cgstAmt=0,sgstAmt=0,igstAmt=0,RCMamt=0,NetAmt=0,NetGRAmt=0,GrQty=0,Totalamt=0,CGSTper=0,SGSTper=0,IGSTper=0,RCMper=0;
        
        
        
        //        int DeptID =  EITLERPGLOBAL.gUserDeptID;
//        CompanyId=(EITLERPGLOBAL.gCompanyID);
        GRDate = txtGRDate.getText().trim();
        GRId = txtGRID.getText().trim();
        GRDesc = txtGRDesc.getText().trim();
        GRPartyCode = txtPartyCode.getText().trim();
        GRPartyName = txtPartyName.getText().trim();
        GRCity = txtCity.getText().trim();
        GRInwardNo = txtInwardNo.getText().trim();
        GRInwardDate = txtInwardDate.getText().trim();

        if (Table.getRowCount() <= 0) {
            return;
        }

        try {

            ObjFeltCreditNote.hmFeltCreditNoteDetails.clear();

            //Check the entered details in Table.
            for (i = 0; i <= Table.getRowCount() - 1; i++) {

                // Piece no Validation before Saving
                j++;
                SrNoString = ((String) Table.getValueAt(i, 0)).trim();
                j++;
                PieceNoString = ((String) Table.getValueAt(i, 1)).trim();
                j++;
                InvoiceNoString = ((String) Table.getValueAt(i, 2)).trim().toUpperCase();
                j++;
                Invoice_DateString = ((String) Table.getValueAt(i, 3)).trim();
                j++;
                LinkNoString = ((String) Table.getValueAt(i, 4)).trim();
                j++;
                QualityNoString = ((String) Table.getValueAt(i, 5)).trim();
                j++;
                ShadString = ((String) Table.getValueAt(i, 6)).trim();
                j++;
                UnitCodeString = ((String) Table.getValueAt(i, 7)).trim();
                j++;
                GrossQtyString = ((String) Table.getValueAt(i, 8)).trim();
                j++;
                FlagString = ((String) Table.getValueAt(i, 9)).trim();
                j++;
                NetQtyString = ((String) Table.getValueAt(i, 10)).trim();
                j++;
                GRQtyString = ((String) Table.getValueAt(i, 11)).trim();
                j++;
                RateString = ((String) Table.getValueAt(i, 12)).trim();
                j++;
                InvoiceDiscString = ((String) Table.getValueAt(i, 13)).trim();
                j++;
                AddDiscString = ((String) Table.getValueAt(i, 14)).trim();
                j++;
                InvoiceAmountString = ((String) Table.getValueAt(i, 15)).trim();
                j++;
                ShotLengthPerString = ((String) Table.getValueAt(i, 16)).trim();
                j++;
                ShotLengthAmtString = ((String) Table.getValueAt(i, 17)).trim();
                j++;
                GRGrossAmountString = ((String) Table.getValueAt(i, 18)).trim();
                j++;
                StockLotPerString = ((String) Table.getValueAt(i, 19)).trim();
                j++;
                StockLotAmtString = ((String) Table.getValueAt(i, 20)).trim();
                j++;
                ReverseAmtString = ((String) Table.getValueAt(i, 21)).trim();
                j++;
                NetGRAmtString = ((String) Table.getValueAt(i, 22)).trim();
                j++;
                HSNNOString = ((String) Table.getValueAt(i, 23)).trim();
                j++;
                CGSTPerString = Table.getValueAt(i, 24)+"";
                j++;
                CGSTAmtString = Table.getValueAt(i, 25)+"";
                j++;
                SGSTPerString = Table.getValueAt(i, 26)+"";
                j++;
                SGSTAmtString = Table.getValueAt(i, 27)+"";
                j++;
                IGSTPerString = Table.getValueAt(i, 28)+"";
                j++;
                IGSTAmtString = Table.getValueAt(i, 29)+"";
                j++;
                GSTCompositionPerString = ((String) Table.getValueAt(i, 30)).trim();
                j++;
                GSTCompositionAmtString = ((String) Table.getValueAt(i, 31)).trim();
                j++;
                TotalamtString = ((String) Table.getValueAt(i, 32)).trim();
                
                

                clsSuitingGRDetails ObjFeltCreditNoteDetails = new clsSuitingGRDetails();

                ObjFeltCreditNoteDetails.setAttribute("SR_NO", SrNoString);//0
                ObjFeltCreditNoteDetails.setAttribute("GR_PIECE_NO", PieceNoString);//1
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_NO", InvoiceNoString);//2
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_DATE", EITLERPGLOBAL.formatDateDB(Invoice_DateString));//3
                //ObjFeltCreditNoteDetails.setAttribute("GR_ALPHA_INVOICE_NO", AlphaInvoieNoString);//4
                ObjFeltCreditNoteDetails.setAttribute("GR_QUALITY_NO", QualityNoString);///5
                ObjFeltCreditNoteDetails.setAttribute("GR_SHADE", ShadString);//6
                ObjFeltCreditNoteDetails.setAttribute("GR_UNIT_CODE", UnitCodeString);//7
                ObjFeltCreditNoteDetails.setAttribute("GR_GROSS_QTY", GrossQtyString);//8
                ObjFeltCreditNoteDetails.setAttribute("FLAG", FlagString);//9
                ObjFeltCreditNoteDetails.setAttribute("GR_NET_QTY", NetQtyString);//10
                ObjFeltCreditNoteDetails.setAttribute("GR_QTY", GRQtyString);//11
                ObjFeltCreditNoteDetails.setAttribute("GR_RATE", RateString);//12
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_DISC", InvoiceDiscString);//13
                ObjFeltCreditNoteDetails.setAttribute("GR_ADD_DISC", AddDiscString);//14
                ObjFeltCreditNoteDetails.setAttribute("GR_INVOICE_AMOUNT", InvoiceAmountString);//15
                ObjFeltCreditNoteDetails.setAttribute("GR_SHOT_LENGTH_PER", ShotLengthPerString);//16
                ObjFeltCreditNoteDetails.setAttribute("GR_SHOT_LENGTH_AMOUNT", ShotLengthAmtString);//17
                ObjFeltCreditNoteDetails.setAttribute("GR_GROSS_AMOUNT", GRGrossAmountString);//18
                ObjFeltCreditNoteDetails.setAttribute("GR_STOCK_LOT_PER", StockLotPerString);//19
                ObjFeltCreditNoteDetails.setAttribute("GR_STOCK_LOT_AMOUNT", StockLotAmtString);//20
                ObjFeltCreditNoteDetails.setAttribute("GR_REVERSE_AMOUNT", ReverseAmtString);//21
                ObjFeltCreditNoteDetails.setAttribute("GR_NET_GR_AMOUNT", NetGRAmtString);//22
                ObjFeltCreditNoteDetails.setAttribute("GR_HSN_NO", HSNNOString);//23
                ObjFeltCreditNoteDetails.setAttribute("GR_CGST_PER", CGSTPerString);//24
                ObjFeltCreditNoteDetails.setAttribute("GR_CGST_AMOUNT", CGSTAmtString);//25
                ObjFeltCreditNoteDetails.setAttribute("GR_SGST_PER", SGSTPerString);//26
                ObjFeltCreditNoteDetails.setAttribute("GR_SGST_AMOUNT", SGSTAmtString);//27
                ObjFeltCreditNoteDetails.setAttribute("GR_IGST_PER", IGSTPerString);//28
                ObjFeltCreditNoteDetails.setAttribute("GR_IGST_AMOUNT", IGSTAmtString);//29
                ObjFeltCreditNoteDetails.setAttribute("GR_RCM_PER", GSTCompositionPerString);//30
                ObjFeltCreditNoteDetails.setAttribute("GR_RCM_AMOUNT", GSTCompositionAmtString);//31
                ObjFeltCreditNoteDetails.setAttribute("GR_TOTAL_AMT", TotalamtString);//31
                ObjFeltCreditNoteDetails.setAttribute("GR_LINK_NO", LinkNoString);//31
                
                ObjFeltCreditNote.hmFeltCreditNoteDetails.put(Integer.toString(ObjFeltCreditNote.hmFeltCreditNoteDetails.size() + 1), ObjFeltCreditNoteDetails);

            }
        } catch (Exception e) {
            e.printStackTrace();
        };
        
        try {
            
        ObjFeltCreditNote.hmFeltCreditNoteDetails1.clear();
        
        for (i = 0; i <= Table1.getRowCount() - 1; i++) {

                // Piece no Validation before Saving
                j++;
                HSNnoSting = ((String) Table1.getValueAt(i, 0)).trim();
                j++;
                InvoicenoString = ((String) Table1.getValueAt(i, 1)).trim().toUpperCase();
                j++;
                InvoiceDateString = ((String) Table1.getValueAt(i, 2)).trim();
                j++;
                GrQty = ((Double)Table1.getValueAt(i, 3));
                j++;
                NetAmt = ((Double)Table1.getValueAt(i, 4));
                j++;
                NetGRAmt = ((Double)Table1.getValueAt(i, 5));
                j++;
                CGSTper = ((Double)Table1.getValueAt(i, 6));
                j++;
                cgstAmt = ((Double)Table1.getValueAt(i, 7));
                j++;
                SGSTper = ((Double)Table1.getValueAt(i, 8));
                j++;
                sgstAmt = ((Double)Table1.getValueAt(i, 9));
                j++;
                IGSTper = ((Double)Table1.getValueAt(i, 10));
                j++;
                igstAmt = ((Double)Table1.getValueAt(i, 11));
                j++;
                RCMper = ((Double)Table1.getValueAt(i, 12));
                j++;
                RCMamt = ((Double)Table1.getValueAt(i, 13));
                j++;
                Totalamt = ((Double)Table1.getValueAt(i, 14));
                j++;
                linkno = ((String)Table1.getValueAt(i, 15)).trim();
                
                clsSuitingGRDetails ObjFeltCreditNoteDetails1 = new clsSuitingGRDetails();

                ObjFeltCreditNoteDetails1.setAttribute("HSN_NO", HSNnoSting);//0
                ObjFeltCreditNoteDetails1.setAttribute("INVOICE_NO", InvoicenoString);//1
                ObjFeltCreditNoteDetails1.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDateDB(InvoiceDateString));//2
                ObjFeltCreditNoteDetails1.setAttribute("GR_QTY", GrQty);//3
                ObjFeltCreditNoteDetails1.setAttribute("NET_AMOUNT", NetAmt);//3
                ObjFeltCreditNoteDetails1.setAttribute("NET_GR_AMOUNT", NetGRAmt);///4
                ObjFeltCreditNoteDetails1.setAttribute("CGST_PER", CGSTper);//5
                ObjFeltCreditNoteDetails1.setAttribute("CGST_AMOUNT", cgstAmt);//5
                ObjFeltCreditNoteDetails1.setAttribute("SGST_PER", SGSTper);//5
                ObjFeltCreditNoteDetails1.setAttribute("SGST_AMOUNT", sgstAmt);//5
                ObjFeltCreditNoteDetails1.setAttribute("IGST_PER", IGSTper);//5
                ObjFeltCreditNoteDetails1.setAttribute("IGST_AMOUNT", igstAmt);//5
                ObjFeltCreditNoteDetails1.setAttribute("RCM_PER", RCMper);//5
                ObjFeltCreditNoteDetails1.setAttribute("RCM_AMOUNT", RCMamt);//5
                ObjFeltCreditNoteDetails1.setAttribute("TOTAL_AMOUNT", Totalamt);//8
                ObjFeltCreditNoteDetails1.setAttribute("LINK_NO", linkno);//8
                
                ObjFeltCreditNote.hmFeltCreditNoteDetails1.put(Integer.toString(ObjFeltCreditNote.hmFeltCreditNoteDetails1.size() + 1), ObjFeltCreditNoteDetails1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        };
        
        
        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(frmSuitingGR.this, "Select the hierarchy.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(frmSuitingGR.this, "Select the Approval Action.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(frmSuitingGR.this, "Enter the remarks for rejection", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(frmSuitingGR.this, "Select the user, to whom rejected document to be send", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(int k=0;k<=Table.getRowCount()-1;k++) {
            for(int l=k;l<=Table.getRowCount()-1;l++){
                if(l!=k && ((String)Table.getValueAt(k, 1)).trim().equals(((String)Table.getValueAt(l, 1)).trim()) ){
                    JOptionPane.showMessageDialog(this, "Same Piece No at Row "+(k+1)+" and "+(l+1),"ERROR",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        //set data for insert/update
        ObjFeltCreditNote.setAttribute("GR_DATE", GRDate);
        ObjFeltCreditNote.setAttribute("GR_ID", GRId);
        ObjFeltCreditNote.setAttribute("GR_DESC_NO", GRDesc);
        ObjFeltCreditNote.setAttribute("PARTY_CODE", GRPartyCode);
        ObjFeltCreditNote.setAttribute("PARTY_NAME", GRPartyName);
        ObjFeltCreditNote.setAttribute("CITY", GRCity);
        ObjFeltCreditNote.setAttribute("INWARD_NO", GRInwardNo);
        ObjFeltCreditNote.setAttribute("INWARD_DATE", GRInwardDate);
        

        SetData();

        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjFeltCreditNote.Insert()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(frmSuitingGR.this, "Error occured while saving. Error is " + ObjFeltCreditNote.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjFeltCreditNote.Update()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(frmSuitingGR.this, "Error occured while saving editing. Error is " + ObjFeltCreditNote.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
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

    //Sets data to the Details Class Object    
    //Sets data to the Details Class Object    
    //Sets data to the Details Class Object
    private void SetData() {

        //-------- Update Approval Specific Fields -----------//
        ObjFeltCreditNote.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjFeltCreditNote.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjFeltCreditNote.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjFeltCreditNote.setAttribute("REJECTED_REMARKS", txtToRemarks.getText().trim());
        ObjFeltCreditNote.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);

        if (OpgApprove.isSelected()) {
            ObjFeltCreditNote.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjFeltCreditNote.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjFeltCreditNote.setAttribute("APPROVAL_STATUS", "R");
            ObjFeltCreditNote.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjFeltCreditNote.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjFeltCreditNote.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
        } else {
            ObjFeltCreditNote.setAttribute("CREATED_BY", (int) ObjFeltCreditNote.getAttribute("CREATED_BY").getVal());
            ObjFeltCreditNote.setAttribute("CREATED_DATE", ObjFeltCreditNote.getAttribute("CREATED_DATE").getString());
            ObjFeltCreditNote.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
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
        Loader ObjLoader = new Loader(this, "EITLERP.Suitings.SuitingGR.frmFindSuitingGR", true);
        frmFindSuitingGR ObjFindFeltPacking = (frmFindSuitingGR) ObjLoader.getObj();

        if (ObjFindFeltPacking.Cancelled == false) {
            if (!ObjFeltCreditNote.Filter(ObjFindFeltPacking.stringFindQuery)) {
                JOptionPane.showMessageDialog(frmSuitingGR.this, " No records found.", "Find Suiting Goods Returns Details", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    // find details by production date
    public void Find(String AmendID) {
        ObjFeltCreditNote.Filter(" GR_ID='" + AmendID + "'");
        SetMenuForRights();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pPartyCode) {
        ObjFeltCreditNote.Filter(" GR_ID='" + pPartyCode + "'");
        //System.out.print("Party Code = "+pPartyCode);
        ObjFeltCreditNote.MoveLast();
        DisplayData();
    }

    // find all pending document
    public void FindWaiting() {

        ObjFeltCreditNote.Filter("AND GR_ID IN (SELECT  DINESHMILLS.FELT_CN_TEMP_HEADER.GR_ID FROM  DINESHMILLS.FELT_CN_TEMP_HEADER,DINESHMILLS.D_COM_DOC_DATA WHERE  DINESHMILLS.FELT_CN_TEMP_HEADER.GR_ID=DINESHMILLS.D_COM_DOC_DATA.DOC_NO AND DINESHMILLS.D_COM_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND DINESHMILLS.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=757");
        SetMenuForRights();
        DisplayData();
    }

    private void MoveFirst() {
        ObjFeltCreditNote.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjFeltCreditNote.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjFeltCreditNote.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjFeltCreditNote.MoveLast();
        DisplayData();
    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(" " + pMessage);
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
        Table.setEnabled(true);
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
    }

    private void FormatGridHSN_InvoiePst() {
           Updating = true;
            
            DataModelHSNINVOICEPOST = new EITLTableModel();
            Table1.removeAll();

            Table1.setModel(DataModelHSNINVOICEPOST);
            TableColumnModel ColModel = Table.getColumnModel();
            Table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();

            //Add Columns to it
            DataModelHSNINVOICEPOST.addColumn("HSN No");//1
            DataModelHSNINVOICEPOST.addColumn("Invoice No");//2
            DataModelHSNINVOICEPOST.addColumn("Invoice Date");//3
            DataModelHSNINVOICEPOST.addColumn("GR Qty");//5
            DataModelHSNINVOICEPOST.addColumn("Net Amount");//5
            DataModelHSNINVOICEPOST.addColumn("Net GR Amount");//6
            DataModelHSNINVOICEPOST.addColumn("CGST Per");//7
            DataModelHSNINVOICEPOST.addColumn("CGST Amount");//7
            DataModelHSNINVOICEPOST.addColumn("SGST Per");//7
            DataModelHSNINVOICEPOST.addColumn("SGST Amount");//8
            DataModelHSNINVOICEPOST.addColumn("IGST Per");//7
            DataModelHSNINVOICEPOST.addColumn("IGST Amount");//9
            DataModelHSNINVOICEPOST.addColumn("RCM Per");//10
            DataModelHSNINVOICEPOST.addColumn("RCM Amount");//10
            DataModelHSNINVOICEPOST.addColumn("Total Amount");//10
            DataModelHSNINVOICEPOST.addColumn("Link No");//10
            
            Table1.getColumnModel().getColumn(1).setMinWidth(100);
            Table1.getColumnModel().getColumn(2).setMinWidth(100);
            Table1.getColumnModel().getColumn(3).setMinWidth(100);
            Table1.getColumnModel().getColumn(4).setMinWidth(100);
            Table1.getColumnModel().getColumn(5).setMinWidth(100);
            Table1.getColumnModel().getColumn(6).setMinWidth(100);
            Table1.getColumnModel().getColumn(7).setMinWidth(100);
            Table1.getColumnModel().getColumn(8).setMinWidth(100);
            Table1.getColumnModel().getColumn(9).setMinWidth(100);
            Table1.getColumnModel().getColumn(10).setMinWidth(100);
            Table1.getColumnModel().getColumn(11).setMinWidth(100);
            
            
            DataModelHSNINVOICEPOST.SetReadOnly(0);
            DataModelHSNINVOICEPOST.SetReadOnly(1);
            DataModelHSNINVOICEPOST.SetReadOnly(2);
            DataModelHSNINVOICEPOST.SetReadOnly(3);
            DataModelHSNINVOICEPOST.SetReadOnly(4);
            DataModelHSNINVOICEPOST.SetReadOnly(5);
            DataModelHSNINVOICEPOST.SetReadOnly(6);
            DataModelHSNINVOICEPOST.SetReadOnly(7);
            DataModelHSNINVOICEPOST.SetReadOnly(8);
            DataModelHSNINVOICEPOST.SetReadOnly(9);
            DataModelHSNINVOICEPOST.SetReadOnly(10);
            DataModelHSNINVOICEPOST.SetReadOnly(11);
            Updating = false;
        
    }
    
      private void GenerateHSNGRNData() {
        try {
            String hsnCd = "";
            String InvoiceNo="";
            int aCnt = 0;
            int bCnt = 0;
            
            for (int i = 0; i < Table.getRowCount(); i++) {
                hsnCd = DataModel.getValueAt(i, 23).toString()+DataModel.getValueAt(i, 2).toString() ;
                if (hsnCd != null || !hsnCd.equals("") || hsnCd.length() > 0) {
                    a[aCnt] = DataModel.getValueAt(i, 23).toString()+DataModel.getValueAt(i, 2).toString();
                    System.out.println("A Count : " + aCnt);
                    aCnt++;
                }
            }
            
            if (aCnt > 0) {
                for (int s = 0; s < aCnt; s++) {
                    //System.out.println("S Count : " + s + " : " + a[s]);
                    for (int m = s + 1; m < aCnt; m++) {
                        if (a[s] != null && a[s].equals(a[m])) {
                            System.out.println("M Count : " + m + " : " + a[m]);
                            a[m] = null;
                            System.out.println("Value of M : " + m + " : " + a[m]);
                        }
                    }
                }
                
                for (int p = 0; p < aCnt; p++) {
                    if (a[p] != null) {
                        b[bCnt] = a[p];
                        System.out.println("b[" + bCnt + "] : " + b[bCnt]);
                        bCnt++;
                    }
                }
                
                for (int i = 0; i < bCnt; i++) {
   
                     netAmt=0;
                     netgrAmt=0;
                     CGSTAmt=0;
                     SGSTAmt=0;
                     IGSTAmt=0;
                     GRQty=0;
                     TotalAmt=0;
                     CGSTPer=0;
                     SGSTPer=0;
                     IGSTPer=0;
                     RCMPer=0;
                    
                    HsnNo =0;
                    RCMAmt = 0;
                    
                    for (int j = 0; j < Table.getRowCount(); j++) {
                        hsnCode = DataModel.getValueAt(j, 23).toString()+DataModel.getValueAt(j, 2).toString();
                        if (b[i].equals(hsnCode)) {
                            hcd = DataModel.getValueAt(j, 23).toString();
                            icd = DataModel.getValueAt(j, 2).toString();
                            idt = DataModel.getValueAt(j, 3).toString();
                            GRQty += Double.valueOf(DataModel.getValueAt(j, 11).toString());
                            netAmt += Double.valueOf(DataModel.getValueAt(j, 15).toString());
                            netgrAmt += Double.valueOf(DataModel.getValueAt(j, 22).toString());
                            CGSTPer = Double.valueOf(DataModel.getValueAt(j, 24).toString());
                            CGSTAmt += Double.valueOf(DataModel.getValueAt(j, 25).toString());
                            SGSTPer = Double.valueOf(DataModel.getValueAt(j, 26).toString());
                            SGSTAmt += Double.valueOf(DataModel.getValueAt(j, 27).toString());
                            IGSTPer = Double.valueOf(DataModel.getValueAt(j, 28).toString());
                            IGSTAmt += Double.valueOf(DataModel.getValueAt(j, 29).toString());
                            RCMPer = Double.valueOf(DataModel.getValueAt(j, 30).toString());
                            RCMAmt += Double.valueOf(DataModel.getValueAt(j, 31).toString());
                            TotalAmt += Double.valueOf(DataModel.getValueAt(j, 32).toString());
                            LINKno = DataModel.getValueAt(j, 33).toString();
                        }
                    }
                    Object[] rowData2 = new Object[25];
                    rowData2[0] = hcd;
                    rowData2[1] = icd;
                    rowData2[2] = idt;
                    rowData2[3] = GRQty;
                    rowData2[4] = netAmt;
                    rowData2[5] = netgrAmt;
                    rowData2[6] = CGSTPer;
                    rowData2[7] = CGSTAmt;
                    rowData2[8] = SGSTPer;
                    rowData2[9] = SGSTAmt;
                    rowData2[10] = IGSTPer;
                    rowData2[11] = IGSTAmt;
                    rowData2[12] = RCMPer;
                    rowData2[13] = RCMAmt;
                    rowData2[14] = TotalAmt;
                    rowData2[15] = LINKno;
                    
                    DataModelHSNINVOICEPOST.addRow(rowData2);
                }
                
            } //End of Array with Record
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      private void ReportShow() {
        
        try {
            
            
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            
            objReportData.AddColumn("GRCN_NO");
            objReportData.AddColumn("GRCN_DATE");
            objReportData.AddColumn("GR_ID");
            objReportData.AddColumn("GR_DATE");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("PARTY_NAME");
            objReportData.AddColumn("HSN_NO");
            objReportData.AddColumn("INVOICE_NO");
            objReportData.AddColumn("INVOICE_DATE");
            objReportData.AddColumn("NET_AMOUNT");
            objReportData.AddColumn("NET_GR_AMOUNT");
            objReportData.AddColumn("CGST_PER");
            objReportData.AddColumn("CGST_AMOUNT");
            objReportData.AddColumn("SGST_PER");
            objReportData.AddColumn("SGST_AMOUNT");
            objReportData.AddColumn("IGST_PER");
            objReportData.AddColumn("IGST_AMOUNT");
            objReportData.AddColumn("RCM_PER");
            objReportData.AddColumn("RCM_AMOUNT");
            objReportData.AddColumn("TOTAL_AMOUNT");
            objReportData.AddColumn("LINK_NO");
            objReportData.AddColumn("INWARD_NO");
            objReportData.AddColumn("INWARD_DATE");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("GRCN_NO","");
            objOpeningRow.setValue("GRCN_DATE","");
            objOpeningRow.setValue("GR_ID","");
            objOpeningRow.setValue("GR_DATE","");
            objOpeningRow.setValue("PARTY_CODE","");
            objOpeningRow.setValue("PARTY_NAME","");
            objOpeningRow.setValue("HSN_NO","");
            objOpeningRow.setValue("INVOICE_NO","");
            objOpeningRow.setValue("INVOICE_DATE","");
            objOpeningRow.setValue("NET_AMOUNT","");
            objOpeningRow.setValue("NET_GR_AMOUNT","");
            objOpeningRow.setValue("CGST_PER","");
            objOpeningRow.setValue("CGST_AMOUNT","");
            objOpeningRow.setValue("SGST_PER","");
            objOpeningRow.setValue("SGST_AMOUNT","");
            objOpeningRow.setValue("IGST_PER","");
            objOpeningRow.setValue("IGST_AMOUNT","");
            objOpeningRow.setValue("RCM_PER","");
            objOpeningRow.setValue("RCM_AMOUNT","");
            objOpeningRow.setValue("TOTAL_AMOUNT","");
            objOpeningRow.setValue("LINK_NO","");
            objOpeningRow.setValue("INWARD_NO","");
            objOpeningRow.setValue("INWARD_DATE","");
            
            
            String strSQL="SELECT A.GR_ID,A.GR_DATE,A.PARTY_CODE,A.PARTY_NAME,A.CITY,A.INWARD_NO,A.INWARD_DATE,A.GRCN_NO,A.GRCN_DATE,B.HSN_NO,B.INVOICE_NO,B.INVOICE_DATE,B.NET_AMOUNT,B.NET_GR_AMOUNT,B.CGST_PER,B.CGST_AMOUNT, B.SGST_PER,B.SGST_AMOUNT,B.IGST_PER,B.IGST_AMOUNT,B.RCM_PER,B.RCM_AMOUNT,B.TOTAL_AMOUNT,B.LINK_NO FROM STGSALES.D_STG_GOODS_RETURNS_HEADER A,STGSALES.D_SAL_HSN_INVOICE_POSTING B WHERE A.GR_ID=B.GR_ID AND A.GR_ID='"+ txtGRID.getText()+"'";
            
            System.out.println(strSQL);
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("GR_ID",UtilFunctions.getString(rsTmp,"GR_ID",""));
                    objRow.setValue("GR_DATE",UtilFunctions.getString(rsTmp,"GR_DATE",""));
                    objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                    objRow.setValue("PARTY_NAME",UtilFunctions.getString(rsTmp,"PARTY_NAME",""));
                    objRow.setValue("HSN_NO",UtilFunctions.getString(rsTmp,"HSN_NO",""));
                    objRow.setValue("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objRow.setValue("INVOICE_DATE",UtilFunctions.getString(rsTmp,"INVOICE_DATE",""));
                    objRow.setValue("NET_AMOUNT",UtilFunctions.getString(rsTmp,"NET_AMOUNT",""));
                    objRow.setValue("NET_GR_AMOUNT",UtilFunctions.getString(rsTmp,"NET_GR_AMOUNT",""));
                    objRow.setValue("CGST_PER",UtilFunctions.getString(rsTmp,"CGST_PER",""));
                    objRow.setValue("CGST_AMOUNT",UtilFunctions.getString(rsTmp,"CGST_AMOUNT",""));
                    objRow.setValue("SGST_PER",UtilFunctions.getString(rsTmp,"SGST_PER",""));
                    objRow.setValue("SGST_AMOUNT",UtilFunctions.getString(rsTmp,"SGST_AMOUNT",""));
                    objRow.setValue("IGST_PER",UtilFunctions.getString(rsTmp,"IGST_PER",""));
                    objRow.setValue("IGST_AMOUNT",UtilFunctions.getString(rsTmp,"IGST_AMOUNT",""));
                    objRow.setValue("RCM_PER",UtilFunctions.getString(rsTmp,"RCM_PER",""));
                    objRow.setValue("RCM_AMOUNT",UtilFunctions.getString(rsTmp,"RCM_AMOUNT",""));
                    objRow.setValue("TOTAL_AMOUNT",UtilFunctions.getString(rsTmp,"TOTAL_AMOUNT",""));
                    objRow.setValue("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                    objRow.setValue("INWARD_NO",UtilFunctions.getString(rsTmp,"INWARD_NO",""));
                    objRow.setValue("INWARD_DATE",UtilFunctions.getString(rsTmp,"INWARD_DATE",""));
                    objRow.setValue("GRCN_NO",UtilFunctions.getString(rsTmp,"GRCN_NO",""));
                    objRow.setValue("GRCN_DATE",UtilFunctions.getString(rsTmp,"GRCN_DATE",""));
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            
            HashMap Parameters=new HashMap();
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/sales/stg/rptGR.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
      
      private void PartyCreditNoteReport() {
        
        try {
            
            
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("GRCN_NO");
            objReportData.AddColumn("GRCN_DATE");
            objReportData.AddColumn("PARTY_NAME");
            objReportData.AddColumn("ADDRESS1");
            objReportData.AddColumn("ADDRESS2");
            objReportData.AddColumn("CITY_ID");
            objReportData.AddColumn("PINCODE");
            objReportData.AddColumn("GSTIN_NO");
            objReportData.AddColumn("PARTY_STATE");
            objReportData.AddColumn("STATE_GST_CODE");
            objReportData.AddColumn("GR_DESC_NO");
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("GR_INVOICE_NO");
            objReportData.AddColumn("GR_INVOICE_DATE");
            objReportData.AddColumn("GR_HSN_NO");
            objReportData.AddColumn("GR_GROSS_QTY");
            objReportData.AddColumn("GR_RATE");
            objReportData.AddColumn("GR_NET_GR_AMOUNT");
            objReportData.AddColumn("GR_CGST_PER");
            objReportData.AddColumn("GR_CGST_AMOUNT");
            objReportData.AddColumn("GR_SGST_PER");
            objReportData.AddColumn("GR_SGST_AMOUNT");
            objReportData.AddColumn("GR_IGST_PER");
            objReportData.AddColumn("GR_IGST_AMOUNT");
            objReportData.AddColumn("GR_RCM_PER");
            objReportData.AddColumn("GR_RCM_AMOUNT");
            objReportData.AddColumn("GR_GROSS_AMOUNT");
            objReportData.AddColumn("GR_REVERSE_AMOUNT");
            objReportData.AddColumn("GR_TOTAL_AMT");
            objReportData.AddColumn("GR_ID");
            objReportData.AddColumn("PARTY_CODE");
            
                
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("GRCN_NO","");
            objOpeningRow.setValue("GRCN_DATE","");
            objOpeningRow.setValue("PARTY_NAME","");
            objOpeningRow.setValue("ADDRESS1","");
            objOpeningRow.setValue("ADDRESS2","");
            objOpeningRow.setValue("CITY_ID","");
            objOpeningRow.setValue("PINCODE","");
            objOpeningRow.setValue("GSTIN_NO","");
            objOpeningRow.setValue("PARTY_STATE","");
            objOpeningRow.setValue("STATE_GST_CODE","");
            objOpeningRow.setValue("GR_DESC_NO","");
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("GR_INVOICE_NO","");
            objOpeningRow.setValue("GR_INVOICE_DATE","");
            objOpeningRow.setValue("GR_HSN_NO","");
            objOpeningRow.setValue("GR_GROSS_QTY","");
            objOpeningRow.setValue("GR_RATE","");
            objOpeningRow.setValue("GR_NET_GR_AMOUNT","");
            objOpeningRow.setValue("GR_CGST_PER","");
            objOpeningRow.setValue("GR_CGST_AMOUNT","");
            objOpeningRow.setValue("GR_SGST_PER","");
            objOpeningRow.setValue("GR_SGST_AMOUNT","");
            objOpeningRow.setValue("GR_IGST_PER","");
            objOpeningRow.setValue("GR_IGST_AMOUNT","");
            objOpeningRow.setValue("GR_RCM_PER","");
            objOpeningRow.setValue("GR_RCM_AMOUNT","");
            objOpeningRow.setValue("GR_GROSS_AMOUNT","");
            objOpeningRow.setValue("GR_REVERSE_AMOUNT","");
            objOpeningRow.setValue("GR_TOTAL_AMT","");
            objOpeningRow.setValue("GR_ID","");
            objOpeningRow.setValue("PARTY_CODE","");
            
           // String strSQL="SELECT PARTY.PARTY_NAME,PARTY.ADDRESS1,PARTY.ADDRESS2,PARTY.CITY_ID, PARTY.PINCODE,PARTY.GSTIN_NO,concat(PARTY.STATE)  as PARTY_STATE, PARTY.STATE_GST_CODE,A.GR_DESC_NO,A.GRCN_NO,A.GRCN_DATE,B.SR_NO,B.GR_INVOICE_NO, B.GR_INVOICE_DATE,B.GR_HSN_NO,B.GR_GROSS_QTY,B.GR_RATE, B.GR_NET_GR_AMOUNT,B.GR_CGST_PER,B.GR_CGST_AMOUNT,B.GR_SGST_PER, B.GR_SGST_AMOUNT,B.GR_IGST_PER,B.GR_IGST_AMOUNT,B.GR_RCM_PER, B.GR_RCM_AMOUNT,B.GR_GROSS_AMOUNT,B.GR_REVERSE_AMOUNT,B.GR_TOTAL_AMT, sum(B.GR_NET_GR_AMOUNT) as sum_GR_NET_GR_AMOUNT, sum(GR_TOTAL_AMT) as sum_GR_TOTAL_AMT, sum(GR_REVERSE_AMOUNT) as sum_GR_REVERSE_AMOUNT FROM  STGSALES.D_STG_GOODS_RETURNS_HEADER A,  STGSALES.D_STG_GOODS_RETURNS_DETAIL B,  DINESHMILLS.D_SAL_PARTY_MASTER PARTY WHERE  B.GR_ID = A.GR_ID AND PARTY.PARTY_CODE = A.PARTY_CODE AND A.APPROVED=1 AND A.CALCELED=0 AND A.GR_ID = '"+txtGRID.getText()+"';  ";
           
            String strSQL="SELECT PARTY.PARTY_NAME,PARTY.ADDRESS1,PARTY.ADDRESS2,PARTY.CITY_ID, PARTY.PINCODE,PARTY.GSTIN_NO,concat(PARTY.STATE)  as PARTY_STATE, PARTY.STATE_GST_CODE,A.GR_DESC_NO,A.GRCN_NO,A.GRCN_DATE,B.SR_NO,B.GR_INVOICE_NO, B.GR_INVOICE_DATE,B.GR_HSN_NO,B.GR_GROSS_QTY,B.GR_RATE, round(B.GR_NET_GR_AMOUNT,2) as GR_NET_GR_AMOUNT,B.GR_CGST_PER,round(B.GR_CGST_AMOUNT,2) as GR_CGST_AMOUNT,B.GR_SGST_PER, round(B.GR_SGST_AMOUNT,2) as GR_SGST_AMOUNT,B.GR_IGST_PER,round(B.GR_IGST_AMOUNT,2) as GR_IGST_AMOUNT,B.GR_RCM_PER, round(B.GR_RCM_AMOUNT,2) as GR_RCM_AMOUNT,round(B.GR_GROSS_AMOUNT,2) as GR_GROSS_AMOUNT,round(B.GR_REVERSE_AMOUNT,2) as GR_REVERSE_AMOUNT,round(B.GR_TOTAL_AMT,2) as GR_TOTAL_AMT FROM  STGSALES.D_STG_GOODS_RETURNS_HEADER A,  STGSALES.D_STG_GOODS_RETURNS_DETAIL B,  DINESHMILLS.D_SAL_PARTY_MASTER PARTY WHERE  B.GR_ID = A.GR_ID AND PARTY.PARTY_CODE = A.PARTY_CODE AND A.APPROVED=1 AND A.CANCELED=0 AND A.GR_ID = '"+txtGRID.getText()+"'";
            System.out.println(strSQL);
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("GRCN_NO",UtilFunctions.getString(rsTmp,"GRCN_NO",""));
                    objRow.setValue("GRCN_DATE",UtilFunctions.getString(rsTmp,"GRCN_DATE",""));
                    objRow.setValue("PARTY_NAME",UtilFunctions.getString(rsTmp,"PARTY_NAME",""));
                    objRow.setValue("ADDRESS1",UtilFunctions.getString(rsTmp,"ADDRESS1",""));
                    objRow.setValue("ADDRESS2",UtilFunctions.getString(rsTmp,"ADDRESS2",""));
                    objRow.setValue("CITY_ID",UtilFunctions.getString(rsTmp,"CITY_ID",""));
                    objRow.setValue("PINCODE",UtilFunctions.getString(rsTmp,"PINCODE",""));
                    objRow.setValue("GSTIN_NO",UtilFunctions.getString(rsTmp,"GSTIN_NO",""));
                    objRow.setValue("PARTY_STATE",UtilFunctions.getString(rsTmp,"PARTY_STATE",""));
                    objRow.setValue("STATE_GST_CODE",UtilFunctions.getString(rsTmp,"STATE_GST_CODE",""));
                    objRow.setValue("GR_DESC_NO",UtilFunctions.getString(rsTmp,"GR_DESC_NO",""));
                    objRow.setValue("SR_NO",UtilFunctions.getString(rsTmp,"SR_NO",""));
                    objRow.setValue("GR_INVOICE_NO",UtilFunctions.getString(rsTmp,"GR_INVOICE_NO",""));
                    objRow.setValue("GR_INVOICE_DATE",UtilFunctions.getString(rsTmp,"GR_INVOICE_DATE",""));
                    objRow.setValue("GR_HSN_NO",UtilFunctions.getString(rsTmp,"GR_HSN_NO",""));
                    objRow.setValue("GR_GROSS_QTY",UtilFunctions.getString(rsTmp,"GR_GROSS_QTY",""));
                    objRow.setValue("GR_RATE",UtilFunctions.getString(rsTmp,"GR_RATE",""));
                    objRow.setValue("GR_NET_GR_AMOUNT",UtilFunctions.getString(rsTmp,"GR_NET_GR_AMOUNT",""));
                    objRow.setValue("GR_CGST_PER",UtilFunctions.getString(rsTmp,"GR_CGST_PER",""));
                    objRow.setValue("GR_CGST_AMOUNT",UtilFunctions.getString(rsTmp,"GR_CGST_AMOUNT",""));
                    objRow.setValue("GR_SGST_PER",UtilFunctions.getString(rsTmp,"GR_SGST_PER",""));
                    objRow.setValue("GR_SGST_AMOUNT",UtilFunctions.getString(rsTmp,"GR_SGST_AMOUNT",""));
                    objRow.setValue("GR_IGST_PER",UtilFunctions.getString(rsTmp,"GR_IGST_PER",""));
                    objRow.setValue("GR_IGST_AMOUNT",UtilFunctions.getString(rsTmp,"GR_IGST_AMOUNT",""));
                    objRow.setValue("GR_RCM_PER",UtilFunctions.getString(rsTmp,"GR_RCM_PER",""));
                    objRow.setValue("GR_RCM_AMOUNT",UtilFunctions.getString(rsTmp,"GR_RCM_AMOUNT",""));
                    objRow.setValue("GR_GROSS_AMOUNT",UtilFunctions.getString(rsTmp,"GR_GROSS_AMOUNT",""));
                    objRow.setValue("GR_REVERSE_AMOUNT",UtilFunctions.getString(rsTmp,"GR_REVERSE_AMOUNT",""));
                    objRow.setValue("GR_TOTAL_AMT",UtilFunctions.getString(rsTmp,"GR_TOTAL_AMT",""));
                    objRow.setValue("GR_ID",UtilFunctions.getString(rsTmp,"GR_ID",""));
                    objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));

                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            
            HashMap Parameters=new HashMap();
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/sales/stg/GR_REPORT.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
