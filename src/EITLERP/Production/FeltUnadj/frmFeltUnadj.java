/*
 *
 */
package EITLERP.Production.FeltUnadj;

/**
 *
 * @author GAURANG SOLANKI
 */
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
import EITLERP.Production.FeltCreditNote.clsFeltCNAutoPosting;
import static EITLERP.Production.FeltCreditNote.clsFeltCNAutoPosting.*;
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
public class frmFeltUnadj extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsFeltF ObjSalesParty;
    private clsFeltUnadj ObjSalesParty;
    private clsExcelExporter exp = new clsExcelExporter();

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
    private EITLTableModel DataModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    // EITLTableModel DataModelDesc = new EITLTableModel();

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
    public String mdocno = "";
    private int charge09index = 0;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();
    String cellLastValue = "";
    boolean mchinests = false;
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module

    /**
     * Creates new form frmSalesParty
     */
    public frmFeltUnadj() {
        System.gc();
        setSize(900, 700);
        initComponents();
        cmdDelete.setEnabled(false);
        cmdDelete.disable();
        cmdDelete.setVisible(false);

        cmdPrint.setEnabled(false);
        cmdPrint.disable();
        cmdPrint.setVisible(false);

        cmdAdd.setVisible(false);
        cmdItemdelete.setVisible(false);
        btnshow.setVisible(false);

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

        CheckCNPrint();
        //FormatGrid();
        //GenerateGrid();

        //  cmdSelectAll.setEnabled(false);
        //  cmdClearAll.setEnabled(false);
        ObjSalesParty = new clsFeltUnadj();

        //lblDocThrough.setVisible(false);
        //txtDocThrough.setVisible(false);
        SetMenuForRights();

        if (ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID)) {
            ObjSalesParty.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data. \n Error is " + ObjSalesParty.LastError);
        }

        // txtfromdate.setEditable(false);
        txtAuditRemarks.setVisible(false);
        DataModelDesc.TableReadOnly(true);

    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsFeltUnadj.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsFeltUnadj.ModuleID + "");
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
        grpSisterConcern = new javax.swing.ButtonGroup();
        jFrame1 = new javax.swing.JFrame();
        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemDisc = new javax.swing.JMenuItem();
        jMenuItemSeam = new javax.swing.JMenuItem();
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
        jButton2 = new javax.swing.JButton();
        btnCNSumm = new javax.swing.JButton();
        btnCNDetail = new javax.swing.JButton();
        btnCNDraft = new javax.swing.JButton();
        btnCNGst = new javax.swing.JButton();
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
        txtunadjno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtfromdate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txttodate = new javax.swing.JTextField();
        btnshow = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtdate = new javax.swing.JTextField();
        lblremark1 = new javax.swing.JLabel();
        lblremark2 = new javax.swing.JLabel();
        txtremark1 = new javax.swing.JTextField();
        txtremark2 = new javax.swing.JTextField();

        jMenuItemDisc.setText("on Discount");
        jMenuItemDisc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDiscActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemDisc);

        jMenuItemSeam.setText("on Seam Charge");
        jMenuItemSeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSeamActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemSeam);

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
        lblTitle.setText("FELT UNADJUSTED TRN FORM -");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 30, 840, 20);

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
        jScrollPane1.setBounds(0, 0, 810, 230);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(700, 310, 90, 30);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(lblStatus);
        lblStatus.setBounds(10, 240, 610, 20);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        cmdAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdAddKeyPressed(evt);
            }
        });
        Tab1.add(cmdAdd);
        cmdAdd.setBounds(630, 240, 70, 25);

        cmdItemdelete.setText("Remove");
        cmdItemdelete.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdItemdelete.setEnabled(false);
        cmdItemdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdItemdeleteActionPerformed(evt);
            }
        });
        Tab1.add(cmdItemdelete);
        cmdItemdelete.setBounds(710, 240, 80, 25);

        jButton2.setText("Export To Excel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Tab1.add(jButton2);
        jButton2.setBounds(530, 310, 160, 30);

        btnCNSumm.setText("CN SUMMARY");
        btnCNSumm.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnCNSumm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNSummActionPerformed(evt);
            }
        });
        Tab1.add(btnCNSumm);
        btnCNSumm.setBounds(10, 270, 190, 30);

        btnCNDetail.setText("CN DETAIL");
        btnCNDetail.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnCNDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNDetailActionPerformed(evt);
            }
        });
        Tab1.add(btnCNDetail);
        btnCNDetail.setBounds(210, 270, 190, 30);

        btnCNDraft.setText("CN DRAFT ");
        btnCNDraft.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnCNDraft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNDraftActionPerformed(evt);
            }
        });
        Tab1.add(btnCNDraft);
        btnCNDraft.setBounds(410, 270, 190, 30);

        btnCNGst.setText("CN GST");
        btnCNGst.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnCNGst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNGstActionPerformed(evt);
            }
        });
        Tab1.add(btnCNGst);
        btnCNGst.setBounds(610, 270, 190, 30);

        jTabbedPane1.addTab("Unadjusted Detail", Tab1);

        ApprovalPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ApprovalPanel.setToolTipText("");
        ApprovalPanel.setLayout(null);

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
        OpgHold.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgHoldMouseClicked(evt);
            }
        });
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
        cmdNext3.setBounds(550, 300, 101, 25);

        jButton3.setText("Next >>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(jButton3);
        jButton3.setBounds(650, 300, 100, 25);

        jTabbedPane1.addTab("Approval", null, ApprovalPanel, "");

        StatusPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        StatusPanel.setLayout(null);

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
        jTabbedPane1.setBounds(20, 130, 830, 380);

        jLabel1.setText("UnAdjusted ID");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 60, 110, 15);

        txtunadjno.setEditable(false);
        txtunadjno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtunadjnoMouseClicked(evt);
            }
        });
        getContentPane().add(txtunadjno);
        txtunadjno.setBounds(120, 60, 120, 19);

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

        btnshow.setText("Show");
        btnshow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnshowActionPerformed(evt);
            }
        });
        getContentPane().add(btnshow);
        btnshow.setBounds(460, 90, 120, 30);

        jLabel3.setText("DATE");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(300, 60, 50, 15);

        txtdate.setEditable(false);
        getContentPane().add(txtdate);
        txtdate.setBounds(350, 60, 120, 19);

        lblremark1.setText("Remark 1");
        getContentPane().add(lblremark1);
        lblremark1.setBounds(30, 520, 90, 20);

        lblremark2.setText("Remark 2");
        getContentPane().add(lblremark2);
        lblremark2.setBounds(30, 540, 90, 20);

        txtremark1.setEnabled(false);
        getContentPane().add(txtremark1);
        txtremark1.setBounds(110, 520, 660, 20);

        txtremark2.setEnabled(false);
        getContentPane().add(txtremark2);
        txtremark2.setBounds(110, 540, 660, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {

            exp.fillData(TableDesc, new File("/root/Desktop/UNADJTRN.xls"));
            exp.fillData(TableDesc, new File("D://UNADJTRN.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/UNADJTRN.xls' successfully in Linux PC or 'D://UNADJ.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        // TODO add your handling code here:
        OpgHold.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgReject.setSelected(false);

        cmbSendTo.setEnabled(false);
    }//GEN-LAST:event_OpgHoldMouseClicked

    private void jMenuItemSeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSeamActionPerformed
        // TODO add your handling code here:

        //        cmdAdd.setVisible(true);
        //        cmdItemdelete.setVisible(true);
        //        btnshow.setVisible(true);
        //
        //        //Now Generate new document no.
        //        SelectFirstFree aList=new SelectFirstFree();
        //        aList.ModuleID=732;
        //
        //        if(aList.ShowList()) {
        //            EditMode=EITLERPGLOBAL.ADD;
        //            SetFields(true);
        //            DisableToolbar();
        //            ClearFields();
        //            SelPrefix=aList.Prefix; //Selected Prefix;
        //            SelSuffix=aList.Suffix;
        //            FFNo=aList.FirstFreeNo;
        //            SetupApproval();
        //            //Display newly generated document no.
        //            txtunadjno.setText("S"+ clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 732, FFNo,  false));
        //            txtdate.setText(EITLERPGLOBAL.getCurrentDate());
        //            txtfromdate.requestFocus();
        //
        //            lblTitle.setText("FELT UNADJUSTED TRN FORM - "+ txtunadjno.getText());
        //            lblTitle.setBackground(Color.BLUE);
        //        }
        //        else {
        //            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        //        }
        cmdAdd.setVisible(false);
        cmdItemdelete.setVisible(true);

        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 732;
        aList.FirstFreeNo = 189;

        // if(aList.ShowList()) {
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SelPrefix = aList.Prefix; //Selected Prefix;
        SelSuffix = aList.Suffix;
        FFNo = aList.FirstFreeNo;
        SetupApproval();
        //Display newly generated document no.
        txtunadjno.setText(clsFeltUnadj.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 732, FFNo, false));
        txtdate.setText(EITLERPGLOBAL.getCurrentDate());
        txtfromdate.requestFocus();

        lblTitle.setText("FELT UNADJUSTED TRN FORM - " + txtunadjno.getText());
        lblTitle.setBackground(Color.BLUE);

        btnshow.setVisible(true);

        btnCNSumm.setVisible(false);
        btnCNDetail.setVisible(false);
        btnCNDraft.setVisible(false);
            btnCNGst.setVisible(false);
        // }
        // else {
        //     JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        // }

    }//GEN-LAST:event_jMenuItemSeamActionPerformed

    private void jMenuItemDiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDiscActionPerformed
        // TODO add your handling code here:

        //  EditMode=EITLERPGLOBAL.ADD;
        //        cmdAdd.setVisible(true);
        //        cmdItemdelete.setVisible(true);
        //        btnshow.setVisible(true);
        //
        //        //Now Generate new document no.
        //        SelectFirstFree aList=new SelectFirstFree();
        //        aList.ModuleID=732;
        //
        //        if(aList.ShowList()) {
        //            EditMode=EITLERPGLOBAL.ADD;
        //            SetFields(true);
        //            DisableToolbar();
        //            ClearFields();
        //            SelPrefix=aList.Prefix; //Selected Prefix;
        //            SelSuffix=aList.Suffix;
        //            FFNo=aList.FirstFreeNo;
        //            SetupApproval();
        //            //Display newly generated document no.
        //            txtunadjno.setText("D"+ clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 732, FFNo,  false));
        //            txtdate.setText(EITLERPGLOBAL.getCurrentDate());
        //            txtfromdate.requestFocus();
        //
        //            lblTitle.setText("FELT UNADJUSTED TRN FORM - "+ txtunadjno.getText());
        //            lblTitle.setBackground(Color.BLUE);
        //        }
        //        else {
        //            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        //        }
        cmdAdd.setVisible(false);
        cmdItemdelete.setVisible(true);

        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 732;
        aList.FirstFreeNo = 188;

        // if(aList.ShowList()) {
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SelPrefix = aList.Prefix; //Selected Prefix;
        SelSuffix = aList.Suffix;
        FFNo = aList.FirstFreeNo;
        SetupApproval();
        //Display newly generated document no.
        txtunadjno.setText(clsFeltUnadj.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 732, FFNo, false));
        txtdate.setText(EITLERPGLOBAL.getCurrentDate());
        txtfromdate.requestFocus();

        lblTitle.setText("FELT UNADJUSTED TRN FORM - " + txtunadjno.getText());
        lblTitle.setBackground(Color.BLUE);

        btnshow.setVisible(true);

        btnCNSumm.setVisible(false);
        btnCNDetail.setVisible(false);
        btnCNDraft.setVisible(false);
            btnCNGst.setVisible(false);
        // }
        // else {
        //     JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        // }

    }//GEN-LAST:event_jMenuItemDiscActionPerformed

    private void cmdAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdAddKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdAddKeyPressed

    private void cmbHierarchyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbHierarchyFocusLost

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void btnshowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnshowActionPerformed
        FormatGrid();
        GenerateData();       // TODO add your handling code here:
    }//GEN-LAST:event_btnshowActionPerformed

    private void txtunadjnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtunadjnoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtunadjnoMouseClicked

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        ShowMessage("Select the user to whom document to be forwarded");        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSendToFocusGained

    private void cmdItemdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdItemdeleteActionPerformed
        // TODO add your handling code here:
        if (TableDesc.getRowCount() > 0) {
            DataModelDesc.removeRow(TableDesc.getSelectedRow());
            // DisplayIndicators();
        }

    }//GEN-LAST:event_cmdItemdeleteActionPerformed

    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed

//        String chk = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 1);
//        //        if(chk.equals("")){
//        System.out.println("chk =" + chk);
//
//        String UnadjId = txtunadjno.getText().substring(0, 2);
//
//        String FromDate = EITLERPGLOBAL.formatDateDB(txtfromdate.getText());
//        String ToDate = EITLERPGLOBAL.formatDateDB(txttodate.getText());
//
//        if (evt.getKeyCode() == evt.VK_ENTER && chk.toString().equals("")) {
//            String PieceNo = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 2);
//            String InvNo = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 4);
//
//            //        if(PieceNo.trim().equals("") || InvNo.trim().equals(""))
//            //        {
//            //            JOptionPane.showMessageDialog(null,"type");
//            //        }
//            //        else
//            //        {
//            //String query = "SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,F.SEAM_CHARGE,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMOUNT  FROM FELT_INVOICE_DATA F LEFT JOIN FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '2015-01-01' AND F.INVOICE_DATE<='2015-01-31' AND F.PIECE_NO='013221' AND F.INVOICE_NO='F002261' AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)";
//            String PieceDetail[] = clsFeltUnadj.getPiecedetail(UnadjId, PieceNo, InvNo, FromDate, ToDate);
//
//            TableDesc.setValueAt(PieceDetail[0], TableDesc.getSelectedRow(), 0);
//            TableDesc.setValueAt(PieceDetail[1], TableDesc.getSelectedRow(), 1);
//            TableDesc.setValueAt(PieceDetail[2], TableDesc.getSelectedRow(), 2);
//            TableDesc.setValueAt(PieceDetail[3], TableDesc.getSelectedRow(), 3);
//            TableDesc.setValueAt(PieceDetail[4], TableDesc.getSelectedRow(), 4);
//            TableDesc.setValueAt(PieceDetail[5], TableDesc.getSelectedRow(), 5);
//            TableDesc.setValueAt(PieceDetail[6], TableDesc.getSelectedRow(), 6);
//            TableDesc.setValueAt(PieceDetail[7], TableDesc.getSelectedRow(), 7);
//            TableDesc.setValueAt(PieceDetail[8], TableDesc.getSelectedRow(), 8);
//            TableDesc.setValueAt(PieceDetail[9], TableDesc.getSelectedRow(), 9);
//            TableDesc.setValueAt(PieceDetail[10], TableDesc.getSelectedRow(), 10);
//            TableDesc.setValueAt(PieceDetail[11], TableDesc.getSelectedRow(), 11);
//            TableDesc.setValueAt(PieceDetail[12], TableDesc.getSelectedRow(), 12);
//            TableDesc.setValueAt(PieceDetail[13], TableDesc.getSelectedRow(), 13);
//            TableDesc.setValueAt(PieceDetail[14], TableDesc.getSelectedRow(), 14);
//            TableDesc.setValueAt(PieceDetail[15], TableDesc.getSelectedRow(), 15);
//            TableDesc.setValueAt(PieceDetail[16], TableDesc.getSelectedRow(), 16);
//            TableDesc.setValueAt(PieceDetail[17], TableDesc.getSelectedRow(), 17);
//            TableDesc.setValueAt(PieceDetail[18], TableDesc.getSelectedRow(), 18);
//
//            //        }
//        }
//        // }
//        //  else{JOptionPane.showMessageDialog(null,"blank");}

    }//GEN-LAST:event_TableDescKeyPressed

    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased

//        String a = txtunadjno.getText().substring(0, 2);
//        //System.out.println("a = "+ a);
//
//        if (a.matches("UD")) {
//            String InvAmt = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 11);
//            String WorkDisc = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 14);
//
//            String NewDisamt = clsFeltUnadj.getNewDisamt(WorkDisc, InvAmt);
//            TableDesc.setValueAt(NewDisamt, TableDesc.getSelectedRow(), 18);
//        }
//        if (a.matches("SD")) {
//            String Width = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 8);
//            String SpiralChg = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 15);
//            String SeamDisc = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 17);
//
//            String NewDisamt = clsFeltUnadj.getSeamDisamt(Width, SeamDisc, SpiralChg);
//            TableDesc.setValueAt(NewDisamt, TableDesc.getSelectedRow(), 18);
//        }
        //        String chk = (String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 0);
        //        if(chk.equalsIgnoreCase("") || chk.equalsIgnoreCase(null))
        //        {
        //            JOptionPane.showMessageDialog(null,"true");
        //        }
        //        else
        //        {
        //            JOptionPane.showMessageDialog(null,"false");
        //        }

    }//GEN-LAST:event_TableDescKeyReleased

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed

        if (txtfromdate.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter From Date And To Date Detail First.");
            return;
        } else {

            Object[] rowData = new Object[45];
            //rowData[0]=Integer.toString(TableDesc.getRowCount()+1);
            rowData[0] = "";
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

            DataModelDesc.addRow(rowData);
            DataModelDesc.SetReadOnly(0);
            DataModelDesc.SetReadOnly(1);
            DataModelDesc.ResetReadOnly(2);
            DataModelDesc.SetReadOnly(3);
            DataModelDesc.ResetReadOnly(4);
            DataModelDesc.SetReadOnly(5);
            DataModelDesc.SetReadOnly(6);
            DataModelDesc.SetReadOnly(7);
            DataModelDesc.SetReadOnly(8);
            DataModelDesc.SetReadOnly(9);
            DataModelDesc.SetReadOnly(10);
            DataModelDesc.SetReadOnly(11);
            DataModelDesc.SetReadOnly(12);
            DataModelDesc.SetReadOnly(13);
            if (jMenuItemSeam.isSelected()) {
                DataModelDesc.SetReadOnly(14);
            }
            DataModelDesc.SetReadOnly(15);
            DataModelDesc.SetReadOnly(16);
            if (jMenuItemSeam.isSelected()) {
                DataModelDesc.SetReadOnly(17);
            }
            DataModelDesc.SetReadOnly(18);
            //DataModelDesc.SetReadOnly(19);
            //DataModelDesc.SetReadOnly(20);

            DataModelDesc.SetReadOnly(21);
            DataModelDesc.SetReadOnly(22);
            DataModelDesc.SetReadOnly(23);
            DataModelDesc.SetReadOnly(24);
            DataModelDesc.SetReadOnly(25);
            DataModelDesc.SetReadOnly(26);
            DataModelDesc.SetReadOnly(27);

            //Updating=false;
            TableDesc.changeSelection(TableDesc.getRowCount() - 1, 2, false, false);

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

        //String F6ID=txtunadjno.getText().trim();
        SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(732, txtunadjno.getText())) {
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
        OpgFinal.setSelected(true);
        OpgReject.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);
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
        Add();
        //btnshow.setVisible(true);

        // GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        // txtfromdate.setEditable(false);
        //  txtPartyname.setEditable(false);
        //  txtPartystation.setEditable(false);
        cmdAdd.setVisible(false);
        cmdItemdelete.setVisible(true);
        btnshow.setVisible(false);
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
        btnshow.setVisible(false);
        // GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
        cmdAdd.setVisible(false);
        cmdItemdelete.setVisible(false);
        btnshow.setVisible(false);
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
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void btnCNSummActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCNSummActionPerformed
        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtdate.getText()) + "','2020-07-10') FROM DUAL") <= 0) {
            String Title = "", strSQL = "";

            Title = "PARTY WISE UNADJUSTED CREDIT NOTE SUMMARY STATEMENT";
            strSQL = "SELECT CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,ROUND(CNH_CREDIT_AMOUNT,0) AS CNH_CREDIT_AMOUNT,CNH_INVOICE_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID LIKE ('%" + txtunadjno.getText() + "%') AND CNH_TYPE='UNADJ' ORDER BY CNH_SUB_ACCOUNT_CODE";

            PrintCNSummary(Title, strSQL);
        }
    }//GEN-LAST:event_btnCNSummActionPerformed

    private void btnCNDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCNDetailActionPerformed
        // TODO add your handling code here:
        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtdate.getText()) + "','2020-07-10') FROM DUAL") <= 0) {
            String Title = "", strSQL = "";
            String fileName = "/EITLERP/Production/FeltCreditNote/CreditNoteUNADJDetail.jrxml";

            Title = "PARTY WISE UNADJUSTED CREDIT NOTE DETAIL STATEMENT";
            strSQL = "SELECT CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,CND_INVOICE_NO,CND_RC_VOUCHER_NO AS CND_QUALITY,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_PERCENT,CND_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID LIKE ('%" + txtunadjno.getText() + "%') ORDER BY CND_SUB_ACCOUNT_CODE,CND_INVOICE_NO";

            PrintCNDetail(Title, strSQL, fileName);
        } else {
            try {

                Connection Conn = data.getConn();
                Statement st = Conn.createStatement();
                String title = "", strSQL = "";
                String fileName = "/EITLERP/Production/FeltUnadj/CreditNoteUnadjDetail_GST.jrxml";

                title = "PARTY WISE UNADJUSTED CREDIT NOTE DETAIL STATEMENT";
                strSQL = "SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID LIKE ('%" + txtunadjno.getText() + "%') ORDER BY CND_SUB_ACCOUNT_CODE,CND_NO";

                HashMap parameterMap = new HashMap();

                parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
                parameterMap.put("TITLE", title);

                EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(parameterMap, Conn);

                System.out.println("SQL QUERY : " + strSQL);
                rpt.setReportName(fileName, 1, strSQL);
                rpt.callReport();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnCNDetailActionPerformed

    private void btnCNDraftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCNDraftActionPerformed
        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtdate.getText()) + "','2020-07-10') FROM DUAL") <= 0) {
            String strSQL = "";

            strSQL = "SELECT CNH_ID,CNH_NO,CNH_TYPE,CNH_EFFECT,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CITY,CNH_PARTY_NAME,CNH_REASON_CODE,CNH_BOOK_CODE,CNH_PERCENT,ROUND(CNH_CREDIT_AMOUNT,0) AS CNH_CREDIT_AMOUNT,CNH_LINK_NO,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_INVOICE_AMOUNT,CNH_DRAFT_CR_NOTE_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_EXT11,CNH_FIN_VOUCHER_NO,CNH_REMARKS FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID LIKE ('%" + txtunadjno.getText() + "%') AND CNH_TYPE='UNADJ' ORDER BY CNH_SUB_ACCOUNT_CODE";

            PrintCNDraft(strSQL);
        } else {
            try {

                Connection Conn = data.getConn();
                Statement st = Conn.createStatement();
                String strSQL = "";

                strSQL = "SELECT CNH_ID,CNH_NO,CNH_TYPE,CNH_EFFECT,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CITY,CNH_PARTY_NAME,CNH_REASON_CODE,CNH_BOOK_CODE,CNH_PERCENT,ROUND(CNH_CREDIT_AMOUNT,0) AS CNH_CREDIT_AMOUNT,CNH_LINK_NO,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_INVOICE_AMOUNT,CNH_DRAFT_CR_NOTE_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_EXT11,CNH_FIN_VOUCHER_NO,CNH_REMARKS,CND_EXT11,CND_EXT12,CND_EXT13,CND_EXT14,CONCAT(CND_INVOICE_NO,'   &   ',DATE_FORMAT(CND_INVOICE_DATE,'%d/%m/%Y')) AS CND_INVOICE_NO_DATE FROM PRODUCTION.D_CREDIT_NOTE_HEADER,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_YEAR_MON_ID LIKE ('%" + txtunadjno.getText() + "%') AND CNH_TYPE='UNADJ' AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID ORDER BY CNH_SUB_ACCOUNT_CODE,CNH_NO";

                HashMap parameterMap = new HashMap();

                EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(parameterMap, Conn);

                System.out.println("SQL QUERY : " + strSQL);
                rpt.setReportName("/EITLERP/Production/FeltUnadj/CreditNoteUnadjDraft_GST.jrxml", 1, strSQL);
                rpt.callReport();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnCNDraftActionPerformed

    private void btnCNGstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCNGstActionPerformed
        if (data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(txtdate.getText()) + "','2020-07-10') FROM DUAL") > 0) {
            try {

                Connection Conn = data.getConn();
                Statement st = Conn.createStatement();
                String strSQL = "";

                strSQL = "SELECT * FROM (SELECT CNH_ID,CNH_NO,CNH_TYPE,CNH_EFFECT,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CITY,CNH_PARTY_NAME,CNH_REASON_CODE,CNH_BOOK_CODE,CNH_PERCENT,ROUND(CNH_CREDIT_AMOUNT,0) AS CNH_CREDIT_AMOUNT,CNH_LINK_NO,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_INVOICE_AMOUNT,CNH_DRAFT_CR_NOTE_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_EXT11,CNH_FIN_VOUCHER_NO,CNH_REMARKS,CND_EXT11,CND_EXT12,CND_EXT13,CND_EXT14,CND_CREDIT_AMOUNT,CND_INVOICE_NO,CND_INVOICE_DATE,CONCAT(CND_INVOICE_NO,'   &   ',DATE_FORMAT(CND_INVOICE_DATE,'%d/%m/%Y')) AS CND_INVOICE_NO_DATE FROM PRODUCTION.D_CREDIT_NOTE_HEADER,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_YEAR_MON_ID LIKE ('%" + txtunadjno.getText() + "%') AND CNH_TYPE='UNADJ' AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID) A LEFT JOIN (SELECT PARTY_CODE,GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER) B ON B.PARTY_CODE=A.CNH_SUB_ACCOUNT_CODE ORDER BY CNH_SUB_ACCOUNT_CODE,CNH_NO";
                
                HashMap parameterMap = new HashMap();

                EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(parameterMap, Conn);

                System.out.println("SQL QUERY : " + strSQL);
                rpt.setReportName("/EITLERP/Production/FeltUnadj/CreditNoteUnadjCN_GST.jrxml", 1, strSQL);
                rpt.callReport();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnCNGstActionPerformed


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
    private javax.swing.JButton btnCNDetail;
    private javax.swing.JButton btnCNDraft;
    private javax.swing.JButton btnCNGst;
    private javax.swing.JButton btnCNSumm;
    private javax.swing.JButton btnshow;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JMenuItem jMenuItemDisc;
    private javax.swing.JMenuItem jMenuItemSeam;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblremark1;
    private javax.swing.JLabel lblremark2;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtdate;
    private javax.swing.JTextField txtfromdate;
    private javax.swing.JTextField txtremark1;
    private javax.swing.JTextField txtremark2;
    private javax.swing.JTextField txttodate;
    private javax.swing.JTextField txtunadjno;
    // End of variables declaration//GEN-END:variables

    private void Add() {

        jPopupMenu.show(cmdNew, 0, 30);
        //        //  EditMode=EITLERPGLOBAL.ADD;
        //
        //        //Now Generate new document no.
        //        SelectFirstFree aList=new SelectFirstFree();
        //        aList.ModuleID=732;
        //
        //        if(aList.ShowList()) {
        //            EditMode=EITLERPGLOBAL.ADD;
        //            SetFields(true);
        //            DisableToolbar();
        //            ClearFields();
        //            SelPrefix=aList.Prefix; //Selected Prefix;
        //            SelSuffix=aList.Suffix;
        //            FFNo=aList.FirstFreeNo;
        //            SetupApproval();
        //            //Display newly generated document no.
        //            txtunadjno.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 732, FFNo,  false));
        //            txtdate.setText(EITLERPGLOBAL.getCurrentDate());
        //            txtfromdate.requestFocus();
        //
        //            lblTitle.setText("FELT UNADJUSTED TRN FROM - "+ txtunadjno.getText());
        //            lblTitle.setBackground(Color.BLUE);
        //        }
        //        else {
        //            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        //        }
    }

    private void SetupApproval() {
        /*
         if(cmbHierarchy.getItemCount()>1) {
         cmbHierarchy.setEnabled(true);
         }
         */
        //OpgHold.setSelected(true);

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

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsFeltUnadj.ModuleID, (String) ObjSalesParty.getAttribute("UNADJ_ID").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsFeltUnadj.ModuleID, FromUserID, (String) ObjSalesParty.getAttribute("UNADJ_ID").getObj());

            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();

        if (clsHierarchy.CanSkip(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }

        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        } else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }

        //In Edit Mode Hierarchy Should be disabled
        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(clsFeltUnadj.ModuleID, txtunadjno.getText())) {
                OpgReject.setEnabled(false);
            }
        }

        if (EditMode == 0) {
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

                String ProformaNo = (String) ObjSalesParty.getAttribute("UNADJ_ID").getObj();

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsFeltUnadj.ModuleID, ProformaNo);
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

        txtfromdate.setEnabled(pStat);
        txttodate.setEnabled(pStat);
        txtremark1.setEnabled(pStat);
        txtremark2.setEnabled(pStat);

        txtToRemarks.setEnabled(pStat);
//        cmdAdd.setEnabled(pStat);
        cmdAdd.setEnabled(false);
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
//        cmdNew.setEnabled(true);
        cmdNew.setEnabled(false);
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
        int ValidEntryCount = 0;

        if (txtfromdate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter From Date");
            txtfromdate.requestFocus(true);
            return false;
        }
        //Now Header level validations
        if (txttodate.getText().trim().equals("") && OpgFinal.isSelected()) {
            JOptionPane.showMessageDialog(null, "Please enter To Date");
            txttodate.requestFocus(true);
            return false;
        }

        if (!txtfromdate.getText().trim().equals("")) {
            if (EditMode == EITLERPGLOBAL.ADD) {
                /*if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE UNADJ_ID='"+txtunadjno.getText().trim()+"'"))  {
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
            if (txtfromdate.getText().trim().substring(0, 4).equals("NEWD")) {
                JOptionPane.showMessageDialog(null, "Invalid Party Code");
                txtfromdate.requestFocus(true);
                return false;
            }
        }
        return true;
    }

    private void ClearFields() {

        txtfromdate.setText("");
        txttodate.setText("");
        txtremark1.setText("");
        txtremark2.setText("");
        txtToRemarks.setText("");
        FormatGrid();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
        //GenerateGrid();
    }

    private void Edit() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }

        //----------------------------------//
        String docno = ObjSalesParty.getAttribute("UNADJ_ID").getString();
        if (ObjSalesParty.IsEditable(EITLERPGLOBAL.gCompanyID, docno, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (clsFeltProductionApprovalFlow.IsCreator(clsFeltUnadj.ModuleID, docno) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7041, 70412)) {
                SetFields(true);
            } else {
                EnableApproval();
            }

            txtfromdate.setEnabled(false);
            txttodate.setEnabled(false);

            cmdAdd.setVisible(false);
            cmdItemdelete.setVisible(true);

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

        String lDocNo = (String) ObjSalesParty.getAttribute("UNADJ_ID").getObj();

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
        if (Validate() == false) {
            return; //Validation failed
        }

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Please select the hierarchy.");
            return;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null, "Please select the Approval Action");
            return;
        }

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

            if (ObjSalesParty.Insert()) {
                // MoveLast();
                DisplayData();
                //     String tnxtno="";
                //   tnxtno=clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 731, FFNo,  true);
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjSalesParty.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjSalesParty.Update()) {
                DisplayData();

                String query = "SELECT USER_ID FROM D_COM_HIERARCHY H,D_COM_HIERARCHY_RIGHTS HR WHERE H.HIERARCHY_ID=HR.HIERARCHY_ID AND MODULE_ID=" + clsFeltUnadj.ModuleID + " AND HR.HIERARCHY_ID=" + EITLERPGLOBAL.getComboCode(cmbHierarchy) + " AND SR_NO=2";
                int AreaIncharge = data.getIntValueFromDB(query);
                if ((AreaIncharge == EITLERPGLOBAL.gNewUserID) && (OpgApprove.isSelected())) {
                    try {
                        String DOC_NO = txtunadjno.getText();
                        String DOC_DATE = txtdate.getText();
                        String Party_Code = "";

                        //String responce = JavaMail.sendFinalApprovalMail(603, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                        String responce = JavaMail.sendNotificationMailOfDetail(clsFeltUnadj.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                        System.out.println("Send Mail Responce : " + responce);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (OpgFinal.isSelected()) {
                    try {
                        String DOC_NO = txtunadjno.getText();
                        String DOC_DATE = txtdate.getText();
                        String Party_Code = "";

                        //String responce = JavaMail.sendFinalApprovalMail(603, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                        String responce = JavaMail.sendNotificationMailOfDetail(clsFeltUnadj.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
                        System.out.println("Send Mail Responce : " + responce);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
        //chkOtherparty.setSelected(false);
        EnableToolbar();
        SetMenuForRights();
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.Production.FeltUnadj.frmFeltUnadjFind", true);
        frmFeltUnadjFind ObjReturn = (frmFeltUnadjFind) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            if (!ObjSalesParty.Filter(ObjReturn.stringFindQuery)) {
                JOptionPane.showMessageDialog(null, "No records found.");
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
            if (EditMode == 0) {
                if (ObjSalesParty.getAttribute("APPROVED").getInt() == 1) {

                    lblTitle.setBackground(Color.BLUE);
                }

                if (ObjSalesParty.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                }

                if (ObjSalesParty.getAttribute("CANCELED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = clsFeltUnadj.ModuleID;

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
            lblTitle.setText("FELT UNADJUSTED TRN FORM-" + (String) ObjSalesParty.getAttribute("UNADJ_ID").getObj());
            txtunadjno.setText((String) ObjSalesParty.getAttribute("UNADJ_ID").getObj());
            txtdate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("UNADJ_DATE").getString()));
            txtfromdate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("UNADJ_FROM_DATE").getString()));
            txttodate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("UNADJ_TO_DATE").getString()));
            txtremark1.setText((String) ObjSalesParty.getAttribute("H_REMARK1").getObj());
            txtremark2.setText((String) ObjSalesParty.getAttribute("H_REMARK2").getObj());

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());

            //===================Fill up Table===================//
            FormatGrid();
            DoNotEvaluate = true;
            //Now Generate Table
            for (int i = 1; i <= ObjSalesParty.colMRItems.size(); i++) {

                clsFeltUnadjitem ObjItem = (clsFeltUnadjitem) ObjSalesParty.colMRItems.get(Integer.toString(i));
                Object[] rowData = new Object[50];

                // rowData[0]=(String)ObjItem.getAttribute(" ").getObj();
                rowData[0] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();
                rowData[1] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[2] = (String) ObjItem.getAttribute("PIECE_NO").getObj();
                rowData[3] = (String) ObjItem.getAttribute("PRODUCT_CODE").getObj();
                rowData[4] = (String) ObjItem.getAttribute("INVOICE_NO").getObj();
                rowData[5] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("INVOICE_DATE").getObj());
                //rowData[5]=(String)ObjItem.getAttribute("INVOICE_DATE").getObj();
                rowData[6] = (String) ObjItem.getAttribute("KG").getObj();
                rowData[7] = (String) ObjItem.getAttribute("SQR_MTR").getObj();
                rowData[8] = (String) ObjItem.getAttribute("WIDTH").getObj();
                rowData[9] = (String) ObjItem.getAttribute("LENGTH").getObj();
                rowData[10] = (String) ObjItem.getAttribute("RATE").getObj();
                rowData[11] = (String) ObjItem.getAttribute("INV_BASIC_AMT").getObj();
                rowData[12] = (String) ObjItem.getAttribute("INV_DISC_PER").getObj();
                rowData[13] = (String) ObjItem.getAttribute("SANC_DISC_PER").getObj();
                rowData[14] = (String) ObjItem.getAttribute("WORK_DISC_PER").getObj();
                rowData[15] = (String) ObjItem.getAttribute("INV_SEAM_CHARGES").getObj();
                rowData[16] = (String) ObjItem.getAttribute("SANC_SEAM_CHARGES").getObj();
                rowData[17] = (String) ObjItem.getAttribute("SEAM_PER").getObj();
                rowData[18] = (String) ObjItem.getAttribute("DISC_AMT").getObj();
                rowData[19] = (String) ObjItem.getAttribute("D_REMARK1").getObj();
                rowData[20] = (String) ObjItem.getAttribute("D_REMARK2").getObj();

                rowData[21] = (String) ObjItem.getAttribute("IGST_PER").getObj();
                rowData[22] = (String) ObjItem.getAttribute("IGST_AMT").getObj();
                rowData[23] = (String) ObjItem.getAttribute("CGST_PER").getObj();
                rowData[24] = (String) ObjItem.getAttribute("CGST_AMT").getObj();
                rowData[25] = (String) ObjItem.getAttribute("SGST_PER").getObj();
                rowData[26] = (String) ObjItem.getAttribute("SGST_AMT").getObj();

                rowData[27] = (String) ObjItem.getAttribute("TOTAL_DISC_AMT").getObj();

                DataModelDesc.addRow(rowData);
            }

            DoNotEvaluate = false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap List = new HashMap();

            String ProformaNo = ObjSalesParty.getAttribute("UNADJ_ID").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsFeltUnadj.ModuleID, ProformaNo);
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
            FormatGridUpdateHistory();
            HashMap History = clsFeltUnadj.getHistoryList(EITLERPGLOBAL.gCompanyID, ProformaNo);
            for (int i = 1; i <= History.size(); i++) {
                clsFeltUnadj ObjHistory = (clsFeltUnadj) History.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATE_BY").getVal());
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

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }

                rowData[3] = ApprovalStatus;
                rowData[4] = ObjHistory.getAttribute("APPROVER_REMARKS").getString();
                DataModelHS.addRow(rowData);
            }
            //=========================================//
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Display Data Error: " + e.getMessage());
        }

        CheckCNPrint();
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields

        ObjSalesParty.setAttribute("UNADJ_ID", txtunadjno.getText());
        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjSalesParty.setAttribute("UNADJ_DATE", EITLERPGLOBAL.formatDateDB(txtdate.getText()));
        }
        ObjSalesParty.setAttribute("UNADJ_FROM_DATE", EITLERPGLOBAL.formatDateDB(txtfromdate.getText()));
        ObjSalesParty.setAttribute("UNADJ_TO_DATE", EITLERPGLOBAL.formatDateDB(txttodate.getText()));

        ObjSalesParty.setAttribute("H_REMARK1", txtremark1.getText());
        ObjSalesParty.setAttribute("H_REMARK2", txtremark2.getText());

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
            ObjSalesParty.setAttribute("UNADJ_DATE", EITLERPGLOBAL.getCurrentDateDB());
            ObjSalesParty.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjSalesParty.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjSalesParty.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjSalesParty.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        //======= Set Line part ============
        ObjSalesParty.colMRItems.clear();
        String mposition;
        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            clsFeltUnadjitem ObjItem = new clsFeltUnadjitem();

            ObjItem.setAttribute("UNADJ_ID", txtunadjno.getText());

            ObjItem.setAttribute("PARTY_NAME", (String) TableDesc.getValueAt(i, 0));
            ObjItem.setAttribute("PARTY_CODE", (String) TableDesc.getValueAt(i, 1));
            ObjItem.setAttribute("PIECE_NO", (String) TableDesc.getValueAt(i, 2));
            ObjItem.setAttribute("PRODUCT_CODE", (String) TableDesc.getValueAt(i, 3));
            ObjItem.setAttribute("INVOICE_NO", (String) TableDesc.getValueAt(i, 4));
            ObjItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDateDB((String) TableDesc.getValueAt(i, 5)));
            ObjItem.setAttribute("KG", (String) TableDesc.getValueAt(i, 6));
            ObjItem.setAttribute("SQR_MTR", (String) TableDesc.getValueAt(i, 7));
            ObjItem.setAttribute("WIDTH", (String) TableDesc.getValueAt(i, 8));
            ObjItem.setAttribute("LENGTH", (String) TableDesc.getValueAt(i, 9));
            ObjItem.setAttribute("RATE", (String) TableDesc.getValueAt(i, 10));
            ObjItem.setAttribute("INV_BASIC_AMT", (String) TableDesc.getValueAt(i, 11));
            ObjItem.setAttribute("INV_DISC_PER", (String) TableDesc.getValueAt(i, 12));
            ObjItem.setAttribute("SANC_DISC_PER", (String) TableDesc.getValueAt(i, 13));
            ObjItem.setAttribute("WORK_DISC_PER", (String) TableDesc.getValueAt(i, 14));
            ObjItem.setAttribute("INV_SEAM_CHARGES", (String) TableDesc.getValueAt(i, 15));
            ObjItem.setAttribute("SANC_SEAM_CHARGES", (String) TableDesc.getValueAt(i, 16));
            ObjItem.setAttribute("SEAM_PER", (String) TableDesc.getValueAt(i, 17));
            ObjItem.setAttribute("DISC_AMT", (String) TableDesc.getValueAt(i, 18));
            ObjItem.setAttribute("D_REMARK1", (String) TableDesc.getValueAt(i, 19));
            ObjItem.setAttribute("D_REMARK2", (String) TableDesc.getValueAt(i, 20));

            ObjItem.setAttribute("IGST_PER", (String) TableDesc.getValueAt(i, 21));
            ObjItem.setAttribute("IGST_AMT", (String) TableDesc.getValueAt(i, 22));
            ObjItem.setAttribute("CGST_PER", (String) TableDesc.getValueAt(i, 23));
            ObjItem.setAttribute("CGST_AMT", (String) TableDesc.getValueAt(i, 24));
            ObjItem.setAttribute("SGST_PER", (String) TableDesc.getValueAt(i, 25));
            ObjItem.setAttribute("SGST_AMT", (String) TableDesc.getValueAt(i, 26));

            ObjItem.setAttribute("TOTAL_DISC_AMT", (String) TableDesc.getValueAt(i, 27));

            ObjSalesParty.colMRItems.put(Integer.toString(ObjSalesParty.colMRItems.size() + 1), ObjItem);

        }

    }

    private void FormatGridApprovalStatus() {
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7041, 70411)) { //7008,70081
//            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7041, 70412)) { //7008,70082
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7041, 70413)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7041, 70414)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
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
        String FieldName = "";
        int SelHierarchy = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        for (int i = 0; i < Tab1.getComponentCount() - 1; i++) {
            if (Tab1.getComponent(i).getName() != null) {

                FieldName = Tab1.getComponent(i).getName();

                //   if(FieldName.trim().equals("MM_PARTY_CODE")) {
                //    int a=0;
                //   }
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
        //ObjSalesParty.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsFeltF.ModuleID+")");
        //ObjSalesParty.Filter(" UNADJ_ID IN (SELECT PRODUCTION.FELT_MACHINE_MASTER_HEADER.UNADJ_ID FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_HEADER.UNADJ_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=732)");
        ObjSalesParty.Filter(" UNADJ_ID IN (SELECT PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=732)");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    public void Find(String docNo) {
        ObjSalesParty.Filter(" UNADJ_ID='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pUnadjId) {

        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjSalesParty.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjSalesParty.Filter(" UNADJ_ID='" + pUnadjId + "'");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        String UnadjId = txtunadjno.getText();

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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsFeltUnadj.ModuleID, UnadjId, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsFeltUnadj.ModuleID, UnadjId, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                    //IncludeUser=true;
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsFeltUnadj.ModuleID, UnadjId);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
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

            //DataModelDesc.addColumn("Sr.");    //0
            DataModelDesc.addColumn("Party Name");    //1
            DataModelDesc.addColumn("Party Code");    //2
            DataModelDesc.addColumn("Piece No");    //3
            DataModelDesc.addColumn("Product Code");    //4
            DataModelDesc.addColumn("Invoice No");    //5
            DataModelDesc.addColumn("Invoice Date");    //6
            DataModelDesc.addColumn("KGS");    //7
            DataModelDesc.addColumn("SQR MTR");    //8
            DataModelDesc.addColumn("WIDTH");    //9
            DataModelDesc.addColumn("LENGTH");    //10
            DataModelDesc.addColumn("RATE");    //11
            DataModelDesc.addColumn("BASIC AMT");    //12
            DataModelDesc.addColumn("INV Disc %");    //13
            DataModelDesc.addColumn("SANC Disc %");    //14
            DataModelDesc.addColumn("Working Disc %");    //15
            DataModelDesc.addColumn("Inv Seam Charges");    //16
            DataModelDesc.addColumn("Sanc Seam %");    //17
            DataModelDesc.addColumn("Seam %");    //18
            DataModelDesc.addColumn("Disc AMT");    //19
            DataModelDesc.addColumn("Remark1");    //20
            DataModelDesc.addColumn("Remark2");    //21

            DataModelDesc.addColumn("IGST PER");    //22
            DataModelDesc.addColumn("IGST AMT");    //23
            DataModelDesc.addColumn("CGST PER");    //24
            DataModelDesc.addColumn("CGST AMT");    //25
            DataModelDesc.addColumn("SGST PER");    //26
            DataModelDesc.addColumn("SGST AMT");    //27

            DataModelDesc.addColumn("Total Disc AMT");    //28

            //DataModelDesc.TableReadOnly(true);
            //DataModelDesc.SetVariable(0,"SR_NO");  //0
            DataModelDesc.SetVariable(0, "PARTY_NAME");    //1
            DataModelDesc.SetVariable(1, "PARTY_CODE");    //2
            DataModelDesc.SetVariable(2, "PIECE_NO");    //3
            DataModelDesc.SetVariable(3, "PRODUCT_CODE");    //4
            DataModelDesc.SetVariable(4, "INVOICE_NO");    //5
            DataModelDesc.SetVariable(5, "INVOICE_DATE");    //6
            DataModelDesc.SetVariable(6, "KG");    //7
            DataModelDesc.SetVariable(7, "SQR_MTR");    //8
            DataModelDesc.SetVariable(8, "WIDTH");    //9
            DataModelDesc.SetVariable(9, "LENGTH");    //10
            DataModelDesc.SetVariable(10, "RATE");    //11
            DataModelDesc.SetVariable(11, "INV_BASIC_AMT");    //12
            DataModelDesc.SetVariable(12, "INV_DISC_PER");    //13
            DataModelDesc.SetVariable(13, "SANC_DISC_PER");    //14
            DataModelDesc.SetVariable(14, "WORK_DISC_PER");    //15
            DataModelDesc.SetVariable(15, "INV_SEAM_CHARGES");    //16
            DataModelDesc.SetVariable(16, "SANC_SEAM_CHARGES");    //17
            DataModelDesc.SetVariable(17, "SEAM_PER");    //18
            DataModelDesc.SetVariable(18, "DISC_AMT");    //19
            DataModelDesc.SetVariable(19, "D_REMARK1");    //20
            DataModelDesc.SetVariable(20, "D_REMARK2");    //21

            DataModelDesc.SetVariable(21, "IGST_PER");    //22
            DataModelDesc.SetVariable(22, "IGST_AMT");    //23
            DataModelDesc.SetVariable(23, "CGST_PER");    //24
            DataModelDesc.SetVariable(24, "CGST_AMT");    //25
            DataModelDesc.SetVariable(25, "SGST_PER");    //26
            DataModelDesc.SetVariable(26, "SGST_AMT");    //27

            DataModelDesc.SetVariable(27, "TOTAL_DISC_AMT");    //28

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
//            if (jMenuItemSeam.isSelected()) {
                DataModelDesc.SetReadOnly(14);
//            }
            DataModelDesc.SetReadOnly(15);
            DataModelDesc.SetReadOnly(16);
//            if (jMenuItemDisc.isSelected()) {
                DataModelDesc.SetReadOnly(17);
//            }
            DataModelDesc.SetReadOnly(18);
            //DataModelDesc.SetReadOnly(19);
            //DataModelDesc.SetReadOnly(20);

            DataModelDesc.SetReadOnly(21);
            DataModelDesc.SetReadOnly(22);
            DataModelDesc.SetReadOnly(23);
            DataModelDesc.SetReadOnly(24);
            DataModelDesc.SetReadOnly(25);
            DataModelDesc.SetReadOnly(26);

            DataModelDesc.SetReadOnly(27);

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

                            ShowMessage("Ready...");

                            //       if(last==7){
                            //         TableDesc.editCellAt(TableDesc.getSelectedRow(),TableDesc.getSelectedColumn()+1);
                            //    }
                        }
                    }
            );

            TableDesc.getColumnModel().getColumn(17).setMinWidth(0);
            TableDesc.getColumnModel().getColumn(17).setMaxWidth(0);

        } catch (Exception e) {

        }
        Updating = false;
        //Table formatting completed

    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);

    }

    private void PreviewReport() {
        String UnadjId = txtunadjno.getText();
        String FromDt = EITLERPGLOBAL.formatDateDB(txtfromdate.getText());
        String ToDt = EITLERPGLOBAL.formatDateDB(txttodate.getText());

        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Conn = data.getConn();
            st = Conn.createStatement();

            rs = st.executeQuery("SELECT STATUS FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + UnadjId + "' AND STATUS = 'F' ");
            rs.first();

            HashMap parameterMap = new HashMap();

            String fdt = EITLERPGLOBAL.formatDate(FromDt);
            String tdt = EITLERPGLOBAL.formatDate(ToDt);

            parameterMap.put("FROM_DATE", fdt);
            parameterMap.put("TO_DATE", tdt);

            if (rs.getString("STATUS").equals("F")) {
                ReportRegister rpt = new ReportRegister(parameterMap, Conn);
                //String sql = "select d.bill_no,d.item_type,d.quality_cd,d.retail_rate,sum(d.sale_qty) as sale_qty,sum(d.item_amount) as item_amount,sum(d.item_disc_amount) AS discount,sum(d.item_amount-d.item_disc_amount) as item_net_amount,d.bill_date,h.bill_discount_amount,h.net_payble,case when (d.BILL_TYPE IN ('SPECIAL','EMPLOYEE','SHARE HOLDER')) then sum(d.item_type_disc_amount) else 0.00 end as special,h.cash_payment,case when (h.CARD_TYPE IN('VISA','MASTER')) then h.card_payment else 0.00 end as card_pay,case when (h.CARD_TYPE='CHEQUE') then h.card_payment else 0.00 end as cheque_pay from BILL_DETAIL d,BILL_HEADER h where d.bill_no=h.bill_no and d.bill_date=h.bill_date and h.bill_status is NULL group by d.quality_cd,d.bill_no having d.bill_date = '" + pdt + "' order by d.bill_date,d.bill_no*1";
                String sql = "SELECT PARTY_NAME,PARTY_CODE,PIECE_NO,PRODUCT_CODE,INVOICE_NO,INVOICE_DATE,CASE WHEN UNADJ_ID LIKE ('UD%') THEN KG ELSE WIDTH END AS KGS_M2_WIDTH,CASE WHEN UNADJ_ID LIKE ('UD%') THEN INV_BASIC_AMT ELSE 'SEAM CHARGES' END AS BASIC_AMT,WORK_DISC_PER AS DISC_PER,DISC_AMT FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='" + UnadjId + "'";
                //rpt.setReportName("/report/billregister.jrxml", 1, sql); //productlist is the name of my jasper file.
                rpt.setReportName("/EITLERP/Production/FeltUnadj/unadjtrn.jrxml", 1, sql); //productlist is the name of my jasper file.
                rpt.callReport();
            } else {
                ReportRegister rpt = new ReportRegister(parameterMap, Conn);
                //String sql = "select d.bill_no,d.item_type,d.quality_cd,d.retail_rate,sum(d.sale_qty) as sale_qty,sum(d.item_amount) as item_amount,sum(d.item_disc_amount) AS discount,sum(d.item_amount-d.item_disc_amount) as item_net_amount,d.bill_date,h.bill_discount_amount,h.net_payble,case when (d.BILL_TYPE IN ('SPECIAL','EMPLOYEE','SHARE HOLDER')) then sum(d.item_type_disc_amount) else 0.00 end as special,h.cash_payment,case when (h.CARD_TYPE IN('VISA','MASTER')) then h.card_payment else 0.00 end as card_pay,case when (h.CARD_TYPE='CHEQUE') then h.card_payment else 0.00 end as cheque_pay from BILL_DETAIL d,BILL_HEADER h where d.bill_no=h.bill_no and d.bill_date=h.bill_date and h.bill_status is NULL group by d.quality_cd,d.bill_no having d.bill_date = '" + pdt + "' order by d.bill_date,d.bill_no*1";
                String sql = "SELECT PARTY_NAME,PARTY_CODE,PIECE_NO,PRODUCT_CODE,INVOICE_NO,INVOICE_DATE,CASE WHEN UNADJ_ID LIKE ('UD%') THEN KG ELSE WIDTH END AS KGS_M2_WIDTH,CASE WHEN UNADJ_ID LIKE ('UD%') THEN INV_BASIC_AMT ELSE 'SEAM CHARGES' END AS BASIC_AMT,WORK_DISC_PER AS DISC_PER,DISC_AMT FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='" + UnadjId + "'";
                //rpt.setReportName("/report/billregister.jrxml", 1, sql); //productlist is the name of my jasper file.
                rpt.setReportName("/EITLERP/Production/FeltUnadj/unadjtrn4.jrxml", 1, sql); //productlist is the name of my jasper file.
                rpt.callReport();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                Conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getNextFreeNo(int pCompanyID, int pModuleID, String pPrefix, String pSuffix, boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        String strNewNo = "";
        int lnNewNo = 0;

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL = "SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID=" + pCompanyID + " AND MODULE_ID=" + pModuleID + " AND PREFIX_CHARS='" + pPrefix + "' ";
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                lnNewNo = rsTmp.getInt("LAST_USED_NO") + 1;
                strNewNo = EITLERPGLOBAL.Padding(Integer.toString(lnNewNo), rsTmp.getInt("NO_LENGTH"), rsTmp.getString("PADDING_BY"));

                if (UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='" + strNewNo.trim() + "',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND MODULE_ID=" + pModuleID + " AND PREFIX_CHARS='" + pPrefix + "' ");
                }

                strNewNo = pPrefix + strNewNo + pSuffix;

                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();

                return strNewNo;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private void GenerateData() {
        try {

            //String strSQL="SELECT * FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' ";
            String strSQL = "";
            String a = txtunadjno.getText().substring(0, 2);
            System.out.println("a = " + a);

            if (a.matches("UD")) {
                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,ROUND(((F.SPIRAL_CHG/F.WIDTH)/4899)*100,2) AS INV_SEAM_CHARGES,ROUND(D.SEAM_VALUE,2) AS SANC_SEAM_CHARGES,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND F.QUALITY_NO NOT LIKE '7190%' AND F.TRD_DISCOUNT<((F.GROSS_AMOUNT*D.DISC_PER)/100) AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO) ORDER BY F.PARTY_CODE,F.INVOICE_DATE,F.INVOICE_NO";
                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,ROUND(((F.SPIRAL_CHG/F.WIDTH)/4899)*100,2) AS INV_SEAM_CHARGES,ROUND(D.SEAM_VALUE,2) AS SANC_SEAM_CHARGES,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND F.QUALITY_NO NOT LIKE '7190%' AND F.TRD_DISCOUNT<((F.GROSS_AMOUNT*D.DISC_PER)/100) AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO) ORDER BY F.PARTY_CODE,F.INVOICE_DATE,F.INVOICE_NO";

                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,ROUND(F.SPIRAL_CHG,2) AS INV_SEAM_CHARGES,ROUND(D.SEAM_VALUE,2) AS SANC_SEAM_CHARGES,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND D.EFFECTIVE_FROM>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND D.EFFECTIVE_TO<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND ROUND(F.TRD_DISCOUNT,2)<ROUND(((F.GROSS_AMOUNT*D.DISC_PER)/100),2) AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO) ORDER BY F.PARTY_CODE,F.INVOICE_DATE,F.INVOICE_NO";
                UnadjDataProcess();
                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,ROUND(F.SPIRAL_CHG,2) AS INV_SEAM_CHARGES,ROUND(D.SEAM_VALUE,2) AS SANC_SEAM_CHARGES,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.PARTY_CODE=D.PARTY_CODE AND F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND D.EFFECTIVE_FROM>='"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND (D.EFFECTIVE_TO<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' OR D.EFFECTIVE_TO IS NULL) AND ROUND(F.TRD_DISCOUNT,2)<ROUND(((F.GROSS_AMOUNT*D.DISC_PER)/100),2) AND D.APPROVED=1 AND D.CANCELED=0 AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO) ORDER BY F.PARTY_CODE,F.INVOICE_DATE,F.INVOICE_NO";
                strSQL = "SELECT * FROM PRODUCTION.TEMP_DATA WHERE DISC_AMT>5 ORDER BY PARTY_CODE,INVOICE_DATE,INVOICE_NO";

            }
            if (a.matches("SD")) {
                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,F.SPIRAL_CHG AS INV_SEAM_CHARGES,D.SEAM_VALUE AS SANC_SEAM_CHARGES,F.SPIRAL_CHG AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND F.QUALITY_NO LIKE '7190%' AND F.SPIRAL_CHG>0 AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U WHERE U.UNADJ_ID LIKE ('SD%') GROUP BY U.INVOICE_NO,U.PIECE_NO) ORDER BY F.PARTY_CODE,F.INVOICE_DATE,F.INVOICE_NO";
                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,ROUND(((F.SPIRAL_CHG/F.WIDTH)/4899)*100,2) AS INV_SEAM_CHARGES,ROUND(D.SEAM_VALUE,2) AS SANC_SEAM_CHARGES,(F.SPIRAL_CHG-(ROUND((D.SEAM_VALUE*4899)/100,0)*F.WIDTH) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND F.QUALITY_NO LIKE '7190%' AND F.SPIRAL_CHG>0 AND F.SPIRAL_CHG<(ROUND((D.SEAM_VALUE*4899)/100,0)*F.WIDTH) AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U WHERE U.UNADJ_ID LIKE ('SD%') GROUP BY U.INVOICE_NO,U.PIECE_NO) ORDER BY F.PARTY_CODE,F.INVOICE_DATE,F.INVOICE_NO";

                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,ROUND(F.SPIRAL_CHG,2) AS INV_SEAM_CHARGES,ROUND(D.SEAM_VALUE,2) AS SANC_SEAM_CHARGES,ROUND(F.SPIRAL_CHG-F.WIDTH*(4899*(100-D.SEAM_VALUE)/100),2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND D.EFFECTIVE_FROM>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND D.EFFECTIVE_TO<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND F.QUALITY_NO LIKE '7190%' AND F.SPIRAL_CHG>0 AND ROUND(F.SPIRAL_CHG,2)>ROUND(F.WIDTH*ROUND(4899*(100-D.SEAM_VALUE)/100,2),2) AND ROUND(F.SPIRAL_CHG-F.WIDTH*(4899*(100-D.SEAM_VALUE)/100),2)>5 AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U WHERE U.UNADJ_ID LIKE ('SD%') GROUP BY U.INVOICE_NO,U.PIECE_NO) ORDER BY F.PARTY_CODE,F.INVOICE_DATE,F.INVOICE_NO";
                UnadjSeamDataProcess();
                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,ROUND(F.SPIRAL_CHG,2) AS INV_SEAM_CHARGES,ROUND(D.SEAM_VALUE,2) AS SANC_SEAM_CHARGES,ROUND(F.SPIRAL_CHG-F.WIDTH*(4899*(100-D.SEAM_VALUE)/100),2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.PARTY_CODE=D.PARTY_CODE AND F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND D.EFFECTIVE_FROM>='"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND (D.EFFECTIVE_TO<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' OR D.EFFECTIVE_TO IS NULL) AND F.QUALITY_NO LIKE '7190%' AND F.SPIRAL_CHG>0 AND ROUND(F.SPIRAL_CHG,2)>ROUND(F.WIDTH*ROUND(4899*(100-D.SEAM_VALUE)/100,2),2) AND ROUND(F.SPIRAL_CHG-F.WIDTH*(4899*(100-D.SEAM_VALUE)/100),2)>5 AND D.APPROVED=1 AND D.CANCELED=0 AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U WHERE U.UNADJ_ID LIKE ('SD%') GROUP BY U.INVOICE_NO,U.PIECE_NO) ORDER BY F.PARTY_CODE,F.INVOICE_DATE,F.INVOICE_NO";
                strSQL = "SELECT * FROM PRODUCTION.TEMP_DATA WHERE DISC_AMT>5 ORDER BY PARTY_CODE,INVOICE_DATE,INVOICE_NO";

                //  ROUND(ROUND(((F.SPIRAL_CHG/F.WIDTH)*100)/4899,2)-ROUND(D.SEAM_VALUE,2),2) AS SEAM_PER
                //  AND ROUND(ROUND(F.SPIRAL_CHG,2)-ROUND(F.WIDTH*ROUND(4899*(100-D.SEAM_VALUE)/100,0),2),2)>0
            }
            System.out.println(strSQL);

            //String strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,F.SEAM_CHARGE AS INV_SEAM_CHARGE,D.SEAM_VALUE AS SANC_SEAM_CHARGE,ROUND((D.SEAM_VALUE-F.SEAM_CHARGE),2) AS WORK_SEAM_CHARGE,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND F.TRD_DISCOUNT<((F.GROSS_AMOUNT*D.DISC_PER)/100) AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)";
            ResultSet rsTmp = data.getResult(strSQL);
            rsTmp.first();

            int Counter = 0;

            if (rsTmp.getRow() > 0) {

                while (!rsTmp.isAfterLast()) {
                    Counter++;
                    // objRow=objReportData.newRow();
                    Object[] rowData = new Object[50];

                    //objRow=objReportData.newRow();
                    //  rowData[0]=UtilFunctions.getString(rsTmp,"","");
                    //rowData[0]=UtilFunctions.getString(rsTmp,"INVOICE_NO","");
                    rowData[0] = UtilFunctions.getString(rsTmp, "PARTY_NAME", "");
                    rowData[1] = UtilFunctions.getString(rsTmp, "PARTY_CODE", "");
                    rowData[2] = UtilFunctions.getString(rsTmp, "PIECE_NO", "");
                    rowData[3] = UtilFunctions.getString(rsTmp, "QUALITY_NO", "");
                    rowData[4] = UtilFunctions.getString(rsTmp, "INVOICE_NO", "");
                    rowData[5] = EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp, "INVOICE_DATE", ""));
                    rowData[6] = UtilFunctions.getString(rsTmp, "GROSS_KG", "");
                    rowData[7] = UtilFunctions.getString(rsTmp, "GROSS_SQ_MTR", "");
                    rowData[8] = UtilFunctions.getString(rsTmp, "WIDTH", "");
                    rowData[9] = UtilFunctions.getString(rsTmp, "LENGTH", "");
                    rowData[10] = UtilFunctions.getString(rsTmp, "RATE", "");
                    rowData[11] = UtilFunctions.getString(rsTmp, "GROSS_AMOUNT", "");
                    rowData[12] = UtilFunctions.getString(rsTmp, "INV_DISC_PER", "");
                    rowData[13] = UtilFunctions.getString(rsTmp, "SANC_DISC_PER", "");
                    rowData[14] = UtilFunctions.getString(rsTmp, "WORK_DISC_PER", "");
                    rowData[15] = UtilFunctions.getString(rsTmp, "INV_SEAM_CHARGES", "");
                    rowData[16] = UtilFunctions.getString(rsTmp, "SANC_SEAM_CHARGES", "");
                    //rowData[17]=UtilFunctions.getString(rsTmp,"SEAM_PER","");
                    //rowData[17]="0.00";
                    rowData[18] = UtilFunctions.getString(rsTmp, "DISC_AMT", "");
                    ////rowData[19]=UtilFunctions.getString(rsTmp,"REMARK1","");
                    ////rowData[20]=UtilFunctions.getString(rsTmp,"REMARK2","");

                    rowData[21] = UtilFunctions.getString(rsTmp, "IGST_PER", "");
                    rowData[22] = UtilFunctions.getString(rsTmp, "IGST_AMT", "");
                    rowData[23] = UtilFunctions.getString(rsTmp, "CGST_PER", "");
                    rowData[24] = UtilFunctions.getString(rsTmp, "CGST_AMT", "");
                    rowData[25] = UtilFunctions.getString(rsTmp, "SGST_PER", "");
                    rowData[26] = UtilFunctions.getString(rsTmp, "SGST_AMT", "");

                    rowData[27] = UtilFunctions.getString(rsTmp, "TOTAL_DISC_AMT", "");

                    DataModelDesc.addRow(rowData);
                    rsTmp.next();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void UnadjDataProcess() {
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

//            stmt.execute("TRUNCATE PRODUCTION.TEMP_DATA");
            stmt.execute("DELETE FROM PRODUCTION.TEMP_DATA");

            //stmt.execute("INSERT INTO PRODUCTION.TEMP_DATA(INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,NET_AMOUNT,SEAM_CHARGE,SPIRAL_CHG) SELECT INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,NET_AMOUNT,SEAM_CHARGE,SPIRAL_CHG FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) +"' AND INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText()) +"' AND PARTY_CODE IN(SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.MASTER_NO=D.MASTER_NO) AND CONCAT(INVOICE_NO,INVOICE_DATE) NOT IN(SELECT CONCAT(U.INVOICE_NO,U.INVOICE_DATE) AS INV FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H WHERE H.UNADJ_ID=U.UNADJ_ID AND H.CANCELLED=0 GROUP BY U.INVOICE_NO,U.INVOICE_DATE,U.PIECE_NO)");            
            //stmt.execute("INSERT INTO PRODUCTION.TEMP_DATA(INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,NET_AMOUNT,SEAM_CHARGE) SELECT INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,NET_AMOUNT,SEAM_CHARGE FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) +"' AND INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText()) +"' AND PARTY_CODE IN(SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.MASTER_NO=D.MASTER_NO) AND CONCAT(INVOICE_NO,INVOICE_DATE) NOT IN(SELECT CONCAT(U.INVOICE_NO,U.INVOICE_DATE) AS INV FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H WHERE H.UNADJ_ID=U.UNADJ_ID AND H.CANCELLED=0 GROUP BY U.INVOICE_NO,U.INVOICE_DATE,U.PIECE_NO)");
            stmt.execute("INSERT INTO PRODUCTION.TEMP_DATA(INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,NET_AMOUNT,SEAM_CHARGE,IGST_PER,CGST_PER,SGST_PER) SELECT INVOICE_NO,INVOICE_DATE,PRODUCT_CODE,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,BAS_AMT,SQMTR,ACTUAL_WEIGHT,GROSS_AMT,DISC_AMT,NET_AMT,SEAM_CHG,IGST_PER,CGST_PER,SGST_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND INVOICE_DATE<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE IN ( SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.MASTER_NO=D.MASTER_NO UNION ALL SELECT DISTINCT D.PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE ) AND CONCAT(INVOICE_NO,INVOICE_DATE) NOT IN(SELECT CONCAT(U.INVOICE_NO,U.INVOICE_DATE) AS INV FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H WHERE H.UNADJ_ID=U.UNADJ_ID AND H.CANCELLED=0 AND H.UNADJ_ID LIKE 'U%' GROUP BY U.INVOICE_NO,U.INVOICE_DATE,U.PIECE_NO)");

            /*
             SELECT T.PARTY_CODE,D.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO,T.INVOICE_DATE,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE
             FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D,PRODUCTION.TEMP_DATA T
             WHERE D.PARTY_CODE=T.PARTY_CODE
             AND D.PRODUCT_CODE=T.QUALITY_NO
             AND D.EFFECTIVE_FROM>='2015-04-01' AND D.EFFECTIVE_TO<='2016-03-31'
             AND D.APPROVED=1 AND D.CANCELED=0
             AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO
             GROUP BY T.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO,T.INVOICE_DATE
             ORDER BY T.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO;
            
             SELECT T.PARTY_CODE,D.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO,T.INVOICE_DATE,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE
             FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D,PRODUCTION.TEMP_DATA T
             WHERE D.PARTY_CODE=T.PARTY_CODE
             AND D.PRODUCT_CODE=T.QUALITY_NO
             AND D.EFFECTIVE_FROM>='2015-04-01' AND D.EFFECTIVE_TO IS NULL
             AND D.APPROVED=1 AND D.CANCELED=0
             AND T.INVOICE_DATE>=D.EFFECTIVE_FROM
             GROUP BY T.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO,T.INVOICE_DATE
             ORDER BY T.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO;
             */
            stmt.execute("DELETE FROM PRODUCTION.TEMP_DATA WHERE CONCAT(INVOICE_NO,INVOICE_DATE) IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE) FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=1 AND H.CANCELED=0) ");

            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D SET T.DISC_GROUP_CODE=D.GROUP_CODE WHERE H.GROUP_CODE=D.GROUP_CODE AND D.PARTY_CODE=T.PARTY_CODE AND H.APPROVED=1 AND H.CANCELED=0");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.EFFECTIVE_TO=D.EFFECTIVE_TO,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.GROUP_CODE=T.DISC_GROUP_CODE AND LENGTH(D.GROUP_CODE)>3 AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.GROUP_CODE=T.DISC_GROUP_CODE AND LENGTH(D.GROUP_CODE)>3 AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO ORDER BY D.MASTER_NO DESC ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.GROUP_CODE=TD.DISC_GROUP_CODE AND LENGTH(DE.GROUP_CODE)>3 AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT * FROM (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.GROUP_CODE=T.DISC_GROUP_CODE AND LENGTH(D.GROUP_CODE)>3 AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO AND D.MASTER_NO IN (SELECT MAX(D.MASTER_NO) FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.GROUP_CODE=T.DISC_GROUP_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO GROUP BY D.GROUP_CODE,D.PRODUCT_CODE,D.EFFECTIVE_FROM) ORDER BY D.MASTER_NO DESC) AS X GROUP BY X.GROUP_CODE,X.PRODUCT_CODE,X.EFFECTIVE_FROM ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.GROUP_CODE=TD.DISC_GROUP_CODE AND LENGTH(DE.GROUP_CODE)>3 AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.EFFECTIVE_TO=D.EFFECTIVE_TO,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO");
            //stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO ");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO AND D.MASTER_NO IN (SELECT MAX(D.MASTER_NO) FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO GROUP BY D.PARTY_CODE,D.PRODUCT_CODE,D.EFFECTIVE_FROM) GROUP BY D.PARTY_CODE,D.PRODUCT_CODE,D.EFFECTIVE_FROM) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO ");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM ORDER BY D.MASTER_NO DESC ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO IS NULL AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE>=DE.EFFECTIVE_FROM");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.EFFECTIVE_TO=D.EFFECTIVE_TO,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=RIGHT(T.PIECE_NO,5) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=RIGHT(T.PIECE_NO,5) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO ORDER BY D.MASTER_NO DESC ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND DE.PIECE_NO=RIGHT(TD.PIECE_NO,5) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=T.PIECE_NO AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO ORDER BY D.MASTER_NO DESC ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND DE.PIECE_NO=TD.PIECE_NO AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=RIGHT(T.PIECE_NO,5)  AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=RIGHT(T.PIECE_NO,5)  AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM ORDER BY D.MASTER_NO DESC ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND DE.PIECE_NO=RIGHT(TD.PIECE_NO,5)  AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO IS NULL AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE>=DE.EFFECTIVE_FROM");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=T.PIECE_NO  AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM ORDER BY D.MASTER_NO DESC ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND DE.PIECE_NO=TD.PIECE_NO  AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO IS NULL AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE>=DE.EFFECTIVE_FROM");

            //SELECT * FROM PRODUCTION.TEMP_DATA WHERE EFFECTIVE_FROM='0000-00-00';
            stmt.execute("DELETE FROM PRODUCTION.TEMP_DATA WHERE EFFECTIVE_FROM='0000-00-00'");

            //SELECT * FROM PRODUCTION.TEMP_DATA;
            //stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/GROSS_AMOUNT),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/GROSS_AMOUNT)),2),INV_SEAM_CHARGES=ROUND(SPIRAL_CHG,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND((GROSS_AMOUNT*DISC_PER)/100-TRD_DISCOUNT,2)");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/GROSS_AMOUNT),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/GROSS_AMOUNT)),2),INV_SEAM_CHARGES=ROUND(SEAM_CHARGE,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND((GROSS_AMOUNT*DISC_PER)/100-TRD_DISCOUNT,2)");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/GROSS_AMOUNT),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/GROSS_AMOUNT)),2),INV_SEAM_CHARGES=ROUND(SEAM_CHARGE,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND((TOTAL_GROSS*DISC_PER)/100-TRD_DISCOUNT,2)");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/TOTAL_GROSS),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/TOTAL_GROSS)),2),INV_SEAM_CHARGES=ROUND(SEAM_CHARGE,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND((TOTAL_GROSS*DISC_PER)/100-TRD_DISCOUNT,2)");

            stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET IGST_AMT=ROUND(DISC_AMT*(IGST_PER/100),0),CGST_AMT=ROUND(DISC_AMT*(CGST_PER/100),0),SGST_AMT=ROUND(DISC_AMT*(SGST_PER/100),0),TOTAL_DISC_AMT=ROUND(DISC_AMT+IGST_AMT+CGST_AMT+SGST_AMT,2)");

            //SELECT * FROM PRODUCTION.TEMP_DATA WHERE DISC_AMT>5;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UnadjSeamDataProcess() {
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE PRODUCTION.TEMP_DATA");

//            stmt.execute("INSERT INTO PRODUCTION.TEMP_DATA(INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,NET_AMOUNT,SEAM_CHARGE,SPIRAL_CHG) SELECT INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,NET_AMOUNT,SEAM_CHARGE,SPIRAL_CHG FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) +"' AND INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText()) +"' AND QUALITY_NO LIKE '7190%' AND SPIRAL_CHG>0 AND PARTY_CODE IN(SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.MASTER_NO=D.MASTER_NO) AND CONCAT(INVOICE_NO,INVOICE_DATE) NOT IN(SELECT CONCAT(U.INVOICE_NO,U.INVOICE_DATE) AS INV FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.INVOICE_DATE,U.PIECE_NO)");//AND INVOICE_NO NOT IN(SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)");            
            stmt.execute("INSERT INTO PRODUCTION.TEMP_DATA(INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,NET_AMOUNT,SEAM_CHARGE,IGST_PER,CGST_PER,SGST_PER) SELECT INVOICE_NO, INVOICE_DATE, PRODUCT_CODE, PIECE_NO, PARTY_CODE, PARTY_NAME, LENGTH, WIDTH, RATE, BAS_AMT,  SQMTR, ACTUAL_WEIGHT, GROSS_AMT, DISC_AMT, NET_AMT, SEAM_CHG, IGST_PER, CGST_PER, SGST_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND INVOICE_DATE<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND APPROVED=1 AND CANCELLED=0 AND PRODUCT_CODE LIKE '7190%' AND SEAM_CHG>0 AND PARTY_CODE IN(SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.MASTER_NO=D.MASTER_NO UNION ALL SELECT DISTINCT D.PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE ) AND CONCAT(INVOICE_NO,INVOICE_DATE) NOT IN(SELECT CONCAT(U.INVOICE_NO,U.INVOICE_DATE) AS INV FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H WHERE H.UNADJ_ID=U.UNADJ_ID AND H.CANCELLED=0 AND H.UNADJ_ID LIKE 'S%' GROUP BY U.INVOICE_NO,U.INVOICE_DATE,U.PIECE_NO)");//AND INVOICE_NO NOT IN(SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)");            

            /*
             SELECT T.PARTY_CODE,D.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO,T.INVOICE_DATE,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE
             FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D,PRODUCTION.TEMP_DATA T
             WHERE D.PARTY_CODE=T.PARTY_CODE
             AND D.PRODUCT_CODE=T.QUALITY_NO
             AND D.EFFECTIVE_FROM>='2015-04-01' AND D.EFFECTIVE_TO<='2016-03-31'
             AND D.APPROVED=1 AND D.CANCELED=0
             AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO
             GROUP BY T.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO,T.INVOICE_DATE
             ORDER BY T.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO;
            
             SELECT T.PARTY_CODE,D.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO,T.INVOICE_DATE,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE
             FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D,PRODUCTION.TEMP_DATA T
             WHERE D.PARTY_CODE=T.PARTY_CODE
             AND D.PRODUCT_CODE=T.QUALITY_NO
             AND D.EFFECTIVE_FROM>='2015-04-01' AND D.EFFECTIVE_TO IS NULL
             AND D.APPROVED=1 AND D.CANCELED=0
             AND T.INVOICE_DATE>=D.EFFECTIVE_FROM
             GROUP BY T.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO,T.INVOICE_DATE
             ORDER BY T.PARTY_CODE,D.PRODUCT_CODE,T.INVOICE_NO;
             */
            stmt.execute("DELETE FROM PRODUCTION.TEMP_DATA WHERE CONCAT(INVOICE_NO,INVOICE_DATE) IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE) FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=1 AND H.CANCELED=0) ");

            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_GROUP_MASTER_HEADER H,PRODUCTION.FELT_GROUP_MASTER_DETAIL D SET T.DISC_GROUP_CODE=D.GROUP_CODE WHERE H.GROUP_CODE=D.GROUP_CODE AND D.PARTY_CODE=T.PARTY_CODE AND H.APPROVED=1 AND H.CANCELED=0");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.EFFECTIVE_TO=D.EFFECTIVE_TO,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.GROUP_CODE=T.DISC_GROUP_CODE AND LENGTH(D.GROUP_CODE)>3 AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.GROUP_CODE=T.DISC_GROUP_CODE AND LENGTH(D.GROUP_CODE)>3 AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO ORDER BY D.MASTER_NO DESC) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.GROUP_CODE=TD.DISC_GROUP_CODE AND LENGTH(DE.GROUP_CODE)>3 AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT * FROM (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.GROUP_CODE=T.DISC_GROUP_CODE AND LENGTH(D.GROUP_CODE)>3 AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO AND D.MASTER_NO IN (SELECT MAX(D.MASTER_NO) FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.GROUP_CODE=T.DISC_GROUP_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO GROUP BY D.GROUP_CODE,D.PRODUCT_CODE,D.EFFECTIVE_FROM) ORDER BY D.MASTER_NO DESC) AS X GROUP BY X.GROUP_CODE,X.PRODUCT_CODE,X.EFFECTIVE_FROM ) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.GROUP_CODE=TD.DISC_GROUP_CODE AND LENGTH(DE.GROUP_CODE)>3 AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.EFFECTIVE_TO=D.EFFECTIVE_TO,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO ORDER BY D.MASTER_NO DESC) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND SUBSTRING(D.PRODUCT_CODE,1,6)=SUBSTRING(T.QUALITY_NO,1,6) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM ORDER BY D.MASTER_NO DESC) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO IS NULL AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE>=DE.EFFECTIVE_FROM");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.EFFECTIVE_TO=D.EFFECTIVE_TO,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=RIGHT(T.PIECE_NO,5) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=RIGHT(T.PIECE_NO,5) AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO ORDER BY D.MASTER_NO DESC) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND DE.PIECE_NO=RIGHT(TD.PIECE_NO,5) AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=T.PIECE_NO AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE BETWEEN D.EFFECTIVE_FROM AND D.EFFECTIVE_TO ORDER BY D.MASTER_NO DESC) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND DE.PIECE_NO=TD.PIECE_NO AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO<='" + EITLERPGLOBAL.formatDateDB(txttodate.getText()) + "' AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE BETWEEN DE.EFFECTIVE_FROM AND DE.EFFECTIVE_TO");

//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET T.DISC_PER=D.DISC_PER,T.EFFECTIVE_FROM=D.EFFECTIVE_FROM,T.SEAM_VALUE=D.SEAM_VALUE WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=RIGHT(T.PIECE_NO,5)  AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=RIGHT(T.PIECE_NO,5)  AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM ORDER BY D.MASTER_NO DESC) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND DE.PIECE_NO=RIGHT(TD.PIECE_NO,5)  AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO IS NULL AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE>=DE.EFFECTIVE_FROM");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA AS TD, (SELECT D.PARTY_CODE,D.GROUP_CODE,D.PRODUCT_CODE,D.PIECE_NO,D.DISC_PER,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.SEAM_VALUE,D.APPROVED,D.CANCELED FROM PRODUCTION.TEMP_DATA T,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE D.PARTY_CODE=T.PARTY_CODE AND D.PIECE_NO=T.PIECE_NO  AND D.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND D.EFFECTIVE_TO IS NULL AND D.APPROVED=1 AND D.CANCELED=0 AND T.INVOICE_DATE>=D.EFFECTIVE_FROM ORDER BY D.MASTER_NO DESC) AS DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE DE.PARTY_CODE=TD.PARTY_CODE AND DE.PIECE_NO=TD.PIECE_NO  AND DE.EFFECTIVE_FROM>='" + EITLERPGLOBAL.formatDateDB(txtfromdate.getText()) + "' AND DE.EFFECTIVE_TO IS NULL AND DE.APPROVED=1 AND DE.CANCELED=0 AND TD.INVOICE_DATE>=DE.EFFECTIVE_FROM");

            //SELECT * FROM PRODUCTION.TEMP_DATA WHERE EFFECTIVE_FROM='0000-00-00';
            stmt.execute("DELETE FROM PRODUCTION.TEMP_DATA WHERE EFFECTIVE_FROM='0000-00-00'");

            //SELECT * FROM PRODUCTION.TEMP_DATA;
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/GROSS_AMOUNT),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/GROSS_AMOUNT)),2),INV_SEAM_CHARGES=ROUND(SPIRAL_CHG,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND(SPIRAL_CHG-WIDTH*(4899*(100-SEAM_VALUE)/100),2)");
//            stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/GROSS_AMOUNT),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/GROSS_AMOUNT)),2),INV_SEAM_CHARGES=ROUND(SEAM_CHARGE,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND(SEAM_CHARGE-WIDTH*(4899*(100-SEAM_VALUE)/100),2)");
            stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/TOTAL_GROSS),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/TOTAL_GROSS)),2),INV_SEAM_CHARGES=ROUND(SEAM_CHARGE,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND(SEAM_CHARGE-WIDTH*(4899*(100-SEAM_VALUE)/100),2)");

            stmt.execute("UPDATE PRODUCTION.TEMP_DATA SET IGST_AMT=ROUND(DISC_AMT*(IGST_PER/100),0),CGST_AMT=ROUND(DISC_AMT*(CGST_PER/100),0),SGST_AMT=ROUND(DISC_AMT*(SGST_PER/100),0),TOTAL_DISC_AMT=ROUND(DISC_AMT+IGST_AMT+CGST_AMT+SGST_AMT,2)");

            //SELECT * FROM PRODUCTION.TEMP_DATA WHERE DISC_AMT>5;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckCNPrint() {
        if (data.IsRecordExist("SELECT DISTINCT CNH_YEAR_MON_ID FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID LIKE ('%" + txtunadjno.getText() + "%')")) {
            btnCNSumm.setVisible(true);
            btnCNDetail.setVisible(true);
            btnCNDraft.setVisible(true);
            btnCNGst.setVisible(true);
        } else {
            btnCNSumm.setVisible(false);
            btnCNDetail.setVisible(false);
            btnCNDraft.setVisible(false);
            btnCNGst.setVisible(false);
        }
    }

}
