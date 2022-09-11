/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */

package EITLERP.Production.FeltMachineSurvey;
/**
 *
 * @author 
 */
/*<APPLET CODE=frmmachinesurvey.class HEIGHT=574 WIDTH=758></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.file_management.clsDocManagement;
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
import javax.swing.filechooser.FileNameExtensionFilter;
//import EITLERP.FeltSales;



public class frmmachinesurvey extends javax.swing.JApplet {
    
    private int EditMode=0;
    
    private clsmachinesurvey ObjSalesParty;
    
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
    //private EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
    //private EITLTableCellRenderer Paint=new EITLTableCellRenderer();
    
    private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelMainCode;
    private EITLTableModel DataModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private HashMap colVariables=new HashMap();
    private HashMap colVariables_H=new HashMap();
    
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
    int Approved_By = 0;
    /** Creates new form frmSalesParty */
    public frmmachinesurvey() {
        System.gc();
        setSize(975,670);
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
        cmdSave.setIcon(EITLERPGLOBAL.getImage("SAVE"));
        cmdCancel.setIcon(EITLERPGLOBAL.getImage("UNDO"));
        cmdFilter.setIcon(EITLERPGLOBAL.getImage("FIND"));
        cmdPreview.setIcon(EITLERPGLOBAL.getImage("PREVIEW"));
        cmdPrint.setIcon(EITLERPGLOBAL.getImage("PRINT"));
        cmdExit.setIcon(EITLERPGLOBAL.getImage("EXIT"));
        
        
        
        GenerateCombos();
        
        ObjSalesParty = new clsmachinesurvey();
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
        txtmachinetype.setEditable(false);
        txtpapergrade.setEditable(false);
        txtpresstype.setEditable(false);
        txtfurnish.setEditable(false);
        txttypeoffiller.setEditable(false);
        txtzone.setEditable(false);
        txtMachineStatus.setEditable(false);
        txtDrivetype.setEditable(false);
        txtmachinemake.setEditable(false);
        txtDryer.setEditable(false);
        txtPartyname.setEditable(false);
        txtSizePress.setEditable(false);
      
    }
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel=new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsmachinesurvey.ModuleID+"");
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsmachinesurvey.ModuleID+"");
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        btnSendFAmail = new javax.swing.JButton();
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
        jPanel1 = new javax.swing.JPanel();
        lblAvailable3 = new javax.swing.JLabel();
        lblAvailable1 = new javax.swing.JLabel();
        lblAvailable2 = new javax.swing.JLabel();
        btnShow3 = new javax.swing.JButton();
        btnShow1 = new javax.swing.JButton();
        btnShow2 = new javax.swing.JButton();
        lblAvailable4 = new javax.swing.JLabel();
        btnShow4 = new javax.swing.JButton();
        lblAvailable5 = new javax.swing.JLabel();
        btnShow5 = new javax.swing.JButton();
        btnRemove1 = new javax.swing.JButton();
        btnRemove4 = new javax.swing.JButton();
        btnRemove3 = new javax.swing.JButton();
        btnRemove5 = new javax.swing.JButton();
        btnRemove2 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        btnUpload = new javax.swing.JButton();
        doc_name5 = new javax.swing.JTextField();
        doc_name1 = new javax.swing.JTextField();
        doc_name2 = new javax.swing.JTextField();
        doc_name3 = new javax.swing.JTextField();
        doc_name4 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        UID_2 = new javax.swing.JLabel();
        UID_3 = new javax.swing.JLabel();
        UID_4 = new javax.swing.JLabel();
        UID_5 = new javax.swing.JLabel();
        UID_1 = new javax.swing.JLabel();
        txtPartycode = new javax.swing.JTextField();
        txtPartyname = new javax.swing.JTextField();
        txtPartystation = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        lbl_press_type = new javax.swing.JLabel();
        txtpresstype = new javax.swing.JTextField();
        lbl_furnish = new javax.swing.JLabel();
        txtfurnish = new javax.swing.JTextField();
        lbl_type_of_filler = new javax.swing.JLabel();
        txttypeoffiller = new javax.swing.JTextField();
        lbl_machine_spped_range = new javax.swing.JLabel();
        txtspeedrange = new javax.swing.JTextField();
        lbl_paper_gsm_range = new javax.swing.JLabel();
        txtpapergsmrange = new javax.swing.JTextField();
        txtmachineno = new javax.swing.JTextField();
        lbl_machine_type = new javax.swing.JLabel();
        txtmachinetype = new javax.swing.JTextField();
        lbl_paper_grade = new javax.swing.JLabel();
        txtpapergrade = new javax.swing.JTextField();
        lbl_machine_no = new javax.swing.JLabel();
        lbl_survey_date = new javax.swing.JLabel();
        txtsurveydate = new javax.swing.JTextField();
        lbl_tech_representative = new javax.swing.JLabel();
        txtzone = new javax.swing.JTextField();
        lbl_incharge_name = new javax.swing.JLabel();
        txtinchargename = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtmachinedocno = new javax.swing.JTextField();
        txtWireNo4L = new javax.swing.JTextField();
        txtWireNo1L = new javax.swing.JTextField();
        txtWireNo2L = new javax.swing.JTextField();
        txtWireNo3L = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbl_tech_representative1 = new javax.swing.JLabel();
        txtMachineStatus = new javax.swing.JTextField();
        lbl_tech_representative2 = new javax.swing.JLabel();
        txtDryer = new javax.swing.JTextField();
        lbl_tech_representative3 = new javax.swing.JLabel();
        txtPeparDeckleWIre = new javax.swing.JTextField();
        lbl_tech_representative4 = new javax.swing.JLabel();
        txtPaperDecklePress = new javax.swing.JTextField();
        lbl_tech_representative5 = new javax.swing.JLabel();
        txtDecklePopeReel = new javax.swing.JTextField();
        lbl_tech_representative6 = new javax.swing.JLabel();
        txtTotalDryGrp = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtUnirunGrp = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtConventionalGroups = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtHoodType = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtSizePress = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtSizePressPosition = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtSheetDrynessSize = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        lbl_tech_representative7 = new javax.swing.JLabel();
        txtCapacity = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDrivetype = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtmachinemake = new javax.swing.JTextField();

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
        ToolBar.setBounds(0, 0, 970, 30);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("FELT MACHINE MASTER");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 30, 970, 20);

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
        TableDesc.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                TableDescMouseWheelMoved(evt);
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
        jScrollPane1.setBounds(0, 0, 950, 240);

        jButton1.setText("Next >>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(840, 280, 90, 25);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(lblStatus);
        lblStatus.setBounds(0, 280, 830, 20);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        Tab1.add(cmdAdd);
        cmdAdd.setBounds(770, 250, 70, 20);

        cmdItemdelete.setText("Remove");
        cmdItemdelete.setEnabled(false);
        cmdItemdelete.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdItemdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdItemdeleteActionPerformed(evt);
            }
        });
        Tab1.add(cmdItemdelete);
        cmdItemdelete.setBounds(850, 250, 80, 20);

        jTabbedPane1.addTab("MACHINE MASTER DETAIL", Tab1);

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
        jLabel36.setBounds(10, 100, 100, 16);

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
        jPanel6.setBounds(110, 100, 182, 100);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        ApprovalPanel.add(jLabel33);
        jLabel33.setBounds(0, 200, 100, 16);

        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        ApprovalPanel.add(cmbSendTo);
        cmbSendTo.setBounds(110, 200, 270, 28);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        ApprovalPanel.add(jLabel34);
        jLabel34.setBounds(10, 240, 100, 16);

        txtToRemarks.setEnabled(false);
        ApprovalPanel.add(txtToRemarks);
        txtToRemarks.setBounds(110, 230, 516, 22);

        cmdNext3.setText("<<Previous");
        cmdNext3.setMargin(new java.awt.Insets(2, 7, 2, 7));
        cmdNext3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(cmdNext3);
        cmdNext3.setBounds(720, 220, 99, 30);

        jButton3.setText("Next >>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        ApprovalPanel.add(jButton3);
        jButton3.setBounds(820, 220, 100, 30);

        btnSendFAmail.setText("Send final approved mail");
        btnSendFAmail.setEnabled(false);
        btnSendFAmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFAmailActionPerformed(evt);
            }
        });
        ApprovalPanel.add(btnSendFAmail);
        btnSendFAmail.setBounds(620, 10, 200, 28);

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
        jScrollPane2.setBounds(10, 30, 694, 120);

        jLabel19.setText("Document Update History");
        StatusPanel.add(jLabel19);
        jLabel19.setBounds(10, 160, 182, 16);

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
        jScrollPane6.setBounds(10, 180, 540, 130);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        StatusPanel.add(cmdViewHistory);
        cmdViewHistory.setBounds(570, 160, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        StatusPanel.add(cmdNormalView);
        cmdNormalView.setBounds(570, 190, 132, 24);

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
        txtAuditRemarks.setBounds(570, 250, 129, 28);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(570, 280, 90, 32);

        jTabbedPane1.addTab("Status", StatusPanel);

        jPanel1.setLayout(null);

        lblAvailable3.setText("Available");
        jPanel1.add(lblAvailable3);
        lblAvailable3.setBounds(180, 100, 150, 16);

        lblAvailable1.setText("Available");
        jPanel1.add(lblAvailable1);
        lblAvailable1.setBounds(180, 40, 150, 16);

        lblAvailable2.setText("Available");
        jPanel1.add(lblAvailable2);
        lblAvailable2.setBounds(180, 70, 150, 16);

        btnShow3.setText("Show");
        btnShow3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShow3ActionPerformed(evt);
            }
        });
        jPanel1.add(btnShow3);
        btnShow3.setBounds(340, 90, 80, 28);

        btnShow1.setText("Show");
        btnShow1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShow1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnShow1);
        btnShow1.setBounds(340, 30, 80, 28);

        btnShow2.setText("Show");
        btnShow2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShow2ActionPerformed(evt);
            }
        });
        jPanel1.add(btnShow2);
        btnShow2.setBounds(340, 60, 80, 28);

        lblAvailable4.setText("Available");
        jPanel1.add(lblAvailable4);
        lblAvailable4.setBounds(180, 130, 150, 16);

        btnShow4.setText("Show");
        btnShow4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShow4ActionPerformed(evt);
            }
        });
        jPanel1.add(btnShow4);
        btnShow4.setBounds(340, 120, 80, 28);

        lblAvailable5.setText("Available");
        jPanel1.add(lblAvailable5);
        lblAvailable5.setBounds(180, 160, 150, 16);

        btnShow5.setText("Show");
        btnShow5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShow5ActionPerformed(evt);
            }
        });
        jPanel1.add(btnShow5);
        btnShow5.setBounds(340, 150, 80, 28);

        btnRemove1.setText("Remove");
        btnRemove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnRemove1);
        btnRemove1.setBounds(420, 30, 90, 28);

        btnRemove4.setText("Remove");
        btnRemove4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove4ActionPerformed(evt);
            }
        });
        jPanel1.add(btnRemove4);
        btnRemove4.setBounds(420, 120, 90, 28);

        btnRemove3.setText("Remove");
        btnRemove3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove3ActionPerformed(evt);
            }
        });
        jPanel1.add(btnRemove3);
        btnRemove3.setBounds(420, 90, 90, 28);

        btnRemove5.setText("Remove");
        btnRemove5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove5ActionPerformed(evt);
            }
        });
        jPanel1.add(btnRemove5);
        btnRemove5.setBounds(420, 150, 90, 28);

        btnRemove2.setText("Remove");
        btnRemove2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove2ActionPerformed(evt);
            }
        });
        jPanel1.add(btnRemove2);
        btnRemove2.setBounds(420, 60, 90, 28);

        jLabel20.setText("Upload New Document");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(90, 210, 200, 16);

        btnUpload.setText("SELECT FILE");
        btnUpload.setEnabled(false);
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });
        jPanel1.add(btnUpload);
        btnUpload.setBounds(250, 200, 150, 28);

        doc_name5.setEnabled(false);
        jPanel1.add(doc_name5);
        doc_name5.setBounds(40, 150, 130, 28);

        doc_name1.setEnabled(false);
        jPanel1.add(doc_name1);
        doc_name1.setBounds(40, 30, 130, 28);

        doc_name2.setEnabled(false);
        jPanel1.add(doc_name2);
        doc_name2.setBounds(40, 60, 130, 28);

        doc_name3.setEnabled(false);
        jPanel1.add(doc_name3);
        doc_name3.setBounds(40, 90, 130, 28);

        doc_name4.setEnabled(false);
        jPanel1.add(doc_name4);
        doc_name4.setBounds(40, 120, 130, 28);

        jLabel21.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel21.setText("Document Name");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(40, 10, 130, 17);

        jLabel22.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel22.setText("File Name");
        jPanel1.add(jLabel22);
        jLabel22.setBounds(220, 10, 110, 17);

        jLabel16.setText("DOC Unique ID");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(520, 10, 130, 16);
        jPanel1.add(UID_2);
        UID_2.setBounds(520, 60, 110, 20);
        jPanel1.add(UID_3);
        UID_3.setBounds(520, 90, 110, 20);
        jPanel1.add(UID_4);
        UID_4.setBounds(520, 120, 110, 20);
        jPanel1.add(UID_5);
        UID_5.setBounds(520, 150, 110, 20);
        jPanel1.add(UID_1);
        UID_1.setBounds(520, 37, 110, 20);

        jTabbedPane1.addTab("Documents", jPanel1);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 300, 970, 360);

        txtPartycode.setToolTipText("Press F1 key for search Party Code");
        txtPartycode.setDisabledTextColor(new java.awt.Color(102, 102, 255));
        txtPartycode = new JTextFieldHint(new JTextField(),"Search by F1");
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
        txtPartycode.setBounds(110, 60, 90, 20);

        txtPartyname.setEditable(false);
        txtPartyname.setBackground(new java.awt.Color(204, 204, 204));
        txtPartyname.setDisabledTextColor(new java.awt.Color(51, 51, 255));
        txtPartyname.setEnabled(false);
        txtPartyname = new JTextFieldHint(new JTextField(),"Party Name");
        getContentPane().add(txtPartyname);
        txtPartyname.setBounds(210, 60, 270, 20);

        txtPartystation.setEditable(false);
        txtPartystation.setBackground(new java.awt.Color(204, 204, 204));
        txtPartystation.setDisabledTextColor(new java.awt.Color(102, 102, 255));
        getContentPane().add(txtPartystation);
        txtPartystation.setBounds(480, 60, 110, 20);

        jLabel8.setText("Party Code ");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(10, 60, 100, 16);

        lbl_press_type.setText("Machine Type (Pressing)");
        getContentPane().add(lbl_press_type);
        lbl_press_type.setBounds(600, 90, 180, 20);

        txtpresstype.setEnabled(false);
        txtpresstype = new JTextFieldHint(new JTextField(),"Search by F1");
        txtpresstype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpresstypeActionPerformed(evt);
            }
        });
        txtpresstype.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpresstypeKeyPressed(evt);
            }
        });
        getContentPane().add(txtpresstype);
        txtpresstype.setBounds(800, 90, 160, 20);

        lbl_furnish.setText("Furnish");
        getContentPane().add(lbl_furnish);
        lbl_furnish.setBounds(10, 170, 60, 20);

        txtfurnish.setEnabled(false);
        txtfurnish = new JTextFieldHint(new JTextField(),"Search by F1");
        txtfurnish.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtfurnishKeyPressed(evt);
            }
        });
        getContentPane().add(txtfurnish);
        txtfurnish.setBounds(100, 170, 110, 20);

        lbl_type_of_filler.setText("Type of Filler");
        getContentPane().add(lbl_type_of_filler);
        lbl_type_of_filler.setBounds(220, 170, 110, 20);

        txttypeoffiller = new JTextFieldHint(new JTextField(),"Search by F1");
        txttypeoffiller.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttypeoffillerKeyPressed(evt);
            }
        });
        getContentPane().add(txttypeoffiller);
        txttypeoffiller.setBounds(320, 170, 100, 20);

        lbl_machine_spped_range.setText("Machine Speed Range (MPM)");
        getContentPane().add(lbl_machine_spped_range);
        lbl_machine_spped_range.setBounds(600, 110, 220, 20);

        txtspeedrange.setEnabled(false);
        txtspeedrange.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtspeedrangeKeyPressed(evt);
            }
        });
        getContentPane().add(txtspeedrange);
        txtspeedrange.setBounds(840, 110, 120, 20);

        lbl_paper_gsm_range.setText("Paper GSM Range");
        getContentPane().add(lbl_paper_gsm_range);
        lbl_paper_gsm_range.setBounds(360, 110, 130, 20);

        txtpapergsmrange.setEnabled(false);
        txtpapergsmrange.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpapergsmrangeKeyPressed(evt);
            }
        });
        getContentPane().add(txtpapergsmrange);
        txtpapergsmrange.setBounds(500, 110, 90, 20);

        txtmachineno.setEnabled(false);
        txtmachineno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtmachinenoFocusLost(evt);
            }
        });
        txtmachineno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtmachinenoKeyPressed(evt);
            }
        });
        getContentPane().add(txtmachineno);
        txtmachineno.setBounds(110, 90, 50, 20);

        lbl_machine_type.setText("Machine Type (Forming)");
        getContentPane().add(lbl_machine_type);
        lbl_machine_type.setBounds(210, 90, 190, 20);

        txtmachinetype.setEnabled(false);
        txtmachinetype = new JTextFieldHint(new JTextField(),"Search by F1");
        txtmachinetype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmachinetypeActionPerformed(evt);
            }
        });
        txtmachinetype.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtmachinetypeKeyPressed(evt);
            }
        });
        getContentPane().add(txtmachinetype);
        txtmachinetype.setBounds(380, 90, 210, 20);

        lbl_paper_grade.setText("Paper Grade");
        getContentPane().add(lbl_paper_grade);
        lbl_paper_grade.setBounds(10, 110, 100, 20);

        txtpapergrade = new JTextFieldHint(new JTextField(),"Search by F1");
        txtpapergrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpapergradeActionPerformed(evt);
            }
        });
        txtpapergrade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpapergradeKeyPressed(evt);
            }
        });
        getContentPane().add(txtpapergrade);
        txtpapergrade.setBounds(110, 110, 200, 20);

        lbl_machine_no.setText("Machine No.");
        getContentPane().add(lbl_machine_no);
        lbl_machine_no.setBounds(10, 90, 100, 20);

        lbl_survey_date.setText("Date Of Update");
        getContentPane().add(lbl_survey_date);
        lbl_survey_date.setBounds(710, 150, 130, 20);

        txtsurveydate.setEnabled(false);
        getContentPane().add(txtsurveydate);
        txtsurveydate.setBounds(840, 150, 120, 20);

        lbl_tech_representative.setText("Zone");
        getContentPane().add(lbl_tech_representative);
        lbl_tech_representative.setBounds(430, 170, 50, 20);

        txtzone.setEnabled(false);
        txtzone = new JTextFieldHint(new JTextField(),"Search by F1");
        txtzone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtzoneKeyPressed(evt);
            }
        });
        getContentPane().add(txtzone);
        txtzone.setBounds(470, 170, 70, 20);

        lbl_incharge_name.setText("Zone Representative");
        getContentPane().add(lbl_incharge_name);
        lbl_incharge_name.setBounds(470, 190, 170, 20);

        txtinchargename.setEnabled(false);
        getContentPane().add(txtinchargename);
        txtinchargename.setBounds(620, 190, 100, 20);

        jLabel1.setText("Machine Document No");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(640, 60, 190, 16);

        txtmachinedocno.setEditable(false);
        txtmachinedocno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtmachinedocnoMouseClicked(evt);
            }
        });
        getContentPane().add(txtmachinedocno);
        txtmachinedocno.setBounds(840, 60, 120, 20);

        txtWireNo4L.setEnabled(false);
        getContentPane().add(txtWireNo4L);
        txtWireNo4L.setBounds(620, 150, 70, 20);

        txtWireNo1L.setEnabled(false);
        txtWireNo1L.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWireNo1LActionPerformed(evt);
            }
        });
        getContentPane().add(txtWireNo1L);
        txtWireNo1L.setBounds(100, 150, 70, 20);

        txtWireNo2L.setEnabled(false);
        getContentPane().add(txtWireNo2L);
        txtWireNo2L.setBounds(260, 150, 70, 20);

        txtWireNo3L.setEnabled(false);
        getContentPane().add(txtWireNo3L);
        txtWireNo3L.setBounds(430, 150, 70, 20);

        jLabel3.setText("Wire 1 No.");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 150, 90, 20);

        jLabel4.setText("Wire 2 No.");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(180, 150, 100, 16);

        jLabel5.setText("Wire 3 No.");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(340, 150, 90, 16);

        jLabel6.setText("Wire 4 No.");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(530, 150, 90, 16);

        lbl_tech_representative1.setText("Machine Status");
        getContentPane().add(lbl_tech_representative1);
        lbl_tech_representative1.setBounds(10, 190, 110, 20);

        txtMachineStatus = new JTextFieldHint(new JTextField(),"Search by F1");
        txtMachineStatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMachineStatusKeyPressed(evt);
            }
        });
        getContentPane().add(txtMachineStatus);
        txtMachineStatus.setBounds(120, 190, 80, 20);

        lbl_tech_representative2.setText("Dryer Section");
        getContentPane().add(lbl_tech_representative2);
        lbl_tech_representative2.setBounds(550, 170, 120, 20);

        txtDryer.setEnabled(false);
        txtDryer = new JTextFieldHint(new JTextField(),"Search by F1");
        txtDryer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDryerKeyPressed(evt);
            }
        });
        getContentPane().add(txtDryer);
        txtDryer.setBounds(650, 170, 60, 20);

        lbl_tech_representative3.setText("Paper Deckle After Wire(MTR)");
        getContentPane().add(lbl_tech_representative3);
        lbl_tech_representative3.setBounds(10, 130, 230, 20);

        txtPeparDeckleWIre.setEnabled(false);
        getContentPane().add(txtPeparDeckleWIre);
        txtPeparDeckleWIre.setBounds(220, 130, 50, 20);

        lbl_tech_representative4.setText("Paper Deckle After Press(MTR)");
        getContentPane().add(lbl_tech_representative4);
        lbl_tech_representative4.setBounds(320, 130, 220, 20);

        txtPaperDecklePress.setEnabled(false);
        getContentPane().add(txtPaperDecklePress);
        txtPaperDecklePress.setBounds(540, 130, 50, 20);

        lbl_tech_representative5.setText("Paper Deckle At Pope Reel (MTR)");
        getContentPane().add(lbl_tech_representative5);
        lbl_tech_representative5.setBounds(600, 130, 250, 20);

        txtDecklePopeReel.setEnabled(false);
        getContentPane().add(txtDecklePopeReel);
        txtDecklePopeReel.setBounds(840, 130, 120, 20);

        lbl_tech_representative6.setText("Dryer Section Only");
        lbl_tech_representative6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(lbl_tech_representative6);
        lbl_tech_representative6.setBounds(410, 220, 140, 20);

        txtTotalDryGrp.setText("\n");
        txtTotalDryGrp.setEnabled(false);
        getContentPane().add(txtTotalDryGrp);
        txtTotalDryGrp.setBounds(150, 250, 60, 20);

        jLabel9.setText("Unirun Groups");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(220, 250, 120, 16);

        txtUnirunGrp.setText("\n");
        txtUnirunGrp.setEnabled(false);
        getContentPane().add(txtUnirunGrp);
        txtUnirunGrp.setBounds(330, 250, 110, 20);

        jLabel10.setText("Conventional Groups");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(450, 250, 160, 16);

        txtConventionalGroups.setText("\n");
        txtConventionalGroups.setEnabled(false);
        getContentPane().add(txtConventionalGroups);
        txtConventionalGroups.setBounds(600, 250, 60, 20);

        jLabel11.setText("Hood Type");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(670, 250, 80, 16);

        txtHoodType.setText("\n");
        txtHoodType.setEnabled(false);
        txtHoodType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHoodTypeKeyPressed(evt);
            }
        });
        getContentPane().add(txtHoodType);
        txtHoodType.setBounds(760, 250, 140, 20);

        jLabel12.setText("Size Press");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(10, 270, 100, 16);

        txtSizePress.setText("\n");
        txtSizePress = new JTextFieldHint(new JTextField(),"Search by F1");
        txtSizePress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSizePressActionPerformed(evt);
            }
        });
        txtSizePress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSizePressKeyPressed(evt);
            }
        });
        getContentPane().add(txtSizePress);
        txtSizePress.setBounds(90, 270, 110, 20);

        jLabel13.setText("Size Press Position");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(220, 270, 160, 16);

        txtSizePressPosition.setText("\n");
        txtSizePressPosition.setEnabled(false);
        txtSizePressPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSizePressPositionActionPerformed(evt);
            }
        });
        getContentPane().add(txtSizePressPosition);
        txtSizePressPosition.setBounds(360, 270, 110, 20);

        jLabel14.setText("Sheet Dryness Before Size Press (%)");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(470, 270, 280, 16);

        txtSheetDrynessSize.setText("\n");
        txtSheetDrynessSize.setEnabled(false);
        getContentPane().add(txtSheetDrynessSize);
        txtSheetDrynessSize.setBounds(760, 270, 140, 20);

        jLabel15.setText("Total Dryer Groups");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(10, 250, 170, 16);

        lbl_tech_representative7.setText("Installed Capacity (TPD)");
        getContentPane().add(lbl_tech_representative7);
        lbl_tech_representative7.setBounds(200, 190, 180, 20);

        txtCapacity.setEnabled(false);
        getContentPane().add(txtCapacity);
        txtCapacity.setBounds(370, 190, 80, 20);

        jLabel2.setText("Drive Type");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(730, 170, 100, 20);

        txtDrivetype = new JTextFieldHint(new JTextField(),"Search by F1");
        txtDrivetype.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDrivetypeKeyPressed(evt);
            }
        });
        getContentPane().add(txtDrivetype);
        txtDrivetype.setBounds(840, 170, 120, 20);

        jLabel7.setText("Machine Make");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(740, 190, 110, 20);

        txtmachinemake.setEnabled(false);
        txtmachinemake.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtmachinemakeKeyPressed(evt);
            }
        });
        getContentPane().add(txtmachinemake);
        txtmachinemake.setBounds(850, 190, 110, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void TableDescMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_TableDescMouseWheelMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_TableDescMouseWheelMoved
    
    private void txtSizePressPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSizePressPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSizePressPositionActionPerformed
    
    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        ShowMessage("Select the user to whom document to be forwarded");        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSendToFocusGained
    
    private void txtSizePressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSizePressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSizePressActionPerformed
    
    private void txtpapergradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpapergradeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpapergradeActionPerformed
    
    private void txtWireNo1LActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWireNo1LActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWireNo1LActionPerformed
    
    private void txtDryerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDryerKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_DRYER' ORDER BY PARA_CODE";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtDryer.setText("");
                    txtDryer.enable();
                    txtDryer.setEditable(true);
                }
                else{
                    txtDryer.setText(aList.ReturnVal);
                    
                    if(aList.ReturnVal.equalsIgnoreCase("yes")) {
                        txtTotalDryGrp.setEditable(true);
                        txtSizePress.setEditable(true);
                        txtUnirunGrp.setEditable(true);
                        txtSizePressPosition.setEditable(true);
                        txtConventionalGroups.setEditable(true);
                        txtHoodType.setEditable(true);
                        txtSheetDrynessSize.setEditable(true);
                        DataModelDesc.ResetReadOnly(26);
                        DataModelDesc.ResetReadOnly(27);
                        DataModelDesc.ResetReadOnly(28);
                        DataModelDesc.ResetReadOnly(29);
                        DataModelDesc.ResetReadOnly(30);
                        DataModelDesc.ResetReadOnly(31);
                        DataModelDesc.ResetReadOnly(32);
                        DataModelDesc.ResetReadOnly(33);
                        DataModelDesc.ResetReadOnly(34);
                        DataModelDesc.ResetReadOnly(35);
                        DataModelDesc.ResetReadOnly(36);
                        DataModelDesc.ResetReadOnly(37);
                        DataModelDesc.ResetReadOnly(38);
                        DataModelDesc.ResetReadOnly(39);
                        DataModelDesc.ResetReadOnly(40);
                        DataModelDesc.ResetReadOnly(41);
                        DataModelDesc.ResetReadOnly(42);
                        
                    }
                    else{
                        txtTotalDryGrp.setEditable(false);
                        txtSizePress.setEditable(false);
                        txtUnirunGrp.setEditable(false);
                        txtSizePressPosition.setEditable(false);
                        txtConventionalGroups.setEditable(false);
                        txtHoodType.setEditable(false);
                        txtSheetDrynessSize.setEditable(false);
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
                    }
                    txtDryer.setEditable(false);
                }
                
            }
        }// TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_txtDryerKeyPressed
    
    private void txtMachineStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMachineStatusKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_STATUS' ORDER BY PARA_CODE";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtMachineStatus.setText("");
                    txtMachineStatus.enable();
                    txtMachineStatus.setEditable(true);
                }
                else{
                    txtMachineStatus.setText(aList.ReturnVal);
                    txtMachineStatus.setEditable(false);
                }
                
              
            }
        }// TODO add your handling code here:        // TODO add your handling code here:          // TODO add your handling code here:
    }//GEN-LAST:event_txtMachineStatusKeyPressed
    
    private void txtzoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtzoneKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_ZONE' ORDER BY PARA_CODE";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtzone.setText("");
                    txtzone.enable();
                    txtzone.setEditable(true);
                }
                else{
                    txtzone.setText(aList.ReturnVal);
                    txtzone.setEditable(false);
                }
                
              
            }
        }// TODO add your handling code here:        // TODO add your handling code here:           // TODO add your handling code here:
    }//GEN-LAST:event_txtzoneKeyPressed
    
    private void txtmachinetypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmachinetypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmachinetypeActionPerformed
    
    private void txtmachinenoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtmachinenoFocusLost
        // TODO add your handling code here:
        try {
            //DISPLAY MACHINE DOC NUMBER :- PARTY CODE + MACHINE NO...
            String partyCode  = txtPartycode.getText().trim();
            String machineNo  = txtmachineno.getText().trim();
            String machineDocNo = partyCode + machineNo;
            if(partyCode != null && machineNo != null) {
                txtmachinedocno.setText(machineDocNo);
            }
            
            //CHECK FOR MACHINE DOC NUMBER IN FELT_MACHINE_MASTER_HEADER TABLE...IF EXISTS STOP...
            String strSQL = "SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MM_DOC_NO = "+ machineDocNo + "";
            ResultSet rsTmp = data.getResult(strSQL);
            if(rsTmp.first()) {
                cmdAdd.setVisible(false);
                cmdItemdelete.setVisible(false);
                JOptionPane.showMessageDialog(null,"Machine Document Number already exists for the Party.\nPlease enter dfferent Machine Number.");
                txtmachinedocno.setText("");
                txtmachineno.setText("");
                txtmachineno.requestFocus();
            }
            DisplayStatus();
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtmachinenoFocusLost
    
    private void txtmachinedocnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtmachinedocnoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmachinedocnoMouseClicked
    
    private void txttypeoffillerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttypeoffillerKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_TYPE_FILLER'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txttypeoffiller.setText("");
                    txttypeoffiller.enable();
                    txttypeoffiller.setEditable(true);
                }
                else{
                    txttypeoffiller.setText(aList.ReturnVal);
                    txttypeoffiller.setEditable(false);
                }
            }
        }  // TODO add your handling code here:
    }//GEN-LAST:event_txttypeoffillerKeyPressed
    
    private void txtfurnishKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtfurnishKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_FURNISH'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtfurnish.setText("");
                    txtfurnish.enable();
                    txtfurnish.setEditable(true);
                }
                else{
                    txtfurnish.setText(aList.ReturnVal);
                    txtfurnish.setEditable(false);
                }
            }
        }  // TODO add your handling code here:
    }//GEN-LAST:event_txtfurnishKeyPressed
    
    private void txtpresstypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpresstypeKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_PRESS_TYPE'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtpresstype.setText("");
                    txtpresstype.enable();
                    txtpresstype.setEditable(true);
                }
                else{
                    txtpresstype.setText(aList.ReturnVal);
                    txtpresstype.setEditable(false);
                }
            }
        }    // TODO add your handling code here:
    }//GEN-LAST:event_txtpresstypeKeyPressed
    
    private void txtpapergsmrangeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpapergsmrangeKeyPressed
        
    }//GEN-LAST:event_txtpapergsmrangeKeyPressed
    
    private void txtspeedrangeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtspeedrangeKeyPressed
        
    }//GEN-LAST:event_txtspeedrangeKeyPressed
    
    private void txtpapergradeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpapergradeKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_PAPER_GRADE'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtpapergrade.setText("");
                    txtpapergrade.enable();
                    txtpapergrade.setEditable(true);
                }
                else{
                    txtpapergrade.setText(aList.ReturnVal);
                    txtpapergrade.setEditable(false);
                }
            }
        } // TODO add your handling code here:
    }//GEN-LAST:event_txtpapergradeKeyPressed
    
    private void txtmachinetypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmachinetypeKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_TYPE' ORDER BY PARA_CODE";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtmachinetype.setText("");
                    txtmachinetype.enable();
                    txtmachinetype.setEditable(true);
                }
                else{
                    txtmachinetype.setText(aList.ReturnVal);
                    txtmachinetype.setEditable(false);
                }
                
                
            }
        }// TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_txtmachinetypeKeyPressed
    
    private void txtmachinenoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmachinenoKeyPressed
        
    }//GEN-LAST:event_txtmachinenoKeyPressed
    
    private void txtpresstypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpresstypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpresstypeActionPerformed
    
    private void cmdItemdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdItemdeleteActionPerformed
        // TODO add your handling code here:
        if(TableDesc.getRowCount()>0) {
            DataModelDesc.removeRow(TableDesc.getSelectedRow());
            
        }
        
    }//GEN-LAST:event_cmdItemdeleteActionPerformed
    
    private void txtPartycodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartycodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Press F1 for Party Code List...");
    }//GEN-LAST:event_txtPartycodeFocusGained
    
    private void txtPartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartycodeFocusLost
        try{
            if(!txtPartycode.getText().equals("")){
                String strSQL="";
                ResultSet rsTmp;
                strSQL= "";
                strSQL+="SELECT PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+txtPartycode.getText().trim()+" AND APPROVED=1 AND CANCELLED=0";
                rsTmp=data.getResult(strSQL);
                if(rsTmp.first()){
                    rsTmp.first();
                    txtPartyname.setText(rsTmp.getString("PARTY_NAME"));
                    txtPartystation.setText(rsTmp.getString("DISPATCH_STATION"));
                    txtmachineno.requestFocus();
                }
                else{
                    Cancel();
                    cmdAdd.setVisible(true);
                    cmdItemdelete.setVisible(true);
                    JOptionPane.showMessageDialog(null,"No Such Party exist in Party Master...");
                    return;
                }
          
                strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CODE="+txtPartycode.getText().trim()+" AND APPROVED=1 AND CANCELLED=0 ";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                if(rsTmp.getInt("COUNT")==0) {
                    Cancel();
                    cmdAdd.setVisible(false);
                    cmdItemdelete.setVisible(false);
                    JOptionPane.showMessageDialog(null,"No Such Party exist in Party Master...");
                    return;
                    //  }
                }
            }
        }
        catch(Exception e){
            
        }
    }//GEN-LAST:event_txtPartycodeFocusLost
    
    private void txtPartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartycodeKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 ORDER BY PARTY_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                txtPartycode.setText(aList.ReturnVal);
                txtPartyname.setText(clsFeltparty.getParyName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                txtPartystation.setText(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtPartycodeKeyPressed
    
    private void TableDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyPressed
        if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT){
            if(evt.getKeyCode()==112) //F1 Key pressed
            {
                if(TableDesc.getSelectedColumn()==10) {
                    LOV aList=new LOV();
                    String strSQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_PRESS_TYPE_D'";
                    aList.SQL=strSQL;
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    aList.DefaultSearchOn=1;
                    
                    
                    if(aList.ShowLOV()) {
                        if(TableDesc.getCellEditor()!=null) {
                            TableDesc.getCellEditor().stopCellEditing();
                        }
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),10);
                    }
                }
                 if(TableDesc.getSelectedColumn()==8) {
//                    LOV aList=new LOV();
//                    
//                    //String strSQL = "SELECT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO='0000-00-00'";
//                    String strSQL = "SELECT distinct A.PRODUCT_CODE,B.GROUP_NAME FROM PRODUCTION.FELT_SALES_CATEGORY_WISE_POSITION A,PRODUCTION.FELT_QLT_RATE_MASTER B where POSITION_CATEGORY='"+Category+"' AND A.PRODUCT_CODE=B.PRODUCT_CODE";
//                    aList.SQL=strSQL;
//                    aList.ReturnCol=1;
//                    aList.SecondCol=2;
//                    aList.ShowReturnCol=true;
//                    aList.DefaultSearchOn=1;
//                    
//                    
//                    if(aList.ShowLOV()) {
//                        if(TableDesc.getCellEditor()!=null) {
//                            TableDesc.getCellEditor().stopCellEditing();
//                        }
//                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),4);
//                        TableDesc.setValueAt(aList.SecondVal, TableDesc.getSelectedRow(),5);
//                    }
                     
                    String Category = TableDesc.getValueAt(TableDesc.getSelectedRow(), 4).toString();
                    
                    if(!Category.equals(""))
                    {
                        LOV aList=new LOV();

                        //String strSQL = "SELECT PRODUCT_CODE,PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO='0000-00-00'";
                        String strSQL = "SELECT distinct A.PRODUCT_CODE,B.GROUP_NAME,B.PRODUCT_DESC FROM PRODUCTION.FELT_SALES_CATEGORY_WISE_POSITION A,PRODUCTION.FELT_QLT_RATE_MASTER B where POSITION_CATEGORY='"+Category+"' AND A.PRODUCT_CODE=B.PRODUCT_CODE";
                        aList.SQL=strSQL;
                        aList.ReturnCol=1;
                        aList.SecondCol=2;
                        aList.ShowReturnCol=true;
                        //aList.DefaultSearchOn=2;
                        aList.DefaultSearchOn=1;

                        if(aList.ShowLOV()) {
                            if(TableDesc.getCellEditor()!=null) {
                                TableDesc.getCellEditor().stopCellEditing();
                            }
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),8);
                            TableDesc.setValueAt(aList.SecondVal, TableDesc.getSelectedRow(),9);
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "Please select Position Category first!");
                    }    
                }
                
                if(TableDesc.getSelectedColumn()==4) {
                
                    EITLERP.FeltSales.common.LOV aList = new EITLERP.FeltSales.common.LOV();

                    aList.SQL = "SELECT distinct POSITION_CATEGORY FROM PRODUCTION.FELT_SALES_CATEGORY_WISE_POSITION";
                    aList.ReturnCol = 1;
                    aList.ShowReturnCol = true;
                    aList.DefaultSearchOn = 1;
                    aList.UseSpecifiedConn = true;
                    aList.dbURL = EITLERPGLOBAL.DatabaseURL;
                    if (aList.ShowLOV()) {
                        //PARTY_CODE.setText(aList.ReturnVal);
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(), 4);
                    }
                }
                 
                if(TableDesc.getSelectedColumn()==19) {
                    LOV aList=new LOV();
                    String strSQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_LP_SHOWER'";
                    aList.SQL=strSQL;
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    aList.DefaultSearchOn=1;
                    
                    
                    if(aList.ShowLOV()) {
                        if(TableDesc.getCellEditor()!=null) {
                            TableDesc.getCellEditor().stopCellEditing();
                        }
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),19);
                }
                }
                
                
                if(TableDesc.getSelectedColumn()==25) {

                    String Product_Code = TableDesc.getValueAt(TableDesc.getSelectedRow(), 8).toString();
                    if("".equals(Product_Code))
                    {
                        JOptionPane.showMessageDialog(this, "Please select PRODUCT CODE");
                        return;
                    }
                    else
                    {
                        LOV aList=new LOV();
                        String strSQL = "SELECT STYLE_CODE FROM PRODUCTION.FELT_SALES_STYLE_MASTER where PRODUCT_CODE = '"+Product_Code+"' AND STATUS='ACTIVE'";
                        aList.SQL=strSQL;
                        aList.ReturnCol=1;
                        aList.ShowReturnCol=true;
                        aList.DefaultSearchOn=1;

                        if(aList.ShowLOV()) {
                            if(TableDesc.getCellEditor()!=null) {
                                TableDesc.getCellEditor().stopCellEditing();
                            }
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),25);
                        }
                    }
                }
                
                if(TableDesc.getSelectedColumn()==47) {

                    String Product_Code = TableDesc.getValueAt(TableDesc.getSelectedRow(), 8).toString();
                    if("".equals(Product_Code))
                    {
                        JOptionPane.showMessageDialog(this, "Please select PRODUCT CODE");
                        return;
                    }
                    else
                    {
                        LOV aList=new LOV();
                        String strSQL = "SELECT STYLE_CODE FROM PRODUCTION.FELT_SALES_STYLE_MASTER where PRODUCT_CODE = '"+Product_Code+"'  AND STATUS='ACTIVE'";
                        aList.SQL=strSQL;
                        aList.ReturnCol=1;
                        aList.ShowReturnCol=true;
                        //aList.DefaultSearchOn=2;
                        aList.DefaultSearchOn=1;

                        if(aList.ShowLOV()) {
                            if(TableDesc.getCellEditor()!=null) {
                                TableDesc.getCellEditor().stopCellEditing();
                            }
                            TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),47);
                        }
                    }
                }
                
                if(TableDesc.getSelectedColumn()==40) {
                    LOV aList=new LOV();
                    String strSQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='GUIDE_TYPE'";
                    aList.SQL=strSQL;
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    aList.DefaultSearchOn=1;
                    
                    if(aList.ShowLOV()) {
                        if(TableDesc.getCellEditor()!=null) {
                            TableDesc.getCellEditor().stopCellEditing();
                        }
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),40);
                    }
                }
                
                if(TableDesc.getSelectedColumn()==56) {
                    LOV aList=new LOV();
                    String strSQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='POSITION_WISE'";
                    aList.SQL=strSQL;
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    aList.DefaultSearchOn=1;
                    
                    if(aList.ShowLOV()) {
                        if(TableDesc.getCellEditor()!=null) {
                            TableDesc.getCellEditor().stopCellEditing();
                        }
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),56);
                    }
                }
                
                
                
                
                
                if(TableDesc.getSelectedColumn()==24) {
                    LOV aList=new LOV();
                    String strSQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MACHINE_FELT_TYPE'";
                    aList.SQL=strSQL;
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    aList.DefaultSearchOn=1;
                    
                    if(aList.ShowLOV()) {
                        if(TableDesc.getCellEditor()!=null) {
                            TableDesc.getCellEditor().stopCellEditing();
                        }
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),24);
                    }
                }
                if(TableDesc.getSelectedColumn()==40) {
                    LOV aList=new LOV();
                    String strSQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='GUIDE_TYPE'";
                    aList.SQL=strSQL;
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    aList.DefaultSearchOn=1;
                    
                    if(aList.ShowLOV()) {
                        if(TableDesc.getCellEditor()!=null) {
                            TableDesc.getCellEditor().stopCellEditing();
                        }
                        TableDesc.setValueAt(aList.ReturnVal, TableDesc.getSelectedRow(),40);
                    }
                }
                
            }
            
        }
    }//GEN-LAST:event_TableDescKeyPressed
    
    private void TableDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableDescKeyReleased
        
        String Position=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 1);
        String machine=txtmachineno.getText().trim();
        String mpositiondesc=clsmachinesurvey.getpositiondesc(Position);
        TableDesc.setValueAt(mpositiondesc, TableDesc.getSelectedRow(), 2);
        String mcombicd=clsmachinesurvey.getcombinationcode(Position,machine);
        TableDesc.setValueAt(mcombicd, TableDesc.getSelectedRow(), 3);
        String wlength=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 43);
        String wwidth=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 44);
        String mlength=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 20);
        String mwidth=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 21);
        String mgsm=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(), 22);
        
        String mconsumption=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),26);
        String dconsumption=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),45);
        String mpresstype=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),10);
        String mfelttype=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),24);
        
        String mfeltlife=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),35);
        String mtpd=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),36);
        String totalprodction=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),37);
     
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
        
        String mshrdinesh=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),28);
        if(mshrdinesh.equals("")){
            mshrdin=0;
        }else{
            mshrdin=Double.parseDouble(mshrdinesh);
        }
        
        double dcon=0.00;
        double dconsum=0.00;
        double dshrdin=0.00;
        int dtcon=0;
        if(dconsumption.equals("")){
            dcon=1;
        }else {
            try{
                
                dtcon=Integer.parseInt(mconsumption.substring(dconsumption.compareToIgnoreCase(".")));
                
                dcon=Double.parseDouble(dconsumption);
                
                if(dtcon>0){
                    JOptionPane.showMessageDialog(null,"Life of Felt is in Days & Days is always in number only...");
                }
                
            }catch(Exception e) {
                
            }
            dcon=Double.parseDouble(dconsumption);
            dconsum=365/dcon;
            dconsum=Math.ceil(dconsum);
        }
        
        String dshrdinesh=(String)TableDesc.getValueAt(TableDesc.getSelectedRow(),46);
        if(dshrdinesh.equals("")){
            dshrdin=0;
        }else{
            dshrdin=Double.parseDouble(dshrdinesh);
        }
        
        
        String msize="";
        if(!mlength.equals("") && !mwidth.equals("")) {
            msize=String.valueOf((Double.parseDouble(mlength)*Double.parseDouble(mwidth)*Double.parseDouble(mgsm))/1000);
        }
        String wsize="";
        if(!wlength.equals("") && !wwidth.equals("")) {
            wsize=String.valueOf(Double.parseDouble(wlength)*Double.parseDouble(wwidth));
        }
        
        String mtotalpro="";
        if(!mfeltlife.equals("") && !mtpd.equals("")) {
            mtotalpro=String.valueOf((Double.parseDouble(mfeltlife)*Double.parseDouble(mtpd)));
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
        TableDesc.setValueAt(msize, TableDesc.getSelectedRow(), 23);
        TableDesc.setValueAt(wsize, TableDesc.getSelectedRow(), 45);
        TableDesc.setValueAt(String.valueOf(mconsum) , TableDesc.getSelectedRow(), 28);
        TableDesc.setValueAt(String.valueOf(dconsum) , TableDesc.getSelectedRow(), 50);
        TableDesc.setValueAt(mtotalpro, TableDesc.getSelectedRow(), 37);
        
        String mpaperfelt="";
        if(!mtotalpro.equals("") && !msize.equals("")) {
            mpaperfelt=String.valueOf((Double.parseDouble(mtotalpro)/Double.parseDouble(msize)));
        }
        
        
        
        TableDesc.setValueAt(mpaperfelt, TableDesc.getSelectedRow(), 38);
        
    }//GEN-LAST:event_TableDescKeyReleased
    
    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        
        if(txtPartycode.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Enter Party Code Detail First.");
            //return;
        }
        else if(txtmachineno.getText().length()==1)
        {
            JOptionPane.showMessageDialog(null,"Machine Number should be two charactor.");
            //return;
        }
        else {
            
            String POSITION_CATEGORY="",PRODUCT_CODE="",GROUP_NAME="",POSITION_NO="",POSITION_DESC="",UC_CODE="";
            
            EITLERP.FeltSales.common.LOV aList = new EITLERP.FeltSales.common.LOV();

            aList.SQL = "SELECT distinct POSITION_CATEGORY FROM PRODUCTION.FELT_SALES_CATEGORY_WISE_POSITION";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;
            aList.UseSpecifiedConn = true;
            aList.dbURL = EITLERPGLOBAL.DatabaseURL;
            if (aList.ShowLOV()) {
                //PARTY_CODE.setText(aList.ReturnVal);
                POSITION_CATEGORY = aList.ReturnVal;
            }
            
            aList = new EITLERP.FeltSales.common.LOV();
            
            if(!POSITION_CATEGORY.equals(""))
            {
                aList.SQL = "SELECT distinct A.PRODUCT_CODE,B.GROUP_NAME FROM PRODUCTION.FELT_SALES_CATEGORY_WISE_POSITION A,PRODUCTION.FELT_QLT_RATE_MASTER B where POSITION_CATEGORY='"+POSITION_CATEGORY+"' AND A.PRODUCT_CODE=B.PRODUCT_CODE";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;
                if (aList.ShowLOV()) {
                    //PARTY_CODE.setText(aList.ReturnVal);
                    PRODUCT_CODE = aList.ReturnVal;
                    GROUP_NAME = data.getStringValueFromDB("SELECT GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+PRODUCT_CODE+"'");
                }
            }
            
            aList = new EITLERP.FeltSales.common.LOV();
            
            if(!PRODUCT_CODE.equals(""))
            {
                //aList.SQL = "SELECT distinct POSITION_NO,POSITION_DESC FROM PRODUCTION.FELT_SALES_CATEGORY_WISE_POSITION where PRODUCT_CODE='"+PRODUCT_CODE+"'";
                //
                aList.SQL = "SELECT distinct A.POSITION_NO,B.POSITION_DESC FROM PRODUCTION.FELT_SALES_CATEGORY_WISE_POSITION A,PRODUCTION.FELT_MACHINE_POSITION_MST B WHERE A.POSITION_NO=B.POSITION_NO AND PRODUCT_CODE='"+PRODUCT_CODE+"'";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;
                if (aList.ShowLOV()) {
                    //PARTY_CODE.setText(aList.ReturnVal);
                    POSITION_NO = aList.ReturnVal;
                    POSITION_DESC = data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+POSITION_NO); 
                    UC_CODE = data.getStringValueFromDB("SELECT UC_CODE FROM PRODUCTION.FELT_SALES_CATEGORY_WISE_POSITION where POSITION_NO='"+POSITION_NO+"' AND PRODUCT_CODE='"+PRODUCT_CODE+"'"); 
                }
            }
            
            if(!POSITION_CATEGORY.equals("") || !PRODUCT_CODE.equals("") || !POSITION_NO.equals(""))
            {
                String Length= JOptionPane.showInputDialog("Please value for Length ");

                String Width= JOptionPane.showInputDialog("Please value for Width");

                String GSM= JOptionPane.showInputDialog("Please value for GSM");

                String Length1="",Length2="";
                String Width1="",Width2="";
                
                if(POSITION_CATEGORY.equals("PRESS"))
                {
                    Length1 = Length;
                    Width1 = Width;
                }
                else if(POSITION_CATEGORY.equals("DRYER"))
                {
                    Length2 = Length;
                    Width2 = Width;
                }
                
                    Updating=true;
                    Object[] rowData=new Object[89];
                    rowData[0]=Integer.toString(TableDesc.getRowCount()+1);
                    
                    DataModelDesc.addRow(rowData);
                    
                    DataModelDesc.setValueByVariable("MM_MACHINE_POSITION", POSITION_NO+"", TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("MM_MACHINE_POSITION_DESC", POSITION_DESC+"", TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("MM_CATEGORY", POSITION_CATEGORY, TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("UC_CODE", UC_CODE, TableDesc.getRowCount()-1);
                    String Design_no = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO='"+POSITION_NO+"'");
                    DataModelDesc.setValueByVariable("MM_POSITION_DESIGN_NO", Design_no, TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("MM_UPN_NO", txtPartycode.getText()+""+txtmachineno.getText()+""+Design_no, TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("MM_ITEM_CODE", PRODUCT_CODE, TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("MM_GRUP", GROUP_NAME, TableDesc.getRowCount()-1);
                    
                    DataModelDesc.setValueByVariable("MM_FELT_LENGTH", Length1, TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("MM_FELT_WIDTH", Width1, TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("MM_FELT_GSM", GSM, TableDesc.getRowCount()-1);
                    
                    DataModelDesc.setValueByVariable("MM_FABRIC_LENGTH", Length2, TableDesc.getRowCount()-1);
                    DataModelDesc.setValueByVariable("MM_FABRIC_WIDTH", Width2, TableDesc.getRowCount()-1);
                    TableDescKeyReleased(null);
                    
                    
//                    rowData[1]=POSITION_NO;
//                    rowData[2]=POSITION_DESC;
//                    rowData[3]="";
////                    
//                    rowData[4]=POSITION_CATEGORY;//
//                    rowData[5]=UC_CODE;//
//                    
//                    rowData[6]= Design_no;
//                    rowData[7]=txtPartycode.getText()+""+txtmachineno.getText()+""+Design_no;
//                    
//                    rowData[8]=PRODUCT_CODE;
//                    rowData[9]=GROUP_NAME;
//                    rowData[10]="";
//                    rowData[11]="";
//                    rowData[12]="";
//                    rowData[13]="";
//                    rowData[14]="";
//                    rowData[15]="";
//                    rowData[16]="";
//                    rowData[17]="";
//                    rowData[18]="";
//                    rowData[19]="";
//                    rowData[20]=Length1;
//                    rowData[21]=Width1;
//                    rowData[22]=GSM;
//                    rowData[23]="";
//                    rowData[24]="";
//                    rowData[25]="";
//                    rowData[26]="";
//                    rowData[27]="";
//                    rowData[28]="";
//                    rowData[29]="";
//                    rowData[30]="";
//                    rowData[31]="";
//                    rowData[32]="";
//                    rowData[33]="";
//                    rowData[34]="";
//                    rowData[35]="";
//                    rowData[36]="";
//                    rowData[37]="";
//                    rowData[38]="";
//                    rowData[39]="";
//                    rowData[40]="";
//                    rowData[41]="";
//                    rowData[42]="";
//                    rowData[43]=Length2;
//                    rowData[44]=Width2;
//                    rowData[45]="";
//                    rowData[42]="";
//                    rowData[43]="";
//                    rowData[44]="";
//                    rowData[45]="";
//                    rowData[46]="";
//                    rowData[47]="";
//                    rowData[48]="";
//                    rowData[49]="";
//                    rowData[50]="";
//                    rowData[51]="";
//                    rowData[52]="";
//                    rowData[53]="";

                    

                    
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
                    DataModelDesc.ResetReadOnly(25);
                    DataModelDesc.ResetReadOnly(26);
                    DataModelDesc.ResetReadOnly(27);

                    TableDesc.changeSelection(TableDesc.getRowCount()-1, 1, false,false);
                    TableDesc.requestFocus();
            }
        }
        
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
        SetupApproval();
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if(clsFeltProductionApprovalFlow.IsOnceRejectedDoc(724,txtmachinedocno.getText())) {
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
    
    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String MachineDocNo=txtmachinedocno.getText();
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed
    
    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID);
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
        MovePrevious();
    
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
        txtPartycode.enable();
        txtPartycode.requestFocus();
        cmdAdd.setVisible(true);
        cmdItemdelete.setVisible(true);
        Add();
        // GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdNewActionPerformed
    
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        txtPartyname.setEditable(false);
        txtPartystation.setEditable(false);
        cmdAdd.setVisible(true);
        cmdItemdelete.setVisible(true);
        
        Edit();
 
    }//GEN-LAST:event_cmdEditActionPerformed
    
    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
        //  GeneratePreviousDiscount();
    }//GEN-LAST:event_cmdDeleteActionPerformed
    
    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
 
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
        ObjSalesParty.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void txtHoodTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoodTypeKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='HOOD_TYPE'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtHoodType.setText("");
                    txtHoodType.enable();
                    txtHoodType.setEditable(true);
                }
                else{
                    txtHoodType.setText(aList.ReturnVal);
                    txtHoodType.setEditable(false);
                }
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoodTypeKeyPressed

    private void txtSizePressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSizePressKeyPressed
         if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='SIZE_PRESS'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtSizePress.setText("");
                    txtSizePress.enable();
                    txtSizePress.setEditable(true);
                }
                else{
                    txtSizePress.setText(aList.ReturnVal);
                    txtSizePress.setEditable(false);
                }
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtSizePressKeyPressed

    private void txtDrivetypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDrivetypeKeyPressed
    if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL= "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='DRIVE_TYPE'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            
            if(aList.ShowLOV()) {
                if(aList.ReturnVal.equalsIgnoreCase("OTHER")){
                    txtDrivetype.setText("");
                    txtDrivetype.enable();
                    txtDrivetype.setEditable(true);
                }
                else{
                    txtDrivetype.setText(aList.ReturnVal);
                    txtDrivetype.setEditable(false);
                }
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtDrivetypeKeyPressed

    private void txtmachinemakeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmachinemakeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmachinemakeKeyPressed

    private void btnShow3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShow3ActionPerformed
        // TODO add your handling code here:openFile(lblAvailable1.getText());openFile(lblAvailable1.getText());
        openFile(lblAvailable3.getText());
    }//GEN-LAST:event_btnShow3ActionPerformed

    private void btnShow1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShow1ActionPerformed
        // TODO add your handling code here:
        openFile(lblAvailable1.getText());
    }//GEN-LAST:event_btnShow1ActionPerformed

    private void btnShow2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShow2ActionPerformed
        // TODO add your handling code here:
        openFile(lblAvailable2.getText());
    }//GEN-LAST:event_btnShow2ActionPerformed

    private void btnShow4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShow4ActionPerformed
        // TODO add your handling code here:
        openFile(lblAvailable4.getText());
    }//GEN-LAST:event_btnShow4ActionPerformed

    private void btnShow5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShow5ActionPerformed
        // TODO add your handling code here:
        openFile(lblAvailable5.getText());
    }//GEN-LAST:event_btnShow5ActionPerformed

    private void btnRemove1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove1ActionPerformed
        // TODO add your handling code here:
        deleteFileFromDatabase(lblAvailable1.getText());
        DisplayStatus();
    }//GEN-LAST:event_btnRemove1ActionPerformed

    private void btnRemove4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove4ActionPerformed
        // TODO add your handling code here:
        deleteFileFromDatabase(lblAvailable4.getText());
        DisplayStatus();
    }//GEN-LAST:event_btnRemove4ActionPerformed

    private void btnRemove3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove3ActionPerformed
        // TODO add your handling code here:
        deleteFileFromDatabase(lblAvailable3.getText());
        DisplayStatus();
    }//GEN-LAST:event_btnRemove3ActionPerformed

    private void btnRemove5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove5ActionPerformed
        // TODO add your handling code here:
        deleteFileFromDatabase(lblAvailable5.getText());
        DisplayStatus();
    }//GEN-LAST:event_btnRemove5ActionPerformed

    private void btnRemove2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove2ActionPerformed
        // TODO add your handling code here:
        deleteFileFromDatabase(lblAvailable2.getText());
        DisplayStatus();
    }//GEN-LAST:event_btnRemove2ActionPerformed

    private void deleteFileFromDatabase(String file_name)
    {
        if(!file_name.equals(""))
        {
            int n=JOptionPane.showConfirmDialog(this, "Are you sure? You want to remove file "+file_name+"?","Delete Option",2);
            //int n=JOptionPane.showConfirmDialog(this, "Are you sure? You want to remove file "+file_name+"?",2);
            if(n==JOptionPane.YES_OPTION)
            {
                data.Execute("DELETE FROM DOC_MGMT.FELT_MACHINE_CIRCUIT_DIAGRAM where CD_MACHINE_DOC_NO='"+txtmachinedocno.getText()+"' AND CD_NAME='"+file_name+"' ");
                System.out.println("DELETE FROM DOC_MGMT.FELT_MACHINE_CIRCUIT_DIAGRAM where CD_MACHINE_DOC_NO='"+txtmachinedocno.getText()+"' AND CD_NAME='"+file_name+"' ");
            }
        }
    }
    
    
    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        // TODO add your handling code here:
        if(!UID_5.getText().equals("")){
            JOptionPane.showMessageDialog(this, "5 Documents only allowed");
            return;
        }
        uploadDocument();
    }//GEN-LAST:event_btnUploadActionPerformed

    private void btnSendFAmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFAmailActionPerformed
        // TODO add your handling code here:
        //System.out.println("Sel Hierarchy : "+SelHierarchyID);

        System.out.println("approved = "+ObjSalesParty.getAttribute("APPROVED").getString());

        if(ObjSalesParty.getAttribute("APPROVED").getInt() == 1)
        {
            int value = JOptionPane.showConfirmDialog(this, " Are you sure? You want to send Final Approved mail to all users? ","Confirmation Alert!",JOptionPane.YES_NO_OPTION);
            System.out.println("VALUE = "+value);
            if(value==0)
            {
                try{
                    String DOC_NO = txtmachinedocno.getText();
                    String DOC_DATE = ObjSalesParty.getAttribute("CREATED_DATE").getString();
                    String Party_Code = txtPartycode.getText();
                    int Hierarchy = ObjSalesParty.getAttribute("HIERARCHY_ID").getInt();
                    //System.out.println("ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true "+ModuleId+","+DOC_NO+","+DOC_DATE+","+Party_Code+","+EITLERPGLOBAL.gNewUserID+","+Hierarchy+","+true);
                    //System.out.println("Final Approved By : "+FinalApprovedBy);
                    
                    String responce = JavaMail.sendFinalApprovalMail(724, DOC_NO, DOC_DATE, Party_Code, Approved_By, Hierarchy, true, EITLERPGLOBAL.gNewUserID);
                    System.out.println("Send Mail Responce : "+responce);

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_btnSendFAmailActionPerformed
    /*
    private void openFile(String file_name)
    {
        try{
            ResultSet rs = null;
            rs = sdml.felt.commonUI.data.getResult("SELECT CD_CIRCUIT_DIAGRAM FROM DOC_MGMT.FELT_MACHINE_CIRCUIT_DIAGRAM where CD_NAME='"+file_name+"'");
 
            File file = new File("/root/Machine_Master_Files/"+file_name);
            File file1 = new File("D://Machine_Master_Files/"+file_name);
            try{
                    //le.mkdir();
                    File directory = new File("/root/Machine_Master_Files");

                    if(!directory.exists()){
                               directory.mkdir();
                               if(!file.exists()){
                                   file.getParentFile().mkdir();
                               }
                    }
                
                    file.createNewFile();
                    FileOutputStream output = new FileOutputStream(file);
                    System.out.println("Writing to file " + file.getAbsolutePath());
                    rs.first();
                    byte[] imagebytes = rs.getBytes("CD_CIRCUIT_DIAGRAM");
                    output.write(imagebytes);
                    output.close();
                    
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            try{
                    file1.mkdir();
                    file1.createNewFile();
                    FileOutputStream output = new FileOutputStream(file1);
                    System.out.println("Writing to file " + file1.getAbsolutePath());
                    rs.first();
                    byte[] imagebytes = rs.getBytes("CD_CIRCUIT_DIAGRAM");
                    output.write(imagebytes);
                    output.close();
            
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
            
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(null, "Desktop Not Supported");
                return;
            } else {
                Desktop desk = Desktop.getDesktop();
                if (file.exists()) {
                    desk.open(file);
                } 
                else if (file1.exists()) {
                    desk.open(file1);
                }
            }
            
         }catch(Exception e)
         {
             e.printStackTrace();
         }
    }
    */
    private void openFile(String file_name)
    {
        try{
            ResultSet rs = null;
            rs = sdml.felt.commonUI.data.getResult("SELECT CD_CIRCUIT_DIAGRAM FROM DOC_MGMT.FELT_MACHINE_CIRCUIT_DIAGRAM where CD_NAME='"+file_name+"'");
 
            //File file = new File("/root/Machine_Master_Files/"+file_name);
            //File file1 = new File("D://Machine_Master_Files/"+file_name);
           
            File file = new File(file_name);
           
            try{
                    //le.mkdir();
//                    File directory = new File("/root/Machine_Master_Files");
//
//                    if(!directory.exists()){
//                               directory.mkdir();
//                               if(!file.exists()){
//                                   file.getParentFile().mkdir();
//                               }
//                    }
                
                    //file.createNewFile();
                    FileOutputStream output = new FileOutputStream(file);
                    System.out.println("Writing to file " + file.getAbsolutePath());
                    rs.first();
                    byte[] imagebytes = rs.getBytes("CD_CIRCUIT_DIAGRAM");
                    output.write(imagebytes);
                    output.close();
                    
            }catch(Exception e)
            {
                e.printStackTrace();
            }
//            try{
//                    File folder = new File("D://Machine_Master_Files");
//                    folder.mkdir();
//                    
//                    file1.createNewFile();
//                    FileOutputStream output = new FileOutputStream(file1);
//                    System.out.println("Writing to file " + file1.getAbsolutePath());
//                    rs.first();
//                    byte[] imagebytes = rs.getBytes("CD_CIRCUIT_DIAGRAM");
//                    output.write(imagebytes);
//                    output.close();
//            
//            }catch(Exception e)
//            {
//                e.printStackTrace();
//            }
            
            
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(null, "Desktop Not Supported");
                return;
            } else {
                Desktop desk = Desktop.getDesktop();
                if (file.exists()) {
                    desk.open(file);
                } 
//                else if (file1.exists()) {
//                    desk.open(file1);
//                }
            }
            
         }catch(Exception e)
         {
             e.printStackTrace();
         }
    }
    private void DisplayStatus(){
        setInvisible();
        clsDocManagement  d = new clsDocManagement();
        ArrayList<clsDocManagement> dataList = d.getStatus(txtmachinedocno.getText());
        for(int i=0;i<dataList.size();i++)
        {
            clsDocManagement Obj = dataList.get(i);
            if(i==0)
            {
                lblAvailable1.setVisible(true);
                lblAvailable1.setText(Obj.getCD_NAME());
                lblAvailable1.setForeground(Color.GREEN);
                btnShow1.setVisible(true);
                btnRemove1.setVisible(true);
                UID_1.setText(Obj.getCD_ID()+"");
                doc_name1.setText(Obj.getCD_DOC_NAME());
            }
            if(i==1)
            {
                lblAvailable2.setVisible(true);
                lblAvailable2.setText(Obj.getCD_NAME());
                lblAvailable2.setForeground(Color.GREEN);
                btnShow2.setVisible(true);
                btnRemove2.setVisible(true);
                UID_2.setText(Obj.getCD_ID()+"");
                doc_name2.setText(Obj.getCD_DOC_NAME());
            }
            if(i==2)
            {
                lblAvailable3.setVisible(true);
                lblAvailable3.setText(Obj.getCD_NAME());
                lblAvailable3.setForeground(Color.GREEN);
                btnShow3.setVisible(true);
                btnRemove3.setVisible(true);
                UID_3.setText(Obj.getCD_ID()+"");
                doc_name3.setText(Obj.getCD_DOC_NAME());
            }
            if(i==3)
            {
                lblAvailable4.setVisible(true);
                lblAvailable4.setText(Obj.getCD_NAME());
                lblAvailable4.setForeground(Color.GREEN);
                btnShow4.setVisible(true);
                btnRemove4.setVisible(true);
                UID_4.setText(Obj.getCD_ID()+"");
                doc_name4.setText(Obj.getCD_DOC_NAME());
            }
            if(i==4)
            {
                lblAvailable5.setVisible(true);
                lblAvailable5.setText(Obj.getCD_NAME());
                lblAvailable5.setForeground(Color.GREEN);
                btnShow5.setVisible(true);
                btnRemove5.setVisible(true);
                UID_5.setText(Obj.getCD_ID()+"");
                doc_name5.setText(Obj.getCD_DOC_NAME());
            }
        } 
        if((EditMode==EITLERPGLOBAL.ADD && clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) || (EditMode==EITLERPGLOBAL.EDIT && clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)))
        {
            doc_name1.setEnabled(true);
            doc_name2.setEnabled(true);
            doc_name3.setEnabled(true);
            doc_name4.setEnabled(true);
            doc_name5.setEnabled(true);
            btnUpload.setEnabled(true);
            btnRemove1.setEnabled(true);
            btnRemove2.setEnabled(true);
            btnRemove3.setEnabled(true);
            btnRemove4.setEnabled(true);
            btnRemove5.setEnabled(true);
            
        }
        else
        {
            doc_name1.setEnabled(false);
            doc_name2.setEnabled(false);
            doc_name3.setEnabled(false);
            doc_name4.setEnabled(false);
            doc_name5.setEnabled(false);
            btnUpload.setEnabled(false);
            btnRemove1.setEnabled(false);
            btnRemove2.setEnabled(false);
            btnRemove3.setEnabled(false);
            btnRemove4.setEnabled(false);
            btnRemove5.setEnabled(false);
        }
    }
    private void setInvisible()
    {
        lblAvailable1.setText("");
        lblAvailable2.setText("");
        lblAvailable3.setText("");
        lblAvailable4.setText("");
        lblAvailable5.setText("");
        
        btnShow1.setVisible(false);
        btnShow2.setVisible(false);
        btnShow3.setVisible(false);
        btnShow4.setVisible(false);
        btnShow5.setVisible(false);
        
        btnRemove1.setVisible(false);
        btnRemove2.setVisible(false);
        btnRemove3.setVisible(false);
        btnRemove4.setVisible(false);
        btnRemove5.setVisible(false);
        
        btnRemove1.setEnabled(false);
        btnRemove2.setEnabled(false);
        btnRemove3.setEnabled(false);
        btnRemove4.setEnabled(false);
        btnRemove5.setEnabled(false);
        
        doc_name1.setText("");
        doc_name2.setText("");
        doc_name3.setText("");
        doc_name4.setText("");
        doc_name5.setText("");
        
        doc_name1.setEnabled(false);
        doc_name2.setEnabled(false);
        doc_name3.setEnabled(false);
        doc_name4.setEnabled(false);
        doc_name5.setEnabled(false);
        
        UID_1.setText("");
        UID_2.setText("");
        UID_3.setText("");
        UID_4.setText("");
        UID_5.setText("");
        
    }
    private void uploadDocument(){
        try{

            if(txtmachinedocno.getText().equals(""))
            {
                JOptionPane.showMessageDialog(this, "Please enter Party and Machine Number");
                return;
            }
            
            
            File Source_File;
            JFileChooser chooser =new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf", "jpg");
            //sfilter.
            chooser.setFileFilter(filter);
            
            int n  = chooser.showOpenDialog(this);
            
            Source_File = chooser.getSelectedFile();
            clsDocManagement d = new clsDocManagement();
            FileInputStream inputStream = new FileInputStream(Source_File);
            d.setCD_NAME(Source_File.getName());
            d.setCD_CIRCUIT_DIAGRAM(inputStream);
            d.setCD_MACHINE_DOC_NO(txtmachinedocno.getText());
            System.out.println("File Size : "+(int)Source_File.length());
            if((int)Source_File.length() < 1000000)
            {
                d.saveMachineFile((int)Source_File.length());

                System.out.println("Uploding Done...!");
            }
            else
            {
                JOptionPane.showMessageDialog(this, "File size not more than 1 MB allowed");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        DisplayStatus();
    }
    
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
    private javax.swing.JLabel UID_1;
    private javax.swing.JLabel UID_2;
    private javax.swing.JLabel UID_3;
    private javax.swing.JLabel UID_4;
    private javax.swing.JLabel UID_5;
    private javax.swing.JButton btnRemove1;
    private javax.swing.JButton btnRemove2;
    private javax.swing.JButton btnRemove3;
    private javax.swing.JButton btnRemove4;
    private javax.swing.JButton btnRemove5;
    private javax.swing.JButton btnSendFAmail;
    private javax.swing.JButton btnShow1;
    private javax.swing.JButton btnShow2;
    private javax.swing.JButton btnShow3;
    private javax.swing.JButton btnShow4;
    private javax.swing.JButton btnShow5;
    private javax.swing.JButton btnUpload;
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
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JTextField doc_name1;
    private javax.swing.JTextField doc_name2;
    private javax.swing.JTextField doc_name3;
    private javax.swing.JTextField doc_name4;
    private javax.swing.JTextField doc_name5;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAvailable1;
    private javax.swing.JLabel lblAvailable2;
    private javax.swing.JLabel lblAvailable3;
    private javax.swing.JLabel lblAvailable4;
    private javax.swing.JLabel lblAvailable5;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lbl_furnish;
    private javax.swing.JLabel lbl_incharge_name;
    private javax.swing.JLabel lbl_machine_no;
    private javax.swing.JLabel lbl_machine_spped_range;
    private javax.swing.JLabel lbl_machine_type;
    private javax.swing.JLabel lbl_paper_grade;
    private javax.swing.JLabel lbl_paper_gsm_range;
    private javax.swing.JLabel lbl_press_type;
    private javax.swing.JLabel lbl_survey_date;
    private javax.swing.JLabel lbl_tech_representative;
    private javax.swing.JLabel lbl_tech_representative1;
    private javax.swing.JLabel lbl_tech_representative2;
    private javax.swing.JLabel lbl_tech_representative3;
    private javax.swing.JLabel lbl_tech_representative4;
    private javax.swing.JLabel lbl_tech_representative5;
    private javax.swing.JLabel lbl_tech_representative6;
    private javax.swing.JLabel lbl_tech_representative7;
    private javax.swing.JLabel lbl_type_of_filler;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtCapacity;
    private javax.swing.JTextField txtConventionalGroups;
    private javax.swing.JTextField txtDecklePopeReel;
    private javax.swing.JTextField txtDrivetype;
    private javax.swing.JTextField txtDryer;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtHoodType;
    private javax.swing.JTextField txtMachineStatus;
    private javax.swing.JTextField txtPaperDecklePress;
    private javax.swing.JTextField txtPartycode;
    private javax.swing.JTextField txtPartyname;
    private javax.swing.JTextField txtPartystation;
    private javax.swing.JTextField txtPeparDeckleWIre;
    private javax.swing.JTextField txtSheetDrynessSize;
    private javax.swing.JTextField txtSizePress;
    private javax.swing.JTextField txtSizePressPosition;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtTotalDryGrp;
    private javax.swing.JTextField txtUnirunGrp;
    private javax.swing.JTextField txtWireNo1L;
    private javax.swing.JTextField txtWireNo2L;
    private javax.swing.JTextField txtWireNo3L;
    private javax.swing.JTextField txtWireNo4L;
    private javax.swing.JTextField txtfurnish;
    private javax.swing.JTextField txtinchargename;
    private javax.swing.JTextField txtmachinedocno;
    private javax.swing.JTextField txtmachinemake;
    private javax.swing.JTextField txtmachineno;
    private javax.swing.JTextField txtmachinetype;
    private javax.swing.JTextField txtpapergrade;
    private javax.swing.JTextField txtpapergsmrange;
    private javax.swing.JTextField txtpresstype;
    private javax.swing.JTextField txtspeedrange;
    private javax.swing.JTextField txtsurveydate;
    private javax.swing.JTextField txttypeoffiller;
    private javax.swing.JTextField txtzone;
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
        aList.ModuleID=724;
        
        if(aList.ShowList()) {
            EditMode=EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SelPrefix=aList.Prefix; //Selected Prefix;
            SelSuffix=aList.Suffix;
            FFNo=aList.FirstFreeNo;
            SetupApproval();
            
            txtPartycode.requestFocus();
            
            lblTitle.setText("FELT MACHINE SURVEY- "+txtmachinedocno.getText());
            lblTitle.setBackground(Color.BLUE);
        }
        else {
            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
        DisplayStatus();
    }
    
    private void SetupApproval() {
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
            
            int FromUserID=clsFeltProductionApprovalFlow.getFromID( clsmachinesurvey.ModuleID,(String)ObjSalesParty.getAttribute("MM_DOC_NO").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=clsFeltProductionApprovalFlow.getFromRemarks(clsmachinesurvey.ModuleID,FromUserID,(String)ObjSalesParty.getAttribute("MM_DOC_NO").getObj());
            
            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }
        
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if(clsHierarchy.CanSkip( (int)EITLERPGLOBAL.gCompanyID,SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
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
        
        
        //In Edit Mode Hierarchy Should be disabled
        if(EditMode==EITLERPGLOBAL.EDIT) {
        }
        
        if(EditMode==0) {
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
                
                String ProformaNo=(String)ObjSalesParty.getAttribute("MM_DOC_NO").getObj();
                
                List=clsFeltProductionApprovalFlow.getRemainingUsers(clsmachinesurvey.ModuleID,ProformaNo);
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
        txtPartycode.setEnabled(pStat);
        txtPartyname.setEnabled(pStat);
        txtPartystation.setEnabled(pStat);
        txtmachineno.setEnabled(pStat);
        txtspeedrange.setEnabled(pStat);
        txtpapergsmrange.setEnabled(pStat);
        txtsurveydate.setEnabled(pStat);
        txtinchargename.setEnabled(pStat);
        txtWireNo1L.setEnabled(pStat);
        txtWireNo2L.setEnabled(pStat);
        txtWireNo3L.setEnabled(pStat);
        txtWireNo4L.setEnabled(pStat);
        txtPeparDeckleWIre.setEnabled(pStat);
        txtPaperDecklePress.setEnabled(pStat);
        txtDecklePopeReel.setEnabled(pStat);
        txtTotalDryGrp.setEnabled(pStat);
        txtUnirunGrp.setEnabled(pStat);
        txtConventionalGroups.setEnabled(pStat);
        txtHoodType.setEnabled(pStat);
        txtSizePress.setEnabled(pStat);
        txtSheetDrynessSize.setEnabled(pStat);
        txtSizePressPosition.setEnabled(pStat);
        txtDrivetype.setEnabled(pStat);
        txtmachinemake.setEnabled(pStat);
        txtCapacity.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        cmdAdd.setEnabled(pStat);
        cmdItemdelete.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        
         if((EditMode==EITLERPGLOBAL.ADD && clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) || (EditMode==EITLERPGLOBAL.EDIT && clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)))
        {
            doc_name1.setEnabled(true);
            doc_name2.setEnabled(true);
            doc_name3.setEnabled(true);
            doc_name4.setEnabled(true);
            doc_name5.setEnabled(true);
            btnUpload.setEnabled(true);
            btnRemove1.setEnabled(true);
            btnRemove2.setEnabled(true);
            btnRemove3.setEnabled(true);
            btnRemove4.setEnabled(true);
            btnRemove5.setEnabled(true);
            
        }
        else
        {
            doc_name1.setEnabled(false);
            doc_name2.setEnabled(false);
            doc_name3.setEnabled(false);
            doc_name4.setEnabled(false);
            doc_name5.setEnabled(false);
            btnUpload.setEnabled(false);
            btnRemove1.setEnabled(false);
            btnRemove2.setEnabled(false);
            btnRemove3.setEnabled(false);
            btnRemove4.setEnabled(false);
            btnRemove5.setEnabled(false);
        }
        
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
        
        
        if(!txtPartycode.getText().trim().equals("")) {
            if(EditMode==EITLERPGLOBAL.ADD) {
            }
        }
        
        
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
        txtmachineno.setText("");
        txtmachinetype.setText("");
        txtpapergrade.setText("");
        txtspeedrange.setText("");
        txtpapergsmrange.setText("");
        txtpresstype.setText("");
        txtfurnish.setText("");
        txttypeoffiller.setText("");
        txtMachineStatus.setText("");
        txtDryer.setText("");
        txtzone.setText("");
        txtsurveydate.setText("");
        txtinchargename.setText("");
        txtmachinedocno.setText("");
        txtWireNo1L.setText("");
        txtWireNo2L.setText("");
        txtWireNo3L.setText("");
        txtWireNo4L.setText("");
        txtCapacity.setText("");
        txtPeparDeckleWIre.setText("");
        txtPaperDecklePress.setText("");
        txtDecklePopeReel.setText("");
        txtTotalDryGrp.setText("");
        txtUnirunGrp.setText("");
        txtConventionalGroups.setText("");
        txtSizePress.setText("");
        txtSheetDrynessSize.setText("");
        txtSizePressPosition.setText("");
        txtDrivetype.setText("");
        txtmachinemake.setText("");
        txtFromRemarks.setText("");
        txtToRemarks.setText("");
        txtCapacity.setText("");
        txtHoodType.setText("");
        txtToRemarks.setText("");
        FormatGrid();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
   }
    
    private void Edit() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        
        //----------------------------------//
        String docno=ObjSalesParty.getAttribute("MM_DOC_NO").getString();
        //System.out.println("New User Id = "+EITLERPGLOBAL.gNewUserID);
        if(ObjSalesParty.IsEditable(EITLERPGLOBAL.gCompanyID, docno, EITLERPGLOBAL.gNewUserID)) {
            EditMode=EITLERPGLOBAL.EDIT;
            
            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//
            
            if(clsFeltProductionApprovalFlow.IsCreator(clsmachinesurvey.ModuleID,docno)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7038,70382)) {
                SetFields(true);
            }
            else {
                EnableApproval();
            }
            
            
            
            cmdAdd.setVisible(true);
            cmdItemdelete.setVisible(true);
            
            
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
        if (txtmachinetype.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Machine Type (Forming) ", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
         if (txtpresstype.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Machine Type (Press) ", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
         
        if (txtpapergrade.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Paper Grade ", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        
        if (txtpapergsmrange.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Paper Gsm Range ", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtspeedrange.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Machine Speed Range ", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtfurnish.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Furnish ", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(txtCapacity.getText().trim().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Installed Capacity (TPD) is Compulsory", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else
        {
            try{
                int capacity = Integer.parseInt(txtCapacity.getText().trim());
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, "Integer value only allowed in Installed Capacity (TPD) ", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String presstype="",pressload="";
        int  i = 0;
        if (!OpgReject.isSelected())
        {
            for (i = 0; i <= TableDesc.getRowCount() - 1; i++) {

                presstype = ((String) TableDesc.getValueAt(i, 10)).trim();
                if (presstype.equals("")) {
                    JOptionPane.showMessageDialog(this, "Enter Press Type", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String  prod_code = ((String) TableDesc.getValueAt(i, 8)).trim();
                if(prod_code.equals(""))
                {
                    JOptionPane.showMessageDialog(this, "Product Code is compulsory.");
                    return;
                }
                
                String  prod_group = ((String) TableDesc.getValueAt(i, 9)).trim();
                if(prod_group.equals(""))
                {
                    JOptionPane.showMessageDialog(this, "Product Group is compulsory.");
                    return;
                }
                
                String  avg_life1 = ((String) TableDesc.getValueAt(i, 26)).trim();
                String  avg_life2 = ((String) TableDesc.getValueAt(i, 49)).trim();
                
                if(avg_life1.equals("") && avg_life2.equals(""))
                {
                    JOptionPane.showMessageDialog(this, "AVG LIFE is compulsory.");
                    return;
                }
                
                if(avg_life1.equals("-") && avg_life2.equals("-"))
                {
                    JOptionPane.showMessageDialog(this, "AVG LIFE is compulsory.");
                    return;
                }
                
                pressload = ((String) TableDesc.getValueAt(i, 15)).trim();
                if (pressload.equals("")) {
                    JOptionPane.showMessageDialog(this, "Enter Press Load", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                pressload = ((String) TableDesc.getValueAt(i, 15)).trim();
                if (pressload.equals("")) {
                    JOptionPane.showMessageDialog(this, "Enter Press Load", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                
//                int tpd_col = DataModelDesc.getColFromVariable("MM_TPD");
//                int TPD = 0;
//                try{
//                    TPD = Integer.parseInt(TableDesc.getValueAt(i, tpd_col).toString());
//                }catch(Exception e)
//                {
//                    TPD = 0;
//                    e.printStackTrace();
//                }
//                if(TPD == 0)
//                {
//                    JOptionPane.showMessageDialog(this, "Capacity Utilization (TPD) is compulsory and should not 0 in Detail Table", "ERROR", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
                
                //MM_MIN_CIRCUIT_LENGTH
                String MM_MIN_CIRCUIT_LENGTH = ((String) TableDesc.getValueAt(i, 91)).trim();

                String MM_MAX_CIRCUIT_LENGTH = ((String) TableDesc.getValueAt(i, 92)).trim();
                
                String Goal = ((String) TableDesc.getValueAt(i, 93)).trim();

                if(MM_MIN_CIRCUIT_LENGTH.equals(""))
                {
                    JOptionPane.showMessageDialog(this, "Enter Min Circuit Length", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(MM_MAX_CIRCUIT_LENGTH.equals(""))
                {
                    JOptionPane.showMessageDialog(this, "Enter Max Circuit Length", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(Goal.equals("") || Goal.equals("0.0"))
                {
                    JOptionPane.showMessageDialog(this, "Enter Goal", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
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
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. \nError is "+ObjSalesParty.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjSalesParty.Update()) {
                
                if(OpgFinal.isSelected())
                {
                                try{
                                    String DOC_NO = txtmachinedocno.getText();
                                    String DOC_DATE = EITLERPGLOBAL.getCurrentDate();
                                    String Party_Code = txtPartycode.getText();
                                    int Module_Id = clsmachinesurvey.ModuleID;

                                    String responce = JavaMail.sendFinalApprovalMail(Module_Id, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true, 0);
                                    System.out.println("Send Mail Responce : "+responce); 

                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                } 
                }
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
        EnableToolbar();
        SetMenuForRights();
    }
    
    private void Find() {
        Loader ObjLoader=new Loader(this,"EITLERP.Production.FeltMachineSurvey.frmmachinesurveyFind",true);
        frmmachinesurveyFind ObjReturn= (frmmachinesurveyFind) ObjLoader.getObj();
        
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
                btnSendFAmail.setEnabled(false);
                if(ObjSalesParty.getAttribute("APPROVED").getInt()==1) {
                    
                    lblTitle.setBackground(Color.BLUE);
                    lblTitle.setForeground(Color.WHITE);
                    btnSendFAmail.setEnabled(true);
                }
                
                if(ObjSalesParty.getAttribute("APPROVED").getInt()!=1) {
                    lblTitle.setBackground(Color.GRAY);
                    lblTitle.setForeground(Color.WHITE);
                }
                
                if(ObjSalesParty.getAttribute("CANCELED").getInt()==1) {
                    lblTitle.setBackground(Color.RED);
                    lblTitle.setForeground(Color.WHITE);
                }
            }
        }
        catch(Exception c) {
            
        }
        //============================================//
        
        //========= Authority Delegation Check =====================//
        if(EITLERPGLOBAL.gAuthorityUserID!=EITLERPGLOBAL.gUserID) {
            int ModuleID=clsmachinesurvey.ModuleID;
            
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
            lblTitle.setText("FELT MACHINE MASTER "+(String)ObjSalesParty.getAttribute("MM_DOC_NO").getObj());
            txtPartycode.setText((String)ObjSalesParty.getAttribute("MM_PARTY_CODE").getObj());
            txtPartyname.setText(clsmachinesurvey.getPartyName(EITLERPGLOBAL.gCompanyID,(String)ObjSalesParty.getAttribute("MM_PARTY_CODE").getObj()));
            txtPartystation.setText(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID,(String)ObjSalesParty.getAttribute("MM_PARTY_CODE").getObj()));
            txtmachinedocno.setText((String)ObjSalesParty.getAttribute("MM_DOC_NO").getObj());
            txtmachineno.setText((String)ObjSalesParty.getAttribute("MM_MACHINE_NO").getObj());
            txtmachinetype.setText((String)ObjSalesParty.getAttribute("MM_MACHINE_TYPE_FORMING").getObj());
            txtpapergrade.setText((String)ObjSalesParty.getAttribute("MM_PAPER_GRADE").getObj());
            txtspeedrange.setText((String)ObjSalesParty.getAttribute("MM_MACHINE_SPEED_RANGE").getObj());
            txtpapergsmrange.setText((String)ObjSalesParty.getAttribute("MM_PAPER_GSM_RANGE").getObj());
            txtpresstype.setText((String)ObjSalesParty.getAttribute("MM_MACHINE_TYPE_PRESSING").getObj());
            txtfurnish.setText((String)ObjSalesParty.getAttribute("MM_FURNISH").getObj());
            txttypeoffiller.setText((String)ObjSalesParty.getAttribute("MM_TYPE_OF_FILLER").getObj());
            txtPeparDeckleWIre.setText((String)ObjSalesParty.getAttribute("MM_PAPER_DECKLE_AFTER_WIRE").getObj());
            txtPaperDecklePress.setText((String)ObjSalesParty.getAttribute("MM_PAPER_DECKLE_AFTER_PRESS").getObj());
            txtDecklePopeReel.setText((String)ObjSalesParty.getAttribute("MM_PAPER_DECKLE_AT_POPE_REEL").getObj());
            txtWireNo1L.setText((String)ObjSalesParty.getAttribute("MM_WIRE_DETAIL_1").getObj());
            txtWireNo2L.setText((String)ObjSalesParty.getAttribute("MM_WIRE_DETAIL_2").getObj());
            txtWireNo3L.setText((String)ObjSalesParty.getAttribute("MM_WIRE_DETAIL_3").getObj());
            txtWireNo4L.setText((String)ObjSalesParty.getAttribute("MM_WIRE_DETAIL_4").getObj());
            txtDryer.setText((String)ObjSalesParty.getAttribute("MM_DRYER_SECTION").getObj());
            txtzone.setText((String)ObjSalesParty.getAttribute("MM_ZONE").getObj());
            txtMachineStatus.setText((String)ObjSalesParty.getAttribute("MM_MACHINE_STATUS").getObj());
            txtCapacity.setText((String)ObjSalesParty.getAttribute("MM_CAPACITY").getObj());
            txtsurveydate.setText((String)ObjSalesParty.getAttribute("MM_DATE_OF_UPDATE").getObj());
            
            try{
                    String last_update_date = data.getStringValueFromDB("SELECT MAX(APPROVED_DATE) FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE  MM_PARTY_CODE = '"+(String)ObjSalesParty.getAttribute("MM_PARTY_CODE").getObj()+"'  AND MM_MACHINE_NO = "+(String)ObjSalesParty.getAttribute("MM_MACHINE_NO").getObj()+"  AND APPROVED = 1  AND CANCELED = 0");
                    txtsurveydate.setText(EITLERPGLOBAL.formatDate(last_update_date));
            }catch(Exception e)
            {
            }
            
            txtinchargename.setText((String)ObjSalesParty.getAttribute("MM_ZONE_REPRESENTATIVE").getObj());
            txtTotalDryGrp.setText((String)ObjSalesParty.getAttribute("MM_TOTAL_DRYER_GROUP").getObj());
            txtUnirunGrp.setText((String)ObjSalesParty.getAttribute("MM_UNIRUM_GROUP").getObj());
            txtConventionalGroups.setText((String)ObjSalesParty.getAttribute("MM_CONVENTIONAL_GROUP").getObj());
            txtHoodType.setText((String)ObjSalesParty.getAttribute("MM_HOOD_TYPE").getObj());
            txtSizePress.setText((String)ObjSalesParty.getAttribute("MM_SIZE_PRESS").getObj());
            txtSheetDrynessSize.setText((String)ObjSalesParty.getAttribute("MM_SIZE_PRESS_POSITION").getObj());
            txtSizePressPosition.setText((String)ObjSalesParty.getAttribute("MM_SHEET_DRYNESS_SIZE_PRESS").getObj());
            txtDrivetype.setText((String)ObjSalesParty.getAttribute("MM_DRIVE_TYPE").getObj());
            txtmachinemake.setText((String)ObjSalesParty.getAttribute("MM_MACHINE_MAKE").getObj());
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());
            Renderer.removeBackColors();
            
            DoNotEvaluate=true;
            //===================Fill up Table===================//
            FormatGrid();
            //Now Generate Table
            for(int i=1;i<=ObjSalesParty.colMRItems.size();i++) {
                
                
                clsmachinesurveyitem ObjItem=(clsmachinesurveyitem)ObjSalesParty.colMRItems.get(Integer.toString(i));
                Object[] rowData=new Object[100];
                
                rowData[0]=(String)ObjItem.getAttribute("SR_NO").getObj();
                rowData[1]=(String)ObjItem.getAttribute("MM_MACHINE_POSITION").getObj();
                try{
                    String PositionCloseIndicator  = (String)ObjItem.getAttribute("POSITION_CLOSE_IND").getObj();
                   
                    if("1".equals(PositionCloseIndicator))
                    {
                        Renderer.setBackColor((i-1), 1, Color.RED);
                    }
                }catch(Exception e)
                {
                    //e.printStackTrace();
                }
                rowData[2]=(String)ObjItem.getAttribute("MM_MACHINE_POSITION_DESC").getObj();
                rowData[3]=(String)ObjItem.getAttribute("MM_COMBINATION_CODE").getObj();
            
                rowData[4]=(String)ObjItem.getAttribute("MM_CATEGORY").getObj();
                rowData[5]=(String)ObjItem.getAttribute("UC_CODE").getObj();
                rowData[6]=(String)ObjItem.getAttribute("MM_POSITION_DESIGN_NO").getObj();
                rowData[7]=(String)ObjItem.getAttribute("MM_UPN_NO").getObj();
                
                rowData[8]=(String)ObjItem.getAttribute("MM_ITEM_CODE").getObj();
                rowData[9]=(String)ObjItem.getAttribute("MM_GRUP").getObj();
                rowData[10]=(String)ObjItem.getAttribute("MM_PRESS_TYPE").getObj();
                rowData[11]=(String)ObjItem.getAttribute("MM_PRESS_ROLL_DAI_MM").getObj();
                rowData[12]=(String)ObjItem.getAttribute("MM_PRESS_ROLL_FACE_TOTAL_MM").getObj();
                rowData[13]=(String)ObjItem.getAttribute("MM_PRESS_ROLL_FACE_NET_MM").getObj();
                rowData[14]=(String)ObjItem.getAttribute("MM_FELT_ROLL_WIDTH_MM").getObj();
                rowData[15]=(String)ObjItem.getAttribute("MM_PRESS_LOAD").getObj();
                rowData[16]=(String)ObjItem.getAttribute("MM_VACCUM_CAPACITY").getObj();
                rowData[17]=(String)ObjItem.getAttribute("MM_UHLE_BOX").getObj();
                rowData[18]=(String)ObjItem.getAttribute("MM_HP_SHOWER").getObj();
                rowData[19]=(String)ObjItem.getAttribute("MM_LP_SHOWER").getObj();
                rowData[20]=(String)ObjItem.getAttribute("MM_FELT_LENGTH").getObj();
                rowData[21]=(String)ObjItem.getAttribute("MM_FELT_WIDTH").getObj();
                rowData[22]=(String)ObjItem.getAttribute("MM_FELT_GSM").getObj();
                //rowData[19]=(String)ObjItem.getAttribute("MM_FELT_WEIGHT").getObj();
                if(((String)ObjItem.getAttribute("MM_FELT_WEIGHT").getObj()).equals(""))
                {
                    rowData[23]="";
                }
                else
                {
                    rowData[23]=""+EITLERPGLOBAL.round(Double.parseDouble((String)ObjItem.getAttribute("MM_FELT_WEIGHT").getObj()), 2);
                }
                rowData[24]=(String)ObjItem.getAttribute("MM_FELT_TYPE").getObj();
                rowData[25]=(String)ObjItem.getAttribute("MM_FELT_STYLE").getObj();
                rowData[26]=(String)ObjItem.getAttribute("MM_AVG_LIFE").getObj();
                rowData[27]=(String)ObjItem.getAttribute("MM_AVG_PRODUCTION").getObj();
                rowData[28]=(String)ObjItem.getAttribute("MM_FELT_CONSUMPTION").getObj();
                rowData[29]=(String)ObjItem.getAttribute("MM_DINESH_SHARE").getObj();
                rowData[30]=(String)ObjItem.getAttribute("MM_REMARK_DESIGN").getObj();
                rowData[31]=(String)ObjItem.getAttribute("MM_REMARK_GENERAL").getObj();
                rowData[32]=(String)ObjItem.getAttribute("MM_NO_DRYER_CYLINDER").getObj();
                rowData[33]=(String)ObjItem.getAttribute("MM_CYLINDER_DIA_MM").getObj();
                rowData[34]=(String)ObjItem.getAttribute("MM_CYLINDER_FACE_NET_MM").getObj();
                rowData[35]=(String)ObjItem.getAttribute("MM_FELT_LIFE").getObj();
                rowData[36]=(String)ObjItem.getAttribute("MM_TPD").getObj();
                rowData[37]=(String)ObjItem.getAttribute("MM_TOTAL_PRODUCTION").getObj();
                rowData[38]=(String)ObjItem.getAttribute("MM_PAPER_FELT").getObj();
                rowData[39]=(String)ObjItem.getAttribute("MM_DRIVE_TYPE").getObj();
                rowData[40]=(String)ObjItem.getAttribute("MM_GUIDE_TYPE").getObj();
                rowData[41]=(String)ObjItem.getAttribute("MM_GUIDE_PAM_TYPE").getObj();
                rowData[42]=(String)ObjItem.getAttribute("MM_VENTILATION_TYPE").getObj();
                rowData[43]=(String)ObjItem.getAttribute("MM_FABRIC_LENGTH").getObj();
                rowData[44]=(String)ObjItem.getAttribute("MM_FABRIC_WIDTH").getObj();
                //rowData[41]=(String)ObjItem.getAttribute("MM_SIZE_M2").getObj();
                if(((String)ObjItem.getAttribute("MM_SIZE_M2").getObj()).equals(""))
                {
                    rowData[45]="";
                }
                else
                {
                    rowData[45]=""+EITLERPGLOBAL.round(Double.parseDouble((String)ObjItem.getAttribute("MM_SIZE_M2").getObj()), 2);
                }
                rowData[46]=(String)ObjItem.getAttribute("MM_SCREEN_TYPE").getObj();
                rowData[47]=(String)ObjItem.getAttribute("MM_STYLE_DRY").getObj();
                rowData[48]=(String)ObjItem.getAttribute("MM_CFM_DRY").getObj();
                rowData[49]=(String)ObjItem.getAttribute("MM_AVG_LIFE_DRY").getObj();
                rowData[50]=(String)ObjItem.getAttribute("MM_CONSUMPTION_DRY").getObj();
                rowData[51]=(String)ObjItem.getAttribute("MM_DINESH_SHARE_DRY").getObj();
                rowData[52]=(String)ObjItem.getAttribute("MM_REMARK_DRY").getObj();
                rowData[53]=(String)ObjItem.getAttribute("MM_BASE_GSM").getObj();
                rowData[54]=(String)ObjItem.getAttribute("MM_WEB_GSM").getObj();
                rowData[55]=(String)ObjItem.getAttribute("MM_TOTAL_GSM").getObj();
                rowData[56]=(String)ObjItem.getAttribute("MM_POSITION_WISE").getObj();
                rowData[57]=(String)ObjItem.getAttribute("MM_HARDNESS").getObj();
                rowData[58]=(String)ObjItem.getAttribute("MM_FELT_WASHING_CHEMICALS").getObj();
                rowData[59]=(String)ObjItem.getAttribute("MM_VACCUM_IN_UHLE_BOX").getObj();
                
                rowData[60]=(String)ObjItem.getAttribute("MM_MACHINE_FLOOR").getObj();
                rowData[61]=(String)ObjItem.getAttribute("MM_NUMBER_OF_FORMING_FABRIC").getObj();
                rowData[62]=(String)ObjItem.getAttribute("MM_TYPE_OF_FORMING_FABRIC").getObj();
                rowData[63]=(String)ObjItem.getAttribute("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH").getObj();
                rowData[64]=(String)ObjItem.getAttribute("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH").getObj();
                rowData[65]=(String)ObjItem.getAttribute("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH").getObj();
                rowData[66]=(String)ObjItem.getAttribute("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH").getObj();
                rowData[67]=(String)ObjItem.getAttribute("MM_WASH_ROLL_SHOWER").getObj();
                rowData[68]=(String)ObjItem.getAttribute("MM_HP_SHOWER_NOZZLES").getObj();
                rowData[69]=(String)ObjItem.getAttribute("MM_UHLE_BOX_VACUUM").getObj();
                rowData[70]=(String)ObjItem.getAttribute("MM_CHEMICAL_SHOWER").getObj();
                rowData[71]=(String)ObjItem.getAttribute("MM_1ST_LINEAR_NIP_PRESSURE").getObj();
                rowData[72]=(String)ObjItem.getAttribute("MM_2ND_LINEAR_NIP_PRESSURE").getObj();
                rowData[73]=(String)ObjItem.getAttribute("MM_3RD_LINEAR_NIP_PRESSURE").getObj();
                rowData[74]=(String)ObjItem.getAttribute("MM_4TH_LINEAR_NIP_PRESSURE").getObj();
                rowData[75]=(String)ObjItem.getAttribute("MM_LOADING_SYSTEM").getObj();
                rowData[76]=(String)ObjItem.getAttribute("MM_LP_SHOWER_NOZZLES").getObj();
                rowData[77]=(String)ObjItem.getAttribute("MM_1ST_ROLL_MATERIAL").getObj();
                rowData[78]=(String)ObjItem.getAttribute("MM_2ND_ROLL_MATERIAL").getObj();
                rowData[79]=(String)ObjItem.getAttribute("MM_3RD_ROLL_MATERIAL").getObj();
                rowData[80]=(String)ObjItem.getAttribute("MM_4TH_ROLL_MATERIAL").getObj();
                rowData[81]=(String)ObjItem.getAttribute("MM_5TH_ROLL_MATERIAL").getObj();
                rowData[82]=(String)ObjItem.getAttribute("MM_6TH_ROLL_MATERIAL").getObj();
                rowData[83]=(String)ObjItem.getAttribute("MM_7TH_ROLL_MATERIAL").getObj();
                rowData[84]=(String)ObjItem.getAttribute("MM_8TH_ROLL_MATERIAL").getObj();
                rowData[85]=(String)ObjItem.getAttribute("MM_BATT_GSM").getObj();
                rowData[86]=(String)ObjItem.getAttribute("MM_FIBERS_USED").getObj();
                rowData[87]=(String)ObjItem.getAttribute("MM_STRETCH").getObj();
                rowData[88]=(String)ObjItem.getAttribute("MM_MG").getObj();
                rowData[89]=(String)ObjItem.getAttribute("MM_YANKEE").getObj();
                rowData[90]=(String)ObjItem.getAttribute("MM_MG_YANKEE_NIP_LOAD").getObj();
                
                rowData[91]=(String)ObjItem.getAttribute("MM_MIN_CIRCUIT_LENGTH").getObj();
                rowData[92]=(String)ObjItem.getAttribute("MM_MAX_CIRCUIT_LENGTH").getObj();
                rowData[93]=(String)ObjItem.getAttribute("GOAL").getObj();
                
                DataModelDesc.addRow(rowData);
            }
            
            DoNotEvaluate=false;
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridApprovalStatus();
            HashMap List=new HashMap();
            
            String ProformaNo=ObjSalesParty.getAttribute("MM_DOC_NO").getString();
            List=clsFeltProductionApprovalFlow.getDocumentFlow(clsmachinesurvey.ModuleID, ProformaNo);
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
            HashMap History=clsmachinesurvey.getHistoryList(EITLERPGLOBAL.gCompanyID, ProformaNo);
            for(int i=1;i<=History.size();i++) {
                clsmachinesurvey ObjHistory=(clsmachinesurvey)History.get(Integer.toString(i));
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
                    Approved_By = (int)ObjHistory.getAttribute("UPDATE_BY").getVal();
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Display Data Error: " + e.getMessage());
        }
        DisplayStatus();
    }
    
    
    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        
        ObjSalesParty.setAttribute("MM_DOC_NO",txtPartycode.getText()+txtmachineno.getText());
        ObjSalesParty.setAttribute("MM_PARTY_CODE",txtPartycode.getText());
        ObjSalesParty.setAttribute("PARTY_NAME",txtPartyname.getText());
        ObjSalesParty.setAttribute("DISPATCH_STATION",txtPartystation.getText());
        ObjSalesParty.setAttribute("MM_MACHINE_NO",txtmachineno.getText());
        ObjSalesParty.setAttribute("MM_MACHINE_TYPE_FORMING",txtmachinetype.getText());
        ObjSalesParty.setAttribute("MM_PAPER_GRADE",txtpapergrade.getText());
        ObjSalesParty.setAttribute("MM_MACHINE_SPEED_RANGE",txtspeedrange.getText());
        ObjSalesParty.setAttribute("MM_PAPER_GSM_RANGE",txtpapergsmrange.getText());
        ObjSalesParty.setAttribute("MM_MACHINE_TYPE_PRESSING",txtpresstype.getText());
        ObjSalesParty.setAttribute("MM_FURNISH",txtfurnish.getText());
        ObjSalesParty.setAttribute("MM_TYPE_OF_FILLER",txttypeoffiller.getText());
        ObjSalesParty.setAttribute("MM_PAPER_DECKLE_AFTER_WIRE",txtPeparDeckleWIre.getText());
        ObjSalesParty.setAttribute("MM_PAPER_DECKLE_AFTER_PRESS",txtPaperDecklePress.getText());
        ObjSalesParty.setAttribute("MM_PAPER_DECKLE_AT_POPE_REEL",txtDecklePopeReel.getText());
        ObjSalesParty.setAttribute("MM_DRYER_SECTION",txtDryer.getText());
        ObjSalesParty.setAttribute("MM_WIRE_DETAIL_1",txtWireNo1L.getText());
        ObjSalesParty.setAttribute("MM_WIRE_DETAIL_2",txtWireNo2L.getText());
        ObjSalesParty.setAttribute("MM_WIRE_DETAIL_3",txtWireNo3L.getText());
        ObjSalesParty.setAttribute("MM_WIRE_DETAIL_4",txtWireNo4L.getText());
        ObjSalesParty.setAttribute("MM_ZONE",txtzone.getText());
        ObjSalesParty.setAttribute("MM_CAPACITY",txtCapacity.getText().trim());
        ObjSalesParty.setAttribute("MM_ZONE_REPRESENTATIVE",txtinchargename.getText());
        ObjSalesParty.setAttribute("MM_MACHINE_STATUS",txtMachineStatus.getText());
        if (EITLERPGLOBAL.formatDateDB(txtsurveydate.getText()).equals("")) {
            ObjSalesParty.setAttribute("MM_DATE_OF_UPDATE", "0000-00-00");
        } else {
            ObjSalesParty.setAttribute("MM_DATE_OF_UPDATE", EITLERPGLOBAL.formatDateDB(txtsurveydate.getText()));
        }
        ObjSalesParty.setAttribute("MM_TOTAL_DRYER_GROUP",txtTotalDryGrp.getText());
        ObjSalesParty.setAttribute("MM_UNIRUM_GROUP",txtUnirunGrp.getText());
        ObjSalesParty.setAttribute("MM_CONVENTIONAL_GROUP",txtConventionalGroups.getText());
        ObjSalesParty.setAttribute("MM_HOOD_TYPE",txtHoodType.getText());
        ObjSalesParty.setAttribute("MM_SIZE_PRESS",txtSizePress.getText());
        ObjSalesParty.setAttribute("MM_SIZE_PRESS_POSITION",txtSheetDrynessSize.getText());
        ObjSalesParty.setAttribute("MM_SHEET_DRYNESS_SIZE_PRESS",txtSizePressPosition.getText());
        ObjSalesParty.setAttribute("MM_DRIVE_TYPE",txtDrivetype.getText());
        ObjSalesParty.setAttribute("MM_MACHINE_MAKE",txtmachinemake.getText());
        
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
            clsmachinesurveyitem ObjItem=new clsmachinesurveyitem();
            ObjItem.setAttribute("MM_PARTY_CODE",txtPartycode.getText());
            ObjItem.setAttribute("MM_DOC_NO",txtmachinedocno.getText());
            ObjItem.setAttribute("SR_NO",DataModelDesc.getValueByVariable("SR_NO", i));
            System.out.println(DataModelDesc.getValueByVariable("SR_NO", i));
            ObjItem.setAttribute("MM_MACHINE_POSITION",DataModelDesc.getValueByVariable("MM_MACHINE_POSITION", i));
            ObjItem.setAttribute("MM_MACHINE_POSITION_DESC",DataModelDesc.getValueByVariable("MM_MACHINE_POSITION_DESC", i));
            ObjItem.setAttribute("MM_COMBINATION_CODE",DataModelDesc.getValueByVariable("MM_COMBINATION_CODE", i));
            ObjItem.setAttribute("MM_ITEM_CODE",DataModelDesc.getValueByVariable("MM_ITEM_CODE", i));
            ObjItem.setAttribute("MM_GRUP",DataModelDesc.getValueByVariable("MM_GRUP", i));
            ObjItem.setAttribute("MM_PRESS_TYPE",DataModelDesc.getValueByVariable("MM_PRESS_TYPE", i));
            ObjItem.setAttribute("MM_PRESS_ROLL_DAI_MM",DataModelDesc.getValueByVariable("MM_PRESS_ROLL_DAI_MM", i));
            ObjItem.setAttribute("MM_PRESS_ROLL_FACE_TOTAL_MM",DataModelDesc.getValueByVariable("MM_PRESS_ROLL_FACE_TOTAL_MM", i));
            ObjItem.setAttribute("MM_PRESS_ROLL_FACE_NET_MM",DataModelDesc.getValueByVariable("MM_PRESS_ROLL_FACE_NET_MM", i));
            ObjItem.setAttribute("MM_FELT_ROLL_WIDTH_MM",DataModelDesc.getValueByVariable("MM_FELT_ROLL_WIDTH_MM", i));
            ObjItem.setAttribute("MM_PRESS_LOAD",DataModelDesc.getValueByVariable("MM_PRESS_LOAD", i));
            ObjItem.setAttribute("MM_VACCUM_CAPACITY",DataModelDesc.getValueByVariable("MM_VACCUM_CAPACITY", i));
            ObjItem.setAttribute("MM_UHLE_BOX",DataModelDesc.getValueByVariable("MM_UHLE_BOX", i));
            ObjItem.setAttribute("MM_HP_SHOWER",DataModelDesc.getValueByVariable("MM_HP_SHOWER", i));
            ObjItem.setAttribute("MM_LP_SHOWER",DataModelDesc.getValueByVariable("MM_LP_SHOWER", i));
            ObjItem.setAttribute("MM_FELT_LENGTH",DataModelDesc.getValueByVariable("MM_FELT_LENGTH", i));
            ObjItem.setAttribute("MM_FELT_WIDTH",DataModelDesc.getValueByVariable("MM_FELT_WIDTH", i));
            ObjItem.setAttribute("MM_FELT_GSM",DataModelDesc.getValueByVariable("MM_FELT_GSM", i));
            ObjItem.setAttribute("MM_FELT_WEIGHT",DataModelDesc.getValueByVariable("MM_FELT_WEIGHT", i));
            ObjItem.setAttribute("MM_FELT_TYPE",DataModelDesc.getValueByVariable("MM_FELT_TYPE", i));
            ObjItem.setAttribute("MM_FELT_STYLE",DataModelDesc.getValueByVariable("MM_FELT_STYLE", i));
            ObjItem.setAttribute("MM_AVG_LIFE",DataModelDesc.getValueByVariable("MM_AVG_LIFE", i));
            ObjItem.setAttribute("MM_AVG_PRODUCTION",DataModelDesc.getValueByVariable("MM_AVG_PRODUCTION", i));
            ObjItem.setAttribute("MM_FELT_CONSUMPTION",DataModelDesc.getValueByVariable("MM_FELT_CONSUMPTION", i));
            ObjItem.setAttribute("MM_DINESH_SHARE",DataModelDesc.getValueByVariable("MM_DINESH_SHARE", i));
            ObjItem.setAttribute("MM_REMARK_DESIGN",DataModelDesc.getValueByVariable("MM_REMARK_DESIGN", i));
            ObjItem.setAttribute("MM_REMARK_GENERAL",DataModelDesc.getValueByVariable("MM_REMARK_GENERAL", i));
            ObjItem.setAttribute("MM_NO_DRYER_CYLINDER",DataModelDesc.getValueByVariable("MM_NO_DRYER_CYLINDER", i));
            ObjItem.setAttribute("MM_CYLINDER_DIA_MM",DataModelDesc.getValueByVariable("MM_CYLINDER_DIA_MM", i));
            ObjItem.setAttribute("MM_CYLINDER_FACE_NET_MM",DataModelDesc.getValueByVariable("MM_CYLINDER_FACE_NET_MM", i));
            ObjItem.setAttribute("MM_FELT_LIFE",DataModelDesc.getValueByVariable("MM_FELT_LIFE", i));
            ObjItem.setAttribute("MM_TPD",DataModelDesc.getValueByVariable("MM_TPD", i));
            ObjItem.setAttribute("MM_TOTAL_PRODUCTION",DataModelDesc.getValueByVariable("MM_TOTAL_PRODUCTION", i));
            ObjItem.setAttribute("MM_PAPER_FELT",DataModelDesc.getValueByVariable("MM_PAPER_FELT", i));
            ObjItem.setAttribute("MM_DRIVE_TYPE",DataModelDesc.getValueByVariable("MM_DRIVE_TYPE", i));
            ObjItem.setAttribute("MM_GUIDE_TYPE",DataModelDesc.getValueByVariable("MM_GUIDE_TYPE", i));
            ObjItem.setAttribute("MM_GUIDE_PAM_TYPE",DataModelDesc.getValueByVariable("MM_GUIDE_PAM_TYPE", i));
            ObjItem.setAttribute("MM_VENTILATION_TYPE",DataModelDesc.getValueByVariable("MM_VENTILATION_TYPE", i));
            ObjItem.setAttribute("MM_FABRIC_LENGTH",DataModelDesc.getValueByVariable("MM_FABRIC_LENGTH", i));
            ObjItem.setAttribute("MM_FABRIC_WIDTH",DataModelDesc.getValueByVariable("MM_FABRIC_WIDTH", i));
            ObjItem.setAttribute("MM_SIZE_M2",DataModelDesc.getValueByVariable("MM_SIZE_M2", i));
            ObjItem.setAttribute("MM_SCREEN_TYPE",DataModelDesc.getValueByVariable("MM_SCREEN_TYPE", i));
            ObjItem.setAttribute("MM_STYLE_DRY",DataModelDesc.getValueByVariable("MM_STYLE_DRY", i));
            ObjItem.setAttribute("MM_CFM_DRY",DataModelDesc.getValueByVariable("MM_CFM_DRY", i));
            ObjItem.setAttribute("MM_AVG_LIFE_DRY",DataModelDesc.getValueByVariable("MM_AVG_LIFE_DRY", i));
            ObjItem.setAttribute("MM_CONSUMPTION_DRY",DataModelDesc.getValueByVariable("MM_CONSUMPTION_DRY", i));
            ObjItem.setAttribute("MM_DINESH_SHARE_DRY",DataModelDesc.getValueByVariable("MM_DINESH_SHARE_DRY", i));
            ObjItem.setAttribute("MM_REMARK_DRY",DataModelDesc.getValueByVariable("MM_REMARK_DRY", i));
            ObjItem.setAttribute("MM_BASE_GSM",DataModelDesc.getValueByVariable("MM_BASE_GSM", i));
            ObjItem.setAttribute("MM_WEB_GSM",DataModelDesc.getValueByVariable("MM_WEB_GSM", i));
            ObjItem.setAttribute("MM_TOTAL_GSM",DataModelDesc.getValueByVariable("MM_TOTAL_GSM", i));
            ObjItem.setAttribute("MM_POSITION_WISE",DataModelDesc.getValueByVariable("MM_POSITION_WISE", i));
            ObjItem.setAttribute("MM_HARDNESS",DataModelDesc.getValueByVariable("MM_HARDNESS", i));
            ObjItem.setAttribute("MM_FELT_WASHING_CHEMICALS",DataModelDesc.getValueByVariable("MM_FELT_WASHING_CHEMICALS", i));
            ObjItem.setAttribute("MM_VACCUM_IN_UHLE_BOX",DataModelDesc.getValueByVariable("MM_VACCUM_IN_UHLE_BOX", i));
            ObjItem.setAttribute("MM_MACHINE_FLOOR",DataModelDesc.getValueByVariable("MM_MACHINE_FLOOR", i));
            ObjItem.setAttribute("MM_NUMBER_OF_FORMING_FABRIC",DataModelDesc.getValueByVariable("MM_NUMBER_OF_FORMING_FABRIC", i));
            ObjItem.setAttribute("MM_TYPE_OF_FORMING_FABRIC",DataModelDesc.getValueByVariable("MM_TYPE_OF_FORMING_FABRIC", i));
            ObjItem.setAttribute("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH",DataModelDesc.getValueByVariable("MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH", i));
            ObjItem.setAttribute("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH",DataModelDesc.getValueByVariable("MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH", i));
            ObjItem.setAttribute("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH",DataModelDesc.getValueByVariable("MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH", i));
            ObjItem.setAttribute("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH",DataModelDesc.getValueByVariable("MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH", i));
            ObjItem.setAttribute("MM_WASH_ROLL_SHOWER",DataModelDesc.getValueByVariable("MM_WASH_ROLL_SHOWER", i));
            ObjItem.setAttribute("MM_HP_SHOWER_NOZZLES",DataModelDesc.getValueByVariable("MM_HP_SHOWER_NOZZLES", i));
            ObjItem.setAttribute("MM_UHLE_BOX_VACUUM",DataModelDesc.getValueByVariable("MM_UHLE_BOX_VACUUM", i));
            ObjItem.setAttribute("MM_CHEMICAL_SHOWER",DataModelDesc.getValueByVariable("MM_CHEMICAL_SHOWER", i));
            ObjItem.setAttribute("MM_1ST_LINEAR_NIP_PRESSURE",DataModelDesc.getValueByVariable("MM_1ST_LINEAR_NIP_PRESSURE", i));
            ObjItem.setAttribute("MM_2ND_LINEAR_NIP_PRESSURE",DataModelDesc.getValueByVariable("MM_2ND_LINEAR_NIP_PRESSURE", i));
            ObjItem.setAttribute("MM_3RD_LINEAR_NIP_PRESSURE",DataModelDesc.getValueByVariable("MM_3RD_LINEAR_NIP_PRESSURE", i));
            ObjItem.setAttribute("MM_4TH_LINEAR_NIP_PRESSURE",DataModelDesc.getValueByVariable("MM_4TH_LINEAR_NIP_PRESSURE", i));
            ObjItem.setAttribute("MM_LOADING_SYSTEM",DataModelDesc.getValueByVariable("MM_LOADING_SYSTEM", i));
            ObjItem.setAttribute("MM_LP_SHOWER_NOZZLES",DataModelDesc.getValueByVariable("MM_LP_SHOWER_NOZZLES", i));
            ObjItem.setAttribute("MM_1ST_ROLL_MATERIAL",DataModelDesc.getValueByVariable("MM_1ST_ROLL_MATERIAL", i));
            ObjItem.setAttribute("MM_2ND_ROLL_MATERIAL",DataModelDesc.getValueByVariable("MM_2ND_ROLL_MATERIAL", i));
            ObjItem.setAttribute("MM_3RD_ROLL_MATERIAL",DataModelDesc.getValueByVariable("MM_3RD_ROLL_MATERIAL", i));
            ObjItem.setAttribute("MM_4TH_ROLL_MATERIAL",DataModelDesc.getValueByVariable("MM_4TH_ROLL_MATERIAL", i));
            ObjItem.setAttribute("MM_5TH_ROLL_MATERIAL",DataModelDesc.getValueByVariable("MM_5TH_ROLL_MATERIAL", i));
            ObjItem.setAttribute("MM_6TH_ROLL_MATERIAL",DataModelDesc.getValueByVariable("MM_6TH_ROLL_MATERIAL", i));
            ObjItem.setAttribute("MM_7TH_ROLL_MATERIAL",DataModelDesc.getValueByVariable("MM_7TH_ROLL_MATERIAL", i));
            ObjItem.setAttribute("MM_8TH_ROLL_MATERIAL",DataModelDesc.getValueByVariable("MM_8TH_ROLL_MATERIAL", i));
            ObjItem.setAttribute("MM_BATT_GSM",DataModelDesc.getValueByVariable("MM_BATT_GSM", i));
            ObjItem.setAttribute("MM_FIBERS_USED",DataModelDesc.getValueByVariable("MM_FIBERS_USED", i));
            ObjItem.setAttribute("MM_STRETCH",DataModelDesc.getValueByVariable("MM_STRETCH", i));
            ObjItem.setAttribute("MM_MG",DataModelDesc.getValueByVariable("MM_MG", i));
            ObjItem.setAttribute("MM_YANKEE",DataModelDesc.getValueByVariable("MM_YANKEE", i));
            ObjItem.setAttribute("MM_MG_YANKEE_NIP_LOAD",DataModelDesc.getValueByVariable("MM_MG_YANKEE_NIP_LOAD", i));
            //MM_CATEGORY
            ObjItem.setAttribute("MM_CATEGORY",DataModelDesc.getValueByVariable("MM_CATEGORY", i));
            ObjItem.setAttribute("UC_CODE",DataModelDesc.getValueByVariable("UC_CODE", i));
            ObjItem.setAttribute("MM_POSITION_DESIGN_NO",DataModelDesc.getValueByVariable("MM_POSITION_DESIGN_NO", i));
            ObjItem.setAttribute("MM_UPN_NO",DataModelDesc.getValueByVariable("MM_UPN_NO", i));
            
            ObjItem.setAttribute("MM_MIN_CIRCUIT_LENGTH",DataModelDesc.getValueByVariable("MM_MIN_CIRCUIT_LENGTH", i));
            ObjItem.setAttribute("MM_MAX_CIRCUIT_LENGTH",DataModelDesc.getValueByVariable("MM_MAX_CIRCUIT_LENGTH", i));
            ObjItem.setAttribute("GOAL",DataModelDesc.getValueByVariable("GOAL", i));
            
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
    }
    
    private void FormatGridDiscount(){
        
    }
    
    private void FormatGridOtherpartyDiscount(){
        
    }
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID,7037,70371)) { //7008,70081
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7037,70372)) { //7008,70082
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7037,70373)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7037,70374)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
        }
    }
    
    private void EnableApproval() {
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        txtToRemarks.setEnabled(true);
        txtmachineno.setEnabled(true);
        txtPartycode.setEnabled(true);
        txtPartyname.setEnabled(true);
        txtmachinetype.setEnabled(true);
        txtpapergrade.setEnabled(true);
        txtpapergsmrange.setEnabled(true);
        txtpresstype.setEnabled(true);
        txtspeedrange.setEnabled(true);
        txtPeparDeckleWIre.setEnabled(true);
        txtPaperDecklePress.setEnabled(true);
        txtDecklePopeReel.setEnabled(true);
        txtWireNo1L.setEnabled(true);
        txtWireNo2L.setEnabled(true);
        txtWireNo3L.setEnabled(true);
        txtWireNo4L.setEnabled(true);
        txtfurnish.setEnabled(true);
        txttypeoffiller.setEnabled(true);
        txtzone.setEnabled(true);
        txtDryer.setEnabled(true);
        txtMachineStatus.setEnabled(true);
        txtCapacity.setEnabled(true);
        txtinchargename.setEnabled(true);
        txtsurveydate.setEnabled(true);
        txtTotalDryGrp.setEnabled(true);
        txtSizePress.setEnabled(true);
        txtUnirunGrp.setEnabled(true);
        txtSizePressPosition.setEnabled(true);
        txtDrivetype.setEnabled(true);
        txtmachinemake.setEnabled(true);
        txtConventionalGroups.setEnabled(true);
        txtHoodType.setEnabled(true);
        txtSheetDrynessSize.setEnabled(true);
      
        SetupApproval();
        
        //========== Setting Up Header Fields ================//
        String FieldName="";
        int SelHierarchy=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        
        for(int i=0;i<Tab1.getComponentCount()-1;i++) {
            if(Tab1.getComponent(i).getName()!=null) {
                
                FieldName=Tab1.getComponent(i).getName();
                
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
        ObjSalesParty.Filter(" MM_DOC_NO IN (SELECT PRODUCTION.FELT_MACHINE_MASTER_HEADER.MM_DOC_NO FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_HEADER.MM_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=724)");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }
    
    
    public void FindEx(int pCompanyID,String pPartyCode) {
        
        ObjSalesParty.Filter(" MM_DOC_NO='"+pPartyCode+"'");
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
            
            boolean IncludeUser=false;
            //Decide to include user or not
            if(EditMode==EITLERPGLOBAL.EDIT) {
                if(OpgApprove.isSelected()) {
                    IncludeUser=clsFeltProductionApprovalFlow.IncludeUserInApproval(clsmachinesurvey.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
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
            int Creator=clsFeltProductionApprovalFlow.getCreator(clsmachinesurvey.ModuleID, machinedocno);
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
        Updating=true; //Stops recursion
        try{
            cmdAdd.requestFocus();
            
            DataModelDesc=new EITLTableModel();
            TableDesc.removeAll();
            TableDesc.setModel(DataModelDesc);
            TableColumnModel ColModel=TableDesc.getColumnModel();
            TableDesc.setAutoResizeMode(TableDesc.AUTO_RESIZE_OFF);
            //EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
            //Renderer.setColor(0, 0, Color.LIGHT_GRAY);
            
            
            DataModelDesc.addColumn("Sr.");    //0
            DataModelDesc.addColumn("Machine Position/Dryer Group");    //1
            DataModelDesc.addColumn("Machine Position Description");    //2
            DataModelDesc.addColumn("Combination Code");    //3
            DataModelDesc.addColumn("Category");    //4
            DataModelDesc.addColumn("UC_CODE");    //5
           
            DataModelDesc.addColumn("Position Design No");    //6
            DataModelDesc.addColumn("UPN No");    //7
            DataModelDesc.addColumn("Product Code");    //4
            DataModelDesc.addColumn("Product Group");    //5
            DataModelDesc.addColumn("Press Type");    //6
            DataModelDesc.addColumn("Press Roll Dia(mm)");    //7
            DataModelDesc.addColumn("Press Roll Face(Total)(mm)");    //8
            DataModelDesc.addColumn("Press Roll Face(Net)(mm)");    //9
            DataModelDesc.addColumn("Felt Roll Width(mm)");    //10
            DataModelDesc.addColumn("Press Load(kg.cm)");    //11
            DataModelDesc.addColumn("Vaccum Capacity(M3/Min");    //12
            DataModelDesc.addColumn("Uhle Box(Slot Width(mm) X No."); //13
            DataModelDesc.addColumn("HP Shower(Pressure)");    //14
            DataModelDesc.addColumn("LP Shower");    //15
            DataModelDesc.addColumn("Felt Length(mt)");    //16
            DataModelDesc.addColumn("Felt Width(mt)");    //17
            DataModelDesc.addColumn("Felt GSM");    //18
            DataModelDesc.addColumn("Felt Weight");    //19
            DataModelDesc.addColumn("Felt Type");    //20
            DataModelDesc.addColumn("Felt Style");    //21
            DataModelDesc.addColumn("Avg.Life(Days)");    //22
            DataModelDesc.addColumn("Avg.Production(tons)");    //23
            DataModelDesc.addColumn("Felt Consumption");    //24
            DataModelDesc.addColumn("Dinesh Share");    //25
            DataModelDesc.addColumn("Remark(Design/Perfomance");    //26
            DataModelDesc.addColumn("Remark(General)");    //27
            DataModelDesc.addColumn("No.Of Dryer Cylinder"); //28
            DataModelDesc.addColumn("Cylinder Dia.(mm)");    //29
            DataModelDesc.addColumn("Cylinder Face(net)(mm)"); //30
            DataModelDesc.addColumn("Felt Life");  //31
            DataModelDesc.addColumn("Capacity Utilization(TPD)");  //32
            DataModelDesc.addColumn("Total Production");  //33
            DataModelDesc.addColumn("Paper Felt(k.g)");  //34
            DataModelDesc.addColumn("Drive Type");  //35
            DataModelDesc.addColumn("Guide Type");//32
            DataModelDesc.addColumn("Guide Pam Type");//33
            DataModelDesc.addColumn("Ventilation Type");    //34
            DataModelDesc.addColumn("Febric Length(mt)");    //35
            DataModelDesc.addColumn("Febric Width(mt)");    //36
            DataModelDesc.addColumn("Size(m2)");    //37
            DataModelDesc.addColumn("Screen Type");    //38
            DataModelDesc.addColumn("Style");    //39
            DataModelDesc.addColumn("CFM");    //40
            DataModelDesc.addColumn("Avg.Life");    //41
            DataModelDesc.addColumn("Consumption");    //42
            DataModelDesc.addColumn("Dinesh Share(%)");    //43
            DataModelDesc.addColumn("Remark");    //44
            DataModelDesc.addColumn("Base Gsm");    //45
            DataModelDesc.addColumn("Web Gsm");//46
            DataModelDesc.addColumn("Total GSM");    //47
            DataModelDesc.addColumn("Machine Position Wise");    //47
            DataModelDesc.addColumn("Hardness (Shore Degree A,P&J)");    //47
            DataModelDesc.addColumn("Felt Washing Chemicals");    //47
            DataModelDesc.addColumn("Vaccum In Uhle Box(mmHg)");    //47
            DataModelDesc.addColumn("Machine Floor");    //56
            DataModelDesc.addColumn("Number Of Forming Fabric");    //57
            DataModelDesc.addColumn("Type Of Forming Fabric");    //58
            DataModelDesc.addColumn("Press Roll For 1st Nip,Face Length");    //59
            DataModelDesc.addColumn("Press Roll For 2nd Nip,Face Length");    //60
            DataModelDesc.addColumn("Press Roll For 3rd Nip,Face Length");    //61
            DataModelDesc.addColumn("Press Roll For 4th Nip,Face Length");    //62
            DataModelDesc.addColumn("Washh Roll Shower");    //63
            DataModelDesc.addColumn("HP Shower Nozzles");    //64
            DataModelDesc.addColumn("Uhle Box Vacuum");    //65
            DataModelDesc.addColumn("Chemical Shower");    //66
            DataModelDesc.addColumn("1st Linear Nip Pressure Gauge/Design");    //67
            DataModelDesc.addColumn("2nd Linear Nip Pressure Gauge/Design");    //68
            DataModelDesc.addColumn("3rd Linear Nip Pressure Gauge/Design");    //69
            DataModelDesc.addColumn("4th Linear Nip Pressure Gauge/Design");    //70
            DataModelDesc.addColumn("Loading System");    //71
            DataModelDesc.addColumn("LP Shower Nozzles");    //72
            DataModelDesc.addColumn("1st Roll Material");    //73
            DataModelDesc.addColumn("2nd Roll Material");    //74
            DataModelDesc.addColumn("3rd Roll Material");    //75
            DataModelDesc.addColumn("4th Roll Material");    //76
            DataModelDesc.addColumn("5th Roll Material");    //77
            DataModelDesc.addColumn("6th Roll Material");    //78
            DataModelDesc.addColumn("7th Roll Material");    //79
            DataModelDesc.addColumn("8th Roll Material");    //80
            DataModelDesc.addColumn("Batt GSM");    //81
            DataModelDesc.addColumn("Fibers Used");   //82
            DataModelDesc.addColumn("Stretch");    //83
            DataModelDesc.addColumn("MG");    //84
            DataModelDesc.addColumn("Yankee");    //85
            DataModelDesc.addColumn("MG/Yankee Load");    //86
            DataModelDesc.addColumn("Min. Circuit length");    //86
            DataModelDesc.addColumn("Max. Circuit length");    //86
            DataModelDesc.addColumn("Goal");    //
            
            DataModelDesc.SetVariable(0,"SR_NO");  //0
            DataModelDesc.SetVariable(1,"MM_MACHINE_POSITION");    //1
            DataModelDesc.SetVariable(2,"MM_MACHINE_POSITION_DESC");    //2
            DataModelDesc.SetVariable(3,"MM_COMBINATION_CODE");    //3
            DataModelDesc.SetVariable(4,"MM_CATEGORY");    //47
            DataModelDesc.SetVariable(5,"UC_CODE");    //47
            DataModelDesc.SetVariable(6,"MM_POSITION_DESIGN_NO");
            DataModelDesc.SetVariable(7,"MM_UPN_NO");
            DataModelDesc.SetVariable(8,"MM_ITEM_CODE");    //43
            DataModelDesc.SetVariable(9,"MM_GRUP");    //44
            DataModelDesc.SetVariable(10,"MM_PRESS_TYPE");    //4
            DataModelDesc.SetVariable(11,"MM_PRESS_ROLL_DAI_MM");    //5
            DataModelDesc.SetVariable(12,"MM_PRESS_ROLL_FACE_TOTAL_MM");    //6
            DataModelDesc.SetVariable(13,"MM_PRESS_ROLL_FACE_NET_MM");    //7
            DataModelDesc.SetVariable(14,"MM_FELT_ROLL_WIDTH_MM");    //8
            DataModelDesc.SetVariable(15,"MM_PRESS_LOAD");    //9
            DataModelDesc.SetVariable(16,"MM_VACCUM_CAPACITY");    //10
            DataModelDesc.SetVariable(17,"MM_UHLE_BOX");    //11
            DataModelDesc.SetVariable(18,"MM_HP_SHOWER");    //12
            DataModelDesc.SetVariable(19,"MM_LP_SHOWER");    //13
            DataModelDesc.SetVariable(20,"MM_FELT_LENGTH");    //14
            DataModelDesc.SetVariable(21,"MM_FELT_WIDTH");    //15
            DataModelDesc.SetVariable(22,"MM_FELT_GSM");    //16
            DataModelDesc.SetVariable(23,"MM_FELT_WEIGHT");    //17
            DataModelDesc.SetVariable(24,"MM_FELT_TYPE");    //18
            DataModelDesc.SetVariable(25,"MM_FELT_STYLE");    //19
            DataModelDesc.SetVariable(26,"MM_AVG_LIFE");    //20
            DataModelDesc.SetVariable(27,"MM_AVG_PRODUCTION");    //21
            DataModelDesc.SetVariable(28,"MM_FELT_CONSUMPTION");    //22
            DataModelDesc.SetVariable(29,"MM_DINESH_SHARE");    //23
            DataModelDesc.SetVariable(30,"MM_REMARK_DESIGN");    //24
            DataModelDesc.SetVariable(31,"MM_REMARK_GENERAL");    //25
            DataModelDesc.SetVariable(32,"MM_NO_DRYER_CYLINDER");    //26
            DataModelDesc.SetVariable(33,"MM_CYLINDER_DIA_MM");    //27
            DataModelDesc.SetVariable(34,"MM_CYLINDER_FACE_NET_MM");    //28
            DataModelDesc.SetVariable(35,"MM_FELT_LIFE");    //29
            DataModelDesc.SetVariable(36,"MM_TPD");    //29
            DataModelDesc.SetVariable(37,"MM_TOTAL_PRODUCTION");    //29
            DataModelDesc.SetVariable(38,"MM_PAPER_FELT");    //29
            DataModelDesc.SetVariable(39,"MM_DRIVE_TYPE");    //29
            DataModelDesc.SetVariable(40,"MM_GUIDE_TYPE");    //30
            DataModelDesc.SetVariable(41,"MM_GUIDE_PAM_TYPE");    //31
            DataModelDesc.SetVariable(42,"MM_VENTILATION_TYPE");    //32
            DataModelDesc.SetVariable(43,"MM_FABRIC_LENGTH");    //33
            DataModelDesc.SetVariable(44,"MM_FABRIC_WIDTH");    //34
            DataModelDesc.SetVariable(45,"MM_SIZE_M2");    //35
            DataModelDesc.SetVariable(46,"MM_SCREEN_TYPE");    //36
            DataModelDesc.SetVariable(47,"MM_STYLE_DRY");    //37
            DataModelDesc.SetVariable(48,"MM_CFM_DRY");    //38
            DataModelDesc.SetVariable(49,"MM_AVG_LIFE_DRY");    //39
            DataModelDesc.SetVariable(50,"MM_CONSUMPTION_DRY");    //40
            DataModelDesc.SetVariable(51,"MM_DINESH_SHARE_DRY ");    //41
            DataModelDesc.SetVariable(52,"MM_REMARK_DRY");    //42
            DataModelDesc.SetVariable(53,"MM_BASE_GSM");    //45
            DataModelDesc.SetVariable(54,"MM_WEB_GSM");    //46
            DataModelDesc.SetVariable(55,"MM_TOTAL_GSM");    //47
            DataModelDesc.SetVariable(56,"MM_POSITION_WISE");    //47
            DataModelDesc.SetVariable(57,"MM_HARDNESS");    //47
            DataModelDesc.SetVariable(58,"MM_FELT_WASHING_CHEMICALS");    //47
            DataModelDesc.SetVariable(59,"MM_VACCUM_IN_UHLE_BOX");    //47
            DataModelDesc.SetVariable(60,"MM_MACHINE_FLOOR");    //47
            DataModelDesc.SetVariable(61,"MM_NUMBER_OF_FORMING_FABRIC");    //47
            DataModelDesc.SetVariable(62,"MM_TYPE_OF_FORMING_FABRIC");    //47
            DataModelDesc.SetVariable(63,"MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH");    //47
            DataModelDesc.SetVariable(64,"MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH");    //47
            DataModelDesc.SetVariable(65,"MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH");    //47
            DataModelDesc.SetVariable(66,"MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH");    //47
            DataModelDesc.SetVariable(67,"MM_WASH_ROLL_SHOWER");    //47
            DataModelDesc.SetVariable(68,"MM_HP_SHOWER_NOZZLES");    //47
            DataModelDesc.SetVariable(69,"MM_UHLE_BOX_VACUUM");    //47
            DataModelDesc.SetVariable(70,"MM_CHEMICAL_SHOWER");    //47
            DataModelDesc.SetVariable(71,"MM_1ST_LINEAR_NIP_PRESSURE");    //47
            DataModelDesc.SetVariable(72,"MM_2ND_LINEAR_NIP_PRESSURE");    //47
            DataModelDesc.SetVariable(73,"MM_3RD_LINEAR_NIP_PRESSURE");    //47
            DataModelDesc.SetVariable(74,"MM_4TH_LINEAR_NIP_PRESSURE");    //47
            DataModelDesc.SetVariable(75,"MM_LOADING_SYSTEM");    //47
            DataModelDesc.SetVariable(76,"MM_LP_SHOWER_NOZZLES");    //47
            DataModelDesc.SetVariable(77,"MM_1ST_ROLL_MATERIA");    //47
            DataModelDesc.SetVariable(78,"MM_2ND_ROLL_MATERIA");    //47
            DataModelDesc.SetVariable(79,"MM_3RD_ROLL_MATERIAL");    //47
            DataModelDesc.SetVariable(80,"MM_4TH_ROLL_MATERIAL");    //47
            DataModelDesc.SetVariable(81,"MM_5TH_ROLL_MATERIAL");    //47
            DataModelDesc.SetVariable(82,"MM_6TH_ROLL_MATERIAL");    //47
            DataModelDesc.SetVariable(83,"MM_7TH_ROLL_MATERIAL");    //47
            DataModelDesc.SetVariable(84,"MM_8TH_ROLL_MATERIAL");    //47
            DataModelDesc.SetVariable(85,"MM_BATT_GSM");    //47
            DataModelDesc.SetVariable(86,"MM_FIBERS_USED");    //47
            DataModelDesc.SetVariable(87,"MM_STRETCH");    //47
            DataModelDesc.SetVariable(88,"MM_MG");    //47
            DataModelDesc.SetVariable(89,"MM_YANKEE");    //47
            DataModelDesc.SetVariable(90,"MM_MG_YANKEE_NIP_LOAD");    //
            DataModelDesc.SetVariable(91,"MM_MIN_CIRCUIT_LENGTH");    //
            DataModelDesc.SetVariable(92,"MM_MAX_CIRCUIT_LENGTH");    //
            DataModelDesc.SetVariable(93,"GOAL");    //
            
               //    
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
            DataModelDesc.SetReadOnly(33);
            DataModelDesc.SetReadOnly(34);
            DataModelDesc.SetReadOnly(38);
            DataModelDesc.SetReadOnly(39);
            DataModelDesc.SetReadOnly(40);
            DataModelDesc.SetReadOnly(41);
            DataModelDesc.SetReadOnly(42);
            DataModelDesc.SetReadOnly(43);
            DataModelDesc.SetReadOnly(44);
            DataModelDesc.SetReadOnly(45);
            DataModelDesc.SetReadOnly(46);
            DataModelDesc.SetReadOnly(47);
            DataModelDesc.SetReadOnly(48);
            DataModelDesc.SetReadOnly(49);
            DataModelDesc.SetReadOnly(50);
            DataModelDesc.SetReadOnly(51);
            
            DataModelDesc.SetReadOnly(87);
            DataModelDesc.SetReadOnly(88);
          
            
            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
//            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            TableDesc.getColumnModel().getColumn(1).setWidth(150);
            TableDesc.getColumnModel().getColumn(17).setPreferredWidth(100);
            TableDesc.getColumnModel().getColumn(DataModelDesc.getColFromVariable("MM_MACHINE_POSITION")).setCellRenderer(Renderer);
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
                    
                    if(last==1) {
                        ShowMessage("Press F1 for Position Code List...");
                    }
                    if(last==6) {
                        ShowMessage("Press F1 for Press Type List...");
                    }
                    if(last==20){
                        ShowMessage("Press F1 for Felt Type List...");
                    }
                    if(last==21){
                        ShowMessage("Press F1 for Felt Style List...");
                    }
                    if(last==36){
                        ShowMessage("Press F1 for Guide Type...");
                    }
                     if(last==52){
                        ShowMessage("Press F1 for Felt Position...");
                    }
                    
                }
            }
            );
            
            //===================================================//
            
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
        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;
        //String docno = txtmachinedocno.getText().trim();
        try {
            
            Conn = data.getConn();
            st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            //String strSQL = "SELECT C.ADDRESS1,C.ADDRESS2,A.MM_PARTY_CODE,C.PARTY_NAME,A.MM_MACHINE_NO,A.MM_MACHINE_SPEED_RANGE,A.MM_PAPER_GRADE,A.MM_PAPER_GSM_RANGE,A.MM_CAPACITY,A.MM_FURNISH,A.MM_MACHINE_TYPE_FORMING,A.MM_PAPER_DECKLE_AT_POPE_REEL,A.MM_TYPE_OF_FILLER,A.MM_MACHINE_MAKE,A.MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,A.MM_WIRE_DETAIL_3,A.MM_WIRE_DETAIL_4,B.MM_PRESS_TYPE,B.MM_MACHINE_FLOOR,B.MM_MACHINE_POSITION_DESC,B.MM_NUMBER_OF_FORMING_FABRIC,B.MM_MG_YANKEE_NIP_LOAD,B.MM_TYPE_OF_FORMING_FABRIC,B.MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH,B.MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH,B.MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH,B.MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH,B.MM_WASH_ROLL_SHOWER,B.MM_HP_SHOWER_NOZZLES,B.MM_UHLE_BOX_VACUUM,B.MM_CHEMICAL_SHOWER,B.MM_1ST_LINEAR_NIP_PRESSURE,B.MM_2ND_LINEAR_NIP_PRESSURE,B.MM_3RD_LINEAR_NIP_PRESSURE,B.MM_4TH_LINEAR_NIP_PRESSURE,B.MM_LOADING_SYSTEM,B.MM_LP_SHOWER_NOZZLES,B.MM_1ST_ROLL_MATERIAL,B.MM_2ND_ROLL_MATERIAL,B.MM_3RD_ROLL_MATERIAL,B.MM_6TH_ROLL_MATERIAL,B.MM_4TH_ROLL_MATERIAL,B.MM_5TH_ROLL_MATERIAL,B.MM_7TH_ROLL_MATERIAL,B.MM_8TH_ROLL_MATERIAL,B.MM_BATT_GSM,B.MM_FIBERS_USED,B.MM_STRETCH,B.MM_MG,B.MM_YANKEE,B.MM_MG_YANKEE_NIP_LOAD FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER A,PRODUCTION.FELT_MACHINE_MASTER_DETAIL B,DINESHMILLS.D_SAL_PARTY_MASTER C WHERE C.PARTY_CODE=A.MM_PARTY_CODE AND A.MM_DOC_NO=B.MM_DOC_NO AND A.MM_DOC_NO='"+txtmachinedocno.getText().trim()+"' AND A.APPROVED=1 AND A.CANCELED=0 ORDER BY B.SR_NO";
            String strSQL = "SELECT C.ADDRESS1,C.ADDRESS2,A.MM_PARTY_CODE,C.PARTY_NAME,A.MM_MACHINE_NO,A.MM_MACHINE_SPEED_RANGE,A.MM_PAPER_GRADE,A.MM_PAPER_GSM_RANGE,A.MM_CAPACITY,A.MM_FURNISH,A.MM_MACHINE_TYPE_FORMING,A.MM_PAPER_DECKLE_AT_POPE_REEL,A.MM_TYPE_OF_FILLER,A.MM_MACHINE_MAKE,A.MM_WIRE_DETAIL_1,MM_WIRE_DETAIL_2,A.MM_WIRE_DETAIL_3,A.MM_WIRE_DETAIL_4,B.MM_PRESS_TYPE,B.MM_MACHINE_FLOOR,B.MM_MACHINE_POSITION_DESC,B.MM_NUMBER_OF_FORMING_FABRIC,B.MM_MG_YANKEE_NIP_LOAD,B.MM_TYPE_OF_FORMING_FABRIC,B.MM_PRESS_ROLL_FOR_1ST_NIP_FACE_LENGTH,B.MM_PRESS_ROLL_FOR_2ND_NIP_FACE_LENGTH,B.MM_PRESS_ROLL_FOR_3RD_NIP_FACE_LENGTH,B.MM_PRESS_ROLL_FOR_4TH_NIP_FACE_LENGTH,B.MM_WASH_ROLL_SHOWER,B.MM_HP_SHOWER_NOZZLES,B.MM_UHLE_BOX_VACUUM,B.MM_CHEMICAL_SHOWER,B.MM_1ST_LINEAR_NIP_PRESSURE,B.MM_2ND_LINEAR_NIP_PRESSURE,B.MM_3RD_LINEAR_NIP_PRESSURE,B.MM_4TH_LINEAR_NIP_PRESSURE,B.MM_LOADING_SYSTEM,B.MM_LP_SHOWER_NOZZLES,B.MM_1ST_ROLL_MATERIAL,B.MM_2ND_ROLL_MATERIAL,B.MM_3RD_ROLL_MATERIAL,B.MM_6TH_ROLL_MATERIAL,B.MM_4TH_ROLL_MATERIAL,B.MM_5TH_ROLL_MATERIAL,B.MM_7TH_ROLL_MATERIAL,B.MM_8TH_ROLL_MATERIAL,B.MM_BATT_GSM,B.MM_FIBERS_USED,B.MM_STRETCH,B.MM_MG,B.MM_YANKEE,B.MM_MG_YANKEE_NIP_LOAD,B.MM_FELT_STYLE,B.MM_FELT_WIDTH,B.MM_FELT_LENGTH,B.MM_FELT_GSM,B.MM_STYLE_DRY,B.MM_CFM_DRY,B.MM_VACCUM_CAPACITY,B.MM_LP_SHOWER,B.MM_UHLE_BOX,B.MM_NO_DRYER_CYLINDER,B.MM_CYLINDER_DIA_MM,B.MM_SIZE_M2 FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER A,PRODUCTION.FELT_MACHINE_MASTER_DETAIL B,DINESHMILLS.D_SAL_PARTY_MASTER C WHERE C.PARTY_CODE=A.MM_PARTY_CODE AND A.MM_DOC_NO=B.MM_DOC_NO AND A.MM_DOC_NO='"+txtmachinedocno.getText().trim()+"' AND A.APPROVED=1 AND A.CANCELED=0 ORDER BY B.SR_NO";
            
            System.out.println("msg : "+ strSQL); 
            rpt.setReportName("/EITLERP/Production/FeltMachineSurvey/MachnemasterTEST27.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();
     
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        }
    }
    public void FindEx2(int pCompanyID,String pPartyCode) {
        ObjSalesParty.Filter(" MM_PARTY_CODE='"+pPartyCode+"'");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID,String pPartyCode,String pMachineNo) {
        ObjSalesParty.Filter(" MM_PARTY_CODE='"+pPartyCode+"' AND MM_MACHINE_NO='"+pMachineNo+"' ");
        ObjSalesParty.MoveFirst();
        DisplayData();
    } 
    
    public void Find(String DocNo) {
        ObjSalesParty.Filter(" MM_DOC_NO='" + DocNo + "'");
        SetMenuForRights();
        DisplayData();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}


