/*
 * frmSupplier.java
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP; 

/**
 *
 * @author jadave
 */
/*<APPLET CODE=frmSupplier.class HEIGHT=574 WIDTH=758></APPLET>*/
import javax.swing.*; 
import java.awt.*;
import java.util.*;
import EITLERP.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import java.lang.*;
import javax.swing.text.*;
import EITLERP.Finance.*;

public class frmSupplier extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLTableModel DataModelH;
    private EITLTableModel DataModelL;
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private EITLTableModel DataModelD;
    private EITLTableModel DataModelP;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLComboModel cmbStateModel;
    private EITLComboModel cmbCountryModel;
    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    clsColumn ObjColumn = new clsColumn();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private clsSupplier ObjSupplier;

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0; 

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbPaymentModel;

    private EITLComboModel cmbServiceTaxModel;
    private EITLComboModel cmbCSTModel;
    private EITLComboModel cmbGSTModel;
    private EITLComboModel cmbGSTINModel;
    private EITLComboModel cmbECCModel;
    private EITLComboModel cmbSSIModel;
    private EITLComboModel cmbESIModel;
    private EITLComboModel cmbPANModel;
    private EITLComboModel cmbMSMEUANModel;

    private EITLTableModel DataModelA;

    private boolean HistoryView = false;
    private String theDocNo = "";
    private EITLTableModel DataModelHS;

    public frmPendingApprovals frmPA;

    /**
     * Creates new form frmSupplier
     */
    public frmSupplier() {
        setSize(815, 650);
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

        ObjColumn.LoadData((int) EITLERPGLOBAL.gCompanyID);

        FormatGrid();
        FormatGridP();

        // SetNumberFormats();
        GenerateCombos();
        GenerateStateCombo();
        GenerateCountryCombo();
        ObjSupplier = new clsSupplier();
        if (ObjSupplier.LoadData(EITLERPGLOBAL.gCompanyID)) {
            //ObjSupplier.MoveFirst();
            DisplayData();
            SetMenuForRights();
        } else {
            JOptionPane.showMessageDialog(null, "Error occured while loading data. \n Error is " + ObjSupplier.LastError);
        }

        txtAuditRemarks.setVisible(false);
        lblAmendNo.setVisible(false);
        cmbMSMEUAN.setVisible(false);
    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=37");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=37");
        }

        for (int i = 1; i <= List.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //

        cmbPaymentModel = new EITLComboModel();
        cmbPayment.removeAllItems();
        cmbPayment.setModel(cmbPaymentModel);

        strCondition = " WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND PARA_ID='PAYMENT_CODE'";
        List = clsParameter.getList(strCondition);

        ComboData oData = new ComboData();
        oData.Code = 0;
        oData.Text = "";
        oData.strCode = "";
        cmbPaymentModel.addElement(oData);
        for (int i = 1; i <= List.size(); i++) {
            clsParameter ObjPara = (clsParameter) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjPara.getAttribute("PARA_CODE").getVal();
            aData.Text = (String) ObjPara.getAttribute("DESC").getObj();
            aData.strCode = "";
            cmbPaymentModel.addElement(aData);
        }

        cmbServiceTaxModel = new EITLComboModel();
        cmbServiceTax.removeAllItems();
        cmbServiceTax.setModel(cmbServiceTaxModel);

        ComboData aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbServiceTaxModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbServiceTaxModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbServiceTaxModel.addElement(aData);

        cmbCSTModel = new EITLComboModel();
        cmbCST.removeAllItems();
        cmbCST.setModel(cmbCSTModel);

        aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbCSTModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbCSTModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbCSTModel.addElement(aData);

        cmbGSTModel = new EITLComboModel();
        cmbGST.removeAllItems();
        cmbGST.setModel(cmbGSTModel);

        aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbGSTModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbGSTModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbGSTModel.addElement(aData);

        cmbGSTINModel = new EITLComboModel();
        cmbGSTIN.removeAllItems();
        cmbGSTIN.setModel(cmbGSTINModel);

        aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbGSTINModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbGSTINModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbGSTINModel.addElement(aData);

        cmbECCModel = new EITLComboModel();
        cmbECC.removeAllItems();
        cmbECC.setModel(cmbECCModel);

        aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbECCModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbECCModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbECCModel.addElement(aData);

        cmbSSIModel = new EITLComboModel();
        cmbSSI.removeAllItems();
        cmbSSI.setModel(cmbSSIModel);

        aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbSSIModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbSSIModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbSSIModel.addElement(aData);

        cmbESIModel = new EITLComboModel();
        cmbESI.removeAllItems();
        cmbESI.setModel(cmbESIModel);

        aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbESIModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbESIModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbESIModel.addElement(aData);

        cmbPANModel = new EITLComboModel();
        cmbPAN.removeAllItems();
        cmbPAN.setModel(cmbPANModel);

        aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbPANModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbPANModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbPANModel.addElement(aData);

        cmbMSMEUANModel = new EITLComboModel();
        cmbMSMEUAN.removeAllItems();
        cmbMSMEUAN.setModel(cmbMSMEUANModel);

        aData = new ComboData();
        aData.strCode = "N/A";
        aData.Text = "N/A";
        cmbMSMEUANModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "APPLIED";
        aData.Text = "APPLIED";
        cmbMSMEUANModel.addElement(aData);

        aData = new ComboData();
        aData.strCode = "Enter";
        aData.Text = "Enter";
        cmbMSMEUANModel.addElement(aData);
    }

    private void FormatGrid() {
        HashMap ColList = new HashMap();

        DataModelL = new EITLTableModel();

        TableL.removeAll();
        TableL.setModel(DataModelL);

        //Set the table Readonly
        DataModelL.TableReadOnly(false);

        ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=37 AND HIDDEN=0 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
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

            DataModelL.SetColID(TableL.getColumnCount() - 1, 0);

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

        //----- Install Table Model Event Listener -------//
        TableL.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int col = e.getColumn();

                    if (col == DataModelL.getColFromVariable("CATEGORY_ID")) {

                        String CategoryID = DataModelL.getValueByVariable("CATEGORY_ID", TableL.getSelectedRow());
                        TableL.setValueAt(clsItemCategory.getCategoryDesc((int) EITLERPGLOBAL.gCompanyID, Long.parseLong(CategoryID)), TableL.getSelectedRow(), DataModelL.getColFromVariable("CATEGORY_DESC"));
                    }
                    /*        if(!Updating) {
                     UpdateResults(col);
                     }*/

                }
            }
        });
    }

    private void SetupColumns() {
        HashMap List = new HashMap();
        HashMap ColList = new HashMap();

        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableL.setRowSelectionAllowed(true);
        TableL.setColumnSelectionAllowed(true);

        ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=37 AND HIDDEN=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for (int i = 1; i <= ColList.size(); i++) {
            clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));

            //Add Column First
            DataModelL.addColumn(""); //
            DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
            DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE").getObj());
            DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
            DataModelL.SetInclude(TableL.getColumnCount() - 1, true);

            DataModelL.SetReadOnly(TableL.getColumnCount() - 1);

            //Hide the Column
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setMaxWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setWidth(0);
        }
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtDummyCode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        cmdNext1 = new javax.swing.JButton();
        txtSuppCode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAttn = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAdd1 = new javax.swing.JTextField();
        txtAdd2 = new javax.swing.JTextField();
        txtAdd3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPIN = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtPhone_O = new javax.swing.JTextField();
        txtPhone_R = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtFax = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtMobile = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtWebsite = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtURL = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtCP2 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        lblBankID = new javax.swing.JLabel();
        txtBankID = new javax.swing.JTextField();
        lblPaymentDays = new javax.swing.JLabel();
        txtPayDays = new javax.swing.JTextField();
        chkOneTime = new javax.swing.JCheckBox();
        chkBlocked = new javax.swing.JCheckBox();
        chkSlow = new javax.swing.JCheckBox();
        chkReg = new javax.swing.JCheckBox();
        txtBankName = new javax.swing.JTextField();
        chkSSI = new javax.swing.JCheckBox();
        lblRevNo = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cmbPayment = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        txtItem = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        TableP = new javax.swing.JTable();
        cmdAddTerm = new javax.swing.JButton();
        cmdRemoveTerm = new javax.swing.JButton();
        lblAmendNo = new javax.swing.JLabel();
        cmbState = new javax.swing.JComboBox();
        cmbCountry = new javax.swing.JComboBox();
        jLabel42 = new javax.swing.JLabel();
        txtCP1 = new javax.swing.JTextField();
        txtPlaceofsupply = new javax.swing.JTextField();
        txtStateCode = new javax.swing.JTextField();
        txtStateGstCode = new javax.swing.JTextField();
        txtCountryName = new javax.swing.JTextField();
        txtStateName = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        txtDistanceKm = new javax.swing.JTextField();
        Tab3 = new javax.swing.JPanel();
        cmdNext2 = new javax.swing.JButton();
        cmdNext4 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtRegFrom = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtRegTo = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtSTNO = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtSTDate = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtCST = new javax.swing.JTextField();
        txtCSTDate = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtGST = new javax.swing.JTextField();
        txtGSTDate = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtECC = new javax.swing.JTextField();
        txtECCDate = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txtSSI = new javax.swing.JTextField();
        txtSSIDate = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtESI = new javax.swing.JTextField();
        txtESIDate = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        cmbESI = new javax.swing.JComboBox();
        cmbServiceTax = new javax.swing.JComboBox();
        cmbCST = new javax.swing.JComboBox();
        cmbGST = new javax.swing.JComboBox();
        cmbECC = new javax.swing.JComboBox();
        cmbSSI = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        cmbPAN = new javax.swing.JComboBox();
        txtPAN = new javax.swing.JTextField();
        txtPANDate = new javax.swing.JTextField();
        cmbGSTIN = new javax.swing.JComboBox();
        txtGSTINDate = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtGSTIN = new javax.swing.JTextField();
        txtMSMEUAN = new javax.swing.JTextField();
        cmbMSMEUAN = new javax.swing.JComboBox();
        jLabel43 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txtMSMEDIC = new javax.swing.JTextField();
        chkMSME = new javax.swing.JCheckBox();
        Tab2 = new javax.swing.JPanel();
        cmdNext5 = new javax.swing.JButton();
        cmdBack2 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableD = new javax.swing.JTable();
        cmdDeliveryAdd = new javax.swing.JButton();
        cmdDeliveryRemove = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableO = new javax.swing.JTable();
        cmdOtherAdd = new javax.swing.JButton();
        cmdOtherRemove = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableL = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtPSuppID = new javax.swing.JTextField();
        txtPSuppName = new javax.swing.JTextField();
        txtCSuppID = new javax.swing.JTextField();
        txtCSuppName = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        TableSC = new javax.swing.JTable();
        cmdAddC = new javax.swing.JButton();
        cmdRemoveC = new javax.swing.JButton();
        opgParent = new javax.swing.JRadioButton();
        opgChild = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMainAccountCode = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
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

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Supplier Master");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 804, 25);

        jTabbedPane1.setToolTipText("Supplier Master Header");

        Tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.setToolTipText("Supplier Header");
        Tab1.setLayout(null);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Supplier Code :");
        Tab1.add(jLabel2);
        jLabel2.setBounds(0, 20, 115, 15);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Dummy Code :");
        Tab1.add(jLabel3);
        jLabel3.setBounds(0, 50, 115, 15);

        txtDummyCode.setEnabled(false);
        txtDummyCode.setName("DUMMY_SUPPLIER_CODE"); // NOI18N
        txtDummyCode.setNextFocusableComponent(txtName);
        Tab1.add(txtDummyCode);
        txtDummyCode.setBounds(120, 50, 120, 19);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Name :");
        Tab1.add(jLabel5);
        jLabel5.setBounds(0, 90, 115, 15);

        txtName.setEnabled(false);
        txtName.setName("SUPP_NAME"); // NOI18N
        txtName.setNextFocusableComponent(txtAttn);
        Tab1.add(txtName);
        txtName.setBounds(120, 90, 322, 19);

        cmdNext1.setText("Next >>");
        cmdNext1.setNextFocusableComponent(txtSuppCode);
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
        cmdNext1.setBounds(679, 354, 90, 25);

        txtSuppCode.setEnabled(false);
        txtSuppCode.setName("SUPPLIER_CODE"); // NOI18N
        txtSuppCode.setNextFocusableComponent(txtDummyCode);
        txtSuppCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusLost(evt);
            }
        });
        Tab1.add(txtSuppCode);
        txtSuppCode.setBounds(125, 14, 120, 19);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("ATTN :");
        Tab1.add(jLabel4);
        jLabel4.setBounds(0, 120, 115, 15);

        txtAttn.setEnabled(false);
        txtAttn.setName("ATTN"); // NOI18N
        txtAttn.setNextFocusableComponent(txtAdd1);
        Tab1.add(txtAttn);
        txtAttn.setBounds(120, 120, 322, 19);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Address :");
        Tab1.add(jLabel7);
        jLabel7.setBounds(0, 150, 115, 15);

        txtAdd1.setEnabled(false);
        txtAdd1.setName("ADD1"); // NOI18N
        txtAdd1.setNextFocusableComponent(txtAdd2);
        Tab1.add(txtAdd1);
        txtAdd1.setBounds(120, 150, 322, 19);

        txtAdd2.setEnabled(false);
        txtAdd2.setName("ADD2"); // NOI18N
        txtAdd2.setNextFocusableComponent(txtAdd3);
        Tab1.add(txtAdd2);
        txtAdd2.setBounds(120, 180, 322, 19);

        txtAdd3.setEnabled(false);
        txtAdd3.setName("ADD3"); // NOI18N
        txtAdd3.setNextFocusableComponent(txtCity);
        Tab1.add(txtAdd3);
        txtAdd3.setBounds(120, 210, 322, 19);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("City :");
        Tab1.add(jLabel9);
        jLabel9.setBounds(0, 240, 115, 15);

        txtCity.setEnabled(false);
        txtCity.setName("CITY"); // NOI18N
        txtCity.setNextFocusableComponent(txtPIN);
        Tab1.add(txtCity);
        txtCity.setBounds(120, 240, 120, 19);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Pincode :");
        Tab1.add(jLabel10);
        jLabel10.setBounds(240, 240, 80, 15);

        txtPIN.setEnabled(false);
        txtPIN.setName("PINCODE"); // NOI18N
        Tab1.add(txtPIN);
        txtPIN.setBounds(330, 240, 110, 19);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("State :");
        Tab1.add(jLabel6);
        jLabel6.setBounds(0, 270, 115, 15);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Country :");
        Tab1.add(jLabel8);
        jLabel8.setBounds(30, 310, 80, 15);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Phone (R) :");
        Tab1.add(jLabel11);
        jLabel11.setBounds(240, 340, 80, 15);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Phone (O) :");
        Tab1.add(jLabel12);
        jLabel12.setBounds(0, 340, 115, 15);

        txtPhone_O.setEnabled(false);
        txtPhone_O.setName("PHONE_O"); // NOI18N
        txtPhone_O.setNextFocusableComponent(txtPhone_R);
        Tab1.add(txtPhone_O);
        txtPhone_O.setBounds(120, 340, 120, 19);

        txtPhone_R.setEnabled(false);
        txtPhone_R.setName("PHONE_RES"); // NOI18N
        txtPhone_R.setNextFocusableComponent(txtFax);
        Tab1.add(txtPhone_R);
        txtPhone_R.setBounds(330, 340, 110, 19);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Fax No :");
        Tab1.add(jLabel13);
        jLabel13.setBounds(0, 370, 115, 15);

        txtFax.setEnabled(false);
        txtFax.setName("FAX_NO"); // NOI18N
        txtFax.setNextFocusableComponent(txtMobile);
        Tab1.add(txtFax);
        txtFax.setBounds(120, 370, 120, 19);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Mobile No :");
        Tab1.add(jLabel14);
        jLabel14.setBounds(240, 370, 80, 15);

        txtMobile.setEnabled(false);
        txtMobile.setName("MOBILE_NO"); // NOI18N
        txtMobile.setNextFocusableComponent(txtEmail);
        txtMobile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMobileActionPerformed(evt);
            }
        });
        Tab1.add(txtMobile);
        txtMobile.setBounds(330, 370, 110, 19);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("E-Mail :");
        Tab1.add(jLabel15);
        jLabel15.setBounds(0, 400, 115, 15);

        txtEmail.setEnabled(false);
        txtEmail.setName("EMAIL_ADD"); // NOI18N
        txtEmail.setNextFocusableComponent(txtWebsite);
        Tab1.add(txtEmail);
        txtEmail.setBounds(120, 400, 120, 19);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("URL :");
        Tab1.add(jLabel16);
        jLabel16.setBounds(0, 430, 115, 15);

        txtWebsite.setEnabled(false);
        txtWebsite.setName("WEB_SITE"); // NOI18N
        txtWebsite.setNextFocusableComponent(txtURL);
        Tab1.add(txtWebsite);
        txtWebsite.setBounds(330, 400, 110, 19);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Website :");
        Tab1.add(jLabel17);
        jLabel17.setBounds(240, 400, 80, 15);

        txtURL.setEnabled(false);
        txtURL.setName("URL"); // NOI18N
        Tab1.add(txtURL);
        txtURL.setBounds(120, 430, 322, 19);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Place Of Supply : ");
        Tab1.add(jLabel29);
        jLabel29.setBounds(0, 460, 115, 15);

        txtCP2.setEnabled(false);
        txtCP2.setName("CONTACT_PERSON_2"); // NOI18N
        txtCP2.setNextFocusableComponent(cmbPayment);
        Tab1.add(txtCP2);
        txtCP2.setBounds(120, 520, 322, 19);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Contact Person 2 :");
        Tab1.add(jLabel30);
        jLabel30.setBounds(0, 520, 115, 15);

        lblBankID.setText("Bank");
        Tab1.add(lblBankID);
        lblBankID.setBounds(480, 80, 39, 15);

        txtBankID.setEditable(false);
        txtBankID.setName("BANK_ID"); // NOI18N
        txtBankID.setNextFocusableComponent(txtBankName);
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
        Tab1.add(txtBankID);
        txtBankID.setBounds(520, 74, 58, 19);

        lblPaymentDays.setText("Days");
        Tab1.add(lblPaymentDays);
        lblPaymentDays.setBounds(475, 76, 39, 15);

        txtPayDays.setName("PAYMENT_DAYS"); // NOI18N
        txtPayDays.setNextFocusableComponent(txtItem);
        txtPayDays.setEnabled(false);
        txtPayDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPayDaysActionPerformed(evt);
            }
        });
        Tab1.add(txtPayDays);
        txtPayDays.setBounds(520, 74, 59, 19);

        chkOneTime.setText("One Time Supplier");
        chkOneTime.setName("ONETIME_SUPPLIER"); // NOI18N
        chkOneTime.setNextFocusableComponent(chkBlocked);
        chkOneTime.setEnabled(false);
        chkOneTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOneTimeActionPerformed(evt);
            }
        });
        Tab1.add(chkOneTime);
        chkOneTime.setBounds(460, 322, 140, 20);

        chkBlocked.setText("Blocked");
        chkBlocked.setName("BLOCKED"); // NOI18N
        chkBlocked.setNextFocusableComponent(chkSSI);
        chkBlocked.setEnabled(false);
        Tab1.add(chkBlocked);
        chkBlocked.setBounds(620, 322, 110, 20);

        chkSlow.setText("Slow Moving");
        chkSlow.setName("SLOW_MOVING"); // NOI18N
        chkSlow.setNextFocusableComponent(chkOneTime);
        chkSlow.setEnabled(false);
        chkSlow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSlowActionPerformed(evt);
            }
        });
        Tab1.add(chkSlow);
        chkSlow.setBounds(620, 301, 130, 20);

        chkReg.setText("Registered ");
        chkReg.setName("ST35_REGISTERED"); // NOI18N
        chkReg.setNextFocusableComponent(chkSlow);
        chkReg.setEnabled(false);
        Tab1.add(chkReg);
        chkReg.setBounds(460, 301, 130, 20);

        txtBankName.setName("BANK_NAME"); // NOI18N
        txtBankName.setNextFocusableComponent(txtPayDays);
        txtBankName.setEnabled(false);
        Tab1.add(txtBankName);
        txtBankName.setBounds(583, 73, 190, 20);

        chkSSI.setText("SSI Supplier");
        chkSSI.setName("SSIREG"); // NOI18N
        chkSSI.setNextFocusableComponent(cmdNext1);
        chkSSI.setEnabled(false);
        Tab1.add(chkSSI);
        chkSSI.setBounds(460, 343, 130, 20);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(260, 40, 38, 16);

        jLabel18.setText("Payment Terms :");
        Tab1.add(jLabel18);
        jLabel18.setBounds(460, 19, 110, 15);

        cmbPayment.setNextFocusableComponent(txtBankID);
        cmbPayment.setEnabled(false);
        cmbPayment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPaymentItemStateChanged(evt);
            }
        });
        Tab1.add(cmbPayment);
        cmbPayment.setBounds(460, 38, 282, 24);

        jLabel25.setText("Proposed Item to be Purchased");
        Tab1.add(jLabel25);
        jLabel25.setBounds(460, 250, 204, 15);

        txtItem.setEnabled(false);
        txtItem.setNextFocusableComponent(chkReg);
        Tab1.add(txtItem);
        txtItem.setBounds(460, 273, 283, 21);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(755, 273, 32, 22);

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Tab1.add(jButton2);
        jButton2.setBounds(750, 38, 35, 24);

        TableP.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(TableP);

        Tab1.add(jScrollPane5);
        jScrollPane5.setBounds(460, 143, 316, 94);

        cmdAddTerm.setText("Add Term");
        cmdAddTerm.setEnabled(false);
        cmdAddTerm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddTermActionPerformed(evt);
            }
        });
        Tab1.add(cmdAddTerm);
        cmdAddTerm.setBounds(566, 116, 109, 21);

        cmdRemoveTerm.setText("Remove");
        cmdRemoveTerm.setEnabled(false);
        cmdRemoveTerm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveTermActionPerformed(evt);
            }
        });
        Tab1.add(cmdRemoveTerm);
        cmdRemoveTerm.setBounds(679, 116, 89, 21);

        lblAmendNo.setBackground(new java.awt.Color(255, 102, 0));
        lblAmendNo.setText("Amendment No.");
        Tab1.add(lblAmendNo);
        lblAmendNo.setBounds(260, 15, 167, 15);

        cmbState.setEnabled(false);
        cmbState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStateActionPerformed(evt);
            }
        });
        Tab1.add(cmbState);
        cmbState.setBounds(330, 270, 110, 24);

        cmbCountry.setEnabled(false);
        cmbCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCountryActionPerformed(evt);
            }
        });
        Tab1.add(cmbCountry);
        cmbCountry.setBounds(280, 310, 160, 24);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Contact Person 1 :");
        Tab1.add(jLabel42);
        jLabel42.setBounds(0, 490, 115, 15);

        txtCP1.setEnabled(false);
        txtCP1.setName("CONTACT_PERSON_1"); // NOI18N
        txtCP1.setNextFocusableComponent(txtCP2);
        Tab1.add(txtCP1);
        txtCP1.setBounds(120, 490, 322, 19);

        txtPlaceofsupply.setEnabled(false);
        txtPlaceofsupply.setName("URL"); // NOI18N
        Tab1.add(txtPlaceofsupply);
        txtPlaceofsupply.setBounds(120, 460, 322, 19);

        txtStateCode.setEnabled(false);
        Tab1.add(txtStateCode);
        txtStateCode.setBounds(240, 270, 40, 19);

        txtStateGstCode.setEnabled(false);
        Tab1.add(txtStateGstCode);
        txtStateGstCode.setBounds(280, 270, 40, 19);

        txtCountryName.setEnabled(false);
        Tab1.add(txtCountryName);
        txtCountryName.setBounds(120, 310, 140, 19);

        txtStateName.setEnabled(false);
        Tab1.add(txtStateName);
        txtStateName.setBounds(120, 270, 120, 30);

        jLabel53.setText("Distance In Km :");
        Tab1.add(jLabel53);
        jLabel53.setBounds(490, 410, 110, 20);

        txtDistanceKm.setEnabled(false);
        Tab1.add(txtDistanceKm);
        txtDistanceKm.setBounds(600, 410, 160, 30);

        jTabbedPane1.addTab("Header", null, Tab1, "Quotation Header");

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.setToolTipText("Supplier Item Information");
        Tab3.setLayout(null);

        cmdNext2.setText("Next >>");
        cmdNext2.setNextFocusableComponent(cmdNext4);
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });
        Tab3.add(cmdNext2);
        cmdNext2.setBounds(670, 390, 90, 25);

        cmdNext4.setText("<< Back");
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
        cmdNext4.setBounds(580, 390, 90, 25);

        jLabel21.setText("Registration Time Limit :");
        Tab3.add(jLabel21);
        jLabel21.setBounds(10, 340, 161, 15);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Date From :");
        Tab3.add(jLabel38);
        jLabel38.setBounds(10, 370, 80, 15);

        txtRegFrom.setEnabled(false);
        txtRegFrom.setName("FROM_DATE_REG"); // NOI18N
        txtRegFrom.setNextFocusableComponent(txtRegTo);
        Tab3.add(txtRegFrom);
        txtRegFrom.setBounds(100, 370, 104, 21);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("To :");
        Tab3.add(jLabel41);
        jLabel41.setBounds(210, 370, 30, 15);

        txtRegTo.setEnabled(false);
        txtRegTo.setName("TO_DATE_REG"); // NOI18N
        Tab3.add(txtRegTo);
        txtRegTo.setBounds(240, 370, 105, 21);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Service Tax :");
        Tab3.add(jLabel22);
        jLabel22.setBounds(15, 44, 90, 15);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("No");
        Tab3.add(jLabel26);
        jLabel26.setBounds(240, 20, 197, 15);

        txtSTNO.setEnabled(false);
        txtSTNO.setName("SERVICETAX_NO"); // NOI18N
        txtSTNO.setNextFocusableComponent(txtSTDate);
        Tab3.add(txtSTNO);
        txtSTNO.setBounds(240, 42, 197, 21);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Date");
        Tab3.add(jLabel23);
        jLabel23.setBounds(450, 20, 90, 15);

        txtSTDate.setName("SERVICETAX_DATE"); // NOI18N
        txtSTDate.setNextFocusableComponent(txtCST);
        txtSTDate.setEnabled(false);
        Tab3.add(txtSTDate);
        txtSTDate.setBounds(450, 42, 90, 21);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("CST :");
        Tab3.add(jLabel24);
        jLabel24.setBounds(15, 71, 90, 15);

        txtCST.setEnabled(false);
        txtCST.setName("CST_NO"); // NOI18N
        txtCST.setNextFocusableComponent(txtCSTDate);
        Tab3.add(txtCST);
        txtCST.setBounds(240, 69, 197, 21);

        txtCSTDate.setName("CST_DATE"); // NOI18N
        txtCSTDate.setNextFocusableComponent(txtGST);
        txtCSTDate.setEnabled(false);
        Tab3.add(txtCSTDate);
        txtCSTDate.setBounds(450, 69, 90, 21);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("GST/VAT No. :");
        Tab3.add(jLabel27);
        jLabel27.setBounds(15, 100, 90, 15);

        txtGST.setEnabled(false);
        txtGST.setName("GST_NO"); // NOI18N
        txtGST.setNextFocusableComponent(txtGSTDate);
        Tab3.add(txtGST);
        txtGST.setBounds(240, 97, 197, 21);

        txtGSTDate.setEnabled(false);
        txtGSTDate.setName("GST_DATE"); // NOI18N
        txtGSTDate.setNextFocusableComponent(txtECC);
        Tab3.add(txtGSTDate);
        txtGSTDate.setBounds(450, 97, 90, 21);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("ECC :");
        Tab3.add(jLabel28);
        jLabel28.setBounds(15, 130, 90, 15);

        txtECC.setEnabled(false);
        txtECC.setName("ECC_NO"); // NOI18N
        txtECC.setNextFocusableComponent(txtECCDate);
        Tab3.add(txtECC);
        txtECC.setBounds(240, 128, 197, 21);

        txtECCDate.setName("ECC_DATE"); // NOI18N
        txtECCDate.setNextFocusableComponent(txtSSI);
        txtECCDate.setEnabled(false);
        Tab3.add(txtECCDate);
        txtECCDate.setBounds(450, 128, 90, 21);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel55.setText("SSI :");
        Tab3.add(jLabel55);
        jLabel55.setBounds(15, 163, 90, 15);

        txtSSI.setEnabled(false);
        txtSSI.setName("SSIREG_NO"); // NOI18N
        txtSSI.setNextFocusableComponent(txtSSIDate);
        Tab3.add(txtSSI);
        txtSSI.setBounds(240, 160, 197, 21);

        txtSSIDate.setEnabled(false);
        txtSSIDate.setName("SSIREG_DATE"); // NOI18N
        txtSSIDate.setNextFocusableComponent(txtESI);
        Tab3.add(txtSSIDate);
        txtSSIDate.setBounds(450, 160, 90, 21);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("ESI :");
        Tab3.add(jLabel56);
        jLabel56.setBounds(15, 194, 90, 15);

        txtESI.setEnabled(false);
        txtESI.setName("ESIREG_NO"); // NOI18N
        txtESI.setNextFocusableComponent(txtESIDate);
        Tab3.add(txtESI);
        txtESI.setBounds(240, 190, 197, 21);

        txtESIDate.setEnabled(false);
        txtESIDate.setName("ESIREG_DATE"); // NOI18N
        txtESIDate.setNextFocusableComponent(txtPAN);
        Tab3.add(txtESIDate);
        txtESIDate.setBounds(450, 190, 90, 21);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Type");
        Tab3.add(jLabel37);
        jLabel37.setBounds(125, 20, 100, 15);

        cmbESI.setEnabled(false);
        cmbESI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbESIItemStateChanged(evt);
            }
        });
        Tab3.add(cmbESI);
        cmbESI.setBounds(125, 190, 100, 21);

        cmbServiceTax.setEnabled(false);
        cmbServiceTax.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbServiceTaxItemStateChanged(evt);
            }
        });
        cmbServiceTax.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbServiceTaxMouseClicked(evt);
            }
        });
        Tab3.add(cmbServiceTax);
        cmbServiceTax.setBounds(125, 42, 100, 21);

        cmbCST.setEnabled(false);
        cmbCST.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCSTItemStateChanged(evt);
            }
        });
        Tab3.add(cmbCST);
        cmbCST.setBounds(125, 69, 100, 21);

        cmbGST.setEnabled(false);
        cmbGST.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbGSTItemStateChanged(evt);
            }
        });
        Tab3.add(cmbGST);
        cmbGST.setBounds(125, 97, 100, 21);

        cmbECC.setEnabled(false);
        cmbECC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbECCItemStateChanged(evt);
            }
        });
        Tab3.add(cmbECC);
        cmbECC.setBounds(125, 128, 100, 21);

        cmbSSI.setEnabled(false);
        cmbSSI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSSIItemStateChanged(evt);
            }
        });
        Tab3.add(cmbSSI);
        cmbSSI.setBounds(125, 160, 100, 21);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab3.add(jPanel1);
        jPanel1.setBounds(20, 320, 750, 10);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("PAN No. :");
        Tab3.add(jLabel57);
        jLabel57.setBounds(15, 222, 90, 15);

        cmbPAN.setEnabled(false);
        cmbPAN.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPANItemStateChanged(evt);
            }
        });
        Tab3.add(cmbPAN);
        cmbPAN.setBounds(120, 220, 100, 21);

        txtPAN.setEnabled(false);
        txtPAN.setName("ESIREG_NO"); // NOI18N
        txtPAN.setNextFocusableComponent(txtPANDate);
        txtPAN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPANFocusLost(evt);
            }
        });
        Tab3.add(txtPAN);
        txtPAN.setBounds(240, 220, 197, 21);

        txtPANDate.setEnabled(false);
        txtPANDate.setName("ESIREG_DATE"); // NOI18N
        txtPANDate.setNextFocusableComponent(cmbGSTIN);
        Tab3.add(txtPANDate);
        txtPANDate.setBounds(450, 220, 90, 21);

        cmbGSTIN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbGSTIN.setEnabled(false);
        cmbGSTIN.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbGSTINItemStateChanged(evt);
            }
        });
        cmbGSTIN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGSTINActionPerformed(evt);
            }
        });
        Tab3.add(cmbGSTIN);
        cmbGSTIN.setBounds(120, 250, 100, 24);

        txtGSTINDate.setEnabled(false);
        txtGSTINDate.setNextFocusableComponent(cmbMSMEUAN);
        Tab3.add(txtGSTINDate);
        txtGSTINDate.setBounds(450, 250, 90, 19);

        jLabel40.setText("DIC NO: ");
        Tab3.add(jLabel40);
        jLabel40.setBounds(460, 290, 60, 20);

        txtGSTIN.setEnabled(false);
        txtGSTIN.setName("ESIREG_NO"); // NOI18N
        txtGSTIN.setNextFocusableComponent(txtGSTINDate);
        txtGSTIN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGSTINFocusLost(evt);
            }
        });
        Tab3.add(txtGSTIN);
        txtGSTIN.setBounds(240, 250, 197, 21);

        txtMSMEUAN.setEnabled(false);
        txtMSMEUAN.setName("ESIREG_NO"); // NOI18N
        txtMSMEUAN.setNextFocusableComponent(txtMSMEDIC);
        txtMSMEUAN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMSMEUANFocusLost(evt);
            }
        });
        Tab3.add(txtMSMEUAN);
        txtMSMEUAN.setBounds(240, 290, 197, 21);

        cmbMSMEUAN.setEnabled(false);
        cmbMSMEUAN.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMSMEUANItemStateChanged(evt);
            }
        });
        Tab3.add(cmbMSMEUAN);
        cmbMSMEUAN.setBounds(740, 290, 30, 20);

        jLabel43.setText("GSTIN NO: ");
        Tab3.add(jLabel43);
        jLabel43.setBounds(31, 250, 75, 15);

        jLabel46.setText("UAN NO: ");
        Tab3.add(jLabel46);
        jLabel46.setBounds(160, 290, 70, 15);

        txtMSMEDIC.setEnabled(false);
        Tab3.add(txtMSMEDIC);
        txtMSMEDIC.setBounds(520, 290, 190, 20);

        chkMSME.setText("MSME");
        chkMSME.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkMSMEItemStateChanged(evt);
            }
        });
        Tab3.add(chkMSME);
        chkMSME.setBounds(60, 290, 70, 20);

        jTabbedPane1.addTab("Tax & Item Information", null, Tab3, "Quotation Item");

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.setLayout(null);

        cmdNext5.setText("Next >>");
        cmdNext5.setNextFocusableComponent(cmdBack2);
        cmdNext5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext5ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNext5);
        cmdNext5.setBounds(662, 348, 102, 25);

        cmdBack2.setText("<< Back");
        cmdBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBack2);
        cmdBack2.setBounds(558, 348, 102, 25);

        jLabel44.setText("Delivery Terms :");
        Tab2.add(jLabel44);
        jLabel44.setBounds(11, 7, 181, 15);

        TableD.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(TableD);

        Tab2.add(jScrollPane4);
        jScrollPane4.setBounds(11, 23, 369, 104);

        cmdDeliveryAdd.setText("Add");
        cmdDeliveryAdd.setName("DELIVERY_TERMS"); // NOI18N
        cmdDeliveryAdd.setEnabled(false);
        cmdDeliveryAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeliveryAddActionPerformed(evt);
            }
        });
        Tab2.add(cmdDeliveryAdd);
        cmdDeliveryAdd.setBounds(203, 131, 74, 19);

        cmdDeliveryRemove.setText("Remove");
        cmdDeliveryRemove.setName("DELIVERY_TERMS"); // NOI18N
        cmdDeliveryRemove.setEnabled(false);
        cmdDeliveryRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeliveryRemoveActionPerformed(evt);
            }
        });
        Tab2.add(cmdDeliveryRemove);
        cmdDeliveryRemove.setBounds(283, 131, 96, 19);

        jLabel45.setText("Other Terms :");
        Tab2.add(jLabel45);
        jLabel45.setBounds(14, 173, 160, 15);

        TableO.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(TableO);

        Tab2.add(jScrollPane3);
        jScrollPane3.setBounds(11, 191, 365, 91);

        cmdOtherAdd.setText("Add");
        cmdOtherAdd.setName("OTHER_TERMS"); // NOI18N
        cmdOtherAdd.setEnabled(false);
        cmdOtherAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOtherAddActionPerformed(evt);
            }
        });
        Tab2.add(cmdOtherAdd);
        cmdOtherAdd.setBounds(179, 286, 74, 19);

        cmdOtherRemove.setText("Remove");
        cmdOtherRemove.setName("OTHER_TERMS"); // NOI18N
        cmdOtherRemove.setEnabled(false);
        cmdOtherRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOtherRemoveActionPerformed(evt);
            }
        });
        Tab2.add(cmdOtherRemove);
        cmdOtherRemove.setBounds(259, 286, 96, 19);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setText("Supplier Item Category :");
        Tab2.add(jLabel20);
        jLabel20.setBounds(434, 6, 185, 15);

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
        TableL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableLKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(TableL);

        Tab2.add(jScrollPane1);
        jScrollPane1.setBounds(434, 26, 320, 95);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setNextFocusableComponent(cmdRemove);
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        Tab2.add(cmdAdd);
        cmdAdd.setBounds(575, 127, 86, 25);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setNextFocusableComponent(cmdNext2);
        cmdRemove.setEnabled(false);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        Tab2.add(cmdRemove);
        cmdRemove.setBounds(666, 125, 87, 25);

        jTabbedPane1.addTab("Other Information", Tab2);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(null);

        txtPSuppID.setName("SUPPLIER_CODE"); // NOI18N
        txtPSuppID.setNextFocusableComponent(txtDummyCode);
        txtPSuppID.setEnabled(false);
        txtPSuppID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPSuppIDFocusLost(evt);
            }
        });
        txtPSuppID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPSuppIDKeyPressed(evt);
            }
        });
        jPanel2.add(txtPSuppID);
        txtPSuppID.setBounds(21, 45, 120, 19);

        txtPSuppName.setName("SUPP_NAME"); // NOI18N
        txtPSuppName.setNextFocusableComponent(txtAttn);
        txtPSuppName.setEnabled(false);
        jPanel2.add(txtPSuppName);
        txtPSuppName.setBounds(146, 45, 322, 19);

        txtCSuppID.setName("SUPPLIER_CODE"); // NOI18N
        txtCSuppID.setNextFocusableComponent(txtDummyCode);
        txtCSuppID.setEnabled(false);
        txtCSuppID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCSuppIDFocusLost(evt);
            }
        });
        txtCSuppID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCSuppIDKeyPressed(evt);
            }
        });
        jPanel2.add(txtCSuppID);
        txtCSuppID.setBounds(21, 102, 120, 19);

        txtCSuppName.setName("SUPP_NAME"); // NOI18N
        txtCSuppName.setNextFocusableComponent(txtAttn);
        txtCSuppName.setEnabled(false);
        jPanel2.add(txtCSuppName);
        txtCSuppName.setBounds(146, 102, 322, 19);

        TableSC.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane7.setViewportView(TableSC);

        jPanel2.add(jScrollPane7);
        jScrollPane7.setBounds(21, 172, 444, 185);

        cmdAddC.setText("Add");
        cmdAddC.setEnabled(false);
        cmdAddC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddCActionPerformed(evt);
            }
        });
        jPanel2.add(cmdAddC);
        cmdAddC.setBounds(274, 134, 88, 25);

        cmdRemoveC.setText("Remove");
        cmdRemoveC.setEnabled(false);
        cmdRemoveC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveCActionPerformed(evt);
            }
        });
        jPanel2.add(cmdRemoveC);
        cmdRemoveC.setBounds(373, 134, 88, 25);

        grpSisterConcern.add(opgParent);
        opgParent.setSelected(true);
        opgParent.setText("Parent Supplier :");
        opgParent.setEnabled(false);
        opgParent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opgParentMouseClicked(evt);
            }
        });
        jPanel2.add(opgParent);
        opgParent.setBounds(14, 14, 145, 23);

        grpSisterConcern.add(opgChild);
        opgChild.setText("Sister Concerns :");
        opgChild.setEnabled(false);
        opgChild.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opgChildMouseClicked(evt);
            }
        });
        jPanel2.add(opgChild);
        opgChild.setBounds(15, 79, 149, 23);

        jTabbedPane1.addTab("Sister Concerns", jPanel2);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        jLabel1.setText("Main Account Codes :");
        jPanel4.add(jLabel1);
        jLabel1.setBounds(13, 20, 150, 15);

        txtMainAccountCode.setEnabled(false);
        jPanel4.add(txtMainAccountCode);
        txtMainAccountCode.setBounds(167, 18, 340, 19);

        jLabel39.setText("Note: Use comma to separate multiple main account codes. e.g.  125019,125033 ");
        jPanel4.add(jLabel39);
        jLabel39.setBounds(15, 46, 530, 15);

        jTabbedPane1.addTab("Finance Information", jPanel4);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setToolTipText("Supplier Approval");
        jPanel3.setLayout(null);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        jPanel3.add(jLabel31);
        jLabel31.setBounds(5, 13, 85, 15);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbHierarchy);
        cmbHierarchy.setBounds(99, 13, 270, 24);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        jPanel3.add(jLabel32);
        jLabel32.setBounds(5, 43, 85, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFrom);
        txtFrom.setBounds(99, 43, 270, 22);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        jPanel3.add(jLabel35);
        jLabel35.setBounds(5, 76, 85, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFromRemarks);
        txtFromRemarks.setBounds(99, 73, 518, 22);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        jPanel3.add(jLabel36);
        jLabel36.setBounds(5, 116, 85, 15);

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

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        jPanel3.add(jLabel33);
        jLabel33.setBounds(5, 226, 85, 15);

        cmbSendTo.setEnabled(false);
        jPanel3.add(cmbSendTo);
        cmbSendTo.setBounds(99, 223, 270, 24);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        jPanel3.add(jLabel34);
        jLabel34.setBounds(5, 262, 85, 15);

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

        jTabbedPane1.addTab("Approval", null, jPanel3, "Quotation Approval");

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(null);

        jLabel60.setText("Document Approval Status :");
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

        jLabel19.setText("Document Update History :");
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
        jTabbedPane1.setBounds(0, 70, 810, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void txtGSTINFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTINFocusLost
        if (txtGSTIN.getText().length() != 15) {
            JOptionPane.showMessageDialog(null, "GSTIN number must be 15 character");
            txtGSTIN.setText("");
            txtGSTIN.requestFocus();
            //return;            
        }
    //txtGSTIN.requestFocus();
        //if (txtGSTIN.getText().length() == 15) {
        //    txtGSTINDate.requestFocus();
        //}
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTINFocusLost

    private void cmbGSTINActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGSTINActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGSTINActionPerformed

    private void cmbPANItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPANItemStateChanged
        // TODO add your handling code here:
        String Selection = EITLERPGLOBAL.getCombostrCode(cmbPAN);

        if (Selection.trim().equals("N/A")) {
            txtPAN.setText("N/A");
            txtPAN.setEnabled(false);
            txtPANDate.setEnabled(false);
        }

        if (Selection.trim().equals("APPLIED")) {
            txtPAN.setText("APPLIED");
            txtPAN.setEnabled(false);
            txtPANDate.setEnabled(false);

        }

        if (Selection.trim().equals("Enter")) {
            txtPAN.setText("");
            txtPAN.setText("");
            txtPAN.setEnabled(true);
            txtPANDate.setEnabled(true);
        }
    }//GEN-LAST:event_cmbPANItemStateChanged

    private void cmbGSTINItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbGSTINItemStateChanged
        String Selection = EITLERPGLOBAL.getCombostrCode(cmbGSTIN);

        if (Selection.trim().equals("N/A")) {
            txtGSTIN.setText("N/A");
            txtGSTINDate.setText("");
            txtGSTIN.setEnabled(false);
            txtGSTINDate.setEnabled(false);
        }

        if (Selection.trim().equals("APPLIED")) {
            txtGSTIN.setText("APPLIED");
            txtGSTINDate.setText("");
            txtGSTIN.setEnabled(false);
            txtGSTINDate.setEnabled(false);

        }

        if (Selection.trim().equals("Enter")) {
            txtGSTIN.setText("");
            txtGSTIN.setText("");
            txtGSTIN.setEnabled(true);
            txtGSTINDate.setEnabled(true);
        }
    }//GEN-LAST:event_cmbGSTINItemStateChanged

    private void txtMobileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMobileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMobileActionPerformed

    private void txtSuppCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusLost
        // TODO add your handling code here:
        /*
         //komal ***
         int len = txtSuppCode.getText().length();
         if (len == 6) {
         if (! EITLERPGLOBAL.IsNumber(txtSuppCode.getText())) {
         JOptionPane.showMessageDialog(null,"Supplier Code Must be Numeric");
         }
         }
         else
         {
         JOptionPane.showMessageDialog(null,"Please enter valid Supplier Code");
         }
         //*** komal
         */
    }//GEN-LAST:event_txtSuppCodeFocusLost

    private void opgChildMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opgChildMouseClicked
        // TODO add your handling code here:
        if (opgChild.isSelected()) {
            txtPSuppID.setEnabled(false);
            txtCSuppID.setEnabled(true);
            cmdAddC.setEnabled(true);
            cmdRemoveC.setEnabled(true);
        }
    }//GEN-LAST:event_opgChildMouseClicked

    private void opgParentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opgParentMouseClicked
        // TODO add your handling code here:
        if (opgParent.isSelected()) {
            txtPSuppID.setEnabled(true);
            txtCSuppID.setEnabled(false);
            cmdAddC.setEnabled(false);
            cmdRemoveC.setEnabled(false);
        }
    }//GEN-LAST:event_opgParentMouseClicked

    private void cmdRemoveCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveCActionPerformed
        // TODO add your handling code here:
        if (TableSC.getRowCount() > 0) {
            DataModelSC.removeRow(TableSC.getSelectedRow());
        }

    }//GEN-LAST:event_cmdRemoveCActionPerformed

    private void cmdAddCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddCActionPerformed
        // TODO add your handling code here:
        if (txtCSuppID.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter supplier code");
            return;
        }

        if (txtCSuppID.getText().trim().equals(txtSuppCode.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Supplier code should be different than current supplier code");
            return;
        }

        int SuppID = clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID, txtCSuppID.getText());

        try {
            //check the duplication
            for (int i = 0; i < TableSC.getRowCount(); i++) {
                String SuppCode = (String) TableSC.getValueAt(i, 1);
                if (SuppCode.trim().equals(txtCSuppID.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Supplier code already exist");
                    return;
                }

            }

            Object[] rowData = new Object[4];
            rowData[0] = Integer.toString(TableSC.getRowCount() + 1);
            rowData[1] = txtCSuppID.getText();
            rowData[2] = clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtCSuppID.getText());
            rowData[3] = Integer.toString(SuppID);
            DataModelSC.addRow(rowData);
        } catch (Exception e) {
                 e.printStackTrace();
        }


    }//GEN-LAST:event_cmdAddCActionPerformed

    private void txtCSuppIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCSuppIDFocusLost
        // TODO add your handling code here:
        if (!txtCSuppID.getText().trim().equals("")) {
            txtCSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtCSuppID.getText()));
        }
    }//GEN-LAST:event_txtCSuppIDFocusLost

    private void txtPSuppIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPSuppIDFocusLost
        // TODO add your handling code here:
        if (!txtPSuppID.getText().trim().equals("")) {
            txtPSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtPSuppID.getText()));
        }
    }//GEN-LAST:event_txtPSuppIDFocusLost

    private void txtCSuppIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCSuppIDKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND BLOCKED='N' AND APPROVED=1 AND ST35_REGISTERED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtCSuppID.setText(aList.ReturnVal);
                txtCSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
        //=========================================

    }//GEN-LAST:event_txtCSuppIDKeyPressed

    private void txtPSuppIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPSuppIDKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND BLOCKED='N' AND APPROVED=1 AND ST35_REGISTERED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtPSuppID.setText(aList.ReturnVal);
                txtPSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
        //=========================================
    }//GEN-LAST:event_txtPSuppIDKeyPressed

    private void cmbESIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbESIItemStateChanged
        // TODO add your handling code here:
        String Selection = EITLERPGLOBAL.getCombostrCode(cmbESI);

        if (Selection.trim().equals("N/A")) {
            txtESI.setText("N/A");
            txtESI.setEnabled(false);
            txtESIDate.setEnabled(false);
        }

        if (Selection.trim().equals("APPLIED")) {
            txtESI.setText("APPLIED");
            txtESI.setEnabled(false);
            txtESIDate.setEnabled(false);

        }

        if (Selection.trim().equals("Enter")) {
            txtESI.setText("");
            txtESI.setText("");
            txtESI.setEnabled(true);
            txtESIDate.setEnabled(true);
        }
    }//GEN-LAST:event_cmbESIItemStateChanged

    private void cmbSSIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSSIItemStateChanged
        // TODO add your handling code here:
        String Selection = EITLERPGLOBAL.getCombostrCode(cmbSSI);

        if (Selection.trim().equals("N/A")) {
            txtSSI.setText("N/A");
            txtSSIDate.setText("");
            txtSSI.setEnabled(false);
            txtSSIDate.setEnabled(false);
        }

        if (Selection.trim().equals("APPLIED")) {
            txtSSI.setText("APPLIED");
            txtSSIDate.setText("");
            txtSSI.setEnabled(false);
            txtSSIDate.setEnabled(false);

        }

        if (Selection.trim().equals("Enter")) {
            txtSSI.setText("");
            txtSSI.setText("");
            txtSSI.setEnabled(true);
            txtSSIDate.setEnabled(true);
        }

    }//GEN-LAST:event_cmbSSIItemStateChanged

    private void cmbECCItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbECCItemStateChanged
        // TODO add your handling code here:
        String Selection = EITLERPGLOBAL.getCombostrCode(cmbECC);

        if (Selection.trim().equals("N/A")) {
            txtECC.setText("N/A");
            txtECCDate.setText("");
            txtECC.setEnabled(false);
            txtECCDate.setEnabled(false);
        }

        if (Selection.trim().equals("APPLIED")) {
            txtECC.setText("APPLIED");
            txtECCDate.setText("");
            txtECC.setEnabled(false);
            txtECCDate.setEnabled(false);
        }

        if (Selection.trim().equals("Enter")) {
            txtECC.setText("");
            txtECC.setText("");
            txtECC.setEnabled(true);
            txtECCDate.setEnabled(true);
        }

    }//GEN-LAST:event_cmbECCItemStateChanged

    private void cmbGSTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbGSTItemStateChanged
        // TODO add your handling code here:
        String Selection = EITLERPGLOBAL.getCombostrCode(cmbGST);

        if (Selection.trim().equals("N/A")) {
            txtGST.setText("N/A");
            txtGSTDate.setText("");
            txtGST.setEnabled(false);
            txtGSTDate.setEnabled(false);
        }

        if (Selection.trim().equals("APPLIED")) {
            txtGST.setText("APPLIED");
            txtGSTDate.setText("");
            txtGST.setEnabled(false);
            txtGSTDate.setEnabled(false);

        }

        if (Selection.trim().equals("Enter")) {
            txtGST.setText("");
            txtGST.setText("");
            txtGST.setEnabled(true);
            txtGSTDate.setEnabled(true);
        }

    }//GEN-LAST:event_cmbGSTItemStateChanged

    private void cmbCSTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCSTItemStateChanged
        // TODO add your handling code here:
        String Selection = EITLERPGLOBAL.getCombostrCode(cmbCST);

        if (Selection.trim().equals("N/A")) {
            txtCST.setText("N/A");
            txtCSTDate.setText("");
            txtCST.setEnabled(false);
            txtCSTDate.setEnabled(false);
        }

        if (Selection.trim().equals("APPLIED")) {
            txtCST.setText("APPLIED");
            txtCSTDate.setText("");
            txtCST.setEnabled(false);
            txtCSTDate.setEnabled(false);

        }

        if (Selection.trim().equals("Enter")) {
            txtCST.setText("");
            txtCST.setText("");
            txtCST.setEnabled(true);
            txtCSTDate.setEnabled(true);
        }

    }//GEN-LAST:event_cmbCSTItemStateChanged

    private void cmbServiceTaxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbServiceTaxMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_cmbServiceTaxMouseClicked

    private void cmbServiceTaxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbServiceTaxItemStateChanged
        // TODO add your handling code here:

        String Selection = EITLERPGLOBAL.getCombostrCode(cmbServiceTax);

        if (Selection.trim().equals("N/A")) {
            txtSTNO.setText("N/A");
            txtSTDate.setText("");
            txtSTNO.setEnabled(false);
            txtSTDate.setEnabled(false);
        }

        if (Selection.trim().equals("APPLIED")) {
            txtSTNO.setText("APPLIED");
            txtSTDate.setText("");
            txtSTNO.setEnabled(false);
            txtSTDate.setEnabled(false);

        }

        if (Selection.trim().equals("Enter")) {
            txtSTNO.setText("");
            txtSTNO.setText("");
            txtSTNO.setEnabled(true);
            txtSTDate.setEnabled(true);
        }


    }//GEN-LAST:event_cmbServiceTaxItemStateChanged

    private void cmdRemoveTermActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveTermActionPerformed
        // TODO add your handling code here:
        if (TableP.getRowCount() > 0 && TableP.getSelectedRow() >= 0) {
            DataModelP.removeRow(TableP.getSelectedRow());
        }
    }//GEN-LAST:event_cmdRemoveTermActionPerformed

    private void cmdAddTermActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddTermActionPerformed
        // TODO add your handling code here:
        int PayTermCode = EITLERPGLOBAL.getComboCode(cmbPayment);

        if (PayTermCode == 2 || PayTermCode == 6 || PayTermCode == 7) {
            if (!EITLERPGLOBAL.IsNumber(txtPayDays.getText()) || txtPayDays.getText().equals("0") || txtPayDays.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Please Insert proper days.");
                return;
            }
        }

        String PayTerm = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "PAYMENT_CODE", PayTermCode);

        if (PayTermCode == 1) {
            PayTerm = "THROUGH " + txtBankName.getText() + " BANK";
        }

        if (PayTermCode == 6) {
            PayTerm = "CREDIT TERMS WITH " + txtPayDays.getText() + " DAYS";
        }

        if (PayTermCode == 7) {
            PayTerm = "CREDIT TERMS WITH " + txtPayDays.getText() + " DAYS, THROUGH BOMBAY OFFICE";
        }

        if (PayTermCode == 2) {
            PayTerm = "SIGHT DRAFT/HUNDI WITH " + txtPayDays.getText() + " DAYS";
        }

        Object[] rowData = new Object[4];
        rowData[0] = Integer.toString(TableP.getRowCount() + 1);
        rowData[1] = PayTerm;
        rowData[2] = txtPayDays.getText();
        rowData[3] = Integer.toString(PayTermCode);
        DataModelP.addRow(rowData);

    }//GEN-LAST:event_cmdAddTermActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        txtAuditRemarks.setText((String) cmbPayment.getModel().getSelectedItem());
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtAuditRemarks;
        bigEdit.ShowEdit();

    }//GEN-LAST:event_jButton2ActionPerformed

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
        int SuppID = clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());

        SetupApproval();

        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if (ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID, 37, Integer.toString(SuppID))) {
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

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(true);
        OpgHold.setSelected(false);

        GenerateRejectedUserCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit = new BigEdit();
        bigEdit.theText = txtItem;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbPaymentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPaymentItemStateChanged
        // TODO add your handling code here:

        //Hide All other controls
        lblBankID.setVisible(false);
        txtBankID.setVisible(false);
        txtBankName.setVisible(false);
        lblPaymentDays.setVisible(false);
        txtPayDays.setVisible(false);

        int PayCode = EITLERPGLOBAL.getComboCode(cmbPayment);

        if (PayCode == 1) //Through Bank
        {
            lblBankID.setVisible(true);
            txtBankID.setVisible(true);
            txtBankName.setVisible(true);
        }

        if (PayCode == 6 || PayCode == 2 || PayCode == 7) {
            lblPaymentDays.setVisible(true);
            txtPayDays.setVisible(true);
        }

    }//GEN-LAST:event_cmbPaymentItemStateChanged

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        // TODO add your handling code here:
        if (!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjSupplier.LoadData(EITLERPGLOBAL.gCompanyID);
        MoveFirst();        
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo = txtSuppCode.getText();
        int SuppID = (int) ObjSupplier.getAttribute("SUPP_ID").getVal();
        ObjSupplier.ShowHistory(EITLERPGLOBAL.gCompanyID, SuppID);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdOtherRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOtherRemoveActionPerformed
        // TODO add your handling code here:
        if (TableO.getRowCount() > 0) {
            DataModelO.removeRow(TableO.getSelectedRow());
        }

    }//GEN-LAST:event_cmdOtherRemoveActionPerformed

    private void cmdOtherAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOtherAddActionPerformed
        // TODO add your handling code here:
        Object[] rowData = new Object[3];
        rowData[0] = Integer.toString(TableO.getRowCount() + 1);
        rowData[1] = "";
        rowData[2] = "";
        DataModelO.addRow(rowData);

    }//GEN-LAST:event_cmdOtherAddActionPerformed

    private void cmdDeliveryRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeliveryRemoveActionPerformed
        // TODO add your handling code here:
        if (TableD.getRowCount() > 0) {
            DataModelD.removeRow(TableD.getSelectedRow());
        }

    }//GEN-LAST:event_cmdDeliveryRemoveActionPerformed

    private void cmdDeliveryAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeliveryAddActionPerformed
        // TODO add your handling code here:
        Object[] rowData = new Object[3];
        rowData[0] = Integer.toString(TableD.getRowCount() + 1);
        rowData[1] = "";
        rowData[2] = "";
        DataModelD.addRow(rowData);

    }//GEN-LAST:event_cmdDeliveryAddActionPerformed

    private void cmdBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBack2ActionPerformed

    private void cmdNext5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext5ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_cmdNext5ActionPerformed

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        // TODO add your handling code here:
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //GenerateFromCombo();

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
        ObjColumn.Close();
        ObjSupplier.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void txtBankIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBankIDKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
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

    private void cmdNext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdNext1KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmdNext1KeyPressed

    private void TableLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableLKeyPressed
        // TODO add your handling code here:
        //=========== Item List ===============
        if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("CATEGORY_ID")) {
            if (evt.getKeyCode() == 112) //F1 Key pressed
            {
                LOV aList = new LOV();

                aList.SQL = "SELECT CATEGORY_ID,CATEGORY_DESC FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  ORDER BY CATEGORY_ID";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;

                if (aList.ShowLOV()) {
                    TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(), DataModelL.getColFromVariable("CATEGORY_ID"));
                    TableL.setValueAt(clsItemCategory.getCategoryDesc((int) EITLERPGLOBAL.gCompanyID, Long.parseLong(aList.ReturnVal)), TableL.getSelectedRow(), DataModelL.getColFromVariable("CATEGORY_DESC"));
                }
            }
        }
        //=========================================
        if (evt.getKeyCode() == 155)//Insert Key Pressed
        {
            Object[] rowData = new Object[1];
            DataModelL.addRow(rowData);
            DataModelL.SetUserObject(TableL.getRowCount() - 1, new HashMap());
            TableL.changeSelection(TableL.getRowCount() - 1, 1, false, false);
        }

        if (evt.getKeyCode() == 127) //Delete key pressed
        {
            if (TableL.getRowCount() > 0) {
                DataModelL.removeRow(TableL.getSelectedRow());
            }
        }
    }//GEN-LAST:event_TableLKeyPressed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        Object[] RowData = new Object[1];
        DataModelL.addRow(RowData);
        TableL.changeSelection(TableL.getRowCount() - 1, 1, false, false);
        TableL.requestFocus();
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        if (TableL.getRowCount() > 0) {
            DataModelL.removeRow(TableL.getSelectedRow());
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void chkOneTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOneTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkOneTimeActionPerformed

    private void chkSlowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSlowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSlowActionPerformed

    private void txtPayDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPayDaysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPayDaysActionPerformed

    private void cmbStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStateActionPerformed
        String st1, dt1, dt2;
        st1 = "SELECT STATE_CODE FROM DINESHMILLS.D_SAL_STATE_MASTER WHERE STATE_NAME='" + cmbState.getSelectedItem().toString() + "'";
        txtStateCode.setText(data.getStringValueFromDB(st1));
        dt1 = "SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_STATE_MASTER WHERE STATE_NAME='" + cmbState.getSelectedItem().toString() + "'";
        txtStateGstCode.setText(data.getStringValueFromDB(dt1));
        dt2 = "SELECT STATE_NAME FROM DINESHMILLS.D_SAL_STATE_MASTER WHERE STATE_NAME='" + cmbState.getSelectedItem().toString() + "'";
        txtStateName.setText(data.getStringValueFromDB(dt2));
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStateActionPerformed

    private void cmbCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCountryActionPerformed
        String st2;
        st2 = "SELECT COUNTRY_NAME FROM DINESHMILLS.D_SAL_COUNTRY_MASTER WHERE COUNTRY_NAME='" + cmbCountry.getSelectedItem().toString() + "'";
        txtCountryName.setText(data.getStringValueFromDB(st2));  // TODO add your handling code here:
    }//GEN-LAST:event_cmbCountryActionPerformed

    private void txtPANFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPANFocusLost
        txtPANDate.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_txtPANFocusLost

    private void txtMSMEUANFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMSMEUANFocusLost
    if(txtMSMEUAN.getText().length()>0){
        if(txtMSMEUAN.getText().length() !=12)
        {
            JOptionPane.showMessageDialog(null, "MSME UAN number must be 12 character");
            txtMSMEUAN.requestFocus();
    }else{  
            txtMSMEDIC.setText("N/A");
            txtMSMEDIC.setEnabled(false);
        }        
        }else{
            if(!txtMSMEDIC.isEnabled() && txtMSMEDIC.getText().equals("N/A")){
                txtMSMEDIC.setText("");
            txtMSMEDIC.setEnabled(true);
            txtMSMEDIC.requestFocus();
            }
        }
    }//GEN-LAST:event_txtMSMEUANFocusLost

    private void cmbMSMEUANItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMSMEUANItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMSMEUANItemStateChanged

    private void chkMSMEItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkMSMEItemStateChanged
if(evt.getStateChange() == ItemEvent.SELECTED){
                        txtMSMEUAN.setEnabled(true);
                        txtMSMEDIC.setEnabled(true); 
                        txtMSMEUAN.setText("");
                        txtMSMEDIC.setText("");
                    }
                    else if(evt.getStateChange() == ItemEvent.DESELECTED){
                        txtMSMEUAN.setEnabled(false);
                        txtMSMEDIC.setEnabled(false);
                        txtMSMEUAN.setText("N/A");
                        txtMSMEDIC.setText("N/A");
                        //textField.setText("Disabled");
                    }
    }//GEN-LAST:event_chkMSMEItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab3;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableD;
    private javax.swing.JTable TableHS;
    private javax.swing.JTable TableL;
    private javax.swing.JTable TableO;
    private javax.swing.JTable TableP;
    private javax.swing.JTable TableSC;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkBlocked;
    private javax.swing.JCheckBox chkMSME;
    private javax.swing.JCheckBox chkOneTime;
    private javax.swing.JCheckBox chkReg;
    private javax.swing.JCheckBox chkSSI;
    private javax.swing.JCheckBox chkSlow;
    private javax.swing.JComboBox cmbCST;
    private javax.swing.JComboBox cmbCountry;
    private javax.swing.JComboBox cmbECC;
    private javax.swing.JComboBox cmbESI;
    private javax.swing.JComboBox cmbGST;
    private javax.swing.JComboBox cmbGSTIN;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbMSMEUAN;
    private javax.swing.JComboBox cmbPAN;
    private javax.swing.JComboBox cmbPayment;
    private javax.swing.JComboBox cmbSSI;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JComboBox cmbServiceTax;
    private javax.swing.JComboBox cmbState;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdAddC;
    private javax.swing.JButton cmdAddTerm;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBack2;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdDeliveryAdd;
    private javax.swing.JButton cmdDeliveryRemove;
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
    private javax.swing.JButton cmdNext5;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdOtherAdd;
    private javax.swing.JButton cmdOtherRemove;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdRemoveC;
    private javax.swing.JButton cmdRemoveTerm;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.ButtonGroup grpSisterConcern;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAmendNo;
    private javax.swing.JLabel lblBankID;
    private javax.swing.JLabel lblPaymentDays;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JRadioButton opgChild;
    private javax.swing.JRadioButton opgParent;
    private javax.swing.JTextField txtAdd1;
    private javax.swing.JTextField txtAdd2;
    private javax.swing.JTextField txtAdd3;
    private javax.swing.JTextField txtAttn;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtBankID;
    private javax.swing.JTextField txtBankName;
    private javax.swing.JTextField txtCP1;
    private javax.swing.JTextField txtCP2;
    private javax.swing.JTextField txtCST;
    private javax.swing.JTextField txtCSTDate;
    private javax.swing.JTextField txtCSuppID;
    private javax.swing.JTextField txtCSuppName;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCountryName;
    private javax.swing.JTextField txtDistanceKm;
    private javax.swing.JTextField txtDummyCode;
    private javax.swing.JTextField txtECC;
    private javax.swing.JTextField txtECCDate;
    private javax.swing.JTextField txtESI;
    private javax.swing.JTextField txtESIDate;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFax;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGST;
    private javax.swing.JTextField txtGSTDate;
    private javax.swing.JTextField txtGSTIN;
    private javax.swing.JTextField txtGSTINDate;
    private javax.swing.JTextField txtItem;
    private javax.swing.JTextField txtMSMEDIC;
    private javax.swing.JTextField txtMSMEUAN;
    private javax.swing.JTextField txtMainAccountCode;
    private javax.swing.JTextField txtMobile;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPAN;
    private javax.swing.JTextField txtPANDate;
    private javax.swing.JTextField txtPIN;
    private javax.swing.JTextField txtPSuppID;
    private javax.swing.JTextField txtPSuppName;
    private javax.swing.JTextField txtPayDays;
    private javax.swing.JTextField txtPhone_O;
    private javax.swing.JTextField txtPhone_R;
    private javax.swing.JTextField txtPlaceofsupply;
    private javax.swing.JTextField txtRegFrom;
    private javax.swing.JTextField txtRegTo;
    private javax.swing.JTextField txtSSI;
    private javax.swing.JTextField txtSSIDate;
    private javax.swing.JTextField txtSTDate;
    private javax.swing.JTextField txtSTNO;
    private javax.swing.JTextField txtStateCode;
    private javax.swing.JTextField txtStateGstCode;
    private javax.swing.JTextField txtStateName;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtURL;
    private javax.swing.JTextField txtWebsite;
    // End of variables declaration//GEN-END:variables

    private void Add() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        EditMode = EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SetupApproval();
        lblTitle.setBackground(Color.BLUE);
    }

    private void SetupApproval() {

        /*if(cmbHierarchy.getItemCount()>1) {
         cmbHierarchy.setEnabled(true);
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

            int FromUserID = ApprovalFlow.getFromID((int) EITLERPGLOBAL.gCompanyID, 37, Integer.toString(ObjSupplier.getAttribute("SUPP_ID").getInt()));
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = ApprovalFlow.getFromRemarks((int) EITLERPGLOBAL.gCompanyID, 37, FromUserID, (String) ObjSupplier.getAttribute("SUPP_ID").getObj());

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

                String DocNo = Integer.toString((int) ObjSupplier.getAttribute("SUPP_ID").getVal());

                List = ApprovalFlow.getRemainingUsers((int) EITLERPGLOBAL.gCompanyID, 37, DocNo);
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
            e.printStackTrace();
        }

    }

    private void SetFields(boolean pStat) {
        txtSuppCode.setEnabled(pStat);
        //txtDummyCode.setEnabled(pStat);
        txtName.setEnabled(pStat);
        txtAttn.setEnabled(pStat);
        txtAdd1.setEnabled(pStat);
        txtAdd2.setEnabled(pStat);
        txtAdd3.setEnabled(pStat);
        txtCity.setEnabled(pStat);
        txtPIN.setEnabled(pStat);
        cmbState.setEnabled(pStat);
        cmbCountry.setEnabled(pStat);
        txtStateName.setEnabled(pStat);
        txtCountryName.setEnabled(pStat);
        txtPhone_O.setEnabled(pStat);
        txtPhone_R.setEnabled(pStat);
        txtFax.setEnabled(pStat);
        txtMobile.setEnabled(pStat);
        txtEmail.setEnabled(pStat);
        txtWebsite.setEnabled(pStat);
        txtURL.setEnabled(pStat);
        txtSTNO.setEnabled(pStat);
        txtSTDate.setEnabled(pStat);
        txtCST.setEnabled(pStat);
        txtCSTDate.setEnabled(pStat);
        txtGST.setEnabled(pStat);
        txtGSTDate.setEnabled(pStat);
        txtGSTIN.setEnabled(pStat);
        txtGSTINDate.setEnabled(pStat);
        chkMSME.setEnabled(pStat); 
        //txtMSMEUAN.setEnabled(pStat);
        //txtMSMEDIC.setEnabled(pStat); 
        txtECC.setEnabled(pStat);
        txtECCDate.setEnabled(pStat);
        txtSSI.setEnabled(pStat);
        txtSSIDate.setEnabled(pStat);
        txtESI.setEnabled(pStat);
        txtESIDate.setEnabled(pStat);
        txtCP1.setEnabled(pStat);
        txtCP2.setEnabled(pStat);
        txtBankID.setEnabled(pStat);
        txtPayDays.setEnabled(pStat);
        //txtLastTran.setEnabled(pStat);
        txtRegFrom.setEnabled(pStat);
        txtRegTo.setEnabled(pStat);
        //txtPO.setEnabled(pStat);
        txtMainAccountCode.setEnabled(pStat);

        txtDistanceKm.setEnabled(pStat);

        txtPlaceofsupply.setEnabled(pStat);
        chkOneTime.setEnabled(pStat);
        chkBlocked.setEnabled(pStat);
        chkSlow.setEnabled(pStat);
        chkReg.setEnabled(pStat);
        chkSSI.setEnabled(pStat);

        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);

        cmdDeliveryAdd.setEnabled(pStat);
        cmdDeliveryRemove.setEnabled(pStat);
        cmdOtherAdd.setEnabled(pStat);
        cmdOtherRemove.setEnabled(pStat);

        cmdAdd.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);

        cmbPayment.setEnabled(pStat);
        txtBankID.setEnabled(pStat);
        txtPayDays.setEnabled(pStat);
        txtItem.setEnabled(pStat);

        cmdAddTerm.setEnabled(pStat);
        cmdRemoveTerm.setEnabled(pStat);

        cmbServiceTax.setEnabled(pStat);
        cmbCST.setEnabled(pStat);
        cmbGST.setEnabled(pStat);
        cmbECC.setEnabled(pStat);
        cmbSSI.setEnabled(pStat);
        cmbESI.setEnabled(pStat);
        cmbPAN.setEnabled(pStat);
        cmbMSMEUAN.setEnabled(pStat);
        cmbGSTIN.setEnabled(pStat);

        SetupApproval();

        //DataModelH.TableReadOnly(!pStat);
        DataModelL.TableReadOnly(!pStat);
        //DataModelP.TableReadOnly(!pStat);
        DataModelD.TableReadOnly(!pStat);
        DataModelO.TableReadOnly(!pStat);

        opgParent.setEnabled(pStat);
        opgChild.setEnabled(pStat);
        cmdAddC.setEnabled(pStat);
        cmdRemoveC.setEnabled(pStat);

        // ============ Service Tax ===================//
        if (txtSTNO.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbServiceTax, "APPLIED");
            txtSTDate.setText("");
            txtSTNO.setEnabled(false);
            txtSTDate.setEnabled(false);
        } else {
            if (txtSTNO.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbServiceTax, "N/A");
                txtSTDate.setText("");
                txtSTNO.setEnabled(false);
                txtSTDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbServiceTax, "Enter");
                txtSTNO.setEnabled(true);
                txtSTDate.setEnabled(true);
            }
        }
        //===========================================//

        // ============ CST ===================//
        if (txtCST.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbCST, "APPLIED");
            txtCSTDate.setText("");
            txtCST.setEnabled(false);
            txtCSTDate.setEnabled(false);
        } else {
            if (txtCST.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbCST, "N/A");
                txtCSTDate.setText("");
                txtCST.setEnabled(false);
                txtCSTDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbCST, "Enter");
                txtCST.setEnabled(true);
                txtCSTDate.setEnabled(true);
            }
        }
        //===========================================//

        // ============ GST ===================//
        if (txtGST.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbGST, "APPLIED");
            txtGSTDate.setText("");
            txtGST.setEnabled(false);
            txtGSTDate.setEnabled(false);
        } else {
            if (txtGST.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbGST, "N/A");
                txtGSTDate.setText("");
                txtGST.setEnabled(false);
                txtGSTDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbGST, "Enter");
                txtGST.setEnabled(true);
                txtGSTDate.setEnabled(true);
            }
        }
        //===========================================//

        // ============ GSTIN ===================//
        if (txtGSTIN.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbGSTIN, "APPLIED");
            txtGSTINDate.setText("");
            txtGSTIN.setEnabled(false);
            txtGSTINDate.setEnabled(false);
        } else {
            if (txtGSTIN.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbGSTIN, "N/A");
                txtGSTINDate.setText("");
                txtGSTIN.setEnabled(false);
                txtGSTINDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbGSTIN, "Enter");
                txtGSTIN.setEnabled(true);
                txtGSTINDate.setEnabled(true);
            }
        }
        //===========================================//

        // ============ ECC ===================//
        if (txtECC.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbECC, "APPLIED");
            txtECCDate.setText("");
            txtECC.setEnabled(false);
            txtECCDate.setEnabled(false);
        } else {
            if (txtECC.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbECC, "N/A");
                txtECCDate.setText("");
                txtECC.setEnabled(false);
                txtECCDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbECC, "Enter");
                txtECC.setEnabled(true);
                txtECCDate.setEnabled(true);
            }
        }
        //===========================================//

        // ============ SSI ===================//
        if (txtSSI.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbSSI, "APPLIED");
            txtSSIDate.setText("");
            txtSSI.setEnabled(false);
            txtSSIDate.setEnabled(false);
        } else {
            if (txtSSI.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbSSI, "N/A");
                txtSSIDate.setText("");
                txtSSI.setEnabled(false);
                txtSSIDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbSSI, "Enter");
                txtSSI.setEnabled(true);
                txtSSIDate.setEnabled(true);
            }
        }
        //===========================================//

        // ============ ESI ===================//
        if (txtESI.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbESI, "APPLIED");
            txtESIDate.setText("");
            txtESI.setEnabled(false);
            txtESIDate.setEnabled(false);
        } else {
            if (txtESI.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbESI, "N/A");
                txtESIDate.setText("");
                txtESI.setEnabled(false);
                txtESIDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbESI, "Enter");
                txtESI.setEnabled(true);
                txtESIDate.setEnabled(true);
            }
        }
        //===========================================//

        // ============ PAN ===================//
        if (txtPAN.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbPAN, "APPLIED");
            txtPANDate.setText("");
            txtPAN.setEnabled(false);
            txtPANDate.setEnabled(false);
        } else {
            if (txtPAN.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbPAN, "N/A");
                txtPANDate.setText("");
                txtPAN.setEnabled(false);
                txtPANDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbPAN, "Enter");
                txtPAN.setEnabled(true);
                txtPANDate.setEnabled(true);
            }
        }
        //===========================================//

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

        //Search in Table
        int ItemCol = DataModelL.getColFromVariable("CATEGORY_ID");

        if (cmbPayment.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Select atleast one term.");
            return false;
        }

        if (opgParent.isSelected()) {
            //Validate supplier
            if (!clsSupplier.IsValidSuppCode(EITLERPGLOBAL.gCompanyID, txtPSuppID.getText())) {
                JOptionPane.showMessageDialog(null, "Parent supplier code is not valid. Please check whether it is not cancelled/blocked/registered ");
                return false;
            }
        }

        //Now Header level validations
        if (clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID) == 10) {
            if (txtSuppCode.getText().trim().equals("")) { //&&OpgFinal.isSelected()
                JOptionPane.showMessageDialog(null, "Please enter Supplier Code");
                txtSuppCode.requestFocus(true);
                return false;
            }
        }

        if (!txtSuppCode.getText().trim().equals("")) {
            if (EditMode == EITLERPGLOBAL.ADD) {

                if (clsSupplier.IsDuplicateSuppCode(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim(), false)) {
                    JOptionPane.showMessageDialog(null, "Supplier Code already exists!!");
                    txtSuppCode.requestFocus(true);
                    return false;
                }

            } else {
                int CurrentSuppID = (int) ObjSupplier.getAttribute("SUPP_ID").getVal();
                if (clsSupplier.IsDuplicateSuppCode(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText().trim(), false, CurrentSuppID)) {
                    JOptionPane.showMessageDialog(null, "Supplier Code already exists!!");
                    txtSuppCode.requestFocus(true);
                    return false;
                }

            }
        }

        if (txtGST.getText().trim().equals("") && txtCST.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please either enter GST or CST No. and date");
            return false;
        }

        if (txtGSTIN.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter GSTIN No. and date");
            return false;
        }

        if (txtName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Supplier Name");
            return false;
        }

        if (txtAdd1.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Address Line 1");
            return false;
        }

        if (txtCity.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter City");
            return false;
        }

        if (!txtSTDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtSTDate.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid Service Tax Date");
                return false;
            }
        }

        if (chkSSI.isSelected() && txtSSI.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter SSI Registration no.");
            return false;
        }

        if (chkSSI.isSelected() && txtSSIDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter SSI Registration date");
            return false;
        }

        if (chkSSI.isSelected() && !txtSSIDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtSSIDate.getText())) {

                JOptionPane.showMessageDialog(null, "Invalid SSI Registration date");
                return false;

            }
        }

        if (txtCST.getText().trim().equals("")) {
            //JOptionPane.showMessageDialog(null,"Please enter CST No");
            //return false;
        }

        if (!txtCSTDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtCSTDate.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid CST Date");
                return false;
            }
        }

        if (!OpgReject.isSelected()) {
            if (txtPAN.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please PAN No. and date");
                return false;
            }

            if (!txtPANDate.getText().trim().equals("")) {
                if (!EITLERPGLOBAL.isDate(txtPANDate.getText())) {
                    JOptionPane.showMessageDialog(null, "Invalid PAN Date");
                    return false;
                }
            }
            /*else {
             JOptionPane.showMessageDialog(null,"Invalid PAN Date");
             return false;
             }*/
        }

        if (!txtGSTDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtGSTDate.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid GST Date");
                return false;
            }
        }
        if (!txtGSTINDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtGSTINDate.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid GSTIN Date");
                return false;
            }
        }
        if (!txtECCDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtECCDate.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid ECC Date");
                return false;
            }
        }
        if (!txtSSIDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtSSIDate.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid SSI Date");
                return false;
            }
        }
        if (!txtESIDate.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtESIDate.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid ESI Date");
                return false;
            }
        }
        if (!txtRegFrom.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtRegFrom.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid Registration From Date");
                return false;
            }
        }
        if (!txtRegTo.getText().trim().equals("")) {
            if (!EITLERPGLOBAL.isDate(txtRegTo.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid Registration To Date");
                return false;
            }
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

//        if(EITLERPGLOBAL.getComboCode(cmbState)==0){
//            JOptionPane.showMessageDialog(this,"Select State Name","ERROR",JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//        
//        // country name selection validation
//        if(EITLERPGLOBAL.getComboCode(cmbCountry)==0){
//            JOptionPane.showMessageDialog(this,"Select Country Name","ERROR",JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
        //if (EITLERPGLOBAL.gUserID != 11) {
        if (clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) == 16) {
            //Validate main account codes
            if (!OpgReject.isSelected()) {
                if (!txtMainAccountCode.getText().trim().equals("")) {
                    String MainCodes[] = txtMainAccountCode.getText().split(",");

                    for (int i = 0; i < MainCodes.length; i++) {
                        String MainCode = MainCodes[i];

                        if (!clsAccount.IsValidAccount(MainCode, "")) {
                            JOptionPane.showMessageDialog(null, "Main Account code " + MainCode + " is not valid. Please verify");
                            return false;
                        }
                    }
                }
            }
        }

        //        //komal ***
        //        if (OpgFinal.isSelected()) {
        //            if (txtMainAccountCode.getText().trim().equals("")) {
        //                JOptionPane.showMessageDialog(null,"Main Account code is not entered by Account.");
        //                return false;
        //            }
        //        }
        //        //*** komal
        return true;
    }

    private void ClearFields() {
        txtSuppCode.setText("");
        txtDummyCode.setText("");
        txtName.setText("");
        txtAttn.setText("");
        txtAdd1.setText("");
        txtAdd2.setText("");
        txtAdd3.setText("");
        txtCity.setText("");
        txtPIN.setText("");
//        txtState.setText("");
//        txtCountry.setText("");
        cmbState.setSelectedIndex(0);
        cmbCountry.setSelectedIndex(0);
        txtPhone_O.setText("");
        txtPhone_R.setText("");
        txtFax.setText("");
        txtMobile.setText("");
        txtEmail.setText("");
        txtWebsite.setText("");
        txtURL.setText("");
        txtSTNO.setText("");
        txtSTDate.setText("");
        txtCST.setText("");
        txtCSTDate.setText("");
        txtGST.setText("");
        txtGSTDate.setText("");
        txtGSTIN.setText("");
        txtGSTINDate.setText("");
        txtECC.setText("");
        txtECCDate.setText("");
        txtSSI.setText("");
        txtSSIDate.setText("");
        txtESI.setText("");
        txtESIDate.setText("");
        txtCP1.setText("");
        txtCP2.setText("");
        txtBankID.setText("");
        txtBankName.setText("");
        txtPayDays.setText("");
        txtRegFrom.setText("");
        txtRegTo.setText("");
        txtToRemarks.setText("");
        txtItem.setText("");
        txtBankID.setText("0");
        txtBankName.setText("");
        txtPayDays.setText("0");

        txtDistanceKm.setText("");
        chkMSME.setSelected(false);
        txtMSMEUAN.setText("N/A"); 
        txtMSMEDIC.setText("N/A");

        txtPSuppID.setText("");
        txtPSuppName.setText("");
        txtCSuppID.setText("");
        txtCSuppName.setText("");
        txtMainAccountCode.setText("");
        txtPlaceofsupply.setText("");
        chkOneTime.setSelected(false);
        chkBlocked.setSelected(false);
        chkSlow.setSelected(false);
        chkReg.setSelected(false);
        chkSSI.setSelected(false);
        cmbPayment.setSelectedIndex(0);
        /*OpgApprove.setSelected(false);
         OpgFinal.setSelected(false);
         OpgReject.setSelected(false);;
         OpgHold.setSelected(true);
         txtToRemarks.setText("");*/

        FormatGrid();
        FormatTermsGrid();
        FormatGridP();
        FormatGridA();
        FormatGridHS();
        FormatGridSC();
    }

    private void Edit() {
        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        String sSuppCode = Long.toString((long) ObjSupplier.getAttribute("SUPP_ID").getVal());
        if (ObjSupplier.IsEditable(EITLERPGLOBAL.gCompanyID, sSuppCode, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (ApprovalFlow.IsCreator(37, sSuppCode) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 162)) {
                SetFields(true);
            } else {
                EnableApproval();
            }

            DisplayData();
            DisableToolbar();
            if (EITLERPGLOBAL.gNewUserID == 11) {
                OpgFinal.setEnabled(true);
            }
            if (EITLERPGLOBAL.gNewUserID == 124) {
                txtSuppCode.setEnabled(true);
            }
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
        String sSuppCode = Long.toString((long) ObjSupplier.getAttribute("SUPP_ID").getVal());

        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record ?", "SDML ERP", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (ObjSupplier.CanDelete(EITLERPGLOBAL.gCompanyID, sSuppCode, EITLERPGLOBAL.gNewUserID)) {
                if (ObjSupplier.Delete()) {
                    ObjSupplier.LoadData(EITLERPGLOBAL.gCompanyID);
                    MoveLast();
                } else {
                    JOptionPane.showMessageDialog(null, "Error occured while deleting. Error is " + ObjSupplier.LastError);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You cannot delete this record. \n It is either approved/rejected record or waiting approval for other user or is referred in other documents");
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
            if (ObjSupplier.Insert()) {
                // MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjSupplier.LastError);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjSupplier.Update()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjSupplier.LastError);
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
            e.printStackTrace();
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
        Loader ObjLoader = new Loader(this, "EITLERP.frmSupplierFind", true);
        frmSupplierFind ObjReturn = (frmSupplierFind) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            if (!ObjSupplier.Filter(ObjReturn.strQuery)) {
                JOptionPane.showMessageDialog(null, "No records found.");
            }
            MoveFirst();
        }
    }

    private void MoveFirst() {
        ObjSupplier.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjSupplier.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjSupplier.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjSupplier.MoveLast();
        DisplayData();
    }

    //Didplay data on the Screen
    private void DisplayData() {

        //=========== Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (ObjSupplier.getAttribute("APPROVED").getInt() == 1) {

                    lblTitle.setBackground(Color.BLUE);
                }

                if (ObjSupplier.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                }

                if (ObjSupplier.getAttribute("CANCELLED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
        } catch (Exception c) {
            c.printStackTrace();
        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = 37;

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

            txtSuppCode.setText((String) ObjSupplier.getAttribute("SUPPLIER_CODE").getObj());

            int AmendNo = 0;
            AmendNo = clsSupplierAmend.getAmendmentNo(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());
            if (AmendNo > 0) {
                lblAmendNo.setVisible(true);
                lblAmendNo.setText("Amendment No. " + AmendNo);
            } else {
                lblAmendNo.setVisible(false);
            }

            lblRevNo.setText(Integer.toString((int) ObjSupplier.getAttribute("REVISION_NO").getVal()));
            txtDummyCode.setText((String) ObjSupplier.getAttribute("DUMMY_SUPPLIER_CODE").getObj());
            txtName.setText((String) ObjSupplier.getAttribute("SUPP_NAME").getObj());
            //txtPODate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplier.getAttribute("PO_DATE").getObj()));
            txtAttn.setText((String) ObjSupplier.getAttribute("ATTN").getObj());
            txtAdd1.setText((String) ObjSupplier.getAttribute("ADD1").getObj());
            txtAdd2.setText((String) ObjSupplier.getAttribute("ADD2").getObj());
            txtAdd3.setText((String) ObjSupplier.getAttribute("ADD3").getObj());
            txtCity.setText((String) ObjSupplier.getAttribute("CITY").getObj());
            txtPIN.setText((String) ObjSupplier.getAttribute("PINCODE").getObj());
            //EITLERPGLOBAL.setComboIndex(cmbState, ObjSupplier.getAttribute("STATE").getInt());
            txtStateName.setText((String) ObjSupplier.getAttribute("STATE").getObj());
            txtStateCode.setText((String) ObjSupplier.getAttribute("STATE_CODE").getObj());
            txtStateGstCode.setText((String) ObjSupplier.getAttribute("STATE_GST_CODE").getObj());
            //txtCountry.setText((String)ObjSupplier.getAttribute("COUNTRY").getObj());
            //EITLERPGLOBAL.setComboIndex(cmbCountry,  ObjSupplier.getAttribute("COUNTRY").getInt());
            txtCountryName.setText((String) ObjSupplier.getAttribute("COUNTRY").getObj());
            // txtState.setText((String)ObjSupplier.getAttribute("STATE").getObj());
            // txtCountry.setText((String)ObjSupplier.getAttribute("COUNTRY").getObj());
            txtPlaceofsupply.setText((String) ObjSupplier.getAttribute("PLACE_OF_SUPPLY").getObj());
            txtPhone_O.setText((String) ObjSupplier.getAttribute("PHONE_O").getObj());
            txtPhone_R.setText((String) ObjSupplier.getAttribute("PHONE_RES").getObj());
            txtFax.setText((String) ObjSupplier.getAttribute("FAX_NO").getObj());
            txtMobile.setText((String) ObjSupplier.getAttribute("MOBILE_NO").getObj());
            txtEmail.setText((String) ObjSupplier.getAttribute("EMAIL_ADD").getObj());
            txtWebsite.setText((String) ObjSupplier.getAttribute("WEB_SITE").getObj());
            txtURL.setText((String) ObjSupplier.getAttribute("URL").getObj());
            txtSTNO.setText((String) ObjSupplier.getAttribute("SERVICETAX_NO").getObj());
            txtSTDate.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("SERVICETAX_DATE").getObj()));
            txtCST.setText((String) ObjSupplier.getAttribute("CST_NO").getObj());
            txtCSTDate.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("CST_DATE").getObj()));
            txtGST.setText((String) ObjSupplier.getAttribute("GST_NO").getObj());
            txtGSTDate.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("GST_DATE").getObj()));
            txtGSTIN.setText((String) ObjSupplier.getAttribute("GSTIN_NO").getObj());
            txtGSTINDate.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("GSTIN_DATE").getObj()));
            txtECC.setText((String) ObjSupplier.getAttribute("ECC_NO").getObj());
            txtECCDate.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("ECC_DATE").getObj()));
            txtSSI.setText((String) ObjSupplier.getAttribute("SSIREG_NO").getObj());
            txtSSIDate.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("SSIREG_DATE").getObj()));
            txtESI.setText((String) ObjSupplier.getAttribute("ESIREG_NO").getObj());
            txtESIDate.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("ESIREG_DATE").getObj()));

            txtPAN.setText(ObjSupplier.getAttribute("PAN_NO").getString());
            txtPANDate.setText(EITLERPGLOBAL.formatDate(ObjSupplier.getAttribute("PAN_DATE").getString()));

           chkMSME.setSelected(ObjSupplier.getAttribute("MSME").getBool());
            txtMSMEUAN.setText(ObjSupplier.getAttribute("MSME_UAN").getString());
            txtMSMEDIC.setText(ObjSupplier.getAttribute("MSME_DIC_NO").getString());

            txtCP1.setText((String) ObjSupplier.getAttribute("CONTACT_PERSON_1").getObj());
            txtCP2.setText((String) ObjSupplier.getAttribute("CONTACT_PERSON_2").getObj());
            txtRegFrom.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("FROM_DATE_REG").getObj()));
            txtRegTo.setText(EITLERPGLOBAL.formatDate((String) ObjSupplier.getAttribute("TO_DATE_REG").getObj()));
            txtPayDays.setText(Long.toString((long) ObjSupplier.getAttribute("PAYMENT_DAYS").getVal()));
            txtBankID.setText(Long.toString((long) ObjSupplier.getAttribute("BANK_ID").getVal()));
            txtBankName.setText((String) ObjSupplier.getAttribute("BANK_NAME").getObj());
            txtMainAccountCode.setText(ObjSupplier.getAttribute("MAIN_ACCOUNT_CODE").getString());

            EITLERPGLOBAL.setComboIndex(cmbPayment, (int) ObjSupplier.getAttribute("PAYMENT_TERM_CODE").getVal());
            txtItem.setText((String) ObjSupplier.getAttribute("PROPOSED_ITEM").getObj());

            txtDistanceKm.setText(Integer.toString(ObjSupplier.getAttribute("DISTANCE_KM").getInt()));

            lblBankID.setVisible(false);
            txtBankID.setVisible(false);
            txtBankName.setVisible(false);
            lblPaymentDays.setVisible(false);
            txtPayDays.setVisible(false);

            chkOneTime.setSelected(ObjSupplier.getAttribute("ONETIME_SUPPLIER").getBool());
            if (((String) ObjSupplier.getAttribute("BLOCKED").getObj()).equals("Y")) {
                bState = true;
            }
            chkBlocked.setSelected(bState);
            chkSlow.setSelected(ObjSupplier.getAttribute("SLOW_MOVING").getBool());
            chkReg.setSelected(ObjSupplier.getAttribute("ST35_REGISTERED").getBool());
            chkSSI.setSelected(ObjSupplier.getAttribute("SSIREG").getBool());

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) ObjSupplier.getAttribute("HIERARCHY_ID").getVal());

            //========= Display Line Items =============//
            FormatGrid();

            for (int i = 1; i <= ObjSupplier.colSupplierItems.size(); i++) {
                //Insert New Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);
                int NewRow = TableL.getRowCount() - 1;

                clsSupplierItem ObjItem = (clsSupplierItem) ObjSupplier.colSupplierItems.get(Integer.toString(i));

                TableL.setValueAt(Long.toString((long) ObjItem.getAttribute("CATEGORY_ID").getVal()), NewRow, DataModelL.getColFromVariable("CATEGORY_ID"));
                String CategoryDesc = clsItemCategory.getCategoryDesc((int) EITLERPGLOBAL.gCompanyID, (long) ObjItem.getAttribute("CATEGORY_ID").getVal());
                DataModelL.setValueByVariable("CATEGORY_DESC", CategoryDesc, NewRow);

            }

            //======================Displaying Child Suppliers =================//
            FormatGridSC();
            int ParentID = (int) ObjSupplier.getAttribute("PARENT_SUPP_ID").getVal();
            String cSupplierCode = "";
            int cSuppID = 0;

            if (ParentID != 0) {
                opgParent.setSelected(true);
                opgChild.setSelected(false);
                cmdAddC.setEnabled(false);
                cmdRemoveC.setEnabled(false);

                cSupplierCode = clsSupplier.getSupplierCode(EITLERPGLOBAL.gCompanyID, ParentID);
                txtPSuppID.setText(cSupplierCode);
                txtPSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, cSupplierCode));
            } else {
                opgParent.setSelected(false);
                opgChild.setSelected(true);
                cmdAddC.setEnabled(true);
                cmdRemoveC.setEnabled(true);

                txtPSuppID.setText("");
                txtPSuppName.setText("");

                for (int i = 1; i <= ObjSupplier.colSuppChilds.size(); i++) {
                    clsSuppChilds objChild = (clsSuppChilds) ObjSupplier.colSuppChilds.get(Integer.toString(i));
                    cSuppID = (int) objChild.getAttribute("CHILD_SUPP_ID").getVal();
                    cSupplierCode = clsSupplier.getSupplierCode(EITLERPGLOBAL.gCompanyID, cSuppID);

                    Object[] rowData = new Object[4];
                    rowData[0] = Integer.toString(i);
                    rowData[1] = cSupplierCode;
                    rowData[2] = clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, cSupplierCode);
                    rowData[3] = Integer.toString(cSuppID);

                    DataModelSC.addRow(rowData);
                }
            }
            //==========================================//

            //============= P.O. Terms ==============//
            //========= Display Line Items =============//
            FormatTermsGrid();
            FormatGridP();
            for (int i = 1; i <= ObjSupplier.colSuppTerms.size(); i++) {
                //Insert New Row
                Object[] rowData = new Object[4];
                clsSuppTerms ObjItem = (clsSuppTerms) ObjSupplier.colSuppTerms.get(Integer.toString(i));
                String TermType = (String) ObjItem.getAttribute("TERM_TYPE").getObj();

                if (TermType.equals("D")) {

                    rowData[0] = Integer.toString(TableD.getRowCount() + 1);
                    rowData[1] = Integer.toString((int) ObjItem.getAttribute("TERM_CODE").getVal());
                    rowData[2] = (String) ObjItem.getAttribute("TERM_DESC").getObj();
                    DataModelD.addRow(rowData);
                }

                if (TermType.equals("O")) {

                    rowData[0] = Integer.toString(TableO.getRowCount() + 1);
                    rowData[1] = Integer.toString((int) ObjItem.getAttribute("TERM_CODE").getVal());
                    rowData[2] = (String) ObjItem.getAttribute("TERM_DESC").getObj();
                    DataModelO.addRow(rowData);
                }

                if (TermType.equals("P")) {
                    rowData[0] = Integer.toString(TableP.getRowCount() + 1);
                    rowData[1] = ObjItem.getAttribute("TERM_DESC").getString();
                    rowData[2] = Integer.toString(ObjItem.getAttribute("TERM_DAYS").getInt());
                    rowData[3] = Integer.toString(ObjItem.getAttribute("TERM_CODE").getInt());
                    DataModelP.addRow(rowData);
                }

            }
            //=======================================//

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();

            String DocNo = Integer.toString((int) ObjSupplier.getAttribute("SUPP_ID").getVal());
            List = ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, 37, DocNo);
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

            if (EditMode == 0) {
                DataModelL.TableReadOnly(true);
            }

            //Showing Audit Trial History
            FormatGridHS();
            int SuppID = (int) ObjSupplier.getAttribute("SUPP_ID").getVal();
            HashMap History = clsSupplier.getHistoryList(EITLERPGLOBAL.gCompanyID, SuppID);
            for (int i = 1; i <= History.size(); i++) {
                clsSupplier ObjHistory = (clsSupplier) History.get(Integer.toString(i));
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

            //=========================================//
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Display Data Error: " + e.getMessage());
        }
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjSupplier.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
        //ObjSupplier.setAttribute("SUPP_ID",Long.parseLong(txtDeptID.getText()));

        //ObjSupplier.setAttribute("DECLARATION_DATE",EITLERPGLOBAL.formatDateDB(txtDate.getText()));
        ObjSupplier.setAttribute("SUPPLIER_CODE", txtSuppCode.getText());
        ObjSupplier.setAttribute("DUMMY_SUPPLIER_CODE", txtDummyCode.getText());
        ObjSupplier.setAttribute("SUPP_NAME", txtName.getText());
        ObjSupplier.setAttribute("ATTN", txtAttn.getText());
        ObjSupplier.setAttribute("ADD1", txtAdd1.getText());
        ObjSupplier.setAttribute("ADD2", txtAdd2.getText());
        ObjSupplier.setAttribute("ADD3", txtAdd3.getText());
        ObjSupplier.setAttribute("CITY", txtCity.getText());
        ObjSupplier.setAttribute("PINCODE", txtPIN.getText());
        // ObjSupplier.setAttribute("STATE_ID",EITLERPGLOBAL.getComboCode(cmbState)); 
        ObjSupplier.setAttribute("STATE", txtStateName.getText());
        ObjSupplier.setAttribute("STATE_CODE", txtStateCode.getText());
        ObjSupplier.setAttribute("STATE_GST_CODE", txtStateGstCode.getText());
        ObjSupplier.setAttribute("COUNTRY", txtCountryName.getText());
        //ObjSupplier.setAttribute("COUNTRY",EITLERPGLOBAL.getComboCode(cmbCountry));
        ObjSupplier.setAttribute("PLACE_OF_SUPPLY", txtPlaceofsupply.getText());
        ObjSupplier.setAttribute("PHONE_O", txtPhone_O.getText());
        ObjSupplier.setAttribute("PHONE_RES", txtPhone_R.getText());
        ObjSupplier.setAttribute("FAX_NO", txtFax.getText());
        ObjSupplier.setAttribute("MOBILE_NO", txtMobile.getText());
        ObjSupplier.setAttribute("EMAIL_ADD", txtEmail.getText());
        ObjSupplier.setAttribute("URL", txtURL.getText());
        ObjSupplier.setAttribute("WEB_SITE", txtWebsite.getText());
        ObjSupplier.setAttribute("SERVICETAX_NO", txtSTNO.getText());

        if ("".equals(EITLERPGLOBAL.formatDateDB(txtSTDate.getText()))) {
            ObjSupplier.setAttribute("SERVICETAX_DATE", "0000-00-00");
        } else {
            ObjSupplier.setAttribute("SERVICETAX_DATE", EITLERPGLOBAL.formatDateDB(txtSTDate.getText()));
        }
        //ObjSupplier.setAttribute("SERVICETAX_DATE",EITLERPGLOBAL.formatDateDB(txtSTDate.getText()));

        ObjSupplier.setAttribute("CST_NO", txtCST.getText());
        if ("".equals(EITLERPGLOBAL.formatDateDB(txtCSTDate.getText()))) {
            ObjSupplier.setAttribute("CST_DATE", "0000-00-00");
        } else {
            ObjSupplier.setAttribute("CST_DATE", EITLERPGLOBAL.formatDateDB(txtCSTDate.getText()));
        }
        //ObjSupplier.setAttribute("CST_DATE",EITLERPGLOBAL.formatDateDB(txtCSTDate.getText()));
        ObjSupplier.setAttribute("GST_NO", txtGST.getText());
        if ("".equals(EITLERPGLOBAL.formatDateDB(txtGSTDate.getText()))) {
            ObjSupplier.setAttribute("GST_DATE", "0000-00-00");
        } else {
            ObjSupplier.setAttribute("GST_DATE", EITLERPGLOBAL.formatDateDB(txtGSTDate.getText()));
        }

        //ObjSupplier.setAttribute("GST_DATE",EITLERPGLOBAL.formatDateDB(txtGSTDate.getText()));
        ObjSupplier.setAttribute("GSTIN_NO", txtGSTIN.getText());
        if ("".equals(EITLERPGLOBAL.formatDateDB(txtGSTINDate.getText()))) {
            ObjSupplier.setAttribute("GSTIN_DATE", "0000-00-00");
        } else {
            ObjSupplier.setAttribute("GSTIN_DATE", EITLERPGLOBAL.formatDateDB(txtGSTINDate.getText()));
        }

        //ObjSupplier.setAttribute("GSTIN_DATE",EITLERPGLOBAL.formatDateDB(txtGSTINDate.getText()));
        ObjSupplier.setAttribute("ECC_NO", txtECC.getText());
        if ("".equals(EITLERPGLOBAL.formatDateDB(txtECCDate.getText()))) {
            ObjSupplier.setAttribute("ECC_DATE", "0000-00-00");
        } else {
            ObjSupplier.setAttribute("ECC_DATE", EITLERPGLOBAL.formatDateDB(txtECCDate.getText()));
        }
        //ObjSupplier.setAttribute("ECC_DATE",EITLERPGLOBAL.formatDateDB(txtECCDate.getText()));
        ObjSupplier.setAttribute("SSIREG_NO", txtSSI.getText());
        if ("".equals(EITLERPGLOBAL.formatDateDB(txtSSIDate.getText()))) {
            ObjSupplier.setAttribute("SSIREG_DATE", "0000-00-00");
        } else {
            ObjSupplier.setAttribute("SSIREG_DATE", EITLERPGLOBAL.formatDateDB(txtSSIDate.getText()));
        }

        //ObjSupplier.setAttribute("SSIREG_DATE",EITLERPGLOBAL.formatDateDB(txtSSIDate.getText()));
        ObjSupplier.setAttribute("ESIREG_NO", txtESI.getText());
        if ("".equals(EITLERPGLOBAL.formatDateDB(txtESIDate.getText()))) {
            ObjSupplier.setAttribute("ESIREG_DATE", "0000-00-00");
        } else {
            ObjSupplier.setAttribute("ESIREG_DATE", EITLERPGLOBAL.formatDateDB(txtESIDate.getText()));
        }

        //ObjSupplier.setAttribute("ESIREG_DATE",EITLERPGLOBAL.formatDateDB(txtESIDate.getText()));
        ObjSupplier.setAttribute("PAN_NO", txtPAN.getText());
        if ("".equals(EITLERPGLOBAL.formatDateDB(txtPANDate.getText()))) {
            ObjSupplier.setAttribute("PAN_DATE", "0000-00-00");
        } else {
            ObjSupplier.setAttribute("PAN_DATE", EITLERPGLOBAL.formatDateDB(txtPANDate.getText()));
        }

        //ObjSupplier.setAttribute("PAN_DATE",EITLERPGLOBAL.formatDateDB(txtPANDate.getText()));
        if(chkMSME.isSelected()) {
            ObjSupplier.setAttribute("MSME",true);
        }
        else {
            ObjSupplier.setAttribute("MSME",false);
        }
        ObjSupplier.setAttribute("MSME_UAN",txtMSMEUAN.getText());
        ObjSupplier.setAttribute("MSME_DIC_NO",txtMSMEDIC.getText());

        ObjSupplier.setAttribute("CONTACT_PERSON_1", txtCP1.getText());
        ObjSupplier.setAttribute("CONTACT_PERSON_2", txtCP2.getText());
        ObjSupplier.setAttribute("FROM_DATE_REG", EITLERPGLOBAL.formatDateDB(txtRegFrom.getText()));
        ObjSupplier.setAttribute("TO_DATE_REG", EITLERPGLOBAL.formatDateDB(txtRegTo.getText()));
        ObjSupplier.setAttribute("PAYMENT_DAYS", EITLERPGLOBAL.formatLNumber(txtPayDays.getText()));
        ObjSupplier.setAttribute("BANK_ID", EITLERPGLOBAL.formatLNumber(txtBankID.getText()));
        ObjSupplier.setAttribute("BANK_NAME", txtBankName.getText());

        ObjSupplier.setAttribute("PAYMENT_TERM_CODE", EITLERPGLOBAL.getComboCode(cmbPayment));
        ObjSupplier.setAttribute("PROPOSED_ITEM", txtItem.getText());
        ObjSupplier.setAttribute("MAIN_ACCOUNT_CODE", txtMainAccountCode.getText());

        ObjSupplier.setAttribute("DISTANCE_KM", UtilFunctions.CInt(txtDistanceKm.getText()));

        if (chkOneTime.isSelected()) {
            ObjSupplier.setAttribute("ONETIME_SUPPLIER", true);
        } else {
            ObjSupplier.setAttribute("ONETIME_SUPPLIER", false);
        }
        if (chkBlocked.isSelected()) {
            ObjSupplier.setAttribute("BLOCKED", "Y");
        } else {
            ObjSupplier.setAttribute("BLOCKED", "N");
        }
        if (chkSlow.isSelected()) {
            ObjSupplier.setAttribute("SLOW_MOVING", true);
        } else {
            ObjSupplier.setAttribute("SLOW_MOVING", false);
        }
        if (chkReg.isSelected()) {
            ObjSupplier.setAttribute("ST35_REGISTERED", true);
        } else {
            ObjSupplier.setAttribute("ST35_REGISTERED", false);
        }
        if (chkSSI.isSelected()) {
            ObjSupplier.setAttribute("SSIREG", true);
        } else {
            ObjSupplier.setAttribute("SSIREG", false);
        }

        int SuppID = 0;

        ObjSupplier.setAttribute("PARENT_SUPP_ID", 0);
        ObjSupplier.colSuppChilds.clear();

        if (opgParent.isSelected()) {
            if (!txtPSuppID.getText().trim().equals("")) {
                SuppID = clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID, txtPSuppID.getText());
                ObjSupplier.setAttribute("PARENT_SUPP_ID", SuppID);
            }
        } else {
            //Define
            ObjSupplier.setAttribute("PARENT_SUPP_ID", 0);

            for (int i = 0; i < TableSC.getRowCount(); i++) {
                clsSuppChilds objChild = new clsSuppChilds();
                SuppID = clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID, (String) TableSC.getValueAt(i, 1));

                objChild.setAttribute("CHILD_SUPP_ID", SuppID);
                ObjSupplier.colSuppChilds.put(Integer.toString(ObjSupplier.colSuppChilds.size() + 1), objChild);
            }
        }

        //----- Update Approval Specific Fields -----------//
        ObjSupplier.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjSupplier.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjSupplier.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjSupplier.setAttribute("FROM_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            ObjSupplier.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjSupplier.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjSupplier.setAttribute("APPROVAL_STATUS", "R");
            ObjSupplier.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjSupplier.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjSupplier.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjSupplier.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjSupplier.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjSupplier.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }

        //=================== Setting up Line Items ==================//
        ObjSupplier.colSupplierItems.clear();

        for (int i = 0; i < TableL.getRowCount(); i++) {
            clsSupplierItem ObjItem = new clsSupplierItem();

            ObjItem.setAttribute("CATEGORY_ID", Long.parseLong(DataModelL.getValueByVariable("CATEGORY_ID", i)));
            //ObjItem.setAttribute("CATEGORY_DESC",DataModelL.getValueByVariable("CATEGORY_DESC",i));

            ObjSupplier.colSupplierItems.put(Integer.toString(ObjSupplier.colSupplierItems.size() + 1), ObjItem);
        }

        int TermCounter = 0;

        ObjSupplier.colSuppTerms.clear();

        for (int i = 0; i < TableP.getRowCount(); i++) {
            int PayTermCode = Integer.parseInt(TableP.getValueAt(i, 3).toString());
            String PayTerm = TableP.getValueAt(i, 1).toString();
            int TermDays = Integer.parseInt(TableP.getValueAt(i, 2).toString());
            clsSuppTerms ObjItem = new clsSuppTerms();

            ObjItem.setAttribute("TERM_TYPE", "P");
            ObjItem.setAttribute("TERM_CODE", PayTermCode);
            ObjItem.setAttribute("TERM_DAYS", TermDays);
            ObjItem.setAttribute("TERM_DESC", PayTerm);

            TermCounter++;
            ObjSupplier.colSuppTerms.put(Integer.toString(TermCounter), ObjItem);
        }

        for (int i = 0; i < TableD.getRowCount(); i++) {
            TermCounter++;

            clsSuppTerms ObjItem = new clsSuppTerms();

            ObjItem.setAttribute("TERM_TYPE", "D");
            if (EITLERPGLOBAL.IsNumber((String) TableD.getValueAt(i, 1))) {
                ObjItem.setAttribute("TERM_CODE", Integer.parseInt((String) TableD.getValueAt(i, 1)));
            } else {
                ObjItem.setAttribute("TERM_CODE", 0);
            }
            ObjItem.setAttribute("TERM_DESC", (String) TableD.getValueAt(i, 2));

            ObjSupplier.colSuppTerms.put(Integer.toString(TermCounter), ObjItem);
        }

        for (int i = 0; i < TableO.getRowCount(); i++) {
            TermCounter++;

            clsSuppTerms ObjItem = new clsSuppTerms();

            ObjItem.setAttribute("TERM_TYPE", "O");
            if (EITLERPGLOBAL.IsNumber((String) TableO.getValueAt(i, 1))) {
                ObjItem.setAttribute("TERM_CODE", Integer.parseInt((String) TableO.getValueAt(i, 1)));
            } else {
                ObjItem.setAttribute("TERM_CODE", 0);
            }
            ObjItem.setAttribute("TERM_DESC", (String) TableO.getValueAt(i, 2));

            ObjSupplier.colSuppTerms.put(Integer.toString(TermCounter), ObjItem);
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

    private void FormatGridSC() {
        DataModelSC = new EITLTableModel();

        TableSC.removeAll();
        TableSC.setModel(DataModelSC);

        //Set the table Readonly
        DataModelSC.TableReadOnly(true);

        //Add the columns
        DataModelSC.addColumn("Sr.");
        DataModelSC.addColumn("Supp. Code");
        DataModelSC.addColumn("Supp. Name");
        DataModelSC.addColumn("Supp. ID");

        TableSC.setAutoResizeMode(TableA.AUTO_RESIZE_OFF);
        DataModelSC.TableReadOnly(true);
    }

    private void SetMenuForRights() {
        // --- Add Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 161)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,372))
         {
         cmdEdit.setEnabled(true);
         }
         else
         {
         cmdEdit.setEnabled(false);
         }*/

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 163)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 164)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }

    private void FormatTermsGrid() {

        //--- PO Terms Formatting -----//
        DataModelD = new EITLTableModel();

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

                    if (col == 1) {
                        int theCode = Integer.parseInt((String) TableD.getValueAt(TableD.getSelectedRow(), 1));

                        String Desc = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "DELIVERY_CODE", theCode);
                        TableD.setValueAt(Desc, TableD.getSelectedRow(), 2);
                    }
                }
            }
        });
        //=============================================//

        //--- PO Terms Formatting -----//
        DataModelO = new EITLTableModel();

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

                    if (col == 1) {
                        int theCode = Integer.parseInt((String) TableO.getValueAt(TableO.getSelectedRow(), 1));

                        String Desc = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "OTHER_CODE", theCode);
                        TableO.setValueAt(Desc, TableO.getSelectedRow(), 2);
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

                if (FieldName.trim().equals("SUPPLIER_CODE")) {
                    int a = 0;
                }
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

        for (int i = 0; i < Tab3.getComponentCount() - 1; i++) {
            if (Tab3.getComponent(i).getName() != null) {

                FieldName = Tab3.getComponent(i).getName();
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    Tab3.getComponent(i).setEnabled(true);
                }

            }
        }
        //=============== Header Fields Setup Complete =================//

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

    private void FormatGridP() {
        DataModelP = new EITLTableModel();

        TableP.removeAll();
        TableP.setModel(DataModelP);

        //Set the table Readonly
        DataModelP.TableReadOnly(true);

        //Add the columns
        DataModelP.addColumn("Sr.");
        DataModelP.addColumn("Term");
        DataModelP.addColumn("Days");
        DataModelP.addColumn("Code");

        TableP.setAutoResizeMode(TableP.AUTO_RESIZE_OFF);
    }

    public void FindWaiting() {
        ObjSupplier.Filter(" WHERE SUPP_ID IN (SELECT D_COM_SUPP_MASTER.SUPP_ID FROM D_COM_SUPP_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_MASTER.SUPP_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_MASTER.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND D_COM_DOC_DATA.USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=37)");
        ObjSupplier.MoveFirst();
        DisplayData();
    }

    public void FindEx(int pCompanyID, String pSuppID) {
        ObjSupplier.Filter(" WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND SUPP_ID=" + pSuppID);
        ObjSupplier.MoveFirst();
        DisplayData();
    }

    private void GenerateRejectedUserCombo() {

        HashMap List = new HashMap();
        HashMap DeptList = new HashMap();
        HashMap DeptUsers = new HashMap();
        int SuppID = Integer.parseInt(txtDummyCode.getText());

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
                    IncludeUser = ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, 37, Integer.toString(SuppID), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    IncludeUser = ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, 37, Integer.toString(SuppID), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String DocNo = Long.toString((long) ObjSupplier.getAttribute("SUPP_ID").getVal());
            int Creator = ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, 37, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }

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
}
