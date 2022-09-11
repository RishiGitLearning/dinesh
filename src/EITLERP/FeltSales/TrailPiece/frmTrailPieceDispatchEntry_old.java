/*  
 *
 * 
 */
package EITLERP.FeltSales.TrailPiece;

/**
 *
 * @author Dharmendra
 */
import EITLERP.Production.ReportUI.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.FeltSales.PieceRegister.clsIncharge;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.MailNotification;
import EITLERP.FeltSales.common.SelectSortFields;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import java.lang.*;
import javax.swing.text.*;
import java.sql.*;
import java.lang.String;
import java.net.*;
import EITLERP.Utils.*;
import java.io.*;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import java.math.*;
import EITLERP.Production.ReportUI.JTextFieldHint;

import EITLERP.Stores.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import EITLERP.Purchase.*;
import TReportWriter.NumWord;
import java.math.BigDecimal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import static javax.swing.JDialog.setDefaultLookAndFeelDecorated;
import javax.swing.JFrame;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import EITLERP.Purchase.frmSendMail;
public class frmTrailPieceDispatchEntry_old extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsTrailPieceEntry Obj;
    private EITLERP.FeltSales.common.FeltInvCalc inv_calculation;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbIncharge;

    private EITLTableModel DataModelDesc;
    private EITLTableModel DataModelDiscount;
    private EITLTableModel DataModelA, DataModelH;
    private EITLTableModel DataModelHS;
    private EITLTableModel DataModelOtherpartyDiscount;

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelMainCode;
    private EITLComboModel cmodelProductGroup;
    private EITLComboModel cmbFromModel;

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    //clsColumn ObjColumn=new clsColumn();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private EITLComboModel cmbPriorityModel;
    String ORDER_BY = "";

    private boolean HistoryView = false;
    private String theDocNo = "";
    public frmPendingApprovals frmPA;
    private int charge09index = 0;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();
    String cellLastValue = " ORDER BY PR_PIECE_NO ";
    public boolean PENDING_DOCUMENT = false;
    private DecimalFormat df;
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

//    private  static ObservableList<PieChart.Data> details = FXCollections.observableArrayList();
//    private static PieChart pieChart;
    /**
     * Creates new form frmSalesParty
     */
    public frmTrailPieceDispatchEntry_old() {
        //this.requestFocus();

        System.gc();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
//        if (scrwidth > 800) {
//            scrwidth = 800;
//        }

        setSize(scrwidth, scrheight - 50);

        cmbIncharge = new EITLComboModel();
        cmodelProductGroup = new EITLComboModel();
        cmbFromModel = new EITLComboModel();

        initComponents();
        GenerateCombo();
        GenerateGroupCombo();
        GenerateYearCombo();
        int CurFinYear = EITLERPGLOBAL.getCurrentFinYear();
        EITLERPGLOBAL.setComboIndex(cmbFromYear, CurFinYear);
        df = new DecimalFormat("0.00");
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        btnShowDataActionPerformed(null);
        txtPartyCode.requestFocus();
        lblTitle.setForeground(Color.BLUE);
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableDesc = new javax.swing.JTable();
        btnsave = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyCode = new JTextFieldHint(new JTextField(),"Search by F1");
        jLabel5 = new javax.swing.JLabel();
        txtUPN = new javax.swing.JTextField();
        txtUPN = new JTextFieldHint(new JTextField(),"Search by F1");
        btnSorting = new javax.swing.JButton();
        btnShowData = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtPieceNo = new javax.swing.JTextField();
        txtPieceNo = new JTextFieldHint(new JTextField(),"Search by F1");
        lblPartyName = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbZone = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cmbProdGroup = new javax.swing.JComboBox();
        lblFromYear = new javax.swing.JLabel();
        cmbFromYear = new javax.swing.JComboBox();
        lblFromYear1 = new javax.swing.JLabel();
        lblToYear = new javax.swing.JLabel();
        txtToYear = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(211, 221, 225));
        lblTitle.setText("Felt Trail Piece Post Dispatch Detail Entry");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 0, 730, 25);

        Tab1.setLayout(null);

        TableDesc.setModel(new javax.swing.table.DefaultTableModel(
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
        TableDesc.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(TableDesc);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 1360, 450);

        btnsave.setText("SAVE");
        btnsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsaveActionPerformed(evt);
            }
        });
        Tab1.add(btnsave);
        btnsave.setBounds(10, 450, 130, 40);

        jTabbedPane1.addTab("Trail/Sample Piece[s] Post Dispatch Detail Entry", Tab1);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 130, 1370, 530);

        jLabel8.setText("PARTY CODE");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(10, 60, 100, 14);

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
        getContentPane().add(txtPartyCode);
        txtPartyCode.setBounds(110, 50, 120, 30);

        jLabel5.setText("UPN");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(250, 50, 40, 30);

        txtUPN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUPNFocusLost(evt);
            }
        });
        txtUPN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUPNKeyPressed(evt);
            }
        });
        getContentPane().add(txtUPN);
        txtUPN.setBounds(300, 50, 120, 30);

        btnSorting.setText("SORTING");
        btnSorting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortingActionPerformed(evt);
            }
        });
        getContentPane().add(btnSorting);
        btnSorting.setBounds(1060, 50, 130, 30);

        btnShowData.setText("SHOW DATA");
        btnShowData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowDataActionPerformed(evt);
            }
        });
        getContentPane().add(btnShowData);
        btnShowData.setBounds(1060, 80, 130, 30);

        jLabel9.setText("Piece No.");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(430, 60, 90, 14);

        txtPieceNo.setToolTipText("Press F1 ");
        txtPieceNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPieceNoFocusLost(evt);
            }
        });
        txtPieceNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPieceNoKeyPressed(evt);
            }
        });
        getContentPane().add(txtPieceNo);
        txtPieceNo.setBounds(520, 50, 120, 30);
        getContentPane().add(lblPartyName);
        lblPartyName.setBounds(110, 90, 410, 30);

        jLabel7.setText("Zone");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(650, 50, 50, 30);

        cmbZone.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbZone);
        cmbZone.setBounds(700, 50, 110, 30);

        jLabel6.setText("Product Group");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(820, 50, 120, 30);

        getContentPane().add(cmbProdGroup);
        cmbProdGroup.setBounds(920, 50, 110, 30);

        lblFromYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromYear.setText("Financial Year");
        getContentPane().add(lblFromYear);
        lblFromYear.setBounds(570, 100, 130, 14);

        cmbFromYear.setToolTipText("");
        cmbFromYear.setOpaque(false);
        cmbFromYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFromYearItemStateChanged(evt);
            }
        });
        getContentPane().add(cmbFromYear);
        cmbFromYear.setBounds(760, 90, 102, 30);

        lblFromYear1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromYear1.setText("From :");
        getContentPane().add(lblFromYear1);
        lblFromYear1.setBounds(680, 100, 70, 14);

        lblToYear.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        lblToYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToYear.setText("To :");
        getContentPane().add(lblToYear);
        lblToYear.setBounds(870, 100, 40, 15);

        txtToYear.setEditable(false);
        txtToYear.setOpaque(false);
        getContentPane().add(txtToYear);
        txtToYear.setBounds(920, 100, 102, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        if (txtPartyCode.getText().equals("")) {
            lblPartyName.setText("");
        } else if (data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE  COALESCE(PR_PIECETRIAL_FLAG,0)=1 AND PR_PARTY_CODE='" + txtPartyCode.getText() + "'").equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Invalid Party Code", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtPartyCode.setText("");
            lblPartyName.setText("");
            txtPartyCode.requestFocus();
        } else {
            lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, txtPartyCode.getText()));
        }
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT DISTINCT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' "
                    + "AND PARTY_CODE IN (SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1)";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);
                lblPartyName.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void btnSortingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortingActionPerformed
        // TODO add your handling code here:
        sort_query_creator();
        btnShowDataActionPerformed(null);
    }//GEN-LAST:event_btnSortingActionPerformed

    private void btnShowDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowDataActionPerformed
        // TODO add your handling code here:
        try {
            FormatGrid();
            String SQL;
            SQL = "SELECT PR_PIECE_NO,PR_PIECE_STAGE,PR_WIP_STATUS,PR_PARTY_CODE,PARTY_NAME,PR_DOC_NO,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(PR_ORDER_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE PR_ORDER_DATE END,'%d/%m/%Y'),'') AS ORDER_DATE,"
                    + "PR_UPN,PR_PRODUCT_CODE,PR_GROUP,PR_MACHINE_NO,POSITION_DESC,PR_STYLE,"
                    + "PR_LENGTH,PR_WIDTH,PR_GSM,PR_THORITICAL_WEIGHT,INCHARGE_NAME AS ZONE,"
                    + "COALESCE(PR_INVOICE_NO,'') AS INVOICE_NO,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE PR_INVOICE_DATE END,'%d/%m/%Y'),'') AS INVOICE_DATE,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(MOUNTING_PLAN_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE MOUNTING_PLAN_DATE END,'%d/%m/%Y'),'') AS MOUNTING_PLAN_DATE,"
                    + "COALESCE(DATE_FORMAT(CASE WHEN COALESCE(MOUNTING_ACTUAL_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE MOUNTING_ACTUAL_DATE END,'%d/%m/%Y'),'') AS MOUNTING_ACTUAL_DATE,"
                    + "COALESCE(EXPECTED_LIFE_DAYS,'') AS EXPECTED_LIFE_DAYS,"
                    + "COALESCE(ACTUAL_LIFE_DAYS,'') AS ACTUAL_LIFE_DAYS,"
                    + "COALESCE(PERFORMANCE_FEEDBACK,'') AS PERFORMANCE_FEEDBACK,"
                    + "COALESCE(REMARKS,'') AS REMARKS  "
                    + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "LEFT JOIN PRODUCTION.FELT_INCHARGE "
                    + "ON PR_INCHARGE=INCHARGE_CD "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST ON PR_POSITION_NO=POSITION_NO "
                    + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,PARTY_LOCK,PARTY_MILL_CLOSED_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS PM "
                    + "ON PR_PARTY_CODE=PARTY_CODE "
                    + "LEFT JOIN PRODUCTION.FELT_TRAIL_PIECE_DETAIL "
                    + "ON PR_PIECE_NO=TRAIL_PIECE_NO "
                    + "WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1  ";
            if (txtPartyCode.getText().trim().length() >= 6) {
                SQL = SQL + " AND PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }
            if (txtUPN.getText().trim().length() >= 6) {
                SQL = SQL + " AND PR_UPN='" + txtUPN.getText() + "'";
            }
            if (txtPieceNo.getText().trim().length() >= 5) {
                SQL = SQL + " AND PR_PIECE_NO='" + txtPieceNo.getText() + "'";
            }
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                SQL = SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {

                SQL = SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }
            SQL = SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

            SQL = SQL + " " + ORDER_BY;
            System.out.println("SQL:" + SQL);
            ResultSet tdata = data.getResult(SQL);
            tdata.first();
            if (tdata.getRow() > 0) {
                int srNo = 0;
                while (!tdata.isAfterLast()) {
                    srNo++;
                    Object[] rowData = new Object[100];
                    rowData[0] = srNo;
                    rowData[1] = tdata.getString("PR_PARTY_CODE");
                    rowData[2] = tdata.getString("PARTY_NAME");
                    rowData[3] = tdata.getString("PR_UPN");
                    rowData[4] = tdata.getString("PR_PIECE_NO");
                    rowData[5] = tdata.getString("MOUNTING_PLAN_DATE");
                    rowData[6] = tdata.getString("MOUNTING_ACTUAL_DATE");
                    rowData[7] = tdata.getString("EXPECTED_LIFE_DAYS");
                    rowData[8] = tdata.getString("ACTUAL_LIFE_DAYS");
                    rowData[9] = tdata.getString("PERFORMANCE_FEEDBACK");
                    rowData[10] = tdata.getString("REMARKS");
                    rowData[11] = tdata.getString("INVOICE_NO");
                    rowData[12] = tdata.getString("INVOICE_DATE");
                    rowData[13] = tdata.getString("PR_DOC_NO");
                    rowData[14] = tdata.getString("ORDER_DATE");
                    rowData[15] = tdata.getString("PR_PIECE_STAGE");
                    rowData[16] = tdata.getString("PR_WIP_STATUS");
                    rowData[17] = tdata.getString("PR_PRODUCT_CODE").trim();
                    rowData[18] = tdata.getString("PR_GROUP");
                    rowData[19] = tdata.getString("ZONE");
                    rowData[20] = tdata.getString("PR_MACHINE_NO");
                    rowData[21] = tdata.getString("POSITION_DESC");
                    rowData[22] = df.format(Double.parseDouble(tdata.getString("PR_LENGTH")));
                    rowData[23] = df.format(Double.parseDouble(tdata.getString("PR_WIDTH")));
                    rowData[24] = tdata.getString("PR_GSM");
                    rowData[25] = df.format(Double.parseDouble(tdata.getString("PR_THORITICAL_WEIGHT")));
                    DataModelDesc.addRow(rowData);
                    tdata.next();
                }
                final TableColumnModel columnModel = TableDesc.getColumnModel();
                for (int column = 0; column < TableDesc.getColumnCount(); column++) {
                    int width = 70; // Min width
                    for (int row = 0; row < TableDesc.getRowCount(); row++) {
                        TableCellRenderer renderer = TableDesc.getCellRenderer(row, column);
                        Component comp = TableDesc.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 1, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    if (column > 4 && column < 11) {
                        columnModel.getColumn(column).setPreferredWidth(130);
                    } else {
                        columnModel.getColumn(column).setPreferredWidth(width);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No Data Found...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnShowDataActionPerformed

    private void txtPieceNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPieceNoFocusLost
        // TODO add your handling code here:
         String SQL = "";
        SQL = "SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1 AND PR_PIECE_NO='" + txtPieceNo.getText() + "'";
        if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
            SQL = SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
        }
        if (!cmbZone.getSelectedItem().equals("ALL")) {

            SQL = SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
        }
        SQL = SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

        if (txtPieceNo.getText().equalsIgnoreCase("")) {

        } else if (data.getStringValueFromDB(SQL).equalsIgnoreCase(""))  {
            JOptionPane.showMessageDialog(this, "Invalid Piece No.", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtPieceNo.setText("");
            txtPieceNo.requestFocus();
        }
    }//GEN-LAST:event_txtPieceNoFocusLost

    private void txtPieceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPieceNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT DISTINCT PR_PIECE_NO,PR_PIECE_STAGE,PR_WIP_STATUS,PR_UPN,PR_PARTY_CODE,PR_MACHINE_NO,POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST "
                    + "ON PR_POSITION_NO=POSITION_NO "
                    + "WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1 AND PR_PIECE_STAGE IN ('IN STOCK','INVOICED','EXP-INVOICE','NEEDLING','MENDING','SEAMING','FINISHING','PLANNING','WEAVING','BSR','BOOKING') ";
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                aList.SQL = aList.SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {

                aList.SQL = aList.SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }
            aList.SQL = aList.SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

            if (txtPartyCode.getText().trim().length() >= 6) {
                aList.SQL = aList.SQL + "AND PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }
            if (txtUPN.getText().trim().length() >= 6) {
                aList.SQL = aList.SQL + "AND PR_UPN='" + txtUPN.getText() + "'";
            }

            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtPieceNo.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtPieceNoKeyPressed

    private void txtUPNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUPNKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            //aList.SQL="SELECT PARTY_CODE,NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER ORDER BY NAME";
            aList.SQL = "SELECT DISTINCT PR_UPN,PR_PARTY_CODE,PR_MACHINE_NO,POSITION_DESC FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST "
                    + "ON PR_POSITION_NO=POSITION_NO "
                    + "WHERE PR_PIECETRIAL_FLAG=1 AND PR_PIECE_STAGE IN ('IN STOCK','INVOICED','EXP-INVOICE','NEEDLING','MENDING','SEAMING','FINISHING','PLANNING','WEAVING','BSR','BOOKING') ";
            if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
                aList.SQL = aList.SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
            }
            if (!cmbZone.getSelectedItem().equals("ALL")) {

                aList.SQL = aList.SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
            }
            aList.SQL = aList.SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

            if (txtPartyCode.getText().trim().length() >= 6) {
                aList.SQL = aList.SQL + "AND PR_PARTY_CODE='" + txtPartyCode.getText() + "'";
            }
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 1;

            if (aList.ShowLOV()) {
                txtUPN.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtUPNKeyPressed

    private void txtUPNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUPNFocusLost
        // TODO add your handling code here:
        String SQL = "";
        SQL = "SELECT PR_UPN FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE COALESCE(PR_PIECETRIAL_FLAG,0)=1 AND PR_UPN='" + txtUPN.getText() + "'";
        if (!cmbProdGroup.getSelectedItem().equals("ALL")) {
            SQL = SQL + " AND PR_GROUP='" + cmbProdGroup.getSelectedItem() + "'";
        }
        if (!cmbZone.getSelectedItem().equals("ALL")) {

            SQL = SQL + " AND PR_INCHARGE = " + data.getStringValueFromDB("SELECT INCHARGE_CD FROM PRODUCTION.FELT_INCHARGE where INCHARGE_NAME='" + cmbZone.getSelectedItem() + "'");
        }
        SQL = SQL + " AND PR_ORDER_DATE>='" + cmbFromYear.getSelectedItem().toString() + "-04-01' AND PR_ORDER_DATE<='" + txtToYear.getText() + "-03-31' ";

        if (txtUPN.getText().equalsIgnoreCase("")) {

        } else if (data.getStringValueFromDB(SQL).equalsIgnoreCase(""))  {
            JOptionPane.showMessageDialog(this, "Invalid UPN", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtUPN.setText("");
            txtUPN.requestFocus();
        }
    }//GEN-LAST:event_txtUPNFocusLost

    private void btnsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsaveActionPerformed
        // TODO add your handling code here:
        if (validateData()) {
            try {
                Connection connection = data.getConn();
                connection.setAutoCommit(false);
                Statement stmt = connection.createStatement();

                String query1 = "UPDATE PRODUCTION.FELT_TRAIL_PIECE_DETAIL "
                        + "SET MOUNTING_PLAN_DATE=STR_TO_DATE( ?, '%Y-%m-%d'),"
                        + "MOUNTING_ACTUAL_DATE=STR_TO_DATE( ?, '%Y-%m-%d'),"
                        + "EXPECTED_LIFE_DAYS=?,"
                        + "ACTUAL_LIFE_DAYS=?,"
                        + "PERFORMANCE_FEEDBACK=?,"
                        + "REMARKS=? "
                        + "WHERE TRAIL_PIECE_NO=?";

                PreparedStatement p2 = connection.prepareStatement(query1);
                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");
                String mpdt, madt, med, mad;
                Double d;
                for (int i = 0; i < TableDesc.getRowCount(); i++) {

                    mpdt = TableDesc.getValueAt(i, 5).toString();
                    madt = TableDesc.getValueAt(i, 6).toString();
                    med = TableDesc.getValueAt(i, 7).toString();
                    mad = TableDesc.getValueAt(i, 8).toString();
                    if (mpdt.length() > 0) {
                        if (EITLERPGLOBAL.formatDateDB(mpdt).equalsIgnoreCase("")) {
                            p2.setString(1, null);
                        } else {
                            p2.setString(1, EITLERPGLOBAL.formatDateDB(mpdt));
                        }
                    } else {
                        p2.setString(1, null);
                    }
                    if (madt.length() > 0) {
                        if (EITLERPGLOBAL.formatDateDB(madt).equalsIgnoreCase("")) {
                            p2.setString(2, null);
                        } else {
                            p2.setString(2, EITLERPGLOBAL.formatDateDB(madt));
                        }
                    } else {
                        p2.setString(2, null);
                    }
                    if (med.length() > 0) {
                        try {
                            d = Double.parseDouble(med);
                            p2.setDouble(3, d);
                        } catch (Exception e) {
                            p2.setString(3, null);
                        }
                    } else {
                        p2.setString(3, null);
                    }
                    if (mad.length() > 0) {
                        try {
                            d = Double.parseDouble(mad);
                            p2.setDouble(4, d);
                        } catch (Exception e) {
                            p2.setString(4, null);
                        }
                    } else {
                        p2.setString(4, null);
                    }
                    p2.setString(5, TableDesc.getValueAt(i, 9).toString());
                    p2.setString(6, TableDesc.getValueAt(i, 10).toString());
                    p2.setString(7, TableDesc.getValueAt(i, 4).toString());
                    p2.addBatch();
                }

                p2.executeBatch();
                connection.commit();
                connection.setAutoCommit(true);
                JOptionPane.showMessageDialog(null, "Trail/Sample Piece[s] Dipstach Detail Saved...");
                btnShowDataActionPerformed(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnsaveActionPerformed

    private void cmbFromYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFromYearItemStateChanged
        // TODO add your handling code here:
        try {
            int ToYear = Integer.parseInt((String) cmbFromYear.getSelectedItem()) + 1;
            txtToYear.setText(Integer.toString(ToYear));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbFromYearItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Tab1;
    private javax.swing.JTable TableDesc;
    private javax.swing.JButton btnShowData;
    private javax.swing.JButton btnSorting;
    private javax.swing.JButton btnsave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbFromYear;
    private javax.swing.JComboBox cmbProdGroup;
    private javax.swing.JComboBox cmbZone;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblFromYear;
    private javax.swing.JLabel lblFromYear1;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblToYear;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPieceNo;
    private javax.swing.JTextField txtToYear;
    private javax.swing.JTextField txtUPN;
    // End of variables declaration//GEN-END:variables

    private void FormatGrid() {

        Updating = true; //Stops recursion
        try {

            DataModelDesc = new EITLTableModel();
            TableDesc.removeAll();
            TableDesc.setModel(DataModelDesc);
            TableColumnModel ColModel = TableDesc.getColumnModel();
            TableDesc.setAutoResizeMode(TableDesc.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);

            DataModelDesc.addColumn("Sr.No.");  //0 - Read Only     
            DataModelDesc.addColumn("Party Code");//1
            DataModelDesc.addColumn("Name");//2
            DataModelDesc.addColumn("UPN"); //3
            DataModelDesc.addColumn("Piece No"); //4
            DataModelDesc.addColumn("Mounting Plan Date"); //5
            DataModelDesc.addColumn("Mounting Actual Date"); //6 
            DataModelDesc.addColumn("Expected Life Days"); //7       
            DataModelDesc.addColumn("Actual Life Days"); //8       
            DataModelDesc.addColumn("Performance Feedback"); //9
            DataModelDesc.addColumn("Remarks");//10
            DataModelDesc.addColumn("Invoice No.");//11
            DataModelDesc.addColumn("Invoice Date");//12
            DataModelDesc.addColumn("Doc No.");//13
            DataModelDesc.addColumn("Order Date");//14
            DataModelDesc.addColumn("Piece Stage"); //15
            DataModelDesc.addColumn("WIP Status"); //16         
            DataModelDesc.addColumn("Product");//17
            DataModelDesc.addColumn("Group"); //18
            DataModelDesc.addColumn("Zone"); //19
            DataModelDesc.addColumn("Machine"); //20
            DataModelDesc.addColumn("Position"); //21            
            DataModelDesc.addColumn("Length"); //22
            DataModelDesc.addColumn("Width"); //23
            DataModelDesc.addColumn("GSM"); //24
            DataModelDesc.addColumn("Weight");   //25

            for (int i = 0; i <= 25; i++) {
                if (i >= 5 || i <= 10) {
                    DataModelDesc.ResetReadOnly(i);
                } else {
                    DataModelDesc.SetReadOnly(i);
                }
            }

            TableDesc.getColumnModel().getColumn(0).setMaxWidth(50);
            TableDesc.getColumnModel().getColumn(0).setCellRenderer(Renderer);
            //TableDesc.getColumnModel().getColumn(16).setPreferredWidth(100);

        } catch (Exception e) {

        }
        Updating = false;
    }

    public void sort_query_creator() {
        SelectSortFields sort = new SelectSortFields();

        sort.setField("PR_PIECE_NO", "PIECE NO");
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
        sort.setField("PR_PIECE_STAGE", "PIECE STAGE");
        //sort.setField("PR_STATUS", "PIECE STATUS");
        ORDER_BY = sort.getQuery(SelectSortFields.DEFAULT_ORDER.ASCENDING);
    }

    private boolean validateData() {
        String msg = "", mpdt, madt, med, mad;
        Double d;
        for (int i = 0; i < TableDesc.getRowCount(); i++) {
            mpdt = TableDesc.getValueAt(i, 5).toString();
            madt = TableDesc.getValueAt(i, 6).toString();
            med = TableDesc.getValueAt(i, 7).toString();
            mad = TableDesc.getValueAt(i, 8).toString();
            if (mpdt.length() > 0) {
                if (EITLERPGLOBAL.formatDateDB(mpdt).equalsIgnoreCase("")) {
                    msg = msg + "\n Invalid Mounting Plan Date at " + i;
                }
            }
            if (madt.length() > 0) {
                if (EITLERPGLOBAL.formatDateDB(madt).equalsIgnoreCase("")) {
                    msg = msg + "\n Invalid Mounting Actual Date at " + i;
                }
            }
            if (med.length() > 0) {
                try {
                    d = Double.parseDouble(med);
                } catch (Exception e) {
                    msg = msg + "\n Invalid Expected Life Days at " + i;
                }
            }
            if (mad.length() > 0) {
                try {
                    d = Double.parseDouble(mad);
                } catch (Exception e) {
                    msg = msg + "\n Invalid Actual Life Days at " + i;
                }
            }
        }
        if (msg.length() > 0) {
            JOptionPane.showMessageDialog(this, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
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

    private void GenerateYearCombo() {
        HashMap List = new HashMap();

        cmbFromYear.setModel(cmbFromModel);
        cmbFromYear.removeAllItems();

        List = clsFinYear.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);

        for (int i = 1; i <= List.size(); i++) {
            clsFinYear ObjYear = (clsFinYear) List.get(Integer.toString(i));

            ComboData cmbData = new ComboData();
            cmbData.Text = Integer.toString((int) ObjYear.getAttribute("YEAR_FROM").getVal());
            cmbData.Code = (int) ObjYear.getAttribute("YEAR_FROM").getVal();
            cmbData.strCode = Integer.toString((int) ObjYear.getAttribute("YEAR_FROM").getVal());
            cmbFromModel.addElement(cmbData);
        }
    }
}
