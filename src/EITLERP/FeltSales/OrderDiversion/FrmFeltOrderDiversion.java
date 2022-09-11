/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.OrderDiversion;

import EITLERP.AppletFrame;
import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.DiversionLoss.FrmFeltDiversionLoss;
import EITLERP.FeltSales.DiversionLoss.FrmFeltOrderDiversionLoss;
import EITLERP.FeltSales.FeltFinishing.frmFeltFinishing;
import EITLERP.FeltSales.FeltFinishing.frmFeltFinishingReport;
import EITLERP.FeltSales.common.FeltInvCalc;
import EITLERP.FeltSales.common.JavaMail;
import static EITLERP.FeltSales.common.JavaMail.SendMail;
//import EITLERP.FeltSales.common.Order_No_Conversion;
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
//import java.text.DecimalFormat;
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
import TReportWriter.SimpleDataProvider.TRow;
import TReportWriter.SimpleDataProvider.TTable;
import TReportWriter.TReportEngine;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FrmFeltOrderDiversion extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel;
    private EITLTableModel DataModel_ExistPiece;
    private EITLTableModel DataModel_NewParty;
    private EITLTableModel DataModel_ExistParty;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    private final int ModuleId = 604;
    private String DOC_NO = "";
    private clsFeltOrderDiversion feltOrder;
    private EITLComboModel cmbSendToModel;
    private TReportEngine objEngine = new TReportEngine();
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
    String PR_PIECE_NO = "", PR_PRODUCT_CODE = "", PR_PARTY_CODE = "", PR_ORDER_DATE = "", PR_MACHINE_NO = "", PR_POSITION_NO = "", PR_RCV_DATE = "", PR_ACTUAL_WEIGHT = "",PR_ACTUAL_LENGTH = "",PR_ACTUAL_WIDTH = "",PR_REQUESTED_MONTH="";
    float PR_LENGTH = 0, PR_WIDTH = 0, PR_THORITICAL_WEIGHT = 0, PR_SQMTR = 0;
    int PR_GSM = 0;
    String PR_SYN_PER = "", PR_STYLE = "", PR_GROUP = "";

    String PR_WEAVING_WEIGHT,PR_NEEDLING_WEIGHT;
    
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
        FormatGridNewParty();
        FormatGridExistParty();
        
        
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
            MaskFormatter dateMask2 = new MaskFormatter("##/##/####");
            dateMask2.setPlaceholderCharacter('_');
            dateMask2.install(REF_DATE);
            MaskFormatter dateMask3 = new MaskFormatter("##/##/####");
            dateMask3.setPlaceholderCharacter('_');
            dateMask3.install(P_O_DATE);
        } catch (ParseException ex) {
            System.out.println("Error on Mask " + ex.getMessage());
        }
        S_O_DATE.setText(df.format(new Date()));

        feltOrder = new clsFeltOrderDiversion();
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

        Add();
        PARTY_CODE.setText(frm_PARTY_CODE);
        
        String check_lock = data.getStringValueFromDB("SELECT COALESCE(PARTY_LOCK, 0) AS PARTY_LOCK FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='"+frm_PARTY_CODE+"'");
        if("1".equals(check_lock))
        {
            JOptionPane.showMessageDialog(null, "Party Code : "+frm_PARTY_CODE+" is locked.");
            PARTY_CODE.requestFocus();
            return;
        }
        
        txtPieceNo.setText(frm_Piece_No);
        
        //S_O_NO.setText(Order_No_Conversion.Order_No_STRING(frm_S_O_No+1));
        P_O_NO.setText(frm_PO_NO);
        P_O_DATE.setText(frm_PO_DATE);
        REFERENCE.setSelectedItem(frm_REFERENCE);
        REF_DATE.setText(frm_REFERENCE_DATE);
        REMARK.setText(frm_REMARK);
        lblDBNOTE.setText("");
        lblDbAmt.setText("");
        clsSales_Party objParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText(), "210010");
        PARTY_NAME.setText(objParty.getAttribute("PARTY_NAME").getString());
        REGION.setText(objParty.getAttribute("ZONE").getString());
        CITY.setText(objParty.getAttribute("CITY_ID").getString());
        DISTRICT.setText(objParty.getAttribute("DISTRICT").getString());
        String Country = data.getStringValueFromDB("SELECT COUNTRY_NAME FROM DINESHMILLS.D_SAL_COUNTRY_MASTER where COUNTRY_ID='" + objParty.getAttribute("COUNTRY_ID").getInt() + "'");
        //System.out.println("SELECT COUNTRY_NAME FROM DINESHMILLS.D_SAL_COUNTRY_MASTER where COUNTRY_ID='" + objParty.getAttribute("COUNTRY_ID").getInt() + "'");

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

            rsData = stmt.executeQuery("SELECT MM_MACHINE_NO,MM_MACHINE_POSITION,MM_MACHINE_POSITION_DESC,MM_ITEM_CODE,MM_GRUP,(MM_FELT_LENGTH+MM_FABRIC_LENGTH),(MM_FELT_WIDTH+MM_FABRIC_WIDTH),MM_FELT_GSM,concat(MM_FELT_STYLE,MM_STYLE_DRY) AS MM_FELT_STYLE FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_MACHINE_NO=" + frm_Machine_No + " AND MM_MACHINE_POSITION=" + frm_Position_No + " AND MM_PARTY_CODE = '" + PARTY_CODE.getText() + "'");

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

        
        String check_lock_MACHINE = data.getStringValueFromDB("SELECT COALESCE(MACHINE_LOCK_IND, 0) AS PARTY_LOCK FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MM_PARTY_CODE='"+frm_PARTY_CODE+"' AND MM_MACHINE_NO='"+Piecedetail[1]+"'");
        if("1".equals(check_lock_MACHINE))
        {
            JOptionPane.showMessageDialog(null, "Machine : "+Piecedetail[1]+" is locked for Party Code : "+frm_PARTY_CODE+".");
            PARTY_CODE.requestFocus();
            return;
        }
        String check_lock_POSITION = data.getStringValueFromDB("SELECT COALESCE(POSITION_LOCK_IND, 0) AS POSITION_LOCK_IND FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='"+frm_PARTY_CODE+"' AND MM_MACHINE_NO='"+Piecedetail[1]+"' AND MM_MACHINE_POSITION='"+Piecedetail[2]+"'");
        if("1".equals(check_lock_POSITION))
        {
            JOptionPane.showMessageDialog(null, "Machine : "+Piecedetail[1]+" AND Position : "+Piecedetail[2]+" is locked for Party Code : "+frm_PARTY_CODE+".");
            PARTY_CODE.requestFocus();
            return;
        }
        
        DataModel.setValueByVariable("MACHINE_NO", Piecedetail[1], 0);
        DataModel.setValueByVariable("POSITION", Piecedetail[2], 0);
        DataModel.setValueByVariable("POSITION_DESC", Pos_Desc, 0);

        String Position_Des_No = "";
        try{
        Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+Piecedetail[2]);
        //DataModel.setValueAt(PARTY_CODE.getText()+Piecedetail[0]+Position_Des_No, Table.getSelectedRow(), 4);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        String UPN_NewParty = PARTY_CODE.getText()+Piecedetail[1]+Position_Des_No;
        DataModel.setValueByVariable("POS_DESIGN_NO", Position_Des_No, 0);
        DataModel.setValueByVariable("UPN", PARTY_CODE.getText()+Piecedetail[1]+Position_Des_No, 0);
        
        DataModel.setValueByVariable("PRODUCT", Piecedetail[4], 0);
        float calculated_GSM=0;
                                     //FeltRateMasterServiceImpl frms = new FeltRateMasterServiceImpl();
        //FeltRateMaster rate_master = frms.getDetailByItemCode(Piecedetail[3]);
        //SELECT f FROM FeltRateMaster f WHERE f.feltRateMasterPK.itemCode = :itemCode
        String ITEM_DESC = "", SYN = "";
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
                                             //rsData = stmt.executeQuery("SELECT ITEM_DESC,SYN_PER FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Piecedetail[4]+"'");

            Piecedetail[4] = Piecedetail[4].substring(0, 6);

            //rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER,GROUP_NAME FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + Piecedetail[4] + "'");
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
           calculated_GSM = Float.parseFloat(Piecedetail[9]); 
            Theoritical_Weigth = (Float.parseFloat(Piecedetail[7]) * Float.parseFloat(Piecedetail[8]) * Float.parseFloat(Piecedetail[9]) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double SQMT = 0;
        try {
            SQMT = EITLERPGLOBAL.round((Double.parseDouble(Piecedetail[7]) * Double.parseDouble(Piecedetail[8])),2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataModel.setValueByVariable("THORTICAL_WEIGHT", EITLERPGLOBAL.round(Theoritical_Weigth,1)+"", 0);
        DataModel.setValueByVariable("SQ_MTR", EITLERPGLOBAL.round(SQMT,2)+"", 0);
        DataModel.setValueByVariable("STYLE", Piecedetail[6], 0);
        DataModel.setValueByVariable("SYN", SYN, 0);

        String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
        float SQM_RATE = 0, SQM_CHRG = 0, WT_RATE = 0, CHARGES = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;

        String Product_Code = Piecedetail[4];
        try {

            Conn = data.getConn();
            stmt = Conn.createStatement();
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

                                           // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
            // rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");
            Product_Code = Product_Code.substring(0, 6);
            System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + Product_Code + "' AND EFFECTIVE_FROM <= '" + df1.format(df.parse(S_O_DATE.getText())) + "' AND (EFFECTIVE_TO >= '" + df1.format(df.parse(S_O_DATE.getText())) + "' or EFFECTIVE_TO = \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
            rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + Product_Code + "' AND EFFECTIVE_FROM <= '" + df1.format(df.parse(S_O_DATE.getText())) + "' AND (EFFECTIVE_TO >= '" + df1.format(df.parse(S_O_DATE.getText())) + "' or EFFECTIVE_TO = \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");

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

        float Weight = 0;
        //EXISTING VALUES
        String UPN_ExistParty = "";
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            rsData = stmt.executeQuery("SELECT PR_PIECE_NO,PR_PRODUCT_CODE,PR_PARTY_CODE,PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,PR_SQMTR,PR_ORDER_DATE,PR_MACHINE_NO,PR_POSITION_NO,PR_SYN_PER,PR_STYLE,PR_GROUP,PR_PIECE_STAGE,PR_RCV_DATE,PR_ACTUAL_WEIGHT,PR_ACTUAL_LENGTH,PR_ACTUAL_WIDTH,PR_WEAVING_WEIGHT,PR_NEEDLING_WEIGHT,PR_REQUESTED_MONTH,PR_BILL_WEIGHT,PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + frm_Piece_No + "'");
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
            PR_WEAVING_WEIGHT = rsData.getString("PR_WEAVING_WEIGHT");
            PR_NEEDLING_WEIGHT = rsData.getString("PR_NEEDLING_WEIGHT");   
            PR_REQUESTED_MONTH = rsData.getString("PR_REQUESTED_MONTH");
            UPN_ExistParty  = rsData.getString("PR_UPN");
            System.out.println("WEAVING WEIGHT : "+PR_WEAVING_WEIGHT);
            System.out.println("NEEDLING WEIGHT : "+PR_NEEDLING_WEIGHT);
            System.out.println("ACTUAL WEIGHT : "+PR_ACTUAL_WEIGHT);

            float LENGTH=0,WIDTH=0;
            double sqmtr = 0;
            
            if ("IN STOCK".equals(PR_PIECE_STAGE) || "IN STOCK".equals(PR_PIECE_STAGE) || "STOCK".equals(PR_PIECE_STAGE)) {
                DataModel.setValueByVariable("PIECE_NO", PR_PIECE_NO + "V", 0);
                //LENGTH = Float.parseFloat(rsData.getString("PR_ACTUAL_LENGTH"));
                //WIDTH = Float.parseFloat(rsData.getString("PR_ACTUAL_WIDTH"));      
                
                LENGTH = Float.parseFloat(rsData.getString("PR_BILL_LENGTH"));
                WIDTH = Float.parseFloat(rsData.getString("PR_BILL_WIDTH"));  
                
                //Weight = Float.parseFloat(rsData.getString("PR_ACTUAL_WEIGHT"));
                Weight = Float.parseFloat(rsData.getString("PR_BILL_WEIGHT"));
                DataModel.setValueByVariable("PR_BILL_WEIGHT", Weight + "", 0);
            } else {
                DataModel.setValueByVariable("PIECE_NO", PR_PIECE_NO + "P", 0);
//                LENGTH = Float.parseFloat(rsData.getString(4));
//                WIDTH = Float.parseFloat(rsData.getString(5));   
                LENGTH = Float.parseFloat(rsData.getString("PR_BILL_LENGTH"));
                WIDTH = Float.parseFloat(rsData.getString("PR_BILL_WIDTH"));
                //Weight = PR_THORITICAL_WEIGHT;
                
                if("FINISHING".equals(PR_PIECE_STAGE)) {
                    Weight = Float.parseFloat(PR_NEEDLING_WEIGHT);
                    calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
                }
                else if("WEAVING".equals(PR_PIECE_STAGE) || "MENDING".equals(PR_PIECE_STAGE) || "NEEDLING".equals(PR_PIECE_STAGE) || "SEAMING".equals(PR_PIECE_STAGE)  || "SPLICING".equals(PR_PIECE_STAGE)) {
                    if("WEAVING".equals(PR_PIECE_STAGE))
                    {
                          
                        Weight = Float.parseFloat(rsData.getString("PR_BILL_WEIGHT"));
                    }
                    else
                    {
                        Weight = Float.parseFloat(PR_WEAVING_WEIGHT);
                    }
                    calculated_GSM = (Weight * 1000) / (LENGTH * WIDTH);  
                }
                else
                {
                    JOptionPane.showConfirmDialog(null, "Piece Stage is not valid for Diversion + "+PR_PIECE_STAGE);
                }
                
            }
            
            try{
                        TableColumn dateColumn_OC_MONTH = Table.getColumnModel().getColumn(DataModel.getColFromVariable("OC_MONTHYEAR"));
                        JComboBox monthbox = new JComboBox();

                        String month_name = "";
                        Date date = new Date();
                        int month;
                        int year = date.getYear() + 1900;

                        if ("IN STOCK".equals(PR_PIECE_STAGE) || "STOCK".equals(PR_PIECE_STAGE)) 
                        {
                            month = date.getMonth();
                        }
                        else
                        {
                            month = date.getMonth()+1;
                        }

                        monthbox.addItem("");
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
                            monthbox.addItem(month_name + " - " + year);
                        }

                        dateColumn_OC_MONTH.setCellEditor(new DefaultCellEditor(monthbox));


            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
            sqmtr = LENGTH * WIDTH;
            
//            float BILL_LENGTH=0,BILL_WIDTH=0,BILL_Weight = 0;
//            double BILL_sqmtr = 0;
//            String BILL_PRODUCT_CODE="";
//            try{
//                
//                BILL_LENGTH = Float.parseFloat(data.getStringValueFromDB("SELECT PR_BILL_LENGTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'"));
//                BILL_WIDTH = Float.parseFloat(data.getStringValueFromDB("SELECT PR_BILL_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'"));
//                BILL_Weight = Float.parseFloat(data.getStringValueFromDB("SELECT PR_BILL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'"));
//                BILL_sqmtr = Float.parseFloat(data.getStringValueFromDB("SELECT PR_BILL_SQMTR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'"));
//                BILL_PRODUCT_CODE = data.getStringValueFromDB("SELECT PR_BILL_PRODUCT_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'");
//                BILL_PRODUCT_CODE = data.getStringValueFromDB("SELECT PR_BILL_PRODUCT_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'");
//            }catch(Exception e)
//            {
//                e.printStackTrace();
//            }
            
            System.out.println("SQMTR Use for Calculation : "+sqmtr);
            inv_calc_existing = clsOrderValueCalc.calculateWithoutGSTINNO(rsData.getString(1), rsData.getString(2), rsData.getString(3), LENGTH, WIDTH, Weight ,Float.parseFloat(EITLERPGLOBAL.round(sqmtr, 2)+""), rsData.getString(9));

            if (!inv_calc_existing.getReason().equals("")) {
                JOptionPane.showMessageDialog(this, inv_calc_existing.getReason());
            }

        } catch (Exception e) {
            System.out.println("Error 3: " + e.getMessage());
        }
        //END EXISTING VALUES
        
        
        //ORDER VALUES
        
        FeltInvCalc inv_calc = new FeltInvCalc();

        try {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

            try {
                    Theoritical_Weigth = (Float.parseFloat(Piecedetail[7]) * Float.parseFloat(Piecedetail[8]) * calculated_GSM) / 1000;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            
            inv_calc = clsOrderValueCalc.calculate(Piecedetail[0], Piecedetail[4], PARTY_CODE.getText(), Float.parseFloat(Piecedetail[7]), Float.parseFloat(Piecedetail[8]), Float.parseFloat(EITLERPGLOBAL.round(Weight,1)+""), Float.parseFloat(EITLERPGLOBAL.round(SQMT,2)+""), df1.format(df.parse(S_O_DATE.getText())));
            //calculate(String Piece_No,String Product_Code,String Party_Code,Float Length,Float Width,Integer GSM,Float Weight,Float SQMT,String Order_Date)

            if (!inv_calc.getReason().equals("")) {
                JOptionPane.showMessageDialog(this, inv_calc.getReason());
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        DataModel.setValueByVariable("OV_RATE", inv_calc.getFicRate() + "", 0);
        
        
        DataModel.setValueByVariable("SURCHARGE_PER", inv_calc.getFicSurcharge_per() + "", 0);
        DataModel.setValueByVariable("SURCHARGE_RATE", inv_calc.getFicSurcharge_rate() + "", 0);
        DataModel.setValueByVariable("GROSS_RATE", inv_calc.getFicGrossRate() + "", 0);
        
        
        //OV_SEAM_CHG
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
        DataModel.setValueByVariable("PR_BILL_SQMTR", EITLERPGLOBAL.round(SQMT,2)+"", 0);
        DataModel.setValueByVariable("PR_BILL_GSM", Piecedetail[9] + "", 0);
        DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", inv_calc.getFicProductCode() + "", 0);
        //
        if(PR_REQUESTED_MONTH.equals("") || PR_REQUESTED_MONTH.equals("0"))
        {
            DataModel.setValueByVariable("REQ_MONTH", "", 0);
        }
        else
        {
            //DataModel.setValueByVariable("REQ_MONTH", PR_REQUESTED_MONTH + "", 0);
            DataModel.setValueByVariable("REQ_MONTH", "", 0);
        }
        
        
        //END ORDER VALUES
        
        
        
        
        
        float diff = 0;

//                                     switch (SQM_IND) {
//                                        case "1":
////                                                    float trim_length = inv_calc_existing.getFicLength()- inv_calc.getFicLength();
////                                                    float trim_width = inv_calc_existing.getFicWidth() - inv_calc.getFicWidth();
////                                                    float trim_amt = SPR_CHRG * (trim_width*trim_length);   
//
//                                                    float exist_piece = inv_calc_existing.getFicLength() * inv_calc_existing.getFicWidth();
//                                                    float order_piece = inv_calc.getFicLength() * inv_calc.getFicWidth();
//                                                    
//                                                    float trim_piece = exist_piece - order_piece;
//                                                    float trim_piece_amt = SQM_RATE * trim_piece;
//                                                    System.out.println("exist_piece : "+exist_piece+" - order_piece : "+order_piece);
//                                                    diff =   trim_piece_amt;
//                                                    
//                                                    //base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() *  inv_calc.getFicRate();
//                                            break;
//                                        case "0":
//                                            
//                                              diff = inv_calc.getFicBasAmount() - inv_calc_existing.getFicBasAmount();
//                                              // base_amt = inv_calc.getFicWeight() * inv_calc.getFicRate();
//                                            break;
//                                    }
        diff = inv_calc_existing.getFicBasAmount() - inv_calc.getFicBasAmount();

        DataModel.setValueByVariable("Exist_Piece_AMT", EITLERPGLOBAL.round(inv_calc_existing.getFicInvAmt(),2) + "", 0);

        DataModel.setValueByVariable("BASE_ORDER_AMT", EITLERPGLOBAL.round(inv_calc.getFicBasAmount(),2) + "", 0);
        DataModel.setValueByVariable("BASE_EXISTING_PIECE_AMT", EITLERPGLOBAL.round(inv_calc_existing.getFicBasAmount(),2) + "", 0);

        float discount = 0;
        if (inv_calc_existing.getFicProductCode().equals("729000") || inv_calc_existing.getFicProductCode().equals("7290000")) {
            System.out.println("get Discount ::  SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + inv_calc_existing.getFicPartyCode() + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + inv_calc_existing.getFicProductCode() + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO = '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM,MASTER_NO DESC");

            try {
               // String Disc_Per = data.getStringValueFromDB("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE ='" + inv_calc_existing.getFicPartyCode() + "' AND SUBSTRING(PRODUCT_CODE,1,6) ='" + inv_calc_existing.getFicProductCode() + "' AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO >= '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                System.out.println("Exist Party Dist : SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE ='" + inv_calc.getFicPartyCode() + "' AND SUBSTRING(PRODUCT_CODE,1,6) ='" + inv_calc.getFicProductCode() + "' AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO = '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM,MASTER_NO DESC");
                String Disc_Per = data.getStringValueFromDB("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE ='" + inv_calc.getFicPartyCode() + "' AND SUBSTRING(PRODUCT_CODE,1,6) ='" + inv_calc.getFicProductCode() + "' AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO = '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY ,MASTER_NO DESC");
                float d_per = Float.parseFloat(Disc_Per);

                discount = (diff * d_per) / 100;

                discount = Math.round(discount);

                DataModel.setValueByVariable("DISCOUNT_PER", EITLERPGLOBAL.round(d_per,2)+"", 0);
                DataModel.setValueByVariable("DISCOUNT_AMT", discount + "", 0);

                diff = diff - discount;
            } catch (Exception e) {
                DataModel.setValueByVariable("DISCOUNT_PER", "0", 0);
                DataModel.setValueByVariable("DISCOUNT_AMT", "0", 0);
                //e.printStackTrace();
            }
        } else {
            DataModel.setValueByVariable("DISCOUNT_PER", "NA", 0);
            DataModel.setValueByVariable("DISCOUNT_AMT", "NA", 0);
        }

        
        try{
                if (inv_calc_existing.getFicProductCode().equals("719011") || inv_calc_existing.getFicProductCode().equals("7190110") ||
                    inv_calc_existing.getFicProductCode().equals("719021") || inv_calc_existing.getFicProductCode().equals("7190210") ||
                    inv_calc_existing.getFicProductCode().equals("719031") || inv_calc_existing.getFicProductCode().equals("7190310") ||
                    inv_calc_existing.getFicProductCode().equals("719041") || inv_calc_existing.getFicProductCode().equals("7190410") ||
                    inv_calc_existing.getFicProductCode().equals("719051") || inv_calc_existing.getFicProductCode().equals("7190510"))
                {

                    double SEAM_CHARGE = 0;
                    double seam_width =  inv_calc_existing.getFicWidth() - inv_calc.getFicWidth();

                    SEAM_CHARGE = seam_width * 4899;

                    //OV_SEAM_CHG
                    DataModel.setValueByVariable("OV_SEAM_CHG", EITLERPGLOBAL.round(SEAM_CHARGE,2) + "", 0);

                    if(!PR_PIECE_STAGE.equals("SEAMING"))
                    {
                         diff = diff + Float.parseFloat(EITLERPGLOBAL.round(SEAM_CHARGE,2)+"");
                    }
                }
                else
                {
                    DataModel.setValueByVariable("OV_SEAM_CHG", "0", 0);
                }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
        diff = Math.round(diff);
        difference = diff;

        DataModel.setValueByVariable("DIFFERENCE_AMT", EITLERPGLOBAL.round(diff,2)+"", 0);
        Table.requestFocus();
        cal_order_value();

        Table.setModel(DataModel);

        DataModel_ExistPiece.setValueAt(PR_MACHINE_NO, 0, 1);
        DataModel_ExistPiece.setValueAt(PR_POSITION_NO, 0, 2);

        DataModel_ExistPiece.setValueAt(frm_Piece_No, 0, 6);

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

        //DataModel_ExistPiece.setValueAt(Piecedetail[0], 0, 4);
        DataModel_ExistPiece.setValueAt(PR_PRODUCT_CODE, 0, 7);

        String ITEM_DESC1 = "";
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            //rsData = stmt.executeQuery("SELECT ITEM_DESC,SYN_PER FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+PR_PRODUCT_CODE+"'");
            PR_PRODUCT_CODE = PR_PRODUCT_CODE.substring(0, 6);
            rsData = stmt.executeQuery("SELECT PRODUCT_DESC,SYN_PER FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='" + PR_PRODUCT_CODE + "'");

            rsData.first();
            ITEM_DESC1 = rsData.getString(1);
            SYN = rsData.getString(2);

        } catch (Exception e) {
            System.out.println("Error 4: " + e.getMessage());
        }

        DataModel_ExistPiece.setValueAt(ITEM_DESC1, 0, 8);
        DataModel_ExistPiece.setValueAt(PR_GROUP, 0, 9);
        DataModel_ExistPiece.setValueAt(PR_LENGTH, 0, 10);
        DataModel_ExistPiece.setValueAt(PR_WIDTH, 0, 11);
        DataModel_ExistPiece.setValueAt(PR_GSM, 0, 12);

        DataModel_ExistPiece.setValueAt(EITLERPGLOBAL.round(PR_THORITICAL_WEIGHT,1), 0, 13);
        DataModel_ExistPiece.setValueAt(EITLERPGLOBAL.round(PR_SQMTR,2), 0, 14);
        DataModel_ExistPiece.setValueAt(PR_STYLE, 0, 15);
        DataModel_ExistPiece.setValueAt(PR_SYN_PER, 0, 16);

                                    // FeltSalesOrderServiceImpl service = new FeltSalesOrderServiceImpl();
        // FeltInvCalc  inv_calc = new FeltInvCalc();
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicRate(), 0, 18);
        
        
        DataModel_ExistPiece.setValueByVariable("EXIST_SURCHARGE_PER", inv_calc_existing.getFicSurcharge_per()+"", 0);
        DataModel_ExistPiece.setValueByVariable("EXIST_SURCHARGE_RATE", inv_calc_existing.getFicSurcharge_rate()+"", 0);
        DataModel_ExistPiece.setValueByVariable("EXIST_GROSS_RATE", inv_calc_existing.getFicGrossRate()+"", 0);

        
        //JOptionPane.showMessageDialog(null, "Base Amouint catch at table model : "+inv_calc.getFicBasAmount());
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicBasAmount(), 0, 19);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicChemTrtChg(), 0, 20);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicSpiralChg(), 0, 21);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicPinChg(), 0, 22);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicSeamChg(), 0, 23);
        DataModel_ExistPiece.setValueAt(0, 0, 24);
        DataModel_ExistPiece.setValueAt(0, 0, 25);
        DataModel_ExistPiece.setValueAt(0, 0, 26);
        DataModel_ExistPiece.setValueAt(0, 0, 27);
        DataModel_ExistPiece.setValueAt(0, 0, 28);
        DataModel_ExistPiece.setValueAt(0, 0, 29);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getVat(), 0, 30);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getCst(), 0, 31);
        DataModel_ExistPiece.setValueAt(inv_calc_existing.getFicInvAmt(), 0, 32);

        DataModel_ExistPiece.setValueAt(PR_RCV_DATE, 0, 33);
        DataModel_ExistPiece.setValueAt(PR_PARTY_CODE, 0, 34);

        try {

            Conn = data.getConn();
            stmt = Conn.createStatement();
            // System.out.println("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='"+PARTY_CODE.getText()+"' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
            rsData = stmt.executeQuery("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='" + PR_PARTY_CODE + "' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
            rsData.first();
            DataModel_ExistPiece.setValueAt(rsData.getString(5), 0, 35);
            DataModel_ExistPiece.setValueAt(rsData.getString(1), 0, 36);

        } catch (Exception e) {
            System.out.println("Error onm fetch data for D_SAL_PARTY_MASTER " + e.getMessage());
        }

        /*
         DataModel_ExistPiece.setValueAt(PR_PARTY_CODE, 0, 32);
         DataModel_ExistPiece.setValueAt(PR_PARTY_CODE, 0, 32);
         DataModel_ExistPiece.setValueAt(PR_PARTY_CODE, 0, 32);
         DataModel_ExistPiece.setValueAt(PR_PARTY_CODE, 0, 32);
                                     
         */
        DataModel_ExistPiece.setValueAt(PR_ACTUAL_WEIGHT, 0, 37); //ACTUAL_WEIGTH
        DataModel_ExistPiece.setValueAt(PR_PIECE_STAGE, 0, 38); //PIECE_STATUS
        DataModel_ExistPiece.setValueAt(PR_ACTUAL_LENGTH, 0, 39); //ACTUAL_LENGTH
        DataModel_ExistPiece.setValueAt(PR_ACTUAL_WIDTH, 0, 40); //ACTUAL_WIDTH
        
        DataModel_ExistPiece.setValueAt(PR_WEAVING_WEIGHT, 0, 41); //ACTUAL_WIDTH
        DataModel_ExistPiece.setValueAt(PR_NEEDLING_WEIGHT, 0, 42); //ACTUAL_WIDTH
        
        DataModel_ExistPiece.setValueByVariable("BILL_LENGTH", inv_calc_existing.getFicLength()+"", 0);
        DataModel_ExistPiece.setValueByVariable("BILL_WIDTH", inv_calc_existing.getFicWidth()+"", 0);
        DataModel_ExistPiece.setValueByVariable("BILL_WEIGHT", inv_calc_existing.getFicWeight()+"", 0);
        DataModel_ExistPiece.setValueByVariable("BILL_SQMTR", inv_calc_existing.getFicSqMtr()+"", 0);
        
        
        AcNoAction.setSelected(false);
        //filterHierarchyCombo();
        TableKeyReleased(null);
        setNewPartyPieces(UPN_NewParty);
        setExistPartyPieces(UPN_ExistParty);
    }

    public void DefaultSettings() {

        //String data = toString();
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
        REFERENCE.setSelectedItem("");
        REF_DATE.setText("");
        P_O_NO.setText("");
        P_O_DATE.setText("");
        REMARK.setText("");
        ORDER_VALUE.setText("0");
        lblDBNOTE.setText("");
        lblDbAmt.setText("");
        lblPriorApprovalNo.setText("");
        lblPriorApprovalDate.setText("");
        btnParty.setSelected(true);
        txtContactPerson.setText("");
        txtPhoneNo.setText("");
        txtEmailId.setText("");
        txtEmailId2.setText("");
        txtEmailId3.setText("");
        txtReschCanRemark.setText("");
        txtNewPartyRemark.setText("");
        chbEmailUpdate.setSelected(false);
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
        DataModel_ExistPiece.addRow(rowData);

        Ac1.setSelected(false);
        Ac2.setSelected(false);
        Ac3.setSelected(false);
        Ac4.setSelected(false);
        Ac5.setSelected(false);
        Ac6.setSelected(false);
        Ac7.setSelected(false);
        Ac8.setSelected(false);
        Ac9.setSelected(false);
        Ac10.setSelected(false);
        Ac11.setSelected(false);
        Ac12.setSelected(false);
        Ac13.setSelected(false);
        Ac14.setSelected(false);
        Ac15.setSelected(false);
        Ac16.setSelected(false);
        Ac17.setSelected(false);
        
        AcNoAction.setSelected(false);

        txtAction1.setText("");
        txtAction2.setText("");
        txtAction3.setText("");
        txtAction4.setText("");
        txtAction5.setText("");
        txtAction6.setText("");
        txtAction7.setText("");
        txtAction8.setText("");
        txtAction9.setText("");
        txtAction10.setText("");
        txtAction11.setText("");
        txtAction12.setText("");
        txtAction13.setText("");
        txtAction14.setText("");
        txtAction15.setText("");
        txtAction16.setText("");
        txtAction17.setText("");

        txtAction1.setEditable(false);
        txtAction2.setEditable(false);
        txtAction3.setEditable(false);
        txtAction4.setEditable(false);
        txtAction5.setEditable(false);
        txtAction6.setEditable(false);
        txtAction7.setEditable(false);
        txtAction8.setEditable(false);
        txtAction9.setEditable(false);
        txtAction10.setEditable(false);
        txtAction11.setEditable(false);
        txtAction12.setEditable(false);
        txtAction13.setEditable(false);
        txtAction14.setEditable(false);
        txtAction15.setEditable(false);
        txtAction16.setEditable(false);
        txtAction17.setEditable(false);

        DebitMemoNo2.setText("");
        DebitMemoAmt2.setText("");
       
        DebitMemoNo3.setText("");
        DebitMemoAmt3.setText("");
       
        DebitMemoNo4.setText("");
        DebitMemoAmt4.setText("");
       
        DebitMemoNo5.setText("");
        DebitMemoAmt5.setText("");
       
        DebitMemoNo6.setText("");
        DebitMemoAmt6.setText("");
       
        DebitMemoNo7.setText("");
        DebitMemoAmt7.setText("");
       
        DebitMemoNo8.setText("");
        DebitMemoAmt8.setText("");
       
        DebitMemoNo9.setText("");
        DebitMemoAmt9.setText("");
       
        DebitMemoNo10.setText("");
        DebitMemoAmt10.setText("");
       
    }

    private void DisplayData() {

        //=========== Color Indication ===============//
        try {

            jButton2.setEnabled(false);
            
            if ("1".equals(feltOrder.getAttribute("APPROVED").getString())) {
                lblTitle.setBackground(Color.BLUE);
                lblTitle.setForeground(Color.WHITE);
                jButton2.setEnabled(true);
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
            lblTitle.setText("Felt Sales Order Diversion - " + feltOrder.getAttribute("SD_ORDER_NO").getString());
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
            REFERENCE.setSelectedItem(feltOrder.getAttribute("D_REFERENCE").getString());
            REF_DATE.setText(EITLERPGLOBAL.formatDate(feltOrder.getAttribute("D_REFERENCE_DATE").getString()));
            P_O_NO.setText(feltOrder.getAttribute("D_P_O_NO").getString());
            P_O_DATE.setText(EITLERPGLOBAL.formatDate(feltOrder.getAttribute("D_P_O_DATE").getString()));
            REMARK.setText(feltOrder.getAttribute("D_REMARK").getString());
            
            DebitMemoNo2.setText(feltOrder.getAttribute("DEBITMEMO_NO2").getString());
            DebitMemoAmt2.setText(feltOrder.getAttribute("DEBITMEMO_AMT2").getString());
            
            DebitMemoNo3.setText(feltOrder.getAttribute("DEBITMEMO_NO3").getString());
            DebitMemoAmt3.setText(feltOrder.getAttribute("DEBITMEMO_AMT3").getString());
            
            DebitMemoNo4.setText(feltOrder.getAttribute("DEBITMEMO_NO4").getString());
            DebitMemoAmt4.setText(feltOrder.getAttribute("DEBITMEMO_AMT4").getString());
            
            DebitMemoNo5.setText(feltOrder.getAttribute("DEBITMEMO_NO5").getString());
            DebitMemoAmt5.setText(feltOrder.getAttribute("DEBITMEMO_AMT5").getString());
            
            DebitMemoNo6.setText(feltOrder.getAttribute("DEBITMEMO_NO6").getString());
            DebitMemoAmt6.setText(feltOrder.getAttribute("DEBITMEMO_AMT6").getString());
            
            DebitMemoNo7.setText(feltOrder.getAttribute("DEBITMEMO_NO7").getString());
            DebitMemoAmt7.setText(feltOrder.getAttribute("DEBITMEMO_AMT7").getString());
            
            DebitMemoNo8.setText(feltOrder.getAttribute("DEBITMEMO_NO8").getString());
            DebitMemoAmt8.setText(feltOrder.getAttribute("DEBITMEMO_AMT8").getString());
            
            DebitMemoNo9.setText(feltOrder.getAttribute("DEBITMEMO_NO9").getString());
            DebitMemoAmt9.setText(feltOrder.getAttribute("DEBITMEMO_AMT9").getString());
            
            DebitMemoNo10.setText(feltOrder.getAttribute("DEBITMEMO_NO10").getString());
            DebitMemoAmt10.setText(feltOrder.getAttribute("DEBITMEMO_AMT10").getString());
            
            if (feltOrder.getAttribute("COST_BEARER").getString().equals("PARTY")) {
                btnParty.setSelected(true);
                txtPartyBearer.setText(feltOrder.getAttribute("BEARER_PARTY_CODE").getString());
                txtPartyBearerName.setText(feltOrder.getAttribute("BEARER_PARTY_NAME").getString());
                //lblPartyBearer.setEnabled(true);
                //lblPartyBearerName.setEnabled(true);
                //txtPartyBearer.setEnabled(true);
                //txtPartyBearerName.setEnabled(true);
            }
            else if (feltOrder.getAttribute("COST_BEARER").getString().equals("SDML")) {
                btnCompany.setSelected(true);
                lblPartyBearer.setEnabled(false);
               // lblPartyBearerName.setEnabled(false);
                txtPartyBearer.setEnabled(false);
                txtPartyBearerName.setEnabled(false);
                txtPartyBearer.setText("");
                txtPartyBearerName.setText("");
            }
            else if(feltOrder.getAttribute("COST_BEARER").getString().equals("PARTIAL"))
            {
                btnPartialPayment.setSelected(true);
               
                txtPartyBearer.setText(feltOrder.getAttribute("BEARER_PARTY_CODE").getString());
                txtPartyBearerName.setText(feltOrder.getAttribute("BEARER_PARTY_NAME").getString());
            }
            
            txtSDMLPer.setText(feltOrder.getAttribute("SDML_PER").getString());
            txtBearCompanyAmt.setText(feltOrder.getAttribute("SDML_AMT").getString());

            txtPartyPer.setText(feltOrder.getAttribute("PARTY_PER").getString());
            txtBearPartyAmt.setText(feltOrder.getAttribute("PARTY_AMT").getString());
            
            chbEmailUpdate.setSelected(feltOrder.getAttribute("EMAIL_UPDATE_TO_PM").getBool());
            
            txtContactPerson.setText(feltOrder.getAttribute("CONTACT_PERSON").getString());
            txtEmailId.setText(feltOrder.getAttribute("EMAIL_ID").getString());
            txtEmailId2.setText(feltOrder.getAttribute("EMAIL_ID2").getString());
            txtEmailId3.setText(feltOrder.getAttribute("EMAIL_ID3").getString());
            txtPhoneNo.setText(feltOrder.getAttribute("PHONE_NUMBER").getString());
            
            
            //DataModel.setValueByVariable("SrNo", "1", 0);//0 - Read Only
            DataModel.setValueAt("1", 0, 0);
            DataModel.setValueByVariable("MACHINE_NO", feltOrder.getAttribute("D_MACHINE_NO").getString(), 0);
            DataModel.setValueByVariable("POSITION", feltOrder.getAttribute("D_POSITION_NO").getString(), 0);
            DataModel.setValueByVariable("POSITION_DESC", feltOrder.getAttribute("D_POSITION_DESC").getString(), 0);
            DataModel.setValueByVariable("POS_DESIGN_NO", feltOrder.getAttribute("D_POS_DESIGN_NO").getString(), 0);
            DataModel.setValueByVariable("UPN", feltOrder.getAttribute("D_UPN").getString(), 0);
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
            
            DataModel.setValueByVariable("SURCHARGE_PER", feltOrder.getAttribute("SURCHARGE_PER").getString(), 0);
            DataModel.setValueByVariable("SURCHARGE_RATE", feltOrder.getAttribute("SURCHARGE_RATE").getString(), 0);
            DataModel.setValueByVariable("GROSS_RATE", feltOrder.getAttribute("GROSS_RATE").getString(), 0);
            
            DataModel.setValueByVariable("OV_BAS_AMOUNT", feltOrder.getAttribute("D_OV_BAS_AMOUNT").getString(), 0);
            DataModel.setValueByVariable("0", feltOrder.getAttribute("D_OV_CHEM_TRT_CHG").getString(), 0);
            DataModel.setValueByVariable("0", feltOrder.getAttribute("D_OV_SPIRAL_CHG").getString(), 0);
            DataModel.setValueByVariable("0", feltOrder.getAttribute("D_OV_PIN_CHG").getString(), 0);
            //DataModel.setValueByVariable("0", feltOrder.getAttribute("D_OV_SEAM_CHG").getString(), 0);
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

            lblDBNOTE.setText("" + feltOrder.getAttribute("DEBIT_NOTE_NO").getString());
            lblDbAmt.setText("" + feltOrder.getAttribute("DEBIT_AMT").getString());
            
            lblPriorApprovalNo.setText(feltOrder.getAttribute("PRIOR_APPROVAL_NO").getString());
            lblPriorApprovalDate.setText(feltOrder.getAttribute("PRIOR_APPROVAL_DATE").getString());

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

            DataModel.setValueByVariable("OA_NO", feltOrder.getAttribute("OA_NO").getString(), 0);
            
            DataModel.setValueByVariable("OA_DATE", EITLERPGLOBAL.formatDate(feltOrder.getAttribute("OA_DATE").getString()), 0);
            DataModel.setValueByVariable("OC_NO", feltOrder.getAttribute("OC_NO").getString(), 0);
            DataModel.setValueByVariable("OC_MONTHYEAR", feltOrder.getAttribute("OC_MONTHYEAR").getString(), 0);
            DataModel.setValueByVariable("OC_DATE", EITLERPGLOBAL.formatDate(feltOrder.getAttribute("OC_DATE").getString()), 0);
            
            DataModel.setValueByVariable("REQ_MONTH", feltOrder.getAttribute("REQ_MONTH").getString(), 0);
            
            txtReschCanRemark.setText(feltOrder.getAttribute("RESCH_CAN_REMARK").getString());
            txtNewPartyRemark.setText(feltOrder.getAttribute("RESCH_CAN_REMARK_NEW_PARTY").getString());
            
            if (feltOrder.getAttribute("ACTION1").getString().equals("")) {
                Ac1.setSelected(false);
                txtAction1.setEditable(false);
                txtAction1.setText("");
            } else {
                Ac1.setSelected(true);
                txtAction1.setEditable(true);
                txtAction1.setText(feltOrder.getAttribute("ACTION1").getString());
            }

            if (feltOrder.getAttribute("ACTION2").getString().equals("")) {
                Ac2.setSelected(false);
                txtAction2.setEditable(false);
                txtAction2.setText("");
            } else {
                Ac2.setSelected(true);
                txtAction2.setEditable(true);
                txtAction2.setText(feltOrder.getAttribute("ACTION2").getString());
            }

            if (feltOrder.getAttribute("ACTION3").getString().equals("")) {
                Ac3.setSelected(false);
                txtAction3.setEditable(false);
                txtAction3.setText("");
            } else {
                Ac3.setSelected(true);
                txtAction3.setEditable(true);
                txtAction3.setText(feltOrder.getAttribute("ACTION3").getString());
            }

            if (feltOrder.getAttribute("ACTION4").getString().equals("")) {
                Ac4.setSelected(false);
                txtAction4.setEditable(false);
                txtAction4.setText("");
            } else {
                Ac4.setSelected(true);
                txtAction4.setEditable(true);
                txtAction4.setText(feltOrder.getAttribute("ACTION4").getString());
            }

            if (feltOrder.getAttribute("ACTION5").getString().equals("")) {
                Ac5.setSelected(false);
                txtAction5.setEditable(false);
                txtAction5.setText("");
            } else {
                Ac5.setSelected(true);
                txtAction5.setEditable(true);
                txtAction5.setText(feltOrder.getAttribute("ACTION5").getString());
            }

            if (feltOrder.getAttribute("ACTION6").getString().equals("")) {
                Ac6.setSelected(false);
                txtAction6.setEditable(false);
                txtAction6.setText("");
            } else {
                Ac6.setSelected(true);
                txtAction6.setEditable(true);
                txtAction6.setText(feltOrder.getAttribute("ACTION6").getString());
            }

            if (feltOrder.getAttribute("ACTION7").getString().equals("")) {
                Ac7.setSelected(false);
                txtAction7.setEditable(false);
                txtAction7.setText("");
            } else {
                Ac7.setSelected(true);
                txtAction7.setEditable(true);
                txtAction7.setText(feltOrder.getAttribute("ACTION7").getString());
            }

            if (feltOrder.getAttribute("ACTION8").getString().equals("")) {
                Ac8.setSelected(false);
                txtAction8.setEditable(false);
                txtAction8.setText("");
            } else {
                Ac8.setSelected(true);
                txtAction8.setEditable(true);
                txtAction8.setText(feltOrder.getAttribute("ACTION8").getString());
            }

            if (feltOrder.getAttribute("ACTION9").getString().equals("")) {
                Ac9.setSelected(false);
                txtAction9.setEditable(false);
                txtAction9.setText("");
            } else {
                Ac9.setSelected(true);
                txtAction9.setEditable(true);
                txtAction9.setText(feltOrder.getAttribute("ACTION9").getString());
            }

            if (feltOrder.getAttribute("ACTION10").getString().equals("")) {
                Ac10.setSelected(false);
                txtAction10.setEditable(false);
                txtAction10.setText("");
            } else {
                Ac10.setSelected(true);
                txtAction10.setEditable(true);
                txtAction10.setText(feltOrder.getAttribute("ACTION10").getString());
            }

            if (feltOrder.getAttribute("ACTION11").getString().equals("")) {
                Ac11.setSelected(false);
                txtAction11.setEditable(false);
                txtAction11.setText("");
            } else {
                Ac11.setSelected(true);
                txtAction11.setEditable(true);
                txtAction11.setText(feltOrder.getAttribute("ACTION11").getString());
            }

            if (feltOrder.getAttribute("ACTION12").getString().equals("")) {
                Ac12.setSelected(false);
                txtAction12.setEditable(false);
                txtAction12.setText("");
            } else {
                Ac12.setSelected(true);
                txtAction12.setEditable(true);
                txtAction12.setText(feltOrder.getAttribute("ACTION12").getString());
            }

            if (feltOrder.getAttribute("ACTION13").getString().equals("")) {
                Ac13.setSelected(false);
                txtAction13.setEditable(false);
                txtAction13.setText("");
            } else {
                Ac13.setSelected(true);
                txtAction13.setEditable(true);
                txtAction13.setText(feltOrder.getAttribute("ACTION13").getString());
            }

            if (feltOrder.getAttribute("ACTION14").getString().equals("")) {
                Ac14.setSelected(false);
                txtAction14.setEditable(false);
                txtAction14.setText("");
            } else {
                Ac14.setSelected(true);
                txtAction14.setEditable(true);
                txtAction14.setText(feltOrder.getAttribute("ACTION14").getString());
            }

            if (feltOrder.getAttribute("ACTION15").getString().equals("")) {
                Ac15.setSelected(false);
                txtAction15.setEditable(false);
                txtAction15.setText("");
            } else {
                Ac15.setSelected(true);
                txtAction15.setEditable(true);
                txtAction15.setText(feltOrder.getAttribute("ACTION15").getString());
            }

            if (feltOrder.getAttribute("ACTION16").getString().equals("")) {
                Ac16.setSelected(false);
                txtAction16.setEditable(false);
                txtAction16.setText("");
            } else {
                Ac16.setSelected(true);
                txtAction16.setEditable(true);
                txtAction16.setText(feltOrder.getAttribute("ACTION16").getString());
            }

            if (feltOrder.getAttribute("ACTION17").getString().equals("")) {
                Ac17.setSelected(false);
                txtAction17.setEditable(false);
                txtAction17.setText("");
            } else {
                Ac17.setSelected(true);
                txtAction17.setEditable(true);
                txtAction17.setText(feltOrder.getAttribute("ACTION17").getString());
            }
            
            
            if(!Ac1.isSelected() && !Ac2.isSelected() && !Ac3.isSelected() && !Ac4.isSelected() && !Ac5.isSelected() && !Ac6.isSelected() && !Ac7.isSelected() && !Ac8.isSelected() && !Ac9.isSelected() && !Ac10.isSelected() && !Ac11.isSelected() && !Ac12.isSelected() && !Ac13.isSelected() && !Ac14.isSelected() && !Ac15.isSelected() && !Ac16.isSelected() && !Ac17.isSelected())
            {
                AcNoAction.setSelected(true);
            }
            
            DataModel_ExistPiece.setValueAt("1", 0, 0);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString(), 0, 6);
            
            txtPieceNo.setText(feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString());
            
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_PRODUCT_NO").getString(), 0, 7);
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
            DataModel_ExistPiece.setValueAt(ITEM_DESC, 0, 8);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_MACHINE_NO").getString(), 0, 1);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_POSITION_NO").getString(), 0, 2);
            DataModel_ExistPiece.setValueAt(POSITION_DESC, 0, 3);
            
            String Position_Des_No = "",UPN="";
            try{
                Position_Des_No = data.getStringValueFromDB("SELECT POSITION_DESIGN_NO FROM PRODUCTION.FELT_MACHINE_POSITION_MST where POSITION_NO="+feltOrder.getAttribute("ORIGINAL_POSITION_NO").getString());
                UPN = data.getStringValueFromDB("SELECT PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"'");            
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
            DataModel_ExistPiece.setValueAt(Position_Des_No, 0, 4);
            DataModel_ExistPiece.setValueAt(UPN, 0, 5);
            
            
            
            DataModel_ExistPiece.setValueAt(SYN, 0, 16);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_STYLE_NO").getString(), 0, 15);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_GROUP").getString(), 0, 9);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_LENGTH").getString(), 0, 10);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_WIDTH").getString(), 0, 11);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_GSM").getString(), 0, 12);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_THORITICAL_WEIGHT").getString(), 0, 13);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_SQ_MTR").getString(), 0, 14);
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_OV_RATE").getString(), 0, 18);

            
            DataModel_ExistPiece.setValueByVariable("EXIST_SURCHARGE_PER", feltOrder.getAttribute("EXIST_SURCHARGE_PER").getString(), 0);
            DataModel_ExistPiece.setValueByVariable("EXIST_SURCHARGE_RATE", feltOrder.getAttribute("EXIST_SURCHARGE_RATE").getString(), 0);
            DataModel_ExistPiece.setValueByVariable("EXIST_GROSS_RATE", feltOrder.getAttribute("EXIST_GROSS_RATE").getString(), 0);
            
            
            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ORIGINAL_PARTY_CODE").getString(), 0, 34);

            String ACTUAL_WEIGTH = data.getStringValueFromDB("SELECT PR_ACTUAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String PIECE_STATUS = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            //String RCV_DATE = data.getStringValueFromDB("SELECT PR_RCV_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String RCV_DATE = data.getStringValueFromDB("SELECT PR_FNSG_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String ACTUAL_LENGTH = data.getStringValueFromDB("SELECT PR_ACTUAL_LENGTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String ACTUAL_WIDTH = data.getStringValueFromDB("SELECT PR_ACTUAL_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            
            try{
                        TableColumn dateColumn_OC_MONTH = Table.getColumnModel().getColumn(DataModel.getColFromVariable("OC_MONTHYEAR"));
                        JComboBox monthbox = new JComboBox();

                        String month_name = "";
                        Date date = new Date();
                        int month;
                        int year = date.getYear() + 1900;

                        if ("IN STOCK".equals(PIECE_STATUS) || "STOCK".equals(PIECE_STATUS)) 
                        {
                            month = date.getMonth();
                        }
                        else
                        {
                            month = date.getMonth()+1;
                        }

                        monthbox.addItem("");
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
                            monthbox.addItem(month_name + " - " + year);
                        }

                        dateColumn_OC_MONTH.setCellEditor(new DefaultCellEditor(monthbox));


            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
            
            DataModel_ExistPiece.setValueAt(ACTUAL_WEIGTH, 0, 37);
            DataModel_ExistPiece.setValueAt(PIECE_STATUS, 0, 38);
            DataModel_ExistPiece.setValueAt(ACTUAL_LENGTH, 0, 39);
            DataModel_ExistPiece.setValueAt(ACTUAL_WIDTH, 0, 40);
            DataModel_ExistPiece.setValueAt(RCV_DATE, 0, 33);

            
            String weaving_weight = data.getStringValueFromDB("SELECT PR_WEAVING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            String needling_weight = data.getStringValueFromDB("SELECT PR_NEEDLING_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
            
            
            DataModel_ExistPiece.setValueAt(weaving_weight, 0, 41);
            DataModel_ExistPiece.setValueAt(needling_weight, 0, 42);
           
            try{
                String PR_BILL_LENGTH = data.getStringValueFromDB("SELECT PR_BILL_LENGTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
                String PR_BILL_WIDTH = data.getStringValueFromDB("SELECT PR_BILL_WIDTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
                String PR_BILL_SQMTR = data.getStringValueFromDB("SELECT PR_BILL_SQMTR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
                String PR_BILL_WEIGHT = data.getStringValueFromDB("SELECT PR_BILL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");

                DataModel_ExistPiece.setValueByVariable("BILL_LENGTH", PR_BILL_LENGTH, 0);
                DataModel_ExistPiece.setValueByVariable("BILL_WIDTH", PR_BILL_WIDTH, 0);
                DataModel_ExistPiece.setValueByVariable("BILL_SQMTR", PR_BILL_SQMTR, 0);
                DataModel_ExistPiece.setValueByVariable("BILL_WEIGHT", PR_BILL_WEIGHT, 0);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
//            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("ACTUAL_WEIGTH").getString(), 0, 35);
//            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("PR_BILL_LENGTH").getString(), 0, 36);
//            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("PR_BILL_WIDTH").getString(), 0, 37);
//            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("PR_BILL_WEIGHT").getString(), 0, 38);
//            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("PR_BILL_SQMTR").getString(), 0, 39);
//            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("PR_BILL_GSM").getString(), 0, 40);
//            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("PR_BILL_PRODUCT_CODE").getString(), 0, 41);
//            DataModel_ExistPiece.setValueAt(feltOrder.getAttribute("PIECE_STATUS").getString(), 0, 42);
            lblRevNo.setText(Integer.toString((int) feltOrder.getAttribute("REVISION_NO").getVal()));
            try {
                Conn = data.getConn();
                stmt = Conn.createStatement();
                        // System.out.println("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='"+PARTY_CODE.getText()+"' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
                //System.out.println("NAME & CITY : SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='"+feltOrder.getAttribute("ORIGINAL_PARTY_CODE").getString()+"' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
                rsData = stmt.executeQuery("SELECT A.CITY_ID,A.DISTRICT,B.COUNTRY_NAME,A.ZONE,A.PARTY_NAME,C.INCHARGE_NAME  FROM DINESHMILLS.D_SAL_PARTY_MASTER A,DINESHMILLS.D_SAL_COUNTRY_MASTER B,PRODUCTION.FELT_INCHARGE C WHERE PARTY_CODE='" + feltOrder.getAttribute("ORIGINAL_PARTY_CODE").getString() + "' AND A.COUNTRY_ID=B.COUNTRY_ID AND A.INCHARGE_CD=C.INCHARGE_CD");
                rsData.first();
                DataModel_ExistPiece.setValueAt(rsData.getString(5), 0, 35);
                DataModel_ExistPiece.setValueAt(rsData.getString(1), 0, 36);

            } catch (Exception e) {
                //e.printStackTrace();
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
            clsFeltOrderDiversion ObjHistory = (clsFeltOrderDiversion) History.get(Integer.toString(i));
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
        //setSTATUS();
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
        if(btnCompany.isSelected() || btnPartialPayment.isSelected())
        {
            btnOpenPriorApproval.setEnabled(true);
        }
        else
        {
            btnOpenPriorApproval.setEnabled(false);
        }
        DisplayRescCancellationPiece();
    }
    private void DisplayRescCancellationPiece()
    {
            try {
           
            FormatGridNewParty();
            

            String str_query = "SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where SD_ORDER_NO='"+S_O_NO.getText()+"' AND PARTY='NEW_PARTY'";
            
            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("New Party Pending Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_NewParty.addRow(rowData);

                DataModel_NewParty.setValueByVariable("SrNo", srNo + "", NewRow);
                
                if(resultSet.getInt("CANCEL_PIECE")==1)
                {
                    DataModel_NewParty.setValueByVariable("CANCEL_PIECE", Boolean.TRUE, NewRow);
                }
                else
                {
                    DataModel_NewParty.setValueByVariable("CANCEL_PIECE", Boolean.FALSE, NewRow);
                }
                
                DataModel_NewParty.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_NewParty.setValueByVariable("RESCHEDULE_MONTH", resultSet.getString("RESCHEDULE_MONTH"), NewRow);
                
                String query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR " +
                        "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS " +
                        "ON PS.POSITION_NO = PR.PR_POSITION_NO " +
                        "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM " +
                        "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                      + "WHERE PR.PR_PIECE_NO='"+resultSet.getString("PR_PIECE_NO")+"'";
                try{
                ResultSet rsPiece = data.getResult(query);
                rsPiece.first();
                
                        DataModel_NewParty.setValueByVariable("PR_MACHINE_NO", rsPiece.getString("PR_MACHINE_NO"), NewRow);
                        DataModel_NewParty.setValueByVariable("PR_POSITION_NO", rsPiece.getString("PR_POSITION_NO"), NewRow);
                        DataModel_NewParty.setValueByVariable("POSITION_DESC", rsPiece.getString("POSITION_DESC"), NewRow);
                        DataModel_NewParty.setValueByVariable("UPN_PIECES", rsPiece.getString("PR_UPN"), NewRow);
                        DataModel_NewParty.setValueByVariable("PR_REQUESTED_MONTH", rsPiece.getString("PR_REQUESTED_MONTH"), NewRow);
                        DataModel_NewParty.setValueByVariable("PR_PARTY_CODE", rsPiece.getString("PR_PARTY_CODE"), NewRow);
                        DataModel_NewParty.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, rsPiece.getString("PR_PARTY_CODE")), NewRow);
                        DataModel_NewParty.setValueByVariable("PR_GROUP", rsPiece.getString("PR_GROUP"), NewRow);
                        DataModel_NewParty.setValueByVariable("PR_PIECE_STAGE", rsPiece.getString("PR_PIECE_STAGE"), NewRow);
                        DataModel_NewParty.setValueByVariable("OC_MONTH", rsPiece.getString("PR_OC_MONTHYEAR"), NewRow);
                        DataModel_NewParty.setValueByVariable("CURRENT_SCHEDULE_MONTH", rsPiece.getString("PR_CURRENT_SCH_MONTH"), NewRow);
                        DataModel_NewParty.setValueByVariable("LENGTH", rsPiece.getString("PR_BILL_LENGTH"), NewRow);
                        DataModel_NewParty.setValueByVariable("WIDTH", rsPiece.getString("PR_BILL_WIDTH"), NewRow);
                        DataModel_NewParty.setValueByVariable("GSM", rsPiece.getString("PR_BILL_GSM"), NewRow);
                        DataModel_NewParty.setValueByVariable("STYLE", rsPiece.getString("PR_BILL_STYLE"), NewRow);
                        DataModel_NewParty.setValueByVariable("WEIGHT", rsPiece.getString("PR_BILL_WEIGHT"), NewRow);
                        DataModel_NewParty.setValueByVariable("SQMTR", rsPiece.getString("PR_BILL_SQMTR"), NewRow);
                        try{
                            String incharge = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD="+rsPiece.getString("PR_INCHARGE")+"");
                            DataModel_NewParty.setValueByVariable("ZONE", incharge, NewRow);
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                }catch(Exception we)
                {
                    we.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            
            
            //existing party
            try {
           
            FormatGridExistParty();
            

            String str_query = "SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where SD_ORDER_NO='"+S_O_NO.getText()+"' AND PARTY='EXIST_PARTY'";
            
            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("EXIST Party Pending Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_ExistParty.addRow(rowData);

                DataModel_ExistParty.setValueByVariable("SrNo", srNo + "", NewRow);
                
                if(resultSet.getInt("CANCEL_PIECE")==1)
                {
                    DataModel_ExistParty.setValueByVariable("CANCEL_PIECE", Boolean.TRUE, NewRow);
                }
                else
                {
                    DataModel_ExistParty.setValueByVariable("CANCEL_PIECE", Boolean.FALSE, NewRow);
                }
                
                DataModel_ExistParty.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_ExistParty.setValueByVariable("RESCHEDULE_MONTH", resultSet.getString("RESCHEDULE_MONTH"), NewRow);
                
                String query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR " +
                        "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS " +
                        "ON PS.POSITION_NO = PR.PR_POSITION_NO " +
                        "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM " +
                        "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                      + "WHERE PR.PR_PIECE_NO='"+resultSet.getString("PR_PIECE_NO")+"'";
                try{
                ResultSet rsPiece = data.getResult(query);
                rsPiece.first();
                
                        DataModel_ExistParty.setValueByVariable("PR_MACHINE_NO", rsPiece.getString("PR_MACHINE_NO"), NewRow);
                        DataModel_ExistParty.setValueByVariable("PR_POSITION_NO", rsPiece.getString("PR_POSITION_NO"), NewRow);
                        DataModel_ExistParty.setValueByVariable("POSITION_DESC", rsPiece.getString("POSITION_DESC"), NewRow);
                        DataModel_ExistParty.setValueByVariable("UPN_PIECES", rsPiece.getString("PR_UPN"), NewRow);
                        DataModel_ExistParty.setValueByVariable("PR_REQUESTED_MONTH", rsPiece.getString("PR_REQUESTED_MONTH"), NewRow);
                        DataModel_ExistParty.setValueByVariable("PR_PARTY_CODE", rsPiece.getString("PR_PARTY_CODE"), NewRow);
                        DataModel_ExistParty.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, rsPiece.getString("PR_PARTY_CODE")), NewRow);
                        DataModel_ExistParty.setValueByVariable("PR_GROUP", rsPiece.getString("PR_GROUP"), NewRow);
                        DataModel_ExistParty.setValueByVariable("PR_PIECE_STAGE", rsPiece.getString("PR_PIECE_STAGE"), NewRow);
                        DataModel_ExistParty.setValueByVariable("OC_MONTH", rsPiece.getString("PR_OC_MONTHYEAR"), NewRow);
                        DataModel_ExistParty.setValueByVariable("CURRENT_SCHEDULE_MONTH", rsPiece.getString("PR_CURRENT_SCH_MONTH"), NewRow);
                        DataModel_ExistParty.setValueByVariable("LENGTH", rsPiece.getString("PR_BILL_LENGTH"), NewRow);
                        DataModel_ExistParty.setValueByVariable("WIDTH", rsPiece.getString("PR_BILL_WIDTH"), NewRow);
                        DataModel_ExistParty.setValueByVariable("GSM", rsPiece.getString("PR_BILL_GSM"), NewRow);
                        DataModel_ExistParty.setValueByVariable("STYLE", rsPiece.getString("PR_BILL_STYLE"), NewRow);
                        DataModel_ExistParty.setValueByVariable("WEIGHT", rsPiece.getString("PR_BILL_WEIGHT"), NewRow);
                        DataModel_ExistParty.setValueByVariable("SQMTR", rsPiece.getString("PR_BILL_SQMTR"), NewRow);
                        try{
                            String incharge = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD="+rsPiece.getString("PR_INCHARGE")+"");
                            DataModel_ExistParty.setValueByVariable("ZONE", incharge, NewRow);
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                }catch(Exception we)
                {
                    we.printStackTrace();
                }
            }
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6078, 60781)) { //7008,70081
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6078, 60782)) { //7008,70082
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6078, 60783)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6078, 60784)) {
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
        //JOptionPane.showMessageDialog(null, "Hierarchy Id = "+SelHierarchyID);
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
            //txtFromRemarks.setEnabled(false);
            OpgApprove.setEnabled(false);
            OpgFinal.setEnabled(false);
            OpgReject.setEnabled(false);
            cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }

//        if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gUserID)) {
//            OpgReject.setEnabled(false);
//        }//GAURANG
        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
            //JOptionPane.showMessageDialog(null, "Final Approver");
            OpgApprove.setEnabled(false);
        }
    }

    private void FormatGridNewParty()
    {
           
        try {
            
            DataModel_NewParty = new EITLTableModel();
            tblNewParty.removeAll();

            tblNewParty.setModel(DataModel_NewParty);
            tblNewParty.setAutoResizeMode(0);

            DataModel_NewParty.addColumn("SrNo"); //0 - Read Only
            DataModel_NewParty.addColumn("CANCEL PIECE"); //0 - Read Only
            DataModel_NewParty.addColumn("PIECE NO"); //1
            DataModel_NewParty.addColumn("PARTY CODE"); //5
            DataModel_NewParty.addColumn("PARTY NAME"); //6
            DataModel_NewParty.addColumn("MAC NO"); //2
            DataModel_NewParty.addColumn("POS NO"); //3
            DataModel_NewParty.addColumn("POS DESC"); //4
            DataModel_NewParty.addColumn("UPN PIECES"); //11
            DataModel_NewParty.addColumn("REQ MTH"); //11
            DataModel_NewParty.addColumn("RESCH. REQ MTH"); //11
            DataModel_NewParty.addColumn("OC MONTH"); //11
            DataModel_NewParty.addColumn("CURR SALES PLAN MTH"); //11
            DataModel_NewParty.addColumn("GROUP"); //8
            DataModel_NewParty.addColumn("PIECE STAGE"); //9
            DataModel_NewParty.addColumn("LENGTH"); //1
            DataModel_NewParty.addColumn("WIDTH"); //1
            DataModel_NewParty.addColumn("GSM"); //1
            DataModel_NewParty.addColumn("STYLE"); //1
            DataModel_NewParty.addColumn("WEIGHT"); //1
            DataModel_NewParty.addColumn("SQMTR"); //1
            DataModel_NewParty.addColumn("ZONE"); //1

            DataModel_NewParty.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_NewParty.SetVariable(1, "CANCEL_PIECE"); //0 - Read Only
            DataModel_NewParty.SetVariable(2, "PR_PIECE_NO"); //1
            DataModel_NewParty.SetVariable(3, "PR_PARTY_CODE"); //1
            DataModel_NewParty.SetVariable(4, "PARTY_NAME"); //1
            DataModel_NewParty.SetVariable(5, "PR_MACHINE_NO"); //1
            DataModel_NewParty.SetVariable(6, "PR_POSITION_NO"); //1
            DataModel_NewParty.SetVariable(7, "POSITION_DESC"); //1
            DataModel_NewParty.SetVariable(8, "UPN_PIECES"); //1
            DataModel_NewParty.SetVariable(9, "PR_REQUESTED_MONTH"); //1
            DataModel_NewParty.SetVariable(10, "RESCHEDULE_MONTH"); //1
            DataModel_NewParty.SetVariable(11, "OC_MONTH"); //1
            DataModel_NewParty.SetVariable(12, "CURRENT_SCHEDULE_MONTH"); //1
            DataModel_NewParty.SetVariable(13, "PR_GROUP"); //1
            DataModel_NewParty.SetVariable(14, "PR_PIECE_STAGE"); //1
            DataModel_NewParty.SetVariable(15, "LENGTH"); //1
            DataModel_NewParty.SetVariable(16, "WIDTH"); //1
            DataModel_NewParty.SetVariable(17, "GSM"); //1
            DataModel_NewParty.SetVariable(18, "STYLE"); //1
            DataModel_NewParty.SetVariable(19, "WEIGHT"); //1
            DataModel_NewParty.SetVariable(20, "SQMTR"); //1
            DataModel_NewParty.SetVariable(21, "ZONE"); //1

            for(int i=0;i<DataModel_NewParty.getColumnCount();i++)
            {
                if(i!=1 && i!=10)
                {
                    DataModel_NewParty.SetReadOnly(i);
                }
            }
            
            tblNewParty.getColumnModel().getColumn(0).setMaxWidth(40);
            tblNewParty.getColumnModel().getColumn(1).setMinWidth(100);
            tblNewParty.getColumnModel().getColumn(2).setMinWidth(100);
            tblNewParty.getColumnModel().getColumn(3).setMinWidth(100);
            tblNewParty.getColumnModel().getColumn(4).setMinWidth(150);
            tblNewParty.getColumnModel().getColumn(5).setMinWidth(70);
            tblNewParty.getColumnModel().getColumn(6).setMinWidth(0);
            tblNewParty.getColumnModel().getColumn(6).setMaxWidth(0);
            tblNewParty.getColumnModel().getColumn(7).setMinWidth(130);
            tblNewParty.getColumnModel().getColumn(8).setMinWidth(100);
            tblNewParty.getColumnModel().getColumn(9).setMinWidth(100);
            tblNewParty.getColumnModel().getColumn(10).setMinWidth(100);
            tblNewParty.getColumnModel().getColumn(11).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(12).setMinWidth(150);
            tblNewParty.getColumnModel().getColumn(13).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(14).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(15).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(16).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(17).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(18).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(19).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(20).setMinWidth(120);
            tblNewParty.getColumnModel().getColumn(21).setMinWidth(120);
            
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            int ImportCol = 1;
            Renderer.setCustomComponent(ImportCol, "CheckBox");
           
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setEnabled(true);
            aCheckBox.setSelected(false);
            aCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
//                    System.out.println(e.getStateChange() == ItemEvent.SELECTED
//                        ? "SELECTED" : "DESELECTED");
                }
            });
            tblNewParty.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblNewParty.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
            
            for(int i=0;i<DataModel_NewParty.getColumnCount();i++)
            {
                if(i!=1  && i!=10)
                {
                    DataModel_NewParty.SetReadOnly(i);
                }
            }
            TableColumn dateColumn_RESCH = tblNewParty.getColumnModel().getColumn(DataModel_NewParty.getColFromVariable("RESCHEDULE_MONTH"));
            JComboBox monthbox = new JComboBox();
            
            String month_name = "";
            Date date = new Date();
            int month;
            int year = date.getYear() + 1900;
            
            month = date.getMonth()+3;
            
            monthbox.addItem("");
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
                monthbox.addItem(month_name + " - " + year);
            }
            
            dateColumn_RESCH.setCellEditor(new DefaultCellEditor(monthbox));
            
            
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    private void FormatGridExistParty()
    {
        try {
            DataModel_ExistParty = new EITLTableModel();
            tblExistParty.removeAll();

            tblExistParty.setModel(DataModel_ExistParty);
            tblExistParty.setAutoResizeMode(0);

            DataModel_ExistParty.addColumn("SrNo"); //0 - Read Only
            DataModel_ExistParty.addColumn("CANCEL PIECE"); //0 - Read Only
            DataModel_ExistParty.addColumn("PIECE NO"); //1
            DataModel_ExistParty.addColumn("PARTY CODE"); //5
            DataModel_ExistParty.addColumn("PARTY NAME"); //6
            DataModel_ExistParty.addColumn("MAC NO"); //2
            DataModel_ExistParty.addColumn("POS NO"); //3
            DataModel_ExistParty.addColumn("POS DESC"); //4
            DataModel_ExistParty.addColumn("UPN PIECES"); //11
            DataModel_ExistParty.addColumn("REQ MTH"); //11
            DataModel_ExistParty.addColumn("RESCH. REQ MTH"); //11
            DataModel_ExistParty.addColumn("OC MONTH"); //11
            DataModel_ExistParty.addColumn("CURR SALES PLAN MTH"); //11
            DataModel_ExistParty.addColumn("GROUP"); //8
            DataModel_ExistParty.addColumn("PIECE STAGE"); //9
            DataModel_ExistParty.addColumn("LENGTH"); //1
            DataModel_ExistParty.addColumn("WIDTH"); //1
            DataModel_ExistParty.addColumn("GSM"); //1
            DataModel_ExistParty.addColumn("STYLE"); //1
            DataModel_ExistParty.addColumn("WEIGHT"); //1
            DataModel_ExistParty.addColumn("SQMTR"); //1
            DataModel_ExistParty.addColumn("ZONE"); //1

            DataModel_ExistParty.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_ExistParty.SetVariable(1, "CANCEL_PIECE"); //0 - Read Only
            DataModel_ExistParty.SetVariable(2, "PR_PIECE_NO"); //1
            DataModel_ExistParty.SetVariable(3, "PR_PARTY_CODE"); //1
            DataModel_ExistParty.SetVariable(4, "PARTY_NAME"); //1
            DataModel_ExistParty.SetVariable(5, "PR_MACHINE_NO"); //1
            DataModel_ExistParty.SetVariable(6, "PR_POSITION_NO"); //1
            DataModel_ExistParty.SetVariable(7, "POSITION_DESC"); //1
            DataModel_ExistParty.SetVariable(8, "UPN_PIECES"); //1
            DataModel_ExistParty.SetVariable(9, "PR_REQUESTED_MONTH"); //1
            DataModel_ExistParty.SetVariable(10, "RESCHEDULE_MONTH"); //1
            DataModel_ExistParty.SetVariable(11, "OC_MONTH"); //1
            DataModel_ExistParty.SetVariable(12, "CURRENT_SCHEDULE_MONTH"); //1
            DataModel_ExistParty.SetVariable(13, "PR_GROUP"); //1
            DataModel_ExistParty.SetVariable(14, "PR_PIECE_STAGE"); //1
            DataModel_ExistParty.SetVariable(15, "LENGTH"); //1
            DataModel_ExistParty.SetVariable(16, "WIDTH"); //1
            DataModel_ExistParty.SetVariable(17, "GSM"); //1
            DataModel_ExistParty.SetVariable(18, "STYLE"); //1
            DataModel_ExistParty.SetVariable(19, "WEIGHT"); //1
            DataModel_ExistParty.SetVariable(20, "SQMTR"); //1
            DataModel_ExistParty.SetVariable(21, "ZONE"); //1

            for(int i=0;i<DataModel_ExistParty.getColumnCount();i++)
            {
                if(i!=1 && i!=10)
                {
                    DataModel_ExistParty.SetReadOnly(i);
                }
            }
            
            tblExistParty.getColumnModel().getColumn(0).setMaxWidth(40);
            tblExistParty.getColumnModel().getColumn(1).setMinWidth(0);
            tblExistParty.getColumnModel().getColumn(1).setMaxWidth(0);
            tblExistParty.getColumnModel().getColumn(2).setMinWidth(100);
            tblExistParty.getColumnModel().getColumn(3).setMinWidth(100);
            tblExistParty.getColumnModel().getColumn(4).setMinWidth(150);
            tblExistParty.getColumnModel().getColumn(5).setMinWidth(70);
            tblExistParty.getColumnModel().getColumn(6).setMinWidth(0);
            tblExistParty.getColumnModel().getColumn(6).setMaxWidth(0);
            tblExistParty.getColumnModel().getColumn(7).setMinWidth(130);
            tblExistParty.getColumnModel().getColumn(8).setMinWidth(100);
            tblExistParty.getColumnModel().getColumn(9).setMinWidth(100);
            tblExistParty.getColumnModel().getColumn(10).setMinWidth(100);
            tblExistParty.getColumnModel().getColumn(11).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(12).setMinWidth(150);
            tblExistParty.getColumnModel().getColumn(13).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(14).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(15).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(16).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(17).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(18).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(19).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(20).setMinWidth(120);
            tblExistParty.getColumnModel().getColumn(21).setMinWidth(120);
            
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            int ImportCol = 1;
            Renderer.setCustomComponent(ImportCol, "CheckBox");
           
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setEnabled(true);
            aCheckBox.setSelected(false);
            aCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
//                    System.out.println(e.getStateChange() == ItemEvent.SELECTED
//                        ? "SELECTED" : "DESELECTED");
                }
            });
            tblExistParty.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblExistParty.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
            
            for(int i=0;i<DataModel_ExistParty.getColumnCount();i++)
            {
                if(i!=1  && i!=10)
                {
                    DataModel_ExistParty.SetReadOnly(i);
                }
            }
            TableColumn dateColumn_RESCH = tblExistParty.getColumnModel().getColumn(DataModel_ExistParty.getColFromVariable("RESCHEDULE_MONTH"));
            JComboBox monthbox = new JComboBox();
            
            String month_name = "";
            Date date = new Date();
            int month;
            int year = date.getYear() + 1900;
            
            month = date.getMonth()+3;
            
            monthbox.addItem("");
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
                monthbox.addItem(month_name + " - " + year);
            }
            
            dateColumn_RESCH.setCellEditor(new DefaultCellEditor(monthbox));
            
            
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    private void setNewPartyPieces(String UPN) {
        try {
           
            FormatGridNewParty();
            
            String str_query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR " +
                        "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS " +
                        "ON PS.POSITION_NO = PR.PR_POSITION_NO " +
                        "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM " +
                        "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                      + "WHERE PR.PR_PIECE_NO!='' AND COALESCE(PR_OC_MONTHYEAR,'')=''  AND PR_PIECE_STAGE IN ('PLANNING','BOOKING') AND PR_UPN='"+UPN+"'";
                
            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("New Party Pending Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_NewParty.addRow(rowData);

                DataModel_NewParty.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_NewParty.setValueByVariable("CANCEL_PIECE", Boolean.FALSE, NewRow);
                DataModel_NewParty.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_NewParty.setValueByVariable("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"), NewRow);
                DataModel_NewParty.setValueByVariable("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"), NewRow);
                DataModel_NewParty.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);
                DataModel_NewParty.setValueByVariable("UPN_PIECES", resultSet.getString("PR_UPN"), NewRow);
                DataModel_NewParty.setValueByVariable("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"), NewRow);
                DataModel_NewParty.setValueByVariable("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                DataModel_NewParty.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, resultSet.getString("PR_PARTY_CODE")), NewRow);
                DataModel_NewParty.setValueByVariable("PR_GROUP", resultSet.getString("PR_GROUP"), NewRow);
                DataModel_NewParty.setValueByVariable("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"), NewRow);
                DataModel_NewParty.setValueByVariable("OC_MONTH", resultSet.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_NewParty.setValueByVariable("CURRENT_SCHEDULE_MONTH", resultSet.getString("PR_CURRENT_SCH_MONTH"), NewRow);
                DataModel_NewParty.setValueByVariable("LENGTH", resultSet.getString("PR_BILL_LENGTH"), NewRow);
                DataModel_NewParty.setValueByVariable("WIDTH", resultSet.getString("PR_BILL_WIDTH"), NewRow);
                DataModel_NewParty.setValueByVariable("GSM", resultSet.getString("PR_BILL_GSM"), NewRow);
                DataModel_NewParty.setValueByVariable("STYLE", resultSet.getString("PR_BILL_STYLE"), NewRow);
                DataModel_NewParty.setValueByVariable("WEIGHT", resultSet.getString("PR_BILL_WEIGHT"), NewRow);
                DataModel_NewParty.setValueByVariable("SQMTR", resultSet.getString("PR_BILL_SQMTR"), NewRow);
                try{
                    String incharge = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD="+resultSet.getString("PR_INCHARGE")+"");
                    DataModel_NewParty.setValueByVariable("ZONE", incharge, NewRow);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setExistPartyPieces(String UPN) {
        try {
           
            FormatGridExistParty();
            
            String str_query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR " +
                        "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS " +
                        "ON PS.POSITION_NO = PR.PR_POSITION_NO " +
                        "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM " +
                        "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                      + "WHERE PR.PR_PIECE_NO!='' AND COALESCE(PR_OC_MONTHYEAR,'')=''  AND PR_PIECE_STAGE IN ('PLANNING','BOOKING') AND PR_UPN='"+UPN+"'";
                
            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("Exist Party Pending Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_ExistParty.addRow(rowData);

                DataModel_ExistParty.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_ExistParty.setValueByVariable("CANCEL_PIECE", Boolean.FALSE, NewRow);
                DataModel_ExistParty.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_ExistParty.setValueByVariable("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"), NewRow);
                DataModel_ExistParty.setValueByVariable("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"), NewRow);
                DataModel_ExistParty.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);
                DataModel_ExistParty.setValueByVariable("UPN_PIECES", resultSet.getString("PR_UPN"), NewRow);
                DataModel_ExistParty.setValueByVariable("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"), NewRow);
                DataModel_ExistParty.setValueByVariable("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                DataModel_ExistParty.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, resultSet.getString("PR_PARTY_CODE")), NewRow);
                DataModel_ExistParty.setValueByVariable("PR_GROUP", resultSet.getString("PR_GROUP"), NewRow);
                DataModel_ExistParty.setValueByVariable("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"), NewRow);
                DataModel_ExistParty.setValueByVariable("OC_MONTH", resultSet.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_ExistParty.setValueByVariable("CURRENT_SCHEDULE_MONTH", resultSet.getString("PR_CURRENT_SCH_MONTH"), NewRow);
                DataModel_ExistParty.setValueByVariable("LENGTH", resultSet.getString("PR_BILL_LENGTH"), NewRow);
                DataModel_ExistParty.setValueByVariable("WIDTH", resultSet.getString("PR_BILL_WIDTH"), NewRow);
                DataModel_ExistParty.setValueByVariable("GSM", resultSet.getString("PR_BILL_GSM"), NewRow);
                DataModel_ExistParty.setValueByVariable("STYLE", resultSet.getString("PR_BILL_STYLE"), NewRow);
                DataModel_ExistParty.setValueByVariable("WEIGHT", resultSet.getString("PR_BILL_WEIGHT"), NewRow);
                DataModel_ExistParty.setValueByVariable("SQMTR", resultSet.getString("PR_BILL_SQMTR"), NewRow);
                try{
                    String incharge = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD="+resultSet.getString("PR_INCHARGE")+"");
                    DataModel_ExistParty.setValueByVariable("ZONE", incharge, NewRow);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
               
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            DataModel.addColumn("POS DESIGN NO "); //3
            DataModel.addColumn("UPN"); //3
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
            DataModel.addColumn("REQ MONTH");
            DataModel.addColumn("OA NO");
            DataModel.addColumn("OA DATE");
            DataModel.addColumn("OC NO");
            DataModel.addColumn("OC MONTH");
            DataModel.addColumn("OC DATE");
            DataModel.addColumn("SURCHARGE PER");
            DataModel.addColumn("SURCHARGE RATE");
            DataModel.addColumn("GROSS RATE");
            
            DataModel.SetVariable(0, "Sr_No"); //0 - Read Only
            DataModel.SetVariable(1, "MACHINE_NO"); //1
            DataModel.SetVariable(2, "POSITION"); //2
            DataModel.SetVariable(3, "POSITION_DESC"); //3
            DataModel.SetVariable(4, "POS_DESIGN_NO"); //3
            DataModel.SetVariable(5, "UPN"); //3
            DataModel.SetVariable(6, "PIECE_NO"); //4
            DataModel.SetVariable(7, "PRODUCT"); //5
            DataModel.SetVariable(8, "DESCRIPTION"); //6
            DataModel.SetVariable(9, "GROUP"); //7
            DataModel.SetVariable(10, "LENGTH"); //8
            DataModel.SetVariable(11, "WIDTH"); //9
            DataModel.SetVariable(12, "GSM"); //10
            DataModel.SetVariable(13, "THORTICAL_WEIGHT"); //11
            DataModel.SetVariable(14, "SQ_MTR"); //12
            DataModel.SetVariable(15, "STYLE"); //13
            DataModel.SetVariable(16, "SYN"); //14
            DataModel.SetVariable(17, "REMARK"); //15
            DataModel.SetVariable(18, "OV_RATE"); //16
            DataModel.SetVariable(19, "OV_BAS_AMOUNT"); //17
            DataModel.SetVariable(20, "OV_CHEM_TRT_CHG"); //18
            DataModel.SetVariable(21, "OV_SPIRAL_CHG"); //19
            DataModel.SetVariable(22, "OV_PIN_CHG"); //20
            DataModel.SetVariable(23, "OV_SEAM_CHG"); //21
            DataModel.SetVariable(24, "OV_INS_IND"); //22
            DataModel.SetVariable(25, "OV_INS_AMT"); //23
            DataModel.SetVariable(26, "OV_EXCISE"); //24
            DataModel.SetVariable(27, "OV_DISC_PER"); //25
            DataModel.SetVariable(28, "OV_DISC_AMT"); //26
            DataModel.SetVariable(29, "OV_DISC_BASAMT"); //27
            DataModel.SetVariable(30, "VAT"); //28
            DataModel.SetVariable(31, "CST"); //29
            DataModel.SetVariable(32, "OV_AMT"); //30
            DataModel.SetVariable(33, "Exist_Piece_AMT"); //31

            DataModel.SetVariable(34, "BASE_ORDER_AMT");
            DataModel.SetVariable(35, "BASE_EXISTING_PIECE_AMT");
            DataModel.SetVariable(36, "DISCOUNT_PER");//DISCOUNT
            DataModel.SetVariable(37, "DISCOUNT_AMT");//DISCOUNT

            DataModel.SetVariable(38, "DIFFERENCE_AMT"); //32//LOSS
            DataModel.SetVariable(39, "DB_NOTE"); //33
            DataModel.SetVariable(40, "DB_AMT"); //35
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
            DataModel.SetVariable(59, "REQ_MONTH");
            DataModel.SetVariable(60, "OA_NO");
            DataModel.SetVariable(61, "OA_DATE");
            DataModel.SetVariable(62, "OC_NO");
            DataModel.SetVariable(63, "OC_MONTHYEAR");
            DataModel.SetVariable(64, "OC_DATE");
            DataModel.SetVariable(65, "SURCHARGE_PER");
            DataModel.SetVariable(66, "SURCHARGE_RATE");
            DataModel.SetVariable(67, "GROSS_RATE");
            
            
            Table.getColumnModel().getColumn(59).setMinWidth(120);
            Table.getColumnModel().getColumn(60).setMinWidth(120);
            Table.getColumnModel().getColumn(61).setMinWidth(120);
            Table.getColumnModel().getColumn(62).setMinWidth(120);
            Table.getColumnModel().getColumn(63).setMinWidth(120);
            Table.getColumnModel().getColumn(64).setMinWidth(120);
            Table.getColumnModel().getColumn(65).setMinWidth(120);
            Table.getColumnModel().getColumn(66).setMinWidth(120);
            Table.getColumnModel().getColumn(67).setMinWidth(120);
            
            for(int i=7;i<=16;i++)
            {
                DataModel.SetReadOnly(i);
            }
            for(int i=18;i<52;i++)
            {
                DataModel.SetReadOnly(i);
            }
            
            for(int i=60;i<=67;i++)
            {
                if(i!=63)
                {
                    DataModel.SetReadOnly(i);
                }
            }
            
            DataModel.SetReadOnly(56);
            DataModel.SetReadOnly(59);
            //DataModel.SetReadOnly(58);
            

            Table.getColumnModel().getColumn(0).setMinWidth(0);
            Table.getColumnModel().getColumn(0).setMaxWidth(0);
            Table.getColumnModel().getColumn(1).setMinWidth(90);
            Table.getColumnModel().getColumn(2).setMinWidth(70);
            Table.getColumnModel().getColumn(3).setMinWidth(120);
            
            Table.getColumnModel().getColumn(4).setMinWidth(120);
            Table.getColumnModel().getColumn(5).setMinWidth(120);
            
            Table.getColumnModel().getColumn(6).setMinWidth(70);
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

            Table.getColumnModel().getColumn(17).setMinWidth(100);
            Table.getColumnModel().getColumn(18).setMinWidth(100);

            for (int i = 19; i <= 31; i++) {
               if(i!=23){ 
                Table.getColumnModel().getColumn(i).setMinWidth(0);
                Table.getColumnModel().getColumn(i).setMaxWidth(0);
               }
            }

            Table.getColumnModel().getColumn(32).setMinWidth(120);
            Table.getColumnModel().getColumn(32).setMaxWidth(120);
            Table.getColumnModel().getColumn(33).setMinWidth(120);
            Table.getColumnModel().getColumn(33).setMaxWidth(120);
            Table.getColumnModel().getColumn(34).setMinWidth(120);
            Table.getColumnModel().getColumn(34).setMaxWidth(120);
            Table.getColumnModel().getColumn(35).setMinWidth(120);
            Table.getColumnModel().getColumn(36).setMinWidth(80);
            Table.getColumnModel().getColumn(35).setMinWidth(80);
            Table.getColumnModel().getColumn(38).setMinWidth(120);

            for (int i = 41; i <= 52; i++) {
                Table.getColumnModel().getColumn(i).setMinWidth(0);
                Table.getColumnModel().getColumn(i).setMaxWidth(0);
            }

            Table.getColumnModel().getColumn(53).setMinWidth(120);
            Table.getColumnModel().getColumn(53).setMaxWidth(120);
            Table.getColumnModel().getColumn(54).setMinWidth(120);
            Table.getColumnModel().getColumn(54).setMaxWidth(120);
            Table.getColumnModel().getColumn(55).setMinWidth(120);
            Table.getColumnModel().getColumn(55).setMaxWidth(120);

            Table.getColumnModel().getColumn(17).setMinWidth(0);
            Table.getColumnModel().getColumn(17).setMaxWidth(0);
            
            
            
            DataModel_ExistPiece = new EITLTableModel();
            TableExist.removeAll();

            TableExist.setModel(DataModel_ExistPiece);
            TableExist.setAutoResizeMode(0);
//
            DataModel_ExistPiece.addColumn("SrNo"); //0 - Read Only
            DataModel_ExistPiece.addColumn("MACHINE NO"); //1
            DataModel_ExistPiece.addColumn("POSITION"); //2
            DataModel_ExistPiece.addColumn("POSITION DESC"); //3
            DataModel_ExistPiece.addColumn("POS DESIGN NO"); //3
            DataModel_ExistPiece.addColumn("UPN"); //3
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
            DataModel_ExistPiece.addColumn("ACTUAL WEIGHT");//33
            DataModel_ExistPiece.addColumn("PIECE STATUS");//34
            DataModel_ExistPiece.addColumn("ACTUAL LENGTH");//33
            DataModel_ExistPiece.addColumn("ACTUAL WIDTH");//33
            DataModel_ExistPiece.addColumn("WEAVING WEIGHT");//33
            DataModel_ExistPiece.addColumn("NEEDLING WEIGHT");//33
            DataModel_ExistPiece.addColumn("BILL LENGTH");//33
            DataModel_ExistPiece.addColumn("BILL WIDTH");//33
            DataModel_ExistPiece.addColumn("BILL SQMTR");//33
            DataModel_ExistPiece.addColumn("BILL WEIGHT");//33
            DataModel_ExistPiece.addColumn("EXIST SURCHARGE PER");
            DataModel_ExistPiece.addColumn("EXIST SURCHARGE RATE");
            DataModel_ExistPiece.addColumn("EXIST GROSS RATE");

            DataModel_ExistPiece.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_ExistPiece.SetVariable(1, "MACHINE_NO"); //1
            DataModel_ExistPiece.SetVariable(2, "POSITION"); //2
            DataModel_ExistPiece.SetVariable(3, "POSITION_DESC"); //3
            DataModel_ExistPiece.SetVariable(4, "POS_DESING_NO"); //3
            DataModel_ExistPiece.SetVariable(5, "UPN"); //3
            DataModel_ExistPiece.SetVariable(6, "PIECE_NO"); //4
            DataModel_ExistPiece.SetVariable(7, "PRODUCT"); //5
            DataModel_ExistPiece.SetVariable(8, "DESCRIPTION"); //6
            DataModel_ExistPiece.SetVariable(9, "GROUP"); //7
            DataModel_ExistPiece.SetVariable(10, "LENGTH"); //8
            DataModel_ExistPiece.SetVariable(11, "WIDTH"); //9
            DataModel_ExistPiece.SetVariable(12, "GSM"); //10
            DataModel_ExistPiece.SetVariable(13, "EXISTING_WIDTH"); //11
            DataModel_ExistPiece.SetVariable(14, "SQ_MTR"); //12
            DataModel_ExistPiece.SetVariable(15, "STYLE"); //13
            DataModel_ExistPiece.SetVariable(16, "SYN"); //14
            DataModel_ExistPiece.SetVariable(17, "REMARK"); //15
            DataModel_ExistPiece.SetVariable(18, "OV_RATE"); //16
            DataModel_ExistPiece.SetVariable(19, "OV_BAS_AMOUNT"); //17
            DataModel_ExistPiece.SetVariable(20, "OV_CHEM_TRT_CHG"); //18
            DataModel_ExistPiece.SetVariable(21, "OV_SPIRAL_CHG"); //19
            DataModel_ExistPiece.SetVariable(22, "OV_PIN_CHG"); //20
            DataModel_ExistPiece.SetVariable(23, "OV_SEAM_CHG"); //21
            DataModel_ExistPiece.SetVariable(24, "OV_INS_IND"); //22
            DataModel_ExistPiece.SetVariable(25, "OV_INS_AMT"); //23
            DataModel_ExistPiece.SetVariable(26, "OV_EXCISE"); //24
            DataModel_ExistPiece.SetVariable(27, "OV_DISC_PER"); //25
            DataModel_ExistPiece.SetVariable(28, "OV_DISC_AMT"); //26
            DataModel_ExistPiece.SetVariable(29, "OV_DISC_BASAMT"); //27
            DataModel_ExistPiece.SetVariable(30, "VAT"); //28
            DataModel_ExistPiece.SetVariable(31, "CST"); //29
            DataModel_ExistPiece.SetVariable(32, "OV_AMT"); //30
            DataModel_ExistPiece.SetVariable(33, "RCVD_DATE"); //31
            DataModel_ExistPiece.SetVariable(34, "PARTY_CODE"); //32
            DataModel_ExistPiece.SetVariable(35, "PARTY_NAME"); //32
            DataModel_ExistPiece.SetVariable(36, "PARTY_CITY"); //32
            DataModel_ExistPiece.SetVariable(37, "ACTUAL_WEIGTH");
            DataModel_ExistPiece.SetVariable(38, "PIECE_STATUS");
            DataModel_ExistPiece.SetVariable(39, "ACTUAL_LENGTH");
            DataModel_ExistPiece.SetVariable(40, "ACTUAL_WIDTH");
            DataModel_ExistPiece.SetVariable(41, "WEAVING_WEIGHT");
            DataModel_ExistPiece.SetVariable(42, "NEEDLING_WEIGHT");
            DataModel_ExistPiece.SetVariable(43, "BILL_LENGTH");
            DataModel_ExistPiece.SetVariable(44, "BILL_WIDTH");
            DataModel_ExistPiece.SetVariable(45, "BILL_SQMTR");
            DataModel_ExistPiece.SetVariable(46, "BILL_WEIGHT");
            DataModel_ExistPiece.SetVariable(47, "EXIST_SURCHARGE_PER");
            DataModel_ExistPiece.SetVariable(48, "EXIST_SURCHARGE_RATE");
            DataModel_ExistPiece.SetVariable(49, "EXIST_GROSS_RATE");

            for(int i=0;i<=49;i++)
            {
                DataModel_ExistPiece.SetReadOnly(i);
            }
            
            TableExist.getColumnModel().getColumn(0).setMinWidth(20);
            TableExist.getColumnModel().getColumn(1).setMinWidth(90);
            TableExist.getColumnModel().getColumn(2).setMinWidth(70);
            TableExist.getColumnModel().getColumn(3).setMinWidth(120);
            TableExist.getColumnModel().getColumn(4).setMinWidth(70);
            TableExist.getColumnModel().getColumn(5).setMinWidth(70);
            TableExist.getColumnModel().getColumn(6).setMinWidth(70);
            TableExist.getColumnModel().getColumn(7).setMinWidth(100);
            TableExist.getColumnModel().getColumn(8).setMinWidth(120);
            TableExist.getColumnModel().getColumn(9).setMinWidth(80);
            TableExist.getColumnModel().getColumn(10).setMinWidth(70);
            TableExist.getColumnModel().getColumn(11).setMinWidth(70);
            TableExist.getColumnModel().getColumn(12).setMinWidth(50);
            TableExist.getColumnModel().getColumn(13).setMinWidth(130);
            TableExist.getColumnModel().getColumn(14).setMinWidth(80);
            TableExist.getColumnModel().getColumn(15).setMinWidth(80);
            TableExist.getColumnModel().getColumn(16).setMinWidth(100);
            TableExist.getColumnModel().getColumn(17).setMinWidth(100);
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
            TableExist.getColumnModel().getColumn(31).setMinWidth(0);
            TableExist.getColumnModel().getColumn(31).setMaxWidth(0);
            TableExist.getColumnModel().getColumn(32).setMinWidth(0);
            TableExist.getColumnModel().getColumn(32).setMaxWidth(0);
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
            
            TableExist.getColumnModel().getColumn(39).setMinWidth(120);
            TableExist.getColumnModel().getColumn(39).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(40).setMinWidth(120);
            TableExist.getColumnModel().getColumn(40).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(41).setMinWidth(120);
            TableExist.getColumnModel().getColumn(41).setMaxWidth(120);
            TableExist.getColumnModel().getColumn(42).setMinWidth(120);
            TableExist.getColumnModel().getColumn(42).setMaxWidth(120);
            
            TableExist.getColumnModel().getColumn(43).setMinWidth(120);
            TableExist.getColumnModel().getColumn(44).setMinWidth(120);
            TableExist.getColumnModel().getColumn(45).setMinWidth(120);
            TableExist.getColumnModel().getColumn(46).setMinWidth(120);
            TableExist.getColumnModel().getColumn(47).setMinWidth(150);
            TableExist.getColumnModel().getColumn(48).setMinWidth(150);
            TableExist.getColumnModel().getColumn(49).setMinWidth(120);
            
            JComboBox monthbox = new JComboBox();
            String month_name = "";
            Date date = new Date();
            int month;
            int year = date.getYear() + 1900;
            
            String Stage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE from PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+frm_Piece_No+"'");
            
            if(Stage.equals("IN STOCK"))
            {
                month = date.getMonth();
            }
            else
            {
                month = date.getMonth()+1;
            }
            
            
            monthbox.addItem("");
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
                monthbox.addItem(month_name + " - " + year);
            }
            
            
            TableColumn dateColumn = Table.getColumnModel().getColumn(DataModel.getColFromVariable("OC_MONTHYEAR"));
            dateColumn.setCellEditor(new DefaultCellEditor(monthbox));
            
            //TableColumn dateColumn1 = Table.getColumnModel().getColumn(DataModel.getColFromVariable("REQ_MONTH"));
            //dateColumn1.setCellEditor(new DefaultCellEditor(monthbox));
            
            //dateColumn.setCellEditor(new DatePi);
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
        // JOptionPane.showMessageDialog(null, "Module Id : "+ModuleId+" , Doc No : "+DOC_NO);
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableExist = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtPartyBearer = new javax.swing.JTextField();
        txtPartyBearerName = new javax.swing.JTextField();
        lblPartyBearer = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel24 = new javax.swing.JLabel();
        btnCompany = new javax.swing.JRadioButton();
        btnParty = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        btnOpenPriorApproval = new javax.swing.JButton();
        lblPriorApprovalNo = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtPartyPer = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txtSDMLPer = new javax.swing.JTextField();
        btnPartialPayment = new javax.swing.JRadioButton();
        jLabel46 = new javax.swing.JLabel();
        txtBearPartyAmt = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        txtBearCompanyAmt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        lblRevNo = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        lblDbNote = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblDbAmt = new javax.swing.JLabel();
        Show_DbNote = new javax.swing.JButton();
        lblDBNOTE = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        lblInchargeName = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        txtContactPerson = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtPhoneNo = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtEmailId = new javax.swing.JTextField();
        chbEmailUpdate = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        txtEmailId2 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        txtEmailId3 = new javax.swing.JTextField();
        lblPriorApprovalDate = new javax.swing.JLabel();
        StatusPanel1 = new javax.swing.JPanel();
        Ac8 = new javax.swing.JCheckBox();
        Ac1 = new javax.swing.JCheckBox();
        Ac2 = new javax.swing.JCheckBox();
        Ac3 = new javax.swing.JCheckBox();
        Ac4 = new javax.swing.JCheckBox();
        Ac5 = new javax.swing.JCheckBox();
        Ac6 = new javax.swing.JCheckBox();
        Ac7 = new javax.swing.JCheckBox();
        txtAction8 = new javax.swing.JTextField();
        txtAction1 = new javax.swing.JTextField();
        txtAction2 = new javax.swing.JTextField();
        txtAction3 = new javax.swing.JTextField();
        txtAction4 = new javax.swing.JTextField();
        txtAction5 = new javax.swing.JTextField();
        txtAction6 = new javax.swing.JTextField();
        txtAction7 = new javax.swing.JTextField();
        Ac9 = new javax.swing.JCheckBox();
        txtAction9 = new javax.swing.JTextField();
        Ac10 = new javax.swing.JCheckBox();
        Ac11 = new javax.swing.JCheckBox();
        Ac12 = new javax.swing.JCheckBox();
        Ac13 = new javax.swing.JCheckBox();
        Ac14 = new javax.swing.JCheckBox();
        txtAction10 = new javax.swing.JTextField();
        txtAction11 = new javax.swing.JTextField();
        txtAction12 = new javax.swing.JTextField();
        txtAction13 = new javax.swing.JTextField();
        txtAction14 = new javax.swing.JTextField();
        Ac15 = new javax.swing.JCheckBox();
        Ac17 = new javax.swing.JCheckBox();
        Ac16 = new javax.swing.JCheckBox();
        txtAction15 = new javax.swing.JTextField();
        txtAction16 = new javax.swing.JTextField();
        txtAction17 = new javax.swing.JTextField();
        AcNoAction = new javax.swing.JCheckBox();
        txtPieceNo = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
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
        StatusPanel2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        DebitMemoAmt2 = new javax.swing.JTextField();
        DebitMemoNo2 = new javax.swing.JTextField();
        DebitMemoNo3 = new javax.swing.JTextField();
        DebitMemoNo4 = new javax.swing.JTextField();
        DebitMemoNo5 = new javax.swing.JTextField();
        DebitMemoNo8 = new javax.swing.JTextField();
        DebitMemoAmt8 = new javax.swing.JTextField();
        DebitMemoAmt5 = new javax.swing.JTextField();
        DebitMemoAmt4 = new javax.swing.JTextField();
        DebitMemoAmt3 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        DebitMemoNo7 = new javax.swing.JTextField();
        DebitMemoAmt7 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        DebitMemoNo6 = new javax.swing.JTextField();
        DebitMemoAmt6 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        DebitMemoNo9 = new javax.swing.JTextField();
        DebitMemoAmt9 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        DebitMemoNo10 = new javax.swing.JTextField();
        DebitMemoAmt10 = new javax.swing.JTextField();
        Show_Memo2 = new javax.swing.JButton();
        Show_Memo3 = new javax.swing.JButton();
        Show_Memo4 = new javax.swing.JButton();
        Show_Memo5 = new javax.swing.JButton();
        Show_Memo6 = new javax.swing.JButton();
        Show_Memo7 = new javax.swing.JButton();
        Show_Memo8 = new javax.swing.JButton();
        Show_Memo9 = new javax.swing.JButton();
        Show_Memo10 = new javax.swing.JButton();
        Select10 = new javax.swing.JButton();
        Select2 = new javax.swing.JButton();
        Select3 = new javax.swing.JButton();
        Select4 = new javax.swing.JButton();
        Select5 = new javax.swing.JButton();
        Select6 = new javax.swing.JButton();
        Select7 = new javax.swing.JButton();
        Select8 = new javax.swing.JButton();
        Select9 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblNewParty = new javax.swing.JTable();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblExistParty = new javax.swing.JTable();
        jLabel55 = new javax.swing.JLabel();
        txtReschCanRemark = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtNewPartyRemark = new javax.swing.JTextField();
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

        jLabel2.setText("S.D.Date");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 10, 80, 15);

        jLabel3.setText("Diversion No");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(230, 10, 100, 15);

        jLabel5.setText("Remark");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 120, 80, 30);

        REMARK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                REMARKFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                REMARKFocusLost(evt);
            }
        });
        jPanel1.add(REMARK);
        REMARK.setBounds(80, 120, 380, 30);

        jLabel7.setText("District");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(480, 60, 80, 30);

        jLabel8.setText("Engineer");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 70, 110, 20);

        S_O_NO.setEditable(false);
        S_O_NO.setBackground(new java.awt.Color(254, 242, 230));
        S_O_NO.setText("S00000001");
        S_O_NO.setEnabled(false);
        jPanel1.add(S_O_NO);
        S_O_NO.setBounds(320, 0, 120, 30);

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
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TableKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 330, 900, 70);

        jLabel9.setText("Party Code");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 40, 100, 20);

        PARTY_CODE.setEditable(false);
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
        PARTY_CODE.setBounds(80, 30, 120, 30);

        PARTY_NAME.setEditable(false);
        jPanel1.add(PARTY_NAME);
        PARTY_NAME.setBounds(320, 30, 330, 30);

        jLabel11.setText("Reference");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(10, 90, 100, 30);

        jLabel12.setText("P.O. No.");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(280, 90, 70, 30);

        jLabel13.setText("P.O. Date");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(470, 90, 120, 30);

        P_O_NO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                P_O_NOFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                P_O_NOFocusLost(evt);
            }
        });
        jPanel1.add(P_O_NO);
        P_O_NO.setBounds(350, 90, 110, 30);

        jLabel14.setText("Reference ");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(650, 80, 130, 30);

        REFERENCE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "P.O.", "Email", "Verbal" }));
        REFERENCE.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                REFERENCEItemStateChanged(evt);
            }
        });
        jPanel1.add(REFERENCE);
        REFERENCE.setBounds(80, 90, 120, 30);

        P_O_DATE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                P_O_DATEFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                P_O_DATEFocusLost(evt);
            }
        });
        jPanel1.add(P_O_DATE);
        P_O_DATE.setBounds(550, 90, 100, 30);

        ORDER_VALUE.setEditable(false);
        ORDER_VALUE.setBackground(new java.awt.Color(209, 206, 203));
        ORDER_VALUE.setText("0");
        jPanel1.add(ORDER_VALUE);
        ORDER_VALUE.setBounds(750, 640, 170, 19);

        jLabel4.setText("Order Value ");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(660, 640, 90, 30);

        REF_DATE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                REF_DATEFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                REF_DATEFocusLost(evt);
            }
        });
        jPanel1.add(REF_DATE);
        REF_DATE.setBounds(730, 90, 140, 30);

        jLabel15.setText("Region");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(650, 40, 70, 15);

        jLabel6.setText("Country");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(650, 60, 70, 30);

        DISTRICT.setEditable(false);
        jPanel1.add(DISTRICT);
        DISTRICT.setBounds(550, 60, 100, 30);

        COUNTRY.setEditable(false);
        jPanel1.add(COUNTRY);
        COUNTRY.setBounds(730, 60, 140, 30);

        REGION.setEditable(false);
        jPanel1.add(REGION);
        REGION.setBounds(730, 30, 140, 30);

        S_ENGINEER.setEditable(false);
        jPanel1.add(S_ENGINEER);
        S_ENGINEER.setBounds(80, 60, 70, 30);

        jLabel16.setText("City");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(310, 60, 80, 30);

        CITY.setEditable(false);
        jPanel1.add(CITY);
        CITY.setBounds(350, 60, 110, 30);

        REMOVE.setText("REMOVE");
        REMOVE.setEnabled(false);
        REMOVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REMOVEActionPerformed(evt);
            }
        });
        jPanel1.add(REMOVE);
        REMOVE.setBounds(540, 640, 100, 25);

        S_O_DATE.setEditable(false);
        jPanel1.add(S_O_DATE);
        S_O_DATE.setBounds(80, 0, 120, 30);

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

        jLabel17.setText("Order Detail");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 310, 120, 20);

        jLabel18.setText("Sales ");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(10, 50, 110, 30);

        jLabel21.setText("Date");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(650, 100, 120, 20);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setEnabled(false);
        jPanel4.setLayout(null);

        txtPartyBearer.setEnabled(false);
        txtPartyBearer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartyBearerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyBearerFocusLost(evt);
            }
        });
        txtPartyBearer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyBearerKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPartyBearerKeyTyped(evt);
            }
        });
        jPanel4.add(txtPartyBearer);
        txtPartyBearer.setBounds(140, 60, 90, 20);

        txtPartyBearerName.setEditable(false);
        txtPartyBearerName.setEnabled(false);
        txtPartyBearerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartyBearerNameActionPerformed(evt);
            }
        });
        jPanel4.add(txtPartyBearerName);
        txtPartyBearerName.setBounds(230, 60, 170, 20);

        lblPartyBearer.setText("PARTY ");
        lblPartyBearer.setEnabled(false);
        jPanel4.add(lblPartyBearer);
        lblPartyBearer.setBounds(10, 60, 50, 20);
        jPanel4.add(jSeparator1);
        jSeparator1.setBounds(0, 24, 420, 2);

        jLabel24.setText("Cost Bearer By ");
        jPanel4.add(jLabel24);
        jLabel24.setBounds(10, 0, 120, 20);

        buttonGroup1.add(btnCompany);
        btnCompany.setSelected(true);
        btnCompany.setText("Company");
        btnCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompanyActionPerformed(evt);
            }
        });
        jPanel4.add(btnCompany);
        btnCompany.setBounds(120, 2, 100, 20);

        buttonGroup1.add(btnParty);
        btnParty.setText("Party");
        btnParty.setPreferredSize(new java.awt.Dimension(57, 20));
        btnParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartyActionPerformed(evt);
            }
        });
        jPanel4.add(btnParty);
        btnParty.setBounds(220, 2, 70, 20);

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.setLayout(null);

        jLabel40.setText("PRIOR APPROVAL");
        jPanel7.add(jLabel40);
        jLabel40.setBounds(70, 0, 119, 15);

        jLabel41.setText("DOC NO");
        jPanel7.add(jLabel41);
        jLabel41.setBounds(10, 30, 60, 20);

        jLabel42.setText("DOC DATE");
        jPanel7.add(jLabel42);
        jLabel42.setBounds(10, 50, 80, 20);

        btnOpenPriorApproval.setText("Show Prior Approval");
        btnOpenPriorApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenPriorApprovalActionPerformed(evt);
            }
        });
        jPanel7.add(btnOpenPriorApproval);
        btnOpenPriorApproval.setBounds(30, 80, 180, 25);

        lblPriorApprovalNo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.add(lblPriorApprovalNo);
        lblPriorApprovalNo.setBounds(100, 30, 130, 20);

        jPanel4.add(jPanel7);
        jPanel7.setBounds(420, 0, 250, 120);

        jLabel44.setText("SDML");
        jPanel4.add(jLabel44);
        jLabel44.setBounds(10, 30, 60, 15);

        txtPartyPer.setEnabled(false);
        txtPartyPer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPartyPerKeyReleased(evt);
            }
        });
        jPanel4.add(txtPartyPer);
        txtPartyPer.setBounds(70, 60, 50, 20);

        jLabel45.setText("%");
        jPanel4.add(jLabel45);
        jLabel45.setBounds(120, 60, 20, 15);

        txtSDMLPer.setDoubleBuffered(true);
        txtSDMLPer.setEnabled(false);
        jPanel4.add(txtSDMLPer);
        txtSDMLPer.setBounds(70, 30, 50, 20);

        buttonGroup1.add(btnPartialPayment);
        btnPartialPayment.setText("Partial Payment");
        btnPartialPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartialPaymentActionPerformed(evt);
            }
        });
        jPanel4.add(btnPartialPayment);
        btnPartialPayment.setBounds(290, 2, 129, 20);

        jLabel46.setText("PARTY AMOUNT");
        jPanel4.add(jLabel46);
        jLabel46.setBounds(10, 90, 120, 15);

        txtBearPartyAmt.setDoubleBuffered(true);
        txtBearPartyAmt.setEnabled(false);
        jPanel4.add(txtBearPartyAmt);
        txtBearPartyAmt.setBounds(130, 90, 80, 20);

        jLabel47.setText("SDML AMOUNT");
        jPanel4.add(jLabel47);
        jLabel47.setBounds(220, 90, 110, 15);

        txtBearCompanyAmt.setDoubleBuffered(true);
        txtBearCompanyAmt.setEnabled(false);
        jPanel4.add(txtBearCompanyAmt);
        txtBearCompanyAmt.setBounds(330, 90, 70, 20);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(10, 190, 670, 120);

        jLabel10.setText("Party Name");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(230, 30, 120, 30);

        lblRevNo.setText("...");
        jPanel1.add(lblRevNo);
        lblRevNo.setBounds(200, 0, 40, 30);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(null);

        jLabel22.setText("Debit Memo ");
        jPanel5.add(jLabel22);
        jLabel22.setBounds(70, 0, 90, 20);
        jPanel5.add(lblDbNote);
        lblDbNote.setBounds(90, 10, 110, 0);

        jLabel23.setText("Amount");
        jPanel5.add(jLabel23);
        jLabel23.setBounds(10, 30, 90, 20);

        lblDbAmt.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.add(lblDbAmt);
        lblDbAmt.setBounds(100, 50, 100, 20);

        Show_DbNote.setText("Show Debit Memo");
        Show_DbNote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_DbNoteActionPerformed(evt);
            }
        });
        jPanel5.add(Show_DbNote);
        Show_DbNote.setBounds(20, 80, 170, 30);

        lblDBNOTE.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.add(lblDBNOTE);
        lblDBNOTE.setBounds(100, 30, 100, 20);

        jLabel43.setText("Debit Memo ");
        jPanel5.add(jLabel43);
        jLabel43.setBounds(10, 50, 90, 20);

        jPanel1.add(jPanel5);
        jPanel5.setBounds(690, 190, 210, 120);
        jPanel1.add(lblInchargeName);
        lblInchargeName.setBounds(150, 60, 160, 30);

        jButton1.setText("FINISHING REPORT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(732, 400, 170, 25);

        jLabel48.setText("Person");
        jPanel1.add(jLabel48);
        jLabel48.setBounds(470, 130, 70, 30);

        txtContactPerson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContactPersonActionPerformed(evt);
            }
        });
        jPanel1.add(txtContactPerson);
        txtContactPerson.setBounds(550, 120, 100, 30);

        jLabel49.setText("Contact");
        jPanel1.add(jLabel49);
        jLabel49.setBounds(470, 110, 70, 40);
        jPanel1.add(txtPhoneNo);
        txtPhoneNo.setBounds(760, 120, 110, 30);

        jLabel50.setText("Phone Number");
        jPanel1.add(jLabel50);
        jLabel50.setBounds(650, 120, 120, 30);

        jLabel51.setText("Email Id *");
        jPanel1.add(jLabel51);
        jLabel51.setBounds(10, 150, 110, 30);

        txtEmailId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailIdActionPerformed(evt);
            }
        });
        jPanel1.add(txtEmailId);
        txtEmailId.setBounds(80, 150, 150, 30);

        chbEmailUpdate.setText("Update on Party Master");
        chbEmailUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbEmailUpdateActionPerformed(evt);
            }
        });
        jPanel1.add(chbEmailUpdate);
        chbEmailUpdate.setBounds(700, 154, 200, 23);

        jLabel20.setText("Email Id 3");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(470, 160, 90, 15);
        jPanel1.add(txtEmailId2);
        txtEmailId2.setBounds(310, 147, 150, 30);

        jLabel52.setText("Email Id 2");
        jPanel1.add(jLabel52);
        jLabel52.setBounds(240, 160, 90, 15);
        jPanel1.add(txtEmailId3);
        txtEmailId3.setBounds(550, 150, 140, 30);

        lblPriorApprovalDate.setText("                   ");
        lblPriorApprovalDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(lblPriorApprovalDate);
        lblPriorApprovalDate.setBounds(340, 510, 130, 20);

        Tab.addTab("Diversion Entry", jPanel1);

        StatusPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusPanel1.setLayout(null);

        Ac8.setText("Tagging Product code");
        Ac8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac8ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac8);
        Ac8.setBounds(10, 220, 170, 23);

        Ac1.setText("Reduction in width");
        Ac1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac1ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac1);
        Ac1.setBounds(10, 10, 160, 23);

        Ac2.setText("Increase in width");
        Ac2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac2ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac2);
        Ac2.setBounds(10, 40, 160, 23);

        Ac3.setText("Reduction in length");
        Ac3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac3ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac3);
        Ac3.setBounds(10, 70, 150, 23);

        Ac4.setText("Increase in GSM");
        Ac4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac4ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac4);
        Ac4.setBounds(10, 100, 160, 23);

        Ac5.setText("Decrease in GSM");
        Ac5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac5ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac5);
        Ac5.setBounds(10, 130, 160, 23);

        Ac6.setText("Seaming required");
        Ac6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac6ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac6);
        Ac6.setBounds(10, 160, 150, 23);

        Ac7.setText("Tagging Style ");
        Ac7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac7ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac7);
        Ac7.setBounds(10, 190, 170, 23);

        txtAction8.setEditable(false);
        StatusPanel1.add(txtAction8);
        txtAction8.setBounds(180, 220, 590, 19);

        txtAction1.setEditable(false);
        StatusPanel1.add(txtAction1);
        txtAction1.setBounds(180, 10, 590, 19);

        txtAction2.setEditable(false);
        txtAction2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAction2ActionPerformed(evt);
            }
        });
        StatusPanel1.add(txtAction2);
        txtAction2.setBounds(180, 40, 590, 19);

        txtAction3.setEditable(false);
        StatusPanel1.add(txtAction3);
        txtAction3.setBounds(180, 70, 590, 19);

        txtAction4.setEditable(false);
        StatusPanel1.add(txtAction4);
        txtAction4.setBounds(180, 100, 590, 19);

        txtAction5.setEditable(false);
        StatusPanel1.add(txtAction5);
        txtAction5.setBounds(180, 130, 590, 19);

        txtAction6.setEditable(false);
        StatusPanel1.add(txtAction6);
        txtAction6.setBounds(180, 160, 590, 19);

        txtAction7.setEditable(false);
        StatusPanel1.add(txtAction7);
        txtAction7.setBounds(180, 190, 590, 19);

        Ac9.setText("Tagging Length");
        Ac9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac9ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac9);
        Ac9.setBounds(10, 250, 160, 23);

        txtAction9.setEditable(false);
        StatusPanel1.add(txtAction9);
        txtAction9.setBounds(180, 250, 590, 30);

        Ac10.setText("Tagging Width");
        Ac10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac10ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac10);
        Ac10.setBounds(10, 280, 170, 23);

        Ac11.setText("Tagging GSM");
        Ac11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac11ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac11);
        Ac11.setBounds(10, 310, 150, 23);

        Ac12.setText("Tagging CFM");
        Ac12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac12ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac12);
        Ac12.setBounds(10, 340, 170, 20);

        Ac13.setText("Tagging Thickness");
        Ac13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac13ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac13);
        Ac13.setBounds(10, 370, 170, 23);

        Ac14.setText("Tagging weight");
        Ac14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac14ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac14);
        Ac14.setBounds(10, 400, 150, 23);

        txtAction10.setEditable(false);
        StatusPanel1.add(txtAction10);
        txtAction10.setBounds(180, 280, 590, 19);

        txtAction11.setEditable(false);
        StatusPanel1.add(txtAction11);
        txtAction11.setBounds(180, 310, 590, 19);

        txtAction12.setEditable(false);
        StatusPanel1.add(txtAction12);
        txtAction12.setBounds(180, 340, 590, 19);

        txtAction13.setEditable(false);
        StatusPanel1.add(txtAction13);
        txtAction13.setBounds(180, 370, 590, 30);

        txtAction14.setEditable(false);
        StatusPanel1.add(txtAction14);
        txtAction14.setBounds(180, 400, 590, 30);

        Ac15.setText("Trim to be retained by Production");
        Ac15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac15ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac15);
        Ac15.setBounds(10, 430, 270, 23);

        Ac17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac17ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac17);
        Ac17.setBounds(10, 490, 21, 21);

        Ac16.setText("Trim to be returned to WH with felt");
        Ac16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ac16ActionPerformed(evt);
            }
        });
        StatusPanel1.add(Ac16);
        Ac16.setBounds(10, 460, 270, 23);

        txtAction15.setEditable(false);
        StatusPanel1.add(txtAction15);
        txtAction15.setBounds(280, 430, 490, 30);

        txtAction16.setEditable(false);
        StatusPanel1.add(txtAction16);
        txtAction16.setBounds(280, 460, 490, 30);

        txtAction17.setEditable(false);
        StatusPanel1.add(txtAction17);
        txtAction17.setBounds(280, 490, 490, 20);

        AcNoAction.setText("No Action");
        AcNoAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcNoActionActionPerformed(evt);
            }
        });
        StatusPanel1.add(AcNoAction);
        AcNoAction.setBounds(800, 10, 120, 23);

        txtPieceNo.setEditable(false);
        StatusPanel1.add(txtPieceNo);
        txtPieceNo.setBounds(800, 70, 100, 19);

        jButton2.setText("Report");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        StatusPanel1.add(jButton2);
        jButton2.setBounds(800, 110, 100, 25);

        Tab.addTab("Action To be Taken", StatusPanel1);

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

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
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

        StatusPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusPanel2.setLayout(null);

        jLabel25.setText("Debit Memo 2");
        StatusPanel2.add(jLabel25);
        jLabel25.setBounds(10, 30, 100, 15);

        jLabel26.setText("Debit Memo 3");
        StatusPanel2.add(jLabel26);
        jLabel26.setBounds(10, 70, 100, 15);

        jLabel27.setText("Debit Memo 4");
        StatusPanel2.add(jLabel27);
        jLabel27.setBounds(10, 100, 100, 30);

        jLabel28.setText("Debit Memo 5");
        StatusPanel2.add(jLabel28);
        jLabel28.setBounds(10, 150, 100, 15);

        jLabel29.setText("Debit Memo 8");
        StatusPanel2.add(jLabel29);
        jLabel29.setBounds(10, 270, 100, 15);

        DebitMemoAmt2.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt2);
        DebitMemoAmt2.setBounds(330, 20, 120, 19);

        DebitMemoNo2.setEnabled(false);
        DebitMemoNo2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DebitMemoNo2KeyPressed(evt);
            }
        });
        StatusPanel2.add(DebitMemoNo2);
        DebitMemoNo2.setBounds(210, 20, 120, 19);

        DebitMemoNo3.setEnabled(false);
        StatusPanel2.add(DebitMemoNo3);
        DebitMemoNo3.setBounds(210, 60, 120, 19);

        DebitMemoNo4.setEnabled(false);
        StatusPanel2.add(DebitMemoNo4);
        DebitMemoNo4.setBounds(210, 100, 120, 19);

        DebitMemoNo5.setEnabled(false);
        StatusPanel2.add(DebitMemoNo5);
        DebitMemoNo5.setBounds(210, 140, 120, 19);

        DebitMemoNo8.setEnabled(false);
        StatusPanel2.add(DebitMemoNo8);
        DebitMemoNo8.setBounds(210, 260, 120, 19);

        DebitMemoAmt8.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt8);
        DebitMemoAmt8.setBounds(330, 260, 120, 19);

        DebitMemoAmt5.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt5);
        DebitMemoAmt5.setBounds(330, 140, 120, 19);

        DebitMemoAmt4.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt4);
        DebitMemoAmt4.setBounds(330, 100, 120, 19);

        DebitMemoAmt3.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt3);
        DebitMemoAmt3.setBounds(330, 60, 120, 19);

        jLabel30.setText("Debit Memo 7");
        StatusPanel2.add(jLabel30);
        jLabel30.setBounds(10, 230, 100, 15);

        DebitMemoNo7.setEnabled(false);
        StatusPanel2.add(DebitMemoNo7);
        DebitMemoNo7.setBounds(210, 220, 120, 19);

        DebitMemoAmt7.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt7);
        DebitMemoAmt7.setBounds(330, 220, 120, 19);

        jLabel37.setText("Debit Memo 6");
        StatusPanel2.add(jLabel37);
        jLabel37.setBounds(10, 190, 100, 15);

        DebitMemoNo6.setEnabled(false);
        StatusPanel2.add(DebitMemoNo6);
        DebitMemoNo6.setBounds(210, 180, 120, 19);

        DebitMemoAmt6.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt6);
        DebitMemoAmt6.setBounds(330, 180, 120, 19);

        jLabel38.setText("Debit Memo 9");
        StatusPanel2.add(jLabel38);
        jLabel38.setBounds(10, 310, 100, 15);

        DebitMemoNo9.setEnabled(false);
        StatusPanel2.add(DebitMemoNo9);
        DebitMemoNo9.setBounds(210, 300, 120, 19);

        DebitMemoAmt9.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt9);
        DebitMemoAmt9.setBounds(330, 300, 120, 19);

        jLabel39.setText("Debit Memo 10");
        StatusPanel2.add(jLabel39);
        jLabel39.setBounds(10, 350, 100, 15);

        DebitMemoNo10.setEnabled(false);
        StatusPanel2.add(DebitMemoNo10);
        DebitMemoNo10.setBounds(210, 340, 120, 19);

        DebitMemoAmt10.setEnabled(false);
        StatusPanel2.add(DebitMemoAmt10);
        DebitMemoAmt10.setBounds(330, 340, 120, 19);

        Show_Memo2.setText("Show Debit Memo 2");
        Show_Memo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo2ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo2);
        Show_Memo2.setBounds(450, 20, 170, 25);

        Show_Memo3.setText("Show Debit Memo 3");
        Show_Memo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo3ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo3);
        Show_Memo3.setBounds(450, 60, 170, 25);

        Show_Memo4.setText("Show Debit Memo 4");
        Show_Memo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo4ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo4);
        Show_Memo4.setBounds(450, 100, 170, 25);

        Show_Memo5.setText("Show Debit Memo 5");
        Show_Memo5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo5ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo5);
        Show_Memo5.setBounds(450, 140, 170, 25);

        Show_Memo6.setText("Show Debit Memo 6");
        Show_Memo6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo6ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo6);
        Show_Memo6.setBounds(450, 180, 170, 25);

        Show_Memo7.setText("Show Debit Memo 7");
        Show_Memo7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo7ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo7);
        Show_Memo7.setBounds(450, 220, 170, 25);

        Show_Memo8.setText("Show Debit Memo 8");
        Show_Memo8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo8ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo8);
        Show_Memo8.setBounds(450, 260, 170, 25);

        Show_Memo9.setText("Show Debit Memo 9");
        Show_Memo9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo9ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo9);
        Show_Memo9.setBounds(450, 300, 170, 25);

        Show_Memo10.setText("Show Debit Memo 10");
        Show_Memo10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_Memo10ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Show_Memo10);
        Show_Memo10.setBounds(450, 340, 170, 25);

        Select10.setText("SELECT");
        Select10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select10ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Select10);
        Select10.setBounds(120, 340, 90, 25);

        Select2.setText("SELECT");
        Select2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select2ActionPerformed(evt);
            }
        });
        Select2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Select2KeyPressed(evt);
            }
        });
        StatusPanel2.add(Select2);
        Select2.setBounds(120, 20, 90, 25);

        Select3.setText("SELECT");
        Select3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select3ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Select3);
        Select3.setBounds(120, 60, 90, 25);

        Select4.setText("SELECT");
        Select4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select4ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Select4);
        Select4.setBounds(120, 100, 90, 25);

        Select5.setText("SELECT");
        Select5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select5ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Select5);
        Select5.setBounds(120, 140, 90, 25);

        Select6.setText("SELECT");
        Select6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select6ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Select6);
        Select6.setBounds(120, 180, 90, 25);

        Select7.setText("SELECT");
        Select7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select7ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Select7);
        Select7.setBounds(120, 220, 90, 25);

        Select8.setText("SELECT");
        Select8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select8ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Select8);
        Select8.setBounds(120, 260, 90, 25);

        Select9.setText("SELECT");
        Select9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select9ActionPerformed(evt);
            }
        });
        StatusPanel2.add(Select9);
        Select9.setBounds(120, 300, 90, 25);

        Tab.addTab("EXTRA DEBIT MEMO MAPPING", StatusPanel2);

        jPanel8.setLayout(null);

        jLabel53.setText("Pending order for Existing Party (With same UPN and OC Month not confirmed)");
        jPanel8.add(jLabel53);
        jLabel53.setBounds(20, 230, 570, 20);

        tblNewParty.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tblNewParty);

        jPanel8.add(jScrollPane4);
        jScrollPane4.setBounds(10, 20, 900, 170);

        jLabel54.setText("Pending order for New Party (With same UPN and OC Month not confirmed)");
        jPanel8.add(jLabel54);
        jLabel54.setBounds(20, 0, 570, 15);

        tblExistParty.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tblExistParty);

        jPanel8.add(jScrollPane5);
        jScrollPane5.setBounds(10, 250, 900, 190);

        jLabel55.setText("Exist Party Remark");
        jPanel8.add(jLabel55);
        jLabel55.setBounds(10, 440, 140, 40);
        jPanel8.add(txtReschCanRemark);
        txtReschCanRemark.setBounds(140, 440, 770, 30);

        jLabel56.setText("New Party Remark");
        jPanel8.add(jLabel56);
        jLabel56.setBounds(20, 200, 160, 15);
        jPanel8.add(txtNewPartyRemark);
        txtNewPartyRemark.setBounds(160, 190, 750, 19);

        Tab.addTab("Reschedule & Cancellation", jPanel8);

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
        lblTitle.setText("Felt Sales Order Diversion");
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

        if (evt.getKeyCode() == 112 || evt.getKeyCode() == 10) {
            if (Table.getSelectedColumn() == 39 || Table.getSelectedColumn() == 40
                    ) {

                if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                //aList.SQL = "SELECT VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO LIKE 'DN%'  AND approved = 1 AND cancelled = 0";

                // String str = "SELECT B.VOUCHER_NO,A.VOUCHER_DATE,B.AMOUNT  FROM FINANCE.D_FIN_VOUCHER_HEADER A,FINANCE.D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO LIKE 'DN%'  AND A.VOUCHER_NO = B.VOUCHER_NO AND A.approved = 1 AND A.cancelled = 0 AND B.SUB_ACCOUNT_CODE = "+PARTY_CODE.getText()+" ORDER BY B.VOUCHER_NO DESC";
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String Piece_No = DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0);
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' AND PIECE_NO='"+Piece_No+"' AND (DM_STATUS='NOT USED' OR DM_STATUS IS NULL) AND APPROVED=1";
                
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    // String Amount = data.getStringValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO = '"+aList.ReturnVal+"' AND EFFECT='D'");
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    lblDBNOTE.setText(aList.ReturnVal);
                    lblDbAmt.setText(Amount);

                    DataModel.setValueByVariable("DB_NOTE", aList.ReturnVal, 0);
                    DataModel.setValueByVariable("DB_AMT", Amount, 0);

                }

            }
        }
//
//            FeltInvCalc inv_calc = new FeltInvCalc();
//
//            String product_code = DataModel.getValueAt(Table.getSelectedRow(), 5).toString();
//            
//            float length = Float.parseFloat(DataModel.getValueAt(Table.getSelectedRow(), 47).toString());
//            float width = Float.parseFloat(DataModel.getValueAt(Table.getSelectedRow(), 48).toString());
//            int gsm = Integer.parseInt(DataModel.getValueAt(Table.getSelectedRow(), 51).toString());
//            
//            float Theoritical_Weigth = ((length * width * gsm) / 1000);
//
//            float SQMT = (length * width);
//            
//                        try {
//                            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
//                            inv_calc = clsOrderValueCalc.calculate("", product_code, PARTY_CODE.getText(), length, width,  Theoritical_Weigth, SQMT, df1.format(df.parse(S_O_DATE.getText())));
//                            //calculate(String Piece_No,String Product_Code,String Party_Code,Float Length,Float Width,Integer GSM,Float Weight,Float SQMT,String Order_Date)
//                        } catch (ParseException ex) {
//                            ex.printStackTrace();
//                        }
//
//                        DataModel.setValueAt(inv_calc.getFicRate(), 0, 16);
//                        //JOptionPane.showMessageDialog(null, "Base Amouint catch at table model : "+inv_calc.getFicBasAmount());
//                        DataModel.setValueAt(inv_calc.getFicBasAmount(), 0, 17);
//                        DataModel.setValueAt(inv_calc.getFicChemTrtChg(), 0, 18);
//                        DataModel.setValueAt(inv_calc.getFicSpiralChg(), 0, 19);
//                        DataModel.setValueAt(inv_calc.getFicPinChg(), 0, 20);
//                        DataModel.setValueAt(inv_calc.getFicSeamChg(), 0, 21);
//                        DataModel.setValueAt(0, 0, 22);
//                        DataModel.setValueAt(0, 0, 23);
//                        DataModel.setValueAt(0, 0, 24);
//                        DataModel.setValueAt(0, 0, 25);
//                        DataModel.setValueAt(0, 0, 26);
//                        DataModel.setValueAt(0, 0, 27);
//                        DataModel.setValueAt(inv_calc.getVat(), 0, 28);
//                        DataModel.setValueAt(inv_calc.getCst(), 0, 29);
//                        DataModel.setValueAt(inv_calc.getFicInvAmt(), 0, 30);
//
//                        DataModel.setValueAt(inv_calc.getFicCGSTPER(), 0, 35);
//                        DataModel.setValueAt(inv_calc.getFicCGST(), 0, 36);
//                        DataModel.setValueAt(inv_calc.getFicSGSTPER(), 0, 37);
//                        DataModel.setValueAt(inv_calc.getFicSGST(), 0, 38);
//                        DataModel.setValueAt(inv_calc.getFicIGSTPER(), 0, 39);
//                        DataModel.setValueAt(inv_calc.getFicIGST(), 0, 40);
//                        DataModel.setValueAt("0", 0, 41);
//                        DataModel.setValueAt("0", 0, 42);
//                        DataModel.setValueAt("0", 0, 43);
//                        DataModel.setValueAt("0", 0, 44);
//                        DataModel.setValueAt("0", 0, 45);
//                        DataModel.setValueAt("0", 0, 46);
//                        
//                        DataModel.setValueByVariable("PR_BILL_LENGTH", inv_calc.getFicLength()+"", Table.getSelectedRow());
//                        DataModel.setValueByVariable("PR_BILL_WIDTH", inv_calc.getFicWidth()+"", Table.getSelectedRow());
//                        DataModel.setValueByVariable("PR_BILL_WEIGHT", inv_calc.getFicWeight()+"", Table.getSelectedRow());
//                        DataModel.setValueByVariable("PR_BILL_SQMTR", inv_calc.getFicSqMtr()+"", Table.getSelectedRow());
//                        DataModel.setValueByVariable("PR_BILL_GSM", gsm+"", Table.getSelectedRow());
//                        DataModel.setValueByVariable("PR_BILL_PRODUCT_CODE", inv_calc.getFicProductCode()+"", Table.getSelectedRow());
//                        
//                        String CATEGORY="",SQM_IND="",CHEM_TRT_IND="",PIN_IND="",SPR_IND="",SUR_IND="";
//                                    float SQM_RATE=0,SQM_CHRG=0,WT_RATE=0,CHARGES=0,CHEM_TRT_CHRG=0,PIN_CHRG=0,SPR_CHRG=0,SUR_CHRG=0;
//
//                        
//                        try {
//                                ResultSet rsData;
//                                Connection Conn;
//                                Statement stmt;
//                               Conn = data.getConn();
//                               stmt = Conn.createStatement();
//                               DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
//
//                              // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
//                              // rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");
//                               product_code = product_code.substring(0, 6);
//                               System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+product_code+"' AND EFFECTIVE_FROM <= '"+df1.format(df.parse(S_O_DATE.getText()))+"' AND (EFFECTIVE_TO >= '"+df1.format(df.parse(S_O_DATE.getText()))+"' or EFFECTIVE_TO >= \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
//                               rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+product_code+"' AND EFFECTIVE_FROM <= '"+df1.format(df.parse(S_O_DATE.getText()))+"' AND (EFFECTIVE_TO >= '"+df1.format(df.parse(S_O_DATE.getText()))+"' or EFFECTIVE_TO >= \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
//
//                               rsData.first();
//
//                               CATEGORY = rsData.getString(1);
//                               SQM_RATE = rsData.getFloat(2);
//                               WT_RATE = rsData.getFloat(3);
//                               SQM_IND = rsData.getString(4);
//                               SQM_CHRG =  rsData.getFloat(5);
//                               CHEM_TRT_IND = rsData.getString(6);
//                               CHEM_TRT_CHRG = rsData.getFloat(7);
//                               PIN_IND = rsData.getString(8);
//                               PIN_CHRG= rsData.getFloat(9);
//                               SPR_IND = rsData.getString(10);
//                               SPR_CHRG = rsData.getFloat(11);
//                               SUR_IND = rsData.getString(12);
//                               SUR_CHRG = rsData.getFloat(13);
//                               CHARGES = rsData.getFloat(14);
//
//                        }catch(Exception e)
//                        {
//                            System.out.println("Error 2: "+e.getMessage());
//                        } 
//                        
//                          float diff = 0;
//                                      
//                                     switch (SQM_IND) {
//                                        case "1":
//                                                    float trim_length = inv_calc_existing.getFicLength()- inv_calc.getFicLength();
//                                                    float trim_width = inv_calc_existing.getFicWidth() - inv_calc.getFicWidth();
//                                                    float trim_amt = SPR_CHRG * (trim_width*trim_length);   
//
//                                                    float exist_piece = inv_calc_existing.getFicLength() * inv_calc_existing.getFicWidth();
//                                                    float order_piece = inv_calc.getFicLength() * inv_calc.getFicWidth();
//                                                    
//                                                    float trim_piece = exist_piece - order_piece;
//                                                    float trim_piece_amt = SQM_RATE * trim_piece;
//                                                    System.out.println("exist_piece : "+exist_piece+" - order_piece : "+order_piece);
//                                                    diff =  trim_amt + trim_piece_amt;
//                                                    
//                                                    //base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() *  inv_calc.getFicRate();
//                                            break;
//                                        case "0":
//                                            
//                                              diff = inv_calc.getFicInvAmt() - inv_calc_existing.getFicInvAmt();
//                                              // base_amt = inv_calc.getFicWeight() * inv_calc.getFicRate();
//                                            break;
//                                    }
//                                      
//                                     DataModel.setValueAt(inv_calc_existing.getFicInvAmt(), 0, 31);
//                                     
//                                     
//                                     difference = diff;
//                                     DataModel.setValueAt(diff, 0, 32);
//                        
//            cal_order_value();            
//        }
    }//GEN-LAST:event_TableKeyPressed
    private void cal_order_value() {
        float inv_amt = 0;
        for (int i = 0; i < Table.getRowCount(); i++) {
            if (!Table.getValueAt(i, 33).toString().equals("")) {
                inv_amt = inv_amt + Float.parseFloat(Table.getValueAt(i, 33).toString());
            }
        }
        ORDER_VALUE.setText(inv_amt + "");
    }
    private void PARTY_CODEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PARTY_CODEFocusLost
        lblStatus1.setText("");
    }//GEN-LAST:event_PARTY_CODEFocusLost

    private void PARTY_CODEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PARTY_CODEKeyPressed

    }//GEN-LAST:event_PARTY_CODEKeyPressed

    private void PARTY_CODEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PARTY_CODEKeyTyped

    }//GEN-LAST:event_PARTY_CODEKeyTyped

    private void REMOVEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REMOVEActionPerformed
        if (Table.getRowCount() > 0) {
            DataModel.removeRow(Table.getSelectedRow());
            // DisplayIndicators();
        }
    }//GEN-LAST:event_REMOVEActionPerformed

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged

        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);//GAURANG
        //JOptionPane.showMessageDialog(null, "On State Change SelHierarchyId : "+SelHierarchyID);
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
        if (!EITLERPGLOBAL.isDate(P_O_DATE.getText())) {
            P_O_DATE.setText("00/00/0000");
        }

    }//GEN-LAST:event_P_O_DATEFocusLost

    private void REF_DATEFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REF_DATEFocusGained
        // TODO add your handling code here:
        lblStatus1.setText("Please enter Reference Date in DD/MM/YYYY formate only");
    }//GEN-LAST:event_REF_DATEFocusGained

    private void REF_DATEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_REF_DATEFocusLost
        // TODO add your handling code here:
        lblStatus1.setText("");
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

    private void txtPartyBearerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyBearerFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyBearerFocusGained

    private void txtPartyBearerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyBearerFocusLost
        // TODO add your handling code here:
        if (!txtPartyBearer.getText().equals("")) {
            txtPartyBearerName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, txtPartyBearer.getText()));
        }

    }//GEN-LAST:event_txtPartyBearerFocusLost

    private void txtPartyBearerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyBearerKeyPressed
        // TODO add your handling code here:
        txtPartyBearerName.setText("");

        if (evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();
                String Existing_Party = DataModel_ExistPiece.getValueAt(0, 34).toString();
                
                String group_parties = "SELECT PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL where GROUP_CODE IN (SELECT GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL where PARTY_CODE='" + PARTY_CODE.getText() + "' or PARTY_CODE='" + Existing_Party + "')";

                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE='210010' and PARTY_CODE IN (" + group_parties + " UNION ALL "
                        + "SELECT '" + PARTY_CODE.getText() + "' FROM DUAL "
                        + "UNION ALL  "
                        + "SELECT '" + Existing_Party + "' FROM DUAL)";
                //aList.SQL = group_parties;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    txtPartyBearer.setText(aList.ReturnVal);

                    clsSales_Party objParty = (clsSales_Party) clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, aList.ReturnVal, "210010");
                    txtPartyBearerName.setText(objParty.getAttribute("PARTY_NAME").getString());

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error 6 = " + e.getMessage());
            }
        }
    }//GEN-LAST:event_txtPartyBearerKeyPressed

    private void txtPartyBearerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyBearerKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyBearerKeyTyped

    private void txtPartyBearerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartyBearerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyBearerNameActionPerformed

    private void btnCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompanyActionPerformed
        // TODO add your handling code here:
        if(EditMode == EITLERPGLOBAL.EDIT || EditMode == EITLERPGLOBAL.ADD)
        {
            if (btnCompany.isSelected()) {

                lblPartyBearer.setEnabled(false);
                //lblPartyBearerName.setEnabled(false);
                txtPartyBearer.setEnabled(false);
                txtPartyBearerName.setEnabled(false);
                txtPartyBearer.setText("");
                txtPartyPer.setEnabled(false);
                txtPartyBearerName.setText("");
                //filterHierarchyCombo();
                txtSDMLPer.setText("100");
                txtPartyPer.setText("0");
                
                findAmount();
                DataModel.setValueByVariable("DB_NOTE", "SDML", 0);
                DataModel.setValueByVariable("DB_AMT", "0", 0);

                lblDBNOTE.setText("");
                lblDbAmt.setText("");
                
                String New_Piece_No = DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0);
                System.out.println("PIECE NO : "+New_Piece_No);
                String Prior_Approval_No = data.getStringValueFromDB("SELECT SD_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE APPROVED=1 AND CANCELED=0 AND D_PARTY_CODE='"+PARTY_CODE.getText()+"' AND ORIGINAL_PIECE_NO='"+New_Piece_No+"'  ORDER BY SD_ORDER_DATE DESC");
                String Prior_Approval_Date = data.getStringValueFromDB("SELECT SD_ORDER_DATE FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE APPROVED=1 AND CANCELED=0  AND D_PARTY_CODE='"+PARTY_CODE.getText()+"'  AND ORIGINAL_PIECE_NO='"+New_Piece_No+"'  ORDER BY SD_ORDER_DATE DESC");
                

              //System.out.println("SELECT SD_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE APPROVED=1 AND CANCELED=0  AND ORIGINAL_PIECE_NO='"+New_Piece_No+"'");
                lblPriorApprovalNo.setText(Prior_Approval_No);
                lblPriorApprovalDate.setText(Prior_Approval_Date);
                btnOpenPriorApproval.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnCompanyActionPerformed

    private void btnPartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartyActionPerformed
        // TODO add your handling code here:
        if(EditMode == EITLERPGLOBAL.EDIT || EditMode == EITLERPGLOBAL.ADD)
        {
            if (btnParty.isSelected()) {
                lblPartyBearer.setEnabled(true);
                //lblPartyBearerName.setEnabled(true);
                txtPartyBearer.setEnabled(true);
                txtPartyBearerName.setEnabled(true);
                txtSDMLPer.setText("0");
                txtPartyPer.setText("100");
                findAmount();
                //filterHierarchyCombo();
                lblPriorApprovalNo.setText("");
                lblPriorApprovalDate.setText("");
                double profit_loss = Double.parseDouble(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
                if(profit_loss>0)
                {
                    DataModel.setValueByVariable("DB_NOTE", "", 0);
                    DataModel.setValueByVariable("DB_AMT", "", 0);
                }
                else
                {
                    DataModel.setValueByVariable("DB_NOTE", "NA", 0);
                    DataModel.setValueByVariable("DB_AMT", "NA", 0);
                }
            }
        }
    }//GEN-LAST:event_btnPartyActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.OrderDiversion.frmReportFeltOrderDiversion", true);
        frmReportFeltOrderDiversion ObjFindFeltorder = (frmReportFeltOrderDiversion) ObjLoader.getObj();
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void Show_DbNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_DbNoteActionPerformed
        // TODO add your handling code here:

        if (lblDBNOTE.getText().equals("")) {

        }
        else if (lblDBNOTE.getText().equals("NA")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Applicable");
        } 
        else if (lblDBNOTE.getText().equals("SDML")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Applicable");
        } 
        else {

//                
//                                AppletFrame aFrame = new AppletFrame("Debit Note Voucher");
//                                aFrame.startAppletEx("EITLERP.Finance.frmDebitNoteVoucher", "Debit Note Voucher");
//                                frmDebitNoteVoucher ObjItem = (frmDebitNoteVoucher) aFrame.ObjApplet;
//                                ObjItem.requestFocus();
//                                ObjItem.FindEx(EITLERPGLOBAL.gCompanyID, lblDBNOTE.getText());
//                                
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(lblDBNOTE.getText());
        }
    }//GEN-LAST:event_Show_DbNoteActionPerformed

    private void Ac1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac1ActionPerformed
        // TODO add your handling code here:
        
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac1.setSelected(false);
            return;
        }

        if (Ac2.isSelected()) {
            JOptionPane.showMessageDialog(this, "Reduction in width not allowed because Increase in width selected ");
            Ac1.setSelected(false);
            return;
        }

        if (Ac1.isSelected()) {
            txtAction1.setEditable(true);
        } else {
            txtAction1.setEditable(false);
            txtAction1.setText("");
        }

    }//GEN-LAST:event_Ac1ActionPerformed

    private void Ac2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac2ActionPerformed
        // TODO add your handling code here:

        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac2.setSelected(false);
            return;
        }
        
        if (Ac1.isSelected()) {
            JOptionPane.showMessageDialog(this, "Increase in width not allowed because Reduction in width selected ");
            Ac2.setSelected(false);
            return;
        }

//        if ((DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("FELT FINISHING") || DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("FINISHING") || DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("STOCK") || DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("IN STOCK"))) {
//            JOptionPane.showMessageDialog(null, "Increase in width is only allowed upto FINISHING");
//            Ac2.setSelected(false);
//            txtAction2.setText("");
//            return;
//        }
        
        if ((DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("STOCK") || DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("IN STOCK"))) {
            JOptionPane.showMessageDialog(null, "Increase in width is only allowed upto FINISHING");
            Ac2.setSelected(false);
            txtAction2.setText("");
            return;
        }

        if (Ac2.isSelected()) {
            txtAction2.setEditable(true);
        } else {
            txtAction2.setEditable(false);
            txtAction2.setText("");
        }
    }//GEN-LAST:event_Ac2ActionPerformed

    private void Ac7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac7ActionPerformed
        // TODO add your handling code here:

        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac7.setSelected(false);
            return;
        }
        
        if (Ac7.isSelected()) {
            txtAction7.setEditable(true);
        } else {
            txtAction7.setEditable(false);
            txtAction7.setText("");
        }

    }//GEN-LAST:event_Ac7ActionPerformed

    private void Ac3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac3ActionPerformed
        // TODO add your handling code here:

        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac3.setSelected(false);
            return;
        }
        
        String PRODUCT_CODE = DataModel_ExistPiece.getValueByVariable("PRODUCT", 0).substring(0, 6);
        System.out.println("PRODUCT CODE : " + PRODUCT_CODE);
        if (!(PRODUCT_CODE.equals("719011") || PRODUCT_CODE.equals("719021") || PRODUCT_CODE.equals("719031") || PRODUCT_CODE.equals("719041") || PRODUCT_CODE.equals("719051") || PRODUCT_CODE.equals("729000") || PRODUCT_CODE.equals("322001") || PRODUCT_CODE.equals("325001") || PRODUCT_CODE.equals("259000") || PRODUCT_CODE.equals("153100"))) {
            JOptionPane.showMessageDialog(null, "Reduction in length is only allowed with Product Code : 719011,719021,719031,719041,719051,729000,322001,325001,259000,153100");
            Ac3.setSelected(false);
            txtAction3.setText("");
            return;
        }

        if (Ac3.isSelected()) {
            txtAction3.setEditable(true);
        } else {
            txtAction3.setEditable(false);
            txtAction3.setText("");
        }
    }//GEN-LAST:event_Ac3ActionPerformed

    private void Ac4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac4ActionPerformed
        // TODO add your handling code here:

        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac4.setSelected(false);
            return;
        }
        
        if (Ac5.isSelected()) {
            JOptionPane.showMessageDialog(this, "Increase in GSM not allowed because Decrease in GSM ");
            Ac5.setSelected(false);
            return;
        }

        String PRODUCT_CODE = DataModel_ExistPiece.getValueByVariable("PRODUCT", 0).substring(0, 6);
        System.out.println("PRODUCT CODE : " + PRODUCT_CODE);
        if (PRODUCT_CODE.equals("719011") || PRODUCT_CODE.equals("719021") || PRODUCT_CODE.equals("719031") || PRODUCT_CODE.equals("719041") || PRODUCT_CODE.equals("719051") || PRODUCT_CODE.equals("729000") || PRODUCT_CODE.equals("322001") || PRODUCT_CODE.equals("325001") || PRODUCT_CODE.equals("259000") || PRODUCT_CODE.equals("153100")) {
            JOptionPane.showMessageDialog(null, "Increase in GSM is only allowed except with Product Code : 719011,719021,719031,719041,719051,729000,322001,325001,259000,153100");
            Ac4.setSelected(false);
            txtAction4.setText("");
            return;
        }

        if (Ac4.isSelected()) {
            txtAction4.setEditable(true);
        } else {
            txtAction4.setEditable(false);
            txtAction4.setText("");
        }
    }//GEN-LAST:event_Ac4ActionPerformed

    private void Ac5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac5ActionPerformed
        // TODO add your handling code here:

        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac5.setSelected(false);
            return;
        }
        
        if (Ac4.isSelected()) {
            JOptionPane.showMessageDialog(this, "Decrease in GSM not allowed because Increase in GSM ");
            Ac5.setSelected(false);
            return;
        }

        String PRODUCT_CODE = DataModel_ExistPiece.getValueByVariable("PRODUCT", 0).substring(0, 6);
        System.out.println("PRODUCT CODE : " + PRODUCT_CODE);
        if ((!DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("WEAVING")) || PRODUCT_CODE.equals("719011") || PRODUCT_CODE.equals("719021") || PRODUCT_CODE.equals("719031") || PRODUCT_CODE.equals("719041") || PRODUCT_CODE.equals("719051") || PRODUCT_CODE.equals("729000") || PRODUCT_CODE.equals("322001") || PRODUCT_CODE.equals("325001") || PRODUCT_CODE.equals("259000") || PRODUCT_CODE.equals("153100")) {
            JOptionPane.showMessageDialog(null, "Decrease in GSM is only allowed except with Product Code : 719011,719021,719031,719041,719051,729000,322001,325001,259000,153100 and PIECE STAGE upto WEAVING");
            Ac5.setSelected(false);
            txtAction5.setText("");
            return;
        }

        if (Ac5.isSelected()) {
            txtAction5.setEditable(true);
        } else {
            txtAction5.setEditable(false);
            txtAction5.setText("");
        }
    }//GEN-LAST:event_Ac5ActionPerformed

    private void Ac6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac6ActionPerformed
        // TODO add your handling code here:

        
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac6.setSelected(false);
            return;
        }
        
        String PRODUCT_CODE = DataModel_ExistPiece.getValueByVariable("PRODUCT", 0).substring(0, 6);
        System.out.println("PRODUCT CODE : " + PRODUCT_CODE);
        if (!(PRODUCT_CODE.equals("719011") || PRODUCT_CODE.equals("719021") || PRODUCT_CODE.equals("719031") || PRODUCT_CODE.equals("719041") || PRODUCT_CODE.equals("719051") || PRODUCT_CODE.equals("322001") || PRODUCT_CODE.equals("325001"))) {
            JOptionPane.showMessageDialog(null, "Seaming required is only allowed with Product Code : 719011,719021,719031,719041,719051,322001,325001");
            Ac6.setSelected(false);
            txtAction6.setText("");
            return;
        }

        if (Ac6.isSelected()) {
            txtAction6.setEditable(true);
        } else {
            txtAction6.setEditable(false);
            txtAction6.setText("");
        }
    }//GEN-LAST:event_Ac6ActionPerformed

    private void Ac8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac8ActionPerformed
        // TODO add your handling code here:

        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac8.setSelected(false);
            return;
        }
        
        
        String PRODUCT_CODE = DataModel_ExistPiece.getValueByVariable("PRODUCT", 0).substring(0, 6);
        System.out.println("PRODUCT CODE : " + PRODUCT_CODE);
        if (!(PRODUCT_CODE.equals("153100") || PRODUCT_CODE.equals("155100") || PRODUCT_CODE.equals("259000") || PRODUCT_CODE.equals("259010") || PRODUCT_CODE.equals("269000") || PRODUCT_CODE.equals("269010") || PRODUCT_CODE.equals("719011") || PRODUCT_CODE.equals("719021") || PRODUCT_CODE.equals("719031") || PRODUCT_CODE.equals("719041") || PRODUCT_CODE.equals("719051"))) {
            JOptionPane.showMessageDialog(null, "Tagging in Product code is only allowed with Product Code : 153100,155100,259000,259010,269000,269010,719011,719021,719031,719041,719051");
            Ac8.setSelected(false);
            txtAction8.setText("");
            return;
        }

        if (Ac8.isSelected()) {
            txtAction8.setEditable(true);
        } else {
            txtAction8.setEditable(false);
            txtAction8.setText("");
        }
    }//GEN-LAST:event_Ac8ActionPerformed

    private void Ac9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac9ActionPerformed
        // TODO add your handling code here:
      
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac9.setSelected(false);
            return;
        }
        
        if (Ac9.isSelected()) {
            txtAction9.setEditable(true);
        } else {
            txtAction9.setEditable(false);
            txtAction9.setText("");
        }
    }//GEN-LAST:event_Ac9ActionPerformed

    private void txtAction2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAction2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAction2ActionPerformed

    private void Ac10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac10ActionPerformed
        // TODO add your handling code here:

        
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac10.setSelected(false);
            return;
        }
        
        if (Ac10.isSelected()) {
            txtAction10.setEditable(true);
        } else {
            txtAction10.setEditable(false);
            txtAction10.setText("");
        }
    }//GEN-LAST:event_Ac10ActionPerformed

    private void Ac11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac11ActionPerformed
        // TODO add your handling code here:
        
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac11.setSelected(false);
            return;
        }
        
        if (Ac11.isSelected()) {
            txtAction11.setEditable(true);
        } else {
            txtAction11.setEditable(false);
            txtAction11.setText("");
        }
    }//GEN-LAST:event_Ac11ActionPerformed

    private void Ac12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac12ActionPerformed
        // TODO add your handling code here:
        
        
       
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac12.setSelected(false);
            return;
        }
        
        if (Ac12.isSelected()) {
            txtAction12.setEditable(true);
        } else {
            txtAction12.setEditable(false);
            txtAction12.setText("");
        }
    }//GEN-LAST:event_Ac12ActionPerformed

    private void Ac13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac13ActionPerformed
        // TODO add your handling code here:
        
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac13.setSelected(false);
            return;
        }
        
        if (Ac13.isSelected()) {
            txtAction13.setEditable(true);
        } else {
            txtAction13.setEditable(false);
            txtAction13.setText("");
        }
    }//GEN-LAST:event_Ac13ActionPerformed

    private void Ac14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac14ActionPerformed
        // TODO add your handling code here:

        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac14.setSelected(false);
            return;
        }
        
        String PRODUCT_CODE = DataModel_ExistPiece.getValueByVariable("PRODUCT", 0).substring(0, 6);
        System.out.println("PRODUCT CODE : " + PRODUCT_CODE);
        if (PRODUCT_CODE.equals("719011") || PRODUCT_CODE.equals("719021") || PRODUCT_CODE.equals("719031") || PRODUCT_CODE.equals("719041") || PRODUCT_CODE.equals("719051") || PRODUCT_CODE.equals("729000")) {
            JOptionPane.showMessageDialog(null, "Tagging in Weight is only allowed with Product Code : 153100,155100,259000,259010,269000,269010,719011,719021,719031,719041,719051");
            Ac14.setSelected(false);
            txtAction14.setText("");
            return;
        }

        if (Ac14.isSelected()) {
            txtAction14.setEditable(true);
        } else {
            txtAction14.setEditable(false);
            txtAction14.setText("");
        }
    }//GEN-LAST:event_Ac14ActionPerformed

    private void Ac15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac15ActionPerformed
        // TODO add your handling code here:
        
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac15.setSelected(false);
            return;
        }
        
        if (Ac16.isSelected()) {
            JOptionPane.showMessageDialog(this, "'Trim to be retained by Production' not allowed because 'Trim to be returned to WH with felt' selected ");
            Ac15.setSelected(false);
            return;
        }
        
        if (Ac15.isSelected()) {
            txtAction15.setEditable(true);
            txtAction15.setText("Action");
        } else {
            txtAction15.setEditable(false);
            txtAction15.setText("");
        }
    }//GEN-LAST:event_Ac15ActionPerformed

    private void Ac16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac16ActionPerformed
        // TODO add your handling code here:
        
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac16.setSelected(false);
            return;
        }
        
        if (Ac15.isSelected()) {
            JOptionPane.showMessageDialog(this, "'Trim to be retained by Production' not allowed because 'Trim to be returned to WH with felt' selected ");
            Ac16.setSelected(false);
            return;
        }
        
        
        if (Ac16.isSelected()) {
            txtAction16.setEditable(true);
            txtAction16.setText("Action");
        } else {
            txtAction16.setEditable(false);
            txtAction16.setText("");
        }
    }//GEN-LAST:event_Ac16ActionPerformed

    private void Ac17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ac17ActionPerformed
        // TODO add your handling code here:
        
        if(AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "You cann't select because 'No Action' selected ");
            Ac17.setSelected(false);
            return;
        }
        
        if (Ac17.isSelected()) {
            txtAction17.setEditable(true);
        } else {
            txtAction17.setEditable(false);
            txtAction17.setText("");
        }
    }//GEN-LAST:event_Ac17ActionPerformed

    private void OpgFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpgFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpgFinalActionPerformed

    private void OpgRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpgRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpgRejectActionPerformed

    private void REFERENCEItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_REFERENCEItemStateChanged
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (REFERENCE.getSelectedItem().equals("P.O.")) {
            P_O_NO.setText("");
            P_O_DATE.setText("__/__/____");
            P_O_NO.setEditable(true);
            P_O_DATE.setEnabled(true);
        } else {
            P_O_NO.setText("");
            P_O_DATE.setText("00/00/0000");
            P_O_NO.setEditable(false);
            P_O_DATE.setEnabled(false);
        }
    }//GEN-LAST:event_REFERENCEItemStateChanged

    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased
        // TODO add your handling code here:
        float length = Float.parseFloat(DataModel.getValueByVariable("PR_BILL_LENGTH", 0));
        float width = Float.parseFloat(DataModel.getValueByVariable("PR_BILL_WIDTH", 0));
        String BILL_PRODUCT_CODE = DataModel.getValueByVariable("PR_BILL_PRODUCT_CODE", 0);
        float THORTICAL_WEIGHT = Float.parseFloat(DataModel.getValueByVariable("THORTICAL_WEIGHT", 0));
        float SQMT = (length * width);
        SQMT = Float.parseFloat(EITLERPGLOBAL.round(SQMT,2)+"");
        DataModel.setValueByVariable("PR_BILL_SQMTR", SQMT+"", 0);

        System.out.println("SQMTR : "+SQMT);
        FeltInvCalc inv_calc = new FeltInvCalc();

        try {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            inv_calc = clsOrderValueCalc.calculate_forDiversion(THORTICAL_WEIGHT,"", BILL_PRODUCT_CODE, PARTY_CODE.getText(), length, width, Float.parseFloat(DataModel.getValueByVariable("PR_BILL_WEIGHT", 0)), SQMT, df1.format(df.parse(S_O_DATE.getText())));
            //calculate(String Piece_No,String Product_Code,String Party_Code,Float Length,Float Width,Integer GSM,Float Weight,Float SQMT,String Order_Date)
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        DataModel.setValueByVariable("BASE_ORDER_AMT", EITLERPGLOBAL.round(inv_calc.getFicBasAmount(),2)+"", 0);
        
        DataModel.setValueByVariable("OV_RATE", inv_calc.getFicRate() + "", 0);
        
        DataModel.setValueByVariable("SURCHARGE_PER", inv_calc.getFicSurcharge_per() + "", 0);
        DataModel.setValueByVariable("SURCHARGE_RATE", inv_calc.getFicSurcharge_rate() + "", 0);
        DataModel.setValueByVariable("GROSS_RATE", inv_calc.getFicGrossRate() + "", 0);
        
        float existing_base_amt = Float.parseFloat(DataModel.getValueByVariable("BASE_EXISTING_PIECE_AMT", 0));
        float difference = existing_base_amt - inv_calc.getFicBasAmount();
        float discount = 0;
        //PARTY_CODE
        String ExistingPartyCode = DataModel_ExistPiece.getValueByVariable("PARTY_CODE", 0);
        
        String ExistingPieceNo = DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0).trim();
        String Product_Code_existing = DataModel_ExistPiece.getValueByVariable("PRODUCT", 0);
        
        if (Product_Code_existing.equals("729000") || Product_Code_existing.equals("7290000")) {
            System.out.println("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + inv_calc.getFicPartyCode() + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + inv_calc.getFicProductCode() + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO = '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM,MASTER_NO DESC");
            String Disc_Per = data.getStringValueFromDB("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + inv_calc.getFicPartyCode() + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + inv_calc.getFicProductCode() + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO = '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM,MASTER_NO DESC");
            float d_per = 0;
            try{
                   d_per = Float.parseFloat(Disc_Per);
            }catch(Exception e)
            {
                System.out.println("Error on getting disc");
                //e.printStackTrace();
            }
            try{
                if(d_per==0)
                {
                        String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
                        //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
                        float disc1 = 0;

                        if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + inv_calc.getFicProductCode() + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                                try {
                                        Connection Conn;
                                        Statement stmt;
                                        ResultSet rsData;

                                        Conn = data.getConn();
                                        stmt = Conn.createStatement();
                                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\" or EFFECTIVE_TO IS NULL)");
                                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                                        //System.out.println("* Query SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
                                        System.out.println("desic1 : SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + inv_calc.getFicProductCode() + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + inv_calc.getFicProductCode() + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                                        rsData.first();
                                        disc1 = rsData.getFloat(1);

                                //System.out.println("Discount Percentage : "+disc);
                                } catch (Exception e) {
                                disc1 = 0;
                                System.out.println("Error on Group Discount: " + e.getMessage());
                                }
                        }
                        d_per = disc1;
                } 
            }catch(Exception e)
            {
            }
            
            discount = (inv_calc.getFicBasAmount() * d_per) / 100;

            discount = Math.round(discount);

            DataModel.setValueByVariable("DISCOUNT_PER", EITLERPGLOBAL.round(d_per,2)+"", 0);
            DataModel.setValueByVariable("DISCOUNT_AMT", discount + "", 0);

            difference = difference - discount;
            
            
            //
            //start
            String Disc_Per_existingparty = data.getStringValueFromDB("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + ExistingPartyCode + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code_existing + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO = '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM,MASTER_NO DESC");
            
            float d_per_existingparty = 0;
            try{
                   d_per_existingparty = Float.parseFloat(Disc_Per_existingparty);
            }catch(Exception e)
            {
               // e.printStackTrace();
            }
            float discount_existingparty = (existing_base_amt * d_per_existingparty) / 100;

            //10 July 2019
            String Disc_Per_existingparty_pieceno = data.getStringValueFromDB("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PIECE_NO ='" + ExistingPieceNo + "'  AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' or EFFECTIVE_TO = '0000-00-00'  or EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM,MASTER_NO DESC");
            float d_per_existingparty_pieceno = 0;
            try{
                   d_per_existingparty_pieceno = Float.parseFloat(Disc_Per_existingparty_pieceno);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            float discount_existingparty_pieceno = (existing_base_amt * d_per_existingparty_pieceno) / 100;

            if(discount_existingparty_pieceno>discount_existingparty)
            {
                discount_existingparty = discount_existingparty_pieceno;
            }
            
            discount_existingparty = Math.round(discount_existingparty);
            System.out.println("123 :"+discount_existingparty);    
            //end
            //
            System.out.println("Disc Per  "+Disc_Per_existingparty);
            System.out.println("Diff "+difference);
            System.out.println(" discount : "+discount);
            System.out.println(" discount exist : "+discount_existingparty);
            
            
            
                float d_per_existingGroupParty = 0;
                String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + ExistingPartyCode + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
                //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
                if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + ExistingPartyCode + " AND PIECE_NO='" + ExistingPieceNo + "' AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code_existing + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                        try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\" or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                            System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code_existing + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code_existing + " AND EFFECTIVE_FROM <= '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO >= '" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO = '0000-00-00' OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        d_per_existingGroupParty = rsData.getFloat(1);

                        //System.out.println("Discount Percentage : "+disc);
                        } catch (Exception e) {
                        d_per_existingGroupParty = 0;

                        System.out.println("Error on Group Discount: " + e.getMessage());
                        }
                    }
                }

                float discount_existingGroupParty = (existing_base_amt * d_per_existingGroupParty) / 100;

                if(discount_existingGroupParty>discount_existingparty_pieceno && discount_existingGroupParty>discount_existingparty)
                {
                    discount_existingparty = discount_existingGroupParty;
                }

            
            float exist_value = (existing_base_amt - discount_existingparty);
            
            System.out.println("Exist value : "+exist_value);
            System.out.println("Base Amt  "+inv_calc.getFicBasAmount());
            System.out.println("Discount "+discount);
            System.out.println("diff = exist_value - inv_calc.getFicBasAmount()-discount");
            System.out.println(":: "+inv_calc.getFicBasAmount()+" - "+discount);
            float val1 = inv_calc.getFicBasAmount()-discount;
            difference = exist_value - val1;
            System.out.println("diff : exist_value - :: val1");
            System.out.println(""+exist_value+" - "+val1);
            System.out.println("VALUE = "+difference);
            
        } else {
            DataModel.setValueByVariable("DISCOUNT_PER", "NA", 0);
            DataModel.setValueByVariable("DISCOUNT_AMT", "NA", 0);
        }

        difference = Math.round(difference);
        
        float WIDTH=0;
        
//        String existing_stage = DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0);
//        if ("IN STOCK".equals(existing_stage) || "STOCK".equals(existing_stage)) {
//            
//            WIDTH = Float.parseFloat(DataModel_ExistPiece.getValueByVariable("ACTUAL_WIDTH", 0));  
//        } else {
            
           // WIDTH = Float.parseFloat(DataModel_ExistPiece.getValueByVariable("WIDTH", 0)); 
            WIDTH = Float.parseFloat(DataModel_ExistPiece.getValueAt(0, 11).toString()); 
        //}
        
        
        try{
                if (Product_Code_existing.equals("719011") || Product_Code_existing.equals("7190110") ||
                    Product_Code_existing.equals("719021") || Product_Code_existing.equals("7190210") ||
                    Product_Code_existing.equals("719031") || Product_Code_existing.equals("7190310") ||
                    Product_Code_existing.equals("719041") || Product_Code_existing.equals("7190410") ||
                    Product_Code_existing.equals("719051") || Product_Code_existing.equals("7190510"))
                {
                    WIDTH = Float.parseFloat(DataModel_ExistPiece.getValueByVariable("BILL_WIDTH", 0));
                    
                    double SEAM_CHARGE = 0;
                    double seam_width =  WIDTH - inv_calc.getFicWidth();

                    SEAM_CHARGE = seam_width * 4899;

                    //OV_SEAM_CHG
                    DataModel.setValueByVariable("OV_SEAM_CHG", EITLERPGLOBAL.round(SEAM_CHARGE,2) + "", 0);

                    if(!PR_PIECE_STAGE.equals("SEAMING"))
                    {
                         difference = difference + Float.parseFloat(EITLERPGLOBAL.round(SEAM_CHARGE,2)+"");
                    }
                    
                    //difference = difference + Float.parseFloat(EITLERPGLOBAL.round(SEAM_CHARGE,2)+"");
                }
                else
                {
                    DataModel.setValueByVariable("OV_SEAM_CHG", "0", 0);
                }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
        DataModel.setValueByVariable("DIFFERENCE_AMT", EITLERPGLOBAL.round(difference,0)+"", 0);
        
        if(difference<=0)
        {
            btnCompany.setSelected(false);
            btnParty.setSelected(false);
            jPanel4.setEnabled(false);
            DataModel.setValueByVariable("DB_NOTE", "NA", 0);
            DataModel.setValueByVariable("DB_AMT", "NA", 0);
            txtPartyBearer.setText("");
            txtPartyBearerName.setText("");
        }
        else
        {
            if(DataModel.getValueByVariable("DB_NOTE", 0).equals("NA"))
            {
                    DataModel.setValueByVariable("DB_NOTE", "", 0);
                    DataModel.setValueByVariable("DB_AMT", "", 0);
            }
            jPanel4.setEnabled(true);
        }
        findAmount();
        //filterHierarchyCombo();
        String Req_Month = DataModel.getValueByVariable("REQ_MONTH", 0);
        //JOptionPane.showMessageDialog(null, "ReqMonth = "+Req_Month);
        DataModel.setValueByVariable("OC_MONTHYEAR", Req_Month, 0);
    }//GEN-LAST:event_TableKeyReleased

    private void AcNoActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcNoActionActionPerformed
        // TODO add your handling code here:
        double profit_loss = Double.parseDouble(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
        if(profit_loss == 0)
        {
            
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Profit or Loss is : "+profit_loss+", So you can not select 'NO ACTION'");
            AcNoAction.setSelected(false);
            return;
        }
        
        
        Ac1.setSelected(false);
        Ac2.setSelected(false);
        Ac3.setSelected(false);
        Ac4.setSelected(false);
        Ac5.setSelected(false);
        Ac6.setSelected(false);
        Ac7.setSelected(false);
        Ac8.setSelected(false);
        Ac9.setSelected(false);
        Ac10.setSelected(false);
        Ac11.setSelected(false);
        Ac12.setSelected(false);
        Ac13.setSelected(false);
        Ac14.setSelected(false);
        Ac15.setSelected(false);
        Ac16.setSelected(false);
        Ac17.setSelected(false);
        
        txtAction1.setEditable(false);
        txtAction1.setText("");
        
        txtAction2.setEditable(false);
        txtAction2.setText("");
        
        txtAction3.setEditable(false);
        txtAction3.setText("");
        
        txtAction4.setEditable(false);
        txtAction4.setText("");
        
        txtAction5.setEditable(false);
        txtAction5.setText("");
        
        txtAction6.setEditable(false);
        txtAction6.setText("");
        
        txtAction7.setEditable(false);
        txtAction7.setText("");
        
        txtAction8.setEditable(false);
        txtAction8.setText("");
        
        txtAction9.setEditable(false);
        txtAction9.setText("");
        
        txtAction10.setEditable(false);
        txtAction10.setText("");
        
        txtAction11.setEditable(false);
        txtAction11.setText("");
        
        txtAction12.setEditable(false);
        txtAction12.setText("");
        
        txtAction13.setEditable(false);
        txtAction13.setText("");
        
        txtAction14.setEditable(false);
        txtAction14.setText("");
        
        txtAction15.setEditable(false);
        txtAction15.setText("");
        
        txtAction16.setEditable(false);
        txtAction16.setText("");
        
        txtAction17.setEditable(false);
        txtAction17.setText("");
        
    }//GEN-LAST:event_AcNoActionActionPerformed

    private void DebitMemoNo2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DebitMemoNo2KeyPressed
        // TODO add your handling code here:
       
        
    }//GEN-LAST:event_DebitMemoNo2KeyPressed

    private void Select2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Select2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Select2KeyPressed

    private void Select2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select2ActionPerformed
        // TODO add your handling code here:
            if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo2.setText(aList.ReturnVal);
                    DebitMemoAmt2.setText(Amount);
                 }
    }//GEN-LAST:event_Select2ActionPerformed

    private void Show_Memo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo2ActionPerformed
        if (DebitMemoNo2.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo2.getText());
        }
    }//GEN-LAST:event_Show_Memo2ActionPerformed

    private void Show_Memo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo3ActionPerformed
        // TODO add your handling code here:
        if (DebitMemoNo3.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo3.getText());
        }
    }//GEN-LAST:event_Show_Memo3ActionPerformed

    private void Show_Memo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo4ActionPerformed
        // TODO add your handling code here:
        if (DebitMemoNo4.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo4.getText());
        }
    }//GEN-LAST:event_Show_Memo4ActionPerformed

    private void Show_Memo5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo5ActionPerformed
        // TODO add your handling code here:
        if (DebitMemoNo5.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo5.getText());
        }
    }//GEN-LAST:event_Show_Memo5ActionPerformed

    private void Show_Memo6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo6ActionPerformed
        // TODO add your handling code here:
        if (DebitMemoNo6.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo6.getText());
        }
    }//GEN-LAST:event_Show_Memo6ActionPerformed

    private void Show_Memo7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo7ActionPerformed
        // TODO add your handling code here:
        if (DebitMemoNo7.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo7.getText());
        }
    }//GEN-LAST:event_Show_Memo7ActionPerformed

    private void Show_Memo8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo8ActionPerformed
        // TODO add your handling code here:
        if (DebitMemoNo8.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo8.getText());
        }
    }//GEN-LAST:event_Show_Memo8ActionPerformed

    private void Show_Memo9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo9ActionPerformed
        // TODO add your handling code here:
        if (DebitMemoNo9.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo9.getText());
        }
    }//GEN-LAST:event_Show_Memo9ActionPerformed

    private void Show_Memo10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_Memo10ActionPerformed
        // TODO add your handling code here:
        if (DebitMemoNo10.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debit Memo Not Selected");
        } 
        else {                    
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(DebitMemoNo10.getText());
        }
    }//GEN-LAST:event_Show_Memo10ActionPerformed

    private void Select3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select3ActionPerformed
        // TODO add your handling code here:
        if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo3.setText(aList.ReturnVal);
                    DebitMemoAmt3.setText(Amount);
                 }
    }//GEN-LAST:event_Select3ActionPerformed

    private void Select4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select4ActionPerformed
        // TODO add your handling code here:
        if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo4.setText(aList.ReturnVal);
                    DebitMemoAmt4.setText(Amount);
                 }
    }//GEN-LAST:event_Select4ActionPerformed

    private void Select5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select5ActionPerformed
        // TODO add your handling code here:
        if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo5.setText(aList.ReturnVal);
                    DebitMemoAmt5.setText(Amount);
                 }
    }//GEN-LAST:event_Select5ActionPerformed

    private void Select6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select6ActionPerformed
        // TODO add your handling code here:
        if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo6.setText(aList.ReturnVal);
                    DebitMemoAmt6.setText(Amount);
                 }
    }//GEN-LAST:event_Select6ActionPerformed

    private void Select7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select7ActionPerformed
        // TODO add your handling code here:
        if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo7.setText(aList.ReturnVal);
                    DebitMemoAmt7.setText(Amount);
                 }
    }//GEN-LAST:event_Select7ActionPerformed

    private void Select8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select8ActionPerformed
        // TODO add your handling code here:
        if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo8.setText(aList.ReturnVal);
                    DebitMemoAmt8.setText(Amount);
                 }
    }//GEN-LAST:event_Select8ActionPerformed

    private void Select9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select9ActionPerformed
        // TODO add your handling code here:
        if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo9.setText(aList.ReturnVal);
                    DebitMemoAmt9.setText(Amount);
                 }
    }//GEN-LAST:event_Select9ActionPerformed

    private void Select10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select10ActionPerformed
        // TODO add your handling code here:
        if (txtPartyBearer.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select Party");
                    txtPartyBearer.requestFocus();
                    return;
                }

                LOV aList = new LOV();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -90);
                String lastDate = dateForDB.format(cal.getTime());
                String str = "SELECT DEBITMEMO_NO,DEBITMEMO_DATE,INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DB_PARTY_CODE = '" + txtPartyBearer.getText() + "' and DEBITMEMO_TYPE='MANUAL' and DEBITMEMO_DATE>='"+lastDate+"' and APPROVED=1;";
                aList.SQL = str;
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String Amount = data.getStringValueFromDB("SELECT INTEREST_AMT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where  DEBITMEMO_NO = '" + aList.ReturnVal + "' AND APPROVED=1");
                    DebitMemoNo10.setText(aList.ReturnVal);
                    DebitMemoAmt10.setText(Amount);
                 }
    }//GEN-LAST:event_Select10ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        if(!DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("WEAVING") ||
                !DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("MENDING") ||
                !DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("NEEDLING") ||
                !DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0).equals("FINISHING"))
        {
//            AppletFrame aFrame = new AppletFrame("FELT FINISHING REPORT");
//            aFrame.startAppletEx("EITLERP.FeltSales.FeltFinishing.frmFeltFinishingReport", "FELT FINISHING REPORT");
//            frmFeltFinishingReport ObjItem = (frmFeltFinishingReport) aFrame.ObjApplet;
//           // ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText());
//            //PR_FNSG_DATE
//            String Piece_No = DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0);
//            String FINISHING_DATE = EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT PR_FNSG_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+Piece_No+"'"));
//            
//            ObjItem.requestFocus();
//            ObjItem.setFromToDate(FINISHING_DATE, FINISHING_DATE);
//            // ObjItem.AddFromOrder(PARTY_CODE.getText(),Piecedetail[0]);
//            // ObjItem.GenerateReport();
            
            AppletFrame aFrame = new AppletFrame("FELT FINISHING");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltFinishing.frmFeltFinishing", "FELT FINISHING");
            frmFeltFinishing ObjItem = (frmFeltFinishing) aFrame.ObjApplet;
           // ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText());
            //PR_FNSG_DATE
            String Piece_No = DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0);
            //String FINISHING_DATE = EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT PR_FNSG_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+Piece_No+"'"));
            
            ObjItem.requestFocus();
            ObjItem.FindByPiece(Piece_No);
            //ObjItem.setFromToDate(FINISHING_DATE, FINISHING_DATE);
            // ObjItem.AddFromOrder(PARTY_CODE.getText(),Piecedetail[0]);
            // ObjItem.GenerateReport();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        PreviewReport();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnOpenPriorApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenPriorApprovalActionPerformed
        // TODO add your handling code here:
        
        if(!lblPriorApprovalNo.getText().equals(""))
        {
            AppletFrame aFrame = new AppletFrame("Prior Approval");
            aFrame.startAppletEx("EITLERP.FeltSales.DiversionLoss.FrmFeltDiversionLoss", "Prior Approval");
            FrmFeltDiversionLoss ObjItem = (FrmFeltDiversionLoss) aFrame.ObjApplet;
            ObjItem.requestFocus();
            ObjItem.Find(lblPriorApprovalNo.getText());
        }
        
    }//GEN-LAST:event_btnOpenPriorApprovalActionPerformed

    private void btnPartialPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartialPaymentActionPerformed
        // TODO add your handling code here:
        if(EditMode == EITLERPGLOBAL.EDIT || EditMode == EITLERPGLOBAL.ADD)
        {
            if (btnPartialPayment.isSelected()) {
                    txtSDMLPer.setText("100");
                    txtPartyPer.setText("0");
                    txtPartyBearer.setText("");
                    txtPartyBearerName.setText("");
                    
                    txtPartyPer.setEnabled(true);
                    txtPartyBearer.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnPartialPaymentActionPerformed
    private void findAmount()
    {
        try{
            
            double party_per = Double.parseDouble(txtPartyPer.getText());
            double company_per = Double.parseDouble(txtSDMLPer.getText());
            
            double profit_loss = Double.parseDouble(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
            
            double party_amt = (profit_loss * party_per) / 100;
            double company_amt = (profit_loss * company_per) /100;
            
            txtBearCompanyAmt.setText(company_amt+"");
            txtBearPartyAmt.setText(party_amt+"");
            
        }catch(Exception e)
        {
            //e.printStackTrace();
        }
    }
    private void txtPartyPerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyPerKeyReleased
        // TODO add your handling code here:
        if(!txtPartyPer.getText().equals(""))
        {
            if(EditMode == EITLERPGLOBAL.EDIT || EditMode == EITLERPGLOBAL.ADD)
            {
                double party_per = Double.parseDouble(txtPartyPer.getText());
                double company_per = 100 - party_per;
                txtSDMLPer.setText(company_per+"");
                
                findAmount();
            }
        }
    }//GEN-LAST:event_txtPartyPerKeyReleased

    private void TableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_TableKeyTyped

    private void txtContactPersonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContactPersonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactPersonActionPerformed

    private void txtEmailIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailIdActionPerformed

    private void chbEmailUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbEmailUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chbEmailUpdateActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:
                
            
    }//GEN-LAST:event_TableMouseClicked
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
        aList.FirstFreeNo = 204;
        S_O_DATE.setText(df.format(new Date()));
        FFNo = aList.FirstFreeNo;
        // JOptionPane.showMessageDialog(null, "FFNO : "+EITLERPGLOBAL.gCompanyID+","+ ModuleId+","+ FFNo+","+  false);
        S_O_NO.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, false));
        lblTitle.setText("Felt Sales Order Diversion - " + S_O_NO.getText());
        btnCompany.doClick();
    }

    private void Save() {

        if (Table.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(this, "Enter Piece Updation Details Before Saving.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(data.IsRecordExist("SELECT H.PC_DOC_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D\n" +
            "where H.CANCELED=0 AND H.PC_DOC_NO=D.PC_DOC_NO AND D.PIECE_NO='"+txtPieceNo.getText()+"'") && !OpgReject.isSelected())
        {
            String DocNo = data.getStringValueFromDB("SELECT H.PC_DOC_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D\n" +
            "where H.CANCELED=0 AND H.PC_DOC_NO=D.PC_DOC_NO AND D.PIECE_NO='"+txtPieceNo.getText()+"'");
            JOptionPane.showMessageDialog(this, "Piece "+txtPieceNo.getText()+" is exist in Clubbing "+DocNo+", So you can not divert this piece.");
            return;
        }
        
        if(data.IsRecordExist("SELECT H.PC_DOC_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D\n" +
            "where H.CANCELED=0 AND H.PC_DOC_NO=D.PC_DOC_NO AND D.PIECE_NO='"+txtPieceNo.getText()+"'") && OpgApprove.isSelected())
        {
            String DocNo = data.getStringValueFromDB("SELECT H.PC_DOC_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D\n" +
            "where H.CANCELED=0 AND H.PC_DOC_NO=D.PC_DOC_NO AND D.PIECE_NO='"+txtPieceNo.getText()+"'");
            JOptionPane.showMessageDialog(this, "Piece "+txtPieceNo.getText()+" is exist in Clubbing "+DocNo+", So you can not divert this piece.");
            return;
        }
        
        if (DataModel.getValueByVariable("DB_NOTE", 0).equals("") && (OpgApprove.isSelected() || OpgFinal.isSelected())) {
            JOptionPane.showMessageDialog(this, "Enter Piece Debit Note No to forward or final approve", "ERROR", JOptionPane.ERROR_MESSAGE);
            Table.requestFocus();
            Table.editCellAt(0, 37);
            return;
        }

        if (DataModel.getValueByVariable("DB_AMT", 0).equals("")  && (OpgApprove.isSelected() || OpgFinal.isSelected())) {
            JOptionPane.showMessageDialog(this, "Enter Piece Debit Note Amount  to forward or final approve", "ERROR", JOptionPane.ERROR_MESSAGE);
            Table.requestFocus();
            Table.editCellAt(0, 38);
            return;
        }

        if (PARTY_CODE.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter Valid PARTY CODE.", "ERROR", JOptionPane.ERROR_MESSAGE);
            PARTY_CODE.requestFocus();
            return;
        }
        
        String check_lock = data.getStringValueFromDB("SELECT COALESCE(PARTY_LOCK, 0) AS PARTY_LOCK FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='"+PARTY_CODE.getText()+"'");
        if("1".equals(check_lock))
        {
            JOptionPane.showMessageDialog(null, "Party Code : "+PARTY_CODE.getText()+" is locked.");
            PARTY_CODE.requestFocus();
            return;
        }

        if (P_O_DATE.getText().equals("__/__/____") || EITLERPGLOBAL.formatDateDB(P_O_DATE.getText()) == null) {
            P_O_DATE.setText("00/00/0000");
        }

        if (REF_DATE.getText().equals("__/__/____")) {
            JOptionPane.showMessageDialog(this, "Enter Reference Date.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (EITLERPGLOBAL.formatDateDB(REF_DATE.getText()) == null) {
            JOptionPane.showMessageDialog(this, "Enter Valid Reference Date.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (REFERENCE.getSelectedItem().equals("P.O.") && P_O_NO.getText().equals("") && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "Enter P O NO.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (REFERENCE.getSelectedItem().equals("P.O.") && EITLERPGLOBAL.formatDateDB(P_O_DATE.getText()).equals("") && !OpgReject.isSelected()) {
            JOptionPane.showMessageDialog(this, "Enter P O DATE.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(txtEmailId.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Enter Email Id.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //DataModel.setValueByVariable("REQ_MONTH", "", 0);
        /*if (DataModel.getValueByVariable("REQ_MONTH", 0).equals("")  && (OpgApprove.isSelected() || OpgFinal.isSelected())) {
            JOptionPane.showMessageDialog(this, "Enter Select Requested Month to forward or final approve", "ERROR", JOptionPane.ERROR_MESSAGE);
            Table.requestFocus();
            Table.editCellAt(0, DataModel.getColFromVariable("REQ_MONTH"));
            return;
        }*/
        
        //DataModel.setValueByVariable("REQ_MONTH", "", 0);
        if (DataModel.getValueByVariable("OC_MONTHYEAR", 0).equals("")  && (OpgApprove.isSelected() || OpgFinal.isSelected())) {
            JOptionPane.showMessageDialog(this, "Enter Select OC Month to forward or final approve", "ERROR", JOptionPane.ERROR_MESSAGE);
            Table.requestFocus();
            Table.editCellAt(0, DataModel.getColFromVariable("OC_MONTHYEAR"));
            return;
        }
        
        
        String MachineNo = DataModel_ExistPiece.getValueByVariable("MACHINE_NO", 0);
        String POSITION = DataModel_ExistPiece.getValueByVariable("POSITION", 0);
        String PARTY_CODE_EXISTING = DataModel_ExistPiece.getValueByVariable("PARTY_CODE", 0);
        
        String check_lock_MACHINE = data.getStringValueFromDB("SELECT COALESCE(MACHINE_LOCK_IND, 0) AS PARTY_LOCK FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MM_PARTY_CODE='"+PARTY_CODE_EXISTING+"' AND MM_MACHINE_NO='"+MachineNo+"'");
        if("1".equals(check_lock_MACHINE))
        {
            JOptionPane.showMessageDialog(null, "Machine : "+MachineNo+" is locked for Party Code : "+PARTY_CODE_EXISTING+".");
            PARTY_CODE.requestFocus();
            return;
        }
        String check_lock_POSITION = data.getStringValueFromDB("SELECT COALESCE(POSITION_LOCK_IND, 0) AS POSITION_LOCK_IND FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='"+PARTY_CODE_EXISTING+"' AND MM_MACHINE_NO='"+MachineNo+"' AND MM_MACHINE_POSITION='"+POSITION+"'");
        if("1".equals(check_lock_POSITION))
        {
            JOptionPane.showMessageDialog(null, "Machine : "+MachineNo+" AND Position : "+POSITION+" is locked for Party Code : "+PARTY_CODE_EXISTING+".");
            PARTY_CODE.requestFocus();
            return;
        }
        
        MachineNo = DataModel.getValueByVariable("MACHINE_NO", 0);
        POSITION = DataModel.getValueByVariable("POSITION", 0);
            
        check_lock_MACHINE = data.getStringValueFromDB("SELECT COALESCE(MACHINE_LOCK_IND, 0) AS PARTY_LOCK FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MM_PARTY_CODE='"+PARTY_CODE.getText()+"' AND MM_MACHINE_NO='"+MachineNo+"'");
        if("1".equals(check_lock_MACHINE))
        {
            JOptionPane.showMessageDialog(null, "Machine : "+MachineNo+" is locked for Party Code : "+PARTY_CODE.getText()+".");
            PARTY_CODE.requestFocus();
            return;
        }
        check_lock_POSITION = data.getStringValueFromDB("SELECT COALESCE(POSITION_LOCK_IND, 0) AS POSITION_LOCK_IND FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_PARTY_CODE='"+PARTY_CODE.getText()+"' AND MM_MACHINE_NO='"+MachineNo+"' AND MM_MACHINE_POSITION='"+POSITION+"'");
        if("1".equals(check_lock_POSITION))
        {
            JOptionPane.showMessageDialog(null, "Machine : "+MachineNo+" AND Position : "+POSITION+" is locked for Party Code : "+PARTY_CODE.getText()+".");
            PARTY_CODE.requestFocus();
            return;
        }
        
        
        if (Ac1.isSelected() && ltrim(txtAction1.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction1.requestFocus();
            return;
        }

        if (Ac2.isSelected() && ltrim(txtAction2.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction2.requestFocus();
            return;
        }

        if (Ac3.isSelected() && ltrim(txtAction3.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction3.requestFocus();
            return;
        }

        if (Ac4.isSelected() && ltrim(txtAction4.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction4.requestFocus();
            return;
        }

        if (Ac5.isSelected() && ltrim(txtAction5.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction5.requestFocus();
            return;
        }

        if (Ac6.isSelected() && ltrim(txtAction6.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction6.requestFocus();
            return;
        }

        if (Ac7.isSelected() && ltrim(txtAction7.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction7.requestFocus();
            return;
        }

        if (Ac8.isSelected() && ltrim(txtAction8.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction8.requestFocus();
            return;
        }

        if (Ac9.isSelected() && ltrim(txtAction9.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction9.requestFocus();
            return;
        }

        if (Ac10.isSelected() && ltrim(txtAction10.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction10.requestFocus();
            return;
        }

        if (Ac11.isSelected() && ltrim(txtAction11.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction11.requestFocus();
            return;
        }

        if (Ac12.isSelected() && ltrim(txtAction12.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction12.requestFocus();
            return;
        }

        if (Ac13.isSelected() && ltrim(txtAction13.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction13.requestFocus();
            return;
        }

        if (Ac14.isSelected() && ltrim(txtAction14.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction14.requestFocus();
            return;
        }

        if (Ac15.isSelected() && ltrim(txtAction15.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction15.requestFocus();
            return;
        }

        if (Ac16.isSelected() && ltrim(txtAction16.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction16.requestFocus();
            return;
        }

        if (Ac17.isSelected() && ltrim(txtAction17.getText()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter remark for Action");
            txtAction17.requestFocus();
            return;
        }

        
        if(!Ac1.isSelected() && !Ac2.isSelected() && !Ac3.isSelected() && !Ac4.isSelected() && !Ac5.isSelected() && !Ac6.isSelected() && !Ac7.isSelected() && !Ac8.isSelected() && !Ac9.isSelected() && !Ac10.isSelected() && !Ac11.isSelected() && !Ac12.isSelected() && !Ac13.isSelected() && !Ac14.isSelected() && !Ac15.isSelected() && !Ac16.isSelected() && !Ac17.isSelected() && !AcNoAction.isSelected())
        {
            JOptionPane.showMessageDialog(this, "Select any Action or select 'No Action' option");
            return;
        }
        
        if(OpgFinal.isSelected())
        {
            
            String Stage = DataModel_ExistPiece.getValueByVariable("PIECE_STATUS", 0);
            //'SEAMING',
            if(Stage.equals("SEAMING") || Stage.equals("SPLICING") || Stage.equals("FINISHING") || Stage.equals("NEEDLING") || Stage.equals("MENDING") || Stage.equals("WARPING") || Stage.equals("WEAVING") || Stage.equals("STOCK") || Stage.equals("IN STOCK"))
            {
                System.out.println("Final Approval Ready...!");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Piece can not divert in Stage "+Stage+" ");
                return;
            }
            // END STEP
            
            
            //19-Oct-2018 - Aditya sir is compulsory for Loss, Now Prior Approval for Diversion
            //STEP 1
            /*
            System.out.println("Selected Hierarchy  : "+SelHierarchyID+" User Id : "+EITLERPGLOBAL.gNewUserID);
            if(EITLERPGLOBAL.gNewUserID != 243 && EITLERPGLOBAL.gNewUserID !=262)
            {
                HashMap hmSendToList = new HashMap();
                hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID,true);
                boolean Adityabhai_exist = false;

                for (int i = 1; i <= hmSendToList.size(); i++) {
                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));

                    int user_id = (int) ObjUser.getAttribute("USER_ID").getVal();
                    System.out.println("User Id checked : "+user_id);
                    if(user_id==243 || user_id==262)
                    {
                        System.out.println("ABP Exist : User Id = "+user_id);
                        Adityabhai_exist=true;
                    }
                }

                double profit_loss =Double.parseDouble(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
                System.out.println("Profit(-) & Loss(+) : "+profit_loss+" Exist : "+Adityabhai_exist);
                if(!Adityabhai_exist && profit_loss > 0)
                {
                    System.out.println("ABP Exist & Loss, document can not final approve ");
                    JOptionPane.showMessageDialog(null, "Diversion is in Loss, and Aditya Sir is not included in Hierarchy. So Diversion will not Final Approve.");
                    return;
                }
            }
            */        
            //STEP1 END
        }
        
        /* As per Chande Request on date 04/04/2019
        try{
                    
            String Piece = data.getStringValueFromDB("SELECT PIECE_NO FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PIECE_NO='"+DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0)+"'");
        
            if(!Piece.equals(""))
            {
                JOptionPane.showMessageDialog(this, "This Piece :  "+Piece+" is on Anual Order Incentive, So You can not divert it. ");
                return;
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        */
        
        if((OpgApprove.isSelected() || OpgFinal.isSelected()) && btnCompany.isSelected() && lblPriorApprovalNo.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "This Piece : "+DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0)+" is not Prior Approved, So You can not divert it with 'Cost Bearer By Company'. ");
            return;
        }
        
        try{
            
            double profit_loss =Double.parseDouble(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
            System.out.println("Profit(-) & Loss(+) : "+profit_loss+" Exist : ");
            if((profit_loss > 0 && (btnCompany.isSelected() || btnPartialPayment.isSelected()) && lblPriorApprovalNo.getText().equals("")) && !OpgHold.isSelected())
            {
                System.out.println("Prior Approval is compulsory for Loss");
                JOptionPane.showMessageDialog(null, "Diversion is in Loss, Prior Approval is compulsory for Loss.");
                return;
            }
            
            if((!lblPriorApprovalNo.getText().equals("")) && !OpgHold.isSelected())
            {
                if(btnCompany.isSelected() || btnPartialPayment.isSelected())
                {
                     String Prior_Approval_Amt = data.getStringValueFromDB("SELECT COST_BY_COMPANY_AMT FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL where SD_ORDER_NO='"+lblPriorApprovalNo.getText()+"'");
                     int CompanyAmt = Integer.parseInt(txtBearCompanyAmt.getText());
                     CompanyAmt = (CompanyAmt * (-1));
                     int PriorCompnyAmt = Integer.parseInt(Prior_Approval_Amt);
//                     if(CompanyAmt!=PriorCompnyAmt)
//                     {
//                         JOptionPane.showMessageDialog(null, "For Bear by Company, Prior Approved Amt and bear by company amt should be same.");
//                         return; 
//                     }
                     int diff = CompanyAmt - PriorCompnyAmt;
                     if(diff==1 || diff ==0 || diff==-1)
                     {
                         
                     }
                     else
                     {
                         JOptionPane.showMessageDialog(null, "For Bear by Company, Prior Approved Amt and bear by company amt should be same.");
                         return; 
                     }
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
        if(EditMode==EITLERPGLOBAL.ADD)
        {
            double profit_loss =Double.parseDouble(DataModel.getValueByVariable("DIFFERENCE_AMT", 0));
            
            System.out.println("Selected Hierarchy  : "+SelHierarchyID+" , Profit & Loss : "+profit_loss);
            HashMap hmSendToList = new HashMap();
            hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
            ArrayList<Integer> user_list = new ArrayList<>();
            for (int i = 1; i <= hmSendToList.size(); i++) {
                clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
                
                int user_id = (int) ObjUser.getAttribute("USER_ID").getVal();
                user_list.add(user_id);
            }
            
            if(profit_loss > 0)
            {
                //19-Oct-2018 - Aditya sir is compulsory for Loss, Now Prior Approval for Diversion
                /*
                if(user_list.contains(243) || user_list.contains(262))
                {
                     System.out.println("Ready with Adityabhai...!");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "For Loss, Select Hierarchy with Aditya Patel.");
                    return;
                }
                */
                
                
                
                //Bear By Party
                if(btnParty.isSelected())
                {
                    if(user_list.contains(16) || user_list.contains(17))
                    {
                            System.out.println("Ready with Harshad Patel(HP) and Mohan Akkalkotkar(MA)..!");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "For Loss and Bear By Party, Select Hierarchy of Harshad Patel(HP) and Mohan Akkalkotkar(MA).");
                        return;
                    }
                }
            }
        }
        
                
        String EXIST_PIECE_NO = DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0);
        ResultSet rs = data.getResult("SELECT * FROM  PRODUCTION.FELT_PKG_SLIP_DETAIL D,PRODUCTION.FELT_PKG_SLIP_HEADER H WHERE D.PKG_PIECE_NO='"+EXIST_PIECE_NO+"' AND H.CANCELED = 0 AND H.APPROVED = 0 AND H.PKG_DP_NO=D.PKG_DP_NO");
        try{
            rs.last();
            if(rs.getRow() > 0)
            {
                JOptionPane.showMessageDialog(null, "Packing Slip created for this PIECE : "+EXIST_PIECE_NO);
                return;
            }
        }catch(SQLException | HeadlessException e)
        {
            e.printStackTrace();
        }
        
        try{
            if(!txtBearPartyAmt.getText().equals(""))
            {
                double BEAR_BY_PARTY = Double.parseDouble(txtBearPartyAmt.getText());
            
                if(BEAR_BY_PARTY>0)
                {
                    double DB_AMT = Double.parseDouble(DataModel.getValueByVariable("DB_AMT", 0));   
                    
                    try{
                        if(!DebitMemoAmt2.equals(""))
                        {
                            double Debit2 = Double.parseDouble(DebitMemoAmt2.getText());
                            DB_AMT = DB_AMT + Debit2;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }
                        try{
                        if(!DebitMemoAmt3.equals(""))
                        {
                            double Debit3 = Double.parseDouble(DebitMemoAmt3.getText());
                            DB_AMT = DB_AMT + Debit3;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }
                        try{
                        if(!DebitMemoAmt4.equals(""))
                        {
                            double Debit4 = Double.parseDouble(DebitMemoAmt4.getText());
                            DB_AMT = DB_AMT + Debit4;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }

                        try{
                        if(!DebitMemoAmt5.equals(""))
                        {
                            double Debit5 = Double.parseDouble(DebitMemoAmt5.getText());
                            DB_AMT = DB_AMT + Debit5;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }

                        try{
                        if(!DebitMemoAmt6.equals(""))
                        {
                            double Debit6 = Double.parseDouble(DebitMemoAmt6.getText());
                            DB_AMT = DB_AMT + Debit6;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }

                        try{
                        if(!DebitMemoAmt7.equals(""))
                        {
                            double Debit7 = Double.parseDouble(DebitMemoAmt7.getText());
                            DB_AMT = DB_AMT + Debit7;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }

                        try{
                        if(!DebitMemoAmt8.equals(""))
                        {
                            double Debit8 = Double.parseDouble(DebitMemoAmt8.getText());
                            DB_AMT = DB_AMT + Debit8;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }

                        try{
                        if(!DebitMemoAmt9.equals(""))
                        {
                            double Debit9 = Double.parseDouble(DebitMemoAmt9.getText());
                            DB_AMT = DB_AMT + Debit9;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }

                        try{
                        if(!DebitMemoAmt10.equals(""))
                        {
                            double Debit10 = Double.parseDouble(DebitMemoAmt10.getText());
                            DB_AMT = DB_AMT + Debit10;
                        }
                        }catch(Exception e)
                        { e.printStackTrace(); }
                    
                    double diff = BEAR_BY_PARTY - DB_AMT;
                    if(diff>-1 && diff<1) {
                   // if(BEAR_BY_PARTY != DB_AMT){
                        String DebitNotNo = DataModel.getValueByVariable("DB_NOTE" , 0);
                        data.Execute("UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING SET DM_STATUS='USED' where DEBITMEMO_TYPE='MANUAL' AND DEBITMEMO_NO='"+DebitNotNo+"'");   
                    }                
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Debit Memo Amount and Loss must be same.");
                        return;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        /*
        try{
            
            double ORDERED_LENGTH =Double.parseDouble(DataModel.getValueByVariable("LENGTH", 0));
            double ORDERED_WIDTH =Double.parseDouble(DataModel.getValueByVariable("WIDTH", 0));
            double ORDERED_GSM =Double.parseDouble(DataModel.getValueByVariable("GSM", 0));
            double ORDERED_THORTICAL_WEIGHT =Double.parseDouble(DataModel.getValueByVariable("THORTICAL_WEIGHT", 0));
            
            double BILL_LENGTH =Double.parseDouble(DataModel.getValueByVariable("PR_BILL_LENGTH", 0));
            double BILL_WIDTH =Double.parseDouble(DataModel.getValueByVariable("PR_BILL_WIDTH", 0));
            double BILL_GSM =Double.parseDouble(DataModel.getValueByVariable("PR_BILL_GSM", 0));
            double BILL_WEIGHT =Double.parseDouble(DataModel.getValueByVariable("PR_BILL_WEIGHT", 0));
            
            if(BILL_LENGTH < ORDERED_LENGTH)
            {
                JOptionPane.showMessageDialog(this, "Bill Length cannot less than Ordered Length");
                return;
            }
            
            if(BILL_WIDTH < ORDERED_WIDTH)
            {
                JOptionPane.showMessageDialog(this, "Bill Width cannot less than Ordered Width");
                return;
            }
            
            
            if(BILL_GSM < ORDERED_GSM)
            {
                JOptionPane.showMessageDialog(this, "Bill GSM cannot less than Ordered GSM");
                return;
            }
            
            
            if(BILL_WEIGHT < ORDERED_THORTICAL_WEIGHT)
            {
                JOptionPane.showMessageDialog(this, "Bill Wieght cannot less than Ordered Wieght");
                return;
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        */
        
        String Req_Month = DataModel.getValueByVariable("OC_MONTHYEAR", 0);//OC_MONTHYEAR
        DataModel.setValueByVariable("REQ_MONTH", Req_Month, 0);
        
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
                aList.FirstFreeNo = 204;
                FFNo = aList.FirstFreeNo;
                clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, true);

                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='InProcess' WHERE PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");

                try{
                        String order_upn = feltOrder.getAttribute("D_UPN").getString();
                        System.out.println("UPDATE PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST SET DIVERSION_NO='"+S_O_NO.getText()+"' WHERE PIECE_NO='"+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"' AND UPN='"+order_upn+"'");
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST SET DIVERSION_NO='"+S_O_NO.getText()+"' WHERE PIECE_NO='"+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"' AND UPN='"+order_upn+"'");
                        System.out.println("UPDATE PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST SET DIVERSION_NO='PIECE DIVERTED' WHERE PIECE_NO='"+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"' AND UPN!='"+order_upn+"'");
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST SET DIVERSION_NO='PIECE DIVERTED' WHERE PIECE_NO='"+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"' AND UPN!='"+order_upn+"'");
                }catch(Exception e)
                {
                e.printStackTrace();
                } 
                
                try{
                    ResultSet  rsResCan;
                    Statement  stResCan;
                    Connection connection = data.getConn();
                    
                    String Doc_No = S_O_NO.getText();
                    
                    data.Execute("DELETE FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where SD_ORDER_NO='"+Doc_No+"'");
                    
                    for(int i=0;i<tblNewParty.getRowCount();i++)
                    {
                        if(!DataModel_NewParty.getValueByVariable("PR_PIECE_NO", i).equals(""))
                        {
                            stResCan=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            rsResCan=stResCan.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where SD_ORDER_NO=''");
                  
                            rsResCan.moveToInsertRow();
                            if(tblNewParty.getValueAt(i, 1).equals(true))
                            {
                                rsResCan.updateInt("CANCEL_PIECE", 1);
                            }                      
                            else
                            {
                                rsResCan.updateInt("CANCEL_PIECE", 0);
                            }                      
                            String RESCHEDULE_MONTH = DataModel_NewParty.getValueByVariable("RESCHEDULE_MONTH", i);
                            String PIECE_NO = DataModel_NewParty.getValueByVariable("PR_PIECE_NO", i);

                            rsResCan.updateString("SD_ORDER_NO", Doc_No);
                            rsResCan.updateString("SD_ORDER_DATE", EITLERPGLOBAL.formatDateDB(S_O_DATE.getText()));
                            rsResCan.updateString("PR_PIECE_NO", PIECE_NO);
                            rsResCan.updateString("RESCHEDULE_MONTH", RESCHEDULE_MONTH);
                            rsResCan.updateString("PARTY", "NEW_PARTY");
                            rsResCan.updateString("UPDATE_STATUS", "NOT UPDATED");
                            
                            rsResCan.insertRow();
                        }
                    }
                    for(int i=0;i<tblExistParty.getRowCount();i++)
                    {
                        if(!DataModel_ExistParty.getValueByVariable("PR_PIECE_NO", i).equals(""))
                        {
                            stResCan=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            rsResCan=stResCan.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where SD_ORDER_NO=''");

                            rsResCan.moveToInsertRow();
                            if(tblExistParty.getValueAt(i, 1).equals(true))
                            {
                                rsResCan.updateInt("CANCEL_PIECE", 1);
                            }                      
                            else
                            {
                                rsResCan.updateInt("CANCEL_PIECE", 0);
                            }                      
                            String RESCHEDULE_MONTH = DataModel_ExistParty.getValueByVariable("RESCHEDULE_MONTH", i);
                            String PIECE_NO = DataModel_ExistParty.getValueByVariable("PR_PIECE_NO", i);

                            rsResCan.updateString("SD_ORDER_NO", Doc_No);
                            rsResCan.updateString("SD_ORDER_DATE", EITLERPGLOBAL.formatDateDB(S_O_DATE.getText()));
                            rsResCan.updateString("PR_PIECE_NO", PIECE_NO);
                            rsResCan.updateString("RESCHEDULE_MONTH", RESCHEDULE_MONTH);
                            rsResCan.updateString("PARTY", "EXIST_PARTY");
                            rsResCan.updateString("UPDATE_STATUS", "NOT UPDATED");

                            rsResCan.insertRow();
                        }
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                
                if (OpgFinal.isSelected()) {
                    try {
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG='Diverted' WHERE PR_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");
                        //data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_DIVERSION_FLAG='Diverted' WHERE WIP_PIECE_NO='" + feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString() + "'");

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
                
                try{
                    ResultSet  rsResCan;
                    Statement  stResCan;
                    Connection connection = data.getConn();
                    
                    String Doc_No = S_O_NO.getText();
                    
                    data.Execute("DELETE FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where SD_ORDER_NO='"+Doc_No+"'");
                    
                    for(int i=0;i<tblNewParty.getRowCount();i++)
                    {
                        if(!DataModel_NewParty.getValueByVariable("PR_PIECE_NO", i).equals(""))
                        {
                            stResCan=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            rsResCan=stResCan.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where SD_ORDER_NO=''");
                  
                            rsResCan.moveToInsertRow();
                            if(tblNewParty.getValueAt(i, 1).equals(true))
                            {
                                rsResCan.updateInt("CANCEL_PIECE", 1);
                            }                      
                            else
                            {
                                rsResCan.updateInt("CANCEL_PIECE", 0);
                            }                      
                            String RESCHEDULE_MONTH = DataModel_NewParty.getValueByVariable("RESCHEDULE_MONTH", i);
                            String PIECE_NO = DataModel_NewParty.getValueByVariable("PR_PIECE_NO", i);

                            rsResCan.updateString("SD_ORDER_NO", Doc_No);
                            rsResCan.updateString("SD_ORDER_DATE", EITLERPGLOBAL.formatDateDB(S_O_DATE.getText()));
                            rsResCan.updateString("PR_PIECE_NO", PIECE_NO);
                            rsResCan.updateString("RESCHEDULE_MONTH", RESCHEDULE_MONTH);
                            rsResCan.updateString("PARTY", "NEW_PARTY");
                            rsResCan.updateString("UPDATE_STATUS", "NOT UPDATED");
                            
                            rsResCan.insertRow();
                        }
                    }
                    
                    for(int i=0;i<tblExistParty.getRowCount();i++)
                    {
                        if(!DataModel_ExistParty.getValueByVariable("PR_PIECE_NO", i).equals(""))
                        {
                            stResCan=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            rsResCan=stResCan.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where SD_ORDER_NO=''");
                  
                            rsResCan.moveToInsertRow();
                            if(tblExistParty.getValueAt(i, 1).equals(true))
                            {
                                rsResCan.updateInt("CANCEL_PIECE", 1);
                            }                      
                            else
                            {
                                rsResCan.updateInt("CANCEL_PIECE", 0);
                            }                      
                            String RESCHEDULE_MONTH = DataModel_ExistParty.getValueByVariable("RESCHEDULE_MONTH", i);
                            String PIECE_NO = DataModel_ExistParty.getValueByVariable("PR_PIECE_NO", i);

                            rsResCan.updateString("SD_ORDER_NO", Doc_No);
                            rsResCan.updateString("SD_ORDER_DATE", EITLERPGLOBAL.formatDateDB(S_O_DATE.getText()));
                            rsResCan.updateString("PR_PIECE_NO", PIECE_NO);
                            rsResCan.updateString("RESCHEDULE_MONTH", RESCHEDULE_MONTH);
                            rsResCan.updateString("PARTY", "EXIST_PARTY");
                            rsResCan.updateString("UPDATE_STATUS", "NOT UPDATED");
                            
                            rsResCan.insertRow();
                        }
                    }
                    
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                
                if (OpgFinal.isSelected()) {
                   
                    try{
                        System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE = 'DIVERTED',PR_DIVERSION_FLAG = 'Diverted', PR_DIVERTED_FLAG = 1, PR_DIVERTED_REASON = '"+REMARK.getText()+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"' ");
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE = 'DIVERTED', PR_DIVERSION_FLAG = 'Diverted', PR_DIVERTED_FLAG = 1, PR_DIVERTED_REASON = '"+REMARK.getText()+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"' ");
                        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE = 'DIVERTED', WIP_DIVERSION_FLAG = 'Diverted', WIP_DIVERTED_FLAG = 1, WIP_DIVERTED_REASON = '"+REMARK.getText()+"' WHERE WIP_PIECE_NO ='"+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"' ");
                    } catch (Exception e) {
                        try{
                            
                        //03/06/2019    
                        //JavaMail.SendMail("daxesh@dineshmills.com", "Piece Stage not changed in Diversion "+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+" Doc No : "+S_O_NO.getText(), "Diversion Error", "sdmlerp@dineshmills.com");
                        }catch(Exception etest){              
                        }
                        //e.printStackTrace();
                    }
                    
                    if(chbEmailUpdate.isSelected())
                    {
                        data.Execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER SET EMAIL='"+txtEmailId.getText()+"',EMAIL_ID2='"+txtEmailId2.getText()+"',EMAIL_ID3='"+txtEmailId3.getText()+"' where PARTY_CODE='"+PARTY_CODE.getText()+"' and COMPANY_ID='"+EITLERPGLOBAL.gCompanyID+"'");
                    }
                    String REQ_MONTH_DDMMYYYY = "";
                    
                    try{
                        
                        if(!Req_Month.equals(""))
                        {
                            REQ_MONTH_DDMMYYYY = LastDayOfReqMonth(Req_Month);
                        }

                        System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET  PR_OC_MONTHYEAR = '"+Req_Month+"', PR_OC_LAST_DDMMYY = '"+REQ_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_MONTH='"+Req_Month+"',PR_CURRENT_SCH_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("D_PIECE_NO").getString()+"'");        
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET  PR_OC_MONTHYEAR = '"+Req_Month+"', PR_OC_LAST_DDMMYY = '"+REQ_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_MONTH='"+Req_Month+"',PR_CURRENT_SCH_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("D_PIECE_NO").getString()+"' ");
                    }catch(Exception e)
                    {
                        try{
                            JavaMail.SendMail("daxesh@dineshmills.com", "Piece OCMONTH not changed in Diversion "+feltOrder.getAttribute("ORIGINAL_PIECE_NO").getString()+"  Doc No : "+S_O_NO.getText()+" - UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET  PR_OC_MONTHYEAR = '"+Req_Month+"', PR_OC_LAST_DDMMYY = '"+REQ_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_MONTH='"+Req_Month+"',PR_CURRENT_SCH_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("D_PIECE_NO").getString()+"' ", "Diversion Error OCMONTH", "sdmlerp@dineshmills.com");
                        }catch(Exception etest){              
                        }
                        e.printStackTrace();
                    }
                  
                    
                    try{
                        String DOC_NO = S_O_NO.getText();
                        String DOC_DATE = S_O_DATE.getText();
                        String Party_Code = PARTY_CODE.getText();

                        //Enable before Diversion Live
                        
                        //03/06/2019    
                        String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true, 0);
                        System.out.println("Send Mail Responce : "+responce); 
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    ActionForRescheduleAndCancellation();
                    
                    //03/06/2019    
                    sendMailToContactPerson(feltOrder.getAttribute("D_PIECE_NO").getString());

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
    
    private void ActionForRescheduleAndCancellation()
    {
            try{
                    ArrayList<String> CancelledPieces =new ArrayList<String>();
                    ArrayList<String> RescheduledPieces =new ArrayList<String>();
                    ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN where UPDATE_STATUS='NOT UPDATED' AND SD_ORDER_NO='"+S_O_NO.getText()+"'");
                    if (rsData.getRow() > 0) {
                    rsData.first();

                    while (!rsData.isAfterLast()) {
                    String PR_PIECE_NO = rsData.getString("PR_PIECE_NO");
                    int CANCEL_PIECE = rsData.getInt("CANCEL_PIECE");
                    String RESCHEDULE_MONTH = rsData.getString("RESCHEDULE_MONTH");
                    String SD_ORDER_NO = rsData.getString("SD_ORDER_NO");
                    String Piece_Stage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'");
                    String PR_OC_MONTHYEAR = data.getStringValueFromDB("SELECT PR_OC_MONTHYEAR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'");

                    if(Piece_Stage.equals("BOOKING") || Piece_Stage.equals("PLANNING") && PR_OC_MONTHYEAR.equals(""))
                    {
                        if(CANCEL_PIECE == 1)
                        {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='CANCELED',PR_WIP_STATUS='CANCELED',PR_PRIORITY_HOLD_CAN_FLAG='9' WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_PIECE_STAGE='CANCELED',WIP_STATUS='CANCELED',WIP_PRIORITY_HOLD_CAN_FLAG='9' WHERE WIP_PIECE_NO='"+PR_PIECE_NO+"'");
                            CancelledPieces.add(PR_PIECE_NO);
                        }
                        else if(!RESCHEDULE_MONTH.equals(""))
                        {
                            String PR_REQ_MTH_LAST_DDMMYY = LastDayOfReqMonth(RESCHEDULE_MONTH);
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_REQUESTED_MONTH='"+RESCHEDULE_MONTH+"',PR_REQ_MTH_LAST_DDMMYY='"+PR_REQ_MTH_LAST_DDMMYY+"' WHERE PR_PIECE_NO='"+PR_PIECE_NO+"'");
                            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_REQUESTED_MONTH='"+RESCHEDULE_MONTH+"',WIP_REQ_MTH_LAST_DDMMYY='"+PR_REQ_MTH_LAST_DDMMYY+"' WHERE WIP_PIECE_NO='"+PR_PIECE_NO+"'");
                            RescheduledPieces.add(PR_PIECE_NO);
                        }

                            data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN SET UPDATE_STATUS='UPDATED' WHERE SD_ORDER_NO='"+SD_ORDER_NO+"' AND PIECE_NO='"+PR_PIECE_NO+"'");
                        }
                        else
                        {
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_ORDER_DIVERSION_RESCH_CAN SET UPDATE_STATUS='NOT CHANGED' WHERE SD_ORDER_NO='"+SD_ORDER_NO+"' AND PIECE_NO='"+PR_PIECE_NO+"'");
                        }
                            rsData.next();
                        }
                    }

                    if(!CancelledPieces.isEmpty() || !RescheduledPieces.isEmpty())
                    {
                        String pMessage = "";

                        pMessage = "<br><br>";

                        pMessage = pMessage + "<div style='min-width:1000px;'>\n" +
                        " \n" +
                        " <p>\n" +
                        " <t>Diversion No : "+S_O_NO.getText()+"\n" +
                        " <t>New Party Remark : "+txtNewPartyRemark.getText()+"\n" +
                        " <t>Exist Party Remark : "+txtReschCanRemark.getText()+"\n" +
                        " </div>\n" +
                        " <br><div style=' text-align:center; min-width:1000px;'><u>Reschedule Pieces</u></div>\n" +
                        " <br><br>\n" +
                        " </div>\n" +
                        " <div>\n" +
                        " <table border='1' width='100%'>\n" +
                        " <tr>" +
                        " <th>PIECE NO </th>\n" +
                        " <th>REQUESTED MONTH</th>\n" +
                        " <th>MACHINE NO</th>\n" +
                        " <th>POSITION</th>\n" +
                        " <th>GROUP</th>\n" +
                        " <th>PARTY CODE</th>\n" +
                        " <th>PARTY NAME</th>\n" +
                        " <th>LENGTH</th>\n" +
                        " <th>WIDTH</th>\n" +
                        " <th>GSM</th>\n" +
                        " <th>WEIGHT</th>\n" +
                        " <th>SQMTR</th>\n" +
                        " </tr>";


                        for(String Piece:RescheduledPieces)
                        {

                                try {
                                ResultSet rsPieces = data.getResult("SELECT PR.*,P.PARTY_NAME,PS.POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR \n" +
                                " LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER P ON P.PARTY_CODE=PR.PR_PARTY_CODE \n" +
                                " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS ON PS.POSITION_NO = PR.PR_POSITION_NO " +
                                "WHERE PR.PR_PIECE_NO IN ('"+Piece+"')");
                                rsPieces.first();


                                pMessage = pMessage + "<tr>" +
                                "<td> "+rsPieces.getString("PR_PIECE_NO")+" </td>\n" +
                                "<td> "+rsPieces.getString("PR_REQUESTED_MONTH")+" </td>\n" +
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

                        pMessage = pMessage + "<div style='min-width:1000px;'>\n" +
                        " <p>\n" +
                        " </div>\n" +
                        " <br><div style=' text-align:center; min-width:1000px;'><u>Cancel Pieces</u></div>\n" +
                        " <br><br>\n" +
                        " </div>\n" +
                        " <div>\n" +
                        " <table border='1' width='100%'>\n" +
                        " <tr>" +
                        " <th>PIECE NO </th>\n" +
                        " <th>REQUESTED MONTH</th>\n" +
                        " <th>MACHINE NO</th>\n" +
                        " <th>POSITION</th>\n" +
                        " <th>GROUP</th>\n" +
                        " <th>PARTY CODE</th>\n" +
                        " <th>PARTY NAME</th>\n" +
                        " <th>LENGTH</th>\n" +
                        " <th>WIDTH</th>\n" +
                        " <th>GSM</th>\n" +
                        " <th>WEIGHT</th>\n" +
                        " <th>SQMTR</th>\n" +
                        " </tr>";


                        for(String Piece:CancelledPieces)
                        {

                        try {
                                ResultSet rsPieces = data.getResult("SELECT PR.*,P.PARTY_NAME,PS.POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR \n" +
                                " LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER P ON P.PARTY_CODE=PR.PR_PARTY_CODE \n" +
                                " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS ON PS.POSITION_NO = PR.PR_POSITION_NO " +
                                "WHERE PR.PR_PIECE_NO IN ('"+Piece+"')");
                                rsPieces.first();

                                pMessage = pMessage + "<tr>" +
                                "<td> "+rsPieces.getString("PR_PIECE_NO")+" </td>\n" +
                                "<td> "+rsPieces.getString("PR_REQUESTED_MONTH")+" </td>\n" +
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
                            //String EmailId= "daxesh@dineshmills.com";
                            String EmailId= "brdfltdesign@dineshmills.com,vdshanbhag@dineshmills.com,feltpp@dineshmills.com,narendramotiani@dineshmills.com,jaydeeppandya@dineshmills.com,mitanglad@dineshmills.com,anupsinghchauhan@dineshmills.com,siddharthneogi@dineshmills.com";
                            System.out.println("pMessage : "+pMessage);
                            SendMail(EmailId.trim(), pMessage, "Rescheduled and Cancelled Pieces by diversion", "sdmlerp@dineshmills.com");
                        } catch (Exception e) {
                            System.out.println("Error Msg "+e.getMessage());
                            e.printStackTrace();
                        }
                }
            }catch(Exception e)
            {
            e.printStackTrace();
            }
    }
    
    private void sendMailToContactPerson(String Piece)
    {
        
        ResultSet rsData1;
        String OC_NO = "";
        String OC_DATE = "";
        String OA_NO = "";
        String OA_DATE = "";
        String REQ_MONTH_DDMMYYYY = "";
        String Payment_Terms = "";

        try{
            OC_NO = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 797, 288, true);
            OC_DATE = EITLERPGLOBAL.getCurrentDateDB();
            String Req_Month = feltOrder.getAttribute("REQ_MONTH").getString();

            data.Execute("update PRODUCTION.FELT_SALES_ORDER_DIVERSION SET  OC_NO='"+OC_NO+"',OC_DATE='"+OC_DATE+"',OC_MONTHYEAR='"+Req_Month+"' WHERE SD_ORDER_NO='"+S_O_NO.getText()+"'");
            if(!Req_Month.equals(""))
            {
                REQ_MONTH_DDMMYYYY = LastDayOfReqMonth(Req_Month);
            }
            System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_OC_NO = '"+OC_NO+"', PR_OC_DATE = '"+OC_DATE+"', PR_OC_MONTHYEAR = '"+Req_Month+"', PR_OC_LAST_DDMMYY = '"+REQ_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_MONTH='"+Req_Month+"',PR_CURRENT_SCH_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("D_PIECE_NO").getString()+"'");        
            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_OC_NO = '"+OC_NO+"', PR_OC_DATE = '"+OC_DATE+"', PR_OC_MONTHYEAR = '"+Req_Month+"', PR_OC_LAST_DDMMYY = '"+REQ_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_MONTH='"+Req_Month+"',PR_CURRENT_SCH_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("D_PIECE_NO").getString()+"' ");

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
        
        String pMessage = "";
                   //Felt Order No : " + S_O_NO.getText() + ",<br>Order Date : " + S_O_DATE.getText() + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) + "<br> 
                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
//                    Connection Conn1; 
//                    Statement stmt1;
//                    ResultSet rsData1;
//                    Conn1 = data.getConn();
//                    stmt1 = Conn1.createStatement();
//                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO IN  ('" + Pieces_Str + "')");
//                    rsData1.first();
                    
                    ResultSet rsPieces = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO IN  ('" + Piece + "') ");
                    System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO IN  ('" + Piece + "')");
                    rsPieces.first();
                    
                   
                    if (rsPieces.getRow() > 0) {
                        while (!rsPieces.isAfterLast()){ 
                            
                            
                            String Reference;
                            String Reference_Date;
                            if(rsPieces.getString("PR_PARTY_CODE").equals("P.O.")){
                                Reference = "P.O. - "+rsPieces.getString("PR_PO_NO");
                                Reference_Date = rsPieces.getString("PR_PO_DATE");
                            }                           
                            else
                            {
                                Reference = rsPieces.getString("PR_REFERENCE");
                                Reference_Date = rsPieces.getString("PR_REFERENCE_DATE");
                            }
                            
                            pMessage = pMessage + "<br><br>";

                            pMessage = pMessage + "<div style='min-width:1000px;'>\n" +
        "	<div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION</u></div>\n" +
        "	<br><br>\n" +
        "	<div style=' width:100%; heigth :200px;'>\n" +
        "	\n" +
        "		<div style=' width: 50%; float:left;'>\n" +
        "			TO:  "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsPieces.getString("PR_PARTY_CODE"))+"<br>\n" +
        "			Party code: "+rsPieces.getString("PR_PARTY_CODE")+" <br>\n" +
        "			Email id: "+rsPieces.getString("PR_EMAIL_ID")+"   <br>\n" +
        "			Kind Attn: "+rsPieces.getString("PR_CONTACT_PERSON")+"  	\n" +
        "			<hr>\n" +
        "		</div>\n" +
        "		<div style=' width:50%;  float:left;'>\n" +
        "			Your order ref: "+Reference+"<br>\n" +
        "			Date : "+EITLERPGLOBAL.formatDate(Reference_Date)+"<br>\n" +
        "			Our order acknowledgement no & date: "+rsPieces.getString("PR_OA_NO")+" - "+EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OA_DATE"))+" <br>\n" +
        "			Our order confirmation no & date: "+rsPieces.getString("PR_OC_NO")+" - "+EITLERPGLOBAL.formatDate(rsPieces.getString("PR_OC_DATE"))+" <br>\n" +
        "			<hr>\n" +
        "		</div>\n" +
        "	\n" +
        "	</div>\n" +
        "	<div>\n" +
        "		Dear sir,\n" +
        "		<p>\n" +
        "		<t>In continuation of our aforesaid order acknowledgement in connection with your valued PO as mentioned above, We are pleased to inform you that your following felts covered under our said order acknowledgement will be taken up in our current manufacturing plan so as to make the same ready as per the following schedule.\n" +
        "	</div>\n" +
        "	<div>\n" +
        "		<table border='1' width='100%'>\n" +
                                    "<tr>" +
        "			<th>M/C NO </th>\n" +
        "			<th>POSITION</th>\n" +
        "			<th>DESCRIPTION</th>\n" +
        "			<th>PIECE NO</th>\n" +
        "			<th>LENGTH</th>\n" +
        "			<th>WIDTH</th>\n" +
        "			<th>GSM</th>\n" +
        "			<th>SCHEDULE</th>\n" +
                                    "</tr>";

                            
                            String PositionDesc = data.getStringValueFromDB("select MM_MACHINE_POSITION_DESC from PRODUCTION.FELT_MACHINE_MASTER_DETAIL where MM_MACHINE_POSITION="+rsPieces.getString("PR_POSITION_NO"));
                            String ProductDesc = data.getStringValueFromDB("SELECT PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + rsPieces.getString("PR_PRODUCT_CODE") + "' )");
                            
                            pMessage = pMessage +  "<tr>" +
                                "<td> "+rsPieces.getString("PR_MACHINE_NO")+" </td>\n" +
                                "<td>"+PositionDesc+"</td>\n" +
                                "<td>"+ProductDesc+"</td>\n" +
                                "<td>"+rsPieces.getString("PR_PIECE_NO")+"</td>\n" +
                                "<td>"+rsPieces.getString("PR_BILL_LENGTH")+"</td>\n" +
                                "<td>"+rsPieces.getString("PR_BILL_WIDTH")+"</td>\n" +
                                "<td>"+rsPieces.getString("PR_BILL_GSM")+"</td>\n" +
                                "<td>"+rsPieces.getString("PR_OC_MONTHYEAR")+"</td>\n" +
                                "</tr>" ;
                                     
                                pMessage = pMessage + "</table>\n" +
        "	</div>\n" +
        
        "	<p>\n" +
        "	 \n" +
        "	You are requested to kindly ensure that the said felt is lifted as soon as it gets ready. The schedules for other felts (if any) covered under your relevant PO would be confirmed later as mentioned in our order acknowledgement.<p>\n" +
        "	Assuring you of our best services at all times and meanwhile thanking you once again. \n" +
        "	<p>\n" +
        "	</div>\n" +
        "	\n" +
        "\n" +
        "</div>";

//                                pMessage = pMessage + "<div>"
//                                        + "<br><br>Reply on : felts@dineshmills.com<br><br>"
//                                        + "</div>";

        //                        Connection Conn;
        //                        Statement stmt;
        //                        ResultSet rsData;

        //                        Conn = data.getConn();
        //                        stmt = Conn.createStatement();
        //                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
        //                        rsData.first();

                                //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                                //pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PARTY_CODE"));
                                ///pMessage = pMessage + "<br> REFERENCE : " + rsData1.getString("REFERENCE");
                                //pMessage = pMessage + "<br> REFERENCE DATE : " + rsData1.getString("REFERENCE_DATE");
                                //pMessage = pMessage + "<br> REMARK : "+rsData1.getString("REMARK");

                                pMessage = pMessage + "";

                                try {
                                        
                                        String EmailId= "sdmlerp@dineshmills.com";
                                        if(!txtEmailId.getText().equals(""))
                                        {
                                            EmailId = EmailId + "," + txtEmailId.getText();
                                        }
                                        if(!txtEmailId2.getText().equals(""))
                                        {
                                            EmailId = EmailId + "," + txtEmailId2.getText();
                                        }
                                        if(!txtEmailId3.getText().equals(""))
                                        {
                                            EmailId = EmailId + "," + txtEmailId3.getText();
                                        }
                                        JavaMail.SendMailFeltOC(EmailId.trim(), pMessage, "Order Confirmation : " + rsPieces.getString("PR_OC_NO"), "feltoc@dineshmills.com");
                                    } catch (Exception e) {
                                        System.out.println("Error Msg "+e.getMessage());
                                        e.printStackTrace();
                                    }
                            
                            
                            rsPieces.next();
                           
                        }
                    }
                       

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
    }
    /*private void sendMailToContactPerson()
    {
        String pMessage = "";
                   //Felt Order No : " + S_O_NO.getText() + ",<br>Order Date : " + S_O_DATE.getText() + ",<br><br><br> Document has been final approved by " + clsUser.getUserName((int) EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) + "<br> 
                //<br>Party Code : "+PARTY_CODE+",<br>Party Name : "+clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE)+"
                try {
                    Connection Conn1; 
                   
                    Statement stmt1;
                    ResultSet rsData1;
                    String OC_NO = "";
                    String OC_DATE = "";
                    String OA_NO = "";
                    String OA_DATE = "";
                    String REQ_MONTH_DDMMYYYY = "";
                    String Payment_Terms = "";
                    
                    try{
                        OC_NO = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 797, 288, true);
                        OC_DATE = EITLERPGLOBAL.getCurrentDateDB();
                        String Req_Month = feltOrder.getAttribute("REQ_MONTH").getString();
                        
                        data.Execute("update PRODUCTION.FELT_SALES_ORDER_DIVERSION SET  OC_NO='"+OC_NO+"',OC_DATE='"+OC_DATE+"',OC_MONTHYEAR='"+Req_Month+"' WHERE SD_ORDER_NO='"+S_O_NO.getText()+"'");
                        if(!Req_Month.equals(""))
                        {
                            REQ_MONTH_DDMMYYYY = LastDayOfReqMonth(Req_Month);
                        }
                        System.out.println("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_OC_NO = '"+OC_NO+"', PR_OC_DATE = '"+OC_DATE+"', PR_OC_MONTHYEAR = '"+Req_Month+"', PR_OC_LAST_DDMMYY = '"+REQ_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_MONTH='"+Req_Month+"',PR_CURRENT_SCH_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("D_PIECE_NO").getString()+"'");        
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_OC_NO = '"+OC_NO+"', PR_OC_DATE = '"+OC_DATE+"', PR_OC_MONTHYEAR = '"+Req_Month+"', PR_OC_LAST_DDMMYY = '"+REQ_MONTH_DDMMYYYY+"',PR_CURRENT_SCH_MONTH='"+Req_Month+"',PR_CURRENT_SCH_LAST_DDMMYY='"+REQ_MONTH_DDMMYYYY+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("D_PIECE_NO").getString()+"' ");
                        
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    
                    try{
                     
                        OA_NO = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 796, 287, true);
                        OA_DATE = EITLERPGLOBAL.getCurrentDateDB();
                        
                        data.Execute("update PRODUCTION.FELT_SALES_ORDER_DIVERSION SET  OA_NO='"+OA_NO+"',OA_DATE='"+OA_DATE+"' WHERE SD_ORDER_NO='"+S_O_NO.getText()+"'");
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_OA_NO = '"+OA_NO+"', PR_OA_DATE = '"+OA_DATE+"' WHERE PR_PIECE_NO ='"+feltOrder.getAttribute("D_PIECE_NO").getString()+"' ");
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    
                    try{
                        data.Execute("update PRODUCTION.FELT_SALES_ORDER_DIVERSION SET  OC_NO='"+OC_NO+"',OA_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE SD_ORDER_NO='"+S_O_NO.getText()+"'");
                    
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    
                    try{
                        String ChargeCode = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='"+PARTY_CODE.getText()+"'");
                        Payment_Terms = data.getStringValueFromDB("SELECT INVOICE_TYPE_DESC FROM DINESHMILLS.D_SAL_POLICY_INVOICE_TYPE WHERE INVOICE_TYPE_ID='"+ChargeCode+"'");
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println("OA NO = "+DataModel.getValueByVariable("OA_NO", 0));
                    Conn1 = data.getConn();
                    stmt1 = Conn1.createStatement();
                    rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION where SD_ORDER_NO='" + S_O_NO.getText() + "'");
                    rsData1.first();

                    pMessage = pMessage + "<br><br>";
                   
                    pMessage = pMessage + "<div style='min-width:1000px;'>\n" +
"	<div style=' text-align:center; min-width:1000px;'><u>ORDER CONFIRMATION</u></div>\n" +
"	<br><br>\n" +
"	<div style=' width:100%; heigth :200px;'>\n" +
"	\n" +
"		<div style=' width: 50%; float:left;'>\n" +
"			TO:  "+PARTY_NAME.getText()+"<br>\n" +
"			Party code: "+PARTY_CODE.getText()+" <br>\n" +
"			Email id: "+txtEmailId.getText()+"   <br>\n" +
"			Kind Attn: "+txtContactPerson.getText()+"  	\n" +
"			<hr>\n" +
"		</div>\n" +
"		<div style=' width:50%;  float:left;'>\n" +
"			Your order ref: "+S_O_NO.getText()+"<br>\n" +
"			Date : "+S_O_DATE.getText()+"<br>\n" +
"			Our order Confirmation ref: "+OC_NO+" <br>\n" +
"			Date: "+EITLERPGLOBAL.formatDate(OC_DATE)+"\n" +
"			<hr>\n" +
"		</div>\n" +
"	\n" +
"	</div>\n" +
"	<div>\n" +
"		Dear sir,\n" +
"		<p>\n" +
"		<t>We thank you for your above valued purchase order which has been registered by us and the tentative schedules are as hereunder.\n" +
"	</div>\n" +
"	<div>\n" +
"		<table border='1' width='100%'>\n" +
                            "<tr>" +
"			<th>M/C NO </th>\n" +
"			<th>POSITION</th>\n" +
"			<th>DESCRIPTION</th>\n" +
"			<th>PIECE NO</th>\n" +
"			<th>LENGTH</th>\n" +
"			<th>WIDTH</th>\n" +
"			<th>GSM</th>\n" +
"			<th>TENTATIVE SCHEDULE</th>\n" +
                            "</tr>";
                    
                    rsData1.first();
                       
                    if (rsData1.getRow() > 0) {
                        while (!rsData1.isAfterLast()){ 

                                    pMessage = pMessage +  "<tr>" +
                                        "<td> "+rsData1.getString("D_MACHINE_NO")+" </td>\n" +
                                        "<td>"+rsData1.getString("D_POSITION_NO")+"</td>\n" +
                                        "<td>"+rsData1.getString("D_POSITION_DESC")+"</td>\n" +
                                        "<td>"+rsData1.getString("D_PIECE_NO")+"</td>\n" +
                                        "<td>"+rsData1.getString("PR_BILL_LENGTH")+"</td>\n" +
                                        "<td>"+rsData1.getString("PR_BILL_WIDTH")+"</td>\n" +
                                        "<td>"+rsData1.getString("PR_BILL_GSM")+"</td>\n" +
                                        "<td>"+rsData1.getString("REQ_MONTH")+"</td>\n" +
                                        "</tr>" ;
                                    rsData1.next();
                            }
                        }
                        pMessage = pMessage + "</table>\n" +
"	</div>\n" +
"	<div>Payment terms: "+Payment_Terms+"\n" +
"	<p>\n" +
"	 The above schedules are tentative and all out efforts will be made to maintain the said schedules. However, in case of any unforeseen circumstances or in the event of any of your  earlier ordered felts for the said positions not lifted by you as per the agreed schedule and hence remaining in our stock, the aforementioned schedules may not hold good.\n" +
"	<p>\n" +
"	We would send you our firm delivery confirmation 60-75  days before the above schedules. We are sure you would appreciate this and extend your cooperation to us as always. \n" +
"	<p>\n" +
"	Assuring  you of our best services at all times and meanwhile thanking you once again,\n" +
"	</div>\n" +
"	\n" +
"\n" +
"</div>";
                        
                        pMessage = pMessage + "<div>"
                                + "<br><br>Reply on : felts@dineshmills.com<br><br>"
                                + "</div>";
                    
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;

//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO = '" + Existing_Piece_No + "'");
//                        rsData.first();
                        
                        //pMessage = pMessage + "<br> PIECE NO : " + rsData.getString("PR_PIECE_NO");
                        //pMessage = pMessage + "<br> PARTY CODE : " + rsData1.getString("PARTY_CODE") + " - " + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsData1.getString("PARTY_CODE"));
                        ///pMessage = pMessage + "<br> REFERENCE : " + rsData1.getString("REFERENCE");
                        //pMessage = pMessage + "<br> REFERENCE DATE : " + rsData1.getString("REFERENCE_DATE");
                        //pMessage = pMessage + "<br> REMARK : "+rsData1.getString("REMARK");
                        
                        pMessage = pMessage + "";
                                 
                        try {
                                String EmailId= txtEmailId.getText();
                                
                                SendMail(EmailId.trim(), pMessage, "Order Confirmation : " + DOC_NO, "feltoc@dineshmills.com");
                            } catch (Exception e) {
                                System.out.println("Error Msg "+e.getMessage());
                                e.printStackTrace();
                            }
                       

                    } catch (Exception e) {
                        System.out.println("Error on Mail: " + e.getMessage());
                    }
    }
    
    */
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
        System.out.println("DOC NO : "+productionDocumentNo);
        System.out.println("New User Id : "+EITLERPGLOBAL.gNewUserID);
            if (feltOrder.IsEditable(productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;

            DisableToolbar();
            GenerateHierarchyCombo();
            GenerateSendToCombo();
            SetupApproval();
            DisplayData();

            //ReasonResetReadonly();
            //cmbOrderReason.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, productionDocumentNo)) {
                SetFields(true);
            } else {

                EnableApproval();
            }
            
            if(btnCompany.isSelected()){
                String New_Piece_No = DataModel.getValueByVariable("PIECE_NO", 0);
                //String EXISTING_Piece_No = DataModel_ExistPiece.getValueByVariable("PIECE_NO", 0);
                System.out.println("PIECE NO : "+New_Piece_No);
                String Prior_Approval_No = data.getStringValueFromDB("SELECT SD_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE APPROVED=1 AND CANCELED=0  AND D_PARTY_CODE='"+PARTY_CODE.getText()+"' AND D_PIECE_NO='"+New_Piece_No+"' ORDER BY SD_ORDER_DATE DESC");
                String Prior_Approval_Date = data.getStringValueFromDB("SELECT SD_ORDER_DATE FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE APPROVED=1 AND CANCELED=0  AND D_PARTY_CODE='"+PARTY_CODE.getText()+"' AND D_PIECE_NO='"+New_Piece_No+"' ORDER BY SD_ORDER_DATE DESC");
                
                System.out.println("SELECT SD_ORDER_NO FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION_PRIOR_APPROVAL WHERE APPROVED=1 AND CANCELED=0  AND D_PIECE_NO='"+New_Piece_No+"'");
                lblPriorApprovalNo.setText(Prior_Approval_No);
                lblPriorApprovalDate.setText(Prior_Approval_Date);
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

        String incharge = data.getStringValueFromDB("SELECT DESIGNER_INCHARGE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PARTY_CODE.getText()+"'");
        System.out.println("Selected Incharge : "+incharge);
        
        if(incharge.equals(""))
        {
            if(!designer_not_msg)
            {
                //JOptionPane.showMessageDialog(this, "For Party "+PARTY_CODE.getText()+" Designer is not selected");
                System.out.println("For Party "+PARTY_CODE.getText()+" Designer is not selected");
                designer_not_msg=true;
            }
            //cmbHierarchy.setEnabled(true);
        }
        
        if(!DataModel.getValueByVariable("DIFFERENCE_AMT", 0).equals(""))
        {
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
            if (btnParty.isSelected()) {
                //Loss - By Party - STOCK
                if ("IN STOCK".equals(PR_PIECE_STAGE) || "STOCK".equals(PR_PIECE_STAGE)) {
                
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
                } ////Loss - By Party - WIP
                else {
                    
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
                }
            } //Loss - By Company
            else {
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
        }

    }

    private void filter(ArrayList<String> Hierarchy) {
        for (String current : Hierarchy) {
            cmbHierarchyModel.setSelectedItem(current);
//            int n = cmbHierarchyModel.getIndexOf(current);
//            if (n != -1) {
//                cmbHierarchyModel.setSelectedItem(current);
//                cmbHierarchy.setEnabled(false);
//            }
        }
    }

//    private void GenerateSendToCombo() {
//        HashMap hmSendToList = new HashMap();
//        try {
//            cmbSendToModel = new EITLComboModel();
//            cmbSendTo.removeAllItems();
//            cmbSendTo.setModel(cmbSendToModel);
//            if (EditMode == EITLERPGLOBAL.ADD) {
//                hmSendToList = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
//                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
//
//                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
//                        //Exclude Current User
//                    } else {
//                        cmbSendToModel.addElement(aData);
//                    }
//                }
//            } else {
//                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, S_O_NO.getText());
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    ComboData aData = new ComboData();
//                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
//                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
//                    cmbSendToModel.addElement(aData);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
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

//        for(int i=0;i<Tab1.getComponentCount()-1;i++) {
//            if(Tab1.getComponent(i).getName()!=null) {
//                
//                FieldName=Tab1.getComponent(i).getName();
//                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
//                    Tab1.getComponent(i).setEnabled(true);
//                }
//            }
//        }
        //=============== Header Fields Setup Complete =================//
        //=============== Setting Table Fields ==================//
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
        REFERENCE.setEnabled(pStat);
        REF_DATE.setEnabled(pStat);
        P_O_NO.setEnabled(pStat);
        P_O_DATE.setEnabled(pStat);
        REMARK.setEnabled(pStat);
        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);

        txtReschCanRemark.setEnabled(pStat);
        txtNewPartyRemark.setEnabled(pStat);
        txtContactPerson.setEnabled(pStat);
        txtEmailId.setEnabled(pStat);
        txtEmailId2.setEnabled(pStat);
        txtEmailId3.setEnabled(pStat);
        txtPhoneNo.setEnabled(pStat);
        chbEmailUpdate.setEnabled(pStat);
        Table.setEnabled(pStat);
        TableExist.setEnabled(pStat);
        btnCompany.setEnabled(pStat);
        btnParty.setEnabled(pStat);
        txtPartyBearer.setEnabled(pStat);
        txtPartyBearerName.setEnabled(pStat);
        btnPartialPayment.setEnabled(pStat);
                
        
        Ac1.setEnabled(pStat);
        Ac2.setEnabled(pStat);
        Ac3.setEnabled(pStat);
        Ac4.setEnabled(pStat);
        Ac5.setEnabled(pStat);
        Ac6.setEnabled(pStat);
        Ac7.setEnabled(pStat);
        Ac8.setEnabled(pStat);
        Ac9.setEnabled(pStat);
        Ac10.setEnabled(pStat);
        Ac11.setEnabled(pStat);
        Ac12.setEnabled(pStat);
        Ac13.setEnabled(pStat);
        Ac14.setEnabled(pStat);
        Ac15.setEnabled(pStat);
        Ac16.setEnabled(pStat);
        Ac17.setEnabled(pStat);
        
        txtAction1.setEnabled(pStat);
        txtAction2.setEnabled(pStat);
        txtAction3.setEnabled(pStat);
        txtAction4.setEnabled(pStat);
        txtAction5.setEnabled(pStat);
        txtAction6.setEnabled(pStat);
        txtAction7.setEnabled(pStat);
        txtAction8.setEnabled(pStat);
        txtAction9.setEnabled(pStat);
        txtAction10.setEnabled(pStat);
        txtAction11.setEnabled(pStat);
        txtAction12.setEnabled(pStat);
        txtAction13.setEnabled(pStat);
        txtAction14.setEnabled(pStat);
        txtAction15.setEnabled(pStat);
        txtAction16.setEnabled(pStat);
        txtAction17.setEnabled(pStat);
        
        AcNoAction.setEnabled(pStat);
        
        SetupApproval();
        btnCompany.setEnabled(false);
        btnParty.setEnabled(false);
        txtPartyBearer.setEnabled(false);
        
        Select2.setEnabled(false);
        Select3.setEnabled(false);
        Select4.setEnabled(false);
        Select5.setEnabled(false);
        Select6.setEnabled(false);
        Select7.setEnabled(false);
        Select8.setEnabled(false);
        Select9.setEnabled(false);
        Select10.setEnabled(false);
                
        if(EditMode == EITLERPGLOBAL.ADD || EditMode == EITLERPGLOBAL.EDIT)
        {
            if (clsHierarchy.IsCreator(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
                btnCompany.setEnabled(true);
                btnParty.setEnabled(true);
                txtPartyBearer.setEnabled(true);
                
                Select2.setEnabled(true);
                Select3.setEnabled(true);
                Select4.setEnabled(true);
                Select5.setEnabled(true);
                Select6.setEnabled(true);
                Select7.setEnabled(true);
                Select8.setEnabled(true);
                Select9.setEnabled(true);
                Select10.setEnabled(true);
            }
        }
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
        feltOrder.setAttribute("D_REFERENCE", REFERENCE.getSelectedItem());
        feltOrder.setAttribute("D_REFERENCE_DATE", REF_DATE.getText());
        feltOrder.setAttribute("D_P_O_NO", P_O_NO.getText());
        feltOrder.setAttribute("D_P_O_DATE", P_O_DATE.getText());
        feltOrder.setAttribute("D_REMARK", REMARK.getText());

        feltOrder.setAttribute("D_PIECE_NO", DataModel.getValueByVariable("PIECE_NO", 0));
        feltOrder.setAttribute("D_PRODUCT_NO", DataModel.getValueByVariable("PRODUCT", 0));
        feltOrder.setAttribute("D_PRODUCT_DESC", DataModel.getValueByVariable("DESCRIPTION", 0));
        feltOrder.setAttribute("D_MACHINE_NO", DataModel.getValueByVariable("MACHINE_NO", 0));
        feltOrder.setAttribute("D_POSITION_NO", DataModel.getValueByVariable("POSITION", 0));
        feltOrder.setAttribute("D_POSITION_DESC", DataModel.getValueByVariable("POSITION_DESC", 0));
        feltOrder.setAttribute("D_POS_DESIGN_NO", DataModel.getValueByVariable("POS_DESIGN_NO", 0));
        feltOrder.setAttribute("D_UPN", DataModel.getValueByVariable("UPN", 0));
        feltOrder.setAttribute("D_STYLE_NO", DataModel.getValueByVariable("STYLE", 0));

        feltOrder.setAttribute("D_GROUP", DataModel.getValueByVariable("GROUP", 0));
        feltOrder.setAttribute("D_LENGTH", DataModel.getValueByVariable("LENGTH", 0));
        feltOrder.setAttribute("D_WIDTH", DataModel.getValueByVariable("WIDTH", 0));
        feltOrder.setAttribute("D_GSM", DataModel.getValueByVariable("GSM", 0));
        feltOrder.setAttribute("D_THORITICAL_WEIGHT", DataModel.getValueByVariable("THORTICAL_WEIGHT", 0));
        feltOrder.setAttribute("D_SQ_MTR", DataModel.getValueByVariable("SQ_MTR", 0));
        feltOrder.setAttribute("D_SYN_PER", DataModel.getValueByVariable("SYN", 0));
        feltOrder.setAttribute("D_OV_RATE", DataModel.getValueByVariable("OV_RATE", 0));
        
        
        feltOrder.setAttribute("SURCHARGE_PER", DataModel.getValueByVariable("SURCHARGE_PER", 0));
        feltOrder.setAttribute("SURCHARGE_RATE", DataModel.getValueByVariable("SURCHARGE_RATE", 0));
        feltOrder.setAttribute("GROSS_RATE", DataModel.getValueByVariable("GROSS_RATE", 0));
        
        
        feltOrder.setAttribute("D_OV_BAS_AMOUNT", DataModel.getValueByVariable("OV_BAS_AMOUNT", 0));
        feltOrder.setAttribute("D_OV_CHEM_TRT_CHG", "0");
        feltOrder.setAttribute("D_OV_SPIRAL_CHG", "0");
        feltOrder.setAttribute("D_OV_PIN_CHG", "0");
        //feltOrder.setAttribute("D_OV_SEAM_CHG", "0");
        feltOrder.setAttribute("D_OV_SEAM_CHG", DataModel.getValueByVariable("OV_SEAM_CHG", 0));
        feltOrder.setAttribute("D_OV_AMT", DataModel.getValueByVariable("OV_AMT", 0));

        if (EditMode != EITLERPGLOBAL.EDIT) {
            feltOrder.setAttribute("ORIGINAL_PARTY_CODE", PR_PARTY_CODE);
            //feltOrder.setAttribute("ORIGINAL_PARTY_NAME","");
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
            
            feltOrder.setAttribute("EXIST_SURCHARGE_PER", inv_calc_existing.getFicSurcharge_per()+"");
            feltOrder.setAttribute("EXIST_SURCHARGE_RATE", inv_calc_existing.getFicSurcharge_rate()+"");
            feltOrder.setAttribute("EXIST_GROSS_RATE", inv_calc_existing.getFicGrossRate()+"");
            
        }

        feltOrder.setAttribute("DEBIT_NOTE_NO", DataModel.getValueByVariable("DB_NOTE", 0));
        feltOrder.setAttribute("DEBIT_AMT", DataModel.getValueByVariable("DB_AMT", 0));
        if (btnCompany.isSelected()) {
            feltOrder.setAttribute("COST_BEARER", "SDML");
            feltOrder.setAttribute("BEARER_PARTY_CODE", "");
            feltOrder.setAttribute("BEARER_PARTY_NAME", "");
        } else if (btnParty.isSelected()) {
            feltOrder.setAttribute("COST_BEARER", "PARTY");
            feltOrder.setAttribute("BEARER_PARTY_CODE", txtPartyBearer.getText());
            feltOrder.setAttribute("BEARER_PARTY_NAME", txtPartyBearerName.getText());
        } else if (btnPartialPayment.isSelected()) {
            feltOrder.setAttribute("COST_BEARER", "PARTIAL");
            feltOrder.setAttribute("BEARER_PARTY_CODE", txtPartyBearer.getText());
            feltOrder.setAttribute("BEARER_PARTY_NAME", txtPartyBearerName.getText());
            
        }
        
        feltOrder.setAttribute("SDML_PER", txtSDMLPer.getText());
        feltOrder.setAttribute("SDML_AMT", txtBearCompanyAmt.getText());
        feltOrder.setAttribute("PARTY_PER", txtPartyPer.getText());
        feltOrder.setAttribute("PARTY_AMT", txtBearPartyAmt.getText());
        

        feltOrder.setAttribute("CONTACT_PERSON", txtContactPerson.getText());
        feltOrder.setAttribute("EMAIL_ID", txtEmailId.getText());
        feltOrder.setAttribute("EMAIL_ID2", txtEmailId2.getText());
        feltOrder.setAttribute("EMAIL_ID3", txtEmailId3.getText());
        feltOrder.setAttribute("PHONE_NUMBER", txtPhoneNo.getText());
        
        feltOrder.setAttribute("EMAIL_UPDATE_TO_PM", chbEmailUpdate.isSelected());
        
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
        
        
        feltOrder.setAttribute("OA_NO", DataModel.getValueByVariable("OA_NO", 0));
        feltOrder.setAttribute("OA_DATE", DataModel.getValueByVariable("OA_DATE", 0));
        feltOrder.setAttribute("OC_MONTHYEAR", DataModel.getValueByVariable("OC_MONTHYEAR", 0));
        feltOrder.setAttribute("OC_NO", DataModel.getValueByVariable("OC_NO", 0));
        feltOrder.setAttribute("OC_DATE", DataModel.getValueByVariable("OC_DATE", 0));
        feltOrder.setAttribute("REQ_MONTH", DataModel.getValueByVariable("REQ_MONTH", 0));

        feltOrder.setAttribute("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        feltOrder.setAttribute("CANCELED", false);

        feltOrder.setAttribute("BASE_ORDER_AMT", DataModel.getValueByVariable("BASE_ORDER_AMT", 0));
        feltOrder.setAttribute("BASE_EXISTING_PIECE_AMT", DataModel.getValueByVariable("BASE_EXISTING_PIECE_AMT", 0));

        feltOrder.setAttribute("ACTION1", ltrim(txtAction1.getText()));
        feltOrder.setAttribute("ACTION2", ltrim(txtAction2.getText()));
        feltOrder.setAttribute("ACTION3", ltrim(txtAction3.getText()));
        feltOrder.setAttribute("ACTION4", ltrim(txtAction4.getText()));
        feltOrder.setAttribute("ACTION5", ltrim(txtAction5.getText()));
        feltOrder.setAttribute("ACTION6", ltrim(txtAction6.getText()));
        feltOrder.setAttribute("ACTION7", ltrim(txtAction7.getText()));
        feltOrder.setAttribute("ACTION8", ltrim(txtAction8.getText()));
        feltOrder.setAttribute("ACTION9", ltrim(txtAction9.getText()));
        feltOrder.setAttribute("ACTION10", ltrim(txtAction10.getText()));
        feltOrder.setAttribute("ACTION11", ltrim(txtAction11.getText()));
        feltOrder.setAttribute("ACTION12", ltrim(txtAction12.getText()));
        feltOrder.setAttribute("ACTION13", ltrim(txtAction13.getText()));
        feltOrder.setAttribute("ACTION14", ltrim(txtAction14.getText()));
        feltOrder.setAttribute("ACTION15", ltrim(txtAction15.getText()));
        feltOrder.setAttribute("ACTION16", ltrim(txtAction16.getText()));
        feltOrder.setAttribute("ACTION17", ltrim(txtAction17.getText()));

        feltOrder.setAttribute("DEBITMEMO_NO2", DebitMemoNo2.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT2", DebitMemoAmt2.getText());
        
        feltOrder.setAttribute("DEBITMEMO_NO3", DebitMemoNo3.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT3", DebitMemoAmt3.getText());
        
        feltOrder.setAttribute("DEBITMEMO_NO4", DebitMemoNo4.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT4", DebitMemoAmt4.getText());
        
        feltOrder.setAttribute("DEBITMEMO_NO5", DebitMemoNo5.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT5", DebitMemoAmt5.getText());
        
        feltOrder.setAttribute("DEBITMEMO_NO6", DebitMemoNo6.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT6", DebitMemoAmt6.getText());
        
        feltOrder.setAttribute("DEBITMEMO_NO7", DebitMemoNo7.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT7", DebitMemoAmt7.getText());
        
        feltOrder.setAttribute("DEBITMEMO_NO8", DebitMemoNo8.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT8", DebitMemoAmt8.getText());
        
        feltOrder.setAttribute("DEBITMEMO_NO9", DebitMemoNo9.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT9", DebitMemoAmt9.getText());
        
        feltOrder.setAttribute("DEBITMEMO_NO10", DebitMemoNo10.getText());
        feltOrder.setAttribute("DEBITMEMO_AMT10", DebitMemoAmt10.getText());
        
        feltOrder.setAttribute("PRIOR_APPROVAL_NO", lblPriorApprovalNo.getText());
        feltOrder.setAttribute("PRIOR_APPROVAL_DATE", lblPriorApprovalDate.getText());
        
        
        feltOrder.setAttribute("NO_ACTION",AcNoAction.isSelected());
        
        feltOrder.setAttribute("RESCH_CAN_REMARK",txtReschCanRemark.getText());
        feltOrder.setAttribute("RESCH_CAN_REMARK_NEW_PARTY",txtNewPartyRemark.getText());
        
        
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
            //System.out.println("s.charAt(i)  "+s.charAt(i));
            i++;
        }
        return s.substring(i);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Ac1;
    private javax.swing.JCheckBox Ac10;
    private javax.swing.JCheckBox Ac11;
    private javax.swing.JCheckBox Ac12;
    private javax.swing.JCheckBox Ac13;
    private javax.swing.JCheckBox Ac14;
    private javax.swing.JCheckBox Ac15;
    private javax.swing.JCheckBox Ac16;
    private javax.swing.JCheckBox Ac17;
    private javax.swing.JCheckBox Ac2;
    private javax.swing.JCheckBox Ac3;
    private javax.swing.JCheckBox Ac4;
    private javax.swing.JCheckBox Ac5;
    private javax.swing.JCheckBox Ac6;
    private javax.swing.JCheckBox Ac7;
    private javax.swing.JCheckBox Ac8;
    private javax.swing.JCheckBox Ac9;
    private javax.swing.JCheckBox AcNoAction;
    private javax.swing.JTextField CITY;
    private javax.swing.JTextField COUNTRY;
    private javax.swing.JTextField DISTRICT;
    private javax.swing.JTextField DebitMemoAmt10;
    private javax.swing.JTextField DebitMemoAmt2;
    private javax.swing.JTextField DebitMemoAmt3;
    private javax.swing.JTextField DebitMemoAmt4;
    private javax.swing.JTextField DebitMemoAmt5;
    private javax.swing.JTextField DebitMemoAmt6;
    private javax.swing.JTextField DebitMemoAmt7;
    private javax.swing.JTextField DebitMemoAmt8;
    private javax.swing.JTextField DebitMemoAmt9;
    private javax.swing.JTextField DebitMemoNo10;
    private javax.swing.JTextField DebitMemoNo2;
    private javax.swing.JTextField DebitMemoNo3;
    private javax.swing.JTextField DebitMemoNo4;
    private javax.swing.JTextField DebitMemoNo5;
    private javax.swing.JTextField DebitMemoNo6;
    private javax.swing.JTextField DebitMemoNo7;
    private javax.swing.JTextField DebitMemoNo8;
    private javax.swing.JTextField DebitMemoNo9;
    private javax.swing.JTextField ORDER_VALUE;
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
    private javax.swing.JButton Select10;
    private javax.swing.JButton Select2;
    private javax.swing.JButton Select3;
    private javax.swing.JButton Select4;
    private javax.swing.JButton Select5;
    private javax.swing.JButton Select6;
    private javax.swing.JButton Select7;
    private javax.swing.JButton Select8;
    private javax.swing.JButton Select9;
    private javax.swing.JButton Show_DbNote;
    private javax.swing.JButton Show_Memo10;
    private javax.swing.JButton Show_Memo2;
    private javax.swing.JButton Show_Memo3;
    private javax.swing.JButton Show_Memo4;
    private javax.swing.JButton Show_Memo5;
    private javax.swing.JButton Show_Memo6;
    private javax.swing.JButton Show_Memo7;
    private javax.swing.JButton Show_Memo8;
    private javax.swing.JButton Show_Memo9;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JPanel StatusPanel1;
    private javax.swing.JPanel StatusPanel2;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable Table;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableExist;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JRadioButton btnCompany;
    private javax.swing.JButton btnOpenPriorApproval;
    private javax.swing.JRadioButton btnPartialPayment;
    private javax.swing.JRadioButton btnParty;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JCheckBox chbEmailUpdate;
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
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblDBNOTE;
    private javax.swing.JLabel lblDbAmt;
    private javax.swing.JLabel lblDbNote;
    private javax.swing.JLabel lblInchargeName;
    private javax.swing.JLabel lblPartyBearer;
    private javax.swing.JLabel lblPriorApprovalDate;
    private javax.swing.JLabel lblPriorApprovalNo;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblExistParty;
    private javax.swing.JTable tblNewParty;
    private javax.swing.JTextField txtAction1;
    private javax.swing.JTextField txtAction10;
    private javax.swing.JTextField txtAction11;
    private javax.swing.JTextField txtAction12;
    private javax.swing.JTextField txtAction13;
    private javax.swing.JTextField txtAction14;
    private javax.swing.JTextField txtAction15;
    private javax.swing.JTextField txtAction16;
    private javax.swing.JTextField txtAction17;
    private javax.swing.JTextField txtAction2;
    private javax.swing.JTextField txtAction3;
    private javax.swing.JTextField txtAction4;
    private javax.swing.JTextField txtAction5;
    private javax.swing.JTextField txtAction6;
    private javax.swing.JTextField txtAction7;
    private javax.swing.JTextField txtAction8;
    private javax.swing.JTextField txtAction9;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtBearCompanyAmt;
    private javax.swing.JTextField txtBearPartyAmt;
    private javax.swing.JTextField txtContactPerson;
    private javax.swing.JTextField txtEmailId;
    private javax.swing.JTextField txtEmailId2;
    private javax.swing.JTextField txtEmailId3;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtNewPartyRemark;
    private javax.swing.JTextField txtPartyBearer;
    private javax.swing.JTextField txtPartyBearerName;
    private javax.swing.JTextField txtPartyPer;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtPieceNo;
    private javax.swing.JTextField txtReschCanRemark;
    private javax.swing.JTextField txtSDMLPer;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables
    private void PreviewReport() {
        String sql;

        HashMap Parameters = new HashMap();
        

        TTable objData = new TTable();
        objData.AddColumn("PARTY_CODE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("OPENING_BAL");
        objData.AddColumn("INVOICE_AMT");
        objData.AddColumn("CLOSING_BAL");
        objData.AddColumn("VOUCHER");
        
        objData.AddColumn("SD_ORDER_NO");
        objData.AddColumn("SD_ORDER_DATE");
        objData.AddColumn("D_REGION");
        objData.AddColumn("D_SALES_ENGINEER");
        objData.AddColumn("D_PARTY_CODE");
        objData.AddColumn("D_PARTY_NAME");
        objData.AddColumn("D_REFERENCE");
        objData.AddColumn("D_REFERENCE_DATE");
        objData.AddColumn("D_P_O_NO");
        objData.AddColumn("D_P_O_DATE");
        objData.AddColumn("D_REMARK");
        objData.AddColumn("D_PIECE_NO");
        objData.AddColumn("D_PRODUCT_NO");
        objData.AddColumn("D_PRODUCT_DESC");
        objData.AddColumn("D_MACHINE_NO");
        objData.AddColumn("D_POSITION_NO");
        objData.AddColumn("D_POSITION_DESC");
        objData.AddColumn("D_STYLE_NO");
        objData.AddColumn("D_GROUP");
        objData.AddColumn("D_LENGTH");
        objData.AddColumn("D_WIDTH");
        objData.AddColumn("D_GSM");
        objData.AddColumn("D_THORITICAL_WEIGHT");
        objData.AddColumn("D_SQ_MTR");
        objData.AddColumn("D_REQ_MONTH");
        objData.AddColumn("D_SYN_PER");
        objData.AddColumn("D_OV_RATE");
        objData.AddColumn("ORIGINAL_PARTY_CODE");
        objData.AddColumn("ORIGINAL_PARTY_NAME");
        objData.AddColumn("ORIGINAL_PIECE_NO");
        objData.AddColumn("ORIGINAL_PRODUCT_NO");
        objData.AddColumn("ORIGINAL_MACHINE_NO");
        objData.AddColumn("ORIGINAL_POSITION_NO");
        objData.AddColumn("ORIGINAL_STYLE_NO");
        objData.AddColumn("ORIGINAL_GROUP");
        objData.AddColumn("ORIGINAL_LENGTH");
        objData.AddColumn("ORIGINAL_WIDTH"); 
        objData.AddColumn("ORIGINAL_GSM");
        objData.AddColumn("ORIGINAL_THORITICAL_WEIGHT");
        objData.AddColumn("ORIGINAL_SQ_MTR"); 
        objData.AddColumn("DEBIT_NOTE_NO");
        objData.AddColumn("DEBIT_AMT");
        objData.AddColumn("COST_BEARER"); 
        objData.AddColumn("BEARER_PARTY_CODE");
        objData.AddColumn("BEARER_PARTY_NAME");
        objData.AddColumn("DIFFERENCE_AMT"); 
        objData.AddColumn("L_G_AMT");
        objData.AddColumn("APPROVED");
        objData.AddColumn("CANCELED"); 
        objData.AddColumn("PR_BILL_LENGTH");
        objData.AddColumn("PR_BILL_WIDTH"); 
        objData.AddColumn("PR_BILL_WEIGHT");
        objData.AddColumn("PR_BILL_SQMTR"); 
        objData.AddColumn("PR_BILL_GSM");
        objData.AddColumn("PR_BILL_PRODUCT_CODE"); 
        objData.AddColumn("BASE_ORDER_AMT");
        objData.AddColumn("BASE_EXISTING_PIECE_AMT"); 
        objData.AddColumn("ACTION1");
        objData.AddColumn("ACTION2"); 
        objData.AddColumn("ACTION3");
        objData.AddColumn("ACTION4"); 
        objData.AddColumn("ACTION5");
        objData.AddColumn("ACTION6"); 
        objData.AddColumn("ACTION7");
        objData.AddColumn("ACTION8"); 
        objData.AddColumn("ACTION9");
        objData.AddColumn("ACTION10"); 
        objData.AddColumn("ACTION11");
        objData.AddColumn("ACTION12"); 
        objData.AddColumn("ACTION13");
        objData.AddColumn("ACTION14"); 
        objData.AddColumn("ACTION15");
        objData.AddColumn("ACTION16"); 
        objData.AddColumn("ACTION17");

        objData.AddColumn("DISCOUNT_PER");
        objData.AddColumn("DISCOUNT_AMT"); 
        objData.AddColumn("DEBITMEMO_NO2");
        objData.AddColumn("DEBITMEMO_AMT2"); 
        objData.AddColumn("DEBITMEMO_NO3");
        objData.AddColumn("DEBITMEMO_AMT3"); 
        objData.AddColumn("DEBITMEMO_NO4");
        objData.AddColumn("DEBITMEMO_AMT4"); 
        objData.AddColumn("DEBITMEMO_NO5");
        objData.AddColumn("DEBITMEMO_AMT5"); 
        objData.AddColumn("DEBITMEMO_NO6");
        objData.AddColumn("DEBITMEMO_AMT6"); 
        objData.AddColumn("DEBITMEMO_NO7");
        objData.AddColumn("DEBITMEMO_AMT7"); 
        objData.AddColumn("DEBITMEMO_NO8");
        objData.AddColumn("DEBITMEMO_AMT8"); 

        objData.AddColumn("ORIGINAL_PIECE_NO");
        objData.AddColumn("NEW_LENGTH"); 
        objData.AddColumn("ORGINAL_LENGTH");
        objData.AddColumn("NEW_WIDTH"); 
        objData.AddColumn("ORGINAL_WIDTH");
        objData.AddColumn("NEW_GSM"); 
        objData.AddColumn("ORGINAL_GSM");
        objData.AddColumn("NEW_SQMTR"); 
        objData.AddColumn("ORGINAL_SQMTR");
        objData.AddColumn("NEW_WEIGHT"); 
        objData.AddColumn("ORGINAL_WEIGHT");
        objData.AddColumn("NEW_MACHINE_NO");
        objData.AddColumn("ORIGINAL_MACHINE_NO"); 
        objData.AddColumn("NEW_POSITION_NO");
        objData.AddColumn("ORIGINAL_POSITION_NO");
        objData.AddColumn("NEW_POSITION_DESC"); 
        objData.AddColumn("ORIGINAL_POSITION_DESC");
        objData.AddColumn("NEW_STYLE");
        objData.AddColumn("ORIGINAL_STYLE");
        objData.AddColumn("NEW_GROUP"); 
        objData.AddColumn("ORIGINAL_GROUP");
        objData.AddColumn("NEW_PRODUCT_CODE");
        objData.AddColumn("ORIGINAL_PRODUCT_CODE"); 
        objData.AddColumn("NEW_PARTY_CODE");
        objData.AddColumn("ORIGINAL_PARTY_CODE");
        objData.AddColumn("ORIGINAL_PARTY_NAME"); 
        objData.AddColumn("DATA_ACTION15");
        objData.AddColumn("DATA_ACTION16");
        
        
        try {

            String strSQL = "";
            ResultSet rsReport;

            //Retrieve data
            strSQL = "SELECT  D.*,ORIGINAL_PIECE_NO,D.PR_BILL_LENGTH AS NEW_LENGTH,   B.PR_BILL_LENGTH AS ORGINAL_LENGTH, "
                    + " D.PR_BILL_WIDTH AS NEW_WIDTH,   B.PR_BILL_WIDTH AS ORGINAL_WIDTH,   D.PR_BILL_GSM AS NEW_GSM,   "
                    + " B.PR_BILL_GSM AS ORGINAL_GSM,   D.PR_BILL_SQMTR AS NEW_SQMTR,   B.PR_BILL_SQMTR AS ORGINAL_SQMTR,     "
                    + " D.PR_BILL_WEIGHT AS NEW_WEIGHT,   B.PR_BILL_WEIGHT AS ORGINAL_WEIGHT,       "
                    + " A.PR_MACHINE_NO AS NEW_MACHINE_NO,    B.PR_MACHINE_NO AS ORIGINAL_MACHINE_NO,   "
                    + " A.PR_POSITION_NO AS NEW_POSITION_NO,B.PR_POSITION_NO AS ORIGINAL_POSITION_NO,   "
                    + " C_NEW.POSITION_DESC AS NEW_POSITION_DESC,    C_ORIGINAL.POSITION_DESC AS ORIGINAL_POSITION_DESC,   "
                    + " A.PR_STYLE AS NEW_STYLE,     B.PR_STYLE AS ORIGINAL_STYLE,   A.PR_GROUP AS NEW_GROUP,      "
                    + " B.PR_GROUP AS ORIGINAL_GROUP,  A.PR_PRODUCT_CODE AS NEW_PRODUCT_CODE,      B.PR_PRODUCT_CODE AS ORIGINAL_PRODUCT_CODE,  "
                    + " A.PR_PARTY_CODE AS NEW_PARTY_CODE,     B.PR_PARTY_CODE AS ORIGINAL_PARTY_CODE,     EXIST_PARTY.PARTY_NAME AS ORIGINAL_PARTY_NAME,   "
                    + " IF(ACTION15 = '','NO','YES') AS DATA_ACTION15,   IF(ACTION16 = '','NO','YES') AS DATA_ACTION16      "
                    + " FROM PRODUCTION.FELT_SALES_ORDER_DIVERSION D "
                    + " LEFT JOIN PRODUCTION.FELT_SALES_PIECE_REGISTER A ON D_PIECE_NO=A.PR_PIECE_NO "
                    + " LEFT JOIN PRODUCTION.FELT_SALES_PIECE_REGISTER B ON ORIGINAL_PIECE_NO=B.PR_PIECE_NO "
                    + " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST C_NEW ON A.PR_POSITION_NO=C_NEW.POSITION_NO "
                    + " LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER EXIST_PARTY ON EXIST_PARTY.PARTY_CODE=ORIGINAL_PARTY_CODE  "
                    + " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST C_ORIGINAL ON B.PR_POSITION_NO=C_ORIGINAL.POSITION_NO  "
                    + " where D.SD_ORDER_NO='"+S_O_NO.getText()+"'";
            
            System.out.println("SQL : "+strSQL);
            rsReport = data.getResult(strSQL);
            rsReport.first();

            if (rsReport.getRow() > 0) {

                while (!rsReport.isAfterLast()) {
                    TRow objRow = new TRow();

                    objRow.setValue("SD_ORDER_NO", rsReport.getString("SD_ORDER_NO"));
                    objRow.setValue("SD_ORDER_DATE", rsReport.getString("SD_ORDER_DATE"));
                    objRow.setValue("D_REGION", rsReport.getString("D_REGION"));
                    objRow.setValue("D_SALES_ENGINEER", rsReport.getString("D_SALES_ENGINEER"));
                    objRow.setValue("D_PARTY_CODE", rsReport.getString("D_PARTY_CODE"));
                    objRow.setValue("D_PARTY_NAME", rsReport.getString("D_PARTY_NAME"));
                    objRow.setValue("D_REFERENCE", rsReport.getString("D_REFERENCE"));
                    objRow.setValue("D_REFERENCE_DATE", rsReport.getString("D_REFERENCE_DATE"));
                    objRow.setValue("D_P_O_NO", rsReport.getString("D_P_O_NO"));
                    objRow.setValue("D_P_O_DATE", rsReport.getString("D_P_O_DATE"));
                    objRow.setValue("D_REMARK", rsReport.getString("D_REMARK"));
                    objRow.setValue("D_PIECE_NO", rsReport.getString("D_PIECE_NO"));
                    objRow.setValue("D_PRODUCT_NO", rsReport.getString("D_PRODUCT_NO"));
                    objRow.setValue("D_PRODUCT_DESC", rsReport.getString("D_PRODUCT_DESC"));
                    objRow.setValue("D_MACHINE_NO", rsReport.getString("D_MACHINE_NO"));
                    objRow.setValue("D_POSITION_NO", rsReport.getString("D_POSITION_NO"));
                    objRow.setValue("D_POSITION_DESC", rsReport.getString("D_POSITION_DESC"));
                    objRow.setValue("D_STYLE_NO", rsReport.getString("D_STYLE_NO"));
                    objRow.setValue("D_GROUP", rsReport.getString("D_GROUP"));
                    objRow.setValue("D_LENGTH", rsReport.getString("D_LENGTH"));
                    objRow.setValue("D_WIDTH", rsReport.getString("D_WIDTH"));
                    objRow.setValue("D_GSM", rsReport.getString("D_GSM"));
                    objRow.setValue("D_THORITICAL_WEIGHT", rsReport.getString("D_THORITICAL_WEIGHT"));
                    objRow.setValue("D_SQ_MTR", rsReport.getString("D_SQ_MTR"));
                    objRow.setValue("D_REQ_MONTH", rsReport.getString("D_REQ_MONTH"));
                    objRow.setValue("D_SYN_PER", rsReport.getString("D_SYN_PER"));
                    objRow.setValue("D_OV_RATE", rsReport.getString("D_OV_RATE"));  
                    objRow.setValue("ORIGINAL_PARTY_CODE", rsReport.getString("ORIGINAL_PARTY_CODE"));
                    objRow.setValue("ORIGINAL_PARTY_NAME", rsReport.getString("ORIGINAL_PARTY_NAME"));
                    objRow.setValue("ORIGINAL_PIECE_NO", rsReport.getString("ORIGINAL_PIECE_NO"));
                    objRow.setValue("ORIGINAL_PRODUCT_NO", rsReport.getString("ORIGINAL_PRODUCT_NO"));
                    objRow.setValue("ORIGINAL_MACHINE_NO", rsReport.getString("ORIGINAL_MACHINE_NO")); 
                    objRow.setValue("ORIGINAL_POSITION_NO", rsReport.getString("ORIGINAL_POSITION_NO"));
                    objRow.setValue("ORIGINAL_STYLE_NO", rsReport.getString("ORIGINAL_STYLE_NO"));
                    objRow.setValue("ORIGINAL_GROUP", rsReport.getString("ORIGINAL_GROUP"));
                    objRow.setValue("ORIGINAL_LENGTH", rsReport.getString("ORIGINAL_LENGTH"));
                    objRow.setValue("ORIGINAL_WIDTH", rsReport.getString("ORIGINAL_WIDTH")); 
                    objRow.setValue("ORIGINAL_GSM", rsReport.getString("ORIGINAL_GSM"));
                    objRow.setValue("ORIGINAL_THORITICAL_WEIGHT", rsReport.getString("ORIGINAL_THORITICAL_WEIGHT"));
                    objRow.setValue("ORIGINAL_SQ_MTR", rsReport.getString("ORIGINAL_SQ_MTR")); 
                    objRow.setValue("DEBIT_NOTE_NO", rsReport.getString("DEBIT_NOTE_NO"));
                    objRow.setValue("DEBIT_AMT", rsReport.getString("DEBIT_AMT"));
                    objRow.setValue("COST_BEARER", rsReport.getString("COST_BEARER")); 
                    objRow.setValue("BEARER_PARTY_CODE", rsReport.getString("BEARER_PARTY_CODE"));
                    objRow.setValue("BEARER_PARTY_NAME", rsReport.getString("BEARER_PARTY_NAME"));
                    objRow.setValue("DIFFERENCE_AMT", rsReport.getString("DIFFERENCE_AMT")); 
                    objRow.setValue("L_G_AMT", rsReport.getString("L_G_AMT"));
                    objRow.setValue("APPROVED", rsReport.getString("APPROVED"));
                    objRow.setValue("CANCELED", rsReport.getString("CANCELED")); 
                    objRow.setValue("PR_BILL_LENGTH", rsReport.getString("PR_BILL_LENGTH"));
                    objRow.setValue("PR_BILL_WIDTH", rsReport.getString("PR_BILL_WIDTH")); 
                    objRow.setValue("PR_BILL_WEIGHT", rsReport.getString("PR_BILL_WEIGHT"));
                    objRow.setValue("PR_BILL_SQMTR", rsReport.getString("PR_BILL_SQMTR")); 
                    objRow.setValue("PR_BILL_GSM", rsReport.getString("PR_BILL_GSM"));
                    objRow.setValue("PR_BILL_PRODUCT_CODE", rsReport.getString("PR_BILL_PRODUCT_CODE")); 
                    objRow.setValue("BASE_ORDER_AMT", rsReport.getString("BASE_ORDER_AMT"));
                    objRow.setValue("BASE_EXISTING_PIECE_AMT", rsReport.getString("BASE_EXISTING_PIECE_AMT")); 
                    objRow.setValue("ACTION1", rsReport.getString("ACTION1"));
                    objRow.setValue("ACTION2", rsReport.getString("ACTION2")); 
                    objRow.setValue("ACTION3", rsReport.getString("ACTION3"));
                    objRow.setValue("ACTION4", rsReport.getString("ACTION4")); 
                    objRow.setValue("ACTION5", rsReport.getString("ACTION5"));
                    objRow.setValue("ACTION6", rsReport.getString("ACTION6")); 
                    objRow.setValue("ACTION7", rsReport.getString("ACTION7"));
                    objRow.setValue("ACTION8", rsReport.getString("ACTION8")); 
                    objRow.setValue("ACTION9", rsReport.getString("ACTION9"));
                    objRow.setValue("ACTION10", rsReport.getString("ACTION10")); 
                    objRow.setValue("ACTION11", rsReport.getString("ACTION11"));
                    objRow.setValue("ACTION12", rsReport.getString("ACTION12")); 
                    objRow.setValue("ACTION13", rsReport.getString("ACTION13"));
                    objRow.setValue("ACTION14", rsReport.getString("ACTION14")); 
                    objRow.setValue("ACTION15", rsReport.getString("ACTION15"));
                    objRow.setValue("ACTION16", rsReport.getString("ACTION16")); 
                    objRow.setValue("ACTION17", rsReport.getString("ACTION17"));
                    
                    objRow.setValue("DISCOUNT_PER", rsReport.getString("DISCOUNT_PER"));
                    objRow.setValue("DISCOUNT_AMT", rsReport.getString("DISCOUNT_AMT")); 
                    objRow.setValue("DEBITMEMO_NO2", rsReport.getString("DEBITMEMO_NO2"));
                    objRow.setValue("DEBITMEMO_AMT2", rsReport.getString("DEBITMEMO_AMT2")); 
                    objRow.setValue("DEBITMEMO_NO3", rsReport.getString("DEBITMEMO_NO3"));
                    objRow.setValue("DEBITMEMO_AMT3", rsReport.getString("DEBITMEMO_AMT3")); 
                    objRow.setValue("DEBITMEMO_NO4", rsReport.getString("DEBITMEMO_NO4"));
                    objRow.setValue("DEBITMEMO_AMT4", rsReport.getString("DEBITMEMO_AMT4")); 
                    objRow.setValue("DEBITMEMO_NO5", rsReport.getString("DEBITMEMO_NO5"));
                    objRow.setValue("DEBITMEMO_AMT5", rsReport.getString("DEBITMEMO_AMT5")); 
                    objRow.setValue("DEBITMEMO_NO6", rsReport.getString("DEBITMEMO_NO6"));
                    objRow.setValue("DEBITMEMO_AMT6", rsReport.getString("DEBITMEMO_AMT6")); 
                    objRow.setValue("DEBITMEMO_NO7", rsReport.getString("DEBITMEMO_NO7"));
                    objRow.setValue("DEBITMEMO_AMT7", rsReport.getString("DEBITMEMO_AMT7")); 
                    objRow.setValue("DEBITMEMO_NO8", rsReport.getString("DEBITMEMO_NO8"));
                    objRow.setValue("DEBITMEMO_AMT8", rsReport.getString("DEBITMEMO_AMT8")); 
                    
                    objRow.setValue("ORIGINAL_PIECE_NO", rsReport.getString("ORIGINAL_PIECE_NO"));
                    objRow.setValue("NEW_LENGTH", rsReport.getString("NEW_LENGTH")); 
                    objRow.setValue("ORGINAL_LENGTH", rsReport.getString("ORGINAL_LENGTH"));
                    objRow.setValue("NEW_WIDTH", rsReport.getString("NEW_WIDTH")); 
                    objRow.setValue("ORGINAL_WIDTH", rsReport.getString("ORGINAL_WIDTH"));
                    objRow.setValue("NEW_GSM", rsReport.getString("NEW_GSM")); 
                    objRow.setValue("ORGINAL_GSM", rsReport.getString("ORGINAL_GSM"));
                    objRow.setValue("NEW_SQMTR", rsReport.getString("NEW_SQMTR")); 
                    objRow.setValue("ORGINAL_SQMTR", rsReport.getString("ORGINAL_SQMTR"));
                    objRow.setValue("NEW_WEIGHT", rsReport.getString("NEW_WEIGHT")); 
                    objRow.setValue("ORGINAL_WEIGHT", rsReport.getString("ORGINAL_WEIGHT"));
                    objRow.setValue("NEW_MACHINE_NO", rsReport.getString("NEW_MACHINE_NO"));
                    objRow.setValue("ORIGINAL_MACHINE_NO", rsReport.getString("ORIGINAL_MACHINE_NO")); 
                    objRow.setValue("NEW_POSITION_NO", rsReport.getString("NEW_POSITION_NO"));
                    objRow.setValue("ORIGINAL_POSITION_NO", rsReport.getString("ORIGINAL_POSITION_NO"));
                    objRow.setValue("NEW_POSITION_DESC", rsReport.getString("NEW_POSITION_DESC")); 
                    objRow.setValue("ORIGINAL_POSITION_DESC", rsReport.getString("ORIGINAL_POSITION_DESC"));
                    objRow.setValue("NEW_STYLE", rsReport.getString("NEW_STYLE"));
                    objRow.setValue("ORIGINAL_STYLE", rsReport.getString("ORIGINAL_STYLE"));
                    objRow.setValue("NEW_GROUP", rsReport.getString("NEW_GROUP")); 
                    objRow.setValue("ORIGINAL_GROUP", rsReport.getString("ORIGINAL_GROUP"));
                    objRow.setValue("NEW_PRODUCT_CODE", rsReport.getString("NEW_PRODUCT_CODE"));
                    objRow.setValue("ORIGINAL_PRODUCT_CODE", rsReport.getString("ORIGINAL_PRODUCT_CODE")); 
                    objRow.setValue("NEW_PARTY_CODE", rsReport.getString("NEW_PARTY_CODE"));
                    objRow.setValue("ORIGINAL_PARTY_CODE", rsReport.getString("ORIGINAL_PARTY_CODE"));
                    objRow.setValue("ORIGINAL_PARTY_NAME", rsReport.getString("ORIGINAL_PARTY_NAME")); 
                    objRow.setValue("DATA_ACTION15", rsReport.getString("DATA_ACTION15"));
                    objRow.setValue("DATA_ACTION16", rsReport.getString("DATA_ACTION16"));
                    
                    
                    objData.AddRow(objRow);
                    rsReport.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
        objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/FeltSales/DIVERSION_TAGGING.rpt", Parameters, objData);

    }
}
