/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.SalesFollowup;
//

import EITLERP.AppletFrame;
import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.FeltPieceMaster.FrmPieceMasterDetail;
import EITLERP.FeltSales.Order.FrmFeltOrder;
import EITLERP.FeltSales.Order.clsOrderUPNList;
import EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion;
import EITLERP.FeltSales.PieceRegister.FrmPOPendingOrder;
import EITLERP.FeltSales.PieceRegister.clsIncharge;
import EITLERP.FeltSales.ProductionReport.clsExcelExporter;
import EITLERP.FeltSales.SalesPieceClubbingAmend.frmSalesPieceClubbingAmend;
import EITLERP.FeltSales.SpilloverRescheduling_New.frmPieceReschedulingDetails_New;
import EITLERP.FeltSales.common.LOV;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsVoucher;
import EITLERP.JTextFieldHint;
import EITLERP.Sales.clsSalesInvoice;
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import EITLERP.frmPendingApprovals;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FrmSalesFollowupEntry extends javax.swing.JApplet {

    private EITLTableModel DataModel;
    private EITLTableModel DataModel_cl_piece;
    private EITLTableModel DataModel_History;
    private EITLTableModel DataModel_Clubbed_History;
    private EITLTableModel DataModel_Clubbing;
    private EITLTableModel DataModel_CLB_Master;
    private EITLTableModel DataModel_ClubbingDetail;
    private EITLTableCellRenderer Renderer1 = new EITLTableCellRenderer();
    private EITLTableCellRenderer Renderer_realese = new EITLTableCellRenderer();
    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    public frmPendingApprovals frmPA;
    private EITLComboModel cmbIncharge;
    boolean ClubbingEditRight = false;

//    int USER_ID = EITLERPGLOBAL.gUserID;
    int USER_ID = EITLERPGLOBAL.gNewUserID;
    private clsExcelExporter exp = new clsExcelExporter();
    private clsExcelExporter exp_OFUP = new clsExcelExporter();
    private EITLTableCellRenderer Renderer_COLOR = new EITLTableCellRenderer();

    private EITLTableModel DataModel_OrderFUP;
    private EITLTableCellRenderer RendererUTC_OrderFUP = new EITLTableCellRenderer();
    private EITLComboModel cmbIncharge_OrderFUP;
    private EITLTableCellRenderer Renderer_btn = new EITLTableCellRenderer();
    private EITLTableModel DataModel_OFUP_History;
    private EITLTableCellRenderer Renderer_OFUP11 = new EITLTableCellRenderer();

    /**
     * Initializes the applet FrmFeltOrder
     */
    @Override
    public void init() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
        initComponents();

        lblTitle.setForeground(Color.WHITE);

        FormatGrid();
//        FormatGrid_cl_piece();
        FormatGrid_History();
        FormatGrid_Clubbing_Master();
        FormatGrid_Clubbing();
        FormatGrid_ClubbingDetail();
        FormatGrid_Clubbed_History();
        //FormatGrid_OldTransaction();

        FormatGrid_OrderFUP();
        FormatGrid_OFUP_History();
        DefaultSettings();

        cmbIncharge = new EITLComboModel();
        cmbIncharge_OrderFUP = new EITLComboModel();

        GenerateCombo();
        GenerateOrderFUPAreaCombo();
//        GenerateComboClubbing();
        cmbZone.setEnabled(false);
        cmbZone_OrderFUP.setEnabled(false);
        System.out.println("Edit Right " + ClubbingEditRight);
        SetFields(false);
        //DisplayData();
        Date date = new Date();
        int month = date.getMonth() + 1;
        int year = date.getYear() + 1900;

        txtMonth.setText(month + "");
        txtYear.setText(year + "");

        String month_name = getMonthName(month) + " - " + year;

        txtCurrentSchMonth.setText(month_name);

        txtMonth.setVisible(false);
        txtYear.setVisible(false);
        txtCurrentSchMonth.setVisible(false);

        txtFWLMonth.setText(month + "");
        txtFWLYear.setText(year + "");
        String month_name_FWL = getMonthName(month) + " - " + year;
        txtFWLCurrentSchMonth.setText(month_name_FWL);

        txtOrderFUPMonth.setText(month + "");
        txtOrderFUPYear.setText(year + "");
        String month_name_OrderFUP = getMonthName(month) + " - " + year;
        txtOrderFUP_CPRS_Month.setText(month_name_OrderFUP);

        lblCPRSMonth.setVisible(false);
        txtOrderFUPMonth.setVisible(false);
        txtOrderFUPYear.setVisible(false);
        txtOrderFUP_CPRS_Month.setVisible(false);

        if (USER_ID == 243 || USER_ID == 311 || USER_ID == 28 || USER_ID == 26 || USER_ID == 278) {
            cmbIncharge.setSelectedItem("ALL");
            cmbIncharge_OrderFUP.setSelectedItem("ALL");
            if (USER_ID == 28) {
                btnSave.setEnabled(true);
                btnSave_OrderFUP.setEnabled(true);
                btnPieceDiv_OrderFUP.setEnabled(false);
                btnBooking_OrderFUP.setEnabled(false);
                btnClubbingFollowupSave.setEnabled(true);
                ClubbingEditRight = true;
                btnAddPiece.setEnabled(true);
                btnRemovePiece.setEnabled(true);
                btnUpdateTransaction.setEnabled(true);
                if (USER_ID != 28) {
                    btnClubbingAmend.setEnabled(true);
                }
                //btnExpDateSave.setEnabled(true);
            }
            Table.setEnabled(true);
            Table_OrderFUP.setEnabled(true);
            cmbZone.setEnabled(true);
            cmbZone_OrderFUP.setEnabled(true);
        }
        if (USER_ID == 352)//North: JAYDEEP
        {
            cmbIncharge.setSelectedItem("NORTH");
            cmbIncharge_OrderFUP.setSelectedItem("NORTH");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(false);
        } else if (USER_ID == 136)//East/West: Mr. Jaydeep Pandya
        {
//            cmbIncharge.setSelectedItem("EAST/WEST");//CLOSED ON 17-05-2022
            cmbIncharge.setSelectedItem("NORTH");
//            cmbIncharge_OrderFUP.setSelectedItem("EAST/WEST");//CLOSED ON 17-05-2022
            cmbIncharge_OrderFUP.setSelectedItem("NORTH");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(true);
        } else if (USER_ID == 318)//East/West: Mr. Jaydeep Pandya
        {
            cmbIncharge.setSelectedItem("EAST/WEST");
            cmbIncharge_OrderFUP.setSelectedItem("EAST/WEST");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(true);
        } else if (USER_ID == 394)//ACNE: Bakhtyar Bavaadam
        {
//            cmbIncharge.setSelectedItem("ACNE");//ACNE/KeyClient
//            cmbIncharge.setSelectedItem("ACNE/KEYCLIENT");
//            cmbIncharge_OrderFUP.setSelectedItem("ACNE/KEYCLIENT");
            cmbIncharge.setSelectedItem("EAST/WEST");
            cmbIncharge_OrderFUP.setSelectedItem("EAST/WEST");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(true);
        } else if (USER_ID == 331)//South: Mr. Siddharth NeogiC
        {
            cmbIncharge.setSelectedItem("SOUTH");
            cmbIncharge_OrderFUP.setSelectedItem("SOUTH");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(true);
        } else if (USER_ID == 333)//South: Mr. Aashish Sawant
        {
            cmbIncharge.setSelectedItem("SOUTH");
            cmbIncharge_OrderFUP.setSelectedItem("SOUTH");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(true);
        } else if (USER_ID == 361)//South: Mr. Manoj Gupta
        {
//            cmbIncharge.setSelectedItem("KEY CLIENT");//ACNE/KeyClient
//            cmbIncharge.setSelectedItem("ACNE/KEYCLIENT");
//            cmbIncharge_OrderFUP.setSelectedItem("ACNE/KEYCLIENT");
            cmbIncharge.setSelectedItem("NORTH");
            cmbIncharge_OrderFUP.setSelectedItem("NORTH");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(true);
        } else if (USER_ID == 280)//South: Mr. Manoj Gupta
        {
            Tab.remove(jPanel1);
            Tab.remove(jPanel2);
            Tab.remove(jPanel4);
//            cmbIncharge.setSelectedItem("KEY CLIENT");//ACNE/KeyClient
            cmbIncharge.setSelectedItem("EXPORT");
            cmbIncharge_OrderFUP.setSelectedItem("EXPORT");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(false);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(false);
            ClubbingEditRight = false;
            btnAddPiece.setEnabled(false);
            btnRemovePiece.setEnabled(false);
            btnUpdateTransaction.setEnabled(false);
            btnClubbingAmend.setEnabled(false);
            //btnExpDateSave.setEnabled(true);
        } else if (USER_ID == 409)//South: Mr. Ravi Kotte
        {
            cmbIncharge.setSelectedItem("SOUTH");
            cmbIncharge_OrderFUP.setSelectedItem("SOUTH");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(true);
        } else if (USER_ID == 415)//East/West: Mr. Jaydeep Pandya
        {
            cmbIncharge.setSelectedItem("EAST/WEST");
            cmbIncharge_OrderFUP.setSelectedItem("EAST/WEST");
            cmbZone.setEnabled(false);
            cmbZone_OrderFUP.setEnabled(false);
            btnSave.setEnabled(true);
            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(true);
            ClubbingEditRight = true;
            btnAddPiece.setEnabled(true);
            btnRemovePiece.setEnabled(true);
            btnUpdateTransaction.setEnabled(true);
            btnClubbingAmend.setEnabled(true);
            //btnExpDateSave.setEnabled(true);
        } 
        btnShowOrderStatus.setVisible(false);
        Table.setEnabled(true);
        Table_OrderFUP.setEnabled(true);
        DisplayData(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
        DisplayData_Master(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));

//        DisplayData_OrderFUP(EITLERPGLOBAL.formatDateDB(txtOrderFUPDate.getText()));
        //
        txtHistoryDate.setText(EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT subdate(curdate(), interval 1 day)")));

    }

    public String getMonthName(int month) {
        String month_name = "";
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
        return month_name;
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

        clearFields();
    }

    private void GenerateCombo() {

        HashMap List = new HashMap();
        clsIncharge ObjIncharge;

        cmbZone.setModel(cmbIncharge);
        cmbIncharge.removeAllElements();  //Clearing previous contents

        List = clsIncharge.getIncgargeList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjIncharge = (clsIncharge) List.get(Integer.toString(i));
            if (!ObjIncharge.getAttribute("INCHARGE_NAME").getObj().toString().equals("EXPORT")
                    && !ObjIncharge.getAttribute("INCHARGE_NAME").getObj().toString().equals("ACNE")
                    && !ObjIncharge.getAttribute("INCHARGE_NAME").getObj().toString().equals("KEY CLIENT")) {

                ComboData aData = new ComboData();
                aData.Text = (String) ObjIncharge.getAttribute("INCHARGE_NAME").getObj();
                aData.Code = (long) ObjIncharge.getAttribute("INCHARGE_CD").getVal();
                //if(!ObjIncharge.getAttribute("INCHARGE_NAME").getObj().equals("ALL"))
                //{
                cmbIncharge.addElement(aData);
                //}
            }
        }
    }

//    private void GenerateComboClubbing() {
//
//        
//        Date date = new Date();
//            int month = date.getMonth();
//            int year = date.getYear() + 1900;
//            String month_name = "";
//            //cmbClubbing.addElement("");
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
//                cmbCurrSchMonthClubbing.addItem(month_name + " - " + year);
//                //cmbClubbing.addElement(month_name + " - " + year);
//                //System.out.println(month_name + " - " + year);
//            }
//        
//    }
    private void clearFields() {

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

    private void clearFields_Clubbing() {

        for (int i = 0; i < DataModel_CLB_Master.getRowCount(); i++) {
            DataModel_CLB_Master.removeRow(i);
        }
        if (DataModel_CLB_Master.getRowCount() > 0) {
            DataModel_CLB_Master.removeRow(0);
        }
        Object[] rowData = new Object[15];
        rowData[0] = 1;
        DataModel_CLB_Master.addRow(rowData);
    }

    private void DisplayData(String FollowDate) {
        try {
            clearFields();
            FormatGrid();

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String InchargeCode = data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");

//            String qry = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR,DINESHMILLS.D_SAL_PARTY_MASTER P where "
//                       + "  PR_PIECE_STAGE='IN STOCK'  AND PR_CURRENT_SCH_MONTH='"+txtCurrentSchMonth.getText()+"' AND PR.PR_PARTY_CODE=P.PARTY_CODE  " +
//                         "  AND coalesce(PR.PR_DELINK,'')!='OBSOLETE' ";
//            System.out.println("qry "+qry);
//            //String qry = " SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR where PR_PIECE_STAGE='IN STOCK'  AND PR_CURRENT_SCH_MONTH='"+month_name+"'";
//            
            Date date = new Date();

            int month = date.getMonth() + 1;
            int year = date.getYear() + 1900;

            if (EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText())) != month) {
                month = EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
                year = EITLERPGLOBAL.getYear(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
            }
            String qry = "SELECT D.* FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL D where DOC_MONTH=" + month + " and DOC_YEAR=" + year + ""
                    + " AND coalesce(PIECE_OBSOLETE,'')!='OBSOLETE' AND CUR_PIECE_STAGE IN ('IN STOCK','BSR')  "
                    + " AND  D.PIECE_NO  NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE CURRENT_STATUS='Active')  "
                    + " AND D.PIECE_NO IN (SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_CURRENT_SCH_MONTH='" + txtCurrentSchMonth.getText() + "')"
                    + "";

//            if(!cmbZone.isEnabled())
//            {
//                qry = qry + " AND EXPECTED_DISPATCH_DATE<='"+FollowDate+"' ";
//            }
//            if (USER_ID == 243 || USER_ID == 311 || USER_ID == 28 || USER_ID == 26 || USER_ID == 278) 
//            {
//              if(!EITLERPGLOBAL.getCurrentDateDB().equals(FollowDate))
//              {
//                  qry = qry + " AND EXPECTED_DISPATCH_DATE<='"+FollowDate+"' ";
//              }
//            }
            if (EITLERPGLOBAL.getCurrentDateDB().equals(FollowDate)) {
                qry = qry + " AND (EXPECTED_DISPATCH_DATE='" + FollowDate + "' OR EXPECTED_DISPATCH_DATE='0000-00-00') ";
            } else {
                qry = qry + " AND EXPECTED_DISPATCH_DATE='" + FollowDate + "' ";
            }

            if (!cmbZone.getSelectedItem().equals("ALL")) {
//                if (USER_ID == 361 || USER_ID == 394) {
//                    qry = qry + " AND D.INCHARGE IN ('5','7') ";
//                } else {
                qry = qry + " AND D.INCHARGE='" + InchargeCode + "'";
//                }
            } else {
                qry = qry + " AND D.INCHARGE!='6'"; // REMOVE EXPORT Pieces
            }

            //DOC_MONTH=" + month + " and DOC_YEAR=" + year + ""
            qry = qry + " UNION ALL SELECT * FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_NEXTMONTH ND WHERE ND.EXPECTED_DISPATCH_DATE='" + FollowDate + "'  ";//AND ND.DOC_MONTH!='" + month + "' AND ND.DOC_YEAR!='" + year + "' ";

            if (!cmbZone.getSelectedItem().equals("ALL")) {
//                if (USER_ID == 361 || USER_ID == 394) {
//                    qry = qry + " AND D.INCHARGE IN ('5','7') ";
//                } else {
                qry = qry + " AND ND.INCHARGE='" + InchargeCode + "'";
//                }
            } else {
                qry = qry + " AND ND.INCHARGE!='6'"; // REMOVE EXPORT Pieces
            }
            
            qry = qry + "  ORDER BY PARTY_CODE,CUR_PIECE_STAGE ";
//            System.out.println(" qry " + qry);
            System.out.println("qry *** " + qry);
            ResultSet resultSet = statement.executeQuery(qry);
            int srNo = 0;

            while (resultSet.next()) {
                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel.addRow(rowData);

                DataModel.setValueByVariable("SRNO", srNo + "", NewRow);
                DataModel.setValueByVariable("SELECT", false, NewRow);
                DataModel.setValueByVariable("PIECE_NO", resultSet.getString("PIECE_NO"), NewRow);
                DataModel.setValueByVariable("PARTY_CODE", resultSet.getString("PARTY_CODE"), NewRow);
                DataModel.setValueByVariable("PARTY_NAME", resultSet.getString("PARTY_NAME"), NewRow);
                DataModel.setValueByVariable("OC_MONTH", resultSet.getString("OC_MONTH"), NewRow);
                DataModel.setValueByVariable("CURRENT_SCH_MONTH", resultSet.getString("CURR_SCH_MONTH"), NewRow);
                DataModel.setValueByVariable("FINISHING_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("FINISHING_DATE")), NewRow);
                DataModel.setValueByVariable("PIECE_STAGE", resultSet.getString("PIECE_STAGE"), NewRow);
                DataModel.setValueByVariable("CURRENT_PIECE_STAGE", resultSet.getString("CUR_PIECE_STAGE"), NewRow);
                DataModel.setValueByVariable("WH_DAYS", data.getStringValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.getCurrentDateDB() + "','" + resultSet.getString("FINISHING_DATE") + "')"), NewRow);
                DataModel.setValueByVariable("EXPECTED_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("EXPECTED_DISPATCH_DATE")), NewRow);
                DataModel.setValueByVariable("NEW_EXPECTED_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("FOLLOWUP_DATE")), NewRow);

                String Disc_Per = "";

                try {

                    String CurDate = EITLERPGLOBAL.getCurrentDateDB();
                    String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + resultSet.getString("PARTY_CODE") + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
                    if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + resultSet.getString("PARTY_CODE") + " AND PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                        if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + resultSet.getString("BILL_PRODUCT_CODE") + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                            try {
                                Connection Conn;
                                Statement stmt;
                                ResultSet rsData;

                                Conn = data.getConn();
                                stmt = Conn.createStatement();
                                // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                                //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                                //System.out.println("* Query SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
                                rsData = stmt.executeQuery("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + resultSet.getString("BILL_PRODUCT_CODE") + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                                rsData.first();
                                Disc_Per = rsData.getString("DISC_PER");
                            } catch (Exception e) {
                                Disc_Per = "0";
                            }
                        }

                        if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + resultSet.getString("PARTY_CODE") + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + resultSet.getString("BILL_PRODUCT_CODE") + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                            try {
                                Connection Conn;
                                Statement stmt;
                                ResultSet rsData;

                                Conn = data.getConn();
                                stmt = Conn.createStatement();
                                rsData = stmt.executeQuery("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + resultSet.getString("PARTY_CODE") + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + resultSet.getString("BILL_PRODUCT_CODE") + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                                rsData.first();
                                Disc_Per = rsData.getString("DISC_PER");
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        try {
                            Connection Conn;
                            Statement stmt;
                            ResultSet rsData;

                            Conn = data.getConn();
                            stmt = Conn.createStatement();
                            // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                            //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                            //System.out.println("Find Discount Query : SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");    
                            rsData = stmt.executeQuery("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + resultSet.getString("PARTY_CODE") + " AND PIECE_NO = '" + resultSet.getString("PIECE_NO") + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                            rsData.first();
                            Disc_Per = rsData.getString("DISC_PER");

                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                DataModel.setValueByVariable("DISCOUNT_PER", Disc_Per + "", NewRow);

                DataModel.setValueByVariable("RESCHEDULE_COUNT", resultSet.getString("DATE_COUNTER_MONTH"), NewRow);
                String ChargeCode = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='" + resultSet.getString("PARTY_CODE") + "'");
                DataModel.setValueByVariable("CHARGE_CODE", ChargeCode, NewRow);

                //DataModel.setValueByVariable("BALANCE", "", NewRow);
                DataModel.setValueByVariable("REMARK", resultSet.getString("DELAY_REASON"), NewRow);
                DataModel.setValueByVariable("INVOICE_VALUE", resultSet.getString("INVOICE_VALUE"), NewRow);

                if ("1".equals(resultSet.getString("UNABLE_TO_CONTACT"))) {
                    DataModel.setValueByVariable("UNABLE_TO_CONTACT", true, NewRow);
                } else {
                    DataModel.setValueByVariable("UNABLE_TO_CONTACT", false, NewRow);
                }

                DataModel.setValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", resultSet.getString("PARTY_LIFTING_COMMITMENT_STATUS"), NewRow);
                DataModel.setValueByVariable("PARTY_LIFTING_COMMITMENT_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PARTY_LIFTING_COMMITMENT_DATE")), NewRow);
                
                DataModel.setValueByVariable("CONTACTED_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel.setValueByVariable("CONTACTED_NO", resultSet.getString("CONTACTED_NO"), NewRow);
                DataModel.setValueByVariable("ADDITIONAL_REMARK", resultSet.getString("ADDITIONAL_REMARK"), NewRow);

                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL where PIECE_NO='" + resultSet.getString("PIECE_NO") + "'")) {
                    DataModel.setValueByVariable("PIECE_CLUBBING", true, NewRow);
                } else {
                    DataModel.setValueByVariable("PIECE_CLUBBING", false, NewRow);
                }
                DataModel.setValueByVariable("DATE_OF_COMMUNICATION", EITLERPGLOBAL.formatDate(resultSet.getString("DATE_OF_COMMUNICATION")), NewRow);
                DataModel.setValueByVariable("MODE_OF_COMMUNICATION", resultSet.getString("MODE_OF_COMMUNICATION"), NewRow);

                //if (EITLERPGLOBAL.getCurrentDateDB().equals(FollowDate)) {
//                String Previous_Party_Justification = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_JUSTIFICATION)) AS PARTY_JUSTIFICATION FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY \n"
//                        + "WHERE \n"
//                        + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND PARTY_JUSTIFICATION!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
//                String Previous_Area_Manager_Comments = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',AREA_MANAGER_COMMENT)) AREA_MANAGER_COMMENT FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY \n"
//                        + "WHERE \n"
//                        + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND AREA_MANAGER_COMMENT!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                /*
                 String Previous_Party_Justification = data.getStringValueFromDB("SELECT PARTY_JUSTIFICATION FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY \n" +
                 "WHERE \n" +
                 " PIECE_NO='"+resultSet.getString("PIECE_NO")+"' AND PARTY_JUSTIFICATION!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                 String Previous_Area_Manager_Comments = data.getStringValueFromDB("SELECT AREA_MANAGER_COMMENT FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY \n" +
                 "WHERE \n" +
                 " PIECE_NO='"+resultSet.getString("PIECE_NO")+"' AND AREA_MANAGER_COMMENT!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                 */

//                DataModel.setValueByVariable("PREVIOUS_PARTY_JUSTIFICATION", Previous_Party_Justification, NewRow);
//                DataModel.setValueByVariable("PREVIOUS_AREA_MANAGER_COMMENT", Previous_Area_Manager_Comments, NewRow);
                DataModel.setValueByVariable("PREVIOUS_PARTY_JUSTIFICATION", resultSet.getString("PREVIOUS_PARTY_JUSTIFICATION"), NewRow);
                DataModel.setValueByVariable("PREVIOUS_AREA_MANAGER_COMMENT", resultSet.getString("PREVIOUS_AREA_MANAGER_COMMENT"), NewRow);
                //}

//                    String Previous_Lifting_Status = "";
//                    String Previous_LiftingDate = "";
//                    if(data.IsRecordExist("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_LIFTING_COMMITMENT_STATUS)) AS PARTY_LIFTING_COMMITMENT_STATUS FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY \n"
//                            + "WHERE \n"
//                            + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND PARTY_LIFTING_COMMITMENT_STATUS!='' ORDER BY ENTRY_DATETIME"))
//                    {
//                        Previous_Lifting_Status = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_LIFTING_COMMITMENT_STATUS)) AS PARTY_LIFTING_COMMITMENT_STATUS FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY \n"
//                                + "WHERE \n"
//                                + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND PARTY_LIFTING_COMMITMENT_STATUS!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
//                        Previous_LiftingDate = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_LIFTING_COMMITMENT_DATE)) PARTY_LIFTING_COMMITMENT_DATE FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY \n"
//                                + "WHERE \n"
//                                + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND (PARTY_LIFTING_COMMITMENT_DATE!='' OR PARTY_LIFTING_COMMITMENT_DATE!='0000-00-00') ORDER BY ENTRY_DATETIME DESC LIMIT 1");
//                    }
                    
                    
//                    DataModel.setValueByVariable("PREVIOUS_PARTY_LIFTING_COMMITMENT_STATUS", Previous_Lifting_Status, NewRow);
//                    DataModel.setValueByVariable("PREVIOUS_COMMITMENT_DATE", Previous_LiftingDate, NewRow);
                    DataModel.setValueByVariable("PREVIOUS_PARTY_LIFTING_COMMITMENT_STATUS", resultSet.getString("PREVIOUS_PARTY_LIFTING_COMMITMENT_STATUS"), NewRow);
                    DataModel.setValueByVariable("PREVIOUS_COMMITMENT_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PREVIOUS_COMMITMENT_DATE")), NewRow);
                                        
                
                DataModel.setValueByVariable("PARTY_JUSTIFICATION", resultSet.getString("PARTY_JUSTIFICATION"), NewRow);
                DataModel.setValueByVariable("AREA_MANAGER_COMMENT", resultSet.getString("AREA_MANAGER_COMMENT"), NewRow);
                DataModel.setValueByVariable("EXP_DISPATCH_HISTORY", resultSet.getString("EXP_DISPATCH_HISTORY"), NewRow);
                DataModel.setValueByVariable("DATE_COUNTER_MONTH", resultSet.getString("DATE_COUNTER_MONTH"), NewRow);
                DataModel.setValueByVariable("RESCHEDULED_MONTH_HISTORY", resultSet.getString("RESCHEDULED_MONTH_HISTORY"), NewRow);
                DataModel.setValueByVariable("RESCHEDULED_MONTH_COUNT", resultSet.getString("RESCHEDULED_MONTH_COUNT"), NewRow);

                DataModel.setValueByVariable("EXPECTED_MONTH_OF_DISPATCH", resultSet.getString("EXPECTED_MONTH_OF_DISPATCH"), NewRow);
//                if (!"".equals(EITLERPGLOBAL.formatDate(resultSet.getString("FOLLOWUP_DATE")))) {
//                    String Followupdate = resultSet.getString("FOLLOWUP_DATE");
//                    int Month = EITLERPGLOBAL.getMonth(Followupdate);
//                    int Year = EITLERPGLOBAL.getYear(Followupdate);
//                    String Month_Name = getMonthName(Month);
//                    String selected_month = Month_Name + " - " + Year;
//
//                    DataModel.setValueByVariable("EXPECTED_MONTH_OF_DESPATCH", selected_month, NewRow);
//
//                } else {
//                    
//                }

                if (!resultSet.getString("CUR_PIECE_STAGE").equals("IN STOCK") && !resultSet.getString("CUR_PIECE_STAGE").equals("BSR")) {
                    for (int i = 0; i < DataModel.getColumnCount(); i++) {
                        // Renderer_COLOR.setBackColor(String.valueOf(NewRow), String.valueOf(i), Color.green);
                    }
                }
            }
//            for(int i=0;i<Table.getRowCount();i++)
//            {
//                if(!DataModel.getValueByVariable("CURRENT_PIECE_STAGE", i).toString().equalsIgnoreCase("IN STOCK")
//                && !DataModel.getValueByVariable("CURRENT_PIECE_STAGE", i).toString().equalsIgnoreCase("BSR"))
//                {
//                    
//                    for(int j=0;j<DataModel.getColumnCount();j++)
//                    {
//                        Renderer_COLOR.setBackColor(i, j, Color.green);
//                    }
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DisplayData_Master(String FollowDate) {
        try {
            clearFields_Clubbing();
            FormatGrid_Clubbing_Master();

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String InchargeCode = data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");

            Date date = new Date();

            int month = date.getMonth() + 1;
            int year = date.getYear() + 1900;

            String selected_monthyear = txtCurrentSchMonth.getText();
            if (EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText())) != month) {
                month = EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
                year = EITLERPGLOBAL.getYear(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
                selected_monthyear = getMonthName(month) + " - " + year;

            }
            String qry = "SELECT * FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL D where EXPECTED_MONTH_OF_DISPATCH='" + txtFWLCurrentSchMonth.getText() + "' AND CURRENT_STATUS='Active'  ";

            System.out.println("*************");
            System.out.println("" + qry);
            try {
                if (EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText())) == Integer.parseInt(txtFWLMonth.getText())) {
                    if (txtFollowupDate.getText().startsWith("01/")) {
                        qry = qry + " AND (EXPECTED_DISPATCH_DATE='" + FollowDate + "' OR EXPECTED_DISPATCH_DATE='0000-00-00') ";
                    } else {
                        qry = qry + " AND (EXPECTED_DISPATCH_DATE='" + FollowDate + "') ";
                    }
                }
            } catch (Exception e) {

            }
            //Closed on 13/12/2021
//            if (!cmbZone.getSelectedItem().equals("ALL")) {
//                
//                    qry = qry + " AND D.INCHARGE='" + InchargeCode + "'";
//
//            } else {
//                qry = qry + " AND D.INCHARGE!='6'"; // REMOVE EXPORT Pieces
//            }

            if (InchargeCode.equals("11")) {
                qry = qry + " AND D.INCHARGE IN ('7','5')";
            } else if (!cmbZone.getSelectedItem().equals("ALL")) {
                qry = qry + " AND D.INCHARGE='" + InchargeCode + "'";
            } else {
                qry = qry + " AND D.INCHARGE!='6'"; // REMOVE EXPORT Pieces
            }

//            System.out.println(" qry " + qry);
            System.out.println("qry **CLB " + qry);
            ResultSet resultSet = statement.executeQuery(qry);
            int srNo = 0;

            while (resultSet.next()) {
                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_CLB_Master.addRow(rowData);

                //
                DataModel_CLB_Master.setValueByVariable("SrNo", srNo + "", NewRow);
                DataModel_CLB_Master.setValueByVariable("CLUBBING_NO", resultSet.getString("DOC_NO"), NewRow);
                DataModel_CLB_Master.setValueByVariable("PIECE_NO", resultSet.getString("PIECE_NO"), NewRow);
                DataModel_CLB_Master.setValueByVariable("PARTY_CODE", resultSet.getString("PARTY_CODE"), NewRow);
                DataModel_CLB_Master.setValueByVariable("PARTY_NAME", resultSet.getString("PARTY_NAME"), NewRow);
                String LAST_OC_MONTH = data.getStringValueFromDB("SELECT LAST_OC_MONTH FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER where PC_DOC_NO='" + resultSet.getString("DOC_NO") + "'");
                String LAST_CURR_SCH_MONTH = data.getStringValueFromDB("SELECT LAST_CURR_SCH_MONTH FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER where PC_DOC_NO='" + resultSet.getString("DOC_NO") + "'");
                DataModel_CLB_Master.setValueByVariable("NO_OF_PIECE", "" + data.getStringValueFromDB("SELECT count(*) as NO_OF_RECORDS FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL where PC_DOC_NO='" + resultSet.getString("DOC_NO") + "' AND CURRENT_STATUS='Active'"), NewRow);
                DataModel_CLB_Master.setValueByVariable("TOTAL_INVOICE_VALUE", "" + data.getStringValueFromDB("SELECT SUM(PR_FELT_VALUE_WITH_GST) AS TOTAL_INVOICE_VALUE FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D,PRODUCTION.FELT_SALES_PIECE_REGISTER PR WHERE D.PIECE_NO=PR.PR_PIECE_NO AND PC_DOC_NO='" + resultSet.getString("DOC_NO") + "' group by PC_DOC_NO"), NewRow);
                DataModel_CLB_Master.setValueByVariable("LAST_OC_MONTH", LAST_OC_MONTH, NewRow);
                DataModel_CLB_Master.setValueByVariable("LAST_CURR_SCH_MONTH", LAST_CURR_SCH_MONTH, NewRow);
                DataModel_CLB_Master.setValueByVariable("EXPECTED_MONTH_OF_DISPATCH", resultSet.getString("EXPECTED_MONTH_OF_DISPATCH"), NewRow);

                DataModel_CLB_Master.setValueByVariable("NEW_EXPECTED_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("FOLLOWUP_DATE")), NewRow);
                DataModel_CLB_Master.setValueByVariable("REMARK", resultSet.getString("DELAY_REASON"), NewRow);

                if ("1".equals(resultSet.getString("UNABLE_TO_CONTACT"))) {
                    DataModel_CLB_Master.setValueByVariable("UNABLE_TO_CONTACT", true, NewRow);
                } else {
                    DataModel_CLB_Master.setValueByVariable("UNABLE_TO_CONTACT", false, NewRow);
                }

                DataModel_CLB_Master.setValueByVariable("CONTACTED_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_CLB_Master.setValueByVariable("CONTACTED_NO", resultSet.getString("CONTACTED_NO"), NewRow);
                DataModel_CLB_Master.setValueByVariable("DATE_OF_COMMUNICATION", EITLERPGLOBAL.formatDate(resultSet.getString("DATE_OF_COMMUNICATION")), NewRow);
                DataModel_CLB_Master.setValueByVariable("MODE_OF_COMMUNICATION", resultSet.getString("MODE_OF_COMMUNICATION"), NewRow);
                DataModel_CLB_Master.setValueByVariable("PARTY_JUSTIFICATION", resultSet.getString("PARTY_JUSTIFICATION"), NewRow);
                DataModel_CLB_Master.setValueByVariable("AREA_MANAGER_COMMENT", resultSet.getString("AREA_MANAGER_COMMENT"), NewRow);

                DataModel_CLB_Master.setValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", resultSet.getString("PARTY_LIFTING_COMMITMENT_STATUS"), NewRow);
                DataModel_CLB_Master.setValueByVariable("PARTY_LIFTING_COMMITMENT_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PARTY_LIFTING_COMMITMENT_DATE")), NewRow);

                //DataModel_CLB_Master.SetVariable(23, "");
                //DataModel_CLB_Master.SetVariable(24, "");
                if (EITLERPGLOBAL.getCurrentDateDB().equals(FollowDate)) {
                    String Previous_Party_Justification = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_JUSTIFICATION)) AS PARTY_JUSTIFICATION FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY \n"
                            + "WHERE \n"
                            + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND PARTY_JUSTIFICATION!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                    String Previous_Area_Manager_Comments = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',AREA_MANAGER_COMMENT)) AREA_MANAGER_COMMENT FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY \n"
                            + "WHERE \n"
                            + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND AREA_MANAGER_COMMENT!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                    DataModel_CLB_Master.setValueByVariable("PREVIOUS_PARTY_JUSTIFICATION", Previous_Party_Justification, NewRow);
                    DataModel_CLB_Master.setValueByVariable("PREVIOUS_AREA_MANAGER_COMMENT", Previous_Area_Manager_Comments, NewRow);
                    
                    String Previous_Lifting_Status = "";
                    String Previous_LiftingDate = "";
                    if(data.IsRecordExist("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_LIFTING_COMMITMENT_STATUS)) AS PARTY_LIFTING_COMMITMENT_STATUS FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY \n"
                            + "WHERE \n"
                            + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND PARTY_LIFTING_COMMITMENT_STATUS!='' ORDER BY ENTRY_DATETIME"))
                    {
                        Previous_Lifting_Status = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_LIFTING_COMMITMENT_STATUS)) AS PARTY_LIFTING_COMMITMENT_STATUS FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY \n"
                                + "WHERE \n"
                                + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND PARTY_LIFTING_COMMITMENT_STATUS!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                        Previous_LiftingDate = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_LIFTING_COMMITMENT_DATE)) PARTY_LIFTING_COMMITMENT_DATE FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY \n"
                                + "WHERE \n"
                                + " PIECE_NO='" + resultSet.getString("PIECE_NO") + "' AND (PARTY_LIFTING_COMMITMENT_DATE!='' OR PARTY_LIFTING_COMMITMENT_DATE!='0000-00-00') ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                    }
                    
                    
                    DataModel_CLB_Master.setValueByVariable("PREVIOUS_PARTY_LIFTING_COMMITMENT_STATUS", Previous_Lifting_Status, NewRow);
                    DataModel_CLB_Master.setValueByVariable("PREVIOUS_COMMITMENT_DATE", Previous_LiftingDate, NewRow);
                    
                    /*
                     String Previous_Party_Justification = data.getStringValueFromDB("SELECT PARTY_JUSTIFICATION FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY \n" +
                     "WHERE \n" +
                     " PIECE_NO='"+resultSet.getString("PIECE_NO")+"' AND PARTY_JUSTIFICATION!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                     String Previous_Area_Manager_Comments = data.getStringValueFromDB("SELECT AREA_MANAGER_COMMENT FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY \n" +
                     "WHERE \n" +
                     " PIECE_NO='"+resultSet.getString("PIECE_NO")+"' AND AREA_MANAGER_COMMENT!='' ORDER BY ENTRY_DATETIME DESC LIMIT 1");
                     */

                    
                    
                }

                //EXPECTED_MONTH_OF_DISPATCH
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

            DataModel.addColumn("SrNo"); //0
            DataModel.addColumn("SELECT"); //1
            DataModel.addColumn("Piece No"); //2
            DataModel.addColumn("Party Code"); //3
            DataModel.addColumn("Party Name"); //4
            DataModel.addColumn("Charge Code"); //5
            DataModel.addColumn("OC Month"); //6
            DataModel.addColumn("Cur Sch Month");// 7
            DataModel.addColumn("Piece Stage");//8
            DataModel.addColumn("Current Piece Stage");//9
            DataModel.addColumn("Finishing Date"); //10
            DataModel.addColumn("Ageing INSTOCK"); //11
            DataModel.addColumn("Discount(%)"); //12
            DataModel.addColumn("Invoice Value"); //13
            DataModel.addColumn("Next Follow Up Date");//14
            DataModel.addColumn("Follow Up Date");//15
            DataModel.addColumn("Follow Up Count");//16
            DataModel.addColumn("Delay Reason");//17
            DataModel.addColumn("Unable to Contact");//18
            DataModel.addColumn("Contacted Person");//19
            DataModel.addColumn("Contacted Contact No");//20
            DataModel.addColumn("Date of Communication");//21
            DataModel.addColumn("Mode of Communication");//22
            DataModel.addColumn("Prevoius Party Justification");//23
            DataModel.addColumn("Party Justification");//23
            DataModel.addColumn("Prevoius Area Manager Comments");//24
            DataModel.addColumn("Area Manager Comments");//24
            DataModel.addColumn("Piece Clubbing");//25
            DataModel.addColumn("Additional Remark");//26
            DataModel.addColumn("Exp Dispatch History");//27
            DataModel.addColumn("Date Counter");//28
            DataModel.addColumn("Rescheduled Month History");//29
            DataModel.addColumn("Rescheduled Month Count");//30
            DataModel.addColumn("Expected Month of Despatch");//30
            DataModel.addColumn("Party Lifting Commitment Status");//30
            DataModel.addColumn("Commitment Date");//30
            DataModel.addColumn("Prevoius Party Lifting Commitment Status");//30
            DataModel.addColumn("Prevoius Commitment Date");//30

            DataModel.SetVariable(0, "SRNO");
            DataModel.SetVariable(1, "SELECT");
            DataModel.SetVariable(2, "PIECE_NO");
            DataModel.SetVariable(3, "PARTY_CODE");
            DataModel.SetVariable(4, "PARTY_NAME");
            DataModel.SetVariable(5, "CHARGE_CODE");
            DataModel.SetVariable(6, "OC_MONTH");
            DataModel.SetVariable(7, "CURRENT_SCH_MONTH");
            DataModel.SetVariable(8, "PIECE_STAGE");
            DataModel.SetVariable(9, "CURRENT_PIECE_STAGE");
            DataModel.SetVariable(10, "FINISHING_DATE");
            DataModel.SetVariable(11, "WH_DAYS");
            DataModel.SetVariable(12, "DISCOUNT_PER");
            DataModel.SetVariable(13, "INVOICE_VALUE");
            DataModel.SetVariable(14, "NEW_EXPECTED_DISPATCH_DATE");
            DataModel.SetVariable(15, "EXPECTED_DISPATCH_DATE");
            DataModel.SetVariable(16, "RESCHEDULE_COUNT");
            DataModel.SetVariable(17, "REMARK");
            DataModel.SetVariable(18, "UNABLE_TO_CONTACT");
            DataModel.SetVariable(19, "CONTACTED_PERSON");
            DataModel.SetVariable(20, "CONTACTED_NO");
            DataModel.SetVariable(21, "DATE_OF_COMMUNICATION");
            DataModel.SetVariable(22, "MODE_OF_COMMUNICATION");
            DataModel.SetVariable(23, "PREVIOUS_PARTY_JUSTIFICATION");
            DataModel.SetVariable(24, "PARTY_JUSTIFICATION");
            DataModel.SetVariable(25, "PREVIOUS_AREA_MANAGER_COMMENT");
            DataModel.SetVariable(26, "AREA_MANAGER_COMMENT");
            DataModel.SetVariable(27, "PIECE_CLUBBING");
            DataModel.SetVariable(28, "ADDITIONAL_REMARK");
            DataModel.SetVariable(29, "EXP_DISPATCH_HISTORY");
            DataModel.SetVariable(30, "DATE_COUNTER_MONTH");
            DataModel.SetVariable(31, "RESCHEDULED_MONTH_HISTORY");
            DataModel.SetVariable(32, "RESCHEDULED_MONTH_COUNT");
            DataModel.SetVariable(33, "EXPECTED_MONTH_OF_DISPATCH");
            DataModel.SetVariable(34, "PARTY_LIFTING_COMMITMENT_STATUS");
            DataModel.SetVariable(35, "PARTY_LIFTING_COMMITMENT_DATE");
            DataModel.SetVariable(36, "PREVIOUS_PARTY_LIFTING_COMMITMENT_STATUS");
            DataModel.SetVariable(37, "PREVIOUS_COMMITMENT_DATE");
            //
            Table.getColumnModel().getColumn(0).setMaxWidth(40);
            Table.getColumnModel().getColumn(1).setMaxWidth(60);
            Table.getColumnModel().getColumn(2).setMinWidth(90);
            Table.getColumnModel().getColumn(3).setMinWidth(80);
            Table.getColumnModel().getColumn(4).setMinWidth(150);
            Table.getColumnModel().getColumn(5).setMinWidth(80);
            Table.getColumnModel().getColumn(6).setMinWidth(100);
            Table.getColumnModel().getColumn(7).setMinWidth(100);
            Table.getColumnModel().getColumn(8).setMinWidth(100);
            Table.getColumnModel().getColumn(9).setMinWidth(100);
            Table.getColumnModel().getColumn(10).setMinWidth(110);
            Table.getColumnModel().getColumn(11).setMinWidth(100);
            Table.getColumnModel().getColumn(12).setMinWidth(100);
            Table.getColumnModel().getColumn(13).setMinWidth(100);
            Table.getColumnModel().getColumn(14).setMinWidth(170);

            //Follow Up Date Hide
            Table.getColumnModel().getColumn(15).setMinWidth(0);
            Table.getColumnModel().getColumn(15).setMaxWidth(0);

            Table.getColumnModel().getColumn(16).setMinWidth(0);
            Table.getColumnModel().getColumn(16).setMaxWidth(0);
            Table.getColumnModel().getColumn(17).setMinWidth(250);
            Table.getColumnModel().getColumn(18).setMinWidth(150);
            Table.getColumnModel().getColumn(19).setMinWidth(150);
            Table.getColumnModel().getColumn(20).setMinWidth(150);
            Table.getColumnModel().getColumn(21).setMinWidth(0);
            Table.getColumnModel().getColumn(21).setMaxWidth(0);
            Table.getColumnModel().getColumn(22).setMinWidth(150);
            Table.getColumnModel().getColumn(23).setMinWidth(230);
            Table.getColumnModel().getColumn(24).setMinWidth(280);
            Table.getColumnModel().getColumn(25).setMinWidth(230);
            Table.getColumnModel().getColumn(26).setMinWidth(280);
            Table.getColumnModel().getColumn(27).setMinWidth(100);
            Table.getColumnModel().getColumn(28).setMinWidth(150);
            Table.getColumnModel().getColumn(29).setMinWidth(0);
            Table.getColumnModel().getColumn(29).setMaxWidth(0);
            Table.getColumnModel().getColumn(30).setMinWidth(0);
            Table.getColumnModel().getColumn(30).setMaxWidth(0);

            Table.getColumnModel().getColumn(31).setMinWidth(250);
            Table.getColumnModel().getColumn(32).setMinWidth(180);
            Table.getColumnModel().getColumn(33).setMinWidth(250);
            Table.getColumnModel().getColumn(34).setMinWidth(250);
            Table.getColumnModel().getColumn(35).setMinWidth(180);
            Table.getColumnModel().getColumn(36).setMinWidth(280);
            Table.getColumnModel().getColumn(37).setMinWidth(180);
            
            Table.getTableHeader().setReorderingAllowed(false);

            DataModel.SetReadOnly(0);
            DataModel.SetReadOnly(2);
            DataModel.SetReadOnly(3);
            DataModel.SetReadOnly(4);
            DataModel.SetReadOnly(5);
            DataModel.SetReadOnly(6);
            DataModel.SetReadOnly(7);
            DataModel.SetReadOnly(8);
            DataModel.SetReadOnly(9);
            DataModel.SetReadOnly(10);
            DataModel.SetReadOnly(11);
            DataModel.SetReadOnly(12);
            DataModel.SetReadOnly(13);
            DataModel.SetReadOnly(15);
            DataModel.SetReadOnly(16);
            DataModel.SetReadOnly(27);
            DataModel.SetReadOnly(28);
            DataModel.SetReadOnly(29);
            DataModel.SetReadOnly(30);
            DataModel.SetReadOnly(31);
            DataModel.SetReadOnly(32);
            //DataModel.SetReadOnly(33);
            DataModel.SetReadOnly(33);
            DataModel.SetReadOnly(17);
            DataModel.SetReadOnly(19);
            DataModel.SetReadOnly(20);
            DataModel.SetReadOnly(36);
            DataModel.SetReadOnly(37);
            if (USER_ID != 311 && USER_ID != 352 && USER_ID != 136 && USER_ID != 333 && USER_ID != 318 && USER_ID != 329 && USER_ID != 331 && USER_ID != 28 && USER_ID != 394 && USER_ID != 361 && USER_ID != 409) {
                DataModel.SetReadOnly(1);
                DataModel.SetReadOnly(14);

                DataModel.SetReadOnly(18);
                //DataModel.SetReadOnly(19);
                //DataModel.SetReadOnly(20);
                DataModel.SetReadOnly(21);
                DataModel.SetReadOnly(22);
                DataModel.SetReadOnly(23);
                DataModel.SetReadOnly(24);
                DataModel.SetReadOnly(25);
                DataModel.SetReadOnly(26);
                //DataModel.SetReadOnly(33);
                DataModel.SetReadOnly(34);
                DataModel.SetReadOnly(35);
            }

            for (int i = 0; i < DataModel.getColumnCount(); i++) {
                Table.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_COLOR);
            }

            int ImportCol1 = 1;
            Renderer1.setCustomComponent(ImportCol1, "CheckBox");
            final JCheckBox aCheckBox1 = new JCheckBox();
            aCheckBox1.setBackground(Color.WHITE);
            aCheckBox1.setVisible(true);
//            aCheckBox1.addItemListener(new ItemListener() {    
//                public void itemStateChanged(ItemEvent e) {                 
//                   aCheckBox1.setEnabled(false);
//                }    
//             });    
            //For Felt Design
//            if (clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) == 39) {
//                aCheckBox2.setEnabled(true);
//            } else {
//                aCheckBox2.setEnabled(false);
//            }

            aCheckBox1.setSelected(false);
            Table.getColumnModel().getColumn(ImportCol1).setCellEditor(new DefaultCellEditor(aCheckBox1));
            Table.getColumnModel().getColumn(ImportCol1).setCellRenderer(Renderer1);

            int ImportCol2 = 18;
            Renderer1.setCustomComponent(ImportCol2, "CheckBox");
            JCheckBox aCheckBox2 = new JCheckBox();
            aCheckBox2.setBackground(Color.WHITE);
            aCheckBox2.setVisible(true);
            aCheckBox2.setSelected(false);
            Table.getColumnModel().getColumn(ImportCol2).setCellEditor(new DefaultCellEditor(aCheckBox2));
            Table.getColumnModel().getColumn(ImportCol2).setCellRenderer(Renderer1);

            int ImportCol3 = DataModel.getColFromVariable("PIECE_CLUBBING");
            Renderer1.setCustomComponent(ImportCol3, "CheckBox");
            JCheckBox aCheckBox3 = new JCheckBox();
            aCheckBox3.setBackground(Color.WHITE);
            aCheckBox3.setVisible(true);
            aCheckBox3.setSelected(false);
            Table.getColumnModel().getColumn(ImportCol3).setCellEditor(new DefaultCellEditor(aCheckBox3));
            Table.getColumnModel().getColumn(ImportCol3).setCellRenderer(Renderer1);

            TableColumn commitmentStatus = Table.getColumnModel().getColumn(DataModel.getColFromVariable("PARTY_LIFTING_COMMITMENT_STATUS"));
            JComboBox Statusbox = new JComboBox();
            Statusbox.addItem("COMMITTED");//committed
            Statusbox.addItem("NOT COMMITTED");
            commitmentStatus.setCellEditor(new DefaultCellEditor(Statusbox));
            
            TableColumn dateColumn = Table.getColumnModel().getColumn(DataModel.getColFromVariable("EXPECTED_MONTH_OF_DISPATCH"));

            JComboBox monthbox = new JComboBox();
            String month_name = "";
            Date date = new Date();
            int month = date.getMonth();
            int year = date.getYear() + 1900;
            //monthbox.addItem("");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void FormatGrid_cl_piece() {
//        try {
//            DataModel_cl_piece = new EITLTableModel();
//            tbl_Piece.removeAll();
//
//            tbl_Piece.setModel(DataModel_cl_piece);
//            tbl_Piece.setAutoResizeMode(0);
//
//            DataModel_cl_piece.addColumn("SrNo"); //0 - Read Only
//            DataModel_cl_piece.addColumn("CLUBBING");
//            DataModel_cl_piece.addColumn("Piece No");
//            DataModel_cl_piece.addColumn("OC Month");
//            DataModel_cl_piece.addColumn("Curr Sch Month");
//            DataModel_cl_piece.addColumn("Piece Stage");
//            DataModel_cl_piece.addColumn("Finishing Date");
//            DataModel_cl_piece.addColumn("Warehouse Days");
//
//            DataModel_cl_piece.SetVariable(0, "SrNo"); //0 - Read Only
//            DataModel_cl_piece.SetVariable(1, "CLUBBING");
//            DataModel_cl_piece.SetVariable(2, "PIECE_NO");
//            DataModel_cl_piece.SetVariable(3, "OC_MONTH");
//            DataModel_cl_piece.SetVariable(4, "CURRENT_SCH_MONTH");
//            DataModel_cl_piece.SetVariable(5, "PIECE_STAGE");
//            DataModel_cl_piece.SetVariable(6, "FINISHING_DATE");
//            DataModel_cl_piece.SetVariable(7, "WH_DAYS");
//
//            tbl_Piece.getColumnModel().getColumn(0).setMaxWidth(40);
//            tbl_Piece.getColumnModel().getColumn(1).setMinWidth(90);
//            tbl_Piece.getColumnModel().getColumn(2).setMinWidth(80);
//            tbl_Piece.getColumnModel().getColumn(3).setMinWidth(150);
//            tbl_Piece.getColumnModel().getColumn(4).setMinWidth(80);
//            tbl_Piece.getColumnModel().getColumn(5).setMinWidth(100);
//            tbl_Piece.getColumnModel().getColumn(6).setMinWidth(100);
//
//            DataModel_cl_piece.SetReadOnly(0);
//            //DataModel_cl_piece.SetReadOnly(1);
//            DataModel_cl_piece.SetReadOnly(2);
//            DataModel_cl_piece.SetReadOnly(3);
//            DataModel_cl_piece.SetReadOnly(4);
//            DataModel_cl_piece.SetReadOnly(5);
//            DataModel_cl_piece.SetReadOnly(6);
//
//            int ImportCol = 1;
//            Renderer1.setCustomComponent(ImportCol, "CheckBox");
//            JCheckBox aCheckBox = new JCheckBox();
//            aCheckBox.setBackground(Color.WHITE);
//            aCheckBox.setVisible(true);
//
////            //For Felt Design
////            if (clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID) == 39) {
////                aCheckBox.setEnabled(true);
////            } else {
////                aCheckBox.setEnabled(false);
////            }
//            aCheckBox.setSelected(false);
//            tbl_Piece.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
//            tbl_Piece.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer1);
//
////            if(USER_ID!=311 && USER_ID!=1)
////            {
////                DataModel.SetReadOnly(12);
////                DataModel.SetReadOnly(13);
////                DataModel.SetReadOnly(14);
////                DataModel.SetReadOnly(15);
////                DataModel.SetReadOnly(16);
////                DataModel.SetReadOnly(17);
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void FormatGrid_History() {
        try {
            DataModel_History = new EITLTableModel();
            tblHistory.removeAll();

            tblHistory.setModel(DataModel_History);
            tblHistory.setAutoResizeMode(0);

            DataModel_History.addColumn("SrNo"); //0 - Read Only
            DataModel_History.addColumn("Piece No");
            DataModel_History.addColumn("Party Code");
            DataModel_History.addColumn("Party Name");
            DataModel_History.addColumn("OC Month");
            DataModel_History.addColumn("Cur Sch Month");
            DataModel_History.addColumn("Follow Up Date");
            DataModel_History.addColumn("Charge Code");
            DataModel_History.addColumn("Delay Reason");
            DataModel_History.addColumn("Unable to Contact");
            DataModel_History.addColumn("Contacted Person");
            DataModel_History.addColumn("Contacted No");
            DataModel_History.addColumn("Date Of Communication");
            DataModel_History.addColumn("Mode of Communication");
            DataModel_History.addColumn("Party Justification");
            DataModel_History.addColumn("Area Manager Comments");
            DataModel_History.addColumn("Exp Month of Despatch");
            DataModel_History.addColumn("Party Lifting Commitment Status");//30
            DataModel_History.addColumn("Commitment Date");//30
            DataModel_History.addColumn("DATE_TIME");

            DataModel_History.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_History.SetVariable(1, "PIECE_NO");
            DataModel_History.SetVariable(2, "PARTY_CODE");
            DataModel_History.SetVariable(3, "PARTY_NAME");
            DataModel_History.SetVariable(4, "OC_MONTH");
            DataModel_History.SetVariable(5, "CURRENT_SCH_MONTH");
            DataModel_History.SetVariable(6, "EXPECTED_DISPATCH_DATE");
            DataModel_History.SetVariable(7, "CHARGE_CODE");
            DataModel_History.SetVariable(8, "REMARK");
            DataModel_History.SetVariable(9, "UNABLE_TO_CONTACT");
            DataModel_History.SetVariable(10, "CONTACTED_PERSON");
            DataModel_History.SetVariable(11, "CONTACTED_NO");

            DataModel_History.SetVariable(12, "DATE_OF_COMMUNICATION");
            DataModel_History.SetVariable(13, "MODE_OF_COMMUNICATION");
            DataModel_History.SetVariable(14, "PARTY_JUSTIFICATION");
            DataModel_History.SetVariable(15, "AREA_MANAGER_COMMENT");
            DataModel_History.SetVariable(16, "EXPECTED_MONTH_OF_DISPATCH");
            DataModel_History.SetVariable(17, "PARTY_LIFTING_COMMITMENT_STATUS");
            DataModel_History.SetVariable(18, "PARTY_LIFTING_COMMITMENT_DATE");
            DataModel_History.SetVariable(19, "DATE_TIME");

            int ImportCol = 9;
            Renderer1.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);

            aCheckBox.setSelected(false);
            tblHistory.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblHistory.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer1);

            tblHistory.getColumnModel().getColumn(0).setMaxWidth(40);
            tblHistory.getColumnModel().getColumn(1).setMinWidth(90);
            tblHistory.getColumnModel().getColumn(2).setMinWidth(80);
            tblHistory.getColumnModel().getColumn(3).setMinWidth(150);
            tblHistory.getColumnModel().getColumn(4).setMinWidth(100);
            tblHistory.getColumnModel().getColumn(5).setMinWidth(100);
            tblHistory.getColumnModel().getColumn(6).setMinWidth(250);
            tblHistory.getColumnModel().getColumn(7).setMinWidth(100);
            tblHistory.getColumnModel().getColumn(8).setMinWidth(250);
            tblHistory.getColumnModel().getColumn(9).setMinWidth(250);
            tblHistory.getColumnModel().getColumn(10).setMinWidth(250);
            tblHistory.getColumnModel().getColumn(11).setMinWidth(250);
            tblHistory.getColumnModel().getColumn(12).setMinWidth(150);
            tblHistory.getColumnModel().getColumn(13).setMinWidth(150);
            tblHistory.getColumnModel().getColumn(14).setMinWidth(150);
            tblHistory.getColumnModel().getColumn(15).setMinWidth(250);
            tblHistory.getColumnModel().getColumn(16).setMinWidth(150);
            tblHistory.getColumnModel().getColumn(17).setMinWidth(250);
            tblHistory.getColumnModel().getColumn(18).setMinWidth(180);
            tblHistory.getColumnModel().getColumn(19).setMinWidth(150);

            DataModel_History.SetReadOnly(0);
            DataModel_History.SetReadOnly(1);
            DataModel_History.SetReadOnly(2);
            DataModel_History.SetReadOnly(3);
            DataModel_History.SetReadOnly(4);
            DataModel_History.SetReadOnly(5);
            DataModel_History.SetReadOnly(7);
            DataModel_History.SetReadOnly(6);
            DataModel_History.SetReadOnly(8);
            DataModel_History.SetReadOnly(9);
            DataModel_History.SetReadOnly(10);
            DataModel_History.SetReadOnly(11);
            DataModel_History.SetReadOnly(12);
            DataModel_History.SetReadOnly(13);
            DataModel_History.SetReadOnly(14);
            DataModel_History.SetReadOnly(15);
            DataModel_History.SetReadOnly(16);
            DataModel_History.SetReadOnly(17);
            DataModel_History.SetReadOnly(18);
            DataModel_History.SetReadOnly(19);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGrid_Clubbed_History() {
        try {
            DataModel_Clubbed_History = new EITLTableModel();
            tblClubbedHistory.removeAll();

            tblClubbedHistory.setModel(DataModel_Clubbed_History);
            tblClubbedHistory.setAutoResizeMode(0);

            DataModel_Clubbed_History.addColumn("SrNo"); //0 - Read Only
            DataModel_Clubbed_History.addColumn("Piece No");
            DataModel_Clubbed_History.addColumn("Party Code");
            DataModel_Clubbed_History.addColumn("Party Name");
            DataModel_Clubbed_History.addColumn("OC Month");
            DataModel_Clubbed_History.addColumn("Cur Sch Month");
            DataModel_Clubbed_History.addColumn("Follow Up Date");
            DataModel_Clubbed_History.addColumn("Charge Code");
            DataModel_Clubbed_History.addColumn("Delay Reason");
            DataModel_Clubbed_History.addColumn("Unable to Contact");
            DataModel_Clubbed_History.addColumn("Contacted Person");
            DataModel_Clubbed_History.addColumn("Contacted No");
            DataModel_Clubbed_History.addColumn("Date Of Communication");
            DataModel_Clubbed_History.addColumn("Mode of Communication");
            DataModel_Clubbed_History.addColumn("Party Justification");
            DataModel_Clubbed_History.addColumn("Area Manager Comments");
            DataModel_Clubbed_History.addColumn("Party Lifting Commitment Status");//30
            DataModel_Clubbed_History.addColumn("Commitment Date");//30
            DataModel_Clubbed_History.addColumn("DATE_TIME");

            DataModel_Clubbed_History.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_Clubbed_History.SetVariable(1, "PIECE_NO");
            DataModel_Clubbed_History.SetVariable(2, "PARTY_CODE");
            DataModel_Clubbed_History.SetVariable(3, "PARTY_NAME");
            DataModel_Clubbed_History.SetVariable(4, "OC_MONTH");
            DataModel_Clubbed_History.SetVariable(5, "CURRENT_SCH_MONTH");
            DataModel_Clubbed_History.SetVariable(6, "EXPECTED_DISPATCH_DATE");
            DataModel_Clubbed_History.SetVariable(7, "CHARGE_CODE");
            DataModel_Clubbed_History.SetVariable(8, "REMARK");
            DataModel_Clubbed_History.SetVariable(9, "UNABLE_TO_CONTACT");
            DataModel_Clubbed_History.SetVariable(10, "CONTACTED_PERSON");
            DataModel_Clubbed_History.SetVariable(11, "CONTACTED_NO");

            DataModel_Clubbed_History.SetVariable(12, "DATE_OF_COMMUNICATION");
            DataModel_Clubbed_History.SetVariable(13, "MODE_OF_COMMUNICATION");
            DataModel_Clubbed_History.SetVariable(14, "PARTY_JUSTIFICATION");
            DataModel_Clubbed_History.SetVariable(15, "AREA_MANAGER_COMMENT");
            DataModel_Clubbed_History.SetVariable(16, "PARTY_LIFTING_COMMITMENT_STATUS");
            DataModel_Clubbed_History.SetVariable(17, "PARTY_LIFTING_COMMITMENT_DATE");
            DataModel_Clubbed_History.SetVariable(18, "DATE_TIME");

            int ImportCol = 9;
            Renderer1.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);

            aCheckBox.setSelected(false);
            tblClubbedHistory.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblClubbedHistory.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer1);

            tblClubbedHistory.getColumnModel().getColumn(0).setMaxWidth(40);
            tblClubbedHistory.getColumnModel().getColumn(1).setMinWidth(90);
            tblClubbedHistory.getColumnModel().getColumn(2).setMinWidth(80);
            tblClubbedHistory.getColumnModel().getColumn(3).setMinWidth(150);

            tblClubbedHistory.getColumnModel().getColumn(4).setMinWidth(0);
            tblClubbedHistory.getColumnModel().getColumn(5).setMinWidth(0);
            tblClubbedHistory.getColumnModel().getColumn(4).setMaxWidth(0);
            tblClubbedHistory.getColumnModel().getColumn(5).setMaxWidth(0);

            tblClubbedHistory.getColumnModel().getColumn(6).setMinWidth(250);
            tblClubbedHistory.getColumnModel().getColumn(7).setMinWidth(100);
            tblClubbedHistory.getColumnModel().getColumn(8).setMinWidth(250);
            tblClubbedHistory.getColumnModel().getColumn(9).setMinWidth(250);
            tblClubbedHistory.getColumnModel().getColumn(10).setMinWidth(250);
            tblClubbedHistory.getColumnModel().getColumn(11).setMinWidth(250);

            tblClubbedHistory.getColumnModel().getColumn(12).setMinWidth(0);
            tblClubbedHistory.getColumnModel().getColumn(12).setMaxWidth(0);

            tblClubbedHistory.getColumnModel().getColumn(13).setMinWidth(150);
            tblClubbedHistory.getColumnModel().getColumn(14).setMinWidth(150);
            tblClubbedHistory.getColumnModel().getColumn(15).setMinWidth(250);
            tblClubbedHistory.getColumnModel().getColumn(16).setMinWidth(250);
            tblClubbedHistory.getColumnModel().getColumn(17).setMinWidth(250);
            tblClubbedHistory.getColumnModel().getColumn(18).setMinWidth(150);

            DataModel_Clubbed_History.SetReadOnly(0);
            DataModel_Clubbed_History.SetReadOnly(1);
            DataModel_Clubbed_History.SetReadOnly(2);
            DataModel_Clubbed_History.SetReadOnly(3);
            DataModel_Clubbed_History.SetReadOnly(4);
            DataModel_Clubbed_History.SetReadOnly(5);
            DataModel_Clubbed_History.SetReadOnly(7);
            DataModel_Clubbed_History.SetReadOnly(6);
            DataModel_Clubbed_History.SetReadOnly(8);
            DataModel_Clubbed_History.SetReadOnly(9);
            DataModel_Clubbed_History.SetReadOnly(10);
            DataModel_Clubbed_History.SetReadOnly(11);
            DataModel_Clubbed_History.SetReadOnly(12);
            DataModel_Clubbed_History.SetReadOnly(13);
            DataModel_Clubbed_History.SetReadOnly(14);
            DataModel_Clubbed_History.SetReadOnly(15);
            DataModel_Clubbed_History.SetReadOnly(16);
            DataModel_Clubbed_History.SetReadOnly(17);
            DataModel_Clubbed_History.SetReadOnly(18);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGrid_Clubbing() {
        try {
            DataModel_Clubbing = new EITLTableModel();
            tblPieceClubbingHeader.removeAll();

            tblPieceClubbingHeader.setModel(DataModel_Clubbing);
            tblPieceClubbingHeader.setAutoResizeMode(0);

            DataModel_Clubbing.addColumn("SrNo"); //0 - Read Only
            DataModel_Clubbing.addColumn("Clubbing No");
            DataModel_Clubbing.addColumn("Party Code");
            DataModel_Clubbing.addColumn("Party Name");
            DataModel_Clubbing.addColumn("No of Pieces");
            DataModel_Clubbing.addColumn("Last OC MONTH");
            DataModel_Clubbing.addColumn("Last SALES PLAN");
            DataModel_Clubbing.addColumn("Approval Status");

            DataModel_Clubbing.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_Clubbing.SetVariable(1, "CLUBBING_NO");
            DataModel_Clubbing.SetVariable(2, "PARTY_CODE");
            DataModel_Clubbing.SetVariable(3, "PARTY_NAME");
            DataModel_Clubbing.SetVariable(4, "NO_OF_PIECE");
            DataModel_Clubbing.SetVariable(5, "LAST_OC_MONTH");
            DataModel_Clubbing.SetVariable(6, "LAST_CURR_SCH_MONTH");
            DataModel_Clubbing.SetVariable(7, "APPROVAL_STATUS");

            tblPieceClubbingHeader.getColumnModel().getColumn(0).setMaxWidth(40);
            tblPieceClubbingHeader.getColumnModel().getColumn(1).setMinWidth(150);
            tblPieceClubbingHeader.getColumnModel().getColumn(2).setMinWidth(80);
            tblPieceClubbingHeader.getColumnModel().getColumn(3).setMinWidth(150);
            tblPieceClubbingHeader.getColumnModel().getColumn(4).setMinWidth(100);
            tblPieceClubbingHeader.getColumnModel().getColumn(5).setMinWidth(100);
            tblPieceClubbingHeader.getColumnModel().getColumn(6).setMinWidth(250);
            tblPieceClubbingHeader.getColumnModel().getColumn(7).setMinWidth(180);

            tblPieceClubbingHeader.getTableHeader().setReorderingAllowed(false);

            DataModel_Clubbing.SetReadOnly(0);
            DataModel_Clubbing.SetReadOnly(1);
            DataModel_Clubbing.SetReadOnly(2);
            DataModel_Clubbing.SetReadOnly(3);
            DataModel_Clubbing.SetReadOnly(4);
            DataModel_Clubbing.SetReadOnly(5);
            DataModel_Clubbing.SetReadOnly(7);
            DataModel_Clubbing.SetReadOnly(6);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGrid_ClubbingDetail() {
        try {
            DataModel_ClubbingDetail = new EITLTableModel();
            tblPieceClubbingDetail.removeAll();

            tblPieceClubbingDetail.setModel(DataModel_ClubbingDetail);
            tblPieceClubbingDetail.setAutoResizeMode(0);

            DataModel_ClubbingDetail.addColumn("SrNo"); //0 - Read Only
            DataModel_ClubbingDetail.addColumn("Piece No");
            DataModel_ClubbingDetail.addColumn("REQ MONTH");
            DataModel_ClubbingDetail.addColumn("OC MONTH");
            DataModel_ClubbingDetail.addColumn("CURRENT SALES PLAN");
            DataModel_ClubbingDetail.addColumn("CURRENT PIECE STAGE");
            DataModel_ClubbingDetail.addColumn("LENGTH");
            DataModel_ClubbingDetail.addColumn("WIDTH");
            DataModel_ClubbingDetail.addColumn("GSM");
            DataModel_ClubbingDetail.addColumn("SQMTR");
            DataModel_ClubbingDetail.addColumn("WEIGHT");

            DataModel_ClubbingDetail.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_ClubbingDetail.SetVariable(1, "PIECE_NO");
            DataModel_ClubbingDetail.SetVariable(2, "REQ_MONTH");
            DataModel_ClubbingDetail.SetVariable(3, "OC_MONTH");
            DataModel_ClubbingDetail.SetVariable(4, "CURRENT_SALES_PLAN");
            DataModel_ClubbingDetail.SetVariable(5, "CURRENT_PIECE_STAGE");
            DataModel_ClubbingDetail.SetVariable(6, "LENGTH");
            DataModel_ClubbingDetail.SetVariable(7, "WIDTH");
            DataModel_ClubbingDetail.SetVariable(8, "GSM");
            DataModel_ClubbingDetail.SetVariable(9, "SQMTR");
            DataModel_ClubbingDetail.SetVariable(10, "WEIGHT");

            tblPieceClubbingDetail.getColumnModel().getColumn(0).setMaxWidth(40);
            tblPieceClubbingDetail.getColumnModel().getColumn(1).setMinWidth(100);
            tblPieceClubbingDetail.getColumnModel().getColumn(2).setMinWidth(100);
            tblPieceClubbingDetail.getColumnModel().getColumn(3).setMinWidth(100);
            tblPieceClubbingDetail.getColumnModel().getColumn(4).setMinWidth(120);
            tblPieceClubbingDetail.getColumnModel().getColumn(5).setMinWidth(120);
            tblPieceClubbingDetail.getColumnModel().getColumn(6).setMinWidth(80);
            tblPieceClubbingDetail.getColumnModel().getColumn(7).setMinWidth(80);
            tblPieceClubbingDetail.getColumnModel().getColumn(8).setMinWidth(80);
            tblPieceClubbingDetail.getColumnModel().getColumn(9).setMinWidth(80);
            tblPieceClubbingDetail.getColumnModel().getColumn(10).setMinWidth(80);

            tblPieceClubbingDetail.getTableHeader().setReorderingAllowed(false);

            DataModel_ClubbingDetail.SetReadOnly(0);
            DataModel_ClubbingDetail.SetReadOnly(1);
            DataModel_ClubbingDetail.SetReadOnly(2);
            DataModel_ClubbingDetail.SetReadOnly(3);
            DataModel_ClubbingDetail.SetReadOnly(4);
            DataModel_ClubbingDetail.SetReadOnly(5);
            DataModel_ClubbingDetail.SetReadOnly(7);
            DataModel_ClubbingDetail.SetReadOnly(6);
            DataModel_ClubbingDetail.SetReadOnly(8);
            DataModel_ClubbingDetail.SetReadOnly(9);
            DataModel_ClubbingDetail.SetReadOnly(10);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGrid_Clubbing_Master() {
        try {
            DataModel_CLB_Master = new EITLTableModel();
            tblPieceClubbingMaster.removeAll();

            tblPieceClubbingMaster.setModel(DataModel_CLB_Master);
            tblPieceClubbingMaster.setAutoResizeMode(0);

            DataModel_CLB_Master.addColumn("SrNo"); //0 - Read Only
            DataModel_CLB_Master.addColumn("Clubbing No");
            DataModel_CLB_Master.addColumn("Pieces");
            DataModel_CLB_Master.addColumn("Party Code");
            DataModel_CLB_Master.addColumn("Party Name");
            DataModel_CLB_Master.addColumn("No of Pieces");
            DataModel_CLB_Master.addColumn("Total Invoice Value");
            DataModel_CLB_Master.addColumn("OC MONTH");
            DataModel_CLB_Master.addColumn("Last SALES PLAN");
            DataModel_CLB_Master.addColumn("Next Follow Up Date");
            DataModel_CLB_Master.addColumn("Delay Reason");
            DataModel_CLB_Master.addColumn("Unable to Contact");
            DataModel_CLB_Master.addColumn("Contatcted Person");
            DataModel_CLB_Master.addColumn("Contatcted Number");
            DataModel_CLB_Master.addColumn("Date of Communication");
            DataModel_CLB_Master.addColumn("Mode of Communication");
//            DataModel_CLB_Master.addColumn("Party Justification");
//            DataModel_CLB_Master.addColumn("Area Manager Comments");
            DataModel_CLB_Master.addColumn("Prevoius Party Justification");//23
            DataModel_CLB_Master.addColumn("Party Justification");//23
            DataModel_CLB_Master.addColumn("Prevoius Area Manager Comments");//24
            DataModel_CLB_Master.addColumn("Area Manager Comments");//24
            DataModel_CLB_Master.addColumn("Expected Month of Dispatch");//EXPECTED_MONTH_OF_DISPATCH
            DataModel_CLB_Master.addColumn("Party Lifting Commitment Status");//30
            DataModel_CLB_Master.addColumn("Commitment Date");//30
            DataModel_CLB_Master.addColumn("Prevoius Party Lifting Commitment Status");//30
            DataModel_CLB_Master.addColumn("Prevoius Commitment Date");//30
            
            DataModel_CLB_Master.SetVariable(0, "SrNo"); //0 - Read Only
            DataModel_CLB_Master.SetVariable(1, "CLUBBING_NO");
            DataModel_CLB_Master.SetVariable(2, "PIECE_NO");
            DataModel_CLB_Master.SetVariable(3, "PARTY_CODE");
            DataModel_CLB_Master.SetVariable(4, "PARTY_NAME");
            DataModel_CLB_Master.SetVariable(5, "NO_OF_PIECE");
            DataModel_CLB_Master.SetVariable(6, "TOTAL_INVOICE_VALUE");
            DataModel_CLB_Master.SetVariable(7, "LAST_OC_MONTH");
            DataModel_CLB_Master.SetVariable(8, "LAST_CURR_SCH_MONTH");
            DataModel_CLB_Master.SetVariable(9, "NEW_EXPECTED_DISPATCH_DATE");
            DataModel_CLB_Master.SetVariable(10, "REMARK");
            DataModel_CLB_Master.SetVariable(11, "UNABLE_TO_CONTACT");
            DataModel_CLB_Master.SetVariable(12, "CONTACTED_PERSON");
            DataModel_CLB_Master.SetVariable(13, "CONTACTED_NO");
            DataModel_CLB_Master.SetVariable(14, "DATE_OF_COMMUNICATION");
            DataModel_CLB_Master.SetVariable(15, "MODE_OF_COMMUNICATION");
            DataModel_CLB_Master.SetVariable(16, "PREVIOUS_PARTY_JUSTIFICATION");
            DataModel_CLB_Master.SetVariable(17, "PARTY_JUSTIFICATION");
            DataModel_CLB_Master.SetVariable(18, "PREVIOUS_AREA_MANAGER_COMMENT");
            DataModel_CLB_Master.SetVariable(19, "AREA_MANAGER_COMMENT");
            DataModel_CLB_Master.SetVariable(20, "EXPECTED_MONTH_OF_DISPATCH");
            DataModel_CLB_Master.SetVariable(21, "PARTY_LIFTING_COMMITMENT_STATUS");
            DataModel_CLB_Master.SetVariable(22, "PARTY_LIFTING_COMMITMENT_DATE");
            DataModel_CLB_Master.SetVariable(23, "PREVIOUS_PARTY_LIFTING_COMMITMENT_STATUS");
            DataModel_CLB_Master.SetVariable(24, "PREVIOUS_COMMITMENT_DATE");
            
            
            int ImportCol2 = DataModel_CLB_Master.getColFromVariable("UNABLE_TO_CONTACT");
            Renderer1.setCustomComponent(ImportCol2, "CheckBox");
            JCheckBox aCheckBox2 = new JCheckBox();
            aCheckBox2.setBackground(Color.WHITE);
            aCheckBox2.setVisible(true);
            aCheckBox2.setSelected(false);
            tblPieceClubbingMaster.getColumnModel().getColumn(ImportCol2).setCellEditor(new DefaultCellEditor(aCheckBox2));
            tblPieceClubbingMaster.getColumnModel().getColumn(ImportCol2).setCellRenderer(Renderer1);

            TableColumn commitmentStatus = tblPieceClubbingMaster.getColumnModel().getColumn(DataModel_CLB_Master.getColFromVariable("PARTY_LIFTING_COMMITMENT_STATUS"));
            JComboBox Statusbox = new JComboBox();
            Statusbox.addItem("COMMITTED");//committed
            Statusbox.addItem("NOT COMMITTED");
            commitmentStatus.setCellEditor(new DefaultCellEditor(Statusbox));
            
            
            tblPieceClubbingMaster.getColumnModel().getColumn(0).setMaxWidth(40);
            tblPieceClubbingMaster.getColumnModel().getColumn(1).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(2).setMinWidth(250);
            tblPieceClubbingMaster.getColumnModel().getColumn(3).setMinWidth(80);
            tblPieceClubbingMaster.getColumnModel().getColumn(4).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(5).setMinWidth(100);
            tblPieceClubbingMaster.getColumnModel().getColumn(6).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(7).setMinWidth(110);
            tblPieceClubbingMaster.getColumnModel().getColumn(8).setMinWidth(130);
            tblPieceClubbingMaster.getColumnModel().getColumn(9).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(10).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(11).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(12).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(13).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(14).setMinWidth(0);
            tblPieceClubbingMaster.getColumnModel().getColumn(14).setMaxWidth(0);
            tblPieceClubbingMaster.getColumnModel().getColumn(15).setMinWidth(150);
            tblPieceClubbingMaster.getColumnModel().getColumn(16).setMinWidth(230);
            tblPieceClubbingMaster.getColumnModel().getColumn(17).setMinWidth(280);
            tblPieceClubbingMaster.getColumnModel().getColumn(18).setMinWidth(230);
            tblPieceClubbingMaster.getColumnModel().getColumn(19).setMinWidth(280);
            tblPieceClubbingMaster.getColumnModel().getColumn(20).setMinWidth(180);
            
            tblPieceClubbingMaster.getColumnModel().getColumn(21).setMinWidth(250);
            tblPieceClubbingMaster.getColumnModel().getColumn(22).setMinWidth(180);
            tblPieceClubbingMaster.getColumnModel().getColumn(23).setMinWidth(280);
            tblPieceClubbingMaster.getColumnModel().getColumn(24).setMinWidth(180);

            tblPieceClubbingMaster.getTableHeader().setReorderingAllowed(false);

            DataModel_CLB_Master.SetReadOnly(0);
            DataModel_CLB_Master.SetReadOnly(1);
            DataModel_CLB_Master.SetReadOnly(2);
            DataModel_CLB_Master.SetReadOnly(3);
            DataModel_CLB_Master.SetReadOnly(4);
            DataModel_CLB_Master.SetReadOnly(5);
            DataModel_CLB_Master.SetReadOnly(6);
            DataModel_CLB_Master.SetReadOnly(7);
            DataModel_CLB_Master.SetReadOnly(8);
            DataModel_CLB_Master.SetReadOnly(10);
            DataModel_CLB_Master.SetReadOnly(12);
            DataModel_CLB_Master.SetReadOnly(13);
            
            DataModel_CLB_Master.SetReadOnly(23);
            DataModel_CLB_Master.SetReadOnly(24);
            DataModel_CLB_Master.SetReadOnly(20);
            if (USER_ID != 311 && USER_ID != 352 && USER_ID != 136 && USER_ID != 333 && USER_ID != 318 && USER_ID != 329 && USER_ID != 331 && USER_ID != 28 && USER_ID != 394 && USER_ID != 361 && USER_ID != 409) {
                DataModel_CLB_Master.SetReadOnly(9);
                DataModel_CLB_Master.SetReadOnly(11);

                DataModel_CLB_Master.SetReadOnly(14);
                DataModel_CLB_Master.SetReadOnly(15);
                DataModel_CLB_Master.SetReadOnly(16);
                DataModel_CLB_Master.SetReadOnly(17);
                DataModel_CLB_Master.SetReadOnly(18);
                DataModel_CLB_Master.SetReadOnly(19);
                //DataModel_CLB_Master.SetReadOnly(20);
                
                DataModel_CLB_Master.SetReadOnly(21);
                DataModel_CLB_Master.SetReadOnly(22);
            }

            //DataModel_CLB_Master.SetReadOnly(17);
            //DataModel_CLB_Master.SetReadOnly(19);
            TableColumn dateColumn = tblPieceClubbingMaster.getColumnModel().getColumn(DataModel_CLB_Master.getColFromVariable("EXPECTED_MONTH_OF_DISPATCH"));

            JComboBox monthbox = new JComboBox();
            String month_name = "";
            Date date = new Date();
            int month = date.getMonth();
            int year = date.getYear() + 1900;
            //monthbox.addItem("");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        file1 = new javax.swing.JFileChooser();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        Tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cmbZone = new javax.swing.JComboBox();
        Export_WeavingProduction = new javax.swing.JButton();
        txtCurrentSchMonth = new javax.swing.JTextField();
        txtYear = new javax.swing.JTextField();
        txtMonth = new javax.swing.JTextField();
        lblSelectedPartyName = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        AddPiece = new javax.swing.JButton();
        btnShowData = new javax.swing.JButton();
        txtFollowupDate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblSelectedPiece = new javax.swing.JLabel();
        lblSelectedParty = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtWidth = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtMachine = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtGSM = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtLength = new javax.swing.JTextField();
        txtPosition = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblPieceClubbingMaster = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        btnClubbingFollowupSave = new javax.swing.JButton();
        txtFWLMonth = new javax.swing.JTextField();
        txtFWLYear = new javax.swing.JTextField();
        txtFWLCurrentSchMonth = new javax.swing.JTextField();
        btnShowOrderStatus = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtInchargeCd = new javax.swing.JTextField();
        txtInchargeName = new javax.swing.JTextField();
        btnHistory = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtPartyCode_Search = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtPieceNo_Search = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtHistoryDate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblClubbedHistory = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPieceClubbingHeader = new javax.swing.JTable();
        btnClubbingHistory = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtPartyCode_ClubbingSearch = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtPieceNo_ClubbingSearch = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtClubbibgNo = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPieceClubbingDetail = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        btnRemovePiece = new javax.swing.JButton();
        btnAddPiece = new javax.swing.JButton();
        btnUpdateTransaction = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtSelectedClubbingPartyCode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtSelectedClubbingNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtApprovalStatus = new javax.swing.JTextField();
        txtInchargeCd_Clubbing = new javax.swing.JTextField();
        txtInchargeName_Clubbing = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnClubbingAmend = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        chkINSTOCK = new javax.swing.JCheckBox();
        chkWIP = new javax.swing.JCheckBox();
        chkINVOICED = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        Table_OrderFUP = new javax.swing.JTable();
        btnSave_OrderFUP = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        cmbZone_OrderFUP = new javax.swing.JComboBox();
        Export_OrderFUP = new javax.swing.JButton();
        lblOFUP_PartyName = new javax.swing.JLabel();
        btnShowData_OrderFUP = new javax.swing.JButton();
        txtOrderFUPDate = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        lblOFUP_UPN = new javax.swing.JLabel();
        lblOFUP_PartyCode = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtOFUP_Width = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtOFUP_Machine = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtOFUP_GSM = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtOFUP_ProductCode = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtOFUP_Length = new javax.swing.JTextField();
        txtOFUP_Position = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        lblCPRSMonth = new javax.swing.JLabel();
        txtOrderFUPMonth = new javax.swing.JTextField();
        txtOrderFUPYear = new javax.swing.JTextField();
        txtOrderFUP_CPRS_Month = new javax.swing.JTextField();
        btnBooking_OrderFUP = new javax.swing.JButton();
        btnPieceDiv_OrderFUP = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        txtOrderFUPpartycode = new javax.swing.JTextField();
        txtOrderFUPpartyname = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtOrderFUPgroupcode = new javax.swing.JTextField();
        txtOrderFUPgroupname = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtOrderFUPprodcategory = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtOrderFUPCPRSMonth = new javax.swing.JTextField();
        Order_FUP_Rpt = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblOFUP_History = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        txtInchargeCd_OFUPH = new javax.swing.JTextField();
        txtInchargeName_OFUPH = new javax.swing.JTextField();
        btnOFUP_History = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        txtOFUP_PartyCode_Search = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtOFUP_HistoryDate = new javax.swing.JTextField();
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
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                TableAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
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
        jScrollPane1.setBounds(10, 40, 1270, 290);

        btnSave.setText("SAVE");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave);
        btnSave.setBounds(1150, 10, 130, 20);

        jLabel4.setText("Area");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 10, 50, 20);

        cmbZone.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbZone.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbZoneItemStateChanged(evt);
            }
        });
        jPanel1.add(cmbZone);
        cmbZone.setBounds(50, 10, 130, 20);

        Export_WeavingProduction.setText("Export to Excel");
        Export_WeavingProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Export_WeavingProductionActionPerformed(evt);
            }
        });
        jPanel1.add(Export_WeavingProduction);
        Export_WeavingProduction.setBounds(1120, 330, 160, 20);

        txtCurrentSchMonth.setEnabled(false);
        jPanel1.add(txtCurrentSchMonth);
        txtCurrentSchMonth.setBounds(660, 20, 80, 20);

        txtYear.setEnabled(false);
        jPanel1.add(txtYear);
        txtYear.setBounds(620, 20, 40, 20);

        txtMonth.setEnabled(false);
        jPanel1.add(txtMonth);
        txtMonth.setBounds(590, 20, 30, 20);

        lblSelectedPartyName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(lblSelectedPartyName);
        lblSelectedPartyName.setBounds(390, 330, 260, 20);

        jLabel10.setText("Clubbed Piece");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(10, 380, 120, 20);

        AddPiece.setText("Add");
        AddPiece.setEnabled(false);
        AddPiece.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddPieceActionPerformed(evt);
            }
        });
        jPanel1.add(AddPiece);
        AddPiece.setBounds(1030, 10, 110, 20);

        btnShowData.setText("Show Data");
        btnShowData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowDataActionPerformed(evt);
            }
        });
        jPanel1.add(btnShowData);
        btnShowData.setBounds(430, 10, 130, 20);

        txtFollowupDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();
        jPanel1.add(txtFollowupDate);
        txtFollowupDate.setBounds(320, 10, 100, 20);

        jLabel13.setText("Follow Up Date");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(190, 10, 130, 15);

        jLabel14.setText("Party");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(240, 330, 70, 15);

        lblSelectedPiece.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(lblSelectedPiece);
        lblSelectedPiece.setBounds(140, 330, 90, 20);

        lblSelectedParty.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(lblSelectedParty);
        lblSelectedParty.setBounds(290, 330, 100, 20);

        jLabel15.setText("Length");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(400, 350, 70, 20);

        txtWidth.setEditable(false);
        jPanel1.add(txtWidth);
        txtWidth.setBounds(570, 350, 50, 20);

        jLabel16.setText("Width");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(520, 350, 60, 20);

        txtMachine.setEditable(false);
        jPanel1.add(txtMachine);
        txtMachine.setBounds(150, 350, 50, 20);

        jLabel17.setText("GSM");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(630, 350, 60, 20);

        txtGSM.setEditable(false);
        jPanel1.add(txtGSM);
        txtGSM.setBounds(670, 350, 50, 20);

        jLabel18.setText("Product Code");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(730, 350, 100, 20);

        txtProductCode.setEditable(false);
        jPanel1.add(txtProductCode);
        txtProductCode.setBounds(840, 350, 90, 20);

        jLabel19.setText("Machine");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(80, 350, 80, 20);

        txtLength.setEditable(false);
        jPanel1.add(txtLength);
        txtLength.setBounds(460, 350, 50, 20);

        txtPosition.setEditable(false);
        jPanel1.add(txtPosition);
        txtPosition.setBounds(270, 350, 120, 20);

        jLabel20.setText("Position");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(210, 350, 80, 20);

        tblPieceClubbingMaster.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPieceClubbingMaster.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPieceClubbingMasterKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(tblPieceClubbingMaster);

        jPanel1.add(jScrollPane6);
        jScrollPane6.setBounds(2, 402, 1280, 140);

        jLabel25.setText("Selected   Piece");
        jPanel1.add(jLabel25);
        jLabel25.setBounds(10, 330, 130, 20);
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(0, 380, 900, 0);
        jPanel1.add(jSeparator2);
        jSeparator2.setBounds(0, 370, 1400, 10);

        jLabel26.setText("Current Schedule Month");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(570, 0, 190, 40);

        btnClubbingFollowupSave.setText("SAVE");
        btnClubbingFollowupSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClubbingFollowupSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnClubbingFollowupSave);
        btnClubbingFollowupSave.setBounds(1152, 378, 120, 20);

        txtFWLMonth.setEnabled(false);
        jPanel1.add(txtFWLMonth);
        txtFWLMonth.setBounds(760, 10, 50, 20);

        txtFWLYear.setEnabled(false);
        jPanel1.add(txtFWLYear);
        txtFWLYear.setBounds(810, 10, 50, 20);

        txtFWLCurrentSchMonth.setEnabled(false);
        jPanel1.add(txtFWLCurrentSchMonth);
        txtFWLCurrentSchMonth.setBounds(860, 10, 120, 20);

        btnShowOrderStatus.setText("Show Order Status");
        btnShowOrderStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowOrderStatusActionPerformed(evt);
            }
        });
        jPanel1.add(btnShowOrderStatus);
        btnShowOrderStatus.setBounds(914, 330, 200, 20);

        Tab.addTab("Sales Follow Up", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(null);

        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        tblHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHistoryMouseClicked(evt);
            }
        });
        tblHistory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblHistoryFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblHistoryFocusLost(evt);
            }
        });
        tblHistory.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                tblHistoryCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        tblHistory.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                tblHistoryAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblHistory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblHistoryKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblHistoryKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblHistory);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(10, 40, 1270, 330);

        jLabel1.setText("INCHARGE");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(20, 10, 90, 15);

        txtInchargeCd.setEnabled(false);
        jPanel2.add(txtInchargeCd);
        txtInchargeCd.setBounds(120, 10, 50, 20);

        txtInchargeName.setEnabled(false);
        jPanel2.add(txtInchargeName);
        txtInchargeName.setBounds(170, 10, 90, 20);

        btnHistory.setText("Show History");
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });
        jPanel2.add(btnHistory);
        btnHistory.setBounds(870, 10, 150, 25);

        jLabel9.setText("Party Code");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(500, 10, 90, 15);

        txtPartyCode_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCode_SearchKeyPressed(evt);
            }
        });
        jPanel2.add(txtPartyCode_Search);
        txtPartyCode_Search.setBounds(580, 10, 100, 20);

        jLabel11.setText("Piece No");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(690, 10, 80, 15);
        jPanel2.add(txtPieceNo_Search);
        txtPieceNo_Search.setBounds(760, 10, 100, 20);

        jLabel21.setText("History Date");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(280, 10, 110, 15);

        txtHistoryDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();
        jPanel2.add(txtHistoryDate);
        txtHistoryDate.setBounds(390, 10, 100, 19);

        jLabel8.setText("Clubbed Pieces");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(20, 370, 140, 20);

        tblClubbedHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tblClubbedHistory);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(13, 393, 1270, 160);

        Tab.addTab("Sales Follow Up Past History", jPanel2);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        tblPieceClubbingHeader.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPieceClubbingHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPieceClubbingHeaderMouseClicked(evt);
            }
        });
        tblPieceClubbingHeader.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblPieceClubbingHeaderFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblPieceClubbingHeaderFocusLost(evt);
            }
        });
        tblPieceClubbingHeader.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                tblPieceClubbingHeaderCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        tblPieceClubbingHeader.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                tblPieceClubbingHeaderAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblPieceClubbingHeader.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPieceClubbingHeaderKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPieceClubbingHeaderKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tblPieceClubbingHeader);

        jPanel4.add(jScrollPane4);
        jScrollPane4.setBounds(10, 70, 1110, 150);

        btnClubbingHistory.setText("Show Data");
        btnClubbingHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClubbingHistoryActionPerformed(evt);
            }
        });
        jPanel4.add(btnClubbingHistory);
        btnClubbingHistory.setBounds(950, 40, 170, 20);

        jLabel5.setText("Clubbing No");
        jPanel4.add(jLabel5);
        jLabel5.setBounds(280, 10, 90, 15);

        txtPartyCode_ClubbingSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCode_ClubbingSearchKeyPressed(evt);
            }
        });
        jPanel4.add(txtPartyCode_ClubbingSearch);
        txtPartyCode_ClubbingSearch.setBounds(630, 10, 100, 20);

        jLabel12.setText("Piece No");
        jPanel4.add(jLabel12);
        jLabel12.setBounds(750, 10, 70, 15);
        jPanel4.add(txtPieceNo_ClubbingSearch);
        txtPieceNo_ClubbingSearch.setBounds(830, 10, 100, 20);

        jLabel22.setText("Party Code");
        jPanel4.add(jLabel22);
        jLabel22.setBounds(530, 10, 90, 15);
        jPanel4.add(txtClubbibgNo);
        txtClubbibgNo.setBounds(380, 10, 130, 20);

        jLabel23.setText("Clubbed Piece Details");
        jPanel4.add(jLabel23);
        jLabel23.setBounds(10, 230, 190, 15);

        tblPieceClubbingDetail.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPieceClubbingDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPieceClubbingDetailMouseClicked(evt);
            }
        });
        tblPieceClubbingDetail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblPieceClubbingDetailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblPieceClubbingDetailFocusLost(evt);
            }
        });
        tblPieceClubbingDetail.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                tblPieceClubbingDetailCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        tblPieceClubbingDetail.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                tblPieceClubbingDetailAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblPieceClubbingDetail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPieceClubbingDetailKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPieceClubbingDetailKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(tblPieceClubbingDetail);

        jPanel4.add(jScrollPane5);
        jScrollPane5.setBounds(10, 250, 1040, 290);

        jLabel24.setText("Clubbing Details");
        jPanel4.add(jLabel24);
        jLabel24.setBounds(10, 50, 150, 15);

        btnRemovePiece.setText("Remove selected Piece");
        btnRemovePiece.setEnabled(false);
        btnRemovePiece.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePieceActionPerformed(evt);
            }
        });
        jPanel4.add(btnRemovePiece);
        btnRemovePiece.setBounds(1070, 290, 200, 25);

        btnAddPiece.setText("Add Piece");
        btnAddPiece.setEnabled(false);
        btnAddPiece.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPieceActionPerformed(evt);
            }
        });
        jPanel4.add(btnAddPiece);
        btnAddPiece.setBounds(1070, 250, 200, 25);

        btnUpdateTransaction.setText("Update Transaction");
        btnUpdateTransaction.setEnabled(false);
        btnUpdateTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateTransactionActionPerformed(evt);
            }
        });
        jPanel4.add(btnUpdateTransaction);
        btnUpdateTransaction.setBounds(1070, 330, 200, 25);

        jLabel2.setText("Selected Party Code");
        jPanel4.add(jLabel2);
        jLabel2.setBounds(190, 230, 150, 15);

        txtSelectedClubbingPartyCode.setEnabled(false);
        jPanel4.add(txtSelectedClubbingPartyCode);
        txtSelectedClubbingPartyCode.setBounds(350, 230, 90, 20);

        jLabel3.setText("Selected Document");
        jPanel4.add(jLabel3);
        jLabel3.setBounds(470, 230, 150, 15);

        txtSelectedClubbingNo.setEnabled(false);
        jPanel4.add(txtSelectedClubbingNo);
        txtSelectedClubbingNo.setBounds(610, 230, 190, 20);

        jLabel6.setText("Approval Status ");
        jPanel4.add(jLabel6);
        jLabel6.setBounds(810, 230, 120, 15);

        txtApprovalStatus.setEnabled(false);
        jPanel4.add(txtApprovalStatus);
        txtApprovalStatus.setBounds(930, 228, 120, 20);

        txtInchargeCd_Clubbing.setEnabled(false);
        jPanel4.add(txtInchargeCd_Clubbing);
        txtInchargeCd_Clubbing.setBounds(120, 10, 50, 20);

        txtInchargeName_Clubbing.setEnabled(false);
        jPanel4.add(txtInchargeName_Clubbing);
        txtInchargeName_Clubbing.setBounds(170, 10, 90, 20);

        jLabel7.setText("INCHARGE");
        jPanel4.add(jLabel7);
        jLabel7.setBounds(20, 10, 90, 15);

        btnClubbingAmend.setText("Clubbing Amend");
        btnClubbingAmend.setEnabled(false);
        btnClubbingAmend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClubbingAmendActionPerformed(evt);
            }
        });
        jPanel4.add(btnClubbingAmend);
        btnClubbingAmend.setBounds(1070, 370, 200, 25);

        jLabel27.setText("Status");
        jPanel4.add(jLabel27);
        jLabel27.setBounds(940, 10, 47, 20);

        chkINSTOCK.setSelected(true);
        chkINSTOCK.setText("IN STOCK/BSR");
        chkINSTOCK.setVerifyInputWhenFocusTarget(false);
        jPanel4.add(chkINSTOCK);
        chkINSTOCK.setBounds(1000, 10, 120, 23);

        chkWIP.setSelected(true);
        chkWIP.setText("WIP");
        jPanel4.add(chkWIP);
        chkWIP.setBounds(1120, 10, 52, 23);

        chkINVOICED.setText("INVOICED");
        jPanel4.add(chkINVOICED);
        chkINVOICED.setBounds(1180, 10, 90, 23);

        Tab.addTab("Piece Clubbing Details", jPanel4);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        Table_OrderFUP.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        Table_OrderFUP.setModel(new javax.swing.table.DefaultTableModel(
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
        Table_OrderFUP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_OrderFUPMouseClicked(evt);
            }
        });
        Table_OrderFUP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Table_OrderFUPFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                Table_OrderFUPFocusLost(evt);
            }
        });
        Table_OrderFUP.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                Table_OrderFUPCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        Table_OrderFUP.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                Table_OrderFUPAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        Table_OrderFUP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Table_OrderFUPKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_OrderFUPKeyReleased(evt);
            }
        });
        jScrollPane7.setViewportView(Table_OrderFUP);

        jPanel3.add(jScrollPane7);
        jScrollPane7.setBounds(10, 90, 1270, 410);

        btnSave_OrderFUP.setText("SAVE");
        btnSave_OrderFUP.setEnabled(false);
        btnSave_OrderFUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_OrderFUPActionPerformed(evt);
            }
        });
        jPanel3.add(btnSave_OrderFUP);
        btnSave_OrderFUP.setBounds(1150, 10, 130, 20);

        jLabel28.setText("Area");
        jPanel3.add(jLabel28);
        jLabel28.setBounds(10, 10, 50, 20);

        cmbZone_OrderFUP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbZone_OrderFUP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbZone_OrderFUPItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbZone_OrderFUP);
        cmbZone_OrderFUP.setBounds(50, 10, 130, 20);

        Export_OrderFUP.setText("Export to Excel");
        Export_OrderFUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Export_OrderFUPActionPerformed(evt);
            }
        });
        jPanel3.add(Export_OrderFUP);
        Export_OrderFUP.setBounds(1120, 510, 160, 20);

        lblOFUP_PartyName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.add(lblOFUP_PartyName);
        lblOFUP_PartyName.setBounds(390, 510, 260, 20);

        btnShowData_OrderFUP.setText("Show Data");
        btnShowData_OrderFUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowData_OrderFUPActionPerformed(evt);
            }
        });
        jPanel3.add(btnShowData_OrderFUP);
        btnShowData_OrderFUP.setBounds(430, 10, 130, 20);

        txtOrderFUPDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();
        jPanel3.add(txtOrderFUPDate);
        txtOrderFUPDate.setBounds(320, 10, 100, 20);

        jLabel30.setText("Follow Up Date");
        jPanel3.add(jLabel30);
        jLabel30.setBounds(190, 10, 130, 15);

        jLabel31.setText("Party");
        jPanel3.add(jLabel31);
        jLabel31.setBounds(240, 510, 70, 15);

        lblOFUP_UPN.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.add(lblOFUP_UPN);
        lblOFUP_UPN.setBounds(100, 510, 130, 20);

        lblOFUP_PartyCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.add(lblOFUP_PartyCode);
        lblOFUP_PartyCode.setBounds(290, 510, 100, 20);

        jLabel32.setText("Length");
        jPanel3.add(jLabel32);
        jLabel32.setBounds(400, 530, 70, 20);

        txtOFUP_Width.setEditable(false);
        jPanel3.add(txtOFUP_Width);
        txtOFUP_Width.setBounds(570, 530, 50, 20);

        jLabel33.setText("Width");
        jPanel3.add(jLabel33);
        jLabel33.setBounds(520, 530, 60, 20);

        txtOFUP_Machine.setEditable(false);
        jPanel3.add(txtOFUP_Machine);
        txtOFUP_Machine.setBounds(80, 530, 50, 20);

        jLabel34.setText("GSM");
        jPanel3.add(jLabel34);
        jLabel34.setBounds(630, 530, 60, 20);

        txtOFUP_GSM.setEditable(false);
        jPanel3.add(txtOFUP_GSM);
        txtOFUP_GSM.setBounds(670, 530, 50, 20);

        jLabel35.setText("Product Code");
        jPanel3.add(jLabel35);
        jLabel35.setBounds(730, 530, 100, 20);

        txtOFUP_ProductCode.setEditable(false);
        jPanel3.add(txtOFUP_ProductCode);
        txtOFUP_ProductCode.setBounds(840, 530, 90, 20);

        jLabel36.setText("Machine");
        jPanel3.add(jLabel36);
        jLabel36.setBounds(10, 530, 80, 20);

        txtOFUP_Length.setEditable(false);
        jPanel3.add(txtOFUP_Length);
        txtOFUP_Length.setBounds(460, 530, 50, 20);

        txtOFUP_Position.setEditable(false);
        jPanel3.add(txtOFUP_Position);
        txtOFUP_Position.setBounds(210, 530, 180, 20);

        jLabel37.setText("Position");
        jPanel3.add(jLabel37);
        jLabel37.setBounds(140, 530, 80, 20);

        jLabel38.setText("UPN Detail");
        jPanel3.add(jLabel38);
        jLabel38.setBounds(10, 510, 90, 20);
        jPanel3.add(jSeparator3);
        jSeparator3.setBounds(0, 380, 900, 0);

        lblCPRSMonth.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCPRSMonth.setText("CPRS Month  ");
        jPanel3.add(lblCPRSMonth);
        lblCPRSMonth.setBounds(1160, 40, 30, 20);

        txtOrderFUPMonth.setEnabled(false);
        jPanel3.add(txtOrderFUPMonth);
        txtOrderFUPMonth.setBounds(1190, 40, 20, 20);

        txtOrderFUPYear.setEnabled(false);
        jPanel3.add(txtOrderFUPYear);
        txtOrderFUPYear.setBounds(1210, 40, 20, 20);

        txtOrderFUP_CPRS_Month.setEnabled(false);
        jPanel3.add(txtOrderFUP_CPRS_Month);
        txtOrderFUP_CPRS_Month.setBounds(1230, 40, 40, 20);

        btnBooking_OrderFUP.setText("Sales Order Booking");
        btnBooking_OrderFUP.setEnabled(false);
        btnBooking_OrderFUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBooking_OrderFUPActionPerformed(evt);
            }
        });
        jPanel3.add(btnBooking_OrderFUP);
        btnBooking_OrderFUP.setBounds(880, 10, 200, 20);

        btnPieceDiv_OrderFUP.setText("Piece Diversion");
        btnPieceDiv_OrderFUP.setEnabled(false);
        btnPieceDiv_OrderFUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPieceDiv_OrderFUPActionPerformed(evt);
            }
        });
        jPanel3.add(btnPieceDiv_OrderFUP);
        btnPieceDiv_OrderFUP.setBounds(880, 40, 200, 20);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Party Code");
        jPanel3.add(jLabel39);
        jLabel39.setBounds(10, 40, 90, 20);

        txtOrderFUPpartycode.setToolTipText("Press F1 key for search Party Code");
        txtOrderFUPpartycode = new JTextFieldHint(new JTextField(),"Search by F1");
        txtOrderFUPpartycode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOrderFUPpartycodeFocusLost(evt);
            }
        });
        txtOrderFUPpartycode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOrderFUPpartycodeKeyPressed(evt);
            }
        });
        jPanel3.add(txtOrderFUPpartycode);
        txtOrderFUPpartycode.setBounds(110, 40, 70, 20);

        txtOrderFUPpartyname.setDisabledTextColor(java.awt.Color.black);
        txtOrderFUPpartyname = new JTextFieldHint(new JTextField(),"Party Name");
        txtOrderFUPpartyname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderFUPpartynameActionPerformed(evt);
            }
        });
        jPanel3.add(txtOrderFUPpartyname);
        txtOrderFUPpartyname.setBounds(180, 40, 240, 20);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Group Code");
        jPanel3.add(jLabel41);
        jLabel41.setBounds(10, 60, 90, 20);

        txtOrderFUPgroupcode.setToolTipText("Press F1 key for search Group Code");
        txtOrderFUPgroupcode = new JTextFieldHint(new JTextField(),"Search by F1");
        txtOrderFUPgroupcode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOrderFUPgroupcodeFocusLost(evt);
            }
        });
        txtOrderFUPgroupcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOrderFUPgroupcodeKeyPressed(evt);
            }
        });
        jPanel3.add(txtOrderFUPgroupcode);
        txtOrderFUPgroupcode.setBounds(110, 60, 70, 20);

        txtOrderFUPgroupname.setDisabledTextColor(java.awt.Color.black);
        txtOrderFUPgroupname = new JTextFieldHint(new JTextField(),"Group Name");
        txtOrderFUPgroupname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderFUPgroupnameActionPerformed(evt);
            }
        });
        jPanel3.add(txtOrderFUPgroupname);
        txtOrderFUPgroupname.setBounds(180, 60, 240, 20);

        jLabel43.setText("Prod. Category");
        jPanel3.add(jLabel43);
        jLabel43.setBounds(430, 40, 120, 15);

        txtOrderFUPprodcategory.setToolTipText("Press F1 key for search Product Category");
        txtOrderFUPprodcategory = new JTextFieldHint(new JTextField(),"Search by F1");
        txtOrderFUPprodcategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOrderFUPprodcategoryKeyPressed(evt);
            }
        });
        jPanel3.add(txtOrderFUPprodcategory);
        txtOrderFUPprodcategory.setBounds(550, 40, 110, 20);

        jLabel44.setText("CPRS Month");
        jPanel3.add(jLabel44);
        jLabel44.setBounds(430, 60, 130, 15);

        txtOrderFUPCPRSMonth.setToolTipText("Press F1 key for search CPRS Month");
        txtOrderFUPCPRSMonth = new JTextFieldHint(new JTextField(),"Search by F1");
        txtOrderFUPCPRSMonth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOrderFUPCPRSMonthKeyPressed(evt);
            }
        });
        jPanel3.add(txtOrderFUPCPRSMonth);
        txtOrderFUPCPRSMonth.setBounds(550, 60, 110, 20);

        Order_FUP_Rpt.setText("Report");
        Order_FUP_Rpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Order_FUP_RptActionPerformed(evt);
            }
        });
        jPanel3.add(Order_FUP_Rpt);
        Order_FUP_Rpt.setBounds(730, 10, 82, 20);

        Tab.addTab("Order Follow Up", jPanel3);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        tblOFUP_History.setModel(new javax.swing.table.DefaultTableModel(
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
        tblOFUP_History.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOFUP_HistoryMouseClicked(evt);
            }
        });
        tblOFUP_History.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblOFUP_HistoryFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblOFUP_HistoryFocusLost(evt);
            }
        });
        tblOFUP_History.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                tblOFUP_HistoryCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        tblOFUP_History.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                tblOFUP_HistoryAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblOFUP_History.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblOFUP_HistoryKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblOFUP_HistoryKeyReleased(evt);
            }
        });
        jScrollPane8.setViewportView(tblOFUP_History);

        jPanel6.add(jScrollPane8);
        jScrollPane8.setBounds(10, 40, 1270, 510);

        jLabel29.setText("INCHARGE");
        jPanel6.add(jLabel29);
        jLabel29.setBounds(20, 10, 90, 15);

        txtInchargeCd_OFUPH.setEnabled(false);
        jPanel6.add(txtInchargeCd_OFUPH);
        txtInchargeCd_OFUPH.setBounds(120, 10, 50, 20);

        txtInchargeName_OFUPH.setEnabled(false);
        jPanel6.add(txtInchargeName_OFUPH);
        txtInchargeName_OFUPH.setBounds(170, 10, 90, 20);

        btnOFUP_History.setText("Show History");
        btnOFUP_History.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOFUP_HistoryActionPerformed(evt);
            }
        });
        jPanel6.add(btnOFUP_History);
        btnOFUP_History.setBounds(870, 10, 150, 25);

        jLabel40.setText("Party Code");
        jPanel6.add(jLabel40);
        jLabel40.setBounds(500, 10, 90, 15);

        txtOFUP_PartyCode_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOFUP_PartyCode_SearchKeyPressed(evt);
            }
        });
        jPanel6.add(txtOFUP_PartyCode_Search);
        txtOFUP_PartyCode_Search.setBounds(580, 10, 100, 20);

        jLabel42.setText("History Date");
        jPanel6.add(jLabel42);
        jLabel42.setBounds(280, 10, 110, 15);

        txtOFUP_HistoryDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();
        jPanel6.add(txtOFUP_HistoryDate);
        txtOFUP_HistoryDate.setBounds(390, 10, 100, 19);

        Tab.addTab("Order Follow Up Past History", jPanel6);

        getContentPane().add(Tab);
        Tab.setBounds(0, 40, 1500, 590);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Felt Followup System");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 10, 1430, 25);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 580, 930, 22);

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus1);
        lblStatus1.setBounds(20, 590, 740, 30);
        getContentPane().add(jPanel5);
        jPanel5.setBounds(210, 50, 10, 10);
        getContentPane().add(ltbPink);
        ltbPink.setBounds(10, 70, 0, 0);
    }// </editor-fold>//GEN-END:initComponents

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        if (evt.getKeyCode() == 32 && Table.getSelectedColumn() == 1) {
//          if (Table.getValueAt(Table.getSelectedRow(), 1).equals(false)) {
//            //JOptionPane.showMessageDialog(null, "Only Sales can confirm");
//            DataModel.setValueByVariable("SELECT", true, Table.getSelectedRow());
//          }
            if (Table.getValueAt(Table.getSelectedRow(), 1).equals(true)) {
                //JOptionPane.showMessageDialog(null, "Only Sales can confirm");
                DataModel.setValueByVariable("SELECT", false, Table.getSelectedRow());
            }
        }

        if (Table.getSelectedColumn() == 17 && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER  where PARA_ID = 'SALES_FOLLOWUP'";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    //PARTY_CODE.setText(aList.ReturnVal);
                    DataModel.setValueByVariable("REMARK", aList.ReturnVal, Table.getSelectedRow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }
        //DataModel.SetVariable(19, "CONTACTED_PERSON");
        //    DataModel.SetVariable(20, "CONTACTED_NO");
        if (Table.getSelectedColumn() == 19 && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT distinct CONTACT_PERSON as CONTACT_PERSON,CONTACTED_NO FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY where PARTY_CODE=" + DataModel.getValueByVariable("PARTY_CODE", Table.getSelectedRow()) + ""
                        + "  UNION ALL SELECT '','' FROM DUAL"
                        + "";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;
                aList.SecondCol = 2;
                if (aList.ShowLOV()) {
                    String value = "";
                    String value_number = "";
                    if (aList.ReturnVal.equals("")) {
                        value = JOptionPane.showInputDialog(this, "Please enter Contact Person : ");
                    } else {
                        value = aList.ReturnVal;
                        value_number = aList.SecondVal;
                    }
                    DataModel.setValueByVariable("CONTACTED_PERSON", value, Table.getSelectedRow());
                    DataModel.setValueByVariable("CONTACTED_NO", value_number, Table.getSelectedRow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }
        if (Table.getSelectedColumn() == 20 && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT distinct CONTACTED_NO FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY where PARTY_CODE=" + DataModel.getValueByVariable("PARTY_CODE", Table.getSelectedRow()) + " "
                        + "  UNION ALL SELECT '' FROM DUAL";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    //PARTY_CODE.setText(aList.ReturnVal);
                    String value = "";
                    if (aList.ReturnVal.equals("")) {
                        value = JOptionPane.showInputDialog(this, "Please enter Contact Number : ");
                    } else {
                        value = aList.ReturnVal;
                    }
                    DataModel.setValueByVariable("CONTACTED_NO", value, Table.getSelectedRow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }
    }//GEN-LAST:event_TableKeyPressed


    private void TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabMouseClicked


    }//GEN-LAST:event_TabMouseClicked

    private void TableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusGained
        // TODO add your handling code here:

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

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:
        //jdbc:mysql://200.0.0.230:3306/PRODUCTION
        //System.out.println("1 "+DataModel.getValueByVariable("SELECT", Table.getSelectedRow()));

        if (Table.getSelectedColumn() == 1 && Table.getValueAt(Table.getSelectedRow(), 1).equals(false)) {
            //JOptionPane.showMessageDialog(null, "Only Sales can confirm");
            DataModel.setValueByVariable("SELECT", true, Table.getSelectedRow());
            return;
        }

//        if (evt.getClickCount() == 2 && Table.getSelectedColumn() == 2 && !DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow()).equals("")) {
//            String PieceNo = DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow());
//
//            AppletFrame aFrame = new AppletFrame("Felt Piece Details");
//            aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceMaster.FrmPieceMasterDetail", "Felt Piece Details");
//            FrmPieceMasterDetail ObjItem = (FrmPieceMasterDetail) aFrame.ObjApplet;
//
//            ObjItem.requestFocus();
//            ObjItem.setData(PieceNo);
//        }
        if (evt.getClickCount() == 2 && Table.getSelectedColumn() == 2 && !DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow()).equals("")) {
            //String PieceNo = DataModel_History.getValueByVariable("PIECE_NO", tblHistory.getSelectedRow());
            AppletFrame aFrame = new AppletFrame("Follow Up History");
            aFrame.startAppletEx("EITLERP.FeltSales.SpilloverRescheduling_New.frmPieceReschedulingDetails_New", "Follow Up History");
            frmPieceReschedulingDetails_New ObjItem = (frmPieceReschedulingDetails_New) aFrame.ObjApplet;

            String PieceNo = DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow());
            //System.out.println(" PieceNo "+PieceNo);
            ObjItem.requestFocus();
            // ObjItem.Doc_No=DFNo.getText();
            ObjItem.FindByPieceNo(PieceNo);
        }

        if (evt.getClickCount() == 2 && Table.getSelectedColumn() == 3 && !DataModel.getValueByVariable("PARTY_CODE", Table.getSelectedRow()).equals("")) {
            AppletFrame aFrame = new AppletFrame("Select Piece for Clubbing");
            aFrame.startAppletEx("EITLERP.FeltSales.SalesFollowup.frmPieceClubbingDetails_Selection", "Select Piece for Clubbing");
            frmPieceClubbingDetails_Selection ObjItem = (frmPieceClubbingDetails_Selection) aFrame.ObjApplet;

            String PartyCode = DataModel.getValueByVariable("PARTY_CODE", Table.getSelectedRow());
            //System.out.println(" PieceNo "+PieceNo);
            ObjItem.requestFocus();
            // ObjItem.Doc_No=DFNo.getText();
            ObjItem.FindByPartyCode(PartyCode, ClubbingEditRight);
        }

        try {

            String PartyCode = DataModel.getValueByVariable("PARTY_CODE", Table.getSelectedRow());
            //txtPartyCode.setText(PartyCode);
            //txtPartyName.setText(DataModel.getValueByVariable("PARTY_NAME", Table.getSelectedRow()));
            lblSelectedPiece.setText(DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow()));
            lblSelectedParty.setText(PartyCode);
            lblSelectedPartyName.setText(DataModel.getValueByVariable("PARTY_NAME", Table.getSelectedRow()));

            ResultSet rsData1 = data.getResult("SELECT PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_PRODUCT_CODE,PR_MACHINE_NO,PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow()) + "'");
            txtLength.setText(rsData1.getString("PR_BILL_LENGTH"));
            txtWidth.setText(rsData1.getString("PR_BILL_WIDTH"));
            txtGSM.setText(rsData1.getString("PR_BILL_GSM"));
            txtProductCode.setText(rsData1.getString("PR_BILL_PRODUCT_CODE"));
            txtMachine.setText(rsData1.getString("PR_MACHINE_NO"));
            txtPosition.setText(rsData1.getString("PR_POSITION_NO") + " - " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("PR_POSITION_NO") + "'"));

        } catch (Exception e) {
            e.printStackTrace();
        }

//        FormatGrid_cl_piece();
//        try {
//            String PartyCode = DataModel.getValueByVariable("PARTY_CODE", Table.getSelectedRow());
//            txtPartyCode.setText(PartyCode);
//            txtPartyName.setText(DataModel.getValueByVariable("PARTY_NAME", Table.getSelectedRow()));
//            lblSelectedPiece.setText(DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow()));
//            lblSelectedParty.setText(PartyCode);
//            lblSelectedPartyName.setText(DataModel.getValueByVariable("PARTY_NAME", Table.getSelectedRow()));
//
//            ResultSet rsData1 = data.getResult("SELECT PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_PRODUCT_CODE,PR_MACHINE_NO,PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow()) + "'");
//            txtLength.setText(rsData1.getString("PR_BILL_LENGTH"));
//            txtWidth.setText(rsData1.getString("PR_BILL_WIDTH"));
//            txtGSM.setText(rsData1.getString("PR_BILL_GSM"));
//            txtProductCode.setText(rsData1.getString("PR_BILL_PRODUCT_CODE"));
//            txtMachine.setText(rsData1.getString("PR_MACHINE_NO"));
//            txtPosition.setText(rsData1.getString("PR_POSITION_NO") + " - " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData1.getString("PR_POSITION_NO") + "'"));
//
//            ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PartyCode + "' "
//                    + "AND PR_PIECE_STAGE IN ('IN STOCK','SEAMING','NEEDLING','MENDING','FINISHING','OSG STOCK','WEAVING','PLANNING','BSR','SPIRALLING','ASSEMBLY') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)");
//            rsData.first();
//            System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + PartyCode + "' "
//                    + "AND PR_PIECE_STAGE IN ('IN STOCK','SEAMING','NEEDLING','MENDING','FINISHING','OSG STOCK','WEAVING','PLANNING','BSR','SPIRALLING','ASSEMBLY') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)");
//            int srNo = 0;
//            while (!rsData.isAfterLast()) {
//                srNo++;
//                int NewRow = srNo - 1;
//
//                Object[] rowData = new Object[1];
//                DataModel_cl_piece.addRow(rowData);
//
//                DataModel_cl_piece.setValueByVariable("SrNo", srNo + "", NewRow);
//
//                DataModel_cl_piece.setValueByVariable("PIECE_NO", rsData.getString("PR_PIECE_NO"), NewRow);
//                DataModel_cl_piece.setValueByVariable("CLUBBING", false, NewRow);
//                DataModel_cl_piece.setValueByVariable("OC_MONTH", rsData.getString("PR_OC_MONTHYEAR"), NewRow);
//                DataModel_cl_piece.setValueByVariable("CURRENT_SCH_MONTH", rsData.getString("PR_CURRENT_SCH_MONTH"), NewRow);
//                DataModel_cl_piece.setValueByVariable("PIECE_STAGE", rsData.getString("PR_PIECE_STAGE"), NewRow);
//                DataModel_cl_piece.setValueByVariable("FINISHING_DATE", rsData.getString("PR_FNSG_DATE"), NewRow);
//                DataModel_cl_piece.setValueByVariable("WH_DAYS", data.getStringValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.getCurrentDateDB() + "','" + rsData.getString("PR_FNSG_DATE") + "')"), NewRow);
//
//                rsData.next();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }//GEN-LAST:event_TableMouseClicked


    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == 38) {
            lblSelectedPiece.setText(DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow()));
            lblSelectedParty.setText(DataModel.getValueByVariable("PARTY_CODE", Table.getSelectedRow()));
            lblSelectedPartyName.setText(DataModel.getValueByVariable("PARTY_NAME", Table.getSelectedRow()));
            try {
                ResultSet rsData = data.getResult("SELECT PR_BILL_LENGTH,PR_BILL_WIDTH,PR_BILL_GSM,PR_BILL_PRODUCT_CODE,PR_MACHINE_NO,PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + DataModel.getValueByVariable("PIECE_NO", Table.getSelectedRow()) + "'");
                txtLength.setText(rsData.getString("PR_BILL_LENGTH"));
                txtWidth.setText(rsData.getString("PR_BILL_WIDTH"));
                txtGSM.setText(rsData.getString("PR_BILL_GSM"));
                txtProductCode.setText(rsData.getString("PR_BILL_PRODUCT_CODE"));
                txtMachine.setText(rsData.getString("PR_MACHINE_NO"));
                txtPosition.setText(rsData.getString("PR_POSITION_NO") + " - " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='" + rsData.getString("PR_POSITION_NO") + "'"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_TableKeyReleased

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:

        int Selected_Month = EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
        int Selected_Year = EITLERPGLOBAL.getYear(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));

        boolean MONTH_CHANGE_SAVED = false;

        if (Selected_Month != EITLERPGLOBAL.getCurrentMonth() || Selected_Year != EITLERPGLOBAL.getCurrentYear()) {

            for (int i = 0; i <= Table.getRowCount() - 1; i++) {

                String selected_monthyear = DataModel.getValueByVariable("EXPECTED_MONTH_OF_DISPATCH", i);
                String PIECE_NO = DataModel.getValueByVariable("PIECE_NO", i);

                String Current_Month = txtFWLCurrentSchMonth.getText();

                if (!selected_monthyear.equals(Current_Month)) {
                    String date_selected_monthyear = EITLERPGLOBAL.formatDateDB(get1stDate(selected_monthyear));
                    String date_Current_Month = EITLERPGLOBAL.formatDateDB(get1stDate(Current_Month));
                    int date_diff = data.getIntValueFromDB("SELECT DATEDIFF('" + date_selected_monthyear + "','" + date_Current_Month + "')");

                    if (date_diff < 0) {
                        // data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL SET EXPECTED_MONTH_OF_DISPATCH='"+selected_monthyear+"' WHERE DOC_NO='" + CLUBBING_NO + "'");

                        if (!data.IsRecordExist("SELECT * FROM PRODUCTION.SPILLOVER_RESCHEDULING_NEW_DETAIL WHERE PIECE_NO='" + PIECE_NO + "' AND UPDATE_RE_SCH_MONTH='" + selected_monthyear + "'")) {
                            String Piece_OC_MONTH = data.getStringValueFromDB("SELECT PR_OC_MONTHYEAR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                            String Piece_Curr_sch_month = data.getStringValueFromDB("SELECT PR_CURRENT_SCH_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                            if (clsValidator.isCurrentSchMonthValid(selected_monthyear, Piece_OC_MONTH)) {

                                try {
                                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_CURRENT_SCH_MONTH='" + selected_monthyear + "' WHERE PR_PIECE_NO='" + PIECE_NO + "' ");
                                    data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_CURRENT_SCH_MONTH='" + selected_monthyear + "' WHERE WIP_PIECE_NO='" + PIECE_NO + "' ");

                                    data.Execute("INSERT INTO PRODUCTION.FELT_SALES_PIECE_MONTH_CHANGE_HISTORY\n"
                                            + "(PIECE_NO, OLD_OC_MONTH, OLD_CURR_SCH_MONTH, NEW_OC_MONTH, NEW_CURR_SCH_MONTH, CHANGE_REMARK, "
                                            + "USER_ID, FORM_NAME, ENTRY_DATE)\n"
                                            + "VALUES\n"
                                            + "('" + PIECE_NO + "', '" + Piece_OC_MONTH + "', '" + Piece_Curr_sch_month + "', '" + Piece_OC_MONTH + "', '" + selected_monthyear + "', 'UPDATE CURR SCH MONTH ONLY', "
                                            + "'" + EITLERPGLOBAL.gNewUserID + "', 'FOLLOWUP_ENTRY', '" + EITLERPGLOBAL.getCurrentDateTimeDB() + "')");

                                    SelectFirstFree aList = new SelectFirstFree();
                                    aList.ModuleID = 867;
                                    aList.FirstFreeNo = 381;
                                    String Spilover_Doc_No = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 867, aList.FirstFreeNo, true);

                                    data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA"
                                            + " (MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) "
                                            + " VALUES "
                                            + " ('867', '" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '28', 'F', 'F', '', '1', '28', '', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00')");
                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_HEADER "
                                            + "(             DOC_NO, DOC_DATE, REMARK, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS) "
                                            + " VALUES "
                                            + "('" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 'Entry by followup nextmonth', '4789', '28', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00', 1, '0', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, 0, '0000-00-00', 0, 0, '0000-00-00', '') ");

                                    ResultSet rsTmp = data.getResult("SELECT USER()");
                                    rsTmp.first();
                                    String str = rsTmp.getString(1);
                                    String str_split[] = str.split("@");
                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_HEADER_H "
                                            + "(REVISION_NO, DOC_NO, DOC_DATE, REMARK, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS, FROM_IP, APPROVAL_STATUS, APPROVER_REMARKS, ENTRY_DATE) "
                                            + " VALUES "
                                            + "(1,'" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 'Entry by followup nextmonth', '4789', '28', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00', 1, '0', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, 0, '0000-00-00', 0, 0, '" + EITLERPGLOBAL.getCurrentDateDB() + "', '','" + str_split[1] + "','F','Postpond Schedule, Auto Approved','" + EITLERPGLOBAL.getCurrentDateTimeDB() + "') ");
                                    ResultSet rsDataPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_DETAIL "
                                            + " (DOC_NO, SR_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, MACHINE_NO, POSITION, UPN, REQUESTED_MONTH, OC_MONTH, CURR_SCH_MONTH, RE_SCH_MONTH, PRODUCT_CODE, BILL_PRODUCT_CODE, PR_GROUP, STYLE, BILL_STYLE, LENGTH, ACTUAL_LENGTH, BILL_LENGTH, WIDTH, ACTUAL_WIDTH, SQMTR, BILL_SQMTR, GSM, BILL_GSM, TENDER_GSM, TH_WEIGHT, TENDER_WEIGHT, ACTUAL_WEIGHT, BILL_WEIGHT, WEAVING_WEIGHT, NEEDLING_WEIGHT, SEAM_WEIGHT, SPLICE_WEIGHT, WEAVING_DATE, MANDING_DATE, NEEDLING_DATE, INCHARGE, PIECE_STAGE, PIECE_OBSOLETE, OBSOLETE_DATE, REQUESTED_MONTH_DATE, OC_MONTH_DATE, CURR_SCH_MONTH_DATE, RE_SCH_MONTH_DATE, CUR_PIECE_STAGE, OC_FALL_MONTH, OC_FALL_MONTH_DATE, INVOICE_NO, INVOICE_DATE, DOC_MONTH, DOC_YEAR, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, FINISHING_DATE, UPDATE_RE_SCH_MONTH) "
                                            + " VALUES "
                                            // PR_PRODUCT_CODE, PR_BILL_PRODUCT_CODE, PR_GROUP, PR_SYN_PER, PR_STYLE, PR_BILL_STYLE, PR_LENGTH, PR_ACTUAL_LENGTH, PR_BILL_LENGTH, PR_WIDTH, PR_ACTUAL_WIDTH, PR_BILL_WIDTH, PR_SQMTR, PR_BILL_SQMTR, PR_GSM, PR_TENDER_GSM, PR_BILL_GSM, PR_THORITICAL_WEIGHT, PR_TENDER_WEIGHT, PR_ACTUAL_WEIGHT, PR_BILL_WEIGHT, PR_FELT_VALUE_WITH_GST, PR_FELT_VALUE_WITHOUT_GST, PR_FELT_BASE_VALUE, PR_INCHARGE, PR_PIECE_AB_FLAG, PR_REQUESTED_MONTH, PR_REQ_MTH_LAST_DDMMYY, PR_REQ_MONTH_ORIGINAL, PR_REQ_MONTH_REMARKS, PR_PRIORITY_HOLD_CAN_FLAG, PR_PIECE_STAGE, PR_WIP_STATUS, PR_GIDC_STATUS, PR_WARP_DATE, PR_WVG_DATE, PR_SPLICE_DATE, PR_MND_DATE, PR_NDL_DATE, PR_SEAM_DATE, PR_FNSG_DATE, PR_RCV_DATE, PR_WARP_A_DATE, PR_WARP_B_DATE, PR_WARP_LAYER_REMARK, PR_WARPING_WEIGHT, PR_WARPING_WEIGHT_A, PR_WARPING_WEIGHT_B, PR_WVG_A_DATE, PR_WVG_B_DATE, WVG_LAYER_REMARK, PR_WEAVING_WEIGHT, PR_WEAVING_WEIGHT_A, PR_WEAVING_WEIGHT_B, PR_SPLICE_WIEGHT, PR_MND_A_DATE, PR_MND_B_DATE, PR_MND_LAYER_REMARK, PR_MENDING_WEIGHT, PR_MENDING_WEIGHT_A, PR_MENDING_WEIGHT_B, PR_NEEDLING_WEIGHT, PR_SEAM_WEIGHT, PR_DIVERSION_FLAG, PR_DIVERSION_REASON, PR_DIVERTED_FLAG, PR_DIVERTED_REASON, PR_DIVERTED_DATE, PR_DIV_BEFORE_STAGE, PR_DIV_NO_CHANGE_DATE, PR_DIV_NO_CHANGE_FLAG, PR_EXP_DISPATCH_DATE, PR_REGION, PR_REFERENCE, PR_REFERENCE_DATE, PR_PO_NO, PR_PO_DATE, PR_ORDER_REMARK, PR_PIECE_REMARK, PR_PKG_DP_NO, PR_PKG_DP_DATE, PR_BALE_NO, PR_PACKED_DATE, PR_INVOICE_NO, PR_INVOICE_DATE, PR_INVOICE_AMOUNT, PR_EXPORT_BL_NO, PR_EXPORT_BL_DATE, PR_EXPORT_UNIT_OF_CURRENCY, PR_EXPORT_EXCHANGE_RATE, PR_EXPORT_INV_AMOUNT, PR_LR_NO, PR_LR_DATE, PR_INVOICE_PARTY, PR_PARTY_CODE_ORIGINAL, PR_PIECE_NO_ORIGINAL, PR_HOLD_DATE, PR_HOLD_REASON, PR_RELEASE_DATE, BALE_REOPEN_FLG, PR_SALES_RETURNS_NO, PR_SALES_RETURNS_DATE, PR_SALES_RETURNS_REMARKS, PR_SALES_RETURNS_FLG, PR_REJECTED_FLAG, PR_REJECTED_REMARK, PR_ADJUSTABLE_LENGTH, PR_ADJUSTABLE_WIDTH, PR_ADJUSTABLE_GSM, PR_ADJUSTABLE_WEIGHT, PR_DELINK, PR_OBSOLETE_DATE, PR_DELINK_REASON, PR_WH_LOCATION_ID, PR_DAYS_ORDER_WARPED, PR_DAYS_ORDER_WARPED_STATUS, PR_DAYS_ORDER_WVG, PR_DAYS_ORDER_WVG_STATUS, PR_DAYS_ORDER_MND, PR_DAYS_ORDER_MND_STATUS, PR_DAYS_ORDER_NDL, PR_DAYS_ORDER_NDL_STATUS, PR_DAYS_ORDER_FNG, PR_DAYS_ORDER_FNG_STATUS, PR_DAYS_WRP_WVG, PR_DAYS_WRP_WVG_STATUS, PR_DAYS_WVG_MND, PR_DAYS_WVG_MND_STATUS, PR_DAYS_MND_NDL, PR_DAYS_MND_NDL_STATUS, PR_DAYS_NDL_FNG, PR_DAYS_NDL_FNG_STATUS, PR_DAYS_MND_FNG, PR_DAYS_MND_FNG_STATUS, PR_DAYS_ORDER_INVOICE, PR_DAYS_ORDER_INVOICE_STATUS, PR_DAYS_STOCK_INVOICE, PR_DAYS_STOCK_INVOICE_STATUS, PR_DAYS_WH_STOCK, PR_DAYS_WH_PACKED, PR_DAYS_STATUS, PR_DAYS_PACK_INVOICE, PR_DAYS_PACK_NOT_INVOICE, PR_DAYS_PACK_NOT_INVOICE_STATUS, PR_DAYS_WH_STOCK_STATUS, PR_DAYS_CURRENT_STAGE, PR_WARP_EXECUTE_DATE, PR_CLOSURE_REOPEN_IND, PR_CLOSURE_DATE, PR_CLOSURE_REMARK, PR_REOPEN_DATE, PR_REOPEN_REMARK, PR_EXPECTED_DISPATCH, PR_EXP_DISPATCH_FROM, PR_EXP_DISPATCH_DOCNO, PE_PIECE_IT_DEPT_REMARK, PR_CONTACT_PERSON, PR_EMAIL_ID, PR_PHONE_NO, PR_OA_MONTHYEAR, PR_OA_NO, PR_OA_DATE, PR_OC_NO, PR_OC_DATE, PR_OC_MONTHYEAR, PR_OC_LAST_DDMMYY, PR_PIECE_OC_MONTH_IT_REMARK, PR_CURRENT_SCH_MONTH, PR_CURRENT_SCH_LAST_DDMMYY, PR_PIECE_CURRENT_MONTH_IT_REMARK, PR_SP_MONTHYEAR, PR_SP_LAST_DDMMYY, PR_SP_REMARKS, PR_EXP_WIP_DELIVERY_DATE, PR_EXP_PI_DATE, PR_ACT_PI_DATE, PR_EXP_DESPATCH_DATE, PR_EXP_PAY_CHQRC_DATE, PR_ACT_PAY_CHQRC_DATE, PR_EXP_PAY_CHQRC_DATE_ORIGINAL, PR_SPL_REQUEST_MONTHYEAR, PR_SPL_REQUEST_DATE, PR_SPL_REQUEST_REMARK, PR_PAYMENT_RCVD_VOUCHER, PR_PAYMENT_SHORT_AMOUNT, PR_PAYMENT_RCVD_AMOUNT, PR_PIECETRIAL_FLAG, PR_PIECETRIAL_CATEGORY, PR_WH_CODE, PR_INWARD_NO, PR_RACK_NO, PR_PIECE_ID, PR_LOCATION, PR_MFG_MONTH, PR_MFG_YEAR, PR_SCHEDULE_MONTH, PR_MFG_SPILL_OVEVER_REMARK, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, HIERARCHY_ID, APPROVER_BY, APPROVER_DATE, APPROVER_REMARK, PR_NEEDLING_WEIGHT_A, PR_NEEDLING_WEIGHT_B, PR_EXP_PAY_CHQRC_REMARKS, PR_EXP_PAY_CHQRC_REMARKS_DATE, PR_EXPORT_PARTY_REMARK, PR_EXPORT_PARTY_ORDERNO, PR_EXPORT_PARTY_PAYMENT_DATE, PR_RATE_INDICATOR, PR_SDF_INSTRUCT_DATE, PR_SDF_SPIRALED_DATE, PR_SDF_ASSEMBLED_DATE, PR_FELT_RATE, PR_FELT_VALUE_WITHOUT_DISCOUNT, PR_RATE_IND_DESPATCH_DATE, PR_SIZE_CRITERIA, PR_PRODUCT_CATG, PR_DFS_IN_DATE, PR_DFS_OUT_DATE, PR_MATERIAL_CODE, PR_OBSOLETE_SOURCE, PR_OBSOLETE_UPN_ASSIGN_STATUS, PR_SCRAP_REASON, PR_UNMAPPED_REASON, PR_WH_EXP_DISPATCH_DATE, PR_WH_EXP_DISPATCH_REMARK, PR_REJECTION_ORIGINATED_FROM, PR_UNABLE_TO_CONTACT, PR_CONTACTED_PERSON, PR_CONTACTED_NO, PR_FOLLOWUP_ADDITIONAL_REMARK
                                            + " ('" + Spilover_Doc_No + "', '" + (i + 1) + "', '" + PIECE_NO + "', '" + rsDataPiece.getString("PR_PARTY_CODE") + "', "
                                            + "'" + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsDataPiece.getString("PR_PARTY_CODE"))
                                            + "', '" + rsDataPiece.getString("PR_MACHINE_NO") + "', '" + rsDataPiece.getString("PR_POSITION_NO") + "', "
                                            + "'" + rsDataPiece.getString("PR_UPN") + "', '" + rsDataPiece.getString("PR_REQUESTED_MONTH") + "', "
                                            + "'" + rsDataPiece.getString("PR_OC_MONTHYEAR") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_MONTH") + "'"
                                            + ", '" + selected_monthyear + "', '" + rsDataPiece.getString("PR_PRODUCT_CODE") + "', "
                                            + "'" + rsDataPiece.getString("PR_BILL_PRODUCT_CODE") + "', '" + rsDataPiece.getString("PR_GROUP") + "', "
                                            + "'" + rsDataPiece.getString("PR_STYLE") + "', '" + rsDataPiece.getString("PR_BILL_STYLE") + "', "
                                            + "'" + rsDataPiece.getString("PR_LENGTH") + "', '" + rsDataPiece.getString("PR_ACTUAL_LENGTH") + "', "
                                            + "'" + rsDataPiece.getString("PR_BILL_LENGTH") + "', '" + rsDataPiece.getString("PR_WIDTH") + "', "
                                            + "'" + rsDataPiece.getString("PR_ACTUAL_WIDTH") + "', '" + rsDataPiece.getString("PR_SQMTR") + "', "
                                            + "'" + rsDataPiece.getString("PR_BILL_SQMTR") + "', '" + rsDataPiece.getString("PR_GSM") + "', "
                                            + "'" + rsDataPiece.getString("PR_BILL_GSM") + "', '" + rsDataPiece.getString("PR_TENDER_GSM") + "', "
                                            + "'" + rsDataPiece.getString("PR_THORITICAL_WEIGHT") + "', '" + rsDataPiece.getString("PR_TENDER_WEIGHT") + "'"
                                            + ", '" + rsDataPiece.getString("PR_ACTUAL_WEIGHT") + "', '" + rsDataPiece.getString("PR_BILL_WEIGHT") + "', "
                                            + "'" + rsDataPiece.getString("PR_WEAVING_WEIGHT") + "', '" + rsDataPiece.getString("PR_NEEDLING_WEIGHT") + "',"
                                            + " '" + rsDataPiece.getString("PR_SEAM_WEIGHT") + "', '" + rsDataPiece.getString("PR_SPLICE_WIEGHT") + "', "
                                            + "'" + rsDataPiece.getString("PR_WVG_DATE") + "', '" + rsDataPiece.getString("PR_MND_DATE") + "',"
                                            + " '" + rsDataPiece.getString("PR_NDL_DATE") + "', '" + rsDataPiece.getString("PR_INCHARGE") + "', "
                                            + "'" + rsDataPiece.getString("PR_PIECE_STAGE") + "', '" + rsDataPiece.getString("PR_DELINK") + "', "
                                            + "'" + rsDataPiece.getString("PR_OBSOLETE_DATE") + "', '" + rsDataPiece.getString("PR_REQ_MTH_LAST_DDMMYY") + "', '" + rsDataPiece.getString("PR_OC_LAST_DDMMYY") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_LAST_DDMMYY") + "', '0000-00-00', '" + rsDataPiece.getString("PR_PIECE_STAGE") + "', '', '', '" + rsDataPiece.getString("PR_INVOICE_NO") + "', '" + rsDataPiece.getString("PR_INVOICE_DATE") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_MONTH") + "', '', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i) + "', '" + DataModel.getValueByVariable("CONTACTED_PERSON", i) + "', '" + DataModel.getValueByVariable("PARTY_JUSTIFICATION", i) + "', '" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "', '" + rsDataPiece.getString("PR_FNSG_DATE") + "', '" + selected_monthyear + "') ");

                                    System.out.println("Change : UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET "
                                            + " EXPECTED_MONTH_OF_DISPATCH='" + selected_monthyear + "',"
                                            + "CONTACT_PERSON='',CONTACTED_NO='', "
                                            + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "MODE_OF_COMMUNICATION='',"
                                            + "PARTY_JUSTIFICATION='',"
                                            + "AREA_MANAGER_COMMENT='' "
                                            + " WHERE PIECE_NO='" + PIECE_NO + "' AND DOC_MONTH='" + Selected_Month + "' AND DOC_YEAR='" + Selected_Year + "'");
                                    data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET "
                                            + " EXPECTED_MONTH_OF_DISPATCH='" + selected_monthyear + "',"
                                            + "CONTACT_PERSON='',CONTACTED_NO='', "
                                            + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "MODE_OF_COMMUNICATION='',"
                                            + "PARTY_JUSTIFICATION='',"
                                            + "AREA_MANAGER_COMMENT='' "
                                            + " WHERE PIECE_NO='" + PIECE_NO + "' AND DOC_MONTH='" + Selected_Month + "' AND DOC_YEAR='" + Selected_Year + "'");

                                    MONTH_CHANGE_SAVED = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        if (!data.IsRecordExist("SELECT * FROM PRODUCTION.SPILLOVER_RESCHEDULING_NEW_DETAIL WHERE PIECE_NO='" + PIECE_NO + "' AND UPDATE_RE_SCH_MONTH='" + selected_monthyear + "'")) {
                            String Piece_OC_MONTH = data.getStringValueFromDB("SELECT PR_OC_MONTHYEAR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                            String Piece_Curr_sch_month = data.getStringValueFromDB("SELECT PR_CURRENT_SCH_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                            if (clsValidator.isCurrentSchMonthValid(selected_monthyear, Piece_OC_MONTH)) {

                                try {
                                    //data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_CURRENT_SCH_MONTH='" + selected_monthyear + "' WHERE PR_PIECE_NO='" + PIECE_NO + "' ");
                                    //data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_CURRENT_SCH_MONTH='" + selected_monthyear + "' WHERE WIP_PIECE_NO='" + PIECE_NO + "' ");

                                    data.Execute("INSERT INTO PRODUCTION.FELT_SALES_PIECE_MONTH_CHANGE_HISTORY\n"
                                            + "(PIECE_NO, OLD_OC_MONTH, OLD_CURR_SCH_MONTH, NEW_OC_MONTH, NEW_CURR_SCH_MONTH, CHANGE_REMARK, "
                                            + "USER_ID, FORM_NAME, ENTRY_DATE)\n"
                                            + "VALUES\n"
                                            + "('" + PIECE_NO + "', '" + Piece_OC_MONTH + "', '" + Piece_Curr_sch_month + "', '" + Piece_OC_MONTH + "', '" + selected_monthyear + "', 'REQUEST FOR CURR SCH MONTH ONLY', "
                                            + "'" + EITLERPGLOBAL.gNewUserID + "', 'FOLLOWUP_ENTRY', '" + EITLERPGLOBAL.getCurrentDateTimeDB() + "')");

                                    SelectFirstFree aList = new SelectFirstFree();
                                    aList.ModuleID = 867;
                                    aList.FirstFreeNo = 381;
                                    String Spilover_Doc_No = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 867, aList.FirstFreeNo, true);

                                    data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA"
                                            + " (MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) "
                                            + " VALUES "
                                            + " ('867', '" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '28', 'W', 'W', '', '1', '28', '', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00')");
                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_HEADER "
                                            + "(             DOC_NO, DOC_DATE, REMARK, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS) "
                                            + " VALUES "
                                            + "('" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 'Entry by followup nextmonth', '4789', '28', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00', 0, '0', '0000-00-00', 0, 0, '0000-00-00', 0, 0, '0000-00-00', '') ");

                                    ResultSet rsTmp = data.getResult("SELECT USER()");
                                    rsTmp.first();
                                    String str = rsTmp.getString(1);
                                    String str_split[] = str.split("@");
                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_HEADER_H "
                                            + "(REVISION_NO, DOC_NO, DOC_DATE, REMARK, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS, FROM_IP, APPROVAL_STATUS, APPROVER_REMARKS, ENTRY_DATE) "
                                            + " VALUES "
                                            + "(1,'" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 'Entry by followup nextmonth', '4789', '28', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00', 0, '0', '0000-00-00', 0, 0, '0000-00-00', 0, 0, '" + EITLERPGLOBAL.getCurrentDateDB() + "', '','" + str_split[1] + "','W','Postpond Schedule','" + EITLERPGLOBAL.getCurrentDateTimeDB() + "') ");
                                    ResultSet rsDataPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_DETAIL "
                                            + " (DOC_NO, SR_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, MACHINE_NO, POSITION, UPN, REQUESTED_MONTH, OC_MONTH, CURR_SCH_MONTH, RE_SCH_MONTH, PRODUCT_CODE, BILL_PRODUCT_CODE, PR_GROUP, STYLE, BILL_STYLE, LENGTH, ACTUAL_LENGTH, BILL_LENGTH, WIDTH, ACTUAL_WIDTH, SQMTR, BILL_SQMTR, GSM, BILL_GSM, TENDER_GSM, TH_WEIGHT, TENDER_WEIGHT, ACTUAL_WEIGHT, BILL_WEIGHT, WEAVING_WEIGHT, NEEDLING_WEIGHT, SEAM_WEIGHT, SPLICE_WEIGHT, WEAVING_DATE, MANDING_DATE, NEEDLING_DATE, INCHARGE, PIECE_STAGE, PIECE_OBSOLETE, OBSOLETE_DATE, REQUESTED_MONTH_DATE, OC_MONTH_DATE, CURR_SCH_MONTH_DATE, RE_SCH_MONTH_DATE, CUR_PIECE_STAGE, OC_FALL_MONTH, OC_FALL_MONTH_DATE, INVOICE_NO, INVOICE_DATE, DOC_MONTH, DOC_YEAR, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, FINISHING_DATE, UPDATE_RE_SCH_MONTH) "
                                            + " VALUES "
                                            // PR_PRODUCT_CODE, PR_BILL_PRODUCT_CODE, PR_GROUP, PR_SYN_PER, PR_STYLE, PR_BILL_STYLE, PR_LENGTH, PR_ACTUAL_LENGTH, PR_BILL_LENGTH, PR_WIDTH, PR_ACTUAL_WIDTH, PR_BILL_WIDTH, PR_SQMTR, PR_BILL_SQMTR, PR_GSM, PR_TENDER_GSM, PR_BILL_GSM, PR_THORITICAL_WEIGHT, PR_TENDER_WEIGHT, PR_ACTUAL_WEIGHT, PR_BILL_WEIGHT, PR_FELT_VALUE_WITH_GST, PR_FELT_VALUE_WITHOUT_GST, PR_FELT_BASE_VALUE, PR_INCHARGE, PR_PIECE_AB_FLAG, PR_REQUESTED_MONTH, PR_REQ_MTH_LAST_DDMMYY, PR_REQ_MONTH_ORIGINAL, PR_REQ_MONTH_REMARKS, PR_PRIORITY_HOLD_CAN_FLAG, PR_PIECE_STAGE, PR_WIP_STATUS, PR_GIDC_STATUS, PR_WARP_DATE, PR_WVG_DATE, PR_SPLICE_DATE, PR_MND_DATE, PR_NDL_DATE, PR_SEAM_DATE, PR_FNSG_DATE, PR_RCV_DATE, PR_WARP_A_DATE, PR_WARP_B_DATE, PR_WARP_LAYER_REMARK, PR_WARPING_WEIGHT, PR_WARPING_WEIGHT_A, PR_WARPING_WEIGHT_B, PR_WVG_A_DATE, PR_WVG_B_DATE, WVG_LAYER_REMARK, PR_WEAVING_WEIGHT, PR_WEAVING_WEIGHT_A, PR_WEAVING_WEIGHT_B, PR_SPLICE_WIEGHT, PR_MND_A_DATE, PR_MND_B_DATE, PR_MND_LAYER_REMARK, PR_MENDING_WEIGHT, PR_MENDING_WEIGHT_A, PR_MENDING_WEIGHT_B, PR_NEEDLING_WEIGHT, PR_SEAM_WEIGHT, PR_DIVERSION_FLAG, PR_DIVERSION_REASON, PR_DIVERTED_FLAG, PR_DIVERTED_REASON, PR_DIVERTED_DATE, PR_DIV_BEFORE_STAGE, PR_DIV_NO_CHANGE_DATE, PR_DIV_NO_CHANGE_FLAG, PR_EXP_DISPATCH_DATE, PR_REGION, PR_REFERENCE, PR_REFERENCE_DATE, PR_PO_NO, PR_PO_DATE, PR_ORDER_REMARK, PR_PIECE_REMARK, PR_PKG_DP_NO, PR_PKG_DP_DATE, PR_BALE_NO, PR_PACKED_DATE, PR_INVOICE_NO, PR_INVOICE_DATE, PR_INVOICE_AMOUNT, PR_EXPORT_BL_NO, PR_EXPORT_BL_DATE, PR_EXPORT_UNIT_OF_CURRENCY, PR_EXPORT_EXCHANGE_RATE, PR_EXPORT_INV_AMOUNT, PR_LR_NO, PR_LR_DATE, PR_INVOICE_PARTY, PR_PARTY_CODE_ORIGINAL, PR_PIECE_NO_ORIGINAL, PR_HOLD_DATE, PR_HOLD_REASON, PR_RELEASE_DATE, BALE_REOPEN_FLG, PR_SALES_RETURNS_NO, PR_SALES_RETURNS_DATE, PR_SALES_RETURNS_REMARKS, PR_SALES_RETURNS_FLG, PR_REJECTED_FLAG, PR_REJECTED_REMARK, PR_ADJUSTABLE_LENGTH, PR_ADJUSTABLE_WIDTH, PR_ADJUSTABLE_GSM, PR_ADJUSTABLE_WEIGHT, PR_DELINK, PR_OBSOLETE_DATE, PR_DELINK_REASON, PR_WH_LOCATION_ID, PR_DAYS_ORDER_WARPED, PR_DAYS_ORDER_WARPED_STATUS, PR_DAYS_ORDER_WVG, PR_DAYS_ORDER_WVG_STATUS, PR_DAYS_ORDER_MND, PR_DAYS_ORDER_MND_STATUS, PR_DAYS_ORDER_NDL, PR_DAYS_ORDER_NDL_STATUS, PR_DAYS_ORDER_FNG, PR_DAYS_ORDER_FNG_STATUS, PR_DAYS_WRP_WVG, PR_DAYS_WRP_WVG_STATUS, PR_DAYS_WVG_MND, PR_DAYS_WVG_MND_STATUS, PR_DAYS_MND_NDL, PR_DAYS_MND_NDL_STATUS, PR_DAYS_NDL_FNG, PR_DAYS_NDL_FNG_STATUS, PR_DAYS_MND_FNG, PR_DAYS_MND_FNG_STATUS, PR_DAYS_ORDER_INVOICE, PR_DAYS_ORDER_INVOICE_STATUS, PR_DAYS_STOCK_INVOICE, PR_DAYS_STOCK_INVOICE_STATUS, PR_DAYS_WH_STOCK, PR_DAYS_WH_PACKED, PR_DAYS_STATUS, PR_DAYS_PACK_INVOICE, PR_DAYS_PACK_NOT_INVOICE, PR_DAYS_PACK_NOT_INVOICE_STATUS, PR_DAYS_WH_STOCK_STATUS, PR_DAYS_CURRENT_STAGE, PR_WARP_EXECUTE_DATE, PR_CLOSURE_REOPEN_IND, PR_CLOSURE_DATE, PR_CLOSURE_REMARK, PR_REOPEN_DATE, PR_REOPEN_REMARK, PR_EXPECTED_DISPATCH, PR_EXP_DISPATCH_FROM, PR_EXP_DISPATCH_DOCNO, PE_PIECE_IT_DEPT_REMARK, PR_CONTACT_PERSON, PR_EMAIL_ID, PR_PHONE_NO, PR_OA_MONTHYEAR, PR_OA_NO, PR_OA_DATE, PR_OC_NO, PR_OC_DATE, PR_OC_MONTHYEAR, PR_OC_LAST_DDMMYY, PR_PIECE_OC_MONTH_IT_REMARK, PR_CURRENT_SCH_MONTH, PR_CURRENT_SCH_LAST_DDMMYY, PR_PIECE_CURRENT_MONTH_IT_REMARK, PR_SP_MONTHYEAR, PR_SP_LAST_DDMMYY, PR_SP_REMARKS, PR_EXP_WIP_DELIVERY_DATE, PR_EXP_PI_DATE, PR_ACT_PI_DATE, PR_EXP_DESPATCH_DATE, PR_EXP_PAY_CHQRC_DATE, PR_ACT_PAY_CHQRC_DATE, PR_EXP_PAY_CHQRC_DATE_ORIGINAL, PR_SPL_REQUEST_MONTHYEAR, PR_SPL_REQUEST_DATE, PR_SPL_REQUEST_REMARK, PR_PAYMENT_RCVD_VOUCHER, PR_PAYMENT_SHORT_AMOUNT, PR_PAYMENT_RCVD_AMOUNT, PR_PIECETRIAL_FLAG, PR_PIECETRIAL_CATEGORY, PR_WH_CODE, PR_INWARD_NO, PR_RACK_NO, PR_PIECE_ID, PR_LOCATION, PR_MFG_MONTH, PR_MFG_YEAR, PR_SCHEDULE_MONTH, PR_MFG_SPILL_OVEVER_REMARK, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, HIERARCHY_ID, APPROVER_BY, APPROVER_DATE, APPROVER_REMARK, PR_NEEDLING_WEIGHT_A, PR_NEEDLING_WEIGHT_B, PR_EXP_PAY_CHQRC_REMARKS, PR_EXP_PAY_CHQRC_REMARKS_DATE, PR_EXPORT_PARTY_REMARK, PR_EXPORT_PARTY_ORDERNO, PR_EXPORT_PARTY_PAYMENT_DATE, PR_RATE_INDICATOR, PR_SDF_INSTRUCT_DATE, PR_SDF_SPIRALED_DATE, PR_SDF_ASSEMBLED_DATE, PR_FELT_RATE, PR_FELT_VALUE_WITHOUT_DISCOUNT, PR_RATE_IND_DESPATCH_DATE, PR_SIZE_CRITERIA, PR_PRODUCT_CATG, PR_DFS_IN_DATE, PR_DFS_OUT_DATE, PR_MATERIAL_CODE, PR_OBSOLETE_SOURCE, PR_OBSOLETE_UPN_ASSIGN_STATUS, PR_SCRAP_REASON, PR_UNMAPPED_REASON, PR_WH_EXP_DISPATCH_DATE, PR_WH_EXP_DISPATCH_REMARK, PR_REJECTION_ORIGINATED_FROM, PR_UNABLE_TO_CONTACT, PR_CONTACTED_PERSON, PR_CONTACTED_NO, PR_FOLLOWUP_ADDITIONAL_REMARK
                                            + " ('" + Spilover_Doc_No + "', '" + (i + 1) + "', '" + PIECE_NO + "', '" + rsDataPiece.getString("PR_PARTY_CODE") + "', "
                                            + "'" + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsDataPiece.getString("PR_PARTY_CODE"))
                                            + "', '" + rsDataPiece.getString("PR_MACHINE_NO") + "', '" + rsDataPiece.getString("PR_POSITION_NO") + "', "
                                            + "'" + rsDataPiece.getString("PR_UPN") + "', '" + rsDataPiece.getString("PR_REQUESTED_MONTH") + "', "
                                            + "'" + rsDataPiece.getString("PR_OC_MONTHYEAR") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_MONTH") + "'"
                                            + ", '" + selected_monthyear + "', '" + rsDataPiece.getString("PR_PRODUCT_CODE") + "', "
                                            + "'" + rsDataPiece.getString("PR_BILL_PRODUCT_CODE") + "', '" + rsDataPiece.getString("PR_GROUP") + "', "
                                            + "'" + rsDataPiece.getString("PR_STYLE") + "', '" + rsDataPiece.getString("PR_BILL_STYLE") + "', "
                                            + "'" + rsDataPiece.getString("PR_LENGTH") + "', '" + rsDataPiece.getString("PR_ACTUAL_LENGTH") + "', "
                                            + "'" + rsDataPiece.getString("PR_BILL_LENGTH") + "', '" + rsDataPiece.getString("PR_WIDTH") + "', "
                                            + "'" + rsDataPiece.getString("PR_ACTUAL_WIDTH") + "', '" + rsDataPiece.getString("PR_SQMTR") + "', "
                                            + "'" + rsDataPiece.getString("PR_BILL_SQMTR") + "', '" + rsDataPiece.getString("PR_GSM") + "', "
                                            + "'" + rsDataPiece.getString("PR_BILL_GSM") + "', '" + rsDataPiece.getString("PR_TENDER_GSM") + "', "
                                            + "'" + rsDataPiece.getString("PR_THORITICAL_WEIGHT") + "', '" + rsDataPiece.getString("PR_TENDER_WEIGHT") + "'"
                                            + ", '" + rsDataPiece.getString("PR_ACTUAL_WEIGHT") + "', '" + rsDataPiece.getString("PR_BILL_WEIGHT") + "', "
                                            + "'" + rsDataPiece.getString("PR_WEAVING_WEIGHT") + "', '" + rsDataPiece.getString("PR_NEEDLING_WEIGHT") + "',"
                                            + " '" + rsDataPiece.getString("PR_SEAM_WEIGHT") + "', '" + rsDataPiece.getString("PR_SPLICE_WIEGHT") + "', "
                                            + "'" + rsDataPiece.getString("PR_WVG_DATE") + "', '" + rsDataPiece.getString("PR_MND_DATE") + "',"
                                            + " '" + rsDataPiece.getString("PR_NDL_DATE") + "', '" + rsDataPiece.getString("PR_INCHARGE") + "', "
                                            + "'" + rsDataPiece.getString("PR_PIECE_STAGE") + "', '" + rsDataPiece.getString("PR_DELINK") + "', "
                                            + "'" + rsDataPiece.getString("PR_OBSOLETE_DATE") + "', '" + rsDataPiece.getString("PR_REQ_MTH_LAST_DDMMYY") + "', '" + rsDataPiece.getString("PR_OC_LAST_DDMMYY") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_LAST_DDMMYY") + "', '0000-00-00', '" + rsDataPiece.getString("PR_PIECE_STAGE") + "', '', '', '" + rsDataPiece.getString("PR_INVOICE_NO") + "', '" + rsDataPiece.getString("PR_INVOICE_DATE") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_MONTH") + "', '', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i) + "', '" + DataModel.getValueByVariable("CONTACTED_PERSON", i) + "', '" + DataModel.getValueByVariable("PARTY_JUSTIFICATION", i) + "', '" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "', '" + rsDataPiece.getString("PR_FNSG_DATE") + "', '" + selected_monthyear + "')");

                                    System.out.println("Change : UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET "
                                            + " EXPECTED_MONTH_OF_DISPATCH='" + selected_monthyear + "',"
                                            + "CONTACT_PERSON='',CONTACTED_NO='', "
                                            + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "MODE_OF_COMMUNICATION='',"
                                            + "PARTY_JUSTIFICATION='',"
                                            + "AREA_MANAGER_COMMENT='' "
                                            + " WHERE PIECE_NO='" + PIECE_NO + "' AND DOC_MONTH='" + Selected_Month + "' AND DOC_YEAR='" + Selected_Year + "'");
                                    data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET "
                                            + " EXPECTED_MONTH_OF_DISPATCH='" + selected_monthyear + "',"
                                            + "CONTACT_PERSON='',CONTACTED_NO='', "
                                            + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                            + "MODE_OF_COMMUNICATION='',"
                                            + "PARTY_JUSTIFICATION='',"
                                            + "AREA_MANAGER_COMMENT='' "
                                            + " WHERE PIECE_NO='" + PIECE_NO + "' AND DOC_MONTH='" + Selected_Month + "' AND DOC_YEAR='" + Selected_Year + "'");

                                    MONTH_CHANGE_SAVED = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

//
//                    String date_selected_monthyear = EITLERPGLOBAL.formatDateDB(get1stDate(selected_monthyear));
//                    String date_Current_Month = EITLERPGLOBAL.formatDateDB(get1stDate(Current_Month));
//                    int date_diff = data.getIntValueFromDB("SELECT DATEDIFF('" + date_selected_monthyear + "','" + date_Current_Month + "')");
//                    if (date_diff < 0) {
//                      // data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL SET EXPECTED_MONTH_OF_DISPATCH='"+selected_monthyear+"' WHERE DOC_NO='" + CLUBBING_NO + "'");
//                                try{  
//                                  data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_CURRENT_SCH_MONTH='" + selected_monthyear + "' WHERE PR_PIECE_NO='" + PIECE_NO + "' ");
//                                  data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_CURRENT_SCH_MONTH='" + selected_monthyear + "' WHERE WIP_PIECE_NO='" + PIECE_NO + "' ");
//
//                                    SelectFirstFree aList = new SelectFirstFree();
//                                    aList.ModuleID = 867;
//                                    aList.FirstFreeNo = 381;
//                                    String Spilover_Doc_No = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 867, aList.FirstFreeNo, true);
//
//                                    data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA"
//                                            + " (MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) "
//                                            + " VALUES "
//                                            + " ('867', '" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '28', 'F', 'F', '', '1', '28', '', '"+EITLERPGLOBAL.getCurrentDateDB()+"', '"+EITLERPGLOBAL.getCurrentDateDB()+"', 0, '0000-00-00')");
//                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_HEADER "
//                                            + "(             DOC_NO, DOC_DATE, REMARK, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS) "
//                                            + " VALUES "
//                                            + "('" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 'Auto Approved', '4789', '28', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00', 1, '0', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, 0, '0000-00-00', 0, 0, '0000-00-00', '') ");
//
//                                    ResultSet rsTmp = data.getResult("SELECT USER()");
//                                    rsTmp.first();
//                                    String str = rsTmp.getString(1);
//                                    String str_split[] = str.split("@");
//                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_HEADER_H "
//                                            + "(REVISION_NO, DOC_NO, DOC_DATE, REMARK, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS, FROM_IP, APPROVAL_STATUS, APPROVER_REMARKS, ENTRY_DATE) "
//                                            + " VALUES "
//                                            + "(1,'" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 'Auto Approved', '4789', '28', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00', 1, '0', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, 0, '0000-00-00', 0, 0, '"+EITLERPGLOBAL.getCurrentDateDB()+"', '','"+str_split[1]+"','F','Prepond Schedule, Auto Approved','"+EITLERPGLOBAL.getCurrentDateTimeDB()+"') ");
//                                    ResultSet rsDataPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PIECE_NO + "'");
//                                    data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_DETAIL "
//                                            + " (DOC_NO, SR_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, MACHINE_NO, POSITION, UPN, REQUESTED_MONTH, OC_MONTH, CURR_SCH_MONTH, RE_SCH_MONTH, PRODUCT_CODE, BILL_PRODUCT_CODE, PR_GROUP, STYLE, BILL_STYLE, LENGTH, ACTUAL_LENGTH, BILL_LENGTH, WIDTH, ACTUAL_WIDTH, SQMTR, BILL_SQMTR, GSM, BILL_GSM, TENDER_GSM, TH_WEIGHT, TENDER_WEIGHT, ACTUAL_WEIGHT, BILL_WEIGHT, WEAVING_WEIGHT, NEEDLING_WEIGHT, SEAM_WEIGHT, SPLICE_WEIGHT, WEAVING_DATE, MANDING_DATE, NEEDLING_DATE, INCHARGE, PIECE_STAGE, PIECE_OBSOLETE, OBSOLETE_DATE, REQUESTED_MONTH_DATE, OC_MONTH_DATE, CURR_SCH_MONTH_DATE, RE_SCH_MONTH_DATE, CUR_PIECE_STAGE, OC_FALL_MONTH, OC_FALL_MONTH_DATE, INVOICE_NO, INVOICE_DATE, DOC_MONTH, DOC_YEAR, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, FINISHING_DATE) "
//                                            + " VALUES "
//                                            // PR_PRODUCT_CODE, PR_BILL_PRODUCT_CODE, PR_GROUP, PR_SYN_PER, PR_STYLE, PR_BILL_STYLE, PR_LENGTH, PR_ACTUAL_LENGTH, PR_BILL_LENGTH, PR_WIDTH, PR_ACTUAL_WIDTH, PR_BILL_WIDTH, PR_SQMTR, PR_BILL_SQMTR, PR_GSM, PR_TENDER_GSM, PR_BILL_GSM, PR_THORITICAL_WEIGHT, PR_TENDER_WEIGHT, PR_ACTUAL_WEIGHT, PR_BILL_WEIGHT, PR_FELT_VALUE_WITH_GST, PR_FELT_VALUE_WITHOUT_GST, PR_FELT_BASE_VALUE, PR_INCHARGE, PR_PIECE_AB_FLAG, PR_REQUESTED_MONTH, PR_REQ_MTH_LAST_DDMMYY, PR_REQ_MONTH_ORIGINAL, PR_REQ_MONTH_REMARKS, PR_PRIORITY_HOLD_CAN_FLAG, PR_PIECE_STAGE, PR_WIP_STATUS, PR_GIDC_STATUS, PR_WARP_DATE, PR_WVG_DATE, PR_SPLICE_DATE, PR_MND_DATE, PR_NDL_DATE, PR_SEAM_DATE, PR_FNSG_DATE, PR_RCV_DATE, PR_WARP_A_DATE, PR_WARP_B_DATE, PR_WARP_LAYER_REMARK, PR_WARPING_WEIGHT, PR_WARPING_WEIGHT_A, PR_WARPING_WEIGHT_B, PR_WVG_A_DATE, PR_WVG_B_DATE, WVG_LAYER_REMARK, PR_WEAVING_WEIGHT, PR_WEAVING_WEIGHT_A, PR_WEAVING_WEIGHT_B, PR_SPLICE_WIEGHT, PR_MND_A_DATE, PR_MND_B_DATE, PR_MND_LAYER_REMARK, PR_MENDING_WEIGHT, PR_MENDING_WEIGHT_A, PR_MENDING_WEIGHT_B, PR_NEEDLING_WEIGHT, PR_SEAM_WEIGHT, PR_DIVERSION_FLAG, PR_DIVERSION_REASON, PR_DIVERTED_FLAG, PR_DIVERTED_REASON, PR_DIVERTED_DATE, PR_DIV_BEFORE_STAGE, PR_DIV_NO_CHANGE_DATE, PR_DIV_NO_CHANGE_FLAG, PR_EXP_DISPATCH_DATE, PR_REGION, PR_REFERENCE, PR_REFERENCE_DATE, PR_PO_NO, PR_PO_DATE, PR_ORDER_REMARK, PR_PIECE_REMARK, PR_PKG_DP_NO, PR_PKG_DP_DATE, PR_BALE_NO, PR_PACKED_DATE, PR_INVOICE_NO, PR_INVOICE_DATE, PR_INVOICE_AMOUNT, PR_EXPORT_BL_NO, PR_EXPORT_BL_DATE, PR_EXPORT_UNIT_OF_CURRENCY, PR_EXPORT_EXCHANGE_RATE, PR_EXPORT_INV_AMOUNT, PR_LR_NO, PR_LR_DATE, PR_INVOICE_PARTY, PR_PARTY_CODE_ORIGINAL, PR_PIECE_NO_ORIGINAL, PR_HOLD_DATE, PR_HOLD_REASON, PR_RELEASE_DATE, BALE_REOPEN_FLG, PR_SALES_RETURNS_NO, PR_SALES_RETURNS_DATE, PR_SALES_RETURNS_REMARKS, PR_SALES_RETURNS_FLG, PR_REJECTED_FLAG, PR_REJECTED_REMARK, PR_ADJUSTABLE_LENGTH, PR_ADJUSTABLE_WIDTH, PR_ADJUSTABLE_GSM, PR_ADJUSTABLE_WEIGHT, PR_DELINK, PR_OBSOLETE_DATE, PR_DELINK_REASON, PR_WH_LOCATION_ID, PR_DAYS_ORDER_WARPED, PR_DAYS_ORDER_WARPED_STATUS, PR_DAYS_ORDER_WVG, PR_DAYS_ORDER_WVG_STATUS, PR_DAYS_ORDER_MND, PR_DAYS_ORDER_MND_STATUS, PR_DAYS_ORDER_NDL, PR_DAYS_ORDER_NDL_STATUS, PR_DAYS_ORDER_FNG, PR_DAYS_ORDER_FNG_STATUS, PR_DAYS_WRP_WVG, PR_DAYS_WRP_WVG_STATUS, PR_DAYS_WVG_MND, PR_DAYS_WVG_MND_STATUS, PR_DAYS_MND_NDL, PR_DAYS_MND_NDL_STATUS, PR_DAYS_NDL_FNG, PR_DAYS_NDL_FNG_STATUS, PR_DAYS_MND_FNG, PR_DAYS_MND_FNG_STATUS, PR_DAYS_ORDER_INVOICE, PR_DAYS_ORDER_INVOICE_STATUS, PR_DAYS_STOCK_INVOICE, PR_DAYS_STOCK_INVOICE_STATUS, PR_DAYS_WH_STOCK, PR_DAYS_WH_PACKED, PR_DAYS_STATUS, PR_DAYS_PACK_INVOICE, PR_DAYS_PACK_NOT_INVOICE, PR_DAYS_PACK_NOT_INVOICE_STATUS, PR_DAYS_WH_STOCK_STATUS, PR_DAYS_CURRENT_STAGE, PR_WARP_EXECUTE_DATE, PR_CLOSURE_REOPEN_IND, PR_CLOSURE_DATE, PR_CLOSURE_REMARK, PR_REOPEN_DATE, PR_REOPEN_REMARK, PR_EXPECTED_DISPATCH, PR_EXP_DISPATCH_FROM, PR_EXP_DISPATCH_DOCNO, PE_PIECE_IT_DEPT_REMARK, PR_CONTACT_PERSON, PR_EMAIL_ID, PR_PHONE_NO, PR_OA_MONTHYEAR, PR_OA_NO, PR_OA_DATE, PR_OC_NO, PR_OC_DATE, PR_OC_MONTHYEAR, PR_OC_LAST_DDMMYY, PR_PIECE_OC_MONTH_IT_REMARK, PR_CURRENT_SCH_MONTH, PR_CURRENT_SCH_LAST_DDMMYY, PR_PIECE_CURRENT_MONTH_IT_REMARK, PR_SP_MONTHYEAR, PR_SP_LAST_DDMMYY, PR_SP_REMARKS, PR_EXP_WIP_DELIVERY_DATE, PR_EXP_PI_DATE, PR_ACT_PI_DATE, PR_EXP_DESPATCH_DATE, PR_EXP_PAY_CHQRC_DATE, PR_ACT_PAY_CHQRC_DATE, PR_EXP_PAY_CHQRC_DATE_ORIGINAL, PR_SPL_REQUEST_MONTHYEAR, PR_SPL_REQUEST_DATE, PR_SPL_REQUEST_REMARK, PR_PAYMENT_RCVD_VOUCHER, PR_PAYMENT_SHORT_AMOUNT, PR_PAYMENT_RCVD_AMOUNT, PR_PIECETRIAL_FLAG, PR_PIECETRIAL_CATEGORY, PR_WH_CODE, PR_INWARD_NO, PR_RACK_NO, PR_PIECE_ID, PR_LOCATION, PR_MFG_MONTH, PR_MFG_YEAR, PR_SCHEDULE_MONTH, PR_MFG_SPILL_OVEVER_REMARK, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, HIERARCHY_ID, APPROVER_BY, APPROVER_DATE, APPROVER_REMARK, PR_NEEDLING_WEIGHT_A, PR_NEEDLING_WEIGHT_B, PR_EXP_PAY_CHQRC_REMARKS, PR_EXP_PAY_CHQRC_REMARKS_DATE, PR_EXPORT_PARTY_REMARK, PR_EXPORT_PARTY_ORDERNO, PR_EXPORT_PARTY_PAYMENT_DATE, PR_RATE_INDICATOR, PR_SDF_INSTRUCT_DATE, PR_SDF_SPIRALED_DATE, PR_SDF_ASSEMBLED_DATE, PR_FELT_RATE, PR_FELT_VALUE_WITHOUT_DISCOUNT, PR_RATE_IND_DESPATCH_DATE, PR_SIZE_CRITERIA, PR_PRODUCT_CATG, PR_DFS_IN_DATE, PR_DFS_OUT_DATE, PR_MATERIAL_CODE, PR_OBSOLETE_SOURCE, PR_OBSOLETE_UPN_ASSIGN_STATUS, PR_SCRAP_REASON, PR_UNMAPPED_REASON, PR_WH_EXP_DISPATCH_DATE, PR_WH_EXP_DISPATCH_REMARK, PR_REJECTION_ORIGINATED_FROM, PR_UNABLE_TO_CONTACT, PR_CONTACTED_PERSON, PR_CONTACTED_NO, PR_FOLLOWUP_ADDITIONAL_REMARK
//                                            + " ('" + Spilover_Doc_No + "', '" + (i + 1) + "', '" + PIECE_NO + "', '" + rsDataPiece.getString("PR_PARTY_CODE") + "', "
//                                            + "'" + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsDataPiece.getString("PR_PARTY_CODE"))
//                                            + "', '" + rsDataPiece.getString("PR_MACHINE_NO") + "', '" + rsDataPiece.getString("PR_POSITION_NO") + "', "
//                                            + "'" + rsDataPiece.getString("PR_UPN") + "', '" + rsDataPiece.getString("PR_REQUESTED_MONTH") + "', "
//                                            + "'" + rsDataPiece.getString("PR_OC_MONTHYEAR") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_MONTH") + "'"
//                                            + ", '" + selected_monthyear + "', '" + rsDataPiece.getString("PR_PRODUCT_CODE") + "', "
//                                            + "'" + rsDataPiece.getString("PR_BILL_PRODUCT_CODE") + "', '" + rsDataPiece.getString("PR_GROUP") + "', "
//                                            + "'" + rsDataPiece.getString("PR_STYLE") + "', '" + rsDataPiece.getString("PR_BILL_STYLE") + "', "
//                                            + "'" + rsDataPiece.getString("PR_LENGTH") + "', '" + rsDataPiece.getString("PR_ACTUAL_LENGTH") + "', "
//                                            + "'" + rsDataPiece.getString("PR_BILL_LENGTH") + "', '" + rsDataPiece.getString("PR_WIDTH") + "', "
//                                            + "'" + rsDataPiece.getString("PR_ACTUAL_WIDTH") + "', '" + rsDataPiece.getString("PR_SQMTR") + "', "
//                                            + "'" + rsDataPiece.getString("PR_BILL_SQMTR") + "', '" + rsDataPiece.getString("PR_GSM") + "', "
//                                            + "'" + rsDataPiece.getString("PR_BILL_GSM") + "', '" + rsDataPiece.getString("PR_TENDER_GSM") + "', "
//                                            + "'" + rsDataPiece.getString("PR_THORITICAL_WEIGHT") + "', '" + rsDataPiece.getString("PR_TENDER_WEIGHT") + "'"
//                                            + ", '" + rsDataPiece.getString("PR_ACTUAL_WEIGHT") + "', '" + rsDataPiece.getString("PR_BILL_WEIGHT") + "', "
//                                            + "'" + rsDataPiece.getString("PR_WEAVING_WEIGHT") + "', '" + rsDataPiece.getString("PR_NEEDLING_WEIGHT") + "',"
//                                            + " '" + rsDataPiece.getString("PR_SEAM_WEIGHT") + "', '" + rsDataPiece.getString("PR_SPLICE_WIEGHT") + "', "
//                                            + "'" + rsDataPiece.getString("PR_WVG_DATE") + "', '" + rsDataPiece.getString("PR_MND_DATE") + "',"
//                                            + " '" + rsDataPiece.getString("PR_NDL_DATE") + "', '" + rsDataPiece.getString("PR_INCHARGE") + "', "
//                                            + "'" + rsDataPiece.getString("PR_PIECE_STAGE") + "', '" + rsDataPiece.getString("PR_DELINK") + "', "
//                                            + "'" + rsDataPiece.getString("PR_OBSOLETE_DATE") + "', '" + rsDataPiece.getString("PR_REQ_MTH_LAST_DDMMYY") + "', '" + rsDataPiece.getString("PR_OC_LAST_DDMMYY") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_LAST_DDMMYY") + "', '0000-00-00', '" + rsDataPiece.getString("PR_PIECE_STAGE") + "', '', '', '" + rsDataPiece.getString("PR_INVOICE_NO") + "', '" + rsDataPiece.getString("PR_INVOICE_DATE") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_MONTH") + "', '', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i) + "', '" + DataModel.getValueByVariable("CONTACTED_PERSON", i) + "', '" + DataModel.getValueByVariable("PARTY_JUSTIFICATION", i) + "', '" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "', '" + rsDataPiece.getString("PR_FNSG_DATE") + "') ");
//
//                                    MONTH_CHANGE_SAVED = true;  
//                                  }catch(Exception e)
//                                  {
//                                      e.printStackTrace();
//                                  }
//                    }
                }
            }
        }

        if (Selected_Month != EITLERPGLOBAL.getCurrentMonth() || Selected_Year != EITLERPGLOBAL.getCurrentYear()) {

            if (MONTH_CHANGE_SAVED) {
                JOptionPane.showMessageDialog(this, "Data Saved succesfully.");
            }
            return;
        }

//        if (Selected_Month != EITLERPGLOBAL.getCurrentMonth() || Selected_Year != EITLERPGLOBAL.getCurrentYear()) {
//            
//            JOptionPane.showMessageDialog(this  , "Currently, You can not do any action for above selected date.");
//            return;
//        }
//        
//        if (Selected_Month != EITLERPGLOBAL.getCurrentMonth() || Selected_Year != EITLERPGLOBAL.getCurrentYear()) {
//            
//            for (int i = 0; i <= Table.getRowCount() - 1; i++) {
//                
//                String selected_monthyear =  DataModel.getValueByVariable("EXPECTED_MONTH_OF_DISPATCH", i);
//                String Current_Month = txtCurrentSchMonth.getText();
//                
//                if(!selected_monthyear.equals(Current_Month))
//                {
//                    String date_selected_monthyear = EITLERPGLOBAL.formatDateDB(get1stDate(selected_monthyear));
//                    String date_Current_Month = EITLERPGLOBAL.formatDateDB(get1stDate(Current_Month));
//                    int date_diff = data.getIntValueFromDB("SELECT DATEDIFF('" + date_selected_monthyear + "','" + date_Current_Month + "')");
//                    if (date_diff < 0) {
//                      // data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL SET EXPECTED_MONTH_OF_DISPATCH='"+selected_monthyear+"' WHERE DOC_NO='" + CLUBBING_NO + "'");
//                    }
//                }
//                
//            }
//            
//        }
        String Spilover_Doc_No = "";
        boolean DATA_SAVED = false;
        boolean MONTH_CHANGE_REQUEST = false;
        String Month_Change_Doc_No = "";
        boolean spilover_doc_no_generated = false;
        for (int i = 0; i <= Table.getRowCount() - 1; i++) {

            int col_select = DataModel.getColFromVariable("SELECT");
            int col_unable_to_contact = DataModel.getColFromVariable("UNABLE_TO_CONTACT");
            String NewExpectedDate = DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i);
            try {
                if (!"".equals(NewExpectedDate)) {
                    int Month = EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(NewExpectedDate));

                    int Year = EITLERPGLOBAL.getYear(EITLERPGLOBAL.formatDateDB(NewExpectedDate));

                    if (Month != EITLERPGLOBAL.getCurrentMonth() || Year != EITLERPGLOBAL.getCurrentYear()) {
                        JOptionPane.showMessageDialog(this, "Follow Up Date should be current Month for Piece No " + DataModel.getValueByVariable("PIECE_NO", i));
                        return;
                    }
                }
            } catch (Exception e) {

            }
            if (!DataModel.getValueByVariable("PIECE_NO", i).equals("") && Table.getValueAt(i, col_select).equals(true)) {
                if (Table.getValueAt(i, col_unable_to_contact).equals(false) && DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please do any action for Piece No " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
            }
            if (Table.getValueAt(i, col_unable_to_contact).equals(true)) {

                if (!DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "UNABLE TO CONTACT and FOLLOW UP DATE, Both are not allowed at same time, For PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }

                if (DataModel.getValueByVariable("CONTACTED_PERSON", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted person for PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
                if (DataModel.getValueByVariable("CONTACTED_NO", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted number for PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
                if (!DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Mode Of Comminication should be blank for Unable to Contact for Piece No " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
                if (!DataModel.getValueByVariable("PARTY_JUSTIFICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Party Justification should be blank for Unable to Contact for Piece No " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
//                if (!DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i).equals("")) {
//                    JOptionPane.showMessageDialog(this, "Area Manager Comment should be blank for Unable to Contact for Piece No " + DataModel.getValueByVariable("PIECE_NO", i));
//                    return;
//                }
            }
            if (!DataModel.getValueByVariable("PIECE_NO", i).equals("") && !DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {
                System.out.println("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i)) + "','" + EITLERPGLOBAL.getCurrentDateDB() + "')");
                int date_diff = data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i)) + "','" + EITLERPGLOBAL.getCurrentDateDB() + "')");
                if (date_diff <= 0) {
                    JOptionPane.showMessageDialog(this, "Expected Date " + DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i) + " is not valid for PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
            }
            if (!DataModel.getValueByVariable("PIECE_NO", i).equals("") && !DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {
                if (DataModel.getValueByVariable("CONTACTED_PERSON", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted person for PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
                if (DataModel.getValueByVariable("CONTACTED_NO", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted number for PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
//                if (DataModel.getValueByVariable("DATE_OF_COMMUNICATION", i).equals("")) {
//                    JOptionPane.showMessageDialog(this, "Please enter DATE OF COMMUNICATION for PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
//                    return;
//                }
                if (DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter MODE OF COMMUNICATION for PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
                if (DataModel.getValueByVariable("PARTY_JUSTIFICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter PARTY JUSTIFICATION for PIECE NO " + DataModel.getValueByVariable("PIECE_NO", i));
                    return;
                }
                /*
                 DataModel.SetVariable(21, "DATE_OF_COMMUNICATION");
                 DataModel.SetVariable(22, "MODE_OF_COMMUNICATION");
                 */
            }
            String contacted_person = DataModel.getValueByVariable("CONTACTED_PERSON", i);
            String contacted_no = DataModel.getValueByVariable("CONTACTED_NO", i);
            int col_no = DataModel.getColFromVariable("UNABLE_TO_CONTACT");
            if (Table.getValueAt(i, col_no).equals(true)) {
                if (contacted_person.equals("") || contacted_no.equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contected person and number for unable to contact (PIECE NO : " + DataModel.getValueByVariable("PIECE_NO", i) + ")");
                    return;
                }

            }
            try{
                if(!DataModel.getValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", i).equals(""))
                {
                    if (EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("PARTY_LIFTING_COMMITMENT_DATE", i)).equals("")) {
                        JOptionPane.showMessageDialog(this, "Please enter Party Lifting Commitment Date (PIECE NO : " + DataModel.getValueByVariable("PIECE_NO", i) + ")");
                        return;
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            try {
                int reschedule_count = Integer.parseInt(DataModel.getValueByVariable("RESCHEDULE_COUNT", i));
                String Remark = DataModel.getValueByVariable("REMARK", i);
                if (reschedule_count >= 1 && Remark.equals("") && !DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {

                    String New_date = EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i));
                    String old_date = EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("EXPECTED_DISPATCH_DATE", i));
                    if (EITLERPGLOBAL.getMonth(New_date) > EITLERPGLOBAL.getMonth(old_date)) {
                        JOptionPane.showMessageDialog(this, "Please enter Delayed Reason for Piece No : " + DataModel.getValueByVariable("PIECE_NO", i) + "");
                        return;
                    }

//                    
//                    int diff = data.getIntValueFromDB("SELECT DATEDIFF('" + New_date + "','" + old_date + "')");
//                    if(diff>0)
//                    {
//                        JOptionPane.showMessageDialog(this, "Please enter Delayed Reason for Piece No : " + DataModel.getValueByVariable("PIECE_NO", i) + "");
//                        return;
//                    }
                }
            } catch (Exception e) {
                //  e.printStackTrace();
            }
        }

        for (int i = 0; i <= Table.getRowCount() - 1; i++) {

            String PieceNo = DataModel.getValueByVariable("PIECE_NO", i);
            String contacted_person = DataModel.getValueByVariable("CONTACTED_PERSON", i);
            String contacted_no = DataModel.getValueByVariable("CONTACTED_NO", i);
            int col_no = DataModel.getColFromVariable("UNABLE_TO_CONTACT");
            String Remark = DataModel.getValueByVariable("REMARK", i);
            String NewExpectedDate = DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i);
            String Additional_Remark = DataModel.getValueByVariable("ADDITIONAL_REMARK", i);
            String PreviousExpectedDate = DataModel.getValueByVariable("EXPECTED_DISPATCH_DATE", i);

            //Unable to Contact
            if (!DataModel.getValueByVariable("PIECE_NO", i).equals("") && Table.getValueAt(i, col_no).equals(true)) {
                int unable_to_contact = 0;
                data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET UNABLE_TO_CONTACT='1' WHERE PIECE_NO='" + PieceNo + "'  AND DOC_MONTH='" + txtMonth.getText() + "' AND DOC_YEAR='" + txtYear.getText() + "'");
                unable_to_contact = 1;
                data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET "
                        + " ADDITIONAL_REMARK='" + Additional_Remark + "',"
                        + "CONTACT_PERSON='" + contacted_person + "',CONTACTED_NO='" + contacted_no + "', "
                        + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                        + "MODE_OF_COMMUNICATION='',"
                        + "PARTY_JUSTIFICATION='',"
                        + "AREA_MANAGER_COMMENT='" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "' "
                        + "WHERE PIECE_NO='" + PieceNo + "'  AND DOC_MONTH='" + txtMonth.getText() + "' AND DOC_YEAR='" + txtYear.getText() + "'");
                data.Execute("INSERT INTO PRODUCTION.FELT_SALES_EXPECTED_DISPATCH_DATE_HISTORY "
                        + "(PIECE_NO,PR_WH_EXP_DISPATCH_DATE,ENTRY_DATE,USER_ID,REMARK,UNABLE_TO_CONTACT,"
                        + "CONTACTED_PERSON,CONTACTED_NO,ADDITIONAL_REMARK,DATE_OF_COMMUNICATION,MODE_OF_COMMUNICATION,PARTY_JUSTIFICATION,AREA_MANAGER_COMMENT) "
                        + "VALUES "
                        + "('" + PieceNo + "','" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "','" + EITLERPGLOBAL.getCurrentDateTimeDB() + "','" + EITLERPGLOBAL.gUserID + "','" + Remark + "','" + unable_to_contact + "','" + contacted_person + "','" + contacted_no + "','" + Additional_Remark + "','" + EITLERPGLOBAL.getCurrentDateDB() + "','','','" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "')");
                DATA_SAVED = true;
            }

            //Next Follow Up Date
            if (!DataModel.getValueByVariable("PIECE_NO", i).equals("") && !DataModel.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {
                if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL where PIECE_NO='" + PieceNo + "' AND DOC_MONTH='" + txtMonth.getText() + "' AND DOC_YEAR='" + txtYear.getText() + "' AND EXPECTED_DISPATCH_DATE='" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "'")) {
                    //System.out.println("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET EXPECTED_DISPATCH_DATE='"+EITLERPGLOBAL.formatDateDB(NewExpectedDate)+"',PREVIOUS_DISPATCH_DATE='"+EITLERPGLOBAL.formatDateDB(PreviousExpectedDate)+"',EXP_DISPATCH_HISTORY=CONCAT(coalesce(EXP_DISPATCH_HISTORY,''),',"+EITLERPGLOBAL.formatDateDB(NewExpectedDate)+"'),DELAY_REASON='"+Remark+"',ADDITIONAL_REMARK='"+Additional_Remark+"',CONTACT_PERSON='"+contacted_person+"',CONTACTED_NO='"+contacted_no+"' WHERE PIECE_NO='"+PieceNo+"'");
                    data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET "
                            + "FOLLOWUP_DATE='" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "',UNABLE_TO_CONTACT='',"
                            + "PREVIOUS_DISPATCH_DATE='" + EITLERPGLOBAL.formatDateDB(PreviousExpectedDate) + "',"
                            + "EXP_DISPATCH_HISTORY=CONCAT(coalesce(EXP_DISPATCH_HISTORY,''),'"
                            + "," + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "'),"
                            + "DELAY_REASON='" + Remark + "',ADDITIONAL_REMARK='" + Additional_Remark + "',"
                            + "CONTACT_PERSON='" + contacted_person + "',CONTACTED_NO='" + contacted_no + "', "
                            + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                            + "MODE_OF_COMMUNICATION='" + DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i) + "',"
                            + "PARTY_JUSTIFICATION='" + DataModel.getValueByVariable("PARTY_JUSTIFICATION", i) + "',"
                            + "AREA_MANAGER_COMMENT='" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "' "
                            + "WHERE PIECE_NO='" + PieceNo + "'  AND DOC_MONTH='" + txtMonth.getText() + "' AND DOC_YEAR='" + txtYear.getText() + "'");
                    data.Execute("INSERT INTO PRODUCTION.FELT_SALES_EXPECTED_DISPATCH_DATE_HISTORY "
                            + "(PIECE_NO,PR_WH_EXP_DISPATCH_DATE,ENTRY_DATE,USER_ID,REMARK,UNABLE_TO_CONTACT,"
                            + "CONTACTED_PERSON,CONTACTED_NO,ADDITIONAL_REMARK,DATE_OF_COMMUNICATION,MODE_OF_COMMUNICATION,PARTY_JUSTIFICATION,AREA_MANAGER_COMMENT) "
                            + "VALUES "
                            + "('" + PieceNo + "','" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "','" + EITLERPGLOBAL.getCurrentDateTimeDB() + "','" + EITLERPGLOBAL.gUserID + "','" + Remark + "','','" + contacted_person + "','" + contacted_no + "','" + Additional_Remark + "','" + EITLERPGLOBAL.getCurrentDateDB() + "','" + DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i) + "','" + DataModel.getValueByVariable("PARTY_JUSTIFICATION", i) + "','" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "')");
                    //PIECE_NO,      PR_WH_EXP_DISPATCH_DATE,                       ENTRY_DATE,                                USER_ID,                   REMARK,       UNABLE_TO_CONTACT,      CONTACTED_PERSON,      CONTACTED_NO,      ADDITIONAL_REMARK
                    DATA_SAVED = true;
                }
            }

            
            try{
                String PARTY_LIFTING_COMMITMENT_STATUS = DataModel.getValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", i);
                String PARTY_LIFTING_COMMITMENT_DATE = DataModel.getValueByVariable("PARTY_LIFTING_COMMITMENT_DATE" , i);
                //if (Table.getValueAt(i, col_no_PARTY_LIFTING_COMMITMENT_STATUS).equals(true)) {
                    if (!PARTY_LIFTING_COMMITMENT_STATUS.equals("")) {
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET "
                                + "PARTY_LIFTING_COMMITMENT_STATUS='"+PARTY_LIFTING_COMMITMENT_STATUS+"',"
                                + "PARTY_LIFTING_COMMITMENT_DATE='" + EITLERPGLOBAL.formatDateDB(PARTY_LIFTING_COMMITMENT_DATE) + "' "
                                + "WHERE PIECE_NO='" + PieceNo + "'  AND DOC_MONTH='" + txtMonth.getText() + "' AND DOC_YEAR='" + txtYear.getText() + "'");
                    }
                //}
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
            String selected_monthyear = DataModel.getValueByVariable("EXPECTED_MONTH_OF_DISPATCH", i);
            String Piece_Curr_Sch_Month = data.getStringValueFromDB("SELECT PR_CURRENT_SCH_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");

            if (!txtCurrentSchMonth.getText().equals(selected_monthyear)) {
                if (!Piece_Curr_Sch_Month.equals(selected_monthyear)) {

                    if (!data.IsRecordExist("SELECT H.* FROM PRODUCTION.SPILLOVER_RESCHEDULING_NEW_HEADER H,\n"
                            + " PRODUCTION.SPILLOVER_RESCHEDULING_NEW_DETAIL D \n"
                            + " WHERE H.APPROVED=0 AND H.CANCELED=0 AND H.DOC_NO=D.DOC_NO AND D.PIECE_NO='" + PieceNo + "'")) {
                        if (!spilover_doc_no_generated) {
                            SelectFirstFree aList = new SelectFirstFree();
                            aList.ModuleID = 867;
                            aList.FirstFreeNo = 381;
                            Spilover_Doc_No = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 867, aList.FirstFreeNo, true);
                            spilover_doc_no_generated = true;
                            data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA"
                                    + " (MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) "
                                    + " VALUES "
                                    + " ('867', '" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '28', 'W', 'F', '', '1', '28', '', '0000-00-00', '0000-00-00', 0, '0000-00-00')");
                            data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_HEADER "
                                    + "(DOC_NO, DOC_DATE, REMARK, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS) "
                                    + " VALUES "
                                    + "('" + Spilover_Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + Remark + "', '4789', '28', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00', 0, 0, '0000-00-00', 0, 0, '0000-00-00', 0, 0, '0000-00-00', '') ");
                            MONTH_CHANGE_REQUEST = true;
                            Month_Change_Doc_No = Spilover_Doc_No;
                        }
                        try {

                            ResultSet rsDataPiece = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");
                            data.Execute("INSERT INTO PRODUCTION.SPILLOVER_RESCHEDULING_NEW_DETAIL "
                                    + " (DOC_NO, SR_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, MACHINE_NO, POSITION, UPN, REQUESTED_MONTH, OC_MONTH, CURR_SCH_MONTH, RE_SCH_MONTH, PRODUCT_CODE, BILL_PRODUCT_CODE, PR_GROUP, STYLE, BILL_STYLE, LENGTH, ACTUAL_LENGTH, BILL_LENGTH, WIDTH, ACTUAL_WIDTH, SQMTR, BILL_SQMTR, GSM, BILL_GSM, TENDER_GSM, TH_WEIGHT, TENDER_WEIGHT, ACTUAL_WEIGHT, BILL_WEIGHT, WEAVING_WEIGHT, NEEDLING_WEIGHT, SEAM_WEIGHT, SPLICE_WEIGHT, WEAVING_DATE, MANDING_DATE, NEEDLING_DATE, INCHARGE, PIECE_STAGE, PIECE_OBSOLETE, OBSOLETE_DATE, REQUESTED_MONTH_DATE, OC_MONTH_DATE, CURR_SCH_MONTH_DATE, RE_SCH_MONTH_DATE, CUR_PIECE_STAGE, OC_FALL_MONTH, OC_FALL_MONTH_DATE, INVOICE_NO, INVOICE_DATE, DOC_MONTH, DOC_YEAR, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, FINISHING_DATE,UPDATE_RE_SCH_MONTH) "
                                    + " VALUES "
                                    // PR_PRODUCT_CODE, PR_BILL_PRODUCT_CODE, PR_GROUP, PR_SYN_PER, PR_STYLE, PR_BILL_STYLE, PR_LENGTH, PR_ACTUAL_LENGTH, PR_BILL_LENGTH, PR_WIDTH, PR_ACTUAL_WIDTH, PR_BILL_WIDTH, PR_SQMTR, PR_BILL_SQMTR, PR_GSM, PR_TENDER_GSM, PR_BILL_GSM, PR_THORITICAL_WEIGHT, PR_TENDER_WEIGHT, PR_ACTUAL_WEIGHT, PR_BILL_WEIGHT, PR_FELT_VALUE_WITH_GST, PR_FELT_VALUE_WITHOUT_GST, PR_FELT_BASE_VALUE, PR_INCHARGE, PR_PIECE_AB_FLAG, PR_REQUESTED_MONTH, PR_REQ_MTH_LAST_DDMMYY, PR_REQ_MONTH_ORIGINAL, PR_REQ_MONTH_REMARKS, PR_PRIORITY_HOLD_CAN_FLAG, PR_PIECE_STAGE, PR_WIP_STATUS, PR_GIDC_STATUS, PR_WARP_DATE, PR_WVG_DATE, PR_SPLICE_DATE, PR_MND_DATE, PR_NDL_DATE, PR_SEAM_DATE, PR_FNSG_DATE, PR_RCV_DATE, PR_WARP_A_DATE, PR_WARP_B_DATE, PR_WARP_LAYER_REMARK, PR_WARPING_WEIGHT, PR_WARPING_WEIGHT_A, PR_WARPING_WEIGHT_B, PR_WVG_A_DATE, PR_WVG_B_DATE, WVG_LAYER_REMARK, PR_WEAVING_WEIGHT, PR_WEAVING_WEIGHT_A, PR_WEAVING_WEIGHT_B, PR_SPLICE_WIEGHT, PR_MND_A_DATE, PR_MND_B_DATE, PR_MND_LAYER_REMARK, PR_MENDING_WEIGHT, PR_MENDING_WEIGHT_A, PR_MENDING_WEIGHT_B, PR_NEEDLING_WEIGHT, PR_SEAM_WEIGHT, PR_DIVERSION_FLAG, PR_DIVERSION_REASON, PR_DIVERTED_FLAG, PR_DIVERTED_REASON, PR_DIVERTED_DATE, PR_DIV_BEFORE_STAGE, PR_DIV_NO_CHANGE_DATE, PR_DIV_NO_CHANGE_FLAG, PR_EXP_DISPATCH_DATE, PR_REGION, PR_REFERENCE, PR_REFERENCE_DATE, PR_PO_NO, PR_PO_DATE, PR_ORDER_REMARK, PR_PIECE_REMARK, PR_PKG_DP_NO, PR_PKG_DP_DATE, PR_BALE_NO, PR_PACKED_DATE, PR_INVOICE_NO, PR_INVOICE_DATE, PR_INVOICE_AMOUNT, PR_EXPORT_BL_NO, PR_EXPORT_BL_DATE, PR_EXPORT_UNIT_OF_CURRENCY, PR_EXPORT_EXCHANGE_RATE, PR_EXPORT_INV_AMOUNT, PR_LR_NO, PR_LR_DATE, PR_INVOICE_PARTY, PR_PARTY_CODE_ORIGINAL, PR_PIECE_NO_ORIGINAL, PR_HOLD_DATE, PR_HOLD_REASON, PR_RELEASE_DATE, BALE_REOPEN_FLG, PR_SALES_RETURNS_NO, PR_SALES_RETURNS_DATE, PR_SALES_RETURNS_REMARKS, PR_SALES_RETURNS_FLG, PR_REJECTED_FLAG, PR_REJECTED_REMARK, PR_ADJUSTABLE_LENGTH, PR_ADJUSTABLE_WIDTH, PR_ADJUSTABLE_GSM, PR_ADJUSTABLE_WEIGHT, PR_DELINK, PR_OBSOLETE_DATE, PR_DELINK_REASON, PR_WH_LOCATION_ID, PR_DAYS_ORDER_WARPED, PR_DAYS_ORDER_WARPED_STATUS, PR_DAYS_ORDER_WVG, PR_DAYS_ORDER_WVG_STATUS, PR_DAYS_ORDER_MND, PR_DAYS_ORDER_MND_STATUS, PR_DAYS_ORDER_NDL, PR_DAYS_ORDER_NDL_STATUS, PR_DAYS_ORDER_FNG, PR_DAYS_ORDER_FNG_STATUS, PR_DAYS_WRP_WVG, PR_DAYS_WRP_WVG_STATUS, PR_DAYS_WVG_MND, PR_DAYS_WVG_MND_STATUS, PR_DAYS_MND_NDL, PR_DAYS_MND_NDL_STATUS, PR_DAYS_NDL_FNG, PR_DAYS_NDL_FNG_STATUS, PR_DAYS_MND_FNG, PR_DAYS_MND_FNG_STATUS, PR_DAYS_ORDER_INVOICE, PR_DAYS_ORDER_INVOICE_STATUS, PR_DAYS_STOCK_INVOICE, PR_DAYS_STOCK_INVOICE_STATUS, PR_DAYS_WH_STOCK, PR_DAYS_WH_PACKED, PR_DAYS_STATUS, PR_DAYS_PACK_INVOICE, PR_DAYS_PACK_NOT_INVOICE, PR_DAYS_PACK_NOT_INVOICE_STATUS, PR_DAYS_WH_STOCK_STATUS, PR_DAYS_CURRENT_STAGE, PR_WARP_EXECUTE_DATE, PR_CLOSURE_REOPEN_IND, PR_CLOSURE_DATE, PR_CLOSURE_REMARK, PR_REOPEN_DATE, PR_REOPEN_REMARK, PR_EXPECTED_DISPATCH, PR_EXP_DISPATCH_FROM, PR_EXP_DISPATCH_DOCNO, PE_PIECE_IT_DEPT_REMARK, PR_CONTACT_PERSON, PR_EMAIL_ID, PR_PHONE_NO, PR_OA_MONTHYEAR, PR_OA_NO, PR_OA_DATE, PR_OC_NO, PR_OC_DATE, PR_OC_MONTHYEAR, PR_OC_LAST_DDMMYY, PR_PIECE_OC_MONTH_IT_REMARK, PR_CURRENT_SCH_MONTH, PR_CURRENT_SCH_LAST_DDMMYY, PR_PIECE_CURRENT_MONTH_IT_REMARK, PR_SP_MONTHYEAR, PR_SP_LAST_DDMMYY, PR_SP_REMARKS, PR_EXP_WIP_DELIVERY_DATE, PR_EXP_PI_DATE, PR_ACT_PI_DATE, PR_EXP_DESPATCH_DATE, PR_EXP_PAY_CHQRC_DATE, PR_ACT_PAY_CHQRC_DATE, PR_EXP_PAY_CHQRC_DATE_ORIGINAL, PR_SPL_REQUEST_MONTHYEAR, PR_SPL_REQUEST_DATE, PR_SPL_REQUEST_REMARK, PR_PAYMENT_RCVD_VOUCHER, PR_PAYMENT_SHORT_AMOUNT, PR_PAYMENT_RCVD_AMOUNT, PR_PIECETRIAL_FLAG, PR_PIECETRIAL_CATEGORY, PR_WH_CODE, PR_INWARD_NO, PR_RACK_NO, PR_PIECE_ID, PR_LOCATION, PR_MFG_MONTH, PR_MFG_YEAR, PR_SCHEDULE_MONTH, PR_MFG_SPILL_OVEVER_REMARK, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, HIERARCHY_ID, APPROVER_BY, APPROVER_DATE, APPROVER_REMARK, PR_NEEDLING_WEIGHT_A, PR_NEEDLING_WEIGHT_B, PR_EXP_PAY_CHQRC_REMARKS, PR_EXP_PAY_CHQRC_REMARKS_DATE, PR_EXPORT_PARTY_REMARK, PR_EXPORT_PARTY_ORDERNO, PR_EXPORT_PARTY_PAYMENT_DATE, PR_RATE_INDICATOR, PR_SDF_INSTRUCT_DATE, PR_SDF_SPIRALED_DATE, PR_SDF_ASSEMBLED_DATE, PR_FELT_RATE, PR_FELT_VALUE_WITHOUT_DISCOUNT, PR_RATE_IND_DESPATCH_DATE, PR_SIZE_CRITERIA, PR_PRODUCT_CATG, PR_DFS_IN_DATE, PR_DFS_OUT_DATE, PR_MATERIAL_CODE, PR_OBSOLETE_SOURCE, PR_OBSOLETE_UPN_ASSIGN_STATUS, PR_SCRAP_REASON, PR_UNMAPPED_REASON, PR_WH_EXP_DISPATCH_DATE, PR_WH_EXP_DISPATCH_REMARK, PR_REJECTION_ORIGINATED_FROM, PR_UNABLE_TO_CONTACT, PR_CONTACTED_PERSON, PR_CONTACTED_NO, PR_FOLLOWUP_ADDITIONAL_REMARK
                                    + " ('" + Spilover_Doc_No + "', '" + (i + 1) + "', '" + PieceNo + "', '" + rsDataPiece.getString("PR_PARTY_CODE") + "', "
                                    + "'" + clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, rsDataPiece.getString("PR_PARTY_CODE"))
                                    + "', '" + rsDataPiece.getString("PR_MACHINE_NO") + "', '" + rsDataPiece.getString("PR_POSITION_NO") + "', "
                                    + "'" + rsDataPiece.getString("PR_UPN") + "', '" + rsDataPiece.getString("PR_REQUESTED_MONTH") + "', "
                                    + "'" + rsDataPiece.getString("PR_OC_MONTHYEAR") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_MONTH") + "'"
                                    + ", '" + selected_monthyear + "', '" + rsDataPiece.getString("PR_PRODUCT_CODE") + "', "
                                    + "'" + rsDataPiece.getString("PR_BILL_PRODUCT_CODE") + "', '" + rsDataPiece.getString("PR_GROUP") + "', "
                                    + "'" + rsDataPiece.getString("PR_STYLE") + "', '" + rsDataPiece.getString("PR_BILL_STYLE") + "', "
                                    + "'" + rsDataPiece.getString("PR_LENGTH") + "', '" + rsDataPiece.getString("PR_ACTUAL_LENGTH") + "', "
                                    + "'" + rsDataPiece.getString("PR_BILL_LENGTH") + "', '" + rsDataPiece.getString("PR_WIDTH") + "', "
                                    + "'" + rsDataPiece.getString("PR_ACTUAL_WIDTH") + "', '" + rsDataPiece.getString("PR_SQMTR") + "', "
                                    + "'" + rsDataPiece.getString("PR_BILL_SQMTR") + "', '" + rsDataPiece.getString("PR_GSM") + "', "
                                    + "'" + rsDataPiece.getString("PR_BILL_GSM") + "', '" + rsDataPiece.getString("PR_TENDER_GSM") + "', "
                                    + "'" + rsDataPiece.getString("PR_THORITICAL_WEIGHT") + "', '" + rsDataPiece.getString("PR_TENDER_WEIGHT") + "'"
                                    + ", '" + rsDataPiece.getString("PR_ACTUAL_WEIGHT") + "', '" + rsDataPiece.getString("PR_BILL_WEIGHT") + "', "
                                    + "'" + rsDataPiece.getString("PR_WEAVING_WEIGHT") + "', '" + rsDataPiece.getString("PR_NEEDLING_WEIGHT") + "',"
                                    + " '" + rsDataPiece.getString("PR_SEAM_WEIGHT") + "', '" + rsDataPiece.getString("PR_SPLICE_WIEGHT") + "', "
                                    + "'" + rsDataPiece.getString("PR_WVG_DATE") + "', '" + rsDataPiece.getString("PR_MND_DATE") + "',"
                                    + " '" + rsDataPiece.getString("PR_NDL_DATE") + "', '" + rsDataPiece.getString("PR_INCHARGE") + "', "
                                    + "'" + rsDataPiece.getString("PR_PIECE_STAGE") + "', '" + rsDataPiece.getString("PR_DELINK") + "', "
                                    + "'" + rsDataPiece.getString("PR_OBSOLETE_DATE") + "', '" + rsDataPiece.getString("PR_REQ_MTH_LAST_DDMMYY") + "', '" + rsDataPiece.getString("PR_OC_LAST_DDMMYY") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_LAST_DDMMYY") + "', '" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "', '" + rsDataPiece.getString("PR_PIECE_STAGE") + "', '', '', '" + rsDataPiece.getString("PR_INVOICE_NO") + "', '" + rsDataPiece.getString("PR_INVOICE_DATE") + "', '" + rsDataPiece.getString("PR_CURRENT_SCH_MONTH") + "', '', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i) + "', '" + DataModel.getValueByVariable("CONTACTED_PERSON", i) + "', '" + DataModel.getValueByVariable("PARTY_JUSTIFICATION", i) + "', '" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "', '" + rsDataPiece.getString("PR_FNSG_DATE") + "','" + selected_monthyear + "') ");

                            data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET "
                                    + " EXPECTED_MONTH_OF_DISPATCH='" + selected_monthyear + "',"
                                    + "CONTACT_PERSON='" + contacted_person + "',CONTACTED_NO='" + contacted_no + "', "
                                    + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                                    + "MODE_OF_COMMUNICATION='" + DataModel.getValueByVariable("MODE_OF_COMMUNICATION", i) + "',"
                                    + "PARTY_JUSTIFICATION='" + DataModel.getValueByVariable("PARTY_JUSTIFICATION", i) + "',"
                                    + "AREA_MANAGER_COMMENT='" + DataModel.getValueByVariable("AREA_MANAGER_COMMENT", i) + "' "
                                    + " WHERE PIECE_NO='" + PieceNo + "' AND DOC_MONTH='" + txtMonth.getText() + "' AND DOC_YEAR='" + txtYear.getText() + "'");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        }

        try {

            data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL R,\n"
                    + "\n"
                    + "(SELECT PIECE_NO, PARTY_CODE,PARTY_NAME,COUNT(PIECE_NO) AS CNT,GROUP_CONCAT(RE_SCH_MONTH ORDER BY RE_SCH_MONTH_DATE DESC)  AS HISTORY\n"
                    + "\n"
                    + "FROM\n"
                    + "\n"
                    + "(SELECT DISTINCT PIECE_NO, PARTY_CODE,PARTY_NAME,RE_SCH_MONTH_DATE ,CONCAT(RE_SCH_MONTH,'(',CUR_PIECE_STAGE,')') AS RE_SCH_MONTH\n"
                    + "\n"
                    + "FROM PRODUCTION.SPILLOVER_RESCHEDULING_DETAIL) AS P\n"
                    + "\n"
                    + "GROUP BY  PIECE_NO, PARTY_CODE,PARTY_NAME)  AS M\n"
                    + "\n"
                    + "SET RESCHEDULED_MONTH_HISTORY  = HISTORY, RESCHEDULED_MONTH_COUNT = CNT\n"
                    + "\n"
                    + "WHERE M.PIECE_NO = R.PIECE_NO");

            data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SF,\n"
                    + "\n"
                    + "(SELECT PIECE_NO, COUNT(PR_WH_EXP_DISPATCH_DATE) AS DD FROM PRODUCTION.FELT_SALES_EXPECTED_DISPATCH_DATE_HISTORY\n"
                    + "\n"
                    + "WHERE PR_WH_EXP_DISPATCH_DATE != '0000-00-00'\n"
                    + "\n"
                    + "GROUP BY PIECE_NO) AS H\n"
                    + "\n"
                    + "SET DATE_COUNTER_MONTH = DD\n"
                    + "\n"
                    + "WHERE SF.PIECE_NO = H.PIECE_NO");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DATA_SAVED) {
            JOptionPane.showMessageDialog(this, "Data updated successfully.");
        }
        if (MONTH_CHANGE_REQUEST) {
            JOptionPane.showMessageDialog(this, "Data updated successfully. Document Generated : " + Month_Change_Doc_No);
            //Month_Change_Doc_No
        }
        DisplayData(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
    }//GEN-LAST:event_btnSaveActionPerformed

    private void tblHistoryAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblHistoryAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblHistoryAncestorMoved

    private void tblHistoryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblHistoryFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblHistoryFocusGained

    private void tblHistoryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblHistoryFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblHistoryFocusLost

    private void tblHistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHistoryMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            //String PieceNo = DataModel_History.getValueByVariable("PIECE_NO", tblHistory.getSelectedRow());
            AppletFrame aFrame = new AppletFrame("Follow Up History");
            aFrame.startAppletEx("EITLERP.FeltSales.SpilloverRescheduling_New.frmPieceReschedulingDetails_New", "Follow Up History");
            frmPieceReschedulingDetails_New ObjItem = (frmPieceReschedulingDetails_New) aFrame.ObjApplet;

            String PieceNo = DataModel_History.getValueByVariable("PIECE_NO", tblHistory.getSelectedRow());
            //System.out.println(" PieceNo "+PieceNo);
            ObjItem.requestFocus();
            // ObjItem.Doc_No=DFNo.getText();
            ObjItem.FindByPieceNo(PieceNo);
        }
    }//GEN-LAST:event_tblHistoryMouseClicked

    private void tblHistoryCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblHistoryCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblHistoryCaretPositionChanged

    private void tblHistoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblHistoryKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblHistoryKeyPressed

    private void tblHistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblHistoryKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblHistoryKeyReleased

    private void cmbZoneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbZoneItemStateChanged
        // TODO add your handling code here:
        DisplayData(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
        DisplayData_Master(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
    }//GEN-LAST:event_cmbZoneItemStateChanged

    private void tblPieceClubbingHeaderAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblPieceClubbingHeaderAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingHeaderAncestorMoved

    private void tblPieceClubbingHeaderFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPieceClubbingHeaderFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingHeaderFocusGained

    private void tblPieceClubbingHeaderFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPieceClubbingHeaderFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingHeaderFocusLost

    private void tblPieceClubbingHeaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPieceClubbingHeaderMouseClicked
        // TODO add your handling code here:
        FormatGrid_ClubbingDetail();

        String ClubbingNo = DataModel_Clubbing.getValueByVariable("CLUBBING_NO", tblPieceClubbingHeader.getSelectedRow());
        txtSelectedClubbingPartyCode.setText(DataModel_Clubbing.getValueByVariable("PARTY_CODE", tblPieceClubbingHeader.getSelectedRow()));
        txtSelectedClubbingNo.setText(ClubbingNo);
        txtApprovalStatus.setText(DataModel_Clubbing.getValueByVariable("APPROVAL_STATUS", tblPieceClubbingHeader.getSelectedRow()));
        try {
            //System.out.println("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL where PC_DOC_NO='"+ClubbingNo+"' AND CURRENT_STATUS='Active'");    
            ResultSet rsDataHeader = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL where PC_DOC_NO='" + ClubbingNo + "' AND CURRENT_STATUS='Active'");

            int srNo = 0;
            rsDataHeader.first();
            while (!rsDataHeader.isAfterLast()) {
                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_ClubbingDetail.addRow(rowData);

                String PieceNo = rsDataHeader.getString("PIECE_NO");
                ResultSet rsRegister = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");

                DataModel_ClubbingDetail.setValueByVariable("SrNo", srNo + "", NewRow);

                DataModel_ClubbingDetail.setValueByVariable("PIECE_NO", PieceNo, NewRow);
                DataModel_ClubbingDetail.setValueByVariable("REQ_MONTH", rsRegister.getString("PR_REQUESTED_MONTH"), NewRow);
                DataModel_ClubbingDetail.setValueByVariable("OC_MONTH", rsRegister.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_ClubbingDetail.setValueByVariable("CURRENT_SALES_PLAN", rsRegister.getString("PR_CURRENT_SCH_MONTH"), NewRow);
                DataModel_ClubbingDetail.setValueByVariable("CURRENT_PIECE_STAGE", rsRegister.getString("PR_PIECE_STAGE"), NewRow);
                DataModel_ClubbingDetail.setValueByVariable("LENGTH", rsRegister.getString("PR_BILL_LENGTH"), NewRow);
                DataModel_ClubbingDetail.setValueByVariable("WIDTH", rsRegister.getString("PR_BILL_WIDTH"), NewRow);
                DataModel_ClubbingDetail.setValueByVariable("GSM", rsRegister.getString("PR_BILL_GSM"), NewRow);
                DataModel_ClubbingDetail.setValueByVariable("SQMTR", rsRegister.getString("PR_BILL_SQMTR"), NewRow);
                DataModel_ClubbingDetail.setValueByVariable("WEIGHT", rsRegister.getString("PR_BILL_WEIGHT"), NewRow);

                rsDataHeader.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_tblPieceClubbingHeaderMouseClicked

    private void tblPieceClubbingHeaderCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblPieceClubbingHeaderCaretPositionChanged
        // TODO add your handling code here:


    }//GEN-LAST:event_tblPieceClubbingHeaderCaretPositionChanged

    private void tblPieceClubbingHeaderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPieceClubbingHeaderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingHeaderKeyPressed

    private void tblPieceClubbingHeaderKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPieceClubbingHeaderKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingHeaderKeyReleased

    private void Export_WeavingProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Export_WeavingProductionActionPerformed
        //        // TODO add your handling code here:
        //        try{
        //            exp.fillData(Table_b2b,new File("/root/Desktop/gstr1_B2B.xls"));
        //            exp.fillData(Table_b2b,new File("D://gstr1_B2B.xls"));
        //            JOptionPane.showMessageDialog(null, "Data saved at " +
        //                "'/root/Desktop/gstr1_B2B.xls' successfully in Linux PC or 'D://gstr1_B2B.xls' successfully in Windows PC    ", "Message",
        //                JOptionPane.INFORMATION_MESSAGE);
        //        }
        //        catch(Exception ex) {
        //            ex.printStackTrace();
        //        }
        // TODO add your handling code here:
        File file = null;
        file1.setVisible(true);
        try {
            file1.setSelectedFile(new File("SalesFollowup.xls"));
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {

                file = file1.getSelectedFile();
            }

            exp.fillData(Table, file, "SalesFollowup");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file1.getSelectedFile().toString() + " successfully... ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }//GEN-LAST:event_Export_WeavingProductionActionPerformed

    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        // TODO add your handling code here:
        String Incharge_Name = cmbZone.getSelectedItem().toString();

        String Incharge_Cd = data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + Incharge_Name + "'");
        txtInchargeCd.setText(Incharge_Cd);
        txtInchargeName.setText(Incharge_Name);
        try {
            String qry = "SELECT * FROM PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL_HISTORY H,PRODUCTION.FELT_SALES_PIECE_REGISTER PR "
                    + " where PIECE_NO=PR_PIECE_NO AND PR.PR_PIECE_STAGE NOT IN ('INVOICED')"
                    + ""
                    + " AND PIECE_NO  NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE CURRENT_STATUS='Active') "
                    + "";

            if (Incharge_Name.equals("ACNE/KEYCLIENT")) {
                qry = qry + "AND (PR.PR_INCHARGE='5' OR  PR.PR_INCHARGE='7') ";
            } else if (!Incharge_Name.equals("ALL")) {
                qry = qry + "AND PR.PR_INCHARGE='" + Incharge_Cd + "'";
            }

            if (!txtPieceNo_Search.getText().equals("")) {
                qry = qry + "AND H.PIECE_NO='" + txtPieceNo_Search.getText() + "'";
            }

            if (!txtPartyCode_Search.getText().equals("")) {
                qry = qry + "AND PR.PR_PARTY_CODE='" + txtPartyCode_Search.getText() + "'";
            }

            qry = qry + " AND ENTRY_DATETIME>='" + EITLERPGLOBAL.formatDateDB(txtHistoryDate.getText()) + " 00:00:00'";
            qry = qry + " AND ENTRY_DATETIME<='" + EITLERPGLOBAL.formatDateDB(txtHistoryDate.getText()) + " 23:59:59'";
            System.out.println("qry " + qry);
            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(qry);
            int srNo = 0;
            FormatGrid_History();
            resultSet.first();
            while (!resultSet.isAfterLast()) {
                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_History.addRow(rowData);

                DataModel_History.setValueByVariable("SrNo", srNo + "", NewRow);

                DataModel_History.setValueByVariable("PIECE_NO", resultSet.getString("PIECE_NO"), NewRow);
                DataModel_History.setValueByVariable("PARTY_CODE", resultSet.getString("PR_PARTY_CODE"), NewRow);
                //DataModel_History.setValueByVariable("OC_MONTH", resultSet.getString("PR_OC_MONTHYEAR"), NewRow);
                DataModel_History.setValueByVariable("OC_MONTH", resultSet.getString("OC_MONTH"), NewRow);
                //DataModel_History.setValueByVariable("CURRENT_SCH_MONTH", resultSet.getString("PR_CURRENT_SCH_MONTH"), NewRow);
                DataModel_History.setValueByVariable("CURRENT_SCH_MONTH", resultSet.getString("CURR_SCH_MONTH"), NewRow);
                DataModel_History.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, resultSet.getString("PARTY_CODE")), NewRow);
                DataModel_History.setValueByVariable("EXPECTED_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("H.FOLLOWUP_DATE")), NewRow);

                String ChargeCode = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='" + resultSet.getString("PARTY_CODE") + "'");
                DataModel_History.setValueByVariable("CHARGE_CODE", ChargeCode, NewRow);

                DataModel_History.setValueByVariable("REMARK", resultSet.getString("DELAY_REASON"), NewRow);
                if ("1".equals(resultSet.getString("UNABLE_TO_CONTACT"))) {
                    DataModel_History.setValueByVariable("UNABLE_TO_CONTACT", true, NewRow);
                } else {
                    DataModel_History.setValueByVariable("UNABLE_TO_CONTACT", false, NewRow);
                }

                DataModel_History.setValueByVariable("CONTACTED_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_History.setValueByVariable("CONTACTED_NO", resultSet.getString("CONTACTED_NO"), NewRow);

                DataModel_History.setValueByVariable("DATE_OF_COMMUNICATION", EITLERPGLOBAL.formatDate(resultSet.getString("DATE_OF_COMMUNICATION")), NewRow);
                DataModel_History.setValueByVariable("MODE_OF_COMMUNICATION", resultSet.getString("MODE_OF_COMMUNICATION"), NewRow);
                DataModel_History.setValueByVariable("PARTY_JUSTIFICATION", resultSet.getString("PARTY_JUSTIFICATION"), NewRow);
                DataModel_History.setValueByVariable("AREA_MANAGER_COMMENT", resultSet.getString("AREA_MANAGER_COMMENT"), NewRow);
                DataModel_History.setValueByVariable("EXPECTED_MONTH_OF_DISPATCH", resultSet.getString("EXPECTED_MONTH_OF_DISPATCH"), NewRow);
                
                DataModel_History.setValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", resultSet.getString("PARTY_LIFTING_COMMITMENT_STATUS"), NewRow);
                DataModel_History.setValueByVariable("PARTY_LIFTING_COMMITMENT_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PARTY_LIFTING_COMMITMENT_DATE")), NewRow);

                /*
                 DataModel_History.SetVariable(12, "");
                 DataModel_History.SetVariable(13, "");
                 DataModel_History.SetVariable(14, "");
                 DataModel_History.SetVariable(15, "");
                 DataModel_History.SetVariable(16, "");
                 */
                DataModel_History.setValueByVariable("DATE_TIME", resultSet.getString("ENTRY_DATETIME"), NewRow);

                resultSet.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            String qry = "SELECT * FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY H "
                    + " where PIECE_NO!='' "
                    + ""
                    + "  "
                    + "";

//            if (Incharge_Name.equals("ACNE/KEYCLIENT")) {
//                qry = qry + "AND (PR.PR_INCHARGE='5' OR  PR.PR_INCHARGE='7') ";
//            } else if (!Incharge_Name.equals("ALL")) {
//                qry = qry + "AND PR.PR_INCHARGE='" + Incharge_Cd + "'";
//            }
            if (!txtPieceNo_Search.getText().equals("")) {
                qry = qry + "AND H.PIECE_NO like  '%" + txtPieceNo_Search.getText() + "%'";
            }

            if (!txtPartyCode_Search.getText().equals("")) {
                qry = qry + "AND H.PARTY_CODE='" + txtPartyCode_Search.getText() + "'";
            }

            qry = qry + " AND ENTRY_DATETIME>='" + EITLERPGLOBAL.formatDateDB(txtHistoryDate.getText()) + " 00:00:00'";
            qry = qry + " AND ENTRY_DATETIME<='" + EITLERPGLOBAL.formatDateDB(txtHistoryDate.getText()) + " 23:59:59'";
            System.out.println("qry " + qry);

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(qry);
            int srNo = 0;
            FormatGrid_Clubbed_History();
            resultSet.first();
            while (!resultSet.isAfterLast()) {
                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_Clubbed_History.addRow(rowData);

                DataModel_Clubbed_History.setValueByVariable("SrNo", srNo + "", NewRow);

                DataModel_Clubbed_History.setValueByVariable("PIECE_NO", resultSet.getString("PIECE_NO"), NewRow);
                DataModel_Clubbed_History.setValueByVariable("PARTY_CODE", resultSet.getString("PARTY_CODE"), NewRow);
                DataModel_Clubbed_History.setValueByVariable("OC_MONTH", resultSet.getString("OC_MONTH"), NewRow);
                DataModel_Clubbed_History.setValueByVariable("CURRENT_SCH_MONTH", resultSet.getString("CURR_SCH_MONTH"), NewRow);
                DataModel_Clubbed_History.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, resultSet.getString("PARTY_CODE")), NewRow);
                DataModel_Clubbed_History.setValueByVariable("EXPECTED_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("H.FOLLOWUP_DATE")), NewRow);

                String ChargeCode = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER where PARTY_CODE='" + resultSet.getString("PARTY_CODE") + "'");
                DataModel_Clubbed_History.setValueByVariable("CHARGE_CODE", ChargeCode, NewRow);

                DataModel_Clubbed_History.setValueByVariable("REMARK", resultSet.getString("DELAY_REASON"), NewRow);
                if ("1".equals(resultSet.getString("UNABLE_TO_CONTACT"))) {
                    DataModel_Clubbed_History.setValueByVariable("UNABLE_TO_CONTACT", true, NewRow);
                } else {
                    DataModel_Clubbed_History.setValueByVariable("UNABLE_TO_CONTACT", false, NewRow);
                }

                DataModel_Clubbed_History.setValueByVariable("CONTACTED_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_Clubbed_History.setValueByVariable("CONTACTED_NO", resultSet.getString("CONTACTED_NO"), NewRow);

                DataModel_Clubbed_History.setValueByVariable("DATE_OF_COMMUNICATION", EITLERPGLOBAL.formatDate(resultSet.getString("DATE_OF_COMMUNICATION")), NewRow);
                DataModel_Clubbed_History.setValueByVariable("MODE_OF_COMMUNICATION", resultSet.getString("MODE_OF_COMMUNICATION"), NewRow);
                DataModel_Clubbed_History.setValueByVariable("PARTY_JUSTIFICATION", resultSet.getString("PARTY_JUSTIFICATION"), NewRow);
                DataModel_Clubbed_History.setValueByVariable("AREA_MANAGER_COMMENT", resultSet.getString("AREA_MANAGER_COMMENT"), NewRow);

                DataModel_Clubbed_History.setValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", resultSet.getString("PARTY_LIFTING_COMMITMENT_STATUS"), NewRow);
                DataModel_Clubbed_History.setValueByVariable("PARTY_LIFTING_COMMITMENT_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("PARTY_LIFTING_COMMITMENT_DATE")), NewRow);

                /*
                 DataModel_History.SetVariable(12, "");
                 DataModel_History.SetVariable(13, "");
                 DataModel_History.SetVariable(14, "");
                 DataModel_History.SetVariable(15, "");
                 DataModel_History.SetVariable(16, "");
                 */
                DataModel_Clubbed_History.setValueByVariable("DATE_TIME", resultSet.getString("ENTRY_DATETIME"), NewRow);

                resultSet.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnHistoryActionPerformed

    private void btnClubbingHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClubbingHistoryActionPerformed
        // TODO add your handling code here:
        String Incharge_Name = cmbZone.getSelectedItem().toString();

        String Incharge_Cd = data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + Incharge_Name + "'");
        txtInchargeCd_Clubbing.setText(Incharge_Cd);
        txtInchargeName_Clubbing.setText(Incharge_Name);
        try {
            //String qry = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER D WHERE PC_DOC_NO!=''";
            String qry = "SELECT D.*,P.PARTY_NAME,P.INCHARGE_CD FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER D,DINESHMILLS.D_SAL_PARTY_MASTER P "
                    + "WHERE D.PARTY_CODE=P.PARTY_CODE  ";
            System.out.println("qry " + qry);
            if (Incharge_Cd.equals("11")) {
                qry = qry + "AND P.INCHARGE_CD IN ('7','5')";
            } else if (!Incharge_Name.equals("ALL")) {
                qry = qry + "AND P.INCHARGE_CD='" + Incharge_Cd + "'";
            }

            String status = "";

            if (chkINSTOCK.isSelected()) {
                status = "'Active'";
            }
            if (chkWIP.isSelected()) {
                if (status.equals("")) {
                    status = "'InProcess'";
                } else {
                    status = status + ",'InProcess'";
                }
            }
            if (chkINVOICED.isSelected()) {
                if (status.equals("")) {
                    status = "'Invoiced'";
                } else {
                    status = status + ",'Invoiced'";
                }
            }

            if (status.equals("")) {
                status = "'Active','InProcess','Invoiced'";
            }

            if (!status.equals("")) {
                qry = qry + " AND (D.PC_DOC_NO IN (SELECT DOC_NO FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL\n"
                        + "where CURRENT_STATUS IN (" + status + ")) OR D.PC_DOC_NO IN (SELECT PC_DOC_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER WHERE APPROVED=0 AND CANCELED=0) )";
            }

            if (!txtClubbibgNo.getText().equals("")) {
                qry = qry + " AND D.PC_DOC_NO='" + txtClubbibgNo.getText() + "' ";
            }
            if (!txtPartyCode_ClubbingSearch.getText().equals("")) {
                qry = qry + " AND D.PARTY_CODE='" + txtPartyCode_ClubbingSearch.getText() + "' ";
            }
            if (!txtPieceNo_ClubbingSearch.getText().equals("")) {
                String DocNo = data.getStringValueFromDB("SELECT PC_DOC_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE PIECE_NO='" + txtPieceNo_ClubbingSearch.getText() + "' AND CURRENT_STATUS='Active'");
                if (!"".equals(DocNo)) {
                    qry = qry + " AND D.PC_DOC_NO='" + DocNo + "' ";
                }
            }

            System.out.println("Clubbing : " + qry);

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(qry);
            int srNo = 0;
            FormatGrid_Clubbing();
            resultSet.first();
            while (!resultSet.isAfterLast()) {
                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_Clubbing.addRow(rowData);

                DataModel_Clubbing.setValueByVariable("SrNo", srNo + "", NewRow);

                DataModel_Clubbing.setValueByVariable("CLUBBING_NO", resultSet.getString("PC_DOC_NO"), NewRow);
                DataModel_Clubbing.setValueByVariable("PARTY_CODE", resultSet.getString("PARTY_CODE"), NewRow);
                DataModel_Clubbing.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, resultSet.getString("PARTY_CODE")), NewRow);
                DataModel_Clubbing.setValueByVariable("NO_OF_PIECE", "" + data.getStringValueFromDB("SELECT count(*) as NO_OF_RECORDS FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL where PC_DOC_NO='" + resultSet.getString("PC_DOC_NO") + "' AND CURRENT_STATUS='Active'"), NewRow);
                DataModel_Clubbing.setValueByVariable("LAST_OC_MONTH", resultSet.getString("LAST_OC_MONTH"), NewRow);
                DataModel_Clubbing.setValueByVariable("LAST_CURR_SCH_MONTH", resultSet.getString("LAST_CURR_SCH_MONTH"), NewRow);

                if (resultSet.getString("APPROVED").equals("1")) {
                    DataModel_Clubbing.setValueByVariable("APPROVAL_STATUS", "Approved", NewRow);
                } else {
                    DataModel_Clubbing.setValueByVariable("APPROVAL_STATUS", "Not Approved", NewRow);
                }

                resultSet.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnClubbingHistoryActionPerformed

    private void txtPartyCode_SearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCode_SearchKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            EITLERP.LOV aList = new EITLERP.LOV();
//            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_CLOSE_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 ";
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtPartyCode_Search.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtPartyCode_SearchKeyPressed

    private void AddPieceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddPieceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddPieceActionPerformed

    private void btnShowDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowDataActionPerformed
        // TODO add your handling code here:
        txtFWLMonth.setText(txtFollowupDate.getText().substring(3, 5));
        txtFWLYear.setText(txtFollowupDate.getText().substring(6, 10));
        String month_name_FWL = getMonthName(Integer.parseInt(txtFollowupDate.getText().substring(3, 5))) + " - " + txtFollowupDate.getText().substring(6, 10);
        txtFWLCurrentSchMonth.setText(month_name_FWL);

        DisplayData(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
        DisplayData_Master(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
    }//GEN-LAST:event_btnShowDataActionPerformed

    private void txtPartyCode_ClubbingSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCode_ClubbingSearchKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            EITLERP.LOV aList = new EITLERP.LOV();
//            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_CLOSE_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 ";
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtPartyCode_ClubbingSearch.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtPartyCode_ClubbingSearchKeyPressed

    private void tblPieceClubbingDetailAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblPieceClubbingDetailAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingDetailAncestorMoved

    private void tblPieceClubbingDetailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPieceClubbingDetailFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingDetailFocusGained

    private void tblPieceClubbingDetailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPieceClubbingDetailFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingDetailFocusLost

    private void tblPieceClubbingDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPieceClubbingDetailMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingDetailMouseClicked

    private void tblPieceClubbingDetailCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblPieceClubbingDetailCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingDetailCaretPositionChanged

    private void tblPieceClubbingDetailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPieceClubbingDetailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingDetailKeyPressed

    private void tblPieceClubbingDetailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPieceClubbingDetailKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPieceClubbingDetailKeyReleased

    private void btnAddPieceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPieceActionPerformed
        // TODO add your handling code here:
        if (txtApprovalStatus.getText().equals("Approved")) {
            JOptionPane.showMessageDialog(this, "This document is approved. Please Amend Document");
            return;
        }
        if (!txtSelectedClubbingPartyCode.getText().equals("")) {
            EITLERP.LOV aList = new EITLERP.LOV();

            aList.SQL = "SELECT PR_PIECE_NO,PR_OC_MONTHYEAR,PR_CURRENT_SCH_MONTH,PR_PIECE_STAGE,PR_FNSG_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + txtSelectedClubbingPartyCode.getText() + "' "
                    + "AND PR_PIECE_STAGE IN ('IN STOCK','SEAMING','NEEDLING','MENDING','FINISHING','OSG STOCK','WEAVING','PLANNING','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)"
                    + " AND PR_PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL where PC_DOC_NO='" + txtSelectedClubbingNo.getText() + "')"
                    + "";
            System.out.println("SELECT PR_PIECE_NO,PR_OC_MONTHYEAR,PR_CURRENT_SCH_MONTH,PR_PIECE_STAGE,PR_FNSG_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PARTY_CODE='" + txtSelectedClubbingPartyCode.getText() + "' "
                    + "AND PR_PIECE_STAGE IN ('IN STOCK','SEAMING','NEEDLING','MENDING','FINISHING','OSG STOCK','WEAVING','PLANNING','BSR','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') AND (PR_DELINK!='OBSOLETE' OR PR_DELINK IS NULL)"
                    + " AND PR_PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL where PC_DOC_NO='" + txtSelectedClubbingNo.getText() + "')"
                    + "");
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                //aList.ReturnVal
                try {
                    int total_row = tblPieceClubbingDetail.getRowCount();
                    Object[] rowData = new Object[1];
                    DataModel_ClubbingDetail.addRow(rowData);

                    String PieceNo = aList.ReturnVal;
                    ResultSet rsRegister = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + PieceNo + "'");

                    DataModel_ClubbingDetail.setValueByVariable("SrNo", (total_row + 1) + "", total_row);

                    DataModel_ClubbingDetail.setValueByVariable("PIECE_NO", PieceNo, total_row);
                    DataModel_ClubbingDetail.setValueByVariable("REQ_MONTH", rsRegister.getString("PR_REQUESTED_MONTH"), total_row);
                    DataModel_ClubbingDetail.setValueByVariable("OC_MONTH", rsRegister.getString("PR_OC_MONTHYEAR"), total_row);
                    DataModel_ClubbingDetail.setValueByVariable("CURRENT_SALES_PLAN", rsRegister.getString("PR_CURRENT_SCH_MONTH"), total_row);
                    DataModel_ClubbingDetail.setValueByVariable("CURRENT_PIECE_STAGE", rsRegister.getString("PR_PIECE_STAGE"), total_row);
                    DataModel_ClubbingDetail.setValueByVariable("LENGTH", rsRegister.getString("PR_BILL_LENGTH"), total_row);
                    DataModel_ClubbingDetail.setValueByVariable("WIDTH", rsRegister.getString("PR_BILL_WIDTH"), total_row);
                    DataModel_ClubbingDetail.setValueByVariable("GSM", rsRegister.getString("PR_BILL_GSM"), total_row);
                    DataModel_ClubbingDetail.setValueByVariable("SQMTR", rsRegister.getString("PR_BILL_SQMTR"), total_row);
                    DataModel_ClubbingDetail.setValueByVariable("WEIGHT", rsRegister.getString("PR_BILL_WEIGHT"), total_row);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }//GEN-LAST:event_btnAddPieceActionPerformed

    private void btnRemovePieceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePieceActionPerformed
        // TODO add your handling code here:
        if (txtApprovalStatus.getText().equals("Approved")) {
            JOptionPane.showMessageDialog(this, "This document is approved. Please Amend Document");
            return;
        } else {
            if (tblPieceClubbingDetail.getRowCount() > 0) {
                DataModel_ClubbingDetail.removeRow(tblPieceClubbingDetail.getSelectedRow());
                // DisplayIndicators();

                //DataModel.getValueAt(FFNo, WIDTH)
            }
        }

    }//GEN-LAST:event_btnRemovePieceActionPerformed

    private void btnUpdateTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateTransactionActionPerformed
        // TODO add your handling code here:

        if (txtApprovalStatus.getText().equals("Approved")) {
            JOptionPane.showMessageDialog(this, "This document is approved. Please Amend Document");
            return;
        }

        try {

            data.Execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL where PC_DOC_NO='" + txtSelectedClubbingNo.getText() + "'");

            for (int i = 0; i < tblPieceClubbingDetail.getRowCount(); i++) {
                data.Execute("INSERT INTO PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL "
                        + "(PC_DOC_NO, PC_DOC_DATE, PIECE_NO, REMARK, CURRENT_STATUS, USER_ID, REQ_MONTH, OC_MONTH, CURRENT_SALES_PLAN, PIECE_STAGE) values "
                        + "('" + txtSelectedClubbingNo.getText() + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', "
                        + "'" + DataModel_ClubbingDetail.getValueByVariable("PIECE_NO", i) + "', '', 'Active', '" + EITLERPGLOBAL.gNewUserID + "', '" + DataModel_ClubbingDetail.getValueByVariable("REQ_MONTH", i) + "',"
                        + " '" + DataModel_ClubbingDetail.getValueByVariable("OC_MONTH", i) + "', "
                        + "'" + DataModel_ClubbingDetail.getValueByVariable("CURRENT_SALES_PLAN", i) + "', '" + DataModel_ClubbingDetail.getValueByVariable("CURRENT_PIECE_STAGE", i) + "')");
            }

            try {
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H,\n"
                        + "(SELECT * FROM (SELECT PC_DOC_NO,PR_CURRENT_SCH_MONTH,PR_OC_MONTHYEAR,PR_CURRENT_SCH_LAST_DDMMYY\n"
                        + "FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL D,PRODUCTION.FELT_SALES_PIECE_REGISTER R\n"
                        + "WHERE D.PIECE_NO=R.PR_PIECE_NO\n"
                        + "ORDER BY PR_CURRENT_SCH_LAST_DDMMYY DESC) AS D\n"
                        + "GROUP BY PC_DOC_NO) AS D\n"
                        + "SET H.LAST_OC_MONTH=PR_OC_MONTHYEAR,H.LAST_CURR_SCH_MONTH=D.PR_CURRENT_SCH_MONTH\n"
                        + "WHERE H.PC_DOC_NO=D.PC_DOC_NO");

                data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL D,\n"
                        + "PRODUCTION.FELT_SALES_PIECE_CLUBBING_HEADER H \n"
                        + "SET D.EXPECTED_MONTH_OF_DISPATCH=H.LAST_CURR_SCH_MONTH WHERE D.DOC_NO=H.PC_DOC_NO");
            } catch (Exception e) {
                e.printStackTrace();
            }

            btnClubbingHistoryActionPerformed(null);
            JOptionPane.showMessageDialog(this, "Record Updated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateTransactionActionPerformed

    private void btnClubbingFollowupSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClubbingFollowupSaveActionPerformed
        // TODO add your handling code here:

        //if(!cmbCurrSchMonthClubbing.getSelectedItem().equals(txtCurrentSchMonth.getText()))
        //{
        for (int i = 0; i <= tblPieceClubbingMaster.getRowCount() - 1; i++) {

            int col_unable_to_contact = DataModel_CLB_Master.getColFromVariable("UNABLE_TO_CONTACT");
            String NewExpectedDate = DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i);
            try {

//                    if (!tblPieceClubbingMaster.getValueAt(i, col_unable_to_contact).equals(true))
//                    {    
//                        if(!cmbCurrSchMonthClubbing.getSelectedItem().equals(txtCurrentSchMonth.getText()) && !DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals(""))
//                        {
//                            JOptionPane.showMessageDialog(this, "You can update Follow Up detail for Current Month Only.");
//                            return;
//                        }
//                    }
                if (!"".equals(NewExpectedDate)) {
                    int Month = EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(NewExpectedDate));

                    int Year = EITLERPGLOBAL.getYear(EITLERPGLOBAL.formatDateDB(NewExpectedDate));

                    if (Month != EITLERPGLOBAL.getCurrentMonth() || Year != EITLERPGLOBAL.getCurrentYear()) {
                        JOptionPane.showMessageDialog(this, "Follow Up Date should be current Month for Clubbing  " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                        return;
                    }
                }
            } catch (Exception e) {

            }

            if (tblPieceClubbingMaster.getValueAt(i, col_unable_to_contact).equals(true)) {

//                    if(!txtCurrentSchMonth.getText().equals(txtCurrentSchMonth.getText()))
//                    {
//                        JOptionPane.showMessageDialog(this, "Only Current Month Allowed for Edit");
//                        return;
//                    }
                if (!DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "UNABLE TO CONTACT and FOLLOW UP DATE, Both are not allowed at same time, For CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }

                if (DataModel_CLB_Master.getValueByVariable("CONTACTED_PERSON", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted person for CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
                if (DataModel_CLB_Master.getValueByVariable("CONTACTED_NO", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted number for CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
                if (!DataModel_CLB_Master.getValueByVariable("MODE_OF_COMMUNICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Mode Of Comminication should be blank for Unable to Contact for CLUBBING No " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
                if (!DataModel_CLB_Master.getValueByVariable("PARTY_JUSTIFICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Party Justification should be blank for Unable to Contact for CLUBBING No " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
//                    if (!DataModel_CLB_Master.getValueByVariable("AREA_MANAGER_COMMENT", i).equals("")) {
//                        JOptionPane.showMessageDialog(this, "Area Manager Comment should be blank for Unable to Contact for CLUBBING No " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
//                        return;
//                    }
            }
            if (!DataModel_CLB_Master.getValueByVariable("PIECE_NO", i).equals("") && !DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {
                System.out.println("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i)) + "','" + EITLERPGLOBAL.getCurrentDateDB() + "')");
                int date_diff = data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i)) + "','" + EITLERPGLOBAL.getCurrentDateDB() + "')");
                if (date_diff <= 0) {
                    JOptionPane.showMessageDialog(this, "Expected Date " + DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i) + " is not valid for CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
            }
            
            
            if (!DataModel_CLB_Master.getValueByVariable("PIECE_NO", i).equals("") && !DataModel_CLB_Master.getValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", i).equals("")) {
                if (EITLERPGLOBAL.formatDateDB(DataModel_CLB_Master.getValueByVariable("PARTY_LIFTING_COMMITMENT_DATE", i)).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter Party Lifting Commitment Date for CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
            }
            
            if (!DataModel_CLB_Master.getValueByVariable("PIECE_NO", i).equals("") && !DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {

//                    if(!txtCurrentSchMonth.getText().equals(cmbCurrSchMonthClubbing.getSelectedItem()))
//                    {
//                        JOptionPane.showMessageDialog(this, "Only Current Month Allowed for Edit");
//                        return;
//                    }
                if (DataModel_CLB_Master.getValueByVariable("CONTACTED_PERSON", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted person for CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
                if (DataModel_CLB_Master.getValueByVariable("CONTACTED_NO", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted number for CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }

                if (DataModel_CLB_Master.getValueByVariable("MODE_OF_COMMUNICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter MODE OF COMMUNICATION for CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
                if (DataModel_CLB_Master.getValueByVariable("PARTY_JUSTIFICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter PARTY JUSTIFICATION for CLUBBING NO " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
                if (DataModel_CLB_Master.getValueByVariable("AREA_MANAGER_COMMENT", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter Area Manager Comments for CLUBBING No " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i));
                    return;
                }
                /*
                 DataModel.SetVariable(21, "DATE_OF_COMMUNICATION");
                 DataModel.SetVariable(22, "MODE_OF_COMMUNICATION");
                 */
            }
            String contacted_person = DataModel_CLB_Master.getValueByVariable("CONTACTED_PERSON", i);
            String contacted_no = DataModel_CLB_Master.getValueByVariable("CONTACTED_NO", i);
            int col_no = DataModel_CLB_Master.getColFromVariable("UNABLE_TO_CONTACT");
            if (tblPieceClubbingMaster.getValueAt(i, col_no).equals(true)) {
                if (contacted_person.equals("") || contacted_no.equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contected person and number for unable to contact (CLUBBING NO : " + DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i) + ")");
                    return;
                }

            }
        }

        boolean DATA_SAVED = false;
        //SAVE - PROCESS

        for (int i = 0; i <= tblPieceClubbingMaster.getRowCount() - 1; i++) {

            String CLUBBING_NO = DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i);
            String contacted_person = DataModel_CLB_Master.getValueByVariable("CONTACTED_PERSON", i);
            String contacted_no = DataModel_CLB_Master.getValueByVariable("CONTACTED_NO", i);
            int col_no = DataModel_CLB_Master.getColFromVariable("UNABLE_TO_CONTACT");
            String Remark = DataModel_CLB_Master.getValueByVariable("REMARK", i);
            String NewExpectedDate = DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i);
            String Additional_Remark = "";  //DataModel_CLB_Master.getValueByVariable("ADDITIONAL_REMARK", i);
            String PreviousExpectedDate = DataModel_CLB_Master.getValueByVariable("EXPECTED_DISPATCH_DATE", i);

            //Unable to Contact
            if (!DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i).equals("") && tblPieceClubbingMaster.getValueAt(i, col_no).equals(true)) {
                int unable_to_contact = 0;
                data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL SET UNABLE_TO_CONTACT='1' WHERE DOC_NO='" + CLUBBING_NO + "' ");
                // AND DOC_MONTH='"+txtMonth.getText()+"' AND DOC_YEAR='"+txtYear.getText()+"'
                unable_to_contact = 1;
                data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL SET "
                        + " ADDITIONAL_REMARK='" + Additional_Remark + "',"
                        + "CONTACT_PERSON='" + contacted_person + "',CONTACTED_NO='" + contacted_no + "', "
                        + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                        + "MODE_OF_COMMUNICATION='',"
                        + "PARTY_JUSTIFICATION='',"
                        + "AREA_MANAGER_COMMENT='" + DataModel_CLB_Master.getValueByVariable("AREA_MANAGER_COMMENT", i) + "' "
                        + "WHERE DOC_NO='" + CLUBBING_NO + "' ");
                data.Execute("INSERT INTO PRODUCTION.FELT_SALES_EXPECTED_DISPATCH_DATE_HISTORY "
                        + "(PIECE_NO,PR_WH_EXP_DISPATCH_DATE,ENTRY_DATE,USER_ID,REMARK,UNABLE_TO_CONTACT,"
                        + "CONTACTED_PERSON,CONTACTED_NO,ADDITIONAL_REMARK,DATE_OF_COMMUNICATION,MODE_OF_COMMUNICATION,PARTY_JUSTIFICATION,AREA_MANAGER_COMMENT) "
                        + "VALUES "
                        + "('" + CLUBBING_NO + "','" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "','" + EITLERPGLOBAL.getCurrentDateTimeDB() + "','" + EITLERPGLOBAL.gUserID + "','" + Remark + "','" + unable_to_contact + "','" + contacted_person + "','" + contacted_no + "','" + Additional_Remark + "','" + EITLERPGLOBAL.getCurrentDateDB() + "','','','" + DataModel_CLB_Master.getValueByVariable("AREA_MANAGER_COMMENT", i) + "')");
                DATA_SAVED = true;
            }

            //Next Follow Up Date
            if (!DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i).equals("") && !DataModel_CLB_Master.getValueByVariable("NEW_EXPECTED_DISPATCH_DATE", i).equals("")) {
                if (!data.IsRecordExist("SELECT DOC_NO FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL where DOC_NO='" + CLUBBING_NO + "' AND EXPECTED_DISPATCH_DATE='" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "'")) {
                    //System.out.println("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_DETAIL SET EXPECTED_DISPATCH_DATE='"+EITLERPGLOBAL.formatDateDB(NewExpectedDate)+"',PREVIOUS_DISPATCH_DATE='"+EITLERPGLOBAL.formatDateDB(PreviousExpectedDate)+"',EXP_DISPATCH_HISTORY=CONCAT(coalesce(EXP_DISPATCH_HISTORY,''),',"+EITLERPGLOBAL.formatDateDB(NewExpectedDate)+"'),DELAY_REASON='"+Remark+"',ADDITIONAL_REMARK='"+Additional_Remark+"',CONTACT_PERSON='"+contacted_person+"',CONTACTED_NO='"+contacted_no+"' WHERE PIECE_NO='"+PieceNo+"'");
                    data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL SET "
                            + "FOLLOWUP_DATE='" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "',UNABLE_TO_CONTACT='',"
                            + "PREVIOUS_DISPATCH_DATE='" + EITLERPGLOBAL.formatDateDB(PreviousExpectedDate) + "',"
                            + "EXP_DISPATCH_HISTORY=CONCAT(coalesce(EXP_DISPATCH_HISTORY,''),'"
                            + "," + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "'),"
                            + "DELAY_REASON='" + Remark + "',ADDITIONAL_REMARK='" + Additional_Remark + "',"
                            + "CONTACT_PERSON='" + contacted_person + "',CONTACTED_NO='" + contacted_no + "', "
                            + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                            + "MODE_OF_COMMUNICATION='" + DataModel_CLB_Master.getValueByVariable("MODE_OF_COMMUNICATION", i) + "',"
                            + "PARTY_JUSTIFICATION='" + DataModel_CLB_Master.getValueByVariable("PARTY_JUSTIFICATION", i) + "',"
                            + "AREA_MANAGER_COMMENT='" + DataModel_CLB_Master.getValueByVariable("AREA_MANAGER_COMMENT", i) + "' "
                            + "WHERE DOC_NO='" + CLUBBING_NO + "' ");
                    data.Execute("INSERT INTO PRODUCTION.FELT_SALES_EXPECTED_DISPATCH_DATE_HISTORY "
                            + "(PIECE_NO,PR_WH_EXP_DISPATCH_DATE,ENTRY_DATE,USER_ID,REMARK,UNABLE_TO_CONTACT,"
                            + "CONTACTED_PERSON,CONTACTED_NO,ADDITIONAL_REMARK,DATE_OF_COMMUNICATION,MODE_OF_COMMUNICATION,PARTY_JUSTIFICATION,AREA_MANAGER_COMMENT) "
                            + "VALUES "
                            + "('" + CLUBBING_NO + "','" + EITLERPGLOBAL.formatDateDB(NewExpectedDate) + "','" + EITLERPGLOBAL.getCurrentDateTimeDB() + "','" + EITLERPGLOBAL.gUserID + "','" + Remark + "','','" + contacted_person + "','" + contacted_no + "','" + Additional_Remark + "','" + EITLERPGLOBAL.getCurrentDateDB() + "','" + DataModel_CLB_Master.getValueByVariable("MODE_OF_COMMUNICATION", i) + "','" + DataModel_CLB_Master.getValueByVariable("PARTY_JUSTIFICATION", i) + "','" + DataModel_CLB_Master.getValueByVariable("AREA_MANAGER_COMMENT", i) + "')");
                    //PIECE_NO,      PR_WH_EXP_DISPATCH_DATE,                       ENTRY_DATE,                                USER_ID,                   REMARK,       UNABLE_TO_CONTACT,      CONTACTED_PERSON,      CONTACTED_NO,      ADDITIONAL_REMARK
                    DATA_SAVED = true;
                }
            }
            
            if (!CLUBBING_NO.equals("") && !DataModel_CLB_Master.getValueByVariable("CLUBBING_NO", i).equals("") && !DataModel_CLB_Master.getValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", i).equals("")) {
                data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL SET "
                        + " PARTY_LIFTING_COMMITMENT_STATUS='" + DataModel_CLB_Master.getValueByVariable("PARTY_LIFTING_COMMITMENT_STATUS", i) + "',"
                        + "PARTY_LIFTING_COMMITMENT_DATE='" + EITLERPGLOBAL.formatDateDB(DataModel_CLB_Master.getValueByVariable("PARTY_LIFTING_COMMITMENT_DATE", i)) + "' "
                        + " WHERE DOC_NO='" + CLUBBING_NO + "' ");
                DATA_SAVED = true;
            }
            

            String selected_monthyear = DataModel_CLB_Master.getValueByVariable("EXPECTED_MONTH_OF_DISPATCH", i);
            //System.out.println("EXPECTED : SELECT EXPECTED_MONTH_OF_DISPATCH FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL WHERE DOC_NO='" + CLUBBING_NO + "'");
            String Cubbing_Curr_Sch_Month = data.getStringValueFromDB("SELECT EXPECTED_MONTH_OF_DISPATCH FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL WHERE DOC_NO='" + CLUBBING_NO + "'");

            if (!Cubbing_Curr_Sch_Month.equals(selected_monthyear)) {

                String Expected_Month_of_Dispatch_Date = EITLERPGLOBAL.formatDateDB(get1stDate(Cubbing_Curr_Sch_Month));
                String selected_monthyear_Date = EITLERPGLOBAL.formatDateDB(get1stDate(selected_monthyear));

//                    int date_diff = data.getIntValueFromDB("SELECT DATEDIFF('" + selected_monthyear_Date + "','" + Expected_Month_of_Dispatch_Date + "')");
//                    if (date_diff < 0) {
//                        data.Execute("UPDATE PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL SET EXPECTED_MONTH_OF_DISPATCH='"+selected_monthyear+"' WHERE DOC_NO='" + CLUBBING_NO + "'");
//                        DATA_SAVED = true;
//                    }
                if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_RESCHEDULE_HEADER WHERE CLUBBING_NO='" + CLUBBING_NO + "' AND APPROVED=0 AND CANCELED=0")) {
                    SelectFirstFree aList = new SelectFirstFree();
                    aList.ModuleID = 874;
                    aList.FirstFreeNo = 386;
                    String Doc_No = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 874, aList.FirstFreeNo, true);

                    String PARTY_CODE = DataModel_CLB_Master.getValueByVariable("PARTY_CODE", i);
                    String PARTY_NAME = DataModel_CLB_Master.getValueByVariable("PARTY_NAME", i);

                    try {
                        data.Execute("INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA"
                                + " (MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) "
                                + " VALUES "
                                + " ('874', '" + Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '28', 'W', 'W', '', '1', '28', '', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00')");
                        data.Execute("INSERT INTO PRODUCTION.FELT_SALES_PIECE_CLUBBING_RESCHEDULE_HEADER "
                                + "(DOC_NO, DOC_DATE, CLUBBING_NO, PARTY_CODE, PARTY_NAME, CURRENT_SCH_MONTH, REMARK, HIERARCHY_ID, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, REJECTED, REJECTED_BY, REJECTED_DATE, CANCELED, CHANGED, CHANGED_DATE, REJECTED_REMARKS) "
                                + " VALUES "
                                + "('" + Doc_No + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '" + CLUBBING_NO + "', '" + PARTY_CODE + "', '" + PARTY_NAME + "', '" + selected_monthyear + "', '', 4854, '" + EITLERPGLOBAL.gUserID + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', 0, '0000-00-00', 0, 0, '0000-00-00', 0, 0, '0000-00-00', 0, 0, '0000-00-00', '') ");

                        ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_CLUBBING_DETAIL WHERE PC_DOC_NO='" + CLUBBING_NO + "'");

                        rsData.first();
                        int srno = 1;
                        while (!rsData.isAfterLast()) {
                            ResultSet rsRegister = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + rsData.getString("PIECE_NO") + "'");
                            rsRegister.first();
                            System.out.println("INSERT INTO PRODUCTION.FELT_SALES_PIECE_CLUBBING_RESCHEDULE_DETAIL "
                                    + "(DOC_NO, SR_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, MACHINE_NO, POSITION, UPN, REQUESTED_MONTH, OC_MONTH, CURR_SCH_MONTH, PRODUCT_CODE, BILL_PRODUCT_CODE, PR_GROUP, STYLE, BILL_STYLE, LENGTH, ACTUAL_LENGTH, BILL_LENGTH, WIDTH, ACTUAL_WIDTH, SQMTR, BILL_SQMTR, GSM, BILL_GSM, TENDER_GSM, TH_WEIGHT, TENDER_WEIGHT, ACTUAL_WEIGHT, BILL_WEIGHT, WEAVING_WEIGHT, NEEDLING_WEIGHT, SEAM_WEIGHT, SPLICE_WEIGHT, WEAVING_DATE, MANDING_DATE, NEEDLING_DATE, INCHARGE, PIECE_STAGE, PIECE_OBSOLETE, OBSOLETE_DATE, REQUESTED_MONTH_DATE, OC_MONTH_DATE, CURR_SCH_MONTH_DATE, CUR_PIECE_STAGE, INVOICE_NO, INVOICE_DATE, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, FINISHING_DATE, PIECE_ADD_IND) "
                                    + " VALUES "
                                    + "('" + Doc_No + "', '" + srno + "', '" + rsData.getString("PIECE_NO") + "', '" + rsRegister.getString("PR_PARTY_CODE") + "','" + PARTY_NAME + "', '" + rsRegister.getString("PR_MACHINE_NO") + "', '" + rsRegister.getString("PR_POSITION_NO") + "', '" + rsRegister.getString("PR_UPN") + "', '" + rsRegister.getString("PR_REQUESTED_MONTH") + "', '" + rsRegister.getString("PR_OC_MONTHYEAR") + "', '" + rsRegister.getString("PR_CURRENT_SCH_MONTH") + "', '" + rsRegister.getString("PR_PRODUCT_CODE") + "', '" + rsRegister.getString("PR_BILL_PRODUCT_CODE") + "', '" + rsRegister.getString("PR_GROUP") + "', '" + rsRegister.getString("PR_STYLE") + "', '" + rsRegister.getString("PR_BILL_STYLE") + "', '" + rsRegister.getString("PR_LENGTH") + "', '" + rsRegister.getString("PR_ACTUAL_LENGTH") + "', '" + rsRegister.getString("PR_BILL_LENGTH") + "', '" + rsRegister.getString("PR_WIDTH") + "', '" + rsRegister.getString("PR_ACTUAL_WIDTH") + "', '" + rsRegister.getString("PR_SQMTR") + "', '" + rsRegister.getString("PR_BILL_SQMTR") + "', '" + rsRegister.getString("PR_GSM") + "', '" + rsRegister.getString("PR_BILL_GSM") + "','" + rsRegister.getString("PR_TENDER_WEIGHT") + "', '" + rsRegister.getString("PR_THORITICAL_WEIGHT") + "', '" + rsRegister.getString("PR_TENDER_WEIGHT") + "', '" + rsRegister.getString("PR_ACTUAL_WEIGHT") + "', '" + rsRegister.getString("PR_BILL_WEIGHT") + "', '" + rsRegister.getString("PR_WEAVING_WEIGHT") + "', '" + rsRegister.getString("PR_NEEDLING_WEIGHT") + "', '" + rsRegister.getString("PR_SEAM_WEIGHT") + "', '" + rsRegister.getString("PR_SPLICE_WIEGHT") + "', '" + rsRegister.getString("PR_WVG_DATE") + "', '" + rsRegister.getString("PR_MND_DATE") + "', '" + rsRegister.getString("PR_NDL_DATE") + "', '" + rsRegister.getString("PR_INCHARGE") + "', '" + rsRegister.getString("PR_PIECE_STAGE") + "', '" + rsRegister.getString("PR_DELINK") + "', '" + rsRegister.getString("PR_OBSOLETE_DATE") + "', '" + rsRegister.getString("PR_REQ_MTH_LAST_DDMMYY") + "',"
                                    + ""
                                    + ""
                                    + " '" + rsRegister.getString("PR_OC_LAST_DDMMYY") + "', '" + rsRegister.getString("PR_CURRENT_SCH_LAST_DDMMYY") + "', '" + rsRegister.getString("PR_PIECE_STAGE") + "', '" + rsRegister.getString("PR_INVOICE_NO") + "', '" + rsRegister.getString("PR_INVOICE_DATE") + "', '0000-00-00', '', '', '', '', '" + rsRegister.getString("PR_FNSG_DATE") + "', '')");
                            data.Execute("INSERT INTO PRODUCTION.FELT_SALES_PIECE_CLUBBING_RESCHEDULE_DETAIL "
                                    + "(DOC_NO, SR_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, MACHINE_NO, POSITION, UPN, REQUESTED_MONTH, OC_MONTH, CURR_SCH_MONTH, PRODUCT_CODE, BILL_PRODUCT_CODE, PR_GROUP, STYLE, BILL_STYLE, LENGTH, ACTUAL_LENGTH, BILL_LENGTH, WIDTH, ACTUAL_WIDTH, SQMTR, BILL_SQMTR, GSM, BILL_GSM, TENDER_GSM, TH_WEIGHT, TENDER_WEIGHT, ACTUAL_WEIGHT, BILL_WEIGHT, WEAVING_WEIGHT, NEEDLING_WEIGHT, SEAM_WEIGHT, SPLICE_WEIGHT, WEAVING_DATE, MANDING_DATE, NEEDLING_DATE, INCHARGE, PIECE_STAGE, PIECE_OBSOLETE, OBSOLETE_DATE, REQUESTED_MONTH_DATE, OC_MONTH_DATE, CURR_SCH_MONTH_DATE, CUR_PIECE_STAGE, INVOICE_NO, INVOICE_DATE, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, FINISHING_DATE, PIECE_ADD_IND) "
                                    + " VALUES "
                                    + "('" + Doc_No + "', '" + srno + "', '" + rsData.getString("PIECE_NO") + "', '" + rsRegister.getString("PR_PARTY_CODE") + "','" + PARTY_NAME + "', '" + rsRegister.getString("PR_MACHINE_NO") + "', '" + rsRegister.getString("PR_POSITION_NO") + "', '" + rsRegister.getString("PR_UPN") + "', '" + rsRegister.getString("PR_REQUESTED_MONTH") + "', '" + rsRegister.getString("PR_OC_MONTHYEAR") + "', '" + rsRegister.getString("PR_CURRENT_SCH_MONTH") + "', '" + rsRegister.getString("PR_PRODUCT_CODE") + "', '" + rsRegister.getString("PR_BILL_PRODUCT_CODE") + "', '" + rsRegister.getString("PR_GROUP") + "', '" + rsRegister.getString("PR_STYLE") + "', '" + rsRegister.getString("PR_BILL_STYLE") + "', '" + rsRegister.getString("PR_LENGTH") + "', '" + rsRegister.getString("PR_ACTUAL_LENGTH") + "', '" + rsRegister.getString("PR_BILL_LENGTH") + "', '" + rsRegister.getString("PR_WIDTH") + "', '" + rsRegister.getString("PR_ACTUAL_WIDTH") + "', '" + rsRegister.getString("PR_SQMTR") + "', '" + rsRegister.getString("PR_BILL_SQMTR") + "', '" + rsRegister.getString("PR_GSM") + "', '" + rsRegister.getString("PR_BILL_GSM") + "','" + rsRegister.getString("PR_TENDER_WEIGHT") + "', '" + rsRegister.getString("PR_THORITICAL_WEIGHT") + "', '" + rsRegister.getString("PR_TENDER_WEIGHT") + "', '" + rsRegister.getString("PR_ACTUAL_WEIGHT") + "', '" + rsRegister.getString("PR_BILL_WEIGHT") + "', '" + rsRegister.getString("PR_WEAVING_WEIGHT") + "', '" + rsRegister.getString("PR_NEEDLING_WEIGHT") + "', '" + rsRegister.getString("PR_SEAM_WEIGHT") + "', '" + rsRegister.getString("PR_SPLICE_WIEGHT") + "', '" + rsRegister.getString("PR_WVG_DATE") + "', '" + rsRegister.getString("PR_MND_DATE") + "', '" + rsRegister.getString("PR_NDL_DATE") + "', '" + rsRegister.getString("PR_INCHARGE") + "', '" + rsRegister.getString("PR_PIECE_STAGE") + "', '" + rsRegister.getString("PR_DELINK") + "', '" + rsRegister.getString("PR_OBSOLETE_DATE") + "', '" + rsRegister.getString("PR_REQ_MTH_LAST_DDMMYY") + "',"
                                    + ""
                                    + ""
                                    + " '" + rsRegister.getString("PR_OC_LAST_DDMMYY") + "', '" + rsRegister.getString("PR_CURRENT_SCH_LAST_DDMMYY") + "', '" + rsRegister.getString("PR_PIECE_STAGE") + "', '" + rsRegister.getString("PR_INVOICE_NO") + "', '" + rsRegister.getString("PR_INVOICE_DATE") + "', '0000-00-00', '', '', '', '', '" + rsRegister.getString("PR_FNSG_DATE") + "', '')");
                            srno++;
                            rsData.next();
                        }

                        DATA_SAVED = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (DATA_SAVED) {
            JOptionPane.showMessageDialog(this, "Data updated successfully.");
            DisplayData_Master(EITLERPGLOBAL.formatDateDB(txtFollowupDate.getText()));
        }
    }//GEN-LAST:event_btnClubbingFollowupSaveActionPerformed

    private void tblPieceClubbingMasterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPieceClubbingMasterKeyPressed
        // TODO add your handling code here:

        //DataModel.SetVariable(11, "CONTACTED_PERSON");
        //    DataModel.SetVariable(12, "CONTACTED_NO");
        if (tblPieceClubbingMaster.getSelectedColumn() == 10 && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER  where PARA_ID = 'SALES_FOLLOWUP'";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    //PARTY_CODE.setText(aList.ReturnVal);
                    DataModel_CLB_Master.setValueByVariable("REMARK", aList.ReturnVal, tblPieceClubbingMaster.getSelectedRow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }

        //DataModel.SetVariable(11, "CONTACTED_PERSON");
        //    DataModel.SetVariable(12, "CONTACTED_NO");
        if (tblPieceClubbingMaster.getSelectedColumn() == 12 && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT distinct CONTACT_PERSON as CONTACT_PERSON,CONTACTED_NO FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY where PARTY_CODE=" + DataModel_CLB_Master.getValueByVariable("PARTY_CODE", tblPieceClubbingMaster.getSelectedRow()) + ""
                        + "  UNION ALL SELECT '','' FROM DUAL"
                        + "";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;
                aList.SecondCol = 2;
                if (aList.ShowLOV()) {
                    String value = "";
                    String value_contact_no = "";
                    if (aList.ReturnVal.equals("")) {
                        value = JOptionPane.showInputDialog(this, "Please enter Contact Person : ");
                    } else {
                        value = aList.ReturnVal;
                        value_contact_no = aList.SecondVal;
                    }
                    DataModel_CLB_Master.setValueByVariable("CONTACTED_PERSON", value, tblPieceClubbingMaster.getSelectedRow());
                    DataModel_CLB_Master.setValueByVariable("CONTACTED_NO", value_contact_no, tblPieceClubbingMaster.getSelectedRow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }
        if (tblPieceClubbingMaster.getSelectedColumn() == 13 && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT distinct CONTACTED_NO FROM PRODUCTION.FELT_SALES_FOLLOWUP_CLUBBING_DETAIL_HISTORY where PARTY_CODE=" + DataModel_CLB_Master.getValueByVariable("PARTY_CODE", tblPieceClubbingMaster.getSelectedRow()) + " "
                        + "  UNION ALL SELECT '' FROM DUAL";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    //PARTY_CODE.setText(aList.ReturnVal);
                    String value = "";
                    if (aList.ReturnVal.equals("")) {
                        value = JOptionPane.showInputDialog(this, "Please enter Contact Number : ");
                    } else {
                        value = aList.ReturnVal;
                    }
                    DataModel_CLB_Master.setValueByVariable("CONTACTED_NO", value, tblPieceClubbingMaster.getSelectedRow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }
    }//GEN-LAST:event_tblPieceClubbingMasterKeyPressed

    private void btnClubbingAmendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClubbingAmendActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (txtApprovalStatus.getText().equals("Approved")) {
            AppletFrame aFrame = new AppletFrame("Sales Piece Clubbing Amend");
            aFrame.startAppletEx("EITLERP.FeltSales.SalesPieceClubbingAmend.frmSalesPieceClubbingAmend", "Sales Piece Clubbing Amend");
            frmSalesPieceClubbingAmend ObjItem = (frmSalesPieceClubbingAmend) aFrame.ObjApplet;
            // ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, PARTY_CODE.getText());

            ObjItem.requestFocus();
            ObjItem.Add();
            ObjItem.setDefaultData(txtSelectedClubbingNo.getText(), txtSelectedClubbingPartyCode.getText());

            //ObjItem.machine_lostfocus();
        } else {
            JOptionPane.showMessageDialog(this, "This document is not approved.");
            return;
        }
    }//GEN-LAST:event_btnClubbingAmendActionPerformed

    private void btnShowOrderStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowOrderStatusActionPerformed
        // TODO add your handling code here:
        if (lblSelectedParty.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please select any Piece");
        } else {
            String PartyCode = lblSelectedParty.getText();

            AppletFrame aFrame = new AppletFrame("Party Order Status");
            aFrame.startAppletEx("EITLERP.FeltSales.PieceRegister.FrmPOPendingOrder", "Party Order Status");
            FrmPOPendingOrder ObjItem = (FrmPOPendingOrder) aFrame.ObjApplet;

            ObjItem.requestFocus();
            ObjItem.setData(PartyCode);
        }
    }//GEN-LAST:event_btnShowOrderStatusActionPerformed

    private void Table_OrderFUPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_OrderFUPMouseClicked
        // TODO add your handling code here:
//        if (Table_OrderFUP.getSelectedColumn() == 1 && Table_OrderFUP.getValueAt(Table_OrderFUP.getSelectedRow(), 1).equals(false)) {
//            //JOptionPane.showMessageDialog(null, "Only Sales can confirm");
//            DataModel_OrderFUP.setValueByVariable("SELECT_IND", true, Table_OrderFUP.getSelectedRow());
//            return;
//        } else if (Table_OrderFUP.getSelectedColumn() == 1 && Table_OrderFUP.getValueAt(Table_OrderFUP.getSelectedRow(), 1).equals(true)) {
//            //JOptionPane.showMessageDialog(null, "Only Sales can confirm");
//            DataModel_OrderFUP.setValueByVariable("SELECT_IND", false, Table_OrderFUP.getSelectedRow());
//            return;
//        }

//        if (Table_OrderFUP.getSelectedColumn() == 1 && Table_OrderFUP.getValueAt(Table_OrderFUP.getSelectedRow(), 1).equals(false)) {
//            //JOptionPane.showMessageDialog(null, "Only Sales can confirm");
//            DataModel_OrderFUP.setValueByVariable("SELECT_IND", true, Table_OrderFUP.getSelectedRow());
//            return;
//        } else {
//            //JOptionPane.showMessageDialog(null, "Only Sales can confirm");
//            DataModel_OrderFUP.setValueByVariable("SELECT_IND", false, Table_OrderFUP.getSelectedRow());
//            return;
//        }
        if (Table_OrderFUP.getSelectedColumn() == 1 && !DataModel_OrderFUP.getValueByVariable("ORDER_STATUS", Table_OrderFUP.getSelectedRow()).equals("")) {
            DataModel_OrderFUP.setValueByVariable("SELECT_IND", false, Table_OrderFUP.getSelectedRow());
        }
        try {

            String dUPN = DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow());
            lblOFUP_UPN.setText(dUPN);
            lblOFUP_PartyCode.setText(DataModel_OrderFUP.getValueByVariable("PARTY_CODE", Table_OrderFUP.getSelectedRow()));
            lblOFUP_PartyName.setText(DataModel_OrderFUP.getValueByVariable("PARTY_NAME", Table_OrderFUP.getSelectedRow()));

            ResultSet rsData1 = data.getResult("SELECT MM_UPN_NO,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE,MM_FELT_GSM,MM_FELT_LENGTH+MM_FABRIC_LENGTH AS MM_FELT_LENGTH,MM_FELT_WIDTH+MM_FABRIC_WIDTH AS MM_FELT_WIDTH FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_UPN_NO='" + DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow()) + "'");
            txtOFUP_Length.setText(rsData1.getString("MM_FELT_LENGTH"));
            txtOFUP_Width.setText(rsData1.getString("MM_FELT_WIDTH"));
            txtOFUP_GSM.setText(rsData1.getString("MM_FELT_GSM"));
            txtOFUP_ProductCode.setText(rsData1.getString("MM_ITEM_CODE"));
            txtOFUP_Machine.setText(rsData1.getString("MM_MACHINE_NO"));
            txtOFUP_Position.setText(rsData1.getString("MM_MACHINE_POSITION") + " - " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=" + rsData1.getString("MM_MACHINE_POSITION") + " "));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Table_OrderFUP.getSelectedColumn() == 1) {
            getTickCountEffects();
        }

        if (Table_OrderFUP.getSelectedColumn() == DataModel_OrderFUP.getColFromVariable("UPN") && evt.getClickCount() == 2) {
//            JOptionPane.showMessageDialog(null, "Only Sales can confirm");
            String pUPN = DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow());
            try {
                OrderFUP_LOV aList = new OrderFUP_LOV();

                aList.UPN = pUPN;
                aList.dSQL = "SELECT COALESCE(PR_PIECE_STAGE,'') AS 'Piece Stage',COALESCE(PR_PIECE_NO,'') AS 'Piece No',"
                        + "CASE WHEN COALESCE(PR_FNSG_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_FNSG_DATE,'0000-00-00') END AS 'Finishing Date',"
                        + "COALESCE(PR_INVOICE_NO,'') AS 'Inv No',"
                        + "CASE WHEN COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_INVOICE_DATE,'0000-00-00') END AS 'Inv Date' "//PR_UPN,
                        + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE IN ('INVOICED','EXP-INVOICE') "
                        + "AND PR_UPN = '" + pUPN + "' AND PR_INVOICE_DATE >= '2022-04-01' AND PR_INVOICE_DATE <= '2023-03-31' "
                        + "ORDER BY PR_INVOICE_DATE DESC,PR_INVOICE_NO ";
                aList.sSQL = "SELECT COALESCE(PR_PIECE_STAGE,'') AS 'Piece Stage',COALESCE(PR_PIECE_NO,'') AS 'Piece No',"
                        + "CASE WHEN COALESCE(PR_FNSG_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_FNSG_DATE,'0000-00-00') END AS 'Finishing Date',"
                        + "COALESCE(PR_OC_MONTHYEAR,'') AS 'OC Month',COALESCE(PR_CURRENT_SCH_MONTH,'') AS 'Curr.Sch.Month' "//PR_UPN,
                        + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE IN ('IN STOCK','BSR') "
                        + "AND PR_UPN = '" + pUPN + "' ";
                aList.wSQL = "SELECT COALESCE(PR_PRODUCT_CODE,'') AS 'Prod Code',COALESCE(PR_PIECE_STAGE,'') AS 'Piece Stage',COALESCE(PR_PIECE_NO,'') AS 'Piece No',"
                        + "CASE WHEN COALESCE(PR_WARP_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_WARP_DATE,'0000-00-00') END AS 'Warp Dt',"
                        + "CASE WHEN COALESCE(PR_WVG_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_WVG_DATE,'0000-00-00') END AS 'Wvg Dt',"
                        + "CASE WHEN COALESCE(PR_MND_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_MND_DATE,'0000-00-00') END AS 'Mnd Dt',"
                        + "CASE WHEN COALESCE(PR_NDL_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_NDL_DATE,'0000-00-00') END AS 'Ndl Dt', "//PR_UPN,
                        + "CASE WHEN COALESCE(PR_SEAM_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_SEAM_DATE,'0000-00-00') END AS 'Seam Dt',"
                        + "CASE WHEN COALESCE(PR_SDF_SPIRALED_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_SDF_SPIRALED_DATE,'0000-00-00') END AS 'Sprl Dt',"
                        + "CASE WHEN COALESCE(PR_SDF_ASSEMBLED_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_SDF_ASSEMBLED_DATE,'0000-00-00') END AS 'Asmb Dt',"
                        + "CASE WHEN COALESCE(PR_FNSG_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE COALESCE(PR_FNSG_DATE,'0000-00-00') END AS 'Finishing Date',"
                        + "COALESCE(PR_OC_MONTHYEAR,'') AS 'OC Month',COALESCE(PR_CURRENT_SCH_MONTH,'') AS 'Cur.Sch.Month',COALESCE(PR_REQUESTED_MONTH,'') AS 'Req Month' "
                        + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE  PR_PIECE_STAGE  IN ('NEEDLING','MENDING','FINISHING','WEAVING','SEAMING','PLANNING','BOOKING','DIVERTED_FNSG_STOCK','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') "
                        + "AND PR_UPN = '" + pUPN + "' "
                        + "ORDER BY PR_SEAM_DATE DESC,PR_NDL_DATE DESC,PR_MND_DATE DESC,PR_WVG_DATE DESC,PR_WARP_DATE DESC,PR_OC_LAST_DDMMYY DESC,PR_REQ_MTH_LAST_DDMMYY DESC ";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.SecondCol = 2;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    if (aList.ReturnVal.equals("")) {

                    } else {

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }
        }
    }//GEN-LAST:event_Table_OrderFUPMouseClicked

    private void Table_OrderFUPFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Table_OrderFUPFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_Table_OrderFUPFocusGained

    private void Table_OrderFUPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Table_OrderFUPFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_Table_OrderFUPFocusLost

    private void Table_OrderFUPCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_Table_OrderFUPCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_Table_OrderFUPCaretPositionChanged

    private void Table_OrderFUPAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_Table_OrderFUPAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_Table_OrderFUPAncestorMoved

    private void Table_OrderFUPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_OrderFUPKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 32 && Table_OrderFUP.getSelectedColumn() == 1) {
            if (Table_OrderFUP.getValueAt(Table_OrderFUP.getSelectedRow(), 1).equals(true)) {
                //JOptionPane.showMessageDialog(null, "Only Sales can confirm");
                DataModel_OrderFUP.setValueByVariable("SELECT_IND", false, Table_OrderFUP.getSelectedRow());
            }
        }

//        if (Table_OrderFUP.getSelectedColumn() == 17 && evt.getKeyCode() == 112) {
//            try {
//                LOV aList = new LOV();
//
//                aList.SQL = "SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER  where PARA_ID = 'SALES_FOLLOWUP'";
//                aList.ReturnCol = 1;
//                aList.ShowReturnCol = true;
//                aList.DefaultSearchOn = 2;
//                aList.UseSpecifiedConn = true;
//                aList.dbURL = EITLERPGLOBAL.DatabaseURL;
//
//                if (aList.ShowLOV()) {
//                    //PARTY_CODE.setText(aList.ReturnVal);
//                    DataModel_OrderFUP.setValueByVariable("DELAY_REASON", aList.ReturnVal, Table_OrderFUP.getSelectedRow());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Error = " + e.getMessage());
//            }
//
//        }
        //DataModel_OrderFUP.SetVariable(19, "CONTACTED_PERSON");
        //    DataModel_OrderFUP.SetVariable(20, "CONTACTED_NO");
        if (Table_OrderFUP.getSelectedColumn() == DataModel_OrderFUP.getColFromVariable("CONTACTED_PERSON") && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT distinct CONTACT_PERSON as CONTACT_PERSON, CONTACTED_NO FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL_HISTORY where PARTY_CODE=" + DataModel_OrderFUP.getValueByVariable("PARTY_CODE", Table_OrderFUP.getSelectedRow()) + ""
                        + "  UNION ALL SELECT '','' FROM DUAL"
                        + "";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.SecondCol = 2;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    String value = "";
                    String value_contactNo = "";
                    if (aList.ReturnVal.equals("")) {
                        value = JOptionPane.showInputDialog(this, "Please enter Contact Person : ");
                    } else {
                        value = aList.ReturnVal;
                        value_contactNo = aList.SecondVal;
                    }
                    DataModel_OrderFUP.setValueByVariable("CONTACTED_PERSON", value, Table_OrderFUP.getSelectedRow());
                    DataModel_OrderFUP.setValueByVariable("CONTACTED_NO", value_contactNo, Table_OrderFUP.getSelectedRow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }
        if (Table_OrderFUP.getSelectedColumn() == DataModel_OrderFUP.getColFromVariable("CONTACTED_NO") && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT distinct CONTACTED_NO FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL_HISTORY where PARTY_CODE=" + DataModel_OrderFUP.getValueByVariable("PARTY_CODE", Table_OrderFUP.getSelectedRow()) + " "
                        + "  UNION ALL SELECT '' FROM DUAL";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    //PARTY_CODE.setText(aList.ReturnVal);
                    String value = "";
                    if (aList.ReturnVal.equals("")) {
                        value = JOptionPane.showInputDialog(this, "Please enter Contact Number : ");
                    } else {
                        value = aList.ReturnVal;
                    }
                    DataModel_OrderFUP.setValueByVariable("CONTACTED_NO", value, Table_OrderFUP.getSelectedRow());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }

        if (Table_OrderFUP.getSelectedColumn() == DataModel_OrderFUP.getColFromVariable("SELECTED_DIVERSION_PIECE") && evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PIECE_NO AS 'Piece No',PR_PIECE_STAGE AS 'Piece Stage', COALESCE(PR_DELINK,'') AS 'Obsolete Status' "
                        + "FROM PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST,PRODUCTION.FELT_SALES_PIECE_REGISTER "
                        + "WHERE PIECE_NO=PR_PIECE_NO AND COALESCE(DIVERSION_NO,'')='' "
                        + "AND UPN='" + DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow()) + "' "
//                        + "UNION ALL "
//                        + "SELECT PR_PIECE_NO,PR_PIECE_STAGE,COALESCE(PR_DELINK,'') "
//                        + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_DIVERSION_FLAG = 'READY' "
//                        + "AND PR_PIECE_STAGE IN ('IN STOCK','NEEDLING','MENDING','FINISHING','SEAMING','WEAVING','MOVE_TO_SAMPLE') "
//                        + "AND PR_PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST "
//                        + "WHERE COALESCE(DIVERSION_NO,'')='' AND UPN='" + DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow()) + "' ) "
                        + "";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    //PARTY_CODE.setText(aList.ReturnVal);
                    String value = "";
                    value = aList.ReturnVal;
                    DataModel_OrderFUP.setValueByVariable("SELECTED_DIVERSION_PIECE", value, Table_OrderFUP.getSelectedRow());
                }
                getTickCountEffects();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }

        }

        if (Table_OrderFUP.getSelectedColumn() == DataModel_OrderFUP.getColFromVariable("FOLLOW_UP_DATE") && evt.getKeyCode() == 112) {
            Point p = jScrollPane7.getLocation();
            //Rectangle r = Table.getCellRect(Table.getSelectedRow(), Table.getSelectedColumn(), true);
            p.x = p.x + 240;
            p.y = p.y + 80;
//                System.out.println(Table.getSelectedRow()+" x:"+r.getX()+ " Y:"+r.getY());
            new DatePickerTable(null, Table_OrderFUP.getSelectedRow(), DataModel_OrderFUP.getColFromVariable("FOLLOW_UP_DATE"), Table_OrderFUP, p.x, p.y,
                    data.getStringValueFromDB("SELECT DATE_ADD(CURDATE(),INTERVAL 0 DAY) FROM DUAL"),
                    data.getStringValueFromDB("SELECT CASE WHEN DAY(CURDATE())>=11 THEN CONCAT(YEAR(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)),'-',MONTH(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)),'-10') ELSE CONCAT(YEAR(DATE_ADD(CURDATE(),INTERVAL 0 MONTH)),'-',MONTH(DATE_ADD(CURDATE(),INTERVAL 0 MONTH)),'-10') END FROM DUAL"));
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(data.getStringValueFromDB("SELECT DATE_ADD(CURDATE(),INTERVAL 0 DAY) FROM DUAL"));
                Date date3 = sdf.parse(data.getStringValueFromDB("SELECT CASE WHEN DAY(CURDATE())>=11 THEN CONCAT(YEAR(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)),'-',MONTH(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)),'-10') ELSE CONCAT(YEAR(DATE_ADD(CURDATE(),INTERVAL 0 MONTH)),'-',MONTH(DATE_ADD(CURDATE(),INTERVAL 0 MONTH)),'-10') END FROM DUAL"));
                Date date2 = sdf.parse(EITLERPGLOBAL.formatDateDB(Table_OrderFUP.getValueAt(Table_OrderFUP.getSelectedRow(), DataModel_OrderFUP.getColFromVariable("FOLLOW_UP_DATE")).toString()));
                if (date2.compareTo(date1) >= 0 && date3.compareTo(date2) >= 0) {
                } else {
                    Table_OrderFUP.setValueAt("", Table_OrderFUP.getSelectedRow(), DataModel_OrderFUP.getColFromVariable("FOLLOW_UP_DATE"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_Table_OrderFUPKeyPressed

    private void Table_OrderFUPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_OrderFUPKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 40 || evt.getKeyCode() == 38) {
            try {

                String dUPN = DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow());
                lblOFUP_UPN.setText(dUPN);
                lblOFUP_PartyCode.setText(DataModel_OrderFUP.getValueByVariable("PARTY_CODE", Table_OrderFUP.getSelectedRow()));
                lblOFUP_PartyName.setText(DataModel_OrderFUP.getValueByVariable("PARTY_NAME", Table_OrderFUP.getSelectedRow()));

                ResultSet rsData1 = data.getResult("SELECT MM_UPN_NO,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE,MM_FELT_GSM,MM_FELT_LENGTH+MM_FABRIC_LENGTH AS MM_FELT_LENGTH,MM_FELT_WIDTH+MM_FABRIC_WIDTH AS MM_FELT_WIDTH FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_UPN_NO='" + DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow()) + "'");
                txtOFUP_Length.setText(rsData1.getString("MM_FELT_LENGTH"));
                txtOFUP_Width.setText(rsData1.getString("MM_FELT_WIDTH"));
                txtOFUP_GSM.setText(rsData1.getString("MM_FELT_GSM"));
                txtOFUP_ProductCode.setText(rsData1.getString("MM_ITEM_CODE"));
                txtOFUP_Machine.setText(rsData1.getString("MM_MACHINE_NO"));
                txtOFUP_Position.setText(rsData1.getString("MM_MACHINE_POSITION") + " - " + data.getStringValueFromDB("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO=" + rsData1.getString("MM_MACHINE_POSITION") + " "));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_Table_OrderFUPKeyReleased

    private void btnSave_OrderFUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_OrderFUPActionPerformed
        // TODO add your handling code here:

        boolean DATA_SAVED = false;

        for (int i = 0; i <= Table_OrderFUP.getRowCount() - 1; i++) {

            int col_select = DataModel_OrderFUP.getColFromVariable("SELECT_IND");
            int col_unable_to_contact = DataModel_OrderFUP.getColFromVariable("UNABLE_TO_CONTACT");
            String NewFUPDate = DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i);
            try {
                if (!"".equals(NewFUPDate)) {
//                    int Month = EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(NewFUPDate));
//                    int Year = EITLERPGLOBAL.getYear(EITLERPGLOBAL.formatDateDB(NewFUPDate));

//                    if (Month != EITLERPGLOBAL.getCurrentMonth() || Year != EITLERPGLOBAL.getCurrentYear()) {
//                        JOptionPane.showMessageDialog(this, "Follow Up Date should be current Month for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
//                        return;
//                    }
                    SimpleDateFormat nFUPdt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = nFUPdt.parse(data.getStringValueFromDB("SELECT DATE_ADD(CURDATE(),INTERVAL 0 DAY) FROM DUAL"));
                    Date date3 = nFUPdt.parse(data.getStringValueFromDB("SELECT CASE WHEN DAY(CURDATE())>=11 THEN CONCAT(YEAR(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)),'-',MONTH(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)),'-10') ELSE CONCAT(YEAR(DATE_ADD(CURDATE(),INTERVAL 0 MONTH)),'-',MONTH(DATE_ADD(CURDATE(),INTERVAL 0 MONTH)),'-10') END FROM DUAL"));
                    Date date2 = nFUPdt.parse(EITLERPGLOBAL.formatDateDB(Table_OrderFUP.getValueAt(i, DataModel_OrderFUP.getColFromVariable("FOLLOW_UP_DATE")).toString()));
                    if (date2.compareTo(date1) >= 0 && date3.compareTo(date2) >= 0) {
                    } else {
                        JOptionPane.showMessageDialog(this, "Follow Up Date should be current Month for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                        return;
                    }
                }
            } catch (Exception e) {

            }

            if (!DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i).equals("") && Table_OrderFUP.getValueAt(i, col_select).equals(true)) {
                if (Table_OrderFUP.getValueAt(i, col_unable_to_contact).equals(false) && DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please do any action for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
            }
            if (Table_OrderFUP.getValueAt(i, col_unable_to_contact).equals(true)) {

                if (!DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "UNABLE TO CONTACT and FOLLOW UP DATE, Both are not allowed at same time, For UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }

                if (DataModel_OrderFUP.getValueByVariable("CONTACTED_PERSON", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted person for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
                if (DataModel_OrderFUP.getValueByVariable("CONTACTED_NO", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted number for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
                if (!DataModel_OrderFUP.getValueByVariable("MODE_OF_COMMUNICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Mode Of Comminication should be blank for Unable to Contact for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
                if (!DataModel_OrderFUP.getValueByVariable("PARTY_JUSTIFICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Party Justification should be blank for Unable to Contact for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
//                if (!DataModel_OrderFUP.getValueByVariable("AREA_MANAGER_COMMENT", i).equals("")) {
//                    JOptionPane.showMessageDialog(this, "Area Manager Comment should be blank for Unable to Contact for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
//                    return;
//                }
            }
            if (!DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i).equals("") && !DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i).equals("")) {
                System.out.println("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i)) + "','" + EITLERPGLOBAL.getCurrentDateDB() + "')");
                int date_diff = data.getIntValueFromDB("SELECT DATEDIFF('" + EITLERPGLOBAL.formatDateDB(DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i)) + "','" + EITLERPGLOBAL.getCurrentDateDB() + "')");
                if (date_diff <= 0) {
                    JOptionPane.showMessageDialog(this, "FollowUp Date " + DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i) + " is not valid for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
            }
            if (!DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i).equals("") && !DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i).equals("")) {
                if (DataModel_OrderFUP.getValueByVariable("CONTACTED_PERSON", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted person for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
                if (DataModel_OrderFUP.getValueByVariable("CONTACTED_NO", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contacted number for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
//                if (DataModel_OrderFUP.getValueByVariable("DATE_OF_COMMUNICATION", i).equals("")) {
//                    JOptionPane.showMessageDialog(this, "Please enter DATE OF COMMUNICATION for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
//                    return;
//                }
                if (DataModel_OrderFUP.getValueByVariable("MODE_OF_COMMUNICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter MODE OF COMMUNICATION for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
                if (DataModel_OrderFUP.getValueByVariable("PARTY_JUSTIFICATION", i).equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter PARTY JUSTIFICATION for UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i));
                    return;
                }
            }

            String contacted_person = DataModel_OrderFUP.getValueByVariable("CONTACTED_PERSON", i);
            String contacted_no = DataModel_OrderFUP.getValueByVariable("CONTACTED_NO", i);
            int col_no = DataModel_OrderFUP.getColFromVariable("UNABLE_TO_CONTACT");
            if (Table_OrderFUP.getValueAt(i, col_no).equals(true)) {
                if (contacted_person.equals("") || contacted_no.equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter contected person and number for unable to contact (UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i) + ")");
                    return;
                }

            }

            String expCPRS = DataModel_OrderFUP.getValueByVariable("EXPECTED_CPRS", i);
            String curCPRS = DataModel_OrderFUP.getValueByVariable("CPRS_MONTH", i);
            String expReason = DataModel_OrderFUP.getValueByVariable("EXPECTED_CPRS_REASON", i);

            if (!curCPRS.equals(expCPRS) && expReason.trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter expected CPRS reason for (UPN : " + DataModel_OrderFUP.getValueByVariable("UPN", i) + ", Dummy Piece No : " + DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i) + ")");
                return;
            }

        }

        for (int i = 0; i <= Table_OrderFUP.getRowCount() - 1; i++) {

            String UPN = DataModel_OrderFUP.getValueByVariable("UPN", i);
            String dummyPieceNo = DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i);
            String contacted_person = DataModel_OrderFUP.getValueByVariable("CONTACTED_PERSON", i);
            String contacted_no = DataModel_OrderFUP.getValueByVariable("CONTACTED_NO", i);
            int col_no = DataModel_OrderFUP.getColFromVariable("UNABLE_TO_CONTACT");
            String delayReason = DataModel_OrderFUP.getValueByVariable("DELAY_REASON", i);
            String NewFUPDate = DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i);

            //Unable to Contact
            if (!DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i).equals("") && Table_OrderFUP.getValueAt(i, col_no).equals(true)) {
                int unable_to_contact = 0;
                data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL SET UNABLE_TO_CONTACT='1' WHERE UPN='" + UPN + "'  AND DUMMY_PIECE_NO='" + dummyPieceNo + "' ");//AND DOC_MONTH='"+txtMonth.getText()+"' AND DOC_YEAR='"+txtYear.getText()+"'
                unable_to_contact = 1;
                data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL SET "
                        + " DELAY_REASON='" + delayReason + "',"
                        + "CONTACT_PERSON='" + contacted_person + "',CONTACTED_NO='" + contacted_no + "', "
                        + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                        + "MODE_OF_COMMUNICATION='',"
                        + "PARTY_JUSTIFICATION='',"
                        + "AREA_MANAGER_COMMENT='" + DataModel_OrderFUP.getValueByVariable("AREA_MANAGER_COMMENT", i) + "' "
                        + "WHERE UPN='" + UPN + "'  AND DUMMY_PIECE_NO='" + dummyPieceNo + "' ");// AND DOC_MONTH='"+txtMonth.getText()+"' AND DOC_YEAR='"+txtYear.getText()+"'

                DATA_SAVED = true;
            }

            //Next Follow Up Date
            if (!DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i).equals("") && !DataModel_OrderFUP.getValueByVariable("FOLLOW_UP_DATE", i).equals("")) {
                if (!data.IsRecordExist("SELECT DUMMY_PIECE_NO FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL WHERE UPN='" + UPN + "'  AND DUMMY_PIECE_NO='" + dummyPieceNo + "' AND FOLLOW_UP_DATE='" + EITLERPGLOBAL.formatDateDB(NewFUPDate) + "'")) {//AND DOC_MONTH='" + txtMonth.getText() + "' AND DOC_YEAR='" + txtYear.getText() + "' 

                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL SET "
                            + "NEW_FOLLOW_UP_DATE='" + EITLERPGLOBAL.formatDateDB(NewFUPDate) + "',UNABLE_TO_CONTACT='',"
                            + "DELAY_REASON='" + delayReason + "',"
                            + "CONTACT_PERSON='" + contacted_person + "',CONTACTED_NO='" + contacted_no + "', "
                            + "DATE_OF_COMMUNICATION='" + EITLERPGLOBAL.getCurrentDateDB() + "',"
                            + "MODE_OF_COMMUNICATION='" + DataModel_OrderFUP.getValueByVariable("MODE_OF_COMMUNICATION", i) + "',"
                            + "PARTY_JUSTIFICATION='" + DataModel_OrderFUP.getValueByVariable("PARTY_JUSTIFICATION", i) + "',"
                            + "AREA_MANAGER_COMMENT='" + DataModel_OrderFUP.getValueByVariable("AREA_MANAGER_COMMENT", i) + "' "
                            + "WHERE UPN='" + UPN + "'  AND DUMMY_PIECE_NO='" + dummyPieceNo + "' ");// AND DOC_MONTH='"+txtMonth.getText()+"' AND DOC_YEAR='"+txtYear.getText()+"'

                    DATA_SAVED = true;
                }
            }

            String expCPRS = DataModel_OrderFUP.getValueByVariable("EXPECTED_CPRS", i);
            String curCPRS = DataModel_OrderFUP.getValueByVariable("CPRS_MONTH", i);
            String expReason = DataModel_OrderFUP.getValueByVariable("EXPECTED_CPRS_REASON", i);

            if (!curCPRS.equals(expCPRS)) {
                try {
                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL SET "
                            + "EXPECTED_CPRS='" + expCPRS + "',EXPECTED_CPRS_REASON='" + expReason + "' "
                            + "WHERE UPN='" + UPN + "'  AND DUMMY_PIECE_NO='" + dummyPieceNo + "' ");

                    DATA_SAVED = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (DATA_SAVED) {
            ExpectedCPRS();
            JOptionPane.showMessageDialog(this, "Data updated successfully.");
        }

        DisplayData_OrderFUP(EITLERPGLOBAL.formatDateDB(txtOrderFUPDate.getText()));
    }//GEN-LAST:event_btnSave_OrderFUPActionPerformed

    private void cmbZone_OrderFUPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbZone_OrderFUPItemStateChanged
        // TODO add your handling code here:
//        DisplayData_OrderFUP(EITLERPGLOBAL.formatDateDB(txtOrderFUPDate.getText()));
    }//GEN-LAST:event_cmbZone_OrderFUPItemStateChanged

    private void Export_OrderFUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Export_OrderFUPActionPerformed
        // TODO add your handling code here:
        File file = null;
        file1.setVisible(true);
        try {
            file1.setSelectedFile(new File("OrderFollowup.xls"));
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }

            exp_OFUP.fillData(Table_OrderFUP, file, "OrderFollowup");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file1.getSelectedFile().toString() + " successfully... ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }//GEN-LAST:event_Export_OrderFUPActionPerformed

    private void btnShowData_OrderFUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowData_OrderFUPActionPerformed
        // TODO add your handling code here:
        txtOrderFUPMonth.setText(txtOrderFUPDate.getText().substring(3, 5));
        txtOrderFUPYear.setText(txtOrderFUPDate.getText().substring(6, 10));
        String month_name_orderFup = getMonthName(Integer.parseInt(txtOrderFUPDate.getText().substring(3, 5))) + " - " + txtOrderFUPDate.getText().substring(6, 10);
        txtOrderFUP_CPRS_Month.setText(month_name_orderFup);
        btnPieceDiv_OrderFUP.setEnabled(false);
        btnBooking_OrderFUP.setEnabled(false);

        DisplayData_OrderFUP(EITLERPGLOBAL.formatDateDB(txtOrderFUPDate.getText()));
    }//GEN-LAST:event_btnShowData_OrderFUPActionPerformed

    private void tblOFUP_HistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOFUP_HistoryMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOFUP_HistoryMouseClicked

    private void tblOFUP_HistoryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblOFUP_HistoryFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOFUP_HistoryFocusGained

    private void tblOFUP_HistoryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblOFUP_HistoryFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOFUP_HistoryFocusLost

    private void tblOFUP_HistoryCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblOFUP_HistoryCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOFUP_HistoryCaretPositionChanged

    private void tblOFUP_HistoryAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblOFUP_HistoryAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOFUP_HistoryAncestorMoved

    private void tblOFUP_HistoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblOFUP_HistoryKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOFUP_HistoryKeyPressed

    private void tblOFUP_HistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblOFUP_HistoryKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOFUP_HistoryKeyReleased

    private void btnOFUP_HistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOFUP_HistoryActionPerformed
        // TODO add your handling code here:
        FormatGrid_OFUP_History();

        String Incharge_Name = cmbZone_OrderFUP.getSelectedItem().toString();

        String Incharge_Cd = data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_NAME='" + Incharge_Name + "'");
        txtInchargeCd_OFUPH.setText(Incharge_Cd);
        txtInchargeName_OFUPH.setText(Incharge_Name);
        try {
            String qry = "SELECT * FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL_HISTORY ";
            qry = qry + " WHERE ENTRY_DATETIME>='" + EITLERPGLOBAL.formatDateDB(txtHistoryDate.getText()) + " 00:00:00' ";
            qry = qry + " AND ENTRY_DATETIME<='" + EITLERPGLOBAL.formatDateDB(txtHistoryDate.getText()) + " 23:59:59' ";

            if (Incharge_Name.equals("ACNE/KEYCLIENT")) {
                qry = qry + "AND (INCHARGE='5' OR  INCHARGE='7') ";
            } else if (!Incharge_Name.equals("ALL")) {
                qry = qry + "AND INCHARGE='" + Incharge_Cd + "' ";
            }

            if (!txtOFUP_PartyCode_Search.getText().equals("")) {
                qry = qry + "AND PARTY_CODE='" + txtOFUP_PartyCode_Search.getText() + "' ";
            }

            System.out.println("qry " + qry);
            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(qry);
            resultSet.first();
            int srNoH = 0;

            while (!resultSet.isAfterLast()) {
//            while (resultSet.next()) {
                srNoH++;
                int NewRow = srNoH - 1;

                Object[] rowData = new Object[1];
                DataModel_OFUP_History.addRow(rowData);

                DataModel_OFUP_History.setValueByVariable("SRNO", srNoH + "", NewRow);
                DataModel_OFUP_History.setValueByVariable("ORDER_STATUS", resultSet.getString("ORDER_STATUS"), NewRow);
//                if ("1".equals(resultSet.getString("ORDER_STATUS"))) {
//                    DataModel_OFUP_History.setValueByVariable("ORDER_STATUS", true, NewRow);
//                } else {
//                    DataModel_OFUP_History.setValueByVariable("ORDER_STATUS", false, NewRow);
//                }
                DataModel_OFUP_History.setValueByVariable("UPN", resultSet.getString("UPN"), NewRow);
                DataModel_OFUP_History.setValueByVariable("DUMMY_PIECE_NO", resultSet.getString("DUMMY_PIECE_NO"), NewRow);
                DataModel_OFUP_History.setValueByVariable("ASSIGNED_PIECE_NO", resultSet.getString("ASSIGNED_PIECE_NO"), NewRow);
                String pieceOC = data.getStringValueFromDB("SELECT COALESCE(PR_OC_MONTHYEAR,'') AS OC_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + resultSet.getString("ASSIGNED_PIECE_NO") + "' AND COALESCE(PR_PIECE_NO,'')!='' ");
                DataModel_OFUP_History.setValueByVariable("ASSIGNED_PIECE_OC", pieceOC, NewRow);
                DataModel_OFUP_History.setValueByVariable("ASSIGNED_CATEGORY", resultSet.getString("ASSIGNED_CATEGORY"), NewRow);
                DataModel_OFUP_History.setValueByVariable("PARTY_CODE", resultSet.getString("PARTY_CODE"), NewRow);
                DataModel_OFUP_History.setValueByVariable("PARTY_NAME", clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, resultSet.getString("PARTY_CODE")), NewRow);
                DataModel_OFUP_History.setValueByVariable("CPRS_MONTH", resultSet.getString("CPRS_MONTH"), NewRow);
                DataModel_OFUP_History.setValueByVariable("CPRS_QUARTER", resultSet.getString("CPRS_QUARTER"), NewRow);
                DataModel_OFUP_History.setValueByVariable("OPRS_MONTH", resultSet.getString("OPRS_MONTH"), NewRow);
                DataModel_OFUP_History.setValueByVariable("NEW_FOLLOW_UP_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("NEW_FOLLOW_UP_DATE")), NewRow);

                DataModel_OFUP_History.setValueByVariable("DELAY_REASON", resultSet.getString("DELAY_REASON"), NewRow);
                if ("1".equals(resultSet.getString("UNABLE_TO_CONTACT"))) {
                    DataModel_OFUP_History.setValueByVariable("UNABLE_TO_CONTACT", true, NewRow);
                } else {
                    DataModel_OFUP_History.setValueByVariable("UNABLE_TO_CONTACT", false, NewRow);
                }

                DataModel_OFUP_History.setValueByVariable("CONTACTED_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_OFUP_History.setValueByVariable("CONTACTED_NO", resultSet.getString("CONTACTED_NO"), NewRow);
                DataModel_OFUP_History.setValueByVariable("DATE_OF_COMMUNICATION", EITLERPGLOBAL.formatDate(resultSet.getString("DATE_OF_COMMUNICATION")), NewRow);
                DataModel_OFUP_History.setValueByVariable("MODE_OF_COMMUNICATION", resultSet.getString("MODE_OF_COMMUNICATION"), NewRow);
                DataModel_OFUP_History.setValueByVariable("PARTY_JUSTIFICATION", resultSet.getString("PARTY_JUSTIFICATION"), NewRow);
                DataModel_OFUP_History.setValueByVariable("AREA_MANAGER_COMMENT", resultSet.getString("AREA_MANAGER_COMMENT"), NewRow);
                DataModel_OFUP_History.setValueByVariable("EXPECTED_CPRS", resultSet.getString("EXPECTED_CPRS"), NewRow);
                DataModel_OFUP_History.setValueByVariable("EXPECTED_CPRS_REASON", resultSet.getString("EXPECTED_CPRS_REASON"), NewRow);

                DataModel_OFUP_History.setValueByVariable("ENTRY_DATETIME", resultSet.getString("ENTRY_DATETIME"), NewRow);

                resultSet.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnOFUP_HistoryActionPerformed

    private void txtOFUP_PartyCode_SearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOFUP_PartyCode_SearchKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            EITLERP.LOV aList = new EITLERP.LOV();
//            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_CLOSE_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 ";
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtOFUP_PartyCode_Search.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtOFUP_PartyCode_SearchKeyPressed

    private void btnPieceDiv_OrderFUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPieceDiv_OrderFUPActionPerformed
        // TODO add your handling code here:

        try {

            String UPN = "", PARTY_CODE = "", MACHINE_NO = "", POSITION_NO = "", PIECE_NO_FOR_DIVERSION = "";
            boolean divFlag = false;

            for (int i = 0; i <= Table_OrderFUP.getRowCount() - 1; i++) {
                if (DataModel_OrderFUP.getBoolValueByVariable("SELECT_IND", i) && !DataModel_OrderFUP.getValueByVariable("SELECTED_DIVERSION_PIECE", i).toString().trim().equals("")) {
                    UPN = DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow());

                    PARTY_CODE = DataModel_OrderFUP.getValueByVariable("PARTY_CODE", Table_OrderFUP.getSelectedRow());
                    PIECE_NO_FOR_DIVERSION = DataModel_OrderFUP.getValueByVariable("SELECTED_DIVERSION_PIECE", Table_OrderFUP.getSelectedRow());

                    ResultSet rsData1 = data.getResult("SELECT MM_UPN_NO,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE,MM_FELT_GSM,MM_FELT_LENGTH+MM_FABRIC_LENGTH AS MM_FELT_LENGTH,MM_FELT_WIDTH+MM_FABRIC_WIDTH AS MM_FELT_WIDTH FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_UPN_NO='" + DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow()) + "'");
                    MACHINE_NO = rsData1.getString("MM_MACHINE_NO");
                    POSITION_NO = rsData1.getString("MM_MACHINE_POSITION");

                    AppletFrame aFrame = new AppletFrame("Felt Sales Order : Diversion");
                    aFrame.startAppletEx("EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion", "Felt Sales Order : Diversion");
                    FrmFeltOrderDiversion ObjItem = (FrmFeltOrderDiversion) aFrame.ObjApplet;

                    // ObjItem.frm_S_O_No = S_O_NO.getText();
                    ObjItem.frm_PARTY_CODE = PARTY_CODE;
                    ObjItem.frm_REFERENCE = "";
                    ObjItem.frm_REFERENCE_DATE = "";
                    ObjItem.frm_PO_NO = "";
                    ObjItem.frm_PO_DATE = "";
                    ObjItem.frm_REMARK = "";
                    ObjItem.frm_PARTY_CODE = PARTY_CODE;
                    ObjItem.frm_Piece_No = PIECE_NO_FOR_DIVERSION;
                    ObjItem.frm_Machine_No = MACHINE_NO;
                    ObjItem.frm_Position_No = POSITION_NO;

                    ObjItem.DataSettingFromOrder();
                    divFlag = true;
                }
            }
            if (!divFlag) {
                JOptionPane.showMessageDialog(this, "Please Select piece for diversion.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in Diversion List : " + e.getLocalizedMessage());
        }
    }//GEN-LAST:event_btnPieceDiv_OrderFUPActionPerformed

    private void txtOrderFUPpartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOrderFUPpartycodeFocusLost
        // TODO add your handling code here:
        if (!txtOrderFUPpartycode.getText().trim().equals("") && data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + txtOrderFUPpartycode.getText().trim() + "' AND MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0")) {
            txtOrderFUPpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, txtOrderFUPpartycode.getText()));
        } else {
            if (!txtOrderFUPpartycode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Party Code doesn't exist/under approval.");
            }
            txtOrderFUPpartycode.setText("");
            txtOrderFUPpartyname.setText("");
        }
    }//GEN-LAST:event_txtOrderFUPpartycodeFocusLost

    private void txtOrderFUPpartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOrderFUPpartycodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION,CASE WHEN COALESCE(PARTY_CLOSE_IND,0) =0  AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 0 THEN 'ACTIVE' WHEN COALESCE(PARTY_CLOSE_IND,0) =0  AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =1 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 0 THEN 'ACTIVE' WHEN COALESCE(PARTY_CLOSE_IND,0) =0  AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 1 THEN 'ACTIVE' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =1 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 0 THEN 'IN-ACTIVE' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 1 THEN 'PERMANENTLY CLOSED' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 2 THEN 'TEMPORARY CLOSED' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 0 THEN 'PERMANENTLY CLOSED' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =1 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 1 THEN 'PERMANENTLY CLOSED' END AS PARTY_STATUS FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtOrderFUPpartycode.setText(aList.ReturnVal);
                txtOrderFUPpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtOrderFUPpartycodeKeyPressed

    private void txtOrderFUPpartynameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderFUPpartynameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderFUPpartynameActionPerformed

    private void txtOrderFUPgroupcodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOrderFUPgroupcodeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderFUPgroupcodeFocusLost

    private void txtOrderFUPgroupcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOrderFUPgroupcodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT GROUP_CODE,GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE APPROVED=1 AND CANCELED=0 ORDER BY GROUP_CODE";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtOrderFUPgroupcode.setText(aList.ReturnVal);
                txtOrderFUPgroupname.setText(EITLERP.FeltSales.GroupMasterAmend.clsFeltGroupMasterAmend.getgroupdesc(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtOrderFUPgroupcodeKeyPressed

    private void txtOrderFUPgroupnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderFUPgroupnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderFUPgroupnameActionPerformed

    private void txtOrderFUPprodcategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOrderFUPprodcategoryKeyPressed
        // TODO add your handling code here:   
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            aList.SQL = "SELECT 'ALL' AS PRODUCT_CATEGORY FROM DUAL UNION ALL "
                    + "SELECT * FROM (SELECT DISTINCT PRODUCT_CATEGORY FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
                    + "WHERE COALESCE(PRODUCT_CATEGORY,'')!='' ORDER BY PRODUCT_CATEGORY) AS A";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
//            aList.DefaultSearchOn = 2;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtOrderFUPprodcategory.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtOrderFUPprodcategoryKeyPressed

    private void txtOrderFUPCPRSMonthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOrderFUPCPRSMonthKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            aList.SQL = "SELECT 'ALL' AS CPRS_MONTH FROM DUAL UNION ALL "
                    + "SELECT * FROM (SELECT DISTINCT CPRS_MONTH FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
                    + "WHERE COALESCE(CPRS_MONTH,'')!='' ORDER BY CPRS_MONTH_DATE) AS A";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
//            aList.DefaultSearchOn = 2;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtOrderFUPCPRSMonth.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtOrderFUPCPRSMonthKeyPressed

    private void btnBooking_OrderFUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBooking_OrderFUPActionPerformed
        // TODO add your handling code here:

        try {

            String UPN = "", PARTY_CODE = "", MACHINE_NO = "", POSITION_NO = "", DUMMY_PIECE_NO = "", REQ_MONTH = "";

            ArrayList<clsOrderUPNList> upn_list_create = new ArrayList<>();
            for (int i = 0; i <= Table_OrderFUP.getRowCount() - 1; i++) {
                if (DataModel_OrderFUP.getBoolValueByVariable("SELECT_IND", i)) {
                    UPN = DataModel_OrderFUP.getValueByVariable("UPN", i);
                    PARTY_CODE = DataModel_OrderFUP.getValueByVariable("PARTY_CODE", i);
                    DUMMY_PIECE_NO = DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i);
                    REQ_MONTH = DataModel_OrderFUP.getValueByVariable("CPRS_MONTH", i);

//                    ResultSet rsData1 = data.getResult("SELECT MM_UPN_NO,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE,MM_FELT_GSM,MM_FELT_LENGTH+MM_FABRIC_LENGTH AS MM_FELT_LENGTH,MM_FELT_WIDTH+MM_FABRIC_WIDTH AS MM_FELT_WIDTH FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_UPN_NO='" + DataModel_OrderFUP.getValueByVariable("UPN", Table_OrderFUP.getSelectedRow()) + "'"); //closed on 25/02/2022 Due to wrong UPN taken 
                    ResultSet rsData1 = data.getResult("SELECT MM_UPN_NO,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE,MM_FELT_GSM,MM_FELT_LENGTH+MM_FABRIC_LENGTH AS MM_FELT_LENGTH,MM_FELT_WIDTH+MM_FABRIC_WIDTH AS MM_FELT_WIDTH FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_UPN_NO='"+UPN+"' ");
                    rsData1.first();
                    MACHINE_NO = rsData1.getString("MM_MACHINE_NO");
                    POSITION_NO = rsData1.getString("MM_MACHINE_POSITION");

                    clsOrderUPNList upn = new clsOrderUPNList();
                    upn.setUPN(UPN);
                    upn.setDUMMY_PIECE_NO(DUMMY_PIECE_NO);
                    upn.setREQ_MONTH(REQ_MONTH);
                    upn.setMachine_No(MACHINE_NO);
                    upn.setPositionNo(POSITION_NO);
                    upn_list_create.add(upn);

                }
            }
            AppletFrame aFrame = new AppletFrame("Felt Sales Order");
            aFrame.startAppletEx("EITLERP.FeltSales.Order.FrmFeltOrder", "Felt Sales Order");
            FrmFeltOrder ObjItem = (FrmFeltOrder) aFrame.ObjApplet;

            // ObjItem.frm_S_O_No = S_O_NO.getText();
            ObjItem.frm_PARTY_CODE = PARTY_CODE;
            ObjItem.frm_REFERENCE = "";
            ObjItem.frm_REFERENCE_DATE = "";
            ObjItem.frm_PO_NO = "";
            ObjItem.frm_PO_DATE = "";
            ObjItem.frm_REMARK = "";
            ObjItem.frm_PARTY_CODE = PARTY_CODE;
            ObjItem.frm_PARTY_CODE = PARTY_CODE;
            ObjItem.upn_list = upn_list_create;

            ObjItem.DataSettingFromFollowUp();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in Diversion List : " + e.getLocalizedMessage());
        }
        /*16/02/20225
         try {

         String UPN = "", PARTY_CODE = "", MACHINE_NO = "", POSITION_NO = "", DUMMY_PIECE_NO = "";
            
         for (int i = 0; i <= Table_OrderFUP.getRowCount() - 1; i++) {
         if (DataModel_OrderFUP.getBoolValueByVariable("SELECT_IND", i)) {
         UPN = DataModel_OrderFUP.getValueByVariable("UPN", i);
         PARTY_CODE = DataModel_OrderFUP.getValueByVariable("PARTY_CODE", i);
         DUMMY_PIECE_NO = DataModel_OrderFUP.getValueByVariable("DUMMY_PIECE_NO", i);

                    
         ResultSet rsData1 = data.getResult("SELECT MM_UPN_NO,MM_MACHINE_NO,MM_MACHINE_POSITION,MM_ITEM_CODE,MM_FELT_GSM,MM_FELT_LENGTH+MM_FABRIC_LENGTH AS MM_FELT_LENGTH,MM_FELT_WIDTH+MM_FABRIC_WIDTH AS MM_FELT_WIDTH FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE MM_UPN_NO='" + DataModel_OrderFUP.getValueByVariable("UPN", i) + "'");
         MACHINE_NO = rsData1.getString("MM_MACHINE_NO");
         POSITION_NO = rsData1.getString("MM_MACHINE_POSITION");

         AppletFrame aFrame = new AppletFrame("Felt Sales Order");
         aFrame.startAppletEx("EITLERP.FeltSales.Order.FrmFeltOrder", "Felt Sales Order");
         FrmFeltOrder ObjItem = (FrmFeltOrder) aFrame.ObjApplet;

                    
         ObjItem.frm_PARTY_CODE = PARTY_CODE;
         ObjItem.frm_REFERENCE = "";
         ObjItem.frm_REFERENCE_DATE = "";
         ObjItem.frm_PO_NO = "";
         ObjItem.frm_PO_DATE = "";
         ObjItem.frm_REMARK = "";
         ObjItem.frm_PARTY_CODE = PARTY_CODE;
         ObjItem.frm_Dummy_Piece_No = DUMMY_PIECE_NO;
         ObjItem.frm_Machine_No = MACHINE_NO;
         ObjItem.frm_Position_No = POSITION_NO;
         ObjItem.frm_REQ_MONTH = DataModel_OrderFUP.getValueByVariable("CPRS_MONTH", i);//CPRS_MONTH

         ObjItem.DataSettingFromFollowUp();
         }
         }

         } catch (Exception e) {
         e.printStackTrace();
         System.out.println("Error in Sales Order : " + e.getLocalizedMessage());
         }*/
    }//GEN-LAST:event_btnBooking_OrderFUPActionPerformed

    private void Order_FUP_RptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Order_FUP_RptActionPerformed
        // TODO add your handling code here:
        try {
            String cndtn = "";
            String InchargeCode = data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_NAME='" + cmbZone_OrderFUP.getSelectedItem() + "'");

            if (!cmbZone_OrderFUP.getSelectedItem().equals("ALL")) {
                cndtn = cndtn + " AND INCHARGE='" + InchargeCode + "' ";
            } else {
//                cndtn = cndtn + " AND D.INCHARGE!='6' "; // REMOVE EXPORT Pieces
            }
            if (!txtOrderFUPpartycode.getText().toString().equals("")) {
                cndtn = cndtn + " AND PARTY_CODE='" + txtOrderFUPpartycode.getText().toString().trim() + "' ";
            }
            if (!txtOrderFUPgroupcode.getText().toString().equals("")) {
                cndtn = cndtn + " AND PARTY_CODE IN (SELECT PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.APPROVED=1 AND H.CANCELED=0 AND H.GROUP_CODE=" + txtOrderFUPgroupcode.getText().toString().trim() + ") ";
            }
            if (txtOrderFUPprodcategory.getText().toString().equals("ALL")) {
            } else if (txtOrderFUPprodcategory.getText().toString().equals("")) {
            } else {
                cndtn = cndtn + " AND PRODUCT_CATEGORY='" + txtOrderFUPprodcategory.getText().toString().trim() + "' ";
            }
            if (txtOrderFUPCPRSMonth.getText().toString().equals("ALL")) {
            } else if (txtOrderFUPCPRSMonth.getText().toString().equals("")) {
            } else {
                cndtn = cndtn + " AND CPRS_MONTH='" + txtOrderFUPCPRSMonth.getText().toString().trim() + "' ";
            }

            String qry = "SELECT * FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL WHERE 1=1 ";
            qry = qry + cndtn + "  ORDER BY UPN,CPRS_MONTH_DATE ";

            String smry = "SELECT @a := @a+1 as SR_NO,UPN,PARTY_CODE,PARTY_NAME,COUNT(CPRS_MONTH) AS TOTAL_CPRS, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' THEN 1 ELSE 0 END,0)) AS YET_TO_BOOK, "
                    + "SUM(COALESCE(CASE WHEN UPPER(ORDER_STATUS) = 'ASSIGNED' THEN 1 ELSE 0 END,0)) AS PIECE_ASSIGNED, "
                    + "SUM(COALESCE(CASE WHEN UPPER(ORDER_STATUS) = 'BOOKED' THEN 1 ELSE 0 END,0)) AS BOOKED, "
                    + "SUM(COALESCE(CASE WHEN UPPER(ORDER_STATUS) = 'BOOKING UNDER APPROVAL' THEN 1 ELSE 0 END,0)) AS ORDER_UNDER_APPROVAL, "
                    + "SUM(COALESCE(CASE WHEN UPPER(ORDER_STATUS) = 'DIVERSION UNDER APPROVAL' THEN 1 ELSE 0 END,0)) AS DIV_UNDER_APPROVAL, "
                    + "SUM(COALESCE(CASE WHEN UPPER(ORDER_STATUS) = 'INVOICED' THEN 1 ELSE 0 END,0)) AS INVOICED, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Apr - 2022' THEN 1 ELSE 0 END,0)) AS Apr_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='May - 2022' THEN 1 ELSE 0 END,0)) AS May_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Jun - 2022' THEN 1 ELSE 0 END,0)) AS Jun_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Jul - 2022' THEN 1 ELSE 0 END,0)) AS Jul_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Aug - 2022' THEN 1 ELSE 0 END,0)) AS Aug_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Sep - 2022' THEN 1 ELSE 0 END,0)) AS Sep_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Oct - 2022' THEN 1 ELSE 0 END,0)) AS Oct_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Nov - 2022' THEN 1 ELSE 0 END,0)) AS Nov_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Dec - 2022' THEN 1 ELSE 0 END,0)) AS Dec_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Jan - 2023' THEN 1 ELSE 0 END,0)) AS Jan_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Feb - 2023' THEN 1 ELSE 0 END,0)) AS Feb_YTB, "
                    + "SUM(COALESCE(CASE WHEN COALESCE(ORDER_STATUS,'') = '' AND CPRS_MONTH ='Mar - 2023' THEN 1 ELSE 0 END,0)) AS Mar_YTB "
                    + "FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL,(SELECT @a:= 0) AS a WHERE 1=1 ";
            smry = smry + cndtn + "GROUP BY UPN,PARTY_CODE,PARTY_NAME";

            AppletFrame aFrame = new AppletFrame("Order Followup Report");
            aFrame.startAppletEx("EITLERP.FeltSales.SalesFollowup.frmRptOrderFollowup", "Order Followup Report");
            frmRptOrderFollowup ObjItem = (frmRptOrderFollowup) aFrame.ObjApplet;
            ObjItem.DisplayData_OFUP_Detail(qry);
            ObjItem.DisplayData_OFUP_Summary(smry);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in Report : " + e.getLocalizedMessage());
        }
    }//GEN-LAST:event_Order_FUP_RptActionPerformed

    private String get1stDate(String MonthName) {
        String Date = "";

        String Month = MonthName.substring(0, 3);
        String Year = MonthName.substring(6, 10);
        if (Month.equals("Jan")) {
            Date = "01/01/" + Year;
        } else if (Month.equals("Feb")) {
            Date = "01/02/" + Year;
        } else if (Month.equals("Mar")) {
            Date = "01/03/" + Year;
        } else if (Month.equals("Apr")) {
            Date = "01/04/" + Year;
        } else if (Month.equals("May")) {
            Date = "01/05/" + Year;
        } else if (Month.equals("Jun")) {
            Date = "01/06/" + Year;
        } else if (Month.equals("Jul")) {
            Date = "01/07/" + Year;
        } else if (Month.equals("Aug")) {
            Date = "01/08/" + Year;
        } else if (Month.equals("Sep")) {
            Date = "01/09/" + Year;
        } else if (Month.equals("Oct")) {
            Date = "01/10/" + Year;
        } else if (Month.equals("Nov")) {
            Date = "01/11/" + Year;
        } else if (Month.equals("Dec")) {
            Date = "01/12/" + Year;
        }

        return Date;
    }

    private void Save() {

    }

    private void SetFields(boolean pStat) {

        //   Table.setEnabled(pStat);
        if (USER_ID == 311 || USER_ID == 28 || USER_ID == 352 || USER_ID == 136 || USER_ID == 333 || USER_ID == 318 || USER_ID == 331 || USER_ID == 394 || USER_ID == 280 || USER_ID == 409) {
            //DataModel.SetReadOnly(1);
            if (USER_ID != 280) {
                btnSave.setEnabled(true);
                btnClubbingFollowupSave.setEnabled(true);
                ClubbingEditRight = true;
                btnAddPiece.setEnabled(true);
                btnRemovePiece.setEnabled(true);
                btnUpdateTransaction.setEnabled(true);

                if (USER_ID != 28) {
                    btnClubbingAmend.setEnabled(true);
                }
                //btnExpDateSave.setEnabled(true);
            } else {

                btnSave.setEnabled(false);
                btnClubbingFollowupSave.setEnabled(false);
                ClubbingEditRight = false;
                btnAddPiece.setEnabled(false);
                btnRemovePiece.setEnabled(false);
                btnUpdateTransaction.setEnabled(false);
                btnClubbingAmend.setEnabled(false);
            }

            btnSave_OrderFUP.setEnabled(true);
            btnPieceDiv_OrderFUP.setEnabled(true);
            btnBooking_OrderFUP.setEnabled(true);
        } else {
            DataModel.SetReadOnly(1);
            btnSave.setEnabled(false);
            btnClubbingFollowupSave.setEnabled(false);
            ClubbingEditRight = false;
            btnAddPiece.setEnabled(false);
            btnRemovePiece.setEnabled(false);
            btnUpdateTransaction.setEnabled(false);
            btnClubbingAmend.setEnabled(false);
            //btnExpDateSave.setEnabled(false);

            btnSave_OrderFUP.setEnabled(false);
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddPiece;
    private javax.swing.JButton Export_OrderFUP;
    private javax.swing.JButton Export_WeavingProduction;
    private javax.swing.JButton Order_FUP_Rpt;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTable Table;
    private javax.swing.JTable Table_OrderFUP;
    private javax.swing.JButton btnAddPiece;
    private javax.swing.JButton btnBooking_OrderFUP;
    private javax.swing.JButton btnClubbingAmend;
    private javax.swing.JButton btnClubbingFollowupSave;
    private javax.swing.JButton btnClubbingHistory;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnOFUP_History;
    private javax.swing.JButton btnPieceDiv_OrderFUP;
    private javax.swing.JButton btnRemovePiece;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSave_OrderFUP;
    private javax.swing.JButton btnShowData;
    private javax.swing.JButton btnShowData_OrderFUP;
    private javax.swing.JButton btnShowOrderStatus;
    private javax.swing.JButton btnUpdateTransaction;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JCheckBox chkINSTOCK;
    private javax.swing.JCheckBox chkINVOICED;
    private javax.swing.JCheckBox chkWIP;
    private javax.swing.JComboBox cmbZone;
    private javax.swing.JComboBox cmbZone_OrderFUP;
    private javax.swing.JFileChooser file1;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblCPRSMonth;
    private javax.swing.JLabel lblOFUP_PartyCode;
    private javax.swing.JLabel lblOFUP_PartyName;
    private javax.swing.JLabel lblOFUP_UPN;
    private javax.swing.JLabel lblSelectedParty;
    private javax.swing.JLabel lblSelectedPartyName;
    private javax.swing.JLabel lblSelectedPiece;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel ltbPink;
    private javax.swing.JTable tblClubbedHistory;
    private javax.swing.JTable tblHistory;
    private javax.swing.JTable tblOFUP_History;
    private javax.swing.JTable tblPieceClubbingDetail;
    private javax.swing.JTable tblPieceClubbingHeader;
    private javax.swing.JTable tblPieceClubbingMaster;
    private javax.swing.JTextField txtApprovalStatus;
    private javax.swing.JTextField txtClubbibgNo;
    private javax.swing.JTextField txtCurrentSchMonth;
    private javax.swing.JTextField txtFWLCurrentSchMonth;
    private javax.swing.JTextField txtFWLMonth;
    private javax.swing.JTextField txtFWLYear;
    private javax.swing.JTextField txtFollowupDate;
    private javax.swing.JTextField txtGSM;
    private javax.swing.JTextField txtHistoryDate;
    private javax.swing.JTextField txtInchargeCd;
    private javax.swing.JTextField txtInchargeCd_Clubbing;
    private javax.swing.JTextField txtInchargeCd_OFUPH;
    private javax.swing.JTextField txtInchargeName;
    private javax.swing.JTextField txtInchargeName_Clubbing;
    private javax.swing.JTextField txtInchargeName_OFUPH;
    private javax.swing.JTextField txtLength;
    private javax.swing.JTextField txtMachine;
    private javax.swing.JTextField txtMonth;
    private javax.swing.JTextField txtOFUP_GSM;
    private javax.swing.JTextField txtOFUP_HistoryDate;
    private javax.swing.JTextField txtOFUP_Length;
    private javax.swing.JTextField txtOFUP_Machine;
    private javax.swing.JTextField txtOFUP_PartyCode_Search;
    private javax.swing.JTextField txtOFUP_Position;
    private javax.swing.JTextField txtOFUP_ProductCode;
    private javax.swing.JTextField txtOFUP_Width;
    private javax.swing.JTextField txtOrderFUPCPRSMonth;
    private javax.swing.JTextField txtOrderFUPDate;
    private javax.swing.JTextField txtOrderFUPMonth;
    private javax.swing.JTextField txtOrderFUPYear;
    private javax.swing.JTextField txtOrderFUP_CPRS_Month;
    private javax.swing.JTextField txtOrderFUPgroupcode;
    private javax.swing.JTextField txtOrderFUPgroupname;
    private javax.swing.JTextField txtOrderFUPpartycode;
    private javax.swing.JTextField txtOrderFUPpartyname;
    private javax.swing.JTextField txtOrderFUPprodcategory;
    private javax.swing.JTextField txtPartyCode_ClubbingSearch;
    private javax.swing.JTextField txtPartyCode_Search;
    private javax.swing.JTextField txtPieceNo_ClubbingSearch;
    private javax.swing.JTextField txtPieceNo_Search;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtSelectedClubbingNo;
    private javax.swing.JTextField txtSelectedClubbingPartyCode;
    private javax.swing.JTextField txtWidth;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables

    private void clearFields_OrderFUP() {
        if (Table_OrderFUP.getRowCount() > 0) {
            for (int i = 0; i < DataModel_OrderFUP.getRowCount(); i++) {
                DataModel_OrderFUP.removeRow(i);
            }
            if (DataModel_OrderFUP.getRowCount() > 0) {
                DataModel_OrderFUP.removeRow(0);
            }
            Object[] rowData = new Object[50];
            rowData[0] = 1;
            DataModel_OrderFUP.addRow(rowData);
        }
    }

    private void GenerateOrderFUPAreaCombo() {

        HashMap List = new HashMap();
        clsIncharge ObjIncharge;

        cmbZone_OrderFUP.setModel(cmbIncharge_OrderFUP);
        cmbIncharge_OrderFUP.removeAllElements();  //Clearing previous contents

        List = clsIncharge.getIncgargeList("");

        for (int i = 1; i <= List.size(); i++) {
            ObjIncharge = (clsIncharge) List.get(Integer.toString(i));
            if (
//                    !ObjIncharge.getAttribute("INCHARGE_NAME").getObj().toString().equals("EXPORT") && 
                    !ObjIncharge.getAttribute("INCHARGE_NAME").getObj().toString().equals("ACNE")
                    && !ObjIncharge.getAttribute("INCHARGE_NAME").getObj().toString().equals("KEY CLIENT")) {

                ComboData aData9 = new ComboData();
                aData9.Text = (String) ObjIncharge.getAttribute("INCHARGE_NAME").getObj();
                aData9.Code = (long) ObjIncharge.getAttribute("INCHARGE_CD").getVal();
                //if(!ObjIncharge.getAttribute("INCHARGE_NAME").getObj().equals("ALL"))
                //{
                cmbIncharge_OrderFUP.addElement(aData9);
                //}
            }
        }
    }

    private void FormatGrid_OrderFUP() {
        try {
            DataModel_OrderFUP = new EITLTableModel();
            Table_OrderFUP.removeAll();

            Table_OrderFUP.setModel(DataModel_OrderFUP);
            Table_OrderFUP.setAutoResizeMode(0);

            DataModel_OrderFUP.addColumn("SrNo"); //0
            DataModel_OrderFUP.addColumn("Select"); //1
            DataModel_OrderFUP.addColumn("Status"); //1
            DataModel_OrderFUP.addColumn("UPN"); //2
            DataModel_OrderFUP.addColumn("Dummy Ref.No"); //3
            DataModel_OrderFUP.addColumn("Assign PieceNo"); //4
            DataModel_OrderFUP.addColumn("Ass.PieceOC"); //4
            DataModel_OrderFUP.addColumn("Ass.Category"); //4
            DataModel_OrderFUP.addColumn("Party Code"); //5
            DataModel_OrderFUP.addColumn("Party Name"); //6
            DataModel_OrderFUP.addColumn("CPRS Month");// 7
            DataModel_OrderFUP.addColumn("OPRS Month");//8
            DataModel_OrderFUP.addColumn("Next Follow Up Date");//14
            DataModel_OrderFUP.addColumn("Delay Reason");//17
            DataModel_OrderFUP.addColumn("Unable to Contact");//18
            DataModel_OrderFUP.addColumn("Contacted Person");//19
            DataModel_OrderFUP.addColumn("Contacted Contact No");//20
            DataModel_OrderFUP.addColumn("Date of Communication");//21
            DataModel_OrderFUP.addColumn("Mode of Communication");//22
            DataModel_OrderFUP.addColumn("Prevoius Party Justification");//23
            DataModel_OrderFUP.addColumn("Party Justification");//23
            DataModel_OrderFUP.addColumn("Prevoius Area Manager Comments");//24
            DataModel_OrderFUP.addColumn("Area Manager Comments");//25
            DataModel_OrderFUP.addColumn("Quarter");// 7
            DataModel_OrderFUP.addColumn("Prod.Category");// 7
            DataModel_OrderFUP.addColumn("Auto Div.Pieces");// 7
            DataModel_OrderFUP.addColumn("Select Div.Piece");// 7
            DataModel_OrderFUP.addColumn("Expected CPRS");// 7
            DataModel_OrderFUP.addColumn("Expected CPRS Reason");// 7

            DataModel_OrderFUP.SetVariable(0, "SRNO");
            DataModel_OrderFUP.SetVariable(1, "SELECT_IND");
            DataModel_OrderFUP.SetVariable(2, "ORDER_STATUS");
            DataModel_OrderFUP.SetVariable(3, "UPN");
            DataModel_OrderFUP.SetVariable(4, "DUMMY_PIECE_NO");
            DataModel_OrderFUP.SetVariable(5, "ASSIGNED_PIECE_NO");
            DataModel_OrderFUP.SetVariable(6, "ASSIGNED_PIECE_OC");
            DataModel_OrderFUP.SetVariable(7, "ASSIGNED_CATEGORY");
            DataModel_OrderFUP.SetVariable(8, "PARTY_CODE");
            DataModel_OrderFUP.SetVariable(9, "PARTY_NAME");
            DataModel_OrderFUP.SetVariable(10, "CPRS_MONTH");
            DataModel_OrderFUP.SetVariable(11, "OPRS_MONTH");
            DataModel_OrderFUP.SetVariable(12, "FOLLOW_UP_DATE");//14
            DataModel_OrderFUP.SetVariable(13, "DELAY_REASON");//17
            DataModel_OrderFUP.SetVariable(14, "UNABLE_TO_CONTACT");//18
            DataModel_OrderFUP.SetVariable(15, "CONTACTED_PERSON");//19
            DataModel_OrderFUP.SetVariable(16, "CONTACTED_NO");//20
            DataModel_OrderFUP.SetVariable(17, "DATE_OF_COMMUNICATION");//21
            DataModel_OrderFUP.SetVariable(18, "MODE_OF_COMMUNICATION");//22
            DataModel_OrderFUP.SetVariable(19, "PREVIOUS_PARTY_JUSTIFICATION");//23
            DataModel_OrderFUP.SetVariable(20, "PARTY_JUSTIFICATION");//24
            DataModel_OrderFUP.SetVariable(21, "PREVIOUS_AREA_MANAGER_COMMENT");//25
            DataModel_OrderFUP.SetVariable(22, "AREA_MANAGER_COMMENT");//26
            DataModel_OrderFUP.SetVariable(23, "CPRS_QUARTER");//26
            DataModel_OrderFUP.SetVariable(24, "PRODUCT_CATEGORY");//26
            DataModel_OrderFUP.SetVariable(25, "AUTO_DIVERSION_PIECES");//26
            DataModel_OrderFUP.SetVariable(26, "SELECTED_DIVERSION_PIECE");//26
            DataModel_OrderFUP.SetVariable(27, "EXPECTED_CPRS");//26
            DataModel_OrderFUP.SetVariable(28, "EXPECTED_CPRS_REASON");//26EXPECTED_CPRS_REASON

            Table_OrderFUP.getColumnModel().getColumn(0).setMaxWidth(40);
            Table_OrderFUP.getColumnModel().getColumn(1).setMaxWidth(40);
            Table_OrderFUP.getColumnModel().getColumn(2).setMinWidth(80);
            Table_OrderFUP.getColumnModel().getColumn(3).setMinWidth(100);
            Table_OrderFUP.getColumnModel().getColumn(4).setMinWidth(0);
            Table_OrderFUP.getColumnModel().getColumn(4).setMaxWidth(0);
            Table_OrderFUP.getColumnModel().getColumn(5).setMinWidth(100);
            Table_OrderFUP.getColumnModel().getColumn(6).setMinWidth(80);
            Table_OrderFUP.getColumnModel().getColumn(7).setMinWidth(80);
            Table_OrderFUP.getColumnModel().getColumn(8).setMinWidth(60);
            Table_OrderFUP.getColumnModel().getColumn(9).setMinWidth(120);
            Table_OrderFUP.getColumnModel().getColumn(10).setMinWidth(80);
            Table_OrderFUP.getColumnModel().getColumn(11).setMinWidth(80);
            Table_OrderFUP.getColumnModel().getColumn(12).setMinWidth(100);
            Table_OrderFUP.getColumnModel().getColumn(13).setMinWidth(0);
            Table_OrderFUP.getColumnModel().getColumn(13).setMaxWidth(0);
            Table_OrderFUP.getColumnModel().getColumn(14).setMinWidth(80);
            Table_OrderFUP.getColumnModel().getColumn(15).setMinWidth(100);
            Table_OrderFUP.getColumnModel().getColumn(16).setMinWidth(100);
            Table_OrderFUP.getColumnModel().getColumn(17).setMinWidth(0);
            Table_OrderFUP.getColumnModel().getColumn(17).setMaxWidth(0);
            Table_OrderFUP.getColumnModel().getColumn(18).setMinWidth(120);
            Table_OrderFUP.getColumnModel().getColumn(19).setMinWidth(200);
            Table_OrderFUP.getColumnModel().getColumn(20).setMinWidth(250);
            Table_OrderFUP.getColumnModel().getColumn(21).setMinWidth(200);
            Table_OrderFUP.getColumnModel().getColumn(22).setMinWidth(250);
            Table_OrderFUP.getColumnModel().getColumn(23).setMinWidth(60);
            Table_OrderFUP.getColumnModel().getColumn(24).setMinWidth(80);
            Table_OrderFUP.getColumnModel().getColumn(25).setMinWidth(150);
            Table_OrderFUP.getColumnModel().getColumn(26).setMinWidth(100);
            Table_OrderFUP.getColumnModel().getColumn(27).setMinWidth(100);
            Table_OrderFUP.getColumnModel().getColumn(28).setMinWidth(250);

            Table_OrderFUP.getTableHeader().setReorderingAllowed(false);

            DataModel_OrderFUP.SetReadOnly(0);
            DataModel_OrderFUP.SetReadOnly(2);
            DataModel_OrderFUP.SetReadOnly(3);
            DataModel_OrderFUP.SetReadOnly(4);
            DataModel_OrderFUP.SetReadOnly(5);
            DataModel_OrderFUP.SetReadOnly(6);
            DataModel_OrderFUP.SetReadOnly(7);
            DataModel_OrderFUP.SetReadOnly(8);
            DataModel_OrderFUP.SetReadOnly(9);
            DataModel_OrderFUP.SetReadOnly(10);
            DataModel_OrderFUP.SetReadOnly(11);
            DataModel_OrderFUP.SetReadOnly(12);//FOLLOWUP_DATE
            DataModel_OrderFUP.SetReadOnly(13);
            DataModel_OrderFUP.SetReadOnly(15);
            DataModel_OrderFUP.SetReadOnly(16);
            DataModel_OrderFUP.SetReadOnly(23);
            DataModel_OrderFUP.SetReadOnly(24);
            DataModel_OrderFUP.SetReadOnly(25);
            DataModel_OrderFUP.SetReadOnly(26);
            if (USER_ID != 311 && USER_ID != 352 && USER_ID != 136 && USER_ID != 333 && USER_ID != 318 && USER_ID != 329 && USER_ID != 331 && USER_ID != 28 && USER_ID != 394 && USER_ID != 361 && USER_ID != 280 && USER_ID != 409) {
                DataModel_OrderFUP.SetReadOnly(1);
//                DataModel_OrderFUP.SetReadOnly(12);
                DataModel_OrderFUP.SetReadOnly(14);
                DataModel_OrderFUP.SetReadOnly(17);
                DataModel_OrderFUP.SetReadOnly(18);
                DataModel_OrderFUP.SetReadOnly(19);
                DataModel_OrderFUP.SetReadOnly(20);
                DataModel_OrderFUP.SetReadOnly(21);
                DataModel_OrderFUP.SetReadOnly(22);
                DataModel_OrderFUP.SetReadOnly(27);
                DataModel_OrderFUP.SetReadOnly(28);
            }

            for (int i = 0; i < DataModel_OrderFUP.getColumnCount(); i++) {
                Table_OrderFUP.getColumnModel().getColumn(i).setCellRenderer(this.Renderer_COLOR);
            }

//            int ImportCol1 = 1;
            int ImportCol1 = DataModel_OrderFUP.getColFromVariable("SELECT_IND");
            Renderer1.setCustomComponent(ImportCol1, "CheckBox");
            final JCheckBox aCheckBox1 = new JCheckBox();
            aCheckBox1.setBackground(Color.WHITE);
            aCheckBox1.setVisible(true);
            aCheckBox1.setSelected(false);
            Table_OrderFUP.getColumnModel().getColumn(ImportCol1).setCellEditor(new DefaultCellEditor(aCheckBox1));
            Table_OrderFUP.getColumnModel().getColumn(ImportCol1).setCellRenderer(Renderer1);

//            int ImportCol3 = 3;
//            Renderer_btn.setCustomComponent(ImportCol3, "CheckBox");
//            Table_OrderFUP.getColumnModel().getColumn(ImportCol3).setCellRenderer(Renderer_btn);
//            Table_OrderFUP.getColumnModel().getColumn(ImportCol3).setCellEditor(new DefaultCellEditor(new JCheckBox()));
//            Table_OrderFUP.getColumnModel().getColumn(ImportCol3).setCellEditor(new ButtonEditor(new JCheckBox()));
//            Table_OrderFUP.getColumnModel().getColumn(ImportCol3).setCellRenderer(new ButtonRenderer());
//            int ImportColUTC_OrderFUP = 11;
            int ImportColUTC_OrderFUP = DataModel_OrderFUP.getColFromVariable("UNABLE_TO_CONTACT");
            RendererUTC_OrderFUP.setCustomComponent(ImportColUTC_OrderFUP, "CheckBox");
            JCheckBox aCheckBoxUTC_OrderFUP = new JCheckBox();
            aCheckBoxUTC_OrderFUP.setBackground(Color.WHITE);
            aCheckBoxUTC_OrderFUP.setVisible(true);
            aCheckBoxUTC_OrderFUP.setSelected(false);
            Table_OrderFUP.getColumnModel().getColumn(ImportColUTC_OrderFUP).setCellEditor(new DefaultCellEditor(aCheckBoxUTC_OrderFUP));
            Table_OrderFUP.getColumnModel().getColumn(ImportColUTC_OrderFUP).setCellRenderer(RendererUTC_OrderFUP);

            TableColumn dateColumn_OFUP = Table_OrderFUP.getColumnModel().getColumn(DataModel_OrderFUP.getColFromVariable("EXPECTED_CPRS"));

            JComboBox monthbox_OFUP = new JComboBox();
            String month_name = "";
            Date date = new Date();
            int month = date.getMonth();
            int year = date.getYear() + 1900;
            //monthbox.addItem("");
            month = month + 2;
            if(EITLERPGLOBAL.getCurrentDay()<=10)
            {
                month = month - 1;
            }
            for (int i = 0; i < 12; i++) {
                month = month + 1;

                if (month == 13) {
                    month = 1;
                    year = year + 1;
                }
                if (month == 14) {
                    month = 2;
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
                if(month>=4 && year>=2023) {
                    
                } else {
                    monthbox_OFUP.addItem(month_name + " - " + year);
                }
            }
            dateColumn_OFUP.setCellEditor(new DefaultCellEditor(monthbox_OFUP));            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DisplayData_OrderFUP(String FollowDate) {
        try {
            clearFields_OrderFUP();
            FormatGrid_OrderFUP();

            Connection connection = data.getConn();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String InchargeCode = data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_NAME='" + cmbZone_OrderFUP.getSelectedItem() + "'");

            Date date = new Date();
            int month = date.getMonth() + 1;
            int year = date.getYear() + 1900;

            if (EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(txtOrderFUPDate.getText())) != month) {
                month = EITLERPGLOBAL.getMonth(EITLERPGLOBAL.formatDateDB(txtOrderFUPDate.getText()));
                year = EITLERPGLOBAL.getYear(EITLERPGLOBAL.formatDateDB(txtOrderFUPDate.getText()));
            }
            String qry = "SELECT D.* FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL D "            
                    + "WHERE CPRS_OPEN_DATE<='" + FollowDate + "' AND CPRS_CLOSE_DATE>='" + FollowDate + "' ";
//            + "WHERE DOC_MONTH=" + month + " AND DOC_YEAR=" + year + " ";

            if (EITLERPGLOBAL.getCurrentDateDB().equals(FollowDate)) {
                qry = qry + " AND (FOLLOW_UP_DATE='" + FollowDate + "' OR FOLLOW_UP_DATE='0000-00-00') ";
            } else {
//                qry = qry + " AND NEW_FOLLOW_UP_DATE='" + FollowDate + "' ";
                qry = qry + " AND (FOLLOW_UP_DATE='" + FollowDate + "' OR FOLLOW_UP_DATE='0000-00-00') ";
            }

            if (!cmbZone_OrderFUP.getSelectedItem().equals("ALL")) {
                qry = qry + " AND D.INCHARGE='" + InchargeCode + "' ";
            } else {
//                qry = qry + " AND D.INCHARGE!='6' "; // REMOVE EXPORT Pieces
            }

            if (!txtOrderFUPpartycode.getText().toString().equals("")) {
                qry = qry + " AND D.PARTY_CODE='" + txtOrderFUPpartycode.getText().toString().trim() + "' ";
            }

            if (!txtOrderFUPgroupcode.getText().toString().equals("")) {
                qry = qry + " AND D.PARTY_CODE IN (SELECT PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND H.APPROVED=1 AND H.CANCELED=0 AND H.GROUP_CODE=" + txtOrderFUPgroupcode.getText().toString().trim() + ") ";
            }

            if (txtOrderFUPprodcategory.getText().toString().equals("ALL")) {
            } else if (txtOrderFUPprodcategory.getText().toString().equals("")) {
            } else {
                qry = qry + " AND D.PRODUCT_CATEGORY='" + txtOrderFUPprodcategory.getText().toString().trim() + "' ";
            }

            if (txtOrderFUPCPRSMonth.getText().toString().equals("ALL")) {
            } else if (txtOrderFUPCPRSMonth.getText().toString().equals("")) {
            } else {
                qry = qry + " AND D.CPRS_MONTH='" + txtOrderFUPCPRSMonth.getText().toString().trim() + "' ";
            }

//            qry = qry + " UNION ALL SELECT * FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL_NEXTMONTH ND WHERE ND.NEW_FOLLOW_UP_DATE='"+FollowDate+"' AND DOC_MONTH!='"+month+"' AND DOC_YEAR!='"+year+"' ";
            qry = qry + "  ORDER BY UPN,DUMMY_PIECE_NO ";
//            System.out.println(" qry " + qry);
            System.out.println("qry *** " + qry);
            ResultSet resultSet = statement.executeQuery(qry);
            int srNo = 0;

            while (resultSet.next()) {
                srNo++;
                int NewRow = srNo - 1;

                Object[] rowData = new Object[1];
                DataModel_OrderFUP.addRow(rowData);

                DataModel_OrderFUP.setValueByVariable("SRNO", srNo + "", NewRow);
                DataModel_OrderFUP.setValueByVariable("SELECT_IND", false, NewRow);
                DataModel_OrderFUP.setValueByVariable("ORDER_STATUS", resultSet.getString("ORDER_STATUS"), NewRow);
                DataModel_OrderFUP.setValueByVariable("UPN", resultSet.getString("UPN"), NewRow);
                DataModel_OrderFUP.setValueByVariable("DUMMY_PIECE_NO", resultSet.getString("DUMMY_PIECE_NO"), NewRow);
                DataModel_OrderFUP.setValueByVariable("ASSIGNED_PIECE_NO", resultSet.getString("ASSIGNED_PIECE_NO"), NewRow);
//                String pieceOC = data.getStringValueFromDB("SELECT COALESCE(PR_OC_MONTHYEAR,'') AS OC_MONTH FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + resultSet.getString("ASSIGNED_PIECE_NO") + "' AND COALESCE(PR_PIECE_NO,'')!='' ");
//                DataModel_OrderFUP.setValueByVariable("ASSIGNED_PIECE_OC", pieceOC, NewRow);
                DataModel_OrderFUP.setValueByVariable("ASSIGNED_PIECE_OC", resultSet.getString("ASSIGNED_PIECE_OC"), NewRow);
                DataModel_OrderFUP.setValueByVariable("ASSIGNED_CATEGORY", resultSet.getString("ASSIGNED_CATEGORY"), NewRow);
                DataModel_OrderFUP.setValueByVariable("PARTY_CODE", resultSet.getString("PARTY_CODE"), NewRow);
                DataModel_OrderFUP.setValueByVariable("PARTY_NAME", resultSet.getString("PARTY_NAME"), NewRow);
                DataModel_OrderFUP.setValueByVariable("CPRS_MONTH", resultSet.getString("CPRS_MONTH"), NewRow);
                DataModel_OrderFUP.setValueByVariable("OPRS_MONTH", resultSet.getString("OPRS_MONTH"), NewRow);
                DataModel_OrderFUP.setValueByVariable("FOLLOW_UP_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("NEW_FOLLOW_UP_DATE")), NewRow);
//                DataModel.setValueByVariable("NEW_EXPECTED_DISPATCH_DATE", EITLERPGLOBAL.formatDate(resultSet.getString("FOLLOWUP_DATE")), NewRow);

                DataModel_OrderFUP.setValueByVariable("DELAY_REASON", resultSet.getString("DELAY_REASON"), NewRow);

                if ("1".equals(resultSet.getString("UNABLE_TO_CONTACT"))) {
                    DataModel_OrderFUP.setValueByVariable("UNABLE_TO_CONTACT", true, NewRow);
                } else {
                    DataModel_OrderFUP.setValueByVariable("UNABLE_TO_CONTACT", false, NewRow);
                }

                DataModel_OrderFUP.setValueByVariable("CONTACTED_PERSON", resultSet.getString("CONTACT_PERSON"), NewRow);
                DataModel_OrderFUP.setValueByVariable("CONTACTED_NO", resultSet.getString("CONTACTED_NO"), NewRow);

                DataModel_OrderFUP.setValueByVariable("DATE_OF_COMMUNICATION", EITLERPGLOBAL.formatDate(resultSet.getString("DATE_OF_COMMUNICATION")), NewRow);
                DataModel_OrderFUP.setValueByVariable("MODE_OF_COMMUNICATION", resultSet.getString("MODE_OF_COMMUNICATION"), NewRow);

                //if (EITLERPGLOBAL.getCurrentDateDB().equals(FollowDate)) {
//                String Previous_Party_Justification = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',PARTY_JUSTIFICATION)) AS PARTY_JUSTIFICATION "
//                        + "FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL_HISTORY "
//                        + "WHERE UPN='" + resultSet.getString("UPN") + "' AND DUMMY_PIECE_NO='" + resultSet.getString("DUMMY_PIECE_NO") + "' AND PARTY_JUSTIFICATION!='' "
//                        + "ORDER BY ENTRY_DATETIME DESC LIMIT 1");
//                String Previous_Area_Manager_Comments = data.getStringValueFromDB("SELECT CONCAT( DATE_FORMAT(DATE_OF_COMMUNICATION ,'%d/%m/%Y'),CONCAT(' - ',AREA_MANAGER_COMMENT)) AREA_MANAGER_COMMENT "
//                        + "FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL_HISTORY "
//                        + "WHERE UPN='" + resultSet.getString("UPN") + "' AND DUMMY_PIECE_NO='" + resultSet.getString("DUMMY_PIECE_NO") + "' AND AREA_MANAGER_COMMENT!='' "
//                        + "ORDER BY ENTRY_DATETIME DESC LIMIT 1");

//                DataModel_OrderFUP.setValueByVariable("PREVIOUS_PARTY_JUSTIFICATION", Previous_Party_Justification, NewRow);
//                DataModel_OrderFUP.setValueByVariable("PREVIOUS_AREA_MANAGER_COMMENT", Previous_Area_Manager_Comments, NewRow);
                DataModel_OrderFUP.setValueByVariable("PREVIOUS_PARTY_JUSTIFICATION", resultSet.getString("PREVIOUS_PARTY_JUSTIFICATION"), NewRow);
                DataModel_OrderFUP.setValueByVariable("PREVIOUS_AREA_MANAGER_COMMENT", resultSet.getString("PREVIOUS_AREA_MANAGER_COMMENT"), NewRow);
                //}

                DataModel_OrderFUP.setValueByVariable("PARTY_JUSTIFICATION", resultSet.getString("PARTY_JUSTIFICATION"), NewRow);
                DataModel_OrderFUP.setValueByVariable("AREA_MANAGER_COMMENT", resultSet.getString("AREA_MANAGER_COMMENT"), NewRow);

                DataModel_OrderFUP.setValueByVariable("CPRS_QUARTER", resultSet.getString("CPRS_QUARTER"), NewRow);
                DataModel_OrderFUP.setValueByVariable("PRODUCT_CATEGORY", resultSet.getString("PRODUCT_CATEGORY"), NewRow);

                String cDivPieces = data.getStringValueFromDB("SELECT GROUP_CONCAT(PIECE_NO ORDER BY PIECE_NO) AS PIECES "
                        + "FROM PRODUCTION.FELT_SALES_UPNWISE_OBSOLETE_LIST WHERE COALESCE(DIVERSION_NO,'')='' AND UPN='" + resultSet.getString("UPN") + "' GROUP BY UPN");
                
//                String cDivPieces = "";
                DataModel_OrderFUP.setValueByVariable("AUTO_DIVERSION_PIECES", cDivPieces, NewRow);
                DataModel_OrderFUP.setValueByVariable("SELECTED_DIVERSION_PIECE", resultSet.getString("SELECTED_DIVERSION_PIECE"), NewRow);

                DataModel_OrderFUP.setValueByVariable("EXPECTED_CPRS", resultSet.getString("EXPECTED_CPRS"), NewRow);
                DataModel_OrderFUP.setValueByVariable("EXPECTED_CPRS_REASON", resultSet.getString("EXPECTED_CPRS_REASON"), NewRow);

            }

        } catch (Exception e) {
            e.printStackTrace();
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
                if (Table_OrderFUP.getSelectedColumn() == 3) {
                    openPieceDetail();
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

    public void openPieceDetail() {
        try {
            //System.out.println("DRP");
//            String PieceNo = TableDesc.getValueAt(TableDesc.getSelectedRow(), 1).toString();
//            if (PieceNo.contains("-A") || PieceNo.contains("-B")) {
//                PieceNo = PieceNo.substring(0, 5);
//            }
//
//            AppletFrame aFrame = new AppletFrame("Felt Piece Details");
//            aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceMaster.FrmPieceMasterDetail", "Felt Piece Details");
//            FrmPieceMasterDetail ObjItem = (FrmPieceMasterDetail) aFrame.ObjApplet;
//
//            ObjItem.requestFocus();
//            ObjItem.setData(PieceNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FormatGrid_OFUP_History() {
        try {
            DataModel_OFUP_History = new EITLTableModel();
            tblOFUP_History.removeAll();

            tblOFUP_History.setModel(DataModel_OFUP_History);
            tblOFUP_History.setAutoResizeMode(0);

            DataModel_OFUP_History.addColumn("SrNo"); //0
            DataModel_OFUP_History.addColumn("Status"); //1
            DataModel_OFUP_History.addColumn("UPN"); //2
            DataModel_OFUP_History.addColumn("Dummy Ref.No"); //3
            DataModel_OFUP_History.addColumn("Assign PieceNo"); //4
            DataModel_OFUP_History.addColumn("Assign PieceOC"); //4
            DataModel_OFUP_History.addColumn("Assign Category"); //4
            DataModel_OFUP_History.addColumn("Party Code"); //5
            DataModel_OFUP_History.addColumn("Party Name"); //6
            DataModel_OFUP_History.addColumn("CPRS Month");// 7
            DataModel_OFUP_History.addColumn("CPRS Quarter");// 7
            DataModel_OFUP_History.addColumn("OPRS Month");//8
            DataModel_OFUP_History.addColumn("Next Follow Up Date");//14
            DataModel_OFUP_History.addColumn("Delay Reason");//17
            DataModel_OFUP_History.addColumn("Unable to Contact");//18
            DataModel_OFUP_History.addColumn("Contacted Person");//19
            DataModel_OFUP_History.addColumn("Contacted Contact No");//20
            DataModel_OFUP_History.addColumn("Date of Communication");//21
            DataModel_OFUP_History.addColumn("Mode of Communication");//22
            DataModel_OFUP_History.addColumn("Prevoius Party Justification");//23
            DataModel_OFUP_History.addColumn("Party Justification");//23
            DataModel_OFUP_History.addColumn("Prevoius Area Manager Comments");//24
            DataModel_OFUP_History.addColumn("Area Manager Comments");//25
            DataModel_OFUP_History.addColumn("Expected CPRS");
            DataModel_OFUP_History.addColumn("Expected CPRS Reason");
            DataModel_OFUP_History.addColumn("Entry Time");

            DataModel_OFUP_History.SetVariable(0, "SRNO");
            DataModel_OFUP_History.SetVariable(1, "ORDER_STATUS");
            DataModel_OFUP_History.SetVariable(2, "UPN");
            DataModel_OFUP_History.SetVariable(3, "DUMMY_PIECE_NO");
            DataModel_OFUP_History.SetVariable(4, "ASSIGNED_PIECE_NO");
            DataModel_OFUP_History.SetVariable(5, "ASSIGNED_PIECE_OC");
            DataModel_OFUP_History.SetVariable(6, "ASSIGNED_CATEGORY");
            DataModel_OFUP_History.SetVariable(7, "PARTY_CODE");
            DataModel_OFUP_History.SetVariable(8, "PARTY_NAME");
            DataModel_OFUP_History.SetVariable(9, "CPRS_MONTH");
            DataModel_OFUP_History.SetVariable(10, "CPRS_QUARTER");
            DataModel_OFUP_History.SetVariable(11, "OPRS_MONTH");
            DataModel_OFUP_History.SetVariable(12, "NEW_FOLLOW_UP_DATE");//14
            DataModel_OFUP_History.SetVariable(13, "DELAY_REASON");//17
            DataModel_OFUP_History.SetVariable(14, "UNABLE_TO_CONTACT");//18
            DataModel_OFUP_History.SetVariable(15, "CONTACTED_PERSON");//19
            DataModel_OFUP_History.SetVariable(16, "CONTACTED_NO");//20
            DataModel_OFUP_History.SetVariable(17, "DATE_OF_COMMUNICATION");//21
            DataModel_OFUP_History.SetVariable(18, "MODE_OF_COMMUNICATION");//22
            DataModel_OFUP_History.SetVariable(19, "PREVIOUS_PARTY_JUSTIFICATION");//23
            DataModel_OFUP_History.SetVariable(20, "PARTY_JUSTIFICATION");//24
            DataModel_OFUP_History.SetVariable(21, "PREVIOUS_AREA_MANAGER_COMMENT");//25
            DataModel_OFUP_History.SetVariable(22, "AREA_MANAGER_COMMENT");//26            
            DataModel_OFUP_History.SetVariable(23, "EXPECTED_CPRS");
            DataModel_OFUP_History.SetVariable(24, "EXPECTED_CPRS_REASON");//26            
            DataModel_OFUP_History.SetVariable(25, "ENTRY_DATETIME");

//            int ImportCol_OFUP11 = 11;
            int ImportCol_OFUP11 = DataModel_OFUP_History.getColFromVariable("UNABLE_TO_CONTACT");
            Renderer_OFUP11.setCustomComponent(ImportCol_OFUP11, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setSelected(false);
            tblOFUP_History.getColumnModel().getColumn(ImportCol_OFUP11).setCellEditor(new DefaultCellEditor(aCheckBox));
            tblOFUP_History.getColumnModel().getColumn(ImportCol_OFUP11).setCellRenderer(Renderer_OFUP11);

            tblOFUP_History.getColumnModel().getColumn(0).setMaxWidth(80);
            tblOFUP_History.getColumnModel().getColumn(1).setMaxWidth(80);
            tblOFUP_History.getColumnModel().getColumn(2).setMinWidth(100);
            tblOFUP_History.getColumnModel().getColumn(3).setMinWidth(0);
            tblOFUP_History.getColumnModel().getColumn(3).setMaxWidth(0);
            tblOFUP_History.getColumnModel().getColumn(4).setMinWidth(120);
            tblOFUP_History.getColumnModel().getColumn(5).setMinWidth(120);
            tblOFUP_History.getColumnModel().getColumn(6).setMinWidth(120);
            tblOFUP_History.getColumnModel().getColumn(7).setMinWidth(80);
            tblOFUP_History.getColumnModel().getColumn(8).setMinWidth(150);
            tblOFUP_History.getColumnModel().getColumn(9).setMinWidth(100);
            tblOFUP_History.getColumnModel().getColumn(10).setMinWidth(60);
            tblOFUP_History.getColumnModel().getColumn(11).setMinWidth(100);
            tblOFUP_History.getColumnModel().getColumn(12).setMinWidth(170);
            tblOFUP_History.getColumnModel().getColumn(13).setMinWidth(0);
            tblOFUP_History.getColumnModel().getColumn(13).setMaxWidth(0);
            tblOFUP_History.getColumnModel().getColumn(14).setMinWidth(150);
            tblOFUP_History.getColumnModel().getColumn(15).setMinWidth(150);
            tblOFUP_History.getColumnModel().getColumn(16).setMinWidth(150);
            tblOFUP_History.getColumnModel().getColumn(17).setMinWidth(0);
            tblOFUP_History.getColumnModel().getColumn(18).setMaxWidth(0);
            tblOFUP_History.getColumnModel().getColumn(18).setMinWidth(150);
            tblOFUP_History.getColumnModel().getColumn(19).setMinWidth(230);
            tblOFUP_History.getColumnModel().getColumn(20).setMinWidth(280);
            tblOFUP_History.getColumnModel().getColumn(21).setMinWidth(230);
            tblOFUP_History.getColumnModel().getColumn(22).setMinWidth(280);
            tblOFUP_History.getColumnModel().getColumn(23).setMinWidth(100);
            tblOFUP_History.getColumnModel().getColumn(24).setMinWidth(280);
            tblOFUP_History.getColumnModel().getColumn(25).setMinWidth(100);

            DataModel_OFUP_History.SetReadOnly(0);
            DataModel_OFUP_History.SetReadOnly(1);
            DataModel_OFUP_History.SetReadOnly(2);
            DataModel_OFUP_History.SetReadOnly(3);
            DataModel_OFUP_History.SetReadOnly(4);
            DataModel_OFUP_History.SetReadOnly(5);
            DataModel_OFUP_History.SetReadOnly(6);
            DataModel_OFUP_History.SetReadOnly(7);
            DataModel_OFUP_History.SetReadOnly(8);
            DataModel_OFUP_History.SetReadOnly(9);
            DataModel_OFUP_History.SetReadOnly(10);
            DataModel_OFUP_History.SetReadOnly(11);
            DataModel_OFUP_History.SetReadOnly(12);
            DataModel_OFUP_History.SetReadOnly(13);
            DataModel_OFUP_History.SetReadOnly(14);
            DataModel_OFUP_History.SetReadOnly(15);
            DataModel_OFUP_History.SetReadOnly(16);
            DataModel_OFUP_History.SetReadOnly(17);
            DataModel_OFUP_History.SetReadOnly(18);
            DataModel_OFUP_History.SetReadOnly(19);
            DataModel_OFUP_History.SetReadOnly(20);
            DataModel_OFUP_History.SetReadOnly(21);
            DataModel_OFUP_History.SetReadOnly(22);
            DataModel_OFUP_History.SetReadOnly(23);
            DataModel_OFUP_History.SetReadOnly(24);
            DataModel_OFUP_History.SetReadOnly(25);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getQuarter(int cDay, int month) {
        String qtr_name = "";

        if (month == 1 && cDay >= 11) {
            qtr_name = "Q4";
        } else if (month == 1 && cDay < 10) {
            qtr_name = "Q4";
        } else if (month == 2 && cDay >= 11) {
            qtr_name = "Q1";
        } else if (month == 2 && cDay < 10) {
            qtr_name = "Q4";
        } else if (month == 3 && cDay >= 11) {
            qtr_name = "Q1";
        } else if (month == 3 && cDay < 10) {
            qtr_name = "Q1";
        } else if (month == 4 && cDay >= 11) {
            qtr_name = "Q1";
        } else if (month == 4 && cDay < 10) {
            qtr_name = "Q1";
        } else if (month == 5 && cDay >= 11) {
            qtr_name = "Q2";
        } else if (month == 5 && cDay < 10) {
            qtr_name = "Q1";
        } else if (month == 6 && cDay >= 11) {
            qtr_name = "Q2";
        } else if (month == 6 && cDay < 10) {
            qtr_name = "Q2";
        } else if (month == 7 && cDay >= 11) {
            qtr_name = "Q2";
        } else if (month == 7 && cDay < 10) {
            qtr_name = "Q2";
        } else if (month == 8 && cDay >= 11) {
            qtr_name = "Q3";
        } else if (month == 8 && cDay < 10) {
            qtr_name = "Q2";
        } else if (month == 9 && cDay >= 11) {
            qtr_name = "Q3";
        } else if (month == 9 && cDay < 10) {
            qtr_name = "Q3";
        } //else 

//        if (month == 1) {
//            qtr_name = "Q4";
//        } else 
//            
//         
//            
//            if (month == 3) {
//            qtr_name = "Q4";
//        } else if (month == 4) {
//            qtr_name = "Q1";
//        } else if (month == 5) {
//            qtr_name = "Q1";
//        } else if (month == 6) {
//            qtr_name = "Q1";
//        } else if (month == 7) {
//            qtr_name = "Q2";
//        } else if (month == 8) {
//            qtr_name = "Q2";
//        } else if (month == 9) {
//            qtr_name = "Q2";
//        } else if (month == 10) {
//            qtr_name = "Q3";
//        } else if (month == 11) {
//            qtr_name = "Q3";
//        } else if (month == 12) {
//            qtr_name = "Q3";
//        }
        return qtr_name;
    }

    public void getTickCountEffects() {
        int tickCnt = 0, cntdvrs = 0;
        try {
            for (int m = 0; m < Table_OrderFUP.getRowCount(); m++) {
                if (DataModel_OrderFUP.getBoolValueByVariable("SELECT_IND", m)) {
                    tickCnt++;
                    if (!DataModel_OrderFUP.getValueByVariable("AUTO_DIVERSION_PIECES", m).equals("")) {
                        cntdvrs++;
                    }
                }
            }
            btnPieceDiv_OrderFUP.setEnabled(false);
            btnBooking_OrderFUP.setEnabled(false);
            if (tickCnt == 0) {
                btnPieceDiv_OrderFUP.setEnabled(false);
                btnBooking_OrderFUP.setEnabled(false);
            }
            if (tickCnt == 1 && cntdvrs == 1) {
                btnPieceDiv_OrderFUP.setEnabled(true);
                btnBooking_OrderFUP.setEnabled(false);
            }
            if (cntdvrs > 1) {
                btnPieceDiv_OrderFUP.setEnabled(false);
                btnBooking_OrderFUP.setEnabled(false);
            }
            if (tickCnt >= 1 && cntdvrs == 0 && !txtOrderFUPpartycode.getText().toString().trim().equals("")) {
                btnPieceDiv_OrderFUP.setEnabled(false);
                btnBooking_OrderFUP.setEnabled(true);
            }
            if (tickCnt > 1 && cntdvrs == 1) {
                btnPieceDiv_OrderFUP.setEnabled(false);
                btnBooking_OrderFUP.setEnabled(false);
            }

            //till multiple booking not done
//            if (tickCnt > 1) {
//                btnPieceDiv_OrderFUP.setEnabled(false);
//                btnBooking_OrderFUP.setEnabled(false);
//            }
        } catch (Exception e) {

        }
    }

    private void ExpectedCPRS() {
        try {
            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
            rsData1 = stmt1.executeQuery("SELECT UPN,CPRS_MONTH,COUNT(CPRS_MONTH) AS CPRS_CNT,EXPECTED_CPRS,COUNT(EXPECTED_CPRS) AS EXPECTED_CNT "
                    + "FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL WHERE CPRS_MONTH!=EXPECTED_CPRS GROUP BY UPN,CPRS_MONTH,EXPECTED_CPRS");
            rsData1.first();

            if (rsData1.getRow() > 0) {
                
                data.Execute("INSERT INTO PRODUCTION.FELT_ORDER_EXPECTED_CPRS_CHANGE_DATA (DOC_NO, SR_NO, DOC_MONTH, DOC_YEAR, SELECT_IND, ORDER_STATUS, UPN, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, PARTY_CODE, PARTY_NAME, CPRS_MONTH, CPRS_MONTH_DATE, OPRS_MONTH, OPRS_MONTH_DATE, INCHARGE, UNABLE_TO_CONTACT, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, CONTACTED_NO, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, NEW_FOLLOW_UP_DATE, DELAY_REASON, FOLLOW_UP_DATE, ASSIGNED_CATEGORY, CPRS_QUARTER, CPRS_OPEN_DATE, CPRS_CLOSE_DATE, PRODUCT_CATEGORY, SELECTED_DIVERSION_PIECE, EXPECTED_CPRS, EXPECTED_CPRS_REASON, ENTRY_RUNTIME) "
                        + "SELECT DOC_NO, SR_NO, DOC_MONTH, DOC_YEAR, SELECT_IND, ORDER_STATUS, UPN, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, PARTY_CODE, PARTY_NAME, CPRS_MONTH, CPRS_MONTH_DATE, OPRS_MONTH, OPRS_MONTH_DATE, INCHARGE, UNABLE_TO_CONTACT, DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, CONTACTED_NO, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, NEW_FOLLOW_UP_DATE, DELAY_REASON, FOLLOW_UP_DATE, ASSIGNED_CATEGORY, CPRS_QUARTER, CPRS_OPEN_DATE, CPRS_CLOSE_DATE, PRODUCT_CATEGORY, SELECTED_DIVERSION_PIECE, EXPECTED_CPRS, EXPECTED_CPRS_REASON, NOW() "
                        + "FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL WHERE CPRS_MONTH!=EXPECTED_CPRS ");
                
                while (!rsData1.isAfterLast()) {
                    String pUPN = rsData1.getString("UPN");
                    String pCPRS = rsData1.getString("CPRS_MONTH");
                    String pCPRSCnt = rsData1.getString("CPRS_CNT");
                    String pExpected = rsData1.getString("EXPECTED_CPRS");
                    String pExpectedCnt = rsData1.getString("EXPECTED_CNT");

                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL OFP, PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER OPR "
                            + "SET OPR.PLANNED_REQUIREMENT_SCHEDULE=OFP.EXPECTED_CPRS "
                            + "WHERE OFP.UPN = OPR.UPN AND OFP.DUMMY_PIECE_NO=OPR.DUMMY_PIECE_NO AND OFP.CPRS_MONTH!=OFP.EXPECTED_CPRS "
                            + "AND OFP.UPN='" + pUPN + "' AND OFP.CPRS_MONTH='" + pCPRS + "' AND OFP.EXPECTED_CPRS='" + pExpected + "' ");

                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL OFP, PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER OPR "
                            + "SET OPR.PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=COALESCE(LAST_DAY(STR_TO_DATE(CONCAT('00 - ',OPR.PLANNED_REQUIREMENT_SCHEDULE), '%d-%b-%Y')),'0000-00-00') "
                            + "WHERE OFP.UPN = OPR.UPN AND OFP.DUMMY_PIECE_NO=OPR.DUMMY_PIECE_NO AND OFP.CPRS_MONTH!=OFP.EXPECTED_CPRS "
                            + "AND OFP.UPN='" + pUPN + "' AND OFP.CPRS_MONTH='" + pCPRS + "' AND OFP.EXPECTED_CPRS='" + pExpected + "' ");

                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER, PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                            + "SET FUP_CPRS_OPEN_DATE=DATE_SUB(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 2 MONTH), INTERVAL DAY(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 2 MONTH))-11 DAY), "
                            + "FUP_CPRS_CLOSE_DATE=DATE_SUB(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 1 MONTH), INTERVAL DAY(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 1 MONTH))-10 DAY) "
                            + "WHERE UPN=MM_UPN_NO AND MM_GRUP NOT IN ('SDF') AND UPN='" + pUPN + "' ");

                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER, PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                            + "SET FUP_CPRS_OPEN_DATE=DATE_SUB(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 1 MONTH), INTERVAL DAY(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 1 MONTH))-11 DAY), "
                            + "FUP_CPRS_CLOSE_DATE=DATE_SUB(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 0 MONTH), INTERVAL DAY(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 0 MONTH))-10 DAY) "
                            + "WHERE UPN=MM_UPN_NO AND MM_GRUP IN ('SDF') AND UPN='" + pUPN + "' ");

                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER, PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                            + "SET PRS_QUARTER = CASE WHEN FUP_CPRS_OPEN_DATE>='2022-02-11' AND FUP_CPRS_OPEN_DATE<='2022-05-10' THEN 'Q1' "
                            + "WHEN FUP_CPRS_OPEN_DATE>='2022-05-11' AND FUP_CPRS_OPEN_DATE<='2022-08-10' THEN 'Q2' "
                            + "WHEN FUP_CPRS_OPEN_DATE>='2022-08-11' AND FUP_CPRS_OPEN_DATE<='2022-11-10' THEN 'Q3' "
                            + "WHEN FUP_CPRS_OPEN_DATE>='2022-11-11' AND FUP_CPRS_OPEN_DATE<='2023-02-10' THEN 'Q4' END "
                            + "WHERE UPN=MM_UPN_NO AND MM_GRUP NOT IN ('SDF') AND UPN='" + pUPN + "' ");

                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER, PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                            + "SET PRS_QUARTER = CASE WHEN FUP_CPRS_OPEN_DATE>='2022-03-11' AND FUP_CPRS_OPEN_DATE<='2022-06-10' THEN 'Q1' "
                            + "WHEN FUP_CPRS_OPEN_DATE>='2022-06-11' AND FUP_CPRS_OPEN_DATE<='2022-09-10' THEN 'Q2' "
                            + "WHEN FUP_CPRS_OPEN_DATE>='2022-09-11' AND FUP_CPRS_OPEN_DATE<='2022-12-10' THEN 'Q3' "
                            + "WHEN FUP_CPRS_OPEN_DATE>='2022-12-11' AND FUP_CPRS_OPEN_DATE<='2023-03-10' THEN 'Q4' END "
                            + "WHERE UPN=MM_UPN_NO AND MM_GRUP IN ('SDF') AND UPN='" + pUPN + "' ");

                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL OFP, PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER OPR "
                            + "SET OFP.CPRS_MONTH=OFP.EXPECTED_CPRS, OFP.CPRS_MONTH_DATE=COALESCE(LAST_DAY(STR_TO_DATE(CONCAT('00 - ',OFP.EXPECTED_CPRS), '%d-%b-%Y')),'0000-00-00'), "
                            + "OFP.CPRS_QUARTER=OPR.PRS_QUARTER, OFP.FOLLOW_UP_DATE=CURDATE() "
                            + "WHERE OFP.UPN = OPR.UPN AND OFP.DUMMY_PIECE_NO=OPR.DUMMY_PIECE_NO AND OFP.CPRS_MONTH!=OFP.EXPECTED_CPRS "
                            + "AND OFP.UPN='" + pUPN + "' AND OFP.CPRS_MONTH='" + pCPRS + "' AND OFP.EXPECTED_CPRS='" + pExpected + "' ");

                    data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL BGT, (SELECT UPN,PARTY_CODE,PARTY_NAME,COUNT(CPRS_MONTH), "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Apr - 2022' THEN 1 ELSE 0 END,0)) AS APR_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='May - 2022' THEN 1 ELSE 0 END,0)) AS MAY_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Jun - 2022' THEN 1 ELSE 0 END,0)) AS JUN_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Jul - 2022' THEN 1 ELSE 0 END,0)) AS JUL_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Aug - 2022' THEN 1 ELSE 0 END,0)) AS AUG_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Sep - 2022' THEN 1 ELSE 0 END,0)) AS SEP_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Oct - 2022' THEN 1 ELSE 0 END,0)) AS OCT_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Nov - 2022' THEN 1 ELSE 0 END,0)) AS NOV_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Dec - 2022' THEN 1 ELSE 0 END,0)) AS DEC_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Jan - 2023' THEN 1 ELSE 0 END,0)) AS JAN_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Feb - 2023' THEN 1 ELSE 0 END,0)) AS FEB_CNT, "
                            + "SUM(COALESCE(CASE WHEN CPRS_MONTH ='Mar - 2023' THEN 1 ELSE 0 END,0)) AS MAR_CNT "
                            + "FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
                            + "WHERE UPN='" + pUPN + "' GROUP BY UPN,PARTY_CODE,PARTY_NAME) AS OFP "
                            + "SET BGT.APR_BUDGET=OFP.APR_CNT, BGT.MAY_BUDGET=OFP.MAY_CNT, BGT.JUN_BUDGET=OFP.JUN_CNT, "
                            + "BGT.JUL_BUDGET=OFP.JUL_CNT, BGT.AUG_BUDGET=OFP.AUG_CNT, BGT.SEP_BUDGET=OFP.SEP_CNT, "
                            + "BGT.OCT_BUDGET=OFP.OCT_CNT, BGT.NOV_BUDGET=OFP.NOV_CNT, BGT.DEC_BUDGET=OFP.DEC_CNT, "
                            + "BGT.JAN_BUDGET=OFP.JAN_CNT, BGT.FEB_BUDGET=OFP.FEB_CNT, BGT.MAR_BUDGET=OFP.MAR_CNT "
//                            + "WHERE BGT.UPN=OFP.UPN AND BGT.UPN='" + pUPN + "' AND BGT.DOC_NO LIKE ('N2223%') ");
                            + "WHERE BGT.UPN=OFP.UPN AND BGT.UPN='" + pUPN + "' AND BGT.DOC_NO LIKE ('B2223%') AND COALESCE(BGT.APPROVED,0)=0 AND COALESCE(BGT.CANCELED,0)=0  ");

                    rsData1.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
