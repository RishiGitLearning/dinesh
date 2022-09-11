package EITLERP.Production.FeltDiscRateMaster;

/**
 *
 *
 */
/*<APPLET CODE=frmSalesParty.class HEIGHT=574 WIDTH=758></APPLET>*/
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
//import EITLERP.FeltSales.common.JavaMail;
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
import java.lang.Class;
import java.net.*;
import EITLERP.Utils.*;
import java.io.*;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.FeltSales.common.DatePicker.DateTextField;
import EITLERP.FeltSales.common.MailNotification;
import EITLERP.FeltSales.common.clsOrderValueCalc;
import java.math.*;
import TReportWriter.*;
import EITLERP.Stores.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import EITLERP.Purchase.*;
import java.math.BigDecimal;

//import EITLERP.Purchase.frmSendMail;
public class frmDiscRateMaster extends javax.swing.JApplet {

    private ResultSet rsResultSet, rsResultSet1, rsResultSet2;
    private Connection Conn;
    private Statement Stmt;

    private int EditMode = 0;

    //private clsDiscRateMaster ObjSalesParty;
    private clsDiscRateMaster ObjSalesParty;

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private TReportEngine objEngine = new TReportEngine();

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
//    private Connection Conn;

    public String[] p;
    public String[] q;

    /**
     * Creates new form frmSalesParty
     */
    public frmDiscRateMaster() {
        System.gc();
        setSize(800, 600);
        initComponents();

        cmdDelete.setEnabled(false);
        cmdDelete.disable();
        cmdDelete.setVisible(false);

        cmdPrint.setEnabled(false);
        cmdPrint.disable();
        cmdPrint.setVisible(false);

        jLabel9.setVisible(false);
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
        lblTitle.setForeground(Color.WHITE);
        ObjSalesParty = new clsDiscRateMaster();

        //lblDocThrough.setVisible(false);
        //txtDocThrough.setVisible(false);
        SetMenuForRights();

        if (ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID)) {
            ObjSalesParty.MoveLast();
            DisplayData();

        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data. \n Error is " + ObjSalesParty.LastError);
        }

        txtAuditRemarks.setVisible(false);
        txtPartycode.setEnabled(false);
        DataModelDesc.TableReadOnly(true);
        txtremark1.setEditable(false);
        txtremark2.setEditable(false);
        txtremark3.setEditable(false);
        txtremark4.setEditable(false);
        txtremark5.setEditable(false);

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

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsDiscRateMaster.ModuleID + " AND D_COM_HIERARCHY.HIERARCHY_ID>4300");//3714

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsDiscRateMaster.ModuleID + "");
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

        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemProd = new javax.swing.JMenuItem();
        jMenuItemPiece = new javax.swing.JMenuItem();
        jMenuItemGroup = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItemParty = new javax.swing.JMenuItem();
        jMenuItemAll = new javax.swing.JMenuItem();
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
        txtMasterNo = new javax.swing.JTextField();
        txtEffectFrom = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
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
        lblUnadjNo = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        txtEffectTo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSnctnDt = new javax.swing.JTextField();
        txtSnctnDt = new EITLERP.FeltSales.common.DatePicker.DateTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jlbl = new javax.swing.JLabel();
        jlbl2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jMenuItemProd.setText("on Product Code");
        jMenuItemProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemProdActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemProd);

        jMenuItemPiece.setText("on Piece No");
        jMenuItemPiece.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPieceActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemPiece);

        jMenuItemGroup.setText("on Group");
        jMenuItemGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGroupActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemGroup);

        jMenuItemParty.setText("Selected Party");
        jMenuItemParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPartyActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemParty);

        jMenuItemAll.setText("All Party");
        jMenuItemAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAllActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemAll);

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
        lblTitle.setText("FELT DISCOUNT MASTER");
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
        cmdAdd.setBounds(500, 10, 70, 30);

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

        cmdEditPiecedetail.setText("Edit");
        cmdEditPiecedetail.setEnabled(false);
        cmdEditPiecedetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditPiecedetailActionPerformed(evt);
            }
        });
        Tab1.add(cmdEditPiecedetail);
        cmdEditPiecedetail.setBounds(670, 10, 60, 25);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(660, 300, 90, 25);

        jTabbedPane1.addTab("Discount Detail", Tab1);

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
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        ApprovalPanel.add(jPanel6);
        jPanel6.setBounds(110, 113, 182, 100);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        ApprovalPanel.add(jLabel33);
        jLabel33.setBounds(5, 226, 100, 15);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
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
        jTabbedPane1.setBounds(0, 170, 780, 360);

        txtPartycode.setEditable(false);
        txtPartycode.setEnabled(false);
        txtPartycode = new JTextFieldHint(new JTextField(),"Search By F1");
        txtPartycode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartycodeActionPerformed(evt);
            }
        });
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
        txtPartycode.setBounds(100, 80, 110, 20);

        txtPartyname.setEditable(false);
        txtPartyname.setBackground(new java.awt.Color(204, 204, 204));
        //txtPartyname = new JTextFieldHint(new JTextField(),"Party Name");
        getContentPane().add(txtPartyname);
        txtPartyname.setBounds(220, 80, 360, 30);

        jLabel8.setText("Party Code");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(20, 80, 80, 20);

        txtMasterNo.setEditable(false);
        txtMasterNo.setEnabled(false);
        getContentPane().add(txtMasterNo);
        txtMasterNo.setBounds(680, 80, 110, 19);

        txtEffectFrom.setEnabled(false);
        txtEffectFrom = new EITLERP.FeltSales.common.DatePicker.DateTextField();
        txtEffectFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEffectFromFocusLost(evt);
            }
        });
        getContentPane().add(txtEffectFrom);
        txtEffectFrom.setBounds(170, 140, 110, 20);

        jLabel1.setText("Effective Date");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 140, 90, 20);

        lblremark1.setText("Remark 1");
        getContentPane().add(lblremark1);
        lblremark1.setBounds(20, 540, 90, 20);

        lblremark2.setText("Remark 2");
        getContentPane().add(lblremark2);
        lblremark2.setBounds(20, 560, 90, 20);

        lblremark3.setText("Remark 3");
        getContentPane().add(lblremark3);
        lblremark3.setBounds(20, 580, 90, 20);

        lblremark4.setText("Remark 4");
        getContentPane().add(lblremark4);
        lblremark4.setBounds(20, 600, 90, 20);

        lblremark5.setText("Remark 5");
        getContentPane().add(lblremark5);
        lblremark5.setBounds(20, 620, 90, 20);
        getContentPane().add(txtremark1);
        txtremark1.setBounds(100, 540, 660, 20);
        getContentPane().add(txtremark2);
        txtremark2.setBounds(100, 560, 660, 20);
        getContentPane().add(txtremark3);
        txtremark3.setBounds(100, 580, 660, 20);
        getContentPane().add(txtremark4);
        txtremark4.setBounds(100, 600, 660, 20);
        getContentPane().add(txtremark5);
        txtremark5.setBounds(100, 620, 660, 20);

        lblUnadjNo.setText("Master No.");
        getContentPane().add(lblUnadjNo);
        lblUnadjNo.setBounds(600, 80, 70, 20);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(10, 650, 760, 20);

        txtEffectTo.setEditable(false);
        txtEffectTo.setEnabled(false);
        txtEffectTo = new DateTextField();
        txtEffectTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEffectToActionPerformed(evt);
            }
        });
        txtEffectTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEffectToFocusLost(evt);
            }
        });
        getContentPane().add(txtEffectTo);
        txtEffectTo.setBounds(340, 140, 110, 20);

        jLabel2.setText("To");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(310, 140, 30, 20);

        jLabel3.setText("From");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(130, 140, 40, 20);

        jLabel4.setText("TurnOver Target");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(270, 110, 110, 20);

        txtSnctnDt.setEditable(false);
        getContentPane().add(txtSnctnDt);
        txtSnctnDt.setBounds(130, 110, 110, 20);

        jLabel5.setText("Sanction Date");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 110, 100, 20);

        jLabel6.setText("Lakh");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(460, 110, 30, 20);
        getContentPane().add(jlbl);
        jlbl.setBounds(390, 110, 60, 20);
        getContentPane().add(jlbl2);
        jlbl2.setBounds(390, 110, 60, 20);

        jLabel9.setText("Group Code");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(20, 80, 83, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemPartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPartyActionPerformed
        // TODO add your handling code here:
        PreviewPartyReport();
    }//GEN-LAST:event_jMenuItemPartyActionPerformed

    private void jMenuItemAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAllActionPerformed
        // TODO add your handling code here:
        PreviewAllReport();
    }//GEN-LAST:event_jMenuItemAllActionPerformed

    private void jMenuItemPieceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPieceActionPerformed
        // TODO add your handling code here:
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }

        //----------------------------------//
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 730;
        aList.FirstFreeNo = 191;

        //if(aList.ShowList()) {
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        GenerateCombos();
        FormatGrid1();
        jLabel8.setVisible(true);
        jLabel9.setVisible(false);

        SelPrefix = aList.Prefix; //Selected Prefix;
        SelSuffix = aList.Suffix;
        FFNo = aList.FirstFreeNo;

        SetupApproval();
        //Display newly generated document no.
        txtMasterNo.setText(clsDiscRateMaster.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 730, FFNo, false));
        txtPartycode.requestFocus();

        lblTitle.setText("FELT DISCOUNT MASTER - " + txtMasterNo.getText());
        lblTitle.setBackground(Color.GRAY);
        //        }
        //        else {
        //            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        //        }
    }//GEN-LAST:event_jMenuItemPieceActionPerformed

    private void jMenuItemProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemProdActionPerformed
        // TODO add your handling code here:
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }

        //----------------------------------//
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 730;
        aList.FirstFreeNo = 180;

        //if(aList.ShowList()) {
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        GenerateCombos();
        FormatGrid();
        jLabel8.setVisible(true);
        jLabel9.setVisible(false);
        //            chkOtherparty.setSelected(false);
        //            txtOtherPartycode.setEnabled(false);
        SelPrefix = aList.Prefix; //Selected Prefix;
        SelSuffix = aList.Suffix;
        FFNo = aList.FirstFreeNo;
        SetupApproval();
        //Display newly generated document no.
        txtMasterNo.setText(clsDiscRateMaster.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 730, FFNo, false));
        txtPartycode.requestFocus();

        lblTitle.setText("FELT DISCOUNT MASTER - " + txtMasterNo.getText());
        lblTitle.setBackground(Color.GRAY);
        //        }
        //        else {
        //            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        //        }
    }//GEN-LAST:event_jMenuItemProdActionPerformed

    private void txtEffectFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffectFromFocusLost
        // TODO add your handling code here:
        if (txtEffectFrom.getText().equals("")) {
            //        JOptionPane.showMessageDialog(null, "Please enter From Date.");
            txtEffectFrom.requestFocus();
        }
//        else if (!EITLERPGLOBAL.isDate(txtEffectFrom.getText().trim())) {
//            JOptionPane.showMessageDialog(null, "Please enter Valid Effective From Date");
//            txtEffectFrom.setText("");
//            txtEffectFrom.requestFocus();
////        } else if (java.sql.Date.valueOf(txtEffectFrom.getText().trim()).before(java.sql.Date.valueOf(EITLERPGLOBAL.getCurrentDate()))) {
//        } else if (data.getIntValueFromDB("SELECT DATEDIFF('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText())+"',CURDATE()) FROM DUAL")<0 && EditMode==EITLERPGLOBAL.ADD) {                        
//            txtEffectFrom.requestFocus();
//            JOptionPane.showMessageDialog(null, "Please enter Current Date or Greater Date in Effective From Date"); 
//            txtEffectFrom.setText("");
//        } else if (data.getIntValueFromDB("SELECT DATEDIFF('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText())+"','"+ObjSalesParty.getAttribute("CREATED_DATE").getString()+"') FROM DUAL")<0 && EditMode==EITLERPGLOBAL.EDIT) {                        
//            txtEffectFrom.requestFocus();
//            JOptionPane.showMessageDialog(null, "Please enter Date Greater then Date : "+EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("CREATED_DATE").getString())+" in Effective From Date"); 
//            txtEffectFrom.setText("");
//        }

    }//GEN-LAST:event_txtEffectFromFocusLost

    private void txtEffectToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffectToFocusLost
        // TODO add your handling code here:
        //        if(txtEffectTo.getText().equals("")){
        //            txtEffectTo.setText(EITLERPGLOBAL.FinToDate);
        //        }
        if (txtEffectTo.getText().equals("")) {
            //         JOptionPane.showMessageDialog(null, "Please enter To Date.");
//            txtEffectTo.requestFocus();
        }
//        else if (!EITLERPGLOBAL.isDate(txtEffectTo.getText().trim())) {
//            JOptionPane.showMessageDialog(null, "Please enter Valid Effective To Date");
//            txtEffectTo.setText("");
////            txtEffectTo.requestFocus();
////        } else if (java.sql.Date.valueOf(txtEffectTo.getText().trim()).before(java.sql.Date.valueOf(txtEffectFrom.getText().trim()))) {
//        } else if (data.getIntValueFromDB("SELECT DATEDIFF('"+EITLERPGLOBAL.formatDateDB(txtEffectTo.getText())+"','"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText())+"') FROM DUAL")<=0) {            
//            //            txtEffectTo.requestFocus();
//            JOptionPane.showMessageDialog(null, "Please enter Greater Date then Effective From Date in Effective To Date");
//            txtEffectTo.setText("");        
//        }
    }//GEN-LAST:event_txtEffectToFocusLost

    private void txtPartycodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartycodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartycodeActionPerformed

    private void txtEffectToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEffectToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEffectToActionPerformed

    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            String mst = txtMasterNo.getText().trim().substring(0, 2);
            if (mst.matches("FD") || mst.matches("GD")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {
                    //For PRODUCT CODE
                    if (TableDesc.getSelectedColumn() == 1) {
                        LOV aList = new LOV();

                        //String strSQL = "SELECT ITEM_CODE,GRUP FROM PRODUCTION.FELT_RATE_MASTER ORDER BY ITEM_CODE";
                        String strSQL = "SELECT PRODUCT_CODE,GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_FROM<=CURDATE() AND (EFFECTIVE_TO>=CURDATE() OR EFFECTIVE_TO='0000-00-00')";
                        aList.SQL = strSQL;
                        aList.ReturnCol = 1;
                        aList.ShowReturnCol = true;
                        //aList.DefaultSearchOn=2;
                        aList.DefaultSearchOn = 1;

                        if (aList.ShowLOV()) {
                            if (TableDesc.getCellEditor() != null) {
                                TableDesc.getCellEditor().stopCellEditing();
                            }
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 1);
                            TableDesc.setValueAt("", TableDesc.getSelectedRow(), 2);
                            TableDesc.setValueAt("", TableDesc.getSelectedRow(), 3);
                        }
                    }

                    //For MACHINE NO
                    if (TableDesc.getSelectedColumn() == 2) {
                        LOV aList = new LOV();
                        //"SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_ITEM_CODE,MM_GRUP,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='" + PARTY_CODE.getText() + "'"
                        String strSQL = "SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='" + txtPartycode.getText() + "' AND MM_ITEM_CODE='" + TableDesc.getValueAt(TableDesc.getSelectedRow(), 1) + "'";
                        aList.SQL = strSQL;
                        aList.ReturnCol = 1;
                        aList.ShowReturnCol = true;
                        //aList.DefaultSearchOn=2;
                        aList.DefaultSearchOn = 1;

                        if (aList.ShowLOV()) {
                            if (TableDesc.getCellEditor() != null) {
                                TableDesc.getCellEditor().stopCellEditing();
                            }
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 2);
                            TableDesc.setValueAt("", TableDesc.getSelectedRow(), 3);
                        }
                    }

                    //For MACHINE POSITION
                    if (TableDesc.getSelectedColumn() == 3) {
                        LOV aList = new LOV();

                        String strSQL = "SELECT MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='" + txtPartycode.getText() + "' AND MM_ITEM_CODE='" + TableDesc.getValueAt(TableDesc.getSelectedRow(), 1) + "' AND MM_MACHINE_NO='" + TableDesc.getValueAt(TableDesc.getSelectedRow(), 2) + "'";
                        aList.SQL = strSQL;
                        aList.ReturnCol = 1;
                        aList.ShowReturnCol = true;
                        //aList.DefaultSearchOn=2;
                        aList.DefaultSearchOn = 1;

                        if (aList.ShowLOV()) {
                            if (TableDesc.getCellEditor() != null) {
                                TableDesc.getCellEditor().stopCellEditing();
                            }
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 3);
                        }
                    }

                }
            }
        }
    }//GEN-LAST:event_TableDescKeyPressed

    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased
        int count = 0;
        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            count += Integer.parseInt((String) TableDesc.getValueAt(i, 8));
            //System.out.println("count = "+count);
        }
        String d = String.valueOf(count);
        jlbl.setText(d);

    }//GEN-LAST:event_TableDescKeyReleased

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
            rowData[9] = false;

            DataModelDesc.addRow(rowData);
            Updating = false;
            TableDesc.changeSelection(TableDesc.getRowCount() - 1, 1, false, false);
            TableDesc.requestFocus();
        }
        ShowMessage("Search Product By press F1");
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
            if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
                OpgFinal.setEnabled(true);
            }
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
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(clsDiscRateMaster.ModuleID, PartyID)) {
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
        if (EditMode == 0) {
            frmFeltSendMail ObjSend = new frmFeltSendMail();
            //ObjSend.ModuleID=20+POType;
            ObjSend.ModuleID = 730;

            ObjSend.SentBy = EITLERPGLOBAL.gNewUserID;
            ObjSend.MailDocNo = txtMasterNo.getText().trim();

            try {
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

            String FileName = "doc" + ObjSend.ModuleID + txtMasterNo.getText() + ".pdf";
            ObjSend.theFile = FileName;
            //String suppEMail=clsSupplier.getSupplierEMail(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());

            //KOMAL & TO/CC/BCC - SET ENABLE FALSE IN FRMSENDMAIL FORM
            /*
             if(suppEMail.trim().equals("")) {
             return;
             }
             */
            //KOMAL
            ObjSend.colRecList.clear();
            //ObjSend.colRecList.put(Integer.toString(ObjSend.colRecList.size()+1),suppEMail);

            int FinalApprover = 0;
            String strFinalApprover = "";
            int HierarchyID = (int) ObjSalesParty.getAttribute("HIERARCHY_ID").getVal();

            FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
            strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);
            /*
             if(HierarchyID==245) {
             strFinalApprover="A. B. Tewary";
             }
             if(HierarchyID==555||HierarchyID==488||HierarchyID==487) {
             strFinalApprover="Vinod Patel";
             }
             */
            try {
                //URL MailDocument=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptProforma.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&File="+FileName);
                //URL MailDocument=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptProforma.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&ProformaNo="+txtMasterNo.getText()+"&ProformaDate="+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText())+"&File="+FileName);
                URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/rptProformamail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&DocNo=" + txtMasterNo.getText() + "&ProformaDate=" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText()) + "&File=" + FileName);
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
        jPopupMenu1.show(cmdPreview, 0, 30);
        //PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void txtPartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartycodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            if (txtMasterNo.getText().startsWith("GD")) {
                LOV aList = new LOV();

                aList.SQL = "SELECT GROUP_CODE,GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE APPROVED=1 AND CANCELED=0 ORDER BY GROUP_CODE";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;

                if (aList.ShowLOV()) {
                    txtPartycode.setText(aList.ReturnVal);
                    txtPartyname.setText(clsDiscRateMaster.getGroupName(aList.ReturnVal));
                }
            } else {
                LOV aList = new LOV();

//                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' ORDER BY PARTY_NAME";
                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CLOSE_IND!=1 ORDER BY PARTY_NAME";
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
        txtPartycode.setEditable(true);
        //txttarget.setEditable(true);
        jlbl.setVisible(true);
        //jlbl2.setVisible(false);
        txtEffectFrom.setEditable(true);
        txtEffectTo.setEditable(true);
        txtSnctnDt.setEditable(true);
        Add();
        //      GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        txtPartycode.setEditable(false);
        //txttarget.setEditable(true);
        jlbl.setVisible(true);
        //jlbl2.setVisible(false);
//        txtEffectFrom.setEditable(false);
//        txtEffectTo.setEditable(false);
        txtSnctnDt.setEditable(true);
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

        if (txtEffectFrom.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Effective From Date");
            return;
        }
        if (!EITLERPGLOBAL.isDate(txtEffectFrom.getText())) {
            JOptionPane.showMessageDialog(null, "Please enter Valid Effective From Date");
            return;
        }
        if (txtEffectTo.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Effective To Date");
            return;
        }
        if (!EITLERPGLOBAL.isDate(txtEffectTo.getText())) {
            JOptionPane.showMessageDialog(null, "Please enter Valid Effective To Date");
            return;
        }

        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            if (txtMasterNo.getText().startsWith("GD")) {
                String strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE GROUP_CODE='" + txtPartycode.getText().trim() + "' AND MASTER_NO NOT IN ('" + txtMasterNo.getText().trim() + "') AND APPROVED=0 AND CANCELED=0 "; //AND EFFECTIVE_TO IS NULL AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'
                System.out.println(strSQL);
                ResultSet rsTmp = data.getResult(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) //Item is Approved
                {
                    JOptionPane.showMessageDialog(null, "Document Pending for Final Approval for this GROUP CODE : '" + txtPartycode.getText() + "'. Please Final Approve earlier record.");
                    Cancel();
                } else {

                    p = new String[TableDesc.getRowCount()];
                    for (int i = 0; i < p.length; i++) {
                        p[i] = (String) TableDesc.getValueAt(i, 1);
                        System.out.println("Array List " + p[i]);

                        //String str="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'";
                        String str = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE GROUP_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "'";
                        System.out.println(str);
                        ResultSet rsStr = data.getResult(str);
                        rsStr.first();

                        if (rsStr.getInt("COUNT") > 0) //Item is Approved
                        {

                            int conf = JOptionPane.showConfirmDialog(null, "This sanction will cancelled all further dates records for PRODUCT CODE : '" + p[i] + "' for GROUP CODE : '" + txtPartycode.getText() + "'. DO YOU WANT TO SAVE THIS RECORD.");

                            if (conf == 0) {
                                int fconf = JOptionPane.showConfirmDialog(null, "ARE YOU DEFINITLY SURE TO SAVE THIS RECORD.");

                                try {
                                    //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                    data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET CANCELED=1 WHERE GROUP_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "'");
                                    //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                    data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "',1) WHERE GROUP_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELED=0 AND (EFFECTIVE_TO IS NULL OR EFFECTIVE_TO>'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "') AND EFFECTIVE_FROM<'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "'");
                                    //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                    //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                } catch (Exception a) {
                                    a.printStackTrace();
                                }
                            } else {

                            }
                            //Cancel();
                        } else {
                            try {
                                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "',1) WHERE GROUP_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELED=0 AND (EFFECTIVE_TO IS NULL OR EFFECTIVE_TO>'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "') AND EFFECTIVE_FROM<'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "'");
                            } catch (Exception a) {
                                a.printStackTrace();
                            }
                        }
                    }

                    Save();
                }
            } else {
                String strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND MASTER_NO NOT IN ('" + txtMasterNo.getText().trim() + "') AND APPROVED=0 AND CANCELED=0 "; //AND EFFECTIVE_TO IS NULL AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'
                System.out.println(strSQL);
                //rsTmp=tmpStmt.executeQuery(strSQL);
                //rsTmp.first();
                ResultSet rsTmp = data.getResult(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) //Item is Approved
                {
                    JOptionPane.showMessageDialog(null, "Document Pending for Final Approval for this PARTY CODE : '" + txtPartycode.getText() + "'. Please Final Approve earlier record.");
                    Cancel();
                } else {

                    p = new String[TableDesc.getRowCount()];
                    for (int i = 0; i < p.length; i++) {
                        p[i] = (String) TableDesc.getValueAt(i, 1);
                        System.out.println("Array List " + p[i]);

                        //}
                        //String str="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'";
                        String str = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "'";
                        System.out.println(str);
                        ResultSet rsStr = data.getResult(str);
                        rsStr.first();

                        if (rsStr.getInt("COUNT") > 0) //Item is Approved
                        {

//                        try{
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND PRODUCT_CODE='"+p[i]+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND PRODUCT_CODE='"+p[i]+"' AND APPROVED=1 AND CANCELED=0 AND (EFFECTIVE_TO IS NULL OR EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"') AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                            //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
//                        }
//                        catch(Exception a){
//                            a.printStackTrace();
//                        }
                            int conf = JOptionPane.showConfirmDialog(null, "This sanction will cancelled all further dates records for PRODUCT CODE : '" + p[i] + "' for PARTY CODE : '" + txtPartycode.getText() + "'. DO YOU WANT TO SAVE THIS RECORD.");

                            if (conf == 0) {
                                int fconf = JOptionPane.showConfirmDialog(null, "ARE YOU DEFINITLY SURE TO SAVE THIS RECORD.");

                                try {
                                    //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                    data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET CANCELED=1 WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "'");
                                    //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                    data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "',1) WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELED=0 AND (EFFECTIVE_TO IS NULL OR EFFECTIVE_TO>'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "') AND EFFECTIVE_FROM<'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "'");
                                    //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                    //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                } catch (Exception a) {
                                    a.printStackTrace();
                                }
                            } else {

                            }
                            //Cancel();
                        } else {
                            try {
                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND PRODUCT_CODE='"+p[i]+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET EFFECTIVE_TO=SUBDATE('" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "',1) WHERE PARTY_CODE='" + txtPartycode.getText().trim() + "' AND PRODUCT_CODE='" + p[i] + "' AND APPROVED=1 AND CANCELED=0 AND (EFFECTIVE_TO IS NULL OR EFFECTIVE_TO>'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "') AND EFFECTIVE_FROM<'" + EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim()) + "'");
                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET CANCELED=1 WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SET EFFECTIVE_TO=SUBDATE('"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"',1) WHERE PARTY_CODE='"+txtPartycode.getText().trim()+"' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_TO>'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"' AND EFFECTIVE_FROM<'"+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText().trim())+"'");
                            } catch (Exception a) {
                                a.printStackTrace();
                            }
                        }
                    }

                    Save();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Save();
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
        ObjSalesParty.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void jMenuItemGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGroupActionPerformed
        // TODO add your handling code here:
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }

        //----------------------------------//
        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 730;
        aList.FirstFreeNo = 232;

        //if(aList.ShowList()) {
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        GenerateCombos();
        FormatGrid();
        jLabel8.setVisible(false);
        jLabel9.setVisible(true);
        //            chkOtherparty.setSelected(false);
        //            txtOtherPartycode.setEnabled(false);
        SelPrefix = aList.Prefix; //Selected Prefix;
        SelSuffix = aList.Suffix;
        FFNo = aList.FirstFreeNo;
        SetupApproval();
        //Display newly generated document no.
        txtMasterNo.setText(clsDiscRateMaster.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 730, FFNo, false));
        txtPartycode.requestFocus();

        lblTitle.setText("FELT DISCOUNT MASTER - " + txtMasterNo.getText());
        lblTitle.setBackground(Color.GRAY);
        //        }
        //        else {
        //            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        //        }
    }//GEN-LAST:event_jMenuItemGroupActionPerformed


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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItemAll;
    private javax.swing.JMenuItem jMenuItemGroup;
    private javax.swing.JMenuItem jMenuItemParty;
    private javax.swing.JMenuItem jMenuItemPiece;
    private javax.swing.JMenuItem jMenuItemProd;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlbl;
    private javax.swing.JLabel jlbl2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUnadjNo;
    private javax.swing.JLabel lblremark1;
    private javax.swing.JLabel lblremark2;
    private javax.swing.JLabel lblremark3;
    private javax.swing.JLabel lblremark4;
    private javax.swing.JLabel lblremark5;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtEffectFrom;
    private javax.swing.JTextField txtEffectTo;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtMasterNo;
    private javax.swing.JTextField txtPartycode;
    private javax.swing.JTextField txtPartyname;
    private javax.swing.JTextField txtSnctnDt;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtremark1;
    private javax.swing.JTextField txtremark2;
    private javax.swing.JTextField txtremark3;
    private javax.swing.JTextField txtremark4;
    private javax.swing.JTextField txtremark5;
    // End of variables declaration//GEN-END:variables

    private void Add() {

        jPopupMenu.show(cmdNew, 0, 30);
        //        //== Financial Year Validation-------------//
        //        if(!EITLERPGLOBAL.YearIsOpen) {
        //            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
        //            return;
        //        }
        //
        //        //----------------------------------//
        //        //Now Generate new document no.
        //        SelectFirstFree aList=new SelectFirstFree();
        //        aList.ModuleID=730;
        //
        //        if(aList.ShowList()) {
        //            EditMode=EITLERPGLOBAL.ADD;
        //            SetFields(true);
        //            DisableToolbar();
        //            ClearFields();
        //            //            chkOtherparty.setSelected(false);
        //            //            txtOtherPartycode.setEnabled(false);
        //            SelPrefix=aList.Prefix; //Selected Prefix;
        //            SelSuffix=aList.Suffix;
        //            FFNo=aList.FirstFreeNo;
        //            SetupApproval();
        //            //Display newly generated document no.
        //            txtMasterNo.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 730, FFNo,  false));
        //            txtPartycode.requestFocus();
        //
        //            lblTitle.setText("FELT DISCOUNT MASTER - "+txtMasterNo.getText());
        //            lblTitle.setBackground(Color.BLUE);
        //        }
        //        else {
        //            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        //        }

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

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(clsDiscRateMaster.ModuleID, (String) ObjSalesParty.getAttribute("MASTER_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(clsDiscRateMaster.ModuleID, FromUserID, (String) ObjSalesParty.getAttribute("MASTER_NO").getObj());

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
            if (clsFeltProductionApprovalFlow.IsCreator(clsDiscRateMaster.ModuleID, txtMasterNo.getText())) {
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

                String ProformaNo = (String) ObjSalesParty.getAttribute("MASTER_NO").getObj();

                List = clsFeltProductionApprovalFlow.getRemainingUsers(clsDiscRateMaster.ModuleID, ProformaNo);
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
        txtMasterNo.setEnabled(pStat);
        txtEffectFrom.setEnabled(pStat);
        txtEffectTo.setEnabled(pStat);
        txtEffectFrom.setEditable(pStat);
        txtEffectTo.setEditable(pStat);
        //txttarget.setEnabled(pStat);
        txtSnctnDt.setEnabled(pStat);

        cmdAdd.setEnabled(pStat);
        cmdItemdelete.setEnabled(pStat);
        cmdEditPiecedetail.setEnabled(pStat);
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

        txtremark1.setEditable(pStat);
        txtremark2.setEditable(pStat);
        txtremark3.setEditable(pStat);
        txtremark4.setEditable(pStat);
        txtremark5.setEditable(pStat);

        TableDesc.setEnabled(pStat);
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
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='" + txtMasterNo.getText().trim() + "'")) {
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
        //txtPartystation.setText("");
        txtMasterNo.setText("");
        //txtEffectFrom.setText(EITLERPGLOBAL.getCurrentDate());
        txtEffectFrom.setText("");
        txtEffectTo.setText("");
        //txttarget.setText("");
        jlbl.setText("");
        //jlbl2.setText("");
        txtSnctnDt.setText("");
        //        txtContact.setText("");
        //        txtPhone.setText("");

        txtremark1.setText("");
        txtremark2.setText("");
        txtremark3.setText("");
        txtremark4.setText("");
        txtremark5.setText("");

        txtFromRemarks.setText("");
        txtToRemarks.setText("");

        //FormatGrid();
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
        String lProformaNo = ObjSalesParty.getAttribute("MASTER_NO").getString();
        if (ObjSalesParty.IsEditable(EITLERPGLOBAL.gCompanyID, lProformaNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

//            if (clsFeltProductionApprovalFlow.IsCreator(clsDiscRateMaster.ModuleID, lProformaNo) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 8022, 80222)) {
            if (clsFeltProductionApprovalFlow.IsCreator(clsDiscRateMaster.ModuleID, lProformaNo)) {
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

        String lDocNo = (String) ObjSalesParty.getAttribute("MASTER_NO").getObj();

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
        String partyCode = txtPartycode.getText();

        if (txtSnctnDt.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Sanction Date");
            return;
        }
        if (txtEffectFrom.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Effective From Date");
            return;
        }
        if (!EITLERPGLOBAL.isDate(txtEffectFrom.getText())) {
            JOptionPane.showMessageDialog(null, "Please enter Valid Effective From Date");
            return;
        }
        if (txtEffectTo.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Effective To Date");
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

        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + partyCode + "' ")) {
            JOptionPane.showMessageDialog(null, "Party closed in Party Master.");
            return;
        }

        for (int j = 0; j < TableDesc.getRowCount(); j++) {
            String machineNo = ((String) TableDesc.getValueAt(j, 2)).trim();
            String positionNo = ((String) TableDesc.getValueAt(j, 3)).trim();

            if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MACHINE_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCode + "' AND MM_MACHINE_NO='" + machineNo + "' ")) {
                JOptionPane.showMessageDialog(null, "Party Machine closed in Machine Master at Row : " + (j + 1));
                return;
            } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE POSITION_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCode + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "' ")) {
                JOptionPane.showMessageDialog(null, "Party Machine Position closed in Machine Master at Row : " + (j + 1));
                return;
            }

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

            if (ObjSalesParty.Insert()) {
                // MoveLast();

//                if (OpgFinal.isSelected()) {
//                        try{
//                            String DOC_NO = txtPartycode.getText();
//                            String DOC_DATE = txtSnctnDt.getText();
//                            String Party_Code = txtPartycode.getText();
//                            int Module_Id = clsDiscRateMaster.ModuleID;
//                            
//                            String responce = JavaMail.sendFinalApprovalMail(Module_Id, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
//                            System.out.println("Send Mail Responce : "+responce); 
//
//                        }catch(Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                }
                if (OpgFinal.isSelected()) {

                    String docNo = txtMasterNo.getText();
                    String docDt = txtSnctnDt.getText();
                    String pCode = txtPartycode.getText();
                    String effectiveFrom = EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText());
                    String effectiveTo = EITLERPGLOBAL.formatDateDB(txtEffectTo.getText());

                    try {
                        Connection conn = data.getConn();
                        Statement stmt = conn.createStatement();

                        stmt.execute("TRUNCATE PRODUCTION.AUTO_UNADJ_DATA");

                        if (docNo.startsWith("F")) {
                            stmt.execute("INSERT INTO PRODUCTION.AUTO_UNADJ_DATA (INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,INVOICE_AMOUNT,SEAM_CHARGE,DISC_GROUP_CODE,DOC_NO,ENTRY_DATE,DISC_IGST_PER,DISC_CGST_PER,DISC_SGST_PER,SEAM_IGST_PER,SEAM_CGST_PER,SEAM_SGST_PER) "
                                    + "SELECT INVOICE_NO,INVOICE_DATE,PRODUCT_CODE,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,BAS_AMT,SQMTR,ACTUAL_WEIGHT,GROSS_AMT,DISC_AMT,NET_AMT,SEAM_CHG,'','" + docNo + "',CURDATE(),IGST_PER,CGST_PER,SGST_PER,IGST_PER,CGST_PER,SGST_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + effectiveFrom + "' AND INVOICE_DATE<='" + effectiveTo + "' AND APPROVED=1 AND CANCELLED=0 "
                                    + "AND PARTY_CODE IN ('" + pCode + "') ");
                        }

                        if (docNo.startsWith("G")) {
                            stmt.execute("INSERT INTO PRODUCTION.AUTO_UNADJ_DATA (INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,INVOICE_AMOUNT,SEAM_CHARGE,DISC_GROUP_CODE,DOC_NO,ENTRY_DATE,DISC_IGST_PER,DISC_CGST_PER,DISC_SGST_PER,SEAM_IGST_PER,SEAM_CGST_PER,SEAM_SGST_PER) "
                                    + "SELECT INVOICE_NO,INVOICE_DATE,PRODUCT_CODE,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,BAS_AMT,SQMTR,ACTUAL_WEIGHT,GROSS_AMT,DISC_AMT,NET_AMT,SEAM_CHG,'" + pCode + "','" + docNo + "',CURDATE(),IGST_PER,CGST_PER,SGST_PER,IGST_PER,CGST_PER,SGST_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + effectiveFrom + "' AND INVOICE_DATE<='" + effectiveTo + "' AND APPROVED=1 AND CANCELLED=0 "
                                    + "AND PARTY_CODE IN ( SELECT DISTINCT D.PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.GROUP_CODE='" + pCode + "') ");
                        }

                        if (docNo.startsWith("P")) {
                            stmt.execute("INSERT INTO PRODUCTION.AUTO_UNADJ_DATA (INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,INVOICE_AMOUNT,SEAM_CHARGE,DISC_GROUP_CODE,DOC_NO,ENTRY_DATE,DISC_IGST_PER,DISC_CGST_PER,DISC_SGST_PER,SEAM_IGST_PER,SEAM_CGST_PER,SEAM_SGST_PER) "
                                    + "SELECT INVOICE_NO,INVOICE_DATE,PRODUCT_CODE,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,BAS_AMT,SQMTR,ACTUAL_WEIGHT,GROSS_AMT,DISC_AMT,NET_AMT,SEAM_CHG,'','" + docNo + "',CURDATE(),IGST_PER,CGST_PER,SGST_PER,IGST_PER,CGST_PER,SGST_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + effectiveFrom + "' AND INVOICE_DATE<='" + effectiveTo + "' AND APPROVED=1 AND CANCELLED=0 "
                                    + "AND PARTY_CODE IN ('" + pCode + "') AND PIECE_NO IN (SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='" + docNo + "') ");
                        }

                        stmt.execute("DELETE FROM PRODUCTION.AUTO_UNADJ_DATA WHERE CONCAT(INVOICE_NO,INVOICE_DATE) IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE) FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=1 AND H.CANCELED=0) ");

                        if (docNo.startsWith("P")) {
                            stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA AS TD, PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE TD.DOC_NO='" + docNo + "' AND DE.MASTER_NO='" + docNo + "' AND DE.PIECE_NO=TD.PIECE_NO AND DE.APPROVED=1 AND DE.CANCELED=0 ");
                        } else {
                            stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA AS TD, PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE TD.DOC_NO='" + docNo + "' AND DE.MASTER_NO='" + docNo + "' AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.APPROVED=1 AND DE.CANCELED=0 ");
                        }
                        
                        stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/TOTAL_GROSS),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/TOTAL_GROSS)),2),INV_SEAM_CHARGES=ROUND(SEAM_CHARGE,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND((TOTAL_GROSS*DISC_PER)/100-TRD_DISCOUNT,2),SEAM_CHARGE_AMT=ROUND(SEAM_CHARGE-WIDTH*(4899*(100-SEAM_VALUE)/100),2) WHERE DOC_NO='" + docNo + "' ");

                        stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA SET DISC_IGST_AMT=ROUND(DISC_AMT*(DISC_IGST_PER/100),0),DISC_CGST_AMT=ROUND(DISC_AMT*(DISC_CGST_PER/100),0),DISC_SGST_AMT=ROUND(DISC_AMT*(DISC_SGST_PER/100),0),TOTAL_DISC_AMT_GST=ROUND(DISC_AMT+DISC_IGST_AMT+DISC_CGST_AMT+DISC_SGST_AMT,2)");

                        stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA SET SEAM_IGST_AMT=ROUND(SEAM_CHARGE_AMT*(SEAM_IGST_PER/100),0),SEAM_CGST_AMT=ROUND(SEAM_CHARGE_AMT*(SEAM_CGST_PER/100),0),SEAM_SGST_AMT=ROUND(SEAM_CHARGE_AMT*(SEAM_SGST_PER/100),0),TOTAL_SEAM_AMT_GST=ROUND(SEAM_CHARGE_AMT+SEAM_IGST_AMT+SEAM_CGST_AMT+SEAM_SGST_AMT,2)");

                        stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA SET TOTAL_DISC_AMT=ROUND(DISC_AMT+SEAM_CHARGE_AMT,2) WHERE DOC_NO='" + docNo + "' ");

                        Auto_Unadjusted_Data(effectiveFrom, effectiveTo);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    PieceRegisterValueUpdate(docNo, pCode);
                    MachineMasterValueUpdate(docNo, pCode);
                }

                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjSalesParty.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjSalesParty.Update()) {

//                if (OpgFinal.isSelected()) {
//                        try{
//                            String DOC_NO = txtPartycode.getText();
//                            String DOC_DATE = txtSnctnDt.getText();
//                            String Party_Code = txtPartycode.getText();
//                            int Module_Id = clsDiscRateMaster.ModuleID;
//                            
//                            String responce = JavaMail.sendFinalApprovalMail(Module_Id, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
//                            System.out.println("Send Mail Responce : "+responce); 
//
//                        }catch(Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                }
                if (OpgFinal.isSelected()) {

                    String docNo = txtMasterNo.getText();
                    String docDt = txtSnctnDt.getText();
                    String pCode = txtPartycode.getText();
                    String effectiveFrom = EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText());
                    String effectiveTo = EITLERPGLOBAL.formatDateDB(txtEffectTo.getText());

                    try {
                        Connection conn = data.getConn();
                        Statement stmt = conn.createStatement();

                        stmt.execute("TRUNCATE PRODUCTION.AUTO_UNADJ_DATA");

                        if (docNo.startsWith("F")) {
                            stmt.execute("INSERT INTO PRODUCTION.AUTO_UNADJ_DATA (INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,INVOICE_AMOUNT,SEAM_CHARGE,DISC_GROUP_CODE,DOC_NO,ENTRY_DATE,DISC_IGST_PER,DISC_CGST_PER,DISC_SGST_PER,SEAM_IGST_PER,SEAM_CGST_PER,SEAM_SGST_PER) "
                                    + "SELECT INVOICE_NO,INVOICE_DATE,PRODUCT_CODE,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,BAS_AMT,SQMTR,ACTUAL_WEIGHT,GROSS_AMT,DISC_AMT,NET_AMT,SEAM_CHG,'','" + docNo + "',CURDATE(),IGST_PER,CGST_PER,SGST_PER,IGST_PER,CGST_PER,SGST_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + effectiveFrom + "' AND INVOICE_DATE<='" + effectiveTo + "' AND APPROVED=1 AND CANCELLED=0 "
                                    + "AND PARTY_CODE IN ('" + pCode + "') ");
                        }

                        if (docNo.startsWith("G")) {
                            stmt.execute("INSERT INTO PRODUCTION.AUTO_UNADJ_DATA (INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,INVOICE_AMOUNT,SEAM_CHARGE,DISC_GROUP_CODE,DOC_NO,ENTRY_DATE,DISC_IGST_PER,DISC_CGST_PER,DISC_SGST_PER,SEAM_IGST_PER,SEAM_CGST_PER,SEAM_SGST_PER) "
                                    + "SELECT INVOICE_NO,INVOICE_DATE,PRODUCT_CODE,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,BAS_AMT,SQMTR,ACTUAL_WEIGHT,GROSS_AMT,DISC_AMT,NET_AMT,SEAM_CHG,'" + pCode + "','" + docNo + "',CURDATE(),IGST_PER,CGST_PER,SGST_PER,IGST_PER,CGST_PER,SGST_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + effectiveFrom + "' AND INVOICE_DATE<='" + effectiveTo + "' AND APPROVED=1 AND CANCELLED=0 "
                                    + "AND PARTY_CODE IN ( SELECT DISTINCT D.PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.GROUP_CODE='" + pCode + "') ");
                        }

                        if (docNo.startsWith("P")) {
                            stmt.execute("INSERT INTO PRODUCTION.AUTO_UNADJ_DATA (INVOICE_NO,INVOICE_DATE,QUALITY_NO,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,TOTAL_GROSS,GROSS_SQ_MTR,GROSS_KG,GROSS_AMOUNT,TRD_DISCOUNT,INVOICE_AMOUNT,SEAM_CHARGE,DISC_GROUP_CODE,DOC_NO,ENTRY_DATE,DISC_IGST_PER,DISC_CGST_PER,DISC_SGST_PER,SEAM_IGST_PER,SEAM_CGST_PER,SEAM_SGST_PER) "
                                    + "SELECT INVOICE_NO,INVOICE_DATE,PRODUCT_CODE,PIECE_NO,PARTY_CODE,PARTY_NAME,LENGTH,WIDTH,RATE,BAS_AMT,SQMTR,ACTUAL_WEIGHT,GROSS_AMT,DISC_AMT,NET_AMT,SEAM_CHG,'','" + docNo + "',CURDATE(),IGST_PER,CGST_PER,SGST_PER,IGST_PER,CGST_PER,SGST_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + effectiveFrom + "' AND INVOICE_DATE<='" + effectiveTo + "' AND APPROVED=1 AND CANCELLED=0 "
                                    + "AND PARTY_CODE IN ('" + pCode + "') AND PIECE_NO IN (SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='" + docNo + "') ");
                        }

                        stmt.execute("DELETE FROM PRODUCTION.AUTO_UNADJ_DATA WHERE CONCAT(INVOICE_NO,INVOICE_DATE) IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE) FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND H.APPROVED=1 AND H.CANCELED=0) ");

                        if (docNo.startsWith("P")) {
                            stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA AS TD, PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE TD.DOC_NO='" + docNo + "' AND DE.MASTER_NO='" + docNo + "' AND DE.PIECE_NO=TD.PIECE_NO AND DE.APPROVED=1 AND DE.CANCELED=0 ");
                        } else {
                            stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA AS TD, PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL DE SET TD.DISC_PER=DE.DISC_PER,TD.EFFECTIVE_FROM=DE.EFFECTIVE_FROM,TD.EFFECTIVE_TO=DE.EFFECTIVE_TO,TD.SEAM_VALUE=DE.SEAM_VALUE WHERE TD.DOC_NO='" + docNo + "' AND DE.MASTER_NO='" + docNo + "' AND SUBSTRING(DE.PRODUCT_CODE,1,6)=SUBSTRING(TD.QUALITY_NO,1,6) AND DE.APPROVED=1 AND DE.CANCELED=0 ");
                        }
                        
                        stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA SET INV_DISC_PER=ROUND((TRD_DISCOUNT*100/TOTAL_GROSS),2),SANC_DISC_PER=ROUND(DISC_PER,2),WORK_DISC_PER=ROUND((DISC_PER-(TRD_DISCOUNT*100/TOTAL_GROSS)),2),INV_SEAM_CHARGES=ROUND(SEAM_CHARGE,2),SANC_SEAM_CHARGES=ROUND(SEAM_VALUE,2),DISC_AMT=ROUND((TOTAL_GROSS*DISC_PER)/100-TRD_DISCOUNT,2),SEAM_CHARGE_AMT=ROUND(SEAM_CHARGE-WIDTH*(4899*(100-SEAM_VALUE)/100),2) WHERE DOC_NO='" + docNo + "' ");

                        stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA SET DISC_IGST_AMT=ROUND(DISC_AMT*(DISC_IGST_PER/100),0),DISC_CGST_AMT=ROUND(DISC_AMT*(DISC_CGST_PER/100),0),DISC_SGST_AMT=ROUND(DISC_AMT*(DISC_SGST_PER/100),0),TOTAL_DISC_AMT_GST=ROUND(DISC_AMT+DISC_IGST_AMT+DISC_CGST_AMT+DISC_SGST_AMT,2)");

                        stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA SET SEAM_IGST_AMT=ROUND(SEAM_CHARGE_AMT*(SEAM_IGST_PER/100),0),SEAM_CGST_AMT=ROUND(SEAM_CHARGE_AMT*(SEAM_CGST_PER/100),0),SEAM_SGST_AMT=ROUND(SEAM_CHARGE_AMT*(SEAM_SGST_PER/100),0),TOTAL_SEAM_AMT_GST=ROUND(SEAM_CHARGE_AMT+SEAM_IGST_AMT+SEAM_CGST_AMT+SEAM_SGST_AMT,2)");

                        stmt.execute("UPDATE PRODUCTION.AUTO_UNADJ_DATA SET TOTAL_DISC_AMT=ROUND(DISC_AMT+SEAM_CHARGE_AMT,2) WHERE DOC_NO='" + docNo + "' ");

                        Auto_Unadjusted_Data(effectiveFrom, effectiveTo);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    PieceRegisterValueUpdate(docNo, pCode);
                    MachineMasterValueUpdate(docNo, pCode);
                }

                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. \nError is " + ObjSalesParty.LastError);
                return;
            }
        }

        Notification();
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
        Loader ObjLoader = new Loader(this, "EITLERP.Production.FeltDiscRateMaster.frmPartyCodeFind", true);
        frmPartyCodeFind ObjReturn = (frmPartyCodeFind) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            if (!ObjSalesParty.Filter(ObjReturn.stringFindQuery)) {
                JOptionPane.showMessageDialog(null, "No records found.");
            }
//            MoveFirst();
            MoveLast();
        }
    }

    public void Find(String docNo) {
        ObjSalesParty.Filter(" MASTER_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    public void FindDisc(String docNo) {
        ObjSalesParty.Filter(" PARTY_CODE='" + docNo + "'");
        //ObjSalesParty.MoveFirst();
        ObjSalesParty.MoveLast();
        DisplayData();
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
                }

                if (ObjSalesParty.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                    cmdEmail.setEnabled(false);
                }

                if (ObjSalesParty.getAttribute("CANCELED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                    cmdEmail.setEnabled(false);
                }
            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = clsDiscRateMaster.ModuleID;

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
            lblTitle.setText("FELT DISCOUNT MASTER - " + (String) ObjSalesParty.getAttribute("MASTER_NO").getObj());
            if (ObjSalesParty.getAttribute("MASTER_NO").getObj().toString().startsWith("GD")) {
                txtPartycode.setText((String) ObjSalesParty.getAttribute("GROUP_CODE").getObj());
                txtPartyname.setText((String) ObjSalesParty.getAttribute("GROUP_NAME").getObj());
            } else {
                txtPartycode.setText((String) ObjSalesParty.getAttribute("PARTY_CODE").getObj());
                txtPartyname.setText((String) ObjSalesParty.getAttribute("PARTY_NAME").getObj());
            }
            //txttarget.setText((String)ObjSalesParty.getAttribute("TURN_OVER_TARGET").getObj());
            txtMasterNo.setText((String) ObjSalesParty.getAttribute("MASTER_NO").getObj());
            txtSnctnDt.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("SANCTION_DATE").getString()));
            txtEffectFrom.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("EFFECTIVE_FROM").getString()));
            txtEffectTo.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("EFFECTIVE_TO").getString()));

            txtremark1.setText(ObjSalesParty.getAttribute("REMARK1").getString());
            txtremark2.setText(ObjSalesParty.getAttribute("REMARK2").getString());
            txtremark3.setText(ObjSalesParty.getAttribute("REMARK3").getString());
            txtremark4.setText(ObjSalesParty.getAttribute("REMARK4").getString());
            txtremark5.setText(ObjSalesParty.getAttribute("REMARK5").getString());

            String master = (String) ObjSalesParty.getAttribute("MASTER_NO").getObj();
            String mst = "";
            if (!master.equals("")) {
                mst = master.substring(0, 2);
            }
            ResultSet cnt = data.getResult("SELECT SUM(TURN_OVER_TARGET) AS A FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='" + master + "'");
            //jlbl2.setText(cnt.getString("A"));
            jlbl.setText(cnt.getString("A"));

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());

            DoNotEvaluate = true;
            //===================Fill up Table===================//

            if (mst.matches("")) {
                FormatGrid();
            }

            if (mst.matches("FD")) {
                FormatGrid();
                jLabel8.setVisible(true);
                jLabel9.setVisible(false);
            }
            if (mst.matches("PC")) {
                FormatGrid1();
                jLabel8.setVisible(true);
                jLabel9.setVisible(false);
            }
            if (mst.matches("GD")) {
                FormatGrid();
                jLabel8.setVisible(false);
                jLabel9.setVisible(true);
            }
            //Now Generate Table
            for (int i = 1; i <= ObjSalesParty.colMRItems.size(); i++) {

                clsDiscRateMasterItem ObjItem = (clsDiscRateMasterItem) ObjSalesParty.colMRItems.get(Integer.toString(i));
                Object[] rowData = new Object[30];
                //txtEffectTo.setText(EITLERPGLOBAL.formatDate(ObjItem.getAttribute("EFFECTIVE_TO").getString()));
                rowData[0] = Integer.toString(i);
                //rowData[1]=(String)ObjItem.getAttribute("PRODUCT_CODE").getObj();

                if (mst.matches("FD") || mst.matches("GD")) {
                    rowData[1] = (String) ObjItem.getAttribute("PRODUCT_CODE").getObj();
                }
                if (mst.matches("PC")) {
                    rowData[1] = (String) ObjItem.getAttribute("PIECE_NO").getObj();
                }

                rowData[2] = (String) ObjItem.getAttribute("MACHINE_NO").getObj();
                rowData[3] = (String) ObjItem.getAttribute("MACHINE_POSITION").getObj();
                rowData[4] = Double.toString(ObjItem.getAttribute("DISC_PER").getVal());
                rowData[5] = Double.toString(ObjItem.getAttribute("SEAM_VALUE").getVal());
                rowData[6] = Double.toString(ObjItem.getAttribute("YRED_DISC_PER").getVal());
                rowData[7] = Double.toString(ObjItem.getAttribute("YRED_SEAM_VALUE").getVal());
                rowData[8] = (String) ObjItem.getAttribute("TURN_OVER_TARGET").getObj();
                System.out.println("FLAG : " + ObjItem.getAttribute("DIVERSION_FLAG").getString());
                if (ObjItem.getAttribute("DIVERSION_FLAG").getString().equalsIgnoreCase("1")) {
                    rowData[9] = true;
                } else {
                    rowData[9] = false;
                }

                DataModelDesc.addRow(rowData);
            }

            DoNotEvaluate = false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String MasterNo = ObjSalesParty.getAttribute("MASTER_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(clsDiscRateMaster.ModuleID, MasterNo);
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
            HashMap History = clsDiscRateMaster.getHistoryList(EITLERPGLOBAL.gCompanyID, MasterNo);
            for (int i = 1; i <= History.size(); i++) {
                clsDiscRateMaster ObjHistory = (clsDiscRateMaster) History.get(Integer.toString(i));
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
        ObjSalesParty.setAttribute("PREFIX", SelPrefix);
        ObjSalesParty.setAttribute("SUFFIX", SelSuffix);
        ObjSalesParty.setAttribute("FFNO", FFNo);
        ObjSalesParty.setAttribute("MASTER_NO", txtMasterNo.getText());
        ObjSalesParty.setAttribute("SANCTION_DATE", EITLERPGLOBAL.formatDateDB(txtSnctnDt.getText()));
        ObjSalesParty.setAttribute("EFFECTIVE_FROM", EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText()));
        if (txtEffectTo.getText().equalsIgnoreCase("")) {
            ObjSalesParty.setAttribute("EFFECTIVE_TO", null);
        } else {
            ObjSalesParty.setAttribute("EFFECTIVE_TO", EITLERPGLOBAL.formatDateDB(txtEffectTo.getText()));
        }
        if (txtMasterNo.getText().startsWith("GD")) {
            ObjSalesParty.setAttribute("GROUP_CODE", txtPartycode.getText());
            ObjSalesParty.setAttribute("GROUP_NAME", txtPartyname.getText());
            ObjSalesParty.setAttribute("PARTY_CODE", "");
            ObjSalesParty.setAttribute("PARTY_NAME", "");
        } else {
            ObjSalesParty.setAttribute("PARTY_CODE", txtPartycode.getText());
            ObjSalesParty.setAttribute("PARTY_NAME", txtPartyname.getText());
            ObjSalesParty.setAttribute("GROUP_CODE", "");
            ObjSalesParty.setAttribute("GROUP_NAME", "");
        }

        //ObjSalesParty.setAttribute("TURN_OVER_TARGET",txttarget.getText());
        //ObjSalesParty.setAttribute("STATION",txtPartystation.getText());
        //        ObjSalesParty.setAttribute("CONTACT",txtContact.getText());
        //        ObjSalesParty.setAttribute("PHONE",txtPhone.getText());
        //
        ObjSalesParty.setAttribute("REMARK1", txtremark1.getText());
        ObjSalesParty.setAttribute("REMARK2", txtremark2.getText());
        ObjSalesParty.setAttribute("REMARK3", txtremark3.getText());
        ObjSalesParty.setAttribute("REMARK4", txtremark4.getText());
        ObjSalesParty.setAttribute("REMARK5", txtremark5.getText());

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
            clsDiscRateMasterItem ObjItem = new clsDiscRateMasterItem();
            //String lItemID=(String)TableDesc.getValueAt(i, 1);

            ObjItem.setAttribute("SR_NO", i);
            ObjItem.setAttribute("PRODUCT_CODE", (String) TableDesc.getValueAt(i, 1));
            ObjItem.setAttribute("PIECE_NO", (String) TableDesc.getValueAt(i, 1));
            ObjItem.setAttribute("MACHINE_NO", (String) TableDesc.getValueAt(i, 2));
            ObjItem.setAttribute("MACHINE_POSITION", (String) TableDesc.getValueAt(i, 3));

            if (TableDesc.getValueAt(i, 4).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("DISC_PER", "0.0");
            } else {
                ObjItem.setAttribute("DISC_PER", Double.parseDouble((String) TableDesc.getValueAt(i, 4)));
            }
            if (TableDesc.getValueAt(i, 5).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("SEAM_VALUE", "0.0");
            } else {
                ObjItem.setAttribute("SEAM_VALUE", Double.parseDouble((String) TableDesc.getValueAt(i, 5)));
            }
            //ObjItem.setAttribute("SEAM_VALUE",Double.parseDouble((String)TableDesc.getValueAt(i,5)));
            if (TableDesc.getValueAt(i, 6).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("YRDE_DISC_PER", "0.0");
            } else {
                ObjItem.setAttribute("YRED_DISC_PER", Double.parseDouble((String) TableDesc.getValueAt(i, 6)));
            }
            if (TableDesc.getValueAt(i, 7).toString().equalsIgnoreCase("")) {
                ObjItem.setAttribute("YRED_SEAM_VALUE", "0.0");
            } else {
                ObjItem.setAttribute("YRED_SEAM_VALUE", Double.parseDouble((String) TableDesc.getValueAt(i, 7)));
            }
            //ObjItem.setAttribute("YRED_SEAM_VALUE",Double.parseDouble((String)TableDesc.getValueAt(i,7)));

            ObjItem.setAttribute("TURN_OVER_TARGET", (String) TableDesc.getValueAt(i, 8));

            String flg = String.valueOf(TableDesc.getValueAt(i, 9));
            flg = flg.toLowerCase();

            if (flg.equals("true")) {
                ObjItem.setAttribute("DIVERSION_FLAG", "1"); //3
            } else {
                ObjItem.setAttribute("DIVERSION_FLAG", "0"); //3
            }
            ObjSalesParty.colMRItems.put(Integer.toString(ObjSalesParty.colMRItems.size() + 1), ObjItem);
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

    //    private void FormatGridDiscount(){
    //        DataModelDiscount = new EITLTableModel();
    //        TableDiscount.removeAll();
    //        TableDiscount.setModel(DataModelDiscount);
    //
    //        DataModelDiscount.TableReadOnly(true);
    //        DataModelDiscount.addColumn("Sr.");
    //        DataModelDiscount.addColumn("Product Code");
    //        DataModelDiscount.addColumn("Discount %");
    //        DataModelDiscount.addColumn("Current Memo Date");
    //        DataModelDiscount.addColumn("No Of Pieces");
    //        TableDiscount.setAutoResizeMode(TableDiscount.AUTO_RESIZE_OFF);
    //
    //        TableDiscount.getColumnModel().getColumn(0).setMaxWidth(50);
    //    }
    //     private void FormatGridOtherpartyDiscount(){
    //        DataModelOtherpartyDiscount = new EITLTableModel();
    //        Tableotherpartydiscount.removeAll();
    //        Tableotherpartydiscount.setModel(DataModelOtherpartyDiscount);
    //
    //        DataModelOtherpartyDiscount.TableReadOnly(true);
    //        DataModelOtherpartyDiscount.addColumn("Sr.");
    //        DataModelOtherpartyDiscount.addColumn("Product Code");
    //        DataModelOtherpartyDiscount.addColumn("Discount %");
    //        DataModelOtherpartyDiscount.addColumn("Current Memo Date");
    //        DataModelOtherpartyDiscount.addColumn("No Of Pieces");
    //        Tableotherpartydiscount.setAutoResizeMode(Tableotherpartydiscount.AUTO_RESIZE_OFF);
    //
    //        Tableotherpartydiscount.getColumnModel().getColumn(0).setMaxWidth(50);
    //    }
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

                if (FieldName.trim().equals("MASTER_NO")) {
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
        //ObjSalesParty.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsDiscRateMaster.ModuleID+")");
        ObjSalesParty.Filter(" MASTER_NO IN (SELECT PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=730)");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pMasterNo, String pPieceNo) {
        System.out.println(pMasterNo);
        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjSalesParty.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjSalesParty.Filter(" MASTER_NO='" + pMasterNo + "'");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pMasterNo) {
        System.out.println(pMasterNo);
        //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjSalesParty.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjSalesParty.Filter(" MASTER_NO='" + pMasterNo + "'");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        String PartyCode = txtMasterNo.getText();

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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(clsDiscRateMaster.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(clsDiscRateMaster.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String MasterNo = (String) ObjSalesParty.getAttribute("MASTER_NO").getObj();
            int Creator = clsFeltProductionApprovalFlow.getCreator(clsDiscRateMaster.ModuleID, MasterNo);
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
            DataModelDesc.addColumn("Machine No");//2
            DataModelDesc.addColumn("Machine Position");//3
            DataModelDesc.addColumn("Basic Disc % in Bill");//4
            DataModelDesc.addColumn("Seam Disc % in Bill"); //5
            DataModelDesc.addColumn("Year End Basic Disc %");//6
            DataModelDesc.addColumn("Year End Seam Disc %"); //7
            DataModelDesc.addColumn("Turn Over"); //8
            DataModelDesc.addColumn("For Diversion"); //8

            DataModelDesc.SetVariable(0, "SR_NO");  //0
            DataModelDesc.SetVariable(1, "PRODUCT_CODE"); //1
            DataModelDesc.SetVariable(2, "MACHINE_NO"); //2
            DataModelDesc.SetVariable(3, "MACHINE_POSITION"); //3
            DataModelDesc.SetVariable(4, "DISC_PER"); //4
            DataModelDesc.SetVariable(5, "SEAM_VALUE"); //5
            DataModelDesc.SetVariable(6, "YRED_DISC_PER"); //6
            DataModelDesc.SetVariable(7, "YRED_SEAM_VALUE"); //7
            DataModelDesc.SetVariable(8, "TURN_OVER_TARGET"); //8
            DataModelDesc.SetVariable(9, "DIVERSION_FLAG"); //8

            DataModelDesc.SetReadOnly(0);
            DataModelDesc.SetReadOnly(1);
            DataModelDesc.SetReadOnly(2);
            DataModelDesc.SetReadOnly(3);

            int ImportCol = 9;
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
        //Table formatting completed

        TableDesc.getColumnModel().getColumn(0).setMinWidth(30);
        //TableDesc.getColumnModel().getColumn(0).setMaxWidth(30);
        TableDesc.getColumnModel().getColumn(1).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(1).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(2).setMinWidth(80);
        //TableDesc.getColumnModel().getColumn(2).setMaxWidth(80);
        TableDesc.getColumnModel().getColumn(3).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(3).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(4).setMinWidth(80);
        //TableDesc.getColumnModel().getColumn(4).setMaxWidth(80);
        TableDesc.getColumnModel().getColumn(5).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(5).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(6).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(6).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(7).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(7).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(8).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(8).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(9).setMinWidth(80);
        TableDesc.getColumnModel().getColumn(9).setMaxWidth(80);

    }

    private void FormatGrid1() {
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
            DataModelDesc.addColumn("Machine No");//2
            DataModelDesc.addColumn("Machine Position");//3
            DataModelDesc.addColumn("Basic Disc % in Bill");//4
            DataModelDesc.addColumn("Seam Disc % in Bill"); //5
            DataModelDesc.addColumn("Year End Basic Disc %");//6
            DataModelDesc.addColumn("Year End Seam Disc %"); //7
            DataModelDesc.addColumn("Turn Over"); //7
            DataModelDesc.addColumn("For Diversion"); //7

            //DataModelDesc.TableReadOnly(true);
            DataModelDesc.SetVariable(0, "SR_NO");  //0
            DataModelDesc.SetVariable(1, "PIECE_NO"); //1
            DataModelDesc.SetVariable(2, "MACHINE_NO"); //2
            DataModelDesc.SetVariable(3, "MACHINE_POSITION"); //3
            DataModelDesc.SetVariable(4, "DISC_PER"); //4
            DataModelDesc.SetVariable(5, "SEAM_VALUE"); //5
            DataModelDesc.SetVariable(6, "YRED_DISC_PER"); //6
            DataModelDesc.SetVariable(7, "YRED_SEAM_VALUE"); //7
            DataModelDesc.SetVariable(8, "TURN_OVER_TARGET"); //8
            DataModelDesc.SetVariable(9, "DIVERSION_FLAG"); //8

            //DataModelDesc.TableReadOnly(false);
            DataModelDesc.SetReadOnly(0);
            //DataModelDesc.SetReadOnly(1);
            //DataModelDesc.SetReadOnly(2);
            //DataModelDesc.SetReadOnly(3);
            //DataModelDesc.SetReadOnly(4);
            //DataModelDesc.SetReadOnly(5);

//            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
//            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
//            TableDesc.getColumnModel().getColumn(16).setPreferredWidth(100);
            int ImportCol = 9;
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
        //Table formatting completed

        TableDesc.getColumnModel().getColumn(0).setMinWidth(30);
        //TableDesc.getColumnModel().getColumn(0).setMaxWidth(30);
        TableDesc.getColumnModel().getColumn(1).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(1).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(2).setMinWidth(80);
        //TableDesc.getColumnModel().getColumn(2).setMaxWidth(80);
        TableDesc.getColumnModel().getColumn(3).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(3).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(4).setMinWidth(80);
        //TableDesc.getColumnModel().getColumn(4).setMaxWidth(80);
        TableDesc.getColumnModel().getColumn(5).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(5).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(6).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(6).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(7).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(7).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(8).setMinWidth(100);
        //TableDesc.getColumnModel().getColumn(8).setMaxWidth(100);
        TableDesc.getColumnModel().getColumn(9).setMinWidth(80);
        TableDesc.getColumnModel().getColumn(9).setMaxWidth(80);

    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    private void PreviewAllReport() {
        TReportWriter.SimpleDataProvider.TTable objReportData;
        objReportData = new TReportWriter.SimpleDataProvider.TTable();
        try {
            objReportData = new TReportWriter.SimpleDataProvider.TTable();
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("PARTY_NAME");
            objReportData.AddColumn("PRODUCT_CODE");
            objReportData.AddColumn("MACHINE_NO");
            objReportData.AddColumn("MACHINE_POSITION");
            objReportData.AddColumn("SANCTION_DATE");
            objReportData.AddColumn("EFFECTIVE_FROM");
            objReportData.AddColumn("EFFECTIVE_TO");
            objReportData.AddColumn("DISC_PER");
            objReportData.AddColumn("SEAM_VALUE");
            objReportData.AddColumn("YRED_DISC_PER");
            objReportData.AddColumn("YRED_SEAM_VALUE");
            objReportData.AddColumn("REMARK");
            objReportData.AddColumn("DOC_STATUS");

            TReportWriter.SimpleDataProvider.TRow objRow;

            String strSQL = "SELECT D.MACHINE_NO,D.MACHINE_POSITION,H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.PRODUCT_CODE,D.DISC_PER,D.SEAM_VALUE,D.YRED_DISC_PER,D.YRED_SEAM_VALUE,CONCAT(H.REMARK1,'',H.REMARK2,'',H.REMARK3,'',H.REMARK4,'',H.REMARK5,'') AS REMARK,CASE WHEN H.APPROVED=1 THEN 'Final Approved' ELSE 'Pending' END AS DOC_STATUS FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO AND H.CANCELED=0 ORDER BY H.PARTY_CODE,H.SANCTION_DATE,D.PRODUCT_CODE,D.MACHINE_NO,D.MACHINE_POSITION,D.EFFECTIVE_FROM";
            //CASE WHEN D.EFFECTIVE_TO='0000-00-00' THEN 'A' ELSE D.EFFECTIVE_TO END AS EFFECTIVE_TO
            ResultSet rsData = data.getResult(strSQL);
            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {

                    objRow = objReportData.newRow();
                    objRow.setValue("PARTY_CODE", rsData.getString("PARTY_CODE"));
                    objRow.setValue("PARTY_NAME", rsData.getString("PARTY_NAME"));
                    objRow.setValue("PRODUCT_CODE", rsData.getString("PRODUCT_CODE"));
                    objRow.setValue("MACHINE_NO", rsData.getString("MACHINE_NO"));
                    objRow.setValue("MACHINE_POSITION", Integer.toString(rsData.getInt("MACHINE_POSITION")));
                    if (rsData.getString("SANCTION_DATE").equalsIgnoreCase("")) {
                        objRow.setValue("SANCTION_DATE", "");
                    } else {
                        objRow.setValue("SANCTION_DATE", EITLERPGLOBAL.formatDate(rsData.getString("SANCTION_DATE")));
                    }
                    if (rsData.getString("EFFECTIVE_FROM").equalsIgnoreCase("")) {
                        objRow.setValue("EFFECTIVE_FROM", "");
                    } else {
                        objRow.setValue("EFFECTIVE_FROM", EITLERPGLOBAL.formatDate(rsData.getString("EFFECTIVE_FROM")));
                    }
                    String b = rsData.getString("EFFECTIVE_TO");

                    if (b == null) {
                        objRow.setValue("EFFECTIVE_TO", "");
                    } else {
                        objRow.setValue("EFFECTIVE_TO", EITLERPGLOBAL.formatDate(rsData.getString("EFFECTIVE_TO")));
                    }
                    objRow.setValue("DISC_PER", Double.toString(rsData.getDouble("DISC_PER")));
                    objRow.setValue("SEAM_VALUE", Double.toString(rsData.getDouble("SEAM_VALUE")));
                    objRow.setValue("YRED_DISC_PER", Double.toString(rsData.getDouble("YRED_DISC_PER")));
                    objRow.setValue("YRED_SEAM_VALUE", Double.toString(rsData.getDouble("YRED_SEAM_VALUE")));
                    objRow.setValue("REMARK", rsData.getString("REMARK"));
                    objRow.setValue("DOC_STATUS", rsData.getString("DOC_STATUS"));
                    objReportData.AddRow(objRow);
                    rsData.next();
                }
            }

            HashMap Parameters = new HashMap();
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
            Parameters.put("USER_NAME", data.getStringValueFromDB("SELECT USER_NAME FROM DINESHMILLS.D_COM_USER_MASTER WHERE USER_ID='" + EITLERPGLOBAL.gUserID + "' AND COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "' "));
            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptSancDiscMaster.rpt", Parameters, objReportData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PreviewPartyReport() {

        TReportWriter.SimpleDataProvider.TTable objReportData;
        objReportData = new TReportWriter.SimpleDataProvider.TTable();
        try {
            String partyCode = txtPartycode.getText().trim();

            objReportData = new TReportWriter.SimpleDataProvider.TTable();
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("PARTY_NAME");
            objReportData.AddColumn("PRODUCT_CODE");
            objReportData.AddColumn("MACHINE_NO");
            objReportData.AddColumn("MACHINE_POSITION");
            objReportData.AddColumn("SANCTION_DATE");
            objReportData.AddColumn("EFFECTIVE_FROM");
            objReportData.AddColumn("EFFECTIVE_TO");
            objReportData.AddColumn("DISC_PER");
            objReportData.AddColumn("SEAM_VALUE");
            objReportData.AddColumn("YRED_DISC_PER");
            objReportData.AddColumn("YRED_SEAM_VALUE");
            objReportData.AddColumn("REMARK");
            objReportData.AddColumn("DOC_STATUS");

            TReportWriter.SimpleDataProvider.TRow objRow;

            String strSQL = "SELECT D.MACHINE_NO,D.MACHINE_POSITION,H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.PRODUCT_CODE,D.DISC_PER,D.SEAM_VALUE,D.YRED_DISC_PER,D.YRED_SEAM_VALUE,CONCAT(H.REMARK1,'',H.REMARK2,'',H.REMARK3,'',H.REMARK4,'',H.REMARK5,'') AS REMARK,CASE WHEN H.APPROVED=1 THEN 'Final Approved' ELSE 'Pending' END AS DOC_STATUS FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO AND H.PARTY_CODE = '" + partyCode + "' AND H.CANCELED=0 ORDER BY H.PARTY_CODE,H.SANCTION_DATE,D.PRODUCT_CODE,D.MACHINE_NO,D.MACHINE_POSITION,D.EFFECTIVE_FROM";
            //CASE WHEN D.EFFECTIVE_TO='0000-00-00' THEN 'A' ELSE D.EFFECTIVE_TO END AS EFFECTIVE_TO
            ResultSet rsData = data.getResult(strSQL);
            rsData.first();
            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {

                    objRow = objReportData.newRow();
                    objRow.setValue("PARTY_CODE", rsData.getString("PARTY_CODE"));
                    objRow.setValue("PARTY_NAME", rsData.getString("PARTY_NAME"));
                    objRow.setValue("PRODUCT_CODE", rsData.getString("PRODUCT_CODE"));
                    objRow.setValue("MACHINE_NO", rsData.getString("MACHINE_NO"));
                    objRow.setValue("MACHINE_POSITION", Integer.toString(rsData.getInt("MACHINE_POSITION")));
                    if (rsData.getString("SANCTION_DATE").equalsIgnoreCase("")) {
                        objRow.setValue("SANCTION_DATE", "");
                    } else {
                        objRow.setValue("SANCTION_DATE", EITLERPGLOBAL.formatDate(rsData.getString("SANCTION_DATE")));
                    }
                    if (rsData.getString("EFFECTIVE_FROM").equalsIgnoreCase("")) {
                        objRow.setValue("EFFECTIVE_FROM", "");
                    } else {
                        objRow.setValue("EFFECTIVE_FROM", EITLERPGLOBAL.formatDate(rsData.getString("EFFECTIVE_FROM")));
                    }
                    String b = rsData.getString("EFFECTIVE_TO");

                    if (b == null) {
                        objRow.setValue("EFFECTIVE_TO", "");
                    } else {
                        objRow.setValue("EFFECTIVE_TO", EITLERPGLOBAL.formatDate(rsData.getString("EFFECTIVE_TO")));
                    }
                    objRow.setValue("DISC_PER", Double.toString(rsData.getDouble("DISC_PER")));
                    objRow.setValue("SEAM_VALUE", Double.toString(rsData.getDouble("SEAM_VALUE")));
                    objRow.setValue("YRED_DISC_PER", Double.toString(rsData.getDouble("YRED_DISC_PER")));
                    objRow.setValue("YRED_SEAM_VALUE", Double.toString(rsData.getDouble("YRED_SEAM_VALUE")));
                    objRow.setValue("REMARK", rsData.getString("REMARK"));
                    objRow.setValue("DOC_STATUS", rsData.getString("DOC_STATUS"));
                    objReportData.AddRow(objRow);
                    rsData.next();
                }
            }

            HashMap Parameters = new HashMap();
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
            Parameters.put("USER_NAME", data.getStringValueFromDB("SELECT USER_NAME FROM DINESHMILLS.D_COM_USER_MASTER WHERE USER_ID='" + EITLERPGLOBAL.gUserID + "' AND COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "' "));
            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptSancDiscMaster.rpt", Parameters, objReportData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PreviewReport1() {
        String PartyCode = txtPartycode.getText();

        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Conn = data.getConn();
            st = Conn.createStatement();

            //rs=data.getResult("SELECT PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD WHERE PARTY_CODE='" + PartyCode + "'");
            rs = data.getResult("SELECT PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE PARTY_CODE='" + PartyCode + "' AND APPROVED=1 AND CANCELED=0");
            rs.first();

            if (rs.getRow() > 0) {

                HashMap parameterMap = new HashMap();

                ReportRegister rpt = new ReportRegister(parameterMap, Conn);
                //String sql = "select d.bill_no,d.item_type,d.quality_cd,d.retail_rate,sum(d.sale_qty) as sale_qty,sum(d.item_amount) as item_amount,sum(d.item_disc_amount) AS discount,sum(d.item_amount-d.item_disc_amount) as item_net_amount,d.bill_date,h.bill_discount_amount,h.net_payble,case when (d.BILL_TYPE IN ('SPECIAL','EMPLOYEE','SHARE HOLDER')) then sum(d.item_type_disc_amount) else 0.00 end as special,h.cash_payment,case when (h.CARD_TYPE IN('VISA','MASTER')) then h.card_payment else 0.00 end as card_pay,case when (h.CARD_TYPE='CHEQUE') then h.card_payment else 0.00 end as cheque_pay from BILL_DETAIL d,BILL_HEADER h where d.bill_no=h.bill_no and d.bill_date=h.bill_date and h.bill_status is NULL group by d.quality_cd,d.bill_no having d.bill_date = '" + pdt + "' order by d.bill_date,d.bill_no*1";
                //String sql = "SELECT H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,H.EFFECTIVE_FROM,H.EFFECTIVE_TO,D.PRODUCT_CODE,D.PIECE_NO,D.MACHINE_NO,D.MACHINE_POSITION,D.TURN_OVER_TARGET,D.DISC_PER,D.YRED_DISC_PER,D.SEAM_VALUE,D.YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD H,PRODUCTION.FELT_RATE_DISC_MASTER D WHERE H.MASTER_NO=D.MASTER_NO AND H.PARTY_CODE='" + PartyCode + "'";
                String sql = "SELECT H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.PRODUCT_CODE,D.PIECE_NO,D.MACHINE_NO,D.MACHINE_POSITION,D.TURN_OVER_TARGET,D.DISC_PER,D.YRED_DISC_PER,D.SEAM_VALUE,D.YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.MASTER_NO=D.MASTER_NO AND H.PARTY_CODE='" + PartyCode + "' AND D.APPROVED=1 AND D.CANCELED=0 ORDER BY D.PARTY_CODE,D.EFFECTIVE_FROM,D.PRODUCT_CODE";
                //rpt.setReportName("/report/billregister.jrxml", 1, sql); //productlist is the name of my jasper file.
                rpt.setReportName("/EITLERP/Production/FeltDiscRateMaster/PartywiseDiscSanc.jrxml", 1, sql); //productlist is the name of my jasper file.
                rpt.callReport();
            }

            if (rs.getRow() == 0) {
                return;
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

    /*
     private void PreviewReport() {
     HashMap Params=new HashMap();
     
     try {
     URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptProforma.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&DocNo="+txtMasterNo.getText()+"&ProformaDate="+EITLERPGLOBAL.formatDateDB(txtEffectFrom.getText()));
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
    private void Notification() {
        if (OpgApprove.isSelected() || OpgFinal.isSelected()) {
            try {

                int moduleId = clsDiscRateMaster.ModuleID;
                int userId = EITLERPGLOBAL.gNewUserID;
                int hierarchyId = SelHierarchyID;

                String docNo = txtMasterNo.getText();
                String partyCode = txtPartycode.getText();
                String groupCode = txtPartycode.getText();
                String sanctionDt = txtSnctnDt.getText();
                String target = jlbl.getText();
                String effectiveFrom = txtEffectFrom.getText();
                String effectiveTo = txtEffectTo.getText();

                String pSubject = "Notification : Felt Sales Discount Rate Master No : " + docNo;
                String pMessage = "";
                String cc = "";
                String prodPcHeader = "";

                if (!OpgFinal.isSelected()) {
                    pMessage = "<br>Discount Rate Master No : " + docNo + " has been approve and forward by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, userId) + ".<br><br>";
                } else {
                    pMessage = "<br>Discount Rate Master No : " + docNo + " has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, userId) + ".<br><br>";
                }

                if (docNo.startsWith("GD")) {
                    String groupName = data.getStringValueFromDB("SELECT GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE GROUP_CODE='" + groupCode + "'");
                    prodPcHeader = "Product Code";
                    pMessage = pMessage + "<br>Group Code : " + groupCode + ". Name : " + groupName + ".";
                } else if (docNo.startsWith("PC")) {
                    String partyName = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, partyCode);
                    prodPcHeader = "Piece No";
                    pMessage = pMessage + "<br>Party Code : " + partyCode + ". Name : " + partyName + ".";
                } else {
                    String partyName = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, partyCode);
                    prodPcHeader = "Product Code";
                    pMessage = pMessage + "<br>Party Code : " + partyCode + ". Name : " + partyName + ".";
                }

                pMessage = pMessage + "<br>Sanction Date : " + sanctionDt + "";
                pMessage = pMessage + "<br>Target : " + target + " (in Lac).";
                pMessage = pMessage + "<br>Effective Date : From " + effectiveFrom + "    To " + effectiveTo + "<br><br>";
                pMessage = pMessage + "<br>Discount Details as given below : ";

                Connection Conn1;
                Statement stmt1;
                ResultSet rsData1;

                Conn1 = data.getConn();
                stmt1 = Conn1.createStatement();
                rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO ='" + docNo + "'");
                rsData1.first();

                pMessage = pMessage + "<table border='1'>"
                        + "<tr>"
                        + "<th align='center'> " + prodPcHeader + " </th>"
                        + "<th align='center'> Basic Disc in Bill </th>"
                        + "<th align='center'> Basic Disc in Year End </th>"
                        + "<th align='center'> Seam Disc in Bill </th>"
                        + "<th align='center'> Seam Disc in Year End </th>"
                        + "</tr>";

                rsData1.first();

                if (rsData1.getRow() > 0) {
                    while (!rsData1.isAfterLast()) {

                        String prodPcDetail = "";

                        if (docNo.startsWith("PC")) {
                            prodPcDetail = rsData1.getString("PIECE_NO");
                        } else {
                            prodPcDetail = rsData1.getString("PRODUCT_CODE");
                        }

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='center'> " + prodPcDetail + " </td>"
                                + "<td align='center'> " + rsData1.getString("DISC_PER") + " </td>"
                                + "<td align='center'> " + rsData1.getString("YRED_DISC_PER") + " </td>"
                                + "<td align='center'> " + rsData1.getString("SEAM_VALUE") + " </td>"
                                + "<td align='center'> " + rsData1.getString("YRED_SEAM_VALUE") + " </td>"
                                + "</tr>";
                        rsData1.next();
                    }
                }
                pMessage = pMessage + "</table>";

                pMessage = pMessage + "<br><br>All Approvers Remark as given below : ";

                pMessage += "</table>";
                pMessage += "<table border=1>";
                pMessage += "<tr><td align='center'><b> Sr.No </b></td>"
                        + "<td align='center'><b> User </b></td>"
                        + "<td align='center'><b> Status </b></td>"
                        + "<td align='center'><b> Remark </b></td>"
                        + "</tr>";

                HashMap hmApprovalHistory = clsDiscRateMaster.getHistoryList(EITLERPGLOBAL.gCompanyID, docNo);
                for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                    pMessage += "<tr>";

                    clsDiscRateMaster ObjHistory = (clsDiscRateMaster) hmApprovalHistory.get(Integer.toString(i));
                    pMessage += "<td>" + Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal()) + "</td>";

                    pMessage += "<td>" + clsUser.getUserName(2, (int) ObjHistory.getAttribute("UPDATED_BY").getVal()) + "</td>";
                    String ApprovalStatus = "";

                    if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                        ApprovalStatus = "Hold";
                    }

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
                    pMessage += "<td>" + ApprovalStatus + "</td>";
                    pMessage += "<td>" + ObjHistory.getAttribute("APPROVER_REMARKS").getString() + "</td>";
                    pMessage += "</tr>";
                }
                pMessage += "</table>";
                pMessage += "<br>";

                HashMap hmSendToList;
                String recievers = "sdmlerp@dineshmills.com";

                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, hierarchyId, userId, true);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();

                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

//                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
//                    if (!to.equals("")) {
//                        recievers = recievers + "," + to;
//                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
//                    }
                }
                //recievers = recievers + ",vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,soumen@dineshmills.com";
                //Remove manoj@dineshmills.com by Dharmendra on 17-05-2019
                //recievers = recievers + ",vdshanbhag@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,soumen@dineshmills.com";
                recievers = recievers + ",felts.hod@dineshmills.com,felts.aic@dineshmills.com,felts.oic@dineshmills.com,rakeshdalal@dineshmills.com,bansi_audit@dineshmills.com";
                cc = "aditya@dineshmills.com";

                pMessage = pMessage + "<br>felts.hod@dineshmills.com";
                //Remove manoj@dineshmills.com by Dharmendra on 17-05-2019
                //pMessage = pMessage + "<br>manoj@dineshmills.com";
                pMessage = pMessage + "<br>felts.aic@dineshmills.com";
                pMessage = pMessage + "<br>felts.oic@dineshmills.com";
//                pMessage = pMessage + "<br>atulshah@dineshmills.com";
                pMessage = pMessage + "<br>rakeshdalal@dineshmills.com";
                pMessage = pMessage + "<br>bansi_audit@dineshmills.com";

                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";// For Live
//                pMessage = pMessage + "<br><br>**** This is an auto-generated email from TEST SERVER, please do not reply ****"; //For Test

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
                System.out.println("pMessage : " + pMessage);

                String responce = MailNotification.sendNotificationMail(moduleId, pSubject, pMessage, recievers, cc, hierarchyId);
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
        }
    }

    private void PieceRegisterValueUpdate(String docNo, String pCode) {
        Connection connection;
        Statement statement;
        ResultSet resultSet;

        clsOrderValueCalc calc = new clsOrderValueCalc();
        EITLERP.FeltSales.common.FeltInvCalc InvObj;
        String SQL = "";

        if (docNo.startsWith("F") || docNo.startsWith("P")) {
            SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "WHERE PR_PARTY_CODE IN ('" + pCode + "') "
                    + "AND PR_PIECE_STAGE NOT IN ('INVOICED','DIVERTED','EXP-INVOICED','DIVIDED','EXP-INVOICE','CANCELED') ";
        }

        if (docNo.startsWith("G")) {
            SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "WHERE PR_PARTY_CODE IN ( SELECT DISTINCT D.PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.GROUP_CODE='" + pCode + "') "
                    + "AND PR_PIECE_STAGE NOT IN ('INVOICED','DIVERTED','EXP-INVOICED','DIVIDED','EXP-INVOICE','CANCELED') ";
        }

        System.out.println("SQL PIECE UPDATE = " + SQL);
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {

                try {
                    String PieceNo = resultSet.getString("PR_PIECE_NO");
                    String Product_Code = resultSet.getString("PR_BILL_PRODUCT_CODE");
                    String Piece_Stage = resultSet.getString("PR_PIECE_STAGE");
                    String Party_Code = resultSet.getString("PR_PARTY_CODE");
                    Float Length = resultSet.getFloat("PR_BILL_LENGTH");
                    Float Width = resultSet.getFloat("PR_BILL_WIDTH");
                    Float Weight = resultSet.getFloat("PR_BILL_WEIGHT");
                    Float SQMT = resultSet.getFloat("PR_BILL_SQMTR");
                    String CurDate = EITLERPGLOBAL.getCurrentDateDB();
                    String baleNo = resultSet.getString("PR_BALE_NO");
                    String baleDate = resultSet.getString("PR_PACKED_DATE");

                    InvObj = clsOrderValueCalc.calculateWithoutGSTINNO(PieceNo, Product_Code, Party_Code, Length, Width, Weight, SQMT, CurDate);

                    float GST = InvObj.getFicGST();
                    float Inv_Amt = InvObj.getFicInvAmt();
                    float INVAMT_WITHOUT_GST = Inv_Amt - GST;
                    float BaseAmt = InvObj.getFicBasAmount();
//                    System.out.println("Piece No : " + PieceNo + " BASE AMT : " + BaseAmt + ", Inv Amt without GST " + INVAMT_WITHOUT_GST + ", GST : " + GST + ", Invoice Amt : " + Inv_Amt);
                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_FELT_VALUE_WITH_GST='" + Inv_Amt + "',PR_FELT_VALUE_WITHOUT_GST='" + INVAMT_WITHOUT_GST + "',PR_FELT_BASE_VALUE='" + BaseAmt + "' WHERE PR_PIECE_NO='" + PieceNo + "'");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void MachineMasterValueUpdate(String docNo, String pCode) {
        Connection connection;
        Statement statement;
        ResultSet resultSet;

        clsOrderValueCalc calc = new clsOrderValueCalc();
        EITLERP.FeltSales.common.FeltInvCalc InvObj;
        String SQL = "";

        if (docNo.startsWith("F") || docNo.startsWith("P")) {
            SQL = "SELECT D.MM_PARTY_CODE AS PARTY_CODE,D.MM_MACHINE_NO AS MACHINE_NO,D.MM_MACHINE_POSITION AS POSITION, "
                    + "D.MM_ITEM_CODE AS PRODUCT_CODE,D.MM_GRUP AS GRUP, "
                    + "(D.MM_FELT_LENGTH+D.MM_FABRIC_LENGTH) AS LENGTH, "
                    + "(D.MM_FELT_WIDTH+D.MM_FABRIC_WIDTH) AS WIDTH,D.MM_FELT_GSM AS GSM, "
                    + "CONCAT(D.MM_FELT_STYLE,D.MM_STYLE_DRY) AS STYLE , "
                    + "D.MM_FELT_WEIGHT AS WEIGHT,MM_DOC_NO "
                    + "FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D "
                    + "WHERE D.MM_PARTY_CODE IN ('" + pCode + "') AND D.MM_ITEM_CODE!='' AND D.MM_MACHINE_NO!='' AND  D.MM_MACHINE_POSITION!='' AND (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' "
                    + "AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') "
                    + "AND (D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '') )";
        }

        if (docNo.startsWith("G")) {
            SQL = "SELECT D.MM_PARTY_CODE AS PARTY_CODE,D.MM_MACHINE_NO AS MACHINE_NO,D.MM_MACHINE_POSITION AS POSITION, "
                    + "D.MM_ITEM_CODE AS PRODUCT_CODE,D.MM_GRUP AS GRUP, "
                    + "(D.MM_FELT_LENGTH+D.MM_FABRIC_LENGTH) AS LENGTH, "
                    + "(D.MM_FELT_WIDTH+D.MM_FABRIC_WIDTH) AS WIDTH,D.MM_FELT_GSM AS GSM, "
                    + "CONCAT(D.MM_FELT_STYLE,D.MM_STYLE_DRY) AS STYLE , "
                    + "D.MM_FELT_WEIGHT AS WEIGHT,MM_DOC_NO "
                    + "FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D "
                    + "WHERE D.MM_PARTY_CODE IN ( SELECT DISTINCT D.PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.GROUP_CODE='" + pCode + "') "
                    + "AND D.MM_ITEM_CODE!='' AND D.MM_MACHINE_NO!='' AND  D.MM_MACHINE_POSITION!='' AND (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' "
                    + "AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') "
                    + "AND (D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '') )";
        }
        System.out.println("SQL MACHINE UPDATE = " + SQL);
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {

                try {
                    String PieceNo = "";
                    String Product_Code = resultSet.getString("PRODUCT_CODE");
                    String Party_Code = resultSet.getString("PARTY_CODE");
                    Float Length = resultSet.getFloat("LENGTH");
                    Float Width = resultSet.getFloat("WIDTH");
                    Float Weight = resultSet.getFloat("WEIGHT");

                    float SQMT = (float) EITLERPGLOBAL.round((Length * Width), 2);
                    String CurDate = EITLERPGLOBAL.getCurrentDateDB();

                    InvObj = clsOrderValueCalc.calculateWithOutGST(PieceNo, Product_Code, Party_Code, Length, Width, Weight, SQMT, CurDate);

                    float GST = InvObj.getFicGST();
                    float Inv_Amt = InvObj.getFicInvAmt();
                    float INVAMT_WITHOUT_GST = Inv_Amt - GST;
                    float BaseAmt = InvObj.getFicBasAmount();
//                     System.out.println("PARTY CODE : "+resultSet.getString("PARTY_CODE")+"  BASE AMT : "+BaseAmt+", Inv Amt without GST "+INVAMT_WITHOUT_GST+", GST : "+GST+", Invoice Amt : "+Inv_Amt);
                    //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_FELT_VALUE_WITH_GST='"+Inv_Amt+"',PR_FELT_VALUE_WITHOUT_GST='"+INVAMT_WITHOUT_GST+"',PR_FELT_BASE_VALUE='"+BaseAmt+"' WHERE PR_PIECE_NO='"+PieceNo+"'");
                     /*System.out.println("UPDATE PRODUCTION.FELT_MACHINE_MASTER_DETAIL D SET MM_FELT_VALUE_WITH_GST='"+Inv_Amt+"',MM_FELT_VALUE_WITHOUT_GST='"+INVAMT_WITHOUT_GST+"',MM_FELT_BASE_VALUE='"+BaseAmt+"' "
                     + "WHERE (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' " 
                     + " AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') AND "
                     + "(D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '') "
                     + "AND D.MM_FELT_GSM!='') AND MM_DOC_NO='"+resultSet.getString("MM_DOC_NO")+"' AND MM_PARTY_CODE='"+resultSet.getString("PARTY_CODE")+"' AND MM_MACHINE_NO='"+resultSet.getString("MACHINE_NO")+"' AND MM_MACHINE_POSITION='"+resultSet.getString("POSITION")+"'");
                     */
                    data.Execute("UPDATE PRODUCTION.FELT_MACHINE_MASTER_DETAIL D SET MM_FELT_VALUE_WITH_GST='" + Inv_Amt + "',MM_FELT_VALUE_WITHOUT_GST='" + INVAMT_WITHOUT_GST + "',MM_FELT_BASE_VALUE='" + BaseAmt + "' WHERE (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' "
                            + " AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') AND "
                            + "(D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '') "
                            + ") AND MM_DOC_NO='" + resultSet.getString("MM_DOC_NO") + "' AND MM_PARTY_CODE='" + resultSet.getString("PARTY_CODE") + "' AND MM_MACHINE_NO='" + resultSet.getString("MACHINE_NO") + "' AND MM_MACHINE_POSITION='" + resultSet.getString("POSITION") + "'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Auto_Unadjusted_Data(String eFrom, String eTo) {
        String sql = "", pDocNo = "", pDocDate = "", SubDate = "";

        try {

            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();

            ResultSet rsDList = data.getResult("SELECT DISTINCT PARTY_CODE FROM PRODUCTION.AUTO_UNADJ_DATA WHERE DISC_AMT>5 ORDER BY PARTY_CODE");
            rsDList.first();

            if (rsDList.getRow() > 0) {
                while (!rsDList.isAfterLast()) {

                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.AUTO_UNADJ_DATA WHERE DISC_AMT>5 AND PARTY_CODE='" + rsDList.getString("PARTY_CODE") + "' ORDER BY INVOICE_DATE,INVOICE_NO ");
                    rsData1.first();

                    if (rsData1.getRow() > 0) {
                        pDocNo = clsFirstFree.getNextFreeNo(2, 732, 188, true);
                        pDocDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");

                        sql = "INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_HEADER "
                                + "(UNADJ_ID, UNADJ_DATE, UNADJ_FROM_DATE, UNADJ_TO_DATE, "
                                + "H_REMARK1, H_REMARK2, HIERARCHY_ID, "
                                + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, "
                                + "CANCELLED, CHANGED, CHANGED_DATE, REJECTED_REMARKS) "
                                + "VALUES('" + pDocNo + "', '" + pDocDate + "', '" + eFrom + "', '" + eTo + "', "
                                + "'AUTO GENERATED UNADJUSTED DATA', '', 4322, "
                                //                                + "1, '" + pDocDate + "', NULL, '0000-00-00', 0, '0000-00-00', 0, '0000-00-00', "
                                + "1, '" + pDocDate + "', NULL, '0000-00-00', 1, '" + pDocDate + "', 0, '0000-00-00', "
                                + "0, 1, '" + pDocDate + "', '')";
//                        System.out.println("Insert Into Unadjusted Data :" + sql);
                        data.Execute(sql);

                        sql = "INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H "
                                + "(REVISION_NO, UNADJ_ID, UNADJ_DATE, UNADJ_FROM_DATE, UNADJ_TO_DATE, "
                                + "H_REMARK1, H_REMARK2, HIERARCHY_ID, UPDATE_BY, ENTRY_DATE, "
                                + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, "
                                + "CANCELLED, CHANGED, CHANGED_DATE, APPROVAL_STATUS, APPROVER_REMARKS, REJECTED_REMARKS) "
                                + "VALUES(1, '" + pDocNo + "', '" + pDocDate + "', '" + eFrom + "', '" + eTo + "', "
                                + "'AUTO GENERATED UNADJUSTED DATA', '', 4322, 1, '" + pDocDate + "', "
                                //                                + "1, '" + pDocDate + "', NULL, '0000-00-00', 0, '0000-00-00', 0, '0000-00-00', "
                                + "1, '" + pDocDate + "', NULL, '0000-00-00', 1, '" + pDocDate + "', 0, '0000-00-00', "
                                + "0, 1, '" + pDocDate + "', 'F', '', '')";
//                        System.out.println("Insert Into History of Unadjusted Data :" + sql);
                        data.Execute(sql);

                        while (!rsData1.isAfterLast()) {

                            String pPartyName = rsData1.getString("PARTY_NAME");
                            String pPartyCode = rsData1.getString("PARTY_CODE");
                            String pPieceNo = rsData1.getString("PIECE_NO");
                            String pProductCode = rsData1.getString("QUALITY_NO");
                            String pInvoiceNo = rsData1.getString("INVOICE_NO");
                            String pInvoiceDate = rsData1.getString("INVOICE_DATE");
                            String pWeight = rsData1.getString("GROSS_KG");
                            String pSqrMtr = rsData1.getString("GROSS_SQ_MTR");
                            String pWidth = rsData1.getString("WIDTH");
                            String pLength = rsData1.getString("LENGTH");
                            String pRate = rsData1.getString("RATE");
                            String pInvBasicAmt = rsData1.getString("GROSS_AMOUNT");
                            String pInvDiscPer = rsData1.getString("INV_DISC_PER");
                            String pSancDiscPer = rsData1.getString("SANC_DISC_PER");
                            String pWorkDiscPer = rsData1.getString("WORK_DISC_PER");
                            String pInvSeamCharge = rsData1.getString("INV_SEAM_CHARGES");
                            String pSancSeamCharge = rsData1.getString("SANC_SEAM_CHARGES");
                            String pDiscAmt = rsData1.getString("DISC_AMT");

                            String pIgstPer = rsData1.getString("DISC_IGST_PER");
                            String pIgstAmt = rsData1.getString("DISC_IGST_AMT");
                            String pCgstPer = rsData1.getString("DISC_CGST_PER");
                            String pCgstAmt = rsData1.getString("DISC_CGST_AMT");
                            String pSgstPer = rsData1.getString("DISC_SGST_PER");
                            String pSgstAmt = rsData1.getString("DISC_SGST_AMT");
                            String pTotalDiscAmt = rsData1.getString("TOTAL_DISC_AMT_GST");

                            sql = "INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL "
                                    + "(SR_NO, UNADJ_ID, PARTY_CODE, PARTY_NAME, PRODUCT_CODE, "
                                    + "INVOICE_NO, INVOICE_DATE, PIECE_NO, KG, SQR_MTR, "
                                    + "WIDTH, LENGTH, RATE, INV_BASIC_AMT, INV_DISC_PER, "
                                    + "SANC_DISC_PER, WORK_DISC_PER, INV_SEAM_CHARGES, SANC_SEAM_CHARGES, SEAM_PER, "
                                    + "IGST_PER, IGST_AMT, CGST_PER, CGST_AMT, SGST_PER, SGST_AMT, TOTAL_DISC_AMT, "
                                    + "DISC_AMT, D_REMARK1, D_REMARK2, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED, CHANGED_DATE) "
                                    + "VALUES('', '" + pDocNo + "', '" + pPartyCode + "', '" + pPartyName + "', '" + pProductCode + "', "
                                    + "'" + pInvoiceNo + "', '" + pInvoiceDate + "', '" + pPieceNo + "', '" + pWeight + "', '" + pSqrMtr + "', "
                                    + "'" + pWidth + "', '" + pLength + "', '" + pRate + "', '" + pInvBasicAmt + "', '" + pInvDiscPer + "', "
                                    + "'" + pSancDiscPer + "', '" + pWorkDiscPer + "', '" + pInvSeamCharge + "', '" + pSancSeamCharge + "', 0, "
                                    + "'" + pIgstPer + "', '" + pIgstAmt + "', '" + pCgstPer + "', '" + pCgstAmt + "', '" + pSgstPer + "', '" + pSgstAmt + "', '" + pTotalDiscAmt + "', "
                                    + "'" + pDiscAmt + "', '', '', 1, '" + pDocDate + "', NULL, '0000-00-00', 1, '" + pDocDate + "' )";
//                            System.out.println("Insert Into Unadjusted Data :" + sql);
                            data.Execute(sql);

                            sql = "INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL_H "
                                    + "(REVISION_NO, SR_NO, UNADJ_ID, PARTY_CODE, PARTY_NAME, PRODUCT_CODE, "
                                    + "INVOICE_NO, INVOICE_DATE, PIECE_NO, KG, SQR_MTR, "
                                    + "WIDTH, LENGTH, RATE, INV_BASIC_AMT, INV_DISC_PER, "
                                    + "SANC_DISC_PER, WORK_DISC_PER, INV_SEAM_CHARGES, SANC_SEAM_CHARGES, SEAM_PER, "
                                    + "IGST_PER, IGST_AMT, CGST_PER, CGST_AMT, SGST_PER, SGST_AMT, TOTAL_DISC_AMT, "
                                    + "DISC_AMT, D_REMARK1, D_REMARK2, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED, CHANGED_DATE) "
                                    + "VALUES(1, '', '" + pDocNo + "', '" + pPartyCode + "', '" + pPartyName + "', '" + pProductCode + "', "
                                    + "'" + pInvoiceNo + "', '" + pInvoiceDate + "', '" + pPieceNo + "', '" + pWeight + "', '" + pSqrMtr + "', "
                                    + "'" + pWidth + "', '" + pLength + "', '" + pRate + "', '" + pInvBasicAmt + "', '" + pInvDiscPer + "', "
                                    + "'" + pSancDiscPer + "', '" + pWorkDiscPer + "', '" + pInvSeamCharge + "', '" + pSancSeamCharge + "', 0, "
                                    + "'" + pIgstPer + "', '" + pIgstAmt + "', '" + pCgstPer + "', '" + pCgstAmt + "', '" + pSgstPer + "', '" + pSgstAmt + "', '" + pTotalDiscAmt + "', "
                                    + "'" + pDiscAmt + "', '', '', 1, '" + pDocDate + "', NULL, '0000-00-00', 1, '" + pDocDate + "' )";
//                            System.out.println("Insert Into History of Unadjusted Data :" + sql);
                            data.Execute(sql);

//                    sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
//                            + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,TYPE,REMARKS,SR_NO,FROM_USER_ID,FROM_REMARKS,RECEIVED_DATE,ACTION_DATE,CHANGED,CHANGED_DATE) "
//                            + "VALUES(732,'" + pDocNo + "','" + pDocDate + "',1,'W','C','ERP SYSTEM AUTO GENERATED',1,0,NULL,'" + pDocDate + "','0000-00-00',1,'" + pDocDate + "')";
                            sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                                    + "SELECT 732,'" + pDocNo + "','" + pDocDate + "',USER_ID,CASE WHEN CREATOR =1 THEN 'F' ELSE 'P'  END, "
                                    + "CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'ERP SYSTEM AUTO GENERATED',SR_NO,0,'','" + pDocDate + "','0000-00-00',1,'" + pDocDate + "'  "
                                    + "FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID =4322";
//                            System.out.println("Insert Into Felt Prod Doc Data :" + sql);
                            data.Execute(sql);

                            rsData1.next();
                        }
                    }

                    try {
                        data.Execute("INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_H SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='" + pDocNo + "'");
                        data.Execute("INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_D SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='" + pDocNo + "'");

                        GenerateUnadjGSTCN(pDocNo, eFrom, eTo);
                        UnadjGSTCNVoucherPosting(pDocNo, eFrom, eTo);
                    } catch (Exception e) {
                        System.out.println("Error while Posting : " + e.getMessage());
                    }

                    rsDList.next();
                }
            }

            Connection Conn2;
            Statement stmt2;
            ResultSet rsData2;
            Conn2 = data.getConn();
            stmt2 = Conn2.createStatement();

            ResultSet rsSList = data.getResult("SELECT DISTINCT PARTY_CODE FROM PRODUCTION.AUTO_UNADJ_DATA WHERE SEAM_CHARGE_AMT>5 ORDER BY PARTY_CODE");
            rsSList.first();

            if (rsSList.getRow() > 0) {
                while (!rsSList.isAfterLast()) {

                    rsData2 = stmt2.executeQuery("SELECT * FROM PRODUCTION.AUTO_UNADJ_DATA WHERE SEAM_CHARGE_AMT>5 AND PARTY_CODE='" + rsSList.getString("PARTY_CODE") + "' ORDER BY INVOICE_DATE,INVOICE_NO ");
                    rsData2.first();

                    if (rsData2.getRow() > 0) {
                        pDocNo = clsFirstFree.getNextFreeNo(2, 732, 189, true);
                        pDocDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");

                        sql = "INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_HEADER "
                                + "(UNADJ_ID, UNADJ_DATE, UNADJ_FROM_DATE, UNADJ_TO_DATE, "
                                + "H_REMARK1, H_REMARK2, HIERARCHY_ID, "
                                + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, "
                                + "CANCELLED, CHANGED, CHANGED_DATE, REJECTED_REMARKS) "
                                + "VALUES('" + pDocNo + "', '" + pDocDate + "', '" + eFrom + "', '" + eTo + "', "
                                + "'AUTO GENERATED UNADJUSTED DATA', '', 4322, "
                                //                                + "1, '" + pDocDate + "', NULL, '0000-00-00', 0, '0000-00-00', 0, '0000-00-00', "
                                + "1, '" + pDocDate + "', NULL, '0000-00-00', 1, '" + pDocDate + "', 0, '0000-00-00', "
                                + "0, 1, '" + pDocDate + "', '')";
//                        System.out.println("Insert Into Unadjusted Data :" + sql);
                        data.Execute(sql);

                        sql = "INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H "
                                + "(REVISION_NO, UNADJ_ID, UNADJ_DATE, UNADJ_FROM_DATE, UNADJ_TO_DATE, "
                                + "H_REMARK1, H_REMARK2, HIERARCHY_ID, UPDATE_BY, ENTRY_DATE, "
                                + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, "
                                + "CANCELLED, CHANGED, CHANGED_DATE, APPROVAL_STATUS, APPROVER_REMARKS, REJECTED_REMARKS) "
                                + "VALUES(1, '" + pDocNo + "', '" + pDocDate + "', '" + eFrom + "', '" + eTo + "', "
                                + "'AUTO GENERATED UNADJUSTED DATA', '', 4322, 1, '" + pDocDate + "', "
                                //                                + "1, '" + pDocDate + "', NULL, '0000-00-00', 0, '0000-00-00', 0, '0000-00-00', "
                                + "1, '" + pDocDate + "', NULL, '0000-00-00', 1, '" + pDocDate + "', 0, '0000-00-00', "
                                + "0, 1, '" + pDocDate + "', 'F', '', '')";
//                        System.out.println("Insert Into History of Unadjusted Data :" + sql);
                        data.Execute(sql);

                        while (!rsData2.isAfterLast()) {

                            String pPartyName = rsData2.getString("PARTY_NAME");
                            String pPartyCode = rsData2.getString("PARTY_CODE");
                            String pPieceNo = rsData2.getString("PIECE_NO");
                            String pProductCode = rsData2.getString("QUALITY_NO");
                            String pInvoiceNo = rsData2.getString("INVOICE_NO");
                            String pInvoiceDate = rsData2.getString("INVOICE_DATE");
                            String pWeight = rsData2.getString("GROSS_KG");
                            String pSqrMtr = rsData2.getString("GROSS_SQ_MTR");
                            String pWidth = rsData2.getString("WIDTH");
                            String pLength = rsData2.getString("LENGTH");
                            String pRate = rsData2.getString("RATE");
                            String pInvBasicAmt = rsData2.getString("GROSS_AMOUNT");
                            String pInvDiscPer = rsData2.getString("INV_DISC_PER");
                            String pSancDiscPer = rsData2.getString("SANC_DISC_PER");
                            String pWorkDiscPer = rsData2.getString("WORK_DISC_PER");
                            String pInvSeamCharge = rsData2.getString("INV_SEAM_CHARGES");
                            String pSancSeamCharge = rsData2.getString("SANC_SEAM_CHARGES");
                            String pSeamAmt = rsData2.getString("SEAM_CHARGE_AMT");

                            String pIgstPer = rsData2.getString("SEAM_IGST_PER");
                            String pIgstAmt = rsData2.getString("SEAM_IGST_AMT");
                            String pCgstPer = rsData2.getString("SEAM_CGST_PER");
                            String pCgstAmt = rsData2.getString("SEAM_CGST_AMT");
                            String pSgstPer = rsData2.getString("SEAM_SGST_PER");
                            String pSgstAmt = rsData2.getString("SEAM_SGST_AMT");
                            String pTotalSeamAmt = rsData2.getString("TOTAL_SEAM_AMT_GST");

                            sql = "INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL "
                                    + "(SR_NO, UNADJ_ID, PARTY_CODE, PARTY_NAME, PRODUCT_CODE, "
                                    + "INVOICE_NO, INVOICE_DATE, PIECE_NO, KG, SQR_MTR, "
                                    + "WIDTH, LENGTH, RATE, INV_BASIC_AMT, INV_DISC_PER, "
                                    + "SANC_DISC_PER, WORK_DISC_PER, INV_SEAM_CHARGES, SANC_SEAM_CHARGES, SEAM_PER, "
                                    + "IGST_PER, IGST_AMT, CGST_PER, CGST_AMT, SGST_PER, SGST_AMT, TOTAL_DISC_AMT, "
                                    + "DISC_AMT, D_REMARK1, D_REMARK2, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED, CHANGED_DATE) "
                                    + "VALUES('', '" + pDocNo + "', '" + pPartyCode + "', '" + pPartyName + "', '" + pProductCode + "', "
                                    + "'" + pInvoiceNo + "', '" + pInvoiceDate + "', '" + pPieceNo + "', '" + pWeight + "', '" + pSqrMtr + "', "
                                    + "'" + pWidth + "', '" + pLength + "', '" + pRate + "', '" + pInvBasicAmt + "', '" + pInvDiscPer + "', "
                                    + "'" + pSancDiscPer + "', '" + pWorkDiscPer + "', '" + pInvSeamCharge + "', '" + pSancSeamCharge + "', 0, "
                                    + "'" + pIgstPer + "', '" + pIgstAmt + "', '" + pCgstPer + "', '" + pCgstAmt + "', '" + pSgstPer + "', '" + pSgstAmt + "', '" + pTotalSeamAmt + "', "
                                    + "'" + pSeamAmt + "', '', '', 1, '" + pDocDate + "', NULL, '0000-00-00', 1, '" + pDocDate + "' )";
//                            System.out.println("Insert Into Unadjusted Data :" + sql);
                            data.Execute(sql);

                            sql = "INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL_H "
                                    + "(REVISION_NO, SR_NO, UNADJ_ID, PARTY_CODE, PARTY_NAME, PRODUCT_CODE, "
                                    + "INVOICE_NO, INVOICE_DATE, PIECE_NO, KG, SQR_MTR, "
                                    + "WIDTH, LENGTH, RATE, INV_BASIC_AMT, INV_DISC_PER, "
                                    + "SANC_DISC_PER, WORK_DISC_PER, INV_SEAM_CHARGES, SANC_SEAM_CHARGES, SEAM_PER, "
                                    + "IGST_PER, IGST_AMT, CGST_PER, CGST_AMT, SGST_PER, SGST_AMT, TOTAL_DISC_AMT, "
                                    + "DISC_AMT, D_REMARK1, D_REMARK2, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED, CHANGED_DATE) "
                                    + "VALUES(1, '', '" + pDocNo + "', '" + pPartyCode + "', '" + pPartyName + "', '" + pProductCode + "', "
                                    + "'" + pInvoiceNo + "', '" + pInvoiceDate + "', '" + pPieceNo + "', '" + pWeight + "', '" + pSqrMtr + "', "
                                    + "'" + pWidth + "', '" + pLength + "', '" + pRate + "', '" + pInvBasicAmt + "', '" + pInvDiscPer + "', "
                                    + "'" + pSancDiscPer + "', '" + pWorkDiscPer + "', '" + pInvSeamCharge + "', '" + pSancSeamCharge + "', 0, "
                                    + "'" + pIgstPer + "', '" + pIgstAmt + "', '" + pCgstPer + "', '" + pCgstAmt + "', '" + pSgstPer + "', '" + pSgstAmt + "', '" + pTotalSeamAmt + "', "
                                    + "'" + pSeamAmt + "', '', '', 1, '" + pDocDate + "', NULL, '0000-00-00', 1, '" + pDocDate + "' )";
//                            System.out.println("Insert Into History of Unadjusted Data :" + sql);
                            data.Execute(sql);

//                    sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
//                            + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,TYPE,REMARKS,SR_NO,FROM_USER_ID,FROM_REMARKS,RECEIVED_DATE,ACTION_DATE,CHANGED,CHANGED_DATE) "
//                            + "VALUES(732,'" + pDocNo + "','" + pDocDate + "',1,'W','C','ERP SYSTEM AUTO GENERATED',1,0,NULL,'" + pDocDate + "','0000-00-00',1,'" + pDocDate + "')";
                            sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                                    + "SELECT 732,'" + pDocNo + "','" + pDocDate + "',USER_ID,CASE WHEN CREATOR =1 THEN 'F' ELSE 'P'  END, "
                                    + "CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'ERP SYSTEM AUTO GENERATED',SR_NO,0,'','" + pDocDate + "','0000-00-00',1,'" + pDocDate + "'  "
                                    + "FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID =4322";
//                            System.out.println("Insert Into Felt Prod Doc Data :" + sql);
                            data.Execute(sql);

                            rsData2.next();
                        }
                    }

                    try {
                        data.Execute("INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_H SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='" + pDocNo + "'");
                        data.Execute("INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_D SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='" + pDocNo + "'");

                        GenerateUnadjGSTCN(pDocNo, eFrom, eTo);
                        UnadjGSTCNVoucherPosting(pDocNo, eFrom, eTo);
                    } catch (Exception e) {
                        System.out.println("Error while Posting : " + e.getMessage());
                    }

                    rsSList.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Error while Saving : " + e.getMessage());
        }
    }

    public void GenerateUnadjGSTCN(String docNo, String fromDt, String toDt) {
        String fromDate = EITLERPGLOBAL.formatDate(fromDt);
        String toDate = EITLERPGLOBAL.formatDate(toDt);

        String frmyr = String.valueOf(EITLERPGLOBAL.getCurrentFinYear());
        String fyr = frmyr.substring(2, 4);
//        System.out.println(frmyr);
//        System.out.println(fyr);
        String toyr = String.valueOf(EITLERPGLOBAL.getCurrentFinYear()+1);
        String tyr = toyr.substring(2, 4);
//        System.out.println(toyr);
//        System.out.println(tyr);

        String docId = docNo.substring(0, 2);
//        System.out.println(docId);

        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,LCN_RC_VOUCHER,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,LCN_CREDIT_AMOUNT,EXT2,LCN_MAIN_ACCOUNT_CODE,LCN_IGST_PER,LCN_IGST_AMT,LCN_CGST_PER,LCN_CGST_AMT,LCN_SGST_PER,LCN_SGST_AMT,EXT3) SELECT H.UNADJ_ID,PARTY_CODE,PARTY_NAME,PRODUCT_CODE,INVOICE_NO,INVOICE_DATE,INV_BASIC_AMT,CASE WHEN WORK_DISC_PER>0 THEN WORK_DISC_PER ELSE SANC_SEAM_CHARGES END AS DISC_PER,TOTAL_DISC_AMT,PIECE_NO,210010,IGST_PER,IGST_AMT,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT,DISC_AMT FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL D,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H WHERE D.UNADJ_ID = H.UNADJ_ID AND H.UNADJ_ID='" + docNo + "'  AND CANCELLED =0 AND APPROVED =1 AND INVOICE_NO NOT IN (SELECT CND_INVOICE_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID LIKE '%" + docId + "%' AND CND_INVOICE_DATE BETWEEN '" + fromDt + "' AND '" + toDt + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') WHERE LCN_INVOICE_DATE>='" + fromDt + "' AND LCN_INVOICE_DATE<='" + toDt + "'");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION =CONCAT('FROM ','" + fromDate + "',' TO ','" + toDate + "')");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL =CONCAT('" + fromDate.substring(6, 10) + "','/','" + toDate.substring(6, 10) + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID,RIGHT(LCN_INVOICE_NO,4))");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT(RIGHT(LCN_INVOICE_NO,4),'/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_CATG,CND_EXT8,CND_EXT9,CND_EXT10,CND_EXT11,CND_EXT12,CND_EXT13,CND_EXT14) SELECT 1,LCN_NO,'UNADJ',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,ROUND(LCN_CREDIT_AMOUNT,0),EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT2,LCN_CGST_PER,LCN_SGST_PER,LCN_IGST_PER,LCN_CGST_AMT,LCN_SGST_AMT,LCN_IGST_AMT,ROUND(EXT3) FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,435132,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),ROUND(SUM(CND_CREDIT_AMOUNT),0),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-07',CND_LINK_NO,'10',CONCAT('Unadjusted Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_TYPE,CND_LC_OPENER,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION LIMIT 1000000000000");

            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UnadjGSTCNVoucherPosting(String docNo, String fromDt, String toDt) {
        String fromDate = EITLERPGLOBAL.formatDate(fromDt);
        String toDate = EITLERPGLOBAL.formatDate(toDt);

        String frmyr = String.valueOf(EITLERPGLOBAL.getCurrentFinYear());
        String fyr = frmyr.substring(2, 4);
//        System.out.println(frmyr);
//        System.out.println(fyr);
        String toyr = String.valueOf(EITLERPGLOBAL.getCurrentFinYear()+1);
        String tyr = toyr.substring(2, 4);
//        System.out.println(toyr);
//        System.out.println(tyr);

        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)='07' AND SUBSTRING(VOUCHER_NO,3,2)='" + fyr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
//                String draftDate = EITLERPGLOBAL.formatDateDB(toDate);
                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
                String linkNo = "8101";
                int lnk = Integer.parseInt(linkNo);
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_NO,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='UNADJ' ORDER BY CNH_SUB_ACCOUNT_CODE,CNH_NO");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
//                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));
                        System.out.println("CNH_NO : " + rsSchemeHeader.getString("CNH_NO"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' AND CNH_NO='" + rsSchemeHeader.getString("CNH_NO") + "' ";
//                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + fyr + "','0',700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' AND CNH_NO='" + rsSchemeHeader.getString("CNH_NO") + "' ";
//                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);

                        String SQL4 = "UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL D,PRODUCTION.D_CREDIT_NOTE_HEADER H SET D.CND_FIN_VOUCHER_NO = H.CNH_FIN_VOUCHER_NO  WHERE D.CND_NO=H.CNH_NO AND CND_TYPE ='UNADJ' AND CND_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_NO='" + rsSchemeHeader.getString("CNH_NO") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
//                        System.out.println("SQL 4 : " + SQL4);
                        st2.execute(SQL4);

                        EITLERP.FeltSales.FeltInvReport.NumWord num = new EITLERP.FeltSales.FeltInvReport.NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));

                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' AND CNH_NO='" + rsSchemeHeader.getString("CNH_NO") + "' ";
//                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeHeader.next();
                        counter++;
                        lnk++;
                    }
                }
            }

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,EXCLUDE_IN_ADJ,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,'07','DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,1,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            String detailInsertQuery = "";

            rsResultSet1 = Stmt.executeQuery("SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");
            double cgstPer = 0, sgstPer = 0, igstPer = 0;
            double cgstAmt = 0, sgstAmt = 0, igstAmt = 0;
            int srno = 0;
            rsResultSet1.first();
            while (!rsResultSet1.isAfterLast() && rsResultSet1.getRow() > 0) {
                cgstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT8"));
                sgstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT9"));
                igstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT10"));
                cgstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT11"));
                sgstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT12"));
                igstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT13"));

                srno = 1;
                detailInsertQuery = "INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,"
                        + "SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) ";

                detailInsertQuery += "SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,"
                        + "CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO "
                        + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                        + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                        + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                srno = srno + 1;
                detailInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'D',CNH_MAIN_ACCOUNT_CODE,'',"
                        + "CND_EXT14,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                        + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                        + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                        + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                if (igstPer == 12) {
                    srno = srno + 1;
                    detailInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'D',127570,'',"
                            + "CND_EXT13,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";
                } else {
                    srno = srno + 1;
                    detailInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'D',127566,'',"
                            + "CND_EXT11,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                    srno = srno + 1;
                    detailInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'D',127568,'',"
                            + "CND_EXT12,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";
                }
                rsResultSet1.next();
            }
//            System.out.println(detailInsertQuery);
            Stmt.execute(detailInsertQuery);

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");

            String detailEXInsertQuery = "";

            rsResultSet2 = Stmt.executeQuery("SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");
//            double cgstPer = 0, sgstPer = 0, igstPer = 0;
//            double cgstAmt = 0, sgstAmt = 0, igstAmt = 0;
            int srnoEX = 0;
            rsResultSet2.first();
            while (!rsResultSet2.isAfterLast() && rsResultSet2.getRow() > 0) {
                cgstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT8"));
                sgstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT9"));
                igstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT10"));
                cgstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT11"));
                sgstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT12"));
                igstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT13"));

                srnoEX = 1;
                detailEXInsertQuery = "INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,"
                        + "SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) ";

                detailEXInsertQuery += "SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,"
                        + "CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO "
                        + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                        + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                        + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                srnoEX = srnoEX + 1;
                detailEXInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'D',CNH_MAIN_ACCOUNT_CODE,'',"
                        + "CND_EXT14,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                        + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                        + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                        + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                if (igstPer == 12) {
                    srnoEX = srnoEX + 1;
                    detailEXInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'D',127570,'',"
                            + "CND_EXT13,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";
                } else {
                    srnoEX = srnoEX + 1;
                    detailEXInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'D',127566,'',"
                            + "CND_EXT11,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                    srnoEX = srnoEX + 1;
                    detailEXInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'D',127568,'',"
                            + "CND_EXT12,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";
                }
                rsResultSet2.next();
            }
//            System.out.println(detailEXInsertQuery);
            Stmt.execute(detailEXInsertQuery);

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646 AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }

}
