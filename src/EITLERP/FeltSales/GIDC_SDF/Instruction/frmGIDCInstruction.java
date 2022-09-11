/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP.FeltSales.GIDC_SDF.Instruction;

/**
 *
 * @author Dharmendra
 */
import EITLERP.FeltSales.FeltWarpingBeamOrder.*;
import EITLERP.Production.ReportUI.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.MailNotification;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import java.lang.*;
import javax.swing.text.*;
import java.sql.*;
import java.lang.String;
import java.net.*;
import EITLERP.Utils.*;
import java.io.*;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import java.math.*;
import EITLERP.Production.ReportUI.JTextFieldHint;

import EITLERP.Stores.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import EITLERP.Purchase.*;
import TReportWriter.NumWord;
import java.math.BigDecimal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import static javax.swing.JDialog.setDefaultLookAndFeelDecorated;
import javax.swing.JFrame;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import EITLERP.Purchase.frmSendMail;
public class frmGIDCInstruction extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsGIDCInstruction Obj;
    private clsGIDCInstruction Obj;
    private EITLERP.FeltSales.common.FeltInvCalc inv_calculation;
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
    public frmGIDCInstruction() {
        //this.requestFocus();

        System.gc();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
//        if (scrwidth > 800) {
//            scrwidth = 800;
//        }

        setSize(scrwidth - 100, scrheight - 80);
        setDefaultLookAndFeelDecorated(true);
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

        GenerateCombos();

        Obj = new clsGIDCInstruction();

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

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsGIDCInstruction.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsGIDCInstruction.ModuleID + "");
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
        cmdAdd = new javax.swing.JButton();
        cmdItemdelete = new javax.swing.JButton();
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
        Tab2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableDesc1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        bexcel1 = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        DOC_NO = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        file1 = new javax.swing.JFileChooser();
        txtdate = new javax.swing.JTextField();

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
        lblTitle.setText("Felt SDF Manufracturing Instruction");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 730, 25);

        Tab1.setLayout(null);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

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
        TableDesc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableDescMouseClicked(evt);
            }
        });
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
        jScrollPane1.setBounds(0, 50, 970, 240);

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

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(640, 300, 90, 28);

        bexcel.setText("Excel");
        bexcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bexcelActionPerformed(evt);
            }
        });
        Tab1.add(bexcel);
        bexcel.setBounds(10, 300, 140, 40);

        jTabbedPane1.addTab("Instruction Entry [SDF]", Tab1);

        ApprovalPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ApprovalPanel.setToolTipText("");
        ApprovalPanel.setLayout(null);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        ApprovalPanel.add(jLabel31);
        jLabel31.setBounds(5, 13, 100, 16);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        ApprovalPanel.add(cmbHierarchy);
        cmbHierarchy.setBounds(110, 13, 270, 28);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        ApprovalPanel.add(jLabel32);
        jLabel32.setBounds(5, 43, 100, 16);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        ApprovalPanel.add(txtFrom);
        txtFrom.setBounds(110, 43, 270, 22);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        ApprovalPanel.add(jLabel35);
        jLabel35.setBounds(5, 76, 100, 16);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        ApprovalPanel.add(txtFromRemarks);
        txtFromRemarks.setBounds(110, 73, 518, 22);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        ApprovalPanel.add(jLabel36);
        jLabel36.setBounds(5, 116, 100, 16);

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
        jLabel33.setBounds(5, 226, 100, 16);

        cmbSendTo.setEnabled(false);
        ApprovalPanel.add(cmbSendTo);
        cmbSendTo.setBounds(110, 223, 270, 28);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        ApprovalPanel.add(jLabel34);
        jLabel34.setBounds(5, 262, 100, 16);

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
        cmdNext3.setBounds(550, 300, 99, 32);

        jButton3.setText("Next >>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(jButton3);
        jButton3.setBounds(650, 300, 100, 28);

        jTabbedPane1.addTab("Approval", null, ApprovalPanel, "");

        StatusPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        StatusPanel.setLayout(null);

        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 16);

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
        jLabel19.setBounds(10, 170, 182, 16);

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
        txtAuditRemarks.setBounds(570, 260, 129, 28);

        jButton4.setText("Next >>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton4);
        jButton4.setBounds(670, 290, 65, 28);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(570, 290, 90, 32);

        jTabbedPane1.addTab("Status", StatusPanel);

        Tab2.setLayout(null);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        TableDesc1.setModel(new javax.swing.table.DefaultTableModel(
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
        TableDesc1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TableDesc1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableDesc1MouseClicked(evt);
            }
        });
        TableDesc1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableDesc1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableDesc1KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(TableDesc1);

        Tab2.add(jScrollPane3);
        jScrollPane3.setBounds(0, 50, 970, 240);

        jButton2.setText("Next >>");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Tab2.add(jButton2);
        jButton2.setBounds(640, 300, 90, 28);

        bexcel1.setText("Excel");
        bexcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bexcel1ActionPerformed(evt);
            }
        });
        Tab2.add(bexcel1);
        bexcel1.setBounds(10, 300, 140, 40);

        jTabbedPane1.addTab("Instruction Amendment [SDF]", Tab2);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 140, 1020, 430);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(10, 570, 750, 40);

        jLabel1.setText("Document No");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 75, 100, 20);

        DOC_NO.setEditable(false);
        DOC_NO.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        DOC_NO.setForeground(new java.awt.Color(0, 0, 255));
        DOC_NO.setText("D000001");
        DOC_NO.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        getContentPane().add(DOC_NO);
        DOC_NO.setBounds(100, 70, 130, 30);

        jLabel20.setText("Date");
        getContentPane().add(jLabel20);
        jLabel20.setBounds(260, 80, 70, 20);
        getContentPane().add(file1);
        file1.setBounds(730, 40, 140, 340);

        txtdate.setEditable(false);
        txtdate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtdate);
        txtdate.setBounds(320, 70, 120, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased

        String PieceNo = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 1);
        ResultSet trs = null;
        String sql = "";
        try {
            if ((EditMode == 1 || EditMode == 2) && clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
                switch (TableDesc.getSelectedColumn()) {
                    case 1:
                        lblStatus.setText("Press F1 For Select Piece No.");
                        break;
                    case 2:
                        lblStatus.setText("Press F1 For Select RM Code");
                        break;
                    case 4:
                        lblStatus.setText("Enter Require Kg For Length");
                        break;
                    case 5:
                        lblStatus.setText("Press F1 For Select RM Code");
                        break;
                    case 7:
                        lblStatus.setText("Enter Require Kg For Width");
                        break;
                    case 8:
                        lblStatus.setText("Enter Sequence No.");
                        break;
                    default:
                        lblStatus.setText("");
                        break;
                }

                if (TableDesc.getSelectedColumn() == 2) {
                    try {
                        //trs = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE SUBSTRING(PR_PIECE_NO,1,5) = '" + PieceNo.substring(0, 5) + "' AND PR_PIECE_STAGE='PLANNING' AND (PR_WARP_DATE='0000-00-00' OR PR_WARP_DATE IS NULL) AND PR_PIECE_NO NOT IN (SELECT LEFT(PIECE_NO,5) FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL)");
                        sql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                                + "WHERE PR_GROUP='SDF' AND SUBSTRING(PR_PIECE_NO,1,5) = '" + PieceNo.substring(0, 5) + "' AND "
                                + "PR_PIECE_STAGE='PLANNING' AND (PR_WARP_DATE='0000-00-00' OR PR_WARP_DATE IS NULL) "
                                + "AND (PR_PIECE_NO NOT IN (SELECT LEFT(PIECE_NO,5) FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL)) ";
//                                + "OR PR_PIECE_NO IN (SELECT DISTINCT LEFT(D.PIECE_NO,5) FROM ("
//                                + "SELECT  * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL "
//                                + "WHERE PIECE_NO='" + PieceNo + "' ) AS D "
//                                + "LEFT JOIN (SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL_AMEND) AS  A "
//                                + "ON D.PIECE_NO=A.PIECE_NO "
//                                + "WHERE A.PIECE_NO IS NULL)";
                        System.out.println("Check Piece : " + sql);
                        trs = data.getResult(sql);
                    } catch (Exception a) {
                        JOptionPane.showMessageDialog(null, "Invalid Piece No....");
                        cmdItemdeleteActionPerformed(null);
                    }
                    if (trs.getRow() > 0) {
                        TableDesc.setValueAt(trs.getString("PR_PARTY_CODE"), TableDesc.getSelectedRow(), 2); //9
                        TableDesc.setValueAt(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, trs.getString("PR_PARTY_CODE")), TableDesc.getSelectedRow(), 3); //10
                        TableDesc.setValueAt(trs.getString("PR_POSITION_NO"), TableDesc.getSelectedRow(), 4); 
                        TableDesc.setValueAt(trs.getString("PR_PRODUCT_CODE"), TableDesc.getSelectedRow(), 5);  //11
                        TableDesc.setValueAt(trs.getString("PR_GROUP"), TableDesc.getSelectedRow(), 6);  //12
                        TableDesc.setValueAt(trs.getString("PR_STYLE"), TableDesc.getSelectedRow(), 7);  //13
                        TableDesc.setValueAt(df.format(trs.getDouble("PR_LENGTH")), TableDesc.getSelectedRow(), 8);  //14
                        TableDesc.setValueAt(df.format(trs.getDouble("PR_WIDTH")), TableDesc.getSelectedRow(), 9);  //15
                        TableDesc.setValueAt(trs.getString("PR_GSM"), TableDesc.getSelectedRow(), 10);  //16
                        if (PieceNo.contains("B") || PieceNo.contains("b")) {
                            TableDesc.setValueAt("0.00", TableDesc.getSelectedRow(), 11);  //17
                        } else {
                            TableDesc.setValueAt(df.format(trs.getDouble("PR_THORITICAL_WEIGHT")), TableDesc.getSelectedRow(), 11);  //17
                        }

                    } else {
                        trs = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE SUBSTRING(PR_PIECE_NO,1,5) = '" + PieceNo.substring(0, 5) + "'");
                        if (trs.getRow() > 0) {
                            JOptionPane.showMessageDialog(null, "Piece is alredy in " + trs.getString("PR_PIECE_STAGE") + " Stage...");
                            cmdItemdeleteActionPerformed(null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Piece No....");
                            cmdItemdeleteActionPerformed(null);
                        }
                    }
                }
                /*if (TableDesc.getSelectedColumn() == 3 || TableDesc.getSelectedColumn() == 6) {
                    System.out.println("SELECT AVAILABLE FROM PRODUCTION.GIDC_FELT_RM_STOCK WHERE ITEM_CODE='" + TableDesc.getValueAt(TableDesc.getSelectedRow(), TableDesc.getSelectedColumn() - 1).toString() + "'");
                    if (data.getDoubleValueFromDB("SELECT AVAILABLE FROM PRODUCTION.GIDC_FELT_RM_STOCK WHERE ITEM_CODE='" + TableDesc.getValueAt(TableDesc.getSelectedRow(), TableDesc.getSelectedColumn() - 1).toString() + "'") > 0) {
                        TableDesc.setValueAt(data.getStringValueFromDB("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ITEM_ID='" + TableDesc.getValueAt(TableDesc.getSelectedRow(), TableDesc.getSelectedColumn() - 1).toString() + "'"), TableDesc.getSelectedRow(), TableDesc.getSelectedColumn());
                    } else {
                        JOptionPane.showMessageDialog(null, "Stock Not Availble... Please Issue...");
                    }
                }*/
                //chkstk();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_TableDescKeyReleased

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        Updating = true;
        Object[] rowData = new Object[40];
        rowData[0] = Integer.toString(TableDesc.getRowCount() + 1);
        rowData[1] = "";
        DataModelDesc.addRow(rowData);
        Updating = false;
        UpdateSrNo();
        lblStatus.setText("Press F1 For Select Piece No.");
        TableDesc.changeSelection(TableDesc.getRowCount() - 1, 1, false, false);
        TableDesc.requestFocus();


    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdItemdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdItemdeleteActionPerformed
        if (TableDesc.getRowCount() > 0) {
            DataModelDesc.removeRow(TableDesc.getSelectedRow());
            UpdateSrNo();
        }
    }//GEN-LAST:event_cmdItemdeleteActionPerformed

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
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsGIDCInstruction.ModuleID, DocNo)) {
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

            exprt.fillData(TableDesc, new File(file1.getSelectedFile().toString() + ".xls"), "GIDC_INSTRUCTION");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_bexcelActionPerformed

    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed
        // TODO add your handling code here:
        try {
            if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID) && (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT)) {
                if (evt.getKeyCode() == 112) {
                    if (TableDesc.getSelectedColumn() == 1) {

                        LOV aList = new LOV();
                        aList.SQL = "SELECT PR_PIECE_NO AS PIECE,PR_GROUP AS ITEM_GROUP,PR_LENGTH AS LENGTH,PR_WIDTH AS WIDTH,PR_GSM AS GSM,PR_THORITICAL_WEIGHT AS WEIGHT,PR_PARTY_CODE AS PARTY,PARTY_NAME AS NAME,PR_OC_MONTHYEAR AS OC_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER LEFT JOIN D_SAL_PARTY_MASTER ON PR_PARTY_CODE=PARTY_CODE WHERE PR_GROUP='SDF' AND PR_PIECE_STAGE='PLANNING' AND PR_PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL WHERE COALESCE(INDICATOR,'')!='DELETE') AND PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) ";
                        aList.ReturnCol = 1;
                        aList.ShowReturnCol = true;
                        aList.DefaultSearchOn = 1;
                        if (aList.ShowLOV()) {
                            if (data.getStringValueFromDB("SELECT COALESCE(PR_OC_MONTHYEAR,'') FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + aList.ReturnVal + "'").equalsIgnoreCase("")) {
                                JOptionPane.showMessageDialog(this, "OC Month is compulsory.\n Please contact with Sales Person", "ERROR", JOptionPane.ERROR_MESSAGE);
                                return;
                            } else {
                                TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 1);
                                String sql = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                                        + "WHERE PR_GROUP='SDF' AND SUBSTRING(PR_PIECE_NO,1,5) = '" + aList.ReturnVal.substring(0, 5) + "' AND "
                                        + "PR_PIECE_STAGE='PLANNING' AND (PR_WARP_DATE='0000-00-00' OR PR_WARP_DATE IS NULL) "
                                        + "AND (PR_PIECE_NO NOT IN (SELECT LEFT(PIECE_NO,5) FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL WHERE INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD'))) ";
                                System.out.println("Check Piece : " + sql);
                                ResultSet trs = data.getResult(sql);
                                trs.first();
                                
                                TableDesc.setValueAt(trs.getString("PR_PARTY_CODE"), TableDesc.getSelectedRow(), 2); //9
                                TableDesc.setValueAt(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, trs.getString("PR_PARTY_CODE")), TableDesc.getSelectedRow(), 3); //10
                                TableDesc.setValueAt(trs.getString("PR_POSITION_NO"), TableDesc.getSelectedRow(), 4); 
                                TableDesc.setValueAt(trs.getString("PR_PRODUCT_CODE"), TableDesc.getSelectedRow(), 5); //11
                                TableDesc.setValueAt(trs.getString("PR_GROUP"), TableDesc.getSelectedRow(), 6); //12
                                TableDesc.setValueAt(trs.getString("PR_STYLE"), TableDesc.getSelectedRow(), 7);  //13
                                TableDesc.setValueAt(df.format(trs.getDouble("PR_LENGTH")), TableDesc.getSelectedRow(), 8);  //14
                                TableDesc.setValueAt(df.format(trs.getDouble("PR_WIDTH")), TableDesc.getSelectedRow(), 9);  //15
                                TableDesc.setValueAt(trs.getString("PR_GSM"), TableDesc.getSelectedRow(), 10);   //16                        
                                if (aList.ReturnVal.contains("B") || aList.ReturnVal.contains("b")) {
                                    TableDesc.setValueAt("0.00", TableDesc.getSelectedRow(), 11);  //17
                                } else {
                                    TableDesc.setValueAt(df.format(trs.getDouble("PR_THORITICAL_WEIGHT")), TableDesc.getSelectedRow(), 11);  //17
                                }

                            }
                        }

                    }

                    if (TableDesc.getSelectedColumn() == 2 || TableDesc.getSelectedColumn() == 5) {

                        LOV aList1 = new LOV();
                        aList1.SQL = "SELECT ITEM_ID,ITEM_DESCRIPTION,AVAILABLE AS \"AVAILABLE QUANTITY IN (KGS)\" FROM D_INV_ITEM_MASTER "
                                + "LEFT JOIN PRODUCTION.GIDC_FELT_RM_STOCK "
                                + "ON ITEM_ID=ITEM_CODE "
                                + "WHERE ITEM_ID LIKE 'RM1%' AND ITEM_ID IN (SELECT ITEM_CODE FROM PRODUCTION.GIDC_FELT_RM_STOCK WHERE COALESCE(AVAILABLE,0)>0) ";
                        aList1.ReturnCol = 1;
                        aList1.ShowReturnCol = true;
                        aList1.DefaultSearchOn = 1;
                        String val;
                        if (aList1.ShowLOV()) {
                            TableDesc.setValueAt(aList1.ReturnVal, TableDesc.getSelectedRow(), TableDesc.getSelectedColumn());
                            val = data.getStringValueFromDB("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ITEM_ID='" + aList1.ReturnVal + "'");
                            TableDesc.setValueAt(val, TableDesc.getSelectedRow(), TableDesc.getSelectedColumn() + 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_TableDescKeyPressed

    private void TableDescMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableDescMouseClicked
        // TODO add your handling code here:
        switch (TableDesc.getSelectedColumn()) {
            case 1:
                lblStatus.setText("Press F1 For Select Piece No.");
                break;
            case 2:
                lblStatus.setText("Press F1 For Select RM Code");
                break;
            case 4:
                lblStatus.setText("Enter Require Kg For Length");
                break;
            case 5:
                lblStatus.setText("Press F1 For Select RM Code");
                break;
            case 7:
                lblStatus.setText("Enter Require Kg For Width");
                break;
            case 8:
                lblStatus.setText("Enter Sequence No.");
                break;
            default:
                lblStatus.setText("");
                break;
        }
    }//GEN-LAST:event_TableDescMouseClicked

    private void TableDesc1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableDesc1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TableDesc1MouseClicked

    private void TableDesc1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDesc1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TableDesc1KeyPressed

    private void TableDesc1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDesc1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TableDesc1KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void bexcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bexcel1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bexcel1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApprovalPanel;
    private javax.swing.JTextField DOC_NO;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableDesc;
    private javax.swing.JTable TableDesc1;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton bexcel;
    private javax.swing.JButton bexcel1;
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
    private javax.swing.JFileChooser file1;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtdate;
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
        aList.ModuleID = 789;

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
            DOC_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 789, FFNo, false));
            txtdate.setText(EITLERPGLOBAL.getCurrentDate());
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

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsGIDCInstruction.ModuleID, (String) Obj.getAttribute("PROFORMA_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsGIDCInstruction.ModuleID, FromUserID, (String) Obj.getAttribute("PROFORMA_NO").getObj());

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

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsGIDCInstruction.ModuleID, DocNo);
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
        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        FormatGrid();
        FormatGridHis();
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
        String lDocNo = Obj.getAttribute("DOC_NO").getString();
        if (Obj.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (clsFeltProductionApprovalFlow.IsCreator(clsGIDCInstruction.ModuleID, lDocNo)) {  // || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8911, 89112)) {
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
//        if (!chkstk()) {
//            return;
//        }
        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Please select the hierarchy.");
            return;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null, "Please select the Approval Action");
            return;
        }

        if (TableDesc.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter some Instruction.");
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
            if (TableDesc.getValueAt(k, 1).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter Piece No at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (TableDesc.getValueAt(k, 8).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter Sequence No at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (TableDesc.getValueAt(k, 2).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter/Select RMCode[L] at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (TableDesc.getValueAt(k, 4).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter RMCode[L] Kg at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (TableDesc.getValueAt(k, 5).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter/Select RMCode[W] at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (TableDesc.getValueAt(k, 7).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter RMCode[W] Kg at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (data.getStringValueFromDB("SELECT PR_WIP_STATUS FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PIECE_NO='" + TableDesc.getValueAt(k, 1).toString().substring(0, 5) + "'").equalsIgnoreCase("CANCELED")) {
                JOptionPane.showMessageDialog(this, "Piece is Canceled at Row " + (k + 1) + " ... Please Remove Piece...", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

        }
        for (int k = 0; k <= TableDesc.getRowCount() - 1; k++) {
            for (int l = k; l <= TableDesc.getRowCount() - 1; l++) {
                if (l != k && (TableDesc.getValueAt(k, 1).toString()).trim().equals((TableDesc.getValueAt(l, 1).toString()).trim())) {
                    JOptionPane.showMessageDialog(this, "Same Piece No at Row " + (k + 1) + " and " + (l + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (l != k && (TableDesc.getValueAt(k, 8).toString()).trim().equals((TableDesc.getValueAt(l, 8).toString()).trim())) {
                    JOptionPane.showMessageDialog(this, "Same Sequence No at Row " + (k + 1) + " and " + (l + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        SetData();
        if (EditMode == EITLERPGLOBAL.ADD) {

            if (Obj.Insert()) {

            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving.  Error is " + Obj.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (Obj.Update()) {

            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving.  Error is " + Obj.LastError);
                return;
            }
        }
        Obj.Filter(" DOC_NO='" + DOC_NO.getText() + "'");
        DisplayData();
        //Mail();
        //Obj.LoadData(EITLERPGLOBAL.gCompanyID);
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
//        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.FeltWarpingBeamOrder.frmFindWarpingBeamOrder", true);
//        frmFindWarpingBeamOrder ObjReturn = (frmFindWarpingBeamOrder) ObjLoader.getObj();
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
            int ModuleID = clsGIDCInstruction.ModuleID;

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
            txtdate.setText(EITLERPGLOBAL.formatDate(Obj.getAttribute("DOC_DATE").getObj().toString()));
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, Obj.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table
            double mtottpick = 0;
            for (int i = 1; i <= Obj.colMRItems.size(); i++) {

                clsGIDCInstructionItem ObjItem = (clsGIDCInstructionItem) Obj.colMRItems.get(Integer.toString(i));
                Object[] rowData = new Object[50];

                rowData[0] = ObjItem.getAttribute("SR_NO").getInt();
                rowData[1] = (String) ObjItem.getAttribute("PIECE_NO").getObj();
                //rowData[2] = (String) ObjItem.getAttribute("RMCODE_LENGTH").getObj();
                //rowData[3] = data.getStringValueFromDB("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ITEM_ID='" + rowData[2].toString() + "'");
                //rowData[4] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("REQUIRE_QTY_LENGTH").getDouble(), 2));
                //rowData[5] = (String) ObjItem.getAttribute("RMCODE_WEFT").getObj();
                //rowData[6] = data.getStringValueFromDB("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ITEM_ID='" + rowData[5].toString() + "'");
                //rowData[7] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("REQUIRE_QTY_WEFT").getDouble(), 2));
                //rowData[8] = Integer.toString(ObjItem.getAttribute("SEQUANCE_NO").getInt());
                rowData[2] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();    //9
                rowData[3] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rowData[2].toString()); //10
                rowData[4] = data.getStringValueFromDB("SELECT PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO = '"+(String) ObjItem.getAttribute("PIECE_NO").getObj()+"' ");
                rowData[5] = (String) ObjItem.getAttribute("PRODUCT_CODE").getObj();  //11 
                rowData[6] = (String) ObjItem.getAttribute("PRODUCT_GROUP").getObj();  //12
                rowData[7] = (String) ObjItem.getAttribute("STYLE").getObj();  //13
                rowData[8] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("LENGTH").getDouble(), 2)); //14
                rowData[9] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("WIDTH").getDouble(), 2));  //15
                rowData[10] = Integer.toString((int) ObjItem.getAttribute("GSM").getDouble());  //16
                rowData[11] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("WEIGHT").getDouble(), 2));  //17
                rowData[12] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("GREY_LENGTH").getDouble(), 2));  //18
                rowData[13] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("GREY_WIDTH").getDouble(), 2));   //19

                DataModelDesc.addRow(rowData);
            }

            FormatGridHis();
            //Now Generate History Table
            ResultSet t;
            t = data.getResult("SELECT * FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL WHERE INDICATOR IN ('ADD','DELETE') AND DOC_NO='" + DOC_NO.getText() + "' ORDER BY INDICATOR");
            t.first();
            if (t.getRow() > 0) {
                while (!t.isAfterLast()) {
                    Object[] rowData = new Object[50];
                    rowData[0] = t.getInt("SR_NO");
                    rowData[1] = t.getString("INDICATOR");
                    rowData[2] = t.getString("INDICATOR_DOC");
                    rowData[3] = t.getString("PIECE_NO");
                    rowData[4] = t.getString("RMCODE_LENGTH");
                    rowData[5] = data.getStringValueFromDB("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ITEM_ID='" + rowData[4].toString() + "'");
                    rowData[6] = df.format(EITLERPGLOBAL.round(t.getDouble("REQUIRE_QTY_LENGTH"), 2));
                    rowData[7] = t.getString("RMCODE_WEFT");
                    rowData[8] = data.getStringValueFromDB("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ITEM_ID='" + rowData[7].toString() + "'");
                    rowData[9] = df.format(EITLERPGLOBAL.round(t.getDouble("REQUIRE_QTY_WEFT"), 2));
                    rowData[10] = t.getString("SEQUANCE_NO");
                    rowData[11] = t.getString("PARTY_CODE");
                    rowData[12] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rowData[11].toString());
                    rowData[13] = t.getString("PRODUCT_CODE");
                    rowData[14] = t.getString("PRODUCT_GROUP");
                    rowData[15] = t.getString("STYLE");
                    rowData[16] = df.format(EITLERPGLOBAL.round(t.getDouble("LENGTH"), 2));
                    rowData[17] = df.format(EITLERPGLOBAL.round(t.getDouble("WIDTH"), 2));
                    rowData[18] = t.getInt("GSM");
                    rowData[19] = df.format(EITLERPGLOBAL.round(t.getDouble("WEIGHT"), 2));
                    rowData[20] = df.format(EITLERPGLOBAL.round(t.getDouble("GREY_LENGTH"), 2));
                    rowData[21] = df.format(EITLERPGLOBAL.round(t.getDouble("GREY_WIDTH"), 2));

                    DataModelDesc1.addRow(rowData);
                    t.next();
                }
            }

            DoNotEvaluate = false;

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String DocNo = Obj.getAttribute("DOC_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsGIDCInstruction.ModuleID, DocNo);
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
            HashMap History = clsGIDCInstruction.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsGIDCInstruction ObjHistory = (clsGIDCInstruction) History.get(Integer.toString(i));
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
            e.printStackTrace();
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
        Obj.setAttribute("DOC_DATE", txtdate.getText());

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
            clsGIDCInstructionItem ObjItem = new clsGIDCInstructionItem();
            String lItemID = (String) TableDesc.getValueAt(i, 1);

            //Add Only Valid Items
            ObjItem.setAttribute("DOC_NO", DOC_NO.getText());
            ObjItem.setAttribute("DOC_DATE", txtdate.getText());
            ObjItem.setAttribute("SR_NO", TableDesc.getValueAt(i, 0).toString());
            ObjItem.setAttribute("PIECE_NO", lItemID);
            
            ObjItem.setAttribute("RMCODE_LENGTH", "");
            ObjItem.setAttribute("REQUIRE_QTY_LENGTH", 0.00);
            ObjItem.setAttribute("RMCODE_WEFT", "");
            ObjItem.setAttribute("REQUIRE_QTY_WEFT", 0.00);
            ObjItem.setAttribute("SEQUANCE_NO", 0);
            
//            ObjItem.setAttribute("RMCODE_LENGTH", TableDesc.getValueAt(i, 2).toString());
//            ObjItem.setAttribute("REQUIRE_QTY_LENGTH", Double.parseDouble(TableDesc.getValueAt(i, 4).toString()));
//            ObjItem.setAttribute("RMCODE_WEFT", TableDesc.getValueAt(i, 5).toString());
//            ObjItem.setAttribute("REQUIRE_QTY_WEFT", Double.parseDouble(TableDesc.getValueAt(i, 7).toString()));
//            ObjItem.setAttribute("SEQUANCE_NO", Integer.parseInt(TableDesc.getValueAt(i, 8).toString()));
            ObjItem.setAttribute("PARTY_CODE", TableDesc.getValueAt(i, 2).toString());  //9     
            ObjItem.setAttribute("PRODUCT_CODE", TableDesc.getValueAt(i, 5).toString());  //11
            ObjItem.setAttribute("PRODUCT_GROUP", TableDesc.getValueAt(i, 6).toString());  //12
            ObjItem.setAttribute("STYLE", TableDesc.getValueAt(i, 7).toString());  //13
            ObjItem.setAttribute("LENGTH", Double.parseDouble(TableDesc.getValueAt(i, 8).toString()));  //14
            ObjItem.setAttribute("WIDTH", Double.parseDouble(TableDesc.getValueAt(i, 9).toString()));  //15
            ObjItem.setAttribute("GSM", Double.parseDouble(TableDesc.getValueAt(i, 10).toString()));  //16
            ObjItem.setAttribute("WEIGHT", Double.parseDouble(TableDesc.getValueAt(i, 11).toString()));  //17
            ObjItem.setAttribute("GREY_LENGTH", Double.parseDouble(TableDesc.getValueAt(i, 12).toString()));  //18
            ObjItem.setAttribute("GREY_WIDTH", Double.parseDouble(TableDesc.getValueAt(i, 13).toString()));   //19
            Obj.colMRItems.put(Integer.toString(Obj.colMRItems.size() + 1), ObjItem);
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8911, 89111)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8911, 89112)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }
        //cmdEdit.setEnabled(true);

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8911, 89113)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8911, 89115)) {
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
        Obj.Filter(" DOC_NO IN (SELECT DISTINCT H.DOC_NO FROM PRODUCTION.GIDC_INSTRUCTION_DETAIL H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=789 AND CANCELED=0)");
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsGIDCInstruction.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsGIDCInstruction.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsGIDCInstruction.ModuleID, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    private void UpdateSrNo() {
        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            TableDesc.setValueAt(Integer.toString(i + 1), i, 0);
        }
    }

    private void FormatGrid() {

        Updating = true; //Stops recursion
        try {
            cmdAdd.requestFocus();

            DataModelDesc = new EITLTableModel();
            TableDesc.removeAll();
            TableDesc.setModel(DataModelDesc);
            TableColumnModel ColModel = TableDesc.getColumnModel();
            TableDesc.setAutoResizeMode(TableDesc.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);

            DataModelDesc.addColumn("Sr.");  //0 - Read Only
            DataModelDesc.addColumn("Piece No"); //1   
//            DataModelDesc.addColumn("RM Code[L]"); //2
//            DataModelDesc.addColumn("Description"); //3
//            DataModelDesc.addColumn("Require Kg[L]"); //4
//            DataModelDesc.addColumn("RM Code[W]"); //5
//            DataModelDesc.addColumn("Description"); //6
//            DataModelDesc.addColumn("Require Kg[W]"); //7
//            DataModelDesc.addColumn("Sequence No.");   //8   
            DataModelDesc.addColumn("Party Code"); //2   //9  
            DataModelDesc.addColumn("Name"); //3   //10
            DataModelDesc.addColumn("Position"); //4  //10
            DataModelDesc.addColumn("Product"); //5  //11
            DataModelDesc.addColumn("Group"); //6  //12
            DataModelDesc.addColumn("Style");  //7  //13
            DataModelDesc.addColumn("Length"); //8  //14
            DataModelDesc.addColumn("Width");  //9  //15
            DataModelDesc.addColumn("GSM");  //10  //16
            DataModelDesc.addColumn("Wt Kg");  //11    //17
            DataModelDesc.addColumn("Grey Len"); //12  //18
            DataModelDesc.addColumn("Grey Width"); //13    //19

            DataModelDesc.SetReadOnly(0);
            DataModelDesc.SetReadOnly(1);
            SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
            if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID) && (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT)) {

                DataModelDesc.ResetReadOnly(2);  
                //DataModelDesc.ResetReadOnly(3);
                DataModelDesc.ResetReadOnly(4);
                DataModelDesc.ResetReadOnly(5);
                //DataModelDesc.ResetReadOnly(6);
                DataModelDesc.ResetReadOnly(7);
                DataModelDesc.ResetReadOnly(8);
                DataModelDesc.ResetReadOnly(12);  //18
                DataModelDesc.ResetReadOnly(13);  //19

            } else {
                DataModelDesc.SetReadOnly(1);
                DataModelDesc.SetReadOnly(2);
                //DataModelDesc.SetReadOnly(3);
                DataModelDesc.SetReadOnly(4);
                DataModelDesc.SetReadOnly(5);
                //DataModelDesc.SetReadOnly(6);
                DataModelDesc.SetReadOnly(7);
                DataModelDesc.SetReadOnly(8);
                DataModelDesc.SetReadOnly(12);  //18
                DataModelDesc.SetReadOnly(13);  //19

            }
            //DataModelDesc.SetReadOnly(1);
            
            
            
            
            DataModelDesc.SetReadOnly(2);
            DataModelDesc.SetReadOnly(3);
            DataModelDesc.SetReadOnly(4);
            DataModelDesc.SetReadOnly(5);
            DataModelDesc.SetReadOnly(6);
            DataModelDesc.SetReadOnly(7);
            DataModelDesc.SetReadOnly(8);
            DataModelDesc.SetReadOnly(9);  
            DataModelDesc.SetReadOnly(10);
            DataModelDesc.SetReadOnly(11);
            //DataModelDesc.SetReadOnly(12);
            //DataModelDesc.SetReadOnly(13);
            //DataModelDesc.SetReadOnly(14);
            //DataModelDesc.SetReadOnly(15);
            //DataModelDesc.SetReadOnly(16);
            //DataModelDesc.SetReadOnly(17);

            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);

            TableDesc.getColumnModel().getColumn(10).setPreferredWidth(220);

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
        } catch (Exception e) {

        }
        Updating = false;
    }

    private void FormatGridHis() {

        Updating = true; //Stops recursion
        try {

            DataModelDesc1 = new EITLTableModel();
            TableDesc1.removeAll();
            TableDesc1.setModel(DataModelDesc1);
            TableColumnModel ColModel = TableDesc1.getColumnModel();
            TableDesc.setAutoResizeMode(TableDesc1.AUTO_RESIZE_OFF);
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);

            DataModelDesc1.addColumn("Sr.");  //0 - Read Only
            DataModelDesc1.addColumn("Status");  //1 - Read Only
            DataModelDesc1.addColumn("Doc No.");  //2 - Read Only
            DataModelDesc1.addColumn("Piece No"); //3
//            DataModelDesc1.addColumn("RM Code[L]"); //4
//            DataModelDesc1.addColumn("Description"); //5
//            DataModelDesc1.addColumn("Require Kg[L]"); //6
//            DataModelDesc1.addColumn("RM Code[W]"); //7
//            DataModelDesc1.addColumn("Description"); //8
//            DataModelDesc1.addColumn("Require Kg[W]"); //9
//            DataModelDesc1.addColumn("Sequence No.");   //10   
            DataModelDesc1.addColumn("Party Code");//11
            DataModelDesc1.addColumn("Name");//12
            DataModelDesc1.addColumn("Position");//12
            DataModelDesc1.addColumn("Product");//13
            DataModelDesc1.addColumn("Group"); //14
            DataModelDesc1.addColumn("Style");  //15
            DataModelDesc1.addColumn("Length"); //16
            DataModelDesc1.addColumn("Width"); //17
            DataModelDesc1.addColumn("GSM"); //18
            DataModelDesc1.addColumn("Wt Kg");   //19
            DataModelDesc1.addColumn("Grey Len");   //20
            DataModelDesc1.addColumn("Grey Width");   //21

            for (int k = 0; k <= 21; k++) {
                DataModelDesc1.SetReadOnly(k);
            }
            TableDesc1.getColumnModel().getColumn(0).setMaxWidth(50);
            TableDesc1.getColumnModel().getColumn(11).setPreferredWidth(220);

            final TableColumnModel columnModel = TableDesc1.getColumnModel();
            for (int column = 0; column < TableDesc1.getColumnCount(); column++) {
                int width = 100; // Min width
                for (int row = 0; row < TableDesc1.getRowCount(); row++) {
                    TableCellRenderer renderer = TableDesc1.getCellRenderer(row, column);
                    Component comp = TableDesc1.prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 1, width);
                }
                if (width > 300) {
                    width = 300;
                }
                columnModel.getColumn(column).setPreferredWidth(width);
            }
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
        try {
//            Connection Conn = EITLERP.data.getConn();
//            HashMap parameterMap = new HashMap();
//
//            EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(parameterMap, Conn);
//            String strSQL = "SELECT * FROM (SELECT H.DOC_NO,H.BEAM_NO,H.LOOM_NO,H.REED_SPACE,H.WARP_DETAIL,H.FABRIC_REALISATION_PER,H.WARP_TEX,(H.REED_SPACE*H.ENDS_10_CM)*10 AS WARP_END_TOTAL,H.ENDS_10_CM,H.ACTUAL_WARP_RELISATION,H.WARP_LENGTH,H.REED_COUNT, "
//                    + "SEQUANCE_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,READ_SPACE,THEORICAL_LENGTH_MTR,THEORICAL_PICKS_10_CM,TOTAL_PICKS,EXPECTED_GREV_SQ_MTR "
//                    + " FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D,PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER H "
//                    + "WHERE H.DOC_NO=D.DOC_NO AND "
//                    + "H.DOC_NO='" + DOC_NO.getText() + "' AND (D.INDICATOR IS NULL OR D.INDICATOR IN ('INSERT','ADD',''))) AS A "
//                    + "LEFT JOIN (SELECT DOC_NO,SUM(THEORICAL_LENGTH_MTR) AS TOTAL_PC_LENGTH FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL WHERE (INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD','')) AND DOC_NO='" + DOC_NO.getText() + "') AS B "
//                    + "ON A.DOC_NO=B.DOC_NO "
//                    + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS C "
//                    + "ON A.PARTY_CODE=C.PARTY_CODE "
//                    + "ORDER BY SEQUANCE_NO";
//            rpt.setReportName("/EITLERP/FeltSales/FeltWarpingBeamOrder/FELT_WARPING_BEAM_ORDER.jrxml", 1, strSQL); //productlist is the name of my jasper file.
//            rpt.callReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Mail() {
        System.out.println("Felt Warping Beam Order approved = " + Obj.getAttribute("APPROVAL_STATUS").getString());
        String pBody = "", pSubject = "", recievers = "", pcc = "";
        //if (Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("A") || Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {
        if (Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {
            if (Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("A")) {
                pSubject = "Notification : Felt Warping Beam Order No. " + DOC_NO.getText() + ".";
                pBody = "Felt Warping Beam Order No." + DOC_NO.getText() + " has been approved and forward  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID) + "<br><br><br>";
            }
            if (Obj.getAttribute("APPROVAL_STATUS").getString().equalsIgnoreCase("F")) {
                pSubject = "Notification : Felt Warping Beam Order No. :" + DOC_NO.getText() + " Final Approved";
                pBody = "Felt Warping Beam Order No. " + DOC_NO.getText() + " has been final approved  by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID) + "<br><br><br>";
            }
            try {
                pBody += "Document Name : Felt Warping Beam Order  <br>";
                pBody += "Document No.  : " + DOC_NO.getText() + " <br>";

                Connection tmConn;
                Statement tmstmt;
                ResultSet tmrsData;

                tmConn = data.getConn();
                tmstmt = tmConn.createStatement();
                tmrsData = tmstmt.executeQuery("SELECT  D.*,PARTY_NAME FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER P ON D.PARTY_CODE=P.PARTY_CODE WHERE DOC_NO='" + DOC_NO.getText() + "' ORDER BY SEQUANCE_NO ");
                tmrsData.first();
                pBody += "<br>";
                pBody += "<table border=1>";
                pBody += "<tr><td align='center'><b>Sr.No.</b></td>"
                        + "<td align='center'><b>Piece No.</b></td>"
                        + "<td align='center'><b>Party Code</b></td>"
                        + "<td align='center'><b>Party Name</b></td>"
                        + "<td align='center'><b>Product</b></td>"
                        + "<td align='center'><b>Group</b></td>"
                        + "<td align='center'><b>Style</b></td>"
                        + "<td align='center'><b>Fin Length</b></td>"
                        + "<td align='center'><b>Fin Width</b></td>"
                        + "<td align='center'><b>Fin GSM</b></td>"
                        + "<td align='center'><b>Fin Wt Kg</b></td>"
                        + "<td align='center'><b>Sequance No.</b></td>"
                        + "<td align='center'><b>R.Space on Loom</b></td>"
                        + "<td align='center'><b>Theo PC Len Mtr</b></td>"
                        + "<td align='center'><b>Theo Picks/10 cm</b></td>"
                        + "<td align='center'><b>Total Picks</b></td>"
                        + "<td align='center'><b>Expected Grev Sqmtr</b></td>"
                        + "</tr>";
                int j = 1;

                while (!tmrsData.isAfterLast()) {
                    pBody += "<tr>";
                    pBody += "<td>" + j + "</td>";
                    pBody += "<td>" + tmrsData.getString("PIECE_NO") + "</td>";
                    pBody += "<td>" + tmrsData.getString("PARTY_CODE") + "</td>";
                    pBody += "<td>" + tmrsData.getString("PARTY_NAME") + "</td>";
                    pBody += "<td>" + tmrsData.getString("PRODUCT_CODE") + "</td>";
                    pBody += "<td>" + tmrsData.getString("GRUP") + "</td>";
                    pBody += "<td>" + tmrsData.getString("STYLE") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("LENGTH") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("WIDTH") + "</td>";
                    pBody += "<td>" + tmrsData.getInt("GSM") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("WEIGHT") + "</td>";
                    pBody += "<td>" + tmrsData.getInt("SEQUANCE_NO") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("READ_SPACE") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("THEORICAL_LENGTH_MTR") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("THEORICAL_PICKS_10_CM") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("TOTAL_PICKS") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("EXPECTED_GREV_SQ_MTR") + "</td>";

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
                        + "<td align='center'><b>Date</b></td>"
                        + "<td align='center'><b>Status</b></td>"
                        + "<td align='center'><b>Remark</b></td>"
                        + "</tr>";

                HashMap hmApprovalHistory = clsGIDCInstruction.getHistoryList(EITLERPGLOBAL.gCompanyID, DOC_NO.getText());
                for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                    pBody += "<tr>";

                    clsGIDCInstruction ObjWarping = (clsGIDCInstruction) hmApprovalHistory.get(Integer.toString(i));
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
                recievers += ",yrpatel@dineshmills.com,amitkanti@dineshmills.com,abtewary@dineshmills.com";
                pBody = pBody + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

                String responce = MailNotification.sendNotificationMail(789, pSubject, pBody, recievers, pcc, EITLERPGLOBAL.getComboCode(cmbHierarchy));
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean chkstk() {
        try {
            Statement s;
            Connection connchk = data.getConn();
            s = connchk.createStatement();
            ResultSet r = null;

            HashMap<String, Double> hm;
            hm = new HashMap<>();
            String itmcd = "", errmsg = "";
            double stk = 0;
            for (int i = 0; i < TableDesc.getRowCount(); i++) {
                //Length RMCode
                itmcd = TableDesc.getValueAt(i, 2).toString();
                try {
                    stk = Double.parseDouble(TableDesc.getValueAt(i, 4).toString());
                } catch (Exception a) {
                    stk = 0;
                }
                if (stk > 0) {
                    if (hm.containsKey(itmcd)) {
                        if (hm.get(itmcd) >= stk) {
                            hm.put(itmcd, hm.get(itmcd) - stk);
                        } else {
                            errmsg = errmsg + "\n " + itmcd + " has not enough Stock... Please Issue...";
                        }
                    } else {
                        r = s.executeQuery("SELECT SUM(AVAILABLE) AS AVAILABLE_QTY FROM PRODUCTION.GIDC_FELT_RM_STOCK WHERE ITEM_CODE='" + itmcd + "' GROUP BY ITEM_CODE");
                        r.first();
                        if (r.getRow() > 0 && r.getDouble("AVAILABLE_QTY") >= stk) {
                            hm.put(itmcd, r.getDouble("AVAILABLE_QTY") - stk);
                        } else {
                            errmsg = errmsg + "\n " + itmcd + " has not enough Stock... Please Issue...";
                        }
                    }
                }
                //Width RMCode
                itmcd = TableDesc.getValueAt(i, 5).toString();
                try {
                    stk = Double.parseDouble(TableDesc.getValueAt(i, 7).toString());
                } catch (Exception a) {
                    stk = 0;
                }
                if (stk > 0) {
                    if (hm.containsKey(itmcd)) {
                        if (hm.get(itmcd) >= stk) {
                            hm.put(itmcd, hm.get(itmcd) - stk);
                        } else {
                            errmsg = errmsg + "\n " + itmcd + " has not enough Stock... Please Issue...";
                        }
                    } else {
                        r = s.executeQuery("SELECT SUM(AVAILABLE) AS AVAILABLE_QTY FROM PRODUCTION.GIDC_FELT_RM_STOCK WHERE ITEM_CODE='" + itmcd + "' GROUP BY ITEM_CODE");
                        r.first();
                        if (r.getRow() > 0 && r.getDouble("AVAILABLE_QTY") >= stk) {
                            hm.put(itmcd, r.getDouble("AVAILABLE_QTY") - stk);
                        } else {
                            errmsg = errmsg + "\n " + itmcd + " has not enough Stock... Please Issue...";
                        }
                    }
                }
            }
            //r.close();
            //s.close();
            //connchk.close();
            if (errmsg.trim().length() > 0) {
                JOptionPane.showMessageDialog(this, errmsg, "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
