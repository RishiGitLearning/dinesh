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

public class frmRptUnclaimedReceipts extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
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
        jLabel3 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
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
        jLabel6.setText("UNCLAIMED RECEIPT REPORT");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 230, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel3.setText("As On Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(29, 69, 87, 19);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(120, 70, 90, 20);

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
        cmdPreview.setBounds(70, 180, 130, 25);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Receipt Type :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 100, 90, 15);

        getContentPane().add(cmbReceiptType);
        cmbReceiptType.setBounds(120, 100, 90, 24);

        cmdPreviewSummary.setText("Preview Summary");
        cmdPreviewSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewSummaryActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreviewSummary);
        cmdPreviewSummary.setBounds(230, 180, 160, 25);

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("RECEIPT_NO");
            objReportData.AddColumn("LEGACY_NO");
            objReportData.AddColumn("ISSUE_DATE");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("APPLICANT_NAME");
            objReportData.AddColumn("AMOUNT");
            objReportData.AddColumn("PERIOD");
            objReportData.AddColumn("INTEREST");
            objReportData.AddColumn("RECEIPT_DATE");
            objReportData.AddColumn("DUE_DATE");
            objReportData.AddColumn("OLD_RECEIPT_TYPE");
            objReportData.AddColumn("OLD_RECEIPT_NO");
            objReportData.AddColumn("STATUS");
            objReportData.AddColumn("BROKER_CODE");
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("RECEIPT_NO","");
            objOpeningRow.setValue("LEGACY_NO","");
            objOpeningRow.setValue("ISSUE_DATE","0000-00-00");
            objOpeningRow.setValue("PARTY_CODE","");
            objOpeningRow.setValue("APPLICANT_NAME","");
            objOpeningRow.setValue("AMOUNT","");
            objOpeningRow.setValue("PERIOD","");
            objOpeningRow.setValue("INTEREST","");
            objOpeningRow.setValue("RECEIPT_DATE","0000-00-00");
            objOpeningRow.setValue("DUE_DATE","0000-00-00");
            objOpeningRow.setValue("OLD_RECEIPT_TYPE","");
            objOpeningRow.setValue("OLD_RECEIPT_NO","");
            objOpeningRow.setValue("STATUS","");
            objOpeningRow.setValue("BROKER_CODE","");
            
            int ReceiptType = cmbReceiptType.getSelectedIndex() + 1;
            String strSQL = "";
            strSQL="SELECT DM.RECEIPT_NO,DM.LEGACY_NO,DM.REALIZATION_DATE,DM.PARTY_CODE,DM.APPLICANT_NAME,DM.AMOUNT,DM.DEPOSIT_PERIOD, " +
            "DM.INTEREST_RATE,DM.RECEIPT_DATE,DM.MATURITY_DATE,IF (DM.OLD_RECEIPT_NO <> '',CONCAT(CASE WHEN SM.SCHEME_TYPE=1 THEN 'FD' WHEN SM.SCHEME_TYPE=1 THEN 'LD' " +
            "WHEN SM.SCHEME_TYPE = 3 THEN 'CD' ELSE '' END,DM.OLD_RECEIPT_NO),'') AS OLD_RECEIPT_NO, " +
            "CASE WHEN DM.DEPOSITER_CATEGORY = 1 THEN 'E' WHEN DM.DEPOSITER_CATEGORY = 2 THEN 'S' " +
            "WHEN DM.DEPOSITER_CATEGORY = 3 THEN 'C'  WHEN DM.DEPOSITER_CATEGORY = 4 THEN 'D' " +
            "WHEN DM.DEPOSITER_CATEGORY = 5 THEN 'I' ELSE '' END DEPOSITER_CATEGORY, " +
            "DM.BROKER_CODE FROM D_FD_DEPOSIT_MASTER DM, D_FD_SCHEME_MASTER SM " +
            "WHERE DM.MATURITY_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"' AND DM.DEPOSIT_STATUS=0 AND DM.SCHEME_ID=SM.SCHEME_ID AND " +
            "SM.SCHEME_TYPE="+ReceiptType+" " +
            "AND DM.APPROVED=1 AND DM.REJECTED = 0 AND DM.CANCELLED=0 ORDER BY DM.RECEIPT_NO"; 
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("RECEIPT_NO",UtilFunctions.getString(rsTmp,"RECEIPT_NO",""));
                    objRow.setValue("LEGACY_NO",UtilFunctions.getString(rsTmp,"LEGACY_NO",""));
                    objRow.setValue("ISSUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"REALIZATION_DATE","0000-00-00")));
                    objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                    objRow.setValue("APPLICANT_NAME",UtilFunctions.getString(rsTmp,"APPLICANT_NAME",""));
                    objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                    objRow.setValue("PERIOD",UtilFunctions.getString(rsTmp,"DEPOSIT_PERIOD",""));
                    objRow.setValue("INTEREST",UtilFunctions.getString(rsTmp,"INTEREST_RATE",""));
                    objRow.setValue("RECEIPT_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"RECEIPT_DATE","0000-00-00")));
                    objRow.setValue("DUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MATURITY_DATE","0000-00-00")));
                    //objRow.setValue("OLD_RECEIPT_TYPE",UtilFunctions.getString(rsTmp,"O_RECEIPT_TYPE",""));
                    objRow.setValue("OLD_RECEIPT_NO",UtilFunctions.getString(rsTmp,"OLD_RECEIPT_NO",""));
                    objRow.setValue("DEPOSITER_CETGORY",UtilFunctions.getString(rsTmp,"DEPOSITER_CETGORY",""));
                    objRow.setValue("BROKER_CODE",UtilFunctions.getString(rsTmp,"BROKER_CODE",""));
                    
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            HashMap Parameters=new HashMap();
            
            //Parameters.put("FROM_DATE",txtFromDate.getText().trim());
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
            
            
            Parameters.put("TO_DATE",txtToDate.getText().trim());
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            Parameters.put("SCHEME_TYPE", Scheme_type);
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptUnclaimedReceipts.rpt",Parameters,objReportData);
            
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
        
        
        cmbReportTypeModel=new EITLComboModel();
        //cmbReportType.removeAllItems();
        //  cmbReportType.setModel(cmbReportTypeModel);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="FRESH";
        cmbReportTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="RENEWAL";
        cmbReportTypeModel.addElement(aData);
        
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
            objReportData.AddColumn("SUM_EMPLOYEE");
            objReportData.AddColumn("SUM_SHARE_HOLDERS");
            objReportData.AddColumn("SUM_COMPANY");
            objReportData.AddColumn("SUM_INDIVIDUAL");
            objReportData.AddColumn("SUM_DIRECTOR");
            objReportData.AddColumn("MONTHLY");
            objReportData.AddColumn("QUARTERLY");
            objReportData.AddColumn("HALF_YEARLY");
            objReportData.AddColumn("SUM_MONTHLY");
            objReportData.AddColumn("SUM_QUARTERLY");
            objReportData.AddColumn("SUM_HALF_YEARLY");
            objReportData.AddColumn("CNT_DEPOSITER_STATUS");
            objReportData.AddColumn("SUM_AMOUNT");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("NUM_EMPLOYEE","");
            objOpeningRow.setValue("NUM_SHARE_HOLDERS","");
            objOpeningRow.setValue("NUM_COMPANY","");
            objOpeningRow.setValue("NUM_INDIVIDUAL","");
            objOpeningRow.setValue("NUM_DIRECTOR","");
            objOpeningRow.setValue("SUM_EMPLOYEE","");
            objOpeningRow.setValue("SUM_SHARE_HOLDERS","");
            objOpeningRow.setValue("SUM_COMPANY","");
            objOpeningRow.setValue("SUM_INDIVIDUAL","");
            objOpeningRow.setValue("SUM_DIRECTOR","");
            objOpeningRow.setValue("MONTHLY","");
            objOpeningRow.setValue("QUARTERLY","");
            objOpeningRow.setValue("HALF_YEARLY","");
            objOpeningRow.setValue("SUM_MONTHLY","");
            objOpeningRow.setValue("SUM_QUARTERLY","");
            objOpeningRow.setValue("SUM_HALF_YEARLY","");
            objOpeningRow.setValue("CNT_DEPOSITER_STATUS","");
            objOpeningRow.setValue("SUM_AMOUNT","");
            
            
            
            int ReceiptType = cmbReceiptType.getSelectedIndex() + 1;
            String strSQL = "";
            
            strSQL= "SELECT IF (DEPOSITMST.DEPOSITER_STATUS = 1,COUNT(DEPOSITMST.DEPOSITER_STATUS),0) AS NUM_EMPLOYEE, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 2,COUNT(DEPOSITMST.DEPOSITER_STATUS),0) AS NUM_SHARE_HOLDERS, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 3,COUNT(DEPOSITMST.DEPOSITER_STATUS),0) AS NUM_COMPANY, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 4,COUNT(DEPOSITMST.DEPOSITER_STATUS),0) AS NUM_INDIVIDUAL, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 5,COUNT(DEPOSITMST.DEPOSITER_STATUS),0) AS NUM_DIRECTOR, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 1,SUM(DEPOSITMST.AMOUNT),0) AS SUM_EMPLOYEE, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 2,SUM(DEPOSITMST.AMOUNT),0) AS SUM_SHARE_HOLDERS, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 3,SUM(DEPOSITMST.AMOUNT),0) AS SUM_COMPANY, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 4,SUM(DEPOSITMST.AMOUNT),0) AS SUM_INDIVIDUAL, "+
            "IF (DEPOSITMST.DEPOSITER_STATUS = 5,SUM(DEPOSITMST.AMOUNT),0) AS SUM_DIRECTOR, "+
            "IF (SCHEMEMST.INTEREST_CALCULATION_PERIOD = 1, COUNT(SCHEMEMST.INTEREST_CALCULATION_PERIOD),0) AS MONTHLY, "+
            "IF (SCHEMEMST.INTEREST_CALCULATION_PERIOD = 4, COUNT(SCHEMEMST.INTEREST_CALCULATION_PERIOD),0) AS QUARTERLY, "+
            "IF (SCHEMEMST.INTEREST_CALCULATION_PERIOD = 6, COUNT(SCHEMEMST.INTEREST_CALCULATION_PERIOD),0) AS HALF_YEARLY, "+
            "IF (SCHEMEMST.INTEREST_CALCULATION_PERIOD = 1, SUM(DEPOSITMST.AMOUNT),0) AS SUM_MONTHLY, "+
            "IF (SCHEMEMST.INTEREST_CALCULATION_PERIOD = 4, SUM(DEPOSITMST.AMOUNT),0) AS SUM_QUARTERLY, "+
            "IF (SCHEMEMST.INTEREST_CALCULATION_PERIOD = 6, SUM(DEPOSITMST.AMOUNT),0) AS SUM_HALF_YEARLY,"+
            "COUNT(DEPOSITMST.DEPOSITER_STATUS) AS CNT_DEPOSITER_STATUS, "+
            "IF (SUM(DEPOSITMST.AMOUNT) > 0,SUM(DEPOSITMST.AMOUNT),0) AS SUM_AMOUNT "+
            "FROM D_FD_DEPOSIT_MASTER DEPOSITMST, D_FD_SCHEME_MASTER SCHEMEMST "+
            "WHERE DEPOSITMST.COMPANY_ID =" + EITLERPGLOBAL.gCompanyID + " AND DEPOSITMST.DEPOSIT_STATUS=0 "+
            "AND DEPOSITMST.REJECTED = 0 "+
            "AND DEPOSITMST.CANCELLED=0  AND DEPOSITMST.APPROVED=1 "+
            "AND DEPOSITMST.MATURITY_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) +"' "+
            "AND DEPOSITMST.COMPANY_ID = SCHEMEMST.COMPANY_ID "+
            "AND DEPOSITMST.SCHEME_ID = SCHEMEMST.SCHEME_ID AND SCHEMEMST.SCHEME_TYPE= '" + ReceiptType + "' " +
            "GROUP BY DEPOSITMST.DEPOSITER_STATUS " ;
            
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            
            int Counter = 0;
            
            double NUM_EMPLOYEE=0.0,NUM_SHARE_HOLDERS=0.0,NUM_COMPANY=0.0,NUM_INDIVIDUAL=0.0,NUM_DIRECTOR=0.0;
            double SUM_EMPLOYEE=0.0,SUM_SHARE_HOLDERS=0.0,SUM_COMPANY=0.0,SUM_INDIVIDUAL=0.0,SUM_DIRECTOR=0.0;
            double MONTHLY=0.0,QUARTERLY=0.0,HALF_YEARLY=0.0,SUM_MONTHLY=0.0,SUM_QUARTERLY=0.0,SUM_HALF_YEARLY=0.0;
            double CNT_DEPOSITER_STATUS=0.0,SUM_AMOUNT=0.0;
            
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    
                    NUM_EMPLOYEE += UtilFunctions.getLong(rsTmp,"NUM_EMPLOYEE",0);
                    NUM_SHARE_HOLDERS += UtilFunctions.getLong(rsTmp,"NUM_SHARE_HOLDERS",0);
                    NUM_COMPANY += UtilFunctions.getLong(rsTmp,"NUM_COMPANY",0);
                    NUM_INDIVIDUAL += UtilFunctions.getLong(rsTmp,"NUM_INDIVIDUAL",0);
                    NUM_DIRECTOR += UtilFunctions.getLong(rsTmp,"NUM_DIRECTOR",0);
                    SUM_EMPLOYEE += UtilFunctions.getLong(rsTmp,"SUM_EMPLOYEE",0);
                    SUM_SHARE_HOLDERS += UtilFunctions.getLong(rsTmp,"SUM_SHARE_HOLDERS",0);
                    SUM_COMPANY += UtilFunctions.getLong(rsTmp,"SUM_COMPANY",0);
                    SUM_INDIVIDUAL += UtilFunctions.getLong(rsTmp,"SUM_INDIVIDUAL",0);
                    SUM_DIRECTOR += UtilFunctions.getLong(rsTmp,"SUM_DIRECTOR",0);
                    MONTHLY += UtilFunctions.getLong(rsTmp,"MONTHLY",0);
                    QUARTERLY += UtilFunctions.getLong(rsTmp,"QUARTERLY",0);
                    HALF_YEARLY += UtilFunctions.getLong(rsTmp,"HALF_YEARLY",0);
                    SUM_MONTHLY += UtilFunctions.getLong(rsTmp,"SUM_MONTHLY",0);
                    SUM_QUARTERLY +=UtilFunctions.getLong(rsTmp,"SUM_QUARTERLY",0);
                    SUM_HALF_YEARLY += UtilFunctions.getLong(rsTmp,"SUM_HALF_YEARLY",0);
                    CNT_DEPOSITER_STATUS += UtilFunctions.getLong(rsTmp,"CNT_DEPOSITER_STATUS",0);
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
            objRow.setValue("SUM_EMPLOYEE",Double.toString(SUM_EMPLOYEE));
            objRow.setValue("SUM_SHARE_HOLDERS",Double.toString(SUM_SHARE_HOLDERS));
            objRow.setValue("SUM_COMPANY",Double.toString(SUM_COMPANY));
            objRow.setValue("SUM_INDIVIDUAL",Double.toString(SUM_INDIVIDUAL));
            objRow.setValue("SUM_DIRECTOR",Double.toString(SUM_DIRECTOR));
            objRow.setValue("MONTHLY",Double.toString(MONTHLY));
            objRow.setValue("QUARTERLY",Double.toString(QUARTERLY));
            objRow.setValue("HALF_YEARLY",Double.toString(HALF_YEARLY));
            objRow.setValue("SUM_MONTHLY",Double.toString(SUM_MONTHLY));
            objRow.setValue("SUM_QUARTERLY",Double.toString(SUM_QUARTERLY));
            objRow.setValue("SUM_HALF_YEARLY",Double.toString(SUM_HALF_YEARLY));
            objRow.setValue("CNT_DEPOSITER_STATUS",Double.toString(CNT_DEPOSITER_STATUS));
            objRow.setValue("SUM_AMOUNT",Double.toString(SUM_AMOUNT));
            
            objReportData.AddRow(objRow);
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            
            
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
            Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("TO_DATE",txtToDate.getText().trim());
            Parameters.put("SCHEME_TYPE",Scheme_type);
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptUnclaimedReceiptSummary.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private boolean Validate() {
        //Form level validations
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtToDate.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Invalid Date in DD/MM/YYYY format.");
            return false;
        }
        
        return true;
    }
    
}
