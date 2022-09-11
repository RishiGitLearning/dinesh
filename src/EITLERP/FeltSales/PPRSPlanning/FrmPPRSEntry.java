/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PPRSPlanning;

import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.common.JavaMail;
import static EITLERP.FeltSales.common.JavaMail.SendMail;
import EITLERP.Loader;
import EITLERP.Production.FeltCreditNote.clsExcelExporter;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.SelectFirstFree;
import EITLERP.clsAuthority;
import EITLERP.clsDepartment;
import EITLERP.clsDocFlow;
import EITLERP.clsFirstFree;
import EITLERP.clsHierarchy;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import EITLERP.frmPendingApprovals;
import TReportWriter.TReportEngine;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FrmPPRSEntry extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableModel DataModel_Exist;
    public int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private int FFNo = 0;
    private final int ModuleId = 878;
    private String DOC_NO = "";
    private clsPPRSEntry ObjPieceOC;
    private EITLComboModel cmbSendToModel;
    public HashMap objFromMainFormPPRS = new HashMap();
    String seleval = "", seltyp = "", selqlt = "", selshd = "", selpiece = "", selext = "", selinv = "", selsz = "";
    private int mlstrc;
    private int size = 0;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateForDB = new SimpleDateFormat("yyyy-MM-dd");
    public String PPRS_MONTH = "";
    public String FELT_TYPE = "";
//    DecimalFormat f_single = new DecimalFormat("##.0");
//    DecimalFormat f_double = new DecimalFormat("##.00");
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    public frmPendingApprovals frmPA;
    EITLTableCellRenderer Renderer1 = new EITLTableCellRenderer();
    EITLTableCellRenderer Renderer2 = new EITLTableCellRenderer();
    private String LOGIN_USER_TYPE = "";
    private clsExcelExporter exp = new clsExcelExporter();
    private EITLTableModel DataModel_CapacityPlanning;
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();

    /**
     * Initializes the applet FrmObjPieceOC
     */
    @Override
    public void init() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
        initComponents();
        GenerateCombos();

        int USER_ID = EITLERPGLOBAL.gUserID;
        int DEPT_ID = EITLERPGLOBAL.gUserDeptID;
        System.out.println("USER = " + USER_ID + " DEPT ID = " + DEPT_ID);
        if (DEPT_ID == 29) {
            //SALES LOGIN
            LOGIN_USER_TYPE = "SALES";
        } else if (DEPT_ID == 40) {
            LOGIN_USER_TYPE = "PPC";
        } else if (DEPT_ID == 39) {
            LOGIN_USER_TYPE = "DESIGN";
        } else {
            LOGIN_USER_TYPE = "OTHER";
        }

        FormatGrid();

        GenerateFromCombo();
        GenerateHierarchyCombo();
        SetupApproval();
        SetMenuForRights();
        DefaultSettings();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
        //Cancel();
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(OCDDate);

        } catch (ParseException ex) {
            System.out.println("Error on Mask : " + ex.getLocalizedMessage());
        }

        OCDDate.setText(df.format(new Date()));

        ObjPieceOC = new clsPPRSEntry();
        boolean load = ObjPieceOC.LoadData();

        if (load) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + ObjPieceOC.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        SetFields(false);
        //FillPiece();

    }

    public void FillPiece() {
        Add();
        //objFromMainForm = Obj;
        for (int i = 0; i < objFromMainFormPPRS.size(); i++) {
            clsPPRSEntryDetails ObjHistory = (clsPPRSEntryDetails) objFromMainFormPPRS.get(i);

            String PIECE_NO = ObjHistory.getAttribute("PIECE_NO").getString();
            String PPRS_MONTH = ObjHistory.getAttribute("PPRS_MONTH").getString();

            Object[] rowData = new Object[15];
            rowData[0] = "" + i;
            rowData[0] = "";
            DataModel.addRow(rowData);
            DataModel.setValueByVariable("SRNO", (i + 1) + "", i);
            DataModel.setValueByVariable("CONF_PPRS_MONTH", PPRS_MONTH, i);
            DataModel.setValueByVariable("PR_PIECE_NO", PIECE_NO, i);

            try {
                ResultSet rsPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                rsPiece.first();

                DataModel.setValueByVariable("PR_PARTY_CODE", rsPiece.getString("PR_PARTY_CODE"), i);
                DataModel.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsPiece.getString("PR_PARTY_CODE")), i);
                DataModel.setValueByVariable("PR_MACHINE_NO", rsPiece.getString("PR_MACHINE_NO"), i);

                String Pos_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + rsPiece.getString("PR_POSITION_NO"));
                String Pos_Desc = data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + rsPiece.getString("PR_POSITION_NO"));
                DataModel.setValueByVariable("POSITION_DESC", Pos_Desc, i);
                DataModel.setValueByVariable("POSITION_DESIGN_NO", Pos_Des_No, i);
                DataModel.setValueByVariable("UPN_NO", rsPiece.getString("PR_UPN"), i);
                DataModel.setValueByVariable("PR_PRODUCT_CODE", rsPiece.getString("PR_PRODUCT_CODE"), i);
                DataModel.setValueByVariable("PR_GROUP", rsPiece.getString("PR_GROUP"), i);

                DataModel.setValueByVariable("PR_PIECE_STAGE", rsPiece.getString("PR_PIECE_STAGE"), i);
                DataModel.setValueByVariable("PR_REQUESTED_MONTH", rsPiece.getString("PR_REQUESTED_MONTH"), i);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setPiece(String PIECE_NO, String PPRS_MONTH) {
        clsPPRSEntry objDetail = new clsPPRSEntry();
        objDetail.setAttribute("PIECE_NO", PIECE_NO);
        objDetail.setAttribute("PPRS_MONTH", PPRS_MONTH);
        objFromMainFormPPRS.put(size++, objDetail);
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    public void DefaultSettings() {

        //String data = toString();
        Object[] rowData = new Object[15];
        rowData[0] = "1";
        DataModel.addRow(rowData);

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
        OCDNo.setText("0");
        txtOC_MONTH.setText("");
        txtRemark.setText("");
        txtFeltType.setText("");
        //JOptionPane.showMessageDialog(null, "Data Model size : "+DataModel.getRowCount());
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
        FormatGrid();

        FormatGridHS();

        for (int i = 0; i < DataModel.getRowCount(); i++) {
            DataModel.removeRow(i);
        }
        if (DataModel.getRowCount() > 0) {
            DataModel.removeRow(0);
        }
        Object[] rowData = new Object[15];
        rowData[0] = 1;
        DataModel.addRow(rowData);
    }

    private void DisplayData() {

        //=========== Color Indication ===============//
        try {

            if (ObjPieceOC.getAttribute("APPROVED").getInt() == 1) {
                lblTitle.setBackground(Color.BLUE);
                lblTitle.setForeground(Color.WHITE);
            }

            if (ObjPieceOC.getAttribute("APPROVED").getInt() == 0) {
                lblTitle.setBackground(Color.GRAY);
                lblTitle.setForeground(Color.BLACK);
            }

            if (ObjPieceOC.getAttribute("CANCELED").getInt() == 1) {
                lblTitle.setBackground(Color.RED);
                lblTitle.setForeground(Color.BLACK);
            }
        } catch (Exception c) {

            // c.printStackTrace();
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

        //clsObjPieceOC.ChangeVoucherNo=false;
        clearFields();
        try {
            OCDNo.setText(ObjPieceOC.getAttribute("PPRS_DOC_NO").getString());
            lblTitle.setText("Felt PPRS Planning - " + ObjPieceOC.getAttribute("PPRS_DOC_NO").getString());
            OCDDate.setText(EITLERPGLOBAL.formatDate(ObjPieceOC.getAttribute("PPRS_DOC_DATE").getString()));
            lblRevNo.setText(Integer.toString((int) ObjPieceOC.getAttribute("REVISION_NO").getVal()));

            txtRemark.setText(ObjPieceOC.getAttribute("PPRS_REMARK").getString());
            txtOC_MONTH.setText(ObjPieceOC.getAttribute("PPRS_MONTH").getString());
            txtFeltType.setText(ObjPieceOC.getAttribute("FELT_TYPE").getString());
            PPRS_MONTH = ObjPieceOC.getAttribute("PPRS_MONTH").getString();
            FELT_TYPE = ObjPieceOC.getAttribute("FELT_TYPE").getString();

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjPieceOC.getAttribute("HIERARCHY_ID").getInt());

            FormatGrid();
            double total_length = 0;
            double total_width = 0;
            //Now Generate Table
            for (int i = 1; i <= ObjPieceOC.hmFeltPieceIOCDetails.size(); i++) {
                clsPPRSEntryDetails ObjItem = (clsPPRSEntryDetails) ObjPieceOC.hmFeltPieceIOCDetails.get(Integer.toString(i));

                Object[] rowData = new Object[1];
                DataModel.addRow(rowData);
                int NewRow = Table.getRowCount() - 1;
                DataModel.setValueByVariable("SrNo", (NewRow + 1) + "", NewRow);

                //DataModel.setValueByVariable("SELECT_SALES", ObjItem.getAttribute("SELECT_SALES").getBool() NewRow);
                //DataModel.setValueByVariable("SELECT_PPC", ObjItem.getAttribute("SELECT_PPC").getString(), NewRow);
                DataModel.setValueByVariable("PR_PIECE_NO", ObjItem.getAttribute("PIECE_NO").getString(), NewRow);
                DataModel.setValueByVariable("CONF_PPRS_MONTH", ObjItem.getAttribute("PPRS_MONTH").getString(), NewRow);
                DataModel.setValueByVariable("PR_PIECE_STAGE", ObjItem.getAttribute("PIECE_STAGE").getString(), NewRow);
                DataModel.setValueByVariable("PR_REQUESTED_MONTH", ObjItem.getAttribute("REQ_MONTH").getString(), NewRow);
                DataModel.setValueByVariable("PPRS_REMARK", ObjItem.getAttribute("REMARK").getString(), NewRow);

                try {
                    ResultSet rsPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + ObjItem.getAttribute("PIECE_NO").getString() + "'");
                    rsPiece.first();

                    DataModel.setValueByVariable("PR_PARTY_CODE", rsPiece.getString("PR_PARTY_CODE"), NewRow);
                    DataModel.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsPiece.getString("PR_PARTY_CODE")), NewRow);
                    DataModel.setValueByVariable("PR_MACHINE_NO", rsPiece.getString("PR_MACHINE_NO"), NewRow);
                    //DataModel.setValueByVariable("PR_POSITION_NO", rsPiece.getString("PR_POSITION_NO"), NewRow);
                    String Pos_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + rsPiece.getString("PR_POSITION_NO"));
                    String Pos_Desc = data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + rsPiece.getString("PR_POSITION_NO"));
                    DataModel.setValueByVariable("POSITION_DESC", Pos_Desc, NewRow);
                    DataModel.setValueByVariable("POSITION_DESIGN_NO", Pos_Des_No, NewRow);
                    DataModel.setValueByVariable("UPN_NO", rsPiece.getString("PR_UPN"), NewRow);
                    DataModel.setValueByVariable("PR_PRODUCT_CODE", rsPiece.getString("PR_PRODUCT_CODE"), NewRow);
                    DataModel.setValueByVariable("PR_GROUP", rsPiece.getString("PR_GROUP"), NewRow);

                    DataModel.setValueByVariable("ADD_PIECE_BY", ObjItem.getAttribute("ADD_PIECE_BY").getString(), NewRow);
                    DataModel.setValueByVariable("ADD_PIECE_DATE", ObjItem.getAttribute("ADD_PIECE_DATE").getString(), NewRow);
                    String USER = "";
                    try {
                        USER = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(ObjItem.getAttribute("ADD_PIECE_BY").getString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DataModel.setValueByVariable("ADD_BY", USER, NewRow);
                    //System.out.println("SELECT CONTACT_PERSON,EMAIL,PHONE_NO,EMAIL_ID2,EMAIL_ID3 FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+rsPiece.getString("PR_PARTY_CODE")+"'");

                    //EMAIL_ID2
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
            //DoNotEvaluate=false;
            //UpdateTotals();
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = ObjPieceOC.getAttribute("PPRS_DOC_NO").getString();
            List = clsFeltProductionApprovalFlow.getDocumentFlow(ModuleId, DocNo);
            for (int i = 1; i <= List.size(); i++) {
                clsDocFlow ObjFlow = (clsDocFlow) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];
                //JOptionPane.showMessageDialog(null, "USER ID : "+ObjFlow.getAttribute("USER_ID").getVal());
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
            HashMap History = ObjPieceOC.getHistoryList(EITLERPGLOBAL.gCompanyID + "", DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsPPRSEntry ObjHistory = (clsPPRSEntry) History.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(ObjHistory.getAttribute("MODIFIED_BY").getString()));
                rowData[2] = ObjHistory.getAttribute("MODIFIED_DATE").getString();

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
                rowData[4] = ObjHistory.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjHistory.getAttribute("FROM_IP").getString();

                DataModelUpdateHistory.addRow(rowData);
            }

            //============================================================//
            //setSTATUS();
            //GenerateProdMfgReport();
        } catch (Exception e) {
            e.printStackTrace();
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6251, 62511)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6251, 62512)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6251, 62513)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6251, 62515)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            //cmdPreview.setEnabled(false);
            //cmdPrint.setEnabled(false);
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
        //JOptionPane.showMessageDialog(null, "Hierarchy Id = "+SelHierarchyID);
        //GenerateFromCombo();
        //GenerateSendToCombo();

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
        }

        if (EditMode == 0) {
            cmbHierarchy.setEnabled(false);
            txtFrom.setEnabled(false);
            //txtFromRemarks.setEnabled(false);
            OpgApprove.setEnabled(false);
            OpgFinal.setEnabled(false);
            OpgReject.setEnabled(false);
            cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }

        if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
            OpgReject.setEnabled(false);
        }
        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
            //JOptionPane.showMessageDialog(null, "Final Approver");
            OpgApprove.setEnabled(false);
        }
    }

    private void FormatGrid() {
        try {
            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            Table.setAutoResizeMode(0);

            DataModel.addColumn("SrNo"); //0 - Read Only
            DataModel.addColumn("PIECE NO"); //1
            DataModel.addColumn("PARTY CODE"); //5
            DataModel.addColumn("PARTY NAME"); //6
            DataModel.addColumn("MAC NO"); //2
            DataModel.addColumn("POS DESC"); //4
            DataModel.addColumn("POS DESIGN NO"); //12
            DataModel.addColumn("UPN"); //11
            DataModel.addColumn("REQ MTH"); //11
            DataModel.addColumn("PPRS MONTH"); //0 - Read Only
            DataModel.addColumn("PRODUCT CODE"); //7
            DataModel.addColumn("GROUP"); //8
            DataModel.addColumn("PIECE STAGE"); //9
            DataModel.addColumn("PPRS REMARK"); //1
            DataModel.addColumn("ADD BY"); //1
            DataModel.addColumn("DATE"); //1
            DataModel.addColumn("ADD BY"); //1

            DataModel.SetVariable(0, "SrNo"); //0 - Read Only            
            DataModel.SetVariable(1, "PR_PIECE_NO"); //1
            DataModel.SetVariable(2, "PR_PARTY_CODE"); //1
            DataModel.SetVariable(3, "PARTY_NAME"); //1
            DataModel.SetVariable(4, "PR_MACHINE_NO"); //1
            DataModel.SetVariable(5, "POSITION_DESC"); //1
            DataModel.SetVariable(6, "POSITION_DESIGN_NO"); //2
            DataModel.SetVariable(7, "UPN_NO"); //1
            DataModel.SetVariable(8, "PR_REQUESTED_MONTH"); //1
            DataModel.SetVariable(9, "CONF_PPRS_MONTH"); //0 - Read Only
            DataModel.SetVariable(10, "PR_PRODUCT_CODE"); //1
            DataModel.SetVariable(11, "PR_GROUP"); //1
            DataModel.SetVariable(12, "PR_PIECE_STAGE"); //1
            DataModel.SetVariable(13, "PPRS_REMARK"); //1
            DataModel.SetVariable(14, "ADD_PIECE_BY"); //1
            DataModel.SetVariable(15, "ADD_PIECE_DATE"); //1
            DataModel.SetVariable(16, "ADD_BY"); //1

            Table.getColumnModel().getColumn(0).setMaxWidth(40);
            Table.getColumnModel().getColumn(1).setMinWidth(100);
            Table.getColumnModel().getColumn(2).setMinWidth(100);
            Table.getColumnModel().getColumn(3).setMinWidth(130);
            Table.getColumnModel().getColumn(4).setMinWidth(130);
            Table.getColumnModel().getColumn(5).setMinWidth(100);
            Table.getColumnModel().getColumn(6).setMinWidth(100);
            Table.getColumnModel().getColumn(7).setMinWidth(100);
            Table.getColumnModel().getColumn(8).setMinWidth(100);
            Table.getColumnModel().getColumn(9).setMinWidth(130);
            Table.getColumnModel().getColumn(10).setMinWidth(100);
            Table.getColumnModel().getColumn(10).setMaxWidth(100);
            Table.getColumnModel().getColumn(11).setMinWidth(100);
            Table.getColumnModel().getColumn(12).setMinWidth(120);
            Table.getColumnModel().getColumn(13).setMinWidth(120);
            Table.getColumnModel().getColumn(14).setMinWidth(0);
            Table.getColumnModel().getColumn(14).setMaxWidth(0);
            Table.getColumnModel().getColumn(15).setMinWidth(120);
            Table.getColumnModel().getColumn(16).setMinWidth(140);

            for (int i = 0; i < DataModel.getColumnCount(); i++) {
//                DataModel.SetReadOnly(i);
                if (i != 13) {
                    DataModel.SetReadOnly(i);
                }

                if (!LOGIN_USER_TYPE.equals("DESIGN")) {
                    DataModel.SetReadOnly(13);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        DataModelUpdateHistory.addColumn("FROM_IP");

        TableColumnModel tcm = TableUpdateHistory.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(20);
        tcm.getColumn(1).setPreferredWidth(120);
        tcm.getColumn(2).setPreferredWidth(100);
        tcm.getColumn(3).setPreferredWidth(80);
        tcm.getColumn(4).setPreferredWidth(80);
        tcm.getColumn(5).setPreferredWidth(100);
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
        tcm.getColumn(4).setPreferredWidth(150);
        tcm.getColumn(5).setPreferredWidth(150);
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
        DataModelUpdateHistory.addColumn("FROM_IP");

//        TableColumnModel tcm = TableUpdateHistory.getColumnModel();
//        tcm.getColumn(0).setPreferredWidth(10);
//        tcm.getColumn(2).setPreferredWidth(50);
//        tcm.getColumn(3).setPreferredWidth(20);
//        tcm.getColumn(4).setPreferredWidth(80);
//        tcm.getColumn(5).setPreferredWidth(70);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        OCDNo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        OCDDate = new javax.swing.JFormattedTextField();
        lblRevNo = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtOC_MONTH = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtFeltType = new javax.swing.JTextField();
        cmdRemove = new javax.swing.JButton();
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
        lblStatus = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

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

        jLabel2.setText("DOC NO");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 20, 80, 15);

        jLabel3.setText("DOC DATE");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(260, 20, 80, 15);

        OCDNo.setEditable(false);
        OCDNo.setBackground(new java.awt.Color(254, 242, 230));
        OCDNo.setText("PD000001");
        OCDNo.setEnabled(false);
        jPanel1.add(OCDNo);
        OCDNo.setBounds(90, 10, 140, 30);

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
        Table.setSelectionBackground(new java.awt.Color(208, 220, 234));
        Table.setSelectionForeground(new java.awt.Color(231, 16, 16));
        Table.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                TableAncestorMoved(evt);
            }
        });
        Table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TableFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TableFocusLost(evt);
            }
        });
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableMouseClicked(evt);
            }
        });
        Table.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                TableCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 90, 900, 320);

        OCDDate.setEditable(false);
        jPanel1.add(OCDDate);
        OCDDate.setBounds(350, 10, 130, 30);

        lblRevNo.setText("...");
        jPanel1.add(lblRevNo);
        lblRevNo.setBounds(230, 10, 34, 30);

        jLabel7.setText("Remark ");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(10, 50, 80, 30);
        jPanel1.add(txtRemark);
        txtRemark.setBounds(90, 50, 610, 30);

        jLabel1.setText("PPRS MONTH");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(490, 10, 100, 30);
        jPanel1.add(txtOC_MONTH);
        txtOC_MONTH.setBounds(600, 10, 100, 30);

        jButton1.setText("EXPORT TO EXCEL");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(740, 50, 160, 25);

        jLabel4.setText("FELT TYPE");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(710, 10, 80, 30);
        jPanel1.add(txtFeltType);
        txtFeltType.setBounds(790, 10, 100, 30);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setToolTipText("Remove Selected Row");
        cmdRemove.setEnabled(false);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        jPanel1.add(cmdRemove);
        cmdRemove.setBounds(800, 410, 90, 30);

        Tab.addTab("PPRS Detail", jPanel1);

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
        jLabel31.setBounds(10, 23, 66, 15);

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
        cmbHierarchy.setBounds(90, 20, 180, 24);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(10, 62, 56, 15);

        txtFrom.setBackground(new java.awt.Color(246, 238, 238));
        txtFrom.setForeground(new java.awt.Color(11, 7, 7));
        Tab2.add(txtFrom);
        txtFrom.setBounds(90, 60, 180, 19);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(10, 95, 62, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(90, 95, 530, 19);

        jLabel36.setText("Your Action  ");
        Tab2.add(jLabel36);
        jLabel36.setBounds(10, 130, 81, 15);

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
        OpgApprove.setBounds(6, 6, 171, 23);

        buttonGroup1.add(OpgFinal);
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
        OpgFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OpgFinalFocusGained(evt);
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
        OpgReject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpgRejectItemStateChanged(evt);
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
        jLabel33.setBounds(10, 253, 60, 15);

        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(90, 250, 180, 24);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(10, 292, 60, 15);

        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(90, 290, 570, 19);

        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(450, 400, 102, 25);

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
        cmdNextToTab2.setBounds(570, 400, 102, 25);

        jPanel2.add(Tab2);
        Tab2.setBounds(10, 0, 760, 460);

        Tab.addTab("Approval", jPanel2);

        jPanel3.setLayout(null);

        StatusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusPanel.setLayout(null);

        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 15);

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
        jLabel19.setBounds(10, 170, 182, 15);

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
        txtAuditRemarks.setBounds(570, 260, 129, 19);

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
        Tab.setBounds(0, 80, 930, 480);

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
        ToolBar.setBounds(0, 0, 930, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Felt PPRS Planning");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 930, 25);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 560, 930, 30);
        getContentPane().add(jPanel5);
        jPanel5.setBounds(210, 50, 10, 10);
    }// </editor-fold>//GEN-END:initComponents

    private void GenerateRejectedSendToCombo() {
        HashMap hmRejectedSendToList = new HashMap();

        cmbSendToModel = new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbSendToModel);
        DOC_NO = OCDNo.getText();
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
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInApproval(ModuleId, DOC_NO + "", ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                }

                if (OpgReject.isSelected()) {
                    //JOptionPane.showMessageDialog(null, "Module Id :"+ModuleId+", DOC No : "+sorder_no+", User Id : "+ObjUser.getAttribute("USER_ID").getInt()+", New user Id "+SDMLERPGLOBAL.gNewUserID);
                    IncludeUser = clsFeltProductionApprovalFlow.IncludeUserInRejection(ModuleId, DOC_NO + "", ObjUser.getAttribute("USER_ID").getInt(), EITLERPGLOBAL.gNewUserID);
                    // JOptionPane.showMessageDialog(null, "IncludeUser = "+IncludeUser);
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
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
//        if (JOptionPane.showConfirmDialog(this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//            Delete();
//        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        Save();
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

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        ReportShow();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmdPrintActionPerformed

    private void TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabMouseClicked


    }//GEN-LAST:event_TabMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

    }//GEN-LAST:event_jButton4ActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed

    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        ObjPieceOC.HistoryView = false;
        ObjPieceOC.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        String DocNo = OCDNo.getText();
        ObjPieceOC.ShowHistory(DocNo);
        MoveLast();

    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained

    }//GEN-LAST:event_Tab2FocusGained

    private void cmdNextToTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab2ActionPerformed

    }//GEN-LAST:event_cmdNextToTab2ActionPerformed

    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed

    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed

    private void cmdBackToTab0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackToTab0ActionPerformed

    }//GEN-LAST:event_cmdBackToTab0ActionPerformed

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained

    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained

    }//GEN-LAST:event_cmbSendToFocusGained

    private void OpgHoldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgHoldFocusGained

    }//GEN-LAST:event_OpgHoldFocusGained

    private void OpgHoldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgHoldItemStateChanged

    }//GEN-LAST:event_OpgHoldItemStateChanged

    private void OpgHoldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgHoldMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(false);
        OpgHold.setSelected(true);
    }//GEN-LAST:event_OpgHoldMouseClicked

    private void OpgRejectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgRejectFocusGained

    }//GEN-LAST:event_OpgRejectFocusGained

    private void OpgRejectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgRejectItemStateChanged

    }//GEN-LAST:event_OpgRejectItemStateChanged

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgFinalFocusGained

    }//GEN-LAST:event_OpgFinalFocusGained

    private void OpgFinalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgFinalItemStateChanged

    }//GEN-LAST:event_OpgFinalItemStateChanged

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

    private void OpgApproveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OpgApproveFocusGained
        lblStatus.setText("Select the approval action");
    }//GEN-LAST:event_OpgApproveFocusGained

    private void OpgApproveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgApproveItemStateChanged

    }//GEN-LAST:event_OpgApproveItemStateChanged

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked

        if (!OpgApprove.isEnabled()) {
            return;
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //JOptionPane.showMessageDialog(null, "SelHierarchyId : "+SelHierarchyID);

        //cmbSendTo.setEnabled(true);
        if (EditMode == EITLERPGLOBAL.EDIT) {
            GenerateRejectedSendToCombo();
            if (clsFeltProductionApprovalFlow.IsOnceRejectedDoc(ModuleId, DOC_NO + "")) {
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

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        lblStatus.setText("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        GenerateSendToCombo();

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
            OpgApprove.setEnabled(false);
            OpgApprove.setSelected(false);
        }

        if (clsHierarchy.IsCreator((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            OpgApprove.setEnabled(true);
            OpgReject.setEnabled(false);
            OpgReject.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        // if (evt.getKeyCode() == 112 || evt.getKeyCode() == 10) {

    }//GEN-LAST:event_TableKeyPressed

    private void TableAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_TableAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_TableAncestorMoved

    private void TableCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_TableCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_TableCaretPositionChanged

    private void TableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusLost
        // TODO add your handling code here:if(Table.getSelectedColumn() == 1)
        lblStatus.setText("");
    }//GEN-LAST:event_TableFocusLost

    private void TableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusGained
        // TODO add your handling code here:
//        if (Table.getSelectedColumn() == 1) {
//            lblStatus.setText("Press F1 for selection Machine No and Position No");
//        } else {
//            lblStatus.setText("");
//        }
    }//GEN-LAST:event_TableFocusGained

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:
        //jdbc:mysql://200.0.0.230:3306/PRODUCTION
//        if(Table.getSelectedColumn()==1 && (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT))
//        {
//             //   GenerateProdMfgReport();
//        }
    }//GEN-LAST:event_TableMouseClicked

    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased


    }//GEN-LAST:event_TableKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            exp.fillData(Table, new File("/root/Desktop/PPRS_PLANNING_DATA.xls"));
            exp.fillData(Table, new File("D://PPRS_PLANNING_DATA.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/PPRS_PLANNING_DATA.xls' successfully in Linux PC or 'D://PPRS_PLANNING_DATA.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        if (Table.getRowCount() > 1) {
            DataModel.removeRow(Table.getSelectedRow());
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed
    private void ReportShow() {

    }

    private void MoveFirst() {
        ObjPieceOC.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        ObjPieceOC.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        ObjPieceOC.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        ObjPieceOC.MoveLast();
        DisplayData();
    }

    private void Add() {
        //  EditMode=EITLERPGLOBAL.ADD;

        clearFields();

        EditMode = EITLERPGLOBAL.ADD;
        DisableToolbar();
        SetFields(true);
        SetupApproval();
        lblTitle.setBackground(new Color(0, 102, 153));
        lblTitle.setForeground(Color.WHITE);

        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = ModuleId;
        aList.FirstFreeNo = 391;

        OCDDate.setText(df.format(new Date()));
        FFNo = aList.FirstFreeNo;
        OCDNo.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, false));
        lblTitle.setText("Felt PPRS Planning - " + OCDNo.getText());
        //OpgHold.setSelected(true);
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.PPRSPlanning.frmFindPPRSEntry", true);
        frmFindPPRSEntry ObjFindObjPieceOC = (frmFindPPRSEntry) ObjLoader.getObj();

        if (ObjFindObjPieceOC.Cancelled == false) {
            if (!ObjPieceOC.Filter(ObjFindObjPieceOC.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.", "Find PPRS Data", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    public void FindEx(int pCompanyID, String AmendID) {
        ObjPieceOC.Filter(" PPRS_DOC_NO='" + AmendID + "'");
        ObjPieceOC.MoveFirst();
        DisplayData();
    }

    // find rate update by doc no
    public void Find(String docNo) {
        ObjPieceOC.Filter(" PPRS_DOC_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    public void FindWaiting() {
        ObjPieceOC.Filter(" AND PPRS_DOC_NO IN (SELECT DISTINCT PPRS_DOC_NO FROM PRODUCTION.FELT_PPRS_PLANNING_DETAIL, PRODUCTION.FELT_PROD_DOC_DATA WHERE PPRS_DOC_NO=DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=" + ModuleId + " AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    public void Save() {

        if (Table.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Data not correct");
            return;
        }

        if (!OpgHold.isSelected()) {

        }

        SetData();

        if (cmbHierarchy.getSelectedIndex() == -1 && SelHierarchyID == 0) {
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

        //ObjPieceOC.LoadData();
        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjPieceOC.Insert()) {
                SelectFirstFree aList = new SelectFirstFree();
                aList.ModuleID = ModuleId;
                aList.FirstFreeNo = 391;
                FFNo = aList.FirstFreeNo;
                clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, true);
                EditMode = 0;

                if (OpgFinal.isSelected()) {

                }
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + ObjPieceOC.LastError, " SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjPieceOC.Update()) {
                if (OpgFinal.isSelected() || OpgApprove.isSelected()) {

                    for (int i = 0; i <= Table.getRowCount() - 1; i++) {

                        if (!DataModel.getValueByVariable("PR_PIECE_NO", i).equals("")) {
                            if (OpgFinal.isSelected()) {
                                String PPRS_MONTH = DataModel.getValueByVariable("CONF_PPRS_MONTH", i);
                                String PIECE_NO = DataModel.getValueByVariable("PR_PIECE_NO", i);
                                String REQ_MONTH = DataModel.getValueByVariable("PR_REQUESTED_MONTH", i);
                                String PPRS_MONTH_DDMMYYYY = "";
                                String REQ_MONTH_DDMMYYYY = "";
                                String OC_NO = "";
                                String OC_DATE = "";

                                try {
                                    OC_NO = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 797, 288, true);
                                    OC_DATE = EITLERPGLOBAL.getCurrentDateDB();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (!PPRS_MONTH.equals("")) {
                                        PPRS_MONTH_DDMMYYYY = LastDayOfReqMonth(PPRS_MONTH);
                                    }
                                    if (!REQ_MONTH.equals("")) {
                                        REQ_MONTH_DDMMYYYY = LastDayOfReqMonth(REQ_MONTH);
                                    }
                                } catch (Exception ew) {
                                    ew.printStackTrace();
                                }

//                                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='PLANNING',PR_WIP_STATUS='CONFIRMED',PR_OC_MONTHYEAR='"+OC_MONTH+"',PR_CURRENT_SCH_MONTH='"+OC_MONTH+"',PR_OC_LAST_DDMMYY='"+OC_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_LAST_DDMMYY='"+OC_MONTH_DDMMYYYY+"',PR_OC_DATE='"+OC_DATE+"',PR_OC_NO='"+OC_NO+"',PR_CONTACT_PERSON='"+CONTACT_PERSON+"',PR_EMAIL_ID='"+EMAIL_ID+"',PR_PHONE_NO='"+PHONE_NO+"' WHERE PR_PIECE_NO='"+PIECE_NO+"' ");
//                                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='PLANNING',WIP_STATUS='CONFIRMED',WIP_OC_MONTHYEAR='"+OC_MONTH+"',WIP_OC_LAST_DDMMYY='"+OC_MONTH_DDMMYYYY+"',WIP_CURRENT_SCH_MONTH='"+OC_MONTH+"',WIP_CURRENT_SCH_LAST_DDMMYY='"+OC_MONTH_DDMMYYYY+"',WIP_OC_NO='"+OC_NO+"',WIP_OC_DATE='"+OC_DATE+"' WHERE WIP_PIECE_NO='"+PIECE_NO+"'");
//                                            data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL SET OC_DATE='"+OC_DATE+"',OC_NO='"+OC_NO+"' WHERE PIECE_NO='"+PIECE_NO+"' ");
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='PLANNING',PR_WIP_STATUS='CONFIRMED',PR_OC_MONTHYEAR='" + REQ_MONTH + "',PR_OC_LAST_DDMMYY='" + REQ_MONTH_DDMMYYYY + "',PR_CURRENT_SCH_MONTH='" + REQ_MONTH + "',PR_CURRENT_SCH_LAST_DDMMYY='" + REQ_MONTH_DDMMYYYY + "',PR_OC_NO='"+OC_NO+"',PR_OC_DATE='"+OC_DATE+"',PR_PPRS_MONTHYEAR='" + PPRS_MONTH + "',PR_PPRS_LAST_DDMMYY='" + PPRS_MONTH_DDMMYYYY + "' WHERE PR_PIECE_NO='" + PIECE_NO + "' ");
                                data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='PLANNING',WIP_STATUS='CONFIRMED',WIP_OC_MONTHYEAR='"+REQ_MONTH+"',WIP_OC_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"',WIP_CURRENT_SCH_MONTH='"+REQ_MONTH+"',WIP_CURRENT_SCH_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"',WIP_OC_NO='"+OC_NO+"',WIP_OC_DATE='"+OC_DATE+"',WIP_PPRS_MONTHYEAR='" + PPRS_MONTH + "',WIP_PPRS_LAST_DDMMYY='" + PPRS_MONTH_DDMMYYYY + "' WHERE WIP_PIECE_NO='" + PIECE_NO + "' ");

                            }
                        }
                    }

                }
                EditMode = 0;
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving editing. Error is " + ObjPieceOC.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
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

    private String LastDayOfReqMonth(String Req_Month) {
        int Year = Integer.parseInt(Req_Month.substring(6));
        int Month = 0;
        if (Req_Month.contains("Jan")) {
            Month = 1;
        } else if (Req_Month.contains("Feb")) {
            Month = 2;
        } else if (Req_Month.contains("Mar")) {
            Month = 3;
        } else if (Req_Month.contains("Apr")) {
            Month = 4;
        } else if (Req_Month.contains("May")) {
            Month = 5;
        } else if (Req_Month.contains("Jun")) {
            Month = 6;
        } else if (Req_Month.contains("Jul")) {
            Month = 7;
        } else if (Req_Month.contains("Aug")) {
            Month = 8;
        } else if (Req_Month.contains("Sep")) {
            Month = 9;
        } else if (Req_Month.contains("Oct")) {
            Month = 10;
        } else if (Req_Month.contains("Nov")) {
            Month = 11;
        } else if (Req_Month.contains("Dec")) {
            Month = 12;
        }

        Calendar cal = new GregorianCalendar(Year, Month, 0);
        Date date = cal.getTime();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Date : " + sdf.format(date));
        return sdf.format(date);
    }

    private void Cancel() {

        EditMode = 0;
        DisplayData();
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

        String productionDocumentNo = (String) ObjPieceOC.getAttribute("PPRS_DOC_NO").getObj();
        if (ObjPieceOC.IsEditable(productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            DisableToolbar();

            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();

            SetupApproval();
            //ReasonResetReadonly();
            //cmbOrderReason.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, productionDocumentNo)) {
                SetFields(true);
            } else {
                EnableApproval();
            }
        } else {
            JOptionPane.showMessageDialog(this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "EDITING ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Delete() {
        if (ObjPieceOC.CanDelete(OCDNo.getText() + "", OCDDate.getText(), EITLERPGLOBAL.gNewUserID)) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, ObjPieceOC.LastError, "DELETION ERROR", JOptionPane.ERROR_MESSAGE);
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
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, OCDNo.getText() + "");
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

    private void EnableApproval() {
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        txtToRemarks.setEnabled(true);
        SetupApproval();
//        SetFields(true);
        //========== Setting Up Header Fields ================//
        String FieldName = "";
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

    }

    private void SetFields(boolean pStat) {
        OCDDate.setEnabled(pStat);
        OCDNo.setEnabled(pStat);
        txtOC_MONTH.setEnabled(false);
        txtFeltType.setEnabled(false);
        txtRemark.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);

        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);

        Table.setEnabled(pStat);
        //JOptionPane.showMessageDialog(null, "Dept Id :"+EITLERPGLOBAL.gUserDeptID);

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

    private void filter(ArrayList<String> Hierarchy) {
        for (String current : Hierarchy) {
            int n = cmbHierarchyModel.getIndexOf(current);
            if (n != -1) {
                cmbHierarchyModel.setSelectedItem(current);
            }
        }

    }

    private void SetData() {

        ObjPieceOC.setAttribute("PPRS_DOC_NO", OCDNo.getText());
        ObjPieceOC.setAttribute("PPRS_DOC_DATE", OCDDate.getText());
        ObjPieceOC.setAttribute("PPRS_MONTH", PPRS_MONTH);
        ObjPieceOC.setAttribute("FELT_TYPE", FELT_TYPE);

        ObjPieceOC.setAttribute("PPRS_REMARK", txtRemark.getText());

        DOC_NO = OCDNo.getText();
        ObjPieceOC.setAttribute("DOC_NO", OCDNo.getText());
        ObjPieceOC.setAttribute("DOC_DATE", OCDDate.getText());
        ObjPieceOC.setAttribute("MODULE_ID", ModuleId);
        ObjPieceOC.setAttribute("USER_ID", EITLERPGLOBAL.gNewUserID);

        //----- Update Approval Specific Fields -----------//
        ObjPieceOC.setAttribute("HIERARCHY_ID", SelHierarchyID);
        ObjPieceOC.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjPieceOC.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjPieceOC.setAttribute("FROM_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            ObjPieceOC.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjPieceOC.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjPieceOC.setAttribute("APPROVAL_STATUS", "R");
            ObjPieceOC.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            ObjPieceOC.setAttribute("APPROVAL_STATUS", "H");
        }

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjPieceOC.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjPieceOC.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
        } else {
            ObjPieceOC.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjPieceOC.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            ObjPieceOC.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjPieceOC.setAttribute("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }

        //======= Set Line part ============
        try {
            ObjPieceOC.hmFeltPieceIOCDetails.clear();
            //ArrayList<String> RemovedPieces =new ArrayList<String>(); 
            //ArrayList<String> ApprovedPieces =new ArrayList<String>(); 
            for (int i = 0; i <= Table.getRowCount() - 1; i++) {

                clsPPRSEntryDetails objObjPieceOCDetails = new clsPPRSEntryDetails();

                if (!DataModel.getValueByVariable("PR_PIECE_NO", i).equals("")) {
                    //objObjPieceOCDetails.setAttribute("S_ORDER_DETAIL_CODE","");

                    objObjPieceOCDetails.setAttribute("PPRS_DOC_NO", OCDNo.getText());
                    objObjPieceOCDetails.setAttribute("SR_NO", (i + 1));
                    objObjPieceOCDetails.setAttribute("PIECE_NO", DataModel.getValueByVariable("PR_PIECE_NO", i));
                    objObjPieceOCDetails.setAttribute("PPRS_MONTH", DataModel.getValueByVariable("CONF_PPRS_MONTH", i));
                    objObjPieceOCDetails.setAttribute("PIECE_STAGE", DataModel.getValueByVariable("PR_PIECE_STAGE", i));
                    objObjPieceOCDetails.setAttribute("REQ_MONTH", DataModel.getValueByVariable("PR_REQUESTED_MONTH", i));
                    objObjPieceOCDetails.setAttribute("REMARK", DataModel.getValueByVariable("PPRS_REMARK", i));

                    objObjPieceOCDetails.setAttribute("ADD_PIECE_BY", DataModel.getValueByVariable("ADD_PIECE_BY", i));
                    objObjPieceOCDetails.setAttribute("ADD_PIECE_DATE", DataModel.getValueByVariable("ADD_PIECE_DATE", i));

                    if (OpgApprove.isSelected() || OpgFinal.isSelected()) {
                        ObjPieceOC.hmFeltPieceIOCDetails.put(Integer.toString(ObjPieceOC.hmFeltPieceIOCDetails.size() + 1), objObjPieceOCDetails);
                    } else if (OpgHold.isSelected() || OpgReject.isSelected()) {
                        ObjPieceOC.hmFeltPieceIOCDetails.put(Integer.toString(ObjPieceOC.hmFeltPieceIOCDetails.size() + 1), objObjPieceOCDetails);
                    } else {
                        //removed pieces

                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Eroor on setData : " + e.getMessage());
            e.printStackTrace();
        };
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField OCDDate;
    private javax.swing.JTextField OCDNo;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable Table;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
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
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFeltType;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtOC_MONTH;
    private javax.swing.JTextField txtRemark;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables
}
