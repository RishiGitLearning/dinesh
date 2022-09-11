/*
 */
package EITLERP.FeltSales.TrailPiece;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.FeltSales.PieceRegister.clsIncharge;
import EITLERP.FeltSales.common.MailNotification;
import EITLERP.FeltSales.common.SelectSortFields;
import javax.swing.table.*;
import java.text.*;
import javax.swing.text.*;
import java.sql.*;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Production.ReportUI.JTextFieldHint;
import java.awt.Color;

import javax.swing.JFrame;

//import EITLERP.Purchase.frmSendMail;
public class frmTrailPieceDispatchEntry extends javax.swing.JApplet {

    private int EditMode = 0;
    private clsTrailPieceDispatchEntry Obj;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel, cmbFromModel, cmbToModel, cmbIncharge, cmodelProductGroup;
    private EITLTableModel DataModelDesc, DataModelDesc1, DataModelA, DataModelH, DataModelHS;

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    //clsColumn ObjColumn=new clsColumn();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    String ORDER_BY = " ORDER BY PR_PIECE_NO ";

    private boolean HistoryView = false;
    private String theDocNo = "";
    public frmPendingApprovals frmPA;
    private int charge09index = 0;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();
    String cellLastValue = "";
    public boolean PENDING_DOCUMENT = false;
    private DecimalFormat df;
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

//    private  static ObservableList<PieChart.Data> details = FXCollections.observableArrayList();
//    private static PieChart pieChart;
    /**
     * Creates new form frmSalesParty
     */
    public frmTrailPieceDispatchEntry() {
        //this.requestFocus();

        System.gc();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        if (scrwidth > 800) {
            scrwidth = 800;
        }

        setSize(scrwidth, scrheight - 50);
        cmbIncharge = new EITLComboModel();
        cmodelProductGroup = new EITLComboModel();
        cmbFromModel = new EITLComboModel();
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
        cmdPreview.setEnabled(false);
        cmdPrint.setEnabled(false);
        cmdEmail.setEnabled(false);
        //cmdPreview.setIcon(EITLERPGLOBAL.getImage("PREVIEW"));
        //cmdPrint.setIcon(EITLERPGLOBAL.getImage("PRINT"));
        cmdExit.setIcon(EITLERPGLOBAL.getImage("EXIT"));
        //cmdEmail.setIcon(EITLERPGLOBAL.getImage("EMAIL"));

        df = new DecimalFormat("0.00");
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        //JOptionPane.showMessageDialog(null, df.format(10));

        DecimalFormat decimalFormat = new DecimalFormat("0");
        NumberFormatter ObjFormater = new NumberFormatter(decimalFormat);

        ObjFormater.setAllowsInvalid(false);
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        NumberFormatter ObjFormater1 = new NumberFormatter(decimalFormat1);

        ObjFormater1.setAllowsInvalid(false);

        GenerateCombo();
        GenerateGroupCombo();
        GenerateYearCombo();
        int CurFinYear = EITLERPGLOBAL.getCurrentFinYear();
        EITLERPGLOBAL.setComboIndex(cmbFromYear, CurFinYear);

        GenerateCombos();

        Obj = new clsTrailPieceDispatchEntry();

        SetMenuForRights();

        if (Obj.LoadData(EITLERPGLOBAL.gCompanyID)) {
            Obj.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data.   Error is " + Obj.LastError);
        }

        txtAuditRemarks.setVisible(false);
        DataModelDesc.TableReadOnly(true);

        cmdPreview.setEnabled(true);
        SetFields(false);

        // txtbeam.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsTrailPieceDispatchEntry.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsTrailPieceDispatchEntry.ModuleID + "");
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


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
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
        cmdEmail = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableDesc = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        ApprovalPanel = new javax.swing.JPanel();
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
        cmdNext3 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        StatusPanel = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableHS = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        DOC_NO = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyCode = new JTextFieldHint(new JTextField(),"Search by F1");
        jLabel5 = new javax.swing.JLabel();
        txtUPN = new javax.swing.JTextField();
        txtUPN = new JTextFieldHint(new JTextField(),"Search by F1");
        jLabel9 = new javax.swing.JLabel();
        txtPieceNo = new javax.swing.JTextField();
        txtPieceNo = new JTextFieldHint(new JTextField(),"Search by F1");
        jLabel7 = new javax.swing.JLabel();
        cmbZone = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cmbProdGroup = new javax.swing.JComboBox();
        lblFromYear = new javax.swing.JLabel();
        lblFromYear1 = new javax.swing.JLabel();
        cmbFromYear = new javax.swing.JComboBox();
        lblToYear = new javax.swing.JLabel();
        txtToYear = new javax.swing.JTextField();
        btnSorting = new javax.swing.JButton();
        btnShowData = new javax.swing.JButton();
        lblPartyName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        DOC_DATE = new javax.swing.JTextField();

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

        cmdBack.setToolTipText(" Previous Record");
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

        cmdSave.setToolTipText("Save Record");
        cmdSave.setEnabled(false);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        ToolBar.add(cmdSave);

        cmdCancel.setToolTipText("Cancel Record");
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

        cmdEmail.setToolTipText("Email");
        cmdEmail.setEnabled(false);
        cmdEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEmailActionPerformed(evt);
            }
        });
        ToolBar.add(cmdEmail);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 730, 40);

        lblTitle.setBackground(new java.awt.Color(211, 221, 225));
        lblTitle.setText("Felt Trial Piece Post Dispatch Detail Entry");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 730, 25);

        Tab1.setLayout(null);

        TableDesc.setModel(new javax.swing.table.DefaultTableModel(
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
        TableDesc.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TableDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableDescKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(TableDesc);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 740, 290);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(640, 300, 90, 23);

        jTabbedPane1.addTab("Trial/Sample Piece[s] Post Dispatch Detail Entry", Tab1);

        ApprovalPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ApprovalPanel.setToolTipText("");
        ApprovalPanel.setLayout(null);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        ApprovalPanel.add(jLabel31);
        jLabel31.setBounds(5, 13, 100, 14);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        ApprovalPanel.add(cmbHierarchy);
        cmbHierarchy.setBounds(110, 13, 270, 20);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        ApprovalPanel.add(jLabel32);
        jLabel32.setBounds(5, 43, 100, 14);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        ApprovalPanel.add(txtFrom);
        txtFrom.setBounds(110, 43, 270, 22);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        ApprovalPanel.add(jLabel35);
        jLabel35.setBounds(5, 76, 100, 14);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        ApprovalPanel.add(txtFromRemarks);
        txtFromRemarks.setBounds(110, 73, 518, 22);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        ApprovalPanel.add(jLabel36);
        jLabel36.setBounds(5, 116, 100, 14);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        buttonGroup1.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 169, 23);

        buttonGroup1.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
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
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        ApprovalPanel.add(jPanel6);
        jPanel6.setBounds(110, 113, 182, 100);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        ApprovalPanel.add(jLabel33);
        jLabel33.setBounds(5, 226, 100, 14);

        cmbSendTo.setEnabled(false);
        ApprovalPanel.add(cmbSendTo);
        cmbSendTo.setBounds(110, 223, 270, 20);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        ApprovalPanel.add(jLabel34);
        jLabel34.setBounds(5, 262, 100, 14);

        txtToRemarks.setEnabled(false);
        ApprovalPanel.add(txtToRemarks);
        txtToRemarks.setBounds(110, 259, 516, 22);

        cmdNext3.setText("<<Previous");
        cmdNext3.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdNext3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(cmdNext3);
        cmdNext3.setBounds(550, 300, 75, 23);

        jButton3.setText("Next >>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(jButton3);
        jButton3.setBounds(650, 300, 100, 23);

        jTabbedPane1.addTab("Approval", null, ApprovalPanel, "");

        StatusPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        StatusPanel.setLayout(null);

        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 14);

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

        StatusPanel.add(jScrollPane2);
        jScrollPane2.setBounds(12, 40, 694, 120);

        jLabel19.setText("Document Update History");
        StatusPanel.add(jLabel19);
        jLabel19.setBounds(10, 170, 182, 14);

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
        jScrollPane6.setViewportView(TableHS);

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
        txtAuditRemarks.setBounds(570, 260, 129, 20);

        jButton4.setText("Next >>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton4);
        jButton4.setBounds(670, 290, 75, 23);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(570, 290, 90, 23);

        jTabbedPane1.addTab("Status", StatusPanel);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(5, 270, 750, 380);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(10, 650, 750, 40);

        jLabel1.setText("Document No");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 75, 100, 20);

        DOC_NO.setEditable(false);
        DOC_NO.setForeground(new java.awt.Color(0, 0, 255));
        DOC_NO.setText("D000001");
        DOC_NO.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        getContentPane().add(DOC_NO);
        DOC_NO.setBounds(100, 70, 130, 30);

        jLabel8.setText("PARTY CODE");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(0, 120, 100, 14);

        txtPartyCode.setToolTipText("Press F1 ");
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
        getContentPane().add(txtPartyCode);
        txtPartyCode.setBounds(100, 110, 120, 30);

        jLabel5.setText("UPN");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(240, 110, 40, 30);

        txtUPN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUPNFocusLost(evt);
            }
        });
        txtUPN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUPNKeyPressed(evt);
            }
        });
        getContentPane().add(txtUPN);
        txtUPN.setBounds(290, 110, 120, 30);

        jLabel9.setText("Piece No.");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(420, 120, 90, 14);

        txtPieceNo.setToolTipText("Press F1 ");
        txtPieceNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPieceNoFocusLost(evt);
            }
        });
        txtPieceNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPieceNoKeyPressed(evt);
            }
        });
        getContentPane().add(txtPieceNo);
        txtPieceNo.setBounds(510, 110, 120, 30);

        jLabel7.setText("Zone");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(0, 180, 50, 30);

        cmbZone.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbZone);
        cmbZone.setBounds(100, 180, 110, 30);

        jLabel6.setText("Product Group");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(220, 180, 120, 30);

        getContentPane().add(cmbProdGroup);
        cmbProdGroup.setBounds(320, 180, 110, 30);

        lblFromYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromYear.setText("Financial Year");
        getContentPane().add(lblFromYear);
        lblFromYear.setBounds(0, 230, 80, 20);

        lblFromYear1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromYear1.setText("From :");
        getContentPane().add(lblFromYear1);
        lblFromYear1.setBounds(80, 230, 50, 20);

        cmbFromYear.setToolTipText("");
        cmbFromYear.setOpaque(false);
        cmbFromYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFromYearItemStateChanged(evt);
            }
        });
        getContentPane().add(cmbFromYear);
        cmbFromYear.setBounds(140, 230, 102, 30);

        lblToYear.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        lblToYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToYear.setText("To :");
        getContentPane().add(lblToYear);
        lblToYear.setBounds(250, 240, 40, 15);

        txtToYear.setEditable(false);
        txtToYear.setOpaque(false);
        getContentPane().add(txtToYear);
        txtToYear.setBounds(300, 230, 102, 30);

        btnSorting.setText("SORTING");
        btnSorting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortingActionPerformed(evt);
            }
        });
        getContentPane().add(btnSorting);
        btnSorting.setBounds(480, 210, 130, 30);

        btnShowData.setText("SHOW DATA");
        btnShowData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowDataActionPerformed(evt);
            }
        });
        getContentPane().add(btnShowData);
        btnShowData.setBounds(480, 240, 130, 30);
        getContentPane().add(lblPartyName);
        lblPartyName.setBounds(100, 140, 410, 30);

        jLabel2.setText("Document Date");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(240, 80, 100, 20);

        DOC_DATE.setEditable(false);
        DOC_DATE.setForeground(new java.awt.Color(0, 0, 255));
        DOC_DATE.setText("D000001");
        DOC_DATE.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        getContentPane().add(DOC_DATE);
        DOC_DATE.setBounds(340, 70, 150, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEmailActionPerformed

    }//GEN-LAST:event_cmdEmailActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        PreviewReport();
    }//GEN-LAST:event_cmdPrintActionPerformed

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
        Delete();
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
        Obj.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if (TableHS.getRowCount() > 0 && TableHS.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableHS.getValueAt(TableHS.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        Obj.LoadData(EITLERPGLOBAL.gCompanyID);
        //MoveFirst();
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo = DOC_NO.getText();
        Obj.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cmdNext3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_cmdNext3ActionPerformed

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
        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        // TODO add your handling code here:
        String DocNo = DOC_NO.getText();

        SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsTrailPieceDispatchEntry.ModuleID, DocNo)) {
                cmbSendTo.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }

        if (cmbSendTo.getItemCount() <= 0) {
            GenerateFromCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
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
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        if (txtPartyCode.getText().equals("")) {
            lblPartyName.setText("");
        } else if (data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE  COALESCE(PR_PIECETRIAL_FLAG,0)=1 AND PR_PARTY_CODE='" + txtPartyCode.getText() + "'").equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Invalid Party Code", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtPartyCode.setText("");
            lblPartyName.setText("");
            txtPartyCode.requestFocus();
        } else {
            lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText()));
        }
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT DISTINCT PR_PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010') AS D "
                    + "ON PR_PARTY_CODE=PARTY_CODE "
                    + "WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1";

            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);
                lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void txtUPNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUPNFocusLost
        // TODO add your handling code here:
        String SQL = "";
        SQL = "SELECT PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1 AND PR_UPN='" + txtUPN.getText() + "'";
        if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
            SQL = SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
        }
        if (!cmbZone.getSelectedItem().equals("ALL")) {

            SQL = SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
        }
        SQL = SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

        if (txtUPN.getText().equalsIgnoreCase("")) {

        } else if (data.getStringValueFromDB(SQL).equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Invalid UPN", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtUPN.setText("");
            txtUPN.requestFocus();
        }
    }//GEN-LAST:event_txtUPNFocusLost

    private void txtUPNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUPNKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT DISTINCT PR_UPN,PR_PARTY_CODE,PR_MACHINE_NO,POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST "
                    + "ON PR_POSITION_NO=POSITION_NO "
                    + "WHERE PR_PIECETRIAL_FLAG=1 AND PR_PIECE_STAGE IN ('IN STOCK','INVOICED','EXP-INVOICE','NEEDLING','MENDING','SEAMING','FINISHING','PLANNING','WEAVING','BSR','BOOKING') ";
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                aList.SQL = aList.SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {

                aList.SQL = aList.SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }
            aList.SQL = aList.SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

            if (txtPartyCode.getText().trim().length() >= 6) {
                aList.SQL = aList.SQL + "AND PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtUPN.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtUPNKeyPressed

    private void txtPieceNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPieceNoFocusLost
        // TODO add your handling code here:
        String SQL = "";
        SQL = "SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1 AND PR_PIECE_NO='" + txtPieceNo.getText() + "'";
        if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
            SQL = SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
        }
        if (!cmbZone.getSelectedItem().equals("ALL")) {

            SQL = SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
        }
        SQL = SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

        if (txtPieceNo.getText().equalsIgnoreCase("")) {

        } else if (data.getStringValueFromDB(SQL).equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Invalid Piece No.", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtPieceNo.setText("");
            txtPieceNo.requestFocus();
        }
    }//GEN-LAST:event_txtPieceNoFocusLost

    private void txtPieceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPieceNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT DISTINCT PR_PIECE_NO,PR_PIECE_STAGE,PR_WIP_STATUS,PR_UPN,PR_PARTY_CODE,PR_MACHINE_NO,POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST "
                    + "ON PR_POSITION_NO=POSITION_NO "
                    + "WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1 AND PR_PIECE_STAGE IN ('IN STOCK','INVOICED','EXP-INVOICE','NEEDLING','MENDING','SEAMING','FINISHING','PLANNING','WEAVING','BSR','BOOKING') ";
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                aList.SQL = aList.SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {

                aList.SQL = aList.SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }
            aList.SQL = aList.SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

            if (txtPartyCode.getText().trim().length() >= 6) {
                aList.SQL = aList.SQL + "AND PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }
            if (txtUPN.getText().trim().length() >= 6) {
                aList.SQL = aList.SQL + "AND PR_UPN='" + txtUPN.getText() + "'";
            }

            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtPieceNo.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtPieceNoKeyPressed

    private void cmbFromYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFromYearItemStateChanged
        // TODO add your handling code here:
        try {
            int ToYear = Integer.parseInt((String) cmbFromYear.getSelectedItem()) + 1;
            txtToYear.setText(Integer.toString(ToYear));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbFromYearItemStateChanged

    private void btnSortingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortingActionPerformed
        // TODO add your handling code here:
        sort_query_creator();
        btnShowDataActionPerformed(null);
    }//GEN-LAST:event_btnSortingActionPerformed

    private void btnShowDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowDataActionPerformed
        // TODO add your handling code here:
        try {
            FormatGrid();
            String SQL;
            SQL = "SELECT PR_PIECE_NO,PR_PIECE_STAGE,PR_WIP_STATUS,PR_PARTY_CODE,PARTY_NAME,PR_DOC_NO,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(PR_ORDER_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE PR_ORDER_DATE END,'%d/%m/%Y'),'') AS ORDER_DATE,"
                    + "PR_UPN,PR_PRODUCT_CODE,PR_GROUP,PR_MACHINE_NO,POSITION_DESC,PR_STYLE,"
                    + "PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,INCHARGE_NAME AS ZONE,"
                    + "COALESCE(PR_INVOICE_NO,'') AS INVOICE_NO,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE PR_INVOICE_DATE END,'%d/%m/%Y'),'') AS INVOICE_DATE,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(MOUNTING_PLAN_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE MOUNTING_PLAN_DATE END,'%d/%m/%Y'),'') AS MOUNTING_PLAN_DATE,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(MOUNTING_ACTUAL_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE MOUNTING_ACTUAL_DATE END,'%d/%m/%Y'),'') AS MOUNTING_ACTUAL_DATE,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(DEMOUNTING_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE DEMOUNTING_DATE END,'%d/%m/%Y'),'') AS DEMOUNTING_DATE,"
                    + "COALESCE(EXPECTED_LIFE_DAYS,'') AS EXPECTED_LIFE_DAYS,"
                    + "COALESCE(ACTUAL_LIFE_DAYS,'') AS ACTUAL_LIFE_DAYS,"
                    + "COALESCE(PERFORMANCE_FEEDBACK,'') AS PERFORMANCE_FEEDBACK,"
                    + "COALESCE(REMARKS,'') AS REMARKS  "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "LEFT JOIN PRODUCTION.FELT_INCHARGE "
                    + "ON PR_INCHARGE=INCHARGE_CD "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST ON PR_POSITION_NO=POSITION_NO "
                    + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,PARTY_LOCK,PARTY_MILL_CLOSED_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS PM "
                    + "ON PR_PARTY_CODE=PARTY_CODE "
                    + "LEFT JOIN (SELECT * FROM (SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH "
                    + "WHERE APPROVED=1 AND CANCELED=0 ORDER BY DOC_DATE DESC) AS DDD "
                    + "GROUP BY FT_PIECE_NO) AS FTPD  "
                    + "ON PR_PIECE_NO=FT_PIECE_NO "
                    + "WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1  ";
            if (txtPartyCode.getText().trim().length() >= 6) {
                SQL = SQL + " AND PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }
            if (txtUPN.getText().trim().length() >= 6) {
                SQL = SQL + " AND PR_UPN='" + txtUPN.getText() + "'";
            }
            if (txtPieceNo.getText().trim().length() >= 5) {
                SQL = SQL + " AND PR_PIECE_NO='" + txtPieceNo.getText() + "'";
            }
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                SQL = SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {

                SQL = SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }
            SQL = SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

            SQL = SQL + " " + ORDER_BY;
            System.out.println("SQL:" + SQL);
            ResultSet tdata = data.getResult(SQL);
            tdata.first();
            if (tdata.getRow() > 0) {
                int srNo = 0;
                while (!tdata.isAfterLast()) {
                    srNo++;
                    Object[] rowData = new Object[100];
                    rowData[0] = srNo;
                    rowData[1] = tdata.getString("PR_PARTY_CODE");
                    rowData[2] = tdata.getString("PARTY_NAME");
                    rowData[3] = tdata.getString("PR_UPN");
                    rowData[4] = tdata.getString("PR_PIECE_NO");
                    rowData[5] = tdata.getString("MOUNTING_PLAN_DATE");
                    rowData[6] = tdata.getString("MOUNTING_ACTUAL_DATE");
                    rowData[7] = tdata.getString("EXPECTED_LIFE_DAYS");
                    rowData[8] = tdata.getString("DEMOUNTING_DATE");
                    rowData[9] = tdata.getString("ACTUAL_LIFE_DAYS");
                    rowData[10] = tdata.getString("PERFORMANCE_FEEDBACK");
                    rowData[11] = tdata.getString("REMARKS");
                    rowData[12] = tdata.getString("INVOICE_NO");
                    rowData[13] = tdata.getString("INVOICE_DATE");
                    rowData[14] = tdata.getString("PR_DOC_NO");
                    rowData[15] = tdata.getString("ORDER_DATE");
                    rowData[16] = tdata.getString("PR_PIECE_STAGE");
                    rowData[17] = tdata.getString("PR_WIP_STATUS");
                    rowData[18] = tdata.getString("PR_PRODUCT_CODE").trim();
                    rowData[19] = tdata.getString("PR_GROUP");
                    rowData[20] = tdata.getString("ZONE");
                    rowData[21] = tdata.getString("PR_MACHINE_NO");
                    rowData[22] = tdata.getString("POSITION_DESC");
                    rowData[23] = df.format(Double.parseDouble(tdata.getString("PR_LENGTH")));
                    rowData[24] = df.format(Double.parseDouble(tdata.getString("PR_WIDTH")));
                    rowData[25] = tdata.getString("PR_GSM");
                    rowData[26] = df.format(Double.parseDouble(tdata.getString("PR_THORITICAL_WEIGHT")));
                    DataModelDesc.addRow(rowData);
                    tdata.next();
                }
                final TableColumnModel columnModel = TableDesc.getColumnModel();
                for (int column = 0; column < TableDesc.getColumnCount(); column++) {
                    int width = 70; // Min width
                    for (int row = 0; row < TableDesc.getRowCount(); row++) {
                        TableCellRenderer renderer = TableDesc.getCellRenderer(row, column);
                        Component comp = TableDesc.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 1, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    if (column > 4 && column < 11) {
                        columnModel.getColumn(column).setPreferredWidth(130);
                    } else {
                        columnModel.getColumn(column).setPreferredWidth(width);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No Data Found...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnShowDataActionPerformed

    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased
        // TODO add your handling code here:

        if (!TableDesc.getValueAt(TableDesc.getSelectedRow(), 8).toString().equalsIgnoreCase("")) {
            try {
                int adays = data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(TableDesc.getValueAt(TableDesc.getSelectedRow(), 8).toString()) + "','" + EITLERPGLOBAL.formatDateDB(TableDesc.getValueAt(TableDesc.getSelectedRow(), 6).toString()) + "') FROM DUAL");
                TableDesc.setValueAt(adays, TableDesc.getSelectedRow(), 9);
            } catch (Exception e) {
                TableDesc.setValueAt(0, TableDesc.getSelectedRow(), 9);
            }
        }
    }//GEN-LAST:event_TableDescKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApprovalPanel;
    private javax.swing.JTextField DOC_DATE;
    private javax.swing.JTextField DOC_NO;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JPanel Tab1;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableDesc;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnShowData;
    private javax.swing.JButton btnSorting;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbFromYear;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbProdGroup;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JComboBox cmbZone;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdEmail;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblFromYear;
    private javax.swing.JLabel lblFromYear1;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblToYear;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPieceNo;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtToYear;
    private javax.swing.JTextField txtUPN;
    // End of variables declaration//GEN-END:variables

    private void Add() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//        
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 801;

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
            System.out.println("FFNO:" + FFNo);
            DOC_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 801, FFNo, false));
            DOC_DATE.setText(EITLERPGLOBAL.getCurrentDate());

            //lblTitle.setBackground(Color.BLUE);
        } else {
            JOptionPane.showMessageDialog(null, "You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }

    }

    private void SetupApproval() {

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
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsTrailPieceDispatchEntry.ModuleID, (String) Obj.getAttribute("PROFORMA_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsTrailPieceDispatchEntry.ModuleID, FromUserID, (String) Obj.getAttribute("PROFORMA_NO").getObj());

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

        if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
            OpgReject.setEnabled(false);
        }
        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
            //JOptionPane.showMessageDialog(null, "Final Approver");
            OpgApprove.setEnabled(false);
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

                String DocNo = (String) Obj.getAttribute("DOC_NO").getObj();

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsTrailPieceDispatchEntry.ModuleID, DocNo);
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

    private void SetFields(boolean pStat) {

        txtPartyCode.setEnabled(pStat);
        txtUPN.setEnabled(pStat);
        txtPieceNo.setEnabled(pStat);
        cmbZone.setEnabled(pStat);
        cmbProdGroup.setEnabled(pStat);
        cmbFromYear.setEnabled(pStat);
        btnSorting.setEnabled(pStat);
        btnShowData.setEnabled(pStat);
        TableDesc.setEnabled(pStat);

        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);

        txtToRemarks.setEnabled(pStat);

        SetupApproval();
    }

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
        cmdEmail.setEnabled(true);
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
        cmdEmail.setEnabled(false);
    }

    private boolean Validate() {
        int ValidEntryCount = 0;

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Please select the hierarchy.");
            return false;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null, "Please select the Approval Action");
            return false;
        }

        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the remarks for rejection");
            return false;
        }

        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please select the user, to whom rejected document to be send");
            return false;
        }

        if (OpgFinal.isSelected()) {
        }
        return true;
    }

    private void ClearFields() {

        txtPartyCode.setText("");
        lblPartyName.setText("");
        txtUPN.setText("");
        txtPieceNo.setText("");
        cmbZone.setSelectedIndex(0);
        cmbProdGroup.setSelectedIndex(0);
        int CurFinYear = EITLERPGLOBAL.getCurrentFinYear();
        EITLERPGLOBAL.setComboIndex(cmbFromYear, CurFinYear);

        txtFromRemarks.setText("");
        txtToRemarks.setText("");

        FormatGrid();
        FormatGridA();
        FormatGridHS();
    }

    private void Edit() {

        //----------------------------------//
        String lDocNo = Obj.getAttribute("DOC_NO").getString();
        if (Obj.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (clsFeltProductionApprovalFlow.IsCreator(clsTrailPieceDispatchEntry.ModuleID, lDocNo)) {  // || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6070, 60702)) {
                SetFields(true);
            } else {
                EnableApproval();
            }

            //DisplayData();
            DisableToolbar();
        } else {
            JOptionPane.showMessageDialog(null, "You cannot edit this record.   It is either approved/rejected or waiting approval for other user");
        }
    }

    private void Delete() {

        String lDocNo = (String) Obj.getAttribute("DOC_NO").getObj();

        if (Obj.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            if (Obj.Delete(EITLERPGLOBAL.gNewUserID)) {
                MoveLast();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while deleting.  Error is " + Obj.LastError);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
        }
    }

    private void Save() {
        //Form level validations

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Please select the hierarchy.");
            return;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null, "Please select the Approval Action");
            return;
        }

        if (TableDesc.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter some items.");
            return;
        }

        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the remarks for rejection");
            return;
        }

        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please select the user, to whom rejected document to be send");
            return;
        }

        int mcntpc = 0;
        for (int k = 0; k <= TableDesc.getRowCount() - 1; k++) {
            mcntpc++;
        }

        if (mcntpc == 0) {
            JOptionPane.showMessageDialog(null, "Please Select Piece/UPN...");
            return;
        }
        SetData();
        if (EditMode == EITLERPGLOBAL.ADD) {

            if (Obj.Insert()) {
                if (OpgFinal.isSelected()) {
                    Mail();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving.  Error is " + Obj.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (Obj.Update()) {
                if (OpgFinal.isSelected()) {
                    Mail();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving.  Error is " + Obj.LastError);
                return;
            }
        }
        Obj.Filter(" DOC_NO='" + DOC_NO.getText() + "'");
        DisplayData();

        Obj.LoadData(EITLERPGLOBAL.gCompanyID);
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        try {
            frmPA.RefreshView();
        } catch (Exception e) {
        }

    }

    private void Cancel() {
        EditMode = 0;
        DisplayData();
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
    }

    private void Find() {
//        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.FeltWarpingBeamOrder.frmFindWarpingBeamOrderPiece", true);
//        frmFindWarpingBeamOrderPiece ObjReturn = (frmFindWarpingBeamOrderPiece) ObjLoader.getObj();
//
//        if (ObjReturn.Cancelled == false) {
//            if (!Obj.Filter(ObjReturn.stringFindQuery)) {
//                JOptionPane.showMessageDialog(null, "No records found.");
//            }
//            MoveFirst();
//        }
    }

    private void MoveFirst() {
        Obj.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        Obj.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        Obj.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        Obj.MoveLast();
        DisplayData();
    }

    //Didplay data on the Screen
    private void DisplayData() {
        //=========== Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (Obj.getAttribute("APPROVED").getInt() == 1) {

                    lblTitle.setBackground(Color.BLUE);
                    cmdEmail.setEnabled(true);
                    cmdPreview.setEnabled(true);
                }

                if (Obj.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                    cmdEmail.setEnabled(false);
                    cmdPreview.setEnabled(false);
                }

                if (Obj.getAttribute("CANCELED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                    cmdEmail.setEnabled(false);
                    cmdPreview.setEnabled(false);
                }
            }
            lblTitle.setForeground(Color.WHITE);
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = clsTrailPieceDispatchEntry.ModuleID;

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        try {
            ClearFields();
            //lblTitle.setText("FELT PROFORMA INVOICE - " + (String) Obj.getAttribute("PROFORMA_NO").getObj());
            DOC_NO.setText(Obj.getAttribute("DOC_NO").getObj().toString());
            DOC_DATE.setText(EITLERPGLOBAL.formatDate(Obj.getAttribute("DOC_DATE").getObj().toString()));
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, Obj.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table

            int srNo = 0;
            String sql = "SELECT PR_PIECE_NO,PR_PIECE_STAGE,PR_WIP_STATUS,PR_PARTY_CODE,PARTY_NAME,PR_DOC_NO,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(PR_ORDER_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE PR_ORDER_DATE END,'%d/%m/%Y'),'') AS ORDER_DATE,"
                    + "PR_UPN,PR_PRODUCT_CODE,PR_GROUP,PR_MACHINE_NO,POSITION_DESC,PR_STYLE,"
                    + "PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,INCHARGE_NAME AS ZONE,"
                    + "COALESCE(PR_INVOICE_NO,'') AS INVOICE_NO,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE PR_INVOICE_DATE END,'%d/%m/%Y'),'') AS INVOICE_DATE,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(MOUNTING_PLAN_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE MOUNTING_PLAN_DATE END,'%d/%m/%Y'),'') AS MOUNTING_PLAN_DATE,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(MOUNTING_ACTUAL_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE MOUNTING_ACTUAL_DATE END,'%d/%m/%Y'),'') AS MOUNTING_ACTUAL_DATE,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(DEMOUNTING_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE DEMOUNTING_DATE END,'%d/%m/%Y'),'') AS DEMOUNTING_DATE,"
                    + "COALESCE(EXPECTED_LIFE_DAYS,'') AS EXPECTED_LIFE_DAYS,"
                    + "COALESCE(ACTUAL_LIFE_DAYS,'') AS ACTUAL_LIFE_DAYS,"
                    + "COALESCE(PERFORMANCE_FEEDBACK,'') AS PERFORMANCE_FEEDBACK,"
                    + "COALESCE(REMARKS,'') AS REMARKS  "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "LEFT JOIN PRODUCTION.FELT_INCHARGE "
                    + "ON PR_INCHARGE=INCHARGE_CD "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST ON PR_POSITION_NO=POSITION_NO "
                    + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,PARTY_LOCK,PARTY_MILL_CLOSED_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS PM "
                    + "ON PR_PARTY_CODE=PARTY_CODE "
                    + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE DOC_NO='" + DOC_NO.getText() + "') AS D "
                    + "ON PR_PIECE_NO=FT_PIECE_NO "
                    + "WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1 "
                    + "AND PR_PIECE_NO IN ("
                    + "SELECT FT_PIECE_NO FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE DOC_NO='" + DOC_NO.getText() + "' )";
            System.out.println("Display Data :" + sql);
            ResultSet disdata = data.getResult(sql);
            disdata.first();
            if (disdata.getRow() > 0) {
                while (!disdata.isAfterLast()) {
                    srNo++;
                    Object[] rowData = new Object[100];
                    rowData[0] = srNo;
                    rowData[1] = disdata.getString("PR_PARTY_CODE");
                    rowData[2] = disdata.getString("PARTY_NAME");
                    rowData[3] = disdata.getString("PR_UPN");
                    rowData[4] = disdata.getString("PR_PIECE_NO");
                    rowData[5] = disdata.getString("MOUNTING_PLAN_DATE");
                    rowData[6] = disdata.getString("MOUNTING_ACTUAL_DATE");
                    rowData[7] = disdata.getString("EXPECTED_LIFE_DAYS");
                    rowData[8] = disdata.getString("DEMOUNTING_DATE");
                    rowData[9] = disdata.getString("ACTUAL_LIFE_DAYS");
                    rowData[10] = disdata.getString("PERFORMANCE_FEEDBACK");
                    rowData[11] = disdata.getString("REMARKS");
                    rowData[12] = disdata.getString("INVOICE_NO");
                    rowData[13] = disdata.getString("INVOICE_DATE");
                    rowData[14] = disdata.getString("PR_DOC_NO");
                    rowData[15] = disdata.getString("ORDER_DATE");
                    rowData[16] = disdata.getString("PR_PIECE_STAGE");
                    rowData[17] = disdata.getString("PR_WIP_STATUS");
                    rowData[18] = disdata.getString("PR_PRODUCT_CODE").trim();
                    rowData[19] = disdata.getString("PR_GROUP");
                    rowData[20] = disdata.getString("ZONE");
                    rowData[21] = disdata.getString("PR_MACHINE_NO");
                    rowData[22] = disdata.getString("POSITION_DESC");
                    rowData[23] = df.format(Double.parseDouble(disdata.getString("PR_LENGTH")));
                    rowData[24] = df.format(Double.parseDouble(disdata.getString("PR_WIDTH")));
                    rowData[25] = disdata.getString("PR_GSM");
                    rowData[26] = df.format(Double.parseDouble(disdata.getString("PR_THORITICAL_WEIGHT")));
                    DataModelDesc.addRow(rowData);
                    disdata.next();
                }
            }

            DoNotEvaluate = false;

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String DocNo = Obj.getAttribute("DOC_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsTrailPieceDispatchEntry.ModuleID, DocNo);
            for (int i = 1; i <= List.size(); i++) {
                clsDocFlow ObjFlow = (clsDocFlow) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];

                rowData[0] = Integer.toString(i);
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2] = (String) ObjFlow.getAttribute("STATUS").getObj();
                rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal()));
                rowData[4] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6] = (String) ObjFlow.getAttribute("REMARKS").getObj();

                DataModelA.addRow(rowData);

            }
            //============================================================//

            FormatGridHS();
            HashMap History = clsTrailPieceDispatchEntry.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsTrailPieceDispatchEntry ObjHistory = (clsTrailPieceDispatchEntry) History.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = ObjHistory.getAttribute("ENTRY_DATE").getString();

                String ApprovalStatus = "";

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }
                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
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
                rowData[3] = ApprovalStatus;
                rowData[4] = ObjHistory.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjHistory.getAttribute("FROM_IP").getString();
                DataModelHS.addRow(rowData);
            }
            //=========================================//
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Display Data Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields

        Obj.setAttribute("PREFIX", SelPrefix);
        Obj.setAttribute("SUFFIX", SelSuffix);
        Obj.setAttribute("FFNO", FFNo);
        Obj.setAttribute("DOC_NO", DOC_NO.getText());

        //----- Update Approval Specific Fields -----------//
        Obj.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        Obj.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        Obj.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        Obj.setAttribute("REJECTED_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            Obj.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            Obj.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            Obj.setAttribute("APPROVAL_STATUS", "R");
            Obj.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            Obj.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            Obj.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            Obj.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            Obj.setAttribute("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            Obj.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            Obj.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        //======= Set Line part ============
        Obj.colMRItems.clear();
        double tdata = 0;
        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            try {
                tdata = Double.parseDouble(TableDesc.getValueAt(i, 7).toString());
            } catch (Exception e) {
                tdata = 0;
            }
            if (tdata == 0.0) {
                TableDesc.setValueAt("", i, 7);
            }
            try {
                tdata = Double.parseDouble(TableDesc.getValueAt(i, 9).toString());
            } catch (Exception e) {
                tdata = 0;
            }
            if (tdata == 0.0) {
                TableDesc.setValueAt("", i, 9);
            }
        }
        for (int i = 0; i < TableDesc.getRowCount(); i++) {

            //Add Only Valid Items
            if (!TableDesc.getValueAt(i, 4).toString().equalsIgnoreCase("") || !TableDesc.getValueAt(i, 6).toString().equalsIgnoreCase("")
                    || !TableDesc.getValueAt(i, 7).toString().equalsIgnoreCase("") || !TableDesc.getValueAt(i, 8).toString().equalsIgnoreCase("")
                    || !TableDesc.getValueAt(i, 9).toString().equalsIgnoreCase("") || !TableDesc.getValueAt(i, 10).toString().equalsIgnoreCase("")
                    || !TableDesc.getValueAt(i, 11).toString().equalsIgnoreCase("")) {
                clsTrailPieceDispatchEntryItem ObjItem = new clsTrailPieceDispatchEntryItem();

                ObjItem.setAttribute("FT_PIECE_NO", TableDesc.getValueAt(i, 4).toString());
                ObjItem.setAttribute("MOUNTING_PLAN_DATE", EITLERPGLOBAL.formatDateDB(TableDesc.getValueAt(i, 5).toString()));
                ObjItem.setAttribute("MOUNTING_ACTUAL_DATE", EITLERPGLOBAL.formatDateDB(TableDesc.getValueAt(i, 6).toString()));
                try {
                    ObjItem.setAttribute("EXPECTED_LIFE_DAYS", Double.parseDouble(TableDesc.getValueAt(i, 7).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute("EXPECTED_LIFE_DAYS", 0.0);
                }
                ObjItem.setAttribute("DEMOUNTING_DATE", EITLERPGLOBAL.formatDateDB(TableDesc.getValueAt(i, 8).toString()));
                try {
                    ObjItem.setAttribute("ACTUAL_LIFE_DAYS", Double.parseDouble(TableDesc.getValueAt(i, 9).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute("ACTUAL_LIFE_DAYS", 0.0);
                }
                ObjItem.setAttribute("PERFORMANCE_FEEDBACK", TableDesc.getValueAt(i, 10).toString());
                ObjItem.setAttribute("REMARKS", TableDesc.getValueAt(i, 11).toString());
                Obj.colMRItems.put(Integer.toString(Obj.colMRItems.size() + 1), ObjItem);
            }
        }

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

    private void SetMenuForRights() {
        // --- Add Rights --        
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 99002, 990021)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 99002, 990022)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }
        //cmdEdit.setEnabled(true);

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 99002, 990023)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 99002, 990025)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
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

        //========== Setting Up Header Fields ================//
        String FieldName = "";
        int SelHierarchy = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        for (int i = 0; i < Tab1.getComponentCount() - 1; i++) {
            if (Tab1.getComponent(i).getName() != null) {

                FieldName = Tab1.getComponent(i).getName();

                if (FieldName.trim().equals("DOC_NO")) {
                    int a = 0;
                }
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {

                    Tab1.getComponent(i).setEnabled(true);
                }

            }
        }
        //=============== Setting Table Fields ==================//
        DataModelDesc.ClearAllReadOnly();
        for (int i = 0; i < TableDesc.getColumnCount(); i++) {
            FieldName = DataModelDesc.getVariable(i);

            if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
            } else {
                DataModelDesc.SetReadOnly(i);
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
        DataModelHS.addColumn("From Ip");

        TableHS.setAutoResizeMode(TableHS.AUTO_RESIZE_OFF);
    }

    public void FindWaiting() {
        Obj.Filter(" DOC_NO IN (SELECT H.DOC_NO FROM PRODUCTION.FELT_TRAIL_PIECE_SELECTION H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=801 AND CANCELED=0)");
        Obj.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pDocNo) {
        Obj.Filter(" DOC_NO='" + pDocNo + "'");
        Obj.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        String DocNo = DOC_NO.getText();

        //----- Generate cmbType ------- //
        cmbToModel = new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbToModel);

        //Now Add other hierarchy Users
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        List = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, true);
        for (int i = 1; i <= List.size(); i++) {
            clsUser ObjUser = (clsUser) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
            aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();

            boolean IncludeUser = false;
            //Decide to include user or not
            if (EditMode == EITLERPGLOBAL.EDIT) {
                if (OpgApprove.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsTrailPieceDispatchEntry.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsTrailPieceDispatchEntry.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (IncludeUser && (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID)) {
                    cmbToModel.addElement(aData);
                }
            } else {
                if (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID) {
                    cmbToModel.addElement(aData);
                }
            }

        }
        if (EditMode == EITLERPGLOBAL.EDIT) {
            DocNo = (String) Obj.getAttribute("DOC_NO").getObj();
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsTrailPieceDispatchEntry.ModuleID, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    private void FormatGrid() {

        Updating = true; //Stops recursion
        try {

            DataModelDesc = new EITLTableModel();
            TableDesc.removeAll();
            TableDesc.setModel(DataModelDesc);
            TableColumnModel ColModel = TableDesc.getColumnModel();
            TableDesc.setAutoResizeMode(TableDesc.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);

            DataModelDesc.addColumn("Sr.No.");  //0 - Read Only     
            DataModelDesc.addColumn("Party Code");//1
            DataModelDesc.addColumn("Name");//2
            DataModelDesc.addColumn("UPN"); //3
            DataModelDesc.addColumn("Piece No"); //4
            DataModelDesc.addColumn("Mounting Plan Date"); //5
            DataModelDesc.addColumn("Mounting Actual Date"); //6 
            DataModelDesc.addColumn("Expected Life Days"); //7  

            DataModelDesc.addColumn("Demounting Date"); //8

            DataModelDesc.addColumn("Actual Life Days"); //9       
            DataModelDesc.addColumn("Performance Feedback"); //10
            DataModelDesc.addColumn("Remarks");//11
            DataModelDesc.addColumn("Invoice No.");//12
            DataModelDesc.addColumn("Invoice Date");//13
            DataModelDesc.addColumn("Doc No.");//14
            DataModelDesc.addColumn("Order Date");//15
            DataModelDesc.addColumn("Piece Stage"); //16
            DataModelDesc.addColumn("WIP Status"); //17        
            DataModelDesc.addColumn("Product");//18
            DataModelDesc.addColumn("Group"); //19
            DataModelDesc.addColumn("Zone"); //20
            DataModelDesc.addColumn("Machine"); //21
            DataModelDesc.addColumn("Position"); //22            
            DataModelDesc.addColumn("Length"); //23
            DataModelDesc.addColumn("Width"); //24
            DataModelDesc.addColumn("GSM"); //25
            DataModelDesc.addColumn("Weight");   //26

            for (int i = 0; i <= 25; i++) {
                if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID) || EditMode == EITLERPGLOBAL.ADD) {
                    if (i >= 5 && i <= 11 && i != 9) {
                        DataModelDesc.ResetReadOnly(i);
                    } else {
                        DataModelDesc.SetReadOnly(i);
                    }
                } else {
                    DataModelDesc.SetReadOnly(i);
                }
            }

            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            //TableDesc.getColumnModel().getColumn(16).setPreferredWidth(100);

        } catch (Exception e) {

        }
        Updating = false;
    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    private void PreviewReport() {
//        HashMap Params = new HashMap();
//
//        try {
//            Connection Conn = EITLERP.data.getConn();
//            HashMap parameterMap = new HashMap();
//
//            EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(parameterMap, Conn);
//            String strSQL = "SELECT * FROM (SELECT H.DOC_NO,H.BEAM_NO,H.LOOM_NO,H.REED_SPACE,H.WARP_DETAIL,H.FABRIC_REALISATION_PER,H.WARP_TEX,(H.REED_SPACE*H.ENDS_10_CM)*10 AS WARP_END_TOTAL,H.ENDS_10_CM,H.ACTUAL_WARP_RELISATION,H.WARP_LENGTH,H.REED_COUNT, "
//                    + "SEQUANCE_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,READ_SPACE,THEORICAL_LENGTH_MTR,THEORICAL_PICKS_10_CM,TOTAL_PICKS,EXPECTED_GREV_SQ_MTR "
//                    + " FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D,PRODUCTION.FELT_TRAIL_PIECE_SELECTION H "
//                    + "WHERE H.DOC_NO=D.DOC_NO AND "
//                    + "H.DOC_NO='" + DOC_NO.getText() + "' AND (D.INDICATOR IS NULL OR D.INDICATOR IN ('INSERT','ADD',''))) AS A "
//                    + "LEFT JOIN (SELECT DOC_NO,SUM(THEORICAL_LENGTH_MTR) AS TOTAL_PC_LENGTH FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE (INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD','')) AND DOC_NO='" + DOC_NO.getText() + "') AS B "
//                    + "ON A.DOC_NO=B.DOC_NO "
//                    + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS C "
//                    + "ON A.PARTY_CODE=C.PARTY_CODE "
//                    + "ORDER BY SEQUANCE_NO";
//            rpt.setReportName("/EITLERP/FeltSales/FeltWarpingBeamOrder/FELT_WARPING_BEAM_ORDER.jrxml", 1, strSQL); //productlist is the name of my jasper file.
//            rpt.callReport();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void sort_query_creator() {
        SelectSortFields sort = new SelectSortFields();

        sort.setField("PR_PIECE_NO", "PIECE NO");
        sort.setField("PR_MACHINE_NO", "MACHINE NO");
        sort.setField("PR_POSITION_NO", "POSITION");
        sort.setField("PR_PARTY_CODE", "PARTY CODE");
        sort.setField("PR_PRODUCT_CODE", "PRODUCT CODE");
        sort.setField("PR_GROUP", "GROUP");
        sort.setField("PR_STYLE", "STYLE");
        sort.setField("PR_LENGTH", "LENGTH");
        sort.setField("PR_WIDTH", "WIDTH");
        sort.setField("PR_GSM", "GSM");
        sort.setField("PR_THORITICAL_WEIGHT", "WEIGHT");
        sort.setField("PR_PIECE_STAGE", "PIECE STAGE");
        ORDER_BY = sort.getQuery(SelectSortFields.DEFAULT_ORDER.ASCENDING);
    }

    private void GenerateCombo() {

        HashMap List = new HashMap();
        clsIncharge ObjIncharge;

        cmbZone.setModel(cmbIncharge);
        cmbIncharge.removeAllElements();  //Clearing previous contents

        List = clsIncharge.getIncgargeList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjIncharge = (clsIncharge) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Text = (String) ObjIncharge.getAttribute("INCHARGE_NAME").getObj();
            aData.Code = (long) ObjIncharge.getAttribute("INCHARGE_CD").getVal();
            cmbIncharge.addElement(aData);
        }
    }

    private void GenerateGroupCombo() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        tmpConn = data.getCreatedConn();

        cmbProdGroup.setModel(cmodelProductGroup);
        cmodelProductGroup.removeAllElements();  //Clearing previous contents

        ComboData aData1 = new ComboData();
        aData1.Text = "ALL";
        aData1.strCode = "ALL";
        cmodelProductGroup.addElement(aData1);

        try {
            tmpStmt = tmpConn.createStatement();
            //System.out.println("select distinct(GROUP_NAME) FROM PRODUCTION.FELT_QLT_RATE_MASTER");
            rsTmp = tmpStmt.executeQuery("select distinct(GROUP_NAME) FROM PRODUCTION.FELT_QLT_RATE_MASTER");

            while (rsTmp.next()) {
                ComboData aData = new ComboData();
                aData.Text = rsTmp.getString("GROUP_NAME");
                aData.strCode = rsTmp.getString("GROUP_NAME");
                cmodelProductGroup.addElement(aData);
            }

            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateYearCombo() {
        HashMap List = new HashMap();

        cmbFromYear.setModel(cmbFromModel);
        cmbFromYear.removeAllItems();

        List = clsFinYear.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);

        for (int i = 1; i <= List.size(); i++) {
            clsFinYear ObjYear = (clsFinYear) List.get(Integer.toString(i));

            ComboData cmbData = new ComboData();
            cmbData.Text = Integer.toString((int) ObjYear.getAttribute("YEAR_FROM").getVal());
            cmbData.Code = (int) ObjYear.getAttribute("YEAR_FROM").getVal();
            cmbData.strCode = Integer.toString((int) ObjYear.getAttribute("YEAR_FROM").getVal());
            cmbFromModel.addElement(cmbData);
        }
    }

    private void Mail() {
        System.out.println("Felt Trial Piece Dispatch Entry approved = " + Obj.getAttribute("APPROVAL_STATUS").getString());
        String pBody = "", pSubject = "", recievers = "", pcc = "";
        //if (Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("A") || Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {
        if (Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {

            pSubject = "Notification : Felt Trial Piece Dispatch Entry  :" + DOC_NO.getText() + " Final Approved";
            pBody = "Felt Trial Piece Dispatch Entry. " + DOC_NO.getText() + " has been final approved  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID) + "<br><br><br>";

            try {
                pBody += "Document Name : Felt Trial Piece Dispatch Entry  <br>";
                pBody += "Document No.  : " + DOC_NO.getText() + " <br>";

                int srNo = 0;
                String sql = "SELECT PR_PIECE_NO,PR_PIECE_STAGE,PR_WIP_STATUS,PR_PARTY_CODE,PARTY_NAME,PR_DOC_NO,"
                        + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(PR_ORDER_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE PR_ORDER_DATE END,'%d/%m/%Y'),'') AS ORDER_DATE,"
                        + "PR_UPN,PR_PRODUCT_CODE,PR_GROUP,PR_MACHINE_NO,POSITION_DESC,PR_STYLE,"
                        + "PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,INCHARGE_NAME AS ZONE,"
                        + "COALESCE(PR_INVOICE_NO,'') AS INVOICE_NO,"
                        + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE PR_INVOICE_DATE END,'%d/%m/%Y'),'') AS INVOICE_DATE,"
                        + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(MOUNTING_PLAN_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE MOUNTING_PLAN_DATE END,'%d/%m/%Y'),'') AS MOUNTING_PLAN_DATE,"
                        + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(MOUNTING_ACTUAL_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE MOUNTING_ACTUAL_DATE END,'%d/%m/%Y'),'') AS MOUNTING_ACTUAL_DATE,"
                        + "COALESCE(EXPECTED_LIFE_DAYS,'') AS EXPECTED_LIFE_DAYS,"
                        + "COALESCE(ACTUAL_LIFE_DAYS,'') AS ACTUAL_LIFE_DAYS,"
                        + "COALESCE(PERFORMANCE_FEEDBACK,'') AS PERFORMANCE_FEEDBACK,"
                        + "COALESCE(REMARKS,'') AS REMARKS  "
                        + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                        + "LEFT JOIN PRODUCTION.FELT_INCHARGE "
                        + "ON PR_INCHARGE=INCHARGE_CD "
                        + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST ON PR_POSITION_NO=POSITION_NO "
                        + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,PARTY_LOCK,PARTY_MILL_CLOSED_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS PM "
                        + "ON PR_PARTY_CODE=PARTY_CODE "
                        + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE DOC_NO='" + DOC_NO.getText() + "') AS D "
                        + "ON PR_PIECE_NO=FT_PIECE_NO "
                        + "WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1 "
                        + "AND PR_PIECE_NO IN ("
                        + "SELECT FT_PIECE_NO FROM PRODUCTION.FELT_TRAIL_PIECE_DISPATCH WHERE DOC_NO='" + DOC_NO.getText() + "' )";

                ResultSet disdata = data.getResult(sql);
                disdata.first();
                if (disdata.getRow() > 0) {
                    srNo = 0;
                    pBody += "<br>";
                    pBody += "<table border=1>";
                    pBody += "<tr><td align='center'><b>Sr.No.</b></td>"
                            + "<td align='center'><b>Party Code</b></td>"
                            + "<td align='center'><b>Party Name</b></td>"
                            + "<td align='center'><b>UPN</b></td>"
                            + "<td align='center'><b>Piece No.</b></td>"
                            + "<td align='center'><b>Mounting Plan Date</b></td>"
                            + "<td align='center'><b>Mounting Actual Date</b></td>"
                            + "<td align='center'><b>Expected Life Days</b></td>"
                            + "<td align='center'><b>Actual Life Days</b></td>"
                            + "<td align='center'><b>Performance Remarks</b></td>"
                            + "<td align='center'><b>Remarks</b></td>"
                            + "<td align='center'><b>Invoice No.</b></td>"
                            + "<td align='center'><b>Invoice Date</b></td>"
                            + "<td align='center'><b>Doc No.</b></td>"
                            + "<td align='center'><b>Order Date</b></td>"
                            + "<td align='center'><b>Product</b></td>"
                            + "<td align='center'><b>Group</b></td>"
                            + "<td align='center'><b>Zone</b></td>"
                            + "</tr>";
                    while (!disdata.isAfterLast()) {
                        srNo++;
                        pBody += "<tr>";
                        pBody += "<td>" + srNo + "</td>";
                        pBody += "<td>" + disdata.getString("PR_PARTY_CODE") + "</td>";
                        pBody += "<td>" + disdata.getString("PARTY_NAME") + "</td>";
                        pBody += "<td>" + disdata.getString("PR_UPN") + "</td>";
                        pBody += "<td>" + disdata.getString("PR_PIECE_NO") + "</td>";
                        pBody += "<td>" + disdata.getString("MOUNTING_PLAN_DATE") + "</td>";
                        pBody += "<td>" + disdata.getString("MOUNTING_ACTUAL_DATE") + "</td>";
                        pBody += "<td>" + disdata.getString("EXPECTED_LIFE_DAYS") + "</td>";
                        pBody += "<td>" + disdata.getString("ACTUAL_LIFE_DAYS") + "</td>";
                        pBody += "<td>" + disdata.getString("PERFORMANCE_FEEDBACK") + "</td>";
                        pBody += "<td>" + disdata.getString("REMARKS") + "</td>";
                        pBody += "<td>" + disdata.getString("INVOICE_NO") + "</td>";
                        pBody += "<td>" + disdata.getString("INVOICE_DATE") + "</td>";
                        pBody += "<td>" + disdata.getString("PR_DOC_NO") + "</td>";
                        pBody += "<td>" + disdata.getString("ORDER_DATE") + "</td>";
                        pBody += "<td>" + disdata.getString("PR_PRODUCT_CODE") + "</td>";
                        pBody += "<td>" + disdata.getString("PR_GROUP") + "</td>";
                        pBody += "<td>" + disdata.getString("ZONE") + "</td>";
                        pBody += "</tr>";
                        disdata.next();
                    }
                    pBody += "</table>";
                    pBody += "<br><br>";
                    pBody += "<br>";
                }

                pBody += "<br><br>";
                pBody += "<br>";
                pBody += "<table border=1>";
                pBody += "<tr><td align='center'><b>Sr.No.</b></td>"
                        + "<td align='center'><b>User</b></td>"
                        + "<td align='center'><b>Date</b></td>"
                        + "<td align='center'><b>Status</b></td>"
                        + "<td align='center'><b>Remark</b></td>"
                        + "</tr>";

                HashMap hmApprovalHistory = clsTrailPieceDispatchEntry.getHistoryList(EITLERPGLOBAL.gCompanyID, DOC_NO.getText());
                for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                    pBody += "<tr>";

                    clsTrailPieceDispatchEntry ObjWarping = (clsTrailPieceDispatchEntry) hmApprovalHistory.get(Integer.toString(i));
                    Object[] rowData = new Object[6];
                    pBody += "<td>" + Integer.toString((int) ObjWarping.getAttribute("REVISION_NO").getVal()) + "</td>";

                    pBody += "<td>" + clsUser.getUserName(2, (int) ObjWarping.getAttribute("UPDATED_BY").getVal()) + "</td>";
                    pBody += "<td>" + ObjWarping.getAttribute("ENTRY_DATE").getString() + "</td>";
                    String ApprovalStatus = "";

                    if ((ObjWarping.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                        ApprovalStatus = "Hold";
                    }

                    if ((ObjWarping.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                        ApprovalStatus = "Approved";
                    }

                    if ((ObjWarping.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                        ApprovalStatus = "Final Approved";
                    }

                    if ((ObjWarping.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                        ApprovalStatus = "Waiting";
                    }

                    if ((ObjWarping.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                        ApprovalStatus = "Rejected";
                    }

                    if ((ObjWarping.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                        ApprovalStatus = "Pending";
                    }

                    if ((ObjWarping.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                        ApprovalStatus = "Skiped";
                    }
                    pBody += "<td>" + ApprovalStatus + "</td>";
                    pBody += "<td>" + ObjWarping.getAttribute("APPROVER_REMARKS").getString() + "</td>";
                    pBody += "</tr>";
                }
                pBody += "</table>";
                pBody += "<br><br>";
                pBody += "<br>";

                //recievers = "dharmendra@dineshmills.com";
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
                //recievers += ",yrpatel@dineshmills.com,amitkanti@dineshmills.com,abtewary@dineshmills.com";
                recievers += ",vdshanbhag@dineshmills.com,soumen@dineshmills.com";
                pcc = "aditya@dineshmills.com,abtewary@dineshmills.com";
                pBody = pBody + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

                String responce = MailNotification.sendNotificationMail(801, pSubject, pBody, recievers, pcc, EITLERPGLOBAL.getComboCode(cmbHierarchy));
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
