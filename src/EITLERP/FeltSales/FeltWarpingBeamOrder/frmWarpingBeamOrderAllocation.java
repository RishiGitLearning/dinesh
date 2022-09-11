/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP.FeltSales.FeltWarpingBeamOrder;

/**
 *
 * @author Dharmendra
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.FeltSales.FeltPieceMaster.FrmPieceMasterDetail;
import javax.swing.table.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.text.*;
import java.sql.*;
import java.io.*;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import java.awt.Color;
import javax.swing.JFrame;

//import EITLERP.Purchase.frmSendMail;
public class frmWarpingBeamOrderAllocation extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsWarpingBeamAllocation Obj;
    private clsWarpingBeamAllocation Obj;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;

    private EITLTableModel DataModelDesc, DataModelDesc1;
    private EITLTableModel DataModelDiscount;
    private EITLTableModel DataModelA, DataModelH;
    private EITLTableModel DataModelHS;
    private EITLTableModel DataModelOtherpartyDiscount;

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelMainCode;

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    //clsColumn ObjColumn=new clsColumn();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private EITLComboModel cmbPriorityModel;

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
    public frmWarpingBeamOrderAllocation() {
        //this.requestFocus();

        System.gc();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        if (scrwidth > 800) {
            scrwidth = 800;
        }

        setSize(scrwidth, scrheight - 50);
        initComponents();
        file1.setVisible(false);

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
        cmdEmail.setIcon(EITLERPGLOBAL.getImage("EMAIL"));

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

        Obj = new clsWarpingBeamAllocation();

        SetMenuForRights();

        if (Obj.LoadData(EITLERPGLOBAL.gCompanyID)) {
            Obj.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data.   Error is " + Obj.LastError);
        }

        txtAuditRemarks.setVisible(false);
        //DataModelDesc.TableReadOnly(true);

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

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsWarpingBeamAllocation.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsWarpingBeamAllocation.ModuleID + "");
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
        bexcel = new javax.swing.JButton();
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
        file1 = new javax.swing.JFileChooser();

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
        lblTitle.setText("Felt Warping Beam Order Allocation");
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableDescKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(TableDesc);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 1070, 290);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(640, 300, 90, 23);

        bexcel.setText("Excel");
        bexcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bexcelActionPerformed(evt);
            }
        });
        Tab1.add(bexcel);
        bexcel.setBounds(10, 300, 140, 40);

        jTabbedPane1.addTab("Warping Beam Order Allocation", Tab1);

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
        jTabbedPane1.setBounds(5, 110, 1080, 520);

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
        getContentPane().add(file1);
        file1.setBounds(730, 40, 140, 340);
    }// </editor-fold>//GEN-END:initComponents

    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased

    }//GEN-LAST:event_TableDescKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton1ActionPerformed

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

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        // TODO add your handling code here:
        String DocNo = DOC_NO.getText();

        SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsWarpingBeamAllocation.ModuleID, DocNo)) {
                cmbSendTo.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }

        if (cmbSendTo.getItemCount() <= 0) {
            GenerateFromCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        // TODO add your handling code here:
        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(true);
        OpgHold.setSelected(false);

        GenerateRejectedUserCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void cmdNext3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_cmdNext3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo = DOC_NO.getText();
        Obj.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        Obj.LoadData(EITLERPGLOBAL.gCompanyID);
        //MoveFirst();
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if (TableHS.getRowCount() > 0 && TableHS.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableHS.getValueAt(TableHS.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton5ActionPerformed

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

    private void bexcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bexcelActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(TableDesc, new File(file1.getSelectedFile().toString() + ".xls"), "WarpingBeamOrder");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_bexcelActionPerformed

    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed

    }//GEN-LAST:event_TableDescKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApprovalPanel;
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
    private javax.swing.JButton bexcel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
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
    private javax.swing.JFileChooser file1;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
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
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//        
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 875;

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
//            System.out.println("FFNO:" + FFNo);
            DOC_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 875, FFNo, false));

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

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsWarpingBeamAllocation.ModuleID, (String) Obj.getAttribute("PROFORMA_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsWarpingBeamAllocation.ModuleID, FromUserID, (String) Obj.getAttribute("PROFORMA_NO").getObj());

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

                String DocNo = (String) Obj.getAttribute("BA_DOC_NO").getObj();

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsWarpingBeamAllocation.ModuleID, DocNo);
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

        // cmdEditPiecedetail.setEnabled(pStat);
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

        txtFromRemarks.setText("");
        txtToRemarks.setText("");

        FormatGrid();
        FormatGridA();
        FormatGridHS();
    }

    private void Edit() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }

        //----------------------------------//
        String lDocNo = Obj.getAttribute("BA_DOC_NO").getString();
        if (Obj.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID) || true) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (clsFeltProductionApprovalFlow.IsCreator(clsWarpingBeamAllocation.ModuleID, lDocNo) || true) {  // || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6070, 60702)) {
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
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String lDocNo = (String) Obj.getAttribute("BA_DOC_NO").getObj();

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
        String err = "";

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

        for (int k = 0; k <= TableDesc.getRowCount() - 1; k++) {

            if (TableDesc.getValueAt(k, 4).toString().equalsIgnoreCase("true") && !TableDesc.getValueAt(k, 9).toString().equalsIgnoreCase("")) {
                err = err + "\n You can not allocate for Beam for missing data at Row " + (k + 1);
            }
        }
        if (err.length() > 0) {
            JOptionPane.showMessageDialog(this, err, "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SetData();
        if (EditMode == EITLERPGLOBAL.ADD) {

            if (Obj.Insert()) {

//                if (OpgFinal.isSelected()) {
//                    try {
//                        String DOC_NO = "";
//                        String DOC_DATE = "";
//                        String Party_Code = "";
//
//                        String responce = JavaMail.sendFinalApprovalMail(clsWarpingBeamAllocation.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
//                        System.out.println("Send Mail Responce : " + responce);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
                // MoveLast();
                //DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving.  Error is " + Obj.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (Obj.Update()) {

//                if (OpgFinal.isSelected()) {
//                    try {
//                        String DOC_NO = "";
//                        String DOC_DATE = "";
//                        String Party_Code = "";
//
//                        String responce = JavaMail.sendFinalApprovalMail(clsWarpingBeamAllocation.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
//                        System.out.println("Send Mail Responce : " + responce);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving.  Error is " + Obj.LastError);
                return;
            }
        }
        Obj.Filter(" BA_DOC_NO='" + DOC_NO.getText() + "'");
        DisplayData();
        Mail();
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
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.FeltWarpingBeamOrder.frmFindWarpingBeamOrderPiece", true);
        frmFindWarpingBeamOrderPiece ObjReturn = (frmFindWarpingBeamOrderPiece) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            if (!Obj.Filter(ObjReturn.stringFindQuery)) {
                JOptionPane.showMessageDialog(null, "No records found.");
            }
            MoveFirst();
        }
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
            int ModuleID = clsWarpingBeamAllocation.ModuleID;

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
            DOC_NO.setText(Obj.getAttribute("BA_DOC_NO").getObj().toString());

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, Obj.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table

            double mtottpick = 0, mtotwvg = 0;
            String sql;
            sql = "SELECT BA_PIECE_NO, BA_LOOM_NO, COALESCE(BA_BEAM_TYPE,'') AS BA_BEAM_TYPE, BA_WEAVE_OUT_EXP_DATE, BA_WH_EXP_DATE, BA_PROCESS, "
                    + "BA_PARTY_CODE, BA_MACHINE_NO, BA_POSITION_NO, BA_UPN, BA_STYLE, BA_REED, BA_REED_SPACE, BA_LENGTH, BA_BEAM_DOC_NO, BA_GROUP,"
                    + "CASE WHEN COALESCE(BA_STYLE,'')='' THEN 'Design Master Missing' "
                    + "WHEN COALESCE(BA_LOOM_NO,'')='' THEN 'Loom Allocation Master Missing' "
                    + "WHEN COALESCE(BA_WEAVE_OUT_EXP_DATE,'')='' THEN 'MASTER MISMATCH' ELSE '' END AS SYSRMK,POSITION_DESC "
                    + " FROM PRODUCTION.TMP_BEAM_ALLOCATION "
                    + "LEFT JOIN  (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP  ON BA_POSITION_NO=MP.POSITION_NO";
            ResultSet r = data.getResult(sql);
            r.first();

//            if (r.getRow() > 0) {
            int sr = 1, s;
            for (int i = 1; i <= Obj.colMRItems.size(); i++) {

                clsWarpingBeamAllocationItem ObjItem = (clsWarpingBeamAllocationItem) Obj.colMRItems.get(Integer.toString(i));
                Object[] rowData = new Object[50];
                s = 0;
                rowData[s] = sr;
                s++;
                //rowData[s] = r.getString("BA_PIECE_NO");
                rowData[s] = (String) ObjItem.getAttribute("BA_PIECE_NO").getObj();
                s++;
                //rowData[s] = r.getString("BA_LOOM_NO");
                rowData[s] = (String) ObjItem.getAttribute("BA_LOOM_NO").getObj();
                s++;
                if (ObjItem.getAttribute("SYSRMK").getObj().toString().equalsIgnoreCase("")) {
                    if (ObjItem.getAttribute("BA_BEAM_TYPE").toString().equalsIgnoreCase("")) {
                        rowData[s] = "N";
                    } else {
                        //rowData[s] = r.getString("BA_BEAM_TYPE");
                        rowData[s] = (String) ObjItem.getAttribute("BA_BEAM_TYPE").getObj();
                    }
                } else {
                    rowData[s] = "";
                }
                s++;
                if (ObjItem.getAttribute("BA_PROCESS").getString().equalsIgnoreCase("1")) {
                    rowData[s] = true;
                } else {
                    rowData[s] = false;
                }
                s++;
                rowData[s] = (String) ObjItem.getAttribute("BA_REMARK").getObj();
                s++;
                //rowData[s] = EITLERPGLOBAL.formatDate(r.getString("BA_WEAVE_OUT_EXP_DATE"));
                rowData[s] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("BA_WEAVE_OUT_EXP_DATE").getObj());
                s++;
                //rowData[s] = EITLERPGLOBAL.formatDate(r.getString("BA_WH_EXP_DATE"));
                rowData[s] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("BA_WH_EXP_DATE").getObj());
                s++;
                //rowData[s] = r.getString("BA_BEAM_DOC_NO");
                rowData[s] = (String) ObjItem.getAttribute("BA_BEAM_DOC_NO").getObj();
                s++;
                //rowData[s] = r.getString("SYSRMK");
                rowData[s] = (String) ObjItem.getAttribute("SYSRMK").getObj();
                s++;
                //r.next();
                DataModelDesc.addRow(rowData);
                sr++;
            }
            final TableColumnModel columnModel = TableDesc.getColumnModel();
            for (int column = 0; column < TableDesc.getColumnCount(); column++) {
                int width = 60; // Min width
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

//            System.out.println("D");
            // cmdAutoSquanceActionPerformed(null);
            DoNotEvaluate = false;

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String DocNo = Obj.getAttribute("BA_DOC_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsWarpingBeamAllocation.ModuleID, DocNo);
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
//            HashMap History = clsWarpingBeamAllocation.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
//            for (int i = 1; i <= History.size(); i++) {
//                clsWarpingBeamAllocation ObjHistory = (clsWarpingBeamAllocation) History.get(Integer.toString(i));
//                Object[] rowData = new Object[6];
//
//                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
//                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
//                rowData[2] = ObjHistory.getAttribute("ENTRY_DATE").getString();
//
//                String ApprovalStatus = "";
//
//                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
//                    ApprovalStatus = "Approved";
//                }
//                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
//                    ApprovalStatus = "Hold";
//                }
//                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
//                    ApprovalStatus = "Final Approved";
//                }
//
//                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
//                    ApprovalStatus = "Waiting";
//                }
//
//                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
//                    ApprovalStatus = "Rejected";
//                }
//
//                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
//                    ApprovalStatus = "Pending";
//                }
//
//                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
//                    ApprovalStatus = "Skiped";
//                }
//                rowData[3] = ApprovalStatus;
//                rowData[4] = ObjHistory.getAttribute("APPROVER_REMARKS").getString();
//                rowData[5] = ObjHistory.getAttribute("FROM_IP").getString();
//                DataModelHS.addRow(rowData);
//            }
            //=========================================//
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Display Data Error: " + e.getMessage());
        }
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        Obj.setAttribute("BA_DOC_NO", DOC_NO.getText());

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
            //Obj.setAttribute("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            Obj.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            Obj.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        //======= Set Line part ============
        Obj.colMRItems.clear();
        String flg;
        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            clsWarpingBeamAllocationItem ObjItem = new clsWarpingBeamAllocationItem();
            flg = String.valueOf(TableDesc.getValueAt(i, 4));
            flg = flg.toLowerCase();
            //Add Only Valid Items
            if (flg.equals("true")) {
                ObjItem.setAttribute("BA_PIECE_NO", TableDesc.getValueAt(i, 1).toString());
                ObjItem.setAttribute("BA_LOOM_NO", TableDesc.getValueAt(i, 4).toString());
                ObjItem.setAttribute("BA_PROCESS", "1");
                ObjItem.setAttribute("BA_REMARK", TableDesc.getValueAt(i, 5).toString());
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6070, 60701)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6070, 60702)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }
        //cmdEdit.setEnabled(true);

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6070, 60703)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6070, 60705)) {
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
        Obj.Filter(" BA_DOC_NO IN (SELECT H.DOC_NO FROM PRODUCTION.BEAM_ALLOCATION H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.BA_DOC_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=" + clsWarpingBeamAllocation.ModuleID + " AND CANCELED=0)");
        Obj.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pDocNo) {
        Obj.Filter(" BA_DOC_NO='" + pDocNo + "'");
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsWarpingBeamAllocation.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsWarpingBeamAllocation.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            DocNo = (String) Obj.getAttribute("BA_DOC_NO").getObj();
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsWarpingBeamAllocation.ModuleID, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    private void FormatGrid() {

        Updating = true; //Stops recursion
        try {

            DataModelDesc = new EITLTableModel();
            TableDesc.removeAll();

            TableDesc.setModel(DataModelDesc);
            TableDesc.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            DataModelDesc.addColumn("Sr.");  //0 - Read Only
            DataModelDesc.addColumn("Piece No"); //1
            DataModelDesc.addColumn("Loom No"); //2
            DataModelDesc.addColumn("Beam"); //3
            DataModelDesc.addColumn("Allocation"); //4
            DataModelDesc.addColumn("Remark"); //5
            DataModelDesc.addColumn("ExpWeaveoutDate");//6
            DataModelDesc.addColumn("ExpWHDAte");//7
            DataModelDesc.addColumn("Warp No."); //8            
            DataModelDesc.addColumn("Sys.Remark"); //9

            DataModelDesc.SetReadOnly(0);
            //DataModelDesc.SetReadOnly(1);
            DataModelDesc.SetReadOnly(2);
            DataModelDesc.SetReadOnly(3);
            if (EditMode == EITLERPGLOBAL.EDIT) {

            } else {
                DataModelDesc.SetReadOnly(4);
                DataModelDesc.SetReadOnly(5);
            }
            DataModelDesc.SetReadOnly(6);
            DataModelDesc.SetReadOnly(7);
            DataModelDesc.SetReadOnly(8);
            DataModelDesc.SetReadOnly(9);

            int ImportCol = 1;
            Renderer.setCustomComponent(4, "CheckBox");
            TableDesc.getColumnModel().getColumn(4).setCellRenderer(Renderer);
            TableDesc.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));

            TableDesc.getColumnModel().getColumn(ImportCol).setCellEditor(new ButtonEditor(new JCheckBox()));
            TableDesc.getColumnModel().getColumn(ImportCol).setCellRenderer(new ButtonRenderer());

        } catch (Exception e) {

        }
        Updating = false;
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                //JOptionPane.showMessageDialog(button, label + ": Ouch!");
                if (TableDesc.getSelectedColumn() == 1) {
                    openPieceDetail();
                }

            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public void openPieceDetail() {
        try {
            //System.out.println("DRP");
            String PieceNo = TableDesc.getValueAt(TableDesc.getSelectedRow(), 1).toString();
            if (PieceNo.contains("-A") || PieceNo.contains("-B")) {
                PieceNo = PieceNo.substring(0, 5);
            }

            AppletFrame aFrame = new AppletFrame("Felt Piece Details");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceMaster.FrmPieceMasterDetail", "Felt Piece Details");
            FrmPieceMasterDetail ObjItem = (FrmPieceMasterDetail) aFrame.ObjApplet;

            ObjItem.requestFocus();
            ObjItem.setData(PieceNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    private void PreviewReport() {
        HashMap Params = new HashMap();

        try {
            Connection Conn = EITLERP.data.getConn();
            HashMap parameterMap = new HashMap();

            EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(parameterMap, Conn);
            String strSQL = "SELECT * FROM (SELECT H.DOC_NO,H.BEAM_NO,H.LOOM_NO,H.REED_SPACE,H.WARP_DETAIL,H.FABRIC_REALISATION_PER,H.WARP_TEX,(H.REED_SPACE*H.ENDS_10_CM)*10 AS WARP_END_TOTAL,H.ENDS_10_CM,H.ACTUAL_WARP_RELISATION,H.WARP_LENGTH,H.REED_COUNT, "
                    + "SEQUANCE_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,READ_SPACE,THEORICAL_LENGTH_MTR,THEORICAL_PICKS_10_CM,TOTAL_PICKS,EXPECTED_GREV_SQ_MTR "
                    + " FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D,PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER H "
                    + "WHERE H.DOC_NO=D.DOC_NO AND "
                    + "H.DOC_NO='" + DOC_NO.getText() + "' AND (D.INDICATOR IS NULL OR D.INDICATOR IN ('INSERT','ADD',''))) AS A "
                    + "LEFT JOIN (SELECT DOC_NO,SUM(THEORICAL_LENGTH_MTR) AS TOTAL_PC_LENGTH FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE (INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD','')) AND DOC_NO='" + DOC_NO.getText() + "') AS B "
                    + "ON A.DOC_NO=B.DOC_NO "
                    + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS C "
                    + "ON A.PARTY_CODE=C.PARTY_CODE "
                    + "ORDER BY SEQUANCE_NO";
            rpt.setReportName("/EITLERP/FeltSales/FeltWarpingBeamOrder/FELT_WARPING_BEAM_ORDER.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Mail() {

    }
}
