/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Order;

import EITLERP.ReportRegister;
import EITLERP.AppletFrame;
import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.FeltDesignMaster.FrmFeltDesignMaster;
import EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion;
import EITLERP.FeltSales.common.FeltInvCalc;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.LOV;
import EITLERP.FeltSales.common.clsOrderValueCalc;
import EITLERP.FeltSales.common.file_management.clsDocSalesOrderPOAttachment;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Loader;
import EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend;
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
import com.sun.faces.renderkit.html_basic.ButtonRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
//import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FrmFeltOrder extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbModuleModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableModel DataModel_version_info;
    private EITLTableModel DataModel_previousData;
    private EITLTableModel DataModel_previousInvoice;
    private EITLTableModel DataModelOldTransaction;
    private EITLTableModel DataModelDoc;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    private int ModuleId = 602;
    private String DOC_NO = "";
    private clsFeltOrder feltOrder;
    private EITLComboModel cmbSendToModel;
    private TReportEngine objEngine = new TReportEngine();
    private int FinalApprovedBy = 0;

    private EITLTableCellRenderer render_invoice = new EITLTableCellRenderer();

    String seleval = "", seltyp = "", selqlt = "", selshd = "", selpiece = "", selext = "", selinv = "", selsz = "";
    private int mlstrc;

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateForDB = new SimpleDateFormat("yyyy-MM-dd");

    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    public frmPendingApprovals frmPA;
    ArrayList<String> SDF_list = new ArrayList<String>();
    ArrayList<String> NONSDF_list = new ArrayList<String>();
    EITLTableCellRenderer Renderer1 = new EITLTableCellRenderer();

    ArrayList<clsDocSalesOrderPOAttachment> dataList = new ArrayList<clsDocSalesOrderPOAttachment>();

    /**
     * Initializes the applet FrmFeltOrder
     */
    @Override
    public void init() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
        initComponents();
        GenerateCombos();
        FormatGrid();
        //FormatGrid_OldTransaction();
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
            dateMask.install(S_O_DATE);
            MaskFormatter dateMask2 = new MaskFormatter("##/##/####");
            dateMask2.setPlaceholderCharacter('_');
            dateMask2.install(REF_DATE);
            MaskFormatter dateMask3 = new MaskFormatter("##/##/####");
            dateMask3.setPlaceholderCharacter('_');
            dateMask3.install(P_O_DATE);
        } catch (ParseException ex) {
            System.out.println("Error on Mask : " + ex.getLocalizedMessage());
        }
        S_O_DATE.setText(df.format(new Date()));

        feltOrder = new clsFeltOrder();
        boolean load = feltOrder.LoadData();

        if (load) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + feltOrder.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        SetFields(false);
        cmdAddPO.setEnabled(false);
        cmdRemovePO.setEnabled(false);
        version_information();
        cmdAdd.setVisible(false);
       // REMOVE.setVisible(false);
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
        S_O_NO.setText("0");
        REGION.setText("");
        S_ENGINEER.setText("");
        PARTY_CODE.setText("");
        DISTRICT.setText("");
        CITY.setText("");
        COUNTRY.setText("");
        PARTY_NAME.setText("");
        REFERENCE.setSelectedItem("");
        REF_DATE.setText("");
        P_O_NO.setText("");
        P_O_DATE.setText("");
        REMARK.setText("");
        ORDER_VALUE.setText("0");
        lblInchargeName.setText("");
        txtContactPerson.setText("");
        txtPhoneNo.setText("");
        txtEmailId.setText("");
        txtOA_NO.setText("");
        txtOrderMonth.setText("");

        txtContactPerson.setText("");
        txtPhoneNo.setText("");
        txtContectPerson_Dinesh.setText("");
        txtPersonPosition.setText("");
        
        txtPaymentDate.setText("");
        txtPaymentRemark.setText("");
        rdoAdvancePayment.setSelected(false);
        rdoOtherPayment.setSelected(false);

        //JOptionPane.showMessageDialog(null, "Data Model size : "+DataModel.getRowCount());
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
        FormatGrid();
        // FormatGridA();
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
            btnSendFAmail.setEnabled(false);

            if (feltOrder.getAttribute("APPROVED").getInt() == 1) {
                lblTitle.setBackground(Color.BLUE);
                lblTitle.setForeground(Color.WHITE);
                btnSendFAmail.setEnabled(true);
            }

            if (feltOrder.getAttribute("APPROVED").getInt() == 0) {
                lblTitle.setBackground(Color.GRAY);
                lblTitle.setForeground(Color.BLACK);
            }

            if (feltOrder.getAttribute("CANCELED").getInt() == 1) {
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

        //clsFeltOrder.ChangeVoucherNo=false;
        clearFields();
        try {
            S_O_NO.setText(feltOrder.getAttribute("S_ORDER_NO").getString());
            lblTitle.setText("Felt Sales Order  1.3 - " + feltOrder.getAttribute("S_ORDER_NO").getString());
            S_O_DATE.setText(EITLERPGLOBAL.formatDate(feltOrder.getAttribute("S_ORDER_DATE").getString()));
            REGION.setText(feltOrder.getAttribute("REGION").getString());

            S_ENGINEER.setText(feltOrder.getAttribute("SALES_ENGINEER").getString());
            try {

                lblInchargeName.setText(clsSales_Party.getFeltInchargeName(Long.parseLong(feltOrder.getAttribute("SALES_ENGINEER").getString())));
            } catch (Exception er) {
                // er.printStackTrace();
            }
            PARTY_CODE.setText(feltOrder.getAttribute("PARTY_CODE").getString());
            PARTY_NAME.setText(feltOrder.getAttribute("PARTY_NAME").getString());
            lblRevNo.setText(Integer.toString((int) feltOrder.getAttribute("REVISION_NO").getVal()));
            Old_Piece.setSelected(feltOrder.getAttribute("OLD_PIECE").getBool());
            Old_Piece.setEnabled(false);
            chkbox_TenderParty.setSelected(feltOrder.getAttribute("TENDER_PARTY").getBool());
            chkbox_TenderParty.setEnabled(false);

            
            chbEmailUpdate.setSelected(feltOrder.getAttribute("EMAIL_UPDATE_TO_PM").getBool());

            if (feltOrder.getAttribute("EXPORT_PAYMENT_TYPE").getString().equals("ADVANCE")) {
                rdoAdvancePayment.setSelected(true);
                rdoOtherPayment.setSelected(false);
            } else if (feltOrder.getAttribute("EXPORT_PAYMENT_TYPE").getString().equals("OTHER")) {
                rdoAdvancePayment.setSelected(false);
                rdoOtherPayment.setSelected(true);
            } else {
                rdoAdvancePayment.setSelected(false);
                rdoOtherPayment.setSelected(false);
            }
            txtPaymentDate.setText(EITLERPGLOBAL.formatDate(feltOrder.getAttribute("EXPORT_PAYMENT_DATE").getString()));
            txtPaymentRemark.setText(feltOrder.getAttribute("EXPORT_PAYMENT_REMARK").getString());

            txtContectPerson_Dinesh.setText(feltOrder.getAttribute("CONTACT_PERSON_DINESH").getString());
            txtPersonPosition.setText(feltOrder.getAttribute("CONTACT_PERSON_POSITION").getString());
            
            txtContactPerson.setText(feltOrder.getAttribute("CONTACT_PERSON").getString());
            txtEmailId.setText(feltOrder.getAttribute("EMAIL_ID").getString());
            txtPhoneNo.setText(feltOrder.getAttribute("PHONE_NUMBER").getString());
            txtOA_NO.setText(feltOrder.getAttribute("OA_NO").getString());
            txtOrderMonth.setText(EITLERPGLOBAL.formatDate(feltOrder.getAttribute("OA_DATE").getString()));
            txtEmail2.setText(feltOrder.getAttribute("EMAIL_ID2").getString());
            txtEmail3.setText(feltOrder.getAttribute("EMAIL_ID3").getString());

            if (!"".equals(PARTY_CODE.getText())) {

                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    System.out.println("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.TAGGING_APPROVAL_IND  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B WHERE PARTY_CODE='" + PARTY_CODE.getText() + "' AND A.COUNTRY_ID=B.COUNTRY_ID");
                    rsData = stmt.executeQuery("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.TAGGING_APPROVAL_IND  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B WHERE PARTY_CODE='" + PARTY_CODE.getText() + "' AND A.COUNTRY_ID=B.COUNTRY_ID");
                    /*
                     rsData = stmt.executeQuery("SELECT A.CITY_ID,A.DISTRICT,Bsd.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='"+PARTY_CODE.getText()+"' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
                     rsData.first();
                     CITY.setText(rsData.getString(1));
                     DISTRICT.setText(rsData.getString(2));Zc
                     COUNTRY.setText(rsData.getString(3));
                     REGION.setText(rsData.getString(4));
                     PARTY_NAME.setText(rsData.getString(5));
                     S_ENGINEER.setText(rsData.getString(6));
                     */
                    rsData.first();
                    CITY.setText(rsData.getString(1));
                    DISTRICT.setText(rsData.getString(2));
                    COUNTRY.setText(rsData.getString(3));
                    if (rsData.getString("TAGGING_APPROVAL_IND").equals("3")) {
                        chkbox_TenderParty.setSelected(true);
                    } else {
                        chkbox_TenderParty.setSelected(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error onm fetch data for D_SAL_PARTY_MASTER " + e.getMessage());
                }

            }

            REFERENCE.setSelectedItem(feltOrder.getAttribute("REFERENCE").getString());
            REF_DATE.setText(EITLERPGLOBAL.formatDate(feltOrder.getAttribute("REFERENCE_DATE").getString()));
            P_O_NO.setText(feltOrder.getAttribute("P_O_NO").getString());
            P_O_DATE.setText(EITLERPGLOBAL.formatDate(feltOrder.getAttribute("P_O_DATE").getString()));
            REMARK.setText(feltOrder.getAttribute("REMARK").getString());
            ORDER_VALUE.setText("0");
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, feltOrder.getAttribute("HIERARCHY_ID").getInt());

            FormatGrid();
            //Now Generate Table
            for (int i = 1; i <= feltOrder.hmFeltSalesOrderDetails.size(); i++) {
                clsFeltSalesOrderDetails ObjItem = (clsFeltSalesOrderDetails) feltOrder.hmFeltSalesOrderDetails.get(Integer.toString(i));

                Object[] rowData = new Object[1];
                DataModel.addRow(rowData);
                int NewRow = Table.getRowCount() - 1;
                DataModel.setValueByVariable("SrNo", Integer.toString(i), NewRow);
                DataModel.setValueByVariable("MACHINE_NO", ObjItem.getAttribute("MACHINE_NO").getString(), NewRow);
                DataModel.setValueByVariable("POSITION", ObjItem.getAttribute("POSITION").getString(), NewRow);
                DataModel.setValueByVariable("POSITION_DESC", ObjItem.getAttribute("POSITION_DESC").getString(), NewRow);
                DataModel.setValueByVariable("UPN", ObjItem.getAttribute("UPN").getString(), NewRow);
                DataModel.setValueByVariable("PIECE_NO", ObjItem.getAttribute("PIECE_NO").getString(), NewRow);
                DataModel.setValueByVariable("LAYER_TYPE", ObjItem.getAttribute("LAYER_TYPE").getString(), NewRow);
                DataModel.setValueByVariable("PRODUCT_CODE", ObjItem.getAttribute("PRODUCT_CODE").getString(), NewRow);

                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                String ITEM_DESC = "", SYN = "";
                String Group_Name = "";
                try {
                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    String Prod_Code = ObjItem.getAttribute("PRODUCT_CODE").getString().substring(0, 6);
                    rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER,GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER where PRODUCT_CODE = '" + Prod_Code + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");
                    System.out.println("SELECT PRODUCT_DESC,SYN_PER,GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER where PRODUCT_CODE = '" + Prod_Code + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");
                    rsData.first();
                    ITEM_DESC = rsData.getString(1);
                    SYN = rsData.getString(2);
                    Group_Name = rsData.getString(3);
                } catch (Exception e) {
                    System.out.println("Error : " + e.getMessage());
                }
                DataModel.setValueByVariable("DESCRIPTION", ITEM_DESC, NewRow);
                //DataModel.setValueByVariable("PRODUCT_DESC", ITEM_DESC, NewRow);
                DataModel.setValueByVariable("REMARK", ObjItem.getAttribute("REMARK").getString(), NewRow);
                DataModel.setValueByVariable("REQ_MONTH", ObjItem.getAttribute("REQ_MONTH").getString(), NewRow);
                DataModel.setValueByVariable("SYN_PER", SYN, NewRow);
                DataModel.setValueByVariable("GROUP", Group_Name, NewRow);

                DataModel.setValueByVariable("PR_BILL_LENGTH", ObjItem.getAttribute("PR_BILL_LENGTH").getString(), NewRow); //
                DataModel.setValueByVariable("PR_BILL_WIDTH", ObjItem.getAttribute("PR_BILL_WIDTH").getString(), NewRow); //
                DataModel.setValueByVariable("PR_BILL_WEIGHT", ObjItem.getAttribute("PR_BILL_WEIGHT").getString(), NewRow); //
                DataModel.setValueByVariable("PR_BILL_SQMTR", ObjItem.getAttribute("PR_BILL_SQMTR").getString(), NewRow); //
                DataModel.setValueByVariable("PR_BILL_GSM", ObjItem.getAttribute("PR_BILL_GSM").getString(), NewRow); //
                DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", ObjItem.getAttribute("PR_BILL_PRODUCT_CODE").getString(), NewRow); //
                DataModel.setValueByVariable("PR_BILL_STYLE", ObjItem.getAttribute("PR_BILL_STYLE").getString(), NewRow); //
                //        DataModel.setValueByVariable("DATE_SLOT", ObjItem.getAttribute("DATE_SLOT").getString(), NewRow); //
                DataModel.setValueByVariable("TENDER_WEIGHT", ObjItem.getAttribute("TENDER_WEIGHT").getString(), NewRow); //
                DataModel.setValueByVariable("TENDER_GSM", ObjItem.getAttribute("TENDER_GSM").getString(), NewRow); //
                DataModel.setValueByVariable("DM_REVISION_NO", ObjItem.getAttribute("DM_REVISION_NO").getString(), NewRow); //
                DataModel.setValueByVariable("DUMMY_REF_NO", ObjItem.getAttribute("DUMMY_REF_NO").getString(), NewRow); //

                if (ObjItem.getAttribute("PIECE_MERGE").getInt() == 1) {
                    DataModel.setValueByVariable("PIECE_MERGE", true, NewRow);
                } else {
                    DataModel.setValueByVariable("PIECE_MERGE", false, NewRow);
                }

                if (EditMode == EITLERPGLOBAL.EDIT) {

                    //JOptionPane.showMessageDialog(null, "COMBINATION CODE = "+search.ReturnVal+search.SecondVal);
                    String LastQuery = "SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_ITEM_CODE,MM_GRUP,(MM_FELT_LENGTH+MM_FABRIC_LENGTH),(MM_FELT_WIDTH+MM_FABRIC_WIDTH),MM_FELT_GSM,concat(MM_FELT_STYLE,MM_STYLE_DRY) as MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL where MM_MACHINE_NO =  " + ObjItem.getAttribute("MACHINE_NO").getString() + "  AND MM_MACHINE_POSITION=" + ObjItem.getAttribute("POSITION").getString() + "  AND MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND (MM_MACHINE_NO!='' AND MM_MACHINE_POSITION!='' AND (MM_FELT_LENGTH != '' OR MM_FABRIC_LENGTH != '') AND (MM_FELT_WIDTH != '' OR MM_FABRIC_WIDTH != '')  AND MM_FELT_GSM!='')  ORDER BY  MM_MACHINE_NO,MM_MACHINE_POSITION";
                    System.out.println("UPDATES : Machine Master Query = " + LastQuery);

                    try {
                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        rsData = stmt.executeQuery(LastQuery);
                        rsData.first();

                        String Machine_No = rsData.getString(1);
                        String Position = rsData.getString(2);
                        String Position_Desc = rsData.getString(3);
                        String Item_Code = rsData.getString(4);

                        String LENGTH = rsData.getString(6);
                        String WIDTH = rsData.getString(7);
                        String GSM = rsData.getString(8);
                        String STYLE = rsData.getString(9);

//                        DecimalFormat f_single = new DecimalFormat("##.0");
//                        DecimalFormat f_double = new DecimalFormat("##.00");
                        float Theoritical_Weigth = (Float.parseFloat(LENGTH) * Float.parseFloat(WIDTH) * Float.parseFloat(GSM) / 1000);
                        float SQMT = (Float.parseFloat(LENGTH) * Float.parseFloat(WIDTH));

                        FeltInvCalc inv_calc = new FeltInvCalc();

                        try {
                            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

                            inv_calc = clsOrderValueCalc.calculate("", Item_Code, PARTY_CODE.getText(), Float.parseFloat(LENGTH), Float.parseFloat(WIDTH), Float.parseFloat(EITLERPGLOBAL.round(Theoritical_Weigth, 1) + ""), SQMT, df1.format(df.parse(S_O_DATE.getText())));
                            //calculate(String Piece_No,String Product_Code,String Party_Code,Float ,Float Width,Integer GSM,Float Weight,Float SQMT,String Order_Date)
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }

                        DataModel.setValueByVariable("LENGTH", LENGTH, NewRow);
                        DataModel.setValueByVariable("WIDTH", WIDTH, NewRow);
                        DataModel.setValueByVariable("GSM", GSM, NewRow);
                        DataModel.setValueByVariable("THORITICAL_WIDTH", EITLERPGLOBAL.round(Theoritical_Weigth, 1) + "", NewRow);
                        DataModel.setValueByVariable("SQ_MTR", EITLERPGLOBAL.round(SQMT, 2) + "", NewRow);
                        DataModel.setValueByVariable("STYLE", STYLE, NewRow);

                        DataModel.setValueByVariable("OV_RATE", inv_calc.getFicRate() + "", NewRow);

                        DataModel.setValueByVariable("SURCHARGE_PER", inv_calc.getFicSurcharge_per() + "", NewRow);
                        DataModel.setValueByVariable("SURCHARGE_RATE", inv_calc.getFicSurcharge_rate() + "", NewRow);
                        DataModel.setValueByVariable("GROSS_RATE", inv_calc.getFicGrossRate() + "", NewRow);

                        DataModel.setValueByVariable("OV_BAS_AMOUNT", inv_calc.getFicBasAmount() + "", NewRow); //18
                        DataModel.setValueByVariable("OV_CHEM_TRT_CHG", inv_calc.getFicChemTrtChg() + "", NewRow); //19
                        DataModel.setValueByVariable("OV_SPIRAL_CHG", inv_calc.getFicSpiralChg() + "", NewRow); //20
                        DataModel.setValueByVariable("OV_PIN_CHG", inv_calc.getFicPinChg() + "", NewRow); //21
                        DataModel.setValueByVariable("OV_SEAM_CHG", inv_calc.getFicSeamChg() + "", NewRow); //22
                        DataModel.setValueByVariable("OV_INS_IND", inv_calc.getFicInsInd() + "", NewRow); //23
                        DataModel.setValueByVariable("OV_INS_AMT", inv_calc.getFicInsAmt() + "", NewRow); //24
                        DataModel.setValueByVariable("OV_EXCISE", inv_calc.getFicExcise() + "", NewRow); //25
                        DataModel.setValueByVariable("OV_DISC_PER", inv_calc.getFicDiscPer() + "", NewRow); //26
                        DataModel.setValueByVariable("OV_DISC_AMT", inv_calc.getFicDiscAmt() + "", NewRow); //27
                        DataModel.setValueByVariable("OV_DISC_BASAMT", inv_calc.getFicDiscBasamt() + "", NewRow); //28
                        DataModel.setValueByVariable("OV_AMT", inv_calc.getFicInvAmt() + "", NewRow); //18
                        DataModel.setValueByVariable("VAT1", "", NewRow); //19
                        DataModel.setValueByVariable("VAT4", "", NewRow); //20
                        DataModel.setValueByVariable("CST2", "", NewRow); //21
                        DataModel.setValueByVariable("CST5", "", NewRow); //22
                        DataModel.setValueByVariable("CGST_PER", inv_calc.getFicCGSTPER() + "", NewRow); //34
                        DataModel.setValueByVariable("CGST_AMT", inv_calc.getFicCGST() + "", NewRow); //35
                        DataModel.setValueByVariable("SGST_PER", inv_calc.getFicSGSTPER() + "", NewRow); //36
                        DataModel.setValueByVariable("SGST_AMT", inv_calc.getFicSGST() + "", NewRow); //37
                        DataModel.setValueByVariable("IGST_PER", inv_calc.getFicIGSTPER() + "", NewRow); //38
                        DataModel.setValueByVariable("IGST_AMT", inv_calc.getFicIGST() + "", NewRow); //39
                        DataModel.setValueByVariable("COMPOSITION_PER", "0", NewRow); //40
                        DataModel.setValueByVariable("COMPOSITION_AMT", "0", NewRow); //41
                        DataModel.setValueByVariable("RCM_PER", "0", NewRow); //42
                        DataModel.setValueByVariable("RCM_AMT", "0", NewRow); //43
                        DataModel.setValueByVariable("GST_COMPENSATION_CESS_PER", "0", NewRow); //44
                        DataModel.setValueByVariable("GST_COMPENSATION_CESS_AMT", "0", NewRow); //45

                        ORDER_VALUE.setText((Float.parseFloat(ORDER_VALUE.getText()) + inv_calc.getFicInvAmt()) + "");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    DataModel.setValueByVariable("GROUP", ObjItem.getAttribute("S_GROUP").getString(), NewRow);
                    DataModel.setValueByVariable("LENGTH", ObjItem.getAttribute("LENGTH").getString(), NewRow);
                    DataModel.setValueByVariable("WIDTH", ObjItem.getAttribute("WIDTH").getString(), NewRow);
                    DataModel.setValueByVariable("GSM", ObjItem.getAttribute("GSM").getString(), NewRow);
                    DataModel.setValueByVariable("THORITICAL_WIDTH", EITLERPGLOBAL.round(Float.parseFloat(ObjItem.getAttribute("THORITICAL_WIDTH").getString()), 1) + "", NewRow);
                    DataModel.setValueByVariable("SQ_MTR", EITLERPGLOBAL.round(Float.parseFloat(ObjItem.getAttribute("SQ_MTR").getString()), 1) + "", NewRow);
                    DataModel.setValueByVariable("STYLE", ObjItem.getAttribute("STYLE").getString(), NewRow);
                    //JOptionPane.showMessageDialog(null, "Data Catched OV_RATE : "+ObjItem.getAttribute("OV_RATE").getString());
                    DataModel.setValueByVariable("OV_RATE", ObjItem.getAttribute("OV_RATE").getString(), NewRow);

                    DataModel.setValueByVariable("SURCHARGE_PER", ObjItem.getAttribute("SURCHARGE_PER").getString(), NewRow);
                    DataModel.setValueByVariable("SURCHARGE_RATE", ObjItem.getAttribute("SURCHARGE_RATE").getString(), NewRow);
                    DataModel.setValueByVariable("GROSS_RATE", ObjItem.getAttribute("GROSS_RATE").getString(), NewRow);

                    //JOptionPane.showMessageDialog(null, "Data Catched OV_BAS_AMOUNT : "+ObjItem.getAttribute("OV_BAS_AMOUNT").getString());
                    DataModel.setValueByVariable("OV_BAS_AMOUNT", ObjItem.getAttribute("OV_BAS_AMOUNT").getString(), NewRow); //18
                    DataModel.setValueByVariable("OV_CHEM_TRT_CHG", ObjItem.getAttribute("OV_CHEM_TRT_CHG").getString(), NewRow); //19
                    DataModel.setValueByVariable("OV_SPIRAL_CHG", ObjItem.getAttribute("OV_SPIRAL_CHG").getString(), NewRow); //20
                    DataModel.setValueByVariable("OV_PIN_CHG", ObjItem.getAttribute("OV_PIN_CHG").getString(), NewRow); //21
                    DataModel.setValueByVariable("OV_SEAM_CHG", ObjItem.getAttribute("OV_SEAM_CHG").getString(), NewRow); //22
                    DataModel.setValueByVariable("OV_INS_IND", ObjItem.getAttribute("OV_INS_IND").getString(), NewRow); //23
                    DataModel.setValueByVariable("OV_INS_AMT", ObjItem.getAttribute("OV_INS_AMT").getString(), NewRow); //24
                    DataModel.setValueByVariable("OV_EXCISE", ObjItem.getAttribute("OV_EXCISE").getString(), NewRow); //25
                    DataModel.setValueByVariable("OV_DISC_PER", ObjItem.getAttribute("OV_DISC_PER").getString(), NewRow); //26
                    DataModel.setValueByVariable("OV_DISC_AMT", ObjItem.getAttribute("OV_DISC_AMT").getString(), NewRow); //27
                    DataModel.setValueByVariable("OV_DISC_BASAMT", ObjItem.getAttribute("OV_DISC_BASAMT").getString(), NewRow); //28
                    DataModel.setValueByVariable("OV_AMT", ObjItem.getAttribute("OV_AMT").getString(), NewRow); //18
                    DataModel.setValueByVariable("VAT1", "", NewRow); //19
                    DataModel.setValueByVariable("VAT4", "", NewRow); //20
                    DataModel.setValueByVariable("CST2", "", NewRow); //21
                    DataModel.setValueByVariable("CST5", "", NewRow); //22
                    DataModel.setValueByVariable("CGST_PER", ObjItem.getAttribute("CGST_PER").getString(), NewRow); //34
                    DataModel.setValueByVariable("CGST_AMT", ObjItem.getAttribute("CGST_AMT").getString(), NewRow); //35
                    DataModel.setValueByVariable("SGST_PER", ObjItem.getAttribute("SGST_PER").getString(), NewRow); //36
                    DataModel.setValueByVariable("SGST_AMT", ObjItem.getAttribute("SGST_AMT").getString(), NewRow); //37
                    DataModel.setValueByVariable("IGST_PER", ObjItem.getAttribute("IGST_PER").getString(), NewRow); //38
                    DataModel.setValueByVariable("IGST_AMT", ObjItem.getAttribute("IGST_AMT").getString(), NewRow); //39
                    DataModel.setValueByVariable("COMPOSITION_PER", ObjItem.getAttribute("COMPOSITION_PER").getString(), NewRow); //40
                    DataModel.setValueByVariable("COMPOSITION_AMT", ObjItem.getAttribute("COMPOSITION_AMT").getString(), NewRow); //41
                    DataModel.setValueByVariable("RCM_PER", ObjItem.getAttribute("RCM_PER").getString(), NewRow); //42
                    DataModel.setValueByVariable("RCM_AMT", ObjItem.getAttribute("RCM_AMT").getString(), NewRow); //43
                    DataModel.setValueByVariable("GST_COMPENSATION_CESS_PER", ObjItem.getAttribute("GST_COMPENSATION_CESS_PER").getString(), NewRow); //44
                    DataModel.setValueByVariable("GST_COMPENSATION_CESS_AMT", ObjItem.getAttribute("GST_COMPENSATION_CESS_AMT").getString(), NewRow); //45

                    ORDER_VALUE.setText((Float.parseFloat(ORDER_VALUE.getText()) + Float.parseFloat(ObjItem.getAttribute("OV_AMT").getString())) + "");
                }

            }

            //Added PO Attachment 
            ResultSet tdocrs = data.getResult("SELECT * FROM DOC_MGMT.SALES_ORDER_PO_ATTACHMENT WHERE DOCUMENT_DOC_NO='" + S_O_NO.getText() + "'");
            tdocrs.first();
            if (tdocrs.getRow() > 0) {
                while (!tdocrs.isAfterLast()) {
                    Object[] rowData = new Object[50];
                    rowData[0] = tdocrs.getString("DOCUMENT_SR_NO");
                    rowData[1] = tdocrs.getString("DOC_REMARK");
                    rowData[2] = tdocrs.getString("DOC_TYPE");
                    rowData[3] = "UPLOAD";
                    rowData[4] = "VIEW";
                    rowData[5] = tdocrs.getString("DOC_NAME");
                    DataModelDoc.addRow(rowData);
                    tdocrs.next();
                }
            }

            //DoNotEvaluate=false;
            //UpdateTotals();
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = feltOrder.getAttribute("S_ORDER_NO").getString();
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
            HashMap History = feltOrder.getHistoryList(EITLERPGLOBAL.gCompanyID + "", DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsFeltOrder ObjHistory = (clsFeltOrder) History.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Integer.parseInt(ObjHistory.getAttribute("UPDATED_BY").getString()));
                rowData[2] = ObjHistory.getAttribute("UPDATED_DATE").getString();

                String ApprovalStatus = "";

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                    FinalApprovedBy = Integer.parseInt(ObjHistory.getAttribute("UPDATED_BY").getString());
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
            setPrevoiousData();
            setPreviousInvoice();
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6052, 60521)) { //7008,70081
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6052, 60522)) { //7008,70082
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6052, 60523)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6052, 60525)) {
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
            DataModel.addColumn("MACHINE NO"); //1
            DataModel.addColumn("POSITION"); //2
            DataModel.addColumn("POSITION DESC"); //3
            DataModel.addColumn("UPN"); //3
            DataModel.addColumn("PIECE NO"); //4
            DataModel.addColumn("LAYER TYPE");
            DataModel.addColumn("PRODUCT"); //5
            DataModel.addColumn("DESCRIPTION"); //6
            DataModel.addColumn("GROUP"); //7
            DataModel.addColumn("LENGTH"); //8
            DataModel.addColumn("WIDTH"); //9
            DataModel.addColumn("GSM"); //10
            DataModel.addColumn("THORTICAL WEIGHT"); //11
            DataModel.addColumn("SQ MT"); //12
            DataModel.addColumn("STYLE"); //13
            DataModel.addColumn("REQ MONTH"); //14
            DataModel.addColumn("SYN(%)"); //15
            DataModel.addColumn("REMARK"); //16
            DataModel.addColumn("RATE"); //17
            DataModel.addColumn("SURCHARGE PER"); //17
            DataModel.addColumn("SURCHARGE RATE"); //17
            DataModel.addColumn("SURCHARGE 1 PER"); //17
            DataModel.addColumn("SURCHARGE 1 RATE"); //17
            DataModel.addColumn("GROSS RATE"); //17
            DataModel.addColumn("BAS_AMOUNT"); //18
            DataModel.addColumn("CHEM_TRT_CHG"); //19
            DataModel.addColumn("SPIRAL_CHG"); //20
            DataModel.addColumn("PIN_CHG"); //21
            DataModel.addColumn("SEAM_CHG"); //22
            DataModel.addColumn("INS_IND"); //23
            DataModel.addColumn("INS_AMT"); //24
            DataModel.addColumn("EXCISE"); //25
            DataModel.addColumn("DISC_PER"); //26
            DataModel.addColumn("DISC_AMT"); //27
            DataModel.addColumn("DISC_BASAMT"); //28
            DataModel.addColumn("OV AMT"); //29
            DataModel.addColumn("VAT1"); //30
            DataModel.addColumn("VAT4"); //31
            DataModel.addColumn("CST2"); //32
            DataModel.addColumn("CST5"); //33
            DataModel.addColumn("CGST PER");
            DataModel.addColumn("CGST AMT");
            DataModel.addColumn("SGST PER");
            DataModel.addColumn("SGST AMT");
            DataModel.addColumn("IGST PER");
            DataModel.addColumn("IGST AMT");
            DataModel.addColumn("COMPOSITION PER");
            DataModel.addColumn("COMPOSITION AMT");
            DataModel.addColumn("RCM PER");
            DataModel.addColumn("RCM AMT");
            DataModel.addColumn("GST Compensation Cess PER");
            DataModel.addColumn("GST Compensation Cess AMT");
            DataModel.addColumn("BILL LENGTH");
            DataModel.addColumn("BILL WIDTH");
            DataModel.addColumn("BILL WEIGHT");
            DataModel.addColumn("BILL SQMTR");
            DataModel.addColumn("BILL GSM");
            DataModel.addColumn("BILL PRODUCT_CODE");
            DataModel.addColumn("BILL STYLE");
            DataModel.addColumn("TENDER WEIGHT");
            DataModel.addColumn("TENDER GSM");
            DataModel.addColumn("Design Master Revision No");
            DataModel.addColumn("Piece Merge");
            DataModel.addColumn("Dummy Piece No");

            DataModel.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel.SetVariable(1, "MACHINE_NO"); //1
            DataModel.SetVariable(2, "POSITION"); //2
            DataModel.SetVariable(3, "POSITION_DESC"); //3
            DataModel.SetVariable(4, "UPN"); //3
            DataModel.SetVariable(5, "PIECE_NO"); //4
            DataModel.SetVariable(6, "LAYER_TYPE"); //4
            DataModel.SetVariable(7, "PRODUCT_CODE"); //5
            DataModel.SetVariable(8, "DESCRIPTION"); //6
            DataModel.SetVariable(9, "GROUP"); //7
            DataModel.SetVariable(10, "LENGTH"); //8
            DataModel.SetVariable(11, "WIDTH"); //9
            DataModel.SetVariable(12, "GSM"); //10
            DataModel.SetVariable(13, "THORITICAL_WIDTH"); //11
            DataModel.SetVariable(14, "SQ_MTR"); //12
            DataModel.SetVariable(15, "STYLE"); //13
            DataModel.SetVariable(16, "REQ_MONTH"); //14
            DataModel.SetVariable(17, "SYN_PER"); //15
            DataModel.SetVariable(18, "REMARK"); //16
            DataModel.SetVariable(19, "OV_RATE"); //17
            DataModel.SetVariable(20, "SURCHARGE_PER"); //17
            DataModel.SetVariable(21, "SURCHARGE_RATE"); //17
            DataModel.SetVariable(22, "SURCHARGE2_PER"); //17
            DataModel.SetVariable(23, "SURCHARGE2_RATE"); //17DataModel.SetVariable(19, "OV_RATE"); //17
            DataModel.SetVariable(24, "GROSS_RATE"); //17
            DataModel.SetVariable(25, "OV_BAS_AMOUNT"); //18
            DataModel.SetVariable(26, "OV_CHEM_TRT_CHG"); //19
            DataModel.SetVariable(27, "OV_SPIRAL_CHG"); //20
            DataModel.SetVariable(28, "OV_PIN_CHG"); //21
            DataModel.SetVariable(29, "OV_SEAM_CHG"); //22
            DataModel.SetVariable(30, "OV_INS_IND"); //23
            DataModel.SetVariable(31, "OV_INS_AMT"); //24
            DataModel.SetVariable(32, "OV_EXCISE"); //25
            DataModel.SetVariable(33, "OV_DISC_PER"); //26
            DataModel.SetVariable(34, "OV_DISC_AMT"); //27
            DataModel.SetVariable(35, "OV_DISC_BASAMT"); //28
            DataModel.SetVariable(36, "OV_AMT"); //29
            DataModel.SetVariable(37, "VAT1"); //30
            DataModel.SetVariable(38, "VAT4"); //31
            DataModel.SetVariable(39, "CST2"); //32
            DataModel.SetVariable(40, "CST5"); //33
            DataModel.SetVariable(41, "CGST_PER"); //34
            DataModel.SetVariable(42, "CGST_AMT"); //35
            DataModel.SetVariable(43, "SGST_PER"); //36
            DataModel.SetVariable(44, "SGST_AMT"); //37
            DataModel.SetVariable(45, "IGST_PER"); //38
            DataModel.SetVariable(46, "IGST_AMT"); //39
            DataModel.SetVariable(47, "COMPOSITION_PER"); //40
            DataModel.SetVariable(48, "COMPOSITION_AMT"); //41
            DataModel.SetVariable(49, "RCM_PER"); //42
            DataModel.SetVariable(50, "RCM_AMT"); //43
            DataModel.SetVariable(51, "GST_COMPENSATION_CESS_PER"); //44
            DataModel.SetVariable(52, "GST_COMPENSATION_CESS_AMT"); //45
            DataModel.SetVariable(53, "PR_BILL_LENGTH");
            DataModel.SetVariable(54, "PR_BILL_WIDTH");
            DataModel.SetVariable(55, "PR_BILL_WEIGHT");
            DataModel.SetVariable(56, "PR_BILL_SQMTR");
            DataModel.SetVariable(57, "PR_BILL_GSM");
            DataModel.SetVariable(58, "PR_BILL_PRODUCT_CODE");
            DataModel.SetVariable(59, "PR_BILL_STYLE");
            DataModel.SetVariable(60, "TENDER_WEIGHT");
            DataModel.SetVariable(61, "TENDER_GSM");
            DataModel.SetVariable(62, "DM_REVISION_NO");
            DataModel.SetVariable(63, "PIECE_MERGE");
            DataModel.SetVariable(64, "DUMMY_REF_NO");

            if (EditMode == EITLERPGLOBAL.ADD || (EditMode == EITLERPGLOBAL.EDIT && clsFeltProductionApprovalFlow.IsCreator(602, S_O_NO.getText()))) {

            } else {
                
                for(int i=0;i<64;i++)
                {
                   if (EditMode != EITLERPGLOBAL.ADD || EditMode != EITLERPGLOBAL.EDIT)
                   {
                        //DataModel.SetReadOnly(i);
                   }
                }
            }
            
            Table.getColumnModel().getColumn(0).setMinWidth(20);
            Table.getColumnModel().getColumn(1).setMinWidth(90);
            Table.getColumnModel().getColumn(2).setMinWidth(70);
            Table.getColumnModel().getColumn(3).setMinWidth(120);
            Table.getColumnModel().getColumn(4).setMinWidth(120);
            Table.getColumnModel().getColumn(5).setMinWidth(70);
            Table.getColumnModel().getColumn(6).setMinWidth(120);
            Table.getColumnModel().getColumn(7).setMinWidth(100);
            Table.getColumnModel().getColumn(8).setMinWidth(120);
            Table.getColumnModel().getColumn(9).setMinWidth(80);
            Table.getColumnModel().getColumn(10).setMinWidth(70);
            Table.getColumnModel().getColumn(11).setMinWidth(70);
            Table.getColumnModel().getColumn(12).setMinWidth(50);
            Table.getColumnModel().getColumn(13).setMinWidth(130);
            Table.getColumnModel().getColumn(14).setMinWidth(80);
            Table.getColumnModel().getColumn(15).setMinWidth(80);
            Table.getColumnModel().getColumn(16).setMinWidth(100);
            //Table.getColumnModel().getColumn(16).setMinWidth(100);
            Table.getColumnModel().getColumn(17).setMinWidth(100);
            Table.getColumnModel().getColumn(18).setMinWidth(100);
            Table.getColumnModel().getColumn(19).setMinWidth(100);
            Table.getColumnModel().getColumn(20).setMinWidth(150);
            Table.getColumnModel().getColumn(21).setMinWidth(150);

            Table.getColumnModel().getColumn(22).setMinWidth(0);
            Table.getColumnModel().getColumn(22).setMaxWidth(0);

            Table.getColumnModel().getColumn(23).setMinWidth(0);
            Table.getColumnModel().getColumn(23).setMaxWidth(0);

            Table.getColumnModel().getColumn(24).setMinWidth(150);

            Table.getColumnModel().getColumn(25).setMinWidth(150);

            Table.getColumnModel().getColumn(26).setMinWidth(0);
            Table.getColumnModel().getColumn(26).setMaxWidth(0);
            Table.getColumnModel().getColumn(27).setMinWidth(0);
            Table.getColumnModel().getColumn(27).setMaxWidth(0);
            Table.getColumnModel().getColumn(28).setMinWidth(0);
            Table.getColumnModel().getColumn(28).setMaxWidth(0);

            Table.getColumnModel().getColumn(29).setMinWidth(100);

            Table.getColumnModel().getColumn(30).setMinWidth(0);
            Table.getColumnModel().getColumn(30).setMaxWidth(0);

            Table.getColumnModel().getColumn(31).setMinWidth(100);

            Table.getColumnModel().getColumn(32).setMinWidth(0);
            Table.getColumnModel().getColumn(32).setMaxWidth(0);

            Table.getColumnModel().getColumn(33).setMinWidth(100);
            Table.getColumnModel().getColumn(33).setMinWidth(100);

            Table.getColumnModel().getColumn(35).setMinWidth(0);
            Table.getColumnModel().getColumn(35).setMaxWidth(0);

            Table.getColumnModel().getColumn(36).setMinWidth(120);

            Table.getColumnModel().getColumn(37).setMinWidth(0);
            Table.getColumnModel().getColumn(37).setMaxWidth(0);
            Table.getColumnModel().getColumn(38).setMinWidth(0);
            Table.getColumnModel().getColumn(38).setMaxWidth(0);
            Table.getColumnModel().getColumn(39).setMinWidth(0);
            Table.getColumnModel().getColumn(39).setMaxWidth(0);
            Table.getColumnModel().getColumn(40).setMinWidth(0);
            Table.getColumnModel().getColumn(40).setMaxWidth(0);

            Table.getColumnModel().getColumn(41).setMinWidth(100);
            Table.getColumnModel().getColumn(42).setMinWidth(100);
            Table.getColumnModel().getColumn(43).setMinWidth(100);
            Table.getColumnModel().getColumn(44).setMinWidth(100);
            Table.getColumnModel().getColumn(45).setMinWidth(100);
            Table.getColumnModel().getColumn(46).setMinWidth(100);
            Table.getColumnModel().getColumn(47).setMinWidth(100);
            Table.getColumnModel().getColumn(48).setMinWidth(100);
            Table.getColumnModel().getColumn(49).setMinWidth(100);
            Table.getColumnModel().getColumn(50).setMinWidth(100);
            Table.getColumnModel().getColumn(51).setMinWidth(100);
            Table.getColumnModel().getColumn(52).setMinWidth(100);

            Table.getColumnModel().getColumn(53).setMinWidth(100);
            Table.getColumnModel().getColumn(54).setMinWidth(100);
            Table.getColumnModel().getColumn(55).setMinWidth(100);
            Table.getColumnModel().getColumn(56).setMinWidth(100);
            Table.getColumnModel().getColumn(57).setMinWidth(100);
            Table.getColumnModel().getColumn(58).setMinWidth(100);
            Table.getColumnModel().getColumn(59).setMinWidth(100);
            Table.getColumnModel().getColumn(60).setMinWidth(120);
            Table.getColumnModel().getColumn(61).setMinWidth(120);
            Table.getColumnModel().getColumn(62).setMinWidth(180);
            Table.getColumnModel().getColumn(63).setMinWidth(100);
            Table.getColumnModel().getColumn(64).setMinWidth(120);

//            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            // Table.getColumn(DesignMasterColumn).setCellRenderer(new ButtonRenderer());
            // Table.getColumn(DesignMasterColumn).setCellEditor(new ButtonEditor(new JCheckBox()));
            int ImportCol2 = 63;
            Renderer1.setCustomComponent(ImportCol2, "CheckBox");
            JCheckBox aCheckBox2 = new JCheckBox();
            aCheckBox2.setBackground(Color.WHITE);
            aCheckBox2.setVisible(true);

            //For Felt Design
            if (clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) == 39) {
                aCheckBox2.setEnabled(true);
            } else {
                aCheckBox2.setEnabled(false);
            }

            aCheckBox2.setSelected(false);
            Table.getColumnModel().getColumn(ImportCol2).setCellEditor(new DefaultCellEditor(aCheckBox2));
            Table.getColumnModel().getColumn(ImportCol2).setCellRenderer(Renderer1);

            TableColumn layerColumn = Table.getColumnModel().getColumn(6);
            JComboBox layerbox = new JComboBox();
            layerbox.addItem("WITHOUT_AB");
            layerbox.addItem("WITH_AB");
            layerColumn.setCellEditor(new DefaultCellEditor(layerbox));

            TableColumn dateColumn = Table.getColumnModel().getColumn(16);
            JComboBox monthbox = new JComboBox();
            /*monthbox.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent ae) {
             String Req_Month = DataModel.getValueByVariable("REQ_MONTH", Table.getSelectedRow());
             String slot_2 = "";
             if(Req_Month.contains("Jan"))
             {
             slot_2 = "15-31";
             }
             else if(Req_Month.contains("Feb"))
             {
             slot_2 = "15-30";
             }
             else if(Req_Month.contains("Mar"))
             {
             slot_2 = "15-31";
             }
             else if(Req_Month.contains("Apr"))
             {
             slot_2 = "15-30";
             }
             else if(Req_Month.contains("May"))
             {
             slot_2 = "15-31";
             }
             else if(Req_Month.contains("Jun"))
             {
             slot_2 = "15-30";
             }
             else if(Req_Month.contains("Jul"))
             {
             slot_2 = "15-31";
             }
             else if(Req_Month.contains("Aug"))
             {
             slot_2 = "15-31";
             }
             else if(Req_Month.contains("Sep"))
             {
             slot_2 = "15-30";
             }
             else if(Req_Month.contains("Oct"))
             {
             slot_2 = "15-31";
             }
             else if(Req_Month.contains("Nov"))
             {
             slot_2 = "15-30";
             }
             else if(Req_Month.contains("Dec"))
             {
             slot_2 = "15-31";
             }
             TableColumn dayColumn = Table.getColumnModel().getColumn(16);
             JComboBox monthbox1 = new JComboBox();
             monthbox1.addItem("01-15");
             monthbox1.addItem(slot_2);
             dayColumn.setCellEditor(new DefaultCellEditor(monthbox1));
                    
             }
             });*/

            String month_name = "";
            Date date = new Date();
            int month;
            int year = date.getYear() + 1900;

//            if(date.getDate() <= 15)
//            {
//                month = date.getMonth()+2;
//            }
//            else
//            {
            month = date.getMonth() + 1;
            //04-12-2021
            if (EITLERPGLOBAL.getCurrentDay() <= 10) {
                month = date.getMonth();
            }
//            }

//            for (int i = 0; i < 12; i++) {
//                month = month + 1;
//
//                if (month >= 13) {
//                    month = 1;
//                    year = year + 1;
//                }
//
//                if (month == 1) {
//                    month_name = "Jan";
//                } else if (month == 2) {
//                    month_name = "Feb";
//                } else if (month == 3) {
//                    month_name = "Mar";
//                } else if (month == 4) {
//                    month_name = "Apr";
//                } else if (month == 5) {
//                    month_name = "May";
//                } else if (month == 6) {
//                    month_name = "Jun";
//                } else if (month == 7) {
//                    month_name = "Jul";
//                } else if (month == 8) {
//                    month_name = "Aug";
//                } else if (month == 9) {
//                    month_name = "Sep";
//                } else if (month == 10) {
//                    month_name = "Oct";
//                } else if (month == 11) {
//                    month_name = "Nov";
//                } else if (month == 12) {
//                    month_name = "Dec";
//                }
//                monthbox.addItem(month_name + " - " + year);
//            }

            monthbox.addItem("Mar - 2022");
            
            dateColumn.setCellEditor(new DefaultCellEditor(monthbox));

            //dateColumn.setCellEditor(new DatePi);
            //Added PO Attachment
            DataModelDoc = new EITLTableModel();
            DocTable.removeAll();

            DocTable.setModel(DataModelDoc);
            DocTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            //Add Columns to it
            DataModelDoc.addColumn("Sr.No.");   //0
            DataModelDoc.addColumn("Remark");   //1     
            DataModelDoc.addColumn("PO Number");   //2
            DataModelDoc.addColumn("Upload");   //3
            DataModelDoc.addColumn("View");   //4
            DataModelDoc.addColumn("Document");   //5

            int ImportCol = 4;
            DocTable.getColumnModel().getColumn(ImportCol).setCellEditor(new ButtonEditor(new JCheckBox()));
            DocTable.getColumnModel().getColumn(ImportCol).setCellRenderer(new ButtonRenderer());
            ImportCol = 3;
            DocTable.getColumnModel().getColumn(ImportCol).setCellEditor(new ButtonEditor(new JCheckBox()));
            DocTable.getColumnModel().getColumn(ImportCol).setCellRenderer(new ButtonRenderer());

            DataModelDoc.SetReadOnly(0);
            DataModelDoc.SetReadOnly(2);
            DataModelDoc.SetReadOnly(5);

            if (EditMode == EITLERPGLOBAL.ADD || (EditMode == EITLERPGLOBAL.EDIT && clsFeltProductionApprovalFlow.IsCreator(602, S_O_NO.getText()))) {

            } else {
                DataModelDoc.SetReadOnly(1);
                DataModelDoc.SetReadOnly(3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGrid_prevoiusData() {
        try {
            DataModel_previousData = new EITLTableModel();
            Table_prevoiusData.removeAll();

            Table_prevoiusData.setModel(DataModel_previousData);
            Table_prevoiusData.setAutoResizeMode(0);

            DataModel_previousData.addColumn("SrNo"); //0 - Read Only
            DataModel_previousData.addColumn("PR_PIECE_NO"); //1
            DataModel_previousData.addColumn("PR_MACHINE_NO"); //2
            DataModel_previousData.addColumn("PR_POSITION_NO"); //3
            DataModel_previousData.addColumn("POSITION_DESC"); //4
            DataModel_previousData.addColumn("PR_UPN"); //11
            DataModel_previousData.addColumn("POSITION_DESIGN_NO"); //12
            DataModel_previousData.addColumn("PR_PARTY_CODE"); //5
            DataModel_previousData.addColumn("PARTY_NAME"); //6
            DataModel_previousData.addColumn("PR_PRODUCT_CODE"); //7
            DataModel_previousData.addColumn("PR_GROUP"); //8
            DataModel_previousData.addColumn("PR_PIECE_STAGE"); //9
            DataModel_previousData.addColumn("PR_WIP_STATUS"); //10

            DataModel_previousData.addColumn("BILL_LENGTH"); //12
            DataModel_previousData.addColumn("BILL_WIDTH"); //12
            DataModel_previousData.addColumn("BILL_WEIGHT"); //12
            DataModel_previousData.addColumn("BILL_SQMTR"); //12
            DataModel_previousData.addColumn("BILL_GSM"); //12
            DataModel_previousData.addColumn("BILL_PRODUCT_CODE"); //12
            DataModel_previousData.addColumn("BILL_STYLE"); //12

            DataModel_previousData.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_previousData.SetVariable(1, "PR_PIECE_NO"); //1
            DataModel_previousData.SetVariable(2, "PR_MACHINE_NO"); //1
            DataModel_previousData.SetVariable(3, "PR_POSITION_NO"); //1
            DataModel_previousData.SetVariable(4, "POSITION_DESC"); //1
            DataModel_previousData.SetVariable(5, "PR_UPN"); //1
            DataModel_previousData.SetVariable(6, "POSITION_DESIGN_NO"); //2
            DataModel_previousData.SetVariable(7, "PR_PARTY_CODE"); //1
            DataModel_previousData.SetVariable(8, "PARTY_NAME"); //1
            DataModel_previousData.SetVariable(9, "PR_PRODUCT_CODE"); //1
            DataModel_previousData.SetVariable(10, "PR_GROUP"); //1
            DataModel_previousData.SetVariable(11, "PR_PIECE_STAGE"); //1
            DataModel_previousData.SetVariable(12, "PR_WIP_STATUS"); //1

            DataModel_previousData.SetVariable(13, "PR_BILL_LENGTH"); //2
            DataModel_previousData.SetVariable(14, "PR_BILL_WIDTH"); //2
            DataModel_previousData.SetVariable(15, "PR_BILL_WEIGHT"); //2
            DataModel_previousData.SetVariable(16, "PR_BILL_SQMTR"); //2
            DataModel_previousData.SetVariable(17, "PR_BILL_GSM"); //2
            DataModel_previousData.SetVariable(18, "PR_BILL_PRODUCT_CODE"); //2
            DataModel_previousData.SetVariable(19, "PR_BILL_STYLE"); //2

            //dateColumn.setCellEditor(new DatePi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGrid_prevoiusInvoice() {
        try {
            DataModel_previousInvoice = new EITLTableModel();
            tblPreviousInvoice.removeAll();

            tblPreviousInvoice.setModel(DataModel_previousInvoice);
            tblPreviousInvoice.setAutoResizeMode(0);

            DataModel_previousInvoice.addColumn("SrNo"); //0 - Read Only
            DataModel_previousInvoice.addColumn("PIECE_NO"); //1
            DataModel_previousInvoice.addColumn("UPN"); //2
            DataModel_previousInvoice.addColumn("GROUP"); //3
            DataModel_previousInvoice.addColumn("OC_MONTH"); //4
            DataModel_previousInvoice.addColumn("WAREHOUSE_DATE"); //11
            DataModel_previousInvoice.addColumn("DISPATCHED_DATE"); //12
            DataModel_previousInvoice.addColumn("PR_OC_LAST_DDMMYY"); //5

            DataModel_previousInvoice.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_previousInvoice.SetVariable(1, "PIECE_NO"); //1
            DataModel_previousInvoice.SetVariable(2, "UPN"); //1
            DataModel_previousInvoice.SetVariable(3, "GROUP"); //1
            DataModel_previousInvoice.SetVariable(4, "OC_MONTH"); //1
            DataModel_previousInvoice.SetVariable(5, "WAREHOUSE_DATE"); //1
            DataModel_previousInvoice.SetVariable(6, "DISPATCHED_DATE"); //2
            DataModel_previousInvoice.SetVariable(7, "PR_OC_LAST_DDMMYY"); //1

            tblPreviousInvoice.getColumnModel().getColumn(0).setMinWidth(80);
            tblPreviousInvoice.getColumnModel().getColumn(1).setMinWidth(120);
            tblPreviousInvoice.getColumnModel().getColumn(2).setMinWidth(120);
            tblPreviousInvoice.getColumnModel().getColumn(3).setMinWidth(120);
            tblPreviousInvoice.getColumnModel().getColumn(4).setMinWidth(120);
            tblPreviousInvoice.getColumnModel().getColumn(5).setMinWidth(120);
            tblPreviousInvoice.getColumnModel().getColumn(6).setMinWidth(120);
            tblPreviousInvoice.getColumnModel().getColumn(7).setMinWidth(0);
            tblPreviousInvoice.getColumnModel().getColumn(7).setMaxWidth(0);

            tblPreviousInvoice.getColumnModel().getColumn(0).setCellRenderer(render_invoice);
            tblPreviousInvoice.getColumnModel().getColumn(1).setCellRenderer(render_invoice);
            tblPreviousInvoice.getColumnModel().getColumn(2).setCellRenderer(render_invoice);
            tblPreviousInvoice.getColumnModel().getColumn(3).setCellRenderer(render_invoice);
            tblPreviousInvoice.getColumnModel().getColumn(4).setCellRenderer(render_invoice);
            tblPreviousInvoice.getColumnModel().getColumn(5).setCellRenderer(render_invoice);
            tblPreviousInvoice.getColumnModel().getColumn(6).setCellRenderer(render_invoice);

            //dateColumn.setCellEditor(new DatePi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPrevoiousData() {
        try {
            FormatGrid_prevoiusData();

            String str_query = "SELECT "
                    + " PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,POSITION_DESC, PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_PIECE_STAGE, "
                    + " PR_WIP_STATUS,PR_UPN,POSITION_DESIGN_NO,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_GSM,PR_BILL_PRODUCT_CODE,PR_BILL_STYLE "
                    + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR,PRODUCTION.FELT_MACHINE_POSITION_MST PS "
                    + " WHERE PS.POSITION_NO = PR.PR_POSITION_NO   AND PR_PARTY_CODE='" + PARTY_CODE.getText() + "' AND  PR_PIECE_STAGE IN ('PLANNING','SPLICING','SEAMING','WEAVING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK','BSR') limit 10";

            lblParty.setText(PARTY_CODE.getText());
            lblPartyName.setText(clsSales_Party.getPartyName(2, PARTY_CODE.getText()));

            if (!txtMachineNo.getText().equals("")) {
                str_query = str_query + " AND PR_MACHINE_NO=" + txtMachineNo.getText() + " ";
            }

            if (!txtPosition.getText().equals("")) {
                str_query = str_query + " AND PR_POSITION_NO=" + txtPosition.getText() + " ";
            }

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_previousData.addRow(rowData);

                DataModel_previousData.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_previousData.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_previousData.setValueByVariable("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"), NewRow);
                DataModel_previousData.setValueByVariable("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"), NewRow);
                DataModel_previousData.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);

                DataModel_previousData.setValueByVariable("PR_UPN", resultSet.getString("PR_UPN"), NewRow);
                DataModel_previousData.setValueByVariable("POSITION_DESIGN_NO", resultSet.getString("POSITION_DESIGN_NO"), NewRow);
                DataModel_previousData.setValueByVariable("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                DataModel_previousData.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, resultSet.getString("PR_PARTY_CODE")), NewRow);
                DataModel_previousData.setValueByVariable("PR_PRODUCT_CODE", resultSet.getString("PR_PRODUCT_CODE"), NewRow);
                DataModel_previousData.setValueByVariable("PR_GROUP", resultSet.getString("PR_GROUP"), NewRow);
                DataModel_previousData.setValueByVariable("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"), NewRow);
                DataModel_previousData.setValueByVariable("PR_WIP_STATUS", resultSet.getString("PR_WIP_STATUS"), NewRow);

                DataModel_previousData.setValueByVariable("PR_BILL_LENGTH", resultSet.getString("PR_BILL_LENGTH"), NewRow);
                DataModel_previousData.setValueByVariable("PR_BILL_WIDTH", resultSet.getString("PR_BILL_WIDTH"), NewRow);
                DataModel_previousData.setValueByVariable("PR_BILL_WEIGHT", resultSet.getString("PR_BILL_WEIGHT"), NewRow);
                DataModel_previousData.setValueByVariable("PR_BILL_SQMTR", resultSet.getString("PR_BILL_SQMTR"), NewRow);
                DataModel_previousData.setValueByVariable("PR_BILL_GSM", resultSet.getString("PR_BILL_GSM"), NewRow);
                DataModel_previousData.setValueByVariable("PR_BILL_PRODUCT_CODE", resultSet.getString("PR_BILL_PRODUCT_CODE"), NewRow);
                //PR_BILL_STYLE
                DataModel_previousData.setValueByVariable("PR_BILL_STYLE", resultSet.getString("PR_BILL_STYLE"), NewRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPreviousInvoice() {
        ////SELECT PR_PIECE_NO AS 'PIECE_NO',PR_UPN AS 'UPN',PR_GROUP AS 'GROUP',PR_OC_MONTHYEAR AS 'OC_MONTH',PR_FNSG_DATE AS 'WAREHOUSE_DATE',PR_INVOICE_DATE  AS 'DISPATCHED_DATE',PR_OC_LAST_DDMMYY FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='"+PARTY_CODE+"' AND PR_PIECE_STAGE='INVOICED' ORDER BY PR_INVOICE_DATE DESC LIMIT 10

        try {
            FormatGrid_prevoiusInvoice();

            String str_query = "SELECT PR_PIECE_NO AS 'PIECE_NO',PR_UPN AS 'UPN',PR_GROUP AS 'GROUP',PR_OC_MONTHYEAR AS 'OC_MONTH',PR_FNSG_DATE AS 'WAREHOUSE_DATE',PR_INVOICE_DATE  AS 'DISPATCHED_DATE',PR_OC_LAST_DDMMYY FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PARTY_CODE.getText() + "' AND PR_PIECE_STAGE='INVOICED' ORDER BY PR_INVOICE_DATE DESC LIMIT 10";

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_previousInvoice.addRow(rowData);

                DataModel_previousInvoice.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_previousInvoice.setValueByVariable("PIECE_NO", resultSet.getString("PIECE_NO"), NewRow);
                DataModel_previousInvoice.setValueByVariable("UPN", resultSet.getString("UPN"), NewRow);
                DataModel_previousInvoice.setValueByVariable("GROUP", resultSet.getString("GROUP"), NewRow);
                DataModel_previousInvoice.setValueByVariable("OC_MONTH", resultSet.getString("OC_MONTH"), NewRow);

                DataModel_previousInvoice.setValueByVariable("WAREHOUSE_DATE", resultSet.getString("WAREHOUSE_DATE"), NewRow);
                DataModel_previousInvoice.setValueByVariable("DISPATCHED_DATE", resultSet.getString("DISPATCHED_DATE"), NewRow);
                DataModel_previousInvoice.setValueByVariable("PR_OC_LAST_DDMMYY", resultSet.getString("PR_OC_LAST_DDMMYY"), NewRow);

                String OC_MONTH_DDMMYY = resultSet.getString("PR_OC_LAST_DDMMYY");
                String FNSG_DATE = resultSet.getString("WAREHOUSE_DATE");
                String DISPATCHED_DATE = resultSet.getString("DISPATCHED_DATE");
                System.out.println("OCMONTHDDMMYY = " + OC_MONTH_DDMMYY + " FNSG_DATE = " + FNSG_DATE);
                java.util.Date date_oc_month = new SimpleDateFormat("yyyy-MM-dd").parse(OC_MONTH_DDMMYY);
                java.util.Date date_fnsg = new SimpleDateFormat("yyyy-MM-dd").parse(FNSG_DATE);
                java.util.Date date_dispatch = new SimpleDateFormat("yyyy-MM-dd").parse(DISPATCHED_DATE);
                //date_fnsg.setMonth(date_fnsg.getMonth()-1);
                System.out.println("ocmonth date " + date_oc_month + ", fnsg date : " + date_fnsg);

                java.util.Date lastdate_oc_month = null;
                java.util.Date lastdate_fnsg = null;
                java.util.Date lastdate_dispatch = null;

                try {
                    int month = date_oc_month.getMonth() + 1;
                    int year = date_oc_month.getYear() + 1900;

                    String OC_MTH = getMonthName(month) + " - " + year;
                    lastdate_oc_month = LastDayOfReqMonth(OC_MTH);

                    month = date_fnsg.getMonth() + 1;
                    year = date_fnsg.getYear() + 1900;

                    String OC_FNSG = getMonthName(month) + " - " + year;
                    lastdate_fnsg = LastDayOfReqMonth(OC_FNSG);

                    month = date_dispatch.getMonth() + 1;
                    year = date_dispatch.getYear() + 1900;

                    String DISPATCH_MTH = getMonthName(month) + " - " + year;
                    lastdate_dispatch = LastDayOfReqMonth(DISPATCH_MTH);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!"0000-00-00".equals(OC_MONTH_DDMMYY)) {

                    if (lastdate_oc_month.before(lastdate_fnsg)) {
                        //PINK
                        render_invoice.setBackColor(NewRow, 0, new Color(237, 165, 181));
                        render_invoice.setBackColor(NewRow, 1, new Color(237, 165, 181));
                        render_invoice.setBackColor(NewRow, 2, new Color(237, 165, 181));
                        render_invoice.setBackColor(NewRow, 3, new Color(237, 165, 181));
                        render_invoice.setBackColor(NewRow, 4, new Color(237, 165, 181));
                        render_invoice.setBackColor(NewRow, 5, new Color(237, 165, 181));
                        render_invoice.setBackColor(NewRow, 6, new Color(237, 165, 181));
                    } else if (lastdate_oc_month.before(lastdate_dispatch)) {
                        //Blue
                        render_invoice.setBackColor(NewRow, 0, new Color(114, 144, 223));
                        render_invoice.setBackColor(NewRow, 1, new Color(114, 144, 223));
                        render_invoice.setBackColor(NewRow, 2, new Color(114, 144, 223));
                        render_invoice.setBackColor(NewRow, 3, new Color(114, 144, 223));
                        render_invoice.setBackColor(NewRow, 4, new Color(114, 144, 223));
                        render_invoice.setBackColor(NewRow, 5, new Color(114, 144, 223));
                        render_invoice.setBackColor(NewRow, 6, new Color(114, 144, 223));
                    } else {
                        //GREEN
                        render_invoice.setBackColor(NewRow, 0, new Color(112, 192, 127));
                        render_invoice.setBackColor(NewRow, 1, new Color(112, 192, 127));
                        render_invoice.setBackColor(NewRow, 2, new Color(112, 192, 127));
                        render_invoice.setBackColor(NewRow, 3, new Color(112, 192, 127));
                        render_invoice.setBackColor(NewRow, 4, new Color(112, 192, 127));
                        render_invoice.setBackColor(NewRow, 5, new Color(112, 192, 127));
                        render_invoice.setBackColor(NewRow, 6, new Color(112, 192, 127));
                    }
                } else {
                    //WHITE
                    render_invoice.setBackColor(NewRow, 0, new Color(213, 210, 211));
                    render_invoice.setBackColor(NewRow, 1, new Color(213, 210, 211));
                    render_invoice.setBackColor(NewRow, 2, new Color(213, 210, 211));
                    render_invoice.setBackColor(NewRow, 3, new Color(213, 210, 211));
                    render_invoice.setBackColor(NewRow, 4, new Color(213, 210, 211));
                    render_invoice.setBackColor(NewRow, 5, new Color(213, 210, 211));
                    render_invoice.setBackColor(NewRow, 6, new Color(213, 210, 211));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMonthName(int month) {
        if (month == 1) {
            return "Jan";
        } else if (month == 2) {
            return "Feb";
        } else if (month == 3) {
            return "Mar";
        } else if (month == 4) {
            return "Apr";
        } else if (month == 5) {
            return "May";
        } else if (month == 6) {
            return "Jun";
        } else if (month == 7) {
            return "Jul";
        } else if (month == 8) {
            return "Aug";
        } else if (month == 9) {
            return "Sep";
        } else if (month == 10) {
            return "Oct";
        } else if (month == 11) {
            return "Nov";
        } else if (month == 12) {
            return "Dec";
        } else {
            return "";
        }
    }

    private Date LastDayOfReqMonth(String Req_Month) {
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
        return cal.getTime();

    }

//    private void FormatGrid_OldTransaction() {
//        try {
//            DataModelOldTransaction = new EITLTableModel();
//            Table1.removeAll();
//
//            Table1.setModel(DataModelOldTransaction);
//            Table1.setAutoResizeMode(0);
//
//            DataModelOldTransaction.addColumn("SrNo"); //0 - Read Only
//            DataModelOldTransaction.addColumn("ORDER NO"); //1
//            DataModelOldTransaction.addColumn("ORDER DATE"); //1
//            DataModelOldTransaction.addColumn("ORDER BY"); //1
//            DataModelOldTransaction.addColumn("PARTY CODE"); //1
//            DataModelOldTransaction.addColumn("PARTY NAME"); //1
//            DataModelOldTransaction.addColumn("SALES ENGINEER"); //1
//            DataModelOldTransaction.addColumn("REFERENCE"); //1
//            DataModelOldTransaction.addColumn("REFERENCE DATE"); //1
//            DataModelOldTransaction.addColumn("PO NO"); //1
//            DataModelOldTransaction.addColumn("PO DATE"); //1
//            DataModelOldTransaction.addColumn("MACHINE NO"); //1
//            DataModelOldTransaction.addColumn("POSITION"); //2
//            DataModelOldTransaction.addColumn("POSITION DESC"); //3
//            DataModelOldTransaction.addColumn("PIECE NO"); //4
//            DataModelOldTransaction.addColumn("PRODUCT_CODE"); //5
//            DataModelOldTransaction.addColumn("DESCRIPTION"); //6
//            DataModelOldTransaction.addColumn("GROUP"); //7
//            DataModelOldTransaction.addColumn("LENGTH"); //8
//            DataModelOldTransaction.addColumn("WIDTH"); //9
//            DataModelOldTransaction.addColumn("GSM"); //10
//            DataModelOldTransaction.addColumn("THORTICAL WEIGHT"); //11
//            DataModelOldTransaction.addColumn("SQ MT"); //12
//            DataModelOldTransaction.addColumn("STYLE"); //13
//            DataModelOldTransaction.addColumn("REQ MONTH"); //14
//            DataModelOldTransaction.addColumn("SYN(%)"); //15
//            DataModelOldTransaction.addColumn("REMARK"); //16
//            DataModelOldTransaction.addColumn("OV_RATE"); //17
//            DataModelOldTransaction.addColumn("OV_BAS_AMOUNT"); //18
//            DataModelOldTransaction.addColumn("OV_CHEM_TRT_CHG"); //19
//            DataModelOldTransaction.addColumn("OV_SPIRAL_CHG"); //20
//            DataModelOldTransaction.addColumn("OV_PIN_CHG"); //21
//            DataModelOldTransaction.addColumn("OV_SEAM_CHG"); //22
//            DataModelOldTransaction.addColumn("OV_INS_IND"); //23
//            DataModelOldTransaction.addColumn("OV_INS_AMT"); //24
//            DataModelOldTransaction.addColumn("OV_EXCISE"); //25
//            DataModelOldTransaction.addColumn("OV_DISC_PER"); //26
//            DataModelOldTransaction.addColumn("OV_DISC_AMT"); //27
//            DataModelOldTransaction.addColumn("OV_DISC_BASAMT"); //28
//            DataModelOldTransaction.addColumn("OV_AMT"); //29
//            DataModelOldTransaction.addColumn("VAT1"); //30
//            DataModelOldTransaction.addColumn("VAT4"); //31
//            DataModelOldTransaction.addColumn("CST2"); //32
//            DataModelOldTransaction.addColumn("CST5"); //33
//            DataModelOldTransaction.addColumn("CGST PER");
//            DataModelOldTransaction.addColumn("CGST AMT");
//            DataModelOldTransaction.addColumn("SGST PER");
//            DataModelOldTransaction.addColumn("SGST AMT");
//            DataModelOldTransaction.addColumn("IGST PER");
//            DataModelOldTransaction.addColumn("IGST AMT");
//            DataModelOldTransaction.addColumn("COMPOSITION PER");
//            DataModelOldTransaction.addColumn("COMPOSITION AMT");
//            DataModelOldTransaction.addColumn("RCM PER");
//            DataModelOldTransaction.addColumn("RCM AMT");
//            DataModelOldTransaction.addColumn("GST Compensation Cess PER");
//            DataModelOldTransaction.addColumn("GST Compensation Cess AMT");
//
//            DataModelOldTransaction.SetVariable(0, "SrNo"); //0 - Read Only
//            DataModelOldTransaction.SetVariable(1, "S_ORDER_NO"); //1
//            DataModelOldTransaction.SetVariable(2, "S_ORDER_DATE"); //1
//            DataModelOldTransaction.SetVariable(3, "ORDER_BY"); //1
//            DataModelOldTransaction.SetVariable(4, "PARTY_CODE"); //1
//            DataModelOldTransaction.SetVariable(5, "PARTY_NAME"); //1
//            DataModelOldTransaction.SetVariable(6, "SALES_ENGINEER"); //1
//            DataModelOldTransaction.SetVariable(7, "REFERENCE"); //1
//            DataModelOldTransaction.SetVariable(8, "REFERENCE_DATE"); //1
//            DataModelOldTransaction.SetVariable(9, "P_O_NO"); //1
//            DataModelOldTransaction.SetVariable(10, "P_O_DATE"); //1
//            DataModelOldTransaction.SetVariable(11, "MACHINE_NO"); //1
//            DataModelOldTransaction.SetVariable(12, "POSITION"); //2
//            DataModelOldTransaction.SetVariable(13, "POSITION_DESC"); //3
//            DataModelOldTransaction.SetVariable(14, "PIECE_NO"); //4
//            DataModelOldTransaction.SetVariable(15, "PRODUCT_CODE"); //5
//            DataModelOldTransaction.SetVariable(16, "DESCRIPTION"); //6
//            DataModelOldTransaction.SetVariable(17, "GROUP"); //7
//            DataModelOldTransaction.SetVariable(18, "LENGTH"); //8
//            DataModelOldTransaction.SetVariable(19, "WIDTH"); //9
//            DataModelOldTransaction.SetVariable(20, "GSM"); //10
//            DataModelOldTransaction.SetVariable(21, "THORITICAL_WIDTH"); //11
//            DataModelOldTransaction.SetVariable(22, "SQ_MTR"); //12
//            DataModelOldTransaction.SetVariable(23, "STYLE"); //13
//            DataModelOldTransaction.SetVariable(24, "REQ_MONTH"); //14
//            DataModelOldTransaction.SetVariable(25, "SYN"); //15
//            DataModelOldTransaction.SetVariable(26, "REMARK"); //16
//            DataModelOldTransaction.SetVariable(27, "OV_RATE"); //17
//            DataModelOldTransaction.SetVariable(28, "OV_BAS_AMOUNT"); //18
//            DataModelOldTransaction.SetVariable(29, "OV_CHEM_TRT_CHG"); //19
//            DataModelOldTransaction.SetVariable(30, "OV_SPIRAL_CHG"); //20
//            DataModelOldTransaction.SetVariable(31, "OV_PIN_CHG"); //21
//            DataModelOldTransaction.SetVariable(32, "OV_SEAM_CHG"); //22
//            DataModelOldTransaction.SetVariable(33, "OV_INS_IND"); //23
//            DataModelOldTransaction.SetVariable(34, "OV_INS_AMT"); //24
//            DataModelOldTransaction.SetVariable(35, "OV_EXCISE"); //25
//            DataModelOldTransaction.SetVariable(36, "OV_DISC_PER"); //26
//            DataModelOldTransaction.SetVariable(37, "OV_DISC_AMT"); //27
//            DataModelOldTransaction.SetVariable(38, "OV_DISC_BASAMT"); //28
//            DataModelOldTransaction.SetVariable(39, "OV_AMT"); //29
//            DataModelOldTransaction.SetVariable(40, "VAT1"); //30
//            DataModelOldTransaction.SetVariable(41, "VAT4"); //31
//            DataModelOldTransaction.SetVariable(42, "CST2"); //32
//            DataModelOldTransaction.SetVariable(43, "CST5"); //33
//            DataModelOldTransaction.SetVariable(44, "CGST_PER"); //34
//            DataModelOldTransaction.SetVariable(45, "CGST_AMT"); //35
//            DataModelOldTransaction.SetVariable(46, "SGST_PER"); //36
//            DataModelOldTransaction.SetVariable(47, "SGST_AMT"); //37
//            DataModelOldTransaction.SetVariable(48, "IGST_PER"); //38
//            DataModelOldTransaction.SetVariable(49, "IGST_AMT"); //39
//            DataModelOldTransaction.SetVariable(50, "COMPOSITION_PER"); //40
//            DataModelOldTransaction.SetVariable(51, "COMPOSITION_AMT"); //41
//            DataModelOldTransaction.SetVariable(52, "RCM_PER"); //42
//            DataModelOldTransaction.SetVariable(53, "RCM_AMT"); //43
//            DataModelOldTransaction.SetVariable(54, "GST_COMPENSATION_CESS_PER"); //44
//            DataModelOldTransaction.SetVariable(55, "GST_COMPENSATION_CESS_AMT"); //45
//
//            Table1.getColumnModel().getColumn(0).setMinWidth(40);
//            Table1.getColumnModel().getColumn(0).setMaxWidth(40);
//            Table1.getColumnModel().getColumn(1).setMinWidth(80);
//            Table1.getColumnModel().getColumn(2).setMinWidth(85);
//            Table1.getColumnModel().getColumn(3).setMinWidth(100);
//            Table1.getColumnModel().getColumn(3).setMinWidth(85);
//            Table1.getColumnModel().getColumn(4).setMinWidth(85);
//            Table1.getColumnModel().getColumn(5).setMinWidth(130);
//            Table1.getColumnModel().getColumn(6).setMinWidth(130);
//            Table1.getColumnModel().getColumn(7).setMinWidth(100);
//            Table1.getColumnModel().getColumn(8).setMinWidth(100);
//            Table1.getColumnModel().getColumn(9).setMinWidth(130);
//            Table1.getColumnModel().getColumn(10).setMinWidth(1300);
//            Table1.getColumnModel().getColumn(11).setMinWidth(130);
//            Table1.getColumnModel().getColumn(12).setMinWidth(150);
//            Table1.getColumnModel().getColumn(13).setMinWidth(90);
//            Table1.getColumnModel().getColumn(14).setMinWidth(100);
//            Table1.getColumnModel().getColumn(15).setMinWidth(100);
//            Table1.getColumnModel().getColumn(16).setMinWidth(100);
//            Table1.getColumnModel().getColumn(17).setMinWidth(100);
//            Table1.getColumnModel().getColumn(18).setMinWidth(0);
//            Table1.getColumnModel().getColumn(18).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(19).setMinWidth(0);
//            Table1.getColumnModel().getColumn(19).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(20).setMinWidth(0);
//            Table1.getColumnModel().getColumn(20).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(21).setMinWidth(0);
//            Table1.getColumnModel().getColumn(21).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(22).setMinWidth(0);
//            Table1.getColumnModel().getColumn(22).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(23).setMinWidth(0);
//            Table1.getColumnModel().getColumn(23).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(24).setMinWidth(0);
//            Table1.getColumnModel().getColumn(24).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(25).setMinWidth(0);
//            Table1.getColumnModel().getColumn(25).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(26).setMinWidth(0);
//            Table1.getColumnModel().getColumn(26).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(27).setMinWidth(0);
//            Table1.getColumnModel().getColumn(27).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(28).setMinWidth(0);
//            Table1.getColumnModel().getColumn(28).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(29).setMinWidth(120);
//            Table1.getColumnModel().getColumn(30).setMinWidth(0);
//            Table1.getColumnModel().getColumn(30).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(31).setMinWidth(0);
//            Table1.getColumnModel().getColumn(31).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(32).setMinWidth(0);
//            Table1.getColumnModel().getColumn(32).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(33).setMinWidth(0);
//            Table1.getColumnModel().getColumn(33).setMaxWidth(0);
//            Table1.getColumnModel().getColumn(34).setMinWidth(100);
//            Table1.getColumnModel().getColumn(34).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(35).setMinWidth(100);
//            Table1.getColumnModel().getColumn(35).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(36).setMinWidth(100);
//            Table1.getColumnModel().getColumn(36).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(37).setMinWidth(100);
//            Table1.getColumnModel().getColumn(37).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(38).setMinWidth(100);
//            Table1.getColumnModel().getColumn(38).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(39).setMinWidth(100);
//            Table1.getColumnModel().getColumn(39).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(40).setMinWidth(100);
//            Table1.getColumnModel().getColumn(40).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(41).setMinWidth(100);
//            Table1.getColumnModel().getColumn(41).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(42).setMinWidth(100);
//            Table1.getColumnModel().getColumn(42).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(43).setMinWidth(100);
//            Table1.getColumnModel().getColumn(43).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(44).setMinWidth(100);
//            Table1.getColumnModel().getColumn(44).setMaxWidth(100);
//            Table1.getColumnModel().getColumn(45).setMinWidth(100);
//            Table1.getColumnModel().getColumn(45).setMaxWidth(100);
//
//            //dateColumn.setCellEditor(new DatePi);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
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
        jLabel5 = new javax.swing.JLabel();
        REMARK = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        S_O_NO = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        PARTY_CODE = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        PARTY_NAME = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        P_O_NO = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        REFERENCE = new javax.swing.JComboBox();
        P_O_DATE = new javax.swing.JFormattedTextField();
        ORDER_VALUE = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        REF_DATE = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        DISTRICT = new javax.swing.JTextField();
        COUNTRY = new javax.swing.JTextField();
        REGION = new javax.swing.JTextField();
        S_ENGINEER = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        CITY = new javax.swing.JTextField();
        REMOVE = new javax.swing.JButton();
        S_O_DATE = new javax.swing.JFormattedTextField();
        cmdAdd = new javax.swing.JButton();
        lblRevNo = new javax.swing.JLabel();
        Old_Piece = new javax.swing.JCheckBox();
        lblInchargeName = new javax.swing.JLabel();
        chkbox_TenderParty = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        txtContactPerson = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtEmailId = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtPhoneNo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtOrderMonth = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtOA_NO = new javax.swing.JTextField();
        txtEmail3 = new javax.swing.JTextField();
        txtEmail2 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        chbEmailUpdate = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        rdoAdvancePayment = new javax.swing.JRadioButton();
        rdoOtherPayment = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        txtPaymentDate = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtPaymentRemark = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        btnShowDesignMaster = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        txtPersonPosition = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txtContectPerson_Dinesh = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
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
        btnSendFAmail = new javax.swing.JButton();
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
        jPanel10 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblPreviousInvoice = new javax.swing.JTable();
        lblPartyCode1 = new javax.swing.JLabel();
        lblFromDate1 = new javax.swing.JLabel();
        lblToDate1 = new javax.swing.JLabel();
        lblFrom1 = new javax.swing.JLabel();
        lblTo1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Table_prevoiusData = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtMachineNo = new javax.swing.JTextField();
        txtPosition = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        lblParty = new javax.swing.JLabel();
        lblPartyName = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_version_info = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        cmdAddPO = new javax.swing.JButton();
        cmdRemovePO = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        DocTable = new javax.swing.JTable();
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
        lblStatus1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        ltbPink = new javax.swing.JLabel();

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

        jLabel2.setText("S.O.Date");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 10, 90, 15);

        jLabel3.setText("Order No");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(260, 10, 80, 15);

        jLabel5.setText("Remark");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 200, 80, 15);

        REMARK.setToolTipText("Enter Remark");
        REMARK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                REMARKFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                REMARKFocusLost(evt);
            }
        });
        REMARK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REMARKActionPerformed(evt);
            }
        });
        REMARK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                REMARKKeyPressed(evt);
            }
        });
        jPanel1.add(REMARK);
        REMARK.setBounds(90, 190, 470, 30);

        jLabel7.setText("District");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(260, 100, 110, 30);

        jLabel8.setText("Sales Engineer");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(260, 70, 130, 30);

        S_O_NO.setEditable(false);
        S_O_NO.setBackground(new java.awt.Color(254, 242, 230));
        S_O_NO.setText("S00000001");
        S_O_NO.setEnabled(false);
        jPanel1.add(S_O_NO);
        S_O_NO.setBounds(360, 10, 200, 30);

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
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableMouseClicked(evt);
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
        Table.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                TableCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        Table.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                TableAncestorMoved(evt);
            }
        });
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 310, 1270, 160);

        jLabel9.setText("Party Code");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 50, 100, 20);

        PARTY_CODE.setToolTipText("Press F1 key for search Party Code");
        PARTY_CODE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PARTY_CODEFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PARTY_CODEFocusLost(evt);
            }
        });
        PARTY_CODE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PARTY_CODEKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PARTY_CODEKeyTyped(evt);
            }
        });
        jPanel1.add(PARTY_CODE);
        PARTY_CODE.setBounds(90, 40, 150, 30);

        jLabel10.setText("Party Name");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(260, 40, 120, 30);

        PARTY_NAME.setEditable(false);
        jPanel1.add(PARTY_NAME);
        PARTY_NAME.setBounds(360, 40, 200, 30);

        jLabel11.setText("Reference");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(260, 130, 100, 30);

        jLabel12.setText("P.O. No.");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(10, 170, 90, 15);

        jLabel13.setText("P.O. Date");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(260, 160, 80, 30);

        P_O_NO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                P_O_NOFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                P_O_NOFocusLost(evt);
            }
        });
        P_O_NO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                P_O_NOKeyPressed(evt);
            }
        });
        jPanel1.add(P_O_NO);
        P_O_NO.setBounds(90, 160, 150, 30);

        jLabel14.setText("Reference Date");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(580, 130, 120, 30);

        REFERENCE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "P.O.", "Email", "Verbal" }));
        REFERENCE.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                REFERENCEItemStateChanged(evt);
            }
        });
        jPanel1.add(REFERENCE);
        REFERENCE.setBounds(360, 130, 200, 30);

        P_O_DATE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                P_O_DATEFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                P_O_DATEFocusLost(evt);
            }
        });
        P_O_DATE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                P_O_DATEKeyPressed(evt);
            }
        });
        jPanel1.add(P_O_DATE);
        P_O_DATE.setBounds(360, 160, 200, 30);

        ORDER_VALUE.setEditable(false);
        ORDER_VALUE.setBackground(new java.awt.Color(209, 206, 203));
        ORDER_VALUE.setText("0");
        jPanel1.add(ORDER_VALUE);
        ORDER_VALUE.setBounds(1110, 470, 170, 30);

        jLabel4.setText("Order Value ");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(660, 450, 90, 30);

        REF_DATE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                REF_DATEFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                REF_DATEFocusLost(evt);
            }
        });
        REF_DATE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                REF_DATEKeyPressed(evt);
            }
        });
        jPanel1.add(REF_DATE);
        REF_DATE.setBounds(700, 130, 170, 30);

        jLabel15.setText("Region");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(10, 80, 100, 15);

        jLabel6.setText("Country");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(10, 140, 70, 15);

        DISTRICT.setEditable(false);
        jPanel1.add(DISTRICT);
        DISTRICT.setBounds(360, 100, 200, 30);

        COUNTRY.setEditable(false);
        jPanel1.add(COUNTRY);
        COUNTRY.setBounds(90, 130, 150, 30);

        REGION.setEditable(false);
        jPanel1.add(REGION);
        REGION.setBounds(90, 70, 150, 30);

        S_ENGINEER.setEditable(false);
        jPanel1.add(S_ENGINEER);
        S_ENGINEER.setBounds(360, 70, 60, 30);

        jLabel16.setText("City");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(10, 110, 70, 15);

        CITY.setEditable(false);
        jPanel1.add(CITY);
        CITY.setBounds(90, 100, 150, 30);

        REMOVE.setText("REMOVE");
        REMOVE.setEnabled(false);
        REMOVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REMOVEActionPerformed(evt);
            }
        });
        jPanel1.add(REMOVE);
        REMOVE.setBounds(1190, 280, 90, 25);

        S_O_DATE.setEditable(false);
        jPanel1.add(S_O_DATE);
        S_O_DATE.setBounds(90, 10, 150, 30);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        jPanel1.add(cmdAdd);
        cmdAdd.setBounds(1100, 280, 90, 25);

        lblRevNo.setText("...");
        jPanel1.add(lblRevNo);
        lblRevNo.setBounds(240, 10, 34, 15);

        Old_Piece.setText("OLD PIECE");
        Old_Piece.setEnabled(false);
        jPanel1.add(Old_Piece);
        Old_Piece.setBounds(600, 10, 110, 30);
        jPanel1.add(lblInchargeName);
        lblInchargeName.setBounds(420, 70, 140, 30);

        chkbox_TenderParty.setText("TENDER PARTY");
        chkbox_TenderParty.setEnabled(false);
        jPanel1.add(chkbox_TenderParty);
        chkbox_TenderParty.setBounds(730, 10, 160, 30);

        jLabel23.setText("(Dinesh Mills)");
        jPanel1.add(jLabel23);
        jLabel23.setBounds(880, 250, 130, 20);

        txtContactPerson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContactPersonActionPerformed(evt);
            }
        });
        jPanel1.add(txtContactPerson);
        txtContactPerson.setBounds(1010, 130, 160, 30);

        jLabel24.setText("Email Id 3");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(580, 220, 110, 30);

        txtEmailId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailIdActionPerformed(evt);
            }
        });
        jPanel1.add(txtEmailId);
        txtEmailId.setBounds(90, 220, 150, 30);

        jLabel25.setText("Phone Number");
        jPanel1.add(jLabel25);
        jLabel25.setBounds(880, 190, 120, 30);
        jPanel1.add(txtPhoneNo);
        txtPhoneNo.setBounds(1010, 190, 160, 30);

        jLabel26.setText("O.A. Date");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(10, 280, 80, 30);

        txtOrderMonth.setEditable(false);
        txtOrderMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderMonthActionPerformed(evt);
            }
        });
        jPanel1.add(txtOrderMonth);
        txtOrderMonth.setBounds(90, 280, 150, 30);

        jLabel27.setText("O.A. No");
        jPanel1.add(jLabel27);
        jLabel27.setBounds(260, 280, 100, 30);

        txtOA_NO.setEditable(false);
        txtOA_NO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOA_NOActionPerformed(evt);
            }
        });
        jPanel1.add(txtOA_NO);
        txtOA_NO.setBounds(360, 280, 200, 30);
        jPanel1.add(txtEmail3);
        txtEmail3.setBounds(700, 220, 170, 30);
        jPanel1.add(txtEmail2);
        txtEmail2.setBounds(360, 220, 200, 30);

        jLabel28.setText("Email Id *");
        jPanel1.add(jLabel28);
        jLabel28.setBounds(10, 220, 110, 30);

        jLabel29.setText("Email Id 2");
        jPanel1.add(jLabel29);
        jLabel29.setBounds(260, 220, 110, 30);

        chbEmailUpdate.setText("Update Contact Person and Email on Party Master");
        chbEmailUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbEmailUpdateActionPerformed(evt);
            }
        });
        jPanel1.add(chbEmailUpdate);
        chbEmailUpdate.setBounds(90, 250, 470, 23);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        jLabel1.setText("Payment ");
        jPanel4.add(jLabel1);
        jLabel1.setBounds(10, 0, 80, 30);

        buttonGroup1.add(rdoAdvancePayment);
        rdoAdvancePayment.setText("Advance");
        rdoAdvancePayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoAdvancePaymentMouseClicked(evt);
            }
        });
        jPanel4.add(rdoAdvancePayment);
        rdoAdvancePayment.setBounds(130, 4, 90, 20);

        buttonGroup1.add(rdoOtherPayment);
        rdoOtherPayment.setText("Other");
        rdoOtherPayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoOtherPaymentMouseClicked(evt);
            }
        });
        jPanel4.add(rdoOtherPayment);
        rdoOtherPayment.setBounds(220, 4, 100, 20);

        jLabel18.setText("Date");
        jPanel4.add(jLabel18);
        jLabel18.setBounds(10, 30, 50, 15);
        jPanel4.add(txtPaymentDate);
        txtPaymentDate.setBounds(130, 30, 80, 20);

        jLabel30.setText("Remark");
        jPanel4.add(jLabel30);
        jLabel30.setBounds(10, 40, 60, 40);
        jPanel4.add(txtPaymentRemark);
        txtPaymentRemark.setBounds(130, 50, 170, 20);

        jLabel37.setText("DD/MM/YYYY");
        jLabel37.setEnabled(false);
        jPanel4.add(jLabel37);
        jLabel37.setBounds(210, 30, 100, 15);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(580, 40, 330, 80);

        btnShowDesignMaster.setText("Show Design Master");
        btnShowDesignMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowDesignMasterActionPerformed(evt);
            }
        });
        jPanel1.add(btnShowDesignMaster);
        btnShowDesignMaster.setBounds(900, 470, 210, 25);

        jLabel38.setText("Position of Person");
        jPanel1.add(jLabel38);
        jLabel38.setBounds(880, 160, 140, 30);

        txtPersonPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPersonPositionActionPerformed(evt);
            }
        });
        jPanel1.add(txtPersonPosition);
        txtPersonPosition.setBounds(1010, 160, 160, 30);

        jLabel39.setText("Contact Person");
        jPanel1.add(jLabel39);
        jLabel39.setBounds(880, 130, 110, 30);

        jLabel40.setText("Contacted By");
        jPanel1.add(jLabel40);
        jLabel40.setBounds(880, 230, 130, 20);

        txtContectPerson_Dinesh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContectPerson_DineshActionPerformed(evt);
            }
        });
        jPanel1.add(txtContectPerson_Dinesh);
        txtContectPerson_Dinesh.setBounds(1010, 220, 160, 30);

        jButton1.setText("Design Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(700, 470, 190, 25);

        Tab.addTab("Order Entry", jPanel1);

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

        btnSendFAmail.setText("Send final approved mail");
        btnSendFAmail.setEnabled(false);
        btnSendFAmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFAmailActionPerformed(evt);
            }
        });
        Tab2.add(btnSendFAmail);
        btnSendFAmail.setBounds(546, 10, 200, 25);

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

        jPanel10.setLayout(null);

        tblPreviousInvoice.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPreviousInvoice.setSelectionBackground(new java.awt.Color(208, 220, 234));
        tblPreviousInvoice.setSelectionForeground(new java.awt.Color(231, 16, 16));
        tblPreviousInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPreviousInvoiceMouseClicked(evt);
            }
        });
        tblPreviousInvoice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblPreviousInvoiceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblPreviousInvoiceFocusLost(evt);
            }
        });
        tblPreviousInvoice.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                tblPreviousInvoiceCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        tblPreviousInvoice.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                tblPreviousInvoiceAncestorMoved(evt);
            }
        });
        tblPreviousInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPreviousInvoiceKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(tblPreviousInvoice);

        jPanel10.add(jScrollPane7);
        jScrollPane7.setBounds(0, 0, 990, 480);
        jPanel10.add(lblPartyCode1);
        lblPartyCode1.setBounds(20, 30, 110, 20);
        jPanel10.add(lblFromDate1);
        lblFromDate1.setBounds(150, 30, 110, 0);
        jPanel10.add(lblToDate1);
        lblToDate1.setBounds(270, 30, 130, 0);
        jPanel10.add(lblFrom1);
        lblFrom1.setBounds(150, 30, 100, 20);
        jPanel10.add(lblTo1);
        lblTo1.setBounds(270, 30, 100, 20);

        Tab.addTab("Last 10 Invoice ", jPanel10);

        jPanel8.setLayout(null);

        Table_prevoiusData.setModel(new javax.swing.table.DefaultTableModel(
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
        Table_prevoiusData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Table_prevoiusDataKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(Table_prevoiusData);

        jPanel8.add(jScrollPane4);
        jScrollPane4.setBounds(0, 80, 1190, 290);

        jLabel20.setText("Machine No");
        jPanel8.add(jLabel20);
        jLabel20.setBounds(260, 30, 90, 15);

        jLabel21.setText("Position");
        jPanel8.add(jLabel21);
        jLabel21.setBounds(410, 30, 80, 15);
        jPanel8.add(txtMachineNo);
        txtMachineNo.setBounds(350, 20, 50, 30);

        txtPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPositionActionPerformed(evt);
            }
        });
        jPanel8.add(txtPosition);
        txtPosition.setBounds(480, 20, 60, 30);

        jButton2.setText("SHOW DATA");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton2);
        jButton2.setBounds(610, 20, 110, 25);

        jLabel22.setText("Party");
        jPanel8.add(jLabel22);
        jLabel22.setBounds(10, 30, 70, 15);

        lblParty.setText("party_code");
        jPanel8.add(lblParty);
        lblParty.setBounds(80, 30, 140, 15);

        lblPartyName.setText("party_name");
        jPanel8.add(lblPartyName);
        lblPartyName.setBounds(80, 50, 280, 17);

        Tab.addTab("Last 10 Sales Order", jPanel8);

        jPanel9.setLayout(null);

        tbl_version_info.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_version_info.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_version_infoKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(tbl_version_info);

        jPanel9.add(jScrollPane5);
        jScrollPane5.setBounds(0, 10, 910, 400);

        Tab.addTab("Version Information", jPanel9);

        jPanel7.setLayout(null);

        jPanel11.setLayout(null);

        cmdAddPO.setMnemonic('A');
        cmdAddPO.setText("Add");
        cmdAddPO.setToolTipText("Add Row");
        cmdAddPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddPOActionPerformed(evt);
            }
        });
        jPanel11.add(cmdAddPO);
        cmdAddPO.setBounds(10, 40, 90, 25);

        cmdRemovePO.setMnemonic('R');
        cmdRemovePO.setText("Remove");
        cmdRemovePO.setToolTipText("Remove Selected Row");
        cmdRemovePO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemovePOActionPerformed(evt);
            }
        });
        jPanel11.add(cmdRemovePO);
        cmdRemovePO.setBounds(120, 40, 90, 25);

        DocTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane8.setViewportView(DocTable);

        jPanel11.add(jScrollPane8);
        jScrollPane8.setBounds(10, 70, 900, 330);

        jPanel7.add(jPanel11);
        jPanel11.setBounds(0, 10, 920, 470);

        Tab.addTab("Attachment", jPanel7);

        getContentPane().add(Tab);
        Tab.setBounds(0, 70, 1300, 540);

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
        ToolBar.setBounds(0, 0, 1300, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Felt Sales Order 1.2");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 1300, 25);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 740, 930, 22);

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus1);
        lblStatus1.setBounds(20, 710, 740, 30);
        getContentPane().add(jPanel5);
        jPanel5.setBounds(210, 50, 10, 10);
        getContentPane().add(ltbPink);
        ltbPink.setBounds(10, 70, 0, 0);
    }// </editor-fold>//GEN-END:initComponents

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        // if (evt.getKeyCode() == 112 || evt.getKeyCode() == 10) {

//        int today_date = EITLERPGLOBAL.getCurrentDay();
//        
//        if(today_date>=26 && today_date<=31)
//        {
//            JOptionPane.showMessageDialog(this, "On 26th to 31st, You cannot take new order.", "ERROR", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
        if (PARTY_CODE.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please select PARTY");
            PARTY_CODE.requestFocus();
        } else if (Table.getSelectedColumn() == 1) {
            String minqlt = "";
            char mchr = ' ';
            mchr = evt.getKeyChar();
            if (mchr == '\b' || mchr == '\n') {
                minqlt = Table.getValueAt(Table.getRowCount() - 1, 2).toString();
            } else {
                minqlt = Table.getValueAt(Table.getRowCount() - 1, 2).toString();
            }
            searchkey search = new searchkey();
            search.SQL = "SELECT D.MM_MACHINE_NO AS MACHINE_NO,D.MM_MACHINE_POSITION AS POSITION,D.MM_MACHINE_POSITION_DESC AS POSITION_DESC,D.MM_ITEM_CODE AS ITEM_CODE,D.MM_GRUP AS GRUP,(D.MM_FELT_LENGTH+D.MM_FABRIC_LENGTH) AS LENGTH,(D.MM_FELT_WIDTH+D.MM_FABRIC_WIDTH) AS WIDTH,D.MM_FELT_GSM AS GSM,concat(D.MM_FELT_STYLE,D.MM_STYLE_DRY) AS STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,PRODUCTION.FELT_MACHINE_MASTER_DETAIL D WHERE D.MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') AND (D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '') AND D.MM_FELT_GSM!='') AND H.APPROVED=1 AND H.CANCELED=0 AND H.MM_DOC_NO=D.MM_DOC_NO ORDER BY  D.MM_MACHINE_NO,D.MM_MACHINE_POSITION";
            search.ReturnCol = 1;
            search.Party_Code = PARTY_CODE.getText();
            search.ShowReturnCol = true;
            search.DefaultSearchOn = 1;
            search.setsearchText(minqlt);
            if (search.ShowRSLOV()) {
                if (Table.getCellEditor() != null) {
                    Table.getCellEditor().stopCellEditing();
                }
                seleval = search.ReturnVal;
                seltyp = search.ReturnVal;
                String secondval = search.SecondVal;
                //JOptionPane.showMessageDialog(null, "COMBINATION CODE = "+search.ReturnVal+search.SecondVal);
                String LastQuery = "SELECT D.MM_MACHINE_NO,D.MM_MACHINE_POSITION,D.MM_MACHINE_POSITION_DESC,D.MM_ITEM_CODE,D.MM_GRUP,(D.MM_FELT_LENGTH+D.MM_FABRIC_LENGTH),(D.MM_FELT_WIDTH+D.MM_FABRIC_WIDTH),D.MM_FELT_GSM,concat(D.MM_FELT_STYLE,D.MM_STYLE_DRY) as MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,PRODUCTION.FELT_MACHINE_MASTER_DETAIL D where CONCAT(D.MM_COMBINATION_CODE,D.MM_FELT_LENGTH,D.MM_FELT_WIDTH,D.MM_FELT_GSM) like '" + search.ReturnVal + search.SecondVal + "%' AND D.MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') AND (D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '')  AND D.MM_FELT_GSM!='')  AND H.APPROVED=1 AND H.CANCELED=0 AND H.MM_DOC_NO=D.MM_DOC_NO   ORDER BY  D.MM_MACHINE_NO,D.MM_MACHINE_POSITION";
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

                    String check_lock_MACHINE = data.getStringValueFromDB("SELECT COALESCE(MACHINE_LOCK_IND, 0) AS PARTY_LOCK FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND MM_MACHINE_NO='" + Piecedetail[0] + "'");

                    if ("1".equals(check_lock_MACHINE)) {
                        JOptionPane.showMessageDialog(null, "Machine : " + Piecedetail[0] + " is locked for Party Code : " + PARTY_CODE.getText() + ".");
                        PARTY_CODE.requestFocus();
                        return;
                    }

                    String check_lock_POSITION = data.getStringValueFromDB("SELECT COALESCE(POSITION_LOCK_IND, 0) AS POSITION_LOCK_IND FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND MM_MACHINE_NO='" + Piecedetail[0] + "' AND MM_MACHINE_POSITION='" + Piecedetail[1] + "'");

                    if ("1".equals(check_lock_POSITION)) {
                        JOptionPane.showMessageDialog(null, "Machine : " + Piecedetail[0] + " AND Position : " + Piecedetail[1] + " is locked for Party Code : " + PARTY_CODE.getText() + ".");
                        PARTY_CODE.requestFocus();
                        return;
                    }

                    boolean record = data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) AND PR_REQUESTED_MONTH IN (\"\",\"0\",NULL) "
                            + "AND PR_PIECE_STAGE IN ('WEAVING','PLANNING','SEAMING','SPLICING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK') "
                            + "AND PR_MACHINE_NO='" + Piecedetail[0] + "' AND PR_POSITION_NO='" + Piecedetail[1] + "' AND PR_PARTY_CODE='" + PARTY_CODE.getText() + "'");

                    System.out.println("coutn = SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) AND PR_REQUESTED_MONTH IN (\"\",\"0\",NULL) "
                            + "AND PR_PIECE_STAGE IN ('WEAVING','PLANNING','SEAMING','SPLICING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK') "
                            + "AND PR_MACHINE_NO='" + Piecedetail[0] + "' AND PR_POSITION_NO='" + Piecedetail[1] + "' AND PR_PARTY_CODE='" + PARTY_CODE.getText() + "'");
                    if (record) {
                        searchkey_previous_order_req_month search_check = new searchkey_previous_order_req_month();
                        search_check.SQL = "SELECT PR_PIECE_NO,PR_REQUESTED_MONTH,PR_MACHINE_NO,PR_POSITION_NO, PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_PIECE_STAGE, "
                                + " PR_WIP_STATUS,PR_UPN,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_GSM,PR_BILL_PRODUCT_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) AND PR_REQUESTED_MONTH IN (\"\",\"0\",NULL) "
                                + "AND PR_PIECE_STAGE IN ('WEAVING','PLANNING','SEAMING','SPLICING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK') "
                                + "AND PR_MACHINE_NO='" + Piecedetail[0] + "' AND PR_POSITION_NO='" + Piecedetail[1] + "' AND PR_PARTY_CODE='" + PARTY_CODE.getText() + "'";
                        search_check.ReturnCol = 1;
                        search_check.Party_Code = PARTY_CODE.getText();
                        search_check.ShowReturnCol = true;
                        search_check.DefaultSearchOn = 1;
                        search_check.setsearchText(minqlt);
                        if (search_check.ShowRSLOV()) {

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error on connectrion = " + e.getMessage());
                }

//                if (PARTY_CODE.getText().startsWith("8")) {
//                    String MACHINE_NO = Piecedetail[0];
//                    String POSITION = Piecedetail[1];
//                    String Query_BUDGET = "SELECT * FROM PRODUCTION.FELT_BUDGET where PARTY_CODE='" + PARTY_CODE.getText() + "' AND MACHINE_NO=" + MACHINE_NO + " AND POSITION_NO=" + POSITION + " AND YEAR_FROM='" + EITLERPGLOBAL.getCurrentFinYear() + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'";
//                    System.out.println("Query_BUDGET : " + Query_BUDGET);
//
//                    if (!data.IsRecordExist(Query_BUDGET)) {
//                        JOptionPane.showMessageDialog(this, "Please update BUDGET first to enter order of PARTY : " + PARTY_CODE.getText() + ", MACHINE NO : " + MACHINE_NO + ", POSITION : " + POSITION);
//                        return;
//                    }
//                }
                //Change on 21/01/2021 
//                if (PARTY_CODE.getText().startsWith("8")) {
//                    String MACHINE_NO = Piecedetail[0];
//                    String POSITION = Piecedetail[1];
//                    String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");
//                    String Query_BUDGET = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL where PARTY_CODE='" + PARTY_CODE.getText() + "' AND MACHINE_NO=" + MACHINE_NO + " AND POSITION_NO=" + POSITION + " AND YEAR_FROM='" + EITLERPGLOBAL.getCurrentFinYear() + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'  AND DOC_NO LIKE '_____"+Month+"%' AND APPROVED=1 AND CURRENT_PROJECTION-(WIP_QTY+STOCK_QTY+DISPATCH_QTY)>0";
//                    System.out.println("Query_BUDGET : " + Query_BUDGET);
//                    
//                    if (!data.IsRecordExist(Query_BUDGET)) {
//                        JOptionPane.showMessageDialog(this, "Please update Sales Projection first to enter order of PARTY : " + PARTY_CODE.getText() + ", MACHINE NO : " + MACHINE_NO + ", POSITION : " + POSITION);
//                        return;
//                    }
//                }
                //Projection change ... projection cheking is after diversion process
                boolean diversion_flag = false;

                //If any Group, Item, Length, Style, Width, Gsm mot found for Order than It will open Machine Master Updation Form
                if (Piecedetail[3].equals("") || Piecedetail[5].equals("") || Piecedetail[6].equals("") || Piecedetail[7].equals("")) {

                    AppletFrame aFrame = new AppletFrame("Machine Master Amendment");
                    aFrame.startAppletEx("EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend", "Machine Master Amendment");
                    frmmachinesurveyAmend ObjItem = (frmmachinesurveyAmend) aFrame.ObjApplet;
                    // ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText());

                    ObjItem.requestFocus();
                    ObjItem.Add();
                    ObjItem.AddFromOrder(PARTY_CODE.getText(), Piecedetail[0]);
                    ObjItem.machine_lostfocus();
                    //ObjItem.txtmachinenoFocusLost();
                    return;
                }

                //Check for DATA(Machine No, Position, Party) exist on Order
                Connection Conn1;
                Statement stmt1;
                ResultSet rsData1;
                try {
                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    //rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_MACHINE_NO='"+Piecedetail[0]+"' AND PR_POSITION_NO='"+Piecedetail[1]+"' AND PR_PARTY_CODE='"+PARTY_CODE.getText()+"'");

                    rsData1 = stmt1.executeQuery("SELECT COUNT(*) "
                            + "FROM  PRODUCTION.FELT_SALES_ORDER_HEADER A,PRODUCTION.FELT_SALES_ORDER_DETAIL B "
                            + "WHERE  A.S_ORDER_NO = B.S_ORDER_NO "
                            + "AND B.MACHINE_NO='" + Piecedetail[0] + "' "
                            + "AND B.POSITION = '" + Piecedetail[1] + "' "
                            + "AND A.PARTY_CODE = '" + PARTY_CODE.getText() + "'");
                    System.out.println("SELECT COUNT(*) "
                            + "FROM  PRODUCTION.FELT_SALES_ORDER_HEADER A,PRODUCTION.FELT_SALES_ORDER_DETAIL B "
                            + "WHERE  A.S_ORDER_NO = B.S_ORDER_NO "
                            + "AND B.MACHINE_NO='" + Piecedetail[0] + "' "
                            + "AND B.POSITION = '" + Piecedetail[1] + "' "
                            + "AND A.PARTY_CODE = '" + PARTY_CODE.getText() + "'");
                    rsData1.first();
                    int total = rsData1.getInt(1);
                    if (!(total > 0)) {
//                            Object[] options = {"Yes", "No"};
//                            int reply = JOptionPane.showOptionDialog(this, "Previous Order Not Found for this Party, MachineNo and Position \n\n Do you want to update Machine Master?",
//                                    "Confirmation",
//                                    JOptionPane.YES_NO_OPTION,
//                                    JOptionPane.WARNING_MESSAGE,
//                                    null, options, options[1]);
//                            if (reply == JOptionPane.YES_OPTION) {
                        //open Machine Master Updation Form 
                        AppletFrame aFrame1 = new AppletFrame("Machine Master Amendment");
                        aFrame1.startAppletEx("EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend", "Machine Master Amendment");

                        frmmachinesurveyAmend ObjItem1 = (frmmachinesurveyAmend) aFrame1.ObjApplet;
                        //ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText());
                        //aFrame1.setAlwaysOnTop(true);
                        ObjItem1.requestFocus();
                        ObjItem1.Add();
                        ObjItem1.AddFromOrder(PARTY_CODE.getText(), Piecedetail[0]);
                        ObjItem1.machine_lostfocus();

                        //return;
                        // }
                    }
                } catch (Exception e) {

                    System.out.println("Error on connectrion = " + e.getMessage());
                    e.printStackTrace();
                }

                /*//DISPLAY AVAILABLE DATA OF PARTY_CODE MACHINE NO AND POSITION
                 String SQL_previous = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_MACHINE_NO="+Piecedetail[0]+"  AND PR_PARTY_CODE='"+PARTY_CODE.getText()+"'  AND PR_POSITION_NO="+Piecedetail[1]+" AND PR_PIECE_STAGE IN ('WEAVING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK','BSR')";
                 String sql_DATA= "SELECT " +
                 " PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,POSITION_DESC, PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_PIECE_STAGE, " +
                 " PR_WIP_STATUS,PR_UPN,POSITION_DESIGN_NO,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_WEIGHT,PR_BILL_SQMTR,PR_BILL_GSM,PR_BILL_PRODUCT_CODE " +
                 " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR,PRODUCTION.FELT_MACHINE_POSITION_MST PS " +
                 " WHERE PS.POSITION_NO = PR.PR_POSITION_NO AND PR_MACHINE_NO="+Piecedetail[0]+"  AND PR_PARTY_CODE='"+PARTY_CODE.getText()+"'  AND PR_POSITION_NO="+Piecedetail[1]+" AND PR_PIECE_STAGE IN ('WEAVING','FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK','BSR')";
                 searchkey_previous_order pre_order = new searchkey_previous_order(); 
                 pre_order.SQL = sql_DATA;
                 pre_order.txtPartyCode.setText(PARTY_CODE.getText());
                 pre_order.txtMachineNo.setText(Piecedetail[0]);
                 pre_order.txtPosition.setText(Piecedetail[1]);
                 if (pre_order.ShowRSLOV()) {
                    
                 }*/
                String Position_Design_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + Piecedetail[1]);
                String UPN_for_diversion_check = PARTY_CODE.getText() + Piecedetail[0] + Position_Design_No;

                try {
                    if (data.IsRecordExist("SELECT D.PIECE_NO,D.UPN,D.DOC_NO FROM PRODUCTION.FELT_SALES_SPILOVER_ASSIGN_UPN_HEADER H,PRODUCTION.FELT_SALES_SPILOVER_ASSIGN_UPN_DETAIL D where D.DOC_NO=H.DOC_NO AND H.APPROVED=0 AND D.UPN='" + UPN_for_diversion_check + "'")) {
                        ResultSet rs = data.getResult("SELECT D.PIECE_NO,D.UPN,D.DOC_NO FROM PRODUCTION.FELT_SALES_SPILOVER_ASSIGN_UPN_HEADER H,PRODUCTION.FELT_SALES_SPILOVER_ASSIGN_UPN_DETAIL D where D.DOC_NO=H.DOC_NO AND H.APPROVED=0 AND D.UPN='" + UPN_for_diversion_check + "'");
                        try {
                            rs.first();
                            JOptionPane.showMessageDialog(this, "Document pending to Assign UPN for Spilover Piece for this UPN " + UPN_for_diversion_check + ", So You cannot take new order for this UPN.");
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST where UPN='" + UPN_for_diversion_check + "' AND (DIVERSION_NO='' OR DIVERSION_NO IS NULL)")) {
                    //String query = "SELECT * FROM PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST where UPN='"+UPN_for_diversion_check+"' AND (DIVERSION_NO='' OR DIVERSION_NO IS NULL)";
                    String query = "SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PM.POSITION_DESC,PR_PARTY_CODE,PARTY_NAME,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK,'' AS GROUP_WISE,PR_FNSG_DATE,PR_WVG_DATE,PR_MND_DATE "
                            + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR,PRODUCTION.FELT_MACHINE_POSITION_MST PM,DINESHMILLS.D_SAL_PARTY_MASTER SPM WHERE PR_PIECE_NO IN (SELECT distinct PIECE_NO FROM PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST where UPN='" + UPN_for_diversion_check + "' AND (DIVERSION_NO='' OR DIVERSION_NO IS NULL)) AND SPM.PARTY_CODE=PR_PARTY_CODE AND PR_POSITION_NO = PM.POSITION_NO";
                    System.out.println("QUERY : " + query);
                    searchkey_diversionlist search_dv1 = new searchkey_diversionlist();
                    search_dv1.SQL = query;
                    search_dv1.Title = "Suitable Obsolete Pieces for selected UPN ( " + UPN_for_diversion_check + " )";
                    search_dv1.ReturnCol = 1;
                    search_dv1.Party_Code = PARTY_CODE.getText();
                    search_dv1.Order_Group = Order_Group;
                    search_dv1.ShowReturnCol = true;
                    search_dv1.DefaultSearchOn = 1;
                    search_dv1.setsearchText(minqlt);
                    search_dv1.txtSearch.setEditable(false);
                    if (search_dv1.ShowRSLOV()) {
                        seleval = search_dv1.ReturnVal;
                        try {
                            AppletFrame aFrame = new AppletFrame("Felt Sales Order : Diversion");
                            aFrame.startAppletEx("EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion", "Felt Sales Order : Diversion");
                            FrmFeltOrderDiversion ObjItem = (FrmFeltOrderDiversion) aFrame.ObjApplet;

                            ObjItem.frm_S_O_No = S_O_NO.getText();
                            ObjItem.frm_PARTY_CODE = PARTY_CODE.getText();
                            ObjItem.frm_REFERENCE = REFERENCE.getSelectedItem().toString();
                            ObjItem.frm_REFERENCE_DATE = REF_DATE.getText();
                            ObjItem.frm_PO_NO = P_O_NO.getText();
                            ObjItem.frm_PO_DATE = P_O_DATE.getText();
                            ObjItem.frm_REMARK = REMARK.getText();
                            ObjItem.frm_PARTY_CODE = PARTY_CODE.getText();
                            ObjItem.frm_Piece_No = search_dv1.ReturnVal;
                            ObjItem.frm_Machine_No = Piecedetail[0];
                            ObjItem.frm_Position_No = Piecedetail[1];

                            ObjItem.DataSettingFromOrder();

                            //Diversion Flag true when Selected Piece for Diversion
                            diversion_flag = true;

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error in Diversion List : " + e.getLocalizedMessage());
                        }
                    }
                }
                if (data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST where UPN='" + UPN_for_diversion_check + "' AND (DIVERSION_NO='' OR DIVERSION_NO IS NULL)")) {
                    diversion_flag = true;
                }

//COMMENT ON 12 AUG 2019
//                String Diversion_Piece_No = data.getStringValueFromDB("SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST where UPN='"+UPN_for_diversion_check+"' AND (DIVERSION_NO='' OR DIVERSION_NO IS NULL)");
//                String Stage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+Diversion_Piece_No+"'");
//                
//                if(!"".equals(Diversion_Piece_No) && ( Stage.equals("IN STOCK") || Stage.equals("NEEDLING") || Stage.equals("MENDING") || Stage.equals("SEAMING") || Stage.equals("FINISHING"))) 
//                {
//                    try {
//
//                        AppletFrame aFrame = new AppletFrame("Felt Sales Order : Diversion");
//                        aFrame.startAppletEx("EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion", "Felt Sales Order : Diversion");
//                        FrmFeltOrderDiversion ObjItem = (FrmFeltOrderDiversion) aFrame.ObjApplet;
//
//                        ObjItem.frm_S_O_No = S_O_NO.getText();
//                        ObjItem.frm_PARTY_CODE = PARTY_CODE.getText();
//                        ObjItem.frm_REFERENCE = REFERENCE.getSelectedItem().toString();
//                        ObjItem.frm_REFERENCE_DATE = REF_DATE.getText();
//                        ObjItem.frm_PO_NO = P_O_NO.getText();
//                        ObjItem.frm_PO_DATE = P_O_DATE.getText();
//                        ObjItem.frm_REMARK = REMARK.getText();
//                        ObjItem.frm_PARTY_CODE = PARTY_CODE.getText();
//                        ObjItem.frm_Piece_No = Diversion_Piece_No;
//                        ObjItem.frm_Machine_No = Piecedetail[0];
//                        ObjItem.frm_Position_No = Piecedetail[1];
//
//                        ObjItem.DataSettingFromOrder();
//
//                        //Diversion Flag true when Selected Piece for Diversion
//                        diversion_flag = true;
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("Error in Diversion List : " + e.getLocalizedMessage());
//                    }
//                    
//                    
//                    
//                }
                if (!diversion_flag) {
                    //DIVERSION LIST SELECTION
                    double L_Start = 0, L_End = 0, W_Start = 0, W_End = 0, G_Start = 0, G_End = 0;
                    L_Start = Double.parseDouble(Piecedetail[5]) - 0.50;
                    L_End = Double.parseDouble(Piecedetail[5]) + 0.50;
                    W_Start = Double.parseDouble(Piecedetail[6]) - 0.50;
                    W_End = Double.parseDouble(Piecedetail[6]) + 0.20;
                    G_Start = Double.parseDouble(Piecedetail[7]) - 100;
                    G_End = Double.parseDouble(Piecedetail[7]) + 100;

                    String SQL = "SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PM.POSITION_DESC,PR_PARTY_CODE,PARTY_NAME,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK,'' AS GROUP_WISE,PR_FNSG_DATE,PR_WVG_DATE,PR_MND_DATE  "
                            + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_MACHINE_POSITION_MST PM,DINESHMILLS.D_SAL_PARTY_MASTER SPM  where SPM.PARTY_CODE=PR_PARTY_CODE AND PR_POSITION_NO = PM.POSITION_NO AND PR_MACHINE_NO!='' AND PR_POSITION_NO!='' AND PR_LENGTH!='' AND PR_WIDTH!='' AND PR_GSM!='' AND PR_DIVERSION_FLAG='READY' AND (PR_PIECE_STAGE IN ('FINISHING','SEAMING','NEEDLING','MENDING','WARPING','IN STOCK')  OR (PR_PIECE_STAGE IN ('WEAVING') AND PR_DELINK='OBSOLETE'  )) AND PR_PARTY_CODE!='" + PARTY_CODE.getText() + "' "
                            + " UNION ALL SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PM.POSITION_DESC,PR_PARTY_CODE,PARTY_NAME,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK,'' AS GROUP_WISE,PR_FNSG_DATE,PR_WVG_DATE,PR_MND_DATE  "
                            + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_MACHINE_POSITION_MST PM,DINESHMILLS.D_SAL_PARTY_MASTER SPM   WHERE "
                            + " SPM.PARTY_CODE=PR_PARTY_CODE AND PR_POSITION_NO = PM.POSITION_NO AND PR_MACHINE_NO!='' AND PR_POSITION_NO!='' AND  "
                            + "  PR_LENGTH!='' AND PR_WIDTH!='' AND PR_GSM!='' AND "
                            + " PR_PARTY_CODE='" + PARTY_CODE.getText() + "' AND PR_DIVERSION_FLAG='READY' "
                            + " AND (PR_PIECE_STAGE IN ('FINISHING','SEAMING','NEEDLING','MENDING','WARPING','IN STOCK') "
                            + " OR (PR_PIECE_STAGE IN ('WEAVING') AND PR_DELINK='OBSOLETE'  )) "
                            //+ " AND COALESCE(PR_DELINK,'') NOT IN ('DELINK') "
                            + " UNION ALL SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PM.POSITION_DESC,PR_PARTY_CODE,PARTY_NAME,PR_PRODUCT_CODE, "
                            + " PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK,'GROUP_WISE' AS GROUP_WISE,PR_FNSG_DATE,PR_WVG_DATE,PR_MND_DATE  "
                            + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_MACHINE_POSITION_MST PM,DINESHMILLS.D_SAL_PARTY_MASTER SPM   WHERE "
                            + " SPM.PARTY_CODE=PR_PARTY_CODE AND PR_POSITION_NO = PM.POSITION_NO AND PR_MACHINE_NO!='' AND PR_POSITION_NO!='' AND  "
                            + " PR_LENGTH!='' AND PR_WIDTH!='' AND PR_GSM!='' AND PR_DIVERSION_FLAG!='READY' AND "
                            + " (PR_PIECE_STAGE IN ('FINISHING','SEAMING','NEEDLING','MENDING','WARPING','IN STOCK') OR (PR_PIECE_STAGE IN ('WEAVING') AND PR_DELINK='OBSOLETE'  ) )"
                            + " AND PR_PARTY_CODE IN (SELECT PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL where GROUP_CODE IN (SELECT GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL where PARTY_CODE='" + PARTY_CODE.getText() + "')) "
                            + " AND PR_PARTY_CODE != '" + PARTY_CODE.getText() + "'";
                    //                            + "where PR_LENGTH >= " + L_Start + " and "
                    //                            + "PR_LENGTH <= " + L_End + " and "
                    //                            + "PR_WIDTH >= " + W_Start + " and "
                    //                            + "PR_WIDTH <= " + W_End + " and "
                    //                            + "PR_GSM >= " + G_Start + " and "
                    //                            + "PR_GSM <= " + G_End;
                    System.out.println("----- SQL :" + SQL);
                    searchkey_diversionlist search_dv = new searchkey_diversionlist();

                    search_dv.SQL = SQL;
                    search_dv.Title = "Diversion Ready Piece List";
                    search_dv.ReturnCol = 1;
                    search_dv.Party_Code = PARTY_CODE.getText();
                    search_dv.Order_Group = Order_Group;
                    search_dv.ShowReturnCol = true;
                    search_dv.DefaultSearchOn = 1;
                    search_dv.setsearchText(minqlt);

                    if (search_dv.ShowRSLOV()) {
                        seleval = search_dv.ReturnVal;
                        seltyp = search_dv.ReturnVal;
                        secondval = search_dv.SecondVal;
                        LastQuery = search_dv.SQL;
                        try {

                            AppletFrame aFrame = new AppletFrame("Felt Sales Order : Diversion");
                            aFrame.startAppletEx("EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion", "Felt Sales Order : Diversion");
                            FrmFeltOrderDiversion ObjItem = (FrmFeltOrderDiversion) aFrame.ObjApplet;

                            ObjItem.frm_S_O_No = S_O_NO.getText();
                            ObjItem.frm_PARTY_CODE = PARTY_CODE.getText();
                            ObjItem.frm_REFERENCE = REFERENCE.getSelectedItem().toString();
                            ObjItem.frm_REFERENCE_DATE = REF_DATE.getText();
                            ObjItem.frm_PO_NO = P_O_NO.getText();
                            ObjItem.frm_PO_DATE = P_O_DATE.getText();
                            ObjItem.frm_REMARK = REMARK.getText();
                            ObjItem.frm_PARTY_CODE = PARTY_CODE.getText();
                            ObjItem.frm_Piece_No = search_dv.ReturnVal;
                            ObjItem.frm_Machine_No = Piecedetail[0];
                            ObjItem.frm_Position_No = Piecedetail[1];

                            ObjItem.DataSettingFromOrder();

                            //Diversion Flag true when Selected Piece for Diversion
                            diversion_flag = true;

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error in Diversion List : " + e.getLocalizedMessage());
                        }
                    }
                }

                if (diversion_flag) {
                    // Diversion Flag True ... Continue for Diversion Order

                } else {
                   // Diversion Flag False ..... Continue for New Order

                    //Replace on 25/01/2021
                    //Projection cheking is now for booking only.
                    if (PARTY_CODE.getText().startsWith("8")) {
                        String MACHINE_NO = Piecedetail[0];
                        String POSITION = Piecedetail[1];
                        String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");
                        String Query_BUDGET = "";

//                        if (Month.equals("02") || Month.equals("03")) {
//                            Query_BUDGET = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL where PARTY_CODE='" + PARTY_CODE.getText() + "' AND MACHINE_NO=" + MACHINE_NO + " AND POSITION_NO=" + POSITION + " AND YEAR_FROM='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 2) + "'  AND DOC_NO LIKE '_____" + Month + "%' AND APPROVED=1 AND CURRENT_PROJECTION-(WIP_QTY+STOCK_QTY+DISPATCH_QTY)>0";
//                        } else {
                            Query_BUDGET = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL where PARTY_CODE='" + PARTY_CODE.getText() + "' AND MACHINE_NO=" + MACHINE_NO + " AND POSITION_NO=" + POSITION + " AND YEAR_FROM='" + (EITLERPGLOBAL.getCurrentFinYear()) + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'  AND DOC_NO LIKE '_____" + Month + "%' AND APPROVED=1 AND CURRENT_PROJECTION-(WIP_QTY+STOCK_QTY+DISPATCH_QTY)>0";
//                        }

                        System.out.println("Query_BUDGET : " + Query_BUDGET);

                        if (!data.IsRecordExist(Query_BUDGET)) {
                            String Design_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + Piecedetail[1]);
                            String UPN = PARTY_CODE.getText() + Piecedetail[0] + Design_No;

                            String Query = "SELECT * FROM PRODUCTION.FELT_SALES_ALLOWED_BOOKING_AGAINST_PROJECTION_MASTER "
                                    + "where UPN='" + UPN + "' and MONTH=" + EITLERPGLOBAL.getCurrentMonth() + " and YEAR=" + EITLERPGLOBAL.getCurrentYear() + " and UPDATED_ALLOED_QTY>0";
                            System.out.println("Budget 2 Check : " + Query);
                            if (!data.IsRecordExist(Query)) {
                                JOptionPane.showMessageDialog(this, "Please update Sales Projection first to enter order of PARTY : " + PARTY_CODE.getText() + ", MACHINE NO : " + MACHINE_NO + ", POSITION : " + POSITION);
                                return;
                            }
                        }
                    }

                    Table.setValueAt(search.ReturnVal, Table.getSelectedRow(), 1);
                    mlstrc = Table.getSelectedRow();
                    Table.changeSelection(mlstrc, 1, false, false);
                    int NewRow = Table.getSelectedRow();

                    DataModel.setValueByVariable("MACHINE_NO", Piecedetail[0], NewRow);
                    DataModel.setValueByVariable("POSITION", Piecedetail[1], NewRow);
                    DataModel.setValueByVariable("POSITION_DESC", Piecedetail[2], NewRow);

                    //DataModel.setValueAt(Piecedetail[0], Table.getSelectedRow(), 1);
                    //DataModel.setValueAt(Piecedetail[1], Table.getSelectedRow(), 2);
                    //DataModel.setValueAt(Piecedetail[2], Table.getSelectedRow(), 3);
                    String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + Piecedetail[1]);
                    DataModel.setValueByVariable("UPN", PARTY_CODE.getText() + Piecedetail[0] + Position_Des_No, NewRow);
                    //DataModel.setValueAt(PARTY_CODE.getText()+Piecedetail[0]+Position_Des_No, Table.getSelectedRow(), 4);
                    DataModel.setValueByVariable("PIECE_NO", "", NewRow);
                    //DataModel.setValueAt("", Table.getSelectedRow(), 5);
                    DataModel.setValueByVariable("PRODUCT_CODE", Piecedetail[3], NewRow);
                    //DataModel.setValueAt(, Table.getSelectedRow(), 6);

                    String ITEM_DESC = "", SYN = "", Group_Name = "";
                    try {
                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        String Prod_Code = Piecedetail[3].substring(0, 6);
                        //rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER where PRODUCT_CODE = '" + Prod_Code + "'");
                        rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER,GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + Prod_Code + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");
                        rsData.first();
                        ITEM_DESC = rsData.getString(1);
                        SYN = rsData.getString(2);
                        Piecedetail[4] = rsData.getString(3);
                    } catch (Exception e) {
                        System.out.println("Error : " + e.getMessage());
                    }
                    //
                    DataModel.setValueByVariable("DESCRIPTION", ITEM_DESC, NewRow);
                    //DataModel.setValueAt(ITEM_DESC, Table.getSelectedRow(), 7);
                    DataModel.setValueByVariable("SYN_PER", SYN, NewRow);
                    //DataModel.setValueAt(SYN, Table.getSelectedRow(), 16);
                    DataModel.setValueByVariable("GROUP", Piecedetail[4], NewRow);
                    //DataModel.setValueAt(Piecedetail[4], Table.getSelectedRow(), 8);group

                    if (Piecedetail[4].equals("SDF")) {
                        TableColumn dateColumn = Table.getColumnModel().getColumn(16);
                        JComboBox monthbox = new JComboBox();

                        String month_name = "";
                        Date date = new Date();
                        int month;
                        int year = date.getYear() + 1900;

                        //10/08/2021
                        month = date.getMonth() + 1;

                        //04-12-2021
                        if (EITLERPGLOBAL.getCurrentDay() <= 10) {
                            month = date.getMonth();
                        }
                        
                        for (int i = 0; i < 12; i++) {
                            month = month + 1;

                            if (month >= 13) {
                                month = 1;
                                year = year + 1;
                            }

                            if (month == 1) {
                                month_name = "Jan";
                            } else if (month == 2) {
                                month_name = "Feb";
                            } else if (month == 3) {
                                month_name = "Mar";
                            } else if (month == 4) {
                                month_name = "Apr";
                            } else if (month == 5) {
                                month_name = "May";
                            } else if (month == 6) {
                                month_name = "Jun";
                            } else if (month == 7) {
                                month_name = "Jul";
                            } else if (month == 8) {
                                month_name = "Aug";
                            } else if (month == 9) {
                                month_name = "Sep";
                            } else if (month == 10) {
                                month_name = "Oct";
                            } else if (month == 11) {
                                month_name = "Nov";
                            } else if (month == 12) {
                                month_name = "Dec";
                            }
                            monthbox.addItem(month_name + " - " + year);
                        }

                        dateColumn.setCellEditor(new DefaultCellEditor(monthbox));
                    } else {
                        TableColumn dateColumn = Table.getColumnModel().getColumn(16);
                        JComboBox monthbox = new JComboBox();

                        String month_name = "";
                        Date date = new Date();
                        int month;
                        int year = date.getYear() + 1900;

                        month = date.getMonth() + 2;

                        //04-12-2021
                        if (EITLERPGLOBAL.getCurrentDay() <= 10) {
                            month = date.getMonth()+1;
                        }
                        
                        for (int i = 0; i < 11; i++) {
                            month = month + 1;

                            if (month >= 13) {
                                month = 1;
                                year = year + 1;
                            }

                            if (month == 1) {
                                month_name = "Jan";
                            } else if (month == 2) {
                                month_name = "Feb";
                            } else if (month == 3) {
                                month_name = "Mar";
                            } else if (month == 4) {
                                month_name = "Apr";
                            } else if (month == 5) {
                                month_name = "May";
                            } else if (month == 6) {
                                month_name = "Jun";
                            } else if (month == 7) {
                                month_name = "Jul";
                            } else if (month == 8) {
                                month_name = "Aug";
                            } else if (month == 9) {
                                month_name = "Sep";
                            } else if (month == 10) {
                                month_name = "Oct";
                            } else if (month == 11) {
                                month_name = "Nov";
                            } else if (month == 12) {
                                month_name = "Dec";
                            }
                            monthbox.addItem(month_name + " - " + year);
                        }

                        dateColumn.setCellEditor(new DefaultCellEditor(monthbox));
                    }

                    DataModel.setValueByVariable("LENGTH", Piecedetail[5], NewRow);
                    //DataModel.setValueAt(Piecedetail[5], Table.getSelectedRow(), 9);
                    DataModel.setValueByVariable("WIDTH", Piecedetail[6], NewRow);
                    //DataModel.setValueAt(Piecedetail[6], Table.getSelectedRow(), 10);
                    DataModel.setValueByVariable("GSM", Piecedetail[7], NewRow);
                    //DataModel.setValueAt(Piecedetail[7], Table.getSelectedRow(), 11);

                    //System.out.println("Value = "+Float.parseFloat(Piecedetail[5]) * Float.parseFloat(Piecedetail[6]) * Float.parseFloat(Piecedetail[7])/1000);
                    //System.out.println("Value = "+Float.parseFloat(Piecedetail[5]) * Float.parseFloat(Piecedetail[6]));
//                        DecimalFormat f_single = new DecimalFormat("##.0");
//                        DecimalFormat f_double = new DecimalFormat("##.00");
                    float Theoritical_Weigth = (Float.parseFloat(Piecedetail[5]) * Float.parseFloat(Piecedetail[6]) * Float.parseFloat(Piecedetail[7]) / 1000);

                    float SQMT = (Float.parseFloat(Piecedetail[5]) * Float.parseFloat(Piecedetail[6]));

                    DataModel.setValueByVariable("THORITICAL_WIDTH", EITLERPGLOBAL.round(Theoritical_Weigth, 1) + "", NewRow);
                    //DataModel.setValueAt(EITLERPGLOBAL.round(Theoritical_Weigth, 1), Table.getSelectedRow(), 12);
                    DataModel.setValueByVariable("SQ_MTR", EITLERPGLOBAL.round(SQMT, 2) + "", NewRow);
                    //DataModel.setValueAt(EITLERPGLOBAL.round(SQMT, 2), Table.getSelectedRow(), 13);//sqmtr
                    DataModel.setValueByVariable("STYLE", Piecedetail[8], NewRow);
                    //DataModel.setValueAt(Piecedetail[8], Table.getSelectedRow(), 14);//style

                    DataModel.setValueByVariable("REMARK", "", NewRow);

                    // FeltSalesOrderServiceImpl service = new FeltSalesOrderServiceImpl();
                    FeltInvCalc inv_calc = new FeltInvCalc();

                    try {
                        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                        inv_calc = clsOrderValueCalc.calculate("", Piecedetail[3], PARTY_CODE.getText(), Float.parseFloat(Piecedetail[5]), Float.parseFloat(Piecedetail[6]), Float.parseFloat(EITLERPGLOBAL.round(Theoritical_Weigth, 1) + ""), SQMT, df1.format(df.parse(S_O_DATE.getText())));
                        //calculate(String Piece_No,String Product_Code,String Party_Code,Float Length,Float Width,Integer GSM,Float Weight,Float SQMT,String Order_Date)

                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    DataModel.setValueByVariable("OV_RATE", inv_calc.getFicRate() + "", NewRow);

                    DataModel.setValueByVariable("SURCHARGE_PER", inv_calc.getFicSurcharge_per() + "", NewRow);
                    DataModel.setValueByVariable("SURCHARGE_RATE", inv_calc.getFicSurcharge_rate() + "", NewRow);
                    DataModel.setValueByVariable("GROSS_RATE", inv_calc.getFicGrossRate() + "", NewRow);
                    //DataModel.setValueAt(inv_calc.getFicRate(), Table.getSelectedRow(), 18);
                    DataModel.setValueByVariable("OV_BAS_AMOUNT", inv_calc.getFicBasAmount() + "", NewRow);
                    DataModel.setValueByVariable("OV_CHEM_TRT_CHG", inv_calc.getFicChemTrtChg() + "", NewRow); //19
                    DataModel.setValueByVariable("OV_SPIRAL_CHG", inv_calc.getFicSpiralChg() + "", NewRow); //20
                    DataModel.setValueByVariable("OV_PIN_CHG", inv_calc.getFicPinChg() + "", NewRow); //21
                    DataModel.setValueByVariable("OV_SEAM_CHG", inv_calc.getFicSeamChg() + "", NewRow); //22
                    DataModel.setValueByVariable("OV_INS_IND", inv_calc.getFicInsInd() + "", NewRow); //23
                    DataModel.setValueByVariable("OV_INS_AMT", inv_calc.getFicInsAmt() + "", NewRow); //24
                    DataModel.setValueByVariable("OV_EXCISE", inv_calc.getFicExcise() + "", NewRow); //25
                    DataModel.setValueByVariable("OV_DISC_PER", inv_calc.getFicDiscPer() + "", NewRow); //26
                    DataModel.setValueByVariable("OV_DISC_AMT", inv_calc.getFicDiscAmt() + "", NewRow); //27
                    DataModel.setValueByVariable("OV_DISC_BASAMT", inv_calc.getFicDiscBasamt() + "", NewRow); //28
                    DataModel.setValueByVariable("OV_AMT", inv_calc.getFicInvAmt() + "", NewRow); //18
                    DataModel.setValueByVariable("VAT1", "", NewRow); //19
                    DataModel.setValueByVariable("VAT4", "", NewRow); //20
                    DataModel.setValueByVariable("CST2", "", NewRow); //21
                    DataModel.setValueByVariable("CST5", "", NewRow); //22
                    DataModel.setValueByVariable("CGST_PER", inv_calc.getFicCGSTPER() + "", NewRow); //34
                    DataModel.setValueByVariable("CGST_AMT", inv_calc.getFicCGST() + "", NewRow); //35
                    DataModel.setValueByVariable("SGST_PER", inv_calc.getFicSGSTPER() + "", NewRow); //36
                    DataModel.setValueByVariable("SGST_AMT", inv_calc.getFicSGST() + "", NewRow); //37
                    DataModel.setValueByVariable("IGST_PER", inv_calc.getFicIGSTPER() + "", NewRow); //38
                    DataModel.setValueByVariable("IGST_AMT", inv_calc.getFicIGST() + "", NewRow); //39
                    DataModel.setValueByVariable("COMPOSITION_PER", "0", NewRow); //40
                    DataModel.setValueByVariable("COMPOSITION_AMT", "0", NewRow); //41
                    DataModel.setValueByVariable("RCM_PER", "0", NewRow); //42
                    DataModel.setValueByVariable("RCM_AMT", "0", NewRow); //43
                    DataModel.setValueByVariable("GST_COMPENSATION_CESS_PER", "0", NewRow); //44
                    DataModel.setValueByVariable("GST_COMPENSATION_CESS_AMT", "0", NewRow); //45

                    //DataModel.setValueAt(inv_calc.getFicBasAmount(), Table.getSelectedRow(), 19);
                    //DataModel.setValueAt(inv_calc.getFicChemTrtChg(), Table.getSelectedRow(), 20);
                    //DataModel.setValueAt(inv_calc.getFicSpiralChg(), Table.getSelectedRow(), 21);
                    //DataModel.setValueAt(inv_calc.getFicPinChg(), Table.getSelectedRow(), 22);
                    ///DataModel.setValueAt(inv_calc.getFicSeamChg(), Table.getSelectedRow(), 23);
                    //DataModel.setValueAt(inv_calc.getFicInsInd(), Table.getSelectedRow(), 24);
                    //DataModel.setValueAt(inv_calc.getFicInsAmt(), Table.getSelectedRow(), 25);
                    //DataModel.setValueAt(inv_calc.getFicExcise(), Table.getSelectedRow(), 26);
                    //DataModel.setValueAt(inv_calc.getFicDiscPer(), Table.getSelectedRow(), 27);
                    //DataModel.setValueAt(inv_calc.getFicDiscAmt(), Table.getSelectedRow(), 28);
                    //DataModel.setValueAt(inv_calc.getFicDiscBasamt(), Table.getSelectedRow(), 29);
                    //DataModel.setValueAt(inv_calc.getFicInvAmt(), Table.getSelectedRow(), 30);
                    //          DataModel.setValueAt(inv_calc.getGST(), Table.getSelectedRow(), 34);
                    //DataModel.setValueAt(inv_calc.getFicCGSTPER(), Table.getSelectedRow(), 35);
                    //DataModel.setValueAt(inv_calc.getFicCGST(), Table.getSelectedRow(), 36);
                    //DataModel.setValueAt(inv_calc.getFicSGSTPER(), Table.getSelectedRow(), 37);
                    //DataModel.setValueAt(inv_calc.getFicSGST(), Table.getSelectedRow(), 38);
                    //DataModel.setValueAt(inv_calc.getFicIGSTPER(), Table.getSelectedRow(), 39);
                    //DataModel.setValueAt(inv_calc.getFicIGST(), Table.getSelectedRow(), 40);
                    //DataModel.setValueAt("0", Table.getSelectedRow(), 41);
                    //DataModel.setValueAt("0", Table.getSelectedRow(), 42);
                    //DataModel.setValueAt("0", Table.getSelectedRow(), 43);
                    //DataModel.setValueAt("0", Table.getSelectedRow(), 44);
                    //DataModel.setValueAt("0", Table.getSelectedRow(), 45);
                    //DataModel.setValueAt("0", Table.getSelectedRow(), 46);
                    DataModel.setValueByVariable("PR_BILL_LENGTH", inv_calc.getFicLength() + "", Table.getSelectedRow());
                    DataModel.setValueByVariable("PR_BILL_WIDTH", inv_calc.getFicWidth() + "", Table.getSelectedRow());
                    DataModel.setValueByVariable("PR_BILL_WEIGHT", inv_calc.getFicWeight() + "", Table.getSelectedRow());
                    DataModel.setValueByVariable("PR_BILL_SQMTR", inv_calc.getFicSqMtr() + "", Table.getSelectedRow());
                    DataModel.setValueByVariable("PR_BILL_GSM", Piecedetail[7] + "", Table.getSelectedRow());
                    DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", inv_calc.getFicProductCode() + "", Table.getSelectedRow());
                    DataModel.setValueByVariable("PR_BILL_STYLE", Piecedetail[8], Table.getSelectedRow());
                    String vat1 = "0", vat4 = "0", cst2 = "0", cst5 = "0";

//                        if (PARTY_CODE.getText().startsWith("831")) {
//                            vat1 = (inv_calc.getFicBasAmount() * 1 / 100) + "";
//                            vat4 = (inv_calc.getFicBasAmount() * 4 / 100) + "";
//                        } else {
//                            clsSales_Party objParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText(), "210010");
//                           
//                            if (objParty.getAttribute("TIN_NO").getString() == null) {
//                                cst5 = (inv_calc.getFicBasAmount() * 5 / 100) + "";
//                            } else if (objParty.getAttribute("TIN_NO").getString().equals("")) {
//                                cst5 = (inv_calc.getFicBasAmount() * 5 / 100) + "";
//                            } else {
//                                cst2 = (inv_calc.getFicBasAmount() * 2 / 100) + "";
//                            }
//                        }
                    DataModel.setValueByVariable("VAT1", "", NewRow); //19
                    DataModel.setValueByVariable("VAT4", "", NewRow); //20
                    DataModel.setValueByVariable("CST2", "", NewRow); //21
                    DataModel.setValueByVariable("CST5", "", NewRow); //22
                    //DataModel.setValueAt(vat1, Table.getSelectedRow(), 31);
                    //DataModel.setValueAt(vat4, Table.getSelectedRow(), 32);
                    //DataModel.setValueAt(cst2, Table.getSelectedRow(), 33);
                    //DataModel.setValueAt(cst5, Table.getSelectedRow(), 34);

                    Table.changeSelection(Table.getSelectedRow() + 1, 1, false, false);

                    try {
                        Table.repaint();
                        Table.changeSelection(mlstrc, 1, false, false);
                        Table.repaint();

                    } catch (Exception e) {
                        System.out.println("Error on Table Setting :" + e.getMessage());
                    }

                    Table.requestFocus();
                    cal_order_value();

                    if (Table.getSelectedRow() == DataModel.getRowCount() - 1) {
                        Object[] rowData = new Object[15];
                        rowData[0] = DataModel.getRowCount() + 1;
                        DataModel.addRow(rowData);

                    }
                }
            }
            //}
            //DisplayOldTransaction();    
        } else if (Table.getSelectedColumn() == 62) {
            searchkey search = new searchkey();
            String UPN = DataModel.getValueByVariable("UPN", Table.getSelectedRow());
            //DataModel.setValuDataModeleByVariable("UPN"
            search.SQL = "SELECT DESIGN_REVISION_NO AS RevNo,PRODUCT_CODE AS Prod_Code,PRODUCT_GROUP AS Prod_Group,LENGTH AS Length,WIDTH AS Width,GSM,STYLE AS Style_A,STYLE_MULTILAYER AS Style_B,REED_SPACE AS Reed_Sp_A,REED_SPACE_MULTILAYER AS Reed_Sp_B,WEFT AS Weft,WEFT_MULTILAYER AS Weft_B FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER WHERE APPROVED=1 AND UPN_NO='" + UPN + "' AND COALESCE(DESIGN_ACTIVE_STATUS,0)=0";
            search.ReturnCol = 1;
            search.Party_Code = PARTY_CODE.getText();
            search.ShowReturnCol = true;
            search.DefaultSearchOn = 1;
            //search.setsearchText(minqlt);
            if (search.ShowRSLOV()) {
                if (Table.getCellEditor() != null) {
                    Table.getCellEditor().stopCellEditing();
                }
                seleval = search.ReturnVal;
                seltyp = search.ReturnVal;
                String secondval = search.SecondVal;
                DataModel.setValueByVariable("DM_REVISION_NO", search.ReturnVal, Table.getSelectedRow());
            }
        }

        if (Table.getSelectedColumn() == 45 || Table.getSelectedColumn() == 46 || Table.getSelectedColumn() == 47 || Table.getSelectedColumn() == 48 || Table.getSelectedColumn() == 48 || Table.getSelectedColumn() == 50 || Table.getSelectedColumn() == 51 || Table.getSelectedColumn() == 52) {

            FeltInvCalc inv_calc = new FeltInvCalc();

            String product_code = DataModel.getValueByVariable("PRODUCT_CODE", Table.getSelectedRow());

            System.out.println("length : " + DataModel.getValueByVariable("PR_BILL_LENGTH", Table.getSelectedRow()));
            System.out.println("width : " + DataModel.getValueByVariable("PR_BILL_WIDTH", Table.getSelectedRow()));
            System.out.println("gsm : " + DataModel.getValueByVariable("PR_BILL_GSM", Table.getSelectedRow()));
            float length = Float.parseFloat(DataModel.getValueByVariable("PR_BILL_LENGTH", Table.getSelectedRow()));
            float width = Float.parseFloat(DataModel.getValueByVariable("PR_BILL_WIDTH", Table.getSelectedRow()));
            int gsm = Integer.parseInt(DataModel.getValueByVariable("PR_BILL_GSM", Table.getSelectedRow()));

            float Theoritical_Weigth = ((length * width * gsm) / 1000);

            float SQMT = (length * width);

            try {
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                inv_calc = clsOrderValueCalc.calculate("", product_code, PARTY_CODE.getText(), length, width, Theoritical_Weigth, SQMT, df1.format(df.parse(S_O_DATE.getText())));
                //calculate(String Piece_No,String Product_Code,String Party_Code,Float Length,Float Width,Float Weight,Float SQMT,String Order_Date)
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            int NewRow = Table.getSelectedRow();
            DataModel.setValueByVariable("OV_RATE", inv_calc.getFicRate() + "", NewRow);
            //DataModel.setValueAt(inv_calc.getFicRate(), Table.getSelectedRow(), 18);
            DataModel.setValueByVariable("OV_BAS_AMOUNT", inv_calc.getFicBasAmount() + "", NewRow);
            DataModel.setValueByVariable("OV_CHEM_TRT_CHG", inv_calc.getFicChemTrtChg() + "", NewRow); //19
            DataModel.setValueByVariable("OV_SPIRAL_CHG", inv_calc.getFicSpiralChg() + "", NewRow); //20
            DataModel.setValueByVariable("OV_PIN_CHG", inv_calc.getFicPinChg() + "", NewRow); //21
            DataModel.setValueByVariable("OV_SEAM_CHG", inv_calc.getFicSeamChg() + "", NewRow); //22
            DataModel.setValueByVariable("OV_INS_IND", inv_calc.getFicInsInd() + "", NewRow); //23
            DataModel.setValueByVariable("OV_INS_AMT", inv_calc.getFicInsAmt() + "", NewRow); //24
            DataModel.setValueByVariable("OV_EXCISE", inv_calc.getFicExcise() + "", NewRow); //25
            DataModel.setValueByVariable("OV_DISC_PER", inv_calc.getFicDiscPer() + "", NewRow); //26
            DataModel.setValueByVariable("OV_DISC_AMT", inv_calc.getFicDiscAmt() + "", NewRow); //27
            DataModel.setValueByVariable("OV_DISC_BASAMT", inv_calc.getFicDiscBasamt() + "", NewRow); //28
            DataModel.setValueByVariable("OV_AMT", inv_calc.getFicInvAmt() + "", NewRow); //18
            DataModel.setValueByVariable("VAT1", "", NewRow); //19
            DataModel.setValueByVariable("VAT4", "", NewRow); //20
            DataModel.setValueByVariable("CST2", "", NewRow); //21
            DataModel.setValueByVariable("CST5", "", NewRow); //22
            DataModel.setValueByVariable("CGST_PER", inv_calc.getFicCGSTPER() + "", NewRow); //34
            DataModel.setValueByVariable("CGST_AMT", inv_calc.getFicCGST() + "", NewRow); //35
            DataModel.setValueByVariable("SGST_PER", inv_calc.getFicSGSTPER() + "", NewRow); //36
            DataModel.setValueByVariable("SGST_AMT", inv_calc.getFicSGST() + "", NewRow); //37
            DataModel.setValueByVariable("IGST_PER", inv_calc.getFicIGSTPER() + "", NewRow); //38
            DataModel.setValueByVariable("IGST_AMT", inv_calc.getFicIGST() + "", NewRow); //39
            DataModel.setValueByVariable("COMPOSITION_PER", "0", NewRow); //40
            DataModel.setValueByVariable("COMPOSITION_AMT", "0", NewRow); //41
            DataModel.setValueByVariable("RCM_PER", "0", NewRow); //42
            DataModel.setValueByVariable("RCM_AMT", "0", NewRow); //43
            DataModel.setValueByVariable("GST_COMPENSATION_CESS_PER", "0", NewRow); //44
            DataModel.setValueByVariable("GST_COMPENSATION_CESS_AMT", "0", NewRow); //45

            DataModel.setValueByVariable("PR_BILL_LENGTH", inv_calc.getFicLength() + "", Table.getSelectedRow());
            DataModel.setValueByVariable("PR_BILL_WIDTH", inv_calc.getFicWidth() + "", Table.getSelectedRow());
            DataModel.setValueByVariable("PR_BILL_WEIGHT", inv_calc.getFicWeight() + "", Table.getSelectedRow());
            DataModel.setValueByVariable("PR_BILL_SQMTR", inv_calc.getFicSqMtr() + "", Table.getSelectedRow());
            DataModel.setValueByVariable("PR_BILL_GSM", gsm + "", Table.getSelectedRow());
            DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", inv_calc.getFicProductCode() + "", Table.getSelectedRow());

            cal_order_value();
        }
    }//GEN-LAST:event_TableKeyPressed
    private void cal_order_value() {
        float inv_amt = 0;
        for (int i = 0; i < Table.getRowCount(); i++) {
            if (!Table.getValueAt(i, 31).toString().equals("")) {
                inv_amt = inv_amt + Float.parseFloat(Table.getValueAt(i, 31).toString());
            }
        }
        ORDER_VALUE.setText(inv_amt + "");
    }

//    private void DisplayOldTransaction() {
////        for (int i = 0; i < DataModelOldTransaction.getRowCount(); i++) {
////            DataModelOldTransaction.removeRow(i);
////        }
//        while (DataModelOldTransaction.getRowCount() > 0) {
//            DataModelOldTransaction.removeRow(0);
//        }
//
//        int j = 0;
//        String[][] MachinePosition = new String[100][2];
//
//        for (int i = 0; i < Table.getRowCount(); i++) {
//
//            if (!Table.getValueAt(i, 1).toString().equals("")) {
//
//                MachinePosition[j][0] = Table.getValueAt(i, 1).toString();
//                MachinePosition[j][1] = Table.getValueAt(i, 2).toString();
//                //System.out.println("Machine No : "+MachinePosition[j][0]+" Position : "+MachinePosition[j][1]);
//                j++;
//            }
//
//        }
//
//        String Party = PARTY_CODE.getText();
//        String order_date = EITLERPGLOBAL.formatDateDB(S_O_DATE.getText());
//
//        if (!"".equals(Party) && !"".equals(MachinePosition[0][0])) {
//
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.DATE, -30);
//            String lastDate = dateForDB.format(cal.getTime());
//
//            lblPartyCode.setText(Party);
//            lblFrom.setText(EITLERPGLOBAL.formatDate(lastDate));
//            lblTo.setText(EITLERPGLOBAL.getCurrentDate());
//
//            if (!"".equals(order_date)) {
//                int i = 0;
//                String MachinePositionQuery = "";
//                for (; i < j; i++) {
//                    MachinePositionQuery = MachinePositionQuery + "(D.MACHINE_NO = '" + MachinePosition[i][0] + "' AND D.POSITION = '" + MachinePosition[i][1] + "')OR";
//                }
//                if (i > 0) {
//                    try {
//                        Connection connection = data.getConn();
//                        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//                        MachinePositionQuery = "(" + MachinePositionQuery.substring(0, MachinePositionQuery.length() - 2) + ")";
//
//                        String query = "SELECT H.S_ORDER_NO,H.S_ORDER_DATE,H.CREATED_BY,H.PARTY_CODE,H.PARTY_NAME,H.SALES_ENGINEER,H.REFERENCE,H.REFERENCE_DATE,H.P_O_NO,H.P_O_DATE,D.* FROM  PRODUCTION.FELT_SALES_ORDER_HEADER H,PRODUCTION.FELT_SALES_ORDER_DETAIL D WHERE H.S_ORDER_NO = D.S_ORDER_NO AND H.PARTY_CODE = '" + Party + "' AND " + MachinePositionQuery + " AND H.S_ORDER_DATE >= '" + lastDate + "'";
//                        System.out.println("Query : " + query);
//                        ResultSet resultSet = statement.executeQuery(query);
//                        int srNo = 0;
//                        while (resultSet.next()) {
//
//                            srNo++;
//                            int NewRow = srNo - 1;
//
//                            Object[] rowData = new Object[1];
//                            DataModelOldTransaction.addRow(rowData);
//
//                            DataModelOldTransaction.setValueByVariable("SrNo", srNo + "", NewRow);
//                            DataModelOldTransaction.setValueByVariable("S_ORDER_NO", resultSet.getString("S_ORDER_NO"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("S_ORDER_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("S_ORDER_DATE")), NewRow);
//                            DataModelOldTransaction.setValueByVariable("ORDER_BY", clsUser.getUserName(EITLERPGLOBAL.gCompanyID, Long.parseLong(resultSet.getString("CREATED_BY"))), NewRow);
//                            DataModelOldTransaction.setValueByVariable("PARTY_CODE", resultSet.getString("PARTY_CODE"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("PARTY_NAME", resultSet.getString("PARTY_NAME"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("SALES_ENGINEER", resultSet.getString("SALES_ENGINEER"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("REFERENCE", resultSet.getString("REFERENCE"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("REFERENCE_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("REFERENCE_DATE")), NewRow);
//                            DataModelOldTransaction.setValueByVariable("P_O_NO", resultSet.getString("P_O_NO"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("P_O_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("P_O_DATE")), NewRow);
//
//                            DataModelOldTransaction.setValueByVariable("MACHINE_NO", resultSet.getString("MACHINE_NO"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("POSITION", resultSet.getString("POSITION"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("PIECE_NO", resultSet.getString("PIECE_NO"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("PRODUCT_CODE", resultSet.getString("PRODUCT_CODE"), NewRow);
//
//                            Connection Conn;
//                            Statement stmt;
//                            ResultSet rsData;
//
//                            String ITEM_DESC = "", SYN = "";
//
//                            Conn = data.getConn();
//                            stmt = Conn.createStatement();
//                            String Prod_Code = resultSet.getString("PRODUCT_CODE").substring(0, 6);
//                            rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER where PRODUCT_CODE = '" + Prod_Code + "'");
//                            //System.out.println("SELECT PRODUCT_DESC,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER where PRODUCT_CODE = '" + Prod_Code + "'");
//                            rsData.first();
//                            ITEM_DESC = rsData.getString(1);
//                            SYN = rsData.getString(2);
//
//                            DataModelOldTransaction.setValueByVariable("DESCRIPTION", ITEM_DESC, NewRow);
//                            DataModelOldTransaction.setValueByVariable("GROUP", resultSet.getString("S_GROUP"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("LENGTH", resultSet.getString("LENGTH"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("WIDTH", resultSet.getString("WIDTH"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("GSM", resultSet.getString("GSM"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("THORITICAL_WIDTH", resultSet.getString("THORITICAL_WIDTH"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("SQ_MTR", resultSet.getString("SQ_MTR"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("STYLE", resultSet.getString("STYLE"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("REQ_MONTH", resultSet.getString("REQ_MONTH"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("SYN", resultSet.getString("SYN_PER"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("REMARK", resultSet.getString("REMARK"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("OV_RATE", resultSet.getString("OV_RATE"), NewRow);
//                            DataModelOldTransaction.setValueByVariable("OV_BAS_AMOUNT", resultSet.getString("OV_BAS_AMOUNT"), NewRow); //18
//                            DataModelOldTransaction.setValueByVariable("OV_CHEM_TRT_CHG", resultSet.getString("OV_CHEM_TRT_CHG"), NewRow); //19
//                            DataModelOldTransaction.setValueByVariable("OV_SPIRAL_CHG", resultSet.getString("OV_SPIRAL_CHG"), NewRow); //20
//                            DataModelOldTransaction.setValueByVariable("OV_PIN_CHG", resultSet.getString("OV_PIN_CHG"), NewRow); //21
//                            DataModelOldTransaction.setValueByVariable("OV_SEAM_CHG", resultSet.getString("OV_SEAM_CHG"), NewRow); //22
//                            DataModelOldTransaction.setValueByVariable("OV_INS_IND", resultSet.getString("OV_INS_IND"), NewRow); //23
//                            DataModelOldTransaction.setValueByVariable("OV_INS_AMT", resultSet.getString("OV_INS_AMT"), NewRow); //24
//                            DataModelOldTransaction.setValueByVariable("OV_EXCISE", resultSet.getString("OV_EXCISE"), NewRow); //25
//                            DataModelOldTransaction.setValueByVariable("OV_DISC_PER", resultSet.getString("OV_DISC_PER"), NewRow); //26
//                            DataModelOldTransaction.setValueByVariable("OV_DISC_AMT", resultSet.getString("OV_DISC_AMT"), NewRow); //27
//                            DataModelOldTransaction.setValueByVariable("OV_DISC_BASAMT", resultSet.getString("OV_DISC_BASAMT"), NewRow); //28
//                            DataModelOldTransaction.setValueByVariable("OV_AMT", resultSet.getString("OV_AMT"), NewRow); //18
//                            DataModelOldTransaction.setValueByVariable("VAT1", "0", NewRow); //19
//                            DataModelOldTransaction.setValueByVariable("VAT4", "0", NewRow); //20
//                            DataModelOldTransaction.setValueByVariable("CST2", "0", NewRow); //21
//                            DataModelOldTransaction.setValueByVariable("CST5", "0", NewRow); //22
//                            DataModelOldTransaction.setValueByVariable("CGST_PER", resultSet.getString("CGST_PER"), NewRow); //34
//                            DataModelOldTransaction.setValueByVariable("CGST_AMT", resultSet.getString("CGST_AMT"), NewRow); //35
//                            DataModelOldTransaction.setValueByVariable("SGST_PER", resultSet.getString("SGST_PER"), NewRow); //36
//                            DataModelOldTransaction.setValueByVariable("SGST_AMT", resultSet.getString("SGST_AMT"), NewRow); //37
//                            DataModelOldTransaction.setValueByVariable("IGST_PER", resultSet.getString("IGST_PER"), NewRow); //38
//                            DataModelOldTransaction.setValueByVariable("IGST_AMT", resultSet.getString("IGST_AMT"), NewRow); //39
//                            DataModelOldTransaction.setValueByVariable("COMPOSITION_PER", resultSet.getString("COMPOSITION_PER"), NewRow); //40
//                            DataModelOldTransaction.setValueByVariable("COMPOSITION_AMT", resultSet.getString("COMPOSITION_AMT"), NewRow); //41
//                            DataModelOldTransaction.setValueByVariable("RCM_PER", resultSet.getString("RCM_PER"), NewRow); //42
//                            DataModelOldTransaction.setValueByVariable("RCM_AMT", resultSet.getString("RCM_AMT"), NewRow); //43
//                            DataModelOldTransaction.setValueByVariable("GST_COMPENSATION_CESS_PER", resultSet.getString("GST_COMPENSATION_CESS_PER"), NewRow); //44
//                            DataModelOldTransaction.setValueByVariable("GST_COMPENSATION_CESS_AMT", resultSet.getString("GST_COMPENSATION_CESS_AMT"), NewRow); //45
//                        }
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }
//
//    }
    private void PARTY_CODEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PARTY_CODEFocusLost
        lblStatus1.setText("");
        PARTY_NAME.setText("");
        S_ENGINEER.setText("");
        REGION.setText("");
        CITY.setText("");
        DISTRICT.setText("");
        COUNTRY.setText("");
        //cmdAdd.doClick();
        P_O_NO.requestFocus();
        txtEmailId.setText("");
        txtEmail2.setText("");
        txtEmail3.setText("");
        txtContactPerson.setText("");
        txtPhoneNo.setText("");
        try {

//            if (!"".equals(PARTY_CODE.getText())) {
            if (!"".equals(PARTY_CODE.getText()) && data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + PARTY_CODE.getText() + "' AND PARTY_CLOSE_IND!=1 ")) {
//                clsSales_Party objParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText(), "210010");
//
//                PARTY_NAME.setText(objParty.getAttribute("PARTY_NAME").getString());
//                REGION.setText(objParty.getAttribute("ZONE").getString());
//                CITY.setText(objParty.getAttribute("CITY_ID").getString());
//                DISTRICT.setText(objParty.getAttribute("DISTRICT").getString());
//                COUNTRY.setText(objParty.getAttribute("COUNTRY_ID").getString());
//               
                String check_lock = data.getStringValueFromDB("SELECT COALESCE(PARTY_LOCK, 0) AS PARTY_LOCK FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='" + PARTY_CODE.getText() + "'");

                if ("1".equals(check_lock)) {
                    JOptionPane.showMessageDialog(null, "Party Code : " + PARTY_CODE.getText() + " is locked.");
                    PARTY_CODE.requestFocus();
                    return;
                }
                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    // System.out.println("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='"+PARTY_CODE.getText()+"' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
                    //rsData = stmt.executeQuery("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,A.INCHARGE_CD,COALESCE(A.GSTIN_NO,'') AS GSTIN_NO,COALESCE(A.STATE_GST_CODE,'') AS STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B WHERE PARTY_CODE='" + PARTY_CODE.getText() + "' AND A.COUNTRY_ID=B.COUNTRY_ID");
                    System.out.println("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,A.INCHARGE_CD,COALESCE(A.GSTIN_NO,'') AS GSTIN_NO,A.TAGGING_APPROVAL_IND,A.EMAIL,A.PHONE_NO,A.CONTACT_PERSON,A.EMAIL_ID2,A.EMAIL_ID3 FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B WHERE PARTY_CODE='" + PARTY_CODE.getText() + "' AND A.COUNTRY_ID=B.COUNTRY_ID");
                    rsData = stmt.executeQuery("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,A.INCHARGE_CD,COALESCE(A.GSTIN_NO,'') AS GSTIN_NO,A.TAGGING_APPROVAL_IND,A.EMAIL,A.PHONE_NO,A.CONTACT_PERSON,A.EMAIL_ID2,A.EMAIL_ID3 FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B WHERE PARTY_CODE='" + PARTY_CODE.getText() + "' AND A.COUNTRY_ID=B.COUNTRY_ID");

                    rsData.first();

                    //if ((rsData.getString(7).equals("") || rsData.getString(8).equals("")) && PARTY_CODE.getText().startsWith("8")) {
                    if (rsData.getString(7).equals("") && PARTY_CODE.getText().startsWith("8")) {
                        // JOptionPane.showMessageDialog(null, "Party Code : " + PARTY_CODE.getText() + ", GSTIN NO or STATE GST CODE is not updated in PARTY MASTER");
                        JOptionPane.showMessageDialog(null, "Party Code : " + PARTY_CODE.getText() + ", GSTIN NO is not updated in PARTY MASTER");
                    } else {
                        CITY.setText(rsData.getString(1));
                        DISTRICT.setText(rsData.getString(2));
                        COUNTRY.setText(rsData.getString(3));
                        REGION.setText(rsData.getString(4));
                        PARTY_NAME.setText(rsData.getString(5));
                        //S_ENGINEER.setText(rsData.getString(6));
                        S_ENGINEER.setText(rsData.getString(6));
                        lblInchargeName.setText(clsSales_Party.getFeltInchargeName(Long.parseLong(rsData.getString(6))));
                        txtEmailId.setText(rsData.getString(9));
                        txtPhoneNo.setText(rsData.getString(10));
                        txtContactPerson.setText(rsData.getString(11));
                        txtEmail2.setText(rsData.getString("EMAIL_ID2"));
                        txtEmail3.setText(rsData.getString("EMAIL_ID3"));
                        /*if (PARTY_CODE.getText().startsWith("8")) {
                         String Query_BUDGET = "SELECT * FROM PRODUCTION.FELT_BUDGET where PARTY_CODE='" + PARTY_CODE.getText() + "' AND YEAR_FROM='" + EITLERPGLOBAL.getCurrentFinYear() + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'";
                         System.out.println("Query_BUDGET : " + Query_BUDGET);

                         if (!data.IsRecordExist(Query_BUDGET)) {
                         JOptionPane.showMessageDialog(this, "Please update BUDGET first to enter order of PARTY : " + PARTY_CODE.getText());
                         return;
                         }
                         }*/

                        if (rsData.getString("TAGGING_APPROVAL_IND").equals("3")) {
                            chkbox_TenderParty.setSelected(true);
                        } else {
                            chkbox_TenderParty.setSelected(false);
                        }
                        FormatGrid_prevoiusData();
                        setPrevoiousData();

                        //SELECT PR_PIECE_NO AS 'PIECE_NO',PR_UPN AS 'UPN',PR_GROUP AS 'GROUP',PR_OC_MONTHYEAR AS 'OC_MONTH',PR_FNSG_DATE AS 'WAREHOUSE_DATE',PR_INVOICE_DATE  AS 'DISPATCHED_DATE',PR_OC_LAST_DDMMYY FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='"+PARTY_CODE+"' AND PR_PIECE_STAGE='INVOICED' ORDER BY PR_INVOICE_DATE DESC LIMIT 10
                        setPreviousInvoice();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error onm fetch data for D_SAL_PARTY_MASTER " + e.getMessage());
                    return;
                }
                if (!PARTY_NAME.getText().equals("")) {
//                        Object[] options = {"Yes","No"};
//                        int reply = JOptionPane.showOptionDialog(this, "Do you want to update Machine Master?", 
//                                                        "Confirmation", 
//                                                        JOptionPane.YES_NO_OPTION, 
//                                                        JOptionPane.WARNING_MESSAGE, 
//                                                        null, options, options[1]);
//                        if (reply == JOptionPane.YES_OPTION)
//                        { 
                    //comment on 13-10-17
                    //                         if(EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT)
                    //                         {
                    //                             
                    //                                AppletFrame aFrame = new AppletFrame("Machine Master Amendment");
                    //                                aFrame.startAppletEx("EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend", "Machine Master Amendment");
                    //                                frmmachinesurveyAmend ObjItem = (frmmachinesurveyAmend) aFrame.ObjApplet;
                    //                                ObjItem.requestFocus();
                    //                                ObjItem.Add();
                    //                                ObjItem.AddFromOrder(PARTY_CODE.getText(),"01");
                    //                                ObjItem.machine_lostfocus();
                    //                                
                    //                         }      
                    // }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter valid party code");
                    PARTY_CODE.requestFocus();
                    return;
                }
                //Auto Filter for Hierarchy as depend on selection of PARTY
                filterHierarchyCombo();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter valid PARTY CODE");
            PARTY_CODE.requestFocus();
        }

    }//GEN-LAST:event_PARTY_CODEFocusLost


    private void PARTY_CODEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PARTY_CODEKeyPressed

        PARTY_NAME.setText("");
        S_ENGINEER.setText("");
        REGION.setText("");
        CITY.setText("");
        DISTRICT.setText("");
        COUNTRY.setText("");

        int i = Table.getRowCount();
        while (i > 0) {
            DataModel.removeRow(i - 1);
            i--;
        }

        if (evt.getKeyChar() == KeyEvent.VK_TAB || evt.getKeyChar() == 10) {
            P_O_NO.requestFocus();
        } else if (evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

//                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE='210010'";
                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CLOSE_IND!=1 ";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    PARTY_CODE.setText(aList.ReturnVal);

                    String check_lock = data.getStringValueFromDB("SELECT COALESCE(PARTY_LOCK, 0) AS PARTY_LOCK FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='" + PARTY_CODE.getText() + "'");

                    if ("1".equals(check_lock)) {
                        JOptionPane.showMessageDialog(null, "Party Code : " + PARTY_CODE.getText() + " is locked.");
                        PARTY_CODE.requestFocus();
                        return;
                    }

                    clsSales_Party objParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, aList.ReturnVal, "210010");
                    PARTY_NAME.setText(objParty.getAttribute("PARTY_NAME").getString());
                    REGION.setText(objParty.getAttribute("ZONE").getString());
                    S_ENGINEER.setText(objParty.getAttribute("INCHARGE_CD").getString());
                    CITY.setText(objParty.getAttribute("CITY_ID").getString());
                    DISTRICT.setText(objParty.getAttribute("DISTRICT").getString());
                    COUNTRY.setText(objParty.getAttribute("COUNTRY_ID").getString());

                    if (!S_ENGINEER.getText().equals("6")) {
                        rdoAdvancePayment.setSelected(false);
                        rdoOtherPayment.setSelected(false);
                        rdoAdvancePayment.setEnabled(false);
                        rdoOtherPayment.setEnabled(false);
                    }

                    if (!PARTY_NAME.equals("")) {
//                        Object[] options = {"Yes", "No"};
//                        int reply = JOptionPane.showOptionDialog(this, "Do you want to update Machine Master?",
//                                "Confirmation",
//                                JOptionPane.YES_NO_OPTION,
//                                JOptionPane.WARNING_MESSAGE,
//                                null, options, options[1]);
//                        if (reply == JOptionPane.YES_OPTION) {
//                                AppletFrame.startApplet("EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend","Machine Master Amendment");    
//                            AppletFrame aFrame = new AppletFrame("Machine Master Amendment");
//                            aFrame.startAppletEx("EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend", "Machine Master Amendment");
//                            frmmachinesurveyAmend ObjItem = (frmmachinesurveyAmend) aFrame.ObjApplet;
//                            //ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText());
//                            ObjItem.requestFocus();
//                            ObjItem.Add();
//                            ObjItem.AddFromOrder(PARTY_CODE.getText());
//                        }
                    }
                }

                P_O_NO.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }
        }
    }//GEN-LAST:event_PARTY_CODEKeyPressed

    private void PARTY_CODEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PARTY_CODEKeyTyped


    }//GEN-LAST:event_PARTY_CODEKeyTyped

    private void REMOVEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REMOVEActionPerformed
        if (Table.getRowCount() > 0) {
            DataModel.removeRow(Table.getSelectedRow());
            // DisplayIndicators();

            //DataModel.getValueAt(FFNo, WIDTH)
        }
    }//GEN-LAST:event_REMOVEActionPerformed

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

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
    private void GenerateRejectedSendToCombo() {
        HashMap hmRejectedSendToList = new HashMap();

        cmbSendToModel = new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbSendToModel);
        DOC_NO = S_O_NO.getText();
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
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);

        GenerateRejectedSendToCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void OpgRejectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpgRejectItemStateChanged

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

    private void PARTY_CODEFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PARTY_CODEFocusGained
        // TODO add your handling code here:
        lblStatus1.setText("Select PARTY... Press F1");
    }//GEN-LAST:event_PARTY_CODEFocusGained

    private void P_O_NOFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_P_O_NOFocusGained
        // TODO add your handling code here:
        lblStatus1.setText("Enter P. O. Number");
    }//GEN-LAST:event_P_O_NOFocusGained

    private void P_O_NOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_P_O_NOFocusLost
        // TODO add your handling code here:
        lblStatus1.setText("");
    }//GEN-LAST:event_P_O_NOFocusLost

    private void P_O_DATEFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_P_O_DATEFocusGained
        // TODO add your handling code here:
        lblStatus1.setText("Enter P. O. Date in DD/MM/YYYY formate only");
    }//GEN-LAST:event_P_O_DATEFocusGained

    private void P_O_DATEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_P_O_DATEFocusLost
        // TODO add your handling code here:
        lblStatus1.setText("");

    }//GEN-LAST:event_P_O_DATEFocusLost

    private void REF_DATEFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REF_DATEFocusGained
        // TODO add your handling code here:
        lblStatus1.setText("Please enter Reference Date in DD/MM/YYYY formate only");
    }//GEN-LAST:event_REF_DATEFocusGained

    private void REF_DATEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REF_DATEFocusLost
        // TODO add your handling code here:
        lblStatus1.setText("");
        if (EITLERPGLOBAL.formatDateDB(REF_DATE.getText()) == null) {
            JOptionPane.showMessageDialog(this, "Enter Valid Reference Date.", "ERROR", JOptionPane.ERROR_MESSAGE);
            REF_DATE.setText("");
            REF_DATE.requestFocus();
        }
    }//GEN-LAST:event_REF_DATEFocusLost

    private void REMARKFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REMARKFocusGained
        // TODO add your handling code here:
        lblStatus1.setText("Enter Order Remark");
    }//GEN-LAST:event_REMARKFocusGained

    private void REMARKFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REMARKFocusLost
        // TODO add your handling code here:
        lblStatus1.setText("");
    }//GEN-LAST:event_REMARKFocusLost

    private void TableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusGained
        // TODO add your handling code here:
        if (Table.getSelectedColumn() == 1) {
            lblStatus1.setText("Press F1 for selection Machine No and Position No");
        } else {
            lblStatus1.setText("");
        }
    }//GEN-LAST:event_TableFocusGained

    private void TableAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_TableAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_TableAncestorMoved

    private void TableCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_TableCaretPositionChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_TableCaretPositionChanged

    private void TableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusLost
        // TODO add your handling code here:if(Table.getSelectedColumn() == 1)
        lblStatus1.setText("");
    }//GEN-LAST:event_TableFocusLost

    private void P_O_NOKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_P_O_NOKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            P_O_DATE.requestFocus();
        }
    }//GEN-LAST:event_P_O_NOKeyPressed

    private void P_O_DATEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_P_O_DATEKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            REF_DATE.requestFocus();
        }
    }//GEN-LAST:event_P_O_DATEKeyPressed

    private void REF_DATEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_REF_DATEKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            REMARK.requestFocus();
        }
    }//GEN-LAST:event_REF_DATEKeyPressed

    private void REMARKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_REMARKKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Table.requestFocus();
        }
    }//GEN-LAST:event_REMARKKeyPressed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        try {
            Object[] rowData = new Object[15];
            rowData[0] = DataModel.getRowCount() + 1;
            DataModel.addRow(rowData);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_cmdAddActionPerformed

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:
        //jdbc:mysql://200.0.0.230:3306/PRODUCTION

    }//GEN-LAST:event_TableMouseClicked

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        ReportShow();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmdPrintActionPerformed
    private void ReportShow() {

        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData = new TReportWriter.SimpleDataProvider.TTable();

            objReportData.AddColumn("S_ORDER_NO");
            objReportData.AddColumn("S_ORDER_DATE");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("PARTY_NAME");
            objReportData.AddColumn("REGION");
            objReportData.AddColumn("MACHINE_NO");
            objReportData.AddColumn("POSITION");
            objReportData.AddColumn("PIECE_NO");
            objReportData.AddColumn("PRODUCT_CODE");
            objReportData.AddColumn("S_GROUP");
            objReportData.AddColumn("SYN_PER");
            objReportData.AddColumn("LENGTH");
            objReportData.AddColumn("WIDTH");
            objReportData.AddColumn("GSM");
            objReportData.AddColumn("THORITICAL_WIDTH");
            objReportData.AddColumn("SQ_MTR");
            objReportData.AddColumn("STYLE");
            objReportData.AddColumn("REQ_MONTH");
            objReportData.AddColumn("CITY_ID");
            objReportData.AddColumn("DISTRICT");
            objReportData.AddColumn("COUNTRY_NAME");
            objReportData.AddColumn("OV_RATE");
            objReportData.AddColumn("OV_AMT");
            objReportData.AddColumn("P_O_NO");
            objReportData.AddColumn("P_O_DATE");
            objReportData.AddColumn("REFERENCE");
            objReportData.AddColumn("REFERENCE_DATE");
            objReportData.AddColumn("REMARK");
            objReportData.AddColumn("SR_NO");

            TReportWriter.SimpleDataProvider.TRow objOpeningRow = objReportData.newRow();

            objOpeningRow.setValue("S_ORDER_NO", "");
            objOpeningRow.setValue("S_ORDER_DATE", "");
            objOpeningRow.setValue("PARTY_CODE", "");
            objOpeningRow.setValue("PARTY_NAME", "");
            objOpeningRow.setValue("REGION", "");
            objOpeningRow.setValue("MACHINE_NO", "");
            objOpeningRow.setValue("POSITION", "");
            objOpeningRow.setValue("PIECE_NO", "");
            objOpeningRow.setValue("PRODUCT_CODE", "");
            objOpeningRow.setValue("S_GROUP", "");
            objOpeningRow.setValue("SYN_PER", "");
            objOpeningRow.setValue("LENGTH", "");
            objOpeningRow.setValue("WIDTH", "");
            objOpeningRow.setValue("GSM", "");
            objOpeningRow.setValue("THORITICAL_WIDTH", "");
            objOpeningRow.setValue("SQ_MTR", "");
            objOpeningRow.setValue("STYLE", "");
            objOpeningRow.setValue("REQ_MONTH", "");
            objOpeningRow.setValue("CITY_ID", "");
            objOpeningRow.setValue("DISTRICT", "");
            objOpeningRow.setValue("COUNTRY_NAME", "");
            objOpeningRow.setValue("OV_RATE", "");
            objOpeningRow.setValue("OV_AMT", "");
            objOpeningRow.setValue("P_O_NO", "");
            objOpeningRow.setValue("P_O_DATE", "");
            objOpeningRow.setValue("REFERENCE", "");
            objOpeningRow.setValue("REFERENCE_DATE", "");
            objOpeningRow.setValue("REMARK", "");
            objOpeningRow.setValue("SR_NO", "");

            String query_str = "";

            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
            DateFormat df1 = new SimpleDateFormat("yyyy-mm-dd");

            DOC_NO = S_O_NO.getText();

            //String strSQL="SELECT A.PKG_BALE_NO,A.PKG_BALE_DATE,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME,PKG_TRANSPORT_MODE, A.PKG_STATION,A.PKG_BOX_SIZE, B.PKG_PIECE_NO,B.PKG_LENGTH,B.PKG_WIDTH,B.PKG_GSM,B.PKG_SQM,B.PKG_ORDER_NO, B.PKG_ORDER_DATE,B.PKG_MCN_POSITION_DESC,B.PKG_STYLE,B.PKG_SYN_PER,B.PKG_PRODUCT_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_PARTY_CODE='"+txtPartyCode.getText().trim()+"' GROUP BY A.PKG_PARTY_CODE;";
            //String strSQL="SELECT A.S_ORDER_DATE,A.PARTY_CODE,substr(A.PARTY_NAME,1,25) AS PARTY_NAME,A.REGION, B.MACHINE_NO,B.POSITION,B.PIECE_NO,B.PRODUCT_CODE,B.S_GROUP,B.SYN_PER,B.LENGTH,B.WIDTH,B.GSM,B.OV_RATE,B.OV_AMT  FROM PRODUCTION.FELT_SALES_ORDER_HEADER A,PRODUCTION.FELT_SALES_ORDER_DETAIL B where A.APPROVED=1 AND B.PIECE_NO!=''  AND A.PARTY_CODE!='' "+query_str+" and A.S_ORDER_NO = B.S_ORDER_NO";
            String strSQL = "SELECT  A.S_ORDER_NO, A.S_ORDER_DATE, A.REGION, A.SALES_ENGINEER, A.REGION, "
                    + "A.PARTY_CODE, A.PARTY_NAME, A.REFERENCE, A.REFERENCE_DATE, A.P_O_NO, "
                    + "A.P_O_DATE, A.REMARK, A.CREATED_BY, A.CREATED_DATE, A.APPROVED, B.SR_NO, "
                    + "B.MACHINE_NO, B.POSITION, B.POSITION_DESC, B.PIECE_NO, B.PRODUCT_CODE, "
                    + "B.PRODUCT_DESC, B.S_GROUP, B.LENGTH, B.WIDTH, B.GSM, B.THORITICAL_WIDTH, "
                    + "B.SQ_MTR, B.STYLE, B.REQ_MONTH, B.SYN_PER, B.OV_RATE, B.OV_AMT, PM.CITY_ID, "
                    + "PM.DISTRICT, CM.COUNTRY_NAME FROM PRODUCTION.FELT_SALES_ORDER_HEADER A, PRODUCTION.FELT_SALES_ORDER_DETAIL B, DINESHMILLS.D_SAL_PARTY_MASTER PM, DINESHMILLS.D_SAL_COUNTRY_MASTER CM   where  A.S_ORDER_NO = B.S_ORDER_NO AND   A.PARTY_CODE = PM.PARTY_CODE AND  PM.COUNTRY_ID = CM.COUNTRY_ID AND   B.S_ORDER_NO = '" + DOC_NO + "'";
            System.out.println(strSQL);

            ResultSet rsTmp = data.getResult(strSQL);
            rsTmp.first();

            int Counter = 0;

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    Counter++;
                    objRow = objReportData.newRow();

                    objRow.setValue("S_ORDER_NO", UtilFunctions.getString(rsTmp, "S_ORDER_NO", ""));
                    objRow.setValue("S_ORDER_DATE", UtilFunctions.getString(rsTmp, "S_ORDER_DATE", ""));
                    objRow.setValue("PARTY_CODE", UtilFunctions.getString(rsTmp, "PARTY_CODE", ""));
                    objRow.setValue("PARTY_NAME", UtilFunctions.getString(rsTmp, "PARTY_NAME", ""));
                    objRow.setValue("REGION", UtilFunctions.getString(rsTmp, "REGION", ""));
                    objRow.setValue("MACHINE_NO", UtilFunctions.getString(rsTmp, "MACHINE_NO", ""));
                    objRow.setValue("POSITION", UtilFunctions.getString(rsTmp, "POSITION", ""));
                    objRow.setValue("PIECE_NO", UtilFunctions.getString(rsTmp, "PIECE_NO", ""));
                    objRow.setValue("PRODUCT_CODE", UtilFunctions.getString(rsTmp, "PRODUCT_CODE", ""));
                    objRow.setValue("S_GROUP", UtilFunctions.getString(rsTmp, "S_GROUP", ""));
                    objRow.setValue("SYN_PER", UtilFunctions.getString(rsTmp, "SYN_PER", ""));
                    objRow.setValue("LENGTH", UtilFunctions.getString(rsTmp, "LENGTH", ""));
                    objRow.setValue("WIDTH", UtilFunctions.getString(rsTmp, "WIDTH", ""));
                    objRow.setValue("GSM", UtilFunctions.getString(rsTmp, "GSM", ""));
                    objRow.setValue("THORITICAL_WIDTH", UtilFunctions.getString(rsTmp, "THORITICAL_WIDTH", ""));
                    objRow.setValue("SQ_MTR", UtilFunctions.getString(rsTmp, "SQ_MTR", ""));
                    objRow.setValue("STYLE", UtilFunctions.getString(rsTmp, "STYLE", ""));
                    objRow.setValue("REQ_MONTH", UtilFunctions.getString(rsTmp, "REQ_MONTH", ""));
                    objRow.setValue("CITY_ID", UtilFunctions.getString(rsTmp, "CITY_ID", ""));
                    objRow.setValue("DISTRICT", UtilFunctions.getString(rsTmp, "DISTRICT", ""));
                    objRow.setValue("COUNTRY_NAME", UtilFunctions.getString(rsTmp, "COUNTRY_NAME", ""));
                    objRow.setValue("OV_RATE", UtilFunctions.getString(rsTmp, "OV_RATE", ""));
                    objRow.setValue("OV_AMT", UtilFunctions.getString(rsTmp, "OV_AMT", ""));
                    objRow.setValue("P_O_NO", UtilFunctions.getString(rsTmp, "P_O_NO", ""));
                    objRow.setValue("P_O_DATE", UtilFunctions.getString(rsTmp, "P_O_DATE", ""));
                    objRow.setValue("REFERENCE", UtilFunctions.getString(rsTmp, "REFERENCE", ""));
                    objRow.setValue("REFERENCE_DATE", UtilFunctions.getString(rsTmp, "REFERENCE_DATE", ""));
                    objRow.setValue("REMARK", UtilFunctions.getString(rsTmp, "REMARK", ""));
                    objRow.setValue("SR_NO", UtilFunctions.getString(rsTmp, "SR_NO", ""));

                    objReportData.AddRow(objRow);

                    rsTmp.next();
                }
            }

            int Comp_ID = EITLERPGLOBAL.gCompanyID;

            HashMap Parameters = new HashMap();
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
            //System.out.println("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptFeltOrder.rpt");
            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/FeltSales/FELTORDER.rpt", Parameters, objReportData);

        } catch (Exception e) {
            System.out.println("Error : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void REFERENCEItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_REFERENCEItemStateChanged
        // TODO add your handling code here:
        if (REFERENCE.getSelectedItem().equals("P.O.")) {
            P_O_NO.setText("");
            P_O_DATE.setText("__/__/____");
            P_O_NO.setEditable(true);
            P_O_DATE.setEnabled(true);
            Tab.add(jPanel7);
            Tab.setTitleAt(Tab.indexOfComponent(jPanel7), "Attachment");
        }
        else if (REFERENCE.getSelectedItem().equals("Email")) {
            //P_O_NO.setText("");
            //P_O_DATE.setText("__/__/____");
            //P_O_NO.setEditable(true);
            //P_O_DATE.setEnabled(true);
            P_O_NO.setText("");
            P_O_DATE.setText("00/00/0000");
            P_O_NO.setEditable(false);
            P_O_DATE.setEnabled(false);
            Tab.add(jPanel7);
            Tab.setTitleAt(Tab.indexOfComponent(jPanel7), "Attachment");
        }
        else {
            P_O_NO.setText("");
            P_O_DATE.setText("00/00/0000");
            P_O_NO.setEditable(false);
            P_O_DATE.setEnabled(false);
            Tab.remove(jPanel7);
        }
    }//GEN-LAST:event_REFERENCEItemStateChanged

    private void txtPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPositionActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        setPrevoiousData();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void OpgRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpgRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpgRejectActionPerformed

    private void Table_prevoiusDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_prevoiusDataKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_Table_prevoiusDataKeyPressed

    private void btnSendFAmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFAmailActionPerformed
        // TODO add your handling code here:
        //System.out.println("Sel Hierarchy : "+SelHierarchyID);

        System.out.println("approved = " + feltOrder.getAttribute("APPROVED").getString());

        if (feltOrder.getAttribute("APPROVED").getInt() == 1) {
            int value = JOptionPane.showConfirmDialog(this, " Are you sure? You want to send Final Approved mail to all users? ", "Confirmation Alert!", JOptionPane.YES_NO_OPTION);
            System.out.println("VALUE = " + value);
            if (value == 0) {
                try {
                    String DOC_NO = S_O_NO.getText();
                    String DOC_DATE = S_O_DATE.getText();
                    String Party_Code = PARTY_CODE.getText();
                    int Hierarchy = feltOrder.getAttribute("HIERARCHY_ID").getInt();

                    System.out.println("ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true " + ModuleId + "," + DOC_NO + "," + DOC_DATE + "," + Party_Code + "," + EITLERPGLOBAL.gNewUserID + "," + Hierarchy + "," + true);
                    System.out.println("Final Approved By : " + FinalApprovedBy);
                    String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, FinalApprovedBy, Hierarchy, true, EITLERPGLOBAL.gNewUserID);
                    System.out.println("Send Mail Responce : " + responce);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }//GEN-LAST:event_btnSendFAmailActionPerformed

    private void tbl_version_infoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_version_infoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_version_infoKeyPressed

    private void txtContactPersonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContactPersonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactPersonActionPerformed

    private void txtEmailIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailIdActionPerformed

    private void REMARKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REMARKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_REMARKActionPerformed

    private void txtOrderMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderMonthActionPerformed

    private void txtOA_NOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOA_NOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOA_NOActionPerformed

    private void chbEmailUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbEmailUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chbEmailUpdateActionPerformed

    private void tblPreviousInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreviousInvoiceMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPreviousInvoiceMouseClicked

    private void tblPreviousInvoiceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPreviousInvoiceFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPreviousInvoiceFocusGained

    private void tblPreviousInvoiceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPreviousInvoiceFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPreviousInvoiceFocusLost

    private void tblPreviousInvoiceCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblPreviousInvoiceCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPreviousInvoiceCaretPositionChanged

    private void tblPreviousInvoiceAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblPreviousInvoiceAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPreviousInvoiceAncestorMoved

    private void tblPreviousInvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPreviousInvoiceKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPreviousInvoiceKeyPressed

    private void rdoAdvancePaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoAdvancePaymentMouseClicked
        // TODO add your handling code here:
        if (rdoAdvancePayment.isSelected() && EITLERPGLOBAL.gUserDeptID == 27 && EditMode == EITLERPGLOBAL.EDIT) {
            txtPaymentDate.setEditable(true);
            txtPaymentRemark.setEditable(true);
        }
    }//GEN-LAST:event_rdoAdvancePaymentMouseClicked

    private void rdoOtherPaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoOtherPaymentMouseClicked
        // TODO add your handling code here:\
        if (rdoOtherPayment.isSelected() && EITLERPGLOBAL.gUserDeptID == 27 && EditMode == EITLERPGLOBAL.EDIT) {
            txtPaymentDate.setEditable(false);
            txtPaymentDate.setText("");
            txtPaymentRemark.setEditable(true);
        }
    }//GEN-LAST:event_rdoOtherPaymentMouseClicked

    private void btnShowDesignMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowDesignMasterActionPerformed
        // TODO add your handling code here:
        int row = Table.getSelectedRow();

        String UPN = DataModel.getValueByVariable("UPN", row);
        String RevisionNo = DataModel.getValueByVariable("DM_REVISION_NO", row);

        if (RevisionNo != "") {

            String Doc_No = data.getStringValueFromDB("SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER where UPN_NO='" + UPN + "' AND DESIGN_REVISION_NO='" + RevisionNo + "'");
            System.out.println("SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER where UPN_NO='" + UPN + "' AND DESIGN_REVISION_NO='" + RevisionNo + "'");
            if (Doc_No != "") {
                AppletFrame aFrame = new AppletFrame("Felt Design Master");
                aFrame.startAppletEx("EITLERP.FeltSales.FeltDesignMaster.FrmFeltDesignMaster", "Felt Design Master");
                FrmFeltDesignMaster ObjItem = (FrmFeltDesignMaster) aFrame.ObjApplet;

                ObjItem.requestFocus();
                ObjItem.FindByDocNo(Doc_No);
            } else {
                JOptionPane.showMessageDialog(this, "Design Master not found!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Design Revision number not selected.");

        }
    }//GEN-LAST:event_btnShowDesignMasterActionPerformed

    private void cmdAddPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddPOActionPerformed
        Object[] rowData = new Object[12];
        rowData[0] = Integer.toString(DocTable.getRowCount() + 1);
        rowData[1] = "";
        rowData[2] = P_O_NO.getText().toString().trim();
        rowData[3] = "UPLOAD";
        rowData[4] = "VIEW";
        rowData[5] = "";

        DataModelDoc.addRow(rowData);
        DocTable.changeSelection(DocTable.getRowCount() - 1, 1, false, false);
        DocTable.requestFocus();
    }//GEN-LAST:event_cmdAddPOActionPerformed

    private void cmdRemovePOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemovePOActionPerformed
        if (DocTable.getRowCount() > 0) {
            data.Execute("DELETE FROM DOC_MGMT.SALES_ORDER_PO_ATTACHMENT "
                    + "WHERE DOCUMENT_DOC_NO='" + S_O_NO.getText().toString().trim() + "' AND "
                    + "DOCUMENT_SR_NO=" + DocTable.getValueAt(DocTable.getSelectedRow(), 0));
            data.Execute("UPDATE DOC_MGMT.SALES_ORDER_PO_ATTACHMENT "
                    + "SET DOCUMENT_SR_NO=DOCUMENT_SR_NO-1"
                    + "WHERE DOCUMENT_DOC_NO='" + S_O_NO.getText().toString().trim() + "' AND "
                    + "DOCUMENT_SR_NO>" + DocTable.getValueAt(DocTable.getSelectedRow(), 0));
            DataModelDoc.removeRow(DocTable.getSelectedRow());
            RearrangeSrNo();
        }
    }//GEN-LAST:event_cmdRemovePOActionPerformed

    private void txtPersonPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPersonPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPersonPositionActionPerformed

    private void txtContectPerson_DineshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContectPerson_DineshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContectPerson_DineshActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int row = Table.getSelectedRow();

        String UPN = DataModel.getValueByVariable("UPN", row);
        String PIECE_NO = DataModel.getValueByVariable("PIECE_NO", row);
        String RevisionNo = DataModel.getValueByVariable("DM_REVISION_NO", row);
        String REQ_MONTH = DataModel.getValueByVariable("REQ_MONTH", row);
        String LayerType = DataModel.getValueByVariable("LAYER_TYPE", row);
        //WITHOUT_AB
        //WITH_AB
        
        if (!"".equals(PIECE_NO) && !"".equals(RevisionNo) && !"".equals(REQ_MONTH) && !"".equals(UPN)) {     
            
            Connection Conn = null;
           
            try {
                Conn = data.getConn();
                

                HashMap parameterMap = new HashMap();
                parameterMap.put("CONTEXT", "http://200.0.0.227:8080/SDMLERP/");

              
                
                try{
                    String ddRemark = "";
                    ddRemark = data.getStringValueFromDB("SELECT COALESCE(MM_REMARK_GENERAL,'') AS DD_REMARK FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H, PRODUCTION.FELT_MACHINE_MASTER_DETAIL D WHERE H.MM_DOC_NO=D.MM_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0 AND D.MM_UPN_NO='"+UPN+"' ");
                    parameterMap.put("DD_REMARK", ddRemark);
                    
                    String Design_Doc_No  = data.getStringValueFromDB("SELECT DESIGN_DOC_NO FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER WHERE UPN_NO='"+UPN+"' AND DESIGN_REVISION_NO="+RevisionNo+"");
                    String mfgLength  = data.getStringValueFromDB("SELECT COALESCE(MFG_LENGTH,'0') AS MFG_LENGTH FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER WHERE UPN_NO='"+UPN+"' AND DESIGN_REVISION_NO="+RevisionNo+"");
                    
                    String faceWebTotal = data.getStringValueFromDB("SELECT SUM(REPLACE(FACE_WEB,'*','')+0) AS TOTAL FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE FACE_WEB REGEXP '[0-9]' AND DESIGN_DOC_NO='" + Design_Doc_No + "'");
                    String faceNoWebTotal = data.getStringValueFromDB("SELECT SUM(REPLACE(FACE_NO_WEB,'*','')+0) AS TOTAL2 FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE FACE_NO_WEB REGEXP '[0-9]' AND DESIGN_DOC_NO='" + Design_Doc_No + "'");
                    String backWebTotal = data.getStringValueFromDB("SELECT SUM(REPLACE(BACK_WEB,'*','')+0) AS TOTAL FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE BACK_WEB REGEXP '[0-9]' AND DESIGN_DOC_NO='" + Design_Doc_No + "'");
                    String backNoWebTotal = data.getStringValueFromDB("SELECT SUM(REPLACE(BACK_NO_WEB,'*','')+0) AS TOTAL FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE BACK_NO_WEB REGEXP '[0-9]' AND DESIGN_DOC_NO='" + Design_Doc_No + "'");
                    String takeUpAvg = data.getStringValueFromDB("SELECT ROUND(AVG(COALESCE(TAKE_UP,0)),2) AS AVG_TACKUP FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE DESIGN_DOC_NO='" + Design_Doc_No + "'");
                    
                    double formulaNo = 0;
                    formulaNo = Double.parseDouble(faceWebTotal)+Double.parseDouble(faceNoWebTotal)+Double.parseDouble(backWebTotal)+Double.parseDouble(backNoWebTotal);
                    formulaNo = formulaNo*Double.parseDouble(takeUpAvg);
                    formulaNo = formulaNo*Double.parseDouble(mfgLength);
                    formulaNo = formulaNo/Double.parseDouble("18.11");
                    formulaNo = EITLERPGLOBAL.round(formulaNo, 2);
                    

                    ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE DESIGN_DOC_NO='"+Design_Doc_No+"' ORDER BY SR_NO+1");
                    
                    rsData.first();
                    int i=1;
                    while(!rsData.isAfterLast())
                    {
                        parameterMap.put("WEB"+i, rsData.getString("FACE_WEB"));
                        parameterMap.put("NOWEB"+i, rsData.getString("FACE_NO_WEB"));
                        parameterMap.put("BWEB"+i, rsData.getString("BACK_WEB"));
                        parameterMap.put("BNOWEB"+i, rsData.getString("BACK_NO_WEB"));
                        parameterMap.put("TAKEUP"+i, rsData.getString("TAKE_UP"));
                        parameterMap.put("PANETRATION"+i, rsData.getString("PENETRATION"));
                        i=i+1;
                        rsData.next();
                    }
                    rsData.close();
                    
                    parameterMap.put("WEB17", faceWebTotal);
                    parameterMap.put("NOWEB17", faceNoWebTotal);
                    parameterMap.put("BWEB17", backWebTotal);
                    parameterMap.put("BNOWEB17", backNoWebTotal);
                    parameterMap.put("TAKEUP17", takeUpAvg);
                    parameterMap.put("PANETRATION17", String.valueOf(formulaNo));
                }catch(Exception e)
                { e.printStackTrace(); }
                
                
                ReportRegister rpt = new ReportRegister(parameterMap, Conn);
                
                String strSQL = "SELECT DISTINCT\n" +
                        "    DDD.*, WARP_TXT, WEFT_TXT, MM_PAPER_GRADE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_MACHINE_SPEED_RANGE\n" +
                        "FROM\n" +
                        "    (SELECT \n" +
                        "        H.DESIGN_DOC_NO,\n" +
                        "            DESIGN_DOC_DATE,\n" +
                        "            DESIGN_REVISION_NO,\n" +
                        "            UPN_NO,\n" +
                        "            PARTY_CODE,\n" +
                        "            PARTY_NAME,\n" +
                        "            MACHINE_NO,\n" +
                        "            H.POSITION_NO,\n" +
                        "            PM.POSITION_DESC,\n" +
                        "            H.POSITION_DESIGN_NO,\n" +
                        "            REFERENCE,\n" +
                        "            PRESS_CATEGORY,\n" +
                        "            H.STYLE,\n" +
                        "            H.PRODUCT_GROUP,\n" +
                        "            H.PRODUCT_CODE,\n" +
                        "            LENGTH,\n" +
                        "            WIDTH,\n" +
                        "            GSM,\n" +
                        "            SQMTR,\n" +
                        "            WEIGHT,\n" +
                        "            REASON_OF_REVISION,\n" +
                        "            WEAVE,\n" +
                        "            H.WEFT,\n" +
                        "            H.ENDS_10CM,\n" +
                        "            H.PICKS_10CM,\n" +
                        "            WIDTH_FACT,\n" +
                        "            WARP,\n" +
                        "            NO_ENDS,\n" +
                        "            H.REED,\n" +
                        "            H.REED_SPACE,\n" +
                        "            DRAW,\n" +
                        "            H.SYN,\n" +
                        "            GSM_ORD,\n" +
                        "            GSM_MFG,\n" +
                        "            W_WGT,\n" +
                        "            WE_WGT,\n" +
                        "            TK_UP,\n" +
                        "            H.THEO_WEIGHT,\n" +
                        "            THEO_LENGTH,\n" +
                        "            THEO_PICKS,\n" +
                        "            LENGTH_FACT,\n" +
                        "            END_LENGTH,\n" +
                        "            T_THICK,\n" +
                        "            THEO_CFM,\n" +
                        "            BASE_SKG_TOTAL,\n" +
                        "            H.TOTAL_SKG,\n" +
                        "            TRIM_WEIGHT,\n" +
                        "            WEIGHT_RANGE,\n" +
                        "            BILL_WEIGHT,\n" +
                        "            KILLOS,\n" +
                        "            FACE_SINGLE,\n" +
                        "            BACK_SINGLE,\n" +
                        "            PER_COUNT,\n" +
                        "            WEAVING_INSTRUCTION,\n" +
                        "            DRYER_WIDTH_MARK_WET_DRY,\n" +
                        "            TAG_INSTRUCTION,\n" +
                        "            FINISHING_INSTRUCTION,\n" +
                        "            NEEDLING_INSTRUCTION,\n" +
                        "            DRESS_DRAW_WEAVING_INSTRUCTION,\n" +
                        "            BASE_GSM,\n" +//
                        "            BASE_GSM_A,\n" +
                        "            BASE_GSM_B,\n" +
                        "            WEB_GSM,\n" +
                        "            TOTAL_GSM,\n" +
                        "            SAFETY,\n" +
                        "            NEEDLING_TANTION,\n" +
                        "            PER_WEB,\n" +
                        "            PER_SYN_BASE,\n" +
                        "            PER_SYN_WEB,\n" +
                        "            WEB_ON_FACE,\n" +
                        "            WEB_ON_BACK,\n" +
                        "            PAINT_LINES,\n" +
                        "            LOOM_NO,\n" +
                        "            NEEDLING_TEN_FACT,\n" +
                        "            TW_BE_ND,\n" +//,
                        "            TW_BE_ND_A,\n" +
                        "            TW_BE_ND_B,\n" +
                        "            T_WEB_WEIGHT,\n" +
                        "            T_TOTAL_WEIGHT,\n" +
                        "            REMARK,\n" +
                        "            MFG_LENGTH,\n" +
                        "            MKG_WIDTH,\n" +
                        "            MFG_WIDTH,\n" +
                        "            D.SR_NO,\n" +
                        "            FACE_WEB,\n" +
                        "            FACE_NO_WEB,\n" +
                        "            BACK_WEB,\n" +
                        "            BACK_NO_WEB,\n" +
                        "            D.TAKE_UP,\n" +
                        "            PENETRATION\n" +
                        "    FROM\n" +
                        "        PRODUCTION.FELT_DESIGN_MASTER_HEADER H, PRODUCTION.FELT_DESIGN_MASTER_DETAIL D\n" +
                        "    LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PM ON PM.POSITION_NO = POSITION_NO\n" +
                        "    WHERE\n" +
                        "        D.DESIGN_DOC_NO = H.DESIGN_DOC_NO\n" +
                        "            AND H.UPN_NO = '"+UPN+"'\n" +
                        "            AND H.DESIGN_REVISION_NO = '"+RevisionNo+"'\n" +
                        "            AND H.POSITION_NO = PM.POSITION_NO) AS DDD\n" +
                        "        LEFT JOIN\n" +
                        "    PRODUCTION.FELT_DESIGN_PARAMETER_MASTER P ON P.PRODUCT_STYLE = DDD.STYLE\n" +
                        //"        AND P.PRODUCT_CODE = DDD.PRODUCT_CODE\n" +
                        "        AND P.DESIGN = DDD.WEAVE\n" +
                        "        AND P.WEFT_W1 = DDD.WEFT\n" +
                        "        AND P.PICKS_10CM_P1 = DDD.PICKS_10CM\n" +
                        "        LEFT JOIN PRODUCTION.FELT_MACHINE_MASTER_HEADER M \n" +
                        "        ON  DDD.PARTY_CODE = M.MM_PARTY_CODE\n" +
                        "        AND DDD.MACHINE_NO = M.MM_MACHINE_NO\n" +
                        "        \n" +
                        "\n" +
                        "		";

                    
                
                
                ReportRegister rpt2 = new ReportRegister(parameterMap, Conn);
                String str_2ndlayer = "SELECT DISTINCT\n" +
                        "    DDD.*, WARP_TXT, WEFT_TXT, MM_PAPER_GRADE,MM_PAPER_GSM_RANGE,MM_MACHINE_TYPE_PRESSING,MM_MACHINE_SPEED_RANGE\n" +
                        "FROM\n" +
                        "    (SELECT \n" +
                        "        H.*,\n" +
                        "            PM.POSITION_DESC,\n" +
                        "            D.SR_NO,\n" +
                        "            D.FACE_WEB,\n" +
                        "            D.FACE_NO_WEB,\n" +
                        "            D.BACK_WEB,\n" +
                        "            D.BACK_NO_WEB,\n" +
                        "            D.TAKE_UP,\n" +
                        "            D.PENETRATION\n" +
                        "    FROM\n" +
                        "        PRODUCTION.FELT_DESIGN_MASTER_HEADER H, PRODUCTION.FELT_DESIGN_MASTER_DETAIL D\n" +
                        "    LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PM ON PM.POSITION_NO = POSITION_NO\n" +
                        "    WHERE\n" +
                        "        D.DESIGN_DOC_NO = H.DESIGN_DOC_NO\n" +
                        "            AND H.UPN_NO = '"+UPN+"'\n" +
                        "            AND H.DESIGN_REVISION_NO = '"+RevisionNo+"'\n" +
                        "            AND H.POSITION_NO = PM.POSITION_NO) AS DDD\n" +
                        "        LEFT JOIN\n" +
                        "    PRODUCTION.FELT_DESIGN_PARAMETER_MASTER P ON P.PRODUCT_STYLE = DDD.STYLE_MULTILAYER\n" +
                        "        AND P.PRODUCT_CODE = DDD.PRODUCT_CODE_MULTILAYER\n" +
                        "        AND P.DESIGN = DDD.WEAVE_MULTILAYER\n" +
                        "        AND P.WEFT_W1 = DDD.WEFT_MULTILAYER\n" +
                        "        AND P.PICKS_10CM_P1 = DDD.PICKS_10CM_MULTILAYER\n" +
                        "        LEFT JOIN PRODUCTION.FELT_MACHINE_MASTER_HEADER M \n" +
                        "        ON  DDD.PARTY_CODE = M.MM_PARTY_CODE\n" +
                        "        AND DDD.MACHINE_NO = M.MM_MACHINE_NO";
                
                
                
                //WITHOUT_AB
                //WITH_AB
                
                if(LayerType.equals("WITH_AB"))
                {
                    parameterMap.put("PIECE_NO", ""+PIECE_NO+"-A");
                    parameterMap.put("REQ_MONTH", ""+REQ_MONTH);
                
                    //SO2122001332
                    rpt.setReportName("/EITLERP/FeltSales/Order/Design_Report_new.jrxml", 1, strSQL); //productlist is the name of my jasper file.
                    rpt.callReport();

                    parameterMap.put("PIECE_NO", ""+PIECE_NO+"-B");
                    rpt2.setReportName("/EITLERP/FeltSales/Order/Design_Report_new_2ndLayer.jrxml", 1, str_2ndlayer); //productlist is the name of my jasper file.
                    rpt2.callReport();
                }
                else if(LayerType.equals("WITHOUT_AB"))
                {
                     parameterMap.put("PIECE_NO", ""+PIECE_NO);
                     parameterMap.put("REQ_MONTH", ""+REQ_MONTH);
                     
                    rpt.setReportName("/EITLERP/FeltSales/Order/Design_Report_new.jrxml", 1, strSQL); //productlist is the name of my jasper file.
                    rpt.callReport();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed
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

    private void Add() {
        //  EditMode=EITLERPGLOBAL.ADD;

        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }

        EditMode = EITLERPGLOBAL.ADD;

        DisableToolbar();
        SetFields(true);
        cmdAddPO.setEnabled(true);
        cmdRemovePO.setEnabled(true);
        SetupApproval();
        lblTitle.setBackground(new Color(0, 102, 153));
        lblTitle.setForeground(Color.WHITE);
        clearFields();
        PARTY_CODE.requestFocus();

        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 602;
        aList.FirstFreeNo = 212;

        S_O_DATE.setText(df.format(new Date()));
        FFNo = aList.FirstFreeNo;
        S_O_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, false));
        lblTitle.setText("Felt Sales Order - " + S_O_NO.getText());
        cmdAdd.setEnabled(true);
        REMOVE.setEnabled(true);
        REFERENCEItemStateChanged(null);
        Old_Piece.setEnabled(true);
        Old_Piece.setSelected(false);
        chkbox_TenderParty.setEnabled(false);
        chkbox_TenderParty.setSelected(false);
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.Order.frmFindFeltOrder", true);
        frmFindFeltOrder ObjFindFeltorder = (frmFindFeltOrder) ObjLoader.getObj();

        if (ObjFindFeltorder.Cancelled == false) {
            if (!feltOrder.Filter(ObjFindFeltorder.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.", "Find Felt Rate", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    // find rate update by doc no
    public void Find(String docNo) {
        feltOrder.Filter(" S_ORDER_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    public void FindWaiting() {
        //     feltOrder.Filter(" AND PROD_DOC_NO IN (SELECT DISTINCT PROD_DOC_NO FROM PRODUCTION.FELT_PROD_DATA, PRODUCTION.FELT_PROD_DOC_DATA WHERE PROD_DOC_NO=DOC_NO AND USER_ID="+EITLERPGLOBAL.gNewUserID+" AND STATUS='W' AND MODULE_ID="+ModuleId+" AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    private void Save() {

        SDF_list.clear();
        NONSDF_list.clear();
        int selHie = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        if (PARTY_CODE.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Valid PARTY CODE.", "ERROR", JOptionPane.ERROR_MESSAGE);
            PARTY_CODE.requestFocus();
            return;
        }

        if (EditMode == EITLERPGLOBAL.ADD) {

            int today_date = EITLERPGLOBAL.getCurrentDay();

//            if (today_date >= 26 && today_date <= 31) {
//                JOptionPane.showMessageDialog(this, "On 26th to 31st, You cannot take new order.", "ERROR", JOptionPane.ERROR_MESSAGE);
//                return;
//            }

            System.out.println("Selected Hierarchy  : " + selHie);
            HashMap hmSendToList = new HashMap();
            hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, selHie, EITLERPGLOBAL.gNewUserID);
            ArrayList<Integer> user_list = new ArrayList<>();
            for (int i = 1; i <= hmSendToList.size(); i++) {
                clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));

                int user_id = (int) ObjUser.getAttribute("USER_ID").getVal();
                user_list.add(user_id);
            }

            if (S_ENGINEER.getText().equals("6")) {
                if (user_list.contains(98)) {
                    System.out.println("Ready with Rakesh Dalal");
                } else {
                    JOptionPane.showMessageDialog(null, "For Export Party, Select Hierarchy with Rakesh Dalal.");
                    return;
                }

            }
        }

        if (!txtPaymentDate.getText().equals("")) {
            String DateDb = EITLERPGLOBAL.formatDateDB(txtPaymentDate.getText());
            if (DateDb.equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter valid date");
                txtPaymentDate.requestFocus();
                return;
            }
        }

        if (S_ENGINEER.getText().equals("6") && EITLERPGLOBAL.gUserDeptID == 27 && !rdoAdvancePayment.isSelected() && !rdoOtherPayment.isSelected() && (!OpgReject.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please select payment option for EXPORT party");
            return;
        }

        if (S_ENGINEER.getText().equals("6") && EITLERPGLOBAL.gUserDeptID == 27 && rdoAdvancePayment.isSelected() && txtPaymentRemark.getText().equals("") && (!OpgReject.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please enter export party payment remark for Advance payment.");
            return;
        }

        String check_lock = data.getStringValueFromDB("SELECT COALESCE(PARTY_LOCK, 0) AS PARTY_LOCK FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='" + PARTY_CODE.getText() + "'");
        if ("1".equals(check_lock)) {
            JOptionPane.showMessageDialog(null, "Party Code : " + PARTY_CODE.getText() + " is locked.");
            PARTY_CODE.requestFocus();
            return;
        }

        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + PARTY_CODE.getText() + "' ")) {
            JOptionPane.showMessageDialog(null, "Party closed in Party Master.");
            return;
        }

        if (Table.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(this, "Enter Piece Updation Details Before Saving.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (REFERENCE.getSelectedItem().equals("P.O.") && P_O_NO.getText().equals("") && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "Enter P O NO.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (REFERENCE.getSelectedItem().equals("P.O.") && (EITLERPGLOBAL.formatDateDB(P_O_DATE.getText()).equals("") || P_O_DATE.getText().equals("00/00/0000") || P_O_DATE.getText().equals("") || EITLERPGLOBAL.formatDateDB(P_O_DATE.getText()) == null)  && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "Enter P O DATE.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (REFERENCE.getSelectedItem().equals("P.O.") && DocTable.getRowCount() == 0  && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "Upload P O Attachment Document.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (REFERENCE.getSelectedItem().equals("P.O.") && DocTable.getRowCount() > 0) {
            for (int i = 0; i < DocTable.getRowCount(); i++) {
                data.Execute("UPDATE DOC_MGMT.SALES_ORDER_PO_ATTACHMENT SET DOC_TYPE='"+P_O_NO.getText().trim()+"' WHERE DOCUMENT_DOC_NO='"+S_O_NO.getText().trim()+"' ");
                if (DataModelDoc.getValueAt(i, 5).toString().trim().equals("")) {
                    JOptionPane.showMessageDialog(this, "File not Uploaded at row : " + (i + 1) + ".", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (REFERENCE.getSelectedItem().equals("Verbal") && txtContactPerson.getText().equals("") && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "For Verbal Communication, Contact Person is compulsory", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (REFERENCE.getSelectedItem().equals("Verbal") && txtPersonPosition.getText().equals("") && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "For Verbal Communication, Position of Person is compulsory", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (REFERENCE.getSelectedItem().equals("Verbal") && txtPhoneNo.getText().equals("") && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "For Verbal Communication, Phone No of Person is compulsory", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (REFERENCE.getSelectedItem().equals("Verbal") && txtContectPerson_Dinesh.getText().equals("") && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "For Verbal Communication, Contacted By is compulsory", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (P_O_DATE.getText().equals("__/__/____") && !OpgReject.isSelected()) {
            P_O_DATE.setText("00/00/0000");
        }
        if (REF_DATE.getText().equals("__/__/____") || EITLERPGLOBAL.formatDateDB(REF_DATE.getText()).equals("")  && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "Enter Reference Date.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (EITLERPGLOBAL.formatDateDB(REF_DATE.getText()) == null) {
            JOptionPane.showMessageDialog(this, "Enter Valid Reference Date.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if ("".equals(EITLERPGLOBAL.formatDateDB(REF_DATE.getText()))  && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "Enter Valid Reference Date.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtEmailId.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Email Id.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (OpgApprove.isSelected() && EITLERPGLOBAL.gUserDeptID == 39) {
            //All Piece are compulsory 
            boolean piece_no_not_found = false;
            for (int i = 0; i < Table.getRowCount(); i++) {

                if (!Table.getValueAt(i, 1).toString().equals("") || !Table.getValueAt(i, 2).toString().equals("")) {
                    if (Table.getValueAt(i, 5).toString().equals("")) {
                        piece_no_not_found = true;
                    }
                }
            }
            if (piece_no_not_found) {
                JOptionPane.showMessageDialog(null, "Piece No is compulsory in design for approval");
                return;
            }
        }

        for (int j = 0; j < Table.getRowCount(); j++) {

            String machineNo = ((String) Table.getValueAt(j, 1)).trim();
            String positionNo = ((String) Table.getValueAt(j, 2)).trim();

            if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MACHINE_CLOSE_IND=1 AND MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND MM_MACHINE_NO='" + machineNo + "' ")) {
                JOptionPane.showMessageDialog(null, "Party Machine closed in Machine Master at Row : " + (j + 1));
                return;
            } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE POSITION_CLOSE_IND=1 AND MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "' ")) {
                JOptionPane.showMessageDialog(null, "Party Machine Position closed in Machine Master at Row : " + (j + 1));
                return;
            }

            String check_lock_MACHINE = data.getStringValueFromDB("SELECT COALESCE(MACHINE_LOCK_IND, 0) AS PARTY_LOCK FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND MM_MACHINE_NO='" + machineNo + "'");
            if ("1".equals(check_lock_MACHINE)) {
                JOptionPane.showMessageDialog(null, "Machine : " + machineNo + " is locked for Party Code : " + PARTY_CODE.getText() + ".");
                PARTY_CODE.requestFocus();
                return;
            }
            String check_lock_POSITION = data.getStringValueFromDB("SELECT COALESCE(POSITION_LOCK_IND, 0) AS POSITION_LOCK_IND FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "'");
            if ("1".equals(check_lock_POSITION)) {
                JOptionPane.showMessageDialog(null, "Machine : " + machineNo + " AND Position : " + positionNo + " is locked for Party Code : " + PARTY_CODE.getText() + ".");
                PARTY_CODE.requestFocus();
                return;
            }

            String Req_Month = DataModel.getValueByVariable("REQ_MONTH", j);
            if ("".equals(Req_Month) && (OpgFinal.isSelected() || OpgApprove.isSelected())) {
                JOptionPane.showMessageDialog(null, "Requesed Month is compulsory");
                return;
            }

            if (!OpgReject.isSelected()) {
                String MMAMENDPending = data.getStringValueFromDB("SELECT MM_AMEND_NO FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER WHERE MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND MM_MACHINE_NO+0=" + machineNo + " AND APPROVED=0 AND CANCELED=0");
                if (!MMAMENDPending.equals("")) {
                    JOptionPane.showMessageDialog(null, " Machine Master Amend Pending for Party Code " + PARTY_CODE.getText() + " and Machine No " + machineNo + " with AMEND No : " + MMAMENDPending + "");
                    return;
                }
            }

            /*
             String DateSlot = DataModel.getValueByVariable("DATE_SLOT", j);
             if ("".equals(DateSlot) && (OpgFinal.isSelected() || OpgApprove.isSelected())) {
             JOptionPane.showMessageDialog(null, "Date Slot for Requested Month is compulsory");
             return;
             }*/
            if (chkbox_TenderParty.isSelected()) {
                String TenderWeight = DataModel.getValueByVariable("TENDER_WEIGHT", j);
                if (("".equals(TenderWeight) || "0.00".equals(TenderWeight) || "0.0".equals(TenderWeight) || "0".equals(TenderWeight)) && (OpgFinal.isSelected() || OpgApprove.isSelected() || OpgHold.isSelected())) {
                    JOptionPane.showMessageDialog(null, "TenderWeight is compulsory for Tender Party");
                    return;
                }
                String TenderGSM = DataModel.getValueByVariable("TENDER_GSM", j);
                if (("".equals(TenderGSM) || "0.00".equals(TenderGSM) || "0.0".equals(TenderGSM) || "0".equals(TenderGSM)) && (OpgFinal.isSelected() || OpgApprove.isSelected())) {
                    JOptionPane.showMessageDialog(null, "TenderGSM is compulsory for Tender Party");
                    return;
                }
            }

            String Query = "SELECT D.PIECE_NO FROM PRODUCTION.FELT_SALES_ORDER_HEADER H,PRODUCTION.FELT_SALES_ORDER_DETAIL D "
                    + " where H.S_ORDER_NO = D.S_ORDER_NO AND "
                    + " D.PIECE_NO = '" + Table.getValueAt(j, 5).toString() + "' AND "
                    + " H.CANCELED = 0 AND D.S_ORDER_NO!='" + S_O_NO.getText() + "'";
            String PieceNo = data.getStringValueFromDB(Query);
            System.out.println("Query : " + Query);
            if (!"".equals(PieceNo)) {
                JOptionPane.showMessageDialog(null, "Piece No " + PieceNo + " is already exist in ORDER ENTRY");
                return;
            }

            String Query_register = "SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Table.getValueAt(j, 5).toString() + "'";
            System.out.println("Query_register : " + Query_register);
            String PieceNo_register = data.getStringValueFromDB(Query_register);
            if (!"".equals(PieceNo_register)) {
                JOptionPane.showMessageDialog(null, "Piece No " + PieceNo + " is already exist in Piece Register");
                return;
            }

            /*
             String MACHINE_NO = DataModel.getValueAt(j, 1).toString();
             String POSITION = DataModel.getValueAt(j, 2).toString();

             int Year_From = EITLERPGLOBAL.getCurrentFinYear();
             int Year_To = Year_From + 1;

             String Req_Month = DataModel.getValueByVariable("REQ_MONTH", j);
             if(!Req_Month.equals(""))
             {

             if(Req_Month.contains("Jan") || Req_Month.contains("Feb") || Req_Month.contains("Mar"))
             {
             Year_From = Integer.parseInt(Req_Month.substring(6));
             Year_To = Year_From;  
             Year_From = Year_From-1;

             System.out.println("Position "+POSITION+" , Req_Month From = "+Year_From+" - To "+Year_To);
             }
             else
             {
             Year_From = Integer.parseInt(Req_Month.substring(6));
             Year_To = Year_From+1;
             }
             }
             else
             {
             JOptionPane.showMessageDialog(this, "Requested Month is Compulsory.");
             return;
             }


             String Query_BUDGET = "SELECT * FROM PRODUCTION.FELT_BUDGET where PARTY_CODE='" + PARTY_CODE.getText() + "' AND MACHINE_NO=" + MACHINE_NO + " AND POSITION_NO=" + POSITION + " AND YEAR_FROM='" + Year_From + "' AND YEAR_TO='" + Year_To + "'";
             System.out.println("Query_BUDGET : " + Query_BUDGET);

             if (!data.IsRecordExist(Query_BUDGET) && PARTY_CODE.getText().startsWith("8")) {
             JOptionPane.showMessageDialog(this, "Please update BUDGET first to enter order of PARTY : " + PARTY_CODE.getText() + ", MACHINE NO : " + MACHINE_NO + ", POSITION : " + POSITION + " AND YEAR_FROM='" + Year_From + "' AND YEAR_TO='" + Year_To + "'");
             return;
             }
             */
        }

        if (OpgApprove.isSelected() || OpgFinal.isSelected()) {
            String month_name = "";
            Date date = new Date();
            int month;
            int year = date.getYear() + 1900;

            month = date.getMonth() ;

            for (int i = 0; i < 40; i++) {
                month = month + 1;

                if (month >= 13) {
                    month = 1;
                    year = year + 1;
                }

                if (month == 1) {
                    month_name = "Jan";
                } else if (month == 2) {
                    month_name = "Feb";
                } else if (month == 3) {
                    month_name = "Mar";
                } else if (month == 4) {
                    month_name = "Apr";
                } else if (month == 5) {
                    month_name = "May";
                } else if (month == 6) {
                    month_name = "Jun";
                } else if (month == 7) {
                    month_name = "Jul";
                } else if (month == 8) {
                    month_name = "Aug";
                } else if (month == 9) {
                    month_name = "Sep";
                } else if (month == 10) {
                    month_name = "Oct";
                } else if (month == 11) {
                    month_name = "Nov";
                } else if (month == 12) {
                    month_name = "Dec";
                }

                if (i == 0) {
                    SDF_list.add(month_name + " - " + year);
                } else {
                    SDF_list.add(month_name + " - " + year);
                    NONSDF_list.add(month_name + " - " + year);
                }
            }
        }

        if (OpgFinal.isSelected()) {

            //All Piece are compulsory 
            boolean piece_no_not_found = false;
            for (int i = 0; i < Table.getRowCount(); i++) {

                if (!Table.getValueAt(i, 1).toString().equals("") || !Table.getValueAt(i, 2).toString().equals("")) {
                    if (Table.getValueAt(i, 5).toString().equals("")) {
                        piece_no_not_found = true;
                    }
                }
            }
            if (piece_no_not_found) {
                JOptionPane.showMessageDialog(null, "Piece No is compulsory for final approval");
                return;
            }

            //if Machine Master Amend Pending than it will not final approve
            for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                if (!DataModel.getValueByVariable("MACHINE_NO", i).equals("")) {

                    String MACHINE_NO = DataModel.getValueByVariable("MACHINE_NO", i);
                    String POSITION = DataModel.getValueByVariable("POSITION", i);

                    //String MACHINE_NO = DataModel.getValueAt(i, 1).toString();
                    //String POSITION = DataModel.getValueAt(i, 2).toString();
                    String QUERY = "SELECT A.* from PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER A,PRODUCTION.FELT_MACHINE_MASTER_AMEND_DETAIL B WHERE B.MM_MACHINE_NO='" + MACHINE_NO + "' AND B.MM_MACHINE_POSITION='" + POSITION + "' AND B.MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND A.APPROVED = 0 AND A.CANCELED = 0 AND A.MM_DOC_NO = B.MM_DOC_NO";

                    Connection Conn;
                    ResultSet rsTmp;
                    Statement stTmp;
                    HashMap List = new HashMap();
                    int Counter = 0;
                    int SrNo = 0;

                    try {
                        Conn = data.getConn();
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        rsTmp = stTmp.executeQuery(QUERY);
                        rsTmp.first();
                        if (rsTmp.getRow() > 0) {
                            JOptionPane.showMessageDialog(null, "Machine Master Amend Approval Pending for Machine No : " + MACHINE_NO + "  , Position :  " + POSITION + "  ,  Party :  " + PARTY_CODE.getText());
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("Error : " + e.getMessage());
                    }

                    if (!OpgReject.isSelected() && EITLERPGLOBAL.gUserDeptID == 39) {
                        String PIECE_NO = DataModel.getValueByVariable("PIECE_NO", i);
                        String LAYER_TYPE = DataModel.getValueByVariable("LAYER_TYPE", i);
                        if ("".equals(LAYER_TYPE)) {
                            JOptionPane.showMessageDialog(null, "Please select LAYER TYPE for PIECE NO : " + PIECE_NO);
                            return;
                        }
                        
                        String DM_REVISION_NO  = DataModel.getValueByVariable("DM_REVISION_NO", i);
                        String GROUP = DataModel.getValueByVariable("GROUP", i);
                        if ("".equals(DM_REVISION_NO) && !(GROUP.equals("HDS") || GROUP.equals("SDF"))) {
                            JOptionPane.showMessageDialog(null, "Please select Design Master Revision Number for PIECE NO : " + PIECE_NO);
                            return;
                        }
                    }

                    if (OpgApprove.isSelected() || OpgFinal.isSelected()) {
                        String PIECE_NO = DataModel.getValueByVariable("PIECE_NO", i);
                        String GROUP = DataModel.getValueByVariable("GROUP", i);
                        String REQ_MONTH = DataModel.getValueByVariable("REQ_MONTH", i);

                        if (GROUP.equals("SDF")) {
                            if (!SDF_list.contains(REQ_MONTH)) {
                                JOptionPane.showMessageDialog(null, "REQ MONTH " + REQ_MONTH + " is not valid for PIECE NO : " + PIECE_NO);
                                return;
                            }
                        } else {
                            if (!NONSDF_list.contains(REQ_MONTH)) {
                                JOptionPane.showMessageDialog(null, "REQ MONTH " + REQ_MONTH + " is not valid for PIECE NO : " + PIECE_NO);
                                return;
                            }
                        }
                    }
                }
            }

        }

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
                aList.ModuleID = 602;
                aList.FirstFreeNo = 212;
                FFNo = aList.FirstFreeNo;
                clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, true);

                if (OpgFinal.isSelected()) {
                    //clsHierarchy.sendMailToAllUsers(COMPANY_ID,SelHierarchyID,CURRENT_USER,SUBJECT,MESSAGE,cc);
                    //String Subject = "Felt Order Final Approved";
                    //String Message = "Document No : "+S_O_NO.getText()+" has been final approved by "+clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gNewUserID);
                    //clsHierarchy.sendMailToAllUsers((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, Subject, Message, "sdmlerp@dineshmills.com");

                    String DOC_NO = S_O_NO.getText();
                    String DOC_DATE = S_O_DATE.getText();
                    String Party_Code = PARTY_CODE.getText();

                    //17/02/2021
                    String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true, 0);
                    System.out.println("Send Mail Responce : " + responce);

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
                    //clsHierarchy.sendMailToAllUsers(COMPANY_ID,SelHierarchyID,CURRENT_USER,SUBJECT,MESSAGE,cc);
                    //String Subject = "Felt Order Final Approved";
                    //String Message = "Document No : "+S_O_NO.getText()+" has been final approved by "+clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gNewUserID);
                    //clsHierarchy.sendMailToAllUsers((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, Subject, Message, "sdmlerp@dineshmills.com");

                    try {
                        if (S_ENGINEER.equals("6")) {
                            String DateDb = EITLERPGLOBAL.formatDateDB(txtPaymentDate.getText());
                            if (!DateDb.equals("")) {
                                for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                                    if (!DataModel.getValueByVariable("PIECE_NO", i).equals("")) {
                                        String PieceNo = DataModel.getValueByVariable("PIECE_NO", i);
                                        //System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXPORT_PARTY_PAYMENT_DATE='"+DateDb+"',PR_EXPORT_PARTY_ORDERNO='"+S_O_NO.getText()+"',PR_EXPORT_PARTY_REMARK='"+txtPaymentRemark.getText()+"' WHERE PR_PIECE_NO='"+PieceNo+"'");
                                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXPORT_PARTY_PAYMENT_DATE='" + DateDb + "',PR_EXPORT_PARTY_ORDERNO='" + S_O_NO.getText() + "',PR_EXPORT_PARTY_REMARK='" + txtPaymentRemark.getText() + "' WHERE PR_PIECE_NO='" + PieceNo + "'");
                                    } else {
                                        System.out.println("PieceNo is blank");
                                    }
                                }
                            } else {
                                System.out.println("Entered date is blank, Date is :" + txtPaymentDate.getText());
                            }
                        }
                    } catch (Exception e) {
                    }
                    String DOC_NO = S_O_NO.getText();
                    String DOC_DATE = S_O_DATE.getText();
                    String Party_Code = PARTY_CODE.getText();
                    //17/02/2021
                    String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true, 0);
                    System.out.println("Send Mail Responce : " + responce);

                    //  17/02/2021
                    sendMailToContactPerson();

                    if (chbEmailUpdate.isSelected()) {
                        data.Execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER SET EMAIL='" + txtEmailId.getText() + "',EMAIL_ID2='" + txtEmail2.getText() + "',EMAIL_ID3='" + txtEmail3.getText() + "' where PARTY_CODE='" + PARTY_CODE.getText() + "' and COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "'");
                    }
                }
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured while saving editing. Error is " + feltOrder.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        EditMode = 0;
        cmdAdd.setEnabled(false);
        REMOVE.setEnabled(false);
        cmdAddPO.setEnabled(false);
        cmdRemovePO.setEnabled(false);
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

    private void sendMailToContactPerson() {
        String pMessage = "";
        //Felt Order No : " + S_O_NO.getText() + ",<br>Order Date : " + S_O_DATE.getText() + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) + "<br> 
        //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
        try {
            Connection Conn1;

            Statement stmt1;
            ResultSet rsData1;
            String OA_NO = "";
            String OA_DATE = "";
            String Payment_Terms = "";

            try {
                OA_NO = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 796, 287, true);
                OA_DATE = EITLERPGLOBAL.getCurrentDate();

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                data.Execute("update PRODUCTION.FELT_SALES_ORDER_HEADER SET  OA_NO='" + OA_NO + "',OA_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE S_ORDER_NO='" + S_O_NO.getText() + "'");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String ChargeCode = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='" + PARTY_CODE.getText() + "'");
                Payment_Terms = data.getStringValueFromDB("SELECT INVOICE_TYPE_DESC FROM DINESHMILLS.D_SAL_POLICY_INVOICE_TYPE WHERE INVOICE_TYPE_ID='" + ChargeCode + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String Reference;
            String Reference_Date;
            if (REFERENCE.getSelectedItem().toString().equals("P.O.")) {
                Reference = "P.O. - " + P_O_NO.getText();
                Reference_Date = P_O_DATE.getText();
            } else {
                Reference = REFERENCE.getSelectedItem().toString();
                Reference_Date = REF_DATE.getText();
            }

            System.out.println("OA NO = " + OA_NO);
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_HEADER H,PRODUCTION.FELT_SALES_ORDER_DETAIL D  "
                    + " where H.S_ORDER_NO=D.S_ORDER_NO "
                    + " AND H.S_ORDER_NO = '" + S_O_NO.getText() + "'");
            rsData1.first();

            pMessage = pMessage + "<br><br>";

            pMessage = pMessage + "<div style='min-width:1000px;'>\n"
                    + "	<div style=' text-align:center; min-width:1000px;'><u>ORDER ACKNOWLEDGEMENT</u></div>\n"
                    + "	<br><br>\n"
                    + "	<div style=' width:100%; heigth :200px;'>\n"
                    + "	\n"
                    + "		<div style=' width: 50%; float:left;'>\n"
                    + "			TO:  " + PARTY_NAME.getText() + "<br>\n"
                    + "			Party code: " + PARTY_CODE.getText() + " <br>\n"
                    + "			Email id: " + txtEmailId.getText() + "   <br>\n"
                    + "			Kind Attn: " + txtContactPerson.getText() + "  	\n"
                    + "			<hr>\n"
                    + "		</div>\n"
                    + "		<div style=' width:50%;  float:left;'>\n"
                    + "			Your order ref: " + Reference + "<br>\n"
                    + "			Date : " + Reference_Date + "<br>\n"
                    + "			Our order acknowledgement ref: " + OA_NO + " <br>\n"
                    + "			Date: " + OA_DATE + "\n"
                    + "			<hr>\n"
                    + "		</div>\n"
                    + "	\n"
                    + "	</div>\n"
                    + "	<div>\n"
                    + "		Dear sir,\n"
                    + "		<p>\n"
                    + "		<t>We thank you for your above valued purchase order which has been registered by us and the tentative schedules are as hereunder.\n"
                    + "	</div>\n"
                    + "	<div>\n"
                    + "		<table border='1' width='100%'>\n"
                    + "<tr>"
                    + "			<th>M/C NO </th>\n"
                    + "			<th>POSITION</th>\n"
                    + "			<th>DESCRIPTION</th>\n"
                    + "			<th>PIECE NO</th>\n"
                    + "			<th>LENGTH</th>\n"
                    + "			<th>WIDTH</th>\n"
                    + "			<th>GSM</th>\n"
                    + "			<th>TENTATIVE SCHEDULE</th>\n"
                    + "</tr>";

            rsData1.first();

            if (rsData1.getRow() > 0) {
                while (!rsData1.isAfterLast()) {
                    //String Desc = data.getStringValueFromDB("SELECT PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER where PRODUCT_CODE = '" + rsData1.getString("PRODUCT_CODE") + "' AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");
                    pMessage = pMessage + "<tr>"
                            + "<td> " + rsData1.getString("MACHINE_NO") + " </td>\n"
                            + "<td>" + rsData1.getString("POSITION_DESC") + "</td>\n"
                            + "<td>" + rsData1.getString("PRODUCT_DESC") + "</td>\n"
                            + "<td>" + rsData1.getString("PIECE_NO") + "</td>\n"
                            + "<td>" + rsData1.getString("LENGTH") + "</td>\n"
                            + "<td>" + rsData1.getString("WIDTH") + "</td>\n"
                            + "<td>" + rsData1.getString("GSM") + "</td>\n"
                            + "<td>" + rsData1.getString("REQ_MONTH") + "</td>\n"
                            + "</tr>";

                    try {
                        data.Execute("update PRODUCTION.FELT_SALES_PIECE_REGISTER SET  PR_OA_NO='" + OA_NO + "',PR_OA_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PR_PIECE_NO='" + rsData1.getString("PIECE_NO") + "'");

                    } catch (Exception e) {
                        //e.printStackTrace();
                    }

                    rsData1.next();
                }
            }
            pMessage = pMessage + "</table>\n"
                    + "	</div>\n"
                    + "	<div>Payment terms: " + Payment_Terms + "\n"
                    + "	<p>\n"
                    + "	Price : prevailing at the time of despatch would be applicable. Kindly refer our circular dated 02.05.2022 regarding price revision effective from 01.07.2022."
                    + "	<p>\n"
                    + "	 The above schedules are tentative and all out efforts will be made to maintain the said schedules. However, in case of any unforeseen circumstances or in the event of any of your  earlier ordered felts for the said positions not lifted by you as per the agreed schedule and hence remaining in our stock, the aforementioned schedules may not hold good.\n"
                    + "	<p>\n"
                    + "	We would send you our firm delivery confirmation 60-75  days before the above schedules. We are sure you would appreciate this and extend your cooperation to us as always. \n"
                    + "	<p>\n"
                    + "	Assuring  you of our best services at all times and meanwhile thanking you once again,\n"
                    + "	</div>\n"
                    + "	\n"
                    + "\n"
                    + "</div>";
            try {
                String INCHARGE = clsSales_Party.getFeltInchargeName(Long.parseLong(feltOrder.getAttribute("SALES_ENGINEER").getString()));

                pMessage = pMessage + "<div>"
                        + "<br><br>For any clarification in this regard, kindly contact the following.<br><br>"
                        + "";

                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                rsData = stmt.executeQuery("SELECT PARA_DESC,NAME_SUPER FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='OA_CONTACT_PERSON' AND PARA_EXT1='" + INCHARGE + "' AND COMPANY_ID=2");
                rsData.first();
                String Name = rsData.getString("NAME_SUPER");
                String PARA_DESC = rsData.getString("PARA_DESC");
                pMessage = pMessage + "<br><table border='1' width='100%'> <tr>"
                        + "<th>Person </th>"
                        + "<th>Email Id </th>"
                        + "</tr>";
                pMessage = pMessage + "<tr>"
                        + "<td> " + Name + " </td>"
                        + "<td>" + PARA_DESC + "</td>"
                        + "</tr></table>"
                        + ""
                        + "</div>";
            } catch (Exception e) {
                e.printStackTrace();
            }

            pMessage = pMessage + "";

            try {
                String EmailId = txtEmailId.getText();
                if (!txtEmail2.getText().equals("")) {
                    EmailId = EmailId + "," + txtEmail2.getText();
                }
                if (!txtEmail3.getText().equals("")) {
                    EmailId = EmailId + "," + txtEmail3.getText();
                }
                //17/02/2021                   
                JavaMail.SendMailFeltOA(EmailId.trim(), pMessage, "Order Acknowledgement : " + OA_NO, "feltoa@dineshmills.com");
            } catch (Exception e) {
                System.out.println("Error Msg " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("Error on Mail: " + e.getMessage());
        }
    }

    private void Cancel() {
        DisplayData();
        EditMode = 0;
        EnableToolbar();
        SetMenuForRights();
        SetFields(false);
        cmdAddPO.setEnabled(false);
        cmdRemovePO.setEnabled(false);
        cmdAdd.setEnabled(false);
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

        String productionDocumentNo = (String) feltOrder.getAttribute("S_ORDER_NO").getObj();
        if (feltOrder.IsEditable(productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            SetupApproval();
            //ReasonResetReadonly();
            //cmbOrderReason.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(602, productionDocumentNo)) {
                SetFields(true);
                cmdAddPO.setEnabled(true);
                cmdRemovePO.setEnabled(true);
            } else {
                EnableApproval();
                cmdAddPO.setEnabled(false);
                cmdRemovePO.setEnabled(false);
                DataModelDoc.SetReadOnly(1);
                DataModelDoc.SetReadOnly(3);
            }

            if (EITLERPGLOBAL.gUserDeptID == 39 || EITLERPGLOBAL.gUserDeptID == 27) {
                SetFields(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "You cannot edit this record. It is either approved/rejected or waiting approval for other user", "EDITING ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Delete() {
        if (feltOrder.CanDelete(S_O_NO.getText() + "", S_O_DATE.getText(), EITLERPGLOBAL.gNewUserID)) {
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
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, S_O_NO.getText() + "");
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
        //SetFields(true);
        //========== Setting Up Header Fields ================//
        String FieldName = "";
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

    }

    private void SetFields(boolean pStat) {
        S_O_DATE.setEnabled(pStat);
        S_O_NO.setEnabled(pStat);
        PARTY_CODE.setEnabled(pStat);
        PARTY_NAME.setEnabled(pStat);
        REGION.setEnabled(pStat);
        S_ENGINEER.setEnabled(pStat);
        CITY.setEnabled(pStat);
        DISTRICT.setEnabled(pStat);
        COUNTRY.setEnabled(pStat);
        REFERENCE.setEnabled(pStat);
        REF_DATE.setEnabled(pStat);
        P_O_NO.setEnabled(pStat);
        P_O_DATE.setEnabled(pStat);
        REMARK.setEnabled(pStat);
        txtContactPerson.setEnabled(pStat);
        txtEmailId.setEnabled(pStat);
        txtPhoneNo.setEnabled(pStat);
        txtEmail2.setEnabled(pStat);
        txtEmail3.setEnabled(pStat);
        chbEmailUpdate.setEnabled(pStat);
        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        
        P_O_DATE.setEnabled(pStat);
        txtPersonPosition.setEnabled(pStat);
        txtContectPerson_Dinesh.setEnabled(pStat);
        
        //Table.setEnabled(pStat);
        if (!pStat) {
            for (int i = 0; i < DataModel.getColumnCount(); i++) {
                DataModel.SetReadOnly(i);
            }
        }

        //Table.setEnabled(pStat);
        //JOptionPane.showMessageDialog(null, "Dept Id :"+EITLERPGLOBAL.gUserDeptID);
        if (EITLERPGLOBAL.gUserDeptID == 27) {
            txtPaymentDate.setEnabled(pStat);
            txtPaymentRemark.setEnabled(pStat);
            rdoAdvancePayment.setEnabled(pStat);
            rdoOtherPayment.setEnabled(pStat);
            OpgApprove.setEnabled(true);
            OpgHold.setEnabled(true);

            OpgReject.setEnabled(true);
            OpgFinal.setEnabled(false);

            S_O_DATE.setEnabled(false);
            S_O_NO.setEnabled(false);
            PARTY_CODE.setEnabled(false);
            PARTY_NAME.setEnabled(false);
            REGION.setEnabled(false);
            S_ENGINEER.setEnabled(false);
            CITY.setEnabled(false);
            DISTRICT.setEnabled(false);
            COUNTRY.setEnabled(false);
            REFERENCE.setEnabled(false);
            REF_DATE.setEnabled(false);
            P_O_NO.setEnabled(false);
            P_O_DATE.setEnabled(false);
            REMARK.setEnabled(false);
            txtContactPerson.setEnabled(false);
            txtEmailId.setEnabled(false);
            txtPhoneNo.setEnabled(false);
            txtEmail2.setEnabled(false);
            txtEmail3.setEnabled(false);
            chbEmailUpdate.setEnabled(false);
            cmbHierarchy.setEnabled(false);

            cmbSendTo.setEnabled(false);
            // txtToRemarks.setEnabled(false);

            Table.setEnabled(false);
        } else {
            txtPaymentDate.setEnabled(false);
            txtPaymentRemark.setEnabled(false);
            rdoAdvancePayment.setEnabled(false);
            rdoOtherPayment.setEnabled(false);
        }

        txtPaymentDate.setEnabled(pStat);
        txtPaymentRemark.setEnabled(pStat);
        rdoAdvancePayment.setEnabled(pStat);
        rdoOtherPayment.setEnabled(pStat);
        REMOVE.setEnabled(pStat);
        
        if (EditMode == EITLERPGLOBAL.ADD || EITLERPGLOBAL.gUserDeptID != 39) {

            if (Old_Piece.isSelected()) {
                // All user can change or insert PIECE NO if OLD PIECE is not selected
            } else {
                //DataModel.SetReadOnly(4);
            }
            // JOptionPane.showMessageDialog(null, "Dept Id ReadOnly (4):"+EITLERPGLOBAL.gUserDeptID);
        }
        if(EditMode != EITLERPGLOBAL.EDIT && EditMode != EITLERPGLOBAL.ADD)
        {
            for(int i=0;i<64;i++)
            {
                DataModel.SetReadOnly(i);
            }
        }
        if (EITLERPGLOBAL.gUserDeptID == 39 || EITLERPGLOBAL.gUserDeptID == 27) {
            //JOptionPane.showMessageDialog(null, "Dept Id Readable (4) :"+EITLERPGLOBAL.gUserDeptID);
            DataModel.SetReadOnly(0);
            DataModel.SetReadOnly(1);
            DataModel.SetReadOnly(2);
            DataModel.SetReadOnly(3);
            DataModel.SetReadOnly(4);
            //DataModel.SetReadOnly(5);
            //DataModel.SetReadOnly(6);
            DataModel.SetReadOnly(7);
            DataModel.SetReadOnly(8);
            DataModel.SetReadOnly(9);
            DataModel.SetReadOnly(10);
            DataModel.SetReadOnly(11);
            DataModel.SetReadOnly(12);
            DataModel.SetReadOnly(13);
            DataModel.SetReadOnly(14);
            DataModel.SetReadOnly(15);
            //DataModel.SetReadOnly(16);
        } else {
            // JOptionPane.showMessageDialog(null, "Not Design Dept");
            DataModel.SetReadOnly(6);
        }

        DataModel.SetReadOnly(0);
        DataModel.SetReadOnly(1);
        DataModel.SetReadOnly(2);
        DataModel.SetReadOnly(3);
        DataModel.SetReadOnly(4);
        //DataModel.SetReadOnly(5);
        //DataModel.SetReadOnly(6);
        DataModel.SetReadOnly(7);
        DataModel.SetReadOnly(8);
        DataModel.SetReadOnly(9);
        DataModel.SetReadOnly(10);
        DataModel.SetReadOnly(11);
        DataModel.SetReadOnly(12);
        DataModel.SetReadOnly(13);
        DataModel.SetReadOnly(14);
        DataModel.SetReadOnly(15);
        DataModel.SetReadOnly(16);
        DataModel.SetReadOnly(17);
        //DataModel.SetReadOnly(18);
        DataModel.SetReadOnly(19);
        DataModel.SetReadOnly(20);
        DataModel.SetReadOnly(21);
        DataModel.SetReadOnly(22);
        DataModel.SetReadOnly(23);
        DataModel.SetReadOnly(24);
        DataModel.SetReadOnly(25);
        DataModel.SetReadOnly(26);
        DataModel.SetReadOnly(27);
        DataModel.SetReadOnly(28);
        DataModel.SetReadOnly(29);
        DataModel.SetReadOnly(30);
        DataModel.SetReadOnly(31);
        DataModel.SetReadOnly(32);
        DataModel.SetReadOnly(33);
        DataModel.SetReadOnly(34);
        DataModel.SetReadOnly(35);
        DataModel.SetReadOnly(36);
        DataModel.SetReadOnly(37);
        DataModel.SetReadOnly(38);
        DataModel.SetReadOnly(39);
        DataModel.SetReadOnly(62);
        
        DataModel.SetReadOnly(64);
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

    private void filterHierarchyCombo() {
        GenerateHierarchyCombo();

        String incharge = data.getStringValueFromDB("SELECT DESIGNER_INCHARGE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + PARTY_CODE.getText() + "'");
        System.out.println("Selected Incharge : " + incharge);

        switch (incharge) {
            case "2": {
                ArrayList<String> Hierarchy = new ArrayList<>();
                Hierarchy.add("AP-SJP-RKP");
                Hierarchy.add("AC-SJP-RKP");
                Hierarchy.add("SR-SJP-RKP");
                filter(Hierarchy);
                break;
            }
            case "1": {
                ArrayList<String> Hierarchy = new ArrayList<>();
                Hierarchy.add("AP-KM-RKP");
                Hierarchy.add("AC-KM-RKP");
                Hierarchy.add("SR-KM-RKP");
                filter(Hierarchy);
                break;
            }
        }
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

        feltOrder.setAttribute("S_ORDER_NO", S_O_NO.getText());
        feltOrder.setAttribute("S_ORDER_DATE", S_O_DATE.getText());
        feltOrder.setAttribute("REGION", REGION.getText());
        feltOrder.setAttribute("SALES_ENGINEER", S_ENGINEER.getText());
        feltOrder.setAttribute("PARTY_CODE", PARTY_CODE.getText());
        feltOrder.setAttribute("PARTY_NAME", PARTY_NAME.getText());
        feltOrder.setAttribute("REFERENCE", REFERENCE.getSelectedItem());
        feltOrder.setAttribute("REFERENCE_DATE", REF_DATE.getText());
        feltOrder.setAttribute("P_O_NO", P_O_NO.getText());
        feltOrder.setAttribute("P_O_DATE", P_O_DATE.getText());
        feltOrder.setAttribute("REMARK", REMARK.getText());
        feltOrder.setAttribute("OLD_PIECE", Old_Piece.isSelected());
        feltOrder.setAttribute("TENDER_PARTY", chkbox_TenderParty.isSelected());
        feltOrder.setAttribute("CONTACT_PERSON_POSITION", txtPersonPosition.getText());
        feltOrder.setAttribute("CONTACT_PERSON_DINESH", txtContectPerson_Dinesh.getText());
        
        if (rdoAdvancePayment.isSelected()) {
            feltOrder.setAttribute("EXPORT_PAYMENT_TYPE", "ADVANCE");
        } else if (rdoOtherPayment.isSelected()) {
            feltOrder.setAttribute("EXPORT_PAYMENT_TYPE", "OTHER");
        } else {
            feltOrder.setAttribute("EXPORT_PAYMENT_TYPE", "");
        }
        feltOrder.setAttribute("EXPORT_PAYMENT_DATE", EITLERPGLOBAL.formatDateDB(txtPaymentDate.getText()));
        feltOrder.setAttribute("EXPORT_PAYMENT_REMARK", txtPaymentRemark.getText());

        feltOrder.setAttribute("CONTACT_PERSON", txtContactPerson.getText());
        feltOrder.setAttribute("EMAIL_ID", txtEmailId.getText());
        feltOrder.setAttribute("PHONE_NUMBER", txtPhoneNo.getText());

        feltOrder.setAttribute("EMAIL_ID2", txtEmail2.getText());
        feltOrder.setAttribute("EMAIL_ID3", txtEmail3.getText());

        feltOrder.setAttribute("EMAIL_UPDATE_TO_PM", chbEmailUpdate.isSelected());

        DOC_NO = S_O_NO.getText();
        feltOrder.setAttribute("DOC_NO", S_O_NO.getText());
        feltOrder.setAttribute("DOC_DATE", S_O_DATE.getText());
        feltOrder.setAttribute("MODULE_ID", ModuleId);
        feltOrder.setAttribute("USER_ID", EITLERPGLOBAL.gNewUserID);

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

        if (EditMode == EITLERPGLOBAL.ADD) {
            feltOrder.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            feltOrder.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
        } else {
            feltOrder.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            feltOrder.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            feltOrder.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            feltOrder.setAttribute("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }

        //======= Set Line part ============
        try {
            feltOrder.hmFeltSalesOrderDetails.clear();

            for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                if (!DataModel.getValueByVariable("MACHINE_NO", i).equals("")) {
                    clsFeltSalesOrderDetails objFeltOrderDetails = new clsFeltSalesOrderDetails();

                    //objFeltOrderDetails.setAttribute("S_ORDER_DETAIL_CODE","");
                    objFeltOrderDetails.setAttribute("S_ORDER_NO", S_O_NO.getText());
                    objFeltOrderDetails.setAttribute("MACHINE_NO", DataModel.getValueByVariable("MACHINE_NO", i));
                    objFeltOrderDetails.setAttribute("POSITION", DataModel.getValueByVariable("POSITION", i));
                    objFeltOrderDetails.setAttribute("POSITION_DESC", DataModel.getValueByVariable("POSITION_DESC", i));
                    objFeltOrderDetails.setAttribute("UPN", DataModel.getValueByVariable("UPN", i));
                    objFeltOrderDetails.setAttribute("PIECE_NO", DataModel.getValueByVariable("PIECE_NO", i));
                    objFeltOrderDetails.setAttribute("LAYER_TYPE", DataModel.getValueByVariable("LAYER_TYPE", i));
                    objFeltOrderDetails.setAttribute("PRODUCT_CODE", DataModel.getValueByVariable("PRODUCT_CODE", i));
                    objFeltOrderDetails.setAttribute("PRODUCT_DESC", DataModel.getValueByVariable("DESCRIPTION", i));
                    objFeltOrderDetails.setAttribute("S_GROUP", DataModel.getValueByVariable("GROUP", i));
                    objFeltOrderDetails.setAttribute("LENGTH", DataModel.getValueByVariable("LENGTH", i));
                    objFeltOrderDetails.setAttribute("WIDTH", DataModel.getValueByVariable("WIDTH", i));
                    objFeltOrderDetails.setAttribute("GSM", DataModel.getValueByVariable("GSM", i));
                    objFeltOrderDetails.setAttribute("THORITICAL_WIDTH", DataModel.getValueByVariable("THORITICAL_WIDTH", i));
                    objFeltOrderDetails.setAttribute("SQ_MTR", DataModel.getValueByVariable("SQ_MTR", i));
                    objFeltOrderDetails.setAttribute("STYLE", DataModel.getValueByVariable("STYLE", i));
                    objFeltOrderDetails.setAttribute("REQ_MONTH", DataModel.getValueByVariable("REQ_MONTH", i));
                    objFeltOrderDetails.setAttribute("SYN_PER", DataModel.getValueByVariable("SYN_PER", i));
                    objFeltOrderDetails.setAttribute("REMARK", DataModel.getValueByVariable("REMARK", i));

                    objFeltOrderDetails.setAttribute("OV_RATE", DataModel.getValueByVariable("OV_RATE", i));

                    objFeltOrderDetails.setAttribute("SURCHARGE_PER", DataModel.getValueByVariable("SURCHARGE_PER", i));
                    objFeltOrderDetails.setAttribute("SURCHARGE_RATE", DataModel.getValueByVariable("SURCHARGE_RATE", i));
                    objFeltOrderDetails.setAttribute("GROSS_RATE", DataModel.getValueByVariable("GROSS_RATE", i));

                    objFeltOrderDetails.setAttribute("OV_BAS_AMOUNT", DataModel.getValueByVariable("OV_BAS_AMOUNT", i));
                    objFeltOrderDetails.setAttribute("OV_CHEM_TRT_CHG", DataModel.getValueByVariable("OV_CHEM_TRT_CHG", i));
                    objFeltOrderDetails.setAttribute("OV_SPIRAL_CHG", DataModel.getValueByVariable("OV_SPIRAL_CHG", i));
                    objFeltOrderDetails.setAttribute("OV_PIN_CHG", DataModel.getValueByVariable("OV_PIN_CHG", i));
                    objFeltOrderDetails.setAttribute("OV_SEAM_CHG", DataModel.getValueByVariable("OV_SEAM_CHG", i));
                    objFeltOrderDetails.setAttribute("OV_INS_IND", DataModel.getValueByVariable("OV_INS_IND", i));
                    objFeltOrderDetails.setAttribute("OV_INS_AMT", DataModel.getValueByVariable("OV_INS_AMT", i));
                    objFeltOrderDetails.setAttribute("OV_EXCISE", DataModel.getValueByVariable("OV_EXCISE", i));
                    objFeltOrderDetails.setAttribute("OV_DISC_PER", DataModel.getValueByVariable("OV_DISC_PER", i));
                    objFeltOrderDetails.setAttribute("OV_DISC_AMT", DataModel.getValueByVariable("OV_DISC_AMT", i));
                    objFeltOrderDetails.setAttribute("OV_DISC_BASAMT", DataModel.getValueByVariable("OV_DISC_BASAMT", i));
                    objFeltOrderDetails.setAttribute("OV_AMT", DataModel.getValueByVariable("OV_AMT", i));

                    objFeltOrderDetails.setAttribute("CGST_PER", Double.valueOf(DataModel.getValueByVariable("CGST_PER", i)));
                    objFeltOrderDetails.setAttribute("CGST_AMT", Double.valueOf(DataModel.getValueByVariable("CGST_AMT", i)));
                    objFeltOrderDetails.setAttribute("SGST_PER", Double.valueOf(DataModel.getValueByVariable("SGST_PER", i)));
                    objFeltOrderDetails.setAttribute("SGST_AMT", Double.valueOf(DataModel.getValueByVariable("SGST_AMT", i)));
                    objFeltOrderDetails.setAttribute("IGST_PER", Double.valueOf(DataModel.getValueByVariable("IGST_PER", i)));
                    objFeltOrderDetails.setAttribute("IGST_AMT", Double.valueOf(DataModel.getValueByVariable("IGST_AMT", i)));
                    objFeltOrderDetails.setAttribute("COMPOSITION_PER", Double.valueOf(DataModel.getValueByVariable("COMPOSITION_PER", i)));
                    objFeltOrderDetails.setAttribute("COMPOSITION_AMT", Double.valueOf(DataModel.getValueByVariable("COMPOSITION_AMT", i)));
                    objFeltOrderDetails.setAttribute("RCM_PER", Double.valueOf(DataModel.getValueByVariable("RCM_PER", i)));
                    objFeltOrderDetails.setAttribute("RCM_AMT", Double.valueOf(DataModel.getValueByVariable("RCM_AMT", i)));
                    objFeltOrderDetails.setAttribute("GST_COMPENSATION_CESS_PER", Double.valueOf(DataModel.getValueByVariable("GST_COMPENSATION_CESS_PER", i)));
                    objFeltOrderDetails.setAttribute("GST_COMPENSATION_CESS_AMT", Double.valueOf(DataModel.getValueByVariable("GST_COMPENSATION_CESS_AMT", i)));

                    objFeltOrderDetails.setAttribute("PR_BILL_LENGTH", DataModel.getValueByVariable("PR_BILL_LENGTH", i));
                    objFeltOrderDetails.setAttribute("PR_BILL_WIDTH", DataModel.getValueByVariable("PR_BILL_WIDTH", i));
                    objFeltOrderDetails.setAttribute("PR_BILL_WEIGHT", DataModel.getValueByVariable("PR_BILL_WEIGHT", i));
                    objFeltOrderDetails.setAttribute("PR_BILL_SQMTR", DataModel.getValueByVariable("PR_BILL_SQMTR", i));
                    objFeltOrderDetails.setAttribute("PR_BILL_GSM", DataModel.getValueByVariable("PR_BILL_GSM", i));
                    objFeltOrderDetails.setAttribute("PR_BILL_PRODUCT_CODE", DataModel.getValueByVariable("PR_BILL_PRODUCT_CODE", i));
                    objFeltOrderDetails.setAttribute("PR_BILL_STYLE", DataModel.getValueByVariable("PR_BILL_STYLE", i));

                    //        objFeltOrderDetails.setAttribute("DATE_SLOT", DataModel.getValueByVariable("DATE_SLOT", i));
                    objFeltOrderDetails.setAttribute("TENDER_WEIGHT", DataModel.getValueByVariable("TENDER_WEIGHT", i));
                    objFeltOrderDetails.setAttribute("TENDER_GSM", DataModel.getValueByVariable("TENDER_GSM", i));
                    objFeltOrderDetails.setAttribute("DM_REVISION_NO", DataModel.getValueByVariable("DM_REVISION_NO", i));
                    objFeltOrderDetails.setAttribute("DUMMY_REF_NO", DataModel.getValueByVariable("DUMMY_REF_NO", i));
                    
                    if (Table.getValueAt(i, 63).equals(true)) {
                        objFeltOrderDetails.setAttribute("PIECE_MERGE", "1");
                    } else {
                        objFeltOrderDetails.setAttribute("PIECE_MERGE", "0");
                    }

                    feltOrder.hmFeltSalesOrderDetails.put(Integer.toString(feltOrder.hmFeltSalesOrderDetails.size() + 1), objFeltOrderDetails);
                }
            }
        } catch (Exception e) {
            System.out.println("Error on setData : " + e.getMessage());
            e.printStackTrace();
        };
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CITY;
    private javax.swing.JTextField COUNTRY;
    private javax.swing.JTextField DISTRICT;
    private javax.swing.JTable DocTable;
    private javax.swing.JTextField ORDER_VALUE;
    private javax.swing.JCheckBox Old_Piece;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTextField PARTY_CODE;
    private javax.swing.JTextField PARTY_NAME;
    private javax.swing.JFormattedTextField P_O_DATE;
    private javax.swing.JTextField P_O_NO;
    private javax.swing.JComboBox REFERENCE;
    private javax.swing.JFormattedTextField REF_DATE;
    private javax.swing.JTextField REGION;
    private javax.swing.JTextField REMARK;
    private javax.swing.JButton REMOVE;
    private javax.swing.JTextField S_ENGINEER;
    private javax.swing.JFormattedTextField S_O_DATE;
    private javax.swing.JTextField S_O_NO;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable Table;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JTable Table_prevoiusData;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnSendFAmail;
    private javax.swing.JButton btnShowDesignMaster;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chbEmailUpdate;
    private javax.swing.JCheckBox chkbox_TenderParty;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdAddPO;
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
    private javax.swing.JButton cmdRemovePO;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblFrom1;
    private javax.swing.JLabel lblFromDate1;
    private javax.swing.JLabel lblInchargeName;
    private javax.swing.JLabel lblParty;
    private javax.swing.JLabel lblPartyCode1;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTo1;
    private javax.swing.JLabel lblToDate1;
    private javax.swing.JLabel ltbPink;
    private javax.swing.JRadioButton rdoAdvancePayment;
    private javax.swing.JRadioButton rdoOtherPayment;
    private javax.swing.JTable tblPreviousInvoice;
    private javax.swing.JTable tbl_version_info;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtContactPerson;
    private javax.swing.JTextField txtContectPerson_Dinesh;
    private javax.swing.JTextField txtEmail2;
    private javax.swing.JTextField txtEmail3;
    private javax.swing.JTextField txtEmailId;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtMachineNo;
    private javax.swing.JTextField txtOA_NO;
    private javax.swing.JTextField txtOrderMonth;
    private javax.swing.JTextField txtPaymentDate;
    private javax.swing.JTextField txtPaymentRemark;
    private javax.swing.JTextField txtPersonPosition;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables

    private void version_information() {

        try {
            DataModel_version_info = new EITLTableModel();
            tbl_version_info.removeAll();

            tbl_version_info.setModel(DataModel_version_info);
            tbl_version_info.setAutoResizeMode(1);

            DataModel_version_info.addColumn("SrNo"); //0 - Read Only
            DataModel_version_info.addColumn("Version No"); //1
            DataModel_version_info.addColumn("Date"); //2
            DataModel_version_info.addColumn("Description"); //3

            tbl_version_info.getColumnModel().getColumn(0).setMaxWidth(50);
            tbl_version_info.getColumnModel().getColumn(1).setMaxWidth(120);
            tbl_version_info.getColumnModel().getColumn(2).setMinWidth(130);
            tbl_version_info.getColumnModel().getColumn(2).setMaxWidth(130);

            DataModel_version_info.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_version_info.SetVariable(1, "VERSION_NO"); //1
            DataModel_version_info.SetVariable(2, "VERSION_DATE"); //1
            DataModel_version_info.SetVariable(3, "VERSION_DESC"); //1

            //dateColumn.setCellEditor(new DatePi);
            Object[] rowData = new Object[1];
            DataModel_version_info.addRow(rowData);
            DataModel_version_info.setValueByVariable("SrNo", "1", 0);
            DataModel_version_info.setValueByVariable("VERSION_NO", "1.1", 0);
            DataModel_version_info.setValueByVariable("VERSION_DATE", "28-Jan-2019", 0);
            DataModel_version_info.setValueByVariable("VERSION_DESC", "Budget checking removed", 0);

            DataModel_version_info.addRow(rowData);
            DataModel_version_info.setValueByVariable("SrNo", "", 1);
            DataModel_version_info.setValueByVariable("VERSION_NO", "", 1);
            DataModel_version_info.setValueByVariable("VERSION_DATE", "", 1);
            DataModel_version_info.setValueByVariable("VERSION_DESC", "Check for piece which have blank REQUEST MONTH for same party same machine and position.", 1);

            /*
             DataModel_version_info.addRow(rowData);
             DataModel_version_info.setValueByVariable("SrNo", "", 2);
             DataModel_version_info.setValueByVariable("VERSION_NO", "", 2);
             DataModel_version_info.setValueByVariable("VERSION_DATE", "", 2);
             DataModel_version_info.setValueByVariable("VERSION_DESC", "15 Days slot with Request Month", 2);
             */
            DataModel_version_info.addRow(rowData);
            DataModel_version_info.setValueByVariable("SrNo", "2", 2);
            DataModel_version_info.setValueByVariable("VERSION_NO", "1.2", 2);
            DataModel_version_info.setValueByVariable("VERSION_DATE", "01-Feb-2019", 2);
            DataModel_version_info.setValueByVariable("VERSION_DESC", "Tender Weight and GSM for Tender Party.", 2);

            DataModel_version_info.addRow(rowData);
            DataModel_version_info.setValueByVariable("SrNo", "3", 3);
            DataModel_version_info.setValueByVariable("VERSION_NO", "1.3", 3);
            DataModel_version_info.setValueByVariable("VERSION_DATE", "11-Feb-2019", 3);
            DataModel_version_info.setValueByVariable("VERSION_DESC", "Order Acknowledgement", 3);

            DataModel_version_info.addRow(rowData);
            DataModel_version_info.setValueByVariable("SrNo", "4", 4);
            DataModel_version_info.setValueByVariable("VERSION_NO", "1.4", 4);
            DataModel_version_info.setValueByVariable("VERSION_DATE", "04-Apr-2019", 4);
            DataModel_version_info.setValueByVariable("VERSION_DESC", "Piece Layer Type Selection by Design", 4);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RearrangeSrNo() {
        String mno;
        for (int m = 0; m < DocTable.getRowCount(); m++) {
            mno = String.valueOf(m + 1);
            DocTable.setValueAt(mno, m, 0);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                //JOptionPane.showMessageDialog(button, label + ": Ouch!");
                if (DocTable.getSelectedColumn() == 3) {
                    uploadDocument();
                }
                if (DocTable.getSelectedColumn() == 4) {
                    openFile();
                }

            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public void uploadDocument() {
        try {

            File Source_File;
            JFileChooser chooser = new JFileChooser();
            //FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf", "jpg");
            //sfilter.
            //chooser.setFileFilter(filter);

            int n = chooser.showOpenDialog(this);

            Source_File = chooser.getSelectedFile();
            clsDocSalesOrderPOAttachment d = new clsDocSalesOrderPOAttachment();
            FileInputStream inputStream = new FileInputStream(Source_File);
            d.setDOC_NAME(Source_File.getName());
            d.setDOCUMENT(inputStream);
            d.setDOCUMENT_DOC_NO(S_O_NO.getText().toString().trim());
            d.setDoc_Sr_No(DocTable.getValueAt(DocTable.getSelectedRow(), 0).toString());
            d.setDoc_Remark(DocTable.getValueAt(DocTable.getSelectedRow(), 1).toString());
            d.setDoc_type(DocTable.getValueAt(DocTable.getSelectedRow(), 2).toString());
            //System.out.println("File Size : "+(int)Source_File.length());
            if ((int) Source_File.length() < 1000000) {
                data.Execute("DELETE FROM DOC_MGMT.SALES_ORDER_PO_ATTACHMENT "
                        + "WHERE DOCUMENT_DOC_NO='" + S_O_NO.getText().toString().trim() + "' AND "
                        + "DOCUMENT_SR_NO=" + DocTable.getValueAt(DocTable.getSelectedRow(), 0));
                d.saveDocumentFile((int) Source_File.length());
                DocTable.setValueAt(Source_File.getName(), DocTable.getSelectedRow(), 5);
                //System.out.println("Uploding Done...!");
            } else {
                JOptionPane.showMessageDialog(this, "File size not more than 1 MB allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openFile() {

        try {

//            clsDocSalesOrderPOAttachment obj = new clsDocSalesOrderPOAttachment();
//            dataList = obj.getStatus(S_O_NO.getText().toString().trim());
//            obj = dataList.get(0);
//            int PDocId = obj.getDOC_ID();
//            String PFileName = obj.getDOC_NAME().toString().trim();
            int PDocId = data.getIntValueFromDB("SELECT DOC_ID FROM DOC_MGMT.SALES_ORDER_PO_ATTACHMENT "
                    + "WHERE DOCUMENT_DOC_NO='" + S_O_NO.getText().toString().trim() + "' AND "
                    + "DOCUMENT_SR_NO=" + DocTable.getValueAt(DocTable.getSelectedRow(), 0));
            String PFileName = DocTable.getValueAt(DocTable.getSelectedRow(), 5).toString().trim();

            ResultSet rs = null;
            rs = sdml.felt.commonUI.data.getResult("SELECT DOCUMENT FROM DOC_MGMT.SALES_ORDER_PO_ATTACHMENT where DOC_ID=" + PDocId + " AND DOC_NAME='" + PFileName + "' ");

            File file = new File(PFileName);

            try {
                FileOutputStream output = new FileOutputStream(file);
                System.out.println("Writing to file " + file.getAbsolutePath());
                rs.first();
                byte[] imagebytes = rs.getBytes("DOCUMENT");
                output.write(imagebytes);
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(null, "Desktop Not Supported");
                return;
            } else {
                Desktop desk = Desktop.getDesktop();
                if (file.exists()) {
                    desk.open(file);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public String frm_PARTY_CODE;
    public String frm_REFERENCE;
    public String frm_PO_NO;
    public String frm_PO_DATE;
    public String frm_REFERENCE_DATE;
    public String frm_REMARK;
    
    
    //16/02/2022
    public ArrayList<clsOrderUPNList> upn_list;

    public void DataSettingFromFollowUp() {

        Add();
        PARTY_CODE.setText(frm_PARTY_CODE);
        PARTY_CODEFocusLost(null);

        int srno = 0;
        for (clsOrderUPNList upnObject : upn_list) {
            if (srno != 0) {
                Object[] rowData = new Object[15];
                rowData[0] = 1;
                DataModel.addRow(rowData);
            }
           
                DataModel.setValueByVariable("SrNo", (srno+1)+"", srno);
                DataModel.setValueByVariable("DUMMY_REF_NO", upnObject.getDUMMY_PIECE_NO(), srno);
                DataModel.setValueByVariable("MACHINE_NO", upnObject.getMachine_No(), srno);
                DataModel.setValueByVariable("POSITION", upnObject.getPositionNo(), srno);

                DataModel.setValueByVariable("UPN", upnObject.getUPN(), srno);
                DataModel.setValueByVariable("PIECE_NO", "", srno);
                DataModel.setValueByVariable("LAYER_TYPE", "", srno);

                String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + upnObject.getPositionNo());
                DataModel.setValueByVariable("UPN", upnObject.getUPN(), srno);

                try {
                    System.out.println(") SELECT D.MM_MACHINE_NO AS MACHINE_NO,D.MM_MACHINE_POSITION AS POSITION,"
                            + "D.MM_MACHINE_POSITION_DESC AS POSITION_DESC,D.MM_ITEM_CODE AS ITEM_CODE,D.MM_GRUP AS GRUP,"
                            + "(D.MM_FELT_LENGTH+D.MM_FABRIC_LENGTH) AS LENGTH,(D.MM_FELT_WIDTH+D.MM_FABRIC_WIDTH) AS WIDTH,"
                            + "D.MM_FELT_GSM AS GSM,concat(D.MM_FELT_STYLE,D.MM_STYLE_DRY) AS STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,PRODUCTION.FELT_MACHINE_MASTER_DETAIL D "
                            + " WHERE D.MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND D.MM_MACHINE_NO=" + upnObject.getMachine_No() + " AND D.MM_MACHINE_POSITION=" + upnObject.getPositionNo() + " AND (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') AND (D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '') AND D.MM_FELT_GSM!='') AND H.APPROVED=1 AND H.CANCELED=0 AND H.MM_DOC_NO=D.MM_DOC_NO ORDER BY  D.MM_MACHINE_NO,D.MM_MACHINE_POSITION");
                    ResultSet rsData = data.getResult("SELECT D.MM_MACHINE_NO AS MACHINE_NO,D.MM_MACHINE_POSITION AS POSITION,"
                            + "D.MM_MACHINE_POSITION_DESC AS POSITION_DESC,D.MM_ITEM_CODE AS ITEM_CODE,D.MM_GRUP AS GRUP,"
                            + "(D.MM_FELT_LENGTH+D.MM_FABRIC_LENGTH) AS LENGTH,(D.MM_FELT_WIDTH+D.MM_FABRIC_WIDTH) AS WIDTH,"
                            + "D.MM_FELT_GSM AS GSM,concat(D.MM_FELT_STYLE,D.MM_STYLE_DRY) AS STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,PRODUCTION.FELT_MACHINE_MASTER_DETAIL D "
                            + " WHERE D.MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND D.MM_MACHINE_NO=" + upnObject.getMachine_No() + " AND D.MM_MACHINE_POSITION=" + upnObject.getPositionNo() + " AND (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') AND (D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '') AND D.MM_FELT_GSM!='') AND H.APPROVED=1 AND H.CANCELED=0 AND H.MM_DOC_NO=D.MM_DOC_NO ORDER BY  D.MM_MACHINE_NO,D.MM_MACHINE_POSITION");

                    rsData.first();
                    DataModel.setValueByVariable("PRODUCT_CODE", rsData.getString("ITEM_CODE"), srno);
                    String Product_Code = rsData.getString("ITEM_CODE");
                    String Product_Desc = data.getStringValueFromDB("SELECT PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + Product_Code + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");
                    String Syn_Per = data.getStringValueFromDB("SELECT SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + Product_Code + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");
                    String Group_Name = data.getStringValueFromDB("SELECT GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + Product_Code + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");

                    DataModel.setValueByVariable("DESCRIPTION", Product_Desc, srno);
                    DataModel.setValueByVariable("SYN_PER", Syn_Per, srno);
                    DataModel.setValueByVariable("GROUP", Group_Name, srno);
                    DataModel.setValueByVariable("POSITION_DESC", rsData.getString("POSITION_DESC"), srno);
                    DataModel.setValueByVariable("LENGTH", rsData.getString("LENGTH"), srno);
                    DataModel.setValueByVariable("WIDTH", rsData.getString("WIDTH"), srno);
                    DataModel.setValueByVariable("GSM", rsData.getString("GSM"), srno);

                    float Theoritical_Weigth = (Float.parseFloat(rsData.getString("LENGTH")) * Float.parseFloat(rsData.getString("WIDTH")) * Float.parseFloat(rsData.getString("GSM")) / 1000);

                    float SQMT = (Float.parseFloat(rsData.getString("LENGTH")) * Float.parseFloat(rsData.getString("WIDTH")));

                    DataModel.setValueByVariable("THORITICAL_WIDTH", EITLERPGLOBAL.round(Theoritical_Weigth, 1) + "", srno);
                    DataModel.setValueByVariable("SQ_MTR", EITLERPGLOBAL.round(SQMT, 2) + "", srno);
                    DataModel.setValueByVariable("STYLE", rsData.getString("STYLE"), srno);
                    DataModel.setValueByVariable("REMARK", "", srno);

                    if (Group_Name.equals("SDF")) {
                        TableColumn dateColumn = Table.getColumnModel().getColumn(16);
                        JComboBox monthbox = new JComboBox();

                        String month_name = "";
                        Date date = new Date();
                        int month;
                        int year = date.getYear() + 1900;

                        month = date.getMonth() + 1;

                        for (int i = 0; i < 12; i++) {
                            month = month + 1;

                            if (month >= 13) {
                                month = 1;
                                year = year + 1;
                            }

                            if (month == 1) {
                                month_name = "Jan";
                            } else if (month == 2) {
                                month_name = "Feb";
                            } else if (month == 3) {
                                month_name = "Mar";
                            } else if (month == 4) {
                                month_name = "Apr";
                            } else if (month == 5) {
                                month_name = "May";
                            } else if (month == 6) {
                                month_name = "Jun";
                            } else if (month == 7) {
                                month_name = "Jul";
                            } else if (month == 8) {
                                month_name = "Aug";
                            } else if (month == 9) {
                                month_name = "Sep";
                            } else if (month == 10) {
                                month_name = "Oct";
                            } else if (month == 11) {
                                month_name = "Nov";
                            } else if (month == 12) {
                                month_name = "Dec";
                            }
                            monthbox.addItem(month_name + " - " + year);
                        }

                        dateColumn.setCellEditor(new DefaultCellEditor(monthbox));
                    } else {
                        TableColumn dateColumn = Table.getColumnModel().getColumn(16);
                        JComboBox monthbox = new JComboBox();

                        String month_name = "";
                        Date date = new Date();
                        int month;
                        int year = date.getYear() + 1900;

                        month = date.getMonth() + 2;

                        for (int i = 0; i < 11; i++) {
                            month = month + 1;

                            if (month >= 13) {
                                month = 1;
                                year = year + 1;
                            }

                            if (month == 1) {
                                month_name = "Jan";
                            } else if (month == 2) {
                                month_name = "Feb";
                            } else if (month == 3) {
                                month_name = "Mar";
                            } else if (month == 4) {
                                month_name = "Apr";
                            } else if (month == 5) {
                                month_name = "May";
                            } else if (month == 6) {
                                month_name = "Jun";
                            } else if (month == 7) {
                                month_name = "Jul";
                            } else if (month == 8) {
                                month_name = "Aug";
                            } else if (month == 9) {
                                month_name = "Sep";
                            } else if (month == 10) {
                                month_name = "Oct";
                            } else if (month == 11) {
                                month_name = "Nov";
                            } else if (month == 12) {
                                month_name = "Dec";
                            }
                            monthbox.addItem(month_name + " - " + year);
                        }
                        dateColumn.setCellEditor(new DefaultCellEditor(monthbox));
                    }

                    DataModel.setValueByVariable("REQ_MONTH", upnObject.getREQ_MONTH() + "", srno);

                    FeltInvCalc inv_calc = new FeltInvCalc();

                    try {
                        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                        inv_calc = clsOrderValueCalc.calculate("", rsData.getString("ITEM_CODE"), PARTY_CODE.getText(), Float.parseFloat(rsData.getString("LENGTH")), Float.parseFloat(rsData.getString("WIDTH")), Float.parseFloat(EITLERPGLOBAL.round(Theoritical_Weigth, 1) + ""), SQMT, df1.format(df.parse(S_O_DATE.getText())));
                        //String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    DataModel.setValueByVariable("OV_RATE", inv_calc.getFicRate() + "", srno);

                    DataModel.setValueByVariable("SURCHARGE_PER", inv_calc.getFicSurcharge_per() + "", srno);
                    DataModel.setValueByVariable("SURCHARGE_RATE", inv_calc.getFicSurcharge_rate() + "", srno);
                    DataModel.setValueByVariable("GROSS_RATE", inv_calc.getFicGrossRate() + "", srno);
                    //DataModel.setValueAt(inv_calc.getFicRate(), Table.getSelectedRow(), 18);
                    DataModel.setValueByVariable("OV_BAS_AMOUNT", inv_calc.getFicBasAmount() + "", srno);
                    DataModel.setValueByVariable("OV_CHEM_TRT_CHG", inv_calc.getFicChemTrtChg() + "", srno); //19
                    DataModel.setValueByVariable("OV_SPIRAL_CHG", inv_calc.getFicSpiralChg() + "", srno); //20
                    DataModel.setValueByVariable("OV_PIN_CHG", inv_calc.getFicPinChg() + "", srno); //21
                    DataModel.setValueByVariable("OV_SEAM_CHG", inv_calc.getFicSeamChg() + "", srno); //22
                    DataModel.setValueByVariable("OV_INS_IND", inv_calc.getFicInsInd() + "", srno); //23
                    DataModel.setValueByVariable("OV_INS_AMT", inv_calc.getFicInsAmt() + "", srno); //24
                    DataModel.setValueByVariable("OV_EXCISE", inv_calc.getFicExcise() + "", srno); //25
                    DataModel.setValueByVariable("OV_DISC_PER", inv_calc.getFicDiscPer() + "", srno); //26
                    DataModel.setValueByVariable("OV_DISC_AMT", inv_calc.getFicDiscAmt() + "", srno); //27
                    DataModel.setValueByVariable("OV_DISC_BASAMT", inv_calc.getFicDiscBasamt() + "", srno); //28
                    DataModel.setValueByVariable("OV_AMT", inv_calc.getFicInvAmt() + "", srno); //18
                    DataModel.setValueByVariable("VAT1", "", srno); //19
                    DataModel.setValueByVariable("VAT4", "", srno); //20
                    DataModel.setValueByVariable("CST2", "", srno); //21
                    DataModel.setValueByVariable("CST5", "", srno); //22
                    DataModel.setValueByVariable("CGST_PER", inv_calc.getFicCGSTPER() + "", srno); //34
                    DataModel.setValueByVariable("CGST_AMT", inv_calc.getFicCGST() + "", srno); //35
                    DataModel.setValueByVariable("SGST_PER", inv_calc.getFicSGSTPER() + "", srno); //36
                    DataModel.setValueByVariable("SGST_AMT", inv_calc.getFicSGST() + "", srno); //37
                    DataModel.setValueByVariable("IGST_PER", inv_calc.getFicIGSTPER() + "", srno); //38
                    DataModel.setValueByVariable("IGST_AMT", inv_calc.getFicIGST() + "", srno); //39
                    DataModel.setValueByVariable("COMPOSITION_PER", "0", srno); //40
                    DataModel.setValueByVariable("COMPOSITION_AMT", "0", srno); //41
                    DataModel.setValueByVariable("RCM_PER", "0", srno); //42
                    DataModel.setValueByVariable("RCM_AMT", "0", srno); //43
                    DataModel.setValueByVariable("GST_COMPENSATION_CESS_PER", "0", srno); //44
                    DataModel.setValueByVariable("GST_COMPENSATION_CESS_AMT", "0", srno); //45
                    DataModel.setValueByVariable("PR_BILL_LENGTH", inv_calc.getFicLength() + "", srno);
                    DataModel.setValueByVariable("PR_BILL_WIDTH", inv_calc.getFicWidth() + "", srno);
                    DataModel.setValueByVariable("PR_BILL_WEIGHT", inv_calc.getFicWeight() + "", srno);
                    DataModel.setValueByVariable("PR_BILL_SQMTR", inv_calc.getFicSqMtr() + "", srno);
                    DataModel.setValueByVariable("PR_BILL_GSM", rsData.getString("GSM") + "", srno);
                    DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", inv_calc.getFicProductCode() + "", srno);
                    DataModel.setValueByVariable("PR_BILL_STYLE", rsData.getString("STYLE"), srno);
                    String vat1 = "0", vat4 = "0", cst2 = "0", cst5 = "0";

                    DataModel.setValueByVariable("VAT1", "", srno); //19
                    DataModel.setValueByVariable("VAT4", "", srno); //20
                    DataModel.setValueByVariable("CST2", "", srno); //21
                    DataModel.setValueByVariable("CST5", "", srno); //22
                } catch (Exception e) {
                    e.printStackTrace();
                }

           

            srno = srno + 1;
        }

        SetFields(true);

    }
    //END 16/02/2022
    
    
    /*
    
    //16/02/2022
    public String frm_Machine_No;
    public String frm_Position_No;
    public String frm_Dummy_Piece_No;
    public String frm_S_O_No;
    public String frm_UPN;
    public String frm_REQ_MONTH;
    public ArrayList<String> frm_UPN_values=new ArrayList<>();
    public void DataSettingFromFollowUp() {

        Add();
        PARTY_CODE.setText(frm_PARTY_CODE);
        PARTY_CODEFocusLost(null);
        
        DataModel.setValueByVariable("DUMMY_REF_NO", frm_Dummy_Piece_No, 0);
        DataModel.setValueByVariable("MACHINE_NO", frm_Machine_No, 0);
        DataModel.setValueByVariable("POSITION", frm_Position_No, 0);
        
        DataModel.setValueByVariable("UPN", frm_UPN, 0);
        DataModel.setValueByVariable("PIECE_NO", "", 0);
        DataModel.setValueByVariable("LAYER_TYPE", "", 0);
        
        String Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO=" + frm_Position_No);
        DataModel.setValueByVariable("UPN", PARTY_CODE.getText() + frm_Position_No + Position_Des_No, 0);
        
       try{
           ResultSet rsData = data.getResult("SELECT D.MM_MACHINE_NO AS MACHINE_NO,D.MM_MACHINE_POSITION AS POSITION,"
                   + "D.MM_MACHINE_POSITION_DESC AS POSITION_DESC,D.MM_ITEM_CODE AS ITEM_CODE,D.MM_GRUP AS GRUP,"
                   + "(D.MM_FELT_LENGTH+D.MM_FABRIC_LENGTH) AS LENGTH,(D.MM_FELT_WIDTH+D.MM_FABRIC_WIDTH) AS WIDTH,"
                   + "D.MM_FELT_GSM AS GSM,concat(D.MM_FELT_STYLE,D.MM_STYLE_DRY) AS STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,PRODUCTION.FELT_MACHINE_MASTER_DETAIL D "
                   + " WHERE D.MM_PARTY_CODE='"+PARTY_CODE.getText()+"' AND D.MM_MACHINE_NO="+frm_Machine_No+" AND D.MM_MACHINE_POSITION="+frm_Position_No+" AND (D.MM_MACHINE_NO!='' AND D.MM_MACHINE_POSITION!='' AND (D.MM_FELT_LENGTH != '' OR D.MM_FABRIC_LENGTH != '') AND (D.MM_FELT_WIDTH != '' OR D.MM_FABRIC_WIDTH != '') AND D.MM_FELT_GSM!='') AND H.APPROVED=1 AND H.CANCELED=0 AND H.MM_DOC_NO=D.MM_DOC_NO ORDER BY  D.MM_MACHINE_NO,D.MM_MACHINE_POSITION");
           
           rsData.first();
           DataModel.setValueByVariable("PRODUCT_CODE", rsData.getString("ITEM_CODE"), 0);
           String Product_Code = rsData.getString("ITEM_CODE");
           String Product_Desc = data.getStringValueFromDB("SELECT PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + Product_Code + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");        
           String Syn_Per = data.getStringValueFromDB("SELECT SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + Product_Code + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");        
           String Group_Name = data.getStringValueFromDB("SELECT GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + Product_Code + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");        
       
           
           DataModel.setValueByVariable("DESCRIPTION", Product_Desc, 0);
           DataModel.setValueByVariable("SYN_PER", Syn_Per, 0);
           DataModel.setValueByVariable("GROUP", Group_Name, 0);
           DataModel.setValueByVariable("POSITION_DESC", rsData.getString("POSITION_DESC"), 0);
           DataModel.setValueByVariable("LENGTH", rsData.getString("LENGTH"), 0);
           DataModel.setValueByVariable("WIDTH", rsData.getString("WIDTH"), 0);
           DataModel.setValueByVariable("GSM", rsData.getString("GSM"), 0);
           
            float Theoritical_Weigth = (Float.parseFloat(rsData.getString("LENGTH")) * Float.parseFloat(rsData.getString("WIDTH")) * Float.parseFloat(rsData.getString("GSM")) / 1000);

            float SQMT = (Float.parseFloat(rsData.getString("LENGTH")) * Float.parseFloat(rsData.getString("WIDTH")));

            DataModel.setValueByVariable("THORITICAL_WIDTH", EITLERPGLOBAL.round(Theoritical_Weigth, 1) + "", 0);
            DataModel.setValueByVariable("SQ_MTR", EITLERPGLOBAL.round(SQMT, 2) + "", 0);
            DataModel.setValueByVariable("STYLE", rsData.getString("STYLE"), 0);
            DataModel.setValueByVariable("REMARK", "", 0);

            
           if (Group_Name.equals("SDF")) {
                TableColumn dateColumn = Table.getColumnModel().getColumn(16);
                JComboBox monthbox = new JComboBox();

                String month_name = "";
                Date date = new Date();
                int month;
                int year = date.getYear() + 1900;

                month = date.getMonth() + 1;

                for (int i = 0; i < 12; i++) {
                    month = month + 1;

                    if (month >= 13) {
                        month = 1;
                        year = year + 1;
                    }

                    if (month == 1) {
                        month_name = "Jan";
                    } else if (month == 2) {
                        month_name = "Feb";
                    } else if (month == 3) {
                        month_name = "Mar";
                    } else if (month == 4) {
                        month_name = "Apr";
                    } else if (month == 5) {
                        month_name = "May";
                    } else if (month == 6) {
                        month_name = "Jun";
                    } else if (month == 7) {
                        month_name = "Jul";
                    } else if (month == 8) {
                        month_name = "Aug";
                    } else if (month == 9) {
                        month_name = "Sep";
                    } else if (month == 10) {
                        month_name = "Oct";
                    } else if (month == 11) {
                        month_name = "Nov";
                    } else if (month == 12) {
                        month_name = "Dec";
                    }
                    monthbox.addItem(month_name + " - " + year);
                }

                dateColumn.setCellEditor(new DefaultCellEditor(monthbox));
            } else {
                TableColumn dateColumn = Table.getColumnModel().getColumn(16);
                JComboBox monthbox = new JComboBox();

                String month_name = "";
                Date date = new Date();
                int month;
                int year = date.getYear() + 1900;

                month = date.getMonth() + 2;

                for (int i = 0; i < 11; i++) {
                    month = month + 1;

                    if (month >= 13) {
                        month = 1;
                        year = year + 1;
                    }

                    if (month == 1) {
                        month_name = "Jan";
                    } else if (month == 2) {
                        month_name = "Feb";
                    } else if (month == 3) {
                        month_name = "Mar";
                    } else if (month == 4) {
                        month_name = "Apr";
                    } else if (month == 5) {
                        month_name = "May";
                    } else if (month == 6) {
                        month_name = "Jun";
                    } else if (month == 7) {
                        month_name = "Jul";
                    } else if (month == 8) {
                        month_name = "Aug";
                    } else if (month == 9) {
                        month_name = "Sep";
                    } else if (month == 10) {
                        month_name = "Oct";
                    } else if (month == 11) {
                        month_name = "Nov";
                    } else if (month == 12) {
                        month_name = "Dec";
                    }
                    monthbox.addItem(month_name + " - " + year);
                }
                dateColumn.setCellEditor(new DefaultCellEditor(monthbox));
           }
           
           DataModel.setValueByVariable("REQ_MONTH", frm_REQ_MONTH + "", 0);
           
            FeltInvCalc inv_calc = new FeltInvCalc();

            try {
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                inv_calc = clsOrderValueCalc.calculate("", rsData.getString("ITEM_CODE"), PARTY_CODE.getText(), Float.parseFloat(rsData.getString("LENGTH")), Float.parseFloat(rsData.getString("WIDTH")), Float.parseFloat(EITLERPGLOBAL.round(Theoritical_Weigth, 1) + ""), SQMT, df1.format(df.parse(S_O_DATE.getText())));
                                                    //String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            DataModel.setValueByVariable("OV_RATE", inv_calc.getFicRate() + "", 0);

            DataModel.setValueByVariable("SURCHARGE_PER", inv_calc.getFicSurcharge_per() + "", 0);
            DataModel.setValueByVariable("SURCHARGE_RATE", inv_calc.getFicSurcharge_rate() + "", 0);
            DataModel.setValueByVariable("GROSS_RATE", inv_calc.getFicGrossRate() + "", 0);
            //DataModel.setValueAt(inv_calc.getFicRate(), Table.getSelectedRow(), 18);
            DataModel.setValueByVariable("OV_BAS_AMOUNT", inv_calc.getFicBasAmount() + "", 0);
            DataModel.setValueByVariable("OV_CHEM_TRT_CHG", inv_calc.getFicChemTrtChg() + "", 0); //19
            DataModel.setValueByVariable("OV_SPIRAL_CHG", inv_calc.getFicSpiralChg() + "", 0); //20
            DataModel.setValueByVariable("OV_PIN_CHG", inv_calc.getFicPinChg() + "", 0); //21
            DataModel.setValueByVariable("OV_SEAM_CHG", inv_calc.getFicSeamChg() + "", 0); //22
            DataModel.setValueByVariable("OV_INS_IND", inv_calc.getFicInsInd() + "", 0); //23
            DataModel.setValueByVariable("OV_INS_AMT", inv_calc.getFicInsAmt() + "", 0); //24
            DataModel.setValueByVariable("OV_EXCISE", inv_calc.getFicExcise() + "", 0); //25
            DataModel.setValueByVariable("OV_DISC_PER", inv_calc.getFicDiscPer() + "", 0); //26
            DataModel.setValueByVariable("OV_DISC_AMT", inv_calc.getFicDiscAmt() + "", 0); //27
            DataModel.setValueByVariable("OV_DISC_BASAMT", inv_calc.getFicDiscBasamt() + "", 0); //28
            DataModel.setValueByVariable("OV_AMT", inv_calc.getFicInvAmt() + "", 0); //18
            DataModel.setValueByVariable("VAT1", "", 0); //19
            DataModel.setValueByVariable("VAT4", "", 0); //20
            DataModel.setValueByVariable("CST2", "", 0); //21
            DataModel.setValueByVariable("CST5", "", 0); //22
            DataModel.setValueByVariable("CGST_PER", inv_calc.getFicCGSTPER() + "", 0); //34
            DataModel.setValueByVariable("CGST_AMT", inv_calc.getFicCGST() + "", 0); //35
            DataModel.setValueByVariable("SGST_PER", inv_calc.getFicSGSTPER() + "", 0); //36
            DataModel.setValueByVariable("SGST_AMT", inv_calc.getFicSGST() + "", 0); //37
            DataModel.setValueByVariable("IGST_PER", inv_calc.getFicIGSTPER() + "", 0); //38
            DataModel.setValueByVariable("IGST_AMT", inv_calc.getFicIGST() + "", 0); //39
            DataModel.setValueByVariable("COMPOSITION_PER", "0", 0); //40
            DataModel.setValueByVariable("COMPOSITION_AMT", "0", 0); //41
            DataModel.setValueByVariable("RCM_PER", "0", 0); //42
            DataModel.setValueByVariable("RCM_AMT", "0", 0); //43
            DataModel.setValueByVariable("GST_COMPENSATION_CESS_PER", "0", 0); //44
            DataModel.setValueByVariable("GST_COMPENSATION_CESS_AMT", "0", 0); //45
            DataModel.setValueByVariable("PR_BILL_LENGTH", inv_calc.getFicLength() + "", 0);
            DataModel.setValueByVariable("PR_BILL_WIDTH", inv_calc.getFicWidth() + "", 0);
            DataModel.setValueByVariable("PR_BILL_WEIGHT", inv_calc.getFicWeight() + "", 0);
            DataModel.setValueByVariable("PR_BILL_SQMTR", inv_calc.getFicSqMtr() + "", 0);
            DataModel.setValueByVariable("PR_BILL_GSM",  rsData.getString("GSM") + "", 0);
            DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", inv_calc.getFicProductCode() + "", 0);
            DataModel.setValueByVariable("PR_BILL_STYLE", rsData.getString("STYLE"), 0);
            String vat1 = "0", vat4 = "0", cst2 = "0", cst5 = "0";

            DataModel.setValueByVariable("VAT1", "", 0); //19
            DataModel.setValueByVariable("VAT4", "", 0); //20
            DataModel.setValueByVariable("CST2", "", 0); //21
            DataModel.setValueByVariable("CST5", "", 0); //22
            
            SetFields(true);
            
       }catch(Exception e)
       {
           e.printStackTrace();
       }
    }
    */
}
