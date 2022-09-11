/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */

package EITLERP.Production.FeltMachineSurvey;
/**
 *
 * @author Dharmendra
 */
/*<APPLET CODE=frmmachinesurveyamend.class HEIGHT=574 WIDTH=758></APPLET>*/

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
import EITLERP.frmPendingApprovals;


//import EITLERP.Purchase.frmSendMail;


public class frmmachinesurveyamend extends javax.swing.JApplet {
    
    private int EditMode=0;
    
    //private clsmachinesurveyamend ObjSalesParty;
    private clsmachinesurveyamend ObjSalesParty;
    
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
    private int charge09index=0;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    String cellLastValue="";
    boolean mchinests=false;
    public boolean PENDING_DOCUMENT=false; //for refresh pending document module
    
    /** Creates new form frmSalesParty */
    public frmmachinesurveyamend() {
        System.gc();
        setSize(800,700);
        initComponents();
        cmdDelete.setEnabled(false);
        cmdDelete.disable();
        cmdDelete.setVisible(false);
        
        cmdPrint.setEnabled(false);
        cmdPrint.disable();
        cmdPrint.setVisible(false);
        
        
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
        
        ObjSalesParty = new clsmachinesurveyamend();
        
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
        
        txtAuditRemarks.setVisible(false);
        DataModelDesc.TableReadOnly(true);
        
        
        //        GeneratePreviousDiscount();
        //      FormatGridOtherpartyDiscount();
        //FormatGridDiscount();
    }
    
   /* private void ChargeCodeCombos(String strCon) {
        //----------Charge Code---------//
    
    
        //------------------------------//
    
        //----------Second Charge Code---------//
    
        //------------------------------//
    }*/
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel=new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsmachinesurveyamend.ModuleID+"");
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsmachinesurveyamend.ModuleID+"");
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
        cmdAdd = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        cmdEditPiecedetail = new javax.swing.JButton();
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
        jButton5 = new javax.swing.JButton();
        txtPartycode = new javax.swing.JTextField();
        txtPartyname = new javax.swing.JTextField();
        txtPartystation = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblamdno = new javax.swing.JLabel();
        lbl_amdno = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txt_amd_reason = new javax.swing.JTextField();

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
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("FELT MACHINE SURVEY AMEND");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
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
        jScrollPane1.setBounds(10, 20, 720, 270);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        Tab1.add(cmdAdd);
        cmdAdd.setBounds(570, 310, 70, 25);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Tab1.add(jButton1);
        jButton1.setBounds(660, 360, 90, 25);

        cmdEditPiecedetail.setText("Edit");
        cmdEditPiecedetail.setEnabled(false);
        cmdEditPiecedetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditPiecedetailActionPerformed(evt);
            }
        });

        Tab1.add(cmdEditPiecedetail);
        cmdEditPiecedetail.setBounds(650, 310, 80, 25);

        jTabbedPane1.addTab("MACHINE DETAIL", Tab1);

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
        jTabbedPane1.setBounds(5, 150, 780, 430);

        txtPartycode.setToolTipText("Search by F1");
        txtPartycode.setDisabledTextColor(new java.awt.Color(102, 102, 255));
        txtPartycode.setEnabled(false);
        txtPartycode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartycodeFocusGained(evt);
            }
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
        txtPartycode.setBounds(120, 100, 110, 20);

        txtPartyname.setBackground(new java.awt.Color(204, 204, 204));
        txtPartyname.setEditable(false);
        txtPartyname.setDisabledTextColor(new java.awt.Color(51, 51, 255));
        //txtPartyname = new JTextFieldHint(new JTextField(),"Party Name");
        getContentPane().add(txtPartyname);
        txtPartyname.setBounds(240, 100, 360, 19);

        txtPartystation.setBackground(new java.awt.Color(204, 204, 204));
        txtPartystation.setEditable(false);
        txtPartystation.setDisabledTextColor(new java.awt.Color(102, 102, 255));
        getContentPane().add(txtPartystation);
        txtPartystation.setBounds(640, 80, 130, 19);

        jLabel8.setText("Party Code");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(10, 100, 90, 20);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14));
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(20, 600, 610, 20);

        lblamdno.setText("AMENDMENT NO.");
        getContentPane().add(lblamdno);
        lblamdno.setBounds(10, 80, 110, 20);

        getContentPane().add(lbl_amdno);
        lbl_amdno.setBounds(120, 80, 100, 20);

        jLabel1.setText("Enter Reason to Amendment... :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 125, 220, 20);

        txt_amd_reason.setEditable(false);
        getContentPane().add(txt_amd_reason);
        txt_amd_reason.setBounds(240, 130, 360, 19);

    }//GEN-END:initComponents
    
    private void txtPartycodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartycodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Press F1 for Party Code List...");
    }//GEN-LAST:event_txtPartycodeFocusGained
    
    private void txtPartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartycodeFocusLost
        try{
            String mpartycd=txtPartycode.getText().trim();
            if(!txtPartycode.getText().equals("")){
                String strSQL="";
                ResultSet rsTmp;
                strSQL= "";
                strSQL+="SELECT PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+txtPartycode.getText().trim()+"";
                rsTmp=data.getResult(strSQL);
                if(rsTmp.first()){
                    rsTmp.first();
                    txtPartyname.setText(rsTmp.getString("PARTY_NAME"));
                    txtPartystation.setText(rsTmp.getString("DISPATCH_STATION"));
                    //data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_POSITION_AMEND SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MP_PARTY_CODE='"+mpartycd+"'");
                    
                    strSQL="SELECT * FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO="+txtPartycode.getText().trim()+" AND MODULE_ID=724 AND STATUS='W' ";
                    rsTmp=data.getResult(strSQL);
                    if(rsTmp.first()){
                        JOptionPane.showMessageDialog(null,"Party is exist In Felt Machine Survey Entry Form but Final approved...");
                        return;
                    }
                    
                    strSQL="SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MP_PARTY_CODE="+txtPartycode.getText().trim()+"";
                    rsTmp=data.getResult(strSQL);
                    if(!rsTmp.first()){
                        JOptionPane.showMessageDialog(null,"Party is not exist First Add Party to Felt Machine Survey Entry Form...");
                        return;
                    }else{
                        if(ObjSalesParty.LoadPartyData(EITLERPGLOBAL.gCompanyID,txtPartycode.getText().trim())) {
                            ObjSalesParty.MovePLast();
                            DisplayPData();
                            //data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND WHERE MP_AMD_PARTY_CODE='"+mpartycd+"'");
                            return;
                        }
                        else {
                            JOptionPane.showMessageDialog(null,"Error occured while loading data. \n Error is "+ObjSalesParty.LastError);
                        }
                    }
                }
                else{
                    Cancel();
                    JOptionPane.showMessageDialog(null,"No Such Party exist in Party Master...");
                    return;
                }
                txtPartycode.setText(mpartycd);
                strSQL="SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MP_PARTY_CODE="+txtPartycode.getText().trim()+"";
                rsTmp=data.getResult(strSQL);
                if(!rsTmp.first()){
                    JOptionPane.showMessageDialog(null,"Party is not exist First Add Party to Felt Machine Survey Entry Form...");
                    return;
                }
                else{
                    strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CODE="+txtPartycode.getText().trim()+"";
                    rsTmp=data.getResult(strSQL);
                    rsTmp.first();
                    if(rsTmp.getInt("COUNT")==0) {
                        Cancel();
                        JOptionPane.showMessageDialog(null,"No Such Party exist in Party Master...");
                        return;
                    }
                }
            }
        }
        catch(Exception e){
            
        }
        //GeneratePreviousDiscount();
    }//GEN-LAST:event_txtPartycodeFocusLost
    
    private void txtPartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartycodeKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CODE IN(SELECT DISTINCT(MP_PARTY_CODE) FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER )ORDER BY PARTY_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            //aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                txtPartycode.setText(aList.ReturnVal);
                txtPartyname.setText(clsFeltparty.getParyName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                //txtPartyname.setText(aList.ReturnVal);
                //System.out.println(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                txtPartystation.setText(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                
            }
        }
    }//GEN-LAST:event_txtPartycodeKeyPressed
    
    private void cmdEditPiecedetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditPiecedetailActionPerformed
        // TODO add your handling code here:
        
        cmdAdd.setEnabled(false);        
        DataModelDesc.SetReadOnly(0);
        DataModelDesc.ResetReadOnly(1);
        DataModelDesc.ResetReadOnly(2);
        DataModelDesc.ResetReadOnly(5);
        DataModelDesc.ResetReadOnly(6);
        DataModelDesc.ResetReadOnly(8);
        DataModelDesc.ResetReadOnly(9);
        DataModelDesc.ResetReadOnly(10);
        DataModelDesc.ResetReadOnly(11);
        DataModelDesc.ResetReadOnly(12);
        DataModelDesc.ResetReadOnly(13);
        DataModelDesc.ResetReadOnly(14);
        DataModelDesc.ResetReadOnly(15);
        DataModelDesc.ResetReadOnly(16);
        DataModelDesc.ResetReadOnly(17);
        DataModelDesc.ResetReadOnly(18);
        DataModelDesc.ResetReadOnly(19);
        DataModelDesc.ResetReadOnly(20);
        DataModelDesc.ResetReadOnly(21);
        DataModelDesc.ResetReadOnly(22);
        DataModelDesc.ResetReadOnly(23);
        DataModelDesc.ResetReadOnly(24);
        DataModelDesc.ResetReadOnly(26);
        DataModelDesc.ResetReadOnly(27);        
        DataModelDesc.SetReadOnly(25);
        DataModelDesc.SetReadOnly(28);
    }//GEN-LAST:event_cmdEditPiecedetailActionPerformed
    
    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed
        if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT){
            if(evt.getKeyCode()==112) //F1 Key pressed
            {
                if(TableDesc.getSelectedColumn()==2) {
                    LOV aList=new LOV();
                    
                    String strSQL = "SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST ORDER BY POSITION_NO";
                    aList.SQL=strSQL;
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    //aList.DefaultSearchOn=2;
                    aList.DefaultSearchOn=1;
                    
                    
                    if(aList.ShowLOV()) {
                        if(TableDesc.getCellEditor()!=null) {
                            TableDesc.getCellEditor().stopCellEditing();
                        }
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),2);
                    }
                }
                if(TableDesc.getSelectedColumn()==25) {
                    LOV aList=new LOV();
                    String strSQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_STATUS'";
                    aList.SQL=strSQL;
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    //aList.DefaultSearchOn=2;
                    aList.DefaultSearchOn=1;
                    
                    
                    if(aList.ShowLOV()) {
                        if(TableDesc.getCellEditor()!=null) {
                            TableDesc.getCellEditor().stopCellEditing();
                        }
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),25);
                    }
                }
            }
            
        }
    }//GEN-LAST:event_TableDescKeyPressed
    
    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased
        
        String Position=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 2);
        String machine=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 1);
        String mpositiondesc=clsmachinesurveyamend.getpositiondesc(Position);
        TableDesc.setValueAt(mpositiondesc, TableDesc.getSelectedRow(), 3);
        String mcombicd=clsmachinesurveyamend.getcombinationcode(Position,machine);
        TableDesc.setValueAt(mcombicd, TableDesc.getSelectedRow(), 4);
        String mlength=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 5);
        String mwidth=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 6);
        String mactive=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),25);
        String mconsumption=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),27);
        
        
        double mcon=0.00;
        double mconsum=0.00;
        double mshrdin=0.00;
        int tcon=0;
        if(mconsumption.equals("")){
            mcon=1;
        }else {
            try{
                
                tcon=Integer.parseInt(mconsumption.substring(mconsumption.compareToIgnoreCase(".")));
                
                mcon=Double.parseDouble(mconsumption);
                
                if(tcon>0){
                    JOptionPane.showMessageDialog(null,"Life of Felt is in Days & Days is always in number only...");
                }
                
            }catch(Exception e) {
                
            }
            mcon=Double.parseDouble(mconsumption);
            mconsum=365/mcon;
            mconsum=Math.ceil(mconsum);
        }
        
        String mshrdinesh=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),29);
        if(mshrdinesh.equals("")){
            mshrdin=0;
        }else{
            mshrdin=Double.parseDouble(mshrdinesh);
        }
        String msize="";
        
        if(!mlength.equals("") && !mwidth.equals("")) {
            msize=mlength+" X "+mwidth;
        }
        if(mactive.equalsIgnoreCase("ACTIVE") || mactive.equalsIgnoreCase("DEACTIVE")){
            
        }
        else{
            if(mactive.length()>0){
                JOptionPane.showMessageDialog(null,"MACHINE STATUS SHOULD BE EITHER Active OR Deactive...");
            }
            
        }
        if(mcon<0){
            JOptionPane.showMessageDialog(null,"Life of Felt should not be Less than Zero...");
        }else{
            if(mshrdin>0){
                if(mshrdin>mconsum){
                    ShowMessage("Dinesh Share shold not be more than Consumption...");
                }
            }
        }
        TableDesc.setValueAt(msize, TableDesc.getSelectedRow(), 7);
        TableDesc.setValueAt(String.valueOf(mconsum) , TableDesc.getSelectedRow(), 28);
        
    }//GEN-LAST:event_TableDescKeyReleased
    
    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        
        if(txtPartycode.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Enter Party Code Detail First.");
            return;
        }
        else {
            
            cmdEditPiecedetail.disable();
            cmdEditPiecedetail.enableInputMethods(false);
            cmdEditPiecedetail.setEnabled(false);
            Updating=true;
            Object[] rowData=new Object[30];
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
            rowData[14]="";
            rowData[15]="";
            rowData[16]="";
            rowData[17]="";
            rowData[18]="";
            rowData[19]="";
            rowData[20]="";
            rowData[21]="";
            rowData[22]="";
            rowData[23]="";
            rowData[24]="";
            rowData[25]="";
            rowData[26]="";
            rowData[27]="";
            rowData[28]="";
            rowData[29]="";
            
            
            DataModelDesc.addRow(rowData);
            DataModelDesc.SetReadOnly(0);
            DataModelDesc.ResetReadOnly(1);
            DataModelDesc.ResetReadOnly(2);
            DataModelDesc.ResetReadOnly(5);
            DataModelDesc.ResetReadOnly(6);
            DataModelDesc.ResetReadOnly(8);
            DataModelDesc.ResetReadOnly(9);
            DataModelDesc.ResetReadOnly(10);
            DataModelDesc.ResetReadOnly(11);
            DataModelDesc.ResetReadOnly(12);
            DataModelDesc.ResetReadOnly(13);
            DataModelDesc.ResetReadOnly(14);
            DataModelDesc.ResetReadOnly(15);
            DataModelDesc.ResetReadOnly(16);
            DataModelDesc.ResetReadOnly(17);
            DataModelDesc.ResetReadOnly(18);
            DataModelDesc.ResetReadOnly(19);
            DataModelDesc.ResetReadOnly(20);
            DataModelDesc.ResetReadOnly(21);
            DataModelDesc.ResetReadOnly(22);
            DataModelDesc.ResetReadOnly(23);
            DataModelDesc.ResetReadOnly(24);
            DataModelDesc.ResetReadOnly(26);
            DataModelDesc.ResetReadOnly(27);
            
            
            DataModelDesc.SetReadOnly(25);
            DataModelDesc.SetReadOnly(28);
            //Updating=false;
            TableDesc.changeSelection(TableDesc.getRowCount()-1, 1, false,false);
            //TableDesc.setValueAt(Integer.toString(EITLERPGLOBAL.gNewUserID), TableDesc.getSelectedRow(), 26);
            //TableDesc.setValueAt(EITLERPGLOBAL.getCurrentDateDB(), TableDesc.getSelectedRow(), 27);
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
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            
            //cmbSendTo.setEnabled(false);
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
        // TODO add your handling code here:
        String PartyID=txtPartycode.getText().trim();
        
        SetupApproval();
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if(clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsmachinesurveyamend.ModuleID,PartyID)) {
                cmbSendTo.setEnabled(true);
            }
            else {
                //cmbSendTo.setEnabled(false);
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
    
    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        //String ProformaNo=txtProformano.getText();
        //ObjSalesParty.ShowHistory(EITLERPGLOBAL.gCompanyID, ProformaNo);
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
        txtPartycode.enable();
        txtPartycode.setEditable(true);
        txtPartycode.requestFocus();
        txt_amd_reason.setText("");
        txt_amd_reason.setEditable(true);
        Add();
        // GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNewActionPerformed
    
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        txtPartycode.setEditable(false);
        txtPartyname.setEditable(false);
        txtPartystation.setEditable(false);
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
        txt_amd_reason.setEditable(false);
        Save();
        
        // GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdSaveActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
        txt_amd_reason.setEditable(false);
        txt_amd_reason.setText("");
        ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID);
        //MoveFirst();
        MoveLast();
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
    private javax.swing.JButton cmdEditPiecedetail;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lbl_amdno;
    private javax.swing.JLabel lblamdno;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtPartycode;
    private javax.swing.JTextField txtPartyname;
    private javax.swing.JTextField txtPartystation;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txt_amd_reason;
    // End of variables declaration//GEN-END:variables
    
    private void Add() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        //Now Generate new document no.
        SelectFirstFree aList=new SelectFirstFree();
        aList.ModuleID=725;
        
        if(aList.ShowList()) {
            EditMode=EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            //cmdEditPiecedetail.setEnabled(false);
            SelPrefix=aList.Prefix; //Selected Prefix;
            SelSuffix=aList.Suffix;
            FFNo=aList.FirstFreeNo;
            SetupApproval();
            //Display newly generated document no.
            lbl_amdno.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 725, FFNo,  false));
            //txtProformano.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 725, FFNo,  false));
            txtPartycode.requestFocus();
            
            lblTitle.setText("FELT MACHINE SURVEY AMEND");
            lblTitle.setBackground(Color.BLUE);
        }
        else {
            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
        
        //EditMode=EITLERPGLOBAL.ADD;
        //SetFields(true);
        //DisableToolbar();
        //ClearFields();
        //SetupApproval();
        
        
        //int Counter=data.getIntValueFromDB("SELECT MAX(CONVERT(SUBSTR(PARTY_CODE,5),SIGNED))+1 FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE LIKE 'N%'");
        //txtPartyCode.setText("NEWD"+Counter);
        //lblTitle.setBackground(Color.BLUE);
        
        //txtInsuranceCode.setText("01");
        //Object Obj = "09";
        //Object Obj = "Other";
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
            
            int FromUserID=clsFeltProductionApprovalFlow.getFromID( clsmachinesurveyamend.ModuleID,(String)ObjSalesParty.getAttribute("PARTY_CODE").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=clsFeltProductionApprovalFlow.getFromRemarks(clsmachinesurveyamend.ModuleID,FromUserID,(String)ObjSalesParty.getAttribute("PARTY_CODE").getObj());
            
            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }
        
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if(clsHierarchy.CanSkip( (int)EITLERPGLOBAL.gCompanyID,SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            //cmbSendTo.setEnabled(false);
        }
        
        if(clsHierarchy.CanFinalApprove((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        }
        else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
        
        
        //In Edit Mode Hierarchy Should be disabled
        if(EditMode==EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
        }
        
        if(EditMode==0) {
            //Disable all hierarchy controls if not in Add/Edit Mode
            cmbHierarchy.setEnabled(false);
            txtFrom.setEnabled(false);
            txtFromRemarks.setEnabled(false);
            OpgApprove.setEnabled(false);
            OpgFinal.setEnabled(false);
            OpgReject.setEnabled(false);
            //cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }
    }
    
    private void GenerateFromCombo() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        
        try {
            //if(EditMode==EITLERPGLOBAL.ADD) {
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
            //}
            /*else {
                //----- Generate cmbType ------- //
                cmbToModel=new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);
             
                String ProformaNo=(String)ObjSalesParty.getAttribute("MP_AMD_PARTY_CODE").getObj();
             
                List=clsFeltProductionApprovalFlow.getRemainingUsers(clsmachinesurveyamend.ModuleID,ProformaNo);
                for(int i=1;i<=List.size();i++) {
                    clsUser ObjUser=(clsUser) List.get(Integer.toString(i));
                    ComboData aData=new ComboData();
                    aData.Code=(int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
                    cmbToModel.addElement(aData);
                }
                //------------------------------ //
            }*/
        }
        catch(Exception e)
        {}
        
    }
    
    private void SetFields(boolean pStat) {
        txtPartycode.setEnabled(pStat);
        txtPartyname.setEnabled(pStat);
        txtPartystation.setEnabled(pStat);
        //txtProformano.setEnabled(pStat);
        //txtProformaDate.setEnabled(pStat);
        //txtContact.setEnabled(pStat);
        //txtPhone.setEnabled(pStat);
        cmdAdd.setEnabled(pStat);
        //cmdItemdelete.setEnabled(pStat);
        cmdEditPiecedetail.setEnabled(pStat);
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
        
        if (txtPartycode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Party Code");
            txtPartycode.requestFocus(true);
            return false;
        }
        //Now Header level validations
        if(txtPartycode.getText().trim().equals("")&&OpgFinal.isSelected()) {
            JOptionPane.showMessageDialog(null,"Please enter Party Code");
            txtPartycode.requestFocus(true);
            return false;
        }
        
        if(txt_amd_reason.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Reason for Amendment...");
            txt_amd_reason.setEditable(true);
            txt_amd_reason.requestFocus(true);
            return false;
        }
        
        
        /*if(!txtPartycode.getText().trim().equals("")) {
            if(EditMode==EITLERPGLOBAL.ADD) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MP_PARTY_CODE='"+txtPartycode.getText().trim()+"'"))  {
                    JOptionPane.showMessageDialog(null,"Party Code already exists!!");
                    txtPartycode.requestFocus(true);
                    return false;
                }
            }
        }
         */
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
            if (txtPartycode.getText().trim().substring(0,4).equals("NEWD")) {
                JOptionPane.showMessageDialog(null,"Invalid Party Code");
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
        /*txtProformano.setText("");
        txtProformaDate.setText(EITLERPGLOBAL.getCurrentDate());
        txtContact.setText("");
        txtPhone.setText("");
         
        txtremark1.setText("");
        txtremark2.setText("");
        txtremark3.setText("");
        txtremark4.setText("");
        txtremark5.setText("");
         */
        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        FormatGrid();
        FormatGridA();
        FormatGridHS();
        //GenerateGrid();
    }
    
    private void Edit() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        
        //----------------------------------//
        String lProformaNo=ObjSalesParty.getAttribute("MP_AMD_NO").getString();
        if(ObjSalesParty.IsEditable(EITLERPGLOBAL.gCompanyID, lProformaNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode=EITLERPGLOBAL.EDIT;
            
            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//
            
            if(clsFeltProductionApprovalFlow.IsCreator(clsmachinesurveyamend.ModuleID,lProformaNo)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7008,70082)) {
                SetFields(true);
            }
            else {
                EnableApproval();
            }
            
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
        
        String lDocNo=(String)ObjSalesParty.getAttribute("MP_AMD_PARTY_CODE").getObj();
        
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
        
        
        //Check for New Item Code and Final Approval Action. In such case, do not allow new item code while final approving
       /*
        if(OpgFinal.isSelected()) {
            for(int i=0;i<TableDesc.getRowCount();i++) {
                String ItemID=(String)TableDesc.getValueAt(i, 1);
        
                if(ItemID.trim().equals("NEWITEM")) {
                    JOptionPane.showMessageDialog(null,"You cannot final approve the Proforma Invoice with NEWITEM item. Please replace these items with new item codes and then save");
                    return;
                }
            }
        }
        */
        //Form level validations
        /*if(txtProformaDate.getText().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Proforma date");
            return;
        }*/
        if(cmbHierarchy.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(null,"Please select the hierarchy.");
            return;
        }
        
        if((!OpgApprove.isSelected())&&(!OpgReject.isSelected())&&(!OpgFinal.isSelected())&&(!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null,"Please select the Approval Action");
            return;
        }
        /*
        //Table level validation
        float maxdiscper=Float.parseFloat((String)TableDiscount.getValueAt(0,2));
        for(int i=0;i<=TableDesc.getRowCount();i++){
            float Discper=Float.parseFloat((String)TableDesc.getValueAt(i,11));
            if(Discper>=maxdiscper){
         
               JOptionPane.showMessageDialog(null,"Please select discount percentage less than "+maxdiscper+"");
            return;
         
                }
         
        }
         */
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
                String tnxtno="";
                tnxtno=clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 725, FFNo,  true);
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
        Loader ObjLoader=new Loader(this,"EITLERP.Production.FeltMachineSurvey.frmmachinesurveyFindamend",true);
        frmmachinesurveyFindamend ObjReturn= (frmmachinesurveyFindamend) ObjLoader.getObj();
        
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
            int ModuleID=clsmachinesurveyamend.ModuleID;
            
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
            lblTitle.setText("FELT MACHINE SURVEY AMEND");
            txtPartycode.setText((String)ObjSalesParty.getAttribute("MP_AMD_PARTY_CODE").getObj());
            txtPartyname.setText((String)ObjSalesParty.getAttribute("PARTY_NAME").getObj());
            txtPartystation.setText((String)ObjSalesParty.getAttribute("DISPATCH_STATION").getObj());
            /*txtProformano.setText((String)ObjSalesParty.getAttribute("PROFORMA_NO").getObj());
            lblProRevNo.setText(Integer.toString((int)ObjSalesParty.getAttribute("REVISION_NO").getVal()));
            txtProformaDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("PROFORMA_DATE").getString()));
            txtContact.setText(ObjSalesParty.getAttribute("CONTACT").getString());
             
            txtremark1.setText(ObjSalesParty.getAttribute("REMARK1").getString());
            txtremark2.setText(ObjSalesParty.getAttribute("REMARK2").getString());
            txtremark3.setText(ObjSalesParty.getAttribute("REMARK3").getString());
            txtremark4.setText(ObjSalesParty.getAttribute("REMARK4").getString());
            txtremark5.setText(ObjSalesParty.getAttribute("REMARK5").getString());
             
             
             */
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());
            
            DoNotEvaluate=true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table
            for(int i=1;i<=ObjSalesParty.colMRItems.size();i++) {
                
                clsmachinesurveyitemamend ObjItem=(clsmachinesurveyitemamend)ObjSalesParty.colMRItems.get(Integer.toString(i));
                Object[] rowData=new Object[40];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=(String)ObjItem.getAttribute("MP_AMD_MACHINE_NO").getObj();
                if(ObjItem.getAttribute("MP_AMD_POSITION").getObj().toString().equals("")){
                    rowData[2]="";
                }
                else {
                    rowData[2]=Integer.toString((100+Integer.parseInt((String)ObjItem.getAttribute("MP_AMD_POSITION").getObj()))).substring(1,3);
                }
                
                rowData[3]=(String)ObjItem.getAttribute("MP_AMD_POSITION_DESC").getObj();
                rowData[4]=(String)ObjItem.getAttribute("MP_AMD_COMBINATION_CODE").getObj();
                //rowData[5]=Float.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("MP_LENGTH").getVal(),2));
                rowData[5]=(String)ObjItem.getAttribute("MP_AMD_ORDER_LENGTH").getObj();
                rowData[6]=(String)ObjItem.getAttribute("MP_AMD_ORDER_WIDTH").getObj();
                //rowData[6]=Float.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("MP_WIDTH").getVal(),2));
                //rowData[6]=Integer.toString((int)ObjItem.getAttribute("MP_GSQ").getVal());
                rowData[7]=(String)ObjItem.getAttribute("MP_AMD_ORDER_SIZE").getObj();
                rowData[8]=(String)ObjItem.getAttribute("MP_AMD_PRESS_TYPE").getObj();
                rowData[9]=(String)ObjItem.getAttribute("MP_AMD_GSM_RANGE").getObj();
                
                rowData[10]=(String)ObjItem.getAttribute("MP_AMD_MAX_FELT_LENGTH").getObj();
                rowData[11]=(String)ObjItem.getAttribute("MP_AMD_MIN_FELT_LENGTH").getObj();
                //rowData[10]=Float.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("MP_MAX_FELT_LENGTH").getVal(),2));
                //rowData[11]=Float.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("MP_MIN_FELT_LENGTH").getVal(),2));
                rowData[12]=(String)ObjItem.getAttribute("MP_AMD_LINEAR_NIP_LOAD").getObj();
                rowData[13]=(String)ObjItem.getAttribute("MP_AMD_PAPERGRADE_CODE").getObj();
                rowData[14]=(String)ObjItem.getAttribute("MP_AMD_PAPERGRADE_DESC").getObj();
                rowData[15]=(String)ObjItem.getAttribute("MP_AMD_FURNISH").getObj();
                rowData[16]=(String)ObjItem.getAttribute("MP_AMD_TYPE").getObj();
                rowData[17]=(String)ObjItem.getAttribute("MP_AMD_SPEED").getObj();
                rowData[18]=EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("MP_AMD_SURVEY_DATE").getObj());
                rowData[19]=(String)ObjItem.getAttribute("MP_AMD_WIRE_LENGTH").getObj();
                rowData[20]=(String)ObjItem.getAttribute("MP_AMD_WIRE_WIDTH").getObj();
                rowData[21]=(String)ObjItem.getAttribute("MP_AMD_WIRE_TYPE").getObj();
                rowData[22]=(String)ObjItem.getAttribute("MP_AMD_TECH_REP").getObj();
                rowData[23]=(String)ObjItem.getAttribute("MP_AMD_TYPE_OF_FILLER").getObj();
                rowData[24]=(String)ObjItem.getAttribute("MP_AMD_PAPER_DECKLE").getObj();
                rowData[25]=(String)ObjItem.getAttribute("MP_AMD_MCH_ACTIVE").getObj();
                rowData[26]=(String)ObjItem.getAttribute("MP_AMD_PAPERGRADE").getObj();
                rowData[30]=(String)ObjItem.getAttribute("MP_AMD_NO").getObj();
                rowData[31]=(String)ObjItem.getAttribute("MP_AMD_REASON").getObj();
                rowData[27]=(String)ObjItem.getAttribute("MP_AMD_LIFE_OF_FELT").getObj();
                rowData[28]=(String)ObjItem.getAttribute("MP_AMD_CONSUMPTION").getObj();
                rowData[29]=(String)ObjItem.getAttribute("MP_AMD_DINESH_SHARE").getObj();
                
                lbl_amdno.setText((String)ObjItem.getAttribute("MP_AMD_NO").getObj());
                txt_amd_reason.setText((String)ObjItem.getAttribute("MP_AMD_REASON").getObj());
                //rowData[26]=Integer.toString((int)ObjItem.getAttribute("CREATED_BY").getVal());
                //rowData[27]=(String)ObjItem.getAttribute("CREATED_DATE").getObj();
                //rowData[28]=Integer.toString((int)ObjItem.getAttribute("MODIFIED_BY").getVal());
                //rowData[29]=(String)ObjItem.getAttribute("MODIFIED_DATE").getObj();
                
                DataModelDesc.addRow(rowData);
            }
            
            DoNotEvaluate=false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List=new HashMap();
            
            String ProformaNo=ObjSalesParty.getAttribute("MP_AMD_PARTY_CODE").getString();
            List=clsFeltProductionApprovalFlow.getDocumentFlow(clsmachinesurveyamend.ModuleID, ProformaNo);
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
            FormatGridHS();
            HashMap History=clsmachinesurveyamend.getHistoryList(EITLERPGLOBAL.gCompanyID, ProformaNo);
            for(int i=1;i<=History.size();i++) {
                clsmachinesurveyamend ObjHistory=(clsmachinesurveyamend)History.get(Integer.toString(i));
                Object[] rowData=new Object[5];
                
                rowData[0]=Integer.toString((int)ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjHistory.getAttribute("UPDATED_BY").getVal());
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
    
    
    private void DisplayPData() {
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
            int ModuleID=clsmachinesurveyamend.ModuleID;
            
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
            lblTitle.setText("FELT MACHINE SURVEY AMEND");
            txtPartycode.setText((String)ObjSalesParty.getAttribute("MP_PARTY_CODE").getObj());
            txtPartyname.setText((String)ObjSalesParty.getAttribute("PARTY_NAME").getObj());
            txtPartystation.setText((String)ObjSalesParty.getAttribute("DISPATCH_STATION").getObj());
            
            /*txtProformano.setText((String)ObjSalesParty.getAttribute("PROFORMA_NO").getObj());
            lblProRevNo.setText(Integer.toString((int)ObjSalesParty.getAttribute("REVISION_NO").getVal()));
            txtProformaDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("PROFORMA_DATE").getString()));
            txtContact.setText(ObjSalesParty.getAttribute("CONTACT").getString());
             
            txtremark1.setText(ObjSalesParty.getAttribute("REMARK1").getString());
            txtremark2.setText(ObjSalesParty.getAttribute("REMARK2").getString());
            txtremark3.setText(ObjSalesParty.getAttribute("REMARK3").getString());
            txtremark4.setText(ObjSalesParty.getAttribute("REMARK4").getString());
            txtremark5.setText(ObjSalesParty.getAttribute("REMARK5").getString());
             
             
             */
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());
            
            DoNotEvaluate=true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table
            for(int i=1;i<=ObjSalesParty.colMRItems.size();i++) {
                
                clsmachinesurveyitemamend ObjItem=(clsmachinesurveyitemamend)ObjSalesParty.colMRItems.get(Integer.toString(i));
                Object[] rowData=new Object[40];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=(String)ObjItem.getAttribute("MP_MACHINE_NO").getObj();
                rowData[2]=Integer.toString((100+Integer.parseInt((String)ObjItem.getAttribute("MP_POSITION").getObj()))).substring(1,3);
                rowData[3]=(String)ObjItem.getAttribute("MP_POSITION_DESC").getObj();
                rowData[4]=(String)ObjItem.getAttribute("MP_COMBINATION_CODE").getObj();
                //rowData[5]=Float.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("MP_LENGTH").getVal(),2));
                rowData[5]=(String)ObjItem.getAttribute("MP_ORDER_LENGTH").getObj();
                rowData[6]=(String)ObjItem.getAttribute("MP_ORDER_WIDTH").getObj();
                //rowData[6]=Float.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("MP_WIDTH").getVal(),2));
                //rowData[6]=Integer.toString((int)ObjItem.getAttribute("MP_GSQ").getVal());
                rowData[7]=(String)ObjItem.getAttribute("MP_ORDER_SIZE").getObj();
                rowData[8]=(String)ObjItem.getAttribute("MP_PRESS_TYPE").getObj();
                rowData[9]=(String)ObjItem.getAttribute("MP_GSM_RANGE").getObj();
                
                rowData[10]=(String)ObjItem.getAttribute("MP_MAX_FELT_LENGTH").getObj();
                rowData[11]=(String)ObjItem.getAttribute("MP_MIN_FELT_LENGTH").getObj();
                //rowData[10]=Float.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("MP_MAX_FELT_LENGTH").getVal(),2));
                //rowData[11]=Float.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("MP_MIN_FELT_LENGTH").getVal(),2));
                rowData[12]=(String)ObjItem.getAttribute("MP_LINEAR_NIP_LOAD").getObj();
                rowData[13]=(String)ObjItem.getAttribute("MP_PAPERGRADE_CODE").getObj();
                rowData[14]=(String)ObjItem.getAttribute("MP_PAPERGRADE_DESC").getObj();
                rowData[15]=(String)ObjItem.getAttribute("MP_FURNISH").getObj();
                rowData[16]=(String)ObjItem.getAttribute("MP_TYPE").getObj();
                rowData[17]=(String)ObjItem.getAttribute("MP_SPEED").getObj();
                rowData[18]=EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("MP_SURVEY_DATE").getObj());
                rowData[19]=(String)ObjItem.getAttribute("MP_WIRE_LENGTH").getObj();
                rowData[20]=(String)ObjItem.getAttribute("MP_WIRE_WIDTH").getObj();
                rowData[21]=(String)ObjItem.getAttribute("MP_WIRE_TYPE").getObj();
                rowData[22]=(String)ObjItem.getAttribute("MP_TECH_REP").getObj();
                rowData[23]=(String)ObjItem.getAttribute("MP_TYPE_OF_FILLER").getObj();
                rowData[24]=(String)ObjItem.getAttribute("MP_PAPER_DECKLE").getObj();
                rowData[25]=(String)ObjItem.getAttribute("MP_MCH_ACTIVE").getObj();
                rowData[26]=(String)ObjItem.getAttribute("MP_PAPERGRADE").getObj();
                
                rowData[27]=(String)ObjItem.getAttribute("MP_LIFE_OF_FELT").getObj();
                rowData[28]=(String)ObjItem.getAttribute("MP_CONSUMPTION").getObj();
                rowData[29]=(String)ObjItem.getAttribute("MP_DINESH_SHARE").getObj();
                //rowData[26]=Integer.toString((int)ObjItem.getAttribute("CREATED_BY").getVal());
                //rowData[27]=(String)ObjItem.getAttribute("CREATED_DATE").getObj();
                //rowData[28]=Integer.toString((int)ObjItem.getAttribute("MODIFIED_BY").getVal());
                //rowData[29]=(String)ObjItem.getAttribute("MODIFIED_DATE").getObj();
                
                DataModelDesc.addRow(rowData);
            }
            
            DoNotEvaluate=false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List=new HashMap();
            
            String ProformaNo=ObjSalesParty.getAttribute("MP_PARTY_CODE").getString();
            List=clsFeltProductionApprovalFlow.getDocumentFlow(clsmachinesurveyamend.ModuleID, ProformaNo);
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
            FormatGridHS();
            HashMap History=clsmachinesurveyamend.getHistoryList(EITLERPGLOBAL.gCompanyID, ProformaNo);
            for(int i=1;i<=History.size();i++) {
                clsmachinesurveyamend ObjHistory=(clsmachinesurveyamend)History.get(Integer.toString(i));
                Object[] rowData=new Object[5];
                
                rowData[0]=Integer.toString((int)ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjHistory.getAttribute("UPDATED_BY").getVal());
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
        //ObjSalesParty.setAttribute("PREFIX",SelPrefix);
        //ObjSalesParty.setAttribute("SUFFIX",SelSuffix);
        //ObjSalesParty.setAttribute("FFNO",FFNo);
        // ObjSalesParty.setAttribute("PROFORMA_DATE",EITLERPGLOBAL.formatDateDB(txtProformaDate.getText()));
        ObjSalesParty.setAttribute("MP_AMD_PARTY_CODE",txtPartycode.getText());
        ObjSalesParty.setAttribute("PARTY_NAME",txtPartyname.getText());
        ObjSalesParty.setAttribute("DISPATCH_STATION",txtPartystation.getText());
        /*ObjSalesParty.setAttribute("CONTACT",txtContact.getText());
        ObjSalesParty.setAttribute("PHONE",txtPhone.getText());
         
        ObjSalesParty.setAttribute("REMARK1",txtremark1.getText());
        ObjSalesParty.setAttribute("REMARK2",txtremark2.getText());
        ObjSalesParty.setAttribute("REMARK3",txtremark3.getText());
        ObjSalesParty.setAttribute("REMARK4",txtremark4.getText());
        ObjSalesParty.setAttribute("REMARK5",txtremark5.getText());
         */
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
            //ObjSalesParty.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
            //ObjSalesParty.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else {
            //ObjSalesParty.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            //ObjSalesParty.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        //======= Set Line part ============
        ObjSalesParty.colMRItems.clear();
        String mposition;
        for(int i=0;i<TableDesc.getRowCount();i++) {
            clsmachinesurveyitemamend ObjItem=new clsmachinesurveyitemamend();
            ObjItem.setAttribute("MP_AMD_PARTY_CODE",txtPartycode.getText());
            
            //Add Only Valid Items
            //if(clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, lItemID)) {
            //ObjItem.setAttribute("SR_NO",i);
            ObjItem.setAttribute("MP_AMD_MACHINE_NO",((String)TableDesc.getValueAt(i,1)));
            ObjItem.setAttribute("MP_AMD_POSITION",Integer.toString((100+Integer.parseInt(((String)TableDesc.getValueAt(i,2))))).substring(1,3));
            ObjItem.setAttribute("MP_AMD_POSITION_DESC",(String)TableDesc.getValueAt(i,3));
            ObjItem.setAttribute("MP_AMD_COMBINATION_CODE",(String)TableDesc.getValueAt(i,4));
            ObjItem.setAttribute("MP_AMD_ORDER_LENGTH",(String)TableDesc.getValueAt(i,5));
            ObjItem.setAttribute("MP_AMD_ORDER_WIDTH",(String)TableDesc.getValueAt(i,6));
            ObjItem.setAttribute("MP_AMD_ORDER_SIZE",(String)TableDesc.getValueAt(i,7));
            ObjItem.setAttribute("MP_AMD_PRESS_TYPE",(String)TableDesc.getValueAt(i,8));
            ObjItem.setAttribute("MP_AMD_GSM_RANGE",(String)TableDesc.getValueAt(i,9));
            ObjItem.setAttribute("MP_AMD_MAX_FELT_LENGTH",(String)TableDesc.getValueAt(i,10));
            ObjItem.setAttribute("MP_AMD_MIN_FELT_LENGTH",(String)TableDesc.getValueAt(i,11));
            ObjItem.setAttribute("MP_AMD_LINEAR_NIP_LOAD",(String)TableDesc.getValueAt(i,12));
            ObjItem.setAttribute("MP_AMD_PAPERGRADE",(String)TableDesc.getValueAt(i,26));
            ObjItem.setAttribute("MP_AMD_PAPERGRADE_CODE",(String)TableDesc.getValueAt(i,13));
            ObjItem.setAttribute("MP_AMD_PAPERGRADE_DESC",(String)TableDesc.getValueAt(i,14));
            ObjItem.setAttribute("MP_AMD_FURNISH",(String)TableDesc.getValueAt(i,15));
            ObjItem.setAttribute("MP_AMD_TYPE",(String)TableDesc.getValueAt(i,16));
            ObjItem.setAttribute("MP_AMD_SPEED",(String)TableDesc.getValueAt(i,17));
            ObjItem.setAttribute("MP_AMD_SURVEY_DATE",(String)TableDesc.getValueAt(i,18));
            ObjItem.setAttribute("MP_AMD_WIRE_LENGTH",(String)TableDesc.getValueAt(i,19));
            ObjItem.setAttribute("MP_AMD_WIRE_WIDTH",(String)TableDesc.getValueAt(i,20));
            ObjItem.setAttribute("MP_AMD_WIRE_TYPE",(String)TableDesc.getValueAt(i,21));
            ObjItem.setAttribute("MP_AMD_TECH_REP",(String)TableDesc.getValueAt(i,22));
            ObjItem.setAttribute("MP_AMD_TYPE_OF_FILLER",(String)TableDesc.getValueAt(i,23));
            ObjItem.setAttribute("MP_AMD_PAPER_DECKLE",(String)TableDesc.getValueAt(i,24));
            ObjItem.setAttribute("MP_AMD_MCH_ACTIVE",(String)TableDesc.getValueAt(i,25));
            ObjItem.setAttribute("MP_AMD_NO",lbl_amdno.getText().trim());
            ObjItem.setAttribute("MP_AMD_REASON",txt_amd_reason.getText().trim());
            ObjItem.setAttribute("MP_AMD_LIFE_OF_FELT",(String)TableDesc.getValueAt(i,27));
            ObjItem.setAttribute("MP_AMD_CONSUMPTION",(String)TableDesc.getValueAt(i,28));
            ObjItem.setAttribute("MP_AMD_DINESH_SHARE",(String)TableDesc.getValueAt(i,29));
            //ObjItem.setAttribute("CREATED_BY",(String)TableDesc.getValueAt(i,26));
            //ObjItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDateDB((String)TableDesc.getValueAt(i,27)));
            //ObjItem.setAttribute("MODIFIED_BY",(String)TableDesc.getValueAt(i,28));
            //ObjItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB((String)TableDesc.getValueAt(i,29)));
            
            
                /*ObjItem.setAttribute("CHEM_TRT_IN",(String)TableDesc.getValueAt(i,25));
                ObjItem.setAttribute("PIN_IND",(String)TableDesc.getValueAt(i,26));
                ObjItem.setAttribute("CHARGES",(String)TableDesc.getValueAt(i,27));
                ObjItem.setAttribute("SPR_IND",(String)TableDesc.getValueAt(i,28));
                ObjItem.setAttribute("SQM_IND",(String)TableDesc.getValueAt(i,29));
                 */
            ObjSalesParty.colMRItems.put(Integer.toString(ObjSalesParty.colMRItems.size()+1), ObjItem);
            //}
        }
        
    }
    
    private void FormatGridA() {
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
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID,7008,70081)) {
            cmdNew.setEnabled(true);
        }
        else {
            //cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7008,70083)) {
            cmdDelete.setEnabled(true);
        }
        else {
            //cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7008,70084)) {
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
                
                if(FieldName.trim().equals("MP_AMD_PARTY_CODE")) {
                    int a=0;
                }
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
    
    private void FormatGridHS() {
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
        //ObjSalesParty.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsmachinesurveyamend.ModuleID+")");
        ObjSalesParty.Filter(" MM_DOC_NO IN (SELECT PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725)");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }
    
    
    public void FindEx(int pCompanyID,String pamdno) {
        
        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjSalesParty.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjSalesParty.Filter(" MM_DOC_NO='"+pamdno+"'");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }
    
    
    private void GenerateRejectedUserCombo() {
        
        HashMap List=new HashMap();
        HashMap DeptList=new HashMap();
        HashMap DeptUsers=new HashMap();
        String PartyCode=txtPartycode.getText();
        
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
                    IncludeUser=clsFeltProductionApprovalFlow.IncludeUserInApproval(clsmachinesurveyamend.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
                    IncludeUser=clsFeltProductionApprovalFlow.IncludeUserInRejection(clsmachinesurveyamend.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String ProformaNo=(String)ObjSalesParty.getAttribute("MP_AMD_PARTY_CODE").getObj();
            int Creator=clsFeltProductionApprovalFlow.getCreator(clsmachinesurveyamend.ModuleID, ProformaNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo,Creator);
        }
        
    }
    
    
    private void UpdateSrNo() {
        int SrCol=DataModelDesc.getColFromVariable("SR_NO");
        
        for(int i=0;i<TableDesc.getRowCount();i++) {
            TableDesc.setValueAt(Integer.toString(i+1), i, SrCol);
        }
    }
    
    private void FormatGrid() {
       /* DataModelMainCode=new EITLTableModel();
        
        for(int i=1;i<=4;i++) {
            DataModelMainCode.SetReadOnly(i);
        }
        
        //Add Columns to it
        DataModelMainCode.addColumn("*"); //0 Selection
        DataModelMainCode.addColumn("Sr.");//1
        DataModelMainCode.addColumn("Main Account Code");//2
        DataModelMainCode.addColumn("Account Name");//3
        Rend.setCustomComponent(0,"CheckBox");
        */
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
            
            
            DataModelDesc.addColumn("Sr.");    //0
            DataModelDesc.addColumn("Machine No");    //1
            DataModelDesc.addColumn("Position");    //2
            DataModelDesc.addColumn("Position Desc");    //3
            DataModelDesc.addColumn("Combination Code");    //4
            DataModelDesc.addColumn("Order Length");    //5
            DataModelDesc.addColumn("Order Width");    //6
            DataModelDesc.addColumn("Order Size");    //7
            DataModelDesc.addColumn("Press Type");    //8
            DataModelDesc.addColumn("GSM Range");    //9
            DataModelDesc.addColumn("Max Felt Length");    //10
            DataModelDesc.addColumn("Min Felt Length");    //11
            DataModelDesc.addColumn("Linear Nip Load");    //12
            DataModelDesc.addColumn("Paper Grade Code");    //13
            DataModelDesc.addColumn("Paper Grade Description");    //14
            DataModelDesc.addColumn("Furnish");    //15
            DataModelDesc.addColumn("Machine Type");    //16
            DataModelDesc.addColumn("Speed");    //17
            DataModelDesc.addColumn("Survey Date");    //18
            DataModelDesc.addColumn("Wire Length");    //19
            DataModelDesc.addColumn("Wire Width");    //20
            DataModelDesc.addColumn("Wire Type");    //21
            DataModelDesc.addColumn("Tech Rep");    //22
            DataModelDesc.addColumn("Type Of Filler");    //23
            DataModelDesc.addColumn("Paper Deckle");    //24
            DataModelDesc.addColumn("Status");    //25
            //DataModelDesc.addColumn("Created By");    //26
            //DataModelDesc.addColumn("Created Date");    //27
            //DataModelDesc.addColumn("Modified By");    //28
            //DataModelDesc.addColumn("Modified Date");    //29
            DataModelDesc.addColumn("Paper Grade");    //30
            DataModelDesc.addColumn("Life of Felt");
            DataModelDesc.addColumn("Consumption");
            DataModelDesc.addColumn("Dinesh Share");
            
            
            
            //DataModelDesc.TableReadOnly(true);
            DataModelDesc.SetVariable(0,"SR_NO");  //0
            DataModelDesc.SetVariable(1,"MP_AMD_MACHINE_NO");    //1
            DataModelDesc.SetVariable(2,"MP_AMD_POSITION");    //2
            DataModelDesc.SetVariable(3,"MP_AMD_POSITION_DESC");    //3
            DataModelDesc.SetVariable(4,"MP_AMD_COMBINATION_CODE");    //4
            DataModelDesc.SetVariable(5,"MP_AMD_ORDER_LENGTH");    //5
            DataModelDesc.SetVariable(6,"MP_AMD_ORDER_WIDTH");    //6
            DataModelDesc.SetVariable(7,"MP_AMD_ORDER_SIZE");    //7
            DataModelDesc.SetVariable(8,"MP_AMD_PRESS_TYPE");    //8
            DataModelDesc.SetVariable(9,"MP_AMD_GSM_RANGE");    //9
            DataModelDesc.SetVariable(10,"MP_AMD_MAX_FELT_LENGTH");    //10
            DataModelDesc.SetVariable(11,"MP_AMD_MIN_FELT_LENGTH");    //11
            DataModelDesc.SetVariable(12,"MP_AMD_LINEAR_NIP_LOAD");    //12
            DataModelDesc.SetVariable(13,"MP_AMD_PAPERGRADE_CODE");    //13
            DataModelDesc.SetVariable(14,"MP_AMD_PAPERGRADE_DESC");    //13
            DataModelDesc.SetVariable(15,"MP_AMD_FURNISH");    //14
            DataModelDesc.SetVariable(16,"MP_AMD_TYPE");    //15
            DataModelDesc.SetVariable(17,"MP_AMD_SPEED");    //16
            DataModelDesc.SetVariable(18,"MP_AMD_SURVEY_DATE");    //17
            DataModelDesc.SetVariable(19,"MP_AMD_WIRE_LENGTH");    //18
            DataModelDesc.SetVariable(20,"MP_AMD_WIRE_WIDTH");    //19
            DataModelDesc.SetVariable(21,"MP_AMD_WIRE_TYPE");    //20
            DataModelDesc.SetVariable(22,"MP_AMD_TECH_REP");    //21
            DataModelDesc.SetVariable(23,"MP_AMD_TYPE_OF_FILLER");    //22
            DataModelDesc.SetVariable(24,"MP_AMD_PAPER_DECKLE");    //23
            DataModelDesc.SetVariable(25,"MP_AMD_MCH_ACTIVE");    //24
            //DataModelDesc.SetVariable(26,"CREATED_BY");    //25
            //DataModelDesc.SetVariable(27,"CREATED_DATE");    //26
            //DataModelDesc.SetVariable(28,"MODIFIED_BY");    //27
            //DataModelDesc.SetVariable(29,"MODIFIED_DATE");    //28
            DataModelDesc.SetVariable(26,"MP_AMD_PAPERGRADE");    //28
            DataModelDesc.SetVariable(27,"MP_AMD_LIFE_OF_FELT");
            DataModelDesc.SetVariable(28,"MP_AMD_CONSUMPTION");
            DataModelDesc.SetVariable(29,"MP_AMD_DINESH_SHARE");
            
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
            
            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            TableDesc.getColumnModel().getColumn(16).setPreferredWidth(100);
            
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
                    
                    if(last==2) {
                        ShowMessage("Press F1 for Position Code List...");
                    }
                    if(last==25){
                        ShowMessage("MACHINE STATUS SHOULD BE EITHER Active OR Deactive... Press F1 for Selection...");
                    }
                    if(last==7){
                        TableDesc.editCellAt(TableDesc.getSelectedRow(),TableDesc.getSelectedColumn()+1);
                    }
                    if(last==27){
                        ShowMessage("Enter Days for Life of Felt...");
                    }
                }
            }
            );
            
            //===================================================//
            
            
            //----- Install Table Model Event Listener -------//
            /*
            TableDesc.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int col = e.getColumn();
             
                        //=========== Cell Update Prevention Check ===========//
                        String curValue=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), e.getColumn());
                        if(curValue.equals(cellLastValue)) {
                            return;
                        }
                        //====================================================//
             
                        if(DoNotEvaluate) {
                            return;
                        }
                        if(col==1){
                            try {
             
                                //String lItemID=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),1);
                                // String lItemName=clsmachinesurveyamend.getItemName(txtPartycode.getText(),lItemID);
                                //  TableDesc.setValueAt(lItemName, TableDesc.getSelectedRow(), 2);
             
             
             
             
             
             
             
                                /*int lItemPosition=clsmachinesurveyamend.getItemPosition(txtPartycode.getText(), lItemID);
                                Table.setValueAt(Integer.toString(lItemUnit),Table.getSelectedRow(),6);
                                String lUnitName=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", lItemUnit);
                                Table.setValueAt(lUnitName,Table.getSelectedRow(),7);
             
             
                                String lItemPosition=clsmachinesurveyamend.getItemPosition(txtPartycode.getText(),lItemID);
                                TableDesc.setValueAt(lItemPosition, TableDesc.getSelectedRow(), 3);
                                String lItemLength=clsmachinesurveyamend.getItemLength(txtPartycode.getText(),lItemID);
                                TableDesc.setValueAt(lItemLength, TableDesc.getSelectedRow(), 4);
                               double lItemWidth=clsmachinesurveyamend.getItemWidth(txtPartycode.getText(),lItemID);
                                TableDesc.setValueAt(Double.toString(lItemWidth), TableDesc.getSelectedRow(), 5);
                                int lItemGsq=clsmachinesurveyamend.getItemGsq(txtPartycode.getText(),lItemID);
                                TableDesc.setValueAt(Integer.toString(lItemGsq), TableDesc.getSelectedRow(), 6);
                                String lItemStyle=clsmachinesurveyamend.getItemStyle(txtPartycode.getText(),lItemID);
                                TableDesc.setValueAt(lItemStyle, TableDesc.getSelectedRow(), 7);
             */
                                /*
                                String []Piecedetail=clsmachinesurveyamend.getPiecedetail(txtPartycode.getText());
                                 
                                TableDesc.setValueAt(Piecedetail[1],TableDesc.getSelectedRow(),1);
                                TableDesc.setValueAt(Piecedetail[2],TableDesc.getSelectedRow(),2);
                                TableDesc.setValueAt(Piecedetail[3],TableDesc.getSelectedRow(),3);
                                TableDesc.setValueAt(Piecedetail[4],TableDesc.getSelectedRow(),4);
                                TableDesc.setValueAt(Piecedetail[5],TableDesc.getSelectedRow(),5);
                                TableDesc.setValueAt(Piecedetail[6],TableDesc.getSelectedRow(),6);
                                TableDesc.setValueAt(Piecedetail[7],TableDesc.getSelectedRow(),7);
                                TableDesc.setValueAt(Piecedetail[8],TableDesc.getSelectedRow(),8);
                                TableDesc.setValueAt(Piecedetail[9],TableDesc.getSelectedRow(),9);
                                TableDesc.setValueAt(Piecedetail[10],TableDesc.getSelectedRow(),10);
                                TableDesc.setValueAt(Piecedetail[11],TableDesc.getSelectedRow(),11);
                                TableDesc.setValueAt(Piecedetail[12],TableDesc.getSelectedRow(),12);
                                TableDesc.setValueAt(Piecedetail[13],TableDesc.getSelectedRow(),13);
                                TableDesc.setValueAt(Piecedetail[14],TableDesc.getSelectedRow(),14);
                                TableDesc.setValueAt(Piecedetail[15],TableDesc.getSelectedRow(),15);
                                TableDesc.setValueAt(Piecedetail[16],TableDesc.getSelectedRow(),16);
                                TableDesc.setValueAt(Piecedetail[17],TableDesc.getSelectedRow(),17);
                                TableDesc.setValueAt(Piecedetail[18],TableDesc.getSelectedRow(),18);
                                TableDesc.setValueAt(Piecedetail[19],TableDesc.getSelectedRow(),19);
                                TableDesc.setValueAt(Piecedetail[20],TableDesc.getSelectedRow(),20);
                                TableDesc.setValueAt(Piecedetail[21],TableDesc.getSelectedRow(),21);
                                TableDesc.setValueAt(Piecedetail[22],TableDesc.getSelectedRow(),22);
                                TableDesc.setValueAt(Piecedetail[23],TableDesc.getSelectedRow(),23);
                                TableDesc.setValueAt(Piecedetail[24],TableDesc.getSelectedRow(),24);
                                TableDesc.setValueAt(Piecedetail[25],TableDesc.getSelectedRow(),25);
                                TableDesc.setValueAt(Piecedetail[26],TableDesc.getSelectedRow(),26);
                                TableDesc.setValueAt(Piecedetail[27],TableDesc.getSelectedRow(),27);
                                TableDesc.setValueAt(Piecedetail[28],TableDesc.getSelectedRow(),28);
                                TableDesc.setValueAt(Piecedetail[29],TableDesc.getSelectedRow(),29);
                                 
                                 
                                /*TableDesc.setValueAt(Piecedetail[31],TableDesc.getSelectedRow(),25);
                                TableDesc.setValueAt(Piecedetail[32],TableDesc.getSelectedRow(),26);
                                TableDesc.setValueAt(Piecedetail[33], TableDesc.getSelectedRow(),27);
                                TableDesc.setValueAt(Piecedetail[34],TableDesc.getSelectedRow(),28);
                                TableDesc.setValueAt(Piecedetail[35],TableDesc.getSelectedRow(),29);
                                 */
            
                            /*
                            }
                            catch(Exception ex){
                            }
                        }
                    }
                }
            }
            );*/
            
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
            //URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptProforma.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&DocNo="+txtProformano.getText()+"&ProformaDate="+EITLERPGLOBAL.formatDateDB(txtProformaDate.getText()));
            //EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        }
    }
    
    /*private void GeneratePreviousDiscount(){
        try{
            FormatGridDiscount();  //clear existing content of table
            String strPartycode=txtPartycode.getText().toString();
            ResultSet rsTmp,rsBuyer,rsIndent,rsRIA;
            String strSQL= "";
            //strSQL+="SELECT DISTINCT DISC_PER FROM PRODUCTION.FELT_DISCOUNT_MEMO ";
            //strSQL+="SELECT DISTINCT PRODUCT_CD,DISC_PER FROM PRODUCTION.FELT_DISCOUNT_MEMO ";
            strSQL+="SELECT PRODUCT_CD,DISC_PER,MAX(MEMO_DATE) AS CURRENT_MEMO_DATE,COUNT(PIECE_NO) AS COUNT_PCS FROM PRODUCTION.FELT_DISCOUNT_MEMO WHERE ";
            if(!txtPartycode.getText().equals("")){
                //strSQL+="WHERE PARTY_CODE="+strPartycode+" ORDER BY DISC_PER DESC";
                //strSQL+="DISC_PER!=0 AND PARTY_CODE="+strPartycode+" AND MEMO_DATE>'2012-01-01'";
                //strSQL+="WHERE DISC_PER!=0 AND MEMO_DATE>'1900-01-01' AND PARTY_CODE='"+strPartycode+"';
                strSQL+=" PARTY_CODE="+strPartycode+" AND ";
            }
            strSQL+="DISC_PER!=0 AND MEMO_DATE>'1900-01-01' GROUP BY PRODUCT_CD,DISC_PER ORDER BY DISC_PER DESC";
            rsTmp=data.getResult(strSQL);
            //rsTmp.first();
            if(rsTmp.getRow()>0) {
                int cnt=0;
                while(!rsTmp.isAfterLast()) {
                    cnt++;
                    Object[] rowData=new Object[5];
                    rowData[0]=Integer.toString(cnt);
                    rowData[1]=rsTmp.getString("PRODUCT_CD");
                    rowData[2]=rsTmp.getString("DISC_PER");
                    rowData[3]=EITLERPGLOBAL.formatDate(rsTmp.getString("CURRENT_MEMO_DATE"));
                    rowData[4]=rsTmp.getString("COUNT_PCS");
                    DataModelDiscount.addRow(rowData);
                    //   System.out.println("rsTmp.getString('PIECE_NO')");
                    rsTmp.next();
                }
     
            }
        }
     
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
     */
    
    
}
