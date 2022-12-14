/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP.FeltSales.FeltScheme;

/**
 *
 * @author
 */
/*<APPLET CODE=frmInvoiceReport.class HEIGHT=574 WIDTH=758></APPLET>*/
import javax.swing.*;
import java.util.*;
import EITLERP.*;
import TReportWriter.NumWord;
import TReportWriter.SimpleDataProvider.TRow;
import TReportWriter.SimpleDataProvider.TTable;
import TReportWriter.TReportEngine;
import java.lang.*;
import java.sql.*;
import java.lang.String;

//import EITLERP.Purchase.frmSendMail;
public class frmFeltSchemeCNReport extends javax.swing.JApplet {

    private int EditMode = 0;
    private NumWord nw;

    private Connection Conn;
    private Statement Stmt;

    //private clsPackingentry ObjPackingEntry;
    private TReportEngine objEngine = new TReportEngine();
    private EITLComboModel cmbLotModel = new EITLComboModel();

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    //clsColumn ObjColumn=new clsColumn();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private boolean HistoryView = false;
    private String theDocNo = "";
    public frmPendingApprovals frmPA;
    private int charge09index = 0;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();
    String cellLastValue = "";

    /**
     * Creates new form frmSalesParty
     */
    public frmFeltSchemeCNReport() {
        System.gc();
        setSize(500, 350);
        initComponents();
        GenerateCombo();
        btnCNDraft.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        cmbCNDate = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnCNSumm = new javax.swing.JButton();
        btnCNDetail = new javax.swing.JButton();
        btnCNDraft = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("CN Post Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(70, 80, 110, 21);
        getContentPane().add(cmbCNDate);
        cmbCNDate.setBounds(190, 70, 190, 40);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 29));
        jPanel1.setLayout(null);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Felt Scheme Credit Note Report");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(2, 4, 460, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 2, 470, 29);

        btnCNSumm.setText("CN SUMMARY");
        btnCNSumm.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnCNSumm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNSummActionPerformed(evt);
            }
        });
        getContentPane().add(btnCNSumm);
        btnCNSumm.setBounds(20, 180, 190, 30);

        btnCNDetail.setText("CN DETAIL");
        btnCNDetail.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnCNDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNDetailActionPerformed(evt);
            }
        });
        getContentPane().add(btnCNDetail);
        btnCNDetail.setBounds(260, 180, 190, 30);

        btnCNDraft.setText("CN DRAFT ");
        btnCNDraft.setMargin(new java.awt.Insets(2, 7, 2, 7));
        btnCNDraft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCNDraftActionPerformed(evt);
            }
        });
        getContentPane().add(btnCNDraft);
        btnCNDraft.setBounds(140, 240, 190, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCNSummActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCNSummActionPerformed
        String Title = "PARTY WISE EARLY PAYMENT SCHEME CREDIT NOTE SUMMARY";
//        String strSQL = "SELECT A.PARTY_CODE, B.PARTY_NAME, SUM(A.INVOICE_AMT) AS INVOICE_AMT, SUM(A.PI_BASED_CN_AMT) AS PI_BASED_CN_AMT FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE A,DINESHMILLS.D_SAL_PARTY_MASTER B WHERE A.PARTY_CODE=B.PARTY_CODE AND PI_BASED_CN_DATE='" + EITLERPGLOBAL.formatDateDB(cmbCNDate.getSelectedItem().toString()) + "' AND PI_BASED_CN_NO!=''  GROUP BY A.PARTY_CODE";
        String strSQL = "SELECT A.PARTY_CODE, B.PARTY_NAME, SUM(A.INVOICE_AMT) AS INVOICE_AMT, SUM(A.BASIC_VALUE) AS BASIC_AMT, SUM(A.PI_BASED_CN_AMT) AS PI_BASED_CN_AMT FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE A,DINESHMILLS.D_SAL_PARTY_MASTER B WHERE A.PARTY_CODE=B.PARTY_CODE AND PI_BASED_CN_DATE='" + EITLERPGLOBAL.formatDateDB(cmbCNDate.getSelectedItem().toString()) + "' AND PI_BASED_CN_NO!=''  GROUP BY A.PARTY_CODE";

        try {

            Connection Conn = data.getConn();
            Statement st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("TITLE", Title);

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/FeltSales/FeltScheme/FeltSchemeCNSummary.jrxml", 1, strSQL);
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCNSummActionPerformed

    private void btnCNDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCNDetailActionPerformed
        // TODO add your handling code here:
        try {
            String strSQL = "";

            strSQL = "SELECT * FROM (SELECT CONCAT(A.PARTY_CODE,A.PI_BASED_CN_NO) AS ID_NO, '210010' AS MAIN_CODE, ";
            strSQL += "'435077' AS AC_MAIN_CODE, 'CASH/LC DISCOUNT' AS AC_NAME, PIECE_NO, ";
            strSQL += "A.PARTY_CODE, B.PARTY_NAME, DATE_FORMAT(INVOICE_DATE,\"%d/%m/%Y\") AS INVOICE_DATE, INVOICE_NO, ";
            strSQL += "RIGHT(PI_BASED_CN_NO,5) AS PI_BASED_CN_NO, DATE_FORMAT(PI_BASED_CN_DATE,\"%d/%m/%Y\") AS PI_BASED_CN_DATE,  ";
            strSQL += "ROUND(INVOICE_AMT,2) AS INVOICE_AMT, ROUND(BASIC_VALUE,2) AS BAS_AMT, ";
            strSQL += "DATE_FORMAT(RC_VOUCHER_DATE,\"%d/%m/%Y\") AS RC_VOUCHER_DATE, DATE_FORMAT(PI_DATE,\"%d/%m/%Y\") AS  PI_DATE, ";
            strSQL += "CASE WHEN DATEDIFF(RC_VOUCHER_DATE,PI_DATE)>7 THEN '0.75' ELSE '1.50' END AS PER,  ";
            strSQL += "DATEDIFF(RC_VOUCHER_DATE,PI_DATE) AS DAYS, INVOICE_CHARGE_CODE, ROUND(PI_BASED_CN_AMT,2) AS PI_BASED_CN_AMT  ";
            strSQL += "FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE A,DINESHMILLS.D_SAL_PARTY_MASTER B  ";
            strSQL += "WHERE A.PARTY_CODE=B.PARTY_CODE AND PI_BASED_CN_NO!='') A ";
            strSQL += "LEFT JOIN ";
            strSQL += "(SELECT CONCAT(A.PARTY_CODE,A.PI_BASED_CN_NO) AS DN_ID_NO,  ";
            strSQL += "SUM(PI_BASED_CN_AMT) AS PI_BASED_DN_AMT  ";
            strSQL += "FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE A,DINESHMILLS.D_SAL_PARTY_MASTER B  ";
            strSQL += "WHERE A.PARTY_CODE=B.PARTY_CODE AND PI_BASED_CN_NO!=''  ";
            strSQL += "GROUP BY A.PARTY_CODE,PI_BASED_CN_NO) B ";
            strSQL += "ON A.ID_NO=B.DN_ID_NO";

            Conn = data.getConn();
            Stmt = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());

            EITLERP.ReportRegister rpt = new EITLERP.ReportRegister(parameterMap, Conn);

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/FeltSales/FeltScheme/FeltSchemeCNDetail.jrxml", 1, strSQL);
            rpt.callReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCNDetailActionPerformed

    private void btnCNDraftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCNDraftActionPerformed
        try {
            String strSQL = "";
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            data.Execute("TRUNCATE TEMP_DATABASE.TEMP_FELT_SCHEME_CN");
            data.Execute("INSERT INTO TEMP_DATABASE.TEMP_FELT_SCHEME_CN SELECT A.PARTY_CODE, B.PARTY_NAME, PI_BASED_CN_NO, PI_BASED_CN_DATE, SUM(A.PI_BASED_CN_AMT) AS PI_BASED_CN_AMT, '' FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE A,DINESHMILLS.D_SAL_PARTY_MASTER B  WHERE A.PARTY_CODE=B.PARTY_CODE AND PI_BASED_CN_DATE='" + EITLERPGLOBAL.formatDateDB(cmbCNDate.getSelectedItem().toString()) + "' AND PI_BASED_CN_NO!='' GROUP BY A.PARTY_CODE,A.PI_BASED_CN_NO ");

            ResultSet rsData = data.getResult("SELECT * FROM TEMP_DATABASE.TEMP_FELT_SCHEME_CN ORDER BY PARTY_CODE");
            rsData.first();

            if (rsData.getRow() > 0) {
                while (!rsData.isAfterLast()) {
                    System.out.println("PARTY_CODE : " + rsData.getString("PARTY_CODE"));

                    NumWord num = new NumWord();
                    String rsInWord = num.convertNumToWord(Math.round(rsData.getDouble("PI_BASED_CN_AMT")));

                    String upSQL = "UPDATE TEMP_DATABASE.TEMP_FELT_SCHEME_CN SET CN_AMT_IN_WORD = '" + rsInWord + "' WHERE PARTY_CODE='" + rsData.getString("PARTY_CODE") + "' ";
                    System.out.println("UPDATE SQL : " + upSQL);
                    Stmt.execute(upSQL);

                    rsData.next();
                }
            }

            strSQL = "SELECT * FROM TEMP_DATABASE.TEMP_FELT_SCHEME_CN ORDER BY PARTY_CODE";

            HashMap parameterMap = new HashMap();
            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            rpt.setReportName("/EITLERP/FeltSales/FeltScheme/FeltSchemeCNDraft.jrxml", 1, strSQL);
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCNDraftActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCNDetail;
    private javax.swing.JButton btnCNDraft;
    private javax.swing.JButton btnCNSumm;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbCNDate;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    private boolean GenerateCombo() {
        int i = 0;
        try {

            cmbLotModel = new EITLComboModel();
            cmbCNDate.removeAllItems();
            cmbCNDate.setModel(cmbLotModel);

            ResultSet rs = data.getResult("SELECT DISTINCT PI_BASED_CN_DATE FROM PRODUCTION.ANNUAL_ORDER_INCENTIVE WHERE PI_BASED_CN_DATE NOT IN ('0000-00-00') ORDER BY PI_BASED_CN_DATE DESC");
            rs.first();

            while (!rs.isAfterLast()) {
                ComboData objData = new ComboData();
                objData.Code = i;
                objData.Text = EITLERPGLOBAL.formatDate(rs.getString("PI_BASED_CN_DATE"));
                cmbLotModel.addElement(objData);
                i++;
                rs.next();
            }
            if (i > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {

        }
        return false;
    }

    public static void PrintCNSummary(String title, String strSQL) {

    }
}
