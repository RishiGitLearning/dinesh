/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */

package EITLERP.Production.FeltF;
/**
 *
 * @author Dharmendra
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
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
import EITLERP.Stores.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import EITLERP.Purchase.*;
import java.math.BigDecimal;
import EITLERP.Production.ReportUI.JTextFieldHint;


//import EITLERP.Purchase.frmSendMail;


public class frmFeltF extends javax.swing.JApplet {
    
    private int EditMode=0;
    
    //private clsFeltF ObjSalesParty;
    private clsFeltF ObjSalesParty;
    
    private int SelHierarchyID=0; //Selected Hierarchy
    private int lnFromID=0;
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    
    private EITLTableModel DataModelDesc;
    private EITLTableModel DataModelDiscount;
    private EITLTableModel DataModelA;
    private EITLTableModel DataModelHS;
    private EITLTableModel DataModelOtherpartyDiscount;
    
    private EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint=new EITLTableCellRenderer();
    
    private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelMainCode;
    private EITLTableModel DataModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
   // EITLTableModel DataModelDesc = new EITLTableModel();
    
    private HashMap colVariables=new HashMap();
    private HashMap colVariables_H=new HashMap();
    //clsColumn ObjColumn=new clsColumn();
    
    private boolean Updating=false;
    private boolean Updating_H=false;
    private boolean DoNotEvaluate=false;
    
    private EITLComboModel cmbPriorityModel;
    
    private boolean HistoryView=false;
    private String theDocNo="";
    public frmPendingApprovals frmPA;
    public String mdocno="";
    private int charge09index=0;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    String cellLastValue="";
    boolean mchinests=false;
    public boolean PENDING_DOCUMENT=false; //for refresh pending document module
    
    /** Creates new form frmSalesParty */
    public frmFeltF() {
        System.gc();
        setSize(900,700);
        initComponents();
        cmdDelete.setEnabled(false);
        cmdDelete.disable();
        cmdDelete.setVisible(false);
        
        cmdPrint.setEnabled(false);
        cmdPrint.disable();
        cmdPrint.setVisible(false);
        
        cmdAdd.setVisible(false);
        cmdItemdelete.setVisible(false);
        
        
        //Now show the Images
        cmdTop.setIcon(EITLERPGLOBAL.getImage("TOP"));
        cmdBack.setIcon(EITLERPGLOBAL.getImage("BACK"));
        cmdNext.setIcon(EITLERPGLOBAL.getImage("NEXT"));
        cmdLast.setIcon(EITLERPGLOBAL.getImage("LAST"));
        cmdNew.setIcon(EITLERPGLOBAL.getImage("NEW"));
        cmdEdit.setIcon(EITLERPGLOBAL.getImage("EDIT"));
        //cmdDelete.setIcon(EITLERPGLOBAL.getImage("DELETE"));
        cmdSave.setIcon(EITLERPGLOBAL.getImage("SAVE"));
        cmdCancel.setIcon(EITLERPGLOBAL.getImage("UNDO"));
        cmdFilter.setIcon(EITLERPGLOBAL.getImage("FIND"));
        cmdPreview.setIcon(EITLERPGLOBAL.getImage("PREVIEW"));
        cmdPrint.setIcon(EITLERPGLOBAL.getImage("PRINT"));
        cmdExit.setIcon(EITLERPGLOBAL.getImage("EXIT"));
        
        
        //ObjColumn.LoadData((int)EITLERPGLOBAL.gCompanyID);
        
        GenerateCombos();
        //FormatGrid();
        //GenerateGrid();
        
        //  cmdSelectAll.setEnabled(false);
        //  cmdClearAll.setEnabled(false);
        
        ObjSalesParty = new clsFeltF();
        
        //lblDocThrough.setVisible(false);
        //txtDocThrough.setVisible(false);
        
        SetMenuForRights();
        
        if(ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID)) {
            ObjSalesParty.MoveLast();
            DisplayData();
            
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while loading data. \n Error is "+ObjSalesParty.LastError);
        }
        
       // txtfromdate.setEditable(false);
        txtAuditRemarks.setVisible(false);
        DataModelDesc.TableReadOnly(true);
   
    }
    
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel=new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsFeltF.ModuleID+"");
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsFeltF.ModuleID+"");
        }
        
        for(int i=1;i<=List.size();i++) {
            clsHierarchy ObjHierarchy=(clsHierarchy) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text=(String)ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        jFrame1 = new javax.swing.JFrame();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableDesc = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        cmdAdd = new javax.swing.JButton();
        cmdItemdelete = new javax.swing.JButton();
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
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtf6no = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtfromdate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txttodate = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

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
        cmdDelete.setEnabled(false);
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

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 840, 30);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("FELT F6 FORM -");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 30, 830, 20);

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
        jScrollPane1.setBounds(0, 0, 810, 180);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Tab1.add(jButton1);
        jButton1.setBounds(700, 220, 90, 25);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14));
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(lblStatus);
        lblStatus.setBounds(10, 190, 610, 20);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        Tab1.add(cmdAdd);
        cmdAdd.setBounds(630, 190, 70, 25);

        cmdItemdelete.setText("Remove");
        cmdItemdelete.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdItemdelete.setEnabled(false);
        cmdItemdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdItemdeleteActionPerformed(evt);
            }
        });

        Tab1.add(cmdItemdelete);
        cmdItemdelete.setBounds(710, 190, 80, 25);

        jTabbedPane1.addTab("F6 Detail", Tab1);

        ApprovalPanel.setLayout(null);

        ApprovalPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        ApprovalPanel.setToolTipText("");
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        ApprovalPanel.add(jLabel31);
        jLabel31.setBounds(5, 13, 100, 15);

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
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbHierarchyFocusLost(evt);
            }
        });

        ApprovalPanel.add(cmbHierarchy);
        cmbHierarchy.setBounds(110, 13, 270, 24);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        ApprovalPanel.add(jLabel32);
        jLabel32.setBounds(5, 43, 100, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        ApprovalPanel.add(txtFrom);
        txtFrom.setBounds(110, 43, 270, 22);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        ApprovalPanel.add(jLabel35);
        jLabel35.setBounds(5, 76, 100, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        ApprovalPanel.add(txtFromRemarks);
        txtFromRemarks.setBounds(110, 73, 518, 22);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        ApprovalPanel.add(jLabel36);
        jLabel36.setBounds(5, 116, 100, 15);

        jPanel6.setLayout(null);

        jPanel6.setBorder(new javax.swing.border.EtchedBorder());
        OpgApprove.setText("Approve & Forward");
        buttonGroup1.add(OpgApprove);
        OpgApprove.setEnabled(false);
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });

        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 169, 23);

        OpgFinal.setText("Final Approve");
        buttonGroup1.add(OpgFinal);
        OpgFinal.setEnabled(false);
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });

        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 136, 20);

        OpgReject.setText("Reject");
        buttonGroup1.add(OpgReject);
        OpgReject.setEnabled(false);
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });

        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 136, 20);

        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        buttonGroup1.add(OpgHold);
        OpgHold.setEnabled(false);
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        ApprovalPanel.add(jPanel6);
        jPanel6.setBounds(110, 113, 182, 100);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        ApprovalPanel.add(jLabel33);
        jLabel33.setBounds(5, 226, 100, 15);

        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });

        ApprovalPanel.add(cmbSendTo);
        cmbSendTo.setBounds(110, 223, 270, 24);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        ApprovalPanel.add(jLabel34);
        jLabel34.setBounds(5, 262, 100, 15);

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
        cmdNext3.setBounds(550, 300, 93, 25);

        jButton3.setText("Next >>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        ApprovalPanel.add(jButton3);
        jButton3.setBounds(650, 300, 100, 25);

        jTabbedPane1.addTab("Approval", null, ApprovalPanel, "");

        StatusPanel.setLayout(null);

        StatusPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 15);

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
        jLabel19.setBounds(10, 170, 182, 15);

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
        txtAuditRemarks.setBounds(570, 260, 129, 19);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        StatusPanel.add(jButton5);
        jButton5.setBounds(570, 290, 90, 25);

        jTabbedPane1.addTab("Status", StatusPanel);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(20, 140, 830, 370);

        jLabel1.setText("F6 No");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 60, 50, 15);

        txtf6no.setEditable(false);
        txtf6no.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtf6noMouseClicked(evt);
            }
        });

        getContentPane().add(txtf6no);
        txtf6no.setBounds(100, 60, 120, 19);

        jLabel5.setText("From Date");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(10, 90, 70, 15);

        txtfromdate.setEnabled(false);
        getContentPane().add(txtfromdate);
        txtfromdate.setBounds(100, 90, 120, 19);

        jLabel2.setText("To Date");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(230, 90, 60, 15);

        txttodate.setEnabled(false);
        getContentPane().add(txttodate);
        txttodate.setBounds(290, 90, 130, 19);

        jButton2.setText("Show");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton2);
        jButton2.setBounds(450, 90, 68, 25);

    }//GEN-END:initComponents

    private void cmbHierarchyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbHierarchyFocusLost

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       FormatGrid();
       GenerateData();       // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtf6noMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtf6noMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtf6noMouseClicked
        
    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        ShowMessage("Select the user to whom document to be forwarded");        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSendToFocusGained
                                                                            
    private void cmdItemdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdItemdeleteActionPerformed
        // TODO add your handling code here:
        if(TableDesc.getRowCount()>0) {
            DataModelDesc.removeRow(TableDesc.getSelectedRow());
            // DisplayIndicators();
        }
        
    }//GEN-LAST:event_cmdItemdeleteActionPerformed
                
    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed
      
    }//GEN-LAST:event_TableDescKeyPressed
    
    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased
        
      
    }//GEN-LAST:event_TableDescKeyReleased
    
    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        
        if(txtfromdate.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Enter From Date And To Date Detail First.");
            return;
        }
        else {
            
            Updating=true;
            Object[] rowData=new Object[45];
            rowData[0]=Integer.toString(TableDesc.getRowCount()+1);
            rowData[1]="";
            rowData[2]="";
            rowData[3]="";
            rowData[4]="";
            rowData[5]="";
            rowData[6]="";
            rowData[7]="";
            rowData[8]="";
            rowData[9]="";
            rowData[10]="";
            rowData[11]="";
            rowData[12]="";
            rowData[13]="";
           
            
            DataModelDesc.addRow(rowData);
            DataModelDesc.SetReadOnly(0);
            DataModelDesc.ResetReadOnly(1);
            DataModelDesc.ResetReadOnly(2);
            DataModelDesc.ResetReadOnly(4);
            DataModelDesc.ResetReadOnly(5);
            DataModelDesc.ResetReadOnly(6);
            DataModelDesc.ResetReadOnly(7);
            DataModelDesc.ResetReadOnly(8);
            DataModelDesc.ResetReadOnly(9);
            DataModelDesc.ResetReadOnly(10);
            DataModelDesc.ResetReadOnly(11);
            DataModelDesc.ResetReadOnly(12);
            DataModelDesc.ResetReadOnly(13);
           
          
          
            
            
            
            //Updating=false;
            TableDesc.changeSelection(TableDesc.getRowCount()-1, 1, false,false);
            
            TableDesc.requestFocus();
        }
        //ShowMessage("Search Piece by press F2 for other party otherwise press F1");
    }//GEN-LAST:event_cmdAddActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        // TODO add your handling code here:
     //  System.out.println("start");
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            
            cmbSendTo.setEnabled(false);
        }
        
        if(clsHierarchy.CanFinalApprove((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        }
        else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged
    
    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
       
        String F6ID=txtf6no.getText().trim();
        SetupApproval();
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if(clsFeltProductionApprovalFlow.IsOnceRejectedDoc(731,txtf6no.getText())) {
                cmbSendTo.setEnabled(true);
            }
            else {
                cmbSendTo.setEnabled(false);
            }
        }
        
        
        if(cmbSendTo.getItemCount()<=0) {
            GenerateFromCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked
    
    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        // TODO add your handling code here:
        if(!OpgFinal.isEnabled()) {
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
            
    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if(TableHS.getRowCount()>0&&TableHS.getSelectedRow()>=0) {
            txtAuditRemarks.setText((String)TableHS.getValueAt(TableHS.getSelectedRow(),4));
            BigEdit bigEdit=new BigEdit();
            bigEdit.theText=txtAuditRemarks;
            bigEdit.ShowEdit();
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton5ActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
        //GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdTopActionPerformed
    
    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
        //GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdBackActionPerformed
    
    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
        //GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNextActionPerformed
    
    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
        //GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdLastActionPerformed
    
    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
       // txtfromdate.enable();
       // txtfromdate.setEditable(false);
      //  txtfromdate.requestFocus();
        
        //   txtspeedrange.enable();
        //  txtspeedrange.setEditable(false);
        // txtspeedrange.requestFocus();
        
        cmdAdd.setVisible(true);
        cmdItemdelete.setVisible(true);
        Add();
        // GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNewActionPerformed
    
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
       // txtfromdate.setEditable(false);
      //  txtPartyname.setEditable(false);
      //  txtPartystation.setEditable(false);
        cmdAdd.setVisible(true);
        cmdItemdelete.setVisible(true);
        // cmdAdd.disable();
        
        Edit();
        //GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdEditActionPerformed
    
    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
        //  GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdDeleteActionPerformed
    
    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        
        /*else {
             txtOtherPartycode.setEnabled(false);
             txtOtherPartycode.setText("");
             //GenerateOtherpartyPreviousDiscount();
        }
         */
        Save();
        cmdAdd.setVisible(false);
        cmdItemdelete.setVisible(false);
        // GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdSaveActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
        cmdAdd.setVisible(false);
        cmdItemdelete.setVisible(false);
        //GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
        Find();
        //GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdFilterActionPerformed
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        //ObjColumn.Close();
        ObjSalesParty.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    
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
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdItemdelete;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JFrame jFrame1;
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
    private javax.swing.JTextField txtf6no;
    private javax.swing.JTextField txtfromdate;
    private javax.swing.JTextField txttodate;
    // End of variables declaration//GEN-END:variables
    
   
    private void Add() {
        //  EditMode=EITLERPGLOBAL.ADD;
        
        //Now Generate new document no.
        SelectFirstFree aList=new SelectFirstFree();
        aList.ModuleID=731;
        
        if(aList.ShowList()) {
            EditMode=EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SelPrefix=aList.Prefix; //Selected Prefix;
            SelSuffix=aList.Suffix;
            FFNo=aList.FirstFreeNo;
            SetupApproval();
            //Display newly generated document no.
            txtf6no.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 731, FFNo,  false));
            txtfromdate.requestFocus();
            
            lblTitle.setText("FELT F6 FROM - "+ txtf6no.getText());
            lblTitle.setBackground(Color.BLUE);
        }
        else {
            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
    }
    private void SetupApproval() {
        /*
        if(cmbHierarchy.getItemCount()>1) {
            cmbHierarchy.setEnabled(true);
        }
         */
        if(EditMode==EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
        }
        else {
            cmbHierarchy.setEnabled(false);
        }
        //Set Default Hierarchy ID for User
        int DefaultID=clsHierarchy.getDefaultHierarchy((int)EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy,DefaultID);
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            lnFromID=(int)EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        }
        else {
            
            int FromUserID=clsFeltProductionApprovalFlow.getFromID( clsFeltF.ModuleID,(String)ObjSalesParty.getAttribute("F6_ID").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=clsFeltProductionApprovalFlow.getFromRemarks(clsFeltF.ModuleID,FromUserID,(String)ObjSalesParty.getAttribute("F6_ID").getObj());
            
            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }
        
        
        
        
        //In Edit Mode Hierarchy Should be disabled
        if(EditMode==EITLERPGLOBAL.EDIT) {
            //cmbHierarchy.setEnabled(false);
        }
        
        if(EditMode==0) {
            //Disable all hierarchy controls if not in Add/Edit Mode
            //cmbHierarchy.setEnabled(false);
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
        HashMap List=new HashMap();
        
        try {
            if(EditMode==EITLERPGLOBAL.ADD) {
                //----- Generate cmbType ------- //
            cmbToModel=new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);
                
                List=clsHierarchy.getUserList((int)EITLERPGLOBAL.gCompanyID,SelHierarchyID,EITLERPGLOBAL.gNewUserID);
                for(int i=1;i<=List.size();i++) {
                    clsUser ObjUser=(clsUser) List.get(Integer.toString(i));
                    ComboData aData=new ComboData();
                    aData.Code=(int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
                    
                    if(ObjUser.getAttribute("USER_ID").getVal()==EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    }
                    else {
                        cmbToModel.addElement(aData);
                    }
                }
                //------------------------------ //
            }
            else {
                //----- Generate cmbType ------- //
                cmbToModel=new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);
                
                String ProformaNo=(String)ObjSalesParty.getAttribute("F6_ID").getObj();
                
                List=clsFeltProductionApprovalFlow.getRemainingUsers(clsFeltF.ModuleID,ProformaNo);
                for(int i=1;i<=List.size();i++) {
                    clsUser ObjUser=(clsUser) List.get(Integer.toString(i));
                    ComboData aData=new ComboData();
                    aData.Code=(int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
                    cmbToModel.addElement(aData);
                }
                //------------------------------ //
            }
        }
        catch(Exception e)
        {}
        
    }
    
    private void SetFields(boolean pStat) {
       
        txtfromdate.setEnabled(pStat);
        txttodate.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        cmdAdd.setEnabled(pStat);
        cmdItemdelete.setEnabled(pStat);
        
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        
        
        //chkOtherparty.setEnabled(pStat);
        
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
    }
    
    private boolean Validate() {
        int ValidEntryCount=0;
        
        if (txtfromdate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Party Code");
            txtfromdate.requestFocus(true);
            return false;
        }
        //Now Header level validations
        if(txtfromdate.getText().trim().equals("")&&OpgFinal.isSelected()) {
            JOptionPane.showMessageDialog(null,"Please enter Party Code");
            txtfromdate.requestFocus(true);
            return false;
        }
        
        
        if(!txtfromdate.getText().trim().equals("")) {
            if(EditMode==EITLERPGLOBAL.ADD) {
                /*if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE F6_ID='"+txtf6no.getText().trim()+"'"))  {
                    JOptionPane.showMessageDialog(null,"Party Code already exists!!");
                    txtfromdate.requestFocus(true);
                    return false;
                }*/
            }
        }
        
      /*if(txtPieceNo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Piece Number");
            return false;
        }
       */
        
        if(cmbHierarchy.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(null,"Please select the hierarchy.");
            return false;
        }
        
        if((!OpgApprove.isSelected())&&(!OpgReject.isSelected())&&(!OpgFinal.isSelected())&&(!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null,"Please select the Approval Action");
            return false;
        }
        
        if(OpgReject.isSelected()&&txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter the remarks for rejection");
            return false;
        }
        
        if( (OpgApprove.isSelected()||OpgReject.isSelected())&&cmbSendTo.getItemCount()<=0) {
            JOptionPane.showMessageDialog(null,"Please select the user, to whom rejected document to be send");
            return false;
        }
        
        if (OpgFinal.isSelected()) {
            if (txtfromdate.getText().trim().substring(0,4).equals("NEWD")) {
                JOptionPane.showMessageDialog(null,"Invalid Party Code");
                txtfromdate.requestFocus(true);
                return false;
            }
        }
        return true;
    }
    
    private void ClearFields() {
       
        txtfromdate.setText("");
        txttodate.setText("");
        txtToRemarks.setText("");
        FormatGrid();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
        //GenerateGrid();
    }
    
    private void Edit() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        
        //----------------------------------//
        String docno=ObjSalesParty.getAttribute("F6_ID").getString();
        if(ObjSalesParty.IsEditable(EITLERPGLOBAL.gCompanyID, docno, EITLERPGLOBAL.gNewUserID)) {
            EditMode=EITLERPGLOBAL.EDIT;
            
            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//
            
            if(clsFeltProductionApprovalFlow.IsCreator(clsFeltF.ModuleID,docno)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7038,70382)) {
                SetFields(true);
            }
            else {
                EnableApproval();
            }
            
            
            // cmdAdd.setEnabled(false);
            
            
            cmdAdd.setVisible(true);
            cmdItemdelete.setVisible(true);
            
            
            //DisplayData();
            DisableToolbar();
        }
        else {
            JOptionPane.showMessageDialog(null,"You cannot edit this record. \n It is either approved/rejected or waiting approval for other user");
        }
    }
    
    private void Delete() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        String lDocNo=(String)ObjSalesParty.getAttribute("MM_PARTY_CODE").getObj();
        
        if(ObjSalesParty.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            if(ObjSalesParty.Delete(EITLERPGLOBAL.gNewUserID)) {
                MoveLast();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while deleting. \nError is "+ObjSalesParty.LastError);
            }
        }
        else {
            JOptionPane.showMessageDialog(null,"You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
        }
    }
    
    private void Save() {
        
        
        //Form level validations
        if(Validate()==false) {
            return; //Validation failed
        }
        
        
        if(cmbHierarchy.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(null,"Please select the hierarchy.");
            return;
        }
        
        if((!OpgApprove.isSelected())&&(!OpgReject.isSelected())&&(!OpgFinal.isSelected())&&(!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null,"Please select the Approval Action");
            return;
        }
        
        //Check the no. of items
        if(TableDesc.getRowCount()<=0) {
            JOptionPane.showMessageDialog(null,"Please enter some items.");
            return;
        }
        
        if(OpgReject.isSelected()&&txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter the remarks for rejection");
            return;
        }
        
        if( (OpgApprove.isSelected()||OpgReject.isSelected())&&cmbSendTo.getItemCount()<=0) {
            JOptionPane.showMessageDialog(null,"Please select the user, to whom rejected document to be send");
            return;
        }
        
        SetData();
        if(EditMode==EITLERPGLOBAL.ADD) {
            
            if(ObjSalesParty.Insert()) {
                // MoveLast();
                DisplayData();
                //     String tnxtno="";
                //   tnxtno=clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 731, FFNo,  true);
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. \nError is "+ObjSalesParty.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjSalesParty.Update()) {
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. \nError is "+ObjSalesParty.LastError);
                return;
            }
        }
        
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        try {
            frmPA.RefreshView();
        }catch(Exception e){}
    }
    
    private void Cancel() {
        DisplayData();
        EditMode=0;
        SetFields(false);
        //chkOtherparty.setSelected(false);
        EnableToolbar();
        SetMenuForRights();
    }
    
    private void Find() {
        Loader ObjLoader=new Loader(this,"EITLERP.Production.FeltF.frmFeltFFind",true);
        frmFeltFFind ObjReturn= (frmFeltFFind) ObjLoader.getObj();
        
        if(ObjReturn.Cancelled==false) {
            if(!ObjSalesParty.Filter(ObjReturn.stringFindQuery)) {
                JOptionPane.showMessageDialog(null,"No records found.");
                ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID);
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
            if(EditMode==0) {
                if(ObjSalesParty.getAttribute("APPROVED").getInt()==1) {
                    
                    lblTitle.setBackground(Color.BLUE);
                }
                
                if(ObjSalesParty.getAttribute("APPROVED").getInt()!=1) {
                    lblTitle.setBackground(Color.GRAY);
                }
                
                if(ObjSalesParty.getAttribute("CANCELED").getInt()==1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
        }
        catch(Exception c) {
            
        }
        //============================================//
        
        //========= Authority Delegation Check =====================//
        if(EITLERPGLOBAL.gAuthorityUserID!=EITLERPGLOBAL.gUserID) {
            int ModuleID=clsFeltF.ModuleID;
            
            if(clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gUserID,EITLERPGLOBAL.gAuthorityUserID,ModuleID)) {
                EITLERPGLOBAL.gNewUserID=EITLERPGLOBAL.gAuthorityUserID;
            }
            else {
                EITLERPGLOBAL.gNewUserID=EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//
        
        try {
            ClearFields();
            // boolean bState = false;
            lblTitle.setText("FELT F6 FORM-"+(String)ObjSalesParty.getAttribute("F6_ID").getObj());
            txtf6no.setText((String)ObjSalesParty.getAttribute("F6_ID").getObj());
            txtfromdate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("F6_FROM_DATE").getString()));
            txttodate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("F6_TO_DATE").getString()));           
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());
            
                        
            //===================Fill up Table===================//
            FormatGrid();
            DoNotEvaluate=true;
            //Now Generate Table
            for(int i=1;i<=ObjSalesParty.colMRItems.size();i++) {
                
                
                clsFeltFitem ObjItem=(clsFeltFitem)ObjSalesParty.colMRItems.get(Integer.toString(i));
                Object[] rowData=new Object[14];
                
               // rowData[0]=(String)ObjItem.getAttribute(" ").getObj();
                rowData[0]=(String)ObjItem.getAttribute("INVOICE_NO").getObj();
                //rowData[1]=(String)ObjItem.getAttribute("INVOICE_DATE").getObj();
                rowData[1]=EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("INVOICE_DATE").getObj());
                rowData[2]=(String)ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[3]=(String)ObjItem.getAttribute("PARTY_NAME").getObj();
                rowData[4]=(String)ObjItem.getAttribute("INVOICE_AMT").getObj();
                rowData[5]=(String)ObjItem.getAttribute("EXT1").getObj();
                rowData[6]=(String)ObjItem.getAttribute("EXT2").getObj();
                rowData[7]=(String)ObjItem.getAttribute("EXT3").getObj();
                rowData[8]=(String)ObjItem.getAttribute("EXT4").getObj();
                rowData[9]=(String)ObjItem.getAttribute("EXT5").getObj();
                rowData[10]=(String)ObjItem.getAttribute("EXT6").getObj();
                
                
                
                DataModelDesc.addRow(rowData);
            }
            
            DoNotEvaluate=false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap List=new HashMap();
            
            String ProformaNo=ObjSalesParty.getAttribute("F6_ID").getString();
            List=clsFeltProductionApprovalFlow.getDocumentFlow(clsFeltF.ModuleID, ProformaNo);
            for(int i=1;i<=List.size();i++) {
                clsDocFlow ObjFlow=(clsDocFlow)List.get(Integer.toString(i));
                Object[] rowData=new Object[7];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(int)ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2]=(String)ObjFlow.getAttribute("STATUS").getObj();
                rowData[3]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID,clsUser.getDeptID(EITLERPGLOBAL.gCompanyID,(int)ObjFlow.getAttribute("USER_ID").getVal()));
                rowData[4]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6]=(String)ObjFlow.getAttribute("REMARKS").getObj();
                DataModelA.addRow(rowData);
            }
            //============================================================//
            
            //Showing Audit Trial History
            FormatGridUpdateHistory();
            HashMap History=clsFeltF.getHistoryList(EITLERPGLOBAL.gCompanyID, ProformaNo);
            for(int i=1;i<=History.size();i++) {
                clsFeltF ObjHistory=(clsFeltF)History.get(Integer.toString(i));
                Object[] rowData=new Object[5];
                
                rowData[0]=Integer.toString((int)ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjHistory.getAttribute("UPDATE_BY").getVal());
                rowData[2]=EITLERPGLOBAL.formatDate((String)ObjHistory.getAttribute("ENTRY_DATE").getString());
                String ApprovalStatus="";
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus="Approved";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus="Final Approved";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus="Waiting";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus="Rejected";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus="Pending";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus="Skiped";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus="Hold";
                }
                
                rowData[3]=ApprovalStatus;
                rowData[4]=ObjHistory.getAttribute("APPROVER_REMARKS").getString();
                DataModelHS.addRow(rowData);
            }
            //=========================================//
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Display Data Error: " + e.getMessage());
        }
    }
    
    
    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
     
        ObjSalesParty.setAttribute("F6_ID",txtf6no.getText());
        ObjSalesParty.setAttribute("F6_FROM_DATE",EITLERPGLOBAL.formatDateDB(txtfromdate.getText()));
        ObjSalesParty.setAttribute("F6_TO_DATE",EITLERPGLOBAL.formatDateDB(txttodate.getText()));
       
        //----- Update Approval Specific Fields -----------//
        ObjSalesParty.setAttribute("HIERARCHY_ID",EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjSalesParty.setAttribute("FROM",EITLERPGLOBAL.gNewUserID);
        ObjSalesParty.setAttribute("TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjSalesParty.setAttribute("REJECTED_REMARKS",txtToRemarks.getText());
        
        if(OpgApprove.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS","A");
        }
        
        if(OpgFinal.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS","F");
        }
        
        if(OpgReject.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS","R");
            ObjSalesParty.setAttribute("SEND_DOC_TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        }
        
        if(OpgHold.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS","H");
        }
        //-------------------------------------------------//
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            ObjSalesParty.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
            ObjSalesParty.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else {
            ObjSalesParty.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            ObjSalesParty.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        //======= Set Line part ============
        ObjSalesParty.colMRItems.clear();
        String mposition;
        for(int i=0;i<TableDesc.getRowCount();i++) {
            clsFeltFitem ObjItem=new clsFeltFitem();
            
            
            ObjItem.setAttribute("F6_ID",txtf6no.getText());
          
            ObjItem.setAttribute("INVOICE_NO",(String)TableDesc.getValueAt(i,0));
            ObjItem.setAttribute("INVOICE_DATE",(String)TableDesc.getValueAt(i,1));
            ObjItem.setAttribute("PARTY_CODE",(String)TableDesc.getValueAt(i,2));
            ObjItem.setAttribute("PARTY_NAME",(String)TableDesc.getValueAt(i,3));
            ObjItem.setAttribute("INVOICE_AMT",(String)TableDesc.getValueAt(i,4));
            ObjItem.setAttribute("EXT1",(String)TableDesc.getValueAt(i,5));
            ObjItem.setAttribute("EXT2",(String)TableDesc.getValueAt(i,6));
            ObjItem.setAttribute("EXT3",(String)TableDesc.getValueAt(i,7));
            ObjItem.setAttribute("EXT4",(String)TableDesc.getValueAt(i,8));
            ObjItem.setAttribute("EXT5",(String)TableDesc.getValueAt(i,9));
            ObjItem.setAttribute("EXT6",(String)TableDesc.getValueAt(i,10));
            
            ObjSalesParty.colMRItems.put(Integer.toString(ObjSalesParty.colMRItems.size()+1), ObjItem);
            
        }
        
        
    }
    
    private void FormatGridApprovalStatus() {
        DataModelA=new EITLTableModel();
        
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
    
    private void FormatGridDiscount(){
        
    }
    
    private void FormatGridOtherpartyDiscount(){
        
    }
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID,8023,80231)) { //7008,70081
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8023,80232)) { //7008,70082
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8023,80233)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8023,80234)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            //cmdPreview.setEnabled(false);
            //cmdPrint.setEnabled(false);
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
        String FieldName="";
        int SelHierarchy=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        
        for(int i=0;i<Tab1.getComponentCount()-1;i++) {
            if(Tab1.getComponent(i).getName()!=null) {
                
                FieldName=Tab1.getComponent(i).getName();
                
                //   if(FieldName.trim().equals("MM_PARTY_CODE")) {
                //    int a=0;
                //   }
                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    
                    Tab1.getComponent(i).setEnabled(true);
                }
                
            }
        }
        //=============== Header Fields Setup Complete =================//
        
        //=============== Setting Table Fields ==================//
        DataModelDesc.ClearAllReadOnly();
        for(int i=0;i<TableDesc.getColumnCount();i++) {
            FieldName=DataModelDesc.getVariable(i);
            
            if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
                //Do Nothing
            }
            else {
                DataModelDesc.SetReadOnly(i);
            }
        }
        //=======================================================//
    }
    
    
    
    
   /*
    private void FormatGridUpdateHistory() {
        DataModelUpdateHistory=new EITLTableModel();
    
        TableHS.removeAll();
        TableHS.setModel(DataModelUpdateHistory);
    
        TableColumnModel ColModel=TableHS.getColumnModel();
        //TableUpdateHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
        //Set the table Readonly
        DataModelUpdateHistory.TableReadOnly(true);
    
        //Add the columns
        DataModelUpdateHistory.addColumn("Rev No.");
        DataModelUpdateHistory.addColumn("User");
        DataModelUpdateHistory.addColumn("Date");
        DataModelUpdateHistory.addColumn("Status");
        DataModelUpdateHistory.addColumn("Remarks");
    
        TableHS.getColumnModel().getColumn(0).setMaxWidth(150);
        TableHS.getColumnModel().getColumn(3).setMaxWidth(100);
    
    }
    */
    
    
    
    private void FormatGridUpdateHistory() {
        DataModelHS=new EITLTableModel();
        
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
        //ObjSalesParty.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsFeltF.ModuleID+")");
        ObjSalesParty.Filter(" F6_ID IN (SELECT PRODUCTION.FELT_MACHINE_MASTER_HEADER.F6_ID FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_HEADER.F6_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=731)");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }
    
    
    public void FindEx(int pCompanyID,String pPartyCode) {
        
        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjSalesParty.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjSalesParty.Filter(" F6_ID='"+pPartyCode+"'");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }
    
    
    private void GenerateRejectedUserCombo() {
        
        HashMap List=new HashMap();
        HashMap DeptList=new HashMap();
        HashMap DeptUsers=new HashMap();
        String PartyCode=txtfromdate.getText();
        
        //----- Generate cmbType ------- //
        cmbToModel=new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbToModel);
        
        
        //Now Add other hierarchy Users
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        
        List=clsHierarchy.getUserList((int)EITLERPGLOBAL.gCompanyID,SelHierarchyID,EITLERPGLOBAL.gNewUserID,true);
        for(int i=1;i<=List.size();i++) {
            clsUser ObjUser=(clsUser) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjUser.getAttribute("USER_ID").getVal();
            aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
            
            
            
            /// NEW CODE ///
            boolean IncludeUser=false;
            //Decide to include user or not
            if(EditMode==EITLERPGLOBAL.EDIT) {
                if(OpgApprove.isSelected()) {
                    IncludeUser=clsFeltProductionApprovalFlow.IncludeUserInApproval(clsFeltF.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
                    //IncludeUser=clsFeltProductionApprovalFlow.IncludeUserInRejection(clsFeltF.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                    IncludeUser=true;
                }
                
                if(IncludeUser&&(((int) ObjUser.getAttribute("USER_ID").getVal())!=EITLERPGLOBAL.gNewUserID)) {
                    cmbToModel.addElement(aData);
                }
            }
            else {
                if(((int) ObjUser.getAttribute("USER_ID").getVal())!=EITLERPGLOBAL.gNewUserID) {
                    cmbToModel.addElement(aData);
                }
            }
            /// END NEW CODE ///
        }
        //------------------------------ //
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            String machinedocno=(String)ObjSalesParty.getAttribute("MM_PARTY_CODE").getObj();
            int Creator=clsFeltProductionApprovalFlow.getCreator(clsFeltF.ModuleID, machinedocno);
            EITLERPGLOBAL.setComboIndex(cmbSendTo,Creator);
        }
        
    }
    
    
  
    
    private void FormatGrid() {
       
        Updating=true; //Stops recursion
        try{
            cmdAdd.requestFocus();
            
            DataModelDesc=new EITLTableModel();
            TableDesc.removeAll();
            TableDesc.setModel(DataModelDesc);
            TableColumnModel ColModel=TableDesc.getColumnModel();
            TableDesc.setAutoResizeMode(TableDesc.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);
            
            
           // DataModelDesc.addColumn("Sr.");    //0
            DataModelDesc.addColumn("Invoice No");    //1
            DataModelDesc.addColumn("Invoice Date");    //2
            DataModelDesc.addColumn("Party Code");    //3
            DataModelDesc.addColumn("Party Name");    //4
            DataModelDesc.addColumn("Invoice Amt");    //5
            DataModelDesc.addColumn("Ext1");    //6
            DataModelDesc.addColumn("Ext2");    //7
            DataModelDesc.addColumn("Ext3");    //8
            DataModelDesc.addColumn("Ext4");    //9
            DataModelDesc.addColumn("Ext5");    //10
            DataModelDesc.addColumn("Ext6");    //11
           
            
            
           /*
            //DataModelDesc.TableReadOnly(true);
            DataModelDesc.SetVariable(0,"SR_NO");  //0
            DataModelDesc.SetVariable(1,"PARTY_CODE");    //1
            DataModelDesc.SetVariable(2,"PARTY_NAME");    //2
            DataModelDesc.SetVariable(3,"INVOICE_NO");    //3
            DataModelDesc.SetVariable(4,"INVOICE_DATE");    //43
            DataModelDesc.SetVariable(5,"INVOICE_AMT");    //44
            DataModelDesc.SetVariable(6,"EXT1");    //4
            DataModelDesc.SetVariable(7,"EXT2");    //5
            DataModelDesc.SetVariable(8,"EXT3");    //6
            DataModelDesc.SetVariable(9,"EXT4");    //7
            DataModelDesc.SetVariable(10,"EXT5");    //8
            DataModelDesc.SetVariable(11,"EXT6");    //9
            
          */  
            
            
            
            /*
            DataModelDesc.SetReadOnly(0);
            DataModelDesc.SetReadOnly(3);
            DataModelDesc.SetReadOnly(4);
            DataModelDesc.SetReadOnly(7);
            DataModelDesc.SetReadOnly(26);
            DataModelDesc.SetReadOnly(27);
            DataModelDesc.SetReadOnly(28);
            DataModelDesc.SetReadOnly(29);
             */
            /*DataModelDesc.SetVariable(25,"CHEM_TRT_IN"); //25
            DataModelDesc.SetVariable(26,"PIN_IND"); //26
            DataModelDesc.SetVariable(27,"CHARGES"); //27
            DataModelDesc.SetVariable(28,"SPR_IND"); //28
            DataModelDesc.SetVariable(29,"SQM_IND"); //29
             */
            //DataModelDesc.TableReadOnly(false);
            
                   
          //  DataModelDesc.SetReadOnly(0);
        //    DataModelDesc.SetReadOnly(1);
        //    DataModelDesc.SetReadOnly(2);
        //    DataModelDesc.SetReadOnly(3);
        //    DataModelDesc.SetReadOnly(4);
        //    DataModelDesc.SetReadOnly(5);
          //  DataModelDesc.SetReadOnly(6);
            
           
            
            
        
            
            //------- Install Table List Selection Listener ------//
            
            
            TableDesc.getColumnModel().getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    int last=TableDesc.getSelectedColumn();
                    String strVar=DataModelDesc.getVariable(last);
                    
                    //=============== Cell Editing Routine =======================//
                    try {
                        cellLastValue=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),TableDesc.getSelectedColumn());
                        
                        TableDesc.editCellAt(TableDesc.getSelectedRow(),TableDesc.getSelectedColumn());
                        if(TableDesc.getEditorComponent() instanceof JTextComponent) {
                            ((JTextComponent)TableDesc.getEditorComponent()).selectAll();
                        }
                    }
                    catch(Exception cell){}
                    //============= Cell Editing Routine Ended =================//
                    
                    ShowMessage("Ready...");
                    
                  
                    //       if(last==7){
                    //         TableDesc.editCellAt(TableDesc.getSelectedRow(),TableDesc.getSelectedColumn()+1);
                    //    }
                }
            }
            );
            
         
            
        }
        catch(Exception e) {
            
        }
        Updating=false;
        //Table formatting completed
        
        
    }
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
        
    }
    
    private void PreviewReport() {
        HashMap Params=new HashMap();
        
        try {
            //   URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptProforma.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&DocNo="+txtProformano.getText()+"&ProformaDate="+EITLERPGLOBAL.formatDateDB(txtProformaDate.getText()));
            // EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        }
    }
    
    
     private void GenerateData() {
        try {
            
            String strSQL="SELECT * FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' ";
          //  String strSQL1="SELECT * FROM D_SAL_INVOICE_TAX WHERE TAX_FL_CD='FF' AND TAX_TR_TYPE='RC' AND TAX_INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND TAX_INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' AND CST_AMT#0 ORDER BY TAX_INVOICE_DATE";

            
           ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                   // objRow=objReportData.newRow();                    
                    Object[] rowData=new Object[7];
                    
                    //objRow=objReportData.newRow();
                    
                  //  rowData[0]=UtilFunctions.getString(rsTmp,"","");
                    rowData[0]=UtilFunctions.getString(rsTmp,"INVOICE_NO","");
                    rowData[1]=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00"));
                    rowData[2]=UtilFunctions.getString(rsTmp,"PARTY_CODE","");
                    rowData[3]=UtilFunctions.getString(rsTmp,"PARTY_NAME","");
                    rowData[4]=UtilFunctions.getString(rsTmp,"TOTAL_NET_AMOUNT","");
                  
                    DataModelDesc.addRow(rowData);
                    rsTmp.next();
                    
                }
            }
            
          
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}



