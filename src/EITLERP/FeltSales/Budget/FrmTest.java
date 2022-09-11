/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Budget;

import EITLERP.EITLERPGLOBAL;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import sdml.felt.commonUI.data;
//455,457,458,459,460,476,

/**
 *
 * @author root
 */
public class FrmTest extends javax.swing.JApplet {

    private clsExcelExporter exp = new clsExcelExporter();

    /**
     * Initializes the applet FrmAdvanceSearch
     */
    public void init() {

        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panel1 = new java.awt.Panel();
        btnFinalSalesProjection = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Sales Projection Final Approve");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 2, 1050, 25);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        panel1.setBackground(new java.awt.Color(220, 215, 215));
        panel1.setLayout(null);

        btnFinalSalesProjection.setText("Final Sales Projection");
        btnFinalSalesProjection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalSalesProjectionActionPerformed(evt);
            }
        });
        panel1.add(btnFinalSalesProjection);
        btnFinalSalesProjection.setBounds(20, 20, 180, 50);

        jTabbedPane1.addTab("Final Approve ", panel1);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 30, 1050, 540);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Shift Report");
    }// </editor-fold>//GEN-END:initComponents

    private void btnFinalSalesProjectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalSalesProjectionActionPerformed
        // TODO add your handling code here:

        data.Execute("UPDATE PRODUCTION.FELT_PROD_DOC_DATA SET STATUS='F' "
                + "WHERE MODULE_ID=834  AND USER_ID=399");
        data.Execute("UPDATE PRODUCTION.FELT_PROD_DOC_DATA SET STATUS='A' "
                + "WHERE MODULE_ID=834  AND USER_ID!=399 AND STATUS='W'");
        data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL SET APPROVED=1,APPROVED_DATE=CURDATE() "
                + "WHERE COALESCE(APPROVED,0)=0");
        data.Execute("INSERT INTO PRODUCTION.FELT_SALES_PROJECTION_FINAL_APPROVE "
                + "(FINAL_APPROVED_DATE, FINAL_APPROVED_BY, SALES_PROJECTION_MONTH, SALES_PROJECTION_FROM_YEAR, SALES_PROJECTION_TO_YEAR, "
                + "SALES_PROJECTION_PER, IS_GENERATED) "
                + "SELECT NOW()," + EITLERPGLOBAL.gNewUserID + ",MONTH(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)),"
                + " RIGHT(CASE WHEN MONTH(DATE_ADD(CURDATE(),INTERVAL 1 MONTH))<4 THEN YEAR(DATE_ADD(CURDATE(),INTERVAL 1 MONTH))-1 "
                + "ELSE YEAR(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)) END,2),"
                + " RIGHT(CASE WHEN MONTH(DATE_ADD(CURDATE(),INTERVAL 1 MONTH))<4 THEN YEAR(DATE_ADD(CURDATE(),INTERVAL 1 MONTH)) "
                + "ELSE YEAR(DATE_ADD(CURDATE(),INTERVAL 1 MONTH))+1 END,2),100,FALSE "
                + " FROM DUAL");
        JOptionPane.showMessageDialog(this,"Sales Projection has been Final Approved....");
        
    }//GEN-LAST:event_btnFinalSalesProjectionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFinalSalesProjection;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables
}