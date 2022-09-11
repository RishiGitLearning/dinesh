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

public class frmRptProductWiseSalesSummary_OLD extends javax.swing.JApplet {
    
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

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("PRODUCT WISE SALES SUMMARY REPORT");
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
        cmdPreview.setBounds(250, 150, 130, 25);

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
    private javax.swing.JButton cmdPreview;
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
            
            objReportData.AddColumn("SALES");
            objReportData.AddColumn("MAIN_ACCOUNT_CODE");
            objReportData.AddColumn("ACCOUNT_NAME");
            objReportData.AddColumn("AMOUNT");
            objReportData.AddColumn("CODE");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            objOpeningRow.setValue("SALES","");
            objOpeningRow.setValue("MAIN_ACCOUNT_CODE","");
            objOpeningRow.setValue("ACCOUNT_NAME","");
            objOpeningRow.setValue("AMOUNT","");
            objOpeningRow.setValue("CODE","");
            
            
            String strSQL = "";
            
            //FOR FELT SALES
            
            strSQL="SELECT VOUDTLEX.MAIN_ACCOUNT_CODE, GL.ACCOUNT_NAME, "+
            "SUM(VOUDTLEX.AMOUNT) AS AMOUNT "+
            "FROM D_FIN_VOUCHER_HEADER VOUHEAD, D_FIN_VOUCHER_DETAIL_EX  VOUDTLEX, D_FIN_GL  GL "+
            "WHERE VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0 "+
            "AND VOUHEAD.COMPANY_ID=VOUDTLEX.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTLEX.VOUCHER_NO "+
            "AND VOUDTLEX.EFFECT = 'C' "+
            "AND (VOUDTLEX.MAIN_ACCOUNT_CODE IN ('301378','427027','450447')) "+
            "AND VOUHEAD.VOUCHER_DATE >= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' "+
            "AND VOUHEAD.VOUCHER_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"' "+
            "AND VOUDTLEX.MAIN_ACCOUNT_CODE = GL.MAIN_ACCOUNT_CODE "+
            "GROUP BY VOUDTLEX.MAIN_ACCOUNT_CODE ";
            
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            String Account_Name="";
            int s1=0;
            int s2=0;
            int s3=0;
            
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("301378") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "FELT - SALE");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210010");
                        objReportData.AddRow(objRow);
                        s1=1;
                    }
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("427027") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "FELT - SALE");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210010");
                        objReportData.AddRow(objRow);
                        s2=1;
                    }
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("450447") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "FELT - SALE");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210010");
                        objReportData.AddRow(objRow);
                        s3=1;
                    }
                    rsTmp.next();
                }
            }
            
            if(s1==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "FELT - SALE");
                objRow.setValue("MAIN_ACCOUNT_CODE","301378");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='301378' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210010");
                objReportData.AddRow(objRow);
                s1=1;
            }
            if(s2==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "FELT - SALE");
                objRow.setValue("MAIN_ACCOUNT_CODE","427027");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='427027' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210010");
                objReportData.AddRow(objRow);
                s2=1;
            }
            if(s3==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "FELT - SALE");
                objRow.setValue("MAIN_ACCOUNT_CODE","450447");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='450447' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210010");
                objReportData.AddRow(objRow);
                s3=1;
            }
            //END FELT SALES
            
            
            //FOR STE SALES
            strSQL="SELECT  VOUDTLEX.MAIN_ACCOUNT_CODE, GL.ACCOUNT_NAME, "+
            "SUM(VOUDTLEX.AMOUNT) AS AMOUNT "+
            "FROM D_FIN_VOUCHER_HEADER VOUHEAD, D_FIN_VOUCHER_DETAIL_EX  VOUDTLEX, D_FIN_GL  GL "+
            "WHERE VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0 "+
            "AND VOUHEAD.COMPANY_ID=VOUDTLEX.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTLEX.VOUCHER_NO  "+
            "AND VOUDTLEX.EFFECT = 'C' "+
            "AND (VOUDTLEX.MAIN_ACCOUNT_CODE IN ('301017','427027','450447')) "+
            "AND VOUHEAD.VOUCHER_DATE >= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' "+
            "AND VOUHEAD.VOUCHER_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"' "+
            "AND VOUDTLEX.MAIN_ACCOUNT_CODE = GL.MAIN_ACCOUNT_CODE "+
            "GROUP BY VOUDTLEX.MAIN_ACCOUNT_CODE ";
            
            
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            Account_Name="";
            s1=0;
            s2=0;
            s3=0;
            
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("301017") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "STE - SALE");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210027");
                        objReportData.AddRow(objRow);
                        s1=1;
                    }
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("427027") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "STE - SALE");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210027");
                        objReportData.AddRow(objRow);
                        s2=1;
                    }
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("450447") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "STE - SALE");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210027");
                        objReportData.AddRow(objRow);
                        s3=1;
                    }
                    rsTmp.next();
                }
            }
            
            if(s1==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "STE - SALE");
                objRow.setValue("MAIN_ACCOUNT_CODE","301017");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='301017' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210027");
                objReportData.AddRow(objRow);
                s1=1;
            }
            if(s2==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "STE - SALE");
                objRow.setValue("MAIN_ACCOUNT_CODE","427027");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='427027' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210027");
                objReportData.AddRow(objRow);
                s2=1;
            }
            if(s3==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "STE - SALE");
                objRow.setValue("MAIN_ACCOUNT_CODE","450447");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='450447' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210027");
                objReportData.AddRow(objRow);
                s3=1;
            }
            
            //END STE SALES
            
            //FOR FIF SALES
            
            strSQL="SELECT  VOUDTLEX.MAIN_ACCOUNT_CODE, GL.ACCOUNT_NAME, "+
            "SUM(VOUDTLEX.AMOUNT) AS AMOUNT "+
            "FROM D_FIN_VOUCHER_HEADER VOUHEAD, D_FIN_VOUCHER_DETAIL_EX  VOUDTLEX, D_FIN_GL  GL "+
            "WHERE VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0 "+
            "AND VOUHEAD.COMPANY_ID=VOUDTLEX.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTLEX.VOUCHER_NO  "+
            "AND VOUDTLEX.EFFECT = 'C' "+
            "AND (VOUDTLEX.MAIN_ACCOUNT_CODE IN ('301347','427027','450447')) "+
            "AND VOUHEAD.VOUCHER_DATE >= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' "+
            "AND VOUHEAD.VOUCHER_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"' "+
            "AND VOUDTLEX.MAIN_ACCOUNT_CODE = GL.MAIN_ACCOUNT_CODE "+
            "GROUP BY VOUDTLEX.MAIN_ACCOUNT_CODE ";
            
            
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            Account_Name="";
            s1=0;
            s2=0;
            s3=0;
            
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("301347") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "FIF - SALES");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210072");
                        objReportData.AddRow(objRow);
                        s1=1;
                    }
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("427027") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "FIF - SALES");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210072");
                        objReportData.AddRow(objRow);
                        s2=1;
                    }
                    if(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").equals("450447") ) {
                        objRow=objReportData.newRow();
                        objRow.setValue("SALES", "FIF - SALES");
                        objRow.setValue("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                        objRow.setValue("ACCOUNT_NAME",UtilFunctions.getString(rsTmp,"ACCOUNT_NAME",""));
                        objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                        objRow.setValue("CODE", "210072");
                        objReportData.AddRow(objRow);
                        s3=1;
                    }
                    rsTmp.next();
                }
            }
            
            if(s1==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "FIF - SALES");
                objRow.setValue("MAIN_ACCOUNT_CODE","301347");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='301347' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210072");
                objReportData.AddRow(objRow);
                s1=1;
            }
            if(s2==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "FIF - SALES");
                objRow.setValue("MAIN_ACCOUNT_CODE","427027");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='427027' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210072");
                objReportData.AddRow(objRow);
                s2=1;
            }
            if(s3==0) {
                objRow=objReportData.newRow();
                objRow.setValue("SALES", "FIF - SALES");
                objRow.setValue("MAIN_ACCOUNT_CODE","450447");
                Account_Name = data.getStringValueFromDB("SELECT  ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='450447' ",FinanceGlobal.FinURL);
                objRow.setValue("ACCOUNT_NAME",Account_Name);
                objRow.setValue("AMOUNT","0");
                objRow.setValue("CODE", "210072");
                objReportData.AddRow(objRow);
                s3=1;
            }
            
            //END FIF SALES
            
            
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            HashMap Parameters=new HashMap();
            //Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("FROM_DATE",txtFromDate.getText().trim());
            Parameters.put("TO_DATE",txtToDate.getText().trim());
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptProductWiseSalesSummary.rpt",Parameters,objReportData);
            
            
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