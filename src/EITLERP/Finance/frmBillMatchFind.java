/*
 * frmGRNFind.java
 *
 * Created on May 15, 2004, 1:35 PM
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
import EITLERP.*;
 

public class frmBillMatchFind extends javax.swing.JApplet {
    
    private EITLComboModel cmbStatusModel;

    public boolean Cancelled=false;
    public String strQuery;
    public int GRNType=2;

    private EITLComboModel cmbForDeptModel;
    private EITLComboModel cmbBuyerModel;
    
    /** Initializes the applet frmGRNFind */
    public void init() {
        System.gc();
        setSize(565,300);
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
        jLabel2 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Records");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(6, 12, 170, 15);

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
        cmdCancel.setBounds(482, 70, 70, 25);

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
        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setLabelFor(txtDocNo);
        jLabel2.setText("Doc No.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(32, 16, 60, 15);

        txtDocNo.setNextFocusableComponent(txtFromDate);
        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(98, 14, 132, 19);

        jLabel3.setText("Date  From");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 54, 70, 15);

        txtFromDate.setNextFocusableComponent(txtToDate);
        jPanel1.add(txtFromDate);
        txtFromDate.setBounds(98, 50, 130, 19);

        jLabel4.setText(" To");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(236, 54, 34, 15);

        jPanel1.add(txtToDate);
        txtToDate.setBounds(282, 50, 130, 19);

        jLabel13.setText("Party Code");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(13, 90, 80, 15);

        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusLost(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });

        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(100, 89, 121, 19);

        txtPartyName.setEditable(false);
        jPanel1.add(txtPartyName);
        txtPartyName.setBounds(101, 110, 309, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 39, 458, 160);

    }//GEN-END:initComponents

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        txtPartyName.setText(clsPartyMaster.getAccountName("",txtPartyCode.getText()));        
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
try
{
    if(evt.getKeyCode()==112)
    {
    
                            LOV aList=new LOV();
                        
                        aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 ORDER BY PARTY_NAME";
                        aList.ReturnCol=1;
                        aList.ShowReturnCol=true;
                        aList.DefaultSearchOn=2;
                        aList.UseSpecifiedConn=true;
                        aList.dbURL=FinanceGlobal.FinURL;
                        
                        if(aList.ShowLOV()) {
                            txtPartyCode.setText(aList.ReturnVal);
                            txtPartyName.setText(clsPartyMaster.getAccountName("",txtPartyCode.getText()));
                        }
    }
}
catch(Exception e)
{
    
}
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked

    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ";
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
       
       if(!txtDocNo.getText().equals(""))
       {
         strQuery=strQuery+" AND DOC_NO='"+txtDocNo.getText()+"' ";
       }
       
       if(!txtFromDate.getText().equals(""))
       {
         strQuery=strQuery+" AND DOC_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
       }

       if(!txtToDate.getText().equals(""))
       {
         strQuery=strQuery+" AND DOC_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
       }
       
       if(!txtPartyCode.getText().equals(""))
       {
         strQuery=strQuery+" AND PARTY_CODE='"+txtPartyCode.getText()+"' ";
       }
       
       strQuery=strQuery;
       //========== End Sub Query ============//
       
       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    


}
