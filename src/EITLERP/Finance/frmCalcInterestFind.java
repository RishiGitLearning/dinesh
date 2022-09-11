/*
 * frmCalcInterestFind.java
 *
 * Created on May 08, 2008, 11:35 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  Mrugesh Thaker
 */

/*<APPLET CODE=frmCalcInterestFind.class HEIGHT=405 WIDTH=565></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.*;
import EITLERP.*;


public class frmCalcInterestFind extends javax.swing.JApplet {
    
    private EITLComboModel cmbStatusModel;
    
    public boolean Cancelled=false;
    public String strQuery;
    
    //private EITLComboModel cmbForDeptModel;
    //private EITLComboModel cmbBuyerModel;
    private Connection Conn;
    
    /** Initializes the applet frmGRNFind */
    public void init() {
        System.gc();
        setSize(565,300);
        Conn=data.getConn(FinanceGlobal.FinURL);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblDocNo = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        lblEffectiveDateFrom = new javax.swing.JLabel();
        txtEffectiveDateFrom = new javax.swing.JTextField();
        lblEffectiveDateTo = new javax.swing.JLabel();
        txtEffectiveDateTo = new javax.swing.JTextField();
        lblShowMessage = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText(" Find Records");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(6, 7, 550, 20);

        cmdFind.setText("Find");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });

        getContentPane().add(cmdFind);
        cmdFind.setBounds(482, 40, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(482, 76, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(txtDocNo);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(482, 110, 70, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lblDocNo.setDisplayedMnemonic('G');
        lblDocNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocNo.setLabelFor(txtDocNo);
        lblDocNo.setText("Doc No.:");
        jPanel1.add(lblDocNo);
        lblDocNo.setBounds(15, 16, 75, 15);

        txtDocNo.setNextFocusableComponent(txtEffectiveDateFrom);
        txtDocNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDocNoKeyPressed(evt);
            }
        });

        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(98, 14, 132, 19);

        lblEffectiveDateFrom.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffectiveDateFrom.setText("Effective Date From :");
        jPanel1.add(lblEffectiveDateFrom);
        lblEffectiveDateFrom.setBounds(5, 48, 129, 15);

        txtEffectiveDateFrom.setNextFocusableComponent(txtEffectiveDateTo);
        txtEffectiveDateFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEffectiveDateFromFocusLost(evt);
            }
        });

        jPanel1.add(txtEffectiveDateFrom);
        txtEffectiveDateFrom.setBounds(144, 44, 100, 19);

        lblEffectiveDateTo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffectiveDateTo.setText("Effective Date To :");
        jPanel1.add(lblEffectiveDateTo);
        lblEffectiveDateTo.setBounds(8, 73, 124, 15);

        txtEffectiveDateTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEffectiveDateToFocusLost(evt);
            }
        });

        jPanel1.add(txtEffectiveDateTo);
        txtEffectiveDateTo.setBounds(145, 70, 100, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 39, 458, 110);

        lblShowMessage.setDisplayedMnemonic('G');
        lblShowMessage.setLabelFor(txtDocNo);
        lblShowMessage.setText("  ...");
        getContentPane().add(lblShowMessage);
        lblShowMessage.setBounds(7, 155, 455, 20);

    }//GEN-END:initComponents
    
    private void txtDocNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocNoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT DOC_NO,DOC_DATE FROM FINANCE.D_FD_INT_CALC_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY DOC_NO";
            aList.ReturnCol=1;
            aList.SecondCol=2;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            aList.UseSpecifiedConn=true;
            aList.dbURL=FinanceGlobal.FinURL;
            
            if(aList.ShowLOV()) {
                if(!aList.ReturnVal.trim().equals("")) {
                    txtDocNo.setText(aList.ReturnVal);
                    lblEffectiveDateFrom.setText("Doc Date :");
                    txtEffectiveDateTo.setEnabled(false);
                    txtEffectiveDateFrom.setText(EITLERPGLOBAL.formatDate(data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='"+aList.ReturnVal+"' ",FinanceGlobal.FinURL)));
                }
            }
        }
    }//GEN-LAST:event_txtDocNoKeyPressed
    
    private void txtEffectiveDateToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffectiveDateToFocusLost
        // TODO add your handling code here:
        Validate();
    }//GEN-LAST:event_txtEffectiveDateToFocusLost
    
    private void txtEffectiveDateFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffectiveDateFromFocusLost
        // TODO add your handling code here:
        Validate();
    }//GEN-LAST:event_txtEffectiveDateFromFocusLost
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ";
        Cancelled=false;
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
        String subQuery="";
        
        strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
        
        if(!txtDocNo.getText().equals("")) {
            strQuery=strQuery+" AND DOC_NO='"+txtDocNo.getText()+"' ";
        }
        
        if(lblEffectiveDateFrom.getText().equals("Doc Date :")) {
            strQuery=strQuery+" AND DOC_DATE='"+EITLERPGLOBAL.formatDateDB(txtEffectiveDateFrom.getText())+"' ";
        } else {
            if(!txtEffectiveDateFrom.getText().trim().equals("") && !txtEffectiveDateTo.getText().trim().equals("")) {
                strQuery=strQuery+" AND EFFECTIVE_DATE>='"+EITLERPGLOBAL.formatDateDB(txtEffectiveDateFrom.getText())+"' AND EFFECTIVE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txtEffectiveDateTo.getText()) +"' ";
            }
        }
        
        strQuery=strQuery;
        //========== End Sub Query ============//
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    private void Validate() {
        
        if(txtEffectiveDateTo.isEnabled()){
            if(!txtEffectiveDateFrom.equals("")){
                if(!EITLERPGLOBAL.isDate(txtEffectiveDateFrom.getText())) {
                    lblShowMessage.setText("Wrong From Date...");
                    txtEffectiveDateFrom.requestFocus();
                    return;
                }
                lblShowMessage.setText("  ...");
                return;
            }
            return;
        }
        
        if(txtEffectiveDateTo.isEnabled()) {
            if(!txtEffectiveDateTo.equals("")){
                if(!EITLERPGLOBAL.isDate(txtEffectiveDateTo.getText())) {
                    lblShowMessage.setText("Wrong To Date...");
                    txtEffectiveDateTo.requestFocus();
                    return;
                }
                lblShowMessage.setText("  ...");
                return;
            }
            return;
        }
        return;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDocNo;
    private javax.swing.JLabel lblEffectiveDateFrom;
    private javax.swing.JLabel lblEffectiveDateTo;
    private javax.swing.JLabel lblShowMessage;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtEffectiveDateFrom;
    private javax.swing.JTextField txtEffectiveDateTo;
    // End of variables declaration//GEN-END:variables
}
