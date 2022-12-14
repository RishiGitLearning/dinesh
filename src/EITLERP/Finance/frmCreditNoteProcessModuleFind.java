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
import EITLERP.Finance.*;


public class frmCreditNoteProcessModuleFind extends javax.swing.JApplet {
    
    
    public boolean Cancelled=false;
    public String strQuery;
    
    
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
        txtPolicyNo = new javax.swing.JTextField();
        txtPolicyName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();

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
        cmdClear.setNextFocusableComponent(txtPolicyNo);
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
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setLabelFor(txtPolicyNo);
        jLabel2.setText("Policy No.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 70, 75, 15);

        txtPolicyNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPolicyNoFocusLost(evt);
            }
        });
        txtPolicyNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPolicyNoKeyPressed(evt);
            }
        });

        jPanel1.add(txtPolicyNo);
        txtPolicyNo.setBounds(100, 70, 132, 19);

        txtPolicyName.setEditable(false);
        jPanel1.add(txtPolicyName);
        txtPolicyName.setBounds(100, 100, 340, 19);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Date  From");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 40, 75, 15);

        jPanel1.add(txtFromDate);
        txtFromDate.setBounds(100, 40, 132, 19);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(" To");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(240, 40, 34, 15);

        jPanel1.add(txtToDate);
        txtToDate.setBounds(280, 40, 132, 19);

        jLabel5.setDisplayedMnemonic('G');
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Doc No.");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(20, 14, 75, 15);

        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(100, 14, 132, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 39, 458, 223);

    }//GEN-END:initComponents
    
    private void txtPolicyNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPolicyNoFocusLost
        // TODO add your handling code here:
        if (! txtPolicyNo.getText().trim().equals("")) {
            txtPolicyName.setText(clsPolicyMaster.getPolicyName(EITLERPGLOBAL.gCompanyID, txtPolicyNo.getText().trim()));
        }
    }//GEN-LAST:event_txtPolicyNoFocusLost
    
    private void txtPolicyNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPolicyNoKeyPressed
        // TODO add your handling code here:
        try {
            
            if(evt.getKeyCode()==112) {
                LOV aList=new LOV();
                
                aList.SQL="SELECT POLICY_ID,POLICY_NAME FROM D_SAL_POLICY_MASTER WHERE APPROVED = 1 AND CANCELLED=0 ORDER BY POLICY_ID";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                aList.UseSpecifiedConn=true;
                aList.dbURL=EITLERPGLOBAL.DatabaseURL;
                
                if(aList.ShowLOV()) {
                    txtPolicyNo.setText(aList.ReturnVal);
                    txtPolicyName.setText(clsPolicyMaster.getPolicyName(EITLERPGLOBAL.gCompanyID, txtPolicyNo.getText()));
                }
                
            }
            
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_txtPolicyNoKeyPressed
    
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
        if ( ! Validate()) {
            return;
        }
        
        Cancelled=false;
        
        strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
        
        if(!txtDocNo.getText().equals("")) {
            strQuery=strQuery+" AND DOC_NO='"+txtDocNo.getText().trim()+"' ";
        }
        
        if(!txtPolicyNo.getText().equals("")) {
            strQuery=strQuery+" AND POLICY_ID='"+txtPolicyNo.getText().trim()+"' ";
        }
        
        if(!txtFromDate.getText().equals("")) {
            strQuery=strQuery+" AND DOC_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
        }
        
        if(!txtToDate.getText().equals("")) {
            strQuery=strQuery+" AND DOC_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
        }
        
        strQuery=strQuery;
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtPolicyName;
    private javax.swing.JTextField txtPolicyNo;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    
    private boolean Validate() {
        //Form level validations
        if (! txtFromDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid From Date in DD/MM/YYYY format.");
                return false;
            }
        }
        
        if (! txtToDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid To Date in DD/MM/YYYY format.");
                return false;
            }
        }
        
        return true;
    }
}
