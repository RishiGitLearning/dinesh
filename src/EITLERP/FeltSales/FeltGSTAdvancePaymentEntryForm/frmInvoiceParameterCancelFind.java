/*
 * frmItemFind.java
 *
 * Created on April 28, 2004, 11:04 AM
 */

package EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm;

/*<APPLET CODE=frmItemFind.class HEIGHT=280 WIDTH=460></APPLET>*/
/**
 *
 * @author  nrpithva
 */
//import EITLERP.*;
import EITLERP.EITLERPGLOBAL;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class frmInvoiceParameterCancelFind extends javax.swing.JApplet {
    
    public boolean Cancelled=false;
    public String strQuery;
    
    
    /** Initializes the applet frmItemFind */
    public void init() {
        System.gc();
        setSize(500,200);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        cmdCancel = new javax.swing.JButton();
        cmdFind = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();
        txtToDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtReqNo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Find Cancel Request");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(5, 7, 147, 15);

        jLabel2.setText("From Date");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(16, 40, 70, 17);

        txtFromDate.setNextFocusableComponent(txtDocNo);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(92, 36, 110, 27);

        jLabel3.setText("Doc No. ");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(16, 140, 69, 17);
        getContentPane().add(txtDocNo);
        txtDocNo.setBounds(92, 138, 172, 27);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(340, 50, 105, 25);

        cmdFind.setText("Find");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(340, 20, 106, 25);

        cmdClear.setText("Clear ");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(txtFromDate);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });
        getContentPane().add(cmdClear);
        cmdClear.setBounds(340, 90, 104, 25);

        txtToDate.setNextFocusableComponent(txtReqNo);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(93, 65, 110, 27);

        jLabel5.setText("To Date");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(27, 69, 59, 17);

        txtReqNo.setNextFocusableComponent(txtDocNo);
        getContentPane().add(txtReqNo);
        txtReqNo.setBounds(91, 107, 172, 27);

        jLabel7.setText("Req. No. ");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(15, 109, 69, 17);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        Cancelled=false;
        strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdClearActionPerformed
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        strQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        // TODO add your handling code here:
        Cancelled=false;
        
        strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID=80 ";

       if(!txtReqNo.getText().equals(""))
       {
         strQuery=strQuery+" AND REQ_NO='"+txtReqNo.getText()+"' ";
       }
        
       if(!txtDocNo.getText().equals(""))
       {
         strQuery=strQuery+" AND DOC_NO='"+txtDocNo.getText()+"' ";
       }
        
       if(!txtFromDate.getText().equals(""))
       {
         strQuery=strQuery+" AND REQ_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
       }

       if(!txtToDate.getText().equals(""))
       {
         strQuery=strQuery+" AND REQ_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
       }
        
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtReqNo;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    
}
