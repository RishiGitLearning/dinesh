/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.MachineRunForcasting;

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
public class frmMachineRunForcastingEntry extends javax.swing.JApplet {

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
    private int ModuleId = 851;
    private String DOC_NO = "";
    private clsMachineRunForcastingEntry BudgetEntry;
    private EITLComboModel cmbSendToModel;
    private int FinalApprovedBy = 0;
    private int mnoofmachine = 0;
    String cellLastValue = "";
    String seleval = "", seltyp = "", selqlt = "", selshd = "", selpiece = "", selext = "", selinv = "", selsz = "";
    private int mlstrc;
    private HashMap mmonth, change1, change2, change3;
    private int mlabelno = 0;

    String machineno = "";

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateForDB = new SimpleDateFormat("yyyy-MM-dd");

    JScrollPane[] scrollpane = new JScrollPane[100];
    JTable[] tblPress = new JTable[100];
    JLabel[] lblppos = new JLabel[100];
    JLabel[] lblpqlt = new JLabel[100];
    final EITLTableModel[] DataModel_Press = new EITLTableModel[100];

    JTable[] tblDryer = new JTable[100];
    JLabel[] lbldpos = new JLabel[100];
    JLabel[] lbldqlt = new JLabel[100];
    final EITLTableModel[] DataModel_dryer = new EITLTableModel[100];

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

        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
        lblyearto.setVisible(false);
        txtyearfrom.setVisible(false);
        jScrollPane1.setVisible(false);
        Tablepartyinfo.setVisible(false);

        BudgetEntry = new clsMachineRunForcastingEntry();
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

        clearFields();
        mmonth = new HashMap();
//        mmonth.put(1, "JAN");
//        mmonth.put(2, "FEB");
//        mmonth.put(3, "MAR");
//        mmonth.put(4, "APR");
//        mmonth.put(5, "MAY");
//        mmonth.put(6, "JUN");
//        mmonth.put(7, "JUL");
//        mmonth.put(8, "AUG");
//        mmonth.put(9, "SEP");
//        mmonth.put(10, "OCT");
//        mmonth.put(11, "NOV");
//        mmonth.put(12, "DEC");
        
        mmonth.put(1, "Q_1");
        mmonth.put(2, "Q_2");
        mmonth.put(3, "Q_3");
        mmonth.put(4, "Q_4");
        
        change1 = new HashMap();
//        change1.put(1, 37);
//        change1.put(2, 41);
//        change1.put(3, 45);
//        change1.put(4, 1);
//        change1.put(5, 5);
//        change1.put(6, 9);
//        change1.put(7, 13);
//        change1.put(8, 17);
//        change1.put(9, 21);
//        change1.put(10, 25);
//        change1.put(11, 29);
//        change1.put(12, 33);
        
        change1.put(1, 1);
        change1.put(2, 5);
        change1.put(3, 9);
        change1.put(4, 13);
        
        int setvalblnk = 0;
        try {
            DOC_NO = BudgetEntry.getAttribute("DOC_NO").getString();
            lblTitle1.setText("Machine Run Forcasting  - " + DOC_NO);
            DOC_NO1.setText(DOC_NO);
            setvalblnk = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL_H WHERE DOC_NO='" + DOC_NO + "'");
            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) BudgetEntry.getAttribute("HIERARCHY_ID").getVal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mnoofmachine = 0;
        try {
            tabTest.removeAll();

            JPanel machines = new JPanel();
            ResultSet r;
            //final int mcurmonth = data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL");
//            final int mcurmonth = Integer.parseInt(DOC_NO1.getText().substring(7, 9));
            final int mcurmonth = Integer.parseInt(DOC_NO1.getText().substring(8, 9));
            //int mcurmonth = 4;
            r = data.getResult("SELECT * FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' ORDER BY MACHINE_NO");
            r.first();
            String mmachine = "";
            txtpartycode.setText(r.getString("PARTY_CODE"));
            txtpartyname.setText(r.getString("PARTY_NAME"));
            txtyearfrom.setText(r.getString("YEAR_FROM"));
            lblyearto.setText(r.getString("YEAR_TO"));
//            ResultSet pi;
//            pi = data.getResult("SELECT PARTY_CODE,SUM(NO_OF_PIECES) AS INVQTY,ROUND(SUM(INVOICE_AMT),2) AS INVAMT,ROUND(SUM(ACTUAL_WEIGHT),2) AS WEIGHT "
//                    + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H "
//                    + "WHERE COALESCE(APPROVED,0)=1 AND COALESCE(CANCELLED,0)=0 AND "
//                    + " INVOICE_DATE>='2019-04-01'  AND INVOICE_DATE<='2020-03-31' "
//                    + "AND PIECE_NO NOT IN (SELECT PIECE_NO FROM PRODUCTION.FELT_SALES_RETURNS_DETAIL D,PRODUCTION.FELT_SALES_RETURNS_HEADER H WHERE H.DOC_NO=D.DOC_NO AND COALESCE(H.APPROVED,0)=1 AND COALESCE(H.CANCELED,0)=0 ) "
//                    + "AND PARTY_CODE='" + txtpartycode.getText() + "' "
//                    + "GROUP BY PARTY_CODE "
//                    + "UNION ALL "
//                    + "SELECT FELT_AMEND_PARTY_CODE,COUNT(FELT_AMEND_PIECE_NO),ROUND(SUM(FELT_AMEND_EXPORT_INV_AMT),2),ROUND(SUM(FELT_AMEND_WEIGHT),2) FROM PRODUCTION.FELT_PIECE_AMEND "
//                    + "WHERE FELT_AMEND_REASON =8 AND FELT_AMEND_EXPORT_INV_DATE >= '2019-04-01' AND FELT_AMEND_EXPORT_INV_DATE<='2020-03-31' "
//                    + "AND FELT_AMEND_PARTY_CODE='" + txtpartycode.getText() + "' "
//                    + "GROUP BY FELT_AMEND_PARTY_CODE");
//
//            pi.first();
//            Object[] rowDatap = new Object[100];
//            if (pi.getRow() > 0) {
//
//                rowDatap[0] = "Total Sale Previous Year";
//                rowDatap[1] = pi.getString("INVQTY");
//                rowDatap[2] = pi.getString("WEIGHT");
//                rowDatap[3] = pi.getString("INVAMT");
//                DataModelParty.addRow(rowDatap);
//            } else {
//                rowDatap[0] = "Total Sale Previous Year";
//                rowDatap[1] = 0;
//                rowDatap[2] = 0;
//                rowDatap[3] = 0;
//                DataModelParty.addRow(rowDatap);
//            }
//            pi = data.getResult("SELECT SUM(ACTUAL_BUDGET) AS ABUDGET,SUM(ACTUAL_BUDGET_VALUE) AS AVALUE,"
//                    + "round(sum(coalesce(ACTUAL_BUDGET,0)*(coalesce(PRESS_WEIGHT,0)+coalesce(DRY_WEIGHT,0))),2) as AWEIGHT "
//                    + " FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
//                    + "WHERE DOC_NO='" + DOC_NO + "'");
//
//            pi.first();
//
//            rowDatap = new Object[100];
//            rowDatap[0] = "Total Budget Current Year";
//            rowDatap[1] = pi.getString("ABUDGET");
//            rowDatap[2] = pi.getString("AWEIGHT");
//            rowDatap[3] = pi.getString("AVALUE");
//            DataModelParty.addRow(rowDatap);
//
//            pi = data.getResult("SELECT SUM(CURRENT_PROJECTION) AS CBUDGET,SUM(CURRENT_PROJECTION_VALUE) AS CVALUE,"
//                    + "round(sum(coalesce(CURRENT_PROJECTION,0)*(coalesce(PRESS_WEIGHT,0)+coalesce(DRY_WEIGHT,0))),2) as CWEIGHT "
//                    + " FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
//                    + "WHERE DOC_NO='" + DOC_NO + "'");
//
//            pi.first();
//
//            rowDatap = new Object[100];
//            rowDatap[0] = "Total Sale Projection Current Year";
//            rowDatap[1] = pi.getString("CBUDGET");
//            rowDatap[2] = pi.getString("CWEIGHT");
//            rowDatap[3] = pi.getString("CVALUE");
//            DataModelParty.addRow(rowDatap);
//
//            final TableColumnModel cm = Tablepartyinfo.getColumnModel();
//            for (int column = 0; column < Tablepartyinfo.getColumnCount(); column++) {
//                int width = 100; // Min width
//                for (int row = 0; row < Tablepartyinfo.getRowCount(); row++) {
//                    TableCellRenderer renderer = Tablepartyinfo.getCellRenderer(row, column);
//                    Component comp = Tablepartyinfo.prepareRenderer(renderer, row, column);
//                    width = Math.max(comp.getPreferredSize().width + 10, width);
//                }
//                if (width > 300) {
//                    width = 300;
//                }
//                cm.getColumn(column).setPreferredWidth(width);
//            }
            int i = 0;
            mlabelno = 0;
//            lblppos[mlabelno] = new JLabel("Total Exp. Act. Run Days : ");
//            lbldpos[mlabelno] = new JLabel("Position : ");
//            lblpqlt[mlabelno] = new JLabel("Quality : ");
//            lbldqlt[mlabelno] = new JLabel("Quality : ");
            mmachine = r.getString("MACHINE_NO");
            machineno = mmachine;
            machines = new JPanel();
            machines.setLayout(null);
            tabTest.removeAll();
            tabTest.add("Machine " + r.getString("MACHINE_NO"), machines);

            tblPress[i] = new JTable();

            //scrollpane[i]=new JScrollPane();
            //scrollpane[i].add(tblPress[i]);
            DataModel_Press[i] = new EITLTableModel();
            tblPress[i].removeAll();
            tblPress[i].setModel(DataModel_Press[i]);
            //tblDryer.setBounds(10, 10, 500, 100);

            tblPress[i].setAutoResizeMode(0);
            tblPress[i].setRowSelectionAllowed(true);
            tblPress[i].getTableHeader().setReorderingAllowed(false);
            tblPress[i].setEnabled(true);

//            DataModel_Press[i].addColumn("SrNo"); //0 - Read Only
            DataModel_Press[i].addColumn("Fin Year");

//            for (int m = 4; m <= 12; m++) {
//                DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Run Days");
//                DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Remark");
//                DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Run Days");
//                DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Remark");
//            }
//            for (int m = 1; m <= 3; m++) {
//                DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Run Days");
//                DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Remark");
//                DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Run Days");
//                DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Remark");
//            }
            
            for (int m = 1; m <= 4; m++) {
                DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Run Days");
                DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Remark");
                DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Run Days");
                DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Remark");
            }

            DataModel_Press[i].addColumn("Incharge");
            DataModel_Press[i].addColumn("Party Group");

            final int mchange = Integer.parseInt(change1.get(mcurmonth).toString());

//            for (int ro = 0; ro <= 50; ro++) {
            for (int ro = 0; ro <= 18; ro++) {                
                if (EditMode == EITLERPGLOBAL.EDIT) {
//                    if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString() + "")) {

//                    if (mcurmonth == 4) {
//                        if (ro == mchange || ro == mchange + 1) {
//
//                        } else {
//                            DataModel_Press[i].SetReadOnly(ro);
//                        }
//                    } else {
//                        if (ro == mchange || ro == mchange + 1 || ro == mchange - 1 || ro == mchange - 2) {
//
//                        } else {
//                            DataModel_Press[i].SetReadOnly(ro);
//                        }
//                    }
                    
//                    if (ro >= 1 && ro <= 48) {
                    if (ro >= 1 && ro <= 16) {
                        if (ro % 4 == 1 || ro % 4 == 2) {

                        } else {
//                            if (mcurmonth != 4) {
                            if (mcurmonth != 1) {                                
                                if (ro == mchange - 1 || ro == mchange - 2) {

                                } else {
                                    DataModel_Press[i].SetReadOnly(ro);
                                }
                            } else {
                                DataModel_Press[i].SetReadOnly(ro);
                            }
//                            DataModel_Press[i].SetReadOnly(ro);
                        }
                    } else {
                        DataModel_Press[i].SetReadOnly(ro);
                    }

//                    } else {
//                        if (ro == mchange || ro == mchange + 1 || ro == mchange - 1 || ro == mchange - 2) {
//
//                        } else {
//                            DataModel_Press[i].SetReadOnly(ro);
//                        }
//                    }
//                if (mcurmonth < 4) {
//                    if (ro == 49 + ((mcurmonth - 1) * 3) || ro == 49 + ((mcurmonth - 1) * 3) + 2) {
//
//                    } else {
//                        DataModel_Press[i].SetReadOnly(ro);
//                    }
//                } else if (ro == 22 + ((mcurmonth - 4) * 3) || ro == (22 + ((mcurmonth - 4) * 3)) + 2) {
//
//                } else {
//                    DataModel_Press[i].SetReadOnly(ro);
//                }
                } else {
                    DataModel_Press[i].SetReadOnly(ro);
                }
            }

//            final TableColumnModel columnModel1 = tblPress[i].getColumnModel();
//            for (int column = 0; column < tblPress[i].getColumnCount(); column++) {
//                int width = 60; // Min width
//                for (int row = 0; row < tblPress[i].getRowCount(); row++) {
//                    TableCellRenderer renderer = tblPress[i].getCellRenderer(row, column);
//                    Component comp = tblPress[i].prepareRenderer(renderer, row, column);
//                    width = Math.max(comp.getPreferredSize().width + 10, width);
//                }
//                if (width > 300) {
//                    width = 300;
//                }
//                columnModel1.getColumn(column).setPreferredWidth(width);
//            }
            final int final_i = i;
            tblPress[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent ke) {
                    double mcb, mwip, mstk, mobs, macess, mdis, mcanq, mcanv;
//                    for (int ji = 0; ji < DataModel_Press[final_i].getRowCount(); ji++) {
//                        double value = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 5).toString());
//                        double pq = 0, pb = 0;
//                        mcb = mwip = mstk = mobs = macess = mdis = 0;
//                        try {
//                            pq = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, mchange).toString());
////                            if (mcurmonth < 4) {
////                                pq = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 49 + ((mcurmonth - 1) * 3)).toString());
////                            } else {
////                                pq = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 22 + ((mcurmonth - 4) * 3)).toString());
////                            }
//                        } catch (Exception nf) {
//                            pq = -1;
//                        }
//
//                        if (pq >= 0) {
//                            DataModel_Press[final_i].setValueAt(value * pq, ji, mchange + 1);
////                            if (mcurmonth < 4) {
////                                DataModel_Press[final_i].setValueAt(value * pq, ji, 49 + ((mcurmonth - 1) * 3) + 1);
////                            } else {
////                                DataModel_Press[final_i].setValueAt(value * pq, ji, 22 + ((mcurmonth - 4) * 3) + 1);
////                            }
//                            DataModel_Press[final_i].setValueAt(pq, ji, 6);
//                        } else {
//
//                            pb = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 10).toString());
//
//                            DataModel_Press[final_i].setValueAt(pb, ji, 6);
//                        }
//                        try {
//                            mcb = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 6).toString());
//                        } catch (Exception e) {
//                            mcb = 0;
//                        }
//                        try {
//                            mwip = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 16).toString());
//                        } catch (Exception e) {
//                            mwip = 0;
//                        }
//                        try {
//                            mstk = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 18).toString());
//                        } catch (Exception e) {
//                            mstk = 0;
//                        }
//                        try {
//                            mobs = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 20).toString());
//                        } catch (Exception e) {
//                            mobs = 0;
//                        }
//                        try {
//                            mdis = Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 12).toString());
//                        } catch (Exception e) {
//                            mdis = 0;
//                        }
//                        //System.out.println("Row " + ji + " Bud " + mcb + " wip " + mwip + " Dis " + mdis + "acess " + ((mwip + mstk + mdis) - mcb));
//                        if (mcb - (mwip + mstk + mdis) < 0) {
//                            macess = (mwip + mstk + mdis) - mcb;
//                            DataModel_Press[final_i].setValueAt(macess, ji, 22);
//                            DataModel_Press[final_i].setValueAt(value * macess, ji, 23);
//                            mcanq = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                    + "WHERE PR_UPN='" + DataModel_Press[final_i].getValueAt(ji, 1).toString() + "' AND "
//                                    + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                            DataModel_Press[final_i].setValueAt(mcanq, ji, 113);
//                            mcanv = data.getDoubleValueFromDB("SELECT SUM(PR_FELT_VALUE_WITH_GST) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                    + "WHERE PR_UPN='" + DataModel_Press[final_i].getValueAt(ji, 1).toString() + "' AND "
//                                    + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                            DataModel_Press[final_i].setValueAt(mcanv, ji, 114);
//
//                        } else {
//                            macess = 0;
//                            DataModel_Press[final_i].setValueAt(macess, ji, 22);
//                            DataModel_Press[final_i].setValueAt(value * macess, ji, 23);
//                        }
//                        DataModel_Press[final_i].setValueAt(value * Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 6).toString()), ji, 7);
//                        DataModel_Press[final_i].setValueAt((mcb + macess) - (mwip + mstk + mdis), ji, 24);
//                        //DataModel_Press[final_i].setValueAt(Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 6).toString()) - (Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 10).toString()) + Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 12).toString()) + Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 14).toString())), ji, 20);
//                        DataModel_Press[final_i].setValueAt(value * Double.parseDouble(DataModel_Press[final_i].getValueAt(ji, 24).toString()), ji, 25);
//                    }
//                    Double vTotal = data.getDoubleValueFromDB("SELECT CASE WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) "
//                                    + "WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_ACT_RD +MAR_EXP_RD) "
//                                    + "ELSE 0 END AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' ");
//                    lblppos[tabTest.getSelectedIndex()].setText("Total Exp. Act. Run Days : " + vTotal);
//                    lblpqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel_Press[final_i].getValueAt(tblPress[final_i].getSelectedRow(), 3).toString());
                }
            });
            tblPress[i].getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    // do some actions here, for example
                    // print first column value from selected row
//                    Double vTotal = data.getDoubleValueFromDB("SELECT CASE WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) "
//                                    + "WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_ACT_RD +MAR_EXP_RD) "
//                                    + "ELSE 0 END AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' ");
//                    lblppos[tabTest.getSelectedIndex()].setText("Total Exp. Act. Run Days : " + vTotal);
//                    lblpqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel_Press[final_i].getValueAt(tblPress[final_i].getSelectedRow(), 3).toString());
                }
            });

            JScrollPane jScrollPane2 = new JScrollPane();
            jScrollPane2.setViewportView(tblPress[i]);

            machines.add(jScrollPane2);
            jScrollPane2.setBounds(0, 40, 1250, 60);

            Object[] rowData = new Object[300];//20

//            tblDryer[i] = new JTable();
//            tblDryer[i].setRowSelectionAllowed(true);
//            DataModel_dryer[i] = new EITLTableModel();
//            tblDryer[i].removeAll();
//            tblDryer[i].setModel(DataModel_dryer[i]);
//            //tblDryer.setBounds(10, 10, 500, 100);
//
//            tblDryer[i].setAutoResizeMode(0);
//
//            DataModel_dryer[i].addColumn("SrNo"); //0 - Read Only
//            DataModel_dryer[i].addColumn("UPN"); //1
//            DataModel_dryer[i].addColumn("Position Desc"); //2
//            DataModel_dryer[i].addColumn("Quality"); //3
//            DataModel_dryer[i].addColumn("Group"); //4
//            DataModel_dryer[i].addColumn("Value"); //5
//            DataModel_dryer[i].addColumn("CurrentProjQty"); //6
//            DataModel_dryer[i].addColumn("CurrentProjValue"); //7
//            DataModel_dryer[i].addColumn("CurrProj+GR Qty"); //6
//            DataModel_dryer[i].addColumn("CurrProj+GR Value"); //7
//            DataModel_dryer[i].addColumn("BudgetQty"); //8
//            DataModel_dryer[i].addColumn("BudgetValue"); //9
//            DataModel_dryer[i].addColumn("DispatchQty"); //10
//            DataModel_dryer[i].addColumn("DispatchValue"); //11
//            DataModel_dryer[i].addColumn("Prev GR Qty"); //12
//            DataModel_dryer[i].addColumn("Prev GR Value"); //13
//            DataModel_dryer[i].addColumn("WIPQty"); //12
//            DataModel_dryer[i].addColumn("WIPValue"); //13
//            DataModel_dryer[i].addColumn("In StockQty"); //14
//            DataModel_dryer[i].addColumn("In StockValue"); //15
//            DataModel_dryer[i].addColumn("ObsoleteQty"); //16
//            DataModel_dryer[i].addColumn("ObsoleteValue"); //17
//            DataModel_dryer[i].addColumn("ExcessQty"); //18
//            DataModel_dryer[i].addColumn("ExcessValue"); //19
//
//            DataModel_dryer[i].addColumn("ProjOfOrderQty"); //14
//            DataModel_dryer[i].addColumn("ProjOfOrderValue"); //15
//            for (int m = 4; m <= 12; m++) {
//                DataModel_dryer[i].addColumn(mmonth.get(m) + "Qty");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + "Value");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + "DoubtfulQty");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + "Remark");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + " Remarks for Doubtful");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + " Proposed action by AIC");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + " Proposed action for Doubtful");
//            }
//            for (int m = 1; m <= 3; m++) {
//                DataModel_dryer[i].addColumn(mmonth.get(m) + "Qty");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + "Value");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + "DoubtfulQty");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + "Remark");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + " Remarks for Doubtful");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + " Proposed action by AIC");
//                DataModel_dryer[i].addColumn(mmonth.get(m) + " Proposed action for Doubtful");
//            }
////            DataModel_dryer[i].addColumn("AprQty"); //16          
////            DataModel_dryer[i].addColumn("AprValue"); //17
////            DataModel_dryer[i].addColumn("AprRemark"); //18
////            DataModel_dryer[i].addColumn("MayQty"); //19
////            DataModel_dryer[i].addColumn("MayValue"); //20
////            DataModel_dryer[i].addColumn("MayRemark"); //21
////            DataModel_dryer[i].addColumn("JunQty"); //22          
////            DataModel_dryer[i].addColumn("JunValue"); //23
////            DataModel_dryer[i].addColumn("JunRemark"); //24
////            DataModel_dryer[i].addColumn("JulQty"); //25          
////            DataModel_dryer[i].addColumn("JulValue"); //26
////            DataModel_dryer[i].addColumn("JulRemark"); //27
////            DataModel_dryer[i].addColumn("AugQty"); //28          
////            DataModel_dryer[i].addColumn("AugValue"); //29
////            DataModel_dryer[i].addColumn("AugRemark"); //30
////            DataModel_dryer[i].addColumn("SepQty"); //31          
////            DataModel_dryer[i].addColumn("SepValue"); //32
////            DataModel_dryer[i].addColumn("SepRemark"); //33
////            DataModel_dryer[i].addColumn("OctQty"); //34          
////            DataModel_dryer[i].addColumn("OctValue"); //35
////            DataModel_dryer[i].addColumn("OctRemark"); //36
////            DataModel_dryer[i].addColumn("NovQty"); //37          
////            DataModel_dryer[i].addColumn("NovValue"); //38
////            DataModel_dryer[i].addColumn("NovRemark"); //39
////            DataModel_dryer[i].addColumn("DecQty"); //40          
////            DataModel_dryer[i].addColumn("DecValue"); //41
////            DataModel_dryer[i].addColumn("DecRemark"); //42
////            DataModel_dryer[i].addColumn("JanQty"); //40          
////            DataModel_dryer[i].addColumn("JanValue"); //41
////            DataModel_dryer[i].addColumn("JanRemark"); //42
////            DataModel_dryer[i].addColumn("FebQty"); //40          
////            DataModel_dryer[i].addColumn("FebValue"); //41
////            DataModel_dryer[i].addColumn("FebRemark"); //42
////            DataModel_dryer[i].addColumn("MarQty"); //40          
////            DataModel_dryer[i].addColumn("MarValue"); //41
////            DataModel_dryer[i].addColumn("MarRemark"); //42
//            DataModel_dryer[i].addColumn("Incharge"); //43
//            DataModel_dryer[i].addColumn("Size Criteria"); //44
//            DataModel_dryer[i].addColumn("Party Group"); //45
//            DataModel_dryer[i].addColumn("Cancel Qty");
//            DataModel_dryer[i].addColumn("Cancel Value");
//            DataModel_dryer[i].addColumn("ObsoleteWIPQty");
//            DataModel_dryer[i].addColumn("ObsoleteWIPValue");
//            DataModel_dryer[i].addColumn("ObsoleteStkQty");
//            DataModel_dryer[i].addColumn("ObsoleteStkValue");
//
//            for (int ro = 0; ro < 119; ro++) {
//                if (EditMode == EITLERPGLOBAL.EDIT) {
//                    if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString() + "")) {
//                        if (ro == mchange || ro == mchange + 3 || ro==mchange+5) {
//
//                        } else {
//                            DataModel_dryer[i].SetReadOnly(ro);
//                        }
//                    } else {
//                        if (ro == mchange || ro == mchange + 2 || ro == mchange + 3 || ro == mchange + 4 || ro == mchange + 5 || ro == mchange + 6) {
//
//                        } else {
//                            DataModel_dryer[i].SetReadOnly(ro);
//                        }
//                    }
//
////                if (mcurmonth < 4) {
////                    if (ro == 49 + ((mcurmonth - 1) * 3) || ro == 49 + ((mcurmonth - 1) * 3) + 2) {
////
////                    } else {
////                        DataModel_Press[i].SetReadOnly(ro);
////                    }
////                } else if (ro == 22 + ((mcurmonth - 4) * 3) || ro == (22 + ((mcurmonth - 4) * 3)) + 2) {
////
////                } else {
////                    DataModel_Press[i].SetReadOnly(ro);
////                }
//                } else {
//                    DataModel_dryer[i].SetReadOnly(ro);
//                }
//            }
//            final TableColumnModel columnModel = tblDryer[i].getColumnModel();
//            for (int column = 0; column < tblDryer[i].getColumnCount(); column++) {
//                int width = 60; // Min width
//                for (int row = 0; row < tblDryer[i].getRowCount(); row++) {
//                    TableCellRenderer renderer = tblDryer[i].getCellRenderer(row, column);
//                    Component comp = tblDryer[i].prepareRenderer(renderer, row, column);
//                    width = Math.max(comp.getPreferredSize().width + 10, width);
//                }
//                if (width > 300) {
//                    width = 300;
//                }
//                columnModel.getColumn(column).setPreferredWidth(width);
//            }
//
//            tblDryer[i].addKeyListener(new KeyAdapter() {
//                @Override
//                public void keyReleased(KeyEvent ke) {
//                    double mcb, mwip, mstk, mobs, macess, mdis, mcanq, mcanv;
//                    for (int ji = 0; ji < DataModel_dryer[final_i].getRowCount(); ji++) {
//                        double value = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 5).toString());
//
//                        double pq = 0, pb = 0;
//                        mcb = mwip = mstk = mobs = macess = mdis = 0;
//                        try {
//                            pq = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, mchange).toString());
////                            if (mcurmonth < 4) {
////                                pq = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 49 + ((mcurmonth - 1) * 3)).toString());
////                            } else {
////                                pq = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 22 + ((mcurmonth - 4) * 3)).toString());
////                            }
//                        } catch (Exception nf) {
//                            pq = -1;
//                        }
//
//                        if (pq >= 0) {
//                            DataModel_dryer[final_i].setValueAt(value * pq, ji, mchange + 1);
////                            if (mcurmonth < 4) {
////                                DataModel_dryer[final_i].setValueAt(value * pq, ji, 49 + ((mcurmonth - 1) * 3) + 1);
////                            } else {
////                                DataModel_dryer[final_i].setValueAt(value * pq, ji, 22 + ((mcurmonth - 4) * 3) + 1);
////                            }
//                            DataModel_dryer[final_i].setValueAt(pq, ji, 6);
//                        } else {
//
//                            pb = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 10).toString());
//
//                            DataModel_dryer[final_i].setValueAt(pb, ji, 6);
//                        }
//                        try {
//                            mcb = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 6).toString());
//                        } catch (Exception e) {
//                            mcb = 0;
//                        }
//                        try {
//                            mwip = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 16).toString());
//                        } catch (Exception e) {
//                            mwip = 0;
//                        }
//                        try {
//                            mstk = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 18).toString());
//                        } catch (Exception e) {
//                            mstk = 0;
//                        }
//                        try {
//                            mobs = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 20).toString());
//                        } catch (Exception e) {
//                            mobs = 0;
//                        }
//                        try {
//                            mdis = Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 12).toString());
//                        } catch (Exception e) {
//                            mdis = 0;
//                        }
//                        if (mcb - (mwip + mstk + mdis) < 0) {
//                            macess = (mwip + mstk + mdis) - mcb;
//                            DataModel_dryer[final_i].setValueAt(macess, ji, 22);
//                            DataModel_dryer[final_i].setValueAt(value * macess, ji, 23);
//                            mcanq = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                    + "WHERE PR_UPN='" + DataModel_dryer[final_i].getValueAt(ji, 1).toString() + "' AND "
//                                    + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                            DataModel_dryer[final_i].setValueAt(mcanq, ji, 113);
//                            mcanv = data.getDoubleValueFromDB("SELECT SUM(PR_FELT_VALUE_WITH_GST) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                    + "WHERE PR_UPN='" + DataModel_dryer[final_i].getValueAt(ji, 1).toString() + "' AND "
//                                    + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                            DataModel_dryer[final_i].setValueAt(mcanv, ji, 114);
//                        } else {
//                            macess = 0;
//                            DataModel_dryer[final_i].setValueAt(macess, ji, 22);
//                            DataModel_dryer[final_i].setValueAt(value * macess, ji, 23);
//                        }
//
//                        DataModel_dryer[final_i].setValueAt(value * Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 6).toString()), ji, 7);
//                        DataModel_dryer[final_i].setValueAt((mcb + macess) - (mwip + mstk + mdis), ji, 24);
//                        //DataModel_dryer[final_i].setValueAt(Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 6).toString()) - (Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 10).toString()) + Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 12).toString()) + Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 14).toString())), ji, 20);
//                        DataModel_dryer[final_i].setValueAt(value * Double.parseDouble(DataModel_dryer[final_i].getValueAt(ji, 24).toString()), ji, 25);
//                    }
//                    lbldpos[tabTest.getSelectedIndex()].setText("Position :" + DataModel_dryer[final_i].getValueAt(tblDryer[final_i].getSelectedRow(), 2).toString());
//                    lbldqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel_dryer[final_i].getValueAt(tblDryer[final_i].getSelectedRow(), 3).toString());
//                }
//            });
//            tblDryer[i].getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//                public void valueChanged(ListSelectionEvent event) {
//                    // do some actions here, for example
//                    // print first column value from selected row
//                    lbldpos[tabTest.getSelectedIndex()].setText("Position :" + DataModel_dryer[final_i].getValueAt(tblDryer[final_i].getSelectedRow(), 2).toString());
//                    lbldqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel_dryer[final_i].getValueAt(tblDryer[final_i].getSelectedRow(), 3).toString());
//                }
//            });
//
//            JScrollPane jScrollPane3 = new JScrollPane();
//            jScrollPane3.setViewportView(tblDryer[i]);
//
//            jScrollPane3.setBounds(0, 200, 1250, 150);
//            machines.add(jScrollPane3);
            JLabel lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Press");
            lblMachine.setBounds(0, 5, 200, 50);
            machines.add(lblMachine);

//            Double vTotal = data.getDoubleValueFromDB("SELECT SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) "
//                    + "AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' AND MACHINE_NO='" + r.getString("MACHINE_NO") + "' ");
            
            Double vTotal = data.getDoubleValueFromDB("SELECT SUM(Q_1_EXP_RD + Q_2_EXP_RD + Q_3_EXP_RD + Q_4_EXP_RD) "
                    + "AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' AND MACHINE_NO='" + r.getString("MACHINE_NO") + "' ");
            
//            Double vTotal = data.getDoubleValueFromDB("SELECT CASE WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) "
//                    + "WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_ACT_RD +MAR_EXP_RD) "
//                    + "ELSE 0 END AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' AND MACHINE_NO='" + r.getString("MACHINE_NO") + "' ");
            JLabel lblTotal = new JLabel("Total Exp. Act. Run Days : " + vTotal);
            lblTotal.setBounds(150, 5, 300, 50);
            machines.add(lblTotal);

//            lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Dryer");
//            lblMachine.setBounds(0, 150, 200, 50);
//            machines.add(lblMachine);
//            lblppos[mlabelno].setBounds(150, 5, 300, 50);
//            machines.add(lblppos[mlabelno]);
//            lbldpos[mlabelno].setBounds(150, 150, 200, 50);
//            machines.add(lbldpos[mlabelno]);
//
//            lblpqlt[mlabelno].setBounds(550, 5, 200, 50);
//            machines.add(lblpqlt[mlabelno]);
//            lbldqlt[mlabelno].setBounds(550, 150, 200, 50);
//            machines.add(lbldqlt[mlabelno]);
            tblDryer[i] = new JTable();
            tblDryer[i].setRowSelectionAllowed(true);
            DataModel_dryer[i] = new EITLTableModel();
            tblDryer[i].removeAll();
            tblDryer[i].setModel(DataModel_dryer[i]);
            tblDryer[i].setAutoResizeMode(0);
            tblDryer[i].getTableHeader().setReorderingAllowed(false);

            DataModel_dryer[i].addColumn("SrNo"); //0 - Read Only
            DataModel_dryer[i].addColumn("UPN"); //1
            DataModel_dryer[i].addColumn("Position"); //1
            DataModel_dryer[i].addColumn("Position Desc"); //2
            DataModel_dryer[i].addColumn("Avg. Life"); //3
            DataModel_dryer[i].addColumn("Total Exp Consumption"); //4

            tblDryer[i].setEnabled(false);

            JScrollPane jScrollPane3 = new JScrollPane();
            jScrollPane3.setViewportView(tblDryer[i]);

            jScrollPane3.setBounds(0, 130, 1250, 150);
            machines.add(jScrollPane3);

            ResultSet posTotal;
            posTotal = data.getResult("SELECT D.MM_UPN_NO,D.MM_PARTY_CODE,D.MM_MACHINE_NO,D.MM_MACHINE_POSITION,D.MM_MACHINE_POSITION_DESC,D.MM_AVG_LIFE,COALESCE(CEILING(" + vTotal + "/D.MM_AVG_LIFE),0) AS CONSUMPTION "
                    + "FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,  PRODUCTION.FELT_MACHINE_MASTER_DETAIL D, PRODUCTION.FELT_QLT_RATE_MASTER R, DINESHMILLS.D_SAL_PARTY_MASTER P , PRODUCTION.FELT_MACHINE_POSITION_MST MP "
                    + "WHERE H.MM_DOC_NO=D.MM_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0  AND MM_ITEM_CODE = PRODUCT_CODE  AND EFFECTIVE_TO ='0000-00-00' AND POSITION_CATG=2 "
                    + "AND P.PARTY_CODE = H.MM_PARTY_CODE AND POSITION_NO+0 = MM_MACHINE_POSITION+0 "
                    + "AND D.MM_PARTY_CODE='" + r.getString("PARTY_CODE") + "' AND D.MM_MACHINE_NO='" + r.getString("MACHINE_NO") + "' AND COALESCE(D.POSITION_CLOSE_IND,0)=0 "
                    + "ORDER BY MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION*1");
            posTotal.first();
            Object[] rowDataT = new Object[100];

            int cntT = 1;
            int posT = 0;
            double cTotal = 0;
            while (!posTotal.isAfterLast()) {
                posT = 0;
                rowDataT[posT] = cntT; //SrNo
                posT++;
                rowDataT[posT] = posTotal.getString("MM_UPN_NO");
                posT++;
                rowDataT[posT] = posTotal.getString("MM_MACHINE_POSITION");
                posT++;
                rowDataT[posT] = posTotal.getString("MM_MACHINE_POSITION_DESC");
                posT++;
                rowDataT[posT] = posTotal.getString("MM_AVG_LIFE");
                posT++;
                rowDataT[posT] = posTotal.getString("CONSUMPTION");
                posT++;

                cTotal = cTotal + Double.parseDouble(posTotal.getString("CONSUMPTION"));

                cntT++;
                DataModel_dryer[i].addRow(rowDataT);
                posTotal.next();
            }
            posT = 0;
            rowDataT[posT] = cntT; //SrNo
            posT++;
            rowDataT[posT] = "";
            posT++;
            rowDataT[posT] = "";
            posT++;
            rowDataT[posT] = "TOTAL";
            posT++;
            rowDataT[posT] = "";
            posT++;
            rowDataT[posT] = cTotal;
            posT++;
            DataModel_dryer[i].addRow(rowDataT);

            final TableColumnModel columnModel = tblDryer[i].getColumnModel();
            for (int column = 0; column < tblDryer[i].getColumnCount(); column++) {
                int width = 60; // Min width
                for (int row = 0; row < tblDryer[i].getRowCount(); row++) {
                    TableCellRenderer renderer = tblDryer[i].getCellRenderer(row, column);
                    Component comp = tblDryer[i].prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 10, width);
                }
                if (width > 300) {
                    width = 300;
                }
                columnModel.getColumn(column).setPreferredWidth(width);
            }

            int msr = 1;
            int msr1 = 1, pos = 0;

            while (!r.isAfterLast()) {
                if (mmachine.equalsIgnoreCase(r.getString("MACHINE_NO"))) {
                    rowData = new Object[300];
//                    if (r.getString("GROUP_NAME").equalsIgnoreCase("HDS") || r.getString("GROUP_NAME").equalsIgnoreCase("SDF")) {
//                        pos = 0;
//                        rowData[pos] = msr1;
//                        pos++;
//                        rowData[pos] = r.getString("UPN");
//                        pos++;
//                        rowData[pos] = r.getString("POSITION_DESC");
//                        pos++;
//                        rowData[pos] = r.getString("QUALITY_NO");
//                        pos++;
//                        rowData[pos] = r.getString("GROUP_NAME");
//                        pos++;
//                        rowData[pos] = r.getDouble("SELLING_PRICE");
//                        pos++;
//                        rowData[pos] = r.getDouble("CURRENT_PROJECTION");
//                        pos++;
//                        rowData[pos] = r.getDouble("CURRENT_PROJECTION_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("CURRENT_PROJECTION") + r.getDouble("PREV_GR_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("CURRENT_PROJECTION_VALUE") + r.getDouble("PREV_GR_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("ACTUAL_BUDGET");
//                        pos++;
//                        rowData[pos] = r.getDouble("ACTUAL_BUDGET_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("DISPATCH_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("DISPATCH_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("PREV_GR_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("PREV_GR_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("WIP_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("WIP_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("STOCK_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("STOCK_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("OBSOLETE_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("OBSOLETE_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("ACESS_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("ACESS_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("PROJ_OF_ORDER_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("PROJ_OF_ORDER_VALUE");
//                        pos++;
//
//                        for (int mn = 4; mn <= 12; mn++) {
//                            if (mn == mcurmonth && setvalblnk == 0 && r.getDouble(mmonth.get(mn) + "_BUDGET") == 0) {
//                                //System.out.println("BLANK" + mn);
//                            } else {
//                                rowData[pos] = r.getDouble(mmonth.get(mn) + "_BUDGET");
//                            }
//                            pos++;
//                            rowData[pos] = r.getDouble(mmonth.get(mn) + "_NET_AMOUNT");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_DOUBTFUL_QTY");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK_DOUBTFUL");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL");
//                            pos++;
//                        }
//                        for (int mn = 1; mn <= 3; mn++) {
//                            if (mn == mcurmonth && setvalblnk == 0 && r.getDouble(mmonth.get(mn) + "_BUDGET") == 0) {
//                                //System.out.println("BLANK" + mn);
//                            } else {
//                                rowData[pos] = r.getDouble(mmonth.get(mn) + "_BUDGET");
//                            }
//                            pos++;
//                            rowData[pos] = r.getDouble(mmonth.get(mn) + "_NET_AMOUNT");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_DOUBTFUL_QTY");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK_DOUBTFUL");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL");
//                            pos++;
//                        }
//
//                        rowData[pos] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='" + r.getString("INCHARGE") + "'");
//                        pos++;
//                        rowData[pos] = r.getString("SIZE_CRITERIA");
//                        pos++;
//                        rowData[pos] = r.getString("PARTY_GROUP");
//                        pos++;
//                        rowData[pos] = r.getString("CANCEL_QTY");
//                        pos++;
//                        rowData[pos] = r.getString("CANCEL_VALUE");
//                        pos++;
//                        rowData[pos] = r.getString("OBSOLETE_WIP_QTY");
//                        pos++;
//                        rowData[pos] = r.getString("OBSOLETE_WIP_VALUE");
//                        pos++;
//                        rowData[pos] = r.getString("OBSOLETE_STOCK_QTY");
//                        pos++;
//                        rowData[pos] = r.getString("OBSOLETE_STOCK_VALUE");
//                        pos++;
//                        msr1++;
//                        DataModel_dryer[i].addRow(rowData);
//                    } else {
                    pos = 0;
//                        rowData[pos] = msr; //SrNo
                    rowData[pos] = r.getString("YEAR_FROM") + "-" + r.getString("YEAR_TO");
                    pos++;

//                    for (int mn = 4; mn <= 12; mn++) {
//                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_EXP_RD");
//                        pos++;
//                        rowData[pos] = r.getString(mmonth.get(mn) + "_EXP_RD_REMARK");
//                        pos++;
//                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_ACT_RD");
//                        pos++;
//                        rowData[pos] = r.getString(mmonth.get(mn) + "_ACT_RD_REMARK");
//                        pos++;
//                    }
//                    for (int mn = 1; mn <= 3; mn++) {
//                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_EXP_RD");
//                        pos++;
//                        rowData[pos] = r.getString(mmonth.get(mn) + "_EXP_RD_REMARK");
//                        pos++;
//                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_ACT_RD");
//                        pos++;
//                        rowData[pos] = r.getString(mmonth.get(mn) + "_ACT_RD_REMARK");
//                        pos++;
//                    }
                    
                    for (int mn = 1; mn <= 4; mn++) {
                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_EXP_RD");
                        pos++;
                        rowData[pos] = r.getString(mmonth.get(mn) + "_EXP_RD_REMARK");
                        pos++;
                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_ACT_RD");
                        pos++;
                        rowData[pos] = r.getString(mmonth.get(mn) + "_ACT_RD_REMARK");
                        pos++;
                    }

                    rowData[pos] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='" + r.getString("INCHARGE") + "'");
                    pos++;
                    rowData[pos] = r.getString("PARTY_GROUP");
                    pos++;

                    msr++;
                    DataModel_Press[i].addRow(rowData);
//                    }                   

                } else {
                    i++;
                    mlabelno++;
                    JPanel machine = new JPanel();
                    machine = new JPanel();
                    machine.setLayout(null);

                    tabTest.add("Machine " + r.getString("MACHINE_NO"), machine);

                    tblPress[i] = new JTable();
                    DataModel_Press[i] = new EITLTableModel();

                    tblPress[i].removeAll();
                    tblPress[i].setModel(DataModel_Press[i]);
                    //tblDryer.setBounds(10, 10, 500, 100);

                    tblPress[i].setAutoResizeMode(0);
                    tblPress[i].getTableHeader().setReorderingAllowed(false);

//                    DataModel_Press[i].addColumn("SrNo"); //0 - Read Only
                    DataModel_Press[i].addColumn("Fin Year");
//                    for (int m = 4; m <= 12; m++) {
//                        DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Run Days");
//                        DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Remark");
//                        DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Run Days");
//                        DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Remark");
//                    }
//                    for (int m = 1; m <= 3; m++) {
//                        DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Run Days");
//                        DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Remark");
//                        DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Run Days");
//                        DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Remark");
//                    }
                    
                    for (int m = 1; m <= 4; m++) {
                        DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Run Days");
                        DataModel_Press[i].addColumn(mmonth.get(m) + " Expected Remark");
                        DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Run Days");
                        DataModel_Press[i].addColumn(mmonth.get(m) + " Actual Remark");
                    }

                    DataModel_Press[i].addColumn("Incharge"); //43
                    DataModel_Press[i].addColumn("Party Group"); //45

//                    for (int ro = 0; ro <= 50; ro++) {
                    for (int ro = 0; ro <= 18; ro++) {
                        if (EditMode == EITLERPGLOBAL.EDIT) {
//                            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString() + "")) {

//                            if (mcurmonth == 4) {
//                                if (ro == mchange || ro == mchange + 1) {
//
//                                } else {
//                                    DataModel_Press[i].SetReadOnly(ro);
//                                }
//                            } else {
//                                if (ro == mchange || ro == mchange + 1 || ro == mchange - 1 || ro == mchange - 2) {
//
//                                } else {
//                                    DataModel_Press[i].SetReadOnly(ro);
//                                }
//                            }
//                            if (ro >= 1 && ro <= 48) {
                            if (ro >= 1 && ro <= 16) {
                                if (ro % 4 == 1 || ro % 4 == 2) {

                                } else {
//                                    if (mcurmonth != 4) {
                                    if (mcurmonth != 1) {
                                        if (ro == mchange - 1 || ro == mchange - 2) {

                                        } else {
                                            DataModel_Press[i].SetReadOnly(ro);
                                        }
                                    } else {
                                        DataModel_Press[i].SetReadOnly(ro);
                                    }
//                            DataModel_Press[i].SetReadOnly(ro);
                                }
                            } else {
                                DataModel_Press[i].SetReadOnly(ro);
                            }

//                            } else {
//                                if (ro == mchange || ro == mchange + 2 || ro == mchange + 3 || ro == mchange + 4 || ro == mchange + 5 || ro == mchange + 6) {
//
//                                } else {
//                                    DataModel_Press[i].SetReadOnly(ro);
//                                }
//                            }
                        } else {
                            DataModel_Press[i].SetReadOnly(ro);
                        }
                    }
//                    tblPress[i].getColumnModel().getColumn(0).setMaxWidth(30);

                    final int final_i1 = i;
                    tblPress[i].addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent ke) {
                            double mcb, mwip, mstk, mobs, macess, mdis, mcanq, mcanv;
//                            for (int ji = 0; ji < DataModel_Press[final_i1].getRowCount(); ji++) {
//                                double value = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 5).toString());
//                                double pq = 0, pb = 0;
//                                mcb = mwip = mstk = mobs = macess = mdis = 0;
//                                try {
//                                    pq = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, mchange).toString());
////                                    if (mcurmonth < 4) {
////                                        pq = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 49 + ((mcurmonth - 1) * 3)).toString());
////                                    } else {
////                                        pq = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 22 + ((mcurmonth - 4) * 3)).toString());
////                                    }
//                                } catch (Exception nf) {
//                                    pq = -1;
//                                }
//
//                                if (pq >= 0) {
//                                    DataModel_Press[final_i1].setValueAt(value * pq, ji, mchange + 1);
////                                    if (mcurmonth < 4) {
////                                        DataModel_Press[final_i1].setValueAt(value * pq, ji, 49 + ((mcurmonth - 1) * 3) + 1);
////                                    } else {
////                                        DataModel_Press[final_i1].setValueAt(value * pq, ji, 22 + ((mcurmonth - 4) * 3) + 1);
////                                    }
//                                    DataModel_Press[final_i1].setValueAt(pq, ji, 6);
//                                } else {
//
//                                    pb = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 10).toString());
//
//                                    DataModel_Press[final_i1].setValueAt(pb, ji, 6);
//                                }
//                                try {
//                                    mcb = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 6).toString());
//                                } catch (Exception e) {
//                                    mcb = 0;
//                                }
//                                try {
//                                    mwip = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 16).toString());
//                                } catch (Exception e) {
//                                    mwip = 0;
//                                }
//                                try {
//                                    mstk = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 18).toString());
//                                } catch (Exception e) {
//                                    mstk = 0;
//                                }
//                                try {
//                                    mobs = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 20).toString());
//                                } catch (Exception e) {
//                                    mobs = 0;
//                                }
//                                try {
//                                    mdis = Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 12).toString());
//                                } catch (Exception e) {
//                                    mdis = 0;
//                                }
//                                if (mcb - (mwip + mstk + mdis) < 0) {
//                                    macess = (mwip + mstk + mdis) - mcb;
//                                    DataModel_Press[final_i1].setValueAt(macess, ji, 22);
//                                    DataModel_Press[final_i1].setValueAt(value * macess, ji, 23);
//                                    mcanq = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                            + "WHERE PR_UPN='" + DataModel_Press[final_i1].getValueAt(ji, 1).toString() + "' AND "
//                                            + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                                    DataModel_Press[final_i1].setValueAt(mcanq, ji, 113);
//                                    mcanv = data.getDoubleValueFromDB("SELECT SUM(PR_FELT_VALUE_WITH_GST) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                            + "WHERE PR_UPN='" + DataModel_Press[final_i1].getValueAt(ji, 1).toString() + "' AND "
//                                            + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                                    DataModel_Press[final_i1].setValueAt(mcanv, ji, 114);
//                                } else {
//                                    macess = 0;
//                                    DataModel_Press[final_i1].setValueAt(macess, ji, 22);
//                                    DataModel_Press[final_i1].setValueAt(value * macess, ji, 23);
//                                }
//
//                                DataModel_Press[final_i1].setValueAt(value * Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 6).toString()), ji, 7);
//                                DataModel_Press[final_i1].setValueAt((mcb + macess) - (mwip + mstk + mdis), ji, 24);
//                                //DataModel_Press[final_i1].setValueAt(Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 6).toString()) - (Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 10).toString()) + Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 12).toString()) + Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 14).toString())), ji, 20);
//                                DataModel_Press[final_i1].setValueAt(value * Double.parseDouble(DataModel_Press[final_i1].getValueAt(ji, 24).toString()), ji, 25);
//                            }
//                            Double vTotal = data.getDoubleValueFromDB("SELECT CASE WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) "
//                                    + "WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_ACT_RD +MAR_EXP_RD) "
//                                    + "ELSE 0 END AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' ");
//                            lblppos[tabTest.getSelectedIndex()].setText("Total Exp. Act. Run Days : " + vTotal);
//                            lblpqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel_Press[final_i1].getValueAt(tblPress[final_i1].getSelectedRow(), 3).toString());
                        }
                    });
                    tblPress[i].getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent event) {
                            // do some actions here, for example
                            // print first column value from selected row
//                            Double vTotal = data.getDoubleValueFromDB("SELECT CASE WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) "
//                                    + "WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_ACT_RD +MAR_EXP_RD) "
//                                    + "ELSE 0 END AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' ");
//                            lblppos[tabTest.getSelectedIndex()].setText("Total Exp. Act. Run Days : " + vTotal);                            
//                            lblpqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel_Press[final_i1].getValueAt(tblPress[final_i1].getSelectedRow(), 3).toString());
                        }
                    });

                    jScrollPane2 = new JScrollPane();
                    jScrollPane2.setViewportView(tblPress[i]);

                    machine.add(jScrollPane2);
                    jScrollPane2.setBounds(0, 40, 1250, 60);

//                    tblDryer[i] = new JTable();
//                    DataModel_dryer[i] = new EITLTableModel();
//
//                    tblDryer[i].removeAll();
//                    tblDryer[i].setModel(DataModel_dryer[i]);
//                    //tblDryer.setBounds(10, 10, 500, 100);
//
//                    tblDryer[i].setAutoResizeMode(0);
//
//                    DataModel_dryer[i].addColumn("SrNo"); //0 - Read Only
//                    DataModel_dryer[i].addColumn("UPN"); //1
//                    DataModel_dryer[i].addColumn("Position Desc"); //2
//                    DataModel_dryer[i].addColumn("Quality"); //3
//                    DataModel_dryer[i].addColumn("Group"); //4
//                    DataModel_dryer[i].addColumn("Value"); //5
//                    DataModel_dryer[i].addColumn("CurrentProjQty"); //6
//                    DataModel_dryer[i].addColumn("CurrentProjValue"); //7
//                    DataModel_dryer[i].addColumn("CurrProj+GR Qty"); //6
//                    DataModel_dryer[i].addColumn("CurrProj+GR Value"); //7
//                    DataModel_dryer[i].addColumn("BudgetQty"); //8
//                    DataModel_dryer[i].addColumn("BudgetValue"); //9
//                    DataModel_dryer[i].addColumn("DispatchQty"); //10
//                    DataModel_dryer[i].addColumn("DispatchValue"); //11
//                    DataModel_dryer[i].addColumn("Prev GR Qty"); //12
//                    DataModel_dryer[i].addColumn("Prev GR Value"); //13
//                    DataModel_dryer[i].addColumn("WIPQty"); //12
//                    DataModel_dryer[i].addColumn("WIPValue"); //13
//                    DataModel_dryer[i].addColumn("In StockQty"); //14
//                    DataModel_dryer[i].addColumn("In StockValue"); //15
//                    DataModel_dryer[i].addColumn("ObsoleteQty"); //16
//                    DataModel_dryer[i].addColumn("ObsoleteValue"); //17
//                    DataModel_dryer[i].addColumn("ExcessQty"); //18
//                    DataModel_dryer[i].addColumn("ExcessValue"); //19
//
//                    DataModel_dryer[i].addColumn("ProjOfOrderQty"); //14
//                    DataModel_dryer[i].addColumn("ProjOfOrderValue"); //15
//
//                    for (int m = 4; m <= 12; m++) {
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + "Qty");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + "Value");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + "DoubtfulQty");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + "Remark");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + " Remarks for Doubtful");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + " Proposed action by AIC");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + " Proposed action for Doubtful");
//                    }
//                    for (int m = 1; m <= 3; m++) {
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + "Qty");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + "Value");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + "DoubtfulQty");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + "Remark");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + " Remarks for Doubtful");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + " Proposed action by AIC");
//                        DataModel_dryer[i].addColumn(mmonth.get(m) + " Proposed action for Doubtful");
//                    }
////                    DataModel_dryer[i].addColumn("AprQty"); //16          
////                    DataModel_dryer[i].addColumn("AprValue"); //17
////                    DataModel_dryer[i].addColumn("AprRemark"); //18
////                    DataModel_dryer[i].addColumn("MayQty"); //19
////                    DataModel_dryer[i].addColumn("MayValue"); //20
////                    DataModel_dryer[i].addColumn("MayRemark"); //21
////                    DataModel_dryer[i].addColumn("JunQty"); //22          
////                    DataModel_dryer[i].addColumn("JunValue"); //23
////                    DataModel_dryer[i].addColumn("JunRemark"); //24
////                    DataModel_dryer[i].addColumn("JulQty"); //25          
////                    DataModel_dryer[i].addColumn("JulValue"); //26
////                    DataModel_dryer[i].addColumn("JulRemark"); //27
////                    DataModel_dryer[i].addColumn("AugQty"); //28          
////                    DataModel_dryer[i].addColumn("AugValue"); //29
////                    DataModel_dryer[i].addColumn("AugRemark"); //30
////                    DataModel_dryer[i].addColumn("SepQty"); //31          
////                    DataModel_dryer[i].addColumn("SepValue"); //32
////                    DataModel_dryer[i].addColumn("SepRemark"); //33
////                    DataModel_dryer[i].addColumn("OctQty"); //34          
////                    DataModel_dryer[i].addColumn("OctValue"); //35
////                    DataModel_dryer[i].addColumn("OctRemark"); //36
////                    DataModel_dryer[i].addColumn("NovQty"); //37          
////                    DataModel_dryer[i].addColumn("NovValue"); //38
////                    DataModel_dryer[i].addColumn("NovRemark"); //39
////                    DataModel_dryer[i].addColumn("DecQty"); //40          
////                    DataModel_dryer[i].addColumn("DecValue"); //41
////                    DataModel_dryer[i].addColumn("DecRemark"); //42
////                    DataModel_dryer[i].addColumn("JanQty"); //40          
////                    DataModel_dryer[i].addColumn("JanValue"); //41
////                    DataModel_dryer[i].addColumn("JanRemark"); //42
////                    DataModel_dryer[i].addColumn("FebQty"); //40          
////                    DataModel_dryer[i].addColumn("FebValue"); //41
////                    DataModel_dryer[i].addColumn("FebRemark"); //42
////                    DataModel_dryer[i].addColumn("MarQty"); //40          
////                    DataModel_dryer[i].addColumn("MarValue"); //41
////                    DataModel_dryer[i].addColumn("MarRemark"); //42
//                    DataModel_dryer[i].addColumn("Incharge"); //43
//                    DataModel_dryer[i].addColumn("Size Criteria"); //44
//                    DataModel_dryer[i].addColumn("Party Group"); //45
//                    DataModel_dryer[i].addColumn("Cancel Qty");
//                    DataModel_dryer[i].addColumn("Cancel Value");
//                    DataModel_dryer[i].addColumn("ObsoleteWIPQty");
//                    DataModel_dryer[i].addColumn("ObsoleteWIPValue");
//                    DataModel_dryer[i].addColumn("ObsoleteStkQty");
//                    DataModel_dryer[i].addColumn("ObsoleteStkValue");
//
//                    for (int ro = 0; ro < 119; ro++) {
//                        if (EditMode == EITLERPGLOBAL.EDIT) {
//                            if (clsFeltProductionApprovalFlow.IsCreator(ModuleId, BudgetEntry.getAttribute("DOC_NO").getString() + "")) {
//                                if (ro == mchange || ro == mchange + 3  || ro==mchange+5) {
//
//                                } else {
//                                    DataModel_dryer[i].SetReadOnly(ro);
//                                }
//                            } else {
//                                if (ro == mchange || ro == mchange + 2 || ro == mchange + 3 || ro == mchange + 4 || ro == mchange + 5 || ro == mchange + 6) {
//
//                                } else {
//                                    DataModel_dryer[i].SetReadOnly(ro);
//                                }
//                            }
//
//                        } else {
//                            DataModel_dryer[i].SetReadOnly(ro);
//                        }
//                    }
//
//                    tblDryer[i].getColumnModel().getColumn(0).setMaxWidth(30);
//                    tblDryer[i].getColumnModel().getColumn(1).setMinWidth(90);
//                    tblDryer[i].getColumnModel().getColumn(2).setMinWidth(80);
//                    tblDryer[i].getColumnModel().getColumn(3).setMaxWidth(50);
//                    tblDryer[i].getColumnModel().getColumn(4).setMinWidth(50);
//                    tblDryer[i].getColumnModel().getColumn(5).setMaxWidth(60);
//                    tblDryer[i].getColumnModel().getColumn(6).setMaxWidth(60);
//                    tblDryer[i].getColumnModel().getColumn(8).setMinWidth(90);
//                    tblDryer[i].getColumnModel().getColumn(9).setMinWidth(80);
//                    tblDryer[i].getColumnModel().getColumn(10).setMinWidth(80);
//                    tblDryer[i].getColumnModel().getColumn(11).setMinWidth(80);
//                    tblDryer[i].getColumnModel().getColumn(12).setMinWidth(80);
//                    tblDryer[i].getColumnModel().getColumn(12).setMinWidth(80);
//                    tblDryer[i].getColumnModel().getColumn(14).setMinWidth(80);
//                    tblDryer[i].getColumnModel().getColumn(15).setMinWidth(80);
//                    tblDryer[i].getColumnModel().getColumn(16).setMinWidth(80);
//
//                    tblDryer[i].addKeyListener(new KeyAdapter() {
//                        @Override
//                        public void keyReleased(KeyEvent ke) {
//                            double mcb, mwip, mstk, mobs, macess, mdis, mcanq, mcanv;
//                            for (int ji = 0; ji < DataModel_dryer[final_i1].getRowCount(); ji++) {
//                                double value = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 5).toString());
//                                double pq = 0, pb = 0;
//                                mcb = mwip = mstk = mobs = macess = mdis = 0;
//                                System.out.println("Value at " + ji + " for " + final_i1 + ":" + DataModel_dryer[final_i1].getValueAt(ji, mchange).toString());
//                                try {
//                                    pq = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, mchange).toString());
////                                    if (mcurmonth < 4) {
////                                        pq = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 49 + ((mcurmonth - 1) * 3)).toString());
////                                    } else {
////                                        pq = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 22 + ((mcurmonth - 4) * 3)).toString());
////                                    }
//                                } catch (Exception nf) {
//                                    pq = -1;
//                                }
//                                System.out.println("PQ" + pq);
//                                System.out.println("Value:" + value);
//                                if (pq >= 0) {
//                                    DataModel_dryer[final_i1].setValueAt(value * pq, ji, mchange + 1);
////                                    if (mcurmonth < 4) {
////                                        DataModel_dryer[final_i1].setValueAt(value * pq, ji, 49 + ((mcurmonth - 1) * 3) + 1);
////                                    } else {
////                                        DataModel_dryer[final_i1].setValueAt(value * pq, ji, 22 + ((mcurmonth - 4) * 3) + 1);
////                                    }
//                                    DataModel_dryer[final_i1].setValueAt(pq, ji, 6);
//                                } else {
//
//                                    pb = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 10).toString());
//
//                                    DataModel_dryer[final_i1].setValueAt(pb, ji, 6);
//                                }
//                                try {
//                                    mcb = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 6).toString());
//                                } catch (Exception e) {
//                                    mcb = 0;
//                                }
//                                try {
//                                    mwip = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 16).toString());
//                                } catch (Exception e) {
//                                    mwip = 0;
//                                }
//                                try {
//                                    mstk = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 18).toString());
//                                } catch (Exception e) {
//                                    mstk = 0;
//                                }
//                                try {
//                                    mobs = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 20).toString());
//                                } catch (Exception e) {
//                                    mobs = 0;
//                                }
//                                try {
//                                    mdis = Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 12).toString());
//                                } catch (Exception e) {
//                                    mdis = 0;
//                                }
//                                if (mcb - (mwip + mstk + mdis) < 0) {
//                                    macess = (mwip + mstk + mdis) - mcb;
//                                    DataModel_dryer[final_i1].setValueAt(macess, ji, 22);
//                                    DataModel_dryer[final_i1].setValueAt(value * macess, ji, 23);
//                                    mcanq = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                            + "WHERE PR_UPN='" + DataModel_dryer[final_i1].getValueAt(ji, 1).toString() + "' AND "
//                                            + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                                    DataModel_dryer[final_i1].setValueAt(mcanq, ji, 113);
//                                    mcanv = data.getDoubleValueFromDB("SELECT SUM(PR_FELT_VALUE_WITH_GST) FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
//                                            + "WHERE PR_UPN='" + DataModel_dryer[final_i1].getValueAt(ji, 1).toString() + "' AND "
//                                            + "PR_PIECE_STAGE IN ('PLANNING','BOOKING') GROUP BY PR_UPN");
//                                    DataModel_dryer[final_i1].setValueAt(mcanv, ji, 114);
//                                } else {
//                                    macess = 0;
//                                    DataModel_dryer[final_i1].setValueAt(macess, ji, 22);
//                                    DataModel_dryer[final_i1].setValueAt(value * macess, ji, 23);
//                                }
//
//                                DataModel_dryer[final_i1].setValueAt(value * Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 6).toString()), ji, 7);
//                                DataModel_dryer[final_i1].setValueAt((mcb + macess) - (mwip + mstk + mdis), ji, 24);
//                                //DataModel_dryer[final_i1].setValueAt(Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 6).toString()) - (Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 10).toString()) + Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 12).toString()) + Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 14).toString())), ji, 20);
//                                DataModel_dryer[final_i1].setValueAt(value * Double.parseDouble(DataModel_dryer[final_i1].getValueAt(ji, 24).toString()), ji, 25);
//                            }
//                            lbldpos[tabTest.getSelectedIndex()].setText("Position :" + DataModel_dryer[final_i1].getValueAt(tblDryer[final_i1].getSelectedRow(), 2).toString());
//                            lbldqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel_dryer[final_i1].getValueAt(tblDryer[final_i1].getSelectedRow(), 3).toString());
//                        }
//                    });
//                    tblDryer[i].getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//                        public void valueChanged(ListSelectionEvent event) {
//                            // do some actions here, for example
//                            // print first column value from selected row
//                            lbldpos[tabTest.getSelectedIndex()].setText("Position :" + DataModel_dryer[final_i1].getValueAt(tblDryer[final_i1].getSelectedRow(), 2).toString());
//                            lbldqlt[tabTest.getSelectedIndex()].setText("Quality :" + DataModel_dryer[final_i1].getValueAt(tblDryer[final_i1].getSelectedRow(), 3).toString());
//                        }
//                    });
//
//                    jScrollPane3 = new JScrollPane();
//                    jScrollPane3.setViewportView(tblDryer[i]);
//
//                    jScrollPane3.setBounds(0, 200, 1250, 150);
//                    machine.add(jScrollPane3);
                    lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Press");
                    lblMachine.setBounds(0, 5, 200, 50);
                    machine.add(lblMachine);

//                    vTotal = data.getDoubleValueFromDB("SELECT SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) "
//                            + "AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' AND MACHINE_NO='" + r.getString("MACHINE_NO") + "' ");
                    
                    vTotal = data.getDoubleValueFromDB("SELECT SUM(Q_1_EXP_RD + Q_2_EXP_RD + Q_3_EXP_RD + Q_4_EXP_RD) "
                            + "AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' AND MACHINE_NO='" + r.getString("MACHINE_NO") + "' ");
                    
//                    vTotal = data.getDoubleValueFromDB("SELECT CASE WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=0 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) "
//                            + "WHEN MONTH(CURDATE())=4 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_EXP_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=5 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_EXP_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=6 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_EXP_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=7 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_EXP_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=8 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_EXP_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=9 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_EXP_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=10 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_EXP_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=11 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_EXP_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=12 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_EXP_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=1 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_EXP_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=2 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_EXP_RD +MAR_EXP_RD) WHEN MONTH(CURDATE())=3 AND COALESCE(APPROVED,0)=1 THEN SUM(APR_ACT_RD + MAY_ACT_RD + JUN_ACT_RD + JUL_ACT_RD + AUG_ACT_RD + SEP_ACT_RD + OCT_ACT_RD + NOV_ACT_RD + DEC_ACT_RD + JAN_ACT_RD + FEB_ACT_RD +MAR_EXP_RD) "
//                            + "ELSE 0 END AS TOTAL FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + DOC_NO1.getText().trim() + "' AND MACHINE_NO='" + r.getString("MACHINE_NO") + "' ");
                    lblTotal = new JLabel("Total Exp. Act. Run Days : " + vTotal);
                    lblTotal.setBounds(150, 5, 300, 50);
                    machine.add(lblTotal);

                    tblDryer[i] = new JTable();
                    tblDryer[i].setRowSelectionAllowed(true);
                    DataModel_dryer[i] = new EITLTableModel();
                    tblDryer[i].removeAll();
                    tblDryer[i].setModel(DataModel_dryer[i]);
                    tblDryer[i].setAutoResizeMode(0);
                    tblDryer[i].getTableHeader().setReorderingAllowed(false);

                    DataModel_dryer[i].addColumn("SrNo"); //0 - Read Only
                    DataModel_dryer[i].addColumn("UPN"); //1
                    DataModel_dryer[i].addColumn("Position"); //1
                    DataModel_dryer[i].addColumn("Position Desc"); //2
                    DataModel_dryer[i].addColumn("Avg. Life"); //3
                    DataModel_dryer[i].addColumn("Total Exp Consumption"); //4

                    tblDryer[i].setEnabled(false);

                    jScrollPane3 = new JScrollPane();
                    jScrollPane3.setViewportView(tblDryer[i]);

                    jScrollPane3.setBounds(0, 130, 1250, 150);
                    machine.add(jScrollPane3);

                    posTotal = data.getResult("SELECT D.MM_UPN_NO,D.MM_PARTY_CODE,D.MM_MACHINE_NO,D.MM_MACHINE_POSITION,D.MM_MACHINE_POSITION_DESC,D.MM_AVG_LIFE,COALESCE(CEILING(" + vTotal + "/D.MM_AVG_LIFE),0) AS CONSUMPTION "
                            + "FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER H,  PRODUCTION.FELT_MACHINE_MASTER_DETAIL D, PRODUCTION.FELT_QLT_RATE_MASTER R, DINESHMILLS.D_SAL_PARTY_MASTER P , PRODUCTION.FELT_MACHINE_POSITION_MST MP "
                            + "WHERE H.MM_DOC_NO=D.MM_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0  AND MM_ITEM_CODE = PRODUCT_CODE  AND EFFECTIVE_TO ='0000-00-00' AND POSITION_CATG=2 "
                            + "AND P.PARTY_CODE = H.MM_PARTY_CODE AND POSITION_NO+0 = MM_MACHINE_POSITION+0 "
                            + "AND D.MM_PARTY_CODE='" + r.getString("PARTY_CODE") + "' AND D.MM_MACHINE_NO='" + r.getString("MACHINE_NO") + "' AND COALESCE(D.POSITION_CLOSE_IND,0)=0 "
                            + "ORDER BY MM_PARTY_CODE,MM_MACHINE_NO,MM_MACHINE_POSITION*1");
                    posTotal.first();
                    rowDataT = new Object[100];

                    cntT = 1;
                    posT = 0;
                    cTotal = 0;
                    while (!posTotal.isAfterLast()) {
                        posT = 0;
                        rowDataT[posT] = cntT; //SrNo
                        posT++;
                        rowDataT[posT] = posTotal.getString("MM_UPN_NO");
                        posT++;
                        rowDataT[posT] = posTotal.getString("MM_MACHINE_POSITION");
                        posT++;
                        rowDataT[posT] = posTotal.getString("MM_MACHINE_POSITION_DESC");
                        posT++;
                        rowDataT[posT] = posTotal.getString("MM_AVG_LIFE");
                        posT++;
                        rowDataT[posT] = posTotal.getString("CONSUMPTION");
                        posT++;

                        cTotal = cTotal + Double.parseDouble(posTotal.getString("CONSUMPTION"));

                        cntT++;
                        DataModel_dryer[i].addRow(rowDataT);
                        posTotal.next();
                    }
                    posT = 0;
                    rowDataT[posT] = cntT; //SrNo
                    posT++;
                    rowDataT[posT] = "";
                    posT++;
                    rowDataT[posT] = "";
                    posT++;
                    rowDataT[posT] = "TOTAL";
                    posT++;
                    rowDataT[posT] = "";
                    posT++;
                    rowDataT[posT] = cTotal;
                    posT++;
                    DataModel_dryer[i].addRow(rowDataT);

                    final TableColumnModel columnModel2 = tblDryer[i].getColumnModel();
                    for (int column = 0; column < tblDryer[i].getColumnCount(); column++) {
                        int width = 60; // Min width
                        for (int row = 0; row < tblDryer[i].getRowCount(); row++) {
                            TableCellRenderer renderer = tblDryer[i].getCellRenderer(row, column);
                            Component comp = tblDryer[i].prepareRenderer(renderer, row, column);
                            width = Math.max(comp.getPreferredSize().width + 10, width);
                        }
                        if (width > 300) {
                            width = 300;
                        }
                        columnModel2.getColumn(column).setPreferredWidth(width);
                    }

//                    lblMachine = new JLabel("Machine " + r.getString("MACHINE_NO") + " Dryer");
//                    lblMachine.setBounds(0, 150, 200, 50);
//                    machine.add(lblMachine);
//                    lblppos[mlabelno].setBounds(150, 5, 300, 50);
//                    machine.add(lblppos[mlabelno]);
//                    lbldpos[mlabelno].setBounds(150, 150, 200, 50);
//                    machine.add(lbldpos[mlabelno]);
//
//                    lblpqlt[mlabelno].setBounds(550, 5, 200, 50);
//                    machine.add(lblpqlt[mlabelno]);
//                    lbldqlt[mlabelno].setBounds(550, 150, 200, 50);
//                    machine.add(lbldqlt[mlabelno]);
                    mmachine = r.getString("MACHINE_NO");
                    msr = msr1 = 1;
                    rowData = new Object[300];
//                    if (r.getString("GROUP_NAME").equalsIgnoreCase("HDS") || r.getString("GROUP_NAME").equalsIgnoreCase("SDF")) {
//                        pos = 0;
//                        rowData[pos] = msr1;
//                        pos++;
//                        rowData[pos] = r.getString("UPN");
//                        pos++;
//                        rowData[pos] = r.getString("POSITION_DESC");
//                        pos++;
//                        rowData[pos] = r.getString("QUALITY_NO");
//                        pos++;
//                        rowData[pos] = r.getString("GROUP_NAME");
//                        pos++;
//                        rowData[pos] = r.getDouble("SELLING_PRICE");
//                        pos++;
//                        rowData[pos] = r.getDouble("CURRENT_PROJECTION");
//                        pos++;
//                        rowData[pos] = r.getDouble("CURRENT_PROJECTION_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("CURRENT_PROJECTION") + r.getDouble("PREV_GR_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("CURRENT_PROJECTION_VALUE") + r.getDouble("PREV_GR_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("ACTUAL_BUDGET");
//                        pos++;
//                        rowData[pos] = r.getDouble("ACTUAL_BUDGET_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("DISPATCH_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("DISPATCH_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("PREV_GR_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("PREV_GR_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("WIP_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("WIP_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("STOCK_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("STOCK_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("OBSOLETE_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("OBSOLETE_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("ACESS_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("ACESS_VALUE");
//                        pos++;
//                        rowData[pos] = r.getDouble("PROJ_OF_ORDER_QTY");
//                        pos++;
//                        rowData[pos] = r.getDouble("PROJ_OF_ORDER_VALUE");
//                        pos++;
//
//                        for (int mn = 4; mn <= 12; mn++) {
//                            if (mn == mcurmonth && setvalblnk == 0 && r.getDouble(mmonth.get(mn) + "_BUDGET") == 0) {
//                                //System.out.println("BLANK" + mn);
//                            } else {
//                                rowData[pos] = r.getDouble(mmonth.get(mn) + "_BUDGET");
//                            }
//                            pos++;
//                            rowData[pos] = r.getDouble(mmonth.get(mn) + "_NET_AMOUNT");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_DOUBTFUL_QTY");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK_DOUBTFUL");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL");
//                            pos++;
//                        }
//                        for (int mn = 1; mn <= 3; mn++) {
//                            if (mn == mcurmonth && setvalblnk == 0 && r.getDouble(mmonth.get(mn) + "_BUDGET") == 0) {
//                                //System.out.println("BLANK" + mn);
//                            } else {
//                                rowData[pos] = r.getDouble(mmonth.get(mn) + "_BUDGET");
//                            }
//                            pos++;
//                            rowData[pos] = r.getDouble(mmonth.get(mn) + "_NET_AMOUNT");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_DOUBTFUL_QTY");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_REMARK_DOUBTFUL");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC");
//                            pos++;
//                            rowData[pos] = r.getString(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL");
//                            pos++;
//                        }
//
//                        rowData[pos] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='" + r.getString("INCHARGE") + "'");
//                        pos++;
//                        rowData[pos] = r.getString("SIZE_CRITERIA");
//                        pos++;
//                        rowData[pos] = r.getString("PARTY_GROUP");
//                        pos++;
//                        rowData[pos] = r.getString("CANCEL_QTY");
//                        pos++;
//                        rowData[pos] = r.getString("CANCEL_VALUE");
//                        pos++;
//                        rowData[pos] = r.getString("OBSOLETE_WIP_QTY");
//                        pos++;
//                        rowData[pos] = r.getString("OBSOLETE_WIP_VALUE");
//                        pos++;
//                        rowData[pos] = r.getString("OBSOLETE_STOCK_QTY");
//                        pos++;
//                        rowData[pos] = r.getString("OBSOLETE_STOCK_VALUE");
//                        pos++;
//
//                        msr1++;
//                        DataModel_dryer[i].addRow(rowData);
//                    } else {
                    pos = 0;
//                        rowData[pos] = msr;
                    rowData[pos] = r.getString("YEAR_FROM") + "-" + r.getString("YEAR_TO");
                    pos++;

//                    for (int mn = 4; mn <= 12; mn++) {
//                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_EXP_RD");
//                        pos++;
//                        rowData[pos] = r.getString(mmonth.get(mn) + "_EXP_RD_REMARK");
//                        pos++;
//                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_ACT_RD");
//                        pos++;
//                        rowData[pos] = r.getString(mmonth.get(mn) + "_ACT_RD_REMARK");
//                        pos++;
//                    }
//                    for (int mn = 1; mn <= 3; mn++) {
//                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_EXP_RD");
//                        pos++;
//                        rowData[pos] = r.getString(mmonth.get(mn) + "_EXP_RD_REMARK");
//                        pos++;
//                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_ACT_RD");
//                        pos++;
//                        rowData[pos] = r.getString(mmonth.get(mn) + "_ACT_RD_REMARK");
//                        pos++;
//                    }
                    
                    for (int mn = 1; mn <= 4; mn++) {
                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_EXP_RD");
                        pos++;
                        rowData[pos] = r.getString(mmonth.get(mn) + "_EXP_RD_REMARK");
                        pos++;
                        rowData[pos] = r.getDouble(mmonth.get(mn) + "_ACT_RD");
                        pos++;
                        rowData[pos] = r.getString(mmonth.get(mn) + "_ACT_RD_REMARK");
                        pos++;
                    }
                    
                    rowData[pos] = data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD='" + r.getString("INCHARGE") + "'");
                    pos++;
                    rowData[pos] = r.getString("PARTY_GROUP");
                    pos++;

                    msr++;
                    DataModel_Press[i].addRow(rowData);
//                    }
                }
                r.next();
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
            HashMap History = clsMachineRunForcastingEntry.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsMachineRunForcastingEntry ObjHistory = (clsMachineRunForcastingEntry) History.get(Integer.toString(i));
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
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6801, 68011)) { //7008,70081
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6801, 68012)) { //7008,70082
            cmdEdit.setEnabled(true);
        } else {
            cmdEdit.setEnabled(false);
        }

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6801, 68013)) {
            cmdDelete.setEnabled(false);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 6801, 68014)) {
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
        lblStatus1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        DOC_NO1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtyearfrom = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblyearto = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtpartycode = new javax.swing.JTextField();
        txtpartyname = new javax.swing.JLabel();
        tabTest = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablepartyinfo = new javax.swing.JTable();
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

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(lblStatus1);
        lblStatus1.setBounds(0, 550, 920, 30);

        jLabel1.setText("Document No");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 20, 100, 30);

        DOC_NO1.setEditable(false);
        DOC_NO1.setText("BU000001");
        jPanel1.add(DOC_NO1);
        DOC_NO1.setBounds(120, 20, 150, 30);

        jLabel2.setText("Year");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(280, 20, 40, 30);

        txtyearfrom.setEditable(false);
        jPanel1.add(txtyearfrom);
        txtyearfrom.setBounds(320, 20, 60, 30);

        jLabel3.setText("YYYY");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(320, 5, 60, 15);

        lblyearto.setText("YYYY");
        jPanel1.add(lblyearto);
        lblyearto.setBounds(390, 20, 60, 30);

        jLabel4.setText("Party Code");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 60, 110, 30);

        txtpartycode.setEditable(false);
        txtpartycode.setToolTipText("");
        jPanel1.add(txtpartycode);
        txtpartycode.setBounds(120, 60, 90, 30);

        txtpartyname.setText("Party Name");
        jPanel1.add(txtpartyname);
        txtpartyname.setBounds(120, 90, 410, 20);
        jPanel1.add(tabTest);
        tabTest.setBounds(10, 130, 1270, 390);

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
        jScrollPane1.setBounds(460, 10, 510, 40);

        Tab.addTab("Details", jPanel1);

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
        cmdBackToTab0.setBounds(450, 340, 102, 25);

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
        cmdNextToTab3.setBounds(560, 340, 102, 25);

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
        Tab2.setBounds(10, 0, 760, 410);

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
        ToolBar.setBounds(0, 0, 1310, 40);

        lblTitle1.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle1.setText("Machine Run Forcasting ");
        lblTitle1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle1.setOpaque(true);
        getContentPane().add(lblTitle1);
        lblTitle1.setBounds(0, 40, 1310, 30);
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
        // REPORT QUERY : SELECT A.DOC_NO,A.PIECE_NO,A.DL_REMARK,B.PR_MACHINE_NO,B.PR_POSITION_NO,B.PR_LENGTH,B.PR_WIDTH,B.PR_GSM,B.PR_GROUP,B.PR_STYLE,B.PR_SQMTR,B.PR_STYLE,B.PR_SYN_PER,B.PR_PIECE_REMARK,B.PR_PIECE_STAGE,B.PR_PRODUCT_CODE,B.PR_PARTY_CODE,B.PR_PO_NO,B.PR_PO_DATE,B.PR_REFERENCE_DATE,B.PR_ORDER_REMARK,B.PR_ORDER_DATE FROM  PRODUCTION.FELT_SALES_DIVERSION_LIST_APPROVAL A, PRODUCTION.FELT_SALES_PIECE_REGISTER B;
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

        Loader ObjLoader = new Loader(this, "EITLERP.FeltSales.MachineRunForcasting.frmFindMachineRunForcastingEntry", true);
        frmFindMachineRunForcastingEntry ObjFindfeltPieceDivision = (frmFindMachineRunForcastingEntry) ObjLoader.getObj();

        if (ObjFindfeltPieceDivision.Cancelled == false) {
            if (!BudgetEntry.Filter(ObjFindfeltPieceDivision.stringFindQuery)) {
                JOptionPane.showMessageDialog(this, "No records found.", "Find Machine Run Forcasting", JOptionPane.YES_OPTION);
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
        BudgetEntry.Filter(" DOC_NO IN (SELECT DISTINCT PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL, PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL.DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND USER_ID=" + EITLERPGLOBAL.gNewUserID + " AND STATUS='W' AND MODULE_ID=" + ModuleId + " AND CANCELED=0) ");
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
        double expRD = 0;
        double ob, pb, cb, dq;
        //int mcurmonth = data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL");
//        final int mcurmonth = Integer.parseInt(DOC_NO1.getText().substring(7, 9));
        final int mcurmonth = Integer.parseInt(DOC_NO1.getText().substring(8, 9));
//        if (OpgFinal.isSelected()) {
//            String chkdata = data.getStringValueFromDB("SELECT CONCAT('MRF2122',RIGHT(100+MONTH(CURDATE()),2)) FROM DUAL");
//            if (!DOC_NO1.getText().substring(0, 9).equalsIgnoreCase(chkdata)) {
//                JOptionPane.showMessageDialog(this, "Can not Final Approved Before or After Current Month...");
//                chk = false;
//                return false;
//            }
//        }
        //int mcurmonth = 4;
        mmonth = new HashMap();
//        mmonth.put(1, "JAN");
//        mmonth.put(2, "FEB");
//        mmonth.put(3, "MAR");
//        mmonth.put(4, "APR");
//        mmonth.put(5, "MAY");
//        mmonth.put(6, "JUN");
//        mmonth.put(7, "JUL");
//        mmonth.put(8, "AUG");
//        mmonth.put(9, "SEP");
//        mmonth.put(10, "OCT");
//        mmonth.put(11, "NOV");
//        mmonth.put(12, "DEC");
        
        mmonth.put(1, "Q_1");
        mmonth.put(2, "Q_2");
        mmonth.put(3, "Q_3");
        mmonth.put(4, "Q_4");
        
        change1 = new HashMap();
//        change1.put(1, 37);
//        change1.put(2, 41);
//        change1.put(3, 45);
//        change1.put(4, 1);
//        change1.put(5, 5);
//        change1.put(6, 9);
//        change1.put(7, 13);
//        change1.put(8, 17);
//        change1.put(9, 21);
//        change1.put(10, 25);
//        change1.put(11, 29);
//        change1.put(12, 33);
        
        change1.put(1, 1);
        change1.put(2, 5);
        change1.put(3, 9);
        change1.put(4, 13);
        
        int mchange = Integer.parseInt(change1.get(mcurmonth).toString());
        for (int p = 0; p < tabTest.getTabCount(); p++) {
            currmk = "";
            //tblPress[p].setEnabled(false);
            //tblDryer[p].setEnabled(false);
            for (int m = 0; m < tblPress[p].getRowCount(); m++) {
//                if (chkposclose(tblPress[p].getValueAt(m, 1).toString())) {
//                    if (Double.parseDouble(tblPress[p].getValueAt(m, 12).toString()) != Double.parseDouble(tblPress[p].getValueAt(m, 6).toString())) {
//                        err = err + "\n Position Closed so Dispatch Qty=Projected Qty For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                        chk = false;
//                    }
//                } else {
                ob = pb = cb = 0;
//                    try {
//                        ob = Double.parseDouble(tblPress[p].getValueAt(m, 10).toString());
//                    } catch (Exception e) {
//                        ob = 0;
//                    }
//                    try {
//                        dq = Double.parseDouble(tblPress[p].getValueAt(m, 12).toString());
//                    } catch (Exception e) {
//                        dq = 0;
//                    }
//                    try {
//                        cb = Double.parseDouble(tblPress[p].getValueAt(m, 6).toString());
//                    } catch (Exception e) {
//                        cb = 0;
//                    }
                try {
//                    if (mcurmonth < 4) {
//                        expRD = Double.parseDouble(tblPress[p].getValueAt(m, mchange).toString());
////                        currmk = tblPress[p].getValueAt(m, mchange + 1).toString();
////                            if (mcurmonth == 1) {
////                                pb = Double.parseDouble(tblPress[p].getValueAt(m, 46).toString());
////                            } else {
////                                pb = Double.parseDouble(tblPress[p].getValueAt(m, 49 + ((mcurmonth - 2) * 3)).toString());
////                            }
//                    } else {
//                        expRD = Double.parseDouble(tblPress[p].getValueAt(m, mchange).toString());
////                        currmk = tblPress[p].getValueAt(m, mchange + 1).toString();
////                            if (mcurmonth > 4) {
////                                pb = Double.parseDouble(tblPress[p].getValueAt(m, 22 + ((mcurmonth - 4) * 3)).toString());
////                            } else {
////                                pb = Double.parseDouble(tblPress[p].getValueAt(m, 8).toString());
////                            }
//                    }
////                        pb = getPreBudget(tblPress[p].getValueAt(m, 1).toString());
////                        if (mcurmonth == 4) {
////                            pb = Double.parseDouble(tblPress[p].getValueAt(m, 10).toString());
////                        }
                    expRD = Double.parseDouble(tblPress[p].getValueAt(m, mchange).toString());
                } catch (Exception e) {
                    pb = 0;
                }
//                    if (cb < dq) {
//                        err = err + "\n Budget Value is not less than  Dispatch Quantity For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                        chk = false;
//                    }
//                if (mcurmonth == 4) {
////                        if (cb != ob && currmk.equalsIgnoreCase("")) {
//                    if (currmk.equalsIgnoreCase("")) {
//                        err = err + "\n Remark is Compulsory For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                        chk = false;
//                    }
//                } else {
////                        if (cb != pb && currmk.equalsIgnoreCase("")) {
//                    if (currmk.equalsIgnoreCase("")) {
//                        err = err + "\n Remark is Compulsory For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                        chk = false;
//                    }
//                }
//                }
            }

            for (int m = 0; m < tblPress[p].getRowCount(); m++) {
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 1).toString());
//                if (expRD > 30) {
//                    err = err + "\n Apr Expected Days not more than 30 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 5).toString());
//                if (expRD > 31) {
//                    err = err + "\n May Expected Days not more than 31 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 9).toString());
//                if (expRD > 30) {
//                    err = err + "\n Jun Expected Days not more than 30 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 13).toString());
//                if (expRD > 31) {
//                    err = err + "\n Jul Expected Days not more than 31 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 17).toString());
//                if (expRD > 31) {
//                    err = err + "\n Aug Expected Days not more than 31 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 21).toString());
//                if (expRD > 30) {
//                    err = err + "\n Sep Expected Days not more than 30 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 25).toString());
//                if (expRD > 31) {
//                    err = err + "\n Oct Expected Days not more than 31 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 29).toString());
//                if (expRD > 30) {
//                    err = err + "\n Nov Expected Days not more than 30 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 33).toString());
//                if (expRD > 31) {
//                    err = err + "\n Dec Expected Days not more than 31 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 37).toString());
//                if (expRD > 31) {
//                    err = err + "\n Jan Expected Days not more than 30 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 41).toString());
//                if (expRD > 28) {
//                    err = err + "\n Feb Expected Days not more than 28 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
//                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 45).toString());
//                if (expRD > 31) {
//                    err = err + "\n Mar Expected Days not more than 31 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
//                    chk = false;
//                }
                
                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 1).toString());
                if (expRD > 91) {
                    err = err + "\n Q1 Expected Days not more than 91 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
                    chk = false;
                }
                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 5).toString());
                if (expRD > 92) {
                    err = err + "\n Q2 Expected Days not more than 92 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
                    chk = false;
                }
                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 9).toString());
                if (expRD > 92) {
                    err = err + "\n Q3 Expected Days not more than 92 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
                    chk = false;
                }
                expRD = Double.parseDouble(tblPress[p].getValueAt(m, 13).toString());
                if (expRD > 90) {
                    err = err + "\n Q4 Expected Days not more than 90 For " + tabTest.getTitleAt(p) + " In Press at Row :" + (m + 1);
                    chk = false;
                }
                
            }

//            for (int m = 0; m < tblDryer[p].getRowCount(); m++) {
//                if (chkposclose(tblDryer[p].getValueAt(m, 1).toString())) {
//                    if (Double.parseDouble(tblDryer[p].getValueAt(m, 12).toString()) != Double.parseDouble(tblDryer[p].getValueAt(m, 6).toString())) {
//                        err = err + "\n Position Closed so Dispatch Qty=Projected Qty For " + tabTest.getTitleAt(p) + " In Dryer at Row :" + (m + 1);
//                        chk = false;
//                    }
//                } else {
//                    ob = pb = cb = 0;
//                    try {
//                        ob = Double.parseDouble(tblDryer[p].getValueAt(m, 10).toString());
//                    } catch (Exception e) {
//                        ob = 0;
//                    }
//                    try {
//                        cb = Double.parseDouble(tblDryer[p].getValueAt(m, 6).toString());
//                    } catch (Exception e) {
//                        cb = 0;
//                    }
//                    try {
//                        dq = Double.parseDouble(tblDryer[p].getValueAt(m, 12).toString());
//                    } catch (Exception e) {
//                        dq = 0;
//                    }
//
//                    try {
//                        if (mcurmonth < 4) {
//                            currmk = tblDryer[p].getValueAt(m, mchange + 3).toString();
////                            if (mcurmonth == 1) {
////                                pb = Double.parseDouble(tblDryer[p].getValueAt(m, 46).toString());
////                            } else {
////                                pb = Double.parseDouble(tblDryer[p].getValueAt(m, 49 + ((mcurmonth - 2) * 3)).toString());
////                            }
//                        } else {
//                            currmk = tblDryer[p].getValueAt(m, mchange + 3).toString();
////                            if (mcurmonth > 4) {
////                                pb = Double.parseDouble(tblDryer[p].getValueAt(m, 22 + ((mcurmonth - 4) * 3)).toString());
////                            } else {
////                                pb = Double.parseDouble(tblDryer[p].getValueAt(m, 8).toString());
////                            }
//                        }
//                        pb = getPreBudget(tblDryer[p].getValueAt(m, 1).toString());
//                        if (mcurmonth == 4) {
//                            pb = Double.parseDouble(tblDryer[p].getValueAt(m, 10).toString());
//                        }
//                    } catch (Exception e) {
//                        pb = 0;
//                    }
//                    if (cb < dq) {
//                        err = err + "\n Budget Value is not less than  Dispatch Quantity For " + tabTest.getTitleAt(p) + " In Dryer at Row :" + (m + 1);
//                        chk = false;
//                    }
//
//                    if (mcurmonth == 4) {
//                        if (cb != ob && currmk.equalsIgnoreCase("")) {
//                            err = err + "\n Remark is Compulsory For " + tabTest.getTitleAt(p) + " In Dryer at Row :" + (m + 1);
//                            chk = false;
//                        }
//                    } else {
//                        if (cb != pb && currmk.equalsIgnoreCase("")) {
//                            err = err + "\n Remark is Compulsory For " + tabTest.getTitleAt(p) + " In Dryer at Row :" + (m + 1);
//                            chk = false;
//                        }
//                    }
//                }
//            }
        }
        if (!err.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, err);
        }
        return chk;
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
        cmdDelete.setEnabled(false);
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
        mmonth = new HashMap();
//        mmonth.put(1, "JAN");
//        mmonth.put(2, "FEB");
//        mmonth.put(3, "MAR");
//        mmonth.put(4, "APR");
//        mmonth.put(5, "MAY");
//        mmonth.put(6, "JUN");
//        mmonth.put(7, "JUL");
//        mmonth.put(8, "AUG");
//        mmonth.put(9, "SEP");
//        mmonth.put(10, "OCT");
//        mmonth.put(11, "NOV");
//        mmonth.put(12, "DEC");
        
        mmonth.put(1, "Q_1");
        mmonth.put(2, "Q_2");
        mmonth.put(3, "Q_3");
        mmonth.put(4, "Q_4");
        
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
        int gv;
        for (int p = 0; p < tabTest.getTabCount(); p++) {
            //tblPress[p].setEnabled(false);
            //tblDryer[p].setEnabled(false);
            for (int m = 0; m < tblPress[p].getRowCount(); m++) {
                clsMachineRunForcastingEntryItem ObjItem = new clsMachineRunForcastingEntryItem();

                ObjItem.setAttribute("DOC_NO", DOC_NO1.getText());
                ObjItem.setAttribute("YEAR_FROM", txtyearfrom.getText());
                ObjItem.setAttribute("YEAR_TO", lblyearto.getText());
                ObjItem.setAttribute("PARTY_CODE", txtpartycode.getText());
                ObjItem.setAttribute("PARTY_NAME", txtpartyname.getText());
                ObjItem.setAttribute("MACHINE_NO", tabTest.getTitleAt(p).substring(7).trim());

                gv = 1;
//                for (int mn = 4; mn <= 12; mn++) {
//                    ObjItem.setAttribute(mmonth.get(mn) + "_EXP_RD", Double.parseDouble(tblPress[p].getValueAt(m, gv).toString()));
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_EXP_RD_REMARK", tblPress[p].getValueAt(m, gv).toString());
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_ACT_RD", Double.parseDouble(tblPress[p].getValueAt(m, gv).toString()));
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_ACT_RD_REMARK", tblPress[p].getValueAt(m, gv).toString());
//                    gv++;
//                }
//                for (int mn = 1; mn <= 3; mn++) {
//                    ObjItem.setAttribute(mmonth.get(mn) + "_EXP_RD", Double.parseDouble(tblPress[p].getValueAt(m, gv).toString()));
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_EXP_RD_REMARK", tblPress[p].getValueAt(m, gv).toString());
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_ACT_RD", Double.parseDouble(tblPress[p].getValueAt(m, gv).toString()));
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_ACT_RD_REMARK", tblPress[p].getValueAt(m, gv).toString());
//                    gv++;
//                }
                
                for (int mn = 1; mn <= 4; mn++) {
                    ObjItem.setAttribute(mmonth.get(mn) + "_EXP_RD", Double.parseDouble(tblPress[p].getValueAt(m, gv).toString()));
                    gv++;
                    ObjItem.setAttribute(mmonth.get(mn) + "_EXP_RD_REMARK", tblPress[p].getValueAt(m, gv).toString());
                    gv++;
                    ObjItem.setAttribute(mmonth.get(mn) + "_ACT_RD", Double.parseDouble(tblPress[p].getValueAt(m, gv).toString()));
                    gv++;
                    ObjItem.setAttribute(mmonth.get(mn) + "_ACT_RD_REMARK", tblPress[p].getValueAt(m, gv).toString());
                    gv++;
                }
                //ObjItem.setAttribute("INCHARGE", data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_NAME='" + tblPress[p].getValueAt(m, 58).toString().trim() + "'"));
                //ObjItem.setAttribute("SIZE_CRITERIA", tblPress[p].getValueAt(m, 44).toString());
                //ObjItem.setAttribute("PARTY_GROUP", tblPress[p].getValueAt(m, 45).toString());

                BudgetEntry.colMRItems.put(Integer.toString(BudgetEntry.colMRItems.size() + 1), ObjItem);

            }

//            for (int m = 0; m < tblDryer[p].getRowCount(); m++) {
//                clsMachineRunForcastingEntryItem ObjItem = new clsMachineRunForcastingEntryItem();
//
//                ObjItem.setAttribute("DOC_NO", DOC_NO1.getText());
//                ObjItem.setAttribute("YEAR_FROM", txtyearfrom.getText());
//                ObjItem.setAttribute("YEAR_TO", lblyearto.getText());
//                ObjItem.setAttribute("PARTY_CODE", txtpartycode.getText());
//                ObjItem.setAttribute("PARTY_NAME", txtpartyname.getText());
//                ObjItem.setAttribute("MACHINE_NO", tabTest.getTitleAt(p).substring(7));
//                ObjItem.setAttribute("POSITION_NO", data.getStringValueFromDB("SELECT POSITION_NO FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE UPN='" + tblDryer[p].getValueAt(m, 1).toString() + "'"));
//
//                ObjItem.setAttribute("UPN", tblDryer[p].getValueAt(m, 1).toString());
//                //ObjItem.setAttribute("POSITION_DESC", tblPress[p].getValueAt(m, 2).toString());
//                //ObjItem.setAttribute("QUALITY_NO", tblPress[p].getValueAt(m, 3).toString());
//                //ObjItem.setAttribute("GROUP_NAME", tblPress[p].getValueAt(m, 4).toString());
//                //ObjItem.setAttribute("SELLING_PRICE", Double.parseDouble(tblPress[p].getValueAt(m, 5).toString()));
//                ObjItem.setAttribute("CURRENT_PROJECTION", Double.parseDouble(tblDryer[p].getValueAt(m, 6).toString()));
//                ObjItem.setAttribute("CURRENT_PROJECTION_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 7).toString()));
//                //ObjItem.setAttribute("ACTUAL_BUDGET", Double.parseDouble(tblPress[p].getValueAt(m, 8).toString()));
//                //ObjItem.setAttribute("ACTUAL_BUDGET_VALUE", Double.parseDouble(tblPress[p].getValueAt(m, 9).toString()));
//                //ObjItem.setAttribute("DISPATCH_QTY", Double.parseDouble(tblPress[p].getValueAt(m, 10).toString()));
//                //ObjItem.setAttribute("DISPATCH_VALUE", Double.parseDouble(tblPress[p].getValueAt(m, 11).toString()));
//                ObjItem.setAttribute("ACESS_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, 22).toString()));
//                ObjItem.setAttribute("ACESS_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 23).toString()));
//
//                ObjItem.setAttribute("PROJ_OF_ORDER_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, 24).toString()));
//                ObjItem.setAttribute("PROJ_OF_ORDER_VALUE", Double.parseDouble(tblDryer[p].getValueAt(m, 25).toString()));
//
//                gv = 26;
//                for (int mn = 4; mn <= 12; mn++) {
//                    try {
//                        ObjItem.setAttribute(mmonth.get(mn) + "_BUDGET", Double.parseDouble(tblDryer[p].getValueAt(m, gv).toString()));
//                    } catch (Exception e) {
//                        ObjItem.setAttribute(mmonth.get(mn) + "_BUDGET", 0.0);
//                    }
//                    gv++;
//                    try {
//                        ObjItem.setAttribute(mmonth.get(mn) + "_NET_AMOUNT", Double.parseDouble(tblDryer[p].getValueAt(m, gv).toString()));
//                    } catch (Exception e) {
//                        ObjItem.setAttribute(mmonth.get(mn) + "_NET_AMOUNT", 0.0);
//                    }
//                    gv++;
//                    try {
//                        ObjItem.setAttribute(mmonth.get(mn) + "_DOUBTFUL_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, gv).toString()));
//                    } catch (Exception e) {
//                        ObjItem.setAttribute(mmonth.get(mn) + "_DOUBTFUL_QTY", 0.0);
//                    }
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_REMARK", tblDryer[p].getValueAt(m, gv).toString());
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_REMARK_DOUBTFUL", tblDryer[p].getValueAt(m, gv).toString());
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC", tblDryer[p].getValueAt(m, gv).toString());
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL", tblDryer[p].getValueAt(m, gv).toString());
//                    gv++;
//                    
//                }
//                for (int mn = 1; mn <= 3; mn++) {
//                    ObjItem.setAttribute(mmonth.get(mn) + "_BUDGET", Double.parseDouble(tblDryer[p].getValueAt(m, gv).toString()));
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_NET_AMOUNT", Double.parseDouble(tblDryer[p].getValueAt(m, gv).toString()));
//                    gv++;
//                    try {
//                        ObjItem.setAttribute(mmonth.get(mn) + "_DOUBTFUL_QTY", Double.parseDouble(tblDryer[p].getValueAt(m, gv).toString()));
//                    } catch (Exception e) {
//                        ObjItem.setAttribute(mmonth.get(mn) + "_DOUBTFUL_QTY", 0.0);
//                    }
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_REMARK", tblDryer[p].getValueAt(m, gv).toString());
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_REMARK_DOUBTFUL", tblDryer[p].getValueAt(m, gv).toString());
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_PROPOSED_ACTION_BY_IC", tblDryer[p].getValueAt(m, gv).toString());
//                    gv++;
//                    ObjItem.setAttribute(mmonth.get(mn) + "_PROPOSED_ACTION_DOUBTFUL", tblDryer[p].getValueAt(m, gv).toString());
//                    gv++;
//                    
//                }
//                //ObjItem.setAttribute("INCHARGE", data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_NAME='" + tblPress[p].getValueAt(m, 58).toString().trim() + "'"));
//                //ObjItem.setAttribute("SIZE_CRITERIA", tblPress[p].getValueAt(m, 44).toString());
//                //ObjItem.setAttribute("PARTY_GROUP", tblPress[p].getValueAt(m, 45).toString());
//
//                BudgetEntry.colMRItems.put(Integer.toString(BudgetEntry.colMRItems.size() + 1), ObjItem);
//            }
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
    private javax.swing.JTabbedPane tabTest;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JLabel txtpartyname;
    private javax.swing.JTextField txtyearfrom;
    // End of variables declaration//GEN-END:variables

    private void update_budget(String mDocNo) {
        String mcurdt, mqtrdt, sql;
        mcurdt = EITLERPGLOBAL.getCurrentDateDB();
        int mdiff;
        mqtrdt = txtyearfrom.getText().trim() + "-06-30";
        mdiff = data.getIntValueFromDB("SELECT DATEDIFF('" + mcurdt + "','" + mqtrdt + "') FROM DUAL");
        if (mdiff <= 0) {
            sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                    + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
            data.Execute(sql);
            sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA)  "
                    + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1, "
                    + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA"
                    + "FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "WHERE DOC_NO='" + mDocNo + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
            data.Execute(sql);
        } else {
            mqtrdt = txtyearfrom.getText().trim() + "-09-30";
            mdiff = data.getIntValueFromDB("SELECT DATEDIFF('" + mcurdt + "','" + mqtrdt + "') FROM DUAL");
            if (mdiff <= 0) {
                sql = "TRUNCATE TABLE PRODUCTION.TMP_FELT_BUDGET";
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.TMP_FELT_BUDGET "
                        + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,"
                        + "DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,"
                        + "Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,TOTAL_KG,TOTAL_SQMTR,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1, "
                        + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA"
                        + "FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                        + "WHERE DOC_NO='" + mDocNo + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                data.Execute(sql);

                sql = "UPDATE PRODUCTION.FELT_BUDGET B,PRODUCTION.TMP_FELT_BUDGET D "
                        + "SET D.Q1=B.Q1,D.Q1KG=B.Q1KG,D.Q1SQMTR=B.Q1SQMTR "
                        + "WHERE D.KEY1=B.KEY1";
                data.Execute(sql);
                sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                        + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.TMP_FELT_BUDGET)";
                data.Execute(sql);
                sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                        + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA) "
                        + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                        + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                        + "FROM PRODUCTION.TMP_FELT_BUDGET "
                        + "WHERE KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                data.Execute(sql);

            } else {
                mqtrdt = txtyearfrom.getText().trim() + "-12-31";
                mdiff = data.getIntValueFromDB("SELECT DATEDIFF('" + mcurdt + "','" + mqtrdt + "') FROM DUAL");
                if (mdiff <= 0) {
                    sql = "TRUNCATE TABLE PRODUCTION.TMP_FELT_BUDGET";
                    data.Execute(sql);
                    sql = "INSERT INTO PRODUCTION.TMP_FELT_BUDGET  "
                            + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,"
                            + "DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,"
                            + "Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,TOTAL_KG,TOTAL_SQMTR,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                            + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                            + "FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                            + "WHERE DOC_NO='" + mDocNo + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                    data.Execute(sql);

                    sql = "UPDATE PRODUCTION.FELT_BUDGET B,PRODUCTION.TMP_FELT_BUDGET D "
                            + "SET D.Q1=B.Q1,D.Q1KG=B.Q1KG,D.Q1SQMTR=B.Q1SQMTR,"
                            + "D.Q2=B.Q2,D.Q2KG=B.Q2KG,D.Q2SQMTR=B.Q2SQMTR "
                            + "WHERE D.KEY1=B.KEY1";
                    data.Execute(sql);
                    sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                            + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.TMP_FELT_BUDGET)";
                    data.Execute(sql);
                    sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                            + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA) "
                            + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                            + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                            + "FROM PRODUCTION.TMP_FELT_BUDGET "
                            + "WHERE KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                    data.Execute(sql);

                } else {
                    mqtrdt = lblyearto.getText().trim() + "-03-31";
                    mdiff = data.getIntValueFromDB("SELECT DATEDIFF('" + mcurdt + "','" + mqtrdt + "') FROM DUAL");
                    if (mdiff <= 0) {
                        sql = "TRUNCATE TABLE PRODUCTION.TMP_FELT_BUDGET";
                        data.Execute(sql);
                        sql = "INSERT INTO PRODUCTION.TMP_FELT_BUDGET "
                                + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,STYLE,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,"
                                + "DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,"
                                + "Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,TOTAL_KG,TOTAL_SQMTR,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                                + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                                + "FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                                + "WHERE DOC_NO='" + mDocNo + "' AND KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                        data.Execute(sql);

                        sql = "UPDATE PRODUCTION.FELT_BUDGET B,PRODUCTION.TMP_FELT_BUDGET D "
                                + "SET D.Q1=B.Q1,D.Q1KG=B.Q1KG,D.Q1SQMTR=B.Q1SQMTR,"
                                + "D.Q2=B.Q2,D.Q2KG=B.Q2KG,D.Q2SQMTR=B.Q2SQMTR, "
                                + "D.Q3=B.Q3,D.Q3KG=B.Q3KG,D.Q3SQMTR=B.Q3SQMTR "
                                + "WHERE D.KEY1=B.KEY1";
                        data.Execute(sql);
                        sql = "DELETE FROM PRODUCTION.FELT_BUDGET "
                                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.TMP_FELT_BUDGET)";
                        data.Execute(sql);
                        sql = "INSERT INTO PRODUCTION.FELT_BUDGET (YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                                + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA)  "
                                + "SELECT YEAR_FROM,YEAR_TO,PARTY_CODE,PARTY_NAME,MACHINE_NO,POSITION_NO,POSITION_DESC,PRESS_LENGTH,PRESS_WIDTH,PRESS_GSM,PRESS_WEIGHT,PRESS_SQMTR,DRY_LENGTH,DRY_WIDTH,DRY_SQMTR,DRY_WEIGHT,QUALITY_NO,GROUP_NAME,SELLING_PRICE,SPL_DISCOUNT,WIP,STOCK,Q1,Q2,Q3,Q4,PARTY_STATUS,REMARKS,Q1KG,Q1SQMTR,Q2KG,Q2SQMTR,Q3KG,Q3SQMTR,Q4KG,Q4SQMTR,TOTAL,GST_PER,GROSS_AMOUNT,DISCOUNT_AMOUNT,NET_AMOUNT,PP_REMARKS,KEY1,"
                                + "UPN,POTENTIAL,PREV_YEAR_QTY,PREV_YEAR_VALUE,PREV_PREV_YEAR_QTY,PREV_PREV_YEAR_VALUE,WIP_31_MAR_QTY,WIP_31_MAR_VALUE,STOCK_31_MAR_QTY,STOCK_31_MAR_VALUE,LAST_INVOICE_DATE,AVG_LIFE,PROJECTED_MMYYYY,INCHARGE,SIZE_CRITERIA "
                                + "FROM PRODUCTION.TMP_FELT_BUDGET "
                                + "WHERE KEY1 NOT IN (SELECT KEY1 FROM PRODUCTION.FELT_BUDGET)";
                        data.Execute(sql);

                    }
                }
            }
        }
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET TOTAL=COALESCE(Q1,0)+COALESCE(Q2,0)+COALESCE(Q3,0)+COALESCE(Q4,0) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET TOTAL_KG=TOTAL*(COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0)),"
                + "TOTAL_SQMTR=TOTAL*(COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0)) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET GROSS_AMOUNT=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN TOTAL_SQMTR ELSE TOTAL_KG END)*SELLING_PRICE)*1.12,2) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET DISCOUNT_AMOUNT=ROUND(GROSS_AMOUNT*(SPL_DISCOUNT/100),2) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET NET_AMOUNT=GROSS_AMOUNT-DISCOUNT_AMOUNT"
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET Q1GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q1 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q1 END)*SELLING_PRICE)*1.12,2),"
                + "Q2GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q2 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q2 END)*SELLING_PRICE)*1.12,2),"
                + "Q3GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q3 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q3 END)*SELLING_PRICE)*1.12,2),"
                + "Q4GROSS=ROUND(((CASE WHEN LEFT(QUALITY_NO,1)='7' THEN (COALESCE(PRESS_SQMTR,0)+COALESCE(DRY_SQMTR,0))*Q4 ELSE (COALESCE(PRESS_WEIGHT,0)+COALESCE(DRY_WEIGHT,0))*Q4 END)*SELLING_PRICE)*1.12,2) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET Q1DISCOUNT=ROUND(Q1GROSS*(SPL_DISCOUNT/100),2),"
                + "Q2DISCOUNT=ROUND(Q2GROSS*(SPL_DISCOUNT/100),2),"
                + "Q3DISCOUNT=ROUND(Q3GROSS*(SPL_DISCOUNT/100),2),"
                + "Q4DISCOUNT=ROUND(Q4GROSS*(SPL_DISCOUNT/100),2)  "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
        sql = "UPDATE PRODUCTION.FELT_BUDGET "
                + "SET Q1NET_AMOUNT=ROUND(Q1GROSS-Q1DISCOUNT,2),"
                + "Q2NET_AMOUNT=ROUND(Q2GROSS-Q2DISCOUNT,2),"
                + "Q3NET_AMOUNT=ROUND(Q3GROSS-Q3DISCOUNT,2),"
                + "Q4NET_AMOUNT=ROUND(Q4GROSS-Q4DISCOUNT,2) "
                + "WHERE KEY1 IN (SELECT KEY1 FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL WHERE DOC_NO='" + mDocNo + "')";
        data.Execute(sql);
    }

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
            d = data.getDoubleValueFromDB("SELECT CURRENT_PROJECTION FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "WHERE UPN='" + mUPN + "' AND LEFT(DOC_NO, 7) = CONCAT('B2021',RIGHT(100 + (" + cmm + " - 1), 2))  AND "
                    + "YEAR_FROM = 2020 AND YEAR_TO = 2021");
        }
        if (cmm == 1) {
            d = data.getDoubleValueFromDB("SELECT CURRENT_PROJECTION FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "WHERE UPN='" + mUPN + "' AND  LEFT(DOC_NO, 7) = 'B202112'  AND "
                    + "YEAR_FROM = 2020 AND YEAR_TO = 2021");
        }
        if (cmm == 2) {
            d = data.getDoubleValueFromDB("SELECT CURRENT_PROJECTION FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "WHERE UPN='" + mUPN + "' AND  LEFT(DOC_NO, 7) = 'B202101'  AND "
                    + "YEAR_FROM = 2020 AND YEAR_TO = 2021");
        }
        if (cmm == 3) {
            d = data.getDoubleValueFromDB("SELECT CURRENT_PROJECTION FROM PRODUCTION.FELT_MACHINE_RUN_FORCASTING_DETAIL "
                    + "WHERE UPN='" + mUPN + "' AND  LEFT(DOC_NO, 7) = 'B202102'  AND "
                    + "YEAR_FROM = 2020 AND YEAR_TO = 2021");
        }
        return d;
    }
}
