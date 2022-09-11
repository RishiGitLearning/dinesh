/*
 * frmDepositMaster.java
 *
 * Created on April 7, 2004, 3:10 PM
 */

package EITLERP.Finance;
/**
 *
 * @author  Prathmesh Shah
 */
/*<APPLET CODE=frmDepositMaster.class HEIGHT=500 WIDTH=665></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.text.*;
import EITLERP.Utils.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
//import EITLERP.Images.*;

import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import java.text.*;
import java.lang.*;
import javax.swing.text.*;

public class frmSalesDepositMaster extends javax.swing.JApplet {
    
    private int EditMode=0;
    private clsSalesDepositMaster ObjSalesDepositMaster;
    
    private boolean Updating=false;
    
    private String theDocNo="";
    
    private EITLTableModel DataModelA;
    private EITLTableModel DataModelHS;
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbModel;
    
    private EITLComboModel cmbSchemeIDModel;
    private EITLComboModel cmbDepositTypeIDModel;
    private EITLComboModel cmbDepositTypeModel;
    private EITLComboModel cmbDepositPayableToModel;
    private EITLComboModel cmbDepositorCatagoryModel;
    private EITLComboModel cmbDepositorStatusModel;
    
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    
    private int SelHierarchyID=0;
    private int SelSchemeID=0;
    
    private int lnFromID=0;
    public frmPendingApprovals frmPA;
    
    /** Creates new form frmTemplate */
    public void init() {
        initComponents();
        setSize(780,540);
        
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
        
        
        /******************Generating the ComboBoxes*****************/
        ObjSalesDepositMaster = new clsSalesDepositMaster();
        
        GenerateCombos();
        
        GenerateDepositPayableToCombo();
        
        GenerateDepositerCatagoryCombo();
        
        GenerateDepositerStatusCombo();
        
        GenerateDepositTypeIDCombo();
        
        GenerateSchemeIDCombo();
        
        GenerateFromCombo();
        
        
        SetMenuForRights();
        
        FormatGridA();
        FormatGridHS();
        
        /******************Loading the Data to the Form*****************/
        if(getName().equals("Link")) {
        }
        else {
            if(ObjSalesDepositMaster.LoadData(EITLERPGLOBAL.gCompanyID)) {
                ObjSalesDepositMaster.MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while loading data. Error is "+ObjSalesDepositMaster.LastError);
            }
        }
        /***************************************************************************************************/
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
                
                List=ApprovalFlow.getRemainingUsers((int)EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID ,ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getString());
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
        catch(Exception e) {
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
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
        MainPanel = new javax.swing.JTabbedPane();
        ApplicantDetail = new javax.swing.JPanel();
        lblReceiptNo = new javax.swing.JLabel();
        txtReceiptNo = new javax.swing.JTextField();
        txtApplicantName = new javax.swing.JTextField();
        lblApplicantName = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblReceiptDate = new javax.swing.JLabel();
        txtReceiptDate = new javax.swing.JTextField();
        lblTitle1 = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        lblPincode = new javax.swing.JLabel();
        txtPincode = new javax.swing.JTextField();
        lblCity = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        cmdNext1 = new javax.swing.JButton();
        txtAddress1 = new javax.swing.JTextField();
        txtAddress2 = new javax.swing.JTextField();
        txtAddress3 = new javax.swing.JTextField();
        lblContactNo = new javax.swing.JLabel();
        txtContactNo = new javax.swing.JTextField();
        lblPartyCode = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        cmdShowParty = new javax.swing.JButton();
        txtRealizationDate = new javax.swing.JTextField();
        lblRealizationDate = new javax.swing.JLabel();
        cmdContactBig = new javax.swing.JButton();
        OtherDetail = new javax.swing.JPanel();
        lblSchemeID = new javax.swing.JLabel();
        lblDepositTypeID = new javax.swing.JLabel();
        lblInterestRate = new javax.swing.JLabel();
        txtInterestRate = new javax.swing.JTextField();
        lblDepositorStatus = new javax.swing.JLabel();
        cmbDepositorStatus = new javax.swing.JComboBox();
        cmbDepositorCatagory = new javax.swing.JComboBox();
        lblDepositerCatogory = new javax.swing.JLabel();
        lblDepositerCatagoryOthers = new javax.swing.JLabel();
        txtDepositerCatagotyOthers = new javax.swing.JTextField();
        lblDepositPayable = new javax.swing.JLabel();
        cmbDepositPayableTo = new javax.swing.JComboBox();
        txtPANNo = new javax.swing.JTextField();
        lblPanNo = new javax.swing.JLabel();
        txtPANDate = new javax.swing.JTextField();
        lblPanDate = new javax.swing.JLabel();
        lblParticulars = new javax.swing.JLabel();
        txtParticulars = new javax.swing.JTextField();
        cmdNext2 = new javax.swing.JButton();
        cmdBack1 = new javax.swing.JButton();
        lblEffectiveDate = new javax.swing.JLabel();
        txtEffectiveDate = new javax.swing.JTextField();
        lblMainAccountCode = new javax.swing.JLabel();
        txtMainAccountCode = new javax.swing.JTextField();
        lblInterestMainCode = new javax.swing.JLabel();
        txtInterestMainCode = new javax.swing.JTextField();
        cmbDepositTypeID = new javax.swing.JComboBox();
        cmbSchemeID = new javax.swing.JComboBox();
        chkTaxExFormReceived = new javax.swing.JCheckBox();
        chkTDSApplicable = new javax.swing.JCheckBox();
        BankDetail = new javax.swing.JPanel();
        lblChequeDate = new javax.swing.JLabel();
        lblChequeNo = new javax.swing.JLabel();
        txtChequeNo = new javax.swing.JTextField();
        txtChequeDate = new javax.swing.JTextField();
        lblAmount = new javax.swing.JLabel();
        txtBankName = new javax.swing.JTextField();
        lblBankAddress = new javax.swing.JLabel();
        lblBankName = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        txtBankCity = new javax.swing.JTextField();
        lblBankCity = new javax.swing.JLabel();
        txtBankAddress = new javax.swing.JTextField();
        lblBankPincode = new javax.swing.JLabel();
        txtBankPincode = new javax.swing.JTextField();
        cmdNext3 = new javax.swing.JButton();
        cmdBack2 = new javax.swing.JButton();
        lblBankCode = new javax.swing.JLabel();
        txtBankMainCode = new javax.swing.JTextField();
        txtFundTransfer = new javax.swing.JTextField();
        lblFundTransfer = new javax.swing.JLabel();
        chkFundTransfer = new javax.swing.JCheckBox();
        txtAccountName = new javax.swing.JTextField();
        cmdBack4 = new javax.swing.JButton();
        Status = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        lblDocumentHistory = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableHS = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdPreviewA = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        Approval = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        cmbHierarchy = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        txtFrom = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtFromRemarks = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        OpgApprove = new javax.swing.JRadioButton();
        OpgFinal = new javax.swing.JRadioButton();
        OpgReject = new javax.swing.JRadioButton();
        OpgHold = new javax.swing.JRadioButton();
        jLabel33 = new javax.swing.JLabel();
        cmbSendTo = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        txtToRemarks = new javax.swing.JTextField();
        cmdFromRemarksBig = new javax.swing.JButton();
        cmdBack3 = new javax.swing.JButton();
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

        cmdEdit.setToolTipText("Edit");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });

        ToolBar.add(cmdEdit);

        cmdDelete.setToolTipText("Delete");
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
        ToolBar.setBounds(0, 0, 770, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" SALES DEPOSIT MASTER");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 770, 25);

        ApplicantDetail.setLayout(null);

        ApplicantDetail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        lblReceiptNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReceiptNo.setText("Receipt No. :");
        ApplicantDetail.add(lblReceiptNo);
        lblReceiptNo.setBounds(10, 19, 120, 15);

        txtReceiptNo.setEditable(false);
        txtReceiptNo.setName("PARTY_ID");
        txtReceiptNo.setNextFocusableComponent(txtReceiptDate);
        txtReceiptNo.setEnabled(false);
        ApplicantDetail.add(txtReceiptNo);
        txtReceiptNo.setBounds(135, 20, 200, 19);

        txtApplicantName.setName("PARTY_NAME");
        txtApplicantName.setNextFocusableComponent(txtAddress1);
        txtApplicantName.setEnabled(false);
        ApplicantDetail.add(txtApplicantName);
        txtApplicantName.setBounds(135, 100, 200, 19);

        lblApplicantName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApplicantName.setText("Applicant's Name :");
        ApplicantDetail.add(lblApplicantName);
        lblApplicantName.setBounds(10, 99, 120, 15);

        lblAddress.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAddress.setText("Address :");
        ApplicantDetail.add(lblAddress);
        lblAddress.setBounds(10, 130, 120, 15);

        lblReceiptDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReceiptDate.setText("Receipt Date :");
        ApplicantDetail.add(lblReceiptDate);
        lblReceiptDate.setBounds(358, 20, 90, 15);

        txtReceiptDate.setName("PARTY_ID");
        txtReceiptDate.setNextFocusableComponent(txtTitle);
        txtReceiptDate.setEnabled(false);
        ApplicantDetail.add(txtReceiptDate);
        txtReceiptDate.setBounds(453, 20, 200, 19);

        lblTitle1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle1.setText("Title :");
        ApplicantDetail.add(lblTitle1);
        lblTitle1.setBounds(10, 50, 120, 15);

        txtTitle.setName("PARTY_ID");
        txtTitle.setNextFocusableComponent(txtRealizationDate);
        txtTitle.setEnabled(false);
        ApplicantDetail.add(txtTitle);
        txtTitle.setBounds(135, 50, 200, 19);

        lblPincode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPincode.setText("Pincode :");
        ApplicantDetail.add(lblPincode);
        lblPincode.setBounds(358, 162, 90, 15);

        txtPincode.setName("PARTY_ID");
        txtPincode.setNextFocusableComponent(txtContactNo);
        txtPincode.setEnabled(false);
        ApplicantDetail.add(txtPincode);
        txtPincode.setBounds(453, 160, 200, 19);

        lblCity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCity.setText("City :");
        ApplicantDetail.add(lblCity);
        lblCity.setBounds(358, 130, 90, 15);

        txtCity.setName("PARTY_ID");
        txtCity.setNextFocusableComponent(txtPincode);
        txtCity.setEnabled(false);
        ApplicantDetail.add(txtCity);
        txtCity.setBounds(453, 130, 200, 19);

        cmdNext1.setFont(new java.awt.Font("Tahoma", 1, 11));
        cmdNext1.setText("Next >>");
        cmdNext1.setNextFocusableComponent(txtTitle);
        cmdNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext1ActionPerformed(evt);
            }
        });

        ApplicantDetail.add(cmdNext1);
        cmdNext1.setBounds(520, 220, 120, 24);

        txtAddress1.setName("PARTY_ID");
        txtAddress1.setNextFocusableComponent(txtAddress2);
        txtAddress1.setEnabled(false);
        ApplicantDetail.add(txtAddress1);
        txtAddress1.setBounds(135, 130, 200, 19);

        txtAddress2.setName("PARTY_ID");
        txtAddress2.setNextFocusableComponent(txtAddress3);
        txtAddress2.setEnabled(false);
        ApplicantDetail.add(txtAddress2);
        txtAddress2.setBounds(135, 160, 200, 19);

        txtAddress3.setName("PARTY_ID");
        txtAddress3.setNextFocusableComponent(txtCity);
        txtAddress3.setEnabled(false);
        ApplicantDetail.add(txtAddress3);
        txtAddress3.setBounds(135, 190, 200, 19);

        lblContactNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblContactNo.setText("Contact No. :");
        ApplicantDetail.add(lblContactNo);
        lblContactNo.setBounds(358, 192, 90, 15);

        txtContactNo.setName("PARTY_ID");
        txtContactNo.setEnabled(false);
        ApplicantDetail.add(txtContactNo);
        txtContactNo.setBounds(453, 190, 200, 19);

        lblPartyCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPartyCode.setText("Party Code :");
        ApplicantDetail.add(lblPartyCode);
        lblPartyCode.setBounds(358, 102, 90, 15);

        txtPartyCode.setName("PARTY_ID");
        txtPartyCode.setNextFocusableComponent(cmdShowParty);
        txtPartyCode.setEnabled(false);
        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusLost(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });

        ApplicantDetail.add(txtPartyCode);
        txtPartyCode.setBounds(453, 100, 200, 19);

        cmdShowParty.setFont(new java.awt.Font("Tahoma", 1, 11));
        cmdShowParty.setText("Show Party");
        cmdShowParty.setNextFocusableComponent(cmdNext1);
        cmdShowParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowPartyActionPerformed(evt);
            }
        });

        ApplicantDetail.add(cmdShowParty);
        cmdShowParty.setBounds(658, 100, 103, 24);

        txtRealizationDate.setName("PARTY_ID");
        txtRealizationDate.setNextFocusableComponent(txtPartyCode);
        txtRealizationDate.setEnabled(false);
        txtRealizationDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRealizationDateFocusLost(evt);
            }
        });

        ApplicantDetail.add(txtRealizationDate);
        txtRealizationDate.setBounds(453, 50, 200, 19);

        lblRealizationDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRealizationDate.setText("Realization Date :");
        ApplicantDetail.add(lblRealizationDate);
        lblRealizationDate.setBounds(337, 52, 110, 15);

        cmdContactBig.setText("...");
        cmdContactBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdContactBigActionPerformed(evt);
            }
        });

        ApplicantDetail.add(cmdContactBig);
        cmdContactBig.setBounds(658, 190, 33, 21);

        MainPanel.addTab("Applicant Details", ApplicantDetail);

        OtherDetail.setLayout(null);

        lblSchemeID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSchemeID.setText(" Scheme ID :");
        lblSchemeID.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        OtherDetail.add(lblSchemeID);
        lblSchemeID.setBounds(15, 23, 130, 15);

        lblDepositTypeID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositTypeID.setText(" Deposit Type ID :");
        OtherDetail.add(lblDepositTypeID);
        lblDepositTypeID.setBounds(355, 140, 175, 15);

        lblInterestRate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInterestRate.setText("Interest Rate :");
        OtherDetail.add(lblInterestRate);
        lblInterestRate.setBounds(355, 79, 175, 15);

        txtInterestRate.setEditable(false);
        txtInterestRate.setName("PARTY_ID");
        txtInterestRate.setEnabled(false);
        OtherDetail.add(txtInterestRate);
        txtInterestRate.setBounds(536, 78, 200, 19);

        lblDepositorStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositorStatus.setText("Depositor Stauts :");
        OtherDetail.add(lblDepositorStatus);
        lblDepositorStatus.setBounds(15, 82, 130, 15);

        cmbDepositorStatus.setNextFocusableComponent(cmbDepositorCatagory);
        cmbDepositorStatus.setEnabled(false);
        OtherDetail.add(cmbDepositorStatus);
        cmbDepositorStatus.setBounds(151, 80, 200, 24);

        cmbDepositorCatagory.setNextFocusableComponent(cmbDepositPayableTo);
        cmbDepositorCatagory.setEnabled(false);
        cmbDepositorCatagory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepositorCatagoryItemStateChanged(evt);
            }
        });

        OtherDetail.add(cmbDepositorCatagory);
        cmbDepositorCatagory.setBounds(151, 110, 200, 24);

        lblDepositerCatogory.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositerCatogory.setText("Depositer Catagory :");
        OtherDetail.add(lblDepositerCatogory);
        lblDepositerCatogory.setBounds(15, 110, 130, 15);

        lblDepositerCatagoryOthers.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositerCatagoryOthers.setText("Depositer Catagory Others :");
        OtherDetail.add(lblDepositerCatagoryOthers);
        lblDepositerCatagoryOthers.setBounds(355, 110, 175, 15);

        txtDepositerCatagotyOthers.setEditable(false);
        txtDepositerCatagotyOthers.setName("PARTY_ID");
        txtDepositerCatagotyOthers.setNextFocusableComponent(cmbDepositPayableTo);
        txtDepositerCatagotyOthers.setEnabled(false);
        OtherDetail.add(txtDepositerCatagotyOthers);
        txtDepositerCatagotyOthers.setBounds(536, 108, 200, 19);

        lblDepositPayable.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositPayable.setText("Deposit Payable To :");
        OtherDetail.add(lblDepositPayable);
        lblDepositPayable.setBounds(15, 140, 130, 15);

        cmbDepositPayableTo.setNextFocusableComponent(cmbDepositTypeID);
        cmbDepositPayableTo.setEnabled(false);
        OtherDetail.add(cmbDepositPayableTo);
        cmbDepositPayableTo.setBounds(151, 140, 200, 24);

        txtPANNo.setName("PARTY_ID");
        txtPANNo.setNextFocusableComponent(txtPANDate);
        txtPANNo.setEnabled(false);
        OtherDetail.add(txtPANNo);
        txtPANNo.setBounds(151, 200, 200, 19);

        lblPanNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPanNo.setText("PAN No :");
        OtherDetail.add(lblPanNo);
        lblPanNo.setBounds(15, 200, 130, 15);

        txtPANDate.setName("PARTY_ID");
        txtPANDate.setNextFocusableComponent(txtParticulars);
        txtPANDate.setEnabled(false);
        OtherDetail.add(txtPANDate);
        txtPANDate.setBounds(536, 200, 200, 19);

        lblPanDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPanDate.setText("PAN Date :");
        OtherDetail.add(lblPanDate);
        lblPanDate.setBounds(355, 200, 175, 15);

        lblParticulars.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblParticulars.setText("Particulars :");
        OtherDetail.add(lblParticulars);
        lblParticulars.setBounds(38, 231, 110, 15);

        txtParticulars.setName("PARTY_ID");
        txtParticulars.setNextFocusableComponent(cmdBack1);
        txtParticulars.setEnabled(false);
        OtherDetail.add(txtParticulars);
        txtParticulars.setBounds(153, 231, 582, 19);

        cmdNext2.setFont(new java.awt.Font("Tahoma", 1, 11));
        cmdNext2.setText("Next>>");
        cmdNext2.setNextFocusableComponent(cmbSchemeID);
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });

        OtherDetail.add(cmdNext2);
        cmdNext2.setBounds(580, 260, 120, 24);

        cmdBack1.setFont(new java.awt.Font("Tahoma", 1, 11));
        cmdBack1.setText("<< Back");
        cmdBack1.setNextFocusableComponent(cmdNext2);
        cmdBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack1ActionPerformed(evt);
            }
        });

        OtherDetail.add(cmdBack1);
        cmdBack1.setBounds(430, 260, 120, 24);

        lblEffectiveDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffectiveDate.setText("Effective Date :");
        OtherDetail.add(lblEffectiveDate);
        lblEffectiveDate.setBounds(15, 50, 130, 15);

        txtEffectiveDate.setEditable(false);
        txtEffectiveDate.setName("PARTY_ID");
        txtEffectiveDate.setEnabled(false);
        OtherDetail.add(txtEffectiveDate);
        txtEffectiveDate.setBounds(151, 50, 200, 19);

        lblMainAccountCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMainAccountCode.setText("Main Account Code :");
        OtherDetail.add(lblMainAccountCode);
        lblMainAccountCode.setBounds(355, 20, 175, 15);

        txtMainAccountCode.setEditable(false);
        txtMainAccountCode.setName("PARTY_ID");
        txtMainAccountCode.setEnabled(false);
        OtherDetail.add(txtMainAccountCode);
        txtMainAccountCode.setBounds(536, 19, 200, 19);

        lblInterestMainCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInterestMainCode.setText("Interest Main Code :");
        OtherDetail.add(lblInterestMainCode);
        lblInterestMainCode.setBounds(355, 51, 175, 15);

        txtInterestMainCode.setEditable(false);
        txtInterestMainCode.setName("PARTY_ID");
        txtInterestMainCode.setEnabled(false);
        OtherDetail.add(txtInterestMainCode);
        txtInterestMainCode.setBounds(536, 49, 200, 19);

        cmbDepositTypeID.setNextFocusableComponent(chkTaxExFormReceived);
        cmbDepositTypeID.setEnabled(false);
        OtherDetail.add(cmbDepositTypeID);
        cmbDepositTypeID.setBounds(536, 140, 200, 24);

        cmbSchemeID.setNextFocusableComponent(cmbDepositorStatus);
        cmbSchemeID.setEnabled(false);
        cmbSchemeID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSchemeIDItemStateChanged(evt);
            }
        });

        OtherDetail.add(cmbSchemeID);
        cmbSchemeID.setBounds(151, 20, 200, 24);

        chkTaxExFormReceived.setText("chkTaxExFormReceived");
        chkTaxExFormReceived.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkTaxExFormReceived.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        chkTaxExFormReceived.setNextFocusableComponent(chkTDSApplicable);
        chkTaxExFormReceived.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chkTaxExFormReceived.setEnabled(false);
        OtherDetail.add(chkTaxExFormReceived);
        chkTaxExFormReceived.setBounds(151, 170, 169, 20);

        chkTDSApplicable.setText("TDS Applicable");
        chkTDSApplicable.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkTDSApplicable.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        chkTDSApplicable.setNextFocusableComponent(txtPANNo);
        chkTDSApplicable.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chkTDSApplicable.setEnabled(false);
        OtherDetail.add(chkTDSApplicable);
        chkTDSApplicable.setBounds(536, 170, 140, 20);

        MainPanel.addTab("Other Details", OtherDetail);

        BankDetail.setLayout(null);

        lblChequeDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChequeDate.setText("Cheque Date :");
        BankDetail.add(lblChequeDate);
        lblChequeDate.setBounds(380, 21, 90, 15);

        lblChequeNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChequeNo.setText("Cheque No. :");
        BankDetail.add(lblChequeNo);
        lblChequeNo.setBounds(20, 20, 110, 15);

        txtChequeNo.setName("PARTY_ID");
        txtChequeNo.setNextFocusableComponent(txtChequeDate);
        txtChequeNo.setEnabled(false);
        BankDetail.add(txtChequeNo);
        txtChequeNo.setBounds(135, 20, 220, 19);

        txtChequeDate.setName("PARTY_ID");
        txtChequeDate.setNextFocusableComponent(txtAmount);
        txtChequeDate.setEnabled(false);
        BankDetail.add(txtChequeDate);
        txtChequeDate.setBounds(475, 20, 220, 19);

        lblAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmount.setText("Amount :");
        BankDetail.add(lblAmount);
        lblAmount.setBounds(20, 50, 110, 15);

        txtBankName.setEditable(false);
        txtBankName.setName("PARTY_ID");
        txtBankName.setNextFocusableComponent(txtBankAddress);
        txtBankName.setEnabled(false);
        BankDetail.add(txtBankName);
        txtBankName.setBounds(475, 130, 220, 19);

        lblBankAddress.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankAddress.setText("Bank Address :");
        BankDetail.add(lblBankAddress);
        lblBankAddress.setBounds(20, 160, 110, 15);

        lblBankName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankName.setText("Bank Name :");
        BankDetail.add(lblBankName);
        lblBankName.setBounds(380, 130, 90, 15);

        txtAmount.setName("PARTY_ID");
        txtAmount.setNextFocusableComponent(chkFundTransfer);
        txtAmount.setEnabled(false);
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAmountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });

        BankDetail.add(txtAmount);
        txtAmount.setBounds(135, 50, 220, 19);

        txtBankCity.setEditable(false);
        txtBankCity.setName("PARTY_ID");
        txtBankCity.setNextFocusableComponent(txtBankPincode);
        txtBankCity.setEnabled(false);
        BankDetail.add(txtBankCity);
        txtBankCity.setBounds(475, 160, 220, 19);

        lblBankCity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankCity.setText("Bank City :");
        BankDetail.add(lblBankCity);
        lblBankCity.setBounds(380, 160, 90, 15);

        txtBankAddress.setEditable(false);
        txtBankAddress.setName("PARTY_ID");
        txtBankAddress.setNextFocusableComponent(txtBankCity);
        txtBankAddress.setEnabled(false);
        BankDetail.add(txtBankAddress);
        txtBankAddress.setBounds(135, 160, 220, 19);

        lblBankPincode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankPincode.setText("Bank Pincode :");
        BankDetail.add(lblBankPincode);
        lblBankPincode.setBounds(20, 190, 110, 15);

        txtBankPincode.setEditable(false);
        txtBankPincode.setName("PARTY_ID");
        txtBankPincode.setEnabled(false);
        BankDetail.add(txtBankPincode);
        txtBankPincode.setBounds(135, 190, 220, 19);

        cmdNext3.setFont(new java.awt.Font("Tahoma", 1, 11));
        cmdNext3.setText("Next>>");
        cmdNext3.setNextFocusableComponent(txtChequeNo);
        cmdNext3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext3ActionPerformed(evt);
            }
        });

        BankDetail.add(cmdNext3);
        cmdNext3.setBounds(540, 240, 120, 24);

        cmdBack2.setFont(new java.awt.Font("Tahoma", 1, 11));
        cmdBack2.setText("<<Back");
        cmdBack2.setNextFocusableComponent(cmdNext3);
        cmdBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack2ActionPerformed(evt);
            }
        });

        BankDetail.add(cmdBack2);
        cmdBack2.setBounds(400, 240, 120, 24);

        lblBankCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankCode.setText("Bank Main Code :");
        BankDetail.add(lblBankCode);
        lblBankCode.setBounds(20, 130, 110, 15);

        txtBankMainCode.setName("PARTY_ID");
        txtBankMainCode.setNextFocusableComponent(cmdBack2);
        txtBankMainCode.setEnabled(false);
        txtBankMainCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBankMainCodeKeyPressed(evt);
            }
        });

        BankDetail.add(txtBankMainCode);
        txtBankMainCode.setBounds(135, 130, 220, 19);

        txtFundTransfer.setName("PARTY_ID");
        txtFundTransfer.setNextFocusableComponent(cmdBack2);
        txtFundTransfer.setEnabled(false);
        txtFundTransfer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFundTransferKeyPressed(evt);
            }
        });

        BankDetail.add(txtFundTransfer);
        txtFundTransfer.setBounds(240, 90, 115, 19);

        lblFundTransfer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFundTransfer.setText("Fund Transfer  :");
        BankDetail.add(lblFundTransfer);
        lblFundTransfer.setBounds(130, 91, 106, 15);

        chkFundTransfer.setNextFocusableComponent(txtBankMainCode);
        chkFundTransfer.setEnabled(false);
        chkFundTransfer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkFundTransferItemStateChanged(evt);
            }
        });

        BankDetail.add(chkFundTransfer);
        chkFundTransfer.setBounds(109, 90, 20, 15);

        txtAccountName.setName("PARTY_ID");
        txtAccountName.setNextFocusableComponent(cmdBack2);
        txtAccountName.setEnabled(false);
        BankDetail.add(txtAccountName);
        txtAccountName.setBounds(475, 90, 220, 19);

        cmdBack4.setFont(new java.awt.Font("Tahoma", 1, 11));
        cmdBack4.setText("Ref. Voucher");
        cmdBack4.setNextFocusableComponent(cmdNext3);
        cmdBack4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack4ActionPerformed(evt);
            }
        });

        BankDetail.add(cmdBack4);
        cmdBack4.setBounds(170, 240, 120, 24);

        MainPanel.addTab("Bank Details", BankDetail);

        Status.setLayout(null);

        Status.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel26.setText("Document Approval Status");
        Status.add(jLabel26);
        jLabel26.setBounds(12, 10, 242, 14);

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

        Status.add(jScrollPane2);
        jScrollPane2.setBounds(12, 40, 614, 144);

        lblDocumentHistory.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblDocumentHistory.setText("Document Update History");
        Status.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(13, 191, 182, 14);

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
        jScrollPane3.setViewportView(TableHS);

        Status.add(jScrollPane3);
        jScrollPane3.setBounds(13, 207, 473, 148);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });

        Status.add(cmdViewHistory);
        cmdViewHistory.setBounds(498, 242, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });

        Status.add(cmdNormalView);
        cmdNormalView.setBounds(498, 273, 132, 24);

        cmdPreviewA.setText("Preview Report");
        Status.add(cmdPreviewA);
        cmdPreviewA.setBounds(498, 209, 132, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });

        Status.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(498, 305, 132, 24);

        txtAuditRemarks.setEnabled(false);
        Status.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(500, 335, 129, 19);

        MainPanel.addTab("Status", Status);

        Approval.setLayout(null);

        Approval.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy  :");
        Approval.add(jLabel31);
        jLabel31.setBounds(7, 16, 74, 15);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });

        Approval.add(cmbHierarchy);
        cmbHierarchy.setBounds(86, 14, 184, 24);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        Approval.add(jLabel32);
        jLabel32.setBounds(40, 52, 40, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        Approval.add(txtFrom);
        txtFrom.setBounds(86, 50, 182, 19);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        Approval.add(jLabel35);
        jLabel35.setBounds(19, 80, 62, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setEnabled(false);
        Approval.add(txtFromRemarks);
        txtFromRemarks.setBounds(86, 78, 468, 19);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action  :");
        Approval.add(jLabel36);
        jLabel36.setBounds(1, 120, 86, 15);

        jPanel7.setLayout(null);

        jPanel7.setBorder(new javax.swing.border.EtchedBorder());
        OpgApprove.setText("Approve & Forward");
        buttonGroup1.add(OpgApprove);
        OpgApprove.setEnabled(false);
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });

        jPanel7.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 171, 23);

        OpgFinal.setText("Final Approve");
        buttonGroup1.add(OpgFinal);
        OpgFinal.setEnabled(false);
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });

        jPanel7.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 136, 20);

        OpgReject.setText("Reject");
        buttonGroup1.add(OpgReject);
        OpgReject.setEnabled(false);
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });

        jPanel7.add(OpgReject);
        OpgReject.setBounds(6, 54, 136, 20);

        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        buttonGroup1.add(OpgHold);
        OpgHold.setEnabled(false);
        jPanel7.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        Approval.add(jPanel7);
        jPanel7.setBounds(88, 120, 182, 100);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        Approval.add(jLabel33);
        jLabel33.setBounds(21, 232, 60, 15);

        cmbSendTo.setEnabled(false);
        Approval.add(cmbSendTo);
        cmbSendTo.setBounds(88, 228, 184, 24);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        Approval.add(jLabel34);
        jLabel34.setBounds(17, 262, 66, 15);

        txtToRemarks.setEnabled(false);
        Approval.add(txtToRemarks);
        txtToRemarks.setBounds(90, 260, 516, 19);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });

        Approval.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(559, 77, 33, 21);

        cmdBack3.setFont(new java.awt.Font("Tahoma", 1, 11));
        cmdBack3.setText("<<Back");
        cmdBack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack3ActionPerformed(evt);
            }
        });

        Approval.add(cmdBack3);
        cmdBack3.setBounds(500, 290, 120, 24);

        MainPanel.addTab("Approval", Approval);

        getContentPane().add(MainPanel);
        MainPanel.setBounds(0, 70, 770, 400);
        MainPanel.getAccessibleContext().setAccessibleName("Details");
        MainPanel.getAccessibleContext().setAccessibleDescription("Details");

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(5, 475, 760, 22);

    }//GEN-END:initComponents
    
    private void cmdBack4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack4ActionPerformed
        // TODO add your handling code here:
        String SQL = "SELECT REF_VOUCHER_NO FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+txtReceiptNo.getText().trim()+"' ";
        String VoucherNo = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
        clsVoucher.OpenVoucher(VoucherNo, new frmPendingApprovals());
    }//GEN-LAST:event_cmdBack4ActionPerformed
    
    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
        if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT) {
            if(evt.getKeyCode()==112) {//F1 Key pressed
                LOV aList=new LOV();
                
                aList.SQL="SELECT PARTY_CODE,PARTY_NAME "+
                " FROM D_FIN_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE IN ('210010','210027','210034','210072') "+
                " ORDER BY PARTY_CODE ";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                aList.UseSpecifiedConn=true;
                aList.dbURL=FinanceGlobal.FinURL;
                
                if(aList.ShowLOV()) {
                    txtPartyCode.setText(aList.ReturnVal);
                    txtApplicantName.setText(data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"' ",FinanceGlobal.FinURL));
                }
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed
    
    private void txtFundTransferKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFundTransferKeyPressed
        // TODO add your handling code here:
        if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT) {
            if(evt.getKeyCode() == 112) {
                LOV aList=new LOV();
                
                aList.SQL="SELECT BOOK_CODE, MAIN_ACCOUNT_CODE, BOOK_NAME AS BANK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE<>'' ORDER BY MAIN_ACCOUNT_CODE";
                aList.ReturnCol=1;
                aList.SecondCol=2;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                aList.UseSpecifiedConn=true;
                aList.dbURL=FinanceGlobal.FinURL;
                
                if(aList.ShowLOV()) {
                    txtFundTransfer.setText(aList.SecondVal);
                    txtAccountName.setText(data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+txtFundTransfer.getText()+"' ",FinanceGlobal.FinURL));
                    //txtBankName.setText(data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+aList.SecondVal+"'", FinanceGlobal.FinURL));
                    ObjSalesDepositMaster.BookCode = aList.ReturnVal;
                    cmdBack2.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txtFundTransferKeyPressed
    
    private void chkFundTransferItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkFundTransferItemStateChanged
        // TODO add your handling code here:
        if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT) {
            if(chkFundTransfer.isSelected()) {
                txtFundTransfer.setEnabled(true);
                txtFundTransfer.setEditable(true);
                txtBankMainCode.setText("");
                txtBankName.setText("");
                txtBankAddress.setText("");
                txtBankCity.setText("");
                txtBankPincode.setText("");
                txtBankMainCode.setEnabled(false);
                txtBankMainCode.setEnabled(false);
                txtAccountName.setText("");
                txtFundTransfer.requestFocus();
            } else {
                txtFundTransfer.setText("");
                txtFundTransfer.setEnabled(false);
                txtFundTransfer.setEditable(false);
                txtAccountName.setText("");
                txtBankMainCode.setEditable(true);
                txtBankMainCode.setEnabled(true);
                txtBankMainCode.setText(ObjSalesDepositMaster.getAttribute("BANK_MAIN_CODE").getString());
                txtBankName.setText(ObjSalesDepositMaster.getAttribute("BANK_NAME").getString());
                txtBankAddress.setText(ObjSalesDepositMaster.getAttribute("BANK_ADDRESS").getString());
                txtBankCity.setText(ObjSalesDepositMaster.getAttribute("BANK_CITY").getString());
                txtBankPincode.setText(ObjSalesDepositMaster.getAttribute("BANK_PINCODE").getString());
                txtBankMainCode.requestFocus();
            }
        }
    }//GEN-LAST:event_chkFundTransferItemStateChanged
    
    private void cmdContactBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdContactBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtContactNo;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdContactBigActionPerformed
    
    private void txtRealizationDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRealizationDateFocusLost
        // TODO add your handling code here:
        try {
            txtReceiptDate.setText(txtRealizationDate.getText().trim());
            txtEffectiveDate.setText(txtRealizationDate.getText().trim());
        } catch(Exception e) {
        }
    }//GEN-LAST:event_txtRealizationDateFocusLost
    
    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        try {
            if(!txtPartyCode.getText().trim().equals("")) {
                ResultSet rsParty = data.getResult("SELECT PARTY_NAME, ADDRESS, CITY, PINCODE, PHONE, FAX, MOBILE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"' ",FinanceGlobal.FinURL);
                txtApplicantName.setText(UtilFunctions.getString(rsParty,"PARTY_NAME",""));
                txtAddress1.setText(UtilFunctions.getString(rsParty,"ADDRESS",""));
                txtCity.setText(UtilFunctions.getString(rsParty,"CITY", ""));
                txtPincode.setText(UtilFunctions.getString(rsParty,"PINCODE", ""));
                String Phone = UtilFunctions.getString(rsParty,"PHONE", "");
                String Fax = UtilFunctions.getString(rsParty,"FAX", "");
                String Mobile = UtilFunctions.getString(rsParty,"MOBILE", "");
                String Contact = "";
                if(!Phone.equals("")) {
                    Contact += "\nPHONE : " + Phone +" ";
                }
                if(!Fax.equals("")) {
                    Contact += "\nFAX : " + Fax +" ";
                }
                if(!Mobile.equals("")) {
                    Contact += "\nMOBILE : " + Mobile +" ";
                }
                txtContactNo.setText(Contact);
            } else {
                txtApplicantName.setText("");
                txtAddress1.setText("");
                txtAddress2.setText("");
                txtAddress3.setText("");
                txtCity.setText("");
                txtPincode.setText("");
                txtContactNo.setText("");
            }
        } catch(Exception e) {
        }
    }//GEN-LAST:event_txtPartyCodeFocusLost
    
    private void cmdShowPartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowPartyActionPerformed
        // TODO add your handling code here:
        try {
            if(txtPartyCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please Enter Party Code.");
                return;
            }
            
            if(txtMainAccountCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please Enter Main Account Code.");
                return;
            }
            AppletFrame aFrame=new AppletFrame("Party Master");
            aFrame.startAppletEx("EITLERP.Finance.frmPartyMaster","Party Master");
            frmPartyMaster ObjPartyMaster=(frmPartyMaster) aFrame.ObjApplet;
            String pCondition="SELECT PARTY_ID FROM D_FIN_PARTY_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+txtPartyCode.getText().trim()+"' AND MAIN_ACCOUNT_CODE='"+txtMainAccountCode.getText().trim()+"' ";
            int PartyID = data.getIntValueFromDB(pCondition,FinanceGlobal.FinURL);
            ObjPartyMaster.FindEx(EITLERPGLOBAL.gCompanyID,PartyID,txtPartyCode.getText().trim());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdShowPartyActionPerformed
    
    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        if(txtAmount.getText().trim().equals("")) {
            txtAmount.setText("0.0");
        }
    }//GEN-LAST:event_txtAmountFocusLost
    
    private void txtAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusGained
        // TODO add your handling code here:
        if(txtAmount.getText().trim().equals("0.0")) {
            txtAmount.setText("");
        }
    }//GEN-LAST:event_txtAmountFocusGained
    
    private void txtBankMainCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBankMainCodeKeyPressed
        // TODO add your handling code here:
        if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT) {
            if(evt.getKeyCode() == 112) {
                LOV aList=new LOV();
                
                aList.SQL="SELECT BOOK_CODE, MAIN_ACCOUNT_CODE, BOOK_NAME AS BANK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE<>'' ORDER BY MAIN_ACCOUNT_CODE";
                aList.ReturnCol=1;
                aList.SecondCol=2;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                aList.UseSpecifiedConn=true;
                aList.dbURL=FinanceGlobal.FinURL;
                
                if(aList.ShowLOV()) {
                    txtBankMainCode.setText(aList.SecondVal);
                    txtBankName.setText(data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+aList.SecondVal+"'", FinanceGlobal.FinURL));
                    ObjSalesDepositMaster.BookCode = aList.ReturnVal;
                    cmdBack2.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txtBankMainCodeKeyPressed
    
    private void cmbSchemeIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSchemeIDItemStateChanged
        // TODO add your handling code here:
        if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT) {
            if(cmbSchemeID.getSelectedIndex()!=0) {
                String SchemeID = EITLERPGLOBAL.getCombostrCode(cmbSchemeID);
                txtInterestRate.setText(data.getStringValueFromDB("SELECT INTEREST_PERCENTAGE FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+ SchemeID +"' ",FinanceGlobal.FinURL));
                txtInterestMainCode.setText(data.getStringValueFromDB("SELECT INT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+ SchemeID +"' ",FinanceGlobal.FinURL));
                txtMainAccountCode.setText(data.getStringValueFromDB("SELECT DEPOSIT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+ SchemeID +"' ",FinanceGlobal.FinURL));
            } else {
                txtInterestRate.setText("");
                txtInterestMainCode.setText("");
                txtMainAccountCode.setText("");
            }
        }
    }//GEN-LAST:event_cmbSchemeIDItemStateChanged
    
    private void cmbDepositorCatagoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepositorCatagoryItemStateChanged
        // TODO add your handling code here:
        if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT) {
            int Category=cmbDepositorCatagory.getSelectedIndex();
            
            if(Category==3) { // In case of Blank
                txtDepositerCatagotyOthers.setText("");
                txtDepositerCatagotyOthers.setEnabled(true);
                txtDepositerCatagotyOthers.setEditable(true);
                txtDepositerCatagotyOthers.requestFocus();
            } else {
                txtDepositerCatagotyOthers.setText("");
                txtDepositerCatagotyOthers.setEnabled(false);
                txtDepositerCatagotyOthers.setEditable(false);
            }
        }
    }//GEN-LAST:event_cmbDepositorCatagoryItemStateChanged
    
    private void cmdBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack2ActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBack2ActionPerformed
    
    private void cmdBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack1ActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(0);
    }//GEN-LAST:event_cmdBack1ActionPerformed
    
    private void cmdBack3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack3ActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(2);
    }//GEN-LAST:event_cmdBack3ActionPerformed
    
    private void cmdNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext2ActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext2ActionPerformed
    
    private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext1ActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext1ActionPerformed
    
    private void cmdNext3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext3ActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(3);
    }//GEN-LAST:event_cmdNext3ActionPerformed
    
    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        //        // TODO add your handling code here:
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
        // TODO add your handling code here:
        SetupApproval();
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if(ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID,clsSalesDepositMaster.ModuleID , ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getString(),FinanceGlobal.FinURL)) {
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
        OpgHold.setSelected(false);
        
        GenerateRejectedUserCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked
    
    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtFromRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed
    
    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        ObjSalesDepositMaster.getHistoryList(EITLERPGLOBAL.gCompanyID, (String)ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getObj());
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed
    
    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjSalesDepositMaster.LoadData(EITLERPGLOBAL.gCompanyID);
        MoveFirst();
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
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ObjSalesDepositMaster.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
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
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this record ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            //     Delete();
        }
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
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ApplicantDetail;
    private javax.swing.JPanel Approval;
    private javax.swing.JPanel BankDetail;
    private javax.swing.JTabbedPane MainPanel;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel OtherDetail;
    private javax.swing.JPanel Status;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chkFundTransfer;
    private javax.swing.JCheckBox chkTDSApplicable;
    private javax.swing.JCheckBox chkTaxExFormReceived;
    private javax.swing.JComboBox cmbDepositPayableTo;
    private javax.swing.JComboBox cmbDepositTypeID;
    private javax.swing.JComboBox cmbDepositorCatagory;
    private javax.swing.JComboBox cmbDepositorStatus;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSchemeID;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBack1;
    private javax.swing.JButton cmdBack2;
    private javax.swing.JButton cmdBack3;
    private javax.swing.JButton cmdBack4;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdContactBig;
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
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewA;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowParty;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblAmount;
    private javax.swing.JLabel lblApplicantName;
    private javax.swing.JLabel lblBankAddress;
    private javax.swing.JLabel lblBankCity;
    private javax.swing.JLabel lblBankCode;
    private javax.swing.JLabel lblBankName;
    private javax.swing.JLabel lblBankPincode;
    private javax.swing.JLabel lblChequeDate;
    private javax.swing.JLabel lblChequeNo;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblContactNo;
    private javax.swing.JLabel lblDepositPayable;
    private javax.swing.JLabel lblDepositTypeID;
    private javax.swing.JLabel lblDepositerCatagoryOthers;
    private javax.swing.JLabel lblDepositerCatogory;
    private javax.swing.JLabel lblDepositorStatus;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblEffectiveDate;
    private javax.swing.JLabel lblFundTransfer;
    private javax.swing.JLabel lblInterestMainCode;
    private javax.swing.JLabel lblInterestRate;
    private javax.swing.JLabel lblMainAccountCode;
    private javax.swing.JLabel lblPanDate;
    private javax.swing.JLabel lblPanNo;
    private javax.swing.JLabel lblParticulars;
    private javax.swing.JLabel lblPartyCode;
    private javax.swing.JLabel lblPincode;
    private javax.swing.JLabel lblRealizationDate;
    private javax.swing.JLabel lblReceiptDate;
    private javax.swing.JLabel lblReceiptNo;
    private javax.swing.JLabel lblSchemeID;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitle1;
    private javax.swing.JTextField txtAccountName;
    private javax.swing.JTextField txtAddress1;
    private javax.swing.JTextField txtAddress2;
    private javax.swing.JTextField txtAddress3;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtApplicantName;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtBankAddress;
    private javax.swing.JTextField txtBankCity;
    private javax.swing.JTextField txtBankMainCode;
    private javax.swing.JTextField txtBankName;
    private javax.swing.JTextField txtBankPincode;
    private javax.swing.JTextField txtChequeDate;
    private javax.swing.JTextField txtChequeNo;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtContactNo;
    private javax.swing.JTextField txtDepositerCatagotyOthers;
    private javax.swing.JTextField txtEffectiveDate;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtFundTransfer;
    private javax.swing.JTextField txtInterestMainCode;
    private javax.swing.JTextField txtInterestRate;
    private javax.swing.JTextField txtMainAccountCode;
    private javax.swing.JTextField txtPANDate;
    private javax.swing.JTextField txtPANNo;
    private javax.swing.JTextField txtParticulars;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPincode;
    private javax.swing.JTextField txtRealizationDate;
    private javax.swing.JTextField txtReceiptDate;
    private javax.swing.JTextField txtReceiptNo;
    private javax.swing.JTextField txtTitle;
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
    
    private void SetFields(boolean pStat) {
        
        //Applicant Detail
        txtRealizationDate.setEnabled(pStat);
        txtTitle.setEnabled(pStat);
        txtApplicantName.setEnabled(pStat);
        txtAddress1.setEnabled(pStat);
        txtAddress2.setEnabled(pStat);
        txtAddress3.setEnabled(pStat);
        
        //Other Detail
        cmbSchemeID.setEnabled(pStat);
        cmbDepositPayableTo.setEnabled(pStat);
        cmbDepositTypeID.setEnabled(pStat);
        cmbDepositorStatus.setEnabled(pStat);
        cmbDepositorCatagory.setEnabled(pStat);
        
        chkTaxExFormReceived.setEnabled(pStat);
        txtPartyCode.setEnabled(pStat);
        chkTDSApplicable.setEnabled(pStat);
        txtPANNo.setEnabled(pStat);
        txtPANDate.setEnabled(pStat);
        txtParticulars.setEnabled(pStat);
        
        //Bank Detail
        txtChequeNo.setEnabled(pStat);
        txtChequeDate.setEnabled(pStat);
        txtBankMainCode.setEnabled(pStat);
        txtAmount.setEnabled(pStat);
        chkFundTransfer.setEnabled(pStat);
        
        //Approval Specific
        cmbHierarchy.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
    }
    
    private void ClearFields() {
        
        //Applicant Detail
        txtReceiptNo.setText("");
        txtReceiptDate.setText("");
        txtTitle.setText("");
        txtApplicantName.setText("");
        txtAddress1.setText("");
        txtAddress2.setText("");
        txtAddress3.setText("");
        txtCity.setText("");
        txtPincode.setText("");
        txtContactNo.setText("");
        
        //Other Detail
        cmbSchemeID.setSelectedIndex(0);
        cmbDepositorStatus.setSelectedIndex(0);
        cmbDepositorCatagory.setSelectedIndex(0);
        cmbDepositTypeID.setSelectedIndex(0);
        cmbDepositPayableTo.setSelectedIndex(0);
        
        txtInterestRate.setText("");
        txtMainAccountCode.setText("");
        txtInterestMainCode.setText("");
        txtDepositerCatagotyOthers.setText("");
        
        chkTaxExFormReceived.setSelected(false);
        txtPartyCode.setText("");
        chkTDSApplicable.setSelected(false);
        txtEffectiveDate.setText("");
        txtPANNo.setText("");
        txtPANDate.setText("");
        txtParticulars.setText("");
        
        //Bank Detail
        txtChequeNo.setText("");
        txtChequeDate.setText("");
        txtRealizationDate.setText("");
        txtAmount.setText("0.0");
        chkFundTransfer.setSelected(false);
        txtFundTransfer.setText("");
        
        txtBankMainCode.setText("");
        txtBankName.setText("");
        txtBankAddress.setText("");
        txtBankCity.setText("");
        txtBankPincode.setText("");
        
        FormatGridA();
        FormatGridHS();
    }
    
    //Didplay data on the Screen
    private void DisplayData() {
        
        //=========== Color Indication ===============//
        try {
            if(EditMode==0) {
                if((int)(ObjSalesDepositMaster.getAttribute("APPROVED").getVal())==1) {
                    lblTitle.setBackground(Color.BLUE);
                }
                
                if(((int)ObjSalesDepositMaster.getAttribute("APPROVED").getVal())!=1) {
                    lblTitle.setText(" DEPOSIT MASTER");
                    lblTitle.setBackground(Color.GRAY);
                }
                
                if(((int)ObjSalesDepositMaster.getAttribute("CANCELLED").getVal())==1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
        }
        catch(Exception c) {
            c.printStackTrace();
        }
        //============================================//
        
        
        //========= Authority Delegation Check =====================//
        if(EITLERPGLOBAL.gAuthorityUserID!=EITLERPGLOBAL.gUserID) {
            int ModuleID=clsSalesDepositMaster.ModuleID;
            
            if(clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gUserID,EITLERPGLOBAL.gAuthorityUserID,ModuleID)) {
                EITLERPGLOBAL.gNewUserID=EITLERPGLOBAL.gAuthorityUserID;
            }
            else {
                EITLERPGLOBAL.gNewUserID=EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//
        
        try{
            
            ClearFields();
            
            // Applicant Detail
            txtReceiptNo.setText(ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getString());
            txtReceiptDate.setText(EITLERPGLOBAL.formatDate(ObjSalesDepositMaster.getAttribute("RECEIPT_DATE").getString()));
            txtTitle.setText(ObjSalesDepositMaster.getAttribute("TITLE").getString());
            txtApplicantName.setText(ObjSalesDepositMaster.getAttribute("APPLICANT_NAME").getString());
            txtAddress1.setText(ObjSalesDepositMaster.getAttribute("ADDRESS1").getString());
            txtAddress2.setText(ObjSalesDepositMaster.getAttribute("ADDRESS2").getString());
            txtAddress3.setText(ObjSalesDepositMaster.getAttribute("ADDRESS3").getString());
            txtCity.setText(ObjSalesDepositMaster.getAttribute("CITY").getString());
            txtPincode.setText(ObjSalesDepositMaster.getAttribute("PINCODE").getString());
            txtContactNo.setText(ObjSalesDepositMaster.getAttribute("CONTACT_NO").getString());
            // End of Applicant Detail
            
            //Other Detail
            EITLERPGLOBAL.setComboIndex(cmbSchemeID,ObjSalesDepositMaster.getAttribute("SCHEME_ID").getString());
            EITLERPGLOBAL.setComboIndex(cmbDepositTypeID,ObjSalesDepositMaster.getAttribute("DEPOSIT_TYPE_ID").getInt());
            EITLERPGLOBAL.setComboIndex(cmbDepositorStatus,ObjSalesDepositMaster.getAttribute("DEPOSITER_STATUS").getInt());
            EITLERPGLOBAL.setComboIndex(cmbDepositorCatagory,ObjSalesDepositMaster.getAttribute("DEPOSITER_CATEGORY").getInt());
            EITLERPGLOBAL.setComboIndex(cmbDepositPayableTo,ObjSalesDepositMaster.getAttribute("DEPOSIT_PAYABLE_TO").getInt());
            
            txtMainAccountCode.setText(ObjSalesDepositMaster.getAttribute("MAIN_ACCOUNT_CODE").getString());
            txtInterestMainCode.setText(ObjSalesDepositMaster.getAttribute("INTEREST_MAIN_CODE").getString());
            txtInterestRate.setText(Double.toString(ObjSalesDepositMaster.getAttribute("INTEREST_RATE").getDouble()));
            
            txtDepositerCatagotyOthers.setText(ObjSalesDepositMaster.getAttribute("DEPOSITER_CATEGORY_OTHERS").getString());
            txtEffectiveDate.setText(EITLERPGLOBAL.formatDate(ObjSalesDepositMaster.getAttribute("EFFECTIVE_DATE").getString()));
            
            if(ObjSalesDepositMaster.getAttribute("TAX_EX_FORM_RECEIVED").getInt()== 1 ) {
                chkTaxExFormReceived.setSelected(true);
            } else {
                chkTaxExFormReceived.setSelected(false);
            }
            txtPartyCode.setText(ObjSalesDepositMaster.getAttribute("PARTY_CODE").getString());
            
            if(ObjSalesDepositMaster.getAttribute("TDS_APPLICABLE").getInt()== 1) {
                chkTDSApplicable.setSelected(true);
            } else {
                chkTDSApplicable.setSelected(false);
            }
            
            txtPANNo.setText(ObjSalesDepositMaster.getAttribute("PAN_NO").getString());
            txtPANDate.setText(EITLERPGLOBAL.formatDate(ObjSalesDepositMaster.getAttribute("PAN_DATE").getString()));
            txtParticulars.setText(ObjSalesDepositMaster.getAttribute("PARTICULARS").getString());
            //End of Other Detail
            
            //Bank Detail
            txtChequeNo.setText(ObjSalesDepositMaster.getAttribute("CHEQUE_NO").getString());
            txtChequeDate.setText(EITLERPGLOBAL.formatDate(ObjSalesDepositMaster.getAttribute("CHEQUE_DATE").getString()));
            txtRealizationDate.setText(EITLERPGLOBAL.formatDate(ObjSalesDepositMaster.getAttribute("REALIZATION_DATE").getString()));
            txtAmount.setText(Double.toString(ObjSalesDepositMaster.getAttribute("AMOUNT").getDouble()));
            txtFundTransfer.setText(ObjSalesDepositMaster.getAttribute("FUND_TRANSFER_FROM").getString());
            if(!txtFundTransfer.getText().trim().equals("")){
                txtAccountName.setText(data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+txtFundTransfer.getText()+"' ",FinanceGlobal.FinURL));
                chkFundTransfer.setSelected(true);
            } else {
                txtAccountName.setText("");
                chkFundTransfer.setSelected(false);
            }
            
            txtBankMainCode.setText(ObjSalesDepositMaster.getAttribute("BANK_MAIN_CODE").getString());
            txtBankName.setText(ObjSalesDepositMaster.getAttribute("BANK_NAME").getString());
            txtBankAddress.setText(ObjSalesDepositMaster.getAttribute("BANK_ADDRESS").getString());
            txtBankCity.setText(ObjSalesDepositMaster.getAttribute("BANK_CITY").getString());
            txtBankPincode.setText(ObjSalesDepositMaster.getAttribute("BANK_PINCODE").getString());
            //End of Bank Detail
            
            SetupApproval();
            // Set Hierarchy
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,ObjSalesDepositMaster.getAttribute("HIERARCHY_ID").getInt());
            
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List=new HashMap();
            String DocNo=ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getString();
            List=ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID, DocNo);
            for(int i=1;i<=List.size();i++) {
                clsDocFlow ObjFlow=(clsDocFlow)List.get(Integer.toString(i));
                Object[] rowData=new Object[7];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,ObjFlow.getAttribute("USER_ID").getInt());
                rowData[2]=ObjFlow.getAttribute("STATUS").getString();
                rowData[3]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int)ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[4]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6]=(String)ObjFlow.getAttribute("REMARKS").getObj();
                
                DataModelA.addRow(rowData);
            }
            
            FormatGridHS();
            HashMap History=ObjSalesDepositMaster.getHistoryList(EITLERPGLOBAL.gCompanyID, ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getString());
            
            for(int i=1;i<=History.size();i++) {
                clsSalesDepositMaster ObjHistory=(clsSalesDepositMaster)History.get(Integer.toString(i));
                Object[] rowData=new Object[5];
                
                rowData[0]=Integer.toString((int)ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1]=ObjHistory.getAttribute("UPDATED_BY").getString();
                rowData[2]=EITLERPGLOBAL.formatDate(ObjHistory.getAttribute("ENTRY_DATE").getString());
                
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
                rowData[4]=ObjHistory.getAttribute("APPROVER_REMARKS").getString();
                
                DataModelHS.addRow(rowData);
            }
            //=========================== Audit Trail Over ===========================//
            ShowMessage("Ready...");
            //********************************************//
        }
        catch(Exception e) {
        }
    }
    
    //Sets data to the Class Object
    private void SetData() {
        try {
            //===========================Applicant Detail============================
            ObjSalesDepositMaster.setAttribute("PREFIX",SelPrefix);
            ObjSalesDepositMaster.setAttribute("SUFFIX",SelSuffix);
            ObjSalesDepositMaster.setAttribute("FFNO",FFNo);
            
            ObjSalesDepositMaster.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            ObjSalesDepositMaster.setAttribute("RECEIPT_NO",txtReceiptNo.getText());
            ObjSalesDepositMaster.setAttribute("RECEIPT_DATE",txtReceiptDate.getText());
            ObjSalesDepositMaster.setAttribute("TITLE",txtTitle.getText());
            
            ObjSalesDepositMaster.setAttribute("APPLICANT_NAME",txtApplicantName.getText());
            ObjSalesDepositMaster.setAttribute("ADDRESS1",txtAddress1.getText());
            ObjSalesDepositMaster.setAttribute("ADDRESS2",txtAddress2.getText());
            ObjSalesDepositMaster.setAttribute("ADDRESS3",txtAddress3.getText());
            ObjSalesDepositMaster.setAttribute("CITY",txtCity.getText());
            ObjSalesDepositMaster.setAttribute("PINCODE",txtPincode.getText());
            ObjSalesDepositMaster.setAttribute("CONTACT_NO",txtPincode.getText());
            //--------------------End Of Applicant Detail----------------------------
            
            //==============================Other Detail=============================
            
            int DepositerStatus=cmbDepositorStatus.getSelectedIndex();
            int DepositerCategory=cmbDepositorCatagory.getSelectedIndex();
            int DepositPayableTo=cmbDepositPayableTo.getSelectedIndex();
            String SchemeID = EITLERPGLOBAL.getCombostrCode(cmbSchemeID);
            int DepositTypeID=cmbDepositTypeID.getSelectedIndex();
            
            ObjSalesDepositMaster.setAttribute("SCHEME_ID",SchemeID);
            ObjSalesDepositMaster.setAttribute("DEPOSIT_TYPE_ID",DepositTypeID);
            ObjSalesDepositMaster.setAttribute("DEPOSITER_STATUS",DepositerStatus);
            ObjSalesDepositMaster.setAttribute("DEPOSITER_CATEGORY",DepositerCategory);
            ObjSalesDepositMaster.setAttribute("DEPOSIT_PAYABLE_TO",DepositPayableTo);
            
            ObjSalesDepositMaster.setAttribute("INTEREST_MAIN_CODE",txtInterestMainCode.getText());
            ObjSalesDepositMaster.setAttribute("MAIN_ACCOUNT_CODE",txtMainAccountCode.getText());
            ObjSalesDepositMaster.setAttribute("INTEREST_RATE",Double.parseDouble(txtInterestRate.getText()));
            ObjSalesDepositMaster.setAttribute("DEPOSITER_CATEGORY_OTHERS",txtDepositerCatagotyOthers.getText());
            
            ObjSalesDepositMaster.setAttribute("EFFECTIVE_DATE",txtEffectiveDate.getText());
            ObjSalesDepositMaster.setAttribute("REFUND_DATE","0000-00-00");
            //================Calculate Interest Calculation Date======================
            int FinYear = (EITLERPGLOBAL.getCurrentFinYear()+1);
            String interestCalcDate = Integer.toString(FinYear)+"-03-31";
            ObjSalesDepositMaster.setAttribute("INT_CALC_DATE",EITLERPGLOBAL.formatDate(interestCalcDate));
            //--------------------------------------------------------------------------
            
            if(chkTaxExFormReceived.isSelected()) {
                ObjSalesDepositMaster.setAttribute("TAX_EX_FORM_RECEIVED",1);
            } else {
                ObjSalesDepositMaster.setAttribute("TAX_EX_FORM_RECEIVED",0);
            }
            
            ObjSalesDepositMaster.setAttribute("PARTY_CODE",txtPartyCode.getText());
            
            if(chkTDSApplicable.isSelected()) {
                ObjSalesDepositMaster.setAttribute("TDS_APPLICABLE",1);
            } else {
                ObjSalesDepositMaster.setAttribute("TDS_APPLICABLE",0);
            }
            ObjSalesDepositMaster.setAttribute("PAN_NO",txtPANNo.getText());
            ObjSalesDepositMaster.setAttribute("PAN_DATE",txtPANDate.getText());
            ObjSalesDepositMaster.setAttribute("PARTICULARS",txtParticulars.getText());
            //-----------------------------End of Other Detail------------------------------------
            
            //=============================Bank Detail=======================================
            ObjSalesDepositMaster.setAttribute("CHEQUE_NO",txtChequeNo.getText());
            ObjSalesDepositMaster.setAttribute("CHEQUE_DATE",txtChequeDate.getText());
            ObjSalesDepositMaster.setAttribute("REALIZATION_DATE",txtRealizationDate.getText());
            ObjSalesDepositMaster.setAttribute("AMOUNT",Double.parseDouble(txtAmount.getText().trim()));
            ObjSalesDepositMaster.setAttribute("FUND_TRANSFER_FROM",txtFundTransfer.getText());
            ObjSalesDepositMaster.setAttribute("BANK_MAIN_CODE",txtBankMainCode.getText());
            ObjSalesDepositMaster.setAttribute("BANK_NAME",txtBankName.getText());
            ObjSalesDepositMaster.setAttribute("BANK_ADDRESS",txtBankAddress.getText());
            ObjSalesDepositMaster.setAttribute("BANK_CITY",txtBankCity.getText());
            ObjSalesDepositMaster.setAttribute("BANK_PINCODE",txtBankPincode.getText());
            //---------------------------End of Bank Detail----------------------------------
            
            //===========================Deposit Releated Information============================
            ObjSalesDepositMaster.setAttribute("DEPOSIT_STATUS", 0); //Status OPEN for new Entry - 0 Close - 1
            //---------------------End of Deposit Realeted Information---------------------------
            
            //=======================Update Approval Specific Fields==============================
            ObjSalesDepositMaster.setAttribute("HIERARCHY_ID",EITLERPGLOBAL.getComboCode(cmbHierarchy));
            ObjSalesDepositMaster.setAttribute("FROM",EITLERPGLOBAL.gNewUserID);
            ObjSalesDepositMaster.setAttribute("TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
            ObjSalesDepositMaster.setAttribute("FROM_REMARKS",txtToRemarks.getText());
            
            if(OpgApprove.isSelected()) {
                ObjSalesDepositMaster.setAttribute("APPROVAL_STATUS","A");
            }
            
            if(OpgFinal.isSelected()) {
                ObjSalesDepositMaster.setAttribute("APPROVAL_STATUS","F");
            }
            
            if(OpgReject.isSelected()) {
                ObjSalesDepositMaster.setAttribute("APPROVAL_STATUS","R");
                ObjSalesDepositMaster.setAttribute("SEND_DOC_TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
            }
            
            if(OpgHold.isSelected()) {
                ObjSalesDepositMaster.setAttribute("APPROVAL_STATUS","H");
            }
            //-------------------End of Approval Specific Fields------------------------------//
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void SetMenuForRights() {
        
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11421)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11422)) {
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11423)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11424)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }
    
    private void Add() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        //****************Now Generate new document no.****************//
        SelectFirstFree aList=new SelectFirstFree();
        aList.ModuleID=clsSalesDepositMaster.ModuleID;
        
        if(aList.ShowList()) {
            EditMode=EITLERPGLOBAL.ADD;
            ClearFields();
            SetFields(true);
            DisableToolbar();
            
            SelPrefix=aList.Prefix; //Selected Prefix;
            SelSuffix=aList.Suffix;
            FFNo=aList.FirstFreeNo;
            
            SetupApproval();
            //Display newly generated document no.
            
            txtReceiptNo.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID, FFNo,  false));
            txtReceiptDate.setText(EITLERPGLOBAL.getCurrentDate());
            txtRealizationDate.setText(EITLERPGLOBAL.getCurrentDate());
            txtEffectiveDate.setText(EITLERPGLOBAL.getCurrentDate());
            txtTitle.setText("M/S");
            txtRealizationDate.requestFocus();
            
            lblTitle.setText("DEPOSIT MASTER - " + txtReceiptNo.getText());
            lblTitle.setBackground(Color.BLUE);
        }
        else {
            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
    }
    
    private void Edit() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        String lDocNo=ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getString();
        
        if(ObjSalesDepositMaster.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            EITLERPGLOBAL.ChangeCursorToWait(this);
            EditMode=EITLERPGLOBAL.EDIT;
            //---New Change ---//
            GenerateCombos();
            DisplayData();
            SetupApproval();
            //----------------//
            
            if(ApprovalFlow.IsCreator(clsSalesDepositMaster.ModuleID ,lDocNo)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11422)) {
                SetFields(true);
            }
            else {
                EnableApproval();
            }
            SetupApproval();
            DisableToolbar();
            txtReceiptDate.requestFocus();
            
            EITLERPGLOBAL.ChangeCursorToDefault(this);
        }
        else {
            JOptionPane.showMessageDialog(null,"You cannot edit this record. It is either approved/rejected or waiting approval for other user");
        }
    }
    
    private boolean Validate() {
        
        /**********************FILL THE COMPULSORY FIELDS**********************/
        if(txtReceiptDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Receipt Date.");
            txtReceiptDate.requestFocus();
            return false;
        } else if(!(EITLERPGLOBAL.isDate(txtReceiptDate.getText().trim()))) {
            JOptionPane.showMessageDialog(null,"Please Enter The Receipt Date in DD/MM/YYYY Format.");
            txtReceiptDate.requestFocus();
            return false;
        }
        
        if(txtApplicantName.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Please Specify Applicant Name.");
            txtApplicantName.requestFocus();
            return false;
        }
        
        if(txtAddress1.getText().trim().equals("") && txtAddress2.getText().trim().equals("") && txtAddress3.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Please Specify Address");
            txtAddress1.requestFocus();
            return false;
        }
        
        if(cmbSchemeID.getSelectedIndex()==0) { // Select Scheme ID
            JOptionPane.showMessageDialog(null,"Please Select SchemeID.");
            return false;
        }
        
        if(cmbSchemeID.getSelectedIndex()<10) {
            JOptionPane.showMessageDialog(null,"SchemeID not acceptable.");
            return false;
        }
        
        if(cmbSchemeID.getSelectedIndex()>=10 && cmbSchemeID.getSelectedIndex()<=13) {
            if(cmbSchemeID.getSelectedIndex()==11) {
                JOptionPane.showMessageDialog(null,"SchemeID not acceptable.");
                return false;
            }
        }
        
        if(cmbDepositorStatus.getSelectedIndex()==0) { // Resident, NonResident, Indian Company, Others
            JOptionPane.showMessageDialog(null,"Please Select Depositer Status.");
            return false;
        }
        
        if(cmbDepositPayableTo.getSelectedIndex()==0) { //Depositer OR (Either or Survivor)
            JOptionPane.showMessageDialog(null,"Please Deposit PayableTo.");
            return false;
        }
        
        if(cmbDepositorCatagory.getSelectedIndex()==0) { // 1.Employee,2.Share Holder,3.Domestic Company,4.Others
            JOptionPane.showMessageDialog(null,"Please Select Depositer Catagory.");
            return false;
        }
        
        if(cmbDepositTypeID.getSelectedIndex()==0) {
            JOptionPane.showMessageDialog(null,"Please Select Deposit Type ID.");
            return false;
        }
        
        if(txtPANNo.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Please Enter PAN No.");
            txtPANNo.requestFocus();
            return false;
        }
        if(txtPANDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter PAN Date.");
            txtPANDate.requestFocus();
            return false;
        } else if(!(EITLERPGLOBAL.isDate(txtPANDate.getText().trim()))) {
            JOptionPane.showMessageDialog(null,"Please Enter The Pan Date in DD/MM/YYYY Format.");
            txtPANDate.requestFocus();
            return false;
        }
        
        if(chkTaxExFormReceived.isSelected() && chkTDSApplicable.isSelected()) {
            JOptionPane.showMessageDialog(null,"Please Select Either Tax Exemtion Form Received or TDS Applicable.");
            chkTaxExFormReceived.requestFocus();
            return false;
        }
        
        if(txtPartyCode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Party Code.");
            return false;
        }
        
        if(txtChequeNo.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Please Specify Cheque No.");
            txtChequeNo.requestFocus();
            return false;
        }
        
        if(txtChequeDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Cheque Date.");
            txtChequeDate.requestFocus();
            return false;
        } else if(!(EITLERPGLOBAL.isDate(txtChequeDate.getText().trim()))) {
            JOptionPane.showMessageDialog(null,"Please Enter The Cheque Date in DD/MM/YYYY Format.");
            txtChequeDate.requestFocus();
            return false;
        }
        
        if(txtRealizationDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Realization Date.");
            txtRealizationDate.requestFocus();
            return false;
        }
        
        if(!(txtAmount.getText().trim().equals("")) && (!EITLERPGLOBAL.IsNumber((txtAmount.getText()))) && (Integer.parseInt(txtAmount.getText())) > 0){
            JOptionPane.showMessageDialog(null,"Please Enter Valid Amount.");
            txtAmount.requestFocus();
            return false;
        }
        
        if(txtBankMainCode.getText().equals("") && txtFundTransfer.getText().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter either Bank Main Code or Fund transfer code.");
            txtBankMainCode.requestFocus();
            return false;
        }
        
        int DeptID = clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID);
        //if(!clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11421)) {
        if(DeptID!=33) {
            if(txtBankMainCode.getText().equals("999999")) {
                JOptionPane.showMessageDialog(null,"Please Enter valid Bank Main Code.");
                txtBankMainCode.requestFocus();
                return false;
            }
        }
        
        //if(!clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11421)) {
        if(DeptID!=33) {
            String MainCode = txtFundTransfer.getText();
            if(MainCode.equals("999999")) {
                JOptionPane.showMessageDialog(null,"Please Enter valid Main Account Code for Fund Transfer.");
                txtFundTransfer.requestFocus();
                return false;
            }
        }
        
        if(OpgReject.isSelected()) {
            if(txtToRemarks.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please Enter Remarks for rejection.");
                return false;
            }
        }
        return true;
        /**********************************************************************/
    }
    
    private void Save() {
        
        if(!Validate()){
            return;
        }
        EITLERPGLOBAL.ChangeCursorToWait(this);
        
        SetData();
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(ObjSalesDepositMaster.Insert()) {
                MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is : "+ObjSalesDepositMaster.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjSalesDepositMaster.Update()) {
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjSalesDepositMaster.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
        ShowMessage("Ready...");
        try {
            frmPA.RefreshView();
        } catch(Exception e) {
        }
    }
    
    private void Cancel() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        DisplayData();
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        txtEffectiveDate.setEnabled(false);
        txtDepositerCatagotyOthers.setEnabled(false);
        lblTitle.setText("SALES DEPOSIT MASTER");
        lblTitle.setBackground(Color.GRAY);
        EITLERPGLOBAL.ChangeCursorToDefault(this);
        ShowMessage("Ready...");
    }
    
    private void Find() {
        Loader ObjLoader=new Loader(this,"EITLERP.Finance.frmSalesDepositFind",true);
        frmSalesDepositFind ObjReturn= (frmSalesDepositFind) ObjLoader.getObj();
        
        if(ObjReturn.Cancelled==false) {
            if(!ObjSalesDepositMaster.Filter(ObjReturn.strQuery,EITLERPGLOBAL.gCompanyID)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveFirst();
        }
    }
    
    private void MoveFirst() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjSalesDepositMaster.MoveFirst();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
        ShowMessage("Ready...");
    }
    
    private void MovePrevious() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjSalesDepositMaster.MovePrevious();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
        ShowMessage("Ready...");
    }
    
    private void MoveNext() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjSalesDepositMaster.MoveNext();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
        ShowMessage("Ready...");
    }
    
    private void MoveLast() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjSalesDepositMaster.MoveLast();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
        ShowMessage("Ready...");
    }
    
    public void FindEx(int pCompanyID,String pDocNo) {
        ObjSalesDepositMaster.Filter(" WHERE COMPANY_ID="+pCompanyID+" AND RECEIPT_NO='"+pDocNo+"' ",pCompanyID);
        ObjSalesDepositMaster.MoveFirst();
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
    
    private void SetupApproval() {
        
        if(cmbHierarchy.getItemCount()>1) {
            cmbHierarchy.setEnabled(true);
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
            
            int FromUserID=ApprovalFlow.getFromID((int)EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID , (String)ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=ApprovalFlow.getFromRemarks( (int)EITLERPGLOBAL.gCompanyID,clsSalesDepositMaster.ModuleID ,FromUserID,Integer.toString(ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getInt()));
            
            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }
        
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
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
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
    
    private void GenerateDepositerStatusCombo() {
        
        cmbDepositorStatusModel=new EITLComboModel();
        cmbDepositorStatus.setModel(cmbDepositorStatusModel);
        cmbDepositorStatus.removeAllItems();
        
        ComboData aData = new ComboData();
        aData.Text="";
        aData.Code=0;
        cmbDepositorStatusModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="Resident";
        aData.Code=1;
        cmbDepositorStatusModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="NonResident";
        aData.Code=2;
        cmbDepositorStatusModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="Indian Company";
        aData.Code=3;
        cmbDepositorStatusModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="Others";
        aData.Code=4;
        cmbDepositorStatusModel.addElement(aData);
        
        //cmbDepositorStatus.setSelectedIndex(0);
    }
    
    private void GenerateDepositerCatagoryCombo() {
        cmbDepositorCatagoryModel=new EITLComboModel();
        cmbDepositorCatagory.setModel(cmbDepositorCatagoryModel);
        cmbDepositorCatagory.removeAllItems();
        
        ComboData aData = new ComboData();
        aData.Text="";
        aData.Code=0;
        cmbDepositorCatagoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="Company";
        aData.Code=1;
        cmbDepositorCatagoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="Partnership";
        aData.Code=2;
        cmbDepositorCatagoryModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="Others";
        aData.Code=3;
        cmbDepositorCatagoryModel.addElement(aData);
    }
    
    private void GenerateDepositPayableToCombo() {
        cmbDepositPayableToModel=new EITLComboModel();
        cmbDepositPayableTo.setModel(cmbDepositPayableToModel);
        cmbDepositPayableTo.removeAllItems();
        
        ComboData aData = new ComboData();
        aData.Text="";
        aData.Code=0;
        cmbDepositPayableToModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="Depositor";
        aData.Code=1;
        cmbDepositPayableToModel.addElement(aData);
        
        aData = new ComboData();
        aData.Text="Either Or Survivor";
        aData.Code=2;
        cmbDepositPayableToModel.addElement(aData);
        
        //cmbDepositPayableTo.setSelectedIndex(0);
    }
    
    private void GenerateSchemeIDCombo() {
        HashMap List=new HashMap();
        cmbSchemeIDModel=new EITLComboModel();
        cmbSchemeID.removeAllItems();
        cmbSchemeID.setModel(cmbSchemeIDModel);
        
        List=ObjSalesDepositMaster.getList();
        
        ComboData aData=new ComboData();
        aData.strCode="0";
        aData.Text="";
        cmbSchemeIDModel.addElement(aData);
        
        for(int i=1;i<=List.size();i++) {
            clsSalesDepositMaster ObjSalesDepositMaster1 =(clsSalesDepositMaster) List.get(Long.toString(i));
            
            aData = new ComboData();
            aData.strCode=ObjSalesDepositMaster1.getAttribute("SCHEME_ID").getString();
            aData.Text=ObjSalesDepositMaster1.getAttribute("SCHEME_NAME").getString();
            
            cmbSchemeIDModel.addElement(aData);
        }
    }
    
    private void GenerateDepositTypeIDCombo() {
        HashMap List=new HashMap();
        cmbDepositTypeIDModel=new EITLComboModel();
        cmbDepositTypeID.setModel(cmbDepositTypeIDModel);
        cmbDepositTypeID.removeAllItems();
        
        List=ObjSalesDepositMaster.getListD();
        
        ComboData cmbData=new ComboData();
        cmbData.Code=0;
        cmbData.Text="";
        cmbDepositTypeIDModel.addElement(cmbData);
        
        for(int i=1;i<=List.size();i++) {
            clsSalesDepositMaster ObjSalesDepositMaster =(clsSalesDepositMaster) List.get(Integer.toString(i));
            cmbData=new ComboData();
            cmbData.Code=ObjSalesDepositMaster.getAttribute("DEPOSIT_TYPE_ID").getInt();
            cmbData.Text=ObjSalesDepositMaster.getAttribute("DEPOSIT_TYPE_NAME").getString();
            
            cmbDepositTypeIDModel.addElement(cmbData);
        }
        //cmbDepositTypeID.setSelectedIndex(0);
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
                    IncludeUser=ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID,clsSalesDepositMaster.ModuleID , Integer.toString(ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getInt()), ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
                    IncludeUser=ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID, Integer.toString(ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getInt()), ObjUser.getAttribute("USER_ID").getInt() , EITLERPGLOBAL.gNewUserID);
                }
                
                if(IncludeUser&&(( ObjUser.getAttribute("USER_ID").getInt())!=EITLERPGLOBAL.gNewUserID)) {
                    cmbToModel.addElement(aData);
                }
            }
            else {
                if((ObjUser.getAttribute("USER_ID").getInt())!=EITLERPGLOBAL.gNewUserID) {
                    cmbToModel.addElement(aData);
                }
            }
            /// END NEW CODE ///
        }
        //------------------------------ //
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            int Creator=ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, clsSalesDepositMaster.ModuleID , ObjSalesDepositMaster.getAttribute("RECEIPT_NO").getString());
            EITLERPGLOBAL.setComboIndex(cmbSendTo,Creator);
        }
    }
    
    private void GenerateCombos() {
        try {
            //Generates Combo Boxes
            HashMap List=new HashMap();
            String strCondition="";
            ResultSet rsTmp;
            
            cmbHierarchyModel=new EITLComboModel();
            cmbHierarchy.removeAllItems();
            cmbHierarchy.setModel(cmbHierarchyModel);
            
            List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsSalesDepositMaster.ModuleID);
            
            if(EditMode==EITLERPGLOBAL.EDIT) {
                List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsSalesDepositMaster.ModuleID );
            }
            for(int i=1;i<=List.size();i++) {
                clsHierarchy ObjHierarchy=(clsHierarchy) List.get(Integer.toString(i));
                ComboData aData=new ComboData();
                aData.Code=(int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
                aData.Text=(String)ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
                cmbHierarchyModel.addElement(aData);
            }
        }
        catch(Exception e) {
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
        
        for(int i=0;i<ApplicantDetail.getComponentCount()-1;i++) {
            if(ApplicantDetail.getComponent(i).getName()!=null) {
                
                FieldName=ApplicantDetail.getComponent(i).getName();
                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    
                    ApplicantDetail.getComponent(i).setEnabled(true);
                }
            }
        }
        //=============== Header Fields Setup Complete =================//
    }
}