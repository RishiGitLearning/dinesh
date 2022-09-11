/*
 * 
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP.FeltSales.FeltWarpingBeamOrder;

/**
 *
 * @author Dharmendra
 */
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
public class frmWarpingBeamOrderHDS extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsWarpingBeamOrderHDS Obj;
    private clsWarpingBeamOrderHDS Obj;
    private EITLERP.FeltSales.common.FeltInvCalc inv_calculation;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;

    private EITLTableModel DataModelDesc, DataModelDesc1;

    private EITLTableModel DataModelA, DataModelH;
    private EITLTableModel DataModelHS;

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

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
    public frmWarpingBeamOrderHDS() {
        //this.requestFocus();

        System.gc();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        if (scrwidth > 800) {
            scrwidth = 800;
        }

        setSize(scrwidth, scrheight - 50);
        setDefaultLookAndFeelDecorated(true);
        initComponents();
        file1.setVisible(false);

//        JFXPanel dataPaneel = new JFXPanel();
//
//        ScrollPane sp = new ScrollPane();
//
//        details.addAll(new PieChart.Data("Warp", 100), new PieChart.Data("Woven", 10.16)
//        );
//        pieChart = new PieChart();
//        pieChart.setData(details);
//        pieChart.setLegendSide(Side.TOP);
//        pieChart.setLabelsVisible(true);
//        pieChart.setMaxSize(20, 15);
//        sp.setContent(pieChart);
//
//        Scene scene = new Scene(sp, 250, 800);
//        dataPaneel.setScene(scene);
//
//        chart.add(dataPaneel);
//        chart.setVisible(true);
//        //chart1.add(dataPaneel);
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

        txtbeam.requestFocusInWindow();
        df = new DecimalFormat("0.00");
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        //JOptionPane.showMessageDialog(null, df.format(10));

        txtbeam.setFocusTraversalKeysEnabled(false);
        DecimalFormat decimalFormat = new DecimalFormat("0");
        NumberFormatter ObjFormater = new NumberFormatter(decimalFormat);

        ObjFormater.setAllowsInvalid(false);
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        NumberFormatter ObjFormater1 = new NumberFormatter(decimalFormat1);

        ObjFormater1.setAllowsInvalid(false);

        txtbeam.setFormatterFactory(new DefaultFormatterFactory(ObjFormater));

        txtloom.setFocusTraversalKeysEnabled(false);
        txtloom.setFormatterFactory(new DefaultFormatterFactory(ObjFormater));

        txtreedspace.setFocusTraversalKeysEnabled(false);
        txtreedspace.setFormatterFactory(new DefaultFormatterFactory(ObjFormater1));

        txtend10cm.setFocusTraversalKeysEnabled(false);
        txtend10cm.setFormatterFactory(new DefaultFormatterFactory(ObjFormater1));

        txtactlwarpreali.setFocusTraversalKeysEnabled(false);
        txtactlwarpreali.setFormatterFactory(new DefaultFormatterFactory(ObjFormater1));

        txtwarplen.setFocusTraversalKeysEnabled(false);
        txtwarplen.setFormatterFactory(new DefaultFormatterFactory(ObjFormater1));

        Formula.getColumnModel().getColumn(0).setMinWidth(150);
        Formula.getColumnModel().getColumn(0).setMaxWidth(150);

        GenerateCombos();

        Obj = new clsWarpingBeamOrderHDS();

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

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsWarpingBeamOrderHDS.ModuleID + "");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsWarpingBeamOrderHDS.ModuleID + "");
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
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        Formula = new javax.swing.JTable();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableH = new javax.swing.JTable();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        TableAmend = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        DOC_NO = new javax.swing.JTextField();
        txtselewarpdtl = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtwarptex = new javax.swing.JTextField();
        txtwarpdtl = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtwarpendtot = new javax.swing.JTextField();
        txtreedcnt = new javax.swing.JTextField();
        txtwovenlen = new javax.swing.JTextField();
        txttotpclen = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtbeam = new javax.swing.JFormattedTextField();
        txtloom = new javax.swing.JFormattedTextField();
        txtreedspace = new javax.swing.JFormattedTextField();
        txtend10cm = new javax.swing.JFormattedTextField();
        txtactlwarpreali = new javax.swing.JFormattedTextField();
        txtwarplen = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtfabrealper = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
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
        lblTitle.setText("Felt Warping Beam Order [HDS]");
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
        jScrollPane1.setBounds(0, 50, 740, 240);

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
        jButton1.setBounds(640, 300, 90, 29);

        bexcel.setText("Excel");
        bexcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bexcelActionPerformed(evt);
            }
        });
        Tab1.add(bexcel);
        bexcel.setBounds(10, 300, 140, 40);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 255));
        jLabel4.setText("Press F1 For Piece Selection");
        Tab1.add(jLabel4);
        jLabel4.setBounds(10, 14, 270, 30);

        jTabbedPane1.addTab("Warping Beam Order Entry [HDS]", Tab1);

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

        Formula.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Woven Length", "Warp Length-Total Theo PC Length Mtr"},
                {"Warp End Total", "(Reed Space * End/10 CM)*10"},
                {"Total Picks", "Theo PC Length * Theo Pick/10 cm"},
                {"Expected Grev Sq Mtr", "Reed Space on Loom * Theo PC Length Mtr"},
                {"Fabric Realisation Per%", "(sum of Expected_Grev_sq_mtr)/(Reed Space*Actual Warp Length)"},
                {"Reed Space On Loom", "Reed Space +0.10 Allowed"},
                {"Loom No", "Allow only 87 & 90"},
                {"Reed Space", "Not More than 13.00"},
                {"Max Length", "Series-SubSeries wise Total and Find out Maximum from Series as Maximum Length"}
            },
            new String [] {
                "Field", "Formula"
            }
        ));
        jScrollPane3.setViewportView(Formula);

        jTabbedPane2.addTab("Formula's", jScrollPane3);

        jTabbedPane1.addTab("Formula", jTabbedPane2);

        TableH.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(TableH);

        jTabbedPane3.addTab("Amendment", jScrollPane4);

        jTabbedPane1.addTab("Amendment", jTabbedPane3);

        TableAmend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(TableAmend);

        jTabbedPane4.addTab("Amendment Effects", jScrollPane5);

        jTabbedPane1.addTab("Amendment Effect", jTabbedPane4);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(5, 260, 750, 390);

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

        txtselewarpdtl.setEditable(false);
        txtselewarpdtl.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtselewarpdtl.setEnabled(false);
        txtselewarpdtl = new JTextFieldHint(new JTextField(),"Search by F1");
        txtselewarpdtl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtselewarpdtlFocusLost(evt);
            }
        });
        txtselewarpdtl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtselewarpdtlKeyPressed(evt);
            }
        });
        getContentPane().add(txtselewarpdtl);
        txtselewarpdtl.setBounds(350, 100, 100, 30);

        jLabel7.setText("Beam No.");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(25, 105, 70, 20);

        jLabel8.setText("Loom No.");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(25, 135, 70, 20);

        jLabel9.setText("Reed Space");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(280, 80, 70, 20);

        jLabel10.setText("Warp Detail");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(250, 170, 100, 20);

        jLabel11.setText("Warp Tex");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(280, 140, 70, 20);

        txtwarptex.setEditable(false);
        txtwarptex.setForeground(new java.awt.Color(0, 0, 255));
        txtwarptex.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtwarptex.setFocusable(false);
        getContentPane().add(txtwarptex);
        txtwarptex.setBounds(350, 130, 110, 30);

        txtwarpdtl.setEditable(false);
        txtwarpdtl.setForeground(new java.awt.Color(0, 0, 255));
        txtwarpdtl.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtwarpdtl.setFocusable(false);
        getContentPane().add(txtwarpdtl);
        txtwarpdtl.setBounds(350, 160, 160, 30);

        jLabel12.setText("Actual Warp Length");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(480, 75, 140, 20);

        jLabel13.setText(" Woven Length");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(490, 110, 130, 20);

        jLabel14.setText("Total Theo PC Length");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(460, 135, 160, 20);

        jLabel15.setText("Woven Length");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(510, 165, 110, 20);

        jLabel16.setText("Warp Ends Total");
        getContentPane().add(jLabel16);
        jLabel16.setBounds(500, 195, 120, 20);

        jLabel17.setText("Reed Count");
        getContentPane().add(jLabel17);
        jLabel17.setBounds(530, 225, 90, 20);

        txtwarpendtot.setEditable(false);
        txtwarpendtot.setForeground(new java.awt.Color(0, 0, 255));
        txtwarpendtot.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtwarpendtot.setFocusable(false);
        getContentPane().add(txtwarpendtot);
        txtwarpendtot.setBounds(620, 190, 110, 30);

        txtreedcnt.setEditable(false);
        txtreedcnt.setForeground(new java.awt.Color(0, 0, 255));
        txtreedcnt.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtreedcnt.setFocusable(false);
        getContentPane().add(txtreedcnt);
        txtreedcnt.setBounds(620, 220, 110, 30);

        txtwovenlen.setEditable(false);
        txtwovenlen.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtwovenlen.setForeground(new java.awt.Color(0, 0, 255));
        txtwovenlen.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtwovenlen.setFocusable(false);
        getContentPane().add(txtwovenlen);
        txtwovenlen.setBounds(620, 160, 110, 30);

        txttotpclen.setEditable(false);
        txttotpclen.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txttotpclen.setForeground(new java.awt.Color(0, 0, 255));
        txttotpclen.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txttotpclen.setFocusable(false);
        getContentPane().add(txttotpclen);
        txttotpclen.setBounds(620, 130, 110, 30);

        jLabel18.setText("Ends/10 cm");
        getContentPane().add(jLabel18);
        jLabel18.setBounds(250, 200, 100, 20);

        jLabel20.setText("Select Warp Detail");
        getContentPane().add(jLabel20);
        jLabel20.setBounds(210, 110, 140, 20);

        txtbeam.setForeground(new java.awt.Color(0, 0, 255));
        txtbeam.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtbeam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtbeamFocusLost(evt);
            }
        });
        txtbeam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtbeamKeyPressed(evt);
            }
        });
        getContentPane().add(txtbeam);
        txtbeam.setBounds(100, 100, 110, 30);

        txtloom.setForeground(new java.awt.Color(0, 0, 255));
        txtloom.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtloom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtloomKeyPressed(evt);
            }
        });
        getContentPane().add(txtloom);
        txtloom.setBounds(100, 130, 110, 30);

        txtreedspace.setForeground(new java.awt.Color(0, 0, 255));
        txtreedspace.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtreedspace.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtreedspaceFocusLost(evt);
            }
        });
        txtreedspace.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtreedspaceKeyPressed(evt);
            }
        });
        getContentPane().add(txtreedspace);
        txtreedspace.setBounds(350, 70, 110, 30);

        txtend10cm.setEditable(false);
        txtend10cm.setForeground(new java.awt.Color(0, 0, 255));
        txtend10cm.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtend10cm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtend10cmFocusLost(evt);
            }
        });
        txtend10cm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtend10cmKeyPressed(evt);
            }
        });
        getContentPane().add(txtend10cm);
        txtend10cm.setBounds(350, 190, 110, 30);

        txtactlwarpreali.setForeground(new java.awt.Color(0, 0, 255));
        txtactlwarpreali.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtactlwarpreali.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtactlwarprealiFocusLost(evt);
            }
        });
        txtactlwarpreali.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtactlwarprealiKeyPressed(evt);
            }
        });
        getContentPane().add(txtactlwarpreali);
        txtactlwarpreali.setBounds(620, 70, 110, 30);

        txtwarplen.setForeground(new java.awt.Color(0, 0, 255));
        txtwarplen.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        txtwarplen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtwarplenFocusLost(evt);
            }
        });
        txtwarplen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtwarplenKeyPressed(evt);
            }
        });
        getContentPane().add(txtwarplen);
        txtwarplen.setBounds(620, 100, 110, 30);

        jLabel2.setText("Remaining");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(530, 154, 80, 20);

        jLabel21.setText("Percentage");
        getContentPane().add(jLabel21);
        jLabel21.setBounds(10, 200, 90, 20);

        jLabel22.setText("Fabric Realsation");
        getContentPane().add(jLabel22);
        jLabel22.setBounds(0, 190, 130, 20);

        txtfabrealper.setEditable(false);
        txtfabrealper.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtfabrealper.setForeground(new java.awt.Color(0, 0, 255));
        txtfabrealper.setDisabledTextColor(new java.awt.Color(0, 0, 255));
        getContentPane().add(txtfabrealper);
        txtfabrealper.setBounds(130, 190, 120, 30);

        jLabel3.setText("Achivable");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(520, 100, 80, 17);
        getContentPane().add(file1);
        file1.setBounds(730, 40, 140, 340);
    }// </editor-fold>//GEN-END:initComponents

    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased

        String PieceNo = (String) TableDesc.getValueAt(TableDesc.getSelectedRow(), 1);
        ResultSet trs = null;
        String sql = "";
        try {
            if ((EditMode == 1 || EditMode == 2) && clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
                if (TableDesc.getSelectedColumn() == 2) {
                    try {
                        //trs = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE SUBSTRING(PR_PIECE_NO,1,5) = '" + PieceNo.substring(0, 5) + "' AND PR_PIECE_STAGE='PLANNING' AND (PR_WARP_DATE='0000-00-00' OR PR_WARP_DATE IS NULL) AND PR_PIECE_NO NOT IN (SELECT LEFT(PIECE_NO,5) FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL)");
                        sql = "SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER "
                                + "WHERE (WIP_PRODUCT_CODE LIKE '71%' AND WIP_PRODUCT_CODE NOT LIKE '729%' AND "
                                + " WIP_EXT_PIECE_NO = '" + PieceNo + "' AND COALESCE(WIP_REJECTED_FLAG,0)=0 AND "
                                + "WIP_PIECE_STAGE='PLANNING' AND (WIP_WARP_DATE='0000-00-00' OR WIP_WARP_DATE IS NULL) "
                                + "AND (WIP_EXT_PIECE_NO NOT IN (SELECT LEFT(PIECE_NO,5) FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL)) "
                                + "OR WIP_EXT_PIECE_NO IN (SELECT DISTINCT LEFT(D.PIECE_NO,5) FROM ("
                                + "SELECT  * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL "
                                + "WHERE PIECE_NO='" + PieceNo + "' AND BEAM_NO IN (SELECT DISTINCT BEAM_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL_AMEND)) AS D "
                                + "LEFT JOIN (SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL_AMEND) AS  A "
                                + "ON D.BEAM_NO=A.BEAM_NO AND D.PIECE_NO=A.PIECE_NO "
                                + "WHERE A.PIECE_NO IS NULL))";
                        trs = data.getResult(sql);
                    } catch (Exception a) {
                        JOptionPane.showMessageDialog(null, "Invalid Piece No....");
                        cmdItemdeleteActionPerformed(null);
                    }
                    if (trs.getRow() > 0) {
                        TableDesc.setValueAt(trs.getString("WIP_PARTY_CODE"), TableDesc.getSelectedRow(), 2);
                        TableDesc.setValueAt(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, trs.getString("WIP_PARTY_CODE")), TableDesc.getSelectedRow(), 3);
                        TableDesc.setValueAt(trs.getString("WIP_PRODUCT_CODE"), TableDesc.getSelectedRow(), 4);
                        TableDesc.setValueAt(trs.getString("WIP_GROUP"), TableDesc.getSelectedRow(), 5);
                        TableDesc.setValueAt(trs.getString("WIP_STYLE"), TableDesc.getSelectedRow(), 6);
                        TableDesc.setValueAt(df.format(trs.getDouble("WIP_LENGTH")), TableDesc.getSelectedRow(), 7);
                        TableDesc.setValueAt(df.format(trs.getDouble("WIP_WIDTH")), TableDesc.getSelectedRow(), 8);
                        TableDesc.setValueAt(trs.getString("WIP_GSM"), TableDesc.getSelectedRow(), 9);
                        if (PieceNo.contains("B") || PieceNo.contains("b")) {
                            TableDesc.setValueAt("0.00", TableDesc.getSelectedRow(), 10);
                        } else {
                            TableDesc.setValueAt(df.format(trs.getDouble("WIP_THORITICAL_WEIGHT")), TableDesc.getSelectedRow(), 10);
                        }

                    } else {
                        if (PieceNo.contains("A") || PieceNo.contains("a")) {
                            trs = data.getResult("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL WHERE PIECE_NO = '" + PieceNo + "'   AND INDICATOR!='DELETE' ");
                            if (trs.getRow() > 0) {
                                JOptionPane.showMessageDialog(null, "Piece No. is Already Warped...");
                                cmdItemdeleteActionPerformed(null);
                            } else {
                                sql = "SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL "
                                        + "WHERE PIECE_NO = '" + PieceNo + "'   AND INDICATOR!='DELETE'  "
                                        + "UNION ALL "
                                        + "SELECT PROD_PIECE_NO FROM PRODUCTION.FELT_PROD_DATA  "
                                        + "WHERE PROD_PIECE_NO LIKE '" + PieceNo.substring(0, 5) + "%' AND "
                                        + "PROD_DEPT NOT IN ('FELT FINISHING','FINISHING','NEEDLING') AND (PROD_PIECE_NO like '%A%' OR PROD_PIECE_NO like '%a%')";
                                trs = data.getResult(sql);
                                if (trs.getRow() > 0) {
                                    JOptionPane.showMessageDialog(null, "Piece No. is Already Warped...");
                                    cmdItemdeleteActionPerformed(null);
                                } else {
                                    sql = "SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL "
                                            + "WHERE PIECE_NO = '" + PieceNo + "'   AND INDICATOR!='DELETE'  "
                                            + "UNION ALL "
                                            + "SELECT PROD_PIECE_NO FROM PRODUCTION.FELT_PROD_DATA  "
                                            + "WHERE PROD_PIECE_NO LIKE '" + PieceNo.substring(0, 5) + "%' AND "
                                            + "PROD_DEPT NOT IN ('FELT FINISHING','FINISHING','NEEDLING') AND (PROD_PIECE_NO like '%B%' OR PROD_PIECE_NO like '%b%')";
                                    trs = data.getResult(sql);
                                    if (trs.getRow() > 0 && (trs.getString("PIECE_NO").contains("B") || trs.getString(1).contains("b"))) {
                                        trs = data.getResult("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO = '" + PieceNo + "' ");
                                        TableDesc.setValueAt(trs.getString("WIP_PARTY_CODE"), TableDesc.getSelectedRow(), 2);
                                        TableDesc.setValueAt(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, trs.getString("WIP_PARTY_CODE")), TableDesc.getSelectedRow(), 3);
                                        TableDesc.setValueAt(trs.getString("WIP_PRODUCT_CODE"), TableDesc.getSelectedRow(), 4);
                                        TableDesc.setValueAt(trs.getString("WIP_GROUP"), TableDesc.getSelectedRow(), 5);
                                        TableDesc.setValueAt(trs.getString("WIP_STYLE"), TableDesc.getSelectedRow(), 6);
                                        TableDesc.setValueAt(df.format(trs.getDouble("WIP_LENGTH")), TableDesc.getSelectedRow(), 7);
                                        TableDesc.setValueAt(df.format(trs.getDouble("WIP_WIDTH")), TableDesc.getSelectedRow(), 8);
                                        TableDesc.setValueAt(trs.getString("WIP_GSM"), TableDesc.getSelectedRow(), 9);
                                        if (PieceNo.contains("B") || PieceNo.contains("b")) {
                                            TableDesc.setValueAt("0.00", TableDesc.getSelectedRow(), 10);
                                        } else {
                                            TableDesc.setValueAt(df.format(trs.getDouble("WIP_THORITICAL_WEIGHT")), TableDesc.getSelectedRow(), 10);
                                        }

                                    } else {
                                        JOptionPane.showMessageDialog(null, "Piece No. is Invalid...");
                                        cmdItemdeleteActionPerformed(null);
                                    }
                                }
                            }
                        } else if (PieceNo.contains("B") || PieceNo.contains("b")) {
                            trs = data.getResult("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL WHERE PIECE_NO = '" + PieceNo + "'   AND INDICATOR!='DELETE' ");
                            if (trs.getRow() > 0) {
                                JOptionPane.showMessageDialog(null, "Piece No. is Already Warped...");
                                cmdItemdeleteActionPerformed(null);
                            } else {
                                sql = "SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL "
                                        + "WHERE PIECE_NO = '" + PieceNo + "'   AND INDICATOR!='DELETE'   "
                                        + "UNION ALL "
                                        + "SELECT PROD_PIECE_NO FROM PRODUCTION.FELT_PROD_DATA  "
                                        + "WHERE PROD_PIECE_NO LIKE '" + PieceNo.substring(0, 5) + "%' AND "
                                        + "PROD_DEPT NOT IN ('FELT FINISHING','FINISHING','NEEDLING') AND (PROD_PIECE_NO like '%B%' OR PROD_PIECE_NO like '%b%')";
                                trs = data.getResult(sql);
                                if (trs.getRow() > 0) {
                                    JOptionPane.showMessageDialog(null, "Piece No. is Already Warped...");
                                    cmdItemdeleteActionPerformed(null);
                                } else {
                                    sql = "SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL "
                                            + "WHERE PIECE_NO = '" + PieceNo + "'   AND INDICATOR!='DELETE'  "
                                            + "UNION ALL "
                                            + "SELECT PROD_PIECE_NO FROM PRODUCTION.FELT_PROD_DATA  "
                                            + "WHERE PROD_PIECE_NO LIKE '" + PieceNo.substring(0, 5) + "%' AND "
                                            + "PROD_DEPT NOT IN ('FELT FINISHING','FINISHING','NEEDLING') AND (PROD_PIECE_NO like '%a%' OR PROD_PIECE_NO like '%A%')";
                                    trs = data.getResult(sql);
                                    if (trs.getRow() > 0 && (trs.getString("PIECE_NO").contains("A") || trs.getString(1).contains("a"))) {
                                        trs = data.getResult("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO = '" + PieceNo + "' ");
                                        TableDesc.setValueAt(trs.getString("WIP_PARTY_CODE"), TableDesc.getSelectedRow(), 2);
                                        TableDesc.setValueAt(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, trs.getString("WIP_PARTY_CODE")), TableDesc.getSelectedRow(), 3);
                                        TableDesc.setValueAt(trs.getString("WIP_PRODUCT_CODE"), TableDesc.getSelectedRow(), 4);
                                        TableDesc.setValueAt(trs.getString("WIP_GROUP"), TableDesc.getSelectedRow(), 5);
                                        TableDesc.setValueAt(trs.getString("WIP_STYLE"), TableDesc.getSelectedRow(), 6);
                                        TableDesc.setValueAt(df.format(trs.getDouble("WIP_LENGTH")), TableDesc.getSelectedRow(), 7);
                                        TableDesc.setValueAt(df.format(trs.getDouble("WIP_WIDTH")), TableDesc.getSelectedRow(), 8);
                                        TableDesc.setValueAt(trs.getString("WIP_GSM"), TableDesc.getSelectedRow(), 9);
                                        if (PieceNo.contains("B") || PieceNo.contains("b")) {
                                            TableDesc.setValueAt("0.00", TableDesc.getSelectedRow(), 10);
                                        } else {
                                            TableDesc.setValueAt(df.format(trs.getDouble("WIP_THORITICAL_WEIGHT")), TableDesc.getSelectedRow(), 10);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Piece No. is Invalid...");
                                        cmdItemdeleteActionPerformed(null);
                                    }
                                }
                            }
                        } else {
                            trs = data.getResult("SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO = '" + PieceNo + "' AND COALESCE(WIP_REJECTED_FLAG,0)=0 ");
                            if (trs.getRow() > 0) {
                                JOptionPane.showMessageDialog(null, "Piece is alredy in " + trs.getString("WIP_PIECE_STAGE") + " Stage...");
                                cmdItemdeleteActionPerformed(null);
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Piece No....");
                                cmdItemdeleteActionPerformed(null);
                            }
                        }
                    }
                    TableDesc.changeSelection(TableDesc.getSelectedRow(), 11, false, false);
                }
            }
            Double mtpclen, mthep10, mreedspc;
            try {
                mtpclen = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 13).toString());
            } catch (Exception a) {
                mtpclen = 0.0;
            }
            try {
                mthep10 = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 18).toString());
            } catch (Exception b) {
                mthep10 = 0.0;
            }
            try {
                mreedspc = Double.parseDouble(TableDesc.getValueAt(TableDesc.getSelectedRow(), 12).toString());
            } catch (Exception c) {
                mreedspc = 0.0;
            }
            if (mreedspc > (Double.parseDouble(txtreedspace.getText()) + 0.10)) {
                JOptionPane.showMessageDialog(null, "Reed Space on Loom is not More than Reed Space....");
                cmdItemdeleteActionPerformed(null);
            }
            TableDesc.setValueAt(df.format(EITLERPGLOBAL.round((mtpclen * mthep10) * 10, 2)), TableDesc.getSelectedRow(), 19);
            TableDesc.setValueAt(df.format(EITLERPGLOBAL.round((mtpclen * mreedspc), 2)), TableDesc.getSelectedRow(), 20);

            UpdateWovenLength();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_TableDescKeyReleased

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        if (txtwarplen.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Warp Length First.");
            return;
        } else {

            Updating = true;
            Object[] rowData = new Object[40];
            rowData[0] = Integer.toString(TableDesc.getRowCount() + 1);
            rowData[1] = "";
            DataModelDesc.addRow(rowData);
            Updating = false;
            UpdateSrNo();
            TableDesc.changeSelection(TableDesc.getRowCount() - 1, 1, false, false);
            TableDesc.requestFocus();
        }

    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdItemdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdItemdeleteActionPerformed
        if (TableDesc.getRowCount() > 0) {
            DataModelDesc.removeRow(TableDesc.getSelectedRow());
            UpdateSrNo();
            UpdateWovenLength();
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
        String DocNo = DOC_NO.getText();

        SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsWarpingBeamOrderHDS.ModuleID, DocNo)) {
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

    private void txtselewarpdtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtselewarpdtlFocusLost
        txtselewarpdtl.setText("");
        txtactlwarpreali.requestFocus();
    }//GEN-LAST:event_txtselewarpdtlFocusLost

    private void txtselewarpdtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtselewarpdtlKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT * FROM PRODUCTION.FELT_WARPING_SELECTION_DATA WHERE TYPE IN ('SDF','HDS')";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {

                String[] str = aList.ReturnVal.split("--");
                txtwarptex.setText(str[0]);
                txtwarpdtl.setText(str[1]);
                txtreedcnt.setText(str[2]);
                txtend10cm.setText(str[3]);

                txtend10cmFocusLost(null);
                //txtselewarpdtl.setText("");
            }
        }
    }//GEN-LAST:event_txtselewarpdtlKeyPressed

    private void txtreedspaceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtreedspaceFocusLost
        // TODO add your handling code here:
        double mrspc, mend10;
        try {
            mrspc = Double.parseDouble(txtreedspace.getText());
        } catch (Exception a) {
            mrspc = 0.0;
        }
        try {
            mend10 = Double.parseDouble(txtend10cm.getText());
        } catch (Exception a) {
            mend10 = 0.0;
        }
        txtwarpendtot.setText(String.valueOf(EITLERPGLOBAL.round((mrspc * mend10) * 10, 2)));
    }//GEN-LAST:event_txtreedspaceFocusLost

    private void txtbeamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbeamKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 9) {
            if (EditMode == EITLERPGLOBAL.ADD) {
                if (chkbeamno()) {
                    txtloom.requestFocus();
                }
            } else {
                txtloom.requestFocus();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (EditMode == EITLERPGLOBAL.ADD) {
                if (chkbeamno()) {
                    txtloom.requestFocus();
                }
            } else {
                txtloom.requestFocus();
            }
        }
    }//GEN-LAST:event_txtbeamKeyPressed

    private void txtloomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtloomKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE AS LOOM FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='LOOM' AND PARA_CODE IN ('90','87')";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {

                txtloom.setText(aList.ReturnVal);
            }
        }
        if (evt.getKeyCode() == 9) {
            if (txtloom.getText().equalsIgnoreCase("90") || txtloom.getText().equalsIgnoreCase("87")) {
                txtreedspace.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Loom for HDS...");
                txtloom.setText("");
                txtloom.requestFocus();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtloom.getText().equalsIgnoreCase("90") || txtloom.getText().equalsIgnoreCase("87")) {
                txtreedspace.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Loom for HDS...");
                txtloom.setText("");
                txtloom.requestFocus();
            }
        }
    }//GEN-LAST:event_txtloomKeyPressed

    private void txtreedspaceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtreedspaceKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 9) {
            if (Double.parseDouble(txtreedspace.getText()) > 13) {
                JOptionPane.showMessageDialog(null, "Reed Space not More Than 13...");
                txtreedspace.setText("");
                txtreedspace.requestFocus();
            } else {
                txtselewarpdtl.requestFocus();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (Double.parseDouble(txtreedspace.getText()) > 13) {
                JOptionPane.showMessageDialog(null, "Reed Space not More Than 13...");
                txtreedspace.setText("");
                txtreedspace.requestFocus();
            } else {
                txtselewarpdtl.requestFocus();
            }
        }
    }//GEN-LAST:event_txtreedspaceKeyPressed

    private void txtend10cmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtend10cmFocusLost
        // TODO add your handling code here:
        double mrspc, mend10;
        try {
            mrspc = Double.parseDouble(txtreedspace.getText());
        } catch (Exception a) {
            mrspc = 0.0;
        }
        try {
            mend10 = Double.parseDouble(txtend10cm.getText());
        } catch (Exception a) {
            mend10 = 0.0;
        }
        txtwarpendtot.setText(String.valueOf(EITLERPGLOBAL.round((mrspc * mend10) * 10, 2)));
    }//GEN-LAST:event_txtend10cmFocusLost

    private void txtend10cmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtend10cmKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 9) {
            txtactlwarpreali.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtactlwarpreali.requestFocus();
        }
    }//GEN-LAST:event_txtend10cmKeyPressed

    private void txtactlwarprealiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtactlwarprealiFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtactlwarprealiFocusLost

    private void txtactlwarprealiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtactlwarprealiKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 9) {
            txtwarplen.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtwarplen.requestFocus();
        }
    }//GEN-LAST:event_txtactlwarprealiKeyPressed

    private void txtwarplenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtwarplenFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtwarplenFocusLost

    private void txtwarplenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtwarplenKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 9) {
            cmdAdd.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmdAdd.requestFocus();
        }
    }//GEN-LAST:event_txtwarplenKeyPressed

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

    private void txtbeamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtbeamFocusLost
        // TODO add your handling code here:
        if (EditMode == EITLERPGLOBAL.ADD) {
            if (chkbeamno()) {
                txtloom.requestFocus();
            }
        } else {
            txtloom.requestFocus();
        }
    }//GEN-LAST:event_txtbeamFocusLost

    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            if ((EditMode == 1) || (EditMode == 2 && clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID))) {
                if (TableDesc.getSelectedColumn() == 1) {
                    LOV aList = new LOV();
                    aList.SQL = "SELECT WIP_EXT_PIECE_NO AS PIECE_NO,WIP_PIECE_NO AS PIECE,DATE_FORMAT(WIP_ORDER_DATE,'%d/%m/%Y') AS ORDER_DATE,WIP_UPN AS UPN,WIP_MACHINE_NO AS MACHINE_NO,"
                            + " WIP_POSITION_NO AS POSITION_NO,WIP_PARTY_CODE AS PARTY_CODE,M.PARTY_NAME,WIP_GROUP AS PRODUCT_GROUP,WIP_PRODUCT_CODE AS PRODUCT_CODE,"
                            + " WIP_STYLE AS STYLE,"
                            + "COALESCE(AMEND_WIP.PIECE_AMEND_NO,'') AS PENDING_WIP_AMEND "
                            + ",CASE WHEN PARTY_LOCK=1 THEN 'LOCKED' ELSE '' END AS PARTY_LOCK "
                            + ",CASE WHEN POSITION_LOCK_IND=1 THEN 'LOCKED' ELSE '' END AS POSITION_LOCK, "
                            + "WIP_OC_MONTHYEAR AS OC_MONTH  "
                            + " FROM PRODUCTION.FELT_WIP_PIECE_REGISTER R "
                            + " LEFT JOIN PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                            + " ON WIP_PARTY_CODE=MM_PARTY_CODE AND WIP_MACHINE_NO=MM_MACHINE_NO AND WIP_POSITION_NO=MM_MACHINE_POSITION "
                            + "LEFT JOIN (SELECT PD.PIECE_NO,PH.PIECE_AMEND_NO,PH.APPROVED  FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP PH, "
                            + "PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP   PD "
                            + "WHERE PH.PIECE_AMEND_NO=PD.PIECE_AMEND_NO  AND COALESCE(PH.APPROVED,0)=0  AND COALESCE(PH.CANCELED,0)!=1) AS AMEND_WIP "
                            + "ON WIP_PIECE_NO=AMEND_WIP.PIECE_NO "
                            + " LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER M "
                            + " ON R.WIP_PARTY_CODE=M.PARTY_CODE "
                            + " WHERE WIP_PRODUCT_CODE LIKE '71%' AND WIP_PRODUCT_CODE NOT LIKE '729%' AND "
                            + " WIP_PIECE_STAGE='PLANNING' AND WIP_PRIORITY_HOLD_CAN_FLAG=0 AND COALESCE(WIP_REJECTED_FLAG,0)=0 AND "
                            + " WIP_EXT_PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL WHERE COALESCE(INDICATOR,'')!='DELETE' ) "
                            + " ORDER BY ORDER_DATE,PIECE_NO ";
                    aList.ReturnCol = 1;
                    aList.ShowReturnCol = true;
                    aList.DefaultSearchOn = 1;

                    if (aList.ShowLOV()) {
                        if (!data.getStringValueFromDB("SELECT PD.PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_AMENDMENT_HEADER_WIP PH, "
                                + "PRODUCTION.FELT_SALES_PIECE_AMENDMENT_DETAIL_WIP   PD "
                                + "WHERE COALESCE(PH.APPROVED,0)!=1 AND COALESCE(PH.CANCELED,0)!=1 AND "
                                + "PH.PIECE_AMEND_NO=PD.PIECE_AMEND_NO AND LEFT(PD.PIECE_NO,5)='" + aList.ReturnVal.substring(0, 5) + "' ").equalsIgnoreCase("")) {
                            JOptionPane.showMessageDialog(this, "Piece is Under Approval in WIP Piece Amend", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else if (data.getIntValueFromDB("SELECT PARTY_LOCK FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=(SELECT WIP_PARTY_CODE FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + aList.ReturnVal + "')") == 1) {
                            JOptionPane.showMessageDialog(this, "Party is Locked...", "ERROR", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else if (data.getIntValueFromDB("SELECT POSITION_LOCK_IND FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE CONCAT(TRIM(MM_PARTY_CODE),TRIM(MM_MACHINE_NO),TRIM(MM_MACHINE_POSITION))=(SELECT CONCAT(TRIM(WIP_PARTY_CODE),TRIM(WIP_MACHINE_NO),TRIM(WIP_POSITION_NO)) FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + aList.ReturnVal + "')") == 1) {
                            JOptionPane.showMessageDialog(this, "Position is Locked...", "ERROR", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else if (data.getStringValueFromDB("SELECT COALESCE(WIP_OC_MONTHYEAR,'') FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + aList.ReturnVal + "'").equalsIgnoreCase("")) {
                            JOptionPane.showMessageDialog(this, "OC Month is compulsory.\n Please contact with Sales Person", "ERROR", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            try {
                                TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 1);
                                String PieceNo = aList.ReturnVal;
                                String sql = "SELECT * FROM PRODUCTION.FELT_WIP_PIECE_REGISTER "
                                        + "WHERE WIP_EXT_PIECE_NO = '" + PieceNo + "' ";
                                System.out.println("Piece Selection query:" + sql);
                                ResultSet trs = data.getResult(sql);
                                if (trs.getRow() > 0) {
                                    TableDesc.setValueAt(trs.getString("WIP_PARTY_CODE"), TableDesc.getSelectedRow(), 2);
                                    TableDesc.setValueAt(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, trs.getString("WIP_PARTY_CODE")), TableDesc.getSelectedRow(), 3);
                                    TableDesc.setValueAt(trs.getString("WIP_PRODUCT_CODE"), TableDesc.getSelectedRow(), 4);
                                    TableDesc.setValueAt(trs.getString("WIP_GROUP"), TableDesc.getSelectedRow(), 5);
                                    TableDesc.setValueAt(trs.getString("WIP_STYLE"), TableDesc.getSelectedRow(), 6);
                                    TableDesc.setValueAt(df.format(trs.getDouble("WIP_LENGTH")), TableDesc.getSelectedRow(), 7);
                                    TableDesc.setValueAt(df.format(trs.getDouble("WIP_WIDTH")), TableDesc.getSelectedRow(), 8);
                                    TableDesc.setValueAt(trs.getString("WIP_GSM"), TableDesc.getSelectedRow(), 9);
                                    if (PieceNo.contains("B") || PieceNo.contains("b")) {
                                        TableDesc.setValueAt("0.00", TableDesc.getSelectedRow(), 10);
                                    } else {
                                        TableDesc.setValueAt(df.format(trs.getDouble("WIP_THORITICAL_WEIGHT")), TableDesc.getSelectedRow(), 10);
                                    }
                                    TableDesc.changeSelection(TableDesc.getSelectedRow(), 11, false, false);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }//GEN-LAST:event_TableDescKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApprovalPanel;
    private javax.swing.JTextField DOC_NO;
    private javax.swing.JTable Formula;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JPanel Tab1;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableAmend;
    private javax.swing.JTable TableDesc;
    private javax.swing.JTable TableH;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton bexcel;
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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JFormattedTextField txtactlwarpreali;
    private javax.swing.JFormattedTextField txtbeam;
    private javax.swing.JFormattedTextField txtend10cm;
    private javax.swing.JTextField txtfabrealper;
    private javax.swing.JFormattedTextField txtloom;
    private javax.swing.JTextField txtreedcnt;
    private javax.swing.JFormattedTextField txtreedspace;
    private javax.swing.JTextField txtselewarpdtl;
    private javax.swing.JTextField txttotpclen;
    private javax.swing.JTextField txtwarpdtl;
    private javax.swing.JTextField txtwarpendtot;
    private javax.swing.JFormattedTextField txtwarplen;
    private javax.swing.JTextField txtwarptex;
    private javax.swing.JTextField txtwovenlen;
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
        aList.ModuleID = 783;

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
            DOC_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 783, FFNo, false));
            txtbeam.requestFocus();
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

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsWarpingBeamOrderHDS.ModuleID, (String) Obj.getAttribute("PROFORMA_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsWarpingBeamOrderHDS.ModuleID, FromUserID, (String) Obj.getAttribute("PROFORMA_NO").getObj());

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

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsWarpingBeamOrderHDS.ModuleID, DocNo);
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

        txtactlwarpreali.setEnabled(pStat);
        txtbeam.setEnabled(pStat);
        txtend10cm.setEnabled(false);
        txtfabrealper.setEnabled(false);
        txtloom.setEnabled(pStat);
        txtreedcnt.setEnabled(false);
        txtreedspace.setEnabled(pStat);
        txtselewarpdtl.setEnabled(pStat);
        txttotpclen.setEnabled(false);
        txtwarpdtl.setEnabled(false);
        txtwarpendtot.setEnabled(false);
        txtwarplen.setEnabled(pStat);
        txtwarptex.setEnabled(false);
        txtwovenlen.setEnabled(false);
        cmdAdd.setEnabled(pStat);
        cmdItemdelete.setEnabled(pStat);
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
        txtbeam.setText("");
        txtbeam.setValue(null);
        txtloom.setText("");
        txtloom.setValue(null);
        txtreedspace.setText("");
        txtreedspace.setValue(null);
        txtwarpdtl.setText("");
        txtwarptex.setText("");
        txtend10cm.setText("");
        txtend10cm.setValue(null);
        txtactlwarpreali.setText("");
        txtactlwarpreali.setValue(null);
        txtwarplen.setText("");
        txtwarplen.setValue(null);
        txttotpclen.setText("");
        txtwovenlen.setText("");
        txtwarpendtot.setText("");
        txtreedcnt.setText("");
        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        txtfabrealper.setText("");
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
        String lDocNo = Obj.getAttribute("DOC_NO").getString();
        if (Obj.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (clsFeltProductionApprovalFlow.IsCreator(clsWarpingBeamOrderHDS.ModuleID, lDocNo)) {  // || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 60031, 600312)) {
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
        if (txtbeam.getText().length() <= 0) {
            JOptionPane.showMessageDialog(null, "Please Enter Beam No..");
            txtbeam.requestFocus();
            return;
        }
        if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID) && EditMode == EITLERPGLOBAL.ADD) {
            if (!chkbeamno()) {
                //JOptionPane.showMessageDialog(null, "Please Enter Beam No..");
                txtbeam.requestFocus();
                return;
            }
        }
        if (txtloom.getText().length() <= 0) {
            JOptionPane.showMessageDialog(null, "Please Enter Loom No..");
            txtloom.requestFocus();
            return;
        }
        if (txtreedspace.getText().length() <= 0) {
            JOptionPane.showMessageDialog(null, "Please Enter Reed Space..");
            txtreedspace.requestFocus();
            return;
        }
        if (txtwarpdtl.getText().length() <= 0) {
            JOptionPane.showMessageDialog(null, "Please Selct Warp Detail..");
            txtwarpdtl.requestFocus();
            return;
        }
        if (txtend10cm.getText().length() <= 0) {
            JOptionPane.showMessageDialog(null, "Please Enter End 10 cm ..");
            txtend10cm.requestFocus();
            return;
        }
        if (txtactlwarpreali.getText().length() <= 0) {
            JOptionPane.showMessageDialog(null, "Please Enter Actual Realisation..");
            txtactlwarpreali.requestFocus();
            return;
        }
        if (txtwarplen.getText().length() <= 0) {
            JOptionPane.showMessageDialog(null, "Please Enter Warp Length..");
            txtwarplen.requestFocus();
            return;
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
            if (TableDesc.getValueAt(k, 1).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter Piece No at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (TableDesc.getValueAt(k, 11).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter Sequence No at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (TableDesc.getValueAt(k, 12).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter R.Space on Loom at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (TableDesc.getValueAt(k, 13).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter Theo PC Length Mtr at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (TableDesc.getValueAt(k, 18).toString().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Enter Theo Picks/10 cm at Row " + (k + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (data.getStringValueFromDB("SELECT WIP_STATUS FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + TableDesc.getValueAt(k, 1).toString() + "'").equalsIgnoreCase("CANCELED")) {
                JOptionPane.showMessageDialog(this, "Piece is Canceled at Row " + (k + 1) + " ... Please Remove Piece...", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

        }
        for (int k = 0; k <= TableDesc.getRowCount() - 1; k++) {
            for (int l = k; l <= TableDesc.getRowCount() - 1; l++) {
                if (l != k && ((String) TableDesc.getValueAt(k, 1)).trim().equals(((String) TableDesc.getValueAt(l, 1)).trim())) {
                    JOptionPane.showMessageDialog(this, "Same Piece No at Row " + (k + 1) + " and " + (l + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (l != k && ((String) TableDesc.getValueAt(k, 11)).trim().equals(((String) TableDesc.getValueAt(l, 11)).trim())) {
                    JOptionPane.showMessageDialog(this, "Same Sequence No at Row " + (k + 1) + " and " + (l + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        SetData();
        if (EditMode == EITLERPGLOBAL.ADD) {

            if (Obj.Insert()) {

                if (OpgFinal.isSelected()) {
//                    try {
//                        String DOC_NO = "";
//                        String DOC_DATE = "";
//                        String Party_Code = "";
//
//                        String responce = JavaMail.sendFinalApprovalMail(clsWarpingBeamOrderHDS.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
//                        System.out.println("Send Mail Responce : " + responce);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
//
//                // MoveLast();
//                DisplayData();
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
//                        String responce = JavaMail.sendFinalApprovalMail(clsWarpingBeamOrderHDS.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
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
        Obj.Filter(" DOC_NO='" + DOC_NO.getText() + "'");
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
            int ModuleID = clsWarpingBeamOrderHDS.ModuleID;

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
            txtbeam.setText(Obj.getAttribute("BEAM_NO").getObj().toString());
            txtloom.setText(Obj.getAttribute("LOOM_NO").getObj().toString());
            txtreedspace.setText(df.format(Double.parseDouble(Obj.getAttribute("REED_SPACE").getObj().toString())));
            txtwarpdtl.setText(Obj.getAttribute("WARP_DETAIL").getObj().toString());
            txtwarptex.setText(Obj.getAttribute("WARP_TEX").getObj().toString());
            txtend10cm.setText(df.format(Double.parseDouble(String.valueOf(Obj.getAttribute("ENDS_10_CM").getDouble()))));
            txtactlwarpreali.setText(df.format(Double.parseDouble(String.valueOf(Obj.getAttribute("ACTUAL_WARP_RELISATION").getDouble()))));
            txtwarplen.setText(df.format(Double.parseDouble(String.valueOf(Obj.getAttribute("WARP_LENGTH").getDouble()))));
            txtreedcnt.setText(Obj.getAttribute("REED_COUNT").getObj().toString());
            txtfabrealper.setText(df.format(Double.parseDouble(Obj.getAttribute("FABRIC_REALISATION_PER").getObj().toString())));
            try {
                txtwarpendtot.setText(df.format(Double.parseDouble(String.valueOf((Double.parseDouble(txtreedspace.getText()) * Double.parseDouble(txtend10cm.getText())) * 10))));
            } catch (Exception e) {
                txtwarpendtot.setText("0.00");
            }
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, Obj.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table
            double mtottpick = 0;
            for (int i = 1; i <= Obj.colMRItems.size(); i++) {

                clsWarpingBeamOrderHDSItem ObjItem = (clsWarpingBeamOrderHDSItem) Obj.colMRItems.get(Integer.toString(i));
                Object[] rowData = new Object[50];

                rowData[0] = ObjItem.getAttribute("SR_NO").getInt();
                rowData[1] = (String) ObjItem.getAttribute("PIECE_NO").getObj();
                rowData[2] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[3] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rowData[2].toString());
                rowData[4] = (String) ObjItem.getAttribute("PRODUCT_CODE").getObj();
                rowData[5] = (String) ObjItem.getAttribute("GRUP").getObj();
                rowData[6] = (String) ObjItem.getAttribute("STYLE").getObj();
                rowData[7] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("LENGTH").getDouble(), 2));
                rowData[8] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("WIDTH").getDouble(), 2));
                rowData[9] = Integer.toString((int) ObjItem.getAttribute("GSM").getDouble());
                rowData[10] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("WEIGHT").getDouble(), 2));
                rowData[11] = Integer.toString(ObjItem.getAttribute("SEQUANCE_NO").getInt());
                rowData[12] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("READ_SPACE").getDouble(), 2));
                rowData[13] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("THEORICAL_LENGTH_MTR").getDouble(), 2));
                rowData[14] = Integer.toString((int) ObjItem.getAttribute("SERIES").getDouble());
                rowData[15] = ObjItem.getAttribute("SUB_SERIES").getString();
                if (ObjItem.getAttribute("TOTAL").getObj().toString().equalsIgnoreCase("0.00")) {
                    rowData[16] = "";
                } else {
                    rowData[16] = (String) ObjItem.getAttribute("TOTAL").getObj();
                }
                if (ObjItem.getAttribute("MAX_THEORICAL_LENGTH").getObj().toString().equalsIgnoreCase("0.00")) {
                    rowData[17] = "";
                } else {
                    rowData[17] = (String) ObjItem.getAttribute("MAX_THEORICAL_LENGTH").getObj();
                }
                rowData[18] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("THEORICAL_PICKS_10_CM").getDouble(), 2));
                rowData[19] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("TOTAL_PICKS").getDouble(), 2));
                rowData[20] = df.format(EITLERPGLOBAL.round(ObjItem.getAttribute("EXPECTED_GREV_SQ_MTR").getDouble(), 2));
                try {
                    rowData[21] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("WEAVING_DATE").getObj().toString());
                } catch (Exception e) {
                    rowData[21] = "";
                }
                try {
                    mtottpick = mtottpick + Double.parseDouble(rowData[17].toString());
                } catch (Exception a) {
                    mtottpick = mtottpick;
                }
                DataModelDesc.addRow(rowData);
            }
            UpdateWovenLength();
            txttotpclen.setText(df.format(EITLERPGLOBAL.round(mtottpick, 2)));
            txtwovenlen.setText(df.format(EITLERPGLOBAL.round(Double.parseDouble(txtwarplen.getText()) - mtottpick, 2)));

            DoNotEvaluate = false;
            //======== Generating Grid for History ========//
            FormatGridH();
            String MBeamNo = Obj.getAttribute("BEAM_NO").getString();
            String msql = "SELECT H.DOC_NO,ACTUAL_WARP_RELISATION,WARP_LENGTH,FABRIC_REALISATION_PER,SUM(THEORICAL_LENGTH_MTR) AS TOTAL_PC_LEN,H.REASON "
                    + "FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_HEADER_AMEND H,PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL_AMEND D "
                    + "WHERE H.DOC_NO=D.DOC_NO AND H.BEAM_NO='" + MBeamNo + "' "
                    + " GROUP BY H.DOC_NO";
            ResultSet hisrs = data.getResult(msql);
            hisrs.first();
            if (hisrs.getRow() > 0) {
                while (!hisrs.isAfterLast()) {
                    Object[] rowData = new Object[20];
                    rowData[0] = hisrs.getString("DOC_NO");
                    rowData[1] = df.format(hisrs.getDouble("FABRIC_REALISATION_PER"));
                    rowData[2] = df.format(hisrs.getDouble("ACTUAL_WARP_RELISATION"));
                    rowData[3] = df.format(hisrs.getDouble("WARP_LENGTH"));
                    rowData[4] = df.format(hisrs.getDouble("TOTAL_PC_LEN"));
                    rowData[5] = df.format(hisrs.getDouble("WARP_LENGTH") - hisrs.getDouble("TOTAL_PC_LEN"));
                    rowData[6] = hisrs.getString("REASON");
                    DataModelH.addRow(rowData);
                    hisrs.next();
                }
            }
            //======== END History ========//
            FormatGridHis();
            try {
                ResultSet t;
                t = data.getResult("SELECT * FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL WHERE INDICATOR IN ('ADD','DELETE') AND DOC_NO='" + DOC_NO.getText() + "' ORDER BY INDICATOR");
                t.first();
                if (t.getRow() > 0) {
                    while (!t.isAfterLast()) {
                        Object[] rowData = new Object[50];

                        rowData[0] = t.getInt("SR_NO");
                        rowData[1] = t.getString("INDICATOR");
                        rowData[2] = t.getString("INDICATOR_DOC");
                        rowData[3] = t.getString("PIECE_NO");
                        rowData[4] = t.getString("PARTY_CODE");
                        rowData[5] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rowData[4].toString());
                        rowData[6] = t.getString("PRODUCT_CODE");
                        rowData[7] = t.getString("GRUP");
                        rowData[8] = t.getString("STYLE");
                        rowData[9] = df.format(EITLERPGLOBAL.round(t.getDouble("LENGTH"), 2));
                        rowData[10] = df.format(EITLERPGLOBAL.round(t.getDouble("WIDTH"), 2));
                        rowData[11] = t.getInt("GSM");
                        rowData[12] = df.format(EITLERPGLOBAL.round(t.getDouble("WEIGHT"), 2));
                        rowData[13] = t.getInt("SEQUANCE_NO");
                        rowData[14] = df.format(EITLERPGLOBAL.round(t.getDouble("READ_SPACE"), 2));
                        rowData[15] = df.format(EITLERPGLOBAL.round(t.getDouble("THEORICAL_LENGTH_MTR"), 2));
                        //rowData[16] = t.getInt("SERIES");
                        //rowData[17] = t.getString("MAX_THEORICAL_LENGTH");
                        rowData[16] = df.format(EITLERPGLOBAL.round(t.getDouble("THEORICAL_PICKS_10_CM"), 2));
                        rowData[17] = df.format(EITLERPGLOBAL.round(t.getDouble("TOTAL_PICKS"), 2));
                        rowData[18] = df.format(EITLERPGLOBAL.round(t.getDouble("EXPECTED_GREV_SQ_MTR"), 2));

                        DataModelDesc1.addRow(rowData);
                        t.next();
                    }
                }
            } catch (Exception a) {
                a.printStackTrace();
            }
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String DocNo = Obj.getAttribute("DOC_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsWarpingBeamOrderHDS.ModuleID, DocNo);
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
            HashMap History = clsWarpingBeamOrderHDS.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsWarpingBeamOrderHDS ObjHistory = (clsWarpingBeamOrderHDS) History.get(Integer.toString(i));
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
            // JOptionPane.showMessageDialog(null, "Display Data Error: " + e.getMessage());
            System.out.println("Display Data Error: " + e.getMessage());
        }
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields

        Obj.setAttribute("PREFIX", SelPrefix);
        Obj.setAttribute("SUFFIX", SelSuffix);
        Obj.setAttribute("FFNO", FFNo);
        Obj.setAttribute("DOC_NO", DOC_NO.getText());
        Obj.setAttribute("BEAM_NO", txtbeam.getText());
        Obj.setAttribute("LOOM_NO", txtloom.getText());
        Obj.setAttribute("REED_SPACE", txtreedspace.getText());
        Obj.setAttribute("WARP_DETAIL", txtwarpdtl.getText());
        Obj.setAttribute("WARP_TEX", txtwarptex.getText());
        Obj.setAttribute("ENDS_10_CM", txtend10cm.getText());
        Obj.setAttribute("ACTUAL_WARP_RELISATION", txtactlwarpreali.getText());
        Obj.setAttribute("WARP_LENGTH", txtwarplen.getText());
        Obj.setAttribute("REED_COUNT", txtreedcnt.getText());
        Obj.setAttribute("FABRIC_REALISATION_PER", txtfabrealper.getText());

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
            clsWarpingBeamOrderHDSItem ObjItem = new clsWarpingBeamOrderHDSItem();
            String lItemID = (String) TableDesc.getValueAt(i, 1);

            //Add Only Valid Items
            ObjItem.setAttribute("SR_NO", TableDesc.getValueAt(i, 0).toString());
            ObjItem.setAttribute("PIECE_NO", lItemID);
            ObjItem.setAttribute("PARTY_CODE", TableDesc.getValueAt(i, 2).toString());
            ObjItem.setAttribute("PRODUCT_CODE", TableDesc.getValueAt(i, 4).toString());
            ObjItem.setAttribute("GRUP", TableDesc.getValueAt(i, 5).toString());
            ObjItem.setAttribute("STYLE", TableDesc.getValueAt(i, 6).toString());
            ObjItem.setAttribute("LENGTH", Double.parseDouble(TableDesc.getValueAt(i, 7).toString()));
            ObjItem.setAttribute("WIDTH", Double.parseDouble(TableDesc.getValueAt(i, 8).toString()));
            ObjItem.setAttribute("GSM", Double.parseDouble(TableDesc.getValueAt(i, 9).toString()));
            ObjItem.setAttribute("WEIGHT", Double.parseDouble(TableDesc.getValueAt(i, 10).toString()));
            ObjItem.setAttribute("SEQUANCE_NO", Integer.parseInt(TableDesc.getValueAt(i, 11).toString()));
            ObjItem.setAttribute("READ_SPACE", Double.parseDouble(TableDesc.getValueAt(i, 12).toString()));
            ObjItem.setAttribute("THEORICAL_LENGTH_MTR", Double.parseDouble(TableDesc.getValueAt(i, 13).toString()));
            ObjItem.setAttribute("SERIES", Integer.parseInt(TableDesc.getValueAt(i, 14).toString()));
            ObjItem.setAttribute("SUB_SERIES", TableDesc.getValueAt(i, 15).toString());
            ObjItem.setAttribute("TOTAL", TableDesc.getValueAt(i, 16).toString());
            ObjItem.setAttribute("MAX_THEORICAL_LENGTH", TableDesc.getValueAt(i, 17).toString());
            ObjItem.setAttribute("THEORICAL_PICKS_10_CM", Double.parseDouble(TableDesc.getValueAt(i, 18).toString()));
            ObjItem.setAttribute("TOTAL_PICKS", Double.parseDouble(TableDesc.getValueAt(i, 19).toString()));
            ObjItem.setAttribute("EXPECTED_GREV_SQ_MTR", Double.parseDouble(TableDesc.getValueAt(i, 20).toString()));
            ObjItem.setAttribute("WEAVING_DATE", EITLERPGLOBAL.formatDateDB(TableDesc.getValueAt(i, 21).toString()));
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

    private void FormatGridH() {
        DataModelH = new EITLTableModel();

        TableH.removeAll();
        TableH.setModel(DataModelH);

        //Set the table Readonly
        DataModelH.TableReadOnly(true);

        //Add the columns
        DataModelH.addColumn("DOC No.");
        DataModelH.addColumn("Fabric Realisation Per ");
        DataModelH.addColumn("Actual Warp Length");
        DataModelH.addColumn("Achivable Woven Length");
        DataModelH.addColumn("Total PC Length");
        DataModelH.addColumn("Remaining Woven Length");
        DataModelH.addColumn("Reason");

        TableH.setAutoResizeMode(TableH.AUTO_RESIZE_OFF);

    }

    private void SetMenuForRights() {
        // --- Add Rights --        
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 60031, 600311)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 60031, 600312)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }
        //cmdEdit.setEnabled(true);

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 60031, 600313)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 60031, 600315)) {
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
        Obj.Filter(" DOC_NO IN (SELECT H.DOC_NO FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=783 AND CANCELED=0)");
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsWarpingBeamOrderHDS.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsWarpingBeamOrderHDS.ModuleID, DocNo, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsWarpingBeamOrderHDS.ModuleID, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    private void UpdateSrNo() {
        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            TableDesc.setValueAt(Integer.toString(i + 1), i, 0);
        }
    }

    private void UpdateWovenLength() {
        double mtotlen = 0, mexp_grev = 0, mreedspc, mwrplen;
        HashMap<Integer, Double> hm = new HashMap<Integer, Double>();
        HashMap<String, Double> hmt = new HashMap<String, Double>();
        int mseries = 1, i = 0;
        String mkey = TableDesc.getValueAt(i, 14).toString() + TableDesc.getValueAt(i, 15).toString().substring(0, 1);
        //hmt.put(TableDesc.getValueAt(i, 14).toString() + TableDesc.getValueAt(i, 15).toString(), Double.parseDouble(TableDesc.getValueAt(i, 13).toString()));
        //SERIESWISE TOTAL
        for (i = 0; i < TableDesc.getRowCount(); i++) {
            if (!mkey.equalsIgnoreCase(TableDesc.getValueAt(i, 14).toString() + TableDesc.getValueAt(i, 15).toString().substring(0, 1))) {
                for (int j = 0; j <= i; j++) {
                    if (mkey.equalsIgnoreCase(TableDesc.getValueAt(j, 14).toString() + TableDesc.getValueAt(j, 15).toString().substring(0, 1))) {
                        TableDesc.setValueAt("0", j, 16);
                    }
                }
                TableDesc.setValueAt(EITLERPGLOBAL.round(Double.parseDouble(hmt.get(TableDesc.getValueAt(i - 1, 14).toString() + TableDesc.getValueAt(i - 1, 15).toString().substring(0, 1)).toString()), 2), i - 1, 16);
                mkey = TableDesc.getValueAt(i, 14).toString() + TableDesc.getValueAt(i, 15).toString().substring(0, 1);

            }
            if (hmt.containsKey(TableDesc.getValueAt(i, 14).toString() + TableDesc.getValueAt(i, 15).toString().substring(0, 1))) {

                hmt.put(TableDesc.getValueAt(i, 14).toString() + TableDesc.getValueAt(i, 15).toString().substring(0, 1), Double.parseDouble(hmt.get(TableDesc.getValueAt(i, 14).toString() + TableDesc.getValueAt(i, 15).toString().substring(0, 1)).toString()) + Double.parseDouble(TableDesc.getValueAt(i, 13).toString()));

            } else {
                hmt.put(TableDesc.getValueAt(i, 14).toString() + TableDesc.getValueAt(i, 15).toString().substring(0, 1), Double.parseDouble(TableDesc.getValueAt(i, 13).toString()));
            }
        }
        TableDesc.setValueAt(EITLERPGLOBAL.round(Double.parseDouble(hmt.get(TableDesc.getValueAt(i - 1, 14).toString() + TableDesc.getValueAt(i - 1, 15).toString().substring(0, 1)).toString()), 2), i - 1, 16);
        for (int j = 0; j < i - 1; j++) {
            try {
                if (mkey.equalsIgnoreCase(TableDesc.getValueAt(j, 14).toString() + TableDesc.getValueAt(j, 15).toString().substring(0, 1))) {
                    TableDesc.setValueAt("0", j, 16);
                }
            } catch (Exception e) {

            }
        }
        //MAX LENGTH
        for (i = 0; i < TableDesc.getRowCount(); i++) {
            if (mseries != Integer.parseInt(TableDesc.getValueAt(i, 14).toString())) {
                for (int j = 0; j <= i; j++) {
                    if (mseries == Integer.parseInt(TableDesc.getValueAt(j, 14).toString())) {
                        TableDesc.setValueAt("", j, 17);
                    }
                }
                TableDesc.setValueAt(Double.parseDouble(hm.get(Integer.parseInt(TableDesc.getValueAt(i - 1, 14).toString())).toString()), i - 1, 17);
                mseries = Integer.parseInt(TableDesc.getValueAt(i, 14).toString());

            }
            if (hm.containsKey(Integer.parseInt(TableDesc.getValueAt(i, 14).toString()))) {
                if (Double.parseDouble(hm.get(Integer.parseInt(TableDesc.getValueAt(i, 14).toString())).toString()) < Double.parseDouble(TableDesc.getValueAt(i, 16).toString())) {
                    hm.put(Integer.parseInt(TableDesc.getValueAt(i, 14).toString()), Double.parseDouble(TableDesc.getValueAt(i, 16).toString()));
                }
            } else {
                hm.put(Integer.parseInt(TableDesc.getValueAt(i, 14).toString()), Double.parseDouble(TableDesc.getValueAt(i, 16).toString()));
            }

            try {
                mexp_grev = mexp_grev + Double.parseDouble(TableDesc.getValueAt(i, 20).toString());
            } catch (Exception q) {
                mexp_grev = mexp_grev;
            }
        }
        TableDesc.setValueAt(Double.parseDouble(hm.get(Integer.parseInt(TableDesc.getValueAt(i - 1, 14).toString())).toString()), i - 1, 17);
        for (int j = 0; j < i - 1; j++) {
            if (mseries == Integer.parseInt(TableDesc.getValueAt(j, 14).toString())) {
                TableDesc.setValueAt("", j, 17);
            }
        }

        //mseries = Integer.parseInt(TableDesc.getValueAt(i, 14).toString());
        mtotlen = 0;
        for (i = 0; i < TableDesc.getRowCount(); i++) {
            try {
                mtotlen = mtotlen + Double.parseDouble(TableDesc.getValueAt(i, 17).toString());
            } catch (Exception u) {
                mtotlen = mtotlen;
            }
        }
        try {
            mreedspc = Double.parseDouble(txtreedspace.getText());
        } catch (Exception a) {
            mreedspc = 0;
        }
        try {
            mwrplen = Double.parseDouble(txtactlwarpreali.getText());
        } catch (Exception a) {
            mwrplen = 0;
        }
        if (mexp_grev > 0) {
            txtfabrealper.setText(df.format(EITLERPGLOBAL.round((mexp_grev / (mreedspc * mwrplen)) * 100, 2)));
        } else {
            txtfabrealper.setText("");
        }
        txttotpclen.setText(df.format(EITLERPGLOBAL.round(mtotlen, 2)));
        txtwovenlen.setText(df.format(EITLERPGLOBAL.round(Double.parseDouble(txtwarplen.getText()) - mtotlen, 2)));

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
            DataModelDesc.addColumn("Party Code");//2
            DataModelDesc.addColumn("Name");//3
            DataModelDesc.addColumn("Product");//4
            DataModelDesc.addColumn("Group"); //5
            DataModelDesc.addColumn("Style");  //6
            DataModelDesc.addColumn("Fin Length"); //7
            DataModelDesc.addColumn("Fin Width"); //8
            DataModelDesc.addColumn("Fin GSM"); //9
            DataModelDesc.addColumn("Fin Wt Kg");   //10   
            DataModelDesc.addColumn("Sequence No.");   //11   
            DataModelDesc.addColumn("R.Space on Loom"); //12
            DataModelDesc.addColumn("Theo PC Len Mtr");  //13
            DataModelDesc.addColumn("Series");  //14

            DataModelDesc.addColumn("Sub-Series");  //15
            DataModelDesc.addColumn("Total");  //16

            DataModelDesc.addColumn("Max Theo Length");  //17
            DataModelDesc.addColumn("Theo Picks/10 CM");  //18
            DataModelDesc.addColumn("Total Picks");  //19
            DataModelDesc.addColumn("Expected Grev SqMtr");  //20
            DataModelDesc.addColumn("Weaving Date");  //21

            DataModelDesc.SetReadOnly(0);
            DataModelDesc.SetReadOnly(21);
            SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
            if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID) && (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT)) {
                DataModelDesc.ResetReadOnly(1);
                DataModelDesc.ResetReadOnly(11);
                DataModelDesc.ResetReadOnly(12);
                DataModelDesc.ResetReadOnly(13);
                DataModelDesc.ResetReadOnly(14);
                DataModelDesc.ResetReadOnly(15);
                DataModelDesc.ResetReadOnly(18);
            } else {
                DataModelDesc.SetReadOnly(1);
                DataModelDesc.SetReadOnly(11);
                DataModelDesc.SetReadOnly(12);
                DataModelDesc.SetReadOnly(13);
                DataModelDesc.SetReadOnly(14);
                DataModelDesc.SetReadOnly(15);
                DataModelDesc.SetReadOnly(18);
            }
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
            //DataModelDesc.SetReadOnly(11);
            //DataModelDesc.SetReadOnly(12);
            //DataModelDesc.SetReadOnly(13);
            //DataModelDesc.SetReadOnly(15);
            DataModelDesc.SetReadOnly(16);
            DataModelDesc.SetReadOnly(17);
            //DataModelDesc.SetReadOnly(18);
            DataModelDesc.SetReadOnly(19);
            DataModelDesc.SetReadOnly(20);

            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            TableDesc.getColumnModel().getColumn(16).setPreferredWidth(100);

        } catch (Exception e) {

        }
        Updating = false;
    }

    private void FormatGridHis() {

        Updating = true; //Stops recursion
        try {
            DataModelDesc1 = new EITLTableModel();
            TableAmend.removeAll();
            TableAmend.setModel(DataModelDesc1);
            TableColumnModel ColModel = TableAmend.getColumnModel();
            TableAmend.setAutoResizeMode(TableAmend.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);

            DataModelDesc1.addColumn("Sr.");  //0 - Read Only
            DataModelDesc1.addColumn("Status");  //0 - Read Only
            DataModelDesc1.addColumn("Doc No");  //0 - Read Only
            DataModelDesc1.addColumn("Piece No"); //1
            DataModelDesc1.addColumn("Party Code");//2
            DataModelDesc1.addColumn("Name");//3
            DataModelDesc1.addColumn("Product");//4
            DataModelDesc1.addColumn("Group"); //5
            DataModelDesc1.addColumn("Style");  //6
            DataModelDesc1.addColumn("Fin Length"); //7
            DataModelDesc1.addColumn("Fin Width"); //8
            DataModelDesc1.addColumn("Fin GSM"); //9
            DataModelDesc1.addColumn("Fin Wt Kg");   //10   
            DataModelDesc1.addColumn("Sequence No.");   //11   
            DataModelDesc1.addColumn("R.Space on Loom"); //12
            DataModelDesc1.addColumn("Theo PC Len Mtr");  //13
            DataModelDesc1.addColumn("Theo Picks/10 CM");  //14
            DataModelDesc1.addColumn("Total Picks");  //15
            DataModelDesc1.addColumn("Expected Grev SqMtr");  //16

            for (int k = 0; k < 19; k++) {
                DataModelDesc1.SetReadOnly(k);
            }

            TableAmend.getColumnModel().getColumn(0).setMaxWidth(50);
            TableAmend.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            TableAmend.getColumnModel().getColumn(18).setPreferredWidth(100);

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
            Connection Conn = EITLERP.data.getConn();
            HashMap parameterMap = new HashMap();

            EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(parameterMap, Conn);
            String strSQL = "SELECT * FROM (SELECT H.DOC_NO,H.BEAM_NO,H.LOOM_NO,H.REED_SPACE,H.WARP_DETAIL,H.FABRIC_REALISATION_PER,H.WARP_TEX,(H.REED_SPACE*H.ENDS_10_CM)*10 AS WARP_END_TOTAL,H.ENDS_10_CM,H.ACTUAL_WARP_RELISATION,H.WARP_LENGTH,H.REED_COUNT, "
                    + "SEQUANCE_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,STYLE,LENGTH,WIDTH,GSM,WEIGHT,READ_SPACE,THEORICAL_LENGTH_MTR,SERIES,MAX_THEORICAL_LENGTH,THEORICAL_PICKS_10_CM,TOTAL_PICKS,EXPECTED_GREV_SQ_MTR "
                    + " FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL D,PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_HEADER H "
                    + "WHERE H.DOC_NO=D.DOC_NO AND "
                    + "H.DOC_NO='" + DOC_NO.getText() + "' AND (D.INDICATOR IS NULL OR D.INDICATOR IN ('INSERT','ADD',''))) AS A "
                    + "LEFT JOIN (SELECT DOC_NO,SUM(THEORICAL_LENGTH_MTR) AS TOTAL_PC_LENGTH FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL WHERE (INDICATOR IS NULL OR INDICATOR IN ('INSERT','ADD','')) AND DOC_NO='" + DOC_NO.getText() + "') AS B "
                    + "ON A.DOC_NO=B.DOC_NO "
                    + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS C "
                    + "ON A.PARTY_CODE=C.PARTY_CODE "
                    + "ORDER BY SEQUANCE_NO";
            rpt.setReportName("/EITLERP/FeltSales/FeltWarpingBeamOrder/FELT_WARPING_BEAM_ORDER_HDS.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean chkbeamno() {
        int a = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_HEADER WHERE BEAM_NO='" + txtbeam.getText() + "' ");
        if (a > 0) {
            JOptionPane.showMessageDialog(null, "Beam No. is already Taken...");
            txtbeam.setText("");
            txtbeam.setValue(null);
            txtbeam.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void Mail() {
        System.out.println("Felt Warping Beam Order approved = " + Obj.getAttribute("APPROVAL_STATUS").getString());
        String pBody = "", pSubject = "", recievers = "", pcc = "";
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
                pBody += "Beam No.      : " + txtbeam.getText() + " <br>";
                pBody += "Loom No.      : " + txtloom.getText() + " <br>";
                pBody += "Reed Space    : " + txtreedspace.getText() + " <br>";
                pBody += "Warp Tex      : " + txtwarptex.getText() + " <br>";
                pBody += "Warp Detail   : " + txtwarpdtl.getText() + " <br>";
                pBody += "End/10 cm     : " + txtend10cm.getText() + " <br>";
                pBody += "Actual Warp Length     : " + txtactlwarpreali.getText() + " <br>";
                pBody += "Achivable Woven Length : " + txtwarplen.getText() + " <br>";
                pBody += "Total Theo PC Length   : " + txttotpclen.getText() + " <br>";
                pBody += "Remaining Woven Length : " + txtwovenlen.getText() + " <br>";
                pBody += "Warp End Total: " + txtwarpendtot.getText() + " <br>";
                pBody += "Reed Count    : " + txtreedcnt.getText() + " <br>";

                Connection tmConn;
                Statement tmstmt;
                ResultSet tmrsData;

                tmConn = data.getConn();
                tmstmt = tmConn.createStatement();
                tmrsData = tmstmt.executeQuery("SELECT  D.*,PARTY_NAME FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL D LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER P ON D.PARTY_CODE=P.PARTY_CODE WHERE DOC_NO='" + DOC_NO.getText() + "' ORDER BY SEQUANCE_NO ");
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
                        + "<td align='center'><b>Series</b></td>"
                        + "<td align='center'><b>Sub Series</b></td>"
                        + "<td align='center'><b>Total</b></td>"
                        + "<td align='center'><b>Max Theo Length</b></td>"
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
                    pBody += "<td>" + tmrsData.getInt("SERIES") + "</td>";
                    pBody += "<td>" + tmrsData.getString("SUB_SERIES") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("TOTAL") + "</td>";
                    pBody += "<td>" + tmrsData.getDouble("MAX_THEORICAL_LENGTH") + "</td>";
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

                HashMap hmApprovalHistory = clsWarpingBeamOrderHDS.getHistoryList(EITLERPGLOBAL.gCompanyID, DOC_NO.getText());
                for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                    pBody += "<tr>";

                    clsWarpingBeamOrderHDS ObjWarping = (clsWarpingBeamOrderHDS) hmApprovalHistory.get(Integer.toString(i));
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

                String responce = MailNotification.sendNotificationMail(783, pSubject, pBody, recievers, pcc, EITLERPGLOBAL.getComboCode(cmbHierarchy));
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
