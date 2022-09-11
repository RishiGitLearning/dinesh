/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.OrderConfirmationMain;

import EITLERP.AppletFrame;
import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.OrderConfirmation.FrmPieceOC;
import EITLERP.FeltSales.OrderConfirmation.clsPieceOC;
import EITLERP.FeltSales.OrderConfirmation.clsPieceOCDetails;
import EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry;
import EITLERP.FeltSales.PPRSPlanning.clsPPRSEntryDetails;
import EITLERP.FeltSales.PieceRegister.FrmPOPendingOrder;
import EITLERP.FeltSales.PieceRegister.clsIncharge;
import EITLERP.FeltSales.SalesFollowup.clsValidator;
import EITLERP.FeltSales.common.SelectSortFields;
import EITLERP.JTextFieldHint;
import EITLERP.LOV;
import EITLERP.Production.FeltCreditNote.clsExcelExporter;
import EITLERP.clsSales_Party;
import EITLERP.data;
import com.sun.faces.renderkit.html_basic.ButtonRenderer;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FrmOrderConfirmationMain extends javax.swing.JApplet {

    private EITLTableModel DataModel_PPRS_Planning;
    private EITLTableModel DataModel_Planning;
    private EITLTableModel DataModel_WIP;
    private EITLTableModel DataModel_STOCK;
    private EITLTableModel DataModel_Dispatch;
    private EITLTableModel DataModel_CapacityPlanning;
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Renderer_invoiced = new EITLTableCellRenderer();
    private EITLTableCellRenderer Renderer_planning = new EITLTableCellRenderer();
    private EITLTableCellRenderer Renderer_PPRS_planning = new EITLTableCellRenderer();
    private EITLTableCellRenderer Renderer_wip = new EITLTableCellRenderer();
    private EITLTableCellRenderer Renderer_stock = new EITLTableCellRenderer();
    private EITLComboModel cmbIncharge;
    private EITLComboModel cmbIncharge_PPRS;
    private EITLComboModel modelMonthYear;
    private EITLComboModel modelMonthYear_PPRS;
    String ORDER_BY = "";
    String ORDER_BY_PPRS = "";
    private EITLComboModel cmodelProductGroup;
    private EITLComboModel cmodelProductGroup_PPRS;

    private String LOGIN_USER_TYPE = "";
    private clsExcelExporter exp = new clsExcelExporter();
    production_mfg_report4 search_check = new production_mfg_report4();
    private EITLTableCellRenderer Renderer_report = new EITLTableCellRenderer();
    String SPCL_REQ_MONTH_INVALID = "";

    /**
     * Initializes the applet FrmFeltOrder
     */
    @Override
    public void init() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
        initComponents();
        cmbIncharge = new EITLComboModel();
        cmodelProductGroup = new EITLComboModel();
        modelMonthYear = new EITLComboModel();
        cmbIncharge_PPRS = new EITLComboModel();
        cmodelProductGroup_PPRS = new EITLComboModel();
        modelMonthYear_PPRS = new EITLComboModel();
        SPCL_REQ_MONTH_INVALID = "";

//        Date d = new Date();
//        int current_month = d.getMonth();
//        if(current_month==0)
//        {
//            cmbMonth.setSelectedItem("Jan");
//        }
//        else if(current_month==1)
//        {
//            cmbMonth.setSelectedItem("Feb");
//        }
//        else if(current_month==2)
//        {
//            cmbMonth.setSelectedItem("Mar");
//        }
//        else if(current_month==3)
//        {
//            cmbMonth.setSelectedItem("Apr");
//        }
//        else if(current_month==4)
//        {
//            cmbMonth.setSelectedItem("May");
//        }
//        else if(current_month==5)
//        {
//            cmbMonth.setSelectedItem("Jun");
//        }
//        else if(current_month==6)
//        {
//            cmbMonth.setSelectedItem("Jul");
//        }
//        else if(current_month==7)
//        {
//            cmbMonth.setSelectedItem("Aug");
//        }
//        else if(current_month==8)
//        {
//            cmbMonth.setSelectedItem("Sep");
//        }
//        else if(current_month==9)
//        { 
//            cmbMonth.setSelectedItem("Oct");
//        }
//        else if(current_month==10)
//        {
//            cmbMonth.setSelectedItem("Nov");
//        }
//        else if(current_month==11)
//        {
//            cmbMonth.setSelectedItem("Dec");
//        }
        GenerateGroupCombo();
        GenerateGroupCombo_PPRS();

        GenerateCombo();
        GenerateComboMonthYear();

        GenerateCombo_PPRS();
        GenerateComboMonthYear_PPRS();

        int USER_ID = EITLERPGLOBAL.gUserID;
        int DEPT_ID = EITLERPGLOBAL.gUserDeptID;
        //System.out.println("USER = "+USER_ID+" DEPT ID = "+DEPT_ID);
        if (USER_ID == 352 || USER_ID == 136 || USER_ID == 329 || USER_ID == 331 || USER_ID == 28 || USER_ID == 318 || USER_ID == 394) {
            //SALES LOGIN
            //tblStockBsr.setEnabled(false);
            LOGIN_USER_TYPE = "SALES";

            if (USER_ID == 352)//North: Mr. Mitang Lad
            {
                cmbIncharge.setSelectedItem("NORTH");
                cmbZone.setEnabled(false);
            } else if (USER_ID == 136)//East/West: Mr. Jaydeep Pandya
            {
                //cmbIncharge.setSelectedItem("EAST/WEST");//CLOSED ON 17-05-2022
                cmbIncharge.setSelectedItem("NORTH");
                cmbZone.setEnabled(false);
            } else if (USER_ID == 318)//East/West: Mr. Jaydeep Pandya
            {
                cmbIncharge.setSelectedItem("EAST/WEST");
                cmbZone.setEnabled(false);
            } else if (USER_ID == 329)//ACNE: Mr. Anup Singh
            {
                cmbIncharge.setSelectedItem("ACNE");
                cmbZone.setEnabled(false);
            } else if (USER_ID == 331)//South: Mr. Siddharth NeogiC
            {
                cmbIncharge.setSelectedItem("SOUTH");
                cmbZone.setEnabled(false);
            } else if (USER_ID == 28)//V. D. Shanbhag - KEY CLIENT , EXPORT , OTHER
            {
                /*
                 cmbIncharge.removeAllElements();
                 ComboData aData = new ComboData();
                 aData.Text = "KEY CLIENT";
                 aData.Code = 7;
                 cmbIncharge.addElement(aData);
                
                 aData.Text = "EXPORT";
                 aData.Code = 6;
                 cmbIncharge.addElement(aData);
                
                 aData.Text = "OTHER";
                 aData.Code = 8;
                 cmbIncharge.addElement(aData);
                 */
                //cmbZone.setEnabled(false);
            }
            rdoRequestMonth.setSelected(true);
        } else if (DEPT_ID == 40) {
            //P.P.C. Login

            rdoRequestMonth.setEnabled(true);
            rdoOcMonth.setSelected(true);
            rdoCurSchMonth.setEnabled(true);
            //tblPlanning.setEnabled(false);
            //tblWIP.setEnabled(false);
            //tblStockBsr.setEnabled(false);
            //tblDispatch.setEnabled(false);
            LOGIN_USER_TYPE = "PPC";
        } else if (DEPT_ID == 39) {
            //P.P.C. Login

//            rdoRequestMonth.setEnabled(true);
//            rdoOcMonth.setSelected(true);
//            rdoCurSchMonth.setEnabled(true);
            //tblPlanning.setEnabled(false);
            //tblWIP.setEnabled(false);
            //tblStockBsr.setEnabled(false);
            //tblDispatch.setEnabled(false);
            LOGIN_USER_TYPE = "DESIGN";
        } else {
            rdoRequestMonth.setSelected(true);
            //OTHER USER LOGIN
            disableAllTable();
            btnSave.setEnabled(false);
            LOGIN_USER_TYPE = "OTHER";
        }

        int Month = EITLERPGLOBAL.getCurrentMonth();
        String Name = getMonthName(Month) + " - " + EITLERPGLOBAL.getCurrentYear();

        cmbMonthYear.setSelectedItem(Name);
        cmbMonthYear1.setSelectedItem(Name);
        setPlanningPieces();
        setWIPPieces();
        setSTOCKPieces();
        setDispatchPieces();
        setPPRSPlanningPieces();
        //GenerateProdMfgReport();

        btnShowOrderStatus.setVisible(false);
    }

    private void GenerateCombo() {

        HashMap List = new HashMap();
        clsIncharge ObjIncharge;

        cmbZone.setModel(cmbIncharge);
        cmbIncharge.removeAllElements();  //Clearing previous contents

        List = clsIncharge.getIncgargeList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjIncharge = (clsIncharge) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Text = (String) ObjIncharge.getAttribute("INCHARGE_NAME").getObj();
            aData.Code = (long) ObjIncharge.getAttribute("INCHARGE_CD").getVal();
            cmbIncharge.addElement(aData);
        }
    }

    private void GenerateCombo_PPRS() {

        HashMap List = new HashMap();
        clsIncharge ObjIncharge;

        cmbZone1.setModel(cmbIncharge_PPRS);
        cmbIncharge_PPRS.removeAllElements();  //Clearing previous contents

        List = clsIncharge.getIncgargeList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjIncharge = (clsIncharge) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Text = (String) ObjIncharge.getAttribute("INCHARGE_NAME").getObj();
            aData.Code = (long) ObjIncharge.getAttribute("INCHARGE_CD").getVal();
            cmbIncharge_PPRS.addElement(aData);
        }
    }

    private void GenerateComboMonthYear() {

        cmbMonthYear.setModel(modelMonthYear);
        modelMonthYear.removeAllElements();  //Clearing previous contents

        /*
         ResultSet rs= data.getResult("SELECT distinct(PR_REQUESTED_MONTH) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE NOT IN ('CANCELED','CANCELLED','DIVIDED','DIVERTED','INVOICED','EXP-INVOICE') AND PR_REQUESTED_MONTH NOT IN ('','0') order by PR_REQ_MTH_LAST_DDMMYY");
         try{    
         while(rs.next()) {
         addToMonthYearCombo(rs.getString("PR_REQUESTED_MONTH"));
         }
         }catch(Exception e)
         {
         e.printStackTrace();
         }
         */
        if (rdoRequestMonth.isSelected() && rdoExceptSDF.isSelected()) {
            //CHANGED ON 08/03/2020
            //10/08/2021
            //03/09/2021

            int current_month = EITLERPGLOBAL.getCurrentMonth() + 2;
            //int current_month = EITLERPGLOBAL.getCurrentMonth()+1; // comment on 14/02/2022

//            if(EITLERPGLOBAL.getCurrentDay()<15)
//            {
//                current_month = EITLERPGLOBAL.getCurrentMonth()+2;
//            }
            //04/09/2021
            if (EITLERPGLOBAL.getCurrentDay() <= 10) {
                current_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                //current_month = EITLERPGLOBAL.getCurrentMonth(); // comment on 14/02/2022
            }

            int current_year = EITLERPGLOBAL.getCurrentYear();
            String exist_last_date = LastDayOfReqMonth(getMonthName(current_month) + " - " + current_year);
            if (current_month > 12) {
                current_month = current_month - 12;
                current_year = current_year + 1;
            }
            String Month_Name = getMonthName(current_month);
            addToMonthYearCombo(Month_Name + " - " + current_year);
            current_month = current_month + 1;
            // System.out.println("DATA = SELECT PR_REQUESTED_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR')  AND PR_REQ_MTH_LAST_DDMMYY!='0000-00-00'  AND PR_REQ_MTH_LAST_DDMMYY>='"+exist_last_date+"' AND PR_GROUP!='SDF' AND PR_DELINK!='OBSOLETE'  ORDER BY PR_REQ_MTH_LAST_DDMMYY DESC LIMIT 1");    
            String Req_Month_Last_Available = data.getStringValueFromDB("SELECT PR_REQUESTED_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  AND PR_REQ_MTH_LAST_DDMMYY!='0000-00-00'  AND PR_REQ_MTH_LAST_DDMMYY>='" + exist_last_date + "' AND PR_GROUP!='SDF' AND COALESCE(PR_DELINK,'')!='OBSOLETE'  ORDER BY PR_REQ_MTH_LAST_DDMMYY DESC LIMIT 1");
            // System.out.println("Req_Month_Last_Available = "+Req_Month_Last_Available);
            if (!Req_Month_Last_Available.equals("")) {
                int j = 0;
                do {
                    j++;
                    if (current_month > 12) {
                        current_month = current_month - 12;
                        current_year = current_year + 1;
                    }
                    Month_Name = getMonthName(current_month);
                    addToMonthYearCombo(Month_Name + " - " + current_year);
                    current_month = current_month + 1;
                    Month_Name = Month_Name + " - " + current_year;
                    if (j == 10) {
                        break;
                    }

                } while (!Req_Month_Last_Available.equals(Month_Name));
            }

        } else if (rdoRequestMonth.isSelected() && rdoSDF.isSelected()) {
            //CHANGED ON 08/03/2020

            int current_month = EITLERPGLOBAL.getCurrentMonth() + 1;

            //int current_month = EITLERPGLOBAL.getCurrentMonth(); // comment on 14/02/2022
//            if(EITLERPGLOBAL.getCurrentDay()<15)
//            {
//                current_month = EITLERPGLOBAL.getCurrentMonth()+1;
//            }
            if (EITLERPGLOBAL.getCurrentDay() <= 10) {
                current_month = EITLERPGLOBAL.getCurrentMonth();
                //current_month = EITLERPGLOBAL.getCurrentMonth(); // comment on 14/02/2022
            }

            int current_year = EITLERPGLOBAL.getCurrentYear();
            String exist_last_date = LastDayOfReqMonth(getMonthName(current_month) + " - " + current_year);
            if (current_month > 12) {
                current_month = current_month - 12;
                current_year = current_year + 1;
            }
            String Month_Name = getMonthName(current_month);
            addToMonthYearCombo(Month_Name + " - " + current_year);
            current_month = current_month + 1;
            System.out.println("DATA2 = SELECT PR_REQUESTED_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  AND PR_REQ_MTH_LAST_DDMMYY!='0000-00-00' AND PR_REQ_MTH_LAST_DDMMYY>='" + exist_last_date + "'  AND PR_GROUP='SDF' AND COALESCE(PR_DELINK,'')!='OBSOLETE'  ORDER BY PR_REQ_MTH_LAST_DDMMYY DESC LIMIT 1");
            String Req_Month_Last_Available = data.getStringValueFromDB("SELECT PR_REQUESTED_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  AND PR_REQ_MTH_LAST_DDMMYY!='0000-00-00' AND PR_REQ_MTH_LAST_DDMMYY>='" + exist_last_date + "'  AND PR_GROUP='SDF' AND COALESCE(PR_DELINK,'')!='OBSOLETE'  ORDER BY PR_REQ_MTH_LAST_DDMMYY DESC LIMIT 1");
            System.out.println("Req_Month_Last_Available = " + Req_Month_Last_Available);
            if (!Req_Month_Last_Available.equals("")) {
                int i = 0;
                do {
                    i++;
                    if (current_month > 12) {
                        current_month = current_month - 12;
                        current_year = current_year + 1;
                    }
                    Month_Name = getMonthName(current_month);
                    addToMonthYearCombo(Month_Name + " - " + current_year);
                    current_month = current_month + 1;
                    Month_Name = Month_Name + " - " + current_year;
                    if (i == 10) {
                        break;
                    }
                } while (!Req_Month_Last_Available.equals(Month_Name));

            }

//            for(int i=1;i<=24;i++)
//            {
//                if(current_month > 12)
//                {
//                    current_month = 1;
//                    current_year = current_year + 1;
//                }
//                Month_Name = getMonthName(current_month);
//                addToMonthYearCombo(Month_Name + " - " + current_year);
//                current_month = current_month + 1;
//            }
        } else if (rdoOcMonth.isSelected()) {
            if (rdoExceptSDF.isSelected()) {
                //+3
                String previous_date = data.getStringValueFromDB("SELECT MIN(PR_OC_LAST_DDMMYY) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                        + " WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') "
                        + "AND PR_OC_LAST_DDMMYY!='0000-00-00'");

                //CHANGED ON 08/03/2020
                int till_month = EITLERPGLOBAL.getCurrentMonth() + 2;

                if (EITLERPGLOBAL.getCurrentDay() < 15) {
                    //CHANGED ON 08/03/2020
                    till_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                }

                int till_year = EITLERPGLOBAL.getCurrentYear();

                if (till_month == 13) {
                    till_month = 1;
                    till_year = till_year + 1;
                } else if (till_month >= 14) {
                    till_month = 2;
                    till_year = till_year + 1;
                }

                String TILL_MONTH_YEAR = getMonthName(till_month) + " - " + till_year;

                String Generated_Month_For_Add = "";
                int i = 0;
                Date date1 = null;

                try {
                    date1 = new SimpleDateFormat("yyyy-MM-dd").parse(previous_date);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int month_previous;
                int year_previous = date1.getYear() + 1900;
                month_previous = date1.getMonth() + 1;

                do {
                    i++;

                    if (month_previous >= 13) {
                        month_previous = 1;
                        year_previous = year_previous + 1;
                    }

                    String month_name1 = getMonthName(month_previous);
                    addToMonthYearCombo(month_name1 + " - " + year_previous);
                    month_previous = month_previous + 1;
                    Generated_Month_For_Add = month_name1 + " - " + year_previous;
                } while (!Generated_Month_For_Add.equals(TILL_MONTH_YEAR));

                cmbMonthYear.setSelectedIndex(cmbMonthYear.getItemCount() - 1);
            } else {
                //+3
                String previous_date = data.getStringValueFromDB("SELECT MIN(PR_OC_LAST_DDMMYY) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                        + " WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') "
                        + "AND PR_OC_LAST_DDMMYY!='0000-00-00'");

                //CHANGED ON 08/03/2020
                int till_month = EITLERPGLOBAL.getCurrentMonth() + 1;

                if (EITLERPGLOBAL.getCurrentDay() < 15) {
                    till_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                }

                int till_year = EITLERPGLOBAL.getCurrentYear();

                if (till_month >= 13) {
                    till_month = 1;
                    till_year = till_year + 1;
                }
                String TILL_MONTH_YEAR = getMonthName(till_month) + " - " + till_year;
                String Generated_Month_For_Add = "";

                int i = 0;

                Date date1 = null;

                try {
                    date1 = new SimpleDateFormat("yyyy-MM-dd").parse(previous_date);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int month_previous;
                int year_previous = date1.getYear() + 1900;
                month_previous = date1.getMonth() + 1;

                do {
                    i++;

                    if (month_previous >= 13) {
                        month_previous = 1;
                        year_previous = year_previous + 1;
                    }

                    String month_name1 = getMonthName(month_previous);

                    addToMonthYearCombo(month_name1 + " - " + year_previous);
                    month_previous = month_previous + 1;
                    Generated_Month_For_Add = month_name1 + " - " + year_previous;
                } while (!Generated_Month_For_Add.equals(TILL_MONTH_YEAR));

                cmbMonthYear.setSelectedIndex(cmbMonthYear.getItemCount() - 1);

            }

        } /*  // Comented on date 07/01/2020      
         else if(rdoOcMonth.isSelected())
         {
         if(rdoExceptSDF.isSelected())
         {
         //+3  
         String previous_date = data.getStringValueFromDB("SELECT MIN(PR_OC_LAST_DDMMYY) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
         + " WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR') "
         + "AND PR_OC_LAST_DDMMYY!='0000-00-00'");
               
         int till_month = EITLERPGLOBAL.getCurrentMonth()+3;
               
               
         if(EITLERPGLOBAL.getCurrentDay()<15)
         {
         till_month = EITLERPGLOBAL.getCurrentMonth()+2;
         }

               
         //int till_year = EITLERPGLOBAL.getCurrentYear();
               
               
         Date date1 = null;
         try{

         date1 = new SimpleDateFormat("yyyy-MM-dd").parse(previous_date);  
         }catch(Exception e)
         {
         e.printStackTrace();
         }
         int month1;
         int year1 = date1.getYear() + 1900;
         month1 = date1.getMonth();
                
         int till = month1+till_month-1;
         for (int i = month1; i < (till); i++) {
         month1 = month1 + 1;

         if (month1 >= 13) {
         month1 = 1;
         year1 = year1 + 1;
         }

         String month_name1 = getMonthName(month1);
                    
         addToMonthYearCombo(month_name1 + " - " + year1);
         }
         cmbMonthYear.setSelectedIndex(cmbMonthYear.getItemCount()-1);
         }
         else 
         {
         //+3  
         String previous_date = data.getStringValueFromDB("SELECT MIN(PR_OC_LAST_DDMMYY) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
         + " WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR') "
         + "AND PR_OC_LAST_DDMMYY!='0000-00-00'");
               
         int till_month = EITLERPGLOBAL.getCurrentMonth()+2;
               
         if(EITLERPGLOBAL.getCurrentDay()<15)
         {
         till_month = EITLERPGLOBAL.getCurrentMonth()+1;
         }
               
         //int till_year = EITLERPGLOBAL.getCurrentYear();
               
               
         Date date1 = null;
         try{

         date1 = new SimpleDateFormat("yyyy-MM-dd").parse(previous_date);  
         }catch(Exception e)
         {
         e.printStackTrace();
         }
         int month1;
         int year1 = date1.getYear() + 1900;
         month1 = date1.getMonth();
                
         int till = month1+till_month-1;
         for (int i = month1; i < (till); i++) {
         month1 = month1 + 1;

         if (month1 >= 13) {
         month1 = 1;
         year1 = year1 + 1;
         }

         String month_name1 = getMonthName(month1);
                    
         addToMonthYearCombo(month_name1 + " - " + year1);
         }
                
         cmbMonthYear.setSelectedIndex(cmbMonthYear.getItemCount()-1);
                
         }
            
         //addMonth(24,true);
         }
         */ else if (rdoCurSchMonth.isSelected()) {
            //new
            try {
                ResultSet rsData = data.getResult("SELECT distinct PR_CURRENT_SCH_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE "
                        + " PR_PRIORITY_HOLD_CAN_FLAG IN ('0','1','2','3','4','5')  AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','SPLICING','MENDING','NEEDLING','SEAMING','FINISHING','SPIRALLING','ASSEMBLY','IN STOCK','BSR','HEAT_SETTING','MARKING','SPLICING') "
                        + " AND COALESCE(PR_CURRENT_SCH_MONTH,'') != '' AND PR_DELINK != 'OBSOLETE' ORDER BY PR_CURRENT_SCH_LAST_DDMMYY");
                rsData.first();

                while (!rsData.isAfterLast()) {
                    addToMonthYearCombo(rsData.getString("PR_CURRENT_SCH_MONTH"));
                    rsData.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //03/04/2021
            /*
             if(rdoExceptSDF.isSelected())
             {
             //+3  
             //CHANGED ON 08/03/2020
             int till_month = EITLERPGLOBAL.getCurrentMonth()+2;
                
             if(EITLERPGLOBAL.getCurrentDay()<15)
             {
             //CHANGED ON 08/03/2020
             till_month = EITLERPGLOBAL.getCurrentMonth()+1;
             }
                
             //int till_year = EITLERPGLOBAL.getCurrentYear();
               
             Date date1 = null;
             try{
             date1 = new SimpleDateFormat("yyyy-MM-dd").parse(EITLERPGLOBAL.getCurrentDateDB());  
             }catch(Exception e)
             {
             e.printStackTrace();
             }
             int month1;
             int year1 = date1.getYear() + 1900;
             month1 = date1.getMonth();
                
             int till = month1+till_month-1;
             for (int i = month1; i < till_month; i++) {
             month1 = month1 + 1;

             if (month1 >= 13) {
             month1 = 1;
             year1 = year1 + 1;
             }

             String month_name1 = getMonthName(month1);
                    
             addToMonthYearCombo(month_name1 + " - " + year1);
             }
                
             }
             else
             {
             //+3  
             //CHANGED ON 08/03/2020
             int till_month = EITLERPGLOBAL.getCurrentMonth()+1;
                
             if(EITLERPGLOBAL.getCurrentDay()<15)
             {
             till_month = EITLERPGLOBAL.getCurrentMonth()+1;
             }
                
             //int till_year = EITLERPGLOBAL.getCurrentYear();
               
             Date date1 = null;
             try{
             date1 = new SimpleDateFormat("yyyy-MM-dd").parse(EITLERPGLOBAL.getCurrentDateDB());  
             }catch(Exception e)
             {
             e.printStackTrace();
             }
             int month1;
             int year1 = date1.getYear() + 1900;
             month1 = date1.getMonth();
                
             int till = month1+till_month-1;
             for (int i = month1; i < till_month; i++) {
             month1 = month1 + 1;

             if (month1 >= 13) {
             month1 = 1;
             year1 = year1 + 1;
             }

             String month_name1 = getMonthName(month1);
                    
             addToMonthYearCombo(month_name1 + " - " + year1);
             }
                
             }
             //addMonth(24,true);
             */
        }

    }

    private void GenerateComboMonthYear_PPRS() {

        cmbMonthYear1.setModel(modelMonthYear_PPRS);
        modelMonthYear_PPRS.removeAllElements();  //Clearing previous contents

        if (rdoExceptSDF1.isSelected()) {

            int cur_month = EITLERPGLOBAL.getCurrentMonth() + 2;
            if (EITLERPGLOBAL.getCurrentDay() <= 16) {
                cur_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                //current_month = EITLERPGLOBAL.getCurrentMonth(); // comment on 14/02/2022
            }

            int cur_year = EITLERPGLOBAL.getCurrentYear();
            if (cur_month > 12) {
                cur_month = cur_month - 12;
                cur_year = cur_year + 1;
            }
            String Month_Name = getMonthName(cur_month);
            addToMonthYearCombo_PPRS(Month_Name + " - " + cur_year);
            cur_month = cur_month + 1;

        } else if (rdoSDF1.isSelected()) {
            int current_month = EITLERPGLOBAL.getCurrentMonth() + 1;
            if (EITLERPGLOBAL.getCurrentDay() <= 16) {
                current_month = EITLERPGLOBAL.getCurrentMonth();
                //current_month = EITLERPGLOBAL.getCurrentMonth(); // comment on 14/02/2022
            }

            int current_year = EITLERPGLOBAL.getCurrentYear();
            String exist_last_date = LastDayOfReqMonth(getMonthName(current_month) + " - " + current_year);
            if (current_month > 12) {
                current_month = current_month - 12;
                current_year = current_year + 1;
            }
            String Month_Name = getMonthName(current_month);
            addToMonthYearCombo_PPRS(Month_Name + " - " + current_year);
            current_month = current_month + 1;

        }
    }

    /* private void addMonth(int total_no_of_month_add,boolean CURRENT_MONTH)
     {
        
     int current_month = EITLERPGLOBAL.getCurrentMonth();
     int current_year = EITLERPGLOBAL.getCurrentYear();
        
     if(CURRENT_MONTH==false)
     {
     current_month = current_month + 1;
     if(current_month > 12)
     {
     current_month = current_month - 12;
     current_year = current_year + 1;
     }
     }
        
        
     for(int i=1;i<=total_no_of_month_add;i++)
     {
     if(current_month > 12)
     {
     current_month = current_month - 12;
     current_year = current_year + 1;
     }
     String Month_Name = getMonthName(current_month);
     addToMonthYearCombo(Month_Name + " - " + current_year);
     current_month = current_month + 1;
     }
        
     }
     */
    private void addToMonthYearCombo(String MonthYear) {
        ComboData aData = new ComboData();
        aData.Text = MonthYear;
        aData.strCode = MonthYear;
        modelMonthYear.addElement(aData);
    }

    private void addToMonthYearCombo_PPRS(String MonthYear) {
        ComboData aData = new ComboData();
        aData.Text = MonthYear;
        aData.strCode = MonthYear;
        modelMonthYear_PPRS.addElement(aData);
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
    /* private int getMonthNo(String MonthName)
     {
     if(MonthName.contains("Jan"))
     {
     return 1;
     }
     else if(MonthName.contains("Feb"))
     {
     return 2;
     }
     else if(MonthName.contains("Mar"))
     {
     return 3;
     }
     else if(MonthName.contains("Apr"))
     {
     return 4;
     }
     else if(MonthName.contains("May"))
     {
     return 5;
     }
     else if(MonthName.contains("Jun"))
     {
     return 6;
     }
     else if(MonthName.contains("Jul"))
     {
     return 7;
     }
     else if(MonthName.contains("Aug"))
     {
     return 8;
     }
     else if(MonthName.contains("Sep"))
     {
     return 9;
     }
     else if(MonthName.contains("Oct"))
     {
     return 10;
     }
     else if(MonthName.contains("Nov"))
     {
     return 11;
     }
     else if(MonthName.contains("Dec"))
     {
     return 12;
     }
     return 0;
     }
     */

    private void GenerateGroupCombo() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        tmpConn = data.getCreatedConn();

        cmbProdGroup.setModel(cmodelProductGroup);
        cmodelProductGroup.removeAllElements();  //Clearing previous contents

        ComboData aData1 = new ComboData();
        aData1.Text = "ALL";
        aData1.strCode = "ALL";
        cmodelProductGroup.addElement(aData1);

        try {
            tmpStmt = tmpConn.createStatement();
            //System.out.println("select distinct(GROUP_NAME) FROM PRODUCTION.FELT_QLT_RATE_MASTER");
            rsTmp = tmpStmt.executeQuery("select distinct(GROUP_NAME) FROM PRODUCTION.FELT_QLT_RATE_MASTER");

            while (rsTmp.next()) {
                ComboData aData = new ComboData();
                aData.Text = rsTmp.getString("GROUP_NAME");
                aData.strCode = rsTmp.getString("GROUP_NAME");
                cmodelProductGroup.addElement(aData);
            }

            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateGroupCombo_PPRS() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        tmpConn = data.getCreatedConn();

        cmbProdGroup1.setModel(cmodelProductGroup_PPRS);
        cmodelProductGroup_PPRS.removeAllElements();  //Clearing previous contents

        ComboData aData1 = new ComboData();
        aData1.Text = "ALL";
        aData1.strCode = "ALL";
        cmodelProductGroup_PPRS.addElement(aData1);

        try {
            tmpStmt = tmpConn.createStatement();
            //System.out.println("select distinct(GROUP_NAME) FROM PRODUCTION.FELT_QLT_RATE_MASTER");
            rsTmp = tmpStmt.executeQuery("select distinct(GROUP_NAME) FROM PRODUCTION.FELT_QLT_RATE_MASTER");

            while (rsTmp.next()) {
                ComboData aData = new ComboData();
                aData.Text = rsTmp.getString("GROUP_NAME");
                aData.strCode = rsTmp.getString("GROUP_NAME");
                cmodelProductGroup_PPRS.addElement(aData);
            }

            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    public void DefaultSettings() {

        clearFields();
    }

    private void clearFields() {

    }

    private void DisplayData() {

    }

    private void FormatGridPlanning() {
        try {
            DataModel_Planning = new EITLTableModel();
            tblPlanning.removeAll();

            tblPlanning.setModel(DataModel_Planning);
            tblPlanning.setAutoResizeMode(0);

            DataModel_Planning.addColumn("SrNo"); //0 - Read Only
            DataModel_Planning.addColumn("CONF OC MTH"); //0 - Read Only
            DataModel_Planning.addColumn("PIECE NO"); //1
            DataModel_Planning.addColumn("PARTY CODE"); //5
            DataModel_Planning.addColumn("PARTY NAME"); //6
            DataModel_Planning.addColumn("MAC NO"); //2
            DataModel_Planning.addColumn("POS NO"); //3
            DataModel_Planning.addColumn("POS DESC"); //4
            DataModel_Planning.addColumn("UPN PIECES"); //11
            DataModel_Planning.addColumn("REQ MTH"); //11
            DataModel_Planning.addColumn("RESCH. REQ MTH"); //11
            DataModel_Planning.addColumn("OC MONTH"); //11
            DataModel_Planning.addColumn("CURR SALES PLAN MTH"); //11
            DataModel_Planning.addColumn("SPECIAL REQ MTH"); //9
            DataModel_Planning.addColumn("SPECIAL REQ DATE"); //9
            DataModel_Planning.addColumn("EXP DELIVERY DATE"); //7
            DataModel_Planning.addColumn("ACTUAL DELIVERY DATE"); //7
            DataModel_Planning.addColumn("EXP PI DATE"); //7
            DataModel_Planning.addColumn("ACTUAL PI DATE"); //7
            DataModel_Planning.addColumn("EXP PAY/CHQ RCV DATE"); //7
            DataModel_Planning.addColumn("ACTUAL PAY/CHQ RCV DATE"); //7
            DataModel_Planning.addColumn("EXP DISPATCH DATE"); //7
            DataModel_Planning.addColumn("ACTUAL DISPATCH DATE"); //7
            DataModel_Planning.addColumn("GROUP"); //8
            DataModel_Planning.addColumn("PIECE STAGE"); //9
            DataModel_Planning.addColumn("INVOICE VALUE"); //9
            DataModel_Planning.addColumn("CONTACT PERSON"); //1
            DataModel_Planning.addColumn("PHONE NO"); //1
            DataModel_Planning.addColumn("EMAIL ID"); //1
            DataModel_Planning.addColumn("EMAIL ID2"); //1
            DataModel_Planning.addColumn("EMAIL ID3"); //1
            DataModel_Planning.addColumn("LENGTH"); //1
            DataModel_Planning.addColumn("WIDTH"); //1
            DataModel_Planning.addColumn("GSM"); //1
            DataModel_Planning.addColumn("STYLE"); //1
            DataModel_Planning.addColumn("WEIGHT"); //1
            DataModel_Planning.addColumn("SQMTR"); //1
            DataModel_Planning.addColumn("ZONE"); //1

            DataModel_Planning.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_Planning.SetVariable(1, "SELECT"); //0 - Read Only
            DataModel_Planning.SetVariable(2, "PR_PIECE_NO"); //1
            DataModel_Planning.SetVariable(3, "PR_PARTY_CODE"); //1
            DataModel_Planning.SetVariable(4, "PARTY_NAME"); //1
            DataModel_Planning.SetVariable(5, "PR_MACHINE_NO"); //1
            DataModel_Planning.SetVariable(6, "PR_POSITION_NO"); //1
            DataModel_Planning.SetVariable(7, "POSITION_DESC"); //1
            DataModel_Planning.SetVariable(8, "UPN_PIECES"); //1
            DataModel_Planning.SetVariable(9, "PR_REQUESTED_MONTH"); //1
            DataModel_Planning.SetVariable(10, "RESCHEDULE_MONTH"); //1
            DataModel_Planning.SetVariable(11, "OC_MONTH"); //1
            DataModel_Planning.SetVariable(12, "CURRENT_SCHEDULE_MONTH"); //1
            DataModel_Planning.SetVariable(13, "PR_SPL_REQUEST_MONTHYEAR"); //1
            DataModel_Planning.SetVariable(14, "PR_SPL_REQUEST_DATE"); //1
            DataModel_Planning.SetVariable(15, "EXP_WIP_DELIVERY_DATE"); //1
            DataModel_Planning.SetVariable(16, "ACT_WIP_DELIVERY_DATE"); //1
            DataModel_Planning.SetVariable(17, "EXP_PI_DATE"); //1
            DataModel_Planning.SetVariable(18, "ACT_PI_DATE"); //1
            DataModel_Planning.SetVariable(19, "EXP_PAY_CHQ_RCV_DATE"); //1
            DataModel_Planning.SetVariable(20, "ACT_PAY_CHQ_RCV_DATE"); //1
            DataModel_Planning.SetVariable(21, "EXP_DISPATCH_DATE"); //1
            DataModel_Planning.SetVariable(22, "ACT_DISPATCH_DATE"); //1
            DataModel_Planning.SetVariable(23, "PR_GROUP"); //1
            DataModel_Planning.SetVariable(24, "PR_PIECE_STAGE"); //1
            DataModel_Planning.SetVariable(25, "INVOICE_VALUE"); //1
            DataModel_Planning.SetVariable(26, "CONTACT_PERSON"); //1
            DataModel_Planning.SetVariable(27, "PHONE_NO"); //1
            DataModel_Planning.SetVariable(28, "EMAIL_ID"); //1
            DataModel_Planning.SetVariable(29, "EMAIL_ID2"); //1
            DataModel_Planning.SetVariable(30, "EMAIL_ID3"); //1
            DataModel_Planning.SetVariable(31, "LENGTH"); //1
            DataModel_Planning.SetVariable(32, "WIDTH"); //1
            DataModel_Planning.SetVariable(33, "GSM"); //1
            DataModel_Planning.SetVariable(34, "STYLE"); //1
            DataModel_Planning.SetVariable(35, "WEIGHT"); //1
            DataModel_Planning.SetVariable(36, "SQMTR"); //1
            DataModel_Planning.SetVariable(37, "ZONE"); //1

            for (int i = 0; i < DataModel_Planning.getColumnCount(); i++) {
                if (i != 1 && i != 10 && i != 26 && i != 27 && i != 28 && i != 29 && i != 30) {
                    DataModel_Planning.SetReadOnly(i);
                }

//                  Disable for Testing only     
                Date date = new Date();
                //if(!(date.getDate()>=11 && date.getDate()<=25))
                if (date.getDate() >= 11 && date.getDate() <= 14 && !LOGIN_USER_TYPE.equals("SALES")) {
                    // 29 April 2019
                    DataModel_Planning.SetReadOnly(1);
                }

                String selected_month = cmbMonthYear.getSelectedItem().toString();
                //String allowed_month = addMonth(3, true);

                //CHANGED ON 08/03/2020
                int current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                if (rdoSDF.isSelected()) {
                    //CHANGED ON 08/03/2020
                    current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                    if (date.getDate() <= 10) {
                        current_allowed_month = EITLERPGLOBAL.getCurrentMonth();
                    }

                }
                int current_allowed_year = EITLERPGLOBAL.getCurrentYear();

                if (current_allowed_month > 12) {
                    current_allowed_month = current_allowed_month - 12;
                    current_allowed_year = 1 + current_allowed_year;
                }

                String allowed_month = getMonthName(current_allowed_month) + " - " + current_allowed_year;
                //System.out.println("allowed Month = "+allowed_month);

                if (!(rdoRequestMonth.isSelected() && selected_month.equals(allowed_month))) {
                //10/10/2021    
                    //    DataModel_Planning.SetReadOnly(1);
                }

                tblPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_planning);
            }

            tblPlanning.getColumnModel().getColumn(0).setMaxWidth(40);
            tblPlanning.getColumnModel().getColumn(1).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(2).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(3).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(4).setMinWidth(150);
            tblPlanning.getColumnModel().getColumn(5).setMinWidth(70);
            tblPlanning.getColumnModel().getColumn(6).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(6).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(7).setMinWidth(130);
            tblPlanning.getColumnModel().getColumn(8).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(9).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(10).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(11).setMinWidth(120);
            tblPlanning.getColumnModel().getColumn(12).setMinWidth(150);
            tblPlanning.getColumnModel().getColumn(13).setMinWidth(120);
            tblPlanning.getColumnModel().getColumn(14).setMinWidth(120);
            tblPlanning.getColumnModel().getColumn(15).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(15).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(16).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(16).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(17).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(17).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(18).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(18).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(19).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(19).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(20).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(20).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(21).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(21).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(22).setMinWidth(0);
            tblPlanning.getColumnModel().getColumn(22).setMaxWidth(0);
            tblPlanning.getColumnModel().getColumn(23).setMinWidth(90);
            tblPlanning.getColumnModel().getColumn(24).setMinWidth(120);
            tblPlanning.getColumnModel().getColumn(25).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(26).setMinWidth(130);
            tblPlanning.getColumnModel().getColumn(27).setMinWidth(120);
            tblPlanning.getColumnModel().getColumn(28).setMinWidth(130);
            tblPlanning.getColumnModel().getColumn(29).setMinWidth(130);
            tblPlanning.getColumnModel().getColumn(30).setMinWidth(130);
            tblPlanning.getColumnModel().getColumn(31).setMinWidth(90);
            tblPlanning.getColumnModel().getColumn(32).setMinWidth(90);
            tblPlanning.getColumnModel().getColumn(33).setMinWidth(90);
            tblPlanning.getColumnModel().getColumn(34).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(35).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(36).setMinWidth(100);
            tblPlanning.getColumnModel().getColumn(37).setMinWidth(100);

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

            for (int i = 0; i < DataModel_Planning.getColumnCount(); i++) {
                if (i != 1 && i != 10 && i != 15 && i != 14 && i != 19 && i != 26 && i != 27 && i != 28 && i != 29 && i != 30) {
                    DataModel_Planning.SetReadOnly(i);
                }

//                  Disable for Testing only     
                Date date = new Date();
                //if(!(date.getDate()>=15 && date.getDate()<=25)) //On 26/05/2019
//                if(!(date.getDate()>=15 && date.getDate()<=26))
                //if((date.getDate()>=11 && date.getDate()<=14)  && !LOGIN_USER_TYPE.equals("SALES"))
                //{
                //29 April 2019
                //    DataModel_Planning.SetReadOnly(1);
                //}

                if (LOGIN_USER_TYPE.equals("PPC") || LOGIN_USER_TYPE.equals("OTHER")) {
                    DataModel_Planning.SetReadOnly(10);
                    DataModel_Planning.SetReadOnly(13);
                    DataModel_Planning.SetReadOnly(14);
                }
                if (LOGIN_USER_TYPE.equals("SALES") || LOGIN_USER_TYPE.equals("OTHER")) {
                    DataModel_Planning.SetReadOnly(15);
                }
            }

            tblPlanning.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblPlanning.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
            tblPlanning.getColumnModel().getColumn(DataModel_Planning.getColFromVariable("UPN_PIECES")).setCellRenderer(this.Renderer);
            tblPlanning.getColumnModel().getColumn(DataModel_Planning.getColFromVariable("PR_PARTY_CODE")).setCellRenderer(this.Renderer_invoiced);

            TableColumn dateColumn_RESCH = tblPlanning.getColumnModel().getColumn(DataModel_Planning.getColFromVariable("RESCHEDULE_MONTH"));
            TableColumn dateColumn_OC_MONTH = tblPlanning.getColumnModel().getColumn(DataModel_Planning.getColFromVariable("OC_MONTH"));
            JComboBox monthbox = new JComboBox();

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
//                month = date.getMonth()+3;
//            }
            month = date.getMonth() + 1;
            if (rdoSDF.isSelected()) {
                month = date.getMonth();
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

            monthbox.addActionListener(new ActionListener() {//add actionlistner to listen for change
                @Override
                public void actionPerformed(ActionEvent e) {

                        //String s = (String) date1.getSelectedItem();//get the selected item
                    //JOptionPane.showMessageDialog(null, "Event on row "+Table.getSelectedRow()+" col "+Table.getSelectedRow());
                    String OC_MONTH = DataModel_Planning.getValueByVariable("OC_MONTH", tblPlanning.getSelectedRow());
                    if ("".equals(OC_MONTH)) {

                    } else {
                        tblPlanning.setValueAt("", tblPlanning.getSelectedRow(), tblPlanning.getSelectedColumn());
                    }
                }
            });

            dateColumn_RESCH.setCellEditor(new DefaultCellEditor(monthbox));
            dateColumn_OC_MONTH.setCellEditor(new DefaultCellEditor(monthbox));

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void setPlanningPieces() {
        try {
            if (cmbMonthYear.getSelectedItem() == null) {
                return;
            }
            FormatGridPlanning();
            Renderer.removeBackColors();
            String str_query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS "
                    + "ON PS.POSITION_NO = PR.PR_POSITION_NO "
                    + "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM "
                    + "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                    + "WHERE PR.PR_PIECE_NO!='' ";

            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {

                str_query = str_query + " AND PR.PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }

            if (!cmbPieceStage.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_PIECE_STAGE = '" + cmbPieceStage.getSelectedItem() + "' ";
            }

            if (rdoRequestMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_REQUESTED_MONTH='" + cmbMonthYear.getSelectedItem() + "'";

            } else if (rdoOcMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_OC_MONTHYEAR='" + cmbMonthYear.getSelectedItem() + "'";

            } else if (rdoCurSchMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_CURRENT_SCH_MONTH='" + cmbMonthYear.getSelectedItem() + "'";

            }

            if (rdoExceptSDF.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP!='SDF'";
            } else if (rdoSDF.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP='SDF'";
            }

            if (!"".equals(txtPartyCode.getText())) {
                str_query = str_query + " AND PR.PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }

            if (!"".equals(txtUPN.getText())) {
                str_query = str_query + " AND PR.PR_UPN='" + txtUPN.getText() + "'";
            }
            String NewPieces = "";
            if (!txtPieceNo.getText().equals("")) {
                String PieceNo = txtPieceNo.getText();
                String Pieces[] = PieceNo.split(",");

                for (String Piece : Pieces) {
                    if (NewPieces.equals("")) {
                        NewPieces = "'" + Piece + "'";
                    } else {
                        NewPieces = NewPieces + ",'" + Piece + "'";
                    }
                }
            }

            if (!NewPieces.equals("")) {
                str_query = str_query + " AND PR.PR_PIECE_NO IN (" + NewPieces + ") ";
            }

            str_query = str_query + " AND PR_PIECE_STAGE IN ('PLANNING','BOOKING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) ";

            str_query = str_query + ORDER_BY;

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("PLANNING Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            double TotalInvoiceAmt = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_Planning.addRow(rowData);

                DataModel_Planning.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_Planning.setValueByVariable("SELECT", Boolean.FALSE, NewRow);
                DataModel_Planning.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_Planning.setValueByVariable("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"), NewRow);
                DataModel_Planning.setValueByVariable("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"), NewRow);
                DataModel_Planning.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);
                DataModel_Planning.setValueByVariable("UPN_PIECES", resultSet.getString("PR_UPN"), NewRow);
                DataModel_Planning.setValueByVariable("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"), NewRow);
                DataModel_Planning.setValueByVariable("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                DataModel_Planning.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, resultSet.getString("PR_PARTY_CODE")), NewRow);
                DataModel_Planning.setValueByVariable("PR_GROUP", resultSet.getString("PR_GROUP"), NewRow);
                DataModel_Planning.setValueByVariable("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"), NewRow);
                DataModel_Planning.setValueByVariable("OC_MONTH", resultSet.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_Planning.setValueByVariable("CURRENT_SCHEDULE_MONTH", resultSet.getString("PR_CURRENT_SCH_MONTH"), NewRow);

                DataModel_Planning.setValueByVariable("EXP_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_WIP_DELIVERY_DATE")), NewRow);
                DataModel_Planning.setValueByVariable("ACT_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_FNSG_DATE")), NewRow);

                //
                DataModel_Planning.setValueByVariable("EXP_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PI_DATE")), NewRow);
                DataModel_Planning.setValueByVariable("ACT_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PI_DATE")), NewRow);

                DataModel_Planning.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PAY_CHQRC_DATE")), NewRow);
                DataModel_Planning.setValueByVariable("ACT_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PAY_CHQRC_DATE")), NewRow);
                DataModel_Planning.setValueByVariable("EXP_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_DESPATCH_DATE")), NewRow);
                DataModel_Planning.setValueByVariable("ACT_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_INVOICE_DATE")), NewRow);

                DataModel_Planning.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", resultSet.getString("PR_SPL_REQUEST_MONTHYEAR"), NewRow);
                DataModel_Planning.setValueByVariable("PR_SPL_REQUEST_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_SPL_REQUEST_DATE")), NewRow);
                DataModel_Planning.setValueByVariable("INVOICE_VALUE", resultSet.getString("PR_FELT_VALUE_WITH_GST"), NewRow);

                DataModel_Planning.setValueByVariable("CONTACT_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_Planning.setValueByVariable("PHONE_NO", resultSet.getString("PHONE_NO"), NewRow);
                DataModel_Planning.setValueByVariable("EMAIL_ID", resultSet.getString("EMAIL"), NewRow);
                DataModel_Planning.setValueByVariable("EMAIL_ID2", resultSet.getString("EMAIL_ID2"), NewRow);
                DataModel_Planning.setValueByVariable("EMAIL_ID3", resultSet.getString("EMAIL_ID3"), NewRow);

//                DataModel_Planning.setValueByVariable("LENGTH", resultSet.getString("PR_BILL_LENGTH"), NewRow);
//                DataModel_Planning.setValueByVariable("WIDTH", resultSet.getString("PR_BILL_WIDTH"), NewRow);
//                DataModel_Planning.setValueByVariable("GSM", resultSet.getString("PR_BILL_GSM"), NewRow);
//                DataModel_Planning.setValueByVariable("STYLE", resultSet.getString("PR_BILL_STYLE"), NewRow);
//                DataModel_Planning.setValueByVariable("WEIGHT", resultSet.getString("PR_BILL_WEIGHT"), NewRow);
//                DataModel_Planning.setValueByVariable("SQMTR", resultSet.getString("PR_BILL_SQMTR"), NewRow);
                DataModel_Planning.setValueByVariable("LENGTH", resultSet.getString("PR_LENGTH"), NewRow);
                DataModel_Planning.setValueByVariable("WIDTH", resultSet.getString("PR_WIDTH"), NewRow);
                DataModel_Planning.setValueByVariable("GSM", resultSet.getString("PR_GSM"), NewRow);
                DataModel_Planning.setValueByVariable("STYLE", resultSet.getString("PR_STYLE"), NewRow);
                DataModel_Planning.setValueByVariable("WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"), NewRow);
                DataModel_Planning.setValueByVariable("SQMTR", resultSet.getString("PR_SQMTR"), NewRow);

                try {
                    String incharge = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD=" + resultSet.getString("PR_INCHARGE") + "");
                    DataModel_Planning.setValueByVariable("ZONE", incharge, NewRow);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    TotalInvoiceAmt = TotalInvoiceAmt + Double.parseDouble(resultSet.getString("PR_FELT_VALUE_WITH_GST"));
                } catch (Exception e) {
                }

                if (!"".equals(DataModel_Planning.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", NewRow))) {
                    for (int i = 0; i < DataModel_Planning.getColumnCount(); i++) {
                        Renderer_planning.setBackColor(NewRow, i, Color.yellow);
                    }
                } else {
                    for (int i = 0; i < DataModel_Planning.getColumnCount(); i++) {
                        Renderer_planning.setBackColor(NewRow, i, Color.white);
                    }
                }

                Renderer.setBackColor(NewRow, DataModel_Planning.getColFromVariable("UPN_PIECES"), Color.lightGray);
                Renderer_invoiced.setBackColor(NewRow, DataModel_Planning.getColFromVariable("PR_PARTY_CODE"), Color.lightGray);
            }
            //System.out.println("");
            txtTotalPlanning.setText(BigDecimal.valueOf(EITLERPGLOBAL.round(TotalInvoiceAmt, 2)) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGridWIP() {
        try {
            DataModel_WIP = new EITLTableModel();
            tblWIP.removeAll();

            tblWIP.setModel(DataModel_WIP);
            tblWIP.setAutoResizeMode(0);

            DataModel_WIP.addColumn("SrNo"); //0 - Read Only
            DataModel_WIP.addColumn("CONF OC MTH"); //0 - Read Only
            DataModel_WIP.addColumn("PIECE NO"); //1
            DataModel_WIP.addColumn("PARTY CODE"); //5
            DataModel_WIP.addColumn("PARTY NAME"); //6
            DataModel_WIP.addColumn("MAC NO"); //2
            DataModel_WIP.addColumn("POS NO"); //3
            DataModel_WIP.addColumn("POS DESC"); //4
            DataModel_WIP.addColumn("UPN PIECES"); //11
            DataModel_WIP.addColumn("REQ MTH"); //11
            DataModel_WIP.addColumn("RESCH. REQ MTH"); //11
            DataModel_WIP.addColumn("OC MONTH"); //11
            DataModel_WIP.addColumn("CURR SALES PLAN MTH"); //11
            DataModel_WIP.addColumn("SPECIAL REQ MTH"); //9
            DataModel_WIP.addColumn("SPECIAL REQ DATE"); //9
            DataModel_WIP.addColumn("EXP DELIVERY DATE"); //7
            DataModel_WIP.addColumn("ACT DELIVERY DATE"); //7
            DataModel_WIP.addColumn("EXP PI DATE"); //7
            DataModel_WIP.addColumn("ACT PI DATE"); //7
            DataModel_WIP.addColumn("EXP PAY/CHQ RCV DATE"); //7
            DataModel_WIP.addColumn("EXP PAY CHQ RCV REMARKS"); //7
            DataModel_WIP.addColumn("ACT PAY/CHQ RCV DATE"); //7
            DataModel_WIP.addColumn("EXP DISPATCH DATE"); //7
            DataModel_WIP.addColumn("ACT DISPATCH DATE"); //7
            DataModel_WIP.addColumn("GROUP"); //8
            DataModel_WIP.addColumn("PIECE STAGE"); //9
            DataModel_WIP.addColumn("INVOICE VALUE"); //9
            DataModel_WIP.addColumn("CONTACT PERSON"); //1
            DataModel_WIP.addColumn("PHONE NO"); //1
            DataModel_WIP.addColumn("EMAIL ID"); //1
            DataModel_WIP.addColumn("EMAIL ID2"); //1
            DataModel_WIP.addColumn("EMAIL ID3"); //1
            DataModel_WIP.addColumn("LENGTH"); //1
            DataModel_WIP.addColumn("WIDTH"); //1
            DataModel_WIP.addColumn("GSM"); //1
            DataModel_WIP.addColumn("STYLE"); //1
            DataModel_WIP.addColumn("WEIGHT"); //1
            DataModel_WIP.addColumn("SQMTR"); //1
            DataModel_WIP.addColumn("ZONE"); //1

            DataModel_WIP.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_WIP.SetVariable(1, "SELECT"); //0 - Read Only
            DataModel_WIP.SetVariable(2, "PR_PIECE_NO"); //1
            DataModel_WIP.SetVariable(3, "PR_PARTY_CODE"); //1
            DataModel_WIP.SetVariable(4, "PARTY_NAME"); //1
            DataModel_WIP.SetVariable(5, "PR_MACHINE_NO"); //1
            DataModel_WIP.SetVariable(6, "PR_POSITION_NO"); //1
            DataModel_WIP.SetVariable(7, "POSITION_DESC"); //1
            DataModel_WIP.SetVariable(8, "UPN_PIECES"); //1
            DataModel_WIP.SetVariable(9, "PR_REQUESTED_MONTH"); //1
            DataModel_WIP.SetVariable(10, "RESCHEDULE_MONTH"); //1
            DataModel_WIP.SetVariable(11, "OC_MONTH"); //1
            DataModel_WIP.SetVariable(12, "CURRENT_SCHEDULE_MONTH"); //1
            DataModel_WIP.SetVariable(13, "PR_SPL_REQUEST_MONTHYEAR"); //1
            DataModel_WIP.SetVariable(14, "PR_SPL_REQUEST_DATE"); //1
            DataModel_WIP.SetVariable(15, "EXP_WIP_DELIVERY_DATE"); //1
            DataModel_WIP.SetVariable(16, "ACT_WIP_DELIVERY_DATE"); //1
            DataModel_WIP.SetVariable(17, "EXP_PI_DATE"); //1
            DataModel_WIP.SetVariable(18, "ACT_PI_DATE"); //1
            DataModel_WIP.SetVariable(19, "EXP_PAY_CHQ_RCV_DATE"); //1
            DataModel_WIP.SetVariable(20, "PR_EXP_PAY_CHQRC_REMARKS"); //1
            DataModel_WIP.SetVariable(21, "ACT_PAY_CHQ_RCV_DATE"); //old 20
            DataModel_WIP.SetVariable(22, "EXP_DISPATCH_DATE"); //1
            DataModel_WIP.SetVariable(23, "ACT_DISPATCH_DATE"); //1
            DataModel_WIP.SetVariable(24, "PR_GROUP"); //1
            DataModel_WIP.SetVariable(25, "PR_PIECE_STAGE"); //1
            DataModel_WIP.SetVariable(26, "INVOICE_VALUE"); //1
            DataModel_WIP.SetVariable(27, "CONTACT_PERSON"); //1
            DataModel_WIP.SetVariable(28, "PHONE_NO"); //1
            DataModel_WIP.SetVariable(29, "EMAIL_ID"); //1
            DataModel_WIP.SetVariable(30, "EMAIL_ID2"); //1
            DataModel_WIP.SetVariable(31, "EMAIL_ID3"); //1
            DataModel_WIP.SetVariable(32, "LENGTH"); //1
            DataModel_WIP.SetVariable(33, "WIDTH"); //1
            DataModel_WIP.SetVariable(34, "GSM"); //1
            DataModel_WIP.SetVariable(35, "STYLE"); //1
            DataModel_WIP.SetVariable(36, "WEIGHT"); //1
            DataModel_WIP.SetVariable(37, "SQMTR"); //1
            DataModel_WIP.SetVariable(38, "ZONE"); //1

            for (int i = 0; i < DataModel_WIP.getColumnCount(); i++) {
                if (i != 1 && i != 10 && i != 15 && i != 14 && i != 19 && i != 20 && i != 27 && i != 28 && i != 29 && i != 30 && i != 31) {
                    DataModel_WIP.SetReadOnly(i);
                }

//                  Disable for Testing only     
                Date date = new Date();
                //if(!(date.getDate()>=15 && date.getDate()<=25)) //On 26/04/2019
//                if(!(date.getDate()>=15 && date.getDate()<=26))
                //{
                //    DataModel_WIP.SetReadOnly(1);
                //}

                if (LOGIN_USER_TYPE.equals("PPC") || LOGIN_USER_TYPE.equals("OTHER")) {
                    DataModel_WIP.SetReadOnly(10);
                    DataModel_WIP.SetReadOnly(13);
                    DataModel_WIP.SetReadOnly(14);
                    DataModel_WIP.SetReadOnly(20);
                }
                if (LOGIN_USER_TYPE.equals("SALES") || LOGIN_USER_TYPE.equals("OTHER")) {
                    DataModel_WIP.SetReadOnly(15);
                }
                tblWIP.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_wip);
            }

            tblWIP.getColumnModel().getColumn(0).setMaxWidth(40);
            tblWIP.getColumnModel().getColumn(1).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(2).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(3).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(4).setMinWidth(150);
            tblWIP.getColumnModel().getColumn(5).setMinWidth(70);
            tblWIP.getColumnModel().getColumn(6).setMinWidth(0);
            tblWIP.getColumnModel().getColumn(6).setMaxWidth(0);
            tblWIP.getColumnModel().getColumn(7).setMinWidth(130);
            tblWIP.getColumnModel().getColumn(8).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(9).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(10).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(11).setMinWidth(120);
            tblWIP.getColumnModel().getColumn(12).setMinWidth(150);
            tblWIP.getColumnModel().getColumn(13).setMinWidth(120);
            tblWIP.getColumnModel().getColumn(14).setMinWidth(120);
            tblWIP.getColumnModel().getColumn(15).setMinWidth(150);
            tblWIP.getColumnModel().getColumn(16).setMinWidth(180);
            tblWIP.getColumnModel().getColumn(17).setMinWidth(140);
            tblWIP.getColumnModel().getColumn(18).setMinWidth(180);
            tblWIP.getColumnModel().getColumn(19).setMinWidth(150);

            tblWIP.getColumnModel().getColumn(20).setMinWidth(200);

            tblWIP.getColumnModel().getColumn(21).setMinWidth(180);
            tblWIP.getColumnModel().getColumn(22).setMinWidth(140);
            tblWIP.getColumnModel().getColumn(23).setMinWidth(180);
            tblWIP.getColumnModel().getColumn(24).setMinWidth(90);
            tblWIP.getColumnModel().getColumn(25).setMinWidth(120);
            tblWIP.getColumnModel().getColumn(26).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(27).setMinWidth(130);
            tblWIP.getColumnModel().getColumn(28).setMinWidth(120);
            tblWIP.getColumnModel().getColumn(29).setMinWidth(130);
            tblWIP.getColumnModel().getColumn(30).setMinWidth(130);
            tblWIP.getColumnModel().getColumn(31).setMinWidth(130);
            tblWIP.getColumnModel().getColumn(32).setMinWidth(90);
            tblWIP.getColumnModel().getColumn(33).setMinWidth(90);
            tblWIP.getColumnModel().getColumn(34).setMinWidth(90);
            tblWIP.getColumnModel().getColumn(35).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(36).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(37).setMinWidth(100);
            tblWIP.getColumnModel().getColumn(38).setMinWidth(100);

            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            int ImportCol = 1;
            Renderer.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setEnabled(true);
            aCheckBox.setSelected(false);

            aCheckBox.addActionListener(new ActionListener() {//add actionlistner to listen for change
                @Override
                public void actionPerformed(ActionEvent e) {

                    String OC_MONTH = DataModel_WIP.getValueByVariable("OC_MONTH", tblWIP.getSelectedRow());
                    if ("".equals(OC_MONTH)) {

                    } else {
                        tblWIP.setValueAt(false, tblWIP.getSelectedRow(), 1);
                    }
                }
            });

            tblWIP.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblWIP.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
            tblWIP.getColumnModel().getColumn(DataModel_WIP.getColFromVariable("UPN_PIECES")).setCellRenderer(this.Renderer);
            tblWIP.getColumnModel().getColumn(DataModel_WIP.getColFromVariable("PR_PARTY_CODE")).setCellRenderer(this.Renderer_invoiced);
            tblWIP.getColumnModel().getColumn(DataModel_WIP.getColFromVariable("PR_SPL_REQUEST_MONTHYEAR")).setCellRenderer(this.Renderer_wip);
//            
            TableColumn dateColumn_RESCH = tblWIP.getColumnModel().getColumn(DataModel_WIP.getColFromVariable("RESCHEDULE_MONTH"));
            TableColumn dateColumn_OC_MONTH = tblWIP.getColumnModel().getColumn(DataModel_WIP.getColFromVariable("OC_MONTH"));
            TableColumn dateColumn_PR_SPL_REQUEST_MONTHYEAR = tblWIP.getColumnModel().getColumn(DataModel_WIP.getColFromVariable("PR_SPL_REQUEST_MONTHYEAR"));
            JComboBox monthbox = new JComboBox();

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
//                month = date.getMonth()+3;
//            }
            month = date.getMonth() + 3;
            if (rdoSDF.isSelected()) {
                month = date.getMonth() + 2;
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

            JComboBox monthbox1 = new JComboBox();

            String month_name1 = "";
            Date date1 = new Date();
            int month1;
            int year1 = date1.getYear() + 1900;
            month1 = date1.getMonth();

            monthbox1.addItem("");
            for (int i = 0; i < 40; i++) {
                month1 = month1 + 1;

                if (month1 >= 13) {
                    month1 = 1;
                    year1 = year1 + 1;
                }

                if (month1 == 1) {
                    month_name1 = "Jan";
                } else if (month1 == 2) {
                    month_name1 = "Feb";
                } else if (month1 == 3) {
                    month_name1 = "Mar";
                } else if (month1 == 4) {
                    month_name1 = "Apr";
                } else if (month1 == 5) {
                    month_name1 = "May";
                } else if (month1 == 6) {
                    month_name1 = "Jun";
                } else if (month1 == 7) {
                    month_name1 = "Jul";
                } else if (month1 == 8) {
                    month_name1 = "Aug";
                } else if (month1 == 9) {
                    month_name1 = "Sep";
                } else if (month1 == 10) {
                    month_name1 = "Oct";
                } else if (month1 == 11) {
                    month_name1 = "Nov";
                } else if (month1 == 12) {
                    month_name1 = "Dec";
                }
                monthbox1.addItem(month_name1 + " - " + year1);
            }

            dateColumn_OC_MONTH.setCellEditor(new DefaultCellEditor(monthbox));
            dateColumn_PR_SPL_REQUEST_MONTHYEAR.setCellEditor(new DefaultCellEditor(monthbox1));

            monthbox.addActionListener(new ActionListener() {//add actionlistner to listen for change
                @Override
                public void actionPerformed(ActionEvent e) {

                        //String s = (String) date1.getSelectedItem();//get the selected item
                    //JOptionPane.showMessageDialog(null, "Event on row "+Table.getSelectedRow()+" col "+Table.getSelectedRow());
                    String OC_MONTH = DataModel_WIP.getValueByVariable("OC_MONTH", tblWIP.getSelectedRow());
                    if ("".equals(OC_MONTH)) {

                    } else {
                        tblWIP.setValueAt("", tblWIP.getSelectedRow(), tblWIP.getSelectedColumn());
                    }
                }
            });

            dateColumn_RESCH.setCellEditor(new DefaultCellEditor(monthbox));
//
//            EITLTableCellRenderer Renderer2 = new EITLTableCellRenderer();
//            int col_no = 8;
//            Renderer2.setCustomComponent(col_no, "JTextField");
//            final JTextField abtn = new JTextField();
//            abtn.setBorder(new BevelBorder(1));
//            abtn.setBackground(Color.LIGHT_GRAY);
//            abtn.setEditable(false);
//            abtn.setVisible(true);
//            abtn.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e){
//                        searchkey_UPNwise search_check = new searchkey_UPNwise();
//                        search_check.SQL = "SELECT PR_PIECE_NO,PR_UPN,PR_OC_MONTHYEAR,PR_PIECE_STAGE,PR_REQUESTED_MONTH,PR_MACHINE_NO,PR_POSITION_NO, PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP, " +
//" PR_WIP_STATUS FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_UPN='"+abtn.getText()+"' AND PR_OC_MONTHYEAR='' AND PR_PIECE_STAGE IN ('PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR')";
//                        search_check.ReturnCol = 1;
//                        search_check.UPN = abtn.getText();
//                        search_check.ShowReturnCol = true;
//                        search_check.DefaultSearchOn = 1;
//                        
//                        if (search_check.ShowRSLOV()) {
//                            
//                        }
//                    }
//            });
//            tblWIP.getColumnModel().getColumn(col_no).setCellEditor(new DefaultCellEditor(abtn));
//            tblWIP.getColumnModel().getColumn(col_no).setCellRenderer(Renderer2);

            tblWIP.getColumnModel().getSelectionModel().addListSelectionListener(
                    new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent e) {
                            int column = tblWIP.getSelectedColumn();
                            String strVar = DataModel_WIP.getVariable(column);
                            //=============== Cell Editing Routine =======================//
                            tblWIP.editCellAt(tblWIP.getSelectedRow(), column);
                            if (tblWIP.getEditorComponent() instanceof JTextComponent) {
                                ((JTextComponent) tblWIP.getEditorComponent()).selectAll();
                            }
                                //System.out.println("Clicked");
                            //============= Cell Editing Routine Ended =================//
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWIPPieces() {
        try {
            if (cmbMonthYear.getSelectedItem() == null) {
                return;
            }
            FormatGridWIP();

            String str_query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3 FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR "
                    + " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS "
                    + "ON PS.POSITION_NO = PR.PR_POSITION_NO "
                    + "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM "
                    + "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                    + "WHERE PR.PR_PIECE_NO!='' ";

//            if(!txtProductCode.getText().equals(""))
//            {
//                str_query = str_query + " AND PR.PR_PRODUCT_CODE="+txtProductCode.getText();
//            }
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }

            if (!cmbPieceStage.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_PIECE_STAGE = '" + cmbPieceStage.getSelectedItem() + "' ";
            }

            if (rdoRequestMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_REQUESTED_MONTH='" + cmbMonthYear.getSelectedItem() + "'";
            } else if (rdoOcMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_OC_MONTHYEAR='" + cmbMonthYear.getSelectedItem() + "'";
            } else if (rdoCurSchMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_CURRENT_SCH_MONTH='" + cmbMonthYear.getSelectedItem() + "'";

            }

            if (rdoExceptSDF.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP!='SDF'";
            } else if (rdoSDF.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP='SDF'";
            }

            if (!"".equals(txtPartyCode.getText())) {
                str_query = str_query + " AND PR.PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }

            if (!"".equals(txtUPN.getText())) {
                str_query = str_query + " AND PR.PR_UPN='" + txtUPN.getText() + "'";
            }

            String NewPieces = "";
            if (!txtPieceNo.getText().equals("")) {
                String PieceNo = txtPieceNo.getText();
                String Pieces[] = PieceNo.split(",");

                for (String Piece : Pieces) {
                    if (NewPieces.equals("")) {
                        NewPieces = "'" + Piece + "'";
                    } else {
                        NewPieces = NewPieces + ",'" + Piece + "'";
                    }
                }
            }

            if (!NewPieces.equals("")) {
                str_query = str_query + " AND PR.PR_PIECE_NO IN (" + NewPieces + ") ";
            }

            str_query = str_query + " AND PR_PIECE_STAGE IN ('WEAVING','MENDING','NEEDLING','FINISHING','SPLICING','SEAMING','GIDC','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) ";

            str_query = str_query + ORDER_BY;

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("WIP Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            double TotalInvoiceAmt = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_WIP.addRow(rowData);

                DataModel_WIP.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_WIP.setValueByVariable("SELECT", Boolean.FALSE, NewRow);
                DataModel_WIP.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_WIP.setValueByVariable("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"), NewRow);
                DataModel_WIP.setValueByVariable("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"), NewRow);
                DataModel_WIP.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);
                DataModel_WIP.setValueByVariable("UPN_PIECES", resultSet.getString("PR_UPN"), NewRow);
                DataModel_WIP.setValueByVariable("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"), NewRow);

                DataModel_WIP.setValueByVariable("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                DataModel_WIP.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, resultSet.getString("PR_PARTY_CODE")), NewRow);

                DataModel_WIP.setValueByVariable("PR_GROUP", resultSet.getString("PR_GROUP"), NewRow);
                DataModel_WIP.setValueByVariable("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"), NewRow);
                DataModel_WIP.setValueByVariable("OC_MONTH", resultSet.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_WIP.setValueByVariable("CURRENT_SCHEDULE_MONTH", resultSet.getString("PR_CURRENT_SCH_MONTH"), NewRow);

                DataModel_WIP.setValueByVariable("EXP_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_WIP_DELIVERY_DATE")), NewRow);
                DataModel_WIP.setValueByVariable("ACT_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_FNSG_DATE")), NewRow);

                DataModel_WIP.setValueByVariable("EXP_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PI_DATE")), NewRow);
                DataModel_WIP.setValueByVariable("ACT_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PI_DATE")), NewRow);

                DataModel_WIP.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PAY_CHQRC_DATE")), NewRow);

                DataModel_WIP.setValueByVariable("PR_EXP_PAY_CHQRC_REMARKS", resultSet.getString("PR_EXP_PAY_CHQRC_REMARKS"), NewRow);

                DataModel_WIP.setValueByVariable("ACT_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PAY_CHQRC_DATE")), NewRow);
                DataModel_WIP.setValueByVariable("EXP_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_DESPATCH_DATE")), NewRow);
                DataModel_WIP.setValueByVariable("ACT_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_INVOICE_DATE")), NewRow);

                DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", resultSet.getString("PR_SPL_REQUEST_MONTHYEAR"), NewRow);
                DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_SPL_REQUEST_DATE")), NewRow);
                DataModel_WIP.setValueByVariable("INVOICE_VALUE", resultSet.getString("PR_FELT_VALUE_WITH_GST"), NewRow);

                DataModel_WIP.setValueByVariable("CONTACT_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_WIP.setValueByVariable("PHONE_NO", resultSet.getString("PHONE_NO"), NewRow);
                DataModel_WIP.setValueByVariable("EMAIL_ID", resultSet.getString("EMAIL"), NewRow);
                DataModel_WIP.setValueByVariable("EMAIL_ID2", resultSet.getString("EMAIL_ID2"), NewRow);
                DataModel_WIP.setValueByVariable("EMAIL_ID3", resultSet.getString("EMAIL_ID3"), NewRow);

//                DataModel_WIP.setValueByVariable("LENGTH", resultSet.getString("PR_BILL_LENGTH"), NewRow);
//                DataModel_WIP.setValueByVariable("WIDTH", resultSet.getString("PR_BILL_WIDTH"), NewRow);
//                DataModel_WIP.setValueByVariable("GSM", resultSet.getString("PR_BILL_GSM"), NewRow);
//                DataModel_WIP.setValueByVariable("STYLE", resultSet.getString("PR_BILL_STYLE"), NewRow);
//                DataModel_WIP.setValueByVariable("WEIGHT", resultSet.getString("PR_BILL_WEIGHT"), NewRow);
//                DataModel_WIP.setValueByVariable("SQMTR", resultSet.getString("PR_BILL_SQMTR"), NewRow);
                DataModel_WIP.setValueByVariable("LENGTH", resultSet.getString("PR_LENGTH"), NewRow);
                DataModel_WIP.setValueByVariable("WIDTH", resultSet.getString("PR_WIDTH"), NewRow);
                DataModel_WIP.setValueByVariable("GSM", resultSet.getString("PR_GSM"), NewRow);
                DataModel_WIP.setValueByVariable("STYLE", resultSet.getString("PR_STYLE"), NewRow);
                DataModel_WIP.setValueByVariable("WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"), NewRow);
                DataModel_WIP.setValueByVariable("SQMTR", resultSet.getString("PR_SQMTR"), NewRow);

                try {
                    DataModel_WIP.setValueByVariable("ZONE", data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD=" + resultSet.getString("PR_INCHARGE") + ""), NewRow);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TotalInvoiceAmt = TotalInvoiceAmt + Double.parseDouble(resultSet.getString("PR_FELT_VALUE_WITH_GST"));
                } catch (Exception e) {
                }

                if (!"".equals(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", NewRow))) {
                    for (int i = 0; i < DataModel_WIP.getColumnCount(); i++) {
                        Renderer_wip.setBackColor(NewRow, i, Color.yellow);
                    }
                } else {
                    for (int i = 0; i < DataModel_WIP.getColumnCount(); i++) {
                        Renderer_wip.setBackColor(NewRow, i, Color.white);
                    }
                }

                Renderer.setBackColor(NewRow, DataModel_WIP.getColFromVariable("UPN_PIECES"), Color.lightGray);
                Renderer_invoiced.setBackColor(NewRow, DataModel_WIP.getColFromVariable("PR_PARTY_CODE"), Color.lightGray);

            }

            txtTotalWIP.setText(BigDecimal.valueOf(EITLERPGLOBAL.round(TotalInvoiceAmt, 2)) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void FormatGridSTOCK() {
        try {
            DataModel_STOCK = new EITLTableModel();
            tblStockBsr.removeAll();

            tblStockBsr.setModel(DataModel_STOCK);
            tblStockBsr.setAutoResizeMode(0);

            DataModel_STOCK.addColumn("SrNo"); //0 - Read Only
            DataModel_STOCK.addColumn("CONF OC MTH"); //0 - Read Only
            DataModel_STOCK.addColumn("PIECE NO"); //1
            DataModel_STOCK.addColumn("PARTY CODE"); //5
            DataModel_STOCK.addColumn("PARTY NAME"); //6
            DataModel_STOCK.addColumn("MAC NO"); //2
            DataModel_STOCK.addColumn("POS NO"); //3
            DataModel_STOCK.addColumn("POS DESC"); //4
            DataModel_STOCK.addColumn("UPN PIECES"); //11
            DataModel_STOCK.addColumn("REQ MTH"); //11
            DataModel_STOCK.addColumn("RESCH. REQ MTH"); //11
            DataModel_STOCK.addColumn("OC MONTH"); //11
            DataModel_STOCK.addColumn("CURR SALES PLAN MTH"); //11
            DataModel_STOCK.addColumn("SPECIAL REQ MTH"); //9
            DataModel_STOCK.addColumn("SPECIAL REQ DATE"); //9
            DataModel_STOCK.addColumn("EXP DELIVERY DATE"); //7
            DataModel_STOCK.addColumn("ACT DELIVERY DATE"); //7
            DataModel_STOCK.addColumn("EXP PI DATE"); //16
            DataModel_STOCK.addColumn("ACT PI DATE"); //17
            DataModel_STOCK.addColumn("EXP PAY/CHQ RCV DATE"); //7
            DataModel_STOCK.addColumn("EXP PAY CHQ RCV REMARKS"); //7
            DataModel_STOCK.addColumn("ACT PAY/CHQ RCV DATE"); //7
            DataModel_STOCK.addColumn("EXP DISPATCH DATE"); //7
            DataModel_STOCK.addColumn("ACT DISPATCH DATE"); //7
            DataModel_STOCK.addColumn("GROUP"); //8
            DataModel_STOCK.addColumn("PIECE STAGE"); //9
            DataModel_STOCK.addColumn("INVOICE VALUE"); //9
            DataModel_STOCK.addColumn("CONTACT PERSON"); //1
            DataModel_STOCK.addColumn("PHONE NO"); //1
            DataModel_STOCK.addColumn("EMAIL ID"); //1
            DataModel_STOCK.addColumn("EMAIL ID2"); //1
            DataModel_STOCK.addColumn("EMAIL ID3"); //1
            DataModel_STOCK.addColumn("LENGTH"); //1
            DataModel_STOCK.addColumn("WIDTH"); //1
            DataModel_STOCK.addColumn("GSM"); //1
            DataModel_STOCK.addColumn("STYLE"); //1
            DataModel_STOCK.addColumn("WEIGHT"); //1
            DataModel_STOCK.addColumn("SQMTR"); //1
            DataModel_STOCK.addColumn("ZONE"); //1

            DataModel_STOCK.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_STOCK.SetVariable(1, "SELECT"); //0 - Read Only
            DataModel_STOCK.SetVariable(2, "PR_PIECE_NO"); //1
            DataModel_STOCK.SetVariable(3, "PR_PARTY_CODE"); //1
            DataModel_STOCK.SetVariable(4, "PARTY_NAME"); //1
            DataModel_STOCK.SetVariable(5, "PR_MACHINE_NO"); //1
            DataModel_STOCK.SetVariable(6, "PR_POSITION_NO"); //1
            DataModel_STOCK.SetVariable(7, "POSITION_DESC"); //1
            DataModel_STOCK.SetVariable(8, "UPN_PIECES"); //1
            DataModel_STOCK.SetVariable(9, "PR_REQUESTED_MONTH"); //1
            DataModel_STOCK.SetVariable(10, "RESCHEDULE_MONTH"); //1
            DataModel_STOCK.SetVariable(11, "OC_MONTH"); //1
            DataModel_STOCK.SetVariable(12, "CURRENT_SCHEDULE_MONTH"); //1
            DataModel_STOCK.SetVariable(13, "PR_SPL_REQUEST_MONTHYEAR"); //1
            DataModel_STOCK.SetVariable(14, "PR_SPL_REQUEST_DATE"); //1
            DataModel_STOCK.SetVariable(15, "EXP_WIP_DELIVERY_DATE"); //1
            DataModel_STOCK.SetVariable(16, "ACT_WIP_DELIVERY_DATE"); //1
            DataModel_STOCK.SetVariable(17, "EXP_PI_DATE"); //1
            DataModel_STOCK.SetVariable(18, "ACT_PI_DATE"); //1
            DataModel_STOCK.SetVariable(19, "EXP_PAY_CHQ_RCV_DATE"); //1

            DataModel_STOCK.SetVariable(20, "PR_EXP_PAY_CHQRC_REMARKS"); //1

            DataModel_STOCK.SetVariable(21, "ACT_PAY_CHQ_RCV_DATE"); //OLD COL 20
            DataModel_STOCK.SetVariable(22, "EXP_DISPATCH_DATE"); //1
            DataModel_STOCK.SetVariable(23, "ACT_DISPATCH_DATE"); //1
            DataModel_STOCK.SetVariable(24, "PR_GROUP"); //1
            DataModel_STOCK.SetVariable(25, "PR_PIECE_STAGE"); //1
            DataModel_STOCK.SetVariable(26, "INVOICE_VALUE"); //1
            DataModel_STOCK.SetVariable(27, "CONTACT_PERSON"); //1
            DataModel_STOCK.SetVariable(28, "PHONE_NO"); //1
            DataModel_STOCK.SetVariable(29, "EMAIL_ID"); //1
            DataModel_STOCK.SetVariable(30, "EMAIL_ID2"); //1
            DataModel_STOCK.SetVariable(31, "EMAIL_ID3"); //1
            DataModel_STOCK.SetVariable(32, "LENGTH"); //1
            DataModel_STOCK.SetVariable(33, "WIDTH"); //1
            DataModel_STOCK.SetVariable(34, "GSM"); //1
            DataModel_STOCK.SetVariable(35, "STYLE"); //1
            DataModel_STOCK.SetVariable(36, "WEIGHT"); //1
            DataModel_STOCK.SetVariable(37, "SQMTR"); //1
            DataModel_STOCK.SetVariable(38, "ZONE"); //1

            for (int i = 0; i < DataModel_STOCK.getColumnCount(); i++) {
                if (i != 19 && i != 20) {
                    DataModel_STOCK.SetReadOnly(i);
                }
                tblStockBsr.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_stock);
            }

            tblStockBsr.getColumnModel().getColumn(0).setMaxWidth(40);
            tblStockBsr.getColumnModel().getColumn(1).setMinWidth(0);
            tblStockBsr.getColumnModel().getColumn(1).setMaxWidth(0);
            tblStockBsr.getColumnModel().getColumn(2).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(3).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(4).setMinWidth(150);
            tblStockBsr.getColumnModel().getColumn(5).setMinWidth(70);
            tblStockBsr.getColumnModel().getColumn(6).setMinWidth(0);
            tblStockBsr.getColumnModel().getColumn(6).setMaxWidth(0);
            tblStockBsr.getColumnModel().getColumn(7).setMinWidth(130);
            tblStockBsr.getColumnModel().getColumn(8).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(9).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(10).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(11).setMinWidth(120);
            tblStockBsr.getColumnModel().getColumn(12).setMinWidth(150);
            tblStockBsr.getColumnModel().getColumn(13).setMinWidth(120);
            tblStockBsr.getColumnModel().getColumn(14).setMinWidth(120);
            tblStockBsr.getColumnModel().getColumn(15).setMinWidth(150);
            tblStockBsr.getColumnModel().getColumn(16).setMinWidth(180);
            tblStockBsr.getColumnModel().getColumn(17).setMinWidth(140);
            tblStockBsr.getColumnModel().getColumn(18).setMinWidth(180);
            tblStockBsr.getColumnModel().getColumn(19).setMinWidth(150);

            tblStockBsr.getColumnModel().getColumn(20).setMinWidth(200);

            tblStockBsr.getColumnModel().getColumn(21).setMinWidth(180);
            tblStockBsr.getColumnModel().getColumn(22).setMinWidth(140);
            tblStockBsr.getColumnModel().getColumn(23).setMinWidth(180);
            tblStockBsr.getColumnModel().getColumn(24).setMinWidth(90);
            tblStockBsr.getColumnModel().getColumn(25).setMinWidth(120);
            tblStockBsr.getColumnModel().getColumn(26).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(27).setMinWidth(130);
            tblStockBsr.getColumnModel().getColumn(28).setMinWidth(130);
            tblStockBsr.getColumnModel().getColumn(29).setMinWidth(130);
            tblStockBsr.getColumnModel().getColumn(30).setMinWidth(130);
            tblStockBsr.getColumnModel().getColumn(31).setMinWidth(130);
            tblStockBsr.getColumnModel().getColumn(32).setMinWidth(90);
            tblStockBsr.getColumnModel().getColumn(33).setMinWidth(90);
            tblStockBsr.getColumnModel().getColumn(34).setMinWidth(90);
            tblStockBsr.getColumnModel().getColumn(35).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(36).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(37).setMinWidth(100);
            tblStockBsr.getColumnModel().getColumn(38).setMinWidth(100);

            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            int ImportCol = 1;
            Renderer.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setEnabled(true);
            aCheckBox.setSelected(false);

            tblStockBsr.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblStockBsr.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
            tblStockBsr.getColumnModel().getColumn(DataModel_STOCK.getColFromVariable("UPN_PIECES")).setCellRenderer(this.Renderer);
            tblStockBsr.getColumnModel().getColumn(DataModel_STOCK.getColFromVariable("PR_PARTY_CODE")).setCellRenderer(this.Renderer_invoiced);
            tblStockBsr.getColumnModel().getColumn(DataModel_STOCK.getColFromVariable("PR_SPL_REQUEST_MONTHYEAR")).setCellRenderer(this.Renderer_stock);

            TableColumn dateColumn_RESCH = tblStockBsr.getColumnModel().getColumn(DataModel_STOCK.getColFromVariable("RESCHEDULE_MONTH"));
            TableColumn dateColumn_OC_MONTH = tblStockBsr.getColumnModel().getColumn(DataModel_STOCK.getColFromVariable("OC_MONTH"));
            JComboBox monthbox = new JComboBox();

            String month_name = "";
            Date date = new Date();
            int month;
            int year = date.getYear() + 1900;

            if (date.getDate() <= 15) {
                month = date.getMonth() + 2;
                if (rdoSDF.isSelected()) {
                    month = date.getMonth() + 1;
                }
            } else {
                month = date.getMonth() + 3;
                if (rdoSDF.isSelected()) {
                    month = date.getMonth() + 2;
                }
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

            dateColumn_RESCH.setCellEditor(new DefaultCellEditor(monthbox));
            dateColumn_OC_MONTH.setCellEditor(new DefaultCellEditor(monthbox));
//            EITLTableCellRenderer Renderer2 = new EITLTableCellRenderer();
//            int col_no = 8;
//            Renderer2.setCustomComponent(col_no, "JTextField");
//            final JTextField abtn = new JTextField();
//            abtn.setBorder(new BevelBorder(1));
//            abtn.setBackground(Color.LIGHT_GRAY);
//            abtn.setEditable(false);
//            abtn.setVisible(true);
//            abtn.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e){
//                        searchkey_UPNwise search_check = new searchkey_UPNwise();
//                        search_check.SQL = "SELECT PR_PIECE_NO,PR_UPN,PR_OC_MONTHYEAR,PR_PIECE_STAGE,PR_REQUESTED_MONTH,PR_MACHINE_NO,PR_POSITION_NO, PR_PARTY_CODE,PR_PRODUCT_CODE,PR_GROUP, " +
//" PR_WIP_STATUS FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_UPN='"+abtn.getText()+"' AND PR_OC_MONTHYEAR='' AND PR_PIECE_STAGE IN ('PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR')";
//                        search_check.ReturnCol = 1;
//                        search_check.UPN = abtn.getText();
//                        search_check.ShowReturnCol = true;
//                        search_check.DefaultSearchOn = 1;
//                        
//                        if (search_check.ShowRSLOV()) {
//                            
//                        }
//                    }
//            });
//            tblStockBsr.getColumnModel().getColumn(col_no).setCellEditor(new DefaultCellEditor(abtn));
//            tblStockBsr.getColumnModel().getColumn(col_no).setCellRenderer(Renderer2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSTOCKPieces() {
        try {
            if (cmbMonthYear.getSelectedItem() == null) {
                return;
            }
            FormatGridSTOCK();

            String str_query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3 FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR "
                    + " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS "
                    + "ON PS.POSITION_NO = PR.PR_POSITION_NO "
                    + "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM "
                    + "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                    + "WHERE PR.PR_PIECE_NO!='' ";

            //String str_query = "SELECT PR.*,POSITION_DESC,POSITION_DESIGN_NO  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR,PRODUCTION.FELT_MACHINE_POSITION_MST PS WHERE PS.POSITION_NO = PR.PR_POSITION_NO  ";
            //        + "  AND PR.PR_PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H,PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D WHERE H.OC_DOC_NO=D.OC_DOC_NO AND H.APPROVED=0) ORDER BY PR_PARTY_CODE";
//            if(!txtProductCode.getText().equals(""))
//            {
//                str_query = str_query + " AND PR.PR_PRODUCT_CODE="+txtProductCode.getText();
//            }
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }

            if (!cmbPieceStage.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_PIECE_STAGE = '" + cmbPieceStage.getSelectedItem() + "' ";
            }

            if (rdoRequestMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_REQUESTED_MONTH='" + cmbMonthYear.getSelectedItem() + "'";
            } else if (rdoOcMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_OC_MONTHYEAR='" + cmbMonthYear.getSelectedItem() + "'";
            } else if (rdoCurSchMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_CURRENT_SCH_MONTH='" + cmbMonthYear.getSelectedItem() + "'";
            }

            if (!"".equals(txtPartyCode.getText())) {
                str_query = str_query + " AND PR.PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }

            if (rdoExceptSDF.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP!='SDF'";
            } else if (rdoSDF.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP='SDF'";
            }

            if (!"".equals(txtUPN.getText())) {
                str_query = str_query + " AND PR.PR_UPN='" + txtUPN.getText() + "'";
            }

            String NewPieces = "";
            if (!txtPieceNo.getText().equals("")) {
                String PieceNo = txtPieceNo.getText();
                String Pieces[] = PieceNo.split(",");

                for (String Piece : Pieces) {
                    if (NewPieces.equals("")) {
                        NewPieces = "'" + Piece + "'";
                    } else {
                        NewPieces = NewPieces + ",'" + Piece + "'";
                    }
                }
            }

            if (!NewPieces.equals("")) {
                str_query = str_query + " AND PR.PR_PIECE_NO IN (" + NewPieces + ") ";
            }

            str_query = str_query + " AND PR_PIECE_STAGE IN ('IN STOCK','BSR')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) ";

            str_query = str_query + ORDER_BY;

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("stock Query : " + str_query);
            //println("Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            double TotalInvoiceAmt = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_STOCK.addRow(rowData);

                DataModel_STOCK.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_STOCK.setValueByVariable("SELECT", Boolean.FALSE, NewRow);
                DataModel_STOCK.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_STOCK.setValueByVariable("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"), NewRow);
                DataModel_STOCK.setValueByVariable("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"), NewRow);
                DataModel_STOCK.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);
                DataModel_STOCK.setValueByVariable("UPN_PIECES", resultSet.getString("PR_UPN"), NewRow);
                DataModel_STOCK.setValueByVariable("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"), NewRow);

                DataModel_STOCK.setValueByVariable("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                DataModel_STOCK.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, resultSet.getString("PR_PARTY_CODE")), NewRow);

                DataModel_STOCK.setValueByVariable("PR_GROUP", resultSet.getString("PR_GROUP"), NewRow);
                DataModel_STOCK.setValueByVariable("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"), NewRow);
                DataModel_STOCK.setValueByVariable("OC_MONTH", resultSet.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_STOCK.setValueByVariable("CURRENT_SCHEDULE_MONTH", resultSet.getString("PR_CURRENT_SCH_MONTH"), NewRow);

                DataModel_STOCK.setValueByVariable("EXP_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_WIP_DELIVERY_DATE")), NewRow);
                DataModel_STOCK.setValueByVariable("ACT_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_FNSG_DATE")), NewRow);

                DataModel_STOCK.setValueByVariable("EXP_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PI_DATE")), NewRow);
                DataModel_STOCK.setValueByVariable("ACT_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PI_DATE")), NewRow);

                DataModel_STOCK.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PAY_CHQRC_DATE")), NewRow);

                DataModel_STOCK.setValueByVariable("PR_EXP_PAY_CHQRC_REMARKS", resultSet.getString("PR_EXP_PAY_CHQRC_REMARKS"), NewRow);

                DataModel_STOCK.setValueByVariable("ACT_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PAY_CHQRC_DATE")), NewRow);
                DataModel_STOCK.setValueByVariable("EXP_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_DESPATCH_DATE")), NewRow);
                DataModel_STOCK.setValueByVariable("ACT_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_INVOICE_DATE")), NewRow);

                DataModel_STOCK.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", resultSet.getString("PR_SPL_REQUEST_MONTHYEAR"), NewRow);
                DataModel_STOCK.setValueByVariable("PR_SPL_REQUEST_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_SPL_REQUEST_DATE")), NewRow);
                DataModel_STOCK.setValueByVariable("INVOICE_VALUE", resultSet.getString("PR_FELT_VALUE_WITH_GST"), NewRow);

                DataModel_STOCK.setValueByVariable("CONTACT_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_STOCK.setValueByVariable("PHONE_NO", resultSet.getString("PHONE_NO"), NewRow);
                DataModel_STOCK.setValueByVariable("EMAIL_ID", resultSet.getString("EMAIL"), NewRow);
                DataModel_STOCK.setValueByVariable("EMAIL_ID2", resultSet.getString("EMAIL_ID2"), NewRow);
                DataModel_STOCK.setValueByVariable("EMAIL_ID3", resultSet.getString("EMAIL_ID3"), NewRow);

//                DataModel_STOCK.setValueByVariable("LENGTH", resultSet.getString("PR_BILL_LENGTH"), NewRow);
//                DataModel_STOCK.setValueByVariable("WIDTH", resultSet.getString("PR_BILL_WIDTH"), NewRow);
//                DataModel_STOCK.setValueByVariable("GSM", resultSet.getString("PR_BILL_GSM"), NewRow);
//                DataModel_STOCK.setValueByVariable("STYLE", resultSet.getString("PR_BILL_STYLE"), NewRow); 
//                DataModel_STOCK.setValueByVariable("WEIGHT", resultSet.getString("PR_BILL_WEIGHT"), NewRow); 
//                DataModel_STOCK.setValueByVariable("SQMTR", resultSet.getString("PR_BILL_SQMTR"), NewRow);
                DataModel_STOCK.setValueByVariable("LENGTH", resultSet.getString("PR_LENGTH"), NewRow);
                DataModel_STOCK.setValueByVariable("WIDTH", resultSet.getString("PR_WIDTH"), NewRow);
                DataModel_STOCK.setValueByVariable("GSM", resultSet.getString("PR_GSM"), NewRow);
                DataModel_STOCK.setValueByVariable("STYLE", resultSet.getString("PR_STYLE"), NewRow);
                DataModel_STOCK.setValueByVariable("WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"), NewRow);
                DataModel_STOCK.setValueByVariable("SQMTR", resultSet.getString("PR_SQMTR"), NewRow);

                try {
                    DataModel_STOCK.setValueByVariable("ZONE", data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD=" + resultSet.getString("PR_INCHARGE") + ""), NewRow);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TotalInvoiceAmt = TotalInvoiceAmt + Double.parseDouble(resultSet.getString("PR_FELT_VALUE_WITH_GST"));
                } catch (Exception e) {
                }

                if (!"".equals(DataModel_STOCK.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", NewRow))) {
                    for (int i = 0; i < DataModel_STOCK.getColumnCount(); i++) {
                        Renderer_stock.setBackColor(NewRow, i, Color.yellow);
                    }
                } else {
                    for (int i = 0; i < DataModel_STOCK.getColumnCount(); i++) {
                        Renderer_stock.setBackColor(NewRow, i, Color.white);
                    }
                }

                Renderer.setBackColor(NewRow, DataModel_STOCK.getColFromVariable("UPN_PIECES"), Color.LIGHT_GRAY);
                Renderer_invoiced.setBackColor(NewRow, DataModel_STOCK.getColFromVariable("PR_PARTY_CODE"), Color.LIGHT_GRAY);
            }

            txtTotalStockBSR.setText(BigDecimal.valueOf(EITLERPGLOBAL.round(TotalInvoiceAmt, 2)) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void FormatGridDispatch() {
        try {
            DataModel_Dispatch = new EITLTableModel();
            tblDispatch.removeAll();

            tblDispatch.setModel(DataModel_Dispatch);
            tblDispatch.setAutoResizeMode(0);

            DataModel_Dispatch.addColumn("SrNo"); //1 - Read Only
            DataModel_Dispatch.addColumn("CONF OC MTH"); //2 - Read Only
            DataModel_Dispatch.addColumn("PIECE NO"); //3
            DataModel_Dispatch.addColumn("PARTY CODE"); //4
            DataModel_Dispatch.addColumn("PARTY NAME"); //5
            DataModel_Dispatch.addColumn("MAC NO"); //6
            DataModel_Dispatch.addColumn("POS NO"); //7
            DataModel_Dispatch.addColumn("POS DESC"); //8
            DataModel_Dispatch.addColumn("UPN PIECES"); //9
            DataModel_Dispatch.addColumn("REQ MTH"); //10
            DataModel_Dispatch.addColumn("RESCH. REQ MTH"); //11
            DataModel_Dispatch.addColumn("OC MONTH"); //12
            DataModel_Dispatch.addColumn("CURR SALES PLAN MTH"); //13
            DataModel_Dispatch.addColumn("EXP DELIVERY DATE"); //18
            DataModel_Dispatch.addColumn("ACT DELIVERY DATE"); //19
            DataModel_Dispatch.addColumn("EXP PI DATE"); //16
            DataModel_Dispatch.addColumn("ACT PI DATE"); //17
            DataModel_Dispatch.addColumn("EXP PAY/CHQ RCV DATE"); //14
            DataModel_Dispatch.addColumn("ACT PAY/CHQ RCV DATE"); //15
            DataModel_Dispatch.addColumn("EXP DISPATCH DATE"); //16
            DataModel_Dispatch.addColumn("ACT DISPATCH DATE"); //17
            DataModel_Dispatch.addColumn("GROUP"); //20
            DataModel_Dispatch.addColumn("PIECE STAGE"); //21
            DataModel_Dispatch.addColumn("INVOICE VALUE"); //21
            DataModel_Dispatch.addColumn("CONTACT PERSON"); //1
            DataModel_Dispatch.addColumn("PHONE NO"); //1
            DataModel_Dispatch.addColumn("EMAIL ID"); //1
            DataModel_Dispatch.addColumn("EMAIL ID2"); //1
            DataModel_Dispatch.addColumn("EMAIL ID3"); //1
            DataModel_Dispatch.addColumn("LENGTH"); //1
            DataModel_Dispatch.addColumn("WIDTH"); //1
            DataModel_Dispatch.addColumn("GSM"); //1
            DataModel_Dispatch.addColumn("STYLE"); //1
            DataModel_Dispatch.addColumn("WEIGHT"); //1
            DataModel_Dispatch.addColumn("SQMTR"); //1
            DataModel_Dispatch.addColumn("ZONE"); //1

            DataModel_Dispatch.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_Dispatch.SetVariable(1, "SELECT"); //0 - Read Only
            DataModel_Dispatch.SetVariable(2, "PR_PIECE_NO"); //1
            DataModel_Dispatch.SetVariable(3, "PR_PARTY_CODE"); //1
            DataModel_Dispatch.SetVariable(4, "PARTY_NAME"); //1
            DataModel_Dispatch.SetVariable(5, "PR_MACHINE_NO"); //1
            DataModel_Dispatch.SetVariable(6, "PR_POSITION_NO"); //1
            DataModel_Dispatch.SetVariable(7, "POSITION_DESC"); //1
            DataModel_Dispatch.SetVariable(8, "UPN_PIECES"); //1
            DataModel_Dispatch.SetVariable(9, "PR_REQUESTED_MONTH"); //1
            DataModel_Dispatch.SetVariable(10, "RESCHEDULE_MONTH"); //1
            DataModel_Dispatch.SetVariable(11, "OC_MONTH"); //1
            DataModel_Dispatch.SetVariable(12, "CURRENT_SCHEDULE_MONTH"); //1
            DataModel_Dispatch.SetVariable(13, "EXP_WIP_DELIVERY_DATE"); //1
            DataModel_Dispatch.SetVariable(14, "ACT_WIP_DELIVERY_DATE"); //1
            DataModel_Dispatch.SetVariable(15, "EXP_PI_DATE"); //1
            DataModel_Dispatch.SetVariable(16, "ACT_PI_DATE"); //1
            DataModel_Dispatch.SetVariable(17, "EXP_PAY_CHQ_RCV_DATE"); //1
            DataModel_Dispatch.SetVariable(18, "ACT_PAY_CHQ_RCV_DATE"); //1
            DataModel_Dispatch.SetVariable(19, "EXP_DISPATCH_DATE"); //1
            DataModel_Dispatch.SetVariable(20, "ACT_DISPATCH_DATE"); //1
            DataModel_Dispatch.SetVariable(21, "PR_GROUP"); //1
            DataModel_Dispatch.SetVariable(22, "PR_PIECE_STAGE"); //1
            DataModel_Dispatch.SetVariable(23, "INVOICE_VALUE"); //1
            DataModel_Dispatch.SetVariable(24, "CONTACT_PERSON"); //1
            DataModel_Dispatch.SetVariable(25, "PHONE_NO"); //1
            DataModel_Dispatch.SetVariable(26, "EMAIL_ID"); //1
            DataModel_Dispatch.SetVariable(27, "EMAIL_ID2"); //1
            DataModel_Dispatch.SetVariable(28, "EMAIL_ID3"); //1
            DataModel_Dispatch.SetVariable(29, "LENGTH"); //1
            DataModel_Dispatch.SetVariable(30, "WIDTH"); //1
            DataModel_Dispatch.SetVariable(31, "GSM"); //1
            DataModel_Dispatch.SetVariable(32, "STYLE"); //1
            DataModel_Dispatch.SetVariable(33, "WEIGHT"); //1
            DataModel_Dispatch.SetVariable(34, "SQMTR"); //1
            DataModel_Dispatch.SetVariable(35, "ZONE"); //1

            for (int i = 0; i <= DataModel_Dispatch.getColumnCount(); i++) {
                DataModel_Dispatch.SetReadOnly(i);
            }

            tblDispatch.getColumnModel().getColumn(0).setMaxWidth(40);
            tblDispatch.getColumnModel().getColumn(1).setMinWidth(0);
            tblDispatch.getColumnModel().getColumn(1).setMaxWidth(0);
            tblDispatch.getColumnModel().getColumn(2).setMinWidth(100);
            tblDispatch.getColumnModel().getColumn(3).setMinWidth(100);
            tblDispatch.getColumnModel().getColumn(4).setMinWidth(150);
            tblDispatch.getColumnModel().getColumn(5).setMinWidth(70);
            tblDispatch.getColumnModel().getColumn(6).setMinWidth(0);
            tblDispatch.getColumnModel().getColumn(6).setMaxWidth(0);
            tblDispatch.getColumnModel().getColumn(7).setMinWidth(130);
            tblDispatch.getColumnModel().getColumn(8).setMinWidth(100);
            tblDispatch.getColumnModel().getColumn(9).setMinWidth(100);
            tblDispatch.getColumnModel().getColumn(10).setMinWidth(0);
            tblDispatch.getColumnModel().getColumn(10).setMaxWidth(0);
            tblDispatch.getColumnModel().getColumn(11).setMinWidth(120);
            tblDispatch.getColumnModel().getColumn(12).setMinWidth(150);
            tblDispatch.getColumnModel().getColumn(13).setMinWidth(150);
            tblDispatch.getColumnModel().getColumn(14).setMinWidth(150);
            tblDispatch.getColumnModel().getColumn(15).setMinWidth(140);
            tblDispatch.getColumnModel().getColumn(16).setMinWidth(140);
            tblDispatch.getColumnModel().getColumn(17).setMinWidth(140);
            tblDispatch.getColumnModel().getColumn(18).setMinWidth(140);
            tblDispatch.getColumnModel().getColumn(19).setMinWidth(90);
            tblDispatch.getColumnModel().getColumn(20).setMinWidth(120);
            tblDispatch.getColumnModel().getColumn(23).setMinWidth(100);
            tblDispatch.getColumnModel().getColumn(24).setMinWidth(130);
            tblDispatch.getColumnModel().getColumn(25).setMinWidth(130);
            tblDispatch.getColumnModel().getColumn(26).setMinWidth(130);
            tblDispatch.getColumnModel().getColumn(27).setMinWidth(130);
            tblDispatch.getColumnModel().getColumn(28).setMinWidth(130);
            tblDispatch.getColumnModel().getColumn(29).setMinWidth(90);
            tblDispatch.getColumnModel().getColumn(30).setMinWidth(90);
            tblDispatch.getColumnModel().getColumn(31).setMinWidth(90);
            tblDispatch.getColumnModel().getColumn(32).setMinWidth(100);
            tblDispatch.getColumnModel().getColumn(33).setMinWidth(100);
            tblDispatch.getColumnModel().getColumn(34).setMinWidth(100);
            tblDispatch.getColumnModel().getColumn(35).setMinWidth(100);

            tblDispatch.getColumnModel().getColumn(DataModel_Dispatch.getColFromVariable("UPN_PIECES")).setCellRenderer(this.Renderer);
            tblDispatch.getColumnModel().getColumn(DataModel_Dispatch.getColFromVariable("PR_PARTY_CODE")).setCellRenderer(this.Renderer_invoiced);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDispatchPieces() {
        try {
            if (cmbMonthYear.getSelectedItem() == null) {
                return;
            }
            FormatGridDispatch();

            String str_query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3 FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR "
                    + " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS "
                    + "ON PS.POSITION_NO = PR.PR_POSITION_NO "
                    + "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM "
                    + "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                    + "WHERE PR.PR_PIECE_NO!='' ";

            //String str_query = "SELECT PR.*,POSITION_DESC,POSITION_DESIGN_NO  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR,PRODUCTION.FELT_MACHINE_POSITION_MST PS WHERE PS.POSITION_NO = PR.PR_POSITION_NO ";
//            if(!txtProductCode.getText().equals(""))
//            {
//                str_query = str_query + " AND PR.PR_PRODUCT_CODE="+txtProductCode.getText();
//            }
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }

            if (!cmbPieceStage.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_PIECE_STAGE = '" + cmbPieceStage.getSelectedItem() + "' ";
            }
            if (rdoRequestMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_REQUESTED_MONTH='" + cmbMonthYear.getSelectedItem() + "'"
                        + " AND PR_PIECE_STAGE IN ('INVOICED')";
            } else if (rdoOcMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_OC_MONTHYEAR='" + cmbMonthYear.getSelectedItem() + "'"
                        + " AND PR_PIECE_STAGE IN ('INVOICED')";
            } else if (rdoCurSchMonth.isSelected()) {
                str_query = str_query + " AND PR.PR_CURRENT_SCH_MONTH='" + cmbMonthYear.getSelectedItem() + "'"
                        + " AND PR_PIECE_STAGE IN ('INVOICED')";
            }

            if (!"".equals(txtUPN.getText())) {
                str_query = str_query + " AND PR.PR_UPN='" + txtUPN.getText() + "'";
            }

            String NewPieces = "";
            if (!txtPieceNo.getText().equals("")) {
                String PieceNo = txtPieceNo.getText();
                String Pieces[] = PieceNo.split(",");

                for (String Piece : Pieces) {
                    if (NewPieces.equals("")) {
                        NewPieces = "'" + Piece + "'";
                    } else {
                        NewPieces = NewPieces + ",'" + Piece + "'";
                    }
                }
            }

            if (!NewPieces.equals("")) {
                str_query = str_query + " AND PR.PR_PIECE_NO IN (" + NewPieces + ") ";
            }

            if (rdoExceptSDF.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP!='SDF'";
            } else if (rdoSDF.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP='SDF'";
            }

            if (!"".equals(txtPartyCode.getText())) {
                str_query = str_query + " AND PR.PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }

            str_query = str_query + ORDER_BY;

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //System.out.println("Query DATA : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            double TotalInvoiceAmt = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_Dispatch.addRow(rowData);

                DataModel_Dispatch.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_Dispatch.setValueByVariable("SELECT", Boolean.FALSE, NewRow);
                DataModel_Dispatch.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_Dispatch.setValueByVariable("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"), NewRow);
                DataModel_Dispatch.setValueByVariable("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"), NewRow);
                DataModel_Dispatch.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);
                DataModel_Dispatch.setValueByVariable("UPN_PIECES", resultSet.getString("PR_UPN"), NewRow);
                DataModel_Dispatch.setValueByVariable("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"), NewRow);

                DataModel_Dispatch.setValueByVariable("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                DataModel_Dispatch.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, resultSet.getString("PR_PARTY_CODE")), NewRow);

                DataModel_Dispatch.setValueByVariable("PR_GROUP", resultSet.getString("PR_GROUP"), NewRow);
                DataModel_Dispatch.setValueByVariable("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"), NewRow);

                DataModel_Dispatch.setValueByVariable("OC_MONTH", resultSet.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_Dispatch.setValueByVariable("CURRENT_SCHEDULE_MONTH", resultSet.getString("PR_CURRENT_SCH_MONTH"), NewRow);

                DataModel_Dispatch.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PAY_CHQRC_DATE")), NewRow);
                DataModel_Dispatch.setValueByVariable("ACT_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PAY_CHQRC_DATE")), NewRow);
                DataModel_Dispatch.setValueByVariable("EXP_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_DESPATCH_DATE")), NewRow);
                DataModel_Dispatch.setValueByVariable("ACT_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_INVOICE_DATE")), NewRow);

                DataModel_Dispatch.setValueByVariable("EXP_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_WIP_DELIVERY_DATE")), NewRow);
                DataModel_Dispatch.setValueByVariable("ACT_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_FNSG_DATE")), NewRow);

                DataModel_Dispatch.setValueByVariable("EXP_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PI_DATE")), NewRow);
                DataModel_Dispatch.setValueByVariable("ACT_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PI_DATE")), NewRow);

                DataModel_Dispatch.setValueByVariable("INVOICE_VALUE", resultSet.getString("PR_INVOICE_AMOUNT"), NewRow);

                DataModel_Dispatch.setValueByVariable("CONTACT_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_Dispatch.setValueByVariable("PHONE_NO", resultSet.getString("PHONE_NO"), NewRow);
                DataModel_Dispatch.setValueByVariable("EMAIL_ID", resultSet.getString("EMAIL"), NewRow);
                DataModel_Dispatch.setValueByVariable("EMAIL_ID2", resultSet.getString("EMAIL_ID2"), NewRow);
                DataModel_Dispatch.setValueByVariable("EMAIL_ID3", resultSet.getString("EMAIL_ID3"), NewRow);

//                DataModel_Dispatch.setValueByVariable("LENGTH", resultSet.getString("PR_BILL_LENGTH"), NewRow);
//                DataModel_Dispatch.setValueByVariable("WIDTH", resultSet.getString("PR_BILL_WIDTH"), NewRow);
//                DataModel_Dispatch.setValueByVariable("GSM", resultSet.getString("PR_BILL_GSM"), NewRow);
//                DataModel_Dispatch.setValueByVariable("STYLE", resultSet.getString("PR_BILL_STYLE"), NewRow); 
//                DataModel_Dispatch.setValueByVariable("WEIGHT", resultSet.getString("PR_BILL_WEIGHT"), NewRow); 
//                DataModel_Dispatch.setValueByVariable("SQMTR", resultSet.getString("PR_BILL_SQMTR"), NewRow); 
                DataModel_Dispatch.setValueByVariable("LENGTH", resultSet.getString("PR_LENGTH"), NewRow);
                DataModel_Dispatch.setValueByVariable("WIDTH", resultSet.getString("PR_WIDTH"), NewRow);
                DataModel_Dispatch.setValueByVariable("GSM", resultSet.getString("PR_GSM"), NewRow);
                DataModel_Dispatch.setValueByVariable("STYLE", resultSet.getString("PR_STYLE"), NewRow);
                DataModel_Dispatch.setValueByVariable("WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"), NewRow);
                DataModel_Dispatch.setValueByVariable("SQMTR", resultSet.getString("PR_SQMTR"), NewRow);

                try {
                    DataModel_Dispatch.setValueByVariable("ZONE", data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD=" + resultSet.getString("PR_INCHARGE") + ""), NewRow);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TotalInvoiceAmt = TotalInvoiceAmt + Double.parseDouble(resultSet.getString("PR_INVOICE_AMOUNT"));
                } catch (Exception e) {
                }

                Renderer.setBackColor(NewRow, DataModel_Dispatch.getColFromVariable("UPN_PIECES"), Color.LIGHT_GRAY);
                Renderer_invoiced.setBackColor(NewRow, DataModel_Dispatch.getColFromVariable("PR_PARTY_CODE"), Color.LIGHT_GRAY);
            }
            txtTotalDispatch.setText(BigDecimal.valueOf(EITLERPGLOBAL.round(TotalInvoiceAmt, 2)) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPlanning = new javax.swing.JTable();
        cmbMonthYear = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblWIP = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblStockBsr = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblDispatch = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbZone = new javax.swing.JComboBox();
        cmbProdGroup = new javax.swing.JComboBox();
        rdoRequestMonth = new javax.swing.JRadioButton();
        rdoOcMonth = new javax.swing.JRadioButton();
        rdoCurSchMonth = new javax.swing.JRadioButton();
        btnSave = new javax.swing.JButton();
        btnSorting = new javax.swing.JButton();
        btnShowData = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtUPN = new javax.swing.JTextField();
        txtUPN = new JTextFieldHint(new JTextField(),"Search by F1");
        jLabel8 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyCode = new JTextFieldHint(new JTextField(),"Search by F1");
        lblPartyName = new javax.swing.JLabel();
        btnExpToExcelDispatched = new javax.swing.JButton();
        btnExpToExcelPlanning = new javax.swing.JButton();
        btnExpToExcelWIP = new javax.swing.JButton();
        vbtnExpToExcelStock = new javax.swing.JButton();
        rdoExceptSDF = new javax.swing.JRadioButton();
        rdoSDF = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTotalDispatch = new javax.swing.JTextField();
        txtTotalWIP = new javax.swing.JTextField();
        txtTotalPlanning = new javax.swing.JTextField();
        txtTotalStockBSR = new javax.swing.JTextField();
        lblPieceNo = new javax.swing.JLabel();
        txtPieceNo = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        cmbPieceStage = new javax.swing.JComboBox();
        btnShowOrderStatus = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableCapacityPlanning = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtSelectedPieces = new javax.swing.JLabel();
        rdoReqMonthReport = new javax.swing.JRadioButton();
        rdoOcMonthReport = new javax.swing.JRadioButton();
        selectedMonth = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPlanning1 = new javax.swing.JTable();
        cmbMonthYear1 = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cmbZone1 = new javax.swing.JComboBox();
        cmbProdGroup1 = new javax.swing.JComboBox();
        btnSave1 = new javax.swing.JButton();
        btnShowData1 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        txtUPN1 = new javax.swing.JTextField();
        txtUPN = new JTextFieldHint(new JTextField(),"Search by F1");
        txtPartyCode1 = new javax.swing.JTextField();
        txtPartyCode = new JTextFieldHint(new JTextField(),"Search by F1");
        lblPartyName1 = new javax.swing.JLabel();
        btnExpToExcelPlanning1 = new javax.swing.JButton();
        rdoExceptSDF1 = new javax.swing.JRadioButton();
        rdoSDF1 = new javax.swing.JRadioButton();
        jLabel24 = new javax.swing.JLabel();
        txtTotalPlanning1 = new javax.swing.JTextField();
        lblPieceNo1 = new javax.swing.JLabel();
        txtPieceNo1 = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        btnShowOrderStatus1 = new javax.swing.JButton();
        lblPieceNo2 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblStatus1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        getContentPane().setLayout(null);

        Tab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabMouseClicked(evt);
            }
        });

        jPanel2.setLayout(null);

        tblPlanning.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPlanning.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPlanningMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPlanningMousePressed(evt);
            }
        });
        tblPlanning.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPlanningKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPlanningKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblPlanning);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(10, 130, 1160, 100);

        cmbMonthYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonthYearItemStateChanged(evt);
            }
        });
        jPanel2.add(cmbMonthYear);
        cmbMonthYear.setBounds(10, 30, 140, 20);

        tblWIP.setModel(new javax.swing.table.DefaultTableModel(
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
        tblWIP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblWIPMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblWIPMousePressed(evt);
            }
        });
        tblWIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblWIPKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblWIPKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(tblWIP);

        jPanel2.add(jScrollPane5);
        jScrollPane5.setBounds(10, 250, 1160, 80);

        tblStockBsr.setModel(new javax.swing.table.DefaultTableModel(
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
        tblStockBsr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStockBsrMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblStockBsrMousePressed(evt);
            }
        });
        tblStockBsr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblStockBsrKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(tblStockBsr);

        jPanel2.add(jScrollPane6);
        jScrollPane6.setBounds(10, 350, 1160, 90);

        tblDispatch.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDispatch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDispatchMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblDispatch);

        jPanel2.add(jScrollPane7);
        jScrollPane7.setBounds(10, 460, 1160, 90);

        jLabel1.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel1.setText("Dispatched Pieces");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(10, 440, 220, 16);

        jLabel2.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel2.setText("Booking / Planning Pieces");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 110, 240, 16);

        jLabel3.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel3.setText("WIP Pieces ");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(10, 230, 280, 16);

        jLabel4.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel4.setText("STOCK/BSR Pieces ");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(10, 330, 700, 16);

        jLabel6.setText("Prod Group");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(490, 60, 90, 20);

        jLabel7.setText("Zone");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(350, 60, 50, 20);

        cmbZone.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbZone.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbZoneItemStateChanged(evt);
            }
        });
        jPanel2.add(cmbZone);
        cmbZone.setBounds(390, 60, 90, 20);

        cmbProdGroup.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProdGroupItemStateChanged(evt);
            }
        });
        jPanel2.add(cmbProdGroup);
        cmbProdGroup.setBounds(570, 60, 90, 20);

        buttonGroup1.add(rdoRequestMonth);
        rdoRequestMonth.setSelected(true);
        rdoRequestMonth.setText("REQUEST MONTH");
        rdoRequestMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoRequestMonthMouseClicked(evt);
            }
        });
        rdoRequestMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoRequestMonthItemStateChanged(evt);
            }
        });
        rdoRequestMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRequestMonthActionPerformed(evt);
            }
        });
        jPanel2.add(rdoRequestMonth);
        rdoRequestMonth.setBounds(20, 6, 150, 20);

        buttonGroup1.add(rdoOcMonth);
        rdoOcMonth.setText("OC MONTH");
        rdoOcMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoOcMonthMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rdoOcMonthMouseReleased(evt);
            }
        });
        rdoOcMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoOcMonthItemStateChanged(evt);
            }
        });
        rdoOcMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOcMonthActionPerformed(evt);
            }
        });
        jPanel2.add(rdoOcMonth);
        rdoOcMonth.setBounds(170, 6, 110, 20);

        buttonGroup1.add(rdoCurSchMonth);
        rdoCurSchMonth.setText("CURR SALES PLAN MONTH");
        rdoCurSchMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoCurSchMonthMouseClicked(evt);
            }
        });
        rdoCurSchMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoCurSchMonthItemStateChanged(evt);
            }
        });
        jPanel2.add(rdoCurSchMonth);
        rdoCurSchMonth.setBounds(280, 6, 220, 20);

        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel2.add(btnSave);
        btnSave.setBounds(450, 440, 160, 20);

        btnSorting.setText("SORTING");
        btnSorting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortingActionPerformed(evt);
            }
        });
        jPanel2.add(btnSorting);
        btnSorting.setBounds(890, 10, 130, 25);

        btnShowData.setText("SHOW DATA");
        btnShowData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowDataActionPerformed(evt);
            }
        });
        jPanel2.add(btnShowData);
        btnShowData.setBounds(890, 40, 130, 25);

        jLabel5.setText("UPN");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(210, 60, 40, 20);
        jPanel2.add(txtUPN);
        txtUPN.setBounds(250, 60, 90, 20);

        jLabel8.setText("PIECE STAGE");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(670, 60, 100, 20);

        txtPartyCode.setToolTipText("Press F1 ");
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
        jPanel2.add(txtPartyCode);
        txtPartyCode.setBounds(110, 80, 90, 20);
        txtPartyCode.getAccessibleContext().setAccessibleParent(this);

        jPanel2.add(lblPartyName);
        lblPartyName.setBounds(200, 80, 410, 20);

        btnExpToExcelDispatched.setText("Export to Excel");
        btnExpToExcelDispatched.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpToExcelDispatchedActionPerformed(evt);
            }
        });
        jPanel2.add(btnExpToExcelDispatched);
        btnExpToExcelDispatched.setBounds(610, 550, 160, 20);

        btnExpToExcelPlanning.setText("Export to Excel");
        btnExpToExcelPlanning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpToExcelPlanningActionPerformed(evt);
            }
        });
        jPanel2.add(btnExpToExcelPlanning);
        btnExpToExcelPlanning.setBounds(610, 230, 160, 20);

        btnExpToExcelWIP.setText("Export to Excel");
        btnExpToExcelWIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpToExcelWIPActionPerformed(evt);
            }
        });
        jPanel2.add(btnExpToExcelWIP);
        btnExpToExcelWIP.setBounds(610, 330, 160, 20);

        vbtnExpToExcelStock.setText("Export to Excel");
        vbtnExpToExcelStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vbtnExpToExcelStockActionPerformed(evt);
            }
        });
        jPanel2.add(vbtnExpToExcelStock);
        vbtnExpToExcelStock.setBounds(610, 440, 160, 20);

        buttonGroup3.add(rdoExceptSDF);
        rdoExceptSDF.setSelected(true);
        rdoExceptSDF.setText("All(Except SDF)");
        rdoExceptSDF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        rdoExceptSDF.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoExceptSDFItemStateChanged(evt);
            }
        });
        jPanel2.add(rdoExceptSDF);
        rdoExceptSDF.setBounds(520, 10, 150, 20);

        buttonGroup3.add(rdoSDF);
        rdoSDF.setText("SDF");
        rdoSDF.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoSDFItemStateChanged(evt);
            }
        });
        jPanel2.add(rdoSDF);
        rdoSDF.setBounds(690, 10, 70, 20);

        jLabel10.setText("TOTAL VALUE");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(770, 550, 120, 15);

        jLabel11.setText("TOTAL VALUE");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(790, 230, 120, 15);

        jLabel12.setText("TOTAL VALUE");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(780, 330, 120, 15);

        jLabel13.setText("TOTAL VALUE");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(780, 440, 120, 15);
        jPanel2.add(txtTotalDispatch);
        txtTotalDispatch.setBounds(890, 550, 140, 20);
        jPanel2.add(txtTotalWIP);
        txtTotalWIP.setBounds(890, 330, 140, 20);
        jPanel2.add(txtTotalPlanning);
        txtTotalPlanning.setBounds(890, 230, 140, 20);
        jPanel2.add(txtTotalStockBSR);
        txtTotalStockBSR.setBounds(890, 440, 140, 20);

        lblPieceNo.setText("PIECE NO");
        jPanel2.add(lblPieceNo);
        lblPieceNo.setBounds(10, 60, 80, 20);
        jPanel2.add(txtPieceNo);
        txtPieceNo.setBounds(110, 60, 90, 20);

        jSeparator1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(jSeparator1);
        jSeparator1.setBounds(10, 0, 500, 30);

        jSeparator2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(jSeparator2);
        jSeparator2.setBounds(510, 0, 290, 40);

        jLabel14.setText("PARTY CODE");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(10, 80, 100, 20);

        cmbPieceStage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL", "BOOKING", "PLANNING", "SPLICING", "SEAMING", "WEAVING", "MENDING", "NEEDLING", "FINISHING", "GIDC", "IN STOCK", "INVOICED", " " }));
        cmbPieceStage.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPieceStageItemStateChanged(evt);
            }
        });
        jPanel2.add(cmbPieceStage);
        cmbPieceStage.setBounds(770, 60, 110, 20);

        btnShowOrderStatus.setText("Show Order Status");
        btnShowOrderStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowOrderStatusActionPerformed(evt);
            }
        });
        jPanel2.add(btnShowOrderStatus);
        btnShowOrderStatus.setBounds(970, 100, 200, 20);

        Tab.addTab("Order Planner", jPanel2);

        jPanel1.setLayout(null);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setEnabled(false);

        TableCapacityPlanning.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
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
        jScrollPane2.setViewportView(TableCapacityPlanning);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(10, 90, 1320, 390);

        jLabel9.setText("SELECTED PIECES :");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 40, 140, 15);

        txtSelectedPieces.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(txtSelectedPieces);
        txtSelectedPieces.setBounds(10, 60, 1250, 30);

        buttonGroup4.add(rdoReqMonthReport);
        rdoReqMonthReport.setText("REQUEST MONTH");
        rdoReqMonthReport.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoReqMonthReportItemStateChanged(evt);
            }
        });
        jPanel1.add(rdoReqMonthReport);
        rdoReqMonthReport.setBounds(10, 10, 170, 23);

        buttonGroup4.add(rdoOcMonthReport);
        rdoOcMonthReport.setSelected(true);
        rdoOcMonthReport.setText("OC MONTH");
        jPanel1.add(rdoOcMonthReport);
        rdoOcMonthReport.setBounds(200, 10, 130, 23);

        selectedMonth.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(selectedMonth);
        selectedMonth.setBounds(360, 10, 120, 20);

        Tab.addTab("Prod Mfg Report", jPanel1);

        jPanel3.setLayout(null);

        tblPlanning1.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPlanning1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPlanning1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPlanning1MousePressed(evt);
            }
        });
        tblPlanning1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPlanning1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPlanning1KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblPlanning1);

        jPanel3.add(jScrollPane3);
        jScrollPane3.setBounds(10, 130, 1160, 410);

        cmbMonthYear1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonthYear1ItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbMonthYear1);
        cmbMonthYear1.setBounds(10, 30, 140, 20);

        jLabel16.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel16.setText("Booking Pieces");
        jPanel3.add(jLabel16);
        jLabel16.setBounds(10, 110, 240, 16);

        jLabel19.setText("Prod Group");
        jPanel3.add(jLabel19);
        jLabel19.setBounds(490, 60, 90, 20);

        jLabel20.setText("Zone");
        jPanel3.add(jLabel20);
        jLabel20.setBounds(350, 60, 50, 20);

        cmbZone1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbZone1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbZone1ItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbZone1);
        cmbZone1.setBounds(390, 60, 90, 20);

        cmbProdGroup1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProdGroup1ItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbProdGroup1);
        cmbProdGroup1.setBounds(570, 60, 90, 20);

        btnSave1.setText("SAVE");
        btnSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnSave1);
        btnSave1.setBounds(450, 550, 160, 20);

        btnShowData1.setText("SHOW DATA");
        btnShowData1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowData1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnShowData1);
        btnShowData1.setBounds(890, 40, 130, 25);

        jLabel21.setText("UPN");
        jPanel3.add(jLabel21);
        jLabel21.setBounds(210, 60, 40, 20);
        jPanel3.add(txtUPN1);
        txtUPN1.setBounds(250, 60, 90, 20);

        txtPartyCode1.setToolTipText("Press F1 ");
        txtPartyCode1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCode1FocusLost(evt);
            }
        });
        txtPartyCode1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCode1KeyPressed(evt);
            }
        });
        jPanel3.add(txtPartyCode1);
        txtPartyCode1.setBounds(110, 80, 90, 20);
        jPanel3.add(lblPartyName1);
        lblPartyName1.setBounds(200, 80, 410, 20);

        btnExpToExcelPlanning1.setText("Export to Excel");
        btnExpToExcelPlanning1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpToExcelPlanning1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnExpToExcelPlanning1);
        btnExpToExcelPlanning1.setBounds(630, 550, 160, 20);

        buttonGroup5.add(rdoExceptSDF1);
        rdoExceptSDF1.setSelected(true);
        rdoExceptSDF1.setText("All(Except SDF)");
        rdoExceptSDF1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        rdoExceptSDF1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoExceptSDF1ItemStateChanged(evt);
            }
        });
        jPanel3.add(rdoExceptSDF1);
        rdoExceptSDF1.setBounds(520, 10, 150, 20);

        buttonGroup5.add(rdoSDF1);
        rdoSDF1.setText("SDF");
        rdoSDF1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoSDF1ItemStateChanged(evt);
            }
        });
        jPanel3.add(rdoSDF1);
        rdoSDF1.setBounds(690, 10, 70, 20);

        jLabel24.setText("TOTAL VALUE");
        jPanel3.add(jLabel24);
        jLabel24.setBounds(810, 550, 120, 15);
        jPanel3.add(txtTotalPlanning1);
        txtTotalPlanning1.setBounds(910, 550, 140, 20);

        lblPieceNo1.setText("PPRS Month ");
        jPanel3.add(lblPieceNo1);
        lblPieceNo1.setBounds(10, 10, 150, 20);
        jPanel3.add(txtPieceNo1);
        txtPieceNo1.setBounds(110, 60, 90, 20);

        jSeparator4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.add(jSeparator4);
        jSeparator4.setBounds(510, 0, 290, 40);

        jLabel27.setText("PARTY CODE");
        jPanel3.add(jLabel27);
        jLabel27.setBounds(10, 80, 100, 20);

        btnShowOrderStatus1.setText("Show Order Status");
        btnShowOrderStatus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowOrderStatus1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnShowOrderStatus1);
        btnShowOrderStatus1.setBounds(970, 100, 200, 20);

        lblPieceNo2.setText("PIECE NO");
        jPanel3.add(lblPieceNo2);
        lblPieceNo2.setBounds(10, 60, 80, 20);

        Tab.addTab("PPRS Planner", jPanel3);

        getContentPane().add(Tab);
        Tab.setBounds(0, 30, 1350, 630);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Felt Sales Order Planner");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 0, 1350, 25);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 580, 1060, 22);

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus1);
        lblStatus1.setBounds(0, 630, 1350, 30);
        getContentPane().add(jPanel5);
        jPanel5.setBounds(210, 50, 10, 10);
    }// </editor-fold>//GEN-END:initComponents

    private void TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabMouseClicked
        selectedMonth.setText(cmbMonthYear.getSelectedItem().toString());
        Renderer_report.removeBackColors();
        if (rdoRequestMonth.isSelected()) {
            rdoReqMonthReport.setSelected(true);
        } else {
            rdoOcMonthReport.setSelected(true);
        }

        if (rdoReqMonthReport.isSelected()) {
            if (rdoExceptSDF.isSelected()) {
                GenerateProdMfgReportNonSDF_ReqMonth();
            } else {
                GenerateProdMfgReportSDF_ReqMonth();
            }
        } else {
            if (rdoExceptSDF.isSelected()) {
                GenerateProdMfgReportNonSDF();
            } else {
                GenerateProdMfgReportSDF();
            }
        }

    }//GEN-LAST:event_TabMouseClicked

    private void rdoReqMonthReportItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoReqMonthReportItemStateChanged
        // TODO add your handling code here:

        Renderer_report.removeBackColors();
        if (rdoReqMonthReport.isSelected()) {
            if (rdoExceptSDF.isSelected()) {
                GenerateProdMfgReportNonSDF_ReqMonth();
            } else {
                GenerateProdMfgReportSDF_ReqMonth();
            }
        } else {
            if (rdoExceptSDF.isSelected()) {
                GenerateProdMfgReportNonSDF();
            } else {
                GenerateProdMfgReportSDF();
            }
        }

    }//GEN-LAST:event_rdoReqMonthReportItemStateChanged

    private void TableCapacityPlanningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableCapacityPlanningKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TableCapacityPlanningKeyPressed

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

    private void btnShowOrderStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowOrderStatusActionPerformed
        // TODO add your handling code here:
        //        if (lblSelectedParty.getText().equals("")) {
        //            JOptionPane.showMessageDialog(this, "Please select any Piece");
        //        } else {
        //            String PartyCode = lblSelectedParty.getText();

        AppletFrame aFrame = new AppletFrame("Party Order Status");
        aFrame.startAppletEx("EITLERP.FeltSales.PieceRegister.FrmPOPendingOrder", "Party Order Status");
        FrmPOPendingOrder ObjItem = (FrmPOPendingOrder) aFrame.ObjApplet;

        ObjItem.requestFocus();
        ObjItem.setData(txtPartyCode.getText());
        //}
    }//GEN-LAST:event_btnShowOrderStatusActionPerformed

    private void cmbPieceStageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPieceStageItemStateChanged
        // TODO add your handling code here:
        setPlanningPieces();
        setWIPPieces();
        setSTOCKPieces();
        setDispatchPieces();
    }//GEN-LAST:event_cmbPieceStageItemStateChanged

    private void rdoSDFItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoSDFItemStateChanged
        // TODO add your handling code here:
        GenerateComboMonthYear();
    }//GEN-LAST:event_rdoSDFItemStateChanged

    private void rdoExceptSDFItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoExceptSDFItemStateChanged
        // TODO add your handling code here:
        GenerateComboMonthYear();
    }//GEN-LAST:event_rdoExceptSDFItemStateChanged

    private void vbtnExpToExcelStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vbtnExpToExcelStockActionPerformed
        // TODO add your handling code here:
        try {
            //exp.fillData(tblStockBsr, new File("/root/Desktop/STOCKBSR_DATA.xls"));
            exp.fillData(tblStockBsr, new File(System.getProperty("user.home") + "/Desktop/STOCKBSR_DATA.xls"));
            exp.fillData(tblStockBsr, new File("D://STOCKBSR_DATA.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/STOCKBSR_DATA.xls' successfully in Linux PC or 'D://STOCKBSR_DATA.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_vbtnExpToExcelStockActionPerformed

    private void btnExpToExcelWIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpToExcelWIPActionPerformed
        // TODO add your handling code here:
        try {
            //exp.fillData(tblWIP, new File("/root/Desktop/WIP_DATA.xls"));
            exp.fillData(tblWIP, new File(System.getProperty("user.home") + "/Desktop/WIP_DATA.xls"));
            exp.fillData(tblWIP, new File("D://WIP_DATA.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/WIP_DATA.xls' successfully in Linux PC or 'D://WIP_DATA.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnExpToExcelWIPActionPerformed

    private void btnExpToExcelPlanningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpToExcelPlanningActionPerformed
        // TODO add your handling code here:
        try {

            //exp.fillData(tblPlanning, new File("/root/Desktop/PLANNINGBOOKING_DATA.xls"));
            exp.fillData(tblPlanning, new File("D://PLANNINGBOOKING_DATA.xls"));

            exp.fillData(tblPlanning, new File(System.getProperty("user.home") + "/Desktop/PLANNINGBOOKING_DATA.xls"));

            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/PLANNINGBOOKING_DATA.xls' successfully in Linux PC or 'D://PLANNINGBOOKING_DATA.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnExpToExcelPlanningActionPerformed

    private void btnExpToExcelDispatchedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpToExcelDispatchedActionPerformed
        // TODO add your handling code here:
        try {
            //exp.fillData(tblDispatch, new File("/root/Desktop/DISPATCHED_DATA.xls"));
            exp.fillData(tblDispatch, new File(System.getProperty("user.home") + "/Desktop/DISPATCHED_DATA.xls"));
            exp.fillData(tblDispatch, new File("D://DISPATCHED_DATA.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/DISPATCHED_DATA.xls' successfully in Linux PC or 'D://DISPATCHED_DATA.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnExpToExcelDispatchedActionPerformed

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010'";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);
                lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        if (txtPartyCode.getText().equals("")) {
            lblPartyName.setText("");
        }
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void btnShowDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowDataActionPerformed
        // TODO add your handling code here:
        setPlanningPieces();
        setWIPPieces();
        setSTOCKPieces();
        setDispatchPieces();
    }//GEN-LAST:event_btnShowDataActionPerformed

    private void btnSortingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortingActionPerformed
        // TODO add your handling code here:
        sort_query_creator();
        btnShowDataActionPerformed(null);
    }//GEN-LAST:event_btnSortingActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        // TODO add your handling code here:
        int USER_ID = EITLERPGLOBAL.gUserID;
        int DEPT_ID = EITLERPGLOBAL.gUserDeptID;
        //System.out.println("USER = "+USER_ID+" DEPT ID = "+DEPT_ID);
        if (USER_ID == 352 || USER_ID == 136 || USER_ID == 329 || USER_ID == 331 || USER_ID == 28 || USER_ID == 318 || USER_ID == 394) {
            //SALES LOGIN

            for (int i = 0; i < tblPlanning.getRowCount(); i++) {
                //if(tblPlanning.getValueAt(i, 1).equals(true))
                //{
                String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
                String RESCHEDULE_MONTH = DataModel_Planning.getValueByVariable("RESCHEDULE_MONTH", i);
                String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);
                String OC_MONTH_exist = DataModel_Planning.getValueByVariable("OC_MONTH", i);

                if (!"".equals(RESCHEDULE_MONTH) && "".equals(OC_MONTH_exist)) {
                    clsPlannerActivities planner = new clsPlannerActivities();
                    planner.setPLANNER_TYPE("RESCHEDULE_MONTH");
                    planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                    planner.setPIECE_NO(DataModel_Planning.getValueByVariable("PR_PIECE_NO", i));
                    planner.setPARTY_CODE(DataModel_Planning.getValueByVariable("PR_PARTY_CODE", i));//
                    planner.setUPN(DataModel_Planning.getValueByVariable("UPN_PIECES", i));//
                    planner.setREQ_MONTH(DataModel_Planning.getValueByVariable("PR_REQUESTED_MONTH", i));//
                    planner.setRESCHEDULE_REQ_MONTH(DataModel_Planning.getValueByVariable("RESCHEDULE_MONTH", i));//
                    planner.setOC_MONTH(DataModel_Planning.getValueByVariable("OC_MONTH", i));//
                    planner.setUPDATED_OC_MONTH("");
                    planner.setCURRENT_SCH_MONTH(DataModel_Planning.getValueByVariable("CURRENT_SCHEDULE_MONTH", i));//
                    planner.setUPDATED_CURRENT_SCH_MONTH("");
                    planner.setSPECIAL_REQ_MONTH(DataModel_Planning.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i));//
                    planner.setUPDATED_SPECIAL_REQ_MONTH("");
                    planner.setSPECIAL_REQ_MONTH_DATE("");
                    planner.setUPDATED_SPECIAL_REQ_MONTH("");
                    planner.setEXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_Planning.getValueByVariable("EXP_WIP_DELIVERY_DATE", i)));//
                    planner.setUPDATED_EXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_Planning.getValueByVariable("ACT_WIP_DELIVERY_DATE", i)));//
                    planner.setEXP_PI_DATE(EITLERPGLOBAL.formatDateDB(DataModel_Planning.getValueByVariable("EXP_PI_DATE", i)));//
                    planner.setUPDATED_EXP_PI_DATE("");
                    planner.setUSER_ID(EITLERPGLOBAL.gUserID + "");
                    planner.setUPDATE_STATUS("PENDING");
                    planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                    planner.setEXP_PAY_CHQ_RCV_DATE("0000-00-00");
                    planner.saveTransaction();
                } else if (!"".equals(RESCHEDULE_MONTH)) {
                    JOptionPane.showMessageDialog(null, "Reschedule not possible for PIECE if OC MONTH exist for PIECE : " + PIECE_NO);
                }

                String PR_SPL_REQUEST_MONTHYEAR = DataModel_Planning.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i);
                String PR_SPL_REQUEST_DATE = DataModel_Planning.getValueByVariable("PR_SPL_REQUEST_DATE", i);

                if (!"".equals(PR_SPL_REQUEST_DATE)) {
                    try {
                            //if(!"".equals(PR_SPL_REQUEST_DATE) && "".equals(PR_SPL_REQUEST_MONTHYEAR))
                        //{
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(PR_SPL_REQUEST_DATE);
                        Date today = new Date();

                        if (date.before(today)) {
                            JOptionPane.showMessageDialog(null, PR_SPL_REQUEST_DATE + " is not valid!");

                        } else {
                            clsPlannerActivities planner = new clsPlannerActivities();
                            planner.setPLANNER_TYPE("SPL_REQUEST_MONTHYEAR");
                            planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                            planner.setPIECE_NO(DataModel_Planning.getValueByVariable("PR_PIECE_NO", i));
                            planner.setPARTY_CODE(DataModel_Planning.getValueByVariable("PR_PARTY_CODE", i));//
                            planner.setUPN(DataModel_Planning.getValueByVariable("UPN_PIECES", i));//
                            planner.setREQ_MONTH(DataModel_Planning.getValueByVariable("PR_REQUESTED_MONTH", i));//
                            planner.setRESCHEDULE_REQ_MONTH(DataModel_Planning.getValueByVariable("RESCHEDULE_MONTH", i));//
                            planner.setOC_MONTH(DataModel_Planning.getValueByVariable("OC_MONTH", i));//
                            planner.setUPDATED_OC_MONTH("");
                            planner.setCURRENT_SCH_MONTH(DataModel_Planning.getValueByVariable("CURRENT_SCHEDULE_MONTH", i));//
                            planner.setUPDATED_CURRENT_SCH_MONTH("");
                            planner.setSPECIAL_REQ_MONTH(DataModel_Planning.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i));//
                            planner.setUPDATED_SPECIAL_REQ_MONTH("");
                            planner.setSPECIAL_REQ_MONTH_DATE(EITLERPGLOBAL.formatDateDB(DataModel_Planning.getValueByVariable("PR_SPL_REQUEST_DATE", i)));
                            planner.setUPDATED_SPECIAL_REQ_MONTH("");
                            planner.setEXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_Planning.getValueByVariable("EXP_WIP_DELIVERY_DATE", i)));//
                            planner.setUPDATED_EXP_WIP_DELIVERY_DATE("");//
                            planner.setEXP_PI_DATE(EITLERPGLOBAL.formatDateDB(DataModel_Planning.getValueByVariable("EXP_PI_DATE", i)));//
                            planner.setUPDATED_EXP_PI_DATE("");
                            planner.setUSER_ID(EITLERPGLOBAL.gUserID + "");
                            planner.setUPDATE_STATUS("PENDING");
                            planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                            planner.setEXP_PAY_CHQ_RCV_DATE("0000-00-00");
                            planner.saveTransaction();
                        }

                                //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //}
            }

            for (int i = 0; i < tblWIP.getRowCount(); i++) {
                //if(tblWIP.getValueAt(i, 1).equals(true))
                //{
                String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
                String RESCHEDULE_MONTH = DataModel_WIP.getValueByVariable("RESCHEDULE_MONTH", i);
                String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
                String OC_MONTH_exist = DataModel_WIP.getValueByVariable("OC_MONTH", i);
                //EXP_PAY_CHQ_RCV_DATE
                String EXP_PAY_CHQ_RCV_DATE = DataModel_WIP.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", i);
                    //                if(!"".equals(OC_MONTH) && !"".equals(RESCHEDULE_MONTH)){
                //                    JOptionPane.showMessageDialog(null, "Reschedule Not possible for Piece "+PIECE_NO+", if OC MONTH exist");
                //                    return;
                //                }
                String PR_SPL_REQUEST_MONTHYEAR = DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i);
                String PR_SPL_REQUEST_DATE = DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_DATE", i);

                String EXP_WIP_DELIVERY_DATE = DataModel_WIP.getValueByVariable("EXP_WIP_DELIVERY_DATE", i);

                try {
                    String PR_EXP_PAY_CHQRC_REMARKS = DataModel_WIP.getValueByVariable("PR_EXP_PAY_CHQRC_REMARKS", i);
                    if (!PR_EXP_PAY_CHQRC_REMARKS.equals("")) {
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_PAY_CHQRC_REMARKS='" + PR_EXP_PAY_CHQRC_REMARKS + "',PR_EXP_PAY_CHQRC_REMARKS_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (EXP_WIP_DELIVERY_DATE.equals("")) {
                    EXP_PAY_CHQ_RCV_DATE = "";
                }

                if (!"".equals(PR_SPL_REQUEST_DATE)) {
                    try {
                            //if(!"".equals(PR_SPL_REQUEST_DATE) && "".equals(PR_SPL_REQUEST_MONTHYEAR))
                        //{
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(PR_SPL_REQUEST_DATE);
                        Date today = new Date();

                        if (date.before(today)) {
                            JOptionPane.showMessageDialog(null, PR_SPL_REQUEST_DATE + " is not valid!");
                        } else {
                            clsPlannerActivities planner = new clsPlannerActivities();
                            planner.setPLANNER_TYPE("SPL_REQUEST_MONTHYEAR");
                            planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                            planner.setPIECE_NO(DataModel_WIP.getValueByVariable("PR_PIECE_NO", i));
                            planner.setPARTY_CODE(DataModel_WIP.getValueByVariable("PR_PARTY_CODE", i));//
                            planner.setUPN(DataModel_WIP.getValueByVariable("UPN_PIECES", i));//
                            planner.setREQ_MONTH(DataModel_WIP.getValueByVariable("PR_REQUESTED_MONTH", i));//
                            planner.setRESCHEDULE_REQ_MONTH(DataModel_WIP.getValueByVariable("RESCHEDULE_MONTH", i));//
                            planner.setOC_MONTH(DataModel_WIP.getValueByVariable("OC_MONTH", i));//
                            planner.setUPDATED_OC_MONTH("");
                            planner.setCURRENT_SCH_MONTH(DataModel_WIP.getValueByVariable("CURRENT_SCHEDULE_MONTH", i));//
                            planner.setUPDATED_CURRENT_SCH_MONTH("");
                            planner.setSPECIAL_REQ_MONTH(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i));//
                            planner.setUPDATED_SPECIAL_REQ_MONTH("");
                            planner.setSPECIAL_REQ_MONTH_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_DATE", i)));
                            planner.setUPDATED_SPECIAL_REQ_MONTH("");
                            planner.setEXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_WIP_DELIVERY_DATE", i)));//
                            planner.setUPDATED_EXP_WIP_DELIVERY_DATE("");//
                            planner.setEXP_PI_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PI_DATE", i)));//
                            planner.setUPDATED_EXP_PI_DATE("");
                            planner.setUSER_ID(EITLERPGLOBAL.gUserID + "");
                            planner.setUPDATE_STATUS("PENDING");
                            planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                            planner.setEXP_PAY_CHQ_RCV_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", i)));
                            planner.saveTransaction();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!"".equals(RESCHEDULE_MONTH) && "".equals(OC_MONTH_exist)) {
                    clsPlannerActivities planner = new clsPlannerActivities();
                    planner.setPLANNER_TYPE("RESCHEDULE_MONTH");
                    planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                    planner.setPIECE_NO(DataModel_WIP.getValueByVariable("PR_PIECE_NO", i));
                    planner.setPARTY_CODE(DataModel_WIP.getValueByVariable("PR_PARTY_CODE", i));//
                    planner.setUPN(DataModel_WIP.getValueByVariable("UPN_PIECES", i));//
                    planner.setREQ_MONTH(DataModel_WIP.getValueByVariable("PR_REQUESTED_MONTH", i));//
                    planner.setRESCHEDULE_REQ_MONTH(DataModel_WIP.getValueByVariable("RESCHEDULE_MONTH", i));//
                    planner.setOC_MONTH(DataModel_WIP.getValueByVariable("OC_MONTH", i));//
                    planner.setUPDATED_OC_MONTH("");
                    planner.setCURRENT_SCH_MONTH(DataModel_WIP.getValueByVariable("CURRENT_SCHEDULE_MONTH", i));//
                    planner.setUPDATED_CURRENT_SCH_MONTH("");
                    planner.setSPECIAL_REQ_MONTH(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i));//
                    planner.setUPDATED_SPECIAL_REQ_MONTH("");
                    planner.setSPECIAL_REQ_MONTH_DATE("");
                    planner.setUPDATED_SPECIAL_REQ_MONTH("");
                    planner.setEXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_WIP_DELIVERY_DATE", i)));//
                    planner.setUPDATED_EXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("ACT_WIP_DELIVERY_DATE", i)));//
                    planner.setEXP_PI_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PI_DATE", i)));//
                    planner.setUPDATED_EXP_PI_DATE("");
                    planner.setUSER_ID(EITLERPGLOBAL.gUserID + "");
                    planner.setUPDATE_STATUS("PENDING");
                    planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                    planner.setEXP_PAY_CHQ_RCV_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", i)));
                    planner.saveTransaction();
                } else if (!"".equals(RESCHEDULE_MONTH)) {
                    JOptionPane.showMessageDialog(null, "Reschedule not possible for PIECE if OC MONTH exist");
                }

                //
                if (!"".equals(EXP_PAY_CHQ_RCV_DATE)) {
                    try {
                                //if(!"".equals(PR_SPL_REQUEST_DATE) && "".equals(PR_SPL_REQUEST_MONTHYEAR))
                        //{
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(EXP_PAY_CHQ_RCV_DATE);
                        Date today = new Date();
                        boolean validation_flag = true;

                        try {
                            String EXP_PAY_CHQ_RCV_DATE1 = EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", i));
                            Date exp_pay_date = new SimpleDateFormat("yyyy-MM-dd").parse(EXP_PAY_CHQ_RCV_DATE1);

                            if (exp_pay_date.before(today)) {
                                String already_exist = data.getStringValueFromDB("SELECT PR_EXP_PAY_CHQRC_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                if (already_exist.equals("0000-00-00")) {
                                    JOptionPane.showMessageDialog(this, "Previous date not allowed.");
                                }
                                DataModel_WIP.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", i);
                                validation_flag = false;
                            }
                            if (exp_pay_date.getDay() != 6) {
                                JOptionPane.showMessageDialog(this, "Please enter SATURDAY date only.");
                                DataModel_WIP.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", i);
                                validation_flag = false;
                            }

                        } catch (Exception e) {
                            // e.printStackTrace();
                        }

                        if (date.before(today)) {
                            String already_exist = data.getStringValueFromDB("SELECT PR_EXP_PAY_CHQRC_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                            if (already_exist.equals("0000-00-00")) {
                                JOptionPane.showMessageDialog(null, EXP_PAY_CHQ_RCV_DATE + " is not valid!");
                            }
                        } else if (!validation_flag) {
                            //NO VALIDE ENTRY
                        } else {
                            clsPlannerActivities planner = new clsPlannerActivities();
                            planner.setPLANNER_TYPE("EXP_PAY_CHQ_RCV_DATE");
                            planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                            planner.setPIECE_NO(DataModel_WIP.getValueByVariable("PR_PIECE_NO", i));
                            planner.setPARTY_CODE(DataModel_WIP.getValueByVariable("PR_PARTY_CODE", i));//
                            planner.setUPN(DataModel_WIP.getValueByVariable("UPN_PIECES", i));//
                            planner.setREQ_MONTH(DataModel_WIP.getValueByVariable("PR_REQUESTED_MONTH", i));//
                            planner.setRESCHEDULE_REQ_MONTH(DataModel_WIP.getValueByVariable("RESCHEDULE_MONTH", i));//
                            planner.setOC_MONTH(DataModel_WIP.getValueByVariable("OC_MONTH", i));//
                            planner.setUPDATED_OC_MONTH("");
                            planner.setCURRENT_SCH_MONTH(DataModel_WIP.getValueByVariable("CURRENT_SCHEDULE_MONTH", i));//
                            planner.setUPDATED_CURRENT_SCH_MONTH("");
                            planner.setSPECIAL_REQ_MONTH(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i));//
                            planner.setUPDATED_SPECIAL_REQ_MONTH("");
                            planner.setSPECIAL_REQ_MONTH_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_DATE", i)));
                            planner.setUPDATED_SPECIAL_REQ_MONTH("");
                            planner.setEXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_WIP_DELIVERY_DATE", i)));//
                            planner.setUPDATED_EXP_WIP_DELIVERY_DATE("");//
                            planner.setEXP_PI_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PI_DATE", i)));//
                            planner.setUPDATED_EXP_PI_DATE("");
                            planner.setUSER_ID(EITLERPGLOBAL.gUserID + "");
                            planner.setUPDATE_STATUS("PENDING");
                            planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                            planner.setEXP_PAY_CHQ_RCV_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", i)));
                            planner.saveTransaction();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                            //

                //}
            }

            for (int i = 0; i < tblStockBsr.getRowCount(); i++) {

                String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
                String RESCHEDULE_MONTH = DataModel_STOCK.getValueByVariable("RESCHEDULE_MONTH", i);
                String PIECE_NO = DataModel_STOCK.getValueByVariable("PR_PIECE_NO", i);
                String OC_MONTH_exist = DataModel_STOCK.getValueByVariable("OC_MONTH", i);

                String EXP_PAY_CHQ_RCV_DATE = DataModel_STOCK.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", i);

                try {
                    String PR_EXP_PAY_CHQRC_REMARKS = DataModel_STOCK.getValueByVariable("PR_EXP_PAY_CHQRC_REMARKS", i);
                    if (!PR_EXP_PAY_CHQRC_REMARKS.equals("")) {
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_PAY_CHQRC_REMARKS='" + PR_EXP_PAY_CHQRC_REMARKS + "',PR_EXP_PAY_CHQRC_REMARKS_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String EXP_WIP_DELIVERY_DATE = DataModel_STOCK.getValueByVariable("EXP_WIP_DELIVERY_DATE", i);
                String ACT_WIP_DELIVERY_DATE = DataModel_STOCK.getValueByVariable("ACT_WIP_DELIVERY_DATE", i);
                if (EXP_WIP_DELIVERY_DATE.equals("") && ACT_WIP_DELIVERY_DATE.equals("")) {
                    EXP_PAY_CHQ_RCV_DATE = "";
                }

                if (!"".equals(EXP_PAY_CHQ_RCV_DATE)) {
                    try {
                                //if(!"".equals(PR_SPL_REQUEST_DATE) && "".equals(PR_SPL_REQUEST_MONTHYEAR))
                        //{
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(EXP_PAY_CHQ_RCV_DATE);
                        Date today = new Date();

                        boolean validation_flag = true;

                        try {
                            String EXP_PAY_CHQ_RCV_DATE1 = EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", i));
                            Date exp_pay_date = new SimpleDateFormat("yyyy-MM-dd").parse(EXP_PAY_CHQ_RCV_DATE1);

                            if (exp_pay_date.before(today)) {
                                String already_exist = data.getStringValueFromDB("SELECT PR_EXP_PAY_CHQRC_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                if (already_exist.equals("0000-00-00")) {
                                    JOptionPane.showMessageDialog(this, "Previous date not allowed in stock.");
                                }
                                DataModel_STOCK.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", i);
                                validation_flag = false;
                            }
                            if (exp_pay_date.getDay() != 6) {
                                JOptionPane.showMessageDialog(this, "Please enter SATURDAY date only in stock.");
                                DataModel_STOCK.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", i);
                                validation_flag = false;
                            }

                        } catch (Exception e) {
                            // e.printStackTrace();
                        }

                        if (date.before(today)) {
                            String already_exist = data.getStringValueFromDB("SELECT PR_EXP_PAY_CHQRC_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                            if (already_exist.equals("0000-00-00")) {
                                JOptionPane.showMessageDialog(null, EXP_PAY_CHQ_RCV_DATE + " is not valid!");
                            }
                        } else if (!validation_flag) {
                            //NOT VALIVATE
                        } else {
                            clsPlannerActivities planner = new clsPlannerActivities();
                            planner.setPLANNER_TYPE("EXP_PAY_CHQ_RCV_DATE");
                            planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                            planner.setPIECE_NO(DataModel_STOCK.getValueByVariable("PR_PIECE_NO", i));
                            planner.setPARTY_CODE(DataModel_STOCK.getValueByVariable("PR_PARTY_CODE", i));//
                            planner.setUPN(DataModel_STOCK.getValueByVariable("UPN_PIECES", i));//
                            planner.setREQ_MONTH(DataModel_STOCK.getValueByVariable("PR_REQUESTED_MONTH", i));//
                            planner.setRESCHEDULE_REQ_MONTH(DataModel_STOCK.getValueByVariable("RESCHEDULE_MONTH", i));//
                            planner.setOC_MONTH(DataModel_STOCK.getValueByVariable("OC_MONTH", i));//
                            planner.setUPDATED_OC_MONTH("");
                            planner.setCURRENT_SCH_MONTH(DataModel_STOCK.getValueByVariable("CURRENT_SCHEDULE_MONTH", i));//
                            planner.setUPDATED_CURRENT_SCH_MONTH("");
                            planner.setSPECIAL_REQ_MONTH(DataModel_STOCK.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i));//
                            planner.setUPDATED_SPECIAL_REQ_MONTH("");
                            planner.setSPECIAL_REQ_MONTH_DATE(EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("PR_SPL_REQUEST_DATE", i)));
                            planner.setUPDATED_SPECIAL_REQ_MONTH("");
                            planner.setEXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("EXP_WIP_DELIVERY_DATE", i)));//
                            planner.setUPDATED_EXP_WIP_DELIVERY_DATE("");//
                            planner.setEXP_PI_DATE(EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("EXP_PI_DATE", i)));//
                            planner.setUPDATED_EXP_PI_DATE("");
                            planner.setUSER_ID(EITLERPGLOBAL.gUserID + "");
                            planner.setUPDATE_STATUS("PENDING");
                            planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                            planner.setEXP_PAY_CHQ_RCV_DATE(EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", i)));
                            planner.saveTransaction();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /*
                 if(!"".equals(RESCHEDULE_MONTH) && "".equals(OC_MONTH_exist))
                 {
                 clsPlannerActivities planner = new clsPlannerActivities();
                 planner.setPLANNER_TYPE("RESCHEDULE_MONTH");
                 planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                 planner.setPIECE_NO(DataModel_STOCK.getValueByVariable("PR_PIECE_NO", i));
                 planner.setPARTY_CODE(DataModel_STOCK.getValueByVariable("PR_PARTY_CODE", i));//
                 planner.setUPN(DataModel_STOCK.getValueByVariable("UPN_PIECES", i));//
                 planner.setREQ_MONTH(DataModel_STOCK.getValueByVariable("PR_REQUESTED_MONTH", i));//
                 planner.setRESCHEDULE_REQ_MONTH(DataModel_STOCK.getValueByVariable("RESCHEDULE_MONTH", i));//
                 planner.setOC_MONTH(DataModel_STOCK.getValueByVariable("OC_MONTH", i));//
                 planner.setUPDATED_OC_MONTH("");
                 planner.setCURRENT_SCH_MONTH(DataModel_STOCK.getValueByVariable("CURRENT_SCHEDULE_MONTH", i));//
                 planner.setUPDATED_CURRENT_SCH_MONTH("");
                 planner.setSPECIAL_REQ_MONTH(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i));//
                 planner.setUPDATED_SPECIAL_REQ_MONTH("");
                 planner.setSPECIAL_REQ_MONTH_DATE("");
                 planner.setUPDATED_SPECIAL_REQ_MONTH("");
                 planner.setEXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("EXP_WIP_DELIVERY_DATE", i)));//
                 planner.setUPDATED_EXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("ACT_WIP_DELIVERY_DATE", i)));//
                 planner.setEXP_PI_DATE(EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("EXP_PI_DATE", i)));//
                 planner.setUPDATED_EXP_PI_DATE("");
                 planner.setUSER_ID(EITLERPGLOBAL.gUserID+"");
                 planner.setUPDATE_STATUS("PENDING");
                 planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                 planner.setEXP_PAY_CHQ_RCV_DATE("0000-00-00");
                 planner.saveTransaction();
                 }
                 else if(!"".equals(RESCHEDULE_MONTH))
                 {
                 JOptionPane.showMessageDialog(null, "Reschedule not possible for PIECE if OC MONTH exist");
                 }*/
                //}
            }

                    //AppletFrame aFrame = new AppletFrame("Felt Piece Order Reschedule");
            //aFrame.startAppletEx("EITLERP.FeltSales.OrderReschedule.FrmPieceReschedule", "Felt Piece Order Reschedule");
            //FrmPieceReschedule ObjItem = (FrmPieceReschedule) aFrame.ObjApplet;
                    //ObjItem.objFromMainForm = Obj;
            //ObjItem.FillPiece();
            updateActivitiesTransactions();
            Date date = new Date();
                    //Disable for Testing Only
            //29 April 2019
            //10/10/2021
            //if(date.getDate()>=15 && date.getDate()<=25 && LOGIN_USER_TYPE.equals("SALES")) // On 26/04/2019
            if ((!(date.getDate() >= 11 && date.getDate() <= 14) && LOGIN_USER_TYPE.equals("SALES"))) //            if(date.getDate()>=15 && date.getDate()<=26 && LOGIN_USER_TYPE.equals("SALES"))
            {
                try {
                    HashMap Obj = new HashMap();
                    int size = 0;
                            //ObjItem.frm_S_O_No = S_O_NO.getText();

                    //ObjItem.DataSettingFromOrder();
                    for (int i = 0; i < tblPlanning.getRowCount(); i++) {

                        if (tblPlanning.getValueAt(i, 1).equals(true)) {
                            String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
                            String RESCHEDULE_MONTH = DataModel_Planning.getValueByVariable("RESCHEDULE_MONTH", i);
                            String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);

                            String OC_MONTH_exist = DataModel_Planning.getValueByVariable("OC_MONTH", i);

                            if (!"".equals(OC_MONTH_exist)) {
                                JOptionPane.showMessageDialog(null, "OC MONTH already exist for Piece " + PIECE_NO);
                                return;
                            }

                            if (!"".equals(OC_MONTH) && !"".equals(RESCHEDULE_MONTH)) {
                                JOptionPane.showMessageDialog(null, "Reschedule Not possible for Piece " + PIECE_NO + ", if OC MONTH exist");
                                return;
                            }
                            clsPieceOCDetails objDetail = new clsPieceOCDetails();
                            objDetail.setAttribute("PIECE_NO", PIECE_NO);
                            objDetail.setAttribute("OC_MONTH", OC_MONTH);
                            Obj.put(size++, objDetail);

                            try {
                                String PR_PARTY_CODE = DataModel_Planning.getValueByVariable("PR_PARTY_CODE", i);
                                String CONTACT_PERSON = DataModel_Planning.getValueByVariable("CONTACT_PERSON", i);
                                String PHONE_NO = DataModel_Planning.getValueByVariable("PHONE_NO", i);
                                String EMAIL_ID = DataModel_Planning.getValueByVariable("EMAIL_ID", i);
                                String EMAIL_ID2 = DataModel_Planning.getValueByVariable("EMAIL_ID2", i);
                                String EMAIL_ID3 = DataModel_Planning.getValueByVariable("EMAIL_ID3", i);

                                data.Execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER SET CONTACT_PERSON='" + CONTACT_PERSON + "',PHONE_NO='" + PHONE_NO + "',EMAIL='" + EMAIL_ID + "',EMAIL_ID2='" + EMAIL_ID2 + "',EMAIL_ID3='" + EMAIL_ID3 + "' WHERE PARTY_CODE='" + PR_PARTY_CODE + "' AND COMPANY_ID=2");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    for (int i = 0; i < tblWIP.getRowCount(); i++) {
                        if (tblWIP.getValueAt(i, 1).equals(true)) {
                            String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
                            String RESCHEDULE_MONTH = DataModel_WIP.getValueByVariable("RESCHEDULE_MONTH", i);
                            String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);

                            String OC_MONTH_exist = DataModel_Planning.getValueByVariable("OC_MONTH", i);

                            if (!"".equals(OC_MONTH_exist)) {
                                JOptionPane.showMessageDialog(null, "OC MONTH already exist for Piece " + PIECE_NO);
                                return;
                            }

                            if (!"".equals(OC_MONTH) && !"".equals(RESCHEDULE_MONTH)) {
                                JOptionPane.showMessageDialog(null, "Reschedule Not possible for Piece " + PIECE_NO + ", if OC MONTH exist");
                                return;
                            }
                            clsPieceOCDetails objDetail = new clsPieceOCDetails();
                            objDetail.setAttribute("PIECE_NO", PIECE_NO);
                            objDetail.setAttribute("OC_MONTH", OC_MONTH);
                            Obj.put(size++, objDetail);

                            try {
                                String PR_PARTY_CODE = DataModel_WIP.getValueByVariable("PR_PARTY_CODE", i);
                                String CONTACT_PERSON = DataModel_WIP.getValueByVariable("CONTACT_PERSON", i);
                                String PHONE_NO = DataModel_WIP.getValueByVariable("PHONE_NO", i);
                                String EMAIL_ID = DataModel_WIP.getValueByVariable("EMAIL_ID", i);
                                String EMAIL_ID2 = DataModel_WIP.getValueByVariable("EMAIL_ID2", i);
                                String EMAIL_ID3 = DataModel_WIP.getValueByVariable("EMAIL_ID3", i);

                                data.Execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER SET CONTACT_PERSON='" + CONTACT_PERSON + "',PHONE_NO='" + PHONE_NO + "',EMAIL='" + EMAIL_ID + "',EMAIL_ID2='" + EMAIL_ID2 + "',EMAIL_ID3='" + EMAIL_ID3 + "' WHERE PARTY_CODE='" + PR_PARTY_CODE + "' AND COMPANY_ID=2");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    /*for(int i=0;i<tblStockBsr.getRowCount();i++)
                     {
                     if(tblStockBsr.getValueAt(i, 1).equals(true))
                     {

                     String OC_MONTH  = cmbMonthYear.getSelectedItem().toString();
                     String RESCHEDULE_MONTH = DataModel_STOCK.getValueByVariable("RESCHEDULE_MONTH", i);
                     String PIECE_NO = DataModel_STOCK.getValueByVariable("PR_PIECE_NO", i);

                     String OC_MONTH_exist = DataModel_Planning.getValueByVariable("OC_MONTH", i);

                     if(!"".equals(OC_MONTH_exist))
                     {
                     JOptionPane.showMessageDialog(null, "OC MONTH already exist for Piece "+PIECE_NO);
                     return;
                     }
                     if(!"".equals(OC_MONTH) && !"".equals(RESCHEDULE_MONTH)){
                     JOptionPane.showMessageDialog(null, "Reschedule Not possible for Piece "+PIECE_NO+", if OC MONTH exist");
                     return;
                     }
                     clsPieceOCDetails objDetail = new clsPieceOCDetails();
                     objDetail.setAttribute("PIECE_NO", PIECE_NO);
                     objDetail.setAttribute("OC_MONTH", OC_MONTH);
                     Obj.put(size++, objDetail);
                     }
                     }*/
                    if (!Obj.isEmpty()) {
                        String FELT_TYPE = "";
                        if (rdoExceptSDF.isSelected()) {
                            FELT_TYPE = "All(Except SDF)";
                        } else {
                            FELT_TYPE = "SDF";
                        }

                        String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
                        String doc_data_pending_to_VDS = data.getStringValueFromDB("SELECT MAX(H.OC_DOC_NO) FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.OC_MONTH='" + OC_MONTH + "' AND FELT_TYPE='" + FELT_TYPE + "' AND H.OC_DOC_NO=D.DOC_NO AND H.APPROVED=0 AND D.USER_ID='28' AND D.STATUS='W'");

                        if (doc_data_pending_to_VDS.equals("")) {
                            AppletFrame aFrame = new AppletFrame("Felt Piece Order Confirmation");
                            aFrame.startAppletEx("EITLERP.FeltSales.OrderConfirmation.FrmPieceOC", "Felt Piece Order Confirmation");
                            FrmPieceOC ObjItem = (FrmPieceOC) aFrame.ObjApplet;

                            ObjItem.objFromMainForm = Obj;

                            //It will call only if OC_MONTH data not found in OC Month Confirmation Form.
                            ObjItem.FillPiece();
                                    //if(EITLERPGLOBAL.gUserID == 136)
                            //{
                            ObjItem.OC_MONTH = OC_MONTH;
                            ObjItem.FELT_TYPE = FELT_TYPE;
                            ObjItem.SelHierarchyID = 3746;
                            ObjItem.Save();
                            //}
                        } else {
                            int max_sr_no = data.getIntValueFromDB("SELECT MAX(SR_NO*1) FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL where OC_DOC_NO='" + doc_data_pending_to_VDS + "'");
                            for (int i = 0; i < Obj.size(); i++) {
                                clsPieceOCDetails ObjHistory = (clsPieceOCDetails) Obj.get(i);

                                max_sr_no++;

                                String PIECE_NO = ObjHistory.getAttribute("PIECE_NO").getString();
                                //String OC_MONTH = ObjHistory.getAttribute("OC_MONTH").getString();

                                ResultSet rsPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                rsPiece.first();

                                        //                                        ResultSet rsData = data.getResult("SELECT CONTACT_PERSON,PHONE_NUMBER,EMAIL_ID,EMAIL_ID2,EMAIL_ID3 FROM PRODUCTION.FELT_SALES_ORDER_DETAIL d,PRODUCTION.FELT_SALES_ORDER_HEADER h where PIECE_NO='"+PIECE_NO+"' AND h.S_ORDER_NO=d.S_ORDER_NO");
                                //                                        rsData.first();
                                //                                        System.out.println("INSERT INTO PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL (SR_NO,OC_DOC_NO,PIECE_NO,OC_NO,OC_DATE,OC_MONTH,PIECE_STAGE,REQ_MONTH,REMARK,CONTACT_PERSON,PHONE_NO,EMAIL_ID,EMAIL_ID2,EMAIL_ID3,ADD_PIECE_BY,ADD_PIECE_DATE)"
                                //                                                                                           +"              VALUES ("+max_sr_no+",'"+doc_data_pending_to_VDS+"','"+PIECE_NO+"','','0000-00-00','"+OC_MONTH+"','"+rsPiece.getString("PR_PIECE_STAGE")+"','"+rsPiece.getString("PR_REQUESTED_MONTH")+"','','"+rsData.getString("CONTACT_PERSON")+"','"+rsData.getString("PHONE_NUMBER")+"','"+rsData.getString("EMAIL_ID")+"','"+rsData.getString("EMAIL_ID2")+"','"+rsData.getString("EMAIL_ID3")+"','"+EITLERPGLOBAL.gUserID+"','"+EITLERPGLOBAL.getCurrentDateDB()+"')");
                                //                                        data.Execute("INSERT INTO PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL (SR_NO,OC_DOC_NO,PIECE_NO,OC_NO,OC_DATE,OC_MONTH,PIECE_STAGE,REQ_MONTH,REMARK,CONTACT_PERSON,PHONE_NO,EMAIL_ID,EMAIL_ID2,EMAIL_ID3,ADD_PIECE_BY,ADD_PIECE_DATE)"
                                //                                                                                           +"              VALUES ("+max_sr_no+",'"+doc_data_pending_to_VDS+"','"+PIECE_NO+"','','0000-00-00','"+OC_MONTH+"','"+rsPiece.getString("PR_PIECE_STAGE")+"','"+rsPiece.getString("PR_REQUESTED_MONTH")+"','','"+rsData.getString("CONTACT_PERSON")+"','"+rsData.getString("PHONE_NUMBER")+"','"+rsData.getString("EMAIL_ID")+"','"+rsData.getString("EMAIL_ID2")+"','"+rsData.getString("EMAIL_ID3")+"','"+EITLERPGLOBAL.gUserID+"','"+EITLERPGLOBAL.getCurrentDateDB()+"')");
                                String CONTACT_PERSON = "", PHONE_NUMBER = "", EMAIL_ID = "", EMAIL_ID2 = "", EMAIL_ID3 = "";
                                        //                                        try{
                                //                                            ResultSet rsData = data.getResult("SELECT CONTACT_PERSON,PHONE_NO,EMAIL,EMAIL_ID2,EMAIL_ID3 FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+rsPiece.getString("PR_PARTY_CODE")+"'");
                                //                                            rsData.first();
                                //
                                //                                            CONTACT_PERSON = rsData.getString("CONTACT_PERSON");
                                //                                            PHONE_NUMBER = rsData.getString("PHONE_NO");
                                //                                            EMAIL_ID = rsData.getString("EMAIL");
                                //                                            EMAIL_ID2 = rsData.getString("EMAIL_ID2");
                                //                                            EMAIL_ID3 = rsData.getString("EMAIL_ID3");
                                //                                        }catch(Exception e)
                                //                                        {
                                //                                            e.printStackTrace();
                                //                                        }
                                System.out.println("INSERT INTO PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL (SR_NO,OC_DOC_NO,PIECE_NO,OC_NO,OC_DATE,OC_MONTH,PIECE_STAGE,REQ_MONTH,REMARK,CONTACT_PERSON,PHONE_NO,EMAIL_ID,EMAIL_ID2,EMAIL_ID3,ADD_PIECE_BY,ADD_PIECE_DATE)"
                                        + "              VALUES (" + max_sr_no + ",'" + doc_data_pending_to_VDS + "','" + PIECE_NO + "','','0000-00-00','" + OC_MONTH + "','" + rsPiece.getString("PR_PIECE_STAGE") + "','" + rsPiece.getString("PR_REQUESTED_MONTH") + "','','" + CONTACT_PERSON + "','" + PHONE_NUMBER + "','" + EMAIL_ID + "','" + EMAIL_ID2 + "','" + EMAIL_ID3 + "','" + EITLERPGLOBAL.gUserID + "','" + EITLERPGLOBAL.getCurrentDateDB() + "')");
                                data.Execute("INSERT INTO PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL (SR_NO,OC_DOC_NO,PIECE_NO,OC_NO,OC_DATE,OC_MONTH,PIECE_STAGE,REQ_MONTH,REMARK,CONTACT_PERSON,PHONE_NO,EMAIL_ID,EMAIL_ID2,EMAIL_ID3,ADD_PIECE_BY,ADD_PIECE_DATE)"
                                        + "              VALUES (" + max_sr_no + ",'" + doc_data_pending_to_VDS + "','" + PIECE_NO + "','','0000-00-00','" + OC_MONTH + "','" + rsPiece.getString("PR_PIECE_STAGE") + "','" + rsPiece.getString("PR_REQUESTED_MONTH") + "','','" + CONTACT_PERSON + "','" + PHONE_NUMBER + "','" + EMAIL_ID + "','" + EMAIL_ID2 + "','" + EMAIL_ID3 + "','" + EITLERPGLOBAL.gUserID + "','" + EITLERPGLOBAL.getCurrentDateDB() + "')");

                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (DEPT_ID == 40) {
            //P.P.C. Login
            for (int i = 0; i < tblWIP.getRowCount(); i++) {
                String EXP_WIP_DELIVERY_DATE = DataModel_WIP.getValueByVariable("EXP_WIP_DELIVERY_DATE", i);

                if (!"".equals(EXP_WIP_DELIVERY_DATE)) {

                    try {
                        if (!"".equals(EXP_WIP_DELIVERY_DATE)) {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(EXP_WIP_DELIVERY_DATE);
                            Date today = new Date();

                            if (date.before(today)) {
                                JOptionPane.showMessageDialog(null, EXP_WIP_DELIVERY_DATE + " is not valid!");
                                //return;
                            } else {

                                long ltime = date.getTime() + 2 * 24 * 60 * 60 * 1000;
                                Date PI_DATE_dateformat = new Date(ltime);

                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String PI_DATE = formatter.format(PI_DATE_dateformat);

                                clsPlannerActivities planner = new clsPlannerActivities();
                                planner.setPLANNER_TYPE("EXP_WIP_DEL_DATE");
                                planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                                planner.setPIECE_NO(DataModel_WIP.getValueByVariable("PR_PIECE_NO", i));
                                planner.setPARTY_CODE(DataModel_WIP.getValueByVariable("PR_PARTY_CODE", i));//
                                planner.setUPN(DataModel_WIP.getValueByVariable("UPN_PIECES", i));//
                                planner.setREQ_MONTH(DataModel_WIP.getValueByVariable("PR_REQUESTED_MONTH", i));//
                                planner.setRESCHEDULE_REQ_MONTH(DataModel_WIP.getValueByVariable("RESCHEDULE_MONTH", i));//
                                planner.setOC_MONTH(DataModel_WIP.getValueByVariable("OC_MONTH", i));//
                                planner.setUPDATED_OC_MONTH("");
                                planner.setCURRENT_SCH_MONTH(DataModel_WIP.getValueByVariable("CURRENT_SCHEDULE_MONTH", i));//
                                planner.setUPDATED_CURRENT_SCH_MONTH("");
                                planner.setSPECIAL_REQ_MONTH(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i));//
                                planner.setUPDATED_SPECIAL_REQ_MONTH("");
                                planner.setSPECIAL_REQ_MONTH_DATE(DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_DATE", i));
                                planner.setUPDATED_SPECIAL_REQ_MONTH("");
                                planner.setEXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_WIP_DELIVERY_DATE", i)));//
                                planner.setUPDATED_EXP_WIP_DELIVERY_DATE(EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("ACT_WIP_DELIVERY_DATE", i)));//
                                planner.setEXP_PI_DATE(EITLERPGLOBAL.formatDateDB(PI_DATE));//
                                planner.setUPDATED_EXP_PI_DATE("");
                                planner.setUSER_ID(EITLERPGLOBAL.gUserID + "");
                                planner.setUPDATE_STATUS("PENDING");
                                planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                                planner.setEXP_PAY_CHQ_RCV_DATE("0000-00-00");
                                planner.saveTransaction();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            updateActivitiesTransactions();
        } else {
            //OTHER USER LOGIN
            disableAllTable();
        }
        JOptionPane.showMessageDialog(this, "DATA UPDATED..!");
        if (!"".equals(SPCL_REQ_MONTH_INVALID)) {
            JOptionPane.showMessageDialog(this, "You can not enter SPECIAL REQ MONTH before OC MONTH of Piece No (" + SPCL_REQ_MONTH_INVALID + ")");
            SPCL_REQ_MONTH_INVALID = "";
        }
        setPlanningPieces();
        setWIPPieces();
        setSTOCKPieces();
        setDispatchPieces();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void rdoCurSchMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoCurSchMonthItemStateChanged
        // TODO add your handling code here:
        GenerateComboMonthYear();
    }//GEN-LAST:event_rdoCurSchMonthItemStateChanged

    private void rdoCurSchMonthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoCurSchMonthMouseClicked
        // TODO add your handling code here:
        //GenerateComboMonthYear();
        if (rdoRequestMonth.isSelected() || rdoOcMonth.isSelected() || rdoCurSchMonth.isSelected()) {
            setPlanningPieces();
            setWIPPieces();
            setSTOCKPieces();
            setDispatchPieces();
        }
    }//GEN-LAST:event_rdoCurSchMonthMouseClicked

    private void rdoOcMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOcMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoOcMonthActionPerformed

    private void rdoOcMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoOcMonthItemStateChanged
        // TODO add your handling code here:
        GenerateComboMonthYear();

    }//GEN-LAST:event_rdoOcMonthItemStateChanged

    private void rdoOcMonthMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoOcMonthMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoOcMonthMouseReleased

    private void rdoOcMonthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoOcMonthMouseClicked
        // TODO add your handling code here:
        //GenerateComboMonthYear();
        if (rdoRequestMonth.isSelected() || rdoOcMonth.isSelected() || rdoCurSchMonth.isSelected()) {
            setPlanningPieces();
            setWIPPieces();
            setSTOCKPieces();
            setDispatchPieces();
        }

    }//GEN-LAST:event_rdoOcMonthMouseClicked

    private void rdoRequestMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRequestMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoRequestMonthActionPerformed

    private void rdoRequestMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoRequestMonthItemStateChanged
        // TODO add your handling code here:
        GenerateComboMonthYear();
    }//GEN-LAST:event_rdoRequestMonthItemStateChanged

    private void rdoRequestMonthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoRequestMonthMouseClicked
        // TODO add your handling code here:
        //GenerateComboMonthYear();
        if (rdoRequestMonth.isSelected() || rdoOcMonth.isSelected() || rdoCurSchMonth.isSelected()) {
            setPlanningPieces();
            setWIPPieces();
            setSTOCKPieces();
            setDispatchPieces();
        }
    }//GEN-LAST:event_rdoRequestMonthMouseClicked

    private void cmbProdGroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProdGroupItemStateChanged
        // TODO add your handling code here:
        setPlanningPieces();
        setWIPPieces();
        setSTOCKPieces();
        setDispatchPieces();
    }//GEN-LAST:event_cmbProdGroupItemStateChanged

    private void cmbZoneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbZoneItemStateChanged
        // TODO add your handling code here:
        setPlanningPieces();
        setWIPPieces();
        setSTOCKPieces();
        setDispatchPieces();
    }//GEN-LAST:event_cmbZoneItemStateChanged

    private void tblDispatchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDispatchMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if (tblDispatch.getSelectedColumn() == DataModel_Dispatch.getColFromVariable("UPN_PIECES")) {
                String UPN_NO = DataModel_Dispatch.getValueByVariable("UPN_PIECES", tblDispatch.getSelectedRow());
                String PR_PIECE_NO = DataModel_Dispatch.getValueByVariable("PR_PIECE_NO", tblDispatch.getSelectedRow());
                searchkey_UPNwise search_check = new searchkey_UPNwise();
                search_check.SQL = "SELECT PR_PIECE_NO as 'PIECE NO',PR_PARTY_CODE as 'PARTY CODE',PARTY_NAME as 'PARTY NAME',PR_MACHINE_NO as 'MAC NO',POSITION_DESC as 'POS DESC',PR_UPN AS 'UPN NO',PR_REQUESTED_MONTH AS 'REQUESTED MONTH','' as 'RESCHEDULE MTH', PR_OC_MONTHYEAR AS 'OC MONTH',PR_PIECE_STAGE AS 'PIECE STAGE', PR_PRODUCT_CODE AS 'PROD CODE',PR_GROUP AS 'PROD GROUP' "
                        + "  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=PR_POSITION_NO AND PR_PARTY_CODE=PARTY_CODE AND PR_UPN='" + UPN_NO + "' AND PR_PIECE_NO!='" + PR_PIECE_NO + "' AND  PR_PIECE_STAGE IN ('PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')";
                search_check.ReturnCol = 1;
                search_check.UPN = UPN_NO;
                search_check.ShowReturnCol = true;
                search_check.DefaultSearchOn = 1;
                search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
                if (search_check.ShowRSLOV()) {

                }
            }

            if (tblDispatch.getSelectedColumn() == DataModel_Dispatch.getColFromVariable("PR_PARTY_CODE")) {
                String PARTY_CODE = DataModel_Dispatch.getValueByVariable("PR_PARTY_CODE", tblDispatch.getSelectedRow());
                searchkey_invoiced_partywise search_check = new searchkey_invoiced_partywise();
                search_check.SQL = "SELECT PR_PIECE_NO AS 'PIECE_NO',PR_UPN AS 'UPN',PR_GROUP AS 'GROUP',PR_OC_MONTHYEAR AS 'OC_MONTH',PR_FNSG_DATE AS 'WAREHOUSE_DATE',PR_INVOICE_DATE  AS 'DISPATCHED_DATE',PR_OC_LAST_DDMMYY FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PARTY_CODE + "' AND PR_PIECE_STAGE='INVOICED' ORDER BY PR_INVOICE_DATE DESC LIMIT 10";
                search_check.ReturnCol = 1;
                search_check.PARTY_CODE = PARTY_CODE;
                search_check.ShowReturnCol = true;
                search_check.DefaultSearchOn = 1;
                search_check.lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE));
                search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
                if (search_check.ShowRSLOV()) {

                }
            }
        }
    }//GEN-LAST:event_tblDispatchMouseClicked

    private void tblStockBsrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblStockBsrKeyReleased
        // TODO add your handling code here:

        if (tblStockBsr.getSelectedColumn() == 19 && LOGIN_USER_TYPE == "SALES") {
            try {
                String EXP_PAY_CHQ_RCV_DATE = DataModel_STOCK.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", tblStockBsr.getSelectedRow());
                String EXP_WIP_DELIVERY_DATE = DataModel_STOCK.getValueByVariable("EXP_WIP_DELIVERY_DATE", tblStockBsr.getSelectedRow());
                String ACT_WIP_DELIVERY_DATE = DataModel_STOCK.getValueByVariable("ACT_WIP_DELIVERY_DATE", tblStockBsr.getSelectedRow());
                if (EXP_WIP_DELIVERY_DATE.equals("") && ACT_WIP_DELIVERY_DATE.equals("") && !EXP_PAY_CHQ_RCV_DATE.equals("")) {
                    JOptionPane.showMessageDialog(this, "To add Exp Payment Date, Exp Delivery date must!");
                    DataModel_STOCK.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblStockBsr.getSelectedRow());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ((tblStockBsr.getSelectedColumn() == 19 || tblStockBsr.getSelectedColumn() == 20) && (evt.getKeyCode() == KeyEvent.VK_TAB || evt.getKeyCode() == 10 || evt.getKeyCode() == 37 || evt.getKeyCode() == 38 || evt.getKeyCode() == 39 || evt.getKeyCode() == 40) && LOGIN_USER_TYPE == "SALES") {
            try {
                String EXP_PAY_CHQ_RCV_DATE = EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", tblStockBsr.getSelectedRow()));
                Date exp_pay_date = new SimpleDateFormat("yyyy-MM-dd").parse(EXP_PAY_CHQ_RCV_DATE);
                Date today = new Date();
                if (exp_pay_date.before(today)) {
                    JOptionPane.showMessageDialog(this, "Previous date not allowed.");
                    DataModel_STOCK.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblStockBsr.getSelectedRow());
                }
                if (exp_pay_date.getDay() != 6) {
                    JOptionPane.showMessageDialog(this, "Please enter SATURDAY date only.");
                    DataModel_STOCK.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblStockBsr.getSelectedRow());
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }

            try {
                String EXP_PAY_CHQ_RCV_DATE = EITLERPGLOBAL.formatDateDB(DataModel_STOCK.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", tblStockBsr.getSelectedRow() - 1));
                Date exp_pay_date = new SimpleDateFormat("yyyy-MM-dd").parse(EXP_PAY_CHQ_RCV_DATE);
                Date today = new Date();
                if (exp_pay_date.before(today)) {
                    JOptionPane.showMessageDialog(this, "Previous date not allowed.");
                    DataModel_STOCK.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblStockBsr.getSelectedRow() - 1);
                }
                if (exp_pay_date.getDay() != 6) {
                    JOptionPane.showMessageDialog(this, "Please enter SATURDAY date only.");
                    DataModel_STOCK.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblStockBsr.getSelectedRow() - 1);
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }

        }

    }//GEN-LAST:event_tblStockBsrKeyReleased

    private void tblStockBsrMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockBsrMousePressed
        // TODO add your handling code here:
        /*    if(tblStockBsr.getSelectedColumn() == 10)
         {
         if(tblStockBsr.getValueAt( tblStockBsr.getSelectedRow(), 1).equals(true))
         {
         JOptionPane.showMessageDialog(null, "Reschedule not possible, because OC Month Confirmation selected.");
         }

         String OC_MONTH = DataModel_STOCK.getValueByVariable("OC_MONTH", tblStockBsr.getSelectedRow());
         if(!OC_MONTH.equals(""))
         {
         JOptionPane.showMessageDialog(null, "Reschedule not possible, because OC Month is is not blank.");
         }
         }*/
    }//GEN-LAST:event_tblStockBsrMousePressed

    private void tblStockBsrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockBsrMouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {
            if (tblStockBsr.getSelectedColumn() == DataModel_STOCK.getColFromVariable("UPN_PIECES")) {
                String UPN_NO = DataModel_STOCK.getValueByVariable("UPN_PIECES", tblStockBsr.getSelectedRow());
                String PR_PIECE_NO = DataModel_STOCK.getValueByVariable("PR_PIECE_NO", tblStockBsr.getSelectedRow());
                searchkey_UPNwise search_check = new searchkey_UPNwise();
                search_check.SQL = "SELECT PR_PIECE_NO as 'PIECE NO',PR_PARTY_CODE as 'PARTY CODE',PARTY_NAME as 'PARTY NAME',PR_MACHINE_NO as 'MAC NO',POSITION_DESC as 'POS DESC',PR_UPN AS 'UPN NO',PR_REQUESTED_MONTH AS 'REQUESTED MONTH','' as 'RESCHEDULE MTH', PR_OC_MONTHYEAR AS 'OC MONTH',PR_PIECE_STAGE AS 'PIECE STAGE', PR_PRODUCT_CODE AS 'PROD CODE',PR_GROUP AS 'PROD GROUP' "
                        + "  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=PR_POSITION_NO AND PR_PARTY_CODE=PARTY_CODE AND PR_UPN='" + UPN_NO + "' AND PR_PIECE_NO!='" + PR_PIECE_NO + "' AND  PR_PIECE_STAGE IN ('PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')";
                search_check.ReturnCol = 1;
                search_check.UPN = UPN_NO;
                search_check.ShowReturnCol = true;
                search_check.DefaultSearchOn = 1;
                search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
                if (search_check.ShowRSLOV()) {

                }
            }

            if (tblStockBsr.getSelectedColumn() == DataModel_STOCK.getColFromVariable("PR_PARTY_CODE")) {
                String PARTY_CODE = DataModel_STOCK.getValueByVariable("PR_PARTY_CODE", tblStockBsr.getSelectedRow());
                searchkey_invoiced_partywise search_check = new searchkey_invoiced_partywise();
                search_check.SQL = "SELECT PR_PIECE_NO AS 'PIECE_NO',PR_UPN AS 'UPN',PR_GROUP AS 'GROUP',PR_OC_MONTHYEAR AS 'OC_MONTH',PR_FNSG_DATE AS 'WAREHOUSE_DATE',PR_INVOICE_DATE  AS 'DISPATCHED_DATE',PR_OC_LAST_DDMMYY FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PARTY_CODE + "' AND PR_PIECE_STAGE='INVOICED' ORDER BY PR_INVOICE_DATE DESC LIMIT 10";
                search_check.ReturnCol = 1;
                search_check.PARTY_CODE = PARTY_CODE;
                search_check.ShowReturnCol = true;
                search_check.DefaultSearchOn = 1;
                search_check.lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE));
                search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
                if (search_check.ShowRSLOV()) {

                }
            }
        }

        /*
         if(evt.getClickCount()==2)
         {
         if(tblStockBsr.getSelectedColumn()==DataModel_STOCK.getColFromVariable("UPN_PIECES"))
         {
         String UPN_NO = DataModel_STOCK.getValueByVariable("UPN_PIECES", tblStockBsr.getSelectedRow());
         searchkey_UPNwise search_check = new searchkey_UPNwise();
         search_check.SQL = "SELECT PR_PIECE_NO as 'PIECE NO',PR_PARTY_CODE as 'PARTY CODE',PARTY_NAME as 'PARTY NAME',PR_MACHINE_NO as 'MAC NO',PR_POSITION_NO as 'POS NO',POSITION_DESC as 'POS DESC',PR_UPN AS 'UPN NO',PR_REQUESTED_MONTH AS 'REQUESTED MONTH','' as 'RESCHEDULE MTH', PR_OC_MONTHYEAR AS 'OC MONTH',PR_PIECE_STAGE AS 'PIECE STAGE', PR_PRODUCT_CODE AS 'PROD CODE',PR_GROUP AS 'PROD GROUP' " +
         "  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=PR_POSITION_NO AND PR_PARTY_CODE=PARTY_CODE AND PR_UPN='"+UPN_NO+"' AND PR_OC_MONTHYEAR='' AND PR_PIECE_STAGE IN ('PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR')";
         search_check.ReturnCol = 1;
         search_check.UPN = UPN_NO;
         search_check.ShowReturnCol = true;
         search_check.DefaultSearchOn = 1;
         search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
         if (search_check.ShowRSLOV()) {

         }
         }
         }
         String OC_MONTH_exist = DataModel_STOCK.getValueByVariable("OC_MONTH", tblStockBsr.getSelectedRow());

         String RESCHEDULE_MONTH_exist = DataModel_STOCK.getValueByVariable("RESCHEDULE_MONTH", tblStockBsr.getSelectedRow());

         if(!"".equals(RESCHEDULE_MONTH_exist)  && tblStockBsr.getSelectedColumn()==1)
         {
         JOptionPane.showMessageDialog(null, "Cannot Confirm");
         DataModel_STOCK.setValueByVariable("SELECT", false ,tblStockBsr.getSelectedRow());
         return;
         }

         if(!"".equals(OC_MONTH_exist)  && tblStockBsr.getSelectedColumn()==1)
         {
         JOptionPane.showMessageDialog(null, "OC MONTH already exist ");
         DataModel_STOCK.setValueByVariable("SELECT", false ,tblStockBsr.getSelectedRow());
         return;
         }

         if(tblStockBsr.getSelectedColumn()==1)
         {
         String PR_PIECE_NO = DataModel_STOCK.getValueByVariable("PR_PIECE_NO", tblStockBsr.getSelectedRow());
         String OC_DOC_NO = data.getStringValueFromDB("SELECT H.OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D,PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H " +
         " WHERE H.OC_DOC_NO=D.OC_DOC_NO AND D.PIECE_NO='"+PR_PIECE_NO+"' AND H.APPROVED=0;");
         if(!"".equals(OC_DOC_NO))
         {
         JOptionPane.showMessageDialog(null, "This Piece already pending in DOC NO "+OC_DOC_NO);
         DataModel_STOCK.setValueByVariable("SELECT", false ,tblStockBsr.getSelectedRow());
         return;
         }

         String UPN = DataModel_STOCK.getValueByVariable("UPN_PIECES", tblStockBsr.getSelectedRow());
         String PR_REQUESTED_MONTH = DataModel_STOCK.getValueByVariable("PR_REQUESTED_MONTH", tblStockBsr.getSelectedRow());
         String SPILOVER_PIECE = data.getStringValueFromDB("SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE (PR_SP_MONTHYEAR!='0' OR PR_SP_MONTHYEAR IS NOT NULL OR PR_SP_MONTHYEAR!='') AND PR_REQUESTED_MONTH='"+PR_REQUESTED_MONTH+"' AND PR_UPN='"+UPN+"' AND PR_PIECE_STAGE NOT IN ('CANCELLED','DIVERTED','DIVIDED','INVOICED')");

         if(!"".equals(SPILOVER_PIECE))
         {
         int n = JOptionPane.showConfirmDialog(this, "This UPN "+UPN+" has spilover piece "+SPILOVER_PIECE+", Do you want to continue?","Attention.. Please Confirm",JOptionPane.YES_NO_OPTION);
         if(n==0)
         {

         }
         else
         {
         DataModel_STOCK.setValueByVariable("SELECT", false ,tblStockBsr.getSelectedRow());
         return;
         }
         }
         }
         */
    }//GEN-LAST:event_tblStockBsrMouseClicked

    private void tblWIPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblWIPKeyReleased
        // TODO add your handling code here:

        if (tblWIP.getSelectedColumn() == 19 && LOGIN_USER_TYPE == "SALES") {
            try {
                String EXP_PAY_CHQ_RCV_DATE = DataModel_WIP.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", tblWIP.getSelectedRow());
                String EXP_WIP_DELIVERY_DATE = DataModel_WIP.getValueByVariable("EXP_WIP_DELIVERY_DATE", tblWIP.getSelectedRow());
                if (EXP_WIP_DELIVERY_DATE.equals("") && !EXP_PAY_CHQ_RCV_DATE.equals("")) {
                    JOptionPane.showMessageDialog(this, "To add Exp Payment Date, Exp Delivery date must!");
                    DataModel_WIP.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblWIP.getSelectedRow());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ((tblWIP.getSelectedColumn() == 14) && (evt.getKeyCode() == KeyEvent.VK_TAB || evt.getKeyCode() == 10 || evt.getKeyCode() == 37 || evt.getKeyCode() == 38 || evt.getKeyCode() == 39 || evt.getKeyCode() == 40)) {
            try {
                String SPECIAL_REQ_MONTH_DATE = DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_DATE", tblWIP.getSelectedRow());
                String OC_MONTH = DataModel_WIP.getValueByVariable("OC_MONTH", tblWIP.getSelectedRow());
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(SPECIAL_REQ_MONTH_DATE);
                //System.out.println("output month : "+date.getMonth());
                //System.out.println("output year : "+date.getYear());

                Date curr_date = new Date();
                if (date.before(curr_date)) {
                    DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_DATE", "", tblWIP.getSelectedRow());
                    DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", "", tblWIP.getSelectedRow());
                    JOptionPane.showMessageDialog(this, "Previous date not allowed");
                    return;
                }

                int month = date.getMonth() + 1;
                int year = date.getYear() + 1900;

                String SPECIAL_REQ_MONTH = getMonthName(month) + " - " + year;

                int counter = 0;
                for (int i = 0; i < tblWIP.getRowCount() - 1; i++) {
                    String exist_Month = DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i);

                    if (SPECIAL_REQ_MONTH.equals(exist_Month)) {
                        counter++;
                    }
                }

                if (counter > 10 && !SPECIAL_REQ_MONTH.equals(OC_MONTH)) {
                    DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_DATE", "", tblWIP.getSelectedRow());
                    DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", "", tblWIP.getSelectedRow());
                    JOptionPane.showMessageDialog(this, "Only 10 Request Month allowed per month");
                } else {
                    DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", SPECIAL_REQ_MONTH, tblWIP.getSelectedRow());
                }

            } catch (Exception e) {
                DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", "", tblWIP.getSelectedRow());
                DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_DATE", "", tblWIP.getSelectedRow());
                e.printStackTrace();
            }
        }

        if ((tblWIP.getSelectedColumn() == 19 || tblWIP.getSelectedColumn() == 20) && (evt.getKeyCode() == KeyEvent.VK_TAB || evt.getKeyCode() == 10 || evt.getKeyCode() == 37 || evt.getKeyCode() == 38 || evt.getKeyCode() == 39 || evt.getKeyCode() == 40) && LOGIN_USER_TYPE == "SALES") {
            try {
                String EXP_PAY_CHQ_RCV_DATE = EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", tblWIP.getSelectedRow()));
                Date exp_pay_date = new SimpleDateFormat("yyyy-MM-dd").parse(EXP_PAY_CHQ_RCV_DATE);
                Date today = new Date();
                if (exp_pay_date.before(today)) {
                    JOptionPane.showMessageDialog(this, "Previous date not allowed.");
                    DataModel_WIP.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblWIP.getSelectedRow());
                }
                if (exp_pay_date.getDay() != 6) {
                    JOptionPane.showMessageDialog(this, "Please enter SATURDAY date only.");
                    DataModel_WIP.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblWIP.getSelectedRow());
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }

            try {
                String EXP_PAY_CHQ_RCV_DATE = EITLERPGLOBAL.formatDateDB(DataModel_WIP.getValueByVariable("EXP_PAY_CHQ_RCV_DATE", tblWIP.getSelectedRow() - 1));
                Date exp_pay_date = new SimpleDateFormat("yyyy-MM-dd").parse(EXP_PAY_CHQ_RCV_DATE);
                Date today = new Date();
                if (exp_pay_date.before(today)) {
                    JOptionPane.showMessageDialog(this, "Previous date not allowed.");
                    DataModel_WIP.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblWIP.getSelectedRow() - 1);
                }
                if (exp_pay_date.getDay() != 6) {
                    JOptionPane.showMessageDialog(this, "Please enter SATURDAY date only.");
                    DataModel_WIP.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", "", tblWIP.getSelectedRow() - 1);
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }

        }

        //        if((tblWIP.getSelectedColumn()==14 ) && (evt.getKeyCode()== KeyEvent.VK_TAB ||  evt.getKeyCode()== 10 || evt.getKeyCode()== 37 || evt.getKeyCode()== 38 || evt.getKeyCode()== 39 || evt.getKeyCode()== 40))
        //        {
        //            try{
        //                String SPECIAL_REQ_MONTH_DATE = DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_DATE", tblWIP.getSelectedRow());
        //                Date date=new SimpleDateFormat("dd/MM/yyyy").parse(SPECIAL_REQ_MONTH_DATE);
        //                //System.out.println("output month : "+date.getMonth());
        //                //System.out.println("output year : "+date.getYear());
        //
        //                int month = date.getMonth()+1;
        //                int year = date.getYear()+1900;
        //
        //                String SPECIAL_REQ_MONTH = getMonthName(month) + " - " + year;
        //                DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", SPECIAL_REQ_MONTH, tblWIP.getSelectedRow());
        //            }catch(Exception e)
        //            {
        //                DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", "", tblWIP.getSelectedRow());
        //                e.printStackTrace();
        //            }
        //
        ////            for(int i=0;i<tblWIP.getRowCount();i++)
        ////            {
        ////                    String OC_MONTH  = cmbMonthYear.getSelectedItem().toString();
        ////                    String RESCHEDULE_MONTH = DataModel_WIP.getValueByVariable("RESCHEDULE_MONTH", i);
        ////                    String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
        ////                    String OC_MONTH_exist = DataModel_WIP.getValueByVariable("OC_MONTH", i);
        ////                    String PR_SPL_REQUEST_MONTHYEAR = DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", i);
        ////                    String PR_SPL_REQUEST_DATE = DataModel_WIP.getValueByVariable("PR_SPL_REQUEST_DATE", i);
        ////
        ////                    if(!PR_SPL_REQUEST_MONTHYEAR.equals(OC_MONTH))
        ////                    {
        ////                        HashMap<String, Integer> arrayString = new HashMap<String, Integer>();
        ////
        ////                        if(arrayString.containsKey(PR_SPL_REQUEST_MONTHYEAR))
        ////                        {
        ////                            int numberoftimes = arrayString.get(PR_SPL_REQUEST_MONTHYEAR)+1;
        ////                            arrayString.put(PR_SPL_REQUEST_MONTHYEAR, numberoftimes);
        ////                        }
        ////                        else
        ////                        {
        ////                            arrayString.put(PR_SPL_REQUEST_MONTHYEAR, 1);
        ////                        }
        ////                        for(int j=0;j<arrayString.size();j++)
        ////                        {
        ////                            System.out.println("key "+arrayString.values());
        ////                        }
        ////                        System.out.println("Key "+arrayString.size());
        ////                    }
        ////            }
        //
        //        }
    }//GEN-LAST:event_tblWIPKeyReleased

    private void tblWIPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblWIPKeyPressed
        // TODO add your handling code here:
        if (tblWIP.getSelectedColumn() == DataModel_WIP.getColFromVariable("PR_SPL_REQUEST_DATE")) {
            String OC_MONTH = DataModel_WIP.getValueByVariable("OC_MONTH", tblWIP.getSelectedRow());

            if ("".equals(OC_MONTH)) {
                JOptionPane.showMessageDialog(null, "Special Request Month not allowed when OC MONTH not confirmed!");
                DataModel_WIP.setValueByVariable("PR_SPL_REQUEST_DATE", "", tblWIP.getSelectedRow());
            }
        }
    }//GEN-LAST:event_tblWIPKeyPressed

    private void tblWIPMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblWIPMousePressed
        // TODO add your handling code here:
        if (tblWIP.getSelectedColumn() == 10) {
            if (tblWIP.getValueAt(tblWIP.getSelectedRow(), 1).equals(true)) {
                JOptionPane.showMessageDialog(null, "Reschedule not possible, because OC Month Confirmation selected.");
            }

            String OC_MONTH = DataModel_WIP.getValueByVariable("OC_MONTH", tblWIP.getSelectedRow());
            if (!OC_MONTH.equals("")) {
                JOptionPane.showMessageDialog(null, "Reschedule not possible, because OC Month is is not blank.");
            }
        }
    }//GEN-LAST:event_tblWIPMousePressed

    private void tblWIPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblWIPMouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {
            if (tblWIP.getSelectedColumn() == DataModel_WIP.getColFromVariable("UPN_PIECES")) {
                String UPN_NO = DataModel_WIP.getValueByVariable("UPN_PIECES", tblWIP.getSelectedRow());
                String PR_PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", tblWIP.getSelectedRow());
                searchkey_UPNwise search_check = new searchkey_UPNwise();
                search_check.SQL = "SELECT PR_PIECE_NO as 'PIECE NO',PR_PARTY_CODE as 'PARTY CODE',PARTY_NAME as 'PARTY NAME',PR_MACHINE_NO as 'MAC NO',POSITION_DESC as 'POS DESC',PR_UPN AS 'UPN NO',PR_REQUESTED_MONTH AS 'REQUESTED MONTH','' as 'RESCHEDULE MTH', PR_OC_MONTHYEAR AS 'OC MONTH',PR_PIECE_STAGE AS 'PIECE STAGE', PR_PRODUCT_CODE AS 'PROD CODE',PR_GROUP AS 'PROD GROUP' "
                        + "  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=PR_POSITION_NO AND PR_PARTY_CODE=PARTY_CODE AND PR_UPN='" + UPN_NO + "' AND PR_PIECE_NO!='" + PR_PIECE_NO + "' AND  PR_PIECE_STAGE IN ('PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')";
                search_check.ReturnCol = 1;
                search_check.UPN = UPN_NO;
                search_check.ShowReturnCol = true;
                search_check.DefaultSearchOn = 1;
                search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
                if (search_check.ShowRSLOV()) {

                }
            }

            if (tblWIP.getSelectedColumn() == DataModel_WIP.getColFromVariable("PR_PARTY_CODE")) {
                String PARTY_CODE = DataModel_WIP.getValueByVariable("PR_PARTY_CODE", tblWIP.getSelectedRow());
                searchkey_invoiced_partywise search_check = new searchkey_invoiced_partywise();
                search_check.SQL = "SELECT PR_PIECE_NO AS 'PIECE_NO',PR_UPN AS 'UPN',PR_GROUP AS 'GROUP',PR_OC_MONTHYEAR AS 'OC_MONTH',PR_FNSG_DATE AS 'WAREHOUSE_DATE',PR_INVOICE_DATE  AS 'DISPATCHED_DATE',PR_OC_LAST_DDMMYY FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PARTY_CODE + "' AND PR_PIECE_STAGE='INVOICED' ORDER BY PR_INVOICE_DATE DESC LIMIT 10";
                search_check.ReturnCol = 1;
                search_check.PARTY_CODE = PARTY_CODE;
                search_check.ShowReturnCol = true;
                search_check.DefaultSearchOn = 1;
                search_check.lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE));
                search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
                if (search_check.ShowRSLOV()) {

                }
            }
        }

        if ((LOGIN_USER_TYPE.equals("PPC") || LOGIN_USER_TYPE.equals("OTHER")) && tblWIP.getSelectedColumn() == 1) {
            JOptionPane.showMessageDialog(null, "Only Sales can confirm");
            DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
            return;
        }

        //
        if (tblWIP.getSelectedColumn() == 1) {
            Date date = new Date();
            if (!(date.getDate() >= 15 && date.getDate() <= 25 && LOGIN_USER_TYPE.equals("SALES"))) // On 26/04/2019
            //                    if(!(date.getDate()>=15 && date.getDate()<=26 && LOGIN_USER_TYPE.equals("SALES")))
            {

                //Disable for Testing Only
                //JOptionPane.showMessageDialog(null, "Piece only confirm between 15th to 25th of Month");
                //DataModel_WIP.setValueByVariable("SELECT", false ,tblWIP.getSelectedRow());
                //return;
            }

            String selected_month = cmbMonthYear.getSelectedItem().toString();
            //String allowed_month = addMonth(3, true);

            int current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
            if (rdoSDF.isSelected()) {
                current_allowed_month = EITLERPGLOBAL.getCurrentMonth();
            }
            int current_allowed_year = EITLERPGLOBAL.getCurrentYear();

            if (current_allowed_month > 12) {
                current_allowed_month = current_allowed_month - 12;
                current_allowed_year = 1 + current_allowed_year;
            }

            String allowed_month = getMonthName(current_allowed_month) + " - " + current_allowed_year;
            //System.out.println("allowed Month = "+allowed_month);

            if (!(rdoRequestMonth.isSelected() && selected_month.equals(allowed_month) && LOGIN_USER_TYPE.equals("SALES"))) {
                JOptionPane.showMessageDialog(null, "Piece can not confirm for month " + selected_month);
                DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
                return;
            }

            String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");
            String UPN_NO = DataModel_WIP.getValueByVariable("UPN_PIECES", tblWIP.getSelectedRow());
            System.out.println("UPN " + UPN_NO);
            String Str_query = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL where coalesce(ACESS_QTY,0)<=0 AND UPN=" + UPN_NO + " AND YEAR_FROM='" + EITLERPGLOBAL.getCurrentFinYear() + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'  AND DOC_NO LIKE '_____" + Month + "%' AND APPROVED=1";
            System.out.println("str q2 " + Str_query);
            if (!data.IsRecordExist(Str_query)) {
                JOptionPane.showMessageDialog(null, "Piece can not confirm for month " + selected_month + ", Because Excess Qty available in Sales Projection Review.");
                DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
                return;
            }
        }
        //

        String OC_MONTH_exist = DataModel_WIP.getValueByVariable("OC_MONTH", tblWIP.getSelectedRow());
        String RESCHEDULE_MONTH_exist = DataModel_WIP.getValueByVariable("RESCHEDULE_MONTH", tblWIP.getSelectedRow());

        if (!"".equals(RESCHEDULE_MONTH_exist) && tblWIP.getSelectedColumn() == 1) {
            JOptionPane.showMessageDialog(null, "Cannot Confirm");
            DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
            return;
        }

        if (!"".equals(OC_MONTH_exist) && tblWIP.getSelectedColumn() == 1) {
            JOptionPane.showMessageDialog(null, "OC MONTH already exist ");
            DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
            return;
        }

        if (tblWIP.getSelectedColumn() == 1) {
            String PR_PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", tblWIP.getSelectedRow());
            String OC_DOC_NO = data.getStringValueFromDB("SELECT H.OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D,PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H "
                    + " WHERE H.OC_DOC_NO=D.OC_DOC_NO AND D.PIECE_NO='" + PR_PIECE_NO + "' AND H.APPROVED=0;");
            if (!"".equals(OC_DOC_NO)) {

                JOptionPane.showMessageDialog(null, "This Piece already pending in DOC NO " + OC_DOC_NO);
                DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
                return;
            }

            String CONTACT_PERSON = DataModel_WIP.getValueByVariable("CONTACT_PERSON", tblWIP.getSelectedRow());
            String EMAIL_ID = DataModel_WIP.getValueByVariable("EMAIL_ID", tblWIP.getSelectedRow());

            if ("".equals(CONTACT_PERSON)) {
                DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
                JOptionPane.showMessageDialog(this, "Contact person is compulsory to confirm Piece.");
                tblWIP.requestFocus();
                tblWIP.changeSelection(tblWIP.getSelectedRow(), DataModel_WIP.getColFromVariable("CONTACT_PERSON"), false, false);
                return;
            }

            if ("".equals(EMAIL_ID)) {
                DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
                JOptionPane.showMessageDialog(this, "Email id is compulsory to confirm Piece.");
                tblWIP.requestFocus();
                tblWIP.changeSelection(tblWIP.getSelectedRow(), DataModel_WIP.getColFromVariable("EMAIL_ID"), false, false);
                return;
            }

            String FELT_TYPE = "";
            if (rdoExceptSDF.isSelected()) {
                FELT_TYPE = "All(Except SDF)";
            } else {
                FELT_TYPE = "SDF";
            }

            String APPROVED_DOC = data.getStringValueFromDB("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='" + cmbMonthYear.getSelectedItem().toString() + "' AND FELT_TYPE='" + FELT_TYPE + "' AND APPROVED=1");
            System.out.println("validation WIP : SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='" + cmbMonthYear.getSelectedItem().toString() + "' AND FELT_TYPE='" + FELT_TYPE + "' AND APPROVED=1");
            if (!APPROVED_DOC.equals("")) {
                //10/10/2021
                // DataModel_WIP.setValueByVariable("SELECT", false ,tblWIP.getSelectedRow());
                // JOptionPane.showMessageDialog(this, ""+cmbMonthYear.getSelectedItem().toString()+" is closed, because Order Confirmation Document is Approved.");
                // return;
            }

            String DOC_NO = data.getStringValueFromDB("SELECT OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='" + cmbMonthYear.getSelectedItem().toString() + "' AND FELT_TYPE='" + FELT_TYPE + "' ");
            System.out.println("check month data : SELECT OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='" + cmbMonthYear.getSelectedItem().toString() + "' AND FELT_TYPE='" + FELT_TYPE + "'");
            String PROD_DOC_DATA_NO = data.getStringValueFromDB("SELECT DOC_NO FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE USER_ID='28' AND STATUS='W' AND DOC_NO='" + DOC_NO + "'");
            System.out.println("SELECT DOC_NO FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE USER_ID='28' AND STATUS='W' AND DOC_NO='" + DOC_NO + "'");
            if (!DOC_NO.equals("") && PROD_DOC_DATA_NO.equals("")) {
                // DataModel_WIP.setValueByVariable("SELECT", false ,tblWIP.getSelectedRow());
                // JOptionPane.showMessageDialog(this, "For month : "+cmbMonthYear.getSelectedItem().toString()+" is not allowed to confirm, Document already forwarded to PPC.");
                // return;
            }

            String UPN = DataModel_WIP.getValueByVariable("UPN_PIECES", tblWIP.getSelectedRow());
            String PR_REQUESTED_MONTH = DataModel_WIP.getValueByVariable("PR_REQUESTED_MONTH", tblWIP.getSelectedRow());
            String SPILOVER_PIECE = data.getStringValueFromDB("SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE (PR_SP_MONTHYEAR!='0' OR PR_SP_MONTHYEAR IS NOT NULL OR PR_SP_MONTHYEAR!='') AND PR_OC_MONTHYEAR!=PR_CURRENT_SCH_MONTH AND PR_REQUESTED_MONTH='" + PR_REQUESTED_MONTH + "' AND PR_UPN='" + UPN + "' AND PR_PIECE_NO!='" + PR_PIECE_NO + "' AND PR_PIECE_STAGE NOT IN ('CANCELLED','DIVERTED','DIVIDED','INVOICED')");
            System.out.println("SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE (PR_SP_MONTHYEAR!='0' OR PR_SP_MONTHYEAR IS NOT NULL OR PR_SP_MONTHYEAR!='') AND PR_OC_MONTHYEAR!=PR_CURRENT_SCH_MONTH AND PR_REQUESTED_MONTH='" + PR_REQUESTED_MONTH + "' AND PR_UPN='" + UPN + "' AND PR_PIECE_NO!='" + PR_PIECE_NO + "' AND PR_PIECE_STAGE NOT IN ('CANCELLED','DIVERTED','DIVIDED','INVOICED')");
            if (!"".equals(SPILOVER_PIECE)) {
                int n = JOptionPane.showConfirmDialog(this, "This UPN " + UPN + " has spilover piece " + SPILOVER_PIECE + ", Do you want to continue?", "Attention.. Please Confirm", JOptionPane.YES_NO_OPTION);
                if (n == 0) {

                } else {
                    DataModel_WIP.setValueByVariable("SELECT", false, tblWIP.getSelectedRow());
                    return;
                }
            }

        }
        //GenerateProdMfgReport();
    }//GEN-LAST:event_tblWIPMouseClicked

    private void cmbMonthYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonthYearItemStateChanged
        // TODO add your handling code here:
        setPlanningPieces();
        setWIPPieces();
        setSTOCKPieces();
        setDispatchPieces();
        //GenerateProdMfgReport();
    }//GEN-LAST:event_cmbMonthYearItemStateChanged

    private void tblPlanningKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPlanningKeyReleased
        // TODO add your handling code here:
        /*
         if(evt.getKeyCode()== KeyEvent.VK_TAB || evt.getKeyCode()== 10 || evt.getKeyCode()== 37 || evt.getKeyCode()== 38 || evt.getKeyCode()== 39 || evt.getKeyCode()== 40)
         {
         try{
         String SPECIAL_REQ_MONTH_DATE = DataModel_Planning.getValueByVariable("PR_SPL_REQUEST_DATE", tblPlanning.getSelectedRow());
         Date date=new SimpleDateFormat("dd/MM/yyyy").parse(SPECIAL_REQ_MONTH_DATE);
         //System.out.println("output month : "+date.getMonth());
         //System.out.println("output year : "+date.getYear());

         int month = date.getMonth()+1;
         int year = date.getYear()+1900;

         String SPECIAL_REQ_MONTH = getMonthName(month) + " - " + year;
         DataModel_Planning.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", SPECIAL_REQ_MONTH, tblPlanning.getSelectedRow());
         }catch(Exception e)
         {
         e.printStackTrace();
         }

         }*/
    }//GEN-LAST:event_tblPlanningKeyReleased

    private void tblPlanningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPlanningKeyPressed
        // TODO add your handling code here:
        if (tblPlanning.getSelectedColumn() == DataModel_Planning.getColFromVariable("PR_SPL_REQUEST_DATE")) {
            String OC_MONTH = DataModel_Planning.getValueByVariable("OC_MONTH", tblPlanning.getSelectedRow());

            if ("".equals(OC_MONTH)) {
                JOptionPane.showMessageDialog(null, "Special Request Month not allowed when OC MONTH not Confirmed!");
                DataModel_Planning.setValueByVariable("PR_SPL_REQUEST_DATE", "", tblPlanning.getSelectedRow());
            }

        }
    }//GEN-LAST:event_tblPlanningKeyPressed

    private void tblPlanningMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlanningMousePressed
        // TODO add your handling code here:.

        if (tblPlanning.getSelectedColumn() == 10) {
            String OC_MONTH = DataModel_Planning.getValueByVariable("OC_MONTH", tblPlanning.getSelectedRow());
            if (tblPlanning.getValueAt(tblPlanning.getSelectedRow(), 1).equals(true)) {
                JOptionPane.showMessageDialog(null, "Reschedule not possible, because OC Month Confirmation selected.");
                tblPlanning.setValueAt("", tblPlanning.getSelectedRow(), 10);
            }

            if (!OC_MONTH.equals("")) {
                JOptionPane.showMessageDialog(null, "Reschedule not possible, because OC Month is is not blank.");
                tblPlanning.setValueAt("", tblPlanning.getSelectedRow(), 10);
            }
        }

    }//GEN-LAST:event_tblPlanningMousePressed

    private void tblPlanningMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlanningMouseClicked
        // TODO add your handling code here:

        if ((LOGIN_USER_TYPE.equals("PPC") || LOGIN_USER_TYPE.equals("OTHER")) && tblPlanning.getSelectedColumn() == 1) {
            JOptionPane.showMessageDialog(null, "Only Sales can confirm");
            DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
            return;
        }

        if (tblPlanning.isEnabled()) {
            if (evt.getClickCount() == 2) {
                if (tblPlanning.getSelectedColumn() == DataModel_Planning.getColFromVariable("UPN_PIECES")) {
                    String UPN_NO = DataModel_Planning.getValueByVariable("UPN_PIECES", tblPlanning.getSelectedRow());
                    String PR_PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", tblPlanning.getSelectedRow());
                    searchkey_UPNwise search_check = new searchkey_UPNwise();
                    search_check.SQL = "SELECT PR_PIECE_NO as 'PIECE NO',PR_PARTY_CODE as 'PARTY CODE',PARTY_NAME as 'PARTY NAME',PR_MACHINE_NO as 'MAC NO',POSITION_DESC as 'POS DESC',PR_UPN AS 'UPN NO',PR_REQUESTED_MONTH AS 'REQUESTED MONTH','' as 'RESCHEDULE MTH', PR_OC_MONTHYEAR AS 'OC MONTH',PR_PIECE_STAGE AS 'PIECE STAGE', PR_PRODUCT_CODE AS 'PROD CODE',PR_GROUP AS 'PROD GROUP' "
                            + "  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=PR_POSITION_NO AND PR_PARTY_CODE=PARTY_CODE AND PR_UPN='" + UPN_NO + "' AND PR_PIECE_NO != '" + PR_PIECE_NO + "' AND PR_PIECE_STAGE IN ('BOOKING','PLANNING','SEAMING','SPLICING','WEAVING','NEEDLING','MENDING','FINISHING','IN STOCK','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')";
                    search_check.ReturnCol = 1;
                    search_check.UPN = UPN_NO;
                    search_check.ShowReturnCol = true;
                    search_check.DefaultSearchOn = 1;

                    search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
                    if (search_check.ShowRSLOV()) {

                    }
                }

                if (tblPlanning.getSelectedColumn() == DataModel_Planning.getColFromVariable("PR_PARTY_CODE")) {
                    String PARTY_CODE = DataModel_Planning.getValueByVariable("PR_PARTY_CODE", tblPlanning.getSelectedRow());
                    searchkey_invoiced_partywise search_check = new searchkey_invoiced_partywise();
                    search_check.SQL = "SELECT PR_PIECE_NO AS 'PIECE_NO',PR_UPN AS 'UPN',PR_GROUP AS 'GROUP',PR_OC_MONTHYEAR AS 'OC_MONTH',PR_FNSG_DATE AS 'WAREHOUSE_DATE',PR_INVOICE_DATE  AS 'DISPATCHED_DATE',PR_OC_LAST_DDMMYY FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PARTY_CODE + "' AND PR_PIECE_STAGE='INVOICED' ORDER BY PR_INVOICE_DATE DESC LIMIT 10";
                    search_check.ReturnCol = 1;
                    search_check.PARTY_CODE = PARTY_CODE;
                    search_check.ShowReturnCol = true;
                    search_check.DefaultSearchOn = 1;
                    search_check.lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PARTY_CODE));
                    search_check.SELECTED_MONTH = cmbMonthYear.getSelectedItem().toString();
                    if (search_check.ShowRSLOV()) {

                    }
                }
            }
        }

        if (tblPlanning.getSelectedColumn() == 1) {
            Date date = new Date();

            //04/09/2021
            //                    if(!(date.getDate()>=15 && date.getDate()<=25  && LOGIN_USER_TYPE.equals("SALES"))) // On 26/04/2019
            //                    {
            //                        JOptionPane.showMessageDialog(null, "Piece only confirm between 15th to 25th of Month");
            //                        DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
            //                        return;
            //                    }
            //04/09/2021
            if (rdoSDF.isSelected()) {
                //if(!(date.getDate()>=15 && date.getDate()<=25  && LOGIN_USER_TYPE.equals("SALES"))) // On 26/04/2019
                if ((date.getDate() >= 11 && date.getDate() <= 14) && !LOGIN_USER_TYPE.equals("SALES")) // On 26/04/2019
                {
                    JOptionPane.showMessageDialog(null, "For SDF Piece only confirm between before 10 of current month");
                    DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
                    return;
                }
            } else {
                //if(!(date.getDate()>=11 && date.getDate()<=14)  && LOGIN_USER_TYPE.equals("SALES")) // On 26/04/2019
                //{
                //    JOptionPane.showMessageDialog(null, "For Non SDF Piece only confirm between 15th to 10th of Next Month");
                //    DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
                //   return;
                //}
            }

            String selected_month = cmbMonthYear.getSelectedItem().toString();
            //10/08/2021
            int current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 2;

            if (rdoSDF.isSelected()) {
                current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
            }
            //04/09/2021
            if (EITLERPGLOBAL.getCurrentDay() <= 10) {
                current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                if (rdoSDF.isSelected()) {
                    current_allowed_month = EITLERPGLOBAL.getCurrentMonth();
                }
            }

            int current_allowed_year = EITLERPGLOBAL.getCurrentYear();

            if (current_allowed_month > 12) {
                current_allowed_month = current_allowed_month - 12;
                current_allowed_year = 1 + current_allowed_year;
            }

            String allowed_month = getMonthName(current_allowed_month) + " - " + current_allowed_year;
            //System.out.println("allowed Month = "+allowed_month);

            if (!(rdoRequestMonth.isSelected() && selected_month.equals(allowed_month) && LOGIN_USER_TYPE.equals("SALES"))) {
                //29 April 2019
                JOptionPane.showMessageDialog(null, "Piece can not confirm for month " + selected_month);
                DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
                return;
            }

            /*
             String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");
             String UPN_NO = DataModel_Planning.getValueByVariable("UPN_PIECES", tblPlanning.getSelectedRow());
             String Str_query = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL where coalesce(ACESS_QTY,0)<=0 AND UPN="+UPN_NO+" AND YEAR_FROM='" + EITLERPGLOBAL.getCurrentFinYear() + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'  AND DOC_NO LIKE '_____"+Month+"%' AND APPROVED=1";
             System.out.println("str _____ "+Str_query);
             if (!data.IsRecordExist(Str_query)) {
             JOptionPane.showMessageDialog(null, "Piece can not confirm for month "+selected_month+", Because Excess Qty available in Sales Projection Review.");
             DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
             return;
             }
             */
            String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");

            String from_year = (EITLERPGLOBAL.getCurrentFinYear() + "").substring(2);
            String to_year = ((EITLERPGLOBAL.getCurrentFinYear() + 1) + "").substring(2);
            String PieceNo = DataModel_Planning.getValueByVariable("PR_PIECE_NO", tblPlanning.getSelectedRow());
            String str_query = "SELECT * FROM PRODUCTION.FELT_SALES_PROJECTION_CP_NCP_EXCESS WHERE CNE_PIECE_NO='" + PieceNo + "' AND CNE_DOC_NO LIKE '_" + from_year + "" + to_year + "" + Month + "%'";
            System.out.println("Q1 : " + str_query);
            if (data.IsRecordExist(str_query)) {
                JOptionPane.showMessageDialog(null, "Piece can not confirm for month " + selected_month + ", Because Excess Qty available in Sales Projection Review.");
                DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
                return;
            }

        }

        String OC_MONTH_exist = DataModel_Planning.getValueByVariable("OC_MONTH", tblPlanning.getSelectedRow());
        String RESCHEDULE_MONTH_exist = DataModel_Planning.getValueByVariable("RESCHEDULE_MONTH", tblPlanning.getSelectedRow());

        if (!"".equals(OC_MONTH_exist) && (tblPlanning.getSelectedColumn() == 10 || tblPlanning.getSelectedColumn() == 1)) {
            JOptionPane.showMessageDialog(null, "OC MONTH already exist ");
            DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
            return;
        }

        if (tblPlanning.getSelectedColumn() == 1) {

            if (!"".equals(RESCHEDULE_MONTH_exist) && tblPlanning.getSelectedColumn() == 1) {
                JOptionPane.showMessageDialog(null, "Cannot Confirm, if reschedule selected.");
                DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
                return;
            }

            String PR_PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", tblPlanning.getSelectedRow());
            String OC_DOC_NO = data.getStringValueFromDB("SELECT H.OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D,PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H "
                    + " WHERE H.OC_DOC_NO=D.OC_DOC_NO AND D.PIECE_NO='" + PR_PIECE_NO + "' AND H.APPROVED=0");
            System.out.println("getPIECE = SELECT H.OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D,PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H "
                    + " WHERE H.OC_DOC_NO=D.OC_DOC_NO AND D.PIECE_NO='" + PR_PIECE_NO + "' AND H.APPROVED=0");
            if (!"".equals(OC_DOC_NO)) {
                JOptionPane.showMessageDialog(null, "This Piece already pending in DOC NO " + OC_DOC_NO);
                DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
                return;
            }

            String CONTACT_PERSON = DataModel_Planning.getValueByVariable("CONTACT_PERSON", tblPlanning.getSelectedRow());
            String EMAIL_ID = DataModel_Planning.getValueByVariable("EMAIL_ID", tblPlanning.getSelectedRow());

            if ("".equals(CONTACT_PERSON)) {
                DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
                JOptionPane.showMessageDialog(this, "Contact person is compulsory to confirm Piece.");
                tblPlanning.requestFocus();
                tblPlanning.changeSelection(tblPlanning.getSelectedRow(), DataModel_Planning.getColFromVariable("CONTACT_PERSON"), false, false);
                return;
            }

            if ("".equals(EMAIL_ID)) {
                DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
                JOptionPane.showMessageDialog(this, "Email id is compulsory to confirm Piece.");
                tblPlanning.requestFocus();
                tblPlanning.changeSelection(tblPlanning.getSelectedRow(), DataModel_Planning.getColFromVariable("EMAIL_ID"), false, false);
                return;
            }

            String FELT_TYPE = "";
            if (rdoExceptSDF.isSelected()) {
                FELT_TYPE = "All(Except SDF)";
            } else {
                FELT_TYPE = "SDF";
            }

            //04/09/2021
            //            String APPROVED_DOC = data.getStringValueFromDB("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='"+cmbMonthYear.getSelectedItem().toString()+"' AND FELT_TYPE='"+FELT_TYPE+"' AND APPROVED=1");
            //            System.out.println("validation planning : SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='"+cmbMonthYear.getSelectedItem().toString()+"' AND FELT_TYPE='"+FELT_TYPE+"' AND APPROVED=1");
            //            if(!APPROVED_DOC.equals(""))
            //            {
            //                DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
            //                JOptionPane.showMessageDialog(this, ""+cmbMonthYear.getSelectedItem().toString()+" is closed, because Order Confirmation Document is Approved.");
            //                return;
            //            }
            String DOC_NO = data.getStringValueFromDB("SELECT OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='" + cmbMonthYear.getSelectedItem().toString() + "'  AND FELT_TYPE='" + FELT_TYPE + "'");

            String PROD_DOC_DATA_NO = data.getStringValueFromDB("SELECT DOC_NO FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE USER_ID='28' AND STATUS='W' AND DOC_NO='" + DOC_NO + "'");

            if (!DOC_NO.equals("") && PROD_DOC_DATA_NO.equals("")) {
                //10/10/2021
                //DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
                //JOptionPane.showMessageDialog(this, "For month : "+cmbMonthYear.getSelectedItem().toString()+" is not allowed to confirm, Document already forwarded to PPC.");
                //return;
            }

            String UPN = DataModel_Planning.getValueByVariable("UPN_PIECES", tblPlanning.getSelectedRow());
            String PR_REQUESTED_MONTH = DataModel_Planning.getValueByVariable("PR_REQUESTED_MONTH", tblPlanning.getSelectedRow());
            String SPILOVER_PIECE = data.getStringValueFromDB("SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE (PR_SP_MONTHYEAR!='0' OR PR_SP_MONTHYEAR IS NOT NULL OR PR_SP_MONTHYEAR!='') AND PR_OC_MONTHYEAR!=PR_CURRENT_SCH_MONTH AND PR_REQUESTED_MONTH='" + PR_REQUESTED_MONTH + "' AND PR_UPN='" + UPN + "' AND PR_PIECE_NO!='" + PR_PIECE_NO + "' AND PR_PIECE_STAGE NOT IN ('CANCELLED','DIVERTED','DIVIDED','INVOICED')");

            if (!"".equals(SPILOVER_PIECE)) {
                int n = JOptionPane.showConfirmDialog(this, "This UPN " + UPN + " has spilover piece " + SPILOVER_PIECE + ", Do you want to continue?", "Attention.. Please Confirm", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                } else {

                    DataModel_Planning.setValueByVariable("SELECT", false, tblPlanning.getSelectedRow());
                    return;
                }
            }
        }
        //GenerateProdMfgReport();
    }//GEN-LAST:event_tblPlanningMouseClicked

    private void tblPlanning1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlanning1MouseClicked
        // TODO add your handling code here:

        if (!LOGIN_USER_TYPE.equals("DESIGN") && tblPlanning1.getSelectedColumn() == 1) {
            JOptionPane.showMessageDialog(null, "Only Design can confirm");
            DataModel_PPRS_Planning.setValueByVariable("SELECT", false, tblPlanning1.getSelectedRow());
            return;
        }

        if (tblPlanning1.getSelectedColumn() == 1) {
            Date date = new Date();

            //04/09/2021
            //                    if(!(date.getDate()>=15 && date.getDate()<=25  && LOGIN_USER_TYPE.equals("SALES"))) // On 26/04/2019
            //                    {
            //                        JOptionPane.showMessageDialog(null, "Piece only confirm between 15th to 25th of Month");
            //                        DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
            //                        return;
            //                    }
            //04/09/2021
            if (rdoSDF1.isSelected()) {
                //if(!(date.getDate()>=15 && date.getDate()<=25  && LOGIN_USER_TYPE.equals("SALES"))) // On 26/04/2019
                if ((date.getDate() >= 11 && date.getDate() <= 15) && !LOGIN_USER_TYPE.equals("DESIGN")) // On 26/04/2019
                {
                    JOptionPane.showMessageDialog(null, "For SDF Piece only confirm between before 10 of current month");
                    DataModel_PPRS_Planning.setValueByVariable("SELECT", false, tblPlanning1.getSelectedRow());
                    return;
                }
            } else {
                //if(!(date.getDate()>=11 && date.getDate()<=14)  && LOGIN_USER_TYPE.equals("SALES")) // On 26/04/2019
                //{
                //    JOptionPane.showMessageDialog(null, "For Non SDF Piece only confirm between 15th to 10th of Next Month");
                //    DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
                //   return;
                //}
            }

            String selected_month = cmbMonthYear1.getSelectedItem().toString();
            //10/08/2021
            int current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 2;

            if (rdoSDF1.isSelected()) {
                current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
            }
            //04/09/2021
            if (EITLERPGLOBAL.getCurrentDay() <= 10) {
                current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                if (rdoSDF1.isSelected()) {
                    current_allowed_month = EITLERPGLOBAL.getCurrentMonth();
                }
            }

            int current_allowed_year = EITLERPGLOBAL.getCurrentYear();

            if (current_allowed_month > 12) {
                current_allowed_month = current_allowed_month - 12;
                current_allowed_year = 1 + current_allowed_year;
            }

            String allowed_month = getMonthName(current_allowed_month) + " - " + current_allowed_year;
            //System.out.println("allowed Month = "+allowed_month);

//            if(!(rdoRequestMonth.isSelected() && selected_month.equals(allowed_month) && LOGIN_USER_TYPE.equals("SALES")))
//            {
//                //29 April 2019
//                JOptionPane.showMessageDialog(null, "Piece can not confirm for month "+selected_month);
//                DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
//                return;
//            }

            /*
             String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");
             String UPN_NO = DataModel_Planning.getValueByVariable("UPN_PIECES", tblPlanning.getSelectedRow());
             String Str_query = "SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL where coalesce(ACESS_QTY,0)<=0 AND UPN="+UPN_NO+" AND YEAR_FROM='" + EITLERPGLOBAL.getCurrentFinYear() + "' AND YEAR_TO='" + (EITLERPGLOBAL.getCurrentFinYear() + 1) + "'  AND DOC_NO LIKE '_____"+Month+"%' AND APPROVED=1";
             System.out.println("str _____ "+Str_query);
             if (!data.IsRecordExist(Str_query)) {
             JOptionPane.showMessageDialog(null, "Piece can not confirm for month "+selected_month+", Because Excess Qty available in Sales Projection Review.");
             DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
             return;
             }
             */
            String Month = data.getStringValueFromDB("SELECT LPAD(MONTH(CURDATE()),2,0) FROM DUAL");

            String from_year = (EITLERPGLOBAL.getCurrentFinYear() + "").substring(2);
            String to_year = ((EITLERPGLOBAL.getCurrentFinYear() + 1) + "").substring(2);
            String PieceNo = DataModel_PPRS_Planning.getValueByVariable("PR_PIECE_NO", tblPlanning1.getSelectedRow());
            String str_query = "SELECT * FROM PRODUCTION.FELT_SALES_PROJECTION_CP_NCP_EXCESS WHERE CNE_PIECE_NO='" + PieceNo + "' AND CNE_DOC_NO LIKE '_" + from_year + "" + to_year + "" + Month + "%'";
            System.out.println("Q1 : " + str_query);
            if (data.IsRecordExist(str_query)) {
                JOptionPane.showMessageDialog(null, "Piece can not confirm for month " + selected_month + ", Because Excess Qty available in Sales Projection Review.");
                DataModel_PPRS_Planning.setValueByVariable("SELECT", false, tblPlanning1.getSelectedRow());
                return;
            }

        }

        String OC_MONTH_exist = DataModel_PPRS_Planning.getValueByVariable("OC_MONTH", tblPlanning1.getSelectedRow());
        String RESCHEDULE_MONTH_exist = DataModel_PPRS_Planning.getValueByVariable("RESCHEDULE_MONTH", tblPlanning1.getSelectedRow());

        if (!"".equals(OC_MONTH_exist) && (tblPlanning1.getSelectedColumn() == 10 || tblPlanning1.getSelectedColumn() == 1)) {
            JOptionPane.showMessageDialog(null, "OC MONTH already exist ");
            DataModel_PPRS_Planning.setValueByVariable("SELECT", false, tblPlanning1.getSelectedRow());
            return;
        }

        if (tblPlanning1.getSelectedColumn() == 1) {

            if (!"".equals(RESCHEDULE_MONTH_exist) && tblPlanning1.getSelectedColumn() == 1) {
                JOptionPane.showMessageDialog(null, "Cannot Confirm, if reschedule selected.");
                DataModel_PPRS_Planning.setValueByVariable("SELECT", false, tblPlanning1.getSelectedRow());
                return;
            }

            String PR_PIECE_NO = DataModel_PPRS_Planning.getValueByVariable("PR_PIECE_NO", tblPlanning1.getSelectedRow());
            String OC_DOC_NO = data.getStringValueFromDB("SELECT H.OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D,PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H "
                    + " WHERE H.OC_DOC_NO=D.OC_DOC_NO AND D.PIECE_NO='" + PR_PIECE_NO + "' AND H.APPROVED=0");
            System.out.println("getPIECE = SELECT H.OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_DETAIL D,PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER H "
                    + " WHERE H.OC_DOC_NO=D.OC_DOC_NO AND D.PIECE_NO='" + PR_PIECE_NO + "' AND H.APPROVED=0");
            if (!"".equals(OC_DOC_NO)) {
                JOptionPane.showMessageDialog(null, "This Piece already pending in DOC NO " + OC_DOC_NO);
                DataModel_PPRS_Planning.setValueByVariable("SELECT", false, tblPlanning1.getSelectedRow());
                return;
            }

//            String CONTACT_PERSON = DataModel_PPRS_Planning.getValueByVariable("CONTACT_PERSON", tblPlanning1.getSelectedRow());
//            String EMAIL_ID = DataModel_PPRS_Planning.getValueByVariable("EMAIL_ID", tblPlanning1.getSelectedRow());
//
//            if("".equals(CONTACT_PERSON))
//            {
//                DataModel_PPRS_Planning.setValueByVariable("SELECT", false ,tblPlanning1.getSelectedRow());
//                JOptionPane.showMessageDialog(this, "Contact person is compulsory to confirm Piece.");
//                tblPlanning1.requestFocus();
//                tblPlanning1.changeSelection(tblPlanning1.getSelectedRow(), DataModel_PPRS_Planning.getColFromVariable("CONTACT_PERSON"), false, false);
//                return;
//            }
//
//            if("".equals(EMAIL_ID))
//            {
//                DataModel_PPRS_Planning.setValueByVariable("SELECT", false ,tblPlanning1.getSelectedRow());
//                JOptionPane.showMessageDialog(this, "Email id is compulsory to confirm Piece.");
//                tblPlanning1.requestFocus();
//                tblPlanning1.changeSelection(tblPlanning1.getSelectedRow(), DataModel_PPRS_Planning.getColFromVariable("EMAIL_ID"), false, false);
//                return;
//            }
            String FELT_TYPE = "";
            if (rdoExceptSDF1.isSelected()) {
                FELT_TYPE = "All(Except SDF)";
            } else {
                FELT_TYPE = "SDF";
            }

            //04/09/2021
            //            String APPROVED_DOC = data.getStringValueFromDB("SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='"+cmbMonthYear.getSelectedItem().toString()+"' AND FELT_TYPE='"+FELT_TYPE+"' AND APPROVED=1");
            //            System.out.println("validation planning : SELECT * FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='"+cmbMonthYear.getSelectedItem().toString()+"' AND FELT_TYPE='"+FELT_TYPE+"' AND APPROVED=1");
            //            if(!APPROVED_DOC.equals(""))
            //            {
            //                DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
            //                JOptionPane.showMessageDialog(this, ""+cmbMonthYear.getSelectedItem().toString()+" is closed, because Order Confirmation Document is Approved.");
            //                return;
            //            }
            String DOC_NO = data.getStringValueFromDB("SELECT OC_DOC_NO FROM PRODUCTION.FELT_SALES_ORDER_CONFIRMATION_HEADER WHERE OC_MONTH='" + cmbMonthYear1.getSelectedItem().toString() + "'  AND FELT_TYPE='" + FELT_TYPE + "'");

            String PROD_DOC_DATA_NO = data.getStringValueFromDB("SELECT DOC_NO FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE USER_ID='28' AND STATUS='W' AND DOC_NO='" + DOC_NO + "'");

            if (!DOC_NO.equals("") && PROD_DOC_DATA_NO.equals("")) {
                //10/10/2021
                //DataModel_Planning.setValueByVariable("SELECT", false ,tblPlanning.getSelectedRow());
                //JOptionPane.showMessageDialog(this, "For month : "+cmbMonthYear.getSelectedItem().toString()+" is not allowed to confirm, Document already forwarded to PPC.");
                //return;
            }

            String UPN = DataModel_PPRS_Planning.getValueByVariable("UPN_PIECES", tblPlanning1.getSelectedRow());
            String PR_REQUESTED_MONTH = DataModel_PPRS_Planning.getValueByVariable("PR_REQUESTED_MONTH", tblPlanning1.getSelectedRow());
            String SPILOVER_PIECE = data.getStringValueFromDB("SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE (PR_SP_MONTHYEAR!='0' OR PR_SP_MONTHYEAR IS NOT NULL OR PR_SP_MONTHYEAR!='') AND PR_OC_MONTHYEAR!=PR_CURRENT_SCH_MONTH AND PR_REQUESTED_MONTH='" + PR_REQUESTED_MONTH + "' AND PR_UPN='" + UPN + "' AND PR_PIECE_NO!='" + PR_PIECE_NO + "' AND PR_PIECE_STAGE NOT IN ('CANCELLED','DIVERTED','DIVIDED','INVOICED')");

            if (!"".equals(SPILOVER_PIECE)) {
                int n = JOptionPane.showConfirmDialog(this, "This UPN " + UPN + " has spilover piece " + SPILOVER_PIECE + ", Do you want to continue?", "Attention.. Please Confirm", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                } else {
                    DataModel_PPRS_Planning.setValueByVariable("SELECT", false, tblPlanning1.getSelectedRow());
                    return;
                }
            }
        }

    }//GEN-LAST:event_tblPlanning1MouseClicked

    private void tblPlanning1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlanning1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlanning1MousePressed

    private void tblPlanning1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPlanning1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlanning1KeyPressed

    private void tblPlanning1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPlanning1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPlanning1KeyReleased

    private void cmbMonthYear1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonthYear1ItemStateChanged
        // TODO add your handling code here:
        setPPRSPlanningPieces();
    }//GEN-LAST:event_cmbMonthYear1ItemStateChanged

    private void cmbZone1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbZone1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbZone1ItemStateChanged

    private void cmbProdGroup1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProdGroup1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbProdGroup1ItemStateChanged

    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
        // TODO add your handling code here:
        int USER_ID = EITLERPGLOBAL.gUserID;
        int DEPT_ID = EITLERPGLOBAL.gUserDeptID;
        //System.out.println("USER = "+USER_ID+" DEPT ID = "+DEPT_ID);
        if (DEPT_ID == 39) {
            //DESIGN LOGIN

            Date date = new Date();
                    //Disable for Testing Only
            //29 April 2019
            //10/10/2021
            //if(date.getDate()>=15 && date.getDate()<=25 && LOGIN_USER_TYPE.equals("SALES")) // On 26/04/2019
            if (((date.getDate() >= 11 && date.getDate() <= 16) && LOGIN_USER_TYPE.equals("DESIGN"))) //            if(date.getDate()>=15 && date.getDate()<=26 && LOGIN_USER_TYPE.equals("SALES"))
            {
                try {
                    HashMap ObjPPRS = new HashMap();
                    int size = 0;
                            //ObjItem.frm_S_O_No = S_O_NO.getText();

                    //ObjItem.DataSettingFromOrder();
                    for (int i = 0; i < tblPlanning1.getRowCount(); i++) {

                        if (tblPlanning1.getValueAt(i, 1).equals(true)) {
                            String PPRS_MONTH = cmbMonthYear1.getSelectedItem().toString();
                            String PIECE_NO = DataModel_PPRS_Planning.getValueByVariable("PR_PIECE_NO", i);

                            String OC_MONTH_exist = DataModel_PPRS_Planning.getValueByVariable("OC_MONTH", i);

                            if (!"".equals(OC_MONTH_exist)) {
                                JOptionPane.showMessageDialog(null, "OC MONTH already exist for Piece " + PIECE_NO);
                                return;
                            }

                            clsPPRSEntryDetails objDetailPPRS = new clsPPRSEntryDetails();
                            objDetailPPRS.setAttribute("PIECE_NO", PIECE_NO);
                            objDetailPPRS.setAttribute("PPRS_MONTH", PPRS_MONTH);
                            ObjPPRS.put(size++, objDetailPPRS);

                        }
                    }

                    if (!ObjPPRS.isEmpty()) {
                        String FELT_TYPE = "";
                        if (rdoExceptSDF1.isSelected()) {
                            FELT_TYPE = "All(Except SDF)";
                        } else {
                            FELT_TYPE = "SDF";
                        }

                        String PPRS_MONTH = cmbMonthYear1.getSelectedItem().toString();

                        AppletFrame aFrame = new AppletFrame("Felt PPRS Planning");
                        aFrame.startAppletEx("EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry", "Felt PPRS Planning");
                        FrmPPRSEntry ObjItemPPRS = (FrmPPRSEntry) aFrame.ObjApplet;

                        ObjItemPPRS.objFromMainFormPPRS = ObjPPRS;

                        //It will call only if OC_MONTH data not found in OC Month Confirmation Form.
                        ObjItemPPRS.FillPiece();
                                    //if(EITLERPGLOBAL.gUserID == 136)
                        //{
                        ObjItemPPRS.PPRS_MONTH = PPRS_MONTH;
                        ObjItemPPRS.FELT_TYPE = FELT_TYPE;
                        ObjItemPPRS.SelHierarchyID = 5220;
                        ObjItemPPRS.Save();
                        //}
                        
                        JOptionPane.showMessageDialog(this, "PPRS DATA UPDATED..!");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            //OTHER USER LOGIN
            disableAllTable();
        }
//        JOptionPane.showMessageDialog(this, "PPRS DATA UPDATED..!");
        setPPRSPlanningPieces();
    }//GEN-LAST:event_btnSave1ActionPerformed

    private void btnShowData1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowData1ActionPerformed
        // TODO add your handling code here:
        setPPRSPlanningPieces();
    }//GEN-LAST:event_btnShowData1ActionPerformed

    private void txtPartyCode1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCode1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCode1FocusLost

    private void txtPartyCode1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCode1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCode1KeyPressed

    private void btnExpToExcelPlanning1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpToExcelPlanning1ActionPerformed
        // TODO add your handling code here:
        try {

            //exp.fillData(tblPlanning, new File("/root/Desktop/PLANNINGBOOKING_DATA.xls"));
            exp.fillData(tblPlanning1, new File("D://PPRSPLANNINGBOOKING_DATA.xls"));

            exp.fillData(tblPlanning1, new File(System.getProperty("user.home") + "/Desktop/PPRSPLANNINGBOOKING_DATA.xls"));

            JOptionPane.showMessageDialog(null, "Data saved at "
                    + "'/root/Desktop/PPRSPLANNINGBOOKING_DATA.xls' successfully in Linux PC or 'D://PPRSPLANNINGBOOKING_DATA.xls' successfully in Windows PC    ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnExpToExcelPlanning1ActionPerformed

    private void rdoExceptSDF1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoExceptSDF1ItemStateChanged
        // TODO add your handling code here:
        GenerateComboMonthYear_PPRS();
        setPPRSPlanningPieces();
    }//GEN-LAST:event_rdoExceptSDF1ItemStateChanged

    private void rdoSDF1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoSDF1ItemStateChanged
        // TODO add your handling code here:
        GenerateComboMonthYear_PPRS();
        setPPRSPlanningPieces();
    }//GEN-LAST:event_rdoSDF1ItemStateChanged

    private void btnShowOrderStatus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowOrderStatus1ActionPerformed
        // TODO add your handling code here:
        AppletFrame aFrame = new AppletFrame("Party Order Status");
        aFrame.startAppletEx("EITLERP.FeltSales.PieceRegister.FrmPOPendingOrder", "Party Order Status");
        FrmPOPendingOrder ObjItem = (FrmPOPendingOrder) aFrame.ObjApplet;

        ObjItem.requestFocus();
        ObjItem.setData(txtPartyCode1.getText());
    }//GEN-LAST:event_btnShowOrderStatus1ActionPerformed
    public void sort_query_creator() {
        SelectSortFields sort = new SelectSortFields();

        sort.setField("PR_PIECE_NO", "PIECE NO");
        sort.setField("PR_ORDER_DATE", "ORDER DATE");
        sort.setField("PR_DOC_NO", "DOC NO");
        sort.setField("PR_MACHINE_NO", "MACHINE NO");
        sort.setField("PR_POSITION_NO", "POSITION");
        sort.setField("PR_PARTY_CODE", "PARTY CODE");
        sort.setField("PR_PRODUCT_CODE", "PRODUCT CODE");
        sort.setField("PR_GROUP", "GROUP");
        sort.setField("PR_STYLE", "STYLE");
        sort.setField("PR_LENGTH", "LENGTH");
        sort.setField("PR_WIDTH", "WIDTH");
        sort.setField("PR_GSM", "GSM");
        sort.setField("PR_THORITICAL_WEIGHT", "WEIGHT");
        sort.setField("PR_SQMTR", "SQR MTR");
        sort.setField("PR_SYN_PER", "SYN PER");
        sort.setField("PR_REGION", "REGION");
        sort.setField("PR_INCHARGE", "INCHARGE");
        sort.setField("PR_REFERENCE", "REFERENCE");
        sort.setField("PR_REFERENCE_DATE", "REFERENCE DATE");
        sort.setField("PR_PO_NO", "P O NO");
        sort.setField("PR_PO_DATE", "P O DATE");
        sort.setField("PR_PIECE_STAGE", "PIECE STAGE");
        sort.setField("PR_WARP_DATE", "WARPING DATE");
        sort.setField("PR_WVG_DATE", "WEAVING DATE");
        sort.setField("PR_MND_DATE", "MENDING DATE");
        sort.setField("PR_NDL_DATE", "NEEDLING DATE");
        sort.setField("PR_FNSG_DATE", "FINISHING DATE");
        sort.setField("PR_ACTUAL_WEIGHT", "ACTUAL WEIGHT");
        sort.setField("PR_ACTUAL_LENGTH", "ACTUAL LENGTH");
        sort.setField("PR_ACTUAL_WIDTH", "ACTUAL WIDTH");
        sort.setField("PR_BALE_NO", "BALE NO");
        sort.setField("PR_PACKED_DATE", "PACKED DATE");
        sort.setField("PR_RCV_DATE", "RECIVED DATE");
        ORDER_BY = sort.getQuery(SelectSortFields.DEFAULT_ORDER.ASCENDING);
    }

    public void GenerateCapacityPlanning() {
        String report_include_pieces = "";
        for (int i = 0; i < tblPlanning.getRowCount(); i++) {
            if (tblPlanning.getValueAt(i, 1).equals(true)) {
                if (!"".equals(report_include_pieces)) {
                    report_include_pieces = report_include_pieces + ",'" + DataModel_Planning.getValueByVariable("PR_PIECE_NO", i) + "'";
                } else {
                    report_include_pieces = "'" + DataModel_Planning.getValueByVariable("PR_PIECE_NO", i) + "'";
                }
            }
        }
        for (int i = 0; i < tblWIP.getRowCount(); i++) {
            if (tblWIP.getValueAt(i, 1).equals(true)) {
                if (!"".equals(report_include_pieces)) {
                    report_include_pieces = report_include_pieces + ",'" + DataModel_WIP.getValueByVariable("PR_PIECE_NO", i) + "'";
                } else {
                    report_include_pieces = "'" + DataModel_WIP.getValueByVariable("PR_PIECE_NO", i) + "'";
                }
            }
        }

        search_check.SQL = "";
        search_check.ReturnCol = 1;
        search_check.OC_MONTH = cmbMonthYear.getSelectedItem().toString();
        search_check.ShowReturnCol = true;
        search_check.DefaultSearchOn = 1;
        search_check.report_include_pieces = report_include_pieces;
        if (search_check.ShowRSLOV()) {

        }
    }

    private void updateActivitiesTransactions() {
        try {
            ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES where UPDATE_STATUS='PENDING'");
            if (rsData.getRow() > 0) {
                rsData.first();
                while (!rsData.isAfterLast()) {
                    String PLANNER_ID = rsData.getString("PLANNER_ID");
                    String PIECE_NO = rsData.getString("PIECE_NO");

                    switch (rsData.getString("PLANNER_TYPE")) {
                        case "EXP_WIP_DEL_DATE":

                            try {
                                String EXP_WIP_DELIVERY_DATE = rsData.getString("EXP_WIP_DELIVERY_DATE");
                                String EXP_PI_DATE = rsData.getString("EXP_PI_DATE");
                                String PARTY_CODE = data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                String CHARGE_CODE = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + PARTY_CODE + "'");

                                if (CHARGE_CODE.equals("9") || CHARGE_CODE.equals("09") || CHARGE_CODE.equals("8") || CHARGE_CODE.equals("08")) {
                                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_WIP_DELIVERY_DATE='" + EXP_WIP_DELIVERY_DATE + "',PR_EXP_PI_DATE='" + EXP_PI_DATE + "' WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                } else {
                                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_WIP_DELIVERY_DATE='" + EXP_WIP_DELIVERY_DATE + "' WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                }

                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PLANNER_ID='" + PLANNER_ID + "' AND PIECE_NO='" + PIECE_NO + "'");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case "RESCHEDULE_MONTH":

                            String RESCHEDULE_REQ_MONTH = rsData.getString("RESCHEDULE_REQ_MONTH");

                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_REQUESTED_MONTH='" + RESCHEDULE_REQ_MONTH + "' WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PLANNER_ID='" + PLANNER_ID + "' AND  PIECE_NO='" + PIECE_NO + "'");

                            break;
                        case "SPL_REQUEST_MONTHYEAR":
                            String SPECIAL_REQ_MONTH = rsData.getString("SPECIAL_REQ_MONTH");
                            String SPECIAL_REQ_MONTH_DATE = rsData.getString("SPECIAL_REQ_MONTH_DATE");

                                                //if("".equals(SPECIAL_REQ_MONTH))
                            //{
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(SPECIAL_REQ_MONTH_DATE);
                                                        //System.out.println("output month : "+date.getMonth());
                                //System.out.println("output year : "+date.getYear());
                                int month = date.getMonth() + 1;
                                int year = date.getYear() + 1900;

                                SPECIAL_REQ_MONTH = getMonthName(month) + " - " + year;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                                                //}
                            //if("".equals(SPECIAL_REQ_MONTH_DATE))
                            //{
                            //    SPECIAL_REQ_MONTH_DATE = LastDayOfReqMonth(SPECIAL_REQ_MONTH_DATE);
                            //}

                            String OC_Month = data.getStringValueFromDB("SELECT PR_OC_MONTHYEAR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");

                            if (clsValidator.isCurrentSchMonthValid(SPECIAL_REQ_MONTH, OC_Month)) {
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_SPL_REQUEST_MONTHYEAR='" + SPECIAL_REQ_MONTH + "',PR_SPL_REQUEST_DATE='" + SPECIAL_REQ_MONTH_DATE + "',PR_CURRENT_SCH_MONTH='" + SPECIAL_REQ_MONTH + "',PR_CURRENT_SCH_LAST_DDMMYY='' WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PLANNER_ID='" + PLANNER_ID + "' AND  PIECE_NO='" + PIECE_NO + "'");
                            } else {
                                SPCL_REQ_MONTH_INVALID = SPCL_REQ_MONTH_INVALID + PIECE_NO + ",";
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='REJECTED-(" + SPECIAL_REQ_MONTH + "-" + OC_Month + ") IS NOT VALID',STATUS_UPDATE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PLANNER_ID='" + PLANNER_ID + "' AND  PIECE_NO='" + PIECE_NO + "'");
                            }

                            break;
                        case "EXP_PAY_CHQ_RCV_DATE":

                            String EXP_PAY_CHQ_RCV_DATE = rsData.getString("EXP_PAY_CHQ_RCV_DATE");

                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_PAY_CHQRC_DATE='" + EXP_PAY_CHQ_RCV_DATE + "' WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                            data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' WHERE PLANNER_ID='" + PLANNER_ID + "' AND  PIECE_NO='" + PIECE_NO + "'");

                            break;
                    }

                    rsData.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableAllTable() {
        //tblPlanning.setEnabled(false);
        //tblWIP.setEnabled(false);
        //tblStockBsr.setEnabled(false);
        //tblDispatch.setEnabled(false);

        for (int i = 0; i < tblPlanning.getColumnCount(); i++) {
            if (i != 3 && i != 8) {
                DataModel_Planning.SetReadOnly(i);
            }
        }
        for (int i = 0; i < tblWIP.getColumnCount(); i++) {
            if (i != 3 && i != 8) {
                DataModel_WIP.SetReadOnly(i);
            }
        }
        for (int i = 0; i < DataModel_STOCK.getColumnCount(); i++) {
            if (i != 3 && i != 8) {
                DataModel_STOCK.SetReadOnly(i);
            }
        }
        for (int i = 0; i < DataModel_PPRS_Planning.getColumnCount(); i++) {
            if (i != 3 && i != 8) {
                DataModel_PPRS_Planning.SetReadOnly(i);
            }
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
        //System.out.println("Date : " + sdf.format(date));
        return sdf.format(date);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTable TableCapacityPlanning;
    private javax.swing.JButton btnExpToExcelDispatched;
    private javax.swing.JButton btnExpToExcelPlanning;
    private javax.swing.JButton btnExpToExcelPlanning1;
    private javax.swing.JButton btnExpToExcelWIP;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSave1;
    private javax.swing.JButton btnShowData;
    private javax.swing.JButton btnShowData1;
    private javax.swing.JButton btnShowOrderStatus;
    private javax.swing.JButton btnShowOrderStatus1;
    private javax.swing.JButton btnSorting;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JComboBox cmbMonthYear;
    private javax.swing.JComboBox cmbMonthYear1;
    private javax.swing.JComboBox cmbPieceStage;
    private javax.swing.JComboBox cmbProdGroup;
    private javax.swing.JComboBox cmbProdGroup1;
    private javax.swing.JComboBox cmbZone;
    private javax.swing.JComboBox cmbZone1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JLabel lblPartyName1;
    private javax.swing.JLabel lblPieceNo;
    private javax.swing.JLabel lblPieceNo1;
    private javax.swing.JLabel lblPieceNo2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JRadioButton rdoCurSchMonth;
    private javax.swing.JRadioButton rdoExceptSDF;
    private javax.swing.JRadioButton rdoExceptSDF1;
    private javax.swing.JRadioButton rdoOcMonth;
    private javax.swing.JRadioButton rdoOcMonthReport;
    private javax.swing.JRadioButton rdoReqMonthReport;
    private javax.swing.JRadioButton rdoRequestMonth;
    private javax.swing.JRadioButton rdoSDF;
    private javax.swing.JRadioButton rdoSDF1;
    private javax.swing.JLabel selectedMonth;
    private javax.swing.JTable tblDispatch;
    private javax.swing.JTable tblPlanning;
    private javax.swing.JTable tblPlanning1;
    private javax.swing.JTable tblStockBsr;
    private javax.swing.JTable tblWIP;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyCode1;
    private javax.swing.JTextField txtPieceNo;
    private javax.swing.JTextField txtPieceNo1;
    private javax.swing.JLabel txtSelectedPieces;
    private javax.swing.JTextField txtTotalDispatch;
    private javax.swing.JTextField txtTotalPlanning;
    private javax.swing.JTextField txtTotalPlanning1;
    private javax.swing.JTextField txtTotalStockBSR;
    private javax.swing.JTextField txtTotalWIP;
    private javax.swing.JTextField txtUPN;
    private javax.swing.JTextField txtUPN1;
    private javax.swing.JButton vbtnExpToExcelStock;
    // End of variables declaration//GEN-END:variables

    private void GenerateProdMfgReportNonSDF() {
        try {
            String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
            if (OC_MONTH.equals("")) {
                txtSelectedPieces.setText("");
                TableCapacityPlanning.removeAll();
                DataModel_CapacityPlanning = new EITLTableModel();
                TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
                return;
            }
            String report_include_pieces = "";
            for (int i = 0; i < tblPlanning.getRowCount(); i++) {

                if (tblPlanning.getValueAt(i, 1).equals(true)) {
                    String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);
                    if ("".equals(report_include_pieces)) {
                        report_include_pieces = "'" + PIECE_NO + "'";
                    } else {
                        report_include_pieces = report_include_pieces + ",'" + PIECE_NO + "'";
                    }

                }
            }
            for (int i = 0; i < tblWIP.getRowCount(); i++) {

                if (tblWIP.getValueAt(i, 1).equals(true)) {
                    String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
                    if ("".equals(report_include_pieces)) {
                        report_include_pieces = "'" + PIECE_NO + "'";
                    } else {
                        report_include_pieces = report_include_pieces + ",'" + PIECE_NO + "'";
                    }
                }
            }

            txtSelectedPieces.setText(report_include_pieces);
            //String sql = "";
            String cndtn = "";
            String SUB_QRY = "";

            if (!"".equals(report_include_pieces)) {
                SUB_QRY = " (PR_OC_MONTHYEAR = '" + OC_MONTH + "' OR PR_PIECE_NO IN (" + report_include_pieces + ")) ";
            } else {
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
                    + "WHERE (MTR_CAPTION_CODE !=10  AND PRODUCT_CAPTION NOT LIKE '%HDS%') ";

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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND  PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
                    + "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
                    + "AND MTR_CAPTION_CODE =10 AND PRODUCT_CAPTION NOT LIKE '%SDF%' "
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

            System.out.println("\n\nSQL Query ACTUAL :" + sql3);

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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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

            /* NEW CODE */
            String SUB_QRY_PROD = "";

            if (!"".equals(report_include_pieces)) {
                SUB_QRY_PROD = " (PR_OC_MONTHYEAR = '" + OC_MONTH + "' OR PR_PIECE_NO IN (" + report_include_pieces + ")) AND PR_PIECE_STAGE IN ('IN STOCK','BSR','INVOICED','EXP-INVOICE')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) AND  PR_OC_MONTHYEAR IN (concat(DATE_FORMAT(PR_FNSG_DATE, '%b'),' - ',DATE_FORMAT(PR_FNSG_DATE, '%Y')) ) ";
            } else {
                SUB_QRY_PROD = " PR_OC_MONTHYEAR = '" + OC_MONTH + "' AND PR_PIECE_STAGE IN ('IN STOCK','BSR','INVOICED','EXP-INVOICE')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) AND  PR_OC_MONTHYEAR IN (concat(DATE_FORMAT(PR_FNSG_DATE, '%b'),' - ',DATE_FORMAT(PR_FNSG_DATE, '%Y')) ) ";
            }

            String sql2_new = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY_PROD + " "
                    + "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE !=10 AND INCHARGE_CODE = INCHARGE_CD  "
                    + ") AS NG GROUP BY LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS PROD "
                    + "SET CAP.ACNE_PRODUCTION = PROD.SC5, "
                    + "CAP.EXPORT_PRODUCTION = PROD.SC6, "
                    + "CAP.SOUTH_PRODUCTION = PROD.SC1, "
                    + "CAP.NORTH_PRODUCTION = PROD.SC2, "
                    + "CAP.EAST_PRODUCTION = PROD.SC3, "
                    + "CAP.KEY_PRODUCTION = PROD.SC7 "
                    + "WHERE PROD.CATEGORY = CAP.CATEGORY "
                    + "AND PROD.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
                    + "AND PROD.LOGIN_ID = CAP.LOGIN_ID ";
            //+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";

            System.out.println("\n\nSQL Query2:" + sql2_new);

            data.Execute(sql2_new);

            String sql3_new = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND  PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY_PROD + " "
                    + "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
                    + "AND MTR_CAPTION_CODE =10  "
                    + " AND INCHARGE_CODE = INCHARGE_CD  "
                    + ") AS NG GROUP BY LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS PROD "
                    + "SET CAP.ACNE_PRODUCTION = PROD.SC5, "
                    + "CAP.EXPORT_PRODUCTION = PROD.SC6, "
                    + "CAP.SOUTH_PRODUCTION = PROD.SC1, "
                    + "CAP.NORTH_PRODUCTION = PROD.SC2, "
                    + "CAP.EAST_PRODUCTION = PROD.SC3, "
                    + "CAP.KEY_PRODUCTION = PROD.SC7 "
                    + "WHERE PROD.CATEGORY = CAP.CATEGORY "
                    + "AND PROD.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
                    + "AND PROD.LOGIN_ID = CAP.LOGIN_ID ";
            //+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";

            System.out.println("\n\nSQL Query:" + sql3_new);

            data.Execute(sql3_new);

            String sql4_new = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY_PROD + " "
                    + " AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
                    + " AND (MTR_CAPTION_CODE =10  OR PRODUCT_CAPTION LIKE '%HDS%') " //comment
                    + " AND INCHARGE_CODE = INCHARGE_CD  "
                    + ") AS NG GROUP BY LOGIN_ID ) AS PROD "
                    + "SET CAP.ACNE_PRODUCTION = PROD.SC5, "
                    + "CAP.EXPORT_PRODUCTION = PROD.SC6, "
                    + "CAP.SOUTH_PRODUCTION = PROD.SC1, "
                    + "CAP.NORTH_PRODUCTION = PROD.SC2, "
                    + "CAP.EAST_PRODUCTION = PROD.SC3, "
                    + "CAP.KEY_PRODUCTION = PROD.SC7  "
                    + "WHERE PROD.CATEGORY = CAP.CATEGORY "
                    + "AND PROD.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
                    + "AND PROD.LOGIN_ID = CAP.LOGIN_ID ";
            //+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH" ;

            System.out.println("\n\nSQL Query:" + sql4_new);
            data.Execute(sql4_new);

            /* NEW CODE */
            String sql5 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH SET TOTAL_ACTUAL = ACNE_ACTUAL + EXPORT_ACTUAL + NORTH_ACTUAL + SOUTH_ACTUAL + EAST_ACTUAL + KEY_ACTUAL WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'";

            System.out.println("\n\nSQL Query 5 :" + sql5);

            data.Execute(sql5);

            String sql6 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH SET TOTAL_PRODUCTION = ACNE_PRODUCTION + EXPORT_PRODUCTION + NORTH_PRODUCTION + SOUTH_PRODUCTION + EAST_PRODUCTION + KEY_PRODUCTION WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'";

            data.Execute(sql6);

            String sql = "SELECT PRODUCT_CAPTION AS 'PRODUCT', MTR_CAPTION AS 'LENGTH CATEGORY',ACNE_CAPACITY ,ACNE_ACTUAL, ACNE_PRODUCTION, EXPORT_CAPACITY ,EXPORT_ACTUAL ,EXPORT_PRODUCTION ,NORTH_CAPACITY ,NORTH_ACTUAL ,NORTH_PRODUCTION ,EAST_CAPACITY AS 'EAST/WEST_CAPACITY',EAST_ACTUAL AS 'EAST/WEST_ACTUAL',EAST_PRODUCTION ,SOUTH_CAPACITY,SOUTH_ACTUAL ,SOUTH_PRODUCTION ,KEY_CAPACITY AS 'KEYCLIENT_CAPACITY',KEY_ACTUAL AS 'KEYCLIENT_ACTUAL' ,KEY_PRODUCTION ,TOTAL_CAPACITY ,TOTAL_ACTUAL, TOTAL_PRODUCTION ,OC_MONTH FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH  WHERE TOTAL_CAPACITY + TOTAL_ACTUAL > 0 AND LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'  ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";
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
                    String Field_Name = rsInfo.getColumnName(i).replaceAll("CAPACITY", "COMMITMENT");
                    DataModel_CapacityPlanning.addColumn(Field_Name);
                }

                for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                    TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(130);
                    TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_report);
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

                            if (m == 4 || m == 7 || m == 10 || m == 13 || m == 16 || m == 19 || m == 22) {
                                if (Double.parseDouble(rowData[m - 1].toString()) >= Double.parseDouble(rowData[m].toString())) {
                                    Renderer_report.setBackColor(k - 1, m, new Color(45, 219, 109));
                                } else {
                                    Renderer_report.setBackColor(k - 1, m, new Color(243, 99, 99));
                                }
                            }

                            Renderer_report.setBackColor(3, m, new Color(216, 213, 213));
                            Renderer_report.setBackColor(8, m, new Color(216, 213, 213));
                            Renderer_report.setBackColor(10, m, new Color(171, 163, 163));
                        }
                        DataModel_CapacityPlanning.addRow(rowData);

                        try {

                            for (int l = 3; l <= 16; l++) {
                                Double total = Double.parseDouble(TableCapacityPlanning.getValueAt(3, l).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(8, l).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(9, l).toString());;
                                TableCapacityPlanning.setValueAt("" + total, 10, l);
                            }
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                        rs.next();
                        k++;
                    }
                }
                DataModel_CapacityPlanning.TableReadOnly(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateProdMfgReportSDF() {
        try {
            String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
            if (OC_MONTH.equals("")) {
                txtSelectedPieces.setText("");
                TableCapacityPlanning.removeAll();
                DataModel_CapacityPlanning = new EITLTableModel();
                TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
                return;
            }
            String report_include_pieces = "";
            for (int i = 0; i < tblPlanning.getRowCount(); i++) {

                if (tblPlanning.getValueAt(i, 1).equals(true)) {
                    String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);
                    if ("".equals(report_include_pieces)) {
                        report_include_pieces = "'" + PIECE_NO + "'";
                    } else {
                        report_include_pieces = report_include_pieces + ",'" + PIECE_NO + "'";
                    }

                }
            }
            for (int i = 0; i < tblWIP.getRowCount(); i++) {

                if (tblWIP.getValueAt(i, 1).equals(true)) {
                    String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
                    if ("".equals(report_include_pieces)) {
                        report_include_pieces = "'" + PIECE_NO + "'";
                    } else {
                        report_include_pieces = report_include_pieces + ",'" + PIECE_NO + "'";
                    }
                }
            }

            txtSelectedPieces.setText(report_include_pieces);
            //String sql = "";
            String cndtn = "";
            String SUB_QRY = "";

            if (!"".equals(report_include_pieces)) {
                SUB_QRY = " (PR_OC_MONTHYEAR = '" + OC_MONTH + "' OR PR_PIECE_NO IN (" + report_include_pieces + ")) ";
            } else {
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

            /* NEW CODE */
            String SUB_QRY_PROD = "";

            if (!"".equals(report_include_pieces)) {
                SUB_QRY_PROD = " (PR_OC_MONTHYEAR = '" + OC_MONTH + "' OR PR_PIECE_NO IN (" + report_include_pieces + ")) AND PR_PIECE_STAGE IN ('IN STOCK','BSR','INVOICED','EXP-INVOICE')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) AND  PR_OC_MONTHYEAR IN (concat(DATE_FORMAT(PR_FNSG_DATE, '%b'),' - ',DATE_FORMAT(PR_FNSG_DATE, '%Y')) ) ";
            } else {
                SUB_QRY_PROD = " PR_OC_MONTHYEAR = '" + OC_MONTH + "' AND PR_PIECE_STAGE IN ('IN STOCK','BSR','INVOICED','EXP-INVOICE')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) AND  PR_OC_MONTHYEAR IN (concat(DATE_FORMAT(PR_FNSG_DATE, '%b'),' - ',DATE_FORMAT(PR_FNSG_DATE, '%Y')) ) ";
            }

            String sql2_new = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY_PROD + " "
                    + "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE !=10 AND INCHARGE_CODE = INCHARGE_CD  "
                    + ") AS NG GROUP BY LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS PROD "
                    + "SET CAP.ACNE_PRODUCTION = PROD.SC5, "
                    + "CAP.EXPORT_PRODUCTION = PROD.SC6, "
                    + "CAP.SOUTH_PRODUCTION = PROD.SC1, "
                    + "CAP.NORTH_PRODUCTION = PROD.SC2, "
                    + "CAP.EAST_PRODUCTION = PROD.SC3, "
                    + "CAP.KEY_PRODUCTION = PROD.SC7 "
                    + "WHERE PROD.CATEGORY = CAP.CATEGORY "
                    + "AND PROD.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
                    + "AND PROD.LOGIN_ID = CAP.LOGIN_ID ";
//+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";

            System.out.println("\n\nSQL Query2:" + sql2_new);

            data.Execute(sql2_new);
            String sql3_new = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND  PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY_PROD + " "
                    + "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
                    + "AND MTR_CAPTION_CODE =10 "
                    + " AND INCHARGE_CODE = INCHARGE_CD  "
                    + ") AS NG GROUP BY LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS PROD "
                    + "SET CAP.ACNE_PRODUCTION = PROD.SC5, "
                    + "CAP.EXPORT_PRODUCTION = PROD.SC6, "
                    + "CAP.SOUTH_PRODUCTION = PROD.SC1, "
                    + "CAP.NORTH_PRODUCTION = PROD.SC2, "
                    + "CAP.EAST_PRODUCTION = PROD.SC3, "
                    + "CAP.KEY_PRODUCTION = PROD.SC7 "
                    + "WHERE PROD.CATEGORY = CAP.CATEGORY "
                    + "AND PROD.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
                    + "AND PROD.LOGIN_ID = CAP.LOGIN_ID ";
//+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";
            System.out.println("\n\nSQL Query:" + sql3_new);

            data.Execute(sql3_new);

            String sql4_new = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY_PROD + " "
                    + " AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH "
                    + " AND MTR_CAPTION_CODE =10 " //comment
                    + " AND INCHARGE_CODE = INCHARGE_CD  "
                    + ") AS NG GROUP BY LOGIN_ID ) AS PROD "
                    + "SET CAP.ACNE_PRODUCTION = PROD.SC5, "
                    + "CAP.EXPORT_PRODUCTION = PROD.SC6, "
                    + "CAP.SOUTH_PRODUCTION = PROD.SC1, "
                    + "CAP.NORTH_PRODUCTION = PROD.SC2, "
                    + "CAP.EAST_PRODUCTION = PROD.SC3, "
                    + "CAP.KEY_PRODUCTION = PROD.SC7  "
                    + "WHERE PROD.CATEGORY = CAP.CATEGORY "
                    + "AND PROD.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
                    + "AND PROD.LOGIN_ID = CAP.LOGIN_ID ";
//+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH" ;

            System.out.println("\n\nSQL Query:" + sql4_new);
            data.Execute(sql4_new);

            /* NEW CODE END */
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND  PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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

            String sql = "SELECT PRODUCT_CAPTION AS 'PRODUCT', MTR_CAPTION AS 'LENGTH CATEGORY',ACNE_CAPACITY ,ACNE_ACTUAL ,ACNE_PRODUCTION , EXPORT_CAPACITY ,EXPORT_ACTUAL , EXPORT_PRODUCTION ,NORTH_CAPACITY ,NORTH_ACTUAL, NORTH_PRODUCTION ,EAST_CAPACITY AS 'EAST/WEST_CAPACITY',EAST_ACTUAL AS 'EAST/WEST_ACTUAL',EAST_PRODUCTION ,SOUTH_CAPACITY,SOUTH_ACTUAL, SOUTH_PRODUCTION,KEY_CAPACITY AS 'KEYCLIENT_CAPACITY',KEY_ACTUAL AS 'KEYCLIENT_ACTUAL',KEY_PRODUCTION ,TOTAL_CAPACITY ,TOTAL_ACTUAL ,OC_MONTH FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH  WHERE TOTAL_CAPACITY + TOTAL_ACTUAL > 0 AND LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'  ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";

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
                    String Field_Name = rsInfo.getColumnName(i).replaceAll("CAPACITY", "COMMITMENT");
                    DataModel_CapacityPlanning.addColumn(Field_Name);
                }

                for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                    TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
                    TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_report);
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

                            if (m == 4 || m == 7 || m == 10 || m == 13 || m == 16 || m == 19 || m == 22) {
                                if (Double.parseDouble(rowData[m - 1].toString()) >= Double.parseDouble(rowData[m].toString())) {
                                    Renderer_report.setBackColor(k - 1, m, new Color(45, 219, 109));
                                } else {
                                    Renderer_report.setBackColor(k - 1, m, new Color(243, 99, 99));
                                }
                            }

                            Renderer_report.setBackColor(3, m, Color.lightGray);
                            Renderer_report.setBackColor(8, m, Color.lightGray);
                            Renderer_report.setBackColor(11, m, Color.lightGray);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void GenerateProdMfgReportNonSDF()
     {
     try{
     String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
     if(OC_MONTH.equals(""))
     {
     txtSelectedPieces.setText("");
     TableCapacityPlanning.removeAll();
     DataModel_CapacityPlanning = new EITLTableModel();
     TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
     return;
     }
     String report_include_pieces = "";
     for(int i=0;i<tblPlanning.getRowCount();i++)
     {

     if(tblPlanning.getValueAt(i, 1).equals(true))
     {
     String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);
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
     for(int i=0;i<tblWIP.getRowCount();i++)
     {

     if(tblWIP.getValueAt(i, 1).equals(true))
     {
     String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
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
     String Field_Name = rsInfo.getColumnName(i).replaceAll("CAPACITY", "COMMITMENT");                    DataModel_CapacityPlanning.addColumn(Field_Name);
     }
            
     for (i = 1; i <= rsInfo.getColumnCount(); i++) {
     TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
     TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_report);
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
     Renderer_report.setBackColor(k-1, m, new Color(45,219,109));
     }
     else
     {
     Renderer_report.setBackColor(k-1, m, new Color(243,99,99));
     }
     }
                        
     Renderer_report.setBackColor(3, m, new Color(216,213,213));
     Renderer_report.setBackColor(8, m, new Color(216,213,213));
     Renderer_report.setBackColor(10, m, new Color(171,163,163));
     }
     DataModel_CapacityPlanning.addRow(rowData);
                    
     try{
                            
     for(int l=3;l<=16;l++)
     {
     Double total = Double.parseDouble(TableCapacityPlanning.getValueAt(3, l).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(8, l).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(9, l).toString());;
     TableCapacityPlanning.setValueAt(""+total, 10, l);
     }
     }catch(Exception e)
     {
     //e.printStackTrace();
     }
                    
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
     */
    /* private void GenerateProdMfgReport()
     {
     try{
     String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
        
     String report_include_pieces = "";
     for(int i=0;i<tblPlanning.getRowCount();i++)
     {

     if(tblPlanning.getValueAt(i, 1).equals(true))
     {
     String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);
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
     for(int i=0;i<tblWIP.getRowCount();i++)
     {

     if(tblWIP.getValueAt(i, 1).equals(true))
     {
     String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
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
     + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
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
        
     //System.out.println("SQL Query:" + sql1);
       
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

     //System.out.println("SQL Query2:" + sql2);
       
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
     + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
     + "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE =10 AND INCHARGE_CODE = INCHARGE_CD  "
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
     //System.out.println("SQL Query:" + sql3);
       
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
     + " AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE =10 AND INCHARGE_CODE = INCHARGE_CD  "
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

     //System.out.println("SQL Query:" + sql4);
     data.Execute(sql4);

      
     String sql5 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH SET TOTAL_ACTUAL = ACNE_ACTUAL + EXPORT_ACTUAL + NORTH_ACTUAL + SOUTH_ACTUAL + EAST_ACTUAL + KEY_ACTUAL WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'";

     //System.out.println("SQL Query:" + sql5);
       
     data.Execute(sql5);

        
     String sql = "SELECT PRODUCT_CAPTION AS 'PRODUCT', MTR_CAPTION AS 'LENGTH CATEGORY',ACNE_CAPACITY ,ACNE_ACTUAL, EXPORT_CAPACITY ,EXPORT_ACTUAL ,NORTH_CAPACITY ,NORTH_ACTUAL ,EAST_CAPACITY AS 'EAST/WEST_CAPACITY',EAST_ACTUAL AS 'EAST/WEST_ACTUAL' ,SOUTH_CAPACITY,SOUTH_ACTUAL,KEY_CAPACITY AS 'KEYCLIENT_CAPACITY',KEY_ACTUAL AS 'KEYCLIENT_ACTUAL' ,TOTAL_CAPACITY ,TOTAL_ACTUAL ,OC_MONTH FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH  WHERE TOTAL_CAPACITY + TOTAL_ACTUAL > 0 AND LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";
      
     //System.out.println("SQL Query:" + sql);
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
     String Field_Name = rsInfo.getColumnName(i).replaceAll("CAPACITY", "COMMITMENT");                    DataModel_CapacityPlanning.addColumn(Field_Name);
     }

     for (i = 1; i <= rsInfo.getColumnCount(); i++) {
     TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
     TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_report);
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
     try{
     if(Double.parseDouble(rowData[m-1].toString()) >= Double.parseDouble(rowData[m].toString()))
     {
     Renderer_report.setBackColor(k-1, m, new Color(45,219,109));
     }
     else
     {
     Renderer_report.setBackColor(k-1, m, new Color(243,99,99));
     }
     }catch(Exception e)
     {
     e.printStackTrace();
     }
     }
     Renderer_report.setBackColor(3, m, Color.lightGray);
     Renderer_report.setBackColor(8, m, Color.lightGray);
     Renderer_report.setBackColor(11, m, Color.lightGray);
     }
     DataModel_CapacityPlanning.addRow(rowData);
     rs.next();
     k++;
     }
     }
     DataModel_CapacityPlanning.TableReadOnly(true);
     } catch (Exception e) {
     //e.printStackTrace();
     }
     }catch(Exception e)
     {
     //e.printStackTrace();
     }
     }
     */
    /*
     private void GenerateProdMfgReportSDF()
     {
     try{
     String OC_MONTH = cmbMonthYear.getSelectedItem().toString();
     if(OC_MONTH.equals(""))
     {
     txtSelectedPieces.setText("");
     TableCapacityPlanning.removeAll();
     DataModel_CapacityPlanning = new EITLTableModel();
     TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
     return;
     }
     String report_include_pieces = "";
     for(int i=0;i<tblPlanning.getRowCount();i++)
     {

     if(tblPlanning.getValueAt(i, 1).equals(true))
     {
     String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);
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
     for(int i=0;i<tblWIP.getRowCount();i++)
     {

     if(tblWIP.getValueAt(i, 1).equals(true))
     {
     String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
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
     String Field_Name = rsInfo.getColumnName(i).replaceAll("CAPACITY", "COMMITMENT");                    DataModel_CapacityPlanning.addColumn(Field_Name);
     }
            
     for (i = 1; i <= rsInfo.getColumnCount(); i++) {
     TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
     TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_report);
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
     Renderer_report.setBackColor(k-1, m, new Color(45,219,109));
     }
     else
     {
     Renderer_report.setBackColor(k-1, m, new Color(243,99,99));
     }
     }
                        
     Renderer_report.setBackColor(3, m, Color.lightGray);
     Renderer_report.setBackColor(8, m, Color.lightGray);
     Renderer_report.setBackColor(11, m, Color.lightGray);
                        
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
    
     */
    private void GenerateProdMfgReportSDF_ReqMonth() {
        try {
            String REQ_MONTH = cmbMonthYear.getSelectedItem().toString();
            if (REQ_MONTH.equals("")) {
                txtSelectedPieces.setText("");
                TableCapacityPlanning.removeAll();
                DataModel_CapacityPlanning = new EITLTableModel();
                TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
                return;
            }
            String report_include_pieces = "";
            for (int i = 0; i < tblPlanning.getRowCount(); i++) {

                if (tblPlanning.getValueAt(i, 1).equals(true)) {
                    String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);
                    if ("".equals(report_include_pieces)) {
                        report_include_pieces = "'" + PIECE_NO + "'";
                    } else {
                        report_include_pieces = report_include_pieces + ",'" + PIECE_NO + "'";
                    }

                }
            }
            for (int i = 0; i < tblWIP.getRowCount(); i++) {

                if (tblWIP.getValueAt(i, 1).equals(true)) {
                    String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
                    if ("".equals(report_include_pieces)) {
                        report_include_pieces = "'" + PIECE_NO + "'";
                    } else {
                        report_include_pieces = report_include_pieces + ",'" + PIECE_NO + "'";
                    }
                }
            }

            txtSelectedPieces.setText(report_include_pieces);
            //String sql = "";
            String cndtn = "";
            String SUB_QRY = "";

            if (!"".equals(report_include_pieces)) {
                SUB_QRY = " (PR_REQUESTED_MONTH = '" + REQ_MONTH + "' OR PR_PIECE_NO IN (" + report_include_pieces + ")) AND PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPLICING','SEAMING','GIDC')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)";
            } else {
                SUB_QRY = " PR_REQUESTED_MONTH = '" + REQ_MONTH + "' AND PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPLICING','SEAMING','GIDC','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)";
            }
            data.Execute("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND REQ_MONTH ='" + REQ_MONTH + "'");

            System.out.println("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND REQ_MONTH ='" + REQ_MONTH + "'");

            String sql1 = "INSERT INTO PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH "
                    + "(LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION,ACNE_CAPACITY,EXPORT_CAPACITY,NORTH_CAPACITY,EAST_CAPACITY,SOUTH_CAPACITY,KEY_CAPACITY,TOTAL_CAPACITY,REQ_MONTH)"
                    + " SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PRODUCT_CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =5 THEN PRODUCT_CAPACITY END,0)) AS ACNE,"
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =6 THEN PRODUCT_CAPACITY END,0)) AS EXPORT,"
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =2 THEN PRODUCT_CAPACITY END,0)) AS NORTH, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =3 THEN PRODUCT_CAPACITY END,0)) AS WEST, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =1 THEN PRODUCT_CAPACITY END,0)) AS SOUTH, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =7 THEN PRODUCT_CAPACITY END,0)) AS KEYCLIENT, "
                    + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + REQ_MONTH + "' AS REQ_MONTH "
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
//            + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + REQ_MONTH + "' AS REQ_MONTH "
//            + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
//            + "WHERE MTR_CAPTION_CODE !=10 " ;

            System.out.println("\n\nSQL Query:" + sql1);

            data.Execute(sql1);

            String sql2 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH AS CAP, "
                    + "(SELECT LOGIN_ID, PR_REQUESTED_MONTH, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD =5 THEN CNT END,0)) AS SC5, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
                    //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "QM.CATEGORY,PRODUCT_CAPTION, "
                    + "MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
                    + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
//+ "AND ACT.PR_REQUESTED_MONTH = CAP.REQ_MONTH ";

            System.out.println("\n\nSQL Query2:" + sql2);

            data.Execute(sql2);
            String sql3 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH AS CAP, "
                    + "(SELECT LOGIN_ID, PR_REQUESTED_MONTH, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
                    //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
                    + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND  PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
//+ "AND ACT.PR_REQUESTED_MONTH = CAP.REQ_MONTH ";
            System.out.println("\n\nSQL Query:" + sql3);

            data.Execute(sql3);

            String sql4 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH AS CAP, "
                    //+ "(SELECT LOGIN_ID, PR_REQUESTED_MONTH, '5' collate utf8_unicode_ci AS CATEGORY,'5. GRAND TOTAL' collate utf8_unicode_ci AS PRODUCT_CAPTION,'11' collate utf8_unicode_ci AS MTR_CAPTION_CODE,'GRAND TOTAL' collate utf8_unicode_ci AS MTR_CAPTION, "
                    + "(SELECT LOGIN_ID, PR_REQUESTED_MONTH, '5'  AS CATEGORY,'5. GRAND TOTAL'  AS PRODUCT_CAPTION,'11' AS MTR_CAPTION_CODE,'GRAND TOTAL' AS MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
                    //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
                    + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
                    + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
//+ "AND ACT.PR_REQUESTED_MONTH = CAP.REQ_MONTH" ;

            System.out.println("\n\nSQL Query:" + sql4);
            data.Execute(sql4);

            String sql5 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH SET TOTAL_ACTUAL = ACNE_ACTUAL + EXPORT_ACTUAL + NORTH_ACTUAL + SOUTH_ACTUAL + EAST_ACTUAL + KEY_ACTUAL WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND REQ_MONTH ='" + REQ_MONTH + "'";

            System.out.println("\n\nSQL Query 5 :" + sql5);

            data.Execute(sql5);

            String sql = "SELECT PRODUCT_CAPTION AS 'PRODUCT', MTR_CAPTION AS 'LENGTH CATEGORY',ACNE_CAPACITY ,ACNE_ACTUAL, EXPORT_CAPACITY ,EXPORT_ACTUAL ,NORTH_CAPACITY ,NORTH_ACTUAL ,EAST_CAPACITY AS 'EAST/WEST_CAPACITY',EAST_ACTUAL AS 'EAST/WEST_ACTUAL' ,SOUTH_CAPACITY,SOUTH_ACTUAL,KEY_CAPACITY AS 'KEYCLIENT_CAPACITY',KEY_ACTUAL AS 'KEYCLIENT_ACTUAL' ,TOTAL_CAPACITY ,TOTAL_ACTUAL ,REQ_MONTH FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH  WHERE TOTAL_CAPACITY + TOTAL_ACTUAL > 0 AND LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND REQ_MONTH ='" + REQ_MONTH + "'  ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";

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
                    String Field_Name = rsInfo.getColumnName(i).replaceAll("CAPACITY", "COMMITMENT");
                    DataModel_CapacityPlanning.addColumn(Field_Name);
                }

                for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                    TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
                    TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_report);
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

                            if (m == 4 || m == 6 || m == 8 || m == 10 || m == 12 || m == 14 || m == 16) {
                                if (Double.parseDouble(rowData[m - 1].toString()) >= Double.parseDouble(rowData[m].toString())) {
                                    Renderer_report.setBackColor(k - 1, m, new Color(45, 219, 109));
                                } else {
                                    Renderer_report.setBackColor(k - 1, m, new Color(243, 99, 99));
                                }
                            }

                            Renderer_report.setBackColor(3, m, Color.lightGray);
                            Renderer_report.setBackColor(8, m, Color.lightGray);
                            Renderer_report.setBackColor(11, m, Color.lightGray);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateProdMfgReportNonSDF_ReqMonth() {

        try {
            String REQ_MONTH = cmbMonthYear.getSelectedItem().toString();
            if (REQ_MONTH.equals("")) {
                txtSelectedPieces.setText("");
                TableCapacityPlanning.removeAll();
                DataModel_CapacityPlanning = new EITLTableModel();
                TableCapacityPlanning.setModel(DataModel_CapacityPlanning);
                return;
            }
            String report_include_pieces = "";
            for (int i = 0; i < tblPlanning.getRowCount(); i++) {

                if (tblPlanning.getValueAt(i, 1).equals(true)) {
                    String PIECE_NO = DataModel_Planning.getValueByVariable("PR_PIECE_NO", i);
                    if ("".equals(report_include_pieces)) {
                        report_include_pieces = "'" + PIECE_NO + "'";
                    } else {
                        report_include_pieces = report_include_pieces + ",'" + PIECE_NO + "'";
                    }

                }
            }
            for (int i = 0; i < tblWIP.getRowCount(); i++) {

                if (tblWIP.getValueAt(i, 1).equals(true)) {
                    String PIECE_NO = DataModel_WIP.getValueByVariable("PR_PIECE_NO", i);
                    if ("".equals(report_include_pieces)) {
                        report_include_pieces = "'" + PIECE_NO + "'";
                    } else {
                        report_include_pieces = report_include_pieces + ",'" + PIECE_NO + "'";
                    }
                }
            }

            txtSelectedPieces.setText(report_include_pieces);
            //String sql = "";
            String cndtn = "";
            String SUB_QRY = "";

            if (!"".equals(report_include_pieces)) {
                SUB_QRY = " (PR_REQUESTED_MONTH = '" + REQ_MONTH + "' OR PR_PIECE_NO IN (" + report_include_pieces + ")) AND PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPLICING','SEAMING','GIDC','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)";
            } else {
                SUB_QRY = " PR_REQUESTED_MONTH = '" + REQ_MONTH + "' AND PR_PIECE_STAGE IN ('PLANNING','BOOKING','WEAVING','MENDING','NEEDLING','FINISHING','SPLICING','SEAMING','GIDC','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING')  AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)";
            }
            data.Execute("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND REQ_MONTH ='" + REQ_MONTH + "'");

            System.out.println("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND REQ_MONTH ='" + REQ_MONTH + "'");

            String sql1 = "INSERT INTO PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH "
                    + "(LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION,ACNE_CAPACITY,EXPORT_CAPACITY,NORTH_CAPACITY,EAST_CAPACITY,SOUTH_CAPACITY,KEY_CAPACITY,TOTAL_CAPACITY,REQ_MONTH)"
                    + " SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PRODUCT_CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =5 THEN PRODUCT_CAPACITY END,0)) AS ACNE,"
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =6 THEN PRODUCT_CAPACITY END,0)) AS EXPORT,"
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =2 THEN PRODUCT_CAPACITY END,0)) AS NORTH, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =3 THEN PRODUCT_CAPACITY END,0)) AS WEST, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =1 THEN PRODUCT_CAPACITY END,0)) AS SOUTH, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =7 THEN PRODUCT_CAPACITY END,0)) AS KEYCLIENT, "
                    + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + REQ_MONTH + "' AS REQ_MONTH "
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
                    + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + REQ_MONTH + "' AS REQ_MONTH "
                    + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
                    + "WHERE (MTR_CAPTION_CODE !=10  AND PRODUCT_CAPTION NOT LIKE '%HDS%') ";

            System.out.println("\n\nSQL Query:" + sql1);

            data.Execute(sql1);

            String sql2 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH AS CAP, "
                    + "(SELECT LOGIN_ID, PR_REQUESTED_MONTH, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD =5 THEN CNT END,0)) AS SC5, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
                    //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "QM.CATEGORY,PRODUCT_CAPTION, "
                    + "MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
                    + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
            //+ "AND ACT.PR_REQUESTED_MONTH = CAP.REQ_MONTH ";

            System.out.println("\n\nSQL Query2:" + sql2);

            data.Execute(sql2);
            String sql3 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH AS CAP, "
                    + "(SELECT LOGIN_ID, PR_REQUESTED_MONTH, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
                    //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
                    + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND  PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
            //+ "AND ACT.PR_REQUESTED_MONTH = CAP.REQ_MONTH ";

            System.out.println("\n\nSQL Query:" + sql3);

            data.Execute(sql3);

            String sql4 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH AS CAP, "
                    //+ "(SELECT LOGIN_ID, PR_REQUESTED_MONTH, '5' collate utf8_unicode_ci AS CATEGORY,'5. GRAND TOTAL' collate utf8_unicode_ci AS PRODUCT_CAPTION,'11' collate utf8_unicode_ci AS MTR_CAPTION_CODE,'GRAND TOTAL' collate utf8_unicode_ci AS MTR_CAPTION, "
                    + "(SELECT LOGIN_ID, PR_REQUESTED_MONTH, '5'  AS CATEGORY,'5. GRAND TOTAL'  AS PRODUCT_CAPTION,'11' AS MTR_CAPTION_CODE,'GRAND TOTAL' AS MTR_CAPTION, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
                    + "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
                    //+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_REQUESTED_MONTH,  "
                    + "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
                    + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
                    + "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
                    + "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND " + SUB_QRY + " "
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
            //+ "AND ACT.PR_REQUESTED_MONTH = CAP.REQ_MONTH" ;

            System.out.println("\n\nSQL Query:" + sql4);
            data.Execute(sql4);

            String sql5 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH SET TOTAL_ACTUAL = ACNE_ACTUAL + EXPORT_ACTUAL + NORTH_ACTUAL + SOUTH_ACTUAL + EAST_ACTUAL + KEY_ACTUAL WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND REQ_MONTH ='" + REQ_MONTH + "'";

            System.out.println("\n\nSQL Query 5 :" + sql5);

            data.Execute(sql5);

            String sql = "SELECT PRODUCT_CAPTION AS 'PRODUCT', MTR_CAPTION AS 'LENGTH CATEGORY',ACNE_CAPACITY ,ACNE_ACTUAL, EXPORT_CAPACITY ,EXPORT_ACTUAL ,NORTH_CAPACITY ,NORTH_ACTUAL ,EAST_CAPACITY AS 'EAST/WEST_CAPACITY',EAST_ACTUAL AS 'EAST/WEST_ACTUAL' ,SOUTH_CAPACITY,SOUTH_ACTUAL,KEY_CAPACITY AS 'KEYCLIENT_CAPACITY',KEY_ACTUAL AS 'KEYCLIENT_ACTUAL' ,TOTAL_CAPACITY ,TOTAL_ACTUAL ,REQ_MONTH FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_REQ_MONTH  WHERE TOTAL_CAPACITY + TOTAL_ACTUAL > 0 AND LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND REQ_MONTH ='" + REQ_MONTH + "'  ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";

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
                    String Field_Name = rsInfo.getColumnName(i).replaceAll("CAPACITY", "COMMITMENT");
                    DataModel_CapacityPlanning.addColumn(Field_Name);
                }

                for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                    TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
                    TableCapacityPlanning.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_report);
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

                            if (m == 4 || m == 6 || m == 8 || m == 10 || m == 12 || m == 14 || m == 16) {
                                if (Double.parseDouble(rowData[m - 1].toString()) >= Double.parseDouble(rowData[m].toString())) {
                                    Renderer_report.setBackColor(k - 1, m, new Color(45, 219, 109));
                                } else {
                                    Renderer_report.setBackColor(k - 1, m, new Color(243, 99, 99));
                                }
                            }

                            Renderer_report.setBackColor(3, m, new Color(216, 213, 213));
                            Renderer_report.setBackColor(8, m, new Color(216, 213, 213));
                            Renderer_report.setBackColor(10, m, new Color(171, 163, 163));
                        }
                        DataModel_CapacityPlanning.addRow(rowData);

                        try {

                            for (int l = 3; l <= 16; l++) {
                                Double total = Double.parseDouble(TableCapacityPlanning.getValueAt(3, l).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(8, l).toString()) + Double.parseDouble(TableCapacityPlanning.getValueAt(9, l).toString());;
                                TableCapacityPlanning.setValueAt("" + total, 10, l);
                            }
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                        rs.next();
                        k++;
                    }
                }
                DataModel_CapacityPlanning.TableReadOnly(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGridPPRSPlanning() {
        try {
            DataModel_PPRS_Planning = new EITLTableModel();
            tblPlanning1.removeAll();

            tblPlanning1.setModel(DataModel_PPRS_Planning);
            tblPlanning1.setAutoResizeMode(0);

            DataModel_PPRS_Planning.addColumn("SrNo"); //0 - Read Only
            DataModel_PPRS_Planning.addColumn("CONF PPRS MTH"); //0 - Read Only
            DataModel_PPRS_Planning.addColumn("PIECE NO"); //1
            DataModel_PPRS_Planning.addColumn("PARTY CODE"); //5
            DataModel_PPRS_Planning.addColumn("PARTY NAME"); //6
            DataModel_PPRS_Planning.addColumn("MAC NO"); //2
            DataModel_PPRS_Planning.addColumn("POS NO"); //3
            DataModel_PPRS_Planning.addColumn("POS DESC"); //4
            DataModel_PPRS_Planning.addColumn("UPN PIECES"); //11
            DataModel_PPRS_Planning.addColumn("REQ MTH"); //11
            DataModel_PPRS_Planning.addColumn("RESCH. REQ MTH"); //11
            DataModel_PPRS_Planning.addColumn("OC MONTH"); //11
            DataModel_PPRS_Planning.addColumn("CURR SALES PLAN MTH"); //11
            DataModel_PPRS_Planning.addColumn("SPECIAL REQ MTH"); //9
            DataModel_PPRS_Planning.addColumn("SPECIAL REQ DATE"); //9
            DataModel_PPRS_Planning.addColumn("EXP DELIVERY DATE"); //7
            DataModel_PPRS_Planning.addColumn("ACTUAL DELIVERY DATE"); //7
            DataModel_PPRS_Planning.addColumn("EXP PI DATE"); //7
            DataModel_PPRS_Planning.addColumn("ACTUAL PI DATE"); //7
            DataModel_PPRS_Planning.addColumn("EXP PAY/CHQ RCV DATE"); //7
            DataModel_PPRS_Planning.addColumn("ACTUAL PAY/CHQ RCV DATE"); //7
            DataModel_PPRS_Planning.addColumn("EXP DISPATCH DATE"); //7
            DataModel_PPRS_Planning.addColumn("ACTUAL DISPATCH DATE"); //7
            DataModel_PPRS_Planning.addColumn("GROUP"); //8
            DataModel_PPRS_Planning.addColumn("PIECE STAGE"); //9
            DataModel_PPRS_Planning.addColumn("INVOICE VALUE"); //9
            DataModel_PPRS_Planning.addColumn("CONTACT PERSON"); //1
            DataModel_PPRS_Planning.addColumn("PHONE NO"); //1
            DataModel_PPRS_Planning.addColumn("EMAIL ID"); //1
            DataModel_PPRS_Planning.addColumn("EMAIL ID2"); //1
            DataModel_PPRS_Planning.addColumn("EMAIL ID3"); //1
            DataModel_PPRS_Planning.addColumn("LENGTH"); //1
            DataModel_PPRS_Planning.addColumn("WIDTH"); //1
            DataModel_PPRS_Planning.addColumn("GSM"); //1
            DataModel_PPRS_Planning.addColumn("STYLE"); //1
            DataModel_PPRS_Planning.addColumn("WEIGHT"); //1
            DataModel_PPRS_Planning.addColumn("SQMTR"); //1
            DataModel_PPRS_Planning.addColumn("ZONE"); //1

            DataModel_PPRS_Planning.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_PPRS_Planning.SetVariable(1, "SELECT"); //0 - Read Only
            DataModel_PPRS_Planning.SetVariable(2, "PR_PIECE_NO"); //1
            DataModel_PPRS_Planning.SetVariable(3, "PR_PARTY_CODE"); //1
            DataModel_PPRS_Planning.SetVariable(4, "PARTY_NAME"); //1
            DataModel_PPRS_Planning.SetVariable(5, "PR_MACHINE_NO"); //1
            DataModel_PPRS_Planning.SetVariable(6, "PR_POSITION_NO"); //1
            DataModel_PPRS_Planning.SetVariable(7, "POSITION_DESC"); //1
            DataModel_PPRS_Planning.SetVariable(8, "UPN_PIECES"); //1
            DataModel_PPRS_Planning.SetVariable(9, "PR_REQUESTED_MONTH"); //1
            DataModel_PPRS_Planning.SetVariable(10, "RESCHEDULE_MONTH"); //1
            DataModel_PPRS_Planning.SetVariable(11, "OC_MONTH"); //1
            DataModel_PPRS_Planning.SetVariable(12, "CURRENT_SCHEDULE_MONTH"); //1
            DataModel_PPRS_Planning.SetVariable(13, "PR_SPL_REQUEST_MONTHYEAR"); //1
            DataModel_PPRS_Planning.SetVariable(14, "PR_SPL_REQUEST_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(15, "EXP_WIP_DELIVERY_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(16, "ACT_WIP_DELIVERY_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(17, "EXP_PI_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(18, "ACT_PI_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(19, "EXP_PAY_CHQ_RCV_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(20, "ACT_PAY_CHQ_RCV_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(21, "EXP_DISPATCH_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(22, "ACT_DISPATCH_DATE"); //1
            DataModel_PPRS_Planning.SetVariable(23, "PR_GROUP"); //1
            DataModel_PPRS_Planning.SetVariable(24, "PR_PIECE_STAGE"); //1
            DataModel_PPRS_Planning.SetVariable(25, "INVOICE_VALUE"); //1
            DataModel_PPRS_Planning.SetVariable(26, "CONTACT_PERSON"); //1
            DataModel_PPRS_Planning.SetVariable(27, "PHONE_NO"); //1
            DataModel_PPRS_Planning.SetVariable(28, "EMAIL_ID"); //1
            DataModel_PPRS_Planning.SetVariable(29, "EMAIL_ID2"); //1
            DataModel_PPRS_Planning.SetVariable(30, "EMAIL_ID3"); //1
            DataModel_PPRS_Planning.SetVariable(31, "LENGTH"); //1
            DataModel_PPRS_Planning.SetVariable(32, "WIDTH"); //1
            DataModel_PPRS_Planning.SetVariable(33, "GSM"); //1
            DataModel_PPRS_Planning.SetVariable(34, "STYLE"); //1
            DataModel_PPRS_Planning.SetVariable(35, "WEIGHT"); //1
            DataModel_PPRS_Planning.SetVariable(36, "SQMTR"); //1
            DataModel_PPRS_Planning.SetVariable(37, "ZONE"); //1

            for (int i = 0; i < DataModel_PPRS_Planning.getColumnCount(); i++) {
                if (i != 1) {
                    DataModel_PPRS_Planning.SetReadOnly(i);
                }

//                  Disable for Testing only     
                Date date = new Date();
                //if(!(date.getDate()>=11 && date.getDate()<=25))
                if (date.getDate() >= 11 && date.getDate() <= 15 && !LOGIN_USER_TYPE.equals("DESIGN")) {
                    // 29 April 2019
                    DataModel_PPRS_Planning.SetReadOnly(1);
                }

                String selected_month = cmbMonthYear1.getSelectedItem().toString();
                //String allowed_month = addMonth(3, true);

                //CHANGED ON 08/03/2020
                int current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                if (rdoSDF1.isSelected()) {
                    //CHANGED ON 08/03/2020
                    current_allowed_month = EITLERPGLOBAL.getCurrentMonth() + 1;
                    if (date.getDate() <= 10) {
                        current_allowed_month = EITLERPGLOBAL.getCurrentMonth();
                    }

                }
                int current_allowed_year = EITLERPGLOBAL.getCurrentYear();

                if (current_allowed_month > 12) {
                    current_allowed_month = current_allowed_month - 12;
                    current_allowed_year = 1 + current_allowed_year;
                }

                String allowed_month = getMonthName(current_allowed_month) + " - " + current_allowed_year;
                //System.out.println("allowed Month = "+allowed_month);

                if (!(Tab.equals(jPanel3) && selected_month.equals(allowed_month))) {
                //10/10/2021    
                    //    DataModel_Planning.SetReadOnly(1);
                }

                tblPlanning1.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_PPRS_planning);
            }

            tblPlanning1.getColumnModel().getColumn(0).setMaxWidth(40);
            tblPlanning1.getColumnModel().getColumn(1).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(2).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(3).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(4).setMinWidth(150);
            tblPlanning1.getColumnModel().getColumn(5).setMinWidth(70);
            tblPlanning1.getColumnModel().getColumn(6).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(6).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(7).setMinWidth(130);
            tblPlanning1.getColumnModel().getColumn(8).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(9).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(10).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(11).setMinWidth(120);
            tblPlanning1.getColumnModel().getColumn(12).setMinWidth(150);
            tblPlanning1.getColumnModel().getColumn(13).setMinWidth(120);
            tblPlanning1.getColumnModel().getColumn(14).setMinWidth(120);
            tblPlanning1.getColumnModel().getColumn(15).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(15).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(16).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(16).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(17).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(17).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(18).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(18).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(19).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(19).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(20).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(20).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(21).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(21).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(22).setMinWidth(0);
            tblPlanning1.getColumnModel().getColumn(22).setMaxWidth(0);
            tblPlanning1.getColumnModel().getColumn(23).setMinWidth(90);
            tblPlanning1.getColumnModel().getColumn(24).setMinWidth(120);
            tblPlanning1.getColumnModel().getColumn(25).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(26).setMinWidth(130);
            tblPlanning1.getColumnModel().getColumn(27).setMinWidth(120);
            tblPlanning1.getColumnModel().getColumn(28).setMinWidth(130);
            tblPlanning1.getColumnModel().getColumn(29).setMinWidth(130);
            tblPlanning1.getColumnModel().getColumn(30).setMinWidth(130);
            tblPlanning1.getColumnModel().getColumn(31).setMinWidth(90);
            tblPlanning1.getColumnModel().getColumn(32).setMinWidth(90);
            tblPlanning1.getColumnModel().getColumn(33).setMinWidth(90);
            tblPlanning1.getColumnModel().getColumn(34).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(35).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(36).setMinWidth(100);
            tblPlanning1.getColumnModel().getColumn(37).setMinWidth(100);

            EITLTableCellRenderer Renderer_PPRS = new EITLTableCellRenderer();
            int ImportCol = 1;
            Renderer_PPRS.setCustomComponent(ImportCol, "CheckBox");

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

            for (int i = 0; i < DataModel_PPRS_Planning.getColumnCount(); i++) {
                if (i != 1) {
                    DataModel_PPRS_Planning.SetReadOnly(i);
                }

//                  Disable for Testing only     
                Date date = new Date();
                //if(!(date.getDate()>=15 && date.getDate()<=25)) //On 26/05/2019
//                if(!(date.getDate()>=15 && date.getDate()<=26))
                //if((date.getDate()>=11 && date.getDate()<=14)  && !LOGIN_USER_TYPE.equals("SALES"))
                //{
                //29 April 2019
                //    DataModel_Planning.SetReadOnly(1);
                //}

                if (LOGIN_USER_TYPE.equals("DESIGN") || LOGIN_USER_TYPE.equals("OTHER")) {
                    DataModel_PPRS_Planning.SetReadOnly(10);
                    DataModel_PPRS_Planning.SetReadOnly(13);
                    DataModel_PPRS_Planning.SetReadOnly(14);
                }
                if (LOGIN_USER_TYPE.equals("DESIGN") || LOGIN_USER_TYPE.equals("OTHER")) {
                    DataModel_PPRS_Planning.SetReadOnly(15);
                }
            }

            tblPlanning1.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblPlanning1.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer_PPRS);
            tblPlanning1.getColumnModel().getColumn(DataModel_PPRS_Planning.getColFromVariable("UPN_PIECES")).setCellRenderer(this.Renderer);
            tblPlanning1.getColumnModel().getColumn(DataModel_PPRS_Planning.getColFromVariable("PR_PARTY_CODE")).setCellRenderer(this.Renderer_invoiced);

            TableColumn dateColumn_RESCH = tblPlanning1.getColumnModel().getColumn(DataModel_PPRS_Planning.getColFromVariable("RESCHEDULE_MONTH"));
            TableColumn dateColumn_OC_MONTH = tblPlanning1.getColumnModel().getColumn(DataModel_PPRS_Planning.getColFromVariable("OC_MONTH"));
            JComboBox monthbox = new JComboBox();

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
//                month = date.getMonth()+3;
//            }
            month = date.getMonth() + 1;
            if (rdoSDF1.isSelected()) {
                month = date.getMonth();
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

            monthbox.addActionListener(new ActionListener() {//add actionlistner to listen for change
                @Override
                public void actionPerformed(ActionEvent e) {

                        //String s = (String) date1.getSelectedItem();//get the selected item
                    //JOptionPane.showMessageDialog(null, "Event on row "+Table.getSelectedRow()+" col "+Table.getSelectedRow());
                    String OC_MONTH = DataModel_PPRS_Planning.getValueByVariable("OC_MONTH", tblPlanning1.getSelectedRow());
                    if ("".equals(OC_MONTH)) {

                    } else {
                        tblPlanning1.setValueAt("", tblPlanning1.getSelectedRow(), tblPlanning1.getSelectedColumn());
                    }
                }
            });

            dateColumn_RESCH.setCellEditor(new DefaultCellEditor(monthbox));
            dateColumn_OC_MONTH.setCellEditor(new DefaultCellEditor(monthbox));

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void setPPRSPlanningPieces() {
        try {
            if (cmbMonthYear1.getSelectedItem() == null) {
                return;
            }
            FormatGridPPRSPlanning();
//            Renderer.removeBackColors();
            String str_query = "SELECT PR.*,PS.POSITION_DESC,PS.POSITION_DESIGN_NO,PM.CONTACT_PERSON,PM.PHONE_NO,PM.EMAIL,PM.EMAIL_ID2,PM.EMAIL_ID3  FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST PS "
                    + "ON PS.POSITION_NO = PR.PR_POSITION_NO "
                    + "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER PM "
                    + "ON PM.PARTY_CODE=PR.PR_PARTY_CODE "
                    + "WHERE PR.PR_PIECE_NO!='' "
                    + "AND PR_REQ_MTH_LAST_DDMMYY> COALESCE(LAST_DAY(STR_TO_DATE(CONCAT('00 - ','" + cmbMonthYear1.getSelectedItem() + "'), '%d-%b-%Y')),'0000-00-00') "
                    + "AND PR_REQ_MTH_LAST_DDMMYY<= DATE_ADD(COALESCE(LAST_DAY(STR_TO_DATE(CONCAT('00 - ','" + cmbMonthYear1.getSelectedItem() + "'), '%d-%b-%Y')),'0000-00-00'), INTERVAL 2 MONTH) "
                    + "AND COALESCE(PR_PPRS_MONTHYEAR,'')='' "
                    + "AND PR_UPN IN (SELECT DISTINCT U.PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER U WHERE U.PR_OC_MONTHYEAR = '" + cmbMonthYear1.getSelectedItem() + "') "
                    + "AND PR.PR_PIECE_NO NOT IN (SELECT D.PIECE_NO FROM PRODUCTION.FELT_PPRS_PLANNING_HEADER H, PRODUCTION.FELT_PPRS_PLANNING_DETAIL D WHERE H.PPRS_DOC_NO=D.PPRS_DOC_NO AND COALESCE(H.CANCELED,0)=0 ) ";

            if (!cmbProdGroup1.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_GROUP='" + cmbProdGroup1.getSelectedItem() + "'";
            }
            if (!cmbZone1.getSelectedItem().equals("ALL")) {
                str_query = str_query + " AND PR.PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone1.getSelectedItem() + "'");
            }

//            if(!cmbPieceStage.getSelectedItem().equals("ALL"))
//            {   
//                str_query = str_query + " AND PR.PR_PIECE_STAGE = '"+cmbPieceStage.getSelectedItem()+"' ";
//            }
//            
//            
//            if(rdoRequestMonth.isSelected())
//            {
//                str_query = str_query + " AND PR.PR_REQUESTED_MONTH='"+cmbMonthYear.getSelectedItem()+"'";
//                    
//            }
//            else if(rdoOcMonth.isSelected())
//            {
//                str_query = str_query + " AND PR.PR_OC_MONTHYEAR='"+cmbMonthYear.getSelectedItem()+"'";
//                   
//            }
//            else  if(rdoCurSchMonth.isSelected())
//            {
//                str_query = str_query + " AND PR.PR_CURRENT_SCH_MONTH='"+cmbMonthYear.getSelectedItem()+"'";
//                    
//            }
            if (rdoExceptSDF1.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP!='SDF'";
            } else if (rdoSDF1.isSelected()) {
                str_query = str_query + " AND PR.PR_GROUP='SDF'";
            }

            if (!"".equals(txtPartyCode1.getText())) {
                str_query = str_query + " AND PR.PR_PARTY_CODE='" + txtPartyCode1.getText() + "'";
            }

            if (!"".equals(txtUPN1.getText())) {
                str_query = str_query + " AND PR.PR_UPN='" + txtUPN1.getText() + "'";
            }
            String NewPieces = "";
            if (!txtPieceNo1.getText().equals("")) {
                String PieceNo = txtPieceNo1.getText();
                String Pieces[] = PieceNo.split(",");

                for (String Piece : Pieces) {
                    if (NewPieces.equals("")) {
                        NewPieces = "'" + Piece + "'";
                    } else {
                        NewPieces = NewPieces + ",'" + Piece + "'";
                    }
                }
            }

            if (!NewPieces.equals("")) {
                str_query = str_query + " AND PR.PR_PIECE_NO IN (" + NewPieces + ") ";
            }

            str_query = str_query + " AND PR_PIECE_STAGE IN ('BOOKING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL) ";

            str_query = str_query + ORDER_BY_PPRS;

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("PPRS Query : " + str_query);
            ResultSet resultSet = statement.executeQuery(str_query);
            int srNo = 0;
            double TotalInvoiceAmt = 0;
            while (resultSet.next()) {

                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_PPRS_Planning.addRow(rowData);

                DataModel_PPRS_Planning.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_PPRS_Planning.setValueByVariable("SELECT", Boolean.FALSE, NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PR_PIECE_NO", resultSet.getString("PR_PIECE_NO"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PR_MACHINE_NO", resultSet.getString("PR_MACHINE_NO"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PR_POSITION_NO", resultSet.getString("PR_POSITION_NO"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("POSITION_DESC", resultSet.getString("POSITION_DESC"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("UPN_PIECES", resultSet.getString("PR_UPN"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PR_REQUESTED_MONTH", resultSet.getString("PR_REQUESTED_MONTH"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PR_PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(2, resultSet.getString("PR_PARTY_CODE")), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PR_GROUP", resultSet.getString("PR_GROUP"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PR_PIECE_STAGE", resultSet.getString("PR_PIECE_STAGE"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("OC_MONTH", resultSet.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("CURRENT_SCHEDULE_MONTH", resultSet.getString("PR_CURRENT_SCH_MONTH"), NewRow);

                DataModel_PPRS_Planning.setValueByVariable("EXP_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_WIP_DELIVERY_DATE")), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("ACT_WIP_DELIVERY_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_FNSG_DATE")), NewRow);

                //
                DataModel_PPRS_Planning.setValueByVariable("EXP_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PI_DATE")), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("ACT_PI_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PI_DATE")), NewRow);

                DataModel_PPRS_Planning.setValueByVariable("EXP_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_PAY_CHQRC_DATE")), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("ACT_PAY_CHQ_RCV_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_ACT_PAY_CHQRC_DATE")), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("EXP_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_EXP_DESPATCH_DATE")), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("ACT_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_INVOICE_DATE")), NewRow);

                DataModel_PPRS_Planning.setValueByVariable("PR_SPL_REQUEST_MONTHYEAR", resultSet.getString("PR_SPL_REQUEST_MONTHYEAR"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PR_SPL_REQUEST_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PR_SPL_REQUEST_DATE")), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("INVOICE_VALUE", resultSet.getString("PR_FELT_VALUE_WITH_GST"), NewRow);

                DataModel_PPRS_Planning.setValueByVariable("CONTACT_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("PHONE_NO", resultSet.getString("PHONE_NO"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("EMAIL_ID", resultSet.getString("EMAIL"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("EMAIL_ID2", resultSet.getString("EMAIL_ID2"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("EMAIL_ID3", resultSet.getString("EMAIL_ID3"), NewRow);

//                DataModel_Planning.setValueByVariable("LENGTH", resultSet.getString("PR_BILL_LENGTH"), NewRow);
//                DataModel_Planning.setValueByVariable("WIDTH", resultSet.getString("PR_BILL_WIDTH"), NewRow);
//                DataModel_Planning.setValueByVariable("GSM", resultSet.getString("PR_BILL_GSM"), NewRow);
//                DataModel_Planning.setValueByVariable("STYLE", resultSet.getString("PR_BILL_STYLE"), NewRow);
//                DataModel_Planning.setValueByVariable("WEIGHT", resultSet.getString("PR_BILL_WEIGHT"), NewRow);
//                DataModel_Planning.setValueByVariable("SQMTR", resultSet.getString("PR_BILL_SQMTR"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("LENGTH", resultSet.getString("PR_LENGTH"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("WIDTH", resultSet.getString("PR_WIDTH"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("GSM", resultSet.getString("PR_GSM"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("STYLE", resultSet.getString("PR_STYLE"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("WEIGHT", resultSet.getString("PR_THORITICAL_WEIGHT"), NewRow);
                DataModel_PPRS_Planning.setValueByVariable("SQMTR", resultSet.getString("PR_SQMTR"), NewRow);

                try {
                    String incharge = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD=" + resultSet.getString("PR_INCHARGE") + "");
                    DataModel_PPRS_Planning.setValueByVariable("ZONE", incharge, NewRow);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    TotalInvoiceAmt = TotalInvoiceAmt + Double.parseDouble(resultSet.getString("PR_FELT_VALUE_WITH_GST"));
                } catch (Exception e) {
                }

                if (!"".equals(DataModel_PPRS_Planning.getValueByVariable("PR_SPL_REQUEST_MONTHYEAR", NewRow))) {
                    for (int i = 0; i < DataModel_PPRS_Planning.getColumnCount(); i++) {
                        Renderer_PPRS_planning.setBackColor(NewRow, i, Color.yellow);
                    }
                } else {
                    for (int i = 0; i < DataModel_PPRS_Planning.getColumnCount(); i++) {
                        Renderer_PPRS_planning.setBackColor(NewRow, i, Color.white);
                    }
                }

                Renderer.setBackColor(NewRow, DataModel_PPRS_Planning.getColFromVariable("UPN_PIECES"), Color.lightGray);
                Renderer_invoiced.setBackColor(NewRow, DataModel_PPRS_Planning.getColFromVariable("PR_PARTY_CODE"), Color.lightGray);
            }
            //System.out.println("");
            txtTotalPlanning1.setText(BigDecimal.valueOf(EITLERPGLOBAL.round(TotalInvoiceAmt, 2)) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
