/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceRegister;

import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableModel;
import EITLERP.JTextFieldHint;
import EITLERP.LOV;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Dharmendra
 */
public class FrmPOPendingOrder extends javax.swing.JApplet {

    /**
     * Initializes the applet FrmPOPendingOrder
     */
    private EITLTableModel DataModel = new EITLTableModel();
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    @Override
    public void init() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);


        /* Create and display the applet */
        initComponents();

        jLabel1.setForeground(Color.WHITE);
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        file1 = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtpartycode = new javax.swing.JTextField();
        txtpartyname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtgroup = new javax.swing.JTextField();
        BtnView = new javax.swing.JButton();
        btnEmpMstETE = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Party Order Status");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Party Code : ");

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

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Group :");

        txtgroup.setToolTipText("Press F1 key for search Party Code");
        txtgroup = new JTextFieldHint(new JTextField(),"Search by F1");
        txtgroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtgroupKeyPressed(evt);
            }
        });

        BtnView.setText("View");
        BtnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnViewActionPerformed(evt);
            }
        });

        btnEmpMstETE.setLabel("Export to Excel");
        btnEmpMstETE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpMstETEActionPerformed(evt);
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
        jScrollPane1.setViewportView(Table);

        btnClear.setText("Clear All");
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtpartycode, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtgroup))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtpartyname, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEmpMstETE, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 508, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtpartycode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtpartyname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtgroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnView, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEmpMstETE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

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
            aList.SQL = "SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION,CASE WHEN COALESCE(PARTY_CLOSE_IND,0) =0  AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 0 THEN 'ACTIVE' WHEN COALESCE(PARTY_CLOSE_IND,0) =0  AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =1 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 0 THEN 'ACTIVE' WHEN COALESCE(PARTY_CLOSE_IND,0) =0  AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 1 THEN 'ACTIVE' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =1 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 0 THEN 'IN-ACTIVE' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 1 THEN 'PERMANENTLY CLOSED' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 2 THEN 'TEMPORARY CLOSED' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =0 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 0 THEN 'PERMANENTLY CLOSED' WHEN COALESCE(PARTY_CLOSE_IND,0) =1 AND COALESCE(PARTY_CLOSE_INACTIVE_IND,0) =1 AND COALESCE(PARTY_MILL_CLOSED_IND,0) = 1 THEN 'PERMANENTLY CLOSED' END AS PARTY_STATUS FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND APPROVED=1 AND CANCELLED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtpartycode.setText(aList.ReturnVal);
                txtpartyname.setText(clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        }
    }//GEN-LAST:event_txtpartycodeKeyPressed

    private void txtpartynameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpartynameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpartynameActionPerformed

    private void txtgroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtgroupKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();
            aList.SQL = "SELECT DISTINCT GROUP_DESC AS GRUP,GROUP_DESC AS GRUP FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D1 ,PRODUCTION.FELT_GROUP_MASTER_HEADER H "
                    + " WHERE H.GROUP_CODE = D1.GROUP_CODE";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtgroup.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtgroupKeyPressed

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

    private void BtnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnViewActionPerformed
        // TODO add your handling code here:
        GenerateData();
    }//GEN-LAST:event_BtnViewActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtpartycode.setText("");
        txtgroup.setText("");
        txtpartyname.setText("");
        FormatGrid();
    }//GEN-LAST:event_btnClearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnView;
    private javax.swing.JTable Table;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEmpMstETE;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtgroup;
    private javax.swing.JTextField txtpartycode;
    private javax.swing.JTextField txtpartyname;
    // End of variables declaration//GEN-END:variables
private void FormatGrid() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Unit");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Machine No.");
        DataModel.addColumn("Position No.");
        DataModel.addColumn("Position Desc");
        DataModel.addColumn("PO No.");
        DataModel.addColumn("PO Date");
        DataModel.addColumn("Group");
        DataModel.addColumn("Length");
        DataModel.addColumn("Width");
        DataModel.addColumn("SQMTR");
        DataModel.addColumn("GSM");
        DataModel.addColumn("Pending as on 01/04/" + EITLERPGLOBAL.FinYearFrom);
        DataModel.addColumn("Invoiced");
        DataModel.addColumn("Still Pending as on " + EITLERPGLOBAL.getCurrentDate());
        DataModel.addColumn("All Pieces");
        DataModel.addColumn("WIP Pieces");
        DataModel.addColumn("Stock Pieces");
        DataModel.addColumn("Invoiced Pieces");
        DataModel.addColumn("Run Date");

        DataModel.TableReadOnly(true);
    }

    public void setData(String PartyCode)
    {
        txtpartycode.setText(PartyCode);
        txtpartycodeFocusLost(null);
        BtnViewActionPerformed(null);
    }
    private void GenerateData() {
        String cndtn = "";
        String grp_cndtn = "";
        try {
            FormatGrid(); //clear existing content of table
            ResultSet rsTmp;
            if (txtpartycode.getText().trim().length() > 0) {
                cndtn = " AND PR_PARTY_CODE IN (" + txtpartycode.getText() + ")";
            }
            if (txtgroup.getText().trim().length() > 0) {
                grp_cndtn = " AND GROUP_DESC IN ('" + txtgroup.getText() + "')";
            }

            String strSQL = "";

            strSQL = "SELECT DATE_FORMAT(NOW(),'%d/%m/%Y %r') AS  RUNDATE,COALESCE(GROUP_DESC,'') AS GROUP_DESC,PR_PARTY_CODE,PARTY_NAME,"
                    + "PR_MACHINE_NO,PR_POSITION_NO,PR_PO_NO,"
                    + "CASE WHEN COALESCE(PR_PO_DATE,'0000-00-00')='0000-00-00' THEN '' ELSE DATE_FORMAT(PR_PO_DATE,'%d/%m/%Y') END  AS PO_DATE,"
                    + "POSITION_DESC,PR_GROUP,PR_LENGTH,PR_WIDTH,PR_SQMTR,PR_GSM,COUNT(PR_PIECE_NO) AS PENDING,"
                    + "SUM(CASE WHEN COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00' THEN 0 ELSE 1 END) AS INVOICED,"
                    + "GROUP_CONCAT(DISTINCT CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE)) AS PIECE,"
                    + "COALESCE(GROUP_CONCAT(DISTINCT CASE WHEN PR_PIECE_STAGE IN ('PLANNING','BOOKING','SPIRALLING') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_ORDER_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + " WHEN PR_PIECE_STAGE IN ('WEAVING') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_WARP_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + " WHEN PR_PIECE_STAGE IN ('SEAMING','NEEDLING') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_MND_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + " WHEN PR_PIECE_STAGE IN ('MENDING') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_WVG_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + " WHEN PR_PIECE_STAGE IN ('ASSEMBLY') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_SDF_SPIRALED_DATE ),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + " WHEN PR_PIECE_STAGE IN ('FINISHING') AND PR_GROUP IN ('HDS') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_SEAM_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + " WHEN PR_PIECE_STAGE IN ('FINISHING') AND PR_GROUP IN ('SDF') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_SDF_ASSEMBLED_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + " WHEN PR_PIECE_STAGE IN ('FINISHING') AND PR_GROUP NOT IN ('HDS','SDF') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_NDL_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + " END),'') AS WIPPIECE,"
                    + "COALESCE(GROUP_CONCAT(DISTINCT CASE WHEN PR_PIECE_STAGE IN ('BSR') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_PACKED_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + "WHEN PR_PIECE_STAGE IN ('IN STOCK') THEN CONCAT(PR_PIECE_NO,'-',PR_PIECE_STAGE,' -(',DATEDIFF(CURDATE(),PR_FNSG_DATE),' Days) (OC - ',PR_OC_MONTHYEAR,')') "
                    + "END),'') AS STKPIECE,"
                    + "COALESCE(GROUP_CONCAT(DISTINCT CASE WHEN PR_PIECE_STAGE IN ('INVOICED','EXP-INVOICE') THEN CONCAT(PR_PIECE_NO,'-(',DATE_FORMAT(PR_INVOICE_DATE,'%d/%m/%Y'),')') END),'') AS INVPIECE "
                    + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER R "
                    + " LEFT JOIN (SELECT PARTY_CODE,CONCAT(PARTY_NAME,' (',CITY_ID,')') AS PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS PM ON R.PR_PARTY_CODE=PARTY_CODE "
                    + " LEFT JOIN (SELECT DISTINCT PARTY_CODE,GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D1 ,PRODUCTION.FELT_GROUP_MASTER_HEADER H "
                    + " WHERE H.GROUP_CODE = D1.GROUP_CODE) AS G ON R.PR_PARTY_CODE=G.PARTY_CODE "
                    + " LEFT JOIN PRODUCTION.FELT_MACHINE_POSITION_MST ON PR_POSITION_NO+0=POSITION_NO+0 "
                    + " WHERE PR_PIECE_STAGE NOT IN ('CANCELED','JOINED','RETURN','SCRAP','DIVIDED','DIVERTED','CANCELLED','OSG STOCK') "
                    + " AND COALESCE(PR_DELINK,'')!='OBSOLETE' "
                    + " AND (COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00' OR PR_INVOICE_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "') "
                    + cndtn + " "
                    + grp_cndtn + " "
                    + " GROUP BY GROUP_DESC,PR_PARTY_CODE,PR_PO_NO,PR_UPN "
                    + " ORDER BY GROUP_DESC,PR_PARTY_CODE,PR_PO_NO,PR_UPN";

            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
//            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int pos = 0;
                while (!rsTmp.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    pos = 0;
                    rowData[pos] = rsTmp.getString("GROUP_DESC");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_PARTY_CODE");
                    pos++;
                    rowData[pos] = rsTmp.getString("PARTY_NAME");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_MACHINE_NO");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_POSITION_NO");
                    pos++;
                    rowData[pos] = rsTmp.getString("POSITION_DESC");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_PO_NO");
                    pos++;
                    rowData[pos] = rsTmp.getString("PO_DATE");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_GROUP");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_LENGTH");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_WIDTH");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_SQMTR");
                    pos++;
                    rowData[pos] = rsTmp.getString("PR_GSM");
                    pos++;
                    rowData[pos] = rsTmp.getString("PENDING");
                    pos++;
                    rowData[pos] = rsTmp.getString("INVOICED");
                    pos++;
                    rowData[pos] = rsTmp.getDouble("PENDING") - rsTmp.getDouble("INVOICED");
                    pos++;
                    rowData[pos] = rsTmp.getString("PIECE");
                    pos++;
                    rowData[pos] = rsTmp.getString("WIPPIECE");
                    pos++;
                    rowData[pos] = rsTmp.getString("STKPIECE");
                    pos++;
                    rowData[pos] = rsTmp.getString("INVPIECE");
                    pos++;
                    rowData[pos] = rsTmp.getString("RUNDATE");

                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
                final TableColumnModel columnModel = Table.getColumnModel();
                for (int column = 0; column < Table.getColumnCount(); column++) {
                    int width = 60; // Min width
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
                JOptionPane.showMessageDialog(null, "Zero (0) Record Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
