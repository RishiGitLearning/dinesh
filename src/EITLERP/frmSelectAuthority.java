/*
 * frmSelectAuthority.java
 *
 * Created on October 25, 2005, 2:31 PM
 */

package EITLERP;

/**
 *
 * @author  root
 */

import EITLERP.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class frmSelectAuthority extends javax.swing.JApplet {
    
    
    private EITLComboModel cmbAuthorityModel=new EITLComboModel();
    
    /** Initializes the applet frmSelectAuthority */
    public void init() {
        setSize(410,160);
        initComponents();
        GenerateCombo();
        EITLERPGLOBAL.setComboIndex(cmbAuthority,EITLERPGLOBAL.gAuthorityUserID);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmbAuthority = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Select the Authority");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 6, 144, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 2, 399, 26);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(290, 45, 95, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(290, 81, 95, 25);

        getContentPane().add(cmbAuthority);
        cmbAuthority.setBounds(14, 52, 240, 24);

    }//GEN-END:initComponents

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        EITLERPGLOBAL.gAuthorityUserID=EITLERPGLOBAL.getComboCode(cmbAuthority);
        
        System.out.println("ID:" + EITLERPGLOBAL.gAuthorityUserID);
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdOKActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbAuthority;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
    
    private void GenerateCombo() {
        //--- Module Combo ------//
        cmbAuthorityModel=new EITLComboModel();
        cmbAuthority.removeAllItems();
        cmbAuthority.setModel(cmbAuthorityModel);
        
        
        ComboData aData=new ComboData();
        aData.Text=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gUserID);
        aData.Code=EITLERPGLOBAL.gUserID;
        cmbAuthorityModel.addElement(aData);
        
        
        HashMap List=clsAuthority.getAvailableAuthority(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID);
        
        for(int i=1;i<=List.size();i++) {
            clsAuthority ObjAuthority=(clsAuthority) List.get(Integer.toString(i));
            
            //Check that Module Access Rights are given
            int AuthorityID=(int)ObjAuthority.getAttribute("AUTHORITY_USER_ID").getVal();
            
            //if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,MenuID)) {
            aData=new ComboData();
            aData.Text=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,AuthorityID);
            aData.Code=AuthorityID;
            cmbAuthorityModel.addElement(aData);
            //}
        }
        //===============================//
        
    }
    
}
