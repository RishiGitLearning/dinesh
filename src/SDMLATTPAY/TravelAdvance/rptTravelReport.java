/*
 * frmChangePassword.java
 *
 * Created on July 3, 2004, 3:36 PM
 */
package SDMLATTPAY.TravelAdvance;

import EITLERP.*;
import EITLERP.FeltSales.PieceRegister.clsIncharge;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
//import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
//import EITLERP.Sales.clsExcelExporter;

/*<APPLET CODE=frmChangePassword HEIGHT=200 WIDTH=430></APPLET>*/
/**
 *
 * @author Daxesh Prajapati
 */
public class rptTravelReport extends javax.swing.JApplet {

    private EITLTableModel DataModel = new EITLTableModel();

    private EITLComboModel modelDept = new EITLComboModel();
    private EITLComboModel modelShift = new EITLComboModel();
    private EITLComboModel modelMainCategory = new EITLComboModel();
    private EITLComboModel modelCategory = new EITLComboModel();
    private EITLComboModel cmbIncharge = new EITLComboModel();
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    private int prvmnth, curmnth, remainmonth, currentmonth;
    private String pmnth, cmnth, pfinyear, pyear;

    private int st = 0;

    //GenerateInvoiceParameterModificationCombo();

    /**
     * Initializes the applet frmChangePassword
     */
    public void init() {
        initComponents();
        setSize(1000, 750);

        //canpieced.setVisible(false);
        jLabel1.setForeground(Color.WHITE);
        txtpartyname.setEnabled(false);
        GenerateCombo();
        FormatGridTPW();
        btnPWViewActionPerformed(null);
        st = 1;

    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ExporttoExcelFileChooser = new javax.swing.JFileChooser();
        file1 = new javax.swing.JFileChooser();
        CANEXCESSGRUP = new javax.swing.ButtonGroup();
        YTMGROUP = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TabList = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        btnPWView = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane25 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        btnEmpMstETE = new javax.swing.JButton();
        txtpartycode = new javax.swing.JTextField();
        txtpartyname = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        lblDate1 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        INCHARGE = new javax.swing.JComboBox();

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Travel Report");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);

        jLabel2.setBackground(new java.awt.Color(0, 102, 153));
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);

        TabList.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabListStateChanged(evt);
            }
        });

        jPanel12.setLayout(null);

        btnPWView.setText("View");
        btnPWView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPWViewActionPerformed(evt);
            }
        });
        jPanel12.add(btnPWView);
        btnPWView.setBounds(0, 0, 100, 30);

        TabList.addTab("Partywise", jPanel12);

        btnClear.setText("Clear All");
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

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
        jScrollPane25.setViewportView(Table);

        btnEmpMstETE.setLabel("Export to Excel");
        btnEmpMstETE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpMstETEActionPerformed(evt);
            }
        });

        txtpartycode.setToolTipText("Press F1 key for search Party Code");
        txtpartycode = new JTextFieldHint(new JTextField(),"Search by F1");
        txtpartycode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtpartycodeFocusLost(evt);
            }
        });
        txtpartycode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpartycodeKeyPressed(evt);
            }
        });

        txtpartyname.setDisabledTextColor(java.awt.Color.black);
        txtpartyname = new JTextFieldHint(new JTextField(),"Party Name");
        txtpartyname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpartynameActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Party Code : ");

        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setText("From Date : ");

        txtFromDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();

        lblDate1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate1.setText("To Date : ");

        txtToDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Zone : ");

        INCHARGE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Sales Eng 1", "Sales Eng 2", "Sales Eng 3" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TabList, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtpartycode, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtpartyname, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(INCHARGE, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(btnEmpMstETE, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 762, Short.MAX_VALUE))
                    .addComponent(jScrollPane25))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtpartycode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtpartyname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(INCHARGE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(TabList, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEmpMstETE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtpartycode.setText("");
        cmbIncharge.setSelectedItem("ALL");
    }//GEN-LAST:event_btnClearActionPerformed


    private void btnEmpMstETEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpMstETEActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(Table, new File(file1.getSelectedFile().toString() + ".xls"), "Sheet1");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnEmpMstETEActionPerformed

    private void TabListStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabListStateChanged
        // TODO add your handling code here:
        try {
            if (TabList.getTitleAt(TabList.getSelectedIndex()).equals("Partywise")) {
                FormatGridTPW();
                txtpartycode.setEnabled(true);
                txtpartycode.setText("");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_TabListStateChanged

    private void txtpartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtpartycodeFocusLost
        // TODO add your handling code here:
        if (!txtpartycode.getText().trim().equals("") && data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + txtpartycode.getText().trim() + "' AND MAIN_ACCOUNT_CODE=210010 ")) {
            txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, txtpartycode.getText()));

        } else {
            if (!txtpartycode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Party Code doesn't exist/under approval.");
            }
            txtpartycode.setText("");
            txtpartyname.setText("");
        }
    }//GEN-LAST:event_txtpartycodeFocusLost

    private void txtpartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpartycodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            //            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_CLOSE_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 ";
//            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,PARTY_CLOSE_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0";
            aList.SQL = "SELECT DISTINCT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtpartycode.setText(aList.ReturnVal);
                txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtpartycodeKeyPressed

    private void txtpartynameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpartynameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpartynameActionPerformed

    private void btnPWViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPWViewActionPerformed
        // TODO add your handling code here:
        GeneratePPW();
    }//GEN-LAST:event_btnPWViewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup CANEXCESSGRUP;
    private javax.swing.JFileChooser ExporttoExcelFileChooser;
    private javax.swing.JComboBox INCHARGE;
    private javax.swing.JTabbedPane TabList;
    private javax.swing.JTable Table;
    private javax.swing.ButtonGroup YTMGROUP;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEmpMstETE;
    private javax.swing.JButton btnPWView;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDate1;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JTextField txtpartyname;
    // End of variables declaration//GEN-END:variables

    private void FormatGridTPW() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.");   //0     
        DataModel.addColumn("PARTY CODE");//1
        DataModel.addColumn("NAME");//2
        DataModel.addColumn("Visit Date");//3
        DataModel.addColumn("Visit By");//4
        DataModel.addColumn("Visit By");//5
        DataModel.addColumn("Designation");//6
        DataModel.addColumn("Visit Person");//7
        DataModel.addColumn("Purpose of Visit");//8        
        DataModel.addColumn("Document");//9
        DataModel.addColumn("Doc No");//10
        DataModel.addColumn("Incharge");//11
        DataModel.addColumn("Doc Date");//12
        DataModel.addColumn("Doc Sr.No.");//13
        DataModel.addColumn("RunTime");//14

        int ImportCol = 9;
        Table.getColumnModel().getColumn(ImportCol).setCellEditor(new ButtonEditor(new JCheckBox()));
        Table.getColumnModel().getColumn(ImportCol).setCellRenderer(new ButtonRenderer());

        ImportCol = 10;
        Table.getColumnModel().getColumn(ImportCol).setCellEditor(new ButtonEditor(new JCheckBox()));
        Table.getColumnModel().getColumn(ImportCol).setCellRenderer(new ButtonRenderer());

        DataModel.SetReadOnly(0);
        DataModel.SetReadOnly(1);
        DataModel.SetReadOnly(2);
        DataModel.SetReadOnly(3);
        DataModel.SetReadOnly(4);
        DataModel.SetReadOnly(5);
        DataModel.SetReadOnly(6);
        DataModel.SetReadOnly(7);
        DataModel.SetReadOnly(8);
        DataModel.SetReadOnly(11);
        DataModel.SetReadOnly(12);
        DataModel.SetReadOnly(13);
        DataModel.SetReadOnly(14);
    }

    private void GeneratePPW() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            double gobqty, gobkg, gobval, gppqty, gppkg, gppval, gcpqty, gcpkg, gcpval, gdppqty, gdppkg, gdppval, gdcpqty, gdcpkg, gdcpval,
                    pobqty, pobkg, pobval, pppqty, pppkg, pppval, pcpqty, pcpkg, pcpval, pdppqty, pdppkg, pdppval, pdcpqty, pdcpkg, pdcpval,
                    zobqty, zobkg, zobval, zppqty, zppkg, zppval, zcpqty, zcpkg, zcpval, zdppqty, zdppkg, zdppval, zdcpqty, zdcpkg, zdcpval;
            gobqty = gobkg = gobval = gppqty = gppkg = gppval = gcpqty = gcpkg = gcpval = gdppqty = gdppkg = gdppval = gdcpqty = gdcpkg = gdcpval
                    = pobqty = pobkg = pobval = pppqty = pppkg = pppval = pcpqty = pcpkg = pcpval = pdppqty = pdppkg = pdppval = pdcpqty = pdcpkg = pdcpval
                    = zobqty = zobkg = zobval = zppqty = zppkg = zppval = zcpqty = zcpkg = zcpval = zdppqty = zdppkg = zdppval = zdcpqty = zdcpkg = zdcpval = 0;

            String p, z;
            FormatGridTPW(); //clear existing content of table
            ResultSet rsTmp;

            if (!txtpartycode.getText().trim().equals("")) {
                cndtn += " AND DOC_PARTY_CODE= '" + txtpartycode.getText().trim() + "' ";
            }
            if (!INCHARGE.getSelectedItem().equals("ALL")) {
                cndtn += " AND CASE WHEN COALESCE(INCHARGE_NAME,'')='' THEN 'OTHER' ELSE INCHARGE_NAME END = '" + INCHARGE.getSelectedItem() + "' ";
            }
            cndtn += " AND TSD_START_DATE>='" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) + "' ";
            cndtn += " AND TSD_START_DATE<='" + EITLERPGLOBAL.formatDateDB(txtToDate.getText()) + "' ";

            String strSQL = "SELECT D.*,V.*,CASE WHEN COALESCE(INCHARGE_NAME,'')='' THEN 'OTHER' ELSE INCHARGE_NAME END AS INCHARGE_NAME "
                    + "FROM  SDMLATTPAY.TRAVEL_VOUCHER_DETAIL D "
                    + "LEFT JOIN DOC_MGMT.TRAVEL_VOUCHER V "
                    + "ON COALESCE(TSD_DOC_NO,'')=COALESCE(DOCUMENT_DOC_NO,'') AND COALESCE(DOC_PARTY_NAME,'')=COALESCE(TSD_START_PLACE,'') "
                    + "LEFT JOIN (SELECT PARTY_CODE,INCHARGE_CD AS INCHARGECD FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS M ON PARTY_CODE=DOC_PARTY_CODE "
                    + "LEFT JOIN PRODUCTION.FELT_INCHARGE I ON I.INCHARGE_CD=M.INCHARGECD "
                    + "WHERE TSD_INFO='PARTY' AND COALESCE(DOCUMENT_DOC_NO,'')!='' "
                    + cndtn
                    + " ORDER BY DOC_PARTY_CODE";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
//                p = rsTmp.getString("PROD_GROUP");
//                z = rsTmp.getString("INCHARGE_NAME");
                DecimalFormat df = new DecimalFormat("###.##");
                String mcurdttime = EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime();
                while (!rsTmp.isAfterLast()) {
                    cnt++;

                    Object[] rowData = new Object[100];

                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("TSD_TRAVEL_MODE");
                    rowData[2] = rsTmp.getString("TSD_START_PLACE");
                    rowData[3] = EITLERPGLOBAL.formatDate(rsTmp.getString("TSD_START_DATE"));
                    rowData[4] = rsTmp.getString("TSD_PAY_EMP_NO");
                    rowData[5] = rsTmp.getString("TSD_NAME");
                    rowData[6] = rsTmp.getString("TSD_DESIGNATION");
                    rowData[7] = rsTmp.getString("TSD_SEMINAR");
                    rowData[8] = rsTmp.getString("TSD_PURPOSE_OF_VISIT");
                    rowData[9] = rsTmp.getString("DOC_NAME");
                    rowData[10] = rsTmp.getString("TSD_DOC_NO");
                    rowData[11] = rsTmp.getString("INCHARGE_NAME");
                    rowData[12] = EITLERPGLOBAL.formatDate(rsTmp.getString("TSD_DOC_DATE"));
                    rowData[13] = rsTmp.getString("DOCUMENT_SR_NO");
                    rowData[14] = mcurdttime;

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }

                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 100; // Min width
                    for (int row = 0; row < Table.getRowCount(); row++) {
                        TableCellRenderer renderer = Table.getCellRenderer(row, column);
                        Component comp = Table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            } else {
                if (st == 1) {
                    JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
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

                if (Table.getSelectedColumn() == 9) {
                    openFile();
                }
                if (Table.getSelectedColumn() == 10) {
                    openDoc();
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

    public void openFile() {
        try {
            int PDocId = data.getIntValueFromDB("SELECT DOC_ID FROM DOC_MGMT.TRAVEL_VOUCHER "
                    + "WHERE DOCUMENT_DOC_NO='" + Table.getValueAt(Table.getSelectedRow(), 10) + "' AND "
                    + "DOCUMENT_SR_NO=" + Table.getValueAt(Table.getSelectedRow(), 13));

            String PFileName = Table.getValueAt(Table.getSelectedRow(), 9).toString();
            ResultSet rs = null;
            rs = sdml.felt.commonUI.data.getResult("SELECT * FROM DOC_MGMT.TRAVEL_VOUCHER where DOC_ID=" + PDocId);

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

    public void openDoc() {
        AppletFrame aFrame = new AppletFrame("Travelling Voucher");
        aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelVoucher", "Travelling Voucher");
        FrmTravelVoucher Obj = (FrmTravelVoucher) aFrame.ObjApplet;

        Obj.requestFocus();
        Obj.Find(Table.getValueAt(Table.getSelectedRow(), 10).toString());

    }

    private void GenerateCombo() {

        HashMap List = new HashMap();
        clsIncharge ObjIncharge;

        INCHARGE.setModel(cmbIncharge);
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
}
