/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.OrderConfirmation;

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
public class FrmPieceOC extends javax.swing.JApplet {

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
    private final int ModuleId = 798;
    private String DOC_NO = "";
    private clsPieceOC ObjPieceOC;
    private EITLComboModel cmbSendToModel;
    public HashMap objFromMainForm = new HashMap();
    String seleval = "", seltyp = "", selqlt = "", selshd = "", selpiece = "", selext = "", selinv = "", selsz = "";
    private int mlstrc;
    private int size = 0;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateForDB = new SimpleDateFormat("yyyy-MM-dd");
    public String OC_MONTH = "";
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
        System.out.println("USER = "+USER_ID+" DEPT ID = "+DEPT_ID);
        if(DEPT_ID==29)
        {
            //SALES LOGIN
            LOGIN_USER_TYPE = "SALES";
        }
        else if(DEPT_ID==40)
        {
            LOGIN_USER_TYPE = "PPC";
        }
        else
        {
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

        ObjPieceOC = new clsPieceOC();
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
    public void FillPiece()
    {
        Add();
        //objFromMainForm = Obj;
        for (int i = 0; i < objFromMainForm.size(); i++) {
                clsPieceOCDetails ObjHistory = (clsPieceOCDetails) objFromMainForm.get(i);
                
                String PIECE_NO = ObjHistory.getAttribute("PIECE_NO").getString();
                String OC_MONTH = ObjHistory.getAttribute("OC_MONTH").getString();
                
                Object[] rowData = new Object[15];
                rowData[0] = ""+i;
                rowData[0] = "";
                DataModel.addRow(rowData);
                DataModel.setValueByVariable("SRNO", (i+1)+"", i);
                DataModel.setValueByVariable("SELECT_SALES", false, i);
                DataModel.setValueByVariable("SELECT_PPC", false, i);
                DataModel.setValueByVariable("CONF_OC_MONTH", OC_MONTH, i);
                DataModel.setValueByVariable("PR_PIECE_NO", PIECE_NO, i);
                
                try{
                    ResultSet rsPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PIECE_NO+"'");
                    rsPiece.first();
                    
                    DataModel.setValueByVariable("PR_PARTY_CODE", rsPiece.getString("PR_PARTY_CODE"), i);
                    DataModel.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsPiece.getString("PR_PARTY_CODE")), i);
                    DataModel.setValueByVariable("PR_MACHINE_NO", rsPiece.getString("PR_MACHINE_NO"), i);
                    
                    String Pos_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsPiece.getString("PR_POSITION_NO"));
                    String Pos_Desc = data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsPiece.getString("PR_POSITION_NO"));
                    DataModel.setValueByVariable("POSITION_DESC", Pos_Desc, i);
                    DataModel.setValueByVariable("POSITION_DESIGN_NO", Pos_Des_No, i);
                    DataModel.setValueByVariable("UPN_NO", rsPiece.getString("PR_UPN"), i);
                    DataModel.setValueByVariable("CURRENT_SCHEDULE_MONTH", rsPiece.getString("PR_CURRENT_SCH_MONTH"), i);
                    DataModel.setValueByVariable("PR_PRODUCT_CODE", rsPiece.getString("PR_PRODUCT_CODE"), i);
                    DataModel.setValueByVariable("PR_GROUP", rsPiece.getString("PR_GROUP"), i);
                    
                    ResultSet rsData = data.getResult("SELECT CONTACT_PERSON,PHONE_NUMBER,EMAIL_ID,EMAIL_ID2,EMAIL_ID3 FROM PRODUCTION.FELT_SALES_ORDER_DETAIL d,PRODUCTION.FELT_SALES_ORDER_HEADER h where PIECE_NO='"+PIECE_NO+"'");
              
                    DataModel.setValueByVariable("CONTACT_PERSON", rsData.getString("CONTACT_PERSON"), i);
                    DataModel.setValueByVariable("PHONE_NO", rsData.getString("PHONE_NUMBER"), i);
                    DataModel.setValueByVariable("EMAIL_ID", rsData.getString("EMAIL_ID"), i);
                    DataModel.setValueByVariable("EMAIL_ID2", rsData.getString("EMAIL_ID2"), i);
                    DataModel.setValueByVariable("EMAIL_ID3", rsData.getString("EMAIL_ID3"), i);
                    
                    DataModel.setValueByVariable("PR_PIECE_STAGE", rsPiece.getString("PR_PIECE_STAGE"), i);
                    DataModel.setValueByVariable("PR_REQUESTED_MONTH", rsPiece.getString("PR_REQUESTED_MONTH"), i);
                    
                    
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
        }
    }
    public void setPiece(String PIECE_NO,String OC_MONTH)
    {
            clsPieceOC objDetail = new clsPieceOC();
            objDetail.setAttribute("PIECE_NO", PIECE_NO);
            objDetail.setAttribute("OC_MONTH", OC_MONTH);
            objFromMainForm.put(size++, objDetail);
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
            OCDNo.setText(ObjPieceOC.getAttribute("OC_DOC_NO").getString());
            lblTitle.setText("Felt Piece Order Confirmation - " + ObjPieceOC.getAttribute("OC_DOC_NO").getString());
            OCDDate.setText(EITLERPGLOBAL.formatDate(ObjPieceOC.getAttribute("OC_DOC_DATE").getString()));
            lblRevNo.setText(Integer.toString((int) ObjPieceOC.getAttribute("REVISION_NO").getVal()));
            
            txtRemark.setText(ObjPieceOC.getAttribute("OC_REMARK").getString());
            txtOC_MONTH.setText(ObjPieceOC.getAttribute("OC_MONTH").getString());
            txtFeltType.setText(ObjPieceOC.getAttribute("FELT_TYPE").getString());
            OC_MONTH = ObjPieceOC.getAttribute("OC_MONTH").getString();
            FELT_TYPE = ObjPieceOC.getAttribute("FELT_TYPE").getString();
            Tab.setTitleAt(3, "Prod Mfg Report ("+FELT_TYPE+")");
            Tab.setSelectedIndex(0);
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, ObjPieceOC.getAttribute("HIERARCHY_ID").getInt());

            FormatGrid();
            double total_length = 0;
            double total_width = 0;
            //Now Generate Table
            for (int i = 1; i <= ObjPieceOC.hmFeltPieceIOCDetails.size(); i++) {
                clsPieceOCDetails ObjItem = (clsPieceOCDetails) ObjPieceOC.hmFeltPieceIOCDetails.get(Integer.toString(i));

                Object[] rowData = new Object[1];
                DataModel.addRow(rowData);
                int NewRow = Table.getRowCount() - 1;
                DataModel.setValueByVariable("SrNo", (NewRow+1)+"", NewRow);
                
                if (ObjItem.getAttribute("SELECT_SALES").getInt() == 1) {
                    DataModel.setValueByVariable("SELECT_SALES", true, NewRow);
                } else {
                    DataModel.setValueByVariable("SELECT_SALES", false, NewRow);
                }
                
                if (ObjItem.getAttribute("SELECT_PPC").getInt() == 1) {
                    DataModel.setValueByVariable("SELECT_PPC", true, NewRow);
                } else {
                    DataModel.setValueByVariable("SELECT_PPC", false, NewRow);
                }
                
                //DataModel.setValueByVariable("SELECT_SALES", ObjItem.getAttribute("SELECT_SALES").getBool() NewRow);
                //DataModel.setValueByVariable("SELECT_PPC", ObjItem.getAttribute("SELECT_PPC").getString(), NewRow);
                DataModel.setValueByVariable("PR_PIECE_NO", ObjItem.getAttribute("PIECE_NO").getString(), NewRow);
                DataModel.setValueByVariable("OC_NO", ObjItem.getAttribute("OC_NO").getString(), NewRow);
                DataModel.setValueByVariable("OC_DATE", ObjItem.getAttribute("OC_DATE").getString(), NewRow);
                DataModel.setValueByVariable("CONF_OC_MONTH", ObjItem.getAttribute("OC_MONTH").getString(), NewRow);
                DataModel.setValueByVariable("PR_PIECE_STAGE", ObjItem.getAttribute("PIECE_STAGE").getString(), NewRow);
                DataModel.setValueByVariable("PR_REQUESTED_MONTH", ObjItem.getAttribute("REQ_MONTH").getString(), NewRow);
                DataModel.setValueByVariable("OC_REMARK", ObjItem.getAttribute("REMARK").getString(), NewRow);
                
                try{
                    ResultSet rsPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+ObjItem.getAttribute("PIECE_NO").getString()+"'");
                    rsPiece.first();
                    
                    DataModel.setValueByVariable("PR_PARTY_CODE", rsPiece.getString("PR_PARTY_CODE"), NewRow);
                    DataModel.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsPiece.getString("PR_PARTY_CODE")), NewRow);
                    DataModel.setValueByVariable("PR_MACHINE_NO", rsPiece.getString("PR_MACHINE_NO"), NewRow);
                    //DataModel.setValueByVariable("PR_POSITION_NO", rsPiece.getString("PR_POSITION_NO"), NewRow);
                    String Pos_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsPiece.getString("PR_POSITION_NO"));
                    String Pos_Desc = data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+rsPiece.getString("PR_POSITION_NO"));
                    DataModel.setValueByVariable("POSITION_DESC", Pos_Desc, NewRow);
                    DataModel.setValueByVariable("POSITION_DESIGN_NO", Pos_Des_No, NewRow);
                    DataModel.setValueByVariable("UPN_NO", rsPiece.getString("PR_UPN"), NewRow);
                    DataModel.setValueByVariable("CURRENT_SCHEDULE_MONTH", rsPiece.getString("PR_CURRENT_SCH_MONTH"), NewRow);
                    DataModel.setValueByVariable("PR_PRODUCT_CODE", rsPiece.getString("PR_PRODUCT_CODE"), NewRow);
                    DataModel.setValueByVariable("PR_GROUP", rsPiece.getString("PR_GROUP"), NewRow);
                    
                    DataModel.setValueByVariable("ADD_PIECE_BY", ObjItem.getAttribute("ADD_PIECE_BY").getString(), NewRow);
                    DataModel.setValueByVariable("ADD_PIECE_DATE", ObjItem.getAttribute("ADD_PIECE_DATE").getString(), NewRow);
                    String USER = "";
                    try{
                        USER = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(ObjItem.getAttribute("ADD_PIECE_BY").getString()));
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    DataModel.setValueByVariable("ADD_BY", USER, NewRow);
                    //System.out.println("SELECT CONTACT_PERSON,EMAIL,PHONE_NO,EMAIL_ID2,EMAIL_ID3 FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+rsPiece.getString("PR_PARTY_CODE")+"'");
                    ResultSet rsPiece_party = data.getResult("SELECT CONTACT_PERSON,EMAIL,PHONE_NO,EMAIL_ID2,EMAIL_ID3 FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+rsPiece.getString("PR_PARTY_CODE")+"'");
                    rsPiece_party.first();
                    
                    DataModel.setValueByVariable("CONTACT_PERSON", rsPiece_party.getString("CONTACT_PERSON"), NewRow);
                    DataModel.setValueByVariable("PHONE_NO", rsPiece_party.getString("PHONE_NO"), NewRow);
                    DataModel.setValueByVariable("EMAIL_ID", rsPiece_party.getString("EMAIL"), NewRow);
                    DataModel.setValueByVariable("EMAIL_ID2", rsPiece_party.getString("EMAIL_ID2"), NewRow);
                    DataModel.setValueByVariable("EMAIL_ID3", rsPiece_party.getString("EMAIL_ID3"), NewRow);
                    //EMAIL_ID2
                }catch(Exception e)
                {
                    //e.printStackTrace();
                }
            }
            //DoNotEvaluate=false;
            //UpdateTotals();
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = ObjPieceOC.getAttribute("OC_DOC_NO").getString();
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
                clsPieceOC ObjHistory = (clsPieceOC) History.get(Integer.toString(i));
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6235, 62351)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6235, 62352)) {
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6235, 62353)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6235, 62355)) {
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
            DataModel.addColumn("SALES"); //1
            DataModel.addColumn("PPC"); //1
            DataModel.addColumn("PIECE NO"); //1
            DataModel.addColumn("PARTY CODE"); //5
            DataModel.addColumn("PARTY NAME"); //6
            DataModel.addColumn("MAC NO"); //2
            DataModel.addColumn("POS DESC"); //4
            DataModel.addColumn("POS DESIGN NO"); //12
            DataModel.addColumn("UPN"); //11
            DataModel.addColumn("REQ MTH"); //11
            DataModel.addColumn("OC MONTH"); //0 - Read Only
            DataModel.addColumn("CURR SCH. MTH"); //11
            DataModel.addColumn("PRODUCT CODE"); //7
            DataModel.addColumn("GROUP"); //8
            DataModel.addColumn("PIECE STAGE"); //9
            DataModel.addColumn("OC NO"); //1
            DataModel.addColumn("OC DATE"); //1
            DataModel.addColumn("OC REMARK"); //1
            DataModel.addColumn("CONTACT PERSON"); //1
            DataModel.addColumn("PHONE NO"); //1
            DataModel.addColumn("EMAIL ID"); //1
            DataModel.addColumn("EMAIL ID2"); //1
            DataModel.addColumn("EMAIL ID3"); //1
            DataModel.addColumn("ADD BY"); //1
            DataModel.addColumn("DATE"); //1
            DataModel.addColumn("ADD BY"); //1
            
            DataModel.SetVariable(0, "SrNo"); //0 - Read Only            
            DataModel.SetVariable(1, "SELECT_SALES"); //1
            DataModel.SetVariable(2, "SELECT_PPC"); //1
            DataModel.SetVariable(3, "PR_PIECE_NO"); //1
            DataModel.SetVariable(4, "PR_PARTY_CODE"); //1
            DataModel.SetVariable(5, "PARTY_NAME"); //1
            DataModel.SetVariable(6, "PR_MACHINE_NO"); //1
            DataModel.SetVariable(7, "POSITION_DESC"); //1
            DataModel.SetVariable(8, "POSITION_DESIGN_NO"); //2
            DataModel.SetVariable(9, "UPN_NO"); //1
            DataModel.SetVariable(10, "PR_REQUESTED_MONTH"); //1
            DataModel.SetVariable(11, "CONF_OC_MONTH"); //0 - Read Only
            DataModel.SetVariable(12, "CURRENT_SCHEDULE_MONTH"); //1
            DataModel.SetVariable(13, "PR_PRODUCT_CODE"); //1
            DataModel.SetVariable(14, "PR_GROUP"); //1
            DataModel.SetVariable(15, "PR_PIECE_STAGE"); //1
            DataModel.SetVariable(16, "OC_NO"); //1
            DataModel.SetVariable(17, "OC_DATE"); //1
            DataModel.SetVariable(18, "OC_REMARK"); //1
            DataModel.SetVariable(19, "CONTACT_PERSON"); //1
            DataModel.SetVariable(20, "PHONE_NO"); //1
            DataModel.SetVariable(21, "EMAIL_ID"); //1
            DataModel.SetVariable(22, "EMAIL_ID2"); //1
            DataModel.SetVariable(23, "EMAIL_ID3"); //1
            DataModel.SetVariable(24, "ADD_PIECE_BY"); //1
            DataModel.SetVariable(25, "ADD_PIECE_DATE"); //1
            DataModel.SetVariable(26, "ADD_BY"); //1
            
            Table.getColumnModel().getColumn(0).setMaxWidth(40);
            Table.getColumnModel().getColumn(1).setMinWidth(100);
            Table.getColumnModel().getColumn(2).setMinWidth(100);
            Table.getColumnModel().getColumn(3).setMinWidth(100);
            Table.getColumnModel().getColumn(4).setMinWidth(100);
            Table.getColumnModel().getColumn(5).setMinWidth(130);
            Table.getColumnModel().getColumn(6).setMinWidth(130);
            Table.getColumnModel().getColumn(7).setMinWidth(100);
            Table.getColumnModel().getColumn(8).setMinWidth(100);
            Table.getColumnModel().getColumn(9).setMinWidth(100);
            Table.getColumnModel().getColumn(10).setMinWidth(100);
            Table.getColumnModel().getColumn(11).setMinWidth(130);
            Table.getColumnModel().getColumn(12).setMinWidth(80);
            Table.getColumnModel().getColumn(13).setMinWidth(100);
            Table.getColumnModel().getColumn(13).setMaxWidth(100);
            Table.getColumnModel().getColumn(14).setMinWidth(100);
            Table.getColumnModel().getColumn(15).setMinWidth(120);
            Table.getColumnModel().getColumn(16).setMinWidth(120);
            Table.getColumnModel().getColumn(17).setMinWidth(120);
            Table.getColumnModel().getColumn(18).setMinWidth(120);
            Table.getColumnModel().getColumn(19).setMinWidth(120);
            Table.getColumnModel().getColumn(20).setMinWidth(120);
            Table.getColumnModel().getColumn(21).setMinWidth(120);
            Table.getColumnModel().getColumn(22).setMinWidth(120);
            Table.getColumnModel().getColumn(23).setMinWidth(120);
            Table.getColumnModel().getColumn(24).setMinWidth(0);
            Table.getColumnModel().getColumn(24).setMaxWidth(0);
            Table.getColumnModel().getColumn(25).setMinWidth(120);
            Table.getColumnModel().getColumn(26).setMinWidth(140);
            
            for(int i=0;i<DataModel.getColumnCount();i++)
            {
                if(i!=2 && i!=1 && i!=19 && i!=20 && i!=21 && i!=22)
                {
                    DataModel.SetReadOnly(i);
                }
                
                if(LOGIN_USER_TYPE.equals("SALES"))
                {
                    DataModel.SetReadOnly(2);
                }
                else if(LOGIN_USER_TYPE.equals("PPC"))
                {
                    DataModel.SetReadOnly(1);
                }
                else
                {
                    DataModel.SetReadOnly(1);
                    DataModel.SetReadOnly(2);
                }
                
            }
            
            int ImportCol1 = 1;
            Renderer1.setCustomComponent(ImportCol1, "CheckBox");
            JCheckBox aCheckBox1 = new JCheckBox();
            aCheckBox1.setBackground(Color.WHITE);
            aCheckBox1.setVisible(true);
            aCheckBox1.setEnabled(true);
            aCheckBox1.setSelected(false);
            Table.getColumnModel().getColumn(ImportCol1).setCellEditor(new DefaultCellEditor(aCheckBox1));
            Table.getColumnModel().getColumn(ImportCol1).setCellRenderer(Renderer1);
                        
            int ImportCol2 = 2;
            Renderer2.setCustomComponent(ImportCol2, "CheckBox");
            JCheckBox aCheckBox2 = new JCheckBox();
            aCheckBox2.setBackground(Color.WHITE);
            aCheckBox2.setVisible(true);
            aCheckBox2.setEnabled(true);
            aCheckBox2.setSelected(false);
            Table.getColumnModel().getColumn(ImportCol2).setCellEditor(new DefaultCellEditor(aCheckBox2));
            Table.getColumnModel().getColumn(ImportCol2).setCellRenderer(Renderer2);
            
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
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtOC_MONTH = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtFeltType = new javax.swing.JTextField();
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
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableCapacityPlanning = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtSelectedPieces = new javax.swing.JLabel();
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
        jLabel2.setBounds(10, 20, 80, 16);

        jLabel3.setText("DOC DATE");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(260, 20, 80, 16);

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
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                TableAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
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
        jScrollPane1.setBounds(10, 120, 900, 310);

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

        jLabel8.setText("Piece Order Confirmation");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 100, 210, 16);

        jLabel1.setText("OC MONTH");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(490, 10, 100, 30);
        jPanel1.add(txtOC_MONTH);
        txtOC_MONTH.setBounds(580, 10, 120, 30);

        jButton1.setText("EXPORT TO EXCEL");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(744, 90, 160, 28);

        jLabel4.setText("FELT TYPE");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(710, 10, 80, 30);
        jPanel1.add(txtFeltType);
        txtFeltType.setBounds(790, 10, 100, 30);

        Tab.addTab("Piece Confirmation", jPanel1);

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
        jLabel31.setBounds(10, 23, 66, 16);

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
        cmbHierarchy.setBounds(90, 20, 180, 28);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(10, 62, 56, 16);

        txtFrom.setBackground(new java.awt.Color(246, 238, 238));
        txtFrom.setForeground(new java.awt.Color(11, 7, 7));
        Tab2.add(txtFrom);
        txtFrom.setBounds(90, 60, 180, 28);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(10, 95, 62, 16);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(90, 95, 530, 28);

        jLabel36.setText("Your Action  ");
        Tab2.add(jLabel36);
        jLabel36.setBounds(10, 130, 81, 16);

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
        jLabel33.setBounds(10, 253, 60, 16);

        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(90, 250, 180, 28);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(10, 292, 60, 16);

        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(90, 290, 570, 28);

        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(450, 400, 102, 28);

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
        cmdNextToTab2.setBounds(570, 400, 102, 28);

        jPanel2.add(Tab2);
        Tab2.setBounds(10, 0, 760, 460);

        Tab.addTab("Approval", jPanel2);

        jPanel3.setLayout(null);

        StatusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusPanel.setLayout(null);

        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 16);

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
        jLabel19.setBounds(10, 170, 182, 16);

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
        txtAuditRemarks.setBounds(570, 260, 129, 28);

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

        jPanel4.setLayout(null);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setEnabled(false);

        TableCapacityPlanning.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
        TableCapacityPlanning.setModel(new javax.swing.table.DefaultTableModel(
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
        TableCapacityPlanning.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TableCapacityPlanning.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableCapacityPlanningMouseClicked(evt);
            }
        });
        TableCapacityPlanning.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableCapacityPlanningKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(TableCapacityPlanning);

        jPanel4.add(jScrollPane3);
        jScrollPane3.setBounds(10, 60, 910, 380);

        jLabel9.setText("SELECTED PIECES :");
        jPanel4.add(jLabel9);
        jLabel9.setBounds(10, 10, 140, 16);
        jPanel4.add(txtSelectedPieces);
        txtSelectedPieces.setBounds(10, 30, 1250, 30);

        Tab.addTab("Prod Mfg Report", jPanel4);

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
        lblTitle.setText("Order Confirmation");
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
        if (JOptionPane.showConfirmDialog(this, "Are you sure want to delete this record ?", "DELETE RECORD", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Delete();
        }
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
        
        if(txtFeltType.getText().equals("SDF"))
        {
            GenerateProdMfgReportSDF();
        }
        else
        {
            GenerateProdMfgReportNonSDF();
        }
        
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
        if (Table.getSelectedColumn() == 1) {
            lblStatus.setText("Press F1 for selection Machine No and Position No");
        } else {
            lblStatus.setText("");
        }
    }//GEN-LAST:event_TableFocusGained

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:
        //jdbc:mysql://200.0.0.230:3306/PRODUCTION
        if(Table.getSelectedColumn()==1 && (EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT))
        {
             //   GenerateProdMfgReport();
        }
    }//GEN-LAST:event_TableMouseClicked

    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased

        
    }//GEN-LAST:event_TableKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            exp.fillData(Table, new File("/root/Desktop/CONFIRMATION_PIECE_DATA.xls"));
            exp.fillData(Table, new File("D://WIP_DATA.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/CONFIRMATION_PIECE_DATA.xls' successfully in Linux PC or 'D://CONFIRMATION_PIECE_DATA.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TableCapacityPlanningMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableCapacityPlanningMouseClicked
        // TODO add your handling code here:
        //        if(evt.getClickCount() == 2)
        //        {
            //            if (Table.getRowCount() <= 0) {
                //                    Cancelled = true;
                //                } else {
                //                    Cancelled = false;
                //                    ReturnVal = (String) DataModel.getValueAt(Table.getSelectedRow(), ReturnCol - 1);
                //                     //  SecondVal = (String) DataModel.getValueAt(Table.getSelectedRow(), SecondCol - 1);
                //
                //                    try {
                    //                        if (SecondCol >= 0) {
                        //                            SecondVal = (String) DataModel.getValueAt(Table.getSelectedRow(), SecondCol - 1);
                        //                        }
                    //                    } catch (Exception e) {
                    //                           System.out.println("Error on second value = "+e.getMessage());
                    //                    }
                //
                //                }
            //                aDialog.dispose();
            //                return;
            //        }
    }//GEN-LAST:event_TableCapacityPlanningMouseClicked

    private void TableCapacityPlanningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableCapacityPlanningKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TableCapacityPlanningKeyPressed
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

        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        clearFields();

        EditMode = EITLERPGLOBAL.ADD;
        DisableToolbar();
        SetFields(true);
        SetupApproval();
        lblTitle.setBackground(new Color(0, 102, 153));
        lblTitle.setForeground(Color.WHITE);

        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = ModuleId;
        aList.FirstFreeNo = 289;

        OCDDate.setText(df.format(new Date()));
        FFNo = aList.FirstFreeNo;
        OCDNo.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, false));
        lblTitle.setText("Felt Piece Order Confirmation - " + OCDNo.getText());
        //OpgHold.setSelected(true);
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.Order.frmFindObjPieceOC1", true);
        frmFindPieceOC1 ObjFindObjPieceOC = (frmFindPieceOC1) ObjLoader.getObj();

        if (ObjFindObjPieceOC.Cancelled == false) {
            if (!ObjPieceOC.Filter(ObjFindObjPieceOC.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.", "Find Felt Division", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    public void FindEx(int pCompanyID, String AmendID) {
        ObjPieceOC.Filter(" OC_DOC_NO='" + AmendID + "'");
        ObjPieceOC.MoveFirst();
        DisplayData();
    }

    // find rate update by doc no
    public void Find(String docNo) {
        ObjPieceOC.Filter(" OC_DOC_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    public void FindWaiting() {
        ObjPieceOC.Filter(" AND PROD_DOC_NO IN (SELECT DISTINCT PROD_DOC_NO FROM PRODUCTION.FELT_PROD_DATA, PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=" + ModuleId + " AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    public void Save() {

        
        if (Table.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(null, "Data not correct");
            return;
        }

//        if(Float.parseFloat(Total_length.getText()) != Float.parseFloat(DataModel_Exist.getValueByVariable("LENGTH", 0)))
//        {
//            JOptionPane.showMessageDialog(null, "Total Length is not match as per Existing Piece length");
//            return;
//        }
//        
//        if(Float.parseFloat(Total_Width.getText()) != Float.parseFloat(DataModel_Exist.getValueByVariable("WIDTH", 0)))
//        {
//            JOptionPane.showMessageDialog(null, "Total Width is not match as per Existing Piece width");
//            return;
//        }
//        
        if(!OpgHold.isSelected()){
//            for (int i = 0; i <= Table.getRowCount() - 1; i++) {
//
//                String PIECE_NO = DataModel.getValueByVariable("PR_PIECE_NO", i);
//                String CONTACT_PERSON = DataModel.getValueByVariable("CONTACT_PERSON", i);
//                String EMAIL_ID = DataModel.getValueByVariable("EMAIL_ID", i);
//
//                if(!"".equals(PIECE_NO))
//                {
//                    if("".equals(CONTACT_PERSON) || "".equals(EMAIL_ID))
//                    {
//                        JOptionPane.showMessageDialog(null, "Contact Person & Email Id is compulsory.");
//                        return;
//                    }
//                }
//                
//                
//                
//            }
        }
        
        //BUDGET REVIEW (SALES PROJECTION)
        /*
        if(OpgFinal.isSelected() || OpgApprove.isSelected())
        {
            for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                    if(!DataModel.getValueByVariable("PR_PIECE_NO", i).equals("")){
                        //objObjPieceOCDetails.setAttribute("S_ORDER_DETAIL_CODE","");
                         String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");
                         String UPN_NO = DataModel.getValueByVariable("UPN_PIECES", i);
                         String Str_query = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL where coalesce(ACESS_QTY,0)<=0 AND UPN="+UPN_NO+" AND YEAR_FROM='" + EITLERPGLOBAL.getCurrentFinYear() + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'  AND DOC_NO LIKE '_____"+Month+"%' AND APPROVED=1";
                         if (!data.IsRecordExist(Str_query)) {
                              JOptionPane.showMessageDialog(null, "Document can not Approve to confirm for month, Because Excess Qty available in Sales Projection Review.");
                              return;
                         }
                    }
            }
        }
        */
        SetData();

        if (cmbHierarchy.getSelectedIndex() == -1 && SelHierarchyID==0) {
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
                aList.FirstFreeNo = 289;
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
                if (OpgFinal.isSelected() || OpgApprove.isSelected())  {
                    //ArrayList<String> Pieces =new ArrayList<String>();
                    
                    ArrayList<String> RemovedPieces =new ArrayList<String>(); 
                    ArrayList<String> ApprovedPieces =new ArrayList<String>(); 
                    for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                        
                        if(!DataModel.getValueByVariable("PR_PIECE_NO", i).equals("")){
                                //objObjPieceOCDetails.setAttribute("S_ORDER_DETAIL_CODE","");
                                if (OpgFinal.isSelected())  {
                                        boolean sales=false,ppc=false;
                                        if (Table.getValueAt(i, 1).equals(true)) {
                                            sales=true;
                                        } else {
                                            sales=false;
                                        }

                                        if (Table.getValueAt(i, 2).equals(true)) {
                                            ppc=true;
                                        } else {
                                            ppc=false;
                                        }

                                        if(sales && ppc)
                                        {
                                            String OC_NO = "";
                                            String OC_DATE = "";

                                            try{
                                                OC_NO = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 797, 288, true);
                                                OC_DATE = EITLERPGLOBAL.getCurrentDateDB();
                                            }catch(Exception e)
                                            {   e.printStackTrace();   }


                                            String OC_MONTH = DataModel.getValueByVariable("CONF_OC_MONTH", i);
                                            String PIECE_NO = DataModel.getValueByVariable("PR_PIECE_NO", i);

                                            String CONTACT_PERSON = DataModel.getValueByVariable("CONTACT_PERSON", i);
                                            String PHONE_NO = DataModel.getValueByVariable("PHONE_NO", i);
                                            String EMAIL_ID = DataModel.getValueByVariable("EMAIL_ID", i);

                                            String OC_MONTH_DDMMYYYY = "";
                                            try{
                                                if(!OC_MONTH.equals(""))
                                                {
                                                    OC_MONTH_DDMMYYYY = LastDayOfReqMonth(OC_MONTH);
                                                }
                                            }catch(Exception ew)
                                            {
                                                ew.printStackTrace();
                                            }

                                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='PLANNING',PR_WIP_STATUS='CONFIRMED',PR_OC_MONTHYEAR='"+OC_MONTH+"',PR_CURRENT_SCH_MONTH='"+OC_MONTH+"',PR_OC_LAST_DDMMYY='"+OC_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_LAST_DDMMYY='"+OC_MONTH_DDMMYYYY+"',PR_OC_DATE='"+OC_DATE+"',PR_OC_NO='"+OC_NO+"',PR_CONTACT_PERSON='"+CONTACT_PERSON+"',PR_EMAIL_ID='"+EMAIL_ID+"',PR_PHONE_NO='"+PHONE_NO+"' WHERE PR_PIECE_NO='"+PIECE_NO+"' ");
                                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='PLANNING',WIP_STATUS='CONFIRMED',WIP_OC_MONTHYEAR='"+OC_MONTH+"',WIP_OC_LAST_DDMMYY='"+OC_MONTH_DDMMYYYY+"',WIP_CURRENT_SCH_MONTH='"+OC_MONTH+"',WIP_CURRENT_SCH_LAST_DDMMYY='"+OC_MONTH_DDMMYYYY+"',WIP_OC_NO='"+OC_NO+"',WIP_OC_DATE='"+OC_DATE+"' WHERE WIP_PIECE_NO='"+PIECE_NO+"'");
                                            data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL SET OC_DATE='"+OC_DATE+"',OC_NO='"+OC_NO+"' WHERE PIECE_NO='"+PIECE_NO+"' ");
                                            //Pieces.add(PIECE_NO);
                                        }
                                        else if(sales)
                                        {
                                            System.out.println("Sales approved = "+DataModel.getValueByVariable("PR_PIECE_NO", i));
                                        }
                                        else if(ppc)
                                        {
                                            System.out.println("PPC approved = "+DataModel.getValueByVariable("PR_PIECE_NO", i));
                                        }
                                        else
                                        {
                                            System.out.println("Both Reject = "+DataModel.getValueByVariable("PR_PIECE_NO", i));
                                        }
                                }
                                
                            
                                if (Table.getValueAt(i, 1).equals(true) && (OpgApprove.isSelected() || OpgFinal.isSelected())) {
                                    if(OpgApprove.isSelected())
                                    {
                                        ApprovedPieces.add(DataModel.getValueByVariable("PR_PIECE_NO", i));
                                    }
                                    if(OpgFinal.isSelected() && Table.getValueAt(i, 2).equals(true))
                                    {
                                        ApprovedPieces.add(DataModel.getValueByVariable("PR_PIECE_NO", i));    
                                    }
                                    if(OpgFinal.isSelected() && Table.getValueAt(i, 2).equals(false))
                                    {
                                        RemovedPieces.add(DataModel.getValueByVariable("PR_PIECE_NO", i));    
                                    }
                                }
                                else if(OpgHold.isSelected() || OpgReject.isSelected())
                                {
                                    //ObjPieceOC.hmFeltPieceIOCDetails.put(Integer.toString(ObjPieceOC.hmFeltPieceIOCDetails.size() + 1), objObjPieceOCDetails);
                                }
                                else
                                {
                                    //removed pieces
                                    try{
                                            RemovedPieces.add(DataModel.getValueByVariable("PR_PIECE_NO", i));
                                    }catch(Exception ew)
                                    {
                                        ew.printStackTrace();
                                    }
                                }    
                        }
                    }
                    if(OpgApprove.isSelected() && (!RemovedPieces.isEmpty() || !ApprovedPieces.isEmpty()))
                    {
                        sendMailRejectedPiecesBySales(ApprovedPieces,RemovedPieces);
                    }
                    if(OpgFinal.isSelected() && (!RemovedPieces.isEmpty() || !ApprovedPieces.isEmpty()))
                    {
                        sendMailRejectedPiecesByPPC(ApprovedPieces,RemovedPieces);
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
//    private void sendMailToContactPerson(ArrayList<String> Pieces)
//    {
//        
//        
//        String Pieces_Str = "";
//        
//        for(String Piece:Pieces)
//        {
//            Pieces_Str = Pieces_Str +","+Piece;
//        }
//        Pieces_Str=Pieces_Str.substring(1);
//        System.out.println("Pieces : "+Pieces_Str);
//        
//        String pMessage = "";
//                   //Felt Order No : " + S_O_NO.getText() + ",<br>Order Date : " + S_O_DATE.getText() + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) + "<br> 
//                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
//                try {
////                    Connection Conn1; 
////                    Statement stmt1;
////                    ResultSet rsData1;
////                    Conn1 = data.getConn();
////                    stmt1 = Conn1.createStatement();
////                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO IN  ('" + Pieces_Str + "')");
////                    rsData1.first();
//                    
//                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO IN  (" + Pieces_Str + ") ");
//                    System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO IN  (" + Pieces_Str + ")");
//                    rsPieces.first();
//                    
//                   
//                    if (rsPieces.getRow() > 0) {
//                        while (!rsPieces.isAfterLast()){ 
//                            
//                            
//                            String Reference;
//                            String Reference_Date;
////                            if(rsPieces.getString("PR_PARTY_CODE").equals("P.O.")){ // On 26/04/2019
//                            if(rsPieces.getString("PR_REFERENCE").equals("P.O.")){
//                                Reference = "P.O. - "+rsPieces.getString("PR_PO_NO");
//                                Reference_Date = rsPieces.getString("PR_PO_DATE");
//                            }                           
//                            else
//                            {
//                                Reference = rsPieces.getString("PR_REFERENCE");
//                                Reference_Date = rsPieces.getString("PR_REFERENCE_DATE");
//                            }
//                            
////                            pMessage = pMessage + "<br><br>"; // On 26/04/2019
//                            pMessage = "<br><br>";
//
//                            pMessage = pMessage + "<div style='min-width:1000px;'>\n" +
//        "	<div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION</u></div>\n" +
//        "	<br><br>\n" +
//        "	<div style=' width:100%; heigth :200px;'>\n" +
//        "	\n" +
//        "		<div style=' width: 50%; float:left;'>\n" +
//        "			TO:  "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsPieces.getString("PR_PARTY_CODE"))+"<br>\n" +
//        "			Party code: "+rsPieces.getString("PR_PARTY_CODE")+" <br>\n" +
//        "			Email id: "+rsPieces.getString("PR_EMAIL_ID")+"   <br>\n" +
//        "			Kind Attn: "+rsPieces.getString("PR_CONTACT_PERSON")+"  	\n" +
//        "			<hr>\n" +
//        "		</div>\n" +
//        "		<div style=' width:50%;  float:left;'>\n" +
//        "			Your order ref: "+Reference+"<br>\n" +
//        "			Date : "+EITLERPGLOBAL.formatDate(Reference_Date)+"<br>\n" +
//        "			Our order acknowledgement no & date: "+rsPieces.getString("PR_OA_NO")+" - "+EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OA_DATE"))+" <br>\n" +
//        "			Our order confirmation no & date: "+rsPieces.getString("PR_OC_NO")+" - "+EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OC_DATE"))+" <br>\n" +
//        "			<hr>\n" +
//        "		</div>\n" +
//        "	\n" +
//        "	</div>\n" +
//        "	<div>\n" +
//        "		Dear sir,\n" +
//        "		<p>\n" +
//        "		<t>In continuation of our aforesaid order acknowledgement in connection with your valued PO as mentioned above, We are pleased to inform you that your following felts covered under our said order acknowledgement will be taken up in our current manufacturing plan so as to make the same ready as per the following schedule.\n" +
//        "	</div>\n" +
//        "	<div>\n" +
//        "		<table border='1' width='100%'>\n" +
//                                    "<tr>" +
//        "			<th>M/C NO </th>\n" +
//        "			<th>POSITION</th>\n" +
//        "			<th>DESCRIPTION</th>\n" +
//        "			<th>PIECE NO</th>\n" +
//        "			<th>LENGTH</th>\n" +
//        "			<th>WIDTH</th>\n" +
//        "			<th>GSM</th>\n" +
//        "			<th>SCHEDULE</th>\n" +
//                                    "</tr>";
//
//                            
//                            String PositionDesc = data.getStringValueFromDB("select MM_MACHINE_POSITION_DESC from PRODUCTION.FELT_MACHINE_MASTER_DETAIL where MM_MACHINE_POSITION="+rsPieces.getString("PR_POSITION_NO"));
//                            String ProductDesc = data.getStringValueFromDB("SELECT PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + rsPieces.getString("PR_PRODUCT_CODE") + "' )");
//                            
//                            pMessage = pMessage +  "<tr>" +
//                                "<td> "+rsPieces.getString("PR_MACHINE_NO")+" </td>\n" +
//                                "<td>"+PositionDesc+"</td>\n" +
//                                "<td>"+ProductDesc+"</td>\n" +
//                                "<td>"+rsPieces.getString("PR_PIECE_NO")+"</td>\n" +
//                                "<td>"+rsPieces.getString("PR_BILL_LENGTH")+"</td>\n" +
//                                "<td>"+rsPieces.getString("PR_BILL_WIDTH")+"</td>\n" +
//                                "<td>"+rsPieces.getString("PR_BILL_GSM")+"</td>\n" +
//                                "<td>"+rsPieces.getString("PR_OC_MONTHYEAR")+"</td>\n" +
//                                "</tr>" ;
//                                     
//                                pMessage = pMessage + "</table>\n" +
//        "	</div>\n" +
//        
//        "	<p>\n" +
//        "	 \n" +
//        "	You are requested to kindly ensure that the said felt is lifted as soon as it gets ready. The schedules for other felts (if any) covered under your relevant PO would be confirmed later as mentioned in our order acknowledgement.<p>\n" +
//        "	Assuring you of our best services at all times and meanwhile thanking you once again. \n" +
//        "	<p>\n" +
//        "	</div>\n" +
//        "	\n" +
//        "\n" +
//        "</div>";
//
////                                pMessage = pMessage + "<div>"
////                                        + "<br><br>Reply on : felts@dineshmills.com<br><br>"
////                                        + "</div>";
//
//        //                        Connection Conn;
//        //                        Statement stmt;
//        //                        ResultSet rsData;
//
//        //                        Conn = data.getConn();
//        //                        stmt = Conn.createStatement();
//        //                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//        //                        rsData.first();
//
//                                //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
//                                //pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PARTY_CODE"));
//                                ///pMessage = pMessage + "<br> REFERENCE : " + rsData1.getString("REFERENCE");
//                                //pMessage = pMessage + "<br> REFERENCE DATE : " + rsData1.getString("REFERENCE_DATE");
//                                //pMessage = pMessage + "<br> REMARK : "+rsData1.getString("REMARK");
//
//                                pMessage = pMessage + "";
//
//                                try {
//                                        String EmailId= rsPieces.getString("PR_EMAIL_ID");
//
//                                        SendMail(EmailId.trim(), pMessage, "Order Confirmation : " + rsPieces.getString("PR_OC_NO"), "feltoc@dineshmills.com");
//                                    } catch (Exception e) {
//                                        System.out.println("Error Msg "+e.getMessage());
//                                        e.printStackTrace();
//                                    }
//                            
//                            
//                            rsPieces.next();
//                           
//                        }
//                    }
//                    
//                    
//                    
//                    
//                    
//                    
//                    
//
//                    
//                       
//
//                    } catch (Exception e) {
//                        System.out.println("Error on Mail: " + e.getMessage());
//                    }
//    }
    private String LastDayOfReqMonth(String Req_Month)
    {
        int Year = Integer.parseInt(Req_Month.substring(6));
        int Month = 0;
        if(Req_Month.contains("Jan"))
        {    Month = 1; }
        else if(Req_Month.contains("Feb"))
        {    Month = 2; }
        else if(Req_Month.contains("Mar"))
        {    Month = 3; }
        else if(Req_Month.contains("Apr"))
        {    Month = 4; }
        else if(Req_Month.contains("May"))
        {    Month = 5; }
        else if(Req_Month.contains("Jun"))
        {    Month = 6; }
        else if(Req_Month.contains("Jul"))
        {    Month = 7; }
        else if(Req_Month.contains("Aug"))
        {    Month = 8; }
        else if(Req_Month.contains("Sep"))
        {    Month = 9; }
        else if(Req_Month.contains("Oct"))
        {    Month = 10; }
        else if(Req_Month.contains("Nov"))
        {    Month = 11; }
        else if(Req_Month.contains("Dec"))
        {    Month = 12; }
        
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

        String productionDocumentNo = (String) ObjPieceOC.getAttribute("OC_DOC_NO").getObj();
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
        SetFields(true);
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

        ObjPieceOC.setAttribute("OC_DOC_NO", OCDNo.getText());
        ObjPieceOC.setAttribute("OC_DOC_DATE", OCDDate.getText());
        ObjPieceOC.setAttribute("OC_MONTH", OC_MONTH);
        ObjPieceOC.setAttribute("FELT_TYPE", FELT_TYPE);

        ObjPieceOC.setAttribute("OC_REMARK", txtRemark.getText());

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

                clsPieceOCDetails objObjPieceOCDetails = new clsPieceOCDetails();

                if(!DataModel.getValueByVariable("PR_PIECE_NO", i).equals("")){
                    //objObjPieceOCDetails.setAttribute("S_ORDER_DETAIL_CODE","");
                    
                    if (Table.getValueAt(i, 1).equals(true)) {
                        objObjPieceOCDetails.setAttribute("SELECT_SALES", 1);
                    } else {
                        objObjPieceOCDetails.setAttribute("SELECT_SALES", 0);
                    }
                    
                    if (Table.getValueAt(i, 2).equals(true)) {
                        objObjPieceOCDetails.setAttribute("SELECT_PPC", 1);
                    } else {
                        objObjPieceOCDetails.setAttribute("SELECT_PPC", 0);
                    }
                    
                    objObjPieceOCDetails.setAttribute("OC_DOC_NO", OCDNo.getText());
                    objObjPieceOCDetails.setAttribute("SR_NO", (i + 1));
                    objObjPieceOCDetails.setAttribute("PIECE_NO", DataModel.getValueByVariable("PR_PIECE_NO", i));
                    objObjPieceOCDetails.setAttribute("OC_NO", DataModel.getValueByVariable("OC_NO", i));
                    objObjPieceOCDetails.setAttribute("OC_DATE", DataModel.getValueByVariable("OC_DATE", i));
                    objObjPieceOCDetails.setAttribute("OC_MONTH", DataModel.getValueByVariable("CONF_OC_MONTH", i));
                    objObjPieceOCDetails.setAttribute("PIECE_STAGE", DataModel.getValueByVariable("PR_PIECE_STAGE", i));
                    objObjPieceOCDetails.setAttribute("REQ_MONTH", DataModel.getValueByVariable("PR_REQUESTED_MONTH", i));
                    objObjPieceOCDetails.setAttribute("REMARK", DataModel.getValueByVariable("OC_REMARK", i));
                    
                    objObjPieceOCDetails.setAttribute("CONTACT_PERSON", DataModel.getValueByVariable("CONTACT_PERSON", i));
                    objObjPieceOCDetails.setAttribute("PHONE_NO", DataModel.getValueByVariable("PHONE_NO", i));
                    objObjPieceOCDetails.setAttribute("EMAIL_ID", DataModel.getValueByVariable("EMAIL_ID", i));
                    objObjPieceOCDetails.setAttribute("EMAIL_ID2", DataModel.getValueByVariable("EMAIL_ID2", i));
                    objObjPieceOCDetails.setAttribute("EMAIL_ID3", DataModel.getValueByVariable("EMAIL_ID3", i));
                    objObjPieceOCDetails.setAttribute("ADD_PIECE_BY", DataModel.getValueByVariable("ADD_PIECE_BY", i));
                    objObjPieceOCDetails.setAttribute("ADD_PIECE_DATE", DataModel.getValueByVariable("ADD_PIECE_DATE", i));
                    
                    if (Table.getValueAt(i, 1).equals(true) && (OpgApprove.isSelected() || OpgFinal.isSelected())) {
                        ObjPieceOC.hmFeltPieceIOCDetails.put(Integer.toString(ObjPieceOC.hmFeltPieceIOCDetails.size() + 1), objObjPieceOCDetails);
//                        if(OpgApprove.isSelected())
//                        {
//                            ApprovedPieces.add(DataModel.getValueByVariable("PR_PIECE_NO", i));
//                        }
//                        if(OpgFinal.isSelected() && Table.getValueAt(i, 2).equals(true))
//                        {
//                            ApprovedPieces.add(DataModel.getValueByVariable("PR_PIECE_NO", i));    
//                        }
//                        if(OpgFinal.isSelected() && Table.getValueAt(i, 2).equals(false))
//                        {
//                            RemovedPieces.add(DataModel.getValueByVariable("PR_PIECE_NO", i));    
//                        }
                    }
                    else if(OpgHold.isSelected() || OpgReject.isSelected())
                    {
                        ObjPieceOC.hmFeltPieceIOCDetails.put(Integer.toString(ObjPieceOC.hmFeltPieceIOCDetails.size() + 1), objObjPieceOCDetails);
                    }
                    else
                    {
                        //removed pieces
                        try{
                                System.out.println("INSERT INTO PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_UNSELECTED " +
                                                " " +
                                                " VALUES " +
                                                " ('"+OCDNo.getText()+"','"+DataModel.getValueByVariable("PR_PIECE_NO", i)+"','"+DataModel.getValueByVariable("CONF_OC_MONTH", i)+"','"+DataModel.getValueByVariable("PR_REQUESTED_MONTH", i)+"') ");
                                data.Execute("INSERT INTO PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_UNSELECTED " +
                                                " " +
                                                " VALUES " +
                                                " ('"+OCDNo.getText()+"','"+DataModel.getValueByVariable("PR_PIECE_NO", i)+"','"+DataModel.getValueByVariable("CONF_OC_MONTH", i)+"','"+DataModel.getValueByVariable("PR_REQUESTED_MONTH", i)+"') ");
                                //RemovedPieces.add(DataModel.getValueByVariable("PR_PIECE_NO", i));
                        }catch(Exception ew)
                        {
                            ew.printStackTrace();
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Eroor on setData : " + e.getMessage());
            e.printStackTrace();
        };
    }
    private void sendMailRejectedPiecesBySales(ArrayList<String> ApprovedPieces,ArrayList<String> RemovedPieces)
    {
        
        String pMessage = "";
        
        pMessage = "<br><br>";

        pMessage = pMessage + "<div style='min-width:1000px;'>\n" +
        "		\n" +
        "		<p>\n" +
        "		<t>Order confirmation "+txtOC_MONTH.getText()+" document has been forwarded from sales to ppc.\n" +
        "	</div>\n" +
        "	<br><div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION (LIST OF APPROVED PIECE BY SALES)</u></div>\n" +
        "	<br><br>\n" +
        "	</div>\n" +    
        "	<div>\n" +
        "		<table border='1' width='100%'>\n" +
        "                   <tr>" +
        "			<th>PIECE NO </th>\n" +
        "			<th>MACHINE NO</th>\n" +
        "			<th>POSITION</th>\n" +
        "			<th>GROUP</th>\n" +
        "			<th>PARTY CODE</th>\n" +
        "			<th>PARTY NAME</th>\n" +
        "			<th>LENGTH</th>\n" +
        "			<th>WIDTH</th>\n" +
        "			<th>GSM</th>\n" +
        "			<th>WEIGHT</th>\n" +
        "			<th>SQMTR</th>\n" +
        "                   </tr>";
        
        
        for(String Piece:ApprovedPieces)
        {
            
             try {
                 ResultSet rsPieces = data.getResult("SELECT PR.*,P.PARTY_NAME,PS.POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR \n" +
                                                    " LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER P ON P.PARTY_CODE=PR.PR_PARTY_CODE \n" +
                                                    " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS ON PS.POSITION_NO = PR.PR_POSITION_NO " +
                                                    "WHERE PR.PR_PIECE_NO IN  ('"+Piece+"')");
                 rsPieces.first();
                 
                 
                 pMessage = pMessage +  "<tr>" +
                                            "<td> "+rsPieces.getString("PR_PIECE_NO")+" </td>\n" +
                                            "<td> "+rsPieces.getString("PR_MACHINE_NO")+"</td>\n" +
                                            "<td> "+rsPieces.getString("POSITION_DESC")+"</td>\n" +
                                            "<td> "+rsPieces.getString("PR_GROUP")+"</td>\n" +    
                                            "<td>"+rsPieces.getString("PR_PARTY_CODE")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PARTY_NAME")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_LENGTH")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_WIDTH")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_GSM")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_WEIGHT")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_SQMTR")+"</td>\n" +
                                        "</tr>" ;
                 
             }catch(Exception e)
             {
                 e.printStackTrace();
             }
            
        }
        pMessage = pMessage + "</table>\n";
        

        pMessage = pMessage + "</div>";

        pMessage = pMessage + "	<br><br><br><div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION (LIST OF REJECTED PIECE BY SALES)</u></div>\n" +
        "	<br><br>\n" +
        "	</div>\n" +    
        "	<div>\n" +
        "		<table border='1' width='100%'>\n" +
        "                   <tr>" +
        "			<th>PIECE NO </th>\n" +
        "			<th>MACHINE NO</th>\n" +
        "			<th>POSITION</th>\n" +
        "                       <th>GROUP</th>\n"+        
        "			<th>PARTY CODE</th>\n" +
        "			<th>PARTY NAME</th>\n" +
        "			<th>LENGTH</th>\n" +
        "			<th>WIDTH</th>\n" +
        "			<th>GSM</th>\n" +
        "			<th>WEIGHT</th>\n" +
        "			<th>SQMTR</th>\n" +
        "                   </tr>";
        
        
        for(String Piece:RemovedPieces)
        {
            
             try {
                 ResultSet rsPieces = data.getResult("SELECT PR.*,P.PARTY_NAME,PS.POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR\n" +
                                                    " LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER P ON P.PARTY_CODE=PR.PR_PARTY_CODE\n" +
                                                    " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS ON PS.POSITION_NO = PR.PR_POSITION_NO " +
                                                    "WHERE PR.PR_PIECE_NO IN  ('"+Piece+"') ");
                 rsPieces.first();
                 
                 pMessage = pMessage +  "<tr>" +
                                            "<td> "+rsPieces.getString("PR_PIECE_NO")+" </td>\n" +
                                            "<td> "+rsPieces.getString("PR_MACHINE_NO")+"</td>\n" +
                                            "<td> "+rsPieces.getString("POSITION_DESC")+"</td>\n" +
                                            "<td> "+rsPieces.getString("PR_GROUP")+"</td>\n" + 
                                            "<td>"+rsPieces.getString("PR_PARTY_CODE")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PARTY_NAME")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_LENGTH")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_WIDTH")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_GSM")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_WEIGHT")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_SQMTR")+"</td>\n" +
                                        "</tr>" ;
                 
             }catch(Exception e)
             {
                 e.printStackTrace();
             }
            
        }
        pMessage = pMessage + "</table>\n";
        

        pMessage = pMessage + "</div>";
        
        
        try {
                //String EmailId= "vdshanbhag@dineshmills.com,jaydeeppandya@dineshmills.com";
                String EmailId= "abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,vdshanbhag@dineshmills.com,feltpp@dineshmills.com,narendramotiani@dineshmills.com,jaydeeppandya@dineshmills.com,mitanglad@dineshmills.com,anupsinghchauhan@dineshmills.com,siddharthneogi@dineshmills.com";
                System.out.println("SALES PMessage : "+pMessage);
                SendMail(EmailId.trim(), pMessage, txtOC_MONTH.getText()+" Order confirmation document approved by sales ", "sdmlerp@dineshmills.com");
            } catch (Exception e) {
                System.out.println("Error Msg "+e.getMessage());
                e.printStackTrace();
            }
                      
    }
    private void sendMailRejectedPiecesByPPC(ArrayList<String> ApprovedPieces,ArrayList<String> RemovedPieces)
    {
        
        String pMessage = "";
        
        pMessage = "<br><br>";

        pMessage = pMessage + "<div style='min-width:1000px;'>\n" +
        "		\n" +
        "		<p>\n" +
        "		<t>Document has been final approved by ppc.\n" +
        "	</div>\n" +
        "	<div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION (LIST OF APPROVED PIECE BY PPC)</u></div>\n" +
        "	<br><br>\n" +
        "	</div>\n" +    
        "	<div>\n" +
        "		<table border='1' width='100%'>\n" +
        "                   <tr>" +
        "			<th>PIECE NO </th>\n" +
        "			<th>MACHINE NO</th>\n" +
        "			<th>POSITION</th>\n" +
        "                       <th>GROUP</th>\n"+        
        "			<th>PARTY CODE</th>\n" +
        "			<th>PARTY NAME</th>\n" +
        "			<th>LENGTH</th>\n" +
        "			<th>WIDTH</th>\n" +
        "			<th>GSM</th>\n" +
        "			<th>WEIGHT</th>\n" +
        "			<th>SQMTR</th>\n" +
        "                   </tr>";
        
        
        for(String Piece:ApprovedPieces)
        {
            
             try {
                 ResultSet rsPieces = data.getResult("SELECT PR.*,P.PARTY_NAME,PS.POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR\n" +
                                                    "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER P ON P.PARTY_CODE=PR.PR_PARTY_CODE \n" +
                                                    " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS ON PS.POSITION_NO = PR.PR_POSITION_NO " +        
                                                    "WHERE PR.PR_PIECE_NO IN  ('"+Piece+"') ");
                 rsPieces.first();
                 
                 pMessage = pMessage +  "<tr>" +
                                            "<td> "+rsPieces.getString("PR_PIECE_NO")+" </td>\n" +
                                            "<td> "+rsPieces.getString("PR_MACHINE_NO")+"</td>\n" +
                                            "<td> "+rsPieces.getString("POSITION_DESC")+"</td>\n" +
                                            "<td> "+rsPieces.getString("PR_GROUP")+"</td>\n" + 
                                            "<td>"+rsPieces.getString("PR_PARTY_CODE")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PARTY_NAME")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_LENGTH")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_WIDTH")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_GSM")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_WEIGHT")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_SQMTR")+"</td>\n" +
                                        "</tr>" ;
                 
             }catch(Exception e)
             {
                 e.printStackTrace();
             }
            
        }
        pMessage = pMessage + "</table>\n";
        

        pMessage = pMessage + "</div>";

        pMessage = pMessage + "	<br><br><br><div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION (LIST OF REJECTED PIECE BY PPC)</u></div>\n" +
        "	<br><br>\n" +
        "	</div>\n" +    
        "	<div>\n" +
        "		<table border='1' width='100%'>\n" +
        "                   <tr>" +
        "			<th>PIECE NO </th>\n" +
        "			<th>MACHINE NO</th>\n" +
        "			<th>POSITION</th>\n" +
        "                       <th>GROUP</th>\n" +        
        "			<th>PARTY CODE</th>\n" +
        "			<th>PARTY NAME</th>\n" +
        "			<th>LENGTH</th>\n" +
        "			<th>WIDTH</th>\n" +
        "			<th>GSM</th>\n" +
        "			<th>WEIGHT</th>\n" +
        "			<th>SQMTR</th>\n" +
        "                   </tr>";
        
        
        for(String Piece:RemovedPieces)
        {
             try {
                 ResultSet rsPieces = data.getResult("SELECT PR.*,P.PARTY_NAME,PS.POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR\n" +
                                                    " LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER P ON P.PARTY_CODE=PR.PR_PARTY_CODE\n" +
                                                    " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS ON PS.POSITION_NO = PR.PR_POSITION_NO " +        
                                                    " WHERE PR.PR_PIECE_NO IN  ('"+Piece+"') ");
                 rsPieces.first();
                 
                 pMessage = pMessage +  "<tr>" +
                                            "<td> "+rsPieces.getString("PR_PIECE_NO")+" </td>\n" +
                                            "<td> "+rsPieces.getString("PR_MACHINE_NO")+"</td>\n" +
                                            "<td> "+rsPieces.getString("POSITION_DESC")+"</td>\n" +
                                            "<td> "+rsPieces.getString("PR_GROUP")+"</td>\n" + 
                                            "<td>"+rsPieces.getString("PR_PARTY_CODE")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PARTY_NAME")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_LENGTH")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_WIDTH")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_GSM")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_WEIGHT")+"</td>\n" +
                                            "<td>"+rsPieces.getString("PR_BILL_SQMTR")+"</td>\n" +
                                        "</tr>" ;
                 
             }catch(Exception e)
             {
                 e.printStackTrace();
             }
            
        }
        pMessage = pMessage + "</table>\n";
        pMessage = pMessage + "</div>";
        
        
        try {
                //String EmailId= "vdshanbhag@dineshmills.com,jaydeeppandya@dineshmills.com";
                String EmailId= "abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,vdshanbhag@dineshmills.com,feltpp@dineshmills.com,narendramotiani@dineshmills.com,jaydeeppandya@dineshmills.com,mitanglad@dineshmills.com,anupsinghchauhan@dineshmills.com,siddharthneogi@dineshmills.com";
                System.out.println("PPC PMessage : "+pMessage);
                SendMail(EmailId.trim(), pMessage, txtOC_MONTH.getText()+" order confirmation document approved by PPC ", "sdmlerp@dineshmills.com");
            } catch (Exception e) {
                System.out.println("Error Msg "+e.getMessage());
                e.printStackTrace();
            }
                            
                            
            //rsPieces.next();
                      
    }
    private void GenerateProdMfgReportNonSDF()
    {
        try{
        String OC_MONTH = txtOC_MONTH.getText();
        if(OC_MONTH.equals(""))
        {
            txtSelectedPieces.setText("");
            TableCapacityPlanning.removeAll();
            DataModel_CapacityPlanning = new EITLTableModel();
            TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
            return;
        }
        String report_include_pieces = "";
        for(int i=0;i<Table.getRowCount();i++)
        {
            if(Table.getValueAt(i, 1).equals(true))
            {
                String PIECE_NO = DataModel.getValueByVariable("PR_PIECE_NO", i);
                if("".equals(report_include_pieces))
                {
                    report_include_pieces = "'" + PIECE_NO +"'";
                }
                else
                {
                    report_include_pieces = report_include_pieces + ",'" + PIECE_NO +"'";
                }
                
            }
        }
        
        txtSelectedPieces.setText(report_include_pieces);
        //String sql = "";
        String cndtn = "";
        String SUB_QRY = "";
        
        if(!"".equals(report_include_pieces))
        {
            SUB_QRY = " (PR_OC_MONTHYEAR = '" + OC_MONTH + "' OR PR_PIECE_NO IN ("+report_include_pieces+")) ";
        }
        else
        {
            SUB_QRY = " PR_OC_MONTHYEAR = '" + OC_MONTH + "' ";
        }
        data.Execute("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'");
        
        System.out.println("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'");
        
        String sql1 = "INSERT INTO PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH "
            + "(LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION,ACNE_CAPACITY,EXPORT_CAPACITY,NORTH_CAPACITY,EAST_CAPACITY,SOUTH_CAPACITY,KEY_CAPACITY,TOTAL_CAPACITY,OC_MONTH)"
            + " SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PRODUCT_CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =5 THEN PRODUCT_CAPACITY END,0)) AS ACNE,"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =6 THEN PRODUCT_CAPACITY END,0)) AS EXPORT,"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =2 THEN PRODUCT_CAPACITY END,0)) AS NORTH, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =3 THEN PRODUCT_CAPACITY END,0)) AS WEST, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =1 THEN PRODUCT_CAPACITY END,0)) AS SOUTH, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =7 THEN PRODUCT_CAPACITY END,0)) AS KEYCLIENT, "
            + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + OC_MONTH + "' AS OC_MONTH "
            + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY WHERE PRODUCT_CAPTION NOT LIKE '%SDF%' "
            + "GROUP BY PRODUCT_CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION"
            + " UNION ALL "
            + "SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,'5'AS PRODUCT_CATEGORY,'5. GRAND TOTAL' AS  PRODUCT_CAPTION,'11' AS MTR_CAPTION_CODE,'GRAND TOTAL ' AS MTR_CAPTION, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =5 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =6 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =2 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =3 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =1 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =7 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + OC_MONTH + "' AS OC_MONTH "
            + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
            + "WHERE MTR_CAPTION_CODE !=10 " ;
        
        System.out.println("\n\nSQL Query:" + sql1);
       
        data.Execute(sql1);
        
        String sql2 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
                    + "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD =5 THEN CNT END,0)) AS SC5, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
                    //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
                    + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
                    + "QM.CATEGORY,PRODUCT_CAPTION, "
                    + "MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
                    + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
                    + "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE !=10 AND INCHARGE_CODE = INCHARGE_CD  "
                    + ") AS NG GROUP BY LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS ACT "
                    + "SET CAP.ACNE_ACTUAL = ACT.SC5, "
                    + "CAP.EXPORT_ACTUAL = ACT.SC6, "
                    + "CAP.SOUTH_ACTUAL = ACT.SC1, "
                    + "CAP.NORTH_ACTUAL = ACT.SC2, "
                    + "CAP.EAST_ACTUAL = ACT.SC3, "
                    + "CAP.KEY_ACTUAL = ACT.SC7 "
                    + "WHERE ACT.CATEGORY = CAP.CATEGORY "
                    + "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
                    + "AND ACT.LOGIN_ID = CAP.LOGIN_ID ";
                    //+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";

      System.out.println("\n\nSQL Query2:" + sql2);
       
        data.Execute(sql2);
            String sql3 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
            + "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
            //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
            + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
            + "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
            + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
            + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
            + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND  PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
            + "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
                    + "AND MTR_CAPTION_CODE =10 " 
                    + " AND INCHARGE_CODE = INCHARGE_CD  "
            + ") AS NG GROUP BY LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS ACT "
            + "SET CAP.ACNE_ACTUAL = ACT.SC5, "
            + "CAP.EXPORT_ACTUAL = ACT.SC6, "
            + "CAP.SOUTH_ACTUAL = ACT.SC1, "
            + "CAP.NORTH_ACTUAL = ACT.SC2, "
            + "CAP.EAST_ACTUAL = ACT.SC3, "
            + "CAP.KEY_ACTUAL = ACT.SC7 "
            + "WHERE ACT.CATEGORY = CAP.CATEGORY "
            + "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
            + "AND ACT.LOGIN_ID = CAP.LOGIN_ID ";
            //+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";
            
            
            System.out.println("\n\nSQL Query:" + sql3);
       
        data.Execute(sql3);


            String sql4 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
            //+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, '5' collate utf8_unicode_ci AS CATEGORY,'5. GRAND TOTAL' collate utf8_unicode_ci AS PRODUCT_CAPTION,'11' collate utf8_unicode_ci AS MTR_CAPTION_CODE,'GRAND TOTAL' collate utf8_unicode_ci AS MTR_CAPTION, "
            + "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, '5'  AS CATEGORY,'5. GRAND TOTAL'  AS PRODUCT_CAPTION,'11' AS MTR_CAPTION_CODE,'GRAND TOTAL' AS MTR_CAPTION, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
            //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
            + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
            + "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
            + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
            + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
            + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
            + " AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
                    + " AND MTR_CAPTION_CODE =10 " //comment
                    + " AND INCHARGE_CODE = INCHARGE_CD  "
            + ") AS NG GROUP BY LOGIN_ID ) AS ACT "
            + "SET CAP.ACNE_ACTUAL = ACT.SC5, "
            + "CAP.EXPORT_ACTUAL = ACT.SC6, "
            + "CAP.SOUTH_ACTUAL = ACT.SC1, "
            + "CAP.NORTH_ACTUAL = ACT.SC2, "
            + "CAP.EAST_ACTUAL = ACT.SC3, "
            + "CAP.KEY_ACTUAL = ACT.SC7  "
            + "WHERE ACT.CATEGORY = CAP.CATEGORY "
            + "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
            + "AND ACT.LOGIN_ID = CAP.LOGIN_ID ";
            //+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH" ;

      System.out.println("\n\nSQL Query:" + sql4);
      data.Execute(sql4);

      
      String sql5 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH SET TOTAL_ACTUAL = ACNE_ACTUAL + EXPORT_ACTUAL + NORTH_ACTUAL + SOUTH_ACTUAL + EAST_ACTUAL + KEY_ACTUAL WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'";

      System.out.println("\n\nSQL Query 5 :" + sql5);
       
        data.Execute(sql5);

        
        String sql = "SELECT PRODUCT_CAPTION AS 'PRODUCT', MTR_CAPTION AS 'LENGTH CATEGORY',ACNE_CAPACITY ,ACNE_ACTUAL, EXPORT_CAPACITY ,EXPORT_ACTUAL ,NORTH_CAPACITY ,NORTH_ACTUAL ,EAST_CAPACITY AS 'EAST/WEST_CAPACITY',EAST_ACTUAL AS 'EAST/WEST_ACTUAL' ,SOUTH_CAPACITY,SOUTH_ACTUAL,KEY_CAPACITY AS 'KEYCLIENT_CAPACITY',KEY_ACTUAL AS 'KEYCLIENT_ACTUAL' ,TOTAL_CAPACITY ,TOTAL_ACTUAL ,OC_MONTH FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH  WHERE TOTAL_CAPACITY + TOTAL_ACTUAL > 0 AND LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'  ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";
      
        System.out.println("\n\nSQL Query 6 :" + sql);
        ResultSet rs;
        TableCapacityPlanning.removeAll();
        DataModel_CapacityPlanning = new EITLTableModel();
        TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
        TableCapacityPlanning.setAutoResizeMode(TableCapacityPlanning.AUTO_RESIZE_OFF);
        try {
            rs = EITLERP.data.getResult(sql);
            DataModel_CapacityPlanning.addColumn("Sr.No.");
            ResultSetMetaData rsInfo = rs.getMetaData();

            //Format the table from the resultset meta data
            int i = 1;
            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                DataModel_CapacityPlanning.addColumn(rsInfo.getColumnName(i));
            }
            
            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
                TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer);
            }
            
            TableCapacityPlanning.getColumnModel().getColumn(0).setMinWidth(50);
            TableCapacityPlanning.getColumnModel().getColumn(0).setMaxWidth(50);
            TableCapacityPlanning.getColumnModel().getColumn(1).setMinWidth(120);
            TableCapacityPlanning.getColumnModel().getColumn(1).setMaxWidth(120);
            TableCapacityPlanning.getColumnModel().getColumn(2).setMinWidth(200);
            TableCapacityPlanning.getColumnModel().getColumn(2).setMaxWidth(200);
            
            
            
            rs.first();
            int k = 1;
           
            if (rs.getRow() > 0) {
                while (!rs.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    rowData[0] = k;
                    //GreyTotal = GreyTotal + rs.getDouble("Grey Weight Needling");
                    for (int m = 1; m < i; m++) {
                        rowData[m] = rs.getString(m);
                        
                        //M = Column
                        //K = Row
                        
                        if(m==4 || m==6 || m==8 || m==10 || m==12 || m==14 || m==16)
                        {
                            if(Double.parseDouble(rowData[m-1].toString()) >= Double.parseDouble(rowData[m].toString()))
                            {
                                Renderer.setBackColor(k-1, m, new Color(45,219,109));
                            }
                            else
                            {
                                Renderer.setBackColor(k-1, m, new Color(243,99,99));
                            }
                        }
                        
                        Renderer.setBackColor(3, m, new Color(216,213,213));
                        Renderer.setBackColor(8, m, new Color(216,213,213));
                        Renderer.setBackColor(10, m, new Color(171,163,163));
                        
                    }
                    DataModel_CapacityPlanning.addRow(rowData);
                    
                        try{
                            //Double total_3 = Double.parseDouble(TableCapacityPlanning.getValueAt(3, 3).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(8, 3).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(9, 3).toString());;
                            //TableCapacityPlanning.setValueAt(""+total_3, 10, 3);
                            
                            
                            for(int l=3;l<16;l++)
                            {
                                Double total = Double.parseDouble(TableCapacityPlanning.getValueAt(3, l).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(8, l).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(9, l).toString());;
                                TableCapacityPlanning.setValueAt(""+total, 10, l);
                            }
                        }catch(Exception e)
                        {
                            //e.printStackTrace();
                        }
                    //3 to 16
                    
                    rs.next();
                    k++;
                }
            }
            DataModel_CapacityPlanning.TableReadOnly(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void GenerateProdMfgReportSDF()
    {
        try{
        String OC_MONTH = txtOC_MONTH.getText();
        if(OC_MONTH.equals(""))
        {
            txtSelectedPieces.setText("");
            TableCapacityPlanning.removeAll();
            DataModel_CapacityPlanning = new EITLTableModel();
            TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
            return;
        }
        String report_include_pieces = "";
        for(int i=0;i<Table.getRowCount();i++)
        {
            if(Table.getValueAt(i, 1).equals(true))
            {
                String PIECE_NO = DataModel.getValueByVariable("PR_PIECE_NO", i);
                if("".equals(report_include_pieces))
                {
                    report_include_pieces = "'" + PIECE_NO +"'";
                }
                else
                {
                    report_include_pieces = report_include_pieces + ",'" + PIECE_NO +"'";
                }
                
            }
        }
        
        txtSelectedPieces.setText(report_include_pieces);
        //String sql = "";
        String cndtn = "";
        String SUB_QRY = "";
        
        if(!"".equals(report_include_pieces))
        {
            SUB_QRY = " (PR_OC_MONTHYEAR = '" + OC_MONTH + "' OR PR_PIECE_NO IN ("+report_include_pieces+")) ";
        }
        else
        {
            SUB_QRY = " PR_OC_MONTHYEAR = '" + OC_MONTH + "' ";
        }
        data.Execute("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'");
        
        System.out.println("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'");
        
        String sql1 = "INSERT INTO PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH "
            + "(LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION,ACNE_CAPACITY,EXPORT_CAPACITY,NORTH_CAPACITY,EAST_CAPACITY,SOUTH_CAPACITY,KEY_CAPACITY,TOTAL_CAPACITY,OC_MONTH)"
            + " SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PRODUCT_CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =5 THEN PRODUCT_CAPACITY END,0)) AS ACNE,"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =6 THEN PRODUCT_CAPACITY END,0)) AS EXPORT,"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =2 THEN PRODUCT_CAPACITY END,0)) AS NORTH, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =3 THEN PRODUCT_CAPACITY END,0)) AS WEST, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =1 THEN PRODUCT_CAPACITY END,0)) AS SOUTH, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =7 THEN PRODUCT_CAPACITY END,0)) AS KEYCLIENT, "
            + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + OC_MONTH + "' AS OC_MONTH "
            + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY WHERE PRODUCT_CAPTION LIKE '%SDF%' "
            + "GROUP BY PRODUCT_CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION";
//            + " UNION ALL "
//            + "SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,'5'AS PRODUCT_CATEGORY,'5. GRAND TOTAL' AS  PRODUCT_CAPTION,'11' AS MTR_CAPTION_CODE,'GRAND TOTAL ' AS MTR_CAPTION, "
//            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =5 THEN PRODUCT_CAPACITY END,0)),"
//            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =6 THEN PRODUCT_CAPACITY END,0)),"
//            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =2 THEN PRODUCT_CAPACITY END,0)),"
//            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =3 THEN PRODUCT_CAPACITY END,0)),"
//            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =1 THEN PRODUCT_CAPACITY END,0)),"
//            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =7 THEN PRODUCT_CAPACITY END,0)),"
//            + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + OC_MONTH + "' AS OC_MONTH "
//            + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
//            + "WHERE MTR_CAPTION_CODE !=10 " ;
        
        System.out.println("\n\nSQL Query:" + sql1);
       
        data.Execute(sql1);
        
        String sql2 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD =5 THEN CNT END,0)) AS SC5, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
//+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "QM.CATEGORY,PRODUCT_CAPTION, "
+ "MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
+ "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
+ "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
+ "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
+ "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE !=10 AND INCHARGE_CODE = INCHARGE_CD  "
+ ") AS NG GROUP BY LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS ACT "
+ "SET CAP.ACNE_ACTUAL = ACT.SC5, "
+ "CAP.EXPORT_ACTUAL = ACT.SC6, "
+ "CAP.SOUTH_ACTUAL = ACT.SC1, "
+ "CAP.NORTH_ACTUAL = ACT.SC2, "
+ "CAP.EAST_ACTUAL = ACT.SC3, "
+ "CAP.KEY_ACTUAL = ACT.SC7 "
+ "WHERE ACT.CATEGORY = CAP.CATEGORY "
+ "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
+ "AND ACT.LOGIN_ID = CAP.LOGIN_ID ";
//+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";

      System.out.println("\n\nSQL Query2:" + sql2);
       
        data.Execute(sql2);
String sql3 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
//+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
+ "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
+ "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
+ "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND  PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
+ "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
        + "AND MTR_CAPTION_CODE =10 " 
        + " AND INCHARGE_CODE = INCHARGE_CD  "
+ ") AS NG GROUP BY LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS ACT "
+ "SET CAP.ACNE_ACTUAL = ACT.SC5, "
+ "CAP.EXPORT_ACTUAL = ACT.SC6, "
+ "CAP.SOUTH_ACTUAL = ACT.SC1, "
+ "CAP.NORTH_ACTUAL = ACT.SC2, "
+ "CAP.EAST_ACTUAL = ACT.SC3, "
+ "CAP.KEY_ACTUAL = ACT.SC7 "
+ "WHERE ACT.CATEGORY = CAP.CATEGORY "
+ "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
+ "AND ACT.LOGIN_ID = CAP.LOGIN_ID ";
//+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";
      System.out.println("\n\nSQL Query:" + sql3);
       
        data.Execute(sql3);


String sql4 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
//+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, '5' collate utf8_unicode_ci AS CATEGORY,'5. GRAND TOTAL' collate utf8_unicode_ci AS PRODUCT_CAPTION,'11' collate utf8_unicode_ci AS MTR_CAPTION_CODE,'GRAND TOTAL' collate utf8_unicode_ci AS MTR_CAPTION, "
+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, '5'  AS CATEGORY,'5. GRAND TOTAL'  AS PRODUCT_CAPTION,'11' AS MTR_CAPTION_CODE,'GRAND TOTAL' AS MTR_CAPTION, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
//+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
+ " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
+ "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
+ "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
+ " AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
        + " AND MTR_CAPTION_CODE =10 " //comment
        + " AND INCHARGE_CODE = INCHARGE_CD  "
+ ") AS NG GROUP BY LOGIN_ID ) AS ACT "
+ "SET CAP.ACNE_ACTUAL = ACT.SC5, "
+ "CAP.EXPORT_ACTUAL = ACT.SC6, "
+ "CAP.SOUTH_ACTUAL = ACT.SC1, "
+ "CAP.NORTH_ACTUAL = ACT.SC2, "
+ "CAP.EAST_ACTUAL = ACT.SC3, "
+ "CAP.KEY_ACTUAL = ACT.SC7  "
+ "WHERE ACT.CATEGORY = CAP.CATEGORY "
+ "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
+ "AND ACT.LOGIN_ID = CAP.LOGIN_ID ";
//+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH" ;

      System.out.println("\n\nSQL Query:" + sql4);
      data.Execute(sql4);

      
      String sql5 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH SET TOTAL_ACTUAL = ACNE_ACTUAL + EXPORT_ACTUAL + NORTH_ACTUAL + SOUTH_ACTUAL + EAST_ACTUAL + KEY_ACTUAL WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'";

      System.out.println("\n\nSQL Query 5 :" + sql5);
       
        data.Execute(sql5);

        
        String sql = "SELECT PRODUCT_CAPTION AS 'PRODUCT', MTR_CAPTION AS 'LENGTH CATEGORY',ACNE_CAPACITY ,ACNE_ACTUAL, EXPORT_CAPACITY ,EXPORT_ACTUAL ,NORTH_CAPACITY ,NORTH_ACTUAL ,EAST_CAPACITY AS 'EAST/WEST_CAPACITY',EAST_ACTUAL AS 'EAST/WEST_ACTUAL' ,SOUTH_CAPACITY,SOUTH_ACTUAL,KEY_CAPACITY AS 'KEYCLIENT_CAPACITY',KEY_ACTUAL AS 'KEYCLIENT_ACTUAL' ,TOTAL_CAPACITY ,TOTAL_ACTUAL ,OC_MONTH FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH  WHERE TOTAL_CAPACITY + TOTAL_ACTUAL > 0 AND LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'  ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";
      
        System.out.println("\n\nSQL Query 6 :" + sql);
        ResultSet rs;
        TableCapacityPlanning.removeAll();
        DataModel_CapacityPlanning = new EITLTableModel();
        TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
        TableCapacityPlanning.setAutoResizeMode(TableCapacityPlanning.AUTO_RESIZE_OFF);
        try {
            rs = EITLERP.data.getResult(sql);
            DataModel_CapacityPlanning.addColumn("Sr.No.");
            ResultSetMetaData rsInfo = rs.getMetaData();

            //Format the table from the resultset meta data
            int i = 1;
            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                DataModel_CapacityPlanning.addColumn(rsInfo.getColumnName(i));
            }
            
            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
                TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer);
            }
            
            TableCapacityPlanning.getColumnModel().getColumn(0).setMinWidth(50);
            TableCapacityPlanning.getColumnModel().getColumn(0).setMaxWidth(50);
            TableCapacityPlanning.getColumnModel().getColumn(1).setMinWidth(120);
            TableCapacityPlanning.getColumnModel().getColumn(1).setMaxWidth(120);
            TableCapacityPlanning.getColumnModel().getColumn(2).setMinWidth(200);
            TableCapacityPlanning.getColumnModel().getColumn(2).setMaxWidth(200);
            
            
            
            rs.first();
            int k = 1;
           
            if (rs.getRow() > 0) {
                while (!rs.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    rowData[0] = k;
                    //GreyTotal = GreyTotal + rs.getDouble("Grey Weight Needling");
                    for (int m = 1; m < i; m++) {
                        rowData[m] = rs.getString(m);
                        
                        if(m==4 || m==6 || m==8 || m==10 || m==12 || m==14 || m==16)
                        {
                            if(Double.parseDouble(rowData[m-1].toString()) >= Double.parseDouble(rowData[m].toString()))
                            {
                                Renderer.setBackColor(k-1, m, new Color(45,219,109));
                            }
                            else
                            {
                                Renderer.setBackColor(k-1, m, new Color(243,99,99));
                            }
                        }
                        
                        Renderer.setBackColor(3, m, Color.lightGray);
                        Renderer.setBackColor(8, m, Color.lightGray);
                        Renderer.setBackColor(11, m, Color.lightGray);
                        
                    }
                    DataModel_CapacityPlanning.addRow(rowData);
                    
                    rs.next();
                    k++;
                }
            }
            DataModel_CapacityPlanning.TableReadOnly(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
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
    private javax.swing.JTable TableCapacityPlanning;
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
    private javax.swing.JLabel txtSelectedPieces;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables
}
