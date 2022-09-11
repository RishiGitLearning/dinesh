/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP.FeltSales.Perfomainvoice;

/**
 *
 * @author ashutosh
 */
/*<APPLET CODE=frmSalesParty.class HEIGHT=574 WIDTH=758></APPLET>*/
import EITLERP.Production.ReportUI.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.FeltSales.common.JavaMail;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import java.lang.*;
import javax.swing.text.*;
import EITLERP.Finance.*;
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

//import EITLERP.Purchase.frmSendMail;
public class frmperfomainvoice extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsProforma ObjSalesParty;
    private clsProforma ObjSalesParty;
    private EITLERP.FeltSales.common.FeltInvCalc inv_calculation;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;

    private EITLTableModel DataModelDesc;
    private EITLTableModel DataModelDiscount;
    private EITLTableModel DataModelA;
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

    /**
     * Creates new form frmSalesParty
     */
    public frmperfomainvoice() {
        System.gc();
        setSize(800, 800);
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
        cmdEmail.setIcon(EITLERPGLOBAL.getImage("EMAIL"));

        GenerateCombos();

        ObjSalesParty = new clsProforma();

        SetMenuForRights();

        if (ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID)) {
            ObjSalesParty.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data. \n Error is " + ObjSalesParty.LastError);
        }

        txtAuditRemarks.setVisible(false);
        DataModelDesc.TableReadOnly(true);
        txtPartycode.setEditable(false);

        txtremark1.setEditable(false);
        txtremark2.setEditable(false);
        txtremark3.setEditable(false);
        txtremark4.setEditable(false);
        txtremark5.setEditable(false);

        GeneratePreviousDiscount();
        cmdPreview.setEnabled(true);

    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsProforma.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsProforma.ModuleID + "");
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableDiscount = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        txtPartycode = new javax.swing.JTextField();
        txtPartyname = new javax.swing.JTextField();
        txtPartystation = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtProformano = new javax.swing.JTextField();
        txtProformaDate = new javax.swing.JTextField();
        txtContact = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblremark1 = new javax.swing.JLabel();
        lblremark2 = new javax.swing.JLabel();
        lblremark3 = new javax.swing.JLabel();
        lblremark4 = new javax.swing.JLabel();
        lblremark5 = new javax.swing.JLabel();
        txtremark1 = new javax.swing.JTextField();
        txtremark2 = new javax.swing.JTextField();
        txtremark3 = new javax.swing.JTextField();
        txtremark4 = new javax.swing.JTextField();
        txtremark5 = new javax.swing.JTextField();
        lblproformano = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblRevNo = new javax.swing.JLabel();

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

        lblTitle.setBackground(new java.awt.Color(211, 221, 225));
        lblTitle.setText("FELT PROFORMA INVOICE");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 800, 25);

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
        jScrollPane1.setBounds(20, 50, 720, 240);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        Tab1.add(cmdAdd);
        cmdAdd.setBounds(570, 10, 80, 29);

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
        jButton1.setBounds(640, 300, 90, 29);

        jTabbedPane1.addTab("Piece Detail", Tab1);

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

        buttonGroup1.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 169, 22);

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
        cmdNext3.setBounds(550, 300, 102, 33);

        jButton3.setText("Next >>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(jButton3);
        jButton3.setBounds(650, 300, 100, 29);

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
        jButton4.setBounds(670, 290, 68, 29);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(570, 290, 90, 33);

        jTabbedPane1.addTab("Status", StatusPanel);

        jPanel1.setLayout(null);

        TableDiscount.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(TableDiscount);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(20, 50, 720, 240);

        jButton6.setText("<<Previous");
        jButton6.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);
        jButton6.setBounds(648, 300, 100, 33);

        jTabbedPane1.addTab("Previous Discount", jPanel1);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(5, 270, 810, 380);

        txtPartycode.setEnabled(false);
        txtPartycode = new JTextFieldHint(new JTextField(),"Search by F1");
        txtPartycode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartycodeFocusLost(evt);
            }
        });
        txtPartycode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartycodeKeyPressed(evt);
            }
        });
        getContentPane().add(txtPartycode);
        txtPartycode.setBounds(130, 80, 110, 20);

        txtPartyname.setEditable(false);
        txtPartyname.setBackground(new java.awt.Color(204, 204, 204));
        //txtPartyname = new JTextFieldHint(new JTextField(),"Party Name");
        getContentPane().add(txtPartyname);
        txtPartyname.setBounds(260, 80, 360, 27);

        txtPartystation.setEditable(false);
        txtPartystation.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(txtPartystation);
        txtPartystation.setBounds(660, 80, 130, 27);

        jLabel8.setText("Party Code");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(43, 80, 70, 20);

        txtProformano.setEditable(false);
        txtProformano.setEnabled(false);
        getContentPane().add(txtProformano);
        txtProformano.setBounds(130, 100, 110, 27);

        txtProformaDate.setEditable(false);
        txtProformaDate.setEnabled(false);
        getContentPane().add(txtProformaDate);
        txtProformaDate.setBounds(130, 130, 110, 27);

        txtContact.setEnabled(false);
        getContentPane().add(txtContact);
        txtContact.setBounds(350, 130, 220, 27);

        txtPhone.setEnabled(false);
        txtPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPhoneFocusLost(evt);
            }
        });
        getContentPane().add(txtPhone);
        txtPhone.setBounds(630, 130, 170, 30);

        jLabel1.setText("Proforma Date");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 140, 100, 20);

        jLabel2.setText("Contact");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(290, 130, 50, 20);

        jLabel3.setText("Phone");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(580, 130, 40, 20);

        lblremark1.setText("Remark 1");
        getContentPane().add(lblremark1);
        lblremark1.setBounds(50, 160, 67, 20);

        lblremark2.setText("Remark 2");
        getContentPane().add(lblremark2);
        lblremark2.setBounds(50, 180, 67, 20);

        lblremark3.setText("Remark 3");
        getContentPane().add(lblremark3);
        lblremark3.setBounds(50, 200, 67, 20);

        lblremark4.setText("Remark 4");
        getContentPane().add(lblremark4);
        lblremark4.setBounds(50, 220, 67, 20);

        lblremark5.setText("Remark 5");
        getContentPane().add(lblremark5);
        lblremark5.setBounds(50, 240, 67, 20);
        getContentPane().add(txtremark1);
        txtremark1.setBounds(130, 160, 680, 20);
        getContentPane().add(txtremark2);
        txtremark2.setBounds(130, 180, 680, 20);
        getContentPane().add(txtremark3);
        txtremark3.setBounds(130, 200, 680, 20);
        getContentPane().add(txtremark4);
        txtremark4.setBounds(130, 220, 680, 20);
        getContentPane().add(txtremark5);
        txtremark5.setBounds(130, 240, 680, 20);

        lblproformano.setText("Proforma No.");
        getContentPane().add(lblproformano);
        lblproformano.setBounds(30, 110, 90, 20);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(10, 660, 770, 20);

        lblRevNo.setText(".....");
        getContentPane().add(lblRevNo);
        lblRevNo.setBounds(250, 110, 40, 17);
    }// </editor-fold>//GEN-END:initComponents

    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed

        if (evt.getKeyCode() == 112) //F1 Key pressed
        {

            if (TableDesc.getSelectedColumn() == 1) {
                LOV aList = new LOV();

               // String strSQL = "SELECT PR_PIECE_NO,PR_PRODUCT_CODE,PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + txtPartycode.getText() + "'";
                String strSQL = "SELECT PR_PIECE_NO,PR_BILL_PRODUCT_CODE,PR_PARTY_CODE,PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + txtPartycode.getText() + "'  AND PR_MACHINE_NO NOT IN ('',0) AND PR_MACHINE_NO IS NOT NULL AND PR_POSITION_NO NOT IN ('',0) AND PR_POSITION_NO IS NOT NULL";

                aList.SQL = strSQL;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;

                if (aList.ShowLOV()) {

                    if (TableDesc.getCellEditor() != null) {
                        TableDesc.getCellEditor().stopCellEditing();
                    }
                    TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 1);

                    //   inv_calculation = EITLERP.FeltSales.Order.clsOrderValue.calculate(theDocNo, theDocNo, strSQL, TOP_ALIGNMENT, TOP_ALIGNMENT, FFNo, TOP_ALIGNMENT, TOP_ALIGNMENT, strSQL)
                }

            }

        }


    }//GEN-LAST:event_TableDescKeyPressed

    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased

        String PieceNo = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 1);

        String ppiecedec = clsProforma.getpiecedec(PieceNo);
        TableDesc.setValueAt(ppiecedec, TableDesc.getSelectedRow(), 2);
        String pposition = clsProforma.getposition(PieceNo);
        TableDesc.setValueAt(pposition, TableDesc.getSelectedRow(), 3);
        String plength = clsProforma.getlength(PieceNo);
        TableDesc.setValueAt(plength, TableDesc.getSelectedRow(), 4);
        String pwidth = clsProforma.getwidth(PieceNo);
        TableDesc.setValueAt(pwidth, TableDesc.getSelectedRow(), 5);
        String pgsm = clsProforma.getgsm(PieceNo);
        TableDesc.setValueAt(pgsm, TableDesc.getSelectedRow(), 6);
        String pstyle = clsProforma.getstyle(PieceNo);
        TableDesc.setValueAt(pstyle, TableDesc.getSelectedRow(), 7);
        String pweight = clsProforma.getweight(PieceNo);
        //TableDesc.setValueAt(pweight, TableDesc.getSelectedRow(), 8);
        String psqmtr = clsProforma.getsqmtr(PieceNo);
        //TableDesc.setValueAt(psqmtr, TableDesc.getSelectedRow(), 9);
        String pmachienno = clsProforma.getmachienno(PieceNo);
        TableDesc.setValueAt(pmachienno, TableDesc.getSelectedRow(), 25);
        String porderdate = EITLERPGLOBAL.formatDate(clsProforma.getorderdate(PieceNo));
        TableDesc.setValueAt(porderdate, TableDesc.getSelectedRow(), 26);
        String pincharge = clsProforma.getincharge(PieceNo);
        TableDesc.setValueAt(pincharge, TableDesc.getSelectedRow(), 27);
        String pproductcode = clsProforma.getproductcode(PieceNo);
        TableDesc.setValueAt(pproductcode, TableDesc.getSelectedRow(), 28);
        String ppono = clsProforma.getpono(PieceNo);
        TableDesc.setValueAt(ppono, TableDesc.getSelectedRow(), 29);
        String ppodate = EITLERPGLOBAL.formatDate(clsProforma.getpodate(PieceNo));
        TableDesc.setValueAt(ppodate, TableDesc.getSelectedRow(), 30);

        float actual_weight = 0;
        if (pproductcode.startsWith("71") || pproductcode.startsWith("72")) {
            actual_weight = Float.parseFloat(plength) * Float.parseFloat(pwidth);

            TableDesc.setValueAt(actual_weight, TableDesc.getSelectedRow(), 9);
        } else {

            TableDesc.setValueAt(pweight, TableDesc.getSelectedRow(), 8);
        }

        if (pgsm == null) {
            pgsm = "0";
        }
        if (pweight == null) {
            pweight = "0";
        }
        if (psqmtr == null) {
            psqmtr = "0";
        }
        //
//        System.out.println("Data : " + PieceNo + "," + pproductcode + "," + txtPartycode.getText() + "," + plength + "," + pwidth + "," + pgsm + "," + pweight + "," + psqmtr + "," + porderdate + "");
        inv_calculation = EITLERP.FeltSales.common.clsOrderValueCalc.calculate(PieceNo, pproductcode, txtPartycode.getText(), Float.parseFloat(plength), Float.parseFloat(pwidth), Float.parseFloat(pweight), Float.parseFloat(psqmtr), EITLERPGLOBAL.getCurrentDateDB(), "");

        if (inv_calculation.getReason().equals("")) {
            TableDesc.setValueAt(inv_calculation.getFicGrossRate(), TableDesc.getSelectedRow(), 10);
            TableDesc.setValueAt(inv_calculation.getFicBasAmount(), TableDesc.getSelectedRow(), 11);
            TableDesc.setValueAt(inv_calculation.getFicChemTrtChg(), TableDesc.getSelectedRow(), 12);
            TableDesc.setValueAt(inv_calculation.getFicPinChg(), TableDesc.getSelectedRow(), 13);
            TableDesc.setValueAt(inv_calculation.getFicSpiralChg(), TableDesc.getSelectedRow(), 14);
            TableDesc.setValueAt(inv_calculation.getFicDiscPer(), TableDesc.getSelectedRow(), 15);
            TableDesc.setValueAt(inv_calculation.getFicDiscAmt(), TableDesc.getSelectedRow(), 16);

            TableDesc.setValueAt(inv_calculation.getAosd_per(), TableDesc.getSelectedRow(), 17);
            TableDesc.setValueAt(inv_calculation.getAosd_amt(), TableDesc.getSelectedRow(), 18);

            TableDesc.setValueAt(inv_calculation.getFicDiscBasamt(), TableDesc.getSelectedRow(), 19);
            TableDesc.setValueAt(inv_calculation.getFicExcise(), TableDesc.getSelectedRow(), 20);
            TableDesc.setValueAt(inv_calculation.getFicSeamChg(), TableDesc.getSelectedRow(), 21);
            TableDesc.setValueAt(inv_calculation.getFicInsAmt(), TableDesc.getSelectedRow(), 22);
            TableDesc.setValueAt(inv_calculation.getFicInvAmt(), TableDesc.getSelectedRow(), 23);
            TableDesc.setValueAt(inv_calculation.getFicInsInd(), TableDesc.getSelectedRow(), 24);
            TableDesc.setValueAt(inv_calculation.getVat(), TableDesc.getSelectedRow(), 31);
            TableDesc.setValueAt(inv_calculation.getCst(), TableDesc.getSelectedRow(), 32);
            TableDesc.setValueAt(inv_calculation.getSD(), TableDesc.getSelectedRow(), 33);
            TableDesc.setValueAt(inv_calculation.getFicCGST(), TableDesc.getSelectedRow(), 37);
            TableDesc.setValueAt(inv_calculation.getFicSGST(), TableDesc.getSelectedRow(), 38);
            TableDesc.setValueAt(inv_calculation.getFicIGST(), TableDesc.getSelectedRow(), 39);

            TableDesc.setValueAt(inv_calculation.getTCS_per(), TableDesc.getSelectedRow(), 40);
            TableDesc.setValueAt(inv_calculation.getTCS_amt(), TableDesc.getSelectedRow(), 41);
        } else {
            JOptionPane.showMessageDialog(null, inv_calculation.getReason());
            return;
        }

        double d1 = 0, d2 = 0;
        double s1 = 0, s2 = 0, s3 = 0;

        String strSQL_NEW = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE!='IN STOCK' AND PR_PIECE_NO='" + PieceNo + "'";
        System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE!='IN STOCK' AND PR_PIECE_NO='" + PieceNo + "'");
        try {
            ResultSet rsTmp = data.getResult(strSQL_NEW);
            rsTmp.first();

            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                d1 = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 23).toString());
                d2 = (d1) * 5 / 100;
                    //JOptionPane.showMessageDialog(null, "Data D1 : "+d1+ " , D2 = "+d2);

                s3 = (d1 + d2);
                rsTmp.next();
            }

        } catch (Exception e) {

        }

        TableDesc.setValueAt(d2, TableDesc.getSelectedRow(), 34);
        TableDesc.setValueAt(s3, TableDesc.getSelectedRow(), 35);

        double a1, a2, a3;
        a1 = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 23).toString());
        a2 = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 33).toString());
        a3 = (a1 + a2);

        TableDesc.setValueAt(a3, TableDesc.getSelectedRow(), 36);

        String UOM = "";
        if (pproductcode.startsWith("71") || pproductcode.startsWith("72")) {
            UOM = "SQM";
        } else {
            UOM = "Kg";
        }
        TableDesc.setValueAt(UOM, TableDesc.getSelectedRow(), 42);

        TableDesc.setValueAt(inv_calculation.getFicRate(), TableDesc.getSelectedRow(), 43);
        TableDesc.setValueAt(inv_calculation.getFicSurcharge_per(), TableDesc.getSelectedRow(), 44);
        TableDesc.setValueAt(inv_calculation.getFicSurcharge_rate(), TableDesc.getSelectedRow(), 45);

    }//GEN-LAST:event_TableDescKeyReleased

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed

        if (txtPartycode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Party Code Detail First.");
            return;
        } else {

            Updating = true;
            Object[] rowData = new Object[50];
            rowData[0] = Integer.toString(TableDesc.getRowCount() + 1);
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
            rowData[35] = "";
            rowData[36] = "";
            rowData[37] = "";
            rowData[38] = "";
            rowData[39] = "";
            rowData[40] = "";
            rowData[41] = "";
            rowData[42] = "";
            rowData[43] = "";
            rowData[44] = "";
            rowData[45] = "";

            DataModelDesc.addRow(rowData);
            Updating = false;
            TableDesc.changeSelection(TableDesc.getRowCount() - 1, 1, false, false);
            TableDesc.requestFocus();
        }

    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdItemdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdItemdeleteActionPerformed
        if (TableDesc.getRowCount() > 0) {
            DataModelDesc.removeRow(TableDesc.getSelectedRow());
            // DisplayIndicators();
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
        String PartyID = txtPartycode.getText().trim();

        SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsProforma.ModuleID, PartyID)) {
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
        String ProformaNo = txtProformano.getText();
        ObjSalesParty.ShowHistory(EITLERPGLOBAL.gCompanyID, ProformaNo);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID);
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

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void cmdEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEmailActionPerformed
        if (EditMode == 0) {
            frmFeltSendMail ObjSend = new frmFeltSendMail();
            //ObjSend.ModuleID=20+POType;
            ObjSend.ModuleID = 708;

            ObjSend.SentBy = EITLERPGLOBAL.gNewUserID;
            ObjSend.MailDocNo = txtProformano.getText().trim();

            try {
//                String.valueOf(TOP_ALIGNMENT)
                if (!txtPartycode.getText().equals("")) {
                    String strSQL = "";
                    ResultSet rsTmp;
                    strSQL = "";
//                    strSQL += "SELECT EMAIL FROM PRODUCTION.FELT_PARTY_EXTRA_INFO WHERE PARTY_CODE = " + txtPartycode.getText().trim() + "";
                    strSQL += "SELECT CONCAT(EMAIL,',',EMAIL_ID2,',',EMAIL_ID3) EMAIL FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE = " + txtPartycode.getText().trim() + "";
                    rsTmp = data.getResult(strSQL);
                    rsTmp.first();

                    ObjSend.MailTo = rsTmp.getString("EMAIL");
                }

            } catch (Exception e) {
            }

            String FileName = "doc" + ObjSend.ModuleID + txtProformano.getText() + ".pdf";
            ObjSend.theFile = FileName;

            ObjSend.colRecList.clear();
            //ObjSend.colRecList.put(Integer.toString(ObjSend.colRecList.size()+1),suppEMail);

            int FinalApprover = 0;
            String strFinalApprover = "";
            int HierarchyID = (int) ObjSalesParty.getAttribute("HIERARCHY_ID").getVal();

            FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
            strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

            try {
                URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/rptProformamail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&DocNo=" + txtProformano.getText() + "&ProformaDate=" + EITLERPGLOBAL.formatDateDB(txtProformaDate.getText()) + "&File=" + FileName);
                System.out.println(MailDocument);
                MailDocument.openConnection();
                MailDocument.openStream();
            } catch (Exception e) {
            }
            ObjSend.ShowWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Cannot send email while in edit mode. Please save the document and then try");
        }
    }//GEN-LAST:event_cmdEmailActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed

    }//GEN-LAST:event_cmdPrintActionPerformed

    private void txtPartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartycodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

//            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' ORDER BY PARTY_NAME";
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CLOSE_IND!=1 ORDER BY PARTY_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtPartycode.setText(aList.ReturnVal);
                txtPartyname.setText(clsFeltparty.getParyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
                txtPartystation.setText(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
                txtContact.setText(clsFeltparty.getContactNo(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
            txtPhone.setText(clsFeltparty.getPhone(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
        }
    }//GEN-LAST:event_txtPartycodeKeyPressed

    private void txtPartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartycodeFocusLost
        try {
            if (!txtPartycode.getText().equals("")) {
                String strSQL = "";
                ResultSet rsTmp;
                strSQL = "";
                strSQL += "SELECT PARTY_NAME,DISPATCH_STATION,MOBILE_NO,PHONE_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + txtPartycode.getText().trim() + "";
                rsTmp = data.getResult(strSQL);
                rsTmp.first();
                txtPartyname.setText(rsTmp.getString("PARTY_NAME"));
                txtPartystation.setText(rsTmp.getString("DISPATCH_STATION"));
                txtContact.setText(rsTmp.getString("MOBILE_NO"));
                txtPhone.setText(rsTmp.getString("PHONE_NO"));
                txtContact.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter valid party code");
            txtPartycode.requestFocus();

        }
        GeneratePreviousDiscount();

    }//GEN-LAST:event_txtPartycodeFocusLost

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        MoveFirst();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdTopActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        MovePrevious();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        MoveNext();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        MoveLast();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        Add();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        Edit();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        Delete();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        Save();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        Cancel();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        Find();
        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        ObjSalesParty.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void txtPhoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPhoneFocusLost
        txtremark1.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApprovalPanel;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JPanel Tab1;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableDesc;
    private javax.swing.JTable TableDiscount;
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
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblproformano;
    private javax.swing.JLabel lblremark1;
    private javax.swing.JLabel lblremark2;
    private javax.swing.JLabel lblremark3;
    private javax.swing.JLabel lblremark4;
    private javax.swing.JLabel lblremark5;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtPartycode;
    private javax.swing.JTextField txtPartyname;
    private javax.swing.JTextField txtPartystation;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtProformaDate;
    private javax.swing.JTextField txtProformano;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtremark1;
    private javax.swing.JTextField txtremark2;
    private javax.swing.JTextField txtremark3;
    private javax.swing.JTextField txtremark4;
    private javax.swing.JTextField txtremark5;
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
        aList.ModuleID = 708;
        txtPartycode.setEditable(true);
        txtremark1.setEditable(true);
        txtremark2.setEditable(true);
        txtremark3.setEditable(true);
        txtremark4.setEditable(true);
        txtremark5.setEditable(true);

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
            txtProformano.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 708, FFNo, false));
            txtPartycode.requestFocus();

            lblTitle.setText("FELT PROFORMA INVOICE - " + txtProformano.getText());
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

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsProforma.ModuleID, (String) ObjSalesParty.getAttribute("PROFORMA_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsProforma.ModuleID, FromUserID, (String) ObjSalesParty.getAttribute("PROFORMA_NO").getObj());

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

                String ProformaNo = (String) ObjSalesParty.getAttribute("PROFORMA_NO").getObj();

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsProforma.ModuleID, ProformaNo);
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
        txtPartycode.setEnabled(pStat);
        txtPartyname.setEnabled(pStat);
        txtPartystation.setEnabled(pStat);
        txtProformano.setEnabled(pStat);
        txtProformaDate.setEnabled(pStat);
        txtContact.setEnabled(pStat);
        txtPhone.setEnabled(pStat);
        cmdAdd.setEnabled(pStat);
        cmdItemdelete.setEnabled(pStat);
        // cmdEditPiecedetail.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);

        txtToRemarks.setEnabled(pStat);
        txtremark1.setEnabled(pStat);
        txtremark2.setEnabled(pStat);
        txtremark3.setEnabled(pStat);
        txtremark4.setEnabled(pStat);
        txtremark5.setEnabled(pStat);

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

        if (txtPartycode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Party Code");
            txtPartycode.requestFocus(true);
            return false;
        }
        //Now Header level validations
        if (txtPartycode.getText().trim().equals("") && OpgFinal.isSelected()) {
            JOptionPane.showMessageDialog(null, "Please enter Party Code");
            txtPartycode.requestFocus(true);
            return false;
        }

        if (!txtPartycode.getText().trim().equals("")) {
            if (EditMode == EITLERPGLOBAL.ADD) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='" + txtProformano.getText().trim() + "'")) {
                    JOptionPane.showMessageDialog(null, "Party Code already exists!!");
                    txtPartycode.requestFocus(true);
                    return false;
                }
            }
        }

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
            if (txtPartycode.getText().trim().substring(0, 4).equals("NEWD")) {
                JOptionPane.showMessageDialog(null, "Invalid Party Code");
                txtPartycode.requestFocus(true);
                return false;
            }
        }
        return true;
    }

    private void ClearFields() {
        txtPartycode.setText("");
        txtPartyname.setText("");
        txtPartystation.setText("");
        txtProformano.setText("");
        txtProformaDate.setText(EITLERPGLOBAL.getCurrentDate());
        txtContact.setText("");
        txtPhone.setText("");
        txtremark1.setText("");
        txtremark2.setText("");
        txtremark3.setText("");
        txtremark4.setText("");
        txtremark5.setText("");
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
        String lProformaNo = ObjSalesParty.getAttribute("PROFORMA_NO").getString();
        if (ObjSalesParty.IsEditable(EITLERPGLOBAL.gCompanyID, lProformaNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (clsFeltProductionApprovalFlow.IsCreator(clsProforma.ModuleID, lProformaNo) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6058, 60582)) {
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

        String lDocNo = (String) ObjSalesParty.getAttribute("PROFORMA_NO").getObj();

        if (ObjSalesParty.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            if (ObjSalesParty.Delete(EITLERPGLOBAL.gNewUserID)) {
                MoveLast();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while deleting. \nError is " + ObjSalesParty.LastError);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
        }
    }

    private void Save() {
        //Form level validations
        if (txtProformaDate.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Proforma date");
            return;
        }

        String partyCode = txtPartycode.getText();
        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + partyCode + "' ")) {
            JOptionPane.showMessageDialog(null, "Party closed in Party Master.");
            return;
        }

        for (int j = 0; j < TableDesc.getRowCount(); j++) {
            String machineNo = ((String) TableDesc.getValueAt(j, 25)).trim();
            String positionNo = "SELECT B.MM_MACHINE_POSITION FROM PRODUCTION.FELT_SALES_PIECE_REGISTER A, PRODUCTION.FELT_MACHINE_MASTER_DETAIL B WHERE A.PR_POSITION_NO=B.MM_MACHINE_POSITION AND A.PR_MACHINE_NO=B.MM_MACHINE_NO AND PR_PIECE_NO='" + ((String) TableDesc.getValueAt(j, 1)) + "'";

            if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MACHINE_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCode + "' AND MM_MACHINE_NO='" + machineNo + "' ")) {
                JOptionPane.showMessageDialog(null, "Party Machine closed in Machine Master at Row : " + (j + 1));
                return;
            } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE POSITION_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCode + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "' ")) {
                JOptionPane.showMessageDialog(null, "Party Machine Position closed in Machine Master at Row : " + (j + 1));
                return;
            }

        }

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
            for (int l = k; l <= TableDesc.getRowCount() - 1; l++) {
                if (l != k && ((String) TableDesc.getValueAt(k, 1)).trim().equals(((String) TableDesc.getValueAt(l, 1)).trim())) {
                    JOptionPane.showMessageDialog(this, "Same Piece No at Row " + (k + 1) + " and " + (l + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            float cgst = Float.parseFloat(TableDesc.getValueAt(i, 37) + "");
            float sgst = Float.parseFloat(TableDesc.getValueAt(i, 38) + "");
            float igst = Float.parseFloat(TableDesc.getValueAt(i, 39) + "");
            if ((cgst > 0 && sgst > 0) || igst > 0) {

            } else {
                JOptionPane.showMessageDialog(null, "PLease check party master GST detail");
                return;
            }
        }

        SetData();
        if (EditMode == EITLERPGLOBAL.ADD) {

            if (ObjSalesParty.Insert()) {

                if (OpgFinal.isSelected()) {
                    try {
                        String DOC_NO = txtProformano.getText();
                        String DOC_DATE = txtProformaDate.getText();
                        String Party_Code = txtPartycode.getText();

                        String responce = JavaMail.sendFinalApprovalMail(clsProforma.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
                        System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjSalesParty.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjSalesParty.Update()) {

                if (OpgFinal.isSelected()) {
                    try {
                        String DOC_NO = txtProformano.getText();
                        String DOC_DATE = txtProformaDate.getText();
                        String Party_Code = txtPartycode.getText();

                        String responce = JavaMail.sendFinalApprovalMail(clsProforma.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
                        System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjSalesParty.LastError);
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
        Loader ObjLoader = new Loader(this, "EITLERP.Production.ReportUI.frmProformaFind", true);
        frmProformaFind ObjReturn = (frmProformaFind) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            if (!ObjSalesParty.Filter(ObjReturn.stringFindQuery)) {
                JOptionPane.showMessageDialog(null, "No records found.");
            }
            MoveFirst();
        }
    }

    private void MoveFirst() {
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjSalesParty.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjSalesParty.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjSalesParty.MoveLast();
        DisplayData();
    }

    //Didplay data on the Screen
    private void DisplayData() {
        //=========== Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (ObjSalesParty.getAttribute("APPROVED").getInt() == 1) {

                    lblTitle.setBackground(Color.BLUE);
                    cmdEmail.setEnabled(true);
                    cmdPreview.setEnabled(true);
                }

                if (ObjSalesParty.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                    cmdEmail.setEnabled(false);
                    cmdPreview.setEnabled(false);
                }

                if (ObjSalesParty.getAttribute("CANCELED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                    cmdEmail.setEnabled(false);
                    cmdPreview.setEnabled(false);
                }
            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = clsProforma.ModuleID;

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        try {
            ClearFields();
            lblTitle.setText("FELT PROFORMA INVOICE - " + (String) ObjSalesParty.getAttribute("PROFORMA_NO").getObj());
            lblRevNo.setText(Integer.toString((int) ObjSalesParty.getAttribute("REVISION_NO").getVal()));
            txtPartycode.setText((String) ObjSalesParty.getAttribute("PARTY_CD").getObj());
            txtPartyname.setText((String) ObjSalesParty.getAttribute("NAME").getObj());
            txtPartystation.setText((String) ObjSalesParty.getAttribute("STATION").getObj());
            txtProformano.setText((String) ObjSalesParty.getAttribute("PROFORMA_NO").getObj());
            txtProformaDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("PROFORMA_DATE").getString()));
            txtContact.setText(ObjSalesParty.getAttribute("CONTACT").getString());
            txtremark1.setText(ObjSalesParty.getAttribute("REMARK1").getString());
            txtremark2.setText(ObjSalesParty.getAttribute("REMARK2").getString());
            txtremark3.setText(ObjSalesParty.getAttribute("REMARK3").getString());
            txtremark4.setText(ObjSalesParty.getAttribute("REMARK4").getString());
            txtremark5.setText(ObjSalesParty.getAttribute("REMARK5").getString());

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table
            for (int i = 1; i <= ObjSalesParty.colMRItems.size(); i++) {

                clsProformaItem ObjItem = (clsProformaItem) ObjSalesParty.colMRItems.get(Integer.toString(i));
                Object[] rowData = new Object[50];

                rowData[0] = Integer.toString(i);
                rowData[1] = (String) ObjItem.getAttribute("PIECE_NO").getObj();
                rowData[2] = (String) ObjItem.getAttribute("ITEM_DESC").getObj();
                rowData[3] = (String) ObjItem.getAttribute("POSITION").getObj();
                rowData[4] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("LNGTH").getVal(), 3));
                rowData[5] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("WIDTH").getVal(), 3));
                rowData[6] = Integer.toString((int) ObjItem.getAttribute("GSQ").getVal());
                rowData[7] = (String) ObjItem.getAttribute("STYLE").getObj();
                rowData[8] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("WEIGHT").getVal(), 2));
                rowData[9] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("SQMTR").getVal(), 2));
                rowData[10] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("GROSS_RATE").getVal(), 2));
                rowData[11] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("BAS_AMT").getVal(), 2));
                rowData[12] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("CHEM_TRT_CHG").getVal(), 2));
                rowData[13] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("PIN_CHG").getVal(), 2));
                rowData[14] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("SPIRAL_CHG").getVal(), 2));
                rowData[15] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("DISC_PER").getVal(), 2));
                rowData[16] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("DISAMT").getVal(), 2));

                rowData[17] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("AOSD_PER").getVal(), 2));
                rowData[18] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("AOSD_AMT").getVal(), 2));

                rowData[19] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("DISBASAMT").getVal(), 2));
                rowData[20] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("EXCISE").getVal(), 2));
                rowData[21] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("SEAM_CHG").getVal(), 2));
                rowData[22] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("INSACC_AMT").getVal(), 2));
                rowData[23] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("INV_AMT").getVal(), 2));
                rowData[24] = (String) ObjItem.getAttribute("INS_IND").getObj();
                rowData[25] = (String) ObjItem.getAttribute("MACHINE_NO").getObj();
                rowData[26] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("ORDER_DATE").getObj());
                rowData[27] = (String) ObjItem.getAttribute("INCHARGE_NAME").getObj();
                rowData[28] = (String) ObjItem.getAttribute("PRODUCT_CD").getObj();
                rowData[29] = (String) ObjItem.getAttribute("PO_NO").getObj();
                rowData[30] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("PO_DATE").getObj());
                rowData[31] = Double.toString(ObjItem.getAttribute("VAT").getVal());
                rowData[32] = Double.toString(ObjItem.getAttribute("CST").getVal());
                rowData[33] = Double.toString(ObjItem.getAttribute("SD_AMT").getVal());
                rowData[34] = Double.toString(ObjItem.getAttribute("FIVE_PER_WEIGHT").getVal());
                rowData[35] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("PRO_INV_AMT").getVal(), 2));
                rowData[36] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("INV_VAL").getVal(), 0));
                rowData[37] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("CGST_AMT").getVal(), 2));
                rowData[38] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("SGST_AMT").getVal(), 2));
                rowData[39] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("IGST_AMT").getVal(), 2));

                rowData[40] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("TCS_PER").getVal(), 4));
                rowData[41] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("TCS_AMT").getVal(), 2));
                
                rowData[42] = (String) ObjItem.getAttribute("UOM").getObj();
                rowData[43] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("RATE").getVal(), 2));
                rowData[44] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("SURCHARGE_PER").getVal(), 2));
                rowData[45] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("SURCHARGE_RATE").getVal(), 2));

                DataModelDesc.addRow(rowData);
            }

            DoNotEvaluate = false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String ProformaNo = ObjSalesParty.getAttribute("PROFORMA_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsProforma.ModuleID, ProformaNo);
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
            HashMap History = clsProforma.getHistoryList(EITLERPGLOBAL.gCompanyID, ProformaNo);
            for (int i = 1; i <= History.size(); i++) {
                clsProforma ObjHistory = (clsProforma) History.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = ObjHistory.getAttribute("ENTRY_DATE").getString();

                String ApprovalStatus = "";

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
        ObjSalesParty.setAttribute("PREFIX", SelPrefix);
        ObjSalesParty.setAttribute("SUFFIX", SelSuffix);
        ObjSalesParty.setAttribute("FFNO", FFNo);
        ObjSalesParty.setAttribute("PROFORMA_DATE", EITLERPGLOBAL.formatDateDB(txtProformaDate.getText()));
        ObjSalesParty.setAttribute("PARTY_CD", txtPartycode.getText());
        ObjSalesParty.setAttribute("NAME", txtPartyname.getText());
        ObjSalesParty.setAttribute("STATION", txtPartystation.getText());
        ObjSalesParty.setAttribute("CONTACT", txtContact.getText());
        ObjSalesParty.setAttribute("PHONE", txtPhone.getText());

        if (txtremark1.getText().equals("")) {
            ObjSalesParty.setAttribute("REMARK1", txtremark1.getText());
        } else {
            ObjSalesParty.setAttribute("REMARK1", ">" + txtremark1.getText());
        }
        if (txtremark2.getText().equals("")) {
            ObjSalesParty.setAttribute("REMARK2", txtremark2.getText());
        } else {
            ObjSalesParty.setAttribute("REMARK2", ">" + txtremark2.getText());
        }
        if (txtremark3.getText().equals("")) {
            ObjSalesParty.setAttribute("REMARK3", txtremark3.getText());
        } else {
            ObjSalesParty.setAttribute("REMARK3", ">" + txtremark3.getText());
        }
        if (txtremark4.getText().equals("")) {
            ObjSalesParty.setAttribute("REMARK4", txtremark4.getText());
        } else {
            ObjSalesParty.setAttribute("REMARK4", ">" + txtremark4.getText());
        }
        if (txtremark5.getText().equals("")) {
            ObjSalesParty.setAttribute("REMARK5", txtremark5.getText());
        } else {
            ObjSalesParty.setAttribute("REMARK5", ">" + txtremark5.getText());
        }

        //----- Update Approval Specific Fields -----------//
        ObjSalesParty.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjSalesParty.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjSalesParty.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjSalesParty.setAttribute("REJECTED_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS", "R");
            ObjSalesParty.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjSalesParty.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjSalesParty.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjSalesParty.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjSalesParty.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        //======= Set Line part ============
        ObjSalesParty.colMRItems.clear();

        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            clsProformaItem ObjItem = new clsProformaItem();
            String lItemID = (String) TableDesc.getValueAt(i, 1);

            //Add Only Valid Items
            ObjItem.setAttribute("SR_NO", i);
            ObjItem.setAttribute("PIECE_NO", lItemID);
            ObjItem.setAttribute("ITEM_DESC", (String) TableDesc.getValueAt(i, 2));
            ObjItem.setAttribute("POSITION", (String) TableDesc.getValueAt(i, 3));
            ObjItem.setAttribute("LNGTH", Double.parseDouble((String) TableDesc.getValueAt(i, 4)));
            ObjItem.setAttribute("WIDTH", Double.parseDouble((String) TableDesc.getValueAt(i, 5)));
            ObjItem.setAttribute("GSQ", Double.parseDouble((String) TableDesc.getValueAt(i, 6)));
            ObjItem.setAttribute("STYLE", (String) TableDesc.getValueAt(i, 7));
            try {
                ObjItem.setAttribute("WEIGHT", Float.parseFloat(TableDesc.getValueAt(i, 8) + ""));
            } catch (Exception r2) {
                ObjItem.setAttribute("WEIGHT", 0.0);
            }
            try {
                ObjItem.setAttribute("SQMTR", Float.parseFloat(TableDesc.getValueAt(i, 9) + ""));
            } catch (Exception e) {
                ObjItem.setAttribute("SQMTR", 0.0);
            }
            ObjItem.setAttribute("GROSS_RATE", Double.parseDouble(TableDesc.getValueAt(i, 10) + ""));
            ObjItem.setAttribute("BAS_AMT", Float.parseFloat(TableDesc.getValueAt(i, 11) + ""));
            ObjItem.setAttribute("CHEM_TRT_CHG", Float.parseFloat(TableDesc.getValueAt(i, 12) + ""));
            ObjItem.setAttribute("PIN_CHG", Float.parseFloat(TableDesc.getValueAt(i, 13) + ""));
            ObjItem.setAttribute("SPIRAL_CHG", Float.parseFloat(TableDesc.getValueAt(i, 14) + ""));
            ObjItem.setAttribute("DISC_PER", Float.parseFloat(TableDesc.getValueAt(i, 15) + ""));
            ObjItem.setAttribute("DISAMT", Float.parseFloat(TableDesc.getValueAt(i, 16) + ""));

            ObjItem.setAttribute("AOSD_PER", Float.parseFloat(TableDesc.getValueAt(i, 17) + ""));
            ObjItem.setAttribute("AOSD_AMT", Float.parseFloat(TableDesc.getValueAt(i, 18) + ""));

            ObjItem.setAttribute("DISBASAMT", Float.parseFloat(TableDesc.getValueAt(i, 19) + ""));
            ObjItem.setAttribute("EXCISE", Float.parseFloat(TableDesc.getValueAt(i, 20) + ""));
            ObjItem.setAttribute("SEAM_CHG", Float.parseFloat(TableDesc.getValueAt(i, 21) + "")); //wpsc
            ObjItem.setAttribute("INSACC_AMT", Float.parseFloat(TableDesc.getValueAt(i, 22) + ""));
            ObjItem.setAttribute("INV_AMT", Float.parseFloat(TableDesc.getValueAt(i, 23) + ""));
            ObjItem.setAttribute("INS_IND", Float.parseFloat(TableDesc.getValueAt(i, 24) + ""));
            ObjItem.setAttribute("MACHINE_NO", (String) TableDesc.getValueAt(i, 25));
            ObjItem.setAttribute("ORDER_DATE", (String) TableDesc.getValueAt(i, 26));
            ObjItem.setAttribute("INCHARGE_NAME", (String) TableDesc.getValueAt(i, 27));
            ObjItem.setAttribute("PRODUCT_CD", (String) TableDesc.getValueAt(i, 28));
            ObjItem.setAttribute("PO_NO", (String) TableDesc.getValueAt(i, 29));
            ObjItem.setAttribute("PO_DATE", (String) TableDesc.getValueAt(i, 30));
            ObjItem.setAttribute("VAT", Float.parseFloat(TableDesc.getValueAt(i, 31) + ""));
            ObjItem.setAttribute("CST", Float.parseFloat(TableDesc.getValueAt(i, 32) + ""));
            ObjItem.setAttribute("SD_AMT", Float.parseFloat(TableDesc.getValueAt(i, 33) + ""));
            ObjItem.setAttribute("FIVE_PER_WEIGHT", Float.parseFloat(TableDesc.getValueAt(i, 34) + ""));
            ObjItem.setAttribute("PRO_INV_AMT", Float.parseFloat(TableDesc.getValueAt(i, 35) + ""));
            ObjItem.setAttribute("INV_VAL", Float.parseFloat(TableDesc.getValueAt(i, 36) + ""));
            String word = "";
            NumWord nw = new NumWord();
            word = nw.convertNumToWord(Math.round(Double.parseDouble(TableDesc.getValueAt(i, 36) + "")));
            ObjItem.setAttribute("INV_VAL_WORD", word);
            ObjItem.setAttribute("CGST_AMT", Float.parseFloat(TableDesc.getValueAt(i, 37) + ""));
            ObjItem.setAttribute("SGST_AMT", Float.parseFloat(TableDesc.getValueAt(i, 38) + ""));
            ObjItem.setAttribute("IGST_AMT", Float.parseFloat(TableDesc.getValueAt(i, 39) + ""));

            ObjItem.setAttribute("TCS_PER", Float.parseFloat(TableDesc.getValueAt(i, 40) + ""));
            ObjItem.setAttribute("TCS_AMT", Float.parseFloat(TableDesc.getValueAt(i, 41) + ""));
            
            ObjItem.setAttribute("UOM", (String) TableDesc.getValueAt(i, 42));

            ObjItem.setAttribute("RATE", Double.parseDouble(TableDesc.getValueAt(i, 43) + ""));
            ObjItem.setAttribute("SURCHARGE_PER", Double.parseDouble(TableDesc.getValueAt(i, 44) + ""));
            ObjItem.setAttribute("SURCHARGE_RATE", Double.parseDouble(TableDesc.getValueAt(i, 45) + ""));
            ObjSalesParty.colMRItems.put(Integer.toString(ObjSalesParty.colMRItems.size() + 1), ObjItem);

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

    private void FormatGridDiscount() {
        DataModelDiscount = new EITLTableModel();
        TableDiscount.removeAll();
        TableDiscount.setModel(DataModelDiscount);

        DataModelDiscount.TableReadOnly(true);
        DataModelDiscount.addColumn("Sr.");
        DataModelDiscount.addColumn("Product Code");
        DataModelDiscount.addColumn("Discount %");
        DataModelDiscount.addColumn("Current Memo Date");
        DataModelDiscount.addColumn("No Of Pieces");
        TableDiscount.setAutoResizeMode(TableDiscount.AUTO_RESIZE_OFF);

        TableDiscount.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void SetMenuForRights() {
        // --- Add Rights --        
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6058, 60581)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6058, 60583)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6058, 60584)) {
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

                if (FieldName.trim().equals("PROFORMA_NO")) {
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
        ObjSalesParty.Filter(" PROFORMA_NO IN (SELECT H.PROFORMA_NO FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA WHERE H.PROFORMA_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=708 AND CANCELED=0)");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pProformaNo) {
        ObjSalesParty.Filter(" PROFORMA_NO='" + pProformaNo + "'");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        String PartyCode = txtProformano.getText();

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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsProforma.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsProforma.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String ProformaNo = (String) ObjSalesParty.getAttribute("PROFORMA_NO").getObj();
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsProforma.ModuleID, ProformaNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    private void UpdateSrNo() {
        int SrCol = DataModelDesc.getColFromVariable("SR_NO");

        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            TableDesc.setValueAt(Integer.toString(i + 1), i, SrCol);

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
            DataModelDesc.addColumn("Piece Desc");//2
            DataModelDesc.addColumn("Position");//3
            DataModelDesc.addColumn("Length");//4
            DataModelDesc.addColumn("Width"); //5
            DataModelDesc.addColumn("GSM");  //6
            DataModelDesc.addColumn("Style"); //7
            DataModelDesc.addColumn("Weight"); //8
            DataModelDesc.addColumn("Sqmtr"); //9
            DataModelDesc.addColumn("GrossRate");   //10     
            DataModelDesc.addColumn("Base Amt"); //11
            DataModelDesc.addColumn("Chem Trt Charge");  //12
            DataModelDesc.addColumn("Pin Charge");  //13
            DataModelDesc.addColumn("Spiral Charge");  //14
            DataModelDesc.addColumn("Disc %");  //15
            DataModelDesc.addColumn("Dis Amt");  //16     
            DataModelDesc.addColumn("AOD %");  //17
            DataModelDesc.addColumn("AOD Amt");  //18                 
            DataModelDesc.addColumn("Dis BaseAmt"); //19
            DataModelDesc.addColumn("Excise");  //20
            DataModelDesc.addColumn("Seam Chg");  //21
            DataModelDesc.addColumn("Insacc Amt");  //22        
            DataModelDesc.addColumn("Inv Amt"); //23
            DataModelDesc.addColumn("Ins Ind"); //24
            DataModelDesc.addColumn("Machine No"); //25
            DataModelDesc.addColumn("Order Date"); //26
            DataModelDesc.addColumn("Incharge");  //27
            DataModelDesc.addColumn("Product code"); //28
            DataModelDesc.addColumn("PO NO"); //29
            DataModelDesc.addColumn("PO Date"); //30
            DataModelDesc.addColumn("VAT"); //31
            DataModelDesc.addColumn("CST"); //32
            DataModelDesc.addColumn("SD Amount"); //33
            DataModelDesc.addColumn("5% WIP"); //34
            DataModelDesc.addColumn("Perfoma Invoice Amount"); //35
            DataModelDesc.addColumn("Invoice Value"); //36
            DataModelDesc.addColumn("CGST Amt"); //37
            DataModelDesc.addColumn("SGST Amt"); //38
            DataModelDesc.addColumn("IGST Amt"); //39
            DataModelDesc.addColumn("TCS %");   //40
            DataModelDesc.addColumn("TCS Amt");   //41
            DataModelDesc.addColumn("Unit Of Meserment"); //42
            DataModelDesc.addColumn("Rate");   //43
            DataModelDesc.addColumn("Sur. %");   //44
            DataModelDesc.addColumn("Sur.Rate");   //45

            DataModelDesc.SetVariable(0, "SR_NO");  //0
            DataModelDesc.SetVariable(1, "PIECE_NO"); //1
            DataModelDesc.SetVariable(2, "ITEM_DESC"); //2
            DataModelDesc.SetVariable(3, "POSITION"); //3
            DataModelDesc.SetVariable(4, "LNGTH"); //4
            DataModelDesc.SetVariable(5, "WIDTH"); //5
            DataModelDesc.SetVariable(6, "GSQ"); //6
            DataModelDesc.SetVariable(7, "STYLE"); //7   //DataModelDesc.SetVariable(8,"BALNK");
            DataModelDesc.SetVariable(9, "WEIGHT"); //8
            DataModelDesc.SetVariable(10, "GROSS_RATE"); //9
            DataModelDesc.SetVariable(11, "BAS_AMT"); //10
            DataModelDesc.SetVariable(12, "CHEM_TRT_CHG"); //10
            DataModelDesc.SetVariable(13, "PIN_CHG"); //10
            DataModelDesc.SetVariable(14, "SPIRAL_CHG"); //10
            DataModelDesc.SetVariable(15, "DISC_PER"); //11
            DataModelDesc.SetVariable(16, "DISAMT"); //12
            DataModelDesc.SetVariable(17, "DISBASAMT");  //13          
            DataModelDesc.SetVariable(18, "EXCISE");  //14
            DataModelDesc.SetVariable(19, "SEAM_CHG"); //15
            DataModelDesc.SetVariable(20, "INSACC_AMT"); //16
            DataModelDesc.SetVariable(21, "INV_AMT"); //17
            DataModelDesc.SetVariable(22, "INS_IND"); //18
            DataModelDesc.SetVariable(23, "MACHINE_NO"); //21
            DataModelDesc.SetVariable(24, "ORDER_DATE"); //23
            DataModelDesc.SetVariable(25, "INCHARGE_NAME"); //24            
            DataModelDesc.SetVariable(26, "PRODUCT_CD");
            DataModelDesc.SetVariable(27, "PO_NO");
            DataModelDesc.SetVariable(28, "PO_DATE");
            DataModelDesc.SetVariable(29, "VAT");
            DataModelDesc.SetVariable(30, "CST");
            DataModelDesc.SetVariable(31, "SD_AMT");
            DataModelDesc.SetVariable(32, "PRO_INV_AMT");
            DataModelDesc.SetVariable(33, "INV_VAL");
            DataModelDesc.SetVariable(35, "CGST_AMT");
            DataModelDesc.SetVariable(36, "SGST_AMT");
            DataModelDesc.SetVariable(37, "IGST_AMT");
            DataModelDesc.SetVariable(37, "UOM");

            DataModelDesc.SetReadOnly(0);
            DataModelDesc.SetReadOnly(1);
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
            DataModelDesc.SetReadOnly(12);
            DataModelDesc.SetReadOnly(13);
            DataModelDesc.SetReadOnly(14);
            DataModelDesc.SetReadOnly(15);
            DataModelDesc.SetReadOnly(16);
            DataModelDesc.SetReadOnly(17);
            DataModelDesc.SetReadOnly(18);
            DataModelDesc.SetReadOnly(19);
            DataModelDesc.SetReadOnly(20);
            DataModelDesc.SetReadOnly(21);
            DataModelDesc.SetReadOnly(22);
            DataModelDesc.SetReadOnly(23);
            DataModelDesc.SetReadOnly(24);
            DataModelDesc.SetReadOnly(25);
            DataModelDesc.SetReadOnly(26);
            DataModelDesc.SetReadOnly(27);
            DataModelDesc.SetReadOnly(28);
            DataModelDesc.SetReadOnly(29);
            DataModelDesc.SetReadOnly(30);
            DataModelDesc.SetReadOnly(31);
            DataModelDesc.SetReadOnly(32);
            DataModelDesc.SetReadOnly(33);
            DataModelDesc.SetReadOnly(34);
            DataModelDesc.SetReadOnly(35);
            DataModelDesc.SetReadOnly(36);
            DataModelDesc.SetReadOnly(37);
            DataModelDesc.SetReadOnly(38);
            DataModelDesc.SetReadOnly(39);
            DataModelDesc.SetReadOnly(40);

            DataModelDesc.SetReadOnly(41);
            DataModelDesc.SetReadOnly(42);
            DataModelDesc.SetReadOnly(43);
            
            DataModelDesc.SetReadOnly(44);
            DataModelDesc.SetReadOnly(45);

            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            TableDesc.getColumnModel().getColumn(16).setPreferredWidth(100);

            //------- Install Table List Selection Listener ------//
            TableDesc.getColumnModel().getSelectionModel().addListSelectionListener(
                    new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent e) {
                            int last = TableDesc.getSelectedColumn();
                            String strVar = DataModelDesc.getVariable(last);

                            //=============== Cell Editing Routine =======================//
                            try {
                                cellLastValue = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), TableDesc.getSelectedColumn());

                                TableDesc.editCellAt(TableDesc.getSelectedRow(), TableDesc.getSelectedColumn());
                                if (TableDesc.getEditorComponent() instanceof JTextComponent) {
                                    ((JTextComponent) TableDesc.getEditorComponent()).selectAll();
                                }
                            } catch (Exception cell) {
                            }
                            //============= Cell Editing Routine Ended =================//

                            ShowMessage("Ready");

                            if (last == 1) {
                                ShowMessage("Enter Press F1 for the list of Pice No");
                            }
                        }
                    }
            );
            //===================================================//

            //----- Install Table Model Event Listener -------//
            TableDesc.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int col = e.getColumn();

                        //=========== Cell Update Prevention Check ===========//
                        String curValue = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), e.getColumn());
                        if (curValue.equals(cellLastValue)) {
                            return;
                        }
                        //====================================================//

                        if (DoNotEvaluate) {
                            return;
                        }
                        if (col == 1) {
                            try {

                            } catch (Exception ex) {
                            }
                        }
                    }
                }
            });
            int ImportCol = DataModelDesc.getColFromVariable("Calculate_Weight");
            Renderer.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setEnabled(true);
            aCheckBox.setSelected(false);
            TableDesc.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            TableDesc.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);

        } catch (Exception e) {

        }
        Updating = false;
    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    private void PreviewReport() {
        HashMap Params = new HashMap();

        try {
            URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/rptProforma.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&DocNo=" + txtProformano.getText() + "&ProformaDate=" + txtProformaDate.getText());
            EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
        }
    }

    private void GeneratePreviousDiscount() {
        try {
            FormatGridDiscount();  //clear existing content of table
            String strPartycode = txtPartycode.getText().toString();
            ResultSet rsTmp, rsBuyer, rsIndent, rsRIA;
            String strSQL = "";
            strSQL += "SELECT PRODUCT_CD,DISC_PER,MAX(MEMO_DATE) AS CURRENT_MEMO_DATE,COUNT(PIECE_NO) AS COUNT_PCS FROM PRODUCTION.FELT_DISCOUNT_MEMO WHERE ";
            if (!txtPartycode.getText().equals("")) {
                strSQL += " PARTY_CODE=" + strPartycode + " AND ";
            }
            strSQL += "DISC_PER!=0 AND MEMO_DATE>'1900-01-01' GROUP BY PRODUCT_CD,DISC_PER ORDER BY DISC_PER DESC";
            rsTmp = data.getResult(strSQL);
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    Object[] rowData = new Object[5];
                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("PRODUCT_CD");
                    rowData[2] = rsTmp.getString("DISC_PER");
                    rowData[3] = EITLERPGLOBAL.formatDate(rsTmp.getString("CURRENT_MEMO_DATE"));
                    rowData[4] = rsTmp.getString("COUNT_PCS");
                    DataModelDiscount.addRow(rowData);
                    rsTmp.next();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
