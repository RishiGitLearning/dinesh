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
import java.awt.event.KeyEvent;
import java.util.Calendar;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FrmFeltOrderDiversionLoss extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableModel DataModel_ExistPiece;
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
        setSize(950, 670);
        initComponents();
        GenerateCombos();
        FormatGrid();

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
    public void DataSettingFromOrder() {
        //Add();
        //PARTY_CODE.setText(frm_PARTY_CODE);
        //S_O_NO.setText(Order_No_Conversion.Order_No_STRING(frm_S_O_No+1));
        //REMARK.setText(frm_REMARK);
        if (Table.getRowCount() == 0) {
            Object[] rowData = new Object[15];
            rowData[0] = "1";
            DataModel.addRow(rowData);
        }
        if (TableExist.getRowCount() == 0) {
            Object[] rowData = new Object[15];
            rowData[0] = "1";
            DataModel_ExistPiece.addRow(rowData);
        }
        clsSales_Party objParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText(), "210010");
        PARTY_NAME.setText(objParty.getAttribute("PARTY_NAME").getString());
        REGION.setText(objParty.getAttribute("ZONE").getString());
        CITY.setText(objParty.getAttribute("CITY_ID").getString());
        DISTRICT.setText(objParty.getAttribute("DISTRICT").getString());
        String Country = data.getStringValueFromDB("SELECT COUNTRY_NAME FROM DINESHMILLS.D_SAL_COUNTRY_MASTER where COUNTRY_ID='" + objParty.getAttribute("COUNTRY_ID").getInt() + "'");
        System.out.println("SELECT COUNTRY_NAME FROM DINESHMILLS.D_SAL_COUNTRY_MASTER where COUNTRY_ID='" + objParty.getAttribute("COUNTRY_ID").getInt() + "'");

        COUNTRY.setText(Country);
        try {
            S_ENGINEER.setText(objParty.getAttribute("INCHARGE_CD").getString());
            lblInchargeName.setText(clsSales_Party.getFeltInchargeName(Long.parseLong(objParty.getAttribute("INCHARGE_CD").getString())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] Piecedetail = {"", "", "", "", "", "", "", "", "", "", "", "", ""};

        Connection Conn;
        Statement stmt;
        ResultSet rsData;
        String Pos_Desc = "";
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();

            rsData = stmt.executeQuery("SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_ITEM_CODE,MM_GRUP,(MM_FELT_LENGTH+MM_FABRIC_LENGTH),(MM_FELT_WIDTH+MM_FABRIC_WIDTH),MM_FELT_GSM,concat(MM_FELT_STYLE,MM_STYLE_DRY) AS MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_MACHINE_NO='" + frm_Machine_No + "' AND MM_MACHINE_POSITION='" + frm_Position_No + "' AND MM_PARTY_CODE = '" + PARTY_CODE.getText() + "'");

            rsData.first();
            Piecedetail[0] = "";//PR_PIECE_NO
            Piecedetail[1] = rsData.getString(1);//PR_MACHINE_NO

            Piecedetail[2] = rsData.getString(2);//PR_POSITION_NO
            if (Piecedetail[2].length() == 1) {
                Piecedetail[2] = "0" + Piecedetail[2];
            }
            Pos_Desc = rsData.getString(3);
            Piecedetail[3] = PARTY_CODE.getText();//PR_PARTY_CODE
            Piecedetail[4] = rsData.getString(4);//PR_PRODUCT_CODE
            Piecedetail[5] = rsData.getString(5);//PR_GROUP
            Piecedetail[6] = rsData.getString(9);//PR_STYLE
            Piecedetail[7] = rsData.getString(6);//PR_LENGTH
            Piecedetail[8] = rsData.getString(7);//PR_WIDTH
            Piecedetail[9] = rsData.getString(8);//PR_GSM

        } catch (Exception e) {
            System.out.println("Error on connectrion = " + e.getMessage());
        }

        DataModel.setValueByVariable("MACHINE_NO", Piecedetail[1], 0);
        DataModel.setValueByVariable("POSITION", Piecedetail[2], 0);
        DataModel.setValueByVariable("POSITION_DESC", Pos_Desc, 0);

        DataModel.setValueByVariable("PRODUCT", Piecedetail[4], 0);

        String ITEM_DESC = "", SYN = "";
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            Piecedetail[4] = Piecedetail[4].substring(0, 6);
            rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER,GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + Piecedetail[4] + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')");
            rsData.first();
            ITEM_DESC = rsData.getString(1);
            SYN = rsData.getString(2);
            Piecedetail[5] = rsData.getString(3);
        } catch (Exception e) {
            System.out.println("Error 1 : " + e.getMessage());
        }
        DataModel.setValueByVariable("DESCRIPTION", ITEM_DESC, 0);
        DataModel.setValueByVariable("GROUP", Piecedetail[5], 0);
        DataModel.setValueByVariable("LENGTH", Piecedetail[7], 0);
        DataModel.setValueByVariable("WIDTH", Piecedetail[8], 0);
        DataModel.setValueByVariable("GSM", Piecedetail[9], 0);

        float Theoritical_Weigth = 0;
        try {
            Theoritical_Weigth = (Float.parseFloat(Piecedetail[7]) * Float.parseFloat(Piecedetail[8]) * Float.parseFloat(Piecedetail[9]) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double SQMT = 0;
        try {
            SQMT = EITLERPGLOBAL.round((Double.parseDouble(Piecedetail[7]) * Double.parseDouble(Piecedetail[8])), 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataModel.setValueByVariable("THORTICAL_WEIGHT", EITLERPGLOBAL.round(Theoritical_Weigth, 1) + "", 0);
        DataModel.setValueByVariable("SQ_MTR", EITLERPGLOBAL.round(SQMT, 2) + "", 0);
        DataModel.setValueByVariable("STYLE", Piecedetail[6], 0);
        DataModel.setValueByVariable("SYN", SYN, 0);

        String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
        float SQM_RATE = 0, SQM_CHRG = 0, WT_RATE = 0, CHARGES = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;

        String Product_Code = Piecedetail[4];
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            Product_Code = Product_Code.substring(0, 6);
            System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + Product_Code + "' AND EFFECTIVE_FROM <= '" + df1.format(df.parse(S_O_DATE.getText())) + "' AND (EFFECTIVE_TO >= '" + df1.format(df.parse(S_O_DATE.getText())) + "' or EFFECTIVE_TO >= \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
            rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + Product_Code + "' AND EFFECTIVE_FROM <= '" + df1.format(df.parse(S_O_DATE.getText())) + "' AND (EFFECTIVE_TO >= '" + df1.format(df.parse(S_O_DATE.getText())) + "' or EFFECTIVE_TO >= \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
            rsData.first();

            CATEGORY = rsData.getString(1);
            SQM_RATE = rsData.getFloat(2);
            WT_RATE = rsData.getFloat(3);
            SQM_IND = rsData.getString(4);
            SQM_CHRG = rsData.getFloat(5);
            CHEM_TRT_IND = rsData.getString(6);
            CHEM_TRT_CHRG = rsData.getFloat(7);
            PIN_IND = rsData.getString(8);
            PIN_CHRG = rsData.getFloat(9);
            SPR_IND = rsData.getString(10);
            SPR_CHRG = rsData.getFloat(11);
            SUR_IND = rsData.getString(12);
            SUR_CHRG = rsData.getFloat(13);
            CHARGES = rsData.getFloat(14);

        } catch (Exception e) {
            System.out.println("Error 2: " + e.getMessage());
        }
        FeltInvCalc inv_calc = new FeltInvCalc();

        try {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            inv_calc = clsOrderValueCalc.calculate(Piecedetail[0], Piecedetail[4], PARTY_CODE.getText(), Float.parseFloat(Piecedetail[7]), Float.parseFloat(Piecedetail[8]), Float.parseFloat(EITLERPGLOBAL.round(Theoritical_Weigth, 1) + ""), Float.parseFloat(EITLERPGLOBAL.round(SQMT, 2) + ""), df1.format(df.parse(S_O_DATE.getText())));
            if (!inv_calc.getReason().equals("")) {
                JOptionPane.showMessageDialog(this, inv_calc.getReason());
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        DataModel.setValueByVariable("OV_RATE", inv_calc.getFicRate() + "", 0);
        DataModel.setValueByVariable("OV_SEAM_CHG", inv_calc.getFicSeamChg() + "", 0);
        DataModel.setValueByVariable("OV_BAS_AMOUNT", inv_calc.getFicBasAmount() + "", 0);
        DataModel.setValueByVariable("OV_AMT", inv_calc.getFicInvAmt() + "", 0);
        DataModel.setValueByVariable("CGST_PER", inv_calc.getFicCGSTPER() + "", 0);
        DataModel.setValueByVariable("CGST_AMT", inv_calc.getFicCGST() + "", 0);
        DataModel.setValueByVariable("SGST_PER", inv_calc.getFicSGSTPER() + "", 0);
        DataModel.setValueByVariable("SGST_AMT", inv_calc.getFicSGST() + "", 0);
        DataModel.setValueByVariable("IGST_PER", inv_calc.getFicIGSTPER() + "", 0);
        DataModel.setValueByVariable("IGST_AMT", inv_calc.getFicIGST() + "", 0);
        DataModel.setValueByVariable("PR_BILL_LENGTH", inv_calc.getFicLength() + "", 0);
        DataModel.setValueByVariable("PR_BILL_WIDTH", inv_calc.getFicWidth() + "", 0);
        DataModel.setValueByVariable("PR_BILL_WEIGHT", inv_calc.getFicWeight() + "", 0);
        DataModel.setValueByVariable("PR_BILL_SQMTR", EITLERPGLOBAL.round(SQMT, 2) + "", 0);
        DataModel.setValueByVariable("PR_BILL_GSM", Piecedetail[9] + "", 0);
        DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", inv_calc.getFicProductCode() + "", 0);

        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            rsData = stmt.executeQuery("SELECT PR_PIECE_NO,PR_PRODUCT_CODE,PR_PARTY_CODE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_ORDER_DATE,PR_MACHINE_NO,PR_POSITION_NO,PR_SYN_PER,PR_STYLE,PR_GROUP,PR_PIECE_STAGE,PR_RCV_DATE,PR_ACTUAL_WEIGHT,PR_ACTUAL_LENGTH,PR_ACTUAL_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + frm_Piece_No + "'");
            rsData.first();

            PR_PIECE_NO = rsData.getString(1);
            PR_PRODUCT_CODE = rsData.getString(2);
            PR_PARTY_CODE = rsData.getString(3);
            PR_LENGTH = Float.parseFloat(rsData.getString(4));
            PR_WIDTH = Float.parseFloat(rsData.getString(5));
            PR_GSM = Math.round(Float.parseFloat(rsData.getString(6)));
            PR_THORITICAL_WEIGHT = Float.parseFloat(rsData.getString(7));
            PR_SQMTR = Float.parseFloat(rsData.getString(8));
            PR_ORDER_DATE = rsData.getString(9);
            PR_MACHINE_NO = rsData.getString(10);
            PR_POSITION_NO = rsData.getString(11);
            PR_SYN_PER = rsData.getString(12);
            PR_STYLE = rsData.getString(13);
            PR_GROUP = rsData.getString(14);
            PR_PIECE_STAGE = rsData.getString(15);
            PR_RCV_DATE = EITLERPGLOBAL.formatDate(rsData.getString(16));
            PR_ACTUAL_WEIGHT = rsData.getString("PR_ACTUAL_WEIGHT");
            PR_ACTUAL_LENGTH = rsData.getString("PR_ACTUAL_LENGTH");
            PR_ACTUAL_WIDTH = rsData.getString("PR_ACTUAL_WIDTH");
            PR_PIECE_NO = PR_PIECE_NO.trim();

            float Weight = 0, LENGTH = 0, WIDTH = 0;
            double sqmtr = 0;
            if ("IN STOCK".equals(PR_PIECE_STAGE) || "STOCK".equals(PR_PIECE_STAGE)) {
                DataModel.setValueByVariable("PIECE_NO", PR_PIECE_NO + "V", 0);
                LENGTH = Float.parseFloat(rsData.getString("PR_ACTUAL_LENGTH"));
                WIDTH = Float.parseFloat(rsData.getString("PR_ACTUAL_WIDTH"));
                Weight = Float.parseFloat(rsData.getString("PR_ACTUAL_WEIGHT"));
            } else {
                DataModel.setValueByVariable("PIECE_NO", PR_PIECE_NO + "P", 0);
                LENGTH = Float.parseFloat(rsData.getString(4));
                WIDTH = Float.parseFloat(rsData.getString(5));
                Weight = PR_THORITICAL_WEIGHT;
            }
            sqmtr = LENGTH * WIDTH;
            inv_calc_existing = clsOrderValueCalc.calculate(rsData.getString(1), rsData.getString(2), rsData.getString(3), LENGTH, WIDTH, Weight, Float.parseFloat(EITLERPGLOBAL.round(sqmtr, 2) + ""), rsData.getString(9));

            if (!inv_calc_existing.getReason().equals("")) {
                JOptionPane.showMessageDialog(this, inv_calc_existing.getReason());
            }
        } catch (Exception e) {
            System.out.println("Error 3: " + e.getMessage());
        }
        float diff = 0;
        diff = inv_calc_existing.getFicBasAmount() - inv_calc.getFicBasAmount();
        DataModel.setValueByVariable("Exist_Piece_AMT", EITLERPGLOBAL.round(inv_calc_existing.getFicInvAmt(), 2) + "", 0);
        DataModel.setValueByVariable("BASE_ORDER_AMT", EITLERPGLOBAL.round(inv_calc.getFicBasAmount(), 2) + "", 0);
        DataModel.setValueByVariable("BASE_EXISTING_PIECE_AMT", EITLERPGLOBAL.round(inv_calc_existing.getFicBasAmount(), 2) + "", 0);

        float discount = 0;
        if (inv_calc_existing.getFicProductCode().equals("729000") || inv_calc_existing.getFicProductCode().equals("7290000")) {
            System.out.println("get Discount ::  SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + inv_calc_existing.getFicPartyCode() + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + inv_calc_existing.getFicProductCode() + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO >= '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
            try {
                String Disc_Per = data.getStringValueFromDB("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE ='" + inv_calc_existing.getFicPartyCode() + "' AND SUBSTRING(PRODUCT_CODE,1,6) ='" + inv_calc_existing.getFicProductCode() + "' AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO >= '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                float d_per = Float.parseFloat(Disc_Per);
                discount = (diff * d_per) / 100;
                discount = Math.round(discount);

                DataModel.setValueByVariable("DISCOUNT_PER", EITLERPGLOBAL.round(d_per, 2) + "", 0);
                DataModel.setValueByVariable("DISCOUNT_AMT", discount + "", 0);
                diff = diff - discount;
            } catch (Exception e) {
                DataModel.setValueByVariable("DISCOUNT_PER", "0", 0);
                DataModel.setValueByVariable("DISCOUNT_AMT", "0", 0);
                e.printStackTrace();
            }
        } else {
            DataModel.setValueByVariable("DISCOUNT_PER", "NA", 0);
            DataModel.setValueByVariable("DISCOUNT_AMT", "NA", 0);
        }

        try {
            if (inv_calc_existing.getFicProductCode().equals("719011") || inv_calc_existing.getFicProductCode().equals("7190110")
                    || inv_calc_existing.getFicProductCode().equals("719021") || inv_calc_existing.getFicProductCode().equals("7190210")
                    || inv_calc_existing.getFicProductCode().equals("719031") || inv_calc_existing.getFicProductCode().equals("7190310")
                    || inv_calc_existing.getFicProductCode().equals("719041") || inv_calc_existing.getFicProductCode().equals("7190410")
                    || inv_calc_existing.getFicProductCode().equals("719051") || inv_calc_existing.getFicProductCode().equals("7190510")) {

                double SEAM_CHARGE = 0;
                double seam_width = inv_calc_existing.getFicWidth() - inv_calc.getFicWidth();

                SEAM_CHARGE = seam_width * 4899;
                DataModel.setValueByVariable("OV_SEAM_CHG", EITLERPGLOBAL.round(SEAM_CHARGE, 2) + "", 0);
                diff = diff + Float.parseFloat(EITLERPGLOBAL.round(SEAM_CHARGE, 2) + "");
            } else {
                DataModel.setValueByVariable("OV_SEAM_CHG", "0", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        diff = Math.round(diff);
        difference = diff;
        DataModel.setValueByVariable("DIFFERENCE_AMT", EITLERPGLOBAL.round(diff, 2) + "", 0);
        Table.requestFocus();
        cal_order_value();
        Table.setModel(DataModel);
        DataModel_ExistPiece.setValueAt(PR_MACHINE_NO, 0, 1);
        DataModel_ExistPiece.setValueAt(PR_POSITION_NO, 0, 2);
        DataModel_ExistPiece.setValueAt(frm_Piece_No, 0, 4);

        Connection Conn1;
        Statement stmt1;
        ResultSet rsData1;
        String Pos_Desc1 = "";
        try {
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();

            rsData1 = stmt1.executeQuery("SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_ITEM_CODE,MM_GRUP,MM_FELT_LENGTH,MM_FELT_WIDTH,MM_FELT_GSM,MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_MACHINE_NO='" + PR_MACHINE_NO + "' AND MM_MACHINE_POSITION='" + PR_POSITION_NO + "'");
            rsData1.first();

            Pos_Desc1 = rsData1.getString(3);
            TableExist.setModel(DataModel_ExistPiece);
        } catch (Exception e) {
        }
        DataModel_ExistPiece.setValueAt(Pos_Desc1, 0, 3);
        DataModel_ExistPiece.setValueAt(PR_PRODUCT_CODE, 0, 5);

        String ITEM_DESC1 = "";
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            PR_PRODUCT_CODE = PR_PRODUCT_CODE.substring(0, 6);
            rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + PR_PRODUCT_CODE + "'");
            rsData.first();
            ITEM_DESC1 = rsData.getString(1);
            SYN = rsData.getString(2);

        } catch (Exception e) {
            System.out.println("Error 4: " + e.getMessage());
        }

        DataModel_ExistPiece.setValueAt(ITEM_DESC1, 0, 6);
        DataModel_ExistPiece.setValueAt(PR_GROUP, 0, 7);
        DataModel_ExistPiece.setValueAt(PR_LENGTH, 0, 8);
        DataModel_ExistPiece.setValueAt(PR_WIDTH, 0, 9);
        DataModel_ExistPiece.setValueAt(PR_GSM, 0, 10);
        DataModel_ExistPiece.setValueAt(EITLERPGLOBAL.round(PR_THORITICAL_WEIGHT, 1), 0, 11);
        DataModel_ExistPiece.setValueAt(EITLERPGLOBAL.round(PR_SQMTR, 2), 0, 12);
        DataModel_ExistPiece.setValueAt(PR_STYLE, 0, 13);
        DataModel_ExistPiece.setValueAt(PR_SYN_PER, 0, 14);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicRate(), 0, 16);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicBasAmount(), 0, 17);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicChemTrtChg(), 0, 18);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicSpiralChg(), 0, 19);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicPinChg(), 0, 20);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicSeamChg(), 0, 21);
        DataModel_ExistPiece.setValueAt(0, 0, 22);
        DataModel_ExistPiece.setValueAt(0, 0, 23);
        DataModel_ExistPiece.setValueAt(0, 0, 24);
        DataModel_ExistPiece.setValueAt(0, 0, 25);
        DataModel_ExistPiece.setValueAt(0, 0, 26);
        DataModel_ExistPiece.setValueAt(0, 0, 27);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getVat(), 0, 28);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getCst(), 0, 29);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicInvAmt(), 0, 30);
        DataModel_ExistPiece.setValueAt(PR_RCV_DATE, 0, 31);
        DataModel_ExistPiece.setValueAt(PR_PARTY_CODE, 0, 32);
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            rsData = stmt.executeQuery("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='" + PR_PARTY_CODE + "' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
            rsData.first();
            DataModel_ExistPiece.setValueAt(rsData.getString(5), 0, 33);
            DataModel_ExistPiece.setValueAt(rsData.getString(1), 0, 34);
        } catch (Exception e) {
            System.out.println("Error onm fetch data for D_SAL_PARTY_MASTER " + e.getMessage());
        }
        DataModel_ExistPiece.setValueAt(PR_ACTUAL_WEIGHT, 0, 35); //ACTUAL_WEIGTH
        DataModel_ExistPiece.setValueAt(PR_PIECE_STAGE, 0, 36); //PIECE_STATUS
        DataModel_ExistPiece.setValueAt(PR_ACTUAL_LENGTH, 0, 37); //ACTUAL_LENGTH
        DataModel_ExistPiece.setValueAt(PR_ACTUAL_WIDTH, 0, 38); //ACTUAL_WIDTH
    }

    public void DefaultSettings() {
        Object[] rowData = new Object[15];
        rowData[0] = "1";
        DataModel.addRow(rowData);

        DataModel_ExistPiece.addRow(rowData);
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
        lblInchargeName.setText("");
        PARTY_CODE.setText("");
        DISTRICT.setText("");
        CITY.setText("");
        COUNTRY.setText("");
        PARTY_NAME.setText("");
        reason.setSelectedIndex(0);
        REMARK1.setText("");
        REMARK.setText("");
        ORDER_VALUE.setText("0");
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
        DataModel_ExistPiece.addRow(rowData);
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
            REGION.setText(feltOrder.getAttribute("D_REGION").getString());
            S_ENGINEER.setText(feltOrder.getAttribute("D_SALES_ENGINEER").getString());
            try {
                lblInchargeName.setText(clsSales_Party.getFeltInchargeName(Long.parseLong(feltOrder.getAttribute("D_SALES_ENGINEER").getString())));
            } catch (Exception e) {
                e.printStackTrace();
            }

            PARTY_CODE.setText(feltOrder.getAttribute("D_PARTY_CODE").getString());
            PARTY_NAME.setText(feltOrder.getAttribute("D_PARTY_NAME").getString());
            clsSales_Party objParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText(), "210010");
            PARTY_NAME.setText(objParty.getAttribute("PARTY_NAME").getString());
            REGION.setText(objParty.getAttribute("ZONE").getString());
            CITY.setText(objParty.getAttribute("CITY_ID").getString());
            DISTRICT.setText(objParty.getAttribute("DISTRICT").getString());
            String Country = data.getStringValueFromDB("SELECT COUNTRY_NAME FROM DINESHMILLS.D_SAL_COUNTRY_MASTER where COUNTRY_ID='" + objParty.getAttribute("COUNTRY_ID").getInt() + "'");
            System.out.println("SELECT COUNTRY_NAME FROM DINESHMILLS.D_SAL_COUNTRY_MASTER where COUNTRY_ID='" + objParty.getAttribute("COUNTRY_ID").getInt() + "'");

            COUNTRY.setText(Country);
            try {
                S_ENGINEER.setText(objParty.getAttribute("INCHARGE_CD").getString());
                lblInchargeName.setText(clsSales_Party.getFeltInchargeName(Long.parseLong(objParty.getAttribute("INCHARGE_CD").getString())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            reason.setSelectedItem(feltOrder.getAttribute("D_REASON_FOR_DIVERSION").getString());
            REMARK1.setText(feltOrder.getAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT").getString());
            REMARK.setText(feltOrder.getAttribute("D_REMARK").getString());

            DataModel.setValueAt("1", 0, 0);
            DataModel.setValueByVariable("MACHINE_NO", feltOrder.getAttribute("D_MACHINE_NO").getString(), 0);
            DataModel.setValueByVariable("POSITION", feltOrder.getAttribute("D_POSITION_NO").getString(), 0);
            DataModel.setValueByVariable("POSITION_DESC", feltOrder.getAttribute("D_POSITION_DESC").getString(), 0);
            DataModel.setValueByVariable("PIECE_NO", feltOrder.getAttribute("D_PIECE_NO").getString(), 0);
            DataModel.setValueByVariable("PRODUCT", feltOrder.getAttribute("D_PRODUCT_NO").getString(), 0);
            DataModel.setValueByVariable("DESCRIPTION", feltOrder.getAttribute("D_PRODUCT_DESC").getString(), 0);
            DataModel.setValueByVariable("STYLE", feltOrder.getAttribute("D_STYLE_NO").getString(), 0);
            DataModel.setValueByVariable("GROUP", feltOrder.getAttribute("D_GROUP").getString(), 0);
            DataModel.setValueByVariable("LENGTH", feltOrder.getAttribute("D_LENGTH").getString(), 0);
            DataModel.setValueByVariable("WIDTH", feltOrder.getAttribute("D_WIDTH").getString(), 0);
            DataModel.setValueByVariable("GSM", feltOrder.getAttribute("D_GSM").getString(), 0);
            DataModel.setValueByVariable("THORTICAL_WEIGHT", feltOrder.getAttribute("D_THORITICAL_WEIGHT").getString(), 0);
            DataModel.setValueByVariable("SQ_MTR", feltOrder.getAttribute("D_SQ_MTR").getString(), 0);
            DataModel.setValueByVariable("SYN", feltOrder.getAttribute("D_SYN_PER").getString(), 0);
            DataModel.setValueByVariable("OV_RATE", feltOrder.getAttribute("D_OV_RATE").getString(), 0);
            DataModel.setValueByVariable("OV_BAS_AMOUNT", feltOrder.getAttribute("D_OV_BAS_AMOUNT").getString(), 0);
            DataModel.setValueByVariable("0", feltOrder.getAttribute("D_OV_CHEM_TRT_CHG").getString(), 0);
            DataModel.setValueByVariable("0", feltOrder.getAttribute("D_OV_SPIRAL_CHG").getString(), 0);
            DataModel.setValueByVariable("0", feltOrder.getAttribute("D_OV_PIN_CHG").getString(), 0);
            DataModel.setValueByVariable("OV_SEAM_CHG", feltOrder.getAttribute("D_OV_SEAM_CHG").getString(), 0);
            DataModel.setValueByVariable("OV_AMT", feltOrder.getAttribute("D_OV_AMT").getString(), 0);
            DataModel.setValueByVariable("Exist_Piece_AMT", feltOrder.getAttribute("ORIGINAL_OV_AMOUNT").getString(), 0);
            DataModel.setValueByVariable("BASE_ORDER_AMT", feltOrder.getAttribute("BASE_ORDER_AMT").getString(), 0);
            DataModel.setValueByVariable("BASE_EXISTING_PIECE_AMT", feltOrder.getAttribute("BASE_EXISTING_PIECE_AMT").getString(), 0);
            DataModel.setValueByVariable("DISCOUNT_PER", feltOrder.getAttribute("DISCOUNT_PER").getString(), 0);
            DataModel.setValueByVariable("DISCOUNT_AMT", feltOrder.getAttribute("DISCOUNT_AMT").getString(), 0);
            DataModel.setValueByVariable("DIFFERENCE_AMT", feltOrder.getAttribute("DIFFERENCE_AMT").getString(), 0);
            DataModel.setValueByVariable("DB_NOTE", feltOrder.getAttribute("DEBIT_NOTE_NO").getString(), 0);
            DataModel.setValueByVariable("DB_AMT", feltOrder.getAttribute("DEBIT_AMT").getString(), 0);
            DataModel.setValueByVariable("CGST_PER", feltOrder.getAttribute("CGST_PER").getString(), 0);
            DataModel.setValueByVariable("CGST_AMT", feltOrder.getAttribute("CGST_AMT").getString(), 0);
            DataModel.setValueByVariable("SGST_PER", feltOrder.getAttribute("SGST_PER").getString(), 0);
            DataModel.setValueByVariable("SGST_AMT", feltOrder.getAttribute("SGST_AMT").getString(), 0);
            DataModel.setValueByVariable("IGST_PER", feltOrder.getAttribute("IGST_PER").getString(), 0);
            DataModel.setValueByVariable("IGST_AMT", feltOrder.getAttribute("IGST_AMT").getString(), 0);
            DataModel.setValueByVariable("COMPOSITION_PER", feltOrder.getAttribute("COMPOSITION_PER").getString(), 0);
            DataModel.setValueByVariable("COMPOSITION_AMT", feltOrder.getAttribute("COMPOSITION_AMT").getString(), 0);
            DataModel.setValueByVariable("RCM_PER", feltOrder.getAttribute("RCM_PER").getString(), 0);
            DataModel.setValueByVariable("RCM_AMT", feltOrder.getAttribute("RCM_AMT").getString(), 0);
            DataModel.setValueByVariable("GST_COMPENSATION_CESS_PER", feltOrder.getAttribute("GST_COMPENSATION_CESS_PER").getString(), 0);
            DataModel.setValueByVariable("GST_COMPENSATION_CESS_AMT", feltOrder.getAttribute("GST_COMPENSATION_CESS_AMT").getString(), 0);
            DataModel.setValueByVariable("PR_BILL_LENGTH", feltOrder.getAttribute("PR_BILL_LENGTH").getString(), 0);
            DataModel.setValueByVariable("PR_BILL_WIDTH", feltOrder.getAttribute("PR_BILL_WIDTH").getString(), 0);
            DataModel.setValueByVariable("PR_BILL_WEIGHT", feltOrder.getAttribute("PR_BILL_WEIGHT").getString(), 0);
            DataModel.setValueByVariable("PR_BILL_SQMTR", feltOrder.getAttribute("PR_BILL_SQMTR").getString(), 0);
            DataModel.setValueByVariable("PR_BILL_GSM", feltOrder.getAttribute("PR_BILL_GSM").getString(), 0);
            DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", feltOrder.getAttribute("PR_BILL_PRODUCT_CODE").getString(), 0);
            DataModel_ExistPiece.setValueAt("1", 0, 0);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString(), 0, 4);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_PRODUCT_NO").getString(), 0, 5);
            String ITEM_DESC = "", SYN = "", POSITION_DESC = "";
            Connection Conn;
            Statement stmt;
            ResultSet rsData;
            try {
                Conn = data.getConn();
                stmt = Conn.createStatement();

                String productCode = feltOrder.getAttribute("ORIGINAL_PRODUCT_NO").getString().substring(0, 6);
                System.out.println("SELECT PRODUCT_DESC,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER where PRODUCT_CODE = '" + productCode + "'");
                rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER where PRODUCT_CODE = '" + productCode + "'");
                rsData.first();
                ITEM_DESC = rsData.getString(1);
                SYN = rsData.getString(2);

                String str = "SELECT MM_MACHINE_POSITION_DESC FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL where  MM_MACHINE_NO='" + feltOrder.getAttribute("ORIGINAL_MACHINE_NO").getString() + "' AND MM_MACHINE_POSITION='" + feltOrder.getAttribute("ORIGINAL_POSITION_NO").getString() + "'";
                System.out.println("" + str);
                rsData = stmt.executeQuery(str);
                rsData.first();
                POSITION_DESC = rsData.getString(1);
            } catch (Exception e) {
                System.out.println("Error 5: " + e.getMessage());
            }
            DataModel_ExistPiece.setValueAt(ITEM_DESC, 0, 6);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_MACHINE_NO").getString(), 0, 1);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_POSITION_NO").getString(), 0, 2);
            DataModel_ExistPiece.setValueAt(POSITION_DESC, 0, 3);
            DataModel_ExistPiece.setValueAt(SYN, 0, 14);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_STYLE_NO").getString(), 0, 13);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_GROUP").getString(), 0, 7);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_LENGTH").getString(), 0, 8);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_WIDTH").getString(), 0, 9);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_GSM").getString(), 0, 10);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_THORITICAL_WEIGHT").getString(), 0, 11);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_SQ_MTR").getString(), 0, 12);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_OV_RATE").getString(), 0, 16);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_PARTY_CODE").getString(), 0, 32);

            String ACTUAL_WEIGTH = data.getStringValueFromDB("SELECT PR_ACTUAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String PIECE_STATUS = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String RCV_DATE = data.getStringValueFromDB("SELECT PR_FNSG_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String ACTUAL_LENGTH = data.getStringValueFromDB("SELECT PR_ACTUAL_LENGTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String ACTUAL_WIDTH = data.getStringValueFromDB("SELECT PR_ACTUAL_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            DataModel_ExistPiece.setValueAt(ACTUAL_WEIGTH, 0, 35);
            DataModel_ExistPiece.setValueAt(PIECE_STATUS, 0, 36);
            DataModel_ExistPiece.setValueAt(ACTUAL_LENGTH, 0, 37);
            DataModel_ExistPiece.setValueAt(ACTUAL_WIDTH, 0, 38);
            DataModel_ExistPiece.setValueAt(RCV_DATE, 0, 31);
            lblRevNo.setText(Integer.toString((int) feltOrder.getAttribute("REVISION_NO").getVal()));
            try {
                Conn = data.getConn();
                stmt = Conn.createStatement();
                rsData = stmt.executeQuery("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='" + feltOrder.getAttribute("ORIGINAL_PARTY_CODE").getString() + "' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
                rsData.first();
                DataModel_ExistPiece.setValueAt(rsData.getString(5), 0, 33);
                DataModel_ExistPiece.setValueAt(rsData.getString(1), 0, 34);
            } catch (Exception e) {
                System.out.println("Error  " + e.getMessage());
            }
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, Integer.parseInt(feltOrder.getAttribute("HIERARCHY_ID").getString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void FormatGrid() {
        try {
            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            Table.setAutoResizeMode(0);

            DataModel.addColumn("Sr No"); //0 - Read Only
            DataModel.addColumn("MACHINE NO"); //1
            DataModel.addColumn("POSITION"); //2
            DataModel.addColumn("POSITION DESC"); //3
            DataModel.addColumn("PIECE NO"); //4
            DataModel.addColumn("PRODUCT"); //5
            DataModel.addColumn("DESCRIPTION"); //6
            DataModel.addColumn("GROUP"); //7
            DataModel.addColumn("LENGTH"); //8
            DataModel.addColumn("WIDTH"); //9
            DataModel.addColumn("GSM"); //10
            DataModel.addColumn("THEORTICAL WEIGHT"); //11
            DataModel.addColumn("SQ MT"); //12
            DataModel.addColumn("STYLE"); //13
            DataModel.addColumn("SYN(%)"); //15
            DataModel.addColumn(""); //16
            DataModel.addColumn("OV_RATE"); //17
            DataModel.addColumn("OV_BAS_AMOUNT"); //18
            DataModel.addColumn("OV_CHEM_TRT_CHG"); //19
            DataModel.addColumn("OV_SPIRAL_CHG"); //20
            DataModel.addColumn("OV_PIN_CHG"); //21
            DataModel.addColumn("OV_SEAM_CHG"); //22
            DataModel.addColumn("OV_INS_IND"); //23
            DataModel.addColumn("OV_INS_AMT"); //24
            DataModel.addColumn("OV_EXCISE"); //25
            DataModel.addColumn("OV_DISC_PER"); //26
            DataModel.addColumn("OV_DISC_AMT"); //27
            DataModel.addColumn("OV_DISC_BASAMT"); //28
            DataModel.addColumn("VAT"); //29
            DataModel.addColumn("CST"); //30
            DataModel.addColumn("ORDER_AMT"); //31
            DataModel.addColumn("EXIST_PIECE_AMT"); //32
            DataModel.addColumn("BASE ORDER AMT");
            DataModel.addColumn("BASE EXISTING PIECE AMT");
            DataModel.addColumn("DIS PER");
            DataModel.addColumn("DIS AMT");
            DataModel.addColumn("Profit(-) / Loss(+)"); //33
            DataModel.addColumn("DB_MEMO"); //34
            DataModel.addColumn("DB_AMT"); //36
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

            DataModel.SetVariable(0, "Sr_No"); //0 - Read Only
            DataModel.SetVariable(1, "MACHINE_NO"); //1
            DataModel.SetVariable(2, "POSITION"); //2
            DataModel.SetVariable(3, "POSITION_DESC"); //3
            DataModel.SetVariable(4, "PIECE_NO"); //4
            DataModel.SetVariable(5, "PRODUCT"); //5
            DataModel.SetVariable(6, "DESCRIPTION"); //6
            DataModel.SetVariable(7, "GROUP"); //7
            DataModel.SetVariable(8, "LENGTH"); //8
            DataModel.SetVariable(9, "WIDTH"); //9
            DataModel.SetVariable(10, "GSM"); //10
            DataModel.SetVariable(11, "THORTICAL_WEIGHT"); //11
            DataModel.SetVariable(12, "SQ_MTR"); //12
            DataModel.SetVariable(13, "STYLE"); //13
            DataModel.SetVariable(14, "SYN"); //14
            DataModel.SetVariable(15, "REMARK"); //15
            DataModel.SetVariable(16, "OV_RATE"); //16
            DataModel.SetVariable(17, "OV_BAS_AMOUNT"); //17
            DataModel.SetVariable(18, "OV_CHEM_TRT_CHG"); //18
            DataModel.SetVariable(19, "OV_SPIRAL_CHG"); //19
            DataModel.SetVariable(20, "OV_PIN_CHG"); //20
            DataModel.SetVariable(21, "OV_SEAM_CHG"); //21
            DataModel.SetVariable(22, "OV_INS_IND"); //22
            DataModel.SetVariable(23, "OV_INS_AMT"); //23
            DataModel.SetVariable(24, "OV_EXCISE"); //24
            DataModel.SetVariable(25, "OV_DISC_PER"); //25
            DataModel.SetVariable(26, "OV_DISC_AMT"); //26
            DataModel.SetVariable(27, "OV_DISC_BASAMT"); //27
            DataModel.SetVariable(28, "VAT"); //28
            DataModel.SetVariable(29, "CST"); //29
            DataModel.SetVariable(30, "OV_AMT"); //30
            DataModel.SetVariable(31, "Exist_Piece_AMT"); //31
            DataModel.SetVariable(32, "BASE_ORDER_AMT");
            DataModel.SetVariable(33, "BASE_EXISTING_PIECE_AMT");
            DataModel.SetVariable(34, "DISCOUNT_PER");//DISCOUNT
            DataModel.SetVariable(35, "DISCOUNT_AMT");//DISCOUNT
            DataModel.SetVariable(36, "DIFFERENCE_AMT"); //32//LOSS
            DataModel.SetVariable(37, "DB_NOTE"); //33
            DataModel.SetVariable(38, "DB_AMT"); //35
            DataModel.SetVariable(39, "CGST_PER"); //34
            DataModel.SetVariable(40, "CGST_AMT"); //35
            DataModel.SetVariable(41, "SGST_PER"); //36
            DataModel.SetVariable(42, "SGST_AMT"); //37
            DataModel.SetVariable(43, "IGST_PER"); //38
            DataModel.SetVariable(44, "IGST_AMT"); //39
            DataModel.SetVariable(45, "COMPOSITION_PER"); //40
            DataModel.SetVariable(46, "COMPOSITION_AMT"); //41
            DataModel.SetVariable(47, "RCM_PER"); //42
            DataModel.SetVariable(48, "RCM_AMT"); //43
            DataModel.SetVariable(49, "GST_COMPENSATION_CESS_PER"); //44
            DataModel.SetVariable(50, "GST_COMPENSATION_CESS_AMT"); //45
            DataModel.SetVariable(51, "PR_BILL_LENGTH");
            DataModel.SetVariable(52, "PR_BILL_WIDTH");
            DataModel.SetVariable(53, "PR_BILL_WEIGHT");
            DataModel.SetVariable(54, "PR_BILL_SQMTR");
            DataModel.SetVariable(55, "PR_BILL_GSM");
            DataModel.SetVariable(56, "PR_BILL_PRODUCT_CODE");

            for (int i = 5; i <= 14; i++) {
                DataModel.SetReadOnly(i);
            }
            for (int i = 16; i < 50; i++) {
                DataModel.SetReadOnly(i);
            }

            DataModel.SetReadOnly(54);
            DataModel.SetReadOnly(56);

            Table.getColumnModel().getColumn(0).setMinWidth(0);
            Table.getColumnModel().getColumn(0).setMaxWidth(0);
            Table.getColumnModel().getColumn(1).setMinWidth(90);
            Table.getColumnModel().getColumn(2).setMinWidth(70);
            Table.getColumnModel().getColumn(3).setMinWidth(120);
            Table.getColumnModel().getColumn(4).setMinWidth(70);
            Table.getColumnModel().getColumn(5).setMinWidth(100);
            Table.getColumnModel().getColumn(6).setMinWidth(120);
            Table.getColumnModel().getColumn(7).setMinWidth(80);
            Table.getColumnModel().getColumn(8).setMinWidth(70);
            Table.getColumnModel().getColumn(9).setMinWidth(70);
            Table.getColumnModel().getColumn(10).setMinWidth(50);
            Table.getColumnModel().getColumn(11).setMinWidth(130);
            Table.getColumnModel().getColumn(12).setMinWidth(80);
            Table.getColumnModel().getColumn(13).setMinWidth(80);
            Table.getColumnModel().getColumn(14).setMinWidth(100);

            Table.getColumnModel().getColumn(16).setMinWidth(100);

            for (int i = 17; i <= 29; i++) {
                if (i != 21) {
                    Table.getColumnModel().getColumn(i).setMinWidth(0);
                    Table.getColumnModel().getColumn(i).setMaxWidth(0);
                }
            }

            Table.getColumnModel().getColumn(30).setMinWidth(120);
            Table.getColumnModel().getColumn(30).setMaxWidth(120);
            Table.getColumnModel().getColumn(31).setMinWidth(120);
            Table.getColumnModel().getColumn(31).setMaxWidth(120);
            Table.getColumnModel().getColumn(32).setMinWidth(120);
            Table.getColumnModel().getColumn(32).setMaxWidth(120);
            Table.getColumnModel().getColumn(33).setMinWidth(120);
            Table.getColumnModel().getColumn(34).setMinWidth(80);
            Table.getColumnModel().getColumn(35).setMinWidth(80);
            Table.getColumnModel().getColumn(36).setMinWidth(120);

            for (int i = 39; i <= 50; i++) {
                Table.getColumnModel().getColumn(i).setMinWidth(0);
                Table.getColumnModel().getColumn(i).setMaxWidth(0);
            }

            Table.getColumnModel().getColumn(51).setMinWidth(120);
            Table.getColumnModel().getColumn(51).setMaxWidth(120);
            Table.getColumnModel().getColumn(52).setMinWidth(120);
            Table.getColumnModel().getColumn(52).setMaxWidth(120);
            Table.getColumnModel().getColumn(53).setMinWidth(120);
            Table.getColumnModel().getColumn(53).setMaxWidth(120);

            Table.getColumnModel().getColumn(15).setMinWidth(0);
            Table.getColumnModel().getColumn(15).setMaxWidth(0);

            DataModel_ExistPiece = new EITLTableModel();
            TableExist.removeAll();

            TableExist.setModel(DataModel_ExistPiece);
            TableExist.setAutoResizeMode(0);

            DataModel_ExistPiece.addColumn("SrNo"); //0 - Read Only
            DataModel_ExistPiece.addColumn("MACHINE NO"); //1
            DataModel_ExistPiece.addColumn("POSITION"); //2
            DataModel_ExistPiece.addColumn("POSITION DESC"); //3
            DataModel_ExistPiece.addColumn("PIECE NO"); //4
            DataModel_ExistPiece.addColumn("PRODUCT"); //5
            DataModel_ExistPiece.addColumn("DESCRIPTION"); //6
            DataModel_ExistPiece.addColumn("GROUP"); //7
            DataModel_ExistPiece.addColumn("LENGTH"); //8
            DataModel_ExistPiece.addColumn("WIDTH"); //9
            DataModel_ExistPiece.addColumn("GSM"); //10
            DataModel_ExistPiece.addColumn("THEORITICAL WEIGHT"); //11
            DataModel_ExistPiece.addColumn("SQ MT"); //12
            DataModel_ExistPiece.addColumn("STYLE"); //13
            DataModel_ExistPiece.addColumn("SYN(%)"); //14
            DataModel_ExistPiece.addColumn("REMARK"); //15
            DataModel_ExistPiece.addColumn("OV_RATE"); //16
            DataModel_ExistPiece.addColumn("OV_BAS_AMOUNT"); //17
            DataModel_ExistPiece.addColumn("OV_CHEM_TRT_CHG"); //18
            DataModel_ExistPiece.addColumn("OV_SPIRAL_CHG"); //19
            DataModel_ExistPiece.addColumn("OV_PIN_CHG"); //20
            DataModel_ExistPiece.addColumn("OV_SEAM_CHG"); //21
            DataModel_ExistPiece.addColumn("OV_INS_IND"); //22
            DataModel_ExistPiece.addColumn("OV_INS_AMT"); //23
            DataModel_ExistPiece.addColumn("OV_EXCISE"); //24
            DataModel_ExistPiece.addColumn("OV_DISC_PER"); //25
            DataModel_ExistPiece.addColumn("OV_DISC_AMT"); //26
            DataModel_ExistPiece.addColumn("OV_DISC_BASAMT"); //27
            DataModel_ExistPiece.addColumn("VAT"); //28
            DataModel_ExistPiece.addColumn("CST"); //29
            DataModel_ExistPiece.addColumn("ORDER_AMT"); //30
            DataModel_ExistPiece.addColumn("RCVD_DATE"); //31
            DataModel_ExistPiece.addColumn("PARTY CODE"); //32
            DataModel_ExistPiece.addColumn("PARTY NAME"); //32
            DataModel_ExistPiece.addColumn("CITY"); //32
            DataModel_ExistPiece.addColumn("ACTUAL WEIGTH");//33
            DataModel_ExistPiece.addColumn("PIECE STATUS");//34
            DataModel_ExistPiece.addColumn("ACTUAL LENGTH");//33
            DataModel_ExistPiece.addColumn("ACTUAL WIDTH");//33

            DataModel_ExistPiece.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_ExistPiece.SetVariable(1, "MACHINE_NO"); //1
            DataModel_ExistPiece.SetVariable(2, "POSITION"); //2
            DataModel_ExistPiece.SetVariable(3, "POSITION_DESC"); //3
            DataModel_ExistPiece.SetVariable(4, "PIECE_NO"); //4
            DataModel_ExistPiece.SetVariable(5, "PRODUCT"); //5
            DataModel_ExistPiece.SetVariable(6, "DESCRIPTION"); //6
            DataModel_ExistPiece.SetVariable(7, "GROUP"); //7
            DataModel_ExistPiece.SetVariable(8, "LENGTH"); //8
            DataModel_ExistPiece.SetVariable(9, "WIDTH"); //9
            DataModel_ExistPiece.SetVariable(10, "GSM"); //10
            DataModel_ExistPiece.SetVariable(11, "EXISTING_WIDTH"); //11
            DataModel_ExistPiece.SetVariable(12, "SQ_MTR"); //12
            DataModel_ExistPiece.SetVariable(13, "STYLE"); //13
            DataModel_ExistPiece.SetVariable(14, "SYN"); //14
            DataModel_ExistPiece.SetVariable(15, "REMARK"); //15
            DataModel_ExistPiece.SetVariable(16, "OV_RATE"); //16
            DataModel_ExistPiece.SetVariable(17, "OV_BAS_AMOUNT"); //17
            DataModel_ExistPiece.SetVariable(18, "OV_CHEM_TRT_CHG"); //18
            DataModel_ExistPiece.SetVariable(19, "OV_SPIRAL_CHG"); //19
            DataModel_ExistPiece.SetVariable(20, "OV_PIN_CHG"); //20
            DataModel_ExistPiece.SetVariable(21, "OV_SEAM_CHG"); //21
            DataModel_ExistPiece.SetVariable(22, "OV_INS_IND"); //22
            DataModel_ExistPiece.SetVariable(23, "OV_INS_AMT"); //23
            DataModel_ExistPiece.SetVariable(24, "OV_EXCISE"); //24
            DataModel_ExistPiece.SetVariable(25, "OV_DISC_PER"); //25
            DataModel_ExistPiece.SetVariable(26, "OV_DISC_AMT"); //26
            DataModel_ExistPiece.SetVariable(27, "OV_DISC_BASAMT"); //27
            DataModel_ExistPiece.SetVariable(28, "VAT"); //28
            DataModel_ExistPiece.SetVariable(29, "CST"); //29
            DataModel_ExistPiece.SetVariable(30, "OV_AMT"); //30
            DataModel_ExistPiece.SetVariable(31, "RCVD_DATE"); //31
            DataModel_ExistPiece.SetVariable(32, "PARTY_CODE"); //32
            DataModel_ExistPiece.SetVariable(33, "PARTY_NAME"); //32
            DataModel_ExistPiece.SetVariable(34, "PARTY_CITY"); //32
            DataModel_ExistPiece.SetVariable(35, "ACTUAL_WEIGTH");
            DataModel_ExistPiece.SetVariable(36, "PIECE_STATUS");
            DataModel_ExistPiece.SetVariable(37, "ACTUAL_LENGTH");
            DataModel_ExistPiece.SetVariable(38, "ACTUAL_WIDTH");

            for (int i = 0; i <= 38; i++) {
                DataModel_ExistPiece.SetReadOnly(i);
            }

            TableExist.getColumnModel().getColumn(0).setMinWidth(20);
            TableExist.getColumnModel().getColumn(1).setMinWidth(90);
            TableExist.getColumnModel().getColumn(2).setMinWidth(70);
            TableExist.getColumnModel().getColumn(3).setMinWidth(120);
            TableExist.getColumnModel().getColumn(4).setMinWidth(70);
            TableExist.getColumnModel().getColumn(5).setMinWidth(100);
            TableExist.getColumnModel().getColumn(6).setMinWidth(120);
            TableExist.getColumnModel().getColumn(7).setMinWidth(80);
            TableExist.getColumnModel().getColumn(8).setMinWidth(70);
            TableExist.getColumnModel().getColumn(9).setMinWidth(70);
            TableExist.getColumnModel().getColumn(10).setMinWidth(50);
            TableExist.getColumnModel().getColumn(11).setMinWidth(130);
            TableExist.getColumnModel().getColumn(12).setMinWidth(80);
            TableExist.getColumnModel().getColumn(13).setMinWidth(80);
            TableExist.getColumnModel().getColumn(14).setMinWidth(100);
            TableExist.getColumnModel().getColumn(15).setMinWidth(100);
            TableExist.getColumnModel().getColumn(16).setMinWidth(0);
            TableExist.getColumnModel().getColumn(16).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(17).setMinWidth(0);
            TableExist.getColumnModel().getColumn(17).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(18).setMinWidth(0);
            TableExist.getColumnModel().getColumn(18).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(19).setMinWidth(0);
            TableExist.getColumnModel().getColumn(19).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(20).setMinWidth(0);
            TableExist.getColumnModel().getColumn(20).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(21).setMinWidth(0);
            TableExist.getColumnModel().getColumn(21).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(22).setMinWidth(0);
            TableExist.getColumnModel().getColumn(22).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(23).setMinWidth(0);
            TableExist.getColumnModel().getColumn(23).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(24).setMinWidth(0);
            TableExist.getColumnModel().getColumn(24).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(25).setMinWidth(0);
            TableExist.getColumnModel().getColumn(25).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(26).setMinWidth(0);
            TableExist.getColumnModel().getColumn(26).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(27).setMinWidth(0);
            TableExist.getColumnModel().getColumn(27).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(28).setMinWidth(0);
            TableExist.getColumnModel().getColumn(28).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(29).setMinWidth(0);
            TableExist.getColumnModel().getColumn(29).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(30).setMinWidth(0);
            TableExist.getColumnModel().getColumn(30).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(31).setMinWidth(120);
            TableExist.getColumnModel().getColumn(31).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(32).setMinWidth(120);
            TableExist.getColumnModel().getColumn(32).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(33).setMinWidth(120);
            TableExist.getColumnModel().getColumn(33).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(34).setMinWidth(120);
            TableExist.getColumnModel().getColumn(34).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(35).setMinWidth(120);
            TableExist.getColumnModel().getColumn(35).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(36).setMinWidth(120);
            TableExist.getColumnModel().getColumn(36).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(37).setMinWidth(120);
            TableExist.getColumnModel().getColumn(37).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(38).setMinWidth(120);
            TableExist.getColumnModel().getColumn(38).setMaxWidth(120);
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
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        S_O_NO = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        PARTY_CODE = new javax.swing.JTextField();
        PARTY_NAME = new javax.swing.JTextField();
        ORDER_VALUE = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableExist = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblRevNo = new javax.swing.JLabel();
        lblInchargeName = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        reason = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        REMARK1 = new javax.swing.JTextField();
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

        jLabel2.setText("P.D.Date");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 20, 80, 14);

        jLabel3.setText("Diversion No");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(240, 20, 100, 14);

        jLabel5.setText("Remark");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 230, 80, 30);

        REMARK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                REMARKFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                REMARKFocusLost(evt);
            }
        });
        jPanel1.add(REMARK);
        REMARK.setBounds(90, 230, 780, 30);

        jLabel7.setText("District");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(550, 90, 80, 30);

        jLabel8.setText("Engineer");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 100, 110, 20);

        S_O_NO.setEditable(false);
        S_O_NO.setBackground(new java.awt.Color(254, 242, 230));
        S_O_NO.setText("S00000001");
        S_O_NO.setEnabled(false);
        jPanel1.add(S_O_NO);
        S_O_NO.setBounds(330, 10, 200, 20);

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
        Table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TableFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TableFocusLost(evt);
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
        jScrollPane1.setBounds(10, 310, 900, 70);

        jLabel9.setText("Party Code");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 60, 100, 20);

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
        PARTY_CODE.setBounds(90, 50, 130, 20);

        PARTY_NAME.setEditable(false);
        jPanel1.add(PARTY_NAME);
        PARTY_NAME.setBounds(320, 50, 330, 20);

        ORDER_VALUE.setEditable(false);
        ORDER_VALUE.setBackground(new java.awt.Color(209, 206, 203));
        ORDER_VALUE.setText("0");
        jPanel1.add(ORDER_VALUE);
        ORDER_VALUE.setBounds(750, 640, 170, 20);

        jLabel4.setText("Order Value ");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(660, 640, 90, 30);

        jLabel15.setText("Region");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(660, 60, 33, 14);

        jLabel6.setText("Country");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(710, 90, 70, 30);

        DISTRICT.setEditable(false);
        jPanel1.add(DISTRICT);
        DISTRICT.setBounds(600, 90, 100, 20);

        COUNTRY.setEditable(false);
        jPanel1.add(COUNTRY);
        COUNTRY.setBounds(770, 90, 100, 20);

        REGION.setEditable(false);
        jPanel1.add(REGION);
        REGION.setBounds(720, 50, 150, 30);

        S_ENGINEER.setEditable(false);
        jPanel1.add(S_ENGINEER);
        S_ENGINEER.setBounds(90, 90, 70, 20);

        jLabel16.setText("City");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(390, 100, 80, 14);

        CITY.setEditable(false);
        jPanel1.add(CITY);
        CITY.setBounds(430, 90, 110, 20);

        REMOVE.setText("REMOVE");
        REMOVE.setEnabled(false);
        REMOVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REMOVEActionPerformed(evt);
            }
        });
        jPanel1.add(REMOVE);
        REMOVE.setBounds(540, 640, 100, 23);

        S_O_DATE.setEditable(false);
        jPanel1.add(S_O_DATE);
        S_O_DATE.setBounds(90, 10, 120, 30);

        jLabel1.setText("Existing Piece Detail ( PIECE MASTER )");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 410, 270, 20);

        TableExist.setModel(new javax.swing.table.DefaultTableModel(
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
        TableExist.setSelectionBackground(new java.awt.Color(208, 220, 234));
        TableExist.setSelectionForeground(new java.awt.Color(231, 16, 16));
        TableExist.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TableExistFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TableExistFocusLost(evt);
            }
        });
        TableExist.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableExistKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(TableExist);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 430, 900, 70);

        jLabel17.setText("New Piece Detail");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 290, 120, 20);

        jLabel18.setText("Sales ");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(10, 80, 110, 30);

        jLabel10.setText("Party Name");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(240, 50, 120, 30);

        lblRevNo.setText("...");
        jPanel1.add(lblRevNo);
        lblRevNo.setBounds(220, 20, 34, 20);
        jPanel1.add(lblInchargeName);
        lblInchargeName.setBounds(170, 90, 160, 30);

        jLabel11.setText("Reason For Diversion");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(10, 150, 130, 20);

        reason.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Party's Urgency", "Delivery Delay", "Old Stock Liquidation" }));
        jPanel1.add(reason);
        reason.setBounds(180, 140, 240, 30);

        jLabel12.setText("Delivery Status for Existing Client");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(10, 190, 210, 30);

        REMARK1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                REMARK1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                REMARK1FocusLost(evt);
            }
        });
        jPanel1.add(REMARK1);
        REMARK1.setBounds(250, 190, 620, 30);

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
        jLabel31.setBounds(10, 23, 66, 14);

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
        cmbHierarchy.setBounds(90, 20, 180, 20);

        jLabel32.setText("From");
        Tab2.add(jLabel32);
        jLabel32.setBounds(10, 62, 56, 14);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFrom);
        txtFrom.setBounds(90, 60, 180, 20);

        jLabel35.setText("Remarks");
        Tab2.add(jLabel35);
        jLabel35.setBounds(10, 95, 62, 14);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        Tab2.add(txtFromRemarks);
        txtFromRemarks.setBounds(90, 95, 530, 20);

        jLabel36.setText("Your Action  ");
        Tab2.add(jLabel36);
        jLabel36.setBounds(10, 130, 81, 14);

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
        OpgApprove.setBounds(6, 6, 171, 23);

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
        jLabel33.setBounds(10, 253, 60, 14);

        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        Tab2.add(cmbSendTo);
        cmbSendTo.setBounds(90, 250, 180, 20);

        jLabel34.setText("Remarks");
        Tab2.add(jLabel34);
        jLabel34.setBounds(10, 292, 60, 14);

        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        Tab2.add(txtToRemarks);
        txtToRemarks.setBounds(90, 290, 570, 20);

        cmdBackToTab0.setText("<< Back");
        cmdBackToTab0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackToTab0ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBackToTab0);
        cmdBackToTab0.setBounds(450, 400, 102, 23);

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
        cmdNextToTab2.setBounds(570, 400, 102, 23);

        jPanel2.add(Tab2);
        Tab2.setBounds(10, 0, 760, 460);

        Tab.addTab("Approval", jPanel2);

        jPanel3.setLayout(null);

        StatusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusPanel.setLayout(null);

        jLabel60.setText("Document Approval Status");
        StatusPanel.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 14);

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
        jLabel19.setBounds(10, 170, 182, 14);

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
        txtAuditRemarks.setBounds(570, 260, 129, 20);

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

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed

    }//GEN-LAST:event_TableKeyPressed
    private void cal_order_value() {
        float inv_amt = 0;
        for (int i = 0; i < Table.getRowCount(); i++) {
            if (!Table.getValueAt(i, 29).toString().equals("")) {
                inv_amt = inv_amt + Float.parseFloat(Table.getValueAt(i, 29).toString());
            }
        }
        ORDER_VALUE.setText(inv_amt + "");
    }
    private void PARTY_CODEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PARTY_CODEFocusLost
        lblStatus1.setText("");
        if (data.getIntValueFromDB("SELECT COUNT(*) FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + PARTY_CODE.getText() + "'") > 0) {
            DiversionPieceDetail();
        } else {
            if (PARTY_CODE.getText().trim().length() > 0) {
                JOptionPane.showMessageDialog(null, "Invalid Party Code...");
            }
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

        if (evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE='210010'";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    PARTY_CODE.setText(aList.ReturnVal);
                    DiversionPieceDetail();
                }
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
        }
    }//GEN-LAST:event_REMOVEActionPerformed

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

    private void TableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusLost
        // TODO add your handling code here:
        lblStatus1.setText("");
    }//GEN-LAST:event_TableFocusLost

    private void TableExistFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableExistFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_TableExistFocusGained

    private void TableExistFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableExistFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_TableExistFocusLost

    private void TableExistKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableExistKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TableExistKeyPressed

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

    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased
        // TODO add your handling code here:
        float length = Float.parseFloat(DataModel.getValueByVariable("PR_BILL_LENGTH", 0));
        float width = Float.parseFloat(DataModel.getValueByVariable("PR_BILL_WIDTH", 0));
        float SQMT = (length * width);

        DataModel.setValueByVariable("PR_BILL_SQMTR", EITLERPGLOBAL.round(SQMT, 2) + "", 0);

        FeltInvCalc inv_calc = new FeltInvCalc();

        try {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            inv_calc = clsOrderValueCalc.calculate("", DataModel.getValueByVariable("PRODUCT", 0), PARTY_CODE.getText(), length, width, Float.parseFloat(DataModel.getValueByVariable("PR_BILL_WEIGHT", 0)), SQMT, df1.format(df.parse(S_O_DATE.getText())));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        DataModel.setValueByVariable("BASE_ORDER_AMT", EITLERPGLOBAL.round(inv_calc.getFicBasAmount(), 2) + "", 0);

        float existing_base_amt = Float.parseFloat(DataModel.getValueByVariable("BASE_EXISTING_PIECE_AMT", 0));
        float difference = existing_base_amt - inv_calc.getFicBasAmount();
        float discount = 0;

        String Product_Code_existing = DataModel_ExistPiece.getValueByVariable("PRODUCT", 0);

        if (Product_Code_existing.equals("729000") || Product_Code_existing.equals("7290000")) {
            String Disc_Per = data.getStringValueFromDB("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + inv_calc_existing.getFicPartyCode() + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + inv_calc_existing.getFicProductCode() + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO >= '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
            float d_per = Float.parseFloat(Disc_Per);
            discount = (difference * d_per) / 100;
            discount = Math.round(discount);
            DataModel.setValueByVariable("DISCOUNT_PER", EITLERPGLOBAL.round(d_per, 2) + "", 0);
            DataModel.setValueByVariable("DISCOUNT_AMT", discount + "", 0);
            difference = difference - discount;
        } else {
            DataModel.setValueByVariable("DISCOUNT_PER", "NA", 0);
            DataModel.setValueByVariable("DISCOUNT_AMT", "NA", 0);
        }
        difference = Math.round(difference);
        float WIDTH = 0;
        String existing_stage = DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0);
        if ("IN STOCK".equals(existing_stage) || "STOCK".equals(existing_stage)) {

            WIDTH = Float.parseFloat(DataModel_ExistPiece.getValueByVariable("ACTUAL_WIDTH", 0));
        } else {
            WIDTH = Float.parseFloat(DataModel_ExistPiece.getValueByVariable("WIDTH", 0));
        }
        try {
            if (Product_Code_existing.equals("719011") || Product_Code_existing.equals("7190110")
                    || Product_Code_existing.equals("719021") || Product_Code_existing.equals("7190210")
                    || Product_Code_existing.equals("719031") || Product_Code_existing.equals("7190310")
                    || Product_Code_existing.equals("719041") || Product_Code_existing.equals("7190410")
                    || Product_Code_existing.equals("719051") || Product_Code_existing.equals("7190510")) {

                double SEAM_CHARGE = 0;
                double seam_width = WIDTH - inv_calc.getFicWidth();
                SEAM_CHARGE = seam_width * 4899;
                DataModel.setValueByVariable("OV_SEAM_CHG", EITLERPGLOBAL.round(SEAM_CHARGE, 2) + "", 0);
                difference = difference + Float.parseFloat(EITLERPGLOBAL.round(SEAM_CHARGE, 2) + "");
            } else {
                DataModel.setValueByVariable("OV_SEAM_CHG", "0", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataModel.setValueByVariable("DIFFERENCE_AMT", EITLERPGLOBAL.round(difference, 2) + "", 0);
        if (difference <= 0) {
            DataModel.setValueByVariable("DB_NOTE", "NA", 0);
            DataModel.setValueByVariable("DB_AMT", "NA", 0);
        } else {
            if (DataModel.getValueByVariable("DB_NOTE", 0).equals("NA")) {
                DataModel.setValueByVariable("DB_NOTE", "", 0);
                DataModel.setValueByVariable("DB_AMT", "", 0);
            }
        }
    }//GEN-LAST:event_TableKeyReleased

    private void REMARK1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REMARK1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_REMARK1FocusGained

    private void REMARK1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REMARK1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_REMARK1FocusLost
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
    }

    private void Save() {

        if (Table.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(this, "Enter Piece Updation Details Before Saving.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        if (PARTY_CODE.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Valid PARTY CODE.", "ERROR", JOptionPane.ERROR_MESSAGE);
            PARTY_CODE.requestFocus();
            return;
        }

        if (OpgFinal.isSelected()) {

            String Stage = DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0);
            if (Stage.equals("FINISHING") || Stage.equals("NEEDLING") || Stage.equals("MENDING") || Stage.equals("WARPING") || Stage.equals("WEAVING") || Stage.equals("STOCK") || Stage.equals("IN STOCK")) {
                System.out.println("Final Approval Ready...!");
            } else {
                JOptionPane.showMessageDialog(null, "Piece can not divert in Stage " + Stage + " ");
                return;
            }
            //STEP 1
            System.out.println("Selected Hierarchy  : " + SelHierarchyID + " User Id : " + EITLERPGLOBAL.gNewUserID);
            if (EITLERPGLOBAL.gNewUserID != 243 && EITLERPGLOBAL.gNewUserID != 262) {
                HashMap hmSendToList = new HashMap();
                hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID, true);
                boolean Adityabhai_exist = false;

                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));

                    int user_id = (int) ObjUser.getAttribute("USER_ID").getVal();
                    System.out.println("User Id checked : " + user_id);
                    if (user_id == 243 || user_id == 262) {
                        System.out.println("ABP Exist : User Id = " + user_id);
                        Adityabhai_exist = true;
                    }
                }

                double profit_loss = Double.parseDouble(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
                System.out.println("Profit(-) & Loss(+) : " + profit_loss + " Exist : " + Adityabhai_exist);
                if (!Adityabhai_exist) {
                    System.out.println("ABP Exist  document can not final approve ");
                    JOptionPane.showMessageDialog(null, "Aditya Sir is not included in Hierarchy. So Prior Approval of Diversion will not Final Approve.");
                    return;
                }
            }
            //STEP1 END
        }

        if (EditMode == EITLERPGLOBAL.ADD) {
            double profit_loss = Double.parseDouble(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));

            System.out.println("Selected Hierarchy  : " + SelHierarchyID + " , Profit & Loss : " + profit_loss);
            HashMap hmSendToList = new HashMap();
            hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
            ArrayList<Integer> user_list = new ArrayList<>();
            for (int i = 1; i <= hmSendToList.size(); i++) {
                clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));

                int user_id = (int) ObjUser.getAttribute("USER_ID").getVal();
                user_list.add(user_id);
            }

            if (user_list.contains(243) || user_list.contains(262)) {
                System.out.println("Ready with Adityabhai...!");
            } else {
                JOptionPane.showMessageDialog(null, "Please Select Hierarchy of Aditya Sir.");
                return;
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
                aList.ModuleID = ModuleId;
                aList.FirstFreeNo = 263;
                FFNo = aList.FirstFreeNo;
                clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, true);

                //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='InProcess' WHERE PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");

                if (OpgFinal.isSelected()) {
//                    try {
//                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='Diverted' WHERE PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    try {
                        String DOC_NO = S_O_NO.getText();
                        String DOC_DATE = S_O_DATE.getText();
                        String Party_Code = PARTY_CODE.getText();

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
//                    try {
//                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='Diverted' WHERE PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE = 'DIVERTED',PR_DIVERSION_FLAG = '', PR_DIVERTED_FLAG = 1, PR_DIVERTED_REASON = '" + REMARK.getText() + "' WHERE PR_PIECE_NO ='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "' ");
//                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE = 'DIVERTED', PR_DIVERSION_FLAG = '', PR_DIVERTED_FLAG = 1, PR_DIVERTED_REASON = '" + REMARK.getText() + "' WHERE PR_PIECE_NO ='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "' ");
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    try {
                        String DOC_NO = S_O_NO.getText();
                        String DOC_DATE = S_O_DATE.getText();
                        String Party_Code = PARTY_CODE.getText();

                        String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true);
                        System.out.println("Send Mail Responce : " + responce);

                    } catch (Exception e) {
                        e.printStackTrace();
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
        if (feltOrder.IsEditable(productionDocumentNo, EITLERPGLOBAL.gAuthorityUserID)) {
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

    private void filterHierarchyCombo() {
        GenerateHierarchyCombo();

        String incharge = data.getStringValueFromDB("SELECT DESIGNER_INCHARGE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + PARTY_CODE.getText() + "'");
        System.out.println("Selected Incharge : " + incharge);

        if (incharge.equals("")) {
            if (!designer_not_msg) {
                //JOptionPane.showMessageDialog(this, "For Party "+PARTY_CODE.getText()+" Designer is not selected");
                System.out.println("For Party " + PARTY_CODE.getText() + " Designer is not selected");
                designer_not_msg = true;
            }
            //cmbHierarchy.setEnabled(true);
        }

        if (!DataModel.getValueByVariable("DIFFERENCE_AMT", 0).equals("")) {
            difference = Float.parseFloat(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
        }
        if (difference < 0) {
            //Gain - STOCK
            if ("IN STOCK".equals(PR_PIECE_STAGE) || "STOCK".equals(PR_PIECE_STAGE)) {

                switch (incharge) {
                    case "2": //SJP
                    {
                        ArrayList<String> Hierarchy = new ArrayList<>();

                        Hierarchy.add("AC-SJP-BP-MU");
                        Hierarchy.add("AP-SJP-BP-MU");
                        Hierarchy.add("SR-SJP-BP-MU");

                        filter(Hierarchy);
                        break;
                    }
                    case "1": //KM
                    {
                        ArrayList<String> Hierarchy = new ArrayList<>();

                        Hierarchy.add("AC-KM-BP-MU");
                        Hierarchy.add("AP-KM-BP-MU");
                        Hierarchy.add("SR-KM-BP-MU");

                        filter(Hierarchy);
                        break;
                    }
                }
            } //GAIN - WIP
            else {

                switch (incharge) {
                    case "2": //SJP
                    {
                        ArrayList<String> Hierarchy = new ArrayList<>();

                        Hierarchy.add("AC-SJP-BP-MU");
                        Hierarchy.add("AP-SJP-BP-MU");
                        Hierarchy.add("SR-SJP-BP-MU");

                        filter(Hierarchy);
                        break;
                    }
                    case "1": //KM
                    {
                        ArrayList<String> Hierarchy = new ArrayList<>();

                        Hierarchy.add("AC-KM-BP-MU");
                        Hierarchy.add("AP-KM-BP-MU");
                        Hierarchy.add("SR-KM-BP-MU");

                        filter(Hierarchy);
                        break;
                    }
                }
            }
        } //No Gain No Loss
        else if (difference == 0) {

            switch (incharge) {
                case "2": //SJP
                {
                    ArrayList<String> Hierarchy = new ArrayList<>();

                    Hierarchy.add("AC-SJP-BP-MU");
                    Hierarchy.add("AP-SJP-BP-MU");
                    Hierarchy.add("SR-SJP-BP-MU");

                    filter(Hierarchy);
                    break;
                }
                case "1": //KM
                {
                    ArrayList<String> Hierarchy = new ArrayList<>();

                    Hierarchy.add("AC-KM-BP-MU");
                    Hierarchy.add("AP-KM-BP-MU");
                    Hierarchy.add("SR-KM-BP-MU");

                    filter(Hierarchy);
                    break;
                }
            }
        } //Loss
        else {
            //Loss - By Party
            ////Loss - By Party - WIP

            switch (incharge) {
                case "2": //SJP
                {
                    ArrayList<String> Hierarchy = new ArrayList<>();

                    Hierarchy.add("AC-SJP-HP-MA-BP-MU-ABP");
                    Hierarchy.add("AP-SJP-HP-MA-BP-MU-ABP");
                    Hierarchy.add("SR-SJP-HP-MA-BP-MU-ABP");

                    filter(Hierarchy);
                    break;
                }
                case "1": //KM
                {
                    ArrayList<String> Hierarchy = new ArrayList<>();

                    Hierarchy.add("AC-KM-HP-MA-BP-MU-ABP");
                    Hierarchy.add("AP-KM-HP-MA-BP-MU-ABP");
                    Hierarchy.add("SR-KM-HP-MA-BP-MU-ABP");

                    filter(Hierarchy);
                    break;
                }
            }

        } //Loss - By Company

        //Loss - By Company - STOCK
        if ("IN STOCK".equals(PR_PIECE_STAGE) || "STOCK".equals(PR_PIECE_STAGE)) {

            switch (incharge) {
                case "2": //SJP
                {
                    ArrayList<String> Hierarchy = new ArrayList<>();

                    Hierarchy.add("AC-SJP-BP-MU-ABP");
                    Hierarchy.add("AP-SJP-BP-MU-ABP");
                    Hierarchy.add("SR-SJP-BP-MU-ABP");

                    filter(Hierarchy);
                    break;
                }
                case "1": //KM
                {
                    ArrayList<String> Hierarchy = new ArrayList<>();

                    Hierarchy.add("AC-KM-BP-MU-ABP");
                    Hierarchy.add("AP-KM-BP-MU-ABP");
                    Hierarchy.add("SR-KM-BP-MU-ABP");

                    filter(Hierarchy);
                    break;
                }
            }
        } //Loss - By Company - WIP
        else {

            switch (incharge) {
                case "2": //SJP
                {
                    ArrayList<String> Hierarchy = new ArrayList<>();

                    Hierarchy.add("AC-SJP-BP-MU-ABP");
                    Hierarchy.add("AP-SJP-BP-MU-ABP");
                    Hierarchy.add("SR-SJP-BP-MU-ABP");

                    filter(Hierarchy);
                    break;
                }
                case "1": //KM
                {
                    ArrayList<String> Hierarchy = new ArrayList<>();

                    Hierarchy.add("AC-KM-BP-MU-ABP");
                    Hierarchy.add("AP-KM-BP-MU-ABP");
                    Hierarchy.add("SR-KM-BP-MU-ABP");

                    filter(Hierarchy);
                    break;
                }
            }
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
        Table.setEnabled(true);
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

        Table.setEnabled(pStat);
        TableExist.setEnabled(pStat);

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
        feltOrder.setAttribute("SD_ORDER_NO", S_O_NO.getText());
        feltOrder.setAttribute("SD_ORDER_DATE", S_O_DATE.getText());
        feltOrder.setAttribute("D_REGION", REGION.getText());
        feltOrder.setAttribute("D_SALES_ENGINEER", S_ENGINEER.getText());
        feltOrder.setAttribute("D_PARTY_CODE", PARTY_CODE.getText());
        feltOrder.setAttribute("D_PARTY_NAME", PARTY_NAME.getText());
        feltOrder.setAttribute("D_REMARK", REMARK.getText());
        feltOrder.setAttribute("D_REASON_FOR_DIVERSION", reason.getSelectedItem().toString());
        feltOrder.setAttribute("D_DELIVERY_STATUS_FOR_EXISTING_CLIENT", REMARK1.getText());
        feltOrder.setAttribute("D_PIECE_NO", DataModel.getValueByVariable("PIECE_NO", 0));
        feltOrder.setAttribute("D_PRODUCT_NO", DataModel.getValueByVariable("PRODUCT", 0));
        feltOrder.setAttribute("D_PRODUCT_DESC", DataModel.getValueByVariable("DESCRIPTION", 0));
        feltOrder.setAttribute("D_MACHINE_NO", DataModel.getValueByVariable("MACHINE_NO", 0));
        feltOrder.setAttribute("D_POSITION_NO", DataModel.getValueByVariable("POSITION", 0));
        feltOrder.setAttribute("D_POSITION_DESC", DataModel.getValueByVariable("POSITION_DESC", 0));
        feltOrder.setAttribute("D_STYLE_NO", DataModel.getValueByVariable("STYLE", 0));
        feltOrder.setAttribute("D_GROUP", DataModel.getValueByVariable("GROUP", 0));
        feltOrder.setAttribute("D_LENGTH", DataModel.getValueByVariable("LENGTH", 0));
        feltOrder.setAttribute("D_WIDTH", DataModel.getValueByVariable("WIDTH", 0));
        feltOrder.setAttribute("D_GSM", DataModel.getValueByVariable("GSM", 0));
        feltOrder.setAttribute("D_THORITICAL_WEIGHT", DataModel.getValueByVariable("THORTICAL_WEIGHT", 0));
        feltOrder.setAttribute("D_SQ_MTR", DataModel.getValueByVariable("SQ_MTR", 0));
        feltOrder.setAttribute("D_SYN_PER", DataModel.getValueByVariable("SYN", 0));
        feltOrder.setAttribute("D_OV_RATE", DataModel.getValueByVariable("OV_RATE", 0));
        feltOrder.setAttribute("D_OV_BAS_AMOUNT", DataModel.getValueByVariable("OV_BAS_AMOUNT", 0));
        feltOrder.setAttribute("D_OV_CHEM_TRT_CHG", "0");
        feltOrder.setAttribute("D_OV_SPIRAL_CHG", "0");
        feltOrder.setAttribute("D_OV_PIN_CHG", "0");
        feltOrder.setAttribute("D_OV_SEAM_CHG", DataModel.getValueByVariable("OV_SEAM_CHG", 0));
        feltOrder.setAttribute("D_OV_AMT", DataModel.getValueByVariable("OV_AMT", 0));

        if (EditMode != EITLERPGLOBAL.EDIT) {
            feltOrder.setAttribute("ORIGINAL_PARTY_CODE", PR_PARTY_CODE);
            feltOrder.setAttribute("ORIGINAL_PIECE_NO", PR_PIECE_NO);
            feltOrder.setAttribute("ORIGINAL_PRODUCT_NO", PR_PRODUCT_CODE);
            feltOrder.setAttribute("ORIGINAL_MACHINE_NO", PR_MACHINE_NO);
            feltOrder.setAttribute("ORIGINAL_POSITION_NO", PR_POSITION_NO);
            feltOrder.setAttribute("ORIGINAL_STYLE_NO", PR_STYLE);
            feltOrder.setAttribute("ORIGINAL_GROUP", PR_GROUP);
            feltOrder.setAttribute("ORIGINAL_LENGTH", PR_LENGTH + "");
            feltOrder.setAttribute("ORIGINAL_WIDTH", PR_WIDTH + "");
            feltOrder.setAttribute("ORIGINAL_GSM", PR_GSM + "");
            feltOrder.setAttribute("ORIGINAL_THORITICAL_WEIGHT", PR_THORITICAL_WEIGHT + "");
            feltOrder.setAttribute("ORIGINAL_SQ_MTR", PR_SQMTR + "");
            feltOrder.setAttribute("ORIGINAL_OV_RATE", inv_calc_existing.getFicRate());
            feltOrder.setAttribute("ORIGINAL_OV_AMOUNT", inv_calc_existing.getFicInvAmt());
        }

        feltOrder.setAttribute("DEBIT_NOTE_NO", DataModel.getValueByVariable("DB_NOTE", 0));
        feltOrder.setAttribute("DEBIT_AMT", DataModel.getValueByVariable("DB_AMT", 0));
        feltOrder.setAttribute("COST_BEARER", "SDML");
        feltOrder.setAttribute("BEARER_PARTY_CODE", "");
        feltOrder.setAttribute("BEARER_PARTY_NAME", "");
        feltOrder.setAttribute("DISCOUNT_PER", DataModel.getValueByVariable("DISCOUNT_PER", 0));
        feltOrder.setAttribute("DISCOUNT_AMT", DataModel.getValueByVariable("DISCOUNT_AMT", 0));
        feltOrder.setAttribute("DIFFERENCE_AMT", DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
        feltOrder.setAttribute("CGST_PER", Double.valueOf(DataModel.getValueByVariable("CGST_PER", 0)));
        feltOrder.setAttribute("CGST_AMT", Double.valueOf(DataModel.getValueByVariable("CGST_AMT", 0)));
        feltOrder.setAttribute("SGST_PER", Double.valueOf(DataModel.getValueByVariable("SGST_PER", 0)));
        feltOrder.setAttribute("SGST_AMT", Double.valueOf(DataModel.getValueByVariable("SGST_AMT", 0)));
        feltOrder.setAttribute("IGST_PER", Double.valueOf(DataModel.getValueByVariable("IGST_PER", 0)));
        feltOrder.setAttribute("IGST_AMT", Double.valueOf(DataModel.getValueByVariable("IGST_AMT", 0)));
        feltOrder.setAttribute("COMPOSITION_PER", 0);
        feltOrder.setAttribute("COMPOSITION_AMT", 0);
        feltOrder.setAttribute("RCM_PER", 0);
        feltOrder.setAttribute("RCM_AMT", 0);
        feltOrder.setAttribute("GST_COMPENSATION_CESS_PER", 0);
        feltOrder.setAttribute("GST_COMPENSATION_CESS_AMT", 0);
        feltOrder.setAttribute("PR_BILL_LENGTH", DataModel.getValueByVariable("PR_BILL_LENGTH", 0));
        feltOrder.setAttribute("PR_BILL_WIDTH", DataModel.getValueByVariable("PR_BILL_WIDTH", 0));
        feltOrder.setAttribute("PR_BILL_WEIGHT", DataModel.getValueByVariable("PR_BILL_WEIGHT", 0));
        feltOrder.setAttribute("PR_BILL_SQMTR", DataModel.getValueByVariable("PR_BILL_SQMTR", 0));
        feltOrder.setAttribute("PR_BILL_GSM", DataModel.getValueByVariable("PR_BILL_GSM", 0));
        feltOrder.setAttribute("PR_BILL_PRODUCT_CODE", DataModel.getValueByVariable("PR_BILL_PRODUCT_CODE", 0));
        feltOrder.setAttribute("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        feltOrder.setAttribute("CANCELED", false);
        feltOrder.setAttribute("BASE_ORDER_AMT", DataModel.getValueByVariable("BASE_ORDER_AMT", 0));
        feltOrder.setAttribute("BASE_EXISTING_PIECE_AMT", DataModel.getValueByVariable("BASE_EXISTING_PIECE_AMT", 0));

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
    private javax.swing.JTextField CITY;
    private javax.swing.JTextField COUNTRY;
    private javax.swing.JTextField DISTRICT;
    private javax.swing.JTextField ORDER_VALUE;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTextField PARTY_CODE;
    private javax.swing.JTextField PARTY_NAME;
    private javax.swing.JTextField REGION;
    private javax.swing.JTextField REMARK;
    private javax.swing.JTextField REMARK1;
    private javax.swing.JButton REMOVE;
    private javax.swing.JTextField S_ENGINEER;
    private javax.swing.JFormattedTextField S_O_DATE;
    private javax.swing.JTextField S_O_NO;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable Table;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableExist;
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
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblInchargeName;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JComboBox<String> reason;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables
private void DiversionPieceDetail() {
        searchkey search = new searchkey();
        search.SQL = "SELECT MM_MACHINE_NO AS MACHINE_NO,MM_MACHINE_POSITION AS POSITION,MM_MACHINE_POSITION_DESC AS POSITION_DESC,MM_ITEM_CODE AS ITEM_CODE,MM_GRUP AS GRUP,(MM_FELT_LENGTH+MM_FABRIC_LENGTH) AS LENGTH,(MM_FELT_WIDTH+MM_FABRIC_WIDTH) AS WIDTH,MM_FELT_GSM AS GSM,concat(MM_FELT_STYLE,MM_STYLE_DRY) AS STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='" + PARTY_CODE.getText() + "' AND (MM_MACHINE_NO!='' AND MM_MACHINE_POSITION!='' AND (MM_FELT_LENGTH != '' OR MM_FABRIC_LENGTH != '') AND (MM_FELT_WIDTH != '' OR MM_FABRIC_WIDTH != '') AND MM_FELT_GSM!='') ORDER BY  MM_MACHINE_NO,MM_MACHINE_POSITION";
        search.ReturnCol = 1;
        search.Party_Code = PARTY_CODE.getText();
        search.ShowReturnCol = true;
        search.DefaultSearchOn = 1;
        String Selemachine = "", selepositon = "";
        if (search.ShowRSLOV()) {
            Selemachine = search.ReturnVal;
            seltyp = search.ReturnVal;
            selepositon = search.SecondVal;

            clsSales_Party objParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText(), "210010");
            PARTY_NAME.setText(objParty.getAttribute("PARTY_NAME").getString());
            REGION.setText(objParty.getAttribute("ZONE").getString());
            S_ENGINEER.setText(objParty.getAttribute("INCHARGE_CD").getString());
            CITY.setText(objParty.getAttribute("CITY_ID").getString());
            DISTRICT.setText(objParty.getAttribute("DISTRICT").getString());
            COUNTRY.setText(objParty.getAttribute("COUNTRY_ID").getString());

            String SQL = "SELECT * FROM (SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK "
                    + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_MACHINE_NO!='' AND PR_POSITION_NO!='' AND PR_LENGTH!='' AND PR_WIDTH!='' AND PR_GSM!='' AND PR_DIVERSION_FLAG='READY' AND PR_PIECE_STAGE IN ('FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK') AND PR_PARTY_CODE!='" + PARTY_CODE.getText() + "' "
                    + " UNION ALL SELECT PR_PIECE_NO,PR_MACHINE_NO,PR_POSITION_NO,PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP,PR_STYLE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_PIECE_STAGE,PR_DELINK "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE "
                    + "PR_MACHINE_NO!='' AND PR_POSITION_NO!='' AND  "
                    + "PR_LENGTH!='' AND PR_WIDTH!='' AND PR_GSM!='' AND "
                    + "PR_PARTY_CODE='" + PARTY_CODE.getText() + "' AND PR_DIVERSION_FLAG='READY' "
                    + "AND PR_PIECE_STAGE IN ('FINISHING','NEEDLING','MENDING','WARPING','STOCK','IN STOCK') "
                    + "AND PR_DELINK!='DELINK') AS AA "
                    + "WHERE LEFT(PR_PIECE_NO,5) NOT IN "
                    + "(SELECT LEFT(D_PIECE_NO,5) FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE CANCELED=0)";

            searchkey_diversionlist search_dv = new searchkey_diversionlist();

            search_dv.SQL = SQL;
            search_dv.ReturnCol = 1;
            search_dv.Party_Code = PARTY_CODE.getText();
            search_dv.ShowReturnCol = true;
            search_dv.DefaultSearchOn = 1;

            if (search_dv.ShowRSLOV()) {
                seleval = search_dv.ReturnVal;
                seltyp = search_dv.ReturnVal;
                String secondval = search_dv.SecondVal;
                String LastQuery = search_dv.SQL;
                try {
                    ResultSet tpiecers = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + search_dv.ReturnVal + "'");
                    tpiecers.first();
                    this.frm_S_O_No = S_O_NO.getText();
                    this.frm_PARTY_CODE = PARTY_CODE.getText();
                    this.frm_REMARK = REMARK.getText();
                    this.frm_PARTY_CODE = PARTY_CODE.getText();
                    this.frm_Piece_No = search_dv.ReturnVal;
                    //this.frm_Machine_No = tpiecers.getString("PR_MACHINE_NO");
                    //this.frm_Position_No = tpiecers.getString("PR_POSITION_NO");
                    this.frm_Machine_No = Selemachine;
                    this.frm_Position_No = selepositon;
                    DataSettingFromOrder();
                } catch (Exception e) {

                }
            }
        }
    }
}
