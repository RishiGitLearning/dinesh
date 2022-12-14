/*
 * frmGRNFind.java
 *
 * Created on NOVEMBER 15, 2008, 1:35 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  nrpithva
 */

/*<APPLET CODE=frmGRNFind.class HEIGHT=405 WIDTH=565></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.*;
import EITLERP.*;


public class frmSalesDepositFind extends javax.swing.JApplet {
    
    private EITLComboModel cmbStatusModel;
    
    public boolean Cancelled=false;
    public String strQuery;
    
    private EITLComboModel cmbForDeptModel;
    private EITLComboModel cmbBuyerModel;
    private Connection Conn;
    
    /** Initializes the applet frmGRNFind */
    public void init() {
        System.gc();
        setSize(665,400);
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
        lblReceiptDateFrom = new javax.swing.JLabel();
        txtReceiptDateFrom = new javax.swing.JTextField();
        lblReceiptNo = new javax.swing.JLabel();
        txtReceiptNo = new javax.swing.JTextField();
        lblReceiptDateTo = new javax.swing.JLabel();
        txtReceiptDateTo = new javax.swing.JTextField();
        lblEffDateFrom = new javax.swing.JLabel();
        txtEffDateFrom = new javax.swing.JTextField();
        lblEffDateTo = new javax.swing.JLabel();
        txtEffDateTo = new javax.swing.JTextField();
        lblApplicantName = new javax.swing.JLabel();
        txtApplicanctName = new javax.swing.JTextField();
        txtPartyCode = new javax.swing.JTextField();
        lblPartyCode = new javax.swing.JLabel();
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
        jLabel1.setBounds(6, 7, 643, 20);

        cmdFind.setText("Find");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });

        getContentPane().add(cmdFind);
        cmdFind.setBounds(579, 40, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(579, 75, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(txtReceiptNo);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(579, 110, 70, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lblReceiptDateFrom.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReceiptDateFrom.setText("Receipt Date From :");
        jPanel1.add(lblReceiptDateFrom);
        lblReceiptDateFrom.setBounds(21, 48, 123, 15);

        txtReceiptDateFrom.setNextFocusableComponent(txtReceiptDateTo);
        txtReceiptDateFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReceiptDateFromFocusLost(evt);
            }
        });

        jPanel1.add(txtReceiptDateFrom);
        txtReceiptDateFrom.setBounds(155, 47, 132, 19);

        lblReceiptNo.setDisplayedMnemonic('G');
        lblReceiptNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReceiptNo.setText("Receipt No :");
        jPanel1.add(lblReceiptNo);
        lblReceiptNo.setBounds(51, 13, 92, 15);

        txtReceiptNo.setNextFocusableComponent(txtReceiptDateFrom);
        txtReceiptNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReceiptNoFocusLost(evt);
            }
        });
        txtReceiptNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtReceiptNoKeyPressed(evt);
            }
        });

        jPanel1.add(txtReceiptNo);
        txtReceiptNo.setBounds(155, 10, 132, 19);

        lblReceiptDateTo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReceiptDateTo.setText("Receipt Date To :");
        jPanel1.add(lblReceiptDateTo);
        lblReceiptDateTo.setBounds(294, 48, 124, 15);

        txtReceiptDateTo.setNextFocusableComponent(txtEffDateFrom);
        txtReceiptDateTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReceiptDateToFocusLost(evt);
            }
        });

        jPanel1.add(txtReceiptDateTo);
        txtReceiptDateTo.setBounds(425, 49, 132, 19);

        lblEffDateFrom.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffDateFrom.setText("Effective Date From :");
        jPanel1.add(lblEffDateFrom);
        lblEffDateFrom.setBounds(10, 80, 133, 15);

        txtEffDateFrom.setNextFocusableComponent(txtEffDateTo);
        txtEffDateFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEffDateFromFocusLost(evt);
            }
        });

        jPanel1.add(txtEffDateFrom);
        txtEffDateFrom.setBounds(155, 80, 132, 19);

        lblEffDateTo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffDateTo.setText("Effective Date To :");
        jPanel1.add(lblEffDateTo);
        lblEffDateTo.setBounds(294, 80, 124, 15);

        txtEffDateTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEffDateToFocusLost(evt);
            }
        });

        jPanel1.add(txtEffDateTo);
        txtEffDateTo.setBounds(425, 79, 132, 19);

        lblApplicantName.setDisplayedMnemonic('G');
        lblApplicantName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApplicantName.setText("Applicanct Name :");
        jPanel1.add(lblApplicantName);
        lblApplicantName.setBounds(10, 109, 132, 15);

        txtApplicanctName.setNextFocusableComponent(txtPartyCode);
        jPanel1.add(txtApplicanctName);
        txtApplicanctName.setBounds(155, 109, 320, 19);

        txtPartyCode.setNextFocusableComponent(cmdFind);
        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(155, 139, 132, 19);

        lblPartyCode.setDisplayedMnemonic('G');
        lblPartyCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPartyCode.setText("Party Code :");
        jPanel1.add(lblPartyCode);
        lblPartyCode.setBounds(32, 139, 110, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 39, 564, 179);

        lblShowMessage.setDisplayedMnemonic('G');
        lblShowMessage.setText("  ...");
        getContentPane().add(lblShowMessage);
        lblShowMessage.setBounds(9, 225, 559, 20);

    }//GEN-END:initComponents
            
    private void txtEffDateToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffDateToFocusLost
        // TODO add your handling code here:
        Validate_EffDate();
    }//GEN-LAST:event_txtEffDateToFocusLost
    
    private void txtEffDateFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEffDateFromFocusLost
        // TODO add your handling code here:
        Validate_EffDate();
    }//GEN-LAST:event_txtEffDateFromFocusLost
    
    private void txtReceiptNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReceiptNoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT RECEIPT_NO,RECEIPT_DATE,APPLICANT_NAME FROM D_FD_SALES_DEPOSIT_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY RECEIPT_NO";
            aList.ReturnCol=1;
            aList.SecondCol=2;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            aList.UseSpecifiedConn=true;
            aList.dbURL=FinanceGlobal.FinURL;
            
            if(aList.ShowLOV()) {
                if(!aList.ReturnVal.trim().equals("")) {
                    txtReceiptNo.setText(aList.ReturnVal);
                    lblReceiptDateFrom.setText("Receipt Date :");
                    txtReceiptDateTo.setEnabled(false);
                    lblReceiptDateTo.setEnabled(false);
                    txtReceiptDateFrom.setText(EITLERPGLOBAL.formatDate(aList.SecondVal));
                }
            }
        }
    }//GEN-LAST:event_txtReceiptNoKeyPressed
    
    private void txtReceiptNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReceiptNoFocusLost
        // TODO add your handling code here:
        try {
            if(!txtReceiptNo.getText().equals("")) {
                
                if(data.IsRecordExist("SELECT COUNT(*) FROM D_FD_SALES_DEPOSIT_MASTER WHERE COMPANY_ID='"+ EITLERPGLOBAL.gCompanyID +"' AND RECEIPT_NO='"+txtReceiptNo.getText().trim()+"'",FinanceGlobal.FinURL)) {
                    String SQL = "SELECT RECEIPT_NO,RECEIPT_DATE,APPLICANT_NAME,EFFECTIVE_DATE,PARTY_CODE "+
                    " FROM D_FD_SALES_DEPOSIT_MASTER WHERE COMPANY_ID = '"+ EITLERPGLOBAL.gCompanyID +
                    "' AND RECEIPT_NO = '"+ txtReceiptNo.getText().trim() +"' ";
                    
                    Statement stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet rsTmp = stTmp.executeQuery(SQL);
                    
                    rsTmp.first();
                    if (rsTmp.getRow() > 0) {
                        txtReceiptNo.setText(rsTmp.getString("RECEIPT_NO"));
                        txtPartyCode.setText(rsTmp.getString("PARTY_CODE"));
                        txtApplicanctName.setText(rsTmp.getString("APPLICANT_NAME"));
                        lblReceiptDateFrom.setText("Receipt Date :");
                        txtReceiptDateTo.setEnabled(false);
                        lblReceiptDateTo.setEnabled(false);
                        txtReceiptDateFrom.setText(EITLERPGLOBAL.formatDate(rsTmp.getString("RECEIPT_DATE")));
                        txtReceiptDateTo.setText("");
                        
                        lblEffDateFrom.setText("Effective Date :");
                        txtEffDateTo.setEnabled(false);
                        lblEffDateTo.setEnabled(false);
                        txtEffDateFrom.setText(EITLERPGLOBAL.formatDate(rsTmp.getString("EFFECTIVE_DATE")));
                        txtEffDateTo.setText("");
                        
                        lblShowMessage.setText("  ...");
                    }
                } else {
                    //System.out.println("hi");
                    txtReceiptNo.setText("");
                    txtApplicanctName.setText("");
                    txtPartyCode.setText("");
                    lblReceiptDateFrom.setText("Receipt From Date :");
                    txtReceiptDateTo.setEnabled(true);
                    lblReceiptDateTo.setEnabled(true);
                    txtReceiptDateFrom.setText("");
                    txtReceiptDateTo.setText("");
                    
                    lblEffDateFrom.setText("Effective From Date :");
                    txtEffDateTo.setEnabled(true);
                    lblEffDateTo.setEnabled(true);
                    txtEffDateFrom.setText("");
                    txtEffDateTo.setText("");
                    
                    txtReceiptNo.requestFocus();
                    lblShowMessage.setText("  Wrong Receipt No ...");
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtReceiptNoFocusLost
    
    private void txtReceiptDateToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReceiptDateToFocusLost
        // TODO add your handling code here:
        Validate_ReceiptDate();
    }//GEN-LAST:event_txtReceiptDateToFocusLost
    
    private void txtReceiptDateFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReceiptDateFromFocusLost
        // TODO add your handling code here:
        Validate_ReceiptDate();
    }//GEN-LAST:event_txtReceiptDateFromFocusLost
    
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
        
        if(!txtReceiptNo.getText().equals("")) {
            strQuery=strQuery+" AND RECEIPT_NO LIKE '%"+txtReceiptNo.getText()+"%' ";
        }
        
        if(!txtApplicanctName.getText().equals("")) {
            strQuery=strQuery+" AND APPLICANT_NAME LIKE '"+txtApplicanctName.getText()+"%' ";
        }
        
        if(!txtPartyCode.getText().equals("")) {
            strQuery=strQuery+" AND PARTY_CODE='"+txtPartyCode.getText()+"' ";
        }
        
        if (lblReceiptDateFrom.getText().equals("Receipt Date :")) {
            strQuery=strQuery+" AND RECEIPT_DATE='"+EITLERPGLOBAL.formatDateDB(txtReceiptDateFrom.getText())+"' ";
        } else if (!txtReceiptDateFrom.getText().equals("")) {
            strQuery=strQuery+" AND RECEIPT_DATE>='"+EITLERPGLOBAL.formatDateDB(txtReceiptDateFrom.getText())+"' AND RECEIPT_DATE<='"+ EITLERPGLOBAL.formatDateDB(txtReceiptDateTo.getText()) +"' ";
        }
        
        if (lblEffDateFrom.getText().equals("Effective Date :")) {
            strQuery=strQuery+" AND EFFECTIVE_DATE='"+EITLERPGLOBAL.formatDateDB(txtEffDateFrom.getText())+"' ";
        } else if (!txtEffDateFrom.getText().equals("")) {
            strQuery=strQuery+" AND EFFECTIVE_DATE>='"+EITLERPGLOBAL.formatDateDB(txtEffDateFrom.getText())+"' AND EFFECTIVE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txtEffDateTo.getText()) +"' ";
        }
        
        strQuery=strQuery;
        //========== End Sub Query ============//
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    private void Validate_ReceiptDate() {
        
        if(txtReceiptDateTo.isEnabled()){
            if(!txtReceiptDateFrom.equals("")){
                if(!EITLERPGLOBAL.isDate(txtReceiptDateFrom.getText())) {
                    lblShowMessage.setText("Wrong Receipt From Date...");
                    txtReceiptDateFrom.requestFocus();
                    return;
                }
                lblShowMessage.setText("  ...");
                return;
            }
            return;
        }
        
        if(txtReceiptDateTo.isEnabled()) {
            if(!txtReceiptDateTo.equals("")){
                if(!EITLERPGLOBAL.isDate(txtReceiptDateTo.getText())) {
                    lblShowMessage.setText("Wrong Receipt To Date...");
                    txtReceiptDateTo.requestFocus();
                    return;
                }
                lblShowMessage.setText("  ...");
                return;
            }
            return;
        }
        return;
    }
    
    private void Validate_EffDate() {
        
        if(txtEffDateTo.isEnabled()){
            if(!txtEffDateFrom.equals("")){
                if(!EITLERPGLOBAL.isDate(txtEffDateFrom.getText())) {
                    lblShowMessage.setText("Wrong Effective From Date...");
                    txtEffDateFrom.requestFocus();
                    return;
                }
                lblShowMessage.setText("  ...");
                return;
            }
            return;
        }
        
        if(txtEffDateTo.isEnabled()) {
            if(!txtEffDateTo.equals("")){
                if(!EITLERPGLOBAL.isDate(txtEffDateTo.getText())) {
                    lblShowMessage.setText("Wrong Effective To Date...");
                    txtEffDateTo.requestFocus();
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
    private javax.swing.JLabel lblApplicantName;
    private javax.swing.JLabel lblEffDateFrom;
    private javax.swing.JLabel lblEffDateTo;
    private javax.swing.JLabel lblPartyCode;
    private javax.swing.JLabel lblReceiptDateFrom;
    private javax.swing.JLabel lblReceiptDateTo;
    private javax.swing.JLabel lblReceiptNo;
    private javax.swing.JLabel lblShowMessage;
    private javax.swing.JTextField txtApplicanctName;
    private javax.swing.JTextField txtEffDateFrom;
    private javax.swing.JTextField txtEffDateTo;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtReceiptDateFrom;
    private javax.swing.JTextField txtReceiptDateTo;
    private javax.swing.JTextField txtReceiptNo;
    // End of variables declaration//GEN-END:variables
    
    
    
}
