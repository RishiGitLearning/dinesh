/*
 * frmUpdateTransLinks.java
 *
 * Created on March 23, 2006, 12:16 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;
import EITLERP.*;


public class frmUpdateTransLinks extends javax.swing.JApplet {
    
    /** Initializes the applet frmUpdateTransLinks */
    public void init() {
        setSize(461, 201);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        
        getContentPane().setLayout(null);
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        
        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" UPDATE TRANSACTION LINKS");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(-2, 2, 804, 25);
        
        jLabel1.setText("Take transactions from date range");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(13, 38, 225, 15);
        
        jLabel2.setText("From Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(12, 71, 77, 15);
        
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(90, 67, 107, 21);
        
        jLabel3.setText("To Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(220, 71, 62, 15);
        
        getContentPane().add(txtToDate);
        txtToDate.setBounds(283, 67, 107, 21);
        
        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        getContentPane().add(jButton1);
        jButton1.setBounds(23, 134, 88, 25);
        
        jButton2.setText("Exit");
        getContentPane().add(jButton2);
        jButton2.setBounds(122, 134, 88, 25);
        
        lblStatus.setText("...");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(10, 109, 396, 20);
        
    }//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        startProcess();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void startProcess() {
        
        
        new Thread() {
            
            public void run() {
                String strSQL="";
                String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
                String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
                
                ResultSet rsTmp,rsDest;
                
                // ======================   MIR =======================//
                try {
                    
                    strSQL="SELECT A.MIR_NO,A.MIR_TYPE,B.ITEM_ID,B.SR_NO FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.MIR_NO=B.MIR_NO AND A.MIR_TYPE=B.MIR_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.MIR_DATE>='"+FromDate+"' AND A.MIR_DATE<='"+ToDate+"'";
                    rsTmp=data.getResult(strSQL);
                    
                    rsTmp.first();
                    while(!rsTmp.isAfterLast()) {
                        String MIRNo=rsTmp.getString("MIR_NO");
                        int MIRSrNo=rsTmp.getInt("SR_NO");
                        int MIRType=rsTmp.getInt("MIR_TYPE");
                        
                        lblStatus.setText("MIR :"+MIRNo);
                        lblStatus.repaint();
                        
                        //Get the sum of
                        strSQL="SELECT IF(ISNULL(SUM(QTY)),0,SUM(QTY)) SUMQTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.MIR_NO='"+MIRNo+"' AND B.MIR_SR_NO="+MIRSrNo+" AND B.MIR_TYPE="+MIRType;
                        
                        rsDest=data.getResult(strSQL);
                        rsDest.first();
                        
                        if(rsDest.getRow()>0) {
                            double SumQty=rsDest.getDouble("SUMQTY");
                            
                            //Update the MIR as per GRN Received Qty.
                            data.Execute("UPDATE D_INV_MIR_DETAIL SET GRN_RECD_QTY="+SumQty+",BAL_QTY=QTY-GRN_RECD_QTY WHERE MIR_NO='"+MIRNo+"' AND SR_NO="+MIRSrNo+" AND MIR_TYPE="+MIRType);
                        }
                        
                        rsTmp.next();
                    }
                    
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null,"Error occured while updating MIRs. Error is "+e.getMessage());
                }
                // ======================== End MIR ========================//
                
                
                
                
                // ======================   P.O. =======================//
                try {
                    
                    strSQL="SELECT A.PO_NO,A.PO_TYPE,A.PO_DATE,B.ITEM_ID,B.SR_NO,B.QTY FROM D_PUR_PO_HEADER A,D_PUR_PO_DETAIL B WHERE A.PO_NO=B.PO_NO AND A.PO_TYPE=B.PO_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.PO_DATE>='"+FromDate+"' AND A.PO_DATE<='"+ToDate+"' ";
                    
                    rsTmp=data.getResult(strSQL);
                    
                    rsTmp.first();
                    while(!rsTmp.isAfterLast()) {
                        String PONo=rsTmp.getString("PO_NO");
                        int POSrNo=rsTmp.getInt("SR_NO");
                        int POType=rsTmp.getInt("PO_TYPE");
                        
                        lblStatus.setText("PO :"+PONo);
                        lblStatus.repaint();
                        
                        //Get the sum of
                        strSQL="SELECT IF(ISNULL(SUM(QTY)),0,SUM(QTY)) SUMQTY FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.MIR_NO=B.MIR_NO AND A.MIR_TYPE=B.MIR_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.PO_NO='"+PONo+"' AND B.PO_SR_NO="+POSrNo+" AND B.PO_TYPE="+POType;
                        
                        rsDest=data.getResult(strSQL);
                        rsDest.first();
                        
                        if(rsDest.getRow()>0) {
                            double SumQty=rsDest.getDouble("SUMQTY");
                            
                            //Update the MIR as per GRN Received Qty.
                            data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY="+SumQty+",PENDING_QTY=QTY-RECD_QTY WHERE PO_NO='"+PONo+"' AND SR_NO="+POSrNo+" AND PO_TYPE="+POType);
                        }
                        
                        rsTmp.next();
                    }
                    
                    
                    
                    
                    lblStatus.setText("Done");
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null,"Error occured while updating PO Error is "+e.getMessage());
                }
                // ======================== End MIR ========================//
            };
        }.start();
        
    }
    
    
    
    
}
