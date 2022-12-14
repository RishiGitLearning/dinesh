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
 

public class frmPolicyTargetMasterFind extends javax.swing.JApplet {
    
    public boolean Cancelled=false;
    public String strQuery;
    
    
    /** Initializes the applet frmGRNFind */
    public void init() {
        System.gc();
        setSize(730,300);
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
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtSeasonID = new javax.swing.JTextField();
        txtPartyID = new javax.swing.JTextField();
        txtSeasonName = new javax.swing.JTextField();
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
        cmdFind.setBounds(570, 40, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(570, 70, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(570, 110, 70, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Season ID");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(10, 20, 72, 15);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Party ID");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(10, 50, 72, 15);

        txtSeasonID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSeasonIDFocusLost(evt);
            }
        });
        txtSeasonID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSeasonIDKeyPressed(evt);
            }
        });

        jPanel1.add(txtSeasonID);
        txtSeasonID.setBounds(90, 20, 130, 19);

        txtPartyID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyIDFocusLost(evt);
            }
        });
        txtPartyID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyIDKeyPressed(evt);
            }
        });

        jPanel1.add(txtPartyID);
        txtPartyID.setBounds(90, 50, 130, 19);

        txtSeasonName.setEditable(false);
        jPanel1.add(txtSeasonName);
        txtSeasonName.setBounds(220, 20, 325, 19);

        txtPartyName.setEditable(false);
        jPanel1.add(txtPartyName);
        txtPartyName.setBounds(220, 50, 325, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 39, 550, 160);

    }//GEN-END:initComponents

    private void txtPartyIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyIDKeyPressed
        // TODO add your handling code here:
        try {
            
            if(evt.getKeyCode()==112) {
                LOV aList=new LOV();
                
                aList.SQL="SELECT PARTY_CODE,PARTY_NAME "+
                " FROM D_FIN_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 ORDER BY PARTY_CODE ";                
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                aList.UseSpecifiedConn=true;
                aList.dbURL=FinanceGlobal.FinURL;
                
                if(aList.ShowLOV()) {
                    txtPartyID.setText(aList.ReturnVal);
                    txtPartyName.setText(clsPolicyMaster.getPartyName(EITLERPGLOBAL.gCompanyID, txtPartyID.getText()));
                }                
            }
            
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtPartyIDKeyPressed

    private void txtSeasonIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSeasonIDKeyPressed
        // TODO add your handling code here:
        try {
            
            if(evt.getKeyCode()==112) {
                LOV aList=new LOV();
                
                aList.SQL="SELECT SEASON_ID,SEASON_NAME FROM D_SAL_SEASON_MASTER WHERE APPROVED = 1 AND CANCELLED=0 ORDER BY SEASON_ID";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                aList.UseSpecifiedConn=true;
                aList.dbURL=EITLERPGLOBAL.DatabaseURL;
                
                if(aList.ShowLOV()) {
                    txtSeasonID.setText(aList.ReturnVal);
                    txtSeasonName.setText(clsPolicyMaster.getSeasonName(EITLERPGLOBAL.gCompanyID, txtSeasonID.getText()));
                }
                
            }
            
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtSeasonIDKeyPressed

    private void txtPartyIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyIDFocusLost
        // TODO add your handling code here:
        if (! txtPartyID.getText().trim().equals("")) {
            txtPartyName.setText(clsPolicyMaster.getPartyName(EITLERPGLOBAL.gCompanyID, txtPartyID.getText()));
        }
    }//GEN-LAST:event_txtPartyIDFocusLost

    private void txtSeasonIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSeasonIDFocusLost
        // TODO add your handling code here:
        if (! txtSeasonID.getText().trim().equals("")) {
            txtSeasonName.setText(clsPolicyMaster.getSeasonName(EITLERPGLOBAL.gCompanyID, txtSeasonID.getText()));
        }
    }//GEN-LAST:event_txtSeasonIDFocusLost

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
       
       strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
       
       if(!txtPartyID.getText().equals(""))
       {
         strQuery=strQuery+" AND PARTY_ID='"+txtPartyID.getText()+"' ";
       }
       
       
       if(!txtSeasonID.getText().equals(""))
       {
         strQuery=strQuery+" AND SEASON_ID ='"+txtSeasonID.getText()+"' ";
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
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtPartyID;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtSeasonID;
    private javax.swing.JTextField txtSeasonName;
    // End of variables declaration//GEN-END:variables
    


}
