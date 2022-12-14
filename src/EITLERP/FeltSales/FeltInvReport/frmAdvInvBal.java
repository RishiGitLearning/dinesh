/*
 * frmRptInvoiceList.java
 *
 * Created on June 15, 2010, 5:26 PM
 */
package EITLERP.FeltSales.FeltInvReport;

//import EITLERP.Sales.ReportsUI.*;
import EITLERP.*;
import javax.swing.*;
import EITLERP.Utils.*;
import TReportWriter.SimpleDataProvider.TRow;
import TReportWriter.SimpleDataProvider.TTable;
import TReportWriter.TReportEngine;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author user
 */

public class frmAdvInvBal extends javax.swing.JApplet {

    private EITLComboModel cmbInvoiceTypeModel;
    
    private TReportEngine objEngine = new TReportEngine();

    /**
     * Initializes the applet frmRptInvoiceList
     */
    public void init() {
        setSize(400, 300);
        initComponents();
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        cmdOk = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("From Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 52, 100, 16);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("To Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 80, 100, 16);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(115, 50, 100, 28);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(115, 80, 100, 28);

        cmdOk.setText("09 Report");
        cmdOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOkActionPerformed(evt);
            }
        });
        getContentPane().add(cmdOk);
        cmdOk.setBounds(110, 130, 110, 28);

        cmdCancel.setText("CLEAR");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(230, 130, 100, 28);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 29));
        jPanel1.setLayout(null);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Advance Balance Report ");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(2, 4, 380, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 2, 390, 29);

        jButton1.setText("All Charge Code");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(100, 170, 160, 28);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOkActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        } else {
            PreviewReport();
        }
    }//GEN-LAST:event_cmdOkActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        txtFromDate.setText("");
        txtToDate.setText("");
        txtFromDate.requestFocus();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        } else {
            PreviewReport_AllChargeCode_New();
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOk;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables

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
        String sql;
        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());

        HashMap Parameters = new HashMap();
        
        String curDt = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(CURDATE(),'%d/%m/%Y'),SUBSTRING(NOW(),11,20)) AS DATE FROM DUAL");
        Parameters.put("CURDATE", curDt);
        Parameters.put("FROM_DATE", txtFromDate.getText());
        Parameters.put("TO_DATE", txtToDate.getText());

        TTable objData = new TTable();
        objData.AddColumn("PARTY_CODE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("OPENING_BAL");
        objData.AddColumn("INVOICE_AMT");
        objData.AddColumn("CLOSING_BAL");
        objData.AddColumn("VOUCHER");
        
        try {

            String strSQL = "";
            ResultSet rsReport;

            //Retrieve data
            strSQL = "SELECT A1.PARTY_CODE,A1.PARTY_NAME,A1.INVOICE_NO,A1.INVOICE_DATE,OPENING_BAL, ";
            strSQL += "A1.INVOICE_AMT,CLOSING_BAL,COALESCE(VOUCHER,'') AS VOUCHER,A1.CHARGE_CODE,A1.PRODUCT_CODE,A1.BALE_NO,A1.PACKING_DATE FROM ";
            strSQL += "(SELECT PARTY_CODE,SUBSTRING(PARTY_NAME,1,22) AS PARTY_NAME,INVOICE_NO,INVOICE_DATE, ";
            strSQL += "COALESCE(CRITICAL_LIMIT_AMT,0) AS OPENING_BAL,INVOICE_AMT,COALESCE(INV_CRITICAL_LIMIT_AMT,0) AS CLOSING_BAL,CHARGE_CODE,PRODUCT_CODE,BALE_NO,PACKING_DATE";
            strSQL += " FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ";
            strSQL += "WHERE  CHARGE_CODE='09' AND ";
            strSQL += "INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' ";
            strSQL += "AND APPROVED=1 AND CANCELLED=0) AS A1 ";
            strSQL += "LEFT JOIN ";
            strSQL += "(SELECT INVOICE_NO,INVOICE_DATE,INVOICE_AMOUNT, ";
            strSQL += "GROUP_CONCAT(DISTINCT D.VOUCHER_NO ORDER BY D.VOUCHER_NO SEPARATOR ', ') AS VOUCHER  ";
            strSQL += "FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX C,FINANCE.D_FIN_VOUCHER_HEADER D ";
            strSQL += "WHERE INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' ";
            strSQL += "AND C.VOUCHER_NO=D.VOUCHER_NO AND D.APPROVED=1 AND D.CANCELLED=0 ";
            strSQL += "GROUP BY INVOICE_NO,INVOICE_DATE) AS B1 ";
            strSQL += "ON A1.INVOICE_NO = B1.INVOICE_NO  ";
            strSQL += "AND B1.INVOICE_DATE = A1.INVOICE_DATE ";
            strSQL += "ORDER BY A1.INVOICE_DATE,A1.PARTY_CODE,A1.INVOICE_NO ";
            
            System.out.println("SQL : "+strSQL);
            rsReport = data.getResult(strSQL);
            rsReport.first();

            if (rsReport.getRow() > 0) {

                while (!rsReport.isAfterLast()) {
                    TRow objRow = new TRow();

                    objRow.setValue("PARTY_CODE", rsReport.getString("PARTY_CODE"));
                    objRow.setValue("PARTY_NAME", rsReport.getString("PARTY_NAME"));
                    objRow.setValue("INVOICE_NO", rsReport.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE", rsReport.getString("INVOICE_DATE"));
                    objRow.setValue("OPENING_BAL", Double.toString(rsReport.getDouble("OPENING_BAL")));
                    objRow.setValue("INVOICE_AMT", Double.toString(rsReport.getDouble("INVOICE_AMT")));
                    objRow.setValue("CLOSING_BAL", Double.toString(rsReport.getDouble("CLOSING_BAL")));
                    objRow.setValue("VOUCHER", rsReport.getString("VOUCHER"));
                    
                    objData.AddRow(objRow);
                    rsReport.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/FeltSales/rpt09InvBal.rpt", Parameters, objData);

//        String InvoiceType=2+"";
//        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
//        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
//        
//        Connection Conn = null;
//        Statement st = null;
//        ResultSet rs = null;
//        try {
//            Conn = data.getConn();
//            st = Conn.createStatement();
//
//            HashMap parameterMap = new HashMap();
//
//            parameterMap.put("INVOICE_TYPE", InvoiceType);
//            parameterMap.put("INVOICE_DATE_FROM", FromDate);
//            parameterMap.put("INVOICE_DATE_TO", ToDate);
//            
//            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
//            
//            String strSQL = "SELECT  A1.INVOICE_NO,A1.INVOICE_DATE,A1.PARTY_CODE,A1.PARTY_NAME,A1.INVOICE_AMT, ";
//            strSQL += "A1.CHARGE_CODE,A1.DUE_DAYS,VOUCHER,B1.INVOICE_AMOUNT FROM  ";
//            strSQL += "(SELECT  A.INVOICE_NO,SUBSTRING(A.INVOICE_DATE,1,10) AS INVOICE_DATE,A.PARTY_CODE,B.PARTY_NAME,A.INVOICE_AMT,A.CHARGE_CODE ";
//            strSQL += ", COALESCE(DATEDIFF(A.DUE_DATE,SUBSTRING(A.INVOICE_DATE,1,10)),0) AS DUE_DAYS ";
//            strSQL += "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_PARTY_MASTER B WHERE SUBSTRING(A.INVOICE_DATE,1,10)>='"+FromDate+"'  ";
//            strSQL += "AND SUBSTRING(A.INVOICE_DATE,1,10)<='"+ToDate+"' AND A.INVOICE_TYPE=2  ";
//            strSQL += "AND A.PARTY_CODE= B.PARTY_CODE AND A.APPROVED=1 AND A.CANCELLED=0) AS A1 ";
//            strSQL += "LEFT JOIN ";
//            strSQL += "(SELECT INVOICE_NO,INVOICE_DATE,INVOICE_AMOUNT, ";
//            strSQL += "GROUP_CONCAT(DISTINCT D.VOUCHER_NO ORDER BY D.VOUCHER_NO SEPARATOR ', ') AS VOUCHER  ";
//            strSQL += "FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX C,FINANCE.D_FIN_VOUCHER_HEADER D ";
//            strSQL += " WHERE INVOICE_DATE>='"+FromDate+"'  ";
//            strSQL += "AND INVOICE_DATE<='"+ToDate+"'   ";
//            strSQL += "AND C.VOUCHER_NO= D.VOUCHER_NO AND D.APPROVED =1 AND D.CANCELLED =0 ";
//            strSQL += "GROUP BY INVOICE_NO,INVOICE_DATE) AS B1 ";
//            strSQL += "ON A1.INVOICE_NO = B1.INVOICE_NO  ";
//            strSQL += "AND B1.INVOICE_DATE = A1.INVOICE_DATE   ";
//            strSQL += "ORDER BY A1.INVOICE_NO";
//            
//            rpt.setReportName("/EITLERP/FeltSales/FeltInvReport/rptFeltSalesInvoiceDetail.jrxml", 1, strSQL); //productlist is the name of my jasper file.
//            rpt.callReport();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                st.close();
//                Conn.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
    private void PreviewReport_AllChargeCode_New()
    {
                String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
                String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());

                Connection Conn = null;
                Statement st = null;
                try {
                    Conn = data.getConn();
                    st = Conn.createStatement();

                    HashMap parameterMap = new HashMap();

                    parameterMap.put("FROM_INVOICE_DATE", FromDate);
                    parameterMap.put("TO_INVOICE_DATE", ToDate);

                    ReportRegister rpt = new ReportRegister(parameterMap, Conn);
                    
                    String strSQL = " SELECT A1.PARTY_CODE,A1.PARTY_NAME,A1.INVOICE_NO,A1.INVOICE_DATE,F6, " +
                                    " (CASE WHEN A1.CHARGE_CODE IN ('09') AND A1.PARTY_CHARGE_CODE=A1.CHARGE_CODE THEN OPENING_BAL1 " +
                                    " WHEN A1.CHARGE_CODE IN ('08') AND A1.PARTY_CHARGE_CODE=A1.CHARGE_CODE THEN '' " +
                                    " WHEN A1.CHARGE_CODE NOT IN ('09','01','08') THEN OPENING_BAL2 " +
                                    " WHEN A1.CHARGE_CODE IN ('01') AND A1.PARTY_CHARGE_CODE=A1.CHARGE_CODE THEN '' " +
                                    " WHEN A1.CHARGE_CODE IN ('01') AND A1.PARTY_CHARGE_CODE!=A1.CHARGE_CODE THEN '' END) " +
                                    " AS OPENING_BAL,A1.INVOICE_AMT, " +
                                    " (CASE WHEN A1.CHARGE_CODE IN ('09') AND A1.PARTY_CHARGE_CODE=A1.CHARGE_CODE  THEN CLOSING_BAL1 " +
                                    " WHEN A1.CHARGE_CODE IN ('08') AND A1.PARTY_CHARGE_CODE=A1.CHARGE_CODE THEN '' " +
                                    " WHEN A1.CHARGE_CODE NOT IN ('09','01','08') THEN CLOSING_BAL2 " +
                                    " WHEN A1.CHARGE_CODE IN ('01') AND A1.PARTY_CHARGE_CODE=A1.CHARGE_CODE THEN '' " +
                                    " WHEN A1.CHARGE_CODE IN ('01') AND A1.PARTY_CHARGE_CODE!=A1.CHARGE_CODE THEN '' END) " +
                                    " AS CLOSING_BAL ," +
                                    " VOUCHER,A1.CHARGE_CODE,A1.PRODUCT_CODE,A1.BALE_NO,A1.PACKING_DATE  " +
                                    " FROM (SELECT PARTY_CODE,SUBSTRING(PARTY_NAME,1,22) AS PARTY_NAME, " +
                                    " INVOICE_NO,INVOICE_DATE,COALESCE(CRITICAL_LIMIT_AMT,0) AS OPENING_BAL1, " +
                                    " INVOICE_AMT,COALESCE(INV_CRITICAL_LIMIT_AMT,0) AS CLOSING_BAL1,CHARGE_CODE, " +
                                    " COALESCE(INV_CRITICAL_LIMIT_AMT,0) AS OPENING_BAL2," +
                                    " COALESCE(COALESCE(INV_CRITICAL_LIMIT_AMT,0)+INVOICE_AMT,0) AS CLOSING_BAL2, " +
                                    " CASE WHEN PARTY_CHARGE_CODE!=CHARGE_CODE AND CHARGE_CODE IN ('01') THEN 'F6' ELSE '' END AS F6, " +
                                    " PRODUCT_CODE,BALE_NO,PACKING_DATE,PARTY_CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE  " +
                                    " INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"'  AND APPROVED=1 AND CANCELLED=0) AS A1 " +
                                    " LEFT JOIN  (SELECT INVOICE_NO,INVOICE_DATE,INVOICE_AMOUNT,  GROUP_CONCAT(DISTINCT D.VOUCHER_NO ORDER BY D.VOUCHER_NO SEPARATOR ', ') AS VOUCHER  FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX C,FINANCE.D_FIN_VOUCHER_HEADER D   " +
                                    " WHERE INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"'   AND C.VOUCHER_NO=D.VOUCHER_NO AND D.APPROVED=1 AND D.CANCELLED=0   GROUP BY INVOICE_NO,INVOICE_DATE) AS B1  ON A1.INVOICE_NO = B1.INVOICE_NO    " +
                                    " AND B1.INVOICE_DATE = A1.INVOICE_DATE   " +
                                    " ORDER BY A1.INVOICE_DATE,A1.PARTY_CODE,A1.INVOICE_NO";

                    rpt.setReportName("/EITLERP/FeltSales/FeltInvReport/AllChargeCode.jrxml", 1, strSQL); //productlist is the name of my jasper file.
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
    private void PreviewReport_AllChargeCode() {
        String sql;
        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());

        HashMap Parameters = new HashMap();
        
        String curDt = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(CURDATE(),'%d/%m/%Y'),SUBSTRING(NOW(),11,20)) AS DATE FROM DUAL");
        Parameters.put("CURDATE", curDt);
        Parameters.put("FROM_DATE", txtFromDate.getText());
        Parameters.put("TO_DATE", txtToDate.getText());

        TTable objData = new TTable();
        objData.AddColumn("PARTY_CODE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("OPENING_BAL");
        objData.AddColumn("INVOICE_AMT");
        objData.AddColumn("CLOSING_BAL");
        objData.AddColumn("VOUCHER");
        
        try {

            String strSQL = "";
            ResultSet rsReport;

//            //Retrieve data
//            strSQL = "SELECT A1.PARTY_CODE,A1.PARTY_NAME,A1.INVOICE_NO,A1.INVOICE_DATE,(CASE WHEN A1.CHARGE_CODE = '09' THEN OPENING_BAL1 WHEN A1.CHARGE_CODE NOT IN ('09') THEN '' END) AS OPENING_BAL, ";
//            strSQL += "A1.INVOICE_AMT,(CASE WHEN A1.CHARGE_CODE = '09' THEN CLOSING_BAL1 WHEN A1.CHARGE_CODE NOT IN ('09') THEN '0' END) AS CLOSING_BAL ,COALESCE(VOUCHER,'') AS VOUCHER,A1.CHARGE_CODE,A1.PRODUCT_CODE,A1.BALE_NO,A1.PACKING_DATE FROM ";
//            strSQL += "(SELECT PARTY_CODE,SUBSTRING(PARTY_NAME,1,22) AS PARTY_NAME,INVOICE_NO,INVOICE_DATE, ";
//            strSQL += "COALESCE(CRITICAL_LIMIT_AMT,0) AS OPENING_BAL1,INVOICE_AMT,COALESCE(INV_CRITICAL_LIMIT_AMT,0) AS CLOSING_BAL1,CHARGE_CODE,PRODUCT_CODE,BALE_NO,PACKING_DATE";
//            strSQL += " FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ";
//            strSQL += "WHERE  ";
//            strSQL += "INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' ";
//            strSQL += "AND APPROVED=1 AND CANCELLED=0) AS A1 ";
//            strSQL += "LEFT JOIN ";
//            strSQL += "(SELECT INVOICE_NO,INVOICE_DATE,INVOICE_AMOUNT, ";
//            strSQL += "GROUP_CONCAT(DISTINCT D.VOUCHER_NO ORDER BY D.VOUCHER_NO SEPARATOR ', ') AS VOUCHER  ";
//            strSQL += "FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX C,FINANCE.D_FIN_VOUCHER_HEADER D ";
//            strSQL += "WHERE INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' ";
//            strSQL += "AND C.VOUCHER_NO=D.VOUCHER_NO AND D.APPROVED=1 AND D.CANCELLED=0 ";
//            strSQL += "GROUP BY INVOICE_NO,INVOICE_DATE) AS B1 ";
//            strSQL += "ON A1.INVOICE_NO = B1.INVOICE_NO  ";
//            strSQL += "AND B1.INVOICE_DATE = A1.INVOICE_DATE ";
//            strSQL += "ORDER BY A1.INVOICE_DATE,A1.PARTY_CODE,A1.INVOICE_NO ";
            
            strSQL = "SELECT A1.PARTY_CODE,A1.PARTY_NAME,A1.INVOICE_NO,A1.INVOICE_DATE, " +
                    "(CASE WHEN A1.CHARGE_CODE = '09' THEN OPENING_BAL1 WHEN A1.CHARGE_CODE NOT IN ('09','01') THEN OPENING_BAL2 WHEN A1.CHARGE_CODE IN ('01') THEN '' END) " +
                    " AS OPENING_BAL, A1.INVOICE_AMT, " +
                    "(CASE WHEN A1.CHARGE_CODE = '09' THEN CLOSING_BAL1 WHEN A1.CHARGE_CODE NOT IN ('09','01') THEN CLOSING_BAL2 WHEN A1.CHARGE_CODE IN ('01') THEN '' END) " +
                    " AS CLOSING_BAL , " +
                    " " +
                    " VOUCHER,A1.CHARGE_CODE,A1.PRODUCT_CODE,A1.BALE_NO,A1.PACKING_DATE  " +
                    " FROM (SELECT PARTY_CODE,SUBSTRING(PARTY_NAME,1,22) AS PARTY_NAME, " +
                    " INVOICE_NO,INVOICE_DATE,COALESCE(CRITICAL_LIMIT_AMT,0) AS OPENING_BAL1, " +
                    " INVOICE_AMT,COALESCE(INV_CRITICAL_LIMIT_AMT,0) AS CLOSING_BAL1,CHARGE_CODE, " +
                    " COALESCE(INV_CRITICAL_LIMIT_AMT,0) AS OPENING_BAL2, " +
                    " COALESCE(COALESCE(INV_CRITICAL_LIMIT_AMT,0)+INVOICE_AMT,0) AS CLOSING_BAL2, " +
                    " PRODUCT_CODE,BALE_NO,PACKING_DATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE  " +
                    " INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"'  AND APPROVED=1 AND CANCELLED=0) AS A1 " +
                    " LEFT JOIN  (SELECT INVOICE_NO,INVOICE_DATE,INVOICE_AMOUNT,  GROUP_CONCAT(DISTINCT D.VOUCHER_NO ORDER BY D.VOUCHER_NO SEPARATOR ', ') AS VOUCHER  FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX C,FINANCE.D_FIN_VOUCHER_HEADER D   WHERE INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"'   AND C.VOUCHER_NO=D.VOUCHER_NO AND D.APPROVED=1 AND D.CANCELLED=0   GROUP BY INVOICE_NO,INVOICE_DATE) AS B1  ON A1.INVOICE_NO = B1.INVOICE_NO   " +
                    " AND B1.INVOICE_DATE = A1.INVOICE_DATE      " +
                    " ORDER BY A1.INVOICE_DATE,A1.PARTY_CODE,A1.INVOICE_NO;";
            
            System.out.println("SQL : "+strSQL);
            rsReport = data.getResult(strSQL);
            rsReport.first();

            if (rsReport.getRow() > 0) {

                while (!rsReport.isAfterLast()) {
                    TRow objRow = new TRow();

                    objRow.setValue("PARTY_CODE", rsReport.getString("PARTY_CODE"));
                    objRow.setValue("PARTY_NAME", rsReport.getString("PARTY_NAME"));
                    objRow.setValue("INVOICE_NO", rsReport.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE", rsReport.getString("INVOICE_DATE"));
                    objRow.setValue("OPENING_BAL", Double.toString(rsReport.getDouble("OPENING_BAL")));
                    objRow.setValue("INVOICE_AMT", Double.toString(rsReport.getDouble("INVOICE_AMT")));
                    objRow.setValue("CLOSING_BAL", Double.toString(rsReport.getDouble("CLOSING_BAL")));
                    objRow.setValue("VOUCHER", rsReport.getString("VOUCHER"));
                    objRow.setValue("CHARGE_CODE", rsReport.getString("CHARGE_CODE"));
                    objRow.setValue("PRODUCT_CODE", rsReport.getString("PRODUCT_CODE"));
                    objRow.setValue("BALE_NO", rsReport.getString("BALE_NO"));
                    objRow.setValue("PACKING_DATE", rsReport.getString("PACKING_DATE"));
                    
                    objData.AddRow(objRow);
                    rsReport.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/FeltSales/rptAllChargeCodeInvBal.rpt", Parameters, objData);

//        String InvoiceType=2+"";
//        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
//        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
//        
//        Connection Conn = null;
//        Statement st = null;
//        ResultSet rs = null;
//        try {
//            Conn = data.getConn();
//            st = Conn.createStatement();
//
//            HashMap parameterMap = new HashMap();
//
//            parameterMap.put("INVOICE_TYPE", InvoiceType);
//            parameterMap.put("INVOICE_DATE_FROM", FromDate);
//            parameterMap.put("INVOICE_DATE_TO", ToDate);
//            
//            ReportRegister rpt = new ReportRegister(parameterMap, Conn);
//            
//            String strSQL = "SELECT  A1.INVOICE_NO,A1.INVOICE_DATE,A1.PARTY_CODE,A1.PARTY_NAME,A1.INVOICE_AMT, ";
//            strSQL += "A1.CHARGE_CODE,A1.DUE_DAYS,VOUCHER,B1.INVOICE_AMOUNT FROM  ";
//            strSQL += "(SELECT  A.INVOICE_NO,SUBSTRING(A.INVOICE_DATE,1,10) AS INVOICE_DATE,A.PARTY_CODE,B.PARTY_NAME,A.INVOICE_AMT,A.CHARGE_CODE ";
//            strSQL += ", COALESCE(DATEDIFF(A.DUE_DATE,SUBSTRING(A.INVOICE_DATE,1,10)),0) AS DUE_DAYS ";
//            strSQL += "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_PARTY_MASTER B WHERE SUBSTRING(A.INVOICE_DATE,1,10)>='"+FromDate+"'  ";
//            strSQL += "AND SUBSTRING(A.INVOICE_DATE,1,10)<='"+ToDate+"' AND A.INVOICE_TYPE=2  ";
//            strSQL += "AND A.PARTY_CODE= B.PARTY_CODE AND A.APPROVED=1 AND A.CANCELLED=0) AS A1 ";
//            strSQL += "LEFT JOIN ";
//            strSQL += "(SELECT INVOICE_NO,INVOICE_DATE,INVOICE_AMOUNT, ";
//            strSQL += "GROUP_CONCAT(DISTINCT D.VOUCHER_NO ORDER BY D.VOUCHER_NO SEPARATOR ', ') AS VOUCHER  ";
//            strSQL += "FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX C,FINANCE.D_FIN_VOUCHER_HEADER D ";
//            strSQL += " WHERE INVOICE_DATE>='"+FromDate+"'  ";
//            strSQL += "AND INVOICE_DATE<='"+ToDate+"'   ";
//            strSQL += "AND C.VOUCHER_NO= D.VOUCHER_NO AND D.APPROVED =1 AND D.CANCELLED =0 ";
//            strSQL += "GROUP BY INVOICE_NO,INVOICE_DATE) AS B1 ";
//            strSQL += "ON A1.INVOICE_NO = B1.INVOICE_NO  ";
//            strSQL += "AND B1.INVOICE_DATE = A1.INVOICE_DATE   ";
//            strSQL += "ORDER BY A1.INVOICE_NO";
//            
//            rpt.setReportName("/EITLERP/FeltSales/FeltInvReport/rptFeltSalesInvoiceDetail.jrxml", 1, strSQL); //productlist is the name of my jasper file.
//            rpt.callReport();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                st.close();
//                Conn.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
