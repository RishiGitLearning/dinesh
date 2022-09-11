
/*
 * frmFelGroupMaster.java
 *
 * Created on June 19, 2013, 5:27 PM
 */
package EITLERP.FeltSales.GroupCriticalLimitEnhancement;

/**
 *
 * @author Jadeja Rajpalsinh
 */
import EITLERP.FeltSales.GroupCriticalLimitEnhancement.*;
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
import EITLERP.FeltSales.GroupMaster.clsFeltGroupMaster;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.MailNotification;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsVoucher;
import EITLERP.Loader;
import EITLERP.frmPendingApprovals;
import EITLERP.clsSales_Party;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.LOV;
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JTextField;

public class frmFelGroupCriticalLimitEnhancement extends javax.swing.JApplet {

    private clsFeltGroupCriticalLimitEnhancement ObjFeltGroupMaster;

    private int EditMode = 0;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromUserId = 0;
    private int FFNo = 0; //First Free No.
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    private boolean DoNotEvaluate = false;
    private int FlagId = 0;
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbSendToModel;

    private EITLTableModel DataModelPieceNo;
    private EITLTableModel DataModelBaleNo;
    private EITLTableModel DataModelPartyCode;
    private EITLTableModel DataModelPieceNoApprovalStatus;
    private EITLTableModel DataModelPieceNoUpdateHistory;
    private EITLTableModel DataModelPieceNoPieceNo = new EITLTableModel();
    public frmPendingApprovals frmPA;
    int critical_limit_original;
    private EITLERP.FeltSales.common.FeltInvCalc inv_calculation;

    /**
     * Creates new form frmFelGroupMaster
     */
    public void init() {
        System.gc();
        setSize(850, 600);
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

        ObjFeltGroupMaster = new clsFeltGroupCriticalLimitEnhancement();
        SetMenuForRights();
        GenerateHierarchyCombo();
        GenerateSendToCombo();
        //txtgroupdesc.setEnabled(false);
        //txtGroupId.setEnabled(false);
        txtPartyCode.setVisible(false);
        txtPartyName.setVisible(false);
        jLabel3.setVisible(false);
        jLabel5.setVisible(false);
        txtgroupCriticalLimit.setEnabled(false);
        critical_total.setVisible(false);
        FormatGrid();
        FormatGrid1();

        if (ObjFeltGroupMaster.LoadData()) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + ObjFeltGroupMaster.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
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
        lblTitle = new javax.swing.JLabel();
        Tab = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtGroupId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdRemove = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cmdAdd = new javax.swing.JButton();
        txtgroupCriticalLimit = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtgroupdesc = new javax.swing.JTextField();
        critical_total = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtprocessdate = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        Table1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtEnhancementReason = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JTextField();
        txtEnhanceLimit = new javax.swing.JTextField();
        cmdNextToTab1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtGroupOS = new javax.swing.JTextField();
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

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(178, 182, 185));
        lblTitle.setText(" FELT GROUP CRITICAL LIMIT ENHANCEMENT FOR SELECTED BALE DETAILS - ");
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

        jLabel3.setText("Party Code :");
        Tab1.add(jLabel3);
        jLabel3.setBounds(540, 60, 90, 15);

        txtGroupId.setDisabledTextColor(java.awt.Color.black);
        txtGroupId = new JTextFieldHint(new JTextField(),"Search by F1");
        txtGroupId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGroupIdActionPerformed(evt);
            }
        });
        txtGroupId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGroupIdFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupIdFocusLost(evt);
            }
        });
        txtGroupId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGroupIdKeyPressed(evt);
            }
        });
        Tab1.add(txtGroupId);
        txtGroupId.setBounds(110, 40, 110, 19);

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
        Table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TableKeyTyped(evt);
            }
        });
        Table.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                TableVetoableChange(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 300, 630, 140);

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
        cmdRemove.setBounds(650, 200, 110, 25);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel3);
        jPanel3.setBounds(10, 250, 780, 10);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        Tab1.add(cmdAdd);
        cmdAdd.setBounds(650, 130, 110, 25);

        txtgroupCriticalLimit.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        txtgroupCriticalLimit.setEnabled(false);
        txtgroupCriticalLimit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtgroupCriticalLimitFocusLost(evt);
            }
        });
        txtgroupCriticalLimit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtgroupCriticalLimitKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtgroupCriticalLimitKeyReleased(evt);
            }
        });
        Tab1.add(txtgroupCriticalLimit);
        txtgroupCriticalLimit.setBounds(160, 270, 130, 19);

        jLabel5.setText("Party Name :");
        Tab1.add(jLabel5);
        jLabel5.setBounds(500, 80, 100, 15);

        jLabel6.setText("Enhancement Reason :");
        Tab1.add(jLabel6);
        jLabel6.setBounds(230, 40, 170, 15);

        txtgroupdesc.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        txtgroupdesc.setEnabled(false);
        txtgroupdesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtgroupdescFocusLost(evt);
            }
        });
        Tab1.add(txtgroupdesc);
        txtgroupdesc.setBounds(110, 70, 380, 19);
        Tab1.add(critical_total);
        critical_total.setBounds(670, 270, 120, 19);

        lblRevNo.setText("....");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(220, 20, 30, 15);

        jLabel1.setText("Doc No :");
        Tab1.add(jLabel1);
        jLabel1.setBounds(10, 10, 60, 15);

        txtDocNo.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        txtDocNo.setEnabled(false);
        Tab1.add(txtDocNo);
        txtDocNo.setBounds(110, 10, 110, 19);

        jLabel2.setText("Doc Date :");
        Tab1.add(jLabel2);
        jLabel2.setBounds(260, 10, 80, 15);

        txtDocDate.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        txtDocDate.setEnabled(false);
        Tab1.add(txtDocDate);
        txtDocDate.setBounds(340, 10, 110, 19);

        jLabel7.setText(" Process Up To Date :");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Tab1.add(jLabel7);
        jLabel7.setBounds(540, 10, 160, 15);

        txtprocessdate.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        txtprocessdate.setEnabled(false);
        txtprocessdate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtprocessdateFocusLost(evt);
            }
        });
        Tab1.add(txtprocessdate);
        txtprocessdate.setBounds(700, 10, 110, 19);

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
        Table1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Table1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Table1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Table1KeyTyped(evt);
            }
        });
        Table1.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                Table1VetoableChange(evt);
            }
        });
        jScrollPane4.setViewportView(Table1);

        Tab1.add(jScrollPane4);
        jScrollPane4.setBounds(10, 120, 630, 120);

        jLabel4.setText("Group Code :");
        Tab1.add(jLabel4);
        jLabel4.setBounds(10, 40, 100, 20);

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel4);
        jPanel4.setBounds(10, 104, 780, 10);

        jLabel8.setText("Group Name :");
        Tab1.add(jLabel8);
        jLabel8.setBounds(10, 70, 100, 20);

        txtPartyCode.setCaretColor(new java.awt.Color(1, 1, 1));
        txtPartyCode.setDisabledTextColor(java.awt.Color.black);
        txtPartyCode = new JTextFieldHint(new JTextField(),"Search by F1");
        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusLost(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });
        Tab1.add(txtPartyCode);
        txtPartyCode.setBounds(640, 60, 110, 19);

        jLabel9.setText("Group Critical Limit :");
        Tab1.add(jLabel9);
        jLabel9.setBounds(10, 270, 143, 20);

        jLabel10.setText("Group Outsatanding");
        Tab1.add(jLabel10);
        jLabel10.setBounds(650, 290, 160, 30);

        txtEnhancementReason.setCaretColor(new java.awt.Color(1, 1, 1));
        txtEnhancementReason.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        Tab1.add(txtEnhancementReason);
        txtEnhancementReason.setBounds(400, 40, 410, 19);

        txtPartyName.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        txtPartyName.setEnabled(false);
        Tab1.add(txtPartyName);
        txtPartyName.setBounds(600, 80, 180, 19);

        txtEnhanceLimit.setEditable(false);
        txtEnhanceLimit.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        txtEnhanceLimit.setEnabled(false);
        Tab1.add(txtEnhanceLimit);
        txtEnhanceLimit.setBounds(480, 270, 160, 19);

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
        cmdNextToTab1.setBounds(660, 410, 90, 25);

        jLabel11.setText("Enhance Critical Limit :");
        Tab1.add(jLabel11);
        jLabel11.setBounds(310, 270, 160, 20);

        txtGroupOS.setDisabledTextColor(java.awt.Color.black);
        txtGroupOS.setEnabled(false);
        Tab1.add(txtGroupOS);
        txtGroupOS.setBounds(650, 319, 150, 20);

        Tab.addTab("Group Critical LImit Enhancement For Selected Bale Details", Tab1);

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
        txtFromRemarks.setBounds(86, 70, 540, 19);

        jLabel36.setText("Your Action");
        Tab2.add(jLabel36);
        jLabel36.setBounds(7, 100, 73, 15);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        buttonGroup1.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.setNextFocusableComponent(OpgFinal);
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });
        OpgApprove.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgApproveFocusGained(evt);
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

        txtToRemarks.setNextFocusableComponent(cmdBackToTab0);
        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(86, 240, 590, 19);

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
        cmdBackToTab0.setBounds(457, 273, 100, 25);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(633, 70, 49, 21);

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
        cmdNextToTab2.setBounds(574, 273, 100, 25);

        Tab.addTab("Approval", Tab2);

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.setLayout(null);

        jLabel26.setText("Document Approval Status");
        Tab3.add(jLabel26);
        jLabel26.setBounds(8, 5, 170, 15);

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
        jScrollPane2.setBounds(12, 24, 670, 100);

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
        jScrollPane3.setBounds(10, 150, 550, 150);

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
        Tab3.add(cmdBackToTab1);
        cmdBackToTab1.setBounds(570, 275, 110, 25);

        cmdBackToNormal.setText("Back To Normal");
        cmdBackToNormal.setMargin(new java.awt.Insets(2, 3, 2, 3));
        cmdBackToNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToNormalActionPerformed(evt);
            }
        });
        Tab3.add(cmdBackToNormal);
        cmdBackToNormal.setBounds(570, 180, 110, 25);

        cmdViewRevisions.setText("View Revisions");
        cmdViewRevisions.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdViewRevisions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewRevisionsActionPerformed(evt);
            }
        });
        Tab3.add(cmdViewRevisions);
        cmdViewRevisions.setBounds(570, 150, 110, 25);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        Tab3.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(570, 210, 110, 25);

        Tab.addTab("Status", Tab3);

        getContentPane().add(Tab);
        Tab.setBounds(2, 76, 830, 480);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 560, 830, 22);

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
    }// </editor-fold>//GEN-END:initComponents

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
        ObjFeltGroupMaster.HistoryView = false;
        ObjFeltGroupMaster.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdBackToNormalActionPerformed

    private void cmdViewRevisionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewRevisionsActionPerformed
        ObjFeltGroupMaster.ShowHistory(txtGroupId.getText());
        MoveLast();
    }//GEN-LAST:event_cmdViewRevisionsActionPerformed

    private void cmdBackToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab1ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBackToTab1ActionPerformed

    private void cmdNextToTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab2ActionPerformed
        Tab.setSelectedIndex(2);
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
        Tab.setSelectedIndex(0);
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
        //SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedSendToCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString())) {
                cmbSendTo.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }

        if (cmbSendTo.getItemCount() <= 0) {
            GenerateSendToCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked

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
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNextToTab1ActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        if (Table1.getRowCount() > 0) {
            DataModelBaleNo.removeRow(Table1.getSelectedRow());
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
//        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
//            if (evt.getKeyCode() == 112) //F1 Key pressed
//            {
//                if (Table.getSelectedColumn() == 1) {
//                    LOV aList = new LOV();
//
//                    String strSQL = "SELECT PARTY_CODE,PARTY_NAME,CRITICAL_AMOUNT_LIMIT FROM DINESHMILLS.D_SAL_PARTY_MASTER";
//                    aList.SQL = strSQL;
//                    aList.ReturnCol = 1;
//                    aList.ShowReturnCol = true;
//                    //aList.DefaultSearchOn=2;
//                    aList.DefaultSearchOn = 1;
//
//                    if (aList.ShowLOV()) {
//                        if (Table.getCellEditor() != null) {
//                            Table.getCellEditor().stopCellEditing();
//                        }
//                        Table.setValueAt(aList.ReturnVal, Table.getSelectedRow(), 1);
//                        String partyname = clsFeltGroupCriticalLimitEnhancement.getpartyname(aList.ReturnVal);
//                        Table.setValueAt(partyname, Table.getSelectedRow(), 2);
//                        String critical = clsFeltGroupCriticalLimitEnhancement.getcritical(aList.ReturnVal);
//                        Table.setValueAt(critical, Table.getSelectedRow(), 4);
//
//                    }
//
//                }
//
//                if (Table.getSelectedColumn() == 3) {
//                    LOV aList = new LOV();
//
//                    String strSQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='FELT_GROUP_MASTER'";
//                    aList.SQL = strSQL;
//                    aList.ReturnCol = 1;
//                    aList.ShowReturnCol = true;
//                    aList.DefaultSearchOn = 1;
//
//                    if (aList.ShowLOV()) {
//                        if (Table.getCellEditor() != null) {
//                            Table.getCellEditor().stopCellEditing();
//                        }
//                        Table.setValueAt(aList.ReturnVal, Table.getSelectedRow(), 3);
//                    }
//                }
//
//            }
//
//            if (Table.getSelectedColumn() == 4) {
//
//                try {
//                    JOptionPane.showMessageDialog(null, Table.getValueAt(Table.getSelectedRow(), 4).toString());
//                    critical_total.setText(Integer.parseInt(critical_total.getText()) + Integer.parseInt(Table.getValueAt(Table.getSelectedRow(), 4).toString()) + "");
//                } catch (Exception e) {
//
//                }
//            }
//        }

    }//GEN-LAST:event_TableKeyPressed

    private void txtGroupIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupIdFocusGained
        //lblStatus.setText("Enter Packing Date.");
    }//GEN-LAST:event_txtGroupIdFocusGained

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

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed

    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed

    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        ObjFeltGroupMaster.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
//        if (txtgroupCriticalLimit.getText().equals("")) {
//            JOptionPane.showMessageDialog(null, "Please Enter the Critical Limit");
//            return;
//        } else {
        Object[] rowData = new Object[15];
        rowData[0] = Integer.toString(Table1.getRowCount() + 1);
        rowData[1] = "";
        rowData[2] = "";
        rowData[3] = "";
        rowData[4] = "";
        rowData[5] = "";
        rowData[6] = "";
        rowData[7] = "";
        rowData[8] = "";

        DataModelBaleNo.addRow(rowData);
        Table1.changeSelection(Table1.getRowCount() - 1, 1, false, false);
        Table1.requestFocus();
        //}
    }//GEN-LAST:event_cmdAddActionPerformed

    private void txtGroupIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupIdFocusLost
//
//        if (EditMode == EITLERPGLOBAL.ADD) {
//            try {
//
//                //FormatGrid();
//                String groupid = txtGroupId.getText().trim();
//
//                String strSQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_ACTIVE,CRITICAL_LIMIT,CASH_DISC_FLAG,YEAR_END_DISC_FLAG FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE GROUP_CODE=" + groupid + "";
//                String strSQL1 = "SELECT * FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE GROUP_CODE=" + groupid + "";
//
//                ResultSet rsTmp1 = data.getResult(strSQL1);
//
//                txtGroupId.setText(rsTmp1.getString("GROUP_CODE"));
//                txtgroupdesc.setText(rsTmp1.getString("GROUP_DESC"));
//                txtgroupCriticalLimit.setText(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
//                critical_total.setText(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
//                critical_limit_original = Integer.parseInt(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
//                ResultSet rsTmp2 = data.getResult(strSQL);
//                Object[] rowData = new Object[10];
//
//                if (rsTmp2.first()) {
//                    cmdAdd.setVisible(true);
//                    cmdRemove.setVisible(true);
//
//                    while (!rsTmp2.isAfterLast()) {
//                        rowData[0] = Integer.toString(Table.getRowCount() + 1);
//                        rowData[1] = rsTmp2.getString("PARTY_CODE");
//                        rowData[2] = rsTmp2.getString("PARTY_NAME");
//                        rowData[3] = rsTmp2.getString("CRITICAL_LIMIT");
//
//                        DataModelPieceNo.addRow(rowData);
//                        rsTmp2.next();
//                    }
//
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        
//        if (EditMode == EITLERPGLOBAL.ADD) {
//            try {
//
//                //FormatGrid();
//                String PartyCode = txtPartyCode.getText().trim();
//                txtPartyName.setText(clsFeltGroupCriticalLimitEnhancement.getpartyname(PartyCode));
//                txtGroupId.setText(clsFeltGroupCriticalLimitEnhancement.getGroupCode(PartyCode));
//
//                String strSQL = "SELECT GROUP_CODE,PARTY_CODE,PARTY_NAME,CRITICAL_LIMIT FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE GROUP_CODE=(SELECT GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE PARTY_CODE='" + PartyCode + "')";
//                String groupid = txtGroupId.getText().trim();
//                String strSQL1 = "SELECT * FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE GROUP_CODE=" + groupid + "";
////                strSQL = "";
////                strSQL += "SELECT PARTY_NAME,CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + txtPartyCode.getText().trim() + "";
////                ResultSet rsTmp;
////                rsTmp = data.getResult(strSQL);
////                rsTmp.first();
////                txtPartyName.setText(rsTmp.getString("PARTY_NAME"));
//                ResultSet rsTmp1 = data.getResult(strSQL1);
//
//                txtGroupId.setText(rsTmp1.getString("GROUP_CODE"));
//                txtgroupdesc.setText(rsTmp1.getString("GROUP_DESC"));
//                txtgroupCriticalLimit.setText(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
//                critical_total.setText(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
//                critical_limit_original = Integer.parseInt(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
//                txtEnhanceLimit.setText(critical_total.getText());
//                
//                ResultSet rsTmp2 = data.getResult(strSQL);
//                Object[] rowData = new Object[10];
//
//                if (rsTmp2.first()) {
//                    while (!rsTmp2.isAfterLast()) {
//                        rowData[0] = Integer.toString(Table.getRowCount() + 1);
//                        rowData[1] = rsTmp2.getString("PARTY_CODE");
//                        rowData[2] = rsTmp2.getString("PARTY_NAME");
//                        rowData[3] = rsTmp2.getString("CRITICAL_LIMIT");
//                        rowData[4] = rsTmp2.getString("CRITICAL_LIMIT");
//                        rowData[5] = BalanceTransfer("210010",rsTmp2.getString("PARTY_CODE"));
//                        DataModelPieceNo.addRow(rowData);
//                        rsTmp2.next();
//                    }
//                    txtPartyCode.setEnabled(false);
//                    txtGroupId.setEnabled(false);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }

    }//GEN-LAST:event_txtGroupIdFocusLost

    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased
        int size = Table.getRowCount();
        int mtot = 0;
        double ostot = 0;
        critical_total.setText("0");
        for (int i = 0; i < size; i++) {
            try {
                mtot = mtot + Integer.parseInt(Table.getValueAt(i, 4).toString());
                ostot = ostot + Double.parseDouble(Table.getValueAt(i, 5).toString());
            } catch (Exception e) {
                mtot = mtot + 0;
                ostot = ostot + 0;
            }
        }
        critical_total.setText(String.valueOf(mtot));
        txtEnhanceLimit.setText(critical_total.getText());
        txtGroupOS.setText(String.valueOf(ostot));
    }//GEN-LAST:event_TableKeyReleased

    private void txtGroupIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupIdKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            try {
                LOV aList = new LOV();

                // aList.SQL = "SELECT PKG_PARTY_CODE,PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE APPROVED=1 AND CANCELED=0 AND INVOICE_FLG=0 AND BALE_REOPEN_FLG=0 GROUP BY PKG_PARTY_CODE";
//            aList.SQL = "SELECT PKG_PARTY_CODE,PKG_PARTY_NAME FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL A,PRODUCTION.FELT_PKG_SLIP_HEADER B WHERE PKG_PARTY_CODE=PARTY_CODE AND B.APPROVED=1 AND B.CANCELED=0 AND B.INVOICE_FLG=0 AND B.BALE_REOPEN_FLG=0 GROUP BY PKG_PARTY_CODE ORDER BY PKG_PARTY_CODE";
                aList.SQL = "SELECT DISTINCT A.GROUP_CODE,GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER A,PRODUCTION.FELT_GROUP_MASTER_DETAIL B,PRODUCTION.FELT_PKG_SLIP_HEADER P WHERE A.GROUP_CODE=B.GROUP_CODE AND P.PKG_PARTY_CODE=B.PARTY_CODE AND P.APPROVED=1 AND P.CANCELED=0 AND P.INVOICE_FLG=0 AND P.BALE_REOPEN_FLG=0 ORDER BY A.GROUP_CODE";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;

                if (aList.ShowLOV()) {
                    txtGroupId.setText(aList.ReturnVal);
                    txtgroupdesc.setText(clsFeltGroupCriticalLimitEnhancement.getgroupdesc(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));

                    String strSQL1 = "SELECT * FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE GROUP_CODE=" + aList.ReturnVal + "";
                    ResultSet rsTmp1 = data.getResult(strSQL1);

                    txtgroupCriticalLimit.setText(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
                    critical_total.setText(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
                    critical_limit_original = Integer.parseInt(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
                    txtEnhanceLimit.setText(critical_total.getText());

                    String strSQL = "SELECT GROUP_CODE,PARTY_CODE,PARTY_NAME,CRITICAL_LIMIT FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE GROUP_CODE='" + aList.ReturnVal + "' ORDER BY PARTY_CODE ";
                    Double osAmtTot = 0.0;

                    ResultSet rsTmp2 = data.getResult(strSQL);
                    Object[] rowData = new Object[10];

                    if (rsTmp2.first()) {
                        while (!rsTmp2.isAfterLast()) {
                            rowData[0] = Integer.toString(Table.getRowCount() + 1);
                            rowData[1] = rsTmp2.getString("PARTY_CODE");
                            rowData[2] = rsTmp2.getString("PARTY_NAME");
                            rowData[3] = rsTmp2.getString("CRITICAL_LIMIT");
                            rowData[4] = rsTmp2.getString("CRITICAL_LIMIT");
                            rowData[5] = BalanceTransfer("210010", rsTmp2.getString("PARTY_CODE"));
                            osAmtTot = osAmtTot + BalanceTransfer("210010", rsTmp2.getString("PARTY_CODE"));
                            DataModelPieceNo.addRow(rowData);
                            rsTmp2.next();
                        }
//                    txtPartyCode.setEnabled(false);
                        txtGroupId.setEnabled(false);
                    }
                    txtGroupOS.setText(String.valueOf(osAmtTot));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_txtGroupIdKeyPressed

    private void txtGroupIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGroupIdActionPerformed

    }//GEN-LAST:event_txtGroupIdActionPerformed

    private void txtgroupCriticalLimitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtgroupCriticalLimitFocusLost

        double limit;
        try {
            limit = Double.parseDouble(txtgroupCriticalLimit.getText().toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Only Number is Valid...");
            txtgroupCriticalLimit.setText("");
        }
    }//GEN-LAST:event_txtgroupCriticalLimitFocusLost

    private void TableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_TableKeyTyped

    private void TableVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_TableVetoableChange
        // TODO add your handling code here:

    }//GEN-LAST:event_TableVetoableChange

    private void txtgroupCriticalLimitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtgroupCriticalLimitKeyPressed
//        // TODO add your handling code here:
//        //JOptionPane.showMessageDialog(null,"cri limi : "+txtgroupCriticalLimit.getText()+", critical limit original : "+critical_limit_original);
//        if (EditMode == EITLERPGLOBAL.ADD) {
//            System.out.println("cri limi : " + txtgroupCriticalLimit.getText() + "critical_limit_original : " + critical_limit_original);
//            if (Integer.parseInt(txtgroupCriticalLimit.getText()) == critical_limit_original) {
//
//                HashMap hmHierarchyList2 = new HashMap();
//
//                cmbHierarchyModel = new EITLComboModel();
//                cmbHierarchy.removeAllItems();
//                cmbHierarchy.setModel(cmbHierarchyModel);
//
//                hmHierarchyList2 = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=769 ");
//
//                for (int i = 1; i <= hmHierarchyList2.size(); i++) {
//                    clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList2.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
//                    aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
//
//                    if (!ObjHierarchy.getAttribute("HIERARCHY_NAME").getString().endsWith("ABP")) {
//                        cmbHierarchyModel.addElement(aData);
//                    }
//                }
//                
//                HashMap hmSendToList2 = new HashMap();
//                
//                hmSendToList2 = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
//                for (int i = 1; i <= hmSendToList2.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList2.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
//                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
//
//                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
//                        //Exclude Current User
//                    } else {
//                        cmbSendToModel.addElement(aData);
//                    }
//                }
//                
////                //cmbHierarchy.set
////                cmbHierarchy.setSelectedItem("AC-VDS");
////                SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////                cmbHierarchyItemStateChanged(null);
////                
////                cmbHierarchy.setSelectedItem("KR-VDS");
////                SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////                cmbHierarchyItemStateChanged(null);
////                
////                cmbHierarchy.setSelectedItem("HC-VDS");
////                SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////                cmbHierarchyItemStateChanged(null);
////                //JOptionPane.showMessageDialog(null, "Hierarchy Id : "+cmbHierarchy.getSelectedItem());
////                //JOptionPane.showMessageDialog(null, "Heirarchy Id 1760");
////               // 
//            } else {
//                HashMap hmHierarchyList2 = new HashMap();
//
//                cmbHierarchyModel = new EITLComboModel();
//                cmbHierarchy.removeAllItems();
//                cmbHierarchy.setModel(cmbHierarchyModel);
//
//                hmHierarchyList2 = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=769 ");
//
//                for (int i = 1; i <= hmHierarchyList2.size(); i++) {
//                    clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList2.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
//                    aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
//
//                    if (ObjHierarchy.getAttribute("HIERARCHY_NAME").getString().endsWith("ABP")) {
//                        cmbHierarchyModel.addElement(aData);
//                    }
//                }
//                
//                HashMap hmSendToList2 = new HashMap();
//                
//                hmSendToList2 = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
//                for (int i = 1; i <= hmSendToList2.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList2.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
//                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
//
//                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
//                        //Exclude Current User
//                    } else {
//                        cmbSendToModel.addElement(aData);
//                    }
//                }
//
////               cmbHierarchy.setSelectedItem("AC-VDS-BJP-MU-ABP");
////               SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////               cmbHierarchyItemStateChanged(null);
////               
////               cmbHierarchy.setSelectedItem("KR-VDS-BJP-MU-ABP");
////               SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////               cmbHierarchyItemStateChanged(null);
////               
////               cmbHierarchy.setSelectedItem("HC-VDS-BJP-MU-ABP");
////               SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////               cmbHierarchyItemStateChanged(null);
////              // JOptionPane.showMessageDialog(null, "Hierarchy Id : "+cmbHierarchy.getSelectedItem());
////              // JOptionPane.showMessageDialog(null, "Heirarchy Id 1764");
//            }
//        }
    }//GEN-LAST:event_txtgroupCriticalLimitKeyPressed

    private void txtgroupCriticalLimitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtgroupCriticalLimitKeyReleased
        // TODO add your handling code here:
//        if (EditMode == EITLERPGLOBAL.ADD) {
//            System.out.println("cri limi : " + txtgroupCriticalLimit.getText() + "critical_limit_original : " + critical_limit_original);
//            if (Integer.parseInt(txtgroupCriticalLimit.getText()) == critical_limit_original) {
//
//                HashMap hmHierarchyList2 = new HashMap();
//
//                cmbHierarchyModel = new EITLComboModel();
//                cmbHierarchy.removeAllItems();
//                cmbHierarchy.setModel(cmbHierarchyModel);
//
//                hmHierarchyList2 = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=769 ");
//
//                for (int i = 1; i <= hmHierarchyList2.size(); i++) {
//                    clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList2.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
//                    aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
//
//                    if (!ObjHierarchy.getAttribute("HIERARCHY_NAME").getString().endsWith("ABP")) {
//                        cmbHierarchyModel.addElement(aData);
//                    }
//                }
//                
//                HashMap hmSendToList2 = new HashMap();
//                
//                hmSendToList2 = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
//                for (int i = 1; i <= hmSendToList2.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList2.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
//                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
//
//                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
//                        //Exclude Current User
//                    } else {
//                        cmbSendToModel.addElement(aData);
//                    }
//                }
//                
////                //cmbHierarchy.set
////                cmbHierarchy.setSelectedItem("AC-VDS");
////                SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////                cmbHierarchyItemStateChanged(null);
////                
////                cmbHierarchy.setSelectedItem("KR-VDS");
////                SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////                cmbHierarchyItemStateChanged(null);
////                
////                cmbHierarchy.setSelectedItem("HC-VDS");
////                SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////                cmbHierarchyItemStateChanged(null);
////                //JOptionPane.showMessageDialog(null, "Hierarchy Id : "+cmbHierarchy.getSelectedItem());
////                //JOptionPane.showMessageDialog(null, "Heirarchy Id 1760");
////               // 
//            } else {
//                HashMap hmHierarchyList2 = new HashMap();
//
//                cmbHierarchyModel = new EITLComboModel();
//                cmbHierarchy.removeAllItems();
//                cmbHierarchy.setModel(cmbHierarchyModel);
//
//                hmHierarchyList2 = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=769 ");
//
//                for (int i = 1; i <= hmHierarchyList2.size(); i++) {
//                    clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList2.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
//                    aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
//
//                    if (ObjHierarchy.getAttribute("HIERARCHY_NAME").getString().endsWith("ABP")) {
//                        cmbHierarchyModel.addElement(aData);
//                    }
//                }
//                
//                HashMap hmSendToList2 = new HashMap();
//                
//                hmSendToList2 = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
//                for (int i = 1; i <= hmSendToList2.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList2.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
//                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
//
//                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
//                        //Exclude Current User
//                    } else {
//                        cmbSendToModel.addElement(aData);
//                    }
//                }
////               cmbHierarchy.setSelectedItem("AC-VDS-BJP-MU-ABP");
////               SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////               cmbHierarchyItemStateChanged(null);
////               
////               cmbHierarchy.setSelectedItem("KR-VDS-BJP-MU-ABP");
////               SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////               cmbHierarchyItemStateChanged(null);
////               
////               cmbHierarchy.setSelectedItem("HC-VDS-BJP-MU-ABP");
////               SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
////               cmbHierarchyItemStateChanged(null);
////              // JOptionPane.showMessageDialog(null, "Hierarchy Id : "+cmbHierarchy.getSelectedItem());
////              // JOptionPane.showMessageDialog(null, "Heirarchy Id 1764");
//            }
//        }
    }//GEN-LAST:event_txtgroupCriticalLimitKeyReleased

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

    private void Table1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table1KeyPressed
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            if (evt.getKeyCode() == 112) //F1 Key pressed
            {
                LOV aList = new LOV();
                //aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO ";
                // aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL WHERE  CANCELED=0 ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO UNION ALL SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_NO IN (SELECT BALE_NO FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL WHERE CANCELED=1 AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO)";
//                aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_BALE_NO NOT IN (SELECT BALE_NO FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL WHERE  CANCELED=0 ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND (A.CANCELED=0 OR A.CANCELED IS NULL) AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO";
//                aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND CONCAT(A.PKG_BALE_NO,A.PKG_BALE_DATE) NOT IN (SELECT CONCAT(BALE_NO,BALE_DATE) FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL WHERE  CANCELED=0 ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND (A.CANCELED=0 OR A.CANCELED IS NULL) AND A.PKG_PARTY_CODE='" + txtPartyCode.getText() + "' GROUP BY B.PKG_BALE_NO";
//                aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B,PRODUCTION.FELT_GROUP_MASTER_DETAIL C WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_PARTY_CODE=C.PARTY_CODE AND CONCAT(A.PKG_BALE_NO,A.PKG_BALE_DATE) NOT IN (SELECT CONCAT(BALE_NO,BALE_DATE) FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL WHERE  CANCELED=0 ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND (A.CANCELED=0 OR A.CANCELED IS NULL) AND C.GROUP_CODE='" + txtGroupId.getText() + "' GROUP BY B.PKG_BALE_NO";// ON 19/12/2020
                aList.SQL = "SELECT GROUP_CONCAT(B.PKG_PIECE_NO) AS PKG_PIECE_NO,A.PKG_BALE_NO,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B,PRODUCTION.FELT_GROUP_MASTER_DETAIL C WHERE A.PKG_BALE_NO=B.PKG_BALE_NO AND A.PKG_BALE_DATE=B.PKG_BALE_DATE AND A.PKG_PARTY_CODE=C.PARTY_CODE AND CONCAT(A.PKG_BALE_NO,A.PKG_BALE_DATE) NOT IN (SELECT CONCAT(D.BALE_NO,D.BALE_DATE) FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL D, PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER H WHERE D.DOC_NO=H.DOC_NO AND D.DOC_DATE=H.DOC_DATE AND D.CANCELED=0 AND DATEDIFF(CURDATE(),H.PROCESSING_DATE )<=0 ) AND A.BALE_REOPEN_FLG=0 AND A.APPROVED=1 AND A.INVOICE_FLG=0 AND (A.CANCELED=0 OR A.CANCELED IS NULL) AND C.GROUP_CODE='" + txtGroupId.getText() + "' GROUP BY B.PKG_BALE_NO";
                aList.ReturnCol = 2;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 3;

                if (aList.ShowLOV()) {
                    String str = aList.ReturnVal;
                    Table1.setValueAt(aList.ReturnVal, Table1.getSelectedRow(), 1);
                    String BaleDate = EITLERPGLOBAL.formatDate(clsFeltGroupCriticalLimitEnhancement.getBaleDate(aList.ReturnVal));
                    Table1.setValueAt(BaleDate, Table1.getSelectedRow(), 2);

                    String PartyCode = data.getStringValueFromDB("SELECT PKG_PARTY_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String PartyName = data.getStringValueFromDB("SELECT PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");

                    String PieceNO = data.getStringValueFromDB("SELECT PKG_PIECE_NO FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String ProductCode = data.getStringValueFromDB("SELECT PKG_PRODUCT_CODE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
//                    String Partycode = txtPartyCode.getText();//txtPartyCode.setText(Partycode);
                    String Length = data.getStringValueFromDB("SELECT PKG_LENGTH FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String Width = data.getStringValueFromDB("SELECT PKG_WIDTH FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String Weight = data.getStringValueFromDB("SELECT PKG_WEIGHT FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
                    String Sqmtr = data.getStringValueFromDB("SELECT PKG_SQM FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "' AND PKG_BALE_DATE='" + EITLERPGLOBAL.formatDateDB(BaleDate) + "'");
////                String Orderdate = data.getStringValueFromDB("SELECT PKG_ORDER_DATE FROM PRODUCTION.FELT_PKG_SLIP_DETAIL WHERE PKG_BALE_NO='" + str + "'");
                    String Orderdate = EITLERPGLOBAL.getCurrentDateDB();
//                    //lblPartyName.setText(clsFeltGSTAdvancePaymentEntryForm.getParyName(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));
////                    txtchargecodeold1.setText(clsFeltGSTAdvancePaymentEntryForm.getChargeCode(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));
//                    txttransportcode.setText(clsFeltGSTAdvancePaymentEntryForm.gettransportid(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));
//                    txtcriticallimitold.setText(clsFeltGSTAdvancePaymentEntryForm.getCriticalLimit(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText().trim()));
//
                    inv_calculation = EITLERP.FeltSales.common.clsOrderValueCalc.calculate(PieceNO, ProductCode, PartyCode, Float.parseFloat(Length), Float.parseFloat(Width), Float.parseFloat(Weight), Float.parseFloat(Sqmtr), Orderdate);
                    Float billvalue = inv_calculation.getFicInvAmt();
                    //txtBillValue.setText(billvalue.toString());
                    Table1.setValueAt(billvalue, Table1.getSelectedRow(), 3);

                    Table1.setValueAt(PartyCode, Table1.getSelectedRow(), 4);
                    Table1.setValueAt(PartyName, Table1.getSelectedRow(), 5);
                    Table1.setValueAt(PieceNO, Table1.getSelectedRow(), 6);
                }

            }

        }        // TODO add your handling code here:
    }//GEN-LAST:event_Table1KeyPressed

    private void Table1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_Table1KeyReleased

    private void Table1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_Table1KeyTyped

    private void Table1VetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_Table1VetoableChange
        // TODO add your handling code here:
    }//GEN-LAST:event_Table1VetoableChange

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            // aList.SQL = "SELECT PKG_PARTY_CODE,PKG_PARTY_NAME FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE APPROVED=1 AND CANCELED=0 AND INVOICE_FLG=0 AND BALE_REOPEN_FLG=0 GROUP BY PKG_PARTY_CODE";
            aList.SQL = "SELECT PKG_PARTY_CODE,PKG_PARTY_NAME FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL A,PRODUCTION.FELT_PKG_SLIP_HEADER B WHERE PKG_PARTY_CODE=PARTY_CODE AND B.APPROVED=1 AND B.CANCELED=0 AND B.INVOICE_FLG=0 AND B.BALE_REOPEN_FLG=0 GROUP BY PKG_PARTY_CODE ORDER BY PKG_PARTY_CODE";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);
                txtPartyName.setText(clsFeltGroupCriticalLimitEnhancement.getpartyname(aList.ReturnVal));
                txtGroupId.setText(clsFeltGroupCriticalLimitEnhancement.getGroupCode(aList.ReturnVal));

//                txtchargecodeold.setText(clsFeltProcessInvoiceVariable.getChargeCode(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
//                txttransportcode.setText(clsFeltProcessInvoiceVariable.gettransportid(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
//                txtcriticallimitold.setText(clsFeltProcessInvoiceVariable.getCriticalLimit(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }

        }          // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void txtgroupdescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtgroupdescFocusLost
        txtgroupCriticalLimit.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtgroupdescFocusLost

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        if (EditMode == EITLERPGLOBAL.ADD) {
            try {

                //FormatGrid();
                String PartyCode = txtPartyCode.getText().trim();
                txtPartyName.setText(clsFeltGroupCriticalLimitEnhancement.getpartyname(PartyCode));
                txtGroupId.setText(clsFeltGroupCriticalLimitEnhancement.getGroupCode(PartyCode));

                String strSQL = "SELECT GROUP_CODE,PARTY_CODE,PARTY_NAME,CRITICAL_LIMIT FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE GROUP_CODE=(SELECT GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL WHERE PARTY_CODE='" + PartyCode + "')";
                String groupid = txtGroupId.getText().trim();
                String strSQL1 = "SELECT * FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE GROUP_CODE=" + groupid + "";
//                strSQL = "";
//                strSQL += "SELECT PARTY_NAME,CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + txtPartyCode.getText().trim() + "";
//                ResultSet rsTmp;
//                rsTmp = data.getResult(strSQL);
//                rsTmp.first();
//                txtPartyName.setText(rsTmp.getString("PARTY_NAME"));
                ResultSet rsTmp1 = data.getResult(strSQL1);

                txtGroupId.setText(rsTmp1.getString("GROUP_CODE"));
                txtgroupdesc.setText(rsTmp1.getString("GROUP_DESC"));
                txtgroupCriticalLimit.setText(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
                critical_total.setText(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
                critical_limit_original = Integer.parseInt(rsTmp1.getString("GROUP_CRITICAL_LIMIT"));
                txtEnhanceLimit.setText(critical_total.getText());
                ResultSet rsTmp2 = data.getResult(strSQL);
                Object[] rowData = new Object[10];

                if (rsTmp2.first()) {
                    while (!rsTmp2.isAfterLast()) {
                        rowData[0] = Integer.toString(Table.getRowCount() + 1);
                        rowData[1] = rsTmp2.getString("PARTY_CODE");
                        rowData[2] = rsTmp2.getString("PARTY_NAME");
                        rowData[3] = rsTmp2.getString("CRITICAL_LIMIT");
                        rowData[4] = rsTmp2.getString("CRITICAL_LIMIT");
                        rowData[5] = BalanceTransfer("210010", rsTmp2.getString("PARTY_CODE"));
                        DataModelPieceNo.addRow(rowData);
                        rsTmp2.next();
                    }
                    txtPartyCode.setEnabled(false);
                    txtGroupId.setEnabled(false);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCodeFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
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
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewRevisions;
    private javax.swing.JTextField critical_total;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtEnhanceLimit;
    private javax.swing.JTextField txtEnhancementReason;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGroupId;
    private javax.swing.JTextField txtGroupOS;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtgroupCriticalLimit;
    private javax.swing.JTextField txtgroupdesc;
    private javax.swing.JTextField txtprocessdate;
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
        txtDocNo.setEnabled(false);
        txtDocDate.setEnabled(false);
        //txtGroupId.setEnabled(false);
        txtGroupId.setEnabled(pStat);
        txtgroupdesc.setEnabled(false);
        txtgroupCriticalLimit.setEnabled(false);
        cmdAdd.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);
        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        Table.setEnabled(pStat);
//        txtPartyCode.setEnabled(pStat);
//        txtPartyName.setEnabled(pStat);
        txtPartyCode.setEnabled(false);
        txtPartyName.setEnabled(false);
        txtprocessdate.setEnabled(pStat);
        txtEnhancementReason.setEnabled(pStat);
        txtEnhanceLimit.setEnabled(pStat);
        txtGroupOS.setEnabled(false);
        SetupApproval();
    }

    private void ClearFields() {
        txtprocessdate.setText(EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT CURDATE() + INTERVAL '7' DAY FROM DUAL")));
        txtDocNo.setText("");
        txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());
        critical_total.setText("0");
        txtGroupId.setText("");
        txtgroupdesc.setText("");
        txtgroupCriticalLimit.setText("");
        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        txtEnhancementReason.setText("");
        txtEnhanceLimit.setText("");
        txtGroupOS.setText("");
        txtPartyCode.setText("");
        txtPartyName.setText("");
        FormatGrid();
        FormatGrid1();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();

    }

    //Display data on the Screen
    private void DisplayData() {
        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, 769)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        //=========== Title Bar Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (ObjFeltGroupMaster.getAttribute("APPROVED").getInt() == 1) {
                    lblTitle.setBackground(Color.BLUE);
                } else {
                    lblTitle.setBackground(Color.GRAY);
                }

                if (ObjFeltGroupMaster.getAttribute("CANCELED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
            //============================================//

            String DocNo = ObjFeltGroupMaster.getAttribute("DOC_NO").getString();
            txtDocNo.setText(DocNo);
            txtDocDate.setText(EITLERPGLOBAL.formatDate(ObjFeltGroupMaster.getAttribute("DOC_DATE").getString()));
            txtprocessdate.setText(EITLERPGLOBAL.formatDate(ObjFeltGroupMaster.getAttribute("PROCESSING_DATE").getString()));
            txtPartyCode.setText(ObjFeltGroupMaster.getAttribute("PARTY_CODE").getString());
            txtPartyName.setText(ObjFeltGroupMaster.getAttribute("PARTY_NAME").getString());
            txtEnhancementReason.setText(ObjFeltGroupMaster.getAttribute("ENHANCEMENT_REASON").getString());
            //String groupid = ObjFeltGroupMaster.getAttribute("GROUP_CODE").getString();
            txtGroupId.setText(ObjFeltGroupMaster.getAttribute("GROUP_CODE").getString());
            txtgroupdesc.setText(ObjFeltGroupMaster.getAttribute("GROUP_NAME").getString());
            txtgroupCriticalLimit.setText(ObjFeltGroupMaster.getAttribute("ENHANCE_GROUP_CRITICAL_LIMIT").getString());
            //critical_limit_original = Integer.parseInt(ObjFeltGroupMaster.getAttribute("ENHANCE_PARTY_CRITICAL_LIMIT").getString());
            txtEnhanceLimit.setText(ObjFeltGroupMaster.getAttribute("ENHANCE_PARTY_CRITICAL_LIMIT").getString());

            lblTitle.setText(" FELT GROUP CRITICAL LIMIT ENHANCEMENT FOR SELECTED BALE DETAILS - " + DocNo);
            lblRevNo.setText(Integer.toString((int) ObjFeltGroupMaster.getAttribute("REVISION_NO").getVal()));
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjFeltGroupMaster.getAttribute("HIERARCHY_ID").getInt());
            DoNotEvaluate = true;

            txtDocNo.setText(DocNo);

            //Now Generate Table
            FormatGrid();
            FormatGrid1();
            int total = 0;
            double osTotal = 0.0;
            for (int i = 1; i <= ObjFeltGroupMaster.hmFeltGroupMasterDetails_Bale.size(); i++) {
                clsFeltGroupCriticalLimitEnhancementBaleDetails ObjFeltGroupMasterDetails = (clsFeltGroupCriticalLimitEnhancementBaleDetails) ObjFeltGroupMaster.hmFeltGroupMasterDetails_Bale.get(Integer.toString(i));

                Object[] rowData = new Object[10];
                rowData[0] = Integer.toString(i);
                rowData[1] = ObjFeltGroupMasterDetails.getAttribute("BALE_NO").getString();
                rowData[2] = ObjFeltGroupMasterDetails.getAttribute("BALE_DATE").getString();
                rowData[3] = ObjFeltGroupMasterDetails.getAttribute("BILL_VALUE").getString();
                rowData[4] = ObjFeltGroupMasterDetails.getAttribute("PARTY_CODE").getString();
                rowData[5] = ObjFeltGroupMasterDetails.getAttribute("PARTY_NAME").getString();
                rowData[6] = ObjFeltGroupMasterDetails.getAttribute("PIECE_NO").getString();

                DataModelBaleNo.addRow(rowData);

            }

            for (int i = 1; i <= ObjFeltGroupMaster.hmFeltGroupMasterDetails_Group.size(); i++) {
                clsFeltGroupCriticalLimitEnhancementGroupDetails ObjFeltGroupMasterDetails = (clsFeltGroupCriticalLimitEnhancementGroupDetails) ObjFeltGroupMaster.hmFeltGroupMasterDetails_Group.get(Integer.toString(i));

                Object[] rowData1 = new Object[10];
                rowData1[0] = Integer.toString(i);
                rowData1[1] = ObjFeltGroupMasterDetails.getAttribute("GROUP_PARTY_CODE").getString();
                rowData1[2] = ObjFeltGroupMasterDetails.getAttribute("GROUP_PARTY_NAME").getString();
                rowData1[3] = ObjFeltGroupMasterDetails.getAttribute("GROUP_PARTY_CRITICAL_LIMIT").getString();
                rowData1[4] = ObjFeltGroupMasterDetails.getAttribute("GROUP_ENHANCE_PARTY_CRITICAL_LIMIT").getString();
                rowData1[5] = ObjFeltGroupMasterDetails.getAttribute("GROUP_PARTY_OUTSTANDING").getDouble();
                osTotal = osTotal + ObjFeltGroupMasterDetails.getAttribute("GROUP_PARTY_OUTSTANDING").getDouble();

                DataModelPieceNo.addRow(rowData1);

            }

            txtGroupOS.setText(String.valueOf(osTotal));
            critical_total.setText(total + "");
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap hmList = new HashMap();

            hmList = clsFeltProductionApprovalFlow.getDocumentFlow(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString());
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

                DataModelPieceNoApprovalStatus.addRow(rowData);
            }
            //============================================================//

            // Generating Grid for Showing Production Details Update History
            FormatGridUpdateHistory();
            HashMap hmApprovalHistory = clsFeltGroupCriticalLimitEnhancement.getHistoryList(ObjFeltGroupMaster.getAttribute("DOC_DATE").getString(), ObjFeltGroupMaster.getAttribute("DOC_NO").getString());
            for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                clsFeltGroupCriticalLimitEnhancement ObjFeltGroupMaster = (clsFeltGroupCriticalLimitEnhancement) hmApprovalHistory.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjFeltGroupMaster.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(2, (int) ObjFeltGroupMaster.getAttribute("UPDATED_BY").getVal());
                rowData[2] = ObjFeltGroupMaster.getAttribute("ENTRY_DATE").getString();

                String ApprovalStatus = "";

                if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }

                if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }

                if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                rowData[3] = ApprovalStatus;
                rowData[4] = ObjFeltGroupMaster.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjFeltGroupMaster.getAttribute("FROM_IP").getString();

                DataModelPieceNoUpdateHistory.addRow(rowData);
            }
            SetFields(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        cmbHierarchy.setSelectedItem("RJ-DRRP");
//        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
//        cmbHierarchyItemStateChanged(null);
//        DoNotEvaluate = false;
    }

    //Generates Hierarchy Combo Box
    private void GenerateHierarchyCombo() {
        HashMap hmHierarchyList = new HashMap();

        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        hmHierarchyList = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=769  AND D_COM_HIERARCHY.HIERARCHY_ID>3714");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            hmHierarchyList = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=769 ");
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
            int FromUserID = clsFeltProductionApprovalFlow.getFromID(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString());
            lnFromUserId = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(769, FromUserID, ObjFeltGroupMaster.getAttribute("DOC_NO").getString());

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
            if (clsFeltProductionApprovalFlow.IsCreator(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString())) {
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6112, 61121)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6112, 61122)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6112, 61123)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6112, 61124)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }

    private void Add() {

        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(this, "The year is closed. You cannot enter/edit any transaction", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Generate new document no.
        EITLERP.SelectFirstFree aList = new EITLERP.SelectFirstFree();
        aList.ModuleID = 769;
        aList.FirstFreeNo = 237;
        FFNo = aList.FirstFreeNo;
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        GenerateHierarchyCombo();
        GenerateSendToCombo();
        SetupApproval();
        txtDocNo.setText(EITLERP.clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 769, FFNo, false));
        lblTitle.setText(" FELT GROUP CRITICAL LIMIT ENHANCEMENT FOR SELECTED BALE DETAILS - " + txtDocNo.getText());
        lblTitle.setBackground(Color.GRAY);
        txtGroupId.setEnabled(true);
        txtgroupdesc.setEnabled(false);
        txtgroupCriticalLimit.setEnabled(false);
        txtEnhanceLimit.setEnabled(false);
        txtGroupOS.setEnabled(false);

        //cmbHierarchy.setEnabled(false);
        critical_total.setText("0");
    }

    private void Edit() {
        if (ObjFeltGroupMaster.IsEditable(txtDocNo.getText(), EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            if (clsFeltProductionApprovalFlow.IsCreator(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString())) {
                SetFields(true);
                txtGroupId.setEnabled(false);
                txtgroupCriticalLimit.setEnabled(false);
            } else {
                EnableApproval();
            }
        } else {
            JOptionPane.showMessageDialog(this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
//        txtGroupId.setEnabled(false);
//        txtgroupdesc.setEnabled(false);
//        txtgroupCriticalLimit.setEnabled(false);
    }

    private void Delete() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(this, "The year is closed. You cannot enter/edit any transaction", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //----------------------------------//

        int ValidEntryCount = 0;

        if (txtgroupCriticalLimit.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Party Code");
            txtgroupCriticalLimit.requestFocus(true);
            return;
        }
        if (ObjFeltGroupMaster.CanDelete(txtDocNo.getText(), EITLERPGLOBAL.gNewUserID)) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, ObjFeltGroupMaster.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Save() {

        try {

//            if (Integer.parseInt(critical_total.getText()) != Integer.parseInt(txtgroupCriticalLimit.getText())) {
//                JOptionPane.showMessageDialog(null, "Critical Amount And Party Cricical Amount Mismatched. Please Check The Total");
//                txtgroupCriticalLimit.requestFocus();
//
//            } else {
            String groupdesc = "", groupid = "", DocNo = "", DocDate = "", grouplimit = "", ProcessDate = "";
            String PartyName = "", PartyCode = "", EnhancemendReason = "", EnhancementLimit = "";

            DocNo = txtDocNo.getText().trim();
            DocDate = EITLERPGLOBAL.formatDateDB(txtDocDate.getText().trim());
            ProcessDate = EITLERPGLOBAL.formatDateDB(txtprocessdate.getText().trim());
            PartyCode = txtPartyCode.getText().trim();
            PartyName = txtPartyName.getText().trim();
            EnhancemendReason = txtEnhancementReason.getText().trim();
            groupid = txtGroupId.getText().trim();
            groupdesc = txtgroupdesc.getText().trim();
            grouplimit = txtgroupCriticalLimit.getText().trim();
            EnhancementLimit = txtEnhanceLimit.getText().trim();

            //Form level validations
//            if (PartyCode.equals("")) {
//                return;
//            }
            if (groupid.equals("")) {
                return;
            }
            
            if (EnhancemendReason.equals("")) {
                JOptionPane.showMessageDialog(this, "Please Enter Enhancement Reason.", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (DocNo.equals("")) {
                return;
            }

            // check Packing Date is Within Financial Year?
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);

            //Check the no. of items
            int i = 0, j = 0;
            try {
                String BillNo = "", BillDate = "";
                Double BillValue = 0.0;
                Double enhLmt = 0.0;
                Double osAmt = 0.0;
                // check duplicate piece no in table
                for (int k = 0; k <= Table1.getRowCount() - 1; k++) {
                    //if(Table.getValueAt(k,3).toString().equalsIgnoreCase(Table.getValueAt(k,4).toString())){
                    //JOptionPane.showMessageDialog(this, "Group Party Critical Limit And Enhance Pary Critical Limit Are Same at Row " + (k + 1) , "ERROR", JOptionPane.ERROR_MESSAGE);
                    //return;   
                    //}
                    for (int l = k; l <= Table1.getRowCount() - 1; l++) {
                        if (l != k && ((String) Table1.getValueAt(k, 1)).trim().equals(((String) Table1.getValueAt(l, 1)).trim())) {
                            JOptionPane.showMessageDialog(this, "Same Bale No at Row " + (k + 1) + " and " + (l + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                if (Table1.getRowCount() <= 0) {
                    JOptionPane.showMessageDialog(this, "Add Bale No", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //Check the entered details in Table.
                for (i = 0; i <= Table1.getRowCount() - 1; i++) {
                    BillNo = ((String) Table1.getValueAt(i, 1)).trim();
                    BillDate = ((String) Table1.getValueAt(i, 2)).trim();
                    BillValue = Double.parseDouble(Table1.getValueAt(i, 3).toString()); //3

                    j++;
                    if (BillNo.equals("")) {
                        JOptionPane.showMessageDialog(this, "Add Bale No", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    j++;
                    if (BillDate.equals("")) {
                        JOptionPane.showMessageDialog(this, "Add Bale Date", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    j++;
                    if (BillValue.equals("")) {
                        return;
                    }

                }

                for (i = 0; i < Table.getRowCount(); i++) {
                    if (Table.getValueAt(i, 4).toString().trim().equals("")) {
                        Table.setValueAt("0", i, 4);
                        enhLmt = 0.0;
                    } else {
                        enhLmt = Double.parseDouble(Table.getValueAt(i, 4).toString());
                    }
                    osAmt = Double.parseDouble(Table.getValueAt(i, 5).toString());

                    if (enhLmt < osAmt) {
                        JOptionPane.showMessageDialog(this, "Enhanced Limit is less than Outstaning Amount at Row " + (i + 1) + " for Party : " + Table.getValueAt(i, 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException nfe) {
                return;
            }

            if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Enter the remarks for rejection", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
                JOptionPane.showMessageDialog(this, "Select the user, to whom rejected document to be send");
                return;
            }

            if (EditMode == EITLERPGLOBAL.ADD) {
                //STEP 1
                //System.out.println("Selected Hierarchy  : " + SelHierarchyID);
                System.out.println("Selected Hierarchy  : " + SelHierarchyID + " User Id : " + EITLERPGLOBAL.gNewUserID);
                if (EITLERPGLOBAL.gNewUserID != 243 && EITLERPGLOBAL.gNewUserID != 262) {
                    HashMap hmSendToList = new HashMap();
                    hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, true);
                    boolean AdityaSir_exist = false;

                    for (int p = 1; p <= hmSendToList.size(); p++) {
                        clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(p));

                        int user_id = (int) ObjUser.getAttribute("USER_ID").getVal();

                        if (user_id == 243 || user_id == 262) {
                            AdityaSir_exist = true;
                        }
                    }

//                        if (AdityaSir_exist && Integer.parseInt(txtgroupCriticalLimit.getText()) >= critical_limit_original) {
                    if (AdityaSir_exist && Integer.parseInt(txtgroupCriticalLimit.getText()) >= Integer.parseInt(txtEnhanceLimit.getText())) {
                        JOptionPane.showMessageDialog(null, "Group Critical Limit not changed, Aditya Sir is not required in Hierarchy.");
                        return;
                    }
//                        if (!AdityaSir_exist && Integer.parseInt(txtgroupCriticalLimit.getText()) < critical_limit_original) {
                    if (!AdityaSir_exist && Integer.parseInt(txtgroupCriticalLimit.getText()) < Integer.parseInt(txtEnhanceLimit.getText())) {
                        JOptionPane.showMessageDialog(null, "Group Critical Limit changed, Aditya Sir must required in Hierarchy.");
                        return;
                    }
                }
                //STEP1 END
            }

            //set data for insert/update
            SetData();

            if (EditMode == EITLERPGLOBAL.ADD) {
                if (ObjFeltGroupMaster.Insert()) {
                    Mail();
//                    if (OpgFinal.isSelected()) {
//                        try {
//                            String DOC_NO = txtDocNo.getText();
//                            String DOC_DATE = txtDocDate.getText();
//                            String Party_Code = "";
//                            int Module_Id = 769;
//
//                            String responce = JavaMail.sendFinalApprovalMail(Module_Id, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
//                            System.out.println("Send Mail Responce : " + responce);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

                    DisplayData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured while saving. Error is ", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (EditMode == EITLERPGLOBAL.EDIT) {
                if (ObjFeltGroupMaster.Update()) {
                    Mail();
//                    if (OpgFinal.isSelected()) {
//                        try {
//                            String DOC_NO = txtDocNo.getText();
//                            String DOC_DATE = txtDocDate.getText();
//                            String Party_Code = "";
//                            int Module_Id = 769;
//
//                            //String responce = JavaMail.sendFinalApprovalMail(Module_Id, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
//                            //System.out.println("Send Mail Responce : " + responce);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

                    DisplayData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured while saving editing. Error is ", "ERROR", JOptionPane.ERROR_MESSAGE);
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
            // }
        } catch (Exception e) {

        }
    }

    //Sets data to the Details Class Object
    private void SetData() {

        String DocNo = "", DocDate = "", ProcessDate = "", PartyCode = "", PartyName = "", EnhancementReason = "", grouplimit = "", groupid, groupDesc = "", partyCode = "", partyName = "", station = "", transportMode = "", boxSize = "", modePacking = "", EnhancementLimit = "";

        DocNo = txtDocNo.getText().trim();
        DocDate = EITLERPGLOBAL.formatDateDB(txtDocDate.getText().trim());
        ProcessDate = EITLERPGLOBAL.formatDateDB(txtprocessdate.getText().trim());
        PartyCode = txtPartyCode.getText().trim();
        PartyName = txtPartyName.getText().trim();
        EnhancementReason = txtEnhancementReason.getText().trim();
        groupid = txtGroupId.getText().trim();
        groupDesc = txtgroupdesc.getText().trim();
        grouplimit = txtgroupCriticalLimit.getText().trim();
        EnhancementLimit = txtEnhanceLimit.getText().trim();

        ObjFeltGroupMaster.setAttribute("FLAG_ID", FlagId);
        ObjFeltGroupMaster.setAttribute("FFNO", FFNo);
        ObjFeltGroupMaster.setAttribute("DOC_NO", DocNo);
        ObjFeltGroupMaster.setAttribute("DOC_DATE", DocDate);
        ObjFeltGroupMaster.setAttribute("PROCESSING_DATE", ProcessDate);
        ObjFeltGroupMaster.setAttribute("PARTY_CODE", PartyCode);
        ObjFeltGroupMaster.setAttribute("PARTY_NAME", PartyName);
        ObjFeltGroupMaster.setAttribute("ENHANCEMENT_REASON", EnhancementReason);
        ObjFeltGroupMaster.setAttribute("GROUP_CODE", groupid);
        ObjFeltGroupMaster.setAttribute("GROUP_NAME", groupDesc);
        ObjFeltGroupMaster.setAttribute("ENHANCE_GROUP_CRITICAL_LIMIT", grouplimit);
        ObjFeltGroupMaster.setAttribute("ENHANCE_PARTY_CRITICAL_LIMIT", EnhancementLimit);

        //-------- Update Approval Specific Fields -----------//
        ObjFeltGroupMaster.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        //JOptionPane.showMessageDialog(null, "In set Data : "+EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjFeltGroupMaster.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjFeltGroupMaster.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjFeltGroupMaster.setAttribute("FROM_REMARKS", txtToRemarks.getText().trim());
        ObjFeltGroupMaster.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
        if (OpgApprove.isSelected()) {
            ObjFeltGroupMaster.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjFeltGroupMaster.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjFeltGroupMaster.setAttribute("APPROVAL_STATUS", "R");
            ObjFeltGroupMaster.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjFeltGroupMaster.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjFeltGroupMaster.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
        } else {
            ObjFeltGroupMaster.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
        }

        ObjFeltGroupMaster.hmFeltGroupMasterDetails_Bale.clear();
        ObjFeltGroupMaster.hmFeltGroupMasterDetails_Group.clear();
        // Insert entered data in hashmap for update or insert
        for (int i = 0; i < Table1.getRowCount(); i++) {
            clsFeltGroupCriticalLimitEnhancementBaleDetails ObjFeltGroupMasterDetails = new clsFeltGroupCriticalLimitEnhancementBaleDetails();

            ObjFeltGroupMasterDetails.setAttribute("BALE_NO", (String) Table1.getValueAt(i, 1)); //1
            ObjFeltGroupMasterDetails.setAttribute("BALE_DATE", (String) Table1.getValueAt(i, 2));
            ObjFeltGroupMasterDetails.setAttribute("BILL_VALUE", Float.parseFloat(Table1.getValueAt(i, 3).toString()));
            ObjFeltGroupMasterDetails.setAttribute("PARTY_CODE", (String) Table1.getValueAt(i, 4));
            ObjFeltGroupMasterDetails.setAttribute("PARTY_NAME", (String) Table1.getValueAt(i, 5));
            ObjFeltGroupMasterDetails.setAttribute("PIECE_NO", (String) Table1.getValueAt(i, 6));

            ObjFeltGroupMaster.hmFeltGroupMasterDetails_Bale.put(Integer.toString(ObjFeltGroupMaster.hmFeltGroupMasterDetails_Bale.size() + 1), ObjFeltGroupMasterDetails);
        }
        for (int i = 0; i < Table.getRowCount(); i++) {
            clsFeltGroupCriticalLimitEnhancementGroupDetails ObjFeltGroupMasterDetails = new clsFeltGroupCriticalLimitEnhancementGroupDetails();

            ObjFeltGroupMasterDetails.setAttribute("GROUP_PARTY_CODE", (String) Table.getValueAt(i, 1)); //1
            ObjFeltGroupMasterDetails.setAttribute("GROUP_PARTY_NAME", (String) Table.getValueAt(i, 2));
            ObjFeltGroupMasterDetails.setAttribute("GROUP_PARTY_CRITICAL_LIMIT", (String) Table.getValueAt(i, 3));
            ObjFeltGroupMasterDetails.setAttribute("GROUP_ENHANCE_PARTY_CRITICAL_LIMIT", (String) Table.getValueAt(i, 4));
            ObjFeltGroupMasterDetails.setAttribute("GROUP_PARTY_OUTSTANDING", Double.parseDouble(Table.getValueAt(i, 5).toString()));

            ObjFeltGroupMaster.hmFeltGroupMasterDetails_Group.put(Integer.toString(ObjFeltGroupMaster.hmFeltGroupMasterDetails_Group.size() + 1), ObjFeltGroupMasterDetails);
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
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFindGroupCriticalLimitEnhancement", true);
        frmFindGroupCriticalLimitEnhancement ObjFindFeltPacking = (frmFindGroupCriticalLimitEnhancement) ObjLoader.getObj();

        if (ObjFindFeltPacking.Cancelled == false) {
            if (!ObjFeltGroupMaster.Filter(ObjFindFeltPacking.stringFindQuery)) {
                JOptionPane.showMessageDialog(frmFelGroupCriticalLimitEnhancement.this, " No records found.", "Find Felt Packing Details", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    // find details by doc no
    public void Find(String docNo) {
        ObjFeltGroupMaster.Filter("DOC_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    // find all pending document
    public void FindWaiting() {
        ObjFeltGroupMaster.Filter("DOC_NO IN (SELECT H.DOC_NO FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=769 AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pGroupId) {
        ObjFeltGroupMaster.Filter(" DOC_NO='" + pGroupId + "'");
        ObjFeltGroupMaster.MoveFirst();
        DisplayData();
    }

    private void MoveFirst() {
        ObjFeltGroupMaster.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjFeltGroupMaster.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjFeltGroupMaster.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjFeltGroupMaster.MoveLast();
        DisplayData();
    }

    private void EnableApproval() {
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        OpgHold.setSelected(true);
        txtToRemarks.setEnabled(true);
        SetupApproval();

        //=============== Setting Table Fields ==================//
        DataModelPieceNo.ClearAllReadOnly();
        Table.setEnabled(false);
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
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString());
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString(), ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString(), ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(769, ObjFeltGroupMaster.getAttribute("DOC_NO").getString());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }
    }

    private void FormatGrid() {
        DataModelPieceNo = new EITLTableModel();
        Table.removeAll();

        Table.setModel(DataModelPieceNo);
        TableColumnModel ColModel = Table.getColumnModel();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DataModelPieceNo.addColumn("Sr.No"); //0
        DataModelPieceNo.addColumn("Party Code"); //1
        DataModelPieceNo.addColumn("Party Name"); //2
        DataModelPieceNo.addColumn("Critical Party Limit"); //4
        DataModelPieceNo.addColumn("Enhance Critical Party Limit"); //4
        DataModelPieceNo.addColumn("Outstanding Amt"); //1

        DataModelPieceNo.SetVariable(0, "");
        DataModelPieceNo.SetVariable(1, "GROUP_PARTY_CODE");
        DataModelPieceNo.SetVariable(2, "GROUP_PARTY_NAME");
        DataModelPieceNo.SetVariable(3, "GROUP_PARTY_CRITICAL_LIMIT");
        DataModelPieceNo.SetVariable(4, "GROUP_ENHANCE_PARTY_CRITICAL_LIMIT");
        DataModelPieceNo.SetVariable(5, "GROUP_PARTY_OUTSTANDING");

        DataModelPieceNo.SetReadOnly(0);
        DataModelPieceNo.SetReadOnly(1);
        DataModelPieceNo.SetReadOnly(2);
        DataModelPieceNo.SetReadOnly(3);
        DataModelPieceNo.SetReadOnly(5);

//       for (int i = 1; i <= 6; i++) {
//            DataModelPieceNo.SetReadOnly(i);
//        }
//        int last = Table.getSelectedColumn();
//
//        if (last == 1) {
//            ShowMessage("Enter Press F1 for the Party Code");
//        }
        //int ImportCol=DataModelPieceNo.getColFromVariable("CASH_DISC_FLAG");
    }

    private void FormatGrid1() {
        DataModelBaleNo = new EITLTableModel();
        Table1.removeAll();

        Table1.setModel(DataModelBaleNo);
        TableColumnModel ColModel = Table1.getColumnModel();
        Table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DataModelBaleNo.addColumn("Sr.No"); //0
        DataModelBaleNo.addColumn("Bale No"); //1
        DataModelBaleNo.addColumn("Bale Date"); //2
        DataModelBaleNo.addColumn("Bill Value"); //3
        DataModelBaleNo.addColumn("Party Code"); //3
        DataModelBaleNo.addColumn("Party Name"); //3
        DataModelBaleNo.addColumn("Piece No"); //3

        DataModelBaleNo.SetVariable(0, "");
        DataModelBaleNo.SetVariable(1, "BALE_NO");
        DataModelBaleNo.SetVariable(2, "BALE_DATE");
        DataModelBaleNo.SetVariable(3, "BILL_VALUE");
        DataModelBaleNo.SetVariable(4, "PARTY_CODE");
        DataModelBaleNo.SetVariable(5, "PARTY_NAME");
        DataModelBaleNo.SetVariable(6, "PIECE_NO");
        
        //DataModelPieceNo.SetNumeric(2, true);
        DataModelBaleNo.TableReadOnly(false);

        DataModelBaleNo.SetReadOnly(0);
        DataModelBaleNo.SetReadOnly(1);
        DataModelBaleNo.SetReadOnly(2);
        DataModelBaleNo.SetReadOnly(3);
        DataModelBaleNo.SetReadOnly(4);
        DataModelBaleNo.SetReadOnly(5);
        DataModelBaleNo.SetReadOnly(6);

//       for (int i = 1; i <= 6; i++) {
//            DataModelPieceNo.SetReadOnly(i);
//        }
//        int last = Table1.getSelectedColumn();
//
//        if (last == 1) {
//            ShowMessage("Enter Press F1 for the Party Code");
//        }
        //int ImportCol=DataModelPieceNo.getColFromVariable("CASH_DISC_FLAG");
    }

    private void FormatGridApprovalStatus() {
        DataModelPieceNoApprovalStatus = new EITLTableModel();

        TableApprovalStatus.removeAll();
        TableApprovalStatus.setModel(DataModelPieceNoApprovalStatus);

        //Set the table Readonly
        DataModelPieceNoApprovalStatus.TableReadOnly(true);

        //Add the columns
        DataModelPieceNoApprovalStatus.addColumn("Sr.");
        DataModelPieceNoApprovalStatus.addColumn("User");
        DataModelPieceNoApprovalStatus.addColumn("Department");
        DataModelPieceNoApprovalStatus.addColumn("Status");
        DataModelPieceNoApprovalStatus.addColumn("Received Date");
        DataModelPieceNoApprovalStatus.addColumn("Action Date");
        DataModelPieceNoApprovalStatus.addColumn("Remarks");

        TableColumnModel tcm = TableApprovalStatus.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(3).setPreferredWidth(20);
        tcm.getColumn(4).setPreferredWidth(90);
        tcm.getColumn(5).setPreferredWidth(90);
    }

    private void FormatGridUpdateHistory() {
        DataModelPieceNoUpdateHistory = new EITLTableModel();

        TableUpdateHistory.removeAll();
        TableUpdateHistory.setModel(DataModelPieceNoUpdateHistory);

        //Set the table Readonly
        DataModelPieceNoUpdateHistory.TableReadOnly(true);

        //Add the columns
        DataModelPieceNoUpdateHistory.addColumn("Rev No.");
        DataModelPieceNoUpdateHistory.addColumn("User");
        DataModelPieceNoUpdateHistory.addColumn("Date");
        DataModelPieceNoUpdateHistory.addColumn("Status");
        DataModelPieceNoUpdateHistory.addColumn("Remarks");
        DataModelPieceNoUpdateHistory.addColumn("From Ip");

        TableColumnModel tcm = TableUpdateHistory.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(2).setPreferredWidth(50);
        tcm.getColumn(3).setPreferredWidth(20);
    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    private void Mail() {
        System.out.println("Felt Group Critical Limit Enhancement approved = " + ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString());
        String pBody = "", pSubject = "", recievers = "", pcc = "";
        if (ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("A") || ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {
            if (ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("A")) {
                pSubject = "Notification : Felt Group Critical Limit Enhancement No. " + txtDocNo.getText() + ".";
                pBody = "Felt Group Critical Limit Enhancement No." + txtDocNo.getText() + " has been approved and forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID) + "<br><br><br>";
            }
            if (ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {
                pSubject = "Notification : Felt Group Critical Limit Enhancement No. :" + txtDocNo.getText() + " Final Approved";
                pBody = "Felt Group Critical Limit Enhancement No. " + txtDocNo.getText() + " has been final approved  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID) + "<br><br><br>";
            }
            try {
                pBody += "Document Name : Felt Group Critical Limit Enhancement  <br>";
                pBody += "Document No. : " + txtDocNo.getText() + "  <br>";
                pBody += "Document Date : " + txtDocDate.getText() + "  <br>";
                //pBody += "Party Code : " + txtPartyCode.getText() + " <br>";
                //pBody += "Party Name : " + txtPartyName.getText() + " <br>";
                pBody += "Group Code : " + txtGroupId.getText() + " <br>";
                pBody += "Group Name : " + txtgroupdesc.getText() + " <br>";
                pBody += "Enhancement Reason : " + txtEnhancementReason.getText() + " <br><br><br><br>";

                Connection tmConn;
                Statement tmstmt;
                ResultSet tmrsData;

                tmConn = data.getConn();
                tmstmt = tmConn.createStatement();
                tmrsData = tmstmt.executeQuery("SELECT BALE_NO,DATE_FORMAT(BALE_DATE,'%d/%m/%Y') AS BALE_DATE,BILL_VALUE,PARTY_CODE,PARTY_NAME,PIECE_NO FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL WHERE DOC_NO='" + txtDocNo.getText() + "' ");
                tmrsData.first();
                pBody += "<br>";
                pBody += "<table border=1>";
                pBody += "<tr><td align='center'><b>Sr.No.</b></td>"
                        + "<td align='center'><b>Bale No.</b></td>"
                        + "<td align='center'><b>Bale Date</b></td>"
                        + "<td align='center'><b>Bill Value</b></td>"
                        + "<td align='center'><b>Party Code</b></td>"
                        + "<td align='center'><b>Party Name</b></td>"
                        + "<td align='center'><b>Piece No</b></td>"
                        + "</tr>";
                int j = 1;

                while (!tmrsData.isAfterLast()) {
                    pBody += "<tr>";
                    pBody += "<td>" + j + "</td>";
                    pBody += "<td>" + tmrsData.getString("BALE_NO") + "</td>";
                    pBody += "<td>" + tmrsData.getString("BALE_DATE") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("BILL_VALUE") + "</td>";
                    pBody += "<td>" + tmrsData.getString("PARTY_CODE") + "</td>";
                    pBody += "<td>" + tmrsData.getString("PARTY_NAME") + "</td>";
                    pBody += "<td>" + tmrsData.getString("PIECE_NO") + "</td>";
                    pBody += "</tr>";
                    tmrsData.next();
                    j++;
                }
                pBody += "</table>";
                pBody += "<br><br>";
                tmrsData = tmstmt.executeQuery("SELECT * FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL_ENHANCEMENT WHERE DOC_NO='" + txtDocNo.getText() + "' ");
                tmrsData.first();
                pBody += "<br>";
                pBody += "<table border=1>";
                pBody += "<tr><td align='center'><b>Sr.No.</b></td>"
                        + "<td align='center'><b>Party Code</b></td>"
                        + "<td align='center'><b>Party Name</b></td>"
                        + "<td align='center'><b>Critical Limit</b></td>"
                        + "<td align='center'><b>Enhancement of Critical Limit</b></td>"
                        + "<td align='center'><b>Outstanding</b></td>"
                        + "</tr>";
                j = 1;

                while (!tmrsData.isAfterLast()) {
                    pBody += "<tr>";
                    pBody += "<td>" + j + "</td>";
                    pBody += "<td>" + tmrsData.getString("GROUP_PARTY_CODE") + "</td>";
                    pBody += "<td>" + tmrsData.getString("GROUP_PARTY_NAME") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("GROUP_PARTY_CRITICAL_LIMIT") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("GROUP_ENHANCE_PARTY_CRITICAL_LIMIT") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("GROUP_PARTY_OUTSTANDING") + "</td>";
                    pBody += "</tr>";
                    tmrsData.next();
                    j++;
                }
                pBody += "</table>";
                pBody += "<br><br>";
                pBody += "<br>";
                pBody += "<table border=1>";
                pBody += "<tr><td align='center'><b>Sr.No.</b></td>"
                        + "<td align='center'><b>User</b></td>"
                        + "<td align='center'><b>Status</b></td>"
                        + "<td align='center'><b>Remark</b></td>"
                        + "</tr>";

                HashMap hmApprovalHistory = clsFeltGroupCriticalLimitEnhancement.getHistoryList("", txtDocNo.getText());
                for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                    pBody += "<tr>";

                    clsFeltGroupCriticalLimitEnhancement ObjFeltGroupMaster = (clsFeltGroupCriticalLimitEnhancement) hmApprovalHistory.get(Integer.toString(i));
                    Object[] rowData = new Object[6];
                    pBody += "<td>" + Integer.toString((int) ObjFeltGroupMaster.getAttribute("REVISION_NO").getVal()) + "</td>";

                    pBody += "<td>" + clsUser.getUserName(2, (int) ObjFeltGroupMaster.getAttribute("UPDATED_BY").getVal()) + "</td>";
                    String ApprovalStatus = "";

                    if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                        ApprovalStatus = "Hold";
                    }

                    if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                        ApprovalStatus = "Approved";
                    }

                    if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                        ApprovalStatus = "Final Approved";
                    }

                    if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                        ApprovalStatus = "Waiting";
                    }

                    if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                        ApprovalStatus = "Rejected";
                    }

                    if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                        ApprovalStatus = "Pending";
                    }

                    if ((ObjFeltGroupMaster.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                        ApprovalStatus = "Skiped";
                    }
                    pBody += "<td>" + ApprovalStatus + "</td>";
                    pBody += "<td>" + ObjFeltGroupMaster.getAttribute("APPROVER_REMARKS").getString() + "</td>";
                    pBody += "</tr>";
                }
                pBody += "</table>";
                pBody += "<br><br>";
                pBody += "<br>";
                tmrsData.close();
                tmstmt.close();
                tmConn.close();

                recievers = "sdmlerp@dineshmills.com";
                HashMap hmSendToList;
                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.getComboCode(cmbHierarchy), EITLERPGLOBAL.gNewUserID, true);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
                    if (!to.equals("")) {
                        recievers = recievers + "," + to;
                    }
                    //recievers += ",vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,aditya@dineshmills.com";
                }
                //recievers += ",dharmendra@dineshmills.com,gaurang@dineshmills.com";
                //pcc = "aditya@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,atulshah@dineshmills.com";
                //Remove manoj@dineshmills.com by Dharmendra on 17-05-2019
                pcc = "aditya@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com,hcpatel@dineshmills.com,atulshah@dineshmills.com";
                pBody = pBody + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

                String responce = MailNotification.sendNotificationMail(769, pSubject, pBody, recievers, pcc, EITLERPGLOBAL.getComboCode(cmbHierarchy));
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private double BalanceTransfer(String MainCode, String PartyCode) {
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
                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4")  && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("1")) { //Closed on 29/08/2020 as requested by Mr. Motiani from Felt Sales Dept
//                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4") ) {
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
}
