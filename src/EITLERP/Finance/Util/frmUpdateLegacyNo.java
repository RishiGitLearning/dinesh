/*
 * frmUpdateLegacyNo.java
 *
 * Created on January 28, 2010, 10:36 AM
 */

package EITLERP.Finance.Util;

import javax.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.io.*;

import EITLERP.*;
import java.util.*;
import java.net.*;
import EITLERP.Utils.*;
import java.io.*;
import EITLERP.Purchase.*;
import EITLERP.Stores.*;
import EITLERP.Finance.ReportsUI.*;
import java.sql.*;
import TReportWriter.*;
import EITLERP.Utils.SimpleDataProvider.*;
import EITLERP.Finance.Config.*;
import EITLERP.Finance.ReportsUI.*;
import EITLERP.Finance.*;
/**
 *
 * @author  Prathmesh Shah
 */
public class frmUpdateLegacyNo extends javax.swing.JApplet {
    Connection Conn;
    Statement Stmt;
    ResultSet rsResultSet;
    
    /** Initializes the applet frmUpdateLegacyNo */
    public void init() {
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        txtBookCode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtLegacyNo = new javax.swing.JTextField();
        cmbUpdateLegacyNo = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setText("Book Code");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(30, 50, 90, 15);

        txtBookCode.setText("58");
        getContentPane().add(txtBookCode);
        txtBookCode.setBounds(130, 50, 110, 19);

        jLabel2.setText("LastLegacy No.");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 90, 110, 15);

        txtLegacyNo.setText("820");
        getContentPane().add(txtLegacyNo);
        txtLegacyNo.setBounds(130, 90, 110, 19);

        cmbUpdateLegacyNo.setText("Update Legacy Number");
        cmbUpdateLegacyNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUpdateLegacyNoActionPerformed(evt);
            }
        });

        getContentPane().add(cmbUpdateLegacyNo);
        cmbUpdateLegacyNo.setBounds(50, 170, 180, 25);

    }//GEN-END:initComponents
    
    private void cmbUpdateLegacyNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUpdateLegacyNoActionPerformed
        // TODO add your handling code here:
        try{
            String VoucherNo="", VoucherLike="";
            String Qry="",Extra1 = "",Extra2 = "";
            if (txtBookCode.getText().equals("31") || txtBookCode.getText().equals("33") || txtBookCode.getText().equals("34") 
            || txtBookCode.getText().equals("36") || txtBookCode.getText().equals("37") || txtBookCode.getText().equals("52")
            || txtBookCode.getText().equals("54") || txtBookCode.getText().equals("58") || txtBookCode.getText().equals("74")
            || txtBookCode.getText().equals("75") || txtBookCode.getText().equals("76")){
                VoucherLike ="PY%";
            }
            else if (txtBookCode.getText().equals("43")){
                Extra1 = "(";
                VoucherLike ="PA%' OR VOUCHER_NO LIKE 'PJ%";
                Extra2 = ")";
                //PA & PJ
            }
            else if (txtBookCode.getText().equals("30")) {
                //book_code=30
                VoucherLike ="CR%";
                //Cash payment CS
            }
            
            else if (txtBookCode.getText().equals("41")) {
                VoucherLike ="PJ%";
            }
            Qry = "SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE LEGACY_NO="+txtLegacyNo.getText() + " AND BOOK_CODE="+txtBookCode.getText()+ " AND VOUCHER_DATE >='2010-04-01'";
            
            String StratVoucherDate = "2012-04-01";//data.getStringValueFromDB(Qry, FinanceGlobal.FinURL);
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //        Qry = "SELECT VOUCHER_NO, VOUCHER_DATE, LEGACY_NO, LEGACY_DATE FROM D_FIN_VOUCHER_HEADER "+
            /*Qry = "SELECT * FROM D_FIN_VOUCHER_HEADER "+
            "WHERE BOOK_CODE ="+txtBookCode.getText()+" AND "+Extra1+" VOUCHER_NO LIKE '"+VoucherLike+"' "+Extra2+" AND VOUCHER_DATE >='"+StratVoucherDate+"' "+
            "AND (LEGACY_NO>"+txtLegacyNo.getText()+" OR LEGACY_NO ='' OR LEGACY_NO LIKE null) "+
            "ORDER BY VOUCHER_DATE, VOUCHER_NO";*/
            
            /*Qry = "SELECT * FROM D_FIN_VOUCHER_HEADER "+
            "WHERE BOOK_CODE ="+txtBookCode.getText()+" AND VOUCHER_NO LIKE '"+VoucherLike+"' AND VOUCHER_DATE >='2011-05-01' "+
            "AND (CONVERT(LEGACY_NO,SIGNED)>"+txtLegacyNo.getText()+" OR LEGACY_NO ='' OR LEGACY_NO IS NULL) "+
            "AND APPROVED=1 AND CANCELLED=0  "+
            "ORDER BY VOUCHER_DATE, CONVERT(LEGACY_NO,SIGNED)";*/
            
            /*Qry = "SELECT * FROM D_FIN_VOUCHER_HEADER WHERE BOOK_CODE =58 AND VOUCHER_NO LIKE 'PY11%' AND VOUCHER_DATE >='2011-08-12' " +
            "AND (LEGACY_NO>=820 OR LEGACY_NO ='' OR LEGACY_NO LIKE null) AND APPROVED=1 AND CANCELLED=0 " +
            "ORDER BY VOUCHER_DATE ";*/
            
             /*Qry = "SELECT * FROM D_FIN_VOUCHER_HEADER WHERE BOOK_CODE =22 AND VOUCHER_NO LIKE 'JV12%' AND VOUCHER_DATE >='2012-04-01' " +
                    "AND APPROVED=1 AND CANCELLED=0 " +
                    "ORDER BY VOUCHER_DATE,VOUCHER_NO ";*/
            Qry = "SELECT * FROM D_FIN_VOUCHER_HEADER WHERE BOOK_CODE =61 AND VOUCHER_NO LIKE 'PA13%' AND VOUCHER_DATE >='2013-04-01' " +
                    "AND APPROVED=1 AND CANCELLED=0 " +
                    "ORDER BY APPROVED_DATE,VOUCHER_NO ";
            ResultSet rsMain = Stmt.executeQuery(Qry);
            
            rsMain.first();
            int LastLegacyNo=0; //Integer.parseInt(txtLegacyNo.getText().trim());
            
            while (!rsMain.isAfterLast()) {
                VoucherNo = rsMain.getString("VOUCHER_NO");
                String VoucherLegacyNo     = rsMain.getString("LEGACY_NO");
                String VoucherDate         = rsMain.getString("VOUCHER_DATE");
                String VoucherLegacyDate   = rsMain.getString("LEGACY_DATE");
                int VoucherApproved        = rsMain.getInt("APPROVED");
                int VoucherCancelled       = rsMain.getInt("CANCELLED");
                
                //if (VoucherApproved==1){
                    int tmpLegacyNo= ++LastLegacyNo;
                    String strLegacyNo = Integer.toString(LastLegacyNo);
                    rsMain.updateString("LEGACY_NO", strLegacyNo);
                    //rsMain.updateString("LEGACY_DATE", VoucherDate);
                /*}else if(VoucherApproved==0 ){
                        rsMain.updateString("LEGACY_NO", "");
                        rsMain.updateString("LEGACY_DATE", "0000-00-00");
                }*/
                
                rsMain.updateRow();
                rsMain.next();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbUpdateLegacyNoActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmbUpdateLegacyNo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtBookCode;
    private javax.swing.JTextField txtLegacyNo;
    // End of variables declaration//GEN-END:variables
    
}
