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

public class frmRptCFormIssueStatement extends javax.swing.JApplet {
    
    private EITLComboModel cmbMonthModel;
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
        jLabel3 = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdPreview = new javax.swing.JButton();
        cmbMonth = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("C - FORM ISSUE STATEMENT");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 230, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Month :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 70, 90, 20);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Year :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(220, 70, 60, 20);

        txtYear.setColumns(10);
        getContentPane().add(txtYear);
        txtYear.setBounds(285, 70, 90, 20);

        jLabel1.setText("Period");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(14, 46, 90, 15);

        cmdPreview.setText("Preview ");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(240, 130, 130, 25);

        getContentPane().add(cmbMonth);
        cmbMonth.setBounds(120, 70, 90, 24);

    }//GEN-END:initComponents
    
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
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("COMPANY_ID");
            objReportData.AddColumn("VOUCHER_NO");
            objReportData.AddColumn("LEGACY_NO");
            objReportData.AddColumn("VOUCHER_DATE");
            objReportData.AddColumn("BOOK_CODE");
            objReportData.AddColumn("SUPP_NAME");
            objReportData.AddColumn("MAIN_ACCOUNT_CODE");
            objReportData.AddColumn("SUB_ACCOUNT_CODE");
            objReportData.AddColumn("AMOUNT");
            objReportData.AddColumn("GRN_NO");
            objReportData.AddColumn("GRN_DATE");
            objReportData.AddColumn("INVOICE_NO");
            objReportData.AddColumn("INVOICE_DATE");
            objReportData.AddColumn("PO_NO");
            objReportData.AddColumn("PO_DATE");
            objReportData.AddColumn("ITEM_ID");
            objReportData.AddColumn("ITEM_DESC");
            objReportData.AddColumn("TOTAL_AMOUNT");
            objReportData.AddColumn("NET_AMOUNT");
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("COMPANY_ID","");
            objOpeningRow.setValue("VOUCHER_NO","");
            objOpeningRow.setValue("LEGACY_NO","");
            objOpeningRow.setValue("VOUCHER_DATE","0000-00-00");
            objOpeningRow.setValue("BOOK_CODE","");
            objOpeningRow.setValue("SUPP_NAME","");
            objOpeningRow.setValue("MAIN_ACCOUNT_CODE","");
            objOpeningRow.setValue("SUB_ACCOUNT_CODE","");
            objOpeningRow.setValue("AMOUNT","");
            objOpeningRow.setValue("GRN_NO","");
            objOpeningRow.setValue("GRN_DATE","0000-00-00");
            objOpeningRow.setValue("INVOICE_NO","");
            objOpeningRow.setValue("INVOICE_DATE","0000-00-00");
            objOpeningRow.setValue("PO_NO","");
            objOpeningRow.setValue("PO_DATE","0000-00-00");
            objOpeningRow.setValue("ITEM_ID","");
            objOpeningRow.setValue("ITEM_DESC","");
            objOpeningRow.setValue("TOTAL_AMOUNT","");
            objOpeningRow.setValue("NET_AMOUNT","");
            
            int nMonth = cmbMonth.getSelectedIndex() + 1;
            
            String strSQL = "";
            
            /*strSQL= "SELECT VOUHEAD.COMPANY_ID,VOUHEAD.VOUCHER_NO,VOUHEAD.VOUCHER_DATE, "+
            "VOUHEAD.BOOK_CODE, VOUHEAD.LEGACY_NO, VOUDTL.MAIN_ACCOUNT_CODE, "+
            "VOUDTL.SUB_ACCOUNT_CODE, FINMST.PARTY_NAME, VOUDTL.INVOICE_NO,VOUDTL.INVOICE_DATE, "+
            "VOUDTL.AMOUNT,VOUDTL.GRN_NO,VOUDTL.GRN_DATE,  VOUDTL.PO_NO "+
            "FROM FINANCE.D_FIN_VOUCHER_HEADER VOUHEAD, FINANCE.D_FIN_VOUCHER_DETAIL VOUDTL, "+
            "FINANCE.D_FIN_VOUCHER_DETAIL VOUDTLG, D_FIN_PARTY_MASTER FINMST "+
            "WHERE VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0 "+ //VOUHEAD.COMPANY_ID="+ EITLERPGLOBAL.gCompanyID +" AND
            "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO "+
            "AND VOUDTL.SUB_ACCOUNT_CODE <> '' "+
            "AND MONTH(VOUHEAD.VOUCHER_DATE) = "+ nMonth + " AND YEAR(VOUHEAD.VOUCHER_DATE) = "+ txtYear.getText().trim() + " " +
            "AND VOUHEAD.COMPANY_ID = VOUDTLG.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTLG.VOUCHER_NO "+
            "AND (VOUDTLG.MAIN_ACCOUNT_CODE LIKE '%.21' OR VOUDTLG.MAIN_ACCOUNT_CODE LIKE '%.99') AND VOUDTL.VOUCHER_NO=VOUDTLG.VOUCHER_NO "+
            "AND VOUDTL.SUB_ACCOUNT_CODE = FINMST.PARTY_CODE "+ //VOUHEAD.COMPANY_ID = SUPPMST.COMPANY_ID AND 
            "ORDER BY VOUHEAD.VOUCHER_NO "; */
            
            strSQL = "SELECT DISTINCT VOUHEAD.COMPANY_ID,VOUHEAD.VOUCHER_NO,VOUHEAD.VOUCHER_DATE, VOUHEAD.BOOK_CODE, " +
            "VOUHEAD.LEGACY_NO, VOUDTL.MAIN_ACCOUNT_CODE, VOUDTL.SUB_ACCOUNT_CODE, " +
            "VOUDTL.INVOICE_NO,VOUDTL.INVOICE_DATE, VOUDTL.AMOUNT,VOUDTL.GRN_NO,VOUDTL.GRN_DATE,  VOUDTL.PO_NO " +
            "FROM FINANCE.D_FIN_VOUCHER_HEADER VOUHEAD, FINANCE.D_FIN_VOUCHER_DETAIL VOUDTL, " +
            "FINANCE.D_FIN_VOUCHER_DETAIL VOUDTLG WHERE VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0 " +
            "AND MONTH(VOUHEAD.VOUCHER_DATE)="+ nMonth + " AND YEAR(VOUHEAD.VOUCHER_DATE)="+ txtYear.getText().trim() + " " +
            "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO " +
            "AND VOUHEAD.COMPANY_ID = VOUDTLG.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTLG.VOUCHER_NO " +
            "AND VOUDTL.COMPANY_ID=VOUDTLG.COMPANY_ID AND VOUDTL.VOUCHER_NO=VOUDTLG.VOUCHER_NO " +
            "AND VOUDTL.SUB_ACCOUNT_CODE <> '' " +
            "AND (VOUDTLG.MAIN_ACCOUNT_CODE LIKE '%.21' OR VOUDTLG.MAIN_ACCOUNT_CODE LIKE '%.99') " +
            "ORDER BY VOUHEAD.VOUCHER_NO";
            
            //System.out.println("sql="+strSQL);
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("COMPANY_ID",UtilFunctions.getString(rsTmp,"COMPANY_ID",""));
                    objRow.setValue("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objRow.setValue("LEGACY_NO",UtilFunctions.getString(rsTmp,"LEGACY_NO",""));
                    objRow.setValue("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                    objRow.setValue("BOOK_CODE",UtilFunctions.getString(rsTmp,"BOOK_CODE",""));                    
                    objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                    String MainCode = UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                    String SubCode = UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                    objRow.setValue("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE",""));
                    objRow.setValue("SUPP_NAME",clsAccount.getAccountName(MainCode,SubCode));
                    objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                    objRow.setValue("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objRow.setValue("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objRow.setValue("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objRow.setValue("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objRow.setValue("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objRow.setValue("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objRow.setValue("ITEM_ID",UtilFunctions.getString(rsTmp,"ITEM_ID",""));
                    objRow.setValue("ITEM_DESC",UtilFunctions.getString(rsTmp,"ITEM_DESC",""));
                    objRow.setValue("TOTAL_AMOUNT",UtilFunctions.getString(rsTmp,"TOTAL_AMOUNT",""));
                    objRow.setValue("NET_AMOUNT",UtilFunctions.getString(rsTmp,"NET_AMOUNT",""));
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            String Month_Year = cmbMonth.getSelectedItem().toString().trim() + " " + txtYear.getText().trim();
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("MONTH_YEAR",Month_Year);
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptCFormIssueStatement.rpt",Parameters,objReportData);
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void GenerateCombo() {
        
        //--- Generate Type Combo ------//
        ComboData aData=new ComboData();
        
        cmbMonthModel=new EITLComboModel();
        cmbMonth.removeAllItems();
        cmbMonth.setModel(cmbMonthModel);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="January";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="February";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="March";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=4;
        aData.Text="April";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=5;
        aData.Text="May";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=6;
        aData.Text="June";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=7;
        aData.Text="July";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=8;
        aData.Text="August";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=9;
        aData.Text="September";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=10;
        aData.Text="October";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=11;
        aData.Text="November";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=12;
        aData.Text="December";
        cmbMonthModel.addElement(aData);
        
    }
    
    private boolean Validate() {
        //Form level validations
        if(txtYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Year");
            return false;
        } else if(!EITLERPGLOBAL.IsNumber(txtYear.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Invalid Year in YYYY format.");
            return false;
        } else if (txtYear.getText().trim().length() != 4) {
            JOptionPane.showMessageDialog(null,"Invalid Year in YYYY format.");
            return false;
        }
        
        return true;
    }
    
}
