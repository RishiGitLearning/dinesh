/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author  root
 */
import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import TReportWriter.*;

public class frmRptBrokerCommission extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(424,264);
        initComponents();
        
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdPreview = new javax.swing.JButton();
        cmdPreviewSummary = new javax.swing.JButton();
        cmdPreviewSummary1 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("BROKER COMMISSION REPORT");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 230, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("From Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 70, 90, 20);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(120, 70, 90, 19);

        jLabel3.setText("To Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(220, 70, 60, 20);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(285, 70, 90, 20);

        jLabel1.setText("Period");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(14, 46, 90, 15);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(40, 120, 130, 25);

        cmdPreviewSummary.setText("Preview Summary Rate");
        cmdPreviewSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewSummaryActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreviewSummary);
        cmdPreviewSummary.setBounds(180, 120, 185, 25);

        cmdPreviewSummary1.setText("Preview Summary Broker");
        cmdPreviewSummary1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewSummary1ActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreviewSummary1);
        cmdPreviewSummary1.setBounds(181, 151, 189, 25);

    }//GEN-END:initComponents
    
    private void cmdPreviewSummary1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewSummary1ActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        GenerateSummary1();
    }//GEN-LAST:event_cmdPreviewSummary1ActionPerformed
    
    private void cmdPreviewSummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewSummaryActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        GenerateSummary();
    }//GEN-LAST:event_cmdPreviewSummaryActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        
        GenerateReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewSummary;
    private javax.swing.JButton cmdPreviewSummary1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("DEPOSIT_SCHEME");
            objReportData.AddColumn("RECEIPT_NO");
            objReportData.AddColumn("LEGACY_NO");
            objReportData.AddColumn("RECEIPT_DATE");
            objReportData.AddColumn("BROKER_CODE");
            objReportData.AddColumn("BROKER_NAME");
            objReportData.AddColumn("APPLICANT_NAME");
            objReportData.AddColumn("AMOUNT");
            objReportData.AddColumn("PERIOD");
            objReportData.AddColumn("PERCENTAGE_OF_BROKER");
            objReportData.AddColumn("COMMISSION");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("DEPOSIT_SCHEME","");
            objOpeningRow.setValue("RECEIPT_NO","");
            objOpeningRow.setValue("LEGACY_NO","");
            objOpeningRow.setValue("RECEIPT_DATE","0000-00-00");
            objOpeningRow.setValue("BROKER_CODE","");
            objOpeningRow.setValue("BROKER_NAME","");
            objOpeningRow.setValue("APPLICANT_NAME","");
            objOpeningRow.setValue("AMOUNT","");
            objOpeningRow.setValue("PERIOD","");
            objOpeningRow.setValue("PERCENTAGE_OF_BROKER","");
            objOpeningRow.setValue("COMMISSION","");
            
            String strSQL = "";
            //broker commission
            
            strSQL="SELECT A.RECEIPT_NO,A.LEGACY_NO,A.RECEIPT_DATE,A.BROKER_CODE,A.APPLICANT_NAME,A.BROKER_NAME,A.AMOUNT,A.DEPOSIT_PERIOD,B.PERCENTAGE_OF_BROKER,IF(A.BROKER_CODE <> '000',(A.AMOUNT*B.PERCENTAGE_OF_BROKER)/100,0) AS COMMISSION, "+
            "CASE C.SCHEME_TYPE "+
            "WHEN 1 THEN 'FD' "+
            "WHEN 2 THEN 'LD' "+
            "WHEN 3 THEN 'CD' "+
            "END AS  DEPOSIT_SCHEME "+
            "FROM D_FD_DEPOSIT_MASTER A,D_FD_SCHEME_PERIOD B,D_FD_SCHEME_MASTER C "+
            "WHERE A.SCHEME_ID = B.SCHEME_ID "+
            "AND A.SCHEME_ID = C.SCHEME_ID "+
            "AND A.DEPOSIT_STATUS = 0 "+
            "AND A.REJECTED = 0 "+
            "AND A.CANCELLED=0 "+
            "AND A.APPROVED=1 "+
            "AND A.COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " "+
            "AND A.DEPOSIT_PERIOD = B.INTEREST_MONTH "+
            "AND (A.RECEIPT_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' "+
            "AND A.RECEIPT_DATE<= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"' ) "+
            "ORDER BY A.BROKER_CODE";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("DEPOSIT_SCHEME",UtilFunctions.getString(rsTmp,"DEPOSIT_SCHEME",""));
                    objRow.setValue("RECEIPT_NO",UtilFunctions.getString(rsTmp,"RECEIPT_NO",""));
                    objRow.setValue("LEGACY_NO",UtilFunctions.getString(rsTmp,"LEGACY_NO",""));
                    objRow.setValue("RECEIPT_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"RECEIPT_DATE","0000-00-00")));
                    objRow.setValue("BROKER_CODE",UtilFunctions.getString(rsTmp,"BROKER_CODE",""));
                    objRow.setValue("BROKER_NAME",UtilFunctions.getString(rsTmp,"BROKER_NAME",""));
                    objRow.setValue("APPLICANT_NAME",UtilFunctions.getString(rsTmp,"APPLICANT_NAME",""));
                    objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                    objRow.setValue("PERIOD",UtilFunctions.getString(rsTmp,"DEPOSIT_PERIOD",""));
                    objRow.setValue("PERCENTAGE_OF_BROKER",UtilFunctions.getString(rsTmp,"PERCENTAGE_OF_BROKER",""));
                    objRow.setValue("COMMISSION",UtilFunctions.getString(rsTmp,"COMMISSION",""));
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            HashMap Parameters=new HashMap();
            Parameters.put("FROM_DATE",txtFromDate.getText().trim());
            Parameters.put("TO_DATE",txtToDate.getText().trim());
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptBrokerCommission.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void GenerateSummary() {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("TOTAL_RECEIPT");
            objReportData.AddColumn("TOTAL_AMOUNT");
            objReportData.AddColumn("PERCENTAGE_OF_BROKER");
            objReportData.AddColumn("COMMISSION");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("TOTAL_RECEIPT","");
            objOpeningRow.setValue("TOTAL_AMOUNT","");
            objOpeningRow.setValue("PERCENTAGE_OF_BROKER","");
            objOpeningRow.setValue("TOTAL_COMMISSION","");
            
            
            //   int ReceiptType = cmbReceiptType.getSelectedIndex() + 1;
            // int ReportType = cmbReportType.getSelectedIndex() + 1;
            String strSQL = "";
            
            
            strSQL= "SELECT COUNT(A.RECEIPT_NO) AS TOTAL_RECEIPT,SUM(A.AMOUNT) AS TOTAL_AMOUNT,'' AS PERCENTAGE_OF_BROKER, "+
            "'0' AS COMMISSION "+
            "FROM D_FD_DEPOSIT_MASTER A,D_FD_SCHEME_PERIOD B "+
            "WHERE A.SCHEME_ID = B.SCHEME_ID "+
            "AND A.DEPOSIT_STATUS = 0 AND A.REJECTED = 0 AND A.CANCELLED=0 AND A.APPROVED=1 "+
            "AND A.DEPOSIT_PERIOD = B.INTEREST_MONTH "+
            "AND A.COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " "+
            "AND A.BROKER_CODE = '000' "+
            "AND(A.RECEIPT_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' AND A.RECEIPT_DATE<= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"' ) "+
            "GROUP BY A.BROKER_CODE "+
            "UNION "+
            "SELECT COUNT(A.RECEIPT_NO) AS TOTAL_RECEIPT,SUM(A.AMOUNT) AS TOTAL_AMOUNT, B.PERCENTAGE_OF_BROKER, "+
            "ROUND(SUM((A.AMOUNT * B.PERCENTAGE_OF_BROKER)/100),2) AS COMMISSION "+
            "FROM D_FD_DEPOSIT_MASTER A,D_FD_SCHEME_PERIOD B "+
            "WHERE A.SCHEME_ID = B.SCHEME_ID "+
            "AND A.DEPOSIT_STATUS = 0 AND A.REJECTED = 0 AND A.CANCELLED=0 AND A.APPROVED=1 "+
            "AND A.DEPOSIT_PERIOD = B.INTEREST_MONTH "+
            "AND A.COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " "+
            "AND A.BROKER_CODE <> '000' "+
            "AND(A.RECEIPT_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' AND A.RECEIPT_DATE<= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"' ) "+
            "GROUP BY B.PERCENTAGE_OF_BROKER ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("TOTAL_RECEIPT",UtilFunctions.getString(rsTmp,"TOTAL_RECEIPT",""));
                    objRow.setValue("TOTAL_AMOUNT",UtilFunctions.getString(rsTmp,"TOTAL_AMOUNT",""));
                    objRow.setValue("PERCENTAGE_OF_BROKER",UtilFunctions.getString(rsTmp,"PERCENTAGE_OF_BROKER",""));
                    objRow.setValue("COMMISSION",UtilFunctions.getString(rsTmp,"COMMISSION",""));
                    
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("FROM_DATE",txtFromDate.getText().trim());
            Parameters.put("TO_DATE",txtToDate.getText().trim());
            
            
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptBrokerCommissionSummaryRateWise.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void GenerateSummary1() {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("BROKER_CODE");
            objReportData.AddColumn("BROKER_NAME");
            objReportData.AddColumn("RECEIPTS");
            objReportData.AddColumn("DEPOSIT_AMOUNT");
            objReportData.AddColumn("COMMISSION");
            
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("BROKER_CODE","");
            objOpeningRow.setValue("BROKER_NAME","");
            objOpeningRow.setValue("RECEIPTS","");
            objOpeningRow.setValue("DEPOSIT_AMOUNT","");
            objOpeningRow.setValue("COMMISSION","");

            String strSQL = "";
            
            strSQL= "SELECT A.BROKER_CODE,A.BROKER_NAME,COUNT(A.RECEIPT_NO) AS RECEIPTS,SUM(A.AMOUNT) AS DEPOSIT_AMOUNT, "+
            "SUM(IF (A.BROKER_CODE <> '000',(A.AMOUNT * B.PERCENTAGE_OF_BROKER)/100,0)) AS COMMISSION "+
            "FROM D_FD_DEPOSIT_MASTER A,D_FD_SCHEME_PERIOD B "+
            "WHERE A.SCHEME_ID = B.SCHEME_ID "+
            "AND A.DEPOSIT_PERIOD = B.INTEREST_MONTH "+
            "AND A.DEPOSIT_STATUS = 0 AND A.REJECTED = 0 AND A.CANCELLED=0 AND A.APPROVED=1 AND A.COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " "+
            "AND (A.RECEIPT_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' AND A.RECEIPT_DATE<= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"' ) "+
            "GROUP BY A.BROKER_CODE "+
            "ORDER BY A.BROKER_CODE ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("BROKER_CODE",UtilFunctions.getString(rsTmp,"BROKER_CODE",""));
                    objRow.setValue("BROKER_NAME",UtilFunctions.getString(rsTmp,"BROKER_NAME",""));
                    objRow.setValue("RECEIPTS",UtilFunctions.getString(rsTmp,"RECEIPTS",""));
                    objRow.setValue("DEPOSIT_AMOUNT",UtilFunctions.getString(rsTmp,"DEPOSIT_AMOUNT",""));
                    if(UtilFunctions.getString(rsTmp,"BROKER_CODE","").equals("000") || UtilFunctions.getString(rsTmp,"BROKER_CODE","").equals("")) {
                        objRow.setValue("COMMISSION","00");
                    }
                    else{
                        objRow.setValue("COMMISSION",UtilFunctions.getString(rsTmp,"COMMISSION",""));
                    }
                    
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("FROM_DATE",txtFromDate.getText().trim());
            Parameters.put("TO_DATE",txtToDate.getText().trim());
            
            
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptBrokerCommissionSummaryBrokerwise.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean Validate() {
        //Form level validations
        
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter from Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date in DD/MM/YYYY format.");
            return false;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter To Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date in DD/MM/YYYY format.");
            return false;
        }
        
        return true;
    }
    
}
