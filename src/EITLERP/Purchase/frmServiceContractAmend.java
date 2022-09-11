/*
 * frmTemplate.java
 *
 * Created on April 7, 2004, 3:10 PM
 */

package EITLERP.Purchase;

/**
 *
 * @author  nhpatel
 */

/*<APPLET CODE=frmPOGen.class HEIGHT=530 WIDTH=762></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Stores.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.text.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import EITLERP.Utils.*;


/**
 *
 */
public class frmServiceContractAmend extends javax.swing.JApplet {
    
    private int EditMode=0;
    
    private EITLTableModel DataModelA;
    
    private String FormTitle="";
    
    private clsPOAmendGen ObjPO;
    
    private int SelHierarchyID=0; //Selected Hierarchy
    private int lnFromID=0;
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbBuyerModel;

    public int POType=8; //Purchase Order Type
    
    private boolean HistoryView=false;
    private String theDocNo="";
    private EITLTableModel DataModelHS;
    
    public frmPendingApprovals frmPA;
    
    String cellLastValueL="";
    String cellLastValueH="";
    
    /*public frmPOGen(int pType) {
        System.gc();
        POType=pType;
        setSize(762,530);
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
     
        ObjTax.LoadData((int)EITLERPGLOBAL.gCompanyID);
        ObjColumn.LoadData((int)EITLERPGLOBAL.gCompanyID);
     
        FormatGrid();
        FormatGrid_H();
        SetNumberFormats();
     
        GenerateCombos();
        ObjPO=new clsPOGen();
     
        //As per the PO Type, Display appropriate titles
        switch(POType) {
            case 1:
                lblTitle.setText("PURCHASE ORDER (Gen.)");break;
            case 2:
                lblTitle.setText("PURCHASE ORDER (Gen. without indent)");break;
            case 3:
                lblTitle.setText("PURCHASE ORDER (A Class)");break;
            case 4:
                lblTitle.setText("PURCHASE ORDER (Raw Material)");break;
            case 5:
                lblTitle.setText("PURCHASE ORDER (Spares)");break;
            case 6:
                lblTitle.setText("PURCHASE ORDER (Capital Goods)");break;
            case 7:
                lblTitle.setText("PURCHASE ORDER (Contract)");break;
        }
     
        if(ObjPO.LoadData(EITLERPGLOBAL.gCompanyID,POType)) {
            ObjPO.MoveLast();
            DisplayData();
            SetMenuForRights();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while loading data. Error is "+ObjPO.LastError);
        }
     
        //Import Spares PO. Show the Import License No.
        if(POType==5) {
            lblImport.setVisible(true);
            txtImportLicense.setVisible(true);
        }
        else {
            lblImport.setVisible(false);
            txtImportLicense.setVisible(false);
        }
     
     
        //Specifications - Show the Specification Grid in case of A Class P.O.
        if(POType==3) {
            lblSpecs.setVisible(true);
            frameSpecs.setVisible(true);
            cmdSpecAdd.setVisible(true);
            cmdSpecRemove.setVisible(true);
        }
        else {
            lblSpecs.setVisible(false);
            frameSpecs.setVisible(false);
            cmdSpecAdd.setVisible(false);
            cmdSpecRemove.setVisible(false);
        }
     
    }*/
    
    public frmServiceContractAmend() {
        //Nothing
    }
    
    /** Creates new form frmTemplate */
    public void init() {
        
        setSize(762,530);
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
        
        
        
        GenerateCombos();
        ObjPO=new clsPOAmendGen();
        
        SetMenuForRights();
        
        if(getName().equals("Link")) {
            
        }
        else {
            if(ObjPO.LoadData(EITLERPGLOBAL.gCompanyID,POType)) {
                ObjPO.MoveLast();
                DisplayData();
                
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while loading data. Error is "+ObjPO.LastError);
            }
        }
        
        txtAuditRemarks.setVisible(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
        lblTitle = new javax.swing.JLabel();
        Tab = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPONo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPODate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSuppCode = new javax.swing.JTextField();
        txtSuppName = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbBuyer = new javax.swing.JComboBox();
        chkCancelled = new javax.swing.JCheckBox();
        jLabel41 = new javax.swing.JLabel();
        cmdNext1 = new javax.swing.JButton();
        lblRevNo = new javax.swing.JLabel();
        txtSubject = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtPremises = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtServicePeriod = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txtFrequency = new javax.swing.JTextField();
        txtNetAmount = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtReasonCode = new javax.swing.JTextField();
        txtAmendReason = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        txtInWords = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        txtDeptID = new javax.swing.JTextField();
        lblDeptName = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        Tab2 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        cmdNext2 = new javax.swing.JButton();
        cmdBack2 = new javax.swing.JButton();
        cmdRemarksBig = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtScope = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtContractDetails = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtTerminationTerms = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtPaymentTerms = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtServiceReport = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        Tab4 = new javax.swing.JPanel();
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
        cmdBack4 = new javax.swing.JButton();
        cmdFromRemarksBig = new javax.swing.JButton();
        Tab5 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        lblDocmentHistory = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableHS = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdPreviewA = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

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

        cmdFilter.setToolTipText("Find Record");
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

        cmdPrint.setToolTipText("Print Record");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });
        ToolBar.add(cmdPrint);

        cmdExit.setToolTipText("Exit Record");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        ToolBar.add(cmdExit);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText(" SERVICE CONTRACT AMENDMENT");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 804, 25);

        Tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab1FocusGained(evt);
            }
        });
        Tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tab1MouseClicked(evt);
            }
        });
        Tab1.setLayout(null);

        jLabel2.setText("P.O. No.");
        Tab1.add(jLabel2);
        jLabel2.setBounds(52, 18, 56, 15);

        txtPONo.setBackground(new java.awt.Color(204, 204, 255));
        txtPONo.setEditable(false);
        Tab1.add(txtPONo);
        txtPONo.setBounds(114, 14, 100, 19);

        jLabel3.setText("Date");
        Tab1.add(jLabel3);
        jLabel3.setBounds(254, 18, 34, 15);

        txtPODate.setName("PO_DATE"); // NOI18N
        txtPODate.setNextFocusableComponent(txtSuppCode);
        txtPODate.setEnabled(false);
        txtPODate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPODateFocusGained(evt);
            }
        });
        Tab1.add(txtPODate);
        txtPODate.setBounds(290, 16, 100, 19);

        jLabel4.setText("Supplier");
        Tab1.add(jLabel4);
        jLabel4.setBounds(56, 124, 52, 15);

        txtSuppCode.setName("SUPP_ID"); // NOI18N
        txtSuppCode.setEnabled(false);
        txtSuppCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSuppCodeActionPerformed(evt);
            }
        });
        txtSuppCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusLost(evt);
            }
        });
        txtSuppCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSuppCodeKeyPressed(evt);
            }
        });
        Tab1.add(txtSuppCode);
        txtSuppCode.setBounds(116, 120, 62, 19);

        txtSuppName.setEditable(false);
        Tab1.add(txtSuppName);
        txtSuppName.setBounds(180, 120, 212, 19);

        jLabel11.setText("Buyer");
        Tab1.add(jLabel11);
        jLabel11.setBounds(68, 155, 42, 16);

        cmbBuyer.setName("BUYER"); // NOI18N
        cmbBuyer.setEnabled(false);
        cmbBuyer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbBuyerFocusGained(evt);
            }
        });
        Tab1.add(cmbBuyer);
        cmbBuyer.setBounds(118, 151, 180, 24);

        chkCancelled.setText("Cancelled");
        chkCancelled.setEnabled(false);
        chkCancelled.setNextFocusableComponent(txtRemarks);
        chkCancelled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCancelledActionPerformed(evt);
            }
        });
        chkCancelled.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkCancelledFocusGained(evt);
            }
        });
        Tab1.add(chkCancelled);
        chkCancelled.setBounds(657, 6, 94, 23);

        jLabel41.setText("Subject");
        Tab1.add(jLabel41);
        jLabel41.setBounds(58, 188, 52, 15);

        cmdNext1.setText("Next >>");
        cmdNext1.setNextFocusableComponent(txtPODate);
        cmdNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNext1);
        cmdNext1.setBounds(638, 348, 102, 25);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(215, 49, 35, 15);

        txtSubject.setEnabled(false);
        Tab1.add(txtSubject);
        txtSubject.setBounds(116, 186, 549, 21);

        jLabel43.setText("Premises");
        Tab1.add(jLabel43);
        jLabel43.setBounds(41, 216, 72, 15);

        txtPremises.setEnabled(false);
        Tab1.add(txtPremises);
        txtPremises.setBounds(116, 213, 549, 21);

        jLabel44.setText("Service Period");
        Tab1.add(jLabel44);
        jLabel44.setBounds(19, 244, 97, 15);

        txtServicePeriod.setEnabled(false);
        Tab1.add(txtServicePeriod);
        txtServicePeriod.setBounds(116, 241, 549, 21);

        jLabel45.setText("Frequency");
        Tab1.add(jLabel45);
        jLabel45.setBounds(45, 272, 72, 15);

        txtFrequency.setEnabled(false);
        Tab1.add(txtFrequency);
        txtFrequency.setBounds(116, 269, 549, 21);

        txtNetAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtNetAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNetAmount.setNextFocusableComponent(txtInWords);
        txtNetAmount.setEnabled(false);
        Tab1.add(txtNetAmount);
        txtNetAmount.setBounds(116, 304, 140, 22);

        jLabel46.setText("Contract Value");
        Tab1.add(jLabel46);
        jLabel46.setBounds(21, 307, 99, 15);

        jLabel47.setText("Amendment No.");
        Tab1.add(jLabel47);
        jLabel47.setBounds(6, 52, 104, 15);

        txtDocNo.setBackground(new java.awt.Color(204, 204, 255));
        txtDocNo.setEditable(false);
        Tab1.add(txtDocNo);
        txtDocNo.setBounds(114, 48, 100, 19);

        jLabel48.setText("Date");
        Tab1.add(jLabel48);
        jLabel48.setBounds(254, 52, 34, 15);

        txtDocDate.setName("AMEND_DATE"); // NOI18N
        txtDocDate.setEnabled(false);
        Tab1.add(txtDocDate);
        txtDocDate.setBounds(290, 50, 100, 19);

        jLabel49.setText("Amend. Reason");
        Tab1.add(jLabel49);
        jLabel49.setBounds(7, 87, 106, 15);

        txtReasonCode.setName("REASON_CODE"); // NOI18N
        txtReasonCode.setEnabled(false);
        txtReasonCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReasonCodeFocusLost(evt);
            }
        });
        txtReasonCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtReasonCodeKeyPressed(evt);
            }
        });
        Tab1.add(txtReasonCode);
        txtReasonCode.setBounds(117, 85, 40, 19);

        txtAmendReason.setEnabled(false);
        Tab1.add(txtAmendReason);
        txtAmendReason.setBounds(161, 85, 232, 19);

        jLabel50.setText("IN WORDS");
        Tab1.add(jLabel50);
        jLabel50.setBounds(47, 334, 72, 15);

        txtInWords.setNextFocusableComponent(cmdNext1);
        txtInWords.setEnabled(false);
        Tab1.add(txtInWords);
        txtInWords.setBounds(116, 331, 462, 21);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(667, 185, 40, 21);

        jButton2.setText("jButton1");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Tab1.add(jButton2);
        jButton2.setBounds(667, 213, 40, 21);

        jButton3.setText("jButton1");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        Tab1.add(jButton3);
        jButton3.setBounds(668, 242, 40, 21);

        jButton4.setText("jButton1");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        Tab1.add(jButton4);
        jButton4.setBounds(669, 269, 40, 21);

        jButton5.setText("jButton1");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        Tab1.add(jButton5);
        jButton5.setBounds(582, 331, 40, 21);

        txtDeptID.setEnabled(false);
        txtDeptID.setName("SUPP_ID"); // NOI18N
        txtDeptID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDeptIDActionPerformed(evt);
            }
        });
        txtDeptID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDeptIDFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDeptIDFocusLost(evt);
            }
        });
        txtDeptID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDeptIDKeyPressed(evt);
            }
        });
        Tab1.add(txtDeptID);
        txtDeptID.setBounds(500, 140, 60, 19);

        lblDeptName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Tab1.add(lblDeptName);
        lblDeptName.setBounds(570, 140, 170, 20);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel60.setText("Dept :");
        Tab1.add(jLabel60);
        jLabel60.setBounds(440, 140, 50, 20);

        Tab.addTab("Header ", Tab1);

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab2FocusGained(evt);
            }
        });
        Tab2.setLayout(null);

        jLabel39.setText("Remarks");
        Tab2.add(jLabel39);
        jLabel39.setBounds(6, 322, 58, 15);

        txtRemarks.setName("REMARKS"); // NOI18N
        txtRemarks.setNextFocusableComponent(cmdNext2);
        txtRemarks.setEnabled(false);
        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtRemarks);
        txtRemarks.setBounds(66, 320, 464, 19);

        cmdNext2.setText("Next >>");
        cmdNext2.setNextFocusableComponent(cmdBack2);
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNext2);
        cmdNext2.setBounds(638, 348, 102, 25);

        cmdBack2.setText("<< Back");
        cmdBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBack2);
        cmdBack2.setBounds(532, 348, 102, 25);

        cmdRemarksBig.setText("...");
        cmdRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdRemarksBig);
        cmdRemarksBig.setBounds(539, 319, 33, 19);

        jLabel1.setText("Scope of Work");
        Tab2.add(jLabel1);
        jLabel1.setBounds(16, 15, 92, 15);

        txtScope.setLineWrap(true);
        txtScope.setWrapStyleWord(true);
        txtScope.setEnabled(false);
        jScrollPane1.setViewportView(txtScope);

        Tab2.add(jScrollPane1);
        jScrollPane1.setBounds(13, 39, 715, 252);

        Tab.addTab("Other Information", Tab2);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(null);

        jLabel5.setText("Contract Details");
        jPanel3.add(jLabel5);
        jLabel5.setBounds(9, 10, 131, 15);

        txtContractDetails.setEnabled(false);
        jScrollPane3.setViewportView(txtContractDetails);

        jPanel3.add(jScrollPane3);
        jScrollPane3.setBounds(8, 32, 732, 343);

        Tab.addTab("Contract Details", jPanel3);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(null);

        jLabel16.setText("Payment Terms");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(8, 14, 96, 15);

        txtTerminationTerms.setEnabled(false);
        jScrollPane4.setViewportView(txtTerminationTerms);

        jPanel2.add(jScrollPane4);
        jScrollPane4.setBounds(13, 312, 711, 62);

        txtPaymentTerms.setEnabled(false);
        jScrollPane5.setViewportView(txtPaymentTerms);

        jPanel2.add(jScrollPane5);
        jScrollPane5.setBounds(10, 32, 711, 105);

        jLabel17.setText("Payment Terms");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(8, 14, 96, 15);

        jLabel18.setText("Service Report");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(10, 154, 96, 15);

        txtServiceReport.setEnabled(false);
        jScrollPane7.setViewportView(txtServiceReport);

        jPanel2.add(jScrollPane7);
        jScrollPane7.setBounds(9, 176, 711, 89);

        jLabel20.setText("Contract Termination Terms");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(11, 293, 177, 15);

        Tab.addTab("Terms & Conditions", jPanel2);

        Tab4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab4FocusGained(evt);
            }
        });
        Tab4.setLayout(null);

        jLabel31.setText("Hierarchy ");
        Tab4.add(jLabel31);
        jLabel31.setBounds(16, 18, 66, 15);

        cmbHierarchy.setNextFocusableComponent(OpgApprove);
        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbHierarchyFocusGained(evt);
            }
        });
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        Tab4.add(cmbHierarchy);
        cmbHierarchy.setBounds(86, 14, 184, 24);

        jLabel32.setText("From");
        Tab4.add(jLabel32);
        jLabel32.setBounds(20, 52, 56, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        Tab4.add(txtFrom);
        txtFrom.setBounds(86, 50, 182, 19);

        jLabel35.setText("Remarks");
        Tab4.add(jLabel35);
        jLabel35.setBounds(20, 82, 62, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setEnabled(false);
        Tab4.add(txtFromRemarks);
        txtFromRemarks.setBounds(86, 78, 518, 19);

        jLabel36.setText("Your Action  ");
        Tab4.add(jLabel36);
        jLabel36.setBounds(8, 124, 76, 15);

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
        OpgApprove.setBounds(6, 6, 169, 23);

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
        OpgFinal.setBounds(6, 32, 136, 20);

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
        OpgReject.setBounds(6, 54, 136, 20);

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
        OpgHold.setBounds(6, 76, 136, 20);

        Tab4.add(jPanel6);
        jPanel6.setBounds(88, 120, 182, 100);

        jLabel33.setText("Send To");
        Tab4.add(jLabel33);
        jLabel33.setBounds(18, 228, 60, 15);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab4.add(cmbSendTo);
        cmbSendTo.setBounds(84, 224, 184, 24);

        jLabel34.setText("Remarks");
        Tab4.add(jLabel34);
        jLabel34.setBounds(16, 264, 60, 15);

        txtToRemarks.setNextFocusableComponent(cmdBack4);
        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab4.add(txtToRemarks);
        txtToRemarks.setBounds(84, 260, 516, 19);

        cmdBack4.setText("<< Back");
        cmdBack4.setNextFocusableComponent(cmbHierarchy);
        cmdBack4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack4ActionPerformed(evt);
            }
        });
        Tab4.add(cmdBack4);
        cmdBack4.setBounds(638, 348, 102, 25);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab4.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(610, 78, 33, 19);

        Tab.addTab("Approval", Tab4);

        Tab5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab5.setLayout(null);

        jLabel42.setText("Document Approval Status");
        Tab5.add(jLabel42);
        jLabel42.setBounds(12, 10, 242, 15);

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

        Tab5.add(jScrollPane2);
        jScrollPane2.setBounds(12, 40, 648, 144);

        lblDocmentHistory.setText("Document Update History");
        Tab5.add(lblDocmentHistory);
        lblDocmentHistory.setBounds(13, 190, 182, 15);

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

        Tab5.add(jScrollPane6);
        jScrollPane6.setBounds(13, 206, 500, 148);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        Tab5.add(cmdViewHistory);
        cmdViewHistory.setBounds(525, 236, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        Tab5.add(cmdNormalView);
        cmdNormalView.setBounds(525, 266, 132, 24);

        cmdPreviewA.setText("Preview Report");
        cmdPreviewA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewAActionPerformed(evt);
            }
        });
        Tab5.add(cmdPreviewA);
        cmdPreviewA.setBounds(525, 206, 131, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        Tab5.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(525, 296, 132, 24);

        txtAuditRemarks.setEnabled(false);
        Tab5.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(528, 338, 129, 19);

        Tab.addTab("Status", Tab5);

        getContentPane().add(Tab);
        Tab.setBounds(4, 68, 752, 406);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(4, 474, 752, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if(TableHS.getRowCount()>0&&TableHS.getSelectedRow()>=0)
        {
          txtAuditRemarks.setText((String)TableHS.getValueAt(TableHS.getSelectedRow(),4));
          BigEdit bigEdit=new BigEdit();
          bigEdit.theText=txtAuditRemarks;
          bigEdit.ShowEdit();
        }
        
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtInWords;
        bigEdit.ShowEdit();
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtFrequency;
        bigEdit.ShowEdit();
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtServicePeriod;
        bigEdit.ShowEdit();
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtPremises;
        bigEdit.ShowEdit();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtSubject;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtReasonCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReasonCodeFocusLost
        // TODO add your handling code here:
        if(!txtReasonCode.getText().trim().equals("")) {
            if(txtAmendReason.getText().trim().equals("")) {
                txtAmendReason.setText(clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"AMEND_REASON",Integer.parseInt(txtReasonCode.getText())));
            }
        }
        
    }//GEN-LAST:event_txtReasonCodeFocusLost

    private void txtReasonCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReasonCodeKeyPressed
        // TODO add your handling code here:
  if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND  PARA_ID='AMEND_REASON' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtReasonCode.setText(aList.ReturnVal);
                txtAmendReason.setText(clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"AMEND_REASON",Integer.parseInt(aList.ReturnVal)));
            }
        }
                
    }//GEN-LAST:event_txtReasonCodeKeyPressed
                    
    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtFromRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed
    
    private void cmdRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemarksBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtRemarks;
        bigEdit.ShowEdit();
        
    }//GEN-LAST:event_cmdRemarksBigActionPerformed
                                                                            
    private void cmdPreviewAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewAActionPerformed
        // TODO add your handling code here:
        PreviewAuditReport();
    }//GEN-LAST:event_cmdPreviewAActionPerformed
            
    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjPO.LoadData(EITLERPGLOBAL.gCompanyID,POType);
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed
    
    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo=txtDocNo.getText();
        ObjPO.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed
                                    
    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter remarks for next user");
    }//GEN-LAST:event_txtToRemarksFocusGained
    
    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        // TODO add your handling code here:
        ShowMessage("Select user to whom document to be forwarded");
    }//GEN-LAST:event_cmbSendToFocusGained
    
    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained
        // TODO add your handling code here:
        ShowMessage("Select approval action");
    }//GEN-LAST:event_OpgHoldFocusGained
    
    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained
        // TODO add your handling code here:
        ShowMessage("Select approval action");
    }//GEN-LAST:event_OpgRejectFocusGained
    
    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained
        // TODO add your handling code here:
        ShowMessage("Select approval action");
    }//GEN-LAST:event_OpgFinalFocusGained
    
    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        // TODO add your handling code here:
        ShowMessage("Select approval action");
    }//GEN-LAST:event_OpgApproveFocusGained
    
    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained
                
    private void txtRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter remarks for this document");
    }//GEN-LAST:event_txtRemarksFocusGained
                    
    private void chkCancelledFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkCancelledFocusGained
        // TODO add your handling code here:
        ShowMessage("Shows cancel status of this document");
    }//GEN-LAST:event_chkCancelledFocusGained
                            
    private void cmbBuyerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbBuyerFocusGained
        // TODO add your handling code here:
        ShowMessage("Select buyer");
    }//GEN-LAST:event_cmbBuyerFocusGained
                    
    private void txtSuppCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter supplier id. Press F1 for the list of suppliers");
    }//GEN-LAST:event_txtSuppCodeFocusGained
    
    private void txtPODateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPODateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter PO date in DD/MM/YYYY");
    }//GEN-LAST:event_txtPODateFocusGained
    
    private void Tab4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab4FocusGained
        // TODO add your handling code here:
        cmbHierarchy.requestFocus();
    }//GEN-LAST:event_Tab4FocusGained
        
    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_Tab2FocusGained
    
    private void Tab1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab1FocusGained
        // TODO add your handling code here:
        txtDocDate.requestFocus();
    }//GEN-LAST:event_Tab1FocusGained
    
    private void cmdBack4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack4ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdBack4ActionPerformed
            
    private void cmdBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack2ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(0);
    }//GEN-LAST:event_cmdBack2ActionPerformed
    
    private void cmdNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext2ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext2ActionPerformed
    
    private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext1ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext1ActionPerformed
    
    private void Tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tab1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Tab1MouseClicked
        
    private void chkCancelledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCancelledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCancelledActionPerformed
                
    private void txtSuppCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSuppCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSuppCodeActionPerformed
    
    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);
        OpgHold.setSelected(true);
    }//GEN-LAST:event_OpgHoldMouseClicked
    
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
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(true);
        OpgReject.setSelected(false);
        OpgHold.setSelected(false);
        
        if(!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked
    
    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(true);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);
        OpgHold.setSelected(false);
        
        SetupApproval();
    }//GEN-LAST:event_OpgApproveMouseClicked
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ObjPO.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed
    
    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed
    
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed
    
    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed
    
    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed
    
    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed
    
    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed
    
    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed
    
    private void txtSuppCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppCodeKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND BLOCKED='N' AND APPROVED=1 AND ST35_REGISTERED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtSuppCode.setText(aList.ReturnVal);
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
        //=========================================
        
    }//GEN-LAST:event_txtSuppCodeKeyPressed
    
    private void txtSuppCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusLost
        // TODO add your handling code here:
        if(!txtSuppCode.getText().trim().equals("")) {
            txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
            
            clsSupplier tmpObj=new clsSupplier();
            tmpObj.LoadData(EITLERPGLOBAL.gCompanyID);
            
            clsSupplier ObjSupp=(clsSupplier)tmpObj.getObject(EITLERPGLOBAL.gCompanyID,txtSuppCode.getText());
            
            for(int i=1;i<=ObjSupp.colSuppTerms.size();i++) {
                //Insert New Row
                Object[] rowData=new Object[3];
                clsSuppTerms ObjItem=(clsSuppTerms)ObjSupp.colSuppTerms.get(Integer.toString(i));
                String TermType=(String)ObjItem.getAttribute("TERM_TYPE").getObj();
                
                if(TermType.equals("P")) {
                    txtPaymentTerms.setText((String)ObjItem.getAttribute("TERM_DESC").getObj());
                }
            }
            
        }
    }//GEN-LAST:event_txtSuppCodeFocusLost
                        
    
    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        // TODO add your handling code here:
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            cmbSendTo.setEnabled(false);
        }
        
        if(clsHierarchy.CanFinalApprove((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gUserID)) {
            OpgFinal.setEnabled(true);
        }
        else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
        
        //Set Default Send to User
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void txtDeptIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDeptIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeptIDActionPerformed

    private void txtDeptIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDeptIDFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeptIDFocusGained

    private void txtDeptIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDeptIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeptIDFocusLost

    private void txtDeptIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeptIDKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT DEPT_ID,DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DEPT_DESC NOT LIKE 'PLEASE%' ORDER BY DEPT_DESC";
            aList.ReturnCol = 1;
            aList.SecondCol = 2;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtDeptID.setText(aList.ReturnVal);
                lblDeptName.setText(aList.SecondVal);

            }
        }
    }//GEN-LAST:event_txtDeptIDKeyPressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab4;
    private javax.swing.JPanel Tab5;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkCancelled;
    private javax.swing.JComboBox cmbBuyer;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBack2;
    private javax.swing.JButton cmdBack4;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFromRemarksBig;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext1;
    private javax.swing.JButton cmdNext2;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewA;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemarksBig;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lblDeptName;
    private javax.swing.JLabel lblDocmentHistory;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAmendReason;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextArea txtContractDetails;
    private javax.swing.JTextField txtDeptID;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtFrequency;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtInWords;
    private javax.swing.JTextField txtNetAmount;
    private javax.swing.JTextField txtPODate;
    private javax.swing.JTextField txtPONo;
    private javax.swing.JTextArea txtPaymentTerms;
    private javax.swing.JTextField txtPremises;
    private javax.swing.JTextField txtReasonCode;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextArea txtScope;
    private javax.swing.JTextField txtServicePeriod;
    private javax.swing.JTextArea txtServiceReport;
    private javax.swing.JTextField txtSubject;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSuppName;
    private javax.swing.JTextArea txtTerminationTerms;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables
    
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
    
    //Didplay data on the Screen
    private void DisplayData() {
        
        //=========== Color Indication ===============//
        try
        {
        if(EditMode==0)
        {
           if(ObjPO.getAttribute("APPROVED").getInt()==1)
           {
            lblTitle.setBackground(Color.BLUE);   
           }
           
           if(ObjPO.getAttribute("APPROVED").getInt()!=1)
           {
            lblTitle.setBackground(Color.GRAY);   
           }
           
           if(ObjPO.getAttribute("CANCELLED").getInt()==1)
           {
            lblTitle.setBackground(Color.RED);   
           }

            
        }
        }
        catch(Exception c)
        {
            
        }
        //============================================//
        
        try {
            ClearFields();
            txtDocNo.setText((String)ObjPO.getAttribute("AMEND_NO").getObj());
            txtDocDate.setText(EITLERPGLOBAL.formatDate((String)ObjPO.getAttribute("AMEND_DATE").getObj()));

            txtPONo.setText((String)ObjPO.getAttribute("PO_NO").getObj());
            txtPODate.setText(EITLERPGLOBAL.formatDate((String)ObjPO.getAttribute("PO_DATE").getObj()));
            
            
            txtAmendReason.setText((String)ObjPO.getAttribute("AMEND_REASON").getObj());
            txtReasonCode.setText(Integer.toString((int)ObjPO.getAttribute("REASON_CODE").getVal()));
            
            lblTitle.setText("SERVICE CONTRACT AMENDMENT "+" - "+txtDocNo.getText());
            
            
            lblRevNo.setText(Integer.toString((int)ObjPO.getAttribute("REVISION_NO").getVal()));
            
            txtSuppCode.setText((String)ObjPO.getAttribute("SUPP_ID").getObj());
            
                if(!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, (String)ObjPO.getAttribute("SUPP_ID").getObj()))
                {
                txtSuppName.setText((String)ObjPO.getAttribute("SUPP_NAME").getObj());
                }
                else
                {
                  txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));  
                }
            
            txtDeptID.setText(Integer.toString(ObjPO.getAttribute("DEPT_ID").getInt())); 
            lblDeptName.setText(clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, ObjPO.getAttribute("DEPT_ID").getInt()));
            
                
            EITLERPGLOBAL.setComboIndex(cmbBuyer,(int)ObjPO.getAttribute("BUYER").getVal());
            txtSubject.setText((String)ObjPO.getAttribute("SUBJECT").getObj());
            txtRemarks.setText((String)ObjPO.getAttribute("REMARKS").getObj());            
            chkCancelled.setSelected(ObjPO.getAttribute("CANCELLED").getBool());
            
            txtPremises.setText((String)ObjPO.getAttribute("PREMISES_TYPE").getObj());
            txtServicePeriod.setText((String)ObjPO.getAttribute("SERVICE_PERIOD").getObj());
            txtFrequency.setText((String)ObjPO.getAttribute("SERVICE_FREQUENCY").getObj());
            txtNetAmount.setText(Double.toString(ObjPO.getAttribute("NET_AMOUNT").getVal()));
            txtScope.setText((String)ObjPO.getAttribute("SCOPE").getObj());
            txtContractDetails.setText((String)ObjPO.getAttribute("CONTRACT_DETAILS").getObj());
            txtPaymentTerms.setText((String)ObjPO.getAttribute("PAYMENT_TERM").getObj());
            txtServiceReport.setText((String)ObjPO.getAttribute("SERVICE_REPORT").getObj());
            txtInWords.setText((String)ObjPO.getAttribute("AMOUNT_IN_WORDS").getObj());
            txtTerminationTerms.setText((String)ObjPO.getAttribute("TERMINATION_TERMS").getObj());
            
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,(int)ObjPO.getAttribute("HIERARCHY_ID").getVal());
            
            
            
            
            //=========================================//
            
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List=new HashMap();
            String DocNo=(String)ObjPO.getAttribute("AMEND_NO").getObj();
            List=ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, (47), DocNo);
            for(int i=1;i<=List.size();i++) {
                clsDocFlow ObjFlow=(clsDocFlow)List.get(Integer.toString(i));
                Object[] rowData=new Object[7];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(int)ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2]=(String)ObjFlow.getAttribute("STATUS").getObj();
                rowData[3]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int)ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[4]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6]=(String)ObjFlow.getAttribute("REMARKS").getObj();
                
                DataModelA.addRow(rowData);
            }
            
            
            //Showing Audit Trial History
            FormatGridHS();            
            HashMap History=clsPOAmendGen.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo,POType);
            for(int i=1;i<=History.size();i++) {
                clsPOAmendGen ObjHistory=(clsPOAmendGen)History.get(Integer.toString(i));
                
                Object[] rowData=new Object[5];
                
                rowData[0]=Integer.toString((int)ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2]=EITLERPGLOBAL.formatDate((String)ObjHistory.getAttribute("ENTRY_DATE").getObj());
                
                String ApprovalStatus="";
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("A")) {
                    ApprovalStatus="Approved";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("F")) {
                    ApprovalStatus="Final Approved";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("W")) {
                    ApprovalStatus="Waiting";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("R")) {
                    ApprovalStatus="Rejected";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("P")) {
                    ApprovalStatus="Pending";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("C")) {
                    ApprovalStatus="Skiped";
                }
                
                
                rowData[3]=ApprovalStatus;
                rowData[4]=(String)ObjHistory.getAttribute("APPROVER_REMARKS").getObj();
                
                DataModelHS.addRow(rowData);
            }
            //============================================================//
            
            
            ShowMessage("Ready");
        }
        catch(Exception e) {
            
        }
    }
    
    
    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjPO.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjPO.setAttribute("PREFIX",SelPrefix);
        ObjPO.setAttribute("SUFFIX",SelSuffix);
        ObjPO.setAttribute("AMEND_DATE",EITLERPGLOBAL.formatDateDB(txtDocDate.getText()));
        
        ObjPO.setAttribute("REASON_CODE",Integer.parseInt(txtReasonCode.getText()));
        ObjPO.setAttribute("AMEND_REASON",txtAmendReason.getText());
        
        ObjPO.setAttribute("PO_NO",txtPONo.getText());
        ObjPO.setAttribute("PO_DATE",EITLERPGLOBAL.formatDateDB(txtPODate.getText()));
        
        ObjPO.setAttribute("SUPP_ID",txtSuppCode.getText());
        ObjPO.setAttribute("SUPP_NAME",txtSuppName.getText());
        ObjPO.setAttribute("BUYER",EITLERPGLOBAL.getComboCode(cmbBuyer));
        ObjPO.setAttribute("SUBJECT",txtSubject.getText());
        ObjPO.setAttribute("TOTAL_AMOUNT",EITLERPGLOBAL.round(Double.parseDouble(txtNetAmount.getText()),5));
        ObjPO.setAttribute("NET_AMOUNT",EITLERPGLOBAL.round(Double.parseDouble(txtNetAmount.getText()),5));
        ObjPO.setAttribute("REMARKS",txtRemarks.getText());
        
        ObjPO.setAttribute("DEPT_ID", txtDeptID.getText());
        
        ObjPO.setAttribute("PREMISES_TYPE",txtPremises.getText());
        ObjPO.setAttribute("SERVICE_PERIOD",txtServicePeriod.getText());
        ObjPO.setAttribute("SERVICE_FREQUENCY",txtFrequency.getText());
        ObjPO.setAttribute("SCOPE",txtScope.getText());
        ObjPO.setAttribute("CONTRACT_DETAILS",txtContractDetails.getText());
        ObjPO.setAttribute("PAYMENT_TERM",txtPaymentTerms.getText());
        ObjPO.setAttribute("SERVICE_REPORT",txtServiceReport.getText());
        ObjPO.setAttribute("AMOUNT_IN_WORDS",txtInWords.getText());
        ObjPO.setAttribute("TERMINATION_TERMS",txtTerminationTerms.getText());
        ObjPO.setAttribute("SERVICE_TAX_TERM","");
        ObjPO.setAttribute("IMPORTED",0);
        
        if(chkCancelled.isSelected()) {
            ObjPO.setAttribute("CANCELLED",true);
        }
        else {
            ObjPO.setAttribute("CANCELLED",false);
        }
        
        
        
        ObjPO.setAttribute("PO_TYPE",POType); //Fixed type 2 - Raw Material, 1 - General
        
        //----- Update Approval Specific Fields -----------//
        ObjPO.setAttribute("HIERARCHY_ID",EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjPO.setAttribute("FROM",EITLERPGLOBAL.gUserID);
        ObjPO.setAttribute("TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjPO.setAttribute("FROM_REMARKS",txtToRemarks.getText());
        
        if(OpgApprove.isSelected()) {
            ObjPO.setAttribute("APPROVAL_STATUS","A");
        }
        
        if(OpgFinal.isSelected()) {
            ObjPO.setAttribute("APPROVAL_STATUS","F");
        }
        
        if(OpgReject.isSelected()) {
            ObjPO.setAttribute("APPROVAL_STATUS","R");
            ObjPO.setAttribute("SEND_DOC_TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        }
        
        if(OpgHold.isSelected()) {
            ObjPO.setAttribute("APPROVAL_STATUS","H");
        }
        //-------------------------------------------------//
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            ObjPO.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
            ObjPO.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else {
            ObjPO.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
            ObjPO.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        
        
        
        //=================== Setting up Line Items ==================//
        ObjPO.colPOItems.clear();
        //======================Completed ===========================//
        

        
    }
    
    
    
        
        
    
    
    
        
    
        
        
        
        
        
        
        
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        clsUser ObjUser=new clsUser();
        
        //----- Generate cmbType ------- //
        cmbHierarchyModel=new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+(47));
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+(47));
        }
        
        for(int i=1;i<=List.size();i++) {
            clsHierarchy ObjHierarchy=(clsHierarchy) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text=(String)ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //
        
        
            
        //-------- Generating Buyer Combo --------//
        cmbBuyerModel=new EITLComboModel();
        cmbBuyer.removeAllItems();
        cmbBuyer.setModel(cmbBuyerModel);
        
        List=ObjUser.getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID));
        for(int i=1;i<=List.size();i++) {
            ObjUser=(clsUser) List.get(Integer.toString(i));
            
            ComboData aData=new ComboData();
            
            aData.Text=(String) ObjUser.getAttribute("USER_NAME").getObj();
            aData.Code=(long)ObjUser.getAttribute("USER_ID").getVal();
            
            cmbBuyerModel.addElement(aData);
        }
        //----------------------------------------//
        
            
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
                
                List=clsHierarchy.getUserList((int)EITLERPGLOBAL.gCompanyID,SelHierarchyID,EITLERPGLOBAL.gUserID);
                for(int i=1;i<=List.size();i++) {
                    clsUser ObjUser=(clsUser) List.get(Integer.toString(i));
                    ComboData aData=new ComboData();
                    aData.Code=(int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
                    
                    if(ObjUser.getAttribute("USER_ID").getVal()==EITLERPGLOBAL.gUserID) {
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
                
                List=ApprovalFlow.getRemainingUsers((int)EITLERPGLOBAL.gCompanyID, (47),(String)ObjPO.getAttribute("AMEND_NO").getObj());
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
    
    private void SetupApproval() {
        // --- Hierarchy Change Rights Check --------
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,865)) {
            cmbHierarchy.setEnabled(true);
        }
        else {
            cmbHierarchy.setEnabled(false);
        }*/
        if(EditMode==EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
        }
        else
        {
            cmbHierarchy.setEnabled(false);
        }
        
        //Set Default Hierarchy ID for User
        int DefaultID=clsHierarchy.getDefaultHierarchy((int)EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy,DefaultID);
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            lnFromID=(int)EITLERPGLOBAL.gUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID));
            txtFromRemarks.setText("Creator of Document");
        }
        else {
            
            int FromUserID=ApprovalFlow.getFromID((int)EITLERPGLOBAL.gCompanyID, (47),(String)ObjPO.getAttribute("AMEND_NO").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=ApprovalFlow.getFromRemarks( (int)EITLERPGLOBAL.gCompanyID,(47),FromUserID,(String)ObjPO.getAttribute("AMEND_NO").getObj());
            
            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }
        
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            cmbSendTo.setEnabled(false);
        }
        
        if(clsHierarchy.CanFinalApprove((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gUserID)) {
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
            cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }
        
    }
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,861)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,((POType+33)*10)+2)) {
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }*/
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,863)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,864)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
        
    }
    
    
        
    private void ClearFields() {
        txtDocDate.setText("");
        txtSuppCode.setText("");
        txtSuppName.setText("");
        txtSubject.setText("");
        txtRemarks.setText("");
        txtDeptID.setText("");
        lblDeptName.setText("");
        
        
        txtPONo.setText("");
        txtPODate.setText("");
                
        chkCancelled.setSelected(false);
        
        FormatGridA();
        FormatGridHS();
        
        txtNetAmount.setText("0.00");
        txtPremises.setText("");
        txtServicePeriod.setText("");
        txtFrequency.setText("");
        txtScope.setText("");
        txtContractDetails.setText("");
        txtPaymentTerms.setText("");
        txtServiceReport.setText("");
        txtInWords.setText("");
        txtTerminationTerms.setText("");
        txtToRemarks.setText("");
    }
    
    private void SetFields(boolean pStat) {
        txtDocDate.setEnabled(pStat);
        
        //txtSuppCode.setEnabled(pStat);
        //txtSuppName.setEnabled(pStat);
        
        txtSubject.setEnabled(pStat);
        txtRemarks.setEnabled(pStat);
        txtPaymentTerms.setEnabled(pStat);
        txtReasonCode.setEnabled(pStat);
        txtAmendReason.setEnabled(pStat);
        txtDeptID.setEnabled(pStat);
        chkCancelled.setEnabled(pStat);
        cmbBuyer.setEnabled(pStat);
        
        
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        
        
        SetupApproval();

        txtNetAmount.setEnabled(pStat);
        txtPremises.setEnabled(pStat);
        txtServicePeriod.setEnabled(pStat);
        txtFrequency.setEnabled(pStat);
        txtScope.setEnabled(pStat);
        txtContractDetails.setEnabled(pStat);
        txtPaymentTerms.setEnabled(pStat);
        txtServiceReport.setEnabled(pStat);
        txtInWords.setEnabled(pStat);
        txtTerminationTerms.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        
    }
    
    private boolean Validate() {
        
        if(!EITLERPGLOBAL.IsNumber(txtNetAmount.getText())) {
            txtNetAmount.setText("0");
        }
        
        return ValidateGen();
        
    }
    
    private boolean ValidateGen() {
        //Now Header level validations
        if(txtSubject.getText().trim().equals(""))
        {
          JOptionPane.showMessageDialog(null,"Please enter subject");
          return false;
        }
        
        if(txtContractDetails.getText().trim().equals(""))
        {
           JOptionPane.showMessageDialog(null,"Please enter contract details");
           return false;
        }
        
        if(txtPaymentTerms.getText().trim().equals(""))
        {
          JOptionPane.showMessageDialog(null,"Please enter payment terms" );
          return false;
        }
        
        
        if(txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter P.O. Date");
            return false;
        }
        
        if(cmbHierarchy.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(null,"Please select the hierarchy.");
            return false;
        }
        
        if((!OpgApprove.isSelected())&&(!OpgReject.isSelected())&&(!OpgFinal.isSelected())&&(!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null,"Please select the Approval Action");
            return false;
        }
        
        if(!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID,txtSuppCode.getText())) {
            JOptionPane.showMessageDialog(null,"Please enter valid supplier code");
            return false;
        }
        
        if(!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid P.O. date");
            return false;
        }
        
        if(txtReasonCode.getText().trim().equals(""))
        {
          JOptionPane.showMessageDialog(null,"Please enter Reason Code 0 ");
          return false;
        }
        return true;
    }
    
    
        
    
        
    
        
    
        
    
        
    
        
    
    
    
        private void Add() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        
        String PONo="";
        LOV aList=new LOV();
        
        aList.SQL="SELECT PO_NO,DATE_FORMAT(PO_DATE,'%d/%m/%Y') AS PO_DATE from D_PUR_PO_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND PO_TYPE="+POType;
        aList.ReturnCol=1;
        aList.ShowReturnCol=true;
        aList.DefaultSearchOn=1;
        
        if(aList.ShowLOV()) {
            PONo=aList.ReturnVal;
            EditMode=EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SetupApproval();
            
            //Display newly generated document no.
            clsPOGen thePO=new clsPOGen();
            thePO.POType=POType;
            
            int MaxNo=clsPOAmendGen.getMaxAmendSrNo(EITLERPGLOBAL.gCompanyID,PONo, POType)+1;
            
            clsPOGen ObjNewPO=(clsPOGen) thePO.getObject(EITLERPGLOBAL.gCompanyID, PONo,POType);
            
            DisplayPOData(ObjNewPO);
            
            txtDocNo.setText(PONo+"/"+MaxNo);
            txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());
            
            txtDocDate.requestFocus();
            //
            
            lblTitle.setText("SERVICE CONTRACT AMENDMENT "+" - "+txtDocNo.getText());
            lblTitle.setBackground(Color.BLUE);
        }
        else {
            JOptionPane.showMessageDialog(null,"You must select P.O. number.");
        }
        
    }
    

    
    private void Edit() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        //(47)
        
        String lDocNo=(String)ObjPO.getAttribute("AMEND_NO").getObj();
        if(ObjPO.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gUserID)) {
            EITLERPGLOBAL.ChangeCursorToWait(this);
            EditMode=EITLERPGLOBAL.EDIT;
            
            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//
            
            if(ApprovalFlow.IsCreator((47),lDocNo)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,862)) {
                SetFields(true);
            }
            else {
                EnableApproval();
            }
            
            DisableToolbar();
            OpgHold.setSelected(true);
            txtDocDate.requestFocus();
            EITLERPGLOBAL.ChangeCursorToDefault(this);
        }
        else {
            JOptionPane.showMessageDialog(null,"You cannot edit this record. It is either approved/rejected or waiting approval for other user");
        }
    }
    
    private void Delete() {
        
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        String lDocNo=(String)ObjPO.getAttribute("AMEND_NO").getObj();
        
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this record ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if(ObjPO.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gUserID)) {
                if(ObjPO.Delete(EITLERPGLOBAL.gUserID)) {
                    MoveLast();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is "+ObjPO.LastError);
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
            }
        }
    }
    
    private void Save() {
        //Form level validations
        if(Validate()==false) {
            return; //Validation failed
        }
        
        EITLERPGLOBAL.ChangeCursorToWait(this);
        
        SetData();
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(ObjPO.Insert()) {
                MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjPO.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjPO.Update()) {
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjPO.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        try
        {
        frmPA.RefreshView();
        }catch(Exception e){}
        ShowMessage("Ready");
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void Cancel() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        DisplayData();
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void Find() {
        Loader ObjLoader=new Loader(this,"EITLERP.Purchase.frmPOFind",true);
        frmPOFind ObjReturn= (frmPOFind) ObjLoader.getObj();
        
        if(ObjReturn.Cancelled==false) {
            //Add PO Type
            if(!ObjReturn.strQuery.trim().equals("")) {
                ObjReturn.strQuery=ObjReturn.strQuery+" AND PO_TYPE="+POType;
            }
            else {
                ObjReturn.strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_TYPE="+POType+" AND PO_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PO_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ";
            }
            
            if(!ObjPO.Filter(ObjReturn.strQuery,EITLERPGLOBAL.gCompanyID)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveLast();
        }
    }
    
    private void MoveFirst() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjPO.MoveFirst();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MovePrevious() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjPO.MovePrevious();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    
    private void MoveNext() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjPO.MoveNext();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    
    private void MoveLast() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjPO.MoveLast();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    //Recurses through the hierarchy of classes
    //until it finds Frame
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null) {
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
    public void FindEx(int pCompanyID,String pDocNo) {
        ObjPO.POType=POType;
        ObjPO.Filter(" WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND AMEND_NO='"+pDocNo+"' AND PO_TYPE="+POType,pCompanyID);
        ObjPO.MoveLast();
        DisplayData();
    }
    
    public void FindWaiting() {
        ObjPO.POType=POType;
        ObjPO.Filter(" WHERE AMEND_NO IN(SELECT D_PUR_PO_HEADER.AMEND_NO FROM D_PUR_PO_HEADER,D_COM_DOC_DATA WHERE D_PUR_PO_HEADER.AMEND_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_PO_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_PO_HEADER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gUserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_PO_HEADER.PO_TYPE="+POType+" AND MODULE_ID="+(47)+")",EITLERPGLOBAL.gCompanyID);
        ObjPO.MoveLast();
        DisplayData();
    }
    
    
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
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
                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, SelHierarchy, "H", FieldName)) {
                    
                    Tab1.getComponent(i).setEnabled(true);
                }
                
            }
        }
        
        for(int i=0;i<Tab2.getComponentCount()-1;i++) {
            if(Tab2.getComponent(i).getName()!=null) {
                
                FieldName=Tab2.getComponent(i).getName();
                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, SelHierarchy, "H", FieldName)) {
                    
                    Tab2.getComponent(i).setEnabled(true);
                }
                
            }
        }
        //=============== Header Fields Setup Complete =================//
        
        
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
    
    private void PreviewReport() {
        HashMap Params=new HashMap();
    
        if(chkCancelled.isSelected())
        {
         JOptionPane.showMessageDialog(null,"You cannot take printout of cancelled document");
         return;
        }
        
        try {
            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptServiceContractAmend.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        }
        
    }
    
    private void PreviewAuditReport() {
        try {
            if(POType==1) {
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOGenA.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&DocType=1");
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            
            if(POType==2) {
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOWIndentA.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&DocType=2");
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            
            if(POType==3) {
                URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAClassA.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&DocType=3");
                EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
        }
    }

    
private void DisplayPOData(clsPOGen thePO) {
        try {
            ClearFields();
            
            txtPONo.setText((String)thePO.getAttribute("PO_NO").getObj());
            txtPODate.setText(EITLERPGLOBAL.formatDate((String)thePO.getAttribute("PO_DATE").getObj()));
            txtSuppCode.setText((String)thePO.getAttribute("SUPP_ID").getObj());
            txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
            EITLERPGLOBAL.setComboIndex(cmbBuyer,(int)thePO.getAttribute("BUYER").getVal());
            txtSubject.setText((String)thePO.getAttribute("SUBJECT").getObj());
            chkCancelled.setSelected(thePO.getAttribute("CANCELLED").getBool());
            txtRemarks.setText((String)thePO.getAttribute("REMARKS").getObj());
            
            txtPremises.setText((String)thePO.getAttribute("PREMISES_TYPE").getObj());
            txtServicePeriod.setText((String)thePO.getAttribute("SERVICE_PERIOD").getObj());
            txtFrequency.setText((String)thePO.getAttribute("SERVICE_FREQUENCY").getObj());
            txtNetAmount.setText(Double.toString(thePO.getAttribute("NET_AMOUNT").getVal()));
            
            txtScope.setText((String)thePO.getAttribute("SCOPE").getObj());
            txtContractDetails.setText((String)thePO.getAttribute("CONTRACT_DETAILS").getObj());
            txtPaymentTerms.setText((String)thePO.getAttribute("PAYMENT_TERM").getObj());
            txtServiceReport.setText((String)thePO.getAttribute("SERVICE_REPORT").getObj());
            txtInWords.setText((String)thePO.getAttribute("AMOUNT_IN_WORDS").getObj());
            txtTerminationTerms.setText((String)thePO.getAttribute("TERMINATION_TERMS").getObj());
        }
        catch(Exception e) {
          
        }
        
    }    
    
private void GenerateRejectedUserCombo() {
        HashMap List=new HashMap();
        HashMap DeptList=new HashMap();
        HashMap DeptUsers=new HashMap();
        
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
                    
                    IncludeUser=ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, 47, txtDocNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
                    IncludeUser=ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, 47, txtDocNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            int Creator=ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, 47, txtDocNo.getText());
            EITLERPGLOBAL.setComboIndex(cmbSendTo,Creator);
        }
        
        
    }
}



