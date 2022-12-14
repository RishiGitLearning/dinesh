/*
 * frmRptInvoiceList.java
 *
 * Created on June 15, 2010, 5:26 PM
 */
package EITLERP.Finance.ReportsUI;

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
public class frmDebitNoteRpt extends javax.swing.JApplet {

    private EITLComboModel cmbInvoiceTypeModel;

    private TReportEngine objEngine = new TReportEngine();

    /**
     * Initializes the applet frmRptInvoiceList
     */
    public void init() {
        setSize(500, 300);
        DNDataProcess();
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
        cmdAcc = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmdSales = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("From Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 52, 100, 16);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("To Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(220, 50, 100, 16);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(120, 50, 100, 28);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(320, 50, 100, 28);

        cmdAcc.setText("Account Copy");
        cmdAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAccActionPerformed(evt);
            }
        });
        getContentPane().add(cmdAcc);
        cmdAcc.setBounds(90, 180, 290, 28);

        cmdCancel.setText("CLEAR");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(180, 220, 100, 28);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 29));
        jPanel1.setLayout(null);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Debit Note Report");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(2, 4, 460, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 2, 470, 29);

        cmdSales.setText("Sales Copy");
        cmdSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSalesActionPerformed(evt);
            }
        });
        getContentPane().add(cmdSales);
        cmdSales.setBounds(90, 140, 290, 28);

        jLabel9.setText("Party Code");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(30, 90, 90, 20);

        txtPartyCode.setToolTipText("Press F1 key for search Party Code");
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });
        getContentPane().add(txtPartyCode);
        txtPartyCode.setBounds(120, 90, 110, 28);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAccActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        } else {
            PreviewReportAcc();
        }
    }//GEN-LAST:event_cmdAccActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        txtFromDate.setText("");
        txtToDate.setText("");
        txtFromDate.requestFocus();
        txtPartyCode.setText("");
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalesActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        } else {
            PreviewReportSales();
        }
    }//GEN-LAST:event_cmdSalesActionPerformed

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed

        if (evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    txtPartyCode.setText(aList.ReturnVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAcc;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdSales;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtPartyCode;
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

    private void PreviewReportAcc() {

        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            Conn = data.getConn();
            st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            String fdt = txtFromDate.getText();
            String tdt = txtToDate.getText();
            String FromDate = EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
            String ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText());

            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("FROM_DATE", fdt);
            parameterMap.put("TO_DATE", tdt);

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            String strSQL = "SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR ";
            strSQL += "WHERE DEBITNOTE_VOUCHER_DATE>='" + FromDate + "' ";
            strSQL += "AND DEBITNOTE_VOUCHER_DATE<='" + ToDate + "' ";

            if (!txtPartyCode.getText().equals("")) {
                strSQL += "AND DB_PARTY_CODE= '" + txtPartyCode.getText() + "' ";
            }

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/Finance/ReportsUI/Acc_DM_GSTR.jrxml", 1, strSQL); //productlist is the name of my jasper file.
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

    private void PreviewReportSales() {

        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            Conn = data.getConn();
            st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            String fdt = txtFromDate.getText();
            String tdt = txtToDate.getText();
            String FromDate = EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
            String ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText());

            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("FROM_DATE", fdt);
            parameterMap.put("TO_DATE", tdt);

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

//            String strSQL = "SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP ";
            String strSQL = "SELECT A.*,B.TOTAL_AMT FROM ";
            strSQL += "(SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR) AS A ";
            strSQL += "LEFT JOIN  ";
            strSQL += "(SELECT DB_PARTY_CODE,SUM(DEBIT_NOTE_AMOUNT) AS TOTAL_AMT FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR ";
            strSQL += "GROUP BY DB_PARTY_CODE) AS B ";
            strSQL += "ON A.DB_PARTY_CODE=B.DB_PARTY_CODE ";
            strSQL += "WHERE A.DEBITNOTE_VOUCHER_DATE>='" + FromDate + "' ";
            strSQL += "AND A.DEBITNOTE_VOUCHER_DATE<='" + ToDate + "' ";

            if (!txtPartyCode.getText().equals("")) {
                strSQL += "AND A.DB_PARTY_CODE= '" + txtPartyCode.getText() + "' ";
            }

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/Finance/ReportsUI/Sales_DM_GSTR.jrxml", 1, strSQL); //productlist is the name of my jasper file.
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
    
    public void DNDataProcess() {
        
        String Q1 = "TRUNCATE TABLE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP";
        System.out.println("Q1 : "+Q1);
        data.Execute(Q1);
        
        
        String Q2 = "INSERT INTO  FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP ";
        Q2 += "(BOOK_CODE,DEBITNOTE_VOUCHER_NO,DEBITNOTE_VOUCHER_DATE,INVOICE_NO,INVOICE_DATE,HSN_SAC_CODE,MAIN_ACCOUNT_CODE,DB_PARTY_CODE, ";
        Q2 += "DEBIT_NOTE_AMOUNT,INTEREST_ACCOUNT_CODE,INTEREST_VOUCHER_AMOUNT, ";
        Q2 += "IGST_ACCOUNT_CODE,IGST_AMT,CGST_ACCOUNT_CODE,CGST_AMT,SGST_ACCOUNT_CODE,SGST_AMT, ";
        Q2 += "IGST_CREDIT_CODE,IGST_CREDIT_AMOUNT, ";
        Q2 += "CGST_CREDIT_CODE,CGST_CREDIT_AMOUNT, ";
        Q2 += "SGST_CREDIT_CODE,SGST_CREDIT_AMOUNT, ";
        Q2 += "CST_CODE,CST_VALUE) ";
        Q2 += "SELECT BOOK_CODE,H.VOUCHER_NO,H.VOUCHER_DATE,D.INVOICE_NO,D.INVOICE_DATE ,MAX(HSN_SAC_CODE), ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (210010,210027,210072,125019,125033) AND SUB_ACCOUNT_CODE !='' AND EFFECT ='D' THEN MAIN_ACCOUNT_CODE END, 0)) AS MAIN_CODE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (210010,210027,210072,125019,125033) AND SUB_ACCOUNT_CODE !='' AND EFFECT = 'D' THEN SUB_ACCOUNT_CODE END, 0)) AS SUB_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (210010,210027,210072,125019,125033) AND SUB_ACCOUNT_CODE !='' AND EFFECT = 'D' THEN AMOUNT END, 0)) AS TOTVALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN (210010,210027,210072,125019,125033,127570,127575,127576,127569,127572,127565,127566,127571,127567,127574,127568,127573,437277,127547,231758,231756,231757) AND SUB_ACCOUNT_CODE ='' AND EFFECT ='C' THEN MAIN_ACCOUNT_CODE END, 0)) AS INT_CODE, ";
//        #MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN (210010,210027,210072,127570,127575,127576,127569,127572,127565,127566,127571,127567,127574,127568,127573) AND SUB_ACCOUNT_CODE ='' AND EFFECT = 'C' THEN SUB_ACCOUNT_CODE END, 0)) AS SUB_CODE,
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN (210010,210027,210072,125019,127570,127575,127576,127569,127572,127565,127566,127571,127567,127574,127568,127573,437277,127547,231758,231756,231757) AND SUB_ACCOUNT_CODE ='' AND EFFECT = 'C' THEN AMOUNT END, 0)) AS TOTVALUE, ";
//        #SUM(COALESCE(CASE WHEN SUB_ACCOUNT_CODE!='' THEN AMOUNT END, 0)),
//        #SUM(COALESCE(CASE WHEN SUB_ACCOUNT_CODE='' THEN AMOUNT END, 0)),
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127570,127575,127576,127569) THEN MAIN_ACCOUNT_CODE END, 0)) AS IGST_CODE , ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127570,127575,127576,127569) THEN AMOUNT END, 0)) AS IGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127572,127565,127566,127571) THEN MAIN_ACCOUNT_CODE END, 0)) AS CGST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127572,127565,127566,127571) THEN AMOUNT END, 0)) AS CGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127567,127574,127568,127573) THEN MAIN_ACCOUNT_CODE END, 0))AS SGST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127567,127574,127568,127573) THEN AMOUNT END, 0)) AS SGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231758) THEN MAIN_ACCOUNT_CODE END, 0)) AS IGST_CODE , ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231758) THEN AMOUNT END, 0)) AS IGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231756) THEN MAIN_ACCOUNT_CODE END, 0)) AS CGST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231756) THEN AMOUNT END, 0)) AS CGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231757) THEN MAIN_ACCOUNT_CODE END, 0))AS SGST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231757) THEN AMOUNT END, 0)) AS SGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (437277,127547) THEN MAIN_ACCOUNT_CODE END, 0))AS CST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (437277,127547) THEN AMOUNT END, 0)) AS CST_VALUE ";
        Q2 += "FROM  FINANCE.D_FIN_VOUCHER_DETAIL D,FINANCE.D_FIN_VOUCHER_HEADER H ";
        Q2 += "WHERE SUBSTRING(D.VOUCHER_NO,1,2)  ='DN' ";
        Q2 += "AND H.VOUCHER_NO = D.VOUCHER_NO ";
        Q2 += "AND H.CANCELLED =0 AND H.APPROVED IN (1) ";
        Q2 += "AND H.VOUCHER_DATE >='2017-07-01' AND BOOK_CODE IN (12,16,18,45) AND H.VOUCHER_NO NOT IN (SELECT DISTINCT DEBITNOTE_VOUCHER_NO FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR) ";
        Q2 += "GROUP BY  BOOK_CODE,H.VOUCHER_NO,H.VOUCHER_DATE,D.INVOICE_NO,D.INVOICE_DATE "; 

        System.out.println("Q2 : "+Q2);
        data.Execute(Q2);
        
        
        String Q3 = "UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M, FINANCE.D_SAL_GSTR_INVOICE_ERP E SET M.HSN_SAC_CODE = E.HSN_CODE WHERE M.INVOICE_NO = E.INVOICE_NO AND M.INVOICE_DATE = E.INVOICE_DATE AND HSN_SAC_CODE ='' ";
        System.out.println("Q3 : "+Q3);
        data.Execute(Q3);
        
        
        String Q4 = "UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M SET HSN_SAC_CODE = 5515 WHERE  SUBSTRING(INVOICE_NO,1,2) ='00' AND HSN_SAC_CODE ='' ";
        System.out.println("Q4 : "+Q4);
        data.Execute(Q4);
        
        
        String Q5 = "UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M SET HSN_SAC_CODE = 59113290 WHERE  SUBSTRING(INVOICE_NO,1,2) ='F0' AND HSN_SAC_CODE ='' ";
        System.out.println("Q5 : "+Q5);
        data.Execute(Q5);
        
        
        
        String Q6 = "UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M SET HSN_SAC_CODE = 59113290 WHERE  SUBSTRING(INVOICE_NO,1,2) ='B0' AND HSN_SAC_CODE ='' ";
        System.out.println("Q6 : "+Q6);
        data.Execute(Q6);
        
        
        
        String Q7 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP  T, FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M SET T.DEBITMEMO_NO =M.DEBITMEMO_NO,T.DEBITMEMO_DATE = M.DEBITMEMO_DATE ,T.RECEIPT_VOUCHER_NO = M.RECEIPT_VOUCHER_NO, T.INVOICE_DUE_DATE=M.INVOICE_DUE_DATE ,T.VALUE_DATE=M.VALUE_DATE,T.DAYS = M.DAYS,T.INTEREST_PER = M.INTEREST_PER, T.INTEREST_AMT = SUM(M.INTEREST_AMT),T.HSN_SAC_CODE = M.HSN_SAC_CODE WHERE  T.INVOICE_NO = M.INVOICE_NO AND T.INVOICE_DATE = M.INVOICE_DATE AND T.MAIN_ACCOUNT_CODE = M.MAIN_ACCOUNT_CODE AND BOOK_CODE IN (12,16,18) AND T.DB_PARTY_CODE = M.DB_PARTY_CODE GROUP BY M.INVOICE_NO,M.INVOICE_DATE,M.MAIN_ACCOUNT_CODE,M.DB_PARTY_CODE "; 
        System.out.println("Q7 : "+Q7);
        data.Execute(Q7);
        
        
//        #SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP;
        
//        #SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER;
        
        String Q8 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T,DINESHMILLS.D_SAL_PARTY_MASTER M,DINESHMILLS.D_SAL_STATE_MASTER S SET T.PARTY_GSTIN_ID = M.GSTIN_NO,T.PARTY_PLACE_OF_SUPPLY= S.GST_PLACE_OF_SUPPLY,DOCUMENT_TYPE ='D', PARTY_SUPP_NAME =PARTY_NAME, PARTY_ADDRESS1 = ADDRESS1, PARTY_ADDRESS2 = ADDRESS2, PARTY_DISPATCH_STATION = DISPATCH_STATION ,COMPANY_NAME = 'SHRI DINESH MILLS LIMITED' ,COMPANY_ADDRESS ='P.O. BOX NO.-2501, PADRA ROAD,VADODARA-390005, GUJARAT' ,COMPANY_GSTIN ='24AADCDCS3115Q1Z8' PHONE: 2960060/61/62/63/66, URL: www.dineshmills.com' WHERE M.PARTY_CODE = T.DB_PARTY_CODE AND S.STATE_GST_CODE = M.STATE_GST_CODE AND BOOK_CODE IN (12,16,18) ";
        System.out.println("Q8 : "+Q8);
        data.Execute(Q8);
        
        
//#SELECT * FROM DINESHMILLS.D_COM_SUPP_MASTER;
        
        String Q9 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T,DINESHMILLS.D_COM_SUPP_MASTER M SET DOCUMENT_TYPE ='D', PARTY_SUPP_NAME =SUPP_NAME, PARTY_ADDRESS1 = ADD1, PARTY_ADDRESS2 = ADD2, PARTY_ADDRESS3 = ADD3, PARTY_DISPATCH_STATION = CITY WHERE M.SUPPLIER_CODE = T.DB_PARTY_CODE AND M.MAIN_ACCOUNT_CODE = T.MAIN_ACCOUNT_CODE AND BOOK_CODE IN (12,16,18) "; 
        System.out.println("Q9 : "+Q9);
        data.Execute(Q9);        
        
        
        String Q10 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T,DINESHMILLS.D_COM_SUPP_MASTER M SET DOCUMENT_TYPE ='D', PARTY_SUPP_NAME =SUPP_NAME, PARTY_ADDRESS1 = ADD1, PARTY_ADDRESS2 = ADD2, PARTY_ADDRESS3 = ADD3, PARTY_DISPATCH_STATION = CITY ,COMPANY_NAME = 'SHRI DINESH MILLS LIMITED' ,COMPANY_ADDRESS ='P.O. BOX NO.-2501, PADRA ROAD,VADODARA-390005, GUJARAT' ,COMPANY_GSTIN ='24AADCDCS3115Q1Z8' , PHONE: 2960060/61/62/63/66, URL: www.dineshmills.com' WHERE M.SUPPLIER_CODE = T.DB_PARTY_CODE AND M.MAIN_ACCOUNT_CODE = T.MAIN_ACCOUNT_CODE AND BOOK_CODE IN (45) ";
        System.out.println("Q10 : "+Q10);
        data.Execute(Q10);
        
        
        
        String Q11 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T,DINESHMILLS.D_COM_SUPP_MASTER M,DINESHMILLS.D_SAL_STATE_MASTER S SET T.PARTY_GSTIN_ID = M.GSTIN_NO,T.PARTY_PLACE_OF_SUPPLY= S.GST_PLACE_OF_SUPPLY,DOCUMENT_TYPE ='D', PARTY_SUPP_NAME =SUPP_NAME, PARTY_ADDRESS1 = ADD1, PARTY_ADDRESS2 = ADD2, PARTY_ADDRESS3 = ADD3, PARTY_DISPATCH_STATION = CITY WHERE M.SUPPLIER_CODE = T.DB_PARTY_CODE AND M.MAIN_ACCOUNT_CODE = T.MAIN_ACCOUNT_CODE AND S.STATE_GST_CODE = M.STATE_GST_CODE AND BOOK_CODE IN (45) ";
        System.out.println("Q11 : "+Q11);
        data.Execute(Q11);
        
        
        String Q12 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.MAIN_ACCOUNT_CODE_NAME = G.ACCOUNT_NAME WHERE T.MAIN_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE "; 
        System.out.println("Q12 : "+Q12);
        data.Execute(Q12);
        
        
        String Q13 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.INTEREST_CODE_NAME = G.ACCOUNT_NAME WHERE T.INTEREST_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE "; 
        System.out.println("Q13 : "+Q13);
        data.Execute(Q13);
        
        
        String Q14 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.IGST_ACCOUNT_NAME = G.ACCOUNT_NAME,T.IGST_PER = G.GST_PERCENT WHERE T.IGST_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE "; 
        System.out.println("Q14 : "+Q14);
        data.Execute(Q14);
        
        
        String Q15 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.CGST_ACCOUNT_NAME = G.ACCOUNT_NAME,T.CGST_PER = G.GST_PERCENT WHERE  T.CGST_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q15 : "+Q15);
        data.Execute(Q15);
        
        
        String Q16 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.SGST_ACCOUNT_NAME = G.ACCOUNT_NAME,T.SGST_PER = G.GST_PERCENT WHERE T.SGST_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q16 : "+Q16);
        data.Execute(Q16);
        
        
        String Q17 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.CST_CODE_NAME = G.ACCOUNT_NAME WHERE T.CST_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q17 : "+Q17);
        data.Execute(Q17);
        
        
        String Q18 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.IGST_CREDIT_NAME = G.ACCOUNT_NAME,T.IGST_PER = G.GST_PERCENT WHERE T.IGST_CREDIT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q18 : "+Q18);
        data.Execute(Q18);
        
        
        String Q19 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.CGST_CREDIT_NAME = G.ACCOUNT_NAME,T.CGST_PER = G.GST_PERCENT WHERE T.CGST_CREDIT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q19 : "+Q19);
        data.Execute(Q19);
        
        
        String Q20 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.SGST_CREDIT_NAME = G.ACCOUNT_NAME,T.SGST_PER = G.GST_PERCENT WHERE T.SGST_CREDIT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q20 : "+Q20);
        data.Execute(Q20);
        
        
//        SELECT * FROM FINANCE.D_FIN_GL G;

//#SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP WHERE HSN_SAC_CODE = '' AND BOOK_CODE =12 SUBSTRING(INVOICE_NO,1,2) ='SU'

//#SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M WHERE  SUBSTRING(INVOICE_NO,1,2) ='00' AND HSN_SAC_CODE =''
        
//        #SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M WHERE  SUBSTRING(INVOICE_NO,1,2) ='00'
        
        String Q21 = "INSERT INTO FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP ";
        System.out.println("Q21 : "+Q21);
        data.Execute(Q21);

 
//SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP; 
        
//        SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR;

//#WHERE BOOK_CODE =45;
        
    }
}
