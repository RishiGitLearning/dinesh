/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Reports;

import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableModel;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsVoucher;
import EITLERP.ReportRegister;
import EITLERP.data;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Dharmendra
 */
public class frmInvTCSAnalysis extends javax.swing.JApplet {

    /**
     * Initializes the frmF6InvAnalysis
     */
    private int EditMode = 0;
    private boolean DoNotEvaluate = false;
    private EITLTableModel DataModel = new EITLTableModel();
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    @Override
    public void init() {
        /* Create and display the applet */
        initComponents();
        file1.show(false);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        FormatGrid();
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        cmdview = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdWhStkExporttoExcel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        file1 = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        cmdview.setText("View");
        cmdview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdviewActionPerformed(evt);
            }
        });
        getContentPane().add(cmdview);
        cmdview.setBounds(520, 40, 120, 30);

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

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 80, 970, 410);

        cmdWhStkExporttoExcel.setText("Export to Excel");
        cmdWhStkExporttoExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdWhStkExporttoExcelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdWhStkExporttoExcel);
        cmdWhStkExporttoExcel.setBounds(810, 40, 150, 30);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 29));
        jPanel1.setLayout(null);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Felt Invoice TCS Payment Received Report");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(2, 4, 460, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 1, 980, 30);
        getContentPane().add(file1);
        file1.setBounds(750, 50, 220, 420);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("From Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 40, 100, 30);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(120, 40, 120, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("To Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(240, 40, 90, 30);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(340, 40, 120, 30);

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(714, 40, 80, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdviewActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        } else {
            GenerateReport();
        }
    }//GEN-LAST:event_cmdviewActionPerformed

    private void cmdWhStkExporttoExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdWhStkExporttoExcelActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(Table, new File(file1.getSelectedFile().toString() + ".xls"), "Analysis Report of TCS Invoice");

            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cmdWhStkExporttoExcelActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        } else {
            PreviewReport();
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdWhStkExporttoExcel;
    private javax.swing.JButton cmdview;
    private javax.swing.JFileChooser file1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables

    private void FormatGrid() {
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);

        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        DataModel.addColumn("Sr.No");
        DataModel.addColumn("Invoice No");
        DataModel.addColumn("Invoice Date");
        DataModel.addColumn("Party Code");
        DataModel.addColumn("Party Name");
        DataModel.addColumn("Charge Code");
        DataModel.addColumn("Invoice Amt");
        DataModel.addColumn("TCS Amt");
        DataModel.addColumn("TCS Received Amt");
        DataModel.addColumn("Diff TCS Amt");
        DataModel.addColumn("Voucher");
        DataModel.addColumn("Payment receive Voucher Date");
        DataModel.addColumn("Received Amt");
        DataModel.addColumn("Difference Amt");
        DataModel.addColumn("DebitNote No");
        DataModel.addColumn("DebitNote Date");
        DataModel.addColumn("Receipt Voucher No");
                
        DataModel.TableReadOnly(true);
        
        for (int i=0; i<=16; i++) {
            Table.getColumnModel().getColumn(i).setMinWidth(90);            
        }
        Table.getColumnModel().getColumn(0).setMinWidth(50);
        Table.getColumnModel().getColumn(0).setMaxWidth(50);
        Table.getColumnModel().getColumn(5).setMinWidth(50);
        Table.getColumnModel().getColumn(5).setMaxWidth(50);
    }

    private void GenerateReport() {
        String FromDate = EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText());
        try {
            FormatGrid(); //clear existing content of table
            ResultSet rsTmp;
            String strSQL = "";

//            strSQL = "SELECT * FROM ( "
//                    + "SELECT A.PARTY_CODE,A.PARTY_NAME,PARTY_CHARGE_CODE,CHARGE_CODE,A.INVOICE_NO,A.INVOICE_DATE,A.INVOICE_AMT,"
//                    + "C.VALUE_DATE AS V_DATE ,COALESCE(C.AMOUNT,0) AS AMOUNT, (INVOICE_AMT - COALESCE(C.AMOUNT,0)) AS SHORT_AMT,"
//                    + "TCS_AMT, CASE WHEN COALESCE(C.AMOUNT,0)>TCS_AMT THEN TCS_AMT ELSE COALESCE(C.AMOUNT,0) END AS TCS_RECV_AMT,"
//                    + "CASE WHEN COALESCE(C.AMOUNT,0)>TCS_AMT THEN '0.00' ELSE (TCS_AMT - COALESCE(C.AMOUNT,0)) END  AS TCS_DIFF_AMT,"
//                    + "VOUCHER,PIECE_NO "
//                    + " FROM "
//                    + "(SELECT CONCAT(INVOICE_NO,INVOICE_DATE,PARTY_CODE) AS UID,PARTY_CODE,PARTY_NAME,PARTY_CHARGE_CODE,"
//                    + "CHARGE_CODE,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,TCS_AMT,PIECE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER "
//                    + "WHERE INVOICE_DATE>= '" + FromDate + "' AND INVOICE_DATE <='" + ToDate + "' "
//                    + "AND APPROVED=1 AND CANCELLED=0 AND TCS_AMT>=0 "
//                    + ")  AS A "
//                    + "LEFT JOIN "
//                    + "(SELECT CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE) AS UID,SUM(AMOUNT)AS AMOUNT,MAX(VALUE_DATE) AS VALUE_DATE,INVOICE_NO,INVOICE_DATE,EFFECT, "
//                    + "GROUP_CONCAT(DISTINCT B.VOUCHER_NO,' ( ',AMOUNT,' / ',VALUE_DATE,' ) '  ORDER BY A.VOUCHER_NO SEPARATOR ' , ' ) AS VOUCHER "
//                    + "FROM FINANCE.D_FIN_VOUCHER_DETAIL A,FINANCE.D_FIN_VOUCHER_HEADER B "
//                    + "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.APPROVED =1 AND B.CANCELLED =0 "
//                    + "AND CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE) IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE,PARTY_CODE) FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + FromDate + "' AND INVOICE_DATE <='" + ToDate + "'  AND CANCELLED=0 ) "
//                    + "AND MAIN_ACCOUNT_CODE = 210010  AND SUBSTRING(B.VOUCHER_NO,1,2) !='SJ' GROUP BY CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE),INVOICE_NO,INVOICE_DATE "
//                    + ") AS C ON A.UID=C.UID "
//                    + ") AS PAY ORDER BY INVOICE_NO,INVOICE_DATE ";
                    
            strSQL = "SELECT * FROM ( "
                    + "SELECT A.PARTY_CODE,A.PARTY_NAME,PARTY_CHARGE_CODE,CHARGE_CODE,A.INVOICE_NO,A.INVOICE_DATE,A.INVOICE_AMT,"
                    + "C.VALUE_DATE AS V_DATE ,COALESCE(C.AMOUNT,0) AS AMOUNT, (INVOICE_AMT - COALESCE(C.AMOUNT,0)) AS SHORT_AMT,"
                    + "TCS_AMT, CASE WHEN COALESCE(C.AMOUNT,0)>TCS_AMT THEN TCS_AMT ELSE COALESCE(C.AMOUNT,0) END AS TCS_RECV_AMT,"
                    + "CASE WHEN COALESCE(C.AMOUNT,0)>TCS_AMT THEN '0.00' ELSE (TCS_AMT - COALESCE(C.AMOUNT,0)) END  AS TCS_DIFF_AMT,"
                    + "VOUCHER,PIECE_NO, "
                    + "DEBITNOTE_VOUCHER_NO,DEBITNOTE_VOUCHER_DATE,RECEIPT_VOUCHER_NO "
                    + " FROM "
                    + "(SELECT CONCAT(INVOICE_NO,INVOICE_DATE,PARTY_CODE) AS UID,PARTY_CODE,PARTY_NAME,PARTY_CHARGE_CODE,"
                    + "CHARGE_CODE,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,TCS_AMT,PIECE_NO, "
                    + "'' AS DEBITNOTE_VOUCHER_NO,'' AS DEBITNOTE_VOUCHER_DATE,'' AS RECEIPT_VOUCHER_NO "
                    + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER "
                    + "WHERE INVOICE_DATE>= '" + FromDate + "' AND INVOICE_DATE <='" + ToDate + "' "
                    + "AND APPROVED=1 AND CANCELLED=0 AND TCS_AMT>0 "
                    + "UNION ALL "
                    + "SELECT CONCAT(M.INVOICE_NO,M.INVOICE_DATE,M.DB_PARTY_CODE) AS UID,M.DB_PARTY_CODE,H.PARTY_NAME,H.PARTY_CHARGE_CODE,"
                    + "H.CHARGE_CODE,M.INVOICE_NO,M.INVOICE_DATE,M.INVOICE_AMOUNT,M.TCS_AMT,H.PIECE_NO,"
                    + "M.DEBITNOTE_VOUCHER_NO,M.DEBITNOTE_VOUCHER_DATE,M.RECEIPT_VOUCHER_NO "
                    + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, FINANCE.D_FIN_DEBITNOTE_RECEIPT_MAPPING M "
                    + "WHERE M.INVOICE_NO=H.INVOICE_NO AND M.INVOICE_DATE=H.INVOICE_DATE "
                    + "AND M.DEBITNOTE_VOUCHER_DATE>= '" + FromDate + "' AND M.DEBITNOTE_VOUCHER_DATE <='" + ToDate + "' "
                    + "AND H.APPROVED=1 AND H.CANCELLED=0 AND M.TCS_AMT>0 "
                    + ")  AS A "
                    + "LEFT JOIN "
                    + "(SELECT CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE) AS UID,SUM(AMOUNT)AS AMOUNT,MAX(VALUE_DATE) AS VALUE_DATE,INVOICE_NO,INVOICE_DATE,EFFECT, "
                    + "GROUP_CONCAT(DISTINCT B.VOUCHER_NO,' ( ',AMOUNT,' / ',VALUE_DATE,' ) '  ORDER BY A.VOUCHER_NO SEPARATOR ' , ' ) AS VOUCHER "
                    + "FROM FINANCE.D_FIN_VOUCHER_DETAIL A,FINANCE.D_FIN_VOUCHER_HEADER B "
                    + "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.APPROVED =1 AND B.CANCELLED =0 "
                    + "AND CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE) IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE,PARTY_CODE) FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + FromDate + "' AND INVOICE_DATE <='" + ToDate + "'  AND CANCELLED=0 ) "
                    + "AND MAIN_ACCOUNT_CODE = 210010  AND SUBSTRING(B.VOUCHER_NO,1,2) !='SJ' GROUP BY CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE),INVOICE_NO,INVOICE_DATE "
                    + ") AS C ON A.UID=C.UID "
                    + ") AS PAY ORDER BY INVOICE_NO,INVOICE_DATE ";
            
            System.out.println("Query..." + strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();
            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                int cnt = 0;
                double totalAmt = 0.00;

                while (!rsTmp.isAfterLast()) {
                    cnt++;
//                    System.out.println(cnt);

                    Object[] rowData = new Object[50];
                    rowData[0] = Integer.toString(cnt);
                    rowData[1] = rsTmp.getString("INVOICE_NO");
                    rowData[2] = EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE"));
                    rowData[3] = rsTmp.getString("PARTY_CODE");
                    rowData[4] = rsTmp.getString("PARTY_NAME");
                    rowData[5] = rsTmp.getString("CHARGE_CODE");
                    rowData[6] = rsTmp.getString("INVOICE_AMT");
                    rowData[7] = rsTmp.getString("TCS_AMT");
                    rowData[8] = rsTmp.getString("TCS_RECV_AMT");
                    rowData[9] = rsTmp.getString("TCS_DIFF_AMT");
                    rowData[10] = rsTmp.getString("VOUCHER");
                    rowData[11] = EITLERPGLOBAL.formatDate(rsTmp.getString("V_DATE"));
                    rowData[12] = rsTmp.getString("AMOUNT");
                    rowData[13] = rsTmp.getString("SHORT_AMT");
                    rowData[14] = rsTmp.getString("DEBITNOTE_VOUCHER_NO");
                    rowData[15] = EITLERPGLOBAL.formatDate(rsTmp.getString("DEBITNOTE_VOUCHER_DATE"));
                    rowData[16] = rsTmp.getString("RECEIPT_VOUCHER_NO");
                    
                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private boolean Validate() {
        //Form level validations
        if (txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter from date");
            return false;
        } else if (!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid From Date in DD/MM/YYYY format.");
            return false;
        }

        if (txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter To date");
            return false;
        } else if (!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid To Date in DD/MM/YYYY format.");
            return false;
        }

        return true;
    }

    private void PreviewReport() {
        
        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            Conn = data.getConn();
            st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            String fdt = txtFromDate.getText();
            String tdt = txtToDate.getText();
            String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
            String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());

            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("FROM_DATE", fdt);
            parameterMap.put("TO_DATE", tdt);
            
            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            String strSQL = "SELECT * FROM ( "
                    + "SELECT A.PARTY_CODE,A.PARTY_NAME,PARTY_CHARGE_CODE,CHARGE_CODE,A.INVOICE_NO,A.INVOICE_DATE,A.INVOICE_AMT,"
                    + "C.VALUE_DATE AS V_DATE ,COALESCE(C.AMOUNT,0) AS AMOUNT, (INVOICE_AMT - COALESCE(C.AMOUNT,0)) AS SHORT_AMT,"
                    + "TCS_AMT, CASE WHEN COALESCE(C.AMOUNT,0)>TCS_AMT THEN TCS_AMT ELSE COALESCE(C.AMOUNT,0) END AS TCS_RECV_AMT,"
                    + "CASE WHEN COALESCE(C.AMOUNT,0)>TCS_AMT THEN '0.00' ELSE (TCS_AMT - COALESCE(C.AMOUNT,0)) END  AS TCS_DIFF_AMT,"
                    + "VOUCHER,PIECE_NO, "
                    + "DEBITNOTE_VOUCHER_NO,DATE_FORMAT(DEBITNOTE_VOUCHER_DATE,'%d/%m/%Y') AS DEBITNOTE_VOUCHER_DATE,RECEIPT_VOUCHER_NO "
                    + " FROM "
                    + "(SELECT CONCAT(INVOICE_NO,INVOICE_DATE,PARTY_CODE) AS UID,PARTY_CODE,PARTY_NAME,PARTY_CHARGE_CODE,"
                    + "CHARGE_CODE,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,TCS_AMT,PIECE_NO, "
                    + "'' AS DEBITNOTE_VOUCHER_NO,'' AS DEBITNOTE_VOUCHER_DATE,'' AS RECEIPT_VOUCHER_NO "
                    + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER "
                    + "WHERE INVOICE_DATE>= '" + FromDate + "' AND INVOICE_DATE <='" + ToDate + "' "
                    + "AND APPROVED=1 AND CANCELLED=0 AND TCS_AMT>0 "
                    + "UNION ALL "
                    + "SELECT CONCAT(M.INVOICE_NO,M.INVOICE_DATE,M.DB_PARTY_CODE) AS UID,M.DB_PARTY_CODE,H.PARTY_NAME,H.PARTY_CHARGE_CODE,"
                    + "H.CHARGE_CODE,M.INVOICE_NO,M.INVOICE_DATE,M.INVOICE_AMOUNT,M.TCS_AMT,H.PIECE_NO,"
                    + "M.DEBITNOTE_VOUCHER_NO,M.DEBITNOTE_VOUCHER_DATE,M.RECEIPT_VOUCHER_NO "
                    + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, FINANCE.D_FIN_DEBITNOTE_RECEIPT_MAPPING M "
                    + "WHERE M.INVOICE_NO=H.INVOICE_NO AND M.INVOICE_DATE=H.INVOICE_DATE "
                    + "AND M.DEBITNOTE_VOUCHER_DATE>= '" + FromDate + "' AND M.DEBITNOTE_VOUCHER_DATE <='" + ToDate + "' "
                    + "AND H.APPROVED=1 AND H.CANCELLED=0 AND M.TCS_AMT>0 "
                    + ")  AS A "
                    + "LEFT JOIN "
                    + "(SELECT CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE) AS UID,SUM(AMOUNT)AS AMOUNT,MAX(VALUE_DATE) AS VALUE_DATE,INVOICE_NO,INVOICE_DATE,EFFECT, "
                    + "GROUP_CONCAT(DISTINCT B.VOUCHER_NO,' ( ',AMOUNT,' / ',VALUE_DATE,' ) '  ORDER BY A.VOUCHER_NO SEPARATOR ' , ' ) AS VOUCHER "
                    + "FROM FINANCE.D_FIN_VOUCHER_DETAIL A,FINANCE.D_FIN_VOUCHER_HEADER B "
                    + "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.APPROVED =1 AND B.CANCELLED =0 "
                    + "AND CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE) IN (SELECT CONCAT(INVOICE_NO,INVOICE_DATE,PARTY_CODE) FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_DATE>= '" + FromDate + "' AND INVOICE_DATE <='" + ToDate + "'  AND CANCELLED=0 ) "
                    + "AND MAIN_ACCOUNT_CODE = 210010  AND SUBSTRING(B.VOUCHER_NO,1,2) !='SJ' GROUP BY CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE),INVOICE_NO,INVOICE_DATE "
                    + ") AS C ON A.UID=C.UID "
                    + ") AS PAY ORDER BY INVOICE_NO,INVOICE_DATE ";

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/FeltSales/Reports/TCSPaymentReceived.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                Conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
