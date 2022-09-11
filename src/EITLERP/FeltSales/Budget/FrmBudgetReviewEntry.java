/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Budget;

import EITLERP.BigEdit;
import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.SelectFirstFree;
import EITLERP.Loader;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.clsAuthority;
import EITLERP.clsDepartment;
import EITLERP.clsDocFlow;
import EITLERP.clsFirstFree;
import EITLERP.clsHierarchy;
import EITLERP.clsUser;
import EITLERP.data;
import EITLERP.frmPendingApprovals;
import com.mysql.jdbc.RowData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Dharmendra PRAJAPATI
 *
 */
public class FrmBudgetReviewEntry extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbModuleModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;
    private EITLTableModel DataModel, DataModelParty;
    private EITLTableCellRenderer CellAlign = new EITLTableCellRenderer();
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;
    private int ModuleId = 834;
    private String DOC_NO = "";
    private clsBudgetReviewEntry BudgetEntry;
    private EITLComboModel cmbSendToModel;
    private int FinalApprovedBy = 0;
    private int mnoofmachine = 0;
    String cellLastValue = "";
    String seleval = "", seltyp = "", selqlt = "", selshd = "", selpiece = "", selext = "", selinv = "", selsz = "";
    private int mlstrc;
    private HashMap mmonth, change1, change2, change3;
    private int mlabelno = 0;
    private int mlabelnonxt = 0;

    String machineno = "";

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateForDB = new SimpleDateFormat("yyyy-MM-dd");

    JScrollPane[] scrollpane = new JScrollPane[100];
    JTable[] tblPress = new JTable[100];
    JLabel[] lblppos = new JLabel[100];
    JLabel[] lblpqlt = new JLabel[100];
    JLabel[] lblpyear = new JLabel[100];

    JTable[] tblPressnxt = new JTable[100];
    JLabel[] lblpposnxt = new JLabel[100];
    JLabel[] lblpqltnxt = new JLabel[100];
    JLabel[] lblpyearnxt = new JLabel[100];
    final EITLTableModel[] DataModel_Press = new EITLTableModel[100];

    final EITLTableModel[] DataModel_Pressnxt = new EITLTableModel[100];

    JTable[] tblDryer = new JTable[100];
    JLabel[] lbldpos = new JLabel[100];
    JLabel[] lbldqlt = new JLabel[100];
    JLabel[] lbldyear = new JLabel[100];

    JTable[] tblDryernxt = new JTable[100];
    JLabel[] lbldposnxt = new JLabel[100];
    JLabel[] lbldqltnxt = new JLabel[100];
    JLabel[] lbldyearnxt = new JLabel[100];
    final EITLTableModel[] DataModel_dryer = new EITLTableModel[100];

    final EITLTableModel[] DataModel_dryernxt = new EITLTableModel[100];

    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    public frmPendingApprovals frmPA;

    @Override
    public void init() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        initComponents();

        //cmdimport.setVisible(false);
        GenerateCombos();
//        FormatGrid();
        GenerateFromCombo();
        //GenerateHierarchyCombo();

        SetMenuForRights();
        DefaultSettings();
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
        markascomplete.setVisible(false);

        BudgetEntry = new clsBudgetReviewEntry();
        boolean load = BudgetEntry.LoadData(EITLERPGLOBAL.gCompanyID);
        if (load) {
            DisplayData();
            MoveLast();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + BudgetEntry.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        SetupApproval();
        mnoofmachine = 0;
        SetFields(false);

    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    public void DefaultSettings() {

        //String data = toString();
//        Object[] rowData = new Object[15];
//        rowData[0] = "1";
//        DataModel.addRow(rowData);
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
        //lblTitle1.setBackground(new Color(0, 102, 153));
        lblTitle1.setForeground(Color.WHITE);
    }

    private void clearFields() {

        //JOptionPane.showMessageDialog(null, "Data Model size : "+DataModel.getRowCount());
        FormatGridApprovalStatus();
        FormatGridUpdateHistory();
        FormatGrid();
        // FormatGridA();
        FormatGridHS();
        txtyearfrom.setText("");
        lblyearto.setText("");

//        for (int i = 0; i < DataModel.getRowCount(); i++) {
//            DataModel.removeRow(i);
//        }
//        if (DataModel.getRowCount() > 0) {
//            DataModel.removeRow(0);
//        }
//        Object[] rowData = new Object[15];
//        rowData[0] = 1;
//        DataModel.addRow(rowData);
    }

    private void DisplayData() {

        //=========== Color Indication ===============//
        try {
            btnSendFAmail.setEnabled(false);
            if (BudgetEntry.getAttribute("APPROVED").getInt() == 1) {
                lblTitle1.setBackground(Color.BLUE);
                lblTitle1.setForeground(Color.WHITE);
                btnSendFAmail.setEnabled(true);
            }

            if (BudgetEntry.getAttribute("APPROVED").getInt() == 0) {
                lblTitle1.setBackground(Color.GRAY);
                lblTitle1.setForeground(Color.WHITE);
            }

            if (BudgetEntry.getAttribute("CANCELED").getInt() == 1) {
                lblTitle1.setBackground(Color.RED);
                lblTitle1.setForeground(Color.BLACK);
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

        if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString())) {
            markascomplete.setVisible(false);
        } else {
            markascomplete.setVisible(true);
        }

        clearFields();
        mmonth = new HashMap();
        mmonth.put(1, "JAN");
        mmonth.put(2, "FEB");
        mmonth.put(3, "MAR");
        mmonth.put(4, "APR");
        mmonth.put(5, "MAY");
        mmonth.put(6, "JUN");
        mmonth.put(7, "JUL");
        mmonth.put(8, "AUG");
        mmonth.put(9, "SEP");
        mmonth.put(10, "OCT");
        mmonth.put(11, "NOV");
        mmonth.put(12, "DEC");
        change1 = new HashMap();
        change1.put(1, 89);
        change1.put(2, 96);
        change1.put(3, 103);
        change1.put(4, 26);
        change1.put(5, 33);
        change1.put(6, 40);
        change1.put(7, 47);
        change1.put(8, 54);
        change1.put(9, 61);
        change1.put(10, 68);
        change1.put(11, 75);
        change1.put(12, 82);

        int setvalblnk = 0, setvalblnknxt = 0;
        try {
            DOC_NO = BudgetEntry.getAttribute("DOC_NO").getString();
            lblTitle1.setText("Budget Review  - " + DOC_NO);
            DOC_NO1.setText(DOC_NO);
            setvalblnk = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H WHERE YEAR_FROM=CONCAT('20',SUBSTRING(DOC_NO,2,2)) AND DOC_NO='" + DOC_NO + "'");
            setvalblnknxt = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL_H WHERE YEAR_FROM=CONCAT('20',SUBSTRING(DOC_NO,4,2)) AND DOC_NO='" + DOC_NO + "'");
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) BudgetEntry.getAttribute("HIERARCHY_ID").getVal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mnoofmachine = 0;
        try {
            tabTest.removeAll();

            JPanel machines = new JPanel();
            ResultSet r, r1;
            //final int mcurmonth = data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL");

            final int mcurmonth = Integer.parseInt(DOC_NO1.getText().substring(5, 7));
            final int mchange = Integer.parseInt(change1.get(mcurmonth).toString());
            int nxt = 0;
            if (mcurmonth == 2 || mcurmonth == 3) {
                nxt = 0;
            }
            //int mcurmonth = 4;
            r = data.getResult("SELECT D.*,COALESCE(MRF_PROJECTION,0) AS MRF FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D WHERE YEAR_FROM=CONCAT('20',SUBSTRING(DOC_NO,2,2)) AND DOC_NO='" + DOC_NO1.getText().trim() + "' ORDER BY MACHINE_NO,POSITION_NO");
            r.first();

            r1 = data.getResult("SELECT D.*,COALESCE(MRF_PROJECTION,0) AS MRF  "
                    //                    + "CEILING(COALESCE(COALESCE(MRF_TOT_EXP_ACT_RUN_DAYS,0)/COALESCE(AVG_LIFE,0),0)) AS MRF "
                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL D "
                    //                    + "LEFT JOIN (SELECT PARTY_CODE,MACHINE_NO,MRF_TOT_EXP_ACT_RUN_DAYS FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE YEAR_FROM=2021 AND ) AS  M "
                    //                    + "ON D.PARTY_CODE=M.PARTY_CODE AND D.MACHINE_NO=M.MACHINE_NO "
                    + " WHERE D.YEAR_FROM=CONCAT('20',SUBSTRING(D.DOC_NO,4,2)) AND D.DOC_NO='" + DOC_NO1.getText().trim() + "' "
                    + "ORDER BY D.MACHINE_NO,D.POSITION_NO");

            r1.first();
            String mmachine = "";
            txtpartycode.setText(r.getString("PARTY_CODE"));
            txtpartyname.setText(r.getString("PARTY_NAME"));
            txtyearfrom.setText(r.getString("YEAR_FROM"));
            lblyearto.setText(r.getString("YEAR_TO"));
            String mfrmdt, mtodt;
            mfrmdt = "20" + (Integer.parseInt(DOC_NO1.getText().trim().substring(1, 3)) - 1) + "-04-01";
            mtodt = "20" + (Integer.parseInt(DOC_NO1.getText().trim().substring(3, 5)) - 1) + "-04-01";
            ResultSet pi;
            pi = data.getResult("SELECT PARTY_CODE,SUM(NO_OF_PIECES) AS INVQTY,ROUND(SUM(INVOICE_AMT),2) AS INVAMT,ROUND(SUM(ACTUAL_WEIGHT),2) AS WEIGHT "
                    + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H "
                    + "WHERE COALESCE(APPROVED,0)=1 AND COALESCE(CANCELLED,0)=0 AND "
                    + " INVOICE_DATE>='" + mfrmdt + "'  AND INVOICE_DATE<='" + mtodt + "' "
                    + "AND PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ) "
                    + "AND PARTY_CODE='" + txtpartycode.getText() + "' "
                    + "GROUP BY PARTY_CODE "
                    + "UNION ALL "
                    + "SELECT FELT_AMEND_PARTY_CODE,COUNT(FELT_AMEND_PIECE_NO),ROUND(SUM(FELT_AMEND_EXPORT_INV_AMT),2),ROUND(SUM(FELT_AMEND_WEIGHT),2) FROM PRODUCTION.FELT_PIECE_AMEND "
                    + "WHERE FELT_AMEND_REASON =8 AND FELT_AMEND_EXPORT_INV_DATE >= '" + mfrmdt + "' AND FELT_AMEND_EXPORT_INV_DATE<='" + mtodt + "' "
                    + "AND FELT_AMEND_PARTY_CODE='" + txtpartycode.getText() + "' "
                    + "GROUP BY FELT_AMEND_PARTY_CODE");

            pi.first();
            Object[] rowDatap = new Object[100];
            if (pi.getRow() > 0) {

                rowDatap[0] = "Total Sale Previous Year";
                rowDatap[1] = pi.getString("INVQTY");
                rowDatap[2] = pi.getString("WEIGHT");
                rowDatap[3] = pi.getString("INVAMT");
                DataModelParty.addRow(rowDatap);
            } else {
                rowDatap[0] = "Total Sale Previous Year";
                rowDatap[1] = 0;
                rowDatap[2] = 0;
                rowDatap[3] = 0;
                DataModelParty.addRow(rowDatap);
            }
            pi = data.getResult("SELECT SUM(ACTUAL_BUDGET) AS ABUDGET,SUM(ACTUAL_BUDGET_VALUE) AS AVALUE,"
                    + "round(sum(coalesce(ACTUAL_BUDGET,0)*(coalesce(PRESS_WEIGHT,0)+coalesce(DRY_WEIGHT,0))),2) as AWEIGHT "
                    + " FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE DOC_NO='" + DOC_NO + "' AND YEAR_FROM=" + txtyearfrom.getText());

            pi.first();

            rowDatap = new Object[100];
            rowDatap[0] = "Total Budget Current Year";
            rowDatap[1] = pi.getString("ABUDGET");
            rowDatap[2] = pi.getString("AWEIGHT");
            rowDatap[3] = pi.getString("AVALUE");
            DataModelParty.addRow(rowDatap);

            pi = data.getResult("SELECT SUM(CURRENT_PROJECTION) AS CBUDGET,SUM(CURRENT_PROJECTION_VALUE) AS CVALUE,"
                    + "round(sum(coalesce(CURRENT_PROJECTION,0)*(coalesce(PRESS_WEIGHT,0)+coalesce(DRY_WEIGHT,0))),2) as CWEIGHT "
                    + " FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE DOC_NO='" + DOC_NO + "' AND YEAR_FROM=" + txtyearfrom.getText());

            pi.first();

            rowDatap = new Object[100];
            rowDatap[0] = "Total Sale Projection Current Year";
            rowDatap[1] = pi.getString("CBUDGET");
            rowDatap[2] = pi.getString("CWEIGHT");
            rowDatap[3] = pi.getString("CVALUE");
            DataModelParty.addRow(rowDatap);

            final TableColumnModel cm = Tablepartyinfo.getColumnModel();
            for (int column = 0; column < Tablepartyinfo.getColumnCount(); column++) {
                int width = 100; // Min width
                for (int row = 0; row < Tablepartyinfo.getRowCount(); row++) {
                    TableCellRenderer renderer = Tablepartyinfo.getCellRenderer(row, column);
                    Component comp = Tablepartyinfo.prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 10, width);
                }
                if (width > 300) {
                    width = 300;
                }
                cm.getColumn(column).setPreferredWidth(width);
            }
            int i = 0;

            mlabelno = 0;
            lblppos[mlabelno] = new JLabel("Position : ");
            lbldpos[mlabelno] = new JLabel("Position : ");
            lblpqlt[mlabelno] = new JLabel("Quality : ");
            lbldqlt[mlabelno] = new JLabel("Quality : ");

            lblpposnxt[mlabelno] = new JLabel("Position : ");
            lbldposnxt[mlabelno] = new JLabel("Position : ");
            lblpqltnxt[mlabelno] = new JLabel("Quality : ");
            lbldqltnxt[mlabelno] = new JLabel("Quality : ");

            mmachine = r.getString("MACHINE_NO");
            machineno = mmachine;
            machines = new JPanel();
            machines.setLayout(null);
            tabTest.removeAll();
            int dcurmonth = Integer.parseInt(DOC_NO1.getText().substring(5, 7));
            String ddoc = "MRF" + DOC_NO1.getText().substring(1, 5);
            String con = " AND Q_1_EXP_RD+Q_2_EXP_RD+Q_3_EXP_RD+Q_4_EXP_RD!=";
            switch (dcurmonth) {
                case 5:
                    ddoc = ddoc + "Q1%";
                    con = con + "Q_1_EXP_RD+Q_2_EXP_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 6:
                    ddoc = ddoc + "Q1%";
                    con = con + "Q_1_EXP_RD+Q_2_EXP_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 7:
                    ddoc = ddoc + "Q1%";
                    con = con + "Q_1_EXP_RD+Q_2_EXP_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 8:
                    ddoc = ddoc + "Q2%";
                    con = con + "Q_1_ACT_RD+Q_2_EXP_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 9:
                    ddoc = ddoc + "Q2%";
                    con = con + "Q_1_ACT_RD+Q_2_EXP_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 10:
                    ddoc = ddoc + "Q2%";
                    con = con + "Q_1_ACT_RD+Q_2_EXP_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 11:
                    ddoc = ddoc + "Q3%";
                    con = con + "Q_1_ACT_RD+Q_2_ACT_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 12:
                    ddoc = ddoc + "Q3%";
                    con = con + "Q_1_ACT_RD+Q_2_ACT_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 1:
                    ddoc = ddoc + "Q3%";
                    con = con + "Q_1_ACT_RD+Q_2_ACT_RD+Q_3_EXP_RD+Q_4_EXP_RD";
                    break;
                case 2:
                    ddoc = ddoc + "Q4%";
                    con = con + "Q_1_ACT_RD+Q_2_ACT_RD+Q_3_ACT_RD+Q_4_EXP_RD";
                    break;
                case 3:
                    ddoc = ddoc + "Q4%";
                    con = con + "Q_1_ACT_RD+Q_2_ACT_RD+Q_3_ACT_RD+Q_4_EXP_RD";
                    break;
                case 4:
                    ddoc = ddoc + "Q4%";
                    con = con + "Q_1_ACT_RD+Q_2_ACT_RD+Q_3_ACT_RD+Q_4_EXP_RD";
                    break;
            }
            int chngmrf = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "WHERE DOC_NO LIKE '" + ddoc + "' " + con + " AND PARTY_CODE=" + txtpartycode.getText() + " "
                    + "AND MACHINE_NO='" + r.getString("MACHINE_NO") + "'");
            if (chngmrf > 0) {
                tabTest.add("Machine " + r.getString("MACHINE_NO") + " [MRF CHANGE]", machines);
            } else {
                tabTest.add("Machine " + r.getString("MACHINE_NO"), machines);
            }

            tblPress[i] = new JTable();
            tblPressnxt[i] = new JTable();

            //scrollpane[i]=new JScrollPane();
            //scrollpane[i].add(tblPress[i]);
            DataModel_Press[i] = new EITLTableModel();
            tblPress[i].removeAll();
            tblPress[i].setModel(DataModel_Press[i]);

            DataModel_Pressnxt[i] = new EITLTableModel();
            tblPressnxt[i].removeAll();
            tblPressnxt[i].setModel(DataModel_Pressnxt[i]);
            //tblDryer.setBounds(10, 10, 500, 100);

            tblPress[i].setAutoResizeMode(0);
            tblPress[i].setRowSelectionAllowed(true);
            tblPress[i].setEnabled(true);

            tblPressnxt[i].setAutoResizeMode(0);
            tblPressnxt[i].setRowSelectionAllowed(true);
            tblPressnxt[i].setEnabled(true);

            TableGenerate(DataModel_Press[i], tblPress[i], i, "PRESS", "", DataModel_Pressnxt[i]);
            if (nxt == 1) {
                TableGenerate(DataModel_Pressnxt[i], tblPressnxt[i], i, "PRESS", "NEXT", DataModel_Pressnxt[i]);
            }
            final int final_i = i;

            JScrollPane jScrollPane2 = new JScrollPane();
            jScrollPane2.setViewportView(tblPress[i]);

            machines.add(jScrollPane2);
            jScrollPane2.setBounds(0, 40, 1250, 80);

            JScrollPane jScrollPane2nxt = new JScrollPane();
            jScrollPane2nxt.setViewportView(tblPressnxt[i]);

            machines.add(jScrollPane2nxt);
            jScrollPane2nxt.setBounds(0, 120, 1250, 80);

//            jScrollPane2.getVerticalScrollBar().setModel(
//                    jScrollPane2nxt.getVerticalScrollBar().getModel());
            Object[] rowData = new Object[300];//20

            tblDryer[i] = new JTable();
            tblDryernxt[i] = new JTable();

            tblDryer[i].setRowSelectionAllowed(true);
            DataModel_dryer[i] = new EITLTableModel();
            tblDryer[i].removeAll();
            tblDryer[i].setModel(DataModel_dryer[i]);
            tblDryer[i].setAutoResizeMode(0);

            tblDryernxt[i].setRowSelectionAllowed(true);
            DataModel_dryernxt[i] = new EITLTableModel();
            tblDryernxt[i].removeAll();
            tblDryernxt[i].setModel(DataModel_dryernxt[i]);
            tblDryernxt[i].setAutoResizeMode(0);

            TableGenerate(DataModel_dryer[i], tblDryer[i], i, "DRY", "", DataModel_dryernxt[i]);
            if (nxt == 1) {
                TableGenerate(DataModel_dryernxt[i], tblDryernxt[i], i, "DRY", "NEXT", DataModel_dryernxt[i]);
            }

            JScrollPane jScrollPane3 = new JScrollPane();
            jScrollPane3.setViewportView(tblDryer[i]);

            jScrollPane3.setBounds(0, 240, 1250, 80);
            machines.add(jScrollPane3);

            JScrollPane jScrollPane3nxt = new JScrollPane();
            jScrollPane3nxt.setViewportView(tblDryernxt[i]);

            jScrollPane3nxt.setBounds(0, 320, 1250, 80);
            machines.add(jScrollPane3nxt);

            JLabel lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Press");
            lblMachine.setBounds(0, 0, 200, 25);
            machines.add(lblMachine);
            lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Dryer");
            lblMachine.setBounds(0, 200, 200, 25);
            machines.add(lblMachine);

            if (nxt == 1) {
                JLabel lblyear = new JLabel("Year " + r.getString("YEAR_FROM") + "-" + r.getString("YEAR_TO") + " ");//Press
                lblyear.setBounds(950, 0, 200, 25);
                machines.add(lblyear);
                lblyear = new JLabel("Year " + r1.getString("YEAR_FROM") + "-" + r1.getString("YEAR_TO") + " ");//Press
                lblyear.setBounds(950, 15, 200, 25);
                machines.add(lblyear);
                lblyear = new JLabel("Year " + r.getString("YEAR_FROM") + "-" + r.getString("YEAR_TO") + " ");//Dryer
                lblyear.setBounds(950, 200, 200, 25);
                machines.add(lblyear);
                lblyear = new JLabel("Year " + r1.getString("YEAR_FROM") + "-" + r1.getString("YEAR_TO") + " ");//Dryer
                lblyear.setBounds(950, 215, 200, 25);
                machines.add(lblyear);
            }

            lblppos[mlabelno].setBounds(150, 0, 200, 25);
            machines.add(lblppos[mlabelno]);
            lbldpos[mlabelno].setBounds(150, 200, 200, 25);
            machines.add(lbldpos[mlabelno]);
            lblpqlt[mlabelno].setBounds(550, 0, 200, 25);
            machines.add(lblpqlt[mlabelno]);
            lbldqlt[mlabelno].setBounds(550, 200, 200, 25);
            machines.add(lbldqlt[mlabelno]);

            if (nxt == 1) {
                lblpposnxt[mlabelno].setBounds(150, 15, 200, 25);
                machines.add(lblpposnxt[mlabelno]);
                lbldposnxt[mlabelno].setBounds(150, 215, 200, 25);
                machines.add(lbldposnxt[mlabelno]);
                lblpqltnxt[mlabelno].setBounds(550, 15, 200, 25);
                machines.add(lblpqltnxt[mlabelno]);
                lbldqltnxt[mlabelno].setBounds(550, 215, 200, 25);
                machines.add(lbldqltnxt[mlabelno]);
            }

            int msr = 1;
            int msr1 = 1, pos = 0;

            while (!r.isAfterLast()) {
                if (mmachine.equalsIgnoreCase(r.getString("MACHINE_NO"))) {
                    rowData = new Object[300];
                    if (r.getString("GROUP_NAME").equalsIgnoreCase("HDS") || r.getString("GROUP_NAME").equalsIgnoreCase("SDF")) {
                        DataModel_dryer[i].addRow(AddRow(r, msr1, mcurmonth, setvalblnk));
                        if (nxt == 1) {
                            DataModel_dryernxt[i].addRow(AddRow(r1, msr1, mcurmonth, setvalblnk));
                        }
                        msr1++;

                    } else {
                        DataModel_Press[i].addRow(AddRow(r, msr, mcurmonth, setvalblnk));
                        if (nxt == 1) {
                            DataModel_Pressnxt[i].addRow(AddRow(r1, msr, mcurmonth, setvalblnk));
                        }
                        msr++;

                    }
                } else {
                    i++;
                    mlabelno++;
                    lblppos[mlabelno] = new JLabel("Position : ");
                    lbldpos[mlabelno] = new JLabel("Position : ");
                    lblpqlt[mlabelno] = new JLabel("Quality : ");
                    lbldqlt[mlabelno] = new JLabel("Quality : ");

                    lblpposnxt[mlabelno] = new JLabel("Position : ");
                    lbldposnxt[mlabelno] = new JLabel("Position : ");
                    lblpqltnxt[mlabelno] = new JLabel("Quality : ");
                    lbldqltnxt[mlabelno] = new JLabel("Quality : ");
                    JPanel machine = new JPanel();
                    machine = new JPanel();
                    machine.setLayout(null);

                    chngmrf = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                            + "WHERE DOC_NO LIKE '" + ddoc + "' " + con + " AND PARTY_CODE=" + txtpartycode.getText() + " "
                            + "AND MACHINE_NO='" + r.getString("MACHINE_NO") + "'");
                    if (chngmrf > 0) {
                        tabTest.add("Machine " + r.getString("MACHINE_NO") + " [MRF CHANGE]", machine);
                    } else {
                        tabTest.add("Machine " + r.getString("MACHINE_NO"), machine);
                    }

                    tblPress[i] = new JTable();
                    DataModel_Press[i] = new EITLTableModel();

                    tblPress[i].removeAll();
                    tblPress[i].setModel(DataModel_Press[i]);
                    tblPress[i].setAutoResizeMode(0);

                    tblPressnxt[i] = new JTable();
                    DataModel_Pressnxt[i] = new EITLTableModel();

                    tblPressnxt[i].removeAll();
                    tblPressnxt[i].setModel(DataModel_Pressnxt[i]);
                    tblPressnxt[i].setAutoResizeMode(0);

                    TableGenerate(DataModel_Press[i], tblPress[i], i, "PRESS", "", DataModel_Pressnxt[i]);
                    if (nxt == 1) {
                        TableGenerate(DataModel_Pressnxt[i], tblPressnxt[i], i, "PRESS", "NEXT", DataModel_Pressnxt[i]);
                    }

                    jScrollPane2 = new JScrollPane();
                    jScrollPane2.setViewportView(tblPress[i]);
                    machine.add(jScrollPane2);
                    jScrollPane2.setBounds(0, 40, 1250, 80);

                    jScrollPane2nxt = new JScrollPane();
                    jScrollPane2nxt.setViewportView(tblPressnxt[i]);
                    machine.add(jScrollPane2nxt);
                    jScrollPane2nxt.setBounds(0, 120, 1250, 80);

                    tblDryer[i] = new JTable();
                    DataModel_dryer[i] = new EITLTableModel();
                    tblDryer[i].removeAll();
                    tblDryer[i].setModel(DataModel_dryer[i]);
                    tblDryer[i].setAutoResizeMode(0);

                    tblDryernxt[i] = new JTable();
                    DataModel_dryernxt[i] = new EITLTableModel();
                    tblDryernxt[i].removeAll();
                    tblDryernxt[i].setModel(DataModel_dryernxt[i]);
                    tblDryernxt[i].setAutoResizeMode(0);

                    TableGenerate(DataModel_dryer[i], tblDryer[i], i, "DRY", "", DataModel_dryernxt[i]);
                    if (nxt == 1) {
                        TableGenerate(DataModel_dryernxt[i], tblDryernxt[i], i, "DRY", "NEXT", DataModel_dryernxt[i]);
                    }

                    jScrollPane3 = new JScrollPane();
                    jScrollPane3.setViewportView(tblDryer[i]);
                    jScrollPane3.setBounds(0, 260, 1250, 80);
                    machine.add(jScrollPane3);

                    jScrollPane3nxt = new JScrollPane();
                    jScrollPane3nxt.setViewportView(tblDryernxt[i]);
                    jScrollPane3nxt.setBounds(0, 340, 1250, 80);
                    machine.add(jScrollPane3nxt);

                    lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Press");
                    lblMachine.setBounds(0, 0, 200, 25);
                    machine.add(lblMachine);
                    lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Dryer");
                    lblMachine.setBounds(0, 200, 200, 25);
                    machine.add(lblMachine);
                    if (nxt == 1) {
                        JLabel lblyear = new JLabel("Year " + r.getString("YEAR_FROM") + "-" + r.getString("YEAR_TO") + " ");//Press
                        lblyear.setBounds(950, 0, 200, 25);
                        machine.add(lblyear);
                        lblyear = new JLabel("Year " + r1.getString("YEAR_FROM") + "-" + r1.getString("YEAR_TO") + " ");//Press
                        lblyear.setBounds(950, 15, 200, 25);
                        machine.add(lblyear);
                        lblyear = new JLabel("Year " + r.getString("YEAR_FROM") + "-" + r.getString("YEAR_TO") + " ");//Dryer
                        lblyear.setBounds(950, 200, 200, 25);
                        machine.add(lblyear);
                        lblyear = new JLabel("Year " + r1.getString("YEAR_FROM") + "-" + r1.getString("YEAR_TO") + " ");//Dryer
                        lblyear.setBounds(950, 215, 200, 25);
                        machine.add(lblyear);
                    }

                    lblppos[mlabelno].setBounds(150, 5, 200, 25);
                    machine.add(lblppos[mlabelno]);
                    lbldpos[mlabelno].setBounds(150, 200, 200, 25);
                    machine.add(lbldpos[mlabelno]);

                    lblpqlt[mlabelno].setBounds(550, 0, 200, 25);
                    machine.add(lblpqlt[mlabelno]);
                    lbldqlt[mlabelno].setBounds(550, 200, 200, 25);
                    machine.add(lbldqlt[mlabelno]);
                    if (nxt == 1) {
                        lblpposnxt[mlabelno].setBounds(150, 15, 200, 25);
                        machine.add(lblpposnxt[mlabelno]);
                        lbldposnxt[mlabelno].setBounds(150, 215, 200, 25);
                        machine.add(lbldposnxt[mlabelno]);
                        lblpqltnxt[mlabelno].setBounds(550, 15, 200, 25);
                        machine.add(lblpqltnxt[mlabelno]);
                        lbldqltnxt[mlabelno].setBounds(550, 215, 200, 25);
                        machine.add(lbldqltnxt[mlabelno]);
                    }
                    mmachine = r.getString("MACHINE_NO");
                    msr = msr1 = 1;
                    rowData = new Object[300];
                    if (r.getString("GROUP_NAME").equalsIgnoreCase("HDS") || r.getString("GROUP_NAME").equalsIgnoreCase("SDF")) {
                        DataModel_dryer[i].addRow(AddRow(r, msr1, mcurmonth, setvalblnk));
                        if (nxt == 1) {
                            DataModel_dryernxt[i].addRow(AddRow(r1, msr1, mcurmonth, setvalblnk));
                        }
                        msr1++;
                    } else {
                        DataModel_Press[i].addRow(AddRow(r, msr, mcurmonth, setvalblnk));
                        if (nxt == 1) {
                            DataModel_Pressnxt[i].addRow(AddRow(r1, msr, mcurmonth, setvalblnk));
                        }
                        msr++;
                    }
                }
                r.next();
                r1.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SetFields(false);
        try {
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = BudgetEntry.getAttribute("DOC_NO").getString();
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
            //HashMap History = BudgetEntry.getHistoryList(EITLERPGLOBAL.gCompanyID + "", DocNo);
            HashMap History = clsBudgetReviewEntry.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsBudgetReviewEntry ObjHistory = (clsBudgetReviewEntry) History.get(Integer.toString(i));
                Object[] rowData = new Object[6];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = EITLERPGLOBAL.formatDateTime(ObjHistory.getAttribute("ENTRY_DATE").getString());

                String ApprovalStatus = "";

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                    FinalApprovedBy = (int) ObjHistory.getAttribute("UPDATED_BY").getVal();
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
                if ((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("H")) {
                    ApprovalStatus = "Hold";
                }
                rowData[3] = ApprovalStatus;
                rowData[4] = ObjHistory.getAttribute("APPROVER_REMARKS").getString();
                rowData[5] = ObjHistory.getAttribute("FROM_IP").getString();
                DataModelUpdateHistory.addRow(rowData);
            }
            //============================================================//
            //setSTATUS();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Object[] AddRow(ResultSet r, int msr1, int mcurmonth, int setvalblnk) {
        Object[] rowData = new Object[300];
        try {
            int pos = 0;
            rowData[pos] = msr1;
            pos++;
            rowData[pos] = r.getString("UPN");
            pos++;
            rowData[pos] = r.getString("POSITION_DESC");
            pos++;
            rowData[pos] = r.getString("QUALITY_NO");
            pos++;
            rowData[pos] = r.getString("GROUP_NAME");
            pos++;
            rowData[pos] = r.getDouble("SELLING_PRICE");
            pos++;
            rowData[pos] = r.getDouble("CURRENT_PROJECTION");
            pos++;
            rowData[pos] = r.getDouble("CURRENT_PROJECTION_VALUE");
            pos++;
            rowData[pos] = r.getDouble("CURRENT_PROJECTION") + r.getDouble("PREV_GR_QTY");
            pos++;
            rowData[pos] = r.getDouble("CURRENT_PROJECTION_VALUE") + r.getDouble("PREV_GR_VALUE");
            pos++;
            rowData[pos] = r.getDouble("ACTUAL_BUDGET");
            pos++;
            rowData[pos] = r.getDouble("ACTUAL_BUDGET_VALUE");
            pos++;
            rowData[pos] = r.getDouble("DISPATCH_QTY");
            pos++;
            rowData[pos] = r.getDouble("DISPATCH_VALUE");
            pos++;
            rowData[pos] = r.getDouble("PREV_GR_QTY");
            pos++;
            rowData[pos] = r.getDouble("PREV_GR_VALUE");
            pos++;

            rowData[pos] = r.getDouble("WIP_QTY") + r.getDouble("NCE_WIP_P_YEAR");
            pos++;
            rowData[pos] = (r.getDouble("WIP_QTY") + r.getDouble("NCE_WIP_P_YEAR")) * r.getDouble("SELLING_PRICE");
            pos++;
            rowData[pos] = r.getDouble("STOCK_QTY") + r.getDouble("NCE_STK_P_YEAR");
            pos++;
            rowData[pos] = (r.getDouble("STOCK_VALUE") + r.getDouble("NCE_STK_P_YEAR")) * r.getDouble("SELLING_PRICE");
            pos++;
            rowData[pos] = r.getDouble("OBSOLETE_QTY");
            pos++;
            rowData[pos] = r.getDouble("OBSOLETE_VALUE");
            pos++;
            rowData[pos] = r.getDouble("ACESS_QTY");
            pos++;
            rowData[pos] = r.getDouble("ACESS_VALUE");
            pos++;
            rowData[pos] = r.getDouble("PROJ_OF_ORDER_QTY");
            pos++;
            rowData[pos] = r.getDouble("PROJ_OF_ORDER_VALUE");
            pos++;

            for (int mn = 4; mn <= 12; mn++) {
                if (mn == mcurmonth && setvalblnk == 0 && r.getDouble(mmonth.get(mn) + "_BUDGET") == 0) {
                    //System.out.println("BLANK" + mn);
                } else {
                    rowData[pos] = r.getDouble(mmonth.get(mn) + "_BUDGET");
                }
                pos++;
                rowData[pos] = r.getDouble(mmonth.get(mn) + "_NET_AMOUNT");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_DOUBTFUL_QTY");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK_DOUBTFUL");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL");
                pos++;
            }
            for (int mn = 1; mn <= 3; mn++) {
                if (mn == mcurmonth && setvalblnk == 0 && r.getDouble(mmonth.get(mn) + "_BUDGET") == 0) {
                    //System.out.println("BLANK" + mn);
                } else {
                    rowData[pos] = r.getDouble(mmonth.get(mn) + "_BUDGET");
                }
                pos++;
                rowData[pos] = r.getDouble(mmonth.get(mn) + "_NET_AMOUNT");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_DOUBTFUL_QTY");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK_DOUBTFUL");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC");
                pos++;
                rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL");
                pos++;
            }

            rowData[pos] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='" + r.getString("INCHARGE") + "'");
            pos++;
            rowData[pos] = r.getString("SIZE_CRITERIA");
            pos++;
            rowData[pos] = r.getString("PARTY_GROUP");
            pos++;
            rowData[pos] = r.getString("CANCEL_QTY");
            pos++;
            rowData[pos] = r.getString("CANCEL_VALUE");
            pos++;
            rowData[pos] = r.getString("OBSOLETE_WIP_QTY");
            pos++;
            rowData[pos] = r.getString("OBSOLETE_WIP_VALUE");
            pos++;
            rowData[pos] = r.getString("OBSOLETE_STOCK_QTY");
            pos++;
            rowData[pos] = r.getString("OBSOLETE_STOCK_VALUE");
            pos++;
            rowData[pos] = r.getDouble("CE_PC");
            pos++;
            rowData[pos] = r.getDouble("NCE_WIP");
            pos++;
            rowData[pos] = r.getDouble("NCE_STK");
            pos++;
            rowData[pos] = r.getDouble("MRF");
            pos++;
            rowData[pos] = r.getDouble("NCE_WIP_P_YEAR");
            pos++;
            rowData[pos] = r.getDouble("NCE_STK_P_YEAR");
            pos++;
            rowData[pos] = r.getDouble("WIP_QTY");
            pos++;
            rowData[pos] = r.getDouble("WIP_VALUE");
            pos++;
            rowData[pos] = r.getDouble("STOCK_QTY");
            pos++;
            rowData[pos] = r.getDouble("STOCK_VALUE");
            pos++;
            rowData[pos] = r.getDouble("GOAL");
            pos++;
            rowData[pos] = r.getDouble("ADDTIONAL_SCOPE");
            pos++;
            rowData[pos] = r.getDouble("CURRENT_POTENTIAL");
            pos++;
            rowData[pos] = r.getDouble("NEW_GOAL");
            pos++;
            rowData[pos] = r.getDouble("CHANGE_QTY");
            pos++;
            rowData[pos] = r.getDouble("SCOPE");
            pos++;
            return rowData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowData;
    }

    private void TableGenerate(final EITLTableModel DataModel, final JTable Tabel, int i, final String type, final String mnext, final EITLTableModel DataModelNxt) {
        DataModel.addColumn("SrNo"); //0 - Read Only
        DataModel.addColumn("UPN"); //1
        DataModel.addColumn("Position Desc"); //2
        DataModel.addColumn("Quality"); //3
        DataModel.addColumn("Group"); //4
        DataModel.addColumn("Value"); //5
        DataModel.addColumn("CurrentProjQty"); //6
        DataModel.addColumn("CurrentProjValue"); //7
        DataModel.addColumn("CurrProj+GR Qty"); //6
        DataModel.addColumn("CurrProj+GR Value"); //7
        DataModel.addColumn("BudgetQty"); //8
        DataModel.addColumn("BudgetValue"); //9
        DataModel.addColumn("DispatchQty"); //10
        DataModel.addColumn("DispatchValue"); //11
        DataModel.addColumn("Prev GR Qty"); //12
        DataModel.addColumn("Prev GR Value"); //13
        if (mnext.equalsIgnoreCase("NEXT")) {
            DataModel.addColumn("WIPQty Projected"); //12
            DataModel.addColumn("WIPValue Projected"); //13
            DataModel.addColumn("In StockQty Projected"); //14
            DataModel.addColumn("In StockValue Projected"); //15
        } else {
            DataModel.addColumn("WIPQty"); //12
            DataModel.addColumn("WIPValue"); //13
            DataModel.addColumn("In StockQty"); //14
            DataModel.addColumn("In StockValue"); //15
        }
        DataModel.addColumn("ObsoleteQty"); //16
        DataModel.addColumn("ObsoleteValue"); //17
        DataModel.addColumn("ExcessQty"); //18
        DataModel.addColumn("ExcessValue"); //19

        DataModel.addColumn("ProjOfOrderQty"); //14//20
        DataModel.addColumn("ProjOfOrderValue"); //15//21

        for (int m = 4; m <= 12; m++) {
            DataModel.addColumn(mmonth.get(m) + "Qty");
            DataModel.addColumn(mmonth.get(m) + "Value");
            DataModel.addColumn(mmonth.get(m) + "DoubtfulQty");
            DataModel.addColumn(mmonth.get(m) + "Remark");
            DataModel.addColumn(mmonth.get(m) + " Remarks for Doubtful");
            DataModel.addColumn(mmonth.get(m) + " Proposed action by AIC");
            DataModel.addColumn(mmonth.get(m) + " Proposed action for Doubtful");

        }
        for (int m = 1; m <= 3; m++) {
            DataModel.addColumn(mmonth.get(m) + "Qty");
            DataModel.addColumn(mmonth.get(m) + "Value");
            DataModel.addColumn(mmonth.get(m) + "DoubtfulQty");
            DataModel.addColumn(mmonth.get(m) + "Remark");
            DataModel.addColumn(mmonth.get(m) + " Remarks for Doubtful");
            DataModel.addColumn(mmonth.get(m) + " Proposed action by AIC");
            DataModel.addColumn(mmonth.get(m) + " Proposed action for Doubtful");

        }
        DataModel.addColumn("Incharge"); //43
        DataModel.addColumn("Size Criteria"); //44
        DataModel.addColumn("Party Group"); //45
        DataModel.addColumn("Cancel Qty");
        DataModel.addColumn("Cancel Value");
        DataModel.addColumn("ObsoleteWIPQty");
        DataModel.addColumn("ObsoleteWIPValue");
        DataModel.addColumn("ObsoleteStkQty");
        DataModel.addColumn("ObsoleteStkValue");
        DataModel.addColumn("CE_PC");
        DataModel.addColumn("NCE_WIP");
        DataModel.addColumn("NCE_STK");
        DataModel.addColumn("MRF_PROJECTION");
        DataModel.addColumn("NCE_WIP_P_YEAR");
        DataModel.addColumn("NCE_STK_P_YEAR");
        DataModel.addColumn("WIPQty_A"); //12
        DataModel.addColumn("WIPValue_A"); //13
        DataModel.addColumn("In StockQty_A"); //14
        DataModel.addColumn("In StockValue_A"); //15       
        DataModel.addColumn("Goal"); //
        DataModel.addColumn("Additional Scope"); //
        DataModel.addColumn("Current Potential"); //
        DataModel.addColumn("New Goal"); //
        DataModel.addColumn("Change_Qty"); //
        DataModel.addColumn("Scope"); //

        if (mnext.equalsIgnoreCase("")) {
            for (int co = 123; co < 129; co++) {
                Tabel.getColumnModel().getColumn(co).setMinWidth(0);
                Tabel.getColumnModel().getColumn(co).setMaxWidth(0);
            }
        }

        final int mcurmonth = Integer.parseInt(DOC_NO1.getText().substring(5, 7));
        final int mchange = Integer.parseInt(change1.get(mcurmonth).toString());

        for (int ro = 0; ro < 135; ro++) {
            if (EditMode == EITLERPGLOBAL.EDIT) {
                if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString() + "")) {
                    if (ro == mchange || ro == mchange + 3 || ro == mchange + 5 || ro > 129) {
                        if (ro == 131 || ro == 132 || ro == 134) {
                            DataModel.SetReadOnly(ro);
                        }

                    } else {
                        if (ro == 129 && (mcurmonth == 2 || mcurmonth == 4)) {
                        } else {
                            DataModel.SetReadOnly(ro);
                        }
                    }
                } else {
                    if (ro == mchange || ro == mchange + 2 || ro == mchange + 3 || ro == mchange + 4 || ro == mchange + 5 || ro == mchange + 6 || ro > 129) {
                        if (ro == 131 || ro == 132 || ro == 134) {
                            DataModel.SetReadOnly(ro);
                        }
                    } else {
                        if (ro == 129 && (mcurmonth == 2 || mcurmonth == 4)) {
                        } else {
                            DataModel.SetReadOnly(ro);
                        }
                    }
                }
            } else {
                DataModel.SetReadOnly(ro);
            }
        }

        final TableColumnModel columnModel1 = Tabel.getColumnModel();
        for (int column = 0; column < Tabel.getColumnCount(); column++) {
            int width = 60; // Min width
            for (int row = 0; row < Tabel.getRowCount(); row++) {
                TableCellRenderer renderer = Tabel.getCellRenderer(row, column);
                Component comp = Tabel.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 10, width);
            }
            if (width > 300) {
                width = 300;
            }
            columnModel1.getColumn(column).setPreferredWidth(width);
        }

        final int final_i = i;
        Tabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                double mcb, mwip, mstk, mobs, macess, mdis, mcanwip, mcanstk, mcan;
                String s = "", cnd = "";
                for (int ji = 0; ji < DataModel.getRowCount(); ji++) {
                    double value = Double.parseDouble(DataModel.getValueAt(ji, 5).toString());
                    double pq = 0, pb = 0, mgoal = 0, mngoal = 0, maddscope = 0, mchangeqty;
                    mcb = mwip = mstk = mobs = macess = mdis = mcanwip = mcanstk = mcan = mchangeqty = 0;
                    if (mnext.equalsIgnoreCase("NEXT")) {
                        cnd = " ((PR_CURRENT_SCH_LAST_DDMMYY>='" + lblyearto.getText() + "-04-01' AND "
                                + "PR_CURRENT_SCH_LAST_DDMMYY<='" + (Integer.parseInt(lblyearto.getText()) + 1) + "-03-31') "
                                + "OR "
                                + "(PR_OC_LAST_DDMMYY>='" + lblyearto.getText() + "-04-01' AND "
                                + "PR_OC_LAST_DDMMYY<='" + (Integer.parseInt(lblyearto.getText()) + 1) + "-03-31') "
                                + "OR "
                                + "(PR_REQ_MTH_LAST_DDMMYY>='" + lblyearto.getText() + "-04-01' AND "
                                + "PR_REQ_MTH_LAST_DDMMYY<='" + (Integer.parseInt(lblyearto.getText()) + 1) + "-03-31') "
                                + ") ";
                    } else {
                        cnd = " ((PR_CURRENT_SCH_LAST_DDMMYY>='" + txtyearfrom.getText() + "-04-01' AND "
                                + "PR_CURRENT_SCH_LAST_DDMMYY<='" + lblyearto.getText() + "-03-31') "
                                + "OR "
                                + "(PR_OC_LAST_DDMMYY>='" + txtyearfrom.getText() + "-04-01' AND "
                                + "PR_OC_LAST_DDMMYY<='" + lblyearto.getText() + "-03-31') "
                                + "OR "
                                + "(PR_REQ_MTH_LAST_DDMMYY>='" + txtyearfrom.getText() + "-04-01' AND "
                                + "PR_REQ_MTH_LAST_DDMMYY<='" + lblyearto.getText() + "-03-31') "
                                + ") ";
                    }
                    try {
                        pq = Double.parseDouble(DataModel.getValueAt(ji, mchange).toString());
                    } catch (Exception nf) {
                        pq = -1;
                    }

                    if (pq >= 0) {
                        DataModel.setValueAt(value * pq, ji, mchange + 1);
                        DataModel.setValueAt(pq, ji, 6);
                    } else {
                        pb = Double.parseDouble(DataModel.getValueAt(ji, 10).toString());
                        DataModel.setValueAt(pb, ji, 6);
                    }
                    try {
                        mcb = Double.parseDouble(DataModel.getValueAt(ji, 6).toString());
                    } catch (Exception e) {
                        mcb = 0;
                    }
                    try {
                        mwip = Double.parseDouble(DataModel.getValueAt(ji, 16).toString());
                    } catch (Exception e) {
                        mwip = 0;
                    }
                    try {
                        mstk = Double.parseDouble(DataModel.getValueAt(ji, 18).toString());
                    } catch (Exception e) {
                        mstk = 0;
                    }
                    try {
                        mobs = Double.parseDouble(DataModel.getValueAt(ji, 20).toString());
                    } catch (Exception e) {
                        mobs = 0;
                    }
                    try {
                        mdis = Double.parseDouble(DataModel.getValueAt(ji, 12).toString());
                    } catch (Exception e) {
                        mdis = 0;
                    }
                    try {
                        mgoal = Double.parseDouble(DataModel.getValueAt(ji, 129).toString());
                    } catch (Exception e) {
                        mgoal = 0;
                    }
                    try {
                        mngoal = Double.parseDouble(DataModel.getValueAt(ji, 132).toString());
                    } catch (Exception e) {
                        mngoal = 0;
                    }
                    try {
                        maddscope = Double.parseDouble(DataModel.getValueAt(ji, 130).toString());
                    } catch (Exception e) {
                        maddscope = 0;
                    }
                    try {
                        mchangeqty = Double.parseDouble(DataModel.getValueAt(ji, 133).toString());
                    } catch (Exception e) {
                        mchangeqty = 0;
                    }

                    DataModel.setValueAt(mgoal + mngoal + maddscope, ji, 131);
                    DataModel.setValueAt(mgoal + mngoal + mchangeqty, ji, 134);
                    //System.out.println("Row " + ji + " Bud " + mcb + " wip " + mwip + " Dis " + mdis + "acess " + ((mwip + mstk + mdis) - mcb));
                    if (mcb - (mwip + mstk + mdis) < 0) {
                        macess = (mwip + mstk + mdis) - mcb;
                        DataModel.setValueAt(macess, ji, 22);
                        DataModel.setValueAt(value * macess, ji, 23);

                        s = "SELECT COUNT(*) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                                + "WHERE " + cnd + " AND PR_UPN='" + DataModel.getValueAt(ji, 1).toString() + "' AND "
                                + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN";
                        mcan = data.getIntValueFromDB(s);
                        if (mcan > macess) {
                            mcan = macess;
                        }
                        DataModel.setValueAt(mcan, ji, 119);
                        s = "SELECT COUNT(*) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                                + "WHERE " + cnd + " AND PR_UPN='" + DataModel.getValueAt(ji, 1).toString() + "' AND "
                                + "PR_PIECE_STAGE IN ('NEEDLING','MENDING','FINISHING','SEAMING','WEAVING','SPIRALLING','ASSEMBLY','HEAT_SETTING','MARKING','SPLICING') GROUP BY PR_UPN";

                        mcanwip = data.getIntValueFromDB(s);
                        if (mcanwip > (macess - mcan)) {
                            mcanwip = (macess - mcan);
                        }
                        DataModel.setValueAt(mcanwip, ji, 120);

                        s = "SELECT COUNT(*) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                                + "WHERE " + cnd + " AND PR_UPN='" + DataModel.getValueAt(ji, 1).toString() + "' AND "
                                + "PR_PIECE_STAGE IN ('IN STOCK','BSR','DIVERTED_FNSG_STOCK') GROUP BY PR_UPN";
                        mcanstk = data.getIntValueFromDB(s);
                        if (mcanstk > (macess - mcan - mcanwip)) {
                            mcanstk = (macess - mcan - mcanwip);
                        }
                        DataModel.setValueAt(mcanstk, ji, 121);
                        if (mnext.equalsIgnoreCase("")) {
                            try {
                                mwip = Double.parseDouble(DataModelNxt.getValueAt(ji, 125).toString());
                            } catch (Exception e) {
                                mwip = 0;
                            }
                            try {
                                mstk = Double.parseDouble(DataModelNxt.getValueAt(ji, 127).toString());
                            } catch (Exception e) {
                                mstk = 0;
                            }
                            DataModelNxt.setValueAt(mcanwip, ji, 123);
                            DataModelNxt.setValueAt(mcanstk, ji, 124);
                            DataModelNxt.setValueAt(mwip + mcanwip, ji, 16);
                            DataModelNxt.setValueAt((mwip + mcanwip) * value, ji, 17);
                            DataModelNxt.setValueAt(mstk + mcanstk, ji, 18);
                            DataModelNxt.setValueAt((mstk + mcanstk) * value, ji, 19);
                        } else {

                        }
//                            mcanv = data.getDoubleValueFromDB("SELECT SUM(PR_FELT_VALUE_WITH_GST) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                    + "WHERE PR_UPN='" + DataModel.getValueAt(ji, 1).toString() + "' AND "
//                                    + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                            DataModel.setValueAt(mcanv, ji, 114);
                    } else {
                        macess = 0;
                        DataModel.setValueAt(macess, ji, 22);
                        DataModel.setValueAt(value * macess, ji, 23);
                        DataModel.setValueAt(macess, ji, 119);
                        DataModel.setValueAt(macess, ji, 120);
                        DataModel.setValueAt(macess, ji, 121);
                        if (mnext.equalsIgnoreCase("")) {
                            DataModelNxt.setValueAt("0.00", ji, 123);
                            DataModelNxt.setValueAt("0.00", ji, 124);
                        }
                    }
                    DataModel.setValueAt(value * Double.parseDouble(DataModel.getValueAt(ji, 6).toString()), ji, 7);
                    DataModel.setValueAt((mcb + macess) - (mwip + mstk + mdis), ji, 24);
                    //DataModel.setValueAt(Double.parseDouble(DataModel.getValueAt(ji, 6).toString()) - (Double.parseDouble(DataModel.getValueAt(ji, 10).toString()) + Double.parseDouble(DataModel.getValueAt(ji, 12).toString()) + Double.parseDouble(DataModel.getValueAt(ji, 14).toString())), ji, 20);
                    DataModel.setValueAt(value * Double.parseDouble(DataModel.getValueAt(ji, 24).toString()), ji, 25);
                }
                if (type.equalsIgnoreCase("PRESS")) {
                    if (mnext.equalsIgnoreCase("NEXT")) {
                        lblpposnxt[tabTest.getSelectedIndex()].setText("Position :" + DataModel.getValueAt(Tabel.getSelectedRow(), 2).toString());
                        lblpqltnxt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel.getValueAt(Tabel.getSelectedRow(), 3).toString());
                    } else {
                        lblppos[tabTest.getSelectedIndex()].setText("Position :" + DataModel.getValueAt(Tabel.getSelectedRow(), 2).toString());
                        lblpqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel.getValueAt(Tabel.getSelectedRow(), 3).toString());
                    }
                }
                if (type.equalsIgnoreCase("DRY")) {
                    if (mnext.equalsIgnoreCase("NEXT")) {
                        lbldposnxt[tabTest.getSelectedIndex()].setText("Position :" + DataModel.getValueAt(Tabel.getSelectedRow(), 2).toString());
                        lbldqltnxt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel.getValueAt(Tabel.getSelectedRow(), 3).toString());
                    } else {
                        lbldpos[tabTest.getSelectedIndex()].setText("Position :" + DataModel.getValueAt(Tabel.getSelectedRow(), 2).toString());
                        lbldqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel.getValueAt(Tabel.getSelectedRow(), 3).toString());
                    }
                }
            }
        });
        Tabel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if (type.equalsIgnoreCase("PRESS")) {
                    if (mnext.equalsIgnoreCase("NEXT")) {
                        lblpposnxt[tabTest.getSelectedIndex()].setText("Position :" + DataModel.getValueAt(Tabel.getSelectedRow(), 2).toString());
                        lblpqltnxt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel.getValueAt(Tabel.getSelectedRow(), 3).toString());
                    } else {
                        lblppos[tabTest.getSelectedIndex()].setText("Position :" + DataModel.getValueAt(Tabel.getSelectedRow(), 2).toString());
                        lblpqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel.getValueAt(Tabel.getSelectedRow(), 3).toString());
                    }
                }
                if (type.equalsIgnoreCase("DRY")) {
                    if (mnext.equalsIgnoreCase("NEXT")) {
                        lbldposnxt[tabTest.getSelectedIndex()].setText("Position :" + DataModel.getValueAt(Tabel.getSelectedRow(), 2).toString());
                        lbldqltnxt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel.getValueAt(Tabel.getSelectedRow(), 3).toString());
                    } else {
                        lbldpos[tabTest.getSelectedIndex()].setText("Position :" + DataModel.getValueAt(Tabel.getSelectedRow(), 2).toString());
                        lbldqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel.getValueAt(Tabel.getSelectedRow(), 3).toString());
                    }
                }
            }
        });
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 600073, 6000731)) { //7008,70081
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 600073, 6000732)) { //7008,70082
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 600073, 6000733)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 600073, 6000734)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            //cmdPreview.setEnabled(false);
            //cmdPrint.setEnabled(false);
        }
    }

    private void SetupApproval() {
        /*// --- Hierarchy Change Rights Check --------
         if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,75)) {
         cmbHierarchy.setEnabled(true);
         }else {
         cmbHierarchy.setEnabled(false);
         }*/

        // select hold for default approval
        OpgHold.setSelected(true);

        if (EditMode == EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
            OpgReject.setEnabled(false);
        } else {
            cmbHierarchy.setEnabled(false);
        }

        //Set Default Hierarchy ID for User
        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);

        if (EditMode == EITLERPGLOBAL.ADD) {
            //lnFromUserId = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {

            int FromUserID = clsFeltProductionApprovalFlow.getFromID(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString());
            //lnFromUserId = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = clsFeltProductionApprovalFlow.getFromRemarks(ModuleId, FromUserID, BudgetEntry.getAttribute("DOC_NO").getString());

            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        if (clsHierarchy.CanSkip(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }

        if (clsHierarchy.CanFinalApprove(EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        } else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }

        //In Edit Mode Hierarchy and Reject Should be disabled
        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString() + "")) {
                OpgReject.setEnabled(false);
            }
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

    private void FormatGrid() {
        DataModelParty = new EITLTableModel();

        Tablepartyinfo.removeAll();
        Tablepartyinfo.setModel(DataModelParty);

        //Set the table Readonly
        DataModelParty.TableReadOnly(true);

        //Add the columns
        DataModelParty.addColumn("Description");
        DataModelParty.addColumn("Qty");
        DataModelParty.addColumn("Kgs");
        DataModelParty.addColumn("Value");

        Tablepartyinfo.setAutoResizeMode(Tablepartyinfo.AUTO_RESIZE_OFF);
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
            e.printStackTrace();
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
        jLabel17 = new javax.swing.JLabel();
        lblStatus1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        DOC_NO1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtyearfrom = new javax.swing.JTextField();
        lblyearto = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtpartycode = new javax.swing.JTextField();
        txtpartyname = new javax.swing.JLabel();
        tabTest = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablepartyinfo = new javax.swing.JTable();
        markascomplete = new javax.swing.JCheckBox();
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
        cmdNextToTab3 = new javax.swing.JButton();
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
        lblTitle1 = new javax.swing.JLabel();

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

        jLabel17.setText("Budget Review Detail");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 60, 120, 20);

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(lblStatus1);
        lblStatus1.setBounds(0, 550, 920, 30);

        jLabel1.setText("Document No");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 10, 100, 20);

        DOC_NO1.setEditable(false);
        DOC_NO1.setText("BU000001");
        jPanel1.add(DOC_NO1);
        DOC_NO1.setBounds(100, 10, 160, 20);

        jLabel2.setText("Year");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(270, 10, 30, 20);

        txtyearfrom.setEditable(false);
        jPanel1.add(txtyearfrom);
        txtyearfrom.setBounds(300, 10, 60, 20);

        lblyearto.setText("YYYY");
        jPanel1.add(lblyearto);
        lblyearto.setBounds(360, 10, 50, 20);

        jLabel4.setText("Party Code");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 40, 70, 20);

        txtpartycode.setEditable(false);
        txtpartycode.setToolTipText("");
        jPanel1.add(txtpartycode);
        txtpartycode.setBounds(100, 30, 130, 20);

        txtpartyname.setText("Party Name");
        jPanel1.add(txtpartyname);
        txtpartyname.setBounds(230, 30, 410, 20);
        jPanel1.add(tabTest);
        tabTest.setBounds(10, 90, 1270, 460);

        Tablepartyinfo.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(Tablepartyinfo);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(660, 0, 560, 90);

        markascomplete.setText("Marked as Completed");
        jPanel1.add(markascomplete);
        markascomplete.setBounds(450, 10, 170, 30);

        Tab.addTab("Budget Review", jPanel1);

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
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
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
        cmdBackToTab0.setBounds(450, 340, 102, 23);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        Tab2.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(630, 95, 33, 21);

        cmdNextToTab3.setMnemonic('N');
        cmdNextToTab3.setText("Next >>");
        cmdNextToTab3.setToolTipText("Next Tab");
        cmdNextToTab3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextToTab3ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNextToTab3);
        cmdNextToTab3.setBounds(560, 340, 102, 23);

        btnSendFAmail.setText("Send final approved mail");
        btnSendFAmail.setEnabled(false);
        btnSendFAmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFAmailActionPerformed(evt);
            }
        });
        Tab2.add(btnSendFAmail);
        btnSendFAmail.setBounds(546, 10, 200, 23);

        jPanel2.add(Tab2);
        Tab2.setBounds(10, 0, 760, 410);

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

        jButton5.setText("<<Previous");
        jButton5.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        StatusPanel.add(jButton5);
        jButton5.setBounds(570, 290, 130, 30);

        jPanel3.add(StatusPanel);
        StatusPanel.setBounds(10, 0, 790, 380);

        Tab.addTab("Status", jPanel3);

        getContentPane().add(Tab);
        Tab.setBounds(0, 80, 1310, 620);

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

        lblTitle1.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle1.setText("Budget Review");
        lblTitle1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle1.setOpaque(true);
        getContentPane().add(lblTitle1);
        lblTitle1.setBounds(0, 40, 930, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged

        //SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //JOptionPane.showMessageDialog(null, "On State Change SelHierarchyId : "+SelHierarchyID);
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

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        lblStatus1.setText("Select the hierarchy for approval");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //JOptionPane.showMessageDialog(null, "SelHierarchyId : "+SelHierarchyID);
        DOC_NO = DOC_NO1.getText();
        cmbSendTo.setEnabled(true);
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
        //txtToRemarks.setEnabled(false);
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
        lblStatus1.setText("Select the approval action");
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
        Tab.setSelectedIndex(0);
    }//GEN-LAST:event_cmdBackToTab0ActionPerformed

    private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed

    }//GEN-LAST:event_cmdFromRemarksBigActionPerformed

    private void cmdNextToTab3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextToTab3ActionPerformed
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNextToTab3ActionPerformed

    private void Tab2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab2FocusGained

    }//GEN-LAST:event_Tab2FocusGained

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        String DocNo = DOC_NO1.getText();
        //BudgetEntry.ShowHistory(DocNo);
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        //BudgetEntry.HistoryView = false;
        //BudgetEntry.LoadData();
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        if (TableUpdateHistory.getRowCount() > 0 && TableUpdateHistory.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableUpdateHistory.getValueAt(TableUpdateHistory.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Tab.setSelectedIndex(1);
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

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void OpgRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpgRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpgRejectActionPerformed

    private void btnSendFAmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFAmailActionPerformed
        // TODO add your handling code here:
        //System.out.println("Sel Hierarchy : "+SelHierarchyID);

//        System.out.println("finishing approved = " + BudgetEntry.getAttribute("APPROVED").getInt());
//
//        if (BudgetEntry.getAttribute("APPROVED").getInt() == 1) {
//            int value = JOptionPane.showConfirmDialog(this, " Are you sure? You want to send Final Approved mail to all users? ", "Confirmation Alert!", JOptionPane.YES_NO_OPTION);
//            System.out.println("VALUE = " + value);
//            if (value == 0) {
//                try {
//                    String DOC_NO = DOC_NO1.getText();
//                    String DOC_DATE = BudgetEntry.getAttribute("CREATED_DATE").getString();
//                    String Party_Code = "multiple";
//                    int Hierarchy = (int) BudgetEntry.getAttribute("HIERARCHY_ID").getInt();
//
//                    System.out.println("ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, SelHierarchyID, true " + 603 + "," + DOC_NO + "," + DOC_DATE + "," + Party_Code + "," + EITLERPGLOBAL.gNewUserID + "," + Hierarchy + "," + true);
//                    System.out.println("Final Approved By : " + FinalApprovedBy);
//                    String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, FinalApprovedBy, Hierarchy, true, EITLERPGLOBAL.gNewUserID);
//                    System.out.println("Send Mail Responce : " + responce);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }//GEN-LAST:event_btnSendFAmailActionPerformed
    private void MoveFirst() {
        BudgetEntry.MoveFirst();
        DisplayData();
    }

    private void MovePrevious() {
        BudgetEntry.MovePrevious();
        DisplayData();
    }

    private void MoveNext() {
        BudgetEntry.MoveNext();
        DisplayData();
    }

    private void MoveLast() {
        BudgetEntry.MoveLast();
        DisplayData();
    }

    private void Find() {
//        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.Budget.frmFindFeltManualBudget", true);
//        frmFindFeltManualBudget ObjFindfeltPieceDivision = (frmFindFeltManualBudget) ObjLoader.getObj();
//
//        if (ObjFindfeltPieceDivision.Cancelled == false) {
//            if (!BudgetEntry.Filter(ObjFindfeltPieceDivision.stringFindQuery)) {
//                JOptionPane.showMessageDialog(this, "No records found.", "Find Felt Division", JOptionPane.YES_OPTION);
//            }
//            MoveLast();
//        }

        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.Budget.frmFindBudgetEntry", true);
        frmFindBudgetEntry ObjFindfeltPieceDivision = (frmFindBudgetEntry) ObjLoader.getObj();

        if (ObjFindfeltPieceDivision.Cancelled == false) {
            if (!BudgetEntry.Filter(ObjFindfeltPieceDivision.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.", "Find Felt Division", JOptionPane.YES_OPTION);
            }
            MoveLast();
        }
    }

    public void FindEx(int pCompanyID, String docno) {
        BudgetEntry.Filter(" DOC_NO='" + docno + "'");
        BudgetEntry.MoveFirst();
        DisplayData();
    }

    public void FindWaiting() {
        BudgetEntry.Filter(" DOC_NO IN (SELECT DISTINCT PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL, PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=" + ModuleId + " AND CANCELED=0) ");
        SetMenuForRights();
        DisplayData();
    }

    private void Add() {

//        if (!EITLERPGLOBAL.YearIsOpen) {
//            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
//            return;
//        }
//        EditMode = EITLERPGLOBAL.ADD;
////        cmdimport.setVisible(true);
//
//        SetFields(true);
//        DisableToolbar();
//
//        SetupApproval();
//
//        clearFields();
//
//        SelectFirstFree aList = new SelectFirstFree();
//        aList.ModuleID = ModuleId;
//        aList.FirstFreeNo = 236;
//        FFNo = aList.FirstFreeNo;
//        DOC_NO1.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, false));
//        DOC_NO1.setText( DOC_NO1.getText().substring(2));
//        lblTitle1.setText("Budget Manual Entry - " + DOC_NO1.getText());
//        txtyearfrom.requestFocus();
//        //import_data();
    }

    private void Save() {

        if (chk_data()) {
            //if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + txtpartycode.getText() + "' ")) {
            //     JOptionPane.showMessageDialog(null, "Party closed in Party Master.");
            //     return;
            // }

//            for (int j = 0; j < Table.getRowCount(); j++) {
//                String machineNo = ((String) Table.getValueAt(j, 3)).trim();
//                String positionNo = ((String) Table.getValueAt(j, 4)).trim();
//
//                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MACHINE_CLOSE_IND=1 AND MM_PARTY_CODE='" + txtpartycode.getText() + "' AND MM_MACHINE_NO='" + machineNo + "' ")) {
//                    JOptionPane.showMessageDialog(null, "Party Machine closed in Machine Master at Row : " + (j + 1));
//                    return;
//                } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE POSITION_CLOSE_IND=1 AND MM_PARTY_CODE='" + txtpartycode.getText() + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "' ")) {
//                    JOptionPane.showMessageDialog(null, "Party Machine Position closed in Machine Master at Row : " + (j + 1));
//                    return;
//                }
//
//            }
//
//            if (Table.getRowCount() <= 0) {
//                JOptionPane.showMessageDialog(this, "Enter Budget Details Before Saving.", "ERROR", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
            if (txtyearfrom.getText().trim().length() <= 0) {
                JOptionPane.showMessageDialog(this, "Please Enter Year...", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
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

            //BudgetEntry.LoadData();
            if (EditMode == EITLERPGLOBAL.ADD) {
                if (BudgetEntry.Insert()) {
                    SelectFirstFree aList = new SelectFirstFree();
                    aList.ModuleID = ModuleId;
                    aList.FirstFreeNo = 236;
                    FFNo = aList.FirstFreeNo;
                    clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleId, FFNo, true);

                    if (OpgFinal.isSelected()) {
                        try {

                            String DOC_NO = DOC_NO1.getText();
                            String DOC_DATE = EITLERPGLOBAL.getCurrentDate();
                            String Party_Code = "multiple";

                            //update_budget(DOC_NO);
                            String sql = "";
                            sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                                    + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='" + DOC_NO + "')";
                            System.out.println(sql);
                            data.Execute(sql);
                            sql = "INSERT INTO PRODUCTION.FELT_BUDGET "
                                    + "(YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,"
                                    + "PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,"
                                    + "DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,"
                                    + "Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,"
                                    + "Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,"
                                    + "WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,"
                                    + "AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA,Q4NET_AMOUNT)  "
                                    + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,"
                                    + "PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,"
                                    + "DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,"
                                    + "Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,"
                                    + "TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1, "
                                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,"
                                    + "WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,"
                                    + "LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA,Q4NET_AMOUNT"
                                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                                    + "WHERE DOC_NO='" + DOC_NO + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                            System.out.println(sql);
                            data.Execute(sql);

                            String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true, 0);
                            System.out.println("Send Mail Responce : " + responce);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    DisplayData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured while saving. Error is " + BudgetEntry.LastError, " SAVING ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (EditMode == EITLERPGLOBAL.EDIT) {
                if (BudgetEntry.Update()) {
                    if (OpgFinal.isSelected()) {
                        try {

                            String DOC_NO = DOC_NO1.getText();
                            String DOC_DATE = EITLERPGLOBAL.getCurrentDate();
                            String Party_Code = txtpartycode.getText();

                            //update_budget(DOC_NO);
                            String sql = "";
                            sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                                    + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE DOC_NO='" + DOC_NO + "')";
                            data.Execute(sql);
                            sql = "INSERT INTO PRODUCTION.FELT_BUDGET "
                                    + "(YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,"
                                    + "PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,"
                                    + "DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,"
                                    + "Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,"
                                    + "Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,"
                                    + "WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,"
                                    + "AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA,Q4NET_AMOUNT)  "
                                    + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,"
                                    + "PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,"
                                    + "DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,"
                                    + "Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,"
                                    + "TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1, "
                                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,"
                                    + "WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,"
                                    + "LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA,Q4NET_AMOUNT"
                                    + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                                    + "WHERE DOC_NO='" + DOC_NO + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                            System.out.println(sql);
                            data.Execute(sql);

                            String responce = JavaMail.sendFinalApprovalMail(ModuleId, DOC_NO, DOC_DATE, Party_Code, EITLERPGLOBAL.gNewUserID, EITLERPGLOBAL.getComboCode(cmbHierarchy), true, 0);
                            System.out.println("Send Mail Responce : " + responce);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    DisplayData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured while saving editing. Error is " + BudgetEntry.LastError, "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
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
//            else {
//            JOptionPane.showMessageDialog(this, "Invalid Status....");
//        }
    }

    private boolean chk_data() {
        boolean chk = true;
        String err = "", currmk;
        double ob, pb, cb, dq;
        //int mcurmonth = data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL");
        final int mcurmonth = Integer.parseInt(DOC_NO1.getText().substring(5, 7));
        if (OpgFinal.isSelected()) {
            String chkdata = data.getStringValueFromDB("SELECT CONCAT('B2021',RIGHT(100+MONTH(CURDATE()),2)) FROM DUAL");
            if (!DOC_NO1.getText().substring(0, 7).equalsIgnoreCase(chkdata)) {
                JOptionPane.showMessageDialog(this, "Can not Final Approved Before or After Current Month...");
                chk = false;
                return false;
            }
        }
        if (!clsFeltProductionApprovalFlow.IsCreator(ModuleId, DOC_NO1.getText())) {
            if (OpgApprove.isSelected() && !markascomplete.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please Select Mark as Complete before Approve", "Error", JOptionPane.ERROR_MESSAGE);
                chk = false;
                return false;
            }
        }
        //int mcurmonth = 4;
        mmonth = new HashMap();
        mmonth.put(1, "JAN");
        mmonth.put(2, "FEB");
        mmonth.put(3, "MAR");
        mmonth.put(4, "APR");
        mmonth.put(5, "MAY");
        mmonth.put(6, "JUN");
        mmonth.put(7, "JUL");
        mmonth.put(8, "AUG");
        mmonth.put(9, "SEP");
        mmonth.put(10, "OCT");
        mmonth.put(11, "NOV");
        mmonth.put(12, "DEC");
        change1 = new HashMap();
        change1.put(1, 89);
        change1.put(2, 96);
        change1.put(3, 103);
        change1.put(4, 26);
        change1.put(5, 33);
        change1.put(6, 40);
        change1.put(7, 47);
        change1.put(8, 54);
        change1.put(9, 61);
        change1.put(10, 68);
        change1.put(11, 75);
        change1.put(12, 82);
        int mchange = Integer.parseInt(change1.get(mcurmonth).toString());
        for (int p = 0; p < tabTest.getTabCount(); p++) {
            currmk = "";
            //tblPress[p].setEnabled(false);
            //tblDryer[p].setEnabled(false);
            err = err + CheckData(tblPress[p], p, "Press", "");
            err = err + CheckData(tblDryer[p], p, "Dryer", "");
           // err = err + CheckData(tblPressnxt[p], p, "Press", "NEXT");
           // err = err + CheckData(tblDryernxt[p], p, "Dryer", "NEXT");
        }
        if (!err.trim().equalsIgnoreCase("")) {
            chk = false;
            JOptionPane.showMessageDialog(this, err);
        }
        return chk;
    }

    private String CheckData(JTable Table, int p, String pos, String mnext) {
        String err = "", currmk = "", alert = "";
        double ob, pb, cb, dq, pyp, mmrf, mwip, mstk, mgoal;
        final int mcurmonth = Integer.parseInt(DOC_NO1.getText().substring(5, 7));
        change1 = new HashMap();
        change1.put(1, 89);
        change1.put(2, 96);
        change1.put(3, 103);
        change1.put(4, 26);
        change1.put(5, 33);
        change1.put(6, 40);
        change1.put(7, 47);
        change1.put(8, 54);
        change1.put(9, 61);
        change1.put(10, 68);
        change1.put(11, 75);
        change1.put(12, 82);
        int mchange = Integer.parseInt(change1.get(mcurmonth).toString());
        String myear = "";
        if (mnext.equalsIgnoreCase("NEXT")) {
            myear = " " + lblyearto.getText() + " - " + String.valueOf(Integer.parseInt(lblyearto.getText()) + 1) + " Year ";
        } else {
            myear = " " + txtyearfrom.getText() + " - " + lblyearto.getText() + " Year ";
        }
        for (int m = 0; m < Table.getRowCount(); m++) {
            if (chkposclose(Table.getValueAt(m, 1).toString())) {
                if (Double.parseDouble(Table.getValueAt(m, 12).toString()) != Double.parseDouble(Table.getValueAt(m, 6).toString())) {
                    err = err + "\n Position Closed so Dispatch Qty=Projected Qty For " + tabTest.getTitleAt(p) + " In " + pos + myear + " at Row :" + (m + 1);
                }
            } else {
                ob = pb = cb = mmrf = mwip = mstk = mgoal = 0;
//                try {
//                    mgoal = Double.parseDouble(Table.getValueAt(m, 129).toString());
//                } catch (Exception e) {
//                    mgoal = 0;
//                }
//                try {
//                    mgoal = mgoal + Double.parseDouble(Table.getValueAt(m, 132).toString());
//                } catch (Exception e) {
//                    mgoal = mgoal;
//                }
                try {
                    mgoal = Double.parseDouble(Table.getValueAt(m, 134).toString());
                } catch (Exception e) {
                    mgoal = 0;
                }
                try {
                    ob = Double.parseDouble(Table.getValueAt(m, 10).toString());
                } catch (Exception e) {
                    ob = 0;
                }
                try {
                    cb = Double.parseDouble(Table.getValueAt(m, 6).toString());
                } catch (Exception e) {
                    cb = 0;
                }
                try {
                    dq = Double.parseDouble(Table.getValueAt(m, 12).toString());
                } catch (Exception e) {
                    dq = 0;
                }
                try {
                    mwip = Double.parseDouble(Table.getValueAt(m, 16).toString());
                } catch (Exception e) {
                    mwip = 0;
                }
                try {
                    mstk = Double.parseDouble(Table.getValueAt(m, 18).toString());
                } catch (Exception e) {
                    mstk = 0;
                }
                try {
                    mmrf = Double.parseDouble(Table.getValueAt(m, 122).toString());
                } catch (Exception e) {
                    mmrf = 0;
                }

                try {
                    if (mcurmonth < 4) {
                        currmk = Table.getValueAt(m, mchange + 3).toString();
                    } else {
                        currmk = Table.getValueAt(m, mchange + 3).toString();
                    }
                    pb = getPreBudget(Table.getValueAt(m, 1).toString());
                    if (mcurmonth == 4) {
                        pb = Double.parseDouble(Table.getValueAt(m, 10).toString());
                    }
                } catch (Exception e) {
                    pb = 0;
                }
                if (mcurmonth == 2) {
                    if (mgoal <= 0) {

                    }
                }
                if (mnext.equalsIgnoreCase("NEXT")) {
                    if (pos.equalsIgnoreCase("Press")) {
                        try {
                            pyp = Double.parseDouble(tblPress[p].getValueAt(m, 6).toString());
                        } catch (Exception e) {
                            pyp = 0;
                        }
                    } else {
                        try {
                            pyp = Double.parseDouble(tblDryer[p].getValueAt(m, 6).toString());
                        } catch (Exception e) {
                            pyp = 0;
                        }
                    }
                    if (cb > 0 && pyp < 0) {
                        err = err + "\n Enter Current Year Projection then enter Next Year Projection For " + tabTest.getTitleAt(p) + " In " + pos + myear + " at Row :" + (m + 1);
                    }
                    if (mmrf > 0 && cb > mmrf) {
                        alert = alert + "\n Projection is more than MRF For " + tabTest.getTitleAt(p) + " In " + pos + myear + " at Row :" + (m + 1);
                    }

                }
                if (cb > mgoal) {
                    err = err + "\n Projection is more than Goal For " + tabTest.getTitleAt(p) + " In " + pos + myear + " at Row :" + (m + 1);
                }
                if (cb < dq) {
                    err = err + "\n Sales Projection Value is not less than  Dispatch Quantity For " + tabTest.getTitleAt(p) + " In " + pos + myear + " at Row :" + (m + 1);
                }
//                if ((mcurmonth == 2 || mcurmonth == 3) && mnext.equalsIgnoreCase("")) {
//                    if (cb > (dq + mwip + mstk)) {
//                        err = err + "\n Sales Projection Value is not more than  WIP+STOCK+Dispatch Quantity For " + tabTest.getTitleAt(p) + " In " + pos + myear + " at Row :" + (m + 1);
//                    }
//                }
                if (mcurmonth == 4) {
                    if (cb != ob && currmk.equalsIgnoreCase("")) {
                        err = err + "\n Remark is Compulsory For " + tabTest.getTitleAt(p) + " In " + pos + myear + " at Row :" + (m + 1);
                    }
                } else {
                    if (cb != pb && currmk.equalsIgnoreCase("")) {

                        err = err + "\n CB is " + cb + " PB is " + pb + " Remark is Compulsory For " + tabTest.getTitleAt(p) + " In " + pos + myear + " at Row :" + (m + 1);
                    }
                }
            }
        }
        if (alert.trim().length() > 0) {
            JOptionPane.showMessageDialog(this, alert, "Alert", JOptionPane.WARNING_MESSAGE);
        }
        return err;
    }

    private void Cancel() {
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();

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

        String productionDocumentNo = (String) BudgetEntry.getAttribute("DOC_NO").getString();

        if (BudgetEntry.IsEditable(EITLERPGLOBAL.gCompanyID, productionDocumentNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode = EITLERPGLOBAL.EDIT;
            DisableToolbar();
            GenerateCombos();
            //GenerateHierarchyCombo();
            GenerateSendToCombo();
            DisplayData();
            // SetupApproval();
            //ReasonResetReadonly();
            //cmbOrderReason.setEnabled(false);
            //if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, productionDocumentNo)) {
            SetFields(true);
            //} else {

            //   EnableApproval();
            //}
        } else {
            JOptionPane.showMessageDialog(null, "You cannot edit this record. \n It is either approved/rejected or waiting approval for other user");
        }

    }

// find rate update by doc no
    public void Find(String docNo) {
        BudgetEntry.Filter(" DOC_NO='" + docNo + "'");
        SetMenuForRights();
        DisplayData();
    }

    public void FindD(String cnd) {
        BudgetEntry.Filter(cnd);
        SetMenuForRights();
        DisplayData();
    }

    private void Delete() {

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
                hmSendToList = clsFeltProductionApprovalFlow.getRemainingUsers(ModuleId, DOC_NO1.getText());
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
    }

    private void SetFields(boolean pStat) {

        cmbHierarchy.setEnabled(pStat);
        OpgApprove.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        cmbSendTo.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        txtyearfrom.setEnabled(pStat);
        DOC_NO1.setEnabled(pStat);
        txtpartycode.setEnabled(pStat);
        markascomplete.setEnabled(pStat);
//        for (int p = 0; p < tabTest.getTabCount(); p++) {
//            tblPress[p].setEnabled(pStat);
//            tblDryer[p].setEnabled(pStat);
//        }
//        Table.setEnabled(pStat);

        if (!OpgReject.isSelected()) {
            SetupApproval();
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

//      
        BudgetEntry.setAttribute("DOC_NO", DOC_NO1.getText());

        DOC_NO = DOC_NO1.getText();

        BudgetEntry.setAttribute("DOC_NO", DOC_NO);
        BudgetEntry.setAttribute("MODULE_ID", ModuleId);
        BudgetEntry.setAttribute("USER_ID", EITLERPGLOBAL.gNewUserID);

        BudgetEntry.setAttribute("REJECTED_REMARKS", txtToRemarks.getText());
        BudgetEntry.setAttribute("REMARKS", "");
        BudgetEntry.setAttribute("APPROVAL_STATUS", "");
        BudgetEntry.setAttribute("APPROVER_REMARKS", txtFromRemarks.getText());
        BudgetEntry.setAttribute("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());

        //----- Update Approval Specific Fields -----------//
        BudgetEntry.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        BudgetEntry.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        BudgetEntry.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        BudgetEntry.setAttribute("FROM_REMARKS", txtToRemarks.getText());
        if (markascomplete.isSelected()) {
            BudgetEntry.setAttribute("MARK_AS_COMPLETE", 1);
        } else {
            BudgetEntry.setAttribute("MARK_AS_COMPLETE", 0);
        }

        if (OpgApprove.isSelected()) {
            BudgetEntry.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            BudgetEntry.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            BudgetEntry.setAttribute("APPROVAL_STATUS", "R");
            BudgetEntry.setAttribute("SEND_DOC_TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        }

        if (OpgHold.isSelected()) {
            BudgetEntry.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            BudgetEntry.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            BudgetEntry.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            BudgetEntry.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            BudgetEntry.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            BudgetEntry.setAttribute("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            BudgetEntry.setAttribute("UPDATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }
        BudgetEntry.colMRItems.clear();
        for (int p = 0; p < tabTest.getTabCount(); p++) {
            if (tblPress[p].getRowCount() > 0) {
                SetdataRow(tblPress[p], p, "");
            }
            if (tblDryer[p].getRowCount() > 0) {
                SetdataRow(tblDryer[p], p, "");
            }
            if (tblPressnxt[p].getRowCount() > 0) {
                SetdataRow(tblPressnxt[p], p, "NEXT");
            }
            if (tblDryernxt[p].getRowCount() > 0) {
                SetdataRow(tblDryernxt[p], p, "NEXT");
            }
        }
    }

    private void SetdataRow(JTable Table, int p, String mnext) {

        mmonth = new HashMap();
        mmonth.put(1, "JAN");
        mmonth.put(2, "FEB");
        mmonth.put(3, "MAR");
        mmonth.put(4, "APR");
        mmonth.put(5, "MAY");
        mmonth.put(6, "JUN");
        mmonth.put(7, "JUL");
        mmonth.put(8, "AUG");
        mmonth.put(9, "SEP");
        mmonth.put(10, "OCT");
        mmonth.put(11, "NOV");
        mmonth.put(12, "DEC");

        int gv;

        for (int m = 0; m < Table.getRowCount(); m++) {
            clsBudgetReviewEntryItem ObjItem = new clsBudgetReviewEntryItem();
            ObjItem.setAttribute("DOC_NO", DOC_NO1.getText());
            if (mnext.equalsIgnoreCase("")) {
                ObjItem.setAttribute("YEAR_FROM", txtyearfrom.getText());
                ObjItem.setAttribute("YEAR_TO", lblyearto.getText());
            } else {
                ObjItem.setAttribute("YEAR_FROM", lblyearto.getText());
                ObjItem.setAttribute("YEAR_TO", String.valueOf(Integer.parseInt(lblyearto.getText()) + 1));
            }
            ObjItem.setAttribute("PARTY_CODE", txtpartycode.getText());
            ObjItem.setAttribute("PARTY_NAME", txtpartyname.getText());
            ObjItem.setAttribute("MACHINE_NO", tabTest.getTitleAt(p).substring(7));
            ObjItem.setAttribute("POSITION_NO", data.getStringValueFromDB("SELECT POSITION_NO FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE UPN='" + Table.getValueAt(m, 1).toString() + "'"));

            ObjItem.setAttribute("UPN", Table.getValueAt(m, 1).toString());
            ObjItem.setAttribute("CURRENT_PROJECTION", Double.parseDouble(Table.getValueAt(m, 6).toString()));
            ObjItem.setAttribute("CURRENT_PROJECTION_VALUE", Double.parseDouble(Table.getValueAt(m, 7).toString()));
            ObjItem.setAttribute("ACESS_QTY", Double.parseDouble(Table.getValueAt(m, 22).toString()));
            ObjItem.setAttribute("ACESS_VALUE", Double.parseDouble(Table.getValueAt(m, 23).toString()));
            ObjItem.setAttribute("PROJ_OF_ORDER_QTY", Double.parseDouble(Table.getValueAt(m, 24).toString()));
            ObjItem.setAttribute("PROJ_OF_ORDER_VALUE", Double.parseDouble(Table.getValueAt(m, 25).toString()));

            gv = 26;
            for (int mn = 4; mn <= 12; mn++) {
                try {
                    ObjItem.setAttribute(mmonth.get(mn) + "_BUDGET", Double.parseDouble(Table.getValueAt(m, gv).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute(mmonth.get(mn) + "_BUDGET", 0.0);
                }
                gv++;
                try {
                    ObjItem.setAttribute(mmonth.get(mn) + "_NET_AMOUNT", Double.parseDouble(Table.getValueAt(m, gv).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute(mmonth.get(mn) + "_NET_AMOUNT", 0.0);
                }
                gv++;
                try {
                    ObjItem.setAttribute(mmonth.get(mn) + "_DOUBTFUL_QTY", Double.parseDouble(Table.getValueAt(m, gv).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute(mmonth.get(mn) + "_DOUBTFUL_QTY", 0.0);
                }
                gv++;
                ObjItem.setAttribute(mmonth.get(mn) + "_REMARK", Table.getValueAt(m, gv).toString());
                gv++;
                ObjItem.setAttribute(mmonth.get(mn) + "_REMARK_DOUBTFUL", Table.getValueAt(m, gv).toString());
                gv++;
                ObjItem.setAttribute(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC", Table.getValueAt(m, gv).toString());
                gv++;
                ObjItem.setAttribute(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL", Table.getValueAt(m, gv).toString());
                gv++;

            }
            for (int mn = 1; mn <= 3; mn++) {
                try {
                    ObjItem.setAttribute(mmonth.get(mn) + "_BUDGET", Double.parseDouble(Table.getValueAt(m, gv).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute(mmonth.get(mn) + "_BUDGET", 0.0);
                }
                gv++;
                try {
                    ObjItem.setAttribute(mmonth.get(mn) + "_NET_AMOUNT", Double.parseDouble(Table.getValueAt(m, gv).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute(mmonth.get(mn) + "_NET_AMOUNT", 0.0);
                }
                gv++;
                try {
                    ObjItem.setAttribute(mmonth.get(mn) + "_DOUBTFUL_QTY", Double.parseDouble(Table.getValueAt(m, gv).toString()));
                } catch (Exception e) {
                    ObjItem.setAttribute(mmonth.get(mn) + "_DOUBTFUL_QTY", 0.0);
                }
                gv++;
                ObjItem.setAttribute(mmonth.get(mn) + "_REMARK", Table.getValueAt(m, gv).toString());
                gv++;
                ObjItem.setAttribute(mmonth.get(mn) + "_REMARK_DOUBTFUL", Table.getValueAt(m, gv).toString());
                gv++;
                ObjItem.setAttribute(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC", Table.getValueAt(m, gv).toString());
                gv++;
                ObjItem.setAttribute(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL", Table.getValueAt(m, gv).toString());
                gv++;
            }
            gv = 119;
            try {
                ObjItem.setAttribute("CE_PC", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("CE_PC", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("NCE_WIP", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("NCE_WIP", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("NCE_STK", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("NCE_STK", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("MRF_PROJECTION", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("MRF_PROJECTION", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("NCE_WIP_P_YEAR", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("NCE_WIP_P_YEAR", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("NCE_STK_P_YEAR", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("NCE_STK_P_YEAR", 0.0);
            }
            gv = 129;
            try {
                ObjItem.setAttribute("GOAL", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("GOAL", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("ADDTIONAL_SCOPE", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("ADDTIONAL_SCOPE", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("CURRENT_POTENTIAL", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("CURRENT_POTENTIAL", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("NEW_GOAL", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("NEW_GOAL", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("CHANGE_QTY", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("CHANGE_QTY", 0.0);
            }
            gv++;
            try {
                ObjItem.setAttribute("SCOPE", Double.parseDouble(Table.getValueAt(m, gv).toString()));
            } catch (Exception e) {
                ObjItem.setAttribute("SCOPE", 0.0);
            }
            gv++;

            BudgetEntry.colMRItems.put(Integer.toString(BudgetEntry.colMRItems.size() + 1), ObjItem);
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DOC_NO1;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable TableApprovalStatus;
    private javax.swing.JTable TableUpdateHistory;
    private javax.swing.JTable Tablepartyinfo;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton btnSendFAmail;
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
    private javax.swing.JButton cmdNextToTab3;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle1;
    private javax.swing.JLabel lblyearto;
    private javax.swing.JCheckBox markascomplete;
    private javax.swing.JTabbedPane tabTest;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JLabel txtpartyname;
    private javax.swing.JTextField txtyearfrom;
    // End of variables declaration//GEN-END:variables

    private boolean chkposclose(String mUPN) {
        int chk;
        chk = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL D,PRODUCTION.FELT_MACHINE_MASTER_HEADER H  WHERE D.MM_DOC_NO=H.MM_DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 AND MM_UPN_NO='" + mUPN + "' AND COALESCE(POSITION_CLOSE_IND,0) =1");
        if (chk > 0) {
            return true;
        } else {
            return false;
        }
    }

    private double getPreBudget(String mUPN) {
        double d = 0;
        //int cmm = data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL");
        int cmm = Integer.parseInt(DOC_NO1.getText().substring(5, 7));
        if (cmm > 4) {
            d = data.getDoubleValueFromDB("SELECT CURRENT_PROJECTION FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE UPN='" + mUPN + "' AND LEFT(DOC_NO, 7) = CONCAT('B2122',RIGHT(100 + (" + cmm + " - 1), 2))  AND "
                    + "YEAR_FROM = 2021 AND YEAR_TO = 2022");
        }
        if (cmm == 1) {
            d = data.getDoubleValueFromDB("SELECT CURRENT_PROJECTION FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE UPN='" + mUPN + "' AND  LEFT(DOC_NO, 7) = 'B212212'  AND "
                    + "YEAR_FROM = 2021 AND YEAR_TO = 2022");
        }
        if (cmm == 2) {
            d = data.getDoubleValueFromDB("SELECT CURRENT_PROJECTION FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE UPN='" + mUPN + "' AND  LEFT(DOC_NO, 7) = 'B212201'  AND "
                    + "YEAR_FROM = 2021 AND YEAR_TO = 2022");
        }
        if (cmm == 3) {
            d = data.getDoubleValueFromDB("SELECT CURRENT_PROJECTION FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    + "WHERE UPN='" + mUPN + "' AND  LEFT(DOC_NO, 7) = 'B212202'  AND "
                    + "YEAR_FROM = 2021 AND YEAR_TO = 2022");
        }
        return d;
    }
}
