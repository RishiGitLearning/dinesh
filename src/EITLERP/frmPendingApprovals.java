/*
 * frmPendingApprovals.java
 *
 * Created on April 28, 2004, 12:02 PM
 */
package EITLERP;

import EITLERP.FeltSales.Perfomainvoice.clsProforma;
import EITLERP.FeltSales.Perfomainvoice.frmperfomainvoice;
import EITLERP.FeltSales.Budget.clsBudgetUpload;
import EITLERP.FeltSales.Budget.clsBudgetManual;
import EITLERP.FeltSales.DiversionList.FrmDiversionList;
import EITLERP.FeltSales.DiversionList.clsDiversionList;
import EITLERP.FeltSales.DiversionLoss.FrmFeltDiversionLoss;
import EITLERP.FeltSales.DiversionLoss.clsFeltOrderDiversionLoss;
import EITLERP.FeltSales.FeltEvaluation.*;
import EITLERP.FeltSales.FeltInvReport.clsFeltSalesInvoice;
import EITLERP.FeltSales.Order.FrmFeltOrder;
import EITLERP.FeltSales.Order.clsFeltOrder;
import EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion;
import EITLERP.FeltSales.OrderDiversion.clsFeltOrderDiversion;
import EITLERP.FeltSales.PieceUpdation.clsPieceUpdation;
import EITLERP.FeltSales.PieceUpdation.frmFeltPieceUpd;
import SDMLATTPAY.COFF.*;
import SDMLATTPAY.EmployeeAmend.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.io.File;
import EITLERP.Stores.*;
import EITLERP.Purchase.*;
import EITLERP.Finance.*;
import EITLERP.Sales.*;
import EITLERP.Production.FeltRateMaster.*;
import EITLERP.Production.FeltWeaving.*;
import EITLERP.Production.FeltMending.*;
import EITLERP.Production.FeltNeedling.*;
import EITLERP.Production.FeltFinishing.*;
import EITLERP.Production.FeltWarping.*;
import EITLERP.Production.FeltRejection.*;
import EITLERP.FeltSales.FeltPacking.*;
import EITLERP.FeltSales.Order.FrmFeltOrder;
import EITLERP.FeltSales.Order.clsFeltOrder;
import EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion;
import EITLERP.FeltSales.OrderDiversion.clsFeltOrderDiversion;
import EITLERP.Production.FeltExportInvoice.*;
import EITLERP.Production.FeltMachineSurvey.*;
import EITLERP.Production.FeltMachineSurveyAmend.*;
import EITLERP.Production.FeltWeavingLoom.*;
import EITLERP.Production.Felt_Order_Updation.*;
import EITLERP.Production.FeltUnadj.*;
import EITLERP.Production.FeltCreditNote.*;
import EITLERP.Production.FeltDiscRateMaster.*;
import EITLERP.FeltSales.GroupMaster.*;
import EITLERP.FeltSales.GroupMasterAmend.*;
import EITLERP.FeltSales.ReopenBale12.*;
import EITLERP.FeltSales.FeltProcessInvoiceVariable.*;
import EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm;
import EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.clsFeltGSTAdvancePaymentEntryForm;
import EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.clsParameterCancel;
import EITLERP.FeltSales.FeltInvCvrLtr.clsFeltInvCvrLtr;
import EITLERP.FeltSales.FeltInvCvrLtr.frmFeltInvCvrLtr;
import EITLERP.FeltSales.SalesReturns.*;
import EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping;
import EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmend;
import EITLERP.FeltSales.FeltPieceAmend.clsFeltPieceAmend;
import EITLERP.FeltSales.PieceDivision.clsPieceDivision;
import EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmInvoiceParameterCancel;
import EITLERP.FeltSales.FeltLCMaster.clsFeltLCPartyMaster;
import EITLERP.FeltSales.FeltLCMaster.frmFeltLCPartyMaster;
import EITLERP.FeltSales.FeltLRUpdation.clsFeltLRUpdation;
import EITLERP.FeltSales.FeltLRUpdation.frmFeltLRUpdation;
import EITLERP.FeltSales.FeltPDC.clsFeltPDC;
import EITLERP.FeltSales.FeltPDC.frmFeltPDC;
import EITLERP.FeltSales.FeltPDCAmend.clsFeltPDCAmend;
import EITLERP.FeltSales.FeltPDCAmend.frmFeltPDCAmend;
import EITLERP.FeltSales.FeltPartyContact.clsFeltPartyContact;
import EITLERP.FeltSales.FeltPartyContact.frmFeltPartyContact;
import EITLERP.FeltSales.FeltProcessInvoiceVariable.frmFeltProcessInvoiceVariable;
import EITLERP.FeltSales.FeltProcessInvoiceVariable.clsFeltProcessInvoiceVariable;
import EITLERP.FeltSales.FeltWarpingBeamOrder.clsWarpingBeamOrder;
import EITLERP.FeltSales.FeltWarpingBeamOrder.clsWarpingBeamOrderAmend;
import EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrder2;
import EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderAmend;
import EITLERP.FeltSales.GroupCriticalLimitEnhancement.clsFeltGroupCriticalLimitEnhancement;
import EITLERP.FeltSales.LocationAssignment.clsFeltLocationAssignment;
import EITLERP.FeltSales.LocationAssignment.frmFeltLocationAssignment;
import EITLERP.FeltSales.PartyMachineClosure.*;
import EITLERP.FeltSales.PartyMachineReOpen.*;
import EITLERP.FeltSales.PieceAmendmentApproval.clsPieceAmendApproval;
import EITLERP.FeltSales.PieceAmendmentApproval.frmPieceAmendApproval;
import EITLERP.FeltSales.PieceAmendmentApproval_STOCK.clsPieceAmendApproval_STOCK;
import EITLERP.FeltSales.PieceAmendmentApproval_STOCK.frmPieceAmendApproval_STOCK;
import EITLERP.FeltSales.PieceAmendmentDelink.clsPieceAmendDELINK;
import EITLERP.FeltSales.PieceAmendmentDelink.frmPieceAmendDELINK;
import EITLERP.FeltSales.PieceAmendmentWIP.clsPieceAmendWIP;
import EITLERP.FeltSales.PieceAmendmentWIP.frmPieceAmendWIP;
import EITLERP.Production.FeltYearEndDiscountCreditNote.clsFeltYearEndDisc;
import EITLERP.Production.FeltYearEndDiscountCreditNote.frmFeltYearEndDisc;
import EITLERP.Production.YearEndDiscountForm2.clsFeltYearEndDisc2;
import EITLERP.Production.YearEndDiscountForm2.frmFeltYearEndDisc2;
import EITLERP.Sales.DebitMemoReceiptMapping.*;
import EITLERP.FeltSales.FeltPieceAmend.*;
import EITLERP.FeltSales.FeltQualityRateMaster.*;
import EITLERP.FeltSales.FeltTransporterWeight.clsFeltTransporterWeigthEntryForm;
import EITLERP.FeltSales.FeltTransporterWeight.frmFeltTransporterWeigthEntryForm;
import EITLERP.FeltSales.FeltWarpingBeamOrder.clsWarpingBeamOrderHDS;
import EITLERP.FeltSales.FeltWarpingBeamOrder.clsWarpingBeamOrderHDSAmend;
import EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderHDS;
import EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderHDSAmend;
import EITLERP.Production.FeltSeaming.clsFeltSeaming;
import EITLERP.Production.FeltSeaming.frmFeltSeaming;
import EITLERP.FeltSales.GIDC_SDF.Despatch.clsDespatchGIDC_SDML;
import EITLERP.FeltSales.GIDC_SDF.Despatch.frmDespatchGIDC_SDML;
import EITLERP.FeltSales.SDF.ProductionProcess.clsProductionEntry;
import EITLERP.FeltSales.GIDC_SDF.Instruction.*;
import EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_GIDC;
import EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_SDML;
import EITLERP.FeltSales.GIDC_SDF.NRGP.clsNRGP_GIDC;
import EITLERP.FeltSales.GIDC_SDF.NRGP.clsNRGP_SDML;
import EITLERP.FeltSales.SDF.ProductionProcessAmend.clsProductionAmend;
import EITLERP.FeltSales.OrderConfirmation.clsPieceOC;
import EITLERP.FeltSales.TrailPiece.clsTrailPieceDispatchEntry;
import EITLERP.FeltSales.TrailPiece.clsTrailPieceEntry;
import EITLERP.FeltSales.TrailPiece.frmTrailPieceDispatchEntry;
import EITLERP.FeltSales.TrailPiece.frmTrailPieceEntry;
import EITLERP.FeltSales.AutoPI.clsFeltAutoPISelection;
import EITLERP.FeltSales.AutoPI.frmFeltAutoPISelection;
import EITLERP.FeltSales.Budget.FrmNewSalesProjectionEntry;
import EITLERP.FeltSales.Budget.clsNewSalesProjectionEntry;
import EITLERP.FeltSales.ObsoletePieceMapping.clsFeltObsoleteAssignUPN;
import EITLERP.FeltSales.PieceSpilover.clsFeltAssignUPN;
import EITLERP.FeltSales.PreformaceTracking.FrmPerformaceTrackingSheet;
import EITLERP.FeltSales.PreformaceTracking.clsPerformaceTrackingSheet;
import EITLERP.FeltSales.RateEligibility.clsFeltRateEligibility;
import EITLERP.FeltSales.WVG_Prod_Loom_WVR.FrmWVG_Prod_Loom_WVR;
import EITLERP.FeltSales.WVG_Prod_Loom_WVR.clsWVG_Prod_Loom_WVR;
import EITLERP.FeltWH.clsPostInvoiceEntry;
import EITLERP.FeltWH.clsWHInvGatepassEntry;
import EITLERP.FeltWH.frmPostInvoiceEntry;
import EITLERP.FeltWH.frmWHInvGatepassEntry;
import EITLERP.Production.FeltHeatSetting.clsFeltHeatSetting;
import EITLERP.Production.FeltHeatSetting.frmFeltHeatSetting;
import EITLERP.Production.FeltMarking.clsFeltMarking;
import EITLERP.Production.FeltMarking.frmFeltMarking;
import EITLERP.Production.FeltSplicing.clsFeltSplicing;
import EITLERP.Production.FeltSplicing.frmFeltSplicing;
import SDMLATTPAY.Employee.clsEmployeeMaster;
import SDMLATTPAY.IncrementProposal.frmChangeSuperPassword;
import SDMLATTPAY.IncrementProposal.frmSuperPasswordHOD;
import SDMLATTPAY.SUPERANNUATION.FrmSuperannuationProcess;
import SDMLATTPAY.SUPERANNUATION.clsSuperannuationProcess;
import SDMLATTPAY.gatepass.FrmTimeCorrectionEntry;
import SDMLATTPAY.gatepass.clsTimeCorrectionEntry;
import java.sql.ResultSet;

//import EITLERP.Sales.*; 
/**
 *
 * @author nrpithva
 */
public class frmPendingApprovals extends javax.swing.JApplet {

    private int SelModule = 0;
    private int SelDeptID = 0;
    private boolean Continue = true;
    private EITLTableModel DataModel = new EITLTableModel();
    private EITLComboModel cmbModuleModel = new EITLComboModel();
    private EITLComboModel cmbDeptModel = new EITLComboModel();
    private EITLComboModel cmbYearModel = new EITLComboModel();
    private EITLComboModel cmbUserModel;

    private EITLComboModel cmbAuthorityModel = new EITLComboModel();

    private clsExcel_Exporter exp = new clsExcel_Exporter();

    private int SizeCount = 0;
    private boolean DoNotPopulate = false;

    //public static final int defUserID=EITLERPGLOBAL.gUserID;
    public static int defUserID = EITLERPGLOBAL.gUserID;

    private int SorOn = EITLERPGLOBAL.OnDocDate;

    /**
     * Initializes the applet frmPendingApprovals
     */
    public void init() {
        System.gc();
        setSize(895, 522);
        initComponents();
        cmbuser.setVisible(false);
        username.setVisible(false);
        cmbUserold.setVisible(false);
        cmbAuthority.setVisible(false);

        //*************** Financial Year ******************//
        cmbFinYear.setModel(cmbYearModel);
        cmbFinYear.removeAllItems();

        HashMap List = clsFinYear.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);

        for (int i = 1; i <= List.size(); i++) {
            clsFinYear ObjYear = (clsFinYear) List.get(Integer.toString(i));

            ComboData cmbData = new ComboData();
            cmbData.Text = Integer.toString((int) ObjYear.getAttribute("YEAR_FROM").getVal()) + " To " + Integer.toString((int) ObjYear.getAttribute("YEAR_FROM").getVal() + 1);
            cmbData.Code = (int) ObjYear.getAttribute("YEAR_FROM").getVal();
            cmbData.strCode = Integer.toString((int) ObjYear.getAttribute("YEAR_FROM").getVal());
            cmbYearModel.addElement(cmbData);
        }

        int CurFinYear = EITLERPGLOBAL.getCurrentFinYear();
        //EITLERPGLOBAL.setComboIndex(cmbFinYear,CurFinYear);
        EITLERPGLOBAL.setComboIndex(cmbFinYear, EITLERPGLOBAL.FinYearFrom);
        cmbFinYear.setEnabled(true);
        //*************************************************//

        GenerateCombo();
        FormatGridNormal();
        txtUser.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID));

        //GenerateUserCombo();
        cmdOpen1.setVisible(false);

        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 26, 261)) {
            chkUser.setVisible(true);
            cmbUserold.setVisible(false);
            cmbuser.setVisible(false);
            username.setVisible(false);
            //cmdOpen1.setVisible(false);
        } else {
            chkUser.setVisible(false);
            cmbUserold.setVisible(false);
            cmbuser.setVisible(false);
            username.setVisible(false);
            //cmdOpen1.setVisible(false);
        }

    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        cmbModule = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdOpen = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        cmdShow = new javax.swing.JButton();
        cmdRefresh = new javax.swing.JButton();
        cmdOpen1 = new javax.swing.JButton();
        chkUser = new javax.swing.JCheckBox();
        cmbDept = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        OpgDocDate = new javax.swing.JRadioButton();
        OpgRecDate = new javax.swing.JRadioButton();
        OpgDocNo = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        chkDept = new javax.swing.JCheckBox();
        cmbUserold = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        cmbFinYear = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        lblProcess = new javax.swing.JLabel();
        btnExportToExcel = new javax.swing.JButton();
        cmbuser = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        cmbAuthority = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        OpgDefaultUser = new javax.swing.JRadioButton();
        OpgAuthDelUser = new javax.swing.JRadioButton();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText(" PENDING APPROVALS");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(-2, 0, 940, 25);

        jLabel2.setText("User");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 38, 80, 16);

        jLabel3.setText("Select Module ");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(9, 155, 110, 16);

        txtUser.setEditable(false);
        txtUser.setNextFocusableComponent(cmbModule);
        getContentPane().add(txtUser);
        txtUser.setBounds(108, 36, 200, 18);

        cmbModule.setNextFocusableComponent(cmdShow);
        cmbModule.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbModuleItemStateChanged(evt);
            }
        });
        getContentPane().add(cmbModule);
        cmbModule.setBounds(120, 150, 303, 28);

        Table.setModel(new javax.swing.table.DefaultTableModel(
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
        Table.setNextFocusableComponent(cmdOpen);
        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 269, 530, 214);

        cmdOpen.setText("Open Document");
        cmdOpen.setNextFocusableComponent(cmdExit);
        cmdOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOpenActionPerformed(evt);
            }
        });
        getContentPane().add(cmdOpen);
        cmdOpen.setBounds(560, 270, 150, 28);

        cmdExit.setText("Exit");
        cmdExit.setNextFocusableComponent(cmbModule);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        getContentPane().add(cmdExit);
        cmdExit.setBounds(560, 450, 150, 28);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 220, 940, 10);

        cmdShow.setText("Show Pending Documents");
        cmdShow.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdShow.setNextFocusableComponent(Table);
        cmdShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowActionPerformed(evt);
            }
        });
        getContentPane().add(cmdShow);
        cmdShow.setBounds(280, 190, 210, 32);

        cmdRefresh.setText("Refresh");
        cmdRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(cmdRefresh);
        cmdRefresh.setBounds(350, 120, 120, 28);

        cmdOpen1.setText("Open All Pending");
        cmdOpen1.setNextFocusableComponent(cmdExit);
        cmdOpen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOpen1ActionPerformed(evt);
            }
        });
        getContentPane().add(cmdOpen1);
        cmdOpen1.setBounds(560, 310, 150, 28);

        chkUser.setText("Other user");
        chkUser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkUserItemStateChanged(evt);
            }
        });
        getContentPane().add(chkUser);
        chkUser.setBounds(0, 90, 110, 23);

        cmbDept.setEnabled(false);
        getContentPane().add(cmbDept);
        cmbDept.setBounds(118, 236, 220, 28);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(null);

        buttonGroup1.add(OpgDocDate);
        OpgDocDate.setSelected(true);
        OpgDocDate.setText("Document Date");
        jPanel2.add(OpgDocDate);
        OpgDocDate.setBounds(4, 27, 135, 23);

        buttonGroup1.add(OpgRecDate);
        OpgRecDate.setText("Received Date");
        jPanel2.add(OpgRecDate);
        OpgRecDate.setBounds(4, 47, 126, 23);

        buttonGroup1.add(OpgDocNo);
        OpgDocNo.setText("Document No.");
        jPanel2.add(OpgDocNo);
        OpgDocNo.setBounds(4, 68, 123, 23);

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Sort results on");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(6, 5, 124, 17);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(670, 32, 180, 100);

        chkDept.setText("Department");
        chkDept.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDeptItemStateChanged(evt);
            }
        });
        getContentPane().add(chkDept);
        chkDept.setBounds(9, 236, 105, 23);

        cmbUserold.setEnabled(false);
        getContentPane().add(cmbUserold);
        cmbUserold.setBounds(850, 190, 80, 28);

        jLabel5.setText("Search:");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(360, 240, 70, 16);

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        getContentPane().add(txtSearch);
        txtSearch.setBounds(440, 240, 100, 28);
        getContentPane().add(cmbFinYear);
        cmbFinYear.setBounds(108, 59, 140, 28);

        jLabel6.setText("Fin. Year");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(9, 66, 90, 16);

        lblProcess.setText("...");
        getContentPane().add(lblProcess);
        lblProcess.setBounds(500, 196, 230, 20);

        btnExportToExcel.setText("Export to Excel");
        btnExportToExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportToExcelActionPerformed(evt);
            }
        });
        getContentPane().add(btnExportToExcel);
        btnExportToExcel.setBounds(560, 380, 150, 28);

        cmbuser = new JTextFieldHint(new JTextField(),"Press F1");
        cmbuser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbuserKeyPressed(evt);
            }
        });
        getContentPane().add(cmbuser);
        cmbuser.setBounds(110, 90, 70, 30);
        getContentPane().add(username);
        username.setBounds(180, 90, 220, 30);
        getContentPane().add(cmbAuthority);
        cmbAuthority.setBounds(420, 90, 220, 28);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        buttonGroup2.add(OpgDefaultUser);
        OpgDefaultUser.setSelected(true);
        OpgDefaultUser.setText("Default User");
        OpgDefaultUser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgDefaultUserItemStateChanged(evt);
            }
        });
        jPanel3.add(OpgDefaultUser);
        OpgDefaultUser.setBounds(10, 0, 150, 23);

        buttonGroup2.add(OpgAuthDelUser);
        OpgAuthDelUser.setText("Authority Delegation");
        OpgAuthDelUser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgAuthDelUserItemStateChanged(evt);
            }
        });
        jPanel3.add(OpgAuthDelUser);
        OpgAuthDelUser.setBounds(10, 20, 180, 23);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(420, 32, 220, 50);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbModuleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbModuleItemStateChanged
        // TODO add your handling code here:
        lblProcess.setText("...");
    }//GEN-LAST:event_cmbModuleItemStateChanged

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        try {
            String txtData = txtSearch.getText();
            String txtTableData = "";
            int Rows = Table.getModel().getRowCount();
            int DefaultSearchOn = 1;
            if (txtData.equals("")) {
                Table.changeSelection(0, 0, false, false);
                return;
            }
            for (int i = 0; i < Rows; i++) {
                //Read the table data
                txtTableData = (String) Table.getModel().getValueAt(i, 0);

                //Compare with partial search
                if (txtData.length() > txtTableData.length()) {
                } else {
                    if (txtTableData.substring(0, txtData.length()).toLowerCase().equals(txtData.toLowerCase())) {
                        //Move the row pointer to selected row
                        int row = i;
                        int col = DefaultSearchOn - 1;
                        boolean toggle = false;
                        boolean extend = false;
                        Table.changeSelection(row, col, toggle, extend);

                        //Exit the loop
                        i = Table.getModel().getRowCount();
                    }
                }
            }
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>((DefaultTableModel) Table.getModel());

            try {
                sorter.setRowFilter(RowFilter.regexFilter(txtData, 0));
            } catch (Exception e) {
                System.out.println("Pattern Matching Error");
            }

            Table.setRowSorter(sorter);

            DataModel = (EITLTableModel) sorter.getModel();

            Table.setModel(DataModel);
            if (Table.getRowCount() <= 0) {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txtData, 1));
                } catch (Exception e1) {
                    System.out.println("Pattern Matching Error");
                }
                Table.setRowSorter(sorter);
                DataModel = (EITLTableModel) sorter.getModel();
                Table.setModel(DataModel);
            }
            Table.changeSelection(0, 0, false, false);

//            int CurrentCol = Table.getSelectedColumn();
//            String searchText = txtSearch.getText();
//
//            for (int i = 0; i < Table.getRowCount(); i++) {
//                String TableValue = (String) Table.getValueAt(i, CurrentCol);
//
//                if (TableValue.substring(0, searchText.length()).toUpperCase().equals(searchText.toUpperCase())) {
//                    Table.changeSelection(i, CurrentCol, false, false);
//                    return;
//                }
//            }
        } catch (Exception e) {

        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void chkDeptItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDeptItemStateChanged
        // TODO add your handling code here:
        cmbDept.setEnabled(chkDept.isSelected());
    }//GEN-LAST:event_chkDeptItemStateChanged

    private void chkUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkUserItemStateChanged
        // TODO add your handling code here:
        if (chkUser.isSelected()) {
            cmbuser.setVisible(true);
            username.setVisible(true);
            GenerateUserCombo();
        } else {
            cmbuser.setVisible(false);
            username.setVisible(false);
        }
        cmbUserold.setEnabled(chkUser.isSelected());

        if (!chkUser.isSelected()) {
            GenerateCombo();
        } else {
            EITLERPGLOBAL.setComboIndex(cmbUserold, EITLERPGLOBAL.gUserID);
        }
    }//GEN-LAST:event_chkUserItemStateChanged

    private void cmdOpen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOpen1ActionPerformed
        // TODO add your handling code here:
        String DocNo = "";

        if (Table.getRowCount() > 0) {

            if (SelModule == 878) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt PPRS Planning");
                aFrame.startAppletEx("EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry", "Felt PPRS Planning");
                EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry ObjItem = (EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 876) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Zone Master");
                aFrame.startAppletEx("EITLERP.FeltSales.ZoneMaster.FrmZoneMaster", "Zone Master");
                EITLERP.FeltSales.ZoneMaster.FrmZoneMaster ObjDoc = (EITLERP.FeltSales.ZoneMaster.FrmZoneMaster) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                //ObjDoc.Find(DocNo);
                ObjDoc.FindWaiting();
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 877) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Zone Party Selection");
                aFrame.startAppletEx("EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection", "Zone Party Selection");
                EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection ObjDoc = (EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                //ObjDoc.Find(DocNo);
                ObjDoc.FindWaiting();
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 206) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Authority Delegation Request");
                aFrame.startAppletEx("EITLERP.AuthorityDelegation.FrmAuthorityDelegationRequest", "Authority Delegation Request");
                EITLERP.AuthorityDelegation.FrmAuthorityDelegationRequest ObjDoc = (EITLERP.AuthorityDelegation.FrmAuthorityDelegationRequest) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                //ObjDoc.Find(DocNo);
                ObjDoc.FindWaiting();
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 872) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Budget Entry[New]");
                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmNewBudgetEntry", "Felt Budget Entry[New]");
                EITLERP.FeltSales.Budget.FrmNewBudgetEntry ObjItem = (EITLERP.FeltSales.Budget.FrmNewBudgetEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 868) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Daily Rokdi Attendance (Regular)");
                aFrame.startAppletEx("SDMLATTPAY.DailyRokdiAttData.frmDailyRokdiAttData", "Daily Rokdi Attendance (Regular)");
                SDMLATTPAY.DailyRokdiAttData.frmDailyRokdiAttData ObjDoc = (SDMLATTPAY.DailyRokdiAttData.frmDailyRokdiAttData) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 865) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Excess Manpower Requisition");
                aFrame.startAppletEx("SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm", "Excess Manpower Requisition");
                SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm ObjDoc = (SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 866) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Shift Schedule Change");
                aFrame.startAppletEx("SDMLATTPAY.AdvanceShiftChange.FrmShiftScheduleChange", "Shift Schedule");
                SDMLATTPAY.AdvanceShiftChange.FrmShiftScheduleChange ObjDoc = (SDMLATTPAY.AdvanceShiftChange.FrmShiftScheduleChange) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 863) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Shift Schedule");
                aFrame.startAppletEx("SDMLATTPAY.AdvanceShiftChange.FrmShiftSchedule", "Shift Schedule");
                SDMLATTPAY.AdvanceShiftChange.FrmShiftSchedule ObjDoc = (SDMLATTPAY.AdvanceShiftChange.FrmShiftSchedule) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 862) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Selection");
                aFrame.startAppletEx("SDMLATTPAY.RokdiSelection.frmRokdiSelection", "Rokdi Selection");
                SDMLATTPAY.RokdiSelection.frmRokdiSelection ObjDoc = (SDMLATTPAY.RokdiSelection.frmRokdiSelection) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 860) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Travelling Voucher");
                aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelVoucher", "Travelling Voucher");
                SDMLATTPAY.TravelAdvance.FrmTravelVoucher ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelVoucher) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 859) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Travelling Sanction Amendment");
                aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend", "Travelling Sanction Amendment");
                SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 858) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Travelling Sanction");
                aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelEntry", "Travelling Sanction");
                SDMLATTPAY.TravelAdvance.FrmTravelEntry ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 856) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Retainer Entry");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmCoffRetainerEntry", "Retainer Entry");
                SDMLATTPAY.COFF.FrmCoffRetainerEntry ObjDoc = (SDMLATTPAY.COFF.FrmCoffRetainerEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 855) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Spillover Rescheduling");
                aFrame.startAppletEx("EITLERP.FeltSales.SpilloverRescheduling.frmInStockSpillover", "Spillover Rescheduling");
                EITLERP.FeltSales.SpilloverRescheduling.frmInStockSpillover ObjDoc = (EITLERP.FeltSales.SpilloverRescheduling.frmInStockSpillover) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 786) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Beam Order Closure");
                aFrame.startAppletEx("EITLERP.FeltSales.BeamOrderClosure.frmBeamOrderClosure", "Beam Order Closure");
                EITLERP.FeltSales.BeamOrderClosure.frmBeamOrderClosure ObjItem = (EITLERP.FeltSales.BeamOrderClosure.frmBeamOrderClosure) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 851) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Machine Run Forcasting");
                aFrame.startAppletEx("EITLERP.FeltSales.MachineRunForcasting.frmMachineRunForcastingEntry", "Machine Run Forcasting");
                EITLERP.FeltSales.MachineRunForcasting.frmMachineRunForcastingEntry ObjDoc = (EITLERP.FeltSales.MachineRunForcasting.frmMachineRunForcastingEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 850) {
                AppletFrame aFrame = new AppletFrame("SDML SDF Spiralling Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.SDF.FeltSpiral.frmFeltSpiralling", "SDML SDF Spiralling Entry");
                EITLERP.FeltSales.SDF.FeltSpiral.frmFeltSpiralling ObjItem = (EITLERP.FeltSales.SDF.FeltSpiral.frmFeltSpiralling) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 635) {
                AppletFrame aFrame = new AppletFrame("Post Audit Disc Rate Master");
                aFrame.startAppletEx("EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster", "Post Audit Disc Rate Master");
                EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster ObjItem = (EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 204) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Payment Requisition Slip");
                aFrame.startAppletEx("EITLERP.Stores.frmPaymentRequisition", "Payment Requisition Slip");
                EITLERP.Stores.frmPaymentRequisition ObjDoc = (EITLERP.Stores.frmPaymentRequisition) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 846) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Beam Gaiting Status");
                aFrame.startAppletEx("EITLERP.Production.BeamGaitingStatus.FrmBeamGaitingStatus", "Felt Beam Gaiting Status");
                EITLERP.Production.BeamGaitingStatus.FrmBeamGaitingStatus ObjDoc = (EITLERP.Production.BeamGaitingStatus.FrmBeamGaitingStatus) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 842) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Request Form");
                aFrame.startAppletEx("SDMLATTPAY.RokdiRequestAmend.frmRokdiRequestAmendForm", "Rokdi Request Amendment");
                SDMLATTPAY.RokdiRequestAmend.frmRokdiRequestAmendForm ObjDoc = (SDMLATTPAY.RokdiRequestAmend.frmRokdiRequestAmendForm) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 839) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Request Form");
                aFrame.startAppletEx("SDMLATTPAY.RokdiRequest.frmRokdiRequestForm", "Rokdi Request Form");
                SDMLATTPAY.RokdiRequest.frmRokdiRequestForm ObjDoc = (SDMLATTPAY.RokdiRequest.frmRokdiRequestForm) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 837) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Daily Attendance (Regular)");
                aFrame.startAppletEx("SDMLATTPAY.DailyAttDataForm.frmDailyAttDataForm", "Daily Attendance (Regular)");
                SDMLATTPAY.DailyAttDataForm.frmDailyAttDataForm ObjDoc = (SDMLATTPAY.DailyAttDataForm.frmDailyAttDataForm) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 834) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Budget Review Entry Form");
                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmNewSalesProjectionEntry", "Budget Review Entry Form");
                FrmNewSalesProjectionEntry ObjDoc = (FrmNewSalesProjectionEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 634) {
                AppletFrame aFrame = new AppletFrame("Felt Party Contact Detail Upadtion");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPartyContact.frmFeltPartyContact", "Felt Party Contact Detail Upadtion");
                frmFeltPartyContact ObjItem = (frmFeltPartyContact) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 831) {
                AppletFrame aFrame = new AppletFrame("Superannuation");
                aFrame.startAppletEx("SDMLATTPAY.SUPERANNUATION.FrmSuperannuationProcess", "Superannuation");
                FrmSuperannuationProcess ObjItem = (FrmSuperannuationProcess) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 615) {
                AppletFrame aFrame = new AppletFrame("Felt Invoice Covering Letter");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltInvCvrLtr.frmFeltInvCvrLtr", "Felt Invoice Covering Letter");
                frmFeltInvCvrLtr ObjItem = (frmFeltInvCvrLtr) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 830) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Miss Punch");
                aFrame.startAppletEx("SDMLATTPAY.MissedPunchRequest.FrmMissedPunchRequest", "Miss Punch");
                SDMLATTPAY.MissedPunchRequest.FrmMissedPunchRequest ObjDoc = (SDMLATTPAY.MissedPunchRequest.FrmMissedPunchRequest) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 829) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Contract Rokdi Process");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmRokdiCProcess", "Contract Rokdi Process");
                SDMLATTPAY.COFF.FrmRokdiCProcess ObjDoc = (SDMLATTPAY.COFF.FrmRokdiCProcess) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 827) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Contract Rokdi Entry");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmRokdiCEntry", "Contract Rokdi Entry");
                SDMLATTPAY.COFF.FrmRokdiCEntry ObjDoc = (SDMLATTPAY.COFF.FrmRokdiCEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 818) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Coff Process");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmCoffProcess", "Coff Process");
                SDMLATTPAY.COFF.FrmCoffProcess ObjDoc = (SDMLATTPAY.COFF.FrmCoffProcess) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 817) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Process");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmRokdiProcess", "Rokdi Process");
                SDMLATTPAY.COFF.FrmRokdiProcess ObjDoc = (SDMLATTPAY.COFF.FrmRokdiProcess) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 814) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Employee Master Amend");
                aFrame.startAppletEx("SDMLATTPAY.EmployeeAmend.FrmEmployeeMasterAmend", "Employee Master Amend Entry");
                SDMLATTPAY.EmployeeAmend.FrmEmployeeMasterAmend ObjDoc = (SDMLATTPAY.EmployeeAmend.FrmEmployeeMasterAmend) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 812) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Employee Master");
                aFrame.startAppletEx("SDMLATTPAY.Employee.FrmEmployeeMaster", "Employee Master Entry");
                SDMLATTPAY.Employee.FrmEmployeeMaster ObjDoc = (SDMLATTPAY.Employee.FrmEmployeeMaster) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 816) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Entry");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmRokdiEntry", "Rokdi Entry");
                SDMLATTPAY.COFF.FrmRokdiEntry ObjDoc = (SDMLATTPAY.COFF.FrmRokdiEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 815) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Coff Entry");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmCoffEntry", "Coff Entry");
                SDMLATTPAY.COFF.FrmCoffEntry ObjDoc = (SDMLATTPAY.COFF.FrmCoffEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 826) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Monthly Attendance Data");
                aFrame.startAppletEx("SDMLATTPAY.MonthlyAttendance.frmMonthlyAttendance", "Monthly Attendance Data");
                SDMLATTPAY.MonthlyAttendance.frmMonthlyAttendance ObjDoc = (SDMLATTPAY.MonthlyAttendance.frmMonthlyAttendance) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 823) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Special Deduction Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmSpecialDeductionEntry", "Special Deduction Entry");
                SDMLATTPAY.gatepass.FrmSpecialDeductionEntry ObjDoc = (SDMLATTPAY.gatepass.FrmSpecialDeductionEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 632) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("PO Details Updation");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO", "PO Details Updation");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 631) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Special Rate Sanction Eligibility");
                aFrame.startAppletEx("EITLERP.FeltSales.RateEligibility.frmFeltRateEligibility", "Special Rate Sanction Eligibility");
                EITLERP.FeltSales.RateEligibility.frmFeltRateEligibility ObjItem = (EITLERP.FeltSales.RateEligibility.frmFeltRateEligibility) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 804) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Shift Change");
                aFrame.startAppletEx("SDMLATTPAY.Shift.FrmShiftSchChange", "Shift Change");
                SDMLATTPAY.Shift.FrmShiftSchChange ObjItem = (SDMLATTPAY.Shift.FrmShiftSchChange) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 806) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Shift Upload");
                aFrame.startAppletEx("SDMLATTPAY.Shift.FrmShiftUpload", "Shift Upload");
                SDMLATTPAY.Shift.FrmShiftUpload ObjItem = (SDMLATTPAY.Shift.FrmShiftUpload) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 813) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Leave Updation Entry");
                aFrame.startAppletEx("SDMLATTPAY.leave.FrmLeaveUpdation", "Leave Updation Entry");
                SDMLATTPAY.leave.FrmLeaveUpdation ObjItem = (SDMLATTPAY.leave.FrmLeaveUpdation) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 811) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Leave Application Entry");
                aFrame.startAppletEx("SDMLATTPAY.leave.FrmLeaveApplication", "Leave Application Entry");
                SDMLATTPAY.leave.FrmLeaveApplication ObjItem = (SDMLATTPAY.leave.FrmLeaveApplication) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 808) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Holiday WeekOff Entry");
                aFrame.startAppletEx("SDMLATTPAY.holiday.FrmHoliday", "Holiday WeekOff Entry");
                SDMLATTPAY.holiday.FrmHoliday ObjItem = (SDMLATTPAY.holiday.FrmHoliday) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 805) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Gatepass Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmGatepass", "Gatepass Entry");
                SDMLATTPAY.gatepass.FrmGatepass ObjItem = (SDMLATTPAY.gatepass.FrmGatepass) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 822) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Update Punch Date Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmUpdatePunchDateEntry", "Update Punch Date Entry");
                SDMLATTPAY.gatepass.FrmUpdatePunchDateEntry ObjDoc = (SDMLATTPAY.gatepass.FrmUpdatePunchDateEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 819) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Special Sanction Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmSpecialSanctionEntry", "Special Sanction Entry");
                SDMLATTPAY.gatepass.FrmSpecialSanctionEntry ObjDoc = (SDMLATTPAY.gatepass.FrmSpecialSanctionEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 810) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Time Correction Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmTimeCorrectionEntry", "Time Correction Entry");
                FrmTimeCorrectionEntry ObjDoc = (FrmTimeCorrectionEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 802) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt WH Post Invoice Data");
                aFrame.startAppletEx("EITLERP.FeltWH.frmPostInvoiceEntry", "Felt WH Post Invoice Data");
                frmPostInvoiceEntry ObjDoc = (frmPostInvoiceEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 803) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt WH Gatepass");
                aFrame.startAppletEx("EITLERP.FeltWH.frmWHInvGatepassEntry", "Felt WH Gatepass");
                frmWHInvGatepassEntry ObjDoc = (frmWHInvGatepassEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 630) {
                AppletFrame aFrame = new AppletFrame("Felt Auto PI Selection");
                aFrame.startAppletEx("EITLERP.FeltSales.AutoPI.frmFeltAutoPISelection", "Felt Auto PI Selection");
                frmFeltAutoPISelection ObjItem = (frmFeltAutoPISelection) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == clsTrailPieceEntry.ModuleID) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Trial Piece Selection");
                aFrame.startAppletEx("EITLERP.FeltSales.TrailPiece.frmTrailPieceEntry", "Felt Trial Piece Selection");
                frmTrailPieceEntry ObjItem = (frmTrailPieceEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }
            if (SelModule == clsTrailPieceDispatchEntry.ModuleID) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Trial Piece Post Dispatch Detail Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.TrailPiece.frmTrailPieceDispatchEntry", "Felt Trial Piece Post Dispatch Detail Entry");
                frmTrailPieceDispatchEntry ObjItem = (frmTrailPieceDispatchEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 794) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("NON RETURNABLE GATEPASS SDML To GIDC");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_SDML", "NON RETURNABLE GATEPASS SDML To GIDC");
                FrmNRGP_SDML ObjItem = (FrmNRGP_SDML) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 793) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("NON RETURNABLE GATEPASS GIDC To SDML");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_GIDC", "NON RETURNABLE GATEPASS GIDC To SDML");
                FrmNRGP_GIDC ObjItem = (FrmNRGP_GIDC) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == clsGIDCInstruction.ModuleID) {
                AppletFrame aFrame = new AppletFrame("SDML SDF Instruction Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Instruction.frmGIDCInstruction", "SDML SDF Instruction Entry");
                frmGIDCInstruction ObjItem = (frmGIDCInstruction) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == clsGIDCInstructionAmend.ModuleID) {
                AppletFrame aFrame = new AppletFrame("SDML SDF Instruction Amendment Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Instruction.frmGIDCInstructionAmend", "SDML SDF Instruction Amendment Entry");
                frmGIDCInstructionAmend ObjItem = (frmGIDCInstructionAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 792) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("GIDC Despatch of SDF to SDML");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Despatch.frmDespatchGIDC_SDML", "GIDC Despatch of SDF to SDML");
                frmDespatchGIDC_SDML ObjItem = (frmDespatchGIDC_SDML) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 626) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt PDC Amendment");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPDCAmend.frmFeltPDCAmend", "Felt PDC Amendment");
                frmFeltPDCAmend ObjItem = (frmFeltPDCAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 625) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt PDC Entry Form");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPDC.frmFeltPDC", "Felt PDC Entry Form");
                frmFeltPDC ObjItem = (frmFeltPDC) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 764) {
                AppletFrame aFrame = new AppletFrame("Felt Rejection");
                aFrame.startAppletEx("EITLERP.Production.FeltRejection.frmFeltRejection", "Felt Rejection");
                frmFeltRejection ObjItem = (frmFeltRejection) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == clsWarpingBeamOrderHDS.ModuleID) {
                AppletFrame aFrame = new AppletFrame("Warping Beam Order Entry [HDS]");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderHDS", "Warping Beam Order Entry [HDS]");
                frmWarpingBeamOrderHDS ObjItem = (frmWarpingBeamOrderHDS) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == clsWarpingBeamOrderHDSAmend.ModuleID) {
                AppletFrame aFrame = new AppletFrame("Warping Beam Order Entry Amendment [HDS]");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderHDSAmend", "Warping Beam Order Entry Amendment[HDS]");
                frmWarpingBeamOrderHDSAmend ObjItem = (frmWarpingBeamOrderHDSAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 785) {
                AppletFrame aFrame = new AppletFrame("Felt Seaming Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltSeaming.frmFeltSeaming", "Felt Seaming Entry");
                frmFeltSeaming ObjItem = (frmFeltSeaming) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 601) {
                AppletFrame aFrame = new AppletFrame("Felt Quality Rate Master");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltQualityRateMaster.frmFeltQltRateMaster", "Felt Rate Master");
                frmFeltQltRateMaster ObjItem = (frmFeltQltRateMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 621) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("WIP Piece Amendment");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP", "WIP Piece Amendment");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 622) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("STOCK Piece Tagging");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK", "STOCK Piece Tagging");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 623) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Piece Cancellation");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR", "Piece Cancellation");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 624) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Piece Transfer to Export");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT", "Piece Transfer to Export");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 614) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Sales LR Updation");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltLRUpdation.frmFeltLRUpdation", "Felt Sales LR Updation");
                frmFeltLRUpdation ObjItem = (frmFeltLRUpdation) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 612) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Evaluation ReOpen");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltEvaluation.frmFeltEvaluationReOpen", "Felt Evaluation ReOpen");
                frmFeltEvaluationReOpen ObjItem = (frmFeltEvaluationReOpen) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 611) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Evaluation Closure");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltEvaluation.frmFeltEvaluationClosure", "Felt Evaluation Closure");
                frmFeltEvaluationClosure ObjItem = (frmFeltEvaluationClosure) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 609) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("ReOpen Party Machine Position");
                aFrame.startAppletEx("EITLERP.FeltSales.PartyMachineReOpen.frmPartyMachineReOpen", "ReOpen Party Machine Position");
                frmPartyMachineReOpen ObjItem = (frmPartyMachineReOpen) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 608) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Party Machine Position Closure");
                aFrame.startAppletEx("EITLERP.FeltSales.PartyMachineClosure.frmPartyMachineClosure", "Party Machine Position Closure");
                frmPartyMachineClosure ObjItem = (frmPartyMachineClosure) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 100) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Debit Memo Cancellation");
                aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDMwithoutDNCancellation", "Debit Memo Cancellation");
                frmDMwithoutDNCancellation ObjDoc = (frmDMwithoutDNCancellation) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 13) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Declaration Form");
                aFrame.startAppletEx("EITLERP.Stores.frmDeclarationForm", "Declaration Form");
                frmDeclarationForm ObjItem = (frmDeclarationForm) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 52) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue Requisition");
                aFrame.startAppletEx("EITLERP.Purchase.frmIssueReq", "Issue Requisition");
                frmIssueReq ObjItem = (frmIssueReq) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 171) {
                AppletFrame aFrame = new AppletFrame("LC Party Master Module");
                aFrame.startAppletEx("EITLERP.Sales.frmLCPartyMasterUpdated", "LC Party Master Module");
                frmLCPartyMasterUpdated ObjItem = (frmLCPartyMasterUpdated) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                //ObjItem.PENDING_DOCUMENT=true;
            }

            if (SelModule == 606) {
                AppletFrame aFrame = new AppletFrame("Felt Sales LC Party Master");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltLCMaster.frmFeltLCPartyMaster", "Felt Sales LC Party Master");
                frmFeltLCPartyMaster ObjItem = (frmFeltLCPartyMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                //ObjItem.PENDING_DOCUMENT=true;
            }

            if (SelModule == 46) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Service Contract");
                aFrame.startAppletEx("EITLERP.Purchase.frmServiceContract", "Service Contract");
                frmServiceContract ObjItem = (frmServiceContract) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 47) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Service Contract Amendment");
                aFrame.startAppletEx("EITLERP.Purchase.frmServiceContractAmend", "Service Contract Amendment");
                frmServiceContractAmend ObjItem = (frmServiceContractAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 48) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Job work entry");
                aFrame.startAppletEx("EITLERP.Stores.frmJobwork", "Job work entry");
                frmJobwork ObjItem = (frmJobwork) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 50) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Supplier Master Updation");
                aFrame.startAppletEx("EITLERP.frmSupplierAmend", "Supplier Master Updation");
                frmSupplierAmend ObjItem = (frmSupplierAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == 51) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Item Master Updation");
                aFrame.startAppletEx("EITLERP.frmItemAmend", "Item Master Updation");
                frmItemAmend ObjItem = (frmItemAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            //-----*
            if (SelModule == 1) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Item");
                aFrame.startAppletEx("EITLERP.frmItem", "Item Master");
                frmItem ObjItem = (frmItem) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,lItemID);
                ObjItem.FindWaiting();
            }

            //-----*
            if (SelModule == 37) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Supplier");
                aFrame.startAppletEx("EITLERP.frmSupplier", "Supplier");
                frmSupplier ObjItem = (frmSupplier) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,lItemID);
                ObjItem.FindWaiting();
            }

            if (SelModule == 44) {
                int ModuleID = Integer.parseInt(Table.getValueAt(Table.getSelectedRow(), 4).toString());
                if (ModuleID == 80) {
                    String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                    AppletFrame aFrame = new AppletFrame("Invoice Cancellation Request");
                    aFrame.startAppletEx("EITLERP.frmInvoiceCancel", "Invoice Cancellation Request");
                    frmInvoiceCancel ObjItem = (frmInvoiceCancel) aFrame.ObjApplet;
                    ObjItem.frmPA = this;
                    ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
                } else {
                    String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                    AppletFrame aFrame = new AppletFrame("Document Cancel Request");
                    aFrame.startAppletEx("EITLERP.frmDocCancel", "Document Cancel Request");
                    frmDocCancel ObjItem = (frmDocCancel) aFrame.ObjApplet;
                    ObjItem.frmPA = this;
                    ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
                }
                //                String lItemID=(String)Table.getValueAt(Table.getSelectedRow(), 0);
                //                AppletFrame aFrame=new AppletFrame("Document Cancel Request");
                //                aFrame.startAppletEx("EITLERP.frmDocCancel","Document Cancel Request");
                //                frmDocCancel ObjItem=(frmDocCancel) aFrame.ObjApplet;
                //                ObjItem.frmPA=this;
                //                //ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,lItemID);
                //                ObjItem.FindWaiting();
            }

            //----*
            if (SelModule == 2) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Material Requisition");
                aFrame.startAppletEx("EITLERP.Stores.frmMR", "Material Requisition");
                frmMR ObjDoc = (frmMR) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.FindWaiting();
            }

            //----*
            if (SelModule == 42) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Freight Comparison");
                aFrame.startAppletEx("EITLERP.Purchase.frmFreightComparison", "Freight Comparison");
                frmFreightComparison ObjDoc = (frmFreightComparison) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //----*
            if (SelModule == 43) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Freight Calculation");
                aFrame.startAppletEx("EITLERP.Purchase.frmFreightCalculation", "Freight Calculation");
                frmFreightCalculation ObjDoc = (frmFreightCalculation) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //-----*
            if (SelModule == 20) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Summary");
                aFrame.startAppletEx("EITLERP.Purchase.frmQuotApproval", "Quot Approval");
                frmQuotApproval ObjDoc = (frmQuotApproval) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //------*
            if (SelModule == 11) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("NRGP");
                aFrame.startAppletEx("EITLERP.Stores.FrmNRGP_General", "NRGP");
                FrmNRGP_General ObjDoc = (FrmNRGP_General) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //-----*
            if (SelModule == 12) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("RGP");
                aFrame.startAppletEx("EITLERP.Stores.FrmRGP_General", "RGP");
                FrmRGP_General ObjDoc = (FrmRGP_General) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //======*
            if (SelModule == 14) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue");
                aFrame.startAppletEx("EITLERP.Stores.FrmIssue_General", "Issue");
                FrmIssue_General ObjDoc = (FrmIssue_General) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=====*
            if (SelModule == 15) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue");
                aFrame.startAppletEx("EITLERP.Stores.FrmIssue_Raw", "Issue");
                FrmIssue_Raw ObjDoc = (FrmIssue_Raw) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //==*
            if (SelModule == 112) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue RMG Sales");
                aFrame.startAppletEx("EITLERP.Stores.FrmIssue_RMG", "Issue RMG Sales");
                FrmIssue_RMG ObjDoc = (FrmIssue_RMG) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 800) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue RMG Sales");
                aFrame.startAppletEx("EITLERP.Stores.FrmIssue_Storesales", "Issue Stores Sales");
                FrmIssue_Storesales ObjDoc = (FrmIssue_Storesales) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //==*
            if (SelModule == 3) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Indent");
                aFrame.startAppletEx("EITLERP.Stores.FrmIndent", "Indent");
                FrmIndent ObjDoc = (FrmIndent) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //========*
            if (SelModule == 40) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("RGP Return");
                aFrame.startAppletEx("EITLERP.Stores.frmRGPReturn", "RGP Return");
                frmRGPReturn ObjDoc = (frmRGPReturn) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=====*
            if (SelModule == 18) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Inquiry");
                aFrame.startAppletEx("EITLERP.Purchase.FrmInquiry", "Inquiry");
                FrmInquiry ObjDoc = (FrmInquiry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //------ *
            if (SelModule == 4) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Gatepass Requisition");
                aFrame.startAppletEx("EITLERP.Stores.frmGPR", "Gatepass Requisition");
                frmGPR ObjDoc = (frmGPR) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //======*
            if (SelModule == 5) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("MIR General");
                aFrame.startAppletEx("EITLERP.Stores.frmMIRGen", "MIR General");
                frmMIRGen ObjDoc = (frmMIRGen) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //======*
            if (SelModule == 6) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("MIR Raw Material");
                aFrame.startAppletEx("EITLERP.Stores.frmMIR", "MIR Raw Material");
                frmMIR ObjDoc = (frmMIR) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 35) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("MIR Service");
                aFrame.startAppletEx("EITLERP.Stores.frmMIRService", "MIR Service");
                frmMIRService ObjDoc = (frmMIRService) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=====*
            if (SelModule == 7) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("GRN General");
                aFrame.startAppletEx("EITLERP.Stores.frmGRNGen", "GRN General");
                frmGRNGen ObjDoc = (frmGRNGen) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //==========*
            if (SelModule == 8) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("GRN Raw Material");
                aFrame.startAppletEx("EITLERP.Stores.frmGRN", "GRN Raw Material");
                frmGRN ObjDoc = (frmGRN) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=====*
            if (SelModule == 9) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("RJN General");
                aFrame.startAppletEx("EITLERP.Stores.frmRJNGen", "RJN General");
                frmRJNGen ObjDoc = (frmRJNGen) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //======*
            if (SelModule == 10) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("RJN Raw Material");
                aFrame.startAppletEx("EITLERP.Stores.frmRJN", "RJN Raw Material");
                frmRJN ObjDoc = (frmRJN) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //======*
            if (SelModule == 21) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 1;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 153) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 9;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 46) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmServiceContract", "Service Contract");
                frmServiceContract ObjDoc = (frmServiceContract) aFrame.ObjApplet;
                ObjDoc.POType = 8;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //===== *
            if (SelModule == 22) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 2;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //====*
            if (SelModule == 23) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 3;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //====*
            if (SelModule == 24) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 4;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=====*
            if (SelModule == 25) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 5;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //======*
            if (SelModule == 26) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 6;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=====*
            if (SelModule == 27) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 7;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //Amendments
            //======*
            if (SelModule == 28) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 1;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            if (SelModule == 47) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmServiceContractAmend", "Service Contract Amendment");
                frmServiceContractAmend ObjDoc = (frmServiceContractAmend) aFrame.ObjApplet;
                ObjDoc.POType = 8;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=======*
            if (SelModule == 29) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 2;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //========*
            if (SelModule == 30) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 3;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=======*
            if (SelModule == 31) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 4;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //======*
            if (SelModule == 32) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 5;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=======*
            if (SelModule == 33) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 6;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=======*
            if (SelModule == 34) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 7;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //======*
            if (SelModule == 38) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rate Approval");
                aFrame.startAppletEx("EITLERP.Purchase.frmRateApproval", "Rate Approval");
                frmRateApproval ObjDoc = (frmRateApproval) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }

            //=======*
            if (SelModule == 98) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Sales Invoice Due Date");
                aFrame.startAppletEx("EITLERP.Sales.frmSalesInvoiceDueDate", "Sales Invoice Due Date");
                frmSalesInvoiceDueDate ObjDoc = (frmSalesInvoiceDueDate) aFrame.ObjApplet;
                //  ObjDoc.POType=6;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            if (SelModule == 99) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Debit Memo Receipt Mapping");
                aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo Receipt Mapping");
                frmDebitMemoReceiptMapping ObjDoc = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
                //  ObjDoc.POType=6;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindWaiting();
            }
            /*
             //=======*
             if(SelModule==194) {
             DocNo=(String)Table.getValueAt(Table.getSelectedRow(), 0);
             AppletFrame aFrame=new AppletFrame("Cancel Dummy Invoice");
             aFrame.startAppletEx("EITLERP.Finance.frmCancelDummyInvoice","Cancel Dummy Invoice");
             frmCancelDummyInvoice ObjDoc=(frmCancelDummyInvoice) aFrame.ObjApplet;
             //  ObjDoc.POType=6;
             //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
             ObjDoc.frmPA=this;
             //       ObjDoc.FindWaiting();
             }
            
             */
            if (SelModule == clsSales_Party.ModuleID) {
                String PartyID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Sales Party Master");
                aFrame.startAppletEx("EITLERP.frmSalesParty", "Sales Party Master");
                frmSalesParty ObjItem = (frmSalesParty) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == clsSalesPartyAmend.ModuleID) {
                String AmendID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Sales Party Master Updation");
                aFrame.startAppletEx("EITLERP.frmSalesPartyAmend", "Sales Party Master Updation");
                frmSalesPartyAmend ObjItem = (frmSalesPartyAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == clsFeltPriceList.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Price List");
                aFrame.startAppletEx("EITLERP.Sales.frmFeltPriceList", "Felt Price List");
                frmFeltPriceList ObjItem = (frmFeltPriceList) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            if (SelModule == clsPolicyMaster.ModuleID) {
                String PolicyNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Policy Master");
                aFrame.startAppletEx("EITLERP.Finance.frmPolicyMaster", "Policy Master");
                frmPolicyMaster ObjItem = (frmPolicyMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            //Bhavesh Mem JV
            if (SelModule == clsMemorandumJV.ModuleID) {
                String MemNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("MemorandumJV");
                aFrame.startAppletEx("EITLERP.Finance.frmMemorandumJV", "MemorandumJV");
                frmMemorandumJV ObjItem = (frmMemorandumJV) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
            }

            ////
            //            if(SelModule==clsCreditNoteProcessModule.ModuleID) {
            //                DocNo=(String)Table.getValueAt(Table.getSelectedRow(), 0);
            //                AppletFrame aFrame=new AppletFrame("Credit Note Processing Module");
            //                aFrame.startAppletEx("EITLERP.Finance.frmCreditNoteProcessModule","Credit Note Processing Module");
            //                frmCreditNoteProcessModule ObjItem=(frmCreditNoteProcessModule) aFrame.ObjApplet;
            //                ObjItem.frmPA=this;
            //                ObjItem.FindWaiting();
            //            }
            if (SelModule == 59 || SelModule == 65 || SelModule == 66 || SelModule == 67 || SelModule == 68 || SelModule == 83 || SelModule == 69 || SelModule == 70 || SelModule == 89 || SelModule == 94 || SelModule == 90 || SelModule == 205) {
                String VoucherNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                clsVoucher.OpenVoucher(VoucherNo, this);
            }

            //vivek
            if (SelModule == 713) {
                AppletFrame aFrame = new AppletFrame("Felt Export Invoice");
                aFrame.startAppletEx("EITLERP.Production.FeltExportInvoice.frmFeltExportInvoice", "Felt Export Invoice");
                frmFeltExportInvoice ObjItem = (frmFeltExportInvoice) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 715) {
                AppletFrame aFrame = new AppletFrame("Felt Packing Slip");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPacking.frmFeltPacking", "Felt Packing Slip");
                frmFeltPacking ObjItem = (frmFeltPacking) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 712) {
                AppletFrame aFrame = new AppletFrame("Felt Rejection");
                aFrame.startAppletEx("EITLERP.Production.FeltRejection.frmFeltRejection", "Felt Rejection");
                frmFeltRejection ObjItem = (frmFeltRejection) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 709) {
                AppletFrame aFrame = new AppletFrame("Felt Warping Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltWarping.frmFeltWarping", "Felt Warping Entry");
                frmFeltWarping ObjItem = (frmFeltWarping) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 716) {
                AppletFrame aFrame = new AppletFrame("Felt Finishing Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltFinishing.frmFeltFinishing", "Felt Finishing Entry");
                frmFeltFinishing ObjItem = (frmFeltFinishing) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 711) {
                AppletFrame aFrame = new AppletFrame("Felt Mending Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltMending.frmFeltMending_New", "Felt Mending Entry");
                frmFeltMending_New ObjItem = (frmFeltMending_New) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 710) {
                AppletFrame aFrame = new AppletFrame("Felt Needling Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltNeedling.frmFeltNeedling", "Felt Needling Entry");
                frmFeltNeedling ObjItem = (frmFeltNeedling) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;

            }

            if (SelModule == 723) {
                AppletFrame aFrame = new AppletFrame("Felt Weaving Loom Efficiency Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltWeavingLoom.frmFeltWeavingLoom", "Felt Weaving Loom Efficiency Entry");
                frmFeltWeavingLoom ObjItem = (frmFeltWeavingLoom) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 707) {
                AppletFrame aFrame = new AppletFrame("Felt Weaving Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltWeaving.frmFeltWeaving", "Felt Weaving Entry");
                frmFeltWeaving ObjItem = (frmFeltWeaving) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 704) { 
                AppletFrame aFrame = new AppletFrame("Felt Heat Setting Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltHeatSetting.frmFeltHeatSetting", "Felt Heat Setting Entry");
                frmFeltHeatSetting ObjItem = (frmFeltHeatSetting) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            
            if (SelModule == 706) {
                AppletFrame aFrame = new AppletFrame("Felt Marking Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltMarking.frmFeltMarking", "Felt Marking Entry");
                frmFeltMarking ObjItem = (frmFeltMarking) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            
            if (SelModule == 726) {
                AppletFrame aFrame = new AppletFrame("Felt Splicing Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltSplicing.frmFeltSplicing", "Felt Splicing Entry");
                frmFeltSplicing ObjItem = (frmFeltSplicing) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 720) {
                AppletFrame aFrame = new AppletFrame("Felt Order Updation");
                aFrame.startAppletEx("EITLERP.Production.Felt_Order_Updation.frmFelt_Order_Upd", "Felt Weaving Entry");
                frmFeltOrderUpd ObjItem = (frmFeltOrderUpd) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 701) {
                AppletFrame aFrame = new AppletFrame("Felt Rate Master");
                aFrame.startAppletEx("EITLERP.Production.FeltRateMaster.frmFeltRateMaster", "Felt Rate Master");
                frmFeltRateMaster ObjItem = (frmFeltRateMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 702) {
                AppletFrame aFrame = new AppletFrame("Felt Rate Update");
                aFrame.startAppletEx("EITLERP.Production.FeltRateMaster.frmFeltRateUpdate", "Felt Rate Update");
                frmFeltRateUpdate ObjItem = (frmFeltRateUpdate) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 723) {
                AppletFrame aFrame = new AppletFrame("Felt Weaving LoomWise Efficiency");
                aFrame.startAppletEx("EITLERP.Production.FeltWeavingLoom.frmFeltWeavingLoom", "Felt Weaving LoomWise Efficiency");
                frmFeltWeavingLoom ObjItem = (frmFeltWeavingLoom) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 725) {
                AppletFrame aFrame = new AppletFrame("Felt Machine Survey Amendment");
                aFrame.startAppletEx("EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend", "Felt Machine Survey Amendment");
                frmmachinesurveyamend ObjItem = (frmmachinesurveyamend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 724) {
                AppletFrame aFrame = new AppletFrame("Felt Machine Survey");
                aFrame.startAppletEx("EITLERP.Production.FeltMachineSurvey.frmmachinesurvey", "Felt Machine Survey");
                frmmachinesurvey ObjItem = (frmmachinesurvey) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 727) {
                AppletFrame aFrame = new AppletFrame("LC Opener Master");
                aFrame.startAppletEx("EITLERP.Sales.frmLCOpenerMaster", "LC Opener Master");
                frmLCOpenerMaster ObjItem = (frmLCOpenerMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 728) {
                AppletFrame aFrame = new AppletFrame("LC Opener Master Amend1");
                aFrame.startAppletEx("EITLERP.Sales.frmLCOpenerMasterAmend1", "LC Opener Master Amend1");
                frmLCOpenerMasterAmend1 ObjItem = (frmLCOpenerMasterAmend1) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 730) {
                AppletFrame aFrame = new AppletFrame("Felt Discount Rate Master");
                aFrame.startAppletEx("EITLERP.Production.FeltDiscRateMaster.frmDiscRateMaster", "Felt Discount Rate Master");
                frmDiscRateMaster ObjItem = (frmDiscRateMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 732) {
                AppletFrame aFrame = new AppletFrame("Felt Unadjusted TRN");
                aFrame.startAppletEx("EITLERP.Production.FeltUnadj.frmFeltUnadj", "Felt Unadjusted TRN");
                frmFeltUnadj ObjItem = (frmFeltUnadj) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 735) {
                AppletFrame aFrame = new AppletFrame("Felt Credit Note");
                aFrame.startAppletEx("EITLERP.Production.FeltCreditNote.frmFeltCreditNote", "Felt Credit Note");
                frmFeltCreditNote ObjItem = (frmFeltCreditNote) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 737) {
                AppletFrame aFrame = new AppletFrame("Felt Year End Form 1 Credit Note");
                aFrame.startAppletEx("EITLERP.Production.FeltYearEndDiscountCreditNote.frmFeltYearEndDisc", "Felt Year End Form 1 Credit Note");
                frmFeltYearEndDisc ObjItem = (frmFeltYearEndDisc) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 738) {
                AppletFrame aFrame = new AppletFrame("Felt Year End Form 2 Credit Note");
                aFrame.startAppletEx("EITLERP.Production.YearEndDiscountForm2.frmFeltYearEndDisc2", "Felt Year End Form 2 Credit Note");
                frmFeltYearEndDisc2 ObjItem = (frmFeltYearEndDisc2) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 708) {
                AppletFrame aFrame = new AppletFrame("Felt Perfoma Invoice");
                aFrame.startAppletEx("EITLERP.FeltSales.Perfomainvoice.frmperfomainvoice", "Felt Perfoma Invoice");
                frmperfomainvoice ObjItem = (frmperfomainvoice) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 739) {
                AppletFrame aFrame = new AppletFrame("Felt Group Master");
                aFrame.startAppletEx("EITLERP.FeltSales.GroupMaster.frmFelGroupMaster", "Felt Group Master");
                frmFelGroupMaster ObjItem = (frmFelGroupMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 740) {
                AppletFrame aFrame = new AppletFrame("Felt Reopen Bale");
                aFrame.startAppletEx("EITLERP.FeltSales.ReopenBale12.frmFeltReopenBale", "Felt Reopen Bale");
                frmFeltReopenBale ObjItem = (frmFeltReopenBale) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 743) {
                AppletFrame aFrame = new AppletFrame("Felt Group Master Amend");
                aFrame.startAppletEx("EITLERP.FeltSales.GroupMasterAmend.frmFelGroupMasterAmend", "Felt Group Master Amend");
                frmFelGroupMasterAmend ObjItem = (frmFelGroupMasterAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 602) {
                AppletFrame aFrame = new AppletFrame("Felt Sales Order");
                aFrame.startAppletEx("EITLERP.FeltSales.Order.FrmFeltOrder", "Felt Sales Order");
                FrmFeltOrder ObjItem = (FrmFeltOrder) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 603) {
                AppletFrame aFrame = new AppletFrame("Felt Finishing");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltFinishing.frmFeltFinishing", "Felt Finishing");
                EITLERP.FeltSales.FeltFinishing.frmFeltFinishing ObjItem = (EITLERP.FeltSales.FeltFinishing.frmFeltFinishing) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 604) {
                AppletFrame aFrame = new AppletFrame("Felt Sales Order Diversion");
                aFrame.startAppletEx("EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion", "Felt Sales Order Diversion");
                FrmFeltOrderDiversion ObjItem = (FrmFeltOrderDiversion) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 742) {

                AppletFrame aFrame = new AppletFrame("Manual Addition to Diversion List");
                aFrame.startAppletEx("EITLERP.FeltSales.DiversionList.FrmDiversionList", "Manual Addition to Diversion List");
                FrmDiversionList ObjItem = (FrmDiversionList) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 745) {

                AppletFrame aFrame = new AppletFrame("Felt Sales Piece Updation");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceUpdation.frmFeltPieceUpd", "Felt Sales Piece Updation");
                frmFeltPieceUpd ObjItem = (frmFeltPieceUpd) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 610) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Invoice Cancellation Request");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltInvReport.frmInvoiceCancel", "Felt Invoice Cancellation Request");
                EITLERP.FeltSales.FeltInvReport.frmInvoiceCancel ObjItem = (EITLERP.FeltSales.FeltInvReport.frmInvoiceCancel) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 80) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Sales Invoice");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltInvReport.frmFeltSalesInvoice", "Felt Sales Invoice");
                EITLERP.FeltSales.FeltInvReport.frmFeltSalesInvoice ObjItem = (EITLERP.FeltSales.FeltInvReport.frmFeltSalesInvoice) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 754) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                if(lItemID.contains("F6")){
                AppletFrame aFrame = new AppletFrame("Felt Invoice Parameter Modification F6");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltInvoiceParameterModificationF6.frmFeltInvoiceParameterModificationf6Form", "Felt Invoice Parameter Modification F6");
                EITLERP.FeltSales.FeltInvoiceParameterModificationF6.frmFeltInvoiceParameterModificationf6Form ObjItem = (EITLERP.FeltSales.FeltInvoiceParameterModificationF6.frmFeltInvoiceParameterModificationf6Form) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();    
                }else{
                AppletFrame aFrame = new AppletFrame("Felt Invoice Parameter Modification");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm", "Felt Invoice Parameter Modification");
                EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm ObjItem = (EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
                }
                
            }

            if (SelModule == 744) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Sales Return");
                aFrame.startAppletEx("EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns", "Felt Sales Return");
                EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns ObjItem = (EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 759) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Piece Amend");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmend", "Felt Piece Amend");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmend ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 760) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Piece Division");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceDivision.FrmPieceDivision", "Felt Piece Division");
                EITLERP.FeltSales.PieceDivision.FrmPieceDivision ObjItem = (EITLERP.FeltSales.PieceDivision.FrmPieceDivision) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 761) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Invoice Parameter Modification Cancellation");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmInvoiceParameterCancel", "Felt Invoice Parameter Modification Cancellation");
                EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmInvoiceParameterCancel ObjItem = (EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmInvoiceParameterCancel) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 762) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Transporter Weight");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltTransporterWeight.frmFeltTransporterWeigthEntryForm", "Felt Transporter Weight");
                frmFeltTransporterWeigthEntryForm ObjItem = (frmFeltTransporterWeigthEntryForm) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 765) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Invoice F6 Process Parameter");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltProcessInvoiceVariable.frmFeltProcessInvoiceVariable", "Felt Invoice F6 Process Parameter");
                EITLERP.FeltSales.FeltProcessInvoiceVariable.frmFeltProcessInvoiceVariable ObjItem = (EITLERP.FeltSales.FeltProcessInvoiceVariable.frmFeltProcessInvoiceVariable) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 766) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Budget Upload");
                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetUpload", "Felt Budget Upload");
                EITLERP.FeltSales.Budget.FrmBudgetUpload ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetUpload) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
//            if (SelModule == 768) {
//                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
//                AppletFrame aFrame = new AppletFrame("Felt Budget Manual Entry");
//                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetManual", "Felt Budget Manual Entry");
//                EITLERP.FeltSales.Budget.FrmBudgetManual ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetManual) aFrame.ObjApplet;
//                ObjItem.frmPA = this;
//                ObjItem.FindWaiting();
//                ObjItem.PENDING_DOCUMENT = true;
//                aFrame.setVisible(true);
//                aFrame.dispose();
//            }

            if (SelModule == 768) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Budget Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetEntry", "Felt Budget Entry");
                EITLERP.FeltSales.Budget.FrmBudgetEntry ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 769) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("FELT GROUP CRITICAL LIMIT ENHANCEMENT FOR SELECTED BALE DETAILS");
                aFrame.startAppletEx("EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFelGroupCriticalLimitEnhancement", "FELT GROUP CRITICAL LIMIT ENHANCEMENT FOR SELECTED BALE DETAILS");
                EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFelGroupCriticalLimitEnhancement ObjItem = (EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFelGroupCriticalLimitEnhancement) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 770) {
                AppletFrame aFrame = new AppletFrame("Felt Location Assignment");
                aFrame.startAppletEx("EITLERP.FeltSales.LocationAssignment.frmFeltLocationAssignment", "Felt Location Assignment");
                frmFeltLocationAssignment ObjItem = (frmFeltLocationAssignment) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 774) {
                AppletFrame aFrame = new AppletFrame("WIP Piece Amendment based on Machine Master");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentWIP.frmPieceAmendWIP", "WIP Piece Amendment based on Machine Master");
                frmPieceAmendWIP ObjItem = (frmPieceAmendWIP) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 776) {
                AppletFrame aFrame = new AppletFrame("WIP Piece DELINK Approval");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentDelink.frmPieceAmendDELINK", "WIP Piece DELINK Approval");
                frmPieceAmendWIP ObjItem = (frmPieceAmendWIP) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == clsWarpingBeamOrder.ModuleID) {
                AppletFrame aFrame = new AppletFrame("Warping Beam Order Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrder2", "Warping Beam Order Entry");
                frmWarpingBeamOrder2 ObjItem = (frmWarpingBeamOrder2) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == clsWarpingBeamOrderAmend.ModuleID) {
                AppletFrame aFrame = new AppletFrame("Warping Beam Order Amend Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderAmend", "Warping Beam Order Amend Entry");
                frmWarpingBeamOrderAmend ObjItem = (frmWarpingBeamOrderAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 820) {
                AppletFrame aFrame = new AppletFrame("Weaving Loomwise Production Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.WVG_Prod_Loom_WVR.FrmWVG_Prod_Loom_WVR", "Weaving Loomwise Production Entry");
                FrmWVG_Prod_Loom_WVR ObjItem = (FrmWVG_Prod_Loom_WVR) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindWaiting();
                ObjItem.PENDING_DOCUMENT = true;
//                aFrame.setVisible(true);
//                aFrame.dispose();
            }
        }

    }//GEN-LAST:event_cmdOpen1ActionPerformed

    private void cmdRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefreshActionPerformed
        // TODO add your handling code here:
        lblProcess.setText("Process started...");
        FormatGridNormal();

        System.out.println(defUserID);
        if (chkUser.isSelected()) {
            int CurUserID = EITLERPGLOBAL.gUserID;
            //EITLERPGLOBAL.gUserID = EITLERPGLOBAL.getComboCode(cmbUserold);
            EITLERPGLOBAL.gUserID = Integer.parseInt(cmbuser.getText());
            GenerateCombo();
            EITLERPGLOBAL.gUserID = CurUserID;
        } else if (OpgAuthDelUser.isSelected()) {
            EITLERPGLOBAL.gAuthorityUserID = EITLERPGLOBAL.getComboCode(cmbAuthority);
            System.out.println("ID:" + EITLERPGLOBAL.gAuthorityUserID);

            int CurUserID = EITLERPGLOBAL.gUserID;
            EITLERPGLOBAL.gUserID = EITLERPGLOBAL.getComboCode(cmbAuthority);;
            GenerateCombo();
            EITLERPGLOBAL.gUserID = CurUserID;

        } else if (OpgDefaultUser.isSelected()) {
            EITLERPGLOBAL.gAuthorityUserID = 1;
            System.out.println("ID:" + EITLERPGLOBAL.gAuthorityUserID);

            EITLERPGLOBAL.gUserID = defUserID;
            GenerateCombo();

        } else {
            GenerateCombo();
        }
        lblProcess.setText("Process completed...");
    }//GEN-LAST:event_cmdRefreshActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOpenActionPerformed
        // TODO add your handling code here:
        String DocNo = "";

        if (Table.getRowCount() > 0) {

            if (SelModule == 878) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt PPRS Planning");
                aFrame.startAppletEx("EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry", "Felt PPRS Planning");
                EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry ObjItem = (EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            
            if (SelModule == 876) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Zone Master");
                aFrame.startAppletEx("EITLERP.FeltSales.ZoneMaster.FrmZoneMaster", "Zone Master");
                EITLERP.FeltSales.ZoneMaster.FrmZoneMaster ObjDoc = (EITLERP.FeltSales.ZoneMaster.FrmZoneMaster) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 877) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Zone Party Selection");
                aFrame.startAppletEx("EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection", "Zone Party Selection");
                EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection ObjDoc = (EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 206) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Authority Delegation Request");
                aFrame.startAppletEx("EITLERP.AuthorityDelegation.FrmAuthorityDelegationRequest", "Authority Delegation Request");
                EITLERP.AuthorityDelegation.FrmAuthorityDelegationRequest ObjDoc = (EITLERP.AuthorityDelegation.FrmAuthorityDelegationRequest) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 872) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Budget Entry[New]");
                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmNewBudgetEntry", "Felt Budget Entry[New]");
                EITLERP.FeltSales.Budget.FrmNewBudgetEntry ObjItem = (EITLERP.FeltSales.Budget.FrmNewBudgetEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 868) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Daily Rokdi Attendance (Regular)");
                aFrame.startAppletEx("SDMLATTPAY.DailyRokdiAttData.frmDailyRokdiAttData", "Daily Rokdi Attendance (Regular)");
                SDMLATTPAY.DailyRokdiAttData.frmDailyRokdiAttData ObjDoc = (SDMLATTPAY.DailyRokdiAttData.frmDailyRokdiAttData) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 865) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Excess Manpower Requisition");
                aFrame.startAppletEx("SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm", "Excess Manpower Requisition");
                SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm ObjDoc = (SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 866) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Shift Schedule Change");
                aFrame.startAppletEx("SDMLATTPAY.AdvanceShiftChange.FrmShiftScheduleChange", "Shift Schedule");
                SDMLATTPAY.AdvanceShiftChange.FrmShiftScheduleChange ObjDoc = (SDMLATTPAY.AdvanceShiftChange.FrmShiftScheduleChange) aFrame.ObjApplet;

                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 863) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Shift Schedule");
                aFrame.startAppletEx("SDMLATTPAY.AdvanceShiftChange.FrmShiftSchedule", "Shift Schedule");
                SDMLATTPAY.AdvanceShiftChange.FrmShiftSchedule ObjDoc = (SDMLATTPAY.AdvanceShiftChange.FrmShiftSchedule) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 862) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Selection");
                aFrame.startAppletEx("SDMLATTPAY.RokdiSelection.frmRokdiSelection", "Rokdi Selection");
                SDMLATTPAY.RokdiSelection.frmRokdiSelection ObjDoc = (SDMLATTPAY.RokdiSelection.frmRokdiSelection) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 860) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Travelling Voucher");
                aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelVoucher", "Travelling Voucher");
                SDMLATTPAY.TravelAdvance.FrmTravelVoucher ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelVoucher) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 859) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Travelling Sanction Amendment");
                aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend", "Travelling Sanction Amendment");
                SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 858) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Travelling Sanction");
                aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelEntry", "Travelling Sanction");
                SDMLATTPAY.TravelAdvance.FrmTravelEntry ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 856) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Retainer Entry");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmCoffRetainerEntry", "Contract Entry");
                SDMLATTPAY.COFF.FrmCoffRetainerEntry ObjDoc = (SDMLATTPAY.COFF.FrmCoffRetainerEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 855) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Spillover Rescheduling");
                aFrame.startAppletEx("EITLERP.FeltSales.SpilloverRescheduling.frmInStockSpillover", "Spillover Rescheduling");
                EITLERP.FeltSales.SpilloverRescheduling.frmInStockSpillover ObjDoc = (EITLERP.FeltSales.SpilloverRescheduling.frmInStockSpillover) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 867) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Spillover Rescheduling");
                aFrame.startAppletEx("EITLERP.FeltSales.SpilloverRescheduling_New.frmInStockSpillover_New", "Spillover Rescheduling");
                EITLERP.FeltSales.SpilloverRescheduling_New.frmInStockSpillover_New ObjDoc = (EITLERP.FeltSales.SpilloverRescheduling_New.frmInStockSpillover_New) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 870) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Piece Clubbing");
                aFrame.startAppletEx("EITLERP.FeltSales.SalesPieceClubbing.frmSalesPieceClubbing", "Felt Piece Clubbing");
                EITLERP.FeltSales.SalesPieceClubbing.frmSalesPieceClubbing ObjDoc = (EITLERP.FeltSales.SalesPieceClubbing.frmSalesPieceClubbing) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 871) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Piece Clubbing Amend");
                aFrame.startAppletEx("EITLERP.FeltSales.SalesPieceClubbingAmend.frmSalesPieceClubbingAmend", "Felt Piece Clubbing Amend");
                EITLERP.FeltSales.SalesPieceClubbingAmend.frmSalesPieceClubbingAmend ObjDoc = (EITLERP.FeltSales.SalesPieceClubbingAmend.frmSalesPieceClubbingAmend) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 874) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Piece Clubbing Reschedule");
                aFrame.startAppletEx("EITLERP.FeltSales.ClubbingSchChange.frmClubbedPieceSchChange", "Felt Piece Clubbing Reschedule");
                EITLERP.FeltSales.ClubbingSchChange.frmClubbedPieceSchChange ObjDoc = (EITLERP.FeltSales.ClubbingSchChange.frmClubbedPieceSchChange) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 857) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Spillover Special Request Date Approval");
                aFrame.startAppletEx("EITLERP.FeltSales.SpecialRequest.FrmSpiloverSpecialReqDateApproval", "Spillover Special Request Date Approval");
                EITLERP.FeltSales.SpecialRequest.FrmSpiloverSpecialReqDateApproval ObjDoc = (EITLERP.FeltSales.SpecialRequest.FrmSpiloverSpecialReqDateApproval) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 861) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Sample Order Booking");
                aFrame.startAppletEx("EITLERP.FeltSales.SampleOrder.FrmFeltSampleOrder", "Felt Sample Order Booking");
                EITLERP.FeltSales.SampleOrder.FrmFeltSampleOrder ObjDoc = (EITLERP.FeltSales.SampleOrder.FrmFeltSampleOrder) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 786) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Beam Order Closure");
                aFrame.startAppletEx("EITLERP.FeltSales.BeamOrderClosure.frmBeamOrderClosure", "Beam Order Closure");
                EITLERP.FeltSales.BeamOrderClosure.frmBeamOrderClosure ObjItem = (EITLERP.FeltSales.BeamOrderClosure.frmBeamOrderClosure) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 852) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Allow Booking for Projection");
                aFrame.startAppletEx("EITLERP.FeltSales.AllowBookingAgainstProjection.FrmAllowBookingAgainstProjection", "Allow Booking for Projection");
                EITLERP.FeltSales.AllowBookingAgainstProjection.FrmAllowBookingAgainstProjection ObjDoc = (EITLERP.FeltSales.AllowBookingAgainstProjection.FrmAllowBookingAgainstProjection) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 864) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Piece OC Month chage");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltOCChange.FrmOCChange", "Piece OC Month chage");
                EITLERP.FeltSales.FeltOCChange.FrmOCChange ObjDoc = (EITLERP.FeltSales.FeltOCChange.FrmOCChange) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);

                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 851) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Machine Run Forcasting");
                aFrame.startAppletEx("EITLERP.FeltSales.MachineRunForcasting.frmMachineRunForcastingEntry", "Machine Run Forcasting");
                EITLERP.FeltSales.MachineRunForcasting.frmMachineRunForcastingEntry ObjDoc = (EITLERP.FeltSales.MachineRunForcasting.frmMachineRunForcastingEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 849) {
                checkacess("SDMLATTPAY.IncrementProposal.FrmIncrementProposal");
            }

            if (SelModule == 662) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Obsolete Piece Scrap");
                aFrame.startAppletEx("EITLERP.FeltSales.ObsoleteScrap.frmFeltObsoleteScrap", "Obsolete Piece Scrap");
                EITLERP.FeltSales.ObsoleteScrap.frmFeltObsoleteScrap ObjItem = (EITLERP.FeltSales.ObsoleteScrap.frmFeltObsoleteScrap) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 850) {
                AppletFrame aFrame = new AppletFrame("SDML SDF Spiralling Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.SDF.FeltSpiral.frmFeltSpiralling", "SDML SDF Spiralling Entry");
                EITLERP.FeltSales.SDF.FeltSpiral.frmFeltSpiralling ObjItem = (EITLERP.FeltSales.SDF.FeltSpiral.frmFeltSpiralling) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 635) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Post Audit Disc Rate Master");
                aFrame.startAppletEx("EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster", "Post Audit Disc Rate Master");
                EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster ObjItem = (EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 204) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Payment Requisition Slip");
                aFrame.startAppletEx("EITLERP.Stores.frmPaymentRequisition", "Payment Requisition Slip");
                EITLERP.Stores.frmPaymentRequisition ObjDoc = (EITLERP.Stores.frmPaymentRequisition) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                //ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 846) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Beam Gaiting Status");
                aFrame.startAppletEx("EITLERP.Production.BeamGaitingStatus.FrmBeamGaitingStatus", "Felt Beam Gaiting Status");
                EITLERP.Production.BeamGaitingStatus.FrmBeamGaitingStatus ObjDoc = (EITLERP.Production.BeamGaitingStatus.FrmBeamGaitingStatus) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 842) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Request Form");
                aFrame.startAppletEx("SDMLATTPAY.RokdiRequestAmend.frmRokdiRequestAmendForm", "Rokdi Request Amendment");
                SDMLATTPAY.RokdiRequestAmend.frmRokdiRequestAmendForm ObjDoc = (SDMLATTPAY.RokdiRequestAmend.frmRokdiRequestAmendForm) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 839) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Request Form");
                aFrame.startAppletEx("SDMLATTPAY.RokdiRequest.frmRokdiRequestForm", "Rokdi Request Form");
                SDMLATTPAY.RokdiRequest.frmRokdiRequestForm ObjDoc = (SDMLATTPAY.RokdiRequest.frmRokdiRequestForm) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 837) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Daily Attendance (Regular)");
                aFrame.startAppletEx("SDMLATTPAY.DailyAttDataForm.frmDailyAttDataForm", "Daily Attendance (Regular)");
                SDMLATTPAY.DailyAttDataForm.frmDailyAttDataForm ObjDoc = (SDMLATTPAY.DailyAttDataForm.frmDailyAttDataForm) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 834) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Budget Review Entry Form");
                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmNewSalesProjectionEntry", "Budget Review Entry Form");
                FrmNewSalesProjectionEntry ObjDoc = (FrmNewSalesProjectionEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 634) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Party Contact Detail Upadtion");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPartyContact.frmFeltPartyContact", "Felt Party Contact Detail Upadtion");
                frmFeltPartyContact ObjItem = (frmFeltPartyContact) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 831) {
                AppletFrame aFrame = new AppletFrame("Superannuation");
                aFrame.startAppletEx("SDMLATTPAY.SUPERANNUATION.FrmSuperannuationProcess", "Superannuation");
                FrmSuperannuationProcess ObjItem = (FrmSuperannuationProcess) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 615) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Invoice Covering Letter");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltInvCvrLtr.frmFeltInvCvrLtr", "Felt Invoice Covering Letter");
                frmFeltInvCvrLtr ObjItem = (frmFeltInvCvrLtr) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 833) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Performance Tracking Sheet");
                aFrame.startAppletEx("EITLERP.FeltSales.PreformaceTracking.FrmPerformaceTrackingSheet", "Felt Performance Tracking Sheet");
                FrmPerformaceTrackingSheet ObjItem = (FrmPerformaceTrackingSheet) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 830) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Miss Punch");
                aFrame.startAppletEx("SDMLATTPAY.MissedPunchRequest.FrmMissedPunchRequest", "Miss Punch");
                SDMLATTPAY.MissedPunchRequest.FrmMissedPunchRequest ObjDoc = (SDMLATTPAY.MissedPunchRequest.FrmMissedPunchRequest) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 829) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Contract Rokdi Process");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmRokdiCProcess", "Contract Rokdi Process");
                SDMLATTPAY.COFF.FrmRokdiCProcess ObjDoc = (SDMLATTPAY.COFF.FrmRokdiCProcess) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 827) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Contract Rokdi Entry");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmRokdiCEntry", "Contract Rokdi Entry");
                SDMLATTPAY.COFF.FrmRokdiCEntry ObjDoc = (SDMLATTPAY.COFF.FrmRokdiCEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 818) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Coff Process");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmCoffProcess", "Coff Process");
                SDMLATTPAY.COFF.FrmCoffProcess ObjDoc = (SDMLATTPAY.COFF.FrmCoffProcess) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 817) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Process");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmRokdiProcess", "Rokdi Process");
                SDMLATTPAY.COFF.FrmRokdiProcess ObjDoc = (SDMLATTPAY.COFF.FrmRokdiProcess) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 814) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Employee Master Amend");
                aFrame.startAppletEx("SDMLATTPAY.EmployeeAmend.FrmEmployeeMasterAmend", "Employee Master Amend Entry");
                SDMLATTPAY.EmployeeAmend.FrmEmployeeMasterAmend ObjDoc = (SDMLATTPAY.EmployeeAmend.FrmEmployeeMasterAmend) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 816) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rokdi Entry");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmRokdiEntry", "Rokdi Entry");
                SDMLATTPAY.COFF.FrmRokdiEntry ObjDoc = (SDMLATTPAY.COFF.FrmRokdiEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 812) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Employee Master");
                aFrame.startAppletEx("SDMLATTPAY.Employee.FrmEmployeeMaster", "Employee Master Entry");
                SDMLATTPAY.Employee.FrmEmployeeMaster ObjDoc = (SDMLATTPAY.Employee.FrmEmployeeMaster) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 815) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Coff Entry");
                aFrame.startAppletEx("SDMLATTPAY.COFF.FrmCoffEntry", "Coff Entry");
                SDMLATTPAY.COFF.FrmCoffEntry ObjDoc = (SDMLATTPAY.COFF.FrmCoffEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }
            if (SelModule == 826) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Monthly Attendance Data");
                aFrame.startAppletEx("SDMLATTPAY.MonthlyAttendance.frmMonthlyAttendance", "Monthly Attendance Data");
                SDMLATTPAY.MonthlyAttendance.frmMonthlyAttendance ObjDoc = (SDMLATTPAY.MonthlyAttendance.frmMonthlyAttendance) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.Find(DocNo);
                ObjDoc.PENDING_DOCUMENT = true;
            }

            if (SelModule == 823) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Special Deduction Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmSpecialDeductionEntry", "Special Deduction Entry");
                SDMLATTPAY.gatepass.FrmSpecialDeductionEntry ObjDoc = (SDMLATTPAY.gatepass.FrmSpecialDeductionEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 632) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("PO Details Updation");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO", "PO Details Updation");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 631) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Special Rate Sanction Eligibility");
                aFrame.startAppletEx("EITLERP.FeltSales.RateEligibility.frmFeltRateEligibility", "Special Rate Sanction Eligibility");
                EITLERP.FeltSales.RateEligibility.frmFeltRateEligibility ObjItem = (EITLERP.FeltSales.RateEligibility.frmFeltRateEligibility) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 804) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Shift Change");
                aFrame.startAppletEx("SDMLATTPAY.Shift.FrmShiftSchChange", "Shift Change");
                SDMLATTPAY.Shift.FrmShiftSchChange ObjItem = (SDMLATTPAY.Shift.FrmShiftSchChange) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 806) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Shift Upload");
                aFrame.startAppletEx("SDMLATTPAY.Shift.FrmShiftUpload", "Shift Upload");
                SDMLATTPAY.Shift.FrmShiftUpload ObjItem = (SDMLATTPAY.Shift.FrmShiftUpload) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 813) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Leave Updation Entry");
                aFrame.startAppletEx("SDMLATTPAY.leave.FrmLeaveUpdation", "Leave Updation Entry");
                SDMLATTPAY.leave.FrmLeaveUpdation ObjItem = (SDMLATTPAY.leave.FrmLeaveUpdation) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 811) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Leave Application Entry");
                aFrame.startAppletEx("SDMLATTPAY.leave.FrmLeaveApplication", "Leave Application Entry");
                SDMLATTPAY.leave.FrmLeaveApplication ObjItem = (SDMLATTPAY.leave.FrmLeaveApplication) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 808) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Holiday WeekOff Entry");
                aFrame.startAppletEx("SDMLATTPAY.holiday.FrmHoliday", "Holiday WeekOff Entry");
                SDMLATTPAY.holiday.FrmHoliday ObjItem = (SDMLATTPAY.holiday.FrmHoliday) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 805) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Gatepass Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmGatepass", "Gatepass Entry");
                SDMLATTPAY.gatepass.FrmGatepass ObjItem = (SDMLATTPAY.gatepass.FrmGatepass) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 822) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Update Punch Date Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmUpdatePunchDateEntry", "Update Punch Date Entry");
                SDMLATTPAY.gatepass.FrmUpdatePunchDateEntry ObjDoc = (SDMLATTPAY.gatepass.FrmUpdatePunchDateEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 819) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Special Sanction Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmSpecialSanctionEntry", "Special Sanction Entry");
                SDMLATTPAY.gatepass.FrmSpecialSanctionEntry ObjDoc = (SDMLATTPAY.gatepass.FrmSpecialSanctionEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 810) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Time Correction Entry");
                aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmTimeCorrectionEntry", "Time Correction Entry");
                FrmTimeCorrectionEntry ObjDoc = (FrmTimeCorrectionEntry) aFrame.ObjApplet;
                //ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 802) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt WH Post Invoice Data");
                aFrame.startAppletEx("EITLERP.FeltWH.frmPostInvoiceEntry", "Felt WH Post Invoice Data");
                frmPostInvoiceEntry ObjDoc = (frmPostInvoiceEntry) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 803) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt WH Gatepass");
                aFrame.startAppletEx("EITLERP.FeltWH.frmWHInvGatepassEntry", "Felt WH Gatepass");
                frmWHInvGatepassEntry ObjDoc = (frmWHInvGatepassEntry) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 630) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Auto PI Selection");
                aFrame.startAppletEx("EITLERP.FeltSales.AutoPI.frmFeltAutoPISelection", "Felt Auto PI Selection");
                frmFeltAutoPISelection ObjItem = (frmFeltAutoPISelection) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == clsTrailPieceEntry.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Trial Piece Selection");
                aFrame.startAppletEx("EITLERP.FeltSales.TrailPiece.frmTrailPieceEntry", "Felt Trial Piece Selection");
                frmTrailPieceEntry ObjItem = (frmTrailPieceEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }
            if (SelModule == clsTrailPieceDispatchEntry.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Trial Piece Post Dispatch Detail Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.TrailPiece.frmTrailPieceDispatchEntry", "Felt Trial Piece Post Dispatch Detail Entry");
                frmTrailPieceDispatchEntry ObjItem = (frmTrailPieceDispatchEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 794) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("NON RETURNABLE GATEPASS SDML To GIDC");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_SDML", "NON RETURNABLE GATEPASS SDML To GIDC");
                FrmNRGP_SDML ObjItem = (FrmNRGP_SDML) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 793) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("NON RETURNABLE GATEPASS GIDC To SDML");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_GIDC", "NON RETURNABLE GATEPASS GIDC To SDML");
                FrmNRGP_GIDC ObjItem = (FrmNRGP_GIDC) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsGIDCInstruction.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("SDML SDF Instruction Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Instruction.frmGIDCInstruction", "SDML SDF Instruction Entry");
                frmGIDCInstruction ObjItem = (frmGIDCInstruction) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == clsGIDCInstructionAmend.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("SDML SDF Instruction Amendment Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Instruction.frmGIDCInstructionAmend", "SDML SDF Instruction Amendment Entry");
                frmGIDCInstructionAmend ObjItem = (frmGIDCInstructionAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 792) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("GIDC Despatch of SDF to SDML");
                aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Despatch.frmDespatchGIDC_SDML", "GIDC Despatch of SDF to SDML");
                frmDespatchGIDC_SDML ObjItem = (frmDespatchGIDC_SDML) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 626) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt PDC Amendment");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPDCAmend.frmFeltPDCAmend", "Felt PDC Amendment");
                frmFeltPDCAmend ObjItem = (frmFeltPDCAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 625) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt PDC Entry Form");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPDC.frmFeltPDC", "Felt PDC Entry Form");
                frmFeltPDC ObjItem = (frmFeltPDC) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 764) {
                AppletFrame aFrame = new AppletFrame("Felt Rejection");
                aFrame.startAppletEx("EITLERP.Production.FeltRejection.frmFeltRejection", "Felt Rejection Entry");
                frmFeltRejection ObjItem = (frmFeltRejection) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == clsWarpingBeamOrderHDS.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Warping Beam Order Entry [HDS]");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderHDS", "Warping Beam Order Entry HDS");
                frmWarpingBeamOrderHDS ObjItem = (frmWarpingBeamOrderHDS) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == clsWarpingBeamOrderHDSAmend.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Warping Beam Order Amend Entry [HDS]");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderHDSAmend", "Warping Beam Order Amend Entry HDS");
                frmWarpingBeamOrderHDSAmend ObjItem = (frmWarpingBeamOrderHDSAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 785) {
                AppletFrame aFrame = new AppletFrame("Felt Seaming Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltSeaming.frmFeltSeaming", "Felt Seaming Entry");
                frmFeltSeaming ObjItem = (frmFeltSeaming) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 601) {
                AppletFrame aFrame = new AppletFrame("Felt Quality Rate Master");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltQualityRateMaster.frmFeltQltRateMaster", "Felt Rate Master");
                frmFeltQltRateMaster ObjItem = (frmFeltQltRateMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 621) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("WIP Piece Amendment");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP", "WIP Piece Amendment");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 622) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("STOCK Piece Tagging");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK", "STOCK Piece Tagging");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 623) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Piece Cancellation");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR", "Piece Cancellation");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 624) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Piece Transfer to Export");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT", "Piece Transfer to Export");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 612) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Evaluation ReOpen");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltEvaluation.frmFeltEvaluationReOpen", "Felt Evaluation ReOpen");
                frmFeltEvaluationReOpen ObjItem = (frmFeltEvaluationReOpen) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 614) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Sales LR Updation");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltLRUpdation.frmFeltLRUpdation", "Felt Sales LR Updation");
                frmFeltLRUpdation ObjItem = (frmFeltLRUpdation) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 611) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Felt Evaluation Closure");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltEvaluation.frmFeltEvaluationClosure", "Felt Evaluation Closure");
                frmFeltEvaluationClosure ObjItem = (frmFeltEvaluationClosure) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 609) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("ReOpen Party Machine Position");
                aFrame.startAppletEx("EITLERP.FeltSales.PartyMachineReOpen.frmPartyMachineReOpen", "ReOpen Party Machine Position");
                frmPartyMachineReOpen ObjItem = (frmPartyMachineReOpen) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 608) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Party Machine Position Closure");
                aFrame.startAppletEx("EITLERP.FeltSales.PartyMachineClosure.frmPartyMachineClosure", "Party Machine Position Closure");
                frmPartyMachineClosure ObjItem = (frmPartyMachineClosure) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 100) {
                AppletFrame aFrame = new AppletFrame("Debit Memo Cancellation");
                aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDMwithoutDNCancellation", "Debit Memo Cancellation");
                frmDMwithoutDNCancellation ObjItem = (frmDMwithoutDNCancellation) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == clsSalesDepositTransfer.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("SALES DEPOSIT TRANSFER");
                aFrame.startAppletEx("EITLERP.Finance.frmSalesDepositTransfer", "SALES DEPOSIT TRANSFER");
                frmSalesDepositTransfer ObjItem = (frmSalesDepositTransfer) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsUpdationof09.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("UPDATION/DELETION OF 09");
                aFrame.startAppletEx("EITLERP.Finance.frmUpdationof09", "UPDATION/DELETION OF 09");
                frmUpdationof09 ObjItem = (frmUpdationof09) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsUnclaimedDepositTransfer.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Deposit Unclaimed");
                aFrame.startAppletEx("EITLERP.Finance.frmUnclaimedDepositTransfer", "Deposit Unclaimed");
                frmUnclaimedDepositTransfer ObjItem = (frmUnclaimedDepositTransfer) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSalesDepositRefund.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Sales Deposit Refund");
                aFrame.startAppletEx("EITLERP.Finance.frmSalesDepositRefund", "Sales Deposit Refund");
                frmSalesDepositRefund ObjItem = (frmSalesDepositRefund) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsDepositAmend.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Deposit Master Updation");
                aFrame.startAppletEx("EITLERP.Finance.frmDepositAmend", " Deposit Master Updation");
                frmDepositAmend ObjItem = (frmDepositAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSalesDepositSchemeTransfer.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Sales Deposit Scheme Transfer");
                aFrame.startAppletEx("EITLERP.Finance.frmSalesDepositSchemeTransfer", "Sales Deposit Scheme Transfer");
                frmSalesDepositSchemeTransfer ObjItem = (frmSalesDepositSchemeTransfer) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSalesInterest.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Sales Interest");
                aFrame.startAppletEx("EITLERP.Finance.frmSalesInterest", "Sales Interest");
                frmSalesInterest ObjItem = (frmSalesInterest) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSalesDepositMaster.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Sales Deposit Master");
                aFrame.startAppletEx("EITLERP.Finance.frmSalesDepositMaster", "Sales Deposit Master");
                frmSalesDepositMaster ObjItem = (frmSalesDepositMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsDepositPM.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Deposit Prematurity");
                aFrame.startAppletEx("EITLERP.Finance.frmDepositPM", "Deposit Prematurity");
                frmDepositPM ObjItem = (frmDepositPM) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsDepositRefund.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Deposit Refund");
                aFrame.startAppletEx("EITLERP.Finance.frmDepositRefund", "Deposit Refund");
                frmDepositRefund ObjItem = (frmDepositRefund) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsCalcInterest.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Interest Calculation");
                aFrame.startAppletEx("EITLERP.Finance.frmCalcInterest", "Interest Calculation");
                frmCalcInterest ObjItem = (frmCalcInterest) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsDepositMaster.ModuleID) {
                DocNo = Table.getValueAt(Table.getSelectedRow(), 0).toString();
                AppletFrame aFrame = new AppletFrame("Deposit Master");
                aFrame.startAppletEx("EITLERP.Finance.frmDepositMaster", "Deposit Master");
                frmDepositMaster ObjItem = (frmDepositMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsOBCInvoice.ModuleID) {
                String OBCDocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("OBC Invoice Entry");
                aFrame.startAppletEx("EITLERP.Finance.frmOBCInvoice", "OBC Invoice Entry");
                frmOBCInvoice ObjItem = (frmOBCInvoice) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, OBCDocNo);
            }

            //Bhavesh
            if (SelModule == clsOBCReturn.ModuleID) {
                String OBCDocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("OBC Return Entry");
                aFrame.startAppletEx("EITLERP.Finance.frmOBCReturn", "OBC Return Entry");
                frmOBCReturn ObjItem = (frmOBCReturn) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, OBCDocNo);
            }
            //

            if (SelModule == clsOBC.ModuleID) {
                String OBCDocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("OBC");
                aFrame.startAppletEx("EITLERP.Finance.frmOBC", "OBC");
                frmOBC ObjItem = (frmOBC) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, OBCDocNo);
            }

            if (SelModule == 57) {
                String AccountID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("General Ledger");
                aFrame.startAppletEx("EITLERP.Finance.frmGL", "General Ledger");
                frmGL ObjItem = (frmGL) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, Integer.parseInt(AccountID));
            }

            if (SelModule == 54) {
                String PartyID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String PartyCode = (String) Table.getValueAt(Table.getSelectedRow(), 1);
                AppletFrame aFrame = new AppletFrame("Party Master");
                aFrame.startAppletEx("EITLERP.Finance.frmPartyMaster", "Party Master");
                frmPartyMaster ObjItem = (frmPartyMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, Integer.parseInt(PartyID), PartyCode);
            }

            if (SelModule == 59 || SelModule == 65 || SelModule == 66 || SelModule == 67 || SelModule == 68 || SelModule == 83 || SelModule == 69 || SelModule == 70 || SelModule == 89 || SelModule == 94 || SelModule == 90 || SelModule == 205) {
                String VoucherNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                clsVoucher.OpenVoucher(VoucherNo, this);
            }

            //Bhavesh
            if (SelModule == clsMemorandumJV.ModuleID) {
                String MDocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("MemorandumJV");
                aFrame.startAppletEx("EITLERP.Finance.frmMemorandumJV", "MemorandumJV");
                frmMemorandumJV ObjItem = (frmMemorandumJV) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, MDocNo);
            }

            //
            if (SelModule == 61) {
                String VoucherNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Advance Payment Adjustment");
                //aFrame.startAppletEx("EITLERP.Finance.frmBillMatch","Advance Payment Adjustment");
                aFrame.startAppletEx("EITLERP.Finance.frmCrAdjustment", "Advance Payment Adjustment");
                frmCrAdjustment ObjItem = (frmCrAdjustment) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, VoucherNo);
            }

            if (SelModule == 62) {
                String VoucherNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Misc. Expense Master");
                aFrame.startAppletEx("EITLERP.Finance.frmExpense", "Misc. Expense Master");
                frmExpense ObjItem = (frmExpense) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, VoucherNo);
            }

            if (SelModule == 13) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Declaration Form");
                aFrame.startAppletEx("EITLERP.Stores.frmDeclarationForm", "Declaration Form");
                frmDeclarationForm ObjItem = (frmDeclarationForm) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 52) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue Requisition");
                aFrame.startAppletEx("EITLERP.Stores.frmIssueReq", "Issue Requisition");
                frmIssueReq ObjItem = (frmIssueReq) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 171) {
                System.out.println("Open Document Button");
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("LC Party Master Module");
                aFrame.startAppletEx("EITLERP.Sales.frmLCPartyMasterUpdated", "LC Party Master Module");
                frmLCPartyMasterUpdated ObjItem = (frmLCPartyMasterUpdated) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 606) {
                System.out.println("Open Document Button");
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Sales LC Party Master");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltLCMaster.frmFeltLCPartyMaster", "Felt Sales LC Party Master");
                frmFeltLCPartyMaster ObjItem = (frmFeltLCPartyMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 64) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue Requisition RM");
                aFrame.startAppletEx("EITLERP.Stores.frmIssueReqRaw", "Issue Requisition RM");
                frmIssueReqRaw ObjItem = (frmIssueReqRaw) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 46) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Service Contract");
                aFrame.startAppletEx("EITLERP.Purchase.frmServiceContract", "Service Contract");
                frmServiceContract ObjItem = (frmServiceContract) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 47) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Service Contract Amendment");
                aFrame.startAppletEx("EITLERP.Purchase.frmServiceContractAmend", "Service Contract Amendment");
                frmServiceContractAmend ObjItem = (frmServiceContractAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 48) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Job work entry");
                aFrame.startAppletEx("EITLERP.Stores.frmJobwork", "Job work entry");
                frmJobwork ObjItem = (frmJobwork) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 50) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Supplier Master Updation");
                aFrame.startAppletEx("EITLERP.frmSupplierAmend", "Supplier Master Updation");
                frmSupplierAmend ObjItem = (frmSupplierAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 51) {
                int lItemID = Integer.parseInt((String) Table.getValueAt(Table.getSelectedRow(), 0));
                AppletFrame aFrame = new AppletFrame("Item Master Updation");
                aFrame.startAppletEx("EITLERP.frmItemAmend", "Item Master Updation");
                frmItemAmend ObjItem = (frmItemAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            //-----*
            if (SelModule == 1) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Item");
                aFrame.startAppletEx("EITLERP.frmItem", "Item Master");
                frmItem ObjItem = (frmItem) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            //-----*
            if (SelModule == 37) {
                String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Supplier");
                aFrame.startAppletEx("EITLERP.frmSupplier", "Supplier");
                frmSupplier ObjItem = (frmSupplier) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
            }

            if (SelModule == 44) {
                int ModuleID = Integer.parseInt(Table.getValueAt(Table.getSelectedRow(), 4).toString());
                if (ModuleID == 80) {
                    String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                    AppletFrame aFrame = new AppletFrame("Invoice Cancellation Request");
                    aFrame.startAppletEx("EITLERP.frmInvoiceCancel", "Invoice Cancellation Request");
                    frmInvoiceCancel ObjItem = (frmInvoiceCancel) aFrame.ObjApplet;
                    ObjItem.frmPA = this;
                    ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
                } else {
                    String lItemID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                    AppletFrame aFrame = new AppletFrame("Document Cancel Request");
                    aFrame.startAppletEx("EITLERP.frmDocCancel", "Document Cancel Request");
                    frmDocCancel ObjItem = (frmDocCancel) aFrame.ObjApplet;
                    ObjItem.frmPA = this;
                    ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
                }
                //                String lItemID=(String)Table.getValueAt(Table.getSelectedRow(), 0);
                //                AppletFrame aFrame=new AppletFrame("Document Cancel Request");
                //                aFrame.startAppletEx("EITLERP.frmDocCancel","Document Cancel Request");
                //                frmDocCancel ObjItem=(frmDocCancel) aFrame.ObjApplet;
                //                ObjItem.frmPA=this;
                //                ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,lItemID);
            }

            //----*
            if (SelModule == 2) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Material Requisition");
                aFrame.startAppletEx("EITLERP.Stores.frmMR", "Material Requisition");
                frmMR ObjDoc = (frmMR) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //----*
            if (SelModule == 42) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Freight Comparison");
                aFrame.startAppletEx("EITLERP.Purchase.frmFreightComparison", "Freight Comparison");
                frmFreightComparison ObjDoc = (frmFreightComparison) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //----*
            if (SelModule == 43) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Freight Calculation");
                aFrame.startAppletEx("EITLERP.Purchase.frmFreightCalculation", "Freight Calculation");
                frmFreightCalculation ObjDoc = (frmFreightCalculation) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //-----*
            if (SelModule == 20) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Summary");
                aFrame.startAppletEx("EITLERP.Purchase.frmQuotApproval", "Quot Approval");
                frmQuotApproval ObjDoc = (frmQuotApproval) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //------*
            if (SelModule == 11) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("NRGP");
                aFrame.startAppletEx("EITLERP.Stores.FrmNRGP_General", "NRGP");
                FrmNRGP_General ObjDoc = (FrmNRGP_General) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //-----*
            if (SelModule == 12) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("RGP");
                aFrame.startAppletEx("EITLERP.Stores.FrmRGP_General", "RGP");
                FrmRGP_General ObjDoc = (FrmRGP_General) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //======*
            if (SelModule == 14) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue");
                aFrame.startAppletEx("EITLERP.Stores.FrmIssue_General", "Issue");
                FrmIssue_General ObjDoc = (FrmIssue_General) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=====*
            if (SelModule == 15) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue");
                aFrame.startAppletEx("EITLERP.Stores.FrmIssue_Raw", "Issue");
                FrmIssue_Raw ObjDoc = (FrmIssue_Raw) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=====*
            if (SelModule == 112) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue RMG Sales");
                aFrame.startAppletEx("EITLERP.Stores.FrmIssue_RMG", "Issue RMG Sales");
                FrmIssue_RMG ObjDoc = (FrmIssue_RMG) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 800) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Issue Stores Sales");
                aFrame.startAppletEx("EITLERP.Stores.FrmIssue_Storesales", "Issue Stores Sales");
                FrmIssue_Storesales ObjDoc = (FrmIssue_Storesales) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //==*
            if (SelModule == 3) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Indent");
                aFrame.startAppletEx("EITLERP.Stores.FrmIndent", "Indent");
                FrmIndent ObjDoc = (FrmIndent) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //========*
            if (SelModule == 40) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("RGP Return");
                aFrame.startAppletEx("EITLERP.Stores.frmRGPReturn", "RGP Return");
                frmRGPReturn ObjDoc = (frmRGPReturn) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=====*
            if (SelModule == 18) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Inquiry");
                aFrame.startAppletEx("EITLERP.Purchase.FrmInquiry", "Inquiry");
                FrmInquiry ObjDoc = (FrmInquiry) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //------ *
            if (SelModule == 4) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Gatepass Requisition");
                aFrame.startAppletEx("EITLERP.Stores.frmGPR", "Gatepass Requisition");
                frmGPR ObjDoc = (frmGPR) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //======*
            if (SelModule == 5) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("MIR General");
                aFrame.startAppletEx("EITLERP.Stores.frmMIRGen", "MIR General");
                frmMIRGen ObjDoc = (frmMIRGen) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //======*
            if (SelModule == 6) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("MIR Raw Material");
                aFrame.startAppletEx("EITLERP.Stores.frmMIR", "MIR Raw Material");
                frmMIR ObjDoc = (frmMIR) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == 35) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("MIR Service");
                aFrame.startAppletEx("EITLERP.Stores.frmMIRService", "MIR Service");
                frmMIRService ObjDoc = (frmMIRService) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=====*
            if (SelModule == 7) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("GRN General");
                aFrame.startAppletEx("EITLERP.Stores.frmGRNGen", "GRN General");
                frmGRNGen ObjDoc = (frmGRNGen) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //==========*
            if (SelModule == 8) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("GRN Raw Material");
                aFrame.startAppletEx("EITLERP.Stores.frmGRN", "GRN Raw Material");
                frmGRN ObjDoc = (frmGRN) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=====*
            if (SelModule == 9) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("RJN General");
                aFrame.startAppletEx("EITLERP.Stores.frmRJNGen", "RJN General");
                frmRJNGen ObjDoc = (frmRJNGen) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //======*
            if (SelModule == 10) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("RJN Raw Material");
                aFrame.startAppletEx("EITLERP.Stores.frmRJN", "RJN Raw Material");
                frmRJN ObjDoc = (frmRJN) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //======*
            if (SelModule == 21) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 1;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //====== *
            if (SelModule == 153) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 9;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //===== *
            if (SelModule == 22) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 2;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //====*
            if (SelModule == 23) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 3;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //====*
            if (SelModule == 24) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 4;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=====*
            if (SelModule == 25) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 5;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //======*
            if (SelModule == 26) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 6;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=====*
            if (SelModule == 27) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
                frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
                ObjDoc.POType = 7;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //Amendments
            //======*
            if (SelModule == 28) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 1;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=======*
            if (SelModule == 29) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 2;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //========*
            if (SelModule == 30) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 3;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=======*
            if (SelModule == 31) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 4;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //======*
            if (SelModule == 32) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 5;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=======*
            if (SelModule == 33) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 6;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //=======*
            if (SelModule == 34) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 7;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //======*
            if (SelModule == 38) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Rate Approval");
                aFrame.startAppletEx("EITLERP.Purchase.frmRateApproval", "Rate Approval");
                frmRateApproval ObjDoc = (frmRateApproval) aFrame.ObjApplet;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSales_Party.ModuleID) {
                String PartyID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String Maincode = (String) Table.getValueAt(Table.getSelectedRow(), 3);
                AppletFrame aFrame = new AppletFrame("Sales Party Master");
                aFrame.startAppletEx("EITLERP.frmSalesParty", "Sales Party Master");
                frmSalesParty ObjItem = (frmSalesParty) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PartyID, Maincode);
            }

            if (SelModule == clsSalesPartyAmend.ModuleID) {
                String AmendID = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Sales Party Master Updation");
                aFrame.startAppletEx("EITLERP.frmSalesPartyAmend", "Sales Party Master Updation");
                frmSalesPartyAmend ObjItem = (frmSalesPartyAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, Integer.parseInt(AmendID));
            }

            if (SelModule == clsFeltPriceList.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Price List");
                aFrame.startAppletEx("EITLERP.Sales.frmFeltPriceList", "Felt Price List");
                frmFeltPriceList ObjItem = (frmFeltPriceList) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsPolicyMaster.ModuleID) {
                String PolicyNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Policy Master");
                aFrame.startAppletEx("EITLERP.Finance.frmPolicyMaster", "Policy Master");
                frmPolicyMaster ObjItem = (frmPolicyMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PolicyNo);
            }

            /*
             if(SelModule==clsPolicyLCMaster.ModuleID) {
             String LCPartyCode=(String)Table.getValueAt(Table.getSelectedRow(), 0);
             AppletFrame aFrame=new AppletFrame("LC Party Master");
             aFrame.startAppletEx("EITLERP.Sales.frmLCPartyMaster","LC Party Master");
             frmLCPartyMaster ObjItem=(frmLCPartyMaster) aFrame.ObjApplet;
             ObjItem.frmPA=this;
             ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,LCPartyCode);
             }
             */
            if (SelModule == 16) {
                String STMNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("STM General");
                aFrame.startAppletEx("EITLERP.Stores.frmSTMGen", "STM General");
                frmSTMGen ObjItem = (frmSTMGen) aFrame.ObjApplet;
                //ObjItem.frmPA=this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, STMNo);
            }

            if (SelModule == 17) {
                String STMRawNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("STM Raw Material");
                aFrame.startAppletEx("EITLERP.Stores.frmSTM", "STM Raw Material");
                frmSTM ObjItem = (frmSTM) aFrame.ObjApplet;
                //ObjItem.frmPA=this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, STMRawNo);
            }

            if (SelModule == clsDrAdjustment.ModuleID) {
                String AdvDrNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Advance Receipt Adjustment");
                //aFrame.startAppletEx("EITLERP.Finance.frmBillMatch","Advance Payment Adjustment");
                aFrame.startAppletEx("EITLERP.Finance.frmDrAdjustment", "Advance Receipt Adjustment");
                frmDrAdjustment ObjItem = (frmDrAdjustment) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, AdvDrNo);
            }

            if (SelModule == 168) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Purchase Order P.O. Amendment Merchandise");
                aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment Merchandise");
                frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
                ObjDoc.POType = 9;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSTMRequisitionRaw.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("STM REQUISITION (RM)");
                aFrame.startAppletEx("EITLERP.Stores.frmSTMReqRaw", "STM REQUISITION (RM)");
                //frmPOAmendGen ObjDoc=(frmPOAmendGen) aFrame.ObjApplet;
                frmSTMReqRaw ObjDoc = (frmSTMReqRaw) aFrame.ObjApplet;
                //ObjDoc.POType=9;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSTMReqGen.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("STM REQUISITION (GEN)");
                aFrame.startAppletEx("EITLERP.Stores.frmSTMReqGen", "STM REQUISITION (GEN)");
                //frmPOAmendGen ObjDoc=(frmPOAmendGen) aFrame.ObjApplet;
                frmSTMReqGen ObjDoc = (frmSTMReqGen) aFrame.ObjApplet;
                //ObjDoc.POType=9;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSTMReceiptGen.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("STM Receipt (General)");
                aFrame.startAppletEx("EITLERP.Stores.frmSTMReceiptGen", "STM Receipt (General)");
                //frmPOAmendGen ObjDoc=(frmPOAmendGen) aFrame.ObjApplet;
                frmSTMReceiptGen ObjDoc = (frmSTMReceiptGen) aFrame.ObjApplet;
                //ObjDoc.POType=9;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsSTMReceiptRaw.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("STM Receipt (Raw Material)");
                aFrame.startAppletEx("EITLERP.Stores.frmSTMReceiptRaw", "STM Receipt (Raw Material)");
                //frmPOAmendGen ObjDoc=(frmPOAmendGen) aFrame.ObjApplet;
                frmSTMReceiptRaw ObjDoc = (frmSTMReceiptRaw) aFrame.ObjApplet;
                //ObjDoc.POType=9;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            if (SelModule == clsFASCardwithoutGRN.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Fixed Asset Card (Without GRN)");
                aFrame.startAppletEx("EITLERP.Finance.frmFASCardwithoutGRN", "Fixed Asset Card (Without GRN)");
                //frmPOAmendGen ObjDoc=(frmPOAmendGen) aFrame.ObjApplet;
                frmFASCardwithoutGRN ObjDoc = (frmFASCardwithoutGRN) aFrame.ObjApplet;
                //ObjDoc.POType=9;
                ObjDoc.frmPA = this;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }

            //            if(SelModule==clsCreditNoteProcessModule.ModuleID) {
            //                DocNo=(String)Table.getValueAt(Table.getSelectedRow(), 0);
            //                AppletFrame aFrame=new AppletFrame("Credit Note Processing Module");
            //                aFrame.startAppletEx("EITLERP.Finance.frmCreditNoteProcessModule","Credit Note Processing Module");
            //                frmCreditNoteProcessModule ObjItem=(frmCreditNoteProcessModule) aFrame.ObjApplet;
            //                ObjItem.frmPA=this;
            //                ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
            //            }
            if (SelModule == 98) {
                AppletFrame aFrame = new AppletFrame("Sales Invoice Due Date");
                aFrame.startAppletEx("EITLERP.Sales.frmSalesInvoiceDueDate", "Sales Invoice Due Date");
                frmSalesInvoiceDueDate ObjItem = (frmSalesInvoiceDueDate) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 99) {
                AppletFrame aFrame = new AppletFrame("Debit Memo Receipt Mapping");
                aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo Receipt Mapping");
                frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            //Sales Order Diversion
            if (SelModule == 602) {
                AppletFrame aFrame = new AppletFrame("Felt Sales Order");
                aFrame.startAppletEx("EITLERP.FeltSales.Order.FrmFeltOrder", "Felt Sales Order");
                FrmFeltOrder ObjItem = (FrmFeltOrder) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 603) {
                AppletFrame aFrame = new AppletFrame("Felt Finishing");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltFinishing.frmFeltFinishing", "Felt Finishing");
                EITLERP.FeltSales.FeltFinishing.frmFeltFinishing ObjItem = (EITLERP.FeltSales.FeltFinishing.frmFeltFinishing) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            //Sales Order Diversion
            if (SelModule == 604) {
                AppletFrame aFrame = new AppletFrame("Felt Sales Order Diversion");
                aFrame.startAppletEx("EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion", "Felt Sales Order Diversion");
                FrmFeltOrderDiversion ObjItem = (FrmFeltOrderDiversion) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 742) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Manual Addition to Diversion List");
                aFrame.startAppletEx("EITLERP.FeltSales.DiversionList.FrmDiversionList", "Manual Addition to Diversion List");
                FrmDiversionList ObjItem = (FrmDiversionList) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 745) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Sales Piece Updation");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceUpdation.frmFeltPieceUpd", "Felt Sales Piece Updation");
                frmFeltPieceUpd ObjItem = (frmFeltPieceUpd) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 750) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Piece Amendment - WIP Pieces");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentApproval.frmPieceAmendApproval", "Felt Piece Amendment - WIP Pieces");
                frmPieceAmendApproval ObjItem = (frmPieceAmendApproval) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 763) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Piece Amendment - STOCK Pieces");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentApproval_STOCK.frmPieceAmendApproval_STOCK", "Felt Piece Amendment - STOCK Pieces");
                frmPieceAmendApproval_STOCK ObjItem = (frmPieceAmendApproval_STOCK) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            //vivek
            if (SelModule == 713) {
                AppletFrame aFrame = new AppletFrame("Felt Export Invoice");
                aFrame.startAppletEx("EITLERP.Production.FeltExportInvoice.frmFeltExportInvoice", "Felt Export Invoice");
                frmFeltExportInvoice ObjItem = (frmFeltExportInvoice) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 715) {
                AppletFrame aFrame = new AppletFrame("Felt Packing Slip");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPacking.frmFeltPacking", "Felt Packing Slip");
                frmFeltPacking ObjItem = (frmFeltPacking) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 712) {
                AppletFrame aFrame = new AppletFrame("Felt Rejection");
                aFrame.startAppletEx("EITLERP.Production.FeltRejection.frmFeltRejection", "Felt Rejection Entry");
                frmFeltRejection ObjItem = (frmFeltRejection) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 709) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Warping Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltWarping.frmFeltWarping", "Felt Warping Entry");
                frmFeltWarping ObjItem = (frmFeltWarping) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1)));
            }

            if (SelModule == 716) {
                AppletFrame aFrame = new AppletFrame("Felt Finishing Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltFinishing.frmFeltFinishing", "Felt Finishing Entry");
                frmFeltFinishing ObjItem = (frmFeltFinishing) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1)));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 711) {
                AppletFrame aFrame = new AppletFrame("Felt Mending Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltMending.frmFeltMending_New", "Felt Mending Entry");
                frmFeltMending_New ObjItem = (frmFeltMending_New) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 710) {
                AppletFrame aFrame = new AppletFrame("Felt Needling Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltNeedling.frmFeltNeedling", "Felt Needling Entry");
                frmFeltNeedling ObjItem = (frmFeltNeedling) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1)));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 723) {
                AppletFrame aFrame = new AppletFrame("Felt Weaving Loom Efficiency Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltWeavingLoom.frmFeltWeavingLoom", "Felt Weaving Loom Efficiency Entry");
                frmFeltWeavingLoom ObjItem = (frmFeltWeavingLoom) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 707) {
                AppletFrame aFrame = new AppletFrame("Felt Weaving Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltWeaving.frmFeltWeaving", "Felt Weaving Entry");
                frmFeltWeaving ObjItem = (frmFeltWeaving) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1)));
                ObjItem.PENDING_DOCUMENT = true;

            }

            if (SelModule == 704) {
                AppletFrame aFrame = new AppletFrame("Felt Heat Setting Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltHeatSetting.frmFeltHeatSetting", "Felt Heat Setting Entry");
                frmFeltHeatSetting ObjItem = (frmFeltHeatSetting) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.Find(EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1)));
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;

            }
            
            if (SelModule == 706) {
                AppletFrame aFrame = new AppletFrame("Felt Marking Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltMarking.frmFeltMarking", "Felt Marking Entry");
                frmFeltMarking ObjItem = (frmFeltMarking) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.Find(EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1)));
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;

            }
            
            if (SelModule == 726) {
                AppletFrame aFrame = new AppletFrame("Felt Splicing Entry");
                aFrame.startAppletEx("EITLERP.Production.FeltSplicing.frmFeltSplicing", "Felt Splicing Entry");
                frmFeltSplicing ObjItem = (frmFeltSplicing) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.Find(EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1)));
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;

            }

            if (SelModule == 720) {
                AppletFrame aFrame = new AppletFrame("Felt Order Updation");
                aFrame.startAppletEx("EITLERP.Production.Felt_Order_Updation.frmFeltOrderUpd", "Felt Order Updation");
                frmFeltOrderUpd ObjItem = (frmFeltOrderUpd) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                //  ObjItem.Find(EITLERPGLOBAL.formatDateDB((String)Table.getValueAt(Table.getSelectedRow(), 0)));
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 701) {
                AppletFrame aFrame = new AppletFrame("Felt Rate Master");
                aFrame.startAppletEx("EITLERP.Production.FeltRateMaster.frmFeltRateMaster", "Felt Rate Master");
                frmFeltRateMaster ObjItem = (frmFeltRateMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 1));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 702) {
                AppletFrame aFrame = new AppletFrame("Felt Rate Update");
                aFrame.startAppletEx("EITLERP.Production.FeltRateMaster.frmFeltRateUpdate", "Felt Rate Update");
                frmFeltRateUpdate ObjItem = (frmFeltRateUpdate) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }
//            if (SelModule == 725) {
//                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
//                AppletFrame aFrame = new AppletFrame("Felt Machine Survey Amendment");
//                aFrame.startAppletEx("EITLERP.Production.FeltMachineSurvey.frmmachinesurveyamend", "Felt Machine Survey Amendment");
//                frmmachinesurveyamend ObjItem = (frmmachinesurveyamend) aFrame.ObjApplet;
//                ObjItem.frmPA = this;
//                //ObjItem.FindWaiting();
//                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
//                ObjItem.PENDING_DOCUMENT = true;
//            }
            if (SelModule == 725) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Machine Survey Amendment");
                aFrame.startAppletEx("EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend", "Felt Machine Survey Amendment");
                frmmachinesurveyAmend ObjItem = (frmmachinesurveyAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 724) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Machine Survey");
                aFrame.startAppletEx("EITLERP.Production.FeltMachineSurvey.frmmachinesurvey", "Felt Machine Survey");
                frmmachinesurvey ObjItem = (frmmachinesurvey) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 727) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("LC Opener Master");
                aFrame.startAppletEx("EITLERP.Sales.frmLCOpenerMaster", "LC Opener Master");
                frmLCOpenerMaster ObjItem = (frmLCOpenerMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 728) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("LC Opener Master Amend1");
                aFrame.startAppletEx("EITLERP.Sales.frmLCOpenerMasterAmend1", "LC Opener Master Amend1");
                frmLCOpenerMasterAmend1 ObjItem = (frmLCOpenerMasterAmend1) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 730) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Discount Rate Master");
                aFrame.startAppletEx("EITLERP.Production.FeltDiscRateMaster.frmDiscRateMaster", "Felt Discount Rate Master");
                frmDiscRateMaster ObjItem = (frmDiscRateMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 732) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Unadjusted TRN");
                aFrame.startAppletEx("EITLERP.Production.FeltUnadj.frmFeltUnadj", "Felt Unadjusted TRN");
                frmFeltUnadj ObjItem = (frmFeltUnadj) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 735) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                if (DocNo.startsWith("GRI") || DocNo.startsWith("GRP")) {
                    AppletFrame aFrame = new AppletFrame("Felt GR Credit Note");
                    aFrame.startAppletEx("EITLERP.Production.FeltCreditNote.frmFeltGRCreditNote", "Felt GR Credit Note");
                    frmFeltGRCreditNote ObjItem = (frmFeltGRCreditNote) aFrame.ObjApplet;
                    ObjItem.frmPA = this;
                    //ObjItem.FindWaiting();
                    ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                    ObjItem.PENDING_DOCUMENT = true;

                } else {
                    AppletFrame aFrame = new AppletFrame("Felt Credit Note");
                    aFrame.startAppletEx("EITLERP.Production.FeltCreditNote.frmFeltCreditNote", "Felt Credit Note");
                    frmFeltCreditNote ObjItem = (frmFeltCreditNote) aFrame.ObjApplet;
                    ObjItem.frmPA = this;
                    //ObjItem.FindWaiting();
                    ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                    ObjItem.PENDING_DOCUMENT = true;
                }
            }

            if (SelModule == 737) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Year End Form 1 Credit Note");
                aFrame.startAppletEx("EITLERP.Production.FeltYearEndDiscountCreditNote.frmFeltYearEndDisc", "Felt Year End Form 1 Credit Note");
                frmFeltYearEndDisc ObjItem = (frmFeltYearEndDisc) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 738) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Year End Form 2 Credit Note");
                aFrame.startAppletEx("EITLERP.Production.YearEndDiscountForm2.frmFeltYearEndDisc2", "Felt Year End Form 2 Credit Note");
                frmFeltYearEndDisc2 ObjItem = (frmFeltYearEndDisc2) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 708) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Sales Perfoma Invoice");
                aFrame.startAppletEx("EITLERP.FeltSales.Perfomainvoice.frmperfomainvoice", "Felt Sales Perfoma Invoice");
                frmperfomainvoice ObjItem = (frmperfomainvoice) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 739) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Group Master");
                aFrame.startAppletEx("EITLERP.FeltSales.GroupMaster.frmFelGroupMaster", "Felt Group Master");
                frmFelGroupMaster ObjItem = (frmFelGroupMaster) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 740) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Reopen Bale");
                aFrame.startAppletEx("EITLERP.FeltSales.ReopenBale12.frmFeltReopenBale", "Felt Reopen Bale");
                frmFeltReopenBale ObjItem = (frmFeltReopenBale) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 743) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Group Master Amend");
                aFrame.startAppletEx("EITLERP.FeltSales.GroupMasterAmend.frmFelGroupMasterAmend", "Felt Group Master Amend");
                frmFelGroupMasterAmend ObjItem = (frmFelGroupMasterAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 610) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Felt Invoice Cancellation Request");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltInvReport.frmInvoiceCancel", "Felt Invoice Cancellation Request");
                EITLERP.FeltSales.FeltInvReport.frmInvoiceCancel ObjItem = (EITLERP.FeltSales.FeltInvReport.frmInvoiceCancel) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 80) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Sales Invoice");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltInvReport.frmFeltSalesInvoice", "Felt Sales Invoice");
                EITLERP.FeltSales.FeltInvReport.frmFeltSalesInvoice ObjItem = (EITLERP.FeltSales.FeltInvReport.frmFeltSalesInvoice) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo, DocDate);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 754) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                if(DocNo.contains("F6")){
                AppletFrame aFrame = new AppletFrame("Felt Invoice Parameter Modification F6");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltInvoiceParameterModificationF6.frmFeltInvoiceParameterModificationf6Form", "Felt Invoice Parameter Modification F6");
                EITLERP.FeltSales.FeltInvoiceParameterModificationF6.frmFeltInvoiceParameterModificationf6Form ObjItem = (EITLERP.FeltSales.FeltInvoiceParameterModificationF6.frmFeltInvoiceParameterModificationf6Form) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;    
                }else{
                AppletFrame aFrame = new AppletFrame("Felt Invtoice Parameter Modification");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm", "Felt Invtoice Parameter Modification");
                EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm ObjItem = (EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;    
                }
                
            }

            if (SelModule == 744) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Sales Return");
                aFrame.startAppletEx("EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns", "Felt Sales Return");
                EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns ObjItem = (EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 759) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Piece Amend");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmend", "Felt Piece Amend");
                EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmend ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 760) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Piece Division");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceDivision.FrmPieceDivision", "Felt Piece Division");
                EITLERP.FeltSales.PieceDivision.FrmPieceDivision ObjItem = (EITLERP.FeltSales.PieceDivision.FrmPieceDivision) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 761) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Invoice Parameter Modification Cancellation");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmInvoiceParameterCancel", "Felt Invoice Parameter Modification Cancellation");
                EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmInvoiceParameterCancel ObjItem = (EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmInvoiceParameterCancel) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 762) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Transporter Weight");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltTransporterWeight.frmFeltTransporterWeigthEntryForm", "Felt Transporter Weight");
                frmFeltTransporterWeigthEntryForm ObjItem = (frmFeltTransporterWeigthEntryForm) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 765) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Invoice F6 Process Parameter");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltProcessInvoiceVariable.frmFeltProcessInvoiceVariable", "Felt Invoice F6 Process Parameter");
                EITLERP.FeltSales.FeltProcessInvoiceVariable.frmFeltProcessInvoiceVariable ObjItem = (EITLERP.FeltSales.FeltProcessInvoiceVariable.frmFeltProcessInvoiceVariable) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 766) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Budget Upload");
                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetUpload", "Felt Budget Upload");
                EITLERP.FeltSales.Budget.FrmBudgetUpload ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetUpload) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
//            if (SelModule == 768) {
//                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
//                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
//                AppletFrame aFrame = new AppletFrame("Felt Budget Manual Entry");
//                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetManual", "Felt Budget Manual Entry");
//                EITLERP.FeltSales.Budget.FrmBudgetManual ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetManual) aFrame.ObjApplet;
//                ObjItem.frmPA = this;
//                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
//                ObjItem.PENDING_DOCUMENT = true;
//                aFrame.setVisible(true);
//                aFrame.dispose();
//            }

            if (SelModule == 768) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Budget Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetEntry", "Felt Budget Entry");
                EITLERP.FeltSales.Budget.FrmBudgetEntry ObjItem = (EITLERP.FeltSales.Budget.FrmBudgetEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 769) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("FELT GROUP CRITICAL LIMIT ENHANCEMENT FOR SELECTED BALE DETAILS");
                aFrame.startAppletEx("EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFelGroupCriticalLimitEnhancement", "FELT GROUP CRITICAL LIMIT ENHANCEMENT FOR SELECTED BALE DETAILS");
                EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFelGroupCriticalLimitEnhancement ObjItem = (EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFelGroupCriticalLimitEnhancement) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 770) {
                AppletFrame aFrame = new AppletFrame("Felt Location Assignment");
                aFrame.startAppletEx("EITLERP.FeltSales.LocationAssignment.frmFeltLocationAssignment", "Felt Location Assignment");
                frmFeltLocationAssignment ObjItem = (frmFeltLocationAssignment) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 774) {
                AppletFrame aFrame = new AppletFrame("WIP Piece Amendment based on Machine Master");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentWIP.frmPieceAmendWIP", "WIP Piece Amendment based on Machine Master");
                frmPieceAmendWIP ObjItem = (frmPieceAmendWIP) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == 776) {
                AppletFrame aFrame = new AppletFrame("WIP Piece DELINK Approval");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentDelink.frmPieceAmendDELINK", "WIP Piece DELINK Approval");
                frmPieceAmendDELINK ObjItem = (frmPieceAmendDELINK) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }

            if (SelModule == clsWarpingBeamOrder.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Warping Beam Order Amend Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrder2", "Warping Beam Order Entry");
                frmWarpingBeamOrder2 ObjItem = (frmWarpingBeamOrder2) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == clsWarpingBeamOrderAmend.ModuleID) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                AppletFrame aFrame = new AppletFrame("Warping Beam Order Amend Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltWarpingBeamOrder.frmWarpingBeamOrderAmend", "Warping Beam Order Amend Entry");
                frmWarpingBeamOrderAmend ObjItem = (frmWarpingBeamOrderAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                //ObjItem.FindWaiting();
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }

            if (SelModule == 779) {
                AppletFrame aFrame = new AppletFrame("Diversion Prior Approval");
                aFrame.startAppletEx("EITLERP.FeltSales.DiversionLoss.FrmFeltDiversionLoss", "Diversion Prior Approval");
                FrmFeltDiversionLoss ObjItem = (FrmFeltDiversionLoss) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find((String) Table.getValueAt(Table.getSelectedRow(), 0));
                ObjItem.PENDING_DOCUMENT = true;
            }
            if (SelModule == 790) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("SDML SDF Production Process Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.SDF.ProductionProcess.FrmProductionEntry", "SDML SDF Production Process Entry");
                EITLERP.FeltSales.SDF.ProductionProcess.FrmProductionEntry ObjItem = (EITLERP.FeltSales.SDF.ProductionProcess.FrmProductionEntry) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 795) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("SDML SDF Production Process Amendment");
                aFrame.startAppletEx("EITLERP.FeltSales.SDF.ProductionProcessAmend.FrmProductionAmend", "SDML SDF Production Process Amendment");
                EITLERP.FeltSales.SDF.ProductionProcessAmend.FrmProductionAmend ObjItem = (EITLERP.FeltSales.SDF.ProductionProcessAmend.FrmProductionAmend) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 798) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Order Confirmation Form");
                aFrame.startAppletEx("EITLERP.FeltSales.OrderConfirmation.FrmPieceOC", "Order Confirmation Form");
                EITLERP.FeltSales.OrderConfirmation.FrmPieceOC ObjItem = (EITLERP.FeltSales.OrderConfirmation.FrmPieceOC) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 807) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Spilover Assign UPN");
                aFrame.startAppletEx("EITLERP.FeltSales.PieceSpilover.FrmFeltAssignUPN", "Felt Spilover Assign UPN");
                EITLERP.FeltSales.PieceSpilover.FrmFeltAssignUPN ObjItem = (EITLERP.FeltSales.PieceSpilover.FrmFeltAssignUPN) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
                aFrame.setVisible(true);
                aFrame.dispose();
            }
            if (SelModule == 820) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Production Loomwise Weaving Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.WVG_Prod_Loom_WVR.FrmWVG_Prod_Loom_WVR", "Production Loomwise Weaving Entry");
                EITLERP.FeltSales.WVG_Prod_Loom_WVR.FrmWVG_Prod_Loom_WVR ObjItem = (EITLERP.FeltSales.WVG_Prod_Loom_WVR.FrmWVG_Prod_Loom_WVR) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
//                aFrame.setVisible(true);
//                aFrame.dispose();
            }
            if (SelModule == 835) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Production Loomwise Weaving Entry");
                aFrame.startAppletEx("EITLERP.FeltSales.WVG_Prod_Loom_WVR_NEW.FrmWVG_Prod_Loom_WVR_New", "Production Loomwise Weaving Entry");
                EITLERP.FeltSales.WVG_Prod_Loom_WVR_NEW.FrmWVG_Prod_Loom_WVR_New ObjItem = (EITLERP.FeltSales.WVG_Prod_Loom_WVR_NEW.FrmWVG_Prod_Loom_WVR_New) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
//                aFrame.setVisible(true);
//                aFrame.dispose();
            }
            if (SelModule == 661) {
                DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                String DocDate = EITLERPGLOBAL.formatDateDB((String) Table.getValueAt(Table.getSelectedRow(), 1));
                AppletFrame aFrame = new AppletFrame("Felt Sales Assign UPN to Obsolete Piece");
                aFrame.startAppletEx("EITLERP.FeltSales.ObsoletePieceMapping.FrmFeltObsoleteAssignUPN", "Felt Sales Assign UPN to Obsolete Piece");
                EITLERP.FeltSales.ObsoletePieceMapping.FrmFeltObsoleteAssignUPN ObjItem = (EITLERP.FeltSales.ObsoletePieceMapping.FrmFeltObsoleteAssignUPN) aFrame.ObjApplet;
                ObjItem.frmPA = this;
                ObjItem.Find(DocNo);
                ObjItem.PENDING_DOCUMENT = true;
//                aFrame.setVisible(true);
//                aFrame.dispose();
            }
        }
    }//GEN-LAST:event_cmdOpenActionPerformed


    private void cmdShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowActionPerformed
        // TODO add your handling code here:
        lblProcess.setText("...");
        if (OpgRecDate.isSelected()) {
            SorOn = EITLERPGLOBAL.OnRecivedDate;
        }

        if (OpgDocDate.isSelected()) {
            SorOn = EITLERPGLOBAL.OnDocDate;
        }

        if (OpgDocNo.isSelected()) {
            SorOn = EITLERPGLOBAL.OnDocNo;
        }

        if (OpgAuthDelUser.isSelected()) {
            if (cmbModule.getSelectedIndex() > -1) {
                int CurUserID = EITLERPGLOBAL.gUserID;

                EITLERPGLOBAL.gUserID = EITLERPGLOBAL.gAuthorityUserID;
                SelModule = EITLERPGLOBAL.getComboCode(cmbModule);
                GenerateGrid();
                EITLERPGLOBAL.gUserID = CurUserID;
            }
        } else {
            EITLERPGLOBAL.gUserID = defUserID;
            if (!chkUser.isSelected()) {
                if (cmbModule.getSelectedIndex() > -1) {
                    SelModule = EITLERPGLOBAL.getComboCode(cmbModule);
                    GenerateGrid();
                }
            } else {
                if (cmbModule.getSelectedIndex() > -1) {
                    int CurUserID = EITLERPGLOBAL.gUserID;
                    //EITLERPGLOBAL.gUserID = EITLERPGLOBAL.getComboCode(cmbUserold);
                    EITLERPGLOBAL.gUserID = Integer.parseInt(cmbuser.getText());
                    SelModule = EITLERPGLOBAL.getComboCode(cmbModule);
                    GenerateGrid();
                    EITLERPGLOBAL.gUserID = CurUserID;
                }
            }

        }


    }//GEN-LAST:event_cmdShowActionPerformed

    private void btnExportToExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportToExcelActionPerformed
        // TODO add your handling code here:
        try {
            exp.fillData(Table, new File("/root/Desktop/PendingDocumentList.xls"), "First Sheet");
            exp.fillData(Table, new File("D://PendingDocumentList.xls"), "First Sheet");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/PendingDocumentList.xls' successfully in Linux PC or 'D://PendingDocumentList.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnExportToExcelActionPerformed

    private void cmbuserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbuserKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            aList.SQL = "SELECT USER_ID,USER_NAME FROM DINESHMILLS.D_COM_USER_MASTER WHERE COALESCE(LOCKED,0)=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                cmbuser.setText(aList.ReturnVal);
                username.setText(data.getStringValueFromDB("SELECT USER_NAME FROM DINESHMILLS.D_COM_USER_MASTER WHERE USER_ID=" + aList.ReturnVal));
                //txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_cmbuserKeyPressed

    private void OpgAuthDelUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgAuthDelUserItemStateChanged
        if (OpgAuthDelUser.isSelected()) {
            cmbAuthority.setVisible(true);
            //username.setVisible(true);
            GenerateAuthDelCombo();
            if (chkUser.isSelected()) {
                chkUser.setSelected(false);
                cmbuser.setText("");
                username.setText("");
                cmbuser.setVisible(false);
                username.setVisible(false);
            }
        } else {
            cmbAuthority.setVisible(false);
            //username.setVisible(false);
        }
    }//GEN-LAST:event_OpgAuthDelUserItemStateChanged

    private void OpgDefaultUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgDefaultUserItemStateChanged
        if (OpgDefaultUser.isSelected()) {
            cmbAuthority.setVisible(false);
            //username.setVisible(true);
            GenerateAuthDelCombo();
        } else {
            cmbAuthority.setVisible(true);
            //username.setVisible(false);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_OpgDefaultUserItemStateChanged

    public void RefreshView() {
        try {
            int SelectedModule = EITLERPGLOBAL.getComboCode(cmbModule);
            GenerateCombo();
            EITLERPGLOBAL.setComboIndex(cmbModule, SelectedModule);
            GenerateGrid();
        } catch (Exception e) {

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgAuthDelUser;
    private javax.swing.JRadioButton OpgDefaultUser;
    private javax.swing.JRadioButton OpgDocDate;
    private javax.swing.JRadioButton OpgDocNo;
    private javax.swing.JRadioButton OpgRecDate;
    private javax.swing.JTable Table;
    private javax.swing.JButton btnExportToExcel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chkDept;
    private javax.swing.JCheckBox chkUser;
    private javax.swing.JComboBox cmbAuthority;
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JComboBox cmbFinYear;
    private javax.swing.JComboBox cmbModule;
    private javax.swing.JComboBox cmbUserold;
    private javax.swing.JTextField cmbuser;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdOpen;
    private javax.swing.JButton cmdOpen1;
    private javax.swing.JButton cmdRefresh;
    private javax.swing.JButton cmdShow;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblProcess;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUser;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables

    //    private void FormatGrid() {
    //
    //        //Each Module will have different Table Model
    //
    //        if(SelModule==1) //Item Master
    //        {
    //            FormatForItemMaster();
    //        }
    //        else {
    //            FormatGridNormal();
    //        }
    //    }
    private void FormatGrid() {

        //Each Module will have different Table Model
        if (SelModule == 1) //Item Master
        {
            FormatForItemMaster();
        } else if (SelModule == 44) { //Document Cancelltion
            FormatDocCancelGrid();
        } else {
            FormatGridNormal();
        }
    }

    private void FormatDocCancelGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Department");
        DataModel.addColumn("Module ID");
        DataModel.addColumn("Module Name");

        //Now hide the column 1
        TableColumnModel ColModel = Table.getColumnModel();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private void GenerateGrid() {
        FormatGrid();

        if (SelModule == 878) {
            GeneratePPRSPlanning();
        }

        if (SelModule == 876) {
            GenerateZoneMasterGrid();
        }

        if (SelModule == 877) {
            GenerateZonePartySelectionGrid();
        }

        if (SelModule == 206) {
            GenerateAuthorityDelegationReqGrid();
        }

        if (SelModule == 872) {
            GenerateFeltNewBudgetEntryGrid();
        }

        if (SelModule == 868) {
            GenerateDailyRokdiAttendanceGrid();
        }

        if (SelModule == 865) {
            GenerateExcessManpowerGrid();
        }

        if (SelModule == 866) {
            GenerateShiftScheduleChangeGrid();
        }
        if (SelModule == 863) {
            GenerateShiftScheduleGrid();
        }
        if (SelModule == 862) {
            GenerateRokdiSelectionGrid();
        }
        if (SelModule == 860) {
            GenerateTravelVoucherGrid();
        }
        if (SelModule == 859) {
            GenerateTravelSanctionAmendGrid();
        }
        if (SelModule == 858) {
            GenerateTravelSanctionGrid();
        }
        if (SelModule == 856) {
            GenerateRetainerCoffEntryGrid();
        }
        if (SelModule == 855) {
            GenerateSpilloverReschedulingGrid();
        }
        if (SelModule == 867) {
            GenerateSpilloverReschedulingNewGrid();
        }
        if (SelModule == 870) {
            GeneratePieceClubbingGrid();
        }
        if (SelModule == 871) {
            GeneratePieceClubbingAmendGrid();
        }

        if (SelModule == 874) {
            GeneratePieceClubbingReschedulingGrid();
        }

        if (SelModule == 857) {
            GenerateSpecialRequestGrid();
        }

        if (SelModule == 861) {
            GenerateSampleOrder();
        }

        if (SelModule == 786) {
            GenerateBeamOrderClosureGrid();
        }

        if (SelModule == 852) {
            GenerateAllowBookingForProjection();
        }

        if (SelModule == 864) {
            GenerateOCChange();
        }

        if (SelModule == 851) {
            GenerateMRFGrid();
        }
        if (SelModule == 849) {
            if (!chkUser.isSelected()) {
                GenerateIncrementProposal();
            }
        }

        if (SelModule == 662) {
            GenerateObsoleteScrap();
        }

        if (SelModule == 850) {
            GenerateSDFSpirallingGrid();
        }

        if (SelModule == 635) {
            GeneratePostAuditDRMGrid();
        }

        if (SelModule == 846) {
            GenerateBeamGaitingStatusGrid();
        }

        if (SelModule == 842) {
            GenerateRRAGrid();
        }

        if (SelModule == 839) {
            GenerateRRGrid();
        }

        if (SelModule == 837) {
            GenerateDailyAttendanceGrid();
        }

        if (SelModule == 204) {
            GeneratePaymentReqGrid();
        }

        if (SelModule == 834) {
            GenerateBudgetReviewEntryGrid();
        }

        if (SelModule == 634) {
            GenerateFeltPartyContactGrid();
        }

        if (SelModule == 831) {
            GenerateSuperannuationGrid();
        }
        if (SelModule == 615) {
            GenerateFeltInvCvrLtrGrid();
        }
        if (SelModule == 833) {
            Generate_PerformanceTracking();
        }
        if (SelModule == 830) {
            GenerateMissPunchRequestGrid();
        }
        if (SelModule == 829) {
            GenerateContractRokdiProcessGrid();
        }
        if (SelModule == 827) {
            GenerateContractRokdiEntryGrid();
        }
        if (SelModule == 817) {
            GenerateRokdiProcessGrid();
        }
        if (SelModule == 818) {
            GenerateCoffProcessGrid();
        }
        if (SelModule == 816) {
            GenerateRokdiGrid();
        }
        if (SelModule == 815) {
            GenerateCoffGrid();
        }

        if (SelModule == 826) {
            GenerateMonthlyAttendanceGrid();
        }

        if (SelModule == 823) {
            GenerateSpecialDeductionGrid();
        }

        if (SelModule == 632) {
            GenerateFeltPieceAmendmentPOGrid();
        }

        if (SelModule == 631) {
            GenerateFeltSRSEGrid();
        }

        if (SelModule == 806) {
            GenerateShiftUploadGrid();
        }

        if (SelModule == 804) {
            GenerateShiftChangeGrid();
        }

        if (SelModule == 813) {
            GenerateLeaveUpdateGrid();
        }

        if (SelModule == 811) {
            GenerateLeaveEntryGrid();
        }

        if (SelModule == 808) {
            GenerateHolidayWeekOffGrid();

        }

        if (SelModule == 805) {
            GenerateStaffGatepassGrid();

        }

        if (SelModule == 822) {
            GenerateUpdatePunchDateGrid();
        }

        if (SelModule == 819) {
            GenerateSpecialSanctionGrid();
        }

        if (SelModule == 810) {
            GenerateTimeCorrectionGrid();
        }

        if (SelModule == 802) {
            GenerateWHPostInvGrid();
        }

        if (SelModule == 803) {
            GenerateWHGatepassEntryGrid();
        }

        if (SelModule == 630) {
            GenerateFeltAutoPISelectionGrid();
        }

        if (SelModule == clsTrailPieceEntry.ModuleID) {
            GenerateTrailPieceGrid();
        }
        if (SelModule == clsTrailPieceDispatchEntry.ModuleID) {
            GenerateTrailPieceDispatchGrid();
        }
        if (SelModule == 794) {
            GenerateSDMLToGIDC_NRGPGrid();
        }

        if (SelModule == 793) {
            GenerateGIDCToSDML_NRGPGrid();
        }

        if (SelModule == clsGIDCInstruction.ModuleID) {
            GenerateGIDCInstructionApproval();
        }
        if (SelModule == clsGIDCInstructionAmend.ModuleID) {
            GenerateGIDCInstructionAmendApproval();
        }
        if (SelModule == 792) {
            GenerateGIDCDespatchToSDMLGrid();
        }

        if (SelModule == 626) {
            GenerateFeltPDCAmendGrid();
        }

        if (SelModule == 625) {
            GenerateFeltPDCGrid();
        }

        if (SelModule == 764) {
            GenerateFeltRejectionGrid();
        }

        if (SelModule == clsWarpingBeamOrderHDS.ModuleID) {
            GenerateFeltWarpingBeamOrderDryerApproval();
        }
        if (SelModule == clsWarpingBeamOrderHDSAmend.ModuleID) {
            GenerateFeltWarpingBeamOrderDryerAmendApproval();
        }
        if (SelModule == 785) {
            GenerateFeltProductionSeamingGrid();
        }
        if (SelModule == 601) {
            GenerateFeltQltRateMasterGrid();
        }

        if (SelModule == 621) {
            GenerateFeltPieceAmendmentWIPGrid();
        }
        if (SelModule == 622) {
            GenerateFeltPieceAmendmentSTOCKGrid();
        }
        if (SelModule == 623) {
            GenerateFeltPieceAmendmentCHRGrid();
        }
        if (SelModule == 624) {
            GenerateFeltPieceAmendmentEXPORTGrid();
        }

        if (SelModule == 612) {
            GenerateFeltEvaluationReOpenGrid();
        }

        if (SelModule == 614) {
            GenerateFeltLRUpdationGrid();
        }

        if (SelModule == 611) {
            GenerateFeltEvaluationClosureGrid();
        }

        if (SelModule == clsPartyMachineReOpen.ModuleID) {
            GeneratePartyMachinePositionReOpenGrid();
        }

        if (SelModule == clsPartyMachineClosure.ModuleID) {
            GeneratePartyMachinePositionGrid();
        }

        if (SelModule == 100) {
            GenerateDebitMemoCancellationGrid();
        }

        if (SelModule == clsSalesDepositTransfer.ModuleID) {
            GenerateSalesDepositTransferGrid();
        }
        if (SelModule == clsUpdationof09.ModuleID) {
            GenerateUpdationof09Grid();
        }

        if (SelModule == clsUnclaimedDepositTransfer.ModuleID) {
            GenerateUnclaimedDepositTransferGrid();
        }

        if (SelModule == clsSalesDepositRefund.ModuleID) {
            GenerateSalesDepositRefundGrid();
        }

        if (SelModule == clsSalesDepositSchemeTransfer.ModuleID) {
            GenerateSalesDepositSchemeGrid();
        }

        if (SelModule == clsSalesInterest.ModuleID) {
            GenerateSalesInterestGrid();
        }

        if (SelModule == clsSalesDepositMaster.ModuleID) {
            GenerateSalesDepositMasterGrid();
        }

        if (SelModule == clsDepositPM.ModuleID) {
            GeneratePMGrid();
        }

        if (SelModule == clsDepositRefund.ModuleID) {
            GenerateRefundGrid();
        }

        if (SelModule == clsCalcInterest.ModuleID) {
            GenerateInterestGrid();
        }

        if (SelModule == clsDepositMaster.ModuleID) {
            GenerateDepositMasterGrid();
        }

        if (SelModule == clsOBCInvoice.ModuleID) {
            GenerateOBCInvoiceGrid();
        }

        //bhavesh
        if (SelModule == clsOBCReturn.ModuleID) {
            GenerateOBCReturnGrid();
        }

        //
        if (SelModule == clsOBC.ModuleID) {
            GenerateOBCGrid();
        }

        //        if(SelModule==61) {
        //            GenerateBillMatchGrid();
        //        }
        if (SelModule == 106) {
            GenerateDepositAmendGrid();
        }
        if (SelModule == 62) {
            GenerateMiscExpenseGrid();
        }

        if (SelModule == 57) {
            GenerateGLGrid();
        }

        if (SelModule == 54) {
            GeneratePartyGrid();
        }

        if (SelModule == clsMemorandumJV.ModuleID) {
            GenerateMemVoucherGrid(SelModule);
        }
        if (SelModule == 59 || SelModule == 65 || SelModule == 66 || SelModule == 67 || SelModule == 83 || SelModule == 68 || SelModule == 69 || SelModule == 70 || SelModule == 89 || SelModule == 94 || SelModule == 90 || SelModule == 205) {
            GenerateVoucherGrid(SelModule);
        }

        if (SelModule == 13) {
            GenerateDecFormGrid();
        }

        if (SelModule == 64) {
            GenerateIssueReqRawGrid();
        }

        if (SelModule == 52) {
            GenerateIssueReqGrid();
        }

        if (SelModule == 46) {
            GenerateServiceContractGrid();
        }

        if (SelModule == 47) {
            GenerateServiceContractAmendGrid();
        }

        if (SelModule == 48) {
            GenerateJobWorkGrid();
        }

        if (SelModule == 50) {
            GenerateSuppAmendGrid();
        }

        if (SelModule == 51) {
            GenerateItemAmendGrid();
        }

        if (SelModule == 44) {
            GenerateDocCancelGrid();
        }

        if (SelModule == 43) {
            GenerateFreightCalculationGrid();
        }

        if (SelModule == 42) {
            GenerateFreightComparisonGrid();
        }

        if (SelModule == 1) {
            GenerateItemGrid();
        }

        if (SelModule == 37) {
            GenerateSuppGrid();
        }

        if (SelModule == 2) {
            GenerateMRGrid();
        }

        if (SelModule == 20) {
            GenerateSummaryGrid();
        }

        if (SelModule == 11) {
            GenerateNRGPGrid();
        }

        if (SelModule == 12) {
            GenerateRGPGrid();
        }

        if (SelModule == 14) {
            GenerateIssueGenGrid();
        }

        if (SelModule == 15) {
            GenerateIssueRawGrid();
        }

        if (SelModule == 112) {
            GenerateIssueRMGGrid();
        }

        if (SelModule == 800) {
            GenerateIssueStoresGrid();
        }

        if (SelModule == 40) {
            GenerateRGPReturnGrid();
        }

        if (SelModule == 3) {
            GenerateIndentGrid();
        }

        if (SelModule == 4) {
            GenerateGPRGrid();
        }

        if (SelModule == 5) {
            GenerateMIRGrid(1);
        }

        if (SelModule == 6) {
            GenerateMIRGrid(2);
        }

        if (SelModule == 35) {
            GenerateMIRGrid(3);
        }

        if (SelModule == 7) {
            GenerateGRNGrid(1);
        }

        if (SelModule == 8) {
            GenerateGRNGrid(2);
        }

        if (SelModule == 9) {
            GenerateRJNGrid(1);
        }

        if (SelModule == 10) {
            GenerateRJNGrid(2);
        }

        if (SelModule == 18) {
            GenerateInquiryGrid();
        }

        if (SelModule == 21) {
            GeneratePOGrid(1);
        }

        if (SelModule == 22) {
            GeneratePOGrid(2);
        }

        if (SelModule == 23) {
            GeneratePOGrid(3);
        }

        if (SelModule == 24) {
            GeneratePOGrid(4);
        }

        if (SelModule == 25) {
            GeneratePOGrid(5);
        }

        if (SelModule == 26) {
            GeneratePOGrid(6);
        }

        if (SelModule == 27) {
            GeneratePOGrid(7);
        }

        if (SelModule == 153) {
            GeneratePOGrid(9);
        }

        if (SelModule == 28) {
            GenerateAmendGrid(1);
        }

        if (SelModule == 29) {
            GenerateAmendGrid(2);
        }

        if (SelModule == 30) {
            GenerateAmendGrid(3);
        }

        if (SelModule == 31) {
            GenerateAmendGrid(4);
        }

        if (SelModule == 32) {
            GenerateAmendGrid(5);
        }

        if (SelModule == 33) {
            GenerateAmendGrid(6);
        }

        if (SelModule == 34) {
            GenerateAmendGrid(7);
        }

        /*if (SelModule == 35) {
         GenerateCIFGrid();
         }*/
        if (SelModule == 38) {
            GenerateRateApprovalGrid();
        }

        if (SelModule == clsSales_Party.ModuleID) {
            GenerateSalesPartyGrid();
        }

        if (SelModule == clsSalesPartyAmend.ModuleID) {
            GenerateSalesPartyAmendGrid();
        }

        if (SelModule == clsFeltPriceList.ModuleID) {
            GenerateFeltPriceListGrid();
        }

        if (SelModule == clsPolicyMaster.ModuleID) {
            GeneratePolicyGrid();
        }
        /*
         if(SelModule==clsPolicyLCMaster.ModuleID) {
         GeneratePolicyLCMasterGrid();
            
         //        if(SelModule==clsCreditNoteProcessModule.ModuleID) {
         //            GenerateCreditNoteProcessGrid();
         //        }
         }
         */
        if (SelModule == 16) {
            GenerateSTMGrid(1);
        }

        if (SelModule == 17) {
            GenerateSTMRawGrid(2);
        }

        if (SelModule == 61) {
            GeneratePaymentAdjustmentGrid();
        }

        if (SelModule == 92) {
            GenerateRedeiptAdjustmentGrid();
        }
        if (SelModule == 168) {
            GenerateAmendGrid(9);
        }

        if (SelModule == 180) {
            GenerateSTMReqRawGrid();
        }
        if (SelModule == 181) {
            GenerateSTMReceiptRawGrid();
        }
        if (SelModule == 185) { //STM RECEIPT GEN

            GenerateSTMReceiptGenGrid();
        }
        if (SelModule == 186) { //STM REQ. GEN

            GenerateSTMReqGenGrid();
        }

        if (SelModule == 189) {
            GenerateFASCardWithoutGRNGrid();
        }

        if (SelModule == 98) {
            GenerateSalesInvoiceDueDateGrid();
        }
        if (SelModule == 99) {
            GenerateDebitMemoReceiptMappingGrid();
        }

        //vivek
        if (SelModule == 715) {
            GenerateFeltPackingGrid();
        }

        if (SelModule == 709) {
            GenerateFeltWarpingGrid();
        }

        if (SelModule == 716) {
            GenerateFeltFinishingGrid();
        }

        if (SelModule == 712) {
            GenerateFeltRejectionGrid();
        }

        if (SelModule == 710) {
            GenerateFeltProductionNeedlingGrid();
        }

        if (SelModule == 723) {
            GenerateFeltProductionWvgLoomEffGrid();
        }

        if (SelModule == 711) {
            GenerateFeltProductionMendingGrid();
        }

        if (SelModule == 707) {
            GenerateFeltProductionWeavingGrid();
        }

        if (SelModule == 704) {
            GenerateFeltProductionHeatSettingGrid();
        }
        
        if (SelModule == 706) {
            GenerateFeltProductionMarkingGrid();
        }
        
        if (SelModule == 726) {
            GenerateFeltProductionSplicingGrid();
        }

        
        if (SelModule == 720) {
            GenerateFeltProductionFeltOrderUpdGrid();
        }

        if (SelModule == 701) {
            //      GenerateFeltRateMasterGrid();
        }

        if (SelModule == 702) {
//                  GenerateFeltRateUpdateGrid();
        }
        if (SelModule == 725) {
            GenerateFeltMachineSurveyAmendGrid();
        }
        if (SelModule == 724) {
            GenerateFeltMachineSurveyEntryGrid();
        }
        if (SelModule == 727) {
            GenerateLCOpenerMasterGrid();
        }
        if (SelModule == 728) {
            GenerateLCOpenerMasterAmendGrid();
        }
        if (SelModule == 171) {
            GenerateLCPartyUpdateGrid();
        }
        if (SelModule == 606) {
            GenerateFeltLCPartyMasterGrid();
        }
        if (SelModule == 730) {
            GenerateProductionFeltDiscRateMasterGrid();
        }
        if (SelModule == 732) {
            GenerateProductionFeltUnadjTRNGrid();
        }
        if (SelModule == 735) {
            GenerateProductionFeltCreditNoteGrid();
        }

        if (SelModule == 737) {
            GenerateProductionFeltYE1CreditNoteGrid();
        }

        if (SelModule == 738) {
            GenerateProductionFeltYE2CreditNoteGrid();
        }

        if (SelModule == 708) {
            GenerateFeltPerfomaInvoiceGrid();
        }
        if (SelModule == 739) {
            GenerateFeltGroupMasterGrid();
        }
        if (SelModule == 740) {
            GenerateFeltReopenBaleGrid();
        }
        if (SelModule == 743) {
            GenerateFeltGroupMasterAmendGrid();
        }

        if (SelModule == 604) {
            GenerateFeltOrderDiversionGrid();
        }

        if (SelModule == 602) {
            GenerateFeltOrderGrid();
        }

        if (SelModule == 745) {
            GenerateFeltPieceUpdationGrid();
        }

        if (SelModule == 742) {
            GenerateFeltDiversionListGrid();
        }

        if (SelModule == 750) {
            GenerateFeltMM_Piece_Amend_Approval_WIP_Grid();
        }

        if (SelModule == 763) {
            GenerateFeltMM_Piece_Amend_Approval_STOCK_Grid();
        }

        if (SelModule == 610) {
            GenerateFeltInvoiceCancelGrid();
        }

        if (SelModule == 603) {
            GenerateFeltSalesFinishingGrid();
        }

        if (SelModule == 770) {
            GenerateFeltSalesLocationGrid();
        }

        if (SelModule == 80) {
            GenerateFeltSalesInvoiceGrid();
        }

        if (SelModule == 754) {
            GenerateFeltSalesInvoiceParameterModificationGrid();
        }

        if (SelModule == 744) {
            GenerateFeltSalesReturnGrid();
        }

        if (SelModule == 759) {
            GenerateFeltPieceAmendGrid();
        }

        if (SelModule == 760) {
            GenerateFeltPieceDivisionGrid();
        }

        if (SelModule == 761) {
            GenerateFeltInvoiceParameterModificationCancellationGrid();
        }

        if (SelModule == 762) {
            GenerateFeltTransporterWeightGrid();
        }

        if (SelModule == 765) {
            GenerateFeltInvoiceF6ProcssParameterGrid();
        }
        if (SelModule == 766) {
            GenerateFeltBudgetUploadGrid();
        }
//        if (SelModule == 768) {
//            GenerateFeltBudgetManualGrid();
//        }

        if (SelModule == 768) {
            GenerateFeltBudgetEntryGrid();
        }

        if (SelModule == 769) {
            GenerateFeltSalesGroupEnhancementGrid();
        }

        if (SelModule == 774) {
            GenerateFeltSalesWIPAmend();
        }
        if (SelModule == 776) {
            GenerateFeltSalesDelinkApproval();
        }
        if (SelModule == clsWarpingBeamOrder.ModuleID) {
            GenerateFeltWarpingBeamOrderApproval();
        }
        if (SelModule == clsWarpingBeamOrderAmend.ModuleID) {
            GenerateFeltWarpingBeamOrderAmendApproval();
        }

        if (SelModule == 779) {
            GenerateFeltSalesPriorApproval();
        }
        if (SelModule == 790) {
            GenerateGIDCProductionProcess();
        }
        if (SelModule == 795) {
            GenerateGIDCProductionProcessAmend();
        }
        if (SelModule == 798) {
            GenerateOrderConfirmation();
        }
        if (SelModule == 807) {
            GeneratePieceSpiloverAssignUPN();
        }
        if (SelModule == 812) {
            GenerateEmpMaster();
        }
        if (SelModule == 814) {
            GenerateEmpMasterAmend();
        }
        if (SelModule == 820) {
            GenerateWVGProdLoomwiseEntry();
        }
        if (SelModule == 835) {
            GenerateWVGProdLoomwiseEntry_New();
        }
        if (SelModule == 661) {
            Generate661();
        }
    }

    private void GenerateBudgetReviewEntryGrid() {
        HashMap List = new HashMap();

        List = clsNewSalesProjectionEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGrid_BudgetReview();
            for (int i = 1; i <= List.size(); i++) {
                clsNewSalesProjectionEntry ObjItem = (clsNewSalesProjectionEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[5];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = ObjItem.getAttribute("PARTY_NAME").getString();
                    rowData[2] = ObjItem.getAttribute("PARTY_GROUP").getString();
                    rowData[3] = (ObjItem.getAttribute("received_date").getString());
                    rowData[4] = (ObjItem.getAttribute("action_date").getString());
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateGrid_BudgetReview() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Party Group");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Action Date");
        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void GenerateGIDCProductionProcess() {
        HashMap List = new HashMap();
        List = clsProductionEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGIDCProductionProcessGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsProductionEntry ObjItem = (clsProductionEntry) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGIDCProductionProcessAmend() {
        HashMap List = new HashMap();
        List = clsProductionAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGIDCProductionProcessGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsProductionAmend ObjItem = (clsProductionAmend) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateOrderConfirmation() {
        HashMap List = new HashMap();
        List = clsPieceOC.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            //GenerateGIDCProductionProcessGrid();
            GenerateOrderConfirmationGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsPieceOC ObjItem = (clsPieceOC) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GeneratePieceSpiloverAssignUPN() {
        HashMap List = new HashMap();
        List = clsFeltAssignUPN.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            //GenerateGIDCProductionProcessGrid();
            GenerateOrderConfirmationGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltAssignUPN ObjItem = (clsFeltAssignUPN) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateEmpMaster() {
        HashMap List = new HashMap();
        List = clsEmployeeMaster.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateEmpMasterGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsEmployeeMaster ObjItem = (clsEmployeeMaster) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateEmpMasterAmend() {
        HashMap List = new HashMap();
        List = clsEmployeeMasterAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateEmpMasterGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsEmployeeMasterAmend ObjItem = (clsEmployeeMasterAmend) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("AMEND_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateWVGProdLoomwiseEntry() {
        HashMap List = new HashMap();
        List = clsWVG_Prod_Loom_WVR.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsWVG_Prod_Loom_WVR ObjItem = (clsWVG_Prod_Loom_WVR) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateWVGProdLoomwiseEntry_New() {
        HashMap List = new HashMap();
        List = EITLERP.FeltSales.WVG_Prod_Loom_WVR_NEW.clsWVG_Prod_Loom_WVR.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.WVG_Prod_Loom_WVR_NEW.clsWVG_Prod_Loom_WVR ObjItem = (EITLERP.FeltSales.WVG_Prod_Loom_WVR_NEW.clsWVG_Prod_Loom_WVR) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void Generate661() {
        HashMap List = new HashMap();
        List = clsFeltObsoleteAssignUPN.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltObsoleteAssignUPN ObjItem = (clsFeltObsoleteAssignUPN) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGPRGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);

        List = clsGPR.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();

        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsGPR ObjItem = (clsGPR) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("GATEPASS_REQ_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GATEPASS_REQ_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateNRGPGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);

        List = clsNRGP.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsNRGP ObjItem = (clsNRGP) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("GATEPASS_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GATEPASS_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRGPGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsRGP.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsRGP ObjItem = (clsRGP) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("GATEPASS_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GATEPASS_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateIssueGenGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsIssue.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsIssue ObjItem = (clsIssue) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("ISSUE_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("ISSUE_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateDecFormGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsDeclarationForm.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {

            for (int i = 1; i <= List.size(); i++) {
                clsDeclarationForm ObjItem = (clsDeclarationForm) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("DECLARATION_ID").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DECLARATION_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateIssueReqGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsIssueRequisition.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {

            for (int i = 1; i <= List.size(); i++) {
                clsIssueRequisition ObjItem = (clsIssueRequisition) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("REQ_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("REQ_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateIssueReqRawGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsIssueRequisitionRaw.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {

            for (int i = 1; i <= List.size(); i++) {
                clsIssueRequisitionRaw ObjItem = (clsIssueRequisitionRaw) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("REQ_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("REQ_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateIssueRawGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsIssueRaw.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsIssueRaw ObjItem = (clsIssueRaw) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("ISSUE_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("ISSUE_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateIssueStoresGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsIssueStoresales.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsIssueStoresales ObjItem = (clsIssueStoresales) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("ISSUE_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("ISSUE_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateIssueRMGGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsIssueRMG.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsIssueRMG ObjItem = (clsIssueRMG) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("ISSUE_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("ISSUE_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSummaryGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsQuotApproval.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsQuotApproval ObjItem = (clsQuotApproval) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("APPROVAL_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("APPROVAL_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateMRGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsMaterialRequisition.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsMaterialRequisition ObjItem = (clsMaterialRequisition) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("REQ_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("REQ_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateVoucherGrid(int VoucherModuleID) {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsVoucher.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, VoucherModuleID, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsVoucher ObjItem = (clsVoucher) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("VOUCHER_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("VOUCHER_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = (String) ObjItem.getAttribute("GRN_NO").getObj();

                rowData[4] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[5] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateMemVoucherGrid(int VoucherModuleID) {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsMemorandumJV.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, VoucherModuleID, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsMemorandumJV ObjItem = (clsMemorandumJV) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = (String) ObjItem.getAttribute("VOUCHER_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("VOUCHER_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                rowData[3] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[4] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateMiscExpenseGrid() {
        HashMap List = new HashMap();

        List = clsExpense.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsExpense ObjItem = (clsExpense) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("EXPENSE_ID").getObj();
                rowData[1] = "";
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = "";

                rowData[4] = "";
                rowData[5] = "";

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateBillMatchGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsCrAdjustment.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsCrAdjustment ObjItem = (clsCrAdjustment) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = "";

                DataModel.addRow(rowData);
            }
        }
    }

    private void GeneratePartyGrid() {
        HashMap List = new HashMap();

        List = clsPartyMaster.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPartyMaster ObjItem = (clsPartyMaster) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = Integer.toString(ObjItem.getAttribute("PARTY_ID").getInt());
                rowData[1] = ObjItem.getAttribute("PARTY_CODE").getString();
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = "";

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGLGrid() {
        HashMap List = new HashMap();

        List = clsGL.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsGL ObjItem = (clsGL) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = Integer.toString(ObjItem.getAttribute("ACCOUNT_ID").getInt());
                rowData[1] = "";
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = "";

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFreightComparisonGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsFreightComparison.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsFreightComparison ObjItem = (clsFreightComparison) List.get(Integer.toString(i));
                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateFreightCalculationGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsFreightCalculation.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsFreightCalculation ObjItem = (clsFreightCalculation) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateDocCancelGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsDocCancel.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsDocCancel ObjItem = (clsDocCancel) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = (String) ObjItem.getAttribute("REQ_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("REQ_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());
                    rowData[4] = Integer.toString(ObjItem.getAttribute("MODULE_ID").getInt());
                    rowData[5] = clsModules.getModuleName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("MODULE_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateIndentGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsIndent.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            FormatGridIndent();
            for (int i = 1; i <= List.size(); i++) {
                clsIndent ObjItem = (clsIndent) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("INDENT_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateInquiryGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsInquiry.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsInquiry ObjItem = (clsInquiry) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("INQUIRY_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("INQUIRY_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateCIFGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsCIF.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsCIF ObjItem = (clsCIF) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = (String) ObjItem.getAttribute("CIF_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("CIF_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateRGPReturnGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsRGPReturn.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsRGPReturn ObjItem = (clsRGPReturn) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("RETURN_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RETURN_NO").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRateApprovalGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsRateApproval.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsRateApproval ObjItem = (clsRateApproval) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("APPROVAL_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("APPROVAL_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateGRNGrid(int pType) {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsGRN.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, pType, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsGRN ObjItem = (clsGRN) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("GRN_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GRN_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }

    }

    private void GenerateRJNGrid(int pType) {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsRJN.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, pType, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsRJN ObjItem = (clsRJN) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("RJN_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RJN_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSTMGrid(int pType) {

        HashMap List = new HashMap();

        List = clsSTMGen.getPendingApprovals(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, pType, SorOn);

        //List=clsMIRRaw.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,pType,SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSTMGen ObjItem = (clsSTMGen) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[3];

                    rowData[0] = (String) ObjItem.getAttribute("STM_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("STM_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    //rowData[3]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID,(int)ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }

    }

    private void GenerateSTMRawGrid(int pType) {

        HashMap List = new HashMap();

        List = clsSTMGen.getPendingApprovals(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, pType, SorOn);

        //List=clsMIRRaw.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,pType,SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSTMGen ObjItem = (clsSTMGen) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[3];

                    rowData[0] = (String) ObjItem.getAttribute("STM_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("STM_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    //rowData[3]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID,(int)ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateFeltOrderDiversionGrid() {
        HashMap List = new HashMap();

        List = clsFeltOrderDiversion.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltOrderDiversion ObjDoc = (clsFeltOrderDiversion) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltOrderGrid() {
        HashMap List = new HashMap();

        List = clsFeltOrder.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltOrder ObjDoc = (clsFeltOrder) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    //GenerateFeltDiversionListGrid
    private void GenerateFeltDiversionListGrid() {
        HashMap List = new HashMap();

        List = clsDiversionList.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsDiversionList ObjDoc = (clsDiversionList) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltMM_Piece_Amend_Approval_WIP_Grid() {
        HashMap List = new HashMap();

        List = clsPieceAmendApproval.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsPieceAmendApproval ObjDoc = (clsPieceAmendApproval) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltMM_Piece_Amend_Approval_STOCK_Grid() {
        HashMap List = new HashMap();

        List = clsPieceAmendApproval_STOCK.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid_NEW();
            for (int i = 1; i <= List.size(); i++) {
                clsPieceAmendApproval_STOCK ObjDoc = (clsPieceAmendApproval_STOCK) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());
                rowData[5] = ObjDoc.getAttribute("PIECE_NO").getString();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesInvoiceGrid() {
        HashMap List = new HashMap();

        List = clsFeltSalesInvoice.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateInvoiceGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltSalesInvoice ObjDoc = (clsFeltSalesInvoice) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("BALE_NO").getString();
                rowData[4] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[5] = ObjDoc.getAttribute("PARTY_NAME").getString();
                rowData[6] = ObjDoc.getAttribute("PIECE_NO").getString();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesInvoiceParameterModificationGrid() {
        HashMap List = new HashMap();

        List = clsFeltGSTAdvancePaymentEntryForm.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltGSTAdvancePaymentEntryForm ObjDoc = (clsFeltGSTAdvancePaymentEntryForm) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesGroupEnhancementGrid() {
        HashMap List = new HashMap();

        List = clsFeltGroupCriticalLimitEnhancement.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {

            GenerateFeltProductionForCriticalLimit();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltGroupCriticalLimitEnhancement ObjDoc = (clsFeltGroupCriticalLimitEnhancement) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("GROUP_CODE").getString();
                rowData[4] = ObjDoc.getAttribute("GROUP_NAME").getString();
                rowData[5] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[6] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesWIPAmend() {
        HashMap List = new HashMap();

        List = clsPieceAmendWIP.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsPieceAmendWIP ObjDoc = (clsPieceAmendWIP) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesPriorApproval() {
        HashMap List = new HashMap();

        List = clsFeltOrderDiversionLoss.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltOrderDiversionLoss ObjDoc = (clsFeltOrderDiversionLoss) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesDelinkApproval() {
        HashMap List = new HashMap();

        List = clsPieceAmendDELINK.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsPieceAmendDELINK ObjDoc = (clsPieceAmendDELINK) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltWarpingBeamOrderApproval() {
        HashMap List = new HashMap();

        List = clsWarpingBeamOrder.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltWarpingBeamOrderGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsWarpingBeamOrder ObjDoc = (clsWarpingBeamOrder) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("BEAM_NO").getInt();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltWarpingBeamOrderAmendApproval() {
        HashMap List = new HashMap();

        List = clsWarpingBeamOrderAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltWarpingBeamOrderGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsWarpingBeamOrderAmend ObjDoc = (clsWarpingBeamOrderAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("BEAM_NO").getInt();
                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltBudgetUploadGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsBudgetUpload.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltBudgetGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsBudgetUpload ObjItem = (clsBudgetUpload) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltBudgetManualGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsBudgetManual.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltBudgetGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsBudgetManual ObjItem = (clsBudgetManual) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltBudgetEntryGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        //List = clsBudgetManual.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        List = EITLERP.FeltSales.Budget.clsBudgetEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            //GenerateFeltBudgetGrid();
            FormatFeltBudgetEntryGrid();
            for (int i = 1; i <= List.size(); i++) {
                //clsBudgetManual ObjItem = (clsBudgetManual) List.get(Integer.toString(i));
                EITLERP.FeltSales.Budget.clsBudgetEntry ObjItem = (EITLERP.FeltSales.Budget.clsBudgetEntry) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = (String) ObjItem.getAttribute("DOC_DATE").getObj();
                rowData[2] = (String) ObjItem.getAttribute("RECEIVED_DATE").getObj();
//                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
//                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesReturnGrid() {
        HashMap List = new HashMap();

        List = clsFeltSalesReturns.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltSalesReturns ObjDoc = (clsFeltSalesReturns) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPieceAmendGrid() {
        HashMap List = new HashMap();

        List = clsFeltPieceAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPieceAmend ObjDoc = (clsFeltPieceAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPieceDivisionGrid() {
        HashMap List = new HashMap();

        List = clsPieceDivision.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsPieceDivision ObjDoc = (clsPieceDivision) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltTransporterWeightGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsFeltTransporterWeigthEntryForm.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltTransporterWeigthEntryForm ObjDoc = (clsFeltTransporterWeigthEntryForm) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltInvoiceParameterModificationCancellationGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsParameterCancel.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsParameterCancel ObjDoc = (clsParameterCancel) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("REQ_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("REQ_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltInvoiceF6ProcssParameterGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsFeltProcessInvoiceVariable.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltInvoiceF6ProcessGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltProcessInvoiceVariable ObjDoc = (clsFeltProcessInvoiceVariable) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[3] = ObjDoc.getAttribute("PARTY_NAME").getString();
                rowData[4] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltInvoiceCancelGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = EITLERP.FeltSales.FeltInvReport.clsDocCancel.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.FeltInvReport.clsDocCancel ObjItem = (EITLERP.FeltSales.FeltInvReport.clsDocCancel) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = (String) ObjItem.getAttribute("REQ_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("REQ_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());
                    rowData[4] = Integer.toString(ObjItem.getAttribute("MODULE_ID").getInt());
                    rowData[5] = clsModules.getModuleName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("MODULE_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateFeltPieceUpdationGrid() {
        HashMap List = new HashMap();

        List = clsPieceUpdation.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsPieceUpdation ObjDoc = (clsPieceUpdation) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateMIRGrid(int pType) {
        HashMap List = new HashMap();

        List = clsMIRRaw.getPendingApprovals(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, pType, SorOn, EITLERPGLOBAL.FinYearFrom);
        //List=clsMIRRaw.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,pType,SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsMIRRaw ObjItem = (clsMIRRaw) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("MIR_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("MIR_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GeneratePOGrid(int pType) {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsPOGen.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, pType, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPOGen ObjItem = (clsPOGen) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("PO_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("PO_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateAmendGrid(int pType) {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);

        List = clsPOAmendGen.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, pType, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPOAmendGen ObjItem = (clsPOAmendGen) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("AMEND_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("AMEND_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateItemGrid() {
        HashMap List = new HashMap();
        List = clsItem.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsItem ObjItem = (clsItem) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                rowData[1] = (String) ObjItem.getAttribute("ITEM_DESCRIPTION").getObj();
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSalesInvoiceDueDateGrid() {
        HashMap List = new HashMap();
        List = clsItem.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsItem ObjItem = (clsItem) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = (String) ObjItem.getAttribute("DOC NO").getObj();
                rowData[1] = (String) ObjItem.getAttribute("PARTY NAME").getObj();
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[3] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateDebitMemoReceiptMappingGrid() {
        HashMap List = new HashMap();
        List = EITLERP.Sales.DebitMemoReceiptMapping.clsDebitMemoReceiptMapping.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.getCurrentFinYear());
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.Sales.DebitMemoReceiptMapping.clsDebitMemoReceiptMapping ObjItem = (EITLERP.Sales.DebitMemoReceiptMapping.clsDebitMemoReceiptMapping) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DEBITMEMO_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DEBITMEMO_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = "";

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSuppGrid() {
        HashMap List = new HashMap();
        List = clsSupplier.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSupplier ObjItem = (clsSupplier) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = Integer.toString((int) ObjItem.getAttribute("SUPP_ID").getVal());
                    rowData[1] = (String) ObjItem.getAttribute("SUPP_NAME").getObj();
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void FormatForItemMaster() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Item Code");
        DataModel.addColumn("Item Name");
        DataModel.addColumn("Received Date");

        //Now hide the column 1
        TableColumnModel ColModel = Table.getColumnModel();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        /*ColModel.getColumn(0).setMinWidth(0);
         ColModel.getColumn(0).setPreferredWidth(0);*/
    }

    private void GenerateAdvRecptAdjustmentFormGrid(int VoucherModuleID) {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsDrAdjustment.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsDrAdjustment ObjItem = (clsDrAdjustment) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = (String) ObjItem.getAttribute("DEPTID").getObj();

                //rowData[4]=(String)ObjItem.getAttribute("PARTY_CODE").getObj();
                //rowData[5]=(String)ObjItem.getAttribute("PARTY_NAME").getObj();
                DataModel.addRow(rowData);
            }
        }
    }

    //vivek
    private void GenerateFeltPackingGrid() {
        HashMap List = new HashMap();

        List = clsFeltPacking.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPacking ObjDoc = (clsFeltPacking) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltWarpingGrid() {
        HashMap List = new HashMap();

        List = clsFeltWarping.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltWarping ObjDoc = (clsFeltWarping) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltRejectionGrid() {
        HashMap List = new HashMap();

        List = clsFeltRejection.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltRejection ObjDoc = (clsFeltRejection) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltFinishingGrid() {
        HashMap List = new HashMap();

        List = clsFeltFinishing.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltFinishing ObjDoc = (clsFeltFinishing) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesFinishingGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.FeltFinishing.clsFeltFinishing.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.FeltFinishing.clsFeltFinishing ObjDoc = (EITLERP.FeltSales.FeltFinishing.clsFeltFinishing) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltSalesLocationGrid() {
        HashMap List = new HashMap();

        List = clsFeltLocationAssignment.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltLocationAssignment ObjDoc = (clsFeltLocationAssignment) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltProductionNeedlingGrid() {
        HashMap List = new HashMap();

        List = clsFeltNeedling.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltNeedling ObjDoc = (clsFeltNeedling) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltProductionMendingGrid() {
        HashMap List = new HashMap();

        List = clsFeltMending.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltMending ObjDoc = (clsFeltMending) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltProductionWeavingGrid() {
        HashMap List = new HashMap();

        List = clsFeltWeaving.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltWeaving ObjDoc = (clsFeltWeaving) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltProductionHeatSettingGrid() {
        HashMap List = new HashMap();

        List = clsFeltHeatSetting.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltHeatSetting ObjDoc = (clsFeltHeatSetting) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }
    
    private void GenerateFeltProductionMarkingGrid() {
        HashMap List = new HashMap();

        List = clsFeltMarking.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltMarking ObjDoc = (clsFeltMarking) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltProductionSplicingGrid() {
        HashMap List = new HashMap();

        List = clsFeltSplicing.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltSplicing ObjDoc = (clsFeltSplicing) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }
    
    private void GenerateFeltProductionFeltOrderUpdGrid() {
        HashMap List = new HashMap();

        List = clsFeltOrderUpd.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltOrderUpdGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltOrderUpd ObjDoc = (clsFeltOrderUpd) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARA_DESC").getString();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateProductionFeltCreditNoteGrid() {
        HashMap List = new HashMap();

        List = clsFeltCreditNote.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltCreditNoteGrid1();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltCreditNote ObjDoc = (clsFeltCreditNote) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARA_DESC").getString();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateProductionFeltYE1CreditNoteGrid() {
        HashMap List = new HashMap();

        List = clsFeltYearEndDisc.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltBudgetGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltYearEndDisc ObjDoc = (clsFeltYearEndDisc) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateProductionFeltYE2CreditNoteGrid() {
        HashMap List = new HashMap();

        List = clsFeltYearEndDisc2.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltBudgetGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltYearEndDisc2 ObjDoc = (clsFeltYearEndDisc2) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

//    
//private void GenerateFeltOrderDiversionGrid() {
//        HashMap List=new HashMap();
//        
//        List=clsFeltOrderDiversion.getPendingApprovals((int)EITLERPGLOBAL.gUserID,SorOn);
//        SizeCount=List.size();
//        if(!DoNotPopulate){
//            GenerateFeltProductionGrid();
//            for(int i=1;i<=List.size();i++) {
//                clsFeltOrderDiversion ObjDoc=(clsFeltOrderDiversion) List.get(Integer.toString(i));
//                Object[] rowData=new Object[3];
//                
//                rowData[0]=ObjDoc.getAttribute("DOC_NO").getString();
//                rowData[1]=EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
//                rowData[2]=EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
//              
//                DataModel.addRow(rowData);
//            }
//        }
//    }
//    private void GenerateFeltOrderGrid() {
//        HashMap List=new HashMap();
//        
//        List=clsFeltOrder.getPendingApprovals((int)EITLERPGLOBAL.gUserID,SorOn);
//        SizeCount=List.size();
//        if(!DoNotPopulate){
//            GenerateFeltProductionGrid();
//            for(int i=1;i<=List.size();i++) {
//                clsFeltOrder ObjDoc=(clsFeltOrder) List.get(Integer.toString(i));
//                Object[] rowData=new Object[3];
//                
//                rowData[0]=ObjDoc.getAttribute("DOC_NO").getString();
//                rowData[1]=EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
//                rowData[2]=EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
//              
//                DataModel.addRow(rowData);
//            }
//        }
//    }
    private void GenerateFeltPerfomaInvoiceGrid() {
        HashMap List = new HashMap();

        List = clsProforma.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltPerfomaInvoiceGrid1();
            for (int i = 1; i <= List.size(); i++) {
                clsProforma ObjDoc = (clsProforma) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltGroupMasterGrid() {
        HashMap List = new HashMap();

        List = clsFeltGroupMaster.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltGroupMasterGrid1();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltGroupMaster ObjDoc = (clsFeltGroupMaster) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[2] = ObjDoc.getAttribute("GROUP_CODE").getString();
                rowData[3] = ObjDoc.getAttribute("GROUP_DESC").getString();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltGroupMasterAmendGrid() {
        HashMap List = new HashMap();

        List = clsFeltGroupMasterAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltGroupMasterAmendGrid1();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltGroupMasterAmend ObjDoc = (clsFeltGroupMasterAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[2] = ObjDoc.getAttribute("GROUP_CODE").getString();
                rowData[3] = ObjDoc.getAttribute("GROUP_DESC").getString();
                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltReopenBaleGrid() {
        HashMap List = new HashMap();
        List = clsFeltReopenBale.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltReopenBaleGrid1();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltReopenBale ObjDoc = (clsFeltReopenBale) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateLCOpenerMasterAmendGrid() {
        HashMap List = new HashMap();

        List = clsLcOpenerMasterAmend1.getPendingApprovals(2, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        // if(!DoNotPopulate) {
        GenerateLcOenerMasterAMendGrid();
        for (int i = 1; i <= List.size(); i++) {
            clsLcOpenerMasterAmend1 ObjDoc = (clsLcOpenerMasterAmend1) List.get(Integer.toString(i));
            Object[] rowData = new Object[4];

            rowData[0] = ObjDoc.getAttribute("LCO_AMD_NO").getString();
            rowData[1] = ObjDoc.getAttribute("LCO_OPENER_CODE").getString();
            //rowData[1]=EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
            rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
            // rowData[3]=ObjDoc.getAttribute("PARA_DESC").getString();

            DataModel.addRow(rowData);
        }

    }

    private void GenerateFeltProductionWvgLoomEffGrid() {
        HashMap List = new HashMap();
        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsFeltWeavingLoom.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        //  List=clsFeltWeavingLoom.getPendingApprovals(2,(int)EITLERPGLOBAL.gUserID,SorOn); 
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltWeavingLoom ObjDoc = (clsFeltWeavingLoom) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }

        }
    }

    private void GeneratLCOpenerEntryGrid() {
        HashMap List = new HashMap();

        List = clsLcOpenerMaster.getPendingApprovals(2, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        //if(!DoNotPopulate) {
        GenerateLCOpenerMasterGrid();
        for (int i = 1; i <= List.size(); i++) {
            clsLcOpenerMaster ObjDoc = (clsLcOpenerMaster) List.get(Integer.toString(i));
            Object[] rowData = new Object[3];

            rowData[0] = ObjDoc.getAttribute("LCO_OPENER_CODE").getString();
            //rowData[1]=EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("LCO_EXPIRTY_DATE").getString());
            rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

            DataModel.addRow(rowData);
        }
    }

    private void GenerateFeltProductionGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void GenerateFeltClubbingGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Piece No");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
        tcm.getColumn(5).setPreferredWidth(120);
    }

    private void GenerateFeltReschedulingNewGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Piece No");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
    }

    private void GenerateMissedPunchGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Emp No");
        DataModel.addColumn("Emp Name");
        DataModel.addColumn("Department");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setPreferredWidth(100);
        tcm.getColumn(3).setPreferredWidth(100);
        tcm.getColumn(4).setPreferredWidth(130);
        tcm.getColumn(5).setPreferredWidth(120);
    }

    private void GenerateGrid_PerformanceTracking() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Machine No");
        DataModel.addColumn("Position");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setPreferredWidth(100);
        tcm.getColumn(3).setPreferredWidth(100);
        tcm.getColumn(4).setPreferredWidth(130);
        tcm.getColumn(5).setPreferredWidth(120);
    }

    private void GenerateFeltProductionWithPartyGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
    }

    private void GenerateFeltProductionWithPartyGrid_NEW() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Piece No");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
        tcm.getColumn(5).setPreferredWidth(150);

    }

    private void GenerateFeltProductionForCriticalLimit() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Group Code");
        DataModel.addColumn("Group Name");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
        tcm.getColumn(5).setPreferredWidth(120);
        tcm.getColumn(6).setPreferredWidth(120);
    }

    private void GenerateFeltWarpingBeamOrderGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Beam No.");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);

    }

    private void GenerateFeltBudgetGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);

    }

    private void FormatFeltBudgetEntryGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Praty Group Name");
        DataModel.addColumn("Party Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(150);
        tcm.getColumn(2).setPreferredWidth(120);

    }

    private void FormatEmpGPGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Doc No.");
        DataModel.addColumn("Doc Date");
        DataModel.addColumn("Emp Name");
        DataModel.addColumn("GP Type");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setPreferredWidth(150);
        tcm.getColumn(3).setPreferredWidth(100);
    }

    private void FormatTimeCorrectionGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Doc No.");
        DataModel.addColumn("Doc Date");
        DataModel.addColumn("Correction Type");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(80);
        tcm.getColumn(1).setPreferredWidth(80);
        tcm.getColumn(2).setPreferredWidth(150);

    }

    private void FormatEmpLeaveGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Doc No.");
        DataModel.addColumn("Doc Date");
        DataModel.addColumn("Emp Name");
        DataModel.addColumn("Leave Detail");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(80);
        tcm.getColumn(1).setPreferredWidth(80);
        tcm.getColumn(2).setPreferredWidth(150);
        tcm.getColumn(3).setPreferredWidth(200);

    }

    private void GenerateGIDCProductionProcessGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);

    }

    private void GenerateOrderConfirmationGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void GenerateEmpMasterGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void GenerateFeltInvoiceF6ProcessGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
    }

    private void GenerateInvoiceGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Invoice No.");
        DataModel.addColumn("Invoice Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Bale No");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Piece No");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(80);
        tcm.getColumn(1).setPreferredWidth(80);
        tcm.getColumn(2).setPreferredWidth(80);
        tcm.getColumn(3).setPreferredWidth(80);
        tcm.getColumn(4).setPreferredWidth(80);
        tcm.getColumn(5).setPreferredWidth(80);
        tcm.getColumn(6).setPreferredWidth(80);
    }

    private void GenerateFeltPerfomaInvoiceGrid1() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
    }

    private void GenerateFeltGroupMasterGrid1() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Group Code");
        DataModel.addColumn("Group Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);

    }

    private void GenerateFeltGroupMasterAmendGrid1() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Group Code");
        DataModel.addColumn("Group Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
    }

    private void GenerateFeltReopenBaleGrid1() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
    }

    private void GenerateFeltOrderUpdGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("AmendMent Reason");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
    }

    private void GenerateLcOenerMasterAMendGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("LC Amend No.");
        DataModel.addColumn("LC Opener Code");
        DataModel.addColumn("Received Date");
        //  DataModel.addColumn("AmendMent Reason");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        // tcm.getColumn(3).setPreferredWidth(120);
    }

    private void GenerateFeltCreditNoteGrid1() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Credit Note No.");
        DataModel.addColumn("Credit Note Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Credit Note Type");
        //  DataModel.addColumn("AmendMent Reason");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
    }

    /* 
     private void GenerateFeltRateMasterGrid() {
     HashMap List=new HashMap();
        
     List=clsFeltRateMaster.getPendingApprovals((int)EITLERPGLOBAL.gUserID,SorOn);
     SizeCount=List.size();
     if(!DoNotPopulate) {
     GenerateFeltRateGrid();
     for(int i=1;i<=List.size();i++) {
     clsFeltRateMaster ObjDoc=(clsFeltRateMaster) List.get(Integer.toString(i));
     Object[] rowData=new Object[3];
                
     rowData[0]=(String)ObjDoc.getAttribute("DOC_NO").getObj();
     rowData[1]=(String)ObjDoc.getAttribute("PRODUCT_CODE").getObj();
     rowData[2]=EITLERPGLOBAL.formatDate((String)ObjDoc.getAttribute("RECEIVED_DATE").getObj());
                
     DataModel.addRow(rowData);
     }
     }
     }
     */
    /*
     private void GenerateFeltRateUpdateGrid() {
     HashMap List=new HashMap();
        
     List=clsFeltRateUpdate.getPendingApprovals((int)EITLERPGLOBAL.gUserID,SorOn);
     SizeCount=List.size();
     if(!DoNotPopulate) {
     GenerateFeltRateGrid();
     for(int i=1;i<=List.size();i++) {
     clsFeltRateUpdate ObjDoc=(clsFeltRateUpdate) List.get(Integer.toString(i));
     Object[] rowData=new Object[3];
                
     rowData[0]=(String)ObjDoc.getAttribute("DOC_NO").getObj();
     rowData[1]=(String)ObjDoc.getAttribute("PRODUCT_CODE").getObj();
     rowData[2]=EITLERPGLOBAL.formatDate((String)ObjDoc.getAttribute("RECEIVED_DATE").getObj());
                
     DataModel.addRow(rowData);
     }
     }
     }
     */
    private void GenerateFeltMachineSurveyAmendGrid() {
        HashMap List = new HashMap();

        List = clsmachinesurveyAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltMachineSurveyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsmachinesurveyAmend ObjDoc = (clsmachinesurveyAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = (String) ObjDoc.getAttribute("MM_DOC_NO").getObj();
                rowData[1] = (String) ObjDoc.getAttribute("MM_PARTY_CODE").getObj();
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjDoc.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltMachineSurveyEntryGrid() {
        HashMap List = new HashMap();

        List = clsmachinesurvey.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltMachineSurveyViewGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsmachinesurvey ObjDoc = (clsmachinesurvey) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                //rowData[0]=(String)ObjDoc.getAttribute("DOC_NO").getObj();
                rowData[0] = (String) ObjDoc.getAttribute("MM_DOC_NO").getObj();
                rowData[1] = (String) ObjDoc.getAttribute("MM_PARTY_CODE").getObj();
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjDoc.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateLCOpenerMasterGrid() {
        HashMap List = new HashMap();

        List = clsLcOpenerMaster.getPendingApprovals(2, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateLCOpenerMasteViewrGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsLcOpenerMaster ObjDoc = (clsLcOpenerMaster) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = (String) ObjDoc.getAttribute("LCO_OPENER_CODE").getObj();
                // rowData[1]=(String)ObjDoc.getAttribute("LCO_OPENER_CODE").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjDoc.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateLCopenerMasterAmendGrid() {
        HashMap List = new HashMap();

        List = clsLcOpenerMasterAmend1.getPendingApprovals(2, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateLCOpenerMasteAmendViewrGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsLcOpenerMasterAmend1 ObjDoc = (clsLcOpenerMasterAmend1) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = (String) ObjDoc.getAttribute("LCO_AMD_NO").getObj();
                // rowData[1]=(String)ObjDoc.getAttribute("LCO_OPENER_CODE").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjDoc.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateProductionFeltDiscRateMasterGrid() {
        HashMap List = new HashMap();

        List = clsDiscRateMaster.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltDiscMasterGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsDiscRateMaster ObjDoc = (clsDiscRateMaster) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = (String) ObjDoc.getAttribute("MASTER_NO").getObj();
                rowData[1] = (String) ObjDoc.getAttribute("PARTY_CODE").getObj();
                rowData[2] = (String) ObjDoc.getAttribute("PARTY_NAME").getObj();
                rowData[3] = EITLERPGLOBAL.formatDate((String) ObjDoc.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateProductionFeltUnadjTRNGrid() {
        HashMap List = new HashMap();

        List = clsFeltUnadj.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltUnadjGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltUnadj ObjDoc = (clsFeltUnadj) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = (String) ObjDoc.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjDoc.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltDiscMasterGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(80);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);

    }

    private void GenerateFeltUnadjGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);

    }

    private void GenerateLCOpenerMasteAmendViewrGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("LC Amend No.");
        // DataModel.addColumn("Party Code");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        //  tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
    }

    private void GenerateFeltMachineSurveyGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void GenerateLCOpenerMasteViewrGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        // DataModel.addColumn("Document No.");
        DataModel.addColumn("LC Opener Code");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        //tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
    }

    private void GenerateFeltMachineSurveyViewGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void FormatGridNormal() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Department");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");

        //Now hide the column 1
        TableColumnModel ColModel = Table.getColumnModel();
        //Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        Table.getColumnModel().getColumn(0).setMinWidth(100);
    }

    private void FormatGridIndent() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Department");
//        DataModel.addColumn("Party Code");
//        DataModel.addColumn("Party Name");

        //Now hide the column 1
        TableColumnModel ColModel = Table.getColumnModel();
        //Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        Table.getColumnModel().getColumn(0).setMinWidth(100);
    }

    private void GenerateCombo() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate Department Combo ------- //
        cmbDeptModel = new EITLComboModel();
        cmbDept.removeAllItems();
        cmbDept.setModel(cmbDeptModel);

        List = clsDepartment.getList(" ");
        for (int i = 1; i <= List.size(); i++) {
            clsDepartment ObjDept = (clsDepartment) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjDept.getAttribute("DEPT_ID").getVal();
            aData.Text = (String) ObjDept.getAttribute("DEPT_DESC").getObj();
            cmbDeptModel.addElement(aData);
        }
        //System.out.println("Dept End");
        //------------------------------ //

        //----- Generate cmbType ------- //
        cmbModuleModel = new EITLComboModel();
        cmbModule.removeAllItems();
        cmbModule.setModel(cmbModuleModel);

        try {
            int mYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
            String mfrmdt = String.valueOf(mYearFrom) + "-04-01";
            //String mtodt = String.valueOf((mYearFrom + 1)) + "-03-31";
            String mtodt = String.valueOf((mYearFrom + 2)) + "-03-31";
            //String cndtn="";
            if (OpgAuthDelUser.isSelected()) {
                strCondition = " AND MODULE_ID IN (SELECT MODULE_ID FROM DINESHMILLS.D_COM_AUTHORITY WHERE "
                        + "USER_ID=" + defUserID + " AND AUTHORITY_USER_ID=" + EITLERPGLOBAL.gAuthorityUserID + " AND CURDATE() BETWEEN FROM_DATE AND TO_DATE"
                        + " UNION ALL  SELECT MODULE_ID FROM DINESHMILLS.D_COM_AUTHORITY_AUTO WHERE "
                        + " USER_ID=" + defUserID + " AND AUTHORITY_USER_ID=" + EITLERPGLOBAL.gAuthorityUserID + " AND CURDATE() BETWEEN FROM_DATE AND TO_DATE) ";
            }
            ResultSet r;
            String strSQL = "SELECT MODULE_ID,CONCAT(MODULE_DESC,'(',PD,')') AS DOC FROM "
                    + "( "
                    + "SELECT MODULE_ID,COUNT(DOC_NO) AS PD FROM DINESHMILLS.D_COM_DOC_DATA "
                    + "WHERE STATUS IN ('W') "
                    + "AND USER_ID=" + EITLERPGLOBAL.gUserID + " "
                    + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END >='" + mfrmdt + "' "
                    + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END <='" + mtodt + "'  " + strCondition
                    + "GROUP BY MODULE_ID "
                    + "UNION ALL "
                    + "SELECT MODULE_ID,COUNT(DOC_NO) AS PD FROM PRODUCTION.FELT_PROD_DOC_DATA "
                    + "WHERE STATUS IN ('W') "
                    + "AND USER_ID=" + EITLERPGLOBAL.gUserID + " "
                    + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END >='" + mfrmdt + "' "
                    + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END <='" + mtodt + "' " + strCondition
                    + "GROUP BY MODULE_ID "
                    + "UNION ALL "
                    + "SELECT MODULE_ID,COUNT(DOC_NO) AS PD FROM SDMLATTPAY.D_COM_DOC_DATA "
                    + "WHERE STATUS IN ('W') "
                    + "AND USER_ID=" + EITLERPGLOBAL.gUserID + " "
                    + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END >='" + mfrmdt + "' "
                    + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END <='" + mtodt + "' " + strCondition
                    + "GROUP BY MODULE_ID "
                    + ") AS DDD "
                    + "LEFT JOIN (SELECT MODULE_ID AS M_ID,MODULE_DESC FROM DINESHMILLS.D_COM_MODULES) AS M "
                    + "ON DDD.MODULE_ID=M.M_ID";
            System.out.println(strSQL);
            r = data.getResult(strSQL);
            /*r = data.getResult("SELECT MODULE_ID,CONCAT(MODULE_DESC,'(',PD,')') AS DOC FROM "
             + "( "
             + "SELECT MODULE_ID,COUNT(DOC_NO) AS PD FROM DINESHMILLS.D_COM_DOC_DATA "
             + "WHERE STATUS IN ('W') "
             + "AND USER_ID=" + EITLERPGLOBAL.gUserID + " "
             + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END >='" + mfrmdt + "' "
             + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END <='" + mtodt + "' "
             + "GROUP BY MODULE_ID "
             + "UNION ALL "
             + "SELECT MODULE_ID,COUNT(DOC_NO) AS PD FROM PRODUCTION.FELT_PROD_DOC_DATA "
             + "WHERE STATUS IN ('W') "
             + "AND USER_ID=" + EITLERPGLOBAL.gUserID + " "
             + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END >='" + mfrmdt + "' "
             + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END <='" + mtodt + "' "
             + "GROUP BY MODULE_ID "
             + "UNION ALL "
             + "SELECT MODULE_ID,COUNT(DOC_NO) AS PD FROM SDMLATTPAY.D_COM_DOC_DATA "
             + "WHERE STATUS IN ('W') "
             + "AND USER_ID=" + EITLERPGLOBAL.gUserID + " "
             + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END >='" + mfrmdt + "' "
             + " AND CASE WHEN DOC_DATE='0000-00-00' THEN CHANGED_DATE ELSE DOC_DATE END <='" + mtodt + "' "
             + "GROUP BY MODULE_ID "
             + ") AS DDD "
             + "LEFT JOIN (SELECT MODULE_ID AS M_ID,MODULE_DESC FROM DINESHMILLS.D_COM_MODULES) AS M "
             + "ON DDD.MODULE_ID=M.M_ID"
             );*/
            r.first();
            if (r.getRow() > 0) {
                while (!r.isAfterLast()) {
                    if (chkUser.isSelected() && r.getInt("MODULE_ID") == 849) {

                    } else {
                        ComboData aData = new ComboData();
                        aData.Text = r.getString("DOC");
                        aData.Code = r.getInt("MODULE_ID");
                        cmbModuleModel.addElement(aData);
                    }
                    r.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        strCondition = " WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " ORDER BY MODULE_ID";
//
//        List = clsModules.getList(strCondition);
//        System.out.println("Module Start");
//        for (int i = 1; i <= List.size(); i++) {
//            clsModules ObjModules = (clsModules) List.get(Integer.toString(i));
//            //Check that Module Access Rights are given
//            int ModuleID = (int) ObjModules.getAttribute("MODULE_ID").getVal();
//            //int MenuID = clsMenu.getMenuIDFromModule(EITLERPGLOBAL.gCompanyID, ModuleID);
//
////            if (ModuleID == 190) {
////                boolean halt = true;
////            }
//            //Get the Count of Pending Documents
////            System.out.println("Count Start");
////            int theSize = GetCount(ModuleID);
////            System.out.println("Count End");
////            String ExtraDesc = "";
////
////            if (theSize > 0) {
////                ExtraDesc = " (" + Integer.toString(theSize) + ")";
////
////                ComboData aData = new ComboData();
////                aData.Text = ((String) ObjModules.getAttribute("MODULE_DESC").getObj()) + ExtraDesc;
////                aData.Code = (int) ObjModules.getAttribute("MODULE_ID").getVal();
////                cmbModuleModel.addElement(aData);
////
////            }
//        }
        //System.out.println("Module End");
    }

    public int GetCount(int pModuleID) {
        SizeCount = 0;

        DoNotPopulate = true;
	
	if (pModuleID == 878) {
            GeneratePPRSPlanning();
        }

        if (pModuleID == 876) {
            GenerateZoneMasterGrid();
        }

        if (pModuleID == 877) {
            GenerateZonePartySelectionGrid();
        }

        if (pModuleID == 206) {
            GenerateAuthorityDelegationReqGrid();
        }

        if (pModuleID == 872) {
            GenerateFeltNewBudgetEntryGrid();
        }

        if (pModuleID == 868) {
            GenerateDailyRokdiAttendanceGrid();
        }

        if (pModuleID == 865) {
            GenerateExcessManpowerGrid();
        }

        if (pModuleID == 866) {
            GenerateShiftScheduleChangeGrid();
        }
        if (pModuleID == 863) {
            GenerateShiftScheduleGrid();
        }
        if (pModuleID == 862) {
            GenerateRokdiSelectionGrid();
        }
        if (pModuleID == 860) {
            GenerateTravelVoucherGrid();
        }
        if (pModuleID == 859) {
            GenerateTravelSanctionAmendGrid();
        }
        if (pModuleID == 858) {
            GenerateTravelSanctionGrid();
        }
        if (pModuleID == 856) {
            GenerateRetainerCoffEntryGrid();
        }
        if (pModuleID == 855) {
            GenerateSpilloverReschedulingGrid();
        }

        if (pModuleID == 857) {
            GenerateSpecialRequestGrid();
        }

        if (pModuleID == 861) {
            GenerateSampleOrder();
        }

        if (pModuleID == 870) {
            GeneratePieceClubbingGrid();
        }
        if (pModuleID == 871) {
            GeneratePieceClubbingAmendGrid();
        }

        if (pModuleID == 786) {
            GenerateBeamOrderClosureGrid();
        }

        if (pModuleID == 852) {
            GenerateAllowBookingForProjection();
        }

        if (pModuleID == 864) {
            GenerateOCChange();
        }

        if (pModuleID == 851) {
            GenerateMRFGrid();
        }

        if (pModuleID == 850) {
            GenerateSDFSpirallingGrid();
        }

        if (pModuleID == 635) {
            GeneratePostAuditDRMGrid();
        }

        if (pModuleID == 846) {
            GenerateBeamGaitingStatusGrid();
        }

        if (pModuleID == 842) {
            GenerateRRAGrid();
        }

        if (pModuleID == 839) {
            GenerateRRGrid();
        }

        if (pModuleID == 837) {
            GenerateDailyAttendanceGrid();
        }

        if (pModuleID == 204) {
            GeneratePaymentReqGrid();
        }

        if (pModuleID == 834) {
            GenerateBudgetReviewEntryGrid();
        }

        if (pModuleID == 634) {
            GenerateFeltPartyContactGrid();
        }

        if (pModuleID == 831) {
            GenerateSuperannuationGrid();
        }
        if (pModuleID == 615) {
            GenerateFeltInvCvrLtrGrid();
        }

        if (pModuleID == 833) {
            Generate_PerformanceTracking();
        }
        if (pModuleID == 830) {
            GenerateMissPunchRequestGrid();
        }
        if (pModuleID == 829) {
            GenerateContractRokdiProcessGrid();
        }
        if (pModuleID == 827) {
            GenerateContractRokdiEntryGrid();
        }
        if (pModuleID == 817) {
            GenerateRokdiProcessGrid();
        }
        if (pModuleID == 818) {
            GenerateCoffProcessGrid();
        }
        if (pModuleID == 816) {
            GenerateRokdiGrid();
        }
        if (pModuleID == 815) {
            GenerateCoffGrid();
        }
        if (pModuleID == 826) {
            GenerateMonthlyAttendanceGrid();
        }

        if (pModuleID == 823) {
            GenerateSpecialDeductionGrid();
        }

        if (pModuleID == 632) {
            GenerateFeltPieceAmendmentPOGrid();
        }

        if (pModuleID == 631) {
            GenerateFeltSRSEGrid();
        }

        if (pModuleID == 804) {
            GenerateShiftChangeGrid();
        }

        if (pModuleID == 806) {
            GenerateShiftUploadGrid();
        }

        if (pModuleID == 813) {
            GenerateLeaveUpdateGrid();
        }

        if (pModuleID == 811) {
            GenerateLeaveEntryGrid();
        }

        if (pModuleID == 808) {
            GenerateHolidayWeekOffGrid();
        }

        if (pModuleID == 805) {
            GenerateStaffGatepassGrid();
        }

        if (SelModule == 822) {
            GenerateUpdatePunchDateGrid();
        }

        if (pModuleID == 819) {
            GenerateSpecialSanctionGrid();
        }

        if (pModuleID == 810) {
            GenerateTimeCorrectionGrid();
        }

        if (pModuleID == 802) {
            GenerateWHPostInvGrid();
        }

        if (pModuleID == 803) {
            GenerateWHGatepassEntryGrid();
        }

        if (pModuleID == 630) {
            GenerateFeltAutoPISelectionGrid();
        }

        if (pModuleID == clsTrailPieceEntry.ModuleID) {
            GenerateTrailPieceGrid();
        }
        if (pModuleID == clsTrailPieceDispatchEntry.ModuleID) {
            GenerateTrailPieceDispatchGrid();
        }
        if (pModuleID == clsGIDCInstruction.ModuleID) {
            GenerateGIDCInstructionGrid();
        }
        if (pModuleID == clsGIDCInstructionAmend.ModuleID) {
            GenerateGIDCInstructionAmendGrid();
        }

        if (pModuleID == 794) {
            GenerateSDMLToGIDC_NRGPGrid();
        }

        if (pModuleID == 793) {
            GenerateGIDCToSDML_NRGPGrid();
        }

        if (pModuleID == 792) {
            GenerateGIDCDespatchToSDMLGrid();
        }

        if (pModuleID == 626) {
            GenerateFeltPDCAmendGrid();
        }

        if (pModuleID == 625) {
            GenerateFeltPDCGrid();
        }

        if (pModuleID == 764) {
            GenerateFeltRejectionGrid();
        }

        if (pModuleID == clsWarpingBeamOrderHDS.ModuleID) {
            GenerateFeltWarpingBeamOrderDryerApproval();
        }
        if (pModuleID == clsWarpingBeamOrderHDSAmend.ModuleID) {
            GenerateFeltWarpingBeamOrderDryerAmendApproval();
        }
        if (pModuleID == 785) {
            GenerateFeltProductionSeamingGrid();
        }
        if (pModuleID == 601) {
            GenerateFeltQltRateMasterGrid();
        }

        if (pModuleID == 621) {
            GenerateFeltPieceAmendmentWIPGrid();
        }
        if (pModuleID == 622) {
            GenerateFeltPieceAmendmentSTOCKGrid();
        }
        if (pModuleID == 623) {
            GenerateFeltPieceAmendmentCHRGrid();
        }
        if (pModuleID == 624) {
            GenerateFeltPieceAmendmentEXPORTGrid();
        }

        if (pModuleID == 612) {
            GenerateFeltEvaluationReOpenGrid();
        }

        if (pModuleID == 614) {
            GenerateFeltLRUpdationGrid();
        }

        if (pModuleID == 611) {
            GenerateFeltEvaluationClosureGrid();
        }

        if (pModuleID == clsPartyMachineReOpen.ModuleID) {
            GeneratePartyMachinePositionReOpenGrid();
        }

        if (pModuleID == clsPartyMachineClosure.ModuleID) {
            GeneratePartyMachinePositionGrid();
        }

        if (pModuleID == 100) {
            GenerateDebitMemoCancellationGrid();
        }

        if (pModuleID == clsSalesDepositTransfer.ModuleID) {
            GenerateSalesDepositTransferGrid();
        }

        if (pModuleID == clsUpdationof09.ModuleID) {
            GenerateUpdationof09Grid();
        }

        if (pModuleID == clsUnclaimedDepositTransfer.ModuleID) {
            GenerateUnclaimedDepositTransferGrid();
        }

        if (pModuleID == clsSalesDepositRefund.ModuleID) {
            GenerateSalesDepositRefundGrid();
        }

        if (pModuleID == clsSalesDepositSchemeTransfer.ModuleID) {
            GenerateSalesDepositSchemeGrid();
        }

        if (pModuleID == clsSalesInterest.ModuleID) {
            GenerateSalesInterestGrid();
        }

        if (pModuleID == clsSalesDepositMaster.ModuleID) {
            GenerateSalesDepositMasterGrid();
        }

        if (pModuleID == clsDepositPM.ModuleID) {
            GeneratePMGrid();
        }

        if (pModuleID == clsDepositRefund.ModuleID) {
            GenerateRefundGrid();
        }

        if (pModuleID == clsCalcInterest.ModuleID) {
            GenerateInterestGrid();
        }

        if (pModuleID == clsDepositMaster.ModuleID) {
            GenerateDepositMasterGrid();
        }

        //Chirag
        if (pModuleID == clsDepositAmend.ModuleID) {
            GenerateDepositAmendGrid();
        }

        if (pModuleID == clsOBCReturn.ModuleID) {
            GenerateOBCReturnGrid();
        }

        if (pModuleID == clsOBCInvoice.ModuleID) {
            GenerateOBCInvoiceGrid();
        }

        if (pModuleID == clsOBC.ModuleID) {
            GenerateOBCGrid();
        }

        if (pModuleID == 64) {
            GenerateIssueReqRawGrid();
        }

        //        if(pModuleID==61) {
        //            GenerateBillMatchGrid();
        //        }
        if (pModuleID == 62) {
            GenerateMiscExpenseGrid();
        }

        if (pModuleID == 57) {
            GenerateGLGrid();
        }

        if (pModuleID == 54) {
            GeneratePartyGrid();
        }

        if (pModuleID == clsMemorandumJV.ModuleID) {
            GenerateMemVoucherGrid(pModuleID);
        }

        if (pModuleID == 59 || pModuleID == 65 || pModuleID == 66 || pModuleID == 67 || pModuleID == 83 || pModuleID == 68 || pModuleID == 69 || pModuleID == 70 || pModuleID == 89 || pModuleID == 94 || pModuleID == 90 || pModuleID == 205) {
            GenerateVoucherGrid(pModuleID);
        }

        if (pModuleID == 13) {
            GenerateDecFormGrid();
        }

        if (pModuleID == 52) {
            GenerateIssueReqGrid();
        }

        if (pModuleID == 171) {
            GenerateLCPartyUpdateGrid();
        }

        if (pModuleID == 606) {
            GenerateFeltLCPartyMasterGrid();
        }

        if (pModuleID == 1) {
            GenerateItemGrid();
        }

        if (pModuleID == 37) {
            GenerateSuppGrid();
        }

        if (pModuleID == 2) {
            GenerateMRGrid();
        }

        if (pModuleID == 20) {
            GenerateSummaryGrid();
        }

        if (pModuleID == 11) {
            GenerateNRGPGrid();
        }

        if (pModuleID == 12) {
            GenerateRGPGrid();
        }

        if (pModuleID == 14) {
            GenerateIssueGenGrid();
        }

        if (pModuleID == 15) {
            GenerateIssueRawGrid();
        }

        if (pModuleID == 112) {
            GenerateIssueRMGGrid();
        }

        if (pModuleID == 800) {
            GenerateIssueStoresGrid();
        }

        if (pModuleID == 40) {
            GenerateRGPReturnGrid();
        }

        if (pModuleID == 3) {
            GenerateIndentGrid();
        }

        if (pModuleID == 4) {
            GenerateGPRGrid();
        }

        if (pModuleID == 5) {
            GenerateMIRGrid(1);
        }

        if (pModuleID == 6) {
            GenerateMIRGrid(2);
        }

        if (pModuleID == 35) {
            GenerateMIRGrid(3);
        }

        if (pModuleID == 7) {
            GenerateGRNGrid(1);
        }

        if (pModuleID == 8) {
            GenerateGRNGrid(2);
        }

        if (pModuleID == 9) {
            GenerateRJNGrid(1);
        }

        if (pModuleID == 10) {
            GenerateRJNGrid(2);
        }

        if (pModuleID == 18) {
            GenerateInquiryGrid();
        }

        if (pModuleID == 21) {
            GeneratePOGrid(1);
        }

        if (pModuleID == 22) {
            GeneratePOGrid(2);
        }

        if (pModuleID == 23) {
            GeneratePOGrid(3);
        }

        if (pModuleID == 24) {
            GeneratePOGrid(4);
        }

        if (pModuleID == 25) {
            GeneratePOGrid(5);
        }

        if (pModuleID == 26) {
            GeneratePOGrid(6);
        }

        if (pModuleID == 27) {
            GeneratePOGrid(7);
        }

        if (pModuleID == 153) {
            GeneratePOGrid(9);
        }

        if (pModuleID == 28) {
            GenerateAmendGrid(1);
        }

        if (pModuleID == 29) {
            GenerateAmendGrid(2);
        }

        if (pModuleID == 30) {
            GenerateAmendGrid(3);
        }

        if (pModuleID == 31) {
            GenerateAmendGrid(4);
        }

        if (pModuleID == 32) {
            GenerateAmendGrid(5);
        }

        if (pModuleID == 33) {
            GenerateAmendGrid(6);
        }

        if (pModuleID == 34) {
            GenerateAmendGrid(7);
        }
        /* 
         if (pModuleID == 35) {
         GenerateCIFGrid();
         }*/

        if (pModuleID == 38) {
            GenerateRateApprovalGrid();
        }

        if (pModuleID == 42) {
            GenerateFreightComparisonGrid();
        }

        if (pModuleID == 43) {
            GenerateFreightCalculationGrid();
        }

        if (pModuleID == 44) {
            GenerateDocCancelGrid();
        }

        if (pModuleID == 46) {
            GenerateServiceContractGrid();
        }

        if (pModuleID == 47) {
            GenerateServiceContractAmendGrid();
        }

        if (pModuleID == 48) {
            GenerateJobWorkGrid();
        }

        if (pModuleID == 50) {
            GenerateSuppAmendGrid();
        }

        if (pModuleID == 51) {
            GenerateItemAmendGrid();
        }

        if (pModuleID == clsSales_Party.ModuleID) {
            GenerateSalesPartyGrid();
        }

        if (pModuleID == clsSalesPartyAmend.ModuleID) {
            GenerateSalesPartyAmendGrid();
        }

        if (pModuleID == clsFeltPriceList.ModuleID) {
            GenerateFeltPriceListGrid();
        }

        if (pModuleID == clsPolicyMaster.ModuleID) {
            GeneratePolicyGrid();
        }
        /*
         if(pModuleID==clsPolicyLCMaster.ModuleID) {
         GeneratePolicyLCMasterGrid();
         }
         */
        if (pModuleID == 16) {
            GenerateSTMGrid(1);
        }

        if (pModuleID == 17) {
            GenerateSTMRawGrid(2);
        }

        if (pModuleID == 61) {
            GeneratePaymentAdjustmentGrid();
        }

        if (pModuleID == 92) {
            GenerateRedeiptAdjustmentGrid();
        }

        if (pModuleID == 99) {
            GenerateDebitMemoReceiptMappingGrid();
        }

        if (pModuleID == 168) {
            GenerateAmendGrid(9);
        }

        if (pModuleID == 168) {
            GenerateAmendGrid(9);
        }
        if (pModuleID == clsSTMRequisitionRaw.ModuleID) { //STM REQ. RM 180

            GenerateSTMReqRawGrid();
        }
        if (pModuleID == clsSTMReceiptRaw.ModuleID) { //STM RECEIPT RM 181

            GenerateSTMReceiptRawGrid();
        }
        if (pModuleID == clsSTMReceiptGen.ModuleID) { //STM RECEIPT GEN 185

            GenerateSTMReceiptGenGrid();
        }
        if (pModuleID == clsSTMReqGen.ModuleID) { //STM REQ. GEN 186

            GenerateSTMReqGenGrid();
        }
        //if(pModuleID==181) {
        //    GenerateAmendGrid(9);
        // }

        //        if(pModuleID==clsCreditNoteProcessModule.ModuleID) {
        //            GenerateCreditNoteProcessGrid();
        //        }
        if (pModuleID == clsFASCardwithoutGRN.ModuleID) {
            GenerateFASCardWithoutGRNGrid();
        }

        //vivek
        if (pModuleID == 715) {
            GenerateFeltPackingGrid();
        }

        if (pModuleID == 709) {
            GenerateFeltWarpingGrid();
        }

        if (pModuleID == 716) {
            GenerateFeltFinishingGrid();
        }

        if (pModuleID == 712) {
            GenerateFeltRejectionGrid();
        }

        if (pModuleID == 710) {
            GenerateFeltProductionNeedlingGrid();
        }

        if (pModuleID == 723) {
            GenerateFeltProductionWvgLoomEffGrid();
        }

        if (pModuleID == 711) {
            GenerateFeltProductionMendingGrid();
        }

        if (pModuleID == 707) {
            GenerateFeltProductionWeavingGrid();
        }

        if (pModuleID == 704) {
            GenerateFeltProductionHeatSettingGrid();
        }
        
        if (pModuleID == 706) {
            GenerateFeltProductionMarkingGrid();
        }
        
        if (pModuleID == 726) {
            GenerateFeltProductionSplicingGrid();
        }

        if (pModuleID == 720) {
            GenerateFeltProductionFeltOrderUpdGrid();
        }

        if (pModuleID == 701) {
            //       GenerateFeltRateMasterGrid();
        }

        if (pModuleID == 702) {
            //        GenerateFeltRateUpdateGrid();
        }
        if (pModuleID == 725) {
            GenerateFeltMachineSurveyAmendGrid();
        }
        if (pModuleID == 724) {
            GenerateFeltMachineSurveyEntryGrid();
        }
        if (pModuleID == 727) {
            GenerateLCOpenerMasterGrid();
        }
        if (pModuleID == 730) {
            GenerateProductionFeltDiscRateMasterGrid();
        }
        if (pModuleID == 732) {
            GenerateProductionFeltUnadjTRNGrid();
        }

        if (pModuleID == 735) {
            GenerateProductionFeltCreditNoteGrid();
        }

        if (pModuleID == 737) {
            GenerateProductionFeltYE1CreditNoteGrid();
        }

        if (pModuleID == 738) {
            GenerateProductionFeltYE2CreditNoteGrid();
        }

        if (pModuleID == 708) {
            GenerateFeltPerfomaInvoiceGrid();
        }
        if (pModuleID == 739) {
            GenerateFeltGroupMasterGrid();
        }
        if (pModuleID == 740) {
            GenerateFeltReopenBaleGrid();
        }
        if (pModuleID == 743) {
            GenerateFeltGroupMasterAmendGrid();
        }
        if (pModuleID == 604) {
            GenerateFeltOrderDiversionGrid();
        }

        if (pModuleID == 602) {
            GenerateFeltOrderGrid();
        }

        if (pModuleID == 742) {
            GenerateFeltDiversionListGrid();
        }

        if (pModuleID == 745) {
            GenerateFeltPieceUpdationGrid();
        }

        if (pModuleID == 750) {
            GenerateFeltMM_Piece_Amend_Approval_WIP_Grid();
        }

        if (pModuleID == 763) {
            GenerateFeltMM_Piece_Amend_Approval_STOCK_Grid();
        }

        if (pModuleID == 610) {
            GenerateFeltInvoiceCancelGrid();
        }

        if (pModuleID == 603) {
            GenerateFeltSalesFinishingGrid();
        }

        if (pModuleID == 770) {
            GenerateFeltSalesLocationGrid();
        }

        if (pModuleID == 80) {
            GenerateFeltSalesInvoiceGrid();
        }

        if (pModuleID == 754) {
            GenerateFeltSalesInvoiceParameterModificationGrid();
        }

        if (pModuleID == 744) {
            GenerateFeltSalesReturnGrid();
        }

        if (pModuleID == 759) {
            GenerateFeltPieceAmendGrid();
        }

        if (pModuleID == 760) {
            GenerateFeltPieceDivisionGrid();
        }

        if (pModuleID == 761) {
            GenerateFeltInvoiceParameterModificationCancellationGrid();
        }
        if (pModuleID == 762) {
            GenerateFeltTransporterWeightGrid();
        }
        if (pModuleID == 765) {
            GenerateFeltInvoiceF6ProcssParameterGrid();
        }
        if (pModuleID == 766) {
            GenerateFeltBudgetUploadGrid();
        }
//        if (pModuleID == 768) {
//            GenerateFeltBudgetManualGrid();
//        }

        if (pModuleID == 768) {
            GenerateFeltBudgetEntryGrid();
        }

        if (pModuleID == 769) {
            GenerateFeltSalesGroupEnhancementGrid();
        }
        if (pModuleID == 774) {
            GenerateFeltSalesWIPAmend();
        }
        if (pModuleID == 776) {
            GenerateFeltSalesDelinkApproval();
        }
        if (pModuleID == clsWarpingBeamOrder.ModuleID) {
            GenerateFeltWarpingBeamOrderApproval();
        }
        if (pModuleID == clsWarpingBeamOrderAmend.ModuleID) {
            GenerateFeltWarpingBeamOrderAmendApproval();
        }

        if (pModuleID == 779) {
            GenerateFeltSalesPriorApproval();
        }
        if (pModuleID == 790) {
            GenerateGIDCProductionProcess();
        }
        if (pModuleID == 795) {
            GenerateGIDCProductionProcessAmend();
        }

        if (pModuleID == 798) {
            GenerateOrderConfirmation();
        }

        if (pModuleID == 807) {
            GeneratePieceSpiloverAssignUPN();
        }

        if (pModuleID == 812) {
            GenerateEmpMaster();
        }
        if (pModuleID == 814) {
            GenerateEmpMasterAmend();
        }
        if (pModuleID == 820) {
            GenerateWVGProdLoomwiseEntry();
        }
        if (pModuleID == 835) {
            GenerateWVGProdLoomwiseEntry_New();
        }
        if (pModuleID == 661) {
            Generate661();
        }

        DoNotPopulate = false;

        return SizeCount;
    }

    private void GenerateUserCombo() {
//        HashMap List = new HashMap();
//
//        //-------- Generating Buyer Combo --------//
//        cmbUserModel = new EITLComboModel();
//        cmbUserold.removeAllItems();
//        cmbUserold.setModel(cmbUserModel);
//        clsUser ObjUser = new clsUser();
//        List = ObjUser.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID));
//        for (int i = 1; i <= List.size(); i++) {
//            ObjUser = (clsUser) List.get(Integer.toString(i));
//
//            ComboData aData = new ComboData();
//
//            aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
//            aData.Code = (long) ObjUser.getAttribute("USER_ID").getVal();
//
//            cmbUserModel.addElement(aData);
//        }
        //----------------------------------------//

    }

    private void GenerateServiceContractGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsPOGen.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, 8, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPOGen ObjItem = (clsPOGen) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("PO_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("PO_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateServiceContractAmendGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);

        List = clsPOAmendGen.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, 8, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPOAmendGen ObjItem = (clsPOAmendGen) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("AMEND_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("AMEND_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateItemAmendGrid() {
        HashMap List = new HashMap();

        List = clsItemAmend.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsItem ObjItem = (clsItem) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = Integer.toString((int) ObjItem.getAttribute("AMEND_ID").getVal());
                    rowData[1] = (String) ObjItem.getAttribute("ITEM_DESCRIPTION").getObj();
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSuppAmendGrid() {
        HashMap List = new HashMap();

        List = clsSupplierAmend.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSupplierAmend ObjItem = (clsSupplierAmend) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = Long.toString((long) ObjItem.getAttribute("AMEND_ID").getVal());
                    rowData[1] = (String) ObjItem.getAttribute("SUPP_NAME").getObj();
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateJobWorkGrid() {
        HashMap List = new HashMap();
        //List=clsMIRRaw.getPendingApprovals(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, pType, EITLERPGLOBAL.OnDocDate,EITLERPGLOBAL.FinYearFrom);
        List = clsJobwork.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, 0, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsJobwork ObjItem = (clsJobwork) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("JOB_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("JOB_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateOBCGrid() {
        HashMap List = new HashMap();

        List = clsOBC.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsOBC ObjItem = (clsOBC) List.get(Integer.toString(i));

                Continue = true;

                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();
                    //

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateOBCInvoiceGrid() {
        HashMap List = new HashMap();

        List = clsOBCInvoice.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsOBCInvoice ObjItem = (clsOBCInvoice) List.get(Integer.toString(i));

                Continue = true;

                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();
                    //rowData[3]="";

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    //Bhavesh
    private void GenerateOBCReturnGrid() {
        HashMap List = new HashMap();

        List = clsOBCReturn.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsOBCReturn ObjItem = (clsOBCReturn) List.get(Integer.toString(i));

                Continue = true;

                if (Continue) {
                    Object[] rowData = new Object[5];

                    rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("OBC_DOC_NO").getString();

                    //rowData[3]="";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    //
    private void GenerateDepositMasterGrid() {
        HashMap List = new HashMap();
        //List=clsDepositMaster.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,EITLERPGLOBAL.OnDocDate,EITLERPGLOBAL.FinYearFrom);
        List = clsDepositMaster.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsDepositMaster ObjItem = (clsDepositMaster) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = ObjItem.getAttribute("RECEIPT_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIPT_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = ObjItem.getAttribute("DEPT_NAME").getString();
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateInterestGrid() {
        HashMap List = new HashMap();
        List = clsCalcInterest.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsCalcInterest ObjItem = (clsCalcInterest) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateDepositAmendGrid() {
        HashMap List = new HashMap();
        List = clsDepositAmend.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsDepositAmend ObjItem = (clsDepositAmend) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = ObjItem.getAttribute("AMEND_ID").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("AMEND_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRefundGrid() {
        HashMap List = new HashMap();
        List = clsDepositRefund.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsDepositRefund ObjItem = (clsDepositRefund) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GeneratePMGrid() {
        HashMap List = new HashMap();
        List = clsDepositPM.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsDepositPM ObjItem = (clsDepositPM) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    //rowData[3]=ObjItem.getAttribute("RECEIPT_NO").getString();
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSalesDepositMasterGrid() {
        HashMap List = new HashMap();
        List = clsSalesDepositMaster.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSalesDepositMaster ObjItem = (clsSalesDepositMaster) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = ObjItem.getAttribute("RECEIPT_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIPT_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = ObjItem.getAttribute("DEPT_NAME").getString();
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSalesInterestGrid() {
        HashMap List = new HashMap();
        List = clsSalesInterest.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSalesInterest ObjItem = (clsSalesInterest) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSalesDepositSchemeGrid() {
        HashMap List = new HashMap();
        List = clsSalesDepositSchemeTransfer.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSalesDepositSchemeTransfer ObjItem = (clsSalesDepositSchemeTransfer) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSalesDepositRefundGrid() {
        HashMap List = new HashMap();
        List = clsSalesDepositRefund.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSalesDepositRefund ObjItem = (clsSalesDepositRefund) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[7];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateUnclaimedDepositTransferGrid() {
        HashMap List = new HashMap();
        List = clsUnclaimedDepositTransfer.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsUnclaimedDepositTransfer ObjItem = (clsUnclaimedDepositTransfer) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = ObjItem.getAttribute("DEPT_NAME").getString();
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSalesDepositTransferGrid() {
        HashMap List = new HashMap();
        List = clsSalesDepositTransfer.getPendingApprovals(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSalesDepositTransfer ObjItem = (clsSalesDepositTransfer) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];
                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateUpdationof09Grid() {
        HashMap List = new HashMap();
        List = clsUpdationof09.getPendingApprovals(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsUpdationof09 ObjItem = (clsUpdationof09) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];
                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = ObjItem.getAttribute("VOUCHER_NO").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSalesPartyGrid() {
        HashMap List = new HashMap();
        List = clsSales_Party.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSales_Party ObjItem = (clsSales_Party) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                    rowData[1] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString();

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSalesPartyAmendGrid() {
        HashMap List = new HashMap();

        List = clsSalesPartyAmend.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSalesPartyAmend ObjItem = (clsSalesPartyAmend) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = Long.toString((long) ObjItem.getAttribute("AMEND_ID").getVal());
                    rowData[1] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateFeltPriceListGrid() {
        HashMap List = new HashMap();

        List = clsFeltPriceList.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPriceList ObjItem = (clsFeltPriceList) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                    rowData[1] = (String) ObjItem.getAttribute("QUALITY_ID").getObj();
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GeneratePolicyGrid() {
        HashMap List = new HashMap();

        List = clsPolicyMaster.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPolicyMaster ObjItem = (clsPolicyMaster) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("POLICY_ID").getObj();
                    rowData[1] = (String) ObjItem.getAttribute("POLICY_NAME").getObj();
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GeneratePolicyLCMasterGrid() {
        HashMap List = new HashMap();

        List = clsPolicyLCMaster.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPolicyLCMaster ObjItem = (clsPolicyLCMaster) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//
                if (Continue) {
                    Object[] rowData = new Object[4];
                    rowData[0] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                    rowData[1] = (String) ObjItem.getAttribute("DISC").getObj();
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "DEPT_ID";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GeneratePaymentAdjustmentGrid() {
        HashMap List = new HashMap();
        List = clsCrAdjustment.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsCrAdjustment ObjItem = (clsCrAdjustment) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRedeiptAdjustmentGrid() {
        HashMap List = new HashMap();
        List = clsDrAdjustment.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, EITLERPGLOBAL.FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsDrAdjustment ObjItem = (clsDrAdjustment) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = ObjItem.getAttribute("PARTY_NAME").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSTMReqRawGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        //List=clsIssueRequisition.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,SorOn,FinYearFrom);
        List = clsSTMRequisitionRaw.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {

            for (int i = 1; i <= List.size(); i++) {
                clsSTMRequisitionRaw ObjItem = (clsSTMRequisitionRaw) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("REQ_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("REQ_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateSTMReceiptRawGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        //List=clsIssueRequisition.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,SorOn,FinYearFrom);
        List = clsSTMReceiptRaw.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, 3, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {

            for (int i = 1; i <= List.size(); i++) {
                clsSTMReceiptRaw ObjItem = (clsSTMReceiptRaw) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("GRN_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GRN_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateSTMReceiptGenGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        //List=clsIssueRequisition.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,SorOn,FinYearFrom);
        List = clsSTMReceiptGen.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, 4, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {

            for (int i = 1; i <= List.size(); i++) {
                clsGRN ObjItem = (clsGRN) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }
                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("GRN_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GRN_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateSTMReqGenGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        //List=clsIssueRequisition.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,SorOn,FinYearFrom);
        List = clsSTMReqGen.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {

            for (int i = 1; i <= List.size(); i++) {
                //    clsSTMRequisitionRaw ObjItem=(clsSTMRequisitionRaw) List.get(Integer.toString(i));
                clsSTMReqGen ObjItem = (clsSTMReqGen) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                // rishi
                if (chkDept.isSelected()) {
                    SelDeptID = EITLERPGLOBAL.getComboCode(cmbDept);
                    if ((int) ObjItem.getAttribute("DEPT_ID").getVal() == SelDeptID) {
                        Continue = true;
                    } else {
                        Continue = false;
                    }

                }

                //==============================================//
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("STM_REQ_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("STM_REQ_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateFASCardWithoutGRNGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        //List=clsIssueRequisition.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,SorOn,FinYearFrom);
        List = clsFASCardwithoutGRN.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, clsFASCardwithoutGRN.ModuleID, FinYearFrom);
        //(int CompanyID,int UserID,int Order, int ModuleID,int FinYearFrom) {
        SizeCount = List.size();
        if (!DoNotPopulate) {

            for (int i = 1; i <= List.size(); i++) {
                clsFASCardwithoutGRN ObjItem = (clsFASCardwithoutGRN) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                //                if(chkDept.isSelected()) {
                //                    SelDeptID=EITLERPGLOBAL.getComboCode(cmbDept);
                //                    if((int)ObjItem.getAttribute("DEPT_ID").getVal()==SelDeptID) {
                //                        Continue=true;
                //                    }
                //                    else {
                //                        Continue=false;
                //                    }
                //                }
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = (String) ObjItem.getAttribute("ASSET_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("ASSET_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";
                    rowData[4] = (String) ObjItem.getAttribute("SUPPLIER_CODE").getObj();
                    rowData[5] = clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, ObjItem.getAttribute("SUPPLIER_CODE").getString());

                    //rowData[3]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID,(int)ObjItem.getAttribute("DEPT_ID").getVal());
                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateLCPartyUpdateGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsLCPartyUpd.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);

        SizeCount = List.size();
        //System.out.println("LCParty");
        if (!DoNotPopulate) {
            //GenerateLcMasterGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsLCPartyUpd ObjItem = (clsLCPartyUpd) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                /*
                 if(chkDept.isSelected()) {
                 SelDeptID=EITLERPGLOBAL.getComboCode(cmbDept);
                 if((int)ObjItem.getAttribute("DEPT_ID").getVal()==SelDeptID) {
                 Continue=true;
                 }
                 else {
                 Continue=false;
                 }
                 }
                 */
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("LC_ID").getObj();
                    //rowData[1]=(String)ObjItem.getAttribute("LC_OPENER").getObj();
                    rowData[1] = "";
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateFeltLCPartyMasterGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsFeltLCPartyMaster.getPendingApprovals(2, (int) EITLERPGLOBAL.gUserID, SorOn);

        SizeCount = List.size();
        //System.out.println("LCParty");
        if (!DoNotPopulate) {
            //GenerateLcMasterGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltLCPartyMaster ObjItem = (clsFeltLCPartyMaster) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                /*
                 if(chkDept.isSelected()) {
                 SelDeptID=EITLERPGLOBAL.getComboCode(cmbDept);
                 if((int)ObjItem.getAttribute("DEPT_ID").getVal()==SelDeptID) {
                 Continue=true;
                 }
                 else {
                 Continue=false;
                 }
                 }
                 */
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = (String) ObjItem.getAttribute("LC_NO").getObj();
                    //rowData[1]=(String)ObjItem.getAttribute("LC_OPENER").getObj();
                    rowData[1] = "";
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = "";
                    rowData[4] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[5] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjItem.getAttribute("PARTY_CODE").getString());

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateFeltCreditNoteGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsFeltCreditNote.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);

        SizeCount = List.size();
        System.out.println("CreditNOte");
        if (!DoNotPopulate) {
            //GenerateLcMasterGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltCreditNote ObjItem = (clsFeltCreditNote) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                /*
                 if(chkDept.isSelected()) {
                 SelDeptID=EITLERPGLOBAL.getComboCode(cmbDept);
                 if((int)ObjItem.getAttribute("DEPT_ID").getVal()==SelDeptID) {
                 Continue=true;
                 }
                 else {
                 Continue=false;
                 }
                 }
                 */
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("CN_ID").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("CN_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    rowData[3] = (String) ObjItem.getAttribute("PARA_DESC").getObj();;

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GenerateDMCancellationGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date.");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setPreferredWidth(80);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(100);

    }

    private void GenerateDebitMemoCancellationGrid() {
        HashMap List = new HashMap();
        List = clsDMwithoutDNCancellation.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateDMCancellationGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsDMwithoutDNCancellation ObjItem = (clsDMwithoutDNCancellation) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[3] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();
                rowData[4] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    //    private void GenerateCreditNoteProcessGrid() {
    //        HashMap List=new HashMap();
    //
    //        List=clsCreditNoteProcessModule.getPendingApprovals((int)EITLERPGLOBAL.gCompanyID,(int)EITLERPGLOBAL.gUserID,SorOn);
    //        SizeCount=List.size();
    //        if(!DoNotPopulate) {
    //            for(int i=1;i<=List.size();i++) {
    //                clsCreditNoteProcessModule ObjItem=(clsCreditNoteProcessModule) List.get(Integer.toString(i));
    //
    //                //======New Departmentwise Filtering==========//
    //                Continue=true;
    //                if(chkDept.isSelected()) {
    //                    SelDeptID=EITLERPGLOBAL.getComboCode(cmbDept);
    //                    if((int)ObjItem.getAttribute("DEPT_ID").getVal()==SelDeptID) {
    //                        Continue=true;
    //                    }
    //                    else {
    //                        Continue=false;
    //                    }
    //                }
    //                //==============================================//
    //
    //
    //                if(Continue) {
    //                    Object[] rowData=new Object[4];
    //
    //                    rowData[0]=(String)ObjItem.getAttribute("DOC_NO").getObj();
    //                    rowData[1]=(String)ObjItem.getAttribute("POLICY_ID").getObj();
    //                    rowData[2]=EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("RECEIVED_DATE").getObj());
    //                    rowData[3]="";
    //
    //                    DataModel.addRow(rowData);
    //                }
    //            }
    //        }
    //    }
    private void GenerateFeltUnadj() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        List = clsFeltUnadj.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);

        SizeCount = List.size();
        System.out.println("Unadj");
        if (!DoNotPopulate) {
            //GenerateLcMasterGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltUnadj ObjItem = (clsFeltUnadj) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                /*
                 if(chkDept.isSelected()) {
                 SelDeptID=EITLERPGLOBAL.getComboCode(cmbDept);
                 if((int)ObjItem.getAttribute("DEPT_ID").getVal()==SelDeptID) {
                 Continue=true;
                 }
                 else {
                 Continue=false;
                 }
                 }
                 */
                //==============================================//

                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("UNADJ_ID").getObj();
                    //rowData[1]=EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("CN_DATE").getObj());
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                    //rowData[3]=(String)ObjItem.getAttribute("PARA_DESC").getObj();;

                    DataModel.addRow(rowData);
                }
            }

        }
    }

    private void GeneratePartyMachinePositionGrid() {
        HashMap List = new HashMap();
        List = clsPartyMachineClosure.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPartyMachineClosure ObjItem = (clsPartyMachineClosure) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = "";
                rowData[4] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[5] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GeneratePartyMachinePositionReOpenGrid() {
        HashMap List = new HashMap();
        List = clsPartyMachineReOpen.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsPartyMachineReOpen ObjItem = (clsPartyMachineReOpen) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = "";
                rowData[4] = (String) ObjItem.getAttribute("PARTY_CODE").getObj();
                rowData[5] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltEvaluationClosureGrid() {
        HashMap List = new HashMap();
        List = clsFeltEvaluationClosure.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltEvaluationClosure ObjItem = (clsFeltEvaluationClosure) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltEvaluationReOpenGrid() {
        HashMap List = new HashMap();
        List = clsFeltEvaluationReOpen.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltEvaluationReOpen ObjItem = (clsFeltEvaluationReOpen) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltLRUpdationGrid() {
        HashMap List = new HashMap();
        List = clsFeltLRUpdation.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltLRUpdation ObjItem = (clsFeltLRUpdation) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPieceAmendmentWIPGrid() {
        HashMap List = new HashMap();

        List = clsFeltPieceAmendmentWIP.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPieceAmendmentWIP ObjDoc = (clsFeltPieceAmendmentWIP) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPieceAmendmentSTOCKGrid() {
        HashMap List = new HashMap();

        List = clsFeltPieceAmendmentSTOCK.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            //GenerateFeltProductionGrid();
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPieceAmendmentSTOCK ObjDoc = (clsFeltPieceAmendmentSTOCK) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                //rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("FELT_AMEND_PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("FELT_AMEND_PARTY_CODE").getString());
                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPieceAmendmentCHRGrid() {
        HashMap List = new HashMap();

        List = clsFeltPieceAmendmentCHR.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPieceAmendmentCHR ObjDoc = (clsFeltPieceAmendmentCHR) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPieceAmendmentEXPORTGrid() {
        HashMap List = new HashMap();

        List = clsFeltPieceAmendmentEXPORT.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPieceAmendmentEXPORT ObjDoc = (clsFeltPieceAmendmentEXPORT) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltQltRateGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Product Code");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
    }

    private void GenerateFeltQltRateMasterGrid() {
        HashMap List = new HashMap();

        List = clsFeltQltRateMaster.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltQltRateGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltQltRateMaster ObjDoc = (clsFeltQltRateMaster) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = (String) ObjDoc.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = (String) ObjDoc.getAttribute("PRODUCT_CODE").getObj();
                rowData[3] = EITLERPGLOBAL.formatDate((String) ObjDoc.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltWarpingBeamOrderDryerApproval() {
        HashMap List = new HashMap();

        List = clsWarpingBeamOrderHDS.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltWarpingBeamOrderGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsWarpingBeamOrderHDS ObjDoc = (clsWarpingBeamOrderHDS) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("BEAM_NO").getInt();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltWarpingBeamOrderDryerAmendApproval() {
        HashMap List = new HashMap();

        List = clsWarpingBeamOrderHDSAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltWarpingBeamOrderGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsWarpingBeamOrderHDSAmend ObjDoc = (clsWarpingBeamOrderHDSAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("BEAM_NO").getInt();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltProductionSeamingGrid() {
        HashMap List = new HashMap();

        List = clsFeltSeaming.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltSeaming ObjDoc = (clsFeltSeaming) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPDCGrid() {
        HashMap List = new HashMap();

        List = clsFeltPDC.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPDC ObjDoc = (clsFeltPDC) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPDCAmendGrid() {
        HashMap List = new HashMap();

        List = clsFeltPDCAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPDCAmend ObjDoc = (clsFeltPDCAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGIDCDespatchToSDMLGrid() {
        HashMap List = new HashMap();
        List = clsDespatchGIDC_SDML.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsDespatchGIDC_SDML ObjItem = (clsDespatchGIDC_SDML) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGIDCInstructionGrid() {
        HashMap List = new HashMap();
        List = clsGIDCInstruction.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGIDCInstruction_Grid();
            for (int i = 1; i <= List.size(); i++) {
                clsGIDCInstruction ObjItem = (clsGIDCInstruction) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGIDCInstructionAmendGrid() {
        HashMap List = new HashMap();
        List = clsGIDCInstructionAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGIDCInstruction_Grid();
            for (int i = 1; i <= List.size(); i++) {
                clsGIDCInstructionAmend ObjItem = (clsGIDCInstructionAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGIDCInstruction_Grid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);

    }

    private void GenerateGIDCInstructionApproval() {

        HashMap List = new HashMap();

        List = clsGIDCInstruction.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGIDCInstruction_Grid();
            for (int i = 1; i <= List.size(); i++) {
                clsGIDCInstruction ObjDoc = (clsGIDCInstruction) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGIDCInstructionAmendApproval() {

        HashMap List = new HashMap();

        List = clsGIDCInstructionAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGIDCInstruction_Grid();
            for (int i = 1; i <= List.size(); i++) {
                clsGIDCInstructionAmend ObjDoc = (clsGIDCInstructionAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateGIDCToSDML_NRGPGrid() {
        HashMap List = new HashMap();
        List = clsNRGP_GIDC.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltGIDCNRGPGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsNRGP_GIDC ObjItem = (clsNRGP_GIDC) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("GATEPASS_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GATEPASS_DATE").getObj());
                rowData[2] = (String) ObjItem.getAttribute("PIECE_NO").getObj();
                rowData[3] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSDMLToGIDC_NRGPGrid() {
        HashMap List = new HashMap();
        List = clsNRGP_SDML.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltGIDCSDMLNRGPGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsNRGP_SDML ObjItem = (clsNRGP_SDML) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("GATEPASS_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GATEPASS_DATE").getObj());
                rowData[2] = (String) ObjItem.getAttribute("ITEM_CODE").getObj();
                rowData[3] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateTrailPieceGrid() {
        HashMap List = new HashMap();
        List = clsTrailPieceEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsTrailPieceEntry ObjItem = (clsTrailPieceEntry) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateTrailPieceDispatchGrid() {
        HashMap List = new HashMap();
        List = clsTrailPieceDispatchEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsTrailPieceDispatchEntry ObjItem = (clsTrailPieceDispatchEntry) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltGIDCNRGPGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Gatepass No.");
        DataModel.addColumn("Gatepass Date");
        DataModel.addColumn("Piece No.");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
    }

    private void GenerateFeltGIDCSDMLNRGPGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Gatepass No.");
        DataModel.addColumn("Gatepass Date");
        DataModel.addColumn("Item Code.");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
    }

    private void GenerateFeltAutoPISelectionGrid() {
        HashMap List = new HashMap();

        List = clsFeltAutoPISelection.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltAutoPISelection ObjDoc = (clsFeltAutoPISelection) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateWHGatepassEntryGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);

        List = clsWHInvGatepassEntry.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn, FinYearFrom);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            FormatWHGatepassEntryGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsWHInvGatepassEntry ObjItem = (clsWHInvGatepassEntry) List.get(Integer.toString(i));

                //======New Departmentwise Filtering==========//
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = (String) ObjItem.getAttribute("GATEPASS_NO").getObj();
                    rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("GATEPASS_DATE").getObj());
                    rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());

                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void FormatWHGatepassEntryGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Gatepass No.");
        DataModel.addColumn("Gatepass Date");
        DataModel.addColumn("Received Date");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void GenerateWHPostInvGrid() {
        HashMap List = new HashMap();

        List = clsPostInvoiceEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsPostInvoiceEntry ObjDoc = (clsPostInvoiceEntry) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSpecialSanctionGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.gatepass.clsSpecialSanctionEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.gatepass.clsSpecialSanctionEntry ObjItem = (SDMLATTPAY.gatepass.clsSpecialSanctionEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateUpdatePunchDateGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.gatepass.clsUpdatePunchDateEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.gatepass.clsUpdatePunchDateEntry ObjItem = (SDMLATTPAY.gatepass.clsUpdatePunchDateEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateTimeCorrectionGrid() {
        HashMap List = new HashMap();

        List = clsTimeCorrectionEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            FormatTimeCorrectionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsTimeCorrectionEntry ObjItem = (clsTimeCorrectionEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    //rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[2] = ObjItem.getAttribute("CORRECTION_TYPE").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateStaffGatepassGrid() {
        HashMap List = new HashMap();
        List = SDMLATTPAY.gatepass.clsGatepass.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            FormatEmpGPGrid();
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.gatepass.clsGatepass ObjDoc = (SDMLATTPAY.gatepass.clsGatepass) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.EMP_DESG_DEPT_SHIFT_VIEW WHERE PAY_EMP_NO='" + ObjDoc.getAttribute("GP_EMP_NO").getString() + "'");
                if (ObjDoc.getAttribute("GP_TYPE").getString().equals("P")) {
                    rowData[3] = "Personal";
                } else if (ObjDoc.getAttribute("GP_TYPE").getString().equals("O")) {
                    rowData[3] = "Official";
                } else {
                    rowData[3] = "";
                }

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateHolidayWeekOffGrid() {
        HashMap List = new HashMap();
        List = SDMLATTPAY.holiday.clsHoliday.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.holiday.clsHoliday ObjDoc = (SDMLATTPAY.holiday.clsHoliday) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateLeaveEntryGrid() {
        HashMap List = new HashMap();
        List = SDMLATTPAY.leave.clsLeaveApplication.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            FormatEmpLeaveGrid();
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.leave.clsLeaveApplication ObjDoc = (SDMLATTPAY.leave.clsLeaveApplication) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                //rowData[2] = ObjDoc.getAttribute("LVT_PAY_EMPID").getString();
                rowData[2] = data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.EMP_DESG_DEPT_SHIFT_VIEW WHERE PAY_EMP_NO='" + ObjDoc.getAttribute("LVT_PAY_EMPID").getString() + "'");
                String lvcddays = data.getStringValueFromDB("SELECT GROUP_CONCAT(DISTINCT CONCAT(LVT_LEAVE_CODE,'-',LVT_DAYS)) AS LVCD FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO='" + ObjDoc.getAttribute("DOC_NO").getString() + "'");
                rowData[3] = lvcddays;

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateLeaveUpdateGrid() {
        HashMap List = new HashMap();
        List = SDMLATTPAY.leave.clsLeaveAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            FormatEmpLeaveGrid();
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.leave.clsLeaveAmend ObjDoc = (SDMLATTPAY.leave.clsLeaveAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateShiftChangeGrid() {
        HashMap List = new HashMap();
        List = SDMLATTPAY.Shift.clsSftChange.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.Shift.clsSftChange ObjDoc = (SDMLATTPAY.Shift.clsSftChange) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateShiftUploadGrid() {
        HashMap List = new HashMap();
        List = SDMLATTPAY.Shift.clsShiftUpload.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.Shift.clsShiftUpload ObjDoc = (SDMLATTPAY.Shift.clsShiftUpload) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPieceAmendmentPOGrid() {
        HashMap List = new HashMap();

        List = clsFeltPieceAmendmentPO.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPieceAmendmentPO ObjDoc = (clsFeltPieceAmendmentPO) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSpecialDeductionGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.gatepass.clsSpecialDeductionEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.gatepass.clsSpecialDeductionEntry ObjItem = (SDMLATTPAY.gatepass.clsSpecialDeductionEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateMonthlyAttendanceGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.MonthlyAttendance.clsMonthlyAttendance.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.MonthlyAttendance.clsMonthlyAttendance ObjItem = (SDMLATTPAY.MonthlyAttendance.clsMonthlyAttendance) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateCoffGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.COFF.clsCoffEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.COFF.clsCoffEntry ObjItem = (SDMLATTPAY.COFF.clsCoffEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRokdiGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.COFF.clsRokdiEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.COFF.clsRokdiEntry ObjItem = (SDMLATTPAY.COFF.clsRokdiEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateCoffProcessGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.COFF.clsCoffProcess.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.COFF.clsCoffProcess ObjItem = (SDMLATTPAY.COFF.clsCoffProcess) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRokdiProcessGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.COFF.clsRokdiProcess.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.COFF.clsRokdiProcess ObjItem = (SDMLATTPAY.COFF.clsRokdiProcess) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void Generate_PerformanceTracking() {
        HashMap List = new HashMap();
        List = clsPerformaceTrackingSheet.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);

        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGrid_PerformanceTracking();
            for (int i = 1; i <= List.size(); i++) {
                clsPerformaceTrackingSheet ObjItem = (clsPerformaceTrackingSheet) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = ObjItem.getAttribute("PARTY_CODE").getString();
                    rowData[4] = ObjItem.getAttribute("MACHINE_NO").getString();
                    rowData[5] = ObjItem.getAttribute("POSITION").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateMissPunchRequestGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.MissedPunchRequest.clsMissedPunchRequest.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateMissedPunchGrid();
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.MissedPunchRequest.clsMissedPunchRequest ObjItem = (SDMLATTPAY.MissedPunchRequest.clsMissedPunchRequest) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[6];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = ObjItem.getAttribute("MP_EMP_NO").getString();
                    rowData[4] = ObjItem.getAttribute("MP_EMP_NAME").getString();
                    rowData[5] = ObjItem.getAttribute("MP_EMP_DEPT").getString();
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateContractRokdiProcessGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.COFF.clsRokdiCProcess.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.COFF.clsRokdiCProcess ObjItem = (SDMLATTPAY.COFF.clsRokdiCProcess) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateTravelVoucherGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.TravelAdvance.clsTravelVoucher.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.TravelAdvance.clsTravelVoucher ObjItem = (SDMLATTPAY.TravelAdvance.clsTravelVoucher) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];
                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateTravelSanctionAmendGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.TravelAdvance.clsTravelEntryAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.TravelAdvance.clsTravelEntryAmend ObjItem = (SDMLATTPAY.TravelAdvance.clsTravelEntryAmend) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];
                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateTravelSanctionGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.TravelAdvance.clsTravelEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.TravelAdvance.clsTravelEntry ObjItem = (SDMLATTPAY.TravelAdvance.clsTravelEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];
                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRetainerCoffEntryGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.COFF.clsCoffRetainerEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.COFF.clsCoffRetainerEntry ObjItem = (SDMLATTPAY.COFF.clsCoffRetainerEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];
                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateContractRokdiEntryGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.COFF.clsRokdiCEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.COFF.clsRokdiCEntry ObjItem = (SDMLATTPAY.COFF.clsRokdiCEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];
                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateSRSEGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Code/UPN/Piece No");
        DataModel.addColumn("Name");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
    }

    private void GenerateFeltSRSEGrid() {
        HashMap List = new HashMap();

        List = clsFeltRateEligibility.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateSRSEGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltRateEligibility ObjDoc = (clsFeltRateEligibility) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("SANCTION_ON").getString();
                rowData[4] = ObjDoc.getAttribute("SANCTION_NAME").getString();

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltInvCvrLtrGrid() {
        HashMap List = new HashMap();

        List = clsFeltInvCvrLtr.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltInvCvrLtr ObjDoc = (clsFeltInvCvrLtr) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSuperannuationGrid() {
        HashMap List = new HashMap();

        List = clsSuperannuationProcess.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                clsSuperannuationProcess ObjDoc = (clsSuperannuationProcess) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateFeltPartyContactGrid() {
        HashMap List = new HashMap();

        List = clsFeltPartyContact.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionWithPartyGrid();
            for (int i = 1; i <= List.size(); i++) {
                clsFeltPartyContact ObjDoc = (clsFeltPartyContact) List.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[4] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GeneratePaymentReqGrid() {
        HashMap List = new HashMap();
        List = EITLERP.Stores.clsPaymentRequisition.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        //List = SDMLATTPAY.gatepass.clsGatepass.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.Stores.clsPaymentRequisition ObjDoc = (EITLERP.Stores.clsPaymentRequisition) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateDailyAttendanceGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.DailyAttDataForm.clsDailyAttDataForm.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.DailyAttDataForm.clsDailyAttDataForm ObjItem = (SDMLATTPAY.DailyAttDataForm.clsDailyAttDataForm) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRRGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.RokdiRequest.clsRokdiRequestForm.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.RokdiRequest.clsRokdiRequestForm ObjItem = (SDMLATTPAY.RokdiRequest.clsRokdiRequestForm) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateRRAGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.RokdiRequestAmend.clsRokdiRequestAmendForm.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.RokdiRequestAmend.clsRokdiRequestAmendForm ObjItem = (SDMLATTPAY.RokdiRequestAmend.clsRokdiRequestAmendForm) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("AMEND_DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("AMEND_DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    //GenerateAllowBookingForProjection
    private void GenerateAllowBookingForProjection() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.AllowBookingAgainstProjection.clsAllowBookingAgainstProjection.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);

        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.AllowBookingAgainstProjection.clsAllowBookingAgainstProjection ObjItem = (EITLERP.FeltSales.AllowBookingAgainstProjection.clsAllowBookingAgainstProjection) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    //GenerateOCChange
    private void GenerateOCChange() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.FeltOCChange.clsOCChange.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);

        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.FeltOCChange.clsOCChange ObjItem = (EITLERP.FeltSales.FeltOCChange.clsOCChange) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateBeamGaitingStatusGrid() {
        HashMap List = new HashMap();

        List = EITLERP.Production.BeamGaitingStatus.clsBeamGaitingStatus.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.Production.BeamGaitingStatus.clsBeamGaitingStatus ObjItem = (EITLERP.Production.BeamGaitingStatus.clsBeamGaitingStatus) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("BGS_DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GeneratePostAuditDRMGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.PostAuditDiscRateMaster.clsPostAuditDiscRateMaster.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.PostAuditDiscRateMaster.clsPostAuditDiscRateMaster ObjDoc = (EITLERP.FeltSales.PostAuditDiscRateMaster.clsPostAuditDiscRateMaster) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSDFSpirallingGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.SDF.FeltSpiral.clsFeltSpiralling.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.SDF.FeltSpiral.clsFeltSpiralling ObjDoc = (EITLERP.FeltSales.SDF.FeltSpiral.clsFeltSpiralling) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateObsoleteScrapFormatGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Piece No");
        DataModel.addColumn("Piece Stage");
        DataModel.addColumn("Group");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
        tcm.getColumn(5).setPreferredWidth(120);
    }

    private void GenerateObsoleteScrap() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.ObsoleteScrap.clsFeltObsoleteScrap.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateObsoleteScrapFormatGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.ObsoleteScrap.clsFeltObsoleteScrap ObjDoc = (EITLERP.FeltSales.ObsoleteScrap.clsFeltObsoleteScrap) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[3] = ObjDoc.getAttribute("PIECE_NO").getString();
                String pStage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + ObjDoc.getAttribute("PIECE_NO").getString() + "' ");
                rowData[4] = pStage;
                String pGrp = data.getStringValueFromDB("SELECT PR_GROUP FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + ObjDoc.getAttribute("PIECE_NO").getString() + "' ");
                rowData[5] = pGrp;

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateIncrementGrid() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Department");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void GenerateGrid_MRF() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Party Group");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Action Date");
        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
    }

    private void GenerateIncrementProposal() {
        HashMap List = new HashMap();
        GenerateIncrementGrid();
        List = SDMLATTPAY.IncrementProposal.clsIncrementProposal.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.IncrementProposal.clsIncrementProposal ObjItem = (SDMLATTPAY.IncrementProposal.clsIncrementProposal) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = (ObjItem.getAttribute("IED_DEPARTMENT").getString());
                    DataModel.addRow(rowData);
                }
            }
            final TableColumnModel columnModel2 = Table.getColumnModel();
            for (int column = 0; column < Table.getColumnCount(); column++) {
                int width = 100; // Min width
                for (int row = 0; row < Table.getRowCount(); row++) {
                    TableCellRenderer renderer = Table.getCellRenderer(row, column);
                    Component comp = Table.prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 10, width);
                }
                if (width > 300) {
                    width = 300;
                }
                columnModel2.getColumn(column).setPreferredWidth(width);
            }
        }
    }

    private void GenerateMRFGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.MachineRunForcasting.clsMachineRunForcastingEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGrid_MRF();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.MachineRunForcasting.clsMachineRunForcastingEntry ObjItem = (EITLERP.FeltSales.MachineRunForcasting.clsMachineRunForcastingEntry) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[5];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = ObjItem.getAttribute("PARTY_NAME").getString();
                    rowData[2] = ObjItem.getAttribute("PARTY_GROUP").getString();
                    rowData[3] = (ObjItem.getAttribute("received_date").getString());
                    rowData[4] = (ObjItem.getAttribute("action_date").getString());
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateGrid_BOC() {
        DataModel = new EITLTableModel();

        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Set the table Readonly
        DataModel.TableReadOnly(true);

        //Add Columns
        DataModel.addColumn("Document No.");
        DataModel.addColumn("Document Date");
        DataModel.addColumn("Received Date");
        DataModel.addColumn("Beam No");
        DataModel.addColumn("Beam Doc No");

        TableColumnModel tcm = Table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(120);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(120);
        tcm.getColumn(3).setPreferredWidth(120);
        tcm.getColumn(4).setPreferredWidth(120);
    }

    private void GenerateBeamOrderClosureGrid() {
        HashMap List = new HashMap();
        List = EITLERP.FeltSales.BeamOrderClosure.clsBeamOrderClosure.getPendingApprovals((int) EITLERPGLOBAL.gCompanyID, (int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateGrid_BOC();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.BeamOrderClosure.clsBeamOrderClosure ObjItem = (EITLERP.FeltSales.BeamOrderClosure.clsBeamOrderClosure) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                rowData[3] = (String) ObjItem.getAttribute("BEAM_NO").getObj();
                rowData[4] = (String) ObjItem.getAttribute("BEAM_DOC_NO").getObj();

                DataModel.addRow(rowData);
            }
        }
    }

    public void checkacess(String strClass) {
        try {
            ResultSet ruser = data.getResult("SELECT USER()");
            ruser.first();
            String str = ruser.getString(1);
            String str_split[] = str.split("@");
            int validuser = 0;
            validuser = data.getIntValueFromDB("SELECT COUNT(*) FROM SDMLATTPAY.HOD WHERE HOD_USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND HOD_IP='" + str_split[1] + "'");
            if (validuser == 0) {
                JOptionPane.showMessageDialog(this, "You can not acess this system...", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("SELECT COUNT(*) FROM SDMLATTPAY.HOD H "
                        + "WHERE HOD_USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND HOD_PASSWORD_YEAR=YEAR('" + EITLERPGLOBAL.FinFromDateDB + "')");
                if (data.getIntValueFromDB("SELECT COUNT(*) FROM SDMLATTPAY.HOD H "
                        + "WHERE HOD_USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND HOD_PASSWORD_YEAR=YEAR('" + EITLERPGLOBAL.FinFromDateDB + "')") > 0) {
                    String DocNo = (String) Table.getValueAt(Table.getSelectedRow(), 0);
                    //AppletFrame.startApplet("SDMLATTPAY.IncrementProposal.frmSuperPassword", "SDML Employee Management Increment Proposal");
                    frmSuperPasswordHOD objDialog = new frmSuperPasswordHOD();
                    objDialog.mDocNo = DocNo;
                    objDialog.ShowDialog();
                    if (objDialog.cancelled) {
                        JOptionPane.showMessageDialog(null, "Enter Password before logging into Increment Proposal system");
                    }
                } else {
                    frmChangeSuperPassword objDialog = new frmChangeSuperPassword();
                    objDialog.ShowDialog();
                    if (objDialog.cancelled) {
                        JOptionPane.showMessageDialog(null, "Set Password before logging into Increment Proposal system");
                    }
                }
                //AppletFrame.startApplet(strClass.trim(), "SDML Employee Management Increment Proposal");
            }
        } catch (Exception e) {
        }
    }

    private void GenerateSpecialRequestGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.SpecialRequest.clsSpiloverSpecialReqDateApproval.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.SpecialRequest.clsSpiloverSpecialReqDateApproval ObjDoc = (EITLERP.FeltSales.SpecialRequest.clsSpiloverSpecialReqDateApproval) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSampleOrder() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.SampleOrder.clsFeltSampleOrder.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.SampleOrder.clsFeltSampleOrder ObjDoc = (EITLERP.FeltSales.SampleOrder.clsFeltSampleOrder) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSpilloverReschedulingGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.SpilloverRescheduling.clsInStockSpillover.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.SpilloverRescheduling.clsInStockSpillover ObjDoc = (EITLERP.FeltSales.SpilloverRescheduling.clsInStockSpillover) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateSpilloverReschedulingNewGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.SpilloverRescheduling_New.clsInStockSpillover_New.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltReschedulingNewGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.SpilloverRescheduling_New.clsInStockSpillover_New ObjDoc = (EITLERP.FeltSales.SpilloverRescheduling_New.clsInStockSpillover_New) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = ObjDoc.getAttribute("PIECE_NO").getString();
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[3] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GeneratePieceClubbingGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.SalesPieceClubbing.clsSalesPieceClubbing.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            //GenerateFeltReschedulingNewGrid();
            GenerateFeltClubbingGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.SalesPieceClubbing.clsSalesPieceClubbing ObjDoc = (EITLERP.FeltSales.SalesPieceClubbing.clsSalesPieceClubbing) List.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = ObjDoc.getAttribute("PIECE_NO").getString();
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[3] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());
                rowData[4] = ObjDoc.getAttribute("PARTY_CODE").getString();
                rowData[5] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, ObjDoc.getAttribute("PARTY_CODE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    //
    private void GeneratePieceClubbingReschedulingGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.ClubbingSchChange.clsClubbedPieceSchChange.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltReschedulingNewGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.ClubbingSchChange.clsClubbedPieceSchChange ObjDoc = (EITLERP.FeltSales.ClubbingSchChange.clsClubbedPieceSchChange) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = ObjDoc.getAttribute("PIECE_NO").getString();
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[3] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GeneratePieceClubbingAmendGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.SalesPieceClubbingAmend.clsSalesPieceClubbingAmend.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltReschedulingNewGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.SalesPieceClubbingAmend.clsSalesPieceClubbingAmend ObjDoc = (EITLERP.FeltSales.SalesPieceClubbingAmend.clsSalesPieceClubbingAmend) List.get(Integer.toString(i));
                Object[] rowData = new Object[4];

                rowData[0] = ObjDoc.getAttribute("PC_AMEND_DOC_NO").getString();
                rowData[1] = ObjDoc.getAttribute("PIECE_NO").getString();
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("PC_AMEND_DOC_DATE").getString());
                rowData[3] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateRokdiSelectionGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.RokdiSelection.clsRokdiSelection.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.RokdiSelection.clsRokdiSelection ObjItem = (SDMLATTPAY.RokdiSelection.clsRokdiSelection) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateShiftScheduleGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.AdvanceShiftChange.clsShiftSchedule.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.AdvanceShiftChange.clsShiftSchedule ObjItem = (SDMLATTPAY.AdvanceShiftChange.clsShiftSchedule) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateShiftScheduleChangeGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.AdvanceShiftChange.clsShiftScheduleChange.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.AdvanceShiftChange.clsShiftScheduleChange ObjItem = (SDMLATTPAY.AdvanceShiftChange.clsShiftScheduleChange) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateExcessManpowerGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.ManpowerRequisition.clsManpowerRequisitionForm.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.ManpowerRequisition.clsManpowerRequisitionForm ObjItem = (SDMLATTPAY.ManpowerRequisition.clsManpowerRequisitionForm) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateDailyRokdiAttendanceGrid() {
        HashMap List = new HashMap();

        List = SDMLATTPAY.DailyRokdiAttData.clsDailyRokdiAttData.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                SDMLATTPAY.DailyRokdiAttData.clsDailyRokdiAttData ObjItem = (SDMLATTPAY.DailyRokdiAttData.clsDailyRokdiAttData) List.get(Integer.toString(i));
                Continue = true;
                if (Continue) {
                    Object[] rowData = new Object[4];

                    rowData[0] = ObjItem.getAttribute("DOC_NO").getString();
                    rowData[1] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("DOC_DATE").getString());
                    rowData[2] = EITLERPGLOBAL.formatDate(ObjItem.getAttribute("RECEIVED_DATE").getString());
                    rowData[3] = "";
                    DataModel.addRow(rowData);
                }
            }
        }
    }

    private void GenerateAuthorityDelegationReqGrid() {
        HashMap List = new HashMap();
        List = EITLERP.AuthorityDelegation.clsAuthorityDelegationRequest.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        //List = SDMLATTPAY.gatepass.clsGatepass.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.AuthorityDelegation.clsAuthorityDelegationRequest ObjDoc = (EITLERP.AuthorityDelegation.clsAuthorityDelegationRequest) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }

    private void GenerateAuthDelCombo() {
        //--- Module Combo ------//
        cmbAuthorityModel = new EITLComboModel();
        cmbAuthority.removeAllItems();
        cmbAuthority.setModel(cmbAuthorityModel);

        ComboData aData = new ComboData();
        aData.Text = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID);
        aData.Code = EITLERPGLOBAL.gUserID;
        cmbAuthorityModel.addElement(aData);

        HashMap List = clsAuthority.getAvailableAuthority(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID);

        for (int i = 1; i <= List.size(); i++) {
            clsAuthority ObjAuthority = (clsAuthority) List.get(Integer.toString(i));

            //Check that Module Access Rights are given
            int AuthorityID = (int) ObjAuthority.getAttribute("AUTHORITY_USER_ID").getVal();

            //if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,MenuID)) {
            aData = new ComboData();
            aData.Text = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, AuthorityID);
            aData.Code = AuthorityID;
            cmbAuthorityModel.addElement(aData);
            //}
        }
        //===============================//

    }

    private void GenerateFeltNewBudgetEntryGrid() {
        HashMap List = new HashMap();

        int FinYearFrom = EITLERPGLOBAL.getComboCode(cmbFinYear);
        //List = clsBudgetManual.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        List = EITLERP.FeltSales.Budget.clsNewBudgetEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            //GenerateFeltBudgetGrid();
            FormatFeltBudgetEntryGrid();
            for (int i = 1; i <= List.size(); i++) {
                //clsBudgetManual ObjItem = (clsBudgetManual) List.get(Integer.toString(i));
                EITLERP.FeltSales.Budget.clsNewBudgetEntry ObjItem = (EITLERP.FeltSales.Budget.clsNewBudgetEntry) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = (String) ObjItem.getAttribute("PARTY_GROUP").getObj();
                rowData[2] = (String) ObjItem.getAttribute("PARTY_NAME").getObj();
//                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
//                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }
    
    private void GenerateZoneMasterGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.ZoneMaster.clsZoneMaster.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.ZoneMaster.clsZoneMaster ObjDoc = (EITLERP.FeltSales.ZoneMaster.clsZoneMaster) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }
    
    private void GenerateZonePartySelectionGrid() {
        HashMap List = new HashMap();

        List = EITLERP.FeltSales.ZoneMaster.clsZoneMasterPartySelection.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateFeltProductionGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.ZoneMaster.clsZoneMasterPartySelection ObjDoc = (EITLERP.FeltSales.ZoneMaster.clsZoneMasterPartySelection) List.get(Integer.toString(i));
                Object[] rowData = new Object[3];

                rowData[0] = ObjDoc.getAttribute("DOC_NO").getString();
                rowData[1] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("DOC_DATE").getString());
                rowData[2] = EITLERPGLOBAL.formatDate(ObjDoc.getAttribute("RECEIVED_DATE").getString());

                DataModel.addRow(rowData);
            }
        }
    }
    
    private void GeneratePPRSPlanning() {
        HashMap List = new HashMap();
        List = EITLERP.FeltSales.PPRSPlanning.clsPPRSEntry.getPendingApprovals((int) EITLERPGLOBAL.gUserID, SorOn);
        SizeCount = List.size();
        if (!DoNotPopulate) {
            GenerateOrderConfirmationGrid();
            for (int i = 1; i <= List.size(); i++) {
                EITLERP.FeltSales.PPRSPlanning.clsPPRSEntry ObjItem = (EITLERP.FeltSales.PPRSPlanning.clsPPRSEntry) List.get(Integer.toString(i));

                Object[] rowData = new Object[4];

                rowData[0] = (String) ObjItem.getAttribute("DOC_NO").getObj();
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DOC_DATE").getObj());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("RECEIVED_DATE").getObj());
                //rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());

                DataModel.addRow(rowData);
            }
        }
    }
    

}
