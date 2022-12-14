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

public class frmRptDepositRegister extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(424,264);
        initComponents();
        
        GenerateCombo();
        
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
        txtAsonDate = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cmbReceiptType = new javax.swing.JComboBox();
        cmdPreviewSummary = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("Deposit Register");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 230, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("As On Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 72, 90, 15);

        txtAsonDate.setColumns(10);
        getContentPane().add(txtAsonDate);
        txtAsonDate.setBounds(120, 70, 90, 19);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(20, 150, 130, 25);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Receipt Type :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 103, 90, 15);

        getContentPane().add(cmbReceiptType);
        cmbReceiptType.setBounds(120, 100, 90, 20);

        cmdPreviewSummary.setText("Preview Summary");
        cmdPreviewSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewSummaryActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreviewSummary);
        cmdPreviewSummary.setBounds(170, 150, 160, 25);

    }//GEN-END:initComponents
    
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
    private javax.swing.JComboBox cmbReceiptType;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewSummary;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtAsonDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("RECEIPT_NO");
            objReportData.AddColumn("LEGACY_NO");
            objReportData.AddColumn("RECEIPT_DATE");
            objReportData.AddColumn("EFFECTIVE_DATE");
            objReportData.AddColumn("MATURITY_DATE");
            objReportData.AddColumn("APPLICANT_NAME");
            objReportData.AddColumn("ADDRESS1");
            objReportData.AddColumn("ADDRESS2");
            objReportData.AddColumn("ADDRESS3");
            objReportData.AddColumn("CITY");
            objReportData.AddColumn("AMOUNT");
            objReportData.AddColumn("DEPOSIT_PERIOD");
            objReportData.AddColumn("INTEREST_RATE");
            objReportData.AddColumn("BROKER_CODE");
            objReportData.AddColumn("DEPOSITER_STATUS");
            objReportData.AddColumn("PARTY_CODE");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("RECEIPT_NO","");
            objOpeningRow.setValue("LEGACY_NO","");
            objOpeningRow.setValue("RECEIPT_DATE","0000-00-00");
            objOpeningRow.setValue("EFFECTIVE_DATE","0000-00-00");
            objOpeningRow.setValue("MATURITY_DATE","0000-00-00");
            objOpeningRow.setValue("APPLICANT_NAME","");
            objOpeningRow.setValue("ADDRESS1","");
            objOpeningRow.setValue("ADDRESS2","");
            objOpeningRow.setValue("ADDRESS3","");
            objOpeningRow.setValue("CITY","");
            objOpeningRow.setValue("AMOUNT","");
            objOpeningRow.setValue("DEPOSIT_PERIOD","");
            objOpeningRow.setValue("INTEREST_RATE","");
            objOpeningRow.setValue("BROKER_CODE","");
            objOpeningRow.setValue("DEPOSITER_STATUS","");
            objOpeningRow.setValue("PARTY_CODE","");
            
            
            
            
            int ReceiptType = cmbReceiptType.getSelectedIndex() ;
            
            String strSQL = "SELECT A.RECEIPT_NO, "+
            "A.LEGACY_NO, A.RECEIPT_DATE,A.EFFECTIVE_DATE, A.MATURITY_DATE,  "+
            "CONCAT(A.TITLE,'  ',A.APPLICANT_NAME) AS NAME,A.ADDRESS1,A.ADDRESS2,A.ADDRESS3,A.CITY, A.AMOUNT,  "+
            "A.DEPOSIT_PERIOD,A.INTEREST_RATE,A.BROKER_CODE,A.PARTY_CODE,  "+
            "CASE WHEN A.DEPOSITER_CATEGORY = 1 THEN 'E'  "+
            "WHEN A.DEPOSITER_CATEGORY = 2 THEN 'S'  "+
            "WHEN A.DEPOSITER_CATEGORY = 3 THEN 'C'  "+
            "WHEN A.DEPOSITER_CATEGORY = 4 THEN 'D'  "+
            "WHEN A.DEPOSITER_CATEGORY = 5 THEN 'I' ELSE '' END DEPOSITER_CATEGORY "+
            "FROM D_FD_DEPOSIT_MASTER A,D_FD_SCHEME_MASTER B "+
            "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.APPROVED= 1 AND A.CANCELLED = 0  "+
            "AND A.MATURITY_DATE >= '" + EITLERPGLOBAL.formatDateDB(txtAsonDate.getText().trim()) + "' AND (PM_DATE > '" + EITLERPGLOBAL.formatDateDB(txtAsonDate.getText().trim()) + "' OR PM_DATE='' OR PM_DATE='0000-00-00') " +
            "AND A.RECEIPT_DATE <= '" + EITLERPGLOBAL.formatDateDB(txtAsonDate.getText().trim()) +"' " + 
            "AND A.SCHEME_ID = B.SCHEME_ID AND B.SCHEME_TYPE = '"+ ReceiptType+"' ORDER BY A.PARTY_CODE,A.RECEIPT_NO";
            
            System.out.println(strSQL);
            
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            int Counter = 0;
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    if(UtilFunctions.getString(rsTmp,"RECEIPT_NO","").equals("000570")) {
                        boolean halt = true;
                    }
                    objRow=objReportData.newRow();
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("RECEIPT_NO",UtilFunctions.getString(rsTmp,"RECEIPT_NO",""));
                    objRow.setValue("LEGACY_NO",UtilFunctions.getString(rsTmp,"LEGACY_NO",""));
                    objRow.setValue("RECEIPT_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"RECEIPT_DATE","0000-00-00")));
                    objRow.setValue("EFFECTIVE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"EFFECTIVE_DATE","0000-00-00")));
                    objRow.setValue("MATURITY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATURITY_DATE","0000-00-00")));
                    objRow.setValue("APPLICANT_NAME",UtilFunctions.getString(rsTmp,"NAME",""));
                    objRow.setValue("ADDRESS1",UtilFunctions.getString(rsTmp,"ADDRESS1",""));
                    objRow.setValue("ADDRESS2",UtilFunctions.getString(rsTmp,"ADDRESS2",""));
                    objRow.setValue("ADDRESS3",UtilFunctions.getString(rsTmp,"ADDRESS3",""));
                    objRow.setValue("CITY",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CITY","")));
                    objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                    objRow.setValue("DEPOSIT_PERIOD",UtilFunctions.getString(rsTmp,"DEPOSIT_PERIOD",""));
                    objRow.setValue("INTEREST_RATE",UtilFunctions.getString(rsTmp,"INTEREST_RATE",""));
                    objRow.setValue("DEPOSITER_STATUS",UtilFunctions.getString(rsTmp,"DEPOSITER_CATEGORY",""));
                    objRow.setValue("BROKER_CODE",UtilFunctions.getString(rsTmp,"BROKER_CODE",""));
                    objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            
            String Scheme_type="";
            if(ReceiptType == 1) {
                Scheme_type="FD";
            }
            if(ReceiptType == 2) {
                Scheme_type="LD";
            }
            if(ReceiptType == 3) {
                Scheme_type="CD";
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("COMPANY_NAME",Integer.toString(Comp_ID));
            Parameters.put("ASON_DATE",txtAsonDate.getText().trim());
            Parameters.put("SCHEME_TYPE",Scheme_type);
            
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptDepositRegister.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void GenerateCombo() {
        
        //--- Generate Type Combo ------//
        cmbReceiptTypeModel=new EITLComboModel();
        cmbReceiptType.removeAllItems();
        cmbReceiptType.setModel(cmbReceiptTypeModel);
        
        ComboData aData=new ComboData();
        aData.Code=0;
        aData.Text="";
        cmbReceiptTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="FD";
        cmbReceiptTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="LD";
        cmbReceiptTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="CD";
        cmbReceiptTypeModel.addElement(aData);
        
    }
    
    private void GenerateSummary() {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("NUM_EMPLOYEE");
            objReportData.AddColumn("NUM_SHARE_HOLDERS");
            objReportData.AddColumn("NUM_COMPANY");
            objReportData.AddColumn("NUM_INDIVIDUAL");
            objReportData.AddColumn("NUM_DIRECTOR");
            objReportData.AddColumn("NUM_RECEIPT");
            objReportData.AddColumn("SUM_EMPLOYEE");
            objReportData.AddColumn("SUM_SHARE_HOLDERS");
            objReportData.AddColumn("SUM_COMPANY");
            objReportData.AddColumn("SUM_INDIVIDUAL");
            objReportData.AddColumn("SUM_DIRECTOR");
            objReportData.AddColumn("SUM_AMOUNT");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("NUM_EMPLOYEE","");
            objOpeningRow.setValue("NUM_SHARE_HOLDERS","");
            objOpeningRow.setValue("NUM_COMPANY","");
            objOpeningRow.setValue("NUM_INDIVIDUAL","");
            objOpeningRow.setValue("NUM_DIRECTOR","");
            objOpeningRow.setValue("NUM_RECEIPT","");
            objOpeningRow.setValue("SUM_EMPLOYEE","");
            objOpeningRow.setValue("SUM_SHARE_HOLDERS","");
            objOpeningRow.setValue("SUM_COMPANY","");
            objOpeningRow.setValue("SUM_INDIVIDUAL","");
            objOpeningRow.setValue("SUM_DIRECTOR","");
            objOpeningRow.setValue("SUM_AMOUNT","");
            
            double NUM_EMPLOYEE=0.0,NUM_SHARE_HOLDERS=0.0,NUM_COMPANY=0.0,NUM_INDIVIDUAL=0.0,NUM_DIRECTOR=0.0,NUM_RECEIPT=0.0;
            double SUM_EMPLOYEE=0.0,SUM_SHARE_HOLDERS=0.0,SUM_COMPANY=0.0,SUM_INDIVIDUAL=0.0,SUM_DIRECTOR=0.0;
            double SUM_AMOUNT=0.0;
            
            int ReceiptType = cmbReceiptType.getSelectedIndex();
            String strSQL = "SELECT  "+
            "IF (A.DEPOSITER_CATEGORY = 1,COUNT(A.DEPOSITER_CATEGORY),0) AS NUM_EMPLOYEE, "+
            "IF (A.DEPOSITER_CATEGORY = 2,COUNT(A.DEPOSITER_CATEGORY),0) AS NUM_SHARE_HOLDERS,  "+
            "IF (A.DEPOSITER_CATEGORY = 3,COUNT(A.DEPOSITER_CATEGORY),0) AS NUM_COMPANY,  "+
            "IF (A.DEPOSITER_CATEGORY = 4,COUNT(A.DEPOSITER_CATEGORY),0) AS NUM_DIRECTOR, "+
            "IF (A.DEPOSITER_CATEGORY = 5,COUNT(A.DEPOSITER_CATEGORY),0) AS NUM_INDIVIDUAL, "+
            "COUNT(A.DEPOSITER_CATEGORY) AS NUM_RECEIPT, "+
            "IF (A.DEPOSITER_CATEGORY = 1,SUM(A.AMOUNT),0) AS SUM_EMPLOYEE, "+
            "IF (A.DEPOSITER_CATEGORY = 2,SUM(A.AMOUNT),0) AS SUM_SHARE_HOLDERS, "+
            "IF (A.DEPOSITER_CATEGORY = 3,SUM(A.AMOUNT),0) AS SUM_COMPANY, "+
            "IF (A.DEPOSITER_CATEGORY = 4,SUM(A.AMOUNT),0) AS SUM_DIRECTOR, "+
            "IF (A.DEPOSITER_CATEGORY = 5,SUM(A.AMOUNT),0) AS SUM_INDIVIDUAL, "+
            "IF (SUM(A.AMOUNT) > 0,SUM(A.AMOUNT),0) AS SUM_AMOUNT "+
            "FROM D_FD_DEPOSIT_MASTER A,D_FD_SCHEME_MASTER B WHERE A.COMPANY_ID = '2' AND A.APPROVED= 1 " +
            "AND A.CANCELLED = 0  AND A.MATURITY_DATE >= '" + EITLERPGLOBAL.formatDateDB(txtAsonDate.getText().trim()) + "' " +
            "AND (PM_DATE > '" + EITLERPGLOBAL.formatDateDB(txtAsonDate.getText().trim()) + "' OR PM_DATE='' OR PM_DATE='0000-00-00') " +
            "AND A.SCHEME_ID = B.SCHEME_ID "+
            "AND B.SCHEME_TYPE = '" + ReceiptType + "' "+
            "AND A.RECEIPT_DATE <= '" + EITLERPGLOBAL.formatDateDB(txtAsonDate.getText().trim()) +"' " + 
            "GROUP BY A.DEPOSITER_CATEGORY ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    
                    NUM_EMPLOYEE += UtilFunctions.getLong(rsTmp,"NUM_EMPLOYEE",0);
                    NUM_SHARE_HOLDERS += UtilFunctions.getLong(rsTmp,"NUM_SHARE_HOLDERS",0);
                    NUM_COMPANY += UtilFunctions.getLong(rsTmp,"NUM_COMPANY",0);
                    NUM_INDIVIDUAL += UtilFunctions.getLong(rsTmp,"NUM_INDIVIDUAL",0);
                    NUM_DIRECTOR += UtilFunctions.getLong(rsTmp,"NUM_DIRECTOR",0);
                    NUM_RECEIPT += UtilFunctions.getLong(rsTmp,"NUM_RECEIPT",0);
                    SUM_EMPLOYEE += UtilFunctions.getLong(rsTmp,"SUM_EMPLOYEE",0);
                    SUM_SHARE_HOLDERS += UtilFunctions.getLong(rsTmp,"SUM_SHARE_HOLDERS",0);
                    SUM_COMPANY += UtilFunctions.getLong(rsTmp,"SUM_COMPANY",0);
                    SUM_INDIVIDUAL += UtilFunctions.getLong(rsTmp,"SUM_INDIVIDUAL",0);
                    SUM_DIRECTOR += UtilFunctions.getLong(rsTmp,"SUM_DIRECTOR",0);
                    SUM_AMOUNT += UtilFunctions.getLong(rsTmp,"SUM_AMOUNT",0);
                    
                    rsTmp.next();
                }
            }
            
            objRow=objReportData.newRow();
            
            objRow.setValue("NUM_EMPLOYEE",Double.toString(NUM_EMPLOYEE));
            objRow.setValue("NUM_SHARE_HOLDERS",Double.toString(NUM_SHARE_HOLDERS));
            objRow.setValue("NUM_COMPANY",Double.toString(NUM_COMPANY));
            objRow.setValue("NUM_INDIVIDUAL",Double.toString(NUM_INDIVIDUAL));
            objRow.setValue("NUM_DIRECTOR",Double.toString(NUM_DIRECTOR));
            objRow.setValue("NUM_RECEIPT",Double.toString(NUM_RECEIPT));
            objRow.setValue("SUM_EMPLOYEE",Double.toString(SUM_EMPLOYEE));
            objRow.setValue("SUM_SHARE_HOLDERS",Double.toString(SUM_SHARE_HOLDERS));
            objRow.setValue("SUM_COMPANY",Double.toString(SUM_COMPANY));
            objRow.setValue("SUM_INDIVIDUAL",Double.toString(SUM_INDIVIDUAL));
            objRow.setValue("SUM_DIRECTOR",Double.toString(SUM_DIRECTOR));
            objRow.setValue("SUM_AMOUNT",Double.toString(SUM_AMOUNT));
            objReportData.AddRow(objRow);
            
            String Scheme_type="";
            if(ReceiptType == 1) {
                Scheme_type="FD";
            }
            if(ReceiptType == 2) {
                Scheme_type="LD";
            }
            if(ReceiptType == 3) {
                Scheme_type="CD";
            }
            
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_ID",Integer.toString(EITLERPGLOBAL.gCompanyID));
            Parameters.put("ASON_DATE",txtAsonDate.getText().trim());
            Parameters.put("SCHEME_TYPE",Scheme_type);
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptDepositRegisterSummary.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    private boolean Validate() {
        //Form level validations
        if(txtAsonDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtAsonDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date in DD/MM/YYYY format.");
            return false;
        }
        
        return true;
    }
}

