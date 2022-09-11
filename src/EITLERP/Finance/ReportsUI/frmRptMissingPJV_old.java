/*
 * frmVoucherPrinting.java
 *
 * Created on January 20, 2008, 4:00 PM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import TReportWriter.*;
import TReportWriter.SimpleDataProvider.*;
import java.net.*;
import java.sql.*;
import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Finance.Config.*;
import EITLERP.Finance.ReportsUI.*;


public class frmRptMissingPJV_old extends javax.swing.JApplet {
    
    private EITLComboModel cmbCompanyTypeModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    private int SelCompanyID=0 ;
    /** Initializes the applet frmVoucherPrinting */
    public void init() {
        setSize(490,300);
        initComponents();
        Bar.setVisible(false);
        lblBar.setVisible(false);
        
        GenerateCombo();
        
        //        cmbCompanyType.requestFocus();
        //        SelCompanyID=EITLERPGLOBAL.getComboCode(cmbCompanyType);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        cmbCompanyType = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        opgGeneral = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton2 = new javax.swing.JRadioButton();
        opgRM = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        lblBar = new javax.swing.JLabel();
        Bar = new javax.swing.JProgressBar();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("MISSING PJV ");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(-2, 1, 666, 25);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Date From :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(30, 75, 80, 20);

        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(115, 75, 110, 20);

        jLabel5.setText("To");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(235, 75, 20, 20);

        getContentPane().add(txtToDate);
        txtToDate.setBounds(265, 75, 110, 20);

        cmdPrint.setText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPrint);
        cmdPrint.setBounds(140, 220, 80, 25);

        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExit);
        cmdExit.setBounds(230, 220, 80, 25);

        cmbCompanyType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCompanyTypeItemStateChanged(evt);
            }
        });

        getContentPane().add(cmbCompanyType);
        cmbCompanyType.setBounds(115, 40, 350, 20);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Company : ");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(30, 40, 80, 20);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        opgGeneral.setSelected(true);
        opgGeneral.setText("General");
        buttonGroup1.add(opgGeneral);
        jPanel1.add(opgGeneral);
        opgGeneral.setBounds(5, 5, 100, 23);

        jPanel2.setLayout(null);

        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jRadioButton2.setText("All");
        jPanel2.add(jRadioButton2);
        jRadioButton2.setBounds(5, 5, 50, 23);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(87, 185, 300, 30);

        opgRM.setText("Raw Material");
        buttonGroup1.add(opgRM);
        jPanel1.add(opgRM);
        opgRM.setBounds(115, 4, 140, 23);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(115, 110, 260, 30);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("GRN Type : ");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(30, 110, 80, 20);

        lblBar.setText("...");
        getContentPane().add(lblBar);
        lblBar.setBounds(40, 155, 430, 15);

        getContentPane().add(Bar);
        Bar.setBounds(40, 180, 430, 14);

    }//GEN-END:initComponents
    
    private void cmbCompanyTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCompanyTypeItemStateChanged
        // TODO add your handling code here:
        // SelCompanyID=EITLERPGLOBAL.getComboCode(cmbCompanyType);
    }//GEN-LAST:event_cmbCompanyTypeItemStateChanged
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        try {
            ((JFrame)getParent().getParent().getParent().getParent()).dispose();
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        GenerateReport();
        
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar Bar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbCompanyType;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JLabel lblBar;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JRadioButton opgGeneral;
    private javax.swing.JRadioButton opgRM;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        new Thread() {
            public void run() {
                
                try {
                    TReportWriter.SimpleDataProvider.TRow objRow;
                    TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
                    String Grn_Type="";
                    String strSQL = "";
                    int Company_ID = EITLERPGLOBAL.getComboCode(cmbCompanyType);
                    String tmpdbURL=clsFinYear.getDBURL(Company_ID,Integer.parseInt(txtFromDate.getText().trim().substring(6)));
                    
                   // System.out.println(tmpdbURL);
                    
                    objReportData.AddColumn("SR_NO");
                    objReportData.AddColumn("GRN_NO");
                    
                    TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
                    objOpeningRow.setValue("SR_NO","");
                    objOpeningRow.setValue("GRN_NO","");
                    
                    
                    if(opgGeneral.isSelected()) {
                        Grn_Type ="1";
                    } else {
                        Grn_Type ="2";
                    }
                    
                    strSQL = "SELECT H.GRN_NO,H.COMPANY_ID FROM D_INV_GRN_HEADER HD_INV_GRN_DETAIL D" +
                    "WHERE H.GRN_DATE >='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim())+"' " +
                    "AND H.GRN_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"' " +
                    "AND H.APPROVED=1 AND H.CANCELLED=0 AND H.PAYMENT_TYPE=0 AND H.GRN_TYPE='"+Grn_Type+"' " +
                    "AND H.COMPANY_ID = '"+Company_ID+"' AND A.GRN_NO=B.GRN_NO " +
                    "AND A.SUPP_ID NOT IN ('888888','999999') AND D.LANDED_RATE>0 ORDER BY H.GRN_NO ";
                    
                   // System.out.println(strSQL);
                    
                    int BarCounter = data.getIntValueFromDB("SELECT COUNT(*) AS TOTAL FROM D_INV_GRN_HEADER H,D_INV_GRN_DETAIL D " +
                    "WHERE H.GRN_DATE >='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim())+"' " +
                    "AND H.GRN_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"' " +
                    "AND H.APPROVED=1 AND H.CANCELLED=0 AND H.PAYMENT_TYPE=0 AND H.GRN_TYPE = '"+Grn_Type+"' " +
                    "AND H.COMPANY_ID='"+Company_ID+"' AND A.GRN_NO=B.GRN_NO " +
                    "AND A.SUPP_ID NOT IN ('888888','999999') AND D.LANDED_RATE>0 ORDER BY H.GRN_NO");
                    
                    Bar.setVisible(true);
                    lblBar.setVisible(true);
                    Bar.setMaximum(BarCounter);
                    Bar.setMinimum(0);
                    Bar.setValue(0);
                    
                    ResultSet rsData=data.getResult(strSQL,tmpdbURL);
                    //System.out.println("data : " +rsData);
                    rsData.first();
                    
                    int Counter = 0;
                    int i = rsData.getRow();
                    int SrNo=0;
                    if(rsData.getRow()>0) {
                        
                        while(!rsData.isAfterLast()) {
                            String GrnNo = UtilFunctions.getString(rsData,"GRN_NO", "");
                            int CompanyID = UtilFunctions.getInt(rsData,"COMPANY_ID", 0);
                            Counter++;
                            Bar.setValue(Counter);
                            lblBar.setText(GrnNo);
                            strSQL = "SELECT A.VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                            "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim())+"' " +
                            "AND A.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"'AND B.GRN_NO='"+GrnNo+"' " +
                            "AND A.COMPANY_ID = '"+CompanyID+"' AND A.CANCELLED=0 ";
                            //System.out.println("sss: "+strSQL);
                            if(!data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                                SrNo++;
                                objRow=objReportData.newRow();
                                objRow.setValue("SR_NO",Integer.toString(SrNo));
                                objRow.setValue("GRN_NO",GrnNo);
                                objReportData.AddRow(objRow);
                            }
                            rsData.next();
                        }
                    }
                    
                    Bar.setVisible(false);
                    lblBar.setText("Completed...");
                    
                    HashMap Parameters=new HashMap();
                    Parameters.put("COMPANY_ID",Integer.toString(EITLERPGLOBAL.gCompanyID));
                    Parameters.put("FROM_DATE",txtFromDate.getText().trim());
                    Parameters.put("TO_DATE",txtToDate.getText().trim());
                    Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
                    
                    if(Company_ID == 2) {
                        Parameters.put("CITY","VADODARA");
                    } else if(Company_ID == 3) {
                        Parameters.put("CITY","ANKLESHWAR");
                    }
                    
                    objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptMissingPJV.rpt",Parameters,objReportData);
                } catch(Exception e) {  }
                
            };
            
        }.start();
    }
    
    private void GenerateCombo() {
        
        //--- Generate Type Combo ------//
        ComboData aData=new ComboData();
        
        cmbCompanyTypeModel=new EITLComboModel();
        cmbCompanyType.removeAllItems();
        cmbCompanyType.setModel(cmbCompanyTypeModel);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="Shri Dinesh Mills Ltd.";
        cmbCompanyTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="Shri Dinesh Mills Ltd. Ank";
        cmbCompanyTypeModel.addElement(aData);
        
        
        
    }
    private boolean Validate() {
        //Form level validations
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date in DD/MM/YYYY format.");
            return false;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter To date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date in DD/MM/YYYY format.");
            return false;
        }
        
        return true;
    }
}