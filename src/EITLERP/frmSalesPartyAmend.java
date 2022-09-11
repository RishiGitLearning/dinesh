/*
 * frmSalesPartyAmend.java
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP;

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
import java.sql.*;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.JTextComponent;
import java.util.HashMap;

public class frmSalesPartyAmend extends javax.swing.JApplet {

    private int EditMode = 0;

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelU;
    private EITLTableModel DataModelMainCode;
    private EITLComboModel cmbTaggingTypeModel;

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    //clsColumn ObjColumn=new clsColumn();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private clsSalesPartyAmend ObjSalesParty;

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;

    private EITLComboModel cmbPartyTypeModel;
    private EITLComboModel cmbMainCodeTypeModel;
    private EITLComboModel cmbCategoryModel;
    private EITLComboModel cmbChargeCodeModel;
    private EITLComboModel cmbSecondChargeCodeModel;
    private EITLComboModel cmbStateModel;
    private EITLComboModel cmbCountryModel;

    private EITLComboModel cmbZoneModel;

    private EITLTableModel DataModelA;

    private boolean HistoryView = false;
    private String theDocNo = "";
    private EITLTableModel DataModelHS;

    public frmPendingApprovals frmPA;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();

    double critical_limit_original;

    /**
     * Creates new form frmSalesPartyAmend
     */
    public frmSalesPartyAmend() {
        setSize(870, 680);
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

        String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);

        if (type.trim().equals("210010")) {
            ChargeCodeCombos(" AND INVOICE_TYPE_ID NOT IN ('S04') ");
        } else {
            ChargeCodeCombos(" AND INVOICE_TYPE_ID NOT IN ('F04') ");
        }

        FormatGrid();
        GenerateGrid();
        TableMainCode.setEnabled(false);
        cmdSelectAll.setEnabled(false);
        cmdClearAll.setEnabled(false);

        ObjSalesParty = new clsSalesPartyAmend();
        txtOldPartyCode.setVisible(false);
        lblDocThrough.setVisible(false);
        txtDocThrough.setVisible(false);
        cmbZone.setEnabled(false);
        txtAreaInchargeCode.setEnabled(false);
        txtMarketSegment.setEnabled(false);
        txtClientCategory.setEnabled(false);
        txtProductSegment.setEnabled(false);

        GenerateZoneCombo();
        GenerateCountryCombo();
        GenerateStateCombo();

        if (ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID)) {
            //ObjSalesParty.MoveFirst();
            DisplayData();
            SetMenuForRights();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while loading data. \n Error is " + ObjSalesParty.LastError);
        }

        txtAuditRemarks.setVisible(false);

    }

    private void ChargeCodeCombos(String strCon) {
        //----------Charge Code---------//
        cmbChargeCodeModel = new EITLComboModel();
        cmbChargeCode.removeAllItems();
        cmbChargeCode.setModel(cmbChargeCodeModel);

        HashMap List = new HashMap();
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " " + strCon;
        List = clsPolicyInvoiceType.getList(strCondition);

        for (int i = 1; i <= List.size(); i++) {
            clsPolicyInvoiceType ObjType = (clsPolicyInvoiceType) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            String ChargeCode = (String) ObjType.getAttribute("INVOICE_TYPE_ID").getObj();
            if ((ChargeCode.trim().substring(0, 1).equals("F")) || (ChargeCode.trim().substring(0, 1).equals("S"))) {
                ChargeCode = "04";
            }
            aData.strCode = ChargeCode;
            aData.Text = ChargeCode + " , " + (String) ObjType.getAttribute("INVOICE_TYPE_DESC").getObj();
            cmbChargeCodeModel.addElement(aData);
        }
        //------------------------------//

        //----------Second Charge Code---------//
        cmbSecondChargeCodeModel = new EITLComboModel();
        cmbSecondChargeCode.removeAllItems();
        cmbSecondChargeCode.setModel(cmbSecondChargeCodeModel);

        List = new HashMap();
        strCondition = " WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " " + strCon;
        List = clsPolicyInvoiceType.getList(strCondition);

        for (int i = 1; i <= List.size(); i++) {
            clsPolicyInvoiceType ObjType = (clsPolicyInvoiceType) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            String ChargeCode = (String) ObjType.getAttribute("INVOICE_TYPE_ID").getObj();
            if ((ChargeCode.trim().substring(0, 1).equals("F")) || (ChargeCode.trim().substring(0, 1).equals("S"))) {
                ChargeCode = "04";
            }
            aData.strCode = ChargeCode;
            aData.Text = ChargeCode + " , " + (String) ObjType.getAttribute("INVOICE_TYPE_DESC").getObj();
            cmbSecondChargeCodeModel.addElement(aData);
        }
        //------------------------------//

        Object Obj = "09";
        cmbSecondChargeCode.setSelectedItem(Obj);
    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsSalesPartyAmend.ModuleID);

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsSalesPartyAmend.ModuleID);
        }

        for (int i = 1; i <= List.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //

        //-----------Party Type----------//
        cmbPartyTypeModel = new EITLComboModel();
        cmbPartyType.removeAllItems();
        cmbPartyType.setModel(cmbPartyTypeModel);

        ComboData aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Agent";
        cmbPartyTypeModel.addElement(aData);

        aData = new ComboData();
        aData.Code = 3;
        aData.Text = "Wholesaler";
        cmbPartyTypeModel.addElement(aData);

        aData = new ComboData();
        aData.Code = 2;
        aData.Text = "Retailer";
        cmbPartyTypeModel.addElement(aData);

        aData = new ComboData();
        aData.Code = 4;
        aData.Text = "Miscellaneous";
        cmbPartyTypeModel.addElement(aData);

        aData = new ComboData();
        aData.Code = 5;
        aData.Text = "Other";
        cmbPartyTypeModel.addElement(aData);
        //------------------------------ //

        //-------Main Code Type --------//
        cmbMainCodeTypeModel = new EITLComboModel();
        cmbMainCodeType.removeAllItems();
        cmbMainCodeType.setModel(cmbMainCodeTypeModel);

        aData = new ComboData();
        aData.strCode = "210027";
        aData.Text = "Suiting";
        cmbMainCodeTypeModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "210010";
        aData.Text = "Felt";
        cmbMainCodeTypeModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "210072";
        aData.Text = "Filter";
        cmbMainCodeTypeModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "210034";
        aData.Text = "Miscellaneous Sales";
        cmbMainCodeTypeModel.addElement(aData);
        //------------------------------//

        //--------Category--------//
        cmbCategoryModel = new EITLComboModel();
        cmbCategory.removeAllItems();
        cmbCategory.setModel(cmbCategoryModel);

        aData = new ComboData();
        aData.strCode = "Company";
        aData.Text = "Company";
        cmbCategoryModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Other";
        aData.Text = "Other";
        cmbCategoryModel.addElement(aData);
        //------------------------------//

        Object Obj = "Other";
        cmbCategory.setSelectedItem(Obj);
        
         //-----------Tagging type----------//
        cmbTaggingTypeModel=new EITLComboModel();
        cmbStockTagging.removeAllItems();
        cmbStockTagging.setModel(cmbTaggingTypeModel);
        
        aData=new ComboData();
        aData.Code=0;
        aData.Text="0-Tagging approval required from Shri Adityabhai Patel";
        cmbTaggingTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="1-Weight to be tagged on higher side";
        cmbTaggingTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="2-Weight to be tagged only on theoritical weight";
        cmbTaggingTypeModel.addElement(aData);
        
        
     
        aData=new ComboData();
        aData.Code=3;
        aData.Text="3-Weight/GSM to be tagged based on Tender Weight/GSM  entered  on Sales Order Entry";
        cmbTaggingTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=4;
        aData.Text="4-Weight to be tagged on 3% of higher side";
        cmbTaggingTypeModel.addElement(aData);   
        //-------------------------------//
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
        cmdShowOriginal = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        cmdNext1 = new javax.swing.JButton();
        txtPartyCode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAreaID = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAdd1 = new javax.swing.JTextField();
        txtAdd2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPIN = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        txtMobile = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtURL = new javax.swing.JTextField();
        txtContactPerson = new javax.swing.JTextField();
        cmbPartyType = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtOldPartyCode = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtAmendID = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtAmendDate = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txtAmendReason = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        cmbMainCodeType = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        cmbChargeCode = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        cmbSecondChargeCode = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        txtCreditDays = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtInsuranceCode = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox();
        lblSeasonCode = new javax.swing.JLabel();
        txtSeasonCode = new javax.swing.JTextField();
        lblRegDate = new javax.swing.JLabel();
        txtRegDate = new javax.swing.JTextField();
        chkDoNotAllowInterest = new javax.swing.JCheckBox();
        txtDispatchStation = new javax.swing.JTextField();
        lblDispatchStation = new javax.swing.JLabel();
        txtCorrAdd = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        cmbState = new javax.swing.JComboBox();
        jLabel46 = new javax.swing.JLabel();
        cmbCountry = new javax.swing.JComboBox();
        jLabel47 = new javax.swing.JLabel();
        txtExtraCreditDays = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtGraceCreditDays = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtDistanceKm = new javax.swing.JTextField();
        txtEmail3 = new javax.swing.JTextField();
        txtEmail2 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        txtMobile3 = new javax.swing.JTextField();
        txtMobile2 = new javax.swing.JTextField();
        txtDesignation = new javax.swing.JTextField();
        txtDesignation2 = new javax.swing.JTextField();
        txtDesignation3 = new javax.swing.JTextField();
        txtContactPerson3 = new javax.swing.JTextField();
        txtContactPerson2 = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        Tab3 = new javax.swing.JPanel();
        cmdNext2 = new javax.swing.JButton();
        cmdNext4 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtCST = new javax.swing.JTextField();
        txtCSTDate = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtTinNo = new javax.swing.JTextField();
        txtTinDate = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtECC = new javax.swing.JTextField();
        txtECCDate = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        txtAmountLimit = new javax.swing.JTextField();
        lblDocThrough = new javax.swing.JLabel();
        txtDocThrough = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblBankID = new javax.swing.JLabel();
        txtBankID = new javax.swing.JTextField();
        txtBankName = new javax.swing.JTextField();
        lblBankID1 = new javax.swing.JLabel();
        txtBankAdd = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtBankCity = new javax.swing.JTextField();
        txtOtherBankID = new javax.swing.JTextField();
        lblBankID3 = new javax.swing.JLabel();
        txtOtherBankName = new javax.swing.JTextField();
        lblBankID4 = new javax.swing.JLabel();
        txtOtherBankAdd = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtOtherBankCity = new javax.swing.JTextField();
        lblBankID2 = new javax.swing.JLabel();
        txtTranID = new javax.swing.JTextField();
        txtTranName = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtPANNo = new javax.swing.JTextField();
        txtPANDate = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        cmdRemarksBig = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        txtInterestPer = new javax.swing.JTextField();
        txtGSTN = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtGSTDate = new javax.swing.JTextField();
        chkCriticalUnCheck = new javax.swing.JCheckBox();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        lblTransID = new javax.swing.JLabel();
        cmbDeliveryMode = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        lblSubCode9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableMainCode = new javax.swing.JTable();
        cmdSelectAll = new javax.swing.JButton();
        cmdClearAll = new javax.swing.JButton();
        cmdBack5 = new javax.swing.JButton();
        cmdNext6 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        cmbZone = new javax.swing.JComboBox();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txtDesignerIncharge = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txtMarketSegment = new javax.swing.JTextField();
        txtClientCategory = new javax.swing.JTextField();
        txtProductSegment = new javax.swing.JTextField();
        txtAreaInchargeCode = new javax.swing.JTextField();
        txtAreaInchargeName = new javax.swing.JTextField();
        lblClientCategory = new javax.swing.JLabel();
        cmdBack6 = new javax.swing.JButton();
        cmdNext7 = new javax.swing.JButton();
        cmbStockTagging = new javax.swing.JComboBox();
        chkPONoRequired = new javax.swing.JCheckBox();
        chkKeyClientInd = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableU = new javax.swing.JTable();
        cmdShowRemarksU = new javax.swing.JButton();
        cmdBack7 = new javax.swing.JButton();
        cmdNext8 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
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
        btnSendMail = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
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
        ToolBar.add(cmdPreview);

        cmdPrint.setToolTipText("Print");
        ToolBar.add(cmdPrint);

        cmdExit.setToolTipText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        ToolBar.add(cmdExit);

        cmdShowOriginal.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        cmdShowOriginal.setText("<html><center>Show <br>Original</html>");
        cmdShowOriginal.setToolTipText("Show Original");
        cmdShowOriginal.setMaximumSize(new java.awt.Dimension(48, 34));
        cmdShowOriginal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowOriginalActionPerformed(evt);
            }
        });
        ToolBar.add(cmdShowOriginal);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 850, 40);

        lblTitle.setBackground(new java.awt.Color(13, 117, 169));
        lblTitle.setText("Sales Party Master Updation");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 850, 25);

        jTabbedPane1.setToolTipText("Party Master Header");

        Tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.setToolTipText("Party Header");
        Tab1.setLayout(null);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Party Code :");
        Tab1.add(jLabel2);
        jLabel2.setBounds(5, 60, 105, 15);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Name :");
        Tab1.add(jLabel5);
        jLabel5.setBounds(5, 85, 105, 15);

        txtName.setName(""); // NOI18N
        txtName.setNextFocusableComponent(cmbPartyType);
        txtName.setEnabled(false);
        Tab1.add(txtName);
        txtName.setBounds(115, 85, 430, 19);

        cmdNext1.setText("Next >>");
        cmdNext1.setNextFocusableComponent(txtPartyCode);
        cmdNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext1ActionPerformed(evt);
            }
        });
        cmdNext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdNext1KeyPressed(evt);
            }
        });
        Tab1.add(cmdNext1);
        cmdNext1.setBounds(720, 500, 90, 25);

        txtPartyCode.setEnabled(false);
        txtPartyCode.setName(""); // NOI18N
        txtPartyCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartyCodeActionPerformed(evt);
            }
        });
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
        Tab1.add(txtPartyCode);
        txtPartyCode.setBounds(115, 60, 120, 19);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Agent Prefix :");
        Tab1.add(jLabel4);
        jLabel4.setBounds(5, 230, 105, 15);

        txtAreaID.setName("ATTN"); // NOI18N
        txtAreaID.setEnabled(false);
        Tab1.add(txtAreaID);
        txtAreaID.setBounds(115, 230, 120, 19);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Address :");
        Tab1.add(jLabel7);
        jLabel7.setBounds(5, 260, 105, 15);

        txtAdd1.setName("ADD1"); // NOI18N
        txtAdd1.setNextFocusableComponent(txtAdd2);
        txtAdd1.setEnabled(false);
        Tab1.add(txtAdd1);
        txtAdd1.setBounds(115, 260, 430, 19);

        txtAdd2.setEnabled(false);
        txtAdd2.setName("ADD2"); // NOI18N
        txtAdd2.setNextFocusableComponent(txtCity);
        txtAdd2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdd2FocusLost(evt);
            }
        });
        Tab1.add(txtAdd2);
        txtAdd2.setBounds(115, 290, 430, 19);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Address City :");
        Tab1.add(jLabel9);
        jLabel9.setBounds(5, 350, 105, 15);

        txtCity.setEnabled(false);
        txtCity.setName("CITY"); // NOI18N
        txtCity.setNextFocusableComponent(txtPIN);
        Tab1.add(txtCity);
        txtCity.setBounds(115, 350, 120, 19);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Pincode :");
        Tab1.add(jLabel10);
        jLabel10.setBounds(280, 350, 110, 15);

        txtPIN.setEnabled(false);
        txtPIN.setName("PINCODE"); // NOI18N
        txtPIN.setNextFocusableComponent(txtState);
        Tab1.add(txtPIN);
        txtPIN.setBounds(395, 350, 150, 19);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("District :");
        Tab1.add(jLabel6);
        jLabel6.setBounds(5, 380, 105, 15);

        txtState.setEnabled(false);
        txtState.setName("STATE"); // NOI18N
        txtState.setNextFocusableComponent(txtPhone);
        txtState.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStateFocusLost(evt);
            }
        });
        Tab1.add(txtState);
        txtState.setBounds(115, 380, 120, 19);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Phone No :");
        Tab1.add(jLabel12);
        jLabel12.setBounds(5, 410, 105, 15);

        txtPhone.setName("PHONE_O"); // NOI18N
        txtPhone.setNextFocusableComponent(txtMobile);
        txtPhone.setEnabled(false);
        Tab1.add(txtPhone);
        txtPhone.setBounds(115, 410, 120, 19);

        txtMobile.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtMobile.setEnabled(false);
        txtMobile.setName("MOBILE_NO"); // NOI18N
        txtMobile.setNextFocusableComponent(txtEmail);
        txtMobile.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMobileFocusLost(evt);
            }
        });
        Tab1.add(txtMobile);
        txtMobile.setBounds(340, 450, 140, 20);

        txtEmail.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtEmail.setEnabled(false);
        txtEmail.setName("EMAIL_ADD"); // NOI18N
        txtEmail.setNextFocusableComponent(txtURL);
        Tab1.add(txtEmail);
        txtEmail.setBounds(480, 450, 210, 20);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("URL :");
        Tab1.add(jLabel16);
        jLabel16.setBounds(265, 410, 50, 15);

        txtURL.setEnabled(false);
        txtURL.setName("URL"); // NOI18N
        txtURL.setNextFocusableComponent(txtContactPerson);
        Tab1.add(txtURL);
        txtURL.setBounds(320, 410, 230, 19);

        txtContactPerson.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtContactPerson.setEnabled(false);
        txtContactPerson.setName("CONTACT_PERSON_1"); // NOI18N
        txtContactPerson.setNextFocusableComponent(txtTranID);
        txtContactPerson.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContactPersonFocusLost(evt);
            }
        });
        Tab1.add(txtContactPerson);
        txtContactPerson.setBounds(20, 450, 180, 20);

        cmbPartyType.setEditable(true);
        cmbPartyType.setEnabled(false);
        cmbPartyType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPartyTypeItemStateChanged(evt);
            }
        });
        Tab1.add(cmbPartyType);
        cmbPartyType.setBounds(115, 110, 120, 24);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Party Type :");
        Tab1.add(jLabel11);
        jLabel11.setBounds(5, 110, 105, 15);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Main Code Type :");
        Tab1.add(jLabel20);
        jLabel20.setBounds(280, 115, 110, 15);

        txtOldPartyCode.setEditable(false);
        txtOldPartyCode.setFocusable(false);
        txtOldPartyCode.setName("PARTY_CODE"); // NOI18N
        txtOldPartyCode.setEnabled(false);
        Tab1.add(txtOldPartyCode);
        txtOldPartyCode.setBounds(590, 20, 120, 19);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Updation ID :");
        Tab1.add(jLabel37);
        jLabel37.setBounds(5, 10, 105, 15);

        txtAmendID.setName("SUPPLIER_CODE"); // NOI18N
        txtAmendID.setEnabled(false);
        Tab1.add(txtAmendID);
        txtAmendID.setBounds(115, 10, 120, 19);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(240, 10, 30, 16);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Date :");
        Tab1.add(jLabel44);
        jLabel44.setBounds(280, 13, 110, 15);

        txtAmendDate.setName("DUMMY_SUPPLIER_CODE"); // NOI18N
        txtAmendDate.setEnabled(false);
        Tab1.add(txtAmendDate);
        txtAmendDate.setBounds(395, 10, 150, 19);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Update Reason :");
        Tab1.add(jLabel45);
        jLabel45.setBounds(5, 35, 105, 15);

        txtAmendReason.setName("SUPP_NAME"); // NOI18N
        txtAmendReason.setEnabled(false);
        Tab1.add(txtAmendReason);
        txtAmendReason.setBounds(115, 35, 390, 19);

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Tab1.add(jButton2);
        jButton2.setBounds(514, 35, 31, 20);

        cmbMainCodeType.setEditable(true);
        cmbMainCodeType.setEnabled(false);
        cmbMainCodeType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMainCodeTypeItemStateChanged(evt);
            }
        });
        Tab1.add(cmbMainCodeType);
        cmbMainCodeType.setBounds(395, 110, 150, 24);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Charge Code :");
        Tab1.add(jLabel13);
        jLabel13.setBounds(5, 140, 105, 15);

        cmbChargeCode.setEditable(true);
        cmbChargeCode.setEnabled(false);
        cmbChargeCode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbChargeCodeItemStateChanged(evt);
            }
        });
        Tab1.add(cmbChargeCode);
        cmbChargeCode.setBounds(115, 140, 430, 24);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Charge Code II :");
        Tab1.add(jLabel25);
        jLabel25.setBounds(5, 170, 105, 15);

        cmbSecondChargeCode.setEditable(true);
        cmbSecondChargeCode.setEnabled(false);
        Tab1.add(cmbSecondChargeCode);
        cmbSecondChargeCode.setBounds(115, 170, 430, 24);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Credit Days :");
        Tab1.add(jLabel17);
        jLabel17.setBounds(5, 200, 105, 15);

        txtCreditDays.setName("FROM_DATE_REG"); // NOI18N
        txtCreditDays.setEnabled(false);
        Tab1.add(txtCreditDays);
        txtCreditDays.setBounds(115, 200, 120, 21);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Insurance Code :");
        Tab1.add(jLabel30);
        jLabel30.setBounds(550, 170, 110, 15);

        txtInsuranceCode.setName("FROM_DATE_REG"); // NOI18N
        txtInsuranceCode.setEnabled(false);
        Tab1.add(txtInsuranceCode);
        txtInsuranceCode.setBounds(670, 170, 70, 21);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Category :");
        Tab1.add(jLabel38);
        jLabel38.setBounds(280, 230, 110, 15);

        cmbCategory.setEditable(true);
        cmbCategory.setEnabled(false);
        Tab1.add(cmbCategory);
        cmbCategory.setBounds(395, 230, 150, 24);

        lblSeasonCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSeasonCode.setText("Season Code :");
        Tab1.add(lblSeasonCode);
        lblSeasonCode.setBounds(560, 110, 100, 20);

        txtSeasonCode.setEditable(false);
        txtSeasonCode.setEnabled(false);
        Tab1.add(txtSeasonCode);
        txtSeasonCode.setBounds(670, 110, 69, 19);

        lblRegDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRegDate.setText("Reg Date :");
        Tab1.add(lblRegDate);
        lblRegDate.setBounds(560, 140, 100, 20);

        txtRegDate.setEditable(false);
        txtRegDate.setEnabled(false);
        Tab1.add(txtRegDate);
        txtRegDate.setBounds(670, 140, 69, 19);

        chkDoNotAllowInterest.setText(" Do Not Allow Interest");
        Tab1.add(chkDoNotAllowInterest);
        chkDoNotAllowInterest.setBounds(570, 410, 180, 20);

        txtDispatchStation.setEnabled(false);
        txtDispatchStation.setName("CITY"); // NOI18N
        txtDispatchStation.setNextFocusableComponent(txtPIN);
        txtDispatchStation.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDispatchStationFocusLost(evt);
            }
        });
        Tab1.add(txtDispatchStation);
        txtDispatchStation.setBounds(395, 380, 150, 19);

        lblDispatchStation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDispatchStation.setText("Dispatch Station:");
        Tab1.add(lblDispatchStation);
        lblDispatchStation.setBounds(280, 380, 110, 15);

        txtCorrAdd.setToolTipText("Correspondence Address");
        txtCorrAdd.setEnabled(false);
        txtCorrAdd.setName("ADD2"); // NOI18N
        txtCorrAdd.setNextFocusableComponent(txtCity);
        Tab1.add(txtCorrAdd);
        txtCorrAdd.setBounds(115, 320, 430, 19);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Corre. Address :");
        jLabel8.setToolTipText("Correspondence Address");
        Tab1.add(jLabel8);
        jLabel8.setBounds(5, 320, 105, 15);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("State :");
        Tab1.add(jLabel43);
        jLabel43.setBounds(570, 351, 60, 15);

        cmbState.setEnabled(false);
        Tab1.add(cmbState);
        cmbState.setBounds(635, 347, 180, 24);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Country :");
        Tab1.add(jLabel46);
        jLabel46.setBounds(570, 381, 60, 15);

        cmbCountry.setEnabled(false);
        Tab1.add(cmbCountry);
        cmbCountry.setBounds(635, 377, 180, 24);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Extra Credit Days :");
        Tab1.add(jLabel47);
        jLabel47.setBounds(245, 200, 130, 15);

        txtExtraCreditDays.setName("FROM_DATE_REG"); // NOI18N
        txtExtraCreditDays.setEnabled(false);
        Tab1.add(txtExtraCreditDays);
        txtExtraCreditDays.setBounds(380, 200, 80, 21);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Grace Credit Days :");
        Tab1.add(jLabel48);
        jLabel48.setBounds(485, 200, 140, 15);

        txtGraceCreditDays.setName("FROM_DATE_REG"); // NOI18N
        txtGraceCreditDays.setEnabled(false);
        Tab1.add(txtGraceCreditDays);
        txtGraceCreditDays.setBounds(630, 200, 120, 21);

        jLabel57.setText("Distance In Km :");
        Tab1.add(jLabel57);
        jLabel57.setBounds(550, 310, 110, 15);

        txtDistanceKm.setEnabled(false);
        txtDistanceKm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDistanceKmFocusLost(evt);
            }
        });
        Tab1.add(txtDistanceKm);
        txtDistanceKm.setBounds(660, 310, 140, 19);

        txtEmail3.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtEmail3.setEnabled(false);
        txtEmail3.setName("EMAIL_ADD"); // NOI18N
        txtEmail3.setNextFocusableComponent(txtURL);
        Tab1.add(txtEmail3);
        txtEmail3.setBounds(480, 510, 210, 20);

        txtEmail2.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtEmail2.setEnabled(false);
        txtEmail2.setName("EMAIL_ADD"); // NOI18N
        txtEmail2.setNextFocusableComponent(txtURL);
        Tab1.add(txtEmail2);
        txtEmail2.setBounds(480, 480, 210, 20);

        jLabel63.setText("Contact Person :");
        jLabel63.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Tab1.add(jLabel63);
        jLabel63.setBounds(20, 430, 170, 20);

        jLabel64.setText("Designation :");
        jLabel64.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Tab1.add(jLabel64);
        jLabel64.setBounds(200, 430, 130, 20);

        jLabel65.setText("Mobile No :");
        jLabel65.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Tab1.add(jLabel65);
        jLabel65.setBounds(340, 430, 130, 20);

        jLabel66.setText("E-Mail ID :");
        jLabel66.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Tab1.add(jLabel66);
        jLabel66.setBounds(480, 430, 110, 20);

        txtMobile3.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtMobile3.setEnabled(false);
        txtMobile3.setName("MOBILE_NO_3"); // NOI18N
        Tab1.add(txtMobile3);
        txtMobile3.setBounds(340, 510, 140, 20);

        txtMobile2.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtMobile2.setEnabled(false);
        txtMobile2.setName("MOBILE_NO_2"); // NOI18N
        Tab1.add(txtMobile2);
        txtMobile2.setBounds(340, 480, 140, 20);

        txtDesignation.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtDesignation.setEnabled(false);
        txtDesignation.setName("DESIGNATION_1"); // NOI18N
        Tab1.add(txtDesignation);
        txtDesignation.setBounds(200, 450, 140, 20);

        txtDesignation2.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtDesignation2.setEnabled(false);
        txtDesignation2.setName("DESIGNATION_2"); // NOI18N
        Tab1.add(txtDesignation2);
        txtDesignation2.setBounds(200, 480, 140, 20);

        txtDesignation3.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtDesignation3.setEnabled(false);
        txtDesignation3.setName("DESIGNATION_3"); // NOI18N
        Tab1.add(txtDesignation3);
        txtDesignation3.setBounds(200, 510, 140, 20);

        txtContactPerson3.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtContactPerson3.setEnabled(false);
        txtContactPerson3.setName("CONTACT_PERSON_3"); // NOI18N
        Tab1.add(txtContactPerson3);
        txtContactPerson3.setBounds(20, 510, 180, 20);

        txtContactPerson2.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txtContactPerson2.setEnabled(false);
        txtContactPerson2.setName("CONTACT_PERSON_2"); // NOI18N
        Tab1.add(txtContactPerson2);
        txtContactPerson2.setBounds(20, 480, 180, 20);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel67.setText("3 ");
        Tab1.add(jLabel67);
        jLabel67.setBounds(0, 510, 20, 20);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel68.setText("2 ");
        Tab1.add(jLabel68);
        jLabel68.setBounds(0, 480, 20, 20);

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel69.setText("1 ");
        Tab1.add(jLabel69);
        jLabel69.setBounds(0, 450, 20, 20);

        jTabbedPane1.addTab("Header", null, Tab1, "Party Master Header");

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.setToolTipText("");
        Tab3.setLayout(null);

        cmdNext2.setText("Next >>");
        cmdNext2.setNextFocusableComponent(cmdNext4);
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });
        Tab3.add(cmdNext2);
        cmdNext2.setBounds(670, 450, 90, 25);

        cmdNext4.setText("<< Back");
        cmdNext4.setNextFocusableComponent(txtBankID);
        cmdNext4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext4ActionPerformed(evt);
            }
        });
        cmdNext4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdNext4KeyPressed(evt);
            }
        });
        Tab3.add(cmdNext4);
        cmdNext4.setBounds(570, 450, 90, 25);

        jLabel26.setText("No");
        Tab3.add(jLabel26);
        jLabel26.setBounds(100, 150, 48, 15);

        jLabel23.setText("Date");
        Tab3.add(jLabel23);
        jLabel23.setBounds(310, 150, 40, 15);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("CST");
        Tab3.add(jLabel24);
        jLabel24.setBounds(40, 170, 32, 15);

        txtCST.setEnabled(false);
        txtCST.setName("CST_NO"); // NOI18N
        txtCST.setNextFocusableComponent(txtCSTDate);
        Tab3.add(txtCST);
        txtCST.setBounds(90, 170, 197, 21);

        txtCSTDate.setEnabled(false);
        txtCSTDate.setName("CST_DATE"); // NOI18N
        txtCSTDate.setNextFocusableComponent(txtTinNo);
        Tab3.add(txtCSTDate);
        txtCSTDate.setBounds(290, 170, 90, 21);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("TIN NO.");
        Tab3.add(jLabel27);
        jLabel27.setBounds(19, 200, 60, 20);

        txtTinNo.setEnabled(false);
        txtTinNo.setName("GST_NO"); // NOI18N
        txtTinNo.setNextFocusableComponent(txtTinDate);
        Tab3.add(txtTinNo);
        txtTinNo.setBounds(90, 200, 197, 21);

        txtTinDate.setEnabled(false);
        txtTinDate.setName("GST_DATE"); // NOI18N
        txtTinDate.setNextFocusableComponent(txtECC);
        Tab3.add(txtTinDate);
        txtTinDate.setBounds(290, 200, 90, 21);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("ECC");
        Tab3.add(jLabel28);
        jLabel28.setBounds(40, 230, 32, 15);

        txtECC.setEnabled(false);
        txtECC.setName("ECC_NO"); // NOI18N
        txtECC.setNextFocusableComponent(txtECCDate);
        Tab3.add(txtECC);
        txtECC.setBounds(90, 230, 197, 21);

        txtECCDate.setEnabled(false);
        txtECCDate.setName("ECC_DATE"); // NOI18N
        Tab3.add(txtECCDate);
        txtECCDate.setBounds(290, 230, 90, 21);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab3.add(jPanel1);
        jPanel1.setBounds(10, 300, 750, 7);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Critical Amount");
        Tab3.add(jLabel39);
        jLabel39.setBounds(39, 320, 107, 15);

        txtAmountLimit.setEnabled(false);
        txtAmountLimit.setName("FROM_DATE_REG"); // NOI18N
        Tab3.add(txtAmountLimit);
        txtAmountLimit.setBounds(150, 320, 104, 21);

        lblDocThrough.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocThrough.setText("Document Through");
        Tab3.add(lblDocThrough);
        lblDocThrough.setBounds(10, 430, 130, 15);

        txtDocThrough.setName("FROM_DATE_REG"); // NOI18N
        txtDocThrough.setNextFocusableComponent(cmdNext2);
        txtDocThrough.setEnabled(false);
        Tab3.add(txtDocThrough);
        txtDocThrough.setBounds(150, 430, 610, 21);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab3.add(jPanel4);
        jPanel4.setBounds(10, 140, 750, 7);

        lblBankID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankID.setText("Bank");
        Tab3.add(lblBankID);
        lblBankID.setBounds(40, 10, 70, 15);

        txtBankID.setName("BANK_ID"); // NOI18N
        txtBankID.setNextFocusableComponent(txtBankAdd);
        txtBankID.setEnabled(false);
        txtBankID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBankIDFocusLost(evt);
            }
        });
        txtBankID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBankIDKeyPressed(evt);
            }
        });
        Tab3.add(txtBankID);
        txtBankID.setBounds(130, 10, 58, 19);

        txtBankName.setEditable(false);
        txtBankName.setName("BANK_NAME"); // NOI18N
        txtBankName.setEnabled(false);
        Tab3.add(txtBankName);
        txtBankName.setBounds(190, 10, 350, 20);

        lblBankID1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankID1.setText("Address");
        Tab3.add(lblBankID1);
        lblBankID1.setBounds(40, 40, 70, 20);

        txtBankAdd.setName("ADD1"); // NOI18N
        txtBankAdd.setNextFocusableComponent(txtBankCity);
        txtBankAdd.setEnabled(false);
        Tab3.add(txtBankAdd);
        txtBankAdd.setBounds(130, 40, 410, 19);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("City");
        Tab3.add(jLabel18);
        jLabel18.setBounds(570, 40, 50, 15);

        txtBankCity.setName("CITY"); // NOI18N
        txtBankCity.setNextFocusableComponent(txtOtherBankID);
        txtBankCity.setEnabled(false);
        Tab3.add(txtBankCity);
        txtBankCity.setBounds(630, 40, 140, 19);

        txtOtherBankID.setName("BANK_ID"); // NOI18N
        txtOtherBankID.setNextFocusableComponent(txtOtherBankAdd);
        txtOtherBankID.setEnabled(false);
        txtOtherBankID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOtherBankIDFocusLost(evt);
            }
        });
        txtOtherBankID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOtherBankIDKeyPressed(evt);
            }
        });
        Tab3.add(txtOtherBankID);
        txtOtherBankID.setBounds(130, 70, 58, 19);

        lblBankID3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankID3.setText("Second Bank");
        Tab3.add(lblBankID3);
        lblBankID3.setBounds(10, 70, 100, 15);

        txtOtherBankName.setEditable(false);
        txtOtherBankName.setName("BANK_NAME"); // NOI18N
        txtOtherBankName.setEnabled(false);
        Tab3.add(txtOtherBankName);
        txtOtherBankName.setBounds(190, 70, 350, 20);

        lblBankID4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankID4.setText("Second Address");
        Tab3.add(lblBankID4);
        lblBankID4.setBounds(0, 100, 110, 20);

        txtOtherBankAdd.setName("ADD1"); // NOI18N
        txtOtherBankAdd.setNextFocusableComponent(txtOtherBankCity);
        txtOtherBankAdd.setEnabled(false);
        Tab3.add(txtOtherBankAdd);
        txtOtherBankAdd.setBounds(130, 100, 410, 19);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Second City");
        Tab3.add(jLabel21);
        jLabel21.setBounds(540, 100, 80, 15);

        txtOtherBankCity.setName("CITY"); // NOI18N
        txtOtherBankCity.setNextFocusableComponent(txtCST);
        txtOtherBankCity.setEnabled(false);
        Tab3.add(txtOtherBankCity);
        txtOtherBankCity.setBounds(630, 100, 140, 19);

        lblBankID2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankID2.setText("Transporter");
        Tab3.add(lblBankID2);
        lblBankID2.setBounds(40, 350, 105, 15);

        txtTranID.setEnabled(false);
        txtTranID.setName("BANK_ID"); // NOI18N
        txtTranID.setNextFocusableComponent(cmdNext1);
        txtTranID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTranIDFocusLost(evt);
            }
        });
        txtTranID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTranIDKeyPressed(evt);
            }
        });
        Tab3.add(txtTranID);
        txtTranID.setBounds(150, 350, 80, 19);

        txtTranName.setEditable(false);
        txtTranName.setName("BANK_NAME"); // NOI18N
        txtTranName.setEnabled(false);
        Tab3.add(txtTranName);
        txtTranName.setBounds(240, 350, 330, 20);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("PAN NO");
        Tab3.add(jLabel40);
        jLabel40.setBounds(10, 260, 70, 15);

        txtPANNo.setEnabled(false);
        txtPANNo.setName("ECC_NO"); // NOI18N
        Tab3.add(txtPANNo);
        txtPANNo.setBounds(90, 260, 197, 21);

        txtPANDate.setEnabled(false);
        txtPANDate.setName("ECC_DATE"); // NOI18N
        txtPANDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPANDateFocusLost(evt);
            }
        });
        Tab3.add(txtPANDate);
        txtPANDate.setBounds(290, 260, 90, 21);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Remarks");
        Tab3.add(jLabel41);
        jLabel41.setBounds(20, 380, 90, 15);

        txtRemarks.setEnabled(false);
        txtRemarks.setName("FROM_DATE_REG"); // NOI18N
        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRemarksFocusLost(evt);
            }
        });
        Tab3.add(txtRemarks);
        txtRemarks.setBounds(150, 380, 540, 21);

        cmdRemarksBig.setText("...");
        Tab3.add(cmdRemarksBig);
        cmdRemarksBig.setBounds(700, 380, 33, 21);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Delay Interest Percentage");
        Tab3.add(jLabel42);
        jLabel42.setBounds(290, 320, 190, 15);

        txtInterestPer.setName("FROM_DATE_REG"); // NOI18N
        txtInterestPer.setEnabled(false);
        Tab3.add(txtInterestPer);
        txtInterestPer.setBounds(490, 320, 80, 21);

        txtGSTN.setEnabled(false);
        txtGSTN.setName("CST_NO"); // NOI18N
        txtGSTN.setNextFocusableComponent(txtCSTDate);
        txtGSTN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGSTNFocusLost(evt);
            }
        });
        Tab3.add(txtGSTN);
        txtGSTN.setBounds(470, 170, 197, 21);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("GSTIN NO: ");
        Tab3.add(jLabel56);
        jLabel56.setBounds(382, 170, 80, 20);

        txtGSTDate.setEnabled(false);
        txtGSTDate.setName("CST_DATE"); // NOI18N
        txtGSTDate.setNextFocusableComponent(txtTinNo);
        txtGSTDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGSTDateFocusLost(evt);
            }
        });
        Tab3.add(txtGSTDate);
        txtGSTDate.setBounds(680, 170, 90, 21);

        chkCriticalUnCheck.setText("Critical Limit UnCheck");
        Tab3.add(chkCriticalUnCheck);
        chkCriticalUnCheck.setBounds(150, 490, 280, 23);

        jLabel58.setText("No");
        Tab3.add(jLabel58);
        jLabel58.setBounds(510, 150, 48, 15);

        jLabel59.setText("Date");
        Tab3.add(jLabel59);
        jLabel59.setBounds(720, 150, 40, 15);

        lblTransID.setText("Delivery Mode : ");
        lblTransID.setToolTipText("Transporter");
        Tab3.add(lblTransID);
        lblTransID.setBounds(30, 460, 120, 20);

        cmbDeliveryMode.setEditable(true);
        cmbDeliveryMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Delivery Mode", "Godown Delivery", "Door Delivery" }));
        cmbDeliveryMode.setEnabled(false);
        Tab3.add(cmbDeliveryMode);
        cmbDeliveryMode.setBounds(150, 460, 230, 24);

        jTabbedPane1.addTab("Other Information", null, Tab3, "");

        jPanel2.setLayout(null);

        lblSubCode9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSubCode9.setText("Main Account Codes");
        lblSubCode9.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jPanel2.add(lblSubCode9);
        lblSubCode9.setBounds(20, 20, 140, 15);

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.add(jPanel8);
        jPanel8.setBounds(170, 25, 600, 6);

        TableMainCode.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(TableMainCode);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(10, 50, 520, 230);

        cmdSelectAll.setMnemonic('A');
        cmdSelectAll.setText("Select All");
        cmdSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectAllActionPerformed(evt);
            }
        });
        jPanel2.add(cmdSelectAll);
        cmdSelectAll.setBounds(550, 60, 100, 25);

        cmdClearAll.setMnemonic('L');
        cmdClearAll.setText("Clear All");
        cmdClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearAllActionPerformed(evt);
            }
        });
        jPanel2.add(cmdClearAll);
        cmdClearAll.setBounds(550, 100, 100, 25);

        cmdBack5.setText("<< Back");
        cmdBack5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack5ActionPerformed(evt);
            }
        });
        jPanel2.add(cmdBack5);
        cmdBack5.setBounds(590, 410, 90, 25);

        cmdNext6.setText("Next >>");
        cmdNext6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext6ActionPerformed(evt);
            }
        });
        jPanel2.add(cmdNext6);
        cmdNext6.setBounds(680, 410, 90, 25);

        jTabbedPane1.addTab("Finance Information", jPanel2);

        jPanel9.setLayout(null);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(234, 12, 12));
        jLabel1.setText("Only For Felt Sales Purpose");
        jPanel9.add(jLabel1);
        jLabel1.setBounds(20, 20, 240, 17);

        jLabel3.setText("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        jPanel9.add(jLabel3);
        jLabel3.setBounds(20, 40, 750, 15);

        jLabel49.setText("Zone / Area Code");
        jPanel9.add(jLabel49);
        jLabel49.setBounds(40, 70, 160, 15);

        jLabel50.setText("Market Segment");
        jPanel9.add(jLabel50);
        jLabel50.setBounds(30, 130, 140, 15);

        jLabel51.setText("Client Category");
        jPanel9.add(jLabel51);
        jLabel51.setBounds(30, 160, 140, 15);

        jLabel52.setText("Designer Incharge");
        jPanel9.add(jLabel52);
        jLabel52.setBounds(30, 220, 140, 15);

        cmbZone.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel9.add(cmbZone);
        cmbZone.setBounds(180, 60, 230, 24);

        jLabel53.setText("Area Incharge");
        jPanel9.add(jLabel53);
        jLabel53.setBounds(40, 100, 110, 15);

        jLabel54.setText("Product Segment");
        jPanel9.add(jLabel54);
        jLabel54.setBounds(30, 190, 120, 15);

        txtDesignerIncharge.setEnabled(false);
        txtDesignerIncharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDesignerInchargeActionPerformed(evt);
            }
        });
        jPanel9.add(txtDesignerIncharge);
        txtDesignerIncharge.setBounds(180, 210, 470, 19);

        jLabel55.setText("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        jPanel9.add(jLabel55);
        jLabel55.setBounds(20, 260, 760, 15);

        txtMarketSegment.setEnabled(false);
        txtMarketSegment = new JTextFieldHint(new JTextField(),"Search by F1");
        txtMarketSegment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMarketSegmentKeyPressed(evt);
            }
        });
        jPanel9.add(txtMarketSegment);
        txtMarketSegment.setBounds(180, 120, 230, 19);

        txtClientCategory.setEnabled(false);
        txtClientCategory = new JTextFieldHint(new JTextField(),"Search by F1");
        txtClientCategory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClientCategoryFocusLost(evt);
            }
        });
        txtClientCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClientCategoryKeyPressed(evt);
            }
        });
        jPanel9.add(txtClientCategory);
        txtClientCategory.setBounds(180, 150, 240, 19);

        txtProductSegment.setEnabled(false);
        txtProductSegment = new JTextFieldHint(new JTextField(),"Search by F1");
        txtProductSegment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductSegmentKeyPressed(evt);
            }
        });
        jPanel9.add(txtProductSegment);
        txtProductSegment.setBounds(180, 180, 210, 19);

        txtAreaInchargeCode.setEnabled(false);
        txtAreaInchargeCode = new JTextFieldHint(new JTextField(),"Search by F1");
        txtAreaInchargeCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaInchargeCodeKeyPressed(evt);
            }
        });
        jPanel9.add(txtAreaInchargeCode);
        txtAreaInchargeCode.setBounds(180, 90, 80, 19);

        txtAreaInchargeName.setEnabled(false);
        jPanel9.add(txtAreaInchargeName);
        txtAreaInchargeName.setBounds(260, 90, 380, 19);

        lblClientCategory.setText(".....");
        jPanel9.add(lblClientCategory);
        lblClientCategory.setBounds(480, 150, 30, 20);

        cmdBack6.setText("<< Back");
        cmdBack6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack6ActionPerformed(evt);
            }
        });
        jPanel9.add(cmdBack6);
        cmdBack6.setBounds(590, 410, 90, 25);

        cmdNext7.setText("Next >>");
        cmdNext7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext7ActionPerformed(evt);
            }
        });
        jPanel9.add(cmdNext7);
        cmdNext7.setBounds(680, 410, 90, 25);

        cmbStockTagging.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbStockTagging.setEnabled(false);
        jPanel9.add(cmbStockTagging);
        cmbStockTagging.setBounds(20, 290, 410, 24);

        chkPONoRequired.setText("PO No Required");
        chkPONoRequired.setEnabled(false);
        jPanel9.add(chkPONoRequired);
        chkPONoRequired.setBounds(20, 330, 290, 23);

        chkKeyClientInd.setText("Key Client");
        chkKeyClientInd.setEnabled(false);
        jPanel9.add(chkKeyClientInd);
        chkKeyClientInd.setBounds(20, 360, 290, 23);

        jTabbedPane1.addTab("Felt Sales Extra Info", jPanel9);

        jPanel7.setLayout(null);

        jLabel22.setText("Updation History List");
        jPanel7.add(jLabel22);
        jLabel22.setBounds(12, 23, 156, 15);

        TableU.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TableU);

        jPanel7.add(jScrollPane1);
        jScrollPane1.setBounds(12, 45, 510, 149);

        cmdShowRemarksU.setText("Show Remarks");
        cmdShowRemarksU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksUActionPerformed(evt);
            }
        });
        jPanel7.add(cmdShowRemarksU);
        cmdShowRemarksU.setBounds(528, 48, 132, 24);

        cmdBack7.setText("<< Back");
        cmdBack7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack7ActionPerformed(evt);
            }
        });
        jPanel7.add(cmdBack7);
        cmdBack7.setBounds(590, 410, 90, 25);

        cmdNext8.setText("Next >>");
        cmdNext8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext8ActionPerformed(evt);
            }
        });
        jPanel7.add(cmdNext8);
        cmdNext8.setBounds(680, 410, 90, 25);

        jTabbedPane1.addTab("History", jPanel7);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setToolTipText("");
        jPanel3.setLayout(null);

        jLabel31.setText("Hierarchy ");
        jPanel3.add(jLabel31);
        jLabel31.setBounds(29, 13, 66, 15);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbHierarchy);
        cmbHierarchy.setBounds(99, 13, 270, 24);

        jLabel32.setText("From");
        jPanel3.add(jLabel32);
        jLabel32.setBounds(49, 43, 40, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFrom);
        txtFrom.setBounds(99, 43, 270, 22);

        jLabel35.setText("Remarks");
        jPanel3.add(jLabel35);
        jLabel35.setBounds(28, 76, 62, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFromRemarks);
        txtFromRemarks.setBounds(99, 73, 518, 22);

        jLabel36.setText("Your Action  ");
        jPanel3.add(jLabel36);
        jLabel36.setBounds(14, 116, 92, 15);

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

        jPanel3.add(jPanel6);
        jPanel6.setBounds(99, 113, 182, 100);

        jLabel33.setText("Send To");
        jPanel3.add(jLabel33);
        jLabel33.setBounds(34, 226, 60, 15);

        cmbSendTo.setEnabled(false);
        jPanel3.add(cmbSendTo);
        cmbSendTo.setBounds(99, 223, 270, 24);

        jLabel34.setText("Remarks");
        jPanel3.add(jLabel34);
        jLabel34.setBounds(36, 262, 60, 15);

        txtToRemarks.setEnabled(false);
        jPanel3.add(txtToRemarks);
        txtToRemarks.setBounds(99, 259, 516, 22);

        cmdNext3.setLabel("Previous <<");
        cmdNext3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext3ActionPerformed(evt);
            }
        });
        jPanel3.add(cmdNext3);
        cmdNext3.setBounds(640, 340, 120, 25);

        btnSendMail.setEnabled(false);
        btnSendMail.setLabel("Send Email Notification");
        btnSendMail.setMargin(new java.awt.Insets(-4, -4, -4, -4));
        btnSendMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMailActionPerformed(evt);
            }
        });
        jPanel3.add(btnSendMail);
        btnSendMail.setBounds(570, 20, 190, 13);

        jTabbedPane1.addTab("Approval", null, jPanel3, "");

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(null);

        jLabel60.setText("Document Approval Status");
        jPanel5.add(jLabel60);
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

        jPanel5.add(jScrollPane2);
        jScrollPane2.setBounds(12, 40, 694, 144);

        jLabel19.setText("Document Update History");
        jPanel5.add(jLabel19);
        jLabel19.setBounds(13, 191, 182, 15);

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

        jPanel5.add(jScrollPane6);
        jScrollPane6.setBounds(13, 207, 540, 143);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        jPanel5.add(cmdViewHistory);
        cmdViewHistory.setBounds(571, 206, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        jPanel5.add(cmdNormalView);
        cmdNormalView.setBounds(571, 238, 132, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        jPanel5.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(571, 272, 132, 24);

        txtAuditRemarks.setEnabled(false);
        jPanel5.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(575, 304, 129, 19);

        jTabbedPane1.addTab("Status", jPanel5);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(5, 70, 830, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void txtGSTNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTNFocusLost
        if (txtGSTN.getText().length() != 15) {
            JOptionPane.showMessageDialog(null, "GSTIN number must be 15 character");
            txtGSTN.requestFocus();
        }
        txtGSTDate.requestFocus();    // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTNFocusLost

    private void cmdShowOriginalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowOriginalActionPerformed
        // TODO add your handling code here:
        try {
            String PartyCode = txtPartyCode.getText();
            String MainCode = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
            AppletFrame aFrame = new AppletFrame("Sales Party Master");
            aFrame.startAppletEx("EITLERP.frmSalesParty", "Sales Party Master");
            frmSalesParty ObjItem = (frmSalesParty) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PartyCode, MainCode);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmdShowOriginalActionPerformed

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        String MainAccountCode = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);

        if (!txtPartyCode.getText().trim().equals("")) { //F1 Key pressed
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MAIN_ACCOUNT_CODE=" + MainAccountCode + "  AND PARTY_CODE=" + txtPartyCode.getText().trim() + " ");
            txtName.setText(PartyCode);
        } else {
            txtName.setText("");
        }
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:

        String MainAccountCode = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
        //System.out.println(MainAccountCode);

        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

//            aList.SQL="SELECT PARTY_CODE, PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAIN_ACCOUNT_CODE="+MainAccountCode+"  ORDER BY PARTY_NAME";
            aList.SQL = "SELECT PARTY_CODE, PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MAIN_ACCOUNT_CODE=" + MainAccountCode + " AND PARTY_CLOSE_IND!=1 ORDER BY PARTY_NAME";
            aList.ReturnCol = 1;
            aList.SecondCol = 2;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtPartyCode.setText(aList.ReturnVal);
                txtName.setText(aList.SecondVal);
                //                txtBankID.setText(aList.ReturnVal);
                //                txtBankName.setText(clsBank.getBankName((long)EITLERPGLOBAL.gCompanyID, Long.parseLong(aList.ReturnVal)));
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void txtPartyCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartyCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCodeActionPerformed

    private void cmdClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearAllActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < TableMainCode.getRowCount(); i++) {
            DataModelMainCode.setValueAt(Boolean.valueOf(false), i, 0);
        }
    }//GEN-LAST:event_cmdClearAllActionPerformed

    private void cmdSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectAllActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < TableMainCode.getRowCount(); i++) {
            DataModelMainCode.setValueAt(Boolean.valueOf(true), i, 0);
        }
    }//GEN-LAST:event_cmdSelectAllActionPerformed

    private void cmbChargeCodeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbChargeCodeItemStateChanged
        // TODO add your handling code here:
        String ChargeCode = EITLERPGLOBAL.getCombostrCode(cmbChargeCode);
        String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
        if ((ChargeCode.trim().equals("04")) && (type.trim().equals("210010"))) {
            ChargeCode = "F04";
        } else if ((ChargeCode.trim().equals("04")) && (!type.trim().equals("210010"))) {
            ChargeCode = "S04";
        }
        String str = "SELECT INVOICE_TYPE_DESC FROM D_SAL_POLICY_INVOICE_TYPE WHERE INVOICE_TYPE_ID='" + ChargeCode + "' ";
        txtDocThrough.setText(data.getStringValueFromDB(str));
    }//GEN-LAST:event_cmbChargeCodeItemStateChanged
    //    private void DisplayAgentAplha() {
    //        String Partycode = txtPartyCode.getText().trim().substring(0,2);
    //        String MainCode = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
    //        String AgentAlpha = data.getStringValueFromDB("SELECT AREA_ID FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND  MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE='"+Partycode+"0000' ");
    //        txtAreaID.setText(AgentAlpha);
    //    }
    private void cmbMainCodeTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMainCodeTypeItemStateChanged
        // TODO add your handling code here:
        //DisplayAgentAplha();
        String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);

        if (type.trim().equals("210010")) {
            ChargeCodeCombos(" AND INVOICE_TYPE_ID NOT IN ('S04') ");
            GenerateAreaCombo();
        } else {
            ChargeCodeCombos(" AND INVOICE_TYPE_ID NOT IN ('F04') ");
            GenerateZoneCombo();
        }

        String ChargeCode = EITLERPGLOBAL.getCombostrCode(cmbChargeCode);
        type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
        if ((ChargeCode.trim().equals("04")) && (type.trim().equals("210010"))) {
            ChargeCode = "F04";
        } else if ((ChargeCode.trim().equals("04")) && (!type.trim().equals("210010"))) {
            ChargeCode = "S04";
        }
        String str = "SELECT INVOICE_TYPE_DESC FROM D_SAL_POLICY_INVOICE_TYPE WHERE INVOICE_TYPE_ID='" + ChargeCode + "' ";
        txtDocThrough.setText(data.getStringValueFromDB(str));

    }//GEN-LAST:event_cmbMainCodeTypeItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtAmendReason;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cmdShowRemarksUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksUActionPerformed
        // TODO add your handling code here:
        if (TableU.getRowCount() > 0 && TableU.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableU.getValueAt(TableU.getSelectedRow(), 2));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }
    }//GEN-LAST:event_cmdShowRemarksUActionPerformed

    private void txtTranIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTranIDFocusLost
        // TODO add your handling code here:
        if (!txtTranID.getText().trim().equals("")) {
            txtTranName.setText(clsSales_Party.getTransporterName((long) EITLERPGLOBAL.gCompanyID, Long.parseLong(txtTranID.getText().trim())));
        }
        txtRemarks.requestFocus();
    }//GEN-LAST:event_txtTranIDFocusLost

    private void txtTranIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTranIDKeyPressed
        // TODO add your handling code here:
        //=========== Transporter List ===============
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT TRANSPORTER_ID,TRANSPORTER_NAME FROM D_SAL_TRANSPORTER_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ORDER BY TRANSPORTER_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtTranID.setText(aList.ReturnVal);
                txtTranName.setText(clsSales_Party.getTransporterName((long) EITLERPGLOBAL.gCompanyID, Long.parseLong(aList.ReturnVal)));
            }
        }
    }//GEN-LAST:event_txtTranIDKeyPressed

    private void txtOtherBankIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOtherBankIDFocusLost
        // TODO add your handling code here:
        if (!txtOtherBankID.getText().trim().equals("")) {
            txtOtherBankName.setText(clsBank.getBankName(EITLERPGLOBAL.gCompanyID, Long.parseLong(txtOtherBankID.getText())));
        }
    }//GEN-LAST:event_txtOtherBankIDFocusLost

    private void txtOtherBankIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOtherBankIDKeyPressed
        // TODO add your handling code here:
        //=========== BANK List ===============
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT BANK_ID,BANK_NAME FROM D_COM_BANK_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ORDER BY BANK_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtOtherBankID.setText(aList.ReturnVal);
                txtOtherBankName.setText(clsBank.getBankName((long) EITLERPGLOBAL.gCompanyID, Long.parseLong(aList.ReturnVal)));
            }
        }
    }//GEN-LAST:event_txtOtherBankIDKeyPressed

    private void cmdNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext2ActionPerformed

    private void cmdNext4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext4ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_cmdNext4ActionPerformed

    private void cmdNext4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdNext4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdNext4KeyPressed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if (TableHS.getRowCount() > 0 && TableHS.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableHS.getValueAt(TableHS.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }

    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        // TODO add your handling code here:
        //String PartyID=txtPartyCode.getText().trim();

        SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID, clsSalesPartyAmend.ModuleID, txtAmendID.getText().trim())) {
                cmbSendTo.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }

        if (cmbSendTo.getItemCount() <= 0) {
            GenerateFromCombo();
        }

    }//GEN-LAST:event_OpgApproveMouseClicked

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(true);
        OpgHold.setSelected(false);

        GenerateRejectedUserCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void cmbPartyTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPartyTypeItemStateChanged
                                            }//GEN-LAST:event_cmbPartyTypeItemStateChanged

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        // TODO add your handling code here:
        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID);
        MoveFirst();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo = Integer.toString((int) ObjSalesParty.getAttribute("AMEND_ID").getVal());
        ObjSalesParty.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

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

        //Set Default Send to User
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

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
        // TODO add your handling code here:
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
        ObjSalesParty.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void txtBankIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBankIDKeyPressed
        // TODO add your handling code here:
        //=========== BANK List ===============
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT BANK_ID,BANK_NAME FROM D_COM_BANK_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ORDER BY BANK_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtBankID.setText(aList.ReturnVal);
                txtBankName.setText(clsBank.getBankName((long) EITLERPGLOBAL.gCompanyID, Long.parseLong(aList.ReturnVal)));
            }
        }
    }//GEN-LAST:event_txtBankIDKeyPressed

    private void txtBankIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankIDFocusLost
        // TODO add your handling code here:
        if (!txtBankID.getText().trim().equals("")) {
            txtBankName.setText(clsBank.getBankName(EITLERPGLOBAL.gCompanyID, Long.parseLong(txtBankID.getText())));
        }
    }//GEN-LAST:event_txtBankIDFocusLost

    private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext1ActionPerformed

    private void cmdNext3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext3ActionPerformed

    private void cmdNext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdNext1KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmdNext1KeyPressed

    private void txtDesignerInchargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDesignerInchargeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDesignerInchargeActionPerformed

    private void txtMarketSegmentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarketSegmentKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'MARKET_SEGMENT' AND COMPANY_ID =2 ORDER BY PARA_CODE";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtMarketSegment.setText(aList.ReturnVal);
                // txtTranName.setText(clsSales_Party.getTransporterName((long)EITLERPGLOBAL.gCompanyID, Long.parseLong(aList.ReturnVal)));
            }
            if (txtMarketSegment.getText().trim().startsWith("ROO")) {
                txtProductSegment.setText("");
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtMarketSegmentKeyPressed

    private void txtClientCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClientCategoryKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_DESC,PARA_EXT1 FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'CLIENT_CATEGORY' AND COMPANY_ID =2 ORDER BY PARA_CODE";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtClientCategory.setText(aList.ReturnVal);
                //lblClientCategory.setText(clsSales_Party.getParaext1(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
                lblClientCategory.setText(data.getStringValueFromDB("SELECT PARA_EXT1 FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_DESC='" + aList.ReturnVal + "' ORDER BY  PARA_CODE"));

            }
        }
    }//GEN-LAST:event_txtClientCategoryKeyPressed

    private void txtProductSegmentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductSegmentKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            if (txtMarketSegment.getText().trim().startsWith("PMC")) {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'PRODUCT_SEGMENT' AND COMPANY_ID =2 ORDER BY PARA_CODE";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;

                if (aList.ShowLOV()) {
                    txtProductSegment.setText(aList.ReturnVal);
                    // txtTranName.setText(clsSales_Party.getTransporterName((long)EITLERPGLOBAL.gCompanyID, Long.parseLong(aList.ReturnVal)));
                }
            } else {
                //JOptionPane.showMessageDialog(null, "null");
                txtProductSegment.setText("");
            }
        }
    }//GEN-LAST:event_txtProductSegmentKeyPressed

    private void txtAreaInchargeCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaInchargeCodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            if (cmbZone.getSelectedItem().toString().equals("OTHER")) {
                aList.SQL = "SELECT INCHARGE_CD, INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD IN (1,2,3,8) ";
            } else {
                aList.SQL = "SELECT INCHARGE_CD, INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD NOT IN (1,2,3,8) ";
            }
//            aList.SQL="SELECT INCHARGE_CD, INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtAreaInchargeCode.setText(aList.ReturnVal);
                txtAreaInchargeName.setText(clsSales_Party.getFeltInchargeName(Long.parseLong(aList.ReturnVal)));
            }
        }
    }//GEN-LAST:event_txtAreaInchargeCodeKeyPressed

    private void txtClientCategoryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClientCategoryFocusLost
        try {
//            if (!txtClientCategory.getText().equals("")) {
//                String strSQL = "";
//                String codeId = data.getStringValueFromDB("SELECT PARA_EXT1 FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='"+txtClientCategory.getText()+"' ORDER BY  PARA_CODE");
//                lblClientCategory.setText(codeId);
////                ResultSet rsTmp;
////                strSQL += "SELECT PARA_EXT1 FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='"+txtClientCategory.getText()+"' ORDER BY  PARA_CODE";
////                rsTmp = data.getResult(strSQL);
////                rsTmp.first();
////                lblClientCategory.setText(rsTmp.getString("PARA_EXT1"));
//                
//            }

        } catch (Exception e) {

        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtClientCategoryFocusLost

    private void cmdNext6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext6ActionPerformed
        jTabbedPane1.setSelectedIndex(3);        // TODO add your handling code here:
    }//GEN-LAST:event_cmdNext6ActionPerformed

    private void cmdNext7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext7ActionPerformed
        jTabbedPane1.setSelectedIndex(4);        // TODO add your handling code here:       // TODO add your handling code here:
    }//GEN-LAST:event_cmdNext7ActionPerformed

    private void cmdNext8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext8ActionPerformed
        jTabbedPane1.setSelectedIndex(5);        // TODO add your handling code here:       // TODO add your handling code here:
    }//GEN-LAST:event_cmdNext8ActionPerformed

    private void cmdBack5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack5ActionPerformed
        jTabbedPane1.setSelectedIndex(1);        // TODO add your handling code here:
    }//GEN-LAST:event_cmdBack5ActionPerformed

    private void cmdBack6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack6ActionPerformed
        jTabbedPane1.setSelectedIndex(2);        // TODO add your handling code here:
    }//GEN-LAST:event_cmdBack6ActionPerformed

    private void cmdBack7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack7ActionPerformed
        jTabbedPane1.setSelectedIndex(3);        // TODO add your handling code here:
    }//GEN-LAST:event_cmdBack7ActionPerformed

    private void txtStateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateFocusLost
        txtDispatchStation.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtStateFocusLost

    private void txtDispatchStationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDispatchStationFocusLost
        txtPhone.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtDispatchStationFocusLost

    private void txtContactPersonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContactPersonFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactPersonFocusLost

    private void txtAdd2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdd2FocusLost
        txtDistanceKm.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdd2FocusLost

    private void txtDistanceKmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDistanceKmFocusLost
        txtCity.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtDistanceKmFocusLost

    private void txtPANDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPANDateFocusLost
        txtGSTN.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtPANDateFocusLost

    private void txtGSTDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTDateFocusLost
        txtAmountLimit.requestFocus();           // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTDateFocusLost

    private void txtRemarksFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusLost
        txtDocThrough.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtRemarksFocusLost

    private void txtMobileFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobileFocusLost
        if (txtMobile.getText().length() != 10) {
            JOptionPane.showMessageDialog(null, "Mobile number must be 10 Digit");
            txtMobile.requestFocus();
        }
        txtEmail.requestFocus();    // TODO add your handling code here:
    }//GEN-LAST:event_txtMobileFocusLost

    private void btnSendMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMailActionPerformed
        String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
        System.out.println("Sales Party Updation approved = " + ObjSalesParty.getAttribute("APPROVED").getInt());
        if (ObjSalesParty.getAttribute("APPROVED").getInt() == 1) {
            if (type.trim().equals("210010")) {
                int value = JOptionPane.showConfirmDialog(this, " Are you sure? You want to send Final Approved mail to all users? ", "Confirmation Alert!", JOptionPane.YES_NO_OPTION);
                System.out.println("VALUE = " + value);

                if (value == 0) {
                    try {
                        String DOC_NO = txtAmendID.getText();
                        String DOC_DATE = txtAmendDate.getText();
                        String Party_Code = txtPartyCode.getText();

                        String responce = JavaMail.sendNotificationMailOfDetail(clsSalesPartyAmend.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
                        System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        /* if((type.trim().equals("210010")) && (OpgFinal.isSelected())){
         String DOC_NO = txtAmendID.getText();
         String DOC_DATE = txtAmendDate.getText();
         String Party_Code = txtPartyCode.getText();

         String responce = JavaMail.sendNotificationMailOfDetail(clsSalesPartyAmend.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
         System.out.println("Send Mail Responce : " + responce);
         }                    
         if((type.trim().equals("210010")) && (OpgHold.isSelected())){
         String DOC_NO = txtAmendID.getText();
         String DOC_DATE = txtAmendDate.getText();
         String Party_Code = txtPartyCode.getText();

         String responce = JavaMail.sendNotificationMailOfDetail(clsSalesPartyAmend.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
         System.out.println("Send Mail Responce : " + responce);
         }*/
    }//GEN-LAST:event_btnSendMailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab3;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableHS;
    private javax.swing.JTable TableMainCode;
    private javax.swing.JTable TableU;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnSendMail;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkCriticalUnCheck;
    private javax.swing.JCheckBox chkDoNotAllowInterest;
    private javax.swing.JCheckBox chkKeyClientInd;
    private javax.swing.JCheckBox chkPONoRequired;
    private javax.swing.JComboBox cmbCategory;
    private javax.swing.JComboBox cmbChargeCode;
    private javax.swing.JComboBox cmbCountry;
    private javax.swing.JComboBox cmbDeliveryMode;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbMainCodeType;
    private javax.swing.JComboBox cmbPartyType;
    private javax.swing.JComboBox cmbSecondChargeCode;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JComboBox cmbState;
    private javax.swing.JComboBox cmbStockTagging;
    private javax.swing.JComboBox cmbZone;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBack5;
    private javax.swing.JButton cmdBack6;
    private javax.swing.JButton cmdBack7;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClearAll;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext1;
    private javax.swing.JButton cmdNext2;
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdNext4;
    private javax.swing.JButton cmdNext6;
    private javax.swing.JButton cmdNext7;
    private javax.swing.JButton cmdNext8;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemarksBig;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdSelectAll;
    private javax.swing.JButton cmdShowOriginal;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdShowRemarksU;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
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
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblBankID;
    private javax.swing.JLabel lblBankID1;
    private javax.swing.JLabel lblBankID2;
    private javax.swing.JLabel lblBankID3;
    private javax.swing.JLabel lblBankID4;
    private javax.swing.JLabel lblClientCategory;
    private javax.swing.JLabel lblDispatchStation;
    private javax.swing.JLabel lblDocThrough;
    private javax.swing.JLabel lblRegDate;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblSeasonCode;
    private javax.swing.JLabel lblSubCode9;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTransID;
    private javax.swing.JTextField txtAdd1;
    private javax.swing.JTextField txtAdd2;
    private javax.swing.JTextField txtAmendDate;
    private javax.swing.JTextField txtAmendID;
    private javax.swing.JTextField txtAmendReason;
    private javax.swing.JTextField txtAmountLimit;
    private javax.swing.JTextField txtAreaID;
    private javax.swing.JTextField txtAreaInchargeCode;
    private javax.swing.JTextField txtAreaInchargeName;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtBankAdd;
    private javax.swing.JTextField txtBankCity;
    private javax.swing.JTextField txtBankID;
    private javax.swing.JTextField txtBankName;
    private javax.swing.JTextField txtCST;
    private javax.swing.JTextField txtCSTDate;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtClientCategory;
    private javax.swing.JTextField txtContactPerson;
    private javax.swing.JTextField txtContactPerson2;
    private javax.swing.JTextField txtContactPerson3;
    private javax.swing.JTextField txtCorrAdd;
    private javax.swing.JTextField txtCreditDays;
    private javax.swing.JTextField txtDesignation;
    private javax.swing.JTextField txtDesignation2;
    private javax.swing.JTextField txtDesignation3;
    private javax.swing.JTextField txtDesignerIncharge;
    private javax.swing.JTextField txtDispatchStation;
    private javax.swing.JTextField txtDistanceKm;
    private javax.swing.JTextField txtDocThrough;
    private javax.swing.JTextField txtECC;
    private javax.swing.JTextField txtECCDate;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmail2;
    private javax.swing.JTextField txtEmail3;
    private javax.swing.JTextField txtExtraCreditDays;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGSTDate;
    private javax.swing.JTextField txtGSTN;
    private javax.swing.JTextField txtGraceCreditDays;
    private javax.swing.JTextField txtInsuranceCode;
    private javax.swing.JTextField txtInterestPer;
    private javax.swing.JTextField txtMarketSegment;
    private javax.swing.JTextField txtMobile;
    private javax.swing.JTextField txtMobile2;
    private javax.swing.JTextField txtMobile3;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtOldPartyCode;
    private javax.swing.JTextField txtOtherBankAdd;
    private javax.swing.JTextField txtOtherBankCity;
    private javax.swing.JTextField txtOtherBankID;
    private javax.swing.JTextField txtOtherBankName;
    private javax.swing.JTextField txtPANDate;
    private javax.swing.JTextField txtPANNo;
    private javax.swing.JTextField txtPIN;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtProductSegment;
    private javax.swing.JTextField txtRegDate;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtSeasonCode;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtTinDate;
    private javax.swing.JTextField txtTinNo;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtTranID;
    private javax.swing.JTextField txtTranName;
    private javax.swing.JTextField txtURL;
    // End of variables declaration//GEN-END:variables

    private void Add() {

        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(this, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String MainCode = "";

        LOV aList = new LOV();

        aList.SQL = "SELECT DISTINCT MAIN_ACCOUNT_CODE,ACCOUNT_NAME FROM FINANCE.D_FIN_GL WHERE MAIN_ACCOUNT_CODE IN ('210010','210027','210034','210072','210048') AND APPROVED=1 AND CANCELLED=0 ORDER BY ACCOUNT_NAME";
        aList.ReturnCol = 1;
        aList.ShowReturnCol = true;
        aList.DefaultSearchOn = 2;
        aList.UseSpecifiedConn = true;
        aList.dbURL = FinanceGlobal.FinURL;

        if (aList.ShowLOV()) {
            MainCode = aList.ReturnVal;
        } else {
            JOptionPane.showMessageDialog(this, "Select Main code first.");
            return;
        }
        aList.UseSpecifiedConn = false;

        String PartyCode = "";

        LOV bList = new LOV();

//        bList.SQL="SELECT DISTINCT PARTY_CODE,PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND APPROVED=1 AND CANCELLED=0 ORDER BY PARTY_NAME";
        bList.SQL = "SELECT DISTINCT PARTY_CODE,PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND PARTY_CLOSE_IND!=1 AND APPROVED=1 AND CANCELLED=0 ORDER BY PARTY_NAME";
        bList.ReturnCol = 1;
        bList.ShowReturnCol = true;
        bList.DefaultSearchOn = 2;

        if (bList.ShowLOV()) {
            PartyCode = bList.ReturnVal;
        }

        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SetupApproval();
        clsSales_Party ObjParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PartyCode, MainCode);
        //clsSales_Party ObjParty=(clsSales_Party)clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PartyCode, MainCode);

        DisplayPartyData(ObjParty);
        lblTitle.setBackground(Color.BLUE);

        long AmendID = 0;
        AmendID = data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_SAL_PARTY_AMEND_MASTER", "AMEND_ID");
        txtAmendID.setText(Long.toString(AmendID));
        txtAmendDate.setText(EITLERPGLOBAL.getCurrentDate());

        //            }
        //            else {
        //                JOptionPane.showMessageDialog(this,"You must select Party ");
        //            }
    }

    private void DisplayPartyData(clsSales_Party ObjParty) {

        try {
            ClearFields();

            boolean bState = false;

            txtPartyCode.setText((String) ObjParty.getAttribute("PARTY_CODE").getObj());
            txtOldPartyCode.setText((String) ObjParty.getAttribute("OLD_PARTY_CODE").getObj());

            txtName.setText(ObjParty.getAttribute("PARTY_NAME").getString());
            txtAreaID.setText(ObjParty.getAttribute("AREA_ID").getString());
            txtAdd1.setText(ObjParty.getAttribute("ADDRESS1").getString());
            txtAdd2.setText(ObjParty.getAttribute("ADDRESS2").getString());
            txtCity.setText(ObjParty.getAttribute("CITY_ID").getString());
            txtDispatchStation.setText(ObjParty.getAttribute("DISPATCH_STATION").getString());
            txtPIN.setText(ObjParty.getAttribute("PINCODE").getString());
            txtState.setText(ObjParty.getAttribute("DISTRICT").getString());
            txtPhone.setText(ObjParty.getAttribute("PHONE_NO").getString());
            txtMobile.setText(ObjParty.getAttribute("MOBILE_NO").getString());
            txtMobile2.setText(ObjParty.getAttribute("MOBILE_NO_2").getString());
            txtMobile3.setText(ObjParty.getAttribute("MOBILE_NO_3").getString());
            txtCorrAdd.setText(ObjParty.getAttribute("CORR_ADDRESS").getString());
            txtExtraCreditDays.setText(Double.toString(ObjParty.getAttribute("EXTRA_CREDIT_DAYS").getDouble()));
            txtProductSegment.setText(ObjParty.getAttribute("PRODUCT_SEGMENT").getString());
            txtMarketSegment.setText(ObjParty.getAttribute("MARKET_SEGMENT").getString());
            lblClientCategory.setText(ObjParty.getAttribute("CLIENT_CATEGORY").getString());
            String Para_Desc = data.getStringValueFromDB("SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'CLIENT_CATEGORY' AND PARA_EXT1 ='" + ObjParty.getAttribute("CLIENT_CATEGORY").getString() + "'");
            txtClientCategory.setText(Para_Desc);
            //txtClientCategory.setText();
            txtDesignerIncharge.setText(ObjParty.getAttribute("DESIGNER_INCHARGE").getString());
//            txtAreaInchargeCode.setText(ObjParty.getAttribute("INCHARGE_CD").getString());

            if (ObjParty.getAttribute("INCHARGE_CD").getString().equals("")) {
                txtAreaInchargeCode.setText("0");
            } else {
                txtAreaInchargeCode.setText(ObjParty.getAttribute("INCHARGE_CD").getString());
            }

            txtAreaInchargeName.setText(clsSales_Party.getFeltInchargeName(Integer.parseInt(txtAreaInchargeCode.getText().trim())));
            EITLERPGLOBAL.setComboIndex(cmbState, ObjParty.getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            EITLERPGLOBAL.setComboIndex(cmbCountry, ObjParty.getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            EITLERPGLOBAL.setComboIndex(cmbZone, ObjParty.getAttribute("ZONE").getString());
            txtEmail.setText(ObjParty.getAttribute("EMAIL").getString());
            txtEmail2.setText(ObjParty.getAttribute("EMAIL_ID2").getString());
            txtEmail3.setText(ObjParty.getAttribute("EMAIL_ID3").getString());
            
            txtURL.setText(ObjParty.getAttribute("URL").getString());
            txtCST.setText(ObjParty.getAttribute("CST_NO").getString());
            txtCSTDate.setText(EITLERPGLOBAL.formatDate(ObjParty.getAttribute("CST_DATE").getString()));
            txtGSTN.setText(ObjParty.getAttribute("GSTIN_NO").getString());
            txtGSTDate.setText(EITLERPGLOBAL.formatDate(ObjParty.getAttribute("GSTIN_DATE").getString()));
            txtECC.setText(ObjParty.getAttribute("ECC_NO").getString());
            txtECCDate.setText(EITLERPGLOBAL.formatDate(ObjParty.getAttribute("ECC_DATE").getString()));
            txtTinNo.setText(ObjParty.getAttribute("TIN_NO").getString());
            txtTinDate.setText(EITLERPGLOBAL.formatDate(ObjParty.getAttribute("TIN_DATE").getString()));
            txtContactPerson.setText(ObjParty.getAttribute("CONTACT_PERSON").getString());
            txtContactPerson2.setText(ObjParty.getAttribute("CONTACT_PERSON_2").getString());
            txtContactPerson3.setText(ObjParty.getAttribute("CONTACT_PERSON_3").getString());
            txtDesignation.setText(ObjParty.getAttribute("CONT_PERS_DESIGNATION").getString());
            txtDesignation2.setText(ObjParty.getAttribute("CONT_PERS_DESIGNATION_2").getString());
            txtDesignation3.setText(ObjParty.getAttribute("CONT_PERS_DESIGNATION_3").getString());
            txtTranID.setText(Integer.toString(ObjParty.getAttribute("TRANSPORTER_ID").getInt()));
            txtTranName.setText(clsSales_Party.getTransporterName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(txtTranID.getText().trim())));
            txtBankID.setText(Integer.toString(ObjParty.getAttribute("BANK_ID").getInt()));
            txtBankName.setText(ObjParty.getAttribute("BANK_NAME").getString());
            txtBankAdd.setText(ObjParty.getAttribute("BANK_ADDRESS").getString());
            txtBankCity.setText(ObjParty.getAttribute("BANK_CITY").getString());
            txtAmountLimit.setText(Double.toString(ObjParty.getAttribute("AMOUNT_LIMIT").getDouble()));
            critical_limit_original = ObjParty.getAttribute("AMOUNT_LIMIT").getDouble();
            txtDocThrough.setText(ObjParty.getAttribute("DOCUMENT_THROUGH").getString());
            EITLERPGLOBAL.setComboIndex(cmbMainCodeType, ObjParty.getAttribute("MAIN_ACCOUNT_CODE").getString());
            txtPANNo.setText(ObjParty.getAttribute("PAN_NO").getString());
            txtPANDate.setText(EITLERPGLOBAL.formatDate(ObjParty.getAttribute("PAN_DATE").getString()));
            txtRemarks.setText(ObjParty.getAttribute("REMARKS").getString());
            txtInterestPer.setText(Double.toString(ObjParty.getAttribute("DELAY_INTEREST_PER").getDouble()));

            txtDistanceKm.setText(Integer.toString(ObjParty.getAttribute("DISTANCE_KM").getInt()));
            
            if(ObjParty.getAttribute("DELIVERY_MODE").getString().equals("")) {
                cmbDeliveryMode.setSelectedIndex(0);
            } else {
                cmbDeliveryMode.setSelectedItem(ObjParty.getAttribute("DELIVERY_MODE").getString());
            }

            String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
            String ChargeCode = ObjParty.getAttribute("CHARGE_CODE").getString();
            String ChargeCodeII = ObjParty.getAttribute("CHARGE_CODE_II").getString();

            if (type.trim().equals("210010")) {
                ChargeCodeCombos(" AND INVOICE_TYPE_ID NOT IN ('S04') ");
                EITLERPGLOBAL.setComboIndex(cmbZone, ObjParty.getAttribute("ZONE").getString());

            } else {
                ChargeCodeCombos(" AND INVOICE_TYPE_ID NOT IN ('F04') ");
            }

            if ((ChargeCode.trim().equals("04")) && (type.trim().equals("210010"))) {
                EITLERPGLOBAL.setComboIndex(cmbChargeCode, "04");
            } else if ((ChargeCode.trim().equals("04")) && (!type.trim().equals("210010"))) {
                EITLERPGLOBAL.setComboIndex(cmbChargeCode, "04");
            } else {
                EITLERPGLOBAL.setComboIndex(cmbChargeCode, ObjParty.getAttribute("CHARGE_CODE").getString());
            }

            if ((ChargeCodeII.trim().equals("04")) && (type.trim().equals("210010"))) {
                EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode, "04");
            } else if ((ChargeCodeII.trim().equals("04")) && (!type.trim().equals("210010"))) {
                EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode, "04");
            } else {
                EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode, ObjParty.getAttribute("CHARGE_CODE_II").getString());
            }

            //EITLERPGLOBAL.setComboIndex(cmbChargeCode,ObjParty.getAttribute("CHARGE_CODE").getString());
            //EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode,ObjParty.getAttribute("CHARGE_CODE_II").getString());
            EITLERPGLOBAL.setComboIndex(cmbCategory, ObjParty.getAttribute("CATEGORY").getString());
            txtOtherBankAdd.setText(ObjParty.getAttribute("OTHER_BANK_ADDRESS").getString());
            txtOtherBankID.setText(Integer.toString(ObjParty.getAttribute("OTHER_BANK_ID").getInt()));
            txtOtherBankName.setText(ObjParty.getAttribute("OTHER_BANK_NAME").getString());
            txtOtherBankCity.setText(ObjParty.getAttribute("OTHER_BANK_CITY").getString());
            txtCreditDays.setText(Double.toString(ObjParty.getAttribute("CREDIT_DAYS").getDouble()));
            EITLERPGLOBAL.setComboIndex(cmbPartyType, ObjParty.getAttribute("PARTY_TYPE").getInt());
            txtInsuranceCode.setText(ObjParty.getAttribute("INSURANCE_CODE").getString());
            if (ObjParty.getAttribute("DO_NOT_ALLOW_INTEREST").getInt() == 1) {
                chkDoNotAllowInterest.setSelected(true);
            } else {
                chkDoNotAllowInterest.setSelected(false);
            }
            if (ObjParty.getAttribute("CRITICAL_LIMIT_UNCHECK").getInt() == 1) {
                chkCriticalUnCheck.setSelected(true);
            } else {
                chkCriticalUnCheck.setSelected(false);
            }
            if (ObjParty.getAttribute("PO_NO_REQUIRED").getInt() == 1) {
                chkPONoRequired.setSelected(true);
            } else {
                chkPONoRequired.setSelected(false);
            }
            EITLERPGLOBAL.setComboIndex(cmbStockTagging,ObjParty.getAttribute("TAGGING_APPROVAL_IND").getInt());
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjParty.getAttribute("HIERARCHY_ID").getInt());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Display Data Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void SetupApproval() {

        if (cmbHierarchy.getItemCount() > 1) {
            cmbHierarchy.setEnabled(true);
        }

        //Set Default Hierarchy ID for User
        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);

        if (EditMode == EITLERPGLOBAL.ADD) {
            lnFromID = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {

            int FromUserID = ApprovalFlow.getFromID((int) EITLERPGLOBAL.gCompanyID, clsSalesPartyAmend.ModuleID, (String) ObjSalesParty.getAttribute("AMEND_ID").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = ApprovalFlow.getFromRemarks((int) EITLERPGLOBAL.gCompanyID, clsSalesPartyAmend.ModuleID, FromUserID, (String) ObjSalesParty.getAttribute("AMEND_ID").getObj());

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
            checkControl(); //Added on 17/10/2018 by GAURANG
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

                String DocNo = Integer.toString((int) ObjSalesParty.getAttribute("AMEND_ID").getVal());

                List = ApprovalFlow.getRemainingUsers((int) EITLERPGLOBAL.gCompanyID, clsSalesPartyAmend.ModuleID, DocNo);
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
        txtAmendDate.setEnabled(pStat);
        txtAmendReason.setEnabled(pStat);
        txtPartyCode.setEnabled(pStat);
        txtName.setEnabled(pStat);
        txtAreaID.setEnabled(pStat);
        txtAdd1.setEnabled(pStat);
        txtAdd2.setEnabled(pStat);
        txtCorrAdd.setEnabled(pStat);
        txtCity.setEnabled(pStat);
        txtDispatchStation.setEnabled(pStat);
        txtPIN.setEnabled(pStat);
        txtState.setEnabled(pStat);
        txtPhone.setEnabled(pStat);
        txtMobile.setEnabled(pStat);
        txtMobile2.setEnabled(pStat);
        txtMobile3.setEnabled(pStat);
        txtEmail.setEnabled(pStat);
        txtEmail2.setEnabled(pStat);
        txtEmail3.setEnabled(pStat);
        
        txtURL.setEnabled(pStat);
        txtCST.setEnabled(pStat);
        txtCSTDate.setEnabled(pStat);
        txtGSTN.setEnabled(pStat);
        txtGSTDate.setEnabled(pStat);
        txtECC.setEnabled(pStat);
        txtECCDate.setEnabled(pStat);
        txtTinNo.setEnabled(pStat);
        txtTinDate.setEnabled(pStat);
        txtBankID.setEnabled(pStat);
        txtBankName.setEnabled(pStat);
        txtBankAdd.setEnabled(pStat);
        txtBankCity.setEnabled(pStat);
        cmbChargeCode.setEnabled(pStat);
        cmbSecondChargeCode.setEnabled(pStat);
        cmbState.setEnabled(pStat);
        cmbCountry.setEnabled(pStat);
        cmbZone.setEnabled(pStat);
        txtAreaInchargeName.setEnabled(pStat);
        txtAreaInchargeCode.setEnabled(pStat);
        txtMarketSegment.setEnabled(pStat);
        txtProductSegment.setEnabled(pStat);
        txtClientCategory.setEnabled(pStat);
        txtDesignerIncharge.setEnabled(pStat);
        txtContactPerson.setEnabled(pStat);
        txtContactPerson2.setEnabled(pStat);
        txtContactPerson3.setEnabled(pStat);
        txtDesignation.setEnabled(pStat);
        txtDesignation2.setEnabled(pStat);
        txtDesignation3.setEnabled(pStat);
        txtTranID.setEnabled(pStat);
        txtTranName.setEnabled(pStat);
        txtAmountLimit.setEnabled(pStat);
        txtCreditDays.setEnabled(pStat);
        txtExtraCreditDays.setEnabled(pStat);
        txtGraceCreditDays.setEnabled(pStat);
        
        cmbDeliveryMode.setEnabled(pStat);

        txtDistanceKm.setEnabled(pStat);
        //txtDocThrough.setEnabled(pStat);
        cmbMainCodeType.setEnabled(pStat);
        txtOtherBankID.setEnabled(pStat);
        txtOtherBankAdd.setEnabled(pStat);
        txtOtherBankCity.setEnabled(pStat);
        txtOtherBankName.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        cmbCategory.setEnabled(pStat);
        cmbPartyType.setEnabled(pStat);
        txtInsuranceCode.setEnabled(pStat);
        txtPANNo.setEnabled(pStat);
        txtPANDate.setEnabled(pStat);
        txtRemarks.setEnabled(pStat);
        txtInterestPer.setEnabled(pStat);
        chkDoNotAllowInterest.setEnabled(pStat);
        chkCriticalUnCheck.setEnabled(pStat);
        cmbStockTagging.setEnabled(pStat);        
        SetupApproval();
        TableMainCode.setEnabled(pStat);
        cmdSelectAll.setEnabled(pStat);
        cmdClearAll.setEnabled(pStat);
        lblClientCategory.setEnabled(pStat);
        
        chkPONoRequired.setEnabled(pStat);

    }

    private void LockFields(boolean pStat) {
        txtAmendDate.setEnabled(pStat);
        txtAmendReason.setEnabled(pStat);
        txtPartyCode.setEnabled(pStat);
        txtName.setEnabled(pStat);
        txtAreaID.setEnabled(pStat);
        txtAdd1.setEnabled(pStat);
        txtAdd2.setEnabled(pStat);
        txtCity.setEnabled(pStat);
        txtDispatchStation.setEnabled(pStat);
        txtPIN.setEnabled(pStat);
        txtState.setEnabled(pStat);
        txtPhone.setEnabled(pStat);
        txtMobile.setEnabled(pStat);
        txtMobile2.setEnabled(pStat);
        txtMobile3.setEnabled(pStat);
        txtCorrAdd.setEnabled(pStat);
        txtEmail.setEnabled(pStat);
        txtEmail2.setEnabled(pStat);
        txtEmail3.setEnabled(pStat);
        txtURL.setEnabled(pStat);
        txtCST.setEnabled(pStat);
        txtCSTDate.setEnabled(pStat);
        txtGSTN.setEnabled(pStat);
        txtGSTDate.setEnabled(pStat);
        txtECC.setEnabled(pStat);
        txtECCDate.setEnabled(pStat);
        txtTinNo.setEnabled(pStat);
        txtTinDate.setEnabled(pStat);
        txtBankID.setEnabled(pStat);
        txtBankName.setEnabled(pStat);
        txtBankAdd.setEnabled(pStat);
        txtBankCity.setEnabled(pStat);
        cmbChargeCode.setEnabled(pStat);
        cmbSecondChargeCode.setEnabled(pStat);
        cmbState.setEnabled(pStat);
        cmbCountry.setEnabled(pStat);
        cmbZone.setEnabled(pStat);
        txtAreaInchargeName.setEnabled(pStat);
        txtAreaInchargeCode.setEnabled(pStat);
        txtMarketSegment.setEnabled(pStat);
        txtProductSegment.setEnabled(pStat);
        txtClientCategory.setEnabled(pStat);
        txtDesignerIncharge.setEnabled(pStat);
        txtContactPerson.setEnabled(pStat);
        txtContactPerson2.setEnabled(pStat);
        txtContactPerson3.setEnabled(pStat);
        txtDesignation.setEnabled(pStat);
        txtDesignation2.setEnabled(pStat);
        txtDesignation3.setEnabled(pStat);
        txtTranID.setEnabled(pStat);
        txtTranName.setEnabled(pStat);
        txtAmountLimit.setEnabled(pStat);
        txtCreditDays.setEnabled(pStat);
        txtExtraCreditDays.setEnabled(pStat);
        txtGraceCreditDays.setEnabled(pStat);
        //txtDocThrough.setEnabled(pStat);
        cmbMainCodeType.setEnabled(pStat);
        txtOtherBankID.setEnabled(pStat);
        txtOtherBankAdd.setEnabled(pStat);
        txtOtherBankCity.setEnabled(pStat);
        txtOtherBankName.setEnabled(pStat);
        txtDistanceKm.setEnabled(pStat);
        
        cmbDeliveryMode.setEnabled(pStat);
        
        //OpgApprove.setEnabled(pStat);
        //OpgFinal.setEnabled(pStat);
        //OpgReject.setEnabled(pStat);
        //OpgHold.setEnabled(pStat);
        //txtToRemarks.setEnabled(pStat);
        cmbCategory.setEnabled(pStat);
        cmbPartyType.setEnabled(pStat);
        txtInsuranceCode.setEnabled(pStat);
        txtPANNo.setEnabled(pStat);
        txtPANDate.setEnabled(pStat);
        txtRemarks.setEnabled(pStat);
        txtInterestPer.setEnabled(pStat);
        chkDoNotAllowInterest.setEnabled(pStat);
        chkCriticalUnCheck.setEnabled(pStat);
        chkPONoRequired.setEnabled(pStat);
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

        //Now Header level validations
        if (txtPartyCode.getText().trim().equals("") && OpgFinal.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please enter Party Code");
            txtPartyCode.requestFocus(true);
            return false;
        }

        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + txtPartyCode.getText() + "' ")) {
            JOptionPane.showMessageDialog(null, "Party closed in Party Master.");
            return false;
        }

        //        if(!txtPartyCode.getText().trim().equals("")) {
        //            if(EditMode==EITLERPGLOBAL.ADD) {
        //
        //                if (data.IsRecordExist("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"' ")) {
        //                    JOptionPane.showMessageDialog(this,"Party Code already exists!!");
        //                    txtPartyCode.requestFocus(true);
        //                    return false;
        //                }
        //
        //            }
        //        }
        if (txtAmendReason.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Amend Reason");
            txtAmendReason.requestFocus();
            return false;
        }

        if (txtName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Party Name");
            return false;
        }

        if (txtAdd1.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Address Line 1");
            return false;
        }

        if (txtCity.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter City");
            return false;
        }

        if (txtDispatchStation.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Dispatch Station");
            return false;
        }

        if (txtPIN.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Pincode");
            return false;
        }

        if (txtState.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter District");
            return false;
        }

        //new code by vivek on 16/09/2013 to add state & country. start
        // state name selection validation
        if (EITLERPGLOBAL.getComboCode(cmbState) == 0) {
            JOptionPane.showMessageDialog(this, "Select State Name", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // country name selection validation
        if (EITLERPGLOBAL.getComboCode(cmbCountry) == 0) {
            JOptionPane.showMessageDialog(this, "Select Country Name", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //new code by vivek on 16/09/2013 to add state & country. end

        //        if(txtBankID.getText().trim().equals("")) {
        //            JOptionPane.showMessageDialog(this,"Please enter Bank ID");
        //            return false;
        //        }
        //
        //        if(txtBankAdd.getText().trim().equals("")) {
        //            JOptionPane.showMessageDialog(this,"Please enter Bank Address");
        //            return false;
        //        }
        //
        //        if(txtBankCity.getText().trim().equals("")) {
        //            JOptionPane.showMessageDialog(this,"Please enter Bank City");
        //            return false;
        //        }
        //        if(txtBankName.getText().trim().equals("")) {
        //            JOptionPane.showMessageDialog(this,"Please Select Bank");
        //            return false;
        //        }
        if (txtAmountLimit.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Amount Limit");
            return false;
        }

        if (Double.parseDouble(txtAmountLimit.getText()) != critical_limit_original) {
            if (data.IsRecordExist("SELECT PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H WHERE H.GROUP_CODE=D.GROUP_CODE AND H.CANCELED!=1 AND D.PARTY_CODE='" + txtPartyCode.getText().trim() + "'")) {
                JOptionPane.showMessageDialog(this, "Party Exist in Group Master.Please Change Critical Amount Limit in Group Master");
                return false;
            }
        }

        if (txtTranID.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Transporter ID");
            return false;
        }

        if (!txtCSTDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtCSTDate.getText())) {
                JOptionPane.showMessageDialog(this, "Invalid CST Date");
                return false;
            }
        }
        if (!txtGSTDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtGSTDate.getText())) {
                JOptionPane.showMessageDialog(this, "Invalid CST Date");
                return false;
            }
        }

        if (!txtECCDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtECCDate.getText())) {
                JOptionPane.showMessageDialog(this, "Invalid ECC Date");
                return false;
            }
        }

        if (!txtTinDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtTinDate.getText())) {
                JOptionPane.showMessageDialog(this, "Invalid Tin Date");
                return false;
            }
        }

        if (!txtPANDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtPANDate.getText())) {
                JOptionPane.showMessageDialog(this, "Invalid PAN Date");
                return false;
            }
        }

        if(cmbDeliveryMode.getSelectedItem().toString().trim().startsWith("Select")) {
            JOptionPane.showMessageDialog(this,"Please select Delivery Mode");
            return false;
        }
        
        String MainCode = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
        String AreaCode = EITLERPGLOBAL.getCombostrCode(cmbZone);
        if (MainCode.trim().equals("210010") && AreaCode.startsWith("SELECT")) {
            JOptionPane.showMessageDialog(this, "Please select area code.");
            cmbZone.requestFocus(true);
            return false;
        }

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select the hierarchy.");
            return false;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please select the Approval Action");
            return false;
        }

        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter the remarks for rejection");
            return false;
        }

        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(this, "Please select the user, to whom rejected document to be send");
            return false;
        }

        if (OpgFinal.isSelected()) {
            if (txtPartyCode.getText().trim().substring(0, 4).equals("NEWD")) {
                JOptionPane.showMessageDialog(this, "Invalid Party Code");
                txtPartyCode.requestFocus(true);
                return false;
            }
        }

        if (txtPartyCode.getText().trim().substring(0, 4).equals("NEWD")) {

        }

        return true;
    }

    private void ClearFields() {
        txtAmendID.setText("0");
        txtAmendDate.setText("");
        txtAmendReason.setText("");
        txtPartyCode.setText("");
        txtName.setText("");
        txtAreaID.setText("");
        txtAdd1.setText("");
        txtAdd2.setText("");
        txtCity.setText("");
        txtDispatchStation.setText("");
        txtPIN.setText("");
        txtState.setText("");
        txtPhone.setText("");
        txtMobile.setText("");
        txtMobile2.setText("");
        txtMobile3.setText("");
        txtCorrAdd.setText("");
        txtEmail.setText("");
        txtEmail2.setText("");
        txtEmail3.setText("");
        
        txtURL.setText("");
        txtCST.setText("");
        txtCSTDate.setText("");
        txtGSTN.setText("");
        txtGSTDate.setText("");
        txtECC.setText("");
        txtECCDate.setText("");
        txtTinNo.setText("");
        txtTinDate.setText("");
        txtContactPerson.setText("");
        txtContactPerson2.setText("");
        txtContactPerson3.setText("");
        txtDesignation.setText("");
        txtDesignation2.setText("");
        txtDesignation3.setText("");
        txtBankID.setText("");
        txtBankName.setText("");
        txtBankAdd.setText("");
        txtBankCity.setText("");
        txtTranID.setText("");
        txtTranName.setText("");
        cmbMainCodeType.setSelectedIndex(0);
        txtOtherBankID.setText("");
        txtOtherBankAdd.setText("");
        txtOtherBankCity.setText("");
        txtOtherBankName.setText("");
        cmbCategory.setSelectedIndex(0);
        txtAmountLimit.setText("");
        txtDocThrough.setText("");
        txtCreditDays.setText("");
        txtExtraCreditDays.setText("");
        txtGraceCreditDays.setText("");
        cmbChargeCode.setSelectedIndex(0);
        cmbSecondChargeCode.setSelectedIndex(0);
        cmbState.setSelectedIndex(0);
        cmbCountry.setSelectedIndex(0);
        txtToRemarks.setText("");
        cmbPartyType.setSelectedIndex(0);
        txtInsuranceCode.setText("");
        txtPANNo.setText("");
        txtPANDate.setText("");
        txtRemarks.setText("");
        txtInterestPer.setText("");
        chkDoNotAllowInterest.setSelected(false);
        chkCriticalUnCheck.setSelected(false);
        cmbZone.setSelectedIndex(0);
        txtAreaInchargeCode.setText("");
        txtAreaInchargeName.setText("");
        txtMarketSegment.setText("");
        txtProductSegment.setText("");
        txtClientCategory.setText("");
        txtDesignerIncharge.setText("");
        lblClientCategory.setText("");
        cmbStockTagging.setSelectedIndex(0);

        txtDistanceKm.setText("");
        
        cmbDeliveryMode.setSelectedIndex(0);
        
        chkPONoRequired.setSelected(false);

        FormatGridHS();
        FormatGridU();
        FormatGrid();
        FormatGridA();
        GenerateGrid();
    }

    private void Edit() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(this, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        String sAmendID = Long.toString((long) ObjSalesParty.getAttribute("AMEND_ID").getVal());
        if (ObjSalesParty.IsEditable(EITLERPGLOBAL.gCompanyID, sAmendID, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (ApprovalFlow.IsCreator(clsSalesPartyAmend.ModuleID, sAmendID) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 11522)) {
                SetFields(true);
            } else {
                EnableApproval();
            }

            DisplayData();
            DisableToolbar();

            if (!ApprovalFlow.IsCreator(clsSalesPartyAmend.ModuleID, sAmendID) && (EITLERPGLOBAL.gUserID != 135)) {
                LockFields(false);
            }
            
            checkControl(); //Added on 17/10/2018 by GAURANG
            
            /* closed on 17/10/2018 by GAURANG
            String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
            System.out.println(type);
            if (type.trim().equals("210010")) {
                //if(EITLERPGLOBAL.getComboCode(cmbHierarchy)==1133){
                if (EITLERPGLOBAL.gUserID == 9) {
                    clsSales_Party ObjPartytemp = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText(), type);

                    String ChargeCodeMaster = ObjPartytemp.getAttribute("CHARGE_CODE").getString();
                    String ChargeCodeIIMaster = ObjPartytemp.getAttribute("CHARGE_CODE_II").getString();
                    double CreditDaysMaster = ObjPartytemp.getAttribute("CREDIT_DAYS").getDouble();
                    double ExtraCreditDaysMaster = ObjPartytemp.getAttribute("EXTRA_CREDIT_DAYS").getDouble();
                    double GraceCreditDaysMaster = ObjPartytemp.getAttribute("GRACE_CREDIT_DAYS").getDouble();
                    int BankIDMaster = ObjPartytemp.getAttribute("BANK_ID").getInt();
                    int OtherBankIDMaster = ObjPartytemp.getAttribute("OTHER_BANK_ID").getInt();
                    //double CriticalLimitMaster=ObjPartytemp.getAttribute("CRITICAL_AMOUNT_LIMIT").getDouble();
                    double CriticalLimitMaster = ObjPartytemp.getAttribute("AMOUNT_LIMIT").getDouble();
                    double DelayIntPerMaster = ObjPartytemp.getAttribute("DELAY_INTEREST_PER").getDouble();
                    String AreaInchargeCodeMaster = ObjPartytemp.getAttribute("INCHARGE_CD").getString();
                    String ZoneMaster = ObjPartytemp.getAttribute("ZONE").getString();
                    String ClientCatagoryMaster = ObjPartytemp.getAttribute("CLIENT_CATEGORY").getString();

                    
                     if(!ChargeCodeMaster.equals(EITLERPGLOBAL.getCombostrCode(cmbChargeCode))){
                     OpgFinal.setEnabled(false);                       
                     }else{
                     OpgApprove.setEnabled(false);
                     }
                   
                     //if(!ChargeCodeIIMaster.equals(EITLERPGLOBAL.getCombostrCode(cmbSecondChargeCode))){
                     //    OpgFinal.setEnabled(false);
                     //}
                     if(CreditDaysMaster!=UtilFunctions.CDbl(txtCreditDays.getText())){
                     OpgFinal.setEnabled(false);
                     }else{
                     OpgApprove.setEnabled(false);
                     }
                     if(ExtraCreditDaysMaster!=UtilFunctions.CDbl(txtExtraCreditDays.getText())){
                     OpgFinal.setEnabled(false);
                     }else{
                     OpgApprove.setEnabled(false);
                     }
                     if(GraceCreditDaysMaster!=UtilFunctions.CDbl(txtGraceCreditDays.getText())){
                     OpgFinal.setEnabled(false);
                     }else{
                     OpgApprove.setEnabled(false);
                     }
                     //if(BankIDMaster!=EITLERPGLOBAL.formatLNumber(txtBankID.getText())){
                     //    OpgFinal.setEnabled(false);
                     //}
                     //if(OtherBankIDMaster!=EITLERPGLOBAL.formatLNumber(txtOtherBankID.getText())){
                     //    OpgFinal.setEnabled(false);
                     //}
                   
                     if(CriticalLimitMaster!=UtilFunctions.CDbl(txtAmountLimit.getText())){
                     OpgFinal.setEnabled(false);
                     }else{
                     OpgApprove.setEnabled(false);
                     }
                     if(DelayIntPerMaster!=UtilFunctions.CDbl(txtInterestPer.getText())){
                     OpgFinal.setEnabled(false);
                     }else{
                     OpgApprove.setEnabled(false);
                     }
                     //if(!ZoneMaster.equals(EITLERPGLOBAL.getCombostrCode(cmbZone))){
                     //    OpgFinal.setEnabled(false);
                     //}
                     //if(!AreaInchargeCodeMaster.equals(txtAreaInchargeCode.getText())){
                     //    OpgFinal.setEnabled(false);
                     //}
                     //if(!ClientCatagoryMaster.equals(lblClientCategory.getText())){
                     //    OpgFinal.setEnabled(false);
                     //}
                   
                }

            //}
            } */

        } else {
            JOptionPane.showMessageDialog(this, "You cannot edit this record. \n It is either approved/rejected or waiting approval for other user");
        }
    }

    private void Delete() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(this, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        String sPartyCode = Long.toString((long) ObjSalesParty.getAttribute("AMEND_ID").getVal());

        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record ?", "SDML ERP", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (ObjSalesParty.CanDelete(EITLERPGLOBAL.gCompanyID, sPartyCode, EITLERPGLOBAL.gNewUserID)) {
                if (ObjSalesParty.Delete()) {
                    ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID);
                    MoveLast();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured while deleting. Error is " + ObjSalesParty.LastError);
                }
            } else {
                JOptionPane.showMessageDialog(this, "You cannot delete this record. \n It is either approved/rejected record or waiting approval for other user or is referred in other documents");
            }
        }
    }

    private void Save() {
        //Form level validations
        if (Validate() == false) {
            return; //Validation failed
        }

        SetData();

        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjSalesParty.Insert()) {
                // MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + ObjSalesParty.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjSalesParty.Update()) {
                DisplayData();
                String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
                String query = "SELECT USER_ID FROM D_COM_HIERARCHY H,D_COM_HIERARCHY_RIGHTS HR WHERE H.HIERARCHY_ID=HR.HIERARCHY_ID AND MODULE_ID=" + clsSalesPartyAmend.ModuleID + " AND HR.HIERARCHY_ID=" + EITLERPGLOBAL.getComboCode(cmbHierarchy) + " AND SR_NO=2";
                int AreaIncharge = data.getIntValueFromDB(query);
                //if(type.trim().equals("210010")){
                //if(AreaIncharge==EITLERPGLOBAL.gNewUserID){
                //if (OpgApprove.isSelected()) {                    
                if ((type.trim().equals("210010")) && (AreaIncharge == EITLERPGLOBAL.gNewUserID) && (OpgApprove.isSelected())) {
                    String DOC_NO = txtAmendID.getText();
                    String DOC_DATE = txtAmendDate.getText();
                    String Party_Code = txtPartyCode.getText();

                    String responce = JavaMail.sendNotificationMailOfDetail(clsSalesPartyAmend.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true);
                    System.out.println("Send Mail Responce : " + responce);
                }
                //}
                //}
                //}

                if ((type.trim().equals("210010")) && (OpgFinal.isSelected())) {
                    String DOC_NO = txtAmendID.getText();
                    String DOC_DATE = txtAmendDate.getText();
                    String Party_Code = txtPartyCode.getText();

                    String responce = JavaMail.sendNotificationMailOfDetail(clsSalesPartyAmend.ModuleID, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), false);
                    System.out.println("Send Mail Responce : " + responce);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + ObjSalesParty.LastError);
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
        Loader ObjLoader = new Loader(this, "EITLERP.frmSalesPartyFind", true);
        frmSalesPartyFind ObjReturn = (frmSalesPartyFind) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            if (!ObjSalesParty.Filter(ObjReturn.strQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.");
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
            btnSendMail.setEnabled(false);

            if (EditMode == 0) {
                if (ObjSalesParty.getAttribute("APPROVED").getInt() == 1) {

                    lblTitle.setBackground(Color.BLUE);
                    btnSendMail.setEnabled(true);

                }

                if (ObjSalesParty.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                }

                if (ObjSalesParty.getAttribute("CANCELLED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = clsSalesPartyAmend.ModuleID;

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        try {
            ClearFields();

            boolean bState = false;
            //lblTitle.setText("Sales Party Master Updation -" + ObjSalesParty.getAttribute("AMEND_ID").getString());
            txtAmendID.setText(Integer.toString((int) ObjSalesParty.getAttribute("AMEND_ID").getVal()));
            txtAmendDate.setText(EITLERPGLOBAL.formatDate((String) ObjSalesParty.getAttribute("AMEND_DATE").getObj()));
            txtAmendReason.setText((String) ObjSalesParty.getAttribute("AMEND_REASON").getObj());

            txtPartyCode.setText((String) ObjSalesParty.getAttribute("PARTY_CODE").getObj());
            txtOldPartyCode.setText((String) ObjSalesParty.getAttribute("OLD_PARTY_CODE").getObj());

            txtName.setText(ObjSalesParty.getAttribute("PARTY_NAME").getString());
            txtSeasonCode.setText(ObjSalesParty.getAttribute("SEASON_CODE").getString());
            txtRegDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("REG_DATE").getString()));
            lblClientCategory.setText(ObjSalesParty.getAttribute("CLIENT_CATEGORY").getString());
            String Para_Desc = data.getStringValueFromDB("SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_EXT1 ='" + ObjSalesParty.getAttribute("CLIENT_CATEGORY").getString() + "'");
            txtClientCategory.setText(Para_Desc);

            txtAreaID.setText(ObjSalesParty.getAttribute("AREA_ID").getString());
            txtAdd1.setText(ObjSalesParty.getAttribute("ADDRESS1").getString());
            txtAdd2.setText(ObjSalesParty.getAttribute("ADDRESS2").getString());
            txtCity.setText(ObjSalesParty.getAttribute("CITY_ID").getString());
            txtDispatchStation.setText(ObjSalesParty.getAttribute("DISPATCH_STATION").getString());
            txtPIN.setText(ObjSalesParty.getAttribute("PINCODE").getString());
            txtState.setText(ObjSalesParty.getAttribute("DISTRICT").getString());
            txtPhone.setText(ObjSalesParty.getAttribute("PHONE_NO").getString());
            txtMobile.setText(ObjSalesParty.getAttribute("MOBILE_NO").getString());
            txtMobile2.setText(ObjSalesParty.getAttribute("MOBILE_NO_2").getString());
            txtMobile3.setText(ObjSalesParty.getAttribute("MOBILE_NO_3").getString());
            txtCorrAdd.setText(ObjSalesParty.getAttribute("CORR_ADDRESS").getString());
            EITLERPGLOBAL.setComboIndex(cmbState, ObjSalesParty.getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & party.
            EITLERPGLOBAL.setComboIndex(cmbCountry, ObjSalesParty.getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & party.
            txtMarketSegment.setText(ObjSalesParty.getAttribute("MARKET_SEGMENT").getString());
            txtProductSegment.setText(ObjSalesParty.getAttribute("PRODUCT_SEGMENT").getString());
            txtClientCategory.setText(ObjSalesParty.getAttribute("CLIENT_CATEGORY").getString());
            txtAreaInchargeCode.setText(ObjSalesParty.getAttribute("INCHARGE_CD").getString());
            if (!txtAreaInchargeCode.getText().equals("")) {
                //txtAreaInchargeName.setText(clsSales_Party.getFeltInchargeName(Integer.parseInt(txtAreaInchargeCode.getText().trim())));
                txtAreaInchargeName.setText(clsSales_Party.getFeltInchargeName(Integer.parseInt(ObjSalesParty.getAttribute("INCHARGE_CD").getString())));
            }
            EITLERPGLOBAL.setComboIndex(cmbZone, ObjSalesParty.getAttribute("ZONE").getString());
            txtDesignerIncharge.setText(ObjSalesParty.getAttribute("DESIGNER_INCHARGE").getString());
            txtEmail.setText(ObjSalesParty.getAttribute("EMAIL").getString());
            txtEmail2.setText(ObjSalesParty.getAttribute("EMAIL_ID2").getString());
            txtEmail3.setText(ObjSalesParty.getAttribute("EMAIL_ID3").getString());
            txtURL.setText(ObjSalesParty.getAttribute("URL").getString());
            txtCST.setText(ObjSalesParty.getAttribute("CST_NO").getString());
            txtCSTDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("CST_DATE").getString()));
            txtGSTN.setText(ObjSalesParty.getAttribute("GSTIN_NO").getString());
            txtGSTDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("GST_DATE").getString()));
            txtECC.setText(ObjSalesParty.getAttribute("ECC_NO").getString());
            txtECCDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("ECC_DATE").getString()));
            txtTinNo.setText(ObjSalesParty.getAttribute("TIN_NO").getString());
            txtTinDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("TIN_DATE").getString()));
            txtPANNo.setText(ObjSalesParty.getAttribute("PAN_NO").getString());
            txtPANDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("PAN_DATE").getString()));
            txtRemarks.setText(ObjSalesParty.getAttribute("REMRAKS").getString());
            txtContactPerson.setText(ObjSalesParty.getAttribute("CONTACT_PERSON").getString());
            txtContactPerson2.setText(ObjSalesParty.getAttribute("CONTACT_PERSON_2").getString());
            txtContactPerson3.setText(ObjSalesParty.getAttribute("CONTACT_PERSON_3").getString());
            txtDesignation.setText(ObjSalesParty.getAttribute("CONT_PERS_DESIGNATION").getString());
            txtDesignation2.setText(ObjSalesParty.getAttribute("CONT_PERS_DESIGNATION_2").getString());
            txtDesignation3.setText(ObjSalesParty.getAttribute("CONT_PERS_DESIGNATION_3").getString());
            txtTranID.setText(Integer.toString(ObjSalesParty.getAttribute("TRANSPORTER_ID").getInt()));
            //txtTranName.setText((String)ObjSalesParty.getAttribute("TRANSPORTER_NAME").getObj());
            txtTranName.setText(clsSales_Party.getTransporterName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(txtTranID.getText().trim())));
            txtBankID.setText(Integer.toString(ObjSalesParty.getAttribute("BANK_ID").getInt()));
            txtBankName.setText(ObjSalesParty.getAttribute("BANK_NAME").getString());
            txtBankAdd.setText(ObjSalesParty.getAttribute("BANK_ADDRESS").getString());
            txtBankCity.setText(ObjSalesParty.getAttribute("BANK_CITY").getString());
            txtAmountLimit.setText(Double.toString(ObjSalesParty.getAttribute("AMOUNT_LIMIT").getDouble()));
            critical_limit_original = ObjSalesParty.getAttribute("AMOUNT_LIMIT").getDouble();
            txtDocThrough.setText(ObjSalesParty.getAttribute("DOCUMENT_THROUGH").getString());
            txtOtherBankAdd.setText(ObjSalesParty.getAttribute("OTHER_BANK_ADDRESS").getString());
            txtOtherBankID.setText(Integer.toString(ObjSalesParty.getAttribute("OTHER_BANK_ID").getInt()));
            txtOtherBankName.setText(ObjSalesParty.getAttribute("OTHER_BANK_NAME").getString());
            txtOtherBankCity.setText(ObjSalesParty.getAttribute("OTHER_BANK_CITY").getString());
            txtCreditDays.setText(Double.toString(ObjSalesParty.getAttribute("CREDIT_DAYS").getDouble()));
            txtExtraCreditDays.setText(Double.toString(ObjSalesParty.getAttribute("EXTRA_CREDIT_DAYS").getDouble()));
            txtGraceCreditDays.setText(Double.toString(ObjSalesParty.getAttribute("GRACE_CREDIT_DAYS").getDouble()));
            txtInsuranceCode.setText(ObjSalesParty.getAttribute("INSURANCE_CODE").getString());
            txtInterestPer.setText(Double.toString(ObjSalesParty.getAttribute("DELAY_INTEREST_PER").getDouble()));

            if(ObjSalesParty.getAttribute("PO_NO_REQUIRED").getInt()==1) {
                chkPONoRequired.setSelected(true);
            } else {
                chkPONoRequired.setSelected(false);
            }
            if(ObjSalesParty.getAttribute("KEY_CLIENT_IND").getInt()==1) {
                chkKeyClientInd.setSelected(true);
            } else {
                chkKeyClientInd.setSelected(false);
            }
           
            
            if(ObjSalesParty.getAttribute("DELIVERY_MODE").getString().equals("")) {
                cmbDeliveryMode.setSelectedIndex(0);
            } else {
                cmbDeliveryMode.setSelectedItem(ObjSalesParty.getAttribute("DELIVERY_MODE").getString());
            }
            
            txtDistanceKm.setText(Integer.toString(ObjSalesParty.getAttribute("DISTANCE_KM").getInt()));
            EITLERPGLOBAL.setComboIndex(cmbStockTagging,ObjSalesParty.getAttribute("TAGGING_APPROVAL_IND").getInt());

            EITLERPGLOBAL.setComboIndex(cmbMainCodeType, ObjSalesParty.getAttribute("MAIN_ACCOUNT_CODE").getString());
            String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
            String ChargeCode = ObjSalesParty.getAttribute("CHARGE_CODE").getString();
            String ChargeCodeII = ObjSalesParty.getAttribute("CHARGE_CODE_II").getString();

            if (type.trim().equals("210010")) {
                ChargeCodeCombos(" AND INVOICE_TYPE_ID NOT IN ('S04') ");
                EITLERPGLOBAL.setComboIndex(cmbZone, ObjSalesParty.getAttribute("ZONE").getString());
            } else {
                ChargeCodeCombos(" AND INVOICE_TYPE_ID NOT IN ('F04') ");
            }

            if ((ChargeCode.trim().equals("04")) && (type.trim().equals("210010"))) {
                EITLERPGLOBAL.setComboIndex(cmbChargeCode, "04");
            } else if ((ChargeCode.trim().equals("04")) && (!type.trim().equals("210010"))) {
                EITLERPGLOBAL.setComboIndex(cmbChargeCode, "04");
            } else {
                EITLERPGLOBAL.setComboIndex(cmbChargeCode, ObjSalesParty.getAttribute("CHARGE_CODE").getString());
            }

            if ((ChargeCodeII.trim().equals("04")) && (type.trim().equals("210010"))) {
                EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode, "04");
            } else if ((ChargeCodeII.trim().equals("04")) && (!type.trim().equals("210010"))) {
                EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode, "04");
            } else {
                EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode, ObjSalesParty.getAttribute("CHARGE_CODE_II").getString());
            }

            //EITLERPGLOBAL.setComboIndex(cmbChargeCode,ObjSalesParty.getAttribute("CHARGE_CODE").getString());
            //EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode,ObjSalesParty.getAttribute("CHARGE_CODE_II").getString());
            EITLERPGLOBAL.setComboIndex(cmbCategory, ObjSalesParty.getAttribute("CATEGORY").getString());

            EITLERPGLOBAL.setComboIndex(cmbPartyType, ObjSalesParty.getAttribute("PARTY_TYPE").getInt());

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());

            //===================Fill up Table===================//
            FormatGrid();
            GenerateGrid();

            String MainCode = ObjSalesParty.getAttribute("ACCOUNT_CODES").getString();
            if (!MainCode.trim().equals("")) {
                String sMainCode[] = MainCode.trim().split(",");

                for (int n = 0; n < sMainCode.length; n++) {
                    for (int j = 0; j < TableMainCode.getRowCount(); j++) {
                        if (TableMainCode.getValueAt(j, 2).toString().trim().equals(sMainCode[n].trim())) {
                            TableMainCode.setValueAt(Boolean.valueOf(true), j, 0);
                        }
                    }
                }
            } else {
                String str = "SELECT MAIN_ACCOUNT_CODE FROM D_FIN_PARTY_MASTER "
                        + " WHERE PARTY_CODE = '" + txtPartyCode.getText().trim() + "' AND APPROVED=1 AND CANCELLED=0";
                ResultSet rsParty = data.getResult(str, FinanceGlobal.FinURL);
                rsParty.first();
                if (rsParty.getRow() > 0) {
                    while (!rsParty.isAfterLast()) {
                        String Code = rsParty.getString("MAIN_ACCOUNT_CODE").trim();
                        for (int j = 0; j < TableMainCode.getRowCount(); j++) {
                            if (TableMainCode.getValueAt(j, 2).toString().trim().equals(Code)) {
                                TableMainCode.setValueAt(Boolean.valueOf(true), j, 0);
                            }
                        }
                        rsParty.next();
                    }
                }
            }
            //==================================================//

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String DocNo = Integer.toString((int) ObjSalesParty.getAttribute("AMEND_ID").getVal());
            List = ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, clsSalesPartyAmend.ModuleID, DocNo);
            for (int i = 1; i <= List.size(); i++) {
                clsDocFlow ObjFlow = (clsDocFlow) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];

                rowData[0] = Integer.toString(i);
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2] = (String) ObjFlow.getAttribute("STATUS").getObj();
                rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[4] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6] = (String) ObjFlow.getAttribute("REMARKS").getObj();

                DataModelA.addRow(rowData);
            }
            //============================================================//

            //Showing Audit Trial History
            FormatGridHS();
            String PartyID = Integer.toString((int) ObjSalesParty.getAttribute("AMEND_ID").getVal());
            HashMap History = clsSalesPartyAmend.getHistoryList(EITLERPGLOBAL.gCompanyID, PartyID);
            for (int i = 1; i <= History.size(); i++) {
                clsSalesPartyAmend ObjHistory = (clsSalesPartyAmend) History.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = ObjHistory.getAttribute("UPDATED_BY").getString();
                rowData[2] = ObjHistory.getAttribute("ENTRY_DATE").getString();

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

                rowData[3] = ApprovalStatus;
                rowData[4] = ObjHistory.getAttribute("APPROVER_REMARKS").getString();

                DataModelHS.addRow(rowData);
            }

            //=========================================//
            //================ Updation History ================//
            FormatGridU();

            List = clsSalesPartyAmend.getUpdationHistory(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText());

            for (int i = 1; i <= List.size(); i++) {
                clsSalesPartyAmend ObjParty = (clsSalesPartyAmend) List.get(Integer.toString(i));

                Object[] rowData = new Object[3];
                rowData[0] = Integer.toString(i);
                rowData[1] = EITLERPGLOBAL.formatDate((String) ObjParty.getAttribute("AMEND_DATE").getObj());
                rowData[2] = (String) ObjParty.getAttribute("AMEND_REASON").getObj();

                DataModelU.addRow(rowData);
            }

            //=================================================//
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Display Data Error: " + e.getMessage());
        }
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjSalesParty.setAttribute("AMEND_ID", Integer.parseInt(txtAmendID.getText()));
        ObjSalesParty.setAttribute("AMEND_DATE", EITLERPGLOBAL.formatDateDB(txtAmendDate.getText()));
        ObjSalesParty.setAttribute("AMEND_REASON", txtAmendReason.getText());
        ObjSalesParty.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
        ObjSalesParty.setAttribute("PARTY_CODE", txtPartyCode.getText());
        ObjSalesParty.setAttribute("OLD_PARTY_CODE", txtOldPartyCode.getText());
        ObjSalesParty.setAttribute("PARTY_TYPE", EITLERPGLOBAL.getComboCode(cmbPartyType));
        ObjSalesParty.setAttribute("PARTY_NAME", txtName.getText());
        ObjSalesParty.setAttribute("SEASON_CODE", txtSeasonCode.getText().trim());
        if ("".equals(EITLERPGLOBAL.formatDateDB(txtRegDate.getText()))) {
            ObjSalesParty.setAttribute("REG_DATE", "0000-00-00");
        } else {
            ObjSalesParty.setAttribute("REG_DATE", EITLERPGLOBAL.formatDateDB(txtRegDate.getText()));
        }
        ObjSalesParty.setAttribute("AREA_ID", txtAreaID.getText());
        ObjSalesParty.setAttribute("ADDRESS1", txtAdd1.getText());
        ObjSalesParty.setAttribute("ADDRESS2", txtAdd2.getText());
        ObjSalesParty.setAttribute("PINCODE", txtPIN.getText());
        ObjSalesParty.setAttribute("CITY_ID", txtCity.getText());
        ObjSalesParty.setAttribute("DISPATCH_STATION", txtDispatchStation.getText());
        ObjSalesParty.setAttribute("DISTRICT", txtState.getText());
        ObjSalesParty.setAttribute("PHONE_NO", txtPhone.getText());
        ObjSalesParty.setAttribute("MOBILE_NO", txtMobile.getText());
        ObjSalesParty.setAttribute("MOBILE_NO_2", txtMobile2.getText());
        ObjSalesParty.setAttribute("MOBILE_NO_3", txtMobile3.getText());
        ObjSalesParty.setAttribute("CORR_ADDRESS", txtCorrAdd.getText().trim());
        ObjSalesParty.setAttribute("STATE_ID", EITLERPGLOBAL.getComboCode(cmbState)); //new code by vivek on 16/09/2013 to add state & country.
        ObjSalesParty.setAttribute("COUNTRY_ID", EITLERPGLOBAL.getComboCode(cmbCountry)); //new code by vivek on 16/09/2013 to add state & country.
        ObjSalesParty.setAttribute("MARKET_SEGMENT", txtMarketSegment.getText());
        ObjSalesParty.setAttribute("PRODUCT_SEGMENT", txtProductSegment.getText());
        ObjSalesParty.setAttribute("CLIENT_CATEGORY", lblClientCategory.getText());
        ObjSalesParty.setAttribute("DESIGNER_INCHARGE", txtDesignerIncharge.getText().toUpperCase());
        ObjSalesParty.setAttribute("INCHARGE_CD", txtAreaInchargeCode.getText());
        //ObjSalesParty.setAttribute("INCHARGE_CD",txtAreaInchargeCode.getText());
        String pMainCode = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
        String pAreaCode = EITLERPGLOBAL.getCombostrCode(cmbZone);
        String pPartyCode = txtPartyCode.getText().substring(0, 3);
        String pArea = data.getStringValueFromDB("SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='FELT_AREA_CODE' AND PARA_EXT1 LIKE '%" + pPartyCode + "%' ");
        if (pMainCode.trim().equals("210010") && pAreaCode.equals("OTHER") && !pArea.equals("")) {
            ObjSalesParty.setAttribute("ZONE", pArea);
        } else {
            ObjSalesParty.setAttribute("ZONE", EITLERPGLOBAL.getCombostrCode(cmbZone));
        }
//        ObjSalesParty.setAttribute("ZONE",EITLERPGLOBAL.getCombostrCode(cmbZone));
        ObjSalesParty.setAttribute("EMAIL", txtEmail.getText());
        ObjSalesParty.setAttribute("EMAIL_ID2", txtEmail2.getText());
        ObjSalesParty.setAttribute("EMAIL_ID3", txtEmail3.getText());
        ObjSalesParty.setAttribute("URL", txtURL.getText());
        ObjSalesParty.setAttribute("CONTACT_PERSON", txtContactPerson.getText());
        ObjSalesParty.setAttribute("CONTACT_PERSON_2", txtContactPerson2.getText());
        ObjSalesParty.setAttribute("CONTACT_PERSON_3", txtContactPerson3.getText());
        ObjSalesParty.setAttribute("CONT_PERS_DESIGNATION", txtDesignation.getText());
        ObjSalesParty.setAttribute("CONT_PERS_DESIGNATION_2", txtDesignation2.getText());
        ObjSalesParty.setAttribute("CONT_PERS_DESIGNATION_3", txtDesignation3.getText());
        ObjSalesParty.setAttribute("BANK_ID", EITLERPGLOBAL.formatLNumber(txtBankID.getText()));
        ObjSalesParty.setAttribute("BANK_NAME", txtBankName.getText());
        ObjSalesParty.setAttribute("BANK_ADDRESS", txtBankAdd.getText());
        ObjSalesParty.setAttribute("BANK_CITY", txtBankCity.getText());
        ObjSalesParty.setAttribute("CHARGE_CODE", EITLERPGLOBAL.getCombostrCode(cmbChargeCode));
        ObjSalesParty.setAttribute("CHARGE_CODE_II", EITLERPGLOBAL.getCombostrCode(cmbSecondChargeCode));
        ObjSalesParty.setAttribute("CREDIT_DAYS", UtilFunctions.CDbl(txtCreditDays.getText()));
        ObjSalesParty.setAttribute("EXTRA_CREDIT_DAYS", UtilFunctions.CDbl(txtExtraCreditDays.getText()));
        ObjSalesParty.setAttribute("GRACE_CREDIT_DAYS", UtilFunctions.CDbl(txtGraceCreditDays.getText()));
        ObjSalesParty.setAttribute("DOCUMENT_THROUGH", txtDocThrough.getText());
        ObjSalesParty.setAttribute("TRANSPORTER_ID", EITLERPGLOBAL.formatLNumber(txtTranID.getText()));
        ObjSalesParty.setAttribute("TRANSPORTER_NAME", txtTranName.getText());
        ObjSalesParty.setAttribute("AMOUNT_LIMIT", UtilFunctions.CDbl(txtAmountLimit.getText()));
        ObjSalesParty.setAttribute("MAIN_ACCOUNT_CODE", EITLERPGLOBAL.getCombostrCode(cmbMainCodeType));
        ObjSalesParty.setAttribute("CATEGORY", EITLERPGLOBAL.getCombostrCode(cmbCategory));
        ObjSalesParty.setAttribute("CST_NO", txtCST.getText());
        ObjSalesParty.setAttribute("TAGGING_APPROVAL_IND",EITLERPGLOBAL.getComboCode(cmbStockTagging));

        if ("".equals(EITLERPGLOBAL.formatDateDB(txtTinDate.getText()))) {
            ObjSalesParty.setAttribute("CST_DATE", "0000-00-00");
        } else {
            ObjSalesParty.setAttribute("CST_DATE", EITLERPGLOBAL.formatDateDB(txtCSTDate.getText()));
        }

        if(chkPONoRequired.isSelected()) {
            ObjSalesParty.setAttribute("PO_NO_REQUIRED",1);
        } else {
            ObjSalesParty.setAttribute("PO_NO_REQUIRED",0);
        }

        if(chkKeyClientInd.isSelected()) {
            ObjSalesParty.setAttribute("KEY_CLIENT_IND",1);
        } else {
            ObjSalesParty.setAttribute("KEY_CLIENT_IND",0);
        }
        
        //ObjSalesParty.setAttribute("CST_DATE",EITLERPGLOBAL.formatDateDB(txtCSTDate.getText()));
        ObjSalesParty.setAttribute("GSTIN_NO", txtGSTN.getText().toUpperCase());

        if ("".equals(EITLERPGLOBAL.formatDateDB(txtGSTDate.getText()))) {
            ObjSalesParty.setAttribute("GST_DATE", "0000-00-00");
        } else {
            ObjSalesParty.setAttribute("GST_DATE", EITLERPGLOBAL.formatDateDB(txtGSTDate.getText()));
        }
        //ObjSalesParty.setAttribute("GST_DATE",EITLERPGLOBAL.formatDateDB(txtGSTDate.getText()));
        ObjSalesParty.setAttribute("ECC_NO", txtECC.getText());

        if ("".equals(EITLERPGLOBAL.formatDateDB(txtTinDate.getText()))) {
            ObjSalesParty.setAttribute("ECC_DATE", "0000-00-00");
        } else {
            ObjSalesParty.setAttribute("ECC_DATE", EITLERPGLOBAL.formatDateDB(txtECCDate.getText()));
        }

        ObjSalesParty.setAttribute("ECC_DATE", EITLERPGLOBAL.formatDateDB(txtECCDate.getText()));
        ObjSalesParty.setAttribute("TIN_NO", txtTinNo.getText());

        if ("".equals(EITLERPGLOBAL.formatDateDB(txtTinDate.getText()))) {
            ObjSalesParty.setAttribute("TIN_DATE", "0000-00-00");
        } else {
            ObjSalesParty.setAttribute("TIN_DATE", EITLERPGLOBAL.formatDateDB(txtTinDate.getText()));
        }
        //ObjSalesParty.setAttribute("TIN_DATE",EITLERPGLOBAL.formatDateDB(txtTinDate.getText()));
        ObjSalesParty.setAttribute("PAN_NO", txtPANNo.getText());

        if ("".equals(EITLERPGLOBAL.formatDateDB(txtPANDate.getText()))) {
            ObjSalesParty.setAttribute("PAN_DATE", "0000-00-00");
        } else {
            ObjSalesParty.setAttribute("PAN_DATE", EITLERPGLOBAL.formatDateDB(txtPANDate.getText()));
        }
        //ObjSalesParty.setAttribute("PAN_DATE",EITLERPGLOBAL.formatDateDB(txtPANDate.getText()));
        ObjSalesParty.setAttribute("INSURANCE_CODE", txtInsuranceCode.getText());
        ObjSalesParty.setAttribute("OTHER_BANK_ID", EITLERPGLOBAL.formatLNumber(txtOtherBankID.getText()));
        ObjSalesParty.setAttribute("OTHER_BANK_NAME", txtOtherBankName.getText());
        ObjSalesParty.setAttribute("OTHER_BANK_ADDRESS", txtOtherBankAdd.getText());
        ObjSalesParty.setAttribute("OTHER_BANK_CITY", txtOtherBankCity.getText());
        ObjSalesParty.setAttribute("REMARKS", txtRemarks.getText());
        ObjSalesParty.setAttribute("DELAY_INTEREST_PER", UtilFunctions.CDbl(txtInterestPer.getText()));
        
        ObjSalesParty.setAttribute("DELIVERY_MODE",cmbDeliveryMode.getSelectedItem().toString());

        ObjSalesParty.setAttribute("DISTANCE_KM", UtilFunctions.CInt(txtDistanceKm.getText()));

        //--------------Merge Main Code------------------------//
        int SrNo = 0;
        String MainCode = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType) + ",";

        HashMap List = new HashMap();
        String str_Condition = "";

        str_Condition = " WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " ";
        List = clsSalesPartyMainCode.getList(str_Condition);

        //Scan the Table to get selected items
        for (int i = 1; i <= List.size(); i++) {
            clsSalesPartyMainCode ObjType = (clsSalesPartyMainCode) List.get(Integer.toString(i));
            SrNo = i;

            //Search in the table for SrNo.
            for (int j = 0; j < TableMainCode.getRowCount(); j++) {
                if (Integer.parseInt(TableMainCode.getValueAt(j, 1).toString()) == SrNo) {
                    if (TableMainCode.getValueAt(j, 0).toString().equals("true")) {
                        //Selected Item
                        MainCode = MainCode + ObjType.getAttribute("MAIN_ACCOUNT_CODE").getString() + ",";
                    }
                }
            }
        }
        ObjSalesParty.setAttribute("ACCOUNT_CODES", MainCode);
        //-----------------------------------------------------//

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
        TableA.getColumnModel().getColumn(0).setCellRenderer(Paint);
        Paint.setColor(1, 1, Color.CYAN);

    }

    private void SetMenuForRights() {
        // --- Add Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1152, 11521)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1152, 11522)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1152, 11523)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1152, 11524)) {
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

                if (FieldName.trim().equals("PARTY_CODE")) {
                    int a = 0;
                }
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {

                    Tab1.getComponent(i).setEnabled(true);
                }

            }
        }
        //=============== Header Fields Setup Complete =================//

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
        ObjSalesParty.Filter(" WHERE AMEND_ID IN (SELECT D_SAL_PARTY_AMEND_MASTER.AMEND_ID FROM D_SAL_PARTY_AMEND_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_AMEND_MASTER.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND D_COM_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=" + clsSalesPartyAmend.ModuleID + ")");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, int pAmendID) {
        ObjSalesParty.Filter(" WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND AMEND_ID=" + pAmendID);
        ObjSalesParty.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        //String PartyCode=txtPartyCode.getText();

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
                    IncludeUser = ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, clsSalesPartyAmend.ModuleID, txtAmendID.getText().trim(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, clsSalesPartyAmend.ModuleID, txtAmendID.getText().trim(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String DocNo = Long.toString((long) ObjSalesParty.getAttribute("AMEND_ID").getVal());
            int Creator = ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, clsSalesPartyAmend.ModuleID, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    private void FormatGridU() {
        DataModelU = new EITLTableModel();

        TableU.removeAll();
        TableU.setModel(DataModelU);

        //Set the table Readonly
        DataModelU.TableReadOnly(true);

        //Add the columns
        DataModelU.addColumn("Sr.");
        DataModelU.addColumn("Date");
        DataModelU.addColumn("Amendment Reason");

        TableU.setAutoResizeMode(TableA.AUTO_RESIZE_OFF);

    }

    private void FormatGrid() {
        DataModelMainCode = new EITLTableModel();

        TableMainCode.removeAll();

        TableMainCode.setModel(DataModelMainCode);
        TableMainCode.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 1; i <= 4; i++) {
            DataModelMainCode.SetReadOnly(i);
        }

        //Add Columns to it
        DataModelMainCode.addColumn("*"); //0 Selection
        DataModelMainCode.addColumn("Sr.");//1
        DataModelMainCode.addColumn("Main Account Code");//2
        DataModelMainCode.addColumn("Account Name");//3

        Rend.setCustomComponent(0, "CheckBox");
        TableMainCode.getColumnModel().getColumn(0).setCellRenderer(Rend);
        TableMainCode.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        TableMainCode.getColumnModel().getColumn(3).setPreferredWidth(270);
    }

    private void GenerateGrid() {
        HashMap List = new HashMap();
        String str_Condition = "";

        str_Condition = " WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " ";
        List = clsSalesPartyMainCode.getList(str_Condition);

        for (int i = 1; i <= List.size(); i++) {
            clsSalesPartyMainCode ObjType = (clsSalesPartyMainCode) List.get(Integer.toString(i));
            HashMap colLot = ObjType.colTypeLot;

            Object[] rowData = new Object[4];

            rowData[0] = Boolean.valueOf(false); //By default not selected
            rowData[1] = Integer.toString(i);
            rowData[2] = ObjType.getAttribute("MAIN_ACCOUNT_CODE").getString();
            rowData[3] = ObjType.getAttribute("ACCOUNT_NAME").getString();

            DataModelMainCode.addRow(rowData);

            //Set the Collection
            DataModelMainCode.SetUserObject(TableMainCode.getRowCount() - 1, colLot);
        }
    }

    //Generates Encharge Name Combo Box
    private void GenerateZoneCombo() {
        HashMap zoneList = new HashMap();

        cmbZoneModel = new EITLComboModel();
        cmbZone.removeAllItems();
        cmbZone.setModel(cmbZoneModel);

        ComboData aData = new ComboData();
        aData.strCode = "ZONE";
        aData.Text = "ZONE";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "WEST";
        aData.Text = "WEST";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "SOUTH";
        aData.Text = "SOUTH";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "NORTH";
        aData.Text = "NORTH";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "EAST";
        aData.Text = "EAST";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "EXPORT";
        aData.Text = "EXPORT";
        cmbZoneModel.addElement(aData);

    }

    private void GenerateStateCombo() {
        HashMap stateList = new HashMap();

        cmbStateModel = new EITLComboModel();
        cmbState.removeAllItems();
        cmbState.setModel(cmbStateModel);

        stateList = clsSales_Party.getStateNameList();
        for (int i = 1; i <= stateList.size(); i++) {
            cmbStateModel.addElement((ComboData) stateList.get(new Integer(i)));
        }
    }

    private void GenerateCountryCombo() {
        HashMap countryList = new HashMap();

        cmbCountryModel = new EITLComboModel();
        cmbCountry.removeAllItems();
        cmbCountry.setModel(cmbCountryModel);

        countryList = clsSales_Party.getCountryNameList();
        for (int i = 1; i <= countryList.size(); i++) {
            cmbCountryModel.addElement((ComboData) countryList.get(new Integer(i)));
        }
    }

    private void GenerateAreaCombo() {
        HashMap zoneList = new HashMap();

        cmbZoneModel = new EITLComboModel();
        cmbZone.removeAllItems();
        cmbZone.setModel(cmbZoneModel);

        ComboData aData = new ComboData();
        aData.strCode = "SELECT AREA CODE";
        aData.Text = "SELECT AREA CODE";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "KEY CLIENT";
        aData.Text = "KEY CLIENT";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "ACNE";
        aData.Text = "ACNE";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "EXPORT";
        aData.Text = "EXPORT";
        cmbZoneModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "OTHER";
        aData.Text = "OTHER";
        cmbZoneModel.addElement(aData);

    }

    private void checkControl() {
        String type = EITLERPGLOBAL.getCombostrCode(cmbMainCodeType);
        System.out.println(type);
        if (type.trim().equals("210010")) {
            if (EITLERPGLOBAL.gUserID == 9) {
                clsSales_Party ObjPartytemp = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText(), type);

                String ChargeCodeMaster = ObjPartytemp.getAttribute("CHARGE_CODE").getString();
                String ChargeCodeIIMaster = ObjPartytemp.getAttribute("CHARGE_CODE_II").getString();
                double CreditDaysMaster = ObjPartytemp.getAttribute("CREDIT_DAYS").getDouble();
                double ExtraCreditDaysMaster = ObjPartytemp.getAttribute("EXTRA_CREDIT_DAYS").getDouble();
                double GraceCreditDaysMaster = ObjPartytemp.getAttribute("GRACE_CREDIT_DAYS").getDouble();
                int BankIDMaster = ObjPartytemp.getAttribute("BANK_ID").getInt();
                int OtherBankIDMaster = ObjPartytemp.getAttribute("OTHER_BANK_ID").getInt();
                double CriticalLimitMaster = ObjPartytemp.getAttribute("AMOUNT_LIMIT").getDouble();
                double DelayIntPerMaster = ObjPartytemp.getAttribute("DELAY_INTEREST_PER").getDouble();
                String AreaInchargeCodeMaster = ObjPartytemp.getAttribute("INCHARGE_CD").getString();
                String ZoneMaster = ObjPartytemp.getAttribute("ZONE").getString();
                String ClientCatagoryMaster = ObjPartytemp.getAttribute("CLIENT_CATEGORY").getString();

                //ADDED BY GAURANG ON 17/10/2018
                if (!ChargeCodeMaster.equals(EITLERPGLOBAL.getCombostrCode(cmbChargeCode))) {
                    OpgFinal.setEnabled(false);
                } else if (CreditDaysMaster != UtilFunctions.CDbl(txtCreditDays.getText())) {
                    OpgFinal.setEnabled(false);
                } else if (ExtraCreditDaysMaster != UtilFunctions.CDbl(txtExtraCreditDays.getText())) {
                    OpgFinal.setEnabled(false);
                } else if (GraceCreditDaysMaster != UtilFunctions.CDbl(txtGraceCreditDays.getText())) {
                    OpgFinal.setEnabled(false);
                } else if (CriticalLimitMaster != UtilFunctions.CDbl(txtAmountLimit.getText())) {
                    OpgFinal.setEnabled(false);
                } else if (DelayIntPerMaster != UtilFunctions.CDbl(txtInterestPer.getText())) {
                    OpgFinal.setEnabled(false);
                } else {
                    OpgApprove.setEnabled(false);
                }

            }

        }
    }
    
}
