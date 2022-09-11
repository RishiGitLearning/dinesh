/*
 * frmFeltWeaving.java
 *
 * Created on March 12, 2013, 3:10 PM
 */
package EITLERP.Production.FeltWeaveout;

/**
 *
 * @author root
 */
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.util.HashMap;
import java.net.URL;

import EITLERP.EITLComboModel;
import EITLERP.EITLTableModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.BigEdit;
import EITLERP.clsUser;
import EITLERP.clsDepartment;
import EITLERP.clsHierarchy;
import EITLERP.clsAuthority;
import EITLERP.clsDocFlow;
import EITLERP.ComboData;
import EITLERP.Loader;
import EITLERP.AppletFrame;
import EITLERP.EITLTableCellRenderer;
import EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrder;
import EITLERP.FeltSales.Order.FrmFeltOrder;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.LOV;
import EITLERP.frmPendingApprovals;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Production.FeltUser;
import EITLERP.Production.FeltWarping.frmFeltWarping;
import EITLERP.data;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Renderer;
import javax.swing.table.TableCellRenderer;

public class frmFeltWeaveout extends javax.swing.JApplet {

    private int EditMode = 0;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromUserId = 0;
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    private boolean DoNotEvaluate = false;
    public int ShiftNo1;
    
    private clsFeltWeaveout ObjFeltWeaveout;
    
    private int mlabelno = 0;
    
    JTable[] tbl = new JTable[100];
    final EITLTableModel[] DataModel_tbl = new EITLTableModel[100];

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbSendToModel;
    private EITLComboModel cmbUserNameModel;
    private EITLComboModel cmbShiftNoModel;

    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableModel DataModelWvoutPieces = new EITLTableModel();    
    private EITLTableCellRenderer Render = new EITLTableCellRenderer();
    public frmPendingApprovals frmPA;
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    /**
     * Creates new form frmFeltWeaving
     */
    public void init() {
        System.gc();
        //setSize(830, 590);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        initComponents();
        lblTitle.setForeground(Color.WHITE);

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

        DataModel = new EITLTableModel();
        ObjFeltWeaveout = new clsFeltWeaveout();

        SetMenuForRights();
        GenerateHierarchyCombo();
        GenerateSendToCombo();
        GenerateUserNameCombo();
        cmdPrint.setEnabled(true);
        GenerateShiftNoCombo();

        if (ObjFeltWeaveout.LoadData()) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Error occured while Loading Data. Error is " + ObjFeltWeaveout.LastError, "DATA LOADING ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        jTabbedPane1.setVisible(false);
        TabbedPane.remove(Selection);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1stShift = new javax.swing.JMenuItem();
        jMenuItem2ndShift = new javax.swing.JMenuItem();
        jMenuItem3rdShift = new javax.swing.JMenuItem();
        file1 = new javax.swing.JFileChooser();
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
        Tab = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtFeltProductionDate = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        cmdNextToTab1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtProductionDocumentNo = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cmbUserName = new javax.swing.JComboBox();
        cmbShiftNo = new javax.swing.JComboBox();
        txtShiftNo = new javax.swing.JTextField();
        txtShiftname = new javax.swing.JTextField();
        TabbedPane = new javax.swing.JTabbedPane();
        SelectedPieces = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        Table_Pieces = new javax.swing.JTable();
        Selection = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTable12 = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTable15 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableWoutSelection = new javax.swing.JTable();
        LoomwiseSelection = new javax.swing.JPanel();
        tabSelectionLoom = new javax.swing.JTabbedPane();
        cmdNextToTab3 = new javax.swing.JButton();
        Tab2 = new javax.swing.JPanel();
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
        cmdBackToTab0 = new javax.swing.JButton();
        cmdFromRemarksBig = new javax.swing.JButton();
        cmdNextToTab2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableApprovalStatus = new javax.swing.JTable();
        lblDocumentHistory = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableUpdateHistory = new javax.swing.JTable();
        cmdBackToTab1 = new javax.swing.JButton();
        cmdBackToNormal = new javax.swing.JButton();
        cmdViewRevisions = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();

        jMenuItem1stShift.setText("First Shift (7:00 AM To 3:30 PM)");
        jMenuItem1stShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1stShiftActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1stShift);

        jMenuItem2ndShift.setText("Second Shift ( 3:30 PM to 12:00 AM)");
        jMenuItem2ndShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ndShiftActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2ndShift);

        jMenuItem3rdShift.setText("Third Shift 12:AM to 7:00 AM");
        jMenuItem3rdShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3rdShiftActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3rdShift);

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

        cmdBack.setToolTipText("Previous Record");
        cmdBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackActionPerformed(evt);
            }
        });
        ToolBar.add(cmdBack);

        cmdNext.setToolTipText("Next record");
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

        cmdSave.setToolTipText("Save");
        cmdSave.setEnabled(false);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        ToolBar.add(cmdSave);

        cmdCancel.setToolTipText("Cancel");
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
        ToolBar.setBounds(0, 0, 1110, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText(" FELT PRODUCTION WEAVEOUT REPORT - ");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 1110, 25);

        Tab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabStateChanged(evt);
            }
        });

        Tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab1FocusGained(evt);
            }
        });
        Tab1.setLayout(null);

        jLabel3.setText("Production Date");
        Tab1.add(jLabel3);
        jLabel3.setBounds(20, 12, 120, 15);

        txtFeltProductionDate.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtFeltProductionDate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtFeltProductionDate.setEnabled(false);
        txtFeltProductionDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFeltProductionDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFeltProductionDateFocusLost(evt);
            }
        });
        Tab1.add(txtFeltProductionDate);
        txtFeltProductionDate.setBounds(150, 10, 120, 21);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel3);
        jPanel3.setBounds(6, 66, 1080, 10);

        cmdNextToTab1.setMnemonic('N');
        cmdNextToTab1.setText("Next >>");
        cmdNextToTab1.setToolTipText("Next Tab");
        cmdNextToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNextToTab1);
        cmdNextToTab1.setBounds(980, 510, 90, 25);

        jLabel4.setText("Shift");
        Tab1.add(jLabel4);
        jLabel4.setBounds(430, 10, 40, 20);

        jLabel5.setText("Document No.");
        Tab1.add(jLabel5);
        jLabel5.setBounds(20, 42, 120, 15);

        txtProductionDocumentNo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtProductionDocumentNo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtProductionDocumentNo.setEnabled(false);
        Tab1.add(txtProductionDocumentNo);
        txtProductionDocumentNo.setBounds(150, 40, 120, 21);

        lblRevNo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(270, 10, 20, 18);

        jLabel12.setText("User Name");
        Tab1.add(jLabel12);
        jLabel12.setBounds(385, 43, 90, 15);

        cmbUserName.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        cmbUserName.setEnabled(false);
        Tab1.add(cmbUserName);
        cmbUserName.setBounds(480, 40, 250, 30);

        cmbShiftNo.setBackground(new java.awt.Color(255, 255, 255));
        cmbShiftNo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        cmbShiftNo.setEnabled(false);
        cmbShiftNo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbShiftNoItemStateChanged(evt);
            }
        });
        cmbShiftNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShiftNoActionPerformed(evt);
            }
        });
        Tab1.add(cmbShiftNo);
        cmbShiftNo.setBounds(480, 10, 250, 24);

        txtShiftNo.setEnabled(false);
        txtShiftNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShiftNoActionPerformed(evt);
            }
        });
        Tab1.add(txtShiftNo);
        txtShiftNo.setBounds(740, 10, 69, 19);

        txtShiftname.setEnabled(false);
        Tab1.add(txtShiftname);
        txtShiftname.setBounds(810, 10, 180, 19);

        TabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabbedPaneStateChanged(evt);
            }
        });

        Table_Pieces.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        Table_Pieces.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(Table_Pieces);

        javax.swing.GroupLayout SelectedPiecesLayout = new javax.swing.GroupLayout(SelectedPieces);
        SelectedPieces.setLayout(SelectedPiecesLayout);
        SelectedPiecesLayout.setHorizontalGroup(
            SelectedPiecesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SelectedPiecesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1031, Short.MAX_VALUE)
                .addContainerGap())
        );
        SelectedPiecesLayout.setVerticalGroup(
            SelectedPiecesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectedPiecesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Summary Report", SelectedPieces);

        Selection.setLayout(null);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 8", jPanel4);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane7.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 81", jPanel5);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane8.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 82", jPanel7);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(jTable5);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 83", jPanel8);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane10.setViewportView(jTable6);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 84", jPanel9);

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane11.setViewportView(jTable7);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 85", jPanel10);

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane12.setViewportView(jTable8);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 86", jPanel11);

        jTable9.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane13.setViewportView(jTable9);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 87", jPanel12);

        jTable10.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane14.setViewportView(jTable10);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 88", jPanel13);

        jTable11.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane15.setViewportView(jTable11);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 89", jPanel14);

        jTable12.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane16.setViewportView(jTable12);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 90", jPanel15);

        jTable13.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane17.setViewportView(jTable13);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 91", jPanel16);

        jTable14.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane18.setViewportView(jTable14);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 92", jPanel17);

        jTable15.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane19.setViewportView(jTable15);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LM 93", jPanel18);

        jPanel2.setLayout(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(18, 12, 990, 110);

        jTabbedPane1.addTab("LM 7", jPanel2);

        Selection.add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 320, 1031, 60);

        TableWoutSelection.setModel(new javax.swing.table.DefaultTableModel(
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
        TableWoutSelection.getTableHeader().setReorderingAllowed(false);
        TableWoutSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableWoutSelectionMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TableWoutSelection);

        Selection.add(jScrollPane4);
        jScrollPane4.setBounds(12, 13, 1031, 290);

        TabbedPane.addTab("Weaveout Selection", Selection);

        javax.swing.GroupLayout LoomwiseSelectionLayout = new javax.swing.GroupLayout(LoomwiseSelection);
        LoomwiseSelection.setLayout(LoomwiseSelectionLayout);
        LoomwiseSelectionLayout.setHorizontalGroup(
            LoomwiseSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1055, Short.MAX_VALUE)
            .addGroup(LoomwiseSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LoomwiseSelectionLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tabSelectionLoom, javax.swing.GroupLayout.PREFERRED_SIZE, 1009, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(34, Short.MAX_VALUE)))
        );
        LoomwiseSelectionLayout.setVerticalGroup(
            LoomwiseSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 393, Short.MAX_VALUE)
            .addGroup(LoomwiseSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LoomwiseSelectionLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tabSelectionLoom, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(60, Short.MAX_VALUE)))
        );

        TabbedPane.addTab("Loomwise Selection", LoomwiseSelection);

        Tab1.add(TabbedPane);
        TabbedPane.setBounds(16, 83, 1060, 420);

        cmdNextToTab3.setMnemonic('N');
        cmdNextToTab3.setText("Export to Excel Summary");
        cmdNextToTab3.setToolTipText("Next Tab");
        cmdNextToTab3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdNextToTab3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab3ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNextToTab3);
        cmdNextToTab3.setBounds(260, 510, 210, 25);

        Tab.addTab("Weaving Details", Tab1);

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab2FocusGained(evt);
            }
        });
        Tab2.setLayout(null);

        jLabel31.setText("Hierarchy ");
        Tab2.add(jLabel31);
        jLabel31.setBounds(7, 23, 62, 15);

        cmbHierarchy.setNextFocusableComponent(OpgApprove);
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
        });
        Tab2.add(cmbHierarchy);
        cmbHierarchy.setBounds(86, 20, 184, 24);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(7, 62, 33, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        txtFrom.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtFrom.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFrom);
        txtFrom.setBounds(86, 60, 184, 21);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(7, 97, 61, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setEnabled(false);
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(86, 95, 630, 19);

        jLabel36.setText("Your Action");
        Tab2.add(jLabel36);
        jLabel36.setBounds(7, 130, 73, 15);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        buttonGroup1.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.setNextFocusableComponent(OpgFinal);
        OpgApprove.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgApproveFocusGained(evt);
            }
        });
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 150, 23);

        buttonGroup1.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
        OpgFinal.setNextFocusableComponent(OpgReject);
        OpgFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgFinalFocusGained(evt);
            }
        });
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });
        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 120, 20);

        buttonGroup1.add(OpgReject);
        OpgReject.setText("Reject");
        OpgReject.setEnabled(false);
        OpgReject.setNextFocusableComponent(OpgHold);
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });
        OpgReject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgRejectFocusGained(evt);
            }
        });
        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 70, 20);

        buttonGroup1.add(OpgHold);
        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        OpgHold.setEnabled(false);
        OpgHold.setNextFocusableComponent(cmbSendTo);
        OpgHold.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgHoldFocusGained(evt);
            }
        });
        OpgHold.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgHoldMouseClicked(evt);
            }
        });
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 130, 20);

        Tab2.add(jPanel6);
        jPanel6.setBounds(86, 130, 184, 100);

        jLabel33.setText("Send To");
        Tab2.add(jLabel33);
        jLabel33.setBounds(7, 249, 50, 15);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(86, 245, 184, 24);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(7, 288, 60, 15);

        txtToRemarks.setNextFocusableComponent(cmdBackToTab0);
        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(86, 286, 630, 19);

        cmdBackToTab0.setMnemonic('B');
        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.setToolTipText("Previous Tab");
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(500, 350, 102, 25);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(728, 94, 49, 21);

        cmdNextToTab2.setMnemonic('N');
        cmdNextToTab2.setText("Next >>");
        cmdNextToTab2.setToolTipText("Next Tab");
        cmdNextToTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNextToTab2);
        cmdNextToTab2.setBounds(620, 350, 102, 25);

        Tab.addTab("Approval", Tab2);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(null);

        jLabel26.setText("Document Approval Status");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(8, 5, 170, 15);

        TableApprovalStatus.setModel(new javax.swing.table.DefaultTableModel(
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
        TableApprovalStatus.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(TableApprovalStatus);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(12, 24, 760, 150);

        lblDocumentHistory.setText("Document Update History");
        jPanel1.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(8, 191, 163, 15);

        TableUpdateHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        TableUpdateHistory.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane3.setViewportView(TableUpdateHistory);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 210, 640, 180);

        cmdBackToTab1.setMnemonic('B');
        cmdBackToTab1.setText("<< Back");
        cmdBackToTab1.setToolTipText("Previous Tab");
        cmdBackToTab1.setIconTextGap(0);
        cmdBackToTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab1ActionPerformed(evt);
            }
        });
        jPanel1.add(cmdBackToTab1);
        cmdBackToTab1.setBounds(662, 390, 110, 25);

        cmdBackToNormal.setText("Back To Normal");
        cmdBackToNormal.setMargin(new java.awt.Insets(2, 3, 2, 3));
        cmdBackToNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToNormalActionPerformed(evt);
            }
        });
        jPanel1.add(cmdBackToNormal);
        cmdBackToNormal.setBounds(662, 240, 130, 25);

        cmdViewRevisions.setText("View Revisions");
        cmdViewRevisions.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdViewRevisions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewRevisionsActionPerformed(evt);
            }
        });
        jPanel1.add(cmdViewRevisions);
        cmdViewRevisions.setBounds(662, 210, 130, 25);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        jPanel1.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(662, 270, 130, 25);

        Tab.addTab("Status", jPanel1);

        getContentPane().add(Tab);
        Tab.setBounds(2, 66, 1100, 570);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        new TReportWriter.TReportEngine().PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/rptFeltWeaving.rpt", new HashMap(), ObjFeltWeaveout.getReportData(EITLERPGLOBAL.formatDateDB(txtFeltProductionDate.getText().trim())));
        EITLERPGLOBAL.PAGE_BREAK = true;
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        try {
            URL reportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/Production/rptFeltWeaving.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&WVOUT_DATE=" + EITLERPGLOBAL.formatDateDB(txtFeltProductionDate.getText()));
            EITLERPGLOBAL.loginContext.showDocument(reportFile, "_blank");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "File error " + e.getMessage(), "FILE ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        if (TableUpdateHistory.getRowCount() > 0 && TableUpdateHistory.getSelectedRow() >= 0) {
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText.setText(TableUpdateHistory.getValueAt(TableUpdateHistory.getSelectedRow(), 4).toString());
            bigEdit.ShowEdit();
        } else {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Select a row from Document Update History");
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdBackToNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToNormalActionPerformed
        ObjFeltWeaveout.HistoryView = false;
        ObjFeltWeaveout.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdBackToNormalActionPerformed

    private void cmdViewRevisionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewRevisionsActionPerformed
        ObjFeltWeaveout.ShowHistory(EITLERPGLOBAL.formatDateDB(txtFeltProductionDate.getText()), txtProductionDocumentNo.getText());
        MoveLast();
    }//GEN-LAST:event_cmdViewRevisionsActionPerformed

    private void cmdBackToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab1ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBackToTab1ActionPerformed

    private void cmdNextToTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab2ActionPerformed
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNextToTab2ActionPerformed

    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtFromRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed

    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained
        cmbHierarchy.requestFocus();
    }//GEN-LAST:event_Tab2FocusGained

    private void cmdBackToTab0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab0ActionPerformed
        Tab.setSelectedIndex(0);
    }//GEN-LAST:event_cmdBackToTab0ActionPerformed

    private void cmdNextToTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab1ActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNextToTab1ActionPerformed

    private void Tab1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab1FocusGained
        txtFeltProductionDate.requestFocus();
    }//GEN-LAST:event_Tab1FocusGained

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained
        ShowMessage("Enter the remarks for next user");
    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        ShowMessage("Select the user to whom document to be forwarded");
    }//GEN-LAST:event_cmbSendToFocusGained

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgHoldFocusGained

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgRejectFocusGained

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        ShowMessage("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void txtFeltProductionDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFeltProductionDateFocusGained
        ShowMessage("Enter Production Date");
    }//GEN-LAST:event_txtFeltProductionDateFocusGained

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        OpgHold.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgReject.setSelected(false);

        cmbSendTo.setEnabled(false);
    }//GEN-LAST:event_OpgHoldMouseClicked

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        OpgFinal.setSelected(true);
        OpgReject.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        //SetupApproval();
        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedSendToCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(703, txtProductionDocumentNo.getText())) {
                cmbSendTo.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }

        if (cmbSendTo.getItemCount() <= 0) {
            GenerateSendToCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        ObjFeltWeaveout.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        if (JOptionPane.showConfirmDialog(frmFeltWeaveout.this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Delete();
        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        //Add();
        jPopupMenu1.show(cmdNew, 0, 35);
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateSendToCombo();

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

    private void cmbShiftNoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbShiftNoItemStateChanged
        String productionDate, productionDocumentNo;        
        int Shift = cmbShiftNo.getSelectedIndex();
        //txtShiftNo.setText(Integer.toString(cmbShiftNo.getSelectedIndex()));
        //txtShiftname.setText((String) cmbShiftNo.getSelectedItem());
        //ShiftNo1 = Shift;
        //theopicks();
    }//GEN-LAST:event_cmbShiftNoItemStateChanged

    private void cmbShiftNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShiftNoActionPerformed

    }//GEN-LAST:event_cmbShiftNoActionPerformed

    private void jMenuItem1stShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1stShiftActionPerformed
        cmbShiftNo.setEnabled(false);
        //txtWeavingDate.setEnabled(true);
        txtFeltProductionDate.setEnabled(true);
        EITLERPGLOBAL.setComboIndex(cmbShiftNo, 1);
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SetupApproval();
        //txtWvgProdNo.setText("");
        txtProductionDocumentNo.setText("");
        //txtWeavingDate.requestFocus();
        txtFeltProductionDate.requestFocus();
        lblTitle.setText(" FELT WEAVEOUT ENTRY FORM - " + txtProductionDocumentNo.getText());
        lblTitle.setBackground(Color.BLUE);
        String productionDate, productionDocumentNo;        
        int Shift = cmbShiftNo.getSelectedIndex();
        txtShiftNo.setText(Integer.toString(cmbShiftNo.getSelectedIndex()));
        txtShiftname.setText((String) cmbShiftNo.getSelectedItem());
        ShiftNo1 = Shift;
        //AddAllLoom();
        //theopicks();
        WeaveoutNo();
        FormatGridWoutSelection();
        GenerateListWoutSelection();
        GenerateLoomwiseSelectionData();
        GeneratePiecesList();
    }//GEN-LAST:event_jMenuItem1stShiftActionPerformed

    private void jMenuItem2ndShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ndShiftActionPerformed
        cmbShiftNo.setEnabled(false);
        txtFeltProductionDate.setEnabled(true);
        EITLERPGLOBAL.setComboIndex(cmbShiftNo, 2);
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SetupApproval();
        txtProductionDocumentNo.setText("");
        txtFeltProductionDate.requestFocus();
        lblTitle.setText(" FELT WEAVING LOOM WISE PRODUCTION EFFICIENCY ENTRY FORM - " + txtProductionDocumentNo.getText());
        lblTitle.setBackground(Color.BLUE);
        String productionDate, productionDocumentNo;        
        int Shift = cmbShiftNo.getSelectedIndex();
        txtShiftNo.setText(Integer.toString(cmbShiftNo.getSelectedIndex()));
        txtShiftname.setText((String) cmbShiftNo.getSelectedItem());
        ShiftNo1 = Shift;

        //AddAllLoom();
        //theopicks();
        
        WeaveoutNo();
        FormatGridWoutSelection();
        GenerateListWoutSelection();
        GenerateLoomwiseSelectionData();
    }//GEN-LAST:event_jMenuItem2ndShiftActionPerformed

    private void jMenuItem3rdShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3rdShiftActionPerformed
        cmbShiftNo.setEnabled(false);
        txtFeltProductionDate.setEnabled(true);
        EITLERPGLOBAL.setComboIndex(cmbShiftNo, 3);
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SetupApproval();
        txtProductionDocumentNo.setText("");
        txtFeltProductionDate.requestFocus();
        lblTitle.setText(" FELT WEAVING LOOM WISE PRODUCTION EFFICIENCY ENTRY FORM - " + txtProductionDocumentNo.getText());
        lblTitle.setBackground(Color.BLUE);
        String productionDate, productionDocumentNo;        
        int Shift = cmbShiftNo.getSelectedIndex();
        txtShiftNo.setText(Integer.toString(cmbShiftNo.getSelectedIndex()));
        txtShiftname.setText((String) cmbShiftNo.getSelectedItem());
        ShiftNo1 = Shift;

        //AddAllLoom();
        //theopicks();
        WeaveoutNo();
        FormatGridWoutSelection();
        GenerateListWoutSelection();
        GenerateLoomwiseSelectionData();
    }//GEN-LAST:event_jMenuItem3rdShiftActionPerformed

    private void txtShiftNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShiftNoActionPerformed

    }//GEN-LAST:event_txtShiftNoActionPerformed

    private void txtFeltProductionDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFeltProductionDateFocusLost
        // TODO add your handling code here:
        WeaveoutNo();
    }//GEN-LAST:event_txtFeltProductionDateFocusLost

    private void TableWoutSelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableWoutSelectionMouseClicked
        // TODO add your handling code here:

        //            System.out.println("Clicked : "+(Table.getSelectedRow()+1)+" colum : "+Table.getSelectedColumn());
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
        String woutDocNo=txtProductionDocumentNo.getText().trim();
        String tick = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 0).toString();
        String pieceNo = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 2).toString();
        String partyCode = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 3).toString();
        String prodCode = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 4).toString();        
        String prodGroup = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 5).toString();
        String loomNo = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 6).toString();
        String beamNo = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 7).toString();
        String picksper10cms = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 8).toString();
        String totalPicks = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 9).toString();
        String length = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 10).toString();
        String reedSpace = TableWoutSelection.getValueAt(TableWoutSelection.getSelectedRow(), 11).toString();
        //            String ipAdd = data.getStringValueFromDB("SELECT SUBSTRING_INDEX(USER(),'@',-1)");

        if (TableWoutSelection.getSelectedColumn() == 0) {

            //            System.out.println("tick : "+tick);
            if (tick.equalsIgnoreCase("true")) {
                if(!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='"+woutDocNo+"' AND PIECE_NO='" + pieceNo + "'")){
                data.Execute("INSERT INTO PRODUCTION.FELT_WVOUT_SELECTION_DATA (WVOUT_DOC_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,"
                        + " LOOM_NO,BEAM_NO,THEORICAL_PICKS_10_CM, TOTAL_PICKS, THEORICAL_LENGTH_MTR,REED_SPACE) "
                        + "VALUES ('" + woutDocNo + "','" + pieceNo + "','" + partyCode + "','" + prodCode + "','" + prodGroup + "','" + loomNo + "','" + beamNo + "','" + picksper10cms + "','" + totalPicks + "','" + length + "','" + reedSpace + "')");                
                }
                /*data.Execute("INSERT INTO PRODUCTION.FELT_WVOUT_SELECTION_DATA (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, PIECE_STAGE, UPN, ENTRY_STATUS, ENTRY_TIME, USER_ID, IP_ADDRESS) "
                    + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','" + pieceStage + "','','ADD',NOW(),'" + EITLERPGLOBAL.gNewUserID + "',SUBSTRING_INDEX(USER(),'@',-1) ) ");
                if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "'")) {
                    data.Execute("INSERT INTO PRODUCTION.FELT_DISPATCH_SELECTION_DATA (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, UPN, PIECE_STAGE) "
                        + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','','" + pieceStage + "') ");
                }               
                */  
            }
            
            if (tick.equalsIgnoreCase("false")) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + woutDocNo + "' AND PIECE_NO='" + pieceNo + "'")) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + woutDocNo + "' AND PIECE_NO='" + pieceNo + "' ");
                }
                
                /*data.Execute("INSERT INTO PRODUCTION.FELT_DISPATCH_SELECTION_DATA_HISTORY (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, PIECE_STAGE, UPN, ENTRY_STATUS, ENTRY_TIME, USER_ID, IP_ADDRESS) "
                    + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','" + pieceStage + "','','DELETE',NOW(),'" + EITLERPGLOBAL.gNewUserID + "',SUBSTRING_INDEX(USER(),'@',-1) ) ");
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "'")) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "' ");
                }*/
            }
            
        }
        }
    }//GEN-LAST:event_TableWoutSelectionMouseClicked

    private void TabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneStateChanged
        // TODO add your handling code here:
        if (TabbedPane.getTitleAt(TabbedPane.getSelectedIndex()).equals("Weaveout Selection")) {
            FormatGridWoutSelection();
            GenerateListWoutSelection();
        } else if (TabbedPane.getTitleAt(TabbedPane.getSelectedIndex()).equals("Summary Report")) {
            GeneratePiecesList();        
        } else if(TabbedPane.getTitleAt(TabbedPane.getSelectedIndex()).equals("Loomwise Selection")){
            GenerateLoomwiseSelectionData();
        }else {
            
        }
    }//GEN-LAST:event_TabbedPaneStateChanged

    private void TabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabStateChanged
        if (Tab.getTitleAt(TabbedPane.getSelectedIndex()).equals("Approval")) {
            GeneratePiecesList();        
        }
    }//GEN-LAST:event_TabStateChanged

    private void cmdNextToTab3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab3ActionPerformed
        
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(Table_Pieces, new File(file1.getSelectedFile().toString() + ".xls"), "Sheet1");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cmdNextToTab3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LoomwiseSelection;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel SelectedPieces;
    private javax.swing.JPanel Selection;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JTable TableWoutSelection;
    private javax.swing.JTable Table_Pieces;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JComboBox cmbShiftNo;
    private javax.swing.JComboBox cmbUserName;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBackToNormal;
    private javax.swing.JButton cmdBackToTab0;
    private javax.swing.JButton cmdBackToTab1;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFromRemarksBig;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNextToTab1;
    private javax.swing.JButton cmdNextToTab2;
    private javax.swing.JButton cmdNextToTab3;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewRevisions;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1stShift;
    private javax.swing.JMenuItem jMenuItem2ndShift;
    private javax.swing.JMenuItem jMenuItem3rdShift;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable14;
    private javax.swing.JTable jTable15;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTabbedPane tabSelectionLoom;
    private javax.swing.JTextField txtFeltProductionDate;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtProductionDocumentNo;
    private javax.swing.JTextField txtShiftNo;
    private javax.swing.JTextField txtShiftname;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables

    //Puts toolbar in enable mode
    private void EnableToolbar() {
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

    //Puts toolbar in disable mode
    private void DisableToolbar() {
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

    private void SetFields(boolean pStat) {
        txtFeltProductionDate.setEnabled(pStat);
        //txtShiftNo.setEnabled(pStat);        
        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        cmbUserName.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);

        SetupApproval();
    }

    private void ClearFields() {
        txtFeltProductionDate.setText(EITLERPGLOBAL.getCurrentDate());
        txtShiftNo.setText("");
        txtShiftname.setText("");
        txtProductionDocumentNo.setText("");
        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        
        FormatGridWoutSelection();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
    }

    //Display data on the Screen
    private void DisplayData() {
        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, 703)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        //=========== Title Bar Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (ObjFeltWeaveout.getAttribute("APPROVED").getInt() == 1) {
                    lblTitle.setBackground(Color.BLUE);
                } else {
                    lblTitle.setBackground(Color.GRAY);
                }

                /*if(ObjFeltWeaving.getAttribute("CANCELED").getInt()==1) {
                 lblTitle.setBackground(Color.RED);
                 }*/
            }
            //============================================//

            String productionDate = EITLERPGLOBAL.formatDate(ObjFeltWeaveout.getAttribute("PRODUCTION_DATE").getString());
            String documentNo = ObjFeltWeaveout.getAttribute("PRODUCTION_DOCUMENT_NO").getString();
            lblTitle.setText(" FELT PRODUCTION WEAVEOUT REPORT - " + productionDate);
            lblRevNo.setText(Integer.toString((int) ObjFeltWeaveout.getAttribute("REVISION_NO").getVal()));
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) ObjFeltWeaveout.getAttribute("HIERARCHY_ID").getVal());
            DoNotEvaluate = true;

            FormatGridWoutSelection();
            txtFeltProductionDate.setText(productionDate);
            txtProductionDocumentNo.setText(documentNo);
            txtShiftNo.setText(ObjFeltWeaveout.getAttribute("WVOUT_SHIFT").getString());
            EITLERPGLOBAL.setComboIndex(cmbShiftNo, Integer.parseInt(ObjFeltWeaveout.getAttribute("WVOUT_SHIFT").getString()));
            EITLERPGLOBAL.setComboIndex(cmbShiftNo, Integer.parseInt(txtShiftNo.getText()));
            
            //Now Generate Table
            for (int i = 1; i <= ObjFeltWeaveout.hmFeltWeavingDetails.size(); i++) {
                clsFeltWeaveoutDetails ObjFeltWeavingDetails = (clsFeltWeaveoutDetails) ObjFeltWeaveout.hmFeltWeavingDetails.get(Integer.toString(i));

                Object[] rowData = new Object[24];
                rowData[0] = Integer.toString(i);
                rowData[1] = ObjFeltWeavingDetails.getAttribute("PRODUCTION_PIECE_NO").getString();
                rowData[2] = ObjFeltWeavingDetails.getAttribute("PRODUCTION_PARTY_CODE").getString();
//                String THWeight = data.getStringValueFromDB("SELECT WIP_THORITICAL_WEIGHT FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + ObjFeltWeavingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "' ");
//                rowData[3] = THWeight;
                rowData[3] = Double.toString(EITLERPGLOBAL.round(ObjFeltWeavingDetails.getAttribute("THORITICAL_WEIGHT").getVal(), 2));
                rowData[4] = Double.toString(EITLERPGLOBAL.round(ObjFeltWeavingDetails.getAttribute("WEIGHT").getVal(), 2));
                rowData[5] = ObjFeltWeavingDetails.getAttribute("PICKS_PER_10CMS").getString();
                rowData[6] = Double.toString(EITLERPGLOBAL.round(ObjFeltWeavingDetails.getAttribute("REED_SPACE").getVal(), 2));
                rowData[7] = Double.toString(EITLERPGLOBAL.round(ObjFeltWeavingDetails.getAttribute("LENGTH").getVal(), 2));
                rowData[8] = Integer.toString((int) ObjFeltWeavingDetails.getAttribute("LOOM_NO").getVal());
                
                rowData[9] = Double.toString(EITLERPGLOBAL.round(ObjFeltWeavingDetails.getAttribute("PICK").getVal(), 2));
                rowData[10] = Double.toString(EITLERPGLOBAL.round(ObjFeltWeavingDetails.getAttribute("PICKMTR").getVal(), 2));
                
                rowData[11] = ObjFeltWeavingDetails.getAttribute("REMARKS").getString();
                //rowData[12] = EITLERPGLOBAL.formatDate(ObjFeltWeavingDetails.getAttribute("WEAVE_DATE").getString());
                //rowData[13] = ObjFeltWeavingDetails.getAttribute("WARP_NO").getString();
                //rowData[14] = ObjFeltWeavingDetails.getAttribute("WEAVE_DIFF_DAYS").getString();
                
                //rowData[15] = data.getStringValueFromDB("SELECT WIP_PRODUCT_CODE FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + ObjFeltWeavingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                //rowData[16] = data.getStringValueFromDB("SELECT WIP_GROUP FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + ObjFeltWeavingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                //rowData[17] = data.getStringValueFromDB("SELECT WIP_GSM FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + ObjFeltWeavingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");
                //rowData[18] = data.getStringValueFromDB("SELECT WIP_STYLE FROM PRODUCTION.FELT_WIP_PIECE_REGISTER WHERE WIP_EXT_PIECE_NO='" + ObjFeltWeavingDetails.getAttribute("PRODUCTION_PIECE_NO").getString() + "'");

                DataModel.addRow(rowData);
            }

            
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap hmList = new HashMap();

            hmList = clsFeltProductionApprovalFlow.getDocumentFlow(703, documentNo);
            for (int i = 1; i <= hmList.size(); i++) {
                //clsDocFlow is collection class used for holding approval flow data
                clsDocFlow ObjFlow = (clsDocFlow) hmList.get(Integer.toString(i));
                Object[] rowData = new Object[7];

                rowData[0] = Integer.toString(i);
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal()));
                rowData[3] = ObjFlow.getAttribute("STATUS").getString();
                rowData[4] = EITLERPGLOBAL.formatDate(ObjFlow.getAttribute("RECEIVED_DATE").getString()) + ObjFlow.getAttribute("RECEIVED_DATE").getString().substring(10, 19);
                rowData[5] = EITLERPGLOBAL.formatDate(ObjFlow.getAttribute("ACTION_DATE").getString()) + ObjFlow.getAttribute("ACTION_DATE").getString().substring(10, 19);
                rowData[6] = ObjFlow.getAttribute("REMARKS").getString();

                DataModelApprovalStatus.addRow(rowData);
            }
            //============================================================//

            // Generating Grid for Showing Production Details Update History
            FormatGridUpdateHistory();
            HashMap hmApprovalHistory = clsFeltWeaveout.getHistoryList(ObjFeltWeaveout.getAttribute("PRODUCTION_DATE").getString(), txtProductionDocumentNo.getText());
            for (int i = 1; i <= hmApprovalHistory.size(); i++) {
                clsFeltWeaveout ObjFeltWeaving = (clsFeltWeaveout) hmApprovalHistory.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjFeltWeaving.getAttribute("REVISION_NO").getVal());
                rowData[1] = FeltUser.getUserName((int) ObjFeltWeaving.getAttribute("UPDATED_BY").getVal());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjFeltWeaving.getAttribute("ENTRY_DATE").getString()) + ObjFeltWeaving.getAttribute("ENTRY_DATE").getString().substring(10, 19);

                String ApprovalStatus = "";

                if ((ObjFeltWeaving.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }

                if ((ObjFeltWeaving.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if ((ObjFeltWeaving.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }

                if ((ObjFeltWeaving.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if ((ObjFeltWeaving.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if ((ObjFeltWeaving.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if ((ObjFeltWeaving.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                rowData[3] = ApprovalStatus;
                rowData[4] = ObjFeltWeaving.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjFeltWeaving.getAttribute("FROM_IP").getString();

                DataModelUpdateHistory.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DoNotEvaluate = false;
    }

    private void FormatGridWoutSelection() {
        try {
            

            DataModel = new EITLTableModel();
            TableWoutSelection.removeAll();

            TableWoutSelection.setModel(DataModel);
            TableColumnModel ColModel = TableWoutSelection.getColumnModel();
            TableWoutSelection.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            //Add Columns to it
            DataModel.addColumn("Select"); //0
            DataModel.addColumn("Sr. No."); //1
            DataModel.addColumn("Piece No"); //2
            DataModel.addColumn("Party Code"); //3
            DataModel.addColumn("Product Code"); //4
            DataModel.addColumn("Group"); //5
            DataModel.addColumn("Loom No");  //6
            DataModel.addColumn("Beam No");  //7
            DataModel.addColumn("Th Picks/10CMS"); //8
            DataModel.addColumn("Total Picks");  //9
            DataModel.addColumn("Th Length");    //10        
            DataModel.addColumn("Reed Space");    //11        
            
            Render.setCustomComponent(0, "CheckBox");
            TableWoutSelection.getColumnModel().getColumn(0).setCellRenderer(Render);
            TableWoutSelection.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
            
            for (int a = 1; a <= 10; a++) {
                DataModel.SetReadOnly(a);
            }
            
            
            
            
            
//            DataModel.SetReadOnly(3);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Enter Correct Details in Table. Error is : " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Generates Hierarchy Combo Box
    private void GenerateHierarchyCombo() {
        HashMap hmHierarchyList = new HashMap();

        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        hmHierarchyList = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=703 ");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            hmHierarchyList = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=703 ");
        }
        for (int i = 1; i <= hmHierarchyList.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
    }

    //Generates Send To Combo Box
    private void GenerateSendToCombo() {
        HashMap hmSendToList = new HashMap();
        try {
            cmbSendToModel = new EITLComboModel();
            cmbSendTo.removeAllItems();
            cmbSendTo.setModel(cmbSendToModel);
            if (EditMode == EITLERPGLOBAL.ADD) {
                hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();

                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    } else {
                        cmbSendToModel.addElement(aData);
                    }
                }
            } else {
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(703, ObjFeltWeaveout.getAttribute("PRODUCTION_DOCUMENT_NO").getString());
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
                    cmbSendToModel.addElement(aData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Generates Send To Combo Box for Rejected User
    private void GenerateRejectedSendToCombo() {
        HashMap hmRejectedSendToList = new HashMap();

        cmbSendToModel = new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbSendToModel);

        //Now Add other hierarchy Users
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        hmRejectedSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, true);
        for (int i = 1; i <= hmRejectedSendToList.size(); i++) {
            clsUser ObjUser = (clsUser) hmRejectedSendToList.get(Integer.toString(i));

            ComboData aData = new ComboData();
            aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
            aData.Text = ObjUser.getAttribute("USER_NAME").getString();

            boolean IncludeUser = false;
            //Decide to include user or not
            if (EditMode == EITLERPGLOBAL.EDIT) {
                if (OpgApprove.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(703, txtProductionDocumentNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(703, txtProductionDocumentNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (IncludeUser && (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID)) {
                    cmbSendToModel.addElement(aData);
                }
            } else {
                if (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID) {
                    cmbSendToModel.addElement(aData);
                }
            }

        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            int Creator = clsFeltProductionApprovalFlow.getCreator(703, txtProductionDocumentNo.getText());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }
    }

    //Generates User Name Combo Box
    private void GenerateUserNameCombo() {
        HashMap hmUserNameList = new HashMap();

        cmbUserNameModel = new EITLComboModel();
        cmbUserName.removeAllItems();
        cmbUserName.setModel(cmbUserNameModel);

        hmUserNameList = ObjFeltWeaveout.getUserNameList(EITLERPGLOBAL.getComboCode(cmbHierarchy), EITLERPGLOBAL.gNewUserID, "WEAVEOUT");
        for (int i = 1; i <= hmUserNameList.size(); i++) {
            cmbUserNameModel.addElement((ComboData) hmUserNameList.get(new Integer(i)));
        }
    }

        private void GenerateShiftNoCombo() {
        HashMap List = new HashMap();
        //----- Generate SHIFT  Combo ------- //

        cmbShiftNoModel = new EITLComboModel();
        cmbShiftNo.removeAllItems();
        cmbShiftNo.setModel(cmbShiftNoModel);

        try {
            ComboData combodata = new ComboData();
            combodata.Code = 0;
            combodata.Text = "Select Shift Code";

            cmbShiftNoModel.addElement(combodata);
            ResultSet rs = data.getResult("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID ='SHIFT' ORDER BY PARA_CODE+0");
            while (!rs.isAfterLast()) {
                combodata = new ComboData();
                combodata.Code = rs.getLong("PARA_CODE");
                combodata.Text = rs.getString("PARA_DESC");
                cmbShiftNoModel.addElement(combodata);
                rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    private void SetupApproval() {
        /*// --- Hierarchy Change Rights Check --------
         if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,75)) {
         cmbHierarchy.setEnabled(true);
         }else {
         cmbHierarchy.setEnabled(false);
         }*/

        // select hold for default approval
        OpgHold.setSelected(true);

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
            lnFromUserId = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {
            int FromUserID = clsFeltProductionApprovalFlow.getFromID(703, ObjFeltWeaveout.getAttribute("PRODUCTION_DOCUMENT_NO").getString());
            lnFromUserId = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(703, FromUserID, ObjFeltWeaveout.getAttribute("PRODUCTION_DOCUMENT_NO").getString());

            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateSendToCombo();

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

        //In Edit Mode Hierarchy and Reject Should be disabled
        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(703, txtProductionDocumentNo.getText())) {
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

    private void SetMenuForRights() {
        // --- Add Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6071, 60711)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6071, 60712)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6071, 60713)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6071, 60714)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }

    private void Add() {
        jPopupMenu1.show(cmdNew, 0, 35);
        /*EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SetupApproval();
        lblTitle.setBackground(Color.GRAY);
                */
        //WeaveoutNo();
        //FormatGridWoutSelection();
        //GenerateListWoutSelection();
    }

    private void Edit() {
        String productionDocumentNo = (String) ObjFeltWeaveout.getAttribute("PRODUCTION_DOCUMENT_NO").getObj();
        if (ObjFeltWeaveout.IsEditable(productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            GenerateUserNameCombo();

            if (clsFeltProductionApprovalFlow.IsCreator(703, productionDocumentNo)) {
                SetFields(true);
                txtFeltProductionDate.setEnabled(false);
                //txtFormNo.setEnabled(false);
            } else {
                cmbUserName.setEnabled(true);
                EnableApproval();
            }
        } else {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "EDITING ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Delete() {
        if (ObjFeltWeaveout.CanDelete(txtProductionDocumentNo.getText(), txtFeltProductionDate.getText(), EITLERPGLOBAL.gNewUserID)) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, ObjFeltWeaveout.LastError, "DELETION ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Save() {
        String productionDate, productionDocumentNo;
        float weight = 0, reedSpace = 0, length = 0, THweight = 0 ,pick=0, pickmtr=0;        
        int loomNo = 0, i = 0, j = 0, weave_diff_days = 0;
        String pieceNo = "", shiftNo = "", partyCode = "", remarks = "", warp_no = "", weave_date = "";
        String weightString = "", reedSpaceString = "", lengthString = "", picksPer10CMS = "", loomNoString = "", THweightString = "";
        productionDate = txtFeltProductionDate.getText().trim();
        shiftNo = txtShiftNo.getText().trim();
        String productCode="",groupCode="",beamNo="";

        //Form level validations
        if (productionDate.equals("") || !EITLERPGLOBAL.isDate(productionDate)) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Valid Production Date.", "Wrong Production Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // check Production Date is Within Financial Year?
        java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
        java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
        java.sql.Date ProductionDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(productionDate));
        if ((ProductionDate.after(FinFromDate) || ProductionDate.compareTo(FinFromDate) == 0) && (ProductionDate.before(FinToDate) || ProductionDate.compareTo(FinToDate) == 0)) {
            //Within the year
        } else {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Production Date is Not Within Financial Year.", "FINANCIAL YEAR ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ProductionDate.compareTo(java.sql.Date.valueOf(EITLERPGLOBAL.getCurrentDateDB())) > 0) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Production Date Must be Before or the Same Date as Today.", "Wrong Production Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // creating document no
        if (EditMode == EITLERPGLOBAL.ADD) {
            WeaveoutNo();
        }
        //productionDocumentNo = "FW" + productionDate.substring(6, 10) + productionDate.substring(3, 5) + productionDate.substring(0, 2);
        productionDocumentNo=txtProductionDocumentNo.getText();
        // CHECK Production Weaving Date already exist in database?        
        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjFeltWeaveout.checkProductionDateInDB(EITLERPGLOBAL.formatDateDB(productionDate))) {
                JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Production Date " + productionDate + " Already Exists.", "Production Date Exists ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (shiftNo.equals("") || shiftNo.equals(null)) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Form No.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // user name selection validation
        if (EITLERPGLOBAL.getComboCode(cmbUserName) == 0) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Select Your User Name", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        

        //Check the no. of items in table
        if (Table_Pieces.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Weaveout Details Before Saving or Review selected piece by selecting 'Summary Report' tab!!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // check duplicate piece no in table
            for (int k = 0; k <= Table_Pieces.getRowCount() - 1; k++) {
                for (int l = k; l <= Table_Pieces.getRowCount() - 1; l++) {
                    if (l != k && ((String) Table_Pieces.getValueAt(k, 1)).trim().equals(((String) Table_Pieces.getValueAt(l, 1)).trim())) {
                        JOptionPane.showMessageDialog(this, "Same Piece No at Row " + (k + 1) + " and " + (l + 1), "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            ObjFeltWeaveout.hmFeltWeavingDetails.clear();
            //Check the entered details in Table.
            for (i = 0; i <= Table_Pieces.getRowCount() - 1; i++) {
                j++;
                pieceNo = ((String) Table_Pieces.getValueAt(i, 1)).trim().toUpperCase();
                if (pieceNo.equals("") || pieceNo.equals(null)) {
                    JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Piece No.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                j++;
                partyCode = ((String) Table_Pieces.getValueAt(i, 2)).trim();
                if (partyCode.equals("") || partyCode.equals(null)) {
                    JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Party Code.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                j++;
                productCode=((String) Table_Pieces.getValueAt(i, 3)).trim();
                System.out.println(productCode);             
                
                j++;
//                weightString = ((String) Table.getValueAt(i, 3)).trim();
                //weightString = ((String) Table_Pieces.getValueAt(i, 4)).trim();
                groupCode=((String) Table_Pieces.getValueAt(i, 4)).trim();
                System.out.println(groupCode);

                j++;               
                
                loomNoString = Table_Pieces.getValueAt(i, 5).toString().trim();
                if (loomNoString.equals("") || loomNoString.equals(null)) {
                    JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Loom No.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    loomNo = Integer.parseInt(loomNoString);
                }
                
                j++;
                beamNo=((String) Table_Pieces.getValueAt(i, 6)).trim();
                System.out.println(beamNo);
                
                j++;
//                picksPer10CMS = ((String) Table.getValueAt(i, 4)).trim();
                picksPer10CMS = ((String) Table_Pieces.getValueAt(i, 7)).trim();                
                if (picksPer10CMS.equals("") || picksPer10CMS.equals(null)) {
                    JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Pics Per 10 CMS", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                j++;                
                pick=Float.parseFloat(Table_Pieces.getValueAt(i, 8).toString().trim());

                j++;
//                lengthString = ((String) Table.getValueAt(i, 6)).trim();
                lengthString = ((String) Table_Pieces.getValueAt(i, 9)).trim();
                if (lengthString.equals("") || lengthString.equals(null)) {
                    JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Length of Felt.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    length = Float.parseFloat(lengthString);
                }
                
                j++;
//                reedSpaceString = ((String) Table.getValueAt(i, 5)).trim();
                reedSpaceString = ((String) Table_Pieces.getValueAt(i, 10)).trim();
                if (reedSpaceString.equals("") || reedSpaceString.equals(null)) {
                    JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Reed Space.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    reedSpace = Float.parseFloat(reedSpaceString);
                }

                

                j++;
//                loomNoString = Table.getValueAt(i, 7).toString().trim();                
                

                

                // check piece no is already weaved?
                if (EditMode == EITLERPGLOBAL.ADD) {
                    if (ObjFeltWeaveout.checkPieceNoInDB(pieceNo)) {
                        JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Piece No." + pieceNo + " Is Already Weaved.", " Piece No Already Exists", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // check piece no is already weaved?
                if (EditMode == EITLERPGLOBAL.EDIT) {
                    if (ObjFeltWeaveout.checkPieceNoInDB(pieceNo, productionDate)) {
                        JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Piece No." + pieceNo + " Is Already Weaved.", " Piece No Already Exists", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                String machineNo = data.getStringValueFromDB("SELECT WIP_MACHINE_NO FROM PRODUCTION.FELT_WIP_PIECE_REGISTER  WHERE WIP_EXT_PIECE_NO='" + pieceNo + "' ");
                String positionNo = data.getStringValueFromDB("SELECT WIP_POSITION_NO FROM PRODUCTION.FELT_WIP_PIECE_REGISTER  WHERE WIP_EXT_PIECE_NO='" + pieceNo + "' ");

                if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + partyCode + "' ")) {
                    JOptionPane.showMessageDialog(null, "Party closed in Party Master.");
                    return;
                } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MACHINE_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCode + "' AND MM_MACHINE_NO='" + machineNo + "' ")) {
                    JOptionPane.showMessageDialog(null, "Party Machine closed in Machine Master at Row : " + (i + 1));
                    return;
                } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE POSITION_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCode + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "' ")) {
                    JOptionPane.showMessageDialog(null, "Party Machine Position closed in Machine Master at Row : " + (i + 1));
                    return;
                }
                
                
                
                clsFeltWeaveoutDetails ObjFeltWeavingDetails = new clsFeltWeaveoutDetails();

                ObjFeltWeavingDetails.setAttribute("PRODUCTION_PIECE_NO", pieceNo);
                ObjFeltWeavingDetails.setAttribute("PRODUCTION_PARTY_CODE", partyCode);
                ObjFeltWeavingDetails.setAttribute("PRODUCT_CODE", productCode);                
                ObjFeltWeavingDetails.setAttribute("GROUP", groupCode);
                ObjFeltWeavingDetails.setAttribute("LOOM_NO", loomNo);
                ObjFeltWeavingDetails.setAttribute("BEAM_NO", beamNo);
                ObjFeltWeavingDetails.setAttribute("PICKS_PER_10CMS", picksPer10CMS);
                ObjFeltWeavingDetails.setAttribute("PICK", pick);                
                ObjFeltWeavingDetails.setAttribute("LENGTH", length);                
                ObjFeltWeavingDetails.setAttribute("REED_SPACE", reedSpace);
                

                ObjFeltWeaveout.hmFeltWeavingDetails.put(Integer.toString(ObjFeltWeaveout.hmFeltWeavingDetails.size() + 1), ObjFeltWeavingDetails);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter Correct Details at Row " + (i + 1) + " and Column " + (j + 1) + ". Error is " + nfe.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            j = 0;
            nfe.printStackTrace();
            return;
        }

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Select the hierarchy.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Select the Approval Action.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Enter the remarks for rejection", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Select the user, to whom rejected document to be send", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //set data for insert/update
        ObjFeltWeaveout.setAttribute("PRODUCTION_DATE", productionDate);
        ObjFeltWeaveout.setAttribute("PRODUCTION_DOCUMENT_NO", productionDocumentNo);
        ObjFeltWeaveout.setAttribute("PRODUCTION_FORM_NO", shiftNo);
        SetData();

        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjFeltWeaveout.Insert()) {
                DisplayData();

                if (OpgFinal.isSelected()) {
                    try {
                        String DOC_NO = txtProductionDocumentNo.getText();
                        String DOC_DATE = txtFeltProductionDate.getText();
                        String Party_Code = "";

                        //String responce = JavaMail.sendFinalApprovalMail(603, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                        //String responce = JavaMail.sendNotificationMailOfDetail(703, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
                        //System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Error occured while saving. Error is " + ObjFeltWeaveout.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjFeltWeaveout.Update()) {
                DisplayData();

                if (OpgFinal.isSelected()) {
                    try {
                        String DOC_NO = txtProductionDocumentNo.getText();
                        String DOC_DATE = txtFeltProductionDate.getText();
                        String Party_Code = "";

                        //String responce = JavaMail.sendFinalApprovalMail(603, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                        //String responce = JavaMail.sendNotificationMailOfDetail(703, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
                        //System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Error occured while saving editing. Error is " + ObjFeltWeaveout.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        try {
            if (PENDING_DOCUMENT) {
                frmPA.RefreshView();
                PENDING_DOCUMENT = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Sets data to the Details Class Object
    private void SetData() {
        //-------- Update Approval Specific Fields -----------//
        ObjFeltWeaveout.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjFeltWeaveout.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjFeltWeaveout.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjFeltWeaveout.setAttribute("FROM_REMARKS", txtToRemarks.getText().trim());
        ObjFeltWeaveout.setAttribute("UPDATED_BY", EITLERPGLOBAL.getComboCode(cmbUserName));
        //ObjFeltWeaving.setAttribute("UPDATED_BY",EITLERPGLOBAL.gNewUserID);
        if (OpgApprove.isSelected()) {
            ObjFeltWeaveout.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjFeltWeaveout.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjFeltWeaveout.setAttribute("APPROVAL_STATUS", "R");
            ObjFeltWeaveout.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjFeltWeaveout.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjFeltWeaveout.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
        } else {
            ObjFeltWeaveout.setAttribute("CREATED_BY", (int) ObjFeltWeaveout.getAttribute("CREATED_BY").getVal());
            ObjFeltWeaveout.setAttribute("CREATED_DATE", ObjFeltWeaveout.getAttribute("CREATED_DATE").getString());
            ObjFeltWeaveout.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
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
        Loader ObjLoader = new Loader(this, "EITLERP.Production.FeltWeaveout.frmFindFeltWeaveout", true);
        frmFindFeltWeaveout ObjFindFeltWeaveout = (frmFindFeltWeaveout) ObjLoader.getObj();

        if (ObjFindFeltWeaveout.Cancelled == false) {
            if (!ObjFeltWeaveout.Filter(ObjFindFeltWeaveout.stringFindQuery)) {
                JOptionPane.showMessageDialog(frmFeltWeaveout.this, " No records found.", "Find Felt Weaveout Details", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    // find details by production date
    public void Find(String prodDate) {
        ObjFeltWeaveout.Filter("AND WVOUT_DATE='" + prodDate + "'");
        SetMenuForRights();
        DisplayData();
    }

    // find details by piece no.
    public void Find(String pieceNo, String prodDate) {
        ObjFeltWeaveout.Filter("AND WVOUT_PIECE_NO+0='" + pieceNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    // find all pending document
    public void FindWaiting() {
        ObjFeltWeaveout.Filter(" AND WVOUT_DOC_NO IN (SELECT DISTINCT WVOUT_DOC_NO FROM PRODUCTION.FELT_WVOUT_DATA, PRODUCTION.FELT_PROD_DOC_DATA WHERE WVOUT_DOC_NO=DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=703 AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    private void MoveFirst() {
        ObjFeltWeaveout.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjFeltWeaveout.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjFeltWeaveout.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjFeltWeaveout.MoveLast();
        DisplayData();
    }

    private void ShowMessage(String pMessage) {
        
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
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    Tab1.getComponent(i).setEnabled(true);
                }
            }
        }
        //=============== Header Fields Setup Complete =================//

        //=============== Setting Table Fields ==================//
        DataModel.ClearAllReadOnly();
        Table_Pieces.setEnabled(false);
    }

    private void FormatGridApprovalStatus() {
        DataModelApprovalStatus = new EITLTableModel();

        TableApprovalStatus.removeAll();
        TableApprovalStatus.setModel(DataModelApprovalStatus);

        //Set the table Readonly
        DataModelApprovalStatus.TableReadOnly(true);

        //Add the columns
        DataModelApprovalStatus.addColumn("Sr.");
        DataModelApprovalStatus.addColumn("User");
        DataModelApprovalStatus.addColumn("Department");
        DataModelApprovalStatus.addColumn("Status");
        DataModelApprovalStatus.addColumn("Received Date");
        DataModelApprovalStatus.addColumn("Action Date");
        DataModelApprovalStatus.addColumn("Remarks");
    }

    private void FormatGridUpdateHistory() {
        DataModelUpdateHistory = new EITLTableModel();

        TableUpdateHistory.removeAll();
        TableUpdateHistory.setModel(DataModelUpdateHistory);

        //Set the table Readonly
        DataModelUpdateHistory.TableReadOnly(true);

        //Add the columns
        DataModelUpdateHistory.addColumn("Rev No.");
        DataModelUpdateHistory.addColumn("User");
        DataModelUpdateHistory.addColumn("Date");
        DataModelUpdateHistory.addColumn("Status");
        DataModelUpdateHistory.addColumn("Remarks");
        DataModelUpdateHistory.addColumn("From Ip");
    }

    private void WeaveoutNo() {
        String productionDate, productionDocumentNo;
        productionDate = txtFeltProductionDate.getText().trim();

        //Form level validations
        if (productionDate.equals("") || !EITLERPGLOBAL.isDate(productionDate)) {
            JOptionPane.showMessageDialog(this, "Enter Valid ShiftWise Weaveout  Date", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // check Production Date is Within Financial Year?
        java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
        java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
        java.sql.Date ProductionDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(productionDate));
        if ((ProductionDate.after(FinFromDate) || ProductionDate.compareTo(FinFromDate) == 0) && (ProductionDate.before(FinToDate) || ProductionDate.compareTo(FinToDate) == 0)) {
            //Within the year
        } else {
            JOptionPane.showMessageDialog(this, "ShiftWise Weaving  Date is Not Within Financial Year.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ProductionDate.compareTo(java.sql.Date.valueOf(EITLERPGLOBAL.getCurrentDateDB())) > 0) {
            JOptionPane.showMessageDialog(this, "ShiftWise Weaveout  Date Must be Before or the Same Date as Today.", "Wrong Production Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generating Document No
        productionDocumentNo = "FW" + ShiftNo1 + productionDate.substring(6, 10) + productionDate.substring(3, 5) + productionDate.substring(0, 2);
        txtProductionDocumentNo.setText(productionDocumentNo);
        if (EditMode == EITLERPGLOBAL.EDIT) {
            productionDocumentNo = ObjFeltWeaveout.getAttribute("PRODUCTION_DOCUMENT_NO").getString();
        }

        // CHECK Document No already exist in database?
        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjFeltWeaveout.checkProductionDocumentNoInDB(productionDocumentNo)) {

                JOptionPane.showMessageDialog(frmFeltWeaveout.this, "Production Weaveout Date " + productionDate + " is Already Exists", "Production Weaveout Date Already Exists", JOptionPane.ERROR_MESSAGE);
                txtFeltProductionDate.setEnabled(true);
                txtFeltProductionDate.setText("");
                txtProductionDocumentNo.setText(productionDocumentNo);
                return;
            }
        }

        if (productionDocumentNo.equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Document No.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }      
        
        txtProductionDocumentNo.setText(productionDocumentNo);
    }
    
    private void GenerateListWoutSelection() {
        String sql = "";
        String cndtn = "";       

        try {

            sql = "SELECT H.LOOM_NO,H.BEAM_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,THEORICAL_PICKS_10_CM,TOTAL_PICKS,THEORICAL_LENGTH_MTR,READ_SPACE FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D,\n"
                    + "PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER H \n"
                    + "WHERE H.DOC_NO = D.DOC_NO \n"
                    + "AND H.APPROVED =1 AND COALESCE(WEAVING_DATE,'0000-00-00') ='0000-00-00' AND INDICATOR NOT IN ('DELETE') \n"
                    + "AND COALESCE(BEAM_CLOSURE_IND,0) != 1 \n"
                    + "UNION ALL \n"
                    + "SELECT H.LOOM_NO,H.BEAM_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,THEORICAL_PICKS_10_CM,TOTAL_PICKS,THEORICAL_LENGTH_MTR,READ_SPACE FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL D,\n"
                    + "PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_HEADER H \n"
                    + "WHERE H.DOC_NO = D.DOC_NO \n"
                    + "AND H.APPROVED =1 AND COALESCE(WEAVING_DATE,'0000-00-00') ='0000-00-00' AND INDICATOR NOT IN ('DELETE') \n"
                    + "AND COALESCE(BEAM_CLOSURE_IND,0) != 1 \n"
                    + "ORDER BY LOOM_NO,BEAM_NO,PIECE_NO";
            System.out.println("sql : " + sql);

            ResultSet rsTmp = data.getResult(sql);
            rsTmp.first();
            int p = 0;
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    p = 0;
                    Object[] rowData = new Object[50];
                    //rowData[p] = false;
                    //p++;
                    if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='"+txtProductionDocumentNo.getText().trim()+"' AND PIECE_NO='" + rsTmp.getString("PIECE_NO") + "'")) {
                        rowData[p] = true;
                        p++;
                    } else {
                        rowData[p] = false;
                        p++;
                    }
                    rowData[p] = Integer.toString(cnt);
                    p++;
                    rowData[p] = rsTmp.getString("PIECE_NO");
                    p++;
                    rowData[p] = rsTmp.getString("PARTY_CODE");
                    p++;
                    rowData[p] = rsTmp.getString("PRODUCT_CODE");
                    p++;
                    rowData[p] = rsTmp.getString("GRUP");
                    p++;
                    rowData[p] = rsTmp.getString("LOOM_NO");
                    p++;
                    rowData[p] = rsTmp.getString("BEAM_NO");
                    p++;
                    rowData[p] = rsTmp.getString("THEORICAL_PICKS_10_CM");
                    p++;
                    rowData[p] = rsTmp.getString("TOTAL_PICKS");                    
                    p++;
                    rowData[p] = rsTmp.getString("THEORICAL_LENGTH_MTR");                    
                    p++;
                    rowData[p] = rsTmp.getString("READ_SPACE");                    
                    p++;
//                    rowData[p] = EITLERPGLOBAL.round(rsTmp.getDouble("PR_FELT_VALUE_WITH_GST"), 0);
//                    p++;
//                    FeltInvCalc inv_calc;
//                    try {
//                        inv_calc = clsOrderValueCalc.calculateWithoutGSTINNO(
//                                rsTmp.getString("PR_PIECE_NO"),
//                                rsTmp.getString("PR_BILL_PRODUCT_CODE"),
//                                rsTmp.getString("PR_PARTY_CODE"),
//                                Float.parseFloat(rsTmp.getString("PR_BILL_LENGTH")),
//                                Float.parseFloat(rsTmp.getString("PR_BILL_WIDTH")),
//                                Float.parseFloat(rsTmp.getString("PR_BILL_WEIGHT")),
//                                Float.parseFloat(rsTmp.getString("PR_BILL_SQMTR")),
//                                EITLERPGLOBAL.getCurrentDateDB());
//
//                        rowData[p] = EITLERPGLOBAL.round(inv_calc.getFicInvAmt(),0);
//                        p++;
//                    } catch (Exception e) {
//                        System.out.println("Erro on PIECE REGISTER : " + e.getMessage());
//                    }

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateListLoomwiseSelection() {
        String sql = "";
        String cndtn = "";       

        try {

            sql = "SELECT H.LOOM_NO,H.BEAM_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,THEORICAL_PICKS_10_CM,TOTAL_PICKS,THEORICAL_LENGTH_MTR,READ_SPACE FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D,\n"
                    + "PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER H \n"
                    + "WHERE H.DOC_NO = D.DOC_NO \n"
                    + "AND H.APPROVED =1 AND COALESCE(WEAVING_DATE,'0000-00-00') ='0000-00-00' AND INDICATOR NOT IN ('DELETE') \n"
                    + "AND COALESCE(BEAM_CLOSURE_IND,0) != 1 \n"
                    + "UNION ALL \n"
                    + "SELECT H.LOOM_NO,H.BEAM_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,THEORICAL_PICKS_10_CM,TOTAL_PICKS,THEORICAL_LENGTH_MTR,READ_SPACE FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL D,\n"
                    + "PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_HEADER H \n"
                    + "WHERE H.DOC_NO = D.DOC_NO \n"
                    + "AND H.APPROVED =1 AND COALESCE(WEAVING_DATE,'0000-00-00') ='0000-00-00' AND INDICATOR NOT IN ('DELETE') \n"
                    + "AND COALESCE(BEAM_CLOSURE_IND,0) != 1 \n"
                    + "ORDER BY LOOM_NO,BEAM_NO,PIECE_NO";
            System.out.println("sql : " + sql);

            ResultSet rsTmp = data.getResult(sql);
            rsTmp.first();
            int p = 0;
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                while (!rsTmp.isAfterLast()) {
                    cnt++;
                    p = 0;
                    Object[] rowData = new Object[50];
                    //rowData[p] = false;
                    //p++;
                    if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='"+txtProductionDocumentNo.getText().trim()+"' AND PIECE_NO='" + rsTmp.getString("PIECE_NO") + "'")) {
                        rowData[p] = true;
                        p++;
                    } else {
                        rowData[p] = false;
                        p++;
                    }
                    rowData[p] = Integer.toString(cnt);
                    p++;
                    rowData[p] = rsTmp.getString("PIECE_NO");
                    p++;
                    rowData[p] = rsTmp.getString("PARTY_CODE");
                    p++;
                    rowData[p] = rsTmp.getString("PRODUCT_CODE");
                    p++;
                    rowData[p] = rsTmp.getString("GRUP");
                    p++;
                    rowData[p] = rsTmp.getString("LOOM_NO");
                    p++;
                    rowData[p] = rsTmp.getString("BEAM_NO");
                    p++;
                    rowData[p] = rsTmp.getString("THEORICAL_PICKS_10_CM");
                    p++;
                    rowData[p] = rsTmp.getString("TOTAL_PICKS");                    
                    p++;
                    rowData[p] = rsTmp.getString("THEORICAL_LENGTH_MTR");                    
                    p++;
                    rowData[p] = rsTmp.getString("READ_SPACE");                    
                    p++;
//                    rowData[p] = EITLERPGLOBAL.round(rsTmp.getDouble("PR_FELT_VALUE_WITH_GST"), 0);
//                    p++;
//                    FeltInvCalc inv_calc;
//                    try {
//                        inv_calc = clsOrderValueCalc.calculateWithoutGSTINNO(
//                                rsTmp.getString("PR_PIECE_NO"),
//                                rsTmp.getString("PR_BILL_PRODUCT_CODE"),
//                                rsTmp.getString("PR_PARTY_CODE"),
//                                Float.parseFloat(rsTmp.getString("PR_BILL_LENGTH")),
//                                Float.parseFloat(rsTmp.getString("PR_BILL_WIDTH")),
//                                Float.parseFloat(rsTmp.getString("PR_BILL_WEIGHT")),
//                                Float.parseFloat(rsTmp.getString("PR_BILL_SQMTR")),
//                                EITLERPGLOBAL.getCurrentDateDB());
//
//                        rowData[p] = EITLERPGLOBAL.round(inv_calc.getFicInvAmt(),0);
//                        p++;
//                    } catch (Exception e) {
//                        System.out.println("Erro on PIECE REGISTER : " + e.getMessage());
//                    }

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
private void GeneratePiecesList() {
        String cndtn = "";
        
        String sql;
        sql = "SELECT  \n"
            + "'' AS 'SrNo',PIECE_NO AS 'Piece No',PARTY_CODE AS 'Party Code',PRODUCT_CODE AS 'Product Code',\n"
            + "PRODUCT_GROUP AS 'Group',LOOM_NO AS 'Loom No',BEAM_NO AS 'Beam No',THEORICAL_PICKS_10_CM AS 'Th Picks/10CMS',TOTAL_PICKS AS 'Total Picks',THEORICAL_LENGTH_MTR AS 'Th Length', \n"
            + "REED_SPACE AS 'Reed Space'  FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='"+txtProductionDocumentNo.getText().trim()+"' ORDER BY LOOM_NO,BEAM_NO";
//        System.out.println("sql : " + sql);

        Table_Pieces.setEnabled(true);
        ResultSet rs = data.getResult(sql);
        try {
            rs.first();
            if (rs.getRow() > 0) {
                DataModelWvoutPieces = new EITLTableModel();
                Table_Pieces.removeAll();

                Table_Pieces.setModel(DataModelWvoutPieces);
                Table_Pieces.setAutoResizeMode(0);
                ResultSetMetaData rsInfo = rs.getMetaData();

                //Format the table from the resultset meta data
                int i = 1;
                DataModelWvoutPieces.ClearAllReadOnly();
                DataModelWvoutPieces.SetReadOnly(0);

                for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                    DataModelWvoutPieces.addColumn(rsInfo.getColumnName(i));
                    DataModelWvoutPieces.SetReadOnly(i);
                }
                int m = 1;
                while (!rs.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    rowData[0] = Integer.toString(m);
                    rowData[1] = rs.getString(2);
                    rowData[2] = rs.getString(3);
                    rowData[3] = rs.getString(4);
                    for (int k = 4; k < (i - 1); k++) {
                        rowData[k] = rs.getString(k + 1);
                    }
                    DataModelWvoutPieces.addRow(rowData);
                    rs.next();
                    m++;
                }
                final TableColumnModel columnModel = Table_Pieces.getColumnModel();
                for (int column = 0; column < Table_Pieces.getColumnCount(); column++) {
                    int width = 60; // Min width
                    for (int row = 0; row < Table_Pieces.getRowCount(); row++) {
                        TableCellRenderer renderer_pieces = Table_Pieces.getCellRenderer(row, column);
                        Component comp = Table_Pieces.prepareRenderer(renderer_pieces, row, column);
                        width = Math.max(comp.getPreferredSize().width + 1, width);
                    }
                    if (width > 150) {
                        width = 150;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }                                
                
//                Table_Pieces.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());
//                Table_Pieces.getTableHeader().setPreferredSize(new Dimension(Table_Pieces.getColumnModel().getTotalColumnWidth(), 100));
                DataModelWvoutPieces.TableReadOnly(true);
            } else {
                for (int i = DataModelWvoutPieces.getRowCount() - 1; i >= 0; i--) {
                    DataModelWvoutPieces.removeRow(i);
                }
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private void GenerateLoomwiseSelectionData() {        
        String loomno = ""; 
        
        int mnoofmachine = 0;
        int nooflooms=0;
        try {
            tabSelectionLoom.removeAll();

            //JPanel machines = new JPanel();
            JPanel looms = new JPanel();
            ResultSet r;
            
           String sql = "SELECT H.LOOM_NO,H.BEAM_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,THEORICAL_PICKS_10_CM,TOTAL_PICKS,THEORICAL_LENGTH_MTR,READ_SPACE FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DETAIL D,\n"
                    + "PRODUCTION.FELT_WARPING_BEAM_ORDER_HEADER H \n"
                    + "WHERE H.DOC_NO = D.DOC_NO \n"
                    + "AND H.APPROVED =1 AND COALESCE(WEAVING_DATE,'0000-00-00') ='0000-00-00' AND INDICATOR NOT IN ('DELETE') \n"
                    + "AND COALESCE(BEAM_CLOSURE_IND,0) != 1 \n"
                    + "UNION ALL \n"
                    + "SELECT H.LOOM_NO,H.BEAM_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,GRUP,THEORICAL_PICKS_10_CM,TOTAL_PICKS,THEORICAL_LENGTH_MTR,READ_SPACE FROM PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_DETAIL D,\n"
                    + "PRODUCTION.FELT_WARPING_BEAM_ORDER_DRYER_HEADER H \n"
                    + "WHERE H.DOC_NO = D.DOC_NO \n"
                    + "AND H.APPROVED =1 AND COALESCE(WEAVING_DATE,'0000-00-00') ='0000-00-00' AND INDICATOR NOT IN ('DELETE') \n"
                    + "AND COALESCE(BEAM_CLOSURE_IND,0) != 1 \n"
                    + "ORDER BY LOOM_NO,BEAM_NO,PIECE_NO";
            r = data.getResult(sql);
            r.first();
            String mloom = "";                       
            
            int i = 0;            
            
            mloom = r.getString("LOOM_NO");
            loomno = mloom;
            looms = new JPanel();
            looms.setLayout(null);
            tabSelectionLoom.removeAll();
            tabSelectionLoom.add("LM" + r.getString("LOOM_NO"), looms);

            tbl[i] = new JTable();

            //scrollpane[i]=new JScrollPane();
            //scrollpane[i].add(tblPress[i]);
            DataModel_tbl[i] = new EITLTableModel();
            tbl[i].removeAll();
            tbl[i].setModel(DataModel_tbl[i]);
            //tblDryer.setBounds(10, 10, 500, 100);
            tbl[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            //tbl[i].setAutoResizeMode(0);
            tbl[i].setRowSelectionAllowed(true);
            tbl[i].setEnabled(true);

            DataModel_tbl[i].addColumn("Select");
            DataModel_tbl[i].addColumn("Sr. No."); //0 - Read Only
            DataModel_tbl[i].addColumn("Piece No"); //1
            DataModel_tbl[i].addColumn("Party Code"); //2
            DataModel_tbl[i].addColumn("Product Code"); //3
            DataModel_tbl[i].addColumn("Group"); //4
            DataModel_tbl[i].addColumn("Loom No"); //5
            DataModel_tbl[i].addColumn("Beam No"); //6
            DataModel_tbl[i].addColumn("Th Picks/10CMS"); //7
            DataModel_tbl[i].addColumn("Total Picks"); //6
            DataModel_tbl[i].addColumn("Th Length"); //7
            DataModel_tbl[i].addColumn("Reed Space"); //8                       
            
            Render.setCustomComponent(0, "CheckBox");
            tbl[i].getColumnModel().getColumn(0).setCellRenderer(Render);
            tbl[i].getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

            for (int ro = 1; ro < 119; ro++) {
                   DataModel_tbl[i].SetReadOnly(ro);
            }           
            
            if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
            tbl[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    JTable source = (JTable)evt.getSource();
                    int row = source.rowAtPoint( evt.getPoint() );
                    String woutDocNo = txtProductionDocumentNo.getText().trim();
                    String tick = source.getModel().getValueAt(row, 0).toString();
                    String pieceNo = source.getValueAt(row, 2).toString();
                    String partyCode = source.getValueAt(row, 3).toString();
                    String prodCode = source.getValueAt(row, 4).toString();
                    String prodGroup = source.getValueAt(row, 5).toString();
                    String loomNo = source.getValueAt(row, 6).toString();
                    String beamNo = source.getValueAt(row, 7).toString();
                    String picksper10cms = source.getValueAt(row, 8).toString();
                    String totalPicks = source.getValueAt(row, 9).toString();
                    String length = source.getValueAt(row, 10).toString();
                    String reedSpace = source.getValueAt(row, 11).toString();
                    //            String ipAdd = data.getStringValueFromDB("SELECT SUBSTRING_INDEX(USER(),'@',-1)");
                    
                    System.out.println(tick+"  "+pieceNo+"  "+partyCode);
                    if (source.getSelectedColumn() == 0) {

            //            System.out.println("tick : "+tick);
                        if (tick.equalsIgnoreCase("true")) {
                            if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + woutDocNo + "' AND PIECE_NO='" + pieceNo + "'")) {
                                data.Execute("INSERT INTO PRODUCTION.FELT_WVOUT_SELECTION_DATA (WVOUT_DOC_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,"
                                        + " LOOM_NO,BEAM_NO,THEORICAL_PICKS_10_CM, TOTAL_PICKS, THEORICAL_LENGTH_MTR,REED_SPACE) "
                                        + "VALUES ('" + woutDocNo + "','" + pieceNo + "','" + partyCode + "','" + prodCode + "','" + prodGroup + "','" + loomNo + "','" + beamNo + "','" + picksper10cms + "','" + totalPicks + "','" + length + "','" + reedSpace + "')");
                            }
                            /*data.Execute("INSERT INTO PRODUCTION.FELT_WVOUT_SELECTION_DATA (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, PIECE_STAGE, UPN, ENTRY_STATUS, ENTRY_TIME, USER_ID, IP_ADDRESS) "
                             + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','" + pieceStage + "','','ADD',NOW(),'" + EITLERPGLOBAL.gNewUserID + "',SUBSTRING_INDEX(USER(),'@',-1) ) ");
                             if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "'")) {
                             data.Execute("INSERT INTO PRODUCTION.FELT_DISPATCH_SELECTION_DATA (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, UPN, PIECE_STAGE) "
                             + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','','" + pieceStage + "') ");
                             }               
                             */
                        }

                        if (tick.equalsIgnoreCase("false")) {
                            if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + woutDocNo + "' AND PIECE_NO='" + pieceNo + "'")) {
                                data.Execute("DELETE FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + woutDocNo + "' AND PIECE_NO='" + pieceNo + "' ");
                            }

                            /*data.Execute("INSERT INTO PRODUCTION.FELT_DISPATCH_SELECTION_DATA_HISTORY (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, PIECE_STAGE, UPN, ENTRY_STATUS, ENTRY_TIME, USER_ID, IP_ADDRESS) "
                             + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','" + pieceStage + "','','DELETE',NOW(),'" + EITLERPGLOBAL.gNewUserID + "',SUBSTRING_INDEX(USER(),'@',-1) ) ");
                             if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "'")) {
                             data.Execute("DELETE FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "' ");
                             }*/
                        }

                    }

                            
                }
            });
            }
            
           /* final TableColumnModel columnModel1 = tbl[i].getColumnModel();
            for (int column = 0; column < tbl[i].getColumnCount(); column++) {
                int width = 60; // Min width
                for (int row = 0; row < tbl[i].getRowCount(); row++) {
                    TableCellRenderer renderer = tbl[i].getCellRenderer(row, column);
                    Component comp = tbl[i].prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 10, width);
                }
                if (width > 300) {
                    width = 300;
                }
                columnModel1.getColumn(column).setPreferredWidth(width);
            }            
            */
            JScrollPane jScrollPane2 = new JScrollPane();
            jScrollPane2.setViewportView(tbl[i]);

            looms.add(jScrollPane2);
            jScrollPane2.setBounds(0, 20, 980, 250);
            Object[] rowData = new Object[300];//20            
            
            int msr = 1;
            int msr1 = 1, pos = 0;

            while (!r.isAfterLast()) {
                if (mloom.equalsIgnoreCase(r.getString("LOOM_NO"))) {
                    rowData = new Object[300];
                     
                        pos = 0;
                        if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + txtProductionDocumentNo.getText().trim() + "' AND PIECE_NO='" + r.getString("PIECE_NO") + "'")) {
                        rowData[pos] = true;
                        pos++;
                    } else {
                        rowData[pos] = false;
                        pos++;
                    }
                        rowData[pos] = msr;
                        pos++;
                        rowData[pos] = r.getString("PIECE_NO");
                        pos++;
                        rowData[pos] = r.getString("PARTY_CODE");
                        pos++;
                        rowData[pos] = r.getString("PRODUCT_CODE");
                        pos++;
                        rowData[pos] = r.getString("GRUP");
                        pos++;
                        rowData[pos] = r.getString("LOOM_NO");
                        pos++;
                        rowData[pos] = r.getString("BEAM_NO");
                        pos++;
                        rowData[pos] = r.getString("THEORICAL_PICKS_10_CM");
                        pos++;
                        rowData[pos] = r.getString("TOTAL_PICKS");
                        pos++;
                        rowData[pos] = r.getString("THEORICAL_LENGTH_MTR") ;
                        pos++;
                        rowData[pos] = r.getString("READ_SPACE");

                        msr++;
                        DataModel_tbl[i].addRow(rowData);
                    
                } else {
                    i++;
                    mlabelno++;
                    JPanel loom = new JPanel();
                    loom = new JPanel();
                    loom.setLayout(null);

                    tabSelectionLoom.add("LM" + r.getString("LOOM_NO"), loom);

                    tbl[i] = new JTable();
                    DataModel_tbl[i] = new EITLTableModel();

                    tbl[i].removeAll();
                    tbl[i].setModel(DataModel_tbl[i]);
                    //tblDryer.setBounds(10, 10, 500, 100);
                    tbl[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    //tbl[i].setAutoResizeMode(0);

                    DataModel_tbl[i].addColumn("Select");
                    DataModel_tbl[i].addColumn("Sr. No."); //0 - Read Only
                    DataModel_tbl[i].addColumn("Piece No"); //1
                    DataModel_tbl[i].addColumn("Party Code"); //2
                    DataModel_tbl[i].addColumn("Product Code"); //3
                    DataModel_tbl[i].addColumn("Group"); //4
                    DataModel_tbl[i].addColumn("Loom No"); //5
                    DataModel_tbl[i].addColumn("Beam No"); //6
                    DataModel_tbl[i].addColumn("Th Picks/10CMS"); //7
                    DataModel_tbl[i].addColumn("Total Picks"); //6
                    DataModel_tbl[i].addColumn("Th Length"); //7
                    DataModel_tbl[i].addColumn("Reed Space"); //8   
                    
                    Render.setCustomComponent(0, "CheckBox");
                    tbl[i].getColumnModel().getColumn(0).setCellRenderer(Render);
                    tbl[i].getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

                    for (int ro = 1; ro < 119; ro++) {                        
                            DataModel_tbl[i].SetReadOnly(ro);
                        }
                    
                    if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {
                    tbl[i].addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            JTable source = (JTable) evt.getSource();
                            int row = source.rowAtPoint(evt.getPoint());
                            String woutDocNo = txtProductionDocumentNo.getText().trim();
                            String tick = source.getModel().getValueAt(row, 0).toString();
                            String pieceNo = source.getValueAt(row, 2).toString();
                            String partyCode = source.getValueAt(row, 3).toString();
                            String prodCode = source.getValueAt(row, 4).toString();
                            String prodGroup = source.getValueAt(row, 5).toString();
                            String loomNo = source.getValueAt(row, 6).toString();
                            String beamNo = source.getValueAt(row, 7).toString();
                            String picksper10cms = source.getValueAt(row, 8).toString();
                            String totalPicks = source.getValueAt(row, 9).toString();
                            String length = source.getValueAt(row, 10).toString();
                            String reedSpace = source.getValueAt(row, 11).toString();
                    //            String ipAdd = data.getStringValueFromDB("SELECT SUBSTRING_INDEX(USER(),'@',-1)");

                            System.out.println(tick + "  " + pieceNo + "  " + partyCode);
                            if (source.getSelectedColumn() == 0) {

                                //            System.out.println("tick : "+tick);
                                if (tick.equalsIgnoreCase("true")) {
                                    if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + woutDocNo + "' AND PIECE_NO='" + pieceNo + "'")) {
                                        data.Execute("INSERT INTO PRODUCTION.FELT_WVOUT_SELECTION_DATA (WVOUT_DOC_NO,PIECE_NO,PARTY_CODE,PRODUCT_CODE,PRODUCT_GROUP,"
                                                + " LOOM_NO,BEAM_NO,THEORICAL_PICKS_10_CM, TOTAL_PICKS, THEORICAL_LENGTH_MTR,REED_SPACE) "
                                                + "VALUES ('" + woutDocNo + "','" + pieceNo + "','" + partyCode + "','" + prodCode + "','" + prodGroup + "','" + loomNo + "','" + beamNo + "','" + picksper10cms + "','" + totalPicks + "','" + length + "','" + reedSpace + "')");
                                    }
                                    /*data.Execute("INSERT INTO PRODUCTION.FELT_WVOUT_SELECTION_DATA (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, PIECE_STAGE, UPN, ENTRY_STATUS, ENTRY_TIME, USER_ID, IP_ADDRESS) "
                                     + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','" + pieceStage + "','','ADD',NOW(),'" + EITLERPGLOBAL.gNewUserID + "',SUBSTRING_INDEX(USER(),'@',-1) ) ");
                                     if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "'")) {
                                     data.Execute("INSERT INTO PRODUCTION.FELT_DISPATCH_SELECTION_DATA (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, UPN, PIECE_STAGE) "
                                     + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','','" + pieceStage + "') ");
                                     }               
                                     */
                                }

                                if (tick.equalsIgnoreCase("false")) {
                                    if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + woutDocNo + "' AND PIECE_NO='" + pieceNo + "'")) {
                                        data.Execute("DELETE FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + woutDocNo + "' AND PIECE_NO='" + pieceNo + "' ");
                                    }

                                    /*data.Execute("INSERT INTO PRODUCTION.FELT_DISPATCH_SELECTION_DATA_HISTORY (SELECTION_DATE, PIECE_NO, PARTY_CODE, INCHARGE_NAME, PIECE_STAGE, UPN, ENTRY_STATUS, ENTRY_TIME, USER_ID, IP_ADDRESS) "
                                     + "VALUES (CURDATE(),'" + pieceNo + "','" + partyCode + "','','" + pieceStage + "','','DELETE',NOW(),'" + EITLERPGLOBAL.gNewUserID + "',SUBSTRING_INDEX(USER(),'@',-1) ) ");
                                     if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "'")) {
                                     data.Execute("DELETE FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() AND PIECE_NO='" + pieceNo + "' ");
                                     }*/
                                }

                            }

                        }
                    });
                    }
                    
                    /*
                    tbl[i].getColumnModel().getColumn(0).setMaxWidth(30);
                    tbl[i].getColumnModel().getColumn(1).setMinWidth(90);
                    tbl[i].getColumnModel().getColumn(2).setMinWidth(80);
                    tbl[i].getColumnModel().getColumn(3).setMaxWidth(50);
                    tbl[i].getColumnModel().getColumn(4).setMinWidth(50);
                    tbl[i].getColumnModel().getColumn(5).setMaxWidth(60);
                    tbl[i].getColumnModel().getColumn(6).setMaxWidth(60);
                    tbl[i].getColumnModel().getColumn(8).setMinWidth(90);
                    tbl[i].getColumnModel().getColumn(9).setMinWidth(80);
                    tbl[i].getColumnModel().getColumn(10).setMinWidth(80);
                    tbl[i].getColumnModel().getColumn(11).setMinWidth(80);
                            */
                    //tbl[i].getColumnModel().getColumn(12).setMinWidth(80);
                    //tbl[i].getColumnModel().getColumn(12).setMinWidth(80);
                                      

                    jScrollPane2 = new JScrollPane();
                    jScrollPane2.setViewportView(tbl[i]);

                    loom.add(jScrollPane2);
                    jScrollPane2.setBounds(0, 20, 950, 250);                  
                    

                    
                    mloom = r.getString("LOOM_NO");
                    msr = msr1 = 1;
                    rowData = new Object[300];
                        pos = 0;
                        if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_WVOUT_SELECTION_DATA WHERE WVOUT_DOC_NO='" + txtProductionDocumentNo.getText().trim() + "' AND PIECE_NO='" + r.getString("PIECE_NO") + "'")) {
                        rowData[pos] = true;
                        pos++;
                    } else {
                        rowData[pos] = false;
                        pos++;
                    }
                        rowData[pos] = msr;
                        pos++;
                        rowData[pos] = r.getString("PIECE_NO");
                        pos++;
                        rowData[pos] = r.getString("PARTY_CODE");
                        pos++;
                        rowData[pos] = r.getString("PRODUCT_CODE");
                        pos++;
                        rowData[pos] = r.getString("GRUP");
                        pos++;
                        rowData[pos] = r.getString("LOOM_NO");
                        pos++;
                        rowData[pos] = r.getString("BEAM_NO");
                        pos++;
                        rowData[pos] = r.getString("THEORICAL_PICKS_10_CM");
                        pos++;
                        rowData[pos] = r.getString("TOTAL_PICKS");
                        pos++;
                        rowData[pos] = r.getString("THEORICAL_LENGTH_MTR") ;
                        pos++;
                        rowData[pos] = r.getString("READ_SPACE");
                        

                        msr++;
                        DataModel_tbl[i].addRow(rowData);
            
                }
                r.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
