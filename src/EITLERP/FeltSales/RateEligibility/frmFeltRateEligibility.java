/*
 * frmFeltRateEligibility.java
 *
 * Created on August 22, 2013, 11:20 AM
 */
package EITLERP.FeltSales.RateEligibility;

/**
 *
 * @author
 */
import EITLERP.*;
import javax.swing.JTextField;
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
import TReportWriter.NumWord;

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
import EITLERP.EITLTableCellRenderer;
import EITLERP.FeltSales.FeltQualityRateMaster.frmFeltQltRateMaster;
import EITLERP.FeltSales.GroupMasterAmend.clsFeltGroupMasterAmend;
import EITLERP.FeltSales.Perfomainvoice.clsProforma;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.JavaMailNew;
import EITLERP.LOV;
import EITLERP.frmPendingApprovals;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Production.FeltUser;
import EITLERP.Production.FeltNeedling.frmFeltNeedling;
import EITLERP.Production.FeltMending.frmFeltMending_New;
import EITLERP.Production.FeltWeaving.frmFeltWeaving;
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;
import EITLERP.data;
import EITLERP.clsDocMailer;
import EITLERP.clsSales_Party;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

public class frmFeltRateEligibility extends javax.swing.JApplet {

    private int EditMode = 0;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromUserId = 0;
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    private boolean DoNotEvaluate = false;

    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private clsFeltRateEligibility ObjFeltFinishing;
    private EITLERP.FeltSales.common.FeltInvCalc inv_calculation;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbSendToModel;
    private EITLComboModel cmbUserNameModel;

    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private int FinalApprovedBy = 0;
    public frmPendingApprovals frmPA;

    /**
     * Creates new form frmFeltFinishing
     */
    public void init() {
        System.gc();
        setSize(830, 590);
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

        DataModel = new EITLTableModel();
        ObjFeltFinishing = new clsFeltRateEligibility();
        lblTitle.setForeground(Color.WHITE);

        SetMenuForRights();
        GenerateHierarchyCombo();
        GenerateSendToCombo();
        if (ObjFeltFinishing.LoadData()) {
            ObjFeltFinishing.MoveLast();
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Error occured while Loading Data. Error is " + ObjFeltFinishing.LastError, "DATA LOADING ERROR", JOptionPane.ERROR_MESSAGE);
        }

//        txtpartycode.setEditable(false);
//        txtgroupcode.setEditable(false);
//        txtupnno.setEditable(false);
//        txtpieceno.setEditable(false);
//        txtBookingFrom.setEditable(false);
//        txtBookingTo.setEditable(false);
//        txtDespatchDate.setEditable(false);
//        rbtnPartyCode.setEnabled(false);
//        rbtnGroupCode.setEnabled(false);
//        rbtnUPNNo.setEnabled(false);
//        rbtnPieceNo.setEnabled(false);
        Table.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
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
        jLabel16 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        cmdNextToTab1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        txtpartycode = new javax.swing.JTextField();
        txtpartyname = new javax.swing.JTextField();
        txtgroupcode = new javax.swing.JTextField();
        txtgroupname = new javax.swing.JTextField();
        txtupnno = new javax.swing.JTextField();
        txtpieceno = new javax.swing.JTextField();
        rbtnPartyCode = new javax.swing.JRadioButton();
        rbtnGroupCode = new javax.swing.JRadioButton();
        rbtnUPNNo = new javax.swing.JRadioButton();
        rbtnPieceNo = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        txtDespatchDate = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtBookingFrom = new javax.swing.JTextField();
        txtBookingTo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btnShowList = new javax.swing.JButton();
        chkSelectAll = new javax.swing.JCheckBox();
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
        lblTitle.setText("SPECIAL SANCTION FOR APPLYING OLD RATE - ");
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

        jLabel16.setText("Doc No ");
        Tab1.add(jLabel16);
        jLabel16.setBounds(20, 10, 70, 20);

        txtDocNo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtDocNo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtDocNo.setEnabled(false);
        Tab1.add(txtDocNo);
        txtDocNo.setBounds(90, 10, 150, 21);

        jLabel6.setText("Doc Date");
        Tab1.add(jLabel6);
        jLabel6.setBounds(280, 10, 80, 20);

        txtDocDate.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtDocDate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtDocDate.setEnabled(false);
        Tab1.add(txtDocDate);
        txtDocDate.setBounds(370, 10, 102, 21);

        lblRevNo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(480, 10, 20, 18);

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.add(jPanel5);

        Tab1.add(jPanel4);
        jPanel4.setBounds(10, 40, 760, 5);

        cmdNextToTab1.setMnemonic('N');
        cmdNextToTab1.setText("Next >>");
        cmdNextToTab1.setToolTipText("Next Tab");
        cmdNextToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNextToTab1);
        cmdNextToTab1.setBounds(660, 430, 90, 25);

        jScrollPane4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jScrollPane4KeyReleased(evt);
            }
        });

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
        jScrollPane4.setViewportView(Table);

        Tab1.add(jScrollPane4);
        jScrollPane4.setBounds(10, 160, 760, 270);

        jPanel7.setBackground(new java.awt.Color(153, 153, 153));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel8.setBackground(new java.awt.Color(153, 153, 153));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.add(jPanel8);

        Tab1.add(jPanel7);
        jPanel7.setBounds(10, 150, 760, 5);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setLayout(null);

        txtpartycode.setToolTipText("Press F1 key for search Party Code");
        txtpartycode.setEnabled(false);
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
        jPanel9.add(txtpartycode);
        txtpartycode.setBounds(130, 10, 70, 20);

        txtpartyname.setEditable(false);
        txtpartyname.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtpartyname.setDisabledTextColor(java.awt.Color.black);
        txtpartyname.setEnabled(false);
        jPanel9.add(txtpartyname);
        txtpartyname.setBounds(200, 10, 290, 20);

        txtgroupcode.setToolTipText("Press F1 key for search Party Code");
        txtgroupcode.setEnabled(false);
        txtgroupcode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtgroupcodeFocusLost(evt);
            }
        });
        txtgroupcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtgroupcodeKeyPressed(evt);
            }
        });
        jPanel9.add(txtgroupcode);
        txtgroupcode.setBounds(130, 30, 70, 20);

        txtgroupname.setEditable(false);
        txtgroupname.setDisabledTextColor(java.awt.Color.black);
        txtgroupname.setEnabled(false);
        jPanel9.add(txtgroupname);
        txtgroupname.setBounds(200, 30, 290, 20);

        txtupnno.setToolTipText("Press F1 key for search Party Code");
        txtupnno.setEnabled(false);
        txtupnno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtupnnoKeyPressed(evt);
            }
        });
        jPanel9.add(txtupnno);
        txtupnno.setBounds(130, 50, 140, 20);

        txtpieceno.setEnabled(false);
        jPanel9.add(txtpieceno);
        txtpieceno.setBounds(130, 70, 340, 20);

        rbtnPartyCode.setText("Party Code");
        rbtnPartyCode.setEnabled(false);
        rbtnPartyCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbtnPartyCodeMouseClicked(evt);
            }
        });
        jPanel9.add(rbtnPartyCode);
        rbtnPartyCode.setBounds(10, 10, 120, 20);

        rbtnGroupCode.setText("Group Code");
        rbtnGroupCode.setEnabled(false);
        rbtnGroupCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbtnGroupCodeMouseClicked(evt);
            }
        });
        jPanel9.add(rbtnGroupCode);
        rbtnGroupCode.setBounds(10, 30, 120, 20);

        rbtnUPNNo.setText("UPN No");
        rbtnUPNNo.setEnabled(false);
        rbtnUPNNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbtnUPNNoMouseClicked(evt);
            }
        });
        jPanel9.add(rbtnUPNNo);
        rbtnUPNNo.setBounds(10, 50, 120, 20);

        buttonGroup2.add(rbtnPieceNo);
        rbtnPieceNo.setText("Piece No");
        rbtnPieceNo.setEnabled(false);
        rbtnPieceNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbtnPieceNoMouseClicked(evt);
            }
        });
        jPanel9.add(rbtnPieceNo);
        rbtnPieceNo.setBounds(10, 70, 120, 20);

        Tab1.add(jPanel9);
        jPanel9.setBounds(10, 50, 500, 100);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(null);

        txtDespatchDate.setEnabled(false);
        jPanel2.add(txtDespatchDate);
        txtDespatchDate.setBounds(150, 10, 90, 20);

        jLabel14.setText("Price valid Till Date");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(10, 10, 140, 20);

        Tab1.add(jPanel2);
        jPanel2.setBounds(520, 110, 250, 40);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        txtBookingFrom.setEnabled(false);
        jPanel3.add(txtBookingFrom);
        txtBookingFrom.setBounds(130, 10, 100, 20);

        txtBookingTo.setEnabled(false);
        jPanel3.add(txtBookingTo);
        txtBookingTo.setBounds(130, 30, 100, 20);

        jLabel15.setText("Booking To");
        jPanel3.add(jLabel15);
        jLabel15.setBounds(10, 30, 110, 20);

        jLabel17.setText("Booking From");
        jPanel3.add(jLabel17);
        jLabel17.setBounds(10, 10, 110, 20);

        Tab1.add(jPanel3);
        jPanel3.setBounds(520, 50, 250, 60);

        btnShowList.setText("Show List");
        btnShowList.setEnabled(false);
        btnShowList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowListActionPerformed(evt);
            }
        });
        Tab1.add(btnShowList);
        btnShowList.setBounds(630, 10, 130, 30);

        chkSelectAll.setText("Select All");
        chkSelectAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chkSelectAllMouseClicked(evt);
            }
        });
        Tab1.add(chkSelectAll);
        chkSelectAll.setBounds(20, 430, 170, 23);

        Tab.addTab("Piece Details", Tab1);

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab2FocusGained(evt);
            }
        });
        Tab2.setLayout(null);

        jLabel31.setText("Hierarchy ");
        Tab2.add(jLabel31);
        jLabel31.setBounds(7, 23, 80, 15);

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
        cmbHierarchy.setBounds(86, 20, 184, 24);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(7, 62, 80, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        txtFrom.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtFrom.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFrom);
        txtFrom.setBounds(86, 60, 184, 21);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(7, 97, 61, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setEnabled(false);
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(86, 95, 630, 19);

        jLabel36.setText("Your Action");
        Tab2.add(jLabel36);
        jLabel36.setBounds(7, 130, 73, 15);

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
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });
        OpgReject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgRejectFocusGained(evt);
            }
        });
        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 70, 20);

        buttonGroup1.add(OpgHold);
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
        jLabel33.setBounds(7, 249, 80, 15);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(86, 245, 184, 24);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(7, 288, 80, 15);

        txtToRemarks.setEnabled(false);
        txtToRemarks.setNextFocusableComponent(cmdBackToTab0);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(86, 286, 630, 19);

        cmdBackToTab0.setMnemonic('B');
        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.setToolTipText("Previous Tab");
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(500, 350, 102, 25);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(728, 94, 49, 21);

        cmdNextToTab2.setMnemonic('N');
        cmdNextToTab2.setText("Next >>");
        cmdNextToTab2.setToolTipText("Next Tab");
        cmdNextToTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNextToTab2);
        cmdNextToTab2.setBounds(620, 350, 102, 25);

        Tab.addTab("Approval", Tab2);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(null);

        jLabel26.setText("Document Approval Status");
        jPanel1.add(jLabel26);
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

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(12, 24, 760, 150);

        lblDocumentHistory.setText("Document Update History");
        jPanel1.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(8, 191, 163, 15);

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
        cmdBackToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab1ActionPerformed(evt);
            }
        });
        jPanel1.add(cmdBackToTab1);
        cmdBackToTab1.setBounds(662, 390, 110, 25);

        cmdBackToNormal.setText("Back To Normal");
        cmdBackToNormal.setMargin(new java.awt.Insets(2, 3, 2, 3));
        cmdBackToNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToNormalActionPerformed(evt);
            }
        });
        jPanel1.add(cmdBackToNormal);
        cmdBackToNormal.setBounds(662, 240, 110, 25);

        cmdViewRevisions.setText("View Revisions");
        cmdViewRevisions.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdViewRevisions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewRevisionsActionPerformed(evt);
            }
        });
        jPanel1.add(cmdViewRevisions);
        cmdViewRevisions.setBounds(662, 210, 110, 25);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        jPanel1.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(662, 270, 110, 25);

        Tab.addTab("Status", jPanel1);

        getContentPane().add(Tab);
        Tab.setBounds(2, 66, 790, 510);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
//        new TReportWriter.TReportEngine().PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptFeltFinishing.rpt",new HashMap(),ObjFeltFinishing.getReportData(EITLERPGLOBAL.formatDateDB(txtFeltProductionDate.getText().trim())));
//        EITLERPGLOBAL.PAGE_BREAK=true;
//        HashMap Parameters = new HashMap();
//        Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
//        
//        new TReportWriter.TReportEngine().PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/FELTFINISHING.rpt",Parameters,ObjFeltFinishing.getReportData(txtProductionDocumentNo.getText().trim(),EITLERPGLOBAL.formatDateDB(txtFeltProductionDate.getText().trim())));
//        EITLERPGLOBAL.PAGE_BREAK=true;
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
//        try {
//            URL reportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptFeltFinishing.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&PROD_DATE="+EITLERPGLOBAL.formatDateDB(txtFeltProductionDate.getText()));
//            EITLERPGLOBAL.loginContext.showDocument(reportFile,"_blank");
//        }
//        catch(Exception e) {
//            JOptionPane.showMessageDialog(frmFeltFinishing.this,"File error "+e.getMessage(),"FILE ERROR",JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }

//        PreviewReport();
//        HashMap Parameters = new HashMap();
//        Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
//
//        new TReportWriter.TReportEngine().PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/FELTFINISHING.rpt", Parameters, ObjFeltFinishing.getReportData(txtProductionDocumentNo.getText().trim(), EITLERPGLOBAL.formatDateDB(txtFeltProductionDate.getText().trim())));
//        EITLERPGLOBAL.PAGE_BREAK = true;
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        if (TableUpdateHistory.getRowCount() > 0 && TableUpdateHistory.getSelectedRow() >= 0) {
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText.setText(TableUpdateHistory.getValueAt(TableUpdateHistory.getSelectedRow(), 4).toString());
            bigEdit.ShowEdit();
        } else {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Select a row from Document Update History");
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdBackToNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToNormalActionPerformed
        ObjFeltFinishing.HistoryView = false;
        ObjFeltFinishing.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdBackToNormalActionPerformed

    private void cmdViewRevisionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewRevisionsActionPerformed
        ObjFeltFinishing.ShowHistory(EITLERPGLOBAL.formatDateDB(txtDocDate.getText()), txtDocNo.getText());
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

    private void Tab1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab1FocusGained
        txtDocDate.requestFocus();
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
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(631, txtDocNo.getText())) {
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
        ObjFeltFinishing.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        Cancel();
        if (ObjFeltFinishing.LoadData()) {
            ObjFeltFinishing.MoveLast();
            DisplayData();
        } else {
        }
        SetFields(false);
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        if (JOptionPane.showConfirmDialog(frmFeltRateEligibility.this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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
            OpgFinal.setEnabled(true);
        } else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void cmdNextToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab1ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNextToTab1ActionPerformed

    private void jScrollPane4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane4KeyReleased

    private void txtpartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtpartycodeFocusLost
        // TODO add your handling code here:
        //        if (!txtpartycode.getText().trim().equals("")) {
        if (!txtpartycode.getText().trim().equals("") && data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + txtpartycode.getText().trim() + "' AND MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0")) {
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
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_CLOSE_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0";
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

    private void txtgroupcodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtgroupcodeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtgroupcodeFocusLost

    private void txtgroupcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtgroupcodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT GROUP_CODE,GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE APPROVED=1 AND CANCELED=0 ORDER BY GROUP_CODE";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtgroupcode.setText(aList.ReturnVal);
                txtgroupname.setText(clsFeltGroupMasterAmend.getgroupdesc(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtgroupcodeKeyPressed

    private void txtupnnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtupnnoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            String strSQL = "SELECT DISTINCT MM_UPN_NO,PARTY_NAME,MM_MACHINE_NO,MM_MACHINE_POSITION_DESC FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL,DINESHMILLS.D_SAL_PARTY_MASTER WHERE MM_PARTY_CODE=PARTY_CODE ";
            //AND MM_PARTY_CODE= '832072' AND MM_MACHINE_NO=1 AND MM_MACHINE_POSITION=1 ORDER BY MM_MACHINE_POSITION  ";

            if (!txtpartycode.getText().trim().equals("")) {
                strSQL = strSQL + " AND MM_PARTY_CODE= '" + txtpartycode.getText() + "' ";
            }

            strSQL = strSQL + " ORDER BY MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION ";

            //            aList.SQL = "SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO!=0";
            aList.SQL = strSQL;
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtupnno.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtupnnoKeyPressed

    private void rbtnPartyCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbtnPartyCodeMouseClicked
        // TODO add your handling code here:
//        if (EditMode == EITLERPGLOBAL.ADD) {
////            ClearFields();
//            txtpartycode.setEnabled(true);
//            txtgroupcode.setEnabled(false);
//            txtupnno.setEnabled(false);
//            txtpieceno.setEnabled(false);
//
//            txtBookingFrom.setEnabled(true);
//            txtBookingTo.setEnabled(true);
//
//            txtpartycode.setText("");
//            txtpartyname.setText("");
//            txtgroupcode.setText("");
//            txtgroupname.setText("");
//            txtupnno.setText("");
//            txtpieceno.setText("");
//
//            txtBookingFrom.setText("");
//            txtBookingTo.setText("");
//            txtDespatchDate.setText("");
//
//            txtFromRemarks.setText("");
//            txtToRemarks.setText("");
//            FormatGrid();
//        }
    }//GEN-LAST:event_rbtnPartyCodeMouseClicked

    private void rbtnGroupCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbtnGroupCodeMouseClicked
        // TODO add your handling code here:
//        if (EditMode == EITLERPGLOBAL.ADD) {
////            ClearFields();
//            txtpartycode.setEnabled(false);
//            txtgroupcode.setEnabled(true);
//            txtupnno.setEnabled(false);
//            txtpieceno.setEnabled(false);
//
//            txtBookingFrom.setEnabled(true);
//            txtBookingTo.setEnabled(true);
//
//            txtpartycode.setText("");
//            txtpartyname.setText("");
//            txtgroupcode.setText("");
//            txtgroupname.setText("");
//            txtupnno.setText("");
//            txtpieceno.setText("");
//
//            txtBookingFrom.setText("");
//            txtBookingTo.setText("");
//            txtDespatchDate.setText("");
//
//            txtFromRemarks.setText("");
//            txtToRemarks.setText("");
//            FormatGrid();
//        }
    }//GEN-LAST:event_rbtnGroupCodeMouseClicked

    private void rbtnUPNNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbtnUPNNoMouseClicked
        // TODO add your handling code here:
//        if (EditMode == EITLERPGLOBAL.ADD) {
////            ClearFields();
//            txtpartycode.setEnabled(false);
//            txtgroupcode.setEnabled(false);
//            txtupnno.setEnabled(true);
//            txtpieceno.setEnabled(false);
//
//            txtBookingFrom.setEnabled(true);
//            txtBookingTo.setEnabled(true);
//
//            txtpartycode.setText("");
//            txtpartyname.setText("");
//            txtgroupcode.setText("");
//            txtgroupname.setText("");
//            txtupnno.setText("");
//            txtpieceno.setText("");
//
//            txtBookingFrom.setText("");
//            txtBookingTo.setText("");
//            txtDespatchDate.setText("");
//
//            txtFromRemarks.setText("");
//            txtToRemarks.setText("");
//            FormatGrid();
//        }
    }//GEN-LAST:event_rbtnUPNNoMouseClicked

    private void rbtnPieceNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbtnPieceNoMouseClicked
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD) {
            txtpartycode.setEnabled(false);
            txtgroupcode.setEnabled(false);
            txtupnno.setEnabled(false);
            txtpieceno.setEnabled(true);

            txtBookingFrom.setEnabled(false);
            txtBookingTo.setEnabled(false);

            txtpartycode.setText("");
            txtpartyname.setText("");
            txtgroupcode.setText("");
            txtgroupname.setText("");
            txtupnno.setText("");
            txtpieceno.setText("");

            txtBookingFrom.setText("");
            txtBookingTo.setText("");
            txtDespatchDate.setText("");

            txtFromRemarks.setText("");
            txtToRemarks.setText("");
            FormatGrid();
        }
    }//GEN-LAST:event_rbtnPieceNoMouseClicked

    private void btnShowListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowListActionPerformed
        // TODO add your handling code here:
        //        GeneratePieceDelink();

        if (rbtnPartyCode.isSelected() && txtpartycode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Party Code");
            return;
        }
        if (rbtnGroupCode.isSelected() && txtgroupcode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Group Code");
            return;
        }
        if (rbtnUPNNo.isSelected() && txtupnno.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter UPN No");
            return;
        }
        if (rbtnPieceNo.isSelected() && txtpieceno.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Piece No");
            return;
        }
        
        if (!rbtnPieceNo.isSelected()) {
            if (txtBookingFrom.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please Enter Booking From Date");
                txtBookingFrom.requestFocus();
                txtBookingFrom.setText("");
                return;
            }
            if (!EITLERPGLOBAL.isDate(txtBookingFrom.getText())) {
                JOptionPane.showMessageDialog(null, "Please Enter Booking From Date in DD/MM/YYYY format.");
                txtBookingFrom.requestFocus();
                txtBookingFrom.setText("");
                return;
            }
            if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText()) + "',CURDATE()) FROM DUAL") > 0) {
                JOptionPane.showMessageDialog(null, "Please Enter Less than or Equals Current Date in Booking From Date");
                txtBookingFrom.requestFocus();
                txtBookingFrom.setText("");
                return;
            }
//            if (data.getIntValueFromDB("SELECT DATEDIFF('2019-04-01','" + EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText()) + "') FROM DUAL") > 0) {
//                JOptionPane.showMessageDialog(null, "Please Enter Booking From Date Greater than or Equals 01/04/2019");
//                txtBookingFrom.requestFocus();
//                txtBookingFrom.setText("");
//                return;
//            }
            if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText()) + "','2023-03-31') FROM DUAL") > 0) {
                JOptionPane.showMessageDialog(null, "Please Enter Booking From Date Less than or Equals 31/03/2023");
                txtBookingFrom.requestFocus();
                txtBookingFrom.setText("");
                return;
            }

            if (txtBookingTo.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please Enter Booking To Date");
                txtBookingTo.requestFocus();
                txtBookingTo.setText("");
                return;
            }
            if (!EITLERPGLOBAL.isDate(txtBookingTo.getText())) {
                JOptionPane.showMessageDialog(null, "Please Enter Booking To Date in DD/MM/YYYY format.");
                txtBookingTo.requestFocus();
                txtBookingTo.setText("");
                return;
            }
            if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtBookingTo.getText()) + "','" + EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText()) + "') FROM DUAL") < 0) {
                JOptionPane.showMessageDialog(null, "Please Enter Greater Date than Booking From Date in Booking To Date");
                txtBookingTo.requestFocus();
                txtBookingTo.setText("");
                return;
            }
            if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtBookingTo.getText()) + "','2023-03-31') FROM DUAL") > 0) {
                JOptionPane.showMessageDialog(null, "Please Enter Booking To Date Less than or Equals 31/03/2023");
                txtBookingTo.requestFocus();
                txtBookingTo.setText("");
                return;
            }
        }

        if (txtDespatchDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Despatch Date");
            txtDespatchDate.requestFocus();
            txtDespatchDate.setText("");
            return;
        }
        if (!EITLERPGLOBAL.isDate(txtDespatchDate.getText())) {
            JOptionPane.showMessageDialog(null, "Please Enter Despatch Date in DD/MM/YYYY format.");
            txtDespatchDate.requestFocus();
            txtDespatchDate.setText("");
            return;
        }
        if (data.getIntValueFromDB("SELECT DATEDIFF(CURDATE(),'" + EITLERPGLOBAL.formatDateDB(txtDespatchDate.getText()) + "') FROM DUAL") > 0) {
            JOptionPane.showMessageDialog(null, "Please Enter Greater than or Equals Current Date in Despatch Date");
            txtDespatchDate.requestFocus();
            txtDespatchDate.setText("");
            return;
        }
//        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtDespatchDate.getText()) + "','2020-06-30') FROM DUAL") > 0) {
//            JOptionPane.showMessageDialog(null, "Please Enter Despatch Date Less than or Equals 30/06/2020");
//            txtDespatchDate.requestFocus();
//            txtDespatchDate.setText("");
//            return;
//        }
        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtDespatchDate.getText()) + "','2023-03-31') FROM DUAL") > 0) {
            JOptionPane.showMessageDialog(null, "Please Enter Despatch Date Less than or Equals 31/03/2023");
            txtDespatchDate.requestFocus();
            txtDespatchDate.setText("");
            return;
        }

        FormatGrid();
        GenerateList();
    }//GEN-LAST:event_btnShowListActionPerformed

    private void chkSelectAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkSelectAllMouseClicked
        // TODO add your handling code here:
        if (chkSelectAll.isSelected()) {
            for (int c=0; c<Table.getRowCount(); c++) {
                Table.getModel().setValueAt(true, c, 0);
            }
        } else {
            for (int c=0; c<Table.getRowCount(); c++) {
                Table.getModel().setValueAt(false, c, 0);
            }
        }
    }//GEN-LAST:event_chkSelectAllMouseClicked

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:
        int chkCount = 0;
        for (int a=0; a<Table.getRowCount(); a++) {
            if (Table.getValueAt(a, 0).equals(true))
                chkCount++;
            }
        if (chkCount==Table.getRowCount()) {
            chkSelectAll.setSelected(true);
        } else {
            chkSelectAll.setSelected(false);
        }
    }//GEN-LAST:event_TableMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable Table;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnShowList;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chkSelectAll;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
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
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewRevisions;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JRadioButton rbtnGroupCode;
    private javax.swing.JRadioButton rbtnPartyCode;
    private javax.swing.JRadioButton rbtnPieceNo;
    private javax.swing.JRadioButton rbtnUPNNo;
    private javax.swing.JTextField txtBookingFrom;
    private javax.swing.JTextField txtBookingTo;
    private javax.swing.JTextField txtDespatchDate;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtgroupcode;
    private javax.swing.JTextField txtgroupname;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JTextField txtpartyname;
    private javax.swing.JTextField txtpieceno;
    private javax.swing.JTextField txtupnno;
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

        btnShowList.setEnabled(pStat);

        rbtnPartyCode.setEnabled(false);
        rbtnGroupCode.setEnabled(false);
        rbtnUPNNo.setEnabled(false);
        
//        rbtnPartyCode.setEnabled(pStat);
//        rbtnGroupCode.setEnabled(pStat);
//        rbtnUPNNo.setEnabled(pStat);
        rbtnPieceNo.setEnabled(pStat);

        txtpartycode.setEnabled(pStat);
        txtpartyname.setEnabled(pStat);
        txtgroupcode.setEnabled(pStat);
        txtgroupname.setEnabled(pStat);
        txtupnno.setEnabled(pStat);
        txtpieceno.setEnabled(pStat);

        txtBookingFrom.setEnabled(pStat);
        txtBookingTo.setEnabled(pStat);
        txtDespatchDate.setEnabled(pStat);

        Table.setEnabled(pStat);

        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);

        SetupApproval();
    }

    private void ClearFields() {
        txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());
        txtDocNo.setText("");

        txtpartycode.setText("");
        txtpartyname.setText("");
        txtgroupcode.setText("");
        txtgroupname.setText("");
        txtupnno.setText("");
        txtpieceno.setText("");

        txtBookingFrom.setText("");
        txtBookingTo.setText("");
        txtDespatchDate.setText("");

        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        FormatGrid();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
    }

    //Display data on the Screen
    private void DisplayData() {
        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, 631)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//
        //=========== Title Bar Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (ObjFeltFinishing.getAttribute("APPROVED").getInt() == 1) {
                    lblTitle.setBackground(Color.BLUE);
                } else {
                    lblTitle.setBackground(Color.GRAY);
                }

                if (ObjFeltFinishing.getAttribute("CANCELED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
            //============================================//

            String docDate = EITLERPGLOBAL.formatDate(ObjFeltFinishing.getAttribute("DOC_DATE").getString());
            String docNo = ObjFeltFinishing.getAttribute("DOC_NO").getString();

            String HPartyCode = ObjFeltFinishing.getAttribute("PARTY_CODE_HEADER").getString();
            String HPartyName = ObjFeltFinishing.getAttribute("PARTY_NAME_HEADER").getString();
            String HGroupCode = ObjFeltFinishing.getAttribute("GROUP_CODE_HEADER").getString();
            String HGroupName = ObjFeltFinishing.getAttribute("GROUP_NAME_HEADER").getString();
            String HUPNNo = ObjFeltFinishing.getAttribute("UPN_HEADER").getString();
            String HPieceNo = ObjFeltFinishing.getAttribute("PIECE_NO_HEADER").getString();

            String HBookingFrom = EITLERPGLOBAL.formatDate(ObjFeltFinishing.getAttribute("BOOKING_DATE_FROM").getString());
            String HBookingTo = EITLERPGLOBAL.formatDate(ObjFeltFinishing.getAttribute("BOOKING_DATE_TO").getString());
            String HDespatchDate = EITLERPGLOBAL.formatDate(ObjFeltFinishing.getAttribute("DESPATCH_DATE").getString());

            lblTitle.setText("SPECIAL SANCTION FOR APPLYING OLD RATE - " + docNo);
            lblRevNo.setText(Integer.toString((int) ObjFeltFinishing.getAttribute("REVISION_NO").getVal()));

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) ObjFeltFinishing.getAttribute("HIERARCHY_ID").getVal());
            DoNotEvaluate = true;

            txtDocDate.setText(docDate);
            txtDocNo.setText(docNo);

            if (!HPartyCode.equals("")) {
                rbtnPartyCode.setSelected(true);
            }
            if (!HGroupCode.equals("")) {
                rbtnGroupCode.setSelected(true);
            }
            if (!HUPNNo.equals("")) {
                rbtnUPNNo.setSelected(true);
            }
            if (!HPieceNo.equals("")) {
                rbtnPieceNo.setSelected(true);
            }
            txtpartycode.setText(HPartyCode);
            txtpartyname.setText(HPartyName);
            txtgroupcode.setText(HGroupCode);
            txtgroupname.setText(HGroupName);
            txtupnno.setText(HUPNNo);
            txtpieceno.setText(HPieceNo);

            txtBookingFrom.setText(HBookingFrom);
            txtBookingTo.setText(HBookingTo);
            txtDespatchDate.setText(HDespatchDate);

            //GenerateHierarchyCombo();
            FormatGrid();

            //Now Generate Table
            for (int i = 1; i <= ObjFeltFinishing.hmFeltFinishingDetails.size(); i++) {
                clsFeltRateEligibilityDetails ObjFeltFinishingDetails = (clsFeltRateEligibilityDetails) ObjFeltFinishing.hmFeltFinishingDetails.get(Integer.toString(i));

                Object[] rowData = new Object[25];

                if (ObjFeltFinishingDetails.getAttribute("SELECTED_FLAG").getString().equalsIgnoreCase("1")) {
                    rowData[0] = true;
                } else {
                    rowData[0] = false;
                }
                rowData[1] = Integer.toString(i);
                rowData[2] = ObjFeltFinishingDetails.getAttribute("PIECE_NO").getString();
                rowData[3] = ObjFeltFinishingDetails.getAttribute("PARTY_CODE").getString();
                rowData[4] = ObjFeltFinishingDetails.getAttribute("PARTY_NAME").getString();
                rowData[5] = ObjFeltFinishingDetails.getAttribute("UPN").getString();
                rowData[6] = ObjFeltFinishingDetails.getAttribute("GROUP_CODE").getString();
                rowData[7] = ObjFeltFinishingDetails.getAttribute("GROUP_NAME").getString();
                rowData[8] = ObjFeltFinishingDetails.getAttribute("MACHINE_NO").getString();
                rowData[9] = ObjFeltFinishingDetails.getAttribute("POSITION_NO").getString();
                rowData[10] = ObjFeltFinishingDetails.getAttribute("POSITION_DESC").getString();
                rowData[11] = ObjFeltFinishingDetails.getAttribute("PRODUCT_CODE").getString();
                rowData[12] = ObjFeltFinishingDetails.getAttribute("PRODUCT_GROUP").getString();

                DataModel.addRow(rowData);
            }

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap hmList = new HashMap();

            hmList = clsFeltProductionApprovalFlow.getDocumentFlow(631, docNo);
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
            HashMap hmApprovalHistory = clsFeltRateEligibility.getHistoryList(txtDocNo.getText());
            for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                clsFeltRateEligibility ObjFeltFinishing = (clsFeltRateEligibility) hmApprovalHistory.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjFeltFinishing.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(2, (int) ObjFeltFinishing.getAttribute("UPDATED_BY").getVal());
                rowData[2] = ObjFeltFinishing.getAttribute("ENTRY_DATE").getString();

                String ApprovalStatus = "";

                if ((ObjFeltFinishing.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }

                if ((ObjFeltFinishing.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if ((ObjFeltFinishing.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                    FinalApprovedBy = (int) ObjFeltFinishing.getAttribute("UPDATED_BY").getVal();
                }

                if ((ObjFeltFinishing.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if ((ObjFeltFinishing.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if ((ObjFeltFinishing.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if ((ObjFeltFinishing.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                rowData[3] = ApprovalStatus;
                rowData[4] = ObjFeltFinishing.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjFeltFinishing.getAttribute("FROM_IP").getString();

                DataModelUpdateHistory.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DoNotEvaluate = false;
    }

    private void FormatGrid() {
        try {
            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            Table.setAutoResizeMode(0);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();

            DataModel.addColumn("Select"); //0
            DataModel.addColumn("SrNo"); //1
            DataModel.addColumn("Piece No"); //2
            DataModel.addColumn("Party Code"); //3
            DataModel.addColumn("Party Name"); //4
            DataModel.addColumn("UPN"); //5
            DataModel.addColumn("Group Code"); //6
            DataModel.addColumn("Group Name"); //7
            DataModel.addColumn("Machine No"); //8
            DataModel.addColumn("Position No"); //9
            DataModel.addColumn("Position Desc"); //10
            DataModel.addColumn("Product Code"); //11
            DataModel.addColumn("Product Group"); //12

            Renderer.setCustomComponent(0, "CheckBox");
            Table.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

            DataModel.SetVariable(0, "PI_SELECTED_FLAG"); //0
            DataModel.SetVariable(1, "SR_NO"); //1
            DataModel.SetVariable(2, "PIECE_NO"); //2
            DataModel.SetVariable(3, "PARTY_CODE"); //3
            DataModel.SetVariable(4, "PARTY_NAME"); //4
            DataModel.SetVariable(5, "UPN"); //5
            DataModel.SetVariable(6, "GROUP_CODE"); //6
            DataModel.SetVariable(7, "GROUP_NAME"); //7
            DataModel.SetVariable(8, "MACHINE_NO"); //11
            DataModel.SetVariable(9, "POSITION_NO"); //12
            DataModel.SetVariable(10, "POSITION_DESC"); //13
            DataModel.SetVariable(11, "PRODUCT_CODE"); //14
            DataModel.SetVariable(12, "PRODUCT_GROUP"); //15

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
            DataModel.SetReadOnly(11);
            DataModel.SetReadOnly(12);

            Table.getColumnModel().getColumn(0).setMinWidth(50);
            Table.getColumnModel().getColumn(0).setMaxWidth(50);
            Table.getColumnModel().getColumn(1).setMinWidth(50);
            Table.getColumnModel().getColumn(1).setMaxWidth(50);
            Table.getColumnModel().getColumn(2).setMinWidth(80);
//            Table.getColumnModel().getColumn(2).setMaxWidth(80);
            Table.getColumnModel().getColumn(3).setMinWidth(80);
            Table.getColumnModel().getColumn(3).setMaxWidth(80);
            Table.getColumnModel().getColumn(4).setMinWidth(120);
            Table.getColumnModel().getColumn(5).setMinWidth(100);
//            Table.getColumnModel().getColumn(5).setMaxWidth(100);
            Table.getColumnModel().getColumn(6).setMinWidth(80);
            Table.getColumnModel().getColumn(6).setMaxWidth(80);
            Table.getColumnModel().getColumn(7).setMinWidth(120);
//            Table.getColumnModel().getColumn(7).setMaxWidth(80);
            Table.getColumnModel().getColumn(8).setMinWidth(50);
            Table.getColumnModel().getColumn(8).setMaxWidth(80);
            Table.getColumnModel().getColumn(9).setMinWidth(50);
            Table.getColumnModel().getColumn(9).setMaxWidth(80);
            Table.getColumnModel().getColumn(10).setMinWidth(120);
//            Table.getColumnModel().getColumn(10).setMaxWidth(120);
            Table.getColumnModel().getColumn(11).setMinWidth(80);
//            Table.getColumnModel().getColumn(11).setMaxWidth(80);
            Table.getColumnModel().getColumn(12).setMinWidth(80);
//            Table.getColumnModel().getColumn(12).setMaxWidth(80);

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

        hmHierarchyList = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=631 ");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            hmHierarchyList = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=631 ");
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
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(631, ObjFeltFinishing.getAttribute("DOC_NO").getString());
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(631, txtDocNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(631, txtDocNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(631, txtDocNo.getText());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
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
            int FromUserID = clsFeltProductionApprovalFlow.getFromID(631, ObjFeltFinishing.getAttribute("DOC_NO").getString());
            lnFromUserId = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(631, FromUserID, ObjFeltFinishing.getAttribute("DOC_NO").getString());

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
            if (clsFeltProductionApprovalFlow.IsCreator(631, txtDocNo.getText())) {
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6248, 62481)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6248, 62482)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6248, 62483)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6248, 62485)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }

    private void Add() {

        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 631;
        aList.FirstFreeNo = 304;

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
        txtDocNo.setText(clsFeltRateEligibility.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 631, FFNo, false));
        txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());

        lblTitle.setText("SPECIAL SANCTION FOR APPLYING OLD RATE - " + txtDocNo.getText());
        lblTitle.setBackground(Color.GRAY);

//        rbtnPartyCode.setSelected(true);
//        rbtnPartyCodeMouseClicked(null);
        rbtnPieceNo.setSelected(true);
        rbtnPieceNoMouseClicked(null);
    }

    private void Edit() {
        String productionDocumentNo = (String) ObjFeltFinishing.getAttribute("DOC_NO").getObj();
        if (ObjFeltFinishing.IsEditable(productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();

            if (clsFeltProductionApprovalFlow.IsCreator(631, productionDocumentNo)) {
                SetFields(false);
                Table.setEnabled(true);

                cmbHierarchy.setEnabled(true);
                OpgApprove.setEnabled(true);
                OpgReject.setEnabled(true);
                OpgFinal.setEnabled(true);
                OpgHold.setEnabled(true);
                cmbSendTo.setEnabled(true);
            } else {
                EnableApproval();
            }
        } else {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "EDITING ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Delete() {
        if (ObjFeltFinishing.CanDelete(txtDocNo.getText(), txtDocDate.getText(), EITLERPGLOBAL.gNewUserID)) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, ObjFeltFinishing.LastError, "DELETION ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Save() {
        String docNo, docDate, Remark;
        String SPartyCode, SPartyName, SGroupCode, SGroupName, SUPNNo, SPieceNo;
        String SBookingFrom, SBookingTo, SDespatchDate;
        float Tagweight = 0, weight = 0, width = 0, length = 0;
        int i = 0, j = 0;
        docDate = txtDocDate.getText().trim();
        docNo = txtDocNo.getText().trim();
        SPartyCode = txtpartycode.getText().trim();
        SPartyName = txtpartyname.getText().trim();
        SGroupCode = txtgroupcode.getText().trim();
        SGroupName = txtgroupname.getText().trim();
        SUPNNo = txtupnno.getText().trim();
        SPieceNo = txtpieceno.getText().trim();

        SBookingFrom = txtBookingFrom.getText().trim();
        SBookingTo = txtBookingTo.getText().trim();
        SDespatchDate = txtDespatchDate.getText().trim();

        try {

            ObjFeltFinishing.hmFeltFinishingDetails.clear();
            //Check the entered details in Table.
            for (i = 0; i <= Table.getRowCount() - 1; i++) {

                clsFeltRateEligibilityDetails ObjFeltFinishingDetails = new clsFeltRateEligibilityDetails();

                String flg = String.valueOf(Table.getValueAt(i, 0));
                flg = flg.toLowerCase();
                if (flg.equals("true")) {
                    ObjFeltFinishingDetails.setAttribute("SELECTED_FLAG", "1");
                } else {
                    ObjFeltFinishingDetails.setAttribute("SELECTED_FLAG", "0");
                }
                ObjFeltFinishingDetails.setAttribute("PIECE_NO", DataModel.getValueByVariable("PIECE_NO", i));
                ObjFeltFinishingDetails.setAttribute("PARTY_CODE", DataModel.getValueByVariable("PARTY_CODE", i));
                ObjFeltFinishingDetails.setAttribute("PARTY_NAME", DataModel.getValueByVariable("PARTY_NAME", i));
                ObjFeltFinishingDetails.setAttribute("UPN", DataModel.getValueByVariable("UPN", i));
                ObjFeltFinishingDetails.setAttribute("GROUP_CODE", DataModel.getValueByVariable("GROUP_CODE", i));
                ObjFeltFinishingDetails.setAttribute("GROUP_NAME", DataModel.getValueByVariable("GROUP_NAME", i));
//                ObjFeltFinishingDetails.setAttribute("BOOKING_DATE_FROM", DataModel.getValueByVariable("BOOKING_DATE_FROM", i));
//                ObjFeltFinishingDetails.setAttribute("BOOKING_DATE_TO", DataModel.getValueByVariable("BOOKING_DATE_TO", i));
//                ObjFeltFinishingDetails.setAttribute("DESPATCH_DATE", DataModel.getValueByVariable("DESPATCH_DATE", i));
                ObjFeltFinishingDetails.setAttribute("MACHINE_NO", DataModel.getValueByVariable("MACHINE_NO", i));
                ObjFeltFinishingDetails.setAttribute("POSITION_NO", DataModel.getValueByVariable("POSITION_NO", i));
                ObjFeltFinishingDetails.setAttribute("POSITION_DESC", DataModel.getValueByVariable("POSITION_DESC", i));
                ObjFeltFinishingDetails.setAttribute("PRODUCT_CODE", DataModel.getValueByVariable("PRODUCT_CODE", i));
                ObjFeltFinishingDetails.setAttribute("PRODUCT_GROUP", DataModel.getValueByVariable("PRODUCT_GROUP", i));

                ObjFeltFinishing.hmFeltFinishingDetails.put(Integer.toString(ObjFeltFinishing.hmFeltFinishingDetails.size() + 1), ObjFeltFinishingDetails);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Enter Correct Details at Row " + (i + 1) + " and Column " + (j + 1) + ". Error is " + nfe.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            j = 0;
            nfe.printStackTrace();
            return;
        }

        if (rbtnPartyCode.isSelected() && SPartyCode.equals("")) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Party Code");
            return;
        }
        if (rbtnGroupCode.isSelected() && SGroupCode.equals("")) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Group Code");
            return;
        }
        if (rbtnUPNNo.isSelected() && SUPNNo.equals("")) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter UPN No");
            return;
        }
        if (rbtnPieceNo.isSelected() && SPieceNo.equals("")) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Piece No");
            return;
        }

        if (!rbtnPieceNo.isSelected()) {
            if (txtBookingFrom.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Booking From Date");
                txtBookingFrom.requestFocus();
                txtBookingFrom.setText("");
                return;
            }
            if (!EITLERPGLOBAL.isDate(txtBookingFrom.getText())) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Booking From Date in DD/MM/YYYY format.");
                txtBookingFrom.requestFocus();
                txtBookingFrom.setText("");
                return;
            }
            if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText()) + "',CURDATE()) FROM DUAL") > 0) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Less than or Equals Current Date in Booking From Date");
                txtBookingFrom.requestFocus();
                txtBookingFrom.setText("");
                return;
            }
//            if (data.getIntValueFromDB("SELECT DATEDIFF('2019-04-01','" + EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText()) + "') FROM DUAL") > 0) {
//                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Booking From Date Greater than or Equals 01/04/2019");
//                txtBookingFrom.requestFocus();
//                txtBookingFrom.setText("");
//                return;
//            }
            if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText()) + "','2023-03-31') FROM DUAL") > 0) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Booking From Date Less than or Equals 31/03/2023");
                txtBookingFrom.requestFocus();
                txtBookingFrom.setText("");
                return;
            }

            if (txtBookingTo.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Booking To Date");
                txtBookingTo.requestFocus();
                txtBookingTo.setText("");
                return;
            }
            if (!EITLERPGLOBAL.isDate(txtBookingTo.getText())) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Booking To Date in DD/MM/YYYY format.");
                txtBookingTo.requestFocus();
                txtBookingTo.setText("");
                return;
            }
            if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtBookingTo.getText()) + "','" + EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText()) + "') FROM DUAL") < 0) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Greater Date than Booking From Date in Booking To Date");
                txtBookingTo.requestFocus();
                txtBookingTo.setText("");
                return;
            }
            if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtBookingTo.getText()) + "','2023-03-31') FROM DUAL") > 0) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Booking To Date Less than or Equals 31/03/2023");
                txtBookingTo.requestFocus();
                txtBookingTo.setText("");
                return;
            }
        }

        if (txtDespatchDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Despatch Date");
            txtDespatchDate.requestFocus();
            txtDespatchDate.setText("");
            return;
        }
        if (!EITLERPGLOBAL.isDate(txtDespatchDate.getText())) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Despatch Date in DD/MM/YYYY format.");
            txtDespatchDate.requestFocus();
            txtDespatchDate.setText("");
            return;
        }
        if (data.getIntValueFromDB("SELECT DATEDIFF(CURDATE(),'" + EITLERPGLOBAL.formatDateDB(txtDespatchDate.getText()) + "') FROM DUAL") > 0) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Greater than or Equals Current Date in Despatch Date");
            txtDespatchDate.requestFocus();
            txtDespatchDate.setText("");
            return;
        }
//        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtDespatchDate.getText()) + "','2020-06-30') FROM DUAL") > 0) {
//            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Despatch Date Less than or Equals 30/06/2020");
//            txtDespatchDate.requestFocus();
//            txtDespatchDate.setText("");
//            return;
//        }
        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtDespatchDate.getText()) + "','2023-03-31') FROM DUAL") > 0) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Please Enter Despatch Date Less than or Equals 31/03/2023");
            txtDespatchDate.requestFocus();
            txtDespatchDate.setText("");
            return;
        }
        
        if (Table.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Document not Saved as Detail List does not contain any Data");
            return;
        }

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Select the hierarchy.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Select the Approval Action.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Enter the remarks for rejection", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Select the user, to whom rejected document to be send", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //set data for insert/update
        ObjFeltFinishing.setAttribute("DOC_DATE", docDate);
        ObjFeltFinishing.setAttribute("DOC_NO", docNo);

        ObjFeltFinishing.setAttribute("PIECE_NO_HEADER", SPieceNo);
        ObjFeltFinishing.setAttribute("PARTY_CODE_HEADER", SPartyCode);
        ObjFeltFinishing.setAttribute("PARTY_NAME_HEADER", SPartyName);
        ObjFeltFinishing.setAttribute("UPN_HEADER", SUPNNo);
        ObjFeltFinishing.setAttribute("GROUP_CODE_HEADER", SGroupCode);
        ObjFeltFinishing.setAttribute("GROUP_NAME_HEADER", SGroupName);

        ObjFeltFinishing.setAttribute("BOOKING_DATE_FROM", SBookingFrom);
        ObjFeltFinishing.setAttribute("BOOKING_DATE_TO", SBookingTo);
        ObjFeltFinishing.setAttribute("DESPATCH_DATE", SDespatchDate);

        SetData();

        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjFeltFinishing.Insert()) {
//                if (OpgFinal.isSelected()) {
//                    
//                }
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Error occured while saving. Error is " + ObjFeltFinishing.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjFeltFinishing.Update()) {
//                if (OpgFinal.isSelected()) {
//                    
//                }
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, "Error occured while saving editing. Error is " + ObjFeltFinishing.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Notification();
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
    private void SetData() {
        //-------- Update Approval Specific Fields -----------//
        ObjFeltFinishing.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjFeltFinishing.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjFeltFinishing.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjFeltFinishing.setAttribute("FROM_REMARKS", txtToRemarks.getText().trim());
        ObjFeltFinishing.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);

        //ObjFeltFinishing.setAttribute("UPDATED_BY",EITLERPGLOBAL.gNewUserID);
        if (OpgApprove.isSelected()) {
            ObjFeltFinishing.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjFeltFinishing.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjFeltFinishing.setAttribute("APPROVAL_STATUS", "R");
            ObjFeltFinishing.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjFeltFinishing.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

//        if(EditMode==EITLERPGLOBAL.ADD) {
//            ObjFeltFinishing.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
//            ObjFeltFinishing.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//        }else {
//            ObjFeltFinishing.setAttribute("CREATED_BY", (int)ObjFeltFinishing.getAttribute("CREATED_BY").getVal());
//            ObjFeltFinishing.setAttribute("CREATED_DATE", ObjFeltFinishing.getAttribute("CREATED_DATE").getString());
//            ObjFeltFinishing.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
//        }
        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjFeltFinishing.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjFeltFinishing.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
        } else {
            //ObjFeltFinishing.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
            //ObjFeltFinishing.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            ObjFeltFinishing.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjFeltFinishing.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
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
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.RateEligibility.frmFindFeltRateEligibility", true);
        frmFindFeltRateEligibility ObjFindFeltFinishing = (frmFindFeltRateEligibility) ObjLoader.getObj();

        if (ObjFindFeltFinishing.Cancelled == false) {
            if (!ObjFeltFinishing.Filter(ObjFindFeltFinishing.stringFindQuery)) {
                JOptionPane.showMessageDialog(frmFeltRateEligibility.this, " No records found.", "Find Felt Special Sanction Rate Eligibility Details", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    // find details by production date
    public void Find(String docNo) {
        ObjFeltFinishing.Filter(" AND DOC_NO='" + docNo + "' ");
        SetMenuForRights();
        DisplayData();
    }

    // find details by piece no.
    public void Find(String pieceNo, String prodDate) {
        ObjFeltFinishing.Filter(" AND PIECE_NO='" + pieceNo + "' ");
        SetMenuForRights();
        DisplayData();
    }

    public void FindByPiece(String pieceNo) {
        ObjFeltFinishing.Filter(" AND PIECE_NO='" + pieceNo + "' ");
        SetMenuForRights();
        DisplayData();
    }

    // find all pending document
    public void FindWaiting() {
        ObjFeltFinishing.Filter(" AND DOC_NO IN (SELECT DISTINCT PI.DOC_NO FROM PRODUCTION.FELT_SPECIAL_RATE_SANCTION_ELIGIBILITY PI, PRODUCTION.FELT_PROD_DOC_DATA D WHERE PI.DOC_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=631 AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    private void MoveFirst() {
        ObjFeltFinishing.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjFeltFinishing.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjFeltFinishing.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjFeltFinishing.MoveLast();
        DisplayData();
    }

    private void ShowMessage(String pMessage) {
//        lblStatus.setText(" " + pMessage);
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
//        DataModel.ClearAllReadOnly();
//        Table.setEnabled(false);
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
        DataModelUpdateHistory.addColumn("From Ip");
    }

    private void GenerateList() {
        String FromDate = EITLERPGLOBAL.formatDateDB(txtBookingFrom.getText());
        String ToDate = EITLERPGLOBAL.formatDateDB(txtBookingTo.getText());
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGrid(); //clear existing content of table
            ResultSet rsTmp;

            if (!txtpartycode.getText().trim().equals("")) {
                cndtn = " AND PR_PARTY_CODE= '" + txtpartycode.getText().trim() + "' ";
            } else if (!txtgroupcode.getText().trim().equals("")) {
                grp_cndtn = " WHERE GROUP_CODE= '" + txtgroupcode.getText().trim() + "' ";
            } else if (!txtupnno.getText().trim().equals("")) {
                cndtn = " AND PR_UPN = '" + txtupnno.getText().trim() + "' ";
            } else if (!txtpieceno.getText().equals("")) {
                String[] Pieces = txtpieceno.getText().split(",");
                for (int i = 0; i < Pieces.length; i++) {
                    if (i == 0) {
                        cndtn = " AND (PR_PIECE_NO = '" + Pieces[i] + "' ";
                    } else {
                        cndtn += " OR PR_PIECE_NO = '" + Pieces[i] + "' ";
                    }
                }
                cndtn += ") ";
            }

            if (!txtBookingFrom.getText().trim().equals("") && !txtBookingTo.getText().trim().equals("")) {
                try {
                    cndtn += " AND PR_ORDER_DATE >= '" + FromDate + "' AND PR_ORDER_DATE <= '" + ToDate + "' ";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String strSQL = "";

            strSQL = "SELECT * FROM "
                    + "(SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + " WHERE PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5') "
                    + " AND PR_PIECE_STAGE NOT IN ('INVOICED','EXP-INVOICE','DIVERTED','DIVIDED') "
                    + " AND PR_RATE_INDICATOR = 'NEW' "
                    + " AND PR_PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SPECIAL_RATE_SANCTION_ELIGIBILITY WHERE SELECTED_FLAG=1 AND CANCELED=0 AND DESPATCH_DATE>CURDATE()) "
                    + cndtn + " "
                    + ") AS PR "
                    + "LEFT JOIN  "
                    + "(SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  "
                    + "WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0) AS PM  "
                    + "ON PR.PR_PARTY_CODE=PM.PARTY_CODE  "
                    + "LEFT JOIN  "
                    + "(SELECT H.GROUP_CODE,H.GROUP_DESC,D.PARTY_CODE AS GROUP_PARTY_CODE  "
                    + "FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D  "
                    + "WHERE H.GROUP_CODE=D.GROUP_CODE  "
                    + "AND H.APPROVED=1 AND H.CANCELED=0) AS GM  "
                    + "ON PR.PR_PARTY_CODE=GM.GROUP_PARTY_CODE "
                    + "LEFT JOIN  "
                    + "(SELECT DISTINCT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER  "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_NO DESC) AS QM  "
                    + "ON PR.PR_PRODUCT_CODE=QM.PRODUCT_CODE "
                    + "LEFT JOIN  "
                    + "(SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP  "
                    + "ON PR.PR_POSITION_NO=MP.POSITION_NO "
                    + "LEFT JOIN  "
                    + "(SELECT * FROM PRODUCTION.FELT_INCHARGE) AS IM  "
                    + "ON PR.PR_INCHARGE=IM.INCHARGE_CD "
                    + grp_cndtn + " ORDER BY PR_PIECE_NO ";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                int cnt = 0, p = 1;

                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];
                    p = 1;
                    rowData[p] = Integer.toString(cnt);
                    p++;
                    rowData[p] = rsTmp.getString("PR_PIECE_NO");
                    p++;
                    rowData[p] = rsTmp.getString("PR_PARTY_CODE");
                    p++;
                    rowData[p] = rsTmp.getString("PARTY_NAME");
                    p++;
                    rowData[p] = rsTmp.getString("PR_UPN");
                    p++;
                    rowData[p] = rsTmp.getString("GROUP_CODE");
                    p++;
                    rowData[p] = rsTmp.getString("GROUP_DESC");
                    p++;
                    rowData[p] = rsTmp.getString("PR_MACHINE_NO");
                    p++;
                    rowData[p] = rsTmp.getString("PR_POSITION_NO");
                    p++;
                    rowData[p] = rsTmp.getString("POSITION_DESC");
                    p++;
                    rowData[p] = rsTmp.getString("PR_PRODUCT_CODE");
                    p++;
                    rowData[p] = rsTmp.getString("PR_GROUP");

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void Notification() {
        if (OpgFinal.isSelected()) {
            try {

                int userId = EITLERPGLOBAL.gNewUserID;
                int SrNo = 1;

                String docNo = txtDocNo.getText();
                String docDate = txtDocDate.getText();

                String pSubject = "Notification : Special Sanction For Applying Old Rate Doc No : " + docNo;
                String pMessage = "";
                String recievers = "";
                String cc = "";

                pMessage = "<br>Special Sanction For Applying Old Rate Doc No : " + docNo + " Dated : " + docDate + " has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, userId) + ".<br>";

                if (rbtnPartyCode.isSelected()) {
                    pMessage = pMessage + "Party Code : " + txtpartycode.getText() + ".<br>";
                    pMessage = pMessage + "Party Name : " + txtpartyname.getText() + ".<br>";
                    pMessage = pMessage + "Booking From : " + txtBookingFrom.getText() + ".<br>";
                    pMessage = pMessage + "Booking To : " + txtBookingTo.getText() + ".<br>";
                }
                
                if (rbtnGroupCode.isSelected()) {
                    pMessage = pMessage + "Group Code : " + txtgroupcode.getText() + ".<br>";
                    pMessage = pMessage + "Group Name : " + txtgroupname.getText() + ".<br>";
                    pMessage = pMessage + "Booking From : " + txtBookingFrom.getText() + ".<br>";
                    pMessage = pMessage + "Booking To : " + txtBookingTo.getText() + ".<br>";
                }
                
                if (rbtnUPNNo.isSelected()) {
                    pMessage = pMessage + "UPN : " + txtupnno.getText() + ".<br>";
                    pMessage = pMessage + "Booking From : " + txtBookingFrom.getText() + ".<br>";
                    pMessage = pMessage + "Booking To : " + txtBookingTo.getText() + ".<br>";
                }
                
                if (rbtnPieceNo.isSelected()) {
                    pMessage = pMessage + "Piece No : " + txtpartycode.getText() + ".<br>";
                }

                pMessage = pMessage + "Despatch Date : " + txtDespatchDate.getText() + ".<br>";
                
                pMessage = pMessage + "<br>Selected Piece detail(s) as given below : ";

                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String strSQL = "SELECT * FROM PRODUCTION.FELT_SPECIAL_RATE_SANCTION_ELIGIBILITY WHERE DOC_NO = '" + docNo + "' AND SELECTED_FLAG=1 ";
                rsData = stmt.executeQuery(strSQL);
                rsData.first();

                System.out.println("String StrSQL : " + strSQL);

                if (rsData.getRow() > 0) {
                    pMessage = pMessage + "<table border='1' cellpadding='10'>"
                            + "<tr>"
                            + "<th align='center'> SrNo </th>"
                            + "<th align='center'> Piece No </th>"
                            + "<th align='center'> Party Code </th>"
                            + "<th align='center'> Party Name </th>"
                            + "<th align='center'> UPN </th>"
                            + "<th align='center'> Group Code </th>"
                            + "<th align='center'> Group Name </th>"
                            + "<th align='center'> Machine No </th>"
                            + "<th align='center'> Position No </th>"
                            + "<th align='center'> Position Description </th>"
                            + "<th align='center'> Product Code </th>"
                            + "<th align='center'> Product Group </th>"
                            + "</tr>";

                    rsData.first();
                    if (rsData.getRow() > 0) {
                        while (!rsData.isAfterLast()) {

                            pMessage = pMessage + ""
                                    + "<tr>"
                                    + "<td align='right'> " + (SrNo++) + " </td>"
                                    + "<td align='left'> " + rsData.getString("PIECE_NO") + " </td>"
                                    + "<td align='left'> " + rsData.getString("PARTY_CODE") + " </td>"
                                    + "<td align='left'> " + rsData.getString("PARTY_NAME") + " </td>"
                                    + "<td align='center'> " + rsData.getString("UPN") + " </td>"
                                    + "<td align='left'> " + rsData.getString("GROUP_CODE") + " </td>"
                                    + "<td align='left'> " + rsData.getString("GROUP_NAME") + " </td>"
                                    + "<td align='left'> " + rsData.getString("MACHINE_NO") + " </td>"
                                    + "<td align='left'> " + rsData.getString("POSITION_NO") + " </td>"
                                    + "<td align='left'> " + rsData.getString("POSITION_DESC") + " </td>"
                                    + "<td align='left'> " + rsData.getString("PRODUCT_CODE") + " </td>"
                                    + "<td align='left'> " + rsData.getString("PRODUCT_GROUP") + " </td>"
                                    + "</tr>";

                            rsData.next();
                        }
                    }
                    pMessage = pMessage + "</table>";
                } else {
                    pMessage = pMessage + "<br>No Detail Found.<br>";
                }

                pMessage += "<br>";

//                recievers = "aditya@dineshmills.com,soumen@dineshmills.com,vdshanbhag@dineshmills.com,felts@dineshmills.com";
                recievers = "felts@dineshmills.com";
                cc = "sdmlerp@dineshmills.com";

                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";

//                System.out.println("Recivers : " + recievers);
//                System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

                JavaMail.SendMail(recievers, pMessage, pSubject, cc);
                System.out.println("Mail Sent.");

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
        }
    }

}
