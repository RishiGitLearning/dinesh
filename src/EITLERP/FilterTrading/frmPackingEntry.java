/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP.FilterTrading;

/**
 *
 * @author
 */
/*<APPLET CODE=frmSalesParty.class HEIGHT=574 WIDTH=758></APPLET>*/
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import TReportWriter.SimpleDataProvider.TRow;
import TReportWriter.SimpleDataProvider.TTable;
import TReportWriter.TReportEngine;
import javax.swing.table.*;
import java.lang.*;
import java.sql.*;
import java.lang.String;

//import EITLERP.Purchase.frmSendMail;
public class frmPackingEntry extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsPackingentry ObjPackingEntry;
    private clsPackingentry ObjPackingEntry;
    private TReportEngine objEngine = new TReportEngine();

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;

    private EITLTableModel DataModelPiece;
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

    /**
     * Creates new form frmSalesParty
     */
    public frmPackingEntry() {
        System.gc();
        setSize(800, 700);
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

        //ObjColumn.LoadData((int)EITLERPGLOBAL.gCompanyID);
        GenerateCombos();
        FormatGrid();
        //GenerateGrid();

        //  cmdSelectAll.setEnabled(false);
        //  cmdClearAll.setEnabled(false);
        ObjPackingEntry = new clsPackingentry();

        //lblDocThrough.setVisible(false);
        //txtDocThrough.setVisible(false);
        SetMenuForRights();

        if (ObjPackingEntry.LoadData(EITLERPGLOBAL.gCompanyID)) {
            ObjPackingEntry.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data. \n Error is " + ObjPackingEntry.LastError);
        }

        txtAuditRemarks.setVisible(false);
        DataModelPiece.TableReadOnly(true);

    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsPackingentry.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsPackingentry.ModuleID + "");
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
        TableEntry = new javax.swing.JTable();
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
        lblStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtpckno = new javax.swing.JTextField();
        lblpcd = new javax.swing.JLabel();
        txtPartycode = new javax.swing.JTextField();
        lblpcdname = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtstation = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtsalent = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbtrns = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtpckdt = new javax.swing.JTextField();

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

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("FILTERFABRIC TRADING PACKING ENTRY");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 800, 25);

        Tab1.setLayout(null);

        TableEntry.setModel(new javax.swing.table.DefaultTableModel(
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
        TableEntry.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TableEntry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableEntryKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(TableEntry);

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
        cmdAdd.setBounds(500, 10, 70, 29);

        cmdItemdelete.setText("Remove");
        cmdItemdelete.setEnabled(false);
        cmdItemdelete.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdItemdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdItemdeleteActionPerformed(evt);
            }
        });
        Tab1.add(cmdItemdelete);
        cmdItemdelete.setBounds(580, 10, 80, 30);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(660, 300, 90, 29);

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

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(5, 260, 780, 370);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(20, 640, 610, 20);

        jLabel1.setText("Packing Slip No.:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 80, 110, 30);

        txtpckno.setEditable(false);
        getContentPane().add(txtpckno);
        txtpckno.setBounds(130, 80, 110, 27);

        lblpcd.setText("Party Code :");
        getContentPane().add(lblpcd);
        lblpcd.setBounds(10, 120, 100, 20);

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
        txtPartycode.setBounds(130, 115, 110, 27);
        getContentPane().add(lblpcdname);
        lblpcdname.setBounds(260, 117, 450, 20);

        jLabel2.setText("Station :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 157, 110, 20);
        getContentPane().add(txtstation);
        txtstation.setBounds(130, 150, 230, 27);

        jLabel3.setText("SaleNote No. :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 190, 110, 20);
        getContentPane().add(txtsalent);
        txtsalent.setBounds(130, 185, 230, 27);

        jLabel4.setText("Mode of Transport:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 227, 130, 20);

        cmbtrns.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1->Angadia ", "2->Transport ", "3->P.Train ", "4->I.R.P.P. ", "5->H.Delivery ", "6->By Air/Sea" }));
        cmbtrns.setSelectedIndex(1);
        getContentPane().add(cmbtrns);
        cmbtrns.setBounds(140, 220, 210, 27);

        jLabel5.setText("Packing Date :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(270, 80, 110, 30);

        txtpckdt.setEditable(false);
        getContentPane().add(txtpckdt);
        txtpckdt.setBounds(370, 80, 140, 27);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        PreviewReport();
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
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed

        Save();

    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        //ObjColumn.Close();
        ObjPackingEntry.Close();
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
        ObjPackingEntry.LoadData(EITLERPGLOBAL.gCompanyID);
        //MoveFirst();
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String PackingNo = txtpckno.getText();
        ObjPackingEntry.ShowHistory(EITLERPGLOBAL.gCompanyID, PackingNo);
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
        SetupApproval();
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

    private void cmdItemdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdItemdeleteActionPerformed
        if (TableEntry.getRowCount() > 0) {
            DataModelPiece.removeRow(TableEntry.getSelectedRow());
            // DisplayIndicators();
        }
    }//GEN-LAST:event_cmdItemdeleteActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed

        if (txtPartycode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Party Code Detail First.");
            return;
        } else {

            Updating = true;
            Object[] rowData = new Object[10];
            rowData[0] = Integer.toString(TableEntry.getRowCount() + 1);
            rowData[1] = "";
            rowData[2] = "";
            rowData[3] = "";
            rowData[4] = "";
            rowData[5] = "";
            rowData[6] = "";
            rowData[7] = "";
            rowData[8] = "";

            DataModelPiece.addRow(rowData);
            Updating = false;
            TableEntry.changeSelection(TableEntry.getRowCount() - 1, 1, false, false);
            TableEntry.requestFocus();
        }

    }//GEN-LAST:event_cmdAddActionPerformed

    private void TableEntryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableEntryKeyPressed
        String seleval;
        if (evt.getKeyCode() == 112) { //f1 Press
            String strSQL;
            if (TableEntry.getSelectedColumn() == 1) {

                searchkey aList = new searchkey();
                strSQL = "SELECT CONCAT(QUALITY_CD,PIECE_NO) AS PKEY,QUALITY_CD,PIECE_NO,GROSS_METER,FLAG_CD,NET_METER,WIDTH,SQ_METER,KGS FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER WHERE PIECE_STATUS='F'";

                aList.SQL = strSQL;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 0;
                if (aList.ShowFFTLOV()) {
                    if (TableEntry.getCellEditor() != null) {
                        TableEntry.getCellEditor().stopCellEditing();
                    }
                    seleval = aList.ReturnVal;

                    TableEntry.setValueAt(aList.ReturnVal, TableEntry.getSelectedRow(), 1);

                    String[] Piecedetail = clsPackingentry.getPiecedetail(seleval);
                    TableEntry.setValueAt(Piecedetail[0], TableEntry.getSelectedRow(), 1);
                    TableEntry.setValueAt(Piecedetail[1], TableEntry.getSelectedRow(), 2);
                    TableEntry.setValueAt(Piecedetail[2], TableEntry.getSelectedRow(), 3);
                    TableEntry.setValueAt(Piecedetail[3], TableEntry.getSelectedRow(), 4);
                    TableEntry.setValueAt(Piecedetail[4], TableEntry.getSelectedRow(), 5);
                    TableEntry.setValueAt(Piecedetail[5], TableEntry.getSelectedRow(), 6);
                    TableEntry.setValueAt(Piecedetail[6], TableEntry.getSelectedRow(), 7);
                    TableEntry.setValueAt(Piecedetail[7], TableEntry.getSelectedRow(), 8);

                    TableEntry.changeSelection(TableEntry.getSelectedRow(), 1, false, false);
                }
            }
        }
    }//GEN-LAST:event_TableEntryKeyPressed

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
                lblpcdname.setText(rsTmp.getString("PARTY_NAME"));
                txtstation.setText(rsTmp.getString("DISPATCH_STATION"));
            }
        } catch (Exception e) {

        }
    }//GEN-LAST:event_txtPartycodeFocusLost

    private void txtPartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartycodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210072' ORDER BY PARTY_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtPartycode.setText(aList.ReturnVal);
                lblpcdname.setText(clsPackingentry.getParyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
                txtstation.setText(clsPackingentry.getStation(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtPartycodeKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApprovalPanel;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JPanel Tab1;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableEntry;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JComboBox cmbtrns;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblpcd;
    private javax.swing.JLabel lblpcdname;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtPartycode;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtpckdt;
    private javax.swing.JTextField txtpckno;
    private javax.swing.JTextField txtsalent;
    private javax.swing.JTextField txtstation;
    // End of variables declaration//GEN-END:variables

    private void Add() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        cmbtrns.setSelectedIndex(1);
        //----------------------------------//        
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 748;

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
            txtpckno.setText("F" + clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 748, FFNo, false));
            txtpckdt.setText(EITLERPGLOBAL.getCurrentDate());
            txtPartycode.requestFocus();

        } else {
            JOptionPane.showMessageDialog(null, "You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
    }

    private void SetupApproval() {
        /*
         if(cmbHierarchy.getItemCount()>1) {
         cmbHierarchy.setEnabled(true);
         }
         */
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

            int FromUserID = clsFilterFabricApprovalFlow.getFromID( clsPackingentry.ModuleID, (String) ObjPackingEntry.getAttribute("PACKING_NOTE_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFilterFabricApprovalFlow.getFromRemarks( clsPackingentry.ModuleID, FromUserID, (String) ObjPackingEntry.getAttribute("PACKING_NOTE_NO").getObj());

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

                String PackingNo = (String) ObjPackingEntry.getAttribute("PACKING_NOTE_NO").getObj();

                List = clsFilterFabricApprovalFlow.getRemainingUsers( clsPackingentry.ModuleID, PackingNo);
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
        txtpckno.setEnabled(pStat);
        txtpckno.setEditable(false);
        txtPartycode.setEditable(pStat);
        txtstation.setEditable(pStat);
        txtsalent.setEditable(pStat);

        cmdAdd.setEnabled(pStat);
        cmdItemdelete.setEnabled(pStat);

        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);

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
        txtpckno.setText("");
        txtstation.setText("");
        txtsalent.setText("");
        lblpcdname.setText("");

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
        String lPackingNo = ObjPackingEntry.getAttribute("PACKING_NOTE_NO").getString();
        if (ObjPackingEntry.IsEditable(EITLERPGLOBAL.gCompanyID, lPackingNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (clsFilterFabricApprovalFlow.IsCreator(clsPackingentry.ModuleID, lPackingNo) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 9003, 90032)) {
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

        String lDocNo = (String) ObjPackingEntry.getAttribute("PACKING_NOTE_NO").getObj();

        if (ObjPackingEntry.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            if (ObjPackingEntry.Delete(EITLERPGLOBAL.gNewUserID)) {
                MoveLast();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while deleting. \nError is " + ObjPackingEntry.LastError);
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
        if (txtPartycode.getText().trim().length() < 1) {
            JOptionPane.showMessageDialog(null, "Please Enter Party Code...");
            return;
        }
        if (lblpcdname.getText().trim().length() < 1) {
            JOptionPane.showMessageDialog(null, "Please Enter Valid Party Code...");
            return;
        }
        if (txtstation.getText().trim().length() < 1) {
            JOptionPane.showMessageDialog(null, "Please Enter Station...");
            return;
        }
        if (txtsalent.getText().trim().length() < 1) {
            JOptionPane.showMessageDialog(null, "Please Enter SaleNote No....");
            return;
        }

        //Check the no. of items
        if (TableEntry.getRowCount() <= 0) {
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

            if (ObjPackingEntry.Insert()) {
                // MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjPackingEntry.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjPackingEntry.Update()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjPackingEntry.LastError);
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
        ObjPackingEntry.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjPackingEntry.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjPackingEntry.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjPackingEntry.MoveLast();
        DisplayData();
    }

    //Didplay data on the Screen
    private void DisplayData() {
        //=========== Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (ObjPackingEntry.getAttribute("APPROVED").getInt() == 1) {

                    lblTitle.setBackground(Color.BLUE);
                }

                if (ObjPackingEntry.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);

                }

                if (ObjPackingEntry.getAttribute("CANCELLED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);

                }
            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = clsPackingentry.ModuleID;

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

            txtpckno.setText((String) ObjPackingEntry.getAttribute("PACKING_NOTE_NO").getObj());
            txtpckdt.setText(EITLERPGLOBAL.formatDate((String) ObjPackingEntry.getAttribute("PACKING_DATE").getObj()));
            txtPartycode.setText((String) ObjPackingEntry.getAttribute("PARTY_CODE").getObj());
            lblpcdname.setText(clsPackingentry.getParyName(EITLERPGLOBAL.gCompanyID, ObjPackingEntry.getAttribute("PARTY_CODE").getString()));
            txtstation.setText((String) ObjPackingEntry.getAttribute("STATION").getObj());
            txtsalent.setText((String) ObjPackingEntry.getAttribute("SALE_NOTE_NO").getObj());

            int mdtrns = 2;
            try {
                mdtrns = Integer.parseInt(ObjPackingEntry.getAttribute("MODE_OF_TRANSPORT").getObj().toString().substring(0, 1));
            } catch (Exception e) {
            }
            for (int a = 0; a < 6; a++) {
                if ((mdtrns - 1) == a) {
                    cmbtrns.setSelectedIndex(a);
                }
            }

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjPackingEntry.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table
            for (int i = 1; i <= ObjPackingEntry.colMRItems.size(); i++) {

                clsPackingItem ObjItem = (clsPackingItem) ObjPackingEntry.colMRItems.get(Integer.toString(i));
                Object[] rowData = new Object[10];

                rowData[0] = Integer.toString(i);
                rowData[1] = (String) ObjItem.getAttribute("QUALITY_CD").getObj();
                rowData[2] = (String) ObjItem.getAttribute("PIECE_NO").getObj();
                rowData[3] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("GROSS_METER").getVal(), 3));
                rowData[4] = (String) ObjItem.getAttribute("FLAG_CD").getObj();
                rowData[5] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("NET_METER").getVal(), 3));
                rowData[6] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("WIDTH").getVal(), 3));
                rowData[7] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("SQ_METER").getVal(), 3));
                rowData[8] = Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("KGS").getVal(), 3));
                DataModelPiece.addRow(rowData);
            }

            DoNotEvaluate = false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String PackingNo = ObjPackingEntry.getAttribute("PACKING_NOTE_NO").getString();
            List = clsFilterFabricApprovalFlow.getDocumentFlow( clsPackingentry.ModuleID, PackingNo);
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
            HashMap History = clsPackingentry.getHistoryList(EITLERPGLOBAL.gCompanyID, PackingNo);
            for (int i = 1; i <= History.size(); i++) {
                clsPackingentry ObjHistory = (clsPackingentry) History.get(Integer.toString(i));
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
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjPackingEntry.setAttribute("PREFIX", SelPrefix);
        ObjPackingEntry.setAttribute("SUFFIX", SelSuffix);
        ObjPackingEntry.setAttribute("FFNO", FFNo);

        ObjPackingEntry.setAttribute("COMAPNY_ID", EITLERPGLOBAL.gCompanyID);
        ObjPackingEntry.setAttribute("PARTY_CODE", txtPartycode.getText());
        ObjPackingEntry.setAttribute("PACKING_NOTE_NO", txtpckno.getText());
        ObjPackingEntry.setAttribute("PACKING_DATE", EITLERPGLOBAL.formatDateDB(txtpckdt.getText().trim()));
        ObjPackingEntry.setAttribute("WH_CODE", "3");
        ObjPackingEntry.setAttribute("STATION", txtstation.getText());
        ObjPackingEntry.setAttribute("SALE_NOTE_NO", txtsalent.getText());
        ObjPackingEntry.setAttribute("MODE_OF_TRANSPORT", cmbtrns.getSelectedItem().toString());

        //----- Update Approval Specific Fields -----------//
        ObjPackingEntry.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjPackingEntry.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjPackingEntry.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjPackingEntry.setAttribute("REJECTED_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            ObjPackingEntry.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjPackingEntry.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjPackingEntry.setAttribute("APPROVAL_STATUS", "R");
            ObjPackingEntry.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjPackingEntry.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjPackingEntry.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjPackingEntry.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjPackingEntry.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjPackingEntry.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        //======= Set Line part ============
        ObjPackingEntry.colMRItems.clear();

        for (int i = 0; i < TableEntry.getRowCount(); i++) {
            if (!TableEntry.getValueAt(i, 1).toString().trim().equals("")) {
                clsPackingItem ObjItem = new clsPackingItem();

                ObjItem.setAttribute("QUALITY_CD", (String) TableEntry.getValueAt(i, 1));
                ObjItem.setAttribute("PIECE_NO", (String) TableEntry.getValueAt(i, 2));
                ObjItem.setAttribute("GROSS_METER", EITLERPGLOBAL.round(Double.parseDouble((String) TableEntry.getValueAt(i, 3)),2));
                ObjItem.setAttribute("FLAG_CD", (String) TableEntry.getValueAt(i, 4));
                ObjItem.setAttribute("NET_METER", EITLERPGLOBAL.round(Double.parseDouble((String) TableEntry.getValueAt(i, 5)),2));
                ObjItem.setAttribute("WIDTH", Double.parseDouble((String) TableEntry.getValueAt(i, 6)));
                ObjItem.setAttribute("SQ_METER", EITLERPGLOBAL.round(Double.parseDouble((String) TableEntry.getValueAt(i, 7)),2));
                ObjItem.setAttribute("KGS", EITLERPGLOBAL.round(Double.parseDouble((String) TableEntry.getValueAt(i, 8)),2));

                ObjPackingEntry.colMRItems.put(Integer.toString(ObjPackingEntry.colMRItems.size() + 1), ObjItem);
            }
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 9003, 90031)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 9003, 90033)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 9003, 90034)) {
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

                if (FieldName.trim().equals("PACKING_NOTE_NO")) {
                    int a = 0;
                }
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {

                    Tab1.getComponent(i).setEnabled(true);
                }

            }
        }
        //=============== Header Fields Setup Complete =================//

        //=============== Setting Table Fields ==================//
        DataModelPiece.ClearAllReadOnly();
        for (int i = 0; i < TableEntry.getColumnCount(); i++) {
            FieldName = DataModelPiece.getVariable(i);

            if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
                //Do Nothing
            } else {
                DataModelPiece.SetReadOnly(i);
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
        //ObjPackingEntry.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND MODULE_ID="+clsPackingentry.ModuleID+")");
        ObjPackingEntry.Filter(" WHERE PACKING_NOTE_NO IN (SELECT FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO FROM FILTERFABRIC.FF_TRD_PACKING_HEADER,FILTERFABRIC.FF_TRD_DOC_DATA WHERE FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND MODULE_ID=748)");
        ObjPackingEntry.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pPackingNo) {
        System.out.println(pPackingNo);
        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjPackingEntry.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjPackingEntry.Filter(" WHERE PACKING_NOTE_NO='" + pPackingNo + "'");
        ObjPackingEntry.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        String PartyCode = txtPartycode.getText();

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
                    IncludeUser = clsFilterFabricApprovalFlow.IncludeUserInApproval( clsPackingentry.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFilterFabricApprovalFlow.IncludeUserInRejection( clsPackingentry.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String ProformaNo = (String) ObjPackingEntry.getAttribute("PACKING_NOTE_NO").getObj();
            int Creator = clsFilterFabricApprovalFlow.getCreator(clsPackingentry.ModuleID, ProformaNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    private void UpdateSrNo() {
        int SrCol = DataModelPiece.getColFromVariable("SR_NO");

        for (int i = 0; i < TableEntry.getRowCount(); i++) {
            TableEntry.setValueAt(Integer.toString(i + 1), i, SrCol);

        }
    }

    private void FormatGrid() {

        Updating = true; //Stops recursion
        try {
            cmdAdd.requestFocus();

            DataModelPiece = new EITLTableModel();
            TableEntry.removeAll();
            TableEntry.setModel(DataModelPiece);
            TableColumnModel ColModel = TableEntry.getColumnModel();
            TableEntry.setAutoResizeMode(TableEntry.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);

            DataModelPiece.addColumn("Sr.");  //0 - Read Only
            DataModelPiece.addColumn("QUALITY CODE"); //1
            DataModelPiece.addColumn("PIECE NO");//2
            DataModelPiece.addColumn("GR. METER");//3
            DataModelPiece.addColumn("FLAG CD");//4
            DataModelPiece.addColumn("NET METER"); //5
            DataModelPiece.addColumn("WIDTH");  //6
            DataModelPiece.addColumn("SQ.METER"); //7
            DataModelPiece.addColumn("NET KG"); //8

            DataModelPiece.TableReadOnly(true);

        } catch (Exception e) {

        }
        Updating = false;
        //Table formatting completed  

    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    private void PreviewReport() {
        HashMap Parameters = new HashMap();
        Parameters.put("CURDATE", EITLERPGLOBAL.getCurrentDate());

        TTable objData = new TTable();

        //Populate Columns
        objData.AddColumn("WH_CODE");
        objData.AddColumn("PACKING_DATE");
        objData.AddColumn("QUALITY_CD");
        objData.AddColumn("PIECE_NO");
        objData.AddColumn("WIDTH");
        objData.AddColumn("GROSS_METER");
        objData.AddColumn("SQ_METER");
        objData.AddColumn("NET_METER");
        objData.AddColumn("KGS");
        objData.AddColumn("FLAG_CD");
        objData.AddColumn("PACKING_NOTE_NO");
        objData.AddColumn("PARTY_CODE");
        objData.AddColumn("SALE_NOTE_NO");
        objData.AddColumn("STATION");
        objData.AddColumn("MODE_OF_TRANSPORT");
        objData.AddColumn("ADDRESS1");
        objData.AddColumn("ADDRESS2");
        objData.AddColumn("PINCODE");
        objData.AddColumn("TIN_NO");
        objData.AddColumn("DESCRIPTION");
        objData.AddColumn("PARTY_NAME");
        
        try {

            String strSQL = "";
            ResultSet rsReport;
            String ExpenseID = "";
            String DocDate = "";

            //Retrieve data of main voucher
            strSQL = "SELECT H.WH_CODE,H.PACKING_NOTE_NO,H.PACKING_DATE,H.PARTY_CODE,P.PARTY_NAME,H.SALE_NOTE_NO,H.STATION,H.MODE_OF_TRANSPORT, "
                    + "P.ADDRESS1,P.ADDRESS2,P.PINCODE,COALESCE(P.TIN_NO,'') AS TIN_NO,D.QUALITY_CD,M.DESCRIPTION,D.PIECE_NO,D.GROSS_METER,D.FLAG_CD,"
                    + "D.NET_METER,D.WIDTH,D.SQ_METER,D.KGS "
                    + "FROM FILTERFABRIC.FF_TRD_PACKING_HEADER H,FILTERFABRIC.FF_TRD_PACKING_DETAIL D,"
                    + "FILTERFABRIC.FF_TRD_QUALITY_MASTER M,DINESHMILLS.D_SAL_PARTY_MASTER P "
                    + "WHERE H.PACKING_NOTE_NO=D.PACKING_NOTE_NO AND H.PACKING_DATE=D.PACKING_DATE AND "
                    + "D.QUALITY_CD=M.QUALITY_CD AND P.PARTY_CODE=H.PARTY_CODE"
                    + " AND H.PACKING_NOTE_NO='" + txtpckno.getText().trim() + "'";

            rsReport = data.getResult(strSQL);
            rsReport.first();

            if (rsReport.getRow() > 0) {

                while (!rsReport.isAfterLast()) {
                    TRow objRow = new TRow();

                    objRow.setValue("WH_CODE", rsReport.getString("WH_CODE"));
                    objRow.setValue("PACKING_NOTE_NO", rsReport.getString("PACKING_NOTE_NO"));
                    objRow.setValue("PACKING_DATE", rsReport.getString("PACKING_DATE"));
                    objRow.setValue("PARTY_CODE", rsReport.getString("PARTY_CODE"));
                    objRow.setValue("SALE_NOTE_NO", rsReport.getString("SALE_NOTE_NO"));
                    objRow.setValue("STATION", rsReport.getString("STATION"));
                    objRow.setValue("MODE_OF_TRANSPORT", rsReport.getString("MODE_OF_TRANSPORT"));
                    objRow.setValue("ADDRESS1", rsReport.getString("ADDRESS1"));
                    objRow.setValue("ADDRESS2", rsReport.getString("ADDRESS2"));
                    objRow.setValue("PINCODE", rsReport.getString("PINCODE"));
                    objRow.setValue("TIN_NO", rsReport.getString("TIN_NO"));
                    objRow.setValue("QUALITY_CD", rsReport.getString("QUALITY_CD"));
                    objRow.setValue("DESCRIPTION", rsReport.getString("DESCRIPTION"));
                    objRow.setValue("PIECE_NO", rsReport.getString("PIECE_NO"));
                    objRow.setValue("WIDTH", Double.toString(rsReport.getDouble("WIDTH")));
                    objRow.setValue("GROSS_METER", Double.toString(rsReport.getDouble("GROSS_METER")));
                    objRow.setValue("SQ_METER", Double.toString(rsReport.getDouble("SQ_METER")));
                    objRow.setValue("NET_METER", Double.toString(rsReport.getDouble("NET_METER")));
                    objRow.setValue("KGS", Double.toString(rsReport.getDouble("KGS")));
                    objRow.setValue("FLAG_CD", rsReport.getString("FLAG_CD"));
                    objRow.setValue("PARTY_NAME", rsReport.getString("PARTY_NAME"));

                    objData.AddRow(objRow);
                    rsReport.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptPackingSlip.rpt", Parameters, objData);
        objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptPackingSlip.rpt", Parameters, objData);

    }

}
