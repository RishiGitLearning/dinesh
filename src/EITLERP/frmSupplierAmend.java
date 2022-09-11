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
/*<APPLET CODE=frmSupplierAmend.class HEIGHT=574 WIDTH=758></APPLET>*/

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
import java.sql.*;
import EITLERP.Finance.*;


public class frmSupplierAmend extends javax.swing.JApplet {
    
    private int EditMode=0;
    private EITLTableModel DataModelH;
    private EITLTableModel DataModelL;
    private EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint=new EITLTableCellRenderer();
    
    private EITLTableModel DataModelD;
    private EITLTableModel DataModelP;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelU;
    
    private HashMap colVariables=new HashMap();
    private HashMap colVariables_H=new HashMap();
    clsColumn ObjColumn=new clsColumn();
    
    private boolean Updating=false;
    private boolean Updating_H=false;
    private boolean DoNotEvaluate=false;
    
    private clsSupplierAmend ObjSupplierAmend;
    
    private int SelHierarchyID=0; //Selected Hierarchy
    private int lnFromID=0;
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbPaymentModel;
    private EITLTableModel DataModelA;
    private EITLTableModel DataModelSC;
    
    private boolean HistoryView=false;
    private String theDocNo="";
    private EITLTableModel DataModelHS;
    private EITLComboModel cmbStateModel;
    private EITLComboModel cmbCountryModel;
    private EITLComboModel cmbServiceTaxModel;
    private EITLComboModel cmbCSTModel;
    private EITLComboModel cmbGSTModel;
    private EITLComboModel cmbECCModel;
    private EITLComboModel cmbSSIModel;
    private EITLComboModel cmbESIModel;
    private EITLComboModel cmbPANModel;
    private EITLComboModel cmbGSTINModel;
    private EITLComboModel cmbMSMEUANModel;
    
    public frmPendingApprovals frmPA;
    
    /** Creates new form frmSupplier */
    public frmSupplierAmend() {
        setSize(830,650);
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
        
        ObjColumn.LoadData((int)EITLERPGLOBAL.gCompanyID);
        
        FormatGrid();
        
        // SetNumberFormats();
        
        GenerateCombos();
        GenerateCountryCombo();
        GenerateStateCombo();
        ObjSupplierAmend = new clsSupplierAmend();
        
        if(ObjSupplierAmend.LoadData(EITLERPGLOBAL.gCompanyID)) {
            //ObjSupplier.MoveFirst();
            DisplayData();
            SetMenuForRights();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while loading data. \n Error is "+ObjSupplierAmend.LastError);
        }
        
        
        txtAuditRemarks.setVisible(false);
        cmbMSMEUAN.setVisible(false);
    }
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel=new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=50");
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=50");
        }
        
        for(int i=1;i<=List.size();i++) {
            clsHierarchy ObjHierarchy=(clsHierarchy) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text=(String)ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //
        
        
        cmbPaymentModel=new EITLComboModel();
        cmbPayment.removeAllItems();
        cmbPayment.setModel(cmbPaymentModel);
        
        strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND PARA_ID='PAYMENT_CODE'";
        
        List=clsParameter.getList(strCondition);
        for(int i=1;i<=List.size();i++) {
            clsParameter ObjPara=(clsParameter) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjPara.getAttribute("PARA_CODE").getVal();
            aData.Text=(String)ObjPara.getAttribute("DESC").getObj();
            aData.strCode="";
            cmbPaymentModel.addElement(aData);
        }
        
        
        cmbServiceTaxModel=new EITLComboModel();
        cmbServiceTax.removeAllItems();
        cmbServiceTax.setModel(cmbServiceTaxModel);
        
        ComboData aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbServiceTaxModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbServiceTaxModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbServiceTaxModel.addElement(aData);
        
        
        
        
        cmbCSTModel=new EITLComboModel();
        cmbCST.removeAllItems();
        cmbCST.setModel(cmbCSTModel);
        
        aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbCSTModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbCSTModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbCSTModel.addElement(aData);
        
        
        
        cmbGSTModel=new EITLComboModel();
        cmbGST.removeAllItems();
        cmbGST.setModel(cmbGSTModel);
        
        aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbGSTModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbGSTModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbGSTModel.addElement(aData);
        
        cmbGSTINModel=new EITLComboModel();
        cmbGSTIN.removeAllItems();
        cmbGSTIN.setModel(cmbGSTINModel);
        
        aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbGSTINModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbGSTINModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbGSTINModel.addElement(aData);
        
        
        
        cmbECCModel=new EITLComboModel();
        cmbECC.removeAllItems();
        cmbECC.setModel(cmbECCModel);
        
        aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbECCModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbECCModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbECCModel.addElement(aData);
        
        
        
        
        cmbSSIModel=new EITLComboModel();
        cmbSSI.removeAllItems();
        cmbSSI.setModel(cmbSSIModel);
        
        aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbSSIModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbSSIModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbSSIModel.addElement(aData);
        
        
        
        
        cmbESIModel=new EITLComboModel();
        cmbESI.removeAllItems();
        cmbESI.setModel(cmbESIModel);
        
        aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbESIModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbESIModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbESIModel.addElement(aData);
        
        cmbPANModel=new EITLComboModel();
        cmbPAN.removeAllItems();
        cmbPAN.setModel(cmbPANModel);
        
        aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbPANModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbPANModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbPANModel.addElement(aData);
        
                cmbMSMEUANModel=new EITLComboModel();
        cmbMSMEUAN.removeAllItems();
        cmbMSMEUAN.setModel(cmbMSMEUANModel);
        
        aData=new ComboData();
        aData.strCode="N/A";
        aData.Text="N/A";
        cmbMSMEUANModel.addElement(aData);
        
        
        aData=new ComboData();
        aData.strCode="APPLIED";
        aData.Text="APPLIED";
        cmbMSMEUANModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="Enter";
        aData.Text="Enter";
        cmbMSMEUANModel.addElement(aData);
    }
    
    private void FormatGrid() {
        HashMap ColList=new HashMap();
        
        DataModelL=new EITLTableModel();
        
        TableL.removeAll();
        TableL.setModel(DataModelL);
        
        //Set the table Readonly
        DataModelL.TableReadOnly(false);
        
        ColList=clsSystemColumn.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=50 AND HIDDEN=0 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for(int i=1;i<=ColList.size();i++) {
            clsSystemColumn ObjColumn=(clsSystemColumn)ColList.get(Integer.toString(i));
            
            //Add Column First
            DataModelL.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj()); //0
            
            
            if(ObjColumn.getAttribute("NUMERIC").getBool()) {
                DataModelL.SetNumeric(TableL.getColumnCount()-1, true);
            }
            else {
                DataModelL.SetNumeric(TableL.getColumnCount()-1, false);
            }
            
            
            String Variable=(String)ObjColumn.getAttribute("VARIABLE").getObj();
            
            DataModelL.SetColID(TableL.getColumnCount()-1, 0);
            
            
            DataModelL.SetVariable(TableL.getColumnCount()-1,Variable.trim());
            DataModelL.SetOperation(TableL.getColumnCount()-1, "-");
            DataModelL.SetInclude(TableL.getColumnCount()-1,true);
            
            if(ObjColumn.getAttribute("READONLY").getBool()) {
                DataModelL.SetReadOnly(TableL.getColumnCount()-1);
            }
        }
        
        SetupColumns();
        
        //Now hide the column 1
        TableColumnModel ColModel=TableL.getColumnModel();
        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //----- Install Table Model Event Listener -------//
        TableL.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int col = e.getColumn();
                    
                    if(col==DataModelL.getColFromVariable("CATEGORY_ID")) {
                        
                        String CategoryID=DataModelL.getValueByVariable("CATEGORY_ID",TableL.getSelectedRow());
                        TableL.setValueAt(clsItemCategory.getCategoryDesc((int)EITLERPGLOBAL.gCompanyID, Long.parseLong(CategoryID)), TableL.getSelectedRow(),DataModelL.getColFromVariable("CATEGORY_DESC"));
                    }
            /*        if(!Updating) {
                        UpdateResults(col);
                    }*/
                    
                }
            }
        });
    }
    
    private void SetupColumns() {
        HashMap List=new HashMap();
        HashMap ColList=new HashMap();
        
        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableL.setRowSelectionAllowed(true);
        TableL.setColumnSelectionAllowed(true);
        
        ColList=clsSystemColumn.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=50 AND HIDDEN=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for(int i=1;i<=ColList.size();i++) {
            clsSystemColumn ObjColumn=(clsSystemColumn)ColList.get(Integer.toString(i));
            
            //Add Column First
            DataModelL.addColumn(""); //
            DataModelL.SetColID(TableL.getColumnCount()-1, 0);
            DataModelL.SetVariable(TableL.getColumnCount()-1,(String)ObjColumn.getAttribute("VARIABLE").getObj());
            DataModelL.SetOperation(TableL.getColumnCount()-1, "-");
            DataModelL.SetInclude(TableL.getColumnCount()-1,true);
            
            DataModelL.SetReadOnly(TableL.getColumnCount()-1);
            
            //Hide the Column
            TableL.getColumnModel().getColumn(TableL.getColumnCount()-1).setPreferredWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount()-1).setMaxWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount()-1).setMinWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount()-1).setWidth(0);
        }
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        groupChild = new javax.swing.ButtonGroup();
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
        txtCP1 = new javax.swing.JTextField();
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
        jLabel37 = new javax.swing.JLabel();
        txtAmendID = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtAmendDate = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtAmendReason = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        TableP = new javax.swing.JTable();
        chkCancelled = new javax.swing.JCheckBox();
        jLabel48 = new javax.swing.JLabel();
        cmbState = new javax.swing.JComboBox();
        cmbCountry = new javax.swing.JComboBox();
        txtPlaceofsupply = new javax.swing.JTextField();
        txtStateCode = new javax.swing.JTextField();
        txtStateGstCode = new javax.swing.JTextField();
        txtStateName = new javax.swing.JTextField();
        txtCountryName = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        txtDistanceKm = new javax.swing.JTextField();
        Tab3 = new javax.swing.JPanel();
        cmdNext2 = new javax.swing.JButton();
        cmdNext4 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        cmbServiceTax = new javax.swing.JComboBox();
        txtSTNO = new javax.swing.JTextField();
        txtSTDate = new javax.swing.JTextField();
        txtCSTDate = new javax.swing.JTextField();
        txtCST = new javax.swing.JTextField();
        cmbCST = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cmbGST = new javax.swing.JComboBox();
        txtGST = new javax.swing.JTextField();
        txtGSTDate = new javax.swing.JTextField();
        txtECCDate = new javax.swing.JTextField();
        txtECC = new javax.swing.JTextField();
        cmbECC = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        cmbSSI = new javax.swing.JComboBox();
        txtSSI = new javax.swing.JTextField();
        txtSSIDate = new javax.swing.JTextField();
        txtESIDate = new javax.swing.JTextField();
        txtESI = new javax.swing.JTextField();
        cmbESI = new javax.swing.JComboBox();
        jLabel56 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtRegFrom = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        txtRegTo = new javax.swing.JTextField();
        lblPanNo = new javax.swing.JLabel();
        cmbPAN = new javax.swing.JComboBox();
        txtPAN = new javax.swing.JTextField();
        txtPANDate = new javax.swing.JTextField();
        cmbGSTIN = new javax.swing.JComboBox();
        txtGSTIN = new javax.swing.JTextField();
        txtGSTINDate = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        cmbMSMEUAN = new javax.swing.JComboBox();
        lblPanNo1 = new javax.swing.JLabel();
        txtMSMEUAN = new javax.swing.JTextField();
        lblPanNo2 = new javax.swing.JLabel();
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
        TableL = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TableO = new javax.swing.JTable();
        cmdOtherAdd = new javax.swing.JButton();
        cmdOtherRemove = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        opgParent = new javax.swing.JRadioButton();
        txtPSuppID = new javax.swing.JTextField();
        txtPSuppName = new javax.swing.JTextField();
        opgChild = new javax.swing.JRadioButton();
        txtCSuppID = new javax.swing.JTextField();
        txtCSuppName = new javax.swing.JTextField();
        cmdAddC = new javax.swing.JButton();
        cmdRemoveC = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        TableSC = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableU = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        cmdShowRemarksU = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMainAccountCode = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
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

        cmdShowOriginal.setText("Show Original");
        cmdShowOriginal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowOriginalActionPerformed(evt);
            }
        });
        ToolBar.add(cmdShowOriginal);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText(" SUPPLIER MASTER UPDATION");
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
        jLabel2.setBounds(5, 60, 115, 15);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Dummy Code :");
        Tab1.add(jLabel3);
        jLabel3.setBounds(245, 61, 95, 15);

        txtDummyCode.setEnabled(false);
        txtDummyCode.setName("DUMMY_SUPPLIER_CODE"); // NOI18N
        txtDummyCode.setNextFocusableComponent(txtName);
        Tab1.add(txtDummyCode);
        txtDummyCode.setBounds(340, 59, 110, 19);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Name :");
        Tab1.add(jLabel5);
        jLabel5.setBounds(30, 80, 90, 20);

        txtName.setEnabled(false);
        txtName.setName("SUPP_NAME"); // NOI18N
        txtName.setNextFocusableComponent(txtAttn);
        Tab1.add(txtName);
        txtName.setBounds(125, 83, 320, 19);

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
        cmdNext1.setBounds(678, 352, 90, 25);

        txtSuppCode.setEnabled(false);
        txtSuppCode.setName("SUPPLIER_CODE"); // NOI18N
        txtSuppCode.setNextFocusableComponent(txtDummyCode);
        Tab1.add(txtSuppCode);
        txtSuppCode.setBounds(125, 59, 120, 19);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("ATTN :");
        Tab1.add(jLabel4);
        jLabel4.setBounds(5, 107, 115, 15);

        txtAttn.setName("ATTN"); // NOI18N
        txtAttn.setNextFocusableComponent(txtAdd1);
        txtAttn.setEnabled(false);
        Tab1.add(txtAttn);
        txtAttn.setBounds(125, 107, 320, 19);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Address :");
        Tab1.add(jLabel7);
        jLabel7.setBounds(20, 130, 100, 15);

        txtAdd1.setEnabled(false);
        txtAdd1.setName("ADD1"); // NOI18N
        txtAdd1.setNextFocusableComponent(txtAdd2);
        Tab1.add(txtAdd1);
        txtAdd1.setBounds(125, 130, 320, 19);

        txtAdd2.setName("ADD2"); // NOI18N
        txtAdd2.setNextFocusableComponent(txtAdd3);
        txtAdd2.setEnabled(false);
        Tab1.add(txtAdd2);
        txtAdd2.setBounds(125, 151, 320, 19);

        txtAdd3.setName("ADD3"); // NOI18N
        txtAdd3.setNextFocusableComponent(txtCity);
        txtAdd3.setEnabled(false);
        Tab1.add(txtAdd3);
        txtAdd3.setBounds(125, 173, 320, 19);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("City :");
        Tab1.add(jLabel9);
        jLabel9.setBounds(5, 198, 115, 15);

        txtCity.setName("CITY"); // NOI18N
        txtCity.setNextFocusableComponent(txtPIN);
        txtCity.setEnabled(false);
        Tab1.add(txtCity);
        txtCity.setBounds(125, 198, 120, 19);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Pincode :");
        Tab1.add(jLabel10);
        jLabel10.setBounds(255, 198, 80, 15);

        txtPIN.setName("PINCODE"); // NOI18N
        txtPIN.setEnabled(false);
        Tab1.add(txtPIN);
        txtPIN.setBounds(340, 198, 110, 19);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("State :");
        Tab1.add(jLabel6);
        jLabel6.setBounds(0, 230, 115, 15);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Country :");
        Tab1.add(jLabel8);
        jLabel8.setBounds(40, 260, 80, 15);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Phone (R) :");
        Tab1.add(jLabel11);
        jLabel11.setBounds(250, 290, 80, 15);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Phone (O) :");
        Tab1.add(jLabel12);
        jLabel12.setBounds(10, 290, 115, 15);

        txtPhone_O.setEnabled(false);
        txtPhone_O.setName("PHONE_O"); // NOI18N
        txtPhone_O.setNextFocusableComponent(txtPhone_R);
        Tab1.add(txtPhone_O);
        txtPhone_O.setBounds(130, 290, 120, 19);

        txtPhone_R.setEnabled(false);
        txtPhone_R.setName("PHONE_RES"); // NOI18N
        txtPhone_R.setNextFocusableComponent(txtFax);
        Tab1.add(txtPhone_R);
        txtPhone_R.setBounds(340, 290, 110, 19);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Fax No :");
        Tab1.add(jLabel13);
        jLabel13.setBounds(10, 320, 115, 15);

        txtFax.setEnabled(false);
        txtFax.setName("FAX_NO"); // NOI18N
        txtFax.setNextFocusableComponent(txtMobile);
        Tab1.add(txtFax);
        txtFax.setBounds(130, 320, 120, 19);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Mobile No :");
        Tab1.add(jLabel14);
        jLabel14.setBounds(250, 320, 80, 15);

        txtMobile.setEnabled(false);
        txtMobile.setName("MOBILE_NO"); // NOI18N
        txtMobile.setNextFocusableComponent(txtEmail);
        Tab1.add(txtMobile);
        txtMobile.setBounds(340, 320, 110, 19);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("E-Mail :");
        Tab1.add(jLabel15);
        jLabel15.setBounds(10, 350, 115, 15);

        txtEmail.setEnabled(false);
        txtEmail.setName("EMAIL_ADD"); // NOI18N
        txtEmail.setNextFocusableComponent(txtWebsite);
        Tab1.add(txtEmail);
        txtEmail.setBounds(130, 350, 120, 19);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("URL :");
        Tab1.add(jLabel16);
        jLabel16.setBounds(40, 380, 80, 15);

        txtWebsite.setEnabled(false);
        txtWebsite.setName("WEB_SITE"); // NOI18N
        txtWebsite.setNextFocusableComponent(txtURL);
        Tab1.add(txtWebsite);
        txtWebsite.setBounds(340, 350, 110, 19);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Website :");
        Tab1.add(jLabel17);
        jLabel17.setBounds(250, 350, 80, 15);

        txtURL.setEnabled(false);
        txtURL.setName("URL"); // NOI18N
        txtURL.setNextFocusableComponent(txtCP1);
        Tab1.add(txtURL);
        txtURL.setBounds(130, 380, 320, 19);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Contact Person 1 :");
        Tab1.add(jLabel29);
        jLabel29.setBounds(10, 440, 115, 15);

        txtCP1.setEnabled(false);
        txtCP1.setName("CONTACT_PERSON_1"); // NOI18N
        txtCP1.setNextFocusableComponent(txtCP2);
        Tab1.add(txtCP1);
        txtCP1.setBounds(130, 440, 319, 19);

        txtCP2.setEnabled(false);
        txtCP2.setName("CONTACT_PERSON_2"); // NOI18N
        txtCP2.setNextFocusableComponent(cmbPayment);
        Tab1.add(txtCP2);
        txtCP2.setBounds(130, 470, 320, 19);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Contact Person 2 :");
        Tab1.add(jLabel30);
        jLabel30.setBounds(10, 470, 115, 15);

        lblBankID.setText("Bank");
        Tab1.add(lblBankID);
        lblBankID.setBounds(480, 70, 39, 15);

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
        txtBankID.setBounds(525, 70, 58, 19);

        lblPaymentDays.setText("Days");
        Tab1.add(lblPaymentDays);
        lblPaymentDays.setBounds(480, 70, 39, 15);

        txtPayDays.setName("PAYMENT_DAYS"); // NOI18N
        txtPayDays.setNextFocusableComponent(txtItem);
        txtPayDays.setEnabled(false);
        txtPayDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPayDaysActionPerformed(evt);
            }
        });
        Tab1.add(txtPayDays);
        txtPayDays.setBounds(525, 70, 59, 19);

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
        chkOneTime.setBounds(475, 330, 140, 20);

        chkBlocked.setText("Blocked");
        chkBlocked.setName("BLOCKED"); // NOI18N
        chkBlocked.setNextFocusableComponent(chkSSI);
        chkBlocked.setEnabled(false);
        Tab1.add(chkBlocked);
        chkBlocked.setBounds(630, 330, 110, 20);

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
        chkSlow.setBounds(630, 313, 130, 20);

        chkReg.setText("Registered ");
        chkReg.setName("ST35_REGISTERED"); // NOI18N
        chkReg.setNextFocusableComponent(chkSlow);
        chkReg.setEnabled(false);
        Tab1.add(chkReg);
        chkReg.setBounds(475, 311, 130, 20);

        txtBankName.setName("BANK_NAME"); // NOI18N
        txtBankName.setNextFocusableComponent(txtPayDays);
        txtBankName.setEnabled(false);
        Tab1.add(txtBankName);
        txtBankName.setBounds(590, 70, 190, 20);

        chkSSI.setText("SSI Supplier");
        chkSSI.setName("SSIREG"); // NOI18N
        chkSSI.setNextFocusableComponent(cmdNext1);
        chkSSI.setEnabled(false);
        Tab1.add(chkSSI);
        chkSSI.setBounds(475, 348, 130, 20);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(250, 10, 30, 16);

        jLabel18.setText("Payment Terms :");
        Tab1.add(jLabel18);
        jLabel18.setBounds(480, 13, 110, 15);

        cmbPayment.setNextFocusableComponent(txtBankID);
        cmbPayment.setEnabled(false);
        cmbPayment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPaymentItemStateChanged(evt);
            }
        });
        Tab1.add(cmbPayment);
        cmbPayment.setBounds(480, 32, 300, 24);

        jLabel25.setText("Proposed Item to be Purchased :");
        Tab1.add(jLabel25);
        jLabel25.setBounds(475, 265, 204, 15);

        txtItem.setNextFocusableComponent(chkReg);
        txtItem.setEnabled(false);
        Tab1.add(txtItem);
        txtItem.setBounds(475, 282, 283, 21);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Tab1.add(jButton1);
        jButton1.setBounds(760, 282, 32, 22);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Updation ID :");
        Tab1.add(jLabel37);
        jLabel37.setBounds(0, 10, 115, 15);

        txtAmendID.setEnabled(false);
        txtAmendID.setName("SUPPLIER_CODE"); // NOI18N
        txtAmendID.setNextFocusableComponent(txtDummyCode);
        Tab1.add(txtAmendID);
        txtAmendID.setBounds(125, 9, 120, 19);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Date :");
        Tab1.add(jLabel39);
        jLabel39.setBounds(285, 12, 50, 15);

        txtAmendDate.setName("DUMMY_SUPPLIER_CODE"); // NOI18N
        txtAmendDate.setNextFocusableComponent(txtAmendReason);
        txtAmendDate.setEnabled(false);
        Tab1.add(txtAmendDate);
        txtAmendDate.setBounds(340, 10, 110, 19);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Update Reason :");
        Tab1.add(jLabel43);
        jLabel43.setBounds(5, 35, 115, 15);

        txtAmendReason.setName("SUPP_NAME"); // NOI18N
        txtAmendReason.setNextFocusableComponent(txtAttn);
        txtAmendReason.setEnabled(false);
        Tab1.add(txtAmendReason);
        txtAmendReason.setBounds(125, 33, 281, 19);

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Tab1.add(jButton2);
        jButton2.setBounds(420, 33, 31, 20);

        jButton3.setText("Add Term");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        Tab1.add(jButton3);
        jButton3.setBounds(566, 116, 109, 21);

        jButton4.setText("Remove");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        Tab1.add(jButton4);
        jButton4.setBounds(679, 116, 89, 21);

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
        jScrollPane5.setBounds(475, 143, 316, 94);

        chkCancelled.setText("Cancelled");
        chkCancelled.setEnabled(false);
        Tab1.add(chkCancelled);
        chkCancelled.setBounds(690, 5, 90, 20);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Place Of Supply : ");
        Tab1.add(jLabel48);
        jLabel48.setBounds(10, 410, 115, 15);

        cmbState.setEnabled(false);
        cmbState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStateActionPerformed(evt);
            }
        });
        Tab1.add(cmbState);
        cmbState.setBounds(340, 230, 110, 24);

        cmbCountry.setEnabled(false);
        Tab1.add(cmbCountry);
        cmbCountry.setBounds(300, 260, 150, 24);

        txtPlaceofsupply.setEnabled(false);
        Tab1.add(txtPlaceofsupply);
        txtPlaceofsupply.setBounds(130, 410, 320, 19);

        txtStateCode.setEnabled(false);
        Tab1.add(txtStateCode);
        txtStateCode.setBounds(240, 230, 50, 30);

        txtStateGstCode.setEnabled(false);
        Tab1.add(txtStateGstCode);
        txtStateGstCode.setBounds(290, 230, 50, 19);

        txtStateName.setEnabled(false);
        Tab1.add(txtStateName);
        txtStateName.setBounds(130, 230, 100, 19);

        txtCountryName.setEnabled(false);
        Tab1.add(txtCountryName);
        txtCountryName.setBounds(130, 260, 130, 19);

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

        cmdNext4.setLabel("Previous <<");
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
        cmdNext4.setBounds(550, 390, 120, 25);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Type");
        Tab3.add(jLabel38);
        jLabel38.setBounds(125, 20, 100, 15);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("No");
        Tab3.add(jLabel26);
        jLabel26.setBounds(240, 20, 197, 15);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Date");
        Tab3.add(jLabel23);
        jLabel23.setBounds(450, 20, 90, 15);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Service Tax :");
        Tab3.add(jLabel22);
        jLabel22.setBounds(15, 44, 90, 15);

        cmbServiceTax.setEnabled(false);
        cmbServiceTax.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbServiceTaxItemStateChanged(evt);
            }
        });
        Tab3.add(cmbServiceTax);
        cmbServiceTax.setBounds(125, 42, 100, 21);

        txtSTNO.setEnabled(false);
        txtSTNO.setName("SERVICETAX_NO"); // NOI18N
        Tab3.add(txtSTNO);
        txtSTNO.setBounds(240, 42, 197, 21);

        txtSTDate.setName("SERVICETAX_DATE"); // NOI18N
        txtSTDate.setEnabled(false);
        Tab3.add(txtSTDate);
        txtSTDate.setBounds(450, 42, 90, 21);

        txtCSTDate.setName("CST_DATE"); // NOI18N
        txtCSTDate.setEnabled(false);
        Tab3.add(txtCSTDate);
        txtCSTDate.setBounds(450, 69, 90, 21);

        txtCST.setEnabled(false);
        txtCST.setName("CST_NO"); // NOI18N
        Tab3.add(txtCST);
        txtCST.setBounds(240, 69, 197, 21);

        cmbCST.setEnabled(false);
        cmbCST.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCSTItemStateChanged(evt);
            }
        });
        Tab3.add(cmbCST);
        cmbCST.setBounds(125, 69, 100, 21);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("CST :");
        Tab3.add(jLabel24);
        jLabel24.setBounds(15, 71, 90, 15);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("GSTIN No. :");
        Tab3.add(jLabel27);
        jLabel27.setBounds(20, 250, 90, 20);

        cmbGST.setEnabled(false);
        cmbGST.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbGSTItemStateChanged(evt);
            }
        });
        Tab3.add(cmbGST);
        cmbGST.setBounds(125, 97, 100, 21);

        txtGST.setEnabled(false);
        txtGST.setName("GST_NO"); // NOI18N
        Tab3.add(txtGST);
        txtGST.setBounds(240, 97, 197, 21);

        txtGSTDate.setEnabled(false);
        txtGSTDate.setName("GST_DATE"); // NOI18N
        Tab3.add(txtGSTDate);
        txtGSTDate.setBounds(450, 97, 90, 21);

        txtECCDate.setName("ECC_DATE"); // NOI18N
        txtECCDate.setEnabled(false);
        Tab3.add(txtECCDate);
        txtECCDate.setBounds(450, 128, 90, 21);

        txtECC.setEnabled(false);
        txtECC.setName("ECC_NO"); // NOI18N
        Tab3.add(txtECC);
        txtECC.setBounds(240, 128, 197, 21);

        cmbECC.setEnabled(false);
        cmbECC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbECCItemStateChanged(evt);
            }
        });
        Tab3.add(cmbECC);
        cmbECC.setBounds(125, 128, 100, 21);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("ECC :");
        Tab3.add(jLabel28);
        jLabel28.setBounds(15, 130, 90, 15);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel55.setText("SSI :");
        Tab3.add(jLabel55);
        jLabel55.setBounds(15, 163, 90, 15);

        cmbSSI.setEnabled(false);
        cmbSSI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSSIItemStateChanged(evt);
            }
        });
        Tab3.add(cmbSSI);
        cmbSSI.setBounds(125, 160, 100, 21);

        txtSSI.setEnabled(false);
        txtSSI.setName("SSIREG_NO"); // NOI18N
        Tab3.add(txtSSI);
        txtSSI.setBounds(240, 160, 197, 21);

        txtSSIDate.setName("SSIREG_DATE"); // NOI18N
        txtSSIDate.setEnabled(false);
        Tab3.add(txtSSIDate);
        txtSSIDate.setBounds(450, 160, 90, 21);

        txtESIDate.setEnabled(false);
        txtESIDate.setName("ESIREG_DATE"); // NOI18N
        Tab3.add(txtESIDate);
        txtESIDate.setBounds(450, 190, 90, 21);

        txtESI.setEnabled(false);
        txtESI.setName("ESIREG_NO"); // NOI18N
        Tab3.add(txtESI);
        txtESI.setBounds(240, 190, 197, 21);

        cmbESI.setEnabled(false);
        cmbESI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbESIItemStateChanged(evt);
            }
        });
        Tab3.add(cmbESI);
        cmbESI.setBounds(125, 190, 100, 21);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("ESI :");
        Tab3.add(jLabel56);
        jLabel56.setBounds(15, 194, 90, 15);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab3.add(jPanel1);
        jPanel1.setBounds(20, 320, 770, 10);

        jLabel21.setText("Registration Time Limit :");
        Tab3.add(jLabel21);
        jLabel21.setBounds(20, 340, 161, 15);

        jLabel41.setText("Date From :");
        Tab3.add(jLabel41);
        jLabel41.setBounds(10, 360, 80, 15);

        txtRegFrom.setEnabled(false);
        txtRegFrom.setName("FROM_DATE_REG"); // NOI18N
        Tab3.add(txtRegFrom);
        txtRegFrom.setBounds(100, 360, 104, 21);

        jLabel47.setText("To :");
        Tab3.add(jLabel47);
        jLabel47.setBounds(210, 360, 30, 15);

        txtRegTo.setEnabled(false);
        txtRegTo.setName("TO_DATE_REG"); // NOI18N
        Tab3.add(txtRegTo);
        txtRegTo.setBounds(250, 360, 105, 21);

        lblPanNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPanNo.setText("DIC NO:");
        Tab3.add(lblPanNo);
        lblPanNo.setBounds(430, 280, 60, 15);

        cmbPAN.setEnabled(false);
        cmbPAN.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPANItemStateChanged(evt);
            }
        });
        Tab3.add(cmbPAN);
        cmbPAN.setBounds(130, 220, 100, 21);

        txtPAN.setEnabled(false);
        txtPAN.setName("ESIREG_NO"); // NOI18N
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
        Tab3.add(cmbGSTIN);
        cmbGSTIN.setBounds(130, 250, 100, 24);

        txtGSTIN.setEnabled(false);
        txtGSTIN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGSTINFocusLost(evt);
            }
        });
        Tab3.add(txtGSTIN);
        txtGSTIN.setBounds(240, 250, 200, 19);

        txtGSTINDate.setToolTipText("");
        txtGSTINDate.setEnabled(false);
        txtGSTINDate.setNextFocusableComponent(cmbMSMEUAN);
        Tab3.add(txtGSTINDate);
        txtGSTINDate.setBounds(450, 250, 90, 19);

        jLabel42.setText("TIN NO");
        Tab3.add(jLabel42);
        jLabel42.setBounds(50, 100, 47, 15);

        cmbMSMEUAN.setEnabled(false);
        cmbMSMEUAN.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMSMEUANItemStateChanged(evt);
            }
        });
        Tab3.add(cmbMSMEUAN);
        cmbMSMEUAN.setBounds(780, 280, 30, 20);

        lblPanNo1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPanNo1.setText("PAN No. :");
        Tab3.add(lblPanNo1);
        lblPanNo1.setBounds(15, 222, 90, 15);

        txtMSMEUAN.setEnabled(false);
        txtMSMEUAN.setName("ESIREG_NO"); // NOI18N
        txtMSMEUAN.setNextFocusableComponent(txtMSMEDIC);
        txtMSMEUAN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMSMEUANFocusLost(evt);
            }
        });
        Tab3.add(txtMSMEUAN);
        txtMSMEUAN.setBounds(240, 280, 180, 21);

        lblPanNo2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPanNo2.setText("UAN NO:");
        Tab3.add(lblPanNo2);
        lblPanNo2.setBounds(170, 280, 60, 20);

        txtMSMEDIC.setEnabled(false);
        Tab3.add(txtMSMEDIC);
        txtMSMEDIC.setBounds(500, 280, 180, 20);

        chkMSME.setText("MSME");
        chkMSME.setEnabled(false);
        chkMSME.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkMSMEItemStateChanged(evt);
            }
        });
        Tab3.add(chkMSME);
        chkMSME.setBounds(40, 280, 90, 23);

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
        cmdNext5.setBounds(638, 348, 102, 25);

        cmdBack2.setText("<< Previous");
        cmdBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBack2);
        cmdBack2.setBounds(517, 348, 119, 25);

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
        Tab2.add(cmdDeliveryAdd);
        cmdDeliveryAdd.setBounds(203, 131, 74, 19);

        cmdDeliveryRemove.setText("Remove");
        cmdDeliveryRemove.setName("DELIVERY_TERMS"); // NOI18N
        cmdDeliveryRemove.setEnabled(false);
        Tab2.add(cmdDeliveryRemove);
        cmdDeliveryRemove.setBounds(283, 131, 96, 19);

        jLabel45.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel45.setText("Supplier Item Category :");
        Tab2.add(jLabel45);
        jLabel45.setBounds(434, 6, 185, 15);

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
        jScrollPane3.setViewportView(TableL);

        Tab2.add(jScrollPane3);
        jScrollPane3.setBounds(434, 26, 320, 95);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        Tab2.add(cmdAdd);
        cmdAdd.setBounds(575, 127, 86, 25);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setEnabled(false);
        Tab2.add(cmdRemove);
        cmdRemove.setBounds(666, 125, 87, 25);

        jLabel46.setText("Other Terms :");
        Tab2.add(jLabel46);
        jLabel46.setBounds(14, 173, 160, 15);

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
        jScrollPane7.setViewportView(TableO);

        Tab2.add(jScrollPane7);
        jScrollPane7.setBounds(11, 191, 365, 91);

        cmdOtherAdd.setText("Add");
        cmdOtherAdd.setName("OTHER_TERMS"); // NOI18N
        cmdOtherAdd.setEnabled(false);
        Tab2.add(cmdOtherAdd);
        cmdOtherAdd.setBounds(179, 286, 74, 19);

        cmdOtherRemove.setText("Remove");
        cmdOtherRemove.setName("OTHER_TERMS"); // NOI18N
        cmdOtherRemove.setEnabled(false);
        Tab2.add(cmdOtherRemove);
        cmdOtherRemove.setBounds(259, 286, 96, 19);

        jTabbedPane1.addTab("Other Information", Tab2);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        groupChild.add(opgParent);
        opgParent.setSelected(true);
        opgParent.setText("Parent Supplier :");
        opgParent.setEnabled(false);
        opgParent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opgParentMouseClicked(evt);
            }
        });
        jPanel4.add(opgParent);
        opgParent.setBounds(14, 14, 145, 23);

        txtPSuppID.setName("SUPPLIER_CODE"); // NOI18N
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
        jPanel4.add(txtPSuppID);
        txtPSuppID.setBounds(21, 45, 120, 19);

        txtPSuppName.setName("SUPP_NAME"); // NOI18N
        txtPSuppName.setEnabled(false);
        jPanel4.add(txtPSuppName);
        txtPSuppName.setBounds(146, 45, 322, 19);

        groupChild.add(opgChild);
        opgChild.setText("Sister Concerns :");
        opgChild.setEnabled(false);
        opgChild.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opgChildMouseClicked(evt);
            }
        });
        jPanel4.add(opgChild);
        opgChild.setBounds(15, 79, 149, 23);

        txtCSuppID.setName("SUPPLIER_CODE"); // NOI18N
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
        jPanel4.add(txtCSuppID);
        txtCSuppID.setBounds(21, 102, 120, 19);

        txtCSuppName.setName("SUPP_NAME"); // NOI18N
        txtCSuppName.setEnabled(false);
        jPanel4.add(txtCSuppName);
        txtCSuppName.setBounds(146, 102, 322, 19);

        cmdAddC.setText("Add");
        cmdAddC.setEnabled(false);
        cmdAddC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddCActionPerformed(evt);
            }
        });
        jPanel4.add(cmdAddC);
        cmdAddC.setBounds(274, 134, 88, 25);

        cmdRemoveC.setText("Remove");
        cmdRemoveC.setEnabled(false);
        cmdRemoveC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveCActionPerformed(evt);
            }
        });
        jPanel4.add(cmdRemoveC);
        cmdRemoveC.setBounds(373, 134, 88, 25);

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
        jScrollPane8.setViewportView(TableSC);

        jPanel4.add(jScrollPane8);
        jScrollPane8.setBounds(21, 172, 444, 185);

        jTabbedPane1.addTab("Sister Concerns", jPanel4);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(null);

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

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(12, 45, 510, 149);

        jLabel20.setText("Updation History List :");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(12, 23, 156, 15);

        cmdShowRemarksU.setText("Show Remarks");
        cmdShowRemarksU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksUActionPerformed(evt);
            }
        });
        jPanel2.add(cmdShowRemarksU);
        cmdShowRemarksU.setBounds(528, 48, 132, 24);

        jTabbedPane1.addTab("History", jPanel2);

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(null);

        jLabel1.setText("Main Account Code :");
        jPanel7.add(jLabel1);
        jLabel1.setBounds(13, 20, 130, 15);

        txtMainAccountCode.setEnabled(false);
        jPanel7.add(txtMainAccountCode);
        txtMainAccountCode.setBounds(144, 18, 340, 19);

        jLabel40.setText("Note: Use comma to separate multiple main account codes. e.g.  125019,125033 ");
        jPanel7.add(jLabel40);
        jLabel40.setBounds(15, 46, 530, 15);

        jTabbedPane1.addTab("Finance", jPanel7);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setToolTipText("Supplier Approval");
        jPanel3.setLayout(null);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        jPanel3.add(jLabel31);
        jLabel31.setBounds(10, 40, 100, 15);

        cmbHierarchy.setEditable(true);
        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbHierarchy);
        cmbHierarchy.setBounds(115, 40, 270, 24);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        jPanel3.add(jLabel32);
        jLabel32.setBounds(10, 70, 100, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFrom);
        txtFrom.setBounds(115, 70, 270, 19);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        jPanel3.add(jLabel35);
        jLabel35.setBounds(10, 100, 100, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFromRemarks);
        txtFromRemarks.setBounds(115, 100, 518, 19);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        jPanel3.add(jLabel36);
        jLabel36.setBounds(10, 145, 100, 15);

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
        jPanel6.setBounds(115, 140, 182, 100);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        jPanel3.add(jLabel33);
        jLabel33.setBounds(10, 250, 100, 15);

        cmbSendTo.setEnabled(false);
        jPanel3.add(cmbSendTo);
        cmbSendTo.setBounds(115, 250, 270, 24);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        jPanel3.add(jLabel34);
        jLabel34.setBounds(10, 280, 100, 15);

        txtToRemarks.setEnabled(false);
        jPanel3.add(txtToRemarks);
        txtToRemarks.setBounds(115, 280, 516, 19);

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
        cmdViewHistory.setBounds(570, 206, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        jPanel5.add(cmdNormalView);
        cmdNormalView.setBounds(570, 238, 132, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        jPanel5.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(570, 272, 132, 24);

        txtAuditRemarks.setEnabled(false);
        jPanel5.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(570, 304, 129, 19);

        jTabbedPane1.addTab("Status", jPanel5);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(5, 70, 850, 590);
    }// </editor-fold>//GEN-END:initComponents

    private void txtGSTINFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTINFocusLost
    if(txtGSTIN.getText().length() !=15)
        {
            JOptionPane.showMessageDialog(null, "GSTIN number must be 15 character");
            txtGSTIN.requestFocus();
    }         // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTINFocusLost
    
    private void cmbPANItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPANItemStateChanged
        // TODO add your handling code here:
        String Selection=EITLERPGLOBAL.getCombostrCode(cmbPAN);
        
        if(Selection.trim().equals("N/A")) {
            txtPAN.setText("N/A");
            txtPAN.setEnabled(false);
            txtPANDate.setEnabled(false);
        }
        
        if(Selection.trim().equals("APPLIED")) {
            txtPAN.setText("APPLIED");
            txtPAN.setEnabled(false);
            txtPANDate.setEnabled(false);
        }
        
        if(Selection.trim().equals("Enter")) {
            txtPAN.setText("");
            txtPAN.setText("");
            txtPAN.setEnabled(true);
            txtPANDate.setEnabled(true);
        }
    }//GEN-LAST:event_cmbPANItemStateChanged
    
    private void cmdRemoveCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveCActionPerformed
        // TODO add your handling code here:
        if(TableSC.getRowCount()>0) {
            DataModelSC.removeRow(TableSC.getSelectedRow());
        }
        
    }//GEN-LAST:event_cmdRemoveCActionPerformed
    
    private void cmdAddCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddCActionPerformed
        // TODO add your handling code here:
        if(txtCSuppID.getText().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter supplier code");
            return;
        }
        
        if(txtCSuppID.getText().trim().equals(txtSuppCode.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Supplier code should be different than current supplier code");
            return;
        }
        
        
        int SuppID=clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID, txtCSuppID.getText());
        
        try {
            //check the duplication
            for(int i=0;i<TableSC.getRowCount();i++) {
                String SuppCode=(String)TableSC.getValueAt(i, 1);
                if(SuppCode.trim().equals(txtCSuppID.getText().trim())) {
                    JOptionPane.showMessageDialog(null,"Supplier code already exist");
                    return;
                }
                
            }
            
            Object[] rowData=new Object[4];
            rowData[0]=Integer.toString(TableSC.getRowCount()+1);
            rowData[1]=txtCSuppID.getText();
            rowData[2]=clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,txtCSuppID.getText());
            rowData[3]=Integer.toString(SuppID);
            DataModelSC.addRow(rowData);
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_cmdAddCActionPerformed
    
    private void opgChildMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opgChildMouseClicked
        // TODO add your handling code here:
        if(opgChild.isSelected()) {
            txtPSuppID.setEnabled(false);
            txtCSuppID.setEnabled(true);
            cmdAddC.setEnabled(true);
            cmdRemoveC.setEnabled(true);
        }
        
    }//GEN-LAST:event_opgChildMouseClicked
    
    private void txtCSuppIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCSuppIDFocusLost
        // TODO add your handling code here:
        if(!txtCSuppID.getText().trim().equals("")) {
            txtCSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtCSuppID.getText()));
        }
        
    }//GEN-LAST:event_txtCSuppIDFocusLost
    
    private void txtCSuppIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCSuppIDKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND BLOCKED='N' AND APPROVED=1 AND ST35_REGISTERED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtCSuppID.setText(aList.ReturnVal);
                txtCSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
        //=========================================
        
    }//GEN-LAST:event_txtCSuppIDKeyPressed
    
    private void txtPSuppIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPSuppIDFocusLost
        // TODO add your handling code here:
        if(!txtPSuppID.getText().trim().equals("")) {
            txtPSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtPSuppID.getText()));
        }
        
    }//GEN-LAST:event_txtPSuppIDFocusLost
    
    private void txtPSuppIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPSuppIDKeyPressed
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
                txtPSuppID.setText(aList.ReturnVal);
                txtPSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
        //=========================================
        
    }//GEN-LAST:event_txtPSuppIDKeyPressed
    
    private void opgParentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opgParentMouseClicked
        // TODO add your handling code here:
        if(opgParent.isSelected()) {
            txtPSuppID.setEnabled(true);
            txtCSuppID.setEnabled(false);
            cmdAddC.setEnabled(false);
            cmdRemoveC.setEnabled(false);
        }
        
    }//GEN-LAST:event_opgParentMouseClicked
    
    private void cmdShowRemarksUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksUActionPerformed
        // TODO add your handling code here:
        if(TableU.getRowCount()>0&&TableU.getSelectedRow()>=0) {
            txtAuditRemarks.setText((String)TableU.getValueAt(TableU.getSelectedRow(),2));
            BigEdit bigEdit=new BigEdit();
            bigEdit.theText=txtAuditRemarks;
            bigEdit.ShowEdit();
        }
        
    }//GEN-LAST:event_cmdShowRemarksUActionPerformed
    
    private void cmbESIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbESIItemStateChanged
        // TODO add your handling code here:
        String Selection=EITLERPGLOBAL.getCombostrCode(cmbESI);
        
        if(Selection.trim().equals("N/A")) {
            txtESI.setText("N/A");
            txtESI.setEnabled(false);
            txtESIDate.setEnabled(false);
        }
        
        
        if(Selection.trim().equals("APPLIED")) {
            txtESI.setText("APPLIED");
            txtESI.setEnabled(false);
            txtESIDate.setEnabled(false);
            
        }
        
        if(Selection.trim().equals("Enter")) {
            txtESI.setText("");
            txtESI.setText("");
            txtESI.setEnabled(true);
            txtESIDate.setEnabled(true);
        }
        
    }//GEN-LAST:event_cmbESIItemStateChanged
    
    private void cmbSSIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSSIItemStateChanged
        // TODO add your handling code here:
        
        String Selection=EITLERPGLOBAL.getCombostrCode(cmbSSI);
        
        if(Selection.trim().equals("N/A")) {
            txtSSI.setText("N/A");
            txtSSIDate.setText("");
            txtSSI.setEnabled(false);
            txtSSIDate.setEnabled(false);
        }
        
        
        if(Selection.trim().equals("APPLIED")) {
            txtSSI.setText("APPLIED");
            txtSSIDate.setText("");
            txtSSI.setEnabled(false);
            txtSSIDate.setEnabled(false);
            
        }
        
        if(Selection.trim().equals("Enter")) {
            txtSSI.setText("");
            txtSSI.setText("");
            txtSSI.setEnabled(true);
            txtSSIDate.setEnabled(true);
        }
        
    }//GEN-LAST:event_cmbSSIItemStateChanged
    
    private void cmbECCItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbECCItemStateChanged
        // TODO add your handling code here:
        
        String Selection=EITLERPGLOBAL.getCombostrCode(cmbECC);
        
        if(Selection.trim().equals("N/A")) {
            txtECC.setText("N/A");
            txtECCDate.setText("");
            txtECC.setEnabled(false);
            txtECCDate.setEnabled(false);
        }
        
        
        if(Selection.trim().equals("APPLIED")) {
            txtECC.setText("APPLIED");
            txtECCDate.setText("");
            txtECC.setEnabled(false);
            txtECCDate.setEnabled(false);
        }
        
        if(Selection.trim().equals("Enter")) {
            txtECC.setText("");
            txtECC.setText("");
            txtECC.setEnabled(true);
            txtECCDate.setEnabled(true);
        }
        
    }//GEN-LAST:event_cmbECCItemStateChanged
    
    private void cmbGSTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbGSTItemStateChanged
        // TODO add your handling code here:
        String Selection=EITLERPGLOBAL.getCombostrCode(cmbGST);
        
        if(Selection.trim().equals("N/A")) {
            txtGST.setText("N/A");
            txtGSTDate.setText("");
            txtGST.setEnabled(false);
            txtGSTDate.setEnabled(false);
        }
        
        
        if(Selection.trim().equals("APPLIED")) {
            txtGST.setText("APPLIED");
            txtGSTDate.setText("");
            txtGST.setEnabled(false);
            txtGSTDate.setEnabled(false);
            
        }
        
        if(Selection.trim().equals("Enter")) {
            txtGST.setText("");
            txtGST.setText("");
            txtGST.setEnabled(true);
            txtGSTDate.setEnabled(true);
        }
        
    }//GEN-LAST:event_cmbGSTItemStateChanged
    
    private void cmbCSTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCSTItemStateChanged
        // TODO add your handling code here:
        String Selection=EITLERPGLOBAL.getCombostrCode(cmbCST);
        
        if(Selection.trim().equals("N/A")) {
            txtCST.setText("N/A");
            txtCSTDate.setText("");
            txtCST.setEnabled(false);
            txtCSTDate.setEnabled(false);
        }
        
        
        if(Selection.trim().equals("APPLIED")) {
            txtCST.setText("APPLIED");
            txtCSTDate.setText("");
            txtCST.setEnabled(false);
            txtCSTDate.setEnabled(false);
            
        }
        
        if(Selection.trim().equals("Enter")) {
            txtCST.setText("");
            txtCST.setText("");
            txtCST.setEnabled(true);
            txtCSTDate.setEnabled(true);
        }
        
    }//GEN-LAST:event_cmbCSTItemStateChanged
    
    private void cmbServiceTaxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbServiceTaxItemStateChanged
        // TODO add your handling code here:
        String Selection=EITLERPGLOBAL.getCombostrCode(cmbServiceTax);
        
        if(Selection.trim().equals("N/A")) {
            txtSTNO.setText("N/A");
            txtSTDate.setText("");
            txtSTNO.setEnabled(false);
            txtSTDate.setEnabled(false);
        }
        
        
        if(Selection.trim().equals("APPLIED")) {
            txtSTNO.setText("APPLIED");
            txtSTDate.setText("");
            txtSTNO.setEnabled(false);
            txtSTDate.setEnabled(false);
            
        }
        
        if(Selection.trim().equals("Enter")) {
            txtSTNO.setText("");
            txtSTNO.setText("");
            txtSTNO.setEnabled(true);
            txtSTDate.setEnabled(true);
        }
        
    }//GEN-LAST:event_cmbServiceTaxItemStateChanged
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if(TableP.getRowCount()>0&&TableP.getSelectedRow()>=0) {
            DataModelP.removeRow(TableP.getSelectedRow());
        }
    }//GEN-LAST:event_jButton4ActionPerformed
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int PayTermCode=EITLERPGLOBAL.getComboCode(cmbPayment);
        
        if(PayTermCode==2 || PayTermCode==6 || PayTermCode==7) {
            if(!EITLERPGLOBAL.IsNumber(txtPayDays.getText())||txtPayDays.getText().equals("0")||txtPayDays.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,"Please Insert proper days.");
                return;
            }
        }
        
        String PayTerm=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"PAYMENT_CODE",PayTermCode);
        
        if(PayTermCode==1) {
            PayTerm="THROUGH "+txtBankName.getText()+" BANK";
        }
        
        if(PayTermCode==6) {
            PayTerm="CREDIT TERMS WITH "+txtPayDays.getText()+" DAYS";
        }
        
        if(PayTermCode==7) {
            PayTerm="CREDIT TERMS WITH "+txtPayDays.getText()+" DAYS, THROUGH BOMBAY OFFICE";
        }
        
        if(PayTermCode==2) {
            PayTerm="SIGHT DRAFT/HUNDI WITH "+txtPayDays.getText()+" DAYS";
        }
        
        Object[] rowData=new Object[4];
        rowData[0]=Integer.toString(TableP.getRowCount()+1);
        rowData[1]=PayTerm;
        rowData[2]=txtPayDays.getText();
        rowData[3]=Integer.toString(PayTermCode);
        DataModelP.addRow(rowData);
        txtPayDays.setText("0");
    }//GEN-LAST:event_jButton3ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtAmendReason;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void cmdShowOriginalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowOriginalActionPerformed
        // TODO add your handling code here:
        try {
            String SupplierCode=txtSuppCode.getText();
            int SuppID=clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID,SupplierCode);
            AppletFrame aFrame=new AppletFrame("Supplier");
            aFrame.startAppletEx("EITLERP.frmSupplier","Supplier");
            frmSupplier ObjItem=(frmSupplier) aFrame.ObjApplet;
            ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,Integer.toString(SuppID));
        }
        catch(Exception e){}
    }//GEN-LAST:event_cmdShowOriginalActionPerformed
    
    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if(TableHS.getRowCount()>0&&TableHS.getSelectedRow()>=0) {
            txtAuditRemarks.setText((String)TableHS.getValueAt(TableHS.getSelectedRow(),4));
            BigEdit bigEdit=new BigEdit();
            bigEdit.theText=txtAuditRemarks;
            bigEdit.ShowEdit();
        }
        
    }//GEN-LAST:event_cmdShowRemarksActionPerformed
    
    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        // TODO add your handling code here:
        SetupApproval();
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if(ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID,50,txtAmendID.getText())) {
                cmbSendTo.setEnabled(true);
            }
            else {
                if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
                    cmbSendTo.setEnabled(true);
                }
                else {
                    cmbSendTo.setEnabled(false);
                }
                //cmbSendTo.setEnabled(false);
            }
        }
        
        if(cmbSendTo.getItemCount()<=0) {
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
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtItem;
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
        
        int PayCode=EITLERPGLOBAL.getComboCode(cmbPayment);
        
        if(PayCode==1) //Through Bank
        {
            lblBankID.setVisible(true);
            txtBankID.setVisible(true);
            txtBankName.setVisible(true);
        }
        
        if(PayCode==6||PayCode==2||PayCode==7) {
            lblPaymentDays.setVisible(true);
            txtPayDays.setVisible(true);
        }
        
    }//GEN-LAST:event_cmbPaymentItemStateChanged
    
    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        // TODO add your handling code here:
        if(!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked
    
    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjSupplierAmend.LoadData(EITLERPGLOBAL.gCompanyID);
        MoveFirst();
    }//GEN-LAST:event_cmdNormalViewActionPerformed
    
    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo=txtSuppCode.getText();
        int SuppID=(int)ObjSupplierAmend.getAttribute("AMEND_ID").getVal();
        ObjSupplierAmend.ShowHistory(EITLERPGLOBAL.gCompanyID, SuppID);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed
    
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
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        
        if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            
            cmbSendTo.setEnabled(false);
        }
        
        if(clsHierarchy.CanFinalApprove((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            if(EditMode==EITLERPGLOBAL.ADD || EditMode==EITLERPGLOBAL.EDIT){
            OpgFinal.setEnabled(true);
            }
        }
        else {
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
        ObjSupplierAmend.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void txtBankIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBankIDKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT BANK_ID,BANK_NAME FROM D_COM_BANK_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY BANK_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtBankID.setText(aList.ReturnVal);
                txtBankName.setText(clsBank.getBankName((long)EITLERPGLOBAL.gCompanyID, Long.parseLong(aList.ReturnVal)));
            }
        }
    }//GEN-LAST:event_txtBankIDKeyPressed
    
    private void txtBankIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankIDFocusLost
        // TODO add your handling code here:
        if(!txtBankID.getText().trim().equals("")) {
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
    
    private void chkOneTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOneTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkOneTimeActionPerformed
    
    private void chkSlowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSlowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSlowActionPerformed
    
    private void txtPayDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPayDaysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPayDaysActionPerformed

    private void cmbGSTINItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbGSTINItemStateChanged
         String Selection=EITLERPGLOBAL.getCombostrCode(cmbGSTIN);
        
        if(Selection.trim().equals("N/A")) {
            txtGSTIN.setText("N/A");
            txtGSTINDate.setText("");
            txtGSTIN.setEnabled(false);
            txtGSTINDate.setEnabled(false);
        }
        
        
        if(Selection.trim().equals("APPLIED")) {
            txtGSTIN.setText("APPLIED");
            txtGSTINDate.setText("");
            txtGSTIN.setEnabled(false);
            txtGSTINDate.setEnabled(false);
            
        }
        
        if(Selection.trim().equals("Enter")) {
            txtGSTIN.setText("");
            txtGSTIN.setText("");
            txtGSTIN.setEnabled(true);
            txtGSTINDate.setEnabled(true);
        }
        
    }//GEN-LAST:event_cmbGSTINItemStateChanged

    private void cmbStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStateActionPerformed
        String st1,dt1;
    st1 = "SELECT STATE_CODE FROM DINESHMILLS.D_SAL_STATE_MASTER WHERE STATE_NAME='"+cmbState.getSelectedItem().toString()+"'"; 
    txtStateCode.setText(data.getStringValueFromDB(st1));
    dt1 = "SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_STATE_MASTER WHERE STATE_NAME='"+cmbState.getSelectedItem().toString()+"'"; 
    txtStateGstCode.setText(data.getStringValueFromDB(dt1));
    // TODO add your handling code here:
    }//GEN-LAST:event_cmbStateActionPerformed

    private void cmbMSMEUANItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMSMEUANItemStateChanged
            String Selection=EITLERPGLOBAL.getCombostrCode(cmbMSMEUAN);
        
        if(Selection.trim().equals("N/A")) {
            txtMSMEUAN.setText("N/A");
            txtMSMEUAN.setEnabled(false);
            
        }
        
        
        if(Selection.trim().equals("APPLIED")) {
            txtMSMEUAN.setText("APPLIED");
            txtMSMEUAN.setEnabled(false);
            
            
        }
        
        if(Selection.trim().equals("Enter")) {
            txtMSMEUAN.setText("");
            txtMSMEUAN.setEnabled(true);
            
        }
    }//GEN-LAST:event_cmbMSMEUANItemStateChanged

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
    private javax.swing.JTable TableU;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkBlocked;
    private javax.swing.JCheckBox chkCancelled;
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
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowOriginal;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdShowRemarksU;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.ButtonGroup groupChild;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblBankID;
    private javax.swing.JLabel lblPanNo;
    private javax.swing.JLabel lblPanNo1;
    private javax.swing.JLabel lblPanNo2;
    private javax.swing.JLabel lblPaymentDays;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JRadioButton opgChild;
    private javax.swing.JRadioButton opgParent;
    private javax.swing.JTextField txtAdd1;
    private javax.swing.JTextField txtAdd2;
    private javax.swing.JTextField txtAdd3;
    private javax.swing.JTextField txtAmendDate;
    private javax.swing.JTextField txtAmendID;
    private javax.swing.JTextField txtAmendReason;
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
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        
        String SupplierCode="";
        LOV aList=new LOV();
        
        aList.SQL="SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 ORDER BY SUPP_NAME";
        aList.ReturnCol=1;
        aList.ShowReturnCol=true;
        aList.DefaultSearchOn=2;
        
        if(aList.ShowLOV()) {
            SupplierCode=aList.ReturnVal;
            txtName.setText(SupplierCode);
            /*if(!CheckForAmendment(SupplierCode)) {
                return;
            }*/
            
            EditMode=EITLERPGLOBAL.ADD;
            
            SetFields(true);
            
            DisableToolbar();
            
            ClearFields();
            
            SetupApproval();
            
            //txtAmendDate.setText(EITLERPGLOBAL.getCurrentDate());
            
            clsSupplier ObjSupplier=(clsSupplier)clsSupplier.getObjectEx(EITLERPGLOBAL.gCompanyID, SupplierCode);
            
            DisplaySuppData(ObjSupplier);
            lblTitle.setBackground(Color.BLUE);
            txtAmendDate.setText(EITLERPGLOBAL.getCurrentDate());
        }
        else {
            JOptionPane.showMessageDialog(null,"You must select Supplier ");
        }
        
    }
    
    private boolean CheckForAmendment(String pSuppID) {
        
        ResultSet rsTmp=null;
        String strSQL="";
        String strMessage="";
        boolean proceed=true;
        
        try {
            
            //Check whether any document is under approval having this supplier code.
            strSQL="SELECT PO_NO FROM D_PUR_PO_HEADER WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nPO No. "+rsTmp.getString("PO_NO")+" is under approval.";
                proceed=false;
            }
            
            
            strSQL="SELECT AMEND_NO FROM D_PUR_AMEND_HEADER WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nAmendment No. "+rsTmp.getString("AMEND_NO")+" is under approval.";
                proceed=false;
            }
            
            strSQL="SELECT DOC_NO FROM D_PUR_FREIGHT_COMPARISON WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nFreight comparison No. "+rsTmp.getString("DOC_NO")+" is under approval.";
                proceed=false;
            }
            
            
            strSQL="SELECT DOC_NO FROM D_PUR_FREIGHT_CALCULATION WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nFreight calculation No. "+rsTmp.getString("DOC_NO")+" is under approval.";
                proceed=false;
            }
            
            
            strSQL="SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nMIR No. "+rsTmp.getString("MIR_NO")+" is under approval.";
                proceed=false;
            }
            
            strSQL="SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nGRN No. "+rsTmp.getString("GRN_NO")+" is under approval.";
                proceed=false;
            }
            
            strSQL="SELECT RJN_NO FROM D_INV_RJN_HEADER WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nRJN No. "+rsTmp.getString("RJN_NO")+" is under approval.";
                proceed=false;
            }
            
            
            strSQL="SELECT RJN_NO FROM D_INV_RJN_HEADER WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nRJN No. "+rsTmp.getString("RJN_NO")+" is under approval.";
                proceed=false;
            }
            
            strSQL="SELECT JOB_NO FROM D_INV_JOB_HEADER WHERE APPROVED=0 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strMessage=strMessage+"\nJobwork No. "+rsTmp.getString("JOB_NO")+" is under approval.";
                proceed=false;
            }
            
            
            if(!proceed) {
                JOptionPane.showMessageDialog(null,"Cannot amend this supplier "+strMessage);
            }
            
        }
        catch(Exception e) {
            proceed=false;
        }
        
        return proceed;
    }
    
    private void SetupApproval() {
        
        boolean canChangePaymentTerm=clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID,50, 937);
        
        /*if(cmbHierarchy.getItemCount()>1) {
            cmbHierarchy.setEnabled(true);
        }*/
        //In Edit Mode Hierarchy Should be disabled
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
            
            int FromUserID=ApprovalFlow.getFromID((int)EITLERPGLOBAL.gCompanyID,50,(String)ObjSupplierAmend.getAttribute("AMEND_ID").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=ApprovalFlow.getFromRemarks( (int)EITLERPGLOBAL.gCompanyID,50,FromUserID,(String)ObjSupplierAmend.getAttribute("AMEND_ID").getObj());
            
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
        
        //Grant final approval to only Mr. Nimish Patel or Mr. A. B. Tewary
        boolean PaymentTermChanged=false;
        
        //Check whether payment term is changed or not
        clsSupplier ObjSupp=(clsSupplier)clsSupplier.getObjectEx(EITLERPGLOBAL.gCompanyID,txtSuppCode.getText());
        
        for(int i=0;i<TableP.getRowCount();i++) {
            String PaymentTerm=(String)TableP.getValueAt(i,1);
            
            //Find this payment term in Supplier object
            for(int j=1;j<=ObjSupp.colSuppTerms.size();j++) {
                clsSuppTerms ObjTerm=(clsSuppTerms)ObjSupp.colSuppTerms.get(Integer.toString(j));
                
                if(((String)ObjTerm.getAttribute("TERM_TYPE").getObj()).trim().equals("P")) {
                    if(((String)ObjTerm.getAttribute("TERM_DESC").getObj()).trim().equals(PaymentTerm)) {
                        //Found
                    }
                    else {
                        //Not Found
                        PaymentTermChanged=true;
                    }
                }
                
            }
        }
        
        
        /*if(PaymentTermChanged && (!canChangePaymentTerm)) {
            OpgFinal.setEnabled(false);
        }
        else {
            OpgFinal.setEnabled(true);
        }*/
        
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
                
                String DocNo=Integer.toString((int)ObjSupplierAmend.getAttribute("AMEND_ID").getVal());
                
                List=ApprovalFlow.getRemainingUsers((int)EITLERPGLOBAL.gCompanyID, 50,DocNo);
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
        //txtAmendDate.setEnabled(pStat);
        txtAmendReason.setEnabled(pStat);
        //txtSuppCode.setEnabled(pStat);
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
        txtECC.setEnabled(pStat);
        txtECCDate.setEnabled(pStat);
        txtSSI.setEnabled(pStat);
        txtSSIDate.setEnabled(pStat);
        txtESI.setEnabled(pStat);
        txtESIDate.setEnabled(pStat);
        txtPlaceofsupply.setEnabled(pStat);
        txtCP1.setEnabled(pStat);
        txtCP2.setEnabled(pStat);
        txtBankID.setEnabled(pStat);
        txtPayDays.setEnabled(pStat);
        //txtLastTran.setEnabled(pStat);
        txtRegFrom.setEnabled(pStat);
        txtRegTo.setEnabled(pStat);
        //txtPO.setEnabled(pStat);
        txtMainAccountCode.setEnabled(pStat);
        chkMSME.setEnabled(pStat); 
        //txtMSMEUAN.setEnabled(pStat);
        //txtMSMEDIC.setEnabled(pStat); 
        txtDistanceKm.setEnabled(pStat);
        
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
        
        cmbServiceTax.setEnabled(pStat);
        cmbCST.setEnabled(pStat);
        cmbGST.setEnabled(pStat);
        cmbGSTIN.setEnabled(pStat);
        
        cmbECC.setEnabled(pStat);
        cmbSSI.setEnabled(pStat);
        cmbESI.setEnabled(pStat);
        cmbPAN.setEnabled(pStat);
        cmbMSMEUAN.setEnabled(pStat); 
        SetupApproval();
        
        //        DataModelH.TableReadOnly(!pStat);
        DataModelL.TableReadOnly(!pStat);
        //DataModelP.TableReadOnly(!pStat);
        DataModelD.TableReadOnly(!pStat);
        DataModelO.TableReadOnly(!pStat);
        
        opgParent.setEnabled(pStat);
        opgChild.setEnabled(pStat);
        cmdAddC.setEnabled(pStat);
        cmdRemoveC.setEnabled(pStat);
        
        
        // ============ Service Tax ===================//
        if(txtSTNO.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbServiceTax,"APPLIED");
            txtSTDate.setText("");
            txtSTNO.setEnabled(false);
            txtSTDate.setEnabled(false);
        }
        else {
            if(txtSTNO.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbServiceTax,"N/A");
                txtSTDate.setText("");
                txtSTNO.setEnabled(false);
                txtSTDate.setEnabled(false);
            }
            else {
                EITLERPGLOBAL.setComboIndex(cmbServiceTax,"Enter");
                txtSTNO.setEnabled(true);
                txtSTDate.setEnabled(true);
            }
        }
        //===========================================//
        
        
        
        // ============ CST ===================//
        if(txtCST.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbCST,"APPLIED");
            txtCSTDate.setText("");
            txtCST.setEnabled(false);
            txtCSTDate.setEnabled(false);
        }
        else {
            if(txtCST.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbCST,"N/A");
                txtCSTDate.setText("");
                txtCST.setEnabled(false);
                txtCSTDate.setEnabled(false);
            }
            else {
                EITLERPGLOBAL.setComboIndex(cmbCST,"Enter");
                txtCST.setEnabled(true);
                txtCSTDate.setEnabled(true);
            }
        }
        //===========================================//
        
        
        // ============ GST ===================//
        if(txtGST.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbGST,"APPLIED");
            txtGSTDate.setText("");
            txtGST.setEnabled(false);
            txtGSTDate.setEnabled(false);
        }
        else {
            if(txtGST.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbGST,"N/A");
                txtGSTDate.setText("");
                txtGST.setEnabled(false);
                txtGSTDate.setEnabled(false);
            }
            else {
                EITLERPGLOBAL.setComboIndex(cmbGST,"Enter");
                txtGST.setEnabled(true);
                txtGSTDate.setEnabled(true);
            }
        }
        //===========================================//
        
        // ============ GSTIN ===================//
        if(txtGSTIN.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbGSTIN,"APPLIED");
            txtGSTINDate.setText("");
            txtGSTIN.setEnabled(false);
            txtGSTINDate.setEnabled(false);
        }
        else {
            if(txtGSTIN.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbGSTIN,"N/A");
                txtGSTINDate.setText("");
                txtGSTIN.setEnabled(false);
                txtGSTINDate.setEnabled(false);
            }
            else {
                EITLERPGLOBAL.setComboIndex(cmbGSTIN,"Enter");
                txtGSTIN.setEnabled(true);
                txtGSTINDate.setEnabled(true);
            }
        }
        //===========================================//
        
        
        // ============ ECC ===================//
        if(txtECC.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbECC,"APPLIED");
            txtECCDate.setText("");
            txtECC.setEnabled(false);
            txtECCDate.setEnabled(false);
        }
        else {
            if(txtECC.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbECC,"N/A");
                txtECCDate.setText("");
                txtECC.setEnabled(false);
                txtECCDate.setEnabled(false);
            }
            else {
                EITLERPGLOBAL.setComboIndex(cmbECC,"Enter");
                txtECC.setEnabled(true);
                txtECCDate.setEnabled(true);
            }
        }
        //===========================================//
        
        
        // ============ SSI ===================//
        if(txtSSI.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbSSI,"APPLIED");
            txtSSIDate.setText("");
            txtSSI.setEnabled(false);
            txtSSIDate.setEnabled(false);
        }
        else {
            if(txtSSI.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbSSI,"N/A");
                txtSSIDate.setText("");
                txtSSI.setEnabled(false);
                txtSSIDate.setEnabled(false);
            }
            else {
                EITLERPGLOBAL.setComboIndex(cmbSSI,"Enter");
                txtSSI.setEnabled(true);
                txtSSIDate.setEnabled(true);
            }
        }
        //===========================================//
        
        
        
        // ============ ESI ===================//
        if(txtESI.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbESI,"APPLIED");
            txtESIDate.setText("");
            txtESI.setEnabled(false);
            txtESIDate.setEnabled(false);
        } else {
            if(txtESI.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbESI,"N/A");
                txtESIDate.setText("");
                txtESI.setEnabled(false);
                txtESIDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbESI,"Enter");
                txtESI.setEnabled(true);
                txtESIDate.setEnabled(true);
            }
        }
        //===========================================//
        
        
        // ============ PAN ===================//
        if(txtPAN.getText().equals("APPLIED")) {
            EITLERPGLOBAL.setComboIndex(cmbPAN,"APPLIED");
            txtPANDate.setText("");
            txtPAN.setEnabled(false);
            txtPANDate.setEnabled(false);
        } else {
            if(txtPAN.getText().equals("N/A")) {
                EITLERPGLOBAL.setComboIndex(cmbPAN,"N/A");
                txtPANDate.setText("");
                txtPAN.setEnabled(false);
                txtPANDate.setEnabled(false);
            } else {
                EITLERPGLOBAL.setComboIndex(cmbPAN,"Enter");
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
        int ValidEntryCount=0;
        
        //Search in Table
        int ItemCol=DataModelL.getColFromVariable("CATEGORY_ID");
        
        int PayTerm=EITLERPGLOBAL.getComboCode(cmbPayment);
        
        if(PayTerm==2||PayTerm==6) {
            if(EITLERPGLOBAL.IsNumber(txtPayDays.getText())&&!txtPayDays.getText().equals("0")&&!txtPayDays.getText().trim().equals("")) {
                
            }
            else {
                txtPayDays.setText("0");
            }
        }
        
        /*for(int i=0;i<TableL.getRowCount();i++) {
            String CatID="0";
         
            if(TableL.getValueAt(i, ItemCol)!=null ) {
                CatID=(String)TableL.getValueAt(i, ItemCol);
         
                if(clsItemCategory.IsValidCategoryID(EITLERPGLOBAL.gCompanyID,Long.parseLong(CatID))) {
                    ValidEntryCount++;
                }
                else {
                    JOptionPane.showMessageDialog(null,"Item Category entry is not valid. Please verify Category ID #" + CatID);
                    return false;
                }
            }
        }
         
         
        if(ValidEntryCount==0) {
            JOptionPane.showMessageDialog(null,"Item Category entry is not valid. Please verify");
            return false;
        }*/
        
        
        if(opgParent.isSelected()) {
            //Validate supplier
            
            if(!clsSupplier.IsSupplierExistEx(EITLERPGLOBAL.gCompanyID,txtPSuppID.getText())) {
                JOptionPane.showMessageDialog(null,"Parent supplier code is not valid. Please check whether it is not cancelled/blocked/registered ");
                return false;
            }
        }
        
        
        //Now Header level validations
        if(txtSuppCode.getText().trim().equals("")&&OpgFinal.isSelected()) {
            JOptionPane.showMessageDialog(null,"Please enter Supplier Code");
            txtSuppCode.requestFocus(true);
            return false;
        }
        
        
        
        
        if(txtGST.getText().trim().equals("")&&txtCST.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please either enter GST or CST No. and date");
            return false;
        }
        
        
        
        /*if(!txtDummyCode.getText().trim().equals("")) {
            if (clsSupplier.IsDuplicateSuppCode(EITLERPGLOBAL.gCompanyID,txtDummyCode.getText().trim(),true)) {
                JOptionPane.showMessageDialog(null,"Dummy Supplier Code already exists!!");
                txtDummyCode.requestFocus(true);
                return false;
            }
        }*/
        
        
        if(txtName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Supplier Name");
            return false;
        }
        
        if(txtAdd1.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Address Line 1");
            return false;
        }
        
        if(txtCity.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter City");
            return false;
        }
        
        
        if(!txtSTDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtSTDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid Service Tax Date");
                return false;
            }
        }
        
        
        
        if(chkSSI.isSelected()&&txtSSI.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter SSI Registration no.");
            return false;
        }
        
        if(chkSSI.isSelected()&&txtSSIDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter SSI Registration date");
            return false;
        }
        
        if(chkSSI.isSelected()&&!txtSSIDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtSSIDate.getText())) {
                
                JOptionPane.showMessageDialog(null,"Invalid SSI Registration date");
                return false;
                
            }
        }
        
        
        
        if(txtCST.getText().trim().equals("")) {
            //JOptionPane.showMessageDialog(null,"Please enter CST No");
            //return false;
        }
        
        if(!OpgReject.isSelected()) {
            if(txtPAN.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please enter PAN No.");
                return false;
            }

            if(!txtPANDate.getText().trim().equals("")) {
                if(!EITLERPGLOBAL.isDate(txtPANDate.getText())) {
                    JOptionPane.showMessageDialog(null,"Invalid PAN Date");
                    return false;
                }
            } 
            /*else {
                JOptionPane.showMessageDialog(null,"enter PAN Date");
                return false;
            }*/
        }
        
        if(!txtCSTDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtCSTDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid CST Date");
                return false;
            }
        }
        else {
            //JOptionPane.showMessageDialog(null,"Please enter CST Date");
            //return false;
        }
        
        if(!txtGSTDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtGSTDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid GST Date");
                return false;
            }
        }
        
          if(!txtGSTINDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtGSTINDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid GSTIN Date");
                return false;
            }
        }
        
        if(!txtECCDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtECCDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid ECC Date");
                return false;
            }
        }
        if(!txtSSIDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtSSIDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid SSI Date");
                return false;
            }
        }
        if(!txtESIDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtESIDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid ESI Date");
                return false;
            }
        }
        if(!txtRegFrom.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtRegFrom.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid Registration From Date");
                return false;
            }
        }
        if(!txtRegTo.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtRegTo.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid Registration To Date");
                return false;
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
        
       /* if(!txtBankID.getText().trim().equals(""))
        {
                JOptionPane.showMessageDialog(null,"Please enter valid Bank Code");
                return false;
        
        }
        */
        
        if(OpgReject.isSelected()&&txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter the remarks for rejection");
            return false;
        }
        
        if( (OpgApprove.isSelected()||OpgReject.isSelected())&&cmbSendTo.getItemCount()<=0) {
            JOptionPane.showMessageDialog(null,"Please select the user, to whom rejected document to be send");
            return false;
        }
        
        
        //Validate main account codes
        if(clsUser.getDeptID(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gNewUserID)==16) {
            if(!txtMainAccountCode.getText().trim().equals("")) {
                String MainCodes[]=txtMainAccountCode.getText().split(",");
                for(int i=0;i<MainCodes.length;i++) {
                    String MainCode=MainCodes[i];
                    if(!clsAccount.IsValidAccount(MainCode,"")) {
                        JOptionPane.showMessageDialog(null,"Main Account code "+MainCode+" is not valid. Please verify");
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void ClearFields() {
        chkCancelled.setSelected(false);
        txtAmendID.setText("0");
        txtAmendDate.setText("");
        txtAmendReason.setText("");
        txtSuppCode.setText("");
        txtDummyCode.setText("");
        txtName.setText("");
        txtAttn.setText("");
        txtAdd1.setText("");
        txtAdd2.setText("");
        txtAdd3.setText("");
        txtCity.setText("");
        txtPIN.setText("");
       // txtState.setText("");
       // txtCountry.setText("");
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
        txtPlaceofsupply.setText("");
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
        
        chkOneTime.setSelected(false);
        chkBlocked.setSelected(false);
        chkSlow.setSelected(false);
        chkReg.setSelected(false);
        chkSSI.setSelected(false);
        
        
        txtPSuppID.setText("");
        txtPSuppName.setText("");
        txtCSuppID.setText("");
        txtCSuppName.setText("");
        txtMainAccountCode.setText("");
        
        /*OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);;
        OpgHold.setSelected(true);
        txtToRemarks.setText("");*/
        
        FormatGrid();
        FormatTermsGrid();
        FormatGridA();
        FormatGridHS();
        FormatGridU();
        FormatGridSC();
        
    }
    
    private void Edit() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        String sSuppCode=Long.toString((long)ObjSupplierAmend.getAttribute("AMEND_ID").getVal());
        if(ObjSupplierAmend.IsEditable(EITLERPGLOBAL.gCompanyID, sSuppCode, EITLERPGLOBAL.gNewUserID)) {
            EditMode=EITLERPGLOBAL.EDIT;
            
            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//
            
            if(ApprovalFlow.IsCreator(50,sSuppCode)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,162)) {
                SetFields(true);
            }
            else {
                EnableApproval();
            }
            
            DisplayData();
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
        String sSuppCode=Long.toString((long)ObjSupplierAmend.getAttribute("AMEND_ID").getVal());
        
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this record ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if(ObjSupplierAmend.CanDelete(EITLERPGLOBAL.gCompanyID, sSuppCode, EITLERPGLOBAL.gNewUserID)) {
                if(ObjSupplierAmend.Delete()) {
                    ObjSupplierAmend.LoadData(EITLERPGLOBAL.gCompanyID);
                    MoveLast();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is "+ObjSupplierAmend.LastError);
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"You cannot delete this record. \n It is either approved/rejected record or waiting approval for other user or is referred in other documents");
            }
        }
    }
    
    private void Save() {
        ResultSet rsTmp=null;
        
        //Form level validations
        if(Validate()==false) {
            return; //Validation failed
        }
        
        
        //==========================================================//
        if(EditMode==EITLERPGLOBAL.ADD) {
            int AmendCount=clsSupplierAmend.getUnderApprovalCount(EITLERPGLOBAL.gCompanyID,txtSuppCode.getText());
            if(AmendCount>0) {
                JOptionPane.showMessageDialog(null,"One amendment of this supplier is already under approval. Cannot create another amendment");
                return;
            }
        }
        //=========================================================//
        
        
        SetData();
        
        ObjSupplierAmend.AmendChilds=false;
        
        
        if(OpgFinal.isEnabled()&&OpgFinal.isSelected()) {
            
            try {
                //Get Original block status of this supplier
                rsTmp=data.getResult("SELECT BLOCKED FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+txtSuppCode.getText()+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    boolean oldBlocked=false;
                    int SuppID=0;
                    
                    SuppID=clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID,(String)ObjSupplierAmend.getAttribute("SUPPLIER_CODE").getObj());
                    
                    if(rsTmp.getString("BLOCKED").trim().equals("Y")) {
                        oldBlocked=true;
                    }
                    
                    if(chkBlocked.isSelected()!=oldBlocked) {
                        
                        frmChildSuppInfo objForm=new frmChildSuppInfo();
                        objForm.colCurrentSupp=ObjSupplierAmend.colSuppChilds;
                        
                        
                        //Find if any parent found of this supplier
                        rsTmp=data.getResult("SELECT SUPP_ID FROM D_COM_SUPP_CHILDS WHERE CHILD_SUPP_ID="+SuppID);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            SuppID=rsTmp.getInt("SUPP_ID");
                            
                            //Add this Supplier to list
                            clsSuppChilds objSupp=new clsSuppChilds();
                            objSupp.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            objSupp.setAttribute("SUPP_ID",SuppID);
                            objSupp.setAttribute("CHILD_SUPP_ID",rsTmp.getInt("SUPP_ID"));
                            objForm.colCurrentSupp.put(Integer.toString(objForm.colCurrentSupp.size()+1),objSupp);
                            
                            // Now find each child supplier of this parent supplier
                            rsTmp=data.getResult("SELECT CHILD_SUPP_ID FROM D_COM_SUPP_CHILDS WHERE SUPP_ID="+SuppID);
                            rsTmp.first();
                            
                            while(!rsTmp.isAfterLast()) {
                                objSupp=new clsSuppChilds();
                                objSupp.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                objSupp.setAttribute("SUPP_ID",SuppID);
                                objSupp.setAttribute("CHILD_SUPP_ID",rsTmp.getInt("CHILD_SUPP_ID"));
                                objForm.colCurrentSupp.put(Integer.toString(objForm.colCurrentSupp.size()+1),objSupp);
                                
                                
                                rsTmp.next();
                            }
                            
                        }
                        
                        objForm.ShowList();
                        
                        if(!objForm.Cancelled) {
                            ObjSupplierAmend.AmendChilds=true;
                            ObjSupplierAmend.colBlocked=objForm.colSupp;
                        }
                        
                    }
                    
                }
            }
            catch(Exception d) {
                
            }
            
        }
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(ObjSupplierAmend.Insert()) {
                // MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjSupplierAmend.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjSupplierAmend.Update()) {
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjSupplierAmend.LastError);
                return;
            }
        }
        
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        try {
            frmPA.RefreshView();
        }catch(Exception e) {
        }
        
    }
    
    private void Cancel() {
        DisplayData();
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
    }
    
    private void Find() {
        Loader ObjLoader=new Loader(this,"EITLERP.frmSupplierFind",true);
        frmSupplierFind ObjReturn= (frmSupplierFind) ObjLoader.getObj();
        
        if(ObjReturn.Cancelled==false) {
            if(!ObjSupplierAmend.Filter(ObjReturn.strQuery)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveFirst();
        }
    }
    
    private void MoveFirst() {
        ObjSupplierAmend.MoveFirst();
        DisplayData();
    }
    
    private void MovePrevious() {
        ObjSupplierAmend.MovePrevious();
        DisplayData();
    }
    
    
    private void MoveNext() {
        ObjSupplierAmend.MoveNext();
        DisplayData();
    }
    
    
    private void MoveLast() {
        ObjSupplierAmend.MoveLast();
        DisplayData();
    }
    
    //Didplay data on the Screen
    private void DisplayData() {
        
        //=========== Color Indication ===============//
        try {
            if(EditMode==0) {
                if(ObjSupplierAmend.getAttribute("APPROVED").getInt()==1) {
                    lblTitle.setBackground(Color.BLUE);
                }
                
                if(ObjSupplierAmend.getAttribute("APPROVED").getInt()!=1) {
                    lblTitle.setBackground(Color.GRAY);
                }
                
                if(ObjSupplierAmend.getAttribute("CANCELLED").getInt()==1) {
                    lblTitle.setBackground(Color.RED);
                }
                
            }
        }
        catch(Exception c) {
            
        }
        //============================================//
        
        
        //========= Authority Delegation Check =====================//
        if(EITLERPGLOBAL.gAuthorityUserID!=EITLERPGLOBAL.gUserID) {
            int ModuleID=50;
            
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
            
            boolean bState = false;
            
            txtAmendID.setText(Integer.toString((int)ObjSupplierAmend.getAttribute("AMEND_ID").getVal()));
            txtAmendDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("AMEND_DATE").getObj()));
            txtAmendReason.setText((String)ObjSupplierAmend.getAttribute("AMEND_REASON").getObj());
            
            txtSuppCode.setText((String)ObjSupplierAmend.getAttribute("SUPPLIER_CODE").getObj());
            
            
            lblRevNo.setText(Integer.toString((int)ObjSupplierAmend.getAttribute("REVISION_NO").getVal()));
            txtDummyCode.setText((String)ObjSupplierAmend.getAttribute("DUMMY_SUPPLIER_CODE").getObj());
            txtName.setText((String)ObjSupplierAmend.getAttribute("SUPP_NAME").getObj());
            //txtPODate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplier.getAttribute("PO_DATE").getObj()));
            txtAttn.setText((String)ObjSupplierAmend.getAttribute("ATTN").getObj());
            txtAdd1.setText((String)ObjSupplierAmend.getAttribute("ADD1").getObj());
            txtAdd2.setText((String)ObjSupplierAmend.getAttribute("ADD2").getObj());
            txtAdd3.setText((String)ObjSupplierAmend.getAttribute("ADD3").getObj());
            txtCity.setText((String)ObjSupplierAmend.getAttribute("CITY").getObj());
            txtPIN.setText((String)ObjSupplierAmend.getAttribute("PINCODE").getObj());
            txtStateName.setText((String)ObjSupplierAmend.getAttribute("STATE").getObj());
            txtCountryName.setText((String)ObjSupplierAmend.getAttribute("COUNTRY").getObj());
            //EITLERPGLOBAL.setComboIndex(cmbState, 12);
            //EITLERPGLOBAL.setComboIndex(cmbState,ObjSupplierAmend.getAttribute("STATE").getInt());
            //EITLERPGLOBAL.setComboIndex(cmbCountry,ObjSupplierAmend.getAttribute("COUNTRY").getInt());
            //txtState.setText((String)ObjSupplierAmend.getAttribute("STATE").getObj());
            //txtCountry.setText((String)ObjSupplierAmend.getAttribute("COUNTRY").getObj());
            txtStateCode.setText((String)ObjSupplierAmend.getAttribute("STATE_CODE").getObj());
            txtStateGstCode.setText((String)ObjSupplierAmend.getAttribute("STATE_GST_CODE").getObj());
            txtPhone_O.setText((String)ObjSupplierAmend.getAttribute("PHONE_O").getObj());
            txtPhone_R.setText((String)ObjSupplierAmend.getAttribute("PHONE_RES").getObj());
            txtFax.setText((String)ObjSupplierAmend.getAttribute("FAX_NO").getObj());
            txtMobile.setText((String)ObjSupplierAmend.getAttribute("MOBILE_NO").getObj());
            txtEmail.setText((String)ObjSupplierAmend.getAttribute("EMAIL_ADD").getObj());
            txtWebsite.setText((String)ObjSupplierAmend.getAttribute("WEB_SITE").getObj());
            txtURL.setText((String)ObjSupplierAmend.getAttribute("URL").getObj());
            txtSTNO.setText((String)ObjSupplierAmend.getAttribute("SERVICETAX_NO").getObj());
            txtSTDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("SERVICETAX_DATE").getObj()));
            txtCST.setText((String)ObjSupplierAmend.getAttribute("CST_NO").getObj());
            txtCSTDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("CST_DATE").getObj()));
            txtGST.setText((String)ObjSupplierAmend.getAttribute("GST_NO").getObj());
            txtGSTDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("GST_DATE").getObj()));
            txtGSTIN.setText((String)ObjSupplierAmend.getAttribute("GSTIN_NO").getObj());
            txtGSTINDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("GSTIN_DATE").getObj()));
            txtECC.setText((String)ObjSupplierAmend.getAttribute("ECC_NO").getObj());
            txtECCDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("ECC_DATE").getObj()));
            txtSSI.setText((String)ObjSupplierAmend.getAttribute("SSIREG_NO").getObj());
            txtSSIDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("SSIREG_DATE").getObj()));
            txtESI.setText((String)ObjSupplierAmend.getAttribute("ESIREG_NO").getObj());
            txtESIDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("ESIREG_DATE").getObj()));
            
            txtDistanceKm.setText(Integer.toString(ObjSupplierAmend.getAttribute("DISTANCE_KM").getInt()));
            
            String s1="";
            try{
            s1=ObjSupplierAmend.getAttribute("PAN_NO").getString();
                }
            catch(Exception e){
                s1="";
            }
            txtPAN.setText(s1);
            
            String s4="";
            try{
            
                s4 = EITLERPGLOBAL.formatDate(ObjSupplierAmend.getAttribute("PAN_DATE").getString());
                }
            catch(Exception e){
                s4="";
            }
            txtPANDate.setText(s4);
            chkMSME.setSelected(ObjSupplierAmend.getAttribute("MSME").getBool());
            String MSMEUAN="";
            try{
            MSMEUAN=ObjSupplierAmend.getAttribute("MSME_UAN").getString();
                }
            catch(Exception e){
                MSMEUAN="N/A";
            }
            txtMSMEUAN.setText(MSMEUAN);
            String MSMEDIC="";
            try{
            MSMEDIC=ObjSupplierAmend.getAttribute("MSME_DIC_NO").getString();
                }
            catch(Exception e){
                MSMEDIC="N/A";
            }
            txtMSMEDIC.setText(MSMEDIC);
            //txtPANDate.setText(EITLERPGLOBAL.formatDate(ObjSupplierAmend.getAttribute("PAN_DATE").getString()));
            String s3="";
            try{
            s3=ObjSupplierAmend.getAttribute("PLACE_OF_SUPPLY").getString();
                }
            catch(Exception e){
                s3="";
            }
            txtPlaceofsupply.setText(s3);
            String s2="";
            try{
            s2=ObjSupplierAmend.getAttribute("CONTACT_PERSON_1").getString();
                }
            catch(Exception e){
                s2="";
            }
            //txtCP1.setText((String)ObjSupplierAmend.getAttribute("CONTACT_PERSON_1").getObj());
            txtCP1.setText(s2);
            txtCP2.setText((String)ObjSupplierAmend.getAttribute("CONTACT_PERSON_2").getObj());
            txtRegFrom.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("FROM_DATE_REG").getObj()));
            txtRegTo.setText(EITLERPGLOBAL.formatDate((String)ObjSupplierAmend.getAttribute("TO_DATE_REG").getObj()));
            txtPayDays.setText(Long.toString((long)ObjSupplierAmend.getAttribute("PAYMENT_DAYS").getVal()));
            txtBankID.setText(Long.toString((long)ObjSupplierAmend.getAttribute("BANK_ID").getVal()));
            txtBankName.setText((String)ObjSupplierAmend.getAttribute("BANK_NAME").getObj());
            txtMainAccountCode.setText(ObjSupplierAmend.getAttribute("MAIN_ACCOUNT_CODE").getString());
            
            EITLERPGLOBAL.setComboIndex(cmbPayment,(int)ObjSupplierAmend.getAttribute("PAYMENT_TERM_CODE").getVal());
            txtItem.setText((String)ObjSupplierAmend.getAttribute("PROPOSED_ITEM").getObj());
            
            
            lblBankID.setVisible(false);
            txtBankID.setVisible(false);
            txtBankName.setVisible(false);
            lblPaymentDays.setVisible(false);
            txtPayDays.setVisible(false);
            
            int PayCode=EITLERPGLOBAL.getComboCode(cmbPayment);
            
            if(PayCode==1) //Through Bank
            {
                lblBankID.setVisible(true);
                txtBankID.setVisible(true);
                txtBankName.setVisible(true);
            }
            
            if(PayCode==6||PayCode==2) {
                lblPaymentDays.setVisible(true);
                txtPayDays.setVisible(true);
            }
            
            
            chkOneTime.setSelected(ObjSupplierAmend.getAttribute("ONETIME_SUPPLIER").getBool());
           // chkCancelled.setSelected(ObjSupplierAmend.getAttribute("CANCELLED").getBool());
            
            if(((String)ObjSupplierAmend.getAttribute("BLOCKED").getObj()).equals("Y")) {
                bState = true;
            }
            chkBlocked.setSelected(bState);
            chkSlow.setSelected(ObjSupplierAmend.getAttribute("SLOW_MOVING").getBool());
            chkReg.setSelected(ObjSupplierAmend.getAttribute("ST35_REGISTERED").getBool());
            chkSSI.setSelected(ObjSupplierAmend.getAttribute("SSIREG").getBool());
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,(int)ObjSupplierAmend.getAttribute("HIERARCHY_ID").getVal());
            //========= Display Line Items =============//
            FormatGrid();
            
            for(int i=1;i<=ObjSupplierAmend.colSupplierItems.size();i++) {
                //Insert New Row
                Object[] rowData=new Object[1];
                DataModelL.addRow(rowData);
                int NewRow=TableL.getRowCount()-1;
                
                clsSupplierItem ObjItem=(clsSupplierItem)ObjSupplierAmend.colSupplierItems.get(Integer.toString(i));
                
                TableL.setValueAt(Long.toString((long)ObjItem.getAttribute("CATEGORY_ID").getVal()),NewRow,DataModelL.getColFromVariable("CATEGORY_ID"));
                String CategoryDesc=clsItemCategory.getCategoryDesc((int)EITLERPGLOBAL.gCompanyID, (long)ObjItem.getAttribute("CATEGORY_ID").getVal());
                DataModelL.setValueByVariable("CATEGORY_DESC",CategoryDesc,NewRow);
                
            }
            
            
            //============= P.O. Terms ==============//
            //========= Display Line Items =============//
            FormatTermsGrid();
            FormatGridP();
            for(int i=1;i<=ObjSupplierAmend.colSuppTerms.size();i++) {
                //Insert New Row
                Object[] rowData=new Object[4];
                clsSuppTerms ObjItem=(clsSuppTerms)ObjSupplierAmend.colSuppTerms.get(Integer.toString(i));
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
                
                if(TermType.equals("P")) {
                    rowData[0]=Integer.toString(TableP.getRowCount()+1);
                    rowData[1]=ObjItem.getAttribute("TERM_DESC").getString();
                    rowData[2]=Integer.toString(ObjItem.getAttribute("TERM_DAYS").getInt());
                    rowData[3]=Integer.toString(ObjItem.getAttribute("TERM_CODE").getInt());
                    DataModelP.addRow(rowData);
                }
                
            }
            //=======================================//
            
            
            //======================Displaying Child Suppliers =================//
            FormatGridSC();
            int ParentID=(int)ObjSupplierAmend.getAttribute("PARENT_SUPP_ID").getVal();
            String cSupplierCode="";
            int cSuppID=0;
            
            if(ParentID!=0) {
                opgParent.setSelected(true);
                opgChild.setSelected(false);
                cmdAddC.setEnabled(false);
                cmdRemoveC.setEnabled(false);
                
                cSupplierCode=clsSupplier.getSupplierCode(EITLERPGLOBAL.gCompanyID, ParentID);
                txtPSuppID.setText(cSupplierCode);
                txtPSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,cSupplierCode));
            }
            else {
                opgParent.setSelected(false);
                opgChild.setSelected(true);
                cmdAddC.setEnabled(true);
                cmdRemoveC.setEnabled(true);
                
                txtPSuppID.setText("");
                txtPSuppName.setText("");
                
                for(int i=1;i<=ObjSupplierAmend.colSuppChilds.size();i++) {
                    clsSuppChilds objChild=(clsSuppChilds)ObjSupplierAmend.colSuppChilds.get(Integer.toString(i));
                    cSuppID=    (int)objChild.getAttribute("CHILD_SUPP_ID").getVal();
                    cSupplierCode=clsSupplier.getSupplierCode(EITLERPGLOBAL.gCompanyID, cSuppID);
                    
                    Object[] rowData=new Object[4];
                    rowData[0]=Integer.toString(i);
                    rowData[1]=cSupplierCode;
                    rowData[2]=clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,cSupplierCode);
                    rowData[3]=Integer.toString(cSuppID);
                    
                    DataModelSC.addRow(rowData);
                }
            }
            //==========================================//
            
            
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List=new HashMap();
            
            String DocNo=Integer.toString((int)ObjSupplierAmend.getAttribute("AMEND_ID").getVal());
            List=ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, 50, DocNo);
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
            //============================================================//
            
            
            //================ Updation History ================//
            FormatGridU();
            
            List=clsSupplierAmend.getUpdationHistory(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());
            
            for(int i=1;i<=List.size();i++) {
                clsSupplierAmend ObjSupp=(clsSupplierAmend)List.get(Integer.toString(i));
                
                Object[] rowData=new Object[3];
                rowData[0]=Integer.toString(i);
                rowData[1]=EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("AMEND_DATE").getObj());
                rowData[2]=(String)ObjSupp.getAttribute("AMEND_REASON").getObj();
                
                DataModelU.addRow(rowData);
            }
            
            //=================================================//
            
            if(EditMode==0) {
                DataModelL.TableReadOnly(true);
            }
            
            //Showing Audit Trial History
            FormatGridHS();
            int SuppID=(int)ObjSupplierAmend.getAttribute("AMEND_ID").getVal();
            HashMap History=clsSupplierAmend.getHistoryList(EITLERPGLOBAL.gCompanyID, SuppID);
            for(int i=1;i<=History.size();i++) {
                clsSupplierAmend ObjHistory=(clsSupplierAmend)History.get(Integer.toString(i));
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
            
            //=========================================//
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Display Data Error: " + e.getMessage());
        }
    }
    
    
    
    
    private void DisplaySuppData(clsSupplier ObjSupp) {
        try {
            ClearFields();
            
            boolean bState = false;
            
            txtSuppCode.setText((String)ObjSupp.getAttribute("SUPPLIER_CODE").getObj());
            lblRevNo.setText(Integer.toString((int)ObjSupp.getAttribute("REVISION_NO").getVal()));
            txtDummyCode.setText((String)ObjSupp.getAttribute("DUMMY_SUPPLIER_CODE").getObj());
            txtName.setText((String)ObjSupp.getAttribute("SUPP_NAME").getObj());
            txtAttn.setText((String)ObjSupp.getAttribute("ATTN").getObj());
            txtAdd1.setText((String)ObjSupp.getAttribute("ADD1").getObj());
            txtAdd2.setText((String)ObjSupp.getAttribute("ADD2").getObj());
            txtAdd3.setText((String)ObjSupp.getAttribute("ADD3").getObj());
            txtCity.setText((String)ObjSupp.getAttribute("CITY").getObj());
            txtPIN.setText((String)ObjSupp.getAttribute("PINCODE").getObj());
            txtPlaceofsupply.setText((String)ObjSupp.getAttribute("PLACE_OF_SUPPLY").getObj());
            txtStateName.setText((String)ObjSupp.getAttribute("STATE").getObj());
            //cmbState.setSelectedItem(clsSales_Party.getStateName((int)ObjSupp.getAttribute("STATE").getVal()));
            txtCountryName.setText((String)ObjSupp.getAttribute("COUNTRY").getObj());
            //cmbCountry.setSelectedItem(clsSales_Party.getCountryName((int)ObjSupp.getAttribute("COUNTRY").getVal()));
            txtStateCode.setText((String)ObjSupp.getAttribute("STATE_CODE").getObj());
            txtStateGstCode.setText((String)ObjSupp.getAttribute("STATE_GST_CODE").getObj());
            txtPhone_O.setText((String)ObjSupp.getAttribute("PHONE_O").getObj());
            txtPhone_R.setText((String)ObjSupp.getAttribute("PHONE_RES").getObj());
            txtFax.setText((String)ObjSupp.getAttribute("FAX_NO").getObj());
            txtMobile.setText((String)ObjSupp.getAttribute("MOBILE_NO").getObj());
            txtEmail.setText((String)ObjSupp.getAttribute("EMAIL_ADD").getObj());
            txtWebsite.setText((String)ObjSupp.getAttribute("WEB_SITE").getObj());
            txtURL.setText((String)ObjSupp.getAttribute("URL").getObj());
            txtSTNO.setText((String)ObjSupp.getAttribute("SERVICETAX_NO").getObj());
            txtSTDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("SERVICETAX_DATE").getObj()));
            txtCST.setText((String)ObjSupp.getAttribute("CST_NO").getObj());
            txtCSTDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("CST_DATE").getObj()));
            txtGST.setText((String)ObjSupp.getAttribute("GST_NO").getObj());
            txtGSTDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("GST_DATE").getObj()));
            txtGSTIN.setText((String)ObjSupp.getAttribute("GSTIN_NO").getObj());
            txtGSTINDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("GSTIN_DATE").getObj()));
            txtECC.setText((String)ObjSupp.getAttribute("ECC_NO").getObj());
            txtECCDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("ECC_DATE").getObj()));
            txtSSI.setText((String)ObjSupp.getAttribute("SSIREG_NO").getObj());
            txtSSIDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("SSIREG_DATE").getObj()));
            txtESI.setText((String)ObjSupp.getAttribute("ESIREG_NO").getObj());
            txtESIDate.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("ESIREG_DATE").getObj()));
            txtPAN.setText(ObjSupp.getAttribute("PAN_NO").getString());
            txtPANDate.setText(EITLERPGLOBAL.formatDate(ObjSupp.getAttribute("PAN_DATE").getString()));
            txtCP1.setText((String)ObjSupp.getAttribute("CONTACT_PERSON_1").getObj());
            txtCP2.setText((String)ObjSupp.getAttribute("CONTACT_PERSON_2").getObj());
            txtRegFrom.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("FROM_DATE_REG").getObj()));
            txtRegTo.setText(EITLERPGLOBAL.formatDate((String)ObjSupp.getAttribute("TO_DATE_REG").getObj()));
            txtPayDays.setText(Long.toString((long)ObjSupp.getAttribute("PAYMENT_DAYS").getVal()));
            txtBankID.setText(Long.toString((long)ObjSupp.getAttribute("BANK_ID").getVal()));
            txtBankName.setText((String)ObjSupp.getAttribute("BANK_NAME").getObj());
            txtMainAccountCode.setText(ObjSupp.getAttribute("MAIN_ACCOUNT_CODE").getString());
            
            txtDistanceKm.setText(Integer.toString(ObjSupp.getAttribute("DISTANCE_KM").getInt()));
            
            EITLERPGLOBAL.setComboIndex(cmbPayment,(int)ObjSupp.getAttribute("PAYMENT_TERM_CODE").getVal());
            txtItem.setText((String)ObjSupp.getAttribute("PROPOSED_ITEM").getObj());
            
            lblBankID.setVisible(false);
            txtBankID.setVisible(false);
            txtBankName.setVisible(false);
            lblPaymentDays.setVisible(false);
            txtPayDays.setVisible(false);
            
            int PayCode=EITLERPGLOBAL.getComboCode(cmbPayment);
            
            if(PayCode==1) //Through Bank
            {
                lblBankID.setVisible(true);
                txtBankID.setVisible(true);
                txtBankName.setVisible(true);
            }
            
            if(PayCode==6||PayCode==2) {
                lblPaymentDays.setVisible(true);
                txtPayDays.setVisible(true);
            }
            
            
            chkOneTime.setSelected(ObjSupp.getAttribute("ONETIME_SUPPLIER").getBool());
            
            if(((String)ObjSupp.getAttribute("BLOCKED").getObj()).equals("Y")) {
                bState = true;
            }
            chkBlocked.setSelected(bState);
            chkSlow.setSelected(ObjSupp.getAttribute("SLOW_MOVING").getBool());
            chkReg.setSelected(ObjSupp.getAttribute("ST35_REGISTERED").getBool());
            chkSSI.setSelected(ObjSupp.getAttribute("SSIREG").getBool());
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,(int)ObjSupp.getAttribute("HIERARCHY_ID").getVal());
            //========= Display Line Items =============//
            FormatGrid();
            
            for(int i=1;i<=ObjSupp.colSupplierItems.size();i++) {
                //Insert New Row
                Object[] rowData=new Object[1];
                DataModelL.addRow(rowData);
                int NewRow=TableL.getRowCount()-1;
                
                clsSupplierItem ObjItem=(clsSupplierItem)ObjSupp.colSupplierItems.get(Integer.toString(i));
                
                TableL.setValueAt(Long.toString((long)ObjItem.getAttribute("CATEGORY_ID").getVal()),NewRow,DataModelL.getColFromVariable("CATEGORY_ID"));
                String CategoryDesc=clsItemCategory.getCategoryDesc((int)EITLERPGLOBAL.gCompanyID, (long)ObjItem.getAttribute("CATEGORY_ID").getVal());
                DataModelL.setValueByVariable("CATEGORY_DESC",CategoryDesc,NewRow);
                
            }
            
            
            
            //======================Displaying Child Suppliers =================//
            FormatGridSC();
            int ParentID=(int)ObjSupp.getAttribute("PARENT_SUPP_ID").getVal();
            String cSupplierCode="";
            int cSuppID=0;
            
            if(ParentID!=0) {
                opgParent.setSelected(true);
                opgChild.setSelected(false);
                cmdAddC.setEnabled(false);
                cmdRemoveC.setEnabled(false);
                
                cSupplierCode=clsSupplier.getSupplierCode(EITLERPGLOBAL.gCompanyID, ParentID);
                txtPSuppID.setText(cSupplierCode);
                txtPSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,cSupplierCode));
            }
            else {
                opgParent.setSelected(false);
                opgChild.setSelected(true);
                cmdAddC.setEnabled(true);
                cmdRemoveC.setEnabled(true);
                
                txtPSuppID.setText("");
                txtPSuppName.setText("");
                
                for(int i=1;i<=ObjSupp.colSuppChilds.size();i++) {
                    clsSuppChilds objChild=(clsSuppChilds)ObjSupp.colSuppChilds.get(Integer.toString(i));
                    cSuppID=    (int)objChild.getAttribute("CHILD_SUPP_ID").getVal();
                    cSupplierCode=clsSupplier.getSupplierCode(EITLERPGLOBAL.gCompanyID, cSuppID);
                    
                    Object[] rowData=new Object[4];
                    rowData[0]=Integer.toString(i);
                    rowData[1]=cSupplierCode;
                    rowData[2]=clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,cSupplierCode);
                    rowData[3]=Integer.toString(cSuppID);
                    
                    DataModelSC.addRow(rowData);
                }
            }
            //==========================================//
            
            
            //============= P.O. Terms ==============//
            //========= Display Line Items =============//
            FormatTermsGrid();
            FormatGridP();
            for(int i=1;i<=ObjSupp.colSuppTerms.size();i++) {
                //Insert New Row
                Object[] rowData=new Object[4];
                clsSuppTerms ObjItem=(clsSuppTerms)ObjSupp.colSuppTerms.get(Integer.toString(i));
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
                
                if(TermType.equals("P")) {
                    rowData[0]=Integer.toString(TableP.getRowCount()+1);
                    rowData[1]=ObjItem.getAttribute("TERM_DESC").getString();
                    rowData[2]=Integer.toString(ObjItem.getAttribute("TERM_DAYS").getInt());
                    rowData[3]=Integer.toString(ObjItem.getAttribute("TERM_CODE").getInt());
                    DataModelP.addRow(rowData);
                }
                
            }
        }
        catch(Exception e) {
        }
    }
    
    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjSupplierAmend.setAttribute("AMEND_ID",Integer.parseInt(txtAmendID.getText()));
        ObjSupplierAmend.setAttribute("AMEND_DATE",EITLERPGLOBAL.formatDateDB(txtAmendDate.getText()));
        ObjSupplierAmend.setAttribute("AMEND_REASON",txtAmendReason.getText());
        ObjSupplierAmend.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        //ObjSupplier.setAttribute("SUPP_ID",Long.parseLong(txtDeptID.getText()));
        
        //ObjSupplier.setAttribute("DECLARATION_DATE",EITLERPGLOBAL.formatDateDB(txtDate.getText()));
        ObjSupplierAmend.setAttribute("SUPPLIER_CODE",txtSuppCode.getText());
        ObjSupplierAmend.setAttribute("DUMMY_SUPPLIER_CODE",txtDummyCode.getText());
        ObjSupplierAmend.setAttribute("SUPP_NAME",txtName.getText());
        ObjSupplierAmend.setAttribute("ATTN",txtAttn.getText());
        ObjSupplierAmend.setAttribute("ADD1",txtAdd1.getText());
        ObjSupplierAmend.setAttribute("ADD2",txtAdd2.getText());
        ObjSupplierAmend.setAttribute("ADD3",txtAdd3.getText());
        ObjSupplierAmend.setAttribute("CITY",txtCity.getText());
        ObjSupplierAmend.setAttribute("PINCODE",txtPIN.getText());
        //ObjSupplierAmend.setAttribute("STATE",EITLERPGLOBAL.getComboCode(cmbState)); 
        ObjSupplierAmend.setAttribute("STATE",txtStateName.getText());
        ObjSupplierAmend.setAttribute("COUNTRY",txtCountryName.getText());
        //ObjSupplierAmend.setAttribute("COUNTRY",EITLERPGLOBAL.getComboCode(cmbCountry));
        ObjSupplierAmend.setAttribute("STATE_CODE",txtStateCode.getText());
        ObjSupplierAmend.setAttribute("STATE_GST_CODE",txtStateGstCode.getText());
        ObjSupplierAmend.setAttribute("PLACE_OF_SUPPLY",txtPlaceofsupply.getText());
        ObjSupplierAmend.setAttribute("PHONE_O",txtPhone_O.getText());
        ObjSupplierAmend.setAttribute("PHONE_RES",txtPhone_R.getText());
        ObjSupplierAmend.setAttribute("FAX_NO",txtFax.getText());
        ObjSupplierAmend.setAttribute("MOBILE_NO",txtMobile.getText());
        ObjSupplierAmend.setAttribute("EMAIL_ADD",txtEmail.getText());
        ObjSupplierAmend.setAttribute("URL",txtURL.getText());
        ObjSupplierAmend.setAttribute("WEB_SITE",txtWebsite.getText());
        ObjSupplierAmend.setAttribute("SERVICETAX_NO",txtSTNO.getText());
       if("".equals(EITLERPGLOBAL.formatDateDB(txtSTDate.getText())))
        {
            ObjSupplierAmend.setAttribute("SERVICETAX_DATE","0000-00-00");
        }
        else
        {
            ObjSupplierAmend.setAttribute("SERVICETAX_DATE",EITLERPGLOBAL.formatDateDB(txtSTDate.getText()));
        }

        // ObjSupplierAmend.setAttribute("SERVICETAX_DATE",EITLERPGLOBAL.formatDateDB(txtSTDate.getText()));
        ObjSupplierAmend.setAttribute("CST_NO",txtCST.getText());
         if("".equals(EITLERPGLOBAL.formatDateDB(txtCSTDate.getText())))
        {
            ObjSupplierAmend.setAttribute("CST_DATE","0000-00-00");
        }
        else
        {
            ObjSupplierAmend.setAttribute("CST_DATE",EITLERPGLOBAL.formatDateDB(txtCSTDate.getText()));
        }

        //ObjSupplierAmend.setAttribute("CST_DATE",EITLERPGLOBAL.formatDateDB(txtCSTDate.getText()));
        ObjSupplierAmend.setAttribute("GST_NO",txtGST.getText());
         if("".equals(EITLERPGLOBAL.formatDateDB(txtGSTDate.getText())))
        {
            ObjSupplierAmend.setAttribute("GST_DATE","0000-00-00");
        }
        else
        {
            ObjSupplierAmend.setAttribute("GST_DATE",EITLERPGLOBAL.formatDateDB(txtGSTDate.getText()));
        }

        //ObjSupplierAmend.setAttribute("GST_DATE",EITLERPGLOBAL.formatDateDB(txtGSTDate.getText()));
        ObjSupplierAmend.setAttribute("GSTIN_NO",txtGSTIN.getText());
         if("".equals(EITLERPGLOBAL.formatDateDB(txtGSTINDate.getText())))
        {
            ObjSupplierAmend.setAttribute("GSTIN_DATE","0000-00-00");
        }
        else
        {
            ObjSupplierAmend.setAttribute("GSTIN_DATE",EITLERPGLOBAL.formatDateDB(txtGSTINDate.getText()));
        }
        //ObjSupplierAmend.setAttribute("GSTIN_DATE",EITLERPGLOBAL.formatDateDB(txtGSTINDate.getText()));
        ObjSupplierAmend.setAttribute("ECC_NO",txtECC.getText());
        if("".equals(EITLERPGLOBAL.formatDateDB(txtECCDate.getText())))
        {
            ObjSupplierAmend.setAttribute("ECC_DATE","0000-00-00");
        }
        else
        {
            ObjSupplierAmend.setAttribute("ECC_DATE",EITLERPGLOBAL.formatDateDB(txtECCDate.getText()));
        }
        //ObjSupplierAmend.setAttribute("ECC_DATE",EITLERPGLOBAL.formatDateDB(txtECCDate.getText()));
        ObjSupplierAmend.setAttribute("SSIREG_NO",txtSSI.getText());
        
        if("".equals(EITLERPGLOBAL.formatDateDB(txtSSIDate.getText())))
        {
            ObjSupplierAmend.setAttribute("SSIREG_DATE","0000-00-00");
        }
        else
        {
            ObjSupplierAmend.setAttribute("SSIREG_DATE",EITLERPGLOBAL.formatDateDB(txtSSIDate.getText()));
        }
        //ObjSupplierAmend.setAttribute("SSIREG_DATE",EITLERPGLOBAL.formatDateDB(txtSSIDate.getText()));
        ObjSupplierAmend.setAttribute("ESIREG_NO",txtESI.getText());
        if("".equals(EITLERPGLOBAL.formatDateDB(txtESIDate.getText())))
        {
            ObjSupplierAmend.setAttribute("ESIREG_DATE","0000-00-00");
        }
        else
        {
            ObjSupplierAmend.setAttribute("ESIREG_DATE",EITLERPGLOBAL.formatDateDB(txtESIDate.getText()));
        }
        //ObjSupplierAmend.setAttribute("ESIREG_DATE",EITLERPGLOBAL.formatDateDB(txtESIDate.getText()));
        
        ObjSupplierAmend.setAttribute("PAN_NO",txtPAN.getText().trim());
        if("".equals(EITLERPGLOBAL.formatDateDB(txtPANDate.getText())))
        {
            ObjSupplierAmend.setAttribute("PAN_DATE","0000-00-00");
        }
        else
        {
            ObjSupplierAmend.setAttribute("PAN_DATE",EITLERPGLOBAL.formatDateDB(txtPANDate.getText()));
        }
        //ObjSupplierAmend.setAttribute("PAN_DATE",EITLERPGLOBAL.formatDateDB(txtPANDate.getText()));
        if(chkMSME.isSelected()) {
            ObjSupplierAmend.setAttribute("MSME",true);
        }
        else {
            ObjSupplierAmend.setAttribute("MSME",false);
        }
        //ObjSupplierAmend.setAttribute("MSME",txtMSMEUAN.getText().trim()); 
        ObjSupplierAmend.setAttribute("MSME_UAN",txtMSMEUAN.getText().trim()); 
        ObjSupplierAmend.setAttribute("MSME_DIC_NO",txtMSMEDIC.getText().trim()); 
        ObjSupplierAmend.setAttribute("CONTACT_PERSON_1",txtCP1.getText());
        ObjSupplierAmend.setAttribute("CONTACT_PERSON_2",txtCP2.getText());
        ObjSupplierAmend.setAttribute("FROM_DATE_REG",EITLERPGLOBAL.formatDateDB(txtRegFrom.getText()));
        ObjSupplierAmend.setAttribute("TO_DATE_REG",EITLERPGLOBAL.formatDateDB(txtRegTo.getText()));
        ObjSupplierAmend.setAttribute("PAYMENT_DAYS",EITLERPGLOBAL.formatLNumber(txtPayDays.getText()));
        ObjSupplierAmend.setAttribute("BANK_ID",EITLERPGLOBAL.formatLNumber(txtBankID.getText()));
        ObjSupplierAmend.setAttribute("BANK_NAME",txtBankName.getText());
        ObjSupplierAmend.setAttribute("PAYMENT_TERM_CODE",EITLERPGLOBAL.getComboCode(cmbPayment));
        ObjSupplierAmend.setAttribute("PROPOSED_ITEM",txtItem.getText());
        ObjSupplierAmend.setAttribute("MAIN_ACCOUNT_CODE",txtMainAccountCode.getText());
        
        ObjSupplierAmend.setAttribute("DISTANCE_KM",UtilFunctions.CInt(txtDistanceKm.getText()));
        
        if(chkOneTime.isSelected()) {
            ObjSupplierAmend.setAttribute("ONETIME_SUPPLIER",true);
        }
        else {
            ObjSupplierAmend.setAttribute("ONETIME_SUPPLIER",false);
        }
        if(chkBlocked.isSelected()) {
            ObjSupplierAmend.setAttribute("BLOCKED","Y");
        }
        else {
            ObjSupplierAmend.setAttribute("BLOCKED","N");
        }
        if(chkSlow.isSelected()) {
            ObjSupplierAmend.setAttribute("SLOW_MOVING",true);
        }
        else {
            ObjSupplierAmend.setAttribute("SLOW_MOVING",false);
        }
        if(chkReg.isSelected()) {
            ObjSupplierAmend.setAttribute("ST35_REGISTERED",true);
        }
        else {
            ObjSupplierAmend.setAttribute("ST35_REGISTERED",false);
        }
        if(chkSSI.isSelected()) {
            ObjSupplierAmend.setAttribute("SSIREG",true);
        }
        else {
            ObjSupplierAmend.setAttribute("SSIREG",false);
        }
        
        int SuppID=0;
        
        ObjSupplierAmend.setAttribute("PARENT_SUPP_ID",0);
        ObjSupplierAmend.colSuppChilds.clear();
        
        if(opgParent.isSelected()) {
            if(!txtPSuppID.getText().trim().equals("")) {
                ObjSupplierAmend.setAttribute("PARENT_SUPP_ID",SuppID);
            }
        }
        else {
            //Define
            ObjSupplierAmend.setAttribute("PARENT_SUPP_ID",0);
            
            for(int i=0;i<TableSC.getRowCount();i++) {
                clsSuppChilds objChild=new clsSuppChilds();
                SuppID=clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID, (String)TableSC.getValueAt(i, 1));
                
                objChild.setAttribute("CHILD_SUPP_ID",SuppID);
                ObjSupplierAmend.colSuppChilds.put(Integer.toString(ObjSupplierAmend.colSuppChilds.size()+1),objChild);
            }
        }
        
        //----- Update Approval Specific Fields -----------//
        ObjSupplierAmend.setAttribute("HIERARCHY_ID",EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjSupplierAmend.setAttribute("FROM",EITLERPGLOBAL.gNewUserID);
        ObjSupplierAmend.setAttribute("TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjSupplierAmend.setAttribute("FROM_REMARKS",txtToRemarks.getText());
        
        if(OpgApprove.isSelected()) {
            ObjSupplierAmend.setAttribute("APPROVAL_STATUS","A");
        }
        
        if(OpgFinal.isSelected()) {
            ObjSupplierAmend.setAttribute("APPROVAL_STATUS","F");
        }
        
        if(OpgReject.isSelected()) {
            ObjSupplierAmend.setAttribute("APPROVAL_STATUS","R");
            ObjSupplierAmend.setAttribute("SEND_DOC_TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        }
        
        if(OpgHold.isSelected()) {
            ObjSupplierAmend.setAttribute("APPROVAL_STATUS","H");
        }
        //-------------------------------------------------//
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            ObjSupplierAmend.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
            ObjSupplierAmend.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else {
            ObjSupplierAmend.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            ObjSupplierAmend.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        
        //=================== Setting up Line Items ==================//
        
        
        try {
            ObjSupplierAmend.colSupplierItems.clear();
            
            for(int i=0;i<TableL.getRowCount();i++) {
                clsSupplierItem ObjItem=new clsSupplierItem();
                
                ObjItem.setAttribute("CATEGORY_ID",Long.parseLong(DataModelL.getValueByVariable("CATEGORY_ID",i)));
                //ObjItem.setAttribute("CATEGORY_DESC",DataModelL.getValueByVariable("CATEGORY_DESC",i));
                
                ObjSupplierAmend.colSupplierItems.put(Integer.toString(ObjSupplierAmend.colSupplierItems.size()+1),ObjItem);
            }
        }
        
        catch(Exception e){}
        
        int TermCounter=0;
        
        ObjSupplierAmend.colSuppTerms.clear();
        
        
        for(int i=0;i<TableP.getRowCount();i++) {
            int PayTermCode=Integer.parseInt(TableP.getValueAt(i, 3).toString());
            String PayTerm=TableP.getValueAt(i, 1).toString();
            int TermDays=Integer.parseInt(TableP.getValueAt(i, 2).toString());
            clsSuppTerms ObjItem=new clsSuppTerms();
            
            ObjItem.setAttribute("TERM_TYPE","P");
            ObjItem.setAttribute("TERM_CODE",PayTermCode);
            ObjItem.setAttribute("TERM_DAYS",TermDays);
            ObjItem.setAttribute("TERM_DESC",PayTerm);
            
            TermCounter++;
            ObjSupplierAmend.colSuppTerms.put(Integer.toString(TermCounter), ObjItem);
            
        }
        
        
        for(int i=0;i<TableD.getRowCount();i++) {
            TermCounter++;
            
            clsSuppTerms ObjItem=new clsSuppTerms();
            
            ObjItem.setAttribute("TERM_TYPE","D");
            if(EITLERPGLOBAL.IsNumber((String)TableD.getValueAt(i, 1))) {
                ObjItem.setAttribute("TERM_CODE",Integer.parseInt((String)TableD.getValueAt(i, 1)));
            }
            else {
                ObjItem.setAttribute("TERM_CODE",0);
            }
            ObjItem.setAttribute("TERM_DESC",(String)TableD.getValueAt(i,2));
            
            ObjSupplierAmend.colSuppTerms.put(Integer.toString(TermCounter), ObjItem);
        }
        
        for(int i=0;i<TableO.getRowCount();i++) {
            TermCounter++;
            
            clsSuppTerms ObjItem=new clsSuppTerms();
            
            ObjItem.setAttribute("TERM_TYPE","O");
            if(EITLERPGLOBAL.IsNumber((String)TableO.getValueAt(i, 1))) {
                ObjItem.setAttribute("TERM_CODE",Integer.parseInt((String)TableO.getValueAt(i, 1)));
            }
            else {
                ObjItem.setAttribute("TERM_CODE",0);
            }
            ObjItem.setAttribute("TERM_DESC",(String)TableO.getValueAt(i,2));
            
            ObjSupplierAmend.colSuppTerms.put(Integer.toString(TermCounter), ObjItem);
        }
        
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
        TableA.getColumnModel().getColumn(0).setCellRenderer(Paint);
        Paint.setColor(1,1,Color.CYAN);
        
    }
    
    
    private void FormatGridU() {
        DataModelU=new EITLTableModel();
        
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
    
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,931)) {
            cmdNew.setEnabled(true);
        }
        else {
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
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,933)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,934)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }
    
    
    private void FormatTermsGrid() {
        
        //--- PO Terms Formatting -----//
        DataModelD=new EITLTableModel();
        
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
        DataModelO=new EITLTableModel();
        
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
        
        for(int i=0;i<Tab2.getComponentCount()-1;i++) {
            if(Tab2.getComponent(i).getName()!=null) {
                
                FieldName=Tab2.getComponent(i).getName();
                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    
                    Tab2.getComponent(i).setEnabled(true);
                }
                
            }
        }
        
        for(int i=0;i<Tab3.getComponentCount()-1;i++) {
            if(Tab3.getComponent(i).getName()!=null) {
                
                FieldName=Tab3.getComponent(i).getName();
                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    Tab3.getComponent(i).setEnabled(true);
                }
                
            }
        }
        //=============== Header Fields Setup Complete =================//
        
        
        
        if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", "DELIVERY_TERMS")) {
            DataModelD.TableReadOnly(false);
        }
        else {
            DataModelD.TableReadOnly(true);
        }
        
        if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", "OTHER_TERMS")) {
            DataModelO.TableReadOnly(false);
        }
        else {
            DataModelO.TableReadOnly(true);
        }
        
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
    
    
    public void FindWaiting() {
        ObjSupplierAmend.Filter(" WHERE AMEND_ID IN (SELECT D_COM_SUPP_AMEND_MASTER.AMEND_ID FROM D_COM_SUPP_AMEND_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=50)");
        ObjSupplierAmend.MoveFirst();
        DisplayData();
    }
    
    
    public void FindEx(int pCompanyID,String pSuppID) {
        ObjSupplierAmend.Filter(" WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND AMEND_ID="+pSuppID);
        ObjSupplierAmend.MoveFirst();
        DisplayData();
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
                    IncludeUser=ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, 50, txtAmendID.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
                    IncludeUser=ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, 50, txtAmendID.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String DocNo=Long.toString((long)ObjSupplierAmend.getAttribute("AMEND_ID").getVal());
            int Creator=ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, 50, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo,Creator);
        }
        
    }
    
    
    private void FormatGridP() {
        DataModelP=new EITLTableModel();
        
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
        DataModelP.SetNumeric(2, true);
    }
    
    
    private void FormatGridSC() {
        DataModelSC=new EITLTableModel();
        
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
    private void GenerateStateCombo(){
        HashMap stateList=new HashMap();
        
        cmbStateModel=new EITLComboModel();
        cmbState.removeAllItems();
        cmbState.setModel(cmbStateModel);
        
        stateList=clsSales_Party.getStateNameList();
        for(int i=1;i<=stateList.size();i++) {
            cmbStateModel.addElement((ComboData)stateList.get(new Integer(i)));
        }
    }
     
     private void GenerateCountryCombo(){
        HashMap countryList=new HashMap();
        
        cmbCountryModel=new EITLComboModel();
        cmbCountry.removeAllItems();
        cmbCountry.setModel(cmbCountryModel);
        
        countryList=clsSales_Party.getCountryNameList();
        for(int i=1;i<=countryList.size();i++) {
            cmbCountryModel.addElement((ComboData)countryList.get(new Integer(i)));
        }
    }
     public static String toDisplayCase(String s) {

    final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
                                                 // to be capitalized

    StringBuilder sb = new StringBuilder();
    boolean capNext = true;

    for (char c : s.toCharArray()) {
        c = (capNext)
                ? Character.toUpperCase(c)
                : Character.toLowerCase(c);
        sb.append(c);
        capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
    }
    return sb.toString();
}

}
