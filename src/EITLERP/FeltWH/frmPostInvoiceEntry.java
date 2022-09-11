/*
 */
package EITLERP.FeltWH;

import EITLERP.FeltSales.TrailPiece.*;
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
import java.io.File;

import javax.swing.JFrame;

//import EITLERP.Purchase.frmSendMail;
public class frmPostInvoiceEntry extends javax.swing.JApplet {

    private int EditMode = 0;
    private clsPostInvoiceEntry Obj;
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
    public EITLERP.FeltSales.Reports.clsExcelExporter exp = new EITLERP.FeltSales.Reports.clsExcelExporter();

//    private  static ObservableList<PieChart.Data> details = FXCollections.observableArrayList();
//    private static PieChart pieChart;
    /**
     * Creates new form frmSalesParty
     */
    public frmPostInvoiceEntry() {
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

        GenerateCombos();

        Obj = new clsPostInvoiceEntry();

        SetMenuForRights();

        if (Obj.LoadData(EITLERPGLOBAL.gCompanyID)) {
            Obj.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data.   Error is " + Obj.LastError);
        }

        txtAuditRemarks.setVisible(false);
        DataModelDesc.TableReadOnly(true);

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

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsPostInvoiceEntry.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsPostInvoiceEntry.ModuleID + "");
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
        cmdAdd = new javax.swing.JButton();
        cmdItemdelete = new javax.swing.JButton();
        excel2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
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
        lblTitle.setText("Felt Post Invoice Entry");
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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableDescKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(TableDesc);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 60, 740, 230);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(640, 300, 90, 23);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        Tab1.add(cmdAdd);
        cmdAdd.setBounds(570, 10, 80, 30);

        cmdItemdelete.setText("Remove");
        cmdItemdelete.setEnabled(false);
        cmdItemdelete.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdItemdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdItemdeleteActionPerformed(evt);
            }
        });
        Tab1.add(cmdItemdelete);
        cmdItemdelete.setBounds(660, 10, 80, 30);

        excel2.setText("EXPORT TO EXCEL");
        excel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excel2ActionPerformed(evt);
            }
        });
        Tab1.add(excel2);
        excel2.setBounds(10, 290, 170, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 255));
        jLabel4.setText("Press F1 For Invoice Selection");
        Tab1.add(jLabel4);
        jLabel4.setBounds(10, 14, 270, 30);

        jTabbedPane1.addTab("Felt Post Invoice Entry", Tab1);

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
        jTabbedPane1.setBounds(5, 140, 780, 510);

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
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsPostInvoiceEntry.ModuleID, DocNo)) {
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
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed

        Updating = true;
        Object[] rowData = new Object[40];
        rowData[0] = Integer.toString(TableDesc.getRowCount() + 1);
        rowData[1] = "";
        DataModelDesc.addRow(rowData);
        Updating = false;
        UpdateSrNo();
        TableDesc.changeSelection(TableDesc.getRowCount() - 1, 1, false, false);
        TableDesc.requestFocus();

    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdItemdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdItemdeleteActionPerformed
        if (TableDesc.getRowCount() > 0) {
            DataModelDesc.removeRow(TableDesc.getSelectedRow());
            UpdateSrNo();
            // DisplayIndicators();
        }
    }//GEN-LAST:event_cmdItemdeleteActionPerformed

    private void excel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excel2ActionPerformed
        // TODO add your handling code here:
        try {
            //exp.fillData(TableDesc, new File("D://PostInvEntry.xls"), "PostInvEntry");
            exp.fillData(TableDesc, new File(System.getProperty("user.home") + "/Desktop/PostInvEntry.xls"), "PostInvEntry");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'" + System.getProperty("user.home") + "/Desktop/PostInvEntry.xls' or 'D://PostInvEntry.xls' successfully in PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_excel2ActionPerformed

    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            try {
                if ((EditMode == 1) || (EditMode == 2 && clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID))) {
                    if (TableDesc.getSelectedColumn() == 1) {
                        LOV aList = new LOV();
                        aList.SQL = "SELECT CONCAT(INVOICE_NO,DATE_FORMAT(INVOICE_DATE,'%d/%m/%Y')) AS KEY1,INVOICE_NO,DATE_FORMAT(INVOICE_DATE,'%d/%m/%Y') AS INVOICE_DATE,BALE_NO,PARTY_CODE,PARTY_NAME  "
                                + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE "
                                + "APPROVED=1 AND CANCELLED=0 AND "
                                + "CONCAT(INVOICE_NO,INVOICE_DATE) NOT IN (SELECT CONCAT(INV_NO,INV_DATE) FROM PRODUCTION.FELT_POST_INVOICE_DETAIL "
                                + "WHERE "
                                //+ "INVOICE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND INVOICE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' AND "
                                + "COALESCE(CANCELED,0)=0) AND INVOICE_DATE>='2019-07-09' "
                                //+ "AND "
                                //+ "INVOICE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND INVOICE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' "
                                + "ORDER BY INVOICE_DATE DESC";
                        aList.ReturnCol = 1;
                        aList.ShowReturnCol = true;
                        aList.DefaultSearchOn = 1;

                        if (aList.ShowLOV()) {
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 1);
                            String sql = "SELECT INVOICE_NO,DATE_FORMAT(INVOICE_DATE,'%d/%m/%Y') AS INVOICE_DATE,BALE_NO,PARTY_CODE,PARTY_NAME,H.PIECE_NO,"
                                    + "DATE_FORMAT(PACKING_DATE,'%d/%m/%Y') AS BALE_DATE,ACTUAL_WEIGHT,DISPATCH_STATION,TRANSPORTER_NAME,"
                                    + "PKG_BOX_SIZE,PKG_MODE_PACKING,PKG_TRANSPORT_MODE "
                                    + "  FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H "
                                    + "LEFT JOIN PRODUCTION.FELT_PKG_SLIP_HEADER P ON H.BALE_NO=P.PKG_BALE_NO AND H.PACKING_DATE=P.PKG_BALE_DATE "
                                    + "WHERE INVOICE_DATE>='2019-07-09' "
                                    //+ "AND INVOICE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND INVOICE_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'  "
                                    + "AND H.APPROVED=1 AND H.CANCELLED=0 "
                                    + "AND CONCAT(H.INVOICE_NO,DATE_FORMAT(H.INVOICE_DATE,'%d/%m/%Y'))='" + aList.ReturnVal + "'";
                            ResultSet r = data.getResult(sql);
                            r.first();
                            if (r.getRow() > 0) {
                                TableDesc.setValueAt(r.getString("INVOICE_NO"), TableDesc.getSelectedRow(), 1);
                                TableDesc.setValueAt(r.getString("INVOICE_DATE"), TableDesc.getSelectedRow(), 2);
                                TableDesc.setValueAt(r.getString("BALE_NO"), TableDesc.getSelectedRow(), 3);
                                TableDesc.setValueAt(r.getString("BALE_DATE"), TableDesc.getSelectedRow(), 4);
                                TableDesc.setValueAt(r.getString("PIECE_NO"), TableDesc.getSelectedRow(), 5);
                                TableDesc.setValueAt(r.getString("PARTY_CODE"), TableDesc.getSelectedRow(), 6);
                                TableDesc.setValueAt(r.getString("PARTY_NAME"), TableDesc.getSelectedRow(), 7);
                                TableDesc.setValueAt(r.getDouble("ACTUAL_WEIGHT"), TableDesc.getSelectedRow(), 8);
                                TableDesc.setValueAt(r.getString("DISPATCH_STATION"), TableDesc.getSelectedRow(), 10);
                                TableDesc.setValueAt(r.getString("PKG_MODE_PACKING"), TableDesc.getSelectedRow(), 11);
                                TableDesc.setValueAt(r.getString("PKG_BOX_SIZE"), TableDesc.getSelectedRow(), 12);
                                TableDesc.setValueAt(r.getString("PKG_TRANSPORT_MODE"), TableDesc.getSelectedRow(), 13);
                                TableDesc.setValueAt(r.getString("TRANSPORTER_NAME"), TableDesc.getSelectedRow(), 14);
                            }
                            final TableColumnModel columnModel = TableDesc.getColumnModel();
                            for (int column = 0; column < TableDesc.getColumnCount(); column++) {
                                int width = 100; // Min width
                                for (int row = 0; row < TableDesc.getRowCount(); row++) {
                                    TableCellRenderer renderer = TableDesc.getCellRenderer(row, column);
                                    Component comp = TableDesc.prepareRenderer(renderer, row, column);
                                    width = Math.max(comp.getPreferredSize().width + 1, width);
                                }
                                if (width > 300) {
                                    width = 300;
                                }
                                columnModel.getColumn(column).setPreferredWidth(width);
                            }
                        }
                    }
                    if (TableDesc.getSelectedColumn() == 11) {
                        LOV aList = new LOV();
                        aList.SQL = "SELECT '1' AS SR_NO,'BALE' AS TYPE_OF_PACKING FROM DUAL "
                                + "UNION ALL SELECT '2' AS SR_NO,'SLEEVE' FROM DUAL "
                                + "UNION ALL SELECT '3' AS SR_NO,'WOODEN BOX' FROM DUAL";
                        aList.ReturnCol = 1;
                        aList.ShowReturnCol = true;
                        aList.DefaultSearchOn = 1;

                        if (aList.ShowLOV()) {
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 11);
                        }
                    }
                    if (TableDesc.getSelectedColumn() == 13) {
                        LOV aList = new LOV();
                        aList.SQL = "SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='FELT_PACKINGSLIP'";
                        aList.ReturnCol = 2;
                        aList.ShowReturnCol = true;
                        aList.DefaultSearchOn = 1;

                        if (aList.ShowLOV()) {
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 13);
                        }
                    }
                    if (TableDesc.getSelectedColumn() == 14) {
                        LOV aList = new LOV();
                        aList.SQL = "SELECT @a:=@a+1 AS 'SR_NO',TRANSPORTER_NAME FROM D_SAL_TRANSPORTER_MASTER ORDER BY TRANSPORTER_NAME ";
                        aList.ReturnCol = 2;
                        aList.ShowReturnCol = true;
                        aList.DefaultSearchOn = 1;

                        if (aList.ShowLOV()) {
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 14);
                        }
                    }
                    if (TableDesc.getSelectedColumn() == 15) {
                        LOV aList = new LOV();
                        aList.SQL = "SELECT @a:=@a+1 AS 'SR_NO',TRANSPORTER_NAME FROM D_SAL_TRANSPORTER_MASTER ORDER BY TRANSPORTER_NAME ";
                        aList.ReturnCol = 2;
                        aList.ShowReturnCol = true;
                        aList.DefaultSearchOn = 1;

                        if (aList.ShowLOV()) {
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 15);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_TableDescKeyPressed


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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdEmail;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdItemdelete;
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
    private javax.swing.JButton excel2;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables

    private void Add() {
        //== Financial Year Validation-------------//
        //if (!EITLERPGLOBAL.YearIsOpen) {
       //     JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
       //     return;
       // }
        //----------------------------------//        
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 802;

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
            DOC_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 802, FFNo, false));
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

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsPostInvoiceEntry.ModuleID, (String) Obj.getAttribute("DOC_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsPostInvoiceEntry.ModuleID, FromUserID, (String) Obj.getAttribute("DOC_NO").getObj());

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

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsPostInvoiceEntry.ModuleID, DocNo);
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

        cmdAdd.setEnabled(pStat);
        cmdItemdelete.setEnabled(pStat);
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

    private void ClearFields() {

        DOC_NO.setText("");
        DOC_DATE.setText("");
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

            if (clsFeltProductionApprovalFlow.IsCreator(clsPostInvoiceEntry.ModuleID, lDocNo)) {  // || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6070, 60702)) {
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
            if (!TableDesc.getValueAt(k, 1).toString().equalsIgnoreCase("")) {
                mcntpc++;
            }
        }
        if (mcntpc == 0) {
            JOptionPane.showMessageDialog(null, "Please Select Invoice No....");
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
            int ModuleID = clsPostInvoiceEntry.ModuleID;

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        try {
            ClearFields();
            //lblTitle.setText("FELT PROFORMA INVOICE - " + (String) Obj.getAttribute("DOC_NO").getObj());
            DOC_NO.setText(Obj.getAttribute("DOC_NO").getObj().toString());
            DOC_DATE.setText(EITLERPGLOBAL.formatDate(Obj.getAttribute("DOC_DATE").getObj().toString()));
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, Obj.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table            
            for (int i = 1; i <= Obj.colMRItems.size(); i++) {

                clsPostInvoiceEntryItem ObjItem = (clsPostInvoiceEntryItem) Obj.colMRItems.get(Integer.toString(i));
                Object[] rowData = new Object[50];

                rowData[0] = i;
                rowData[1] = (String) ObjItem.getAttribute("INV_NO").getObj();
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("INV_DATE").getObj());
                rowData[3] = (String) ObjItem.getAttribute("BALE_NO").getObj();
                rowData[4] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("BALE_DATE").getObj());
                rowData[5] = (String) ObjItem.getAttribute("PI_PIECE_NO").getObj();
                rowData[6] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[7] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();
                rowData[8] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("NET_WEIGHT").getDouble(), 2));
                rowData[9] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("GROSS_WEIGHT").getDouble(), 2));
                rowData[10] = (String) ObjItem.getAttribute("DISPATCH_STATION").getObj();
                rowData[11] = (String) ObjItem.getAttribute("TYPE_OF_PACKING").getObj();
                rowData[12] = (String) ObjItem.getAttribute("BOX_SIZE").getObj();
                rowData[13] = (String) ObjItem.getAttribute("MODE_OF_TRANSPORT").getObj();
                rowData[14] = (String) ObjItem.getAttribute("TRANSPORTER").getObj();
                rowData[15] = (String) ObjItem.getAttribute("CARTING_AGENT").getObj();

//                rowData[14] = (String) ObjItem.getAttribute("TRANSPORT_BY").getObj();
                DataModelDesc.addRow(rowData);
            }
            final TableColumnModel columnModel = TableDesc.getColumnModel();
            for (int column = 0; column < TableDesc.getColumnCount(); column++) {
                int width = 100; // Min width
                for (int row = 0; row < TableDesc.getRowCount(); row++) {
                    TableCellRenderer renderer = TableDesc.getCellRenderer(row, column);
                    Component comp = TableDesc.prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 1, width);
                }
                if (width > 300) {
                    width = 300;
                }
                columnModel.getColumn(column).setPreferredWidth(width);
            }
            DoNotEvaluate = false;

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String DocNo = Obj.getAttribute("DOC_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsPostInvoiceEntry.ModuleID, DocNo);
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
            HashMap History = clsPostInvoiceEntry.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsPostInvoiceEntry ObjHistory = (clsPostInvoiceEntry) History.get(Integer.toString(i));
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
            JOptionPane.showMessageDialog(null, "Display Data Error: " + e.getMessage());
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

        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            clsPostInvoiceEntryItem ObjItem = new clsPostInvoiceEntryItem();

            //Add Only Valid Items
            if (!TableDesc.getValueAt(i, 1).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("INV_NO", TableDesc.getValueAt(i, 1).toString());
                ObjItem.setAttribute("INV_DATE", EITLERPGLOBAL.formatDateDB(TableDesc.getValueAt(i, 2).toString()));
                ObjItem.setAttribute("BALE_NO", TableDesc.getValueAt(i, 3).toString());
                ObjItem.setAttribute("BALE_DATE", EITLERPGLOBAL.formatDateDB(TableDesc.getValueAt(i, 4).toString()));
                ObjItem.setAttribute("PI_PIECE_NO", TableDesc.getValueAt(i, 5).toString());
                ObjItem.setAttribute("PARTY_CODE", TableDesc.getValueAt(i, 6).toString());
                ObjItem.setAttribute("PARTY_NAME", TableDesc.getValueAt(i, 7).toString());
                try {
                    ObjItem.setAttribute("NET_WEIGHT", Double.parseDouble(TableDesc.getValueAt(i, 8).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute("NET_WEIGHT", 0.0);
                }
                try {
                    ObjItem.setAttribute("GROSS_WEIGHT", Double.parseDouble(TableDesc.getValueAt(i, 9).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute("GROSS_WEIGHT", 0.0);
                }
                ObjItem.setAttribute("DISPATCH_STATION", TableDesc.getValueAt(i, 10).toString());
                ObjItem.setAttribute("TYPE_OF_PACKING", TableDesc.getValueAt(i, 11).toString());
                ObjItem.setAttribute("BOX_SIZE", TableDesc.getValueAt(i, 12).toString());
                ObjItem.setAttribute("MODE_OF_TRANSPORT", TableDesc.getValueAt(i, 13).toString());
                ObjItem.setAttribute("TRANSPORTER", TableDesc.getValueAt(i, 14).toString());
                ObjItem.setAttribute("CARTING_AGENT", TableDesc.getValueAt(i, 15).toString());

//                ObjItem.setAttribute("TRANSPORT_BY", TableDesc.getValueAt(i, 14).toString());
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6240, 62401)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6240, 62402)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }
        //cmdEdit.setEnabled(true);

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6240, 62403)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6240, 62405)) {
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
        Obj.Filter(" DOC_NO IN (SELECT H.DOC_NO FROM PRODUCTION.FELT_TRAIL_PIECE_SELECTION H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=802 AND CANCELED=0)");
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsPostInvoiceEntry.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsPostInvoiceEntry.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsPostInvoiceEntry.ModuleID, DocNo);
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
            DataModelDesc.addColumn("Invoice No.");  //1 - Read Only
            DataModelDesc.addColumn("Invoice Date"); //2            
            DataModelDesc.addColumn("Bale No."); //3           
            DataModelDesc.addColumn("Bale Date"); //4
            DataModelDesc.addColumn("Piece No."); //5  
            DataModelDesc.addColumn("Party Code"); //6
            DataModelDesc.addColumn("Name"); //7  
            DataModelDesc.addColumn("Net Weight"); //8
            DataModelDesc.addColumn("Gross Weight"); //9              

            DataModelDesc.addColumn("Dispatch Station");//11    //10

            DataModelDesc.addColumn("Type of Packing"); //14    //11
            DataModelDesc.addColumn("Box Size");//10            //12
            DataModelDesc.addColumn("Mode of Transport"); //15  //13
            DataModelDesc.addColumn("Transporter");//12         //14
            DataModelDesc.addColumn("Carting Agent"); //13      //15

            DataModelDesc.SetReadOnly(0);
            DataModelDesc.SetReadOnly(1);
            DataModelDesc.SetReadOnly(2);
            DataModelDesc.SetReadOnly(3);
            DataModelDesc.SetReadOnly(4);
            DataModelDesc.SetReadOnly(5);
            DataModelDesc.SetReadOnly(6);
            DataModelDesc.SetReadOnly(7);
            DataModelDesc.SetReadOnly(8);
//            DataModelDesc.SetReadOnly(11);
//            DataModelDesc.SetReadOnly(13);
//            DataModelDesc.SetReadOnly(14);
//            DataModelDesc.SetReadOnly(15);

            if (EditMode == EITLERPGLOBAL.ADD || (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID) && EditMode == EITLERPGLOBAL.EDIT)) {
                //DataModelDesc.ResetReadOnly(1);
                DataModelDesc.ResetReadOnly(9);
                DataModelDesc.ResetReadOnly(10);
                DataModelDesc.ResetReadOnly(12);
//                DataModelDesc.SetReadOnly(11);
//                DataModelDesc.SetReadOnly(13);
//                DataModelDesc.SetReadOnly(14);
//                DataModelDesc.SetReadOnly(15);

            } else {
                DataModelDesc.SetReadOnly(9);
                DataModelDesc.SetReadOnly(10);
                DataModelDesc.SetReadOnly(12);
//                DataModelDesc.SetReadOnly(11);
//                DataModelDesc.SetReadOnly(13);
//                DataModelDesc.SetReadOnly(14);
//                DataModelDesc.SetReadOnly(15);

            }
            DataModelDesc.SetReadOnly(11);
            DataModelDesc.SetReadOnly(13);
            DataModelDesc.SetReadOnly(14);
            DataModelDesc.SetReadOnly(15);
            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            //TableDesc.getColumnModel().getColumn(16).setPreferredWidth(100);

        } catch (Exception e) {
            e.printStackTrace();
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

    private void UpdateSrNo() {
        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            TableDesc.setValueAt(Integer.toString(i + 1), i, 0);
        }
    }

    private void Mail() {
        System.out.println("Felt Post Invoice Entry approved = " + Obj.getAttribute("APPROVAL_STATUS").getString());
        String pBody = "", pSubject = "", recievers = "", pcc = "";
        //if (Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("A") || Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {
        if (Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {

            pSubject = "Notification : Felt Post Invoice Entry  :" + DOC_NO.getText() + " Final Approved";
            pBody = "Felt Post Invoice Entry. " + DOC_NO.getText() + " has been final approved  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID) + "<br><br><br>";

            try {
                pBody += "Document Name : Felt Post Invoice Entry  <br>";
                pBody += "Document No.  : " + DOC_NO.getText() + " <br>";

                int srNo = 0;
                String sql = "SELECT INV_NO,DATE_FORMAT(INV_DATE,'%d/%m/%Y') AS INV_DATE,BALE_NO,DATE_FORMAT(BALE_DATE,'%d/%m/%Y') AS BALE_DATE,"
                        + "PI_PIECE_NO,PARTY_CODE,PARTY_NAME,NET_WEIGHT,GROSS_WEIGHT,DISPATCH_STATION,TYPE_OF_PACKING,BOX_SIZE,MODE_OF_TRANSPORT,"
                        + "TRANSPORTER,CARTING_AGENT "
                        + "FROM PRODUCTION.FELT_POST_INVOICE_DETAIL "
                        + "WHERE  DOC_NO='" + DOC_NO.getText() + "' ";
                ResultSet disdata = data.getResult(sql);
                disdata.first();
                if (disdata.getRow() > 0) {
                    srNo = 0;
                    pBody += "<br>";
                    pBody += "<table border=1>";
                    pBody += "<tr><td align='center'><b>Sr.No.</b></td>"
                            + "<td align='center'><b>Invoice No.</b></td>"
                            + "<td align='center'><b>Invoice Date</b></td>"
                            + "<td align='center'><b>Bale No.</b></td>"
                            + "<td align='center'><b>Bale Date</b></td>"
                            + "<td align='center'><b>Piece No.</b></td>"
                            + "<td align='center'><b>Party Code</b></td>"
                            + "<td align='center'><b>Party Name</b></td>"
                            + "<td align='center'><b>Net Weight</b></td>"
                            + "<td align='center'><b>Gross Weight</b></td>"
                            + "<td align='center'><b>Dispatch Station</b></td>"
                            + "<td align='center'><b>Type of Packing</b></td>"
                            + "<td align='center'><b>Box Size</b></td>"
                            + "<td align='center'><b>Mode of Transport</b></td>"
                            + "<td align='center'><b>Transporter</b></td>"
                            + "<td align='center'><b>Catring Agent</b></td>"
                            + "</tr>";
                    while (!disdata.isAfterLast()) {
                        srNo++;
                        pBody += "<tr>";
                        pBody += "<td>" + srNo + "</td>";
                        pBody += "<td>" + disdata.getString("INV_NO") + "</td>";
                        pBody += "<td>" + disdata.getString("INV_DATE") + "</td>";
                        pBody += "<td>" + disdata.getString("BALE_NO") + "</td>";
                        pBody += "<td>" + disdata.getString("BALE_DATE") + "</td>";
                        pBody += "<td>" + disdata.getString("PI_PIECE_NO") + "</td>";
                        pBody += "<td>" + disdata.getString("PARTY_CODE") + "</td>";
                        pBody += "<td>" + disdata.getString("PARTY_NAME") + "</td>";
                        pBody += "<td>" + disdata.getString("NET_WEIGHT") + "</td>";
                        pBody += "<td>" + disdata.getString("GROSS_WEIGHT") + "</td>";
                        pBody += "<td>" + disdata.getString("DISPATCH_STATION") + "</td>";
                        pBody += "<td>" + disdata.getString("TYPE_OF_PACKING") + "</td>";
                        pBody += "<td>" + disdata.getString("BOX_SIZE") + "</td>";
                        pBody += "<td>" + disdata.getString("MODE_OF_TRANSPORT") + "</td>";
                        pBody += "<td>" + disdata.getString("TRANSPORTER") + "</td>";
                        pBody += "<td>" + disdata.getString("CARTING_AGENT") + "</td>";

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

                HashMap hmApprovalHistory = clsPostInvoiceEntry.getHistoryList(EITLERPGLOBAL.gCompanyID, DOC_NO.getText());
                for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                    pBody += "<tr>";

                    clsPostInvoiceEntry ObjWarping = (clsPostInvoiceEntry) hmApprovalHistory.get(Integer.toString(i));
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

                recievers = "narendramotiani@dineshmills.com,feltwh@dineshmills.com";
                pcc = "sdmlerp@dineshmills.com";
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
                pBody = pBody + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

                String responce = MailNotification.sendNotificationMail(802, pSubject, pBody, recievers, pcc, EITLERPGLOBAL.getComboCode(cmbHierarchy));
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
