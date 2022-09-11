/*
 * frmTemplate.java
 *
 * Created on April 7, 2004, 3:10 PM
 */
package EITLERP.Purchase;

/**
 *
 * @author nhpatel
 */
/*<APPLET CODE=frmPOAmendGen.class HEIGHT=534 WIDTH=764></APPLET>*/
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
public class frmPOAmendGen extends javax.swing.JApplet {

    private int EditMode = 0;

    private EITLTableModel DataModelH = new EITLTableModel();
    private EITLTableModel DataModelL = new EITLTableModel();
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer ColorRenderer = new EITLTableCellRenderer();
    private EITLTableModel DataModelA = new EITLTableModel();

    private EITLTableModel DataModelD = new EITLTableModel();
    private EITLTableModel DataModelP = new EITLTableModel();
    private EITLTableModel DataModelO = new EITLTableModel();
    private EITLTableModel DataModelS = new EITLTableModel();

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    clsTaxColumn ObjTax = new clsTaxColumn();
    clsColumn ObjColumn = new clsColumn();

    private JEP myParser = new JEP();
    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;
    private String FormTitle = "";

    private clsPOAmendGen ObjPOAmend;

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix

    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbCurrencyModel;
    private EITLComboModel cmbStatusModel;
    private EITLComboModel cmbBuyerModel;
    private EITLComboModel cmbShipModel;
    private EITLComboModel cmbTrasnportModel;

    public int POType = 1; //PO Amendment Type

    private boolean HistoryView = false;
    private String theDocNo = "";
    private EITLTableModel DataModelHS;

    public frmPendingApprovals frmPA;

    String cellLastValueL = "";
    String cellLastValueH = "";
    String prevValue = "";
    private int TermCode = 0;
    private int TermDays = 0;
    private JFileChooser fc = new JFileChooser();

    public frmPOAmendGen() {
        //Nothing to do
    }

    /*public frmPOAmendGen(int pType) {
     
     System.gc();
     
     POType=pType;
     
     setSize(764,534);
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
     
     //As per the PO Type, Display appropriate titles
     switch(POType) {
     case 1:
     lblTitle.setText("PURCHASE ORDER AMENDMENT (Gen.)");break;
     case 2:
     lblTitle.setText("PURCHASE ORDER AMENDMENT (Gen. without indent)");break;
     case 3:
     lblTitle.setText("PURCHASE ORDER AMENDMENT (A Class)");break;
     case 4:
     lblTitle.setText("PURCHASE ORDER AMENDMENT (Raw Material)");break;
     case 5:
     lblTitle.setText("PURCHASE ORDER AMENDMENT (Spares)");break;
     case 6:
     lblTitle.setText("PURCHASE ORDER AMENDMENT (Capital Goods)");break;
     case 7:
     lblTitle.setText("PURCHASE ORDER AMENDMENT (Contract)");break;
     }
     
     ObjTax.LoadData((int)EITLERPGLOBAL.gCompanyID);
     ObjColumn.LoadData((int)EITLERPGLOBAL.gCompanyID);
     
     FormatGrid();
     FormatGrid_H();
     SetNumberFormats();
     
     GenerateCombos();
     ObjPOAmend=new clsPOAmendGen();
     
     if(ObjPOAmend.LoadData(EITLERPGLOBAL.gCompanyID,POType)) {
     ObjPOAmend.MoveLast();
     DisplayData();
     SetMenuForRights();
     }
     else {
     JOptionPane.showMessageDialog(null,"Error occured while loading data. Error is "+ObjPOAmend.LastError);
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
    /**
     * Creates new form frmTemplate
     */
    public void init() {

        setSize(764, 534);
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

        //As per the PO Type, Display appropriate titles
        switch (POType) {
            case 1:
                FormTitle = "PURCHASE ORDER AMENDMENT (General)";
                break;
            case 2:
                FormTitle = "PURCHASE ORDER AMENDMENT (Without Indent)";
                break;
            case 3:
                FormTitle = "PURCHASE ORDER AMENDMENT (A Class)";
                break;
            case 4:
                FormTitle = "PURCHASE ORDER AMENDMENT (Raw Material)";
                break;
            case 5:
                FormTitle = "PURCHASE ORDER AMENDMENT (Spares)";
                break;
            case 6:
                FormTitle = "PURCHASE ORDER AMENDMENT (Capital Goods)";
                break;
            case 7:
                FormTitle = "PURCHASE ORDER AMENDMENT (Contract)";
                break;
            case 9:
                FormTitle = "PURCHASE ORDER AMENDMENT (Merchandise)";
                break;
        }

        lblTitle.setText(FormTitle);

        ObjTax.LoadData((int) EITLERPGLOBAL.gCompanyID);
        ObjColumn.LoadData((int) EITLERPGLOBAL.gCompanyID);

        FormatGrid();
        FormatGrid_H();
        SetNumberFormats();

        GenerateCombos();
        ObjPOAmend = new clsPOAmendGen();

        SetMenuForRights();

        if (getName().equals("Link")) {

        } else {
            if (ObjPOAmend.LoadData(EITLERPGLOBAL.gCompanyID, POType)) {
                ObjPOAmend.MoveFirst();
                DisplayData();

            } else {
                JOptionPane.showMessageDialog(null, "Error occured while loading data. Error is " + ObjPOAmend.LastError);
            }
        }

        //Import Spares PO. Show the Import License No.
        if (POType == 5) {
            lblImport.setVisible(true);
            txtImportLicense.setVisible(true);
            cmdImportBig.setVisible(true);
        } else {
            lblImport.setVisible(false);
            txtImportLicense.setVisible(false);
            cmdImportBig.setVisible(false);
        }

        //Specifications - Show the Specification Grid in case of A Class P.O.
        if (POType == 3 || POType == 4) {
            lblSpecs.setVisible(true);
            frameSpecs.setVisible(true);
            cmdSpecAdd.setVisible(true);
            cmdSpecRemove.setVisible(true);
        } else {
            lblSpecs.setVisible(false);
            frameSpecs.setVisible(false);
            cmdSpecAdd.setVisible(false);
            cmdSpecRemove.setVisible(false);
        }

        if (POType == 4 || POType == 7) {
            chkImported.setVisible(true);
            txtImportLicense.setVisible(true);
            cmdImportBig.setVisible(true);
        } else {
            chkImported.setVisible(false);
            txtImportLicense.setVisible(false);
            cmdImportBig.setVisible(false);
        }

        txtAuditRemarks.setVisible(false);

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
        mnuGST = new javax.swing.JMenuItem();
        mnuNonGST = new javax.swing.JMenuItem();
        mnuTRN = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        mnuGST1 = new javax.swing.JMenuItem();
        mnuNonGST1 = new javax.swing.JMenuItem();
        mnuTRN1 = new javax.swing.JMenuItem();
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
        cmdEMail = new javax.swing.JButton();
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
        jLabel5 = new javax.swing.JLabel();
        txtReference = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtRefA = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtRefB = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtQuotationNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtQuotationDate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbBuyer = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        txtInquiryNo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtInquiryDate = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtPurpose = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtSubject = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cmbCurrency = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        chkCancelled = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        cmbShip = new javax.swing.JComboBox();
        txtAdd1 = new javax.swing.JTextField();
        txtAdd2 = new javax.swing.JTextField();
        txtAdd3 = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtState = new javax.swing.JTextField();
        cmdNext1 = new javax.swing.JButton();
        txtCurrencyRate = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtReasonCode = new javax.swing.JTextField();
        txtAmendReason = new javax.swing.JTextField();
        chkImportConcess = new javax.swing.JCheckBox();
        lblRevNo = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        cmbTransportMode = new javax.swing.JComboBox();
        cmdPurposeBig = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        chkImported = new javax.swing.JCheckBox();
        lblURD = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        txtDeptID = new javax.swing.JTextField();
        lblDeptName = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        Tab2 = new javax.swing.JPanel();
        cmdNext2 = new javax.swing.JButton();
        cmdBack2 = new javax.swing.JButton();
        lblSpecs = new javax.swing.JLabel();
        frameSpecs = new javax.swing.JScrollPane();
        TableS = new javax.swing.JTable();
        cmdSpecAdd = new javax.swing.JButton();
        cmdSpecRemove = new javax.swing.JButton();
        lblImport = new javax.swing.JLabel();
        txtImportLicense = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        txtLine1Code = new javax.swing.JTextField();
        txtLine1 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtLine2Code = new javax.swing.JTextField();
        txtLine2 = new javax.swing.JTextField();
        txtRemarks = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        cmdImportBig = new javax.swing.JButton();
        cmdLine1Big = new javax.swing.JButton();
        cmdLine2Big = new javax.swing.JButton();
        cmdRemarksBig = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtPaymentTerm = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtPriceBasisTerm = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtDiscountTerm = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtExciseTerm = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtSTTerm = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtPFTerm = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtFreightTerm = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtOctroiTerm = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtFOBTerm = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtCIETerm = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtInsuranceTerm = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtTCCTerm = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtCenvatTerm = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDespatchTerm = new javax.swing.JTextArea();
        cmdSelect = new javax.swing.JButton();
        txtServiceTax = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        txtOthersTerm = new javax.swing.JTextField();
        Tab6 = new javax.swing.JPanel();
        cmdNext6 = new javax.swing.JButton();
        cmdBack5 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        txtCGSTTerm = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txtSGSTTerm = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtGSTCompCessTerm = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtIGSTTerm = new javax.swing.JTextField();
        txtCompositionTerm = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        txtRCMTerm = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        Tab3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableL = new javax.swing.JTable();
        HeaderPane = new javax.swing.JScrollPane();
        TableH = new javax.swing.JTable();
        cmdInsert = new javax.swing.JButton();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        cmdNext3 = new javax.swing.JButton();
        cmdBack3 = new javax.swing.JButton();
        txtGrossAmount = new javax.swing.JTextField();
        txtNetAmount = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdInsertQuot = new javax.swing.JButton();
        cmdShowIndent = new javax.swing.JButton();
        cmdShowQuot = new javax.swing.JButton();
        cmdShowPO = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        txtFinalAmount = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        chkAttachement = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        txtFile = new javax.swing.JTextField();
        cmdBrowse = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        cmdShowFile = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtFileText = new javax.swing.JTextArea();
        chkDoNotErase = new javax.swing.JCheckBox();
        cmdSaveAs = new javax.swing.JButton();
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
        txtFromRemarksBig = new javax.swing.JButton();
        Tab5 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        lblDocumentHistory = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableHS = new javax.swing.JTable();
        cmdNormalView = new javax.swing.JButton();
        cmdViewHistory = new javax.swing.JButton();
        cmdPreviewA = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

        mnuGST.setText("GST");
        mnuGST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGSTActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnuGST);

        mnuNonGST.setText("Non GST");
        mnuNonGST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNonGSTActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnuNonGST);

        mnuTRN.setText("Transaction Phase");
        mnuTRN.setToolTipText("");
        mnuTRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTRNActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnuTRN);

        mnuGST1.setText("GST");
        mnuGST1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGST1ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(mnuGST1);

        mnuNonGST1.setText("Non GST");
        mnuNonGST1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNonGST1ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(mnuNonGST1);

        mnuTRN1.setText("Transaction Phase");
        mnuTRN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTRN1ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(mnuTRN1);

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

        cmdFilter.setToolTipText("Find");
        cmdFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFilterActionPerformed(evt);
            }
        });
        ToolBar.add(cmdFilter);

        cmdPreview.setToolTipText("Preview Record");
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

        cmdExit.setToolTipText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        ToolBar.add(cmdExit);

        cmdEMail.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        cmdEMail.setText("EMail");
        cmdEMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEMailActionPerformed(evt);
            }
        });
        ToolBar.add(cmdEMail);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText(" PURCHASE ORDER AMENDMENT (General)");
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

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("P.O. No. :");
        Tab1.add(jLabel2);
        jLabel2.setBounds(5, 18, 110, 15);

        txtPONo.setEditable(false);
        txtPONo.setBackground(new java.awt.Color(204, 204, 255));
        Tab1.add(txtPONo);
        txtPONo.setBounds(120, 14, 101, 19);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Date :");
        Tab1.add(jLabel3);
        jLabel3.setBounds(260, 18, 40, 15);

        txtPODate.setEnabled(false);
        txtPODate.setNextFocusableComponent(txtSuppCode);
        Tab1.add(txtPODate);
        txtPODate.setBounds(305, 16, 100, 19);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Supplier :");
        Tab1.add(jLabel4);
        jLabel4.setBounds(5, 72, 110, 15);

        txtSuppCode.setNextFocusableComponent(txtQuotationNo);
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
        txtSuppCode.setBounds(120, 68, 62, 19);

        txtSuppName.setEditable(false);
        Tab1.add(txtSuppName);
        txtSuppName.setBounds(190, 68, 212, 19);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Summary Ref. :");
        Tab1.add(jLabel5);
        jLabel5.setBounds(5, 313, 110, 15);

        txtReference.setName("PO_REF"); // NOI18N
        txtReference.setNextFocusableComponent(txtReasonCode);
        txtReference.setEnabled(false);
        txtReference.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtReferenceFocusGained(evt);
            }
        });
        Tab1.add(txtReference);
        txtReference.setBounds(120, 309, 290, 19);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Last P.O. :");
        Tab1.add(jLabel7);
        jLabel7.setBounds(425, 40, 60, 15);

        txtRefA.setName("REF_A"); // NOI18N
        txtRefA.setNextFocusableComponent(txtRefB);
        txtRefA.setEnabled(false);
        txtRefA.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRefAFocusGained(evt);
            }
        });
        Tab1.add(txtRefA);
        txtRefA.setBounds(490, 38, 227, 19);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("RA :");
        Tab1.add(jLabel8);
        jLabel8.setBounds(425, 67, 60, 15);

        txtRefB.setName("REF_B"); // NOI18N
        txtRefB.setNextFocusableComponent(cmbCurrency);
        txtRefB.setEnabled(false);
        txtRefB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRefBFocusGained(evt);
            }
        });
        Tab1.add(txtRefB);
        txtRefB.setBounds(490, 64, 227, 19);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Quotation No. :");
        Tab1.add(jLabel9);
        jLabel9.setBounds(5, 122, 110, 15);

        txtQuotationNo.setName("QUOTATION_NO"); // NOI18N
        txtQuotationNo.setNextFocusableComponent(txtQuotationDate);
        txtQuotationNo.setEnabled(false);
        txtQuotationNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQuotationNoFocusGained(evt);
            }
        });
        Tab1.add(txtQuotationNo);
        txtQuotationNo.setBounds(120, 120, 114, 19);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Date :");
        Tab1.add(jLabel10);
        jLabel10.setBounds(260, 123, 40, 15);

        txtQuotationDate.setName("QUOTATION_DATE"); // NOI18N
        txtQuotationDate.setNextFocusableComponent(txtInquiryNo);
        txtQuotationDate.setEnabled(false);
        txtQuotationDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQuotationDateFocusGained(evt);
            }
        });
        Tab1.add(txtQuotationDate);
        txtQuotationDate.setBounds(305, 120, 100, 19);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Buyer :");
        Tab1.add(jLabel11);
        jLabel11.setBounds(5, 180, 110, 16);

        cmbBuyer.setName("BUYER"); // NOI18N
        cmbBuyer.setNextFocusableComponent(cmbTransportMode);
        cmbBuyer.setEnabled(false);
        cmbBuyer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbBuyerFocusGained(evt);
            }
        });
        Tab1.add(cmbBuyer);
        cmbBuyer.setBounds(120, 176, 180, 24);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Inquiry No. :");
        Tab1.add(jLabel12);
        jLabel12.setBounds(5, 146, 110, 15);

        txtInquiryNo.setName("INQUIRY_NO"); // NOI18N
        txtInquiryNo.setNextFocusableComponent(txtInquiryDate);
        txtInquiryNo.setEnabled(false);
        txtInquiryNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInquiryNoFocusGained(evt);
            }
        });
        Tab1.add(txtInquiryNo);
        txtInquiryNo.setBounds(120, 145, 114, 19);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Date :");
        Tab1.add(jLabel15);
        jLabel15.setBounds(260, 147, 40, 15);

        txtInquiryDate.setName("INQUIRY_DATE"); // NOI18N
        txtInquiryDate.setNextFocusableComponent(cmbBuyer);
        txtInquiryDate.setEnabled(false);
        txtInquiryDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInquiryDateFocusGained(evt);
            }
        });
        Tab1.add(txtInquiryDate);
        txtInquiryDate.setBounds(305, 145, 100, 19);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Purpose :");
        Tab1.add(jLabel40);
        jLabel40.setBounds(5, 253, 110, 15);

        txtPurpose.setName("PURPOSE"); // NOI18N
        txtPurpose.setNextFocusableComponent(txtSubject);
        txtPurpose.setEnabled(false);
        txtPurpose.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPurposeFocusGained(evt);
            }
        });
        Tab1.add(txtPurpose);
        txtPurpose.setBounds(120, 249, 290, 19);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Subject :");
        Tab1.add(jLabel41);
        jLabel41.setBounds(5, 283, 110, 15);

        txtSubject.setName("SUBJECT"); // NOI18N
        txtSubject.setNextFocusableComponent(txtReference);
        txtSubject.setEnabled(false);
        txtSubject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSubjectFocusGained(evt);
            }
        });
        Tab1.add(txtSubject);
        txtSubject.setBounds(120, 279, 290, 19);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Amendment No. :");
        Tab1.add(jLabel42);
        jLabel42.setBounds(5, 43, 110, 15);

        txtDocNo.setBackground(new java.awt.Color(204, 204, 255));
        txtDocNo.setEditable(false);
        Tab1.add(txtDocNo);
        txtDocNo.setBounds(120, 39, 100, 19);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Date :");
        Tab1.add(jLabel43);
        jLabel43.setBounds(260, 43, 40, 15);

        txtDocDate.setEnabled(false);
        txtDocDate.setName("AMEND_DATE"); // NOI18N
        txtDocDate.setNextFocusableComponent(txtSuppCode);
        txtDocDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDocDateFocusGained(evt);
            }
        });
        Tab1.add(txtDocDate);
        txtDocDate.setBounds(305, 41, 100, 19);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Currency :");
        Tab1.add(jLabel13);
        jLabel13.setBounds(410, 101, 70, 15);

        cmbCurrency.setName("CURRENCY_ID"); // NOI18N
        cmbCurrency.setNextFocusableComponent(txtCurrencyRate);
        cmbCurrency.setEnabled(false);
        cmbCurrency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbCurrencyFocusGained(evt);
            }
        });
        Tab1.add(cmbCurrency);
        cmbCurrency.setBounds(485, 99, 134, 24);

        jLabel14.setText("Rate :");
        Tab1.add(jLabel14);
        jLabel14.setBounds(625, 103, 34, 15);

        chkCancelled.setText("Cancelled");
        chkCancelled.setEnabled(false);
        chkCancelled.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkCancelledFocusGained(evt);
            }
        });
        Tab1.add(chkCancelled);
        chkCancelled.setBounds(656, 6, 94, 23);

        jLabel21.setText("Shipment Address :");
        Tab1.add(jLabel21);
        jLabel21.setBounds(460, 198, 130, 15);

        cmbShip.setName("SHIP_ID"); // NOI18N
        cmbShip.setNextFocusableComponent(cmdNext1);
        cmbShip.setEnabled(false);
        cmbShip.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbShipItemStateChanged(evt);
            }
        });
        cmbShip.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbShipFocusGained(evt);
            }
        });
        Tab1.add(cmbShip);
        cmbShip.setBounds(460, 216, 254, 24);

        txtAdd1.setEditable(false);
        txtAdd1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtAdd1.setEnabled(false);
        Tab1.add(txtAdd1);
        txtAdd1.setBounds(460, 240, 256, 20);

        txtAdd2.setEditable(false);
        txtAdd2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtAdd2.setEnabled(false);
        Tab1.add(txtAdd2);
        txtAdd2.setBounds(460, 260, 256, 20);

        txtAdd3.setEditable(false);
        txtAdd3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtAdd3.setEnabled(false);
        Tab1.add(txtAdd3);
        txtAdd3.setBounds(460, 280, 256, 20);

        txtCity.setEditable(false);
        txtCity.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCity.setEnabled(false);
        Tab1.add(txtCity);
        txtCity.setBounds(460, 300, 256, 20);

        txtState.setEditable(false);
        txtState.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtState.setEnabled(false);
        Tab1.add(txtState);
        txtState.setBounds(460, 320, 256, 20);

        cmdNext1.setText("Next >>");
        cmdNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNext1);
        cmdNext1.setBounds(638, 343, 102, 25);

        txtCurrencyRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCurrencyRate.setName("CURRENCY_RATE"); // NOI18N
        txtCurrencyRate.setNextFocusableComponent(chkAttachement);
        txtCurrencyRate.setEnabled(false);
        Tab1.add(txtCurrencyRate);
        txtCurrencyRate.setBounds(661, 99, 76, 21);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Amend. Reason :");
        Tab1.add(jLabel44);
        jLabel44.setBounds(5, 340, 110, 15);

        txtReasonCode.setName("REASON_CODE"); // NOI18N
        txtReasonCode.setNextFocusableComponent(txtAmendReason);
        txtReasonCode.setEnabled(false);
        txtReasonCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtReasonCodeFocusGained(evt);
            }
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
        txtReasonCode.setBounds(120, 338, 50, 19);

        txtAmendReason.setNextFocusableComponent(chkImportConcess);
        txtAmendReason.setEnabled(false);
        txtAmendReason.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAmendReasonFocusGained(evt);
            }
        });
        Tab1.add(txtAmendReason);
        txtAmendReason.setBounds(175, 338, 235, 19);

        chkImportConcess.setText("Import Concessional");
        chkImportConcess.setName("IMPORT_CONCESS"); // NOI18N
        chkImportConcess.setNextFocusableComponent(txtRefA);
        chkImportConcess.setEnabled(false);
        Tab1.add(chkImportConcess);
        chkImportConcess.setBounds(484, 8, 182, 23);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(225, 40, 30, 15);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Transport Mode :");
        Tab1.add(jLabel46);
        jLabel46.setBounds(5, 211, 110, 20);

        cmbTransportMode.setName("TRANSPORT_MODE"); // NOI18N
        cmbTransportMode.setEnabled(false);
        Tab1.add(cmbTransportMode);
        cmbTransportMode.setBounds(120, 210, 308, 24);

        cmdPurposeBig.setText("jButton1");
        cmdPurposeBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPurposeBigActionPerformed(evt);
            }
        });
        Tab1.add(cmdPurposeBig);
        cmdPurposeBig.setBounds(415, 250, 33, 19);

        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(415, 280, 34, 19);

        chkImported.setText("Imported Raw Material");
        chkImported.setEnabled(false);
        Tab1.add(chkImported);
        chkImported.setBounds(490, 170, 179, 23);

        lblURD.setBackground(new java.awt.Color(204, 204, 255));
        lblURD.setText("URD Purchase");
        lblURD.setOpaque(true);
        Tab1.add(lblURD);
        lblURD.setBounds(268, 91, 121, 18);

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Tab1.add(jButton2);
        jButton2.setBounds(415, 338, 34, 19);

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

        lblSpecs.setText("Specifications :");
        Tab2.add(lblSpecs);
        lblSpecs.setBounds(16, 14, 160, 15);

        TableS.setModel(new javax.swing.table.DefaultTableModel(
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
        frameSpecs.setViewportView(TableS);

        Tab2.add(frameSpecs);
        frameSpecs.setBounds(14, 34, 440, 118);

        cmdSpecAdd.setText("Add");
        cmdSpecAdd.setName("OTHER_TERMS"); // NOI18N
        cmdSpecAdd.setEnabled(false);
        Tab2.add(cmdSpecAdd);
        cmdSpecAdd.setBounds(274, 158, 74, 19);

        cmdSpecRemove.setText("Remove");
        cmdSpecRemove.setName("OTHER_TERMS"); // NOI18N
        cmdSpecRemove.setEnabled(false);
        Tab2.add(cmdSpecRemove);
        cmdSpecRemove.setBounds(354, 158, 96, 19);

        lblImport.setText("Import License Details");
        Tab2.add(lblImport);
        lblImport.setBounds(22, 204, 142, 15);

        txtImportLicense.setName("REMARKS"); // NOI18N
        txtImportLicense.setEnabled(false);
        Tab2.add(txtImportLicense);
        txtImportLicense.setBounds(35, 222, 505, 19);

        jLabel47.setText("Line 1");
        Tab2.add(jLabel47);
        jLabel47.setBounds(16, 264, 42, 15);

        txtLine1Code.setName("PRINT_LINE_1"); // NOI18N
        txtLine1Code.setEnabled(false);
        Tab2.add(txtLine1Code);
        txtLine1Code.setBounds(70, 260, 55, 19);

        txtLine1.setEnabled(false);
        Tab2.add(txtLine1);
        txtLine1.setBounds(130, 260, 410, 19);

        jLabel48.setText("Line 2");
        Tab2.add(jLabel48);
        jLabel48.setBounds(18, 290, 42, 15);

        txtLine2Code.setName("PRINT_LINE_2"); // NOI18N
        txtLine2Code.setEnabled(false);
        Tab2.add(txtLine2Code);
        txtLine2Code.setBounds(70, 288, 55, 19);

        txtLine2.setEnabled(false);
        Tab2.add(txtLine2);
        txtLine2.setBounds(130, 288, 410, 19);

        txtRemarks.setName("REMARKS"); // NOI18N
        txtRemarks.setEnabled(false);
        Tab2.add(txtRemarks);
        txtRemarks.setBounds(75, 320, 465, 19);

        jLabel39.setText("Remarks");
        Tab2.add(jLabel39);
        jLabel39.setBounds(6, 322, 58, 15);

        cmdImportBig.setText("...");
        cmdImportBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdImportBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdImportBig);
        cmdImportBig.setBounds(550, 221, 38, 19);

        cmdLine1Big.setText("...");
        cmdLine1Big.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLine1BigActionPerformed(evt);
            }
        });
        Tab2.add(cmdLine1Big);
        cmdLine1Big.setBounds(550, 259, 38, 19);

        cmdLine2Big.setText("...");
        cmdLine2Big.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLine2BigActionPerformed(evt);
            }
        });
        Tab2.add(cmdLine2Big);
        cmdLine2Big.setBounds(550, 287, 38, 19);

        cmdRemarksBig.setText("...");
        cmdRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdRemarksBig);
        cmdRemarksBig.setBounds(550, 320, 38, 19);

        Tab.addTab("Other Information", Tab2);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(null);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Payment Terms :");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(5, 14, 105, 15);

        txtPaymentTerm.setName("PAYMENT_TERM"); // NOI18N
        txtPaymentTerm.setNextFocusableComponent(txtPriceBasisTerm);
        txtPaymentTerm.setEnabled(false);
        jPanel2.add(txtPaymentTerm);
        txtPaymentTerm.setBounds(115, 10, 370, 19);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Price Basis :");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(5, 42, 105, 15);

        txtPriceBasisTerm.setName("PRICE_BASIS_TERM"); // NOI18N
        txtPriceBasisTerm.setNextFocusableComponent(txtDiscountTerm);
        txtPriceBasisTerm.setEnabled(false);
        txtPriceBasisTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPriceBasisTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtPriceBasisTerm);
        txtPriceBasisTerm.setBounds(115, 38, 205, 19);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Discount :");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(330, 40, 95, 15);

        txtDiscountTerm.setName("DISCOUNT_TERM"); // NOI18N
        txtDiscountTerm.setNextFocusableComponent(txtExciseTerm);
        txtDiscountTerm.setEnabled(false);
        txtDiscountTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscountTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtDiscountTerm);
        txtDiscountTerm.setBounds(435, 38, 205, 19);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Excise :");
        jPanel2.add(jLabel23);
        jLabel23.setBounds(5, 68, 105, 15);

        txtExciseTerm.setEnabled(false);
        txtExciseTerm.setName("EXCISE_TERM"); // NOI18N
        txtExciseTerm.setNextFocusableComponent(txtSTTerm);
        txtExciseTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExciseTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtExciseTerm);
        txtExciseTerm.setBounds(115, 64, 205, 19);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("S.T.  :");
        jPanel2.add(jLabel24);
        jLabel24.setBounds(330, 67, 95, 15);

        txtSTTerm.setName("ST_TERM"); // NOI18N
        txtSTTerm.setNextFocusableComponent(txtPFTerm);
        txtSTTerm.setEnabled(false);
        txtSTTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSTTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtSTTerm);
        txtSTTerm.setBounds(435, 64, 205, 19);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("P. & F. :");
        jPanel2.add(jLabel26);
        jLabel26.setBounds(5, 94, 105, 15);

        txtPFTerm.setName("PF_TERM"); // NOI18N
        txtPFTerm.setNextFocusableComponent(txtFreightTerm);
        txtPFTerm.setEnabled(false);
        txtPFTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPFTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtPFTerm);
        txtPFTerm.setBounds(115, 90, 205, 19);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Freight :");
        jPanel2.add(jLabel27);
        jLabel27.setBounds(330, 93, 95, 15);

        txtFreightTerm.setName("FREIGHT_TERM"); // NOI18N
        txtFreightTerm.setNextFocusableComponent(txtOctroiTerm);
        txtFreightTerm.setEnabled(false);
        txtFreightTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFreightTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtFreightTerm);
        txtFreightTerm.setBounds(435, 90, 205, 19);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Octroi :");
        jPanel2.add(jLabel28);
        jLabel28.setBounds(5, 120, 105, 15);

        txtOctroiTerm.setName("OCTROI_TERM"); // NOI18N
        txtOctroiTerm.setNextFocusableComponent(txtServiceTax);
        txtOctroiTerm.setEnabled(false);
        txtOctroiTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOctroiTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtOctroiTerm);
        txtOctroiTerm.setBounds(115, 116, 205, 19);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 144, 676, 6);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("FOB :");
        jPanel2.add(jLabel29);
        jLabel29.setBounds(5, 164, 105, 15);

        txtFOBTerm.setName("FOB_TERM"); // NOI18N
        txtFOBTerm.setNextFocusableComponent(txtCIETerm);
        txtFOBTerm.setEnabled(false);
        txtFOBTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFOBTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtFOBTerm);
        txtFOBTerm.setBounds(115, 160, 205, 19);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("CIF :");
        jPanel2.add(jLabel30);
        jLabel30.setBounds(330, 162, 95, 15);

        txtCIETerm.setName("CIE_TERM"); // NOI18N
        txtCIETerm.setNextFocusableComponent(txtInsuranceTerm);
        txtCIETerm.setEnabled(false);
        txtCIETerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCIETermKeyPressed(evt);
            }
        });
        jPanel2.add(txtCIETerm);
        txtCIETerm.setBounds(435, 158, 205, 19);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Insurance :");
        jPanel2.add(jLabel37);
        jLabel37.setBounds(5, 190, 105, 15);

        txtInsuranceTerm.setName("INSURANCE_TERM"); // NOI18N
        txtInsuranceTerm.setNextFocusableComponent(txtTCCTerm);
        txtInsuranceTerm.setEnabled(false);
        txtInsuranceTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInsuranceTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtInsuranceTerm);
        txtInsuranceTerm.setBounds(115, 186, 205, 19);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("TCC :");
        jPanel2.add(jLabel38);
        jLabel38.setBounds(330, 188, 95, 15);

        txtTCCTerm.setName("TCC_TERM"); // NOI18N
        txtTCCTerm.setNextFocusableComponent(txtCenvatTerm);
        txtTCCTerm.setEnabled(false);
        txtTCCTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTCCTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtTCCTerm);
        txtTCCTerm.setBounds(435, 184, 205, 19);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Cenvat :");
        jPanel2.add(jLabel49);
        jLabel49.setBounds(5, 216, 105, 15);

        txtCenvatTerm.setName("CENVAT_TERM"); // NOI18N
        txtCenvatTerm.setNextFocusableComponent(txtCenvatTerm);
        txtCenvatTerm.setEnabled(false);
        txtCenvatTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCenvatTermKeyPressed(evt);
            }
        });
        jPanel2.add(txtCenvatTerm);
        txtCenvatTerm.setBounds(115, 212, 205, 19);

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.add(jPanel5);
        jPanel5.setBounds(10, 250, 676, 6);

        jLabel50.setText("Despatch Instructions :");
        jPanel2.add(jLabel50);
        jLabel50.setBounds(10, 260, 150, 15);

        txtDespatchTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDespatchTermKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txtDespatchTerm);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(12, 280, 382, 88);

        cmdSelect.setText("Select");
        cmdSelect.setEnabled(false);
        cmdSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectActionPerformed(evt);
            }
        });
        jPanel2.add(cmdSelect);
        cmdSelect.setBounds(530, 10, 78, 20);

        txtServiceTax.setName("OCTROI_TERM"); // NOI18N
        txtServiceTax.setNextFocusableComponent(txtFOBTerm);
        txtServiceTax.setEnabled(false);
        jPanel2.add(txtServiceTax);
        txtServiceTax.setBounds(435, 116, 205, 19);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Service Tax :");
        jPanel2.add(jLabel51);
        jLabel51.setBounds(330, 119, 95, 15);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel52.setText("Others :");
        jPanel2.add(jLabel52);
        jLabel52.setBounds(330, 216, 95, 15);

        txtOthersTerm.setName("TCC_TERM"); // NOI18N
        txtOthersTerm.setNextFocusableComponent(txtCenvatTerm);
        txtOthersTerm.setEnabled(false);
        jPanel2.add(txtOthersTerm);
        txtOthersTerm.setBounds(435, 212, 205, 19);

        Tab.addTab("Terms & Conditions", jPanel2);

        Tab6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab6.setLayout(null);

        cmdNext6.setText("Next >>");
        cmdNext6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext6ActionPerformed(evt);
            }
        });
        Tab6.add(cmdNext6);
        cmdNext6.setBounds(640, 330, 102, 25);

        cmdBack5.setText("<< Back");
        cmdBack5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack5ActionPerformed(evt);
            }
        });
        Tab6.add(cmdBack5);
        cmdBack5.setBounds(530, 330, 102, 25);

        jLabel54.setText("CGST");
        Tab6.add(jLabel54);
        jLabel54.setBounds(10, 10, 96, 15);

        txtCGSTTerm.setEnabled(false);
        txtCGSTTerm.setName("CGST_TERM"); // NOI18N
        Tab6.add(txtCGSTTerm);
        txtCGSTTerm.setBounds(130, 10, 210, 19);

        jLabel55.setText("SGST");
        Tab6.add(jLabel55);
        jLabel55.setBounds(10, 50, 96, 15);

        txtSGSTTerm.setEnabled(false);
        txtSGSTTerm.setName("SGST_TERM"); // NOI18N
        txtSGSTTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSGSTTermKeyPressed(evt);
            }
        });
        Tab6.add(txtSGSTTerm);
        txtSGSTTerm.setBounds(130, 50, 210, 19);

        jLabel56.setText("GST Comp Cess");
        Tab6.add(jLabel56);
        jLabel56.setBounds(10, 210, 120, 15);

        txtGSTCompCessTerm.setEnabled(false);
        txtGSTCompCessTerm.setName("GST_COMPENSATION_CESS_TERM"); // NOI18N
        txtGSTCompCessTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGSTCompCessTermKeyPressed(evt);
            }
        });
        Tab6.add(txtGSTCompCessTerm);
        txtGSTCompCessTerm.setBounds(130, 210, 210, 19);

        jLabel57.setText("IGST");
        Tab6.add(jLabel57);
        jLabel57.setBounds(10, 90, 96, 15);

        txtIGSTTerm.setEnabled(false);
        txtIGSTTerm.setName("IGST_TERM"); // NOI18N
        txtIGSTTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIGSTTermKeyPressed(evt);
            }
        });
        Tab6.add(txtIGSTTerm);
        txtIGSTTerm.setBounds(130, 90, 210, 19);

        txtCompositionTerm.setEnabled(false);
        txtCompositionTerm.setName("COMPOSITION_TERM"); // NOI18N
        txtCompositionTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCompositionTermKeyPressed(evt);
            }
        });
        Tab6.add(txtCompositionTerm);
        txtCompositionTerm.setBounds(130, 130, 210, 19);

        jLabel58.setText("Composition");
        Tab6.add(jLabel58);
        jLabel58.setBounds(10, 130, 96, 15);

        jLabel59.setText("RCM");
        Tab6.add(jLabel59);
        jLabel59.setBounds(10, 170, 96, 15);

        txtRCMTerm.setEnabled(false);
        txtRCMTerm.setName("RCM_TERM"); // NOI18N
        txtRCMTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRCMTermKeyPressed(evt);
            }
        });
        Tab6.add(txtRCMTerm);
        txtRCMTerm.setBounds(130, 170, 210, 19);

        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab6.add(jPanel10);
        jPanel10.setBounds(10, 250, 676, 6);

        Tab.addTab("GST Term Information", Tab6);

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab3FocusGained(evt);
            }
        });
        Tab3.setLayout(null);

        TableL.setModel(new javax.swing.table.DefaultTableModel(
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
        TableL.setNextFocusableComponent(TableH);
        TableL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TableLFocusLost(evt);
            }
        });
        TableL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableLKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableLKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(TableL);

        Tab3.add(jScrollPane1);
        jScrollPane1.setBounds(6, 38, 732, 176);

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
        TableH.setNextFocusableComponent(cmdNext3);
        HeaderPane.setViewportView(TableH);

        Tab3.add(HeaderPane);
        HeaderPane.setBounds(6, 240, 254, 124);

        cmdInsert.setMnemonic('I');
        cmdInsert.setText("Insert from Indent");
        cmdInsert.setNextFocusableComponent(TableL);
        cmdInsert.setEnabled(false);
        cmdInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInsertActionPerformed(evt);
            }
        });
        cmdInsert.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdInsertFocusGained(evt);
            }
        });
        Tab3.add(cmdInsert);
        cmdInsert.setBounds(398, 8, 154, 25);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.setNextFocusableComponent(TableL);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        cmdAdd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdAddFocusGained(evt);
            }
        });
        Tab3.add(cmdAdd);
        cmdAdd.setBounds(554, 8, 88, 25);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setNextFocusableComponent(TableL);
        cmdRemove.setEnabled(false);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        cmdRemove.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdRemoveFocusGained(evt);
            }
        });
        Tab3.add(cmdRemove);
        cmdRemove.setBounds(646, 8, 92, 25);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Gross Amount :");
        Tab3.add(jLabel18);
        jLabel18.setBounds(480, 226, 105, 15);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Net Amount :");
        Tab3.add(jLabel19);
        jLabel19.setBounds(480, 250, 105, 15);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setText("PO Items");
        Tab3.add(jLabel20);
        jLabel20.setBounds(10, 14, 64, 15);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab3.add(jPanel4);
        jPanel4.setBounds(76, 26, 170, 6);

        cmdNext3.setText("Next >>");
        cmdNext3.setNextFocusableComponent(cmdBack3);
        cmdNext3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext3ActionPerformed(evt);
            }
        });
        Tab3.add(cmdNext3);
        cmdNext3.setBounds(638, 348, 102, 25);

        cmdBack3.setText("<< Back");
        cmdBack3.setNextFocusableComponent(cmdInsert);
        cmdBack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack3ActionPerformed(evt);
            }
        });
        Tab3.add(cmdBack3);
        cmdBack3.setBounds(532, 348, 102, 25);

        txtGrossAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtGrossAmount.setEditable(false);
        txtGrossAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Tab3.add(txtGrossAmount);
        txtGrossAmount.setBounds(595, 224, 145, 19);

        txtNetAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtNetAmount.setEditable(false);
        txtNetAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Tab3.add(txtNetAmount);
        txtNetAmount.setBounds(595, 248, 145, 19);

        jLabel1.setText("Press Ctrl+C to copy selected line");
        Tab3.add(jLabel1);
        jLabel1.setBounds(6, 216, 260, 15);

        cmdInsertQuot.setMnemonic('I');
        cmdInsertQuot.setText("Insert from Quot.");
        cmdInsertQuot.setEnabled(false);
        cmdInsertQuot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInsertQuotActionPerformed(evt);
            }
        });
        Tab3.add(cmdInsertQuot);
        cmdInsertQuot.setBounds(251, 8, 142, 25);

        cmdShowIndent.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        cmdShowIndent.setText("Show Indent");
        cmdShowIndent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowIndentActionPerformed(evt);
            }
        });
        Tab3.add(cmdShowIndent);
        cmdShowIndent.setBounds(272, 221, 115, 19);

        cmdShowQuot.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        cmdShowQuot.setText("Show Quot.");
        cmdShowQuot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowQuotActionPerformed(evt);
            }
        });
        Tab3.add(cmdShowQuot);
        cmdShowQuot.setBounds(272, 244, 113, 19);

        cmdShowPO.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        cmdShowPO.setText("Show PO");
        cmdShowPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowPOActionPerformed(evt);
            }
        });
        Tab3.add(cmdShowPO);
        cmdShowPO.setBounds(273, 267, 113, 19);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Final Amount :");
        Tab3.add(jLabel53);
        jLabel53.setBounds(480, 274, 105, 15);

        txtFinalAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtFinalAmount.setEditable(false);
        txtFinalAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Tab3.add(txtFinalAmount);
        txtFinalAmount.setBounds(595, 272, 145, 19);

        Tab.addTab("Item Information", Tab3);

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.setLayout(null);

        chkAttachement.setText("Attach File to this PO");
        chkAttachement.setName("ATTACHEMENT"); // NOI18N
        chkAttachement.setEnabled(false);
        jPanel7.add(chkAttachement);
        chkAttachement.setBounds(15, 12, 212, 23);

        jLabel6.setText("File :");
        jPanel7.add(jLabel6);
        jLabel6.setBounds(28, 51, 30, 15);

        txtFile.setName("ATTACHEMENT_PATH"); // NOI18N
        txtFile.setEnabled(false);
        jPanel7.add(txtFile);
        txtFile.setBounds(60, 49, 230, 21);

        cmdBrowse.setText("Browse");
        cmdBrowse.setName("ATTACHEMENT_PATH"); // NOI18N
        cmdBrowse.setEnabled(false);
        cmdBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBrowseActionPerformed(evt);
            }
        });
        jPanel7.add(cmdBrowse);
        cmdBrowse.setBounds(292, 49, 102, 22);

        jLabel25.setText("Other Terms & Conditions :");
        jPanel7.add(jLabel25);
        jLabel25.setBounds(17, 94, 183, 15);

        cmdShowFile.setText("Show File Contents");
        cmdShowFile.setEnabled(false);
        cmdShowFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowFileActionPerformed(evt);
            }
        });
        jPanel7.add(cmdShowFile);
        cmdShowFile.setBounds(205, 86, 160, 25);

        txtFileText.setEnabled(false);
        jScrollPane4.setViewportView(txtFileText);

        jPanel7.add(jScrollPane4);
        jScrollPane4.setBounds(16, 116, 716, 244);

        chkDoNotErase.setText("Do not erase existing contents");
        jPanel7.add(chkDoNotErase);
        chkDoNotErase.setBounds(505, 87, 223, 23);

        cmdSaveAs.setText("Save As");
        cmdSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveAsActionPerformed(evt);
            }
        });
        jPanel7.add(cmdSaveAs);
        cmdSaveAs.setBounds(410, 50, 89, 20);

        Tab.addTab("Other Terms", jPanel7);

        Tab4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab4FocusGained(evt);
            }
        });
        Tab4.setLayout(null);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        Tab4.add(jLabel31);
        jLabel31.setBounds(5, 18, 90, 15);

        cmbHierarchy.setNextFocusableComponent(OpgApprove);
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
        Tab4.add(cmbHierarchy);
        cmbHierarchy.setBounds(100, 14, 184, 24);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        Tab4.add(jLabel32);
        jLabel32.setBounds(5, 52, 90, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        Tab4.add(txtFrom);
        txtFrom.setBounds(100, 50, 182, 19);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        Tab4.add(jLabel35);
        jLabel35.setBounds(5, 82, 90, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setEnabled(false);
        Tab4.add(txtFromRemarks);
        txtFromRemarks.setBounds(100, 78, 518, 19);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        Tab4.add(jLabel36);
        jLabel36.setBounds(5, 124, 90, 15);

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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                OpgApproveMouseEntered(evt);
            }
        });
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 168, 23);

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
        OpgReject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgRejectFocusGained(evt);
            }
        });
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
        jPanel6.setBounds(100, 120, 182, 100);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        Tab4.add(jLabel33);
        jLabel33.setBounds(5, 228, 90, 15);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab4.add(cmbSendTo);
        cmbSendTo.setBounds(100, 224, 184, 24);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        Tab4.add(jLabel34);
        jLabel34.setBounds(5, 264, 90, 15);

        txtToRemarks.setNextFocusableComponent(cmdBack4);
        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab4.add(txtToRemarks);
        txtToRemarks.setBounds(100, 260, 516, 19);

        cmdBack4.setText("<< Back");
        cmdBack4.setNextFocusableComponent(cmbHierarchy);
        cmdBack4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack4ActionPerformed(evt);
            }
        });
        Tab4.add(cmdBack4);
        cmdBack4.setBounds(638, 340, 102, 25);

        txtFromRemarksBig.setText("...");
        txtFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromRemarksBigActionPerformed(evt);
            }
        });
        Tab4.add(txtFromRemarksBig);
        txtFromRemarksBig.setBounds(625, 77, 37, 20);

        Tab.addTab("Approval", Tab4);

        Tab5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab5.setLayout(null);

        jLabel45.setText("Document Approval Status :");
        Tab5.add(jLabel45);
        jLabel45.setBounds(12, 10, 190, 15);

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
        jScrollPane2.setBounds(12, 40, 660, 144);

        lblDocumentHistory.setText("Document Update History :");
        Tab5.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(13, 190, 182, 15);

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
        jScrollPane6.setBounds(13, 206, 511, 148);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        Tab5.add(cmdNormalView);
        cmdNormalView.setBounds(539, 266, 132, 24);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        Tab5.add(cmdViewHistory);
        cmdViewHistory.setBounds(539, 236, 132, 24);

        cmdPreviewA.setText("Preview Report");
        cmdPreviewA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewAActionPerformed(evt);
            }
        });
        Tab5.add(cmdPreviewA);
        cmdPreviewA.setBounds(538, 207, 131, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        Tab5.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(540, 296, 132, 24);

        txtAuditRemarks.setEnabled(false);
        Tab5.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(542, 338, 129, 19);

        Tab.addTab("Status", Tab5);

        getContentPane().add(Tab);
        Tab.setBounds(4, 68, 752, 396);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(5, 468, 752, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveAsActionPerformed
        // TODO add your handling code here:
        try {

            long DocID = (long) ObjPOAmend.getAttribute("DOC_ID").getVal();

            String FileName = "";

            if (DocID != 0) {

                clsDocument objDocument = clsDocument.getDocument(EITLERPGLOBAL.gCompanyID, DocID);
                FileName = objDocument.getAttribute("FILENAME").getObj().toString();
                fc.setSelectedFile(new File(FileName));

                int returnVal = fc.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    //File file = fc.getSelectedFile();
                    File file = fc.getSelectedFile();
                    clsDocument.SavetoFile(EITLERPGLOBAL.gCompanyID, DocID, file);

                }

                JOptionPane.showMessageDialog(null, "The file has been saved to " + FileName);

                //URL ReportFile=new URL("");
                //EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_cmdSaveAsActionPerformed

    private void OpgApproveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_OpgApproveMouseEntered

    private void cmdShowPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowPOActionPerformed
        // TODO add your handling code here:
        String LastPONo = txtPONo.getText();

        int POType = clsPOGen.getPOType(EITLERPGLOBAL.gCompanyID, LastPONo);
        AppletFrame aFrame = new AppletFrame("Purchase Order");
        aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
        frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
        ObjDoc.POType = POType;
        ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, LastPONo);

    }//GEN-LAST:event_cmdShowPOActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtAmendReason;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cmdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectActionPerformed
        // TODO add your handling code here:
        try {
            String strSQL = "";
            int SuppID = clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

            if (POType == 1 || POType == 2 || POType == 3 || POType == 5 || POType == 9) {
                strSQL = "SELECT TERM_CODE AS P_CODE,TERM_DESC AS P_DESC FROM D_COM_SUPP_TERMS WHERE SUPP_ID=" + SuppID + " AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND TERM_TYPE='P'";
            }

            if (POType == 6 || POType == 7) {
                strSQL = "SELECT D_COM_SUPP_TERMS.PARA_CODE AS P_CODE,D_COM_SUPP_TERMS.DESC AS P_DESC FROM D_COM_PARAMETER_MAST WHERE PARA_ID='PAYMENT_CODE' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID;
            }

            if (POType == 4) {
                if (chkImported.isSelected()) {
                    strSQL = "SELECT D_COM_PARAMETER_MAST.PARA_CODE AS P_CODE,D_COM_PARAMETER_MAST.DESC AS P_DESC FROM D_COM_PARAMETER_MAST WHERE PARA_ID='PAYMENT_CODE' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID;
                } else {
                    strSQL = "SELECT TERM_CODE AS P_CODE,TERM_DESC AS P_DESC FROM D_COM_SUPP_TERMS WHERE SUPP_ID=" + SuppID + " AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND TERM_TYPE='P'";
                }
            }
            LOV aList = new LOV();

            aList.SQL = strSQL;
            aList.ReturnCol = 2;
            aList.SecondCol = 1;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtPaymentTerm.setText(aList.ReturnVal);
                TermCode = Integer.parseInt(aList.SecondVal);
                TermDays = data.getIntValueFromDB("SELECT TERM_DAYS FROM D_COM_SUPP_TERMS WHERE SUPP_ID=" + SuppID + " AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND TERM_TYPE='P' AND TERM_CODE=" + TermCode);
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_cmdSelectActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if (TableHS.getRowCount() > 0 && TableHS.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableHS.getValueAt(TableHS.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }

    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdEMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEMailActionPerformed
        // TODO add your handling code here:
        jPopupMenu2.show(cmdEMail, 0, 30);

//        String AmendDetailCOL = data.getStringValueFromDB("SELECT COLUMN_3_CAPTION FROM DINESHMILLS.D_PUR_AMEND_DETAIL WHERE PO_NO='" + txtPONo.getText().trim() + "'");
//        String PODetailCOL = data.getStringValueFromDB("SELECT COLUMN_3_CAPTION FROM DINESHMILLS.D_PUR_PO_DETAIL WHERE PO_NO='" + txtPONo.getText().trim() + "'");
//
//        if (AmendDetailCOL.equalsIgnoreCase("CGST")) {
//            //JOptionPane.showMessageDialog(null, "cgst");
//            if (PODetailCOL.equalsIgnoreCase("Excise")) {
//                EmailPreviewReport_TRN();
//            } else {
//                EmailPreviewReport_GST();
//            }
//        } else {
//            if (PODetailCOL.equalsIgnoreCase("CGST")) {
//                EmailPreviewReport_TRN();
//            } else {
//                EmailPreviewReport();
//            }
//        }
//        if(EditMode==0) {
//            frmSendMail ObjSend=new frmSendMail();
//            ObjSend.ModuleID=27+POType;
//            
//            if(POType==8) {
//                ObjSend.ModuleID=47;
//            }
//            
//            //start add on 110909
//            if(POType==9) {
//                ObjSend.ModuleID=168;
//            }
//            //end add on 110909
//            
//            ObjSend.SentBy=EITLERPGLOBAL.gNewUserID;
//            ObjSend.MailDocNo=txtDocNo.getText().trim();
//            
//            String FileName="doc"+ObjSend.ModuleID+txtDocNo.getText()+".pdf";
//            
//            String suppEMail=clsSupplier.getSupplierEMail(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());
//            
//            if(suppEMail.trim().equals("")) {
//                return;
//            }
//            
//            ObjSend.colRecList.clear();
//            ObjSend.colRecList.put(Integer.toString(ObjSend.colRecList.size()+1),suppEMail);
//            
//            
//            if(chkCancelled.isSelected()) {
//                JOptionPane.showMessageDialog(null,"You cannot mail cancelled document");
//                return;
//            }
//            
//            int FinalApprover=0;
//            String strFinalApprover="";
//            int HierarchyID=(int)ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();
//            
//            FinalApprover=clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID,HierarchyID);
//            strFinalApprover=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,FinalApprover);
//            
//            PrepareAmendment(txtDocNo.getText(), POType);
//            
//            if(POType==4) //Raw Material
//            {
//                
//                if(HierarchyID==247||HierarchyID==248) {
//                    strFinalApprover="A. B. Tewary";
//                }
//                
//                if(HierarchyID==536||HierarchyID==558) {
//                    strFinalApprover="Vinod Patel";
//                }
//                
//                
//                try {
//                    URL MailDocument=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAmendRawMail.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&APPROVER="+strFinalApprover+"&File="+FileName);
//                    MailDocument.openConnection();
//                    MailDocument.openStream();
//                }
//                catch(Exception e) {
//                    
//                }
//                
//            }
//            
//            if(POType==5) //Import Spares
//            {
//                
//                if(HierarchyID==199||HierarchyID==201) {
//                    strFinalApprover="A. B. Tewary";
//                }
//                
//                if(HierarchyID==553||HierarchyID==554) {
//                    strFinalApprover="Vinod Patel";
//                }
//                
//                
//                try {
//                    URL MailDocument=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAmendImportMail.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&APPROVER="+strFinalApprover+"&File="+FileName);
//                    MailDocument.openConnection();
//                    MailDocument.openStream();
//                }
//                catch(Exception e) {
//                    
//                }
//                
//            }
//            
//            
//            if(POType==6) {
//                if(HierarchyID==570) {
//                    
//                    try {
//                        URL MailDocument=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAmendCapital4Mail.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&File="+FileName);
//                        MailDocument.openConnection();
//                        MailDocument.openStream();
//                    }
//                    catch(Exception e) {
//                        
//                    }
//                    
//                }
//                else {
//                    try {
//                        URL MailDocument=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAmendMail.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&File="+FileName);
//                        MailDocument.openConnection();
//                        MailDocument.openStream();
//                    }
//                    catch(Exception e) {
//                        
//                    }
//                    
//                    
//                }
//            }
//            
//            if(POType!=4&&POType!=5&&POType!=6) //Others
//            {
//                try {
//                    URL MailDocument=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAmendMail.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText()+"&File="+FileName);
//                    MailDocument.openConnection();
//                    MailDocument.openStream();
//                }
//                catch(Exception e) {
//                    
//                }
//            }
//            
//            
//            
//            
//            ObjSend.ShowWindow();
//        }
//        else {
//            JOptionPane.showMessageDialog(null,"Cannot send email while in edit mode. Please save the document and then try");
//        }
//        
    }//GEN-LAST:event_cmdEMailActionPerformed

    private void cmdShowFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowFileActionPerformed
        // TODO add your handling code here:
        ShowFileText();
    }//GEN-LAST:event_cmdShowFileActionPerformed

    private void cmdBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBrowseActionPerformed
        // TODO add your handling code here:
        FileDialog FileDialog = new FileDialog(findParentFrame(this));
        FileDialog.show();
        txtFile.setText(FileDialog.getDirectory() + FileDialog.getFile());

    }//GEN-LAST:event_cmdBrowseActionPerformed

    private void txtFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromRemarksBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtFromRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_txtFromRemarksBigActionPerformed

    private void cmdRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemarksBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdRemarksBigActionPerformed

    private void cmdLine2BigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLine2BigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtLine2;
        bigEdit.ShowEdit();

    }//GEN-LAST:event_cmdLine2BigActionPerformed

    private void cmdLine1BigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLine1BigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtLine1;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdLine1BigActionPerformed

    private void cmdImportBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdImportBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtImportLicense;
        bigEdit.ShowEdit();

    }//GEN-LAST:event_cmdImportBigActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtSubject;
        bigEdit.ShowEdit();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmdPurposeBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPurposeBigActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtPurpose;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdPurposeBigActionPerformed

    private void txtDespatchTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDespatchTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='DESPATCH_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtDespatchTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtDespatchTermKeyPressed

    private void txtCenvatTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCenvatTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='CENVAT_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtCenvatTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtCenvatTermKeyPressed

    private void txtTCCTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTCCTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='TCC_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtTCCTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtTCCTermKeyPressed

    private void txtInsuranceTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInsuranceTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='INSURANCE_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtInsuranceTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtInsuranceTermKeyPressed

    private void txtCIETermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCIETermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='CIE_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtCIETerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtCIETermKeyPressed

    private void txtFOBTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFOBTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='FOB_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtFOBTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtFOBTermKeyPressed

    private void txtOctroiTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOctroiTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='OCTROI_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtOctroiTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtOctroiTermKeyPressed

    private void txtFreightTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFreightTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='FREIGHT_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtFreightTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtFreightTermKeyPressed

    private void txtPFTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPFTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='PF_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtPFTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtPFTermKeyPressed

    private void txtSTTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSTTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='ST_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtSTTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtSTTermKeyPressed

    private void txtExciseTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExciseTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='EXCISE_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtExciseTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtExciseTermKeyPressed

    private void txtDiscountTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='DISCOUNT_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtDiscountTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtDiscountTermKeyPressed

    private void txtPriceBasisTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceBasisTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='PRICE_BASIS_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtPriceBasisTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtPriceBasisTermKeyPressed

    private void cmdPreviewAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewAActionPerformed
        // TODO add your handling code here:
        PreviewAuditReport();
    }//GEN-LAST:event_cmdPreviewAActionPerformed

    private void cmdShowQuotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowQuotActionPerformed
        // TODO add your handling code here:
        try {

            String DocNo = (String) DataModelL.getValueByVariable("QUOT_ID", TableL.getSelectedRow());

            if (!DocNo.trim().equals("")) {
                AppletFrame aFrame = new AppletFrame("Quotation");
                aFrame.startAppletEx("EITLERP.Purchase.frmQuotation", "Quotation");
                frmQuotation ObjDoc = (frmQuotation) aFrame.ObjApplet;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            } else {
                JOptionPane.showMessageDialog(null, "No quotation no. specified");
            }
        } catch (Exception e) {
        }

    }//GEN-LAST:event_cmdShowQuotActionPerformed

    private void cmdShowIndentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowIndentActionPerformed
        // TODO add your handling code here:
        try {
            String DocNo = (String) DataModelL.getValueByVariable("INDENT_NO", TableL.getSelectedRow());

            if (!DocNo.trim().equals("")) {
                AppletFrame aFrame = new AppletFrame("Indent");
                aFrame.startAppletEx("EITLERP.Stores.FrmIndent", "Indent");
                FrmIndent ObjDoc = (FrmIndent) aFrame.ObjApplet;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            } else {
                JOptionPane.showMessageDialog(null, "No indent no. specified");
            }
        } catch (Exception e) {
        }

    }//GEN-LAST:event_cmdShowIndentActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjPOAmend.LoadData(EITLERPGLOBAL.gCompanyID, POType);
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo = txtDocNo.getText();
        ObjPOAmend.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdInsertQuotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInsertQuotActionPerformed
        // TODO add your handling code here:
        SelectQuot ObjQuot = new SelectQuot();
        if (POType == 4) {
            ObjQuot.condition = " AND D_PUR_QUOT_DETAIL.ITEM_ID LIKE 'RM%' ";
        } else {
            ObjQuot.condition = " AND D_PUR_QUOT_DETAIL.ITEM_ID NOT LIKE 'RM%' ";
        }
        if (ObjQuot.ShowList()) {
            DoNotEvaluate = true;
            if (ObjQuot.CopyHeader) {
                //Copy Header Fields
                clsQuotation ObjQuotation = ObjQuot.ObjQuotation;
                //Copy all the terms from supplier to PO
                //Copy all the terms from supplier to PO
                /*FormatTermsGrid();
                 for(int i=1;i<=ObjQuotation.colQuotTerms.size();i++) {
                 //Insert New Row
                 Object[] rowData=new Object[3];
                 clsQuotTerms ObjItem=(clsQuotTerms)ObjQuotation.colQuotTerms.get(Integer.toString(i));
                 String TermType=(String)ObjItem.getAttribute("TERM_TYPE").getObj();
                 
                 if(TermType.equals("D")) {
                 
                 rowData[0]=Integer.toString(TableD.getRowCount()+1);
                 rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                 rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                 DataModelD.addRow(rowData);
                 }
                 
                 if(TermType.equals("O")) {
                 
                 rowData[0]=Integer.toString(TableO.getRowCount()+1);
                 rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                 rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                 DataModelO.addRow(rowData);
                 }
                 }*/

                int PartyID = Integer.parseInt((String) ObjQuotation.getAttribute("SUPP_ID").getObj());
                String SuppCode = clsParty.getSupplierCode(EITLERPGLOBAL.gCompanyID, PartyID);
                txtSuppCode.setText(SuppCode);
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, SuppCode));

                txtQuotationNo.setText((String) ObjQuotation.getAttribute("QUOT_ID").getObj());
                txtQuotationDate.setText(EITLERPGLOBAL.formatDate((String) ObjQuotation.getAttribute("QUOT_DATE").getObj()));
                txtInquiryNo.setText((String) ObjQuotation.getAttribute("INQUIRY_NO").getObj());

                txtPaymentTerm.setText((String) ObjQuotation.getAttribute("PRICE_BASIS_TERM").getObj());
                txtDiscountTerm.setText((String) ObjQuotation.getAttribute("DISCOUNT_TERM").getObj());
                txtExciseTerm.setText((String) ObjQuotation.getAttribute("EXCISE_TERM").getObj());
                txtSTTerm.setText((String) ObjQuotation.getAttribute("ST_TERM").getObj());
                txtPFTerm.setText((String) ObjQuotation.getAttribute("PF_TERM").getObj());
                txtFreightTerm.setText((String) ObjQuotation.getAttribute("FREIGHT_TERM").getObj());
                txtOctroiTerm.setText((String) ObjQuotation.getAttribute("OCTROI_TERM").getObj());
                txtFOBTerm.setText((String) ObjQuotation.getAttribute("FOB_TERM").getObj());
                txtCIETerm.setText((String) ObjQuotation.getAttribute("CIE_TERM").getObj());
                txtInsuranceTerm.setText((String) ObjQuotation.getAttribute("INSURANCE_TERM").getObj());
                txtTCCTerm.setText((String) ObjQuotation.getAttribute("TCC_TERM").getObj());
                txtCenvatTerm.setText((String) ObjQuotation.getAttribute("CENVAT_TERM").getObj());
                txtDespatchTerm.setText((String) ObjQuotation.getAttribute("DESPATCH_TERM").getObj());
                txtServiceTax.setText((String) ObjQuotation.getAttribute("SERVICE_TAX_TERM").getObj());
                txtOthersTerm.setText((String) ObjQuotation.getAttribute("OTHERS_TERM").getObj());

                txtCGSTTerm.setText((String) ObjQuotation.getAttribute("CGST_TERM").getObj());
                txtSGSTTerm.setText((String) ObjQuotation.getAttribute("SGST_TERM").getObj());
                txtIGSTTerm.setText((String) ObjQuotation.getAttribute("IGST_TERM").getObj());
                txtCompositionTerm.setText((String) ObjQuotation.getAttribute("COMPOSITION_TERM").getObj());
                txtRCMTerm.setText((String) ObjQuotation.getAttribute("RCM_TERM").getObj());
                txtGSTCompCessTerm.setText((String) ObjQuotation.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

                //Get the Inquiry Date
                clsInquiry tmpInq = new clsInquiry();
                tmpInq.LoadData(EITLERPGLOBAL.gCompanyID);

                clsInquiry ObjInq = (clsInquiry) tmpInq.getObject(EITLERPGLOBAL.gCompanyID, txtInquiryNo.getText());
                txtInquiryDate.setText(EITLERPGLOBAL.formatDate((String) ObjInq.getAttribute("INQUIRY_DATE").getObj()));

                EITLERPGLOBAL.setComboIndex(cmbBuyer, (int) ObjInq.getAttribute("BUYER").getVal());
                txtPurpose.setText((String) ObjInq.getAttribute("PURPOSE").getObj());

                clsSupplier tmpObj = new clsSupplier();
                tmpObj.LoadData(EITLERPGLOBAL.gCompanyID);

                clsSupplier ObjSupp = (clsSupplier) tmpObj.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                    //Insert New Row
                    Object[] rowData = new Object[3];
                    clsSuppTerms ObjItem = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                    String TermType = (String) ObjItem.getAttribute("TERM_TYPE").getObj();

                    if (TermType.equals("P")) {
                        txtPaymentTerm.setText((String) ObjItem.getAttribute("TERM_DESC").getObj());

                        //rowData[0]=Integer.toString(TableP.getRowCount()+1);
                        //rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                        //rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                        //DataModelP.addRow(rowData);
                    }

                }

            }

            for (int i = 1; i <= ObjQuot.colSelItems.size(); i++) {
                clsQuotationItem ObjItem = (clsQuotationItem) ObjQuot.colSelItems.get(Integer.toString(i));

                for (int r = 0; r < TableL.getRowCount(); r++) {
                    String QuotID = (String) ObjItem.getAttribute("QUOT_ID").getObj();
                    int QuotSrNo = (int) ObjItem.getAttribute("SR_NO").getVal();

                    String tQuotID = (String) DataModelL.getValueByVariable("QUOT_ID", r);
                    int tQuotSrNo = Integer.parseInt((String) DataModelL.getValueByVariable("QUOT_SR_NO", r));

                    if (QuotID.equals(tQuotID) && (QuotSrNo == tQuotSrNo)) {
                        JOptionPane.showMessageDialog(null, "Quotation No. " + QuotID + " Sr. " + QuotSrNo + " already exist");
                        return;
                    }
                }
            }

            //It will contain Indent Item Objects
            for (int i = 1; i <= ObjQuot.colSelItems.size(); i++) {
                clsQuotationItem ObjItem = (clsQuotationItem) ObjQuot.colSelItems.get(Integer.toString(i));

                //Add Blank Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);

                int NewRow = TableL.getRowCount() - 1;

                DataModelL.setValueByVariable("SR_NO", Integer.toString(i), NewRow);
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("MAKE", (String) ObjItem.getAttribute("MAKE").getObj(), NewRow);
                DataModelL.setValueByVariable("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("DELIVERY_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DELIVERY_DATE").getObj()), NewRow);
                String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                String HsnSacCode = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("HSN_SAC_CODE", HsnSacCode, NewRow);
                DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("BAL_QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("RATE", Double.toString(ObjItem.getAttribute("RATE").getVal()), NewRow);
                DataModelL.setValueByVariable("UNIT_ID", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);
                String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);

                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), NewRow);
                DataModelL.setValueByVariable("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("QUOT_SR_NO", Integer.toString((int) ObjItem.getAttribute("SR_NO").getVal()), NewRow);

                DataModelL.setValueByVariable("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("INDENT_SR_NO", Integer.toString((int) ObjItem.getAttribute("INDENT_SR_NO").getVal()), NewRow);

                String QuotID = (String) ObjItem.getAttribute("QUOT_ID").getObj();
                int QuotSrNo = (int) ObjItem.getAttribute("SR_NO").getVal();
                int DeptID = clsQuotation.getIndentDeptID(EITLERPGLOBAL.gCompanyID, QuotID, QuotSrNo);
                DataModelL.setValueByVariable("DEPT_ID", Integer.toString(DeptID), NewRow);
                DataModelL.setValueByVariable("DEPT_NAME", clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, DeptID), NewRow);

                //========= Import Tax Columns - Match both columns with variable name ==========//
//                for(int c=1;c<=10;c++) {
                for (int c = 1; c <= 16; c++) {
                    //Get the Column ID
                    int lnColID = (int) ObjItem.getAttribute("COLUMN_" + c + "_ID").getVal();
                    String strVariable = "";
                    double lnPercentValue = 0, lnValue = 0;

                    //Record the values
                    lnPercentValue = ObjItem.getAttribute("COLUMN_" + c + "_PER").getVal();
                    lnValue = ObjItem.getAttribute("COLUMN_" + c + "_AMT").getVal();

                    if (lnColID > 0) //Column ID set .. Continue
                    {
                        //Get the Variable Name
                        strVariable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, lnColID);
                    }

                    //We have variable Name - Get the column no. of this form
                    int lnDestCol = DataModelL.getColFromVariable(strVariable);

                    if (lnDestCol >= 0) //We have found the column
                    {
                        //Replace the value of this form
                        DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);

                        //Now check that percentage is used
                        int lnTaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, lnColID);
                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, lnTaxID)) {
                            DataModelL.setValueAt(Double.toString(lnPercentValue), NewRow, lnDestCol - 1);
                        }
                    }
                }
                //===================== Import Completed =================//

                DoNotEvaluate = false;
                TableL.changeSelection(NewRow, 0, false, false);
                UpdateResults(DataModelL.getColFromVariable("QTY"));
                DoNotEvaluate = true;

            }

            UpdateSrNo();
            DoNotEvaluate = false;
        }

    }//GEN-LAST:event_cmdInsertQuotActionPerformed

    private void TableLKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableLKeyReleased
        // TODO add your handling code here:
        if (EditMode != 0) {
            if (evt.getKeyCode() == 67 && evt.getModifiersEx() == 128) //Ctrl+C Key Combonation
            {
                //Check that any row exist
                if (TableL.getRowCount() > 0) {
                    //First Add new row
                    Object[] rowData = new Object[1];
                    DataModelL.addRow(rowData);
                    int NewRow = TableL.getRowCount() - 1;

                    //Copy New row with Previous one
                    for (int i = 0; i < TableL.getColumnCount(); i++) {
                        TableL.setValueAt(TableL.getValueAt(TableL.getSelectedRow(), i), NewRow, i);
                    }
                    UpdateSrNo();
                }
            }
        }
    }//GEN-LAST:event_TableLKeyReleased

    private void txtAmendReasonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmendReasonFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter reason for amendment");
    }//GEN-LAST:event_txtAmendReasonFocusGained

    private void txtReasonCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReasonCodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Press F1 for the list of Amendment Reasons");
    }//GEN-LAST:event_txtReasonCodeFocusGained

    private void txtReasonCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReasonCodeFocusLost
        // TODO add your handling code here:
        if (!txtReasonCode.getText().trim().equals("")) {
            if (txtAmendReason.getText().trim().equals("")) {
                txtAmendReason.setText(clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "AMEND_REASON", Integer.parseInt(txtReasonCode.getText())));
            }
        }
    }//GEN-LAST:event_txtReasonCodeFocusLost

    private void txtReasonCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReasonCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='AMEND_REASON' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtReasonCode.setText(aList.ReturnVal);
                txtAmendReason.setText(clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "AMEND_REASON", Integer.parseInt(aList.ReturnVal)));
            }
        }

    }//GEN-LAST:event_txtReasonCodeKeyPressed

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the remarks for next user");
    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the user to whome document to be forwarded");
    }//GEN-LAST:event_cmbSendToFocusGained

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgHoldFocusGained

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgRejectFocusGained

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void cmdRemoveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdRemoveFocusGained
        // TODO add your handling code here:
        ShowMessage("Click this button to remove selected row from the table");
    }//GEN-LAST:event_cmdRemoveFocusGained

    private void cmdAddFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdAddFocusGained
        // TODO add your handling code here:
        ShowMessage("Click this button to add a new row to the table");
    }//GEN-LAST:event_cmdAddFocusGained

    private void cmdInsertFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdInsertFocusGained
        // TODO add your handling code here:
        ShowMessage("Click this button to bring Indent item dialog box");
    }//GEN-LAST:event_cmdInsertFocusGained

    private void cmbShipFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbShipFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the shipment address");
        int SelShipID = EITLERPGLOBAL.getComboCode(cmbShip);
        clsShippingAddress ObjShip = (clsShippingAddress) clsShippingAddress.getObject(EITLERPGLOBAL.gCompanyID, SelShipID);
        txtAdd1.setText((String) ObjShip.getAttribute("ADD1").getObj());
        txtAdd2.setText((String) ObjShip.getAttribute("ADD2").getObj());
        txtAdd3.setText((String) ObjShip.getAttribute("ADD3").getObj());
        txtCity.setText((String) ObjShip.getAttribute("CITY").getObj());
        txtState.setText((String) ObjShip.getAttribute("STATE").getObj());
    }//GEN-LAST:event_cmbShipFocusGained

    private void chkCancelledFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkCancelledFocusGained
        // TODO add your handling code here:
        ShowMessage("Shows the cancel status of this document");
    }//GEN-LAST:event_chkCancelledFocusGained

    private void cmbCurrencyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbCurrencyFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the currency");
    }//GEN-LAST:event_cmbCurrencyFocusGained

    private void txtRefBFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRefBFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the reference");
    }//GEN-LAST:event_txtRefBFocusGained

    private void txtRefAFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRefAFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the reference");
    }//GEN-LAST:event_txtRefAFocusGained

    private void txtReferenceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReferenceFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the reference");
    }//GEN-LAST:event_txtReferenceFocusGained

    private void txtSubjectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubjectFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the subject");
    }//GEN-LAST:event_txtSubjectFocusGained

    private void txtPurposeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurposeFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter the purpose of purchase");
    }//GEN-LAST:event_txtPurposeFocusGained

    private void cmbBuyerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbBuyerFocusGained
        // TODO add your handling code here:
        ShowMessage("Select the buyer");
    }//GEN-LAST:event_cmbBuyerFocusGained

    private void txtInquiryDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInquiryDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter inquiry date in DD/MM/YYYY");
    }//GEN-LAST:event_txtInquiryDateFocusGained

    private void txtInquiryNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInquiryNoFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter inquiry no.");
    }//GEN-LAST:event_txtInquiryNoFocusGained

    private void txtQuotationDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuotationDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter quotation date in DD/MM/YYYY");
    }//GEN-LAST:event_txtQuotationDateFocusGained

    private void txtQuotationNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuotationNoFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter quotation no.");
    }//GEN-LAST:event_txtQuotationNoFocusGained

    private void txtSuppCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter supplier id. Press F1 for the list of suppliers");
    }//GEN-LAST:event_txtSuppCodeFocusGained

    private void txtDocDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDocDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter date in DD/MM/YYYY");
    }//GEN-LAST:event_txtDocDateFocusGained

    private void Tab4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab4FocusGained
        // TODO add your handling code here:
        cmbHierarchy.requestFocus();
    }//GEN-LAST:event_Tab4FocusGained

    private void Tab3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab3FocusGained
        // TODO add your handling code here:
        cmdInsert.requestFocus();
    }//GEN-LAST:event_Tab3FocusGained

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

    private void cmdBack3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack3ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBack3ActionPerformed

    private void cmdNext3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext3ActionPerformed
        // TODO add your handling code here:
        Tab.setSelectedIndex(3);
    }//GEN-LAST:event_cmdNext3ActionPerformed

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

    private void cmbShipItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbShipItemStateChanged
        // TODO add your handling code here:
        int SelShipID = EITLERPGLOBAL.getComboCode(cmbShip);
        clsShippingAddress ObjShip = (clsShippingAddress) clsShippingAddress.getObject(EITLERPGLOBAL.gCompanyID, SelShipID);
        txtAdd1.setText((String) ObjShip.getAttribute("ADD1").getObj());
        txtAdd2.setText((String) ObjShip.getAttribute("ADD2").getObj());
        txtAdd3.setText((String) ObjShip.getAttribute("ADD3").getObj());
        txtCity.setText((String) ObjShip.getAttribute("CITY").getObj());
        txtState.setText((String) ObjShip.getAttribute("STATE").getObj());
    }//GEN-LAST:event_cmbShipItemStateChanged

    private void Tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tab1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Tab1MouseClicked

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

        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        // TODO add your handling code here:
        SetupApproval();
        int ModuleID = (27 + POType);
        //start add on 110909
        if (POType == 9) {
            ModuleID = 168;
        }
        //end add on 110909

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID, ModuleID, txtDocNo.getText())) {
                cmbSendTo.setEnabled(true);
            } else {
                if (clsHierarchy.CanSkip((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
                    cmbSendTo.setEnabled(true);
                } else {
                    cmbSendTo.setEnabled(false);
                }
                //cmbSendTo.setEnabled(false);
            }
        }

        if (cmbSendTo.getItemCount() <= 0) {
            GenerateFromCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ObjPOAmend.Close();
        ObjColumn.Close();
        ObjTax.Close();

        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        jPopupMenu1.show(cmdPreview, 0, 30);

//        String AmendDetailCOL = data.getStringValueFromDB("SELECT COLUMN_3_CAPTION FROM DINESHMILLS.D_PUR_AMEND_DETAIL WHERE PO_NO='" + txtPONo.getText().trim() + "'");
//        String PODetailCOL = data.getStringValueFromDB("SELECT COLUMN_3_CAPTION FROM DINESHMILLS.D_PUR_PO_DETAIL WHERE PO_NO='" + txtPONo.getText().trim() + "'");
//
//        if (AmendDetailCOL.equalsIgnoreCase("CGST")) {
//            //JOptionPane.showMessageDialog(null, "cgst");
//            if (PODetailCOL.equalsIgnoreCase("Excise")) {
//                PreviewReport_TRN();
//            } else {
//                PreviewReport_GST();
//            }
//        } else {
//            if (PODetailCOL.equalsIgnoreCase("CGST")) {
//                PreviewReport_TRN();
//            } else {
//                PreviewReport();
//            }
//        }
        //PreviewReport();
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
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND ST35_REGISTERED=1 AND BLOCKED='N' AND APPROVED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtSuppCode.setText(aList.ReturnVal);
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));

                if (!txtSuppCode.getText().trim().equals("")) {
                    txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));

                    clsSupplier tmpObj = new clsSupplier();
                    tmpObj.LoadData(EITLERPGLOBAL.gCompanyID);

                    clsSupplier ObjSupp = (clsSupplier) tmpObj.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());
                    //Copy all the terms from supplier to PO

                    //FormatPayTermsGrid();
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        //Insert New Row
                        Object[] rowData = new Object[3];
                        clsSuppTerms ObjItem = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjItem.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            txtPaymentTerm.setText((String) ObjItem.getAttribute("TERM_DESC").getObj());
                        }
                    }
                }
            }
        }
        //=========================================

    }//GEN-LAST:event_txtSuppCodeKeyPressed

    private void txtSuppCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusLost
        // TODO add your handling code here:

        txtSuppName.setEnabled(false);

        if (POType == 6 || POType == 7 || POType == 2) {
            if (txtSuppCode.getText().trim().equals("000000")) {
                txtSuppName.setEnabled(true);
                txtSuppName.requestFocus();
            }
        }

        if (!txtSuppCode.getText().equals(prevValue)) {
            if (!txtSuppCode.getText().trim().equals("")) {
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));

                clsSupplier tmpObj = new clsSupplier();
                tmpObj.LoadData(EITLERPGLOBAL.gCompanyID);

                clsSupplier ObjSupp = (clsSupplier) tmpObj.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());
                //Copy all the terms from supplier to PO
                FormatPayTermsGrid();

                for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                    //Insert New Row
                    Object[] rowData = new Object[3];
                    clsSuppTerms ObjItem = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                    String TermType = (String) ObjItem.getAttribute("TERM_TYPE").getObj();

                    if (TermType.equals("P")) {
                        txtPaymentTerm.setText((String) ObjItem.getAttribute("TERM_DESC").getObj());
                        TermCode = ObjItem.getAttribute("TERM_CODE").getInt();
                        TermDays = ObjItem.getAttribute("TERM_DAYS").getInt();
                    }

                }

            }
        }
    }//GEN-LAST:event_txtSuppCodeFocusLost

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        if (TableL.getRowCount() > 0) {
            DataModelL.removeRow(TableL.getSelectedRow());
            UpdateSrNo();
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void TableLFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableLFocusLost
        // TODO add your handling code here:

        //Update Header Custom Columns
        /*for(int i=0;i<TableH.getRowCount();i++) {
         UpdateResults_H(i);
         }*/

    }//GEN-LAST:event_TableLFocusLost

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        Object[] rowData = new Object[2];
        DataModelL.addRow(rowData);
        TableL.changeSelection(TableL.getRowCount() - 1, 1, false, false);
        UpdateSrNo();
    }//GEN-LAST:event_cmdAddActionPerformed

    private void TableLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableLKeyPressed
        // TODO add your handling code here:
        if (EditMode != 0) {
            //=========== External Indent Reference  ===============//
            //======================================================//
            if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("INDENT_NO")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {

                    SelectIndentNo aList = new SelectIndentNo();

                    aList.ItemID = DataModelL.getValueByVariable("ITEM_ID", TableL.getSelectedRow());

                    if (aList.ShowList()) {
                        DataModelL.setValueByVariable("INDENT_NO", aList.SelIndentNo, TableL.getSelectedRow());
                        DataModelL.setValueByVariable("INDENT_SR_NO", Integer.toString(aList.SelIndentSrNo), TableL.getSelectedRow());

                        int DeptID = clsIndent.getDeptID(EITLERPGLOBAL.gCompanyID, aList.SelIndentNo);
                        DataModelL.setValueByVariable("DEPT_ID", Integer.toString(DeptID), TableL.getSelectedRow());
                        DataModelL.setValueByVariable("DEPT_NAME", clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, DeptID), TableL.getSelectedRow());

                        clsIndent tmpObj = new clsIndent();
                        clsIndent ObjIndent = (clsIndent) tmpObj.getObject(EITLERPGLOBAL.gCompanyID, aList.SelIndentNo);

                        for (int i = 1; i <= tmpObj.colLineItems.size(); i++) {
                            if (i == aList.SelIndentSrNo) {
                                clsIndentItem ObjItem = (clsIndentItem) tmpObj.colLineItems.get(Integer.toString(i));
                                DataModelL.setValueByVariable("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj(), TableL.getSelectedRow());
                                DataModelL.setValueByVariable("DELIVERY_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("REQUIRED_DATE").getObj()), TableL.getSelectedRow());
                                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), TableL.getSelectedRow());
                            }

                        }
                    }
                }
            }
            //=====================================================================//
            //====================================================================//

            //=========== Department List ===============
            if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("DEPT_ID")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {
                    LOV aList = new LOV();

                    aList.SQL = "SELECT DEPT_ID,DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ORDER BY DEPT_DESC";
                    aList.ReturnCol = 1;
                    aList.ShowReturnCol = true;
                    aList.DefaultSearchOn = 2;

                    if (aList.ShowLOV()) {
                        if (TableL.getCellEditor() != null) {
                            TableL.getCellEditor().stopCellEditing();
                        }
                        TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(), DataModelL.getColFromVariable("DEPT_ID"));
                    }
                }
            }
            //=========================================

            if (evt.getKeyCode() == 122) //F11 Key pressed
            {
                String lItemID = (String) TableL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));

                frmItemHistory ObjItem = new frmItemHistory();
                ObjItem.ShowForm(lItemID);
            }

            //=========== Item List ===============
            if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("ITEM_ID")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {
                    LOV aList = new LOV();
                    String Condition = "";
                    /*if (POType==4) {
                     Condition = " AND ITEM_ID LIKE 'RM%' ";
                     }
                     else {
                     Condition = " AND ITEM_ID NOT LIKE 'RM%' ";
                     }*/
                    String ItemStartsWith = "";
                    if (EITLERPGLOBAL.gCompanyID == 2) {
                        ItemStartsWith = "RM1";
                    } else if (EITLERPGLOBAL.gCompanyID == 3) {
                        ItemStartsWith = "RM2";
                    }
                    if (POType == 4) {
                        Condition = " AND ITEM_ID LIKE '" + ItemStartsWith + "%' ";
                    } else {
                        Condition = " AND ITEM_ID NOT LIKE 'RM%' ";
                    }

                    aList.SQL = "SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " " + Condition + " AND APPROVED=1 AND CANCELLED=0 ORDER BY ITEM_ID";
                    aList.ReturnCol = 1;
                    aList.ShowReturnCol = true;
                    aList.DefaultSearchOn = 2;

                    if (aList.ShowLOV()) {
                        if (TableL.getCellEditor() != null) {
                            TableL.getCellEditor().stopCellEditing();
                        }
                        TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                    }
                }
            }
            //=========================================

            //=========== Unit List ===============//
            if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("UNIT_NAME")) {
                // poType 1 and 5 added by Muffy
                if (POType != 3 && POType != 4 && POType != 9) {
                    if (evt.getKeyCode() == 112) //F1 Key pressed
                    {

                        LOV aList = new LOV();

                        aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='UNIT' ORDER BY D_COM_PARAMETER_MAST.DESC";
                        aList.ReturnCol = 1;
                        aList.ShowReturnCol = false;
                        aList.DefaultSearchOn = 2;

                        if (aList.ShowLOV()) {
                            if (TableL.getCellEditor() != null) {
                                TableL.getCellEditor().stopCellEditing();
                            }
                            TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(), DataModelL.getColFromVariable("UNIT_ID"));
                            TableL.setValueAt(clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", Integer.parseInt(aList.ReturnVal)), TableL.getSelectedRow(), DataModelL.getColFromVariable("UNIT_NAME"));
                        }
                    }
                }
            }
            //=========================================//

            //            if(evt.getKeyCode()==155)//Insert Key Pressed
            //            {
            //                Object[] rowData=new Object[1];
            //                DataModelL.addRow(rowData);
            //                TableL.changeSelection(TableL.getRowCount()-1, 1, false,false);
            //                UpdateSrNo();
            //            }
        }
    }//GEN-LAST:event_TableLKeyPressed

    private void cmdInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInsertActionPerformed
        // TODO add your handling code here:
        SelectIndent ObjIndent = new SelectIndent();
        if (POType == 4) {
            ObjIndent.condition = " AND B.ITEM_CODE LIKE 'RM%' ";
        } else {
            ObjIndent.condition = " AND B.ITEM_CODE NOT LIKE 'RM%' ";
        }
        if (ObjIndent.ShowList()) {
            DoNotEvaluate = true;

            if (ObjIndent.CopyHeader) {
                EITLERPGLOBAL.setComboIndex(cmbBuyer, (int) ObjIndent.ObjIndent.getAttribute("BUYER").getVal());
                txtPurpose.setText((String) ObjIndent.ObjIndent.getAttribute("PURPOSE").getObj());
            }

            for (int i = 1; i <= ObjIndent.colSelItems.size(); i++) {
                clsIndentItem ObjItem = (clsIndentItem) ObjIndent.colSelItems.get(Integer.toString(i));

                for (int r = 0; r < TableL.getRowCount(); r++) {
                    String IndentNo = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                    int IndentSrNo = (int) ObjItem.getAttribute("SR_NO").getVal();

                    String tIndentNo = (String) DataModelL.getValueByVariable("INDENT_NO", r);
                    int tIndentSrNo = Integer.parseInt((String) DataModelL.getValueByVariable("INDENT_SR_NO", r));

                    if (IndentNo.equals(tIndentNo) && (IndentSrNo == tIndentSrNo)) {
                        JOptionPane.showMessageDialog(null, "Indent No. " + IndentNo + " Sr. " + IndentSrNo + " already exist");
                        return;
                    }
                }
            }

            //It will contain Indent Item Objects
            for (int i = 1; i <= ObjIndent.colSelItems.size(); i++) {
                clsIndentItem ObjItem = (clsIndentItem) ObjIndent.colSelItems.get(Integer.toString(i));

                //Add Blank Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);

                int NewRow = TableL.getRowCount() - 1;

                DataModelL.setValueByVariable("SR_NO", Integer.toString(i), NewRow);
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_CODE").getObj(), NewRow);
                DataModelL.setValueByVariable("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("DELIVERY_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("REQUIRED_DATE").getObj()), NewRow);
                String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_CODE").getObj());
                DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                String HsnSacCode = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("HSN_SAC_CODE", HsnSacCode, NewRow);
                DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("UNIT_ID", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);
                String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);
                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), NewRow);
                DataModelL.setValueByVariable("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("INDENT_SR_NO", Integer.toString((int) ObjItem.getAttribute("SR_NO").getVal()), NewRow);

                //Now Fetch the Department from the Indent
                String IndentNo = (String) ObjItem.getAttribute("INDENT_NO").getObj();
                int DeptID = clsIndent.getDeptID(EITLERPGLOBAL.gCompanyID, IndentNo);
                DataModelL.setValueByVariable("DEPT_ID", Integer.toString(DeptID), NewRow);
                DataModelL.setValueByVariable("DEPT_NAME", clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, DeptID), NewRow);

                DoNotEvaluate = false;
                TableL.changeSelection(NewRow, 0, false, false);
                UpdateResults(DataModelL.getColFromVariable("QTY"));
                DoNotEvaluate = true;
            }

            UpdateSrNo();
            DoNotEvaluate = false;
        }
    }//GEN-LAST:event_cmdInsertActionPerformed


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
            OpgFinal.setEnabled(true);
            OpgFinal.setSelected(false);
        }

        //Set Default Send to User
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void cmdNext6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdNext6ActionPerformed

    private void cmdBack5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdBack5ActionPerformed

    private void txtSGSTTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSGSTTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSGSTTermKeyPressed

    private void txtGSTCompCessTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTCompCessTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTCompCessTermKeyPressed

    private void txtIGSTTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIGSTTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIGSTTermKeyPressed

    private void txtCompositionTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCompositionTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCompositionTermKeyPressed

    private void txtRCMTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRCMTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRCMTermKeyPressed

    private void mnuGSTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGSTActionPerformed
        // TODO add your handling code here:
        PreviewReport_GST();
    }//GEN-LAST:event_mnuGSTActionPerformed

    private void mnuNonGSTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNonGSTActionPerformed
        // TODO add your handling code here:
        PreviewReport();
    }//GEN-LAST:event_mnuNonGSTActionPerformed

    private void mnuGST1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGST1ActionPerformed
        // TODO add your handling code here:
        if (EditMode == 0) {
            frmSendMail ObjSend = new frmSendMail();
            ObjSend.ModuleID = 27 + POType;

            if (POType == 8) {
                ObjSend.ModuleID = 47;
            }

            //start add on 110909
            if (POType == 9) {
                ObjSend.ModuleID = 168;
            }
            //end add on 110909

            ObjSend.SentBy = EITLERPGLOBAL.gNewUserID;
            ObjSend.MailDocNo = txtDocNo.getText().trim();

            String FileName = "doc" + ObjSend.ModuleID + txtDocNo.getText() + ".pdf";

            String suppEMail = clsSupplier.getSupplierEMail(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());

            if (suppEMail.trim().equals("")) {
                return;
            }

            ObjSend.colRecList.clear();
            ObjSend.colRecList.put(Integer.toString(ObjSend.colRecList.size() + 1), suppEMail);

            if (chkCancelled.isSelected()) {
                JOptionPane.showMessageDialog(null, "You cannot mail cancelled document");
                return;
            }

            int FinalApprover = 0;
            String strFinalApprover = "";
            int HierarchyID = (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();

            FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
            strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

            PrepareAmendment(txtDocNo.getText(), POType);

            if (POType == 4) //Raw Material
            {

                if (HierarchyID == 247 || HierarchyID == 248) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 536 || HierarchyID == 558) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendRawMail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 5) //Import Spares
            {

                if (HierarchyID == 199 || HierarchyID == 201) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 553 || HierarchyID == 554) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendImportMail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 6) {
                if (HierarchyID == 570) {

                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital4Mail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                } else {
                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                }
            }

            if (POType != 4 && POType != 5 && POType != 6) //Others
            {
                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }
            }

            ObjSend.ShowWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Cannot send email while in edit mode. Please save the document and then try");
        }

    }//GEN-LAST:event_mnuGST1ActionPerformed

    private void mnuNonGST1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNonGST1ActionPerformed
        // TODO add your handling code here:
        if (EditMode == 0) {
            frmSendMail ObjSend = new frmSendMail();
            ObjSend.ModuleID = 27 + POType;

            if (POType == 8) {
                ObjSend.ModuleID = 47;
            }

            //start add on 110909
            if (POType == 9) {
                ObjSend.ModuleID = 168;
            }
            //end add on 110909

            ObjSend.SentBy = EITLERPGLOBAL.gNewUserID;
            ObjSend.MailDocNo = txtDocNo.getText().trim();

            String FileName = "doc" + ObjSend.ModuleID + txtDocNo.getText() + ".pdf";

            String suppEMail = clsSupplier.getSupplierEMail(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());

            if (suppEMail.trim().equals("")) {
                return;
            }

            ObjSend.colRecList.clear();
            ObjSend.colRecList.put(Integer.toString(ObjSend.colRecList.size() + 1), suppEMail);

            if (chkCancelled.isSelected()) {
                JOptionPane.showMessageDialog(null, "You cannot mail cancelled document");
                return;
            }

            int FinalApprover = 0;
            String strFinalApprover = "";
            int HierarchyID = (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();

            FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
            strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

            PrepareAmendment(txtDocNo.getText(), POType);

            if (POType == 4) //Raw Material
            {

                if (HierarchyID == 247 || HierarchyID == 248) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 536 || HierarchyID == 558) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendRawMail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 5) //Import Spares
            {

                if (HierarchyID == 199 || HierarchyID == 201) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 553 || HierarchyID == 554) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendImportMail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 6) {
                if (HierarchyID == 570) {

                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital4Mail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                } else {
                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                }
            }

            if (POType != 4 && POType != 5 && POType != 6) //Others
            {
                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }
            }

            ObjSend.ShowWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Cannot send email while in edit mode. Please save the document and then try");
        }

    }//GEN-LAST:event_mnuNonGST1ActionPerformed

    private void mnuTRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTRNActionPerformed
        // TODO add your handling code here:
        PreviewReport_TRN();
    }//GEN-LAST:event_mnuTRNActionPerformed

    private void mnuTRN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTRN1ActionPerformed
        // TODO add your handling code here:
        EmailPreviewReport_TRN();
    }//GEN-LAST:event_mnuTRN1ActionPerformed

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
    private javax.swing.JScrollPane HeaderPane;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab3;
    private javax.swing.JPanel Tab4;
    private javax.swing.JPanel Tab5;
    private javax.swing.JPanel Tab6;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableH;
    private javax.swing.JTable TableHS;
    private javax.swing.JTable TableL;
    private javax.swing.JTable TableS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkAttachement;
    private javax.swing.JCheckBox chkCancelled;
    private javax.swing.JCheckBox chkDoNotErase;
    private javax.swing.JCheckBox chkImportConcess;
    private javax.swing.JCheckBox chkImported;
    private javax.swing.JComboBox cmbBuyer;
    private javax.swing.JComboBox cmbCurrency;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JComboBox cmbShip;
    private javax.swing.JComboBox cmbTransportMode;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBack2;
    private javax.swing.JButton cmdBack3;
    private javax.swing.JButton cmdBack4;
    private javax.swing.JButton cmdBack5;
    private javax.swing.JButton cmdBrowse;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEMail;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdImportBig;
    private javax.swing.JButton cmdInsert;
    private javax.swing.JButton cmdInsertQuot;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdLine1Big;
    private javax.swing.JButton cmdLine2Big;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext1;
    private javax.swing.JButton cmdNext2;
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdNext6;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewA;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdPurposeBig;
    private javax.swing.JButton cmdRemarksBig;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdSaveAs;
    private javax.swing.JButton cmdSelect;
    private javax.swing.JButton cmdShowFile;
    private javax.swing.JButton cmdShowIndent;
    private javax.swing.JButton cmdShowPO;
    private javax.swing.JButton cmdShowQuot;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdSpecAdd;
    private javax.swing.JButton cmdSpecRemove;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JScrollPane frameSpecs;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblDeptName;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblImport;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblSpecs;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblURD;
    private javax.swing.JMenuItem mnuGST;
    private javax.swing.JMenuItem mnuGST1;
    private javax.swing.JMenuItem mnuNonGST;
    private javax.swing.JMenuItem mnuNonGST1;
    private javax.swing.JMenuItem mnuTRN;
    private javax.swing.JMenuItem mnuTRN1;
    private javax.swing.JTextField txtAdd1;
    private javax.swing.JTextField txtAdd2;
    private javax.swing.JTextField txtAdd3;
    private javax.swing.JTextField txtAmendReason;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtCGSTTerm;
    private javax.swing.JTextField txtCIETerm;
    private javax.swing.JTextField txtCenvatTerm;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCompositionTerm;
    private javax.swing.JTextField txtCurrencyRate;
    private javax.swing.JTextField txtDeptID;
    private javax.swing.JTextArea txtDespatchTerm;
    private javax.swing.JTextField txtDiscountTerm;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtExciseTerm;
    private javax.swing.JTextField txtFOBTerm;
    private javax.swing.JTextField txtFile;
    private javax.swing.JTextArea txtFileText;
    private javax.swing.JTextField txtFinalAmount;
    private javax.swing.JTextField txtFreightTerm;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JButton txtFromRemarksBig;
    private javax.swing.JTextField txtGSTCompCessTerm;
    private javax.swing.JTextField txtGrossAmount;
    private javax.swing.JTextField txtIGSTTerm;
    private javax.swing.JTextField txtImportLicense;
    private javax.swing.JTextField txtInquiryDate;
    private javax.swing.JTextField txtInquiryNo;
    private javax.swing.JTextField txtInsuranceTerm;
    private javax.swing.JTextField txtLine1;
    private javax.swing.JTextField txtLine1Code;
    private javax.swing.JTextField txtLine2;
    private javax.swing.JTextField txtLine2Code;
    private javax.swing.JTextField txtNetAmount;
    private javax.swing.JTextField txtOctroiTerm;
    private javax.swing.JTextField txtOthersTerm;
    private javax.swing.JTextField txtPFTerm;
    private javax.swing.JTextField txtPODate;
    private javax.swing.JTextField txtPONo;
    private javax.swing.JTextField txtPaymentTerm;
    private javax.swing.JTextField txtPriceBasisTerm;
    private javax.swing.JTextField txtPurpose;
    private javax.swing.JTextField txtQuotationDate;
    private javax.swing.JTextField txtQuotationNo;
    private javax.swing.JTextField txtRCMTerm;
    private javax.swing.JTextField txtReasonCode;
    private javax.swing.JTextField txtRefA;
    private javax.swing.JTextField txtRefB;
    private javax.swing.JTextField txtReference;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtSGSTTerm;
    private javax.swing.JTextField txtSTTerm;
    private javax.swing.JTextField txtServiceTax;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtSubject;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSuppName;
    private javax.swing.JTextField txtTCCTerm;
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
        try {
            if (EditMode == 0) {
                if (ObjPOAmend.getAttribute("APPROVED").getInt() == 1) {
                    lblTitle.setBackground(Color.BLUE);
                }

                if (ObjPOAmend.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                }

                if (ObjPOAmend.getAttribute("CANCELLED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }

            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = POType + 27;

            if (POType == 8) {
                ModuleID = 47;
            }

            //start add on 110909
            if (POType == 9) {
                ModuleID = 168;
            }
            //end add on 110909

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        resetColors();
        ControlDisplay();

        try {
            ClearFields();

            txtDocNo.setText((String) ObjPOAmend.getAttribute("AMEND_NO").getObj());
            lblTitle.setText(FormTitle + " - " + txtDocNo.getText());

            lblRevNo.setText(Integer.toString((int) ObjPOAmend.getAttribute("REVISION_NO").getVal()));
            txtDocDate.setText(EITLERPGLOBAL.formatDate((String) ObjPOAmend.getAttribute("AMEND_DATE").getObj()));
            txtPONo.setText((String) ObjPOAmend.getAttribute("PO_NO").getObj());
            txtPODate.setText(EITLERPGLOBAL.formatDate((String) ObjPOAmend.getAttribute("PO_DATE").getObj()));
            txtSuppCode.setText((String) ObjPOAmend.getAttribute("SUPP_ID").getObj());

            if (POType == 6 || POType == 7 || POType == 2 || POType == 1 || POType == 9) {
                if ((!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, (String) ObjPOAmend.getAttribute("SUPP_ID").getObj())) || (ObjPOAmend.getAttribute("SUPP_ID").getObj().toString().equals("000000"))) {

                    txtSuppName.setText((String) ObjPOAmend.getAttribute("SUPP_NAME").getObj());
                } else {
                    txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
                }
            } else {
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
            }

            //Instant Display
            if (!txtSuppCode.getText().trim().equals("000000")) {
                boolean HasRegNo = clsSupplier.IsRegistrationNoExist(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());
                lblURD.setVisible(!HasRegNo);
            }

            txtDeptID.setText(Integer.toString(ObjPOAmend.getAttribute("DEPT_ID").getInt())); 
            lblDeptName.setText(clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, ObjPOAmend.getAttribute("DEPT_ID").getInt()));
            txtQuotationNo.setText((String) ObjPOAmend.getAttribute("QUOTATION_NO").getObj());
            txtQuotationDate.setText(EITLERPGLOBAL.formatDate((String) ObjPOAmend.getAttribute("QUOTATION_DATE").getObj()));
            txtInquiryNo.setText((String) ObjPOAmend.getAttribute("INQUIRY_NO").getObj());
            txtInquiryDate.setText(EITLERPGLOBAL.formatDate((String) ObjPOAmend.getAttribute("INQUIRY_DATE").getObj()));
            EITLERPGLOBAL.setComboIndex(cmbBuyer, (int) ObjPOAmend.getAttribute("BUYER").getVal());
            EITLERPGLOBAL.setComboIndex(cmbTransportMode, (int) ObjPOAmend.getAttribute("TRANSPORT_MODE").getVal());
            txtReference.setText((String) ObjPOAmend.getAttribute("PO_REF").getObj());
            txtRefA.setText((String) ObjPOAmend.getAttribute("REF_A").getObj());
            txtRefB.setText((String) ObjPOAmend.getAttribute("REF_B").getObj());
            txtPurpose.setText((String) ObjPOAmend.getAttribute("PURPOSE").getObj());
            txtSubject.setText((String) ObjPOAmend.getAttribute("SUBJECT").getObj());
            EITLERPGLOBAL.setComboIndex(cmbCurrency, (int) ObjPOAmend.getAttribute("CURRENCY_ID").getVal());
            txtCurrencyRate.setText(Double.toString(ObjPOAmend.getAttribute("CURRENCY_RATE").getVal()));
            chkCancelled.setSelected(ObjPOAmend.getAttribute("CANCELLED").getBool());
            chkAttachement.setSelected(ObjPOAmend.getAttribute("ATTACHEMENT").getBool());
            txtFile.setText((String) ObjPOAmend.getAttribute("ATTACHEMENT_PATH").getObj());
            EITLERPGLOBAL.setComboIndex(cmbShip, (int) ObjPOAmend.getAttribute("SHIP_ID").getVal());

            txtServiceTax.setText((String) ObjPOAmend.getAttribute("SERVICE_TAX_TERM").getObj());
            txtOthersTerm.setText((String) ObjPOAmend.getAttribute("OTHERS_TERM").getObj());
            txtPaymentTerm.setText((String) ObjPOAmend.getAttribute("PAYMENT_TERM").getObj());

            TermCode = ObjPOAmend.getAttribute("PAYMENT_CODE").getInt();
            TermDays = ObjPOAmend.getAttribute("CR_DAYS").getInt();

            txtPriceBasisTerm.setText((String) ObjPOAmend.getAttribute("PRICE_BASIS_TERM").getObj());
            txtDiscountTerm.setText((String) ObjPOAmend.getAttribute("DISCOUNT_TERM").getObj());
            txtExciseTerm.setText((String) ObjPOAmend.getAttribute("EXCISE_TERM").getObj());
            txtSTTerm.setText((String) ObjPOAmend.getAttribute("ST_TERM").getObj());
            txtPFTerm.setText((String) ObjPOAmend.getAttribute("PF_TERM").getObj());
            txtFreightTerm.setText((String) ObjPOAmend.getAttribute("FREIGHT_TERM").getObj());
            txtOctroiTerm.setText((String) ObjPOAmend.getAttribute("OCTROI_TERM").getObj());
            txtFOBTerm.setText((String) ObjPOAmend.getAttribute("FOB_TERM").getObj());
            txtCIETerm.setText((String) ObjPOAmend.getAttribute("CIE_TERM").getObj());
            txtInsuranceTerm.setText((String) ObjPOAmend.getAttribute("INSURANCE_TERM").getObj());
            txtTCCTerm.setText((String) ObjPOAmend.getAttribute("TCC_TERM").getObj());
            txtCenvatTerm.setText((String) ObjPOAmend.getAttribute("CENVAT_TERM").getObj());
            txtDespatchTerm.setText((String) ObjPOAmend.getAttribute("DESPATCH_TERM").getObj());
            txtServiceTax.setText((String) ObjPOAmend.getAttribute("SERVICE_TAX_TERM").getObj());
            txtFileText.setText((String) ObjPOAmend.getAttribute("FILE_TEXT").getObj());

            txtCGSTTerm.setText((String) ObjPOAmend.getAttribute("CGST_TERM").getObj());
            txtSGSTTerm.setText((String) ObjPOAmend.getAttribute("SGST_TERM").getObj());
            txtIGSTTerm.setText((String) ObjPOAmend.getAttribute("IGST_TERM").getObj());
            txtCompositionTerm.setText((String) ObjPOAmend.getAttribute("COMPOSITION_TERM").getObj());
            txtRCMTerm.setText((String) ObjPOAmend.getAttribute("RCM_TERM").getObj());
            txtGSTCompCessTerm.setText((String) ObjPOAmend.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

            txtAmendReason.setText((String) ObjPOAmend.getAttribute("AMEND_REASON").getObj());
            txtRemarks.setText((String) ObjPOAmend.getAttribute("REMARKS").getObj());

            txtReasonCode.setText(Integer.toString((int) ObjPOAmend.getAttribute("REASON_CODE").getVal()));

            txtLine1.setText((String) ObjPOAmend.getAttribute("PRINT_LINE_1").getObj());
            txtLine2.setText((String) ObjPOAmend.getAttribute("PRINT_LINE_2").getObj());
            txtImportLicense.setText((String) ObjPOAmend.getAttribute("IMPORT_LICENSE").getObj());

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal());

            int SelShipID = EITLERPGLOBAL.getComboCode(cmbShip);
            clsShippingAddress ObjShip = (clsShippingAddress) clsShippingAddress.getObject(EITLERPGLOBAL.gCompanyID, SelShipID);
            txtAdd1.setText((String) ObjShip.getAttribute("ADD1").getObj());
            txtAdd2.setText((String) ObjShip.getAttribute("ADD2").getObj());
            txtAdd3.setText((String) ObjShip.getAttribute("ADD3").getObj());
            txtCity.setText((String) ObjShip.getAttribute("CITY").getObj());
            txtState.setText((String) ObjShip.getAttribute("STATE").getObj());

            chkImportConcess.setSelected(ObjPOAmend.getAttribute("IMPORT_CONCESS").getBool());
            chkImported.setSelected(ObjPOAmend.getAttribute("IMPORTED").getBool());

            //============= Display Custom Columns ========================
//            for(int i=1;i<=15;i++) {
            for (int i = 1; i <= 21; i++) {
                int ColID = (int) ObjPOAmend.getAttribute("COLUMN_" + Integer.toString(i) + "_ID").getVal();
                int Col = DataModelH.getColFromID(ColID);
                int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                String Variable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, ColID);

                if (ColID != 0) {
                    //Set the Formula
                    if (ObjPOAmend.getAttribute("COLUMN_" + Integer.toString(i) + "_FORMULA").getObj() != null) {
                        DataModelH.SetFormula(Col, (String) ObjPOAmend.getAttribute("COLUMN_" + Integer.toString(i) + "_FORMULA").getObj());
                    } else {
                        DataModelH.SetFormula(Col, "");
                    }

                    //Set the Percentage. If there
                    if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                        DataModelH.setValueByVariableEx("P_" + ColID, Double.toString(ObjPOAmend.getAttribute("COLUMN_" + Integer.toString(i) + "_PER").getVal()), 1);
                        //DataModelH.setValueByVariableEx("P_" + Variable, Double.toString(ObjPOAmend.getAttribute("COLUMN_" + Integer.toString(i) + "_PER").getVal()), 1);
                    }

                    //Set the Value
                    DataModelH.setValueByVariableEx(Variable, Double.toString(ObjPOAmend.getAttribute("COLUMN_" + Integer.toString(i) + "_AMT").getVal()), 1);
                }

            }
            //=================================================================//

            //========= Display Line Items =============//
            FormatGrid();

            DoNotEvaluate = true;

            for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                //Insert New Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);
                int NewRow = TableL.getRowCount() - 1;

                clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));

                DataModelL.setValueByVariable("SR_NO", Integer.toString(i), NewRow);
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj(), NewRow);
                String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                String HsnSacCode = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("HSN_SAC_CODE", HsnSacCode, NewRow);
                try{
                DataModelL.setValueByVariable("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj(), NewRow);
                }catch(Exception a){
                    DataModelL.setValueByVariable("VENDOR_SHADE", "", NewRow);
                }
                try{
                DataModelL.setValueByVariable("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj(), NewRow);
                }catch(Exception b){
                    DataModelL.setValueByVariable("SDML_SHADE", "", NewRow);
                }
                DataModelL.setValueByVariable("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("MAKE", (String) ObjItem.getAttribute("MAKE").getObj(), NewRow);
                DataModelL.setValueByVariable("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("TOLERANCE_LIMIT", Double.toString(ObjItem.getAttribute("TOLERANCE_LIMIT").getVal()), NewRow);
                DataModelL.setValueByVariable("RATE", Double.toString(ObjItem.getAttribute("RATE").getVal()), NewRow);
                DataModelL.setValueByVariable("UNIT_ID", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);

                DataModelL.setValueByVariable("DEPT_ID", Integer.toString((int) ObjItem.getAttribute("DEPT_ID").getVal()), NewRow);
                String DeptName = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal());
                DataModelL.setValueByVariable("DEPT_NAME", DeptName, NewRow);

                String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);
                DataModelL.setValueByVariable("DISC_PER", Double.toString(ObjItem.getAttribute("DISC_PER").getVal()), NewRow);
                DataModelL.setValueByVariable("DISC_AMT", Double.toString(ObjItem.getAttribute("DISC_AMT").getVal()), NewRow);
                DataModelL.setValueByVariable("GROSS_AMOUNT", Double.toString(ObjItem.getAttribute("TOTAL_AMOUNT").getVal()), NewRow);
                DataModelL.setValueByVariable("NET_AMOUNT", Double.toString(ObjItem.getAttribute("NET_AMOUNT").getVal()), NewRow);
                DataModelL.setValueByVariable("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("INDENT_SR_NO", Integer.toString((int) ObjItem.getAttribute("INDENT_SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj(), NewRow);
                DataModelL.setValueByVariable("DELIVERY_DATE", EITLERPGLOBAL.formatDateEx((String) ObjItem.getAttribute("DELIVERY_DATE").getObj()), NewRow);
                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), NewRow);
                DataModelL.setValueByVariable("EXCISE_GATEPASS_GIVEN", Boolean.valueOf(ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool()), NewRow);
                DataModelL.setValueByVariable("IMPORT_CONCESS", Boolean.valueOf(ObjItem.getAttribute("IMPORT_CONCESS").getBool()), NewRow);
                DataModelL.setValueByVariable("QUOT_ID", (String) ObjItem.getAttribute("QUOT_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("QUOT_SR_NO", Integer.toString((int) ObjItem.getAttribute("QUOT_SR_NO").getVal()), NewRow);

                //============= Display Custom Columns ========================
//                for(int c=1;c<=15;c++) {                
                /*for (int c = 1; c <= 21; c++) {
                    int ColID = (int) ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_ID").getVal();
                    int Col = DataModelL.getColFromID(ColID);
                    int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                    String Variable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, ColID);

                    if (ColID != 0) {
                        //Set the Formula
                        if (ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_FORMULA").getObj() != null) {
                            DataModelL.SetFormula(Col, (String) ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_FORMULA").getObj());
                        } else {
                            DataModelL.SetFormula(Col, "");
                        }

                        //Set the Percentage. If there
                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                            DataModelL.setValueByVariable("P_"+ColID, Double.toString(ObjItem.getAttribute("COLUMN_"+Integer.toString(c)+"_PER").getVal()),NewRow);
                            //DataModelL.setValueByVariable("P_" + Variable, Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_PER").getVal()), NewRow);
                        }

                        //Set the Value
                        DataModelL.setValueByVariable(Variable, Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_AMT").getVal()), NewRow);
                    }
                }*/
                //REFERENCE  FROM MIR CODE START
                for (int c = 1; c <= 21; c++) {
                    //Get the Column ID
                    int lnColID = (int) ObjItem.getAttribute("COLUMN_" + c + "_ID").getVal();
                    String strVariable = "";
                    double lnPercentValue = 0, lnValue = 0;

                    //Record the values
                    lnPercentValue = ObjItem.getAttribute("COLUMN_" + c + "_PER").getVal();
                    lnValue = ObjItem.getAttribute("COLUMN_" + c + "_AMT").getVal();
                    //System.out.println("total value "+lnValue+" , PER : "+lnPercentValue+" , CAPTION : "+ObjItem.getAttribute("COLUMN_"+c+"_CAPTION").getObj());

                    if (lnColID > 0) //Column ID set .. Continue
                    {
                        //Get the Variable Name
                        strVariable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, lnColID);
                    }

                    //We have variable Name - Get the column no. of this form
                    int lnDestCol = DataModelL.getColFromVariable(strVariable);

                    if (lnDestCol >= 0) //We have found the column
                    {
                        //Replace the value of this form
                        DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);

                        //Now check that percentage is used
                        int lnTaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, lnColID);
                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, lnTaxID)) {
                            DataModelL.setValueAt(Double.toString(lnPercentValue), NewRow, lnDestCol - 1);
                            DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);
                        } else {
                            DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);
                        }
                    }

                }
                //REFERENCE FROM MIR CODE END
                //=================================================================//
            }

            //============= P.O. Terms ==============//
            //========= Display Line Items =============//
            FormatTermsGrid();
            for (int i = 1; i <= ObjPOAmend.colPOTerms.size(); i++) {
                //Insert New Row
                Object[] rowData = new Object[3];
                clsPOTerms ObjItem = (clsPOTerms) ObjPOAmend.colPOTerms.get(Integer.toString(i));
                String TermType = (String) ObjItem.getAttribute("TERM_TYPE").getObj();

                /*if(TermType.equals("P")) {
                 
                 rowData[0]=Integer.toString(TableP.getRowCount()+1);
                 rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                 rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                 DataModelP.addRow(rowData);
                 }
                 
                 if(TermType.equals("D")) {
                 
                 rowData[0]=Integer.toString(TableD.getRowCount()+1);
                 rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                 rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                 DataModelD.addRow(rowData);
                 }
                 
                 if(TermType.equals("O")) {
                 
                 rowData[0]=Integer.toString(TableO.getRowCount()+1);
                 rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                 rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                 DataModelO.addRow(rowData);
                 }*/
                if (TermType.equals("S")) {

                    rowData[0] = Integer.toString(TableS.getRowCount() + 1);
                    rowData[1] = Integer.toString((int) ObjItem.getAttribute("TERM_CODE").getVal());
                    rowData[2] = (String) ObjItem.getAttribute("TERM_DESC").getObj();
                    DataModelS.addRow(rowData);
                }

            }
            //=======================================//

            DoNotEvaluate = false;

            //UpdateResults_H(0);
            UpdateAmounts();
            UpdateSrNo();

            if (EditMode == 0) {
                DataModelL.TableReadOnly(true);
                DataModelH.TableReadOnly(true);
            }
            //=========================================//

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = (String) ObjPOAmend.getAttribute("AMEND_NO").getObj();
            int ModuleID = (POType + 27);
            //start add on 110909
            if (POType == 9) {
                ModuleID = 168;
            }
            //end add on 110909
            List = ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, ModuleID, DocNo);
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

            //Showing Audit Trial History
            FormatGridHS();
            HashMap History = clsPOAmendGen.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo, POType);
            for (int i = 1; i <= History.size(); i++) {
                clsPOAmendGen ObjHistory = (clsPOAmendGen) History.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjHistory.getAttribute("ENTRY_DATE").getObj());

                String ApprovalStatus = "";

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                rowData[3] = ApprovalStatus;
                rowData[4] = (String) ObjHistory.getAttribute("APPROVER_REMARKS").getObj();

                DataModelHS.addRow(rowData);
            }
            //============================================================//

            //============================================================//
            ShowMessage("Ready");

            ShowChangedItems();
        } catch (Exception e) {

        }
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjPOAmend.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
        ObjPOAmend.setAttribute("PREFIX", SelPrefix);
        ObjPOAmend.setAttribute("PO_NO", txtPONo.getText());
        ObjPOAmend.setAttribute("PO_DATE", EITLERPGLOBAL.formatDateDB(txtPODate.getText()));
        ObjPOAmend.setAttribute("AMEND_DATE", EITLERPGLOBAL.formatDateDB(txtDocDate.getText()));
        ObjPOAmend.setAttribute("SUPP_ID", txtSuppCode.getText());
        ObjPOAmend.setAttribute("SUPP_NAME", txtSuppName.getText());
        ObjPOAmend.setAttribute("PO_REF", txtReference.getText());
        ObjPOAmend.setAttribute("REF_A", txtRefA.getText());
        ObjPOAmend.setAttribute("REF_B", txtRefB.getText());
        ObjPOAmend.setAttribute("QUOTATION_NO", txtQuotationNo.getText());
        ObjPOAmend.setAttribute("QUOTATION_DATE", EITLERPGLOBAL.formatDateDB(txtQuotationDate.getText()));
        ObjPOAmend.setAttribute("INQUIRY_NO", txtInquiryNo.getText());
        ObjPOAmend.setAttribute("INQUIRY_DATE", EITLERPGLOBAL.formatDateDB(txtInquiryDate.getText()));
        ObjPOAmend.setAttribute("BUYER", EITLERPGLOBAL.getComboCode(cmbBuyer));
        ObjPOAmend.setAttribute("TRANSPORT_MODE", EITLERPGLOBAL.getComboCode(cmbTransportMode));
        ObjPOAmend.setAttribute("PURPOSE", txtPurpose.getText());
        ObjPOAmend.setAttribute("SUBJECT", txtSubject.getText());
        ObjPOAmend.setAttribute("CURRENCY_ID", EITLERPGLOBAL.getComboCode(cmbCurrency));
        ObjPOAmend.setAttribute("CURERNCY_RATE", EITLERPGLOBAL.round(Double.parseDouble(txtCurrencyRate.getText()), 5));
        ObjPOAmend.setAttribute("TOTAL_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(txtGrossAmount.getText()), 5));
        ObjPOAmend.setAttribute("NET_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(txtNetAmount.getText()), 5));
        ObjPOAmend.setAttribute("REMARKS", txtRemarks.getText());
        ObjPOAmend.setAttribute("SHIP_ID", EITLERPGLOBAL.getComboCode(cmbShip));
        ObjPOAmend.setAttribute("REASON_CODE", Integer.parseInt(txtReasonCode.getText()));
        ObjPOAmend.setAttribute("AMEND_REASON", txtAmendReason.getText());
        ObjPOAmend.setAttribute("IMPORT_CONCESS", chkImportConcess.isSelected());
        ObjPOAmend.setAttribute("IMPORTED", chkImported.isSelected());
        ObjPOAmend.setAttribute("PRINT_LINE_1", txtLine1.getText());
        ObjPOAmend.setAttribute("PRINT_LINE_2", txtLine2.getText());
        ObjPOAmend.setAttribute("IMPORT_LICENSE", txtImportLicense.getText());
        ObjPOAmend.setAttribute("PAYMENT_TERM", txtPaymentTerm.getText());
        ObjPOAmend.setAttribute("DEPT_ID", txtDeptID.getText());
        
        ObjPOAmend.setAttribute("PAYMENT_CODE", TermCode);
        ObjPOAmend.setAttribute("CR_DAYS", TermDays);

        ObjPOAmend.setAttribute("PRICE_BASIS_TERM", txtPriceBasisTerm.getText());
        ObjPOAmend.setAttribute("DISCOUNT_TERM", txtDiscountTerm.getText());
        ObjPOAmend.setAttribute("EXCISE_TERM", txtExciseTerm.getText());
        ObjPOAmend.setAttribute("ST_TERM", txtSTTerm.getText());
        ObjPOAmend.setAttribute("PF_TERM", txtPFTerm.getText());
        ObjPOAmend.setAttribute("FREIGHT_TERM", txtFreightTerm.getText());
        ObjPOAmend.setAttribute("OCTROI_TERM", txtOctroiTerm.getText());
        ObjPOAmend.setAttribute("FOB_TERM", txtFOBTerm.getText());
        ObjPOAmend.setAttribute("CIE_TERM", txtCIETerm.getText());
        ObjPOAmend.setAttribute("INSURANCE_TERM", txtInsuranceTerm.getText());
        ObjPOAmend.setAttribute("TCC_TERM", txtTCCTerm.getText());
        ObjPOAmend.setAttribute("CENVAT_TERM", txtCenvatTerm.getText());
        ObjPOAmend.setAttribute("DESPATCH_TERM", txtDespatchTerm.getText());
        ObjPOAmend.setAttribute("SERVICE_TAX_TERM", txtServiceTax.getText());
        ObjPOAmend.setAttribute("OTHERS_TERM", txtOthersTerm.getText());

        ObjPOAmend.setAttribute("CGST_TERM", txtCGSTTerm.getText());
        ObjPOAmend.setAttribute("SGST_TERM", txtSGSTTerm.getText());
        ObjPOAmend.setAttribute("IGST_TERM", txtIGSTTerm.getText());
        ObjPOAmend.setAttribute("COMPOSITION_TERM", txtCompositionTerm.getText());
        ObjPOAmend.setAttribute("RCM_TERM", txtRCMTerm.getText());
        ObjPOAmend.setAttribute("GST_COMPENSATION_CESS_TERM", txtGSTCompCessTerm.getText());

        ObjPOAmend.setAttribute("FILE_TEXT", txtFileText.getText());
        ObjPOAmend.setAttribute("DIRECTOR_APPROVAL", IsDirectorApprovalRequired());

        if (chkCancelled.isSelected()) {
            ObjPOAmend.setAttribute("CANCELLED", true);
        } else {
            ObjPOAmend.setAttribute("CANCELLED", false);
        }

        if (chkAttachement.isSelected()) {
            ObjPOAmend.setAttribute("ATTACHEMENT", true);
            ObjPOAmend.setAttribute("ATTACHEMENT_PATH", txtFile.getText());
        } else {
            ObjPOAmend.setAttribute("ATTACHEMENT", false);
            ObjPOAmend.setAttribute("ATTACHEMENT_PATH", "");
        }

        ObjPOAmend.setAttribute("PO_TYPE", POType);

        //----- Update Approval Specific Fields -----------//
        ObjPOAmend.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjPOAmend.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjPOAmend.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjPOAmend.setAttribute("FROM_REMARKS", txtToRemarks.getText());
        ObjPOAmend.setAttribute("SEND_DOC_TO", 0);

        if (OpgApprove.isSelected()) {
            ObjPOAmend.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjPOAmend.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjPOAmend.setAttribute("APPROVAL_STATUS", "R");
            ObjPOAmend.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjPOAmend.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjPOAmend.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjPOAmend.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjPOAmend.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjPOAmend.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }

        //============= Set Custom Columns ========================
        int ColCounter = 0;

        for (int i = 0; i < TableH.getRowCount(); i++) {
            double lnPercentValue = 0;
            int ColID = DataModelH.getColID(i);
            int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
            String Variable = DataModelH.getVariable(i);

            if ((ColID != 0) && (ColID != -99) && (!Variable.substring(0, 2).equals("P_"))) {
                ColCounter++;
                ObjPOAmend.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_ID", ColID);
                ObjPOAmend.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_FORMULA", DataModelH.getFormula(i));

                if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                    lnPercentValue = Double.parseDouble(DataModelH.getValueByVariableEx("P_" + ColID, 1));
                    ObjPOAmend.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_PER", EITLERPGLOBAL.round(lnPercentValue, 5));
                }
                ObjPOAmend.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_AMT", EITLERPGLOBAL.round(Double.parseDouble(DataModelH.getValueByVariableEx(Variable, 1)), 5));
                ObjPOAmend.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_CAPTION", (String) TableH.getValueAt(i, 0));
            }
        }
        //=================================================================

        //=================== Setting up Line Items ==================//
        ObjPOAmend.colPOItems.clear();

        for (int i = 0; i < TableL.getRowCount(); i++) {
            clsPOAmendItem ObjItem = new clsPOAmendItem();

            ObjItem.setAttribute("SR_NO", DataModelL.getValueByVariable("SR_NO", i));
            ObjItem.setAttribute("ITEM_ID", DataModelL.getValueByVariable("ITEM_ID", i));
            try{
            ObjItem.setAttribute("VENDOR_SHADE", DataModelL.getValueByVariable("VENDOR_SHADE", i));
            }catch(Exception a){
                ObjItem.setAttribute("VENDOR_SHADE", "");
            }
            try{
            ObjItem.setAttribute("SDML_SHADE", DataModelL.getValueByVariable("SDML_SHADE", i));
            }catch(Exception b){
                ObjItem.setAttribute("SDML_SHADE", "");
            }
            ObjItem.setAttribute("ITEM_DESC", DataModelL.getValueByVariable("ITEM_DESC", i));
            ObjItem.setAttribute("MAKE", DataModelL.getValueByVariable("MAKE", i));
            ObjItem.setAttribute("PRICE_LIST_NO", DataModelL.getValueByVariable("PRICE_LIST_NO", i));
            ObjItem.setAttribute("PART_NO", DataModelL.getValueByVariable("PART_NO", i));
            ObjItem.setAttribute("EXCISE_TARRIF_NO", DataModelL.getValueByVariable("EXCISE_TARRIF_NO", i));
            ObjItem.setAttribute("QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("QTY", i)), 3));
            ObjItem.setAttribute("TOLERANCE_LIMIT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("TOLERANCE_LIMIT", i)), 3));
            ObjItem.setAttribute("RATE", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("RATE", i)), 5));
            ObjItem.setAttribute("UNIT", Integer.parseInt(DataModelL.getValueByVariable("UNIT_ID", i)));
            ObjItem.setAttribute("DEPT_ID", Integer.parseInt(DataModelL.getValueByVariable("DEPT_ID", i)));
            ObjItem.setAttribute("DISC_PER", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("DISC_PER", i)), 5));
            ObjItem.setAttribute("DISC_AMT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("DISC_AMT", i)), 5));
            ObjItem.setAttribute("TOTAL_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("GROSS_AMOUNT", i)), 5));
            ObjItem.setAttribute("NET_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("NET_AMOUNT", i)), 5));
            ObjItem.setAttribute("INDENT_NO", DataModelL.getValueByVariable("INDENT_NO", i));
            ObjItem.setAttribute("INDENT_SR_NO", Integer.parseInt(DataModelL.getValueByVariable("INDENT_SR_NO", i)));
            ObjItem.setAttribute("REFERENCE", DataModelL.getValueByVariable("REFERENCE", i));
            ObjItem.setAttribute("DELIVERY_DATE", EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i)));
            ObjItem.setAttribute("REMARKS", DataModelL.getValueByVariable("REMARKS", i));
            ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN", DataModelL.getBoolValueByVariable("EXCISE_GATEPASS_GIVEN", i));
            ObjItem.setAttribute("IMPORT_CONCESS", DataModelL.getBoolValueByVariable("IMPORT_CONCESS", i));
            ObjItem.setAttribute("QUOT_ID", DataModelL.getValueByVariable("QUOT_ID", i));
            ObjItem.setAttribute("QUOT_SR_NO", Integer.parseInt(DataModelL.getValueByVariable("QUOT_SR_NO", i)));
            //============= Set Custom Columns ========================//
            ColCounter = 0;

            for (int c = 0; c < TableL.getColumnCount() - 1; c++) {
                double lnPercentValue = 0;
                int ColID = DataModelL.getColID(c);
                int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                String Variable = DataModelL.getVariable(c);

                if ((ColID != 0) && (ColID != -99) && (!Variable.substring(0, 2).equals("P_"))) {
                    ColCounter++;
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_ID", ColID);
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_FORMULA", DataModelL.getFormula(c));

                    if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                        lnPercentValue = Double.parseDouble(DataModelL.getValueByVariable("P_" + ColID, i));
                        //lnPercentValue = Double.parseDouble(DataModelL.getValueByVariable("P_" + Variable, i));
                        ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_PER", EITLERPGLOBAL.round(lnPercentValue, 5));
                    }
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_AMT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable(Variable, i)), 5));
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_CAPTION", TableL.getColumnName(c));
                }
            }
            //===========================================================//

            ObjPOAmend.colPOItems.put(Integer.toString(ObjPOAmend.colPOItems.size() + 1), ObjItem);
        }
        //======================Completed ===========================//

        int TermCounter = 0;

        ObjPOAmend.colPOTerms.clear();

        /*for(int i=0;i<TableP.getRowCount();i++) {
         TermCounter++;
         
         clsPOTerms ObjItem=new clsPOTerms();
         
         ObjItem.setAttribute("TERM_TYPE","P");
         if(EITLERPGLOBAL.IsNumber((String)TableP.getValueAt(i, 1))) {
         ObjItem.setAttribute("TERM_CODE",Integer.parseInt((String)TableP.getValueAt(i, 1)));
         }
         else {
         ObjItem.setAttribute("TERM_CODE",0);
         }
         ObjItem.setAttribute("TERM_DESC",(String)TableP.getValueAt(i,2));
         
         ObjPOAmend.colPOTerms.put(Integer.toString(TermCounter), ObjItem);
         }
         
         for(int i=0;i<TableD.getRowCount();i++) {
         TermCounter++;
         
         clsPOTerms ObjItem=new clsPOTerms();
         
         ObjItem.setAttribute("TERM_TYPE","D");
         if(EITLERPGLOBAL.IsNumber((String)TableD.getValueAt(i, 1))) {
         ObjItem.setAttribute("TERM_CODE",Integer.parseInt((String)TableD.getValueAt(i, 1)));
         }
         else {
         ObjItem.setAttribute("TERM_CODE",0);
         }
         ObjItem.setAttribute("TERM_DESC",(String)TableD.getValueAt(i,2));
         
         ObjPOAmend.colPOTerms.put(Integer.toString(TermCounter), ObjItem);
         }
         
         for(int i=0;i<TableO.getRowCount();i++) {
         TermCounter++;
         
         clsPOTerms ObjItem=new clsPOTerms();
         
         ObjItem.setAttribute("TERM_TYPE","O");
         if(EITLERPGLOBAL.IsNumber((String)TableO.getValueAt(i, 1))) {
         ObjItem.setAttribute("TERM_CODE",Integer.parseInt((String)TableO.getValueAt(i, 1)));
         }
         else {
         ObjItem.setAttribute("TERM_CODE",0);
         }
         ObjItem.setAttribute("TERM_DESC",(String)TableO.getValueAt(i,2));
         
         ObjPOAmend.colPOTerms.put(Integer.toString(TermCounter), ObjItem);
         }*/
        for (int i = 0; i < TableS.getRowCount(); i++) {
            TermCounter++;

            clsPOTerms ObjItem = new clsPOTerms();

            ObjItem.setAttribute("TERM_TYPE", "S");
            if (EITLERPGLOBAL.IsNumber((String) TableS.getValueAt(i, 1))) {
                ObjItem.setAttribute("TERM_CODE", Integer.parseInt((String) TableS.getValueAt(i, 1)));
            } else {
                ObjItem.setAttribute("TERM_CODE", 0);
            }
            ObjItem.setAttribute("TERM_DESC", (String) TableS.getValueAt(i, 2));

            ObjPOAmend.colPOTerms.put(Integer.toString(TermCounter), ObjItem);
        }

    }

    private void SetupColumns() {
        HashMap List = new HashMap();
        HashMap ColList = new HashMap();
        int ModuleID = (POType + 27);
        //start add on 110909
        if (POType == 9) {
            ModuleID = 168;
        }
        //end add on 110909
        List = clsColumn.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND MODULE_ID=" + ModuleID + " AND HEADER_LINE='L' ORDER BY COL_ORDER");
        TableColumnModel ColModel = TableL.getColumnModel();

        for (int i = 1; i <= List.size(); i++) {
            clsColumn ObjColumn = (clsColumn) List.get(Integer.toString(i));
            int lTaxID = (int) ObjColumn.getAttribute("TAX_ID").getVal();
            int lColID = (int) ObjColumn.getAttribute("SR_NO").getVal();
            String lVariable = ObjColumn.getAttribute("VARIABLE_NAME").getString();

            clsTaxColumn ObjTax = (clsTaxColumn) clsTaxColumn.getObject((int) EITLERPGLOBAL.gCompanyID, lTaxID);
            if ((boolean) ObjTax.getAttribute("USE_PERCENT").getBool()) {
                //Add Percentage Column
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj() + "%");

                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);

                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);

                //Set Variable for % Column. It will be P_ID
                DataModelL.SetVariable(TableL.getColumnCount() - 1, "P_" + Integer.toString(lColID));
                //DataModelL.SetVariable(TableL.getColumnCount() - 1, "P_" + lVariable);
                //Set the Operation Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");

                //Set stat - Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, true);

                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }

                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj());

                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);

                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);

                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() != null) {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                } else {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, "  ");
                }

                //Set the Operation Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());

                //Set stat - Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());

                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }
            } else {
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj());

                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);

                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() == null) {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, "  ");
                } else {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }

                //Set the Operation Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());

                //Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());

                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }
            }
        }

        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableL.setRowSelectionAllowed(true);
        TableL.setColumnSelectionAllowed(true);
        int ModuleId = (POType + 27);
        //start add on 110909
        if (POType == 9) {
            ModuleId = 168;
        }
        //end add on 110909

        ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId + " AND HIDDEN=0 AND SHOW_LAST=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for (int i = 1; i <= ColList.size(); i++) {
            clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));

            //Add Column First
            DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj()); //0

            if (ObjColumn.getAttribute("NUMERIC").getBool()) {
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
            } else {
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, false);
            }

            String Variable = (String) ObjColumn.getAttribute("VARIABLE").getObj();

            if (Variable.equals("QTY") || Variable.equals("RATE") || Variable.equals("GROSS_AMOUNT") || Variable.equals("NET_AMOUNT")) {
                DataModelL.SetColID(TableL.getColumnCount() - 1, -99);
            } else {
                DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
            }

            DataModelL.SetVariable(TableL.getColumnCount() - 1, Variable.trim());
            DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
            DataModelL.SetInclude(TableL.getColumnCount() - 1, true);

            if (ObjColumn.getAttribute("READONLY").getBool()) {
                DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
            }
        }

        ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId + " AND HIDDEN=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for (int i = 1; i <= ColList.size(); i++) {
            clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));

            //Add Column First
            DataModelL.addColumn(ObjColumn.getAttribute("CAPTION").getObj()); //
            DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
            DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE").getObj());
            DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
            DataModelL.SetInclude(TableL.getColumnCount() - 1, true);
            DataModelL.SetNumeric(TableL.getColumnCount() - 1, ObjColumn.getAttribute("NUMERIC").getBool());
            DataModelL.SetReadOnly(TableL.getColumnCount() - 1);

            //Hide the Column
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setMaxWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setWidth(0);
        }

        int ImportCol = DataModelL.getColFromVariable("EXCISE_GATEPASS_GIVEN");
        Renderer.setCustomComponent(ImportCol, "CheckBox");
        JCheckBox aCheckBox = new JCheckBox();
        aCheckBox.setBackground(Color.WHITE);
        TableL.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
        TableL.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);

        int ImportCol2 = DataModelL.getColFromVariable("IMPORT_CONCESS");
        Renderer.setCustomComponent(ImportCol2, "CheckBox");
        aCheckBox = new JCheckBox();
        aCheckBox.setBackground(Color.WHITE);
        TableL.getColumnModel().getColumn(ImportCol2).setCellEditor(new DefaultCellEditor(aCheckBox));
        TableL.getColumnModel().getColumn(ImportCol2).setCellRenderer(Renderer);

    }

    private void FormatGrid() {
        HashMap ColList = new HashMap();

        try {
            txtGrossAmount.requestFocus();

            DataModelL = new EITLTableModel();

            TableL.removeAll();
            TableL.setModel(DataModelL);

            //Set the table Readonly
            DataModelL.TableReadOnly(false);

            int ModuleID = (POType + 27);
            //start add on 110909
            if (POType == 9) {
                ModuleID = 168;
            }
            //end add on 110909

            ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleID + " AND HIDDEN=0 AND SHOW_LAST=0 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
            for (int i = 1; i <= ColList.size(); i++) {
                clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));

                //Add Column First
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj()); //0

                if (ObjColumn.getAttribute("NUMERIC").getBool()) {
                    DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
                } else {
                    DataModelL.SetNumeric(TableL.getColumnCount() - 1, false);
                }

                String Variable = (String) ObjColumn.getAttribute("VARIABLE").getObj();

                if (Variable.equals("QTY") || Variable.equals("RATE") || Variable.equals("GROSS_AMOUNT") || Variable.equals("NET_AMOUNT")) {
                    DataModelL.SetColID(TableL.getColumnCount() - 1, -99);
                } else {
                    DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
                }

                DataModelL.SetVariable(TableL.getColumnCount() - 1, Variable.trim());
                DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
                DataModelL.SetInclude(TableL.getColumnCount() - 1, true);

                if (ObjColumn.getAttribute("READONLY").getBool()) {
                    DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
                }
            }

            SetupColumns();

            //Now hide the column 1
            TableColumnModel ColModel = TableL.getColumnModel();
            TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            //------- Install Table List Selection Listener ------//
            TableL.getColumnModel().getSelectionModel().addListSelectionListener(
                    new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    //=============== Cell Editing Routine =======================//
                    try {
                        cellLastValueL = (String) TableL.getValueAt(TableL.getSelectedRow(), TableL.getSelectedColumn());

                        TableL.editCellAt(TableL.getSelectedRow(), TableL.getSelectedColumn());
                        if (TableL.getEditorComponent() instanceof JTextComponent) {
                            ((JTextComponent) TableL.getEditorComponent()).selectAll();
                        }
                    } catch (Exception cell) {
                    }
                    //============= Cell Editing Routine Ended =================//

                    int last = TableL.getSelectedColumn();
                    String strVar = DataModelL.getVariable(last);

                    ShowMessage("Ready");
                    try {
                        if (strVar.equals("ITEM_ID")) {
                            ShowMessage("Enter item id. Press F1 for the list of items");
                        }

                        if (strVar.equals("QTY")) {
                            ShowMessage("Enter Qty.");
                        }

                        if (strVar.equals("RATE")) {
                            ShowMessage("Enter Rate");
                        }
                    } catch (Exception v) {

                    }
                }
            }
            );
            //===================================================//

            //----- Install Table Model Event Listener -------//
            TableL.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        //=========== Cell Update Prevention Check ===========//
                        String curValue = (String) TableL.getValueAt(TableL.getSelectedRow(), e.getColumn());
                        if (curValue.equals(cellLastValueL)) {
                            return;
                        }
                        //====================================================//

                        int col = e.getColumn();

                        if (DoNotEvaluate) {
                            return;
                        }
                        if (!Updating) {
                            UpdateResults(col);
                        }

                        if (col == DataModelL.getColFromVariable("DEPT_ID")) {
                            String DeptName = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(DataModelL.getValueByVariable("DEPT_ID", TableL.getSelectedRow())));
                            DataModelL.setValueByVariable("DEPT_NAME", DeptName, TableL.getSelectedRow());
                        }

                        //If Item ID has changed
                        if (col == DataModelL.getColFromVariable("ITEM_ID")) {
                            try {
                                DoNotEvaluate = true; //Stops Formula Evaluation
                                String lItemID = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                                String lItemName = clsItem.getItemName((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lHsnSacCode = clsItem.getHsnSacCode((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lWareHouseID = clsItem.getItemWareHouseID((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lLocationID = clsItem.getItemLocationID((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                TableL.setValueAt(lItemName, TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_NAME"));
                                TableL.setValueAt(lHsnSacCode, TableL.getSelectedRow(), DataModelL.getColFromVariable("HSN_SAC_CODE"));
                                TableL.setValueAt(lWareHouseID, TableL.getSelectedRow(), DataModelL.getColFromVariable("WAREHOUSE_ID"));
                                TableL.setValueAt(lLocationID, TableL.getSelectedRow(), DataModelL.getColFromVariable("LOCATION_ID"));

                                int lItemUnit = clsItem.getItemUnit(EITLERPGLOBAL.gCompanyID, lItemID);
                                TableL.setValueAt(Integer.toString(lItemUnit), TableL.getSelectedRow(), DataModelL.getColFromVariable("UNIT_ID"));
                                String lUnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", lItemUnit);
                                TableL.setValueAt(lUnitName, TableL.getSelectedRow(), DataModelL.getColFromVariable("UNIT_NAME"));
                                DoNotEvaluate = false;
                            } catch (Exception ex) {
                                DoNotEvaluate = false;
                            }
                        }

                        //New Change if Excise is changed then Update Cenvate Amount if Excise Gatepass given is true
                        if (col == DataModelL.getColFromVariable("EXCISE") || col == (DataModelL.getColFromVariable("EXCISE") - 1)) {
                            boolean GatepassGiven = DataModelL.getBoolValueByVariable("EXCISE_GATEPASS_GIVEN", TableL.getSelectedRow());
                            if (GatepassGiven && (!Updating)) {
                                try {
                                    DoNotEvaluate = true;
                                    DataModelL.setValueAt(DataModelL.getValueByVariable("EXCISE", TableL.getSelectedRow()), TableL.getSelectedRow(), DataModelL.getColFromVariable("CENVATE"));
                                    DataModelL.setValueAt("0", TableL.getSelectedRow(), DataModelL.getColFromVariable("CENVATE") - 1);
                                    DoNotEvaluate = false;
                                } catch (Exception q) {
                                    DoNotEvaluate = false;
                                }
                            }
                        }
                        // End of new change

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GatherVariableValues_H() {
        String strVariable = "";
        int varCol = 0;
        double lnValue = 0, lnSum = 0;

        //Scan the table and gather values for variables
        colVariables_H.clear();

        myParser.initSymTab(); // clear the contents of the symbol table
        myParser.addStandardConstants();
        myParser.addComplex(); // among other things adds i to the symbol table

        for (int i = 0; i < TableH.getRowCount(); i++) {
            double lValue = 0;
            if (DataModelH.getVariable(i) != null) {
                if (!DataModelH.getVariable(i).equals("")) //If Variable not blank
                {
                    colVariables_H.put(DataModelH.getVariable(i), (String) TableH.getValueAt(i, 1));

                    //Add variable Value to Parser Table
                    if ((TableH.getValueAt(i, 1) != null) && (!TableH.getValueAt(i, 1).toString().equals(""))) {
                        lValue = Double.parseDouble((String) TableH.getValueAt(i, 1));
                    } else {
                        lValue = 0;
                    }
                    myParser.addVariable(DataModelH.getVariable(i), lValue);
                }
            }
        }

        //Gather Variables - sum of line columns
        for (int c = 0; c < TableL.getColumnCount(); c++) {
            strVariable = DataModelL.getVariable(c);
            strVariable = strVariable.trim();

            if ((strVariable != null) && (!strVariable.equals(""))) {
                varCol = DataModelL.getColFromVariable(strVariable);

                //Do the sum
                lnSum = 0;

                try {
                    for (int r = 0; r < TableL.getRowCount(); r++) {
                        String theVal = (String) DataModelL.getValueAt(r, varCol);

                        if (theVal == null) {
                        } else {
                            lnValue = Double.parseDouble(TableL.getValueAt(r, varCol).toString());
                            lnSum = lnSum + lnValue;
                        }
                    }
                } catch (Exception e) {
                }
                //Sum Complete. Add to Parser Table
                myParser.addVariable("SUM_" + strVariable, lnSum);
            }
        }
    }

    private void SetupColumns_H() {
        HashMap List = new HashMap();
        Object[] rowData;
        int ModuleID = (POType + 27);
        //start add on 110909
        if (POType == 9) {
            ModuleID = 168;
        }
        //end add on 110909

        List = clsColumn.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND MODULE_ID=" + ModuleID + " AND HEADER_LINE='H' ORDER BY COL_ORDER");
        TableColumnModel ColModel = TableH.getColumnModel();

        TableH.removeAll();

        if (List.size() <= 0) {
            HeaderPane.setVisible(false);
        }

        for (int i = 1; i <= List.size(); i++) {
            clsColumn ObjColumn = (clsColumn) List.get(Integer.toString(i));
            int lTaxID = (int) ObjColumn.getAttribute("TAX_ID").getVal();
            int lColID = (int) ObjColumn.getAttribute("SR_NO").getVal();
            String lVariable = ObjColumn.getAttribute("VARIABLE_NAME").getString();

            clsTaxColumn ObjTax = (clsTaxColumn) clsTaxColumn.getObject((int) EITLERPGLOBAL.gCompanyID, lTaxID);
            if ((boolean) ObjTax.getAttribute("USE_PERCENT").getBool()) {
                //Add Percentage Column
                //DataModelL.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj()+"%");

                rowData = new Object[2];
                rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj() + "%";
                rowData[1] = "0.00";
                DataModelH.addRow(rowData);

                //Set Column ID
                DataModelH.SetColID(TableH.getRowCount() - 1, lColID);

                //Set Variable for % Column. It will be P_ID
                DataModelH.SetVariable(TableH.getRowCount() - 1, "P_" + Integer.toString(lColID));
                //DataModelH.SetVariable(TableH.getRowCount() - 1, "P_" + lVariable);

                //Set the Operationg Add/Substract
                DataModelH.SetOperation(TableH.getRowCount() - 1, "-");

                //Set stat - Include it in calculation or not
                DataModelH.SetInclude(TableH.getRowCount() - 1, true);

                //Set Formula
                DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                rowData = new Object[2];
                rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj();
                rowData[1] = "0.00";
                DataModelH.addRow(rowData);

                //Set Column ID
                DataModelH.SetColID(TableH.getRowCount() - 1, lColID);

                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() != null) {
                    DataModelH.SetVariable(TableH.getRowCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                } else {
                    DataModelH.SetVariable(TableH.getRowCount() - 1, "  ");
                }

                //Set the Operationg Add/Substract
                DataModelH.SetOperation(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());

                //Set stat - Include it in calculation or not
                DataModelH.SetInclude(TableH.getRowCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());

                //Set Formula
                DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

            } else {
                //DataModelH.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj());
                //Set Column ID

                rowData = new Object[2];
                rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj();
                rowData[1] = "0.00";
                DataModelH.addRow(rowData);

                DataModelH.SetColID(TableH.getRowCount() - 1, lColID);

                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() == null) {
                    DataModelH.SetVariable(TableH.getRowCount() - 1, "  ");
                } else {
                    DataModelH.SetVariable(TableH.getRowCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }

                //Set the Operationg Add/Substract
                DataModelH.SetOperation(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());

                //Set stat - Include it in calculation or not
                DataModelH.SetInclude(TableH.getRowCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());

                //Set Formula
                DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());
            }
        }
    }

    private void FormatGrid_H() {
        DataModelH = new EITLTableModel();

        EITLTableCellRenderer Renderer = new EITLTableCellRenderer();

        TableH.removeAll();
        TableH.setModel(DataModelH);

        Renderer.setColor(0, 0, Color.LIGHT_GRAY);

        //Set the table Readonly
        DataModelH.TableReadOnly(false);
        DataModelH.SetReadOnly(0);
        DataModelH.SetNumeric(1, true);

        //Add Default Columns
        DataModelH.addColumn("Column");
        DataModelH.addColumn("Value");

        TableH.getColumnModel().getColumn(0).setCellRenderer(Renderer);
        SetupColumns_H();

        TableColumnModel ColModel = TableH.getColumnModel();
        TableH.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //------- Install Table List Selection Listener ------//
        TableH.getColumnModel().getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int last = TableH.getSelectedColumn();
                String strVar = DataModelH.getVariable(last);

                //=============== Cell Editing Routine =======================//
                try {
                    cellLastValueH = (String) TableH.getValueAt(TableH.getSelectedRow(), TableH.getSelectedColumn());

                    TableH.editCellAt(TableH.getSelectedRow(), TableH.getSelectedColumn());
                    if (TableH.getEditorComponent() instanceof JTextComponent) {
                        ((JTextComponent) TableH.getEditorComponent()).selectAll();
                    }
                } catch (Exception cell) {
                }
                //============= Cell Editing Routine Ended =================//

            }
        }
        );
        //===================================================//

        //----- Install Table Model Event Listener -------//
        TableH.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    //=========== Cell Update Prevention Check ===========//
                    String curValue = (String) TableH.getValueAt(TableH.getSelectedRow(), e.getColumn());
                    if (curValue.equals(cellLastValueH)) {
                        return;
                    }
                    //====================================================//

                    int col = e.getColumn();
                    int row = e.getLastRow();
                    if (!Updating_H) {
                        UpdateResults_H(row);
                    }
                }
            }
        });
    }

    private void UpdateResults_H(int pCol) {
        try {
            int ColID = 0, TaxID = 0, UpdateCol = 0;
            String strFormula = "", strItemID = "", strVariable = "", srcVariable = "", srcVar2 = "";
            double lnPercentValue = 0, lnFinalResult = 0, lnNetAmount = 0;
            Object result;
            boolean updateIt = true;
            int QtyCol = 0, RateCol = 0, GAmountCol = 0;

            Updating_H = true; //Stops Recursion

            srcVariable = DataModelH.getVariable(pCol); //Variable name of currently updated Column

            //If this column is percentage column. Variable name would be P_XXX
            //We shoule use actual variable name, it will be found on it's associated next column
            if (srcVariable.substring(0, 2).equals("P_")) {
                srcVariable = DataModelH.getVariable(pCol + 1);
            }

            GatherVariableValues_H();

            for (int i = 0; i < TableH.getRowCount(); i++) {
                strVariable = DataModelH.getVariable(i);

                ColID = DataModelH.getColID(i);

                TaxID = ObjColumn.getTaxID((int) EITLERPGLOBAL.gCompanyID, ColID);

                //Exclude Percentage Columns and System Columns
                if ((!strVariable.substring(0, 2).equals("P_")) && (ColID != 0)) {
                    //If percentage is used
                    if (ObjTax.getUsePercentage((int) EITLERPGLOBAL.gCompanyID, TaxID)) {

                        //Load the Formula for calculation
                        if ((!EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                            strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID);
                        } else {
                            strFormula = DataModelH.getFormula(i);
                        }

                        //Now Read Associated Percentage Column
                        lnPercentValue = Double.parseDouble(DataModelH.getValueByVariableEx("P_" + Integer.toString(ColID), 1));

                        //Now Parse Main expression
                        myParser.parseExpression(strFormula);
                        result = myParser.getValueAsObject();
                        if (result != null) {
                            //Now get the percentage of the main result
                            lnFinalResult = (Double.parseDouble(result.toString()) * lnPercentValue) / 100;
                            //Update the Column
                            srcVar2 = DataModelH.getVariable(pCol + 1);

                            UpdateCol = DataModelH.getColFromVariable(strVariable);

                            updateIt = false;

                            if (UpdateCol != pCol) {
                                if (UpdateCol == pCol + 1) {
                                    updateIt = true;
                                } else {
                                    if ((strFormula.indexOf(srcVariable) != -1)) { //If this column is dependent on updated column
                                        updateIt = true; //Then update it
                                    } else {
                                        if ((strFormula.indexOf("QTY") != -1) || (strFormula.indexOf("RATE") != -1) || (strFormula.indexOf("GROSS_AMOUNT") != -1)) {
                                            if (pCol == QtyCol || pCol == RateCol || pCol == GAmountCol) {
                                                updateIt = true;
                                            }
                                        }
                                    }
                                }

                                //============ New Change In Parser =============//
                                //Now Condition. First check whether percentage has been entered
                                if (lnPercentValue > 0) {
                                    //Yes Percentage Entered. Then we must update the associated column
                                    updateIt = true;
                                } else {
                                    //If not Percentage entered than check whether any value is there
                                    //Otherwise go with the Dependent decision
                                    updateIt = false;
                                }
                                //=================================================//

                            }

                            if (updateIt) {
                                DataModelH.setValueByVariableEx(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 5)), 1);
                            }
                            //Re Gather Fresh Variable Values
                            GatherVariableValues_H();
                        }
                    } else //Percentage Not Used
                    {

                        //Load the Formula for calculation
                        if ((!EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                            strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID);
                        } else {
                            strFormula = DataModelH.getFormula(i);
                        }

                        //Now Parse Main expression
                        myParser.parseExpression(strFormula);
                        result = myParser.getValueAsObject();
                        if (result != null) {
                            //Now get the percentage of the main result
                            lnFinalResult = Double.parseDouble(result.toString());
                            //Update the Column
                            UpdateCol = DataModelH.getColFromVariable(strVariable);

                            updateIt = false;

                            if (UpdateCol != pCol) {
                                if (strFormula.indexOf(srcVariable) != -1) {
                                    updateIt = true;
                                } else {
                                    updateIt = true;
                                }
                            }

                            if (updateIt) {
                                DataModelH.setValueByVariableEx(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 5)), 1);
                            }
                            //Re Gather Fresh Variable Values
                            GatherVariableValues_H();
                        }
                    }
                }
            }
            Updating_H = false;
            UpdateAmounts();
        } catch (Exception e) {
            Updating_H = false;
        }
    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";
        clsUser ObjUser = new clsUser();

        //----- Generate cmbType ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        int ModuleID = (POType + 27);
        //start add on 110909
        if (POType == 9) {
            ModuleID = 168;
        }
        //end add on 110909
        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleID);

        if (EditMode == EITLERPGLOBAL.EDIT) {

            if (EITLERPGLOBAL.gNewUserID == ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, ModuleID, txtDocNo.getText())) {
                List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleID);
            } else {
                List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleID);
            }

        }

        for (int i = 1; i <= List.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //

        //----- Generate Department Combo ------- //
        cmbCurrencyModel = new EITLComboModel();
        cmbCurrency.removeAllItems();
        cmbCurrency.setModel(cmbCurrencyModel);

        List = clsCurrency.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);
        for (int i = 1; i <= List.size(); i++) {
            clsCurrency ObjCurrency = (clsCurrency) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjCurrency.getAttribute("CURRENCY_ID").getVal();
            aData.Text = (String) ObjCurrency.getAttribute("CURRENCY_DESC").getObj();
            cmbCurrencyModel.addElement(aData);
        }
        //------------------------------ //

        //-------- Generating Buyer Combo --------//
        cmbBuyerModel = new EITLComboModel();
        cmbBuyer.removeAllItems();
        cmbBuyer.setModel(cmbBuyerModel);

        List = ObjUser.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID));
        for (int i = 1; i <= List.size(); i++) {
            ObjUser = (clsUser) List.get(Integer.toString(i));

            ComboData aData = new ComboData();

            aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
            aData.Code = (long) ObjUser.getAttribute("USER_ID").getVal();

            cmbBuyerModel.addElement(aData);
        }
        //----------------------------------------//

        //-------- Generating Buyer Combo --------//
        cmbShipModel = new EITLComboModel();
        cmbShip.removeAllItems();
        cmbShip.setModel(cmbShipModel);

        List = clsShippingAddress.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID));
        for (int i = 1; i <= List.size(); i++) {
            clsShippingAddress ObjShip = (clsShippingAddress) List.get(Integer.toString(i));

            ComboData aData = new ComboData();

            aData.Text = Integer.toString((int) ObjShip.getAttribute("SHIP_ID").getVal()) + " " + (String) ObjShip.getAttribute("CITY").getObj();
            aData.Code = (long) ObjShip.getAttribute("SHIP_ID").getVal();

            cmbShipModel.addElement(aData);
        }
        //----------------------------------------//

        cmbTrasnportModel = new EITLComboModel();
        cmbTransportMode.removeAllItems();
        cmbTransportMode.setModel(cmbTrasnportModel);

        strCondition = " WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND PARA_ID='TRANSPORT_MODE'";

        List = clsParameter.getList(strCondition);
        for (int i = 1; i <= List.size(); i++) {
            clsParameter ObjPara = (clsParameter) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjPara.getAttribute("PARA_CODE").getVal();
            aData.Text = (String) ObjPara.getAttribute("DESC").getObj();
            aData.strCode = "";
            cmbTrasnportModel.addElement(aData);
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
                int ModuleID = (POType + 27);
                //start add on 110909
                if (POType == 9) {
                    ModuleID = 168;
                }
                //end add on 110909

                List = ApprovalFlow.getRemainingUsers((int) EITLERPGLOBAL.gCompanyID, ModuleID, (String) ObjPOAmend.getAttribute("AMEND_NO").getObj());
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

    private void SetupApproval() {
        // --- Hierarchy Change Rights Check --------
        int ModuleID = (POType + 27);
        //start add on 110909
        if (POType == 9) {
            ModuleID = 168;
        }
        //end add on 110909
        /* if(POType==9) {
         if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11685)) {
         cmbHierarchy.setEnabled(true);
         }
         else {
         cmbHierarchy.setEnabled(false);
         }
         }
         else {
         if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,((POType+40)*10)+5)) {
         cmbHierarchy.setEnabled(true);
         }
         else {
         cmbHierarchy.setEnabled(false);
         }
         }*/

        //In Edit Mode Hierarchy Should be disabled
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

            int FromUserID = ApprovalFlow.getFromID((int) EITLERPGLOBAL.gCompanyID, ModuleID, (String) ObjPOAmend.getAttribute("AMEND_NO").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = ApprovalFlow.getFromRemarks((int) EITLERPGLOBAL.gCompanyID, ModuleID, FromUserID, (String) ObjPOAmend.getAttribute("AMEND_NO").getObj());

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

        boolean GrantFinal = false;
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CreditTermValidation = false;

        if (EITLERPGLOBAL.getComboCode(cmbSendTo) == 11 && EditMode == EITLERPGLOBAL.EDIT && (POType == 1 || POType == 2))// 11 - Mr. Nimish Patel (Executive Director)
        {
            try {
                tmpConn = data.getConn();

                double POValue = Double.parseDouble(txtNetAmount.getText());

                //--- Special Addition : -----------------//
                //To Grant Final Approval Rights to Audit Department.
                //In Special Cases
                //Supplier and Payment Terms Based Decision
                //(1) ===== Gen. P.O. having credit payment value less then Rs. 10000 ==========
                //Check whether Credit Payment code is there in PO Terms
                //Retrive All Credit Payment Codes from the table
                boolean CreditTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='C'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                CreditTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(CreditTermFound && POValue<10000) {
                //    GrantFinal=true;
                //}                 
                if (CreditTermFound && POValue <= 25000) {
                    GrantFinal = true;
                    CreditTermValidation = true;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end
                //================================================================//

                //(2) Gen. P.O. Order having Advance/Payment or Against Delivery Payment
                //    and Value less than 2500
                boolean AdvanceTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='A' OR PAYMENT_TYPE='D'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {

                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                AdvanceTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(AdvanceTermFound && POValue<2500) {
                //    GrantFinal=true;
                //}                 
                if (AdvanceTermFound && POValue <= 10000) {
                    GrantFinal = true;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end
                //==================================================================//

                //(3) Gen. P.O. having advance/against delivery payment and
                // Value less than 10000 for listed parties only
                //As per requirement by Audit dept on dt 23/01/2015 start
                /*
                 AdvanceTermFound=false;
                
                 stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                 rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PAYMENT_TYPE='A' OR PAYMENT_TYPE='D'");
                 rsTmp.first();
                 while(!rsTmp.isAfterLast()) {
                 int TermCode=rsTmp.getInt("PAYMENT_CODE");
                    
                 clsSupplier tmpSupp=new clsSupplier();
                 clsSupplier ObjSupp= (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());
                    
                 //Find out in Payment Terms of PO
                 for(int i=1;i<=ObjSupp.colSuppTerms.size();i++) {
                 clsSuppTerms ObjTerm=(clsSuppTerms)ObjSupp.colSuppTerms.get(Integer.toString(i));
                 String TermType=(String)ObjTerm.getAttribute("TERM_TYPE").getObj();
                        
                 if(TermType.equals("P")) {
                 int POTermCode=(int)ObjTerm.getAttribute("TERM_CODE").getVal();
                 if (POTermCode==TermCode) {
                 AdvanceTermFound=true;
                 }
                 }
                 }
                 rsTmp.next();
                 }
                
                 if(AdvanceTermFound && POValue<10000) {
                 String SuppID=txtSuppCode.getText().trim();
                    
                 //Check the party
                 stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                 rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_SUPPLIERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID='"+SuppID+"'");
                 rsTmp.first();
                 if(rsTmp.getRow()>0) {
                 //Party is listed party
                 GrantFinal=true;
                 }
                 }
                 */
                //As per requirement by Audit dept on dt 23/01/2015 end
                //==============================================================//
                //(4) P.O. for Printed stationary having value less than 10000
                boolean ItemFound = false;
                boolean NotAllItems = false;

                for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                    if (EITLERPGLOBAL.gCompanyID == 2)//BARODA UNIT
                    {
                        if (ItemID.substring(0, 2).equals("98") || ItemID.substring(0, 2).equals("97") || ItemID.substring(0, 2).equals("96")) {
                            ItemFound = true;
                        } else {
                            NotAllItems = true;
                        }
                    }

                    if (EITLERPGLOBAL.gCompanyID == 3)//ANKLESHWAR UNIT
                    {
                        if (ItemID.substring(0, 2).equals("90")) {
                            ItemFound = true;
                        } else {
                            NotAllItems = true;
                        }
                    }

                }
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(ItemFound && (!NotAllItems) && POValue<10000) {
                //    GrantFinal=true;
                //}                
                if (ItemFound && (!NotAllItems) && POValue <= 10000) {
                    GrantFinal = true;
                }
                if (ItemFound && (!NotAllItems) && POValue > 10000 && CreditTermValidation) {
                    GrantFinal = false;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end
                //==============================================================//

                //(5) Cash Purchase upto 2500 for electronics,oil seals,ice,gas cylinders
                boolean CashTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='H'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                CashTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }

                ItemFound = false;
                NotAllItems = false;

                for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                    if (ItemID.substring(0, 5).equals("60208") || ItemID.substring(0, 4).equals("7030") || ItemID.equals("60703001") || ItemID.equals("60703002") || ItemID.equals("60703004") || ItemID.equals("60703005") || ItemID.equals("96020206")) {
                        ItemFound = true;
                    } else {
                        NotAllItems = true;
                    }
                }

                //if(CashTermFound && ItemFound && (!NotAllItems) && POValue<2500) {
                //    GrantFinal=true;
                //}
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(CashTermFound && POValue<10000) {
                //    GrantFinal=true;
                //}
                if (CashTermFound && ItemFound && (!NotAllItems) && POValue <= 5000) {
                    GrantFinal = true;
                }
                //As per requirement by Audit dept on dt 23/01/2015 start

                //==============================================================//
                //(7) Purchase other than cash upto 10000 for electronics,oil seals,ice,gas cylinders
                boolean OtherTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='P' OR PAYMENT_TYPE='A' OR PAYMENT_TYPE='D' OR PAYMENT_TYPE='C'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                OtherTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }
                // NOTE :              
                /* OIL SEALS - ITEM CODE STARTING WITH 60208 ICE - FOR BARODA - 96020206 */

                ItemFound = false;
                NotAllItems = false;

                for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                    if (ItemID.substring(0, 5).equals("60208") || ItemID.substring(0, 4).equals("7030") || ItemID.equals("60703001") || ItemID.equals("60703002") || ItemID.equals("60703004") || ItemID.equals("60703005") || ItemID.equals("96020206")) {
                        ItemFound = true;
                    } else {
                        NotAllItems = true;
                    }
                }

                if (OtherTermFound && ItemFound && (!NotAllItems) && POValue <= 10000) {
                    GrantFinal = true;
                }
                if (OtherTermFound && ItemFound && (!NotAllItems) && POValue > 10000 && CreditTermValidation) {
                    GrantFinal = false;
                }

                //==============================================================//
                //(6) Cash Purchase upto 5000 for kerosene
                CashTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='H'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                CashTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }

                ItemFound = false;
                NotAllItems = false;

                for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                    if (EITLERPGLOBAL.gCompanyID == 2 || EITLERPGLOBAL.gCompanyID == 1)//BARODA UNIT
                    {
                        if (ItemID.equals("60305002")) {
                            ItemFound = true;
                        } else {
                            NotAllItems = true;
                        }
                    }

                    if (EITLERPGLOBAL.gCompanyID == 3)//ANKLESHWAR UNIT
                    {
                        if (ItemID.equals("40303002")) {
                            ItemFound = true;
                        } else {
                            NotAllItems = true;
                        }
                    }

                }
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(CashTermFound && ItemFound && (!NotAllItems)) {
                //    GrantFinal=true;
                //}                
                if (CashTermFound && ItemFound && (!NotAllItems) && POValue <= 20000) {
                    GrantFinal = true;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end                

                //==============================================================//
                //New Rule added on 9 Nov 2006.
                //For Cash Purchase upto Rs. 1000/-, Audit can approve PO.
                //As per requirement by Audit dept on dt 23/01/2015 end                
                //if(txtSuppCode.getText().equals("000000")&&POValue<=2500) {
                //    GrantFinal=true;
                //}
                //As per requirement by Audit dept on dt 23/01/2015 end                
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();

            } catch (Exception e) {

            }

            //If final approval rights conditions satisfies than
            //Grant final approval rights to Audit department user(any in hierarchy)
            //These rules works only in case of General POs
            if (POType != 1 && POType != 2 && POType != 9) {
                GrantFinal = false;
            }

            if (GrantFinal) {
                OpgFinal.setEnabled(true);
                cmbSendTo.setEnabled(true);
                ObjPOAmend.setAttribute("DIRECTOR_APPROVAL", false);
            } else {
                OpgFinal.setEnabled(false);
                cmbSendTo.setEnabled(false);
                ObjPOAmend.setAttribute("DIRECTOR_APPROVAL", true);
                if (EITLERPGLOBAL.gNewUserID == 243) {
                    OpgFinal.setEnabled(true);
                    cmbSendTo.setEnabled(true);
                }
            }

        }

        //Final approval validation for Mr Aditya Patel 
        //Changes are done as per the requirement by audit dept 
        if (EITLERPGLOBAL.getComboCode(cmbSendTo) == 243 && EditMode == EITLERPGLOBAL.EDIT && (POType == 1 || POType == 2))// 11 - Mr. Nimish Patel (Executive Director)
        {
            try {
                tmpConn = data.getConn();

                double POValue = Double.parseDouble(txtNetAmount.getText());

                //--- Special Addition : -----------------//
                //To Grant Final Approval Rights to Audit Department.
                //In Special Cases
                //Supplier and Payment Terms Based Decision
                //(1) ===== Gen. P.O. having credit payment value less then Rs. 10000 ==========
                //Check whether Credit Payment code is there in PO Terms
                //Retrive All Credit Payment Codes from the table
                boolean CreditTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='C'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                CreditTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(CreditTermFound && POValue<10000) {
                //    GrantFinal=true;
                //}                
                if (CreditTermFound && POValue <= 25000) {
                    GrantFinal = true;
                    CreditTermValidation = true;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end                
                //================================================================//

                //(2) Gen. P.O. Order having Advance/Payment or Against Delivery Payment
                //    and Value less than 2500
                boolean AdvanceTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='A' OR PAYMENT_TYPE='D'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {

                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                AdvanceTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(AdvanceTermFound && POValue<2500) {
                //    GrantFinal=true;
                //}                
                if (AdvanceTermFound && POValue <= 10000) {
                    GrantFinal = true;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end                
                //==================================================================//

                //(3) Gen. P.O. having advance/against delivery payment and
                // Value less than 10000 for listed parties only
                //As per requirement by Audit dept on dt 23/01/2015 start
                /*
                 AdvanceTermFound=false;
                
                 stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                 rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PAYMENT_TYPE='A' OR PAYMENT_TYPE='D'");
                 rsTmp.first();
                 while(!rsTmp.isAfterLast()) {
                 int TermCode=rsTmp.getInt("PAYMENT_CODE");
                    
                 clsSupplier tmpSupp=new clsSupplier();
                 clsSupplier ObjSupp= (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());
                    
                 //Find out in Payment Terms of PO
                 for(int i=1;i<=ObjSupp.colSuppTerms.size();i++) {
                 clsSuppTerms ObjTerm=(clsSuppTerms)ObjSupp.colSuppTerms.get(Integer.toString(i));
                 String TermType=(String)ObjTerm.getAttribute("TERM_TYPE").getObj();
                        
                 if(TermType.equals("P")) {
                 int POTermCode=(int)ObjTerm.getAttribute("TERM_CODE").getVal();
                 if (POTermCode==TermCode) {
                 AdvanceTermFound=true;
                 }
                 }
                 }
                 rsTmp.next();
                 }
                
                 if(AdvanceTermFound && POValue<10000) {
                 String SuppID=txtSuppCode.getText().trim();
                    
                 //Check the party
                 stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                 rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_SUPPLIERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID='"+SuppID+"'");
                 rsTmp.first();
                 if(rsTmp.getRow()>0) {
                 //Party is listed party
                 GrantFinal=true;
                 }
                 }
                 */
                //As per requirement by Audit dept on dt 23/01/2015 end
                //==============================================================//
                //(4) P.O. for Printed stationary having value less than 10000
                boolean ItemFound = false;
                boolean NotAllItems = false;

                for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                    if (EITLERPGLOBAL.gCompanyID == 2)//BARODA UNIT
                    {
                        if (ItemID.substring(0, 2).equals("98") || ItemID.substring(0, 2).equals("97") || ItemID.substring(0, 2).equals("96")) {
                            ItemFound = true;
                        } else {
                            NotAllItems = true;
                        }
                    }

                    if (EITLERPGLOBAL.gCompanyID == 3)//ANKLESHWAR UNIT
                    {
                        if (ItemID.substring(0, 2).equals("90")) {
                            ItemFound = true;
                        } else {
                            NotAllItems = true;
                        }
                    }

                }
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(ItemFound && (!NotAllItems) && POValue<10000) {
                //    GrantFinal=true;
                //}                
                if (ItemFound && (!NotAllItems) && POValue <= 10000) {
                    GrantFinal = true;
                }
                if (ItemFound && (!NotAllItems) && POValue > 10000 && CreditTermValidation) {
                    GrantFinal = false;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end                
                //==============================================================//                

                //(5) Cash Purchase upto 2500 for electronics,oil seals,ice,gas cylinders
                boolean CashTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='H'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                CashTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }

                ItemFound = false;
                NotAllItems = false;

                for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                    if (ItemID.substring(0, 5).equals("60208") || ItemID.substring(0, 4).equals("7030") || ItemID.equals("60703001") || ItemID.equals("60703002") || ItemID.equals("60703004") || ItemID.equals("60703005") || ItemID.equals("96020206")) {
                        ItemFound = true;
                    } else {
                        NotAllItems = true;
                    }
                }

                //if(CashTermFound && ItemFound && (!NotAllItems) && POValue<2500) {
                //    GrantFinal=true;
                //}
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(CashTermFound && POValue<10000) {
                //    GrantFinal=true;
                //}                
                if (CashTermFound && ItemFound && (!NotAllItems) && POValue <= 5000) {
                    GrantFinal = true;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end                              
                //==============================================================//

                //(7) Purchase other than cash upto 10000 for electronics,oil seals,ice,gas cylinders
                boolean OtherTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='P' OR PAYMENT_TYPE='A' OR PAYMENT_TYPE='D' OR PAYMENT_TYPE='C'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                OtherTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }
                // NOTE :              
                /* OIL SEALS - ITEM CODE STARTING WITH 60208 ICE - FOR BARODA - 96020206 */

                ItemFound = false;
                NotAllItems = false;

                for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                    if (ItemID.substring(0, 5).equals("60208") || ItemID.substring(0, 4).equals("7030") || ItemID.equals("60703001") || ItemID.equals("60703002") || ItemID.equals("60703004") || ItemID.equals("60703005") || ItemID.equals("96020206")) {
                        ItemFound = true;
                    } else {
                        NotAllItems = true;
                    }
                }

                if (OtherTermFound && ItemFound && (!NotAllItems) && POValue <= 10000) {
                    GrantFinal = true;
                }
                //==============================================================//

                //(6) Cash Purchase upto 5000 for kerosene
                CashTermFound = false;

                stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='H'");
                rsTmp.first();
                while (!rsTmp.isAfterLast()) {
                    int TermCode = rsTmp.getInt("PAYMENT_CODE");

                    clsSupplier tmpSupp = new clsSupplier();
                    clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                    //Find out in Payment Terms of PO
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                            if (POTermCode == TermCode) {
                                CashTermFound = true;
                            }
                        }
                    }
                    rsTmp.next();
                }

                ItemFound = false;
                NotAllItems = false;

                for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                    clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                    String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                    if (EITLERPGLOBAL.gCompanyID == 2 || EITLERPGLOBAL.gCompanyID == 1)//BARODA UNIT
                    {
                        if (ItemID.equals("60305002")) {
                            ItemFound = true;
                        } else {
                            NotAllItems = true;
                        }
                    }

                    if (EITLERPGLOBAL.gCompanyID == 3)//ANKLESHWAR UNIT
                    {
                        if (ItemID.equals("40303002")) {
                            ItemFound = true;
                        } else {
                            NotAllItems = true;
                        }
                    }

                }
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(CashTermFound && ItemFound && (!NotAllItems)) {
                //    GrantFinal=true;
                //}                
                if (CashTermFound && ItemFound && (!NotAllItems) && POValue <= 20000) {
                    GrantFinal = true;
                }
                //As per requirement by Audit dept on dt 23/01/2015 end                              
                //==============================================================//

                //New Rule added on 9 Nov 2006.
                //For Cash Purchase upto Rs. 1000/-, Audit can approve PO.
                //As per requirement by Audit dept on dt 23/01/2015 start
                //if(txtSuppCode.getText().equals("000000")&&POValue<=2500) {
                //    GrantFinal=true;
                //}
                //As per requirement by Audit dept on dt 23/01/2015 end
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();

            } catch (Exception e) {

            }

            //If final approval rights conditions satisfies than
            //Grant final approval rights to Audit department user(any in hierarchy)
            //These rules works only in case of General POs
            if (POType != 1 && POType != 2 && POType != 9) {
                GrantFinal = false;
            }

            if (GrantFinal) {
                OpgFinal.setEnabled(true);
                cmbSendTo.setEnabled(true);
                ObjPOAmend.setAttribute("DIRECTOR_APPROVAL", false);
            } else {
                OpgFinal.setEnabled(false);
                cmbSendTo.setEnabled(false);
                ObjPOAmend.setAttribute("DIRECTOR_APPROVAL", true);
                if (EITLERPGLOBAL.gNewUserID == 243) {
                    OpgFinal.setEnabled(true);
                    cmbSendTo.setEnabled(true);
                }
            }

        }

    }

    private void SetMenuForRights() {
        // --- Add Rights --
        //start add on 110909
        int ModuleID = 27 + POType;
        if (POType == 9) {
            ModuleID = 168;
        }
        int aFunctionID = ((POType + 40) * 10) + 1;
        int dFunctionID = ((POType + 40) * 10) + 3;
        int pFunctionID = ((POType + 40) * 10) + 4;
        if (POType == 9) {
            aFunctionID = 11681;
            dFunctionID = 11683;
            pFunctionID = 11684;
        }
        //end add on 110909
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, aFunctionID)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,((POType+40)*10)+2))
         {
         cmdEdit.setEnabled(true);
         }
         else
         {
         cmdEdit.setEnabled(false);
         }*/

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, dFunctionID)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, pFunctionID)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }

        //Hide/Show Buttons based on PO Type
        if (POType == 1) {
            cmdInsert.setVisible(true);
            cmdAdd.setVisible(false);
            cmdRemove.setVisible(true);
        }

        if (POType == 2) {
            cmdInsert.setVisible(true);
            cmdAdd.setVisible(true);
            cmdRemove.setVisible(true);
        }

        if (POType == 3) {
            cmdInsert.setVisible(true);
            cmdAdd.setVisible(true);
            cmdRemove.setVisible(true);
        }

        if (POType == 4) {
            cmdInsert.setVisible(true);
            cmdAdd.setVisible(true);
            cmdRemove.setVisible(true);
        }

        if (POType == 5) {
            cmdInsert.setVisible(true);
            cmdAdd.setVisible(false);
            cmdRemove.setVisible(true);
        }

        if (POType == 6) {
            cmdInsert.setVisible(false);
            cmdAdd.setVisible(true);
            cmdRemove.setVisible(true);
        }

        if (POType == 7) {
            cmdInsert.setVisible(false);
            cmdAdd.setVisible(true);
            cmdRemove.setVisible(true);
        }

        //start add on 110909
        if (POType == 9) {
            cmdInsert.setVisible(false);
            cmdAdd.setVisible(true);
            cmdRemove.setVisible(true);
        }
        //end add on 110909

    }

    private void UpdateSrNo() {
        int SrCol = DataModelL.getColFromVariable("SR_NO");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            TableL.setValueAt(Integer.toString(i + 1), i, SrCol);
        }
    }

    private void UpdateAmounts() {

        try {
            //== Final Pass - Update the Net Amount ==
            double lnNetAmount = 0;
            double lnColValue = 0;
            double lnGrossAmount = 0, lnSumNetAmount = 0, lnSumGrossAmount = 0;
            double HeaderTotal = 0;
            int NetAmountCol = 0, GrossAmountCol = 0;

            NetAmountCol = DataModelL.getColFromVariable("NET_AMOUNT");
            GrossAmountCol = DataModelL.getColFromVariable("GROSS_AMOUNT");

            for (int i = 0; i < TableL.getRowCount(); i++) {
                if (TableL.getValueAt(i, NetAmountCol) != null) {
                    lnSumNetAmount = lnSumNetAmount + Double.parseDouble((String) TableL.getValueAt(i, NetAmountCol));
                    lnSumGrossAmount = lnSumGrossAmount + Double.parseDouble((String) TableL.getValueAt(i, GrossAmountCol));
                }
            }

            for (int c = 0; c < TableH.getRowCount(); c++) {
                if (DataModelH.getInclude(c) == false) {
                    //Read column value
                    if (TableH.getValueAt(c, 1).toString().equals("")) {
                        lnColValue = 0;
                    } else {
                        lnColValue = Double.parseDouble((String) TableH.getValueAt(c, 1));
                    }

                    if (DataModelH.getOperation(c).equals("+")) //Add
                    {
                        lnGrossAmount = lnGrossAmount + lnColValue;
                        HeaderTotal += lnColValue;
                    } else //Substract
                    {
                        lnGrossAmount = lnGrossAmount - lnColValue;
                        HeaderTotal -= lnColValue;
                    }
                }
            }

            txtGrossAmount.setText(Double.toString(EITLERPGLOBAL.round(lnSumGrossAmount, 5)));
            txtNetAmount.setText(Double.toString(EITLERPGLOBAL.round(lnSumNetAmount, 5)));
            txtFinalAmount.setText(Double.toString(EITLERPGLOBAL.round(lnSumNetAmount + lnGrossAmount, 5)));

            lnGrossAmount = 0;
            GrossAmountCol = DataModelL.getColFromVariable("NET_AMOUNT");

            for (int i = 0; i < TableL.getRowCount(); i++) {
                if (TableL.getValueAt(i, GrossAmountCol) != null) {
                    lnGrossAmount = lnGrossAmount + Double.parseDouble((String) TableL.getValueAt(i, GrossAmountCol));
                }
            }

            for (int i = 0; i < TableL.getRowCount(); i++) {
                if (TableL.getValueAt(i, NetAmountCol) != null) {
                    //======= Calculate Landed Rate =======//
                    double NetAmount = Double.parseDouble((String) TableL.getValueAt(i, NetAmountCol));
                    double GrossAmount = Double.parseDouble((String) TableL.getValueAt(i, GrossAmountCol));
                    double Percent = 0;
                    double lnQty = Double.parseDouble((String) TableL.getValueAt(i, DataModelL.getColFromVariable("QTY")));

                    double lnLandedRate = 0;

                    if (lnQty > 0) {
                        Percent = (GrossAmount * 100) / lnGrossAmount;

                        if (HeaderTotal != 0) {
                            lnLandedRate = (NetAmount / lnQty) + (((HeaderTotal * Percent) / 100) / lnQty);
                        } else {
                            lnLandedRate = (NetAmount / lnQty);
                        }

                        lnLandedRate = EITLERPGLOBAL.round(lnLandedRate, 5);
                    } else {
                        lnLandedRate = 0;
                    }

                    Updating = true;
                    DataModelL.setValueByVariable("LANDED_RATE", Double.toString(lnLandedRate), i);
                    Updating = false;
                }
            }
            //==========================================================================//
        } catch (Exception e) {

        }
    }

    private void ClearFields() {
        txtFileText.setText("");
        txtPONo.setText("");
        txtPODate.setText("");
        txtDocDate.setText("");
        txtSuppCode.setText("");
        txtReasonCode.setText("0");
        txtAmendReason.setText("");

        txtDeptID.setText("");
        lblDeptName.setText("");        
        
        txtSuppName.setText("");
        txtQuotationNo.setText("");
        txtQuotationDate.setText("");
        txtInquiryNo.setText("");
        txtInquiryDate.setText("");
        txtPurpose.setText("");
        txtSubject.setText("");
        txtReference.setText("");
        txtRefA.setText("");
        txtRefB.setText("");
        txtCurrencyRate.setText("0.00");
        txtFile.setText("");

        txtRemarks.setText("");
        chkCancelled.setSelected(false);
        chkAttachement.setSelected(false);

        txtAdd1.setText("");
        txtAdd2.setText("");
        txtAdd3.setText("");
        txtCity.setText("");
        txtState.setText("");

        txtLine1Code.setText("");
        txtLine1.setText("");
        txtLine2Code.setText("");
        txtLine2.setText("");
        txtImportLicense.setText("");
        chkImported.setSelected(false);

        txtOthersTerm.setText("");
        txtPaymentTerm.setText("");
        txtPriceBasisTerm.setText("");
        txtDiscountTerm.setText("");
        txtExciseTerm.setText("");
        txtSTTerm.setText("");
        txtPFTerm.setText("");
        txtFreightTerm.setText("");
        txtOctroiTerm.setText("");
        txtFOBTerm.setText("");
        txtCIETerm.setText("");
        txtInsuranceTerm.setText("");
        txtTCCTerm.setText("");
        txtCenvatTerm.setText("");
        txtDespatchTerm.setText("");
        txtServiceTax.setText("");

        txtCGSTTerm.setText("");
        txtSGSTTerm.setText("");
        txtIGSTTerm.setText("");
        txtCompositionTerm.setText("");
        txtRCMTerm.setText("");
        txtGSTCompCessTerm.setText("");

        FormatGrid();
        FormatGrid_H();
        FormatGridA();
        FormatTermsGrid();
        FormatGridHS();

        txtGrossAmount.setText("0.00");
        txtNetAmount.setText("0.00");
        txtToRemarks.setText("");
    }

    private void SetFields(boolean pStat) {
        cmdSelect.setEnabled(pStat);
        txtDocDate.setEnabled(pStat);
        // txtSuppCode.setEnabled(pStat);
        // txtSuppName.setEnabled(pStat);
        txtQuotationNo.setEnabled(pStat);
        txtQuotationDate.setEnabled(pStat);
        txtInquiryNo.setEnabled(pStat);
        txtInquiryDate.setEnabled(pStat);
        txtPurpose.setEnabled(pStat);
        txtSubject.setEnabled(pStat);
        txtReference.setEnabled(pStat);
        txtRefA.setEnabled(pStat);
        txtRefB.setEnabled(pStat);
        txtCurrencyRate.setEnabled(pStat);
        txtFile.setEnabled(pStat);
        txtFileText.setEnabled(pStat);
        cmdShowFile.setEnabled(pStat);
        chkImported.setEnabled(pStat);
        txtRemarks.setEnabled(pStat);
        chkCancelled.setEnabled(pStat);
        chkAttachement.setEnabled(pStat);
        cmbBuyer.setEnabled(pStat);
        cmbCurrency.setEnabled(pStat);
        cmbShip.setEnabled(pStat);
        cmdBrowse.setEnabled(pStat);
        cmbTransportMode.setEnabled(pStat);

        txtDeptID.setEnabled(pStat);
        
        txtReasonCode.setEnabled(pStat);
        txtAmendReason.setEnabled(pStat);

        txtOthersTerm.setEnabled(pStat);
        txtServiceTax.setEnabled(pStat);
        txtPriceBasisTerm.setEnabled(pStat);
        txtDiscountTerm.setEnabled(pStat);
        txtExciseTerm.setEnabled(pStat);
        txtSTTerm.setEnabled(pStat);
        txtPFTerm.setEnabled(pStat);
        txtFreightTerm.setEnabled(pStat);
        txtOctroiTerm.setEnabled(pStat);
        txtFOBTerm.setEnabled(pStat);
        txtCIETerm.setEnabled(pStat);
        txtInsuranceTerm.setEnabled(pStat);
        txtTCCTerm.setEnabled(pStat);
        txtCenvatTerm.setEnabled(pStat);
        txtDespatchTerm.setEnabled(pStat);
        txtServiceTax.setEnabled(pStat);

        txtCGSTTerm.setEnabled(pStat);
        txtSGSTTerm.setEnabled(pStat);
        txtIGSTTerm.setEnabled(pStat);
        txtCompositionTerm.setEnabled(pStat);
        txtRCMTerm.setEnabled(pStat);
        txtGSTCompCessTerm.setEnabled(pStat);

        txtLine1Code.setEnabled(pStat);
        txtLine1.setEnabled(pStat);
        txtLine2Code.setEnabled(pStat);
        txtLine2.setEnabled(pStat);
        txtImportLicense.setEnabled(pStat);

        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);

        cmdInsert.setEnabled(pStat);
        cmdInsertQuot.setEnabled(pStat);

        if (POType == 3 || POType == 4 || POType == 5) {
            cmdAdd.setEnabled(pStat);
            cmdRemove.setEnabled(pStat);
        }

        SetupApproval();

        DataModelH.TableReadOnly(!pStat);
        DataModelL.TableReadOnly(!pStat);
        DataModelS.TableReadOnly(!pStat);

        chkImportConcess.setEnabled(pStat);

        //cmdPaymentAdd.setEnabled(pStat);
        //cmdPaymentRemove.setEnabled(pStat);
        //cmdDeliveryAdd.setEnabled(pStat);
        //cmdDeliveryRemove.setEnabled(pStat);
        //cmdOtherAdd.setEnabled(pStat);
        //cmdOtherRemove.setEnabled(pStat);
        cmdSpecAdd.setEnabled(pStat);
        cmdSpecRemove.setEnabled(pStat);

        txtPaymentTerm.setEnabled(false);

        if ((POType == 6 || POType == 7 || POType == 5 || POType == 2 || POType == 9) && pStat) {
            txtPaymentTerm.setEnabled(true);
        }

    }

    private boolean Validate() {

        if (OpgReject.isSelected()) {
            return true;
        }
        if (txtReference.getText().trim().equals("") && txtRefA.getText().trim().equals("") && txtRefB.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter any reference from (Summary,Last PO or RA)");
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

        if (!EITLERPGLOBAL.IsNumber(txtCurrencyRate.getText())) {
            txtCurrencyRate.setText("0");
        }

        if (!EITLERPGLOBAL.IsNumber(txtReasonCode.getText())) {
            txtReasonCode.setText("0");
        }

        if (!EITLERPGLOBAL.IsNumber(txtLine1Code.getText())) {
            txtLine1Code.setText("0");
        }

        if (!EITLERPGLOBAL.IsNumber(txtLine2Code.getText())) {
            txtLine2Code.setText("0");
        }

        if (txtAmendReason.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the reason for amendment");
            return false;
        }

        if (TableL.getRowCount() > 0) {
            for (int i = 0; i < TableL.getRowCount(); i++) {
                int DeptID = Integer.parseInt(DataModelL.getValueByVariable("DEPT_ID", i));
                if (!data.IsRecordExist("SELECT * FROM D_COM_DEPT_MASTER WHERE DEPT_ID NOT IN ('-','--') AND DEPT_ID=" + DeptID)) {
                    JOptionPane.showMessageDialog(this, "Please insert Department...");
                    return false;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please insert at least one item...");
            return false;
        }

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int QtyCol = DataModelL.getColFromVariable("QTY");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "";
            double Qty = 0;
            double POQty = 0;
            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, QtyCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                Qty = Double.parseDouble((String) TableL.getValueAt(i, QtyCol));
                double POSrNo = Double.parseDouble((String) DataModelL.getValueAt(i, DataModelL.getColFromVariable("SR_NO")));
                String PONo = txtPONo.getText().trim();

                String POSql = "SELECT B.QTY FROM D_PUR_PO_HEADER A,D_PUR_PO_DETAIL B "
                        + "WHERE A.PO_NO = B.PO_NO "
                        + "AND A.PO_NO = '" + PONo + "' AND B.SR_NO = " + POSrNo + " "
                        + "AND B.ITEM_ID = '" + ItemID + "' ";
                POQty = data.getDoubleValueFromDB(POSql);

                if (Qty != POQty) {

                    String strSQL = "SELECT IF(SUM(B.RECEIVED_QTY - B.REJECTED_QTY) IS NULL , 0 ,SUM(B.RECEIVED_QTY - B.REJECTED_QTY)) "
                            + "FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.CANCELLED=0 AND A.MIR_NO=B.MIR_NO "
                            + "AND B.PO_NO='" + PONo + "' AND B.ITEM_ID = '" + ItemID + "' AND B.PO_SR_NO=" + POSrNo + " ";
                    double TotalQty = data.getDoubleValueFromDB(strSQL);
                    if (TotalQty > Qty) {
                        JOptionPane.showMessageDialog(this, "PO Qty " + Qty + " for Serial No. " + POSrNo + " , Total Received Qty. " + TotalQty + " \n You can not Short Close this PO Serial No.");
                        return false;
                    }
                }
            }
        }

        /*
         if(!txtRefB.getText().trim().equals("")){
         String RIAdt=txtRefB.getText().substring(txtRefB.getText().length()-10);
         RIAdt=EITLERPGLOBAL.formatDateDB(RIAdt);
         String POdt=EITLERPGLOBAL.formatDateDB(txtDocDate.getText().trim());
         //System.out.println(RIAdt);
         //System.out.println(POdt);
         //int monthdiff=EITLERPGLOBAL.getMonthDifference(POdt, RIAdt);
         //System.out.print(monthdiff);
         int Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(RIAdt), java.sql.Date.valueOf(POdt));
         //System.out.print("Days"+Days);
         if(Days>=180){
         JOptionPane.showMessageDialog(this,"RIA date is 6 months older than PO date");
         return false;
         }
         }
         else{
         for(int i=1;i<=ObjPOAmend.colPOItems.size();i++) {
         clsPOAmendItem ObjItem=(clsPOAmendItem)ObjPOAmend.colPOItems.get(Integer.toString(i));
         String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                        
         if(clsItem.IsNewItem(EITLERPGLOBAL.gCompanyID,ItemID)) {
         JOptionPane.showMessageDialog(this,"RIA required.");
         return false;  
         }
         }
         }
         */
        if (POType == 1) {
            return ValidateGen();
        }

        if (POType == 2) {
            return ValidateEng();
        }

        if (POType == 3) {
            return ValidateAClass();
        }

        if (POType == 4) {
            return ValidateRaw();
        }

        if (POType == 5) {
            return ValidateSpares();
        }

        if (POType == 6) {
            return ValidateCapital();
        }

        if (POType == 7) {
            return ValidateContract();
        }

        //start add on 110909
        if (POType == 9) {
            return ValidateAClass();
        }
        //end add on 110909
        return true;
    }

    private boolean ValidateGen() {
        int ValidEntryCount = 0;
        String IndentNo = "";
        int IndentSrNo = 0;

        Connection tmpConn = null;
        Statement stTmp = null;
        ResultSet rsTmp = null;

        //Search for Indent no.
        for (int i = 0; i < TableL.getRowCount(); i++) {
            IndentNo = DataModelL.getValueByVariable("INDENT_NO", i);
            if (IndentNo.trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please select the indent no. by pressing F1 in Indent Column");
                return false;
            }
        }

        //Validates Item Entries
        if (TableL.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter at least one item");
            return false;
        }

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int RateCol = DataModelL.getColFromVariable("RATE");
        int QtyCol = DataModelL.getColFromVariable("QTY");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "";
            double Rate = 0, Qty = 0;

            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, RateCol) != null && TableL.getValueAt(i, QtyCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                Rate = Double.parseDouble((String) TableL.getValueAt(i, RateCol));
                Qty = Double.parseDouble((String) TableL.getValueAt(i, QtyCol));

                boolean ValidDate = true;
                String theDate = DataModelL.getValueByVariable("DELIVERY_DATE", i);

                if (!EITLERPGLOBAL.isDate(theDate) || theDate.trim().equals("")) {
                    ValidDate = false;
                }

                if (!clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID)) {
                    JOptionPane.showMessageDialog(null, "Item " + ItemID + " is not valid item code");
                    return false;
                }

                if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID) && Rate > 0 && ValidDate) {
                    ValidEntryCount++;
                } else {
                    JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nValid Item ID,Rate,Quantity and Delivery Date");
                    TableL.changeSelection(i, 1, false, false);
                    return false;
                }

                //Now check the Approved RIA qty. if indent no. found
                //                IndentNo=DataModelL.getValueByVariable("INDENT_NO", i);
                //                IndentSrNo=Integer.parseInt(DataModelL.getValueByVariable("INDENT_SR_NO",i));
                //
                //                if((!IndentNo.trim().equals(""))&&IndentSrNo>0) {
                //                    try {
                //                        String strSQL="SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_DETAIL WHERE INQUIRY_NO IN (SELECT INQUIRY_NO FROM D_PUR_INQUIRY_DETAIL WHERE INDENT_NO='"+IndentNo+"'  AND INDENT_SRNO="+IndentSrNo+" AND INQUIRY_NO IN (SELECT INQUIRY_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVED=1)))  ORDER BY APPROVAL_NO DESC";
                //
                //                        tmpConn=data.getConn();
                //                        stTmp=tmpConn.createStatement();
                //                        rsTmp=stTmp.executeQuery(strSQL);
                //                        rsTmp.first();
                //
                //                        if(rsTmp.getRow()>0) {
                //                            //We have latest RIA No.
                //                            if(rsTmp.getBoolean("APPROVED")&&Qty<=rsTmp.getDouble("CURRENT_QTY")) {
                //                                //Continue
                //                            }
                //                            else {
                //                                if(OpgFinal.isSelected()) {
                //                                    JOptionPane.showMessageDialog(null,"Only "+rsTmp.getDouble("CURRENT_QTY")+" of item "+ItemID+" is approved or this item is not approved in RIA. Cannot final approve. Please reduce the qty to "+rsTmp.getDouble("CURRENT_QTY")+" or create new RIA ");
                //
                //                                }
                //                                else {
                //                                    JOptionPane.showMessageDialog(null,"Only "+rsTmp.getDouble("CURRENT_QTY")+" of item "+ItemID+" is approved or this item is not approved in RIA. Cannot final approve. This PO will not be final approved until the qty is reduced or RIA is approved");
                //                                }
                //                            }
                //
                //                        }
                //                    }
                //                    catch(Exception e) {
                //
                //                    }
                //                }
            }
        }

        if (ValidEntryCount == 0) {
            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify that you enter following information \nItemID,Rate,Qty and Delivery date");
            return false;
        }

        //Now Header level validations
        if (txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter P.O. Date");
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

        /*if(!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID,txtSuppCode.getText())) {
         JOptionPane.showMessageDialog(null,"Please enter valid supplier code");
         return false;
         }*/
        if (!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid P.O. date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtQuotationDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid quotation date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtInquiryDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid inquiry date");
            return false;
        }

        //=== Date validity -- //
        java.sql.Date docDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtPODate.getText()));

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String strDate = EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i));

            if (!strDate.trim().equals("")) {
                java.sql.Date DeliveryDateLine = java.sql.Date.valueOf(strDate);
                if (DeliveryDateLine.before(docDate)) {
                    JOptionPane.showMessageDialog(null, "Delivery date must be greater than document date");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean ValidateEng() {
        int ValidEntryCount = 0;

        //Validates Item Entries
        if (TableL.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter at least one item");
            return false;
        }

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int ItemDescCol = DataModelL.getColFromVariable("ITEM_DESC");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "", ItemDesc = "";

            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, ItemDescCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                ItemDesc = (String) TableL.getValueAt(i, ItemDescCol);

                boolean ValidDate = true;
                String theDate = DataModelL.getValueByVariable("DELIVERY_DATE", i);

                if (!EITLERPGLOBAL.isDate(theDate) || theDate.trim().equals("")) {
                    ValidDate = false;
                }

                if (!ItemID.trim().equals("")) {
                    //Validate Item code if it has been entered
                    if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID)) {
                        ValidEntryCount++;
                    } else {
                        if (!ItemDesc.trim().equals("")) {
                            ValidEntryCount++;
                        } else {
                            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nValid Item ID,Delivery Date");
                            TableL.changeSelection(i, 1, false, false);
                            return false;
                        }

                    }
                } else {
                    //If Item id not entered then description must be entered
                    if (!ItemDesc.trim().equals("")) {
                        ValidEntryCount++;
                    } else {
                        JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nItem Description,Delivery Date");
                        TableL.changeSelection(i, 1, false, false);
                        return false;
                    }

                }

            }
        }

        if (ValidEntryCount == 0) {
            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify that you enter following information \nItemID or description and delivery date");
            return false;
        }

        //Now Header level validations
        if (txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter P.O. Date");
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

        /*if(!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID,txtSuppCode.getText())) {
         JOptionPane.showMessageDialog(null,"Please enter valid supplier code");
         return false;
         }*/
        if (txtSuppName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter party name by using party code 000000");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid P.O. date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtQuotationDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid quotation date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtInquiryDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid inquiry date");
            return false;
        }

        //=== Date validity -- //
        java.sql.Date docDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtPODate.getText()));

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String strDate = EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i));

            if (!strDate.trim().equals("")) {
                java.sql.Date DeliveryDateLine = java.sql.Date.valueOf(strDate);
                if (DeliveryDateLine.before(docDate)) {
                    JOptionPane.showMessageDialog(null, "Delivery date must be greater than document date");
                    return false;
                }
            }
        }

        return true;

    }

    private boolean ValidateAClass() {
        int ValidEntryCount = 0;
        String IndentNo = "";
        int IndentSrNo = 0;

        Connection tmpConn = null;
        Statement stTmp = null;
        ResultSet rsTmp = null;

        //Validates Item Entries
        if (TableL.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter at least one item");
            return false;
        }

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int RateCol = DataModelL.getColFromVariable("RATE");
        int QtyCol = DataModelL.getColFromVariable("QTY");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "";
            double Rate = 0, Qty = 0;

            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, RateCol) != null && TableL.getValueAt(i, QtyCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                Rate = Double.parseDouble((String) TableL.getValueAt(i, RateCol));
                Qty = Double.parseDouble((String) TableL.getValueAt(i, QtyCol));

                boolean ValidDate = true;
                String theDate = DataModelL.getValueByVariable("DELIVERY_DATE", i);

                if (!theDate.trim().equals("00/00/0000")) {
                    if (!EITLERPGLOBAL.isDate(theDate) || theDate.trim().equals("")) {
                        ValidDate = false;
                    }
                }

                if (!clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID)) {
                    JOptionPane.showMessageDialog(null, "Item " + ItemID + " is not valid item code");
                    return false;
                }

                if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID) && Rate > 0 && ValidDate) {
                    ValidEntryCount++;
                } else {
                    JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nValid Item ID,Rate,Quantity and Delivery Date");
                    TableL.changeSelection(i, 1, false, false);
                    return false;
                }

                //                IndentNo=DataModelL.getValueByVariable("INDENT_NO", i);
                //                IndentSrNo=Integer.parseInt(DataModelL.getValueByVariable("INDENT_SR_NO",i));
                //
                //                if((!IndentNo.trim().equals(""))&&IndentSrNo>0) {
                //                    try {
                //                        String strSQL="SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_DETAIL WHERE INQUIRY_NO IN (SELECT INQUIRY_NO FROM D_PUR_INQUIRY_DETAIL WHERE INDENT_NO='"+IndentNo+"'  AND INDENT_SRNO="+IndentSrNo+" AND INQUIRY_NO IN (SELECT INQUIRY_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVED=1)))  ORDER BY APPROVAL_NO DESC";
                //
                //                        tmpConn=data.getConn();
                //                        stTmp=tmpConn.createStatement();
                //                        rsTmp=stTmp.executeQuery(strSQL);
                //                        rsTmp.first();
                //
                //                        if(rsTmp.getRow()>0) {
                //                            //We have latest RIA No.
                //                            if(rsTmp.getBoolean("APPROVED")&&Qty<=rsTmp.getDouble("CURRENT_QTY")) {
                //                                //Continue
                //                            }
                //                            else {
                //                                if(OpgFinal.isSelected()) {
                //                                    JOptionPane.showMessageDialog(null,"Only "+rsTmp.getDouble("CURRENT_QTY")+" of item "+ItemID+" is approved or this item is not approved in RIA. Cannot final approve. Please reduce the qty to "+rsTmp.getDouble("CURRENT_QTY")+" or create new RIA ");
                //
                //                                }
                //                                else {
                //                                    JOptionPane.showMessageDialog(null,"Only "+rsTmp.getDouble("CURRENT_QTY")+" of item "+ItemID+" is approved or this item is not approved in RIA. Cannot final approve. This PO will not be final approved until the qty is reduced or RIA is approved");
                //                                }
                //                            }
                //
                //                        }
                //                    }
                //                    catch(Exception e) {
                //
                //                    }
                //                }
            }
        }

        if (ValidEntryCount == 0) {
            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify that you enter following information \nItemID,Rate,Qty and Delivery date");
            return false;
        }

        //Now Header level validations
        if (txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter P.O. Date");
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

        if (!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText())) {
            JOptionPane.showMessageDialog(null, "Please enter valid supplier code");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid P.O. date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtQuotationDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid quotation date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtInquiryDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid inquiry date");
            return false;
        }

        //=== Date validity -- //
        java.sql.Date docDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtPODate.getText()));

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String strDate = EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i));

            if (!strDate.trim().equals("")) {
                java.sql.Date DeliveryDateLine = java.sql.Date.valueOf(strDate);
                if (DeliveryDateLine.before(docDate)) {
                    JOptionPane.showMessageDialog(null, "Delivery date must be greater than document date");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean ValidateRaw() {
        int ValidEntryCount = 0;
        String IndentNo = "";
        int IndentSrNo = 0;

        Connection tmpConn = null;
        Statement stTmp = null;
        ResultSet rsTmp = null;

        //Validates Item Entries
        if (TableL.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter at least one item");
            return false;
        }

        String ItemStartsWith = "";
        if (EITLERPGLOBAL.gCompanyID == 2) {
            ItemStartsWith = "RM1";
        } else if (EITLERPGLOBAL.gCompanyID == 3) {
            ItemStartsWith = "RM2";
        }

        for (int i = 0; i < TableL.getRowCount(); i++) {
            if (!DataModelL.getValueByVariable("ITEM_ID", i).startsWith(ItemStartsWith)) {
                JOptionPane.showMessageDialog(null, "Item Id : " + DataModelL.getValueByVariable("ITEM_ID", i) + " not valid for " + clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                return false;
            }
        }

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int RateCol = DataModelL.getColFromVariable("RATE");
        int QtyCol = DataModelL.getColFromVariable("QTY");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "";
            double Rate = 0, Qty = 0;

            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, RateCol) != null && TableL.getValueAt(i, QtyCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                Rate = Double.parseDouble((String) TableL.getValueAt(i, RateCol));
                Qty = Double.parseDouble((String) TableL.getValueAt(i, QtyCol));

                boolean ValidDate = true;
                String theDate = DataModelL.getValueByVariable("DELIVERY_DATE", i);

                if (!EITLERPGLOBAL.isDate(theDate) || theDate.trim().equals("")) {
                    ValidDate = false;
                }

                if (!clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID)) {
                    JOptionPane.showMessageDialog(null, "Item " + ItemID + " is not valid item code");
                    return false;
                }

                if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID) && Rate > 0) {
                    ValidEntryCount++;
                } else {
                    JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nValid Item ID,Rate,Quantity and Delivery Date");
                    TableL.changeSelection(i, 1, false, false);
                    return false;
                }

                //                IndentNo=DataModelL.getValueByVariable("INDENT_NO", i);
                //                IndentSrNo=Integer.parseInt(DataModelL.getValueByVariable("INDENT_SR_NO",i));
                //
                //                if((!IndentNo.trim().equals(""))&&IndentSrNo>0) {
                //                    try {
                //                        String strSQL="SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_DETAIL WHERE INQUIRY_NO IN (SELECT INQUIRY_NO FROM D_PUR_INQUIRY_DETAIL WHERE INDENT_NO='"+IndentNo+"'  AND INDENT_SRNO="+IndentSrNo+" AND INQUIRY_NO IN (SELECT INQUIRY_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVED=1)))  ORDER BY APPROVAL_NO DESC";
                //
                //                        tmpConn=data.getConn();
                //                        stTmp=tmpConn.createStatement();
                //                        rsTmp=stTmp.executeQuery(strSQL);
                //                        rsTmp.first();
                //
                //                        if(rsTmp.getRow()>0) {
                //                            //We have latest RIA No.
                //                            if(rsTmp.getBoolean("APPROVED")&&Qty<=rsTmp.getDouble("CURRENT_QTY")) {
                //                                //Continue
                //                            }
                //                            else {
                //                                if(OpgFinal.isSelected()) {
                //                                    JOptionPane.showMessageDialog(null,"Only "+rsTmp.getDouble("CURRENT_QTY")+" of item "+ItemID+" is approved or this item is not approved in RIA. Cannot final approve. Please reduce the qty to "+rsTmp.getDouble("CURRENT_QTY")+" or create new RIA ");
                //
                //                                }
                //                                else {
                //                                    JOptionPane.showMessageDialog(null,"Only "+rsTmp.getDouble("CURRENT_QTY")+" of item "+ItemID+" is approved or this item is not approved in RIA. Cannot final approve. This PO will not be final approved until the qty is reduced or RIA is approved");
                //                                }
                //                            }
                //
                //                        }
                //                    }
                //                    catch(Exception e) {
                //
                //                    }
                //                }
            }
        }

        if (ValidEntryCount == 0) {
            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify that you enter following information \nItemID,Rate,Qty and Delivery date");
            return false;
        }

        //Now Header level validations
        if (txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter P.O. Date");
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

        if (!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText())) {
            JOptionPane.showMessageDialog(null, "Please enter valid supplier code");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid P.O. date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtQuotationDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid quotation date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtInquiryDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid inquiry date");
            return false;
        }

        //=== Date validity -- //
        java.sql.Date docDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtPODate.getText()));

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String strDate = EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i));

            if (!strDate.trim().equals("")) {
                java.sql.Date DeliveryDateLine = java.sql.Date.valueOf(strDate);
                if (DeliveryDateLine.before(docDate)) {
                    JOptionPane.showMessageDialog(null, "Delivery date must be greater than document date");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean ValidateSpares() {
        int ValidEntryCount = 0;
        String IndentNo = "";
        int IndentSrNo = 0;

        Connection tmpConn = null;
        Statement stTmp = null;
        ResultSet rsTmp = null;

        //Validates Item Entries
        if (TableL.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter at least one item");
            return false;
        }

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int RateCol = DataModelL.getColFromVariable("RATE");
        int QtyCol = DataModelL.getColFromVariable("QTY");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "";
            double Rate = 0, Qty = 0;

            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, RateCol) != null && TableL.getValueAt(i, QtyCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                Rate = Double.parseDouble((String) TableL.getValueAt(i, RateCol));
                Qty = Double.parseDouble((String) TableL.getValueAt(i, QtyCol));

                boolean ValidDate = true;
                String theDate = DataModelL.getValueByVariable("DELIVERY_DATE", i);

                if (!EITLERPGLOBAL.isDate(theDate) || theDate.trim().equals("")) {
                    ValidDate = false;
                }

                if (!clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID)) {
                    JOptionPane.showMessageDialog(null, "Item " + ItemID + " is not valid item code");
                    return false;
                }

                if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID) && Rate > 0 && ValidDate) {
                    ValidEntryCount++;
                } else {
                    JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nValid Item ID,Rate,Quantity and Delivery Date");
                    TableL.changeSelection(i, 1, false, false);
                    return false;
                }

                //                IndentNo=DataModelL.getValueByVariable("INDENT_NO", i);
                //                IndentSrNo=Integer.parseInt(DataModelL.getValueByVariable("INDENT_SR_NO",i));
                //
                //                if((!IndentNo.trim().equals(""))&&IndentSrNo>0) {
                //                    try {
                //                        String strSQL="SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_DETAIL WHERE INQUIRY_NO IN (SELECT INQUIRY_NO FROM D_PUR_INQUIRY_DETAIL WHERE INDENT_NO='"+IndentNo+"'  AND INDENT_SRNO="+IndentSrNo+" AND INQUIRY_NO IN (SELECT INQUIRY_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVED=1)))  ORDER BY APPROVAL_NO DESC";
                //
                //                        tmpConn=data.getConn();
                //                        stTmp=tmpConn.createStatement();
                //                        rsTmp=stTmp.executeQuery(strSQL);
                //                        rsTmp.first();
                //
                //                        if(rsTmp.getRow()>0) {
                //                            //We have latest RIA No.
                //                            if(rsTmp.getBoolean("APPROVED")&&Qty<=rsTmp.getDouble("CURRENT_QTY")) {
                //                                //Continue
                //                            }
                //                            else {
                //                                if(OpgFinal.isSelected()) {
                //                                    JOptionPane.showMessageDialog(null,"Only "+rsTmp.getDouble("CURRENT_QTY")+" of item "+ItemID+" is approved or this item is not approved in RIA. Cannot final approve. Please reduce the qty to "+rsTmp.getDouble("CURRENT_QTY")+" or create new RIA ");
                //
                //                                }
                //                                else {
                //                                    JOptionPane.showMessageDialog(null,"Only "+rsTmp.getDouble("CURRENT_QTY")+" of item "+ItemID+" is approved or this item is not approved in RIA. Cannot final approve. This PO will not be final approved until the qty is reduced or RIA is approved");
                //                                }
                //                            }
                //
                //                        }
                //                    }
                //                    catch(Exception e) {
                //
                //                    }
                //                }
            }
        }

        if (ValidEntryCount == 0) {
            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify that you enter following information \nItemID,Rate,Qty and Delivery date");
            return false;
        }

        //Now Header level validations
        if (txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter P.O. Date");
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

        if (!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText())) {
            JOptionPane.showMessageDialog(null, "Please enter valid supplier code");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid P.O. date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtQuotationDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid quotation date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtInquiryDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid inquiry date");
            return false;
        }

        //=== Date validity -- //
        java.sql.Date docDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtPODate.getText()));

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String strDate = EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i));

            if (!strDate.trim().equals("")) {
                java.sql.Date DeliveryDateLine = java.sql.Date.valueOf(strDate);
                if (DeliveryDateLine.before(docDate)) {
                    JOptionPane.showMessageDialog(null, "Delivery date must be greater than document date");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean ValidateCapital() {
        int ValidEntryCount = 0;

        //Validates Item Entries
        if (TableL.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter at least one item");
            return false;
        }

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int ItemDescCol = DataModelL.getColFromVariable("ITEM_DESC");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "", ItemDesc = "";

            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, ItemDescCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                ItemDesc = (String) TableL.getValueAt(i, ItemDescCol);

                boolean ValidDate = true;
                String theDate = DataModelL.getValueByVariable("DELIVERY_DATE", i);

                if (!EITLERPGLOBAL.isDate(theDate) || theDate.trim().equals("")) {
                    ValidDate = false;
                }

                if (!ItemID.trim().equals("")) {
                    //Validate Item code if it has been entered
                    if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID)) {
                        ValidEntryCount++;
                    } else {
                        if (!ItemDesc.trim().equals("")) {
                            ValidEntryCount++;
                        } else {
                            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nValid Item ID,Delivery Date");
                            TableL.changeSelection(i, 1, false, false);
                            return false;
                        }

                    }
                } else {
                    //If Item id not entered then description must be entered
                    if (!ItemDesc.trim().equals("")) {
                        ValidEntryCount++;
                    } else {
                        JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nItem Description,Delivery Date");
                        TableL.changeSelection(i, 1, false, false);
                        return false;
                    }

                }

            }
        }

        if (ValidEntryCount == 0) {
            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify that you enter following information \nItemID or description and delivery date");
            return false;
        }

        //Now Header level validations
        if (txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter P.O. Date");
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

        if (!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText())) {
            JOptionPane.showMessageDialog(null, "Please enter valid supplier code");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid P.O. date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtQuotationDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid quotation date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtInquiryDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid inquiry date");
            return false;
        }

        //=== Date validity -- //
        java.sql.Date docDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtPODate.getText()));

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String strDate = EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i));

            if (!strDate.trim().equals("")) {
                java.sql.Date DeliveryDateLine = java.sql.Date.valueOf(strDate);
                if (DeliveryDateLine.before(docDate)) {
                    JOptionPane.showMessageDialog(null, "Delivery date must be greater than document date");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean ValidateContract() {
        int ValidEntryCount = 0;

        //Validates Item Entries
        if (TableL.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter at least one item");
            return false;
        }

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
        int ItemDescCol = DataModelL.getColFromVariable("ITEM_DESC");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String ItemID = "", ItemDesc = "";

            if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, ItemDescCol) != null) {
                ItemID = (String) TableL.getValueAt(i, ItemCol);
                ItemDesc = (String) TableL.getValueAt(i, ItemDescCol);

                boolean ValidDate = true;
                String theDate = DataModelL.getValueByVariable("DELIVERY_DATE", i);

                if (!EITLERPGLOBAL.isDate(theDate) || theDate.trim().equals("")) {
                    ValidDate = false;
                }

                if (!ItemID.trim().equals("") && ValidDate) {
                    //Validate Item code if it has been entered
                    if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID)) {
                        ValidEntryCount++;
                    } else {

                        if (!ItemDesc.trim().equals("") && ValidDate) {
                            ValidEntryCount++;
                        } else {
                            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nValid Item ID,Delivery Date");
                            TableL.changeSelection(i, 1, false, false);
                            return false;
                        }

                    }
                } else {
                    //If Item id not entered then description must be entered
                    if (!ItemDesc.trim().equals("") && ValidDate) {
                        ValidEntryCount++;
                    } else {
                        JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nItem Description,Delivery Date");
                        TableL.changeSelection(i, 1, false, false);
                        return false;
                    }

                }

            }
        }

        if (ValidEntryCount == 0) {
            JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify that you enter following information \nItemID or description and Delivery date");
            return false;
        }

        //Now Header level validations
        if (txtDocDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter P.O. Date");
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

        if (!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText())) {
            JOptionPane.showMessageDialog(null, "Please enter valid supplier code");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtDocDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid P.O. date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtQuotationDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid quotation date");
            return false;
        }

        if (!EITLERPGLOBAL.isDate(txtInquiryDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid inquiry date");
            return false;
        }

        //=== Date validity -- //
        java.sql.Date docDate = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtPODate.getText()));

        for (int i = 0; i < TableL.getRowCount(); i++) {
            String strDate = EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i));

            if (!strDate.trim().equals("")) {
                java.sql.Date DeliveryDateLine = java.sql.Date.valueOf(strDate);
                if (DeliveryDateLine.before(docDate)) {
                    JOptionPane.showMessageDialog(null, "Delivery date must be greater than document date");
                    return false;
                }
            }
        }

        return true;
    }

    private void SetNumberFormats() {
        /* DecimalFormat decimalFormat=new DecimalFormat("0.00");
         NumberFormatter ObjFormater=new NumberFormatter(decimalFormat);
         ObjFormater.setAllowsInvalid(false);
         txtCurrencyRate.setFormatterFactory(new DefaultFormatterFactory(ObjFormater));*/
    }

    private void Add() {

        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String PONo = "";
        LOV aList = new LOV();

        aList.SQL = "SELECT DATE_FORMAT(PO_DATE,'%d/%m/%Y') AS PO_DATE,PO_NO FROM D_PUR_PO_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND APPROVED=1 AND CANCELLED=0 AND PO_TYPE=" + POType;
        aList.ReturnCol = 2;
        aList.ShowReturnCol = true;
        aList.DefaultSearchOn = 2;

        if (aList.ShowLOV()) {
            PONo = aList.ReturnVal;

            if (clsPOAmendGen.IsAmendmentUnderProcess(EITLERPGLOBAL.gCompanyID, PONo, POType)) {
                JOptionPane.showMessageDialog(null, "One amendment of this PO is already under process. Cannot make another amendment");
                return;
            }

            EditMode = EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SetupApproval();

            //Display newly generated document no.
            clsPOGen ObjPO = new clsPOGen();
            ObjPO.POType = POType;

            int MaxNo = clsPOAmendGen.getMaxAmendSrNo(EITLERPGLOBAL.gCompanyID, PONo, POType) + 1;

            clsPOGen ObjNewPO = (clsPOGen) ObjPO.getObject(EITLERPGLOBAL.gCompanyID, PONo, POType);

            DisplayPOData(ObjNewPO);

            txtDocNo.setText(PONo + "/" + MaxNo);
            txtDocDate.setText(EITLERPGLOBAL.getCurrentDate());

            txtDocDate.requestFocus();
            //

            lblTitle.setText(FormTitle + " - " + txtDocNo.getText());
            lblTitle.setBackground(Color.BLUE);
        } else {
            JOptionPane.showMessageDialog(null, "You must select P.O. number.");
        }

    }

    private void DisplayPOData(clsPOGen ObjPO) {
        try {
            ClearFields();

            txtPONo.setText((String) ObjPO.getAttribute("PO_NO").getObj());
            txtPODate.setText(EITLERPGLOBAL.formatDate((String) ObjPO.getAttribute("PO_DATE").getObj()));
            txtSuppCode.setText((String) ObjPO.getAttribute("SUPP_ID").getObj());

            if (POType == 6 || POType == 7 || POType == 2) {
                if (!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, (String) ObjPO.getAttribute("SUPP_ID").getObj())) {
                    txtSuppName.setText((String) ObjPO.getAttribute("SUPP_NAME").getObj());
                } else {
                    txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
                }
            } else {
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
            }

            //Instant Display
            if (!txtSuppCode.getText().trim().equals("000000")) {
                boolean HasRegNo = clsSupplier.IsRegistrationNoExist(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());
                lblURD.setVisible(!HasRegNo);
            }

            txtQuotationNo.setText((String) ObjPO.getAttribute("QUOTATION_NO").getObj());
            txtQuotationDate.setText(EITLERPGLOBAL.formatDate((String) ObjPO.getAttribute("QUOTATION_DATE").getObj()));
            txtInquiryNo.setText((String) ObjPO.getAttribute("INQUIRY_NO").getObj());
            txtInquiryDate.setText(EITLERPGLOBAL.formatDate((String) ObjPO.getAttribute("INQUIRY_DATE").getObj()));
            EITLERPGLOBAL.setComboIndex(cmbBuyer, (int) ObjPO.getAttribute("BUYER").getVal());
            EITLERPGLOBAL.setComboIndex(cmbTransportMode, (int) ObjPO.getAttribute("TRANSPORT_MODE").getVal());
            txtReference.setText((String) ObjPO.getAttribute("PO_REF").getObj());
            txtRefA.setText((String) ObjPO.getAttribute("REF_A").getObj());
            txtRefB.setText((String) ObjPO.getAttribute("REF_B").getObj());
            txtPurpose.setText((String) ObjPO.getAttribute("PURPOSE").getObj());
            txtSubject.setText((String) ObjPO.getAttribute("SUBJECT").getObj());
            EITLERPGLOBAL.setComboIndex(cmbCurrency, (int) ObjPO.getAttribute("CURRENCY_ID").getVal());
            txtCurrencyRate.setText(Double.toString(ObjPO.getAttribute("CURRENCY_RATE").getVal()));
            chkCancelled.setSelected(ObjPO.getAttribute("CANCELLED").getBool());
            chkAttachement.setSelected(ObjPO.getAttribute("ATTACHEMENT").getBool());
            txtFile.setText((String) ObjPO.getAttribute("ATTACHEMENT_PATH").getObj());
            EITLERPGLOBAL.setComboIndex(cmbShip, (int) ObjPO.getAttribute("SHIP_ID").getVal());

            txtRemarks.setText((String) ObjPO.getAttribute("REMARKS").getObj());

            txtLine1.setText((String) ObjPO.getAttribute("PRINT_LINE_1").getObj());
            txtLine2.setText((String) ObjPO.getAttribute("PRINT_LINE_2").getObj());
            txtImportLicense.setText((String) ObjPO.getAttribute("IMPORT_LICENSE").getObj());

            txtPaymentTerm.setText((String) ObjPO.getAttribute("PAYMENT_TERM").getObj());
            TermCode = ObjPO.getAttribute("PAYMENT_CODE").getInt();
            TermDays = ObjPO.getAttribute("CR_DAYS").getInt();

            txtPriceBasisTerm.setText((String) ObjPO.getAttribute("PRICE_BASIS_TERM").getObj());
            txtDiscountTerm.setText((String) ObjPO.getAttribute("DISCOUNT_TERM").getObj());
            txtExciseTerm.setText((String) ObjPO.getAttribute("EXCISE_TERM").getObj());
            txtSTTerm.setText((String) ObjPO.getAttribute("ST_TERM").getObj());
            txtPFTerm.setText((String) ObjPO.getAttribute("PF_TERM").getObj());
            txtFreightTerm.setText((String) ObjPO.getAttribute("FREIGHT_TERM").getObj());
            txtOctroiTerm.setText((String) ObjPO.getAttribute("OCTROI_TERM").getObj());
            txtFOBTerm.setText((String) ObjPO.getAttribute("FOB_TERM").getObj());
            txtCIETerm.setText((String) ObjPO.getAttribute("CIE_TERM").getObj());
            txtInsuranceTerm.setText((String) ObjPO.getAttribute("INSURANCE_TERM").getObj());
            txtTCCTerm.setText((String) ObjPO.getAttribute("TCC_TERM").getObj());
            txtCenvatTerm.setText((String) ObjPO.getAttribute("CENVAT_TERM").getObj());
            txtDespatchTerm.setText((String) ObjPO.getAttribute("DESPATCH_TERM").getObj());
            txtServiceTax.setText((String) ObjPO.getAttribute("SERVICE_TAX_TERM").getObj());
            txtOthersTerm.setText((String) ObjPO.getAttribute("OTHERS_TERM").getObj());
            txtFileText.setText((String) ObjPO.getAttribute("FILE_TEXT").getObj());

            txtCGSTTerm.setText((String) ObjPO.getAttribute("CGST_TERM").getObj());
            txtSGSTTerm.setText((String) ObjPO.getAttribute("SGST_TERM").getObj());
            txtIGSTTerm.setText((String) ObjPO.getAttribute("IGST_TERM").getObj());
            txtCompositionTerm.setText((String) ObjPO.getAttribute("COMPOSITION_TERM").getObj());
            txtRCMTerm.setText((String) ObjPO.getAttribute("RCM_TERM").getObj());
            txtGSTCompCessTerm.setText((String) ObjPO.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

            int SelShipID = EITLERPGLOBAL.getComboCode(cmbShip);
            clsShippingAddress ObjShip = (clsShippingAddress) clsShippingAddress.getObject(EITLERPGLOBAL.gCompanyID, SelShipID);
            txtAdd1.setText((String) ObjShip.getAttribute("ADD1").getObj());
            txtAdd2.setText((String) ObjShip.getAttribute("ADD2").getObj());
            txtAdd3.setText((String) ObjShip.getAttribute("ADD3").getObj());
            txtCity.setText((String) ObjShip.getAttribute("CITY").getObj());
            txtState.setText((String) ObjShip.getAttribute("STATE").getObj());

            //============= Display Custom Columns ========================
//            for(int i=1;i<=15;i++) {
            for (int i = 1; i <= 21; i++) {
                //int ColID=(int)ObjPO.getAttribute("COLUMN_"+Integer.toString(i)+"_ID").getVal();
                int ColID = getPOColID(i, "H");
                int Col = DataModelH.getColFromID(ColID);
                int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                String Variable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, ColID);

                if (ColID != 0) {
                    //Set the Formula
                    if (ObjPOAmend.getAttribute("COLUMN_" + Integer.toString(i) + "_FORMULA").getObj() != null) {
                        DataModelH.SetFormula(Col, (String) ObjPO.getAttribute("COLUMN_" + Integer.toString(i) + "_FORMULA").getObj());
                    } else {
                        DataModelH.SetFormula(Col, "");
                    }

                    //Set the Percentage. If there
                    if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                        DataModelH.setValueByVariableEx("P_" + ColID, Double.toString(ObjPO.getAttribute("COLUMN_" + Integer.toString(i) + "_PER").getVal()), 1);
                        //DataModelH.setValueByVariableEx("P_" + Variable, Double.toString(ObjPO.getAttribute("COLUMN_" + Integer.toString(i) + "_PER").getVal()), 1);
                    }

                    //Set the Value
                    DataModelH.setValueByVariableEx(Variable, Double.toString(ObjPO.getAttribute("COLUMN_" + Integer.toString(i) + "_AMT").getVal()), 1);
                }
            }
            //=================================================================//

            //========= Display Line Items =============//
            FormatGrid();

            DoNotEvaluate = true;

            for (int i = 1; i <= ObjPO.colPOItems.size(); i++) {
                //Insert New Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);
                int NewRow = TableL.getRowCount() - 1;

                clsPOItem ObjItem = (clsPOItem) ObjPO.colPOItems.get(Integer.toString(i));

                DataModelL.setValueByVariable("SR_NO", Integer.toString(i), NewRow);
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj(), NewRow);
                try{
                DataModelL.setValueByVariable("VENDOR_SHADE", (String) ObjItem.getAttribute("VENDOR_SHADE").getObj(), NewRow);
                }catch(Exception a){
                    DataModelL.setValueByVariable("VENDOR_SHADE", "", NewRow);
                }
                try{
                DataModelL.setValueByVariable("SDML_SHADE", (String) ObjItem.getAttribute("SDML_SHADE").getObj(), NewRow);
                }catch(Exception b){
                    DataModelL.setValueByVariable("SDML_SHADE", "", NewRow);
                }
                String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                DataModelL.setValueByVariable("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj(), NewRow);
                DataModelL.setValueByVariable("ITEM_DESC", (String) ObjItem.getAttribute("ITEM_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("MAKE", (String) ObjItem.getAttribute("MAKE").getObj(), NewRow);
                DataModelL.setValueByVariable("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("PART_NO", (String) ObjItem.getAttribute("PART_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("EXCISE_TARRIF_NO", (String) ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("TOLERANCE_LIMIT", Double.toString(ObjItem.getAttribute("TOLERANCE_LIMIT").getVal()), NewRow);
                DataModelL.setValueByVariable("RATE", Double.toString(ObjItem.getAttribute("RATE").getVal()), NewRow);
                DataModelL.setValueByVariable("UNIT_ID", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);
                String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());

                DataModelL.setValueByVariable("DEPT_ID", Integer.toString((int) ObjItem.getAttribute("DEPT_ID").getVal()), NewRow);
                DataModelL.setValueByVariable("DEPT_NAME", clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjItem.getAttribute("DEPT_ID").getVal()), NewRow);
                DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);
                DataModelL.setValueByVariable("DISC_PER", Double.toString(ObjItem.getAttribute("DISC_PER").getVal()), NewRow);
                DataModelL.setValueByVariable("DISC_AMT", Double.toString(ObjItem.getAttribute("DISC_AMT").getVal()), NewRow);
                DataModelL.setValueByVariable("GROSS_AMOUNT", Double.toString(ObjItem.getAttribute("TOTAL_AMOUNT").getVal()), NewRow);
                DataModelL.setValueByVariable("NET_AMOUNT", Double.toString(ObjItem.getAttribute("NET_AMOUNT").getVal()), NewRow);
                DataModelL.setValueByVariable("INDENT_NO", (String) ObjItem.getAttribute("INDENT_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("INDENT_SR_NO", Integer.toString((int) ObjItem.getAttribute("INDENT_SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("REFERENCE", (String) ObjItem.getAttribute("REFERENCE").getObj(), NewRow);
                DataModelL.setValueByVariable("DELIVERY_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DELIVERY_DATE").getObj()), NewRow);
                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), NewRow);

                //============= Display Custom Columns ========================
                //for(int c=1;c<=15;c++) {
                //for(int c=1;c<=16;c++) {                
                /*for (int c = 1; c <= 21; c++) {
                    int ColID = (int) ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_ID").getVal();
                    //int ColID=getPOColID(c, "L");
                    System.out.println("ColID " + ColID);
                    int Col = DataModelL.getColFromID(ColID);
                    System.out.println("Col " + Col);
                    int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                    System.out.println("TaxID " + TaxID);
                    String Variable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, ColID);
                    System.out.println("Variable " + Variable);

                    if (ColID != 0) {
                        //Set the Formula
                        if (ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_FORMULA").getObj() != null) {
                            DataModelL.SetFormula(Col, (String) ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_FORMULA").getObj());
                        } else {
                            DataModelL.SetFormula(Col, "");
                        }

                        //Set the Percentage. If there
                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                            System.out.println(Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_PER").getVal()));
                            System.out.println("P_" + ColID);

                            DataModelL.setValueByVariable("P_"+ColID, Double.toString(ObjItem.getAttribute("COLUMN_"+Integer.toString(c)+"_PER").getVal()),NewRow);                            
                            //DataModelL.setValueByVariable("P_" + Variable, Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_PER").getVal()), NewRow);
                        }

                        //Set the Value
                        System.out.println(Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_AMT").getVal()));
                        DataModelL.setValueByVariable(Variable, Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_AMT").getVal()), NewRow);

                    }
                }*/
                //MIR code start
                for (int c = 1; c <= 21; c++) {
                    //Get the Column ID
                    int lnColID = (int) ObjItem.getAttribute("COLUMN_" + c + "_ID").getVal();
                    String strVariable = "";
                    double lnPercentValue = 0, lnValue = 0;

                    //Record the values
                    lnPercentValue = ObjItem.getAttribute("COLUMN_" + c + "_PER").getVal();
                    lnValue = ObjItem.getAttribute("COLUMN_" + c + "_AMT").getVal();
                    //System.out.println("total value "+lnValue+" , PER : "+lnPercentValue+" , CAPTION : "+ObjItem.getAttribute("COLUMN_"+c+"_CAPTION").getObj());

                    if (lnColID > 0) //Column ID set .. Continue
                    {
                        //Get the Variable Name
                        strVariable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, lnColID);
                    }

                    //We have variable Name - Get the column no. of this form
                    int lnDestCol = DataModelL.getColFromVariable(strVariable);

                    if (lnDestCol >= 0) //We have found the column
                    {
                        //Replace the value of this form
                        DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);

                        //Now check that percentage is used
                        int lnTaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, lnColID);
                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, lnTaxID)) {
                            DataModelL.setValueAt(Double.toString(lnPercentValue), NewRow, lnDestCol - 1);
                            DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);
                        } else {
                            DataModelL.setValueAt(Double.toString(lnValue), NewRow, lnDestCol);
                        }
                    }

                }
                //MIR Code End
                //=================================================================//
            }

            //============= P.O. Terms ==============//
            //========= Display Line Items =============//
            FormatTermsGrid();
            for (int i = 1; i <= ObjPO.colPOTerms.size(); i++) {
                //Insert New Row
                Object[] rowData = new Object[3];
                clsPOTerms ObjItem = (clsPOTerms) ObjPO.colPOTerms.get(Integer.toString(i));
                String TermType = (String) ObjItem.getAttribute("TERM_TYPE").getObj();

                /*if(TermType.equals("P")) {
                 
                 rowData[0]=Integer.toString(TableP.getRowCount()+1);
                 rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                 rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                 DataModelP.addRow(rowData);
                 }
                 
                 if(TermType.equals("D")) {
                 
                 rowData[0]=Integer.toString(TableD.getRowCount()+1);
                 rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                 rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                 DataModelD.addRow(rowData);
                 }
                 
                 if(TermType.equals("O")) {
                 
                 rowData[0]=Integer.toString(TableO.getRowCount()+1);
                 rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
                 rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
                 DataModelO.addRow(rowData);
                 }*/
                if (TermType.equals("S")) {

                    rowData[0] = Integer.toString(TableS.getRowCount() + 1);
                    rowData[1] = Integer.toString((int) ObjItem.getAttribute("TERM_CODE").getVal());
                    rowData[2] = (String) ObjItem.getAttribute("TERM_DESC").getObj();
                    DataModelS.addRow(rowData);
                }

            }
            //=======================================//

            DoNotEvaluate = false;

            UpdateAmounts();
            UpdateSrNo();
            //=========================================//
        } catch (Exception e) {
            DoNotEvaluate = false;
        }

    }

    private void Edit() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String lDocNo = (String) ObjPOAmend.getAttribute("AMEND_NO").getObj();
        if (ObjPOAmend.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            EITLERPGLOBAL.ChangeCursorToWait(this);
            EditMode = EITLERPGLOBAL.EDIT;
            GenerateCombos();
            //(POType+27)

            int ModuleID = (POType + 27);
            //start add on 110909
            if (POType == 9) {
                ModuleID = 168;
            }
            //end add on 110909

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//
            //start add on 110909
            int pFinctionId = ((POType + 40) * 10) + 2;
            if (POType == 9) {
                pFinctionId = 11682;
            }
            //end add on 110909
            if (ApprovalFlow.IsCreator(ModuleID, lDocNo) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, pFinctionId)) {
                SetFields(true);
            } else {
                EnableApproval();
            }

            DisableToolbar();
            OpgHold.setSelected(true);
            txtDocDate.requestFocus();
            EITLERPGLOBAL.ChangeCursorToDefault(this);
        } else {
            JOptionPane.showMessageDialog(null, "You cannot edit this record. It is either approved/rejected or waiting approval for other user");
        }
    }

    private void Delete() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String lDocNo = (String) ObjPOAmend.getAttribute("AMEND_NO").getObj();

        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record ?", "SDML ERP", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (ObjPOAmend.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
                if (ObjPOAmend.Delete(EITLERPGLOBAL.gUserID)) {
                    MoveLast();
                } else {
                    JOptionPane.showMessageDialog(null, "Error occured while deleting. Error is " + ObjPOAmend.LastError);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
            }
        }
    }

    private void Save() {
        //Form level validations
        if (Validate() == false) {
            return; //Validation failed
        }

        EITLERPGLOBAL.ChangeCursorToWait(this);
        SetData();

        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjPOAmend.Insert()) {
                MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjPOAmend.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjPOAmend.Update()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjPOAmend.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
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
        ShowMessage("Ready");
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void Cancel() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.Purchase.frmPOAmendFind", true);
        frmPOAmendFind ObjReturn = (frmPOAmendFind) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            //Add PO Type
            if (!ObjReturn.strQuery.trim().equals("")) {
                ObjReturn.strQuery = ObjReturn.strQuery + " AND PO_TYPE=" + POType;
            } else {
                ObjReturn.strQuery = " WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PO_TYPE=" + POType + " AND AMEND_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND AMEND_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ";
            }

            if (!ObjPOAmend.Filter(ObjReturn.strQuery, EITLERPGLOBAL.gCompanyID)) {
                JOptionPane.showMessageDialog(null, "No records found.");
            }
            MoveLast();
        }
    }

    private void MoveFirst() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjPOAmend.MoveFirst();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void MovePrevious() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjPOAmend.MovePrevious();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void MoveNext() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjPOAmend.MoveNext();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void MoveLast() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjPOAmend.MoveLast();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    //Recurses through the hierarchy of classes
    //until it finds Frame
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while (c != null) {
            if (c instanceof Frame) {
                return (Frame) c;
            }

            c = c.getParent();
        }
        return (Frame) null;
    }

    public void FindEx(int pCompanyID, String pDocNo) {
        ObjPOAmend.POType = POType;
        ObjPOAmend.Filter(" WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND AMEND_NO='" + pDocNo + "' AND PO_TYPE=" + POType, pCompanyID);
        ObjPOAmend.MoveLast();
        DisplayData();
    }

    public void FindWaiting() {
        ObjPOAmend.POType = POType;
        int ModuleID = (POType + 27);
        //start add on 110909
        if (POType == 9) {
            ModuleID = 168;
        }
        //end add on 110909

        ObjPOAmend.Filter(" WHERE AMEND_NO IN(SELECT D_PUR_AMEND_HEADER.AMEND_NO FROM D_PUR_AMEND_HEADER,D_COM_DOC_DATA WHERE D_PUR_AMEND_HEADER.AMEND_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_AMEND_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_AMEND_HEADER.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND D_COM_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_AMEND_HEADER.PO_TYPE=" + POType + " AND MODULE_ID=" + ModuleID + ")", EITLERPGLOBAL.gCompanyID);
        ObjPOAmend.MoveLast();
        DisplayData();
    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
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

    private void UpdateResults(int pCol) {
        if (!DoNotEvaluate) {
            try {
                int ColID = 0, TaxID = 0, UpdateCol = 0;
                String strFormula = "", strItemID = "", strVariable = "", srcVariable = "", srcVar2 = "";
                double lnPercentValue = 0, lnFinalResult = 0, lnNetAmount = 0;
                Object result;
                boolean updateIt = true;
                int QtyCol = 0, RateCol = 0, GAmountCol = 0;

                Updating = true; //Stops Recursion

                srcVariable = DataModelL.getVariable(pCol); //Variable name of currently updated Column

                //If this column is percentage column. Variable name would be P_XXX
                //We shoule use actual variable name, it will be found on it's associated next column
                if (srcVariable.substring(0, 2).equals("P_")) {
                    srcVariable = DataModelL.getVariable(pCol + 1);
                }

                QtyCol = DataModelL.getColFromVariable("QTY"); //Index of Qty Column
                RateCol = DataModelL.getColFromVariable("RATE"); //Index of Rate Column
                GAmountCol = DataModelL.getColFromVariable("GROSS_AMOUNT"); //Index of Gross Amount Column

                //======= Read the Item ID - To be used when accessing item specific formula ===//
                String cellValue = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                if (cellValue == null) {
                    strItemID = "";
                } else {
                    strItemID = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                }
                //================================================================================

                GatherVariableValues();

                //====== Update Gross Amount =======
                myParser.parseExpression("QTY*RATE");
                result = myParser.getValueAsObject();
                if (result != null) {
                    String RoundNum = Double.toString(EITLERPGLOBAL.round(Double.parseDouble(result.toString()), 5));
                    DataModelL.setValueByVariable("GROSS_AMOUNT", RoundNum, TableL.getSelectedRow());
                }
                //=================================

                for (int i = 0; i < TableL.getColumnCount(); i++) {
                    strVariable = DataModelL.getVariable(i);

                    ColID = DataModelL.getColID(i);

                    TaxID = ObjColumn.getTaxID((int) EITLERPGLOBAL.gCompanyID, ColID);

                    //Exclude Percentage Columns and System Columns
                    if ((!strVariable.substring(0, 2).equals("P_")) && (ColID != 0) && (ColID != -99)) {
                        //If percentage is used
                        if (ObjTax.getUsePercentage((int) EITLERPGLOBAL.gCompanyID, TaxID)) {

                            //Load the Formula for calculation
                            if ((!EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                                strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID, strItemID);
                            } else {
                                strFormula = DataModelL.getFormula(i);
                            }

                            //Now Read Associated Percentage Column
                            lnPercentValue = Double.parseDouble(DataModelL.getValueByVariable("P_" + Integer.toString(ColID), TableL.getSelectedRow()));

                            //Now Parse Main expression
                            myParser.parseExpression(strFormula);
                            result = myParser.getValueAsObject();
                            if (result != null) {
                                //Now get the percentage of the main result
                                lnFinalResult = (Double.parseDouble(result.toString()) * lnPercentValue) / 100;
                                //Update the Column
                                srcVar2 = DataModelL.getVariable(pCol + 1);

                                UpdateCol = DataModelL.getColFromVariable(strVariable);

                                updateIt = false;

                                if (UpdateCol != pCol) {
                                    if (UpdateCol == pCol + 1) {
                                        updateIt = true;
                                    } else {
                                        if ((strFormula.indexOf(srcVariable) != -1)) { //If this column is dependent on updated column
                                            updateIt = true; //Then update it
                                        } else {

                                            //Check whether the formula is dependent on any system Columns
                                            boolean Dependent = false;
                                            int dCol = 0;

                                            for (int d = 0; d <= TableL.getColumnCount() - 1; d++) {
                                                if (DataModelL.getColID(d) == 0) //It's System Column
                                                {
                                                    String dVariable = DataModelL.getVariable(d);
                                                    if (strFormula.indexOf(dVariable) != -1) {
                                                        if (pCol == d) {
                                                            Dependent = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }

                                    //============ New Change In Parser =============//
                                    //Now Condition. First check whether percentage has been entered
                                    if (lnPercentValue > 0) {
                                        //Yes Percentage Entered. Then we must update the associated column
                                        updateIt = true;
                                    } else {
                                        //If not Percentage entered than check whether any value is there
                                        //Otherwise go with the Dependent decision
                                    }
                                    //=================================================//

                                }

                                if (updateIt) {
                                    DataModelL.setValueByVariable(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 5)), TableL.getSelectedRow());
                                }
                                //Re Gather Fresh Variable Values
                                GatherVariableValues();
                            }
                        } else //Percentage Not Used
                        {
                            //Load the Formula for calculation
                            if ((!EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                                strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID, strItemID);
                            } else {
                                strFormula = DataModelL.getFormula(i);
                            }

                            //Now Parse Main expression
                            myParser.parseExpression(strFormula);
                            result = myParser.getValueAsObject();
                            if (result != null) {
                                //Now get the percentage of the main result
                                lnFinalResult = Double.parseDouble(result.toString());
                                //Update the Column
                                UpdateCol = DataModelL.getColFromVariable(strVariable);

                                updateIt = false;

                                if (UpdateCol != pCol) {
                                    if (strFormula.indexOf(srcVariable) != -1) {
                                        updateIt = true;
                                    } else {

                                        //Check whether the formula is dependent on any system Columns
                                        boolean Dependent = false;
                                        int dCol = 0;

                                        for (int d = 0; d <= TableL.getColumnCount() - 1; d++) {
                                            if (DataModelL.getColID(d) == 0) //It's System Column
                                            {
                                                String dVariable = DataModelL.getVariable(d);

                                                if (strFormula.indexOf(dVariable) != -1) {
                                                    if (pCol == d) {
                                                        Dependent = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        if (Dependent) {
                                            updateIt = true;
                                        }

                                    }

                                    //============ New Change In Parser =============//
                                    //Now Condition. First check whether percentage has been entered
                                    if (lnPercentValue > 0) {
                                        //Yes Percentage Entered. Then we must update the associated column
                                        updateIt = true;
                                    } else {
                                        //If not Percentage entered than check whether any value is there
                                        //Otherwise go with the Dependent decision
                                    }
                                    //=================================================//

                                }

                                if (updateIt) {
                                    DataModelL.setValueByVariable(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 5)), TableL.getSelectedRow());
                                }
                                //Re Gather Fresh Variable Values
                                GatherVariableValues();
                            }
                        }
                    }
                }

                //== Final Pass - Update the Net Amount ==
                lnNetAmount = 0;
                double lnColValue = 0;
                double lnGrossAmount = 0;

                lnGrossAmount = Double.parseDouble((String) DataModelL.getValueAt(TableL.getSelectedRow(), GAmountCol));

                for (int c = 0; c < TableL.getColumnCount(); c++) {

                    //To be included in Calculation or not
                    if (DataModelL.getInclude(c) == false) {
                        //Read column value
                        if (TableL.getValueAt(TableL.getSelectedRow(), c).toString().equals("")) {
                            lnColValue = 0;
                        } else {
                            lnColValue = Double.parseDouble((String) TableL.getValueAt(TableL.getSelectedRow(), c));
                        }

                        if (DataModelL.getOperation(c).equals("+")) //Add
                        {
                            lnGrossAmount = lnGrossAmount + lnColValue;
                        } else //Substract
                        {
                            lnGrossAmount = lnGrossAmount - lnColValue;
                        }
                    }
                }

                //Now update the Net Amount
                DataModelL.setValueByVariable("NET_AMOUNT", Double.toString(EITLERPGLOBAL.round(lnGrossAmount, 5)), TableL.getSelectedRow());

                Updating = false;

                //=======================================================//
                //======= New Change. Reverse Calculation ===============//
                //Calculate Percentage based on Amount
                ColID = DataModelL.getColID(pCol);
                int AsColID = DataModelL.getColID(pCol - 1);

                if (ColID != 0 && ColID != -99 && ColID == AsColID) {
                    TaxID = ObjColumn.getTaxID((int) EITLERPGLOBAL.gCompanyID, ColID);
                    //Read the formula
                    strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID, strItemID);
                    double EnteredValue = Double.parseDouble((String) DataModelL.getValueAt(TableL.getSelectedRow(), pCol));

                    //Now Parse Main expression
                    myParser.parseExpression(strFormula);
                    result = myParser.getValueAsObject();
                    if (result != null) {
                        //x=(Gross Amount*Percent)/100

                        //Reverse
                        // x*100/Gross amount=Percent
                        double percentValue = 0;
                        double val = 0;

                        val = Double.parseDouble(result.toString());

                        if (val != 0) {
                            percentValue = EITLERPGLOBAL.round((EnteredValue * 100) / val, 5);
                            DoNotEvaluate = true;
                            TableL.setValueAt(Double.toString(percentValue), TableL.getSelectedRow(), pCol - 1);
                            DoNotEvaluate = false;
                        }
                    }
                }
                //======================================================//
                //============= End of Reverse Procedure ===============//

                UpdateAmounts();
            } catch (Exception e) {
                Updating = false;
            }
        }// Do not Evaluate
    }

    private void GatherVariableValues() {
        //Scan the table and gather values for variables
        colVariables.clear();

        myParser.initSymTab(); // clear the contents of the symbol table
        myParser.addStandardConstants();
        myParser.addComplex(); // among other things adds i to the symbol table

        for (int i = 0; i < TableL.getColumnCount(); i++) {
            double lValue = 0;
            if (DataModelL.getVariable(i) != null) {
                //if((!DataModelL.getVariable(i).trim().equals(""))&&(DataModelL.getColID(i)!=0))    //If Variable not blank
                if ((!DataModelL.getVariable(i).trim().equals(""))) {
                    //colVariables.put(DataModelL.getVariable(i),(String)DataModelL.getValueAt(TableL.getSelectedRow(), i));

                    //Add variable Value to Parser Table
                    if ((TableL.getValueAt(TableL.getSelectedRow(), i) != null) && (!TableL.getValueAt(TableL.getSelectedRow(), i).toString().equals(""))) {
                        if (TableL.getValueAt(TableL.getSelectedRow(), i) instanceof Boolean) {
                            if (DataModelL.getBoolValueByVariable(DataModelL.getVariable(i), TableL.getSelectedRow())) {
                                lValue = 1;
                            } else {
                                lValue = 0;
                            }
                        } else {
                            if (EITLERPGLOBAL.IsNumber((String) TableL.getValueAt(TableL.getSelectedRow(), i))) {
                                lValue = Double.parseDouble((String) TableL.getValueAt(TableL.getSelectedRow(), i));
                            }
                        }
                    } else {
                        lValue = 0;
                    }
                    myParser.addVariable(DataModelL.getVariable(i), lValue);
                }
            }
        }

        myParser.addFunction("IIF", new IIF(myParser));
    }

    private void FormatTermsGrid() {

        //--- PO Terms Formatting -----//
        /*DataModelD=new EITLTableModel();
         
         TableD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableD.removeAll();
         TableD.setModel(DataModelD);
         
         DataModelD.addColumn("Sr.");
         DataModelD.addColumn("Code");
         DataModelD.addColumn("Description");
         
         DataModelD.SetNumeric(0, true);
         DataModelD.SetNumeric(1, true);
         
         //----- Install Table Model Event Listener -------//
         TableD.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableD.getValueAt(TableD.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"DELIVERY_CODE",theCode);
         TableD.setValueAt(Desc, TableD.getSelectedRow(), 2);
         }
         }
         }
         });
         //=============================================//
         
         
         
         //--- PO Terms Formatting -----//
         DataModelP=new EITLTableModel();
         
         TableP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableP.removeAll();
         TableP.setModel(DataModelP);
         
         DataModelP.addColumn("Sr.");
         DataModelP.addColumn("Code");
         DataModelP.addColumn("Description");
         
         DataModelP.SetNumeric(0, true);
         DataModelP.SetNumeric(1, true);
         
         DataModelP.TableReadOnly(true);
         
         //----- Install Table Model Event Listener -------//
         TableP.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableP.getValueAt(TableP.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"PAYMENT_CODE",theCode);
         TableP.setValueAt(Desc, TableP.getSelectedRow(), 2);
         }
         }
         }
         });*/
        //=============================================//
        //--- PO Terms Formatting -----//
        /*DataModelO=new EITLTableModel();
         
         TableO.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableO.removeAll();
         TableO.setModel(DataModelO);
         
         DataModelO.addColumn("Sr.");
         DataModelO.addColumn("Code");
         DataModelO.addColumn("Description");
         
         DataModelO.SetNumeric(0, true);
         DataModelO.SetNumeric(1, true);
         
         //----- Install Table Model Event Listener -------//
         TableO.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableO.getValueAt(TableO.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"OTHER_CODE",theCode);
         TableO.setValueAt(Desc, TableO.getSelectedRow(), 2);
         }
         }
         }
         });*/
        //=============================================//
        //--- PO Terms Formatting -----//
        DataModelS = new EITLTableModel();

        TableS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableS.removeAll();
        TableS.setModel(DataModelS);

        DataModelS.addColumn("Sr.");
        DataModelS.addColumn("Code");
        DataModelS.addColumn("Description");

        DataModelS.SetNumeric(0, true);
        DataModelS.SetNumeric(1, true);

        //----- Install Table Model Event Listener -------//
        TableS.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int col = e.getColumn();

                    if (col == 1) {
                        int theCode = Integer.parseInt((String) TableS.getValueAt(TableS.getSelectedRow(), 1));

                        String Desc = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "OTHER_CODE", theCode);
                        TableS.setValueAt(Desc, TableS.getSelectedRow(), 2);
                    }
                }
            }
        });
        //=============================================//

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

        for (int i = 0; i < Tab2.getComponentCount() - 1; i++) {
            if (Tab2.getComponent(i).getName() != null) {

                FieldName = Tab2.getComponent(i).getName();
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {

                    Tab2.getComponent(i).setEnabled(true);
                }

            }
        }
        //=============== Header Fields Setup Complete =================//

        //=============== Setting Table Fields ==================//
        DataModelL.ClearAllReadOnly();
        for (int i = 0; i < TableL.getColumnCount(); i++) {
            FieldName = DataModelL.getVariable(i);

            if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
                //Do Nothing
            } else {
                DataModelL.SetReadOnly(i);
            }
        }

        //Disabling Grids
        if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", "DELIVERY_TERMS")) {
            DataModelD.TableReadOnly(false);
        } else {
            DataModelD.TableReadOnly(true);
        }

        if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", "OTHER_TERMS")) {
            DataModelO.TableReadOnly(false);
        } else {
            DataModelO.TableReadOnly(true);
        }

        //=======================================================//
    }

    private void FormatPayTermsGrid() {
        //--- PO Terms Formatting -----//
        /*DataModelP=new EITLTableModel();
         
         TableP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableP.removeAll();
         TableP.setModel(DataModelP);
         
         DataModelP.addColumn("Sr.");
         DataModelP.addColumn("Code");
         DataModelP.addColumn("Description");
         
         DataModelP.SetNumeric(0, true);
         DataModelP.SetNumeric(1, true);
         
         DataModelP.TableReadOnly(true);
         
         //----- Install Table Model Event Listener -------//
         TableP.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableP.getValueAt(TableP.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"PAYMENT_CODE",theCode);
         TableP.setValueAt(Desc, TableP.getSelectedRow(), 2);
         }
         }
         }
         });*/
        //=============================================//
    }

    private void FormatDeliveryTermsGrid() {
        //--- PO Terms Formatting -----//
        /*DataModelD=new EITLTableModel();
         
         TableD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableD.removeAll();
         TableD.setModel(DataModelD);
         
         DataModelD.addColumn("Sr.");
         DataModelD.addColumn("Code");
         DataModelD.addColumn("Description");
         
         DataModelD.SetNumeric(0, true);
         DataModelD.SetNumeric(1, true);
         
         //----- Install Table Model Event Listener -------//
         TableD.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableD.getValueAt(TableD.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"DELIVERY_CODE",theCode);
         TableD.setValueAt(Desc, TableD.getSelectedRow(), 2);
         }
         }
         }
         });*/
        //=============================================//

    }

    private void FormatOtherTermsGrid() {
        //--- PO Terms Formatting -----//
        /*DataModelO=new EITLTableModel();
         
         TableO.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableO.removeAll();
         TableO.setModel(DataModelO);
         
         DataModelO.addColumn("Sr.");
         DataModelO.addColumn("Code");
         DataModelO.addColumn("Description");
         
         DataModelO.SetNumeric(0, true);
         DataModelO.SetNumeric(1, true);
         
         //----- Install Table Model Event Listener -------//
         TableO.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableO.getValueAt(TableO.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"OTHER_CODE",theCode);
         TableO.setValueAt(Desc, TableO.getSelectedRow(), 2);
         }
         }
         }
         });*/
        //=============================================//

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

    private void PreviewReport() {
        HashMap Params = new HashMap();

        if (chkCancelled.isSelected()) {
            JOptionPane.showMessageDialog(null, "You cannot take printout of cancelled document");
            return;
        }

        int FinalApprover = 0;
        String strFinalApprover = "";
        int HierarchyID = (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();

        FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
        strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

        PrepareAmendment(txtDocNo.getText(), POType);

        //if(POType==2&&(EITLERPGLOBAL.gCompanyID==2)) //Baroda COMMENTED BY NISARG ON 17/09/2008
        if (POType == 2) //Baroda
        {
            if (HierarchyID == 627 || HierarchyID == 628 || HierarchyID == 629) {

                try {
                    //URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOGen2.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
                    URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendWINDENT.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
                }
            } else {
                try {
                    //URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAmend.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
                    URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendWINDENT.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
                }

            }
        }

        if (POType == 4) //Raw Material
        {

            if (HierarchyID == 247 || HierarchyID == 248) {
                strFinalApprover = "A. B. Tewary";
            }

            if (HierarchyID == 536 || HierarchyID == 558) {
                strFinalApprover = "Vinod Patel";
            }

            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendRaw.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }

        }

        if (POType == 5) //Import Spares
        {

            if (HierarchyID == 199 || HierarchyID == 201) {
                strFinalApprover = "A. B. Tewary";
            }

            if (HierarchyID == 553 || HierarchyID == 554 || HierarchyID == 493) {
                strFinalApprover = "Vinod Patel";
            }

            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendImport.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }

        }

        if (POType == 6) {
            try {
                System.out.println("REPORT URL=" + "http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }
        }

        if (POType != 4 && POType != 5 && POType != 6 && POType != 2) //Others
        {
            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmend.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }
        }
    }

    private void PreviewReport_GST() {
        HashMap Params = new HashMap();

        if (chkCancelled.isSelected()) {
            JOptionPane.showMessageDialog(null, "You cannot take printout of cancelled document");
            return;
        }

        int FinalApprover = 0;
        String strFinalApprover = "";
        int HierarchyID = (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();

        FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
        strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

        PrepareAmendment(txtDocNo.getText(), POType);

        //if(POType==2&&(EITLERPGLOBAL.gCompanyID==2)) //Baroda COMMENTED BY NISARG ON 17/09/2008
        if (POType == 2) //Baroda
        {
            if (HierarchyID == 627 || HierarchyID == 628 || HierarchyID == 629) {

                try {
                    //URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOGen2.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
                    URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendWINDENT_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
                }
            } else {
                try {
                    //URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAmend.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
                    URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendWINDENT_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
                }

            }
        }

        if (POType == 4) //Raw Material
        {

            if (HierarchyID == 247 || HierarchyID == 248) {
                strFinalApprover = "A. B. Tewary";
            }

            if (HierarchyID == 536 || HierarchyID == 558) {
                strFinalApprover = "Vinod Patel";
            }

            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendRaw_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }

        }

        if (POType == 5) //Import Spares
        {

            if (HierarchyID == 199 || HierarchyID == 201) {
                strFinalApprover = "A. B. Tewary";
            }

            if (HierarchyID == 553 || HierarchyID == 554 || HierarchyID == 493) {
                strFinalApprover = "Vinod Patel";
            }

            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendImport_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }

        }

        if (POType == 6) {
            try {
                System.out.println("REPORT URL=" + "http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }
        }
        if (POType == 9) //MERCHANDISE
        {
            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmend_GSTM.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }
        }
        if (POType != 4 && POType != 5 && POType != 6 && POType != 2 && POType != 9) //Others
        {
            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmend_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }
        }
    }

    private void PreviewReport_TRN() {
        HashMap Params = new HashMap();

        if (chkCancelled.isSelected()) {
            JOptionPane.showMessageDialog(null, "You cannot take printout of cancelled document");
            return;
        }

        int FinalApprover = 0;
        String strFinalApprover = "";
        int HierarchyID = (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();

        FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
        strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

        PrepareAmendment(txtDocNo.getText(), POType);

        //if(POType==2&&(EITLERPGLOBAL.gCompanyID==2)) //Baroda COMMENTED BY NISARG ON 17/09/2008
        if (POType == 2) //Baroda
        {
            if (HierarchyID == 627 || HierarchyID == 628 || HierarchyID == 629) {

                try {
                    //URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOGen2.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
                    URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendWINDENT_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
                }
            } else {
                try {
                    //URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPOAmend.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDocNo.getText());
                    URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendWINDENT_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
                }

            }
        }

        if (POType == 4) //Raw Material
        {

            if (HierarchyID == 247 || HierarchyID == 248) {
                strFinalApprover = "A. B. Tewary";
            }

            if (HierarchyID == 536 || HierarchyID == 558) {
                strFinalApprover = "Vinod Patel";
            }

            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendRaw_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }

        }

        if (POType == 5) //Import Spares
        {

            if (HierarchyID == 199 || HierarchyID == 201) {
                strFinalApprover = "A. B. Tewary";
            }

            if (HierarchyID == 553 || HierarchyID == 554 || HierarchyID == 493) {
                strFinalApprover = "Vinod Patel";
            }

            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendImport_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover);
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }

        }

        if (POType == 6) {
            try {
                System.out.println("REPORT URL=" + "http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }
        }

        if (POType != 4 && POType != 5 && POType != 6 && POType != 2) //Others
        {
            try {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmend_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText());
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
            }
        }
    }

    private void PreviewAuditReport() {
        try {
            if (POType == 1 || POType == 9) //General Amendment & Merchandise Amendment
            {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOGenAmendA.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&DocType=1");
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            }

            if (POType == 3) //A Class Amendment
            {
                URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAClassAmendA.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&DocType=3");
                EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Previwing " + e.getMessage());
        }
    }

    private void ShowFileText() {
        String FileText = "";

        try {
            String theFile = txtFile.getText();

            BufferedReader aFile = new BufferedReader(new FileReader(new File(theFile)));
            boolean Done = false;
            while (!Done) {
                String lineread = aFile.readLine();

                if (lineread == null) {
                    Done = true;
                } else {
                    FileText = FileText + lineread + "\n";
                }
            }
            aFile.close();
        } catch (Exception e) {
        }

        if (chkDoNotErase.isSelected()) {
            txtFileText.setText(txtFileText.getText() + " " + FileText);
        } else {
            txtFileText.setText(FileText);
        }

    }

    private void GenerateRejectedUserCombo() {
        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();

        //----- Generate cmbType ------- //
        cmbToModel = new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbToModel);

        int ModuleID = (POType + 27);
        //start add on 110909
        if (POType == 9) {
            ModuleID = 168;
        }
        //end add on 110909

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

                if ((int) ObjUser.getAttribute("USER_ID").getVal() == 10) {
                    int a = 0;
                }

                if (OpgApprove.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, ModuleID, txtDocNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, ModuleID, txtDocNo.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (ObjUser.getAttribute("USER_ID").getVal() == 11) {
                    IncludeUser = true;
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
            int Creator = ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, ModuleID, txtDocNo.getText());
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

    }

    public void ControlDisplay() {
        //As per the PO Type, Display appropriate titles
        switch (POType) {
            case 1:
                FormTitle = "PURCHASE ORDER (General)";
                break;
            case 2:
                FormTitle = "PURCHASE ORDER (Without Indent)";
                break;
            case 3:
                FormTitle = "PURCHASE ORDER (A Class)";
                break;
            case 4:
                FormTitle = "PURCHASE ORDER (Raw Material)";
                break;
            case 5:
                FormTitle = "PURCHASE ORDER (Spares)";
                break;
            case 6:
                FormTitle = "PURCHASE ORDER (Capital Goods)";
                break;
            case 7:
                FormTitle = "PURCHASE ORDER (Contract)";
                break;
            case 9:
                FormTitle = "PURCHASE ORDER (Merchandise)";
                break;
        }

        lblTitle.setText(FormTitle);

        //Import Spares PO. Show the Import License No.
        if (POType == 5) {
            lblImport.setVisible(true);
            txtImportLicense.setVisible(true);
            cmdImportBig.setVisible(true);
        } else {
            lblImport.setVisible(false);
            txtImportLicense.setVisible(false);
            cmdImportBig.setVisible(false);
        }

        //Specifications - Show the Specification Grid in case of A Class P.O.
        if (POType == 3 || POType == 4) {
            lblSpecs.setVisible(true);
            frameSpecs.setVisible(true);
            cmdSpecAdd.setVisible(true);
            cmdSpecRemove.setVisible(true);
        } else {
            lblSpecs.setVisible(false);
            frameSpecs.setVisible(false);
            cmdSpecAdd.setVisible(false);
            cmdSpecRemove.setVisible(false);
        }

        if (POType == 4 || POType == 7) {
            chkImported.setVisible(true);
            txtImportLicense.setVisible(true);
            cmdImportBig.setVisible(true);
        } else {
            chkImported.setVisible(false);
            txtImportLicense.setVisible(false);
            cmdImportBig.setVisible(false);
        }

    }

    private boolean IsDirectorApprovalRequired() {
        boolean GrantFinal = false;
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CreditTermValidation = false;

        try {
            tmpConn = data.getConn();

            double POValue = Double.parseDouble(txtNetAmount.getText());

            //--- Special Addition : -----------------//
            //To Grant Final Approval Rights to Audit Department.
            //In Special Cases
            //Supplier and Payment Terms Based Decision
            //(1) ===== Gen. P.O. having credit payment value less then Rs. 10000 ==========
            //Check whether Credit Payment code is there in PO Terms
            //Retrive All Credit Payment Codes from the table
            boolean CreditTermFound = false;

            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='C'");
            rsTmp.first();
            while (!rsTmp.isAfterLast()) {
                int TermCode = rsTmp.getInt("PAYMENT_CODE");

                clsSupplier tmpSupp = new clsSupplier();
                clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                //Find out in Payment Terms of PO
                for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                    clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                    String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                    if (TermType.equals("P")) {
                        int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                        if (POTermCode == TermCode) {
                            CreditTermFound = true;
                        }
                    }
                }

                rsTmp.next();
            }
            //As per requirement by Audit dept on dt 23/01/2015 start
            //if(CreditTermFound && POValue<10000) {
            //    GrantFinal=true;
            //}             
            if (CreditTermFound && POValue <= 25000) {
                GrantFinal = true;
                CreditTermValidation = true;
            }
            //As per requirement by Audit dept on dt 23/01/2015 end
            //================================================================//

            //(2) Gen. P.O. Order having Advance/Payment or Against Delivery Payment
            //    and Value less than 2500.
            boolean AdvanceTermFound = false;
            boolean ApprovedRatesFound = false;
            boolean RIACreated = false;

            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='A' OR PAYMENT_TYPE='D'");
            rsTmp.first();
            while (!rsTmp.isAfterLast()) {
                int TermCode = rsTmp.getInt("PAYMENT_CODE");

                clsSupplier tmpSupp = new clsSupplier();
                clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                //Find out in Payment Terms of PO
                for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                    clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                    String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                    if (TermType.equals("P")) {
                        int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                        if (POTermCode == TermCode) {
                            AdvanceTermFound = true;
                        }
                    }
                }
                rsTmp.next();
            }

            //Check for two conditions again for each item in the PO
            //(1) If Rate Approval Found then continue operation
            //    If Rate Approval not found then check for the PO made in last 6 months. If PO
            //    found then continue operation
            for (int r = 0; r < TableL.getRowCount(); r++) {
                String ItemID = DataModelL.getValueByVariable("ITEM_ID", r);
                String IndentNo = DataModelL.getValueByVariable("INDENT_NO", r);
                int IndentSrNo = Integer.parseInt(DataModelL.getValueByVariable("INDENT_SR_NO", r));

                if ((!IndentNo.trim().equals("")) && IndentSrNo > 0) //Skip in case of blank
                {
                    int RIAStatus = clsIndent.IsRIACreated(EITLERPGLOBAL.gCompanyID, IndentNo, IndentSrNo);

                    if (RIAStatus == 2) {

                    }
                }
            }

            //As per requirement by Audit dept on dt 23/01/2015 start
            //if(AdvanceTermFound && POValue<2500) {
            //    GrantFinal=true;
            //}            
            if (AdvanceTermFound && POValue <= 10000) {
                GrantFinal = true;
            }
            //As per requirement by Audit dept on dt 23/01/2015 end
            //==================================================================//

            //(3) Gen. P.O. having advance/against delivery payment and
            // Value less than 10000 for listed parties only
            //As per requirement by Audit dept on dt 23/01/2015 start
            /*
             AdvanceTermFound=false;
            
             stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
             rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PAYMENT_TYPE='A' OR PAYMENT_TYPE='D'");
             rsTmp.first();
             while(!rsTmp.isAfterLast()) {
             int TermCode=rsTmp.getInt("PAYMENT_CODE");
                
             clsSupplier tmpSupp=new clsSupplier();
             clsSupplier ObjSupp= (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());
                
             //Find out in Payment Terms of PO
             for(int i=1;i<=ObjSupp.colSuppTerms.size();i++) {
             clsSuppTerms ObjTerm=(clsSuppTerms)ObjSupp.colSuppTerms.get(Integer.toString(i));
             String TermType=(String)ObjTerm.getAttribute("TERM_TYPE").getObj();
                    
             if(TermType.equals("P")) {
             int POTermCode=(int)ObjTerm.getAttribute("TERM_CODE").getVal();
             if (POTermCode==TermCode) {
             AdvanceTermFound=true;
             }
             }
             }
             rsTmp.next();
             }
            
             if(AdvanceTermFound && POValue<10000) {
             String SuppID=txtSuppCode.getText().trim();
                
             //Check the party
             stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
             rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_SUPPLIERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID='"+SuppID+"'");
             rsTmp.first();
             if(rsTmp.getRow()>0) {
             //Party is listed party
             GrantFinal=true;
             }
             }
             */
            //As per requirement by Audit dept on dt 23/01/2015 end
            //==============================================================//
            //(4) P.O. for Printed stationary having value less than 10000
            boolean ItemFound = false;
            boolean NotAllItems = false;

            for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                clsPOAmendGen ObjItem = (clsPOAmendGen) ObjPOAmend.colPOItems.get(Integer.toString(i));
                String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                if (EITLERPGLOBAL.gCompanyID == 2)//BARODA UNIT
                {
                    if (ItemID.substring(0, 2).equals("98") || ItemID.substring(0, 2).equals("97") || ItemID.substring(0, 2).equals("96")) {
                        ItemFound = true;
                    } else {
                        NotAllItems = true;
                    }
                }

                if (EITLERPGLOBAL.gCompanyID == 3)//ANKLESHWAR UNIT
                {
                    if (ItemID.substring(0, 2).equals("90")) {
                        ItemFound = true;
                    } else {
                        NotAllItems = true;
                    }
                }

            }
            //As per requirement by Audit dept on dt 23/01/2015 start
            //if(ItemFound && (!NotAllItems) && POValue<10000) {
            //    GrantFinal=true;
            //}            
            if (ItemFound && (!NotAllItems) && POValue <= 10000) {
                GrantFinal = true;
            }
            if (ItemFound && (!NotAllItems) && POValue > 10000 && CreditTermValidation) {
                GrantFinal = false;
            }
            //As per requirement by Audit dept on dt 23/01/2015 end
            //==============================================================//

            //(5) Cash Purchase upto 2500 for electronics,oil seals,ice,gas cylinders
            boolean CashTermFound = false;

            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='H'");
            rsTmp.first();
            while (!rsTmp.isAfterLast()) {
                int TermCode = rsTmp.getInt("PAYMENT_CODE");

                clsSupplier tmpSupp = new clsSupplier();
                clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                //Find out in Payment Terms of PO
                for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                    clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                    String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                    if (TermType.equals("P")) {
                        int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                        if (POTermCode == TermCode) {
                            CashTermFound = true;
                        }
                    }
                }
                rsTmp.next();
            }

            // NOTE :
            /*
             OIL SEALS - ITEM CODE STARTING WITH 60208
             ICE - FOR BARODA - 96020206
             */
            ItemFound = false;
            NotAllItems = false;

            for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                if (ItemID.substring(0, 5).equals("60208") || ItemID.substring(0, 4).equals("7030") || ItemID.equals("60703001") || ItemID.equals("60703002") || ItemID.equals("60703004") || ItemID.equals("60703005") || ItemID.equals("96020206")) {
                    ItemFound = true;
                } else {
                    NotAllItems = true;
                }
            }

            //if(CashTermFound && ItemFound && (!NotAllItems) && POValue<2500) {
            //    GrantFinal=true;
            //}
            //Modified Term : Modified as per Audit Instructions
            //As per requirement by Audit dept on dt 23/01/2015 start
            //if(CashTermFound && POValue<10000) {
            //    GrantFinal=true;
            //}            
            if (CashTermFound && ItemFound && (!NotAllItems) && POValue <= 5000) {
                GrantFinal = true;
            }
            //As per requirement by Audit dept on dt 23/01/2015 end
            //==============================================================//

            //(7) Purchase other than cash upto 10000 for electronics,oil seals,ice,gas cylinders
            boolean OtherTermFound = false;

            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='P' OR PAYMENT_TYPE='A' OR PAYMENT_TYPE='D' OR PAYMENT_TYPE='C'");
            rsTmp.first();
            while (!rsTmp.isAfterLast()) {
                int TermCode = rsTmp.getInt("PAYMENT_CODE");

                clsSupplier tmpSupp = new clsSupplier();
                clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                //Find out in Payment Terms of PO
                for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                    clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                    String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                    if (TermType.equals("P")) {
                        int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                        if (POTermCode == TermCode) {
                            OtherTermFound = true;
                        }
                    }
                }
                rsTmp.next();
            }
            // NOTE :              
            /* OIL SEALS - ITEM CODE STARTING WITH 60208 ICE - FOR BARODA - 96020206 */

            ItemFound = false;
            NotAllItems = false;

            for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                if (ItemID.substring(0, 5).equals("60208") || ItemID.substring(0, 4).equals("7030") || ItemID.equals("60703001") || ItemID.equals("60703002") || ItemID.equals("60703004") || ItemID.equals("60703005") || ItemID.equals("96020206")) {
                    ItemFound = true;
                } else {
                    NotAllItems = true;
                }
            }

            if (OtherTermFound && ItemFound && (!NotAllItems) && POValue <= 10000) {
                GrantFinal = true;
            }
            if (OtherTermFound && ItemFound && (!NotAllItems) && POValue > 10000 && CreditTermValidation) {
                GrantFinal = false;
            }

            //==============================================================//
            //(6) Cash Purchase upto 5000 for kerosene
            CashTermFound = false;

            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_COM_APPROVAL_PAYMENT_CODES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PAYMENT_TYPE='H'");
            rsTmp.first();
            while (!rsTmp.isAfterLast()) {
                int TermCode = rsTmp.getInt("PAYMENT_CODE");

                clsSupplier tmpSupp = new clsSupplier();
                clsSupplier ObjSupp = (clsSupplier) tmpSupp.getObject(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

                //Find out in Payment Terms of PO
                for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                    clsSuppTerms ObjTerm = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                    String TermType = (String) ObjTerm.getAttribute("TERM_TYPE").getObj();

                    if (TermType.equals("P")) {
                        int POTermCode = (int) ObjTerm.getAttribute("TERM_CODE").getVal();
                        if (POTermCode == TermCode) {
                            CashTermFound = true;
                        }
                    }
                }
                rsTmp.next();
            }

            ItemFound = false;
            NotAllItems = false;

            for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                clsPOAmendItem ObjItem = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                String ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();

                if (EITLERPGLOBAL.gCompanyID == 2 || EITLERPGLOBAL.gCompanyID == 1)//BARODA UNIT
                {
                    if (ItemID.equals("60305002")) {
                        ItemFound = true;
                    } else {
                        NotAllItems = true;
                    }
                }

                if (EITLERPGLOBAL.gCompanyID == 3)//ANKLESHWAR UNIT
                {
                    if (ItemID.equals("40303002")) {
                        ItemFound = true;
                    } else {
                        NotAllItems = true;
                    }
                }

            }
            //As per requirement by Audit dept on dt 23/01/2015 start
            //if(CashTermFound && ItemFound && (!NotAllItems)) {
            //    GrantFinal=true;
            //}            
            if (CashTermFound && ItemFound && (!NotAllItems) && POValue <= 20000) {
                GrantFinal = true;
            }
            //As per requirement by Audit dept on dt 23/01/2015 end
            //==============================================================//

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
        } catch (Exception e) {

        }

        return !GrantFinal;

    }

    private void ShowChangedItems() {
        boolean DiffFound = false;

        try {
            clsPOGen tmpObj = new clsPOGen();
            tmpObj.LoadData(EITLERPGLOBAL.gCompanyID, POType);
            clsPOGen ObjPO = (clsPOGen) tmpObj.getObject(EITLERPGLOBAL.gCompanyID, txtPONo.getText());

            ColorRenderer = new EITLTableCellRenderer();
            ColorRenderer.removeBackColors();
            ColorRenderer.removeForeColors();

            //********* DISPLAY COLORS FOR HEADER FIELDS *********//
            if (!ObjPO.getAttribute("QUOTATION_NO").getString().trim().equals(ObjPOAmend.getAttribute("QUOTATION_NO").getString().trim())) {
                txtQuotationNo.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("INQUIRY_NO").getString().trim().equals(ObjPOAmend.getAttribute("INQUIRY_NO").getString().trim())) {
                txtInquiryNo.setBackground(Color.YELLOW);
            }

            if (ObjPO.getAttribute("TRANSPORT_MODE").getInt() != ObjPOAmend.getAttribute("TRANSPORT_MODE").getInt()) {
                cmbTransportMode.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("PURPOSE").getString().trim().equals(ObjPOAmend.getAttribute("PURPOSE").getString().trim())) {
                txtPurpose.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("SUBJECT").getString().trim().equals(ObjPOAmend.getAttribute("SUBJECT").getString().trim())) {
                txtSubject.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("PRINT_LINE_1").getString().trim().equals(ObjPOAmend.getAttribute("PRINT_LINE_1").getString().trim())) {
                txtLine1.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("PRINT_LINE_2").getString().trim().equals(ObjPOAmend.getAttribute("PRINT_LINE_2").getString().trim())) {
                txtLine2.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("REMARKS").getString().trim().equals(ObjPOAmend.getAttribute("REMARKS").getString().trim())) {
                txtRemarks.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("PAYMENT_TERM").getString().trim().equals(ObjPOAmend.getAttribute("PAYMENT_TERM").getString().trim())) {
                txtPaymentTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("PRICE_BASIS_TERM").getString().trim().equals(ObjPOAmend.getAttribute("PRICE_BASIS_TERM").getString().trim())) {
                txtPriceBasisTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("DISCOUNT_TERM").getString().trim().equals(ObjPOAmend.getAttribute("DISCOUNT_TERM").getString().trim())) {
                txtDiscountTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("EXCISE_TERM").getString().trim().equals(ObjPOAmend.getAttribute("EXCISE_TERM").getString().trim())) {
                txtExciseTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("ST_TERM").getString().trim().equals(ObjPOAmend.getAttribute("ST_TERM").getString().trim())) {
                txtSTTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("PF_TERM").getString().trim().equals(ObjPOAmend.getAttribute("PF_TERM").getString().trim())) {
                txtPFTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("FREIGHT_TERM").getString().trim().equals(ObjPOAmend.getAttribute("FREIGHT_TERM").getString().trim())) {
                txtFreightTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("OCTROI_TERM").getString().trim().equals(ObjPOAmend.getAttribute("OCTROI_TERM").getString().trim())) {
                txtOctroiTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("SERVICE_TAX_TERM").getString().trim().equals(ObjPOAmend.getAttribute("SERIVCE_TAX_TERM").getString().trim())) {
                txtServiceTax.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("FOB_TERM").getString().trim().equals(ObjPOAmend.getAttribute("FOB_TERM").getString().trim())) {
                txtFOBTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("CIE_TERM").getString().trim().equals(ObjPOAmend.getAttribute("CIE_TERM").getString().trim())) {
                txtCIETerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("INSURANCE_TERM").getString().trim().equals(ObjPOAmend.getAttribute("INSURANCE_TERM").getString().trim())) {
                txtInsuranceTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("TCC_TERM").getString().trim().equals(ObjPOAmend.getAttribute("TCC_TERM").getString().trim())) {
                txtTCCTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("CENVAT_TERM").getString().trim().equals(ObjPOAmend.getAttribute("CENVAT_TERM").getString().trim())) {
                txtCenvatTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("OTHERS_TERM").getString().trim().equals(ObjPOAmend.getAttribute("OTHERS_TERM").getString().trim())) {
                txtOthersTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("DESPATCH_TERM").getString().trim().equals(ObjPOAmend.getAttribute("DESPATCH_TERM").getString().trim())) {
                txtDespatchTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("CGST_TERM").getString().trim().equals(ObjPOAmend.getAttribute("CGST_TERM").getString().trim())) {
                txtCGSTTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("SGST_TERM").getString().trim().equals(ObjPOAmend.getAttribute("SGST_TERM").getString().trim())) {
                txtSGSTTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("IGST_TERM").getString().trim().equals(ObjPOAmend.getAttribute("IGST_TERM").getString().trim())) {
                txtIGSTTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("COMPOSITION_TERM").getString().trim().equals(ObjPOAmend.getAttribute("COMPOSITION_TERM").getString().trim())) {
                txtCompositionTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("RCM_TERM").getString().trim().equals(ObjPOAmend.getAttribute("RCM_TERM").getString().trim())) {
                txtRCMTerm.setBackground(Color.YELLOW);
            }

            if (!ObjPO.getAttribute("GST_COMPENSATION_CESS_TERM").getString().trim().equals(ObjPOAmend.getAttribute("GST_COMPENSATION_CESS_TERM").getString().trim())) {
                txtGSTCompCessTerm.setBackground(Color.YELLOW);
            }
            //****************************************************//

            for (int i = 0; i < TableL.getColumnCount(); i++) {
                if ((i == DataModelL.getColFromVariable("EXCISE_GATEPASS_GIVEN")) || (i == DataModelL.getColFromVariable("IMPORT_CONCESS"))) {

                } else {

                    TableL.getColumnModel().getColumn(i).setCellRenderer(ColorRenderer);
                }
            }

            for (int i = 1; i <= ObjPOAmend.colPOItems.size(); i++) {
                DiffFound = false;
                clsPOAmendItem ObjAmend = (clsPOAmendItem) ObjPOAmend.colPOItems.get(Integer.toString(i));
                clsPOItem ObjPOItem = (clsPOItem) ObjPO.colPOItems.get(Integer.toString(i));

                //Start Comparing
                if (ObjAmend.getAttribute("ITEM_DESC").getString().equals(ObjPOItem.getAttribute("ITEM_DESC").getString())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("ITEM_DESC"), Color.YELLOW);
                }

                //Start Comparing
                if (((String) ObjAmend.getAttribute("MAKE").getObj()).equals((String) ObjPOItem.getAttribute("MAKE").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("MAKE"), Color.YELLOW);
                }

                //Start Comparing
                if (((String) ObjAmend.getAttribute("PRICE_LIST_NO").getObj()).equals((String) ObjPOItem.getAttribute("PRICE_LIST_NO").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("PRICE_LIST_NO"), Color.YELLOW);
                }

                if (((String) ObjAmend.getAttribute("PART_NO").getObj()).equals((String) ObjPOItem.getAttribute("PART_NO").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("PART_NO"), Color.YELLOW);
                }

                if (((String) ObjAmend.getAttribute("EXCISE_TARRIF_NO").getObj()).equals((String) ObjPOItem.getAttribute("EXCISE_TARRIF_NO").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("EXCISE_TARRIF_NO"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("QTY").getVal()) == (ObjPOItem.getAttribute("QTY").getVal())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("QTY"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("TOLERANCE_LIMIT").getVal()) == (ObjPOItem.getAttribute("TOLERANCE_LIMIT").getVal())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("TOLERANCE_LIMIT"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("RATE").getVal()) == (ObjPOItem.getAttribute("RATE").getVal())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("RATE"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("UNIT").getVal()) == (ObjPOItem.getAttribute("UNIT").getVal())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("UNIT"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("TOTAL_AMOUNT").getVal()) == (ObjPOItem.getAttribute("TOTAL_AMOUNT").getVal())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("GROSS_AMOUNT"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("NET_AMOUNT").getVal()) == (ObjPOItem.getAttribute("NET_AMOUNT").getVal())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("NET_AMOUNT"), Color.YELLOW);
                }

                if (((String) ObjAmend.getAttribute("INDENT_NO").getObj()).equals((String) ObjPOItem.getAttribute("INDENT_NO").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("INDENT_NO"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("INDENT_SR_NO").getVal()) == (ObjPOItem.getAttribute("INDENT_SR_NO").getVal())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("INDENT_SR_NO"), Color.YELLOW);
                }

                if (((String) ObjAmend.getAttribute("REFERENCE").getObj()).equals((String) ObjPOItem.getAttribute("REFERENCE").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("REFERENCE"), Color.YELLOW);
                }

                if (((String) ObjAmend.getAttribute("DELIVERY_DATE").getObj()).equals((String) ObjPOItem.getAttribute("DELIVERY_DATE").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("DELIVERY_DATE"), Color.YELLOW);
                }

                if (((String) ObjAmend.getAttribute("REMARKS").getObj()).equals((String) ObjPOItem.getAttribute("REMARKS").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("REMARKS"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("EXCISE_GATEPASS_GIVEN").getBool()) == (ObjPOItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("EXCISE_GATEPASS_GIVEN"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("IMPORT_CONCESS").getBool()) == (ObjPOItem.getAttribute("IMPORT_CONCESS").getBool())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("IMPORT_CONCESS"), Color.YELLOW);
                }

                if (((String) ObjAmend.getAttribute("QUOT_ID").getObj()).equals((String) ObjPOItem.getAttribute("QUOT_ID").getObj())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("QUOT_NO"), Color.YELLOW);
                }

                if ((ObjAmend.getAttribute("QUOT_SR_NO").getVal()) == (ObjPOItem.getAttribute("QUOT_SR_NO").getVal())) {

                } else {
                    DiffFound = true;
                    ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("QUOT_SR_NO"), Color.YELLOW);
                }

//                for(int c=1;c<=10;c++) {
                for (int c = 1; c <= 16; c++) {
                    if ((ObjAmend.getAttribute("COLUMN_" + c + "_PER").getVal()) == (ObjPOItem.getAttribute("COLUMN_" + c + "_PER").getVal())) {

                    } else {
                        DiffFound = true;
                        ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("COLUMN_" + c + "_PER"), Color.YELLOW);
                    }

                    if ((ObjAmend.getAttribute("COLUMN_" + c + "_AMT").getVal()) == (ObjPOItem.getAttribute("COLUMN_" + c + "_AMT").getVal())) {

                    } else {
                        DiffFound = true;
                        ColorRenderer.setBackColor(i - 1, DataModelL.getColFromVariable("COLUMN_" + c + "_AMT"), Color.YELLOW);
                    }

                }

                if (DiffFound) {
                    for (int c = 0; c < TableL.getColumnCount(); c++) {
                        if ((c == DataModelL.getColFromVariable("EXCISE_GATEPASS_GIVEN")) || (c == DataModelL.getColFromVariable("IMPORT_CONCESS"))) {

                        } else {
                            //ColorRenderer.setBackColor(i-1, c, Color.YELLOW);
                        }
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    public int getPOColID(int pSrNo, String pHeaderLine) {
        HashMap List = new HashMap();
        int ModuleID = (POType + 27);
        //start add on 110909
        if (POType == 9) {
            ModuleID = 168;
        }
        //end add on 110909
        List = clsColumn.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND MODULE_ID=" + ModuleID + " AND HEADER_LINE='" + pHeaderLine + "' ORDER BY COL_ORDER");
        TableColumnModel ColModel = TableL.getColumnModel();

        int theColID = 0;

        for (int i = 1; i <= List.size(); i++) {
            if (i == pSrNo) {
                clsColumn ObjColumn = (clsColumn) List.get(Integer.toString(i));
                int lTaxID = (int) ObjColumn.getAttribute("TAX_ID").getVal();
                theColID = (int) ObjColumn.getAttribute("SR_NO").getVal();
            }
        }

        return theColID;
    }

//    private void PrepareAmendment(String pAmendNo,int pPOType) {
//        
//        Connection tmpConn;
//        Statement stAmendHeader,stAmendDetail,stTmp;
//        ResultSet rsAmendHeader,rsAmendDetail,rsTmp,rsItem;
//        String PONo="";
//        
//        try {
//            tmpConn=data.getConn();
//            stAmendHeader=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            rsAmendHeader=stAmendHeader.executeQuery("SELECT * FROM D_TMP_AMEND_HEADER LIMIT 1");
//            
//            stAmendDetail=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            rsAmendDetail=stAmendDetail.executeQuery("SELECT * FROM D_TMP_AMEND_DETAIL LIMIT 1");
//            
//            
//            data.Execute("DELETE FROM D_TMP_AMEND_HEADER WHERE AMEND_NO='"+pAmendNo+"' AND PO_TYPE="+pPOType);
//            data.Execute("DELETE FROM D_TMP_AMEND_DETAIL WHERE AMEND_NO='"+pAmendNo+"' AND PO_TYPE="+pPOType);
//            
//            //==========Header Part ===============//
//            rsAmendHeader.moveToInsertRow();
//            rsAmendHeader.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
//            rsAmendHeader.updateString("AMEND_NO",pAmendNo);
//            rsAmendHeader.updateInt("PO_TYPE",pPOType);
//            
//            //==== Read data from Amendment ======//
//            rsTmp=data.getResult("SELECT * FROM D_PUR_AMEND_HEADER WHERE AMEND_NO='"+pAmendNo+"' AND PO_TYPE="+pPOType);
//            rsTmp.first();
//            
//            if(rsTmp.getRow()>0) {
//                PONo=rsTmp.getString("PO_NO");
//                
//                //Update Amend Header
//                rsAmendHeader.updateString("PO_NO",rsTmp.getString("PO_NO"));
//                rsAmendHeader.updateString("PO_DATE",rsTmp.getString("PO_DATE"));
//                rsAmendHeader.updateString("AMEND_DATE",rsTmp.getString("AMEND_DATE"));
//                rsAmendHeader.updateString("SUPP_ID",rsTmp.getString("SUPP_ID"));
//                rsAmendHeader.updateString("SUPP_NAME",rsTmp.getString("SUPP_NAME"));
//                rsAmendHeader.updateString("REMARKS",rsTmp.getString("REMARKS"));
//                rsAmendHeader.updateString("PRINT_LINE_1",rsTmp.getString("PRINT_LINE_1"));
//                rsAmendHeader.updateString("PRINT_LINE_2",rsTmp.getString("PRINT_LINE_2"));
//                rsAmendHeader.updateString("AMEND_REASON",rsTmp.getString("AMEND_REASON"));
//                rsAmendHeader.updateString("A_PAYMENT_TERM",rsTmp.getString("PAYMENT_TERM"));
//                rsAmendHeader.updateString("A_PRICE_BASIS_TERM",rsTmp.getString("PRICE_BASIS_TERM"));
//                rsAmendHeader.updateString("A_DISCOUNT_TERM",rsTmp.getString("DISCOUNT_TERM"));
//                rsAmendHeader.updateString("A_EXCISE_TERM",rsTmp.getString("EXCISE_TERM"));
//                rsAmendHeader.updateString("A_ST_TERM",rsTmp.getString("ST_TERM"));
//                rsAmendHeader.updateString("A_PF_TERM",rsTmp.getString("PF_TERM"));
//                rsAmendHeader.updateString("A_FREIGHT_TERM",rsTmp.getString("FREIGHT_TERM"));
//                rsAmendHeader.updateString("A_OCTROI_TERM",rsTmp.getString("OCTROI_TERM"));
//                rsAmendHeader.updateString("A_CENVAT_TERM",rsTmp.getString("CENVAT_TERM"));
//                rsAmendHeader.updateString("A_SERVICE_TAX_TERM",rsTmp.getString("SERVICE_TAX_TERM"));
//                rsAmendHeader.updateString("A_OTHER_TERM",rsTmp.getString("OTHERS_TERM"));
//                rsAmendHeader.updateString("A_FOB_TERM",rsTmp.getString("FOB_TERM"));
//                
//                rsAmendHeader.updateString("A_CGST_TERM",rsTmp.getString("CGST_TERM"));
//                rsAmendHeader.updateString("A_SGST_TERM",rsTmp.getString("SGST_TERM"));
//                rsAmendHeader.updateString("A_IGST_TERM",rsTmp.getString("IGST_TERM"));
//                rsAmendHeader.updateString("A_COMPOSITION_TERM",rsTmp.getString("COMPOSITION_TERM"));
//                rsAmendHeader.updateString("A_RCM_TERM",rsTmp.getString("RCM_TERM"));
//                rsAmendHeader.updateString("A_GST_COMPENSATION_CESS_TERM",rsTmp.getString("GST_COMPENSATION_CESS_TERM"));
//                
//                rsAmendHeader.updateBoolean("APPROVED",rsTmp.getBoolean("APPROVED"));
//            }
//            
//            
//            rsTmp=data.getResult("SELECT * FROM D_PUR_PO_HEADER WHERE PO_NO='"+PONo+"' AND PO_TYPE="+pPOType);
//            rsTmp.first();
//            
//            if(rsTmp.getRow()>0) {
//                
//                rsAmendHeader.updateString("PAYMENT_TERM",rsTmp.getString("PAYMENT_TERM"));
//                rsAmendHeader.updateString("PRICE_BASIS_TERM",rsTmp.getString("PRICE_BASIS_TERM"));
//                rsAmendHeader.updateString("DISCOUNT_TERM",rsTmp.getString("DISCOUNT_TERM"));
//                rsAmendHeader.updateString("EXCISE_TERM",rsTmp.getString("EXCISE_TERM"));
//                rsAmendHeader.updateString("ST_TERM",rsTmp.getString("ST_TERM"));
//                rsAmendHeader.updateString("PF_TERM",rsTmp.getString("PF_TERM"));
//                rsAmendHeader.updateString("FREIGHT_TERM",rsTmp.getString("FREIGHT_TERM"));
//                rsAmendHeader.updateString("OCTROI_TERM",rsTmp.getString("OCTROI_TERM"));
//                rsAmendHeader.updateString("CENVAT_TERM",rsTmp.getString("CENVAT_TERM"));
//                rsAmendHeader.updateString("SERVICE_TAX_TERM",rsTmp.getString("SERVICE_TAX_TERM"));
//                rsAmendHeader.updateString("OTHER_TERM",rsTmp.getString("OTHERS_TERM"));
//                rsAmendHeader.updateString("FOB_TERM",rsTmp.getString("FOB_TERM"));
//                
//                rsAmendHeader.updateString("CGST_TERM",rsTmp.getString("CGST_TERM"));
//                rsAmendHeader.updateString("SGST_TERM",rsTmp.getString("SGST_TERM"));
//                rsAmendHeader.updateString("IGST_TERM",rsTmp.getString("IGST_TERM"));
//                rsAmendHeader.updateString("COMPOSITION_TERM",rsTmp.getString("COMPOSITION_TERM"));
//                rsAmendHeader.updateString("RCM_TERM",rsTmp.getString("RCM_TERM"));
//                rsAmendHeader.updateString("GST_COMPENSATION_CESS_TERM",rsTmp.getString("GST_COMPENSATION_CESS_TERM"));
//            }
//            
//            rsAmendHeader.insertRow();
//            
//            
//            rsItem=data.getResult("SELECT * FROM D_PUR_AMEND_DETAIL WHERE AMEND_NO='"+pAmendNo+"' AND PO_TYPE="+pPOType);
//            rsItem.first();
//            
//            while(!rsItem.isAfterLast()) {
//                int SrNo=rsItem.getInt("SR_NO");
//                
//                //Update Line
//                rsAmendDetail.moveToInsertRow();
//                rsAmendDetail.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
//                rsAmendDetail.updateString("AMEND_NO",pAmendNo);
//                rsAmendDetail.updateInt("PO_TYPE",pPOType);
//                rsAmendDetail.updateInt("SR_NO",rsItem.getInt("SR_NO"));
//                
//                rsAmendDetail.updateString("A_ITEM_ID",rsItem.getString("ITEM_ID"));
//                rsAmendDetail.updateString("A_ITEM_EXTRA_DESC",rsItem.getString("ITEM_DESC"));
//                rsAmendDetail.updateDouble("A_QTY",rsItem.getDouble("QTY"));
//                rsAmendDetail.updateDouble("A_RATE",rsItem.getDouble("RATE"));
////                rsAmendDetail.updateDouble("A_DISC_PER",rsItem.getDouble("COLUMN_1_PER"));
////                rsAmendDetail.updateDouble("A_DISC_AMT",rsItem.getDouble("COLUMN_1_AMT"));
////                rsAmendDetail.updateDouble("A_EXCISE",rsItem.getDouble("COLUMN_3_AMT"));
////                rsAmendDetail.updateDouble("A_ST",rsItem.getDouble("COLUMN_4_AMT"));
////                rsAmendDetail.updateDouble("A_CENVAT",rsItem.getDouble("COLUMN_7_AMT"));
////                rsAmendDetail.updateDouble("A_SERVICE_TAX",rsItem.getDouble("COLUMN_8_AMT"));
////                rsAmendDetail.updateDouble("A_FOB_AMT",rsItem.getDouble("COLUMN_11_AMT"));
//                rsAmendDetail.updateDouble("A_DISC_PER",rsItem.getDouble("COLUMN_1_PER"));
//                rsAmendDetail.updateDouble("A_DISC_AMT",rsItem.getDouble("COLUMN_1_AMT"));
//                rsAmendDetail.updateDouble("A_EXCISE",rsItem.getDouble("COLUMN_9_AMT"));
//                rsAmendDetail.updateDouble("A_ST",rsItem.getDouble("COLUMN_10_AMT"));
//                rsAmendDetail.updateDouble("A_CENVAT",rsItem.getDouble("COLUMN_13_AMT"));
//                rsAmendDetail.updateDouble("A_SERVICE_TAX",rsItem.getDouble("COLUMN_14_AMT"));
//                rsAmendDetail.updateDouble("A_FOB_AMT",rsItem.getDouble("COLUMN_17_AMT"));
//                rsAmendDetail.updateString("A_DELIVERY_DATE",rsItem.getString("DELIVERY_DATE"));
//                
//                rsAmendDetail.updateDouble("A_CGST",rsItem.getDouble("COLUMN_3_AMT"));
//                rsAmendDetail.updateDouble("A_SGST",rsItem.getDouble("COLUMN_4_AMT"));
//                rsAmendDetail.updateDouble("A_IGST",rsItem.getDouble("COLUMN_5_AMT"));
//                rsAmendDetail.updateDouble("A_COMPOSITION",rsItem.getDouble("COLUMN_6_AMT"));
//                rsAmendDetail.updateDouble("A_RCM",rsItem.getDouble("COLUMN_7_AMT"));
//                rsAmendDetail.updateDouble("A_GST_COMPENSATION_CESS",rsItem.getDouble("COLUMN_8_AMT"));
//                
//                //Fill Blank
//                rsAmendDetail.updateString("ITEM_ID",rsItem.getString("ITEM_ID"));
//                rsAmendDetail.updateString("ITEM_EXTRA_DESC","");
//                rsAmendDetail.updateDouble("QTY",0);
//                rsAmendDetail.updateDouble("RATE",0);
//                rsAmendDetail.updateDouble("DISC_PER",0);
//                rsAmendDetail.updateDouble("DISC_AMT",0);
//                rsAmendDetail.updateDouble("EXCISE",0);
//                rsAmendDetail.updateDouble("ST",0);
//                rsAmendDetail.updateDouble("CENVAT",0);
//                rsAmendDetail.updateDouble("SERVICE_TAX",0);
//                rsAmendDetail.updateDouble("FOB_AMT",0);
//                rsAmendDetail.updateString("DELIVERY_DATE","0000-00-00");
//                
//                rsAmendDetail.updateDouble("CGST",0);
//                rsAmendDetail.updateDouble("SGST",0);
//                rsAmendDetail.updateDouble("IGST",0);
//                rsAmendDetail.updateDouble("COMPOSITION",0);
//                rsAmendDetail.updateDouble("RCM",0);
//                rsAmendDetail.updateDouble("GST_COMPENSATION_CESS",0);
//                
//                
//                //Read PO Data
//                rsTmp=data.getResult("SELECT * FROM D_PUR_PO_DETAIL WHERE PO_NO='"+PONo+"' AND PO_TYPE="+pPOType+" AND SR_NO="+SrNo);
//                rsTmp.first();
//                if(rsTmp.getRow()>0) {
//                    rsAmendDetail.updateString("ITEM_ID",rsTmp.getString("ITEM_ID"));
//                    rsAmendDetail.updateString("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_DESC"));
//                    rsAmendDetail.updateDouble("QTY",rsTmp.getDouble("QTY"));
//                    rsAmendDetail.updateDouble("RATE",rsTmp.getDouble("RATE"));
////                    rsAmendDetail.updateDouble("DISC_PER",rsTmp.getDouble("COLUMN_1_PER"));
////                    rsAmendDetail.updateDouble("DISC_AMT",rsTmp.getDouble("COLUMN_1_AMT"));
////                    rsAmendDetail.updateDouble("EXCISE",rsTmp.getDouble("COLUMN_3_AMT"));
////                    rsAmendDetail.updateDouble("ST",rsTmp.getDouble("COLUMN_4_AMT"));
////                    rsAmendDetail.updateDouble("CENVAT",rsTmp.getDouble("COLUMN_7_AMT"));
////                    rsAmendDetail.updateDouble("SERVICE_TAX",rsTmp.getDouble("COLUMN_8_AMT"));
////                    rsAmendDetail.updateDouble("FOB_AMT",rsTmp.getDouble("COLUMN_11_AMT"));
//                    rsAmendDetail.updateDouble("DISC_PER",rsTmp.getDouble("COLUMN_1_PER"));
//                    rsAmendDetail.updateDouble("DISC_AMT",rsTmp.getDouble("COLUMN_1_AMT"));
//                    rsAmendDetail.updateDouble("EXCISE",rsTmp.getDouble("COLUMN_9_AMT"));
//                    rsAmendDetail.updateDouble("ST",rsTmp.getDouble("COLUMN_10_AMT"));
//                    rsAmendDetail.updateDouble("CENVAT",rsTmp.getDouble("COLUMN_13_AMT"));
//                    rsAmendDetail.updateDouble("SERVICE_TAX",rsTmp.getDouble("COLUMN_14_AMT"));
//                    rsAmendDetail.updateDouble("FOB_AMT",rsTmp.getDouble("COLUMN_17_AMT"));
//                    rsAmendDetail.updateString("DELIVERY_DATE",rsTmp.getString("DELIVERY_DATE"));
//                    
//                    rsAmendDetail.updateDouble("CGST",rsTmp.getDouble("COLUMN_3_AMT"));
//                    rsAmendDetail.updateDouble("SGST",rsTmp.getDouble("COLUMN_4_AMT"));
//                    rsAmendDetail.updateDouble("IGST",rsTmp.getDouble("COLUMN_5_AMT"));
//                    rsAmendDetail.updateDouble("COMPOSITION",rsTmp.getDouble("COLUMN_6_AMT"));
//                    rsAmendDetail.updateDouble("RCM",rsTmp.getDouble("COLUMN_7_AMT"));
//                    rsAmendDetail.updateDouble("GST_COMPENSATION_CESS",rsTmp.getDouble("COLUMN_8_AMT"));
//                    
//                }
//                
//                rsAmendDetail.insertRow();
//                
//                
//                rsItem.next();
//            }
//            
//        }
//        catch(Exception e) {
//            
//        }
//        
//        
//    }
    private void PrepareAmendment(String pAmendNo, int pPOType) {

        Connection tmpConn;
        Statement stAmendHeader, stAmendDetail, stTmp;
        ResultSet rsAmendHeader, rsAmendDetail, rsTmp, rsItem;
        String PONo = "";

        try {
            tmpConn = data.getConn();
            stAmendHeader = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsAmendHeader = stAmendHeader.executeQuery("SELECT * FROM D_TMP_AMEND_HEADER LIMIT 1");

            stAmendDetail = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsAmendDetail = stAmendDetail.executeQuery("SELECT * FROM D_TMP_AMEND_DETAIL LIMIT 1");

            data.Execute("DELETE FROM D_TMP_AMEND_HEADER WHERE AMEND_NO='" + pAmendNo + "' AND PO_TYPE=" + pPOType);
            data.Execute("DELETE FROM D_TMP_AMEND_DETAIL WHERE AMEND_NO='" + pAmendNo + "' AND PO_TYPE=" + pPOType);

            //==========Header Part ===============//
            rsAmendHeader.moveToInsertRow();
            rsAmendHeader.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsAmendHeader.updateString("AMEND_NO", pAmendNo);
            rsAmendHeader.updateInt("PO_TYPE", pPOType);

            //==== Read data from Amendment ======//
            rsTmp = data.getResult("SELECT * FROM D_PUR_AMEND_HEADER WHERE AMEND_NO='" + pAmendNo + "' AND PO_TYPE=" + pPOType);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PONo = rsTmp.getString("PO_NO");

                //Update Amend Header
                rsAmendHeader.updateString("PO_NO", rsTmp.getString("PO_NO"));
                rsAmendHeader.updateString("PO_DATE", rsTmp.getString("PO_DATE"));
                rsAmendHeader.updateString("AMEND_DATE", rsTmp.getString("AMEND_DATE"));
                rsAmendHeader.updateString("SUPP_ID", rsTmp.getString("SUPP_ID"));
                rsAmendHeader.updateString("SUPP_NAME", rsTmp.getString("SUPP_NAME"));
                rsAmendHeader.updateString("REMARKS", rsTmp.getString("REMARKS"));
                rsAmendHeader.updateString("PRINT_LINE_1", rsTmp.getString("PRINT_LINE_1"));
                rsAmendHeader.updateString("PRINT_LINE_2", rsTmp.getString("PRINT_LINE_2"));
                rsAmendHeader.updateString("AMEND_REASON", rsTmp.getString("AMEND_REASON"));
                rsAmendHeader.updateString("A_PAYMENT_TERM", rsTmp.getString("PAYMENT_TERM"));
                rsAmendHeader.updateString("A_PRICE_BASIS_TERM", rsTmp.getString("PRICE_BASIS_TERM"));
                rsAmendHeader.updateString("A_DISCOUNT_TERM", rsTmp.getString("DISCOUNT_TERM"));
                rsAmendHeader.updateString("A_EXCISE_TERM", rsTmp.getString("EXCISE_TERM"));
                rsAmendHeader.updateString("A_ST_TERM", rsTmp.getString("ST_TERM"));
                rsAmendHeader.updateString("A_PF_TERM", rsTmp.getString("PF_TERM"));
                rsAmendHeader.updateString("A_FREIGHT_TERM", rsTmp.getString("FREIGHT_TERM"));
                rsAmendHeader.updateString("A_OCTROI_TERM", rsTmp.getString("OCTROI_TERM"));
                rsAmendHeader.updateString("A_CENVAT_TERM", rsTmp.getString("CENVAT_TERM"));
                rsAmendHeader.updateString("A_SERVICE_TAX_TERM", rsTmp.getString("SERVICE_TAX_TERM"));
                rsAmendHeader.updateString("A_OTHER_TERM", rsTmp.getString("OTHERS_TERM"));
                rsAmendHeader.updateString("A_FOB_TERM", rsTmp.getString("FOB_TERM"));

                rsAmendHeader.updateString("A_CGST_TERM", rsTmp.getString("CGST_TERM"));
                rsAmendHeader.updateString("A_SGST_TERM", rsTmp.getString("SGST_TERM"));
                rsAmendHeader.updateString("A_IGST_TERM", rsTmp.getString("IGST_TERM"));
                rsAmendHeader.updateString("A_COMPOSITION_TERM", rsTmp.getString("COMPOSITION_TERM"));
                rsAmendHeader.updateString("A_RCM_TERM", rsTmp.getString("RCM_TERM"));
                rsAmendHeader.updateString("A_GST_COMPENSATION_CESS_TERM", rsTmp.getString("GST_COMPENSATION_CESS_TERM"));

                rsAmendHeader.updateBoolean("APPROVED", rsTmp.getBoolean("APPROVED"));
            }

            rsTmp = data.getResult("SELECT * FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "' AND PO_TYPE=" + pPOType);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {

                rsAmendHeader.updateString("PAYMENT_TERM", rsTmp.getString("PAYMENT_TERM"));
                rsAmendHeader.updateString("PRICE_BASIS_TERM", rsTmp.getString("PRICE_BASIS_TERM"));
                rsAmendHeader.updateString("DISCOUNT_TERM", rsTmp.getString("DISCOUNT_TERM"));
                rsAmendHeader.updateString("EXCISE_TERM", rsTmp.getString("EXCISE_TERM"));
                rsAmendHeader.updateString("ST_TERM", rsTmp.getString("ST_TERM"));
                rsAmendHeader.updateString("PF_TERM", rsTmp.getString("PF_TERM"));
                rsAmendHeader.updateString("FREIGHT_TERM", rsTmp.getString("FREIGHT_TERM"));
                rsAmendHeader.updateString("OCTROI_TERM", rsTmp.getString("OCTROI_TERM"));
                rsAmendHeader.updateString("CENVAT_TERM", rsTmp.getString("CENVAT_TERM"));
                rsAmendHeader.updateString("SERVICE_TAX_TERM", rsTmp.getString("SERVICE_TAX_TERM"));
                rsAmendHeader.updateString("OTHER_TERM", rsTmp.getString("OTHERS_TERM"));
                rsAmendHeader.updateString("FOB_TERM", rsTmp.getString("FOB_TERM"));

                rsAmendHeader.updateString("CGST_TERM", rsTmp.getString("CGST_TERM"));
                rsAmendHeader.updateString("SGST_TERM", rsTmp.getString("SGST_TERM"));
                rsAmendHeader.updateString("IGST_TERM", rsTmp.getString("IGST_TERM"));
                rsAmendHeader.updateString("COMPOSITION_TERM", rsTmp.getString("COMPOSITION_TERM"));
                rsAmendHeader.updateString("RCM_TERM", rsTmp.getString("RCM_TERM"));
                rsAmendHeader.updateString("GST_COMPENSATION_CESS_TERM", rsTmp.getString("GST_COMPENSATION_CESS_TERM"));
            }

            rsAmendHeader.insertRow();

            rsItem = data.getResult("SELECT * FROM D_PUR_AMEND_DETAIL WHERE AMEND_NO='" + pAmendNo + "' AND PO_TYPE=" + pPOType);
            rsItem.first();
            int maxpono = data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_PUR_PO_DETAIL_H WHERE PO_NO='" + PONo + "' AND PO_TYPE=" + pPOType);

            while (!rsItem.isAfterLast()) {
                int SrNo = rsItem.getInt("SR_NO");

                //Update Line
                rsAmendDetail.moveToInsertRow();
                rsAmendDetail.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                rsAmendDetail.updateString("AMEND_NO", pAmendNo);
                rsAmendDetail.updateInt("PO_TYPE", pPOType);
                rsAmendDetail.updateInt("SR_NO", rsItem.getInt("SR_NO"));

                rsAmendDetail.updateString("A_ITEM_ID", rsItem.getString("ITEM_ID"));
                try{
                rsAmendDetail.updateString("A_VENDOR_SHADE", rsItem.getString("VENDOR_SHADE"));
                }catch(Exception a){
                    rsAmendDetail.updateString("A_VENDOR_SHADE", "");
                }
                try{
                rsAmendDetail.updateString("A_SDML_SHADE", rsItem.getString("SDML_SHADE"));
                }catch(Exception b){
                    rsAmendDetail.updateString("A_SDML_SHADE", "");
                }
                rsAmendDetail.updateString("A_REFERENCE", rsItem.getString("REFERENCE"));

                rsAmendDetail.updateString("A_ITEM_EXTRA_DESC", rsItem.getString("ITEM_DESC"));
                rsAmendDetail.updateDouble("A_QTY", rsItem.getDouble("QTY"));
                rsAmendDetail.updateDouble("A_RATE", rsItem.getDouble("RATE"));
                rsAmendDetail.updateDouble("A_DISC_PER", rsItem.getDouble("COLUMN_1_PER"));
                rsAmendDetail.updateDouble("A_DISC_AMT", rsItem.getDouble("COLUMN_1_AMT"));

                if (rsItem.getString("COLUMN_3_CAPTION").equalsIgnoreCase("Excise")) {
                    rsAmendDetail.updateDouble("A_EXCISE", rsItem.getDouble("COLUMN_3_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_EXCISE", rsItem.getDouble("COLUMN_9_AMT"));
                }

                if (rsItem.getString("COLUMN_4_CAPTION").equalsIgnoreCase("S.T.")) {
                    rsAmendDetail.updateDouble("A_ST", rsItem.getDouble("COLUMN_4_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_ST", rsItem.getDouble("COLUMN_10_AMT"));
                }

                if (rsItem.getString("COLUMN_7_CAPTION").equalsIgnoreCase("Cenvate")) {
                    rsAmendDetail.updateDouble("A_CENVAT", rsItem.getDouble("COLUMN_7_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_CENVAT", rsItem.getDouble("COLUMN_13_AMT"));
                }

                if (rsItem.getString("COLUMN_8_CAPTION").equalsIgnoreCase("Service Tax")) {
                    rsAmendDetail.updateDouble("A_SERVICE_TAX", rsItem.getDouble("COLUMN_8_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_SERVICE_TAX", rsItem.getDouble("COLUMN_14_AMT"));
                }

                if (rsItem.getString("COLUMN_11_CAPTION").equalsIgnoreCase("FOB Charges")) {
                    rsAmendDetail.updateDouble("A_FOB_AMT", rsItem.getDouble("COLUMN_11_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_FOB_AMT", rsItem.getDouble("COLUMN_17_AMT"));
                }

                rsAmendDetail.updateString("A_DELIVERY_DATE", rsItem.getString("DELIVERY_DATE"));

                if (rsItem.getString("COLUMN_3_CAPTION").equalsIgnoreCase("CGST")) {
                    rsAmendDetail.updateDouble("A_CGST", rsItem.getDouble("COLUMN_3_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_CGST", 0);
                }

                if (rsItem.getString("COLUMN_4_CAPTION").equalsIgnoreCase("SGST")) {
                    rsAmendDetail.updateDouble("A_SGST", rsItem.getDouble("COLUMN_4_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_SGST", 0);
                }

                if (rsItem.getString("COLUMN_5_CAPTION").equalsIgnoreCase("IGST")) {
                    rsAmendDetail.updateDouble("A_IGST", rsItem.getDouble("COLUMN_5_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_IGST", 0);
                }

                if (rsItem.getString("COLUMN_6_CAPTION").equalsIgnoreCase("Composition")) {
                    rsAmendDetail.updateDouble("A_COMPOSITION", rsItem.getDouble("COLUMN_6_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_COMPOSITION", 0);
                }

                if (rsItem.getString("COLUMN_7_CAPTION").equalsIgnoreCase("RCM")) {
                    rsAmendDetail.updateDouble("A_RCM", rsItem.getDouble("COLUMN_7_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_RCM", 0);
                }

                if (rsItem.getString("COLUMN_8_CAPTION").equalsIgnoreCase("GST Comp Cess")) {
                    rsAmendDetail.updateDouble("A_GST_COMPENSATION_CESS", rsItem.getDouble("COLUMN_8_AMT"));
                } else {
                    rsAmendDetail.updateDouble("A_GST_COMPENSATION_CESS", 0);
                }

                //Fill Blank
                rsAmendDetail.updateString("ITEM_ID", rsItem.getString("ITEM_ID"));
                rsAmendDetail.updateString("ITEM_EXTRA_DESC", "");
                rsAmendDetail.updateDouble("QTY", 0);
                rsAmendDetail.updateDouble("RATE", 0);
                rsAmendDetail.updateDouble("DISC_PER", 0);
                rsAmendDetail.updateDouble("DISC_AMT", 0);
                rsAmendDetail.updateDouble("EXCISE", 0);
                rsAmendDetail.updateDouble("ST", 0);
                rsAmendDetail.updateDouble("CENVAT", 0);
                rsAmendDetail.updateDouble("SERVICE_TAX", 0);
                rsAmendDetail.updateDouble("FOB_AMT", 0);
                rsAmendDetail.updateString("DELIVERY_DATE", "0000-00-00");

                rsAmendDetail.updateDouble("CGST", 0);
                rsAmendDetail.updateDouble("SGST", 0);
                rsAmendDetail.updateDouble("IGST", 0);
                rsAmendDetail.updateDouble("COMPOSITION", 0);
                rsAmendDetail.updateDouble("RCM", 0);
                rsAmendDetail.updateDouble("GST_COMPENSATION_CESS", 0);

                //Read PO Data
                //Changes done on 20-11-2018 by Ashutosh as for amendment of po cant reflect on history table which fetches wrong value for 2nd amend. so detail table consider instead of history table
                //if(maxpono==0){
                   rsTmp = data.getResult("SELECT * FROM D_PUR_PO_DETAIL WHERE PO_NO='" + PONo + "' AND PO_TYPE=" + pPOType + " AND SR_NO=" + SrNo);
                //}else{
                //    rsTmp = data.getResult("SELECT * FROM D_PUR_PO_DETAIL_H WHERE PO_NO='" + PONo + "' AND PO_TYPE=" + pPOType + " AND REVISION_NO=" + maxpono + " AND SR_NO=" + SrNo);
                //}
                rsTmp.first();
                if (rsTmp.getRow() > 0) {
                    rsAmendDetail.updateString("ITEM_ID", rsTmp.getString("ITEM_ID"));
                    try{
                    rsAmendDetail.updateString("VENDOR_SHADE", rsTmp.getString("VENDOR_SHADE"));
                    }catch(Exception a){
                        rsAmendDetail.updateString("VENDOR_SHADE", "");
                    }
                    try{
                    rsAmendDetail.updateString("SDML_SHADE", rsTmp.getString("SDML_SHADE"));
                    }catch(Exception b){
                        rsAmendDetail.updateString("SDML_SHADE", "");
                    }
                    rsAmendDetail.updateString("REFERENCE", rsTmp.getString("REFERENCE"));
                    rsAmendDetail.updateString("ITEM_EXTRA_DESC", rsTmp.getString("ITEM_DESC"));
                    rsAmendDetail.updateDouble("QTY", rsTmp.getDouble("QTY"));
                    rsAmendDetail.updateDouble("RATE", rsTmp.getDouble("RATE"));
                    rsAmendDetail.updateDouble("DISC_PER", rsTmp.getDouble("COLUMN_1_PER"));
                    rsAmendDetail.updateDouble("DISC_AMT", rsTmp.getDouble("COLUMN_1_AMT"));

                    if (rsTmp.getString("COLUMN_3_CAPTION").equalsIgnoreCase("Excise")) {
                        rsAmendDetail.updateDouble("EXCISE", rsTmp.getDouble("COLUMN_3_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("EXCISE", rsTmp.getDouble("COLUMN_9_AMT"));
                    }

                    if (rsTmp.getString("COLUMN_4_CAPTION").equalsIgnoreCase("S.T.")) {
                        rsAmendDetail.updateDouble("ST", rsTmp.getDouble("COLUMN_4_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("ST", rsTmp.getDouble("COLUMN_10_AMT"));
                    }

                    if (rsTmp.getString("COLUMN_7_CAPTION").equalsIgnoreCase("Cenvate")) {
                        rsAmendDetail.updateDouble("CENVAT", rsTmp.getDouble("COLUMN_7_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("CENVAT", rsTmp.getDouble("COLUMN_13_AMT"));
                    }

                    if (rsTmp.getString("COLUMN_8_CAPTION").equalsIgnoreCase("Service Tax")) {
                        rsAmendDetail.updateDouble("SERVICE_TAX", rsTmp.getDouble("COLUMN_8_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("SERVICE_TAX", rsTmp.getDouble("COLUMN_14_AMT"));
                    }

                    if (rsTmp.getString("COLUMN_11_CAPTION").equalsIgnoreCase("FOB Charges")) {
                        rsAmendDetail.updateDouble("FOB_AMT", rsTmp.getDouble("COLUMN_11_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("FOB_AMT", rsTmp.getDouble("COLUMN_17_AMT"));
                    }

                    rsAmendDetail.updateString("DELIVERY_DATE", rsTmp.getString("DELIVERY_DATE"));

                    if (rsTmp.getString("COLUMN_3_CAPTION").equalsIgnoreCase("CGST")) {
                        rsAmendDetail.updateDouble("CGST", rsTmp.getDouble("COLUMN_3_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("CGST", 0);
                    }

                    if (rsTmp.getString("COLUMN_4_CAPTION").equalsIgnoreCase("SGST")) {
                        rsAmendDetail.updateDouble("SGST", rsTmp.getDouble("COLUMN_4_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("SGST", 0);
                    }

                    if (rsTmp.getString("COLUMN_5_CAPTION").equalsIgnoreCase("IGST")) {
                        rsAmendDetail.updateDouble("IGST", rsTmp.getDouble("COLUMN_5_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("IGST", 0);
                    }

                    if (rsTmp.getString("COLUMN_6_CAPTION").equalsIgnoreCase("Composition")) {
                        rsAmendDetail.updateDouble("COMPOSITION", rsTmp.getDouble("COLUMN_6_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("COMPOSITION", 0);
                    }

                    if (rsTmp.getString("COLUMN_7_CAPTION").equalsIgnoreCase("RCM")) {
                        rsAmendDetail.updateDouble("RCM", rsTmp.getDouble("COLUMN_7_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("RCM", 0);
                    }

                    if (rsTmp.getString("COLUMN_8_CAPTION").equalsIgnoreCase("GST Comp Cess")) {
                        rsAmendDetail.updateDouble("GST_COMPENSATION_CESS", rsTmp.getDouble("COLUMN_8_AMT"));
                    } else {
                        rsAmendDetail.updateDouble("GST_COMPENSATION_CESS", 0);
                    }

                }

                rsAmendDetail.insertRow();

                rsItem.next();
            }

        } catch (Exception e) {
          e.printStackTrace();
        }

    }

    private void resetColors() {
        try {
            txtQuotationNo.setBackground(Color.WHITE);
            txtInquiryNo.setBackground(Color.WHITE);
            cmbTransportMode.setBackground(Color.WHITE);
            txtPurpose.setBackground(Color.WHITE);
            txtSubject.setBackground(Color.WHITE);
            txtAmendReason.setBackground(Color.WHITE);
            txtLine1.setBackground(Color.WHITE);
            txtLine2.setBackground(Color.WHITE);
            txtRemarks.setBackground(Color.WHITE);
            txtPaymentTerm.setBackground(Color.WHITE);
            txtPriceBasisTerm.setBackground(Color.WHITE);
            txtDiscountTerm.setBackground(Color.WHITE);
            txtExciseTerm.setBackground(Color.WHITE);
            txtSTTerm.setBackground(Color.WHITE);
            txtPFTerm.setBackground(Color.WHITE);
            txtFreightTerm.setBackground(Color.WHITE);
            txtOctroiTerm.setBackground(Color.WHITE);
            txtServiceTax.setBackground(Color.WHITE);
            txtFOBTerm.setBackground(Color.WHITE);
            txtCIETerm.setBackground(Color.WHITE);
            txtInsuranceTerm.setBackground(Color.WHITE);
            txtTCCTerm.setBackground(Color.WHITE);
            txtCenvatTerm.setBackground(Color.WHITE);
            txtOthersTerm.setBackground(Color.WHITE);
            txtDespatchTerm.setBackground(Color.WHITE);

            txtCGSTTerm.setBackground(Color.WHITE);
            txtSGSTTerm.setBackground(Color.WHITE);
            txtIGSTTerm.setBackground(Color.WHITE);
            txtCompositionTerm.setBackground(Color.WHITE);
            txtRCMTerm.setBackground(Color.WHITE);
            txtGSTCompCessTerm.setBackground(Color.WHITE);

        } catch (Exception e) {

        }
    }

    private void EmailPreviewReport() {

        if (EditMode == 0) {
            frmSendMail ObjSend = new frmSendMail();
            ObjSend.ModuleID = 27 + POType;

            if (POType == 8) {
                ObjSend.ModuleID = 47;
            }

            //start add on 110909
            if (POType == 9) {
                ObjSend.ModuleID = 168;
            }
            //end add on 110909

            ObjSend.SentBy = EITLERPGLOBAL.gNewUserID;
            ObjSend.MailDocNo = txtDocNo.getText().trim();

            String FileName = "doc" + ObjSend.ModuleID + txtDocNo.getText() + ".pdf";

            String suppEMail = clsSupplier.getSupplierEMail(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());

            if (suppEMail.trim().equals("")) {
                return;
            }

            ObjSend.colRecList.clear();
            ObjSend.colRecList.put(Integer.toString(ObjSend.colRecList.size() + 1), suppEMail);

            if (chkCancelled.isSelected()) {
                JOptionPane.showMessageDialog(null, "You cannot mail cancelled document");
                return;
            }

            int FinalApprover = 0;
            String strFinalApprover = "";
            int HierarchyID = (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();

            FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
            strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

            PrepareAmendment(txtDocNo.getText(), POType);

            if (POType == 4) //Raw Material
            {

                if (HierarchyID == 247 || HierarchyID == 248) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 536 || HierarchyID == 558) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendRawMail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 5) //Import Spares
            {

                if (HierarchyID == 199 || HierarchyID == 201) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 553 || HierarchyID == 554) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendImportMail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 6) {
                if (HierarchyID == 570) {

                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital4Mail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                } else {
                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                }
            }

            if (POType != 4 && POType != 5 && POType != 6) //Others
            {
                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }
            }

            ObjSend.ShowWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Cannot send email while in edit mode. Please save the document and then try");
        }

    }

    private void EmailPreviewReport_GST() {

        if (EditMode == 0) {
            frmSendMail ObjSend = new frmSendMail();
            ObjSend.ModuleID = 27 + POType;

            if (POType == 8) {
                ObjSend.ModuleID = 47;
            }

            //start add on 110909
            if (POType == 9) {
                ObjSend.ModuleID = 168;
            }
            //end add on 110909

            ObjSend.SentBy = EITLERPGLOBAL.gNewUserID;
            ObjSend.MailDocNo = txtDocNo.getText().trim();

            String FileName = "doc" + ObjSend.ModuleID + txtDocNo.getText() + ".pdf";

            String suppEMail = clsSupplier.getSupplierEMail(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());

            if (suppEMail.trim().equals("")) {
                return;
            }

            ObjSend.colRecList.clear();
            ObjSend.colRecList.put(Integer.toString(ObjSend.colRecList.size() + 1), suppEMail);

            if (chkCancelled.isSelected()) {
                JOptionPane.showMessageDialog(null, "You cannot mail cancelled document");
                return;
            }

            int FinalApprover = 0;
            String strFinalApprover = "";
            int HierarchyID = (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();

            FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
            strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

            PrepareAmendment(txtDocNo.getText(), POType);

            if (POType == 4) //Raw Material
            {

                if (HierarchyID == 247 || HierarchyID == 248) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 536 || HierarchyID == 558) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendRawMail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 5) //Import Spares
            {

                if (HierarchyID == 199 || HierarchyID == 201) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 553 || HierarchyID == 554) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendImportMail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 6) {
                if (HierarchyID == 570) {

                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital4Mail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                } else {
                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                }
            }

            if (POType != 4 && POType != 5 && POType != 6) //Others
            {
                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail_GST.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }
            }

            ObjSend.ShowWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Cannot send email while in edit mode. Please save the document and then try");
        }

    }

    private void EmailPreviewReport_TRN() {

        if (EditMode == 0) {
            frmSendMail ObjSend = new frmSendMail();
            ObjSend.ModuleID = 27 + POType;

            if (POType == 8) {
                ObjSend.ModuleID = 47;
            }

            //start add on 110909
            if (POType == 9) {
                ObjSend.ModuleID = 168;
            }
            //end add on 110909

            ObjSend.SentBy = EITLERPGLOBAL.gNewUserID;
            ObjSend.MailDocNo = txtDocNo.getText().trim();

            String FileName = "doc" + ObjSend.ModuleID + txtDocNo.getText() + ".pdf";

            String suppEMail = clsSupplier.getSupplierEMail(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim());

            if (suppEMail.trim().equals("")) {
                return;
            }

            ObjSend.colRecList.clear();
            ObjSend.colRecList.put(Integer.toString(ObjSend.colRecList.size() + 1), suppEMail);

            if (chkCancelled.isSelected()) {
                JOptionPane.showMessageDialog(null, "You cannot mail cancelled document");
                return;
            }

            int FinalApprover = 0;
            String strFinalApprover = "";
            int HierarchyID = (int) ObjPOAmend.getAttribute("HIERARCHY_ID").getVal();

            FinalApprover = clsHierarchy.getFinalApprover(EITLERPGLOBAL.gCompanyID, HierarchyID);
            strFinalApprover = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FinalApprover);

            PrepareAmendment(txtDocNo.getText(), POType);

            if (POType == 4) //Raw Material
            {

                if (HierarchyID == 247 || HierarchyID == 248) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 536 || HierarchyID == 558) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendRawMail_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 5) //Import Spares
            {

                if (HierarchyID == 199 || HierarchyID == 201) {
                    strFinalApprover = "A. B. Tewary";
                }

                if (HierarchyID == 553 || HierarchyID == 554) {
                    strFinalApprover = "Vinod Patel";
                }

                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendImportMail_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&APPROVER=" + strFinalApprover + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }

            }

            if (POType == 6) {
                if (HierarchyID == 570) {

                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendCapital4Mail_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                } else {
                    try {
                        URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                        MailDocument.openConnection();
                        MailDocument.openStream();
                    } catch (Exception e) {

                    }

                }
            }

            if (POType != 4 && POType != 5 && POType != 6) //Others
            {
                try {
                    URL MailDocument = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptPOAmendMail_TRN.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtDocNo.getText() + "&File=" + FileName);
                    MailDocument.openConnection();
                    MailDocument.openStream();
                } catch (Exception e) {

                }
            }

            ObjSend.ShowWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Cannot send email while in edit mode. Please save the document and then try");
        }

    }

}
