/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.DiversionLoss;

import EITLERP.FeltSales.OrderDiversion.*;
import EITLERP.AppletFrame;
import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.FeltFinishing.frmFeltFinishing;
import EITLERP.FeltSales.FeltFinishing.frmFeltFinishingReport;
import EITLERP.FeltSales.Order.searchkey;
import EITLERP.FeltSales.Order.searchkey_diversionlist;
import EITLERP.FeltSales.common.FeltInvCalc;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.SelectFirstFree;
import EITLERP.FeltSales.common.LOV;
import EITLERP.FeltSales.common.MailNotification;
import EITLERP.FeltSales.common.clsOrderValueCalc;
import EITLERP.Finance.frmDebitNoteVoucher;
import EITLERP.JavaMail.JMail;
import EITLERP.Loader;
import EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.clsAuthority;
import EITLERP.clsDepartment;
import EITLERP.clsDocFlow;
import EITLERP.clsFirstFree;
import EITLERP.clsHierarchy;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import EITLERP.frmPendingApprovals;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;
import EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.text.NumberFormatter;
import EITLERP.Production.ReportUI.JTextFieldHint;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FrmFeltDiversionLoss extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableModel DataModel_ExistPiece;
    private EITLTableModel DataModel_NewPiece;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    private final int ModuleId = 779;
    private String DOC_NO = "";
    private clsFeltOrderDiversionLoss feltOrder;
    private EITLComboModel cmbSendToModel;

//    DecimalFormat f_single = new DecimalFormat("##.0");
//    DecimalFormat f_double = new DecimalFormat("##.00");
    String seleval = "", seltyp = "", selqlt = "", selshd = "", selpiece = "", selext = "", selinv = "", selsz = "";

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateForDB = new SimpleDateFormat("yyyy-MM-dd");
    double Exist_Invoice_Amt = 0.0;
    double New_Invoice_Amt = 0.0;
    public String frm_PARTY_CODE;
    public String frm_REFERENCE;
    public String frm_PO_NO;
    public String frm_PO_DATE;
    public String frm_REFERENCE_DATE;
    public String frm_REMARK;
    public String frm_Machine_No;
    public String frm_Position_No;
    public String frm_Piece_No;
    public String frm_S_O_No;

    float Existing_Inv_Amt = 0;
    FeltInvCalc inv_calc_existing = new FeltInvCalc();
    String PR_PIECE_NO = "", PR_PRODUCT_CODE = "", PR_PARTY_CODE = "", PR_ORDER_DATE = "", PR_MACHINE_NO = "", PR_POSITION_NO = "", PR_RCV_DATE = "", PR_ACTUAL_WEIGHT = "", PR_ACTUAL_LENGTH = "", PR_ACTUAL_WIDTH = "";
    float PR_LENGTH = 0, PR_WIDTH = 0, PR_THORITICAL_WEIGHT = 0, PR_SQMTR = 0;
    int PR_GSM = 0;
    String PR_SYN_PER = "", PR_STYLE = "", PR_GROUP = "";

    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    public frmPendingApprovals frmPA;

    public float difference = 0;
    public String PR_PIECE_STAGE = "";
    public boolean designer_not_msg = false;

    @Override
    public void init() {
        System.gc();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        if (scrwidth > 800) {
            scrwidth = 800;
        }
        setSize(scrwidth, scrheight - 50);
        initComponents();

        DecimalFormat decimalFormat = new DecimalFormat("0");
        NumberFormatter ObjFormater = new NumberFormatter(decimalFormat);

        ObjFormater.setAllowsInvalid(false);
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        NumberFormatter ObjFormater1 = new NumberFormatter(decimalFormat1);

        GenerateCombos();

        SetupApproval();
        SetMenuForRights();
        GenerateHierarchyCombo();
        GenerateFromCombo();

        DefaultSettings();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();

        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(S_O_DATE);

        } catch (ParseException ex) {
            System.out.println("Error on Mask " + ex.getMessage());
        }
        S_O_DATE.setText(df.format(new Date()));

        feltOrder = new clsFeltOrderDiversionLoss();
        boolean load = feltOrder.LoadData();
        if (load) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + feltOrder.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        SetFields(false);

    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    public void DefaultSettings() {
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
        clearFields();
    }

    private void clearFields() {
        S_O_NO.setText("0");
        reason.setSelectedIndex(0);
        REMARK1.setText("");
        REMARK.setText("");
        
        txtpiece.setText("");
        getPieceDetail();
        txtPartyCode.setText("");
        txtThWeight.setText("");
        txtMachinePosition.setText("");
        txtlength.setText("");
        txtwidth.setText("");
        txtgsm.setText("");
        txtwtsqmtr.setText("");
        txtProductCode.setText("");

        txtProfitLoss.setText("");
        txtProfitLossPer.setText("");
        txtByCompany.setText("");
        txtByParty.setText("");
        txtByCompanyAmt.setText("");
        txtByPartyAmt.setText("");
        
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
        FormatGridHS();

    }

    private void DisplayData() {

        //=========== Color Indication ===============//
        try {

            if ("1".equals(feltOrder.getAttribute("APPROVED").getString())) {
                lblTitle.setBackground(Color.BLUE);
                lblTitle.setForeground(Color.WHITE);
            }

            if ("0".equals(feltOrder.getAttribute("APPROVED").getString())) {

                lblTitle.setBackground(Color.GRAY);
                lblTitle.setForeground(Color.BLACK);
            }

            if ("1".equals(feltOrder.getAttribute("CANCELED").getString())) {
                lblTitle.setBackground(Color.RED);
                lblTitle.setForeground(Color.BLACK);
            }
        } catch (Exception c) {

            c.printStackTrace();
        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleId)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        clearFields();
        try {
            S_O_NO.setText(feltOrder.getAttribute("SD_ORDER_NO").getString());
            lblTitle.setText("Felt Sales Order Diversion Prior Approval - " + feltOrder.getAttribute("SD_ORDER_NO").getString());
            S_O_DATE.setText(feltOrder.getAttribute("SD_ORDER_DATE").getString());
            reason.setSelectedItem(feltOrder.getAttribute("D_REASON_FOR_DIVERSION").getString());
            REMARK1.setText(feltOrder.getAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT").getString());
            REMARK.setText(feltOrder.getAttribute("D_REMARK").getString());
            
            txtpiece.setText(feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString());
            getPieceDetail();
            txtPartyCode.setText(feltOrder.getAttribute("D_PARTY_CODE").getString());
            txtlength.setText(feltOrder.getAttribute("D_LENGTH").getString());
            txtwidth.setText(feltOrder.getAttribute("D_WIDTH").getString());
            txtgsm.setText(feltOrder.getAttribute("D_GSM").getString());
            txtwtsqmtr.setText(feltOrder.getAttribute("D_THORITICAL_WEIGHT").getString());
            txtProductCode.setText(feltOrder.getAttribute("D_PRODUCT_NO").getString());
            
            txtMachinePosition.setText(feltOrder.getAttribute("MACHINE_POSITION").getString());
            txtThWeight.setText(feltOrder.getAttribute("TH_WEIGHT").getString());
            
            FindProfitLoss();
            
            if(EditMode != EITLERPGLOBAL.EDIT && EditMode != EITLERPGLOBAL.ADD)
            {
                txtProfitLoss.setText(feltOrder.getAttribute("DIFFERENCE_AMT").getString());
            }
            
            txtByCompany.setText(feltOrder.getAttribute("COST_BY_COMPANY_PER").getString());
            txtByCompanyAmt.setText(feltOrder.getAttribute("COST_BY_COMPANY_AMT").getString());
                  
            txtByParty.setText(feltOrder.getAttribute("COST_BY_PARTY_PER").getString());
            txtByPartyAmt.setText(feltOrder.getAttribute("COST_BY_PARTY_AMT").getString());
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, Integer.parseInt(feltOrder.getAttribute("HIERARCHY_ID").getString()));
            
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = feltOrder.getAttribute("SD_ORDER_NO").getString();
            DOC_NO = DocNo;
            List = clsFeltProductionApprovalFlow.getDocumentFlow(ModuleId, DocNo);
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

                DataModelApprovalStatus.addRow(rowData);
            }

            //Showing Audit Trial History                                                                                                                                                 
            FormatGridHS();
            HashMap History = feltOrder.getHistoryList(EITLERPGLOBAL.gCompanyID + "", DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsFeltOrderDiversionLoss ObjHistory = (clsFeltOrderDiversionLoss) History.get(Integer.toString(i));
                Object[] rowData = new Object[6];
                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(ObjHistory.getAttribute("UPDATED_BY").getInt() + ""));
                rowData[2] = ObjHistory.getAttribute("ENTRY_DATE").getString();
                String ApprovalStatus = "";
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus = "Pending";
                }
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }
                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }
                rowData[3] = ApprovalStatus;
                rowData[4] = ObjHistory.getAttribute("REJECTED_REMARKS").getString();
                rowData[5] = ObjHistory.getAttribute("FROM_IP").getString();
                DataModelUpdateHistory.addRow(rowData);
            }
            //============================================================//
        } catch (Exception e) {

        }
    }

    private void FormatGridA() {
        DataModelApprovalStatus = new EITLTableModel();

        TableApprovalStatus.removeAll();
        TableApprovalStatus.setModel(DataModelApprovalStatus);

        //Set the table Readonly
        DataModelApprovalStatus.TableReadOnly(true);

        //Add the columns
        DataModelApprovalStatus.addColumn("Sr.");
        DataModelApprovalStatus.addColumn("User");
        DataModelApprovalStatus.addColumn("Status");
        DataModelApprovalStatus.addColumn("Department");
        DataModelApprovalStatus.addColumn("Received Date");
        DataModelApprovalStatus.addColumn("Action Date");
        DataModelApprovalStatus.addColumn("Remarks");

        TableApprovalStatus.setAutoResizeMode(TableApprovalStatus.AUTO_RESIZE_OFF);

    }

    private void SetMenuForRights() {
        // --- Add Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6233, 62331)) { //7008,70081
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6233, 62332)) { //7008,70082
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6233, 62333)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6233, 62334)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }

    private void SetupApproval() {

        if (cmbHierarchy.getItemCount() > 1) {
            cmbHierarchy.setEnabled(true);
        }
        //JOptionPane.showMessageDialog(null, "Approval Cmb : "+cmbHierarchy.getItemCount());
        //In Edit Mode Hierarchy Should be disabled
        if (EditMode == EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
            OpgReject.setEnabled(false);//GAURANG
        } else {
            cmbHierarchy.setEnabled(false);
        }

        //Set Default Hierarchy ID for User
        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);

        if (EditMode == EITLERPGLOBAL.ADD) {
            lnFromID = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {
            lnFromID = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID));
            txtFromRemarks.setText("");
        }
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();//GAURANG
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
        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
            if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
                OpgReject.setEnabled(false);
            }//GAURANG
        }
        if (EditMode == 0) {
            cmbHierarchy.setEnabled(false);
            txtFrom.setEnabled(false);
            OpgApprove.setEnabled(false);
            OpgFinal.setEnabled(false);
            OpgReject.setEnabled(false);
            cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }
        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
            OpgApprove.setEnabled(false);
        }
    }

    private void FormatGridHS() {
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
        DataModelUpdateHistory.addColumn("From IP");

        TableUpdateHistory.setAutoResizeMode(TableUpdateHistory.AUTO_RESIZE_OFF);
    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbType ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId);

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId);
        }
        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (EITLERPGLOBAL.gNewUserID == clsFeltProductionApprovalFlow.getCreator(ModuleId, DOC_NO + "")) {
                List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + (ModuleId));
            } else {
                List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId);
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

                List = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, DOC_NO + "");
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

        TableColumnModel tcm = TableApprovalStatus.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(3).setPreferredWidth(20);
        tcm.getColumn(4).setPreferredWidth(90);
        tcm.getColumn(5).setPreferredWidth(90);
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

        TableColumnModel tcm = TableUpdateHistory.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(2).setPreferredWidth(50);
        tcm.getColumn(3).setPreferredWidth(20);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        Tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        REMARK = new javax.swing.JTextField();
        S_O_NO = new javax.swing.JTextField();
        S_O_DATE = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        reason = new javax.swing.JComboBox<String>();
        jLabel12 = new javax.swing.JLabel();
        REMARK1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblexist = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbldiverted = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblpl = new javax.swing.JLabel();
        txtgsm = new javax.swing.JFormattedTextField();
        txtPartyCode = new javax.swing.JFormattedTextField();
        txtlength = new javax.swing.JFormattedTextField();
        txtwidth = new javax.swing.JFormattedTextField();
        txtpiece = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtwtsqmtr = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        txtProfitLoss = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtFinishingDate = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtByCompany = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtByParty = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtByCompanyAmt = new javax.swing.JTextField();
        txtByPartyAmt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtProfitLossPer = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtThWeight = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtMachinePosition = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
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
        jPanel3 = new javax.swing.JPanel();
        StatusPanel = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableApprovalStatus = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableUpdateHistory = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
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
        lblStatus1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        Tab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabMouseClicked(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        jLabel2.setText("Date");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 20, 80, 20);

        jLabel3.setText("Diversion No");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(150, 20, 100, 20);

        jLabel5.setText("Remark");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 500, 80, 20);

        REMARK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                REMARKFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                REMARKFocusLost(evt);
            }
        });
        jPanel1.add(REMARK);
        REMARK.setBounds(90, 500, 700, 20);

        S_O_NO.setEditable(false);
        S_O_NO.setBackground(new java.awt.Color(254, 242, 230));
        S_O_NO.setText("S00000001");
        S_O_NO.setEnabled(false);
        jPanel1.add(S_O_NO);
        S_O_NO.setBounds(240, 10, 100, 30);

        S_O_DATE.setEditable(false);
        jPanel1.add(S_O_DATE);
        S_O_DATE.setBounds(50, 10, 90, 30);

        jLabel11.setText("Reason For Diversion");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(10, 440, 160, 20);

        reason.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Party's Urgency", "Delivery Delay", "Old Stock Liquidation" }));
        jPanel1.add(reason);
        reason.setBounds(180, 440, 240, 20);

        jLabel12.setText("Delivery Status for Existing Client");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(10, 470, 230, 20);

        REMARK1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                REMARK1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                REMARK1FocusLost(evt);
            }
        });
        jPanel1.add(REMARK1);
        REMARK1.setBounds(230, 470, 560, 20);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("D i v e r t e d    P a r t y    D e t a i l s");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(500, 40, 320, 30);

        jLabel6.setText("Piece No.");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(350, 20, 80, 20);

        tblexist.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblexist);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(20, 70, 240, 290);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Existing Party Detail");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(60, 40, 160, 30);

        tbldiverted.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tbldiverted);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(500, 70, 260, 290);

        jLabel8.setText("Party Code");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(280, 50, 90, 30);

        jLabel10.setText("GSM");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(280, 200, 60, 30);

        jLabel13.setText("Length");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(280, 140, 60, 30);

        jLabel14.setText("Width");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(280, 170, 60, 30);

        lblpl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblpl.setText("Profit (+) / Loss (-)");
        jPanel1.add(lblpl);
        lblpl.setBounds(270, 300, 120, 30);

        txtgsm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtgsmFocusLost(evt);
            }
        });
        txtgsm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtgsmKeyReleased(evt);
            }
        });
        jPanel1.add(txtgsm);
        txtgsm.setBounds(380, 200, 110, 30);

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
        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(380, 50, 110, 30);

        txtlength.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtlengthFocusLost(evt);
            }
        });
        txtlength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtlengthActionPerformed(evt);
            }
        });
        jPanel1.add(txtlength);
        txtlength.setBounds(380, 140, 110, 30);

        txtwidth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtwidthFocusLost(evt);
            }
        });
        jPanel1.add(txtwidth);
        txtwidth.setBounds(380, 170, 110, 30);

        txtpiece.setToolTipText("Press F1 key for search Party Code");
        txtpiece = new JTextFieldHint(new JTextField(),"Search by F1");
        txtpiece.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpieceKeyPressed(evt);
            }
        });
        jPanel1.add(txtpiece);
        txtpiece.setBounds(420, 10, 100, 30);

        jLabel15.setText("Wt/Sqmtr [m]");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(280, 230, 90, 30);

        txtwtsqmtr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtwtsqmtrFocusLost(evt);
            }
        });
        jPanel1.add(txtwtsqmtr);
        txtwtsqmtr.setBounds(380, 230, 110, 30);

        jLabel4.setText("Product Code");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(290, 270, 100, 20);

        txtProductCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductCodeFocusLost(evt);
            }
        });
        jPanel1.add(txtProductCode);
        txtProductCode.setBounds(380, 260, 110, 30);

        txtProfitLoss.setEnabled(false);
        txtProfitLoss.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProfitLossKeyPressed(evt);
            }
        });
        jPanel1.add(txtProfitLoss);
        txtProfitLoss.setBounds(390, 300, 100, 30);

        jLabel9.setText("Finishing Date");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(530, 20, 100, 20);

        txtFinishingDate.setEnabled(false);
        jPanel1.add(txtFinishingDate);
        txtFinishingDate.setBounds(630, 10, 110, 37);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        txtByCompany.setText("100");
        txtByCompany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByCompanyKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtByCompanyKeyReleased(evt);
            }
        });
        jPanel4.add(txtByCompany);
        txtByCompany.setBounds(230, 10, 50, 37);

        jLabel17.setText("LOSS TO BE BORNE BY COMPANY");
        jPanel4.add(jLabel17);
        jLabel17.setBounds(10, 10, 240, 30);

        jLabel16.setText("LOSS TO BE BORNE BY PARTY");
        jPanel4.add(jLabel16);
        jLabel16.setBounds(350, 10, 210, 30);

        txtByParty.setText("0");
        txtByParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtByPartyActionPerformed(evt);
            }
        });
        txtByParty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtByPartyKeyReleased(evt);
            }
        });
        jPanel4.add(txtByParty);
        txtByParty.setBounds(550, 10, 50, 37);

        jLabel20.setText("%");
        jPanel4.add(jLabel20);
        jLabel20.setBounds(280, 10, 20, 30);

        jLabel22.setText("%");
        jPanel4.add(jLabel22);
        jLabel22.setBounds(600, 10, 20, 30);

        txtByCompanyAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtByCompanyAmtKeyReleased(evt);
            }
        });
        jPanel4.add(txtByCompanyAmt);
        txtByCompanyAmt.setBounds(230, 40, 80, 20);

        txtByPartyAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtByPartyAmtKeyReleased(evt);
            }
        });
        jPanel4.add(txtByPartyAmt);
        txtByPartyAmt.setBounds(550, 40, 80, 20);

        jLabel23.setText("Amout");
        jPanel4.add(jLabel23);
        jLabel23.setBounds(500, 40, 60, 20);

        jLabel24.setText("Amout");
        jPanel4.add(jLabel24);
        jLabel24.setBounds(170, 40, 60, 20);

        jLabel25.setText("Rs.");
        jPanel4.add(jLabel25);
        jLabel25.setBounds(630, 40, 53, 20);

        jLabel26.setText("Rs.");
        jPanel4.add(jLabel26);
        jLabel26.setBounds(310, 40, 20, 20);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(20, 360, 740, 70);

        jLabel21.setText("Profit / Loss ");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(310, 330, 100, 20);

        txtProfitLossPer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(txtProfitLossPer);
        txtProfitLossPer.setBounds(400, 330, 60, 20);

        jLabel18.setText("%");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(460, 300, 20, 20);

        jLabel27.setText("Th. Weight");
        jPanel1.add(jLabel27);
        jLabel27.setBounds(280, 110, 80, 30);

        txtThWeight.setEnabled(false);
        txtThWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtThWeightFocusLost(evt);
            }
        });
        jPanel1.add(txtThWeight);
        txtThWeight.setBounds(380, 110, 110, 30);

        jLabel28.setText("Macn, Pos");
        jPanel1.add(jLabel28);
        jLabel28.setBounds(280, 80, 90, 30);

        txtMachinePosition.setEnabled(false);
        txtMachinePosition.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMachinePositionFocusLost(evt);
            }
        });
        txtMachinePosition.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMachinePositionKeyPressed(evt);
            }
        });
        jPanel1.add(txtMachinePosition);
        txtMachinePosition.setBounds(380, 80, 110, 30);

        Tab.addTab("Diversion Entry", jPanel1);

        jPanel2.setLayout(null);

        Tab2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Tab2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab2FocusGained(evt);
            }
        });
        Tab2.setLayout(null);

        jLabel31.setText("Hierarchy ");
        Tab2.add(jLabel31);
        jLabel31.setBounds(10, 23, 66, 20);

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
        cmbHierarchy.setBounds(90, 20, 180, 37);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(10, 62, 56, 20);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFrom);
        txtFrom.setBounds(90, 60, 180, 37);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(10, 95, 62, 20);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(90, 95, 530, 37);

        jLabel36.setText("Your Action  ");
        Tab2.add(jLabel36);
        jLabel36.setBounds(10, 130, 81, 20);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        buttonGroup5.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });
        OpgApprove.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgApproveItemStateChanged(evt);
            }
        });
        OpgApprove.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgApproveFocusGained(evt);
            }
        });
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 171, 24);

        buttonGroup5.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });
        OpgFinal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgFinalItemStateChanged(evt);
            }
        });
        OpgFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpgFinalActionPerformed(evt);
            }
        });
        OpgFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgFinalFocusGained(evt);
            }
        });
        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 136, 20);

        buttonGroup5.add(OpgReject);
        OpgReject.setText("Reject");
        OpgReject.setEnabled(false);
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });
        OpgReject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgRejectItemStateChanged(evt);
            }
        });
        OpgReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpgRejectActionPerformed(evt);
            }
        });
        OpgReject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgRejectFocusGained(evt);
            }
        });
        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 136, 20);

        buttonGroup5.add(OpgHold);
        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        OpgHold.setEnabled(false);
        OpgHold.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgHoldMouseClicked(evt);
            }
        });
        OpgHold.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgHoldItemStateChanged(evt);
            }
        });
        OpgHold.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgHoldFocusGained(evt);
            }
        });
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        Tab2.add(jPanel6);
        jPanel6.setBounds(90, 130, 180, 100);

        jLabel33.setText("Send To");
        Tab2.add(jLabel33);
        jLabel33.setBounds(10, 253, 60, 20);

        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(90, 250, 180, 37);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(10, 292, 60, 20);

        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(90, 290, 570, 37);

        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(450, 400, 102, 30);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(630, 95, 33, 21);

        cmdNextToTab2.setMnemonic('N');
        cmdNextToTab2.setText("Next >>");
        cmdNextToTab2.setToolTipText("Next Tab");
        cmdNextToTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNextToTab2);
        cmdNextToTab2.setBounds(570, 400, 102, 30);

        jPanel2.add(Tab2);
        Tab2.setBounds(10, 0, 760, 460);

        Tab.addTab("Approval", jPanel2);

        jPanel3.setLayout(null);

        StatusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusPanel.setLayout(null);

        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 20);

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
        jScrollPane2.setViewportView(TableApprovalStatus);

        StatusPanel.add(jScrollPane2);
        jScrollPane2.setBounds(0, 40, 694, 120);

        jLabel19.setText("Document Update History");
        StatusPanel.add(jLabel19);
        jLabel19.setBounds(10, 170, 182, 20);

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
        jScrollPane6.setViewportView(TableUpdateHistory);

        StatusPanel.add(jScrollPane6);
        jScrollPane6.setBounds(10, 190, 540, 130);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        StatusPanel.add(cmdViewHistory);
        cmdViewHistory.setBounds(570, 170, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        StatusPanel.add(cmdNormalView);
        cmdNormalView.setBounds(570, 200, 132, 24);

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
        txtAuditRemarks.setBounds(570, 260, 129, 37);

        jButton4.setText("Next >>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton4);
        jButton4.setBounds(660, 290, 100, 30);

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(560, 290, 100, 30);

        jPanel3.add(StatusPanel);
        StatusPanel.setBounds(10, 0, 790, 380);

        Tab.addTab("Status", jPanel3);

        getContentPane().add(Tab);
        Tab.setBounds(0, 70, 930, 560);

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
        ToolBar.add(cmdPrint);

        cmdExit.setToolTipText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        ToolBar.add(cmdExit);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 930, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Felt Sales Order Diversion Prior Approval");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 930, 25);

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus1);
        lblStatus1.setBounds(0, 630, 930, 30);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 630, 930, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);//GAURANG        
        GenerateSendToCombo();

        if (clsHierarchy.CanSkip((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }
        if (clsHierarchy.CanFinalApprove((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        } else {
            OpgApprove.setEnabled(false);
            OpgApprove.setSelected(false);
        }
        if (clsHierarchy.IsCreator((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            OpgApprove.setEnabled(true);
            OpgReject.setEnabled(false);
            OpgReject.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        lblStatus.setText("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked

        if (!OpgApprove.isEnabled()) {
            return;
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedSendToCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(ModuleId, S_O_NO.getText())) {
                cmbSendTo.setEnabled(true);
                txtToRemarks.setEnabled(true);
                txtFromRemarks.setEnabled(true);
            } else {
                cmbSendTo.setEnabled(false);
            }
        }
        if (cmbSendTo.getItemCount() <= 0) {
            GenerateSendToCombo();
        }

        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);
        OpgApprove.setSelected(true);
        OpgHold.setSelected(false);
        txtToRemarks.setEnabled(false);
        if (!OpgApprove.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgApproveMouseClicked
    private void GenerateRejectedSendToCombo() {
        HashMap hmRejectedSendToList = new HashMap();

        cmbSendToModel = new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbSendToModel);

        //Now Add other hierarchy Users
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        DOC_NO = S_O_NO.getText();

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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(ModuleId, DOC_NO + "", ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                }
                if (OpgReject.isSelected()) {
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(ModuleId, DOC_NO + "", ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                }
                if (IncludeUser && (((int) ObjUser.getAttribute("USER_ID").getVal()) != EITLERPGLOBAL.gNewUserID)) {
                    cmbSendToModel.addElement(aData);
                }
            } else {
                if ((ObjUser.getAttribute("USER_ID").getInt()) != EITLERPGLOBAL.gNewUserID) {
                    cmbSendToModel.addElement(aData);
                }
            }
        }
        if (EditMode == EITLERPGLOBAL.EDIT) {
            int Creator = clsFeltProductionApprovalFlow.getCreator(ModuleId, DOC_NO + "");
            EITLERPGLOBAL.setComboIndex(cmbSendTo, Creator);
        }
    }
    private void OpgApproveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgApproveItemStateChanged

    }//GEN-LAST:event_OpgApproveItemStateChanged

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

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

    private void OpgFinalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgFinalItemStateChanged

    }//GEN-LAST:event_OpgFinalItemStateChanged

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained

    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked

        if (!OpgReject.isEnabled()) {
            return;
        }

        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgRejectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgRejectItemStateChanged
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectItemStateChanged

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained

    }//GEN-LAST:event_OpgRejectFocusGained

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);
        OpgHold.setSelected(true);
    }//GEN-LAST:event_OpgHoldMouseClicked


    private void OpgHoldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgHoldItemStateChanged

    }//GEN-LAST:event_OpgHoldItemStateChanged

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained

    }//GEN-LAST:event_OpgHoldFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained

    }//GEN-LAST:event_cmbSendToFocusGained

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained

    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmdBackToTab0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab0ActionPerformed

    }//GEN-LAST:event_cmdBackToTab0ActionPerformed

    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed

    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed

    private void cmdNextToTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab2ActionPerformed

    }//GEN-LAST:event_cmdNextToTab2ActionPerformed

    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained

    }//GEN-LAST:event_Tab2FocusGained

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        String DocNo = S_O_NO.getText();
        feltOrder.ShowHistory(DocNo);
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        feltOrder.HistoryView = false;
        feltOrder.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed

    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

    }//GEN-LAST:event_jButton5ActionPerformed

    private void TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabMouseClicked

    }//GEN-LAST:event_TabMouseClicked

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        MoveNext();

    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        Add();
        txtThWeight.setEnabled(true);
        txtMachinePosition.setEnabled(true);
        txtPartyCode.setEnabled(true);
        txtpiece.setEnabled(true);
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Delete();
        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        Save();
        txtThWeight.setEnabled(false);
        txtMachinePosition.setEnabled(false);
        txtPartyCode.setEnabled(false);
        txtpiece.setEnabled(false);
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void REMARKFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REMARKFocusGained
        // TODO add your handling code here:
        lblStatus1.setText("Enter Order Remark");
    }//GEN-LAST:event_REMARKFocusGained

    private void REMARKFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REMARKFocusLost
        // TODO add your handling code here:
        lblStatus1.setText("");
    }//GEN-LAST:event_REMARKFocusLost

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.OrderDiversion.frmReportFeltOrderDiversion", true);
        frmReportFeltOrderDiversion ObjFindFeltorder = (frmReportFeltOrderDiversion) ObjLoader.getObj();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void OpgFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpgFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpgFinalActionPerformed

    private void OpgRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpgRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpgRejectActionPerformed

    private void REMARK1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REMARK1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_REMARK1FocusGained

    private void REMARK1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REMARK1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_REMARK1FocusLost

    private void txtpieceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpieceKeyPressed
        EditMode = EITLERPGLOBAL.ADD;
        if (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT) {

            if (evt.getKeyCode() == 112) //F1 Key pressed
            {
                String SQL = "SELECT * FROM (SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK "
                        + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_MACHINE_NO!='' AND PR_POSITION_NO!='' AND PR_LENGTH!='' AND PR_WIDTH!='' AND PR_GSM!='' AND PR_DIVERSION_FLAG='READY' AND PR_PIECE_STAGE IN ('SEAMING','SPLICING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK')  "
                        + " UNION ALL SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK "
                        + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE "
                        + "PR_MACHINE_NO!='' AND PR_POSITION_NO!='' AND  "
                        + "PR_LENGTH!='' AND PR_WIDTH!='' AND PR_GSM!='' AND "
                        + " PR_DIVERSION_FLAG='READY' "
                        + "AND PR_PIECE_STAGE IN ('SEAMING','SPLICING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK') "
                        + "AND PR_DELINK!='OBSOLETE') AS AA "
                        + "WHERE LEFT(PR_PIECE_NO,5) NOT IN "
                        + "(SELECT LEFT(D_PIECE_NO,5) FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE CANCELED=0) "
                        + " UNION ALL SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK FROM "
                        + " PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_MACHINE_NO!='' AND PR_POSITION_NO!='' AND  PR_LENGTH!='' AND PR_WIDTH!='' AND PR_GSM!='' AND  "
                        + " (PR_DIVERSION_FLAG='InProcess') AND PR_PIECE_STAGE IN ('SEAMING','SPLICING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK') " 
                        + " AND PR_PIECE_NO IN (SELECT distinct(PIECE_NO) FROM  PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST "
                        + " WHERE DIVERSION_NO!='') ";
                System.out.println("DIVERSION SQL : "+SQL);
                searchkey_diversionlist search_dv = new searchkey_diversionlist();

                search_dv.SQL = SQL;
                search_dv.ReturnCol = 1;
                search_dv.Party_Code = "";
                search_dv.ShowReturnCol = true;
                search_dv.DefaultSearchOn = 1;

                if (search_dv.ShowRSLOV()) {
                    txtpiece.setText(search_dv.ReturnVal);
                    txtpiece.setText(txtpiece.getText().trim());
                    getPieceDetail();
                }
            }
        }
    }//GEN-LAST:event_txtpieceKeyPressed

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
        
        if (evt.getKeyCode() == 112)
        {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CLOSE_IND!=1 ";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    txtPartyCode.setText(aList.ReturnVal);
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        FindProfitLoss();
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void txtlengthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtlengthFocusLost
        // TODO add your handling code here:
        FindProfitLoss();
    }//GEN-LAST:event_txtlengthFocusLost

    private void txtwidthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtwidthFocusLost
        // TODO add your handling code here:
        FindProfitLoss();
    }//GEN-LAST:event_txtwidthFocusLost

    private void txtgsmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtgsmFocusLost
        // TODO add your handling code here:
        FindProfitLoss();
    }//GEN-LAST:event_txtgsmFocusLost

    private void txtwtsqmtrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtwtsqmtrFocusLost
        // TODO add your handling code here:
        FindProfitLoss();
    }//GEN-LAST:event_txtwtsqmtrFocusLost

    private void txtProductCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductCodeFocusLost
        // TODO add your handling code here:
        FindProfitLoss();
    }//GEN-LAST:event_txtProductCodeFocusLost

    private void txtProfitLossKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProfitLossKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProfitLossKeyPressed

    private void txtByPartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtByPartyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByPartyActionPerformed

    private void txtByCompanyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByCompanyKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtByCompanyKeyPressed

    private void txtByCompanyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByCompanyKeyReleased
        // TODO add your handling code here:
        if(!txtByCompany.getText().equals(""))
        {
            double comp_per = Double.parseDouble(txtByCompany.getText());
            txtByParty.setText(""+(100-comp_per));
            
            if(!txtProfitLoss.getText().equals(""))
            {
                double profit_loss = Double.parseDouble(txtProfitLoss.getText());
                
                double bear_by_company = ( profit_loss * comp_per ) / 100;
                double bear_by_party = ( profit_loss * (100-comp_per) ) / 100;
                
                txtByCompanyAmt.setText(EITLERPGLOBAL.round(bear_by_company,2)+"");
                txtByPartyAmt.setText(EITLERPGLOBAL.round(bear_by_party,2)+"");
                
                
            }
            
        }
    }//GEN-LAST:event_txtByCompanyKeyReleased

    private void txtByPartyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByPartyKeyReleased
        // TODO add your handling code here:
        
        if(!txtByParty.getText().equals(""))
        {
            double party_per = Double.parseDouble(txtByParty.getText());
            txtByCompany.setText(""+(100-party_per));
            
            if(!txtProfitLoss.getText().equals(""))
            {
                double profit_loss = Double.parseDouble(txtProfitLoss.getText());
                
                double bear_by_Company = ( profit_loss * (100-party_per) ) / 100;
                double bear_by_party = ( profit_loss * party_per ) / 100;
                
                txtByCompanyAmt.setText(EITLERPGLOBAL.round(bear_by_Company,0)+"");
                txtByPartyAmt.setText(EITLERPGLOBAL.round(bear_by_party,0)+"");
            }
            
        }
        
    }//GEN-LAST:event_txtByPartyKeyReleased

    private void txtByCompanyAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByCompanyAmtKeyReleased
        // TODO add your handling code here:
        if(!txtByCompanyAmt.getText().equals(""))
        {
            if(!txtProfitLoss.getText().equals(""))
            {
                double comp_amt = Double.parseDouble(txtByCompanyAmt.getText());
                double profit_loss = Double.parseDouble(txtProfitLoss.getText());
                double party_amt = profit_loss - comp_amt;
                txtByPartyAmt.setText(""+party_amt);
                
                double comp_per = EITLERPGLOBAL.round(((comp_amt * 100) / profit_loss),0);
                double party_per = EITLERPGLOBAL.round(((party_amt * 100) / profit_loss),0);
                  
                txtByCompany.setText(comp_per+"");
                txtByParty.setText(party_per+"");
            }
            
        }
        
    }//GEN-LAST:event_txtByCompanyAmtKeyReleased

    private void txtByPartyAmtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByPartyAmtKeyReleased
        // TODO add your handling code here:
        if(!txtByPartyAmt.getText().equals(""))
        {
            if(!txtProfitLoss.getText().equals(""))
            {
                double party_amt = Double.parseDouble(txtByPartyAmt.getText());
                double profit_loss = Double.parseDouble(txtProfitLoss.getText());
                double comp_amt = profit_loss - party_amt;
                txtByCompanyAmt.setText(""+comp_amt);
                
                double comp_per = EITLERPGLOBAL.round(((comp_amt * 100) / profit_loss),2);
                double party_per = EITLERPGLOBAL.round(((party_amt * 100) / profit_loss),2);
                  
                txtByCompany.setText(comp_per+"");
                txtByParty.setText(party_per+"");
            }
            
        }
    }//GEN-LAST:event_txtByPartyAmtKeyReleased

    private void txtgsmKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtgsmKeyReleased
        // TODO add your handling code here:
        String PR_NEEDLING_WEIGHT = data.getStringValueFromDB("SELECT PR_NEEDLING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+txtpiece.getText()+"'");
        String PR_WEAVING_WEIGHT = data.getStringValueFromDB("SELECT PR_WEAVING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+txtpiece.getText()+"'");
        String PieceStage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+txtpiece.getText()+"'");
        if("FINISHING".equals(PieceStage)) {
            float LENGTH = Float.parseFloat(txtlength.getText());
            float WIDTH = Float.parseFloat(txtwidth.getText());
            float Weight = Float.parseFloat(PR_NEEDLING_WEIGHT);
            float calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
            txtgsm.setText(calculated_GSM+"");
        }
        else if("MENDING".equals(PieceStage) || "NEEDLING".equals(PieceStage)) {
            float LENGTH = Float.parseFloat(txtlength.getText());
            float WIDTH = Float.parseFloat(txtwidth.getText());
            float Weight = Float.parseFloat(PR_WEAVING_WEIGHT);
            float calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
            txtgsm.setText(calculated_GSM+"");
        }
    }//GEN-LAST:event_txtgsmKeyReleased

    private void txtlengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtlengthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtlengthActionPerformed

    private void txtMachinePositionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMachinePositionKeyPressed
        // TODO add your handling code here:
        
        if (txtPartyCode.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please select PARTY");
            txtPartyCode.requestFocus();
        } else {
            String minqlt = "";
            
            searchkey search = new searchkey();
            search.SQL = "SELECT MM_MACHINE_NO AS MACHINE_NO,MM_MACHINE_POSITION AS POSITION,MM_MACHINE_POSITION_DESC AS POSITION_DESC,MM_ITEM_CODE AS ITEM_CODE,MM_GRUP AS GRUP,(MM_FELT_LENGTH+MM_FABRIC_LENGTH) AS LENGTH,(MM_FELT_WIDTH+MM_FABRIC_WIDTH) AS WIDTH,MM_FELT_GSM AS GSM,concat(MM_FELT_STYLE,MM_STYLE_DRY) AS STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='" + txtPartyCode.getText() + "' AND (MM_MACHINE_NO!='' AND MM_MACHINE_POSITION!='' AND (MM_FELT_LENGTH != '' OR MM_FABRIC_LENGTH != '') AND (MM_FELT_WIDTH != '' OR MM_FABRIC_WIDTH != '') AND MM_FELT_GSM!='') ORDER BY  MM_MACHINE_NO,MM_MACHINE_POSITION";
            search.ReturnCol = 1;
            search.Party_Code = txtPartyCode.getText();
            search.ShowReturnCol = true;
            search.DefaultSearchOn = 1;
            //search.setsearchText(minqlt);
            if (search.ShowRSLOV()) {
                
                seleval = search.ReturnVal;
                seltyp = search.ReturnVal;
                String secondval = search.SecondVal;
                //JOptionPane.showMessageDialog(null, "COMBINATION CODE = "+search.ReturnVal+search.SecondVal);
                String LastQuery = "SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_ITEM_CODE,MM_GRUP,(MM_FELT_LENGTH+MM_FABRIC_LENGTH),(MM_FELT_WIDTH+MM_FABRIC_WIDTH),MM_FELT_GSM,concat(MM_FELT_STYLE,MM_STYLE_DRY) as MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL where CONCAT(MM_COMBINATION_CODE,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM) like '" + search.ReturnVal + search.SecondVal + "%' AND MM_PARTY_CODE='" + txtPartyCode.getText() + "' AND (MM_MACHINE_NO!='' AND MM_MACHINE_POSITION!='' AND (MM_FELT_LENGTH != '' OR MM_FABRIC_LENGTH != '') AND (MM_FELT_WIDTH != '' OR MM_FABRIC_WIDTH != '')  AND MM_FELT_GSM!='')  ORDER BY  MM_MACHINE_NO,MM_MACHINE_POSITION";
                System.out.println("Machine Master Query = " + LastQuery);

                Connection Conn;
                Statement stmt;
                ResultSet rsData;
                String Order_Group = "";
                String[] Piecedetail = {"", "", "", "", "", "", "", "", "", "", "", "", ""};

                try {
                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    rsData = stmt.executeQuery(LastQuery);
                    rsData.first();

                    Piecedetail[0] = rsData.getString(1);
                    if (rsData.getString(2).length() == 1) {
                        Piecedetail[1] = "0" + rsData.getString(2);
                    } else {
                        Piecedetail[1] = rsData.getString(2);
                    }
                    
                    Piecedetail[2] = rsData.getString(3);
                    Piecedetail[3] = rsData.getString(4);
                    Order_Group = rsData.getString("MM_GRUP");
                    Piecedetail[5] = rsData.getString(6);
                    Piecedetail[6] = rsData.getString(7);
                    Piecedetail[7] = rsData.getString(8);
                    Piecedetail[8] = rsData.getString(9);
                    
                    float Theoritical_Weigth = (Float.parseFloat(Piecedetail[5]) * Float.parseFloat(Piecedetail[6]) * Float.parseFloat(Piecedetail[7]) / 1000);

                    //float SQMT = (Float.parseFloat(Piecedetail[5]) * Float.parseFloat(Piecedetail[6]));
                    txtlength.setText(Float.parseFloat(Piecedetail[5])+"");
                    txtwidth.setText(Float.parseFloat(Piecedetail[6])+"");
                    txtgsm.setText(Float.parseFloat(Piecedetail[7])+"");
                    txtThWeight.setText(Theoritical_Weigth + "");
                    txtMachinePosition.setText(Piecedetail[0]+","+Piecedetail[1]);
                    //txtwtsqmtr.setText(Theoritical_Weigth + "");
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        
    }//GEN-LAST:event_txtMachinePositionKeyPressed

    private void txtMachinePositionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMachinePositionFocusLost
        // TODO add your handling code here:
        FindProfitLoss();
    }//GEN-LAST:event_txtMachinePositionFocusLost

    private void txtThWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtThWeightFocusLost
        // TODO add your handling code here:
        FindProfitLoss();
    }//GEN-LAST:event_txtThWeightFocusLost
    private void MoveFirst() {
        feltOrder.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        feltOrder.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        feltOrder.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        feltOrder.MoveLast();
        DisplayData();
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.OrderDiversion.frmFindFeltOrderDiversion", true);
        frmFindFeltOrderDiversion ObjFindFeltorder = (frmFindFeltOrderDiversion) ObjLoader.getObj();

        if (ObjFindFeltorder.Cancelled == false) {
            if (!feltOrder.Filter(ObjFindFeltorder.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.", "Find Felt Diversion", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    // find all pending document
    public void FindWaiting() {
        //  feltOrder.Filter(" PROD_DOC_NO IN (SELECT DISTINCT PROD_DOC_NO FROM PRODUCTION.FELT_PROD_DATA, PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+EITLERPGLOBAL.gNewUserID+" AND STATUS='W' AND MODULE_ID="+ModuleId+" AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    
    
    private void Add() {
        //  EditMode=EITLERPGLOBAL.ADD;

        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }

        EditMode = EITLERPGLOBAL.ADD;

        SetFields(true);
        DisableToolbar();

        lblTitle.setBackground(new Color(0, 102, 153));
        lblTitle.setForeground(Color.WHITE);

        clearFields();
        SetupApproval();
        S_O_DATE.requestFocus();

        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = ModuleId;
        aList.FirstFreeNo = 263;
        S_O_DATE.setText(df.format(new Date()));
        FFNo = aList.FirstFreeNo;
        S_O_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, false));
        lblTitle.setText("Felt Sales Order Diversion Prior Approval - " + S_O_NO.getText());
        tblexist.removeAll();
        
        DataModel = new EITLTableModel();
        tblexist.removeAll();

        tblexist.setModel(DataModel);
        tblexist.setAutoResizeMode(0);

        DataModel.addColumn("Description"); //0 - Read Only
        DataModel.addColumn("Value"); //0 - Read Only

        tbldiverted.setModel(DataModel);
    }

    private void Save() {

        

        SetData();

        if (cmbHierarchy.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select the hierarchy.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(this, "Select the Approval Action.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter the remarks for rejection", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((OpgApprove.isSelected() || OpgReject.isSelected()) && cmbSendTo.getItemCount() <= 0) {
            JOptionPane.showMessageDialog(this, "Select the user, to whom rejected document to be send", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //feltOrder.LoadData();
        if (EditMode == EITLERPGLOBAL.ADD) {
            if (feltOrder.Insert()) {
                SelectFirstFree aList = new SelectFirstFree();
                aList.ModuleID = ModuleId;
                aList.FirstFreeNo = 263;
                FFNo = aList.FirstFreeNo;
                clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, true);

         
                if (OpgFinal.isSelected()) {

                    try {
                        String DOC_NO = S_O_NO.getText();
                        String DOC_DATE = S_O_DATE.getText();
                        String Party_Code = "";

                        String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
                        System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + feltOrder.LastError, " SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (feltOrder.Update()) {
                if (OpgFinal.isSelected()) {

                    
                        //String DOC_NO = S_O_NO.getText();
                        //String DOC_DATE = S_O_DATE.getText();
                        //String Party_Code = "";

                        ///String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
                        //System.out.println("Send Mail Responce : " + responce);

                        try {

                                    
                                    int userId = EITLERPGLOBAL.gNewUserID;
                                    int hierarchyId = SelHierarchyID;

                                    String docNo = S_O_NO.getText();
                                    String partyCode = "";
                                    
                                    String pSubject = "Notification : Felt Diversin Prior Approval : " + docNo;
                                    String pMessage = "";
                                    String cc = "";
                                    String prodPcHeader = "";

                                    if (!OpgFinal.isSelected()) {
                                        pMessage = "<br>Diversin Prior Approval No : " + docNo + " has been approve and forward by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, userId) + ".<br><br>";
                                    } else {
                                        pMessage = "<br>Diversin Prior Approval No : " + docNo + " has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, userId) + ".<br><br>";
                                    }

                                    

                                    pMessage = pMessage + "<br>Document No : " + docNo + "";
                                    pMessage = pMessage + "<br>Document Date : " + S_O_DATE.getText() + "";
                                    
                                    
                                    Connection Conn1;
                                    Statement stmt1;
                                    ResultSet rsData1;

                                    Conn1 = data.getConn();
                                    stmt1 = Conn1.createStatement();
                                    //rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO ='" + docNo + "'");
                                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL where SD_ORDER_NO='" + docNo + "'");
                                    System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL where SD_ORDER_NO='" + docNo + "'");
                                    rsData1.first();

                                    pMessage = pMessage + "<br><br> ORIGINAL PIECE DETAILS <br>";
                                    pMessage = pMessage + "<table border='1'>"
                                            + "<tr>"
                                            + "<th align='center'> Piece No. </th>"
                                            + "<th align='center'> Party Code. </th>"
                                            + "<th align='center'> Party Name </th>"
                                            + "<th align='center'> Product code </th>"
                                            + "<th align='center'> Machine No </th>"
                                            + "<th align='center'> Position </th>"
                                            + "<th align='center'> Position Desc </th>"
                                            + "<th align='center'> Length </th>"
                                            + "<th align='center'> Width </th>"
                                            + "<th align='center'> GSM </th>"
                                            + "<th align='center'> SQRMTR / Weight </th>"
                                            + "<th align='center'> STYLE </th>"
                                            + "<th align='center'> GROUP </th>"
                                            + "<th align='center'> Amount </th>"
                                            + "</tr>";

                                    rsData1.first();
                                    ResultSet piecers = null;
                                    piecers = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + txtpiece.getText() + "'");
                                    piecers.first();
                                    if (piecers.getRow() > 0) {
                                            pMessage = pMessage + ""
                                                    + "<tr>"
                                                    + "<td align='center'> " + txtpiece.getText() + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("ORIGINAL_PARTY_CODE") + " </td>"
                                                    + "<td align='center'> " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("ORIGINAL_PARTY_CODE")) + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("ORIGINAL_PRODUCT_NO")  + " </td>"
                                                    + "<td align='center'> " + piecers.getString("PR_MACHINE_NO") + " </td>"
                                                    + "<td align='center'> " + piecers.getString("PR_POSITION_NO") + " </td>"
                                                    + "<td align='center'> " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + piecers.getString("PR_POSITION_NO") + "'") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("ORIGINAL_LENGTH") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("ORIGINAL_WIDTH") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("ORIGINAL_GSM") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("ORIGINAL_THORITICAL_WEIGHT") + " </td>"
                                                    + "<td align='center'> " + piecers.getString("PR_BILL_STYLE") + " </td>"
                                                    + "<td align='center'> " + piecers.getString("PR_GROUP") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("BASE_EXISTING_PIECE_AMT") + " </td>"
                                                    + "</tr>";
                                           
                                    }
                                    pMessage = pMessage + "</table>";
                                    
                                    pMessage = pMessage + "<br><br> ORDER PIECE DETAILS <br>";
                                    pMessage = pMessage + "<table border='1'>"
                                            + "<tr>"
                                            + "<th align='center'> Piece No. </th>"
                                            + "<th align='center'> Party Code. </th>"
                                            + "<th align='center'> Party Name </th>"
                                            + "<th align='center'> Product code </th>"
                                            + "<th align='center'> Length </th>"
                                            + "<th align='center'> Width </th>"
                                            + "<th align='center'> GSM </th>"
                                            + "<th align='center'> SQRMTR / Weight </th>"
                                            + "<th align='center'> Order Amount </th>"
                                            + "</tr>";

                                    rsData1.first();
                                    piecers.first();
                                    if (piecers.getRow() > 0) {
                                            pMessage = pMessage + ""
                                                    + "<tr>"
                                                    + "<td align='center'> " + rsData1.getString("D_PIECE_NO") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("D_PARTY_CODE") + " </td>"
                                                    + "<td align='center'> " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("D_PARTY_CODE")) + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("D_PRODUCT_NO")  + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("D_LENGTH") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("D_WIDTH") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("D_GSM") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("D_THORITICAL_WEIGHT") + " </td>"
                                                    + "<td align='center'> " + rsData1.getString("BASE_ORDER_AMT") + " </td>"
                                                    + "</tr>";
                                           
                                    }
                                    pMessage = pMessage + "</table>";
                                    
                                    pMessage = pMessage + "<br>";
                                    pMessage = pMessage + "<br>Cost Bear By Company "+rsData1.getString("COST_BY_COMPANY_PER")+"% , Amount : "+rsData1.getString("COST_BY_COMPANY_AMT");
                                    pMessage = pMessage + "<br>Cost Bear By Party "+rsData1.getString("COST_BY_PARTY_PER")+"% , Amount : "+rsData1.getString("COST_BY_PARTY_AMT");
                                    pMessage = pMessage + "<br>";
                                    

                                    pMessage = pMessage + "<br><br>All Approvers Remark as given below : ";

                                    pMessage += "</table>";
                                    pMessage += "<table border=1>";
                                    pMessage += "<tr><td align='center'><b> Sr.No </b></td>"
                                            + "<td align='center'><b> User </b></td>"
                                            + "<td align='center'><b> Date  </b></td>"
                                            + "<td align='center'><b> Status </b></td>"
                                            + "<td align='center'><b> Remark </b></td>"
                                            + "</tr>";

                                    HashMap hmApprovalHistory = clsFeltOrderDiversionLoss.getHistoryList(EITLERPGLOBAL.gCompanyID+"", docNo);
                                    for (int ij = 1; ij <= hmApprovalHistory.size(); ij++) {
                                        pMessage += "<tr>";

                                        clsFeltOrderDiversionLoss ObjHistory = (clsFeltOrderDiversionLoss) hmApprovalHistory.get(Integer.toString(ij));
                                        pMessage += "<td>" + Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal()) + "</td>";

                                        pMessage += "<td>" + clsUser.getUserName(2, (int) ObjHistory.getAttribute("UPDATED_BY").getVal()) + "</td>";
                                        
                                        pMessage += "<td>" +  ObjHistory.getAttribute("ENTRY_DATE").getString() + "</td>";
                                        
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
                                    pMessage += "<br><br>";
                                    pMessage += "<br>";

                    //            HashMap hmSendToList;
                                    String recievers = "sdmlerp@dineshmills.com,vdshanbhag@dineshmills.com";

                    //            pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
                    //            hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, hierarchyId, userId, true);
                    //            for (int i = 1; i <= hmSendToList.size(); i++) {
                    //                clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    //                int U_ID = ObjUser.getAttribute("USER_ID").getInt();
                    //
                    //                String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
                    //
                    //                System.out.println("USERID : " + U_ID + ", send_to : " + to);
                    //                if (!to.equals("")) {
                    //                    recievers = recievers + "," + to;
                    //                    pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
                    //                }
                    //            }
                    //            recievers = recievers + "aditya@dineshmills.com,vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com";
                                    pMessage = pMessage + "<br><br><br><br>**** This is an auto-generated email, please do not reply ****";

                                    System.out.println("Recivers : " + recievers);
                                    System.out.println("pSubject : " + pSubject);
                                    System.out.println("pMessage : " + pMessage);

                                    String responce = MailNotification.sendNotificationMail(ModuleId, pSubject, pMessage, recievers, cc, hierarchyId);
                                    System.out.println("Send Mail Responce : " + responce);

                                } catch (Exception e) {
                                    System.out.println("Error on Mail: " + e.getMessage());
                                }
                    
                }
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving editing. Error is " + feltOrder.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        EditMode = 0;

        EnableToolbar();
        SetMenuForRights();
        SetFields(false);
        try {
            if (PENDING_DOCUMENT) {
                frmPA.RefreshView();
                PENDING_DOCUMENT = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Cancel() {
        DisplayData();
        EditMode = 0;

        EnableToolbar();
        SetMenuForRights();
        SetFields(false);
    }

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

    private void Edit() {

        String productionDocumentNo = (String) feltOrder.getAttribute("SD_ORDER_NO").getObj();
        if (feltOrder.IsEditable(productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            SetupApproval();
            DisplayData();

            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, productionDocumentNo)) {
                SetFields(true);
            } else {
                EnableApproval();
            }
        } else {
            JOptionPane.showMessageDialog(this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "EDITING ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    // find rate update by doc no
    public void Find(String docNo) {
        feltOrder.Filter(" SD_ORDER_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    private void Delete() {
        if (feltOrder.CanDelete(S_O_NO.getText(), S_O_DATE.getText(), EITLERPGLOBAL.gNewUserID)) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, feltOrder.LastError, "DELETION ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void GenerateHierarchyCombo() {
        HashMap hmHierarchyList = new HashMap();

        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        hmHierarchyList = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId + " ");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            hmHierarchyList = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleId + " ");
        }
        for (int i = 1; i <= hmHierarchyList.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) hmHierarchyList.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
    }

    private void filter(ArrayList<String> Hierarchy) {
        for (String current : Hierarchy) {
            cmbHierarchyModel.setSelectedItem(current);
        }
    }

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
                    aData.Text = ObjUser.getAttribute("USER_NAME").getString();

                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    } else {
                        cmbSendToModel.addElement(aData);
                    }
                }
            } else {
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, S_O_NO.getText());
                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = ObjUser.getAttribute("USER_NAME").getString();
                    cmbSendToModel.addElement(aData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
    }

    private void SetFields(boolean pStat) {
        S_O_DATE.setEnabled(pStat);
        S_O_NO.setEnabled(pStat);
        REMARK.setEnabled(pStat);
        REMARK1.setEnabled(pStat);
        reason.setEnabled(pStat);
        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        txtpiece.setEnabled(pStat);
        txtPartyCode.setEnabled(pStat);
        txtlength.setEnabled(pStat);
        txtwidth.setEnabled(pStat);
        txtgsm.setEnabled(pStat);
        txtwtsqmtr.setEnabled(pStat);
        txtProductCode.setEnabled(pStat);
        //txtProfitLoss.setEnabled(pStat);
        txtThWeight.setEnabled(false);
        txtMachinePosition.setEnabled(false);
        txtPartyCode.setEnabled(false);
        txtpiece.setEnabled(false);
        SetupApproval();
    }

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

    private void SetData() {
        
        String Exist_Party_Code = tblexist.getValueAt(1, 1).toString();
              
        String Exist_Piece_No = tblexist.getValueAt(5, 1).toString();
        
        String Exist_Product_Code = tblexist.getValueAt(7, 1).toString();
        
        
        
        feltOrder.setAttribute("SD_ORDER_NO", S_O_NO.getText());
        feltOrder.setAttribute("SD_ORDER_DATE", S_O_DATE.getText());
        
        feltOrder.setAttribute("D_PARTY_CODE", txtPartyCode.getText());
        
        feltOrder.setAttribute("D_REMARK", REMARK.getText());
        feltOrder.setAttribute("D_REASON_FOR_DIVERSION", reason.getSelectedItem().toString());
        feltOrder.setAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT", REMARK1.getText());
        feltOrder.setAttribute("D_PIECE_NO", tbldiverted.getValueAt(5, 1).toString());
        feltOrder.setAttribute("D_PRODUCT_NO", txtProductCode.getText());
        feltOrder.setAttribute("D_PRODUCT_DESC", "");
        
        feltOrder.setAttribute("D_MACHINE_NO", "");
        feltOrder.setAttribute("D_POSITION_NO", "");
        feltOrder.setAttribute("D_POSITION_DESC", "");
        feltOrder.setAttribute("D_STYLE_NO", "");
        feltOrder.setAttribute("D_GROUP", "");
        feltOrder.setAttribute("D_LENGTH", tbldiverted.getValueAt(11, 1).toString());
        feltOrder.setAttribute("D_WIDTH", tbldiverted.getValueAt(13, 1).toString());
        feltOrder.setAttribute("D_GSM", tbldiverted.getValueAt(15, 1).toString());
        feltOrder.setAttribute("D_THORITICAL_WEIGHT", tbldiverted.getValueAt(17, 1).toString());
        feltOrder.setAttribute("D_SQ_MTR", tbldiverted.getValueAt(17, 1).toString());
        feltOrder.setAttribute("D_SYN_PER", "");
        feltOrder.setAttribute("D_OV_RATE", tbldiverted.getValueAt(19, 1).toString());
        feltOrder.setAttribute("D_OV_BAS_AMOUNT", tbldiverted.getValueAt(21, 1).toString());
        feltOrder.setAttribute("D_OV_CHEM_TRT_CHG", tbldiverted.getValueAt(23, 1).toString());
        feltOrder.setAttribute("D_OV_SPIRAL_CHG", "0");
        feltOrder.setAttribute("D_OV_PIN_CHG", "0");
        feltOrder.setAttribute("D_OV_SEAM_CHG", tbldiverted.getValueAt(22, 1).toString());
        feltOrder.setAttribute("D_OV_AMT", tbldiverted.getValueAt(24, 1).toString());

        feltOrder.setAttribute("ORIGINAL_PARTY_CODE", tblexist.getValueAt(1, 1).toString());
        feltOrder.setAttribute("ORIGINAL_PIECE_NO", tblexist.getValueAt(5, 1).toString());
        feltOrder.setAttribute("ORIGINAL_PRODUCT_NO", tblexist.getValueAt(9, 1).toString());
        feltOrder.setAttribute("ORIGINAL_MACHINE_NO", "");
        feltOrder.setAttribute("ORIGINAL_POSITION_NO", "");
        feltOrder.setAttribute("ORIGINAL_STYLE_NO", "");
        feltOrder.setAttribute("ORIGINAL_GROUP", "");
        feltOrder.setAttribute("ORIGINAL_LENGTH", tblexist.getValueAt(11, 1).toString());
        feltOrder.setAttribute("ORIGINAL_WIDTH", tblexist.getValueAt(13, 1).toString());
        feltOrder.setAttribute("ORIGINAL_GSM", tblexist.getValueAt(15, 1).toString());
        feltOrder.setAttribute("ORIGINAL_THORITICAL_WEIGHT", tblexist.getValueAt(17, 1).toString());
        feltOrder.setAttribute("ORIGINAL_SQ_MTR", tblexist.getValueAt(17, 1).toString());
        feltOrder.setAttribute("ORIGINAL_OV_RATE", tblexist.getValueAt(19, 1).toString());
        feltOrder.setAttribute("ORIGINAL_OV_AMOUNT", tblexist.getValueAt(21, 1).toString());
        

        feltOrder.setAttribute("DEBIT_NOTE_NO", "");
        feltOrder.setAttribute("DEBIT_AMT", "");
        feltOrder.setAttribute("COST_BEARER", "SDML");
        feltOrder.setAttribute("BEARER_PARTY_CODE", "");
        feltOrder.setAttribute("BEARER_PARTY_NAME", "");
        feltOrder.setAttribute("DISCOUNT_PER", "");
        feltOrder.setAttribute("DISCOUNT_AMT", "");
        feltOrder.setAttribute("DIFFERENCE_AMT", txtProfitLoss.getText());
        feltOrder.setAttribute("CGST_PER", "");
        feltOrder.setAttribute("CGST_AMT", "");
        feltOrder.setAttribute("SGST_PER", "");
        feltOrder.setAttribute("SGST_AMT", "");
        feltOrder.setAttribute("IGST_PER", "");
        feltOrder.setAttribute("IGST_AMT", "");
        feltOrder.setAttribute("COMPOSITION_PER", 0);
        feltOrder.setAttribute("COMPOSITION_AMT", 0);
        feltOrder.setAttribute("RCM_PER", 0);
        feltOrder.setAttribute("RCM_AMT", 0);
        feltOrder.setAttribute("GST_COMPENSATION_CESS_PER", 0);
        feltOrder.setAttribute("GST_COMPENSATION_CESS_AMT", 0);
        feltOrder.setAttribute("PR_BILL_LENGTH", "");
        feltOrder.setAttribute("PR_BILL_WIDTH", "");
        feltOrder.setAttribute("PR_BILL_WEIGHT", "");
        feltOrder.setAttribute("PR_BILL_SQMTR", "");
        feltOrder.setAttribute("PR_BILL_GSM", "");
        feltOrder.setAttribute("PR_BILL_PRODUCT_CODE", "");
        feltOrder.setAttribute("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        feltOrder.setAttribute("CANCELED", false);
        feltOrder.setAttribute("BASE_ORDER_AMT", tbldiverted.getValueAt(21, 1).toString());
        feltOrder.setAttribute("BASE_EXISTING_PIECE_AMT", tblexist.getValueAt(21, 1).toString());
        
        feltOrder.setAttribute("COST_BY_COMPANY_PER", txtByCompany.getText());
        feltOrder.setAttribute("COST_BY_COMPANY_AMT", txtByCompanyAmt.getText());
        feltOrder.setAttribute("COST_BY_PARTY_PER", txtByParty.getText());
        feltOrder.setAttribute("COST_BY_PARTY_AMT", txtByPartyAmt.getText());
        
        feltOrder.setAttribute("TH_WEIGHT", txtThWeight.getText()+"");
        feltOrder.setAttribute("MACHINE_POSITION", txtMachinePosition.getText()+"");
        
        
        DOC_NO = S_O_NO.getText();
        feltOrder.setAttribute("DOC_NO", DOC_NO);
        feltOrder.setAttribute("DOC_DATE", S_O_DATE.getText());
        feltOrder.setAttribute("MODULE_ID", ModuleId);
        feltOrder.setAttribute("USER_ID", EITLERPGLOBAL.gNewUserID);
        feltOrder.setAttribute("REJECTED_REMARKS", txtToRemarks.getText());
        feltOrder.setAttribute("REMARKS", "");
        feltOrder.setAttribute("APPROVAL_STATUS", "");
        feltOrder.setAttribute("APPROVER_REMARKS", txtFromRemarks.getText());
        feltOrder.setAttribute("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());

//----- Update Approval Specific Fields -----------//
        feltOrder.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        feltOrder.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        feltOrder.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        feltOrder.setAttribute("FROM_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            feltOrder.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            feltOrder.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            feltOrder.setAttribute("APPROVAL_STATUS", "R");
            feltOrder.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            feltOrder.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            feltOrder.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            feltOrder.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            feltOrder.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            feltOrder.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            feltOrder.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            feltOrder.setAttribute("UPARTY_CODEPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }

    }

    public static String ltrim(String s) {
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTextField REMARK;
    private javax.swing.JTextField REMARK1;
    private javax.swing.JFormattedTextField S_O_DATE;
    private javax.swing.JTextField S_O_NO;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBackToTab0;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFromRemarksBig;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNextToTab2;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblpl;
    private javax.swing.JComboBox<String> reason;
    private javax.swing.JTable tbldiverted;
    private javax.swing.JTable tblexist;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtByCompany;
    private javax.swing.JTextField txtByCompanyAmt;
    private javax.swing.JTextField txtByParty;
    private javax.swing.JTextField txtByPartyAmt;
    private javax.swing.JTextField txtFinishingDate;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtMachinePosition;
    private javax.swing.JFormattedTextField txtPartyCode;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProfitLoss;
    private javax.swing.JLabel txtProfitLossPer;
    private javax.swing.JTextField txtThWeight;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JFormattedTextField txtgsm;
    private javax.swing.JFormattedTextField txtlength;
    private javax.swing.JTextField txtpiece;
    private javax.swing.JFormattedTextField txtwidth;
    private javax.swing.JFormattedTextField txtwtsqmtr;
    // End of variables declaration//GEN-END:variables
    private void getPieceDetail() {
        try {
            ResultSet piecers = null;
            piecers = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER P,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + txtpiece.getText() + "'");
            piecers.first();
            if (piecers.getRow() > 0) {
                DataModel = new EITLTableModel();
                tblexist.removeAll();

                tblexist.setModel(DataModel);
                tblexist.setAutoResizeMode(0);

                DataModel.addColumn("Description"); //0 - Read Only
                DataModel.addColumn("Value"); //0 - Read Only

                final TableColumnModel columnModel = tblexist.getColumnModel();
                for (int column = 0; column < tblexist.getColumnCount(); column++) {
                    int width = 100; // Min width
                    for (int row = 0; row < tblexist.getRowCount(); row++) {
                        TableCellRenderer renderer = tblexist.getCellRenderer(row, column);
                        Component comp = tblexist.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 1, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
                txtFinishingDate.setText(EITLERPGLOBAL.formatDate(piecers.getString("PR_FNSG_DATE")));
                
                DataModel.TableReadOnly(true);

                //Add Blank
                Object[] rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add Party Code
                rowData = new Object[10];
                rowData[0] = "PARTY CODE";
                rowData[1] = piecers.getInt("PR_PARTY_CODE");
                DataModel.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add Party NAME
                rowData = new Object[10];
                rowData[0] = "PARTY NAME";
                rowData[1] = piecers.getString("PARTY_NAME");
                DataModel.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add PIECE NO
                rowData = new Object[10];
                rowData[0] = "PIECE NO";
                rowData[1] = piecers.getString("PR_PIECE_NO");
                DataModel.addRow(rowData);
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                
                //Add PIECE NO
                rowData = new Object[10];
                rowData[0] = "PIECE STAGE";
                rowData[1] = piecers.getString("PR_PIECE_STAGE");
                DataModel.addRow(rowData);
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add PRODUCT CD
                rowData = new Object[10];
                rowData[0] = "PRODUCT CODE";
                rowData[1] = piecers.getString("PR_PRODUCT_CODE");
                txtProductCode.setText(piecers.getString("PR_PRODUCT_CODE"));
                DataModel.addRow(rowData);
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add LENGTH
                rowData = new Object[10];
                rowData[0] = "LENGTH";
                rowData[1] = piecers.getDouble("PR_BILL_LENGTH");
                txtlength.setText(piecers.getDouble("PR_BILL_LENGTH")+"");
                
                DataModel.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add WIDTH
                rowData = new Object[10];
                rowData[0] = "WIDTH";
                rowData[1] = piecers.getDouble("PR_BILL_WIDTH");
                DataModel.addRow(rowData);
                txtwidth.setText(piecers.getDouble("PR_BILL_WIDTH")+"");
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add GSM
                rowData = new Object[10];
                rowData[0] = "GSM";
                rowData[1] = piecers.getDouble("PR_BILL_GSM");
                DataModel.addRow(rowData);
                txtgsm.setText(piecers.getDouble("PR_BILL_GSM")+"");
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add WT/SQMTR
                double wt = 0;
                rowData = new Object[10];
                rowData[0] = "WT/SQMTR [M]";
                float Weight = 0;
                float Weight_forcalc = 0;
                float Sqmtr_forcalc = 0;
                if (piecers.getString("PR_BILL_PRODUCT_CODE").startsWith("7")) {
                    wt = piecers.getDouble("PR_BILL_SQMTR");
                    Weight = Float.parseFloat(EITLERPGLOBAL.round(wt, 2)+"");
                    
                    try{
                        Sqmtr_forcalc = (float) wt;
                    }catch(Exception e)
                    {  e.printStackTrace(); }
                    try{
                        Weight_forcalc = (float) piecers.getDouble("PR_BILL_WEIGHT");
                    }catch(Exception e)
                    {   e.printStackTrace(); }
                    
                } else {
                    wt = piecers.getDouble("PR_BILL_WEIGHT");
                    
                    String PieceStage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+piecers.getString("PR_PIECE_NO")+"'");
                    
                            if("FINISHING".equals(PieceStage)) {
                                String PR_NEEDLING_WEIGHT = data.getStringValueFromDB("SELECT PR_NEEDLING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+piecers.getString("PR_PIECE_NO")+"'");

                                wt = Float.parseFloat(PR_NEEDLING_WEIGHT);
                                //calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
                                txtwtsqmtr.setText(""+wt);
                            }
                            else if("SEAMING".equals(PieceStage) || "SPLICING".equals(PieceStage) || "MENDING".equals(PieceStage) || "NEEDLING".equals(PieceStage)) {
                                String PR_WEAVING_WEIGHT = data.getStringValueFromDB("SELECT PR_WEAVING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+piecers.getString("PR_PIECE_NO")+"'");

                                wt = Float.parseFloat(PR_WEAVING_WEIGHT);
                                //calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
                                txtwtsqmtr.setText(""+wt);
                            }
                    Weight = Float.parseFloat(EITLERPGLOBAL.round(wt, 1)+"");
                    
                    
                    try{
                        Sqmtr_forcalc = (float)  piecers.getDouble("PR_BILL_SQMTR");
                    }catch(Exception e)
                    {  e.printStackTrace(); }
                    try{
                        Weight_forcalc = Weight;
                    }catch(Exception e)
                    {   e.printStackTrace(); }
                    
                }
//                float Weight = 0;
//                
//                if (piecers.getString("PR_BILL_PRODUCT_CODE").startsWith("7")) {
//                    wt = piecers.getDouble("PR_BILL_SQMTR");
//                    Weight = Float.parseFloat(EITLERPGLOBAL.round(wt, 2)+"");
//                } else {
//                    wt = piecers.getDouble("PR_BILL_WEIGHT");
//                    
//                    String PieceStage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+piecers.getString("PR_PIECE_NO")+"'");
//                
//                    if("FINISHING".equals(PieceStage)) {
//                        String PR_NEEDLING_WEIGHT = data.getStringValueFromDB("SELECT PR_NEEDLING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+piecers.getString("PR_PIECE_NO")+"'");
//
//                        wt = Float.parseFloat(PR_NEEDLING_WEIGHT);
//                        //calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
//                        txtwtsqmtr.setText(""+wt);
//                    }
//                    else if("SEAMING".equals(PieceStage) || "SPLICING".equals(PieceStage) || "MENDING".equals(PieceStage) || "NEEDLING".equals(PieceStage)) {
//                        String PR_WEAVING_WEIGHT = data.getStringValueFromDB("SELECT PR_WEAVING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+piecers.getString("PR_PIECE_NO")+"'");
//
//                        wt = Float.parseFloat(PR_WEAVING_WEIGHT);
//                        //calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
//                        txtwtsqmtr.setText(""+wt);
//                    }
//                    Weight = Float.parseFloat(EITLERPGLOBAL.round(wt, 1)+"");
//                }
                
                txtwtsqmtr.setText(Weight+"");
                rowData[1] = Weight;
                DataModel.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                //Add SELLING PRICE
                FeltInvCalc inv_calc = new FeltInvCalc();
                inv_calc = clsOrderValueCalc.calculate_ForPriorApproval_GetExist(piecers.getString("PR_PIECE_NO"), piecers.getString("PR_BILL_PRODUCT_CODE"), piecers.getString("PR_PARTY_CODE"), piecers.getFloat("PR_BILL_LENGTH"), piecers.getFloat("PR_BILL_WIDTH"), Weight, Weight, EITLERPGLOBAL.getCurrentDateDB());
                rowData = new Object[10];
                rowData[0] = "SELLING PRICE (Kgs/Sq [M])";
                rowData[1] = EITLERPGLOBAL.round(inv_calc.getFicRate() ,2);
                DataModel.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel.addRow(rowData);
                
                float seam_chrg = inv_calc.getFicSeamChg();
                if("SEAMING".equals(piecers.getString("PR_PIECE_STAGE")))
                {
                    seam_chrg = 0;
                }
                
                //Add Basic Value
                rowData = new Object[10];
                rowData[0] = "BASIC VALUE [RS.]";
                rowData[1] = inv_calc.getFicBasAmount();
                DataModel.addRow(rowData);
                //Add Seam Charge
                rowData = new Object[10];
                rowData[0] = "SEAM CHARGE [If Any] [RS.]";
                rowData[1] = inv_calc.getFicSeamChg();
                DataModel.addRow(rowData);
                //Add Chemical Treatement
                rowData = new Object[10];
                rowData[0] = "CHEMICAL TRATMENT [RS.]";
                rowData[1] = inv_calc.getFicChemTrtChg();
                DataModel.addRow(rowData);
                //Add Total
                rowData = new Object[10];
                rowData[0] = "TOTAL [RS.]";
                rowData[1] = EITLERPGLOBAL.round(inv_calc.getFicBasAmount() + seam_chrg + inv_calc.getFicChemTrtChg(),0);
                DataModel.addRow(rowData);
                
                
                if(piecers.getString("PR_BILL_PRODUCT_CODE").equals("729000"))
                {
                        //ADD BLANK
                        rowData = new Object[10];
                        rowData[0] = "";
                        DataModel.addRow(rowData);

                        rowData = new Object[10];
                        rowData[0] = "DISCOUNT PER";
                        rowData[1] = inv_calc.getFicDiscPer();
                        DataModel.addRow(rowData);

                        rowData = new Object[10];
                        rowData[0] = "DISCOUNT AMT [Rs.]";
                        rowData[1] = inv_calc.getFicDiscAmt();
                        DataModel.addRow(rowData);

                        Exist_Invoice_Amt = EITLERPGLOBAL.round(((inv_calc.getFicBasAmount() + seam_chrg + inv_calc.getFicChemTrtChg())-inv_calc.getFicDiscAmt()),0);

                        //ADD BLANK
                        rowData = new Object[10];
                        rowData[0] = "";
                        DataModel.addRow(rowData);

                        rowData = new Object[10];
                        rowData[0] = "NET AMT [Rs.]";
                        rowData[1] = Exist_Invoice_Amt;
                        DataModel.addRow(rowData);
                }
                else
                {
                        Exist_Invoice_Amt = EITLERPGLOBAL.round((inv_calc.getFicBasAmount() + seam_chrg + inv_calc.getFicChemTrtChg()),0);
                }
                
                rowData = new Object[10];
                rowData[0] = "";
                rowData[1] = "";
                DataModel.addRow(rowData);
                
                rowData = new Object[10];
                rowData[0] = "SURCHARGE PER";
                rowData[1] = inv_calc.getFicSurcharge_per();
                DataModel.addRow(rowData);
                
                rowData = new Object[10];
                rowData[0] = "SURCHARGE RATE";
                rowData[1] = inv_calc.getFicSurcharge_rate();
                DataModel.addRow(rowData);
                
                rowData = new Object[10];
                rowData[0] = "GROSS RATE";
                rowData[1] = inv_calc.getFicGrossRate();
                DataModel.addRow(rowData);
                        
                        
                
            } else {
                //JOptionPane.showMessageDialog(null, "Invalid Piece....");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FindProfitLoss()
    {
        try {
            String Orginal_Piece;
           // if (piecers.getRow() > 0) {
                DataModel_NewPiece = new EITLTableModel();
                tbldiverted.removeAll();

                tbldiverted.setModel(DataModel_NewPiece);
                tbldiverted.setAutoResizeMode(0);

                DataModel_NewPiece.addColumn("Description"); //0 - Read Only
                DataModel_NewPiece.addColumn("Value"); //0 - Read Only

                final TableColumnModel columnModel = tbldiverted.getColumnModel();
                for (int column = 0; column < tbldiverted.getColumnCount(); column++) {
                    int width = 100; // Min width
                    for (int row = 0; row < tbldiverted.getRowCount(); row++) {
                        TableCellRenderer renderer = tbldiverted.getCellRenderer(row, column);
                        Component comp = tbldiverted.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 1, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
                DataModel_NewPiece.TableReadOnly(true);

                //Add Blank
                Object[] rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                //Add Party Code
                rowData = new Object[10];
                rowData[0] = "PARTY CODE";
                rowData[1] = txtPartyCode.getText();
                DataModel_NewPiece.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                //Add Party NAME
                rowData = new Object[10];
                rowData[0] = "PARTY NAME";
                rowData[1] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText());
                DataModel_NewPiece.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                //Add PIECE NO
                rowData = new Object[10];
                rowData[0] = "PIECE NO";
                Orginal_Piece = txtpiece.getText();
               
                String PieceStage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+txtpiece.getText()+"'");
                String Piece_NO="";
                if(PieceStage.equals("IN STOCK"))
                {
                    rowData[1] = txtpiece.getText().trim()+"V";
                    DataModel_NewPiece.addRow(rowData);
                    Piece_NO = rowData[1].toString(); 
                }else if(PieceStage.equals("SEAMING") || PieceStage.equals("SPLICING ") || PieceStage.equals("FINISHING") || PieceStage.equals("WEAVING") || PieceStage.equals("MENDING") || PieceStage.equals("NEEDLING") || PieceStage.equals("WARPING"))
                {
                    
                    rowData[1] = txtpiece.getText().trim()+"P";
                    DataModel_NewPiece.addRow(rowData);
                    Piece_NO = rowData[1].toString();
                    
                }
                   
                
                
                //ADD BLANK 
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                
                //Add PIECE NO
                rowData = new Object[10];
                rowData[0] = "PIECE STAGE";
                rowData[1] = PieceStage;
                DataModel_NewPiece.addRow(rowData);
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                
                //Add PRODUCT CD
                rowData = new Object[10];
                rowData[0] = "PRODUCT CODE";
                rowData[1] = txtProductCode.getText();
                DataModel_NewPiece.addRow(rowData);
                
                float cal_wt = 0;
                float cal_sqmtr = 0;
                if (txtProductCode.getText().startsWith("7")) {
                    try{
                        float length_0 = Float.parseFloat(txtlength.getText());
                        float width_0 = Float.parseFloat(txtwidth.getText());
                        
                        double sqmtr_0 = EITLERPGLOBAL.round((length_0 * width_0),2);
                        txtwtsqmtr.setText(sqmtr_0+"");
                        
                        float cal_gsm= Float.parseFloat(txtgsm.getText());
                        
                        cal_wt = (length_0 * width_0 * cal_gsm) / 1000;
                        cal_sqmtr = (float) sqmtr_0;
                        
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    float length_0 = Float.parseFloat(txtlength.getText());
                    float width_0 = Float.parseFloat(txtwidth.getText());
                        
                    double sqmtr_0 = EITLERPGLOBAL.round((length_0 * width_0),2);
                    
                    cal_wt = Float.parseFloat(txtwtsqmtr.getText());
                    cal_sqmtr = (float) sqmtr_0;
                }
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                //Add LENGTH
                rowData = new Object[10];
                rowData[0] = "LENGTH";
                rowData[1] = txtlength.getText();
                txtlength.setText(txtlength.getText()+"");
                
                DataModel_NewPiece.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                //Add WIDTH
                rowData = new Object[10];
                rowData[0] = "WIDTH";
                rowData[1] = txtwidth.getText();
                DataModel_NewPiece.addRow(rowData);
                txtwidth.setText(txtwidth.getText()+"");
                
                
//                try{
//                    String PR_NEEDLING_WEIGHT = data.getStringValueFromDB("SELECT PR_NEEDLING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+Orginal_Piece+"'");
//                    String PR_WEAVING_WEIGHT = data.getStringValueFromDB("SELECT PR_WEAVING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+Orginal_Piece+"'");
//                    if("FINISHING".equals(PieceStage)) {
//                        float LENGTH = Float.parseFloat(txtlength.getText());
//                        float WIDTH = Float.parseFloat(txtwidth.getText());
//                        float Weight = Float.parseFloat(PR_NEEDLING_WEIGHT);
//                        float calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
//                        txtgsm.setText(calculated_GSM+"");
//                    }
//                    else if("MENDING".equals(PieceStage) || "NEEDLING".equals(PieceStage)) {
//                        float LENGTH = Float.parseFloat(txtlength.getText());
//                        float WIDTH = Float.parseFloat(txtwidth.getText());
//                        float Weight = Float.parseFloat(PR_WEAVING_WEIGHT);
//                        float calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
//                        txtgsm.setText(calculated_GSM+"");
//                    }
//                    else
//                    {
//                        JOptionPane.showConfirmDialog(null, "Piece Stage is not valid for Diversion");
//                    }
//              }catch(Exception e)
//              {
//                  e.printStackTrace();
//              }
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                //Add GSM
                rowData = new Object[10];
                rowData[0] = "GSM";
                rowData[1] = txtgsm.getText();
                DataModel_NewPiece.addRow(rowData);
                txtgsm.setText(txtgsm.getText()+"");
                
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                //Add WT/SQMTR
                double wt = 0;
                rowData = new Object[10];
                rowData[0] = "WT/SQMTR [M]";
                txtwtsqmtr.setText(txtwtsqmtr.getText()+"");
                rowData[1] = txtwtsqmtr.getText();
                DataModel_NewPiece.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                
                float Th_Weight = 0;
                try{
                    Th_Weight = Float.parseFloat(txtThWeight.getText());                    
                }catch(Exception e)
                {
                    //e.printStackTrace();
                }
                
                //Add SELLING PRICE
                FeltInvCalc inv_calc = new FeltInvCalc();
                if(Th_Weight==0)
                {
                    inv_calc = clsOrderValueCalc.calculate("", txtProductCode.getText(), txtPartyCode.getText(), Float.parseFloat(txtlength.getText()), Float.parseFloat(txtwidth.getText()), cal_wt, cal_sqmtr, EITLERPGLOBAL.getCurrentDateDB());
                }
                else
                {
                    inv_calc = clsOrderValueCalc.calculate_forDiversion(Th_Weight,Piece_NO, txtProductCode.getText(), txtPartyCode.getText(), Float.parseFloat(txtlength.getText()), Float.parseFloat(txtwidth.getText()), Float.parseFloat(txtwtsqmtr.getText()), Float.parseFloat(txtwtsqmtr.getText()), EITLERPGLOBAL.getCurrentDateDB());
                }
                
                if(!inv_calc.getReason().equals(""))
                {
                    JOptionPane.showMessageDialog(this, inv_calc.getReason());
                }
                
                float SEAM_CHARGE = 0;
                
                try{
                if (inv_calc.getFicProductCode().equals("719011") || inv_calc.getFicProductCode().equals("7190110") ||
                    inv_calc.getFicProductCode().equals("719021") || inv_calc.getFicProductCode().equals("7190210") ||
                    inv_calc.getFicProductCode().equals("719031") || inv_calc.getFicProductCode().equals("7190310") ||
                    inv_calc.getFicProductCode().equals("719041") || inv_calc.getFicProductCode().equals("7190410") ||
                    inv_calc.getFicProductCode().equals("719051") || inv_calc.getFicProductCode().equals("7190510"))
                {

                            double seam_width =  inv_calc.getFicWidth() - inv_calc_existing.getFicWidth();

                            SEAM_CHARGE = (float)seam_width * 4899;

                        }
                        else
                        {
                            DataModel.setValueByVariable("OV_SEAM_CHG", "0", 0);
                        }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                
                float seam_chrg = SEAM_CHARGE;
                if(PieceStage.equals("SEAMING"))
                {
                    seam_chrg = 0;
                }
                
                rowData = new Object[10];
                rowData[0] = "SELLING PRICE (Kgs/Sq [M])";
                rowData[1] = EITLERPGLOBAL.round(inv_calc.getFicRate() ,2);
                DataModel_NewPiece.addRow(rowData);
                //ADD BLANK
                rowData = new Object[10];
                rowData[0] = "";
                DataModel_NewPiece.addRow(rowData);
                //Add Basic Value
                rowData = new Object[10];
                rowData[0] = "BASIC VALUE [RS.]";
                rowData[1] = inv_calc.getFicBasAmount();
                DataModel_NewPiece.addRow(rowData);
                //Add Seam Charge
                rowData = new Object[10];
                rowData[0] = "SEAM CHARGE [If Any] [RS.]";
                rowData[1] = seam_chrg;
                DataModel_NewPiece.addRow(rowData);
                //Add Chemical Treatement
                rowData = new Object[10];
                rowData[0] = "CHEMICAL TRATMENT [RS.]";
                rowData[1] = inv_calc.getFicChemTrtChg();
                DataModel_NewPiece.addRow(rowData);
                //Add Total
                rowData = new Object[10];
                rowData[0] = "TOTAL [RS.]";
                rowData[1] = EITLERPGLOBAL.round(inv_calc.getFicBasAmount() + seam_chrg + inv_calc.getFicChemTrtChg(),2);
                DataModel_NewPiece.addRow(rowData);
                
                
                if(txtProductCode.getText().equals("729000"))
                {
                        //ADD BLANK
                       rowData = new Object[10];
                       rowData[0] = "";
                       DataModel_NewPiece.addRow(rowData);

                       rowData = new Object[10];
                       rowData[0] = "DISCOUNT PER";
                       rowData[1] = inv_calc.getFicDiscPer();
                       DataModel_NewPiece.addRow(rowData);

                       rowData = new Object[10];
                       rowData[0] = "DISCOUNT AMT [Rs.]";
                       rowData[1] = inv_calc.getFicDiscAmt();
                       DataModel_NewPiece.addRow(rowData);

                       New_Invoice_Amt = EITLERPGLOBAL.round(((inv_calc.getFicBasAmount() + seam_chrg + inv_calc.getFicChemTrtChg())-inv_calc.getFicDiscAmt()),0);

                       //ADD BLANK
                       rowData = new Object[10];
                       rowData[0] = "";
                       DataModel_NewPiece.addRow(rowData);

                       rowData = new Object[10];
                       rowData[0] = "NET AMT [Rs.]";
                       rowData[1] = New_Invoice_Amt;
                       DataModel_NewPiece.addRow(rowData);

                       
                       
                       double profit_loss = Exist_Invoice_Amt - EITLERPGLOBAL.round(((inv_calc.getFicBasAmount() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg())-inv_calc.getFicDiscAmt()),0); 
                       profit_loss = (profit_loss*-1);
                       txtProfitLoss.setText(EITLERPGLOBAL.round(profit_loss,2)+"");

                       double PLper = (profit_loss*100) / Exist_Invoice_Amt;

                       txtProfitLossPer.setText(EITLERPGLOBAL.round(PLper,2)+"");
                }
                else
                {
                       double profit_loss = Exist_Invoice_Amt - EITLERPGLOBAL.round((inv_calc.getFicBasAmount() + seam_chrg + inv_calc.getFicChemTrtChg()),0); 
                       profit_loss = (profit_loss*-1);
                       txtProfitLoss.setText(EITLERPGLOBAL.round(profit_loss,0)+"");

                       double PLper = (profit_loss*100) / Exist_Invoice_Amt;

                       txtProfitLossPer.setText(EITLERPGLOBAL.round(PLper,0)+"");
                }
                
                rowData = new Object[10];
                rowData[0] = "";
                rowData[1] = "";
                DataModel_NewPiece.addRow(rowData);
                
                rowData = new Object[10];
                rowData[0] = "SURCHARGE PER";
                rowData[1] = inv_calc.getFicSurcharge_per();
                DataModel_NewPiece.addRow(rowData);
                
                rowData = new Object[10];
                rowData[0] = "SURCHARGE RATE";
                rowData[1] = inv_calc.getFicSurcharge_rate();
                DataModel_NewPiece.addRow(rowData);
                
                rowData = new Object[10];
                rowData[0] = "GROSS RATE";
                rowData[1] = inv_calc.getFicGrossRate();
                DataModel_NewPiece.addRow(rowData);
                
                
//            } else {
//                JOptionPane.showMessageDialog(null, "Invalid Piece....");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
