package EITLERP;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.*;
import java.lang.*;
import java.sql.*;
import java.lang.String;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class frmimportstmt_hdfc extends javax.swing.JApplet {

    private int EditMode = 0;

    private clsimportstmt_hdfc ObjImportStmt;
    private clsExcel_Exporter exp = new clsExcel_Exporter();

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbItemTypeModel;

    private EITLTableModel DataModelD;
    private EITLTableModel DataModelDesc;
    private EITLTableModel DataModelInvoice;
    private EITLTableModel DataModelDiscount;
    private EITLTableModel DataModelA;
    private EITLTableModel DataModelHS;
    private EITLTableModel DataModelOtherpartyDiscount;

    
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellAlign = new EITLTableCellRenderer();
    private EITLTableCellRenderer RowFormat = new EITLTableCellRenderer();

    //private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelMainCode;

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private EITLComboModel cmbPriorityModel;

    private boolean HistoryView = false;
    private String theDocNo = "", minvdt;
    public frmPendingApprovals frmPA;
    private int charge09index = 0;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();
    String cellLastValue = "", mdocnm = "";
    String mfilenm;
    private boolean chk = true;

    public frmimportstmt_hdfc() {
        System.gc();
        setSize(950,670 );
        initComponents();
//        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd/MM/yyyy");
//        dtefffrom.setFormats(fmt);
//        dteffto.setFormats(fmt);
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
        //cmdEmail.setIcon(EITLERPGLOBAL.getImage("EMAIL"));
        cmdFilter.setVisible(false);

        GenerateCombos();

        ObjImportStmt = new clsimportstmt_hdfc();

        SetMenuForRights();
        editable(false);
        file1.setVisible(false);
        if (ObjImportStmt.LoadData(EITLERPGLOBAL.gCompanyID)) {
            ObjImportStmt.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data. \n Error is " + ObjImportStmt.LastError);
        }
        txtAuditRemarks.setVisible(false);

    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsimportstmt_hdfc.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsimportstmt_hdfc.ModuleID + "");
        }

        for (int i = 1; i <= List.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_grp_hierarchy = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        btn_grp_type = new javax.swing.ButtonGroup();
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
        jButton1 = new javax.swing.JButton();
        lbltot = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableD = new javax.swing.JTable();
        file1 = new javax.swing.JFileChooser();
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
        view = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        bankstmt = new javax.swing.JTable();
        btn_exp_xls = new javax.swing.JButton();
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
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Bank Statement");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 800, 25);

        Tab1.setLayout(null);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(640, 300, 140, 40);

        lbltot.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbltot.setEnabled(false);
        Tab1.add(lbltot);
        lbltot.setBounds(630, 290, 80, 30);

        TableD.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        TableD.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TableD);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 790, 290);
        Tab1.add(file1);
        file1.setBounds(120, 0, 435, 413);

        jTabbedPane1.addTab("Bank Statement Import", Tab1);

        ApprovalPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ApprovalPanel.setToolTipText("");
        ApprovalPanel.setLayout(null);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        ApprovalPanel.add(jLabel31);
        jLabel31.setBounds(5, 13, 100, 17);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        ApprovalPanel.add(cmbHierarchy);
        cmbHierarchy.setBounds(110, 13, 270, 27);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        ApprovalPanel.add(jLabel32);
        jLabel32.setBounds(5, 43, 100, 17);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        ApprovalPanel.add(txtFrom);
        txtFrom.setBounds(110, 43, 270, 22);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        ApprovalPanel.add(jLabel35);
        jLabel35.setBounds(5, 76, 100, 17);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        ApprovalPanel.add(txtFromRemarks);
        txtFromRemarks.setBounds(110, 73, 518, 22);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        ApprovalPanel.add(jLabel36);
        jLabel36.setBounds(5, 116, 100, 17);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        btn_grp_hierarchy.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 169, 22);

        btn_grp_hierarchy.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });
        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 136, 20);

        btn_grp_hierarchy.add(OpgReject);
        OpgReject.setText("Reject");
        OpgReject.setEnabled(false);
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });
        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 136, 20);

        btn_grp_hierarchy.add(OpgHold);
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
        jLabel33.setBounds(5, 226, 100, 17);

        cmbSendTo.setEnabled(false);
        ApprovalPanel.add(cmbSendTo);
        cmbSendTo.setBounds(110, 223, 270, 27);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        ApprovalPanel.add(jLabel34);
        jLabel34.setBounds(5, 262, 100, 17);

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
        cmdNext3.setBounds(515, 300, 110, 30);

        jButton3.setText("Next >>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(jButton3);
        jButton3.setBounds(630, 300, 120, 30);

        jTabbedPane1.addTab("Approval", null, ApprovalPanel, "");

        StatusPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        StatusPanel.setLayout(null);

        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 17);

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
        jLabel19.setBounds(10, 170, 182, 17);

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
        txtAuditRemarks.setBounds(570, 260, 129, 27);

        jButton4.setText("Next >>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton4);
        jButton4.setBounds(660, 290, 100, 30);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(560, 290, 100, 30);

        jTabbedPane1.addTab("Status", StatusPanel);

        view.setLayout(null);

        bankstmt.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(bankstmt);

        view.add(jScrollPane3);
        jScrollPane3.setBounds(0, 0, 780, 340);

        btn_exp_xls.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btn_exp_xls.setText("Export To Excel");
        btn_exp_xls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exp_xlsActionPerformed(evt);
            }
        });
        view.add(btn_exp_xls);
        btn_exp_xls.setBounds(0, 350, 160, 40);

        jTabbedPane1.addTab("Match Record", view);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 70, 800, 430);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(20, 550, 610, 20);
    }// </editor-fold>//GEN-END:initComponents

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
        String PartyID = "";

        SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID, clsimportstmt_hdfc.ModuleID, PartyID)) {
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
        String FileName = "";
        ObjImportStmt.ShowHistory(EITLERPGLOBAL.gCompanyID, FileName);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjImportStmt.LoadData(EITLERPGLOBAL.gCompanyID);
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
        //PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:        
        editable(true);
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        editable(true);
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        Save();
        editable(false);
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
        editable(false);
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        //ObjColumn.Close();
        ObjImportStmt.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void btn_exp_xlsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exp_xlsActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showOpenDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }

            //exp.fillData(TableRate, new File("/root/Desktop/RateMaster.xls"), "RateMaster");
            //exp.fillData(TableRate, new File("D://RateMaster.xls"), "RateMaster");
            exp.fillData(bankstmt, file, "Bank_Match_Statement");
            JOptionPane.showMessageDialog(null, "Data saved at "
                + file.toString() + " successfully ...", "Message",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btn_exp_xlsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApprovalPanel;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JPanel Tab1;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableD;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JTable bankstmt;
    private javax.swing.JButton btn_exp_xls;
    private javax.swing.ButtonGroup btn_grp_hierarchy;
    private javax.swing.ButtonGroup btn_grp_type;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lbltot;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JPanel view;
    // End of variables declaration//GEN-END:variables

    private void Add() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//        
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 758;
        if (aList.ShowList()) {
            EditMode = EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SetupApproval();
            lblTitle.setBackground(Color.BLUE);

            SelPrefix = aList.Prefix; //Selected Prefix;
            SelSuffix = aList.Suffix;
            FFNo = aList.FirstFreeNo;
            mdocnm = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 758, FFNo, false);
            lblTitle.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 758, FFNo, false));
            lblTitle.setText("Bank Statement  - " + lblTitle.getText());
            JOptionPane.showMessageDialog(null, lblTitle.getText().trim().substring(18, 26));
            lblTitle.setBackground(Color.yellow);

            import_data();

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

            int FromUserID = ApprovalFlow.getFromID(EITLERPGLOBAL.gCompanyID, clsimportstmt_hdfc.ModuleID, (String) ObjImportStmt.getAttribute("FILE_NAME").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = ApprovalFlow.getFromRemarks(EITLERPGLOBAL.gCompanyID, clsimportstmt_hdfc.ModuleID, FromUserID, (String) ObjImportStmt.getAttribute("FILE_NAME").getObj());

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

                String FileName = (String) ObjImportStmt.getAttribute("STATEMENT_ID").getObj();

                List = ApprovalFlow.getRemainingUsers(EITLERPGLOBAL.gCompanyID, clsimportstmt_hdfc.ModuleID, FileName);
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
        cmdEmail.setEnabled(false);
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
        cmdSave.setEnabled(chk);
        cmdCancel.setEnabled(true);
        cmdFilter.setEnabled(false);
        cmdPreview.setEnabled(false);
        cmdPrint.setEnabled(false);
        cmdExit.setEnabled(false);
        cmdEmail.setEnabled(false);
    }

    private boolean Validate() {
        int ValidEntryCount = 0;

        //Now Header level validations
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

        return true;
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
        String lFileName = ObjImportStmt.getAttribute("STATEMENT_ID").getString();
        if (ObjImportStmt.IsEditable(EITLERPGLOBAL.gCompanyID, lFileName, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (ApprovalFlow.IsCreator(clsimportstmt_hdfc.ModuleID, lFileName) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 85011)) {
                SetFields(true);
            } else {
                EnableApproval();
            }

            //DisplayData();
            DisableToolbar();
        } else {
            JOptionPane.showMessageDialog(null, "You cannot edit this record. \n It is either approved/rejected or waiting approval for other user");
        }
    }

    private void Delete() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String lDocNo = (String) ObjImportStmt.getAttribute("STATEMENT_ID").getObj();

        if (ObjImportStmt.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            if (ObjImportStmt.Delete(EITLERPGLOBAL.gNewUserID)) {
                MoveLast();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while deleting. \nError is " + ObjImportStmt.LastError);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
        }
    }

    private void Save() {
        //Form level validations
        if (Validate() == false) {
            return; //Validation failed
        }

        SetData();
        if (EditMode == EITLERPGLOBAL.ADD) {

            if (ObjImportStmt.Insert()) {
                MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjImportStmt.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjImportStmt.Update()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjImportStmt.LastError);
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
    }

    private void Cancel() {
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
    }

    private void Find() {
    }

    private void MoveFirst() {
        ObjImportStmt.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjImportStmt.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjImportStmt.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjImportStmt.MoveLast();
        DisplayData();
    }

    //Didplay data on the Screen
    private void DisplayData() {
        //=========== Color Indication ===============//
        try {
            if (EditMode == 0) {
                String mstatus;
                mstatus = clsimportstmt_hdfc.getStatus(ObjImportStmt.getAttribute("STATEMENT_ID").getObj().toString());

                if (mstatus.equalsIgnoreCase("F")) {
                    lblTitle.setBackground(Color.GREEN);
                    cmdEmail.setEnabled(false);
                    cmdEdit.setEnabled(false);
                }
                if (mstatus.equalsIgnoreCase("A")) {
                    lblTitle.setBackground(Color.YELLOW);
                    cmdEmail.setEnabled(false);
                    cmdEdit.setEnabled(true);
                }
                if (mstatus.equalsIgnoreCase("R")) {
                    lblTitle.setBackground(Color.RED);
                    cmdEdit.setEnabled(false);
                    cmdEmail.setEnabled(false);
                }
                if (mstatus.equalsIgnoreCase("H")) {
                    lblTitle.setBackground(Color.GRAY);
                    cmdEmail.setEnabled(false);
                    cmdEdit.setEnabled(true);
                }

                if (ObjImportStmt.getAttribute("CANCELLED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                    cmdEmail.setEnabled(false);
                    cmdEdit.setEnabled(false);
                }
            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = clsimportstmt_hdfc.ModuleID;

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        try {
            ClearFields();
            // boolean bState = false;

            lblTitle.setText("Bank Statement  - " + (String) ObjImportStmt.getAttribute("STATEMENT_ID").getObj());
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjImportStmt.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//

            //Now Generate Table
            FormatGrid();

            for (int i = 1; i <= ObjImportStmt.colMRItems.size(); i++) {
                clsimportstmt_hdfcitem ObjItem = (clsimportstmt_hdfcitem) ObjImportStmt.colMRItems.get(Integer.toString(i));

                Object[] rowData = new Object[30];
                try {
                    rowData[0] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("BOOK_DATE").getObj());
                } catch (Exception e) {
                    rowData[0] = "";
                }
                rowData[1] = (String) ObjItem.getAttribute("DESCRIPTION").getObj();
                rowData[2] = Double.toString(ObjItem.getAttribute("LEDGER_BALANCE").getVal());
                rowData[3] = Double.toString(ObjItem.getAttribute("CREDIT").getVal());
                rowData[4] = Double.toString(ObjItem.getAttribute("DEBIT").getVal());
                try {
                    rowData[5] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("VALUE_DATE").getObj());
                } catch (Exception d) {
                    rowData[5] = "";
                }
                rowData[6] = (String) ObjItem.getAttribute("REFERANCE_NO").getObj();
                rowData[7] = (String) ObjItem.getAttribute("TRANSACTION_BRANCH").getObj();
                rowData[8]=(String) ObjItem.getAttribute("BOOK_CODE").getObj();

                DataModelD.addRow(rowData);

            }

            int sts;
            sts = data.getIntValueFromDB("SELECT COUNT(*) FROM DINESHMILLS.D_COM_DOC_DATA WHERE DOC_NO='" + (String) ObjImportStmt.getAttribute("STATEMENT_ID").getObj() + "' AND STATUS='F'");

            if (sts == 1) {
                ResultSet rs = null;
                String sql;
                FormatGrid_BankStmt();
                try {
                    sql = "SELECT STMT_DATE,STMT_BRANCH,STMT_DESCRIPTION,STMT_REF_NO,STMT_CREDITS,STMT_DEBITS,STMT_INST_NO,STMT_INST_AMOUNT,STMT_VOUCHER_NO "
                            + "FROM BANK_RECO.D_BANK_STMT "
                            + "WHERE STMT_ID='"+(String) ObjImportStmt.getAttribute("STATEMENT_ID").getObj()+"' AND UPDATE_DATE!='0000-00-00'";
                    rs = data.getResult(sql);
                    rs.first();
                    while (!rs.isAfterLast()) {
                        Object[] rowData = new Object[20];
                        rowData[0] = EITLERPGLOBAL.formatDate(rs.getString("STMT_DATE"));
                        rowData[1] = rs.getString("STMT_BRANCH");
                        rowData[2] = rs.getString("STMT_DESCRIPTION");
                        rowData[3] = rs.getString("STMT_REF_NO");
                        rowData[4] = rs.getDouble("STMT_CREDITS");
                        rowData[5] = rs.getDouble("STMT_DEBITS");
                        rowData[6] = rs.getString("STMT_INST_NO");
                        rowData[7] = EITLERPGLOBAL.formatDate(rs.getString("STMT_DATE"));
                        rowData[8] = rs.getDouble("STMT_INST_AMOUNT");
                        rowData[9] = rs.getString("STMT_VOUCHER_NO");
                        DataModelD.addRow(rowData);
                        rs.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String FileName = ObjImportStmt.getAttribute("STATEMENT_ID").getString();
            List = ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, clsimportstmt_hdfc.ModuleID, FileName);
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

            DoNotEvaluate = false;
            //============================================================//

            //Showing Audit Trial History
            FormatGridHS();
            HashMap History = clsimportstmt_hdfc.getHistoryList(EITLERPGLOBAL.gCompanyID, FileName);
            for (int i = 1; i <= History.size(); i++) {
                clsimportstmt_hdfc ObjHistory = (clsimportstmt_hdfc) History.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjHistory.getAttribute("ENTRY_DATE").getString());
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
        ObjImportStmt.setAttribute("PREFIX", SelPrefix);
        ObjImportStmt.setAttribute("SUFFIX", SelSuffix);
        ObjImportStmt.setAttribute("FFNO", FFNo);
        ObjImportStmt.setAttribute("STATEMENT_ID", lblTitle.getText().substring(18, 26).trim());

        //----- Update Approval Specific Fields -----------//
        ObjImportStmt.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjImportStmt.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjImportStmt.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjImportStmt.setAttribute("REJECTED_REASON", txtToRemarks.getText());
        ObjImportStmt.setAttribute("APPROVER_REMARKS", txtToRemarks.getText());
        ObjImportStmt.setAttribute("APPEND_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());

        if (OpgApprove.isSelected()) {
            ObjImportStmt.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjImportStmt.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjImportStmt.setAttribute("APPROVAL_STATUS", "R");
            ObjImportStmt.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjImportStmt.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjImportStmt.setAttribute("CREATED_BY", EITLERPGLOBAL.gUserID);
            ObjImportStmt.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
        } else {
            ObjImportStmt.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gUserID);
            ObjImportStmt.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
        }
        //======= Set Line part ============
        ObjImportStmt.colMRItems.clear();
        for (int i = 0; i < TableD.getRowCount(); i++) {
            clsimportstmt_hdfcitem ObjItem = new clsimportstmt_hdfcitem();
            ObjItem.setAttribute("STATEMENT_ID", lblTitle.getText().substring(18, 26).trim());
            ObjItem.setAttribute("BOOK_DATE", (String) TableD.getValueAt(i, 0));
            ObjItem.setAttribute("DESCRIPTION", (String) TableD.getValueAt(i, 1));
            ObjItem.setAttribute("LEDGER_BALANCE", Double.parseDouble((String) TableD.getValueAt(i, 2)));
            ObjItem.setAttribute("CREDIT", Double.parseDouble((String) TableD.getValueAt(i, 3)));
            ObjItem.setAttribute("DEBIT", Double.parseDouble((String) TableD.getValueAt(i, 4)));
            ObjItem.setAttribute("VALUE_DATE", (String) TableD.getValueAt(i, 5));
            ObjItem.setAttribute("REFERANCE_NO", (String) TableD.getValueAt(i, 6));
            ObjItem.setAttribute("TRANSACTION_BRANCH", (String) TableD.getValueAt(i, 7));
            ObjItem.setAttribute("BOOK_CODE", (String) TableD.getValueAt(i, 8));

            ObjImportStmt.colMRItems.put(Integer.toString(ObjImportStmt.colMRItems.size() + 1), ObjItem);
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
        // TableA.getColumnModel().getColumn(0).setCellRenderer(Paint);
        // Paint.setColor(1,1,Color.CYAN);

    }

    private void SetMenuForRights() {
        // --- Add Rights --        
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 85011)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);

        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 85012)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 85014)) {
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

                if (FieldName.trim().equals("STATEMENT_ID")) {
                    int a = 0;
                }
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {

                    Tab1.getComponent(i).setEnabled(true);
                }

            }
        }
        //=============== Header Fields Setup Complete =================//       
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

    public void FindWaiting() {
        //ObjImportStmt.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsimportstmt_hdfc.ModuleID+")");
        ObjImportStmt.Filter(" STATEMENT_ID IN (SELECT BANK_RECO.TMP_BANK_STATEMENT.STATEMENT_ID FROM BANK_RECO.TMP_BANK_STATEMENT,DINESHMILLS.D_COM_DOC_DATA WHERE BANK_RECO.TMP_BANK_STATEMENT.STATEMENT_ID=DINESHMILLS.D_COM_DOC_DATA.DOC_NO AND DINESHMILLS.D_COM_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND DINESHMILLS.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=758)");
        ObjImportStmt.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pFileName) {
        System.out.println(pFileName);
        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjImportStmt.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjImportStmt.Filter(" STATEMENT_ID='" + pFileName + "'");
        ObjImportStmt.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        String DocCode = lblTitle.getText().trim().substring(lblTitle.getText().trim().length() - 8);

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

            /// NEW CODE ///
            boolean IncludeUser = false;
            //Decide to include user or not
            if (EditMode == EITLERPGLOBAL.EDIT) {
                if (OpgApprove.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, clsimportstmt_hdfc.ModuleID, DocCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, clsimportstmt_hdfc.ModuleID, DocCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String FileName = (String) ObjImportStmt.getAttribute("STATEMENT_ID").getObj();
            int Creator = ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, clsimportstmt_hdfc.ModuleID, FileName);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    private void PreviewReport() {
        /* HashMap Params=new HashMap();
        
         try {
         URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/RS/Reports/Production/rptProforma.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&DocNo="+txtProformano.getText()+"&ProformaDate="+EITLERPGLOBAL.formatDateDB(txtProformaDate.getText()));
         EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
         }
         catch(Exception e) {
         JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
         }
         */
    }

    private void editable(boolean pstat) {
        //dtefffrom.setEditable(pstat);
        //dteffto.setEditable(pstat);
    }

    private void disp_import_data() {
        Connection Conn = null;
        Statement stmt = null;
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            ResultSet rsData = stmt.executeQuery("SELECT d.*,CASE WHEN cast( REFERANCE_NO  as decimal(65,0))>0 THEN cast( REFERANCE_NO  as decimal(65,0))  ELSE REFERANCE_NO END AS NEW_REF FROM BANK_RECO.TMP_BANK_STATEMENT as d");

            rsData.first();
            int i = 0;
            String mdata;
            
            while (!rsData.isAfterLast()) {

                Object[] rowData = new Object[30];
                try {
                    rowData[0] = EITLERPGLOBAL.formatDate(rsData.getString("BOOK_DATE").trim());
                } catch (Exception e) {
                    rowData[0] = "";
                }
                rowData[1] = rsData.getString("DESCRIPTION").trim();
                rowData[2] = rsData.getString("LEDGER_BALANCE").trim();
                rowData[3] = rsData.getString("CREDIT").trim();
                rowData[4] = rsData.getString("DEBIT").trim();
                try {
                    rowData[5] = EITLERPGLOBAL.formatDate(rsData.getString("VALUE_DATE").trim());
                } catch (Exception e) {
                    rowData[5] = "";
                }
                rowData[6] = rsData.getString("NEW_REF").trim();
                rowData[7] = rsData.getString("TRANSACTION_BRANCH").trim();
                rowData[8]= rsData.getString("BOOK_CODE").trim();

                DataModelD.addRow(rowData);
                TableD.changeSelection(TableD.getRowCount() - 1, 1, false, false);
                TableD.requestFocus();
                mdata = (String) TableD.getModel().getValueAt(i, 1);
                TableD.setValueAt(mdata, TableD.getSelectedRow(), 1);
                i = i + 1;
                rsData.next();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in Load Bank Data..."+e.getMessage());
            
        }
    }

    private void FormatGrid() {
        try {
            DataModelD = new EITLTableModel();
            TableD.removeAll();
            TableD.setModel(DataModelD);
            TableColumnModel ColModel = TableD.getColumnModel();
            TableD.setAutoResizeMode(TableD.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.BLUE);

            DataModelD.addColumn("BOOK DATE"); //0
            DataModelD.addColumn("DESCRIPTION"); //1
            DataModelD.addColumn("LEDGER BALANCE");//2
            DataModelD.addColumn("CREDIT"); //3
            DataModelD.addColumn("DEBIT"); //4
            DataModelD.addColumn("VALUE DATE"); //5
            DataModelD.addColumn("REFERANCE No."); //6
            DataModelD.addColumn("TRANSACTION BRANCH"); //7
            DataModelD.addColumn("TR CODE");//8

            DataModelD.TableReadOnly(true);

            CellAlign.setHorizontalAlignment(JLabel.LEFT);

        } catch (Exception e) {
        }
        Updating = false;
        //Table formatting completed

    }

    private void import_data() {
        Connection Conn = null, con = null;
        Statement stmt = null;

        try {
            String strSQL = "";
            Conn = data.getConn();
            Conn.setAutoCommit(false);
            stmt = Conn.createStatement();

            stmt.execute("TRUNCATE TABLE BANK_RECO.TMP_BANK_STATEMENT");

            con = data.getConn();
            PreparedStatement pstm = null;
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showOpenDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);
            FileInputStream input = new FileInputStream(file);
            POIFSFileSystem fs = new POIFSFileSystem(input);
            //File input = new File(file1.toString());
            //OPCPackage pkg = OPCPackage.open(file1.getSelectedFile().toString());

            HSSFWorkbook wb = new HSSFWorkbook(fs);
            //XSSFWorkbook wb = new XSSFWorkbook(pkg);
            //XSSFSheet sheet = wb.getSheetAt(0);
            HSSFSheet sheet = wb.getSheetAt(0);
            DataFormatter objDefaultFormat = new DataFormatter();
            FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) wb);

            Row row;
            String mbdt, mdesc, mefdt, metdt, mlb, mcr, mdr, mref, mtb, sql;
            sql = "INSERT INTO BANK_RECO.TMP_BANK_STATEMENT (BOOK_DATE,DESCRIPTION,LEDGER_BALANCE,CREDIT,DEBIT,VALUE_DATE,REFERANCE_NO,TRANSACTION_BRANCH,BOOK_CODE) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            double dlb, dcr, ddr;
            java.util.Date date;
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("yyyy-dd-MM");
            Cell cellValue;
            String cellValueStr = "";
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = (Row) sheet.getRow(i);

                for (int d = 0; d < 9; d++) {
                    if (d == 0 || d == 5) {
                        try {
                            date = row.getCell(d).getDateCellValue();
                            cellValueStr = sdf.format(date);
                        } catch (Exception e) {
                            try {
                                cellValueStr = row.getCell(d).getStringCellValue();
                                cellValueStr = EITLERPGLOBAL.formatDateDB(cellValueStr.trim());
                            } catch (Exception a) {
                                cellValueStr = null;
                            }
                        }
                    } else {
                        if (d == 6) {
                            try {
                                cellValueStr = String.valueOf(row.getCell(d).getNumericCellValue());
                            } catch (Exception e) {
                                try {
                                    cellValueStr = row.getCell(d).getStringCellValue();
                                } catch (Exception s) {
                                    cellValueStr = "";
                                }
                            }
                        } else {
                            cellValue = row.getCell(d);
                            objFormulaEvaluator.evaluate(cellValue); // This will evaluate the cell, And any type of cell will return string value
                            cellValueStr = objDefaultFormat.formatCellValue(cellValue, objFormulaEvaluator);
                        }
                    }
                    if (d == 2 || d == 3 || d == 4) {
                        cellValueStr = cellValueStr.replaceAll(",", "");
                    }
                    pstm.setString(d + 1, cellValueStr);
                }
              
                pstm.addBatch();
                if ((i + 1) % 1000 == 0) {
                    pstm.executeBatch();
                    Conn.commit();
                }
                System.out.println("Import rows " + i);
            }
            //con.commit();
            pstm.executeBatch();
            Conn.commit();
            pstm.close();
            //con.close();
            input.close();
            Conn.setAutoCommit(true);
            System.out.println("Success import excel to mysql table");
            data.Execute("UPDATE BANK_RECO.TMP_BANK_STATEMENT SET DEBIT=DEBIT*-1 WHERE DEBIT<>0");
            data.Execute("DELETE FROM BANK_RECO.TMP_BANK_STATEMENT WHERE BOOK_DATE IS NULL");
            data.Execute("DELETE FROM BANK_RECO.TMP_BANK_STATEMENT WHERE VALUE_DATE IS NULL");
            disp_import_data();

            //if (chk == true) {                
            //    FormatGrid();
            //    disp_import_data();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
private void FormatGrid_BankStmt() {
        try {
            DataModelD = new EITLTableModel();
            bankstmt.removeAll();
            bankstmt.setModel(DataModelD);
            TableColumnModel ColModel = bankstmt.getColumnModel();
            bankstmt.setAutoResizeMode(bankstmt.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.BLUE);

            DataModelD.addColumn("STATEMENT DATE"); //0
            DataModelD.addColumn("STATEMENT BRANCH");//1
            DataModelD.addColumn("DESCRIPTION"); //2
            DataModelD.addColumn("REFERANCE NO");//3            
            DataModelD.addColumn("CREDIT"); //4
            DataModelD.addColumn("DEBIT"); //5
            DataModelD.addColumn("STATEMENT INST NO"); //6
            DataModelD.addColumn("STATEMENT DATE"); //7
            DataModelD.addColumn("MT_INST_AMOUNT"); //8
            DataModelD.addColumn("STATEMENT VOURCHER");//9

            DataModelD.TableReadOnly(true);

            CellAlign.setHorizontalAlignment(JLabel.LEFT);

        } catch (Exception e) {
        }

        //Table formatting completed
    }
}
