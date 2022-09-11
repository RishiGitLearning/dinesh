package EITLERP.Production.FeltTarget;

/**
 *
 *
 */
/*<APPLET CODE=frmSalesParty.class HEIGHT=574 WIDTH=758></APPLET>*/
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

//import EITLERP.Purchase.frmSendMail;
public class frmTargetEntry extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsDiscRateMaster ObjFeltTarget;
    private clsTargetEntry ObjFeltTarget;

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;

    private EITLTableModel DataModelDesc;
    private EITLTableModel DataModelA;
    private EITLTableModel DataModelHS;

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private boolean Updating = false;
    private boolean DoNotEvaluate = false;

    private boolean HistoryView = false;
    private String theDocNo = "";
    public frmPendingApprovals frmPA;
    private int charge09index = 0;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();
    String cellLastValue = "";
    public boolean PENDING_DOCUMENT = false;
    private Connection Conn;

    public String[] p;
    public String[] q;

    /**
     * Creates new form frmSalesParty
     */
    public frmTargetEntry() {
        System.gc();
        setSize(800, 600);
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
        //cmdEmail.setIcon(EITLERPGLOBAL.getImage("EMAIL"));

        //ObjColumn.LoadData((int)EITLERPGLOBAL.gCompanyID);
        GenerateCombos();
        //FormatGrid();
        //GenerateGrid();

        //  cmdSelectAll.setEnabled(false);
        //  cmdClearAll.setEnabled(false);
        ObjFeltTarget = new clsTargetEntry();

        //lblDocThrough.setVisible(false);
        //txtDocThrough.setVisible(false);
        SetMenuForRights();

        if (ObjFeltTarget.LoadData(EITLERPGLOBAL.gCompanyID)) {
            ObjFeltTarget.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data. \n Error is " + ObjFeltTarget.LastError);
        }
        
        txtAuditRemarks.setVisible(false);
        txtPartycode.setEnabled(false);
        DataModelDesc.TableReadOnly(true);

        //        GeneratePreviousDiscount();
        //        FormatGridOtherpartyDiscount();
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
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsTargetEntry.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsTargetEntry.ModuleID + "");
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
        cmdEditPiecedetail = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
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
        txtPartycode = new javax.swing.JTextField();
        txtPartyname = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        txtFromDate = new javax.swing.JTextField();
        lblUnadjNo = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtFromToYear = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField();
        lblUnadjNo1 = new javax.swing.JLabel();
        label1 = new java.awt.Label();

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
        lblTitle.setText("FELT TARGET QUARTER WISE ENTRY ");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 780, 25);

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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TableDescKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(TableDesc);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 40, 740, 250);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        Tab1.add(cmdAdd);
        cmdAdd.setBounds(500, 10, 70, 25);

        cmdItemdelete.setText("Remove");
        cmdItemdelete.setEnabled(false);
        cmdItemdelete.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdItemdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdItemdeleteActionPerformed(evt);
            }
        });
        Tab1.add(cmdItemdelete);
        cmdItemdelete.setBounds(580, 10, 80, 25);

        cmdEditPiecedetail.setText("Edit");
        cmdEditPiecedetail.setEnabled(false);
        cmdEditPiecedetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditPiecedetailActionPerformed(evt);
            }
        });
        Tab1.add(cmdEditPiecedetail);
        cmdEditPiecedetail.setBounds(670, 10, 50, 25);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(650, 300, 90, 30);

        jLabel1.setText("* Enter full amount in target");
        Tab1.add(jLabel1);
        jLabel1.setBounds(10, 20, 280, 17);

        jTabbedPane1.addTab("Target Detail", Tab1);

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

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
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
        cmdNext3.setBounds(550, 300, 102, 30);

        jButton3.setText("Next >>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(jButton3);
        jButton3.setBounds(650, 300, 100, 30);

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
        jScrollPane6.setBounds(10, 190, 540, 120);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        StatusPanel.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(570, 220, 132, 24);

        txtAuditRemarks.setEnabled(false);
        StatusPanel.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(570, 250, 129, 27);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(570, 287, 130, 30);

        jTabbedPane1.addTab("Status", StatusPanel);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 210, 780, 360);

        txtPartycode.setEditable(false);
        txtPartycode = new JTextFieldHint(new JTextField(),"Search By F1");
        txtPartycode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartycodeActionPerformed(evt);
            }
        });
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
        txtPartycode.setBounds(180, 180, 110, 20);

        txtPartyname.setEditable(false);
        txtPartyname.setBackground(new java.awt.Color(204, 204, 204));
        //txtPartyname = new JTextFieldHint(new JTextField(),"Party Name");
        getContentPane().add(txtPartyname);
        txtPartyname.setBounds(300, 180, 360, 20);

        jLabel8.setText("Party Code");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(90, 180, 80, 20);

        txtDocNo.setEditable(false);
        txtDocNo.setEnabled(false);
        getContentPane().add(txtDocNo);
        txtDocNo.setBounds(70, 70, 110, 20);

        txtFromDate.setEditable(false);
        txtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromDateFocusLost(evt);
            }
        });
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(180, 150, 110, 20);

        lblUnadjNo.setText("Doc No :");
        getContentPane().add(lblUnadjNo);
        lblUnadjNo.setBounds(10, 70, 70, 20);

        txtToDate.setEditable(false);
        txtToDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToDateActionPerformed(evt);
            }
        });
        txtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToDateFocusLost(evt);
            }
        });
        getContentPane().add(txtToDate);
        txtToDate.setBounds(370, 150, 110, 20);

        jLabel2.setText("To Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(300, 150, 70, 20);

        txtFromToYear.setEditable(false);
        txtFromToYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromToYearFocusLost(evt);
            }
        });
        getContentPane().add(txtFromToYear);
        txtFromToYear.setBounds(190, 100, 180, 20);

        jLabel5.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel5.setText("Target Financial Year");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(10, 100, 170, 20);

        jLabel4.setText("From Date :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(90, 150, 90, 20);

        txtDocDate.setEditable(false);
        txtDocDate.setEnabled(false);
        getContentPane().add(txtDocDate);
        txtDocDate.setBounds(280, 70, 110, 20);

        lblUnadjNo1.setText("Doc Date :");
        getContentPane().add(lblUnadjNo1);
        lblUnadjNo1.setBounds(210, 70, 80, 20);

        label1.setText("Ex: 2016-2017");
        getContentPane().add(label1);
        label1.setBounds(190, 120, 110, 19);
    }// </editor-fold>//GEN-END:initComponents

    private void txtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromDateFocusLost
        // TODO add your handling code here:
        if (txtFromDate.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter From Date.");
            txtFromDate.requestFocus(true);
            //return;
        }

    }//GEN-LAST:event_txtFromDateFocusLost

    private void txtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateFocusLost
        // TODO add your handling code here:
        //        if(txtEffectTo.getText().equals("")){
        //            txtEffectTo.setText(EITLERPGLOBAL.FinToDate);
        //        }
    }//GEN-LAST:event_txtToDateFocusLost

    private void txtPartycodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartycodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartycodeActionPerformed

    private void txtToDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToDateActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        /*DoNotEvaluate=true;
         int ImportCol=DataModelDesc.getColFromVariable("EXCISE_GATEPASS_GIVEN");
         Object[] rowData=new Object[ImportCol+1];
         rowData[ImportCol]=Boolean.valueOf(false);
         DataModelDesc.addRow(rowData);
         DataModelDesc.SetUserObject(TableDesc.getRowCount()-1,     );
         TableDesc.changeSelection(TableDesc.getRowCount()-1, 1, false,false);
         UpdateSrNo();
         DoNotEvaluate=false;
         */
        /*
         Object[] rowData=new Object[1];
         DataModelDesc.addRow(rowData);
         //DataModelL.SetUserObject(TableL.getRowCount()-1,new HashMap());
         TableDesc.changeSelection(TableDesc.getRowCount()-1, 1, false,false);
         UpdateSrNo();
         //UpdateAmounts();
         */

        if (txtPartycode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Party Code Detail First.");
            return;
        } //        if(txtPartycode.getText().equals(txtOtherPartycode.getText())){
        //            txtOtherPartycode.setText("");
        //            JOptionPane.showMessageDialog(null,"Current and other Party code should not be same.");
        //            return;
        //
        //        }
        else {

            Updating = true;
            Object[] rowData = new Object[30];
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

    private void cmdEditPiecedetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditPiecedetailActionPerformed
        //DataModelDesc.TableReadOnly(false);
        //DataModelDesc.SetReadOnly(0);

        DataModelDesc.SetReadOnly(0);
        DataModelDesc.ResetReadOnly(1);
        DataModelDesc.ResetReadOnly(2);
        DataModelDesc.ResetReadOnly(3);
        DataModelDesc.ResetReadOnly(4);
        DataModelDesc.ResetReadOnly(5);
        DataModelDesc.ResetReadOnly(6);
        DataModelDesc.ResetReadOnly(7);
        //        DataModelDesc.ResetReadOnly(9);
        //        DataModelDesc.ResetReadOnly(12);
        //        DataModelDesc.ResetReadOnly(8);
        //        DataModelDesc.ResetReadOnly(16);

        //DataModelDesc.ResetReadOnly(11);
    }//GEN-LAST:event_cmdEditPiecedetailActionPerformed

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
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsTargetEntry.ModuleID, PartyID)) {
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

        if (cmbSendTo.getItemCount() <= 0) {
            GenerateFromCombo();
        }
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
        if (TableHS.getRowCount() > 0 && TableHS.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableHS.getValueAt(TableHS.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cmdEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEmailActionPerformed

    }//GEN-LAST:event_cmdEmailActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void txtPartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartycodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' ORDER BY PARTY_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtPartycode.setText(aList.ReturnVal);
                txtPartyname.setText(clsFeltparty.getParyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
                //txtPartyname.setText(aList.ReturnVal);
                //System.out.println(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                //txtPartystation.setText(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));

            }
        }
    }//GEN-LAST:event_txtPartycodeKeyPressed

    private void txtPartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartycodeFocusLost
        try {
            if (!txtPartycode.getText().equals("")) {
                String strSQL = "";
                ResultSet rsTmp;
                strSQL = "";
                //strSQL+="SELECT NAME,AD1,AD2,STATION,CHG_IND_2,TRANS_CD,INS_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE = "+txtPartycode.getText().trim()+"";
                //strSQL+="SELECT PARTY_NAME,DISPATCH_STATION,CONTACT_PERSON FROM (SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS FPM LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_INFO) AS FPEI ON FPM.PARTY_CODE=FPEI.PARTY_CODE WHERE FPM.PARTY_CODE="+txtPartycode.getText().trim()+"";
                strSQL += "SELECT PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + txtPartycode.getText().trim() + "";
                rsTmp = data.getResult(strSQL);
                rsTmp.first();
                txtPartyname.setText(rsTmp.getString("PARTY_NAME"));
                //txtPartystation.setText(rsTmp.getString("DISPATCH_STATION"));
                //txtContact.setText(rsTmp.getString("CONTACT_PERSON"));
            }
        } catch (Exception e) {
            txtPartyname.setText("");
        }
        //      GeneratePreviousDiscount();

    }//GEN-LAST:event_txtPartycodeFocusLost

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
        //      GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdTopActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
        //      GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
        //      GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
        //      GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        txtFromToYear.setEditable(true);
        Add();
        //      GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        Edit();
        //     GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
        //     GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        //       if(chkOtherparty.isSelected()) {
        //            txtOtherPartycode.setEnabled(true);
        //            ShowMessage("Please check Discount % of other party Piece No before Final Approve");
        //
        //        }
        /*else {
         txtOtherPartycode.setEnabled(false);
         txtOtherPartycode.setText("");
         //GenerateOtherpartyPreviousDiscount();
         }
         */

//        try {
//            Connection conn = data.getConn();
//            Statement stmt = conn.createStatement();
//
//            String strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND DOC_NO NOT IN ('" + txtDocNo.getText().trim() + "') AND APPROVED=0 AND CANCELLED=0 "; //AND EFFECTIVE_TO IS NULL AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'
//            System.out.println(strSQL);
//            //rsTmp=tmpStmt.executeQuery(strSQL);
//            //rsTmp.first();
//            ResultSet rsTmp = data.getResult(strSQL);
//            rsTmp.first();
//
//            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
//            {
//                JOptionPane.showMessageDialog(null, "Document Pending for Final Approval for this PARTY CODE : '" + txtPartycode.getText() + "'. Please Final Approve earlier record.");
//                Cancel();
//            } else {
//
//                p = new String[TableDesc.getRowCount()];
//                for (int i = 0; i < p.length; i++) {
//                    p[i] = (String) TableDesc.getValueAt(i, 1);
//                    System.out.println("Array List " + p[i]);
//
//                    //}
//                    //String str="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELLED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'";
//                    String str = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELLED=0 AND EFFECTIVE_FROM>'" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "'";
//                    System.out.println(str);
//                    ResultSet rsStr = data.getResult(str);
//                    rsStr.first();
//
//                    if (rsStr.getInt("COUNT") > 0) //Item is Approved
//                    {
//
////                        try{
////                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
////                            data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND PRODUCT_CODE='"+p[i]+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
////                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
////                            data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND PRODUCT_CODE='"+p[i]+"' AND APPROVED=1 AND CANCELED=0 AND (EFFECTIVE_TO IS NULL OR EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"') AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
////                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
////                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
////                        }
////                        catch(Exception a){
////                            a.printStackTrace();
////                        }
//                        int conf = JOptionPane.showConfirmDialog(null, "This sanction will cancelled all further dates records for PRODUCT CODE : '" + p[i] + "' for PARTY CODE : '" + txtPartycode.getText() + "'. DO YOU WANT TO SAVE THIS RECORD.");
//
//                        if (conf == 0) {
//                            int fconf = JOptionPane.showConfirmDialog(null, "ARE YOU DEFINITLY SURE TO SAVE THIS RECORD.");
//
//                            try {
//                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET CANCELLED=1 WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELLED=0 AND EFFECTIVE_FROM>'" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "'");
//                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "',1) WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELLED=0 AND (EFFECTIVE_TO IS NULL OR EFFECTIVE_TO>'" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "') AND EFFECTIVE_FROM<'" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "'");
//                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            } catch (Exception a) {
//                                a.printStackTrace();
//                            }
//                        } else {
//
//                        }
//                        //Cancel();
//                    } else {
//                        try {
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND PRODUCT_CODE='"+p[i]+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "',1) WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELLED=0 AND (EFFECTIVE_TO IS NULL OR EFFECTIVE_TO>'" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "') AND EFFECTIVE_FROM<'" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "'");
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                        } catch (Exception a) {
//                            a.printStackTrace();
//                        }
//                    }
//                }
//
//                Save();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Save();
        //        GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
        //      GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
        Find();
        //     GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        //ObjColumn.Close();
        ObjFeltTarget.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void txtFromToYearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromToYearFocusLost
        // TODO add your handling code here:

        if (txtFromToYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Financial Year");
            txtFromToYear.requestFocus();
            return;
        }
        else if(txtFromToYear.getText().trim().length()!=9) 
        {
            JOptionPane.showMessageDialog(null, "Please Enter Valid Financial Year");
            txtFromToYear.requestFocus();
            return;
        }
        else
        {
            String Fin_Year = txtFromToYear.getText().trim();
            
            String from_year = Fin_Year.substring(0, 4);
            String to_year = Fin_Year.substring(5, 9);
            //JOptionPane.showMessageDialog(null, "From : "+from_year+" , to : "+to_year);
            txtFromDate.setText("01/04/"+from_year);
            txtToDate.setText("31/03/"+to_year);
        }
//
//        try {
//            int ToYear = Integer.parseInt(txtFromYear.getText().trim()) + 1;
//            txtToYear.setText(Integer.toString(ToYear));
//        } catch (Exception e) {
//        }
        txtPartycode.requestFocus();
        txtPartycode.setEditable(true);
        txtFromDate.setEditable(true);
        txtToDate.setEditable(true);

    }//GEN-LAST:event_txtFromToYearFocusLost

    private void txtPartycodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartycodeFocusGained
        // TODO add your handling code here:
        if (txtFromToYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Financial Target Year.");
            txtPartycode.setText("");
            txtPartyname.setText("");
            txtFromToYear.requestFocus(true);
            return;
        }
        if (txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Financial From Date.");
            txtPartycode.setText("");
            txtPartyname.setText("");
            txtFromDate.requestFocus(true);
            return;
        }
        if (txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Financial To Date.");
            txtPartycode.setText("");
            txtPartyname.setText("");
            txtToDate.requestFocus(true);
            return;
        }
    }//GEN-LAST:event_txtPartycodeFocusGained

    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased
        // TODO add your handling code here:
        double q1 = 0,q2 = 0,q3 = 0,q4 = 0,total = 0;
        if ((TableDesc.getValueAt(TableDesc.getSelectedRow(), 3).toString()).equals("")) {
            q1 = 0;
        } else {
            q1 = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 3).toString());
            q1 = EITLERPGLOBAL.round(q1, 2);
        }
        if ((TableDesc.getValueAt(TableDesc.getSelectedRow(), 4).toString()).equals("")) {
            q2 = 0;
        } else {
            q2 = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 4).toString());
            q2 = EITLERPGLOBAL.round(q2, 2);
        }
        if ((TableDesc.getValueAt(TableDesc.getSelectedRow(), 5).toString()).equals("")) {
            q3 = 0;
        } else {
            q3 = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 5).toString());
            q3 = EITLERPGLOBAL.round(q3, 2);
        }
        if ((TableDesc.getValueAt(TableDesc.getSelectedRow(), 6).toString()).equals("")) {
            q4 = 0;
        } else {
            q4 = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 6).toString());
            q4 = EITLERPGLOBAL.round(q4, 2);
        }
        total = q1+q2+q3+q4;
        TableDesc.setValueAt(total, TableDesc.getSelectedRow(), 7);
        
        //checking already product target found or not??
        String ProductCode = TableDesc.getValueAt(TableDesc.getSelectedRow(), 1).toString();
        String MachinePosition = TableDesc.getValueAt(TableDesc.getSelectedRow(), 2).toString();
        
        String query = "SELECT A.PARTY_CODE FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER A,PRODUCTION.FELT_TARGET_ENTRY_DETAIL B WHERE  " +
                                    " B.PARTY_CODE='"+txtPartycode.getText()+"' AND " +
                                    " B.PRODUCT_CODE = '"+ProductCode+"' AND " +
                                    " A.FROM_TO_YEAR = '"+txtFromToYear.getText()+"' AND " +
                                        " A.APPROVED = 0  AND  A.CANCELLED=0  limit 1";
        // System.out.println("Check Previous Entry : "+query);
        String old_entry="";
        try{
            old_entry = data.getStringValueFromDB(query);
        }catch(Exception e) {  e.printStackTrace();   }
        
        
        boolean already_exist = false;
        if(!old_entry.equals(""))
        {
            already_exist=true;
            JOptionPane.showMessageDialog(null, "DATA ALREADY PENDING FOR PARTYCODE = "+txtPartycode.getText()+" AND YEAR : "+txtFromToYear.getText()+" AND PRODUCTCODE : "+ProductCode);
        }
        //end check
        
       if(TableDesc.getSelectedColumn()==2 && evt.getKeyCode()==KeyEvent.VK_TAB && already_exist==false)
       {
                //get Detail from PRODUCTION.FELT_PARTY_ITEM_POSITION_TARGET
                String LastQuery = "SELECT PARTY_CODE,PRODUCT_CODE,MACHINE_POSITION,"
                        + "TGT_FY_YR,TGT_FROM_DATE,TGT_TO_DATE,"
                        + "TGT_QTR_1,TGT_QTR_2,TGT_QTR_3,TGT_QTR_4,TOTAL_TGT,"
                        + "TGT_QTR_1_OLD,TGT_QTR_2_OLD,TGT_QTR_3_OLD,TGT_QTR_4_OLD,"
                        + "TGT_QTR_1_OLD_R2,TGT_QTR_2_OLD_R2,TGT_QTR_3_OLD_R2,TGT_QTR_4_OLD_R2,";        
                for(int i=3;i<=20;i++)
                {
                    if(i==20)            {
                            LastQuery = LastQuery + "TGT_QTR_1_OLD_R"+i+",TGT_QTR_2_OLD_R"+i+",TGT_QTR_3_OLD_R"+i+",TGT_QTR_4_OLD_R"+i+"";      
                    }
                    else            {
                            LastQuery = LastQuery + "TGT_QTR_1_OLD_R"+i+",TGT_QTR_2_OLD_R"+i+",TGT_QTR_3_OLD_R"+i+",TGT_QTR_4_OLD_R"+i+",";    
                    }            
                }
                LastQuery = LastQuery + " FROM PRODUCTION.FELT_PARTY_ITEM_POSITION_TARGET "
                        + " "
                        + " WHERE PARTY_CODE='"+txtPartycode.getText()+"' AND " 
                        + "PRODUCT_CODE = '"+ProductCode+"' AND " 
                        + "TGT_FY_YR = '"+txtFromToYear.getText()+"'";        
                System.out.println("LastQuery : "+LastQuery);
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                clsTargetEntryItem objTargetItem = new clsTargetEntryItem();
                try {
                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    rsData = stmt.executeQuery(LastQuery);
                    rsData.first();

        //            objTargetItem.setAttribute("PARTY_CODE", rsData.getString("PARTY_CODE"));
        //            objTargetItem.setAttribute("PRODUCT_CODE", rsData.getString("PRODUCT_CODE"));
        //            objTargetItem.setAttribute("MACHINE_POSITION", rsData.getString("MACHINE_POSITION"));
        //            objTargetItem.setAttribute("TGT_FY_YR", rsData.getString("TGT_FY_YR"));
        //            objTargetItem.setAttribute("TGT_FROM_DATE", rsData.getString("TGT_FROM_DATE"));
        //            objTargetItem.setAttribute("TGT_TO_DATE", rsData.getString("TGT_TO_DATE"));
        //            objTargetItem.setAttribute("TGT_QTR_1", rsData.getString("TGT_QTR_1"));
        //            objTargetItem.setAttribute("TGT_QTR_2", rsData.getString("TGT_QTR_2"));
        //            objTargetItem.setAttribute("TGT_QTR_3", rsData.getString("TGT_QTR_3"));
        //            objTargetItem.setAttribute("TGT_QTR_4", rsData.getString("TGT_QTR_4"));
        //            objTargetItem.setAttribute("TOTAL_TGT", rsData.getString("TOTAL_TGT"));
        //            objTargetItem.setAttribute("TGT_QTR_1_OLD", rsData.getString("TGT_QTR_1_OLD"));
        //            objTargetItem.setAttribute("TGT_QTR_2_OLD", rsData.getString("TGT_QTR_2_OLD"));
        //            objTargetItem.setAttribute("TGT_QTR_3_OLD", rsData.getString("TGT_QTR_3_OLD"));
        //            objTargetItem.setAttribute("TGT_QTR_4_OLD", rsData.getString("TGT_QTR_4_OLD"));
        //            
        //            for(int i=2;i<=20;i++)
        //            {
        //                objTargetItem.setAttribute("TGT_QTR_1_OLD_R"+i, rsData.getString("TGT_QTR_1_OLD_R"+i));
        //                objTargetItem.setAttribute("TGT_QTR_2_OLD_R"+i, rsData.getString("TGT_QTR_2_OLD_R"+i));
        //                objTargetItem.setAttribute("TGT_QTR_3_OLD_R"+i, rsData.getString("TGT_QTR_3_OLD_R"+i));
        //                objTargetItem.setAttribute("TGT_QTR_4_OLD_R"+i, rsData.getString("TGT_QTR_4_OLD_R"+i));
        //            }

                    DataModelDesc.setValueByVariable("TGT_QTR_1", "", TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_2", "", TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_3", "", TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_4", "", TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TOTAL_TGT", "", TableDesc.getSelectedRow());

                    DataModelDesc.setValueByVariable("TGT_QTR_1_OLD_R1", rsData.getString("TGT_QTR_1"), TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_2_OLD_R1", rsData.getString("TGT_QTR_2"), TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_3_OLD_R1", rsData.getString("TGT_QTR_3"), TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_4_OLD_R1", rsData.getString("TGT_QTR_4"), TableDesc.getSelectedRow());

                    DataModelDesc.setValueByVariable("TGT_QTR_1_OLD_R2", rsData.getString("TGT_QTR_1_OLD"), TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_2_OLD_R2", rsData.getString("TGT_QTR_2_OLD"), TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_3_OLD_R2", rsData.getString("TGT_QTR_3_OLD"), TableDesc.getSelectedRow());
                    DataModelDesc.setValueByVariable("TGT_QTR_4_OLD_R2", rsData.getString("TGT_QTR_4_OLD"), TableDesc.getSelectedRow());

                    for(int i=3;i<=20;i++)
                    {
                        DataModelDesc.setValueByVariable("TGT_QTR_1_OLD_R"+i, rsData.getString("TGT_QTR_1_OLD_R"+(i-1)), TableDesc.getSelectedRow());
                        DataModelDesc.setValueByVariable("TGT_QTR_2_OLD_R"+i, rsData.getString("TGT_QTR_2_OLD_R"+(i-1)), TableDesc.getSelectedRow());
                        DataModelDesc.setValueByVariable("TGT_QTR_3_OLD_R"+i, rsData.getString("TGT_QTR_3_OLD_R"+(i-1)), TableDesc.getSelectedRow());
                        DataModelDesc.setValueByVariable("TGT_QTR_4_OLD_R"+i, rsData.getString("TGT_QTR_4_OLD_R"+(i-1)), TableDesc.getSelectedRow());
                    }
                    
                //    Targetdetail[0] = rsData.getString(1);

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
       }
    }//GEN-LAST:event_TableDescKeyReleased

    private void TableDescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TableDescKeyTyped

    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TableDescKeyPressed


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
    private javax.swing.JButton cmdEmail;
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private java.awt.Label label1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUnadjNo;
    private javax.swing.JLabel lblUnadjNo1;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtFromToYear;
    private javax.swing.JTextField txtPartycode;
    private javax.swing.JTextField txtPartyname;
    private javax.swing.JTextField txtToDate;
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
        aList.ModuleID = 756;

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
            txtDocNo.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 756, FFNo, false));
            txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());
            txtFromToYear.requestFocus();

            lblTitle.setText("FELT TARGET QUARTER WISE ENTRY - " + txtDocNo.getText());
            lblTitle.setBackground(Color.GRAY);
        } else {
            JOptionPane.showMessageDialog(null, "You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
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
            lnFromID = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsTargetEntry.ModuleID, (String) ObjFeltTarget.getAttribute("DOC_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsTargetEntry.ModuleID, FromUserID, (String) ObjFeltTarget.getAttribute("DOC_NO").getObj());

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
            if (clsFeltProductionApprovalFlow.IsCreator(clsTargetEntry.ModuleID, txtDocNo.getText())) {
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

                String ProformaNo = (String) ObjFeltTarget.getAttribute("DOC_NO").getObj();

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsTargetEntry.ModuleID, ProformaNo);
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
        txtDocNo.setEnabled(pStat);
        txtDocDate.setEnabled(pStat);
        txtFromDate.setEnabled(pStat);
        txtToDate.setEnabled(pStat);
        //txtEffectTo.setEnabled(pStat);
        //txttarget.setEnabled(pStat);
        txtFromToYear.setEnabled(pStat);

        cmdAdd.setEnabled(pStat);
        cmdItemdelete.setEnabled(pStat);
        cmdEditPiecedetail.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);

        TableDesc.setEnabled(pStat);
        
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
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE DOC_NO='" + txtDocNo.getText().trim() + "'")) {
                    JOptionPane.showMessageDialog(null, "Party Code already exists!!");
                    txtPartycode.requestFocus(true);
                    return false;
                }
            }
        }

        /*if(txtPieceNo.getText().trim().equals("")) {
         JOptionPane.showMessageDialog(null,"Please enter Piece Number");
         return false;
         }
         */
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
        txtDocNo.setText("");
        txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());
        txtFromDate.setText("");
        txtToDate.setText("");
        txtFromToYear.setText("");

        txtFromRemarks.setText("");
        txtToRemarks.setText("");

        FormatGrid();
        FormatGridA();
        FormatGridHS();
        //GenerateGrid();
    }

    private void Edit() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }

        //----------------------------------//
        String lProformaNo = ObjFeltTarget.getAttribute("DOC_NO").getString();
        if (ObjFeltTarget.IsEditable(EITLERPGLOBAL.gCompanyID, lProformaNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (clsFeltProductionApprovalFlow.IsCreator(clsTargetEntry.ModuleID, lProformaNo) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8022, 80222)) {
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

        String lDocNo = (String) ObjFeltTarget.getAttribute("DOC_NO").getObj();

        if (ObjFeltTarget.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            if (ObjFeltTarget.Delete(EITLERPGLOBAL.gNewUserID)) {
                MoveLast();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while deleting. \nError is " + ObjFeltTarget.LastError);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
        }
    }

    private void Save() {
        //Form level validations
        /*if(Validate()==false) {
         return; //Validation failed
         }
         */

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
        if (txtFromToYear.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Financial Year");
            return;
        }
        if (txtFromDate.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter From Date");
            return;
        }
        //        if(txtEffectTo.getText().equals("")) {
        //            JOptionPane.showMessageDialog(null,"Please enter Effective To Date");
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

        SetData();
        if (EditMode == EITLERPGLOBAL.ADD) {

            if (ObjFeltTarget.Insert()) {
                // MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjFeltTarget.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjFeltTarget.Update()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjFeltTarget.LastError);
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
        //        chkOtherparty.setSelected(false);
        EnableToolbar();
        SetMenuForRights();
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.Production.FeltTarget.frmPartyCodeFind", true);
        frmPartyCodeFind ObjReturn = (frmPartyCodeFind) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            if (!ObjFeltTarget.Filter(ObjReturn.stringFindQuery)) {
                JOptionPane.showMessageDialog(null, "No records found.");
            }
            MoveFirst();
        }
    }

    private void MoveFirst() {
        ObjFeltTarget.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjFeltTarget.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjFeltTarget.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjFeltTarget.MoveLast();
        DisplayData();
    }

    //Didplay data on the Screen
    private void DisplayData() {
        //=========== Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (ObjFeltTarget.getAttribute("APPROVED").getInt() == 1) {

                    lblTitle.setBackground(Color.BLUE);
                    cmdEmail.setEnabled(true);
                }

                if (ObjFeltTarget.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                    cmdEmail.setEnabled(false);
                }

                if (ObjFeltTarget.getAttribute("CANCELLED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                    cmdEmail.setEnabled(false);
                }
            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = clsTargetEntry.ModuleID;

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
            lblTitle.setText("FELT TARGET QUARTER WISE ENTRY - " + (String) ObjFeltTarget.getAttribute("DOC_NO").getObj());
            txtDocNo.setText((String) ObjFeltTarget.getAttribute("DOC_NO").getObj());
            txtDocDate.setText(EITLERPGLOBAL.formatDate(ObjFeltTarget.getAttribute("DOC_DATE").getString()));
            txtFromToYear.setText((String) ObjFeltTarget.getAttribute("FROM_TO_YEAR").getObj());
            txtFromDate.setText(EITLERPGLOBAL.formatDate(ObjFeltTarget.getAttribute("FROM_DATE").getString()));
            txtToDate.setText(EITLERPGLOBAL.formatDate(ObjFeltTarget.getAttribute("TO_DATE").getString()));
            txtPartycode.setText((String) ObjFeltTarget.getAttribute("PARTY_CODE").getObj());
            txtPartyname.setText((String) ObjFeltTarget.getAttribute("PARTY_NAME").getObj());

            String doc = (String) ObjFeltTarget.getAttribute("DOC_NO").getObj();

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjFeltTarget.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//
            FormatGrid();

            //Now Generate Table
            for (int i = 1; i <= ObjFeltTarget.colTGItems.size(); i++) {

                clsTargetEntryItem ObjItem = (clsTargetEntryItem) ObjFeltTarget.colTGItems.get(Integer.toString(i));
                Object[] rowData = new Object[88];

                rowData[0] = Integer.toString(i);
                rowData[1] = (String) ObjItem.getAttribute("PRODUCT_CODE").getObj();
                rowData[2] = (String) ObjItem.getAttribute("MACHINE_POSITION").getObj();
                rowData[3] = Double.toString(ObjItem.getAttribute("TGT_Q1").getVal());
                rowData[4] = Double.toString(ObjItem.getAttribute("TGT_Q2").getVal());
                rowData[5] = Double.toString(ObjItem.getAttribute("TGT_Q3").getVal());
                rowData[6] = Double.toString(ObjItem.getAttribute("TGT_Q4").getVal());
                rowData[7] = Double.toString(ObjItem.getAttribute("TOTAL_TARGET").getVal());
                
                int j=7;
                for(int k=1;k<=20;k++)
                {
                    j++;
                    rowData[j] = Double.toString(ObjItem.getAttribute("TGT_Q1_OLD_R"+k).getVal());
                    j++;
                    rowData[j] = Double.toString(ObjItem.getAttribute("TGT_Q2_OLD_R"+k).getVal());
                    j++;
                    rowData[j] = Double.toString(ObjItem.getAttribute("TGT_Q3_OLD_R"+k).getVal());
                    j++;
                    rowData[j] = Double.toString(ObjItem.getAttribute("TGT_Q4_OLD_R"+k).getVal());

                }
                /*
                rowData[8] = Double.toString(ObjItem.getAttribute("TGT_Q1_OLD").getVal());
                rowData[9] = Double.toString(ObjItem.getAttribute("TGT_Q2_OLD").getVal());
                rowData[10] = Double.toString(ObjItem.getAttribute("TGT_Q3_OLD").getVal());
                rowData[11] = Double.toString(ObjItem.getAttribute("TGT_Q4_OLD").getVal());
                */
                
                DataModelDesc.addRow(rowData);
            }

            DoNotEvaluate = false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String docNo = ObjFeltTarget.getAttribute("DOC_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsTargetEntry.ModuleID, docNo);
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

            //Showing Audit Trial History
            FormatGridHS();
            HashMap History = clsTargetEntry.getHistoryList(EITLERPGLOBAL.gCompanyID, docNo);
            for (int i = 1; i <= History.size(); i++) {
                clsTargetEntry ObjHistory = (clsTargetEntry) History.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjHistory.getAttribute("ENTRY_DATE").getString());
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

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Display Data Error: " + e.getMessage());
        }
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjFeltTarget.setAttribute("PREFIX", SelPrefix);
        ObjFeltTarget.setAttribute("SUFFIX", SelSuffix);
        ObjFeltTarget.setAttribute("FFNO", FFNo);
        ObjFeltTarget.setAttribute("DOC_NO", txtDocNo.getText());
        ObjFeltTarget.setAttribute("DOC_DATE", EITLERPGLOBAL.formatDateDB(txtDocDate.getText()));
        ObjFeltTarget.setAttribute("FROM_TO_YEAR", txtFromToYear.getText());
        ObjFeltTarget.setAttribute("FROM_DATE", EITLERPGLOBAL.formatDateDB(txtFromDate.getText()));
        ObjFeltTarget.setAttribute("TO_DATE", EITLERPGLOBAL.formatDateDB(txtToDate.getText()));
        ObjFeltTarget.setAttribute("PARTY_CODE", txtPartycode.getText());
        ObjFeltTarget.setAttribute("PARTY_NAME", txtPartyname.getText());

        //----- Update Approval Specific Fields -----------//
        ObjFeltTarget.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjFeltTarget.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjFeltTarget.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjFeltTarget.setAttribute("REJECTED_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            ObjFeltTarget.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjFeltTarget.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjFeltTarget.setAttribute("APPROVAL_STATUS", "R");
            ObjFeltTarget.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjFeltTarget.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjFeltTarget.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjFeltTarget.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjFeltTarget.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjFeltTarget.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        //======= Set Line part ============
        ObjFeltTarget.colTGItems.clear();

        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            clsTargetEntryItem ObjItem = new clsTargetEntryItem();
            //String lItemID=(String)TableDesc.getValueAt(i, 1);

            ObjItem.setAttribute("SR_NO", i);
            ObjItem.setAttribute("PRODUCT_CODE", (String) TableDesc.getValueAt(i, 1));
            ObjItem.setAttribute("MACHINE_POSITION", (String) TableDesc.getValueAt(i, 2));

            if (TableDesc.getValueAt(i, 3).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q1", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q1", Double.parseDouble((String) TableDesc.getValueAt(i, 3)));
            }
            if (TableDesc.getValueAt(i, 4).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q2", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q2", Double.parseDouble((String) TableDesc.getValueAt(i, 4)));
            }
            if (TableDesc.getValueAt(i, 5).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q3", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q3", Double.parseDouble((String) TableDesc.getValueAt(i, 5)));
            }
            if (TableDesc.getValueAt(i, 6).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q4", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q4", Double.parseDouble((String) TableDesc.getValueAt(i, 6)));
            }
            if (TableDesc.getValueAt(i, 7).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TOTAL_TARGET", "0.0");
            } else {
                ObjItem.setAttribute("TOTAL_TARGET", Double.parseDouble(TableDesc.getValueAt(i, 7).toString()));
            }
            
            
            int j=7;
            for(int k=1;k<=20;k++)
            {
                j++;
                if (TableDesc.getValueAt(i, j).toString().equalsIgnoreCase("")) {
                    ObjItem.setAttribute("TGT_Q1_OLD_R"+k, "0.0");
                } else {
                    ObjItem.setAttribute("TGT_Q1_OLD_R"+k, Double.parseDouble((String) TableDesc.getValueAt(i, j)));
                }
                j++;
                if (TableDesc.getValueAt(i, j).toString().equalsIgnoreCase("")) {
                    ObjItem.setAttribute("TGT_Q2_OLD_R"+k, "0.0");
                } else {
                    ObjItem.setAttribute("TGT_Q2_OLD_R"+k, Double.parseDouble((String) TableDesc.getValueAt(i, j)));
                }
                j++;
                if (TableDesc.getValueAt(i, j).toString().equalsIgnoreCase("")) {
                    ObjItem.setAttribute("TGT_Q3_OLD_R"+k, "0.0");
                } else {
                    ObjItem.setAttribute("TGT_Q3_OLD_R"+k, Double.parseDouble((String) TableDesc.getValueAt(i, j)));
                }
                j++;
                if (TableDesc.getValueAt(i, j).toString().equalsIgnoreCase("")) {
                    ObjItem.setAttribute("TGT_Q4_OLD_R"+k, "0.0");
                } else {
                    ObjItem.setAttribute("TGT_Q4_OLD_R"+k, Double.parseDouble((String) TableDesc.getValueAt(i, j)));
                }
            }
            
            /*
            if (TableDesc.getValueAt(i, 8).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q1_OLD_R1", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q1_OLD_R1", Double.parseDouble((String) TableDesc.getValueAt(i, 8)));
            }
            if (TableDesc.getValueAt(i, 9).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q2_OLD_R1", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q2_OLD_R1", Double.parseDouble((String) TableDesc.getValueAt(i, 9)));
            }
            if (TableDesc.getValueAt(i, 10).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q3_OLD_R1", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q3_OLD_R1", Double.parseDouble((String) TableDesc.getValueAt(i, 10)));
            }
            if (TableDesc.getValueAt(i, 11).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q4_OLD_R1", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q4_OLD_R1", Double.parseDouble((String) TableDesc.getValueAt(i, 11)));
            }
            
            
            if (TableDesc.getValueAt(i, 12).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q1_OLD_R2", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q1_OLD_R2", Double.parseDouble((String) TableDesc.getValueAt(i, 12)));
            }
            if (TableDesc.getValueAt(i, 13).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q2_OLD_R2", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q2_OLD_R2", Double.parseDouble((String) TableDesc.getValueAt(i, 13)));
            }
            if (TableDesc.getValueAt(i, 14).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q3_OLD_R2", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q3_OLD_R2", Double.parseDouble((String) TableDesc.getValueAt(i, 14)));
            }
            if (TableDesc.getValueAt(i, 15).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q4_OLD_R2", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q4_OLD_R2", Double.parseDouble((String) TableDesc.getValueAt(i, 15)));
            }
            
            
            if (TableDesc.getValueAt(i, 16).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q1_OLD_R3", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q1_OLD_R3", Double.parseDouble((String) TableDesc.getValueAt(i, 16)));
            }
            if (TableDesc.getValueAt(i, 17).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q2_OLD_R3", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q2_OLD_R3", Double.parseDouble((String) TableDesc.getValueAt(i, 17)));
            }
            if (TableDesc.getValueAt(i, 18).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q3_OLD_R3", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q3_OLD_R3", Double.parseDouble((String) TableDesc.getValueAt(i, 18)));
            }
            if (TableDesc.getValueAt(i, 19).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q4_OLD_R3", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q4_OLD_R3", Double.parseDouble((String) TableDesc.getValueAt(i, 19)));
            }
            
            
            if (TableDesc.getValueAt(i, 20).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q1_OLD_R4", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q1_OLD_R4", Double.parseDouble((String) TableDesc.getValueAt(i, 20)));
            }
            if (TableDesc.getValueAt(i, 21).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q2_OLD_R4", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q2_OLD_R4", Double.parseDouble((String) TableDesc.getValueAt(i, 21)));
            }
            if (TableDesc.getValueAt(i, 22).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q3_OLD_R4", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q3_OLD_R4", Double.parseDouble((String) TableDesc.getValueAt(i, 22)));
            }
            if (TableDesc.getValueAt(i, 23).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q4_OLD_R4", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q4_OLD_R4", Double.parseDouble((String) TableDesc.getValueAt(i, 23)));
            }
            
            if (TableDesc.getValueAt(i, 24).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q1_OLD_R5", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q1_OLD_R5", Double.parseDouble((String) TableDesc.getValueAt(i, 24)));
            }
            if (TableDesc.getValueAt(i, 25).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q2_OLD_R5", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q2_OLD_R5", Double.parseDouble((String) TableDesc.getValueAt(i, 25)));
            }
            if (TableDesc.getValueAt(i, 26).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q3_OLD_R5", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q3_OLD_R5", Double.parseDouble((String) TableDesc.getValueAt(i, 26)));
            }
            if (TableDesc.getValueAt(i, 27).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q4_OLD_R5", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q4_OLD_R5", Double.parseDouble((String) TableDesc.getValueAt(i, 27)));
            }
            
            
            if (TableDesc.getValueAt(i, 28).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q1_OLD_R6", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q1_OLD_R6", Double.parseDouble((String) TableDesc.getValueAt(i, 28)));
            }
            if (TableDesc.getValueAt(i, 29).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q2_OLD_R6", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q2_OLD_R6", Double.parseDouble((String) TableDesc.getValueAt(i, 29)));
            }
            if (TableDesc.getValueAt(i, 30).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q3_OLD_R6", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q3_OLD_R6", Double.parseDouble((String) TableDesc.getValueAt(i, 30)));
            }
            if (TableDesc.getValueAt(i, 31).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("TGT_Q4_OLD_R6", "0.0");
            } else {
                ObjItem.setAttribute("TGT_Q4_OLD_R6", Double.parseDouble((String) TableDesc.getValueAt(i, 31)));
            }
            */
            
            
            ObjFeltTarget.colTGItems.put(Integer.toString(ObjFeltTarget.colTGItems.size() + 1), ObjItem);
            //}
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8022, 80221)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8022, 80223)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8022, 80224)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(true);
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
        //=============== Header Fields Setup Complete =================//

        //=============== Setting Table Fields ==================//
        DataModelDesc.ClearAllReadOnly();
        for (int i = 0; i < TableDesc.getColumnCount(); i++) {
            FieldName = DataModelDesc.getVariable(i);

            if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
                //Do Nothing
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

        TableHS.setAutoResizeMode(TableHS.AUTO_RESIZE_OFF);
    }

    public void FindWaiting() {
        //ObjFeltTarget.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsDiscRateMaster.ModuleID+")");
        ObjFeltTarget.Filter(" WHERE DOC_NO IN (SELECT PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.DOC_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=756)");
        ObjFeltTarget.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pdocNo, String pPieceNo) {
        System.out.println(pdocNo);
        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjFeltTarget.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjFeltTarget.Filter(" WHERE DOC_NO='" + pdocNo + "'");
        ObjFeltTarget.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pdocNo) {
        System.out.println(pdocNo);
        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjFeltTarget.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjFeltTarget.Filter(" WHERE DOC_NO='" + pdocNo + "'");
        ObjFeltTarget.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        String PartyCode = txtDocNo.getText();

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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsTargetEntry.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsTargetEntry.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String docNo = (String) ObjFeltTarget.getAttribute("DOC_NO").getObj();
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsTargetEntry.ModuleID, docNo);
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
            DataModelDesc.addColumn("Product Code"); //1
            DataModelDesc.addColumn("Machine Position");//2
            DataModelDesc.addColumn("TGT Q1");//3
            DataModelDesc.addColumn("TGT Q2"); //4
            DataModelDesc.addColumn("TGT Q3");//5
            DataModelDesc.addColumn("TGT Q4"); //6
            DataModelDesc.addColumn("Total Target"); //7
            
            DataModelDesc.addColumn("TGT Q1 OLD R1");//8
            DataModelDesc.addColumn("TGT Q2 OLD R1"); //9
            DataModelDesc.addColumn("TGT Q3 OLD R1");//10
            DataModelDesc.addColumn("TGT Q4 OLD R1"); //11
            
            DataModelDesc.addColumn("TGT Q1 OLD R2");//12
            DataModelDesc.addColumn("TGT Q2 OLD R2"); //13
            DataModelDesc.addColumn("TGT Q3 OLD R2");//14
            DataModelDesc.addColumn("TGT Q4 OLD R2"); //15
            
            DataModelDesc.addColumn("TGT Q1 OLD R3");//16
            DataModelDesc.addColumn("TGT Q2 OLD R3"); //17
            DataModelDesc.addColumn("TGT Q3 OLD R3");//18
            DataModelDesc.addColumn("TGT Q4 OLD R3"); //19
            
            DataModelDesc.addColumn("TGT Q1 OLD R4");//20
            DataModelDesc.addColumn("TGT Q2 OLD R4");//21
            DataModelDesc.addColumn("TGT Q3 OLD R4");//22
            DataModelDesc.addColumn("TGT Q4 OLD R4");//23
            
            DataModelDesc.addColumn("TGT Q1 OLD R5");//24
            DataModelDesc.addColumn("TGT Q2 OLD R5");//25
            DataModelDesc.addColumn("TGT Q3 OLD R5");//26
            DataModelDesc.addColumn("TGT Q4 OLD R5");//27
            
            DataModelDesc.addColumn("TGT Q1 OLD R6");//28
            DataModelDesc.addColumn("TGT Q2 OLD R6");//29
            DataModelDesc.addColumn("TGT Q3 OLD R6");//30
            DataModelDesc.addColumn("TGT Q4 OLD R6");//31
            
            DataModelDesc.addColumn("TGT Q1 OLD R7");//32
            DataModelDesc.addColumn("TGT Q2 OLD R7");//33
            DataModelDesc.addColumn("TGT Q3 OLD R7");//34
            DataModelDesc.addColumn("TGT Q4 OLD R7");//35
            
            DataModelDesc.addColumn("TGT Q1 OLD R8");//36
            DataModelDesc.addColumn("TGT Q2 OLD R8");//37
            DataModelDesc.addColumn("TGT Q3 OLD R8");//38
            DataModelDesc.addColumn("TGT Q4 OLD R8");//39
            
            DataModelDesc.addColumn("TGT Q1 OLD R9");//40
            DataModelDesc.addColumn("TGT Q2 OLD R9");//41
            DataModelDesc.addColumn("TGT Q3 OLD R9");//42
            DataModelDesc.addColumn("TGT Q4 OLD R9");//43

            DataModelDesc.addColumn("TGT Q1 OLD R10");//44
            DataModelDesc.addColumn("TGT Q2 OLD R10");//45
            DataModelDesc.addColumn("TGT Q3 OLD R10");//46
            DataModelDesc.addColumn("TGT Q4 OLD R10");//47
            
            DataModelDesc.addColumn("TGT Q1 OLD R11");//48
            DataModelDesc.addColumn("TGT Q2 OLD R11");//49
            DataModelDesc.addColumn("TGT Q3 OLD R11");//50
            DataModelDesc.addColumn("TGT Q4 OLD R11");//51
            
            DataModelDesc.addColumn("TGT Q1 OLD R12");//52
            DataModelDesc.addColumn("TGT Q2 OLD R12");//53
            DataModelDesc.addColumn("TGT Q3 OLD R12");//54
            DataModelDesc.addColumn("TGT Q4 OLD R12");//55
            
            DataModelDesc.addColumn("TGT Q1 OLD R13");//56
            DataModelDesc.addColumn("TGT Q2 OLD R13");//57
            DataModelDesc.addColumn("TGT Q3 OLD R13");//58
            DataModelDesc.addColumn("TGT Q4 OLD R13");//59

            DataModelDesc.addColumn("TGT Q1 OLD R14");//60
            DataModelDesc.addColumn("TGT Q2 OLD R14");//61
            DataModelDesc.addColumn("TGT Q3 OLD R14");//62
            DataModelDesc.addColumn("TGT Q4 OLD R14");//63
            
            DataModelDesc.addColumn("TGT Q1 OLD R15");//64
            DataModelDesc.addColumn("TGT Q2 OLD R15");//65
            DataModelDesc.addColumn("TGT Q3 OLD R15");//66
            DataModelDesc.addColumn("TGT Q4 OLD R15");//67
            
            DataModelDesc.addColumn("TGT Q1 OLD R16");//68
            DataModelDesc.addColumn("TGT Q2 OLD R16");//69
            DataModelDesc.addColumn("TGT Q3 OLD R16");//70
            DataModelDesc.addColumn("TGT Q4 OLD R16");//71
            
            DataModelDesc.addColumn("TGT Q1 OLD R17");//72
            DataModelDesc.addColumn("TGT Q2 OLD R17");//73
            DataModelDesc.addColumn("TGT Q3 OLD R17");//74
            DataModelDesc.addColumn("TGT Q4 OLD R17");//75
            
            DataModelDesc.addColumn("TGT Q1 OLD R18");//76
            DataModelDesc.addColumn("TGT Q2 OLD R18");//77
            DataModelDesc.addColumn("TGT Q3 OLD R18");//78
            DataModelDesc.addColumn("TGT Q4 OLD R18");//79
            
            DataModelDesc.addColumn("TGT Q1 OLD R19");//80
            DataModelDesc.addColumn("TGT Q2 OLD R19");//81
            DataModelDesc.addColumn("TGT Q3 OLD R19");//82
            DataModelDesc.addColumn("TGT Q4 OLD R19");//83
            
            DataModelDesc.addColumn("TGT Q1 OLD R20");//84
            DataModelDesc.addColumn("TGT Q2 OLD R20");//85
            DataModelDesc.addColumn("TGT Q3 OLD R20");//86
            DataModelDesc.addColumn("TGT Q4 OLD R20");//87
            
            //DataModelDesc.TableReadOnly(true);
            DataModelDesc.SetVariable(0, "SR_NO");  //0
            DataModelDesc.SetVariable(1, "PRODUCT_CODE"); //1
            DataModelDesc.SetVariable(2, "MACHINE_POSITION"); //2
            DataModelDesc.SetVariable(3, "TGT_QTR_1"); //3
            DataModelDesc.SetVariable(4, "TGT_QTR_2"); //4
            DataModelDesc.SetVariable(5, "TGT_QTR_3"); //5
            DataModelDesc.SetVariable(6, "TGT_QTR_4"); //6
            DataModelDesc.SetVariable(7, "TOTAL_TGT"); //7
            
            int j=7;
            for(int i=1;i<=20;i++)
            {
                    j++;
                    DataModelDesc.SetVariable(j, "TGT_QTR_1_OLD_R"+i); //
                    j++;
                    DataModelDesc.SetVariable(j, "TGT_QTR_2_OLD_R"+i); //
                    j++;
                    DataModelDesc.SetVariable(j, "TGT_QTR_3_OLD_R"+i); //
                    j++;
                    DataModelDesc.SetVariable(j, "TGT_QTR_4_OLD_R"+i); //
            }
            
            /*
            DataModelDesc.SetVariable(8, "TGT_QTR_1_OLD_R1"); //8
            DataModelDesc.SetVariable(9, "TGT_QTR_2_OLD_R1"); //9
            DataModelDesc.SetVariable(10, "TGT_QTR_3_OLD_R1"); //10
            DataModelDesc.SetVariable(11, "TGT_QTR_4_OLD_R1"); //11

            DataModelDesc.SetVariable(12, "TGT_QTR_1_OLD_R2"); //12
            DataModelDesc.SetVariable(13, "TGT_QTR_2_OLD_R2"); //13
            DataModelDesc.SetVariable(14, "TGT_QTR_3_OLD_R2"); //14
            DataModelDesc.SetVariable(15, "TGT_QTR_4_OLD_R2"); //15
            
            DataModelDesc.SetVariable(16, "TGT_QTR_1_OLD_R3"); //16
            DataModelDesc.SetVariable(17, "TGT_QTR_2_OLD_R3"); //17
            DataModelDesc.SetVariable(18, "TGT_QTR_3_OLD_R3"); //18
            DataModelDesc.SetVariable(19, "TGT_QTR_4_OLD_R3"); //19
            
            DataModelDesc.SetVariable(20, "TGT_QTR_1_OLD_R4"); //20
            DataModelDesc.SetVariable(21, "TGT_QTR_2_OLD_R4"); //21
            DataModelDesc.SetVariable(22, "TGT_QTR_3_OLD_R4"); //22
            DataModelDesc.SetVariable(23, "TGT_QTR_4_OLD_R4"); //23
            
            DataModelDesc.SetVariable(24, "TGT_QTR_1_OLD_R5"); //24
            DataModelDesc.SetVariable(25, "TGT_QTR_2_OLD_R5"); //25
            DataModelDesc.SetVariable(26, "TGT_QTR_3_OLD_R5"); //26
            DataModelDesc.SetVariable(27, "TGT_QTR_4_OLD_R5"); //27
            
            DataModelDesc.SetVariable(28, "TGT_QTR_1_OLD_R6"); //28
            DataModelDesc.SetVariable(29, "TGT_QTR_2_OLD_R6"); //29
            DataModelDesc.SetVariable(30, "TGT_QTR_3_OLD_R6"); //30
            DataModelDesc.SetVariable(31, "TGT_QTR_4_OLD_R6"); //31
            
            DataModelDesc.SetVariable(32, "TGT_QTR_1_OLD_R7"); //32
            DataModelDesc.SetVariable(33, "TGT_QTR_2_OLD_R7"); //33
            DataModelDesc.SetVariable(34, "TGT_QTR_3_OLD_R7"); //34
            DataModelDesc.SetVariable(35, "TGT_QTR_4_OLD_R7"); //35

            DataModelDesc.SetVariable(36, "TGT_QTR_1_OLD_R8"); //36
            DataModelDesc.SetVariable(37, "TGT_QTR_2_OLD_R8"); //37
            DataModelDesc.SetVariable(38, "TGT_QTR_3_OLD_R8"); //38
            DataModelDesc.SetVariable(39, "TGT_QTR_4_OLD_R8"); //39
            
            DataModelDesc.SetVariable(40, "TGT_QTR_1_OLD_R9"); //40
            DataModelDesc.SetVariable(41, "TGT_QTR_2_OLD_R9"); //41
            DataModelDesc.SetVariable(42, "TGT_QTR_3_OLD_R9"); //42
            DataModelDesc.SetVariable(43, "TGT_QTR_4_OLD_R9"); //43
            
            DataModelDesc.SetVariable(44, "TGT_QTR_1_OLD_R6"); //50
            DataModelDesc.SetVariable(45, "TGT_QTR_2_OLD_R6"); //49
            DataModelDesc.SetVariable(46, "TGT_QTR_3_OLD_R6"); //48
            DataModelDesc.SetVariable(47, "TGT_QTR_4_OLD_R6"); //47
            
            */
            
            
            //DataModelDesc.TableReadOnly(false);
            DataModelDesc.SetReadOnly(0);
            //DataModelDesc.SetReadOnly(1);
            //DataModelDesc.SetReadOnly(2);
            //DataModelDesc.SetReadOnly(3);
            //DataModelDesc.SetReadOnly(4);
            //DataModelDesc.SetReadOnly(5);
            //DataModelDesc.SetReadOnly(6);
            DataModelDesc.SetReadOnly(7);
            DataModelDesc.SetReadOnly(8);
            DataModelDesc.SetReadOnly(9);
            DataModelDesc.SetReadOnly(10);
            DataModelDesc.SetReadOnly(11);

            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            TableDesc.getColumnModel().getColumn(16).setPreferredWidth(100);

            //            //------- Install Table List Selection Listener ------//
            //            TableDesc.getColumnModel().getSelectionModel().addListSelectionListener(
            //            new ListSelectionListener() {
            //                public void valueChanged(ListSelectionEvent e) {
            //                    int last=TableDesc.getSelectedColumn();
            //                    String strVar=DataModelDesc.getVariable(last);
            //
            //                    //=============== Cell Editing Routine =======================//
            //                    try {
            //                        cellLastValue=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),TableDesc.getSelectedColumn());
            //
            //                        TableDesc.editCellAt(TableDesc.getSelectedRow(),TableDesc.getSelectedColumn());
            //                        if(TableDesc.getEditorComponent() instanceof JTextComponent) {
            //                            ((JTextComponent)TableDesc.getEditorComponent()).selectAll();
            //                        }
            //                    }
            //                    catch(Exception cell){}
            //                    //============= Cell Editing Routine Ended =================//
            //
            //                    ShowMessage("Ready");
            //
            //                    if(last==1) {
            //                        ShowMessage("Enter item id. Press F1 for the list of items");
            //                    }
            //                }
            //            }
            //            );
            //            //===================================================//
            //
            //
            //            //----- Install Table Model Event Listener -------//
            //            TableDesc.getModel().addTableModelListener(new TableModelListener() {
            //                public void tableChanged(TableModelEvent e) {
            //                    if (e.getType() == TableModelEvent.UPDATE) {
            //                        int col = e.getColumn();
            //
            //                        //=========== Cell Update Prevention Check ===========//
            //                        String curValue=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), e.getColumn());
            //                        if(curValue.equals(cellLastValue)) {
            //                            return;
            //                        }
            //                        //====================================================//
            //
            //                        if(DoNotEvaluate) {
            //                            return;
            //                        }
            //                        if(col==1){
            //                            try {
            //
            //                                String lItemID=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),1);
            //                                String lItemName=clsDiscRateMaster.getItemName(txtPartycode.getText(),lItemID);
            //                                TableDesc.setValueAt(lItemName, TableDesc.getSelectedRow(), 2);
            //
            //                                /*int lItemPosition=clsDiscRateMaster.getItemPosition(txtPartycode.getText(), lItemID);
            //                                Table.setValueAt(Integer.toString(lItemUnit),Table.getSelectedRow(),6);
            //                                String lUnitName=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", lItemUnit);
            //                                Table.setValueAt(lUnitName,Table.getSelectedRow(),7);
            //                                 */
            //                                /*
            //                                String lItemPosition=clsDiscRateMaster.getItemPosition(txtPartycode.getText(),lItemID);
            //                                TableDesc.setValueAt(lItemPosition, TableDesc.getSelectedRow(), 3);
            //                                String lItemLength=clsDiscRateMaster.getItemLength(txtPartycode.getText(),lItemID);
            //                                TableDesc.setValueAt(lItemLength, TableDesc.getSelectedRow(), 4);
            //                               double lItemWidth=clsDiscRateMaster.getItemWidth(txtPartycode.getText(),lItemID);
            //                                TableDesc.setValueAt(Double.toString(lItemWidth), TableDesc.getSelectedRow(), 5);
            //                                int lItemGsq=clsDiscRateMaster.getItemGsq(txtPartycode.getText(),lItemID);
            //                                TableDesc.setValueAt(Integer.toString(lItemGsq), TableDesc.getSelectedRow(), 6);
            //                                String lItemStyle=clsDiscRateMaster.getItemStyle(txtPartycode.getText(),lItemID);
            //                                TableDesc.setValueAt(lItemStyle, TableDesc.getSelectedRow(), 7);
            //                                 */
            //
            //                                String []Piecedetail=clsDiscRateMaster.getPiecedetail(txtPartycode.getText(),lItemID);
            //                                TableDesc.setValueAt(Piecedetail[27], TableDesc.getSelectedRow(), 3);
            //                                TableDesc.setValueAt(Piecedetail[10], TableDesc.getSelectedRow(), 4);
            //                                TableDesc.setValueAt(Piecedetail[11], TableDesc.getSelectedRow(), 5);
            //                                TableDesc.setValueAt(Piecedetail[12], TableDesc.getSelectedRow(), 6);
            //                                TableDesc.setValueAt(Piecedetail[9],TableDesc.getSelectedRow(), 7);
            //                                TableDesc.setValueAt(Piecedetail[13],TableDesc.getSelectedRow(),9);
            //                                TableDesc.setValueAt(Piecedetail[14], TableDesc.getSelectedRow(),10);
            //                                TableDesc.setValueAt(Piecedetail[15], TableDesc.getSelectedRow(),11);
            //                                TableDesc.setValueAt(Piecedetail[16],TableDesc.getSelectedRow(),12);
            //                                TableDesc.setValueAt(Piecedetail[17],TableDesc.getSelectedRow(),13);
            //                                TableDesc.setValueAt(Piecedetail[18],TableDesc.getSelectedRow(),14);
            //                                TableDesc.setValueAt(Piecedetail[19], TableDesc.getSelectedRow(),15);
            //                                TableDesc.setValueAt(Piecedetail[34], TableDesc.getSelectedRow(),16);
            //                                TableDesc.setValueAt(Piecedetail[20], TableDesc.getSelectedRow(),17);
            //                                TableDesc.setValueAt(Piecedetail[21],TableDesc.getSelectedRow(),18);
            //                                TableDesc.setValueAt(Piecedetail[22],TableDesc.getSelectedRow(),19);
            //                                TableDesc.setValueAt(Piecedetail[23], TableDesc.getSelectedRow(),20);
            //                                TableDesc.setValueAt(Piecedetail[24], TableDesc.getSelectedRow(),21);
            //                                TableDesc.setValueAt(Piecedetail[25],TableDesc.getSelectedRow(),22);
            //                                TableDesc.setValueAt(Piecedetail[26],TableDesc.getSelectedRow(),23);
            //                                TableDesc.setValueAt(Piecedetail[28], TableDesc.getSelectedRow(),24);
            //                                TableDesc.setValueAt(Piecedetail[29],TableDesc.getSelectedRow(),25);
            //                                TableDesc.setValueAt(Piecedetail[30],TableDesc.getSelectedRow(),26);
            //                                TableDesc.setValueAt(Piecedetail[7],TableDesc.getSelectedRow(),27);
            //
            //                                TableDesc.setValueAt(Piecedetail[31],TableDesc.getSelectedRow(),28);
            //                                TableDesc.setValueAt(Piecedetail[32],TableDesc.getSelectedRow(),29);
            //                                boolean a=false;
            //
            //                                TableDesc.setValueAt(Boolean.valueOf(Piecedetail[33]),TableDesc.getSelectedRow(),8);
            //
            //                                /*TableDesc.setValueAt(Piecedetail[31],TableDesc.getSelectedRow(),25);
            //                                TableDesc.setValueAt(Piecedetail[32],TableDesc.getSelectedRow(),26);
            //                                TableDesc.setValueAt(Piecedetail[33], TableDesc.getSelectedRow(),27);
            //                                TableDesc.setValueAt(Piecedetail[34],TableDesc.getSelectedRow(),28);
            //                                TableDesc.setValueAt(Piecedetail[35],TableDesc.getSelectedRow(),29);
            //                                 */
            //
            //                                //                               if(!txtOtherPartycode.getText().equals("")){
            //                                //                                   String lOtherItemName=clsDiscRateMaster.getItemName(txtOtherPartycode.getText(),lItemID);
            //                                //                                TableDesc.setValueAt(lOtherItemName, TableDesc.getSelectedRow(),2);
            //                                //
            //                                //                                String []OtherPiecedetail=clsDiscRateMaster.getOtherPiecedetail(txtOtherPartycode.getText(),lItemID);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[27], TableDesc.getSelectedRow(), 3);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[10], TableDesc.getSelectedRow(), 4);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[11], TableDesc.getSelectedRow(), 5);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[12], TableDesc.getSelectedRow(), 6);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[9],TableDesc.getSelectedRow(), 7);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[13],TableDesc.getSelectedRow(),9);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[14], TableDesc.getSelectedRow(),10);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[15], TableDesc.getSelectedRow(),11);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[16],TableDesc.getSelectedRow(),12);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[17],TableDesc.getSelectedRow(),13);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[18],TableDesc.getSelectedRow(),14);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[19], TableDesc.getSelectedRow(),15);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[34], TableDesc.getSelectedRow(),16);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[20], TableDesc.getSelectedRow(),17);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[21],TableDesc.getSelectedRow(),18);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[22],TableDesc.getSelectedRow(),19);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[23], TableDesc.getSelectedRow(),20);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[24], TableDesc.getSelectedRow(),21);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[25],TableDesc.getSelectedRow(),22);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[26],TableDesc.getSelectedRow(),23);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[28], TableDesc.getSelectedRow(),24);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[29],TableDesc.getSelectedRow(),25);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[30],TableDesc.getSelectedRow(),26);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[7],TableDesc.getSelectedRow(),27);
            //                                //
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[31],TableDesc.getSelectedRow(),28);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[32],TableDesc.getSelectedRow(),29);
            //                                //
            //                                //                                //TableDesc.setValueAt(String.valueOf(a),TableDesc.getSelectedRow(),8);
            //                                //                                TableDesc.setValueAt(Boolean.valueOf(OtherPiecedetail[33]),TableDesc.getSelectedRow(),8);
            //                                //
            //                                //
            //                                //                                /*TableDesc.setValueAt(OtherPiecedetail[31],TableDesc.getSelectedRow(),25);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[32],TableDesc.getSelectedRow(),26);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[33], TableDesc.getSelectedRow(),27);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[34],TableDesc.getSelectedRow(),28);
            //                                //                                TableDesc.setValueAt(OtherPiecedetail[35],TableDesc.getSelectedRow(),29);
            //                                //                                 */
            //                                //                                //DisplayIndicators();
            //                                //                               }
            //                            }
            //                            catch(Exception ex){
            //                            }
            //                        }
            //                    }
            //                }
            //            });
            //            int ImportCol=DataModelDesc.getColFromVariable("Calculate_Weight");
            //            Renderer.setCustomComponent(ImportCol,"CheckBox");
            //            JCheckBox aCheckBox=new JCheckBox();
            //            aCheckBox.setBackground(Color.WHITE);
            //            aCheckBox.setVisible(true);
            //            aCheckBox.setEnabled(true);
            //            aCheckBox.setSelected(false);
            //            TableDesc.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            //            TableDesc.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
            //
        } catch (Exception e) {

        }
        Updating = false;
        //Table formatting completed

        TableDesc.getColumnModel().getColumn(0).setMinWidth(30);
        
        //TableDesc.getColumnModel().getColumn(0).setMaxWidth(30);
        TableDesc.getColumnModel().getColumn(1).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(1).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(2).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(2).setMaxWidth(80);
        TableDesc.getColumnModel().getColumn(3).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(3).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(4).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(4).setMaxWidth(80);
        TableDesc.getColumnModel().getColumn(5).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(5).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(6).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(6).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(7).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(7).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(8).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(8).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(9).setMinWidth(100);
        TableDesc.getColumnModel().getColumn(10).setMinWidth(100);
        TableDesc.getColumnModel().getColumn(11).setMinWidth(100);

        for(int i=12;i<=87;i++)
        {
            TableDesc.getColumnModel().getColumn(i).setMinWidth(120);
        }
    }

    private void PreviewReport() {
//        String PartyCode = txtPartycode.getText();
//
//        Connection Conn = null;
//        Statement st = null;
//        ResultSet rs = null;
//        try {
//            Conn = data.getConn();
//            st = Conn.createStatement();
//
//            //rs=data.getResult("SELECT PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD WHERE PARTY_CODE='" + PartyCode + "'");
//            rs = data.getResult("SELECT PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE PARTY_CODE='" + PartyCode + "' AND APPROVED=1 AND CANCELED=0");
//            rs.first();
//
//            if (rs.getRow() > 0) {
//
//                HashMap parameterMap = new HashMap();
//
//                ReportRegister rpt = new ReportRegister(parameterMap, Conn);
//                //String sql = "select d.bill_no,d.item_type,d.quality_cd,d.retail_rate,sum(d.sale_qty) as sale_qty,sum(d.item_amount) as item_amount,sum(d.item_disc_amount) AS discount,sum(d.item_amount-d.item_disc_amount) as item_net_amount,d.bill_date,h.bill_discount_amount,h.net_payble,case when (d.BILL_TYPE IN ('SPECIAL','EMPLOYEE','SHARE HOLDER')) then sum(d.item_type_disc_amount) else 0.00 end as special,h.cash_payment,case when (h.CARD_TYPE IN('VISA','MASTER')) then h.card_payment else 0.00 end as card_pay,case when (h.CARD_TYPE='CHEQUE') then h.card_payment else 0.00 end as cheque_pay from BILL_DETAIL d,BILL_HEADER h where d.bill_no=h.bill_no and d.bill_date=h.bill_date and h.bill_status is NULL group by d.quality_cd,d.bill_no having d.bill_date = '" + pdt + "' order by d.bill_date,d.bill_no*1";
//                //String sql = "SELECT H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,H.EFFECTIVE_FROM,H.EFFECTIVE_TO,D.PRODUCT_CODE,D.PIECE_NO,D.MACHINE_NO,D.MACHINE_POSITION,D.TURN_OVER_TARGET,D.DISC_PER,D.YRED_DISC_PER,D.SEAM_VALUE,D.YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD H,PRODUCTION.FELT_RATE_DISC_MASTER D WHERE H.DOC_NO=D.DOC_NO AND H.PARTY_CODE='" + PartyCode + "'";
//                String sql = "SELECT H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.PRODUCT_CODE,D.PIECE_NO,D.MACHINE_NO,D.MACHINE_POSITION,D.TURN_OVER_TARGET,D.DISC_PER,D.YRED_DISC_PER,D.SEAM_VALUE,D.YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.PARTY_CODE='" + PartyCode + "' AND D.APPROVED=1 AND D.CANCELED=0 ORDER BY D.PARTY_CODE,D.EFFECTIVE_FROM,D.PRODUCT_CODE";
//                //rpt.setReportName("/report/billregister.jrxml", 1, sql); //productlist is the name of my jasper file.
//                rpt.setReportName("/EITLERP/Production/FeltDiscRateMaster/PartywiseDiscSanc.jrxml", 1, sql); //productlist is the name of my jasper file.
//                rpt.callReport();
//            }
//
//            if (rs.getRow() == 0) {
//                return;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                st.close();
//                Conn.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    /*
     private void PreviewReport() {
     HashMap Params=new HashMap();
     
     try {
     URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptProforma.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&DocNo="+txtdocNo.getText()+"&ProformaDate="+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText()));
     EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
     }
     catch(Exception e) {
     JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
     }
     }
     */
    //    private void GeneratePreviousDiscount(){
    //        try{
    //            // FormatGridDiscount();  //clear existing content of table
    //            String strPartycode=txtPartycode.getText().toString();
    //            ResultSet rsTmp,rsBuyer,rsIndent,rsRIA;
    //            String strSQL= "";
    //            //strSQL+="SELECT DISTINCT DISC_PER FROM PRODUCTION.FELT_DISCOUNT_MEMO ";
    //            //strSQL+="SELECT DISTINCT PRODUCT_CD,DISC_PER FROM PRODUCTION.FELT_DISCOUNT_MEMO ";
    //            strSQL+="SELECT PRODUCT_CD,DISC_PER,MAX(MEMO_DATE) AS CURRENT_MEMO_DATE,COUNT(PIECE_NO) AS COUNT_PCS FROM PRODUCTION.FELT_DISCOUNT_MEMO WHERE ";
    //            if(!txtPartycode.getText().equals("")){
    //                //strSQL+="WHERE PARTY_CODE="+strPartycode+" ORDER BY DISC_PER DESC";
    //                //strSQL+="DISC_PER!=0 AND PARTY_CODE="+strPartycode+" AND MEMO_DATE>'2012-01-01'";
    //                //strSQL+="WHERE DISC_PER!=0 AND MEMO_DATE>'1900-01-01' AND PARTY_CODE='"+strPartycode+"';
    //                strSQL+=" PARTY_CODE="+strPartycode+" AND ";
    //            }
    //            strSQL+="DISC_PER!=0 AND MEMO_DATE>'1900-01-01' GROUP BY PRODUCT_CD,DISC_PER ORDER BY DISC_PER DESC";
    //            rsTmp=data.getResult(strSQL);
    //            //rsTmp.first();
    //            if(rsTmp.getRow()>0) {
    //                int cnt=0;
    //                while(!rsTmp.isAfterLast()) {
    //                    cnt++;
    //                    Object[] rowData=new Object[5];
    //                    rowData[0]=Integer.toString(cnt);
    //                    rowData[1]=rsTmp.getString("PRODUCT_CD");
    //                    rowData[2]=rsTmp.getString("DISC_PER");
    //                    rowData[3]=EITLERPGLOBAL.formatDate(rsTmp.getString("CURRENT_MEMO_DATE"));
    //                    rowData[4]=rsTmp.getString("COUNT_PCS");
    //                    DataModelDiscount.addRow(rowData);
    //                    //   System.out.println("rsTmp.getString('PIECE_NO')");
    //                    rsTmp.next();
    //                }
    //
    //            }
    //        }
    //
    //        catch(Exception e){
    //            e.printStackTrace();
    //            JOptionPane.showMessageDialog(null,e.getMessage());
    //        }
    //    }
    //     private void GenerateOtherpartyPreviousDiscount(){
    //      try{
    //          FormatGridOtherpartyDiscount();  //clear existing content of table
    //          String strOtherPartycode=txtOtherPartycode.getText().toString();
    //          ResultSet rsTmp,rsBuyer,rsIndent,rsRIA;
    //          String strSQL= "";
    //          //strSQL+="SELECT DISTINCT DISC_PER FROM PRODUCTION.FELT_DISCOUNT_MEMO ";
    //          //strSQL+="SELECT DISTINCT PRODUCT_CD,DISC_PER FROM PRODUCTION.FELT_DISCOUNT_MEMO ";
    //          strSQL+="SELECT PRODUCT_CD,DISC_PER,MAX(MEMO_DATE) AS CURRENT_MEMO_DATE,COUNT(PIECE_NO) AS COUNT_PCS FROM PRODUCTION.FELT_DISCOUNT_MEMO WHERE ";
    //          if(!txtOtherPartycode.getText().equals("")){
    //            //strSQL+="WHERE PARTY_CODE="+strPartycode+" ORDER BY DISC_PER DESC";
    //            //strSQL+="DISC_PER!=0 AND PARTY_CODE="+strPartycode+" AND MEMO_DATE>'2012-01-01'";
    //              //strSQL+="WHERE DISC_PER!=0 AND MEMO_DATE>'1900-01-01' AND PARTY_CODE='"+strPartycode+"';
    //              strSQL+=" PARTY_CODE="+strOtherPartycode+" AND ";
    //          }
    //         strSQL+="DISC_PER!=0 AND MEMO_DATE>'1900-01-01' GROUP BY PRODUCT_CD,DISC_PER ORDER BY DISC_PER DESC";
    //         rsTmp=data.getResult(strSQL);
    //         //rsTmp.first();
    //         if(rsTmp.getRow()>0) {
    //                int cnt=0;
    //                 while(!rsTmp.isAfterLast()) {
    //                    cnt++;
    //                 Object[] rowData=new Object[5];
    //                    rowData[0]=Integer.toString(cnt);
    //                    rowData[1]=rsTmp.getString("PRODUCT_CD");
    //                    rowData[2]=rsTmp.getString("DISC_PER");
    //                    rowData[3]=EITLERPGLOBAL.formatDate(rsTmp.getString("CURRENT_MEMO_DATE"));
    //                    rowData[4]=rsTmp.getString("COUNT_PCS");
    //                    DataModelOtherpartyDiscount.addRow(rowData);
    //              //   System.out.println("rsTmp.getString('PIECE_NO')");
    //                    rsTmp.next();
    //                 }
    //
    //          }
    //     }
    //
    //      catch(Exception e){
    //        e.printStackTrace();
    //        JOptionPane.showMessageDialog(null,e.getMessage());
    //      }
    //     }
}
