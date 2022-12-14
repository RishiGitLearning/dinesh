/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.FeltSales.ReopenBaleReport;

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
import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URL;
import javax.swing.JOptionPane;
import java.io.*;
import java.sql.ResultSet;
import javax.swing.JTable;
import EITLERP.FeltSales.ReopenBaleReport.ReportRegister;
//import EITLERP.FeltSales.ReopenBaleReport.ReportRegister;



public class frmRptReopenBaleReport extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    private EITLTableCellRenderer RowFormat=new EITLTableCellRenderer();
    //private clsExcelExporter exp = new clsExcelExporter();
    EITLTableModel DataModel= new EITLTableModel();
  //  private TReportEngine objEngine=new TReportEngine();
    
    /** Initializes the applet frmRptGRNInfo */
    
    public void init() {
       // setSize(424,264);
        setSize(500,250);
        initComponents();
        
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(null);

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel6.setText("Felt Reopen Bale Report");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(10, 0, 260, 30);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 450, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("From Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 70, 90, 20);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(120, 70, 110, 27);

        jLabel3.setText("To Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(240, 70, 60, 20);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(300, 70, 120, 30);

        jLabel1.setText("Period");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 40, 90, 17);

        jButton1.setText("Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(150, 120, 100, 29);

        jButton2.setText("Daily Report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(270, 120, 120, 29);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     ReportShow();  
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    DailyReport();             // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
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
    
    @SuppressWarnings("UnusedAssignment")
   private void ReportShow() {
        
        try {
            
            
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            
            objReportData.AddColumn("BALE_NO");
            objReportData.AddColumn("BALE_DATE");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("PIECE_NO");
            objReportData.AddColumn("PRODUCT_CODE");
            objReportData.AddColumn("LENGTH");
            objReportData.AddColumn("WIDTH");
            objReportData.AddColumn("GSM");
            objReportData.AddColumn("PAC_MT");
            objReportData.AddColumn("PR_BILL_WEIGHT");
            //PR_BILL_WEIGHT
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("BALE_NO","");
            objOpeningRow.setValue("BALE_DATE","");
            objOpeningRow.setValue("PARTY_CODE","");
            objOpeningRow.setValue("PIECE_NO","");
            objOpeningRow.setValue("PRODUCT_CODE","");
            objOpeningRow.setValue("LENGTH","");
            objOpeningRow.setValue("WIDTH","");
            objOpeningRow.setValue("GSM","");
            objOpeningRow.setValue("PAC_MT","");
            objOpeningRow.setValue("PR_BILL_WEIGHT","");
            
            String query_str=" ";

            
            if(!"".equals(txtFromDate.getText()))
            {
                query_str = " AND A.CREATED_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"'";
            }
            
            if(!"".equals(txtToDate.getText()))
            {
                query_str =  query_str +" AND A.CREATED_DATE<='"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"'";
            }

            
            
//            String strSQL="SELECT A.PARTY_CODE,A.BALE_NO,A.BALE_DATE,B.PIECE_NO,B.PRODUCT_CODE FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL B,PRODUCTION.FELT_REOPEN_BALE_HEADER A WHERE A.BALE_NO!='' "+query_str+" GROUP BY A.BALE_NO";
            String strSQL="SELECT A.PARTY_CODE,A.BALE_NO,A.BALE_DATE,B.PIECE_NO,B.PRODUCT_CODE,B.LENGTH,B.WIDTH,B.GSM,SUM(B.LENGTH) AS PAC_MT,PR.PR_BILL_WEIGHT FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL B,PRODUCTION.FELT_REOPEN_BALE_HEADER A,PRODUCTION.FELT_SALES_PIECE_REGISTER PR WHERE A.BALE_NO=B.BALE_NO AND A.BALE_DATE=B.BALE_DATE AND A.BALE_NO!='' "+query_str+"  AND A.DOC_NO=B.DOC_NO AND B.PIECE_NO=PR.PR_PIECE_NO GROUP BY A.BALE_NO ";
            
            System.out.println(strSQL);
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("BALE_NO",UtilFunctions.getString(rsTmp,"BALE_NO",""));
                    objRow.setValue("BALE_DATE",UtilFunctions.getString(rsTmp,"BALE_DATE",""));
                    objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                    objRow.setValue("PIECE_NO",UtilFunctions.getString(rsTmp,"PIECE_NO",""));
                    objRow.setValue("PRODUCT_CODE",UtilFunctions.getString(rsTmp,"PRODUCT_CODE",""));
                    objRow.setValue("LENGTH",UtilFunctions.getString(rsTmp,"LENGTH",""));
                    objRow.setValue("WIDTH",UtilFunctions.getString(rsTmp,"WIDTH",""));
                    objRow.setValue("GSM",UtilFunctions.getString(rsTmp,"GSM",""));
                    objRow.setValue("PAC_MT",UtilFunctions.getString(rsTmp,"PAC_MT",""));
                    objRow.setValue("PR_BILL_WEIGHT",UtilFunctions.getString(rsTmp,"PR_BILL_WEIGHT",""));
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            
            HashMap Parameters=new HashMap();
            Parameters.put("FROM_DATE",txtFromDate.getText().trim());
            Parameters.put("TO_DATE",txtToDate.getText().trim());
            Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/Production/rptReopenBale.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
  private void DailyReport() {
        
        String FromDate =EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate =EITLERPGLOBAL.formatDateDB(txtToDate.getText());
        
        Connection Conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Conn = data.getConn();
            st = Conn.createStatement();

            HashMap parameterMap = new HashMap();
            
            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            
            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            String strSQL = "SELECT B.BALE_DATE,PRODUCT_CODE,A.PIECE_NO,A.LENGTH,B.BALE_NO,B.PARTY_CODE FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL A,PRODUCTION.FELT_REOPEN_BALE_HEADER B WHERE B.BALE_NO=A.BALE_NO AND B.BALE_DATE=A.BALE_DATE AND B.CREATED_DATE>='"+FromDate+"' AND B.CREATED_DATE<='"+ToDate+"' AND B.APPROVED=1 AND B.CANCELED=0";
            System.out.println("SQL : "+strSQL);

            rpt.setReportName("/EITLERP/FeltSales/ReopenBaleReport/DailyRepoen.jrxml", 1, strSQL); //productlist is the name of my jasper file.
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
}
