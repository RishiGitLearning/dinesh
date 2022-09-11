/*
 * frmFeltPieceUpd.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.FeltSales.PieceAmendmentApproval_STOCK;

/**
 * @author DAXESH PRAJAPATI
 */
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.util.HashMap;
import EITLERP.*;
import EITLERP.data;
import EITLERP.EITLComboModel;
import EITLERP.EITLTableModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.BigEdit;
import EITLERP.clsUser;
import EITLERP.clsHierarchy;
import EITLERP.clsAuthority;
import EITLERP.ComboData;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.MailNotification;
//import EITLERP.FeltSales.common.Order_No_Conversion;
import EITLERP.frmPendingApprovals;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;

public class frmPieceAmendApproval_STOCK extends javax.swing.JApplet {

    private int EditMode = 0;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromUserId = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    public int DeptID = EITLERPGLOBAL.gUserDeptID;
    public String finalapproved = "";
    public int ModuleId = 763;
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    private boolean DoNotEvaluate = false;
    private String DOC_NO = "";
    private EITLComboModel cmbToModel;
    private EITLTableModel DataModel_Revision;
    //private clsFeltPieceUpd objPieceAmendApproval;
    private clsPieceAmendApproval_STOCK objPieceAmendApproval;
    private int lnFromID = 0;
    HashMap hmPieceList = new HashMap();

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbSendToModel;
    private EITLComboModel cmbAmendReasonModel;

    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    public frmPendingApprovals frmPA;
    public boolean from_machine_master = false;

    /**
     * Creates new form frmFeltPieceUpd
     */
    public void init() {
        System.gc();
        setSize(830, 590);
        initComponents();

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
        //objPieceAmendApproval=new clsFeltPieceUpd();
        objPieceAmendApproval = new clsPieceAmendApproval_STOCK();
        GenerateAmendReasonCombo();
        //    DeptID = 40 ;
        SetMenuForRights();
        GenerateCombos();
        FormatGrid();
        GenerateFromCombo();
        GenerateHierarchyCombo();
        SetFields(false);
        objPieceAmendApproval = new clsPieceAmendApproval_STOCK();
        boolean load = objPieceAmendApproval.LoadData();

        if (load) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + objPieceAmendApproval.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemWarping = new javax.swing.JMenuItem();
        jMenuItemOrder = new javax.swing.JMenuItem();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        PARTY_CODE = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        MACHINE_NO = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        POSITION = new javax.swing.JTextField();
        SHOW_LIST = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        PIECE_AMEND_STOCK_NO = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        PIECE_AMEND_DATE = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        lblPartyName = new javax.swing.JLabel();
        lblPositionDesc = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblPositionDesignNo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblUPN = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtEntryReason = new javax.swing.JTextField();
        lblRefDocNo = new javax.swing.JLabel();
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
        jButton1 = new javax.swing.JButton();
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
        panel1 = new java.awt.Panel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblRevision = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();

        jMenuItemWarping.setText("Warping Report");
        jMenuItemWarping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemWarpingActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemWarping);

        jMenuItemOrder.setText("Order Detail");
        jMenuItemOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOrderActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemOrder);

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
        lblTitle.setText("Position Stock Piece Review ");
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
            public void focusLost(java.awt.event.FocusEvent evt) {
                TableFocusLost(evt);
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
        jScrollPane1.setBounds(10, 170, 800, 250);

        jLabel1.setText("Party Code ");
        Tab1.add(jLabel1);
        jLabel1.setBounds(20, 50, 80, 30);
        Tab1.add(PARTY_CODE);
        PARTY_CODE.setBounds(110, 50, 120, 19);

        jLabel2.setText("Machine No");
        Tab1.add(jLabel2);
        jLabel2.setBounds(240, 50, 90, 30);
        Tab1.add(MACHINE_NO);
        MACHINE_NO.setBounds(320, 50, 60, 19);

        jLabel3.setText("POSITION");
        Tab1.add(jLabel3);
        jLabel3.setBounds(390, 50, 80, 30);
        Tab1.add(POSITION);
        POSITION.setBounds(460, 50, 50, 19);

        SHOW_LIST.setText("SHOW LIST");
        SHOW_LIST.setEnabled(false);
        SHOW_LIST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SHOW_LISTActionPerformed(evt);
            }
        });
        Tab1.add(SHOW_LIST);
        SHOW_LIST.setBounds(670, 50, 110, 25);

        jLabel4.setText("Doc No");
        Tab1.add(jLabel4);
        jLabel4.setBounds(20, 7, 70, 30);

        PIECE_AMEND_STOCK_NO.setEditable(false);
        Tab1.add(PIECE_AMEND_STOCK_NO);
        PIECE_AMEND_STOCK_NO.setBounds(80, 10, 100, 19);

        jLabel5.setText("Date");
        Tab1.add(jLabel5);
        jLabel5.setBounds(240, 10, 34, 30);

        PIECE_AMEND_DATE.setEditable(false);
        Tab1.add(PIECE_AMEND_DATE);
        PIECE_AMEND_DATE.setBounds(290, 10, 130, 19);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(184, 7, 50, 30);
        Tab1.add(lblPartyName);
        lblPartyName.setBounds(110, 80, 300, 20);
        Tab1.add(lblPositionDesc);
        lblPositionDesc.setBounds(460, 80, 180, 20);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel3);
        jPanel3.setBounds(0, 130, 820, 5);

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel4);
        jPanel4.setBounds(0, 160, 820, 5);

        jLabel6.setText(":: Important Note ::");
        Tab1.add(jLabel6);
        jLabel6.setBounds(10, 140, 160, 15);

        jLabel17.setText("* Select Piece to Obsolete.");
        Tab1.add(jLabel17);
        jLabel17.setBounds(170, 140, 190, 15);

        jLabel18.setText("* Unselected Pieces will no effect.");
        Tab1.add(jLabel18);
        jLabel18.setBounds(370, 140, 270, 15);

        jLabel7.setText("Position Design No ");
        Tab1.add(jLabel7);
        jLabel7.setBounds(10, 110, 130, 20);

        lblPositionDesignNo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Tab1.add(lblPositionDesignNo);
        lblPositionDesignNo.setBounds(140, 110, 60, 20);

        jLabel8.setText("UPN");
        Tab1.add(jLabel8);
        jLabel8.setBounds(210, 110, 40, 20);

        lblUPN.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Tab1.add(lblUPN);
        lblUPN.setBounds(250, 110, 120, 20);

        jLabel12.setText("ENTRY REASON");
        Tab1.add(jLabel12);
        jLabel12.setBounds(380, 110, 110, 15);

        txtEntryReason.setEnabled(false);
        Tab1.add(txtEntryReason);
        txtEntryReason.setBounds(490, 107, 200, 20);
        Tab1.add(lblRefDocNo);
        lblRefDocNo.setBounds(690, 105, 100, 20);

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
        jLabel31.setBounds(7, 23, 62, 15);

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
        cmbHierarchy.setBounds(86, 20, 184, 24);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(7, 62, 33, 15);

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
        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        OpgHold.setEnabled(false);
        OpgHold.setNextFocusableComponent(cmbSendTo);
        OpgHold.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgHoldMouseClicked(evt);
            }
        });
        OpgHold.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgHoldFocusGained(evt);
            }
        });
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 130, 20);

        Tab2.add(jPanel6);
        jPanel6.setBounds(86, 130, 184, 100);

        jLabel33.setText("Send To");
        Tab2.add(jLabel33);
        jLabel33.setBounds(7, 249, 50, 15);

        cmbSendTo.setEnabled(false);
        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(86, 245, 184, 24);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(7, 288, 60, 15);

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

        jButton1.setText("Resend Final Approval Mail ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab2.add(jButton1);
        jButton1.setBounds(590, 10, 210, 25);

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

        panel1.setBackground(new java.awt.Color(189, 189, 189));
        panel1.setLayout(null);

        tblRevision.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tblRevision);

        panel1.add(jScrollPane4);
        jScrollPane4.setBounds(2, 2, 810, 430);

        Tab.addTab("Expected Dispatch Month Revision", panel1);

        getContentPane().add(Tab);
        Tab.setBounds(0, 70, 830, 470);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 540, 830, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOrderActionPerformed

    }//GEN-LAST:event_jMenuItemOrderActionPerformed

    private void jMenuItemWarpingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemWarpingActionPerformed

    }//GEN-LAST:event_jMenuItemWarpingActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        //   new TReportWriter.TReportEngine().PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptFeltWeaving.rpt",new HashMap(),objPieceAmendApproval.getReportData(EITLERPGLOBAL.formatDateDB(txtFeltProductionDate.getText().trim())));
        //  EITLERPGLOBAL.PAGE_BREAK=true;
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed

    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        //objPieceAmendApproval.Close();
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
        if (JOptionPane.showConfirmDialog(frmPieceAmendApproval_STOCK.this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Delete();
        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed

        try {
            for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                if (!DataModel.getValueAt(i, 1).toString().equals("")) {
                    String bale_reopen_pending = data.getStringValueFromDB("SELECT H.BALE_NO FROM PRODUCTION.FELT_REOPEN_BALE_HEADER H,PRODUCTION.FELT_REOPEN_BALE_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=0 AND H.CANCELED=0 AND D.PIECE_NO='" + DataModel.getValueAt(i, 1).toString() + "'");
                    System.out.println("SELECT H.BALE_NO FROM PRODUCTION.FELT_REOPEN_BALE_HEADER H,PRODUCTION.FELT_REOPEN_BALE_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=0 AND H.CANCELED=0 AND D.PIECE_NO='" + DataModel.getValueAt(i, 1).toString() + "'");
                    if (!bale_reopen_pending.equals("")) {
                        JOptionPane.showMessageDialog(this, "Bale Reopen Pending for PIECE No = " + DataModel.getValueAt(i, 1).toString() + ", Bale No : " + bale_reopen_pending);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        if (TableUpdateHistory.getRowCount() > 0 && TableUpdateHistory.getSelectedRow() >= 0) {
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText.setText(TableUpdateHistory.getValueAt(TableUpdateHistory.getSelectedRow(), 4).toString());
            bigEdit.ShowEdit();
        } else {
            JOptionPane.showMessageDialog(frmPieceAmendApproval_STOCK.this, "Select a row from Document Update History");
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdViewRevisionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewRevisionsActionPerformed
        objPieceAmendApproval.ShowHistory(PIECE_AMEND_STOCK_NO.getText());
        objPieceAmendApproval.HistoryView = true;
        MoveLast();
    }//GEN-LAST:event_cmdViewRevisionsActionPerformed

    private void cmdBackToNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToNormalActionPerformed
        objPieceAmendApproval.HistoryView = false;
        objPieceAmendApproval.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdBackToNormalActionPerformed

    private void cmdBackToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab1ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBackToTab1ActionPerformed

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
        ShowMessage("Enter the remarks for next user");
    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        ShowMessage("Select the user to whom document to be forwarded");
    }//GEN-LAST:event_cmbSendToFocusGained

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        finalapproved = "NO";
        OpgHold.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgReject.setSelected(false);
        cmbSendTo.setEnabled(false);
    }//GEN-LAST:event_OpgHoldMouseClicked

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgHoldFocusGained

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);
        finalapproved = "NO";
        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained
        ShowMessage("Select the approval action");
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

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //JOptionPane.showMessageDialog(null, "SelHierarchyId : "+SelHierarchyID);
        DOC_NO = PIECE_AMEND_STOCK_NO.getText();
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
        txtToRemarks.setEnabled(false);
        if (!OpgApprove.isEnabled()) {
            OpgHold.setSelected(true);
        }

//        if (!OpgApprove.isEnabled()) {
//            return;
//        }
//        DOC_NO = Order_No_Conversion.PieceAmnd_No_Only(PIECE_AMEND_STOCK_NO.getText());
//        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
//        //JOptionPane.showMessageDialog(null, "SelHierarchyId : "+SelHierarchyID);
//
//        cmbSendTo.setEnabled(true);
//        if (EditMode == EITLERPGLOBAL.EDIT) {
//            GenerateRejectedSendToCombo();
//            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(ModuleId, DOC_NO + "")) {
//                cmbSendTo.setEnabled(true);
//                txtToRemarks.setEnabled(true);
//                txtFromRemarks.setEnabled(true);
//            } else {
//                cmbSendTo.setEnabled(false);
//            }
//        }
//        if (cmbSendTo.getItemCount() <= 0) {
//            GenerateSendToCombo();
//        }
//
//        OpgFinal.setSelected(false);
//        OpgReject.setSelected(false);
//        OpgApprove.setSelected(true);
//        OpgHold.setSelected(false);
//        txtToRemarks.setEnabled(false);
//        if (!OpgApprove.isEnabled()) {
//            OpgHold.setSelected(true);
//        }
    }//GEN-LAST:event_OpgApproveMouseClicked

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        ShowMessage("Select the hierarchy for approval");
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
            OpgFinal.setEnabled(true);
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

    private void Tab1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab1FocusGained

    }//GEN-LAST:event_Tab1FocusGained

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // ReasonResetReadonly() ;       // TODO add your handling code here:
        if (txtEntryReason.getText().equals("AUTO_EXPECTED_MONTH")) {
            if (Table.getSelectedColumn() == 2) {
                Table.setValueAt(false, Table.getSelectedRow(), 2);
                JOptionPane.showMessageDialog(null, "This Document cannt review. Only Expected Month can select");
                return;
            }
            if (Table.getSelectedColumn() == 4) {
                Table.setValueAt(false, Table.getSelectedRow(), 4);
                JOptionPane.showMessageDialog(null, "This Document cannt review. Only Expected Month can select");
                return;
            }
            if (Table.getSelectedColumn() == 5) {
                Table.setValueAt(false, Table.getSelectedRow(), 5);
                JOptionPane.showMessageDialog(null, "This Document cannt review. Only Expected Month can select");
                return;
            }
        }
    }//GEN-LAST:event_TableMouseClicked

    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased

    }//GEN-LAST:event_TableKeyReleased

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        // TODO add your handling code here:
        //    ReasonResetReadonly();
//        
//        if (evt.getKeyCode() == 112 || evt.getKeyCode() == 10) {
//
//            if (Table.getSelectedColumn() == 3 || Table.getSelectedColumn() == 4) {
//                String minqlt = "";
//                char mchr = ' ';
//                mchr = evt.getKeyChar();
//                if (mchr == '\b' || mchr == '\n') {
//                    minqlt = Table.getValueAt(Table.getRowCount() - 1, 3).toString();
//                } else {
//                    minqlt = Table.getValueAt(Table.getRowCount() - 1, 3).toString();
//                }
//                searchkey search = new searchkey();
//                search.SQL = "SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL where MM_MACHINE_NO!='' GROUP BY  MM_MACHINE_NO,MM_MACHINE_POSITION";
//                search.ReturnCol = 1;
//                search.ShowReturnCol = true;
//                search.DefaultSearchOn = 1;
//                search.setsearchText(minqlt);
//                if (search.ShowRSLOV()) {
//                    if (Table.getCellEditor() != null) {
//                        Table.getCellEditor().stopCellEditing();
//                    }
//                    Table.setValueAt(search.ReturnVal, Table.getSelectedRow(), 3);
//                    Table.setValueAt(search.SecondVal, Table.getSelectedRow(), 4);
//                }
//            }
//        }
    }//GEN-LAST:event_TableKeyPressed

    private void TableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusGained
        //   ReasonReadOnly();
        ReasonResetReadonly();
        if (Table.getSelectedColumn() == 3 || Table.getSelectedColumn() == 4) {
            lblStatus.setText("Press F1 for Machine No and Position");
        }
    }//GEN-LAST:event_TableFocusGained

    private void TableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusLost
        // TODO add your handling code here:
        lblStatus.setText("");
    }//GEN-LAST:event_TableFocusLost

    private void SHOW_LISTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SHOW_LISTActionPerformed
        // TODO add your handling code here:

        String Party_Code = PARTY_CODE.getText();
        String MachineNo = MACHINE_NO.getText();
        String Position = POSITION.getText();

        clsPieceAmendApproval_STOCK piece = new clsPieceAmendApproval_STOCK();

        hmPieceList = piece.getPieceList_STOCK(Party_Code, MachineNo, Position);

        setData_toTable();

    }//GEN-LAST:event_SHOW_LISTActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (objPieceAmendApproval.getAttribute("APPROVED").getInt() == 1) {
            int value = JOptionPane.showConfirmDialog(this, " Are you sure? You want to send Final Approved mail to all users? ", "Confirmation Alert!", JOptionPane.YES_NO_OPTION);
            System.out.println("VALUE = " + value);
            if (value == 0) {
                try {
                    String DOC_NO = PIECE_AMEND_STOCK_NO.getText();
                    String DOC_DATE = PIECE_AMEND_DATE.getText();
                    String Party_Code = PARTY_CODE.getText();

                    System.out.println("ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true " + ModuleId + "," + DOC_NO + "," + DOC_DATE + "," + Party_Code + "," + EITLERPGLOBAL.gNewUserID + "," + objPieceAmendApproval.getAttribute("HIERARCHY_ID").getInt() + "," + true);

                    String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, objPieceAmendApproval.getAttribute("APPROVED_BY").getInt(), objPieceAmendApproval.getAttribute("HIERARCHY_ID").getInt(), true, EITLERPGLOBAL.gNewUserID);
                    System.out.println("Send Mail Responce : " + responce);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed
    public void setData_toTable() {

        int size = DataModel.getRowCount();
        for (int j = size - 1; j >= 0; j--) {
            DataModel.removeRow(j);
        }
        if (hmPieceList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Updated Record Found in Machine Master on Existing Production Pieces");

        } else {
            int i = 0;
            for (int j = 1; j <= hmPieceList.size(); j++) {
                i++;
                Object[] rowData = new Object[72];
                clsPieceAmendApproval_STOCK piece = (clsPieceAmendApproval_STOCK) hmPieceList.get(j);

                rowData[0] = false;
                rowData[1] = (String) piece.getAttribute("PIECE_NO").getObj();
                rowData[2] = "00/00/0000";
                rowData[3] = (String) piece.getAttribute("MM_MACHINE_NO").getObj();
                rowData[4] = (String) piece.getAttribute("MM_MACHINE_POSITION").getObj();

                rowData[5] = "";
                try {

                    ResultSet rsTmp;
                    String strSQL;
                    strSQL = "SELECT MM_MACHINE_POSITION_DESC from PRODUCTION.FELT_MACHINE_MASTER_DETAIL where MM_MACHINE_NO='" + (String) piece.getAttribute("MM_MACHINE_NO").getObj() + "' and MM_MACHINE_POSITION = '" + (String) piece.getAttribute("MM_MACHINE_POSITION").getObj() + "'";
                    rsTmp = data.getResult(strSQL);
                    rsTmp.first();

                    rowData[5] = rsTmp.getString("MM_MACHINE_POSITION_DESC");
                } catch (Exception ew) {
                    System.out.println("Error on getting data");
                }

                rowData[6] = piece.getAttribute("MM_PARTY_CODE").getObj();

                rowData[7] = "";
                try {

                    ResultSet rsTmp;
                    String strSQL;
                    strSQL = "SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE = " + (String) piece.getAttribute("MM_PARTY_CODE").getObj() + "";
                    rsTmp = data.getResult(strSQL);
                    rsTmp.first();

                    rowData[7] = rsTmp.getString("PARTY_NAME");
                } catch (Exception ew) {
                    System.out.println("Error on getting data");
                }
                rowData[8] = "PRODUCT_CODE";
                rowData[9] = "PRODUCT_DESC";

                rowData[10] = (String) piece.getAttribute("FLET_GROUP").getObj();
                rowData[11] = (String) piece.getAttribute("FLET_GROUP_UPDATED").getObj();
                rowData[12] = (String) piece.getAttribute("STYLE").getObj();
                rowData[13] = (String) piece.getAttribute("STYLE_UPDATED").getObj();
                rowData[14] = (String) piece.getAttribute("LENGTH").getObj();
                rowData[15] = (String) piece.getAttribute("LENGTH_UPDATED").getObj();
                rowData[16] = (String) piece.getAttribute("WIDTH").getObj();
                rowData[17] = (String) piece.getAttribute("WIDTH_UPDATED").getObj();
                rowData[18] = (String) piece.getAttribute("GSM").getObj();
                rowData[19] = (String) piece.getAttribute("GSM_UPDATED").getObj();
                rowData[20] = (String) piece.getAttribute("PIECE_STAGE").getObj();

                DataModel.addRow(rowData);
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField MACHINE_NO;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTextField PARTY_CODE;
    private javax.swing.JTextField PIECE_AMEND_DATE;
    private javax.swing.JTextField PIECE_AMEND_STOCK_NO;
    private javax.swing.JTextField POSITION;
    private javax.swing.JButton SHOW_LIST;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable Table;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
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
    private javax.swing.JButton cmdNextToTab2;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewRevisions;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JMenuItem jMenuItemOrder;
    private javax.swing.JMenuItem jMenuItemWarping;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JLabel lblPositionDesc;
    private javax.swing.JLabel lblPositionDesignNo;
    private javax.swing.JLabel lblRefDocNo;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUPN;
    private java.awt.Panel panel1;
    private javax.swing.JTable tblRevision;
    private javax.swing.JTextField txtEntryReason;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
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
    //    txtAmendDate.setEnabled(pStat);
        //     txtFormNo.setEnabled(pStat);

        PARTY_CODE.setEnabled(pStat);
        MACHINE_NO.setEnabled(pStat);
        POSITION.setEnabled(pStat);

        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        Table.setEnabled(pStat);
        //cmbAmendReason.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        SHOW_LIST.setEnabled(false);
//        SetupApproval();
    }

    private void ClearFields() {

        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        PARTY_CODE.setText("");
        MACHINE_NO.setText("");
        POSITION.setText("");
        lblRefDocNo.setText("");
        FormatGrid();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
    }

    //Display data on the Screen
    private void DisplayData() {
        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleId)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        jButton1.setEnabled(false);
        //=========== Color Indication ===============//
        try {
            if (objPieceAmendApproval.getAttribute("APPROVED").getInt() == 1) {
                lblTitle.setBackground(Color.BLUE);
                lblTitle.setForeground(Color.WHITE);
                jButton1.setEnabled(true);
            }

            if (objPieceAmendApproval.getAttribute("APPROVED").getInt() == 0) {
                lblTitle.setBackground(Color.GRAY);
                lblTitle.setForeground(Color.BLACK);
            }

            if (objPieceAmendApproval.getAttribute("CANCELED").getInt() == 1) {
                lblTitle.setBackground(Color.RED);
                lblTitle.setForeground(Color.BLACK);
            }
        } catch (Exception c) {
            c.printStackTrace();
        }
        ClearFields();
        try {
            PIECE_AMEND_STOCK_NO.setText(objPieceAmendApproval.getAttribute("PIECE_AMEND_STOCK_NO").getString());
            lblTitle.setText(" Position Stock Piece Review -  " + PIECE_AMEND_STOCK_NO.getText());
            PIECE_AMEND_DATE.setText(EITLERPGLOBAL.formatDate(objPieceAmendApproval.getAttribute("PIECE_AMEND_DATE").getString()));
            PARTY_CODE.setText(objPieceAmendApproval.getAttribute("MM_PARTY_CODE").getString());
            lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, objPieceAmendApproval.getAttribute("MM_PARTY_CODE").getString()));
            MACHINE_NO.setText(objPieceAmendApproval.getAttribute("MM_MACHINE_NO").getString());
            POSITION.setText(objPieceAmendApproval.getAttribute("MM_MACHINE_POSITION").getString());
            lblPositionDesc.setText(data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + objPieceAmendApproval.getAttribute("MM_MACHINE_POSITION").getString() + "'"));

            txtEntryReason.setText(objPieceAmendApproval.getAttribute("ENTRY_REASON").getString());
            lblRefDocNo.setText(objPieceAmendApproval.getAttribute("ENTRY_DOCNO").getString());

            String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + objPieceAmendApproval.getAttribute("MM_MACHINE_POSITION").getString());
            lblPositionDesignNo.setText("" + Position_Des_No);
            lblUPN.setText(objPieceAmendApproval.getAttribute("MM_PARTY_CODE").getString() + objPieceAmendApproval.getAttribute("MM_MACHINE_NO").getString() + Position_Des_No);
            lblRevNo.setText(Integer.toString((int) objPieceAmendApproval.getAttribute("REVISION_NO").getVal()));

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, objPieceAmendApproval.getAttribute("HIERARCHY_ID").getInt());
            SelHierarchyID = objPieceAmendApproval.getAttribute("HIERARCHY_ID").getInt();
            FormatGrid();
            int srNo = 0;
            //Now Generate Table
            //DoNotEvaluate=false;
            //UpdateTotals();
            Renderer.removeBackColors();
            for (int i = 1; i <= objPieceAmendApproval.hmPieceAmendApprovalDetail.size(); i++) {
                clsPieceAmendApprovalDetail_STOCK ObjItem = (clsPieceAmendApprovalDetail_STOCK) objPieceAmendApproval.hmPieceAmendApprovalDetail.get(Integer.toString(i));

                Object[] rowData = new Object[1];
                DataModel.addRow(rowData);
                int NewRow = Table.getRowCount() - 1;
                DataModel.setValueByVariable("SrNo", true, NewRow);

                DataModel.setValueByVariable("PIECE_NO", ObjItem.getAttribute("PIECE_NO").getString(), NewRow);

                if (ObjItem.getAttribute("SELECTED").getInt() == 1) {
                    DataModel.setValueByVariable("SELECTED", true, NewRow);
                } else {
                    DataModel.setValueByVariable("SELECTED", false, NewRow);
                }

                DataModel.setValueByVariable("GROUP", ObjItem.getAttribute("FLET_GROUP").getString(), NewRow);
                DataModel.setValueByVariable("GROUP_UPDATED", ObjItem.getAttribute("FLET_GROUP_UPDATED").getString(), NewRow);
                DataModel.setValueByVariable("STYLE", ObjItem.getAttribute("STYLE").getString(), NewRow);
                DataModel.setValueByVariable("STYLE_UPDATED", ObjItem.getAttribute("STYLE_UPDATED").getString(), NewRow);
                DataModel.setValueByVariable("LENGTH", ObjItem.getAttribute("LENGTH").getString(), NewRow);
                DataModel.setValueByVariable("LENGTH_UPDATED", ObjItem.getAttribute("LENGTH_UPDATED").getString(), NewRow);
                DataModel.setValueByVariable("WIDTH", ObjItem.getAttribute("WIDTH").getString(), NewRow);
                DataModel.setValueByVariable("WIDTH_UPDATED", ObjItem.getAttribute("WIDTH_UPDATED").getString(), NewRow);
                DataModel.setValueByVariable("GSM", ObjItem.getAttribute("GSM").getString(), NewRow); //18
                DataModel.setValueByVariable("GSM_UPDATED", ObjItem.getAttribute("GSM_UPDATED").getString(), NewRow); //19
                DataModel.setValueByVariable("PIECE_STAGE", ObjItem.getAttribute("PIECE_STAGE").getString(), NewRow); //19
                DataModel.setValueByVariable("EXPECTED_DISPATCH", ObjItem.getAttribute("EXPECTED_DISPATCH").getString(), NewRow); //19

                
                DataModel.setValueByVariable("OBSOLETE_UPN_ASSIGN_STATUS", ObjItem.getAttribute("OBSOLETE_UPN_ASSIGN_STATUS").getString(), NewRow);
                DataModel.setValueByVariable("BASE_GSM", ObjItem.getAttribute("BASE_GSM").getString(), NewRow);
                DataModel.setValueByVariable("WEB_GSM", ObjItem.getAttribute("WEB_GSM").getString(), NewRow);
                DataModel.setValueByVariable("WEAVE", ObjItem.getAttribute("WEAVE").getString(), NewRow);
                DataModel.setValueByVariable("CFM_TARGETTED", ObjItem.getAttribute("CFM_TARGETTED").getString(), NewRow);
                DataModel.setValueByVariable("LOOM_NO", ObjItem.getAttribute("LOOM_NO").getString(), NewRow);
                DataModel.setValueByVariable("PAPER_PROD_TYPE", ObjItem.getAttribute("PAPER_PROD_TYPE").getString(), NewRow);
                DataModel.setValueByVariable("UNMAPPED_REASON", ObjItem.getAttribute("UNMAPPED_REASON").getString(), NewRow);
                DataModel.setValueByVariable("SCRAP_REASON", ObjItem.getAttribute("SCRAP_REASON").getString(), NewRow);
                
                if (!ObjItem.getAttribute("STYLE").getString().equals(ObjItem.getAttribute("STYLE_UPDATED").getString())) {
                    //DataModel.setValueByVariable("STYLE_UPDATED","",NewRow);
                    Renderer.setBackColor(NewRow, DataModel.getColFromVariable("STYLE_UPDATED"), Color.LIGHT_GRAY);
                }

                float length = Float.parseFloat(ObjItem.getAttribute("LENGTH").getString());

                float width = Float.parseFloat(ObjItem.getAttribute("WIDTH").getString());

                float gsm = Float.parseFloat(ObjItem.getAttribute("GSM").getString());

                float length_u = Float.parseFloat(ObjItem.getAttribute("LENGTH_UPDATED").getString());

                float width_u = Float.parseFloat(ObjItem.getAttribute("WIDTH_UPDATED").getString());

                float gsm_u = Float.parseFloat(ObjItem.getAttribute("GSM_UPDATED").getString());

                if (length != length_u) {
                    //DataModel.setValueByVariable("STYLE_UPDATED","",NewRow);
                    Renderer.setBackColor(NewRow, DataModel.getColFromVariable("LENGTH_UPDATED"), Color.LIGHT_GRAY);
                }

                if (width != width_u) {
                    //DataModel.setValueByVariable("STYLE_UPDATED","",NewRow);
                    Renderer.setBackColor(NewRow, DataModel.getColFromVariable("WIDTH_UPDATED"), Color.LIGHT_GRAY);
                }

                if (gsm != gsm_u) {
                    //DataModel.setValueByVariable("STYLE_UPDATED","",NewRow);
                    Renderer.setBackColor(NewRow, DataModel.getColFromVariable("GSM_UPDATED"), Color.LIGHT_GRAY);
                }

                ResultSet rs_history = data.getResult("SELECT distinct PIECE_NO,EXPECTED_DISPATCH FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_DETAIL_H where PIECE_NO='" + ObjItem.getAttribute("PIECE_NO").getString() + "' AND EXPECTED_DISPATCH!='" + ObjItem.getAttribute("PIECE_NO").getString() + "'");
                //System.out.println("SELECT distinct PIECE_NO,EXPECTED_DISPATCH FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_DETAIL_H where PIECE_NO='" + ObjItem.getAttribute("PIECE_NO").getString() + "' AND EXPECTED_DISPATCH!=''");
                try {

                    while (rs_history.next()) {
                        Object[] rowData1 = new Object[1];
                        DataModel_Revision.addRow(rowData1);

                        String PIECE_AMEND_NO = data.getStringValueFromDB("SELECT PIECE_AMEND_STOCK_NO FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_DETAIL_H WHERE PIECE_NO='" + rs_history.getString("PIECE_NO") + "' AND EXPECTED_DISPATCH='" + rs_history.getString("EXPECTED_DISPATCH") + "'");
                        String PIECE_AMEND_DATE = data.getStringValueFromDB("SELECT PIECE_AMEND_DATE FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER_H WHERE PIECE_AMEND_STOCK_NO='" + PIECE_AMEND_NO + "'");
                        System.out.println("SELECT PIECE_AMEND_STOCK_NO FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_DETAIL_H WHERE PIECE_NO='" + rs_history.getString("PIECE_NO") + "' AND EXPECTED_DISPATCH='" + rs_history.getString("EXPECTED_DISPATCH") + "'");
                        System.out.println("SELECT PIECE_AMEND_DATE FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER_H WHERE PIECE_AMEND_STOCK_NO='" + PIECE_AMEND_NO + "'");
                       DataModel_Revision.setValueByVariable("SRNO", (srNo + 1) + "", (srNo));
                        DataModel_Revision.setValueByVariable("PIECE_NO", rs_history.getString("PIECE_NO"), (srNo));
                        DataModel_Revision.setValueByVariable("REVIEW_NO", PIECE_AMEND_NO, (srNo));
                        DataModel_Revision.setValueByVariable("REVIEW_DATE", EITLERPGLOBAL.formatDate(PIECE_AMEND_DATE), (srNo));
                        DataModel_Revision.setValueByVariable("REVISION_MONTH", rs_history.getString("EXPECTED_DISPATCH"), (srNo));
                        srNo++;
                        /*
                         DataModel_Revision.SetVariable(0, "SRNO");
                         DataModel_Revision.SetVariable(1, "PIECE_NO");
                         DataModel_Revision.SetVariable(2, "REVIEW_NO");
                         DataModel_Revision.SetVariable(3, "REVIEW_DATE");
                         DataModel_Revision.SetVariable(4, "REVISION_MONTH");
                         */
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap List = new HashMap();
            String DocNo = objPieceAmendApproval.getAttribute("PIECE_AMEND_STOCK_NO").getString() + "";
            List = clsFeltProductionApprovalFlow.getDocumentFlow(ModuleId, DocNo);
            for (int i = 1; i <= List.size(); i++) {
                clsDocFlow ObjFlow = (clsDocFlow) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];
                //JOptionPane.showMessageDialog(null, "USER ID : "+ObjFlow.getAttribute("USER_ID").getVal());
                rowData[0] = Integer.toString(i);
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[3] = (String) ObjFlow.getAttribute("STATUS").getObj();
                rowData[4] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6] = (String) ObjFlow.getAttribute("REMARKS").getObj();

                DataModelApprovalStatus.addRow(rowData);
            }

            //Showing Audit Trial History
            FormatGridUpdateHistory();
            HashMap History = objPieceAmendApproval.getHistoryList(EITLERPGLOBAL.gCompanyID + "", DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsPieceAmendApproval_STOCK ObjHistory = (clsPieceAmendApproval_STOCK) History.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                String user = ObjHistory.getAttribute("UPDATED_BY").getString();
                if (user.equals("")) {
                    user = "0";
                }
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(user));
                rowData[2] = ObjHistory.getAttribute("ENTRY_DATE").getString();

                String ApprovalStatus = "";

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {

                    ApprovalStatus = "Final Approved";

                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
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
            System.out.println("");
            e.printStackTrace();
        }

        DoNotEvaluate = false;
    }

    private void FormatGrid() {
        try {

            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            TableColumnModel ColModel = Table.getColumnModel();
            Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            DataModel.addColumn("SELECT"); //0 - Read Only
            DataModel.addColumn("PIECE NO"); //1 - Read Only
            DataModel.addColumn("ORDER DATE");//2
            DataModel.addColumn("MACHINE NO");//3
            DataModel.addColumn("POSITION NO");//4
            DataModel.addColumn("POSITION DESC");//5
            DataModel.addColumn("PARTY CODE");//6
            DataModel.addColumn("PARTY");//7
            DataModel.addColumn("PRODUCT CODE");//8
            DataModel.addColumn("PRODUCT DESC"); //9
            DataModel.addColumn("GROUP"); //10
            DataModel.addColumn("GROUP_UPDATED"); //11 
            DataModel.addColumn("STYLE"); //12
            DataModel.addColumn("STYLE_UPDATED"); //13
            DataModel.addColumn("LENGTH"); //14
            DataModel.addColumn("LENGTH_UPDATED"); //15
            DataModel.addColumn("WIDTH"); //16
            DataModel.addColumn("WIDTH_UPDATED"); //17
            DataModel.addColumn("GSM"); //18
            DataModel.addColumn("GSM_UPDATED"); //19
            DataModel.addColumn("PIECE STAGE"); //20
            DataModel.addColumn("Expected Dispatch"); //18
            DataModel.addColumn("Obsolete UPN Assign Status"); //18
            DataModel.addColumn("Base GSM"); //18
            DataModel.addColumn("Web GSM"); //18
            DataModel.addColumn("Weave"); //18
            DataModel.addColumn("CFM (Targetted)"); //18
            DataModel.addColumn("Loom No"); //18
            DataModel.addColumn("Paper Prod. Type"); //18
            DataModel.addColumn("Unmapped Reason"); //18
            DataModel.addColumn("Scrap Reason"); //18
            
            DataModel.SetVariable(0, "SELECT");
            DataModel.SetVariable(1, "PIECE_NO");
            DataModel.SetVariable(2, "DATE");
            DataModel.SetVariable(3, "MACHINE_NO");
            DataModel.SetVariable(4, "POSITION_NO");
            DataModel.SetVariable(5, "POSITION_DESC");
            DataModel.SetVariable(6, "PARTY_CODE");
            DataModel.SetVariable(7, "PARTY");
            DataModel.SetVariable(8, "PRODUCT_CODE");
            DataModel.SetVariable(9, "PRODUCT_DESC"); //
            DataModel.SetVariable(10, "GROUP"); //
            DataModel.SetVariable(11, "GROUP_UPDATED"); //
            DataModel.SetVariable(12, "STYLE"); //
            DataModel.SetVariable(13, "STYLE_UPDATED"); //
            DataModel.SetVariable(14, "LENGTH"); //
            DataModel.SetVariable(15, "LENGTH_UPDATED"); //
            DataModel.SetVariable(16, "WIDTH"); //
            DataModel.SetVariable(17, "WIDTH_UPDATED"); //
            DataModel.SetVariable(18, "GSM"); //
            DataModel.SetVariable(19, "GSM_UPDATED"); //
            DataModel.SetVariable(20, "PIECE_STAGE"); //
            DataModel.SetVariable(21, "EXPECTED_DISPATCH"); //
            DataModel.SetVariable(22, "OBSOLETE_UPN_ASSIGN_STATUS"); //
            DataModel.SetVariable(23, "BASE_GSM"); //
            DataModel.SetVariable(24, "WEB_GSM"); //
            DataModel.SetVariable(25, "WEAVE"); //
            DataModel.SetVariable(26, "CFM_TARGETTED"); //
            DataModel.SetVariable(27, "LOOM_NO"); //
            DataModel.SetVariable(28, "PAPER_PROD_TYPE"); //
            DataModel.SetVariable(29, "UNMAPPED_REASON"); //
            DataModel.SetVariable(30, "SCRAP_REASON"); //
            
            
            int ImportCol = 0;
            Renderer.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setEnabled(true);
            aCheckBox.setSelected(false);
            Table.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            Table.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);

            Table.getColumnModel().getColumn(0).setMinWidth(20);
            Table.getColumnModel().getColumn(1).setMinWidth(90);

            Table.getColumnModel().getColumn(2).setMinWidth(0);
            Table.getColumnModel().getColumn(2).setMaxWidth(0);
            Table.getColumnModel().getColumn(3).setMinWidth(0);
            Table.getColumnModel().getColumn(3).setMaxWidth(0);
            Table.getColumnModel().getColumn(4).setMinWidth(0);
            Table.getColumnModel().getColumn(4).setMaxWidth(0);
            Table.getColumnModel().getColumn(5).setMinWidth(0);
            Table.getColumnModel().getColumn(5).setMaxWidth(0);
            Table.getColumnModel().getColumn(6).setMinWidth(0);
            Table.getColumnModel().getColumn(6).setMaxWidth(0);
            Table.getColumnModel().getColumn(7).setMinWidth(0);
            Table.getColumnModel().getColumn(7).setMaxWidth(0);
            Table.getColumnModel().getColumn(8).setMinWidth(0);
            Table.getColumnModel().getColumn(8).setMaxWidth(0);
            Table.getColumnModel().getColumn(9).setMinWidth(0);
            Table.getColumnModel().getColumn(9).setMaxWidth(0);
            Table.getColumnModel().getColumn(11).setMinWidth(0);
            Table.getColumnModel().getColumn(11).setMaxWidth(0);

            Table.getColumnModel().getColumn(10).setMinWidth(70);
            Table.getColumnModel().getColumn(12).setMinWidth(100);
            Table.getColumnModel().getColumn(13).setMinWidth(100);
            Table.getColumnModel().getColumn(14).setMinWidth(100);
            Table.getColumnModel().getColumn(15).setMinWidth(100);
            Table.getColumnModel().getColumn(16).setMinWidth(100);
            Table.getColumnModel().getColumn(17).setMinWidth(100);
            Table.getColumnModel().getColumn(18).setMinWidth(100);
            Table.getColumnModel().getColumn(19).setMinWidth(100);
            Table.getColumnModel().getColumn(20).setMinWidth(100);
            Table.getColumnModel().getColumn(21).setMinWidth(150);
            Table.getColumnModel().getColumn(22).setMinWidth(200);
            Table.getColumnModel().getColumn(26).setMinWidth(150);
            Table.getColumnModel().getColumn(27).setMinWidth(150);
            Table.getColumnModel().getColumn(28).setMinWidth(150);
            Table.getColumnModel().getColumn(29).setMinWidth(250);
            Table.getColumnModel().getColumn(30).setMinWidth(250);
            
            Table.getColumnModel().getColumn(DataModel.getColFromVariable("STYLE_UPDATED")).setCellRenderer(Renderer);
            Table.getColumnModel().getColumn(DataModel.getColFromVariable("LENGTH_UPDATED")).setCellRenderer(Renderer);
            Table.getColumnModel().getColumn(DataModel.getColFromVariable("WIDTH_UPDATED")).setCellRenderer(Renderer);
            Table.getColumnModel().getColumn(DataModel.getColFromVariable("GSM_UPDATED")).setCellRenderer(Renderer);

            TableColumn ObsoleteColumn = Table.getColumnModel().getColumn(22);
            JComboBox fields = new JComboBox();
            fields.addItem("MAPPED");
            fields.addItem("UNMAPPED");
            fields.addItem("SCRAP");
            ObsoleteColumn.setCellEditor(new DefaultCellEditor(fields));
            
            TableColumn dateColumn = Table.getColumnModel().getColumn(21);

            JComboBox monthbox = new JComboBox();
            String month_name = "";
            Date date = new Date();
            int month = date.getMonth();
            int year = date.getYear() + 1900;
            monthbox.addItem("");
            for (int i = 0; i < 40; i++) {
                month = month + 1;

                if (month >= 13) {
                    month = 1;
                    year = year + 1;
                }

                if (month == 1) {
                    month_name = "Jan";
                } else if (month == 2) {
                    month_name = "Feb";
                } else if (month == 3) {
                    month_name = "Mar";
                } else if (month == 4) {
                    month_name = "Apr";
                } else if (month == 5) {
                    month_name = "May";
                } else if (month == 6) {
                    month_name = "Jun";
                } else if (month == 7) {
                    month_name = "Jul";
                } else if (month == 8) {
                    month_name = "Aug";
                } else if (month == 9) {
                    month_name = "Sep";
                } else if (month == 10) {
                    month_name = "Oct";
                } else if (month == 11) {
                    month_name = "Nov";
                } else if (month == 12) {
                    month_name = "Dec";
                }
                monthbox.addItem(month_name + " - " + year);
            }

            dateColumn.setCellEditor(new DefaultCellEditor(monthbox));
            ReasonReadOnly();

            FormatGrid_revision();
        } catch (Exception e) {

            System.out.println("Error in FormateGrid : " + e.getMessage());
        }
    }

    private void FormatGrid_revision() {
        DataModel_Revision = new EITLTableModel();
        tblRevision.removeAll();

        tblRevision.setModel(DataModel_Revision);

        tblRevision.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DataModel_Revision.addColumn("SRNO"); //0 - Read Only
        DataModel_Revision.addColumn("PIECE NO"); //1 - 
        DataModel_Revision.addColumn("REVIEW NO"); //1 - 
        DataModel_Revision.addColumn("REVIEW DATE"); //1 -
        DataModel_Revision.addColumn("REVISION MONTH"); //1 -

        DataModel_Revision.SetVariable(0, "SRNO");
        DataModel_Revision.SetVariable(1, "PIECE_NO");
        DataModel_Revision.SetVariable(2, "REVIEW_NO");
        DataModel_Revision.SetVariable(3, "REVIEW_DATE");
        DataModel_Revision.SetVariable(4, "REVISION_MONTH");

        tblRevision.getColumnModel().getColumn(0).setMinWidth(10);
        tblRevision.getColumnModel().getColumn(1).setMinWidth(90);
        tblRevision.getColumnModel().getColumn(2).setMinWidth(150);
        tblRevision.getColumnModel().getColumn(3).setMinWidth(150);
        tblRevision.getColumnModel().getColumn(4).setMinWidth(150);
    }

    //Generates Hierarchy Combo Box

    private void GenerateHierarchyCombo() {
        HashMap hmHierarchyList = new HashMap();

        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        hmHierarchyList = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId);

        if (EditMode == EITLERPGLOBAL.EDIT) {
            hmHierarchyList = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId);
        }
        for (int i = 1; i <= hmHierarchyList.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
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
        DOC_NO = PIECE_AMEND_STOCK_NO.getText();
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

                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(ModuleId, DOC_NO + "", (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(ModuleId, DOC_NO, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(ModuleId, PIECE_AMEND_STOCK_NO.getText());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }
    }

    //Generates User Name Combo Box
//    private void SetupApproval() {
//        /*// --- Hierarchy Change Rights Check --------
//         if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,75)) {
//         cmbHierarchy.setEnabled(true);
//         }else {
//         cmbHierarchy.setEnabled(false);
//         }*/
//
//        // select hold for default approval
//        OpgHold.setSelected(true);
//
//        if (EditMode == EITLERPGLOBAL.ADD) {
//            cmbHierarchy.setEnabled(true);
//            OpgReject.setEnabled(false);
//        } else {
//            cmbHierarchy.setEnabled(false);
//        }
//
//        //Set Default Hierarchy ID for User
//        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
//        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);
//
//        if (EditMode == EITLERPGLOBAL.ADD) {
//            lnFromUserId = (int) EITLERPGLOBAL.gNewUserID;
//            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
//            txtFromRemarks.setText("Creator of Document");
//        } else {
//            int FromUserID = clsFeltProductionApprovalFlow.getFromID(ModuleId, objPieceAmendApproval.getAttribute("PIECE_AMEND_STOCK_NO").getString());
//            lnFromUserId = FromUserID;
//            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
//            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(ModuleId, FromUserID, objPieceAmendApproval.getAttribute("PIECE_AMEND_STOCK_NO").getString());
//
//            txtFrom.setText(strFromUser);
//            txtFromRemarks.setText(strFromRemarks);
//        }
//
//        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
//
//        if (clsHierarchy.CanSkip(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
//            cmbSendTo.setEnabled(true);
//        } else {
//            cmbSendTo.setEnabled(false);
//        }
//
//        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
//            OpgFinal.setEnabled(true);
//        } else {
//            OpgFinal.setEnabled(false);
//            OpgFinal.setSelected(false);
//        }
//
//        //In Edit Mode Hierarchy and Reject Should be disabled
//        if (EditMode == EITLERPGLOBAL.EDIT) {
//            cmbHierarchy.setEnabled(false);
//            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, Order_No_Conversion.PieceAmnd_No_Only(PIECE_AMEND_STOCK_NO.getText())+"")) {
//                OpgReject.setEnabled(false);
//            }
//        }
//
//        if (EditMode == 0) {
//            //Disable all hierarchy controls if not in Add/Edit Mode
//            cmbHierarchy.setEnabled(false);
//            txtFrom.setEnabled(false);
//            txtFromRemarks.setEnabled(false);
//            OpgApprove.setEnabled(false);
//            OpgFinal.setEnabled(false);
//            OpgReject.setEnabled(false);
//            cmbSendTo.setEnabled(false);
//            txtToRemarks.setEnabled(false);
//        }
//    }
    private void SetupApproval() {

        if (cmbHierarchy.getItemCount() > 1) {
            cmbHierarchy.setEnabled(true);
        }
        //JOptionPane.showMessageDialog(null, "Approval Cmb : "+cmbHierarchy.getItemCount());
        //In Edit Mode Hierarchy Should be disabled
        if (EditMode == EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
        } else {
            cmbHierarchy.setEnabled(false);
        }

        //Set Default Hierarchy ID for User
        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);

        if (EditMode == EITLERPGLOBAL.ADD) {
            lnFromID = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {
            lnFromID = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID));
            txtFromRemarks.setText("");
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //JOptionPane.showMessageDialog(null, "Hierarchy Id = "+SelHierarchyID);
        //GenerateFromCombo();
        //GenerateSendToCombo();

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

        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
        }

        if (EditMode == 0) {
            cmbHierarchy.setEnabled(false);
            txtFrom.setEnabled(false);
            //txtFromRemarks.setEnabled(false);
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6087, 60871)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6087, 60872)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6087, 60873)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6087, 60874)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }

    public void Add() {
        //  EditMode=EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        SetupApproval();
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 763;
        jButton1.setEnabled(false);

        aList.FirstFreeNo = 227;

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
            PIECE_AMEND_STOCK_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, false));
            PIECE_AMEND_DATE.setText(EITLERPGLOBAL.getCurrentDate());

            lblTitle.setText(" Piece Register Updation based on Machine Master Amendment (STOCK) -   " + PIECE_AMEND_STOCK_NO.getText());
            lblTitle.setBackground(Color.BLUE);
        } else {
            JOptionPane.showMessageDialog(null, "You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
//        SetFields(true);
//        DisableToolbar();
//        ClearFields();
//        SetupApproval();
        lblTitle.setBackground(Color.GRAY);
        SHOW_LIST.setEnabled(true);
    }

    private void Edit() {
        String productionDocumentNo = objPieceAmendApproval.getAttribute("PIECE_AMEND_STOCK_NO").getString() + "";
        if (objPieceAmendApproval.IsEditable(productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            jButton1.setEnabled(false);
            //ReasonResetReadonly();
            //cmbOrderReason.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, productionDocumentNo)) {
                SetFields(true);
            } else {
                EnableApproval();
            }

            SetupApproval();
        } else {
            JOptionPane.showMessageDialog(this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "EDITING ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Delete() {

    }

    private void Save() {

        if (Table.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(this, "Enter Piece Updation Details Before Saving.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select the hierarchy", "ERROR", JOptionPane.ERROR_MESSAGE);
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

        try {
            for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                if (!DataModel.getValueAt(i, 1).toString().equals("")) {
                    String bale_reopen_pending = data.getStringValueFromDB("SELECT H.BALE_NO FROM PRODUCTION.FELT_REOPEN_BALE_HEADER H,PRODUCTION.FELT_REOPEN_BALE_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=0 AND H.CANCELED=0 AND D.PIECE_NO='" + DataModel.getValueAt(i, 1).toString() + "'");
                    System.out.println("SELECT H.BALE_NO FROM PRODUCTION.FELT_REOPEN_BALE_HEADER H,PRODUCTION.FELT_REOPEN_BALE_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=0 AND H.CANCELED=0 AND D.PIECE_NO='" + DataModel.getValueAt(i, 1).toString() + "'");
                    if (!bale_reopen_pending.equals("")) {
                        JOptionPane.showMessageDialog(this, "Bale Reopen Pending for PIECE No = " + DataModel.getValueAt(i, 1).toString() + ", Bale No : " + bale_reopen_pending);
                        return;
                    }
                }
            }

//            for (int i = 0; i < Table.getRowCount(); i++) {
//                if (DataModel.getValueAt(i, 21).toString().equals("")) {
//                    JOptionPane.showMessageDialog(this, "Expected Dispatch is compulsory");
//                    return;
//                }
//            }
            for (int i = 0; i < Table.getRowCount(); i++) {
                if (clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) == 29) {
                    if (DataModel.getValueAt(i, 21).toString().equals("") && Table.getValueAt(i, 0).equals(false)) {
                        JOptionPane.showMessageDialog(this, "Expected Dispatch is compulsory");
                        return;
                    }
                }
                //OBSOLETE_UPN_ASSIGN_STATUS
                if(Table.getValueAt(i, 0).equals(true) && DataModel.getValueByVariable("OBSOLETE_UPN_ASSIGN_STATUS", i).equals(""))
                {
                    JOptionPane.showMessageDialog(this, "Obsolete Status is compulsory for obsolete pieces");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SetData();

        //feltOrder.LoadData();
        if (EditMode == EITLERPGLOBAL.ADD) {

            if (objPieceAmendApproval.Insert()) {

                SelectFirstFree aList = new SelectFirstFree();
                aList.ModuleID = 763;
                aList.FirstFreeNo = 227;
                clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, aList.ModuleID, aList.FirstFreeNo, true);

                if (OpgFinal.isSelected()) {

                    try {
                        String DOC_NO = PIECE_AMEND_STOCK_NO.getText();
                        String DOC_DATE = PIECE_AMEND_DATE.getText();
                        String Party_Code = "";
                        int Module_Id = ModuleId;

                        String responce = JavaMail.sendFinalApprovalMail(Module_Id, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true, 0);
                       System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                DisplayData();

            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + objPieceAmendApproval.LastError, " SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (objPieceAmendApproval.Update()) {

                if (OpgFinal.isSelected()) {
                    try {
                        String DOC_NO = PIECE_AMEND_STOCK_NO.getText();
                        String DOC_DATE = PIECE_AMEND_DATE.getText();
                        String Party_Code = PARTY_CODE.getText();
                        int Module_Id = ModuleId;

                        //String responce = JavaMail.sendFinalApprovalMail(Module_Id, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true, 0);
                        //System.out.println("Send Mail Responce : " + responce);
                        
                        //SEND F.APPROVAL MAIL
                        // STOCK Review START 
                
                        String pMessage = "STOCK Piece Review No : " + DOC_NO + ",<br>Review Entry Date : " + DOC_DATE + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID) + "<br>";

                        try {
                            Connection Conn1;
                            Statement stmt1;
                            ResultSet rsData1;
                            boolean Obsolete_Flag = false;
                            Conn1 = data.getConn();
                            stmt1 = Conn1.createStatement();
                            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER H, " +
                                                        "PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_DETAIL D " +
                                                        "WHERE H.PIECE_AMEND_STOCK_NO=D.PIECE_AMEND_STOCK_NO AND H.PIECE_AMEND_STOCK_NO='" + DOC_NO + "'");
                            rsData1.first();

                            pMessage = pMessage + "<br><br>";

        //                        Connection Conn;
        //                        Statement stmt;
        //                        ResultSet rsData;

        //                        Conn = data.getConn();
        //                        stmt = Conn.createStatement();
        //                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
        //                        rsData.first();

                                //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                                pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("MM_PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("MM_PARTY_CODE"));
                                pMessage = pMessage + "<br> MACHINE NO : " + rsData1.getString("MM_MACHINE_NO");
                                pMessage = pMessage + "<br> POSITION NO : " + rsData1.getString("MM_MACHINE_POSITION");
                                pMessage = pMessage + "<br> POSITION DESCRIPTION : " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("MM_MACHINE_POSITION") + "'");
                                String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsData1.getString("MM_MACHINE_POSITION"));
                                pMessage = pMessage + "<br> POSITION DESIGN NO : "+Position_Des_No;
                                pMessage = pMessage + "<br> UPN : "+rsData1.getString("MM_PARTY_CODE")+""+rsData1.getString("MM_MACHINE_NO")+""+Position_Des_No;

                                pMessage = pMessage + "<br><br><u>PIECE DETAILS</u><br>";
                                pMessage = pMessage + "<table border='1'>"
                                        + "<tr>"
                                        + "<th align='center'> PIECE NO </th>"
                                        + "<th align='center'> OBSOLETE</th>"
                                        + "<th align='center'> STYLE </th>"
                                        + "<th align='center'> UPDATE STYLE </th>"
                                        + "<th align='center'> LENGTH </th>"
                                        + "<th align='center'> UPDATED LENGTH </th>"
                                        + "<th align='center'> WIDTH </th>"
                                        + "<th align='center'> UPDATED WIDTH </th>"
                                        + "<th align='center'> GSM </th>"
                                        + "<th align='center'> UPDATED GSM </th>"
                                        + "<th align='center'> PIECE STAGE </th>"
                                        + "<th align='center'> EXPECTED DISPACH </th>"
                                        + "</tr>";

                                rsData1.first();

                                if (rsData1.getRow() > 0) {
                                    while (!rsData1.isAfterLast()){ 

                                        String Obsolete = "";


                                        if("1".equals(rsData1.getString("SELECTED")))
                                        {
                                            Obsolete = "YES";
                                            Obsolete_Flag = true;
                                        }

                                        pMessage = pMessage + ""
                                            + "<tr>"
                                            + "<td align='center'> "+rsData1.getString("PIECE_NO")+" </td>"
                                            + "<td align='center'> "+Obsolete+" </td>"
                                            + "<td align='center'> "+rsData1.getString("STYLE")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("STYLE_UPDATED")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("LENGTH")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("LENGTH_UPDATED")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("WIDTH")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("WIDTH_UPDATED")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("GSM")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("GSM_UPDATED")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("PIECE_STAGE")+" </td>"
                                            + "<td align='center'> "+rsData1.getString("EXPECTED_DISPATCH")+" </td>"
                                            + "</tr>";
                                        rsData1.next();
                                    }
                                }
                                pMessage = pMessage + "</table>";

                                
                                //
                                int rows = DataModel_Revision.getRowCount();
                                if(rows>0)
                                {
                                    pMessage = pMessage + "<br><br><u>PIECE EXPECTED DISPATCH MONTH - REVISION</u><br>";
                                    pMessage = pMessage + "<table border='1'>"
                                                        + "<tr>"
                                                        + "<th align='center'> PIECE NO </th>"
                                                        + "<th align='center'> REVIEW NO</th>"
                                                        + "<th align='center'> REVIEW DATE</th>"
                                                        + "<th align='center'> REVISION MONTH </th>"
                                                        + "</tr>";
                                    
                                    for(int i=0;i<rows;i++)
                                    {
                                            pMessage = pMessage + ""
                                            + "<tr>"
                                            + "<td align='center'> "+DataModel_Revision.getValueByVariable("PIECE_NO", i)+" </td>"
                                            + "<td align='center'> "+DataModel_Revision.getValueByVariable("REVIEW_NO", i)+" </td>"
                                            + "<td align='center'> "+DataModel_Revision.getValueByVariable("REVIEW_DATE", i)+" </td>"
                                            + "<td align='center'> "+DataModel_Revision.getValueByVariable("REVISION_MONTH", i)+" </td>"
                                            + "</tr>";
                                                    
                                    }
                                }
                                pMessage = pMessage + "</table>";
                                System.out.println("MESSAGE : "+pMessage);
                                
                                pMessage = pMessage + "<br><br>All Approvers Remark as given below : ";

                                    pMessage += "</table>";
                                    pMessage += "<table border=1>";
                                    pMessage += "<tr><td align='center'><b> Sr.No </b></td>"
                                            + "<td align='center'><b> User </b></td>"
                                            + "<td align='center'><b> Date  </b></td>"
                                            + "<td align='center'><b> Status </b></td>"
                                            + "<td align='center'><b> Remark </b></td>"
                                            + "</tr>";

                                    HashMap hmApprovalHistory = clsPieceAmendApproval_STOCK.getHistoryList(EITLERPGLOBAL.gCompanyID+"", DOC_NO);
                                    for (int ij = 1; ij <= hmApprovalHistory.size(); ij++) {
                                        pMessage += "<tr>";

                                        clsPieceAmendApproval_STOCK ObjHistory = (clsPieceAmendApproval_STOCK) hmApprovalHistory.get(Integer.toString(ij));
                                        pMessage += "<td>" + Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal()) + "</td>";

                                        pMessage += "<td>" + clsUser.getUserName(2, (int) ObjHistory.getAttribute("UPDATED_BY").getVal()) + "</td>";
                                        
                                        pMessage += "<td>" +  ObjHistory.getAttribute("ENTRY_DATE").getString() + "</td>";
                                        
                                        String ApprovalStatus = "";

                                        if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                                            ApprovalStatus = "Hold";
                                        }

                                        if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                                            ApprovalStatus = "Approved";
                                        }

                                        if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                                            ApprovalStatus = "Final Approved";
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
                                        pMessage += "<td>" + ApprovalStatus + "</td>";
                                        pMessage += "<td>" + ObjHistory.getAttribute("APPROVER_REMARKS").getString() + "</td>";
                                        pMessage += "</tr>";
                                    }
                                    pMessage += "</table>";
                                    pMessage += "<br><br>";
                                    pMessage += "<br>";

                    //            HashMap hmSendToList;
                                    String recievers = "sdmlerp@dineshmills.com";

                                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
                                HashMap hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, true);
                                for (int i = 1; i <= hmSendToList.size(); i++) {
                                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();
                    
                                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
                    
                                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
                                    if (!to.equals("")) {
                                        recievers = recievers + "," + to;
                                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
                                    }
                                }
                                
                                
                                //
                                
                                if(Obsolete_Flag)
                                {
                                    recievers = recievers + "," + "feltwh@dineshmills.com";
                                    pMessage = pMessage + "<br> feltwh@dineshmills.com";
                                }
                                
                                //
                                //recievers = recievers + "aditya@dineshmills.com,vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com";
                                    pMessage = pMessage + "<br><br><br><br>**** This is an auto-generated email, please do not reply ****";
                                    
                                    String pSubject = "Notification : Felt Piece Review : " + DOC_NO;
                                    System.out.println("Recivers : " + recievers);
                                    System.out.println("pSubject : " + pSubject);
                                    System.out.println("pMessage : " + pMessage);

                                    String responce1 = MailNotification.sendNotificationMail(ModuleId, pSubject, pMessage, recievers, "", SelHierarchyID);
                                    System.out.println("Send Mail Responce : " + responce1);
                                
                                
                            } catch (Exception e) {
                                System.out.println("Error on Mail: " + e.getMessage());
                            }
                            //SENDMAIL END STOCK Review END
                        
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving editing. Error is " + objPieceAmendApproval.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
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
    private void SetData() {

        objPieceAmendApproval.setAttribute("PIECE_AMEND_STOCK_NO", PIECE_AMEND_STOCK_NO.getText());
        objPieceAmendApproval.setAttribute("PIECE_AMEND_DATE", EITLERPGLOBAL.formatDateDB(PIECE_AMEND_DATE.getText()));

        objPieceAmendApproval.setAttribute("MM_PARTY_CODE", PARTY_CODE.getText());
        objPieceAmendApproval.setAttribute("MM_MACHINE_NO", MACHINE_NO.getText());
        objPieceAmendApproval.setAttribute("MM_MACHINE_POSITION", POSITION.getText());

        objPieceAmendApproval.setAttribute("ENTRY_REASON", txtEntryReason.getText());
        //ENTRY_DOCNO
        objPieceAmendApproval.setAttribute("ENTRY_DOCNO", lblRefDocNo.getText());

        DOC_NO = PIECE_AMEND_STOCK_NO.getText();
        objPieceAmendApproval.setAttribute("DOC_NO", DOC_NO);
        objPieceAmendApproval.setAttribute("DOC_DATE", EITLERPGLOBAL.formatDateDB(PIECE_AMEND_DATE.getText()));
        objPieceAmendApproval.setAttribute("MODULE_ID", ModuleId);
        objPieceAmendApproval.setAttribute("USER_ID", EITLERPGLOBAL.gNewUserID);

        //----- Update Approval Specific Fields -----------//
        objPieceAmendApproval.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));

        objPieceAmendApproval.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        objPieceAmendApproval.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        objPieceAmendApproval.setAttribute("FROM_REMARKS", txtToRemarks.getText());
        objPieceAmendApproval.setAttribute("APPROVER_REMARKS", txtToRemarks.getText());
        objPieceAmendApproval.setAttribute("REJECTED_REMARKS", txtToRemarks.getText());

        if (from_machine_master) {
                //Fix Hierarchy Code  : 1766 VDS-KM-RKP
            //VDS-28
            //TO-60
            objPieceAmendApproval.setAttribute("HIERARCHY_ID", 1766);
            objPieceAmendApproval.setAttribute("FROM", 28);
            objPieceAmendApproval.setAttribute("TO", 60);
        }

        if (OpgApprove.isSelected()) {
            objPieceAmendApproval.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            objPieceAmendApproval.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            objPieceAmendApproval.setAttribute("APPROVAL_STATUS", "R");
            objPieceAmendApproval.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            objPieceAmendApproval.setAttribute("APPROVAL_STATUS", "H");
        }

        if (EditMode == EITLERPGLOBAL.ADD) {
            objPieceAmendApproval.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            objPieceAmendApproval.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
        } else {
            objPieceAmendApproval.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            objPieceAmendApproval.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            objPieceAmendApproval.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            objPieceAmendApproval.setAttribute("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }

        //======= Set Line part ============
        try {
            objPieceAmendApproval.hmPieceAmendApprovalDetail.clear();

            for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                if (!DataModel.getValueAt(i, 1).toString().equals("")) {
                    clsPieceAmendApprovalDetail_STOCK objFeltOrderDetails = new clsPieceAmendApprovalDetail_STOCK();

                    if (Table.getValueAt(i, 0).equals(true)) {
                        objFeltOrderDetails.setAttribute("SELECTED", 1);
                    } else {
                        objFeltOrderDetails.setAttribute("SELECTED", 0);
                    }

                    objFeltOrderDetails.setAttribute("PIECE_AMEND_STOCK_NO", PIECE_AMEND_STOCK_NO.getText());

                    objFeltOrderDetails.setAttribute("PIECE_NO", Table.getValueAt(i, 1));
                    objFeltOrderDetails.setAttribute("MM_PARTY_CODE", Table.getValueAt(i, 7));
                    objFeltOrderDetails.setAttribute("MM_MACHINE_NO", Table.getValueAt(i, 3));
                    objFeltOrderDetails.setAttribute("MM_MACHINE_POSITION", Table.getValueAt(i, 4));
                    objFeltOrderDetails.setAttribute("LENGTH", Table.getValueAt(i, 14));
                    objFeltOrderDetails.setAttribute("LENGTH_UPDATED", Table.getValueAt(i, 15));
                    objFeltOrderDetails.setAttribute("WIDTH", Table.getValueAt(i, 16));
                    objFeltOrderDetails.setAttribute("WIDTH_UPDATED", Table.getValueAt(i, 17));
                    objFeltOrderDetails.setAttribute("GSM", Table.getValueAt(i, 18));
                    objFeltOrderDetails.setAttribute("GSM_UPDATED", Table.getValueAt(i, 19));
                    objFeltOrderDetails.setAttribute("STYLE", Table.getValueAt(i, 12));
                    objFeltOrderDetails.setAttribute("STYLE_UPDATED", Table.getValueAt(i, 13));
                    objFeltOrderDetails.setAttribute("FLET_GROUP", Table.getValueAt(i, 10));
                    objFeltOrderDetails.setAttribute("FLET_GROUP_UPDATED", Table.getValueAt(i, 11));

                    float l = Float.parseFloat(Table.getValueAt(i, 14) + "");
                    float l_u = Float.parseFloat(Table.getValueAt(i, 15) + "");

                    float w = Float.parseFloat(Table.getValueAt(i, 16) + "");
                    float w_u = Float.parseFloat(Table.getValueAt(i, 17) + "");

                    float g = Float.parseFloat(Table.getValueAt(i, 18) + "");
                    float g_u = Float.parseFloat(Table.getValueAt(i, 19) + "");

                    objFeltOrderDetails.setAttribute("WEIGHT", EITLERPGLOBAL.round(((l * w * g) / 1000), 1) + "");
                    objFeltOrderDetails.setAttribute("WEIGHT_UPDATED", EITLERPGLOBAL.round(((l_u * w_u * g_u) / 1000), 1) + "");
                    objFeltOrderDetails.setAttribute("SQMTR", EITLERPGLOBAL.round((l * w), 2) + "");
                    objFeltOrderDetails.setAttribute("SQMTR_UPDATED", EITLERPGLOBAL.round((l_u * w_u), 2) + "");
                    objFeltOrderDetails.setAttribute("PIECE_STAGE", Table.getValueAt(i, 20));
                    objFeltOrderDetails.setAttribute("EXPECTED_DISPATCH", Table.getValueAt(i, 21));
                    
                    objFeltOrderDetails.setAttribute("OBSOLETE_UPN_ASSIGN_STATUS", DataModel.getValueByVariable("OBSOLETE_UPN_ASSIGN_STATUS", i));
                    objFeltOrderDetails.setAttribute("BASE_GSM", DataModel.getValueByVariable("BASE_GSM", i));
                    objFeltOrderDetails.setAttribute("WEB_GSM", DataModel.getValueByVariable("WEB_GSM", i));
                    objFeltOrderDetails.setAttribute("WEAVE", DataModel.getValueByVariable("WEAVE", i));
                    objFeltOrderDetails.setAttribute("CFM_TARGETTED", DataModel.getValueByVariable("CFM_TARGETTED", i));
                    objFeltOrderDetails.setAttribute("LOOM_NO", DataModel.getValueByVariable("LOOM_NO", i));
                    objFeltOrderDetails.setAttribute("PAPER_PROD_TYPE", DataModel.getValueByVariable("PAPER_PROD_TYPE", i));
                    objFeltOrderDetails.setAttribute("UNMAPPED_REASON", DataModel.getValueByVariable("UNMAPPED_REASON", i));
                    objFeltOrderDetails.setAttribute("SCRAP_REASON", DataModel.getValueByVariable("SCRAP_REASON", i));

                    objPieceAmendApproval.hmPieceAmendApprovalDetail.put(Integer.toString(objPieceAmendApproval.hmPieceAmendApprovalDetail.size() + 1), objFeltOrderDetails);

                }
            }
        } catch (Exception e) {
            System.out.println("Eroor on setData : " + e.getMessage());
            e.printStackTrace();
        };
    }

    private void Cancel() {
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();

    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.PieceAmendmentApproval_STOCK.frmFindFeltPieceAmendApproval_Stock", true);
        frmFindFeltPieceAmendApproval_Stock ObjFind = (frmFindFeltPieceAmendApproval_Stock) ObjLoader.getObj();

        if (ObjFind.Cancelled == false) {
            if (!objPieceAmendApproval.Filter(ObjFind.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, " No records found.", "Find Felt Piece Details", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    // find details by production date
    public void Find(String AmendID) {
        objPieceAmendApproval.Filter(" PIECE_AMEND_STOCK_NO='" + AmendID + "'");
        SetMenuForRights();
        DisplayData();
    }

    // find details by piece no.
    public void Find(String pieceNo, String prodDate) {

    }

    // find all pending document
    public void FindWaiting() {
        //   objPieceAmendApproval.Filter(" AND PROD_DOC_NO IN (SELECT DISTINCT PROD_DOC_NO FROM PRODUCTION.FELT_PROD_DATA, PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+EITLERPGLOBAL.gNewUserID+" AND STATUS='W' AND MODULE_ID="+ModuleId+" AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    private void MoveFirst() {
        objPieceAmendApproval.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        objPieceAmendApproval.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        objPieceAmendApproval.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        objPieceAmendApproval.MoveLast();
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
        //   DataModel.ClearAllReadOnly();
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
        DataModelUpdateHistory.addColumn("FROM IP");
    }

    private void GenerateAmendReasonCombo() {
        HashMap hmAmendList = new HashMap();

        cmbAmendReasonModel = new EITLComboModel();

        //    hmAmendList = clsPieceUpdation.getAmendReasonList();
        for (int i = 1; i <= hmAmendList.size(); i++) {
            cmbAmendReasonModel.addElement((ComboData) hmAmendList.get(new Integer(i)));
        }
    }

    private void ReasonReadOnly() {
        //DataModel.SetReadOnly(0);
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
        DataModel.SetReadOnly(13);
        DataModel.SetReadOnly(14);
        DataModel.SetReadOnly(15);
        DataModel.SetReadOnly(16);
        DataModel.SetReadOnly(17);
        DataModel.SetReadOnly(18);
        DataModel.SetReadOnly(19);
        DataModel.SetReadOnly(20);

        if (EditMode == EITLERPGLOBAL.EDIT) {
            //For Felt Sales
            if (clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) == 29) {

            } else {
                DataModel.SetReadOnly(20);
            }
        }
        //For Not Sales Dept Users ... Disable Expected Month
        if (clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) != 29) {
            DataModel.SetReadOnly(21);
        }
//        DataModel.SetReadOnly(21);
//        DataModel.SetReadOnly(22);
//        DataModel.SetReadOnly(23);
//        DataModel.SetReadOnly(24);
//        DataModel.SetReadOnly(25);
//        DataModel.SetReadOnly(26);
//        DataModel.SetReadOnly(27);
//        DataModel.SetReadOnly(28);
//        DataModel.SetReadOnly(29);
//        DataModel.SetReadOnly(30);
//        DataModel.SetReadOnly(31);
//        DataModel.SetReadOnly(32);
    }

    private void ReasonResetReadonly() {
//        int AmdReason = Integer.parseInt(txtamendreasoncode.getText());
        ReasonReadOnly();
    }

//    public void setData_ToTable() {
//        int size = DataModel.getRowCount();
//        for (int j = size - 1; j >= 0; j--) {
//            DataModel.removeRow(j);
//    }
//
//        for (int j = 1; j <= hmPieceList.size(); j++) {
//            clsPieceAmendApproval piece = (clsPieceAmendApproval) hmPieceList.get(j);
//
//            Object[] rowData = new Object[72];
//
//            // JOptionPane.showMessageDialog(null  , "Piece : "+piece.getAttribute("PR_PIECE_NO").getString());
//            rowData[0] = (String) piece.getAttribute("PR_PIECE_NO").getObj();
//            rowData[1] = (String) piece.getAttribute("PR_ORDER_DATE").getObj();
//            rowData[2] = (String) piece.getAttribute("PR_DOC_NO").getObj();
//            rowData[3] = (String) piece.getAttribute("PR_MACHINE_NO").getObj();
//            rowData[4] = (String) piece.getAttribute("PR_POSITION_NO").getObj();
//
//            
//            rowData[5] = "";
//
//            try {
//                String strSQL = "";
//                ResultSet rsTmp;
//                strSQL = "";
//                strSQL += "SELECT MM_MACHINE_POSITION_DESC from PRODUCTION.FELT_MACHINE_MASTER_DETAIL where MM_MACHINE_NO='" + (String) piece.getAttribute("PR_MACHINE_NO").getObj() + "' and MM_MACHINE_POSITION = '" + (String) piece.getAttribute("PR_POSITION_NO").getObj() + "'";
//                rsTmp = data.getResult(strSQL);
//                rsTmp.first();
//
//                rowData[5] = rsTmp.getString("MM_MACHINE_POSITION_DESC");
//            } catch (Exception ew) {
//                System.out.println("Error on getting data");
//            }
//
//            rowData[6] = (String) piece.getAttribute("PR_PARTY_CODE").getObj();
//
//            rowData[7] = "";
//            try {
//                String strSQL = "";
//                ResultSet rsTmp;
//                strSQL = "";
//                strSQL += "SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE = " + (String) piece.getAttribute("PR_PARTY_CODE").getObj() + "";
//                rsTmp = data.getResult(strSQL);
//                rsTmp.first();
//
//                rowData[7] = rsTmp.getString("PARTY_NAME");
//            } catch (Exception ew) {
//                System.out.println("Error on getting data");
//            }
//
//            rowData[8] = (String) piece.getAttribute("PR_PRODUCT_CODE").getObj();
//            rowData[9] = "";
//                    //FeltRateMasterServiceImpl frms = new FeltRateMasterServiceImpl();
//            //System.out.println("Data :::: Rate Master = "+Piecedetail[3]);
//            //FeltRateMaster rate_master = frms.getDetailByItemCode(mmDetail.getMmItemCode());
//            try {
//                String strSQL = "";
//                ResultSet rsTmp;
//                strSQL = "";
//                strSQL += "SELECT ITEM_CODE,ITEM_DESC FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = " + (String) piece.getAttribute("PR_PRODUCT_CODE").getObj() + "";
//                rsTmp = data.getResult(strSQL);
//                rsTmp.first();
//
//                rowData[9] = rsTmp.getString("ITEM_DESC");
//            } catch (Exception ew) {
//                System.out.println("Error on getting data");
//            }
//
//            rowData[10] = (String) piece.getAttribute("PR_GROUP").getObj();
//            rowData[11] = (String) piece.getAttribute("PR_STYLE").getObj();
//            rowData[12] = (String) piece.getAttribute("PR_LENGTH").getObj();
//            rowData[13] = (String) piece.getAttribute("PR_WIDTH").getObj();
//            rowData[14] = (String) piece.getAttribute("PR_GSM").getObj();
//            rowData[15] = (String) piece.getAttribute("PR_THORITICAL_WEIGHT").getObj();
//            rowData[16] = (String) piece.getAttribute("PR_SQMTR").getObj();
//            rowData[17] = (String) piece.getAttribute("PR_SYN_PER").getObj();
//            rowData[18] = (String) piece.getAttribute("PR_REQUESTED_MONTH").getObj();;
//            rowData[19] = (String) piece.getAttribute("PR_REGION").getObj();
//            rowData[20] = (String) piece.getAttribute("PR_INCHARGE").getObj();
//            rowData[21] = (String) piece.getAttribute("PR_REFERENCE").getObj();
//            rowData[22] = (String) piece.getAttribute("PR_REFERENCE_DATE").getObj();
//            rowData[23] = (String) piece.getAttribute("PR_PO_NO").getObj();
//            rowData[24] = (String) piece.getAttribute("PR_PO_DATE").getObj();
//            rowData[25] = (String) piece.getAttribute("PR_ORDER_REMARK").getObj();
//            rowData[26] = (String) piece.getAttribute("PR_PIECE_REMARK").getObj();
//
//            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
//            FeltInvCalc inv_calc;
//            try {
//                inv_calc = clsOrderValue.calculate(
//                        (String) piece.getAttribute("PR_PIECE_NO").getObj(),
//                        (String) piece.getAttribute("PR_PRODUCT_CODE").getObj(),
//                        (String) piece.getAttribute("PR_PARTY_CODE").getObj(),
//                        Float.parseFloat((String) piece.getAttribute("PR_LENGTH").getObj()),
//                        Float.parseFloat((String) piece.getAttribute("PR_WIDTH").getObj()),
//                        Math.round(Float.parseFloat((String) piece.getAttribute("PR_GSM").getObj())),
//                        Float.parseFloat((String) piece.getAttribute("PR_THORITICAL_WEIGHT").getObj()),
//                        Float.parseFloat((String) piece.getAttribute("PR_SQMTR").getObj()),
//                        (String) piece.getAttribute("PR_ORDER_DATE").getObj());
//
//                rowData[27] = inv_calc.getFicRate();
//                rowData[28] = inv_calc.getFicBasAmount();
//                rowData[29] = inv_calc.getFicChemTrtChg();
//                rowData[30] = inv_calc.getFicSpiralChg();
//                rowData[31] = inv_calc.getFicPinChg();
//                rowData[32] = inv_calc.getFicSeamChg();
//                rowData[33] = inv_calc.getFicInsInd();
//                rowData[34] = inv_calc.getFicInsAmt();
//                rowData[35] = inv_calc.getFicExcise();
//                rowData[36] = inv_calc.getFicDiscPer();
//                rowData[37] = inv_calc.getFicDiscAmt();
//                rowData[38] = inv_calc.getFicDiscBasamt();
//                rowData[39] = inv_calc.getFicInvAmt();
//            } catch (Exception e) {
//                System.out.println("Error on PIECE REGISTER : " + e.getMessage());
//            }
//            rowData[40] = (String) piece.getAttribute("PR_PIECE_STAGE").getObj();
//
//            DataModel.addRow(rowData);
//        }
//
//    }
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
            } else if (EditMode == EITLERPGLOBAL.EDIT) {
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, PIECE_AMEND_STOCK_NO.getText());
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
        }

    }

    public void from_MachineMaster(String Party_Code, String MachineNo, String Position) {
        PARTY_CODE.setText(Party_Code);
        MACHINE_NO.setText(MachineNo);
        POSITION.setText(Position);

        SHOW_LIST.doClick();
        if (Table.getRowCount() != 0) {
            Save();
        }
    }
}
